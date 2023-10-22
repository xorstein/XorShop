package com.xorshop.common.entity.section;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import com.xorshop.common.entity.Category;
import com.xorshop.common.entity.IdBasedEntity;



@Entity
@Table(name = "sections_categories")

public class CategorySection extends IdBasedEntity {

	@Column(name = "category_order")
	private int categoryOrder;

	@ManyToOne
	@JoinColumn(name = "category_id")	
	private Category category;
	public CategorySection() {
		
	}
	public int getCategoryOrder() {
		return categoryOrder;
	}
	public void setCategoryOrder(int categoryOrder) {
		this.categoryOrder = categoryOrder;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
    
}