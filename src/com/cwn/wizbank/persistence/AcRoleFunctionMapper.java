package com.cwn.wizbank.persistence;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.junit.runners.Parameterized.Parameters;

import com.cwn.wizbank.entity.AcFunction;
import com.cwn.wizbank.entity.AcRoleFunction;


public interface AcRoleFunctionMapper extends BaseMapper<AcRoleFunction>{

	public int hasRoleFunction(Map<String,String> map);	
	public int hasFunction(Map<String,String> map);
	public List<AcFunction> getFunctions(String currentRole);
	public void deleteAll();
	public void truncate();
	public List<AcFunction> getMenusMarkFavorite(Map<String,Object> map);
	public List<AcFunction> roleFavoriteFunctionJson(String current_role);
	
	public List<AcFunction> getRoleFunctions(Integer ftn_id);
	/**管理员端角色管理删除角色*/
	public void delRolFunById(Long rfn_rol_id);
	
	/**删除角色的所有功能，但不删除角色固化功能*/
	public void delRolFunByIdExtRolFun(Long rfn_rol_id);
	
	
	/**查询当前角色所拥有的功能*/
	public List<AcFunction> getRoleHasFunctions(Long role_id);
}