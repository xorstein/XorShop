package com.xorshop.admin.product;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.ui.Model;

import com.xorshop.admin.pagin.PagingAndSortingHelper;
import com.xorshop.common.entity.product.Product;

public interface IProductService {

	public List<Product> listAll();
	
	public Product save(Product product);
	
	public String checkUnique(Integer id, String name);
	
	public String checkUnique(Integer id, String name,String alias);
	
	public void updateProductEnabledStatus(Integer id, boolean enabled);
	
	public void delete(Integer id) throws ProductNotFoundException;
	
	public Product get(Integer id) throws ProductNotFoundException;
	
	public void listByPage(int pageNum, PagingAndSortingHelper helper, Integer categoryId);
	
	public void saveProductPrice(Product productInForm);
	
}
