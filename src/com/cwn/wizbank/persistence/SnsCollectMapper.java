package com.cwn.wizbank.persistence;

import java.util.List;
import java.util.Map;

import com.cwn.wizbank.entity.AeItem;
import com.cwn.wizbank.entity.Article;
import com.cwn.wizbank.entity.KbItem;
import com.cwn.wizbank.entity.SnsCollect;
import com.cwn.wizbank.utils.Page;


public interface SnsCollectMapper extends BaseMapper<SnsCollect>{

	List<AeItem> selectCollectItemList(Page<AeItem> page);
	
	List<Article> selectCollectArticleList(Page<Article> page);
	
	void delete(SnsCollect snsCollect);

	SnsCollect getByUserId(Map<String, Object> map);

	List<Article> selectCollectKnowledgeList(Page<KbItem> page);
	void delErrorData(Map<String, Object> map);

}