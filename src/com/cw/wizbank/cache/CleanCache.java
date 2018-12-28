package com.cw.wizbank.cache;

import com.cw.wizbank.ScheduledTask;
import com.cwn.wizbank.utils.CommonLog;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * clear cache
 * @author Jacky xiao
 */
public class CleanCache extends ScheduledTask implements Job{

	public CleanCache(){

	}

	protected void init() {

	}
	protected void process() {
		CommonLog.info("clean cache");
		wizbCacheManager.getInstance().clearCache();
	}

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		init();
		process();
	}
}
