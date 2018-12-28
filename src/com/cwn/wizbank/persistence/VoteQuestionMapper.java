package com.cwn.wizbank.persistence;

import com.cwn.wizbank.entity.VoteQuestion;


/**
 * 投票 Mapper
 * @author Andrew 2015/6/9 上午 10：54
 *
 */
public interface VoteQuestionMapper extends BaseMapper<VoteQuestion>{

	/**
	 * 添加问题
	 * @param question 问题
	 * @return 插入的主键
	 */
	Long add(VoteQuestion question);

	/**
	 * 通过投票活动的id更新问题
	 * @param vot_id
	 */
	void updateByVtqVotId(VoteQuestion question);
	
}