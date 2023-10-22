package com.xorshop.common.entity;

import java.util.HashSet;
import java.util.Set;

import com.xorshop.common.constants.Constants;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "brands")
public class Brand {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column( length = 45, nullable = false,unique=true)
	private String name;
	
	@Column( length = 128, nullable = false)
	private String logo;
	@ManyToMany
	@JoinTable(
			name="brands_categories",
			joinColumns = @JoinColumn(name="brand_id"),
			inverseJoinColumns = @JoinColumn(name="category_id")
			)
	private Set<Category> categories =new HashSet<>();
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public Set<Category> getCategories() {
		return categories;
	}
	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}
	public Brand() {
		
	}
	public Brand(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	public Brand(String name) {
		this.name = name;
		this.logo="brand-logo.png";
	}
	public Brand(Integer brandId) {
		this.id=brandId;
		this.logo="brand-logo.png";
		
	}
	@Override
	public String toString() {
		return "Brand [id=" + id + ", name=" + name + ", categories=" + categories + "]";
	}
	@Transient
	public String getBrandLogoPath() {
		if(logo ==null || logo.equals("brand-logo.png")) return "/assets/media/images/image-thumbnail.png";
		else 
			return Constants.S3_BASE_URI +"/brand-images/"+this.id+"/"+this.logo;
	}
	

}
