package com.cw.wizbank.db.view;

import java.util.Vector;
import java.util.Hashtable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.qdb.dbResource;
import com.cw.wizbank.qdb.dbQuestion;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.qdb.qdbException;

import com.cw.wizbank.util.*;
import com.cw.wizbank.db.DbQueContainerSpec;
import com.cw.wizbank.db.sql.*;

public class ViewQueContainer extends dbResource {
    //public long res_id = 0;   

    public String qct_select_logic;
    public int qct_allow_shuffle_ind = 0;

    public static final int orderByOrderNTitle = 0;
    public static final int orderByScoreNTitle = 1;
    private static final String sqlOrderByOrderNTitle =
        " ORDER BY rcn_order ASC, res_title ASC ";
    private static final String sqlOrderByScoreNTitle =
        " ORDER BY que_score ASC, res_title ASC ";

    public ViewQueContainer() {}

    public Vector getSpecifiedQuestionsInOrder(Connection con, Vector queId)
        throws SQLException, qdbException {
        if (queId == null || queId.isEmpty()) {
            return new Vector();
        }
        StringBuffer sql =
            new StringBuffer(
                SqlStatements.sql_get_container_specified_questions);
        sql.append(cwUtils.vector2list(queId));
        PreparedStatement stmt = con.prepareStatement(sql.toString());
        ResultSet rs = stmt.executeQuery();
        Hashtable h_que = new Hashtable();
        while (rs.next()) {
            dbQuestion que = new dbQuestion();
            que.que_res_id = rs.getLong("que_res_id");
            que.que_xml = cwSQL.getClobValue(rs, "que_xml");
            que.que_score = rs.getInt("que_score");
            que.que_type = rs.getString("que_type");
            que.que_int_count = rs.getInt("que_int_count");
            que.que_prog_lang = rs.getString("que_prog_lang");
            que.que_media_ind = rs.getBoolean("que_media_ind");
            que.res_lan = rs.getString("res_lan");
            que.res_title = rs.getString("res_title");
            que.res_desc = cwSQL.getClobValue(rs, "res_desc");
            que.res_type = rs.getString("res_type");
            que.res_subtype = rs.getString("res_subtype");
            que.res_annotation = rs.getString("res_annotation");
            que.res_format = rs.getString("res_format");
            que.res_difficulty = rs.getInt("res_difficulty");
            que.res_duration = rs.getFloat("res_duration");
            que.res_privilege = rs.getString("res_privilege");
            que.res_status = rs.getString("res_status");
            que.res_tpl_name = rs.getString("res_tpl_name");
            que.res_res_id_root = rs.getLong("res_res_id_root");
            que.res_upd_user = rs.getString("res_upd_user");
            que.res_upd_date = rs.getTimestamp("res_upd_date");
            que.res_src_type = rs.getString("res_src_type");
            que.res_src_link = rs.getString("res_src_link");
            que.res_usr_id_owner = rs.getString("res_usr_id_owner");
            que.res_mod_res_id_test = rs.getLong("res_mod_res_id_test");
            que.res_instructor_name = rs.getString("res_instructor_name");
            que.res_instructor_organization =
                rs.getString("res_instructor_organization");
            h_que.put(new Long(que.res_id), que);
        }
        stmt.close();
        Vector v_ordered_que = new Vector();
        for (int i = 0; i < queId.size(); i++) {
            dbQuestion que = (dbQuestion)h_que.get((Long)queId.elementAt(i));
            if (que != null) {
                v_ordered_que.addElement(que);
            } else {
                throw new qdbException(
                    "Failed to get the question, que_id = "
                        + queId.elementAt(i));
            }
        }
        return v_ordered_que;
    }

    public Vector getQuestions(Connection con, int orderByOption)
        throws qdbException {

        Vector v = new Vector();
        PreparedStatement stmt = null;
        try {
            try {
                StringBuffer SQLBuf =
                    new StringBuffer(SqlStatements.sql_get_container_questions);
                if (orderByOption == orderByOrderNTitle) {
                    SQLBuf.append(sqlOrderByOrderNTitle);
                } else if (orderByOption == orderByScoreNTitle) {
                    SQLBuf.append(sqlOrderByScoreNTitle);
                }
                stmt = con.prepareStatement(SQLBuf.toString());
                stmt.setLong(1, this.res_id);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    dbQuestion que = new dbQuestion();
                    que.que_res_id = rs.getLong("que_res_id");
                    que.res_id = que.que_res_id;
                    que.que_xml = cwSQL.getClobValue(rs, "que_xml");
                    que.que_score = rs.getInt("que_score");
                    que.que_type = rs.getString("que_type");
                    que.que_int_count = rs.getInt("que_int_count");
                    que.que_prog_lang = rs.getString("que_prog_lang");
                    que.que_media_ind = rs.getBoolean("que_media_ind");
                    que.res_lan = rs.getString("res_lan");
                    que.res_title = rs.getString("res_title");
                    que.res_desc = rs.getString("res_desc");
                    que.res_type = rs.getString("res_type");
                    que.res_subtype = rs.getString("res_subtype");
                    que.res_annotation = rs.getString("res_annotation");
                    que.res_format = rs.getString("res_format");
                    que.res_difficulty = rs.getInt("res_difficulty");
                    que.res_duration = rs.getFloat("res_duration");
                    que.res_privilege = rs.getString("res_privilege");
                    que.res_status = rs.getString("res_status");
                    que.res_tpl_name = rs.getString("res_tpl_name");
                    que.res_res_id_root = rs.getLong("res_res_id_root");
                    que.res_upd_user = rs.getString("res_upd_user");
                    que.res_upd_date = rs.getTimestamp("res_upd_date");
                    que.res_src_type = rs.getString("res_src_type");
                    que.res_src_link = rs.getString("res_src_link");
                    que.res_usr_id_owner = rs.getString("res_usr_id_owner");
                    que.res_mod_res_id_test = rs.getLong("res_mod_res_id_test");
                    que.res_instructor_name =
                        rs.getString("res_instructor_name");
                    que.res_instructor_organization =
                        rs.getString("res_instructor_organization");

                    // check whether the question has been attempted or not
                    PreparedStatement stmt2 =
                        con.prepareStatement(
                            " SELECT count(*) "
                                + "   FROM ProgressAttempt "
                                + "  WHERE atm_int_res_id = ? ");

                    stmt2.setLong(1, que.que_res_id);
                    ResultSet rs2 = stmt2.executeQuery();
                    boolean bNoChildOk = false;
                    if (rs2.next()) {
                        if (rs2.getInt(1) == 0) {
                            bNoChildOk = true;
                        }
                    }
                    stmt2.close();

                    if (!bNoChildOk) {
                        que.que_attempted = true;
                    } else {
                        que.que_attempted = false;
                    }

                    v.addElement(que);
                }
            } finally {
                if (stmt != null)
                    stmt.close();
            }
        } catch (SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
        return v;
    }

    public void ins(Connection con) throws qdbException {
        try {
            String SQL =
                "Insert into QueContainer (qct_res_id, qct_select_logic, qct_allow_shuffle_ind) Values (?,?,?)";
            PreparedStatement stmt = con.prepareStatement(SQL);

            stmt.setLong(1, res_id);
            stmt.setString(2, qct_select_logic);
            stmt.setLong(3, qct_allow_shuffle_ind);
            if (stmt.executeUpdate() != 1) {
            	stmt.close();
                con.rollback();
                throw new qdbException("Failed to insert Question Container.");
            }

            stmt.close();
        } catch (SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    public void upd(Connection con) throws qdbException {
        try {
            String SQL =
                "Update QueContainer Set qct_select_logic=?, qct_allow_shuffle_ind=? Where qct_res_id=?";
            PreparedStatement stmt = con.prepareStatement(SQL);

            stmt.setString(1, qct_select_logic);
            stmt.setLong(2, qct_allow_shuffle_ind);
            stmt.setLong(3, res_id);
            if (stmt.executeUpdate() != 1) {
            	stmt.close();
                con.rollback();
                throw new qdbException("Failed to edit Question Container.");
            }

            stmt.close();
        } catch (SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    public void del(Connection con) throws qdbException, cwSysMessage {
        try {
            String SQL = "Delete from QueContainer Where qct_res_id=?";
            PreparedStatement stmt = con.prepareStatement(SQL);

            stmt.setLong(1, res_id);
            if (stmt.executeUpdate() != 1) {
                con.rollback();
                throw new qdbException("Failed to delete Question Container.");
            }

            stmt.close();
        } catch (SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    public boolean checkExist(Connection con) throws qdbException {
        try {
            boolean bExist = false;
            String SQL =
                "Select count(*) cnt from QueContainer Where qct_res_id=?";
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, res_id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                if (rs.getInt("cnt") > 0) {
                    bExist = true;
                }
            }
            stmt.close();
            return bExist;
        } catch (SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    public void save(Connection con) throws qdbException {
        if (checkExist(con)) {
            upd(con);
        } else {
            ins(con);
        }
    }

    public void get(Connection con) throws qdbException {
        try {
            String SQL =
                "Select qct_allow_shuffle_ind, qct_select_logic from QueContainer Where qct_res_id=?";
            PreparedStatement stmt = con.prepareStatement(SQL);

            stmt.setLong(1, res_id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                qct_allow_shuffle_ind = rs.getInt("qct_allow_shuffle_ind");
                qct_select_logic = rs.getString("qct_select_logic");
            }

            stmt.close();
        } catch (SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    // update score for Scenario Question
    public void updScore(Connection con, String res_subtype)
        throws qdbException, cwSysMessage {
        try {
            String SQL = "";

            if (res_subtype.equalsIgnoreCase(dbResource.RES_SUBTYPE_DSC)) {
                SQL = SqlStatements.sql_get_dynamic_que_container_score;
            } else if (
                res_subtype.equalsIgnoreCase(dbResource.RES_SUBTYPE_FSC)) {
                SQL = SqlStatements.sql_get_fixed_que_container_score;
            } else {
                return;
            }

            PreparedStatement stmt = con.prepareStatement(SQL);

            stmt.setLong(1, res_id);
            ResultSet rs = stmt.executeQuery();
            long maxScore = 0;
            while (rs.next()) {
                if (res_subtype.equalsIgnoreCase(dbResource.RES_SUBTYPE_DAS)
                    || res_subtype.equalsIgnoreCase(dbResource.RES_SUBTYPE_DSC)) {
                    maxScore += rs.getLong("qcs_score")
                        * rs.getLong("qcs_qcount");
                } else {
                    maxScore += rs.getLong("rcn_score_multiplier")
                        * rs.getInt("que_score");
                }
            }

            PreparedStatement stmt1 =
                con.prepareStatement(
                    "UPDATE Question SET "
                        + " que_score = ? "
                        + " where que_res_id = ?");

            stmt1.setLong(1, maxScore);
            stmt1.setLong(2, res_id);

            if (stmt1.executeUpdate() != 1) {
                con.rollback();
                throw new qdbException("Failed to modify the max score.");
            }

            stmt.close();
            stmt1.close();
        } catch (SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    // insert objective for Fixed Assessment
    public void insObj(Connection con, long objId, String res_upd_user)
        throws qdbException, qdbErrMessage, cwSysMessage {
        DbQueContainerSpec myDbQueContainerSpec = new DbQueContainerSpec();
        myDbQueContainerSpec.qcs_obj_id = objId;
        myDbQueContainerSpec.qcs_res_id = res_id;

        myDbQueContainerSpec.get_frm_res_obj_id(con);
        if (myDbQueContainerSpec.qcs_id == 0) {
            myDbQueContainerSpec.ins(
                con,
                dbResource.RES_SUBTYPE_FAS,
                res_upd_user);
        }

        /*
        String aobjs = ""; // (1, 2, 34)
            
        aobjs = " (" + objId + ") ";
            
        if (aobjs == "") {
            throw new qdbException( "No resource objective selected. ");
        }
        
        String SQL =                     
                " INSERT INTO ResourceObjective "
            +   " SELECT " + res_id + ", obj_id "
            +   "  FROM Objective "
            +   " WHERE obj_id IN " + aobjs
            +   "   AND obj_id NOT IN (SELECT rob_obj_id from ResourceObjective where "
            +   "                             rob_res_id = " + res_id + ") ";
        
            
        try {
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.executeUpdate();
            super.updateTimeStamp(con);
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }
        */
    }

    /**
    Update the shuffle indicator of the QueContainer
    Pre-defined class variable: res_id
                                qct_allow_shuffle_ind
    */
    public void updateShuffleInd(Connection con) throws SQLException {

        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(SqlStatements.sql_upd_qct_shuffle_ind);
            stmt.setInt(1, this.qct_allow_shuffle_ind);
            stmt.setLong(2, this.res_id);
            stmt.execute();
        } finally {
            if (stmt != null)
                stmt.close();
        }
        return;
    }

}