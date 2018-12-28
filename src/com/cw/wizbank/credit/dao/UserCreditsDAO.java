/**
 * 
 */
package com.cw.wizbank.credit.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.cw.wizbank.credit.db.UserCreditsDB;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwUtils;

/**
 * @author DeanChen
 * 
 */
public class UserCreditsDAO {

    private static final String INS_UCT = " INSERT INTO userCredits(uct_ent_id,uct_total, uct_update_timestamp) VALUES(?,?,?) ";

    public void ins(Connection con, UserCreditsDB creditsDb)
            throws SQLException {
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(INS_UCT);
            int index = 1;
            stmt.setInt(index++, creditsDb.getUct_ent_id());
//            stmt.setFloat(index++, creditsDb.getUct_total());
            
            if(cwSQL.getDbProductName().indexOf(cwSQL.ProductName_MSSQL) >= 0){
				stmt.setString(index++,  creditsDb.getUct_total() + "");
			} else {
				stmt.setFloat(index++,  creditsDb.getUct_total());
			}
            
            stmt.setTimestamp(index++, creditsDb.getUct_update_timestmp());

            int insCount = stmt.executeUpdate();
            if (insCount != 1) {
                con.rollback();
                throw new SQLException(
                        " Failed to add a record of userCredits. ");
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    private static final String UPD_UCT = " UPDATE userCredits SET uct_total = ?, uct_update_timestamp = ?  WHERE uct_ent_id = ? ";

    public void upd(Connection con, UserCreditsDB creditsDb)
            throws SQLException {
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(UPD_UCT);
            int index = 1;
//            stmt.setFloat(index++, creditsDb.getUct_total());
            if(cwSQL.getDbProductName().indexOf(cwSQL.ProductName_MSSQL) >= 0){
				stmt.setString(index++,  cwUtils.formatNumber(creditsDb.getUct_total(), 2)   + "");
			} else {
				stmt.setFloat(index++,  creditsDb.getUct_total());
			}
            stmt.setTimestamp(index++, creditsDb.getUct_update_timestmp());
            stmt.setInt(index++, creditsDb.getUct_ent_id());

            stmt.executeUpdate();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    private static final String IS_EXIST_UCT = " SELECT uct_ent_id FROM userCredits WHERE uct_ent_id = ? ";

    public static boolean isExistUct(Connection con, int uctEntId)
            throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean isExist = false;
        try {
            stmt = con.prepareStatement(IS_EXIST_UCT);
            int index = 1;
            stmt.setInt(index++, uctEntId);

            rs = stmt.executeQuery();
            if (rs.next()) {
                isExist = true;
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        return isExist;
    }
    
}
