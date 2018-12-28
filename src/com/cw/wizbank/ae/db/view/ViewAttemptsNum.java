/*
 * Created on 2004-4-23
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.cw.wizbank.ae.db.view;

import java.sql.*;
import java.util.Vector;

import com.cw.wizbank.qdb.dbModuleEvaluation;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwException;

/**
 * @author Donald
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ViewAttemptsNum {

	public static String PRESQL =
		"SELECT sum(mov_total_attempt) num FROM ModuleEvaluation  WHERE  mov_tkh_id in(select * FROM ";
		
	String tableName="";
	Vector tkh_id_vec = new Vector();
	Connection con=null;

	public ViewAttemptsNum(Connection con,long[] tkh_id_ary) throws SQLException {	
		this.con=con;
		creatTable(con,tkh_id_ary);	
		
	}

    public void creatTable(Connection con,long[] tkh_id_ary) throws SQLException{
		for (int i=0; i< tkh_id_ary.length; i++){
			tkh_id_vec.add(new Long(tkh_id_ary[i]));
		}
		String colName = "tmp_mov_tkh_id";
		tableName = cwSQL.createSimpleTemptable(con, colName, cwSQL.COL_TYPE_LONG, 0);
		cwSQL.insertSimpleTempTable(con,tableName,tkh_id_vec,cwSQL.COL_TYPE_LONG);
		
    }

	public int getAttemptsNum()
		throws SQLException, cwException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		StringBuffer SQLBuf=new StringBuffer(512);
		int num = 0;

		SQLBuf.append(PRESQL).append(tableName).append(" )");

	    stmt = con.prepareStatement(SQLBuf.toString());
	    rs = stmt.executeQuery();
       
		if (rs.next()) {
			num = rs.getInt("num");
		}
		stmt.close();
		
		return num;
	}
	
	
	/**@author Christ Qiu
			 Get the total users with attempts 
			 @param con Connection to database
			 @param tkh_id_ary: array of all tkh_id selected
			 @param tkh_ary_length:length of array "tkh_id_ary" 
			 @return number of users with attempts
			 */
		public int getAttemptUsers() throws SQLException
	 {
			int attemptusers = 0;	
			attemptusers = dbModuleEvaluation.getAttemptUsers(this.con, this.tkh_id_vec, this.tableName);
			return attemptusers;
		}
		
		public void dropTable()throws SQLException{
			cwSQL.dropTempTable(con,tableName);
		}
		
		
}
