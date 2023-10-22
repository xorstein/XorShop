package com.xorshop.admin.article;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.xorshop.admin.pagin.SearchRepository;
import com.xorshop.common.entity.article.Article;
import com.xorshop.common.entity.article.ArticleType;

public interface ArticleRepository extends CrudRepository<Article, Integer>, SearchRepository<Article, Integer> {

	@Query("SELECT NEW Article(a.id, a.title, a.type, a.updatedTime, a.published, a.user) "
			+ "FROM Article a")
	public Page<Article> findAll(Pageable pageable);
	
	@Query("SELECT NEW Article(a.id, a.title, a.type, a.updatedTime, a.published, a.user) "
			+ "FROM Article a WHERE a.title LIKE %?1% OR a.content LIKE %?1%")
	public Page<Article> findAll(String keyword, Pageable pageable);
	
	@Query("UPDATE Article a SET a.published = ?2 WHERE a.id = ?1")
	@Modifying
	public void updatePublishStatus(Integer id, boolean published);
	
	public List<Article> findByTypeOrderByTitle(ArticleType type);
	
	@Query("SELECT NEW Article(a.id, a.title) From Article a WHERE a.published = true ORDER BY a.title")
	public List<Article> findPublishedArticlesWithIDAndTitleOnly();
}
