package com.cw.wizbank.report;

/**
 * @author donaldl
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
import java.util.*;
import java.io.IOException;
import java.io.Serializable;
import java.math.*;
import java.text.DecimalFormat;

import com.cw.wizbank.qdb.qdbEnv;
import com.cw.wizbank.util.LangLabel;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.ae.aeAttendanceStatus;
import com.cwn.wizbank.utils.CommonLog;

public class StatisticBean implements Serializable{
	private static final int day = 24 * 60 * 60;
	private static final int hour = 60 * 60;
	private static final int min = 60;

	public int totalCos;
	public int totalLrn;
	public int totalEnroll;
	public int totalAttemp;
	public float totalTimeSpent;
	public String totalScore = "0.0";

	private int enrolled;
	private int completed;
	private int inCompleted;
	private int withDrawn;

	public int ats_num;
	//private int[] status;
	public Map status = new HashMap();
	;

	public int attemptUsers;

	// indicate if there exists record
	private int hasValue = 0;

	//cos title and cos code
	public String t_code;
	public String t_title;
	//lrn display and login id
	public String u_display;
	public String u_login_id;
	public StatisticBean(Vector v) {
		/*if(i==0)
		   ats_num=11;
		ats_num = i;
		status= new int[ats_num+1];
		status[0]=-1;*/
		initMap(v);
	}

	private void initMap(Vector v) {
		int size = v.size();
		for (int i = 0; i < size; i++) {
			aeAttendanceStatus aes = (aeAttendanceStatus) v.elementAt(i);
			status.put(aes.getAtsId(), new Integer(0));
			//status.put(v.elementAt(i),new Integer(0));
		}
	}
	/*public StatisticBean() {
		status[0] = status.length;
		for(int i=1;i<status[0];i++){
			 status[i]=-1;
		}
	}*/
	public StatisticBean() {
		
	}

	public int getHasValue() {
		return this.hasValue;
	}
	public void setHasValue(int m) {
		this.hasValue = m;
	}

	public int getAttemptUsers() {
		return this.attemptUsers;
	}

	public void setAttemptUsers(int users) {
		this.attemptUsers = users;
	}

	public int getTotalCos() {
		return this.totalCos;
	}

	public void setTotalCos(int totalCos) {
		this.totalCos = totalCos;
	}

	public int getTotalLrn() {
		return this.totalLrn;
	}

	public void setTotalLrn(int totalLrn) {
		this.totalLrn = totalLrn;
	}

	public float getTotalTimeSpent() {
		return this.totalTimeSpent;
	}

	public void setTotalTimeSpent(float totalTimeSpent) {
		this.totalTimeSpent = totalTimeSpent;
	}

	public int getTotalAttemp() {
		return this.totalAttemp;
	}

	public void setTotalAttemp(int totalAttemp) {
		this.totalAttemp = totalAttemp;
	}
	public int getTotalEnroll() {
		return this.totalEnroll;
	}

	public void setTotalEnroll(int totalEnroll) {
		this.totalEnroll = totalEnroll;
	}

	public String getTotalScore() {
		return this.totalScore;
	}

	public void setTotalScore(String totalScore) {
		this.totalScore = totalScore;
	}

	public int getStatus(long ats_id) {
		//if(ats_ids==null||!ats_ids.contains(new Long(ats_id)))
		//throw new cwException("No this ats_id");
		//System.out.println("No this ats_id or cannot get status from database!");
		Integer s = (Integer) this.status.get(new Long(ats_id).toString());
		return s.intValue();
	}

	public void setStatus(long ats_id, int num) {
		//status[new Long(ats_id).intValue()] = num;
		this.status.put(new Long(ats_id).toString(), new Integer(num));
	}

	public int getStatusSize() {
		return this.status.size();
	}
	//========calculate and return the percentage of an attendance status
	public String getEnrolledPerc(int ats_id) throws cwException {
		String s = "";
		if (this.totalEnroll == 0) {
			//throw new cwException("Cannot compute the percent for the totalEnroll has no value");
			//System.out.println("this group has no body!So return 0");
			return "0";
		}
		DecimalFormat df = new DecimalFormat("#0.0%");
		try {
			//System.out.println("status id :"+ats_id);
			int m =
				((Integer) this.status.get(new Long(ats_id).toString()))
					.intValue();
			BigDecimal b1 = new BigDecimal(Integer.toString(m * 100));
			BigDecimal b2 = new BigDecimal(Integer.toString(this.totalEnroll));
			s = b1.divide(b2, 1, BigDecimal.ROUND_HALF_UP).toString();

		} catch (Exception e) {
			throw new cwException(e.getMessage());
		}
		return s;
	}

	// return average score, pass in total number of learners as the parameter
	public String getAverScore(int totalLrn) throws cwException {
		String s = "";
		if (totalLrn == 0 || totalScore == null) {
			//throw new cwException("total learners cannot be 0!");
			//System.out.println("totalLrn cannot be 0!So return 0");
			return "0";
		}

		BigDecimal tScore = new BigDecimal(this.totalScore);
		BigDecimal tLrn = new BigDecimal(totalLrn);
		try {
			s = tScore.divide(tLrn, 1, BigDecimal.ROUND_HALF_UP).toString();
		} catch (Exception e) {
			throw new cwException(e.getMessage());
		}
		return s;
	}

	// return average score
	public String getAverScore() throws cwException {
		String s = "0.0";

		/*if(this.totalLrn == 0) {
			  System.out.println("no total learner!");
		}*/
		BigDecimal tScore = new BigDecimal(this.totalScore);
		if (this.attemptUsers != 0) {
			BigDecimal tLrn = new BigDecimal(this.attemptUsers);
			try {
				s = tScore.divide(tLrn, 1, BigDecimal.ROUND_HALF_UP).toString();
			} catch (Exception e) {
				throw new cwException(e.getMessage());
			}
		}
		return s;
	}

	// accumulate score
	public void addScore(String score) throws cwException {
		if (score == null)
			return;
		try {
			BigDecimal oriScore = new BigDecimal(this.totalScore);
			BigDecimal add = new BigDecimal(score);

			this.totalScore = oriScore.add(add).toString();
		} catch (Exception e) {
			throw new cwException(
				"StatisticBean:addScore(String s)" + e.getMessage());
		}
	}

	// accumulate attempUsers
	public void addAttemptUsers(int attemptUser) {
		this.setAttemptUsers(this.attemptUsers + attemptUser);
	}

	// accumulate total time spent
	public void addTimeSpent(float time) {
		this.setTotalTimeSpent(this.totalTimeSpent + time);
	}

	// accumulate enrollments
	public void addTotalEnroll(int enroll) {
		this.setTotalEnroll(this.totalEnroll + enroll);
	}

	// accumulate totalAttempts
	public void addAttempts(int attempt) {
		this.setTotalAttemp(this.totalAttemp + attempt);
	}

	// accumulate status
	public void addStatus(long ats_id, int status) {
		//long atsId = new Long(ats_id).longValue();
		this.setStatus(ats_id, this.getStatus(ats_id) + status);
	}

	// transform display format of date and time, separated by '#'
	public String transTime() {
		StringBuffer s = new StringBuffer();
		try {
			int currTime = new Float(this.getTotalTimeSpent()).intValue();
			int days = currTime / day;
			int hours = (currTime % day) / hour;
			int mins = (currTime % day % hour) / min;
			int seconds = currTime % day % hour % min;
			s.append(Integer.toString(days)).append("#");
			if (hours < 10) {
				s.append("0");
			}
			s.append(Integer.toString(hours)).append(":");
			if (mins < 10) {
				s.append("0");
			}
			s.append(Integer.toString(mins)).append(":");
			if (seconds < 10) {
				s.append("0");
			}
			s.append(Integer.toString(seconds));
			//s = Integer.toString(days)+"#"+Integer.toString(hours)+":"+Integer.toString(mins)+":"+Integer.toString(seconds);

		} catch (Exception e) {
			CommonLog.error(e.getMessage(),e);
		}
		return s.toString();
	}

	public static String getSummary(String chartImgSavedPath, String tempPathName, String cur_lang, StatisticBean sb, Vector statusVec, int i, boolean show_stat_only)
		throws cwException, IOException {
		StringBuffer statisInfo = new StringBuffer(1024);
		if (i == 1) {
			statisInfo.append("<group_summary>");
		} else {
			statisInfo.append("<report_summary>");
			statisInfo.append("<total_courses value=\"").append(
				sb.getTotalCos()).append("\" />");
			statisInfo.append("<total_learners value=\"").append(sb.getTotalLrn()).append("\" />");
		}
		
		statisInfo
			.append("<total_enrollments value=\"")
			.append(Integer.toString(sb.getTotalEnroll()))
			.append("\"");
		statisInfo.append(">");
		Vector chartdata = new Vector();
		ReportChart rChart = new ReportChart();
		for (int k = 0; k < statusVec.size(); k++) {
			String atsId =((aeAttendanceStatus) statusVec.elementAt(k)).getAtsId();
			statisInfo.append("<attendance id=\"").append(atsId).append("\" value=\"").append(sb.getStatus(new Long(atsId).longValue())) 
			.append("\" percentage=\"").append(sb.getEnrolledPerc(new Integer(atsId).intValue())).append("\" />");
			if ((!show_stat_only || i != 1)&& sb.getTotalEnroll() >0){
				//set the pieplot data and color
				ReportChart.PiePlotData pieData = rChart.new PiePlotData();
				pieData.label = LearnerRptExportHelper.getAtsTitle(new Long(atsId).longValue(), cur_lang);
				pieData.value = sb.getStatus(new Long(atsId).longValue());
				pieData.pie_ats_id = new Integer(atsId).intValue();
				chartdata.add(pieData);
			}
		}
		if ((!show_stat_only || i != 1)&& sb.getTotalEnroll() > 0){
long start_time = System.currentTimeMillis();			
			String title = LangLabel.getValue(cur_lang, "lab_total_enroll") +": " + sb.getTotalEnroll();
			//statisInfo.append("<enrollment_img_name>").append(".."+ cwUtils.SLASH + tempPathName + cwUtils.SLASH + rChart.create(chartImgSavedPath, title, chartdata)).append("</enrollment_img_name>");
			String imgpath = ".."+ cwUtils.SLASH + tempPathName + cwUtils.SLASH + rChart.create(chartImgSavedPath, title, chartdata);
			imgpath = cwUtils.replaceSlashToHttp(imgpath);
			statisInfo.append("<enrollment_img_name>").append(imgpath).append("</enrollment_img_name>");
			CommonLog.debug("gen pieplot time = " + (System.currentTimeMillis() - start_time));
		}
		statisInfo.append("</total_enrollments>");
		//about the course
		statisInfo
			.append("<total_attempts value=\"")
			.append(Integer.toString(sb.getTotalAttemp())) 
			.append("\" />");
		String times = sb.transTime();
		String day = times.substring(0, times.indexOf("#"));
		String time = times.substring(times.indexOf("#") + 1, times.length());
		statisInfo
			.append("<total_time_spent day=\"")
			.append(day)
			.append("\" time=\"")
			.append(time)
			.append("\" />");
		statisInfo.append("<average_score value=\"").append(sb.getAverScore(sb.getTotalEnroll())).append("\" />");

		if (i == 1) {
			statisInfo.append("</group_summary>");
		} else {
			statisInfo.append("</report_summary>");
				}
		return statisInfo.toString();
	}

	// for testing only
	public static void main(String[] args) {
		Vector v = new Vector();
		for (int i = 0; i < 5; i++) {
			v.add(new Integer(i + 1).toString());
		}
		StatisticBean sb = new StatisticBean(v);
		try {
			sb.addScore("100.0000");
			sb.addScore("99.00");
			CommonLog.debug("Score Now:" + sb.getTotalScore());
			CommonLog.debug(
				"total 2 persons,averScore:" + sb.getAverScore(2));
			sb.setTotalEnroll(66);
			/*sb.setStatus(new Long(2).longValue(), 57);
			System.out.println("======Enrolled Percent:"
					+ sb.getStatus(new Long(2).longValue())+ "====="
					+ sb.getEnrolledPerc(2));
			//System.out.println("===status[1]=="
					+ sb.getStatus(new Long(1).longValue())+ "====="
					+ sb.getStatusSize());*/
			sb.setTotalTimeSpent((float) (60 * 60 + 69));
			CommonLog.debug("=======" + sb.transTime());
			CommonLog.debug("=======" + sb.transTime().indexOf("#"));
			CommonLog.debug(
				"======="
					+ sb.transTime().substring(0, sb.transTime().indexOf("#")));

			sb.setTotalEnroll(30);
			sb.setStatus(2, 10);
			sb.setStatus(3, 6);
			String en1 = sb.getEnrolledPerc(1);
			String en2 = sb.getEnrolledPerc(2);
			String en3 = sb.getEnrolledPerc(3);
			CommonLog.debug(
				"Enroll 1:" + en1 + "; Enroll 2:" + en2 + "; Enroll 3:" + en3);
		} catch (Exception e) {
			CommonLog.error(e.getMessage(),e);
		}

	}
}
