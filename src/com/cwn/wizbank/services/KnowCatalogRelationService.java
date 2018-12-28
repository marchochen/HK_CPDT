package com.cwn.wizbank.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.entity.KnowCatalogRelation;
import com.cwn.wizbank.persistence.KnowCatalogRelationMapper;
/**
 *  service 实现
 */
@Service
public class KnowCatalogRelationService extends BaseService<KnowCatalogRelation> {

	public String QUE_PARENT_KCA = "QUE_PARENT_KCA";
	
	@Autowired
	KnowCatalogRelationMapper knowCatalogRelationMapper;

	public void setKnowCatalogRelationMapper(KnowCatalogRelationMapper knowCatalogRelationMapper){
		this.knowCatalogRelationMapper = knowCatalogRelationMapper;
	}
	
	/**
	 * 插入问题所在分类位置
	 * @return
	 */
	public void insertKnowCatalogRelation(long que_id, long kcaIdOne, long kcaIdTwo, String usrId){
		KnowCatalogRelation knowCatalogRelation = new KnowCatalogRelation();
		knowCatalogRelation.setKcr_child_kca_id(que_id);
		//当存在子分类时，问题只添加进入子分类即可，不用添加在分类中
		if(kcaIdTwo > 0){
			knowCatalogRelation.setKcr_ancestor_kca_id(kcaIdTwo);
			knowCatalogRelation.setKcr_type(QUE_PARENT_KCA);
			knowCatalogRelation.setKcr_order(1);
			knowCatalogRelation.setKcr_parent_ind(1);
			knowCatalogRelation.setKcr_create_usr_id(usrId);
			knowCatalogRelation.setKcr_create_timestamp(getDate());
			knowCatalogRelationMapper.insert(knowCatalogRelation);
		}
		knowCatalogRelation.setKcr_ancestor_kca_id(kcaIdOne);
		knowCatalogRelation.setKcr_type(QUE_PARENT_KCA);
		knowCatalogRelation.setKcr_order(0);
		knowCatalogRelation.setKcr_parent_ind(kcaIdTwo>0?0:1);
		knowCatalogRelation.setKcr_create_usr_id(usrId);
		knowCatalogRelation.setKcr_create_timestamp(getDate());
		knowCatalogRelationMapper.insert(knowCatalogRelation);
		
	}
	
	/**
	 * 删除问题所在分类相关信息
	 * @return
	 */
	public void deleteKnowCatalogRelation(long que_id){
		KnowCatalogRelation kCatalogRelation = new KnowCatalogRelation();
		kCatalogRelation.setKcr_child_kca_id(que_id);
		kCatalogRelation.setKcr_type(QUE_PARENT_KCA);
		knowCatalogRelationMapper.delete(kCatalogRelation);
	}

	/**
	 * 删除子分类
	 * @param id
	 */
	public void delCatalogRecation(long parentId) {
		knowCatalogRelationMapper.deleteRecation(parentId);
	}

	public KnowCatalogRelationMapper getKnowCatalogRelationMapper() {
		return knowCatalogRelationMapper;
	}

	
}