package com.xorshop.admin.role;



import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import org.springframework.stereotype.Repository;

import com.xorshop.admin.pagin.SearchRepository;
import com.xorshop.common.entity.Role;
import com.xorshop.common.entity.User;

import jakarta.transaction.Transactional;



@Repository
public interface RoleRepository extends CrudRepository<Role, Integer>,SearchRepository<Role, Integer> {

	
	
	
	@Query("select r FROM Role r WHERE CONCAT (r.id, ' ', r.name) LIKE %?1% ")
	public Page<Role> findAll(String keyword,Pageable pageable);
	
	@Query("SELECT r FROM Role r")
	public List<Role> findAll(Sort sort);
	

	

}
