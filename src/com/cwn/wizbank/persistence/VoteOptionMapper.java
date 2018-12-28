package com.cwn.wizbank.persistence;


import java.util.List;

import com.cwn.wizbank.entity.VoteOption;

/**
 * 投票 Mapper
 * @author Andrew 2015/6/9 上午10：56
 *
 */
public interface VoteOptionMapper extends BaseMapper<VoteOption>{

	/**
	 * 通过关联的问题删除问题选项
	 * @param vtq_id
	 */
	void deleteByVtoVtqId(Long vtq_id);
	
	void insertBatch(List<VoteOption> list);

	
}