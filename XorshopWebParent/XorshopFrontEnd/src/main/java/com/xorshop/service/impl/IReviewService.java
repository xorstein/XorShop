package com.xorshop.service.impl;

import org.springframework.data.domain.Page;

import com.xorshop.common.entity.Customer;
import com.xorshop.common.entity.Review;
import com.xorshop.common.exception.ReviewNotFoundException;

public interface IReviewService {

	public Page<Review> listByCustomerByPage(Customer customer, String keyword, int pageNum, 
			String sortField, String sortDir);
	
	public Review getByCustomerAndId(Customer customer, Integer reviewId) throws ReviewNotFoundException;
}
