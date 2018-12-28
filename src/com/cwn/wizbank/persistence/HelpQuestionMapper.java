package com.cwn.wizbank.persistence;

import java.util.List;

import com.cwn.wizbank.entity.HelpQuestion;
import com.cwn.wizbank.entity.vo.HelpQuestionTypeVo;
import com.cwn.wizbank.utils.Page;
import com.cwn.wizbank.utils.Params;

public interface HelpQuestionMapper extends BaseMapper<HelpQuestion>{
	
	List<HelpQuestion> getQuestionList(Page<HelpQuestion> page);
	
	HelpQuestion getQuestion(HelpQuestion ques);
	
	void delHelpQuestion(Integer id);
	
	Integer checkNumber(HelpQuestion ques);

}
