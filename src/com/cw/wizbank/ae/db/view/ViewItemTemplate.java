package com.cw.wizbank.ae.db.view;

import java.util.Vector;
import java.util.Hashtable;
import java.sql.*;
import com.cw.wizbank.util.*;
import com.cw.wizbank.ae.db.sql.SqlStatements;

/**
Handle templates related to item
*/
public class ViewItemTemplate{

    public String itemType;
    public long ownerEntId;
    public String templateType;
    public long templateId;
    public long viewId;
    public long itemId;
    public boolean runInd;
    public boolean sessionInd;
    public Timestamp createTimestamp;
    public String createUserId;

    /**
    get the 1st tpl_id of(itemType, templateType, ownerEntId) from database <BR>
    pre-define variables:
    <ul>
    <li>itemType</li>
    <li>ownerEntId</li>
    <li>templateType</li>
    <li>runInd</li>
    <li>sessionInd</li>
    <ul>
    */
    public long getFirstTplId(Connection con) throws SQLException {
        
        long templateId;
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_templateId_of_itemTypeTemplate);
        stmt.setLong(1, this.ownerEntId);
        stmt.setString(2, this.itemType);
        stmt.setString(3, this.templateType);
        stmt.setBoolean(4, this.runInd);
        stmt.setBoolean(5, this.sessionInd);

        ResultSet rs = stmt.executeQuery();
        if(rs.next()) {
            templateId = rs.getLong("itt_tpl_id");
        }
        else {
            templateId = 0;
        }
        stmt.close();
        return templateId;
    }


    /** 
    Get Template Id and Template Type Id of an Item Type
    Pre-define variables:
    <ul>
    <li>itemType</li>
    <li>ownerEntId</li>
    <li>runInd</li>
    <li>sessionInd</li>
    </ul>
    TODO: this method cannot handle run.
    @return Hashtable of keys as template type id and values template id
    */
    public Hashtable getTemplateByItemType(Connection con) throws SQLException {
        Hashtable h = new Hashtable();
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(SqlStatements.SQL_GET_TPL_BY_ITY);
            stmt.setLong(1, this.ownerEntId);
            stmt.setString(2, this.itemType);
            stmt.setBoolean(3, this.runInd);
            stmt.setBoolean(4, this.sessionInd);

            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                h.put(rs.getString("ttp_title"), new Long(rs.getLong("itt_tpl_id")));
            }
        } finally {
            if(stmt!=null) stmt.close();
        }
        return h;
    }
    

    /**
    get the template tpyes that the item type will have<BR>
    pre-define variables:
    <ul>
    <li>itemType</li>
    <li>ownerEntId</li>
    <li>runInd</li>
    <li>sessionInd</li>
    </ul>
    */
    public String[] getItemTypeTemplates(Connection con) throws SQLException {
        
        Vector v_templateTypes = new Vector();
        String[] templateTypes;
        long templateId;
        String templateType;
        StringBuffer SQLBuf = new StringBuffer(512);
        // template type for this item
        SQLBuf.append(SqlStatements.sql_get_templateType_of_itemType);
        if(this.runInd ) {
            // union with the root item
            SQLBuf.append(" Union ");
            SQLBuf.append(SqlStatements.sql_get_templateType_of_itemType);            
//            if (this.sessionInd){
                // union with the run item (for the case course > run > session)
//                SQLBuf.append(" Union ");
//                SQLBuf.append(SqlStatements.sql_get_templateType_of_itemType);
//            }
        }
        PreparedStatement stmt = con.prepareStatement(SQLBuf.toString());
        int index = 1;
        stmt.setLong(index++, this.ownerEntId);
        stmt.setString(index++, this.itemType);
        stmt.setBoolean(index++, this.runInd);
        stmt.setBoolean(index++, this.sessionInd);

        if(this.runInd ) {
            stmt.setLong(index++, this.ownerEntId);
            stmt.setString(index++, this.itemType);
            stmt.setBoolean(index++, false);
            stmt.setBoolean(index++, false);
//            if (this.sessionInd){
//                stmt.setLong(index++, this.ownerEntId);
//                stmt.setString(index++, this.itemType);
//                stmt.setBoolean(index++, true);
//                stmt.setBoolean(index++, false);
//            } 
        }
        ResultSet rs = stmt.executeQuery();
        
        //put the templateTypes into Vector
        while(rs.next()) {
            
            templateId = rs.getLong("ttp_id");
            templateType = rs.getString("ttp_title");
            v_templateTypes.addElement(templateType);
        }
        stmt.close();
        
        //convert the Vector into String[]
        templateTypes = new String[v_templateTypes.size()];
        for(int i=0; i<v_templateTypes.size(); i++) {
        
            templateTypes[i] = (String) v_templateTypes.elementAt(i);
        }
        return templateTypes;
    }
    
    /**
     Get all tpl_id and ttp_title (template type) of item
     Pre-define variable:
     <ul>
     <li>itemId
     </ul>
     */
    private final String sql_get_item_all_template_id =
        "SELECT itp_tpl_id, ttp_title FROM aeItemTemplate, aeTemplateType WHERE itp_itm_id = ? AND itp_ttp_id = ttp_id ";
    public Hashtable getUsage(Connection con) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int col = 0;
        Hashtable result = new Hashtable();
        
        // get all the templates this item has been using
        long tplID;
        String tplType;
        stmt = con.prepareStatement(sql_get_item_all_template_id);
        col = 1;
        stmt.setLong(col++, itemId);
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
    
    /**
    Insert into table ItemTemplate
    */
    public void insItemTemplate(Connection con, String usr_id, Timestamp cur_time) throws SQLException {
        
        if(cur_time == null) {
            cur_time = cwSQL.getTime(con);
        }
        this.createTimestamp = cur_time;
        this.createUserId = usr_id;
        
		PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_select_view_item_template);
		stmt.setString(1, this.templateType);
		ResultSet rs = stmt.executeQuery();
		long ttp_id = 0;
		if(rs.next()) {
			ttp_id = rs.getLong("ttp_id");
		}
		rs.close();
		stmt.close();

		int index=1;
		PreparedStatement stmt2 = con.prepareStatement(SqlStatements.sql_ins_view_item_template);
		stmt2.setLong(index++, ttp_id);
		stmt2.setLong(index++, this.itemId);
		stmt2.setLong(index++, this.templateId);
		stmt2.setTimestamp(index++, this.createTimestamp);
		stmt2.setString(index++, this.createUserId);
		stmt2.executeUpdate();
        stmt2.close();
        return;
    }
    
    /**
    * Update item template
    */
    public void updItemTemplate(Connection con, Vector v_itm_id)
        throws SQLException{
        
            PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_upd_multiple_item_template + cwUtils.vector2list(v_itm_id));
            stmt.setLong(1, this.templateId);            
            stmt.setString(2, this.templateType);
            stmt.executeUpdate();
            stmt.close();
            return;
        }
    
    /**
     Pre-define variable:
     <ul>
     <li>itemId
     </ul>
    */
    public long getItemSelectedWorkflowTemplateId(Connection con)
        throws SQLException {
            
            PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_item_selected_workflow_template_id);
            stmt.setLong(1, this.itemId);
            stmt.setString(2, "WORKFLOW"); 
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                this.templateId = rs.getLong("itp_tpl_id");
            } else {
                this.templateId = 0;
            }
            stmt.close();
            return this.templateId;
        }


    /**
     Pre-define variable:
     <ul>
     <li>itemId
     <li>ownerEntId
     <li>templateType
     </ul>
    */    
    public Vector getItemSupportedTemplate(Connection con)
        throws SQLException {
            
            PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_item_suppored_template_id);
            stmt.setLong(1, this.itemId);
            stmt.setString(2, this.templateType);
            stmt.setLong(3, this.ownerEntId);
            ResultSet rs = stmt.executeQuery();
            Vector idVec = new Vector();
            while(rs.next())
                idVec.addElement(new Long(rs.getLong("itt_tpl_id")));
            stmt.close();
            return idVec;
            
        }

    /**
     Pre-define variable:
     <ul>
     <li>itemId
     <li>ownerEntId
     <li>templateType
     </ul>
    */        
    public String getItemSupportedTemplateAsXML(Connection con)
        throws SQLException {
            Vector vec = getItemSupportedTemplate(con);
            StringBuffer xml = new StringBuffer(256);
            long selected_id = getItemSelectedWorkflowTemplateId(con);
            xml.append("<workflow_list>");
            for(int i=0; i<vec.size(); i++) {
                xml.append("<workflow ")
                   .append(" tpl_id=\"").append(vec.elementAt(i)).append("\" ");
                if( selected_id == ((Long)vec.elementAt(i)).longValue() )
                    xml.append(" selected=\"selected\" ");
                xml.append(" />");
            }
            xml.append("</workflow_list>");
            return xml.toString();
        }
 
    public boolean getItemApprovalInd(Connection con) throws SQLException{
        boolean approval_ind = false;
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_item_approval_ind);
        stmt.setLong(1, this.itemId);
        stmt.setString(2, "WORKFLOW");
        
        ResultSet rs = stmt.executeQuery();
        while(rs.next()){
            approval_ind = rs.getBoolean("tpl_approval_ind");
        }
        stmt.close();
        return approval_ind; 
    }        
}