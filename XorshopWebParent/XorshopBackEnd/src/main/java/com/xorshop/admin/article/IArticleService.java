package com.xorshop.admin.article;

import java.util.List;

import com.xorshop.admin.pagin.PagingAndSortingHelper;
import com.xorshop.common.entity.User;
import com.xorshop.common.entity.article.Article;
import com.xorshop.common.entity.article.ArticleType;
import com.xorshop.common.exception.ArticleNotFoundException;

public interface IArticleService {
	
	public void listByPage(int pageNum, PagingAndSortingHelper helper);
	public Article get(Integer id) throws ArticleNotFoundException;
	public void save(Article article, User user);
	public void delete(Integer id) throws ArticleNotFoundException;
	public void updatePublishStatus(Integer id, boolean published) throws ArticleNotFoundException;
	public List<Article> findByTypeOrderByTitle(ArticleType type);
}
