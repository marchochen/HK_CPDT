package com.cwn.wizbank.services;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.common.SNS;
import com.cwn.wizbank.entity.AeApplication;
import com.cwn.wizbank.entity.AeItem;
import com.cwn.wizbank.entity.CourseEvaluation;
import com.cwn.wizbank.persistence.AeApplicationMapper;
import com.cwn.wizbank.persistence.AeItemMapper;
import com.cwn.wizbank.utils.CwnUtil;
import com.cwn.wizbank.utils.ImageUtil;
import com.cwn.wizbank.utils.Page;
/**
 *  service 实现
 */
@Service
public class AeApplicationService extends BaseService<AeApplication>{

	@Autowired
	AeApplicationMapper aeApplicationMapper;
	
	@Autowired
	AeItemMapper aeItemMapper;
	
	@Autowired
	AeItemService aeItemService;
	
	@Autowired
	SnsCountService snsCountService;

	@Autowired
	SnsCommentService snsCommentService;
	
	/**
	 * 已报名课程
	 * @param userEntId
	 * @param selectType 推荐类型
	 * @param isCompulsory 是否必修 1是，0 否
	 * @param itemType   课程类型： 面授课程，网上课程，项目式课程
	 * @param appStatus 状态 ： 学习中 inprocessed，审批中 pending，已结束	completed
	 * @param page
	 * @return
	 */
	public Page<AeApplication> signup(long userEntId, String curLang, Page<AeApplication> page){
		page.getParams().put("userEntId", userEntId);
		List<AeApplication> list = aeApplicationMapper.getSignup(page);
		for(AeApplication app : list) {
			AeItem itm = app.getItem();
			ImageUtil.combineImagePath(itm);
			AeItem parent = null;
			if(itm != null) {
				parent = aeItemMapper.getParent(itm.getItm_id());
			}
			if(parent != null && parent.getItm_id() != null) {
				ImageUtil.combineImagePath(parent);
				itm.setParent(parent);
				//当是班级的时候就显示课程的图片
				itm.setItm_icon(parent.getItm_icon());
			}
			if(itm != null){
				if(itm.getItm_exam_ind() != null && itm.getItm_exam_ind() == 1){
					itm.setItm_type(CwnUtil.getExamTypeStr(itm.getItm_type(), curLang));
				} else {
					itm.setItm_type(CwnUtil.getCourseTypeStr(itm.getItm_type(), curLang));
				}
				itm.setCnt_app_count(getAeApplicationMapper().getCount(itm.getItm_id()));
				itm.setSnsCount(snsCountService.getByTargetInfo(itm.getItm_id(), SNS.MODULE_COURSE));
				itm.setCnt_comment_count(snsCommentService.getCommentCount(itm.getItm_id(), SNS.MODULE_COURSE));
			}
			app.setApp_status(CwnUtil.getAppStatusStr(app.getApp_status(), curLang));
		}		
		page.setResults(list);		
		return page;
	}
	
	/**
	 * 取到用户在该课程下最后一次报名的记录
	 * @param userEntId 用户的ent_id
	 * @param app_itm_id 课程的ID
	 * @return
	 */
	public AeApplication getMaxAppByUser(long usr_ent_id, long app_itm_id){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("usr_ent_id", usr_ent_id);
		map.put("app_itm_id", app_itm_id);
		AeApplication app = null ;
		List<AeApplication> list  = aeApplicationMapper.getUsrApps(map);
		if(list != null && list.size() > 0){
			app = list.get(0);
			
		}
	
		return app;
	}
	
	/**
	 * 获取审批列表
	 * @param usr_ent_id 用户id
	 * @param type 审批类型	PENDING : 等待	HISTORY : 已审批
	 * @return
	 */
	public Page<AeApplication> getSubordinateApprovalDetail(Page<AeApplication> page, long usr_ent_id, String type){
		page.getParams().put("usr_ent_id", usr_ent_id);
		page.getParams().put("type", type);
		aeApplicationMapper.getSubordinateApprovalDetail(page);
		return page;
	}
	
	public void setAeApplicationMapper(AeApplicationMapper aeApplicationMapper){
		this.aeApplicationMapper = aeApplicationMapper;
	}

	public AeApplicationMapper getAeApplicationMapper() {
		return aeApplicationMapper;
	}
	
	public Long getItemId(long tkh_id){
		return aeApplicationMapper.selectItemIdByTkhId(tkh_id);
	}

	public int getSignupCount(long userEntId, int beforeDay, int isExam, long tcrId) {
		
		Calendar cld = Calendar.getInstance();
		cld.setTime(getDate());
		cld.set(Calendar.DAY_OF_MONTH, beforeDay);
		
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("userEntId", userEntId);
		map.put("beforeTime", cld.getTime());
		map.put("isExam", isExam);
		map.put("tcrId", tcrId);
		return aeApplicationMapper.getSignupCount(map);
	}

	/**
	 * 获取我的课程
	 * @param userEntId
	 * @param curLang
	 * @param page
	 * @return
	 */
	public Page<AeApplication> getMyCourse(long userEntId, String curLang, Page<AeApplication> page){
		page.getParams().put("userEntId", userEntId);
		List<AeApplication> list = aeApplicationMapper.getMyCourse(page);
		for(AeApplication app : list) {
			AeItem itm = app.getItem();
			ImageUtil.combineImagePath(itm);
			AeItem parent = null;
			if(itm != null) {
				parent = aeItemMapper.getParent(itm.getItm_id());
			}
			if(parent != null && parent.getItm_id() != null) {
				ImageUtil.combineImagePath(parent);
				itm.setParent(parent);
				//当是班级的时候就显示课程的图片
				itm.setItm_icon(parent.getItm_icon());
			}
			if(itm != null){
				itm.setCnt_app_count(getAeApplicationMapper().getCount(itm.getItm_id()));
				itm.setSnsCount(snsCountService.getByTargetInfo(itm.getItm_id(), SNS.MODULE_COURSE));
				itm.setCnt_comment_count(snsCommentService.getCommentCount(itm.getItm_id(), SNS.MODULE_COURSE));
			}
			if(itm.getItm_content_eff_end_time() != null) {
				itm.setItm_online_content_period(1);
			} else if(itm.getItm_content_eff_duration() != null && itm.getItm_content_eff_duration() != 0) {
				itm.setItm_online_content_period(2);
				if("I".equals(app.getApp_status()) || "C".equals(app.getApp_status()) || "F".equals(app.getApp_status()) || "W".equals(app.getApp_status())) {
					itm.setItm_is_enrol(1);
					try {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						Calendar date = Calendar.getInstance();
						date.setTime(app.getApp_upd_timestamp());
						date.set(Calendar.DATE, date.get(Calendar.DATE) + Integer.parseInt(itm.getItm_content_eff_duration().toString()));
						itm.setItm_content_eff_end_time(sdf.parse(sdf.format(date.getTime())));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			//app.setApp_status(CwnUtil.getAppStatusStr(app.getApp_status(), curLang));
		}		
		page.setResults(list);		
		return page;
	}

	/**
	 * 获取待审批的报名数量
	 * @param usr_ent_id
	 * @return
	 */
	public long selectPedingAppCount(long usr_ent_id, String acRole) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userEntId", usr_ent_id);
		map.put("acRole", acRole);
		return aeApplicationMapper.selectPedingAppCount(map);
	}
	
	public List<AeApplication> getCItem(Long itmId){
		Map map = new HashMap<String,Object>();
		map.put("status", CourseEvaluation.Completed);
		map.put("itm_id", itmId);
		return aeApplicationMapper.getCItem(map);
	}
	
}