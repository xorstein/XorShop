package com.xorshop.service.impl;

import com.xorshop.common.entity.Customer;
import com.xorshop.common.entity.ReviewVote;
import com.xorshop.common.entity.VoteResultDTO;
import com.xorshop.common.entity.VoteType;

public interface IReviewVoteService {

	public VoteResultDTO undoVote(ReviewVote vote, Integer reviewId, VoteType voteType);
	public VoteResultDTO doVote(Integer reviewId, Customer customer, VoteType voteType);
	
}
