package com.cw.wizbank.qdb;

import com.cw.wizbank.ScheduledTask;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class CleanTestMemory extends ScheduledTask implements Job{

	public CleanTestMemory(){
	}

	protected void init() {
		// no param	
	}
	
	protected void process() {
		this.setPriority(Thread.MIN_PRIORITY);
		qdbAction.tests_memory.clear();
		qdbAction.Ques_memory.clear();
	}

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		init();
		process();
	}
}
