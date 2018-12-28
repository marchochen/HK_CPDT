package com.cw.wizbank.db;

import java.sql.*;
import java.util.Vector;
import com.cw.wizbank.util.*;
import com.cw.wizbank.db.sql.*;
import com.cw.wizbank.qdb.*;

public class DbEntityRelation extends dbEntityRelation{
    
    public static class ViewSynEntityRelation {
        public String group_uid;
        public String member_uid;
        public long group_ent_id;
        public long member_ent_id;
    }
    
    public void updSynDate(Connection con) throws SQLException{
		String dbproduct = cwSQL.getDbProductName();
    	PreparedStatement stmt = null;
        try{
            ern_syn_timestamp = cwSQL.getTime(con);
            String sql = "";
            if (dbproduct.indexOf(cwSQL.ProductName_ORACLE) >= 0) {
                sql =  "UPDATE EntityRelation parent "  
    				+ " SET parent.ern_syn_timestamp = ? "
    				+ "  WHERE EXISTS "
    				+ " (select * from  "
    				+ " EntityRelation child " 
    				+ " WHERE child.ern_child_ent_id = parent.ern_child_ent_id " 
    				+ " and child.ern_type = parent.ern_type  "
    				+ " and child.ern_child_ent_id = ? "
    				+ " and child.ern_ancestor_ent_id = ?) "; 
            }else{
	            sql =  "UPDATE parent "
					+ " SET ern_syn_timestamp = ? "
					+ " from EntityRelation child, EntityRelation parent "
					+ " WHERE child.ern_child_ent_id = parent.ern_child_ent_id "
					+ " and child.ern_type = parent.ern_type "
					+ " and child.ern_child_ent_id = ? "
					+ " and child.ern_ancestor_ent_id = ?";
            }
            stmt = con.prepareStatement(sql);
            
            stmt.setTimestamp(1, ern_syn_timestamp);
            stmt.setLong(2,ern_child_ent_id);
            stmt.setLong(3,ern_ancestor_ent_id);

            /* update */
            if(stmt.executeUpdate() < 1 )
            {
                // update fails, rollback
                con.rollback();
                throw new SQLException("Fails to update Entity Relation.");
            }
        }finally{
            if (stmt!=null)     stmt.close();    
        }
        return;
    }
    /**
    get a list of userRelation that the user is in-syn and active while userRelation is not syn
    @param con
    @param system start syn timestamp
    @param siteId
    @param entityrelation type
    @param parent ent type
    @return result set with result column: GROUP_UID, MEMBER_UID, ern_ancestor_ent_id, ern_child_ent_id 
    */
    public static ViewSynEntityRelation[] getNotInSynUsrRelation(Connection con, Timestamp synTimestamp, long siteId, String ernType, String parentType, String usr_source) throws SQLException{ 
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Vector tempResult = null;
        try {
            String sql = new String();
            if (usr_source != null) {
                sql = SqlStatements.sql_get_not_syn_usr_relation + " AND usr_source = ? ";
            } else {
                sql = SqlStatements.sql_get_not_syn_usr_relation;
            }
            stmt = con.prepareStatement(sql);
            int col=1;
            stmt.setLong(col++, siteId);
            stmt.setString(col++, DbRegUser.USR_STATUS_OK);
            stmt.setString(col++, DbRegUser.USR_STATUS_PENDING);
            stmt.setString(col++, "%" + parentType + "%");
            stmt.setTimestamp(col++, synTimestamp);
            stmt.setString(col++, ernType);
            stmt.setBoolean(col++, false);      // not remain
            stmt.setTimestamp(col++, synTimestamp); 
            stmt.setTimestamp(col++, synTimestamp); 
            stmt.setBoolean(col++, true);
            stmt.setBoolean(col++, true);
            if (usr_source != null) {
                stmt.setString(col++, usr_source);
            }
            rs = stmt.executeQuery();
            
            tempResult = new Vector();
            while (rs.next()) {
                ViewSynEntityRelation entRel = new ViewSynEntityRelation();
                entRel.group_uid = rs.getString("GROUP_UID");
                entRel.member_uid = rs.getString("MEMBER_UID");
                entRel.group_ent_id = rs.getLong("ern_ancestor_ent_id");
                entRel.member_ent_id = rs.getLong("ern_child_ent_id");
                tempResult.addElement(entRel);
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        ViewSynEntityRelation result[] = new ViewSynEntityRelation[tempResult.size()];
        result = (ViewSynEntityRelation[])tempResult.toArray(result);
        
        return result;
    }
    
    /**
    used for EntityRelation that usergroup as child
    get a list of not-in-syn usegRelation 
    @param con
    @param system start syn timestamp
    @param siteId
    @param entityrelation type
    @return result set with result column: GROUP_UID, MEMBER_UID, ern_ancestor_ent_id, ern_child_ent_id 
    */
    public static ViewSynEntityRelation[] getNotInSynUsgRelation(Connection con, Timestamp synTimestamp, long siteId, String ernType) throws SQLException{ 
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Vector tempResult = null;
        try {
            stmt = con.prepareStatement(SqlStatements.sql_get_not_syn_usergroup_relation);
            int col = 1;
            stmt.setLong(col++, siteId);
            stmt.setTimestamp(col++, synTimestamp);
            stmt.setString(col++, ernType);
            stmt.setBoolean(col++, false);
            stmt.setTimestamp(col++, synTimestamp); 
            stmt.setBoolean(col++, true);     
            stmt.setTimestamp(col++, synTimestamp); 
            stmt.setBoolean(col++, true);
            stmt.setBoolean(col++, true);
            rs = stmt.executeQuery();
            
            tempResult = new Vector();
            while (rs.next()) {
                ViewSynEntityRelation entRel = new ViewSynEntityRelation();
                entRel.group_uid = rs.getString("GROUP_UID");
                entRel.member_uid = rs.getString("MEMBER_UID");
                entRel.group_ent_id = rs.getLong("ern_ancestor_ent_id");
                entRel.member_ent_id = rs.getLong("ern_child_ent_id");
                tempResult.addElement(entRel);
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        ViewSynEntityRelation result[] = new ViewSynEntityRelation[tempResult.size()];
        result = (ViewSynEntityRelation[])tempResult.toArray(result);
        
        return result;
    }    
}
