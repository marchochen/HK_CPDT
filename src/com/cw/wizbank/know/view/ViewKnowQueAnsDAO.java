package com.cw.wizbank.know.view;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Vector;

import com.cw.wizbank.JsonMod.know.bean.KnowQuestionBean;
import com.cw.wizbank.know.dao.KnowAnswerDAO;
import com.cw.wizbank.know.dao.KnowQuestionDAO;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwUtils;

/**
 * @author DeanChen
 * 
 */
public class ViewKnowQueAnsDAO {

    // for pagination
    public int page_start_num = 0;

    public int page_end_num = 10;

    // for sort
    public String sort_col;

    public String sort_order;

    private Vector sort_col_vec = null;

    public ViewKnowQueAnsDAO(int pageStartNum, int pageSize) {
        if (pageSize == 0) {
            pageSize = 10;
        }
        page_start_num = pageStartNum + 1;
        page_end_num = pageStartNum + pageSize;
    }

    private static final String GET_QUE_BY_QUE_CREATOR = " SELECT que_id, que_title, que_type, que_create_timestamp, ans.ans_count AS ans_count "
            + " FROM knowQuestion "
            + " LEFT JOIN ("
            + "    SELECT ans_que_id ,COUNT(ans_id) AS ans_count "
            + "    FROM knowAnswer GROUP BY ans_que_id "
            + " ) ans ON (que_id = ans.ans_que_id) "
            + " WHERE que_create_ent_id = ? AND que_status = ? ";

    private static final String GET_QUE_BY_ANS_CREATOR = " SELECT que_id, que_title, que_type, que_create_timestamp, ans.ans_count AS ans_count "
            + " FROM knowQuestion "
            + " INNER JOIN ("
            + "     SELECT ans_que_id, COUNT(ans_que_id) AS ans_count  "
            + "     FROM knowAnswer "
            + "     WHERE ans_create_ent_id = ? "
            + "     GROUP BY ans_que_id "
            + "     HAVING COUNT(ans_que_id) > 0 "
            + " ) ans ON (que_id = ans.ans_que_id) AND que_status = ? ";

    /**
     * to get the questions by creator.
     * 
     * @param con
     * @param usr_ent_id
     * @param queVec
     * @return the count of record
     * @throws SQLException
     * @throws cwException
     */
    public int getQueLstByCreator(Connection con, long usr_ent_id,
            Vector queVec, boolean isGetByQueCreator) throws SQLException,
            cwException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int recTotal = 0;
        try {
            // filter sort column and sort order
            String[] sortColArr = { "que_title", "ans_count", "que_type", "que_create_timestamp" };
            sort_col_vec = cwUtils.String2vector(sortColArr, false);
            String defaultSort = "que_create_timestamp";
            String defaultDir = "DESC";
            if(this.sort_col == null) {
                this.sort_col = defaultSort;
            }
            if(this.sort_order == null) {
                this.sort_order = defaultDir;
            }
            String sortSQL = dbUtils.getSortSQL(sort_col_vec, sort_col, sort_order,
                    defaultSort, defaultDir);

            // decide to get questions by creator of question or creator of
            // answer
            String getQueLstSQL = null;
            if (isGetByQueCreator) {
                getQueLstSQL = GET_QUE_BY_QUE_CREATOR;
            } else {
                getQueLstSQL = GET_QUE_BY_ANS_CREATOR;
            }

            stmt = con.prepareStatement(getQueLstSQL + sortSQL);
            int index = 1;
            stmt.setLong(index++, usr_ent_id);
            stmt.setString(index++, KnowQuestionDAO.QUE_STATUS_OK);

            // set bean for json
            rs = stmt.executeQuery();
            while (rs.next()) {
                recTotal++;
                if (recTotal >= page_start_num && recTotal <= page_end_num) {
                    KnowQuestionBean knowQueBean = new KnowQuestionBean();
                    if (queVec != null) {
                        knowQueBean.setLst_order(queVec.size() + 1);
                    } else {
                        knowQueBean.setLst_order(1);
                    }
                    knowQueBean.setQue_id(rs.getLong("que_id"));
                    knowQueBean.setQue_title(rs.getString("que_title"));
                    knowQueBean.setQue_create_timestamp(rs.getTimestamp("que_create_timestamp"));
                    knowQueBean.setQue_type(rs.getString("que_type"));
                    knowQueBean.setAns_count(rs.getInt("ans_count"));
                    queVec.add(knowQueBean);
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
        return recTotal;
    }
    
    private static String getAnsIdsSql(String conditionSql) {
    	String sql = " SELECT ans_id, que_answered_timestamp FROM knowQuestion,knowAnswer" +
    			" WHERE ans_que_id = que_id and ans_right_ind = ? and que_type = ? and que_status = ? and ans_status = ?";
    	sql += " " + conditionSql;
    	return sql;
    }

    /**
     * to get invail temp vote.
     * 
     * @param con
     * @param invailDay
     * @return
     * @throws SQLException
     */
    public static Vector getQueIdsWithOverdueVote(Connection con, int voteOverdueDay)throws SQLException {
    	Vector queIdVec = null;
    	Calendar curCalendar = Calendar.getInstance();
    	curCalendar.add(Calendar.DAY_OF_YEAR, -voteOverdueDay);
    	Timestamp comparedTime = new Timestamp(curCalendar.getTime().getTime());

    	String conditionSql = "and que_answered_timestamp < ?";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(getAnsIdsSql(conditionSql));
            int index = 1;
            stmt.setBoolean(index++, true);
            stmt.setString(index++, KnowQuestionDAO.QUE_TYPE_SOLVED);
            stmt.setString(index++, KnowQuestionDAO.QUE_STATUS_OK);
            stmt.setString(index++, KnowAnswerDAO.ANS_STATUS_OK);
            stmt.setTimestamp(index++, comparedTime);
            
            rs = stmt.executeQuery();
            while (rs.next()) {
            	if(queIdVec == null) {
            		queIdVec = new Vector();
            	}
                Long ansId = new Long(rs.getString("ans_id"));
                queIdVec.addElement(ansId);
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }
        return queIdVec;
    }
    
    public static Vector getQueIdsAtMarkPeriodEnd(Connection con, Timestamp curStartTime, Timestamp nextStartTime, int queVotePeriod,
			int queMarkPopularPeriod) throws SQLException {
    	Vector queIdVec = null;
    	
    	//condition sql
    	String conditionSql = "";
    	int periodCount = 0;
    	if(queMarkPopularPeriod > 0) {
    		periodCount = queVotePeriod / queMarkPopularPeriod;
    	}
    	
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(getAnsIdsSql(conditionSql));
            int index = 1;
            stmt.setBoolean(index++, true);
            stmt.setString(index++, KnowQuestionDAO.QUE_TYPE_SOLVED);
            stmt.setString(index++, KnowQuestionDAO.QUE_STATUS_OK);
            stmt.setString(index++, KnowAnswerDAO.ANS_STATUS_OK);
            
            rs = stmt.executeQuery();
            boolean time = false;
            if(curStartTime != null && nextStartTime != null) {
            	time = true;
            }
            while (rs.next()) {
            	if(queIdVec == null) {
            		queIdVec = new Vector();
            	}
            	Long ansId;
            	if(time) {
        			for(int i = 1; i <= periodCount; i++) {
		    			int addDays = queMarkPopularPeriod * i;
		    			Timestamp t = cwUtils.dateAdd(rs.getTimestamp("que_answered_timestamp"), addDays);
		    			int after = t.compareTo(curStartTime);
		    			int befor = t.compareTo(nextStartTime);
		    			if(after >= 0 && befor < 0) {
		    				ansId = new Long(rs.getString("ans_id"));
		                    queIdVec.addElement(ansId);
		    			}
        			}
    	    	} if(queMarkPopularPeriod < 0) {
    	    		ansId = new Long(rs.getString("ans_id"));
                    queIdVec.addElement(ansId);
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
        return queIdVec;
    }

    private static final String GET_FUTURE_POPULAR_QUE_IDS = " SELECT ans_que_id FROM knowAnswer"
            + " INNER JOIN knowQuestion ON (ans_que_id = que_id AND que_status = ? ) "
            + " WHERE ans_right_ind = 1 and ans_status = ? AND  ans_temp_vote_for_down_diff >= ? ORDER BY ans_temp_vote_for_down_diff desc ";

    public static Vector getFuturePopularQueIds(Connection con,
            int popularQueNum, int availableVoteThreshold) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Vector queIdVec = new Vector();
        try {
            stmt = con.prepareStatement(GET_FUTURE_POPULAR_QUE_IDS);
            int index = 1;
            stmt.setString(index++, KnowQuestionDAO.QUE_STATUS_OK);
            stmt.setString(index++, KnowAnswerDAO.ANS_STATUS_OK);
            stmt.setInt(index++, availableVoteThreshold);
            rs = stmt.executeQuery();
            int queCount = 0;
            while (rs.next()) {
                queCount++;
                if (queCount > popularQueNum) {
                    break;
                }
                Long queId = new Long(rs.getString("ans_que_id"));
                queIdVec.addElement(queId);
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }
        return queIdVec;
    }

    private static final String IS_QUE_CREATOR = " SELECT que_id FROM knowQuestion "
            + " INNER JOIN knowAnswer ON (ans_que_id = que_id) WHERE ans_id = ? AND que_create_ent_id = ? ";

    /**
     * check whether user is the creator of question.
     * 
     * @param con
     * @param ansId
     * @param userEntId
     * @return
     * @throws SQLException
     */
    public static boolean isQueCreator(Connection con, int ansId, int userEntId)
            throws SQLException {
        boolean isQueCreator = false;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = con.prepareStatement(IS_QUE_CREATOR);
            int index = 1;
            stmt.setInt(index++, ansId);
            stmt.setInt(index++, userEntId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                isQueCreator = true;
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
    
    private static final String GET_QUE_COUNT_BY_RESPONDER = " select count(distinct que_id) as que_count from knowQuestion" +
    		" inner join knowAnswer on (ans_que_id = que_id and ans_status = ?)" +
    		" where que_status = ?  and ans_create_ent_id = ?";
    
    /**
     * get the count of questions that user has answered.  
     * @param con
     * @param userEntId
     * @return
     * @throws SQLException
     */
    public static int getQueCountByReponser(Connection con, int userEntId) throws SQLException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int queCount = 0;
		try {
			stmt = con.prepareStatement(GET_QUE_COUNT_BY_RESPONDER);
			int index = 1;
			stmt.setString(index++, KnowAnswerDAO.ANS_STATUS_OK);
			stmt.setString(index++, KnowQuestionDAO.QUE_STATUS_OK);
			stmt.setInt(index++, userEntId);
			rs = stmt.executeQuery();
			if (rs.next()) {
				queCount = rs.getInt("que_count");
			}
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}
		return queCount;
	}
}
