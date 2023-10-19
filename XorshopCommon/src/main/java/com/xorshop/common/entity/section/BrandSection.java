package com.xorshop.common.entity.section;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import com.xorshop.common.entity.Brand;
import com.xorshop.common.entity.IdBasedEntity;



@Entity
@Table(name = "sections_brands")
public class BrandSection extends IdBasedEntity {

	@Column(name = "brand_order")
	private int brandOrder;

	@ManyToOne
	@JoinColumn(name = "brand_id")
	private Brand brand;
	public BrandSection() {
		
	}
	public int getBrandOrder() {
		return brandOrder;
	}
	public void setBrandOrder(int brandOrder) {
		this.brandOrder = brandOrder;
	}
	public Brand getBrand() {
		return brand;
	}
	public void setBrand(Brand brand) {
		this.brand = brand;
	}

}
