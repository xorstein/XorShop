package com.xorshop.admin.product;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.xorshop.common.entity.Brand;
import com.xorshop.common.entity.Category;
import com.xorshop.common.entity.product.Product;


	@Repository
	public interface ProductRepository  extends CrudRepository<Product, Integer>,PagingAndSortingRepository<Product, Integer> {
		
		public Product findByName(String name);
		
		@Query("UPDATE Product p SET p.enabled = ?2 WHERE p.id = ?1")
		@Modifying
		public void updateEnabledStatus(Integer id, boolean enabled);
		
		public Long countById(Integer id);
		
		
		@Query("SELECT p FROM Product p WHERE p.name LIKE %?1% " 
				+ "OR p.shortDescription LIKE %?1% "
				+ "OR p.fullDescription LIKE %?1% "
				+ "OR p.brand.name LIKE %?1% "
				+ "OR p.category.name LIKE %?1%")
		public Page<Product> findAll(String keyword, Pageable pageable);
		
		@Query("SELECT p FROM Product p WHERE p.category.id = ?1 "
				+ "OR p.category.allParentIDs LIKE %?2%")	
		public Page<Product> findAllInCategory(Integer categoryId, String categoryIdMatch, 
				Pageable pageable);

		@Query("SELECT p FROM Product p WHERE (p.category.id = ?1 "
				+ "OR p.category.allParentIDs LIKE %?2%) AND "
				+ "(p.name LIKE %?3% " 
				+ "OR p.shortDescription LIKE %?3% "
				+ "OR p.fullDescription LIKE %?3% "
				+ "OR p.brand.name LIKE %?3% "
				+ "OR p.category.name LIKE %?3%)")			
		public Page<Product> searchInCategory(Integer categoryId, String categoryIdMatch, 
				String keyword, Pageable pageable);
		
		@Query("SELECT p FROM Product p WHERE p.name LIKE %?1%")
		public Page<Product> searchProductsByName(String keyword, Pageable pageable);
		
		
		
		@Query("select c from Product c WHERE c.alias= ?1")
		public Product findByAlias(String alias);
	
		@Query("UPDATE Product p SET p.averageRating = COALESCE(CAST((SELECT AVG(r.rating) FROM Review r WHERE r.product.id = ?1) AS Float), 0), p.reviewCount = (SELECT COUNT(r.id) FROM Review r WHERE r.product.id = ?1) WHERE p.id = ?1")
		@Modifying
		public void updateReviewCountAndAverageRating(Integer productId);
		
		@Query("SELECT p FROM Product p ORDER BY p.averageRating DESC LIMIT 10")
	    public List<Product> findTop10ByOrderByAverageRatingDesc();
		
	
}
