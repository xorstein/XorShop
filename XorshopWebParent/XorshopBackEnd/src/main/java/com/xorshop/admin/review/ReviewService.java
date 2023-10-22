package com.xorshop.admin.review;

import java.util.NoSuchElementException;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xorshop.admin.pagin.PagingAndSortingHelper;
import com.xorshop.admin.product.ProductRepository;
import com.xorshop.common.entity.Review;
import com.xorshop.common.exception.ReviewNotFoundException;

@Service
@Transactional
public class ReviewService implements IReviewService{
	
	public static final int REVIEWS_PER_PAGE = 5;

	
	private ReviewRepository reviewRepo;
	
	private ProductRepository productRepo;
	
	@Autowired 
	public ReviewService(ReviewRepository reviewRepo, ProductRepository productRepo) {
		super();
		this.reviewRepo = reviewRepo;
		this.productRepo = productRepo;
	}

	public void listByPage(int pageNum, PagingAndSortingHelper helper) {
		helper.listEntities(pageNum, REVIEWS_PER_PAGE, reviewRepo);
	}

	@Override
	public Review get(Integer id) throws ReviewNotFoundException {
		// TODO Auto-generated method stub
		try {
			return reviewRepo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new ReviewNotFoundException("Impossible de trouver des avis avec le numéro " + id);
		}
	}

	@Override
	public void save(Review reviewInForm) {
		// TODO Auto-generated method stub
		Review reviewInDB = reviewRepo.findById(reviewInForm.getId()).get();
		reviewInDB.setHeadline(reviewInForm.getHeadline());
		reviewInDB.setComment(reviewInForm.getComment());

		reviewRepo.save(reviewInDB);
		productRepo.updateReviewCountAndAverageRating(reviewInDB.getProduct().getId());
	}

	@Override
	public void delete(Integer id) throws ReviewNotFoundException {
		// TODO Auto-generated method stub
		if (!reviewRepo.existsById(id)) {
			throw new ReviewNotFoundException("Impossible de trouver des avis avec le numéro " + id);
		}

		reviewRepo.deleteById(id);
	}

}
