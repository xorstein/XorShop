package com.xorshop.service.impl;

import java.util.List;

import com.xorshop.common.entity.Category;
import com.xorshop.common.exception.CategoryNotFoundException;

public interface ICategoryService {

	public List<Category> listNoChildrenCategories();
	
	public Category getCategory(String alias) throws CategoryNotFoundException;
	
	public List<Category> getCategoryParents(Category child);
}
