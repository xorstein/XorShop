package com.xorshop.admin.category;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xorshop.common.entity.Category;


@RestController
public class CategoryRestController {
	@Autowired
	private CategoryService service;
	
	@PostMapping("/categories/check_unique")
	public String CheckUnique(@Param("id")Integer id,@Param("name")String name,@Param("alias")String alias) {
		return service.checkUnique(id,name,alias) ;
		
	}

}
