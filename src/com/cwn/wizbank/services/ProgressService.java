package com.cwn.wizbank.services;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.entity.Progress;
import com.cwn.wizbank.persistence.ProgressMapper;
import com.cwn.wizbank.utils.Page;
/**
 *  service 实现
 */
@Service
public class ProgressService extends BaseService<Progress> {

	@Autowired
	ProgressMapper progressMapper;

	public void setProgressMapper(ProgressMapper progressMapper){
		this.progressMapper = progressMapper;
	}
	
	/**
	 * 获取考试成绩，对错情况
	 * @param tkh_id
	 * @param mod_id
	 * @return
	 */
	public List<Progress> getAnswerDetail(Page<Progress> page, long mod_id, long tkh_id, long attempt){
		page.getParams().put("res_id", mod_id);
		page.getParams().put("tkh_id", tkh_id);
		page.getParams().put("attempt", attempt);
		return progressMapper.selectAnswerDetail(page);
	}
	
	/**
	 * 查询所有考试结果详情
	 * @param pgr
	 * @return
	 */
	public List<Progress> selectAllTstResult(Progress pgr) {
		return progressMapper.selectAllTstResult(pgr);
	}
	
	
	/**
	 * 查询考试导航数据
	 * @param page
	 * @param mod_id
	 * @param tkh_id
	 * @param attempt
	 * @param usr_id
	 * @return
	 */
	public List<Progress> selectAllAnswerDetail(Page<Progress> page, long mod_id, long tkh_id, long attempt, String usr_id){
		page.setPageSize(999);
		page.getParams().put("res_id", mod_id);
		page.getParams().put("tkh_id", tkh_id);
		page.getParams().put("attempt", attempt);
		page.getParams().put("usr_id", usr_id);
		return progressMapper.selectAllAnswerDetail(page);
	}
	
	/**
	 * 查询某次考试未评分的题目数
	 * @param mod_id
	 * @param tkh_id
	 * @param attempt
	 * @param prof
	 * @return
	 */
	public Long selectNotScore(int mod_id, int tkh_id, int attempt, loginProfile prof) {
		Progress pgr = new Progress();
		pgr.setPgr_res_id(mod_id);
		pgr.setPgr_tkh_id(tkh_id);
		pgr.setPgr_attempt_nbr(attempt);
		pgr.setPgr_usr_id(prof.usr_id);
		return progressMapper.selectNotScore(pgr);
	}
	
	public long getMaxAttemptNbr(long mod_id,long tkh_id){
		Progress pgr = new Progress();
		pgr.setPgr_res_id((int) mod_id);
		pgr.setPgr_tkh_id((int) tkh_id);
		return progressMapper.getMaxAttemptNbr(pgr);
	}
	
	
	/**
	 * 获取考试的最高成绩
	 * @param tkh_id
	 * @param mod_id
	 * @return
	 */
	public List<Progress> getMaxProgressAttempt(Page<Progress> page, long mod_id, long tkh_id){
		page.getParams().put("res_id", mod_id);
		page.getParams().put("tkh_id", tkh_id);
		return progressMapper.getMaxProgressAttempt(page);
	}
}