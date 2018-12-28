package com.cw.wizbank.ae;

import java.util.*;
import java.sql.*;
import com.cw.wizbank.qdb.*;

// extract different parts of a sql timestamp (2000.06.07 by wai)
public class aeTimeField {
    private Calendar field_date_;
    private int field_year_;
    private int field_month_;
    private int field_day_;
    private int field_hour_;
    private int field_minute_;
    private int field_second_;
    private int field_am_pm_;
    
    
    public aeTimeField(Timestamp in_ts) {
        field_date_ = Calendar.getInstance();
        setTime(in_ts);
    }
    
    public void setTime(Timestamp in_ts) {
        field_date_.setTime(in_ts);
        field_year_   = field_date_.get(Calendar.YEAR);
        field_month_  = field_date_.get(Calendar.MONTH) + 1;
        field_day_    = field_date_.get(Calendar.DAY_OF_MONTH);
        field_hour_   = field_date_.get(Calendar.HOUR_OF_DAY);
        field_minute_ = field_date_.get(Calendar.MINUTE);
        field_second_ = field_date_.get(Calendar.SECOND);
        field_am_pm_  = field_date_.get(Calendar.AM_PM);
    }
    public int getYear() {
        return field_year_;
    }
    public int getMonth() {
        return field_month_;
    }
    public int getDay() {
        return field_day_;
    }
    public int getHour() {
        return field_hour_;
    }
    public int getMinute() {
        return field_minute_;
    }
    public int getSecond() {
        return field_second_;
    }
    public int getNoon() {
        return field_am_pm_;
    }
    
    public String asXML(String fld_name) {
        StringBuffer xmlBuf = new StringBuffer(200);
        xmlBuf.append("<").append(fld_name);
        xmlBuf.append(" year=\"").append(getYear()).append("\"");
        xmlBuf.append(" month=\"").append(getMonth()).append("\"");
        xmlBuf.append(" day=\"").append(getDay()).append("\"");
        xmlBuf.append(" hour=\"").append(getHour()).append("\"");
        xmlBuf.append(" minute=\"").append(getMinute()).append("\"");
        xmlBuf.append(" second=\"").append(getSecond()).append("\"");
        xmlBuf.append("/>").append(dbUtils.NEWL);
        
        String xml = new String(xmlBuf);
        return xml;
    }
    
    
    public static String curTimeAsXML(Connection con) throws SQLException {
        try {
            aeTimeField curTime = new aeTimeField(dbUtils.getTime(con));
            
            return curTime.asXML("cur_time");
        }
        catch(qdbException e) {
            throw new SQLException(e.getMessage());
        }
    }
    
/*    public void addDay(int no_of_day){
        field_date_.add(Calendar.DATE, no_of_day);
    }
*/
}