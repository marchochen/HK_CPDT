package com.cw.wizbank.db;

import java.util.Vector;
import java.sql.Connection;
import java.sql.Timestamp;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.cw.wizbank.qdb.dbObjective;
import com.cw.wizbank.qdb.dbQuestion;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.dbResource;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.qdb.loginProfile;

import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.db.sql.SqlStatements;
import com.cw.wizbank.quebank.quecontainer.DynamicScenarioQue;

public class DbQueContainerSpec {
    public static final int EMPTY = -1;

    public long qcs_id = 0;
    public String qcs_type;
    public long qcs_score = 0;
    public long qcs_difficulty = 0;
    public String qcs_privilege;
    public float qcs_duration = 0;
    public long qcs_qcount = 0;
    public long qcs_res_id = 0;
    public long qcs_obj_id = 0;

    public Timestamp qcs_create_timestamp;
    public String qcs_create_usr_id;
    public Timestamp qcs_update_timestamp;
    public String qcs_update_usr_id;

    public DbQueContainerSpec() {
    }

    public void get(Connection con) throws qdbException, cwSysMessage {
        try {
            String SQL = new String();

            SQL = SqlStatements.sql_get_que_container_spec;

            PreparedStatement stmt = con.prepareStatement(SQL);

            stmt.setLong(1, qcs_id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                qcs_res_id = rs.getLong("qcs_res_id");
                qcs_obj_id = rs.getLong("qcs_obj_id");
                qcs_type = rs.getString("qcs_type");
                qcs_score = rs.getLong("qcs_score");
                qcs_difficulty = rs.getLong("qcs_difficulty");
                qcs_privilege = rs.getString("qcs_privilege");
                if (rs.getString("qcs_duration") != null) {
                    qcs_duration = rs.getFloat("qcs_duration");
                } else {
                    qcs_duration = EMPTY;
                }
                qcs_qcount = rs.getLong("qcs_qcount");
                qcs_create_timestamp = rs.getTimestamp("qcs_create_timestamp");
                qcs_create_usr_id = rs.getString("qcs_create_usr_id");
                qcs_update_timestamp = rs.getTimestamp("qcs_update_timestamp");
                qcs_update_usr_id = rs.getString("qcs_update_usr_id");
            }
            stmt.close();

        } catch (SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }

    }

    public void get_frm_res_obj_id(Connection con)
        throws qdbException, cwSysMessage {
        try {
            String SQL = new String();

            SQL = SqlStatements.sql_get_que_container_spec_fr_res_obj_id;

            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, qcs_res_id);
            stmt.setLong(2, qcs_obj_id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                qcs_id = rs.getLong("qcs_id");
                qcs_type = rs.getString("qcs_type");
                qcs_score = rs.getLong("qcs_score");
                qcs_difficulty = rs.getLong("qcs_difficulty");
                qcs_privilege = rs.getString("qcs_privilege");

                if (rs.getString("qcs_duration") != null) {
                    qcs_duration = rs.getFloat("qcs_duration");
                } else {
                    qcs_duration = EMPTY;
                }
                qcs_qcount = rs.getLong("qcs_qcount");
                qcs_create_timestamp = rs.getTimestamp("qcs_create_timestamp");
                qcs_create_usr_id = rs.getString("qcs_create_usr_id");
                qcs_update_timestamp = rs.getTimestamp("qcs_update_timestamp");
                qcs_update_usr_id = rs.getString("qcs_update_usr_id");
            }
            stmt.close();

        } catch (SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    // add Question Container Spec for Dynamic Question Container
    public void ins(Connection con, String res_subtype, String usr_id)
        throws qdbException, cwSysMessage {

        try {
            String SQL = new String();

            SQL = SqlStatements.sql_ins_que_container_spec;
            Timestamp curTime = cwSQL.getTime(con);
            qcs_create_usr_id = usr_id;
            qcs_update_usr_id = usr_id;
            qcs_create_timestamp = curTime;
            qcs_update_timestamp = curTime;

            PreparedStatement stmt = con.prepareStatement(SQL);

            stmt.setString(1, qcs_type);
            stmt.setLong(2, qcs_score);
            stmt.setLong(3, qcs_difficulty);
            stmt.setString(4, qcs_privilege);
            if (qcs_duration != EMPTY) {
                stmt.setFloat(5, qcs_duration);
            } else {
                stmt.setNull(5, java.sql.Types.FLOAT);
            }
            stmt.setLong(6, qcs_qcount);
            stmt.setLong(7, qcs_res_id);
            if (res_subtype.equalsIgnoreCase(dbResource.RES_SUBTYPE_DSC)) {
                stmt.setLong(8, 0);
            } else {
                stmt.setLong(8, qcs_obj_id);
            }
            stmt.setTimestamp(9, qcs_create_timestamp);
            stmt.setString(10, qcs_create_usr_id);
            stmt.setTimestamp(11, qcs_update_timestamp);
            stmt.setString(12, qcs_update_usr_id);

            stmt.executeUpdate();
            stmt.close();

        } catch (SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    // add Question Container Spec for Dynamic Question Container
    public void upd(Connection con, String res_subtype, String usr_id)
        throws qdbException, cwSysMessage {

        try {
            String SQL = new String();
            Timestamp curTime = cwSQL.getTime(con);
            qcs_update_usr_id = usr_id;
            qcs_update_timestamp = curTime;

            SQL = SqlStatements.sql_upd_que_container_spec;

            PreparedStatement stmt = con.prepareStatement(SQL);

            stmt.setString(1, qcs_type);
            stmt.setLong(2, qcs_score);
            stmt.setLong(3, qcs_difficulty);
            stmt.setString(4, qcs_privilege);
            if (qcs_duration != EMPTY) {
                stmt.setFloat(5, qcs_duration);
            } else {
                stmt.setNull(5, java.sql.Types.FLOAT);
            }
            stmt.setLong(6, qcs_qcount);
            stmt.setTimestamp(7, qcs_update_timestamp);
            stmt.setString(8, qcs_update_usr_id);
            stmt.setLong(9, qcs_id);

            stmt.executeUpdate();
            stmt.close();

        } catch (SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    // delete a Question Container Spec for Dynamic Question Container
    public void del(Connection con, String res_subtype)
        throws qdbException, cwSysMessage {

        try {
            String SQL = new String();

            SQL = SqlStatements.sql_del_que_container_spec;

            PreparedStatement stmt = con.prepareStatement(SQL);

            stmt.setLong(1, qcs_id);

            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    // delete all Question Container Spec for a Dynamic Question Container
    public void del_frm_res_id(Connection con)
        throws qdbException, cwSysMessage {

        try {
            String SQL = new String();

            SQL = SqlStatements.sql_del_que_container_spec_frm_res_id;

            PreparedStatement stmt = con.prepareStatement(SQL);

            stmt.setLong(1, qcs_res_id);

            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    public void del_frm_res_obj_id(Connection con)
        throws qdbException, cwSysMessage {

        try {
            String SQL = new String();

            SQL = SqlStatements.sql_del_que_container_spec_frm_res_obj_id;

            PreparedStatement stmt = con.prepareStatement(SQL);

            stmt.setLong(1, qcs_res_id);
            stmt.setLong(2, qcs_obj_id);

            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }

    }

    public static Vector getQueContainerSpecs(Connection con, long qcs_res_id)
        throws SQLException {
        Vector result = new Vector();

        PreparedStatement stmt =
            con.prepareStatement(SqlStatements.sql_get_que_container_specs);

        stmt.setLong(1, qcs_res_id);

        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            DbQueContainerSpec dbqcs = new DbQueContainerSpec();
            dbqcs.qcs_id = rs.getLong("qcs_id");
            dbqcs.qcs_res_id = qcs_res_id;
            dbqcs.qcs_obj_id = rs.getLong("qcs_obj_id");
            dbqcs.qcs_type = rs.getString("qcs_type");
            dbqcs.qcs_score = rs.getLong("qcs_score");
            dbqcs.qcs_difficulty = rs.getLong("qcs_difficulty");
            dbqcs.qcs_privilege = rs.getString("qcs_privilege");
            if (rs.getString("qcs_duration") != null) {
                dbqcs.qcs_duration = rs.getFloat("qcs_duration");
            } else {
                dbqcs.qcs_duration = EMPTY;
            }
            dbqcs.qcs_qcount = rs.getLong("qcs_qcount");
            dbqcs.qcs_create_timestamp =
                rs.getTimestamp("qcs_create_timestamp");
            dbqcs.qcs_create_usr_id = rs.getString("qcs_create_usr_id");
            dbqcs.qcs_update_timestamp =
                rs.getTimestamp("qcs_update_timestamp");
            dbqcs.qcs_update_usr_id = rs.getString("qcs_update_usr_id");

            result.addElement(dbqcs);
        }
        stmt.close();
        return result;

    }

    public String asXML(Connection con, loginProfile prof)
        throws qdbException, SQLException {
        StringBuffer xmlBuf = new StringBuffer();

        xmlBuf
            .append("<criterion id=\"")
            .append(qcs_id)
            .append("\" res_id=\"")
            .append(qcs_res_id)
            .append("\" obj_id=\"")
            .append(qcs_obj_id);

        if (qcs_type != null) {
            xmlBuf.append("\" type=\"").append(qcs_type);
        } else {
            xmlBuf.append("\" type=\"");
        }

        xmlBuf
            .append("\" score=\"")
            .append(qcs_score)
            .append("\" difficulty=\"")
            .append(qcs_difficulty);

        if (qcs_privilege != null) {
            xmlBuf.append("\" privilege=\"").append(qcs_privilege);
        } else {
            xmlBuf.append("\" privilege=\"");
        }

        if (qcs_duration != EMPTY) {
            xmlBuf.append("\" duration=\"").append(qcs_duration);
        } else {
            xmlBuf.append("\" duration=\"");
        }

        xmlBuf.append("\" q_count=\"").append(qcs_qcount).append("\"");
        Vector v_que_container_spec = new Vector();
        v_que_container_spec.addElement(this);
        xmlBuf
            .append(" valid_q_count=\"")
            .append(
                DynamicScenarioQue.getChildQueCount(con, v_que_container_spec))
            .append("\"");

        xmlBuf.append(" total_score=\"").append(qcs_score * qcs_qcount).append(
            "\"");
        xmlBuf.append(" create_usr=\"").append(qcs_create_usr_id).append("\"");
        xmlBuf.append(" create_timestamp=\"").append(
            qcs_create_timestamp).append(
            "\"");
        xmlBuf.append(" update_usr=\"").append(qcs_update_usr_id).append("\"");
        xmlBuf.append(" update_timestamp=\"").append(
            qcs_update_timestamp).append(
            "\"");

        xmlBuf.append(">").append(dbUtils.NEWL);

        if (qcs_obj_id > 0) {
            dbObjective myDbObjective = new dbObjective();
            myDbObjective.obj_id = qcs_obj_id;
            myDbObjective.get(con);

            xmlBuf
                .append("<objective id=\"")
                .append(myDbObjective.obj_id)
                .append("\" syl_id=\"")
                .append(myDbObjective.obj_syl_id)
                .append("\" type=\"")
                .append(myDbObjective.obj_type)
                .append("\" root_ent_id=\"")
                .append(prof.root_ent_id)
                .append("\" obj_id_parent=\"")
                .append(myDbObjective.obj_obj_id_parent)
                .append("\">")
                .append(dbUtils.NEWL);
            xmlBuf
                .append("<desc>")
                .append(dbUtils.esc4XML(myDbObjective.obj_desc))
                .append("</desc>")
                .append(dbUtils.NEWL);
            xmlBuf.append("</objective>").append(dbUtils.NEWL);
        }

        xmlBuf.append("</criterion>").append(dbUtils.NEWL);
        return xmlBuf.toString();
    }

    /**
    Check if this Spec is a valid new spec for Dynamic Scenario Question
    Pre-defined class variables: qcs_res_id
                                 qcs_score
    @param con Connection to database
    @return true if the spec is valid; false otherwise
    */
    public boolean validateDSCSpecBeforeInsert(Connection con)
        throws SQLException {
        PreparedStatement stmt = null;
        boolean isValid = false;
        try {
            stmt = con.prepareStatement(SqlStatements.SQL_VALID_NEW_DSC_SPEC);
            stmt.setLong(1, this.qcs_res_id);
            stmt.setLong(2, this.qcs_score);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                isValid = (rs.getLong(1) == 0);
            }
        } finally {
            if (stmt != null)
                stmt.close();
        }
        return isValid;
    }

    /**
    Check if this Spec is a valid new spec for Dynamic Scenario Question
    Pre-defined class variables: qcs_res_id
                                 qcs_score
                                 qcs_id
    @param con Connection to database
    @return true if the spec is valid; false otherwise
    */
    public boolean validateDSCSpecBeforeUpdate(Connection con)
        throws SQLException {
        PreparedStatement stmt = null;
        boolean isValid = false;
        try {
            stmt = con.prepareStatement(SqlStatements.SQL_VALID_EXIST_DSC_SPEC);
            stmt.setLong(1, this.qcs_res_id);
            stmt.setLong(2, this.qcs_score);
            stmt.setLong(3, this.qcs_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                isValid = (rs.getLong(1) == 0);
            }
        } finally {
            if (stmt != null)
                stmt.close();
        }
        return isValid;
    }

    public void removeAll(Connection con, long container_res_id)
        throws SQLException {
        PreparedStatement stmt = null;
        boolean isValid = false;
        try {
            stmt = con.prepareStatement(SqlStatements.SQL_DEL_DSC_ALL_SPEC);
            stmt.setLong(1, container_res_id);
            stmt.setString(2, dbQuestion.RES_SUBTYPE_DSC);
            stmt.execute();
        } finally {
            if (stmt != null)
                stmt.close();
        }
    }

}