package com.cwn.wizbank.services;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.entity.UserCredits;
import com.cwn.wizbank.entity.UserCreditsDetailLog;
import com.cwn.wizbank.persistence.UserCreditsMapper;
import com.cwn.wizbank.utils.CwnUtil;
import com.cwn.wizbank.utils.ImageUtil;
import com.cwn.wizbank.utils.Page;
/**
 *  service 实现
 */
@Service
public class UserCreditsService extends BaseService<UserCredits> {

	@Autowired
	UserCreditsMapper userCreditsMapper;

	public void setUserCreditsMapper(UserCreditsMapper userCreditsMapper){
		this.userCreditsMapper = userCreditsMapper;
	}
	
	/**
	 * 获取积分排行榜信息
	 * 
	 * @return
	 */
	public Page<UserCredits> getUserCreditsRankList(Page<UserCredits> page, long tcrId){
		page.getParams().put("tcrId", tcrId);
		List<UserCredits> list = userCreditsMapper.selectRankList(page);
		for(UserCredits userCredits : list){
			ImageUtil.combineImagePath(userCredits.getUser());
			//只保留两位小数
			userCredits.setUct_total(Double.parseDouble(String.format("%.2f", userCredits.getUct_total())));
		}
		page.setResults(list);
		return page;
	}
	
	/**
	 * 获取个人的总积分及其排名
	 * @param usrEntId 用户id
	 * @return
	 */
	public UserCredits getUserCreditAndRank(long usrEntId, long tcrId){
		UserCredits userCredits = new UserCredits();
		userCredits.setUct_ent_id(usrEntId);
		userCredits.setTcr_id(tcrId);
		userCredits = userCreditsMapper.selectUserCreditAndRank(userCredits);
		if(userCredits != null){
			ImageUtil.combineImagePath(userCredits.getUser());
			userCredits.setUct_total(userCredits.getUct_total() > 0 ?  CwnUtil.formatNumber(userCredits.getUct_total(), 2) : 0);
		}else{
			userCredits = new UserCredits();
			userCredits.setUct_total(getUserTotalCredits(usrEntId, "all"));
		}		
		return userCredits;
	}
	
	/**
	 * 获取个人的积分详细信息列表
	 * @param usr_ent_id 用户id
	 * @return
	 */
	public Page<UserCreditsDetailLog> getUserCreditsDetailList(Page<UserCreditsDetailLog> page, long usr_ent_id){
		page.getParams().put("usr_ent_id", usr_ent_id);
		userCreditsMapper.selectUserCreditDetailList(page);
		return page;
	}
	
	/**
	 * 获取积分数
	 * @param usr_ent_id 用户id
	 * @param type   all : 全部 ; activity : 活动积分; train : 培训积分
	 * @return
	 */
	public double getUserTotalCredits(long usr_ent_id, String type){
		UserCreditsDetailLog userCreditsDetailLog = new UserCreditsDetailLog();
		userCreditsDetailLog.setUcl_usr_ent_id(usr_ent_id);
		userCreditsDetailLog.setUcl_relation_type(type);
		userCreditsDetailLog = userCreditsMapper.selectUserTotalCredits(userCreditsDetailLog);
		return userCreditsDetailLog != null && userCreditsDetailLog.getTotal_credits() > 0 ?  CwnUtil.formatNumber(userCreditsDetailLog.getTotal_credits(), 2) : 0;
	}
}