package com.xorshop.admin.brand;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xorshop.admin.dto.CategoryDTO;
import com.xorshop.common.entity.Brand;
import com.xorshop.common.entity.Category;
import com.xorshop.common.exception.BrandNotFoundException;

@RestController
public class BrandRestController {
	@Autowired
	private BrandService service;
	@PostMapping("/brands/check_unique")
	public String CheckDuplicateName(@Param("name")String name,@Param("id")Integer id) {
		return service.checkUnique(id,name) ;
		
	}
	@GetMapping("/brands/{id}/categories")
	public List<CategoryDTO> listCategoriesByBrand(@PathVariable(name="id")Integer id) throws BrandNotFoundRestException{
		List<CategoryDTO> listCategories= new ArrayList<>();
		try {
			Brand brand= service.get(id);
			Set <Category> categories=brand.getCategories();
			for(Category category :categories) {
				CategoryDTO cDTO= new CategoryDTO(category.getId(),category.getName());
				listCategories.add(cDTO);
			}
			return listCategories;
			
		} catch (BrandNotFoundException e) {
			// TODO Auto-generated catch block
			throw new BrandNotFoundRestException();
		}
	}

}
