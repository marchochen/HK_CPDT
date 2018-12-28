package com.cw.wizbank.ae.db;

import java.sql.*;
import com.cw.wizbank.ae.db.sql.*;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;

public class DbReportSpec {
    public long rsp_id;
    public long rsp_rte_id;
    public long rsp_ent_id;
    public String rsp_title;
    public String rsp_xml;
    public String rsp_create_usr_id;
    public Timestamp rsp_create_timestamp;
    public String rsp_upd_usr_id;
    public Timestamp rsp_upd_timestamp;

    public static String RSP_ID = "rsp_id";
    public static String RSP_RTE_ID = "rsp_rte_id";
    public static String RSP_ENT_ID = "rsp_ent_id";
    public static String RSP_TITLE = "rsp_title";
    public static String RSP_XML = "rsp_xml";
    public static String RSP_CREATE_USR_ID = "rsp_create_usr_id";
    public static String RSP_CREATE_TIMESTAMP = "rsp_create_timstamp";
    public static String RSP_UPD_USR_ID = "rsp_upd_usr_id";
    public static String RSP_UPD_TIMESTAMP = "rsp_upd_timestamp";

    public void ins(Connection con) throws SQLException, cwException {
        PreparedStatement stmt;
        StringBuffer sql = new StringBuffer();

        sql.append("INSERT INTO ReportSpec (rsp_rte_id, rsp_ent_id, rsp_title, rsp_xml, rsp_create_usr_id, rsp_create_timestamp, rsp_upd_usr_id, rsp_upd_timestamp) VALUES (?, ?, ?, ");
        sql.append(cwSQL.getClobNull()).append(", ?, ?, ?, ?)");

        if (rsp_create_timestamp == null) {
            rsp_create_timestamp = cwSQL.getTime(con);
            rsp_upd_timestamp = rsp_create_timestamp;
        }

        stmt = con.prepareStatement(sql.toString(), PreparedStatement.RETURN_GENERATED_KEYS);
        stmt.setLong(1, rsp_rte_id);
        stmt.setLong(2, rsp_ent_id);
        stmt.setString(3, rsp_title);
        stmt.setString(4, rsp_create_usr_id);
        stmt.setTimestamp(5, rsp_create_timestamp);
        stmt.setString(6, rsp_upd_usr_id);
        stmt.setTimestamp(7, rsp_upd_timestamp);

        if (stmt.executeUpdate()!= 1) {
            throw new cwException("com.cw.wizbank.ae.db.DbReportSpec.ins: Fail to insert ReportSpec to DB");
        }

        // Update rsp_xml
        // for oracle clob
        rsp_id = cwSQL.getAutoId(con, stmt, "ReportSpec", "rsp_id");
        String condition = "rsp_id = " + rsp_id;
        String tableName = "ReportSpec";
        String[] colName = {"rsp_xml"};
        String[] colValue = {rsp_xml};
        cwSQL.updateClobFields(con, tableName, colName, colValue, condition);
        //SELECT rsp_xml FROM ReportSpec WHERE rsp_rte_id = ? AND rsp_ent_id = ? AND rsp_create_usr_id = ? AND rsp_create_timestamp = ? FOR UPDATE
//        stmt = con.prepareStatement(SqlStatements.sql_get_rpt_rsp_xml_1, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
//        stmt.setLong(1, rsp_rte_id);
//        stmt.setLong(2, rsp_ent_id);
//        stmt.setString(3, rsp_create_usr_id);
//        stmt.setTimestamp(4, rsp_create_timestamp);
//
//        ResultSet rs = stmt.executeQuery();
//
//        if (rs.next()) {
//            cwSQL.setClobValue(con, rs, "rsp_xml", rsp_xml);
//            rs.updateRow();
//
//        }

        stmt.close();
    }

    public void upd(Connection con) throws SQLException, cwException {
        PreparedStatement stmt;

        if (rsp_upd_timestamp == null) {
            rsp_upd_timestamp = cwSQL.getTime(con);
        }

        stmt = con.prepareStatement(SqlStatements.sql_upd_rpt_spec);
        stmt.setLong(1, rsp_ent_id);
        stmt.setString(2, rsp_title);
        stmt.setString(3, rsp_upd_usr_id);
        stmt.setTimestamp(4, rsp_upd_timestamp);
        stmt.setLong(5, rsp_id);

        if (stmt.executeUpdate()!= 1) {
            throw new cwException("com.cw.wizbank.ae.db.DbReportSpec.upd: Fail to update ReportSpec to DB");
        }
        //SELECT rsp_xml FROM ReportSpec WHERE rsp_id = ? FOR UPDATE
        //Update rsp_xml
        // for oracle clob
        String condition = "rsp_id = " + rsp_id;
        String tableName = "ReportSpec";
        String[] colName = {"rsp_xml"};
        String[] colValue = {rsp_xml};
        cwSQL.updateClobFields(con, tableName, colName, colValue, condition);

//        stmt = con.prepareStatement(SqlStatements.sql_get_rpt_rsp_xml_2, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
//        stmt.setLong(1, rsp_id);
//        ResultSet rs = stmt.executeQuery();
//
//        if (rs.next()) {
//            cwSQL.setClobValue(con, rs, "rsp_xml", rsp_xml);
//            rs.updateRow();
//        }

        stmt.close();
    }

    public void del(Connection con) throws SQLException, cwException {
        PreparedStatement stmt;

        stmt = con.prepareStatement(SqlStatements.sql_del_rpt_spec);
        stmt.setLong(1, rsp_id);

        if (stmt.executeUpdate()!= 1) {
            throw new cwException("com.cw.wizbank.ae.db.DbReportSpec.del: Fail to delete ReportSpec from DB");
        }

        stmt.close();
    }
}