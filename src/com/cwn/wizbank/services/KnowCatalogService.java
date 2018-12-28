package com.cwn.wizbank.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cw.wizbank.know.dao.KnowCatalogDAO;
import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.entity.KnowCatalog;
import com.cwn.wizbank.entity.KnowCatalogRelation;
import com.cwn.wizbank.entity.vo.AeTreeNodeVo;
import com.cwn.wizbank.persistence.KnowCatalogMapper;
import com.cwn.wizbank.utils.LabelContent;
import com.cwn.wizbank.utils.Page;
/**
 *  service 实现
 */
@Service
public class KnowCatalogService extends BaseService<KnowCatalog> {

	@Autowired
	KnowCatalogMapper knowCatalogMapper;
	
	@Autowired
	KnowCatalogRelationService knowCatalogRelationService;
	
	@Autowired
	KnowQuestionService knowQuestionService;
	
	public void setKnowCatalogMapper(KnowCatalogMapper knowCatalogMapper){
		this.knowCatalogMapper = knowCatalogMapper;
	}
	
	/**
	 * 获取问题分类列表
	 * @param kcaType 问题分类类型
	 * @param kcaId 问题分类id
	 * @param tcrId 培训中心id
	 * @param userEntId 
	 * @return
	 */
	public List<KnowCatalog> getKnowCatalogLlist(String kcaType, long kcaId, long tcrId, loginProfile prof, boolean check_status){
		KnowCatalog knowCatalog = new KnowCatalog();
		knowCatalog.setKca_type(kcaType);
		knowCatalog.setKca_id(kcaId);
		knowCatalog.setKca_tcr_id(tcrId);
		if(check_status){
			knowCatalog.setKca_public_ind(1l);
		}
		
		
		List<KnowCatalog> knowCatalogLlist = knowCatalogMapper.selectKnowCatalog(knowCatalog);
		
		if(knowCatalogLlist!=null){
			for (int i = 0; i < knowCatalogLlist.size(); i++) {
				KnowCatalog knowca = knowCatalogLlist.get(i);
				if(knowca.getKca_id()==-1){
					knowca.setKca_title(LabelContent.get(prof.cur_lan, "lab_kb_unspecified"));
					break;
				}
			}
		}
		
		if((knowCatalogLlist == null || knowCatalogLlist.size() <= 0) && KnowCatalogDAO.KCA_TYPE_CATALOG.equalsIgnoreCase(kcaType)){
			KnowCatalog catalog = new KnowCatalog();
			catalog.setKca_public_ind(1l);
			catalog.setKca_title(LabelContent.get(prof.cur_lan, "lab_kb_unclassified"));
			catalog.setKca_type(kcaType);
			KnowCatalogRelation kcr = new KnowCatalogRelation();
			kcr.setKcr_ancestor_kca_id(0l);
			catalog.setKnowCatalogRelation(kcr);
			this.saveOrUpdate(catalog, prof.usr_ent_id, tcrId);
			knowCatalogLlist = knowCatalogMapper.selectKnowCatalog(knowCatalog);
		}
		return knowCatalogLlist;
	}
	
	/**
	 * 获取问题分类情况
	 * @param queId 问题id
	 * @return
	 */
	public List<KnowCatalog> getCatalogSituation(long queId){
		KnowCatalog knowCatalog = new KnowCatalog();
		KnowCatalogRelation knowCatalogRelation = new KnowCatalogRelation();
		knowCatalogRelation.setKcr_child_kca_id(queId);
		knowCatalog.setKnowCatalogRelation(knowCatalogRelation);
		List<KnowCatalog> list = knowCatalogMapper.selectKnowCatalog(knowCatalog);
		return list;
	}
	
	/**
	 * 更新分类里的问题总数
	 * @param kca_id 分类id
	 * @param upd_count 更新总数
	 * @param usr_id 操作人id
	 * @return
	 */
	public void updateKcaQueCountByKcaId(long kca_id, long upd_count, String usr_id){
		KnowCatalog knowCatalog = new KnowCatalog();
		knowCatalog.setKca_id(kca_id);
		knowCatalog.setKca_update_usr_id(usr_id);
		knowCatalog.setKca_update_timestamp(getDate());
		knowCatalog.setUpd_count(upd_count);
		knowCatalogMapper.updateCountByKcaId(knowCatalog);
	}
	
	public void updateKcaQueCountByQueId(long que_id, long upd_count, String usr_id){
		KnowCatalog knowCatalog = new KnowCatalog();
		knowCatalog.setKca_update_usr_id(usr_id);
		knowCatalog.setKca_update_timestamp(getDate());
		knowCatalog.setUpd_count(upd_count);
		KnowCatalogRelation knowCatalogRelation = new KnowCatalogRelation();
		knowCatalogRelation.setKcr_child_kca_id(que_id);
		knowCatalogRelation.setKcr_type(KnowCatalogRelation.QUE_PARENT_KCA);
		knowCatalog.setKnowCatalogRelation(knowCatalogRelation);
		knowCatalogMapper.updateCountByQueId(knowCatalog);
	}
	
	/**
	 * 获取问题分类树
	 * @return
	 */
	public List<AeTreeNodeVo> getKnowCatalogTree(long tcr_id){
		Map<String, Long> map = new HashMap<String, Long>();
		map.put("tcr_id", tcr_id);
		return knowCatalogMapper.selectknowCatalogTree(map);
	}

	/**
	 * 翻页
	 * @param page
	 * @param tcrId
	 * @return
	 */
	public Page<KnowCatalog> page(Page<KnowCatalog> page, long tcrId) {
		page.getParams().put("tcrId", tcrId);
		
		knowCatalogMapper.page(page);
		return page;
	}
	
	/**
	 * 添加
	 */
	public void saveOrUpdate(KnowCatalog catalog, long userEntId, long tcrId) {
		if(catalog != null && catalog.getKca_id() != null && catalog.getKca_id() >0) {
			KnowCatalog dbCatalog = this.get(catalog.getKca_id());
			if(dbCatalog != null) {
				dbCatalog.setKca_title(catalog.getKca_title());
				dbCatalog.setKca_public_ind(catalog.getKca_public_ind());
				this.update(dbCatalog);
			}
		} else {
			catalog.setKca_create_timestamp(getDate());
			catalog.setKca_create_usr_id(userEntId + "");
			catalog.setKca_que_count(0l);
			catalog.setKca_tcr_id(tcrId);
			catalog.setKca_update_timestamp(getDate());
			catalog.setKca_update_usr_id(userEntId + "");
			this.add(catalog);
			
			//关联表
			KnowCatalogRelation kcr = catalog.getKnowCatalogRelation();
			if(kcr.getKcr_ancestor_kca_id() != null && kcr.getKcr_ancestor_kca_id() > 0) {
				kcr.setKcr_child_kca_id(catalog.getKca_id());
				kcr.setKcr_create_timestamp(getDate());
				kcr.setKcr_create_usr_id(userEntId + "");
				kcr.setKcr_parent_ind(kcr.getKcr_ancestor_kca_id() != null? 1 : 0);
				kcr.setKcr_syn_timestamp(getDate());
				kcr.setKcr_type("KCA_PARENT_KCA");
				kcr.setKcr_order(0);
				knowCatalogRelationService.add(kcr);
			}
		}
		
	}

	public int del(long id, String type) {
/*		if("parent".equals(type)) {
			if(this.isEmptyCatalog()) {
				this.delete(id);
			}
			//删除子分类
			//this.knowCatalogMapper.delSubCatalog(id);
			//删除
			
			//删除与该分类相关的关联信息
			//this.knowCatalogRelationService.delCatalogRecation(id);
		}
		*/
		//如果是空，则可以删，否则失败
		int flag = 0;
		if(this.knowCatalogMapper.isHaveSubCatalog(id)) {
			flag = 1;
		}
		Boolean flg=this.knowCatalogMapper.isHaveQue(id);
		if(this.knowCatalogMapper.isHaveQue(id)) {
			flag = 2;
		}
		if(flag == 0) {
			//if("parent".equals(type)) {   //当删除的为子分类时  也要删除关系
				this.knowCatalogRelationService.delCatalogRecation(id);
			//}
			this.delete(id);
		}
		return flag;
	}

	public void updateStatus(long id, String type) {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("type", type);
		this.knowCatalogMapper.updateStatus(map);
		this.knowCatalogMapper.updatechildStatus(map);
	}
	
	public int checkCatalogName(KnowCatalog catalog){
		if(null != catalog && null != catalog.getKca_title()){
			catalog.setKca_title(catalog.getKca_title().trim());
		}
		return this.knowCatalogMapper.checkCatalogName(catalog);
	}

}