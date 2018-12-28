package com.cwn.wizbank.persistence;

import java.util.Map;

import com.cwn.wizbank.entity.EntityRelation;
import com.cwn.wizbank.entity.EntityRelationHistory;


public interface EntityRelationHistoryMapper extends BaseMapper<EntityRelationHistory>{

	void deleteAll(Map<String,Object> map);

	void insertHistory(EntityRelation entityRelation);
}