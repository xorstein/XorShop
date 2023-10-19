package com.xorshop.admin.question;


import java.util.Date;
import java.util.NoSuchElementException;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.xorshop.admin.pagin.PagingAndSortingHelper;
import com.xorshop.common.entity.User;
import com.xorshop.common.entity.question.Question;
import com.xorshop.common.exception.QuestionNotFoundException;

@Service
@Transactional
public class QuestionService implements IQuestionService{

	public static final int QUESTIONS_PER_PAGE = 10;
	
	private final QuestionRepository repo;
	
	public QuestionService(QuestionRepository repo) {
		super();
		this.repo = repo;
	}

	@Override
	public void listByPage(int pageNum, PagingAndSortingHelper helper) {
		// TODO Auto-generated method stub
		helper.listEntities(pageNum, QUESTIONS_PER_PAGE, repo);
	}

	@Override
	public Question getQuestionById(Integer id) throws QuestionNotFoundException {
		// TODO Auto-generated method stub
		try {
			return repo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new QuestionNotFoundException("Impossibe de trouver la question avec le numéro  " + id);
		}
	}

	@Override
	public void saveQuestionByUser(Question questionInForm, User user) throws QuestionNotFoundException {
		// TODO Auto-generated method stub
		try {
			Question questionInDB = repo.findById(questionInForm.getId()).get();
			questionInDB.setQuestionContent(questionInForm.getQuestionContent());
			questionInDB.setAnswer(questionInForm.getAnswer());
			questionInDB.setApproved(questionInForm.isApproved());

			if (questionInDB.isAnswered()) {
				questionInDB.setAnswerTime(new Date());
				questionInDB.setAnswerer(user);
			}

			repo.save(questionInDB);

		} catch (NoSuchElementException ex) {
			throw new QuestionNotFoundException("Impossibe de trouver la question avec le numéro " + questionInForm.getId());
		}
	}

	@Override
	public void approve(Integer id) {
		// TODO Auto-generated method stub
		repo.updateApprovalStatus(id, true);
	}

	@Override
	public void disapprove(Integer id) {
		// TODO Auto-generated method stub
		repo.updateApprovalStatus(id, false);
	}

	@Override
	public void delete(Integer id) throws QuestionNotFoundException {
		// TODO Auto-generated method stub
		if (!repo.existsById(id)) {
			throw new QuestionNotFoundException("Could not find any question with ID " + id);
		}
		repo.deleteById(id);
	}
	
}
