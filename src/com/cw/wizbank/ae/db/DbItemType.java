package com.cw.wizbank.ae.db;

import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.Hashtable;
import java.sql.*;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.ae.aeItemDummyType;
import com.cw.wizbank.ae.db.sql.SqlStatements;


/**
Database layer for table aeItemType
*/
public class DbItemType{

    /**
    database field
    */
    public long ity_owner_ent_id;

    /**
    database field
    */
    public String ity_id;

    /**
    database field
    */
    public boolean ity_run_ind = false;

    /**
    database field
    */
    public boolean ity_session_ind = false;

    /**
    database field
    */
    public String ity_init_life_status;

    /**
    database field
    */
    public long ity_seq_id;
    
    /**
    database field
    */
    public String ity_title_xml;
    
    /**
    database field
    */
    public String ity_icon_url;

    /**
    database field
    */
    public Timestamp ity_create_timestamp;

    /**
    database field
    */
    public String ity_create_usr_id;

    /**
    database field
    */
    public boolean ity_create_run_ind;
    /**
    database field
    */
    public boolean ity_create_session_ind;
    /**
    database field
    */
    public boolean ity_has_attendance_ind;
    /**
    database field
    */
    public boolean ity_ji_ind;
    /**
    database field
    */
    public boolean ity_completion_criteria_ind;
    
    /**
    database field
    */
    public boolean ity_apply_ind;

    /**
    database field
    */
    public boolean ity_qdb_ind;

    /**
    database field
    */
    public boolean ity_auto_enrol_qdb_ind;

     /**
    database field
    */
    public boolean ity_can_cancel_ind;
    
     /**
    database field
    */
    // =============== public boolean ity_iad_ind;
    
    public boolean ity_reminder_criteria_ind;
    
    public boolean ity_exam_ind;
    
    public boolean ity_integ_ind;
    
    public boolean ity_ref_ind;
    
    public boolean ity_blend_ind;
    
    public String ity_dummy_type;
    
    public String ity_tkh_method;

	public String item_type;
    /**
    Get item type from database<BR>
    Pre-define variables:
    <ul>
    <li>ity_owner_ent_id
    <li>ity_id
    <li>ity_run_ind
    </ul>
    */
    public void getItemType(Connection con) 
        throws SQLException {
        
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_itemType);
        int index = 1;
        stmt.setLong(index++, this.ity_owner_ent_id);
        stmt.setString(index++, this.ity_id);
        stmt.setBoolean(index++, this.ity_run_ind);
        stmt.setBoolean(index++, this.ity_session_ind);
        stmt.setBoolean(index++, this.ity_blend_ind);
        stmt.setBoolean(index++, this.ity_exam_ind);
        stmt.setBoolean(index++, this.ity_ref_ind);

        ResultSet rs = stmt.executeQuery();
        
        //put all the ItemTypes into Vector
        if(rs.next()) {
            this.ity_owner_ent_id = rs.getLong("ity_owner_ent_id");
            this.ity_id = rs.getString("ity_id");
            this.ity_seq_id = rs.getLong("ity_seq_id");
            this.ity_icon_url = rs.getString("ity_icon_url");
            this.ity_create_timestamp = rs.getTimestamp("ity_create_timestamp");
            this.ity_create_usr_id = rs.getString("ity_create_usr_id");
            this.ity_init_life_status = rs.getString("ity_init_life_status");
            this.ity_create_run_ind = rs.getBoolean("ity_create_run_ind");
            this.ity_create_session_ind = rs.getBoolean("ity_create_session_ind");
            this.ity_has_attendance_ind = rs.getBoolean("ity_has_attendance_ind");
            this.ity_blend_ind = rs.getBoolean("ity_blend_ind");
            this.ity_exam_ind = rs.getBoolean("ity_exam_ind");
            this.ity_ref_ind = rs.getBoolean("ity_ref_ind");
            this.ity_ji_ind = rs.getBoolean("ity_ji_ind");
            this.ity_completion_criteria_ind = rs.getBoolean("ity_completion_criteria_ind");
            this.ity_apply_ind = rs.getBoolean("ity_apply_ind");
            this.ity_qdb_ind = rs.getBoolean("ity_qdb_ind");
            this.ity_auto_enrol_qdb_ind = rs.getBoolean("ity_auto_enrol_qdb_ind");
//=============            this.ity_iad_ind = rs.getBoolean("ity_iad_ind");
            this.ity_reminder_criteria_ind = rs.getBoolean("ity_reminder_criteria_ind");
            this.ity_tkh_method = rs.getString("ity_tkh_method");
            this.ity_title_xml = getItemTypeXml(ity_id, this);
        }
        stmt.close();
        return;
    }

    private static HashMap AllItemType = new HashMap();
    /**
    Get all the item types in the input organization, ity_run_ind = false and ity_session_ind = false
    @return an array of DbItemType 
    */
    public static DbItemType[] getAllItemTypeInOrg(Connection con, long owner_ent_id) 
        throws SQLException {
        if (AllItemType.get(new Long(owner_ent_id))==null){
            Vector v_dbItys= new Vector();
            DbItemType[] dbItys;
            DbItemType dbIty;
            PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_all_itemType_in_org);
            stmt.setLong(1, owner_ent_id);
            stmt.setBoolean(2, false);
            stmt.setBoolean(3, false);
            ResultSet rs = stmt.executeQuery();
            
            //put all the ItemTypes into Vector
            while(rs.next()) {
                dbIty = new DbItemType();
                dbIty.ity_owner_ent_id = rs.getLong("ity_owner_ent_id");
                dbIty.ity_id = rs.getString("ity_id");
                dbIty.ity_seq_id = rs.getLong("ity_seq_id");
                dbIty.ity_icon_url = rs.getString("ity_icon_url");
                dbIty.ity_create_timestamp = rs.getTimestamp("ity_create_timestamp");
                dbIty.ity_create_usr_id = rs.getString("ity_create_usr_id");
                dbIty.ity_init_life_status = rs.getString("ity_init_life_status");
                dbIty.ity_create_run_ind = rs.getBoolean("ity_create_run_ind");
                dbIty.ity_create_session_ind = rs.getBoolean("ity_create_session_ind");
                dbIty.ity_blend_ind = rs.getBoolean("ity_blend_ind");
                dbIty.ity_exam_ind = rs.getBoolean("ity_exam_ind");
                dbIty.ity_ref_ind = rs.getBoolean("ity_ref_ind");
                dbIty.ity_has_attendance_ind = rs.getBoolean("ity_has_attendance_ind");
                dbIty.ity_ji_ind = rs.getBoolean("ity_ji_ind");
                dbIty.ity_completion_criteria_ind = rs.getBoolean("ity_completion_criteria_ind");
                dbIty.ity_apply_ind = rs.getBoolean("ity_apply_ind");
                dbIty.ity_qdb_ind = rs.getBoolean("ity_qdb_ind");
                dbIty.ity_auto_enrol_qdb_ind = rs.getBoolean("ity_auto_enrol_qdb_ind");
    //==================            dbIty.ity_iad_ind = rs.getBoolean("ity_iad_ind");
                dbIty.ity_reminder_criteria_ind = rs.getBoolean("ity_reminder_criteria_ind");
                dbIty.ity_tkh_method = rs.getString("ity_tkh_method");
                dbIty.ity_title_xml = getItemTypeXml(dbIty.ity_id, dbIty);
                dbIty.ity_run_ind = false;
                dbIty.ity_session_ind = false;
                
                dbIty.ity_exam_ind =rs.getBoolean("ity_exam_ind");
                dbIty.ity_integ_ind =rs.getBoolean("ity_integ_ind");
                dbIty.ity_blend_ind = rs.getBoolean("ity_blend_ind");
                dbIty.ity_ref_ind = rs.getBoolean("ity_ref_ind");
                dbIty.item_type=aeItemDummyType.getDummyItemType(dbIty.ity_id, dbIty.ity_blend_ind, dbIty.ity_exam_ind, dbIty.ity_ref_ind);
                v_dbItys.addElement(dbIty);
            }
            stmt.close();
            
            //convert the resulting Vector into DbItemType[]
            dbItys = new DbItemType[v_dbItys.size()];
            for(int i=0; i<v_dbItys.size(); i++) {
                dbItys[i] = (DbItemType) v_dbItys.elementAt(i);
            }
            AllItemType.put(new Long(owner_ent_id), dbItys);
            return dbItys;
        } else {
            return (DbItemType[]) AllItemType.get(new Long(owner_ent_id));
        }
    }

    /**
    Get all the item types in the input organization, and ity_run_ind = false and ity_session_ind = false
    @return an array of ity_id
    */
    public static String[] getAllItemTypeIdInOrg(Connection con, long owner_ent_id) 
        throws SQLException {
        
        Vector v_ity_id= new Vector();
        String[] ity_ids;
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_all_itemType_in_org);
        stmt.setLong(1, owner_ent_id);
        stmt.setBoolean(2, false);
        stmt.setBoolean(3, false);

        ResultSet rs = stmt.executeQuery();
        
        //put all the ItemTypes into Vector
        while(rs.next()) {
            v_ity_id.addElement(rs.getString("ity_id"));
        }
        stmt.close();
        
        //convert the resulting Vector into DbItemType[]
        ity_ids = new String[v_ity_id.size()];
        for(int i=0; i<v_ity_id.size(); i++) {
            ity_ids[i] = (String) v_ity_id.elementAt(i);
        }
        return ity_ids;
    }

    /**
    Get ity_id by ity_seq_id and ity_owner_ent_id<BR>
    Pre-define variables:
    <ul>
    <li>ity_owner_ent_id</li>
    <li>ity_seq_id</li>
    </ul>
    */
    public String getItemTypeIdBySeqId(Connection con) throws SQLException, cwSysMessage {
        
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_itemTypeId_by_seqId);
        stmt.setLong(1, this.ity_seq_id);
        stmt.setLong(2, this.ity_owner_ent_id);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()) {
            this.ity_id = rs.getString("ity_id");
            stmt.close();
        }
        else {
            throw new cwSysMessage("GEN005", "Item Type Sequence Id = " + this.ity_seq_id);
        }
        return this.ity_id;
    }

    /**
    Get ity_init_life_status by ity_id and ity_owner_ent_id<BR>
    Pre-define variables:
    <ul>
    <li>ity_owner_ent_id</li>
    <li>ity_id</li>
    <li>ity_run_ind</li>
    <li>ity_session_ind</li>
    </ul>
    */
    public String getItemTypeInitLifeStatus(Connection con) throws SQLException, cwSysMessage {
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_ity_init_life_status);
        stmt.setString(1, this.ity_id);
        stmt.setLong(2, this.ity_owner_ent_id);
        stmt.setBoolean(3, this.ity_run_ind);
        stmt.setBoolean(4, this.ity_session_ind);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()) {
            this.ity_init_life_status = rs.getString("ity_init_life_status");
            stmt.close();
        }
        else {
            throw new cwSysMessage("GEN005", "Item Type Not Found:" + this.ity_id);
        }
        return this.ity_init_life_status;
    }

    /**
    Only format XML, you need to get data from database from outside 1st
    */
    public String getMetaDataAsXML() {
        StringBuffer xmlBuf = new StringBuffer(512);
        this.item_type=aeItemDummyType.getDummyItemType(this.ity_id, this.ity_blend_ind, this.ity_exam_ind, this.ity_ref_ind);
        
        xmlBuf.append("<item_type_meta id=\"").append(this.ity_id).append("\"")
        	  .append(" dummy_type=\"").append(this.item_type).append("\"")
              .append(" run_ind=\"").append(this.ity_run_ind).append("\"")
              .append(" session_ind=\"").append(this.ity_session_ind).append("\"")
              .append(" create_run_ind=\"").append(this.ity_create_run_ind).append("\"")
              .append(" create_session_ind=\"").append(this.ity_create_session_ind).append("\"")
              .append(" blend_ind=\"").append(this.ity_blend_ind).append("\"")
              .append(" exam_ind=\"").append(this.ity_exam_ind).append("\"")
              .append(" ref_ind=\"").append(this.ity_ref_ind).append("\"")
              .append(" has_attendance_ind=\"").append(this.ity_has_attendance_ind).append("\"")
              .append(" ji_ind=\"").append(this.ity_ji_ind).append("\"")
              .append(" completion_criteria_ind=\"").append(this.ity_completion_criteria_ind).append("\"")
              .append(" apply_ind=\"").append(this.ity_apply_ind).append("\"")
              .append(" qdb_ind=\"").append(this.ity_qdb_ind).append("\"")
              .append(" auto_enrol_qdb_ind=\"").append(this.ity_auto_enrol_qdb_ind).append("\"")
//=====================              .append(" iad_ind=\"").append(this.ity_iad_ind).append("\"")
              .append(" reminder_criteria_ind=\"").append(this.ity_reminder_criteria_ind).append("\"")
              .append(" tkh_method=\"").append(this.ity_tkh_method).append("\"")
              .append(" init_life_status=\"").append(cwUtils.escNull(this.ity_init_life_status)).append("\"/>");
        return xmlBuf.toString();
    }

    public static DbItemType[] getHasQdbItemTypeInOrg(Connection con, long owner_ent_id) throws SQLException {
        Vector v_dbItys= new Vector();
        DbItemType[] dbItys;
        DbItemType dbIty;
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_has_qdb_itemType_in_org);
        stmt.setLong(1, owner_ent_id);
        stmt.setBoolean(2, true);
        stmt.setBoolean(3, false);
        ResultSet rs = stmt.executeQuery();
        
        //put all the ItemTypes into Vector
        while(rs.next()) {
            dbIty = new DbItemType();
            dbIty.ity_owner_ent_id = rs.getLong("ity_owner_ent_id");
            dbIty.ity_id = rs.getString("ity_id");
            dbIty.ity_seq_id = rs.getLong("ity_seq_id");
            dbIty.ity_icon_url = rs.getString("ity_icon_url");
            dbIty.ity_create_timestamp = rs.getTimestamp("ity_create_timestamp");
            dbIty.ity_create_usr_id = rs.getString("ity_create_usr_id");
            dbIty.ity_create_run_ind = rs.getBoolean("ity_create_run_ind");
            dbIty.ity_blend_ind = rs.getBoolean("ity_blend_ind");
            dbIty.ity_exam_ind = rs.getBoolean("ity_exam_ind");
            dbIty.ity_ref_ind = rs.getBoolean("ity_ref_ind");
            dbIty.ity_apply_ind = rs.getBoolean("ity_apply_ind");
            dbIty.ity_qdb_ind = rs.getBoolean("ity_qdb_ind");
            dbIty.ity_auto_enrol_qdb_ind = rs.getBoolean("ity_auto_enrol_qdb_ind");
//=========================            dbIty.ity_iad_ind = rs.getBoolean("ity_iad_ind");
            dbIty.ity_reminder_criteria_ind = rs.getBoolean("ity_reminder_criteria_ind");
            dbIty.ity_tkh_method = rs.getString("ity_tkh_method");
            dbIty.ity_title_xml = getItemTypeXml(dbIty.ity_id, dbIty);
            dbIty.ity_run_ind = false;
            dbIty.ity_init_life_status = rs.getString("ity_init_life_status");
            v_dbItys.addElement(dbIty);
        }
        stmt.close();
        
        //convert the resulting Vector into DbItemType[]
        dbItys = new DbItemType[v_dbItys.size()];
        for(int i=0; i<v_dbItys.size(); i++) {
            dbItys[i] = (DbItemType) v_dbItys.elementAt(i);
        }
        return dbItys;
    }

    public static DbItemType[] getApplicableItemTypeInOrg(Connection con, long owner_ent_id) throws SQLException {
        Vector v_dbItys= new Vector();
        DbItemType[] dbItys;
        DbItemType dbIty;
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_applicable_itemType_in_org);
        stmt.setLong(1, owner_ent_id);
        stmt.setBoolean(2, true);
        stmt.setBoolean(3, false);
        stmt.setBoolean(4, false);
        stmt.setBoolean(5, true);
        stmt.setBoolean(6, true);
        stmt.setBoolean(7, true);
        ResultSet rs = stmt.executeQuery();
        
        //put all the ItemTypes into Vector
        while(rs.next()) {
            dbIty = new DbItemType();
            dbIty.ity_owner_ent_id = rs.getLong("ity_owner_ent_id");
            dbIty.ity_id = rs.getString("ity_id");
            dbIty.ity_seq_id = rs.getLong("ity_seq_id");
            dbIty.ity_icon_url = rs.getString("ity_icon_url");
            dbIty.ity_create_timestamp = rs.getTimestamp("ity_create_timestamp");
            dbIty.ity_create_usr_id = rs.getString("ity_create_usr_id");
            dbIty.ity_init_life_status = rs.getString("ity_init_life_status");
            dbIty.ity_create_run_ind = rs.getBoolean("ity_create_run_ind");
            dbIty.ity_apply_ind = rs.getBoolean("ity_apply_ind");
            dbIty.ity_qdb_ind = rs.getBoolean("ity_qdb_ind");
            dbIty.ity_auto_enrol_qdb_ind = rs.getBoolean("ity_auto_enrol_qdb_ind");
//======================            dbIty.ity_iad_ind = rs.getBoolean("ity_iad_ind");
            dbIty.ity_reminder_criteria_ind = rs.getBoolean("ity_reminder_criteria_ind");
            dbIty.ity_tkh_method = rs.getString("ity_tkh_method");
            dbIty.ity_title_xml = getItemTypeXml(dbIty.ity_id, dbIty);
            dbIty.ity_run_ind = false;
            v_dbItys.addElement(dbIty);
        }
        stmt.close();
        
        //convert the resulting Vector into DbItemType[]
        dbItys = new DbItemType[v_dbItys.size()];
        for(int i=0; i<v_dbItys.size(); i++) {
            dbItys[i] = (DbItemType) v_dbItys.elementAt(i);
        }
        return dbItys;
    }
// kim:todo
    /**
    Get the applicable item type of organization
    */
    private static HashMap APPLICABLE_ITEM_TYPE_CACHED = new HashMap();
    public static Vector getApplicableItemType(Connection con, long owner_ent_id) throws SQLException {
        if (APPLICABLE_ITEM_TYPE_CACHED.get(new Long(owner_ent_id)) == null) {
            PreparedStatement stmt = null;
            Vector v = new Vector();
            try {
                stmt = con.prepareStatement(SqlStatements.SQL_GET_APPLICABLE_ITY);
                stmt.setLong(1, owner_ent_id);
                stmt.setBoolean(2, true);
                ResultSet rs = stmt.executeQuery();
                while(rs.next()) {
                    v.addElement(rs.getString("ity_id"));
                }
            } finally {
                if(stmt!=null) stmt.close();
            }
            APPLICABLE_ITEM_TYPE_CACHED.put(new Long(owner_ent_id), v);
            return v;
        } else {
            return (Vector) APPLICABLE_ITEM_TYPE_CACHED.get(new Long(owner_ent_id));
        }
    }

    /**
    Get indicators from aeItemType<BR>
    Pre-define variables:
    <ul>
    <li>ity_id
    <li>ity_run_ind
    <li>ity_session_ind
    <li>ity_owner_ent_id
    </ul>
    */
    public Hashtable getInd(Connection con) throws SQLException {
        Hashtable h_ind = new Hashtable();
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_ity_ind);
        stmt.setLong(1, this.ity_owner_ent_id);
        stmt.setString(2, this.ity_id);
        stmt.setBoolean(3, this.ity_run_ind);
        stmt.setBoolean(4, this.ity_session_ind);
        stmt.setBoolean(5, this.ity_blend_ind);
        stmt.setBoolean(6, this.ity_exam_ind);
        stmt.setBoolean(7, this.ity_ref_ind);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()) {
            h_ind.put("ity_create_run_ind", new Boolean(rs.getBoolean("ity_create_run_ind")));
            h_ind.put("ity_create_session_ind", new Boolean(rs.getBoolean("ity_create_session_ind")));
            h_ind.put("ity_has_attendance_ind", new Boolean(rs.getBoolean("ity_has_attendance_ind")));
            h_ind.put("ity_ji_ind", new Boolean(rs.getBoolean("ity_ji_ind")));
            h_ind.put("ity_completion_criteria_ind", new Boolean(rs.getBoolean("ity_completion_criteria_ind")));
            h_ind.put("ity_apply_ind", new Boolean(rs.getBoolean("ity_apply_ind")));
            h_ind.put("ity_qdb_ind", new Boolean(rs.getBoolean("ity_qdb_ind")));
            h_ind.put("ity_auto_enrol_qdb_ind", new Boolean(rs.getBoolean("ity_auto_enrol_qdb_ind")));
//=====================            h_ind.put("ity_iad_ind", new Boolean(rs.getBoolean("ity_iad_ind")));
            h_ind.put("ity_reminder_criteria_ind", new Boolean(rs.getBoolean("ity_reminder_criteria_ind")));
            h_ind.put("ity_run_ind", new Boolean(this.ity_run_ind));
            h_ind.put("ity_session_ind", new Boolean(this.ity_session_ind));
            h_ind.put("ity_blend_ind", new Boolean(this.ity_blend_ind));
            h_ind.put("ity_exam_ind", new Boolean(this.ity_exam_ind));
            h_ind.put("ity_ref_ind", new Boolean(this.ity_ref_ind));
            h_ind.put("ity_can_cancel_ind", new Boolean(rs.getBoolean("ity_can_cancel_ind")));
        }
        rs.close();
        stmt.close();
        return h_ind;
    }

    public static String getItemTypeXml(String item_type, DbItemType ity) {
    	String ity_xml = "<item_type id=\"" + cwUtils.escNull(item_type) +"\"" + " dummy_type=\"" + aeItemDummyType.getDummyItemType(item_type, ity.ity_blend_ind, ity.ity_exam_ind, ity.ity_ref_ind) + "\"/>";
    	return ity_xml;
    }
    public static String getItemTypeXml(String item_type, boolean blend_ind, boolean exam_ind, boolean ref_ind) {
    	String ity_xml = "<item_type id=\"" + cwUtils.escNull(item_type) +"\"" + " dummy_type=\"" + aeItemDummyType.getDummyItemType(item_type, blend_ind, exam_ind, ref_ind) + "\"/>";
    	return ity_xml;
    }
    
    public void setItyTypeByDummyType(String dummytype) {
    	if (dummytype == null || dummytype.length() == 0){
    		return;
    	}
    	if (dummytype.indexOf(aeItemDummyType.ITEM_DUMMY_TYPE_DELIMITER) >= 0) {
    		StringTokenizer token = new StringTokenizer(dummytype, aeItemDummyType.ITEM_DUMMY_TYPE_DELIMITER);
    		int count = 0;
    		while (token.hasMoreTokens()) {
    			String tmp = token.nextToken();
    			if (count == 0) {
    				if (tmp.indexOf(" ") == -1) {
       					this.ity_id = tmp;
    				}
    			} else {
    				if (aeItemDummyType.ITEM_DUMMY_TYPE_BLEND.equalsIgnoreCase(tmp)) {
    					this.ity_blend_ind = true;
    				} else if (aeItemDummyType.ITEM_DUMMY_TYPE_EXAM.equalsIgnoreCase(tmp)) {
    					this.ity_exam_ind = true;
    				} else if (aeItemDummyType.ITEM_DUMMY_TYPE_REF.equalsIgnoreCase(tmp)) {
    					this.ity_ref_ind = true;
    				}
    			}
    			count++;
    		}
    	} else {
			if (aeItemDummyType.ITEM_DUMMY_TYPE_COS.equalsIgnoreCase(dummytype)) {
				this.ity_exam_ind = false;
				this.ity_ref_ind = false;
			} else if (aeItemDummyType.ITEM_DUMMY_TYPE_EXAM.equalsIgnoreCase(dummytype)) {
				this.ity_exam_ind = true;
				this.ity_ref_ind = false;
			} else if (aeItemDummyType.ITEM_DUMMY_TYPE_REF.equalsIgnoreCase(dummytype)) {
				this.ity_exam_ind = false;
				this.ity_ref_ind = true;
			}
    	}
    }
}