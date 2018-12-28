package com.cw.wizbank.know.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import com.cw.wizbank.know.db.KnowQuestionDB;
import com.cw.wizbank.know.view.ViewKnowCatalogRelationDAO;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwUtils;

public class KnowQuestionDAO {

    // the type of question for web
    public static final String QUE_TYPE_ALL = "ALL";

    public static final String QUE_TYPE_UNSOLVED = "UNSOLVED";

    public static final String QUE_TYPE_SOLVED = "SOLVED";
    
    public static final String QUE_TYPE_FAQ = "FAQ";

    public static final String QUE_TYPE_POPULAR = "POPULAR";

    // question status

    public static final String QUE_STATUS_OK = "OK";

    public static final String QUE_STATUS_DELETED = "DELETED";

    public static final String QUE_STATUS_CANCEL = "CANCEL";

    /**
     * the sql of insert question.
     */
    private static final String INS_QUE = " INSERT INTO knowQuestion (que_title, que_content, que_type, que_popular_ind, que_reward_credits, que_status, que_create_ent_id, que_create_timestamp, que_update_ent_id, que_update_timestamp, que_answered_timestamp) VALUES(?,?,?,?,?,?,?,?,?,?,?) ";

    /**
     * To insert a record into table knowquestion. 插入问题记录
     * 
     * @param con
     * @param queObj
     * @throws SQLException
     */
    public void ins(Connection con, KnowQuestionDB queObj) throws SQLException {
        PreparedStatement stmt = null;
        int queId = 0;
        try {
            stmt = con.prepareStatement(INS_QUE,
                    PreparedStatement.RETURN_GENERATED_KEYS);
            int index = 1;
            stmt.setString(index++, queObj.getQue_title());
            stmt.setString(index++, queObj.getQue_content());
            stmt.setString(index++, queObj.getQue_type());
            stmt.setBoolean(index++, queObj.isQue_popular_ind());
            stmt.setLong(index++, queObj.getQue_reward_credits());
            stmt.setString(index++, queObj.getQue_status());
            stmt.setLong(index++, queObj.getQue_create_ent_id());
            stmt.setTimestamp(index++, queObj.getQue_create_timestamp());
            stmt.setLong(index++, queObj.getQue_update_ent_id());
            stmt.setTimestamp(index++, queObj.getQue_update_timestamp());
            stmt.setTimestamp(index++, queObj.getQue_answered_timestamp());
            int insCount = stmt.executeUpdate();
            if (insCount != 1) {
                con.rollback();
                throw new SQLException(
                        " Failed to add a record of kownQuestion. ");
            }
            queId = (int) cwSQL.getAutoId(con, stmt, "knowQuestion", "que_id");
            queObj.setQue_id(queId);
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    /**
     * to update question by id.
     * 
     * @param con
     * @param queDb
     * @throws SQLException
     */
    public void upd(Connection con, KnowQuestionDB queDb) throws SQLException {
    	String UPD_QUE = " UPDATE knowQuestion SET que_content = ? , que_update_ent_id = ? , que_update_timestamp = ? ";
    	if (queDb.getQue_title() != null && queDb.getQue_title().length() > 0) {
    		UPD_QUE += ", que_title = ? ";
    	}
    	UPD_QUE += " WHERE que_id = ? ";
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(UPD_QUE);
            int index = 1;
            stmt.setString(index++, queDb.getQue_content());
            stmt.setInt(index++, queDb.getQue_update_ent_id());
            stmt.setTimestamp(index++, queDb.getQue_update_timestamp());
            if (queDb.getQue_title() != null && queDb.getQue_title().length() > 0) {
            	stmt.setString(index++, queDb.getQue_title());
            }
            stmt.setInt(index++, queDb.getQue_id());
            int updCount = stmt.executeUpdate();
            if (updCount != 1) {
                throw new SQLException(
                        " Failed to update a record of kownQuestion. question id :"
                                + queDb.getQue_id());
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }
    
    private static final String DEL_QUE = " delete from knowQuestion WHERE que_id = ? ";
    public void del(Connection con, long que_id) throws SQLException {
		PreparedStatement stmt = null;
		try {
			int kca_id = KnowCatalogRelationDAO.getQueParentCatId(con, que_id);
			if(kca_id > 0){
				ViewKnowCatalogRelationDAO.updCatQueCount(con, kca_id, -1);
			}
			
		    KnowAnswerDAO.delAllAnsByQueId(con, que_id); // 删除答案
		    
		    stmt = con.prepareStatement(DEL_QUE); // 删除问题
			int index = 1;
			stmt.setLong(index++, que_id);
			int delCount = stmt.executeUpdate(); 
            stmt.close();
			if (delCount != 1) {
				throw new SQLException(" Failed to update a record of kownQuestion. question id :" + que_id);
			}
		} finally {
			if (stmt != null) {
				stmt.close();
			}
			
		}
	}

    private static final String UPD_QUE_TYPE = " UPDATE knowQuestion SET que_type = ?, que_answered_timestamp = ? WHERE que_id = ? ";

    /**
     * to update type of question.
     * 
     * @param con
     * @param queId
     * @throws SQLException
     */
    public static void updQueType(Connection con, int queId, String queType)
            throws SQLException {
        PreparedStatement stmt = null;
        Timestamp curTime = cwSQL.getTime(con);
        try {
            stmt = con.prepareStatement(UPD_QUE_TYPE);
            int index = 1;
            stmt.setString(index++, queType);
            stmt.setTimestamp(index++, curTime);
            stmt.setInt(index++, queId);
            int updCount = stmt.executeUpdate();
            if (updCount != 1) {
                throw new SQLException(
                        " Failed to update a record of kownQuestion. question id :"
                                + queId);
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    private static final String DEL_QUE_BY_QUE_ID = " delete from knowQuestion WHERE que_id = ? ";

    /**
     * to delete question by question id.
     * 
     * @param con
     * @param queId
     * @throws SQLException
     */
    public static void delQueByQueId(Connection con, int queId)
            throws SQLException {
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(DEL_QUE_BY_QUE_ID);
            int index = 1;
            stmt.setInt(index++, queId);
            stmt.executeUpdate();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    private static final String GET_QUE_CNT_BY_CREATOR = " SELECT COUNT(que_id) AS que_count FROM knowQuestion WHERE que_create_ent_id = ? AND que_status = ? ";

    /**
     * to get the count of questions by creator. <br>
     * 获取用户已回答问题的数目
     * 
     * @param con
     * @param userEntId
     * @return the count of questions
     * @throws SQLException
     */
    public static int getQueCountByCreator(Connection con, long userEntId)
            throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int queCount = 0;
        try {
            stmt = con.prepareStatement(GET_QUE_CNT_BY_CREATOR);
            int index = 1;
            stmt.setLong(index++, userEntId);
            stmt.setString(index++, QUE_STATUS_OK);
            rs = stmt.executeQuery();
            if (rs.next()) {
                queCount = rs.getInt("que_count");
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        return queCount;
    }

    private static final String GET_QUE_BY_QUE_ID = " SELECT kcr_ancestor_kca_id, que_title, que_content, que_type, "
            + " que_create_ent_id, que_update_timestamp FROM knowQuestion"+ (cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType()) ? " with(nolock) ":"") 
                    + ", knowCatalogRelation"+ (cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType()) ? " with(nolock) ":"") 
                    + " WHERE kcr_child_kca_id = que_id and kcr_type = ? and kcr_parent_ind = ? and que_id = ? AND que_status = ? ";

    /**
     * to get question by id.
     * 
     * @param con
     * @param knowQueDb
     * @throws SQLException
     */
    public boolean getQueByQueId(Connection con, KnowQuestionDB knowQueDb)
            throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean isExist = false;
        try {
            stmt = con.prepareStatement(GET_QUE_BY_QUE_ID);
            int index = 1;
            stmt.setString(index++, KnowCatalogRelationDAO.QUE_PARENT_KCA);
            stmt.setBoolean(index++, true);
            stmt.setLong(index++, knowQueDb.getQue_id());
            stmt.setString(index++, QUE_STATUS_OK);
            rs = stmt.executeQuery();
            if (rs.next()) {
                isExist = true;
                knowQueDb.setQue_kca_id(rs.getInt("kcr_ancestor_kca_id"));
                knowQueDb.setQue_title(rs.getString("que_title"));
                knowQueDb.setQue_content(rs.getString("que_content"));
                knowQueDb.setQue_type(rs.getString("que_type"));
                knowQueDb.setQue_create_ent_id(rs.getInt("que_create_ent_id"));
                knowQueDb.setQue_update_timestamp(rs.getTimestamp("que_update_timestamp"));
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

    private static final String GET_DIFF_STATUS_QUE_CNT = " SELECT que_type, COUNT(que_id) AS que_count "
            + " FROM knowQuestion"
            + " inner join knowCatalogRelation on (kcr_child_kca_id = que_id and kcr_parent_ind = ? and kcr_type = ?)"
			+ " inner join knowcatalog on (kca_id = kcr_ancestor_kca_id)" 
			+ " inner join tcTrainingCenter on (tcr_id = kca_tcr_id and tcr_ste_ent_id = ?)"
			+ " WHERE que_status = ? GROUP BY que_type ";

    /**
	 * to get the count of unsolved questions and solved questions.<br>
	 * 获取待解决和未解决问题的数目
	 * 
	 * @param con
     * @param steEntId
     * @param tcIds
	 * @return
	 * @throws SQLException
	 */
    public static HashMap getDiffStatusQueCnt(Connection con, long steEntId)
            throws SQLException {
        HashMap diffStatQueMap = new HashMap();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(GET_DIFF_STATUS_QUE_CNT);
            int index = 1;
            stmt.setBoolean(index++, true);
            stmt.setString(index++, KnowCatalogRelationDAO.QUE_PARENT_KCA);
            stmt.setLong(index++, steEntId);
            stmt.setString(index++, QUE_STATUS_OK);
            rs = stmt.executeQuery();

            String queType;
            int solvedQueCount = 0;
            int unSolvedQueCount = 0;
            while (rs.next()) {
            	queType = rs.getString("que_type");
                if (queType.equals(QUE_TYPE_SOLVED)) {
                    solvedQueCount = rs.getInt("que_count");
                } else if(queType.equals(QUE_TYPE_UNSOLVED)){
                    unSolvedQueCount = rs.getInt("que_count");
                }
            }

            // set question map
            diffStatQueMap.put(QUE_TYPE_SOLVED, new Integer(solvedQueCount));
            diffStatQueMap.put(QUE_TYPE_UNSOLVED, new Integer(unSolvedQueCount));

        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }

        return diffStatQueMap;
    }

    
    private static final String GET_QUE_RELATION_QUE = " SELECT rel_que.que_id,rel_que.que_title, rel_que.que_type, rel_que.que_popular_ind "
            + " FROM knowQuestion rel_que"
            + " inner join knowQuestion cur_que ON"
            + "     (cur_que.que_id = ? and cur_que.que_type = rel_que.que_type and cur_que.que_popular_ind = rel_que.que_popular_ind) "
            + " inner join knowCatalogRelation cur_cat on(cur_cat.kcr_child_kca_id = cur_que.que_id and cur_cat.kcr_type = ? and cur_cat.kcr_parent_ind = ?) "
            + " inner join knowCatalogRelation rel_cat on (rel_cat.kcr_ancestor_kca_id = cur_cat.kcr_ancestor_kca_id and rel_cat.kcr_child_kca_id = rel_que.que_id and rel_cat.kcr_type = ? and rel_cat.kcr_parent_ind = ?)"
            + " WHERE rel_que.que_id <> ? AND rel_que.que_status = ? ORDER BY rel_que.que_create_timestamp DESC ";

    private static final int RELATION_QUE_NUM = 5;

    /**
     * to get all relation record of question.
     * 
     * @param con
     * @param queId
     * @return
     * @throws SQLException
     */
    public static Vector getRelationQue(Connection con, int queId)
            throws SQLException {
        Vector queVec = new Vector();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(GET_QUE_RELATION_QUE);
            int index = 1;
            stmt.setInt(index++, queId);
            stmt.setString(index++, KnowCatalogRelationDAO.QUE_PARENT_KCA);
            stmt.setBoolean(index++, true);
            stmt.setString(index++, KnowCatalogRelationDAO.QUE_PARENT_KCA);
            stmt.setBoolean(index++, true);
            stmt.setInt(index++, queId);
            stmt.setString(index++, QUE_STATUS_OK);
            rs = stmt.executeQuery();

            int queCount = 0;
            while (rs.next()) {
                queCount++;
                if (queCount > RELATION_QUE_NUM) {
                    break;
                }
                KnowQuestionDB knowQueDb = new KnowQuestionDB();
                knowQueDb.setQue_id(rs.getInt("que_id"));
                knowQueDb.setQue_title(rs.getString("que_title"));
                knowQueDb.setQue_type(rs.getString("que_type"));
                knowQueDb.setQue_popular_ind(rs.getBoolean("que_popular_ind"));
                queVec.addElement(knowQueDb);
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }

        return queVec;
    }

    private static final String DEL_POPULAR_QUE_MARK = " UPDATE knowQuestion SET que_popular_ind = 0 ,que_popular_timestamp = null "
            + " WHERE que_popular_ind = 1 and que_status = ?";

    /**
     * to delete popular mark of all popular questions.
     * 
     * @param con
     * @throws SQLException
     */
    public static void delDelAllPopularMark(Connection con) throws SQLException {
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(DEL_POPULAR_QUE_MARK);
            int index = 1;
            stmt.setString(index++, KnowQuestionDAO.QUE_STATUS_OK);
            stmt.executeUpdate();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    private static final String MARK_POPULAR_QUE = " UPDATE knowQuestion "
            + " set que_popular_ind = 1 ,que_popular_timestamp = ?  where que_id in ";

    /**
     * to mark popular question.
     * 
     * @param con
     * @param queIdStr
     * @throws SQLException
     */
    public static void MarkPopularQue(Connection con, String queIdStr)
            throws SQLException {
        String sql = MARK_POPULAR_QUE + queIdStr;
        PreparedStatement stmt = null;
        Timestamp curTime = cwSQL.getTime(con);
        try {
            stmt = con.prepareStatement(sql);
            int index = 1;
            stmt.setTimestamp(index++, curTime);
            stmt.executeUpdate();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    /**
     * to validate whether vote is overdue.
     * 
     * @param con
     * @param overdueDay
     * @return
     * @throws SQLException
     */
    public static boolean isOverdueVote(Connection con, int queId,
            int overdueDay) throws SQLException {
        boolean isOverdue = true;
        
        Timestamp curTime = cwSQL.getTime(con);

        String sql = " SELECT que_id, que_answered_timestamp FROM knowQuestion WHERE que_id = ? ";

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = con.prepareStatement(sql);
            int index = 1;
            stmt.setInt(index++, queId);

            rs = stmt.executeQuery();
            if (rs.next()) {
            	Timestamp  date = cwUtils.dateAdd(rs.getTimestamp("que_answered_timestamp"),overdueDay);
                int diff = curTime.compareTo(date);
                if(diff < 0) {
                	isOverdue = false;
                }
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }

        return isOverdue;
    }

    private static final String IS_QUE_CREATOR = " SELECT que_status, que_type FROM knowQuestion WHERE que_id = ? AND que_create_ent_id = ? ";

    /**
     * check whether user is the creator of question.
     * 
     * @param con
     * @param queDb
     * @return
     * @throws SQLException
     */
    public boolean isQueCreator(Connection con, KnowQuestionDB queDb)
            throws SQLException {
        boolean isQueCreator = false;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = con.prepareStatement(IS_QUE_CREATOR);
            int index = 1;
            stmt.setInt(index++, queDb.getQue_id());
            stmt.setInt(index++, queDb.getQue_create_ent_id());
            rs = stmt.executeQuery();
            if (rs.next()) {
                isQueCreator = true;
                queDb.setQue_status(rs.getString("que_status"));
                queDb.setQue_type(rs.getString("que_type"));
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }

        return isQueCreator;
    }

    private static final String QUE_HAS_UPDATED = "SELECT que_id FROM knowQuestion WHERE que_id = ? AND que_update_timestamp = ?";

    /**
     * check whether the question has been updated.
     * 
     * @param con
     * @param queId
     * @param updateDate
     * @return
     * @throws SQLException
     */
    public static boolean hasUpdated(Connection con, int queId,
            Timestamp updateDate) throws SQLException {
        boolean hasUpdated = true;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = con.prepareStatement(QUE_HAS_UPDATED);
            int index = 1;
            stmt.setInt(index++, queId);
            stmt.setTimestamp(index++, updateDate);
            rs = stmt.executeQuery();
            if (rs.next()) {
                hasUpdated = false;
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }
        return hasUpdated;
    }
    
    private static final String QUE_HAS_UPDATED_KCR = "select kcr_child_kca_id,kcr_create_timestamp from knowCatalogRelation " +
    		"	where kcr_child_kca_id  = ? and kcr_parent_ind = ? and kcr_create_timestamp = ? and kcr_type =?";
    public static boolean hasKcrUpdated(Connection con, int queId, Timestamp updateDate) throws SQLException {
		boolean hasUpdated = true;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		stmt = con.prepareStatement(QUE_HAS_UPDATED_KCR);
		int index = 1;
		stmt.setInt(index++, queId);
		stmt.setInt(index++, 1);
		stmt.setTimestamp(index++, updateDate);
		stmt.setString(index++, KnowCatalogRelationDAO.QUE_PARENT_KCA);
		rs = stmt.executeQuery();
		if (rs.next()) {
			hasUpdated = false;
		}
		rs.close();
		stmt.close();
		return hasUpdated;
	}

    /**
	 * 改变题目的所在目录
	 */
	private final static String UPD_QUE_KCA_ID = " update knowQuestion set que_update_ent_id = ?, que_update_timestamp = ?  where que_id = ? ";
	private final static String DEL_QUE_RAL = " delete from knowCatalogRelation where kcr_child_kca_id = ? and kcr_type = ? ";
	public void change(Connection con, int que_id, int kca_id, long usr_ent_id) throws SQLException {
		PreparedStatement stmt = null;

		int old_kca_id = KnowCatalogRelationDAO.getQueParentCatId(con, que_id);

		stmt = con.prepareStatement(UPD_QUE_KCA_ID); // 更新问题所在目录ID
		int index = 1;
		stmt.setLong(index++, usr_ent_id);
		stmt.setTimestamp(index++, cwSQL.getTime(con));
		stmt.setInt(index++, que_id);
		stmt.executeUpdate();
		stmt.close();

		ViewKnowCatalogRelationDAO.updCatQueCount(con, old_kca_id, -1); // 更新旧目录问题数量
		ViewKnowCatalogRelationDAO.updCatQueCount(con, kca_id, 1); // 更新新目录的问题数量

		stmt = con.prepareStatement(DEL_QUE_RAL); // 删除旧关联信息
		stmt.setInt(1, que_id);
		stmt.setString(2, KnowCatalogRelationDAO.QUE_PARENT_KCA);
		stmt.executeUpdate();
		stmt.close();
	}
	
	public static List<Long> getQueIdListByUsrEntId(Connection con, long usr_ent_id) throws SQLException {
		String sql = " select que_id from knowQuestion where que_create_ent_id = ? ";
    	PreparedStatement stmt = null;
    	ResultSet rs = null;
    	List<Long> que_id_list = new ArrayList<Long>();
    	try{
    		stmt = con.prepareStatement(sql);
    		stmt.setLong(1, usr_ent_id);
    		rs = stmt.executeQuery();
    		while(rs.next()){
    			que_id_list.add(rs.getLong("que_id"));
    		}
    	} finally {
    		if(stmt != null){
    			stmt.close();
    		}
    		if(rs != null){
    			rs.close();
    		}
    	}
    	return que_id_list;
	}
}
