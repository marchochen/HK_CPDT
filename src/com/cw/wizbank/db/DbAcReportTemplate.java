package com.cw.wizbank.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.cw.wizbank.qdb.loginProfile;

public class DbAcReportTemplate {

    private static final String sql_ins_all_report_template = "insert into acreporttemplate (ac_rte_id,ac_rte_rol_ext_id,ac_rte_ftn_ext_id,ac_rte_owner_ind,ac_rte_create_usr_id,ac_rte_create_timestamp)"
            + "values (?,?,?,?,?,?)";

    public static void insReportForRole(Connection con, String rol_ext_id,
            Timestamp cur_time, String upd_usr_id, long root_ent_id)
            throws SQLException {
        List lst = DbReportTemplate.getAllReportTemplate(con, root_ent_id);
        if (lst.size() > 0) {
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            try {
                pstmt = con.prepareStatement(sql_ins_all_report_template);
                for (int i = 0; i < lst.size(); i++) {
                    long rte_id = ((Long) lst.get(i)).longValue();
                    int index = 1;
                    pstmt.setLong(index++, rte_id);
                    pstmt.setString(index++, rol_ext_id);
                    pstmt.setString(index++, "RTE_READ");
                    pstmt.setInt(index++, 0);
                    pstmt.setString(index++, upd_usr_id);
                    pstmt.setTimestamp(index++, cur_time);
                    pstmt.addBatch();
                }
                pstmt.executeBatch();
            } finally {
                if (pstmt != null)
                    pstmt.close();
            }
        }
    }
    
    public static void insReportForRole(Connection con, long rol_id,
            Timestamp cur_time, String upd_usr_id, long root_ent_id)
            throws SQLException {
        DbAcRole rol = DbAcRole.getRoleById(con, rol_id);
        insReportForRole(con, rol.getRolExtId(), cur_time, upd_usr_id, root_ent_id);
    }
    
    private static final String sql_del_report_template = "delete from acreporttemplate where ac_rte_rol_ext_id = ?";
    public static void delReportTemplateForRole(Connection con, String rol_ext_id) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            pstmt = con.prepareStatement(sql_del_report_template);
            pstmt.setString(1, rol_ext_id);
            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) pstmt.close();
        }
    }
    
    public static void delReportTemplateForRole(Connection con, long rol_id) throws SQLException {
        DbAcRole rol = DbAcRole.getRoleById(con, rol_id);
        delReportTemplateForRole(con, rol.getRolExtId());
    }
    
    private static final String sql_check_exists_by_ext_id = "select ac_rte_id from acReportTemplate where ac_rte_rol_ext_id = ?";
    public static boolean checkExist(Connection con, String rol_ext_id) throws SQLException {
        boolean result = false;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = con.prepareStatement(sql_check_exists_by_ext_id);
            pstmt.setString(1, rol_ext_id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                result = true;
            }
        } finally {
            if (pstmt != null)
                pstmt.close();
        }
        return result;
    }
    
    public static boolean checkExist(Connection con, long rol_id) throws SQLException {
        DbAcRole rol = DbAcRole.getRoleById(con, rol_id);
        return checkExist(con, rol.getRolExtId());
    }
}
