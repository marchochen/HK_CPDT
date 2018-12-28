package com.cwn.wizbank.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.entity.RegUser;
import com.cwn.wizbank.entity.SuperviseTargetEntity;
import com.cwn.wizbank.persistence.SuperviseTargetEntityMapper;
import com.cwn.wizbank.utils.ImageUtil;
import com.cwn.wizbank.utils.Page;
/**
 *  service 实现
 */
@Service
public class SuperviseTargetEntityService extends BaseService<SuperviseTargetEntity> {

	@Autowired
	SuperviseTargetEntityMapper SuperviseTargetEntityMapper;

	public void setSuperviseTargetEntityMapper(SuperviseTargetEntityMapper SuperviseTargetEntityMapper){
		this.SuperviseTargetEntityMapper = SuperviseTargetEntityMapper;
	}
	
	/**
	 * 获取下属列表
	 * @param usr_ent_id 用户id
	 * @param type 类型 	all : 所以下属		direct : 直属下属		group : 下属部门	
	 * @param searchContent 搜索内容
	 * @return
	 */
	public Page<RegUser> getSubordinateList(Page<RegUser> page, long usr_ent_id, String type, String searchContent, long itmId){
		page.getParams().put("usr_ent_id", usr_ent_id);
		page.getParams().put("type", type);
		page.getParams().put("searchContent", searchContent);
		page.getParams().put("curTime", getDate());
		page.getParams().put("itmId", itmId);
		SuperviseTargetEntityMapper.selectSubordinateList(page);
		for(RegUser regUser : page.getResults()){
			ImageUtil.combineImagePath(regUser);
		}
		return page;
	}
	
	/**
	 * 获取我的直属上司
	 * @param usr_ent_id 用户id
	 * @return
	 */
	public List<RegUser> getMySupervise(long usr_ent_id){
		return SuperviseTargetEntityMapper.selectMySupervise(usr_ent_id);
	}
	
}