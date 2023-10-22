package com.xorshop.admin.exporter;

public class BrandCsv {
	  private Integer id;
      private String name;
      private String categories;
	public Integer getId() {
		return id;
	}
	public BrandCsv(Integer id, String name, String categories) {
		this.id = id;
		this.name = name;
		this.categories = categories;
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
	public String getCategories() {
		return categories;
	}
	public void setCategories(String categories) {
		this.categories = categories;
	}
}
