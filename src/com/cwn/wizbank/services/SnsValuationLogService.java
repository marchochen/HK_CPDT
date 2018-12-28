package com.cwn.wizbank.services;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cw.wizbank.config.WizbiniLoader;
import com.cwn.wizbank.common.SNS;
import com.cwn.wizbank.entity.KnowQuestion;
import com.cwn.wizbank.entity.Knowanswer;
import com.cwn.wizbank.entity.SnsComment;
import com.cwn.wizbank.entity.SnsDoing;
import com.cwn.wizbank.entity.SnsGroup;
import com.cwn.wizbank.entity.SnsShare;
import com.cwn.wizbank.entity.SnsValuationLog;
import com.cwn.wizbank.entity.vo.LikeMsgVo;
import com.cwn.wizbank.persistence.SnsValuationLogMapper;
import com.cwn.wizbank.utils.ContextPath;
import com.cwn.wizbank.utils.ImageUtil;
import com.cwn.wizbank.utils.LabelContent;
import com.cwn.wizbank.utils.Page;
/**
 *  service 实现
 */
@Service
public class SnsValuationLogService extends BaseService<SnsValuationLog> {
	@Autowired
	KnowQuestionService knowQuestionService;
	@Autowired
	KnowanswerService knowanswerService;
	@Autowired
	SnsGroupService snsGroupService;
	@Autowired
	SnsValuationLogMapper snsValuationLogMapper;
	@Autowired
	SnsDoingService snsDoingService;
	@Autowired
	SnsShareService snsShareService;
	@Autowired
	SnsCommentService snsCommentService;

	public void setSnsValuationLogMapper(SnsValuationLogMapper snsValuationLogMapper){
		this.snsValuationLogMapper = snsValuationLogMapper;
	}

	public SnsValuationLog getByUserId(long targetId,
			long userEntId, String module, int isComment) {
		SnsValuationLog valuation = null;
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("targetId", targetId);
		map.put("userEntId", userEntId);
		map.put("module", module);
		map.put("isComment", isComment);

		try{
			valuation = snsValuationLogMapper.getByUserId(map);
		}catch(Exception e){
			// 由于页面上快速点击多次，可能导致同一个用户在一个对像下会有多条记录，在这种情况下会出错，所以先删除出错的数据，再重新取数据
			snsValuationLogMapper.delErrorData(map);
			valuation = snsValuationLogMapper.getByUserId(map);
		}
		
		return valuation;
	}
	
	public int getCount (long targetId, long usrEntId, String module, int isComment){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("targetId", targetId);
		map.put("usrEntId", usrEntId);
		map.put("module", module);
		map.put("isComment", isComment);
		// 如果已赞过
		return snsValuationLogMapper.getCount(map);
	}
	
	public void deleteList(long targetId, String module,int is_comment){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("targetId", targetId);
		map.put("module", module);
		map.put("is_comment", is_comment);
		snsValuationLogMapper.deleteList(map);
	}
		
	public Long getUserLikeTotal(long usr_ent_id){
		return snsValuationLogMapper.selectUserLikeTotal(usr_ent_id);
	}
	
	public Page<LikeMsgVo> getLikeList(Page<LikeMsgVo> page,WizbiniLoader wizbini,long usr_ent_id,String cur_lang ){
		page.getParams().put("usr_ent_id", usr_ent_id);
		snsValuationLogMapper.userLikeList(page);
		//判断请求是不是由手机端发出
		if(page.getParams().get("isMobile") !=null && page.getParams().get("isMobile").equals("true")){
			adaptorLike(page.getResults(), cur_lang);
		}else{
			adaptorLike(page.getResults(),wizbini, cur_lang);					
		}
		return page;
	}
	/**
	 * mobile端使用
	 * @param list
	 * @param cur_lang
	 */
	private void adaptorLike(List<LikeMsgVo> list, String cur_lang) {
		if(list!=null&&list.size()>0){
			for(LikeMsgVo likeMsg:list){
				StringBuffer title=new StringBuffer();
				String url=null;
				if(SNS.MODULE_COMMENT.equalsIgnoreCase(likeMsg.getModule())){
					   	likeMsg.setTitleTcr(likeAction(cur_lang, likeMsg.getModule()));
					   	url = "course/detail.html?itmId=" + likeMsg.getTargetId();
					   	title.append(assemblyTitleMobile(url,likeMsg.getTitle()));
					}else if(SNS.MODULE_DOING.equalsIgnoreCase(likeMsg.getModule())){
						if(likeMsg.getIsComment() == 1){
							likeMsg.setModule(SNS.MODULE_COMMENT);
							title.append(likeMsg.getTitle());
						}else{
							SnsDoing sdoing = snsDoingService.get(get(likeMsg.getId()).getS_vtl_target_id());
							if(sdoing != null){
								if(sdoing.getS_doi_act_id() != null && sdoing.getS_doi_act_id() > 0) {
									if(SNS.DOING_ACTION_SHARE.equals(sdoing.getS_doi_act())){
										SnsShare share = snsShareService.get(sdoing.getS_doi_act_id());
										if(share != null){
											title.append(share.getS_sha_title());
										}
									} else {
										SnsComment targetComment = snsCommentService.get(sdoing.getS_doi_act_id());
										if(targetComment != null){
											title.append(targetComment.getS_cmt_content());
										}
									}
								} else {
									title.append(sdoing.getS_doi_title());
								}	
							}							
						}
					   likeMsg.setTitleTcr(likeAction(cur_lang, likeMsg.getModule()));
					   url="";					  
					}else if(SNS.MODULE_COURSE.equalsIgnoreCase(likeMsg.getModule())){
					   likeMsg.setTitleTcr(likeAction(cur_lang, likeMsg.getModule()));				   
					   url = "course/detail.html?itmId=" + likeMsg.getTargetId();
					   title.append(assemblyTitleMobile(url, likeMsg.getTitle()));
					}else if(SNS.MODULE_GROUP.equalsIgnoreCase(likeMsg.getModule())){
					   likeMsg.setTitleTcr(likeAction(cur_lang,SNS.MODULE_COMMENT));		
					   SnsGroup group = snsGroupService.get(likeMsg.getTargetId());					   
					   if(group != null){
							if(group.getS_grp_status().equalsIgnoreCase("OK")){
								url = "group/detail.html?groupId=" + likeMsg.getTargetId();
								title.append(assemblyTitleMobile(url, likeMsg.getTitle()));
							} else {
								title.append(likeMsg.getTitle() + LabelContent.get(cur_lang, "group_dissolved"));
							}
						}
					}else if(SNS.MODULE_ANSWER.equalsIgnoreCase(likeMsg.getModule())){
					   likeMsg.setTitleTcr(likeAction(cur_lang,likeMsg.getModule()));				  
					   Knowanswer answer = knowanswerService.get(likeMsg.getTargetId());
					   KnowQuestion question =knowQuestionService.get(answer.getAns_que_id());				  
						if(answer != null  && question != null){
							if(answer != null  && question != null){
								title.append(answer.getAns_content());
							    url = "know/detail.html?type=" + question.getQue_type() + "&id=" + question.getQue_id();
							    title.append("<br/>");
								title.append(assemblyTitleMobile(url,question.getQue_title()));
							}
					   }
					}
				    if(url !=null && !url.equals("")){
				    	likeMsg.setUrl("");
				    }
					if(title.toString()!=null){
						likeMsg.setTitle(likeMsg.getTitleTcr()+":"+title.toString());
					}
					if (likeMsg.getUser() != null) {
						ImageUtil.combineImagePath(likeMsg.getUser());
					}
					if(likeMsg.getOperator() != null){
						ImageUtil.combineImagePath(likeMsg.getOperator());
					}
			}
		}
	}

	private void adaptorLike(List<LikeMsgVo> list,WizbiniLoader wizbini,String cur_lang) {

		if(list!=null&&list.size()>0){
			for(LikeMsgVo likeMsg:list){
				String url=null;
				StringBuffer title=new StringBuffer();
				if(SNS.MODULE_COMMENT.equalsIgnoreCase(likeMsg.getModule())){
					likeMsg.setTitleTcr(likeAction(cur_lang,likeMsg.getModule()));				   
					url = ContextPath.getContextPath() + "/app/course/detail/" + likeMsg.getTargetId();
					title.append(assemblyTitle(url, likeMsg.getTitle()));
				}else if(SNS.MODULE_DOING.equalsIgnoreCase(likeMsg.getModule())){		
					if(likeMsg.getIsComment() == 1){
						title.append(likeMsg.getTitle());
						likeMsg.setModule(SNS.MODULE_COMMENT);
					}else{
						SnsDoing sdoing = snsDoingService.get(get(likeMsg.getId()).getS_vtl_target_id());
						if(sdoing != null){
							if(sdoing.getS_doi_act_id() != null && sdoing.getS_doi_act_id() > 0) {
								if(SNS.DOING_ACTION_SHARE.equals(sdoing.getS_doi_act())){
									SnsShare share = snsShareService.get(sdoing.getS_doi_act_id());
									if(share != null){
										title.append(share.getS_sha_title());
									}
								} else {
									SnsComment targetComment = snsCommentService.get(sdoing.getS_doi_act_id());
									if(targetComment != null){
										title.append(targetComment.getS_cmt_content());
									}
								}
							} else {
								title.append(sdoing.getS_doi_title());
							}	
						}						
					}
					likeMsg.setTitleTcr(likeAction(cur_lang,likeMsg.getModule()));
					url="";					
				}else if(SNS.MODULE_COURSE.equalsIgnoreCase(likeMsg.getModule())){
				   likeMsg.setTitleTcr(likeAction(cur_lang,likeMsg.getModule()));				   
				   url = ContextPath.getContextPath() + "/app/course/detail/" + likeMsg.getTargetId();
				   title.append(assemblyTitle(url, likeMsg.getTitle()));
				}else if(SNS.MODULE_GROUP.equalsIgnoreCase(likeMsg.getModule())){
				   likeMsg.setTitleTcr(likeAction(cur_lang,SNS.MODULE_COMMENT));		
				   SnsGroup group = snsGroupService.get(likeMsg.getTargetId());
				   url = ContextPath.getContextPath() + "/app/group/groupDetail/" + likeMsg.getTargetId();
				   if(group != null){
						if(group.getS_grp_status().equalsIgnoreCase("OK")){
							title.append(assemblyTitle(url, likeMsg.getTitle()));
						} else {
							title.append(likeMsg.getTitle() + LabelContent.get(cur_lang, "group_dissolved"));
						}
					}
				}else if(SNS.MODULE_ANSWER.equalsIgnoreCase(likeMsg.getModule())){
				   likeMsg.setTitleTcr(likeAction(cur_lang,likeMsg.getModule()));				  
				   Knowanswer answer = knowanswerService.get(likeMsg.getTargetId());
				   KnowQuestion question =knowQuestionService.get(answer.getAns_que_id());				  
					if(answer != null  && question != null){
						if(answer != null  && question != null){
							title.append(answer.getAns_content());
						    url = ContextPath.getContextPath() + "/app/know/knowDetail/" + question.getQue_type() + "/" + question.getQue_id();
							title.append("<br/>");
							title.append(assemblyTitle(url, question.getQue_title()));
						}
				   }
				}else if(SNS.MODULE_ARTICLE.equalsIgnoreCase(likeMsg.getModule())){
					   likeMsg.setTitleTcr(likeAction(cur_lang,likeMsg.getModule()));				   
					   url = ContextPath.getContextPath() + "/app/article/detail/" + likeMsg.getTargetId();
					   title.append(assemblyTitle(url, likeMsg.getTitle()));
				}else if(SNS.MODULE_KNOWLEDGE.equalsIgnoreCase(likeMsg.getModule())){
					   likeMsg.setTitleTcr(likeAction(cur_lang,likeMsg.getModule()));				   
					   url = ContextPath.getContextPath() + "/app/kb/center/view?source=index&enc_kbi_id=" + likeMsg.getTargetId();
					   title.append(assemblyTitle(url, likeMsg.getTitle()));
					}
				if(title.toString()!=null){
					likeMsg.setTitle(title.toString());
				}
				if (likeMsg.getUser() != null) {
					ImageUtil.combineImagePath(likeMsg.getUser());
				}
				if(likeMsg.getOperator() != null){
					ImageUtil.combineImagePath(likeMsg.getOperator());
				}
			}
		}
	}
	private String likeAction(String curLang,String module) {
		String action=LabelContent.get(curLang, "doing_action_like");
		action+=getActionType(curLang,module);
		return action;
	}
	private String getActionType(String cur_lang, String module){
		String actionType = "";
		if(SNS.MODULE_COURSE.equalsIgnoreCase(module)){
			actionType = LabelContent.get(cur_lang, "doing_lowercase_course");
		} else if(SNS.MODULE_ARTICLE.equalsIgnoreCase(module)){
			actionType = LabelContent.get(cur_lang, "doing_lowercase_article");
		} else if(SNS.MODULE_DOING.equalsIgnoreCase(module)){
			actionType = LabelContent.get(cur_lang, "doing_lowercase_title");
		} else if(SNS.MODULE_QUESTION.equalsIgnoreCase(module) || SNS.MODULE_ANSWER.equals(module)){
			actionType = LabelContent.get(cur_lang, "personal_know");
		} else if(SNS.MODULE_GROUP.equalsIgnoreCase(module)){
			actionType = LabelContent.get(cur_lang, "doing_lowercase_group");
		} else if(SNS.MODULE_COMMENT.equalsIgnoreCase(module)){
			actionType = LabelContent.get(cur_lang, "sns_lowercase_comment");
		}else if(SNS.MODULE_KNOWLEDGE.equalsIgnoreCase(module)){
			actionType = LabelContent.get(cur_lang, "doing_lowercase_knowledge");
		}
		return actionType;
	}
	/**
	 * 组合超链接
	 * @param url
	 * @param title
	 * @return
	 */
	public String assemblyTitle(String url, String title){
		StringBuffer ahtml = new StringBuffer();
		ahtml.append("<a href=\"");
		ahtml.append(url);
		ahtml.append("\" class=\"swop_color mr3\" title=\"\">");
		ahtml.append(title);
		ahtml.append("</a>  ");
		return ahtml.toString();
	}
	//组合手机端的超链接
	public String assemblyTitleMobile(String url, String title){
		StringBuffer ahtml = new StringBuffer();
		ahtml.append("<a href=\"javascript:clicked('../"+url+"',true);");
		ahtml.append("\" title=\"\">");
		ahtml.append(title);
		ahtml.append("</a>  ");
		return ahtml.toString();
	}
	
}