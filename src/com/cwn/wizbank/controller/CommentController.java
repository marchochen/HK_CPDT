/**
 * 
 */
package com.cwn.wizbank.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cw.wizbank.JsonMod.credit.Credit;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.entity.InstructorComment;
import com.cwn.wizbank.entity.SnsComment;
import com.cwn.wizbank.exception.AuthorityException;
import com.cwn.wizbank.exception.DataNotFoundException;
import com.cwn.wizbank.exception.MessageException;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.security.service.AclService;
import com.cwn.wizbank.services.AcRoleFunctionService;
import com.cwn.wizbank.services.ForCallOldAPIService;
import com.cwn.wizbank.services.InstructorCommentService;
import com.cwn.wizbank.services.SnsCommentService;
import com.cwn.wizbank.utils.EncryptUtil;
import com.cwn.wizbank.utils.JsonFormat;
import com.cwn.wizbank.utils.Page;
import com.cwn.wizbank.utils.Params;
import com.cwn.wizbank.utils.RequestStatus;

/**
 * 讲师评分，评论
 * @author leon.li
 * 2014-9-5 上午11:12:31
 */
@Controller
@RequestMapping("comment")
public class CommentController {

	@Autowired
	InstructorCommentService InstructorCommentService;
	
	@Autowired
	SnsCommentService snsCommentService;
	
	@Autowired
	AcRoleFunctionService acRoleFunctionService;
	
	@Autowired
	ForCallOldAPIService forCallOldAPIService;
	
	@Autowired
	AclService aclService;
	
	/**
	 * 讲师评分
	 * @throws AuthorityException 
	 * @throws MessageException 
	 */
	@RequestMapping("instructor/{itmId}/{tkhId}")
	@ResponseBody
	public String commnetInstructor(
			loginProfile prof, WizbiniLoader wizbini, Model model,
			Params param,
			@PathVariable("itmId") String itmId,
			@PathVariable("tkhId") long tkhId,
			//@RequestParam("note") String note,
			@RequestParam(value = "note", required = false) String note,
			@RequestParam("styleScore") double styleScore,
			@RequestParam("qualityScore") double qualityScore,
			@RequestParam("structureScore") double structureScore,
			@RequestParam("interactionScore") double interactionScore,
			@RequestParam(value="isMobile", required=false, defaultValue = "0") int isMobile
			) throws MessageException{
		
		try{
			InstructorComment ic = InstructorCommentService.comment(EncryptUtil.cwnDecrypt(itmId), tkhId, note, styleScore, qualityScore, structureScore, interactionScore, prof.cur_lan, prof.usr_ent_id);
			model.addAttribute(RequestStatus.STATUS, RequestStatus.SUCCESS);
			model.addAttribute("instructorComment", ic);
		}catch(MessageException e){
			if(isMobile == 1){//移动端请求，不直接抛异常，用【请求状态】告知客户端错误消息
				model.addAttribute(RequestStatus.STATUS, RequestStatus.ERROR);
				model.addAttribute("msg", e.getMessage());
			}else{
				throw e; 
			}
		}
		
		return JsonFormat.format(param, model);
	}
	
	/**
	 * 讲师评分
	 * @throws AuthorityException 
	 * @throws MessageException 
	 */
	@RequestMapping("instructor/{id}")
	@ResponseBody
	public String commnetInstructor(
			loginProfile prof, Model model,
			@PathVariable("id") String instructorIds,
			Page<InstructorComment> page
			) throws MessageException{
		InstructorCommentService.getInstructorComments(model, page, instructorIds);
		return JsonFormat.format(model, page);
	}
	
	@RequestMapping("instructorComment/{itmId}")
	@ResponseBody
	public String commnetInstructor(
			loginProfile prof, Model model,
			Params param,
			@PathVariable("itmId") String itmId
			) throws MessageException{
		InstructorComment ic = InstructorCommentService.getInstructorComemnt(EncryptUtil.cwnDecrypt(itmId), prof.usr_ent_id);
		model.addAttribute("instructorComment", ic);
		return JsonFormat.format(param, model);
	}
	
	
	@RequestMapping("{module}/{targetId}")
	@ResponseBody
	public String comment(Model model,
			loginProfile prof,
			Params param,
			@PathVariable(value="targetId") String targetId,
			@RequestParam(value="tkhId", required=false, defaultValue = "0") long tkhId,
			@RequestParam(value="repalyTo", required = false, defaultValue = "0") long repalyTo,
			@RequestParam(value="toUserId", required = false, defaultValue = "0") long toUserId,
			@RequestParam(value="note", required = true, defaultValue="") String note,
			@PathVariable(value="module") String module) throws MessageException, Exception{
		long real_targetId = EncryptUtil.cwnDecrypt(targetId);
		snsCommentService.comment(model, real_targetId, tkhId, module, note, prof.usr_ent_id, repalyTo, toUserId, prof.cur_lan, prof.current_role);
		if(module.equalsIgnoreCase("Course") && repalyTo == 0){
			forCallOldAPIService.updUserCredits(null, Credit.SYS_COS_COMMENT, prof.usr_ent_id, prof.usr_id, real_targetId, 0);
		}
		return JsonFormat.format(param, model);
	}
	
	
	@RequestMapping("{module}/commentJson/{encId}")
	@ResponseBody
	public String courseCommentList(Model model,
			loginProfile prof,
			Params param,
			@PathVariable(value="encId") String encId,
			@PathVariable(value="module") String module){
		long id = EncryptUtil.cwnDecrypt(encId);
		List<SnsComment> list = snsCommentService.getComments(id, module, prof.usr_ent_id);
		model.addAttribute(list);
		return JsonFormat.format(param, model);
	}
	
	
	@RequestMapping("{module}/commentPageJson/{encId}")
	@ResponseBody
	public String commentPage(Model model,
			loginProfile prof,
			@PathVariable(value="encId") String encId,
			@PathVariable(value="module") String module,
			Page<SnsComment> page){
		long id = EncryptUtil.cwnDecrypt(encId);
		List<SnsComment> list = snsCommentService.getComments(id, module, prof.usr_ent_id, page,prof);
		model.addAttribute(list);
		return JsonFormat.format(model, page);
	}
	
	
	@RequestMapping("del/{id}/{topParentId}")
	@ResponseBody
	public String del(Model model, 
			loginProfile prof,
			Params param,
			@PathVariable(value="id") long id,@PathVariable(value="topParentId") long top_parent_id) throws DataNotFoundException{
		boolean isManage = false;
	
		isManage = aclService.hasAnyPermission(prof.current_role, AclFunction.FTN_AMD_ARTICLE_MAIN);
		snsCommentService.delete(model, id,top_parent_id, prof.usr_ent_id, prof.my_top_tc_id, prof.cur_lan,isManage);
		return JsonFormat.format(param, model);
	}
	
}
