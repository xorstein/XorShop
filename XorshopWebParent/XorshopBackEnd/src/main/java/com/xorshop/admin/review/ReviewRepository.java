package com.xorshop.admin.review;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.xorshop.admin.pagin.SearchRepository;
import com.xorshop.common.entity.Brand;
import com.xorshop.common.entity.Review;

public interface ReviewRepository extends CrudRepository<Review, Integer>,SearchRepository<Review, Integer> {

	@Query("SELECT r FROM Review r WHERE "
			+ "r.headline LIKE %?1% OR "
			+ "r.comment LIKE %?1% OR r.product.name LIKE %?1% OR "
			+ "CONCAT(r.customer.firstName, ' ', r.customer.lastName) LIKE %?1%")
	public Page<Review> findAll(String keyword, Pageable pageable);

	public List<Review> findAll();
}
