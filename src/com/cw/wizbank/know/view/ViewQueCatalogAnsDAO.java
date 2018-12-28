package com.cw.wizbank.know.view;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Vector;

import com.cw.wizbank.JsonMod.know.Know;
import com.cw.wizbank.JsonMod.know.KnowModuleParam;
import com.cw.wizbank.JsonMod.know.bean.KnowQuestionBean;
import com.cw.wizbank.JsonMod.know.bean.QueSearchCriteriaBean;
import com.cw.wizbank.know.dao.KnowAnswerDAO;
import com.cw.wizbank.know.dao.KnowCatalogRelationDAO;
import com.cw.wizbank.know.dao.KnowQuestionDAO;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwPagination;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwUtils;

/**
 * @author DeanChen
 * 
 */
public class ViewQueCatalogAnsDAO {

    // for pagination
    public int page_start_num = 0;

    public int page_end_num = 10;

    // for sort
    public String sort;

    public String dir;

    private Vector sort_vec = null;

    private Vector dir_vec = null;

    // search condition
    public QueSearchCriteriaBean srhCriteriaBean = null;

    public ViewQueCatalogAnsDAO() {
        ;
    }

    public ViewQueCatalogAnsDAO(KnowModuleParam modParam) {
        dir_vec = new Vector();
        dir_vec.addElement("ASC");
        dir_vec.addElement("DESC");
        int pageSize = modParam.getLimit();
        if (pageSize == 0) {
            pageSize = 10;
        }
        page_start_num = modParam.getStart() + 1;
        page_end_num = modParam.getStart() + pageSize;
        
        this.sort = modParam.getSort();
		this.dir = modParam.getDir();
    }

    private static final String GET_QUE = " SELECT kca_id,kca_title,que_id,que_title,que_create_timestamp, que_popular_ind, ans.ans_count "
            + " FROM knowCatalog "
            + " inner join knowCatalogRelation on (kcr_type = ? and kcr_ancestor_kca_id = kca_id and kcr_parent_ind = ?)"
            + " inner join knowQuestion on (que_id = kcr_child_kca_id)"
            + " inner join tcTrainingCenter on (tcr_id = kca_tcr_id and tcr_ste_ent_id = ?)"
            + " LEFT JOIN ("
            + "     SELECT ans_que_id ,count(ans_id) ans_count "
            + "     FROM knowAnswer WHERE ans_status = ? GROUP BY ans_que_id"
            + " ) ans ON (que_id = ans.ans_que_id) "
            + " WHERE kca_public_ind = 1 AND que_status = ? AND que_type = ? ";

    /**
     * to get the list of questions.
     * 
     * @param con
     * @param isSolved
     * @param catId
     * @param steEntId
     * @return the count of questions
     * @throws SQLException
     * @throws cwException
     */
    public int getQue(Connection con, String queType, Vector queVec, int catId, long steEntId)
            throws SQLException, cwException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int recTotal = 0;
        try {
            String conditionSQL = this.getCatIdConditionSQL(catId);
            String sortSQL = getQueSortSQL();
            stmt = con.prepareStatement(GET_QUE + conditionSQL + sortSQL);
            int index = 1;
            stmt.setString(index++, KnowCatalogRelationDAO.QUE_PARENT_KCA);
            stmt.setBoolean(index++, true);
            stmt.setLong(index++, steEntId);
            stmt.setString(index++, KnowAnswerDAO.ANS_STATUS_OK);
            stmt.setString(index++, KnowQuestionDAO.QUE_STATUS_OK);
            stmt.setString(index++, queType);
            if (catId != 0) {
                stmt.setString(index++, KnowCatalogRelationDAO.KCA_PARENT_KCA);
            }
            rs = stmt.executeQuery();
            while (rs.next()) {
                recTotal++;
                if (queVec == null) {
                    queVec = new Vector();
                }
                if (recTotal >= page_start_num && recTotal <= page_end_num) {
                    KnowQuestionBean knowQueBean = new KnowQuestionBean();
                    knowQueBean.setKca_id(rs.getInt("kca_id"));
                    knowQueBean.setKca_title(rs.getString("kca_title"));
                    knowQueBean.setQue_id(rs.getInt("que_id"));
                    knowQueBean.setQue_title(rs.getString("que_title"));
                    knowQueBean.setQue_create_timestamp(rs.getTimestamp("que_create_timestamp"));
                    knowQueBean.setQue_type(queType);
                    knowQueBean.setQue_popular_ind(rs.getBoolean("que_popular_ind"));
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

    private String getQueSortSQL() {
        // filter sort column and sort order
        String[] sortArr = {"kca_title", "que_title", "ans_count", "que_type", "que_create_timestamp", "ans_vote_for"};
        sort_vec = cwUtils.String2vector(sortArr, false);
        String defaultSortColumn = "que_create_timestamp";
        String defaultOrder = "desc";
        String sortSQL = dbUtils.getSortSQL(sort_vec, sort, dir, defaultSortColumn.toLowerCase(), defaultOrder);
        if (this.sort == null) {
            this.sort = defaultSortColumn;
        }
        if (this.dir == null) {
            this.dir = defaultOrder;
        }

        return sortSQL;
    }

    private static final String GET_POPULAR_QUE = " SELECT kca_id,kca_title,que_id,que_title,que_create_timestamp, ans.ans_count, right_ans.ans_vote_for "
            + " FROM knowCatalog"
            + " inner join knowCatalogRelation on (kcr_type = ? and kcr_ancestor_kca_id = kca_id and kcr_parent_ind = ?)"
            + " inner join knowQuestion on (que_id = kcr_child_kca_id)"
            + " inner join tcTrainingCenter on (tcr_id = kca_tcr_id and tcr_ste_ent_id = ?)"
            + " LEFT JOIN ("
            + "     SELECT ans_que_id ,count(ans_id) ans_count "
            + "     FROM knowAnswer WHERE ans_status = ? GROUP BY ans_que_id"
            + " ) ans ON (que_id = ans.ans_que_id) " 
            + " inner join knowAnswer right_ans on (right_ans.ans_que_id = que_id and right_ans.ans_right_ind = 1 and right_ans.ans_status = ?)"
            + " WHERE  kca_public_ind = 1 AND que_status = ? AND que_type = ? "
            + "     AND que_popular_ind = 1 ";

    /**
     * to get the list of questions.
     * 
     * @param con
     * @param catId
     * @param steEntId
     * @param isSolved
     * @return the count of questions
     * @throws SQLException
     * @throws cwException
     */
    public int getPopularQue(Connection con, Vector popularQueVec, int catId, long steEntId)
            throws SQLException, cwException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int recTotal = 0;
        try {
            String conditionSQL = this.getCatIdConditionSQL(catId);
            String sortSQL = getQueSortSQL();
            stmt = con.prepareStatement(GET_POPULAR_QUE + conditionSQL + sortSQL);
            int index = 1;
            stmt.setString(index++, KnowCatalogRelationDAO.QUE_PARENT_KCA);
            stmt.setBoolean(index++, true);
            stmt.setLong(index++, steEntId);
            stmt.setString(index++, KnowAnswerDAO.ANS_STATUS_OK);
            stmt.setString(index++, KnowAnswerDAO.ANS_STATUS_OK);
            stmt.setString(index++, KnowQuestionDAO.QUE_STATUS_OK);
            stmt.setString(index++, KnowQuestionDAO.QUE_TYPE_SOLVED);
            if (catId != 0) {
                stmt.setString(index++, KnowCatalogRelationDAO.KCA_PARENT_KCA);
            }
            rs = stmt.executeQuery();
            while (rs.next()) {
                recTotal++;
                if (recTotal >= page_start_num && recTotal <= page_end_num) {
                    KnowQuestionBean knowQueBean = new KnowQuestionBean();
                    knowQueBean.setKca_id(rs.getInt("kca_id"));
                    knowQueBean.setKca_title(rs.getString("kca_title"));
                    knowQueBean.setQue_id(rs.getInt("que_id"));
                    knowQueBean.setQue_title(rs.getString("que_title"));
                    knowQueBean.setQue_create_timestamp(rs.getTimestamp("que_create_timestamp"));
                    knowQueBean.setAns_count(rs.getInt("ans_count"));
                    knowQueBean.setQue_type(KnowQuestionDAO.QUE_TYPE_SOLVED);
                    knowQueBean.setQue_popular_ind(true);
                    popularQueVec.add(knowQueBean);
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

    private static final String SEARCH_QUE_ID = " select distinct que_id, que_create_timestamp from knowQuestion "
            + " left join knowAnswer on (ans_que_id = que_id and ans_status = ?) "
            + " inner join knowCatalogRelation on (kcr_type = ? and kcr_parent_ind = ? and kcr_child_kca_id = que_id)"
            + " inner join knowCatalog on (kcr_ancestor_kca_id = kca_id and kca_public_ind = ?)"
            + " inner join tcTrainingCenter on (tcr_id = kca_tcr_id and tcr_ste_ent_id = ?)"
            + " where que_status = ? ";

    private static final String GET_QUE_BY_ID_LST = " select kca_id,kca_title,que_id,que_title,que_content,que_create_ent_id,que_type,"
            + " que_create_timestamp,que_update_timestamp,ans_vote_total,ans_vote_for,ans_vote_down,usr_nickname,usr_display_bil "
            + " from knowQuestion"
            + " left join knowAnswer on (ans_que_id = que_id and ans_right_ind = ? and ans_status = ?) "
            + " inner join knowCatalogRelation on (kcr_type = ? and kcr_parent_ind = ? and kcr_child_kca_id = que_id)"
            + " inner join knowCatalog on (kca_id = kcr_ancestor_kca_id and kca_public_ind = ?)"
            + " inner join regUser on (usr_ent_id = que_create_ent_id and usr_status <> ?)"
            + " where que_status = ?  ";

    public int searchQue(Connection con, Vector queVec, long steEntId) throws SQLException {
    	return searchQue(con, queVec, steEntId, null);
    }

    public int searchQue(Connection con, Vector queVec, long steEntId, cwPagination cwPage) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int recTotal = 0;
        try {
            // search question id that agree search criterion.
            String sql = SEARCH_QUE_ID;
            sql += getSearchConditionSQL(con);

            stmt = con.prepareStatement(sql);
            int index = 1;
            stmt.setString(index++, KnowAnswerDAO.ANS_STATUS_OK);
            stmt.setString(index++, KnowCatalogRelationDAO.QUE_PARENT_KCA);
            stmt.setBoolean(index++, true);
            stmt.setBoolean(index++, true);
            stmt.setLong(index++, steEntId);
            stmt.setString(index++, KnowQuestionDAO.QUE_STATUS_OK);
            
            //condition parameters
            if (Know.SRH_KEY_TYPE_TITLE.equalsIgnoreCase(srhCriteriaBean.getSrh_key_type())) {
            	stmt.setString(index++, "%" + srhCriteriaBean.getSrh_key() + "%");
            } else {
            	stmt.setString(index++, "%" + srhCriteriaBean.getSrh_key() + "%");
            	stmt.setString(index++, "%" + srhCriteriaBean.getSrh_key() + "%");
            	stmt.setString(index++, "%" + srhCriteriaBean.getSrh_key() + "%");
            	stmt.setString(index++, "%" + srhCriteriaBean.getSrh_key() + "%");
            } 
            
            rs = stmt.executeQuery();
            Vector queIdVec = null;
//          srh_que_start_period
            String queStartPeriod = srhCriteriaBean.getSrh_que_start_period();
            int queStartPeriodDay = 0;
            Timestamp curTime = cwSQL.getTime(con);
            if(queStartPeriod != null) {
                for(int i = 0; i < Know.que_start_period_arr.length; i++) {
                    if(Know.que_start_period_arr[i].equalsIgnoreCase(queStartPeriod)) {
                        queStartPeriodDay = Know.que_start_period_day_arr[i];
                        break;
                    }
                }
            }
            while (rs.next()) {
                if(queIdVec == null) {
                    queIdVec = new Vector();
                }
                    
                if(queStartPeriodDay != 0) {
                	if(queStartPeriodDay > 0) {
                		Timestamp t = cwUtils.dateAdd(rs.getTimestamp("que_create_timestamp"), queStartPeriodDay);
                		int diff = t.compareTo(curTime);
                		if(diff >= 0) {
                			queIdVec.addElement(new Long(rs.getLong("que_id")));
                		}
                	}else{
                		Timestamp start;
                		Calendar cal_today = Calendar.getInstance();
                		cal_today.set(Calendar.HOUR_OF_DAY, 0);
                		cal_today.set(Calendar.MINUTE, 0);
                		cal_today.set(Calendar.SECOND, 0);
                		cal_today.set(Calendar.MILLISECOND, 0);
                		start = new Timestamp(cal_today.getTime().getTime());
                		int diff = rs.getTimestamp("que_create_timestamp").compareTo(start);
                		if(diff >= 0) {
                			queIdVec.addElement(new Long(rs.getLong("que_id")));
                		}
                	}
                }else{
                	queIdVec.addElement(new Long(rs.getLong("que_id")));
                }
            }

            // get question list by question id list
            // get search sql and set default sort of popular question
            String sortSQL = getSrhQueSortSQL();
            if(queIdVec != null && queIdVec.size() > 0) {
            	sql = GET_QUE_BY_ID_LST;
                String queIdLstStr = cwUtils.vector2list(queIdVec);
                sql += "and que_id in " + queIdLstStr;
                if (cwPage != null) {
            		if (cwPage.sortCol == null) {
            			cwPage.sortCol = "que_create_timestamp";
            		}
            		if (cwPage.sortOrder == null) {
            			cwPage.sortOrder = "desc";
            		}
            		sql += " order by " + cwPage.sortCol + " " + cwPage.sortOrder;
            	} else {
            		sql += sortSQL;
            	}
                
                stmt = con.prepareStatement(sql);
                index = 1;
                stmt.setBoolean(index++, true);
                stmt.setString(index++, KnowAnswerDAO.ANS_STATUS_OK);
                stmt.setString(index++, KnowCatalogRelationDAO.QUE_PARENT_KCA);
                stmt.setBoolean(index++, true);
                stmt.setBoolean(index++, true);
                stmt.setString(index++, dbRegUser.USR_STATUS_DELETED);
                stmt.setString(index++, KnowQuestionDAO.QUE_STATUS_OK);
                
                rs = stmt.executeQuery();
                String name = null;
                while (rs.next()) {
                    recTotal++;
                    if (cwPage != null) {
                        KnowQuestionBean knowQueBean = new KnowQuestionBean();
                        knowQueBean.setKca_id(rs.getInt("kca_id"));
                        knowQueBean.setKca_title(rs.getString("kca_title"));
                        knowQueBean.setQue_id(rs.getInt("que_id"));
                        knowQueBean.setQue_title(rs.getString("que_title"));
                        knowQueBean.setQue_create_timestamp(rs.getTimestamp("que_create_timestamp"));
                        knowQueBean.setQue_content(rs.getString("que_content"));
                        knowQueBean.setQue_create_ent_id(rs.getInt("que_create_ent_id"));
                        
                        name = rs.getString("usr_nickname");
                        if (name == null || name.length() == 0) {
                        	name = rs.getString("usr_display_bil");
                        }
                        knowQueBean.setUsr_nickname(name);
                        knowQueBean.setAns_vote_total(rs.getInt("ans_vote_total"));
                        knowQueBean.setAns_vote_for(rs.getInt("ans_vote_for"));
                        knowQueBean.setAns_vote_down(rs.getInt("ans_vote_down"));
                        knowQueBean.setQue_create_timestamp(rs.getTimestamp("que_create_timestamp"));
                        knowQueBean.setQue_update_timestamp(rs.getTimestamp("que_update_timestamp"));
                        knowQueBean.setQue_create_ent_id(rs.getInt("que_create_ent_id"));
                        knowQueBean.setQue_type(rs.getString("que_type"));
                        knowQueBean.setUsr_nickname(rs.getString("usr_nickname"));
                        int ansVoteTotal = rs.getInt("ans_vote_total");
                        knowQueBean.setAns_vote_total(ansVoteTotal);
                        float ansVoteFor = rs.getInt("ans_vote_for");
                        knowQueBean.setAns_vote_for((int)ansVoteFor);
                        int ansVoteDown = rs.getInt("ans_vote_down");
                        knowQueBean.setAns_vote_down(ansVoteDown);
                        
                        int ansVoteForRate = 0;
                        int ansVoteDownRate = 0;
                        if(ansVoteTotal > 0) {
                            ansVoteForRate = Math.round((ansVoteFor / ansVoteTotal) * 100);
                            ansVoteDownRate = 100 - ansVoteForRate;
                        }
                        knowQueBean.setAns_vote_for_rate(ansVoteForRate);
                        knowQueBean.setAns_vote_down_rate(ansVoteDownRate);                     
                        
                        queVec.add(knowQueBean);
                    } else {
                    	if (recTotal >= page_start_num && recTotal <= page_end_num) {
                            KnowQuestionBean knowQueBean = new KnowQuestionBean();
                            knowQueBean.setKca_id(rs.getInt("kca_id"));
                            knowQueBean.setKca_title(rs.getString("kca_title"));
                            knowQueBean.setQue_id(rs.getInt("que_id"));
                            knowQueBean.setQue_title(rs.getString("que_title"));
                            knowQueBean.setQue_create_timestamp(rs.getTimestamp("que_create_timestamp"));
                            knowQueBean.setQue_content(rs.getString("que_content"));
                            knowQueBean.setQue_create_ent_id(rs.getInt("que_create_ent_id"));
                            knowQueBean.setQue_type(rs.getString("que_type"));
                            
                            name = rs.getString("usr_nickname");
                            if (name == null || name.length() == 0) {
                            	name = rs.getString("usr_display_bil");
                            }
                            knowQueBean.setUsr_nickname(name);
                            int ansVoteTotal = rs.getInt("ans_vote_total");
                            knowQueBean.setAns_vote_total(ansVoteTotal);
                            float ansVoteFor = rs.getInt("ans_vote_for");
                            knowQueBean.setAns_vote_for((int)ansVoteFor);
                            int ansVoteDown = rs.getInt("ans_vote_down");
                            knowQueBean.setAns_vote_down(ansVoteDown);
                            
                            int ansVoteForRate = 0;
                            int ansVoteDownRate = 0;
                            if(ansVoteTotal > 0) {
                                ansVoteForRate = Math.round((ansVoteFor / ansVoteTotal) * 100);
                                ansVoteDownRate = 100 - ansVoteForRate;
                            }
                            knowQueBean.setAns_vote_for_rate(ansVoteForRate);
                            knowQueBean.setAns_vote_down_rate(ansVoteDownRate);
                            
                            queVec.add(knowQueBean);
                        }
                    }
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

    private String getSrhQueSortSQL() {
        // filter sort column and sort order
        String[] sortArr = { "que_title", "que_create_timestamp", "kca_title", "ans_vote_total" };
        sort_vec = cwUtils.String2vector(sortArr, false);
        String defaultSort = "que_create_timestamp";
        String defaultDir = "DESC";
        
        // popular defalut sort
        if(srhCriteriaBean != null && this.sort == null) {
            String queType = srhCriteriaBean.getSrh_tab_que_type();
            if (Know.TAB_QUE_POPULAR_LST.equalsIgnoreCase(queType)) {
                this.sort = "ans_vote_total";
            } else {
                this.sort = defaultSort;
            }
            this.dir = defaultDir;
        // normal deaflut sort
        } else if (this.sort == null) {
            this.sort = defaultSort;
        }
        
        String sortSQL = dbUtils.getSortSQL(sort_vec, sort, dir, defaultSort, defaultDir);

        return sortSQL;
    }

    private String getSearchConditionSQL(Connection con) throws SQLException {
        String srhConditionSQL = "";

        // srh_keyword
        String srhKeyType = srhCriteriaBean.getSrh_key_type();
        if (Know.SRH_KEY_TYPE_TITLE.equalsIgnoreCase(srhKeyType)) {
            srhConditionSQL += " AND lower(que_title) LIKE ? ";
        } else {
            srhConditionSQL += " AND (lower(que_title) LIKE ? ";
            srhConditionSQL += " OR lower(que_content) LIKE ? ";
            srhConditionSQL += " OR lower(ans_refer_content) LIKE ? ";
            srhConditionSQL += " OR ans_content_search LIKE ? )";
        } 

        // srh_que_type
        String queType = srhCriteriaBean.getSrh_tab_que_type();
        String queTypecondSQL = "";
        if (Know.TAB_QUE_UNANS_LST.equalsIgnoreCase(queType)) {
            queTypecondSQL += " and que_type = '" + KnowQuestionDAO.QUE_TYPE_UNSOLVED + "' ";
        } else if (Know.TAB_QUE_ANSED_LST.equalsIgnoreCase(queType)) {
            queTypecondSQL += " and que_type = '" + KnowQuestionDAO.QUE_TYPE_SOLVED + "' ";
        } else if (Know.TAB_QUE_POPULAR_LST.equalsIgnoreCase(queType)) {
            queTypecondSQL += " and que_type = '" + KnowQuestionDAO.QUE_TYPE_SOLVED + "' "
                + " and que_popular_ind = 1 ";
        } else if (Know.TAB_QUE_FAQ_LST.equalsIgnoreCase(queType)) {
            queTypecondSQL += " and que_type = '" + KnowQuestionDAO.QUE_TYPE_FAQ + "' ";
        }
        srhConditionSQL += queTypecondSQL;

        // srh_catalog_id
        long[] catalogId = srhCriteriaBean.getSrh_catalog_id();
   
        // check whether all catalogs has been selected.
        Vector catIdVec = new Vector();
        if (catalogId != null) {
	        for(int i = 0; i < catalogId.length; i++) {
	            if(catalogId[i] == 0) {
	                catIdVec = null;
	                break;
	            } else {
	                int catId = (int) catalogId[i];
	                catIdVec.addElement(new Long(catId)); 
	            }
	        }
        }
        if(catalogId != null && catalogId.length > 0) {
            catalogId = cwUtils.vec2longArray(catIdVec);
        } else {
            catalogId = null;
        }
        if(catalogId != null && catalogId.length > 0) {
            String catIdStr = cwUtils.array2list(catalogId);
            srhConditionSQL += getCatIdConditionSQL(catIdStr);
        }

        return srhConditionSQL;
    }
    
    private String getCatIdConditionSQL(int catId) {
        String catIdCondSQL = "";
        if (catId != 0) {
            catIdCondSQL += " AND ( kca_id IN ( "
                    + " SELECT kcr_child_kca_id FROM knowCatalogRelation"
                    + " WHERE kcr_type = ? AND kcr_ancestor_kca_id = " + catId
                    + " ) " + " OR kca_id = " + catId + " ) ";
        }
        return catIdCondSQL;
    }

    private String getCatIdConditionSQL(String catIdStr) {
        String catIdCondSQL = "";
        if (catIdStr != null) {
            catIdCondSQL += " AND ( kca_id IN ( "
                    + " SELECT kcr_child_kca_id FROM knowCatalogRelation"
                    + " WHERE kcr_type = '"
                    + KnowCatalogRelationDAO.KCA_PARENT_KCA
                    + "' AND kcr_ancestor_kca_id IN " + catIdStr + " ) "
                    + " OR kca_id IN " + catIdStr + " ) ";
        }
        return catIdCondSQL;
    }

}
