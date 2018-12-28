package com.cwn.wizbank.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cw.wizbank.JsonMod.credit.Credit;
import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.common.SNS;
import com.cwn.wizbank.entity.RegUser;
import com.cwn.wizbank.entity.SnsGroupMember;
import com.cwn.wizbank.persistence.SnsGroupMemberMapper;
import com.cwn.wizbank.utils.ImageUtil;
import com.cwn.wizbank.utils.Page;
/**
 * service 实现
 */
@Service
public class SnsGroupMemberService extends BaseService<SnsGroupMember> {

	@Autowired
	SnsGroupMemberMapper snsGroupMemberMapper;

	@Autowired
	SnsDoingService snsDoingService;

	@Autowired
	ForCallOldAPIService forCallOldAPIService;

	public void setSnsGroupMemberMapper(SnsGroupMemberMapper snsGroupMemberMapper) {
		this.snsGroupMemberMapper = snsGroupMemberMapper;
	}

	/**
	 * 添加群组成员
	 * @param s_grp_id 群组id
	 * @param usr_ent_id 成员id
	 * @param s_gpm_status 成员状态
	 * @param s_gpm_type 成员类型
	 */
	public void addSnsGroupMember(long s_grp_id, long usr_ent_id, long s_gpm_status, long s_gpm_type){
		SnsGroupMember snsGroupMember = new SnsGroupMember();
		snsGroupMember.setS_gpm_grp_id(s_grp_id);
		snsGroupMember.setS_gpm_usr_id(usr_ent_id);
		snsGroupMember.setS_gpm_join_datetime(getDate());
		snsGroupMember.setS_gpm_status(s_gpm_status);
		snsGroupMember.setS_gpm_type(s_gpm_type);
		if(s_gpm_status == 0){
			snsGroupMember.setS_gpm_apply_datetime(getDate());
		}
		snsGroupMemberMapper.insert(snsGroupMember);
	}

	/**
	 * 获取群组成员列表
	 * @param page
	 * @param s_gpm_grp_id 群组id
	 * @param s_gpm_usr_id 成员id
	 * @param searchContent 搜索内容
	 * @param type
	 */
	public Page<SnsGroupMember> getSnsGroupMemberList(Page<SnsGroupMember> page, long s_gpm_grp_id, long s_gpm_usr_id, String type, String searchContent){
		page.getParams().put("s_gpm_grp_id", s_gpm_grp_id);
		if(s_gpm_usr_id > 0){
			page.getParams().put("s_gpm_usr_id", s_gpm_usr_id);
		}
		if(type.equalsIgnoreCase("admitted") || type.equalsIgnoreCase("myMember")){
			//已通过的用户
			page.getParams().put("s_gpm_status", 1);
			page.getParams().put("s_gpm_type", 2);
		} else if(type.equalsIgnoreCase("pending")){
			//等待中的用户
			page.getParams().put("s_gpm_status", 0);
			page.getParams().put("s_gpm_type", 2);
		} else if(type.equalsIgnoreCase("rejected")){
			//已解决的用户
			page.getParams().put("s_gpm_status", 3);
			page.getParams().put("s_gpm_type", 2);
		}
		if(type.equalsIgnoreCase("admitted") || type.equalsIgnoreCase("pending") || type.equalsIgnoreCase("rejected")){
			page.getParams().put("type", "approve");
		}
		page.getParams().put("searchContent", searchContent);
		snsGroupMemberMapper.selectSnsGroupMemberList(page);
		for(SnsGroupMember snsGroupMember : page.getResults()){
			ImageUtil.combineImagePath(snsGroupMember.getUser());
		}

		return page;
	}

	/**
	 * 更新群组成员状态
	 * @param s_gpm_id_list 群组成员id
	 * @param usr_ent_id 成员id
	 * @param s_gpm_status 成员状态
	 * @param type
	 */
	public void updateGpmStatus(List<Long> s_gpm_id_list, List<Long> usr_ent_id_list, long usr_ent_id, long s_gpm_status, String usr_id, String type, long s_grp_id) throws Exception {
		SnsGroupMember snsGroupMember = new SnsGroupMember();
		snsGroupMember.setS_gpm_status(s_gpm_status);
		if(type.equalsIgnoreCase("add")){
			snsGroupMember.setS_gpm_join_datetime(getDate());
			snsGroupMember.setS_gpm_apply_datetime(null);
			snsGroupMember.setS_gpm_check_datetime(null);
			snsGroupMember.setS_gpm_check_user(null);
		} else if(type.equalsIgnoreCase("apply")){
			snsGroupMember.setS_gpm_join_datetime(getDate());
			snsGroupMember.setS_gpm_apply_datetime(getDate());
			snsGroupMember.setS_gpm_check_datetime(null);
			snsGroupMember.setS_gpm_check_user(null);
		} else if(type.equalsIgnoreCase("upd")){
			snsGroupMember.setS_gpm_check_datetime(getDate());
			snsGroupMember.setS_gpm_check_user(usr_ent_id);
		}
		snsGroupMember.setS_gpm_id_list(s_gpm_id_list);
		snsGroupMemberMapper.update(snsGroupMember);

		if(s_gpm_status == 1){
			for(long join_ent_id : usr_ent_id_list){
				forCallOldAPIService.updUserCredits(null, Credit.SYS_JION_GROUP, join_ent_id, usr_id, 0, 0);
			}
		}

		/*if(s_gpm_status == 1 && type.equalsIgnoreCase("upd")){
			for(int i = 0; i < s_gpm_id_list.size(); i++){
				snsDoingService.add(s_grp_id, 0, usr_ent_id_list.get(i), 0, SNS.DOING_ACTION_GROUP_APP, s_gpm_id_list.get(i), SNS.MODULE_GROUP, "", 0);
			}
		}*/
	}

	/**
	 * 获取没有加入该群组的学员
	 * @param page
	 * @param s_gpm_grp_id 群组id
	 * @param usr_ent_id 成员id
	 * @param tcr_id 培训中心id
	 * @param searchContent 搜索内容
	 */
	public Page<RegUser> getNotJoinGroupMemberList(Page<RegUser> page, long usr_ent_id, long tcr_id, long s_gpm_grp_id, long s_gpm_status, String searchContent){
		page.getParams().put("usr_ent_id", usr_ent_id);
		page.getParams().put("tcr_id", tcr_id);
		page.getParams().put("s_gpm_grp_id", s_gpm_grp_id);
		page.getParams().put("s_gpm_status", s_gpm_status);
		page.getParams().put("searchContent", searchContent);
		snsGroupMemberMapper.findNotJoinGroupMemberList(page);
		for(RegUser regUser : page.getResults()){
			ImageUtil.combineImagePath(regUser);
		}
		return page;
	}

	/**
	 * 获取没有加入该群组的学员
	 * @param page
	 * @param s_gpm_grp_id 群组id
	 * @param usr_ent_id 成员id
	 * @param tcr_id 培训中心id
	 * @param searchContent 搜索内容
	 */
	public Page<RegUser> getNotJoinGroupMemberList(Page<RegUser> page, long usr_ent_id, long tcr_id, long s_gpm_grp_id, long s_gpm_status, String searchContent, boolean instrOnly){
		page.getParams().put("usr_ent_id", usr_ent_id);
		page.getParams().put("tcr_id", tcr_id);
		page.getParams().put("s_gpm_grp_id", s_gpm_grp_id);
		page.getParams().put("s_gpm_status", s_gpm_status);
		page.getParams().put("searchContent", searchContent);
		page.getParams().put("instrOnly", instrOnly);
		snsGroupMemberMapper.findNotJoinGroupMemberList(page);
		for(RegUser regUser : page.getResults()){
			ImageUtil.combineImagePath(regUser);
		}
		return page;
	}

	/**
	 * 删除群组成员
	 * @param s_grp_id 群组id
	 * @param usr_ent_id 成员id
	 */
	public void deleteGroupMember(long usr_ent_id, long s_grp_id){
		SnsGroupMember snsGroupMember = new SnsGroupMember();
		snsGroupMember.setS_gpm_grp_id(s_grp_id);
		snsGroupMember.setS_gpm_usr_id(usr_ent_id);
		snsGroupMemberMapper.delete(snsGroupMember);
	}

	/**
	 * 更新群组成员类型
	 * @param s_grp_id 群组id
	 * @param usr_ent_id 成员id
	 * @param s_gpm_type 成员类型
	 */
	public void changeManager(long usr_ent_id, long s_grp_id, long s_gpm_type){
		SnsGroupMember snsGroupMember = new SnsGroupMember();
		snsGroupMember.setS_gpm_grp_id(s_grp_id);
		snsGroupMember.setS_gpm_usr_id(usr_ent_id);
		snsGroupMember.setS_gpm_type(s_gpm_type);
		snsGroupMemberMapper.updateManager(snsGroupMember);
	}

	/**
	 * 检测该用户是否是该群组的成员
	 * @param s_grp_id 群组id
	 * @param usr_ent_id 成员id
	 * @param s_gpm_status 成员状态
	 */
	public boolean checkGroupMember(long usr_ent_id, long s_grp_id, long s_gpm_status){
		SnsGroupMember snsGroupMember = new SnsGroupMember();
		snsGroupMember.setS_gpm_grp_id(s_grp_id);
		snsGroupMember.setS_gpm_usr_id(usr_ent_id);
		snsGroupMember.setS_gpm_status(s_gpm_status);
		return snsGroupMemberMapper.isThisGroupMember(snsGroupMember) != null;
	}

	 /**
	  * 判断该用户是否已经加入该组了
	  * @param userEntId  用户ID
	  * @param groupId 组ID
	  * @return
	  */
	public SnsGroupMember getByGroupIdAndUserId(long userEntId,long groupId) {
		Map map = new HashMap<String,String>();
		map.put("usr_id", userEntId);
		map.put("grp_id", groupId);
		return snsGroupMemberMapper.getByGroupIdAndUserId(map);
	}

	public void clear(String userStrs, long grpId, long userEntId) {
		if(StringUtils.isEmpty(userStrs)) return;
		String[] userIds = userStrs.split(",");
		if(userIds != null && userIds.length > 0) {
			for(String id : userIds) {
				long uid = Integer.parseInt(id);
				if(this.checkGroupMember(uid, grpId, 1) && uid != userEntId){
					this.deleteGroupMember(uid, grpId);
				}
			}
		}
	}

	/**
	 * 批量添加用户到群组
	 * @param userIds
	 * @param grpId
	 * @param prof
	 * @throws Exception
	 */
	public void addSnsGroupMember(Long[] userIds, Long grpId, loginProfile prof) throws Exception {
		if(userIds != null && userIds.length > 0) {
			for(Long id : userIds) {
				SnsGroupMember sgm = this.getByGroupIdAndUserId(id,grpId);
				if(sgm != null) {
					List<Long> s_gpm_id_list = new ArrayList<Long>();
					s_gpm_id_list.add(grpId);
					if(sgm.getS_gpm_status() == 3) {
						List<Long> usr_ent_id_list = new ArrayList<Long>();
						usr_ent_id_list.add(id);
						this.updateGpmStatus(s_gpm_id_list, usr_ent_id_list, prof.usr_ent_id, 1, prof.usr_id, "add", grpId);
					} else if(sgm.getS_gpm_status() == 0){
						List<Long> usr_ent_id_list = new ArrayList<Long>();
						usr_ent_id_list.add(id);
						this.updateGpmStatus(s_gpm_id_list, usr_ent_id_list, prof.usr_ent_id, 1, prof.usr_id, "upd", grpId);
					}
				} else {
					this.addSnsGroupMember(grpId, id, 1, 2);
					forCallOldAPIService.updUserCredits(null, Credit.SYS_JION_GROUP, id, prof.usr_id, 0, 0);
				}
			}
		}

	}

	public void deleteAllGroupMember(long s_grp_id) {
		snsGroupMemberMapper.deleteAll(s_grp_id);
	}

}