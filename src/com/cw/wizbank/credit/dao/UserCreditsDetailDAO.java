/**
 * 
 */
package com.cw.wizbank.credit.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.soap.Detail;

import com.cw.wizbank.Dispatcher;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.credit.db.UserCreditsDetailDB;

/**
 * @author DeanChen
 * 
 */
public class UserCreditsDetailDAO {

    private static final String INS_UCD = " INSERT INTO userCreditsDetail"+ (cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType()) ? " with(rowlock) ":"") 
                    + " (ucd_ent_id,ucd_cty_id,ucd_itm_id,ucd_total,ucd_hit,ucd_hit_temp ," +
    		" ucd_create_timestamp,ucd_create_usr_id,ucd_update_timestamp,ucd_update_usr_id,ucd_app_id) VALUES(?,?,?,?,?,?,?,?,?,?,?) ";

    /**
     * to insert a record into table UserCreditsDetail.
     * 
     * @param con
     * @param ucdDb
     * @throws SQLException
     */
    public void ins(Connection con, UserCreditsDetailDB ucdDb)
            throws SQLException {
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(INS_UCD);
            int index = 1;
            stmt.setInt(index++, ucdDb.getUcd_ent_id());
            stmt.setInt(index++, ucdDb.getUcd_cty_id());
            stmt.setLong(index++, ucdDb.getUcd_itm_id());
//            stmt.setFloat(index++, ucdDb.getUcd_total());
            
            if(cwSQL.getDbProductName().indexOf(cwSQL.ProductName_MSSQL) >= 0){
				stmt.setString(index++,  ucdDb.getUcd_total() + "");
			} else {
				stmt.setFloat(index++, ucdDb.getUcd_total());
			}
            
            stmt.setInt(index++, ucdDb.getUcd_hit());
            stmt.setInt(index++, ucdDb.getUcd_hit_temp());
            stmt.setTimestamp(index++, ucdDb.getUcd_create_timestamp());
            stmt.setString(index++, ucdDb.getUcd_create_usr_id());
            stmt.setTimestamp(index++, ucdDb.getUcd_update_timestamp());
            stmt.setString(index++, ucdDb.getUcd_update_usr_id());
            stmt.setLong(index++, ucdDb.getUcd_app_id());
            int insCount = stmt.executeUpdate();
            if (insCount != 1) {
                throw new SQLException(
                        " Failed to add a record of UserCreditsDetail. ");
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    private static final String UPD_UCD = " UPDATE userCreditsDetail "+ (cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType()) ? " with(rowlock) ":"") 
                    + " SET ucd_total =  ?, "
            + " ucd_hit = ?, ucd_hit_temp = ? ,ucd_update_timestamp = ?,ucd_update_usr_id = ?"
            + " WHERE ucd_ent_id = ? AND ucd_cty_id = ? ";
    
    private static final String DEL_UCD = " DELETE userCreditsDetail "+ (cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType()) ? " with(rowlock) ":"") 
    + " WHERE ucd_ent_id = ? AND ucd_cty_id = ? ";
    
    private static final String SEL_UCD = " SELECT * FROM  userCreditsDetail "+ (cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType()) ? " with(rowlock) ":"") 
    	    + " WHERE ucd_ent_id = ? AND ucd_cty_id = ? ";

    /**
     * to update UserCreditsDetail by id.
     * 
     * @param con
     * @param ucdDb
     * @throws SQLException
     */
    public void upd(Connection con, UserCreditsDetailDB ucdDb)
            throws SQLException {
        PreparedStatement stmt = null;
        ResultSet  rs=null;
        try {
        	String sql = null;
        	if (ucdDb.getUcd_itm_id() > 0 && ucdDb.getUcd_app_id() > 0) {
        		sql = UPD_UCD + " AND ucd_itm_id = ? AND ucd_app_id = ? ";
        	} else {
        		sql = UPD_UCD;
        	}
            stmt = con.prepareStatement(sql);
            int index = 1;
//            stmt.setFloat(index++, ucdDb.getUcd_total());
            
            if(cwSQL.getDbProductName().indexOf(cwSQL.ProductName_MSSQL) >= 0){
				stmt.setString(index++,  ucdDb.getUcd_total() + "");
			} else {
				stmt.setFloat(index++, ucdDb.getUcd_total());
			}
            
            stmt.setInt(index++, ucdDb.getUcd_hit());
            stmt.setInt(index++, ucdDb.getUcd_hit_temp());
            stmt.setTimestamp(index++, ucdDb.getUcd_update_timestamp());
            stmt.setString(index++, ucdDb.getUcd_update_usr_id());
            stmt.setInt(index++, ucdDb.getUcd_ent_id());
            stmt.setInt(index++, ucdDb.getUcd_cty_id());
            if (ucdDb.getUcd_itm_id() > 0 && ucdDb.getUcd_app_id() > 0) {
            	stmt.setLong(index++, ucdDb.getUcd_itm_id());
            	stmt.setLong(index++, ucdDb.getUcd_app_id());
            }
            int updCount = stmt.executeUpdate();
            if (updCount != 1) {
               // throw new SQLException( " Failed to update a record of userCreditsDetail. ");
            	if (ucdDb.getUcd_itm_id() > 0 && ucdDb.getUcd_app_id() > 0) {
            		sql = SEL_UCD + " AND ucd_itm_id = ? AND ucd_app_id = ? ";
            	} else {
            		sql = SEL_UCD;
            	}
            	stmt = con.prepareStatement(sql);
            	index = 1;
                 stmt.setInt(index++, ucdDb.getUcd_ent_id());
                 stmt.setInt(index++, ucdDb.getUcd_cty_id());
                 if (ucdDb.getUcd_itm_id() > 0 && ucdDb.getUcd_app_id() > 0) {
                 	stmt.setLong(index++, ucdDb.getUcd_itm_id());
                 	stmt.setLong(index++, ucdDb.getUcd_app_id());
                 }
                 rs=stmt.executeQuery();
                 UserCreditsDetailDB credit_datail=new UserCreditsDetailDB();
                 if(rs.next()){
                	credit_datail.setUcd_ent_id(rs.getInt("ucd_ent_id"));
                	credit_datail.setUcd_cty_id(rs.getInt("ucd_cty_id"));
                	credit_datail.setUcd_itm_id(rs.getLong("ucd_itm_id"));
                	credit_datail.setUcd_total(rs.getFloat("ucd_total"));
                	credit_datail.setUcd_hit(rs.getInt("ucd_hit"));
                	credit_datail.setUcd_create_timestamp(rs.getTimestamp("ucd_create_timestamp"));
                	credit_datail.setUcd_create_usr_id(rs.getString("ucd_create_usr_id"));
                	credit_datail.setUcd_update_timestamp(rs.getTimestamp("ucd_update_timestamp"));
                	credit_datail.setUcd_update_usr_id(rs.getString("ucd_update_usr_id"));
                	credit_datail.setUcd_app_id(rs.getLong("ucd_app_id"));
                 }
                 
                if (ucdDb.getUcd_itm_id() > 0 && ucdDb.getUcd_app_id() > 0) {
             		sql = DEL_UCD + " AND ucd_itm_id = ? AND ucd_app_id = ? ";
             	} else {
             		sql = DEL_UCD;
             	}
             	stmt = con.prepareStatement(sql);
             	index = 1;
                  stmt.setInt(index++, ucdDb.getUcd_ent_id());
                  stmt.setInt(index++, ucdDb.getUcd_cty_id());
                  if (ucdDb.getUcd_itm_id() > 0 && ucdDb.getUcd_app_id() > 0) {
                  	stmt.setLong(index++, ucdDb.getUcd_itm_id());
                  	stmt.setLong(index++, ucdDb.getUcd_app_id());
                  }
                 stmt.executeUpdate();
                 
                UserCreditsDetailDAO dao=new UserCreditsDetailDAO();
                dao.ins(con, credit_datail);
                	
             
               
            	
//                System.out.println(" Failed to update a record of userCreditsDetail. updCount=" + updCount);
//                System.out.println(" ucdDb.getUcd_total()=" + ucdDb.getUcd_total() 
//                        + ";  getUcd_hit_temp()=" + ucdDb.getUcd_hit_temp() 
//                        + ";  getUcd_update_timestamp()=" + ucdDb.getUcd_update_timestamp() 
//                        + ";  getUcd_update_usr_id()=" + ucdDb.getUcd_update_usr_id() 
//                        + ";  getUcd_ent_id()=" + ucdDb.getUcd_ent_id() 
//                        + ";  getUcd_cty_id()=" + ucdDb.getUcd_cty_id()
//                        + ";  getUcd_itm_id()=" + ucdDb.getUcd_itm_id() 
//                        + ";  getUcd_app_id()=" + ucdDb.getUcd_app_id() 
//                         );
            }
        } finally {
        	if (rs != null){
            	rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            
        }
    }

    private static final String GET_UCD = " SELECT ucd_ent_id,ucd_cty_id,ucd_itm_id,ucd_total,ucd_hit,"
            + " ucd_hit_temp ,ucd_create_timestamp, ucd_create_usr_id,ucd_update_timestamp,ucd_update_usr_id "
            + " FROM userCreditsDetail "+ (cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType()) ? " with(nolock) ":"") 
                    + " WHERE ucd_ent_id = ? AND ucd_cty_id = ? ";

    /**
     * to get UserCreditsDetail record.
     * 
     * @param con
     * @param ucdDb
     * @return
     * @throws SQLException
     */
    public boolean get(Connection con, UserCreditsDetailDB ucdDb)
            throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean isExist = false;
        try {
        	String sql = null;
        	if (ucdDb.getUcd_itm_id() > 0 && ucdDb.getUcd_app_id() > 0) {
        		sql = GET_UCD + " AND ucd_itm_id = ? AND ucd_app_id = ? ";
        	} else {
        		sql = GET_UCD;
        	}
            stmt = con.prepareStatement(sql);
            int index = 1;
            stmt.setInt(index++, ucdDb.getUcd_ent_id());
            stmt.setInt(index++, ucdDb.getUcd_cty_id());
            if (ucdDb.getUcd_itm_id() > 0 && ucdDb.getUcd_app_id() > 0) {
            	stmt.setLong(index++, ucdDb.getUcd_itm_id());
            	stmt.setLong(index++, ucdDb.getUcd_app_id());
            }
            rs = stmt.executeQuery();
            if (rs.next()) {
                isExist = true;
                ucdDb.setUcd_total(rs.getFloat("ucd_total"));
                ucdDb.setUcd_hit(rs.getInt("ucd_hit"));
                ucdDb.setUcd_hit_temp(rs.getInt("ucd_hit_temp"));
                ucdDb.setUcd_create_timestamp(rs
                        .getTimestamp("ucd_create_timestamp"));
                ucdDb.setUcd_create_usr_id(rs.getString("ucd_create_usr_id"));
                ucdDb.setUcd_update_timestamp(rs
                        .getTimestamp("ucd_update_timestamp"));
                ucdDb.setUcd_update_usr_id(rs.getString("ucd_update_usr_id"));
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }
        return isExist;
    }
    
    public void delete(Connection con, UserCreditsDetailDB ucdDb) throws SQLException {
    	PreparedStatement stmt = null;
    	String sql = " DELETE FROM userCreditsDetail WHERE ucd_ent_id = ? AND ucd_itm_id = ? AND ucd_app_id = ? ";
        try {
            stmt = con.prepareStatement(sql);
            int index = 1;
            stmt.setInt(index++, ucdDb.getUcd_ent_id());
            stmt.setLong(index++, ucdDb.getUcd_itm_id());
            stmt.setLong(index++, ucdDb.getUcd_app_id());
            int deleteCnt = stmt.executeUpdate();
            if (deleteCnt > 0) {
            	Dispatcher.creditsLogger.debug("===Delete=== ucd_ent_id: " +  ucdDb.getUcd_ent_id() + "\t" + "ucd_itm_id: " + ucdDb.getUcd_itm_id() 
            								+ "\t" + "ucd_app_id: " + ucdDb.getUcd_app_id() + "\t" + "deleteCnt: " + deleteCnt);
            }
        } finally {
            cwSQL.closePreparedStatement(stmt);
        }
    }

}
