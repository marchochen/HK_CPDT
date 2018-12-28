package com.cwn.wizbank.services;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.common.SNS;
import com.cwn.wizbank.entity.AcRole;
import com.cwn.wizbank.entity.KnowCatalog;
import com.cwn.wizbank.entity.SnsDoing;
import com.cwn.wizbank.entity.SnsGroup;
import com.cwn.wizbank.entity.SnsGroupMember;
import com.cwn.wizbank.exception.MessageException;
import com.cwn.wizbank.persistence.SnsGroupMapper;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.security.service.AclService;
import com.cwn.wizbank.utils.ImageUtil;
import com.cwn.wizbank.utils.Page;
/**
 *  service 实现
 */
@Service
public class SnsGroupService extends BaseService<SnsGroup> {


	@Autowired
	SnsGroupMapper snsGroupMapper;

	@Autowired
	SnsGroupMemberService snsGroupMemberService;

	@Autowired
	AcRoleFunctionService acRoleFunctionService;

	@Autowired
	SnsDoingService snsDoingService;

	@Autowired
	AclService aclService;

	@Autowired
	AcRoleService acRoleService;


	public static final String SNS_GROUP_MANAGE = "SNS_GROUP_MANAGE";

	public void setSnsGroupMapper(SnsGroupMapper snsGroupMapper){
		this.snsGroupMapper = snsGroupMapper;
	}

	/**
	 * 个人群组列表
	 * @param page
	 * @param usr_ent_id 用户id
	 * @param isMeInd 是否是我的群组
	 * @return
	 */
	public Page<SnsGroup> getPersonalGroupList(Page<SnsGroup> page, long usr_ent_id, boolean isMeInd, long tcr_id){
		page.getParams().put("usr_ent_id", usr_ent_id);
		page.getParams().put("tcr_id", tcr_id);
		page.getParams().put("isMeInd", isMeInd);
		snsGroupMapper.selectSnsGroupList(page);
		for(SnsGroup snsGroup : page.getResults()){
			ImageUtil.combineImagePath(snsGroup);
			Page<SnsGroupMember> mPage=new Page<SnsGroupMember>();
			mPage.setPageSize(5);
			snsGroup.setGoupMemberList(snsGroupMemberService.getSnsGroupMemberList(mPage, snsGroup.getS_grp_id(), 0, "myMember", "").getResults());
		}

		return page;
	}

	/**
	 * 获取群组列表
	 * @param page
	 * @param usr_ent_id 用户id
	 * @param tcr_id 培训中心id
	 * @param isMeInd 是否是我的群组
	 * @param type 类型
	 * @param searchContent 搜索内容
	 * @param s_grp_title 群组名称
	 * @return
	 */
	public Page<SnsGroup> getSnsGroupList(Page<SnsGroup> page, long usr_ent_id, long tcr_id, boolean isMeInd, String type, String searchContent, String s_grp_title, loginProfile prof){
		page.getParams().put("usr_ent_id", usr_ent_id);
		page.getParams().put("tcr_id", tcr_id);
		page.getParams().put("isMeInd", isMeInd);
		page.getParams().put("type", type);
		page.getParams().put("searchContent", searchContent);
		if(prof != null){
			page.getParams().put("isInstructor", hasInstrPrivilege(prof.current_role, usr_ent_id));
			page.getParams().put("hasSnsGroupManage", aclService.hasAnyPermission(prof.current_role, AclFunction.FTN_AMD_SNS_GROUP_MAIN));
		}
		
		if(s_grp_title != null && !s_grp_title.equalsIgnoreCase("")){
			page.getParams().put("s_grp_title", s_grp_title);
		}
		snsGroupMapper.selectSnsGroupList(page);
		for(SnsGroup snsGroup : page.getResults()){
			ImageUtil.combineImagePath(snsGroup);
			Page<SnsGroupMember> mPage=new Page<SnsGroupMember>();
			mPage.setPageSize(5);
			snsGroup.setGoupMemberList(snsGroupMemberService.getSnsGroupMemberList(mPage, snsGroup.getS_grp_id(), 0, "myMember", "").getResults());
		}
		return page;
	}

	/**
	 * 获取群组列表
	 * @param page
	 * @param usr_ent_id 用户id
	 * @param tcr_id 培训中心id
	 * @param isMeInd 是否是我的群组
	 * @param type 类型
	 * @param searchContent 搜索内容
	 * @param s_grp_title 群组名称
	 * @return
	 */
	public Page<SnsGroup> getSnsGroupList(Page<SnsGroup> page, long usr_ent_id, long tcr_id, boolean isMeInd, String type, String searchContent, String s_grp_title){
		return this.getSnsGroupList(page, usr_ent_id, tcr_id, isMeInd, type, searchContent, s_grp_title, null);
	}

	/**
	 * 获取所有群组列表
	 * @param page
	 * @param tcr_id 培训中心id
	 * @param searchContent 搜索内容
	 * @param s_grp_title 群组名称
	 * @return
	 */
	public Page<SnsGroup> getAllSnsGroupList(Page<SnsGroup> page, long tcr_id, String searchContent, long userEntId, String rolExtId){
		page.getParams().put("tcr_id", tcr_id);
		page.getParams().put("searchContent", searchContent);
		page.getParams().put("isInstructor", hasInstrPrivilege(rolExtId, userEntId));
		snsGroupMapper.selectAllSnsGroupList(page);
		return page;
	}

	/**
	 * 添加群组
	 * @param snsGroup
	 * @param usr_ent_id 用户id
	 * @param tcr_id 培训中心id
	 * @param image 群组名片
	 * @return
	 */
	public void addSnsGroup(WizbiniLoader wizbini, SnsGroup snsGroup, long usr_ent_id, long tcr_id, MultipartFile image) throws Exception{
		snsGroup.setS_grp_uid(usr_ent_id);
		snsGroup.setS_grp_create_uid(usr_ent_id);
		snsGroup.setS_grp_create_datetime(getDate());
		snsGroup.setS_grp_update_uid(usr_ent_id);
		snsGroup.setS_grp_update_datetime(getDate());
		snsGroup.setS_grp_tcr_id(tcr_id);
		snsGroup.setS_grp_status("OK");

		if(null != snsGroup && null != snsGroup.getS_grp_desc()){
			snsGroup.setS_grp_desc(cwUtils.esc4Json(snsGroup.getS_grp_desc()));
		}
		if(null != snsGroup && null != snsGroup.getS_grp_title()){
			snsGroup.setS_grp_title(cwUtils.esc4Json(snsGroup.getS_grp_title()));
		}
		
		String new_filename = System.currentTimeMillis() + "";
		if (image != null) {
			String filename = image.getOriginalFilename();
			String type = filename.substring(filename.lastIndexOf(".") + 1, filename.length());
			new_filename +=  "." + type;
			snsGroup.setS_grp_card(new_filename);
		}
		snsGroupMapper.insert(snsGroup);

		if (image != null) {
			//保存上传的图片
			String saveDirPath = wizbini.getFileUploadGroupDirAbs() + dbUtils.SLASH + snsGroup.getS_grp_id();
			dbUtils.delFiles(saveDirPath);
			File saveDir = new File(saveDirPath);
			if (!saveDir.exists()) {
				saveDir.mkdirs();
			}

			File targetFile = new File(saveDirPath, new_filename);
			image.transferTo(targetFile);
		}
		snsGroupMemberService.addSnsGroupMember(snsGroup.getS_grp_id(), usr_ent_id, 1, 1);

		//发动态
		/*if(snsGroup.getS_grp_private() == 0 || snsGroup.getS_grp_private() == 2){
			snsDoingService.add(snsGroup.getS_grp_id(), 0, usr_ent_id, 0, SNS.DOING_ACTION_GROUP_CREATE, 0, SNS.MODULE_GROUP, "", 0);
		}*/
	}

	/**
	 * 解散群组
	 * @param usr_ent_id 用户id
	 * @param s_grp_id 群组id
	 * @return
	 * @throws MessageException 
	 */
	public void dissolveSnsGroup(long usr_ent_id, long s_grp_id, String cur_lan) throws MessageException{
		SnsGroup snsGroup = new SnsGroup();
		snsGroup.setS_grp_id(s_grp_id);
		snsGroup.setS_grp_status("DELETED");
		snsGroup.setS_grp_update_uid(usr_ent_id);
		snsGroup.setS_grp_update_datetime(getDate());
		snsGroupMapper.update(snsGroup);
		
		//获取删除群组下的评论
		List<SnsDoing> snsList = snsDoingService.getAllGroupDoingList(s_grp_id, usr_ent_id);
		if(null != snsList && snsList.size() > 0){
			for(SnsDoing snsDoing : snsList){
				snsDoingService.delete(snsDoing.getS_doi_id(), usr_ent_id, SNS.MODULE_GROUP, cur_lan);
			}
		}
		
		//发动态
		//snsDoingService.add(snsGroup.getS_grp_id(), 0, usr_ent_id, 0, SNS.DOING_ACTION_GROUP_DISSMISS, 0, SNS.MODULE_GROUP, "", 0);
	}

	/**
	 * 获取群组详细信息
	 * @param s_grp_id 群组id
	 * @param usr_ent_id 用户id
	 * @param s_gpm_usr_id 群组成员id
	 * @return
	 */
	public SnsGroup getSnsGroupDetail(long s_grp_id, long usr_ent_id, long s_gpm_usr_id){
		SnsGroup snsGroup = new SnsGroup();
		snsGroup.setS_grp_id(s_grp_id);
		if(usr_ent_id > 0){
			snsGroup.setS_grp_uid(usr_ent_id);
		}

		SnsGroupMember snsGroupMember = new SnsGroupMember();
		snsGroupMember.setS_gpm_usr_id(s_gpm_usr_id);
		snsGroup.setS_gpm(snsGroupMember);
		return snsGroupMapper.selectSnsGroupDetail(snsGroup);
	}

	/**
	 * 更新群组信息
	 * @param snsGroup
	 * @param usr_ent_id 用户id
	 * @param image 群组名片
	 * @return
	 */
	public void updateSnsGroup(WizbiniLoader wizbini,SnsGroup group, SnsGroup snsGroup, long usr_ent_id, MultipartFile image) throws Exception{
		snsGroup.setS_grp_update_uid(usr_ent_id);
		snsGroup.setS_grp_update_datetime(getDate());
		
		if(null != snsGroup && null != snsGroup.getS_grp_desc()){
			snsGroup.setS_grp_desc(cwUtils.esc4Json(snsGroup.getS_grp_desc()));
		}
		if(null != snsGroup && null != snsGroup.getS_grp_title()){
			snsGroup.setS_grp_title(cwUtils.esc4Json(snsGroup.getS_grp_title()));
		}
		
		if(image != null){
			String saveDirPath = wizbini.getFileUploadGroupDirAbs() + dbUtils.SLASH + snsGroup.getS_grp_id();
			dbUtils.delFiles(saveDirPath);
			File saveDir = new File(saveDirPath);
			if (!saveDir.exists()) {
				saveDir.mkdirs();
			}

			String new_filename = System.currentTimeMillis() + "";
			String filename = image.getOriginalFilename();
			String type = filename.substring(filename.lastIndexOf(".") + 1, filename.length());
			new_filename +=  "." + type;
			snsGroup.setS_grp_card(new_filename);

			File targetFile = new File(saveDirPath, new_filename);
			image.transferTo(targetFile);
			snsGroup.setS_grp_card(new_filename);
		}
		//修改群组的隐私设置为公开时，发布动态
		/*if(group != null && group.getS_grp_private() != snsGroup.getS_grp_private() && snsGroup.getS_grp_private() == 0){
			snsDoingService.add(snsGroup.getS_grp_id(),0, usr_ent_id,0, SNS.DOING_ACTION_GROUP_OPEN, 0, SNS.MODULE_GROUP, "", 0);
		}*/

		snsGroupMapper.update(snsGroup);
	}

	/**
	 * 更新群组管理员
	 * @param s_grp_uid 新管理员id
	 * @param s_grp_update_uid 更新者id
	 * @param s_grp_id 群组id
	 * @return
	 */
	public void updateGrpUid(long s_grp_uid, long s_grp_update_uid, long s_grp_id){
		SnsGroup snsGroup = new SnsGroup();
		snsGroup.setS_grp_id(s_grp_id);
		snsGroup.setS_grp_uid(s_grp_uid);
		snsGroup.setS_grp_update_uid(s_grp_update_uid);
		snsGroup.setS_grp_update_datetime(getDate());
		snsGroupMapper.update(snsGroup);
	}

	public boolean checkManger(long s_grp_uid, loginProfile prof) {
		boolean isManage = false;
		if (!checkNormal(prof) ){
			isManage = true;
		}else{
			if(s_grp_uid==prof.getUsr_ent_id())
				isManage = true;
		}
		return isManage;
	}

	public boolean checkNormal(loginProfile prof){
		boolean isManage = true;
		if (aclService.hasAnyPermission(prof.current_role, AclFunction.FTN_AMD_SNS_GROUP_MAIN) ){
			isManage = false;
		}
		return isManage;
	}

	/**
	 *  判断当前用户有没有群主管理权限，或者是不是群主
	 * @param prof
	 * @param grpUid
	 * @return
	 */
	public boolean hasAdminPrivilege(loginProfile prof, long grpUid) {
		return aclService.hasAnyPermission(prof.usr_ent_id, AclFunction.FTN_AMD_SNS_GROUP_MAIN)
				&& aclService.hasAnyPermission(prof.current_role, AclFunction.FTN_AMD_SNS_GROUP_MAIN)
				|| prof.usr_ent_id == grpUid;
	}

	/**
	 * 是否有用讲师的权限
	 * @param rolExtId
	 * @param userEntId
	 * @return
	 */
	public boolean hasInstrPrivilege(String rolExtId, long userEntId) {
		return aclService.hasAnyPermission(rolExtId, AclFunction.FTN_AMD_SNS_GROUP_MAIN)
				|| rolExtId.equals(AcRole.ROLE_INSTR_1);
	}
	
	public boolean hasInstrRole(String rolExtId) {
		return rolExtId.equals(AcRole.ROLE_ADM_1) || rolExtId.equals(AcRole.ROLE_TADM_1)
				|| rolExtId.equals(AcRole.ROLE_INSTR_1);
	}

	public SnsGroup getByName(String s_grp_title) {
		SnsGroup group = null;
		Page<SnsGroup> page = new Page<SnsGroup>();
		page.getParams().put("s_grp_title", s_grp_title);
		List<SnsGroup> list = snsGroupMapper.getByName(page);
		if(list.size() > 0){
			group = list.get(0);
		}
		return group;
	}

	public void deleteAll(long s_grp_id) {
		this.delete(s_grp_id);
		snsGroupMemberService.deleteAllGroupMember(s_grp_id);
	}
	
	public int checkGroupName(SnsGroup snsgroup){
		return this.snsGroupMapper.checkGroupName(snsgroup);
	}

}