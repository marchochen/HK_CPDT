package com.cwn.wizbank.services;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.entity.AcFunction;
import com.cwn.wizbank.entity.AcRole;
import com.cwn.wizbank.exception.MessageException;
import com.cwn.wizbank.persistence.AcFunctionMapper;
import com.cwn.wizbank.persistence.AcRoleFunctionMapper;
import com.cwn.wizbank.persistence.AcRoleMapper;
import com.cwn.wizbank.utils.Page;
/**
 *  service 实现
 */
@Service
public class AcRoleService extends BaseService<AcRole> {

	
	public static final String EXIST = "EXIST";
	
	@Autowired
	AcRoleMapper acRoleMapper;
	
	@Autowired
	AcRoleFunctionMapper acRoleFunctionMapper;
	
	@Autowired
	AcFunctionMapper acFunctionMapper;

	/**
	 * 所有的角色
	 * @param userEntId
	 * @return
	 */
	public List<AcRole> getUserRoles(long userEntId){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userEntId" , userEntId);
		map.put("curDate", getDate());
		return acRoleMapper.getUserRoles(map);
	}
	/**
	 * 是否包含该角色
	 * @param userEntId
	 * @param rolExtId
	 * @return
	 */
	public int hasRole(long userEntId, String rolExtId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userEntId" , userEntId);
		map.put("curDate", getDate());
		map.put("rolExtId", rolExtId);
		return acRoleMapper.hasRole(map);
	}
	
	/**
	 * 是否只有该角色
	 * @param userEntId
	 * @param rolExtId
	 * @return
	 */
	public boolean isRoleOnly(long userEntId, String rolExtId) {
		List<AcRole> roles = this.getUserRoles(userEntId);
		return (roles != null && roles.size() == 1 && roles.get(0).getRol_ext_id().equalsIgnoreCase(rolExtId));
	}
	
	/**
	 * 是否包含该角色
	 * @param userEntId
	 * @param rolExtId
	 * @return
	 */
	public boolean hasRole(long userEntId, String[] rolExtIds) {
		boolean hasRole = false;
		for(String rol : rolExtIds) {
			if(hasRole = this.hasRole(userEntId, rol) > 0)
			break;
		}
		return hasRole;
	}
	/**
	 * 是否包含讲师角色
	 * @param userEntId
	 * @return
	 */
	public boolean isInstructor(long userEntId){
		return this.hasRole(userEntId, AcRole.ROLE_INSTR_1) > 0;
	}
	
	
	/**
	 * 是否是培训管理员
	 * @param userEntId
	 * @return
	 */
	public boolean isTadmin(long userEntId){
		return this.hasRole(userEntId, AcRole.ROLE_TADM_1) > 0;
	}
	
	/**
	 * 是否是培训管理员
	 * @param userEntId
	 * @return
	 */
	public boolean isSysadmin(long userEntId){
		return this.hasRole(userEntId, AcRole.ROLE_ADM_1) > 0;
	}
	
	
	/**
	 * 是否只有学员角色
	 * @param userEntId
	 * @return
	 */
	public boolean isLearnerOnly(long userEntId){
		return this.hasRole(userEntId, AcRole.ROLE_ADM_1) == 0;
	}
	public AcRoleMapper getAcRoleMapper() {
		return acRoleMapper;
	}
	public void setAcRoleMapper(AcRoleMapper acRoleMapper) {
		this.acRoleMapper = acRoleMapper;
	}
	
	
	/**管理员端角色列表**/
	public Page<AcRole> getList(Page<AcRole> page, loginProfile prof) {
		page.getParams().put("root_ent_id", prof.root_ent_id);
		acRoleMapper.selectRole(page);
		return page;
	}
	
	/**管理员端新增角色
	 * @throws MessageException */
	public void save(AcRole acRole, loginProfile prof,String type) throws MessageException {
		acRole.setRol_title(acRole.getRol_title());
		acRole.setRol_tc_ind(acRole.getRol_tc_ind());
		acRole.setRol_ext_id("ROL" + getMaxRoleId());
		acRole.setRol_ste_ent_id(prof.getRoot_ent_id());
		acRole.setRol_ste_default_ind(false);
		acRole.setRol_skin_root("skin1");
		acRole.setRol_status("OK");
		acRole.setRol_url_home("app/admin/home");
		acRole.setRol_report_ind(false);
		acRole.setRol_seq_id(0L);
		acRole.setRol_create_usr_id(prof.getUsr_id());
		acRole.setRol_update_usr_id(prof.getUsr_id());
		acRole.setRol_create_timestamp(getDate());
		acRole.setRol_update_timestamp(getDate());
		acRole.setRol_auth_level((long)acRole.ROLE_CUSTOM_LEVEL);
		
		String ftn_id_lst = acRole.getFtn_id_lst();
		String[] str = ftn_id_lst.split("~", 0);
		AcFunction acFunction = new AcFunction();
		
		if(type != null && type.equals("update")){
			
			Integer exist = acRoleMapper.isExistForRolTitle(acRole);
			if(exist.intValue()>0)
			{
				throw new MessageException(EXIST);
			}
			//修改角色表
			acRoleMapper.updateAcRole(acRole);
			//先删后添加
			acRoleFunctionMapper.delRolFunByIdExtRolFun(acRole.getRol_id());
			for(int i = 0;i<str.length;i++){
				
				acFunction.setFtn_id(Integer.valueOf(str[i]));
				acFunction = acFunctionMapper.get(acFunction.getFtn_id());
				acRole.setAcFunction(acFunction);
				acRole.setRol_id(acRole.getRol_id());
				//添加角色权限功能表acRoleFunction
				acRoleMapper.addRoleFunction(acRole);
			}
		}else{
			//添加角色表acRole
			
			
			Integer exist = acRoleMapper.findByTitle(acRole.getRol_title());
			if(exist.intValue()>0)
			{
				throw new MessageException(EXIST);
			}
			
			
			
			acRoleMapper.add(acRole);
			for(int i = 0;i<str.length;i++){
				acFunction.setFtn_id(Integer.valueOf(str[i]));
				acFunction = acFunctionMapper.get(acFunction.getFtn_id());
				acRole.setAcFunction(acFunction);
				acRole.setRol_id(getMaxRoleId());
				//添加角色权限功能表acRoleFunction
				acRoleMapper.addRoleFunction(acRole);
			}
		}
		
		//添加homepage 表
//		if(acRole.getRol_ext_id() != null && acRole.getRol_ext_id().length() > 0){
//			acRoleMapper.insAcHomePageFtn(acRole.getRol_ext_id());
//		}
		
//		if(hasRoleFunction(acRole.getRol_ext_id(),"RPT_LINK")){
//			//角色报表
//			acRoleMapper.insReportForRole(acRole);
//		}
	}

	/**获取到role 角色表当前最大的ID值*/
	public long getMaxRoleId(){
		
		return acRoleMapper.getMaxRoleId();
	}
	/**根据id删除角色*/
	public void delById(Long role_id) {
		acRoleFunctionMapper.delRolFunById(role_id);
		//删除关联表
//		acRoleMapper.delRoleHomepageFunction(role_id);
		acRoleMapper.delRoleRelationTrainningCentre(role_id);
		//删除关联的acreporttemplate表
//		acRoleMapper.delOtherFunction(role_id);
		acRoleMapper.delete(role_id);
	}

	public String getRole(long usr_ste_ent_id, String rol_ste_uid) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("usr_ste_ent_id", usr_ste_ent_id);
		map.put("rol_ste_uid", rol_ste_uid);
		return this.acRoleMapper.getRole(map);
	}
	
	
	public boolean isRoleTcInd(String rol_ste_uid) {
		AcRole role = this.acRoleMapper.getByExtId(rol_ste_uid);
		return role.getRol_tc_ind() == 1 ? true: false;
	}
	
	public Long getEntSysUser(){
		return acRoleMapper.getEntSysUser();
	}


}