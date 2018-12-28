package com.cwn.wizbank.persistence;

import java.util.List;
import java.util.Map;

import com.cwn.wizbank.entity.AcRole;
import com.cwn.wizbank.utils.Page;


public interface AcRoleMapper extends BaseMapper<AcRole>{

	/**
	 * 获取当前用户的角色
	 * @param map
	 * @return
	 */
	List<AcRole> getUserRoles(Map<String, Object> map);
	
	int hasRole(Map<String, Object> map);

	AcRole getByExtId(String rol);
	
	/**管理员端获取角色列表*/
	List<AcRole> selectRole(Page<AcRole> page);
	
	/**新增角色*/
	void add(AcRole acRole);
	
	/**获取role表中最大的ID值*/
	long getMaxRoleId();

	void addRoleFunction(AcRole acRole);

//	void insAcHomePageFtn(String rol_ext_id);

	long hasRoleFunction(String rol_ext_id, String ftn_ext_id);

	void insReportForRole(AcRole acRole);

	void delRoleHomepageFunction(Long role_id);

	void delRoleRelationTrainningCentre(Long role_id);

	void delOtherFunction(Long role_id);

	void updateRoleFunction(AcRole acRole);

	void updateAcRole(AcRole acRole);

	void updateAcFunction(Long rol_id);


	String getRole(Map<String, Object> map);

	/**
	 * 根据Title来查询是否存在名字相同的对象
	 * @param title
	 * @return
	 */
	Integer findByTitle(String title);
	
	/**
	 * 获取有TADM_1权限的管理员ID
	 * @return
	 */
	Long getEntSysUser();
	
	/**
	 * 修改 验证是否存在名字相同的对象
	 * @param acRole
	 * @return
	 */
	Integer isExistForRolTitle(AcRole acRole);

}