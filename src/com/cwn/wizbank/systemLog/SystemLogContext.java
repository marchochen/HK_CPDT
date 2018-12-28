package com.cwn.wizbank.systemLog;

import com.cwn.wizbank.entity.SystemLog;
import com.cwn.wizbank.systemLog.service.SystemActionLogService;

public class SystemLogContext {
	
    public static void saveLog(SystemLog systemLog, SystemLogTypeEnum systemLogTypeEnum) {
       SystemActionLogService  systemActionLogService = SystemLogFactory.getInstance().creator(systemLogTypeEnum.value());
       systemActionLogService.saveLog(systemLog);
    }
 
}
