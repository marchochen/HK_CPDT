package com.cwn.wizbank.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.entity.APIToken;
import com.cwn.wizbank.entity.AeItem;
import com.cwn.wizbank.entity.SystemStatistics;
import com.cwn.wizbank.persistence.SystemStatisticsMapper;

/**
 * 系统数据统计服务
 * @author andrew.xiao
 *
 */
@Service("systemStatisticsService")
public class SystemStatisticsService extends BaseService<SystemStatistics>{
	
	@Autowired
	EnterpriseInfoPortalService enterpriseInfoPortalService;
	
	@Autowired
	SystemStatisticsMapper systemStatisticsMapper;
	
	@Autowired
	AeItemService aeItemService;
	
	@Autowired
	UserSpecialTopicService userSpecialTopicService;
	
	@Autowired
	KbItemService kbItemService;
	
	@Autowired
	RegUserService regUserService;
	
	@Autowired
	UserGroupService userGroupService;
	
	@Autowired
	ForCallOldAPIService forCallOldAPIService;
	
	/**
	 * 培训管理员获取系统实时统计数据
	 * @param myTopTcrId top培训中心Id
	 * @param usrEntId 用户Id
	 * @return
	 */
	public SystemStatistics getInstanceStatisticForTA(long myTopTcrId,long usrEntId){
		SystemStatistics ss = new SystemStatistics();
		//网上课程数目
		ss.setSsc_web_base_couse_count(aeItemService.getAeItemTotalCountForTA(usrEntId,0,0,AeItem.SELFSTUDY));
		//面授课程数目
		ss.setSsc_classroom_course_count(aeItemService.getAeItemTotalCountForTA(usrEntId,0,0,AeItem.CLASSROOM));
		//项目式培训数目
		ss.setSsc_integrated_course_count(aeItemService.getAeItemTotalCountForTA(usrEntId,0,0,AeItem.INTEGRATED));
		//网上考试数目
		ss.setSsc_web_base_exam_count(aeItemService.getAeItemTotalCountForTA(usrEntId,0,1,AeItem.SELFSTUDY));
		//离线考试数目
		ss.setSsc_classroom_exam_count(aeItemService.getAeItemTotalCountForTA(usrEntId,0,1,AeItem.CLASSROOM));
		//公开课数目
		ss.setSsc_open_course_count(aeItemService.getAeItemTotalCountForTA(usrEntId,1,0,null));

		//培训专题数目
		ss.setSsc_special_topic_count(userSpecialTopicService.getTotalCountByTcrIdAndChild(myTopTcrId));
		
		//知识中心管理员分享数
		ss.setSsc_admin_know_share_count(kbItemService.getAdminShareCountByTcrIdAndChild(myTopTcrId));
		//知识中心学员分享量
		ss.setSsc_learner_know_share_count(kbItemService.getLearnerShareCountByTcrIdAndChild(myTopTcrId));
		
		//用户总数
		ss.setSsc_user_count(regUserService.getRegUserCountByTcrId(myTopTcrId,null));
		
		//移动APP登录数
		ss.setSsc_mobile_app_user_count(regUserService.getRegUserCountByTcrId(myTopTcrId, APIToken.API_DEVELOPER_MOBILE));
		
		//绑定微信用户数
		ss.setSsc_wechat_user_count(regUserService.getRegUserCountByTcrId(myTopTcrId, APIToken.API_DEVELOPER_WEIXIN));
		
		//用户组数目
		ss.setSsc_user_group_count(userGroupService.getUserGroupCountInTCent(myTopTcrId));
		
		//当前登录的用户数
		ss.setSsc_user_online_count(forCallOldAPIService.getCurActUserCntByEipTcrId(null, myTopTcrId));
		
		return ss;
	}
	
}











