package com.cw.wizbank.db.view;

import java.util.*;
import java.sql.*;

import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.qdb.dbResource;
import com.cw.wizbank.quebank.quecontainer.DynamicScenarioQue;
import com.cw.wizbank.quebank.quecontainer.FixedScenarioQue;
import com.cw.wizbank.db.sql.SqlStatements;
public class ViewResources {

    /**
     * Get Quiz question and objective
     */
    public static ResultSet getQuiz(Connection con, long mod_id, long startPtr, long endPtr)
        throws SQLException {
            String SQL = " SELECT res_id, res_title, res_usr_id_owner, usr_display_bil, res_status, res_lan, "
                       + " res_upd_date, que_xml, que_score, int_xml_outcome, int_xml_explain, "
                       + " qse_obj_id, obj_desc, res_difficulty "
                       + " FROM Resources, Objective, QSequence, Question, Interaction, RegUser "
                       + " WHERE res_id = que_res_id AND res_id = int_res_id "
                       + " AND res_usr_id_owner = usr_id AND int_order = ? "
                       + " AND res_id = qse_que_res_id AND qse_obj_id = obj_id "
                       + " AND qse_mod_id = ? "
                       + " AND qse_order BETWEEN ? AND ? ";


            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, 1);
            stmt.setLong(2, mod_id);
            stmt.setLong(3, startPtr);
            stmt.setLong(4, endPtr);
            ResultSet rs = stmt.executeQuery();
            return rs;
        }

    public Vector getDynamicScenarioChildQueId(
        Connection con,
        long rcn_res_id,
        long que_score,
        Vector v_cur_que_id)
        throws SQLException {

        Vector v_que_id = new Vector();
        String SQL = SqlStatements.sql_get_dynamic_scenario_child_que_id;
        if (v_cur_que_id != null && !v_cur_que_id.isEmpty()) {
            SQL += " And res_id Not In " + cwUtils.vector2list(v_cur_que_id);
        }
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, rcn_res_id);
        stmt.setString(2, dbResource.RES_STATUS_ON);
        stmt.setLong(3, que_score);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            v_que_id.addElement(new Long(rs.getLong("res_id")));
        }
        stmt.close();
        return v_que_id;
    }

    public Vector getChildQueId(Connection con, long rcn_res_id)
        throws SQLException {

        Vector v_que_id = new Vector();
        PreparedStatement stmt =
            con.prepareStatement(SqlStatements.sql_get_child_que_id);
        stmt.setLong(1, rcn_res_id);
        stmt.setString(2, dbResource.RES_STATUS_ON);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            v_que_id.addElement(new Long(rs.getLong("res_id")));
        }
        stmt.close();
        return v_que_id;
    }

    public Vector getDynamicAssChildQueId(Connection con, String usr_id, String objLst, long qcs_res_id, String qcs_type, long qcs_score, long qcs_difficulty, String qcs_privilege, float qcs_duration) throws SQLException {

        Vector v_que_id = new Vector();
        StringBuffer sql = new StringBuffer(512);
        sql
            .append(" SELECT res_id, res_subtype FROM Question, Resources, ResourceObjective ")
            .append(" WHERE que_res_id = res_id ")
            .append(" and rob_res_id = res_id ")
            .append(" and res_res_id_root is null ")
            .append(" and res_mod_res_id_test is null ")
            .append(" and res_status = ? ");

        if (qcs_duration > 0) {
            sql.append(" and res_duration = ? ");
        }

        if (qcs_score > 0) {
            sql.append(" and que_score = ? ");
        }

        if (qcs_privilege != null && qcs_privilege.length() > 0) {
            if (qcs_privilege.equalsIgnoreCase(dbResource.RES_PRIV_AUTHOR)) {
                sql.append(" and res_privilege = ? and res_usr_id_owner = ? ");
            } else if (
                qcs_privilege.equalsIgnoreCase(dbResource.RES_PRIV_CW)) {
                sql.append(" and res_privilege = ? ");
            }
        }

        if (qcs_type != null && qcs_type.length() > 0) {
            sql.append(" and res_subtype = ? ");
        }

        if (qcs_difficulty >= 1 && qcs_difficulty <= 3) {
            sql.append(" and res_difficulty = ? ");
        }

        if (objLst != null && objLst.length() > 0) {
            sql.append(" and rob_obj_id IN ").append(objLst);
        }
        PreparedStatement stmt = con.prepareStatement(sql.toString());
        int index = 1;
        stmt.setString(index++, "ON");
        if (qcs_duration > 0) {
            stmt.setFloat(index++, qcs_duration);
        }
        if (qcs_score > 0) {
            stmt.setLong(index++, qcs_score);
        }
        if (qcs_privilege != null && qcs_privilege.length() > 0) {
            if (qcs_privilege.equalsIgnoreCase(dbResource.RES_PRIV_AUTHOR)) {
                stmt.setString(index++, dbResource.RES_PRIV_AUTHOR);
                stmt.setString(index++, usr_id);
            } else if (
                qcs_privilege.equalsIgnoreCase(dbResource.RES_PRIV_CW)) {
                stmt.setString(index++, dbResource.RES_PRIV_CW);
            }
        }
        if (qcs_type != null && qcs_type.length() > 0) {
            stmt.setString(index++, qcs_type);
        }
        if (qcs_difficulty >= 1 && qcs_difficulty <= 3) {
            stmt.setLong(index++, qcs_difficulty);
        }

        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            //check num of sub-que if question is a dsc question
            String subtype = rs.getString("res_subtype");
            long que_id = rs.getLong("res_id");
//            if (subtype.equals(dbResource.RES_SUBTYPE_FSC)) {
//                FixedScenarioQue fsq = new FixedScenarioQue();
//                fsq.res_id = que_id;
//                v_que_id = fsq.getChildQueId(con);
//                if (v_que_id.size() > 0) {
//                    v_que_id.addElement(new Long(que_id));
//                }
//            }
//            else if (subtype.equals(dbResource.RES_SUBTYPE_DSC)) {
//                DynamicScenarioQue dsq = new DynamicScenarioQue();
//                dsq.res_id = que_id;
//                try {
//                    v_que_id = dsq.getChildQueId(con);
//                }
//                catch (cwSysMessage e) {
//                    //do nothing,just ignore this id
//                }
//                if (v_que_id.size() > 0) {
//                    v_que_id.addElement(new Long(que_id));
//                }
//            }
//            else {
                v_que_id.addElement(new Long(que_id));
//            }
        }
        stmt.close();
        return v_que_id;
    }
    

    public Vector getChildQueIdforDynRestore(Connection con, long rcn_res_id, long rcn_tkh_id)
    throws SQLException {
    	String sql_get_child_que_id =
            "Select res_id "
                + "From ResourceContent, Resources "
                + "Where rcn_res_id = ? "
                + "And rcn_res_id_content = res_id "
                + "And res_status = ? " 
                + "And rcn_tkh_id = ? "
                + "ORDER BY rcn_order ASC, res_title ASC"; 
    Vector v_que_id = new Vector();
    PreparedStatement stmt = con.prepareStatement(sql_get_child_que_id);
    stmt.setLong(1, rcn_res_id);
    stmt.setString(2, dbResource.RES_STATUS_ON);
    stmt.setLong(3, rcn_tkh_id);
    ResultSet rs = stmt.executeQuery();
    while (rs.next()) {
        v_que_id.addElement(new Long(rs.getLong("res_id")));
    }
    stmt.close();
    return v_que_id;
}    
}