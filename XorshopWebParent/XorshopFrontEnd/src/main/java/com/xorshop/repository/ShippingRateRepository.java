package com.xorshop.repository;

import org.springframework.data.repository.CrudRepository;

import com.xorshop.common.entity.Country;
import com.xorshop.common.entity.ShippingRate;

public interface ShippingRateRepository extends CrudRepository<ShippingRate, Integer> {

	public ShippingRate findByCountryAndState(Country country, String state);
	
}