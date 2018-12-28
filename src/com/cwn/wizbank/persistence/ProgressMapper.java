package com.cwn.wizbank.persistence;

import java.util.List;

import com.cwn.wizbank.entity.Progress;
import com.cwn.wizbank.utils.Page;


public interface ProgressMapper extends BaseMapper<Progress>{

	List<Progress> selectAnswerDetail(Page<Progress> page);
	List<Progress> selectAllTstResult(Progress pgr);
	List<Progress> selectAllAnswerDetail(Page<Progress> page);
	Long selectNotScore(Progress pgr);
	long getMaxAttemptNbr(Progress pgr);
	List<Progress> getMaxProgressAttempt(Page<Progress> page);
}