package com.cw.wizbank.course;

import java.util.Vector;
import java.util.Hashtable;
import java.sql.*;
import java.io.*;

import com.cw.wizbank.util.*;
import com.cw.wizbank.ae.aeAttendance;
import com.cw.wizbank.db.DbCourseCriteria;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.qdb.qdbException;

public class UploadAttdRate
{
	private Vector failVt = new Vector();
	private Vector missingFieldVt = new Vector();
	private Vector duplicatedVt = new Vector();
	private Vector noSuchApplicationVt = new Vector();
	private Vector errLearnerNameVt = new Vector();
	private Vector illogicRateRangeVt = new Vector();
	private Vector edundantColumnVt = new Vector();
	private Vector notEnrolledVt = new Vector();
	private File srcFile;
	private String enc;
	private long itm_id;
	private Connection con;
	private loginProfile prof;
	
	private static final int uploadColumn = 4;
	private static final int STATUS_ENROLLED = 2; 
	public static final String SESS_UPLOADED_SRC_FILE = "src_file";
	public static final String MSG_STATUS="STATUS";
	public static final String MSG_SAVE_ATTRATE_SUCCESS="ENT002";
	public static final String SMSG_ULG_INVALID_FILE  = "ULG001";
	
		
	UploadAttdRate(Connection con,loginProfile prof,long itm_id,File srcFile,String enc)
	{	
		this.con = con;
		this.itm_id=itm_id;
		this.srcFile=srcFile;
		this.enc=enc;
		this.prof=prof;
	}
	
	private void check(Vector uploadRecords) throws cwException, SQLException
	{
		for (int lineNum=1;lineNum<uploadRecords.size();lineNum++)
		{
			String type = new String("");
			boolean bFail = false;
			int recordNum =	((String[])uploadRecords.elementAt(lineNum)).length;
			String[] RecLine=new String[recordNum];
			RecLine=(String[])uploadRecords.elementAt(lineNum);
			aeAttendance attendance = new aeAttendance();
			attendance.att_itm_id = this.itm_id;
			if (recordNum>uploadColumn)
			{
				edundantColumnVt.addElement(new Long(lineNum));
				bFail=true;
				type = "edundantColumn";
			}else{
				try
				{
					long app_id=new Long(RecLine[0]).longValue();
					attendance.att_app_id = app_id;
					boolean bSuccessGet=attendance.get(con);
					if(!bSuccessGet)
					{
						noSuchApplicationVt.addElement(new Long(lineNum));
						bFail=true;
						type = "noSuchApplication";
					}/*else if(attendance.att_ats_id!=STATUS_ENROLLED)
					{
						notEnrolledVt.addElement(new Long(lineNum));
						bFail=true;
						type = "notEnrolled";
					}*/
					else{
						try
						{
							double rate = new Double(RecLine[2]).doubleValue();
							if(!RecLine[1].equals(attendance.usr.usr_display_bil))
							{
								errLearnerNameVt.addElement(new Long(lineNum));
								bFail=true;
								type = "nameErr";
							}else if(rate>100||rate<0)
							{
								illogicRateRangeVt.addElement(new Long(lineNum));
								bFail=true;
								type = "illogicRateRange";
							}
						}
						catch(NumberFormatException e)
						{
							if(!(RecLine[2].equals("-")||RecLine[2].equals("")))
							{
								illogicRateRangeVt.addElement(new Long(lineNum));
								bFail=true;
								type = "illogicRateRange";
							}
						}
					}
				}catch(NumberFormatException e){
				noSuchApplicationVt.addElement(new Long(lineNum));
				bFail=true;
				type = "noSuchApplication";
				}
			}
			if(bFail)  
			{
				Hashtable failHt = new Hashtable();
				failHt.put("type",type);
				failHt.put("lineNum",new Long(lineNum) );
				failVt.addElement(failHt);
			}
				
		}
	}
	
	public String updatePreview()throws cwException, cwSysMessage,SQLException
	{
		Vector uploadRecords = new Vector();
		String delimiter = new String("\t");
		uploadRecords = cwUtils.splitFileToVector(srcFile,enc,delimiter);
		
		check(uploadRecords);
		
		StringBuffer xml = new StringBuffer("");
		xml.append("<itm_id>").append(itm_id).append("</itm_id>");
		xml.append("<fail_list>");
		for(int i=0;i<failVt.size();i++)
		{
			String lineNum = ((Hashtable)failVt.elementAt(i)).get("lineNum").toString();
			String type = ((Hashtable)failVt.elementAt(i)).get("type").toString();
			
			xml.append("<line num=\"").append(lineNum).append("\" type=\"").append(type).append("\"/>");
		}
		xml.append("</fail_list>");
		
		xml.append("<app_id_fail_list>");
		for(int i=0;i<noSuchApplicationVt.size();i++)
		{
			xml.append("<line num=\"").append(noSuchApplicationVt.elementAt(i).toString()).append("\"/>");
		}
		xml.append("</app_id_fail_list>");
		
		xml.append("<err_name_list>");
		for(int i=0;i<errLearnerNameVt.size();i++)
		{
			xml.append("<line num=\"").append(errLearnerNameVt.elementAt(i).toString()).append("\"/>");
		}
		xml.append("</err_name_list>");
		
		xml.append("<fail_rate_list>");
		for(int i=0;i<illogicRateRangeVt.size();i++)
		{
			xml.append("<line num=\"").append(illogicRateRangeVt.elementAt(i).toString()).append("\"/>");
		}
		xml.append("</fail_rate_list>");
		
		xml.append("<edundantColumn_list>");
		for(int i=0;i<edundantColumnVt.size();i++)
		{
			xml.append("<line num=\"").append(edundantColumnVt.elementAt(i).toString()).append("\"/>");
		}
		xml.append("</edundantColumn_list>");
		
		xml.append("<notEnrolled_list>");
		for(int i=0;i<notEnrolledVt.size();i++)
		{
			xml.append("<line num=\"").append(notEnrolledVt.elementAt(i).toString()).append("\"/>");
		}
		xml.append("</notEnrolled_list>");
		
		xml.append("<records>");
		for (int i=0;i<uploadRecords.size();i++)
		{
			
			int recordNum =	((String[])uploadRecords.elementAt(i)).length;
			String[] temp=new String[recordNum];
			temp=(String[])uploadRecords.elementAt(i);
			
			xml.append("<record>");
			for(int j=0;j<recordNum;j++)
			{
				xml.append("<column>");
				xml.append(dbUtils.esc4XML(temp[j]));
				xml.append("</column>");
			}
			xml.append("</record>");
		}
		xml.append("</records>");
		return xml.toString();	
	}
	
	public void cook()throws cwException,cwSysMessage,SQLException,qdbException, qdbErrMessage
	{
		CourseCriteria ccr = new CourseCriteria();
		Vector uploadRecords = new Vector();
		String delimiter = new String("\t");
		uploadRecords = cwUtils.splitFileToVector(srcFile,enc,delimiter);
		aeAttendance attendance = new aeAttendance();
		attendance.att_itm_id = this.itm_id;
		attendance.setAllAttRateNull(con);
		attendance.setAllAttRateRemarkNull(con);	
		
		attendance.att_ats_id = STATUS_ENROLLED;
		
		for(int i=1;i<uploadRecords.size();i++)
		{
			String[] RecLine=(String[])uploadRecords.elementAt(i);
			long att_app_id= new Long(RecLine[0]).longValue();
			String att_rate =RecLine[2];
			if (att_rate.equals("-")) att_rate=null;
			String att_rate_remark = RecLine[3];
			//excel 文件如果有回车键，则另存为unicode txt 文件时会多出双引号
			if(att_rate_remark.length() > 0 && att_rate_remark.startsWith("\"")){
				att_rate_remark = att_rate_remark.substring(1,att_rate_remark.length()-1);
			}
			if (att_rate_remark.equals("-"))att_rate_remark=null;
			attendance.att_rate = att_rate;
			attendance.att_rate_remark = att_rate_remark;
			attendance.att_app_id = att_app_id;
			attendance.updAttRateAndRateRemark(con);
			String upd_usr_id = prof.usr_id;
			//attendance.updAttTimestamp(con,upd_usr_id);
			attendance.updAttUpdTimestamp(con,upd_usr_id); 
		}
		DbCourseCriteria DbCcr = new DbCourseCriteria();
		DbCcr.ccr_itm_id =  itm_id;
		DbCcr.ccr_type ="COMPLETION";
		DbCcr.getCcrIdByItmNType(con);
		ccr.setFromMarkingSchema(con,prof,DbCcr.ccr_id,false,false, false, false);
		con.commit();
	}
}