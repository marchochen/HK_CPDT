package com.cw.wizbank;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.cw.wizbank.config.system.scheduledtask.RepeatType;
import com.cw.wizbank.config.system.scheduledtask.ScheduleType;
import com.cw.wizbank.config.system.scheduledtask.StartTimeType;
import com.cw.wizbank.config.system.scheduledtask.TaskType;

public class ScheduledStatus {

	public static final String Class_Name = "Class Name";

	public static final String Start_Time = "Start Time";

	public static final String End_Time = "End Time";

	public static final String Next_Start_Time = "Next Start Time";

	public static final String Period = "Period";

	public static final String Finish_Status = "Finish Status";

	public static final String Is_Alive = "Is Alive";

	public static final String Run_Count = "Run Count";

	//
	private Calendar startTime = null;

	private Calendar endTime = null;

	private Calendar nextStartTime = null;

	private boolean finishStatus = false;

	private long period = 0;

	private int runCount = 0;

	private String className = null;

	private List param = null;

	private ScheduledTask task = null;

	private ScheduleType scheduleType = null;

	//
	public ScheduledStatus(TaskType taskType, Calendar now) {
		this.scheduleType = taskType.getSchedule();
		this.param = taskType.getParam();
		this.className = taskType.getClassName();
		initPeriod(now);
	}

	//
	public void initPeriod(Calendar now) {
		Calendar newStartTime = (Calendar) now.clone();

		if (scheduleType != null) {
			RepeatType repeatType = scheduleType.getRepeat();
			StartTimeType startTimeType = scheduleType.getStartTime();
			if (repeatType != null) {
				if (repeatType.getUnit().equals(ScheduledTask.Repeat_Type_Hour)) {
					period = repeatType.getEvery() * 60 * 60 * 1000;
				}
				else if (repeatType.getUnit().equals(ScheduledTask.Repeat_Type_Minute)) {
					period = repeatType.getEvery() * 60 * 1000;
				}

				setNextStartTime(newStartTime);
			}
			else if (startTimeType != null) {
				period = 24 * 60 * 60 * 1000;

				int hour = startTimeType.getHour();
				int minute = startTimeType.getMinute();

				newStartTime.set(Calendar.HOUR_OF_DAY, hour);
				newStartTime.set(Calendar.MINUTE, minute);

				if (now.after(newStartTime)) {
					newStartTime.add(Calendar.DATE, 1);
				}
				setNextStartTime(newStartTime);
			}
		}
		else {
			period = 24 * 60 * 60 * 1000;
			setNextStartTime(newStartTime);
		}
        resetPeriod();
	}

	public boolean canStart(Calendar now) {
		boolean result = false;
		int hour = now.get(Calendar.HOUR_OF_DAY);
		int minute = now.get(Calendar.MINUTE);

		int taskHour = nextStartTime.get(Calendar.HOUR_OF_DAY);
		int taskMinute = nextStartTime.get(Calendar.MINUTE);

		if ((hour == taskHour) && (minute == taskMinute)) {
			resetPeriod();
			if (task == null || !task.isAlive()) {
				result = true;
			}
		}
		if (nextStartTime.before(now)) {
			initPeriod(now);
			resetPeriod();
			
		}
		return result;
	}

	public void resetPeriod() {
		this.nextStartTime.add(Calendar.MILLISECOND, (int) period);
	}

	public void printStatusMessage(PrintWriter out) {
		String br = "<br />";
		if (out == null) {
			out = new PrintWriter(System.out, true);
			br = "\r\n";
		}
		StringBuffer sb = new StringBuffer();
		sb.append("-------------------------------------------------------------------------").append(br);
		sb.append(ScheduledStatus.Class_Name).append(": ").append(className).append(br);

		Calendar c = startTime;
		sb.append(ScheduledStatus.Start_Time).append(": ").append((c == null) ? "" : parseDate(c.getTime())).append(br);

		c = endTime;
		sb.append(ScheduledStatus.End_Time).append(": ").append((c == null) ? "" : parseDate(c.getTime())).append(br);

		c = nextStartTime;
		sb.append(ScheduledStatus.Next_Start_Time).append(": ").append((c == null) ? "" : parseDate(c.getTime())).append(br);

		sb.append(ScheduledStatus.Period).append(" (MIN)").append(": ").append(period / 60 / 1000).append(br);
		sb.append(ScheduledStatus.Finish_Status).append(": ").append(finishStatus).append(br);
		sb.append(ScheduledStatus.Is_Alive).append(": ").append((task == null) ? false : task.isAlive()).append(br);
		sb.append(ScheduledStatus.Run_Count).append(": ").append(runCount).append(br);
		sb.append("-------------------------------------------------------------------------").append(br);

		out.println(sb.toString());
	}

	public String parseDate(Date date) {
		String p = "yyyy-M-dd HH:mm:ss";
		SimpleDateFormat format = new SimpleDateFormat(p);
		return format.format(date);
	}

	public void addRunCount() {
		this.runCount++;
	}

	//
	public String getClassName() {
		return this.className;
	}

	public List getParam() {
		return param;
	}

	public int getRunCount() {
		return runCount;
	}

	//
	public long getPeriod() {
		return period;
	}

	public void setStartTime(Calendar startTime) {
		this.startTime = startTime;
	}

	public void setEndTime(Calendar endTime) {
		this.endTime = endTime;
	}

	public void setNextStartTime(Calendar nextStartTime) {
		this.nextStartTime = nextStartTime;
	}

	public void setFinishStatus(boolean finishStatus) {
		this.finishStatus = finishStatus;
	}

	public void setPeriod(long period) {
		this.period = period;
	}

	public void setParam(List param) {
		this.param = param;
	}

	public void setTask(ScheduledTask task) {
		this.task = task;
	}

	public void setScheduleType(ScheduleType scheduleType) {
		this.scheduleType = scheduleType;
	}

}
