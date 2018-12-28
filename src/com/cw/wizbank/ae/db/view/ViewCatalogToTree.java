package com.cw.wizbank.ae.db.view;

import java.util.Hashtable;
import java.util.Vector;
import java.sql.*;
import com.cw.wizbank.ae.aeTreeNode;
import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.util.cwSQL;

public class ViewCatalogToTree {
    public static Hashtable treeNodeNoChildAndTypeInfo(Connection con, long[] tnd_lst, boolean checkStatus, boolean notIncludeItemNode) throws SQLException{
		StringBuffer tnd_lst_sql = new StringBuffer();
		Hashtable result = new Hashtable();
		Vector v_tnd_id = new Vector();
		// tnd_lst_sql.append("(0");
		v_tnd_id.add(new Long(0));

		for (int i = 0; i < tnd_lst.length; i++) {
			// tnd_lst_sql.append(", ").append(tnd_lst[i]);
			v_tnd_id.add(new Long(tnd_lst[i]));
		}

		String colName = "tmp_tnd_id";
		String tableName = cwSQL.createSimpleTemptable(con, colName, cwSQL.COL_TYPE_LONG, 0);
		cwSQL.insertSimpleTempTable(con, tableName, v_tnd_id, cwSQL.COL_TYPE_LONG);

		tnd_lst_sql.append(" (select ").append(colName).append(" from ").append(tableName).append(") ");
        StringBuffer sql = new StringBuffer();
        
        sql.append("select parent.tnd_id AS iden, parent.tnd_type AS type from aeTreeNode parent where parent.tnd_id in ");

        if (notIncludeItemNode) {
            sql.append(tnd_lst_sql.toString());            
        } else {
            StringBuffer sql2 = new StringBuffer();

            sql2.append("select parent.tnd_id from aeTreeNode parent where parent.tnd_id in ").append(tnd_lst_sql.toString()).append(" AND NOT EXISTS (select child.tnd_id from aeTreeNode child, aeItem where child.tnd_parent_tnd_id = parent.tnd_id and itm_id = child.tnd_itm_id");
            
            if (checkStatus) {
                sql2.append(" and itm_status <> '").append(aeItem.ITM_STATUS_OFF).append("' ");       
            }
            
            sql2.append(" )");            
            sql.append("(").append(sql2.toString()).append(")");
        }
                
        sql.append(" AND NOT EXISTS (Select child.tnd_id from aeTreeNode child where child.tnd_parent_tnd_id = parent.tnd_id AND tnd_type <> '").append(aeTreeNode.TND_TYPE_ITEM).append("' ");

        if (checkStatus) {
            sql.append(" And tnd_status <> '").append(aeTreeNode.TND_STATUS_OFF).append("' ");
        }
        
        sql.append(" )");
        PreparedStatement stmt = con.prepareStatement(sql.toString());
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            result.put(rs.getString("iden"), rs.getString("type"));
        }
        if(tableName != null && tableName.length() > 0){
            cwSQL.dropTempTable(con, tableName); 
        }
        cwSQL.cleanUp(rs, stmt);
        return result;
    }
}