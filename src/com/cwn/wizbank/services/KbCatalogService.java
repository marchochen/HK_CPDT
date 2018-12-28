package com.cwn.wizbank.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.entity.AcRole;
import com.cwn.wizbank.entity.KbCatalog;
import com.cwn.wizbank.entity.KbItem;
import com.cwn.wizbank.entity.KbItemCat;
import com.cwn.wizbank.entity.TcTrainingCenter;
import com.cwn.wizbank.persistence.KbCatalogMapper;
import com.cwn.wizbank.persistence.KbItemCatMapper;
import com.cwn.wizbank.utils.CommonLog;
import com.cwn.wizbank.utils.LabelContent;
import com.cwn.wizbank.utils.Page;

/**
 * service 实现
 */
@Service
public class KbCatalogService extends BaseService<KbCatalog> {

	@Autowired
	KbCatalogMapper kbCatalogMapper;
	
	@Autowired
	KbItemCatMapper kbItemCatMapper;

	public void setKbCatalogMapper(KbCatalogMapper kbCatalogMapper) {
		this.kbCatalogMapper = kbCatalogMapper;
	}

	public void saveOrUpdate(KbCatalog kbCatalog, loginProfile prof) {
		kbCatalog.setKbc_title(kbCatalog.getKbc_title().trim());
		if (kbCatalog.getKbc_desc() != null && !"".equals(kbCatalog.getKbc_desc())) {
			kbCatalog.setKbc_desc(kbCatalog.getKbc_desc().trim());
		}
		if (kbCatalog.getKbc_id() == null || kbCatalog.getKbc_id().equals("")) {
			kbCatalog.setKbc_create_datetime(super.getDate());
			kbCatalog.setKbc_create_user_id(prof.usr_id);
			kbCatalog.setKbc_status(KbItem.STATUS_OFF);
			super.add(kbCatalog);
		} else {
			kbCatalog.setKbc_update_datetime(super.getDate());
			kbCatalog.setKbc_update_user_id(prof.usr_id);
			super.update(kbCatalog);
		}
	}

	public Page<KbCatalog> listPage(Page<KbCatalog> page, loginProfile prof) {
		if(!AccessControlWZB.isRoleTcInd(prof.current_role)){
			page.getParams().put("usr_ent_id", prof.root_ent_id);
			page.getParams().put("current_role", AcRole.ROLE_ADM_1);
		}else{
			page.getParams().put("usr_ent_id", prof.usr_ent_id);
			page.getParams().put("current_role", prof.current_role);
		}
		kbCatalogMapper.selectPage(page);
		return page;
	}

	public List<Map<String, Object>> treeJsonList(String filter, loginProfile prof) throws Exception {
		if(AccessControlWZB.isRoleTcInd(prof.current_role)){
			return kbCatalogMapper.jsonList("%" + filter + "%", prof.usr_ent_id, prof.current_role);
		}else{
			return kbCatalogMapper.jsonList("%" + filter + "%", prof.root_ent_id, AcRole.ROLE_ADM_1);
		}
		
	}

	public Map<String, Object> delete(KbCatalog kbCatalog, loginProfile prof) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			super.delete(kbCatalog.getKbc_id());
			map.put("success", true);
		} catch (Exception e) {
			map.put("success", false);
			//查找该文件夹是否含有已审批（APPROVED）过的知识
			if(!kbCatalogMapper.hasApprovedKb(kbCatalog)){
				//查出相应目录与知识的关系，把目录ID改为未分类的ID
				Long catalog_id = this.getTmpCatalogByTopTcr(prof.my_top_tc_id);
				if (catalog_id == null) {
					catalog_id = this.insertTmpCatalogInTopTcr(prof);
				}
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("new_kbc_id", catalog_id);
				param.put("old_kbc_id", kbCatalog.getKbc_id());
				kbItemCatMapper.updateKbcId(param);
				super.delete(kbCatalog.getKbc_id());
				map.put("success", true);
			}
			CommonLog.error(e.getMessage(),e);
		}
		return map;
	}

	public List<KbCatalog> getCatalogByTcr(WizbiniLoader wizbini, loginProfile prof, String kbc_status, String kbi_id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("kbc_status", kbc_status);
		map.put("usr_ent_id", prof.usr_ent_id);
		map.put("kbi_id", kbi_id);
		map.put("tc_independent", wizbini.cfgSysSetupadv.isTcIndependent());
		map.put("tcr_id", prof.my_top_tc_id);
		return kbCatalogMapper.selectCatByTcr(map);
	}

	public void publish(KbCatalog kbCatalog, loginProfile prof) {
		if (kbCatalog != null && kbCatalog.getKbc_id() != null) {
			kbCatalog.setKbc_update_datetime(super.getDate());
			kbCatalog.setKbc_update_user_id(prof.usr_id);
			kbCatalogMapper.publish(kbCatalog);
		}
	}

	public boolean isExist(KbCatalog kbCatalog) {
		return kbCatalogMapper.isExist(kbCatalog);
	}

	public List<KbCatalog> getCatalogByIds(Long[] ids) {
		List<KbCatalog> list = new ArrayList<KbCatalog>();
		if (ids != null && ids.length > 0) {
			for (int i = 0; i < ids.length; i++) {
				list.add(kbCatalogMapper.get(ids[i]));
			}
		}
		return list;
	}

	public Long getTmpCatalogByTopTcr(long my_top_tc_id) {
		return kbCatalogMapper.getTmpCatalogByTopTcr(my_top_tc_id);
	}

	public Long insertTmpCatalogInTopTcr(loginProfile prof) {
		KbCatalog kbCatalog = new KbCatalog();
		TcTrainingCenter tcTrainingCenter = new TcTrainingCenter();
		tcTrainingCenter.setTcr_id(prof.my_top_tc_id);
		kbCatalog.setTcTrainingCenter(tcTrainingCenter);
		kbCatalog.setKbc_create_user_id("s1u3");
		kbCatalog.setKbc_create_datetime(getDate());
		kbCatalog.setKbc_type(KbCatalog.TYPE_TEMP);
		kbCatalog.setKbc_title(LabelContent.get(prof.cur_lan, "lab_kb_unclassified"));
		kbCatalog.setKbc_status(KbItem.STATUS_ON);
		kbCatalogMapper.insert(kbCatalog);
		return kbCatalog.getKbc_id();
	}
}