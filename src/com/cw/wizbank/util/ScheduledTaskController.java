package com.cw.wizbank.util;

import java.io.File;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.cw.wizbank.ScheduledStatus;
import com.cw.wizbank.ScheduledTask;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.config.system.scheduledtask.TaskType;
import com.cw.wizbank.course.loadTargetLrnCacheAndCourseEnrollScheduler;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.qdbEnv;
import com.cwn.wizbank.utils.CommonLog;

public class ScheduledTaskController extends Thread {

	private WizbiniLoader wizbini = null;

	private qdbEnv staticEnv = null;

	private cwSQL cwsql = null;

	private com.cw.wizbank.config.system.scheduledtask.ScheduledTask scheduleTask = null;

	private Logger logger = Logger.getLogger(ScheduledTaskController.class);

	private Vector taskVector = new Vector();

	private boolean reload = false;

	private boolean stopped = false;
	
	public ScheduledTaskController(WizbiniLoader wizbini, cwSQL cwsql, qdbEnv staticEnv) {
		this.wizbini = wizbini;
		this.cwsql = cwsql;
		this.staticEnv = staticEnv;
		this.scheduleTask = wizbini.cfgSysScheduledTask;
		this.reload = true;
	}

	private void init() {
		/*
		String logdir = wizbini.getAppnRoot() + dbUtils.SLASH + wizbini.cfgSysSetupadv.getLogDir().getName() + dbUtils.SLASH;
		File dir = new File(logdir);
		if (!dir.exists()) {
			dir.mkdir();
		}
		System.setProperty("log_path", logdir);
		*/
		System.setProperty("file_encoding", wizbini.cfgSysSetupadv.getEncoding());
		logger = Logger.getLogger(getClass().getName() + ".log");
		PropertyConfigurator.configure(wizbini.getCfgFileLog4jDir());
	}

	public void reload(WizbiniLoader wizbini, PrintWriter out, cwSQL sqlCon, qdbEnv staticEnv) {
		String br = "<br />";
		if (out == null) {
			out = new PrintWriter(System.out, true);
			br = "";
		}
		logger.debug("ScheduledTaskController.reload() start.");
		out.print("ScheduledTaskController.reload() start." + br);

		this.wizbini = wizbini;
		this.staticEnv = staticEnv;
		this.scheduleTask = wizbini.cfgSysScheduledTask;
		this.cwsql = sqlCon;
		this.reload = true;
		logger.debug("ScheduledTaskController.reload() finish.");
		out.print("ScheduledTaskController.reload() finish.");
	}

	private void parse(Calendar now) {
		logger.debug("ScheduledTaskController.parse() start.");
		if (taskVector != null) {
			taskVector.clear();
		}

		boolean autoEnroll = false;
		if (scheduleTask != null && scheduleTask.isEnabled()) {
			List list = this.scheduleTask.getTask();
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					TaskType taskType = (TaskType) list.get(i);
					if (taskType != null && taskType.isEnabled()) {
						ScheduledStatus scheduledStatus = new ScheduledStatus(taskType, now);
						taskVector.add(scheduledStatus);
					}
				}
			}

			for (int i = 0; i < this.taskVector.size(); i++) {
				ScheduledStatus status = (ScheduledStatus) this.taskVector.get(i);
				if (status.getClassName().equals(loadTargetLrnCacheAndCourseEnrollScheduler.class.getName())) {
				    loadTargetLrnCacheAndCourseEnrollScheduler.Auto_Enroll_Interval = status.getPeriod() / 60 / 1000;
					autoEnroll = true;
				}
			}
		}

		if (!autoEnroll) {
		    loadTargetLrnCacheAndCourseEnrollScheduler.Auto_Enroll_Interval = 0;
		}
		logger.debug("ScheduledTaskController.parse() finish.");
	}

	public void run() {
		// wait for a period of time in case other parts of servlet are not started yet
		try {
			sleep(1000 * 30);
		}
		catch (InterruptedException e) {
			CommonLog.error(e.getMessage(),e);
		}

		init();
		logger.debug("ScheduledTaskController.run() start.");
		while (!stopped) {
			Calendar now = Calendar.getInstance();
			if (this.reload) {
				parse(now);
				this.reload = false;
			}
			doScheduledTask(now);
			try {
				sleep(1000 * 60);
			}
			catch (InterruptedException e) {
				logger.debug("ScheduledTaskController error", e);
				CommonLog.error(e.getMessage(),e);
			}
		}
	}

	private void doScheduledTask(Calendar now) {
		logger.debug("ScheduledTaskController.doScheduleTask() start.");
		for (int i = 0; i < taskVector.size(); i++) {
			ScheduledStatus taskStatus = (ScheduledStatus) taskVector.get(i);
			if (taskStatus.canStart(now)) {
				ScheduledTask task = null;
				try {
					Class c = Class.forName(taskStatus.getClassName());
					task = (ScheduledTask) c.newInstance();
					task.setWizbini(wizbini);
					task.setLogger(logger);
					task.setStatic_env(staticEnv);
					task.setDbSource(cwsql);
					task.setParam(taskStatus.getParam());

					task.setScheduledStatus(taskStatus);
					taskStatus.setTask(task);

					task.start();
				}
				catch (Exception e) {
					logger.debug("ScheduledTaskController.doScheduledTask() error", e);
					CommonLog.error(e.getMessage(),e);
				}
			}
		}

		// for test
		if (false) {
			ScheduledTaskConsole console = new ScheduledTaskConsole(this);
			console.start();
		}
		logger.debug("ScheduledTaskController.doScheduleTask() finish.");
	}

	public void printStatusMessage(PrintWriter out) {
		if (taskVector != null && taskVector.size() > 0) {
			for (int i = 0; i < taskVector.size(); i++) {
				ScheduledStatus scheduledStatus = (ScheduledStatus) taskVector.get(i);
				scheduledStatus.printStatusMessage(out);
			}
		}
	}
	
	public void stopController() {
	    stopped = true;
	}
}

/**
 * only for test
 */
class ScheduledTaskConsole extends Thread {

	private ScheduledTaskController controller;

	public ScheduledTaskConsole(ScheduledTaskController controller) {
		this.controller = controller;
	}

	public void run() {
		try {
			sleep(1000 * 30);
		}
		catch (InterruptedException e) {
			CommonLog.error(e.getMessage(),e);
		}
		while (true) {
			controller.printStatusMessage(null);
			try {
				sleep(1000 * 10);
			}
			catch (InterruptedException e) {
				CommonLog.error(e.getMessage(),e);
			}
		}
	}

	public void setController(ScheduledTaskController controller) {
		this.controller = controller;
	}

}
