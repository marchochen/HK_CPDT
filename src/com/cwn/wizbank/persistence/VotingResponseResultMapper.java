package com.cwn.wizbank.persistence;

import java.util.List;

import com.cwn.wizbank.entity.VotingResponseResult;

/**
 * 投票选项结果 Mapper
 * @author Andrew 2015/6/11 下午 1：46
 *
 */
public interface VotingResponseResultMapper extends BaseMapper<VotingResponseResult>{

	public List<VotingResponseResult> listByVotId(Long votId);
	
}