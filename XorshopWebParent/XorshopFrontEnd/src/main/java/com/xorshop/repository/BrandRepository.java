package com.xorshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xorshop.common.entity.Brand;

public interface BrandRepository extends JpaRepository<Brand, Integer> {

}
