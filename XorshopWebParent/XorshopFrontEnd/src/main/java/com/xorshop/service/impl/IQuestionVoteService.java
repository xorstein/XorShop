package com.xorshop.service.impl;

import java.util.List;

import com.xorshop.common.entity.Customer;
import com.xorshop.common.entity.VoteResultDTO;
import com.xorshop.common.entity.VoteType;
import com.xorshop.common.entity.question.Question;
import com.xorshop.common.entity.question.QuestionVote;

public interface IQuestionVoteService {

	public VoteResultDTO doVote(Integer questionId, Customer customer, VoteType voteType);
	public VoteResultDTO undoVote(QuestionVote vote, Integer questionId, VoteType voteType);
	public void markQuestionsVotedForProductByCustomer(List<Question> listQuestions, 
			Integer productId, Integer customerId);
	
	
}
