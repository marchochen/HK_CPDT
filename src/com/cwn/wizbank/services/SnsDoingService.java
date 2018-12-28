package com.cwn.wizbank.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.cw.wizbank.config.WizbiniLoader;
import com.cwn.wizbank.common.SNS;
import com.cwn.wizbank.entity.AeItem;
import com.cwn.wizbank.entity.Article;
import com.cwn.wizbank.entity.KbItem;
import com.cwn.wizbank.entity.KnowQuestion;
import com.cwn.wizbank.entity.Knowanswer;
import com.cwn.wizbank.entity.ModuleTempFile;
import com.cwn.wizbank.entity.RegUser;
import com.cwn.wizbank.entity.SnsComment;
import com.cwn.wizbank.entity.SnsDoing;
import com.cwn.wizbank.entity.SnsGroup;
import com.cwn.wizbank.entity.SnsShare;
import com.cwn.wizbank.exception.MessageException;
import com.cwn.wizbank.persistence.SnsDoingMapper;
import com.cwn.wizbank.utils.CommonLog;
import com.cwn.wizbank.utils.ContextPath;
import com.cwn.wizbank.utils.FileUtils;
import com.cwn.wizbank.utils.ImageUtil;
import com.cwn.wizbank.utils.LabelContent;
import com.cwn.wizbank.utils.Page;
import com.cwn.wizbank.utils.RequestStatus;

/**
 * 用户 service 实现
 *
 * 赞，和评论 会产生动态，这个动态的类型为 Notice @see SNS.java
 * 二级回复产生的动态，在页面上显示为，
 * 						      某某 回复了你：
 * 						   xxx(课程)
 * 							----------
 * 						 	回复的内容
 * 赞课程，文章等产生的动态，不能再继续点赞，不过可以评论
 * 评论文章，课程产生的动态，可以赞/可以评论，产生的动态可以评论可以继续点赞。
 * 通知不能点赞，但是可以评论。且评论和赞通知类型的动态不产生新的动态
 */
@Service
public class SnsDoingService extends BaseService<SnsDoing> {

	@Autowired
	SnsDoingMapper snsDoingMapper;

	@Autowired
	ModuleTempFileService moduleTempFileService;

	@Autowired
	SnsCommentService snsCommentService;

	@Autowired
	SnsCountService snsCountService;

	@Autowired
	SnsValuationLogService snsValuationLogService;

	@Autowired
	ArticleService articleService;

	@Autowired
	AeItemService aeItemService;

	@Autowired
	SnsShareService snsShareService;

	@Autowired
	SnsGroupService snsGroupService;

	@Autowired
	KnowQuestionService knowQuestionService;

	@Autowired
	KnowanswerService knowanswerService;

	@Autowired
	KbItemService kbItemService;

	@Autowired
	SnsValuationService snsValuationService;
	
	/**
	 * 获取所有的动态信息
	 *
	 * @param usr_ent_id
	 *            用户id
	 * @param tcr_id
	 * @param cur_lang
	 *            当前语言
	 * @return
	 */
	public Page<SnsDoing> listAll(long userEntId, Page<SnsDoing> page, WizbiniLoader wizbini,
			Long tcr_id, String cur_lang, String type) {
		page.getParams().put("userEntId", userEntId);
		page.getParams().put("tcrId", tcr_id);
		page.getParams().put("type", type);

		snsDoingMapper.selectList(page);
		try{
			//不try 报spring事务异常
			if(page.getParams().get("isMobile") != null && page.getParams().get("isMobile").equals("true")){
				adaptorDoing(page.getResults(), wizbini, userEntId, cur_lang, "");
			}else{
				adaptorDoing(page.getResults(), wizbini, userEntId, cur_lang, "", false);
			}
		} catch(Exception e){
			CommonLog.error(e.getMessage(),e);
		}
		return page;
	}

	/**
	 * 获取首页动态
	 * @param userEntId
	 * @param page
	 * @param wizbini
	 * @param tcr_id
	 * @param cur_lang
	 * @param type
	 * @return
	 */
	public Page<SnsDoing> listIndex(long userEntId, Page<SnsDoing> page, WizbiniLoader wizbini,
			Long tcrId, String cur_lang, String type){
		page.getParams().put("userEntId", userEntId);
		page.getParams().put("tcrId", tcrId);
		page.getParams().put("type", type);
		snsDoingMapper.selectIndexList(page);
		adaptorDoing(page.getResults(), wizbini,userEntId, cur_lang, "", true);
		return page;
	}

	/**
	 * 封装其他的数据
	 * @param list
	 * @param wizbini
	 * @param userEntId
	 * @param cur_lang
	 */
	public void adaptorDoing(List<SnsDoing> list, WizbiniLoader wizbini, long userEntId, String cur_lang, String module, boolean isIndex){
		if(StringUtils.isEmpty(module)) {
			module = SNS.MODULE_DOING;
		}
		String urlPath =  "/"+ wizbini.cfgSysSetupadv.getFileUpload().getEditorDir().getUrl();
		try{
			if(list != null && list.size() > 0){
				for(SnsDoing doing : list){
					long isExam = 0;
					long isComment = 0;

					//处理用户已删除的情况
					if(doing.getUser() == null) {
						doing.setUser(new RegUser(0l, "", ""));
					}
					if(doing.getOperator() == null) {
						doing.setOperator(new RegUser(0l, "", ""));
					}
					if( (SNS.MODULE_COURSE.equals(doing.getS_doi_module()) || SNS.MODULE_KNOWLEDGE.equals(doing.getS_doi_module())
							|| SNS.MODULE_ARTICLE.equals(doing.getS_doi_module())) && StringUtils.isNotEmpty(doing.getS_doi_title())){
						//课程和文章、知识
						String url = "";
						StringBuffer title = new StringBuffer();
						String objTitle = "";
						//文章
						if(SNS.MODULE_ARTICLE.equals(doing.getS_doi_module())) {
							Article obj = articleService.get(doing.getS_doi_target_id());
							if(obj != null) {
								objTitle = obj.getArt_title();
								if(obj.getArt_status() == 1)
									url = ContextPath.getContextPath() + "/app/article/detail/" + doing.getS_doi_target_id();
							}
						} else if(SNS.MODULE_KNOWLEDGE.equals(doing.getS_doi_module())){
							KbItem kbitem = kbItemService.get(doing.getS_doi_target_id());
							if(kbitem != null) {
								objTitle = kbitem.getKbi_title();
								url = ContextPath.getContextPath() + "/app/kb/center/view?source=index&kbi_id=" + doing.getS_doi_target_id()+"&kbi_type="+kbitem.getKbi_type();
							} else {
								objTitle = doing.getS_doi_title();
								url = ContextPath.getContextPath() + "/app/kb/center/view?source=index&kbi_id=" + doing.getS_doi_target_id();
							}
						} else {
							//课程
							AeItem item = aeItemService.get(doing.getS_doi_target_id());
							if(item !=  null){
								objTitle = item.getItm_title();
								if(item.getItm_status().equalsIgnoreCase("ON")){
									url = ContextPath.getContextPath() + "/app/course/detail/" + doing.getS_doi_target_id();
								}
								isExam = item.getItm_exam_ind();
							}
						}
						//组合超链接
						if(!url.equalsIgnoreCase("")){
							title.append(assemblyTitle(url, objTitle) + "<br/>");
						} else {
							title.append(objTitle + "(" +  LabelContent.get(cur_lang, "global_unpublished") + ")<br/>");
						}

						//分享
						if(SNS.DOING_ACTION_SHARE.equals(doing.getS_doi_act())
								&& doing.getS_doi_act_id() > 0){
							SnsShare share = snsShareService.get(doing.getS_doi_act_id());
							if(share != null){
								title.append(share.getS_sha_title()==null? "" : share.getS_sha_title());
							}
						}
						//评论
						if(SNS.DOING_ACTION_COMMENT.equals(doing.getS_doi_act())
								&& doing.getS_doi_act_id() > 0){

							SnsComment cmt = snsCommentService.get(doing.getS_doi_reply_id());
							if (cmt != null) {
								title.append(cmt.getS_cmt_content()==null? "" : cmt.getS_cmt_content());
							}

							SnsComment comment = snsCommentService.get(doing.getS_doi_act_id());
							if (comment != null) {
								if(cmt != null )
									title.append("<div class=replydesc>");

								title.append(comment.getS_cmt_content()==null? "" : comment.getS_cmt_content());

								if(cmt != null )
									title.append("</div>");
							}

						}
						//赞课程
						if(SNS.DOING_ACTION_LIKE.equals(doing.getS_doi_act())
							&& doing.getS_doi_act_id() > 0)
						{
							SnsComment comment = snsCommentService.get(doing.getS_doi_act_id());
							if(comment != null) {
								title.insert(0,comment.getS_cmt_content() == null ? "" : comment.getS_cmt_content());
							}

						}

						doing.setS_doi_title(title.toString());

					} else if(SNS.MODULE_GROUP.equals(doing.getS_doi_module()) && !SNS.DOING_ACTION_GROUP.equals(doing.getS_doi_act())){
						//群组
						SnsGroup group = snsGroupService.get(doing.getS_doi_target_id());
						String url = ContextPath.getContextPath() + "/app/group/groupDetail/" + doing.getS_doi_target_id();
						StringBuffer title = new StringBuffer();
						if(group != null){
							if(group.getS_grp_status().equalsIgnoreCase("OK")){
								title.append(assemblyTitle(url, group.getS_grp_title()));
							} else {
								title.append(group.getS_grp_title() + LabelContent.get(cur_lang, "group_dissolved"));
							}
						}
						doing.setS_doi_title(title.toString());
					} else if(SNS.MODULE_QUESTION.equals(doing.getS_doi_module())) {
						//问答
						KnowQuestion question = knowQuestionService.get(doing.getS_doi_target_id());
						if(question != null){
							String url = ContextPath.getContextPath() + "/app/know/knowDetail/" + question.getQue_type() + "/" + doing.getS_doi_target_id();
							doing.setS_doi_title(assemblyTitle(url, question.getQue_title()));
						}

					} else if(SNS.MODULE_ANSWER.equals(doing.getS_doi_module())){
						//问答，如果是新增问题，则act_id 为问题的id，否则是回答的id
						StringBuffer title = new StringBuffer();
						Knowanswer answer = knowanswerService.get(doing.getS_doi_target_id());
						KnowQuestion question = null;
						if(SNS.DOING_ACTION_ANSWER_ADD.equals(doing.getS_doi_act())){
							question = knowQuestionService.get(doing.getS_doi_target_id());
							if(question != null) {
								String url = ContextPath.getContextPath() + "/app/know/knowDetail/" + question.getQue_type() + "/" + question.getQue_id();
								title.append(assemblyTitle(url, question.getQue_title()));
							}
						} else {
							question = knowQuestionService.get(answer.getAns_que_id());
							if(answer != null  && question != null){
								title.append(answer.getAns_content());
								String url = ContextPath.getContextPath() + "/app/know/knowDetail/" + question.getQue_type() + "/" + question.getQue_id();
								title.append("<br/>");
								title.append(assemblyTitle(url, question.getQue_title()));
							}
						}
						doing.setS_doi_title(title.toString());

					} else if(SNS.MODULE_DOING.equals(doing.getS_doi_module())

							) {
						StringBuffer title = new StringBuffer();

						//如果是动态或者是通知
						//动作赞
						if(SNS.DOING_ACTION_LIKE.equals(doing.getS_doi_act())){
							SnsDoing sdoing = get(doing.getS_doi_target_id());
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
							doing.setS_doi_title(title.toString());
						//动作评论
						} else if(SNS.DOING_ACTION_COMMENT.equals(doing.getS_doi_act())){
							SnsDoing sdoing = get(doing.getS_doi_target_id());

							SnsComment targetComment = snsCommentService.get(doing.getS_doi_act_id());
							if(targetComment != null){
								//下滑线
								title.append("<div class=replydesc>");
								title.append(targetComment.getS_cmt_content() == null ? "" : targetComment.getS_cmt_content());
								title.append("</div>");
							}
							//回复二级的话
							if(targetComment != null && targetComment.getS_cmt_reply_to_id() != null && targetComment.getS_cmt_reply_to_id() > 0
									&& targetComment.getS_cmt_reply_to_uid() != null && targetComment.getS_cmt_reply_to_uid() >0){
								SnsComment tc = snsCommentService.get(targetComment.getS_cmt_reply_to_id());
								if(tc != null){
									title.insert(0, tc.getS_cmt_content() == null ? "" : tc.getS_cmt_content() );
								}
								isComment = 1;	//标记是评论
							} else {
								//回复二级评论产生的通知

								if(sdoing != null && sdoing.getS_doi_act_id() != null && sdoing.getS_doi_act_id() > 0) {
									if(SNS.DOING_ACTION_SHARE.equals(sdoing.getS_doi_act())){

										SnsShare share = snsShareService.get(sdoing.getS_doi_act_id());
										if(share != null){
											title.insert(0, share.getS_sha_title() == null ? "" : share.getS_sha_title());
										}
									}
									else if(SNS.DOING_ACTION_COMMENT.equals(doing.getS_doi_act())){
										SnsComment sc = snsCommentService.get(sdoing.getS_doi_act_id());
										//拿回复的内容
										if(sc!=null)
										title.insert(0, sc.getS_cmt_content() == null ? "" : sc.getS_cmt_content());
									}
								} else {
									if(SNS.DOING_ACTION_COMMENT.equals(doing.getS_doi_act())){
										//拿回复的内容
										if(sdoing!=null)
										title.insert(0,sdoing.getS_doi_title() == null ? "" : sdoing.getS_doi_title());
									}
								}
							}


							doing.setS_doi_title(title.toString());

						}
					} else if("1".equals(doing.getS_doi_target_type())){

						StringBuffer title = new StringBuffer();
						//如果是动态或者是通知
						//动作赞
						if(SNS.DOING_ACTION_LIKE.equals(doing.getS_doi_act())){
							SnsComment comment = snsCommentService.get(doing.getS_doi_act_id());
							if(comment != null) {
								title.append(comment.getS_cmt_content());
							}
							doing.setS_doi_title(title.toString());
						//动作评论
						} else if(SNS.DOING_ACTION_COMMENT.equals(doing.getS_doi_act())){
							SnsComment comment = snsCommentService.get(doing.getS_doi_target_id());
							//拿回复的内容
							if(comment != null){
								title.append(comment.getS_cmt_content() == null ? "" : comment.getS_cmt_content());
							}
							//TODO action

							//本条通知回复的内容
							SnsComment targetComment = snsCommentService.get(doing.getS_doi_act_id());
							if(targetComment != null){
								//下滑线
								title.append("<div class=replydesc>");
								title.append(targetComment.getS_cmt_content() == null ? "" : targetComment.getS_cmt_content());
								title.append("</div>");
							}
							doing.setS_doi_title(title.toString());
						}

					} else {
						if(StringUtils.isNotEmpty(doing.getS_doi_title())){
							doing.setS_doi_title(doing.getS_doi_title().replaceAll("course_detail\\?itm_id=", "detail/"));
						}
					}

					//动态的动作
					doing.setS_doi_act_str(doAction(cur_lang, doing, isExam, isComment));

					//用户头像
					if (doing.getUser() != null) {
						ImageUtil.combineImagePath(doing.getUser());
					}
					if(doing.getOperator() != null){
						ImageUtil.combineImagePath(doing.getOperator());
					}

					//不是首页再封装这些数据
					if(!isIndex){
						//二级评论，如有性能问题，可以考虑异步获取
						List<SnsComment> replies = snsCommentService.getTargetCommnet(doing.getS_doi_id(), userEntId, module);
						for(SnsComment c : replies) {
							if (c.getUser() != null) {
								ImageUtil.combineImagePath(c.getUser());;
							}
							//二级回复的数量
							c.setSnsCount(snsCountService.getByTargetInfo(c.getS_cmt_id(), module, 1));
						}
						doing.setReplies(replies);

						//一级评论的数量
						doing.setSnsCount(snsCountService.getByTargetInfo(doing.getS_doi_id(), module, 0));

						//动态的附件
						List<ModuleTempFile> fileList = doing.getFileList();
						if(fileList != null){
							for(ModuleTempFile mtf : fileList) {
								if(!"url".equals(mtf.getMtf_file_type())){
									mtf.setMtf_url(	urlPath + "/" + FileUtils.SNS_TEMP_DIR + "/" + mtf.getMtf_module() + "/" + mtf.getMtf_usr_id());
								}
							}
						}
					}
///////////////////try end

				}
			}
		} catch(Exception e){
			CommonLog.error(e.getMessage(),e);
		}
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
		ahtml.append("\" class=\"wzb-link04 mr3\" title=\"\">");
		ahtml.append(title);
		ahtml.append("</a>  ");
		return ahtml.toString();
	}

	/**
	 * 组合超链接(手机端使用)
	 * @param url
	 * @param title
	 * @return
	 */
	public String assemblyTitleMobile(String url, String title){
		StringBuffer ahtml = new StringBuffer();
		ahtml.append("<a href=\"javascript:clicked('../"+url+"',true);");
		ahtml.append("\" title=\"\">");
		ahtml.append(title);
		ahtml.append("</a>  ");
		return ahtml.toString();
	}
	/**
	 * 按类型组合对象超链接
	 * @param id
	 * @param type
	 * @return
	 */
	public String getTargetHtmlTitle(long id, String type) {
		String url = "";
		String title = "";
		if(SNS.MODULE_ARTICLE.equals(type)){
			Article article = articleService.get(id);
			if(article != null){
				if(article.getArt_status() == 1)
					url = ContextPath.getContextPath() + "/app/article?id=" + id;
				title = article.getArt_title();
			}
		} else if(SNS.MODULE_COURSE.equals(type)){
			AeItem aeItem = aeItemService.get(id);
			if(aeItem != null){
				url = ContextPath.getContextPath() + "/app/course/detail/" + id;
				title = aeItem.getItm_title();
			}
		} else if(SNS.MODULE_DOING.equals(type)){
			SnsDoing doing = get(id);
			if(doing != null) {
				url = ContextPath.getContextPath() + "/app/personal/personalDoing/0";
				title = doing.getS_doi_title();
			}
		} else if(SNS.MODULE_GROUP.equals(type)){
			SnsGroup group =  snsGroupService.get(id);
			if(group != null) {
				url = ContextPath.getContextPath() + "/app/group/groupDetail/" + id;
				title = group.getS_grp_title();
			}
		}
		return assemblyTitle(url, title);
	}

	/**
	 * 什么时间发表了什么类型的动态
	 *
	 * @param cur_lang
	 *            当前语言
	 * @param act
	 * @param date
	 * @return
	 */
	public String doAction(String curLang, SnsDoing doing, long isExam, long isComment) {
		String action = "";
		String act = doing.getS_doi_act();
		String module = doing.getS_doi_module();
		if(isComment > 0){
			action += LabelContent.get(curLang, "doing_action_reply") ;
			action += getActionType(curLang, SNS.MODULE_COMMENT,isExam);

		} else if("1".equals(doing.getS_doi_target_type())){
			if(SNS.DOING_ACTION_LIKE.equals(act)){
				action += LabelContent.get(curLang, "doing_action_like");
				if(doing.getS_doi_act_id() > 0 && !SNS.MODULE_DOING.equals(doing.getS_doi_module()) ){
					action += LabelContent.get(curLang, "sns_lowercase_comment");
				} else {
					action += getActionType(curLang, module,isExam);
				}
			} else {
				action += LabelContent.get(curLang, "doing_action_reply") ;
				if(SNS.MODULE_DOING.equals(module)){
					action += getActionType(curLang, module,isExam);
				}
			}
		} else
		if (SNS.DOING_ACTION_DOING.equals(act)) {
			if(SNS.MODULE_GROUP.equals(module)){
				action += LabelContent.get(curLang, "doing_publish") + LabelContent.get(curLang, "group_lowercase_speech");
			} else {
				action += LabelContent.get(curLang, "doing_action");
			}
		} else if (SNS.DOING_ACTION_SHARE.equals(act)) {
			action += LabelContent.get(curLang, "doing_action_share") + getActionType(curLang, module,isExam);
		} else if (SNS.DOING_ACTION_LIKE.equals(act)) {
			//如果是通知，则显示赞了你的动态
			if(SNS.MODULE_NOTICE.equals(module)){
				action += LabelContent.get(curLang, "doing_action_like") + LabelContent.get(curLang, "doing_your_doing");
			} else if (SNS.MODULE_KNOWLEDGE.equals(module)) {
				action += LabelContent.get(curLang, "doing_action_like");
				action += " " + LabelContent.get(curLang, "lab_kb_item");
			} else {
				action += LabelContent.get(curLang, "doing_action_like") + getActionType(curLang, module,isExam);
			}
		} else if (SNS.DOING_ACTION_COMMENT.equals(act)) {
			if(SNS.MODULE_NOTICE.equals(module)){
				//如果是通知，则显示回复了，否则显示评价了
				action += LabelContent.get(curLang, "doing_action_reply") ;
			} else {
				action += LabelContent.get(curLang, "doing_action_comment") + getActionType(curLang, module,isExam);
			}
		} else if (SNS.DOING_ACTION_GROUP_CREATE.equals(act)) {
			action += LabelContent.get(curLang, "doing_action_create_group");
		} else if (SNS.DOING_ACTION_GROUP.equals(act)) {
			action += LabelContent.get(curLang, "doing_action_group_doing");
		} else if (SNS.DOING_ACTION_GROUP_DISSMISS.equals(act)) {
			action += LabelContent.get(curLang, "doing_action_dissmiss_group");
		} else if (SNS.DOING_ACTION_GROUP_APP.equals(act)) {
			action += LabelContent.get(curLang, "doing_action_app_group");
		} else if (SNS.DOING_ACTION_GROUP_OPEN.equals(act)) {
			action += LabelContent.get(curLang, "doing_action_open_group");
		} else if (SNS.DOING_ACTION_QUESTION_ADD.equals(act)) {
			action += LabelContent.get(curLang, "doing_action_add_question");
		} else if (SNS.DOING_ACTION_ANSWER_ADD.equals(act)) {
			action += LabelContent.get(curLang, "doing_action_add_answer");
		} else if(SNS.DOING_ACTION_ENROLL_COS.equals(doing.getS_doi_act())){
			action += LabelContent.get(curLang, "doing_action_enroll_cos");
			if(isExam == 1) {
				action += LabelContent.get(curLang, "doing_lowercase_exam");
			} else {
				action += LabelContent.get(curLang, "doing_lowercase_course");
			}
		} else if(SNS.DOING_ACTION_COMPLETED_COS.equals(doing.getS_doi_act())) {
			action += LabelContent.get(curLang, "doing_action_completed_cos");
			if(isExam == 1) {
				action += " " + LabelContent.get(curLang, "doing_lowercase_exam");
			} else {
				action += " " +LabelContent.get(curLang, "doing_lowercase_course");
			}
		}
		return action;
	}
	/**
	 * 获取动态类型
	 * @param cur_lang
	 * @param module
	 * @return
	 */
	public String getActionType(String cur_lang, String module,long isExam){
		String actionType = "";
		if(SNS.MODULE_COURSE.equals(module)){
			if(isExam == 1) {
			    actionType = LabelContent.get(cur_lang, "doing_lowercase_exam");
			}else{
				actionType = LabelContent.get(cur_lang, "doing_lowercase_course");
			}
		} else if(SNS.MODULE_ARTICLE.equals(module)){
			actionType = LabelContent.get(cur_lang, "doing_lowercase_article");
		} else if(SNS.MODULE_KNOWLEDGE.equals(module)){
			actionType = LabelContent.get(cur_lang, "doing_lowercase_knowledge");
		} else if(SNS.MODULE_DOING.equals(module)){
			actionType = LabelContent.get(cur_lang, "doing_lowercase_title");
		} else if(SNS.MODULE_QUESTION.equals(module) || SNS.MODULE_ANSWER.equals(module)){
			actionType = LabelContent.get(cur_lang, "personal_know");
		} else if(SNS.MODULE_GROUP.equals(module)){
			actionType = LabelContent.get(cur_lang, "doing_lowercase_group");
		}
		return actionType;
	}

	/**
	 * 发布动态
	 *
	 * @param targetId
	 *            目标对象
	 * @param uId
	 *            动作的人
	 * @param action
	 *            动作 【分享，赞，动态，评论，群组】
	 * @param module
	 *            模块对象 【文章，课程】
	 * @param text
	 *            内容或标题
	 */
	public void add(long targetId, int isNotice, long uId, long operator, String action, long actId, String module, String text, long replyId){
		//this.publish(null, targetId, isNotice, uId, operator, action, actId, module, text, replyId);
	}

	/**
	 * 发布动态
	 *
	 * @param targetId
	 *            目标对象
	 * @param uId
	 *            动作的人
	 * @param action
	 *            动作 【分享，赞，动态，评论，群组】
	 * @param module
	 *            模块对象 【文章，课程】
	 * @param text
	 *            内容或标题
	 */
	public void publish(Model model, long targetId, int isNotice, long uId, long operator, String action, long actId, String module, String text, long replyId) {
		SnsDoing doing = new SnsDoing();

		String title = text;
		if(title == null || "".equals(title)){
			if(SNS.MODULE_ARTICLE.equals(module)){//文章
				Article article = articleService.get(targetId);
				if(article != null){
					title = article.getArt_title();
				}
			} else if(SNS.MODULE_COURSE.equals(module)){//课程
				AeItem item = aeItemService.get(targetId);
				if(item != null) {
					title = item.getItm_title();
				}
			} else if(SNS.MODULE_KNOWLEDGE.equals(module)){//知识中心
				KbItem kbItem = kbItemService.get(targetId);
				if(kbItem != null) {
					title = kbItem.getKbi_title();
				}
			}
		}

		doing.setS_doi_target_id(targetId);
		doing.setS_doi_uid(uId);
		doing.setS_doi_act(action);
		doing.setS_doi_module(module);
		doing.setS_doi_title(title);
		doing.setS_doi_url("");
		doing.setS_doi_act_id(actId);
		doing.setS_doi_reply_id(replyId);
		doing.setS_doi_target_type(isNotice+"");
		doing.setS_doi_operator_uid(operator);

		doing.setS_doi_create_datetime(getDate());

		snsDoingMapper.insert(doing);

		if(SNS.DOING_ACTION_DOING.equals(action) || SNS.DOING_ACTION_GROUP.equals(action)){
			//更新附件的id
			moduleTempFileService.setMaster(doing.getS_doi_id(), uId, module);
		}
		if(model != null){
			model.addAttribute(RequestStatus.STATUS, RequestStatus.SUCCESS);
		}
	}

	/**
	 * 获取个人动态列表
	 *
	 * @param type
	 * @param usr_ent_id
	 *            用户id
	 * @return
	 */
	public Page<SnsDoing> getPersonalDoingList(Page<SnsDoing> page, long usr_ent_id, String type, String cur_lang) {
		SnsDoing snsDoing = new SnsDoing();
		snsDoing.setS_doi_uid(usr_ent_id);
		snsDoing.setS_doi_act(type);
		page.getParams().put("snsDoing", snsDoing);
		snsDoingMapper.selectPersonalDoingList(page);
		return page;
	}

	/**
	 * 获取跟用户相关的动态
	 * @param userEntId
	 * @param page
	 * @param wizbini
	 * @param uid
	 * @param tcrId
	 * @param cur_lang
	 * @return
	 */
	public Page<SnsDoing> listUserAll(long targetUid, Page<SnsDoing> page,WizbiniLoader wizbini,
			long uid, long tcrId, String cur_lang, String module, long targetId) {
		page.getParams().put("userEntId", uid);
		page.getParams().put("module", module);
		page.getParams().put("tcrId", tcrId);
		page.getParams().put("targetId", targetId);
		page.getParams().put("targetUid", targetUid);
		if(SNS.MODULE_GROUP.equals(module)){
			//群组言论
			snsDoingMapper.listGroupDoing(page);
		} else {
			snsDoingMapper.listUserAll(page);
		}
		//此处加入手机端的判断及处理
		if(page.getParams().get("isMobile") != null && page.getParams().get("isMobile").equals("true")){
			adaptorDoing(page.getResults(), wizbini, targetUid, cur_lang, module);
		}else{
			adaptorDoing(page.getResults(), wizbini, targetUid, cur_lang, module, false);
		}
		return page;
	}

	public void delete(long id, long userEntId, String module, String cur_lan) throws MessageException{
		SnsDoing doing = get(id);
		if(doing == null) { throw new MessageException(LabelContent.get(cur_lan, "error_data_not_found"));}

		super.delete(id);

		//获取需要删除评论所有子级评论
		List<SnsComment> sub_comment=new ArrayList<SnsComment>();
		snsCommentService.getCommentAllReply(doing.getS_doi_id(),sub_comment);
		//删除所有子级评论的所有的赞，赞的记录，动态，统计信息
		for(SnsComment ment:sub_comment){
			//删除评论所有的赞记录
			snsValuationService.delValuation(ment.getS_cmt_module(),ment.getS_cmt_id(),1);
			//删除评论所有赞记录日志
			snsValuationLogService.deleteList(ment.getS_cmt_id(), ment.getS_cmt_module(),1);
			//删除评论赞统计信息
			snsCountService.deleteRecord(ment.getS_cmt_id(), ment.getS_cmt_module(), 1);
			//删除评论所有的动态
			//deleteDoingByModule(ment.getS_cmt_id(), "COMMENT");
			//删除评论
			snsCommentService.delete(ment.getS_cmt_id());
		}
		
		//snsValuationLogService.deleteList(doing.getS_doi_id(), doing.getS_doi_module());
		//TODO删除关联的评论
		//snsCommentService.deleteList(doing.getS_doi_id(), doing.getS_doi_module());
		//TODO删除统计信息
		//snsCountService.deleteRecord(doing.getS_doi_id(), doing.getS_doi_module());

		moduleTempFileService.deleteList(userEntId, module, id);
	}

	/**
	 * 封装其他的数据(mobile端使用)
	 * @param list
	 * @param wizbini
	 * @param userEntId
	 * @param cur_lang
	 */
	public void adaptorDoing(List<SnsDoing> list, WizbiniLoader wizbini, long userEntId, String cur_lang, String module){
		if(StringUtils.isEmpty(module)) {
			module = SNS.MODULE_DOING;
		}
		String urlPath = "/"+ wizbini.cfgSysSetupadv.getFileUpload().getEditorDir().getUrl();
		try{
			if(list != null && list.size() > 0){
				for(SnsDoing doing : list){
					long isExam = 0;
					long isComment = 0;
					try{
						if( (SNS.MODULE_COURSE.equals(doing.getS_doi_module()) || SNS.MODULE_KNOWLEDGE.equals(doing.getS_doi_module())
							|| SNS.MODULE_ARTICLE.equals(doing.getS_doi_module())) && StringUtils.isNotEmpty(doing.getS_doi_title())){
							//课程和文章
							String url = "";
							StringBuffer title = new StringBuffer();
							String objTitle = "";
							//文章
							if(SNS.MODULE_ARTICLE.equals(doing.getS_doi_module())) {
								Article obj = articleService.get(doing.getS_doi_target_id());
								if(obj != null) {
									objTitle = obj.getArt_title();
									if(obj.getArt_status() == 1)
										url = "article/articleDetail.html?article=" + doing.getS_doi_target_id();
								}
							} else if(SNS.MODULE_KNOWLEDGE.equals(doing.getS_doi_module())){
								KbItem kbitem = kbItemService.get(doing.getS_doi_target_id());
								if(kbitem != null) {
									objTitle = kbitem.getKbi_title();
									url = "knowledge/detail.html?kbiId=" + doing.getS_doi_target_id()+"&kbi_type="+kbitem.getKbi_type();
								} else {
									objTitle = doing.getS_doi_title();
									url = "knowledge/detail.html?kbiId=" + doing.getS_doi_target_id();
								}
							} else {
								//课程
								AeItem item = aeItemService.get(doing.getS_doi_target_id());
								if(item !=  null){
									objTitle = item.getItm_title();
									if(item.getItm_status().equalsIgnoreCase("ON")){
										url = "course/detail.html?itmId=" + doing.getS_doi_target_id();
									}
									isExam = item.getItm_exam_ind();
								}
							}
							//组合超链接
							if(!url.equalsIgnoreCase("")){
								title.append(assemblyTitleMobile(url, objTitle) + "<br/>");
							} else {
								title.append(objTitle + "(" +  LabelContent.get(cur_lang, "global_unpublished") + ")<br/>");
							}
							//分享
							if(SNS.DOING_ACTION_SHARE.equals(doing.getS_doi_act())
									&& doing.getS_doi_act_id() > 0){
								SnsShare share = snsShareService.get(doing.getS_doi_act_id());
								if(share != null){
									title.append(share.getS_sha_title()==null? "" : share.getS_sha_title());
								}
							}
							//评论
							if(SNS.DOING_ACTION_COMMENT.equals(doing.getS_doi_act())
									&& doing.getS_doi_act_id() > 0){
								SnsComment cmt = snsCommentService.get(doing.getS_doi_reply_id());
								if (cmt != null) {
									title.append(cmt.getS_cmt_content() == null ? "" : cmt.getS_cmt_content());
								}
								SnsComment comment = snsCommentService.get(doing.getS_doi_act_id());
								if (comment != null) {
									if(cmt != null )
										title.append("<div class=replydesc>");
									title.append(comment.getS_cmt_content() == null ? "" : comment.getS_cmt_content());
									if(cmt != null )
										title.append("</div>");
								}

							}
							//赞课程
							if(SNS.DOING_ACTION_LIKE.equals(doing.getS_doi_act())
									&& doing.getS_doi_act_id() > 0)
							{
								SnsComment comment = snsCommentService.get(doing.getS_doi_act_id());
								if(comment != null) {
									title.insert(0,comment.getS_cmt_content() == null ? "" : comment.getS_cmt_content());
								}

							}
							doing.setS_doi_title(title.toString());
						} else if(SNS.MODULE_GROUP.equals(doing.getS_doi_module()) && !SNS.DOING_ACTION_GROUP.equals(doing.getS_doi_act())){
							//群组
							SnsGroup group = snsGroupService.get(doing.getS_doi_target_id());
							String url = "group/detail.html?groupId=" + doing.getS_doi_target_id();
							StringBuffer title = new StringBuffer();
							if(group != null){
								if(group.getS_grp_status().equalsIgnoreCase("OK")){
									title.append(assemblyTitleMobile(url, group.getS_grp_title()));
								} else {
									title.append(group.getS_grp_title() + LabelContent.get(cur_lang, "group_dissolved"));
								}
							}
							doing.setS_doi_title(title.toString());
						} else if(SNS.MODULE_QUESTION.equals(doing.getS_doi_module())) {
							//问答
							KnowQuestion question = knowQuestionService.get(doing.getS_doi_target_id());
							if(question != null){
								String url = "know/detail.html?type=" + question.getQue_type() + "&id=" + doing.getS_doi_target_id();
								doing.setS_doi_title(assemblyTitleMobile(url, question.getQue_title()));
							}
						} else if(SNS.MODULE_ANSWER.equals(doing.getS_doi_module())){
							//问答，如果是新增问题，则act_id 为问题的id，否则是回答的id
							StringBuffer title = new StringBuffer();
							Knowanswer answer = knowanswerService.get(doing.getS_doi_target_id());
							KnowQuestion question = null;
							if(SNS.DOING_ACTION_ANSWER_ADD.equals(doing.getS_doi_act())){
								question = knowQuestionService.get(doing.getS_doi_target_id());
								if(question != null) {
									String url = "know/detail.html?type=" + question.getQue_type() + "&id=" + question.getQue_id();
									title.append(assemblyTitleMobile(url, question.getQue_title()));
								}
							} else {
								question = knowQuestionService.get(answer.getAns_que_id());
								if(answer != null  && question != null){
									title.append(answer.getAns_content());
									String url = "know/detail.html?type=" + question.getQue_type() + "&id=" + question.getQue_id();
									title.append("<br/>");
									title.append(assemblyTitleMobile(url, question.getQue_title()));
								}
							}
							doing.setS_doi_title(title.toString());
						} else if(SNS.MODULE_DOING.equals(doing.getS_doi_module())) {
							StringBuffer title = new StringBuffer();
							//如果是动态或者是通知
							//动作赞
							if(SNS.DOING_ACTION_LIKE.equals(doing.getS_doi_act())){
								SnsDoing sdoing = get(doing.getS_doi_target_id());
								if(sdoing != null){
									if( sdoing.getS_doi_act_id() != null && sdoing.getS_doi_act_id() > 0) {
										if(SNS.DOING_ACTION_SHARE.equals(sdoing.getS_doi_act())){
											title.append(sdoing.getS_doi_title());
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
								doing.setS_doi_title(title.toString());
								//动作评论
							} else if(SNS.DOING_ACTION_COMMENT.equals(doing.getS_doi_act())){
								SnsDoing sdoing = get(doing.getS_doi_target_id());
								SnsComment targetComment = snsCommentService.get(doing.getS_doi_act_id());
								if(targetComment != null){
									//下滑线
									title.append("<div class=replydesc>");
									title.append(targetComment.getS_cmt_content() == null ? "" : targetComment.getS_cmt_content());
									title.append("</div>");
								}
								//回复二级的话
								if(targetComment != null && targetComment.getS_cmt_reply_to_id() != null && targetComment.getS_cmt_reply_to_id() > 0
										&& targetComment.getS_cmt_reply_to_uid() != null && targetComment.getS_cmt_reply_to_uid() >0){
									SnsComment tc = snsCommentService.get(targetComment.getS_cmt_reply_to_id());
									if(tc != null){
										title.insert(0, tc.getS_cmt_content() == null ? "" : tc.getS_cmt_content() );
									}
									isComment = 1;	//标记是评论
								} else {
									//回复二级评论产生的通知
									if(sdoing != null && sdoing.getS_doi_act_id() != null && sdoing.getS_doi_act_id() > 0) {
										if(SNS.DOING_ACTION_SHARE.equals(sdoing.getS_doi_act())){
											SnsShare share = snsShareService.get(sdoing.getS_doi_act_id());
											if(share != null){
												title.insert(0, share.getS_sha_title() == null ? "" : share.getS_sha_title());
											}
										}
										else
											if(SNS.DOING_ACTION_COMMENT.equals(doing.getS_doi_act())){
												SnsComment sc = snsCommentService.get(sdoing.getS_doi_act_id());
												//拿回复的内容
												if(sc!=null)
													title.insert(0, sc.getS_cmt_content() == null ? "" : sc.getS_cmt_content());
											}
									} else {
										if(SNS.DOING_ACTION_COMMENT.equals(doing.getS_doi_act())){
											//拿回复的内容
											if(sdoing!=null)
												title.insert(0,sdoing.getS_doi_title() == null ? "" : sdoing.getS_doi_title());
										}
									}
								}
								doing.setS_doi_title(title.toString());
							}
						} else if("1".equals(doing.getS_doi_target_type())){
							StringBuffer title = new StringBuffer();
							//如果是动态或者是通知
							//动作赞
							if(SNS.DOING_ACTION_LIKE.equals(doing.getS_doi_act())){
								SnsComment comment = snsCommentService.get(doing.getS_doi_act_id());
								if(comment != null) {
									title.append(comment.getS_cmt_content());
								}
								doing.setS_doi_title(title.toString());
								//动作评论
							} else if(SNS.DOING_ACTION_COMMENT.equals(doing.getS_doi_act())){
								SnsComment comment = snsCommentService.get(doing.getS_doi_target_id());
								//拿回复的内容
								if(comment != null){
									title.append(comment.getS_cmt_content() == null ? "" : comment.getS_cmt_content());
								}
								//TODO action
								//本条通知回复的内容
								SnsComment targetComment = snsCommentService.get(doing.getS_doi_act_id());
								if(targetComment != null){
									//下滑线
									title.append("<div class=replydesc>");
									title.append(targetComment.getS_cmt_content() == null ? "" : targetComment.getS_cmt_content());
									title.append("</div>");
								}
								doing.setS_doi_title(title.toString());
							}
						} else {
							if(StringUtils.isNotEmpty(doing.getS_doi_title())){
								doing.setS_doi_title(doing.getS_doi_title().replaceAll("course_detail\\?itm_id=", "detail/"));
							}
						}
						//动态的动作
						doing.setS_doi_act_str(doAction(cur_lang, doing, isExam, isComment));
						doing.setS_doi_title(doing.getS_doi_act_str()+"："+doing.getS_doi_title());
						//用户头像
						if (doing.getUser() != null) {
							ImageUtil.combineImagePath(doing.getUser());
						}
						if(doing.getOperator() != null){
							ImageUtil.combineImagePath(doing.getOperator());
						}
						//二级评论，如有性能问题，可以考虑异步获取
						List<SnsComment> replies = snsCommentService.getTargetCommnet(doing.getS_doi_id(), userEntId, module);
						for(SnsComment c : replies) {
							if (c.getUser() != null) {
								ImageUtil.combineImagePath(c.getUser());;
							}
							//二级回复的数量
							c.setSnsCount(snsCountService.getByTargetInfo(c.getS_cmt_id(), module, 1));
						}
						doing.setReplies(replies);
						//一级评论的数量
						doing.setSnsCount(snsCountService.getByTargetInfo(doing.getS_doi_id(), module, 0));
						//动态的附件
						List<ModuleTempFile> fileList = doing.getFileList();
						if(fileList != null){
							for(ModuleTempFile mtf : fileList) {
								if(!"url".equals(mtf.getMtf_file_type())){
									mtf.setMtf_url(	urlPath + "/" + FileUtils.SNS_TEMP_DIR + "/" + mtf.getMtf_module() + "/" + mtf.getMtf_usr_id());
								}
							}
						}
					}catch(Exception e){
						CommonLog.error(e.getMessage(),e);
					}
				}
			}
		} catch(Exception e){
			CommonLog.error(e.getMessage(),e);
		}
	}

	/**
	 * 根据targetId删除动态
	 * @param targetId 目标id
	 * @param module 模块类型
	 */
	public void deleteDoingByModule(long targetId, String module){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("targetId", targetId);
		map.put("module", module);
		snsDoingMapper.deleteDoingByModule(map);
	}

	
	/**
	 * 获取群组下的评论
	 * @param targetId
	 * @param userEntId
	 * @return
	 */
	public List<SnsDoing> getAllGroupDoingList(long targetId, long userEntId){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("module", SNS.MODULE_GROUP);
		map.put("targetId", targetId);
		map.put("userEntId", userEntId);
		
		List<SnsDoing> list = snsDoingMapper.getAllGroupDoingList(map);
		return list;
	}
	
	
	
}