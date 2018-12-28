package com.cwn.wizbank.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.entity.WebMsgReadHistory;
import com.cwn.wizbank.persistence.WebMsgReadHistoryMapper;
/**
 *  service 实现
 */
@Service
public class WebMsgReadHistoryService extends BaseService<WebMsgReadHistory> {

	@Autowired
	WebMsgReadHistoryMapper webMsgReadHistoryMapper;

	public void setWebMsgReadHistoryMapper(WebMsgReadHistoryMapper webMsgReadHistoryMapper){
		this.webMsgReadHistoryMapper = webMsgReadHistoryMapper;
	}
	
	public void insertWebMsgReadHistory(long wmsg_id){
		WebMsgReadHistory webMsgReadHistory = new WebMsgReadHistory();
		webMsgReadHistory.setWmrh_wmsg_id(wmsg_id);
		webMsgReadHistory.setWmrh_status("Y");
		webMsgReadHistory.setWmrh_read_datetime(getDate());
		webMsgReadHistoryMapper.insert(webMsgReadHistory);
	}
	
	public boolean checkMyReadHistory(long wmsg_id){
		return webMsgReadHistoryMapper.selectWebMsgReadHistory(wmsg_id) == null;
	}
	
}