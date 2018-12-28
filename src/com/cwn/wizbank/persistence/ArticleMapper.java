package com.cwn.wizbank.persistence;

import java.util.List;

import com.cwn.wizbank.entity.Article;
import com.cwn.wizbank.entity.ArticleType;
import com.cwn.wizbank.utils.Page;


public interface ArticleMapper extends BaseMapper<Article>{

	/**
	 * 	查询所有动态
	 * @param page
	 * @return
	 */
	public List<Article> selectList(Page<Article> page);

	List<ArticleType> selectArticleType(ArticleType articleType);	
	
}