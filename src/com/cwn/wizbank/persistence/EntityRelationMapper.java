package com.cwn.wizbank.persistence;

import java.util.List;
import java.util.Map;

import com.cwn.wizbank.entity.EntityRelation;


public interface EntityRelationMapper extends BaseMapper<EntityRelation>{

	void delByUsrEntId(long usrEntId);
	public List<EntityRelation> getByChild(EntityRelation enr);
	void delAsAncestor(String usrId);
	void delAsChild(Map<String, Object> map);
	List<EntityRelation> getEntityRelations(Map<String, Object> map);
	void delAncestor(Map<String, Object> map);
	int getCount(Map<String, Object> map);
	List<EntityRelation> getList(long usr_ent_id);
	void delByChildEntId(long usr_ent_id);


}