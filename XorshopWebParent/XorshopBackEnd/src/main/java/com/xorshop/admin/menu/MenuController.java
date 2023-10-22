package com.xorshop.admin.menu;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.xorshop.admin.article.ArticleService;
import com.xorshop.admin.util.MenuMoveDirection;
import com.xorshop.common.entity.article.Article;
import com.xorshop.common.entity.menu.Menu;
import com.xorshop.common.exception.MenuItemNotFoundException;

@Controller
public class MenuController {

	private static final Logger LOGGER = LoggerFactory.getLogger(MenuController.class);
	
	private final String defaultRedirectURL = "redirect:/menus";
	
	private MenuService menuService;
	
	private ArticleService articleService;
	
	@Autowired
	public MenuController(MenuService menuService, ArticleService articleService) {
		super();
		this.menuService = menuService;
		this.articleService = articleService;
	}

	@GetMapping("/menus")
	public String listAll(Model model) {
		
		LOGGER.info("MenuController | listAll is called");
		List<Menu> listMenuItems = menuService.listAll();
		String module = (listMenuItems.size() <= 1) ? "menu" : "menus";
		LOGGER.info("MenuController | listAll | listMenuItems size :" + listMenuItems.size());
		model.addAttribute("listMenuItems", listMenuItems);
		model.addAttribute("pageTitle", "Menus");
		model.addAttribute("activelink", "15");
		model.addAttribute("moduleURL", module);
		return "menus/menu_items";
	}
	
	@GetMapping("menus/new")
	public String newMenu(Model model) {
		
		LOGGER.info("MenuController | newMenu is called");
		
		List<Article> listArticles = articleService.listArticlesForMenu();
		
		LOGGER.info("MenuController | newMenu | listArticles : " + listArticles.toString());
        Menu menu= new Menu();
        menu.setEnabled(true);
		model.addAttribute("menu",menu);
		model.addAttribute("listArticles", listArticles);
		model.addAttribute("activelink", "15");
		model.addAttribute("pageTitle", "Créer un nouvel élément de menu");
		
		LOGGER.info("MenuController | newMenu | pageTitle : " + "Create New Menu Item");

		return "menus/menu_form";
	}

	@PostMapping("/menus/save")
	public String saveMenu(Menu menu, RedirectAttributes ra) {
		
		LOGGER.info("MenuController | saveMenu is called");
		
		menuService.save(menu);
		
		ra.addFlashAttribute("message", "L'élément de menu a été enregistré avec succès.");
		
		LOGGER.info("MenuController | saveMenu | message : " + "The menu item has been saved successfully.");

		return defaultRedirectURL;
	}
	
	@GetMapping("/menus/edit/{id}")
	public String editMenu(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes ra) {
		
		LOGGER.info("MenuController | editMenu is called");
		LOGGER.info("MenuController | editMenu | id : " + id);
		
		try {
			Menu menu = menuService.get(id);
			
			LOGGER.info("MenuController | editMenu | id : " + id);
			
			List<Article> listArticles = articleService.listArticlesForMenu();
			
			LOGGER.info("MenuController | editMenu | listArticles : " + listArticles.toString());
			
			LOGGER.info("MenuController | editMenu | menu : " + menu.toString());
			model.addAttribute("activelink", "15");
			model.addAttribute("menu", menu);
			model.addAttribute("listArticles", listArticles);
			model.addAttribute("pageTitle", "Modifier l'élément de menu (Numéro: " + id + ")");

			return "menus/menu_form";
		} catch (MenuItemNotFoundException ex) {
			ra.addFlashAttribute("error", ex.getMessage());
			LOGGER.info("MenuController | editMenu | error : " + ex.getMessage());
			return defaultRedirectURL;
		}
	}
	
	@GetMapping("/menus/{id}/enabled/{enabledStatus}")
	public String updateMenuEnabledStatus(@PathVariable("id") Integer id, 
			@PathVariable("enabledStatus") String enabledStatus, RedirectAttributes ra) {
		
		LOGGER.info("MenuController | updateMenuEnabledStatus is called");
		LOGGER.info("MenuController | updateMenuEnabledStatus | id : " + id);
		LOGGER.info("MenuController | updateMenuEnabledStatus | enabledStatus : " + enabledStatus);
		
		try {
			boolean enabled = Boolean.parseBoolean(enabledStatus);
			
			LOGGER.info("MenuController | updateMenuEnabledStatus | enabled : " + enabled);
			
			menuService.updateEnabledStatus(id, enabled);		

			String updateResult = enabled ? "activé." : "désactivé.";
			
			LOGGER.info("MenuController | updateMenuEnabledStatus | updateResult : " + updateResult);
			
			ra.addFlashAttribute("message", "L'élément de menu numéro " + id + " a été " + updateResult);
			
			LOGGER.info("MenuController | updateMenuEnabledStatus | message : " + "The menu item ID " + id + " has been " + updateResult);
			
		} catch (MenuItemNotFoundException ex) {
			LOGGER.info("MenuController | updateMenuEnabledStatus | error : " + ex.getMessage());
			
			ra.addFlashAttribute("error", ex.getMessage());
		}

		return defaultRedirectURL;
	}
	
	
	@GetMapping("/menus/delete/{id}")
	public String deleteMenu(@PathVariable(name = "id") Integer id, RedirectAttributes ra) {
		
		LOGGER.info("MenuController | deleteMenu is called");
		
		try {
			menuService.delete(id);

			LOGGER.info("MenuController | deleteMenu | message : " + "The menu item ID " + id + " has been deleted.");
			
			ra.addFlashAttribute("message", "L'élément de menu numéro " + id + " a été supprimé.");
		} catch (MenuItemNotFoundException ex) {
			LOGGER.info("MenuController | deleteMenu | error : " + ex.getMessage());
			
			ra.addFlashAttribute("error", ex.getMessage());
		}

		return defaultRedirectURL;
	}
	
	@GetMapping("/menus/{direction}/{id}")
	public String moveMenu(@PathVariable("direction") String direction, @PathVariable("id") Integer id, 
			RedirectAttributes ra) {
		
		LOGGER.info("MenuController | moveMenu is called");
		LOGGER.info("MenuController | moveMenu | direction : " + direction);
		LOGGER.info("MenuController | moveMenu | id : " + id);
		
		try {
			MenuMoveDirection moveDirection = MenuMoveDirection.valueOf(direction.toUpperCase());
			menuService.moveMenu(id, moveDirection);

			LOGGER.info("MenuController | moveMenu | message : " + "The menu ID " + id + " has been moved up by one position.");
			
			ra.addFlashAttribute("message", "L'élément de menu numéro " + id + " a été avancé d'une position.");

		} catch (MenuUnmoveableException ex) {
			
			LOGGER.info("MenuController | moveMenu | MenuUnmoveableException error : " + ex.getMessage());
			ra.addFlashAttribute("error", ex.getMessage());
		} catch (MenuItemNotFoundException ex) {
			
			LOGGER.info("MenuController | moveMenu | MenuUnmoveableException error : " + ex.getMessage());
			ra.addFlashAttribute("error", ex.getMessage());
		}

		return defaultRedirectURL;		
	}
	
}
