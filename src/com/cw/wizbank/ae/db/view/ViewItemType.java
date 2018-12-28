package com.cw.wizbank.ae.db.view;

import java.util.Vector;
import java.sql.*;

import com.cw.wizbank.ae.aeItemDummyType;
import com.cw.wizbank.ae.db.DbItemType;
import com.cw.wizbank.util.cwUtils;



public class ViewItemType{

    public String ity_id;
    public boolean ity_run_ind;
    public boolean ity_create_run_ind;
    public boolean ity_apply_ind;
    public boolean ity_qdb_ind;
    public boolean ity_blend_ind;
    public boolean ity_exam_ind;
    public boolean ity_ref_ind;
    public boolean ity_auto_enrol_qdb_ind;
    public String ity_init_life_status;
    public String ity_title_xml;
    public long itt_ttp_id;
    public String ttp_title;
    public long itt_seq_id;
    public long itt_tpl_id;

    public String item_type;

    /**
     * 按课程类型获取课程类型详细信息
     * @param con
     * @param root_ent_id(集团ID)
     * @param item_type(课程类型)
     * @return
     * @throws SQLException
     */
    public Vector getAllItemTypeDetailInOrgAsXML(Connection con, long root_ent_id, String item_type)
        throws SQLException{

            String SQL = " Select ity_id, ity_run_ind, ity_create_run_ind, ity_apply_ind, ity_qdb_ind, "
                       + " ity_auto_enrol_qdb_ind, ity_init_life_status, ity_title_xml, "
                       + " ity_exam_ind ,ity_blend_ind ,ity_ref_ind, "
                       + " itt_ttp_id, ttp_title, itt_seq_id, itt_tpl_id "
                       + " FROM aeItemType, aeItemTypeTemplate, aeTemplateType "
                       + " WHERE ity_owner_ent_id = itt_ity_owner_ent_id "
                       + " AND itt_ity_id = ity_id "
                       + " AND ity_run_ind = ? "
                       + " AND ity_session_ind = ? "
                       + " AND itt_ity_owner_ent_id = ? "
                       + " AND itt_ttp_id = ttp_id ";
            if( item_type != null && item_type.length() > 0 )
                SQL += " AND ity_id = ? ";
            SQL += " Order by ity_seq_id, itt_ttp_id, itt_seq_id ASC ";

            PreparedStatement stmt = con.prepareStatement(SQL);
            int index = 1;
            stmt.setBoolean(index++, false);
            stmt.setBoolean(index++, false);
            stmt.setLong(index++, root_ent_id);
            if( item_type != null && item_type.length() > 0 )
                stmt.setString(index++, item_type);
            ResultSet rs = stmt.executeQuery();
            Vector resultVec = new Vector();
            while(rs.next()){
                ViewItemType vIty = new ViewItemType();
                vIty.ity_id = rs.getString("ity_id");
                vIty.ity_run_ind = rs.getBoolean("ity_run_ind");
                vIty.ity_create_run_ind = rs.getBoolean("ity_create_run_ind");
                vIty.ity_apply_ind = rs.getBoolean("ity_apply_ind");
                vIty.ity_blend_ind = rs.getBoolean("ity_blend_ind");
                vIty.ity_exam_ind = rs.getBoolean("ity_exam_ind");
                vIty.ity_ref_ind = rs.getBoolean("ity_ref_ind");
                vIty.ity_qdb_ind = rs.getBoolean("ity_qdb_ind");
                vIty.ity_auto_enrol_qdb_ind = rs.getBoolean("ity_auto_enrol_qdb_ind");
                vIty.ity_init_life_status = rs.getString("ity_init_life_status");
                vIty.itt_ttp_id = rs.getLong("itt_ttp_id");
                vIty.ttp_title = rs.getString("ttp_title");
                vIty.itt_seq_id = rs.getLong("itt_seq_id");
                vIty.itt_tpl_id = rs.getLong("itt_tpl_id");
                vIty.item_type=aeItemDummyType.getDummyItemType(vIty.ity_id, vIty.ity_blend_ind, vIty.ity_exam_ind, vIty.ity_ref_ind);
                vIty.ity_title_xml = DbItemType.getItemTypeXml(vIty.ity_id, vIty.ity_blend_ind, vIty.ity_exam_ind, vIty.ity_ref_ind);

                resultVec.addElement(vIty);
            }
            stmt.close();
            return resultVec;
    }

    public String getMetaDataAsXML() {
        StringBuffer xmlBuf = new StringBuffer(512);
        xmlBuf.append("<item_type_meta id=\"").append(this.ity_id).append("\"")
              .append(" run_ind=\"").append(this.ity_run_ind).append("\"")
              .append(" create_run_ind=\"").append(this.ity_create_run_ind).append("\"")
              .append(" apply_ind=\"").append(this.ity_apply_ind).append("\"")
              .append(" qdb_ind=\"").append(this.ity_qdb_ind).append("\"")
              .append(" auto_enrol_qdb_ind=\"").append(this.ity_auto_enrol_qdb_ind).append("\"")
              .append(" init_life_status=\"").append(cwUtils.escNull(this.ity_init_life_status)).append("\"/>");
        return xmlBuf.toString();
    }

    /*
    public static boolean isItmAccreditation(Connection con, long itm_id)
        throws SQLException {
            
            String SQL = " SELECT ity_iad_ind "
                       + " From aeItemType, aeItem "
                       + " WHERE ity_id = itm_type "
                       + " AND itm_id = ? "
                       + " AND itm_run_ind = ity_run_ind "
                       + " AND itm_owner_ent_id = ity_owner_ent_id ";
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, itm_id);
            ResultSet rs = stmt.executeQuery();
            boolean flag = false;
            if(rs.next())
                flag = rs.getBoolean("ity_iad_ind");
            else
                throw new SQLException("Failed to find an item, itm id = " + itm_id);
                
            stmt.close();
            return flag;
        }
    */
}