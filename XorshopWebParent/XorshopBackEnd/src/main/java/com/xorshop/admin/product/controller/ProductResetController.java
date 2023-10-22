package com.xorshop.admin.product.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xorshop.admin.dto.ProductDTO;
import com.xorshop.admin.product.ProductNotFoundException;
import com.xorshop.admin.product.ProductService;
import com.xorshop.common.entity.product.Product;

@RestController
public class ProductResetController {
	@Autowired
	private ProductService service;
	@PostMapping("/products/check_unique")
	public String CheckUnique(@Param("id")Integer id,@Param("name")String name,@Param("alias")String alias) {
		return service.checkUnique(id,name,alias) ;
		
	}
	@GetMapping("/products/get/{id}")
	public ProductDTO getProductInfo(@PathVariable("id") Integer id) 
			throws ProductNotFoundException {
		
		Product product = service.get(id);
		
		return new ProductDTO(product.getName(), product.getMainImagePath(), 
				product.getDiscountPrice(), product.getCost());
	}

}
