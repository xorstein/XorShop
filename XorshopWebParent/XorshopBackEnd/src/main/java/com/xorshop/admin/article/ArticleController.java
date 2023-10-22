package com.xorshop.admin.article;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.xorshop.admin.pagin.PagingAndSortingHelper;
import com.xorshop.admin.pagin.PagingAndSortingParam;
import com.xorshop.admin.security.XorshopUserDetails;
import com.xorshop.common.entity.User;
import com.xorshop.common.entity.article.Article;
import com.xorshop.common.exception.ArticleNotFoundException;

@Controller
public class ArticleController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ArticleController.class);
	
	private String defaultRedirectURL = "redirect:/articles/page/1?sortField=title&sortDir=asc";

	@Autowired 
	private ArticleService service;

	@GetMapping("/articles")
	public String listFirstPage(Model model) {
		
		LOGGER.info("ArticleController | listFirstPage is called");
		
		return defaultRedirectURL;
	}

	@GetMapping("/articles/page/{pageNum}")
	public String listByPage(@PagingAndSortingParam(moduleURL = "/articles", listName = "listArticles") PagingAndSortingHelper helper, 
						@PathVariable(name = "pageNum") int pageNum,Model model) {
		
		LOGGER.info("ArticleController | listByPage is called");
		model.addAttribute("pageTitle", "Articles");
		model.addAttribute("activelink", "14");
		service.listByPage(pageNum, helper);
		return "articles/articles";
	}
	
	@GetMapping("/articles/detail/{id}")
	public String viewArticle(@PathVariable(name = "id") Integer id, RedirectAttributes ra,  Model model) {
		
		LOGGER.info("ArticleController | viewArticle is called");
		
		try {
			Article article = service.get(id);
			
			LOGGER.info("ArticleController | viewArticle | article title : " + article.getTitle());
			
			model.addAttribute("article", article);

			return "articles/article_detail_modal";

		} catch (ArticleNotFoundException ex) {
			
			LOGGER.info("ArticleController | viewArticle | message : " + "Could not find any article with ID " + id);
			
			ra.addFlashAttribute("message", "Could not find any article with ID " + id);
			return defaultRedirectURL;
		}		
	}
	
	@GetMapping("/articles/new")
	public String newArticle(Model model) {
		
		LOGGER.info("ArticleController | newArticle is called");
		model.addAttribute("activelink", "14");
		Article article=new Article();
		article.setPublished(true);
		model.addAttribute("article", article);
		model.addAttribute("pageTitle", "Nouvelle article");
		
		LOGGER.info("ArticleController | newArticle | pageTitle : " + "Create New Article");

		return "articles/article_form";
	}
	
	@PostMapping("/articles/save")
	public String saveArticle(Article article, RedirectAttributes ra, 
			@AuthenticationPrincipal XorshopUserDetails userDetails) {

		LOGGER.info("ArticleController | saveArticle is called");
		
		User authenticatedUser = userDetails.getUser();
		
		LOGGER.info("ArticleController | saveArticle | authenticatedUser : " + authenticatedUser.getFullName());
		
		service.save(article, authenticatedUser);
		
		LOGGER.info("ArticleController | saveArticle | message : " + "The article has been saved successfully.");

		ra.addFlashAttribute("message", "L'article a été enregistré avec succès.");

		return defaultRedirectURL;
	}
	
	@GetMapping("/articles/edit/{id}")
	public String editArticle(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes ra) {
		
		LOGGER.info("ArticleController | editArticle is called");
		
		try {
			Article article = service.get(id);
			
			LOGGER.info("ArticleController | editArticle | article content: " + article.getContent());
			model.addAttribute("article", article);
			model.addAttribute("activelink", "14");
			LOGGER.info("ArticleController | editArticle | pageTitle: " + "Edit Article (ID: " + id + ")");
			model.addAttribute("pageTitle", "Modifier l'article (Numéro: " + id + ")");

			return "articles/article_form"; 

		} catch (ArticleNotFoundException ex) {
			LOGGER.info("ArticleController | editArticle | error: " + ex.getMessage());
			ra.addFlashAttribute("error", ex.getMessage());

			return defaultRedirectURL;
		}		
	}
	
	
	@GetMapping("/articles/delete/{id}")
	public String deleteArticle(@PathVariable(name = "id") Integer id, RedirectAttributes ra) {
		
		LOGGER.info("ArticleController | deleteArticle is called");
		LOGGER.info("ArticleController | deleteArticle | id : " + id);
		
		try {
			service.delete(id);
			
			LOGGER.info("ArticleController | editArticle | message: " + "The article ID " + id + " has been deleted.");
			ra.addFlashAttribute("message", "L'article numéro " + id + " a été supprimé.");
		} catch (ArticleNotFoundException ex) {
			LOGGER.info("ArticleController | deleteArticle | error: " + ex.getMessage());
			ra.addFlashAttribute("error", ex.getMessage());
		}

		return defaultRedirectURL;
	}
	
	@GetMapping("/articles/{id}/enabled/{publishStatus}")
	public String publishArticle(@PathVariable("id") Integer id, 
			@PathVariable("publishStatus") String publishStatus, RedirectAttributes ra) {
		
		LOGGER.info("ArticleController | publishArticle is called");
		LOGGER.info("ArticleController | publishArticle | id : " + id);
		LOGGER.info("ArticleController | publishArticle | publishStatus : " + publishStatus);
		
		try {
			boolean published = Boolean.parseBoolean(publishStatus);	
			
			LOGGER.info("ArticleController | publishArticle | published : " + published);
			
			service.updatePublishStatus(id, published);		

			String publishResult = published ? " a été publié." : " n'a pas été publié.";
			
			LOGGER.info("ArticleController | publishArticle | publishResult : " + publishResult);
			
			LOGGER.info("ArticleController | publishArticle | message : " + "The article ID " + id + " has been " + publishResult);
			ra.addFlashAttribute("message", "L'article numéro " + id  + publishResult);
				
		} catch (ArticleNotFoundException ex) {
			LOGGER.info("ArticleController | publishArticle | error: " + ex.getMessage());
			ra.addFlashAttribute("error", ex.getMessage());
		}

		return defaultRedirectURL;
	}
}
