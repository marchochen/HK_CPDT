package com.cwn.wizbank.cpd.service;


import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.cpd.utils.CpdUtils;
import com.cwn.wizbank.cpd.vo.CpdPeriodVO;
import com.cwn.wizbank.entity.CpdGroup;
import com.cwn.wizbank.entity.CpdGroupHours;
import com.cwn.wizbank.entity.CpdGroupPeriod;
import com.cwn.wizbank.entity.CpdGroupRegistration;
import com.cwn.wizbank.entity.CpdType;
import com.cwn.wizbank.entity.ObjectActionLog;
import com.cwn.wizbank.persistence.CpdGroupHoursMapper;
import com.cwn.wizbank.persistence.CpdGroupMapper;
import com.cwn.wizbank.persistence.CpdGroupPeriodMapper;
import com.cwn.wizbank.persistence.CpdTypeMapper;
import com.cwn.wizbank.services.BaseService;
import com.cwn.wizbank.systemLog.SystemLogContext;
import com.cwn.wizbank.systemLog.SystemLogTypeEnum;
import com.cwn.wizbank.utils.DateUtil;
import com.cwn.wizbank.utils.LabelContent;
import com.cwn.wizbank.utils.Page;




@Service
public class CpdGroupPeriodService extends BaseService<CpdGroupPeriod> {

	@Autowired
	CpdGroupMapper cpdGroupMapper;
	
	@Autowired
	CpdGroupPeriodMapper cpdGroupPeriodMapper;
	
	@Autowired
	CpdGroupHoursMapper cpdGroupHoursMapper;
	
	@Autowired
	CpdTypeMapper cpdTypeMapper;
	
	@Autowired
	CpdUtilService cpdUtilService;
	
	@Autowired
	CpdGroupRegistrationService cpdGroupRegistrationService;
	
    /**
     * 判断设置的日期是否已经存在
     * @param cpdGroupPeriod
     * @return
     */
	public boolean isExistForTime(CpdGroupPeriod cpdGroupPeriod,int statrData, int statrMonth ) {
		cpdGroupPeriod.setCgp_effective_time(DateUtil.getInstance().getDate(statrData, statrMonth, 1));
		return cpdGroupPeriodMapper.isExistForTime(cpdGroupPeriod);
	}

	/**
	 * 通过cgp_id获取CPT/D牌照组別（小牌）周期信息
	 * @param cgp_id
	 * @return
	 */
	public CpdGroupPeriod getByCgpId(long cgp_id){
		CpdGroupPeriod cpdGroupPeriod =  cpdGroupPeriodMapper.get(cgp_id);
		cpdGroupPeriod.setCpdGroupHours(cpdGroupHoursMapper.getByCghCgpID(cpdGroupPeriod.getCgp_id()));
		return cpdGroupPeriod;
	}
	
	
	
	/**
	 * 新增/修改时间
	 * @param cpdGroupPeriod
	 * @param prof
	 */
	public void saveOrUpdate(CpdGroupPeriod cpdGroupPeriod, loginProfile prof, int statrData, int statrMonth ) {
		
		cpdGroupPeriod.setCgp_effective_time(DateUtil.getInstance().getDate(statrData, statrMonth, 1));
		//id 不存在  执行新增操作
		if (cpdGroupPeriod.getCgp_id() == null || cpdGroupPeriod.getCgp_id().equals("")) {
			cpdGroupPeriod.setCgp_create_datetime(super.getDate());
			cpdGroupPeriod.setCgp_create_usr_ent_id(prof.usr_ent_id);
			cpdGroupPeriod.setCgp_status(CpdUtils.STATUS_OK);
			cpdGroupPeriodMapper.insertReturnID(cpdGroupPeriod);
			
			for(int i = 0; i < cpdGroupPeriod.getCpdGroupHours().size(); i++ ){
				if(null != cpdGroupPeriod.getCpdGroupHours().get(i) ){
					if(null != cpdGroupPeriod.getCpdGroupHours().get(i).getCgh_declare_month() && 0 != cpdGroupPeriod.getCpdGroupHours().get(i).getCgh_declare_month()){
						cpdGroupPeriod.getCpdGroupHours().get(i).setCgh_cgp_id(cpdGroupPeriod.getCgp_id());
						cpdGroupPeriod.getCpdGroupHours().get(i).setCgh_create_usr_ent_id(prof.usr_ent_id);
						cpdGroupPeriod.getCpdGroupHours().get(i).setCgh_create_datetime(super.getDate());
						cpdGroupPeriod.getCpdGroupHours().get(i).setCgh_status(CpdUtils.STATUS_OK);
						cpdGroupHoursMapper.insert(cpdGroupPeriod.getCpdGroupHours().get(i));
					}
				}
			}
			//记录操作日志   新增所需时数
			ObjectActionLog log = new ObjectActionLog();
			log.setObjectId(cpdGroupPeriod.getCgp_id());
			log.setObjectCode(""+DateUtil.getInstance().getDateYear(cpdGroupPeriod.getCgp_effective_time()));
			log.setObjectAction(ObjectActionLog.OBJECT_ACTION_ADD);
			log.setObjectActionType(ObjectActionLog.OBJECT_ACTION_TYPE_WEB);
			log.setObjectOptUserId(prof.getUsr_ent_id());
			log.setObjectActionTime(DateUtil.getCurrentTime());
			log.setObjectTitle(cpdGroupPeriod.getCt_name()+"-->"+cpdGroupPeriod.getCg_name()+"-->"+LabelContent.get(prof.cur_lan, "label_core_cpt_d_management_96"));
			log.setObjectType(ObjectActionLog.OBJECT_TYPE_CPD_GROUP_HOURS);
			log.setObjectOptUserLoginTime(prof.login_date);
			log.setObjectOptUserLoginIp(prof.ip);
			SystemLogContext.saveLog(log, SystemLogTypeEnum.OBJECT_ACTION_LOG);
			
		} else {
			//id 存在  执行修改操作
			cpdGroupPeriod.setCgp_update_datetime(super.getDate());
			cpdGroupPeriod.setCgp_update_usr_ent_id(prof.usr_ent_id);
			super.update(cpdGroupPeriod);
			
			for(int i = 0; i < cpdGroupPeriod.getCpdGroupHours().size(); i++ ){
				if(null != cpdGroupPeriod.getCpdGroupHours().get(i) ){
					if(null != cpdGroupPeriod.getCpdGroupHours().get(i).getCgh_declare_month() && 0 != cpdGroupPeriod.getCpdGroupHours().get(i).getCgh_declare_month()){
						cpdGroupPeriod.getCpdGroupHours().get(i).setCgh_update_usr_ent_id(prof.usr_ent_id);
						cpdGroupPeriod.getCpdGroupHours().get(i).setCgh_update_datetime(super.getDate());
						cpdGroupHoursMapper.update(cpdGroupPeriod.getCpdGroupHours().get(i));
					}
				}
			 }
			//小牌ID
			Long cgId = cpdGroupPeriod.getCgp_cg_id();
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			int period = c.get(Calendar.YEAR);
			List<CpdGroupRegistration>  periodCgrList = cpdGroupRegistrationService.getByCgId(cgId,period);
			//大牌信息
			CpdType cpdType = cpdTypeMapper.get(cpdGroupPeriod.getCgp_ct_id());
			//小牌信息
			CpdGroup cpdGroup = cpdGroupMapper.get(cpdGroupPeriod.getCgp_cg_id());
            //获取周期信息
			if(null!=periodCgrList && periodCgrList.size()>0){
				for(CpdGroupRegistration cgr : periodCgrList){
					//上一个周期
                    CpdGroupRegistration preCpdGroupRegistration =  cpdUtilService.getPreGroupRegistration(cgr.getCgr_cg_id(), cgr.getCgr_usr_ent_id(), cgr.getCgr_initial_date());
	                //获取周期信息
                    CpdPeriodVO prePeriod = null;
                    if(preCpdGroupRegistration!=null){
                        prePeriod = cpdUtilService.getPeriod(preCpdGroupRegistration.getCgr_initial_date(), cpdType.getCt_id());
                    }
					cpdUtilService.calReqHours(cgr, cpdType, cpdGroup, cpdGroupPeriod, CpdUtils.REQUIRE_HOURS_ACTION_RECAL, prePeriod, prof.getUsr_ent_id());
					
				}
			}
			
			//记录操作日志  修改所需时数
			ObjectActionLog log = new ObjectActionLog();
			log.setObjectId(cpdGroupPeriod.getCgp_id());
			log.setObjectCode(""+DateUtil.getInstance().getDateYear(cpdGroupPeriod.getCgp_effective_time()));
			log.setObjectAction(ObjectActionLog.OBJECT_ACTION_UPD);
			log.setObjectActionType(ObjectActionLog.OBJECT_ACTION_TYPE_WEB);
			log.setObjectOptUserId(prof.getUsr_ent_id());
			log.setObjectActionTime(DateUtil.getCurrentTime());
			log.setObjectTitle(cpdGroupPeriod.getCt_name()+"-->"+cpdGroupPeriod.getCg_name()+"-->"+LabelContent.get(prof.cur_lan, "label_core_cpt_d_management_96"));
			log.setObjectType(ObjectActionLog.OBJECT_TYPE_CPD_GROUP_HOURS);
			log.setObjectOptUserLoginTime(prof.login_date);
			log.setObjectOptUserLoginIp(prof.ip);
			SystemLogContext.saveLog(log, SystemLogTypeEnum.OBJECT_ACTION_LOG);
		}
	}
	
	
	/**
	 * 查询小牌下所有牌照生效时间信息
	 * 功能是属于一个与培训中心无关的功能，即拥有功能权限的角色可以管理所有牌照信息
	 * @param page
	 * @param cg_id
	 * @return
	 */
	public Page<CpdGroupPeriod> searchAll(Page<CpdGroupPeriod> page,long cg_id) {
		page.getParams().put("cg_id", cg_id);
		cpdGroupPeriodMapper.searchAll(page);
		return page;
	}
	
	/**
	 * 删除生效日期以及所设置的时间
	 * @param cpdGroupPeriod
	 * @param prof
	 */
	public void delete(CpdGroupPeriod cpdGroupPeriod, loginProfile prof){
		cpdGroupPeriod.setCgp_update_datetime(super.getDate());
		cpdGroupPeriod.setCgp_update_usr_ent_id(prof.usr_ent_id);
		cpdGroupPeriod.setCgp_status(CpdUtils.STATUS_DEL);
		cpdGroupPeriodMapper.delete(cpdGroupPeriod);
		
		CpdGroupHours cpdGroupHours = new CpdGroupHours();
		cpdGroupHours.setCgh_status(CpdUtils.STATUS_DEL);
		cpdGroupHours.setCgh_update_datetime(super.getDate());
		cpdGroupHours.setCgh_update_usr_ent_id(prof.usr_ent_id);
		cpdGroupHours.setCgh_cgp_id(cpdGroupPeriod.getCgp_id());
		cpdGroupHoursMapper.deleteByCgpId(cpdGroupHours);
		
		//记录操作日志  删除所需时数
		ObjectActionLog log = new ObjectActionLog();
		log.setObjectId(cpdGroupPeriod.getCgp_id());
		log.setObjectCode(cpdGroupPeriod.getPeriod());
		log.setObjectAction(ObjectActionLog.OBJECT_ACTION_DEL);
		log.setObjectActionType(ObjectActionLog.OBJECT_ACTION_TYPE_WEB);
		log.setObjectOptUserId(prof.getUsr_ent_id());
		log.setObjectActionTime(DateUtil.getCurrentTime());
		log.setObjectTitle(cpdGroupPeriod.getCt_name()+"-->"+cpdGroupPeriod.getCg_name()+"-->"+LabelContent.get(prof.cur_lan, "label_core_cpt_d_management_96"));
		log.setObjectType(ObjectActionLog.OBJECT_TYPE_CPD_GROUP_HOURS);
		log.setObjectOptUserLoginTime(prof.login_date);
		log.setObjectOptUserLoginIp(prof.ip);
		SystemLogContext.saveLog(log, SystemLogTypeEnum.OBJECT_ACTION_LOG);
	}
	
	
	
	
}
