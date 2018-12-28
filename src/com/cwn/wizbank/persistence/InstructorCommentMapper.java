package com.cwn.wizbank.persistence;

import java.util.List;
import java.util.Map;

import com.cwn.wizbank.entity.InstructorComment;
import com.cwn.wizbank.utils.Page;


public interface InstructorCommentMapper extends BaseMapper<InstructorComment>{

	/**
	 * 获取讲师评分
	 * @param map
	 * @return
	 */
	List<InstructorComment> getInstructorCommentScore(Map<String,Object> map);

	List<InstructorComment> getInstructorComments(Page<InstructorComment> page);
	
}