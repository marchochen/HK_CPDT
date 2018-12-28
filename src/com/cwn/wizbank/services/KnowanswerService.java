package com.cwn.wizbank.services;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cw.wizbank.config.WizbiniLoader;
import com.cwn.wizbank.common.SNS;
import com.cwn.wizbank.entity.KnowQuestion;
import com.cwn.wizbank.entity.KnowVoteDetail;
import com.cwn.wizbank.entity.Knowanswer;
import com.cwn.wizbank.entity.ModuleTempFile;
import com.cwn.wizbank.persistence.KnowQuestionMapper;
import com.cwn.wizbank.persistence.KnowVoteDetailMapper;
import com.cwn.wizbank.persistence.KnowanswerMapper;
import com.cwn.wizbank.utils.FileUtils;
import com.cwn.wizbank.utils.ImageUtil;
import com.cwn.wizbank.utils.Page;
/**
 *  service 实现
 */
@Service
public class KnowanswerService extends BaseService<Knowanswer> {

	@Autowired
	KnowanswerMapper knowAnswerMapper;

	@Autowired
	KnowQuestionMapper knowQuestionMapper;

	@Autowired
	KnowVoteDetailMapper knowVoteDetailMapper;

	@Autowired
	SnsDoingService snsDoingService;
	
	@Autowired
	ModuleTempFileService moduleTempFileService;

	public void setKnowAnswerMapper(KnowanswerMapper knowAnswerMapper){
		this.knowAnswerMapper = knowAnswerMapper;
	}

	/**
	 * 获取回答列表
	 * @param page
	 * @param queId 问题id
	 * @param rightInd 回答是否正确
	 * @return
	 */
	public Page<Knowanswer> getKnowAnswerList(Page<Knowanswer> page, long usr_ent_id, long queId, int rightInd,WizbiniLoader wizbini){
		String urlPath =  "/"+ wizbini.cfgSysSetupadv.getFileUpload().getEditorDir().getUrl();
		Knowanswer knowanswer = new Knowanswer();
		knowanswer.setAns_que_id(queId);
		knowanswer.setAns_right_ind(rightInd);
		page.getParams().put("usr_ent_id", usr_ent_id);
		page.getParams().put("knowanswer", knowanswer);
		knowAnswerMapper.selectKnowAnswerList(page);
		for(Knowanswer answer : page.getResults()){
			ImageUtil.combineImagePath(answer.getRegUser());
			//动态的附件
			List<ModuleTempFile> fileList = answer.getFileList();
			if(fileList != null){
				for(ModuleTempFile mtf : fileList) {
					if(!"url".equals(mtf.getMtf_file_type())){
						mtf.setMtf_url(	urlPath + "/" + FileUtils.SNS_TEMP_DIR + "/" + mtf.getMtf_module() + "/" + mtf.getMtf_usr_id());
					}
				}
			}
		}
		return page;
	}

	/**
	 * 添加回答
	 * @param queId 问题id
	 * @param usrEntId 用户id
	 * @param ansContent 回答内容
	 * @param ansReferContent 参考资料
	 * @return
	 */
	public void addKnowAnswer(long queId, long usrEntId, String ansContent, String ansReferContent,int ans_right_ind){
		this.addAnswer(queId, usrEntId, ansContent, ansReferContent, ans_right_ind);
	}


	/**
	 * 添加回答
	 * @param queId 问题id
	 * @param usrEntId 用户id
	 * @param ansContent 回答内容
	 * @param ansReferContent 参考资料
	 * @return
	 */
	public void addAnswer(long queId, long usrEntId, String ansContent, String ansReferContent, int rightInd){
		if(StringUtils.isNotEmpty(ansContent)) {
			ansContent = ansContent.replaceAll("<", "&lt;");
		}
		if(StringUtils.isNotEmpty(ansReferContent)) {
			ansReferContent = ansReferContent.replaceAll("<", "&lt;");
		}
		Knowanswer knowanswer = new Knowanswer();
		knowanswer.setAns_que_id(queId);
		knowanswer.setAns_content(ansContent);
		knowanswer.setAns_refer_content(ansReferContent);
		knowanswer.setAns_right_ind(rightInd);
		knowanswer.setAns_status("OK");
		knowanswer.setAns_create_ent_id(usrEntId);
		knowanswer.setAns_create_timestamp(getDate());
		knowanswer.setAns_update_ent_id(usrEntId);
		knowanswer.setAns_update_timestamp(getDate());
		knowanswer.setAns_content_search(ansContent);
		knowAnswerMapper.insert(knowanswer);
		
		//更新附件的id
		moduleTempFileService.setMaster(knowanswer.getAns_id(), usrEntId, "Know");

		//发动态
		//snsDoingService.add(queId, 0, usrEntId, 0, SNS.DOING_ACTION_ANSWER_ADD, 0, SNS.MODULE_ANSWER, "", 0);
	}

	/**
	 * 设为最佳回答
	 * @param ansId 回答id
	 * @param queId 问题id
	 */
	public void updateKnowAnswer(long ansId, long queId, long usrEntId){
		Knowanswer knowanswer = new Knowanswer();
		knowanswer.setAns_id(ansId);
		knowanswer.setAns_right_ind(1);
		knowanswer.setAns_vote_total(0);
		knowanswer.setAns_vote_for(0);
		knowanswer.setAns_vote_down(0);
		knowanswer.setAns_temp_vote_total(0);
		knowanswer.setAns_temp_vote_for(0);
		knowanswer.setAns_temp_vote_for_down_diff(0);
		knowanswer.setAns_update_ent_id(usrEntId);
		knowanswer.setAns_update_timestamp(getDate());
		knowAnswerMapper.update(knowanswer);

		KnowQuestion knowQuestion = new KnowQuestion();
		knowQuestion.setQue_id(queId);
		knowQuestion.setQue_answered_timestamp(getDate());
		knowQuestion.setQue_update_timestamp(getDate());
		knowQuestion.setQue_type("SOLVED");
		knowQuestionMapper.update(knowQuestion);
	}
	
	/**
	 * 删除相关answer
	 * @param ansId 回答id
	 * @param queId 问题id
	 */
	public void deleteKnowAnswer(long ansId, long queId){
		Knowanswer knowanswer = new Knowanswer();
		knowanswer.setAns_id(ansId);
		knowanswer.setAns_que_id(queId);
		knowAnswerMapper.deleteThisAnswer(knowanswer);
	}

	/**
	 * 更新回答评价
	 * @param knowanswer
	 * @param ansId 回答id
	 * @param queId 问题id
	 * @param goodInd 是否好
	 * @param usrEntId 用户id
	 * @param usrId
	 */
	public void updateAnsVote(Knowanswer knowanswer, long queId, boolean goodInd, long usrEntId, String usrId){
		if(goodInd){
			knowanswer.setAns_vote_for(knowanswer.getAns_vote_for() + 1);
			knowanswer.setAns_temp_vote_for(knowanswer.getAns_temp_vote_for() + 1);
		} else {
			knowanswer.setAns_vote_down(knowanswer.getAns_vote_down() + 1);
		}
		knowanswer.setAns_vote_total(knowanswer.getAns_vote_for() + knowanswer.getAns_vote_down());
		knowanswer.setAns_temp_vote_total(knowanswer.getAns_vote_for() + knowanswer.getAns_vote_down());
		knowanswer.setAns_temp_vote_for_down_diff(2 * knowanswer.getAns_temp_vote_for() - knowanswer.getAns_temp_vote_total());
		knowAnswerMapper.update(knowanswer);

		KnowVoteDetail knowVoteDetail = new KnowVoteDetail();
		knowVoteDetail.setKvd_que_id(queId);
		knowVoteDetail.setKvd_ans_id(knowanswer.getAns_id());
		knowVoteDetail.setKvd_ent_id(usrEntId);
		knowVoteDetail.setKvd_create_usr_id(usrId);
		knowVoteDetail.setKvd_create_timestamp(getDate());
		knowVoteDetailMapper.insert(knowVoteDetail);

	}

	public Knowanswer getByQueId(long id) {
		return this.knowAnswerMapper.getByQueId(id);
	}

}