
package com.cwn.wizbank.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang.StringUtils;

/**
 * 说明:
 * @version 1.0
 */
public class DateUtil {

	/**
	 * @param yyyy-MM-dd
	 * @return
	 */
	public static final String patternA = "yyyy-MM-dd";
	/**
	 * @param yyyyMMdd
	 * @return
	 */
	public static final String patternB = "yyyyMMdd";
	/**
	 * @param yyyy-MM-dd HH-mm-ss
	 * @return
	 */
	public static final String patternC = "yyyy-MM-dd HH-mm-ss";
	/**
	 * @param yyyy:MM:dd HH:mm:ss
	 * @return
	 */
	public static final String patternD = "yyyy:MM:dd HH:mm:ss";
	/**
	 * @param yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static final String patternE = "yyyy-MM-dd HH:mm:ss";
	
	/**
	 * @param yyyyMMddHHmmss
	 * @return
	 */
	public static final String patternF = "yyyyMMddHHmmss";
	
	public static final String patternG = "yyyy";
	
	public static final String patternH = "dd/MM/yyyy";
	
	private static DateUtil instance;

	public static DateUtil getInstance() {
		if (instance == null) {
			instance = new DateUtil();
		}
		return instance;
	}
	/**
	 * 格式化日期为yyyy-MM-dd
	 * @param date
	 * @return
	 */
	public String formateDate(Date date){
		return dateToString(date,patternA);
	}
	/**
	 * @param 取当天日期
	 * @return
	 */
    public Date getDate() {
        return Calendar.getInstance().getTime();
    }
	/**
	 * @param 取指定年月日的日期,格式为yyyy-MM-dd,HH-mm-ss 00-00-00
	 * @return
	 */
    public Date getDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month-1, day,0,0,0);
        return cal.getTime();
        
    }
	/**
	 * @param 取指定年,月,日,小时,分,秒的时间
	 * @return
	 */
    public Date getDate(int year,int month,int date,int hour,int mintue,int second)
	{
		Calendar cal=Calendar.getInstance();
		cal.set(Calendar.YEAR,year);
		cal.set(Calendar.MONTH,month-1);
		cal.set(Calendar.DATE,date);
		cal.set(Calendar.HOUR_OF_DAY,hour);
		cal.set(Calendar.MINUTE,mintue);
		cal.set(Calendar.SECOND,second);
		return cal.getTime();
	}


	/**
	 * @param days=n n为-,则取n天前,n为+,则取n天后的日期
	 * @param date
	 * @param days
	 * @return
	 */
	public Date getSomeDaysBeforeAfter(Date date, int days){
		GregorianCalendar gc =new GregorianCalendar();
		gc.setTime(date);
		gc.add(5, days);
		gc.set(gc.get(Calendar.YEAR),gc.get(Calendar.MONTH),gc.get(Calendar.DATE));
		return gc.getTime();
	}
	/**
	 * @param 取指定日期年份
	 * @return
	 */
	public int getDateYear(Date date){
		
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.YEAR);
	}
	/**
	 * @param 取指定日期月份
	 * @return
	 */
	public int getDateMonth(Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.MONTH)+1;
	}
	/**
	 * @param 取指定日期日份
	 * @return
	 */
	public int getDateDay(Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.DATE);
	}	
	/**
	 * @param 取指定日期小时
	 * @return
	 */
	public int getDateHour(Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.HOUR_OF_DAY);
	}
	/**
	 * @param 取指定日期分钟
	 * @return
	 */
	public int getDateMinute(Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.MINUTE);
	}
	/**
	 * @param 取指定日期的第二天的开始时间,小时,分,秒为00:00:00
	 * @return
	 */
    public Date getNextDayStartTime(Date date) {
    	Calendar c = Calendar.getInstance();
    	c.setTime(date);
    	return getNextDayStart(c.get(Calendar.YEAR),
    			c.get(Calendar.MONTH)+1, c.get(Calendar.DATE));
    }


	/**
	 * @param 取指定年,月,日的下一日的开始时间,小时,分,秒为00:00:00
	 * @param 主要是用来取跨月份的日期
	 * @return
	 */
    public Date getNextDayStart(int year, int month, int date) {
    	month = month - 1;
    	boolean lastDayOfMonth = false;
    	boolean lastDayOfYear = false;
    	
    	Calendar time = Calendar.getInstance();
    	time.set(year, month, date, 0, 0, 0);
    	Calendar nextMonthFirstDay = Calendar.getInstance();
    	nextMonthFirstDay.set(year, month + 1, 1, 0, 0, 0);
    	
    	if (time.get(Calendar.DAY_OF_YEAR) + 1 == nextMonthFirstDay.get(Calendar.DAY_OF_YEAR))
    		lastDayOfMonth = true;
    	
    	if (time.get(Calendar.DAY_OF_YEAR) == time.getMaximum(Calendar.DATE))
    		lastDayOfYear = true;
    	
    	time.roll(Calendar.DATE, 1);
    	
    	if (lastDayOfMonth)
    		time.roll(Calendar.MONTH, 1);
    	
    	if (lastDayOfYear)
    		time.roll(Calendar.YEAR, 1);
    	
    	
    	return time.getTime();
    }

	/**
	 * @param 取指定日期的下一日的时间
	 * @return
	 */
    public Date nextDate(Date date)
    {
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(date);
    	cal.add(Calendar.DATE,1);
    	return cal.getTime();
    }

	/**
	 * @param 指定日期的下一日的开始时间,小时,分,秒为00:00:00
	 * @return
	 */
    public Date getStartDateNext(Date date)
    {
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(date);
    	cal.add(Calendar.DATE,1);
    	cal.set(Calendar.HOUR_OF_DAY,0);
    	cal.set(Calendar.MINUTE,0);
    	cal.set(Calendar.SECOND,0);
    	return cal.getTime();
    }

	/**
	 * @param 指定日期的开始时间,小时,分,秒为00:00:00
	 * @return
	 */
    public Date getStartDateDay(Date date)
    {
    	Calendar cal=Calendar.getInstance();
    	cal.setTime(date);
    	cal.set(Calendar.HOUR_OF_DAY,0);
    	cal.set(Calendar.MINUTE,0);
    	cal.set(Calendar.SECOND,0);
    	return cal.getTime();
    }

	/**
	 * @param 指定日期的结束时间,小时,分,秒为23:59:59
	 * @return
	 */
    public Date getEndDateDay(Date date)
    {
    	Calendar cal=Calendar.getInstance();
    	cal.setTime(date);
    	cal.set(Calendar.HOUR_OF_DAY,23);
    	cal.set(Calendar.MINUTE,59);
    	cal.set(Calendar.SECOND,59);
    	return cal.getTime();
    }
    
	/**
	 * @param 将指定日期,以指定pattern格式,输出String值
	 * @return
	 */
    public String dateToString(Date date ,String pattern) {
		if (date == null) {
			return "";
		} else {
			SimpleDateFormat format = new SimpleDateFormat(pattern);
			return format.format(date);
		}
    }


	/**
	 * @param 将指定年,月,日的日期转为字符型,格式为yyyy-MM-dd
	 * @return
	 */
    public String dateToString(int year, int month, int day, String pattern) {
    	return dateToString(getDate(year, month, day), pattern);
    }


	/**
	 * @param 将指定字符型日期转为日期型,,格式为指定的pattern
	 * @return
	 */
    public static Date stringToDate(String string, String pattern){
        SimpleDateFormat format = (SimpleDateFormat)DateFormat.getDateInstance();
        format.applyPattern(pattern);
        try {
            return format.parse(string);
        } catch (Exception e) {
            return null;
        }
    }

	/**
	 * @param 将指定字符型日期转为日期型,指定格式为yyyy-MM-dd
	 * @return
	 */
    public static Date stringToDate(String string){
        return stringToDate(string, patternA);
    }

	/**
	 * 获得两个日期之间间隔的天数
	 * @param startDate 起始年月日
	 * @param endDate 结束年月日
	 * @return int
	 */
	public int getDays(Date startDate, Date endDate) {
		int elapsed = 0;
		if(startDate != null && endDate != null){
	        long daterange = endDate.getTime() - startDate.getTime();
	        long time = 1000*60*60*24; //一天的毫秒数
        	elapsed = (int) (daterange/time);
		}
		return elapsed;
   }
	/**
	 * 获取日期间的月数
	 * @param startDate
	 * @param endDate
	 * @return
	 */
   public int getMonths(Date startDate, Date endDate){
       Calendar c = Calendar.getInstance();
       c.setTime(startDate);
       int year1 = c.get(Calendar.YEAR);
       int month1 = c.get(Calendar.MONTH);
       int day1 = c.get(Calendar.DAY_OF_MONTH);

       c.setTime(endDate);
       int year2 = c.get(Calendar.YEAR);
       int month2 = c.get(Calendar.MONTH);
       int day2= c.get(Calendar.DAY_OF_MONTH);
       
       int result;
       if(year1 == year2) {
           result = month2 - month1;
       } else {
           result = 12*(year2 - year1) + month2- month1;
       }
       if(getDays(startDate, endDate) < 31 && day2 < day1){
    	  //result -= 1;
       }
       return result;
   }
   
   public int getWeeks(Date start, Date end){
		int days = getDays(start, end);
		int interval = (int) (days / 7);
		return interval;
   }
	/**
	 * @param date
	 * @param startTime 格式为0800，表示上午8点00分
	 * @param endTime格式为2200
	 * @return
	 */
	public boolean isWorkHour(Date date,String startTime,String endTime){//是否是工作时间
		if(StringUtils.isEmpty(startTime))
			startTime = "0800";
		if(StringUtils.isEmpty(endTime))
			endTime = "2200";
		int start = Integer.parseInt(startTime);
		int end = Integer.parseInt(endTime);
		int hour = getDateHour(date);
		int m = getDateMinute(date);
		String hstr = hour<=9?"0"+hour:hour+"";
		String mstr = m<=9?"0"+m:m+"";
		int dateInt = Integer.parseInt(hstr+mstr);
		if(dateInt>=start&&dateInt<=end){
			return true;
		}
		return false;
	}
	//取日期的当前月第一天
	public Date getMonthFirstDay(Date date){
		return getDate(getDateYear(date), getDateMonth(date), 1);
	}
	
	
	public String secToTime(long time) {  
        String timeStr = null;  
        long hour = 0;  
        long minute = 0;  
        long second = 0;  
        if (time <= 0)  
            return "00:00";  
        else {  
            minute = time / 60;  
            if (minute < 60) {  
                second = time % 60;  
                timeStr = "00:" + unitFormat(minute) + ":" + unitFormat(second);  
            } else {  
                hour = minute / 60;  
                if (hour > 99)  
                    return "99:59:59";  
                minute = minute % 60;  
                second = time - hour * 3600 - minute * 60;  
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);  
            }  
        }  
        return timeStr;  
    } 
	
	public String unitFormat(long i) {  
        String retStr = null;  
        if (i >= 0 && i < 10)  
            retStr = "0" + i;  
        else  
            retStr = "" + i;  
        return retStr;  
    } 
	
	public static Date getCurrentTime(){
		Date date = new Date();
		  try {  
	           SimpleDateFormat format = new SimpleDateFormat(patternE);  
	          String time =  format.format(date);  
	          date = format.parse(time);  
	         System.out.println(date);  
	       } catch (Exception e) {  
	           e.printStackTrace();  
	       }  
		  return date;
	}
	
	/**
	 * @param 取指定年,月,日,小时,分,秒的时间
	 * @return
	 */
    public Date getDate(Date date ,int hour,int mintue,int second)
	{
		Calendar cal=Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY,hour);
		cal.set(Calendar.MINUTE,mintue);
		cal.set(Calendar.SECOND,second);
		cal.set(Calendar.MILLISECOND,0);
		return cal.getTime();
	}
    
	/**
	 * 日期格式是否有效
	 * @param date 日期 ： 20180631
	 * @param pattern 格式：yyyyMMdd
	 * @return false
	 */
	public boolean valid(String date,String pattern) {
		try {
			if(date.length() != pattern.length()) {
				return false;
			}
			SimpleDateFormat format = new SimpleDateFormat(pattern);
			format.setLenient(false);
			format.parse(date);
			return true;
		} catch (ParseException e) {
			return false;
		}
	}
	
	/**
	 * 增加年份
	 * @param date 传入的年份
	 * @param year 增加多少年
	 * @return
	 */
	public Date addDateYear(Date date,int num) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.YEAR, +num);
		return calendar.getTime();
	}


	public static void main(String[] args) {
		//System.out.println(DateUtil.dateToString(DateUtil.getDate(), pattern6));
		//System.out.println(DateUtil.getInstance().dateToString(getMonthFirstDay(new Date()), patternC));
		//String startDate = "2014-02-28 00:00:00";
		//String endDate = "2014-03-03 23:59:00";
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
/*		try {
			System.out.println(DateUtil.getInstance().getWeeks(sdf.parse(startDate),sdf.parse(endDate)));
		} catch (ParseException e) {
		}*/
		DateUtil du = new DateUtil();
		System.out.println(du.getCurrentTime());
		System.out.println(new Date());
	//	System.out.println(DateUtil.getInstance().secToTime(30000));

	}
		
}