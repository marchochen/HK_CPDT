package com.cwn.wizbank.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cwn.wizbank.entity.VoteOption;
import com.cwn.wizbank.entity.VoteQuestion;
import com.cwn.wizbank.entity.VoteResponse;
import com.cwn.wizbank.entity.Voting;
import com.cwn.wizbank.entity.VotingResponseResult;
import com.cwn.wizbank.persistence.VoteOptionMapper;
import com.cwn.wizbank.persistence.VoteQuestionMapper;
import com.cwn.wizbank.persistence.VoteResponseMapper;
import com.cwn.wizbank.persistence.VotingMapper;
import com.cwn.wizbank.persistence.VotingResponseResultMapper;
import com.cwn.wizbank.utils.Page;

/**
 * 投票 Service
 * @author Andrew 2015/6/6 下午 3：30
 *
 */
@Service
public class VotingService extends BaseService<Voting> {

	@Autowired
	private VotingMapper votingMapper;

	@Autowired
	private VoteQuestionMapper voteQuestionMapper;

	@Autowired
	private VoteOptionMapper voteOptionMapper;

	@Autowired
	private VotingResponseResultMapper votingResponseResultMapper;

	@Autowired
	private VoteResponseMapper voteResponseMapper;

	public static final String OPERATER_TYPE_UPDATE = "update";
	public static final String OPERATER_TYPE_ADD = "add";

	/**
	 * 投票错误编码：用户已经参与投票
	 */
	public static final String VOTE_ERROR_USER_HASH_VOTE = "-1";

	/**
	 * 投票错误编码：未选择答案
	 */
	public static final String VOTE_ERROR_NO_ANSWER = "-2";

	/**
	 * 投票错误编码：提交成功
	 */
	public static final String VOTE_ERROR_SUCCESS = "0";

	/**
	 * 获取分页列表
	 * @param page
	 * @param userEntId
	 * @return
	 */
	public Page<Voting> pageAdmin(Page<Voting> page,Long userEntId,String rolExtId){
		page.getParams().put("userEntId", userEntId);
		boolean isRoleTcInd=AccessControlWZB.isRoleTcInd(rolExtId);
		page.getParams().put("isRoleTcInd", isRoleTcInd);
		votingMapper.pageAdmin(page);
		return page;
	}


	/**
	 * 创建投票活动
	 * @param voting
	 * @param usr_ent_id 当前用户的id
	 * @param options
	 * @param optionType
	 */
	public void createVoting(Voting voting, Long usr_ent_id,String[] options, String optionType) {
		voting = generateVoting(voting,usr_ent_id,OPERATER_TYPE_ADD);
		votingMapper.add(voting);
		VoteQuestion question = generateVoteQuestion(voting,optionType,usr_ent_id,OPERATER_TYPE_ADD);
		voteQuestionMapper.add(question);
		List<VoteOption> voteOptions = generateVoteOption(question.getVtq_id(),options);
		//voteOptionMapper.insertBatch(voteOptions);
		for(VoteOption v : voteOptions)
		{
			voteOptionMapper.insert(v);
		}
	}


	private List<VoteOption> generateVoteOption(Long questionId,
			String[] options) {
		List<VoteOption> voteOptions = new ArrayList<VoteOption>();

		for(int i=0;options!=null&&i<options.length;i++){
			if(!StringUtils.isEmpty(options[i])){
				VoteOption item = new VoteOption();
				item.setVto_desc(options[i]);
				item.setVto_order(i+1L);
				item.setVto_vtq_id(questionId);
				voteOptions.add(item);
			}
		}

		return voteOptions;
	}


	private VoteQuestion generateVoteQuestion(Voting voting,String optionType,Long usr_ent_id,String operateType) {
		Date curDate = new Date();
		String userId = usr_ent_id+"";
		VoteQuestion question = new VoteQuestion();
		question.setVtq_vot_id(voting.getVot_id());
		question.setVtq_title(voting.getVot_title());
		question.setVtq_type(optionType);
		if(OPERATER_TYPE_ADD.equals(operateType)){
			question.setVtq_create_timestamp(curDate);
			question.setVtq_create_usr_id(userId);
		}
		question.setVtq_update_timestamp(curDate);
		question.setVtq_update_usr_id(userId);
		return question;
	}


	private Voting generateVoting(Voting voting,Long usr_ent_id,String operateType) {
		String userId = usr_ent_id+"";
		Date curDate = new Date();
		if(OPERATER_TYPE_ADD.equals(operateType)){
			voting.setVot_create_timestamp(curDate);
			voting.setVot_create_usr_id(userId);
		}
		voting.setVot_update_timestamp(curDate);
		voting.setVot_update_usr_id(userId);
		return voting;
	}


	/**
	 * 更新投票活动
	 * @param voting
	 * @param usr_ent_id
	 * @param options
	 * @param optionType
	 */
	public void updateVoting(Voting voting, long usr_ent_id, String[] options,
			String optionType) {
		voting = generateVoting(voting,usr_ent_id,OPERATER_TYPE_UPDATE);
		votingMapper.update(voting);
		voting = this.get(voting.getVot_id());
		VoteQuestion question = generateVoteQuestion(voting,optionType,usr_ent_id,OPERATER_TYPE_UPDATE);
		voteQuestionMapper.updateByVtqVotId(question);
		question = voting.getVoteQuestion();
		voteOptionMapper.deleteByVtoVtqId(question.getVtq_id());
		List<VoteOption> voteOptions = generateVoteOption(question.getVtq_id(),options);
		//voteOptionMapper.insertBatch(voteOptions);
		for(VoteOption v : voteOptions)
		{
			voteOptionMapper.insert(v);
		}
	}


	public void delete(Voting voting) {
		voting = this.get(voting.getVot_id());
		votingMapper.delete(voting.getVot_id());
		if(voting.getVoteQuestion() != null){
			voteQuestionMapper.delete(voting.getVoteQuestion().getVtq_id());
		}
	}

	/**
	 * 取消发布活动（修改状态）
	 * @param voting
	 */
	public Voting cancelPublished(Voting voting,Long usr_ent_id) {
		voting=votingMapper.get(voting.getVot_id());
		if(voting.getVot_status().equals("OFF")){
			voting.setVot_status("ON");
		}else{
			voting.setVot_status("OFF");
		}
		voting.setVot_update_usr_id(usr_ent_id+"");
		voting.setVot_update_timestamp(new Date());
		votingMapper.cancelPublished(voting);
		return voting;
	}


	/**
	 * 查询活动的投票结果信息
	 * @param vot_id
	 */
	public List<VotingResponseResult> getVotingResponseResult(Long vot_id) {
		return votingResponseResultMapper.listByVotId(vot_id);
	}

	/**
	 * 学员端获取投票列表
	 * @param page
	 * @param userEntId
	 * @return
	 */
	public Page<Voting> pageFront(Page<Voting> page,Long userEntId){
		page.getParams().put("userEntId", userEntId);
		votingMapper.pageFront(page);
		return page;
	}

	/**
	 * 参与投票
	 * @param voting
	 * @param usr_ent_id
	 * @param answers 答案，即问题的选项
	 * @return 返回错误编码
	 */
	public String vote(Voting voting, long usr_ent_id, Long[] answers) {
		Date curDate = new Date();
		if(answers == null||answers.length==0){//沒有提交答案
			return VOTE_ERROR_NO_ANSWER;
		}

		if(userHasVoted(usr_ent_id,voting.getVot_id())){//如果用户已经参与投票了
			return VOTE_ERROR_USER_HASH_VOTE;
		}

		List<VoteResponse> responses = new ArrayList<VoteResponse>();
		voting = this.get(voting.getVot_id());
		for(Long a : answers){
			VoteResponse vr = new VoteResponse();
			vr.setVrp_respone_time(curDate);
			vr.setVrp_usr_ent_id(usr_ent_id);
			vr.setVrp_vot_id(voting.getVot_id());
			vr.setVrp_vtq_id(voting.getVoteQuestion().getVtq_id());
			vr.setVrp_vto_id(a);
			responses.add(vr);
			voteResponseMapper.insert(vr);
		}
		return VOTE_ERROR_SUCCESS;
	}

	/**
	 * 用户是否已经参与了某个投票活动
	 * @param usr_ent_id 用户Id
	 * @param vot_id 投票Id
	 * @return
	 */
	private boolean userHasVoted(long usr_ent_id, Long vot_id) {
		Map<String, Long> params = new HashMap<String, Long>();
		params.put("userId", usr_ent_id);
		params.put("votId", vot_id);

		List<VoteResponse> responseList = voteResponseMapper.selectByUserIdAndVotId(params);

		if(responseList != null && responseList.size() > 0){
			return true;
		}

		return false;
	}


	public long getInProgressCount(long usrEntId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("usrEntId", usrEntId);
		map.put("curDate", getDate());
		return votingMapper.getInProgressCount(map);
	}
}