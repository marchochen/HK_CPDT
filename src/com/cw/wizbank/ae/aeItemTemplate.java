package com.cw.wizbank.ae;

import java.sql.*;

/**
 * a class to handle all template usage of an item
 * REPLACED BY com.cw.wizbank.ae.db.view.ViewItemTemplate
 */
public class aeItemTemplate {
    private Connection con;
    private long itemID;
    
    public aeItemTemplate(Connection con, long itemID) {
        this.con    = con;
        this.itemID = itemID;
    }
    
    /**
     * insert a template into the template usage of this item
     */
     /*
    private final String sql_ins_itm_tpl =
        " Insert into aeItemTemplate "
      + " (itp_ttp_id, itp_itm_id, itp_tpl_id, itp_create_timestamp, itp_create_usr_id) "
      + " Select ttp_id, ?, ?, ?, ? From aeTemplateType "
      + " Where ttp_title = ? ";
    public void ins(long tplID, String tplType, Timestamp actTime, String actUserID) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(sql_ins_itm_tpl);
        int col = 1;
        
        stmt.setLong(col++, itemID);
        stmt.setLong(col++, tplID);
        stmt.setTimestamp(col++, actTime);
        stmt.setString(col++, actUserID);
        stmt.setString(col++, tplType);
        stmt.executeUpdate();
        stmt.close();
    }
    */
    /**
     * delete all item usage of this item
     */
     /*
    private final String sql_del_itm_tpl =
        " Delete From aeItemTemplate Where itp_itm_id = ? ";
    public void del() throws SQLException {
        PreparedStatement stmt = con.prepareStatement(sql_del_itm_tpl);
        int col = 1;
        stmt.setLong(col++, itemID);
        stmt.executeUpdate();
        stmt.close();
    }
    */
    /**
     * get the id's of all the template this item has been using
     * 2001.07.30 wai
     */
     /*
    private final String sql_get_item_all_template_id =
        "SELECT itp_tpl_id, ttp_title FROM aeItemTemplate, aeTemplateType WHERE itp_itm_id = ? AND itp_ttp_id = ttp_id ";
    public Hashtable getUsage() throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int col = 0;
        Hashtable result = new Hashtable();
        
        // get all the templates this item has been using
        long tplID;
        String tplType;
        stmt = con.prepareStatement(sql_get_item_all_template_id);
        col = 1;
        stmt.setLong(col++, itemID);
        rs = stmt.executeQuery();
        // for each template, get the details
        while (rs.next()) {
            col = 1;
            tplID   = rs.getLong(col++);
            tplType = rs.getString(col++);
            result.put(tplType, new Long(tplID));
        }
        stmt.close();
        
        return result;
    }
    */
    /**
     * get view of this item of the specified template and type
     * 2001.08.01 wai
     */
     /*
    private final String sql_get_item_template_view =
        "SELECT tvw_url "
      + "FROM aeItemTemplateView, aeTemplateType tpltype, aeTemplateType viewtype, aeTemplateView "
      + "WHERE itv_itp_itm_id = ? "
      + "AND itv_itp_ttp_id = tpltype.ttp_id AND tpltype.ttp_title = ? "
      + "AND itv_tvw_ttp_id = viewtype.ttp_id AND viewtype.ttp_title = ? "
      + "AND itv_tvw_tpl_id = tvw_tpl_id AND itv_tvw_ttp_id = tvw_ttp_id AND itv_tvw_seq_id = tvw_seq_id ";
    public String getView(String tplType, String viewType) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int col = 0;
        
        String tplView;
        stmt = con.prepareStatement(sql_get_item_template_view);
        col = 1;
        stmt.setLong(col++, itemID);
        stmt.setString(col++, tplType);
        stmt.setString(col++, viewType);
        rs = stmt.executeQuery();
        if (rs.next()) {
            col = 1;
            tplView = rs.getString(col++);
        } else {
            tplView = null;
        }
        stmt.close();
        
        return tplView;
    }
    */
    /**
     * get all the views this item is using, for the specific type of template
     * 2001.08.03 wai
     */
     /*
    private final String sql_get_all_item_template_view =
        "SELECT viewtype.ttp_title, tvw_url "
      + "FROM aeItemTemplateView, aeTemplateType tpltype, aeTemplateView, aeTemplateType viewtype "
      + "WHERE itv_itp_itm_id = ? "
      + "AND itv_itp_ttp_id = tpltype.ttp_id AND tpltype.ttp_title = ? "
      + "AND itv_tvw_tpl_id = tvw_tpl_id AND itv_tvw_ttp_id = tvw_ttp_id AND itv_tvw_seq_id = tvw_seq_id "
      + "AND itv_tvw_ttp_id = viewtype.ttp_id ";
    public StringBuffer getViewAsXML(String tplType) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int col = 0;
        StringBuffer result = new StringBuffer(500);
        result.append("<template_details type=\"").append(tplType).append("\">")
              .append("<view_list>");
        
        String viewTitle;
        String viewURL;
        stmt = con.prepareStatement(sql_get_all_item_template_view);
        col = 1;
        stmt.setLong(col++, itemID);
        stmt.setString(col++, tplType);
        rs = stmt.executeQuery();
        while (rs.next()) {
            col = 1;
            viewTitle = rs.getString(col++);
            viewURL   = rs.getString(col++);
            result.append("<view")
                  .append(" type=\"").append(viewTitle).append("\"")
                  .append(" url=\"").append(viewURL).append("\"")
                  .append(" />");
        }
        stmt.close();
        
        result.append("</view_list>")
              .append("</template_details>");
        return result;
    }
    */
}