package com.cwn.wizbank.persistence;

import java.util.List;
import java.util.Map;

import com.cwn.wizbank.entity.KnowCatalog;
import com.cwn.wizbank.entity.vo.AeTreeNodeVo;

public interface KnowCatalogMapper extends BaseMapper<KnowCatalog>{

	List<KnowCatalog> selectKnowCatalog(KnowCatalog knowCatalog);
	
	List<AeTreeNodeVo> selectknowCatalogTree(Map<String, Long> map);
	
	void updateCountByKcaId(KnowCatalog knowCatalog);
	
	void updateCountByQueId(KnowCatalog knowCatalog);

	void delSubCatalog(long id);

	boolean isHaveSubCatalog(long id);
	
	boolean isHaveQue(long id);

	void updateStatus(Map<String,Object> map);
	void updatechildStatus(Map<String,Object> map);
	

	int checkCatalogName(KnowCatalog knowCatalog);
	
}