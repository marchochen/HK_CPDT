/*
 * Created on 2004-10-9 15:08:27
 */
package com.cw.wizbank.ae;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.utils.CommonLog;

public class aeItemLesson {

	public static final String MSG_INS_OK = "ILS001";
	public static final String MSG_INS_ERROR = "ILS003";
	public static final String NULL_ILS_TIME_LIMIT = "1900-01-01 23:59:59.999" ;
	//when not set date,set default date field "1900-1-1"
	public static final Calendar cal_ref_time;
	static {
		cal_ref_time = Calendar.getInstance();
		cal_ref_time.set(1900,0,1,0,0,0);
		cal_ref_time.set(Calendar.MILLISECOND, 0);
	}
	public static final Calendar cal_ref_time_bound;
	static {
	    cal_ref_time_bound = Calendar.getInstance();
	    cal_ref_time_bound.set(1900,0,2,0,0,0);
	    cal_ref_time_bound.set(Calendar.MILLISECOND, 0);
	}	
	
	public long ils_id;
	public long ils_itm_id;
	public int ils_day;	
	public Timestamp ils_start_time;
	public Timestamp ils_end_time;
	public Timestamp ils_update_timestamp;	
	public String ils_title;
	public String ils_place;
	public String ils_create_usr_id;
	public String ils_update_usr_id;
	
	public int start_h;
	public int start_m;
	public int end_h;
	public int end_m;
	
	public Timestamp ils_date;
	public int ils_qiandao;
	public int ils_qiandao_chidao;
	public int ils_qiandao_queqin;
	public int ils_qiandao_youxiaoqi;
	public Timestamp ils_qiandao_chidao_time;
	public Timestamp ils_qiandao_queqin_time;
	public Timestamp ils_qiandao_youxiaoqi_time;
	
	public String insture_name;
	
	
	private static final String SQL_LESSON_INSERT =
		"INSERT INTO aeItemLesson("
			+ "ils_itm_id, "
			+ "ils_title, "
			+ "ils_day, "
			+ "ils_start_time, "
			+ "ils_end_time, "
			+ "ils_create_timestamp, "
			+ "ils_create_usr_id, "
			+ "ils_update_timestamp, "
			+ "ils_update_usr_id," 
			+ "ils_place,"
			+ "ils_date,"
			+ "ils_qiandao,"
			+ "ils_qiandao_chidao,"
			+ "ils_qiandao_queqin,"
			+ "ils_qiandao_youxiaoqi,"
			+ "ils_qiandao_chidao_time,"
			+ "ils_qiandao_queqin_time,"
			+ "ils_qiandao_youxiaoqi_time )"
			+ " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";

	private static final String SQL_LESSON_UPDATE =
		"Update aeItemLesson set"
			+ " ils_title=?,"
			+ " ils_day=?,"
			+ " ils_start_time=?,"
			+ " ils_end_time=?,"
			+ " ils_update_timestamp=?,"
			+ " ils_update_usr_id=? ," 
			+ " ils_place=? ,"
			+ " ils_date=? ,"
			+ " ils_qiandao=? ,"
			+ " ils_qiandao_chidao=? ,"
			+ " ils_qiandao_queqin=? ,"
			+ " ils_qiandao_youxiaoqi=? ,"
			+ " ils_qiandao_chidao_time=? ,"
			+ " ils_qiandao_queqin_time=? ,"
			+ " ils_qiandao_youxiaoqi_time=? "
			+ " Where ils_id=?";

	private static final String SQL_LESSON_DELETE =	"delete from aeItemLesson where ils_id=?";
	
	private static final String SQL_LESSON_SELECT_ID = 
			"Select"
					+ " ils_id,ils_itm_id,"
					+ " ils_title,"
					+ " ils_day,"
					+ " ils_start_time,"
					+ " ils_end_time,"
					+ " ils_create_timestamp,"
					+ " ils_create_usr_id,"
					+ " ils_update_timestamp,"
					+ " ils_update_usr_id,"
					+ " ils_date,"
					+ " ils_qiandao,"
					+ " ils_qiandao_chidao_time,"
					+ " ils_qiandao_queqin_time,"
					+ " ils_qiandao_youxiaoqi_time "
					+ " from aeItemLesson where ils_id=? order by ils_date ,ils_start_time asc";
	
	private static final String SQL_LESSON_SELECT =
		"Select"
			+ " ils_id,ils_itm_id,"
			+ " ils_title,"
			+ " ils_day,"
			+ " ils_start_time,"
			+ " ils_end_time,"
			+ " ils_create_timestamp,"
			+ " ils_create_usr_id,"
			+ " ils_update_timestamp,"
			+ " ils_update_usr_id,"
			+ " ils_date,"
			+ " ils_qiandao,"
			+ " ils_qiandao_chidao_time,"
			+ " ils_qiandao_queqin_time,"
			+ " ils_qiandao_youxiaoqi_time "
			+ " from aeItemLesson where ils_itm_id=? order by ils_date ,ils_start_time asc";
	
	private static final String SQL_LESSON_SELECT_COUNT =
		"Select count(*) from aeItemLesson where ils_itm_id=? ";	

	private static final String SQL_LESSON_SEL_SINGLE =
		"Select"
			+ " ils_id,"
			+ " ils_title,"
			+ " ils_day,"
			+ " ils_start_time,"
			+ " ils_end_time,"
			+ " ils_create_timestamp,"
			+ " ils_create_usr_id,"
			+ " ils_update_timestamp,"
			+ " ils_update_usr_id,"
			+ " ils_place, "
			+ " ils_date,"
			+ " ils_qiandao,"
			+ " ils_qiandao_chidao,"
			+ " ils_qiandao_queqin,"
			+ " ils_qiandao_youxiaoqi "
			+ " from aeItemLesson where ils_id=?";

	private static final String SQL_LESSON_CHK_TIME = "select count(*) as CountX from aeItemLesson where ils_itm_id=? and ils_day=? and ((? between ils_qiandao_youxiaoqi_time and ils_end_time) or (? between ils_qiandao_youxiaoqi_time and ils_end_time) or (? < ils_qiandao_youxiaoqi_time and ? > ils_end_time))";

	private static final String SQL_LESSON_GET_DATE_BYDAY =	"select max(ils_start_time) as start_time from aeItemLesson where ils_itm_id=? and ils_day=?";

	private static final String SQL_LESSON_UPD_CHK_TIMESTAMP = "select count(*) as CountX from aeItemLesson where ils_id=? and ils_update_timestamp=?";

	private static final String SQL_LESSON_GET_INSTRUCTOR = "select ili_usr_ent_id, " +  cwSQL.replaceNull("usr_ste_usr_id", "'--'") + " as  usr_ste_usr_id, " +  cwSQL.replaceNull("usr_display_bil", "iti_name") + " as  teacher_name from aeItemLessonInstructor left join  reguser on(ili_usr_ent_id=usr_ent_id ) left join Instructorinf on(iti_ent_id = ili_usr_ent_id ) where ili_ils_id=?";

	private static final String SQL_LESSON_GET_DAY_AND_DATE = "select ils_day,max(ils_start_time) as day_date from aeItemLesson where ils_itm_id=? group by ils_day order by ils_day";
    
    private static final String SQL_LESSON_DEL_BY_ITEM = "delete from aeItemLesson where ils_itm_id=?";
    
	private static final String SQL_GET_ILS_ITM_ID = "Select ils_itm_id From aeItemLesson Where ils_id=?";
	
	private static final String SQL_GET_TIME_BY_ILS_ID = "Select ils_start_time,ils_end_time From aeItemLesson Where ils_id=?";
	
    private static final String SQL_GET_MAX_DAY = "select max(ils_day) maxday from aeItemLesson where ils_itm_id=?";
    
    private static final String SQL_LESSON_GET_MAX_DATE = "select max(ils_end_time) from aeItemLesson where ils_itm_id=? and ils_end_time > ?";
    
    private static final String SQL_LESSON_GET_MIN_DATE = "select min(ils_start_time) from aeItemLesson where ils_itm_id=? and ils_start_time > ?";
    
    private static final String SQL_LESSON_CHK_TIME_CONFLICT_BY_ITM = "select  count(*) from" 
        + " aeItemLesson myLesson, aeItemLessonInstructor myInstructor, aeItemLesson otherLesson, aeItemLessonInstructor otherInstructor"
        + " where myLesson.ils_itm_id = ?"
        + " and myLesson.ils_id = myInstructor.ili_ils_id"
        + " and otherInstructor.ili_usr_ent_id = myInstructor.ili_usr_ent_id"
        + " and otherLesson.ils_id = otherInstructor.ili_ils_id"
        + " and otherLesson.ils_itm_id <> myLesson.ils_itm_id"
        + " and ((myLesson.ils_start_time between otherLesson.ils_start_time and otherLesson.ils_end_time)" 
        + " or (myLesson.ils_end_time between otherLesson.ils_start_time and otherLesson.ils_end_time)" 
        + " or (myLesson.ils_start_time > otherLesson.ils_start_time and myLesson.ils_end_time < otherLesson.ils_end_time))";
    
	private static final String SQL_LESSON_GET_DAY_DATE_DETAIL_BY_ITM = "select ils_day, ils_id, ils_start_time, ils_end_time from aeItemLesson where ils_itm_id=? order by ils_day, ils_id, ils_start_time";
	
	private static final String SQL_LESSON_SET_DATE = "update aeItemLesson set ils_start_time=?, ils_end_time=? where ils_id=?";
	
	private static final String SQL_LESSON_CHK_EXIST_INSTRUCTOR = "select count(*) from aeItemLessonInstructor where ili_ils_id in (select ils_id from aeItemLesson where ils_itm_id=? and ils_day=?)";
	
	public aeItemLesson() {
	}

	/**
	 * @throws SQLException
	 */
	public boolean ins(Connection con) throws SQLException, cwSysMessage {
		boolean result = false;
		Timestamp ils_create_timestamp = cwSQL.getTime(con);
		Timestamp ils_update_timestamp = ils_create_timestamp;
		int index = 1;
		PreparedStatement stmt = con.prepareStatement(SQL_LESSON_INSERT);
		if(ils_qiandao == 0){
			ils_qiandao_chidao = 0;
			ils_qiandao_queqin = 0;
			ils_qiandao_youxiaoqi = 0;
		}
        try{
		stmt.setLong(index++, ils_itm_id);
		stmt.setString(index++, ils_title);
		stmt.setLong(index++, ils_day);
		stmt.setTimestamp(index++, ils_start_time);
		stmt.setTimestamp(index++, ils_end_time);
		stmt.setTimestamp(index++, ils_create_timestamp);
		stmt.setString(index++, ils_create_usr_id);
		stmt.setTimestamp(index++, ils_update_timestamp);
		stmt.setString(index++, ils_update_usr_id);
		stmt.setString(index++, ils_place);
		stmt.setTimestamp(index++, ils_date);
		stmt.setLong(index++, ils_qiandao);
		stmt.setLong(index++, ils_qiandao_chidao);
		stmt.setLong(index++, ils_qiandao_queqin);
		stmt.setLong(index++, ils_qiandao_youxiaoqi);
		stmt.setTimestamp(index++, DateYunSuan(ils_start_time, ils_qiandao_chidao));
		stmt.setTimestamp(index++, DateYunSuan(ils_start_time, ils_qiandao_queqin));
		stmt.setTimestamp(index++, DateYunSuan(ils_start_time, -ils_qiandao_youxiaoqi));
		if (stmt.executeUpdate() == 1) {
			result = true;
    		} else {
			stmt.close();
			throw new cwSysMessage("ILS003");
			//throw new SQLException("Failed to Insert Lesson of Item itm_id = " + ils_itm_id);
		}
        }catch(SQLException e){
            stmt.close();
           // e.printStackTrace();
            CommonLog.error(e.getMessage(),e);
            throw new cwSysMessage("ILS003");
        }
		return result;
	}

	/**
	 * 
	 * @param con
	 * @param id
	 * @return @throws
	 *         SQLException
	 */
	public boolean del(Connection con, long id)
		throws SQLException, cwSysMessage {
		boolean result = false;
		PreparedStatement stmt = con.prepareStatement(SQL_LESSON_DELETE);
		try {
			stmt.setLong(1, id);
			if (stmt.executeUpdate() > 0) {
				result = true;
				} else {
				stmt.close();
				throw new cwSysMessage("ILS006");
			}
		} catch (SQLException e) {
			stmt.close();
			throw new cwSysMessage("ILS006");
		} finally {
			if (stmt != null) stmt.close();
		}
		return result;
	}

    public static void delByItemId(Connection con, long itm_id) throws SQLException {
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(SQL_LESSON_DEL_BY_ITEM);
            stmt.setLong(1, itm_id);
            stmt.executeUpdate();
        } finally {
            if(stmt != null)
                stmt.close();
        }
    }
    
	public boolean upd(Connection con) throws SQLException, cwSysMessage {
		boolean result = false;
		int index = 1;
		Timestamp ils_update_timestamp = cwSQL.getTime(con);
		PreparedStatement stmt = con.prepareStatement(SQL_LESSON_UPDATE);
		if(ils_qiandao == 0){
			ils_qiandao_chidao = 0;
			ils_qiandao_queqin = 0;
			ils_qiandao_youxiaoqi = 0;
		}
        try {
			stmt.setString(index++, ils_title);
			stmt.setInt(index++, ils_day);
			stmt.setTimestamp(index++, this.ils_start_time);
			stmt.setTimestamp(index++, this.ils_end_time);
			stmt.setTimestamp(index++, ils_update_timestamp);
			stmt.setString(index++, ils_update_usr_id);
			stmt.setString(index++, ils_place);
			stmt.setTimestamp(index++, ils_date);
			stmt.setLong(index++, ils_qiandao);
			stmt.setLong(index++, ils_qiandao_chidao);
			stmt.setLong(index++, ils_qiandao_queqin);
			stmt.setLong(index++, ils_qiandao_youxiaoqi);
			stmt.setTimestamp(index++, DateYunSuan(ils_start_time, ils_qiandao_chidao));
			stmt.setTimestamp(index++, DateYunSuan(ils_start_time, ils_qiandao_queqin));
			stmt.setTimestamp(index++, DateYunSuan(ils_start_time, -ils_qiandao_youxiaoqi));
			stmt.setLong(index++, ils_id);
			if (stmt.executeUpdate() > 0) {
				result = true;
			} else {
				throw new cwSysMessage("GEN006");
			}
			return result;
		} catch (SQLException e) {
			//e.printStackTrace();
			CommonLog.error(e.getMessage(),e);
			throw new cwSysMessage("GEN006");
		} finally {
			if (stmt != null) stmt.close();
		}
	}
    
    //check if the time of unit conflict with other unit  
	public boolean checkTimePeriod(Connection con, long l_ils_id)
		throws SQLException {
		
		Timestamp ils_date = this.ils_date;
		Calendar cal_s = Calendar.getInstance();
		Calendar cal_e = Calendar.getInstance();
		Calendar cal = Calendar.getInstance();
		//Timestamp tmpDate = getLessonDateByDay(con, this.ils_itm_id,
		//		this.ils_day);
		//if (tmpDate == null) {
		//	cal_s.set(cal_ref_time.get(Calendar.YEAR), cal_ref_time
		//			.get(Calendar.MONTH), cal_ref_time.get(Calendar.DATE),
		//			start_h, start_m, 0);
		//	cal_e.set(cal_ref_time.get(Calendar.YEAR), cal_ref_time
		//			.get(Calendar.MONTH), cal_ref_time.get(Calendar.DATE),
		//			end_h, end_m, 0);
		//} else {
		cal.setTime(ils_date);
		cal_s.set(cal.get(Calendar.YEAR),
				cal.get(Calendar.MONTH),
				cal.get(Calendar.DATE),
				start_h, start_m, 0);
		cal_e.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal
				.get(Calendar.DATE), end_h, end_m, 0);
		//}
		cal_s.set(Calendar.MILLISECOND, 0);
		cal_e.set(Calendar.MILLISECOND, 0);
		this.ils_start_time = new Timestamp(cal_s.getTime().getTime());
		this.ils_end_time = new Timestamp(cal_e.getTime().getTime());
		StringBuffer sqlbuffer = new StringBuffer(SQL_LESSON_CHK_TIME);
		if (l_ils_id != 0) {
			sqlbuffer.append(" and ils_id!=?");
		}
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(sqlbuffer.toString());
			int index = 1;
			stmt.setLong(index++, ils_itm_id);
			stmt.setInt(index++, ils_day);
			stmt.setTimestamp(index++, this.ils_start_time);
			stmt.setTimestamp(index++, this.ils_end_time);
			stmt.setTimestamp(index++, this.ils_start_time);
			stmt.setTimestamp(index++, this.ils_end_time);
			if (l_ils_id != 0) {
				stmt.setLong(index++, l_ils_id);
			}
			rs = stmt.executeQuery();
			if (rs.next())
				if (rs.getInt("CountX") > 0)
					return true;
		} finally {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
		}
		return false;
	}

	public Timestamp getLessonDateByDay(Connection con, long ils_itm_id, int ils_day)
		throws SQLException {
		Timestamp tmpDate;
		PreparedStatement stmt = con.prepareStatement(SQL_LESSON_GET_DATE_BYDAY);
		int index = 1;
		stmt.setLong(index++, ils_itm_id);
		stmt.setInt(index++, ils_day);
		ResultSet rs = stmt.executeQuery();
		if (rs.next()) {
			tmpDate = rs.getTimestamp("start_time");
		} else {
			tmpDate = null;
		}
		rs.close();
		stmt.close();
		return tmpDate;
	}
   
	public boolean checkConflict(Connection con, long l_ils_id,	Timestamp upd_timestamp)
		           throws SQLException {
		boolean result = false;
		if (upd_timestamp != null) {
			PreparedStatement stmt =
				con.prepareStatement(SQL_LESSON_UPD_CHK_TIMESTAMP);
			int index = 1;
			stmt.setLong(index++, l_ils_id);
			stmt.setTimestamp(index++, upd_timestamp);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				if (rs.getInt("CountX") == 1) {
					result = true;
				}
			}
			rs.close();
			stmt.close();
		}
		return result;
	}

    public boolean isExistInstructorByDay(Connection con, long ils_itm_id, int ils_day) throws SQLException {
        boolean result = false;
        
        PreparedStatement stmt = con.prepareStatement(SQL_LESSON_CHK_EXIST_INSTRUCTOR);
        int index = 1;
        stmt.setLong(index++, ils_itm_id);
        stmt.setLong(index++, ils_day);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()){
            if(rs.getInt(1) > 0)
                result = true;
        }
        stmt.close();
        return result;
    }
    
	public String getRunListAsXML(Connection con, long itm_id)
		throws SQLException {
		StringBuffer xmlBuffer = new StringBuffer();
		PreparedStatement stmt = con.prepareStatement(SQL_LESSON_SELECT);
		stmt.setLong(1, itm_id);
		ResultSet rs = stmt.executeQuery();
		xmlBuffer
			.append("<itm_lessons itm_id=\"")
			.append(itm_id)
			.append("\" count=\"")
			.append(getLessonCountByItmId(con, itm_id))
			.append("\">");
		while (rs.next()) {
			xmlBuffer
			.append("<lesson ils_id=\"")
			.append(rs.getInt("ils_id"))
			.append("\" day=\"")
			.append(rs.getInt("ils_day"))
			.append("\" ils_date=\"")
			.append(rs.getTimestamp("ils_date") == null ? "" : cwUtils.format2simple(rs.getTimestamp("ils_date")))
			.append("\" ils_qiandao=\"")
			.append(rs.getInt("ils_qiandao"))
			.append("\" ils_qiandao_chidao=\"")
			.append(rs.getTimestamp("ils_qiandao_chidao_time"))
			.append("\" ils_qiandao_queqin=\"")
			.append(rs.getTimestamp("ils_qiandao_queqin_time"))
			.append("\" ils_qiandao_youxiaoqi=\"")
			.append(rs.getTimestamp("ils_qiandao_youxiaoqi_time"))
			.append("\" start_time=\"")
			.append(rs.getTimestamp("ils_start_time"))
			.append("\" end_time=\"")
			.append(rs.getTimestamp("ils_end_time"))
			.append("\" >")
			.append("<title>")
			.append(cwUtils.esc4XML(rs.getString("ils_title")))
			.append("</title>")
			.append(getRunListTeacherXML(con, rs.getLong("ils_id")))
			.append("<last_updated timestamp=\"")
			.append(rs.getTimestamp("ils_update_timestamp"))
			.append("\" usr_id=\"")
			.append(rs.getString("ils_update_usr_id"))
			.append("\" />")
			.append("</lesson>");
		}
		stmt.close();
		xmlBuffer.append("</itm_lessons>");
		return xmlBuffer.toString();
	}
	
	public Vector<aeItemLesson> getRunList(Connection con, long ils_id, boolean isQianDao)
		throws SQLException {
		
		Vector<aeItemLesson> vec = new Vector<aeItemLesson>();
		PreparedStatement stmt = con.prepareStatement(SQL_LESSON_SELECT_ID);
		stmt.setLong(1, ils_id);
		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			aeItemLesson al = new aeItemLesson();
			al.ils_id = rs.getInt("ils_id");
			al.ils_date = rs.getTimestamp("ils_date");
			al.ils_qiandao = rs.getInt("ils_qiandao");
			al.ils_qiandao_chidao_time = rs.getTimestamp("ils_qiandao_chidao_time");
			al.ils_qiandao_queqin_time = rs.getTimestamp("ils_qiandao_queqin_time");
			al.ils_qiandao_youxiaoqi_time = rs.getTimestamp("ils_qiandao_youxiaoqi_time");
			al.ils_start_time = rs.getTimestamp("ils_start_time");
			al.ils_end_time = rs.getTimestamp("ils_end_time");
			al.ils_title = cwUtils.esc4XML(rs.getString("ils_title"));
			al.insture_name = getRunListTeacherName(con, rs.getLong("ils_id"));
			if(isQianDao){
				if(al.ils_qiandao == 1){
					vec.add(al);
				}
			} else {
				vec.add(al);
			}
		}
		stmt.close();
		return vec;
	}

	public String getRunListTeacherXML(Connection con, long ils_id)
		throws SQLException {
		StringBuffer xmlBuffer = new StringBuffer();
		PreparedStatement stmt = con.prepareStatement(SQL_LESSON_GET_INSTRUCTOR);
		int index = 1;
		stmt.setLong(index++, ils_id);
		ResultSet rs = stmt.executeQuery();
		xmlBuffer.append("<teachers>");
		while (rs.next()) {
            xmlBuffer.append("<teacher>") .append(cwUtils.esc4XML(rs.getString("teacher_name")))
                     .append("</teacher>");
		}
		xmlBuffer.append("</teachers>");
		rs.close();
		stmt.close();
		return xmlBuffer.toString();
	}
	
	public String getRunListTeacherName(Connection con, long ils_id)
	throws SQLException {
		PreparedStatement stmt = con.prepareStatement(SQL_LESSON_GET_INSTRUCTOR);
		int index = 1;
		stmt.setLong(index++, ils_id);
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
	       return cwUtils.esc4XML(rs.getString("teacher_name"));
		}
		rs.close();
		stmt.close();
		return "";
	}
	
    //set datepart of training unit
	public boolean setDate(Connection con, long l_itm_id, Hashtable ils_day_date_Req)
		throws SQLException, cwSysMessage {
		Calendar cal_new, cal_s, cal_e;
		cal_new = Calendar.getInstance();
		cal_s = Calendar.getInstance();
		cal_e = Calendar.getInstance();
        
        if(!ils_day_date_Req.isEmpty()){
        	Hashtable[] orgdate = getLessonDayDateByItm(con, l_itm_id);
			Enumeration emuKey = ils_day_date_Req.keys();
			String day, date;
			PreparedStatement stmt = con.prepareStatement(SQL_LESSON_SET_DATE);
			try {
				while (emuKey.hasMoreElements()) {
					day = (String) emuKey.nextElement();
					date = (String) ils_day_date_Req.get(day);
					if (day != null && day.length() > 0) {
						if (date == null || date == "") {
							//empty the datefield of lesson
							cal_new.setTime(cal_ref_time.getTime());
						} else {
							Timestamp new_date = Timestamp.valueOf(date + " 00:00:0.000");
							cal_new.setTime(new_date);						    
						}
						Long day_l = Long.valueOf(day);
						Long ils_id;
						if (orgdate[0].containsKey(day_l)) {
							ArrayList ils_id_lst = (ArrayList) orgdate[0].get(day_l);
							for (int i = 0; i < ils_id_lst.size(); i++) {
								ils_id = (Long) ils_id_lst.get(i);
								cal_s.setTime((Timestamp) orgdate[1].get(ils_id));
								cal_e.setTime((Timestamp) orgdate[2].get(ils_id));
								cal_s.set(cal_new.get(Calendar.YEAR), cal_new.get(Calendar.MONTH), cal_new.get(Calendar.DATE));
								cal_e.set(cal_new.get(Calendar.YEAR), cal_new.get(Calendar.MONTH), cal_new.get(Calendar.DATE));
								cal_s.set(Calendar.MILLISECOND, 0);
								cal_e.set(Calendar.MILLISECOND, 0);
								int index = 1;
								stmt.setTimestamp(index++, new Timestamp(cal_s.getTime().getTime()));
								stmt.setTimestamp(index++, new Timestamp(cal_e.getTime().getTime()));
								stmt.setLong(index++, ils_id.longValue());
								stmt.executeUpdate();
							}
						}
					}
				}
			} catch (Exception e) {
				throw new cwSysMessage("ILS009");
			} finally {
				if (stmt != null) stmt.close();
			}
        }else{
            return false;
        }
        return true;
    }

    public boolean checkLessonDateWithClass(Connection con) throws SQLException, cwSysMessage{
        boolean result = false;
        Timestamp classStartDate, classEndDate;
        classStartDate = getMinDateOfClass(con);
        classEndDate = getMaxDateOfClass(con);
        aeItem itm = new aeItem();
        itm.itm_id = this.ils_itm_id;
        itm.get(con);
        if(itm.itm_eff_start_datetime.after(classStartDate) || itm.itm_eff_start_datetime.after(classEndDate)){
            result = false;
        }else if(itm.itm_eff_end_datetime.before(classStartDate) || itm.itm_eff_end_datetime.before(classEndDate)){
            result = false;
        }else{
            result = true;
        }
        return result;
    }
    
    public Timestamp getMaxDateOfClass(Connection con) throws SQLException, cwSysMessage{
        Timestamp result = null;
        Timestamp min_time = new Timestamp(cal_ref_time.getTime().getTime());
        PreparedStatement stmt = con.prepareStatement(SQL_LESSON_GET_MAX_DATE);
        int index = 1;
        stmt.setLong(index++, this.ils_itm_id);
        stmt.setTimestamp(index++, min_time);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()){
            result = rs.getTimestamp(1);
        }
        rs.close();
        stmt.close();
        return result;
    }
    
    public Timestamp getMinDateOfClass(Connection con) throws SQLException, cwSysMessage{
        Timestamp timestamp = null;
        Timestamp min_time = new Timestamp(cal_ref_time.getTime().getTime());
        PreparedStatement stmt = con.prepareStatement(SQL_LESSON_GET_MIN_DATE);
        int index = 1;
        stmt.setLong(index++, this.ils_itm_id);
        stmt.setTimestamp(index++, min_time);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()){
            timestamp = rs.getTimestamp(1);
        }
        rs.close();
        stmt.close();
        return timestamp;
    }
    
    public int getMaxDay(Connection con) throws SQLException, cwSysMessage{
        int result = 0;
        PreparedStatement stmt = con.prepareStatement(SQL_GET_MAX_DAY);
        int index = 1;
        stmt.setLong(index++, this.ils_itm_id);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()){
            result = rs.getInt("maxday");
        }
        rs.close();
        stmt.close();
        return result;
    }
    
    public Timestamp DateYunSuan(Timestamp time, int num){
    	Date dt = new Date();
		dt = time;
		Calendar cal=Calendar.getInstance();
		cal.setTime(dt);
		cal.add(Calendar.MINUTE, num);
		return new Timestamp(cal.getTime().getTime());
    	
    }
    
	public String getListAsXML(Connection con, long itm_id)
		throws SQLException {
		StringBuffer xmlBuffer = new StringBuffer();
		long rowCount = getLessonCountByItmId(con ,itm_id);

		xmlBuffer
			.append("<itm_lessons itm_id=\"")
			.append(itm_id)
			.append("\" count=\"")
			.append(rowCount)
			.append("\">");
		if (rowCount > 0) {
			PreparedStatement stmt = con.prepareStatement(SQL_LESSON_SELECT);
			stmt.setLong(1, itm_id);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				
				xmlBuffer
					.append("<lesson ils_id=\"")
					.append(rs.getInt("ils_id"))
					.append("\" day=\"")
					.append(rs.getInt("ils_day"))
					.append("\" ils_date=\"")
					.append(rs.getTimestamp("ils_date") == null ? "" :  cwUtils.format2simple(rs.getTimestamp("ils_date")))
					.append("\" ils_qiandao=\"")
					.append(rs.getInt("ils_qiandao"))
					.append("\" ils_qiandao_chidao=\"")
					.append(rs.getTimestamp("ils_qiandao_chidao_time"))
					.append("\" ils_qiandao_queqin=\"")
					.append(rs.getTimestamp("ils_qiandao_queqin_time"))
					.append("\" ils_qiandao_youxiaoqi=\"")
					.append(rs.getTimestamp("ils_qiandao_youxiaoqi_time"))
					.append("\" start_time=\"")
					.append(rs.getTimestamp("ils_start_time"))
					.append("\" end_time=\"")
					.append(rs.getTimestamp("ils_end_time"))
					.append("\" >")
					.append("<title>")
					.append(cwUtils.esc4XML(rs.getString("ils_title")))
					.append("</title><last_updated timestamp=\"")
					.append(rs.getTimestamp("ils_update_timestamp"))
					.append("\" usr_id=\"")
					.append(rs.getString("ils_update_usr_id"))
					.append("\" />")
					.append("</lesson>");
			}
	        stmt.close();
		}
		xmlBuffer.append("</itm_lessons>");
		return xmlBuffer.toString();
	}

	public long getLessonCountByItmId(Connection con, long itm_id) throws SQLException {
		long count = 0;
		PreparedStatement stmt = con.prepareStatement(SQL_LESSON_SELECT_COUNT);
		stmt.setLong(1, itm_id);
		ResultSet rs = stmt.executeQuery();
		if (rs.next()) 
			count = rs.getLong(1);
	    rs.close();
	    stmt.close();
		return count;
	}
	
	public String getSingleAsXML(Connection con, long val_ils_id)
		throws SQLException {
		StringBuffer xmlBuffer = new StringBuffer();
		PreparedStatement stmt = con.prepareStatement(SQL_LESSON_SEL_SINGLE);
		stmt.setLong(1, val_ils_id);

		ResultSet rs = stmt.executeQuery();
		if (rs.next()) {
			xmlBuffer.append("<lesson ils_id=\"")
				.append(rs.getInt("ils_id"))
				.append("\" day=\"")
				.append(rs.getInt("ils_day"))
				.append("\" start_time=\"")
				.append(rs.getTimestamp("ils_start_time"))
				.append("\" end_time=\"")
				.append(rs.getTimestamp("ils_end_time"))
				.append("\" ils_date=\"")
				.append(rs.getTimestamp("ils_date"))
				.append("\" ils_qiandao=\"")
				.append(rs.getInt("ils_qiandao"))
				.append("\" ils_qiandao_chidao=\"")
				.append(rs.getInt("ils_qiandao_chidao"))
				.append("\" ils_qiandao_queqin=\"")
				.append(rs.getInt("ils_qiandao_queqin"))
				.append("\" ils_qiandao_youxiaoqi=\"")
				.append(rs.getInt("ils_qiandao_youxiaoqi"))
				.append("\">")
				.append("<title>")
				.append(cwUtils.esc4XML(rs.getString("ils_title")))
				.append("</title><last_updated timestamp=\"")
				.append(rs.getTimestamp("ils_update_timestamp"))
				.append("\" usr_id=\"")
				.append(rs.getString("ils_update_usr_id"))
				.append("\" />")
				.append("<place>").append(cwUtils.esc4XML(rs.getString("ils_place"))).append("</place>")
				.append("</lesson>");
		}
        rs.close();
        stmt.close();
		return xmlBuffer.toString();
	}

	public String getDateListAsXML(Connection con, long val_ils_itm_id)
		throws SQLException, cwSysMessage {
		StringBuffer xmlBuffer = new StringBuffer();
		PreparedStatement stmt = con.prepareStatement(SQL_LESSON_GET_DAY_AND_DATE);
		stmt.setLong(1, val_ils_itm_id);
		ResultSet rs = stmt.executeQuery();
		Calendar c = Calendar.getInstance();
		Timestamp tstamp = null;
		aeItem itm = new aeItem();
		itm.itm_id = val_ils_itm_id;
		itm.get(con);
		xmlBuffer.append("<lesson_date_list>");
		while (rs.next()) {
			tstamp = rs.getTimestamp("day_date");
			c.setTime(tstamp);
			if (c.get(Calendar.YEAR) == cal_ref_time.get(Calendar.YEAR)
				&& c.get(Calendar.MONTH) == cal_ref_time.get(Calendar.MONTH)
                    && c.get(Calendar.DATE) == cal_ref_time.get(Calendar.DATE)) {
				xmlBuffer.append("<lesson_date ils_day=\"")
					.append(rs.getInt("ils_day"))
					.append("\" ")
					.append("org_date=\"")
					.append("")
					.append("\" ");
			} else {
				xmlBuffer.append("<lesson_date ils_day=\"")
					.append(rs.getInt("ils_day"))
					.append("\" ")
					.append("org_date=\"")
					.append(tstamp)
					.append("\" ");

			}
            if(this.isExistInstructorByDay(con, val_ils_itm_id, rs.getInt("ils_day"))){
                xmlBuffer.append("required=\"true\"");
            } else {
                xmlBuffer.append("required=\"false\"");
            }
            xmlBuffer.append(" />");
			//xmlBuffer.append("<lesson_date ils_day=\"").append(rs.getInt("ils_day")).append("\" ").append("year=\"").append(c.get(Calendar.YEAR)).append("\" ").append("month=\"").append(c.get(Calendar.MONTH)).append("\" ").append("day=\"").append(c.get(Calendar.DATE)).append("\" ").append("org_date=\"").append(tstamp).append("\" />");
		}
		xmlBuffer.append("</lesson_date_list>");
        rs.close();
        stmt.close();
		return xmlBuffer.toString();
	}
    
    public static long getIlsItmId(Connection con, long ils_id)
    throws SQLException, qdbException ,cwSysMessage {
        long ils_itm_id = 0;
		PreparedStatement stmt = con.prepareStatement(SQL_GET_ILS_ITM_ID);
        stmt.setLong(1, ils_id);
        ResultSet rs = stmt.executeQuery();
        if(rs.next())
            ils_itm_id = rs.getLong("ils_itm_id");
        else
            //throw new SQLException("Cannot find Item, itm_id = " + itm_id);
			throw new cwSysMessage(aeUtils.MSG_REC_NOT_FOUND,"Item ID = " + ils_itm_id);
        stmt.close();
        return ils_itm_id;
    }
    
	public static void checkTimeConflict(Connection con,long instr_ent_id,long ils_id)
		throws SQLException, cwSysMessage {
        Timestamp ils_start_time = null;
        Timestamp ils_end_time = null;
		PreparedStatement pstmt = 
			con.prepareStatement(SQL_GET_TIME_BY_ILS_ID);
        pstmt.setLong(1,ils_id);
        ResultSet rs = pstmt.executeQuery();
        if(rs.next()){
            ils_start_time = rs.getTimestamp(1);
            ils_end_time = rs.getTimestamp(2);
        }
        pstmt.close();
		final String sql = 
			"select itm_title,itm_code,usr_display_bil,ils_title,ils_day,ils_start_time,ils_end_time " 
		  + "from aeItemLesson,aeItem,RegUser " 
		  +	"where ils_itm_id = itm_id " 
		  +	"and ils_id in (" 
		  +	"select ili_ils_id from aeItemLessonInstructor " 
		  +	"where ili_usr_ent_id = ? " 
		  +	"and (ils_start_time between ? and ? or ils_end_time between ? and ?)" 
		  +	") "
		  + "and itm_create_usr_id = usr_id ";
        pstmt = con.prepareStatement(sql);
        pstmt.setLong(1,instr_ent_id);
        pstmt.setTimestamp(2,ils_start_time);
        pstmt.setTimestamp(3,ils_end_time);
		pstmt.setTimestamp(4,ils_start_time);
		pstmt.setTimestamp(5,ils_end_time);
        rs = pstmt.executeQuery();
        StringBuffer conflict_buf = new StringBuffer();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh-mm");
        while(rs.next()){
			conflict_buf.append("<br/>"); 
        	conflict_buf.append(rs.getString("itm_title"))
        				.append("(")
        				.append(rs.getString("itm_code"))
        				.append("); ")
        				.append(rs.getString("usr_display_bil"))
						.append("; ")
        				.append(rs.getString("ils_title"))
        				.append("(")
        				.append(dateFormat.format(rs.getTimestamp("ils_start_time")))
        				.append(" - ")
        				.append(dateFormat.format(rs.getTimestamp("ils_end_time")))
        				.append(")");
        }
        if(conflict_buf.length()>0){
			throw new cwSysMessage("CND003",conflict_buf.toString());
        }
        pstmt.close();
    }
    
    public boolean checkTimeConflictByItem(Connection con) throws SQLException {
        boolean result = false;
        PreparedStatement stmt = con.prepareStatement(SQL_LESSON_CHK_TIME_CONFLICT_BY_ITM);
        stmt.setLong(1, this.ils_itm_id);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()){
            if(rs.getInt(1) > 0)
                result = true;
        }               
        rs.close();
        stmt.close();                      
        return result;
    }
    
    public Hashtable[] getLessonDayDateByItm(Connection con, long itm_id) throws SQLException {
    	Hashtable[] result = null; 
    	PreparedStatement stmt = null;
    	ResultSet rs = null;
    	int index = 1;
    	try {
    		stmt = con.prepareStatement(SQL_LESSON_GET_DAY_DATE_DETAIL_BY_ITM);
    		stmt.setLong(index++, itm_id);
    		rs = stmt.executeQuery();
    		result = new Hashtable[] {new Hashtable(), new Hashtable(), new Hashtable()};
	    	while (rs.next()) {
	    		Long ils_id = new Long(rs.getLong("ils_id"));
	    		if (result[0] != null && result[0].containsKey(new Long(rs.getLong("ils_day")))) {
	    			((ArrayList) result[0].get(new Long(rs.getLong("ils_day")))).add(ils_id);
	    		} else {
	    			ArrayList tmplst = new ArrayList();
	    			tmplst.add(ils_id);
	    			result[0].put(new Long(rs.getLong("ils_day")), tmplst);
	    		}
	    		result[1].put(ils_id, rs.getTimestamp("ils_start_time"));
	    		result[2].put(ils_id, rs.getTimestamp("ils_end_time"));
	    	}
    	} finally {
    		if (rs != null) rs.close();
    		if (stmt != null) stmt.close();
    	}
    	return result;
    }
    
    public static final String SQL_CHK_DATE_SETTED = "select ils_id from aeItemLesson where ils_itm_id = ? and ils_start_time > ?"; 
    public boolean isDateSetted(Connection con) throws SQLException {
        boolean result = false;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = con.prepareStatement(SQL_CHK_DATE_SETTED);
            pstmt.setLong(1, ils_itm_id);
            pstmt.setTimestamp(2, new Timestamp(cal_ref_time_bound.getTime().getTime()));
            rs = pstmt.executeQuery();
            if (rs.next()) {
                result = true;
            }
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
        }
        return result;
    }
    
/*    public static final String SQL_GET_APPN_ITM_ID_WITH_TIMETABLE = "select app_itm_id from aeApplication" +
    		" where app_ent_id = ? and app_status in (?, ?)" +
    		" and exists (select ils_id from aeItemLesson where ils_itm_id = app_itm_id and ils_start_time > ?)" +
    		" union" +
    		" select app_itm_id from aeApplication, aeAttendance, aeAttendanceStatus" +
    		" where app_ent_id = ?" +
    		" and exists (select ils_id from aeItemLesson where ils_itm_id = app_itm_id and ils_start_time > ?)" +
    		" and att_app_id = app_id  and att_ats_id = ats_id  and ats_cov_status <> ?";

    public static final String SQL_GET_APPN_ITM_ID_WITHOUT_TIMETABLE = "select app_itm_id from aeApplication" +
			" where app_ent_id = ? and app_status in (?, ?)" +
			" and not exists (select ils_id from aeItemLesson where ils_itm_id = app_itm_id and ils_start_time > ?)" +
			" union" +
			" select app_itm_id from aeApplication, aeAttendance, aeAttendanceStatus" +
			" where app_ent_id = ?" +
			" and not exists (select ils_id from aeItemLesson where ils_itm_id = app_itm_id and ils_start_time > ?)" +
			" and att_app_id = app_id  and att_ats_id = ats_id  and ats_cov_status <> ?";
    
    public long[] getActiveAppnItmid(Connection con, long usr_ent_id, boolean hastimetable) throws SQLException {
        List tmp_ids = new ArrayList();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        if (hastimetable) {
            pstmt = con.prepareStatement(SQL_GET_APPN_ITM_ID_WITH_TIMETABLE);
        } else {
            pstmt = con.prepareStatement(SQL_GET_APPN_ITM_ID_WITHOUT_TIMETABLE);
        }
        int index = 1;
        pstmt.setLong(index++, usr_ent_id);
        pstmt.setString(index++, aeApplication.PENDING);
        pstmt.setString(index++, aeApplication.WAITING);
        pstmt.setTimestamp(index++, new Timestamp(cal_ref_time_bound.getTime().getTime()));
        pstmt.setLong(index++, usr_ent_id);
        pstmt.setTimestamp(index++, new Timestamp(cal_ref_time_bound.getTime().getTime()));
        pstmt.setString(index++, "W");
        rs = pstmt.executeQuery();
        while (rs.next()) {
            tmp_ids.add(new Long(rs.getLong(1)));
        }
        if (rs != null) rs.close();
        if (pstmt != null) pstmt.close();
        if (tmp_ids != null && tmp_ids.size() > 0) {
            long[] ids = new long[tmp_ids.size()];
            for (int i = 0; i < tmp_ids.size(); i++) {
                ids[i] = ((Long) tmp_ids.get(i)).longValue();
            }
            return ids;
        } else {
            return new long[0];
        }
    }*/

		private static final String SQL_GET_ACT_APPN_ITM_ID = "select distinct app_itm_id, " +
				" (select count(*) from aeItemLesson where ils_itm_id = app_itm_id and ils_start_time > ?)  num" +
				" from(" +
				" select app_itm_id from aeApplication" +
				" where app_ent_id = ?" +
				" and app_status in (?, ?)" +
				" union" +
				" select app_itm_id" +
				" from aeApplication, aeAttendance, aeAttendanceStatus" +
				" where app_ent_id = ?" +
				" and att_app_id = app_id  and att_ats_id = ats_id  and ats_cov_status <> ?" +
				" )  tmp where app_itm_id <> ?";
    
		public void getActiveAppnItmid(Connection con, long usr_ent_id, long itm_id, List ttb_itm_ids) throws SQLException {
			List tmp_ids = new ArrayList();
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			pstmt = con.prepareStatement(SQL_GET_ACT_APPN_ITM_ID);
			int index = 1;
			pstmt.setTimestamp(index++, new Timestamp(cal_ref_time_bound.getTime().getTime()));
			pstmt.setLong(index++, usr_ent_id);
			pstmt.setString(index++, aeApplication.PENDING);
			pstmt.setString(index++, aeApplication.WAITING);
			pstmt.setLong(index++, usr_ent_id);
			pstmt.setString(index++, "W");
			pstmt.setLong(index++, itm_id);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				if (rs.getLong("num") > 0) {
					ttb_itm_ids.add(new Long(rs.getLong("app_itm_id")));
				}
			}
			if (rs != null) rs.close();
			if (pstmt != null) pstmt.close();
		}
		
		public String getAeItemLessonXML(Connection con, long itm_id) throws SQLException{
			String sql = " select ils_id, ils_title from aeItemLesson where ils_itm_id = ? ";
			String xml = "<ils_list>";
			PreparedStatement stmt = null;
			ResultSet rs = null;
			try{
				stmt = con.prepareStatement(sql);
				stmt.setLong(1, itm_id);
				rs = stmt.executeQuery();
				while(rs.next()){
					xml += "<ils><ils_id>" + rs.getLong("ils_id") + "</ils_id>";
					xml += "<ils_title>" + rs.getString("ils_title") + "</ils_title></ils>";
				}
			} finally {
				cwSQL.cleanUp(rs, stmt);
			}
			return xml + "</ils_list>";
		}
		
	}
