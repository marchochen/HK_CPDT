package com.cw.wizbank.db;

import java.sql.*;
import java.util.*;

import com.cw.wizbank.qdb.dbQuestion;
import com.cw.wizbank.util.cwSQL;

public class DbRawQuestion {
    public long raq_parent_id;
    public String raq_criteria;
    public long		raq_id;
    public long     raq_ulg_id;
    public int      raq_line_num;
    public String   raq_title;
    public String   raq_lan;
    public String   raq_type;
    public String   raq_status;
    public int      raq_difficulty;
    public int      raq_score;
    public float    raq_duration;
    public String   raq_privilege;
    public String   raq_usr_id_owner;
    public long     raq_obj_id;
    public String   raq_que_xml;
    public int      raq_int_cnt;
    public String   raq_outcome_xml_1;
    public String   raq_explain_xml_1;
	public boolean 		raq_shuffle;
	public boolean raq_submit_file_ind;
	//public String	raq_criteria;
	public String	raq_desc;
    public long resource_id;
    public void RawQuestion(){;}
    public String raq_sc_sub_shuffle = null;

//    private static String sql_ins_ = "INSERT INTO RawQuestion ( "
//        + " raq_ulg_id, raq_line_num, raq_title, "
//        + " raq_lan, raq_type, raq_status, "
//        + " raq_difficulty, raq_score, raq_duration, "
//        + " raq_privilege, raq_usr_id_owner, raq_obj_id, "
//        + " raq_que_xml, raq_int_cnt, raq_outcome_xml_1, raq_explain_xml_1 ) "
//        + " VALUES (?,?,?,?,?,?, "
//        + "         ?,?,?,?,?,?, "
//      // for oracle clob
//        + clobNull+",?"+clobNull+","+clobNull+") ";

    private static String sql_get_job_ = " SELECT "
        + " raq_line_num, raq_title, "
        + " raq_lan, raq_type, raq_status, raq_submit_file_ind, "
        + " raq_difficulty, raq_score, raq_duration, "
        + " raq_privilege, raq_usr_id_owner, raq_obj_id, "
        + " raq_que_xml, raq_int_cnt, raq_outcome_xml_1, raq_explain_xml_1, raq_desc, raq_shuffle, raq_res_id, raq_criteria "
        + " FROM RawQuestion WHERE "
        + " raq_ulg_id = ? ORDER BY raq_line_num ";

    private static String sql_del_ = "DELETE From RawQuestion  "
        + " WHERE raq_ulg_id = ? ";

    public int ins(Connection con) throws SQLException    {
        // for oracle clob
        // get clobNull
        String clobNull = cwSQL.getClobNull();

        // sql
        String sql_ins_ = "INSERT INTO RawQuestion ( "
        + " raq_ulg_id, raq_line_num, raq_title, "
        + " raq_lan, raq_type, raq_status, raq_submit_file_ind, "
        + " raq_difficulty, raq_score, raq_duration, "
        + " raq_privilege, raq_usr_id_owner, raq_obj_id, "
        + " raq_que_xml, raq_int_cnt, raq_outcome_xml_1, raq_explain_xml_1, raq_desc, raq_shuffle, raq_res_id, raq_criteria ) "
        + " VALUES (?,?,?,?,?,?,?, "
        + "         ?,?,?,?,?,?, "
        // for oracle clob
        + clobNull + ",? , " + clobNull + "," + clobNull + "," + clobNull + ", ? , ?, ?) ";

        PreparedStatement stmt = con.prepareStatement(sql_ins_);
        int index =0;
        stmt.setLong(++index, raq_ulg_id);
        stmt.setInt(++index, raq_line_num);
        stmt.setString(++index, raq_title);
        stmt.setString(++index, raq_lan);
        stmt.setString(++index, raq_type);
        stmt.setString(++index, raq_status);
		stmt.setBoolean(++index, raq_submit_file_ind);
        stmt.setInt(++index, raq_difficulty);
        stmt.setInt(++index, raq_score);
        stmt.setFloat(++index, raq_duration);
        stmt.setString(++index, raq_privilege);
        stmt.setString(++index, raq_usr_id_owner);
        stmt.setLong(++index, raq_obj_id);
        // for oracle clob
//        stmt.setString(++index, raq_que_xml);
        stmt.setInt(++index, raq_int_cnt);
//        stmt.setString(++index, raq_outcome_xml_1);
//        stmt.setString(++index, raq_explain_xml_1);
        if (raq_type.equals(dbQuestion.QUE_TYPE_MULTI) && raq_sc_sub_shuffle == null) {
            stmt.setBoolean(++index, raq_shuffle);
        } else {
            if (raq_sc_sub_shuffle != null && raq_sc_sub_shuffle.equals("Y")) {
                raq_shuffle = true;
            } else  {
                raq_shuffle = false;
            }
            stmt.setBoolean(++index, raq_shuffle);
        }
        stmt.setLong(++index, resource_id);
        stmt.setString(++index, raq_criteria);
        int cnt = stmt.executeUpdate();
        if (cnt>0) {
            // Update raq_que_xml,raq_outcome_xml_1,raq_explain_xml_1
            // for oracle clob
            String condition = "raq_ulg_id = " + raq_ulg_id +" and raq_line_num = " + raq_line_num;
            String tableName = "RawQuestion";
            String[] colName = {"raq_que_xml","raq_outcome_xml_1","raq_explain_xml_1","raq_desc"};
            String[] colValue = {raq_que_xml,raq_outcome_xml_1,raq_explain_xml_1,raq_desc};
            cwSQL.updateClobFields(con, tableName, colName, colValue, condition);
        }
        stmt.close();

        return cnt;
    }

    public static Vector getQuetionSet(Connection con, long ulg_id) throws SQLException {

        Vector raqVec = new Vector();
        PreparedStatement stmt = con.prepareStatement(sql_get_job_);
        stmt.setLong(1, ulg_id);
        ResultSet rs = stmt.executeQuery();
        DbRawQuestion raq = null;
        long parent_sc_raq_id = 0;
        while (rs.next()) {
            raq = new DbRawQuestion();
            raq.raq_ulg_id = ulg_id;
            raq.raq_line_num = rs.getInt("raq_line_num");
            raq.raq_title = rs.getString("raq_title");
            raq.raq_lan = rs.getString("raq_lan");
            raq.raq_type = rs.getString("raq_type");
            if (raq.raq_type.equals(dbQuestion.RES_SUBTYPE_FSC) 
                || raq.raq_type.equals(dbQuestion.RES_SUBTYPE_DSC)) {
                    parent_sc_raq_id = raq.raq_line_num;
                }
            raq.raq_status = rs.getString("raq_status");
			raq.raq_submit_file_ind = rs.getBoolean("raq_submit_file_ind");
            raq.raq_difficulty = rs.getInt("raq_difficulty");
            raq.raq_score = rs.getInt("raq_score");
            raq.raq_duration = rs.getFloat("raq_duration");
            raq.raq_privilege = rs.getString("raq_privilege");
            raq.raq_usr_id_owner = rs.getString("raq_usr_id_owner");
            raq.raq_obj_id = rs.getLong("raq_obj_id");
            if (raq.raq_obj_id == 0 && parent_sc_raq_id > 0) {
                raq.raq_parent_id = parent_sc_raq_id;
            }
            raq.raq_criteria = rs.getString("raq_criteria");
            // for oracle clob
            raq.raq_que_xml = cwSQL.getClobValue(rs, "raq_que_xml");
            raq.raq_outcome_xml_1 = cwSQL.getClobValue(rs, "raq_outcome_xml_1");
            raq.raq_explain_xml_1 = cwSQL.getClobValue(rs, "raq_explain_xml_1");
			raq.raq_desc = cwSQL.getClobValue(rs, "raq_desc");
//            raq.raq_que_xml = rs.getString("raq_que_xml");
            raq.raq_int_cnt = rs.getInt("raq_int_cnt");
//            raq.raq_outcome_xml_1 = rs.getString("raq_outcome_xml_1");
//            raq.raq_explain_xml_1 = rs.getString("raq_explain_xml_1");
			raq.raq_shuffle = rs.getBoolean("raq_shuffle");
            raq.resource_id = rs.getLong("raq_res_id");
            raqVec.addElement(raq);
        }
        stmt.close();
        return raqVec;

    }

    public static int del(Connection con, long ulg_id) throws SQLException {

        PreparedStatement stmt = con.prepareStatement(sql_del_);
        stmt.setLong(1, ulg_id);
        int cnt = stmt.executeUpdate();
        stmt.close();
        return cnt;

    }

}