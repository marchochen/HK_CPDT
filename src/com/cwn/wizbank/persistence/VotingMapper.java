package com.cwn.wizbank.persistence;

import java.util.List;
import java.util.Map;

import com.cwn.wizbank.entity.Voting;
import com.cwn.wizbank.utils.Page;

/**
 * 投票 Mapper
 * @author Andrew 2015/6/6 下午 3：30
 *
 */
public interface VotingMapper extends BaseMapper<Voting>{

	List<Voting> pageAdmin(Page<Voting> page);
	Long add(Voting voting);
	void cancelPublished(Voting voting);
	List<Voting> pageFront(Page<Voting> page);
	
	long getInProgressCount(Map<String,Object> map );
}