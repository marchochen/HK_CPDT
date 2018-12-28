/**
 * 
 */
package com.cw.wizbank.credit.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.cw.wizbank.JsonMod.credit.Credit;
import com.cw.wizbank.credit.db.UserCreditsDetailLogDB;
import com.cw.wizbank.util.cwSQL;

/**
 * @author DeanChen
 * 
 */
public class UserCreditsDetailLogDAO {

    private static final String INS_UCL = " INSERT INTO userCreditsDetailLog "+ (cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType()) ? " with(rowlock) ":"") 
                    + "(ucl_usr_ent_id,ucl_bpt_id,"
            + " ucl_relation_type,ucl_source_id,ucl_point,ucl_create_timestamp,ucl_create_usr_id,ucl_app_id) "
            + " VALUES(?,?,?,?,?,?,?,?) ";

    /**
     * to insert one record into table userCreditsDetailLog.
     * 
     * @param con
     * @param uclDb
     * @throws SQLException
     */
    public void ins(Connection con, UserCreditsDetailLogDB uclDb)
            throws SQLException {
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(INS_UCL);
            int index = 1;
            stmt.setInt(index++, uclDb.getUcl_usr_ent_id());
            stmt.setInt(index++, uclDb.getUcl_bpt_id());
            stmt.setString(index++, uclDb.getUcl_relation_type());
            stmt.setLong(index++, uclDb.getUcl_source_id());
//            stmt.setFloat(index++, uclDb.getUcl_point());
            
            if(cwSQL.getDbProductName().indexOf(cwSQL.ProductName_MSSQL) >= 0){
				stmt.setString(index++, uclDb.getUcl_point() + "");
			} else {
				stmt.setFloat(index++, uclDb.getUcl_point());
			}
			
			
            stmt.setTimestamp(index++, uclDb.getUcl_create_timestamp());
            stmt.setString(index++, uclDb.getUcl_create_usr_id());
            stmt.setLong(index++, uclDb.getUcl_app_id());
            int insCount = stmt.executeUpdate();
            if (insCount != 1) {
                throw new SQLException(
                        " Failed to add a record of userCreditsDetailLog. ");
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    private static final String HAS_ANSWER_QUESTION = " SELECT ucl_usr_ent_id FROM userCreditsDetailLog "+ (cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType()) ? " with(nolock) ":"") 
            + " WHERE ucl_usr_ent_id = ? AND ucl_relation_type = ? AND ucl_source_id = ? ";

    /**
     * whether user has answer the question.
     * 
     * @param con
     * @param userEntId
     * @param queId
     * @return
     * @throws SQLException
     */
    public static boolean hasAnswerQue(Connection con, int userEntId, int queId)
            throws SQLException {
        boolean hasAnswerQue = false;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(HAS_ANSWER_QUESTION);
            int index = 1;
            stmt.setInt(index++, userEntId);
            stmt.setString(index++, Credit.CTY_KNOW);
            stmt.setInt(index++, queId);

            rs = stmt.executeQuery();
            if (rs.next()) {
                hasAnswerQue = true;
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }
        return hasAnswerQue;
    }
    
    public void del(Connection con, UserCreditsDetailLogDB uclDb, boolean isManualItemCredit)
		    throws SQLException {
    	String sql = " DELETE FROM userCreditsDetailLog "+ (cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType()) ? " with(rowlock) ":"") 
                    + " WHERE ucl_usr_ent_id = ? AND ucl_source_id = ? AND ucl_app_id = ? ";
		if(isManualItemCredit){
			sql += " AND ucl_bpt_id IN( SELECT cty_id FROM creditsType WHERE  cty_code = ?)";
		}else{
			sql += " AND ucl_bpt_id NOT IN( SELECT cty_id FROM creditsType WHERE  cty_code = ?)";
		}
    	PreparedStatement stmt = null;
		try {
		    stmt = con.prepareStatement(sql);
		    int index = 1;
		    stmt.setInt(index++, uclDb.getUcl_usr_ent_id());
		    stmt.setLong(index++, uclDb.getUcl_source_id());
		    stmt.setLong(index++, uclDb.getUcl_app_id());		
		    stmt.setString(index++, Credit.ITM_IMPORT_CREDIT);
		    stmt.executeUpdate();
		} finally {
			cwSQL.closePreparedStatement(stmt);
		}
	}
    
    public static boolean hasUserCreditsDetailLog(Connection con, long ucl_usr_ent_id, long ucl_bpt_id, long ucl_source_id, String ucl_create_usr_id) throws SQLException {
    	String sql = " select * from usercreditsdetaillog where ucl_usr_ent_id = ? and ucl_bpt_id = ? and ucl_source_id = ? and ucl_create_usr_id = ? ";
    	PreparedStatement stmt = null;
    	ResultSet rs = null;
    	boolean userCreditsDetailLogInd = false;
    	try {
    		stmt = con.prepareStatement(sql);
    		int index = 1;
    		stmt.setLong(index++, ucl_usr_ent_id);
    		stmt.setLong(index++, ucl_bpt_id);
    		stmt.setLong(index++, ucl_source_id);
    		stmt.setString(index++, ucl_create_usr_id);
    		rs = stmt.executeQuery();
    		if(rs.next()){
    			userCreditsDetailLogInd = true;
    		}
    	} finally {
    		cwSQL.cleanUp(rs, stmt);
    	}
    	return userCreditsDetailLogInd;
    }
    
    public static boolean hasUserCreditsDetailLog(Connection con, long ucl_usr_ent_id,String cty_title, long ucl_bpt_id) throws SQLException {
    	String sql = " select * from userCreditsDetailLog where ucl_usr_ent_id = ? and ucl_bpt_id = (select cty_id from creditsType where cty_title= ? and cty_tcr_id= ?)";
    	PreparedStatement stmt = null;
    	ResultSet rs = null;
    	boolean userCreditsDetailLogInd = false;
    	try {
    		stmt = con.prepareStatement(sql);
    		int index = 1;
    		stmt.setLong(index++, ucl_usr_ent_id);
    		stmt.setString(index++, cty_title);
    		stmt.setLong(index++, ucl_bpt_id);
    		rs = stmt.executeQuery();
    		if(rs.next()){
    			userCreditsDetailLogInd = true;
    		}
    	} finally {
    		cwSQL.cleanUp(rs, stmt);
    	}
    	return userCreditsDetailLogInd;
    }
    
}
