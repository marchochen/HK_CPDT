package com.cwn.wizbank.persistence;

import java.util.List;
import java.util.Map;

import com.cwn.wizbank.entity.VoteResponse;


/**
 * 投票结果 Mapper
 * @author Andrew 2015/6/12 上午 9：49
 *
 */
public interface VoteResponseMapper extends BaseMapper<VoteResponse>{

	/**
	 * 批量增加
	 * @param responses
	 */
	public void insertBatch(List<VoteResponse> responses);


	/**
	 * 根据用户id查询答题结果
	 * @param userId
	 * @return
	 */
	public List<VoteResponse> selectByUserId(Long userId);

	/**
	 * 根据用户id投票Id查询答题结果
	 * @param params
	 * @return
	 */
	public List<VoteResponse> selectByUserIdAndVotId(Map<String, Long> params);

}