package com.xorshop.admin.user;
import java.util.List;

import org.springframework.data.domain.Page;

import com.xorshop.admin.pagin.PagingAndSortingHelper;
import com.xorshop.common.entity.Role;
import com.xorshop.common.entity.User;
import com.xorshop.common.exception.UserNotFoundException;
public interface IUserService {
    public List<User> listAll(String keyword);
	
	public List<Role> listRoles();
	
	public User save(User user);
	
	public boolean isEmailUnique(Integer id, String email);
	
	public User get(Integer id) throws UserNotFoundException;
	
	public void delete(Integer id) throws UserNotFoundException;
	
	public void updateUserEnabledStatus(Integer id, boolean enabled);
	
	public void listByPage(int pageNum, PagingAndSortingHelper helper);
	
	public User getByEmail(String email);
	
	public User updateAccount(User userInForm);

}
