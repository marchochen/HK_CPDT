/**
 * 
 */
package com.cw.wizbank.credit.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.cw.wizbank.JsonMod.credit.Credit;
import com.cw.wizbank.JsonMod.know.bean.CreditsTypeBean;
import com.cw.wizbank.credit.db.CreditsTypeDB;
import com.cw.wizbank.db.view.ViewTrainingCenter;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.utils.CommonLog;

/**
 * CreditsTypeDAO is a class that operates on table creditsType.
 * 
 * @author DeanChen
 */
public class CreditsTypeDAO {

    // the relation type of credits
    public static final String CTY_KNOW = "ZD";

    public static final String CTY_COURSE = "COS";
    
    public static final String CTY_PERIOD_DAY = "DAY";
    
    public static final String CTY_PERIOD_MONTH = "MONTH";
    
    public static final String CTY_PERIOD_YEAR = "YEAR";

    private static final String GET_CTY_BY_CODE = " SELECT cty_id,cty_title,cty_deduction_ind,cty_manual_ind,"
            + " cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,"
            + " cty_default_credits,cty_create_usr_id,cty_create_timestamp,cty_update_usr_id,cty_update_timestamp,cty_hit,cty_period"
            + " FROM creditsType "+ (cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType()) ? " with(nolock) ":"") 
                    + " WHERE cty_code = ? AND cty_deleted_ind = 0 and cty_tcr_id = ?";

    public void getByCode(Connection con, CreditsTypeDB ctyDb) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(GET_CTY_BY_CODE);
            int index = 1;
            stmt.setString(index++, ctyDb.getCty_code());
            stmt.setLong(index++, ctyDb.getCty_tcr_id());
            rs = stmt.executeQuery();
            if (rs.next()) {
                ctyDb.setCty_id(rs.getInt("cty_id"));
                ctyDb.setCty_title(rs.getString("cty_title"));
                ctyDb.setCty_deduction_ind(rs.getBoolean("cty_deduction_ind"));
                ctyDb.setCty_manual_ind(rs.getBoolean("cty_manual_ind"));
                ctyDb.setCty_deleted_ind(rs.getBoolean("cty_deleted_ind"));
                ctyDb.setCty_relation_total_ind(rs
                        .getBoolean("cty_relation_total_ind"));
                ctyDb.setCty_relation_type(rs.getString("cty_relation_type"));
                ctyDb.setCty_default_credits_ind(rs
                        .getBoolean("cty_default_credits_ind"));
                ctyDb.setCty_default_credits(rs.getFloat("cty_default_credits"));
                ctyDb.setCty_create_usr_id(rs.getString("cty_create_usr_id"));
                ctyDb.setCty_create_timestamp(rs
                        .getTimestamp("cty_create_timestamp"));
                ctyDb.setCty_update_usr_id(rs.getString("cty_update_usr_id"));
                ctyDb.setCty_update_timestamp(rs.getTimestamp("cty_update_timestamp"));
                ctyDb.setCty_hit(rs.getInt("cty_hit"));
                ctyDb.setCty_period(rs.getString("cty_period"));
            } 
//            else {
//                System.out.print(" Failed to get a record of userCreditsDetail by code: " + ctyDb.getCty_code());
//            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }
    }
    
    private static final String GET_CTY_BY_ID = " SELECT cty_id,cty_title,cty_deduction_ind,cty_manual_ind,"
        + " cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,"
        + " cty_default_credits,cty_create_usr_id,cty_create_timestamp,cty_update_usr_id,cty_update_timestamp,cty_hit,cty_period"
        + " FROM creditsType "+ (cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType()) ? " with(nolock) ":""); 
    public void getById(Connection con, CreditsTypeDB ctyDb) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
        	String GET_CTY_SQL =  GET_CTY_BY_ID + " WHERE cty_deleted_ind = 0 ";
            if(null != ctyDb && ctyDb.getCty_id() > 0){
            	GET_CTY_SQL += "  AND cty_id = ?";
            }
            if(null != ctyDb && null != ctyDb.getCty_code() && !ctyDb.getCty_code().equals("")){
            	GET_CTY_SQL += "  AND cty_code = ?";
            }
        	stmt = con.prepareStatement(GET_CTY_SQL);
            int index = 1;
            if(null != ctyDb && ctyDb.getCty_id() > 0){
            	stmt.setLong(index++, ctyDb.getCty_id());
            }
            if(null != ctyDb && null != ctyDb.getCty_code() && !ctyDb.getCty_code().equals("")){
            	stmt.setString(index++, ctyDb.getCty_code());
            }
            rs = stmt.executeQuery();
            if (rs.next()) {
                ctyDb.setCty_id(rs.getInt("cty_id"));
                ctyDb.setCty_title(rs.getString("cty_title"));
                ctyDb.setCty_deduction_ind(rs.getBoolean("cty_deduction_ind"));
                ctyDb.setCty_manual_ind(rs.getBoolean("cty_manual_ind"));
                ctyDb.setCty_deleted_ind(rs.getBoolean("cty_deleted_ind"));
                ctyDb.setCty_relation_total_ind(rs
                        .getBoolean("cty_relation_total_ind"));
                ctyDb.setCty_relation_type(rs.getString("cty_relation_type"));
                ctyDb.setCty_default_credits_ind(rs
                        .getBoolean("cty_default_credits_ind"));
                ctyDb.setCty_default_credits(rs.getFloat("cty_default_credits"));
                ctyDb.setCty_create_usr_id(rs.getString("cty_create_usr_id"));
                ctyDb.setCty_create_timestamp(rs
                        .getTimestamp("cty_create_timestamp"));
                ctyDb.setCty_update_usr_id(rs.getString("cty_update_usr_id"));
                ctyDb.setCty_update_timestamp(rs.getTimestamp("cty_update_timestamp"));
                ctyDb.setCty_hit(rs.getInt("cty_hit"));
                ctyDb.setCty_period(rs.getString("cty_period"));
            } else {
            	CommonLog.info(" Failed to get a record of userCreditsDetail by code: " + ctyDb.getCty_code());
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }
    }
    
    
    private static final String GET_CTY_CODE = " SELECT cty_code FROM creditsType WHERE cty_id = ? ";
    public String getCode(Connection con, int cty_id) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String cty_code = null;
        try {
            stmt = con.prepareStatement(GET_CTY_CODE);
            int index = 1;
            stmt.setInt(index++, cty_id);
            rs = stmt.executeQuery();
            if (rs.next()) {
            	cty_code = rs.getString("cty_code");
            } else {
                throw new SQLException(
                        " Failed to get cty_code of userCreditsDetail by id: "
                                + cty_id);
            }
        } finally {
            cwSQL.cleanUp(rs, stmt);
        }
        return cty_code;
    }

    private static final String GET_ALL = " SELECT cty_id, cty_code, cty_deduction_ind, cty_default_credits "
            + " FROM creditsType WHERE cty_relation_type = ? AND cty_deleted_ind = ? ";

    public static Vector getAll(Connection con) throws SQLException {
        Vector creidtsTypeVec = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(GET_ALL);
            int index = 1;
            stmt.setString(index++, CTY_KNOW);
            stmt.setBoolean(index++, false);
            rs = stmt.executeQuery();
            while (rs.next()) {
                if(creidtsTypeVec == null) {
                    creidtsTypeVec = new Vector();
                }
                CreditsTypeBean creditsTypeBean = new CreditsTypeBean();
                creditsTypeBean.setCty_id(rs.getInt("cty_id"));
                creditsTypeBean.setCty_code(rs.getString("cty_code"));
                creditsTypeBean.setCty_deduction_ind(rs.getBoolean("cty_deduction_ind"));
                creditsTypeBean.setCty_default_credits(rs.getFloat("cty_default_credits"));
                creidtsTypeVec.addElement(creditsTypeBean);
            } 
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }
        
        return creidtsTypeVec;
    }
    
    private static final String GET_ALL_AUTO_CREDIT ="SELECT cty_id, cty_code, cty_default_credits, cty_relation_type, cty_period, cty_hit, cty_update_timestamp " +
    											"FROM creditsType WHERE cty_manual_ind = ? and  cty_deleted_ind = ? and cty_relation_total_ind = ? and cty_tcr_id =? order by cty_relation_type, cty_id";

	public Vector getAllAutoCredit(Connection con, long tcr_id) throws SQLException {
	    Vector creidtsAutoTypeVec = null;
	    PreparedStatement stmt = null;
	    ResultSet rs = null;
	    try {
	        stmt = con.prepareStatement(GET_ALL_AUTO_CREDIT);
	        int index = 1;
	        stmt.setBoolean(index++, false);
	        stmt.setBoolean(index++, false);
	        stmt.setBoolean(index++, true);
	        stmt.setLong(index++, tcr_id);
	        rs = stmt.executeQuery();
	        while (rs.next()) {
	            if(creidtsAutoTypeVec == null) {
	                creidtsAutoTypeVec = new Vector();
	            }
	            if(Credit.ITM_VIEW_FAQ.equalsIgnoreCase(rs.getString("cty_code"))){
	            	continue;
	            }
	            //屏蔽点赞功能
	            if(Credit.SYS_CLICK_LIKE.equalsIgnoreCase(rs.getString("cty_code"))){
	            	continue;
	            //屏蔽被点赞功能
	            }  if(Credit.SYS_GET_LIKE.equalsIgnoreCase(rs.getString("cty_code"))){
	            	continue;
	            }
	            //屏蔽评论功能
	            if(Credit.SYS_COS_COMMENT.equalsIgnoreCase(rs.getString("cty_code"))){
	            	continue;
	            }
	            
	            CreditsTypeBean creditsTypeBean = new CreditsTypeBean();
	            creditsTypeBean.setCty_id(rs.getInt("cty_id"));
	            creditsTypeBean.setCty_code(rs.getString("cty_code"));
	            creditsTypeBean.setCty_default_credits(rs.getFloat("cty_default_credits"));
	            creditsTypeBean.setCty_relation_type(rs.getString("cty_relation_type"));
	            creditsTypeBean.setCty_period(rs.getString("cty_period"));
	            creditsTypeBean.setCty_hit(rs.getLong("cty_hit"));
	            creditsTypeBean.setCty_update_timestamp(rs.getTimestamp("cty_update_timestamp"));
	            creidtsAutoTypeVec.addElement(creditsTypeBean);
	        } 
	    } finally {
	    	 cwSQL.cleanUp(rs, stmt);
	    }
	    return creidtsAutoTypeVec;
	}
	
    private static final String UPD_AUTO_CREDIT ="UPDATE creditstype SET cty_default_credits = ?, cty_hit=? , cty_update_usr_id=?, cty_update_timestamp=? "+ 
    											 "WHERE cty_code = ? and cty_tcr_id =?";
	public static void updAutoCreditType(Connection con, String credit_type, String update_usr_id, Timestamp cur_time, float default_score, long hit, long tcr_id) throws SQLException {
	    PreparedStatement stmt = null;
        try {
    	    if(!isCreditPointTypeExist(con, credit_type, tcr_id) ){
    	        long root_tcr_id=ViewTrainingCenter.getRootTcId(con);
    	        copyAllAutoCreditTypeFromRootTC( con, tcr_id, root_tcr_id, credit_type);
    	    }
		
			stmt = con.prepareStatement(UPD_AUTO_CREDIT);
			int index = 1;
			 if(cwSQL.getDbProductName().indexOf(cwSQL.ProductName_MSSQL) >= 0){
				stmt.setString(index++, default_score + "");
			} else {
				stmt.setFloat(index++, default_score);
			}
			stmt.setInt(index++, (int)hit);
			stmt.setString(index++, update_usr_id);
			stmt.setTimestamp(index++, cur_time);
			stmt.setString(index++, credit_type);
			stmt.setLong(index++, tcr_id);
			stmt.executeUpdate();
		} finally {
			cwSQL.closePreparedStatement(stmt);
		}
	}
	
	/**
	 * Get manual credit point list.
	 * @param con Db connection
	 * @param deduction_ind Manual credit point type.
	 * @return 
	 * @throws SQLException
	 */
	public List getManualBonusList(Connection con, boolean deduction_ind, long tcr_id,String sort_order,boolean... empty) throws SQLException {
		List bonus_lst = new ArrayList();
		String sql = "SELECT cty_id, cty_code FROM creditstype WHERE cty_deduction_ind = ? AND cty_manual_ind = ? ";
		boolean empty_flag = false;
		if(empty!=null){
			if(empty.length > 0){
				empty_flag = true;
			}
		}
		//当为清空积分时，获取的类型
		if(!empty_flag){
			sql += "AND cty_deleted_ind = ? ";
		}
		sql+= "AND (cty_relation_type is null or cty_relation_type = '') and  cty_tcr_id =? ORDER BY cty_code ";
		if(sort_order != null && sort_order.equals("desc")) sql += " desc";
		PreparedStatement stmt = null; 
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(sql);
			int index = 1;
			stmt.setBoolean(index++, deduction_ind);
			stmt.setBoolean(index++, true);
			if(!empty_flag){
				stmt.setBoolean(index++, false);
			}
			stmt.setLong(index++, tcr_id);
			rs = stmt.executeQuery();
			while (rs.next()) {
				CreditsTypeBean creditsTypeBean = new CreditsTypeBean();
				creditsTypeBean.setCty_id(rs.getInt("cty_id")); 
				creditsTypeBean.setCty_code(rs.getString("cty_code"));
				bonus_lst.add(creditsTypeBean);
			}
		}finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return bonus_lst;
	}

	/**
	 * Check if the credit point type already exists.
	 * @param con Db connection.
	 * @param cty_code.
	 * @return true if the manual point type already exists. 
	 * @throws SQLException
	 * @throws cwException
	 */
     public static boolean isCreditPointTypeExist(Connection con,  String cty_code, long tcr_id) 
        throws SQLException{
		boolean result = false;
		PreparedStatement stmt = null; 
		ResultSet rs = null;
		String sql = "SELECT cty_id FROM CreditsType WHERE cty_code = ? and cty_deleted_ind = ?  ";
		if(tcr_id > 0){
		    sql+=" AND cty_tcr_id = ?";
		}
		try {
			stmt = con.prepareStatement(sql);
			int index = 1;
			stmt.setString(index++, cty_code);
			stmt.setBoolean(index++, false);
			if(tcr_id > 0){
			    stmt.setLong(index++, tcr_id);
			}
			rs = stmt.executeQuery();
			if(rs.next()){
			    result = true;
			}
		}finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return result;
    }

    private static final String INS_MANUAL_CREDIT ="INSERT INTO creditsType(cty_code, cty_title, cty_deduction_ind, cty_manual_ind, cty_deleted_ind, cty_relation_total_ind, cty_default_credits_ind, cty_default_credits, cty_create_usr_id, cty_create_timestamp, cty_update_usr_id, cty_update_timestamp, cty_tcr_id) "+ 
	 "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public void insManualPoint(Connection con, CreditsTypeBean ctyBean) throws SQLException {
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(INS_MANUAL_CREDIT);
			int index = 1;
			stmt.setString(index++, ctyBean.getCty_code());
			stmt.setString(index++, ctyBean.getCty_code());
			stmt.setBoolean(index++, ctyBean.getCty_deduction_ind());
			stmt.setBoolean(index++, ctyBean.getCty_manual_ind());
			stmt.setBoolean(index++, ctyBean.getCty_deleted_ind());
			stmt.setBoolean(index++, ctyBean.getCty_relation_total_ind());
			stmt.setBoolean(index++, ctyBean.getCty_default_credits_ind());
			stmt.setFloat(index++, ctyBean.getCty_default_credits());
			stmt.setString(index++, ctyBean.getCty_create_usr_id());
			stmt.setTimestamp(index++, ctyBean.getCty_create_timestamp());
			stmt.setString(index++, ctyBean.getCty_update_usr_id());
			stmt.setTimestamp(index++, ctyBean.getCty_update_timestamp());
			stmt.setLong(index++, ctyBean.getCty_tcr_id());
			stmt.executeUpdate();
		} finally {
			cwSQL.closePreparedStatement(stmt);
		}
		
	}
	
    private static final String SOFT_DEL_CREDIT ="UPDATE creditsType SET cty_deleted_ind = ?, cty_update_usr_id = ?, cty_update_timestamp = ? WHERE cty_id = ?";
	public void softDelPoint(Connection con, CreditsTypeBean ctyBean) throws SQLException {
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(SOFT_DEL_CREDIT);
			int index = 1;
			stmt.setBoolean(index++, ctyBean.getCty_deleted_ind());
			stmt.setString(index++, ctyBean.getCty_update_usr_id());
			stmt.setTimestamp(index++, ctyBean.getCty_update_timestamp());
			stmt.setInt(index++, ctyBean.getCty_id());
			stmt.executeUpdate();
		} finally {
			cwSQL.closePreparedStatement(stmt);
		}
	}
	
	private static final String GET_CTY_ID ="SELECT cty_id FROM creditsType WHERE cty_code = ? AND cty_deduction_ind = ? AND cty_deleted_ind = ? AND cty_manual_ind = ? AND cty_code <> ?";
	public int getManualIdByCode(Connection con, String cty_code, boolean cty_deduction_ind) throws SQLException {
        //按照 getManualBonusList 里面的条件来筛选   
		PreparedStatement stmt = null;
        ResultSet rs = null;
        int cty_id = 0;
        try {
            stmt = con.prepareStatement(GET_CTY_ID);
            int index = 1;
            stmt.setString(index++, cty_code);
            stmt.setBoolean(index++, cty_deduction_ind);
            stmt.setBoolean(index++, false);
            stmt.setBoolean(index++, true);
            stmt.setString(index++, Credit.ITM_IMPORT_CREDIT);
            rs = stmt.executeQuery();
            if (rs.next()) {
            	cty_id = rs.getInt("cty_id");
            }
        } finally {
            cwSQL.cleanUp(rs, stmt);
        }
        return cty_id;
    }
	
	/*
	 * 返回手工设置课程积分点ID
	 * */
	private static final String GET_MANUAL_COS_CTY_ID ="SELECT cty_id FROM creditsType WHERE cty_code = ? ";
	public static int getManualCosCtyId(Connection con) throws SQLException {
		PreparedStatement stmt = null;
        ResultSet rs = null;
        int cty_id = 0;
        try {
            stmt = con.prepareStatement(GET_MANUAL_COS_CTY_ID);
            int index = 1;
            stmt.setString(index++, Credit.ITM_IMPORT_CREDIT);
            rs = stmt.executeQuery();
            if (rs.next()) {
            	cty_id = rs.getInt("cty_id");
            }
        } finally {
            cwSQL.cleanUp(rs, stmt);
        }
        return cty_id;
    }
	
	/**
	 * Get manual credit point list.
	 * @param con Db connection
	 * @param deduction_ind Manual credit point type.
	 * @return 
	 * @throws SQLException
	 */
	public List getManualBonusList(Connection con, boolean deduction_ind, Vector cty_tcr_ids) throws SQLException {
		List bonus_lst = new ArrayList();
		String sql = "SELECT cty_id, cty_code FROM creditstype WHERE cty_deduction_ind = ? AND cty_manual_ind = ? AND cty_deleted_ind = ? AND cty_code <> ?";

		sql += " ORDER BY cty_code";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(sql);
			int index = 1;
			stmt.setBoolean(index++, deduction_ind);
			stmt.setBoolean(index++, true);
			stmt.setBoolean(index++, false);
			stmt.setString(index++, Credit.ITM_IMPORT_CREDIT);
			rs = stmt.executeQuery();
			while (rs.next()) {
				CreditsTypeBean creditsTypeBean = new CreditsTypeBean();
				creditsTypeBean.setCty_id(rs.getInt("cty_id")); 
				creditsTypeBean.setCty_code(rs.getString("cty_code"));
				bonus_lst.add(creditsTypeBean);
			}
		}finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return bonus_lst;
	}
	
	
	public static void copyAllAutoCreditTypeFromRootTC(Connection con,long tcr_id, long root_tcr_id, String cty_code) throws SQLException {
		String SQL1= null;
		if(cwSQL.DBVENDOR_MYSQL.equalsIgnoreCase(cwSQL.getDbType())){
			  SQL1= "  insert into creditsType (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp,cty_hit,cty_period,cty_tcr_id ,cty_update_timestamp) " +
				    " select cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp,cty_hit,cty_period,"+ tcr_id+" ,SYSDATE() from creditsType where cty_tcr_id = ? and cty_code =? ";
		}else{
			  SQL1= "  insert into creditsType (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp,cty_hit,cty_period,cty_tcr_id) " +
			    	" select cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp,cty_hit,cty_period,"+ tcr_id+" from creditsType where cty_tcr_id = ? and cty_code =? ";
		}
		PreparedStatement stmt = null;
	      try {
	          int index = 1;
	              stmt = con.prepareStatement(SQL1);
	              stmt.setLong(index++, root_tcr_id);
	              stmt.setString(index++, cty_code);
	              stmt.executeUpdate();
	        } finally {
	            stmt.close();
	        }
	    }

}