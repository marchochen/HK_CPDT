package com.cwn.wizbank.cpd.service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.cpd.utils.CpdUtils;
import com.cwn.wizbank.entity.CpdGroup;
import com.cwn.wizbank.entity.CpdType;
import com.cwn.wizbank.entity.ObjectActionLog;
import com.cwn.wizbank.persistence.CpdGroupMapper;
import com.cwn.wizbank.services.BaseService;
import com.cwn.wizbank.utils.Page;




@Service
public class CpdGroupService extends BaseService<CpdGroup> {

	@Autowired
	CpdGroupMapper cpdGroupMapper;
	
	@Autowired
	CpdGroupRegistrationService cpdGroupRegistrationService;
	
	@Autowired
	AeItemCPDGourpItemService aeItemCPDGourpItemService;

    @Autowired
    CpdManagementService cpdManagementService;

    @Autowired
    CpdRegistrationMgtService cpdRegistrationMgtService;
    
	
    /**
     * 判断牌照组别在该大牌类型下是否存在（cg_code）
     * @param cpdGroup
     * @return
     */
	public boolean isExistForType(CpdGroup cpdGroup) {
		cpdGroup.setCg_code(cpdGroup.getCg_code().trim());
		return cpdGroupMapper.isExistForType(cpdGroup);
	}

	
	/**
	 * 新增/修改 组别信息
	 * @param cpdGroup
	 * @param prof
	 */
	public void saveOrUpdate(CpdGroup cpdGroup, loginProfile prof) {
        CpdType cpdType = cpdRegistrationMgtService.getCpdTypeByid(cpdGroup.getCg_ct_id());
		if (cpdGroup.getCg_id() == null || cpdGroup.getCg_id().equals("")) {
			cpdGroup.setCg_code(cpdGroup.getCg_code().trim());
			cpdGroup.setCg_create_datetime(super.getDate());
			cpdGroup.setCg_create_usr_ent_id(prof.usr_ent_id);
			cpdGroup.setCg_display_order(getMaxOrder()+1);
			cpdGroup.setCg_status(CpdUtils.STATUS_OK);
			super.add(cpdGroup);
			
	        CpdGroup cpdGroup1 = cpdGroupMapper.getGroupByCode(cpdGroup);
			cpdManagementService.saveOrUpdOrDelGroupLog(prof, cpdType, cpdGroup1, ObjectActionLog.OBJECT_ACTION_ADD);
		} else {
			cpdGroup.setCg_code(cpdGroup.getCg_code().trim());
			cpdGroup.setCg_update_datetime(super.getDate());
			cpdGroup.setCg_update_usr_ent_id(prof.usr_ent_id);
			super.update(cpdGroup);
			
            cpdManagementService.saveOrUpdOrDelGroupLog(prof, cpdType, cpdGroup, ObjectActionLog.OBJECT_ACTION_UPD);
		}
	}
	
	/**
	 * 获取当前排序的最大值
	 * @return
	 */
	public int getMaxOrder(){
		return cpdGroupMapper.getMaxOrder();
	}
	
	
	/**
	 * 查询大牌下所有牌照信息
	 * 功能是属于一个与培训中心无关的功能，即拥有功能权限的角色可以管理所有牌照
	 * @param page
	 * @param ct_id
	 * @return
	 */
	public Page<CpdGroup> searchAll(Page<CpdGroup> page,long ct_id) {
		page.getParams().put("cg_ct_id", ct_id);
		cpdGroupMapper.searchAll(page);
		return page;
	}
	
	/**
	 * 删除组别
	 * @param cpdGroup
	 * @param prof
	 */
	public void delete(CpdGroup cpdGroup, loginProfile prof){
        CpdGroup cpdGroup1 = cpdRegistrationMgtService.getCpdGroupById(cpdGroup.getCg_id());//小牌删除前查询信息供日志使用
        CpdType cpdType1 = cpdRegistrationMgtService.getCpdTypeByid(cpdGroup1.getCg_ct_id());
        
		cpdGroup.setCg_update_datetime(super.getDate());
		cpdGroup.setCg_update_usr_ent_id(prof.usr_ent_id);
		cpdGroup.setCg_status(CpdUtils.STATUS_DEL);
		cpdGroupMapper.delete(cpdGroup);
		
		cpdManagementService.saveOrUpdOrDelGroupLog(prof, cpdType1, cpdGroup1, ObjectActionLog.OBJECT_ACTION_DEL);
	}
	
	/**
	 * 牌照组别排序
	 * @param sortStr
	 * @param cg_ct_id
	 * @param prof
	 */
	public void infoSort(String sortStr,long cg_ct_id, loginProfile prof){
		  String[] infoSortArray=sortStr.split(",");
		  for(int i=0;i<infoSortArray.length;i++)
		  {
			  CpdGroup cpdGroup = new CpdGroup();
			  cpdGroup.setCg_update_datetime(super.getDate());
			  cpdGroup.setCg_update_usr_ent_id(prof.usr_ent_id);
			  cpdGroup.setCg_display_order(i+1);
			  cpdGroup.setCg_ct_id(cg_ct_id);
			  cpdGroup.setCg_code(infoSortArray[i].trim());
			  cpdGroupMapper.updateOrder(cpdGroup);
		  }
		
		
	};
	
	 /**
     * 判断牌照是否有学员注册
     * @param cpdType
     * @return
     */
	public boolean getCountByCgID(long cg_id) {
		return cpdGroupRegistrationService.getCountByCgID(cg_id);
	}
	
	public void deleteAllByCtID(long ct_id, loginProfile prof){
		 Map<String ,Object> map = new HashMap<String, Object>();
		 map.put("cg_update_datetime", super.getDate());
		 map.put("cg_update_usr_ent_id", prof.usr_ent_id);
		 map.put("cg_status", CpdUtils.STATUS_DEL);
		 map.put("ct_id", ct_id);
		 cpdGroupMapper.deleteAllByCtID(map);
	}
	
	 /**
	  * 判断小牌是否有课程关联
	  * @param cg_id
	  * @return
	  */
	public boolean getCountItemByCgID(long cg_id) {
		return aeItemCPDGourpItemService.getCountItemByCgID(cg_id);
	}
	
	/**
	 * 查询所有小牌（先通过大牌的order排序，再小牌order排序）
	 * @param ct_id
	 * @return
	 */
	public List<CpdGroup> getAllOrder(long ct_id) {
		Map<String ,Object> map = new HashMap<String, Object>();
		if(ct_id > 0){
			map.put("cg_ct_id", ct_id);
		}
		List<CpdGroup>  list = cpdGroupMapper.getAllOrder(map);
		return list;
	}
	
	public List<CpdGroup> searchByType(List<Long> typeList,boolean display_in_report_ind){
		Map<String ,Object> map = new HashMap<String, Object>();
		map.put("cghiCtIds", typeList);
		map.put("display_in_report_ind", display_in_report_ind==true?1:0);
		List<CpdGroup>  list = cpdGroupMapper.searchByType(map);
		return list;
	}
	
	public int getCountGroupByCtId(long ct_id){
		  int count = 	cpdGroupMapper.getCountGroupByCtId(ct_id);
		  return count;
		}
	
}
