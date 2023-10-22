package com.xorshop.admin.role;

import java.util.List;

import com.xorshop.admin.pagin.PagingAndSortingHelper;
import com.xorshop.common.entity.Role;

public interface IRoleService {
	
	public void listByPage(int pageNum, PagingAndSortingHelper helper);
	
	public List<Role> findAll();

}
