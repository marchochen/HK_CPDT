package com.cwn.wizbank.persistence;

import com.cwn.wizbank.entity.WebMsgReadHistory;


public interface WebMsgReadHistoryMapper extends BaseMapper<WebMsgReadHistory>{

	WebMsgReadHistory selectWebMsgReadHistory(long wmsg_id);
	
}