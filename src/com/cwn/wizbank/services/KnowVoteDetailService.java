package com.cwn.wizbank.services;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.entity.KnowVoteDetail;
import com.cwn.wizbank.persistence.KnowVoteDetailMapper;
/**
 *  service 实现
 */
@Service
public class KnowVoteDetailService extends BaseService<KnowVoteDetail> {

	@Autowired
	KnowVoteDetailMapper knowVoteDetailMapper;

	public void setKnowVoteDetailMapper(KnowVoteDetailMapper knowVoteDetailMapper){
		this.knowVoteDetailMapper = knowVoteDetailMapper;
	}
	
	/**
	 * 是否已评价过
	 * @param queId 问题id
	 * @param usrEntId 用户id
	 * @return
	 */
	public boolean hasKnowVoteDetail(long queId, long usrEntId){
		KnowVoteDetail knowVoteDetail = new KnowVoteDetail();
		knowVoteDetail.setKvd_que_id(queId);
		knowVoteDetail.setKvd_ent_id(usrEntId);
		List<KnowVoteDetail> list = knowVoteDetailMapper.selectAll(knowVoteDetail);
		return list.size() > 0;
	}
}