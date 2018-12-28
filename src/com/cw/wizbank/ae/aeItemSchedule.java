package com.cw.wizbank.ae;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.Calendar;


import java.sql.*;

import com.cw.wizbank.ae.db.view.ViewItemSchedule;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.db.DbTrainingCenter;
import com.cw.wizbank.db.view.ViewTrainingCenter;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.utils.CommonLog;

public class aeItemSchedule
{
	private final static int dayNum=7;
	private int weekCount=0;
	private Connection con = null;
	private int year;
	private int month;
	//private int ste_ent_id;
	
	public aeItemSchedule(Connection con) throws SQLException
	{
		if (con==null)
			throw new SQLException("connection not available");
		else
			this.con=con;
	}
	
	public aeItemSchedule(Connection con,int year,int month) throws SQLException,IllegalArgumentException
	{
		if(con==null)
			throw new SQLException("connection not available");
		else if((year<1) || (year>9999) || (month<1) || (month >12))
			throw new IllegalArgumentException("(year>9999 or <1) or (month>12 or <1)");
		else
		{
			this.con=con;
			this.year=year;
			this.month=month; 
		}
	}
	
	public void setYear(int year)
	{
		if (year<1 || year>9999)
			throw new IllegalArgumentException("Illegal year!!! ");
		else
			this.year=year;
	}
	
	public void setMonth(int month)
	{
		if(month<1 || month>12)
			throw new IllegalArgumentException("Illegal month!!!");
		else
			this.month=month;
	}
	
	private Calendar getMonthStart()
	{
		Calendar monthStart =Calendar.getInstance();
		monthStart.set(this.year,this.month-1,1,0,0,0);
		return monthStart;	
	}
	
	private Calendar getMonthEnd()
	{
		Calendar monthEnd =Calendar.getInstance();
		monthEnd.set(this.year,this.month-1,getDaysOfMonth(this.year,this.month),0,0,0);
		return monthEnd;
	}
	
	private Calendar getStart()
	{
		Calendar monthStart = getMonthStart();
		Calendar start=(Calendar)monthStart.clone();
		//int increment =-monthStart.getTime().getDay();
		int increment = Calendar.SUNDAY - monthStart.get(Calendar.DAY_OF_WEEK);
		start.add(Calendar.DATE,increment);
		return start;
	}
	
	private Calendar getEnd()
	{
		Calendar monthEnd = getMonthEnd();
		Calendar end=(Calendar)monthEnd.clone();
		int increment =Calendar.SATURDAY -monthEnd.get(Calendar.DAY_OF_WEEK);//monthEnd.getTime().getDay();
		end.add(Calendar.DATE,increment);
		return end;
	}
	private int getWeeksOfMonth(int year,int month)
	{
		Calendar tmp = Calendar.getInstance();
		tmp.set(Calendar.YEAR,year);
		tmp.set(Calendar.MONTH,month-1);
		return tmp.getActualMaximum(Calendar.WEEK_OF_MONTH);	
	}
	
	private int getDaysOfMonth(int year,int month)
	{
		Calendar tmp = Calendar.getInstance();
		tmp.set(Calendar.YEAR,year);
		tmp.set(Calendar.MONTH,month-1);
		return tmp.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
	
	private String[] getDays(Calendar start,Calendar end) 
	{
		Vector dateList = new Vector();
		Calendar day = Calendar.getInstance();
		day.setTime(start.getTime());
		int dayCount=0;
		while (!(day.after(end)))
		{
			Timestamp ts=new Timestamp(day.getTime().getTime());
			ts.setNanos(0);
			dateList.addElement(ts.toString());
			day.add(Calendar.DATE, 1);
			dayCount++;
		}	
		this.weekCount=dayCount/7;
		String[] resultArray = new String[dateList.size()];
		return (String[])dateList.toArray(resultArray);
	}
	
	public String getSchduleXML(int year,int month,loginProfile prof, boolean tc_enabled, WizbiniLoader wizbini) throws SQLException
	{
		setYear(year);
		setMonth(month);
		Calendar monthStart=getMonthStart();
		Calendar monthEnd=getMonthEnd();
		Calendar start=getStart();
		Calendar end=getEnd();
		int weekCount=getWeeksOfMonth(year,month);
		String[] dateList=getDays(start,end);
		List tcList = null;
		if (tc_enabled) {
			tcList= ViewTrainingCenter.getEffTrainingCenters(this.con,prof.root_ent_id, prof.usr_ent_id, prof.current_role,  wizbini);
		} else {
			tcList = new ArrayList();
			DbTrainingCenter dbTcr = new DbTrainingCenter(DbTrainingCenter.getSuperTcId(con, prof.root_ent_id));
			dbTcr.get(con);
			tcList.add(dbTcr);
		}
		String outXML = new String();
		try
		{
			outXML=ScheduleAsXML(weekCount,year,month,tcList,dateList, tc_enabled);
		}
		catch (SQLException e)
		{
			//e.printStackTrace();
			CommonLog.error(e.getMessage(),e);
		}
		return outXML;
	}
	
	private String ScheduleAsXML(int weekCount,int year,int month,List tcList,String[] dateList, boolean tc_enabled) throws SQLException
		{
			Timestamp monthStart=new Timestamp(getMonthStart().getTime().getTime());
			Timestamp monthEnd=new Timestamp(getMonthEnd().getTime().getTime());
			monthStart.setNanos(0);
			monthEnd.setNanos(0);
			StringBuffer result=new StringBuffer("<monthly_item_schedule year=\"");
			result.append(year).append("\" month=\"").append(month);
			result.append("\">");
			for(int i=0;i<weekCount;i++)
			{	
				result.append("<weekly_schedule>");
				result.append("<week>");
				int dateCount=dayNum*i;
				for(int j=dateCount;j<dateCount+dayNum;j++)
				{
					result.append("<day>");
					result.append(dateList[j]);
					result.append("</day>");
				}
				result.append("</week>");
				DbTrainingCenter tc = null;
				for(int tcCount=0,size=tcList.size();tcCount<size;tcCount++)
				{
					tc = (DbTrainingCenter)tcList.get(tcCount);
					long tc_id = tc.getTcr_id();
					String tc_code = tc.getTcr_code();
					String tc_title = tc.getTcr_title();
					result.append("<training_center id=\"").append(tc_id).append("\">");
					result.append("<code>").append(cwUtils.esc4XML(tc_code)).append("</code>");
					result.append("<title>").append(cwUtils.esc4XML(tc_title)).append("</title>");
				
					for(int j=dateCount;j<dateCount+dayNum;j++)
					{
						result.append("<daily_schedule>");
						
						Timestamp ts=Timestamp.valueOf(dateList[j]);
						if(!ts.before(monthStart)  && !ts.after( monthEnd))
						{
							ViewItemSchedule vis = new ViewItemSchedule(con);
							//if tc_enabled is false.no filter by tcr_id
							if (!tc_enabled) {
								tc_id = 0;
							}
							Vector itmSch = vis.getDailySchedule(dateList[j],tc_id);
							if(itmSch.size()!=0)
							{
								result.append("<item_list>");
								for (int k=0;k<itmSch.size();k++)
								{
									HashMap record = (HashMap)itmSch.elementAt(k);
									long itm_id = ((Long)record.get("itm_id")).longValue();
									String itm_title = (String)record.get("itm_title");
									String itm_life_status = (String)record.get("itm_life_status");
									result.append("<item id=\"").append(itm_id);
									result.append("\" life_status=\"").append(itm_life_status).append("\">");
									result.append("<title>").append(cwUtils.esc4XML(itm_title)).append("</title>");
									result.append("</item>");
								}
							result.append("</item_list>");
							}
						}
							result.append("</daily_schedule>");
					}
					result.append("</training_center>");  
				}
				result.append("</weekly_schedule>");
			
			}
			result.append("</monthly_item_schedule>");
			return result.toString();
		}
}