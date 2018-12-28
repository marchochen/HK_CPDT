package com.cwn.wizbank.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.entity.AcRole;
import com.cwn.wizbank.entity.EnterpriseInfoPortal;
import com.cwn.wizbank.entity.TcTrainingCenter;
import com.cwn.wizbank.entity.UserGroup;
import com.cwn.wizbank.exception.DataNotFoundException;
import com.cwn.wizbank.persistence.EnterpriseInfoPortalMapper;

@Service
public class EnterpriseInfoPortalService extends BaseService<EnterpriseInfoPortal>{
	
	@Autowired
	EnterpriseInfoPortalMapper eipMapper;
	@Autowired
	UserGroupService userGroupService;
	@Autowired
	TcTrainingCenterTargetEntityService tcTrainingCenterTargetEntityService;
	@Autowired
	TcTrainingCenterService tcTrainingCenterService;
	
	@Autowired
	AcRoleService acRoleService;
	
	private static final Logger logger = LoggerFactory.getLogger(EnterpriseInfoPortalService.class);

	public void setEipMapper(EnterpriseInfoPortalMapper eipMapper) {
		this.eipMapper = eipMapper;
	}
	
	public Long getTcrByDomain(String domain){
		EnterpriseInfoPortal eip = eipMapper.getEipByDomain(domain);
		if(eip!=null){
			return eip.getEip_tcr_id();
		}
		return null;
	}
	
	/**
	 * 当外部通过调用API维护用户信息时，通过方式API判断当前用户有没有权限查看、管理该企业下面的用户
	 * @param eip_code 企业编号，当非LNOW模式该值效
	 * @param userEntId
	 * @return
	 */
	
	public boolean canManageInterprise(WizbiniLoader wizbini,loginProfile prof,String eip_code){
		
		boolean result = false;
		
		if(wizbini.cfgSysSetupadv.isTcIndependent() && (eip_code == null || eip_code.trim().length() < 1)){
			//LNOW模式，必须指定企业号
			return false;
		}
		
		if(acRoleService.isSysadmin(prof.usr_ent_id)){
			//如果是系统管理员，则可以管理所有用户
			result = true;
		}else if (acRoleService.isTadmin(prof.usr_ent_id)){
			//如果是培训管理员
			TcTrainingCenter rootTc =  tcTrainingCenterService.getRootTrainingCenter();
			if(rootTc != null && tcTrainingCenterService.isTcOfficer(rootTc.getTcr_id(), prof.usr_ent_id,AcRole.ROLE_TADM_1)){
				//最项层培训中心管理员有可以管理所有用户
				result = true;
			}
			else if(!wizbini.cfgSysSetupadv.isTcIndependent()  ){
				//非LNOW模式，只有最顶级培训中心才有权限
				result = false;
			}
			else{
				if(eip_code == null || eip_code.trim().length() < 1){
					result = false;
				}else{
					EnterpriseInfoPortal dbEpi = eipMapper.getByCode(eip_code);
					if(dbEpi == null || !tcTrainingCenterService.isTcOfficer(dbEpi.getEip_tcr_id(), prof.usr_ent_id,AcRole.ROLE_TADM_1)){
						//如果没有这样的企业或都不是该企业的管理员
						result = false;
					}else{
						result = true;
					}
				}
			}
		}else{
			//非管理员角色
			result = false;
		}
		return result;
	}

	/**
	 * 创建企业
	 * @param eip
	 * @param userEntId
	 * @return
	 */
	public List<Long> addEip(EnterpriseInfoPortal eip, loginProfile prof) {
		List<Long> usgEntIds = new ArrayList<Long>();
		if(eip.getEip_code() == null ){
			logger.debug("eip code 是空，直接返回空");
			return null;
		}
		EnterpriseInfoPortal dbEpi = eipMapper.getByCode(eip.getEip_code());
		if(dbEpi != null && dbEpi.getEip_id() != null) {
			logger.debug("数据库中 eip id : " + dbEpi.getEip_id());

			//是否与培训中心关联
			Long tcrId = dbEpi.getEip_tcr_id();
			logger.debug("tcrId : " + tcrId);
			if(tcrId != null && tcrId > 0) {
				//培训中心是否用户组关联
				logger.debug("培训中心是否用户组关联");
				List<UserGroup> groups = userGroupService.getListByTcrId(tcrId);
				if(groups != null && groups.size() > 0) {
					//返回用户组id
					for(UserGroup ug : groups){
						logger.debug("group  : " + ug.getUsg_ent_id());
						usgEntIds.add(ug.getUsg_ent_id());
					}
					return usgEntIds;
				} else {
					logger.debug("创建用户组，并把用户组和培训个中心关联，用户组编号直接使用ent_id ");

					//创建用户组，并把用户组和培训个中心关联，用户组编号直接使用ent_id
					long usgEntId = userGroupService.create(eip.getEip_name(), 1, prof.usr_id);
					//关联
					tcTrainingCenterTargetEntityService.create(tcrId, usgEntId, prof.usr_id);
					
					usgEntIds.add(usgEntId);
					return usgEntIds;
				}
				
			} else {
				logger.debug("创建培训中心，用户组，并把用户组和培训中心关联");

				//创建培训中心，用户组，并把用户组和培训中心关联
				tcrId = tcTrainingCenterService.create(eip.getEip_code(), eip.getEip_name(), 1, prof.usr_ent_id, prof.usr_id);
				//创建用户组
				long usgEntId = userGroupService.create(eip.getEip_name(), 1, prof.usr_id);
				//关联
				tcTrainingCenterTargetEntityService.create(tcrId, usgEntId, prof.usr_id);
				
				usgEntIds.add(usgEntId);
				return usgEntIds;
			}
			
		} else {
			logger.debug("创建企业，培训中心，用户组");
			//创建企业，培训中心，用户组
			//创建培训中心
			long tcrId = tcTrainingCenterService.create(eip.getEip_code(), eip.getEip_name(), 1, prof.usr_ent_id , prof.usr_id);
			//创建企业
			eip.setEip_account_num(1000l);
			eip.setEip_tcr_id(tcrId);
			eip.setEip_status("ON");
			eip.setEip_create_timestamp(getDate());
			eip.setEip_update_timestamp(getDate());
			eip.setEip_create_usr_id(prof.usr_id);
			eip.setEip_update_usr_id(prof.usr_id);
			this.add(eip);
			
			//创建用户组
			long usgEntId = userGroupService.create(eip.getEip_name(), 1, prof.usr_id);
			//关联
			tcTrainingCenterTargetEntityService.create(tcrId, usgEntId, prof.usr_id);
			
			usgEntIds.add(usgEntId);
			return usgEntIds;
		}
	}
	
	
	public boolean editEip(EnterpriseInfoPortal eip, long userEntId) throws DataNotFoundException{
		if(eip.getEip_code() == null ){
			logger.debug("eip code 是空，直接返回空");
			return false;
		}
		EnterpriseInfoPortal dbEip = eipMapper.getByCode(eip.getEip_code());
		if(dbEip != null && StringUtils.isNotEmpty(dbEip.getEip_code())) {
			TcTrainingCenter tc = tcTrainingCenterService.get(dbEip.getEip_tcr_id());
			List<UserGroup> groups = userGroupService.getListByTcrId(dbEip.getEip_tcr_id());
			
			if(StringUtils.isNotEmpty(eip.getEip_name())) {
				dbEip.setEip_name(eip.getEip_name());
				tc.setTcr_title(eip.getEip_name());
				for(UserGroup ug : groups) {
					ug.setUsg_display_bil(eip.getEip_name());
					userGroupService.update(ug);
				}
			}
			eipMapper.update(dbEip);
			tcTrainingCenterService.update(tc);
		} else {
			
			throw new DataNotFoundException("不存在该企业");
		}
		return true;
	}

	public EnterpriseInfoPortalMapper getEipMapper() {
		return eipMapper;
	}
	
	/**
	 * 获取所有企业列表
	 * @return
	 */
	public List<EnterpriseInfoPortal> getEnterpriseInfoPortalList(){
		return this.eipMapper.getEnterpriseInfoPortalList();
	}
	
	/**
	 * 根据培训中心获取直播并发数
	 * 
	 */
	public long getLiveMaxCountByTcrId(long tcr_id){
		long live_max_count = 0;
		List<EnterpriseInfoPortal> list =  eipMapper.getLiveMaxCountByTcrId(tcr_id);
		if(list != null && list.size() > 0){
			if(list.get(0).getEip_live_max_count() != null && list.get(0).getEip_live_max_count() > 0){
				live_max_count = list.get(0).getEip_live_max_count().longValue();
			}
		}else{
			live_max_count = -1;
		}
		return live_max_count;
	}
	
	public boolean checkTcrOccupancy(long tcr_id){
		boolean flag = false;
		List<EnterpriseInfoPortal> list = this.eipMapper.getEnterpriseInfoPortalListByTcrId(tcr_id);
		if(list != null){
			if(list.size() > 0 && list.get(0) != null){
				flag = true;
			}
		}
		return flag;
	}
	
	/**
	 * 获取企业直播模式
	 */
	public String[] getEipLiveMode(long tcr_id){
		String[] liveModes = null;
		List<EnterpriseInfoPortal> list = this.eipMapper.getEnterpriseInfoPortalListByTcrId(tcr_id);
		if(list != null){
			if(list.size() > 0 && list.get(0) != null){
				String liveMode = list.get(0).getEip_live_mode();
				if(liveMode != null){
					if(liveMode.indexOf(",") != -1){
						liveModes = liveMode.split(",");
					}else{
						liveModes = new String[]{liveMode};
					}
				}
			}
		}		
		return liveModes;
	}
	
	/**
	 * 获取企业SecretId，SecretKey
	 */
	public Map<String, Object> getEipLiveSecretInfo(long tcr_id){
		Map<String, Object> liveSecret = new HashMap<String, Object>();
		List<EnterpriseInfoPortal> list = this.eipMapper.getEnterpriseInfoPortalListByTcrId(tcr_id);
		if(list != null){
			if(list.size() > 0 && list.get(0) != null){
				String SecretId = list.get(0).getEip_live_qcloud_secretid().trim();
				String SecretKey = list.get(0).getEip_live_qcloud_secretkey().trim();
				if(SecretId != null && SecretKey != null && !"".equals(SecretId) &&  !"".equals(SecretKey)){
					liveSecret.put("SecretFlag", true);
				}else{
					liveSecret.put("SecretFlag", false);
				}
				liveSecret.put("SecretId", SecretId != null ? SecretId : "");
				liveSecret.put("SecretKey", SecretKey != null ? SecretKey : "");
			}
		}		
		return liveSecret;
	}

}
