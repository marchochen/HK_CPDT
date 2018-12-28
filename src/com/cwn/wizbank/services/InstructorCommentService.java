package com.cwn.wizbank.services;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.cwn.wizbank.entity.AeApplication;
import com.cwn.wizbank.entity.AeItemAccess;
import com.cwn.wizbank.entity.InstructorComment;
import com.cwn.wizbank.exception.AuthorityException;
import com.cwn.wizbank.exception.MessageException;
import com.cwn.wizbank.persistence.AeApplicationMapper;
import com.cwn.wizbank.persistence.AeItemAccessMapper;
import com.cwn.wizbank.persistence.InstructorCommentMapper;
import com.cwn.wizbank.utils.LabelContent;
import com.cwn.wizbank.utils.Page;
import com.cwn.wizbank.utils.RequestStatus;
/**
 *  service 实现
 */
@Service
public class InstructorCommentService extends BaseService<InstructorComment> {

	@Autowired
	InstructorCommentMapper instructorCommentMapper;
	
	@Autowired
	AeApplicationMapper aeApplicationMapper;
	
	@Autowired
	AeItemAccessMapper aeItemAccessMapper;
	
	/**
	 * 获取课程的讲师评分及评论
	 * @param itmId	课程id
	 * @param usrEntId user id
	 * @return
	 */
	public List<InstructorComment> getInstructorCommentScore(long itmId, long usrEntId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("itmId", itmId);
		map.put("usrEntId", usrEntId);
		return instructorCommentMapper.getInstructorCommentScore(map);
	}
	
	/**
	 * 获取课程的讲师评分及评论
	 * @param itmId	课程id
	 * @param usrEntId user id
	 * @return
	 */
	public InstructorComment getInstructorComemnt(long itmId, long usrEntId) {
		List<InstructorComment> list = getInstructorCommentScore(itmId, usrEntId);
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}


	/**
	 * 讲师评分
	 * @param itmId
	 * @param tkhId
	 * @param note
	 * @param styleScore
	 * @param qualityScore
	 * @param structureScore
	 * @param interactionScore
	 * @return
	 * @throws AuthorityException 
	 */
	public InstructorComment comment(long itmId, long tkhId, String note, double styleScore, double qualityScore,
			double structureScore, double interactionScore, String curLang, long userEntId) throws MessageException {
		if(tkhId < 1){
			throw new MessageException(LabelContent.get(curLang, "error_msg_need_app"));
		}
		AeApplication app = aeApplicationMapper.getByTkhId(tkhId);
		if(app != null){
			if(!AeApplication.APP_STATUS_ADMITTED.equals(app.getApp_status())){
			//TODO没有权限操作
				throw new MessageException(LabelContent.get(curLang, "error_msg_need_app"));
			}
		} else {
			//TODO没有权限操作
			throw new MessageException(LabelContent.get(curLang, "error_msg_need_app"));
		}
		List<InstructorComment> ics = getInstructorCommentScore(itmId, userEntId);
		InstructorComment ic = null;
		if(ics != null && ics.size()>0){
			ic = ics.get(0);
		}
		boolean isUpdate = true;
		if(ic == null) {
			ic = new InstructorComment();
			ic.setItc_itm_id(itmId);
			ic.setItc_ent_id(userEntId);
			ic.setItc_create_datetime(getDate());
			ic.setItc_create_user_id(userEntId+"");
			isUpdate = false;
		} else {
			throw new MessageException(LabelContent.get(curLang, "error_repeat_score"));
		}
		ic.setItc_comment(note);
		ic.setItc_interaction_score(interactionScore);
		ic.setItc_structure_score(structureScore);
		ic.setItc_style_score(styleScore);
		ic.setItc_quality_score(qualityScore);
		
		Double avgScore = (interactionScore + structureScore + styleScore + qualityScore)/4;
		ic.setItc_score(avgScore);
		
		ic.setItc_update_datetime(getDate());
		ic.setItc_update_user_id(userEntId + "");
		if(isUpdate) {
			super.update(ic);
		} else {
			List<Long> instrucotrs = aeItemAccessMapper.getUniqueInstructorsByItmId(itmId);
			if(instrucotrs != null && !instrucotrs.isEmpty()){
				for(Long ins : instrucotrs) {
					ic.setItc_iti_ent_id(ins);
					super.add(ic);
				}
			}
		}
		return ic;
	}

	public List<InstructorComment> getInstructorComments(Model model,
			Page<InstructorComment> page, String instructorIds) {
		String ids[] = instructorIds.split(",");
		page.getParams().put("ids", ids);
		List<InstructorComment> ics = instructorCommentMapper.getInstructorComments(page);
		model.addAttribute(RequestStatus.STATUS, RequestStatus.SUCCESS);
		model.addAttribute("instructorComments", ics);
		return ics;
	}
	
}