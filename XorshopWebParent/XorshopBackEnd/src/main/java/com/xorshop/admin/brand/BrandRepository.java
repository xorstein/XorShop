package com.xorshop.admin.brand;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.xorshop.admin.pagin.SearchRepository;
import com.xorshop.common.entity.Brand;
import com.xorshop.common.entity.User;

@Repository
public interface BrandRepository  extends CrudRepository<Brand, Integer>, SearchRepository<Brand, Integer> {

	
public Long countById(Integer id);
	
	public Brand findByName(String name);
	
	@Query("SELECT b FROM Brand b WHERE b.name LIKE %?1%")
	public Page<Brand> findAll(String keyword, Pageable pageable);
	
	// NEW -> new anonymous type with only the properties you need
	@Query("SELECT NEW Brand(b.id, b.name) FROM Brand b ORDER BY b.name ASC")
	public List<Brand> findAll();

}
