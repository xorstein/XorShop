package com.xorshop.admin.question;


import com.xorshop.admin.pagin.PagingAndSortingHelper;
import com.xorshop.common.entity.User;
import com.xorshop.common.entity.question.Question;
import com.xorshop.common.exception.QuestionNotFoundException;


public interface IQuestionService {

	public void listByPage(int pageNum, PagingAndSortingHelper helper);
	public Question getQuestionById(Integer id) throws QuestionNotFoundException;
	public void saveQuestionByUser(Question questionInForm, User user) throws QuestionNotFoundException;
	public void approve(Integer id);
	public void disapprove(Integer id);
	public void delete(Integer id) throws QuestionNotFoundException;
}
