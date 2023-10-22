package com.xorshop.admin.role;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.xorshop.admin.pagin.PagingAndSortingHelper;
import com.xorshop.common.entity.Role;

import jakarta.transaction.Transactional;



@Service
@Transactional
public class RoleService  implements IRoleService{ 
	public static final int ROLE_PER_PAGE = 3;

	@Autowired 
	private RoleRepository reporole;
	
	@Override
	public void listByPage(int pageNum, PagingAndSortingHelper helper) {
		helper.listEntities(pageNum, ROLE_PER_PAGE, reporole);
		
	}

	@Override
	public List<Role> findAll() {
		// TODO Auto-generated method stub
		return (List<Role>) reporole.findAll();
	}
}
