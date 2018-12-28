package com.cwn.wizbank.persistence;

import java.util.List;
import java.util.Map;

import com.cwn.wizbank.entity.SnsComment;
import com.cwn.wizbank.utils.Page;


public interface SnsCommentMapper extends BaseMapper<SnsComment>{

	/**
	 * 获取课程评论
	 * @param page
	 * @return list
	 */
//	List<SnsComment> getCourseComment(Page<SnsComment> page);
	
	List<SnsComment> getCommentReply(Map<String, Object> map);
	
	List<SnsComment> getTargetCommnet(Map<String, Object> map);

	List<SnsComment> getComments(Map<String, Object> map);

	SnsComment getByUserId(Map<String, Object> params);

	List<SnsComment> getCommentPage(Page<SnsComment> page);

	void deleteList(Map<String, Object> map);

	Long getCommentCount(Map<String, Object> params);
	
	Long getClassCommentCount(Map<String, Object> params);
	
	List<SnsComment> getCommentByReplyId(long cmt_id);

}