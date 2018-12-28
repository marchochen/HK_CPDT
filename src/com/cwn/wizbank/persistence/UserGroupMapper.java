package com.cwn.wizbank.persistence;

import java.util.List;
import java.util.Map;

import com.cwn.wizbank.entity.UserGroup;
import com.cwn.wizbank.entity.vo.UserGroupVo;
import com.cwn.wizbank.utils.Params;

public interface UserGroupMapper extends BaseMapper<UserGroup>{
	
	String selectUsgId(String usg_code);

	List<UserGroupVo> selectList(Params params);

	List<UserGroup> selectListByTcrId(long tcrId);

	UserGroup getByUsgCode(String usg_code);

	boolean hasChild(long usgCode);

	long getUserGroupCountInTCent(long tcrId);
	
	Integer groupHasChild(Map map);
	
	//根据用户ID获取用户能管理的用户组
	List<UserGroup> getGroupByUserId(Map map);
	
	/**
	 * 获取child_ent_id及child_ent_id所有父用户组名称的集合
	 * @param child_ent_id
	 * @return
	 */
	List<String> selectGroupLevelList(long child_ent_id);
	
	List<UserGroup> getSubordinateGroup(long usr_ent_id);
}
