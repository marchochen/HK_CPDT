package com.cw.wizbank.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DbReportTemplate {

    private static final String sql_get_all_report_template = "select rte_id from Reporttemplate where rte_owner_ent_id =? and rte_type <> ?";
    public static List getAllReportTemplate(Connection con, long ste_ent_id) throws SQLException {
        List lst = new ArrayList();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = con.prepareStatement(sql_get_all_report_template);
            pstmt.setLong(1, ste_ent_id);
            pstmt.setString(2, "STAFF");
            rs = pstmt.executeQuery();
            while(rs.next()) {
                lst.add(new Long(rs.getLong("rte_id")));
            }
        } finally {
            if (pstmt != null) pstmt.close();
        }
        return lst;
    }
}
