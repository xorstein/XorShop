package com.xorshop.service.impl;

import java.util.List;

import com.xorshop.common.entity.article.Article;
import com.xorshop.common.entity.menu.Menu;
import com.xorshop.common.exception.MenuItemNotFoundException;

public interface IMenuService {

	public List<Menu> getHeaderMenuItems();
	public List<Menu> getFooterMenuItems();
	public Article getArticleBoundToMenu(String menuAlias) throws MenuItemNotFoundException;
}
