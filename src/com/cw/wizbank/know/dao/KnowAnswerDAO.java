package com.cw.wizbank.know.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.cw.wizbank.know.db.KnowAnswerDB;
import com.cw.wizbank.util.cwSQL;

/**
 * @author DeanChen
 * 
 */
public class KnowAnswerDAO {

    public static final String ANS_STATUS_OK = "OK";

    public static final String ANS_STATUS_DELETED = "DELETED";

    private static final String INS_ANS = " INSERT INTO knowAnswer (ans_que_id, ans_content, ans_content_search, ans_refer_content, ans_right_ind, ans_status, ans_create_ent_id, ans_create_timestamp, ans_update_ent_id, ans_update_timestamp ) VALUES(?,?,?,?,?,?,?,?,?,?) ";

    /**
     * to insert a record into table knowAnswer.
     * 
     * @param con
     * @param ansObj
     * @throws SQLException
     */
    public void ins(Connection con, KnowAnswerDB ansDb) throws SQLException {
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(INS_ANS);
            int index = 1;
            stmt.setInt(index++, ansDb.getAns_que_id());
            stmt.setString(index++, ansDb.getAns_content());
            stmt.setString(index++, ansDb.getAns_content_search());
            stmt.setString(index++, ansDb.getAns_refer_content());
            stmt.setBoolean(index++, ansDb.isAns_right_ind());
            stmt.setString(index++, ansDb.getAns_status());
            stmt.setInt(index++, ansDb.getAns_create_ent_id());
            stmt.setTimestamp(index++, ansDb.getAns_create_timestamp());
            stmt.setInt(index++, ansDb.getAns_update_ent_id());
            stmt.setTimestamp(index++, ansDb.getAns_update_timestamp());

            int insCount = stmt.executeUpdate();
            if (insCount != 1) {
                throw new SQLException("Failed to add new answer of know.");
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    private static final String GET_ANS = " SELECT ans_que_id, ans_content, ans_refer_content, ans_right_ind, ans_status, ans_create_ent_id, ans_create_timestamp,"
            + "  ans_vote_total, ans_vote_for, ans_vote_down, ans_temp_vote_total, ans_temp_vote_for, "
            + " ans_update_ent_id, ans_update_timestamp FROM knowAnswer where ans_id = ? AND ans_status = ? ";

    /**
     * to get answer by id.
     * 
     * @param con
     * @param ansDb
     * @return
     * @throws SQLException
     */
    public boolean get(Connection con, KnowAnswerDB ansDb) throws SQLException {
        boolean isExist = true;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(GET_ANS);
            int index = 1;
            stmt.setInt(index++, ansDb.getAns_id());
            stmt.setString(index++, ANS_STATUS_OK);
            rs = stmt.executeQuery();
            if (rs.next()) {
                ansDb.setAns_que_id(rs.getInt("ans_que_id"));
                ansDb
                        .setAns_content(cwSQL.getClobValue(rs, "ans_content"));
                ansDb.setAns_refer_content(rs.getString("ans_refer_content"));
                ansDb.setAns_right_ind(rs.getBoolean("ans_right_ind"));
                ansDb.setAns_create_ent_id(rs.getInt("ans_create_ent_id"));
                ansDb.setAns_create_timestamp(rs
                        .getTimestamp("ans_create_timestamp"));
                ansDb.setAns_update_ent_id(rs.getInt("ans_update_ent_id"));
                ansDb.setAns_update_timestamp(rs
                        .getTimestamp("ans_update_timestamp"));
                ansDb.setAns_vote_total(rs.getInt("ans_vote_total"));
                ansDb.setAns_vote_for(rs.getInt("ans_vote_for"));
                ansDb.setAns_vote_down(rs.getInt("ans_vote_down"));
                ansDb.setAns_temp_vote_total(rs.getInt("ans_temp_vote_total"));
                ansDb.setAns_temp_vote_for(rs.getInt("ans_temp_vote_for"));
            } else {
                isExist = false;
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        return isExist;
    }

    private static final String DEL_ANS_BY_QUE_ID = " delete from knowAnswer where ans_status = ? AND ans_que_id = ? ";

    /**
     * to delete answer by question id.
     * 
     * @param con
     * @param queId
     * @throws SQLException
     */
    public static void delAnsByQueId(Connection con, long queId)
            throws SQLException {
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(DEL_ANS_BY_QUE_ID);
            int index = 1;
            stmt.setString(index++, ANS_STATUS_OK);
            stmt.setLong(index++, queId);
            stmt.executeUpdate();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    private static final String UPD_ANS_VOTE = " UPDATE knowAnswer SET ans_vote_total = ? ,ans_vote_for = ?, ans_vote_down = ?,"
            + "  ans_temp_vote_total = ?, ans_temp_vote_for = ?, ans_temp_vote_for_down_diff = ?, "
            + " ans_update_ent_id = ?, ans_update_timestamp = ? WHERE ans_id = ? ";

    /**
     * to update the vote of answer
     * 
     * @param con
     * @param ansDb
     * @throws SQLException
     */
    public void updVote(Connection con, KnowAnswerDB ansDb) throws SQLException {
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(UPD_ANS_VOTE);
            int index = 1;
            stmt.setInt(index++, ansDb.getAns_vote_total());
            stmt.setInt(index++, ansDb.getAns_vote_for());
            stmt.setInt(index++, ansDb.getAns_vote_down());
            stmt.setInt(index++, ansDb.getAns_temp_vote_total());
            stmt.setInt(index++, ansDb.getAns_temp_vote_for());
            stmt.setInt(index++, ansDb.getAns_temp_vote_for_down_diff());
            stmt.setInt(index++, ansDb.getAns_update_ent_id());
            stmt.setTimestamp(index++, ansDb.getAns_update_timestamp());
            stmt.setInt(index++, ansDb.getAns_id());
            int updCount = stmt.executeUpdate();
            if (updCount != 1) {
                throw new SQLException(
                        " Failed to update a record of kownQuestion. question id :"
                                + ansDb.getAns_id());
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    private static final String UPD_ANS_RIGHT = " UPDATE knowAnswer SET ans_right_ind = ?, ans_update_timestamp = ? WHERE ans_id = ? ";

    /**
     * to update answer right.
     * 
     * @param con
     * @param queId
     * @throws SQLException
     */
    public static void updAnsRight(Connection con, int ansId)
            throws SQLException {
        PreparedStatement stmt = null;
        Timestamp curTime = cwSQL.getTime(con);
        try {
            stmt = con.prepareStatement(UPD_ANS_RIGHT);
            int index = 1;
            stmt.setBoolean(index++, true);
            stmt.setTimestamp(index++, curTime);
            stmt.setInt(index++, ansId);
            int updCount = stmt.executeUpdate();
            if (updCount != 1) {
                throw new SQLException(
                        " Failed to update a record of kownAnswer. answer id :"
                                + ansId);
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    private static final String DEL_INVAIL_TEMP_VOTE = " UPDATE knowAnswer"
            + " SET ans_temp_vote_total = 0 ,ans_temp_vote_for = 0, ans_temp_vote_for_down_diff = 0"
            + " WHERE ans_status = ? and ans_id in ";

    /**
     * to clear invail temp vote.
     * 
     * @param con
     * @param ansIdsStr
     * @throws SQLException
     */
    public static void clearTempVote(Connection con, String ansIdsStr)
            throws SQLException {
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(DEL_INVAIL_TEMP_VOTE + ansIdsStr);
            int index = 1;
            stmt.setString(index++, KnowAnswerDAO.ANS_STATUS_OK);
            stmt.executeUpdate();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }
    
    /**
	 * 删除问题所属的所有答案
	 */
	private static String DEL_ALL_ANS = " delete from knowAnswer where ans_que_id = ? ";
	public static void delAllAnsByQueId(Connection con, long que_id) throws SQLException {
		PreparedStatement stmt = null;
		stmt = con.prepareStatement(DEL_ALL_ANS);
		int index = 1;
		stmt.setLong(index++, que_id);
		stmt.executeUpdate();
		stmt.close();
	}
	
	private static String GET_FAQ_ANS_CONTENT = " select ans_content from knowAnswer where ans_que_id = ? and ans_status = ? ";
	public static String getAnsContentByQueId(Connection con, int queId) throws SQLException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String ansContent = null;
		try {
			stmt = con.prepareStatement(GET_FAQ_ANS_CONTENT);
			int index = 1;
			stmt.setInt(index++, queId);
			stmt.setString(index++, ANS_STATUS_OK);
			rs = stmt.executeQuery();
			if(rs.next()) {
				ansContent = rs.getString("ans_content");
			}
		} finally {
			cwSQL.closePreparedStatement(stmt);
        }
		return ansContent;
	}
	
}
