package com.cwn.wizbank.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.cw.wizbank.JsonMod.credit.Credit;
import com.cwn.wizbank.common.SNS;
import com.cwn.wizbank.entity.Knowanswer;
import com.cwn.wizbank.entity.SnsComment;
import com.cwn.wizbank.entity.SnsDoing;
import com.cwn.wizbank.entity.SnsValuation;
import com.cwn.wizbank.entity.SnsValuationLog;
import com.cwn.wizbank.persistence.AeApplicationMapper;
import com.cwn.wizbank.persistence.SnsValuationLogMapper;
import com.cwn.wizbank.persistence.SnsValuationMapper;
import com.cwn.wizbank.utils.RequestStatus;

/**
 * 赞 service 实现
 */
@Service
public class SnsValuationService extends BaseService<SnsValuation> {

	@Autowired
	SnsValuationMapper snsValuationMapper;

	@Autowired
	SnsValuationLogMapper snsValuationLogMapper;

	@Autowired
	SnsCountService snsCountService;

	@Autowired
	SnsValuationLogService snsValuationLogService;

	@Autowired
	AeApplicationMapper aeApplicationMapper;

	@Autowired
	SnsDoingService snsDoingService;

	@Autowired
	SnsCommentService snsCommentService;

	@Autowired
	KnowanswerService  knowanswerService;

	@Autowired
	ForCallOldAPIService forCallOldAPIService;

	public int getCount (long targetId, long usrEntId, String module){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("targetId", targetId);
		map.put("usrEntId", usrEntId);
		map.put("module", module);
		// 如果已赞过
		return snsValuationMapper.getCount(map);
	}
	/**
	 * 保存赞，并且更新统计信息
	 *
	 * @param targetId
	 * @param usrEntId
	 * @param module
	 * @param type
	 * @param score
	 * @return
	 */
	public long add(long targetId, long usrEntId, String module, String type,
			long score, int isComment) {

		// 不符合的操作
/*		if (!SNS.MODULE_COURSE.equals(module)
				&& !SNS.MODULE_DOING.equals(module)
				&& !SNS.MODULE_GROUP.equals(module)
				&& !SNS.MODULE_ARTICLE.equals(module))
			return RequestStatus.ERROR;*/

		// 保存明细
		SnsValuationLog valuationLog = new SnsValuationLog();
		valuationLog.setS_vtl_create_datetime(getDate());
		valuationLog.setS_vtl_module(module);
		valuationLog.setS_vtl_score(score);
		valuationLog.setS_vtl_target_id(targetId);
		valuationLog.setS_vtl_type(type);
		valuationLog.setS_vtl_uid(usrEntId);
		valuationLog.setS_vtl_is_comment(isComment);
		snsValuationLogMapper.add(valuationLog);

		SnsValuation valuation = new SnsValuation();
		valuation.setS_vlt_module(module);
		valuation.setS_vlt_score(score);
		valuation.setS_vlt_target_id(targetId);
		valuation.setS_vlt_type(type);
		valuation.setS_vlt_is_comment(isComment);

		if (getCount(targetId, usrEntId, module) > 0) {
			// 针对目标 更新统计信息
			snsValuationMapper.updateScore(valuation);
		} else {
			// 插入统计信息
			snsValuationMapper.insert(valuation);
		}

		return valuationLog.getS_vtl_log_id();
	}

	/**
	 * 赞对象
	 *
	 * @param targetId
	 *            赞的目标
	 * @param uId
	 *            操作的人呢
	 * @param module
	 *            模块
	 * @throws Exception
	 * @see module SNS 取SNS.java中的module值
	 */
	public void praise(Model model, long targetId, long userEntId, String module, int isComment, long tkhId, String curLang, String usr_id) throws Exception {


		if(snsValuationLogService.getCount(targetId, userEntId, module, isComment) > 0){
			model.addAttribute(RequestStatus.STATUS, RequestStatus.EXISTS);
			//已经赞过
			return;
		}
		add(targetId, userEntId, module, SNS.DOING_ACTION_LIKE, 1l, isComment);
		// 更新统计信息
		long count = snsCountService.updateLikeCount(targetId, module, isComment, userEntId, true);
		model.addAttribute("likeCount", count);

		if(isComment == 1) {
			SnsComment comment = snsCommentService.get(targetId);
			if (comment != null){
				// 产生通知
				//snsDoingService.add(comment.getS_cmt_id(), 1, comment.getS_cmt_uid(), userEntId, SNS.DOING_ACTION_LIKE, comment.getS_cmt_id(), SNS.MODULE_COMMENT, "", 0);
				forCallOldAPIService.updUserCredits(null, Credit.SYS_GET_LIKE, comment.getS_cmt_uid(), usr_id, targetId, 0);
				forCallOldAPIService.updUserCredits(null, Credit.SYS_CLICK_LIKE, userEntId, usr_id, targetId, 0);
			}
		} else if(SNS.MODULE_DOING.equals(module) || SNS.MODULE_GROUP.equals(module)){
			//赞动态产生动态
			SnsDoing doing	= snsDoingService.get(targetId);
			if (doing != null){
				if(SNS.MODULE_DOING.equals(module)){
					// 产生通知
					Long tagId = doing.getS_doi_act_id();
					if(tagId == null) tagId = targetId;
					//snsDoingService.add(targetId, 1, doing.getS_doi_uid(), userEntId, SNS.DOING_ACTION_LIKE, tagId, module, "", 0);
				}
				forCallOldAPIService.updUserCredits(null, Credit.SYS_GET_LIKE, doing.getS_doi_uid(), usr_id, targetId, 0);
				forCallOldAPIService.updUserCredits(null, Credit.SYS_CLICK_LIKE, userEntId, usr_id, targetId, 0);
			}
		} else if(!SNS.MODULE_GROUP.equals(module)){
			//snsDoingService.add(targetId, 0, userEntId, 0, SNS.DOING_ACTION_LIKE, 0, module, "", 0);
		}

		if(SNS.MODULE_ANSWER.equals(module)){
			Knowanswer knowAnswer = knowanswerService.get(targetId);
			forCallOldAPIService.updUserCredits(null, Credit.SYS_GET_LIKE, knowAnswer.getAns_create_ent_id(), usr_id, targetId, 0);
			forCallOldAPIService.updUserCredits(null, Credit.SYS_CLICK_LIKE, userEntId, usr_id, targetId, 0);
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("targetId", targetId);
		map.put("module", module);
		model.addAttribute("count", snsValuationMapper.getCount(map));

		model.addAttribute(RequestStatus.STATUS, RequestStatus.SUCCESS);
	}

	public void cancel(Model model, long targetId, String module, long userEntId, int isComment) throws Exception {
		// 删除赞的信息
		SnsValuationLog log = snsValuationLogService.getByUserId(targetId, userEntId, module, isComment);
		if (log == null)
			return;
		snsValuationLogMapper.delete(log.getS_vtl_log_id());

		// 更新统计信息
		SnsValuation valuation = new SnsValuation();
		valuation.setS_vlt_module(log.getS_vtl_module());
		valuation.setS_vlt_target_id(log.getS_vtl_target_id());
		valuation.setS_vlt_type(log.getS_vtl_type());
		valuation.setS_vlt_is_comment(isComment);
		snsValuationMapper.updateScore(valuation);

		// 更新统计信息
		long count = snsCountService.updateLikeCount(
				log.getS_vtl_target_id(), log.getS_vtl_module(), isComment, userEntId, false);
		model.addAttribute("count", count);

		model.addAttribute(RequestStatus.STATUS, RequestStatus.SUCCESS);
	}

	public void setSnsValuationMapper(SnsValuationMapper snsValuationMapper) {
		this.snsValuationMapper = snsValuationMapper;
	}

	public void delValuation(String module,long target_id,int is_comment){
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("s_vlt_module", module);
		params.put("s_vlt_target_id", target_id);
		params.put("s_vlt_is_comment", is_comment);
		snsValuationMapper.delValuation(params);

	}

}