package com.cw.wizbank.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.util.cwSQL;

public class DBUserPwdResetHis {
	
	public static final String DEF_STATUS = "Y";
	public static final int DEF_ATTEMPTED = 0;
	public static final String PRH_STATUS_Y ="Y";
	public static final String PRH_STATUS_N ="N";
	
	/**
	 * 添加忘记密码请求的记录
	 * @param con
	 * @param ent_id
	 * @param ip
	 * @return
	 * @throws SQLException
	 */
	public static long ins(Connection con, long ent_id, String ip) throws SQLException{
		String sql ="insert into usrPwdResetHis (prh_ent_id,prh_ip,prh_status,prh_attempted,prh_create_timestamp) values(?,?,?,?,?)";
		Timestamp cur_time = cwSQL.getTime(con);
		PreparedStatement stmt = con.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);
		stmt.setLong(1, ent_id);
		stmt.setString(2, ip);
		stmt.setString(3, DEF_STATUS);
		stmt.setInt(4, DEF_ATTEMPTED);
		stmt.setTimestamp(5, cur_time);
		stmt.execute();
		long  prh_id= cwSQL.getAutoId(con, stmt, "usrPwdResetHis", "prh_id");
		if(stmt !=null){
			stmt.close();
		}
		return prh_id;
	}
	
	/**
	 * 检查邮件里修改密码链接的有效性
	 * @param con
	 * @param prh_id
	 * @param link_max_days
	 * @param max_attempted
	 * @param usr_id
	 * @return
	 * @throws SQLException
	 */
	public boolean checkPwdResetLink(Connection con, long prh_id, int link_max_days, int max_attempted, String usr_id) throws SQLException{
		String sql = "Select count(*) num from usrPwdResetHis, reguser  where prh_id =? and prh_create_timestamp > ?" +
				" and prh_status = ?" +
				" and prh_attempted < ?" +
				" and prh_ent_id = usr_ent_id " +
				" and usr_ste_usr_id = ?  " +
				" and usr_status=? " ;	
		Timestamp cur_time = cwSQL.getTime(con);
		Calendar cal=Calendar.getInstance();
		cal.setTimeInMillis(cur_time.getTime());
		cal.add(Calendar.DATE, -link_max_days);
		long tmpTime = cal.getTimeInMillis();
		Timestamp before_time = new Timestamp(tmpTime);
		PreparedStatement stmt = con.prepareStatement(sql);
		int index =1;
		stmt.setLong(index++, prh_id);
		stmt.setTimestamp(index++, before_time);
		stmt.setString(index++, PRH_STATUS_Y);
		stmt.setInt(index++, max_attempted);
		stmt.setString(index++, usr_id);
		stmt.setString(index++, dbRegUser.USR_STATUS_OK);
		ResultSet rs = stmt.executeQuery();
		boolean result = false;
		if(rs.next()){
			if(rs.getInt("num")>0){
				result= true;				
			}
		}
		if(stmt !=null){
			stmt.close();
		}
		return result;		
	}
	
	/**
	 * 获取尝试修改密码的次数
	 * @param con
	 * @param prh_id
	 * @return
	 * @throws SQLException
	 */
	public int getAttempted(Connection con, long prh_id) throws SQLException{
		String sql = "select prh_attempted from usrPwdResetHis where prh_id = ?";
		PreparedStatement stmt = con.prepareStatement(sql);
		int index =1;
		stmt.setLong(index++, prh_id);
		ResultSet rs = stmt.executeQuery();
		int attempted =0;
		if(rs.next()){
			attempted = rs.getInt("prh_attempted");
		}
		if(stmt != null){
			stmt.close();
		}
		return attempted;
	}

	/**
	 * 更新状态和修改次数
	 * @param con
	 * @param prh_id
	 * @param attempted
	 * @param max_attempted
	 * @throws SQLException
	 */
	public void updStutusAndAttempted(Connection con, long prh_id, int attempted, int max_attempted) throws SQLException{
		StringBuffer sqlBuf = new StringBuffer();
		sqlBuf.append("update usrPwdResetHis set  prh_attempted =? ");
		if(attempted >= max_attempted){
			sqlBuf.append(" prh_status=?");
		}
		sqlBuf.append(" where prh_id =?");
		PreparedStatement stmt = con.prepareStatement(sqlBuf.toString());
		int index =1;
		stmt.setInt(index++, attempted+1);
		if(attempted >= max_attempted){
			stmt.setString(index++, PRH_STATUS_N);			
		}
		stmt.setLong(index++, prh_id);
		stmt.execute();
		if(stmt !=null){
			stmt.close();
		}	
	}
	
	public Timestamp getPrhCreateTime(Connection con, long prh_id) throws SQLException{
		String sql ="select prh_create_timestamp from usrPwdResetHis where prh_id=?";
		PreparedStatement stmt = con.prepareStatement(sql);
		int index =1;
		stmt.setLong(index++, prh_id);
		ResultSet rs = stmt.executeQuery();
		Timestamp create_time = null;
		if(rs.next()){
			create_time = rs.getTimestamp("prh_create_timestamp");
		}
		if(stmt != null){
			stmt.close();
		}
		return create_time;
	}
	
	/**
     * 获取尝试修改密码的次数
     * @param con
     * @param prh_id
     * @return
     * @throws SQLException
     */
    public long getEntId(Connection con, long prh_id) throws SQLException{
        String sql = "select prh_ent_id from usrPwdResetHis where prh_id = ?";
        PreparedStatement stmt = con.prepareStatement(sql);
        int index =1;
        stmt.setLong(index++, prh_id);
        ResultSet rs = stmt.executeQuery();
        long prh_ent_id =0;
        if(rs.next()){
            prh_ent_id = rs.getLong("prh_ent_id");
        }
        if(stmt != null){
            stmt.close();
        }
        return prh_ent_id;
    }
}
