package com.cwn.wizbank.systemLog;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import com.cw.wizbank.ScheduledTask;
import com.cwn.wizbank.systemLog.service.LoginActionLogService;
import com.cwn.wizbank.systemLog.service.SystemActionLogService;
import com.cwn.wizbank.utils.SpringContextUtil;

public class LoginLogScheduler extends  ScheduledTask implements Job{

	@Autowired
	LoginActionLogService loginActionLogService;

	public LoginLogScheduler(){

	}
	
	@Override
	protected void init() {
		// TODO Auto-generated method stub
		if(null == loginActionLogService)
		{
			loginActionLogService = (LoginActionLogService)SpringContextUtil.getBean("loginActionLogService");
		}
	}

	@Override
	protected void process() {
	
		//删除一年前的数据表
		loginActionLogService.delOneYear();
		//跨月时，需要先生成对应月份log日志表
		loginActionLogService.createLoginLogTableByDate();
		//生成3个报表
		loginActionLogService.saveGourpLoginReport();
		loginActionLogService.saveGradeLoginReport();
		loginActionLogService.savePositionLoginReport();
	}


	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		init();
		process();
	}
}
