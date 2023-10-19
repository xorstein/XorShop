package com.xorshop.service;

import java.util.List;
import java.util.NoSuchElementException;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.xorshop.common.entity.Customer;
import com.xorshop.common.entity.VoteResultDTO;
import com.xorshop.common.entity.VoteType;
import com.xorshop.common.entity.question.Question;
import com.xorshop.common.entity.question.QuestionVote;
import com.xorshop.repository.QuestionRepository;
import com.xorshop.repository.QuestionVoteRepository;
import com.xorshop.service.impl.IQuestionVoteService;

@Service
@Transactional
public class QuestionVoteService implements IQuestionVoteService{

	private QuestionVoteRepository voteRepo;

	private QuestionRepository questionRepo;

	
	public QuestionVoteService(QuestionVoteRepository voteRepo, QuestionRepository questionRepo) {
		super();
		this.voteRepo = voteRepo;
		this.questionRepo = questionRepo;
	}


	@Override
	public VoteResultDTO doVote(Integer questionId, Customer customer, VoteType voteType) {
		// TODO Auto-generated method stub
		Question question = null;

		try {
			question = questionRepo.findById(questionId).get();
		} catch (NoSuchElementException ex) {
			return VoteResultDTO.fail("La question numéro " + questionId + " est introuvable.");
		}

		QuestionVote vote = voteRepo.findByQuestionAndCustomer(questionId, customer.getId());

		if (vote != null) {
			if (vote.isUpvoted() && voteType.equals(VoteType.UP) || 
					vote.isDownvoted() && voteType.equals(VoteType.DOWN)) {
				return undoVote(vote, questionId, voteType);
			} else if (vote.isUpvoted() && voteType.equals(VoteType.DOWN)) {
				vote.voteDown();
			} else if (vote.isDownvoted() && voteType.equals(VoteType.UP)) {
				vote.voteUp();
			}			
		} else {
			vote = new QuestionVote();
			vote.setQuestion(question);
			vote.setCustomer(customer);

			if (voteType.equals(VoteType.UP)) {
				vote.voteUp();
			} else {
				vote.voteDown();
			}			
		}

		voteRepo.save(vote);
		questionRepo.updateVoteCount(questionId);
		Integer voteCount = questionRepo.getVoteCount(questionId);

		return VoteResultDTO.success("You have successfully voted " + voteType + " that question.", 
				voteCount);
	}


	@Override
	public VoteResultDTO undoVote(QuestionVote vote, Integer questionId, VoteType voteType) {
		// TODO Auto-generated method stub
		voteRepo.delete(vote);
		questionRepo.updateVoteCount(questionId);
		Integer voteCount = questionRepo.getVoteCount(questionId);

		return VoteResultDTO.success("You have unvoted " + voteType + " that question.", voteCount);
	}


	@Override
	public void markQuestionsVotedForProductByCustomer(List<Question> listQuestions, Integer productId,
			Integer customerId) {
		// TODO Auto-generated method stub
		List<QuestionVote> listVotes = voteRepo.findByProductAndCustomer(productId, customerId);

		for (QuestionVote aVote : listVotes) {
			Question votedQuestion = aVote.getQuestion();
			if (listQuestions.contains(votedQuestion)) {
				int index = listQuestions.indexOf(votedQuestion);
				Question question = listQuestions.get(index);

				if (aVote.isUpvoted()) {
					question.setUpvotedByCurrentCustomer(true);
				} else if (aVote.isDownvoted()) {
					question.setDownvotedByCurrentCustomer(true);
				}
			}
		}
	}
	
	
}
