package com.cwn.wizbank.persistence;

import java.util.List;
import java.util.Map;

import com.cwn.wizbank.entity.WebMessage;
import com.cwn.wizbank.utils.Page;


public interface WebMessageMapper extends BaseMapper<WebMessage>{

	List<WebMessage> selectWebMessageList(Page<WebMessage> page);
	
	int getWebMessageCount(long userEntId);

	List<WebMessage> selectMessageList(Page<WebMessage> page);

	void delReadHistory(long id);

	List<WebMessage> selectSendMessageList(Page<WebMessage> page);

	void updateSendMessage(long id);

	void updateRecMessage(long id);
	
	List<WebMessage> getWebMessageTypeCount(long userEntId);
	
	long isExistFormMessage(Map<String,Object> map);
}