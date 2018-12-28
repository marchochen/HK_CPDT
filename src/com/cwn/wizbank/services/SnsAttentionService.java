package com.cwn.wizbank.services;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.entity.RegUser;
import com.cwn.wizbank.entity.SnsAttention;
import com.cwn.wizbank.persistence.SnsAttentionMapper;
import com.cwn.wizbank.utils.ImageUtil;
import com.cwn.wizbank.utils.Page;
import com.opensymphony.oscache.util.StringUtil;
/**
 * service 实现
 */
@Service
public class SnsAttentionService extends BaseService<SnsAttention> {

	@Autowired
	SnsAttentionMapper snsAttentionMapper;
	
	@Autowired
	RegUserService regUserService;

	public void setSnsAttentionMapper(SnsAttentionMapper snsAttentionMapper) {
		this.snsAttentionMapper = snsAttentionMapper;
	}
	
	/**
	 * 用户列表
	 * @param page
	 * @param type {attention : 关注; fans : 粉丝; find : 找人}
	 * @param he_usr_ent_id 目标用户id
	 * @param tcrId 培训中心id
	 * @param my_usr_ent_id 我的用户id
	 * @param searchContent 搜索内容
	 * @return
	 */
	public Page<RegUser> getUserList(Page<RegUser> page, String type, long usr_ent_id, long tcrId, long my_usr_ent_id, String searchContent ,String groupId){
		
		if(!StringUtil.isEmpty(searchContent)){
			Pattern pattern = Pattern.compile("'");
			Matcher matcher = pattern.matcher(searchContent);
			searchContent = matcher.replaceAll("''");
		}
		
		page.getParams().put("searchContent", searchContent);
		page.getParams().put("my_usr_ent_id", my_usr_ent_id);
		if(!type.equalsIgnoreCase("find")){
			SnsAttention snsAttention = new SnsAttention();
			if(type.equalsIgnoreCase("fans")){
				snsAttention.setS_att_target_uid(usr_ent_id);
			} else if(type.equalsIgnoreCase("attention")){
				snsAttention.setS_att_source_uid(usr_ent_id);
			}
			page.getParams().put("snsAttention", snsAttention);
			snsAttentionMapper.selectUserList(page);
		} else {
			RegUser regUser = new RegUser();
			regUser.setUsr_ent_id(usr_ent_id);
			page.getParams().put("regUser", regUser);
			page.getParams().put("groupid", groupId);
			snsAttentionMapper.findUserList(page);
		}
		for(RegUser regUser : page.getResults()){
			ImageUtil.combineImagePath(regUser);
		}

		return page;
	}
	
	/**
	 * 新增关注用户
	 * @param s_att_source_uid 关注者用户id
	 * @param s_att_target_uid 被关注用户id 
	 * @return
	 */
	public void addAttention(long s_att_source_uid, long s_att_target_uid){
		SnsAttention snsAttention = new SnsAttention();
		snsAttention.setS_att_source_uid(s_att_source_uid);
		snsAttention.setS_att_target_uid(s_att_target_uid);
		snsAttention.setS_att_create_datetime(getDate());
		snsAttentionMapper.insert(snsAttention);
	}
	
	/**
	 * 取消用户关注 
	 * @param s_att_source_uid 关注者用户id
	 * @param s_att_target_uid 被关注用户id 
	 * @return
	 */
	public void cancelAttention(long s_att_source_uid, long s_att_target_uid){
		SnsAttention snsAttention = new SnsAttention();
		snsAttention.setS_att_source_uid(s_att_source_uid);
		snsAttention.setS_att_target_uid(s_att_target_uid);
		snsAttentionMapper.delete(snsAttention);
	}
	
	/**
	 * 获取关注数或粉丝数
	 * @param usr_ent_id 用户id
	 * @param type   attent : 关注数 ; fans : 粉丝数
	 * @return
	 */
	public long getSnsAttentiontotal(long usr_ent_id, String type){
		SnsAttention snsAttention = new SnsAttention();
		if(type.equalsIgnoreCase("fans")){
			snsAttention.setS_att_target_uid(usr_ent_id);
		} else {
			snsAttention.setS_att_source_uid(usr_ent_id);
		}
		return snsAttentionMapper.selectSnsAttentionTotal(snsAttention);
	}
	
}