package com.xorshop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xorshop.common.entity.article.Article;
import com.xorshop.common.entity.menu.Menu;
import com.xorshop.common.entity.menu.MenuType;
import com.xorshop.common.exception.MenuItemNotFoundException;
import com.xorshop.repository.MenuRepository;
import com.xorshop.service.impl.IMenuService;

@Service
public class MenuService implements IMenuService{
	
	@Autowired 
	private MenuRepository repo;

	@Override
	public List<Menu> getHeaderMenuItems() {
		// TODO Auto-generated method stub
		return repo.findByTypeAndEnabledOrderByPositionAsc(MenuType.HEADER, true);
	}

	@Override
	public List<Menu> getFooterMenuItems() {
		// TODO Auto-generated method stub
		return repo.findByTypeAndEnabledOrderByPositionAsc(MenuType.FOOTER, true);
	}

	@Override
	public Article getArticleBoundToMenu(String menuAlias) throws MenuItemNotFoundException {
		// TODO Auto-generated method stub
		Menu menu = repo.findByAlias(menuAlias);
		if (menu == null) {
			throw new MenuItemNotFoundException("Aucun menu n'a été trouvé avec l'alias " + menuAlias);
		}

		return menu.getArticle();
	}

	public List<Menu> getHeaderMenuTopItems() {
		// TODO Auto-generated method stub
				return repo.findByTypeAndEnabledOrderByPositionAsc(MenuType.HEADER_TOP, true);
	}

}
