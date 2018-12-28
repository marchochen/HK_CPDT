package com.cwn.wizbank.persistence;

import java.util.List;
import java.util.Map;

import com.cwn.wizbank.entity.RegUser;
import com.cwn.wizbank.entity.SnsGroupMember;
import com.cwn.wizbank.utils.Page;


public interface SnsGroupMemberMapper extends BaseMapper<SnsGroupMember>{

	List<SnsGroupMember> selectSnsGroupMemberList(Page<SnsGroupMember> page);
	
	void update(SnsGroupMember snsGroupMember);
	
	List<RegUser> findNotJoinGroupMemberList(Page<RegUser> page);
	
	void delete(SnsGroupMember snsGroupMember);
	
	void updateManager (SnsGroupMember snsGroupMember);
	
	SnsGroupMember isThisGroupMember(SnsGroupMember snsGroupMember);

	/**
	 *  判断该用户是否已经加入该组了
	 * @param map
	 * @return
	 */
	SnsGroupMember getByGroupIdAndUserId(Map map);

	void deleteAll(long s_grp_id);
	
}