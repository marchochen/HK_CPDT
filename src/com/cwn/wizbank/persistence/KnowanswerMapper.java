package com.cwn.wizbank.persistence;

import java.util.List;

import com.cwn.wizbank.entity.Knowanswer;
import com.cwn.wizbank.utils.Page;

public interface KnowanswerMapper extends BaseMapper<Knowanswer>{

	public List<Knowanswer> selectKnowAnswerList(Page<Knowanswer> page);

	public Knowanswer getByQueId(long id);

	public void deleteThisAnswer(Knowanswer knowanswer);
	
}