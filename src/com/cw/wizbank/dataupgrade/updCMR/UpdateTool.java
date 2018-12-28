package com.cw.wizbank.dataupgrade.updCMR;

import java.io.*;
import java.sql.*;
import java.util.*;

import javax.naming.NamingException;

import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.util.*;
import com.cwn.wizbank.utils.CommonLog;

public class UpdateTool {
    
	public static void main(String[] argv) {
		if(argv.length < 1) {
			System.out.println("Please input the ini file");
		}
		else {
			boolean commit = false;
			Connection con=null;
			try {
				//make database connection
				con = openCon(argv[0]);
                
				//recursive upd
				
				updDatabase(con);
                
				commit = true;
			}
			catch(cwException cwe) {
				System.out.println("CW error: "+ cwe.getMessage());
				commit = false;
			}
			catch(SQLException sqle) {
				System.out.println("Database error: "+ sqle.getMessage());
				commit = false;
			}
            catch(Exception e) {
                CommonLog.error(e.getMessage(),e);
                commit = false;
            }
			finally {
				try {
					if(con != null)
						if(commit) {
							con.commit();
							CommonLog.debug("Transactions committed");
						}
						else {
							con.rollback();
							CommonLog.debug("Transaction rollbacked");
						}
					con.close();
					CommonLog.debug("Connection closed");
				}
				catch(SQLException e) {
					CommonLog.error("Connection close error: "+ e.getMessage(),e);
				}
			}
		}
	}
    
	//open database connection 
	static Connection openCon(String iniFilename) throws cwException, NamingException, SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		CommonLog.debug("Getting database connection...");
        WizbiniLoader wizbini = new WizbiniLoader(iniFilename);
        cwSQL sql = new cwSQL();
        sql.setParam(wizbini);
        Connection con = sql.openDB(true);
        CommonLog.debug("Get database connection done"); 
		return con;
	}
	
	public static String replaceStr(String inStr, String src_pattern, String target_pattern,boolean ignore_case) {
		int start = 0;
		int index = 0; 
		int src_length = src_pattern.length();
		StringBuffer result = new StringBuffer();
		String _src_pattern = src_pattern;
		String _inStr = inStr;

		if(ignore_case){
			_inStr = inStr.toUpperCase();
			_src_pattern = src_pattern.toUpperCase(); 
		}         
		while ((index = _inStr.indexOf(_src_pattern,start)) != -1) {
			result.append(inStr.substring(start, index)).append(target_pattern);
			start = index+src_length;
			} 
		result.append(inStr.substring(start));
		return result.toString();
	}
    
	private static final String sql_get_itm_id = 
		" select itm_id,itm_create_timestamp,itm_create_usr_id from aeItem where (itm_apply_ind = 1 or itm_create_run_ind = 1 or itm_run_ind = 1) "
	   +" and (itm_id not in (select ccr_itm_id from courseCriteria))";
	private static final String sql_ins_ccr = 
		" insert into courseCriteria (ccr_itm_id,ccr_pass_score,ccr_duration,ccr_pass_ind,ccr_create_timestamp,ccr_create_usr_id,ccr_upd_timestamp,ccr_upd_usr_id,ccr_upd_method,ccr_all_cond_ind)"
	   +" values (?,0,0,0,?,?,?,?,'AUTO',0)";
	private static final String sql_get_score_cmr_id = 
		" select cmr_id,cmr_res_id,cmr_ccr_id,cmr_status,cmr_create_timestamp,cmr_create_usr_id,res_title from coursemodulecriteria,resources "
	   +" where cmr_del_timestamp is null and cmr_status is null and res_id = cmr_res_id and cmr_id not in (select cmt_cmr_id from coursemeasurement where cmt_ccr_id = cmr_ccr_id ) ";
	private static final String sql_get_not_score_cmr_id = 
		" select cmr_id,cmr_res_id,cmr_ccr_id,cmr_status,cmr_create_timestamp,cmr_create_usr_id,res_title from coursemodulecriteria,resources "
	   +" where cmr_del_timestamp is null and cmr_status is not null and res_id = cmr_res_id and cmr_id not in (select cmt_cmr_id from coursemeasurement where cmt_ccr_id = cmr_ccr_id )";
	private static final String sql_ins_cmt = 
		" insert into courseMeasurement (cmt_title,cmt_ccr_id,cmt_cmr_id,cmt_max_score,cmt_pass_score,cmt_contri_rate,cmt_is_contri_by_score,cmt_create_timestamp,cmt_create_usr_id,cmt_update_timestamp,cmt_update_usr_id)"
	   +" values (?,?,?,0,0,0,?,?,?,?,?)";
	private static final String sql_upd_cmr_prep = "update courseModulecriteria set cmr_status = null where cmr_status = ''";
	private static final String sql_upd_cmr = 
		" update courseModulecriteria set cmr_contri_rate = 0,cmr_is_contri_by_score = 0 where cmr_del_timestamp is null and cmr_status is not null ";      
	private static final String sql_upd_cmr_status_desc_option = 
		" update courseModuleCriteria set cmr_status_desc_option = ? where cmr_del_timestamp is null and cmr_res_id in "
	   +" ( select mod_res_id from module where mod_type in (?) ) and cmr_status = ? ";
	private static final String sql_upd_cmr_contri_rate = 
		" update courseModuleCriteria set cmr_contri_rate = ? where cmr_del_timestamp is null and cmr_id = ?";
	private static final String sql_get_contri_rate_less_cmr_id = 
		" select ccr_id,sum(cmr_contri_rate) as contri_rate_sum from courseModuleCriteria ,coursecriteria " 
       +"where cmr_del_timestamp is null and cmr_is_contri_by_score = 1 and cmr_ccr_id = ccr_id group by ccr_id having sum(cmr_contri_rate) != 100  ";	
	private static final String sql_get_cmr_id = 
		" select cmr_id,cmr_contri_rate from courseModuleCriteria  " 
	   +"where cmr_del_timestamp is null and cmr_ccr_id = ? and cmr_is_contri_by_score = 1 ";       	
	static void updDatabase(Connection con) throws SQLException {
//update coursecriteria
		CommonLog.debug("***********Updating CourseCriteria table...");
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		stmt = con.prepareStatement(sql_get_itm_id);
		ResultSet rs = stmt.executeQuery();
		int rc = 0;
		while(rs.next()) {
			stmt1 = con.prepareStatement(sql_ins_ccr);          
			stmt1.setLong(1,rs.getLong("itm_id"));	
			stmt1.setTimestamp(2,rs.getTimestamp("itm_create_timestamp"));			
			stmt1.setString(3,rs.getString("itm_create_usr_id"));			
			stmt1.setTimestamp(4,rs.getTimestamp("itm_create_timestamp"));			
			stmt1.setString(5,rs.getString("itm_create_usr_id"));
			stmt1.executeUpdate();
			stmt1.close();
			rc++;								
		}
		stmt.close(); 
		CommonLog.debug(rc + " row(s) inserted into CourseCriteria table!");
		CommonLog.debug("Update CourseCriteria done!");
//update coursemodulecriteria		
		CommonLog.debug("***********Updating CourseModuleCriteria table(reset contri_rate and is_contri_by_score)...");
		rc = 0;
		stmt = con.prepareStatement(sql_upd_cmr_prep);
		rc = stmt.executeUpdate();
		CommonLog.debug(rc + " row(s) updated(set cmr_status from '' to null)!");
		rc = 0;
		stmt = con.prepareStatement(sql_upd_cmr);
		rc = stmt.executeUpdate();
		CommonLog.debug(rc + " row(s) updated!");
		CommonLog.debug("Update CourseModuleCriteria(reset contri_rate and is_contri_by_score) done!");
		stmt.close();	
		CommonLog.debug("***********Updating CourseModuleCriteria table(update cmr_status_desc_option)...");
		rc = 0;
		for(int i=0;i<8;i++){
			if(i==0){
				stmt = con.prepareStatement(replaceStr(sql_upd_cmr_status_desc_option,"(?)","('RDG','REF','GLO','VOD')",false));
				stmt.setInt(1,i);
				stmt.setString(2,"IFCP");
			}
			if(i==1){
				stmt = con.prepareStatement(replaceStr(sql_upd_cmr_status_desc_option,"(?)","('TST','DXT','AICC_AU','NETG_COK')",false));
				stmt.setInt(1,i);
				stmt.setString(2,"IFCP");			
			}
			if(i==2){
				stmt = con.prepareStatement(replaceStr(sql_upd_cmr_status_desc_option,"(?)","('TST','DXT','AICC_AU','NETG_COK')",false));				
				stmt.setInt(1,i);
				stmt.setString(2,"CP");				
			}
			if(i==3){
				stmt = con.prepareStatement(replaceStr(sql_upd_cmr_status_desc_option,"(?)","('ASS')",false));
				stmt.setInt(1,i);
				stmt.setString(2,"IFCP");				
			}
			if(i==4){
				stmt = con.prepareStatement(replaceStr(sql_upd_cmr_status_desc_option,"(?)","('SVY')",false));
				stmt.setInt(1,i);
				stmt.setString(2,"IFCP");				
			}
			if(i==5){
				stmt = con.prepareStatement(replaceStr(sql_upd_cmr_status_desc_option,"(?)","('FOR')",false));
				stmt.setInt(1,i);
				stmt.setString(2,"IFCP");				
			}
			if(i==6){
				stmt = con.prepareStatement(replaceStr(sql_upd_cmr_status_desc_option,"(?)","('CHT')",false));
				stmt.setInt(1,i);
				stmt.setString(2,"IFCP");				
			}
			if(i==7){
				stmt = con.prepareStatement(replaceStr(sql_upd_cmr_status_desc_option,"(?)","('FAQ')",false));
				stmt.setInt(1,i);
				stmt.setString(2,"IFCP");				
			}
			rc = stmt.executeUpdate();		
			CommonLog.debug(rc + " row(s) updated (cmr_status_desc_option = "+i+")!");
			rc = 0;			
		}
		stmt.close();	
		CommonLog.debug("Update CourseModuleCriteria(update cmr_status_desc_option) done!");
		CommonLog.debug("***********Updating CourseModuleCriteria table(update the cmr_contri_rate)...");	
		PreparedStatement stmt2 = null;	
		stmt = con.prepareStatement(sql_get_contri_rate_less_cmr_id);
		rs = stmt.executeQuery();
		while(rs.next()){
			int count = 0;
			int average = 0;
			int residue = 0;
			int sum = 0;
			rc = 1;
			stmt1 = con.prepareStatement(sql_get_cmr_id);
			stmt1.setLong(1,rs.getLong("ccr_id"));
			CommonLog.debug("ccr_id: "+rs.getLong("ccr_id"));
			ResultSet rs1 = null;
			rs1 = stmt1.executeQuery();
			if(rs1.next()){
				rs1.last();
				count = rs1.getRow();
				rs1.beforeFirst();
			}
			if(stmt1 != null){
				stmt1.close();
			}
			sum = rs.getInt("contri_rate_sum");
			residue = (100 - sum)%count;
			average = (100 - sum - residue)/count;
			if(residue<0){
				CommonLog.debug("The contri_rate_sum of this ccr_id is greater than 100("+rs.getInt("contri_rate_sum")+")!!!");
				continue;
			}
			if(count > 0 && sum < 100){
				while(rs1.next()){
					stmt2 = con.prepareStatement(sql_upd_cmr_contri_rate);
					float add =average;
					if (residue > 0){
						add++;
						residue--;
					}
					stmt2.setFloat(1,rs1.getInt("cmr_contri_rate") + add);
					CommonLog.debug("contri_rate is: " + (rs1.getInt("cmr_contri_rate") + add));
					CommonLog.debug("  cmr_id: "+rs1.getLong("cmr_id"));				
					stmt2.setLong(2,rs1.getLong("cmr_id"));
					stmt2.executeUpdate();
					rc++;
					if(stmt2 !=null){
						stmt2.close();
					}
				}		
			}
		}
		
		
		stmt.close();
		CommonLog.debug("Update CourseModuleCriteria table(update the cmr_contri_rate) done!");
//update coursemeasurement
		CommonLog.debug("***********Updating CourseMeasurement(cmt_is_contri_by_score = 1)...");
		stmt = con.prepareStatement(sql_get_score_cmr_id);
		rs = stmt.executeQuery();
		rc = 0;
		while(rs.next()){
			stmt1 = con.prepareStatement(sql_ins_cmt);
			stmt1.setString(1,rs.getString("res_title")+"(criterion "+(rc+1)+")");
			stmt1.setLong(2,rs.getLong("cmr_ccr_id"));
			stmt1.setLong(3,rs.getLong("cmr_id"));
			stmt1.setInt(4,1);
			stmt1.setTimestamp(5,rs.getTimestamp("cmr_create_timestamp"));
			stmt1.setString(6,rs.getString("cmr_create_usr_id"));
			stmt1.setTimestamp(7,rs.getTimestamp("cmr_create_timestamp"));
			stmt1.setString(8,rs.getString("cmr_create_usr_id"));
			stmt1.executeUpdate();
			stmt1.close();
			rc++;
		}
		stmt.close();
		CommonLog.debug(rc + " row(s) inserted into CourseMeasurement table!");
		CommonLog.debug("Update CourseMeasurement(cmt_is_contri_by_score = 1) done!");
		CommonLog.debug("***********Updating CourseMeasurement(cmt_is_contri_by_score = 0) table...");
		stmt = con.prepareStatement(sql_get_not_score_cmr_id);
		rs = stmt.executeQuery();
		rc = 0;
		while(rs.next()){
			stmt1 = con.prepareStatement(sql_ins_cmt);
			stmt1.setString(1,rs.getString("res_title")+"(criterion "+(rc+1)+")");
			stmt1.setLong(2,rs.getLong("cmr_ccr_id"));
			stmt1.setLong(3,rs.getLong("cmr_id"));
			stmt1.setInt(4,0);
			stmt1.setTimestamp(5,rs.getTimestamp("cmr_create_timestamp"));
			stmt1.setString(6,rs.getString("cmr_create_usr_id"));
			stmt1.setTimestamp(7,rs.getTimestamp("cmr_create_timestamp"));
			stmt1.setString(8,rs.getString("cmr_create_usr_id"));
			stmt1.executeUpdate();
			stmt1.close();
			rc++;
		}
		stmt.close();
		CommonLog.debug(rc + " row(s) inserted into CourseMeasurement table!");
		CommonLog.debug("Update CourseMeasurement(cmt_is_contri_by_score = 0) done!");
	}
	
    
	/*************** Utils ***************/
	private static long[] vector2long(Vector v) {
		int size = v.size();
		long[] result = new long[size];
		for(int i=0;i<size;i++) {
			result[i] = ((Long) v.elementAt(i)).longValue();
		}
		return result;
	}
}