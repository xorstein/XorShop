package com.xorshop.admin.menu;

import java.util.List;


import com.xorshop.admin.util.MenuMoveDirection;
import com.xorshop.common.entity.menu.Menu;
import com.xorshop.common.exception.MenuItemNotFoundException;

public interface IMenuService{

	public List<Menu> listAll();
	public void save(Menu menu);
	public Menu get(Integer id) throws MenuItemNotFoundException;
	public void updateEnabledStatus(Integer id, boolean enabled) throws MenuItemNotFoundException;
	public void delete(Integer id) throws MenuItemNotFoundException;
	public void moveMenu(Integer id,MenuMoveDirection direction) throws MenuUnmoveableException, MenuItemNotFoundException;
}
