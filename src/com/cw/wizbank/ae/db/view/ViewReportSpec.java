package com.cw.wizbank.ae.db.view;

import java.sql.*;
import java.util.Vector;
import com.cw.wizbank.ae.db.sql.*;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwException;

public class ViewReportSpec {
    public class Data {
        public Data(){ ; }
        
        public long rte_id;
        public String rte_title_xml;
        public String rte_type;
        public String rte_get_xsl;
        public String rte_exe_xsl;
        public String rte_dl_xsl;
        public String rte_meta_data_url;
        
        public long rsp_id;
        public long rsp_rte_id;
        public long rsp_ent_id;
        public String rsp_title;
        public String rsp_xml;
        public String rsp_create_usr_id;
        public Timestamp rsp_create_timestamp;
        public String rsp_upd_usr_id;
        public Timestamp rsp_upd_timestamp;
    }

    public Data getReportTemplate(Connection con, long root_ent_id, String rte_type) throws SQLException, cwException {
        long id = 0;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "";
            if(rte_type.equals("LEARNER")){
                sql="SELECT rte_id FROM ReportTemplate WHERE rte_type = ?";
            }else{
                sql=SqlStatements.sql_get_rpt_id;
            }
            stmt = con.prepareStatement(sql);
            if(rte_type.equals("LEARNER")){
                stmt.setString(1, rte_type);
            }else{
                stmt.setLong(1, root_ent_id);
                stmt.setString(2, rte_type);
            }
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                id = rs.getLong("rte_id");
            } else {
                throw new cwException("com.cw.wizbank.ae.db.ViewReportSpec.getReportTemplate: Can't retrieve data from DB where rte_type = " + rte_type);            
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        return getReportTemplate(con, id);
    }

    public Data getReportTemplate(Connection con, long rte_id) throws SQLException, cwException {
        Data d = new Data();
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_rpt_tpl);
        stmt.setLong(1, rte_id);
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            d.rte_id = rs.getLong("rte_id");
            d.rte_type = rs.getString("rte_type");
            d.rte_title_xml = rs.getString("rte_title_xml");
            d.rte_get_xsl = rs.getString("rte_get_xsl");
            d.rte_exe_xsl = rs.getString("rte_exe_xsl");
            d.rte_dl_xsl = rs.getString("rte_dl_xsl");
            d.rte_meta_data_url = rs.getString("rte_meta_data_url");            
        } else {
            throw new cwException("com.cw.wizbank.ae.db.ViewReportSpec.getReportTemplate: Can't retrieve data from DB where rte_id = " + rte_id);
        }
        
        stmt.close();
        
        return d;
    }
    
    public Vector getReportTemplateList(Connection con, long root_ent_id, String type_str) throws SQLException {
        Vector vec = new Vector();
        StringBuffer sql = new StringBuffer();
        
        sql.append("SELECT rte_id, rte_type, rte_title_xml, rte_meta_data_url, rte_get_xsl, rte_exe_xsl, rte_dl_xsl FROM ReportTemplate WHERE rte_owner_ent_id = ? ");

        if (type_str != null) {
            sql.append(" AND rte_type IN ").append(type_str);
        }
        
        sql.append(" ORDER BY rte_seq_no");
//System.out.println(sql.toString());                
        PreparedStatement stmt = con.prepareStatement(sql.toString());
        stmt.setLong(1, root_ent_id);
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            Data d = new Data();
            
            d.rte_id = rs.getLong("rte_id");
            d.rte_type = rs.getString("rte_type");
            d.rte_title_xml = rs.getString("rte_title_xml");
            d.rte_get_xsl = rs.getString("rte_get_xsl");
            d.rte_exe_xsl = rs.getString("rte_exe_xsl");
            d.rte_dl_xsl = rs.getString("rte_dl_xsl");
            d.rte_meta_data_url = rs.getString("rte_meta_data_url");
            
            vec.addElement(d);
        }        

        stmt.close();
        
        return vec;
    }
    
    public Vector getReportSpecList(Connection con, long usr_ent_id, String template_id_str, boolean show_public) throws SQLException {
        Vector vec = new Vector();
        StringBuffer sql = new StringBuffer();
        
        sql.append("SELECT rsp_id, rsp_rte_id, rsp_ent_id, rsp_title, rsp_xml FROM ReportSpec WHERE ");
        
        if (show_public) {
            sql.append(" (rsp_ent_id = ? OR rsp_ent_id is null)");
        } else {
            sql.append(" rsp_ent_id = ?");
        }
                
        sql.append(" AND rsp_rte_id IN ");
        sql.append(template_id_str).append(" ORDER BY rsp_title");
       
        PreparedStatement stmt = con.prepareStatement(sql.toString());
        stmt.setLong(1, usr_ent_id);
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            Data d = new Data();
            
            d.rsp_id = rs.getLong("rsp_id");
            d.rsp_rte_id = rs.getLong("rsp_rte_id");
            d.rsp_ent_id = rs.getLong("rsp_ent_id");
            d.rsp_title = rs.getString("rsp_title");
            d.rsp_xml = cwSQL.getClobValue(rs, "rsp_xml");
            
            vec.addElement(d);
        }
        
        stmt.close();
        
        return vec;
    }
    
    public Data getTemplateAndSpec(Connection con, long rsp_id) throws SQLException, cwException {
        Data data = new Data();
        PreparedStatement stmt;
        ResultSet rs;

        stmt = con.prepareStatement(SqlStatements.sql_get_rpt_spec);
        stmt.setLong(1, rsp_id);
        rs = stmt.executeQuery();
        
        if (rs.next()) {
            data.rte_id = rs.getLong("rte_id");
            data.rte_title_xml = rs.getString("rte_title_xml");
            data.rte_type = rs.getString("rte_type");
            data.rte_get_xsl = rs.getString("rte_get_xsl");
            data.rte_exe_xsl = rs.getString("rte_exe_xsl");
            data.rte_dl_xsl = rs.getString("rte_dl_xsl");
            data.rte_meta_data_url = rs.getString("rte_meta_data_url");
            
            data.rsp_id = rs.getLong("rsp_id");
            data.rsp_rte_id = rs.getLong("rsp_rte_id");
            data.rsp_ent_id = rs.getLong("rsp_ent_id");
            data.rsp_title = rs.getString("rsp_title");
            data.rsp_xml = cwSQL.getClobValue(rs, "rsp_xml");
            data.rsp_create_usr_id = rs.getString("rsp_create_usr_id");
            data.rsp_create_timestamp = rs.getTimestamp("rsp_create_timestamp");
            data.rsp_upd_usr_id = rs.getString("rsp_upd_usr_id");
            data.rsp_upd_timestamp = rs.getTimestamp("rsp_upd_timestamp"); 
        } else {
            throw new cwException("com.cw.wizbank.ae.db.ViewReportSpec.getTemplateAndSpec: no TemplateSpec for rsp_id = " + rsp_id);
        }
        
        stmt.close();
        
        return data;
    }
    
    public Data getNewData() {
        return new Data();
    }
}