package com.cwn.wizbank.persistence;

import java.util.List;

import com.cwn.wizbank.entity.KnowVoteDetail;


public interface KnowVoteDetailMapper extends BaseMapper<KnowVoteDetail>{
	
	List<KnowVoteDetail> selectAll(KnowVoteDetail knowVoteDetail);

	void insert(KnowVoteDetail knowVoteDetail);
	
}