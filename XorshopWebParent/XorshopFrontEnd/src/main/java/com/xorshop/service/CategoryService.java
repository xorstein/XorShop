package com.xorshop.service;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xorshop.common.entity.Category;
import com.xorshop.common.exception.CategoryNotFoundException;
import com.xorshop.repository.CategoryRepository;
import com.xorshop.service.impl.ICategoryService;
@Service
@Transactional
public class CategoryService implements ICategoryService{

	@Autowired 
	private CategoryRepository repo;
	
	public CategoryService(CategoryRepository repo) {
		super();
		this.repo = repo;
	}

	@Override
	public List<Category> listNoChildrenCategories() {
		
		List<Category> listNoChildrenCategories = new ArrayList<>();

		List<Category> listEnabledCategories = repo.findAllEnabled();

		listEnabledCategories.forEach(category -> {
			Set<Category> children = category.getChildren();
			if (children == null || children.size() == 0) {
				listNoChildrenCategories.add(category);
			}
		});

		return listNoChildrenCategories;
	}
	
	@Override
	public Category getCategory(String alias) throws CategoryNotFoundException {
		Category category = repo.findByAliasEnabled(alias);
		if (category == null) {
			throw new CategoryNotFoundException("Impossible de trouver la catégorie avec l'alias " + alias);
		}

		return category;
	}

	@Override
	public List<Category> getCategoryParents(Category child) {
		List<Category> listParents = new ArrayList<>();

		Category parent = child.getParent();

		while (parent != null) {
			listParents.add(0, parent);
			parent = parent.getParent();
		}

		listParents.add(child);

		return listParents;
	}

}
