package com.xorshop.controller;

import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xorshop.common.entity.Customer;
import com.xorshop.common.entity.VoteResultDTO;
import com.xorshop.common.entity.VoteType;
import com.xorshop.common.exception.CustomerNotFoundException;
import com.xorshop.service.ReviewVoteService;
import com.xorshop.util.AuthenticationControllerHelperUtil;

@RestController
public class ReviewVoteRestController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReviewVoteRestController.class);
	
	private ReviewVoteService service;
	
	private AuthenticationControllerHelperUtil helper;
	
	@Autowired
	public ReviewVoteRestController(ReviewVoteService service, AuthenticationControllerHelperUtil helper) {
		super();
		this.service = service;
		this.helper = helper;
	}


	@PostMapping("/vote_review/{id}/{type}")
	public VoteResultDTO voteReview(@PathVariable(name = "id") Integer reviewId,
			@PathVariable(name = "type") String type,
			HttpServletRequest request) throws CustomerNotFoundException {
		
		LOGGER.info("ReviewVoteRestController | voteReview is called");
		
		LOGGER.info("ReviewVoteRestController | voteReview | reviewId : " + reviewId);
		LOGGER.info("ReviewVoteRestController | voteReview | type : " + type);

		Customer customer = helper.getAuthenticatedCustomer(request);
		
		if (customer == null) {
			LOGGER.info("ReviewVoteRestController | voteReview | CustomerNotFoundException");
			return VoteResultDTO.fail("You must login to vote the review.");
		}
		
		VoteType voteType = VoteType.valueOf(type.toUpperCase());
		
		LOGGER.info("ReviewVoteRestController | voteReview | voteType : " + voteType);
		
		return service.doVote(reviewId, customer, voteType);
	}
}
