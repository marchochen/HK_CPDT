package com.cwn.wizbank.persistence;

import java.util.List;
import java.util.Map;

import com.cwn.wizbank.entity.SnsValuationLog;
import com.cwn.wizbank.entity.vo.LikeMsgVo;
import com.cwn.wizbank.utils.Page;


public interface SnsValuationLogMapper extends BaseMapper<SnsValuationLog>{

	int getCount(Map<String,Object> map);

	SnsValuationLog getByUserId(Map<String, Object> map);

	Long selectUserLikeTotal(Long usr_ent_id);
	
	Long add(SnsValuationLog valuationLog);
	
	void deleteList(Map<String, Object> map);
	
	public List<LikeMsgVo> userLikeList(Page<LikeMsgVo> page);
	void delErrorData(Map<String, Object> map);
}