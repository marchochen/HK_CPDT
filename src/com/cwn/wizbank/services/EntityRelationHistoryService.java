package com.cwn.wizbank.services;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.entity.EntityRelationHistory;
import com.cwn.wizbank.persistence.EntityRelationHistoryMapper;
/**
 *  service 实现
 */
@Service
public class EntityRelationHistoryService extends BaseService<EntityRelationHistory> {

	@Autowired
	EntityRelationHistoryMapper entityRelationHistoryMapper;

	public void setEntityRelationHistoryMapper(EntityRelationHistoryMapper entityRelationHistoryMapper){
		this.entityRelationHistoryMapper = entityRelationHistoryMapper;
	}
	
	void deleteAll(Long erh_child_ent_id, String erh_type){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("erh_child_ent_id", erh_child_ent_id);
		map.put("erh_type", erh_type);
		this.entityRelationHistoryMapper.deleteAll(map);
	}

	public EntityRelationHistoryMapper getEntityRelationHistoryMapper() {
		return entityRelationHistoryMapper;
	}
}