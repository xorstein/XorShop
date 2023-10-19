package com.xorshop.controller;

import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xorshop.common.entity.Customer;
import com.xorshop.common.entity.VoteResultDTO;
import com.xorshop.common.entity.VoteType;
import com.xorshop.service.QuestionVoteService;
import com.xorshop.util.AuthenticationControllerHelperUtil;

@RestController
public class QuestionVoteRestController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(QuestionVoteRestController.class);

	private AuthenticationControllerHelperUtil authenticationControllerHelperUtil;

	private QuestionVoteService service;
	
	public QuestionVoteRestController(AuthenticationControllerHelperUtil authenticationControllerHelperUtil,
			QuestionVoteService service) {
		super();
		this.authenticationControllerHelperUtil = authenticationControllerHelperUtil;
		this.service = service;
	}

	@PostMapping("/vote_question/{id}/{type}")
	public VoteResultDTO voteQuestion(@PathVariable(name = "id") Integer questionId,
			@PathVariable(name = "type") String type, HttpServletRequest request) {

		LOGGER.info("QuestionController | voteQuestion is called");
		
		Customer customer = authenticationControllerHelperUtil.getAuthenticatedCustomer(request);
		
		LOGGER.info("QuestionController | voteQuestion | customer : " + customer.getFullName());
		
		if (customer == null) {
			return VoteResultDTO.fail("Vous devez vous connecter pour voter sur la question.");
		}

		VoteType voteType = VoteType.valueOf(type.toUpperCase());

		return service.doVote(questionId, customer, voteType);		
	}

}
