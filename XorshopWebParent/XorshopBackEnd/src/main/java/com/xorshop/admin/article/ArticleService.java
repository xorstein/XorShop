package com.xorshop.admin.article;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xorshop.admin.pagin.PagingAndSortingHelper;
import com.xorshop.common.entity.User;
import com.xorshop.common.entity.article.Article;
import com.xorshop.common.entity.article.ArticleType;
import com.xorshop.common.exception.ArticleNotFoundException;

@Service
@Transactional
public class ArticleService implements IArticleService{

	public static final int ARTICLES_PER_PAGE = 5;

	@Autowired 
	private ArticleRepository repo;
	
	@Override
	public void listByPage(int pageNum, PagingAndSortingHelper helper) {
		// TODO Auto-generated method stub
		helper.listEntities(pageNum, ARTICLES_PER_PAGE, repo);
	}

	@Override
	public Article get(Integer id) throws ArticleNotFoundException {
		// TODO Auto-generated method stub
		try {
			return repo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new ArticleNotFoundException("Impossible de trouver l'article avec le numéro  " + id);
		}
	}

	@Override
	public void save(Article article, User user) {
		// TODO Auto-generated method stub
		
		if (article.getAlias() == null || article.getAlias().isEmpty()) {
			article.setAlias(article.getTitle().replaceAll(" ", "-"));
		}

		article.setUpdatedTime(new Date());
		article.setUser(user);

		repo.save(article);
	}

	@Override
	public void delete(Integer id) throws ArticleNotFoundException {
		// TODO Auto-generated method stub
		if (!repo.existsById(id)) {
			throw new ArticleNotFoundException("Impossible de trouver l'article avec le numéro " + id);			
		}
		repo.deleteById(id);
	}

	@Override
	public void updatePublishStatus(Integer id, boolean published) throws ArticleNotFoundException {
		// TODO Auto-generated method stub
		if (!repo.existsById(id)) {
			throw new ArticleNotFoundException("Impossible de trouver l'article avec le numéro " + id);
		}
		repo.updatePublishStatus(id, published);
	}

	@Override
	public List<Article> findByTypeOrderByTitle(ArticleType type) {
		// TODO Auto-generated method stub
		return repo.findByTypeOrderByTitle(ArticleType.MENU_BOUND);
	}
	
	public List<Article> listArticlesForMenu() {
		return repo.findByTypeOrderByTitle(ArticleType.MENU_BOUND);
	}

	public List<Article> listAll() {
		return repo.findPublishedArticlesWithIDAndTitleOnly();
	}

}
