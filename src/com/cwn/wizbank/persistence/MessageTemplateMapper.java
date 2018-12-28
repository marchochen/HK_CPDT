package com.cwn.wizbank.persistence;

import java.util.Map;

import com.cwn.wizbank.entity.MessageTemplate;


public interface MessageTemplateMapper extends BaseMapper<MessageTemplate>{

	MessageTemplate getByType(Map<String ,Object> param);


}