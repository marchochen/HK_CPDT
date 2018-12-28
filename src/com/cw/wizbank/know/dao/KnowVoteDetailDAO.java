package com.cw.wizbank.know.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.cw.wizbank.know.db.KnowVoteDetailDB;

/**
 * the operation of knowVoteDetail
 * 
 * @author DeanChen
 * 
 */
public class KnowVoteDetailDAO {

    public static final String INS = " INSERT INTO knowVoteDetail(kvd_que_id, kvd_ans_id, kvd_ent_id, kvd_create_usr_id, kvd_create_timestamp) "
            + " VALUES(?,?,?,?,?) ";

    /**
     * to insert one record.
     * 
     * @param con
     * @param voteDetailDb
     * @return
     * @throws SQLException
     */
    public boolean ins(Connection con, KnowVoteDetailDB voteDetailDb)
            throws SQLException {
        boolean isInsSuccess = false;
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement(INS);
            int index = 1;
            stmt.setInt(index++, voteDetailDb.getKvd_que_id());
            stmt.setInt(index++, voteDetailDb.getKvd_ans_id());
            stmt.setInt(index++, voteDetailDb.getKvd_ent_id());
            stmt.setString(index++, voteDetailDb.getKvd_create_usr_id());
            stmt.setTimestamp(index++, voteDetailDb.getKvd_create_timestamp());

            int insCount = stmt.executeUpdate();
            if (insCount != 1) {
                isInsSuccess = true;
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        return isInsSuccess;
    }

    public static final String IS_EXIST_VOTE = " SELECT kvd_que_id FROM knowVoteDetail "
            + " WHERE kvd_que_id = ? AND kvd_ans_id = ? AND kvd_ent_id = ? ";

    /**
     * to determine whether the vote is exist.
     * @param con
     * @param queId
     * @param ansId
     * @param userEntId
     * @return
     * @throws SQLException
     */
    public static boolean isExistVote(Connection con, int queId, int ansId,
            int userEntId) throws SQLException {
        boolean isExist = false;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = con.prepareStatement(IS_EXIST_VOTE);
            int index = 1;
            stmt.setInt(index++, queId);
            stmt.setInt(index++, ansId);
            stmt.setInt(index++, userEntId);

            rs = stmt.executeQuery();
            if (rs.next()) {
                isExist = true;
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
    
    private static String getDeleteByQueIdSql() {
    	StringBuffer sqlBuf = new StringBuffer();
    	sqlBuf.append("delete from knowVoteDetail where kvd_que_id = ? ");
    	return sqlBuf.toString();
    }
    
    /**
     * delete record according to question id.
     * @param con
     * @param queId
     * @throws SQLException
     */
    public static void deleteByQueId(Connection con, long queId) throws SQLException {
    	PreparedStatement stmt = null;
    	try {
    		stmt = con.prepareStatement(getDeleteByQueIdSql());
    		int index = 1;
    		stmt.setLong(index++, queId);
    		stmt.executeUpdate();
    	} finally {
    		if(stmt != null) {
    			stmt.close();
    		}
    	}
    }

}
