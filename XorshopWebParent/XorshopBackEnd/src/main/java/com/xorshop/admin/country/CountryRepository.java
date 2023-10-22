package com.xorshop.admin.country;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


import com.xorshop.common.entity.Country;
public interface CountryRepository extends CrudRepository<Country, Integer> {
	
	public List<Country> findAllByOrderByNameAsc();
	
	@Query("SELECT c FROM Country c WHERE c.name = :name")
	public Country findByName(String name);
}
