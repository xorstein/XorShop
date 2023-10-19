package com.xorshop.admin.brand;

import java.util.List;

import com.xorshop.admin.pagin.PagingAndSortingHelper;
import com.xorshop.common.entity.Brand;
import com.xorshop.common.exception.BrandNotFoundException;

public interface IBrandService {

	public List<Brand> listAll();
	public Brand save(Brand brand);
	public Brand get(Integer id) throws BrandNotFoundException;
	public void delete(Integer id) throws BrandNotFoundException;
	public String checkUnique(Integer id, String name);
	public void listByPage(int pageNum, PagingAndSortingHelper helper);
}
