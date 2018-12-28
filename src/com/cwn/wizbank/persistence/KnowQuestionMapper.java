package com.cwn.wizbank.persistence;

import java.util.List;

import com.cwn.wizbank.entity.KnowQuestion;
import com.cwn.wizbank.utils.Page;


public interface KnowQuestionMapper extends BaseMapper<KnowQuestion>{

	List<KnowQuestion> selectKnowQuestionList(Page<KnowQuestion> page);
	
	List<KnowQuestion> selectKnowSolveSituation(KnowQuestion knowQuestion);

	List<KnowQuestion> getLatestQuestionList(Page<KnowQuestion> page);
	
	void updateQuestion(KnowQuestion knowQuestion);
	
}