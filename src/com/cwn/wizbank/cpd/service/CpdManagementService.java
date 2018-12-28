package com.cwn.wizbank.cpd.service;


import java.sql.Timestamp;
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
import com.cwn.wizbank.persistence.CpdTypeMapper;
import com.cwn.wizbank.services.BaseService;
import com.cwn.wizbank.systemLog.SystemLogContext;
import com.cwn.wizbank.systemLog.SystemLogTypeEnum;
import com.cwn.wizbank.utils.CommonLog;
import com.cwn.wizbank.utils.DateUtil;
import com.cwn.wizbank.utils.Page;




@Service
public class CpdManagementService extends BaseService<CpdType> {

	@Autowired
	CpdTypeMapper cpdTypeMapper;
	
	@Autowired
	CpdRegistrationMgtService cpdRegistrationMgtService;
	
	@Autowired
	CpdGroupService cpdGroupService;
	
	@Autowired
	AeItemCPDItemService aeItemCPDItemService;
	
    /**
     * 判断牌照类别是否存在（ct_license_type）
     * @param cpdType
     * @return
     */
	public boolean isExistForType(CpdType cpdType) {
		cpdType.setCt_license_type(cpdType.getCt_license_type().trim());
		return cpdTypeMapper.isExistForType(cpdType);
	}

	
	/**
	 * 新增/修改 牌照信息
	 * @param cpdType
	 * @param prof
	 */
	public void saveOrUpdate(CpdType cpdType, loginProfile prof) {
		cpdType.setCt_license_alias(cpdType.getCt_license_alias().trim());
		cpdType.setCt_license_type(cpdType.getCt_license_type().trim());
		if (cpdType.getCt_id() == null || cpdType.getCt_id().equals("")) {
			//cpdType.setCt_license_type(cpdType.getCt_license_type().trim());
			cpdType.setCt_create_datetime(super.getDate());
			cpdType.setCt_create_usr_ent_id(prof.usr_ent_id);
			cpdType.setCt_display_order(getMaxOrder()+1);
			cpdType.setCt_status(CpdUtils.STATUS_OK);
			super.add(cpdType);

            cpdType = cpdTypeMapper.getTypeByCode(cpdType);
			saveOrUpdOrDelTypeLog(prof, cpdType,ObjectActionLog.OBJECT_ACTION_ADD);
		} else {
			cpdType.setCt_update_datetime(super.getDate());
			cpdType.setCt_update_usr_ent_id(prof.usr_ent_id);
			super.update(cpdType);
			saveOrUpdOrDelTypeLog(prof, cpdType,ObjectActionLog.OBJECT_ACTION_UPD);
		}
	}
	
	/**
	 * 获取当前排序的最大值
	 * @return
	 */
	public int getMaxOrder(){
		return cpdTypeMapper.getMaxOrder();
	}
	
	
	/**
	 * 查询所有牌照信息
	 * 功能是属于一个与培训中心无关的功能，即拥有功能权限的角色可以管理所有牌照
	 * @param page
	 * @return
	 */
	public Page<CpdType> searchAll(Page<CpdType> page) {
		cpdTypeMapper.searchAll(page);
		return page;
	}
	
	/**
	 * 删除牌照
	 * @param cpdType
	 * @param prof
	 */
	public void delete(CpdType cpdType, loginProfile prof){
	    CpdType cpdType1 = cpdRegistrationMgtService.getCpdTypeByid(cpdType.getCt_id());//大牌删除前查询信息供日志使用
        List<CpdGroup> cpdGroups = cpdRegistrationMgtService.getCpdGroupMap(cpdType1.getCt_id());//小牌删除前查询信息供日志使用
	    
		cpdType.setCt_update_datetime(super.getDate());
		cpdType.setCt_update_usr_ent_id(prof.usr_ent_id);
		cpdType.setCt_status(CpdUtils.STATUS_DEL);
		cpdTypeMapper.delete(cpdType);
		//删除该大牌下对应的所有小牌
		cpdGroupService.deleteAllByCtID(cpdType.getCt_id(),prof);

        saveOrUpdOrDelTypeLog(prof, cpdType1, ObjectActionLog.OBJECT_ACTION_DEL);
        for (int i = 0; i < cpdGroups.size(); i++) {
            CpdGroup cpdGroup = cpdGroups.get(i);
            saveOrUpdOrDelGroupLog(prof, cpdType1, cpdGroup, ObjectActionLog.OBJECT_ACTION_DEL);
        }

		
	}
	
	/**
	 * 牌照排序
	 * @param sortStr
	 * @param prof
	 */
	public void infoSort(String sortStr, loginProfile prof){
		  String[] infoSortArray=sortStr.split(",");
		  for(int i=1;i<=infoSortArray.length;i++)
		  {
			  CpdType cpdType = new CpdType();
			  cpdType.setCt_update_datetime(super.getDate());
			  cpdType.setCt_update_usr_ent_id(prof.usr_ent_id);
			  cpdType.setCt_display_order(i);
			  cpdType.setCt_id(Long.parseLong(infoSortArray[i-1].trim()));
			  cpdTypeMapper.updateOrder(cpdType);
		  }
		
		
	};
	

	 /**
	  * 判断是否有课程关联
	  * @param ct_id
	  * @return
	  */
	public boolean getCountItemByCtID(long ct_id) {
		return aeItemCPDItemService.getCountItemByCtID(ct_id);
	}
	
	public List<CpdType> getAll() {
		CpdType ct = new CpdType();
		ct.setCt_status(CpdUtils.STATUS_OK);
		return cpdTypeMapper.getCpdType(ct);
	}
	
	 /**
     * 判断牌照是否有学员注册
     * @param ct_id
     * @return
     */
	public boolean getCountByCtID(long ct_id) {
		return cpdRegistrationMgtService.getCountByCtID(ct_id);
	}
	

	public CpdType getById(Long ctId) {
		return cpdTypeMapper.get(ctId);
	}
	
	   /**
     * 修改牌照最后一次发送邮件时间
     * @param sortStr
     * @param prof
     */
    public void updLastEmailSendTime(Timestamp sendTime,long ct_id){
         CpdType cpdType = new CpdType();
         cpdType.setCt_last_email_send_time(sendTime);
         cpdType.setCt_id(ct_id);
         cpdTypeMapper.updLastEmailSendTime(cpdType);
    };
	
       /**
      * 获取当天需要发送 CPT/D Outstanding Hours Email 邮件的大牌
      * @param sortStr
      * @param prof
      */
     public List<CpdType> getCpdTypeOutStandingEmail(int month,int date){
         Map<String, Object> map = new HashMap<String, Object>();
         map.put("month", month);
         map.put("date", date);
         List<CpdType> cpdTypes =  cpdTypeMapper.getCpdTypeOutStandingEmail(map);
         return cpdTypes;
     };
    
     
     /**
    * 大牌添加修改删除操作记录log
    * @param prof
    * @param cpdType
    * @param actionType //操作类型
    */
   public void saveOrUpdOrDelTypeLog(loginProfile prof,CpdType cpdType,String actionType){
       try {
           ObjectActionLog log = new ObjectActionLog();
           log.setObjectId(cpdType.getCt_id());//大牌ID
           log.setObjectCode(cpdType.getCt_license_type());//大牌类型
           log.setObjectTitle(cpdType.getCt_license_alias());//大牌别名
           log.setObjectType(ObjectActionLog.OBJECT_TYPE_CPD_TYPE);
           log.setObjectAction(actionType);//操作类型
           log.setObjectActionType(ObjectActionLog.OBJECT_ACTION_TYPE_WEB);
           log.setObjectOptUserId(prof.getUsr_ent_id());
           log.setObjectActionTime(DateUtil.getCurrentTime());
           log.setObjectOptUserLoginTime(prof.login_date);
           log.setObjectOptUserLoginIp(prof.ip);
           SystemLogContext.saveLog(log, SystemLogTypeEnum.OBJECT_ACTION_LOG);
       } catch (Exception e) {
           CommonLog.error("CPT/D  Type  exception:"+e.getMessage(),e);
           // TODO: handle exception
       }
   };
   
   
   /**
  * 小牌添加修改删除操作记录log
  * @param prof
  * @param cpdType
  * @param actionType //操作类型
  */
 public void saveOrUpdOrDelGroupLog(loginProfile prof,CpdType cpdType,CpdGroup cpdGroup,String actionType){
     try {
         ObjectActionLog log = new ObjectActionLog();
         log.setObjectId(cpdGroup.getCg_id());//小牌ID
         log.setObjectCode(cpdGroup.getCg_code());//小牌组名
         log.setObjectTitle(cpdType.getCt_license_alias()+"-->"+cpdGroup.getCg_alias());//大牌别名+小牌别名
         log.setObjectType(ObjectActionLog.OBJECT_TYPE_CPD_GROUP);
         log.setObjectAction(actionType);//操作类型
         log.setObjectActionType(ObjectActionLog.OBJECT_ACTION_TYPE_WEB);
         log.setObjectOptUserId(prof.getUsr_ent_id());
         log.setObjectActionTime(DateUtil.getCurrentTime());
         log.setObjectOptUserLoginTime(prof.login_date);
         log.setObjectOptUserLoginIp(prof.ip);
         SystemLogContext.saveLog(log, SystemLogTypeEnum.OBJECT_ACTION_LOG);
     } catch (Exception e) {
         CommonLog.error("CPT/D  Group  exception:"+e.getMessage(),e);
         // TODO: handle exception
     }
 };
  
     
     

}
