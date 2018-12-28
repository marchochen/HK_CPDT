package com.cwn.wizbank.services;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.entity.MessageTemplate;
import com.cwn.wizbank.persistence.MessageTemplateMapper;
/**
 *  service 实现
 */
@Service
public class MessageTemplateService extends BaseService<MessageTemplate> {

	@Autowired
	MessageTemplateMapper messageTemplateMapper;

	public void setMessageTemplateMapper(MessageTemplateMapper messageTemplateMapper){
		this.messageTemplateMapper = messageTemplateMapper;
	}

	public MessageTemplate getByType(
			Map<String, Object> param) {
		return messageTemplateMapper.getByType(param);
	}
}