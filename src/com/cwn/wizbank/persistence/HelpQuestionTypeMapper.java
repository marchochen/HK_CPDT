package com.cwn.wizbank.persistence;

import java.util.List;

import com.cwn.wizbank.entity.HelpQuestionType;
import com.cwn.wizbank.entity.vo.HelpQuestionTypeVo;
import com.cwn.wizbank.utils.Page;
import com.cwn.wizbank.utils.Params;

public interface HelpQuestionTypeMapper extends BaseMapper<HelpQuestionType>{
	
	List<HelpQuestionTypeVo> getQuestionTypeList(Params params);
	
	HelpQuestionTypeVo getHelpQuestionType(Integer id);
	
	void delHelpQuestionType(Integer id);
	
	public List<HelpQuestionTypeVo>  getTypeList(Page<HelpQuestionTypeVo> page);

}
