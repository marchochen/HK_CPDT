package com.cwn.wizbank.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.common.SNS;
import com.cwn.wizbank.entity.AeApplication;
import com.cwn.wizbank.entity.SnsComment;
import com.cwn.wizbank.entity.SnsDoing;
import com.cwn.wizbank.exception.DataNotFoundException;
import com.cwn.wizbank.exception.MessageException;
import com.cwn.wizbank.persistence.AeApplicationMapper;
import com.cwn.wizbank.persistence.SnsCommentMapper;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.security.service.AclService;
import com.cwn.wizbank.utils.ImageUtil;
import com.cwn.wizbank.utils.LabelContent;
import com.cwn.wizbank.utils.Page;
import com.cwn.wizbank.utils.RequestStatus;
/**
 * service 实现
 */
@Service
public class SnsCommentService extends BaseService<SnsComment> {

	@Autowired
	SnsCommentMapper snsCommentMapper;

	@Autowired
	SnsDoingService snsDoingService;

	@Autowired
	SnsValuationLogService snsValuationLogService;

	@Autowired
	SnsValuationService SnsValuationService;

	@Autowired
	SnsCountService snsCountService;

	@Autowired
	AcRoleFunctionService acRoleFunctionService;

	@Autowired
	AeApplicationMapper aeApplicationMapper;

	@Autowired
	AeApplicationService aeApplicationService;

	@Autowired
	RegUserService regUserService;

	@Autowired
	AclService aclService;

	public SnsComment add(long targetId, String module, String note, long usr_ent_id, long replayTo, long toUserId) {
		SnsComment comment = new SnsComment();
		comment.setS_cmt_target_id(targetId);
		comment.setS_cmt_content(note.replaceAll("<", "&gt;"));
		comment.setS_cmt_create_datetime(getDate());
		comment.setS_cmt_is_reply(replayTo > 0);
		comment.setS_cmt_module(module);
		comment.setS_cmt_reply_to_id(replayTo);
		comment.setS_cmt_uid(usr_ent_id);
		comment.setS_cmt_anonymous(0l);
		comment.setS_cmt_reply_to_uid(toUserId);
		snsCommentMapper.insert(comment);
		return comment;
	}
	/**
	 *
	 * @param model
	 * @param targetId(评论的对象的ID)
	 * @param tkhId(学习记录ID)
	 * @param module(评论的类型，如课程，群组)
	 * @param note(评论的内容)
	 * @param userEntId(发起评论用户的ID)
	 * @param replyTo(回复评论的ID)
	 * @param toUserId(评论对象用户的ID)
	 * @param curLang
	 * @param commentRoleId
	 * @throws MessageException
	 */
	public void comment(Model model, long targetId, long tkhId, String module, String note, long userEntId, long replyTo, long toUserId, String curLang, String commentRoleId) throws MessageException {
		// -1 是公开课
		/*if(SNS.MODULE_COURSE.equals(module) && tkhId != -1){
			AeApplication app = null;
			if(tkhId < 1){//如果没有学习记录，则代表该评论是对评论进行回复
				 if(replyTo > 0){//如果该评论不是回复
					 SnsComment comment = snsCommentMapper.get(replyTo);//获取需要回复的评论
					 if(targetId != replyTo){
						 comment = snsCommentMapper.get(comment.getS_cmt_target_id());
					 }
					 app = aeApplicationService.getMaxAppByUser(userEntId, comment.getS_cmt_target_id());//获取回复的评论的课程ID
				 } else{
					 app = aeApplicationService.getMaxAppByUser(userEntId, targetId);//获取该课程的报名ID
				 }
			} else {
				 app = aeApplicationMapper.getByTkhId(tkhId);//获取学习记录的报名ID
			}
			if(app != null){
				if(!AeApplication.APP_STATUS_ADMITTED.equals(app.getApp_status())){
					throw new MessageException(LabelContent.get(curLang, "error_msg_need_app"));
				}
			} else {
				throw new MessageException(LabelContent.get(curLang, "error_msg_need_app"));
			}
		}*/
		SnsComment comment = add(targetId, module, note, userEntId, replyTo, toUserId);//插入评论表
		comment.setUser(regUserService.getUserDetail(userEntId));//设置评论的用户
		model.addAttribute("comment",comment);
		// 发布课程评论后，发布动态，回复评论不发布动态
		if (replyTo <= 0) {
//			if(SNS.MODULE_COURSE.equals(module) || SNS.MODULE_ARTICLE.equals(module)){
				//snsDoingService.add(targetId, 0, userEntId, 0, SNS.DOING_ACTION_COMMENT, comment.getS_cmt_id(), module, "", 0);
//			}
		} else {
			boolean isManage = false;
			boolean isCommonManage;
			if(SNS.MODULE_ARTICLE.equals(module)){
				isManage = aclService.hasAnyPermission(commentRoleId, AclFunction.FTN_AMD_ARTICLE_MAIN);
			}
			else if(SNS.MODULE_COURSE.equals(module)){
				isManage = aclService.hasAnyPermission(commentRoleId, new String []{AclFunction.FTN_AMD_EXAM_MGT,AclFunction.FTN_AMD_ITM_COS_MAIN,AclFunction.FTN_AMD_OPEN_COS_MAIN});
			}

			//找到根
			List<SnsComment> replies = getCommentReply(targetId, userEntId, comment.getS_cmt_module());//获取该评论的所有回复

			for(SnsComment c : replies){
				if(c.getUser() != null){
					ImageUtil.combineImagePath(c.getUser());
				}
				if(c.getToUser() != null){
					ImageUtil.combineImagePath(c.getToUser());
				}
				c.setSnsCount(snsCountService.getByTargetInfo(c.getS_cmt_id(), c.getS_cmt_module(),1));//获取每个评论的赞统计信息
				isCommonManage = false;
				isCommonManage = isManage || (c.getS_cmt_uid() == userEntId);
				c.setManage(isManage);
				c.setCommonManage(isCommonManage);
			}
			model.addAttribute("replies", replies);
			//产生通知
			if(SNS.MODULE_DOING.equals(module)){
				//赞动态产生动态
				SnsDoing doing	= snsDoingService.get(targetId);
				if (doing != null){
					// 产生通知
					//snsDoingService.add(targetId, 1, toUserId>0?toUserId:doing.getS_doi_uid(), userEntId, SNS.DOING_ACTION_COMMENT, comment.getS_cmt_id(), module, "", replyTo);
				}
			} else if(!SNS.MODULE_GROUP.equals(module)){
				SnsComment scm = get(targetId);//获取被回复的评论

				//snsDoingService.add(scm.getS_cmt_target_id(), 1, toUserId>0?toUserId:scm.getS_cmt_uid(), userEntId, SNS.DOING_ACTION_COMMENT, comment.getS_cmt_id(), module, "", replyTo);
				//snsDoingService.add(targetId, module, userEntId, SNS.DOING_ACTION_COMMENT, 0, module, "", 0);
			}

		}
		model.addAttribute(RequestStatus.STATUS, RequestStatus.SUCCESS);
	}

	/**
	 * 递归查找子类
	 * @param list
	 */
	public void setSubComment(List<SnsComment> list){
		if(list!=null&& list.size()>0){
			for(SnsComment comment : list) {
				comment.setReplies(getTargetCommnet(comment.getS_cmt_id(), 0, comment.getS_cmt_module()));
				if(comment.getReplies()!=null && comment.getReplies().size()>0){
					setSubComment(comment.getReplies());
				}
			}
		}
	}

	/**
	 * 获取对象的评论
	 * @param id	对象的id
	 * @param module 对象的类型
	 * @return
	 */
	public List<SnsComment> getComments(long id, String module, long userEntId){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("targetId", id);
		map.put("module", module);
		map.put("userEntId", userEntId);
		List<SnsComment> list = snsCommentMapper.getComments(map);
		for(SnsComment comment : list) {
			List<SnsComment> replies = getTargetCommnet(comment.getS_cmt_id(), userEntId, comment.getS_cmt_module());
			for(SnsComment c : replies){
				ImageUtil.combineImagePath(c.getUser());
			}
			comment.setReplies(replies);
			ImageUtil.combineImagePath(comment.getUser());
		}
		return list;
	}

	public List<SnsComment> getComments(long id, String module, long userEntId, Page<SnsComment> page,loginProfile prof){
		Map<String,Object> map = page.getParams();
		map.put("targetId", id);
		map.put("module", module);
		map.put("userEntId", userEntId);
		boolean isManage = false;
		if(SNS.MODULE_ARTICLE.equals(module)){
			isManage = aclService.hasAnyPermission(prof.current_role, AclFunction.FTN_AMD_ARTICLE_MAIN);
		} else if(SNS.MODULE_COURSE.equals(module)){
			isManage = aclService.hasAnyPermission(prof.current_role, new String []{AclFunction.FTN_AMD_EXAM_MGT,AclFunction.FTN_AMD_ITM_COS_MAIN,AclFunction.FTN_AMD_OPEN_COS_MAIN});
		} else if(SNS.MODULE_KNOWLEDGE.equals(module)){
			isManage = aclService.hasAnyPermission(prof.current_role, AclFunction.FTN_AMD_KNOWLEDGE_MGT);
		}
		List<SnsComment> list = snsCommentMapper.getCommentPage(page);
		for(SnsComment comment : list) {
			comment.setManage(isManage);
			comment.setCommonManage(isManage || (comment.getS_cmt_uid() == prof.usr_ent_id));

			List<SnsComment> replies = getCommentReply(comment.getS_cmt_id(), userEntId, module);
			for(SnsComment c : replies){
				ImageUtil.combineImagePath(c.getUser());
				c.setManage(isManage);
				c.setCommonManage(isManage || (c.getS_cmt_uid() == prof.usr_ent_id));
			}
			comment.setReplies(replies);
			ImageUtil.combineImagePath(comment.getUser());

		}
		return list;
	}


	public void delete(Model model, long id,long top_parent_id, long usr_ent_id, long my_top_tc_id, String cur_lan,boolean isManage) throws DataNotFoundException {
		SnsComment comment = get(id);
		if(comment == null) { throw new DataNotFoundException(LabelContent.get(cur_lan, "error_data_not_found"));}
		//有权限删除
		if((comment.getS_cmt_reply_to_uid() != null && comment.getS_cmt_reply_to_uid() == usr_ent_id)
				|| (comment.getS_cmt_uid() != null && comment.getS_cmt_uid() == usr_ent_id || isManage)){

			//获取需要删除评论所有子级评论
			List<SnsComment> sub_comment=new ArrayList<SnsComment>();
			getCommentAllReply(comment.getS_cmt_id(),sub_comment);
			sub_comment.add(comment);
			//删除所有子级评论的所有的赞，赞的记录，动态，统计信息
			for(SnsComment ment:sub_comment){
				//删除评论所有的赞记录
				SnsValuationService.delValuation(comment.getS_cmt_module(),comment.getS_cmt_id(),1);
				//删除评论所有赞记录日志
				snsValuationLogService.deleteList(comment.getS_cmt_id(), comment.getS_cmt_module(),1);
				//删除评论赞统计信息
				snsCountService.deleteRecord(comment.getS_cmt_id(), comment.getS_cmt_module(), 1);
				//删除评论所有的动态
				snsDoingService.deleteDoingByModule(ment.getS_cmt_id(), "COMMENT");
				//删除评论
				delete(ment.getS_cmt_id());
			}

			List<SnsComment> replies = null;
			replies = getCommentReply(top_parent_id, usr_ent_id, comment.getS_cmt_module());
			for(SnsComment c : replies){
				ImageUtil.combineImagePath(c.getUser());
				c.setManage(isManage);
				c.setCommonManage(isManage || (c.getS_cmt_uid() == usr_ent_id));
			}
			model.addAttribute("replies", replies);
		} else {
			//TODO没有权限
			model.addAttribute(RequestStatus.STATUS, RequestStatus.NO_PERMISSION);
		}

		model.addAttribute(RequestStatus.STATUS, RequestStatus.SUCCESS);
	}


	public void deleteList(long targetId, String module){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("targetId", targetId);
		map.put("module", module);
		snsCommentMapper.deleteList(map);
	}


	public SnsComment getByUserId(Long s_doi_target_id, long userEntId,
			String s_doi_module, long s_doi_act_id) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("targetId", s_doi_target_id);
		params.put("userEntId", userEntId);
		params.put("module", s_doi_module);
		params.put("actId", s_doi_act_id);
		return snsCommentMapper.getByUserId(params);
	}

	public List<SnsComment> getTargetCommnet(long targetId, long userEntId, String module){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("targetId", targetId);
		params.put("userEntId", userEntId);
		params.put("module", module);
		return snsCommentMapper.getTargetCommnet(params);
	}

	public List<SnsComment> getCommentReply(long targetId, long userEntId, String module){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("targetId", targetId);
		params.put("userEntId", userEntId);
		params.put("module", module);
		return snsCommentMapper.getCommentReply(params);
	}

	public void setSnsCommentMapper(SnsCommentMapper snsCommentMapper) {
		this.snsCommentMapper = snsCommentMapper;
	}

	/**
	 * 获取某个评论所有子级评论
	 * @param cmt_id 评论ID
	 * @param result 返回的结果
	 */
	public void getCommentAllReply(long cmt_id,List<SnsComment> result){
		if(result==null){
			result=new ArrayList<SnsComment>();
		}
		List<SnsComment> sub_comment=snsCommentMapper.getCommentByReplyId(cmt_id);
		for(SnsComment ment:sub_comment){
			result.add(ment);
			getCommentAllReply(ment.getS_cmt_id(),result);
		}
	}

	/**
	 * 获取目标评论数量
	 * @param itmId
	 * @param module
	 * @return
	 */
	public Long getCommentCount(Long itmId, String module) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("itmId", itmId);
		params.put("module", module);

		return snsCommentMapper.getCommentCount(params);
	}
	
	/**
	 * 获取课程下班级或者考试场次的目标评论数量
	 * @param itmId
	 * @param module
	 * @return
	 */
	public Long getClassCommentCount(Long itmId, String module) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("itmId", itmId);
		params.put("module", module);
		return snsCommentMapper.getClassCommentCount(params);
	}
}