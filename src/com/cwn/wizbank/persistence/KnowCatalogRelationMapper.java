package com.cwn.wizbank.persistence;

import java.util.List;

import com.cwn.wizbank.entity.KnowCatalogRelation;


public interface KnowCatalogRelationMapper extends BaseMapper<KnowCatalogRelation>{

	void insert(KnowCatalogRelation knowCatalogRelation);
	
	void delete(KnowCatalogRelation knowCatalogRelation);

	void deleteRecation(long parentId);
	
	List<Long> selectParents(long queId);
	
}