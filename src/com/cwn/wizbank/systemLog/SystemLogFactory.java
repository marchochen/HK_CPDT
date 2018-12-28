package com.cwn.wizbank.systemLog;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cwn.wizbank.systemLog.service.LoginActionLogService;
import com.cwn.wizbank.systemLog.service.ObjectActionLogService;
import com.cwn.wizbank.systemLog.service.SystemActionLogService;
import com.cwn.wizbank.utils.SpringContextUtil;

public class SystemLogFactory {

	private static SystemLogFactory factory = null;
	
	private static Map<Integer,SystemActionLogService> strategyMap = new HashMap<Integer,SystemActionLogService>();
	
	@Autowired
	ObjectActionLogService objectActionLogService;
	
	@Autowired
	LoginActionLogService loginActionLogService;
	
    private SystemLogFactory(){
    	if(null == objectActionLogService){
    		loginActionLogService = (LoginActionLogService) SpringContextUtil.getBean("loginActionLogService"); 	
    		objectActionLogService = (ObjectActionLogService) SpringContextUtil.getBean("objectActionLogService"); 		
    	}
    	strategyMap.put(SystemLogTypeEnum.LOGIN_ACTION_LOG.value(),  loginActionLogService);
    	strategyMap.put(SystemLogTypeEnum.OBJECT_ACTION_LOG.value(),  objectActionLogService);
    }
    
    public SystemActionLogService creator(Integer type){
       return strategyMap.get(type);
    }
    
    public synchronized static SystemLogFactory getInstance(){
    	
       if(null==factory){
    	   factory = new SystemLogFactory();
       }
       
       return factory;
    }

}
