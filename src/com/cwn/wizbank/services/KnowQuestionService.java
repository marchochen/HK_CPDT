package com.cwn.wizbank.services;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import com.cw.wizbank.JsonMod.credit.Credit;
import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.common.SNS;
import com.cwn.wizbank.entity.AcRole;
import com.cwn.wizbank.entity.KnowCatalog;
import com.cwn.wizbank.entity.KnowCatalogRelation;
import com.cwn.wizbank.entity.KnowQuestion;
import com.cwn.wizbank.entity.Knowanswer;
import com.cwn.wizbank.entity.MessageTemplate;
import com.cwn.wizbank.entity.RegUser;
import com.cwn.wizbank.exception.BeanAssignException;
import com.cwn.wizbank.exception.DataNotFoundException;
import com.cwn.wizbank.persistence.KnowQuestionMapper;
import com.cwn.wizbank.utils.BeanUtils;
import com.cwn.wizbank.utils.ContextPath;
import com.cwn.wizbank.utils.Page;

/**
 *  service 实现
 */
@Service
public class KnowQuestionService extends BaseService<KnowQuestion> {

	@Autowired
	KnowQuestionMapper knowQuestionMapper;

	@Autowired
	KnowCatalogRelationService knowCatalogRelationService;

	@Autowired
	SnsDoingService snsDoingService;

	@Autowired
	KnowCatalogService knowCatalogService;

	@Autowired
	ForCallOldAPIService forCallOldAPIService;

	@Autowired
	KnowanswerService knowanswerService;

	@Autowired
	WebMessageService webMessageService;

	@Autowired
	RegUserService regUserService;
	
	@Autowired
	MessageTemplateService messageTemplateService;
	
	@Autowired
	ModuleTempFileService moduleTempFileService;

	public void setKnowQuestionMapper(KnowQuestionMapper knowQuestionMapper){
		this.knowQuestionMapper = knowQuestionMapper;
	}

	/**
	 * 获取问题列表
	 * @param page
	 * @param queType 问题类型
	 * @param kcaId 问题分类id
	 * @param command
	 * @param usr_ent_id 用户id
	 * @param kcrAncestorKcaId 父分类id
	 * @param searchContent 搜索内容
	 * @param tcrId 培训中心id
	 * @return
	 */
	public Page<KnowQuestion> getKnowQuestionList(Page<KnowQuestion> page, String queType, long kcaId, String command,
				long usr_ent_id, long queId, long kcrAncestorKcaId, String searchContent, long tcrId){
		KnowQuestion knowQuestion = new KnowQuestion();
		if(command.equalsIgnoreCase("my_question")){
			knowQuestion.setQue_create_ent_id(usr_ent_id);
		} else if(command.equalsIgnoreCase("my_answer")){
			Knowanswer knowAnswer = new Knowanswer();
			knowAnswer.setAns_create_ent_id(usr_ent_id);
			knowQuestion.setKnowAnswer(knowAnswer);
		}else if(command.equalsIgnoreCase("my_know_help")){
			knowQuestion.setQue_ask_ent_ids(String.valueOf(usr_ent_id));
		} else if(StringUtils.isNotEmpty(queType)){
			knowQuestion.setQue_type(queType);
		}
		if(queId > 0){
			knowQuestion.setQue_id(queId);
		}
		if(kcaId > 0 || kcaId == -1){
			KnowCatalog knowCatalog = new KnowCatalog();
			knowCatalog.setKca_id(kcaId);
			knowQuestion.setKnowCatalog(knowCatalog);
		}
		if(kcrAncestorKcaId > 0){
			KnowCatalogRelation knowCatalogRelation = new KnowCatalogRelation();
			knowCatalogRelation.setKcr_ancestor_kca_id(kcrAncestorKcaId);
			knowQuestion.setKnowCatalogRelation(knowCatalogRelation);
		}
		if(!searchContent.equalsIgnoreCase("")){
			knowQuestion.setSearchContent(searchContent);
		}
		if(tcrId > 0){
			KnowCatalog knowCatalog = new KnowCatalog();
			knowCatalog.setKca_tcr_id(tcrId);
			knowQuestion.setKnowCatalog(knowCatalog);
		}
		page.getParams().put("knowQuestion", knowQuestion);
		knowQuestionMapper.selectKnowQuestionList(page);
		return page;
	}

	/**
	 * 添加问题
	 * @param queTitle 问题标题
	 * @param queContent 问题补充
	 * @param userEntId 用户id
	 * @return
	 * @throws SQLException 
	 */
	public long insertKnowQuestion(String queTitle, String queContent, String queAskEntIds, long userEntId,double queBounty, String userName, loginProfile prof,KnowQuestion question) throws SQLException {
		KnowQuestion knowQuestion = new KnowQuestion();
		knowQuestion.setQue_title(queTitle);
		knowQuestion.setQue_popular_ind(0);
		knowQuestion.setQue_reward_credits(0);
		knowQuestion.setQue_status("OK");
		knowQuestion.setQue_create_timestamp(getDate());
		knowQuestion.setQue_create_ent_id(userEntId);
		knowQuestion.setQue_update_timestamp(getDate());
		knowQuestion.setQue_update_ent_id(userEntId);
		knowQuestion.setQue_content(queContent);
		if(question.getQue_type() != null && !question.getQue_type().equals("")){
			knowQuestion.setQue_type(question.getQue_type());
		}else{
			knowQuestion.setQue_type("UNSOLVED");
		}
		knowQuestion.setQue_bounty(queBounty);
		knowQuestion.setQue_ask_ent_ids(queAskEntIds);
		knowQuestionMapper.insert(knowQuestion);
		//站内信
		knowQuestion.setType_sys(true);//添加问题时，站内信type区别(true为sendMessage时设为SYS用)
		sendMessage(knowQuestion, userName, userEntId , prof);
		return knowQuestion.getQue_id();
	}

	/**
	 * 添加问题
	 * @param queTitle 问题标题
	 * @param queContent 问题补充
	 * @param kcaIdOne 一级分类id
	 * @param kcaIdTwo 二级分类id
	 * @param userEntId 用户id
	 * @param usrId
	 * @return
	 * @throws Exception 
	 */
	public long insertKnowQuestion(String queTitle, String queContent, String queAskEntIds, long kcaIdOne, long kcaIdTwo, long userEntId, String usrId, double queBounty, String userName, loginProfile prof,KnowQuestion question) throws Exception{
		if(StringUtils.isNotEmpty(queTitle)) {
			queTitle = queTitle.replaceAll("<", "&lt;");
		}
		if(StringUtils.isNotEmpty(queContent)) {
			queContent = queContent.replaceAll("<", "&lt;");
		}
		long que_id = insertKnowQuestion(queTitle, queContent, queAskEntIds, userEntId,queBounty, userName, prof,question);
		knowCatalogRelationService.insertKnowCatalogRelation(que_id, kcaIdOne, kcaIdTwo, usrId);
		
		
		//发动态
		//snsDoingService.add(que_id, 0, userEntId, 0, SNS.DOING_ACTION_QUESTION_ADD, 0, SNS.MODULE_QUESTION, "", 0);
		
		//更新附件的id
		moduleTempFileService.setMaster(que_id, userEntId, "KnowQuestion");
		
		return que_id;
	}


	/**
	 * 添加问题
	 * @param queTitle 问题标题
	 * @param queContent 问题补充
	 * @param kcaIdOne 一级分类id
	 * @param kcaIdTwo 二级分类id
	 * @param userEntId 用户id
	 * @param usrId
	 * @return
	 * @throws Exception
	 */
	public long  addQuestion(KnowQuestion question, long kcaIdOne, long kcaIdTwo, long userEntId, String usrId, String userName, loginProfile prof) throws Exception{
		long que_id = 0;
		if(question.getQue_bounty()==null || question.getQue_bounty().equals("")){
			que_id = this.insertKnowQuestion(question.getQue_title(), question.getQue_content(), question.getQue_ask_ent_ids(), kcaIdOne, kcaIdTwo, prof.usr_ent_id, prof.usr_id,0,prof.usr_display_bil, prof,question);
		}else{
			que_id = this.insertKnowQuestion(question.getQue_title(), question.getQue_content(), question.getQue_ask_ent_ids(), kcaIdOne, kcaIdTwo, prof.usr_ent_id, prof.usr_id,question.getQue_bounty(), prof.usr_display_bil, prof,question);			
		}
		
		/*if(StringUtils.isNotEmpty(question.getQue_title())) {
			question.setQue_title(question.getQue_title().replaceAll("<", "&lt;"));
		}
		if(StringUtils.isNotEmpty(question.getQue_content())) {
			question.setQue_content(question.getQue_content().replaceAll("<", "&lt;"));
		}
		
		//新增问题
		question.setQue_popular_ind(0);
		question.setQue_reward_credits(0);
		question.setQue_status("OK");
		question.setQue_create_timestamp(getDate());
		question.setQue_create_ent_id(userEntId);
		question.setQue_update_timestamp(getDate());
		question.setQue_update_ent_id(userEntId);
		knowQuestionMapper.insert(question);
		long queId = question.getQue_id();
		//关联关系
		knowCatalogRelationService.insertKnowCatalogRelation(queId, kcaIdOne, kcaIdTwo, usrId);

		//答案
		if(question.getKnowAnswer() != null && StringUtils.isNotEmpty(question.getKnowAnswer().getAns_content())) {
			int bestAnser = 0;
			if(KnowQuestion.TYPE_FAQ.equals(question.getQue_type())){
				bestAnser = 1;
			}
			knowanswerService.addAnswer(queId, userEntId, question.getKnowAnswer().getAns_content(), "", bestAnser);
		}*/
		
		//发动态
		//snsDoingService.add(queId, 0, userEntId, 0, SNS.DOING_ACTION_QUESTION_ADD, 0, SNS.MODULE_QUESTION, "", 0);

		if(kcaIdTwo > 0){
			//更新问题个数
			knowCatalogService.updateKcaQueCountByKcaId(kcaIdTwo, 1, usrId);
		}else{
			//更新问题个数
			knowCatalogService.updateKcaQueCountByKcaId(kcaIdOne, 1, usrId);
		}
		//积分
		forCallOldAPIService.updUserCredits(null, Credit.ZD_NEW_QUE, userEntId, usrId, 0, 0);

		//站内信
		//sendMessage(question, userName, userEntId, prof);
		
		return que_id;

	}

	/**
	 * 获取在线问答解决情况
	 * @param tcrId 培训中心id
	 * @return
	 */
	public List<KnowQuestion> getKnowSolveSituation(long tcrId, long usr_ent_id){
		KnowQuestion knowQuestion = new KnowQuestion();
		KnowCatalog knowCatalog = new KnowCatalog();
		knowCatalog.setKca_tcr_id(tcrId);
		knowQuestion.setQue_create_ent_id(usr_ent_id);
		knowQuestion.setKnowCatalog(knowCatalog);
		return knowQuestionMapper.selectKnowSolveSituation(knowQuestion);
	}

	/**
	 * 取消提问
	 * @param queId 问题id
	 * @return
	 */
	public void delKnowQuestion(long usr_ent_id, long queId){
		KnowQuestion knowQuestion = new KnowQuestion();
		knowQuestion.setQue_id(queId);
		knowQuestion.setQue_status("DELETED");
		knowQuestion.setQue_update_ent_id(usr_ent_id);
		knowQuestion.setQue_update_timestamp(getDate());
		knowQuestionMapper.update(knowQuestion);
	}

	/**
	 * 修改问题补充
	 * @param queId 问题id
	 * @param queContent 问题补充内容
	 * @return
	 */
	public void changeQueContent(long usr_ent_id, long queId, String queContent){
		if(StringUtils.isNotEmpty(queContent)) {
			queContent = queContent.replaceAll("<", "&lt;");
		}
		KnowQuestion knowQuestion = new KnowQuestion();
		knowQuestion.setQue_id(queId);
		knowQuestion.setQue_content(queContent);
		knowQuestion.setQue_update_ent_id(usr_ent_id);
		knowQuestion.setQue_update_timestamp(getDate());
		knowQuestionMapper.update(knowQuestion);
		
		//更新附件的id
		moduleTempFileService.setMaster(queId, usr_ent_id, "KnowQuestion");		
	}

	/**
	 * 计算评价率
	 * @param num 评价数
	 * @param total 总数
	 * @return
	 */
	public String CountRate(int num, int total){
		BigDecimal num1 = new BigDecimal(num);
		BigDecimal num2 = new BigDecimal(total);
		return num1.divide(num2, 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).intValue() + "%";
	}

	/**
	 * 计算评价率
	 * @param num 评价数
	 * @param total 总数
	 * @return
	 */
	public Integer getRate(int num, int total){
		BigDecimal num1 = new BigDecimal(num);
		BigDecimal num2 = new BigDecimal(total);
		return num1.divide(num2, 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).intValue();
	}
	/**
	 * 获取悬赏积分数
	 * @param queId 问题id
	 * @return
	 */
	public Double getQueBounty(long queId){
		return knowQuestionMapper.get(queId).getQue_bounty();
	}

	/**
	 * 获取问题解决数和未解决数
	 * @param tcrId 培训中心id
	 * @return
	 */
	public ModelAndView getKnowSolveTotal(ModelAndView mav, long tcrId){
		List<KnowQuestion> list = getKnowSolveSituation(tcrId,0);
		long solved_num = 0;
		long unsolved_num = 0;
		for(int i = 0; i < list.size(); i++){
			if(list.get(i).getQue_type().equalsIgnoreCase("SOLVED")){
				solved_num = list.get(i).getQue_num();
			} else if(list.get(i).getQue_type().equalsIgnoreCase("UNSOLVED")){
				unsolved_num = list.get(i).getQue_num();
			}
		}
		//已解决问题数
		mav.addObject("solved_num", solved_num);
		//未解决问题数
		mav.addObject("unsolved_num", unsolved_num);
		return mav;
	}
	/**
	 * 获取问题解决数和未解决数
	 * @param tcrId 培训中心id
	 * @return
	 */
	public Model getKnowSolveTotal(Model model, long tcrId){
		List<KnowQuestion> list = getKnowSolveSituation(tcrId,0);
		long solved_num = 0;
		long unsolved_num = 0;
		for(int i = 0; i < list.size(); i++){
			if(list.get(i).getQue_type().equalsIgnoreCase("SOLVED")){
				solved_num = list.get(i).getQue_num();
			} else if(list.get(i).getQue_type().equalsIgnoreCase("UNSOLVED")){
				unsolved_num = list.get(i).getQue_num();
			}
		}
		//已解决问题数
		model.addAttribute("solvedNum", solved_num);
		//未解决问题数
		model.addAttribute("unsolvedNum", unsolved_num);

		return model;
	}


	public void saveOrUpdateQuestion(KnowQuestion question, int kcaIdOne,
			int kcaIdTwo, long userEntId, String usrId, String userName,loginProfile prof) throws Exception {
		if(question.getQue_id() != null && question.getQue_id() > 0) {
			this.updateQuestion(question, kcaIdOne, kcaIdTwo, userEntId, usrId, userName, prof);
		} else {
			long que_id = this.addQuestion(question, kcaIdOne, kcaIdTwo, userEntId, usrId, userName, prof);
			if(question.getQue_type()!=null&&question.getQue_type().equals("FAQ") &&question.getKnowAnswer() != null && question.getKnowAnswer().getAns_content() != null && !question.getKnowAnswer().getAns_content().equals("")){
				if(que_id != 0){
					knowanswerService.addKnowAnswer(que_id, prof.usr_ent_id, question.getKnowAnswer().getAns_content(), "",1);
				}
			}
		}
	}


	/**
	 * 移动端首页获取最近问题列表
	 * @param page
	 */
	public void getLatestQuestionList(Page<KnowQuestion>page) {
		this.knowQuestionMapper.getLatestQuestionList(page);
	}


	public void updateQuestion(KnowQuestion question, int kcaIdOne,
			int kcaIdTwo, long userEntId, String usrId, String userName, loginProfile prof) throws BeanAssignException, DataNotFoundException, Exception{
		Long queId = question.getQue_id();
		KnowQuestion dbQuestion = this.get(queId);
		if(dbQuestion == null) {
			throw new DataNotFoundException();
		}
		if(StringUtils.isNotEmpty(question.getQue_title())) {
			dbQuestion.setQue_title(question.getQue_title().replaceAll("<", "&lt;"));
		}
		if(StringUtils.isNotEmpty(question.getQue_content())) {
			dbQuestion.setQue_content(question.getQue_content().replaceAll("<", "&lt;"));
		}
		//先减掉原来分类的总数
		List<Long> list =  this.knowCatalogRelationService.getKnowCatalogRelationMapper().selectParents(question.getQue_id());
		if(list != null && list.size() > 0) {
			long kcaId0 = 0;
			long kcaId1 = 0;
			
			if(list.size() > 0 && list.size() == 1) {
				kcaId0 = list.get(0);
				knowCatalogService.updateKcaQueCountByKcaId(kcaId0, -1, usrId);
			}
			if(list.size() > 1) {
				kcaId1 = list.get(1);
				knowCatalogService.updateKcaQueCountByKcaId(kcaId1, -1, usrId);
			}
		}
		//关联关系, 删除原来的
		knowCatalogRelationService.deleteKnowCatalogRelation(queId);
		knowCatalogRelationService.insertKnowCatalogRelation(queId, kcaIdOne, kcaIdTwo, usrId);
		
		//更新问题个数
		if(kcaIdTwo > 0){
			knowCatalogService.updateKcaQueCountByKcaId(kcaIdTwo, 1, usrId);
		}else{
			knowCatalogService.updateKcaQueCountByKcaId(kcaIdOne, 1, usrId);
		}

		//站内信
		if(StringUtils.isNotEmpty(question.getQue_ask_ent_ids())) {
			String oldEntIds = dbQuestion.getQue_ask_ent_ids();
			List<String> olds = null;
			String[] entIds = question.getQue_ask_ent_ids().split(",");

			if(StringUtils.isNotEmpty(oldEntIds)){
				olds = Arrays.asList(oldEntIds.split(","));
			}
			for(String toEntId : entIds) {
				if(olds != null && olds.contains(toEntId)) {
					continue;
				}
				RegUser user = regUserService.get(Long.parseLong(toEntId));
				if(user ==  null) {
					continue;
				}
				this.sendMessage(dbQuestion, userName, userEntId, prof);
			}
		}

		//修改问题
		BeanUtils.setNotNullValue(question, dbQuestion);
		knowQuestionMapper.updateQuestion(dbQuestion);
		
		//更新附件的id
		moduleTempFileService.setMaster(queId, userEntId, "KnowQuestion");

		//修改答案
		if(question.getKnowAnswer() != null && StringUtils.isNotEmpty(question.getKnowAnswer().getAns_content())) {
			if(KnowQuestion.TYPE_FAQ.equals(question.getQue_type())){
				Knowanswer answer = knowanswerService.getByQueId(queId);
				answer.setAns_content(question.getKnowAnswer().getAns_content());
				knowanswerService.update(answer);
			}
		}

	}
	
	
	/**
	 * 站内信
	 * @throws SQLException 
	 */
	public void sendMessage(KnowQuestion question, String userName, long userEntId, loginProfile prof) throws SQLException{
		//站内信
		if(StringUtils.isNotEmpty(question.getQue_ask_ent_ids())) {
			String[] entIds = question.getQue_ask_ent_ids().split(",");
			String wmsgSubject = "";
			String wmsgContent = "";
			String[] contents = null;
			for(String toEntId : entIds) {
				RegUser user = regUserService.get(Long.parseLong(toEntId));
				if(user ==  null) {
					continue;
				}
				
				Map<String,Object> param = new HashMap<String,Object>();
				param.put("tpl_type", "KNOW_ADD_MESSAGE");
				param.put("tcr_id", prof.my_top_tc_id);
				MessageTemplate mtp = messageTemplateService.getByType(param);
				com.cw.wizbank.newmessage.entity.MessageTemplate ordmtp = new com.cw.wizbank.newmessage.entity.MessageTemplate();
				if(mtp != null){
					
					org.springframework.beans.BeanUtils.copyProperties(mtp, ordmtp);
					
					contents = forCallOldAPIService.getKnowQuestionContent(null, ordmtp, prof, question, user.getUsr_display_bil());
					wmsgSubject = contents[0];
					wmsgContent = contents[2];
					//判断是否为添加问题时发出的站内信，是则设type为SYS
					if(question.isType_sys()){
						webMessageService.addWebMessage(userEntId, toEntId, wmsgSubject, wmsgContent, "SYS", contents);
					}else{
						webMessageService.addWebMessage(userEntId, toEntId, wmsgSubject, wmsgContent, "PERSON", contents);
					}
				}
			}
		}
	}

}
