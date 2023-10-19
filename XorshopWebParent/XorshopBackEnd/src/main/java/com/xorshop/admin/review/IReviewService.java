package com.xorshop.admin.review;

import com.xorshop.common.entity.Review;
import com.xorshop.common.exception.ReviewNotFoundException;

public interface IReviewService {

	public Review get(Integer id) throws ReviewNotFoundException;
	public void save(Review reviewInForm);
	public void delete(Integer id) throws ReviewNotFoundException;
}
