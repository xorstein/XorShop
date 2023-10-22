package com.xorshop.admin.user;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.xorshop.admin.pagin.SearchRepository;
import com.xorshop.common.entity.User;


@Repository
public interface UserRepository extends CrudRepository<User, Integer>, SearchRepository<User, Integer>  {
	@Query("SELECT u FROM User u WHERE u.emailid = :emailid")
	public User getUserByEmail(@Param("emailid") String email);
	
	@Query("SELECT COUNT(*) FROM User u WHERE u.delete_status = false AND u.id LIKE %?1%")
	public Long countById(Integer id);
	
	@Query("UPDATE User u SET u.enabled = ?2 WHERE u.id = ?1 and u.delete_status= false")
	@Modifying
	public void updateEnabledStatus(Integer id, boolean enabled);
	
	@Query("SELECT u FROM User u WHERE CONCAT(u.id, ' ', u.emailid, ' ', u.firstname, ' ',"
			+ " u.lastname) LIKE %?1% and u.delete_status= false")
	public Page<User> findAll(String keyword, Pageable pageable);
	
	@Query("SELECT u FROM User u WHERE CONCAT(u.id, ' ', u.emailid, ' ', u.firstname, ' ',"
			+ " u.lastname) LIKE %?1% and u.delete_status= false")
	public List<User> findAll(String keyword,Sort sort);
	
	@Query("SELECT u FROM User u WHERE u.delete_status= false")
	public List<User> findAll(Sort sort);
	
	@Query("SELECT u FROM User u WHERE u.delete_status= false")
	public Page<User> findAll(Pageable pageable);
	
	@Query("SELECT u FROM User u WHERE u.delete_status = false AND u.id = ?1")
	public User findByUserId(Integer id);
	
	
	
	

	
	
	


}
