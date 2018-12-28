package com.cw.wizbank.ae;

import java.sql.*;
import java.io.*;
import java.util.*;
import java.util.Date;

import com.cw.wizbank.qdb.*;
import com.cw.wizbank.util.*;

public class aeItemRelation {
    
    //Database fields
    public long ire_parent_itm_id;
    public long ire_child_itm_id;
    public Timestamp ire_create_timestamp;
    public String ire_create_usr_id;
    
    
    //SQL Statements
    static final String INS_ITEM_RELATION_SQL = " Insert into aeItemRelation " 
                                              + " (ire_parent_itm_id, ire_child_itm_id, ire_create_timestamp, ire_create_usr_id) "
                                              + " Values "
                                              + " (?, ?, ?, ?) ";
    
    static final String DEL_BY_PARENT_SQL = " Delete From aeItemRelation "
                                          + " Where ire_parent_itm_id = ? ";

    static final String DEL_BY_CHILD_SQL = " Delete From aeItemRelation "
                                          + " Where ire_child_itm_id = ? ";
                                          
    static final String CONFIRMED_BOOKSLOT_SQL = " And (Select count(*) From aeApplication Where itm_id = app_itm_id and (app_status = 'Confirmed' or app_status = 'Pending Cancel')) > 0";

    static final String AVAILABLE_BOOKSLOT_SQL = " And (Select count(*) From aeApplication Where itm_id = app_itm_id and (app_status = 'Confirmed' or app_status = 'Pending Cancel' or app_status = 'Pending' or app_status = 'Waiting')) = 0 ";
    
    static final String PENDING_BOOKSLOT_SQL = " And (Select count(*) From aeApplication Where itm_id = app_itm_id and (app_status = 'Confirmed' or app_status = 'Pending Cancel')) = 0 " +
                                               " And (Select count(*) From aeApplication Where itm_id = app_itm_id and (app_status = 'Pending' or app_status = 'Waiting')) > 0 ";
    
    static final String GET_PARENT_ITM_ID_SQL = " Select ire_parent_itm_id From aeItemRelation "
                                              + " Where ire_child_itm_id = ? ";
    static final String GET_PARENT_ITM_XML_SQL = " Select itm_xml From aeItemRelation, aeItem "
                                               + " Where ire_child_itm_id = ? " 
                                               + " And ire_parent_itm_id = itm_id ";
    static final String GET_CHILD_ITM_ID_SQL = " Select ire_child_itm_id  From aeItemRelation "
                                              + " Where ire_parent_itm_id = ? ";
    static final String GET_ALIVE_CHILD_ITM_ID_SQL = " Select ire_child_itm_id  From aeItemRelation, aeItem "
                                              + " Where ire_parent_itm_id = ? and ire_child_itm_id = itm_id and itm_life_status is null ";

    
    static final String GET_PARENT_ITM_INFO_SQL = " Select itm_title, itm_id, itm_code, itm_type, itm_apply_method " 
                                                + " From aeItemRelation, aeItem "
                                                + " Where ire_child_itm_id = ? " 
                                                + " And ire_parent_itm_id = itm_id ";
    
    static final String GET_CHILD_COUNT_SQL = " Select Count(ire_child_itm_id) From aeItemRelation "
                                            + " Where ire_parent_itm_id = ? Group By ire_parent_itm_id ";

    /**
    Get the parent item info
    @return resultset contain parent item info ( id, title )
    pre-defined variable:
    <ul>
    <li>ire_child_itm_id</li>
    */
    public aeItem getParentInfo(Connection con) 
        throws SQLException {
        int idx = 0;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        aeItem ireParentItm = null;
        
        try {
            idx = 1;
            stmt = con.prepareStatement(GET_PARENT_ITM_INFO_SQL);
            stmt.setLong(idx++, ire_child_itm_id);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                ireParentItm = new aeItem();
                idx = 1;
                ireParentItm.itm_title = rs.getString(idx++);
                ireParentItm.itm_id = rs.getLong(idx++);
				ireParentItm.itm_code = rs.getString(idx++);
                ireParentItm.itm_type = rs.getString(idx++);
                ireParentItm.itm_apply_method = rs.getString(idx++);
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if(rs!=null)rs.close();
        }
        
        return ireParentItm;
    }
    
    
    
    
    
    //get the parent item id from aeItemRelation
    //assume only one parent will be found
    //return 0 if record not found
    public String getParentItemXML(Connection con) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(GET_PARENT_ITM_XML_SQL);
        stmt.setLong(1, ire_child_itm_id);
        ResultSet rs = stmt.executeQuery();
        String pitm_xml;
        if(rs.next())
            pitm_xml = cwSQL.getClobValue(rs, "itm_xml");
        else
            pitm_xml = null;
            
        stmt.close();
        return pitm_xml;
    }
    
    //get the parent item id from aeItemRelation
    //assume only one parent will be found
    //return 0 if record not found
    public long getParentItemId(Connection con) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(GET_PARENT_ITM_ID_SQL);
        stmt.setLong(1, ire_child_itm_id);
        ResultSet rs = stmt.executeQuery();
        if(rs.next())
            ire_parent_itm_id = rs.getLong("ire_parent_itm_id");
        else
            ire_parent_itm_id = 0;
        rs.close();  
        stmt.close();
        return ire_parent_itm_id;
    }
    
    public static Vector getChildItemId(Connection con, long ire_parent_itm_id) 
        throws SQLException {
        return getChildItemId(con, ire_parent_itm_id, false);
    }

    public static Vector getChildItemId(Connection con, long ire_parent_itm_id, boolean checkLifeStatus) 
        throws SQLException {

        Vector itemId = new Vector();
        Long childItemId;

        String sql;
        if (checkLifeStatus){
            sql = GET_ALIVE_CHILD_ITM_ID_SQL;
        }else{
            sql = GET_CHILD_ITM_ID_SQL;   
        }
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setLong(1, ire_parent_itm_id);
        ResultSet rs = stmt.executeQuery();
        while(rs.next()){
            childItemId = new Long(rs.getLong("ire_child_itm_id"));
            itemId.addElement(childItemId);
        }
        rs.close();
        stmt.close();
        return itemId;
    }
    
    
    //ins a record into aeItemRelation
    public void ins(Connection con) throws SQLException {
        try {
            if(ire_create_timestamp == null)
                ire_create_timestamp = dbUtils.getTime(con);
            
            PreparedStatement stmt = con.prepareStatement(INS_ITEM_RELATION_SQL);
            stmt.setLong(1, ire_parent_itm_id);
            stmt.setLong(2, ire_child_itm_id);
            stmt.setTimestamp(3, ire_create_timestamp);
            stmt.setString(4, ire_create_usr_id);
            stmt.executeUpdate();
            stmt.close();
        }
        catch(qdbException e) {
            throw new SQLException(e.getMessage());
        }
    }
    
    public static void delByParent(Connection con, long ire_parent_itm_id) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(DEL_BY_PARENT_SQL);
        stmt.setLong(1, ire_parent_itm_id);
        stmt.executeUpdate();
        stmt.close();
    }

    public static void delByChild(Connection con, long ire_child_itm_id) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(DEL_BY_CHILD_SQL);
        stmt.setLong(1, ire_child_itm_id);
        stmt.executeUpdate();
        stmt.close();
    }
    
    public Timestamp[] fillSlot(Timestamp start_date, Timestamp end_date
                               ,boolean start_inclusive, boolean end_inclusive) {
                                
        int weekBeginDay = Calendar.FRIDAY;
        Calendar c = Calendar.getInstance();
        
        if(!start_inclusive) {
            c.setTime(start_date);
            c.add(Calendar.DAY_OF_MONTH, 1);
            start_date = new Timestamp(c.getTime().getTime());
        }
        if(!end_inclusive) {
            c.setTime(end_date);
            c.add(Calendar.DAY_OF_MONTH, -1);
            end_date = new Timestamp(c.getTime().getTime());
        }
        
        return(aeUtils.getWeekBeginDates(start_date, end_date, weekBeginDay));
                                
    }    
    
    public void getExistingSlot(Connection con, String status, Timestamp start_date, Timestamp end_date
                               ,String child_itm_type, Vector v_slotTimestamp, Vector v_slotStatus) 
      throws SQLException {
        if(status == null || status.length() == 0)
            status = aeItem.SLOT_STATUS_ALL;
        
        //get the slots that fall within the time range
        StringBuffer SQLBuf = new StringBuffer(300);
        SQLBuf.append(" Select ire_child_itm_id, itm_eff_start_datetime, ? status ");
        SQLBuf.append(" From aeItem, aeItemRelation ");
        SQLBuf.append(" Where ire_child_itm_id = itm_id ");
        SQLBuf.append(" And ire_parent_itm_id = ? ");
        SQLBuf.append(" And itm_eff_start_datetime >= ? ");
        SQLBuf.append(" And itm_eff_start_datetime <= ? ");
        if(child_itm_type != null && child_itm_type.length() > 0)
            SQLBuf.append(" And itm_type = ? ");
        
        String Header_SQL = new String(SQLBuf);
        SQLBuf = new StringBuffer(300);
        
        if(status.equals(aeItem.SLOT_STATUS_CONFIRMED))
            SQLBuf.append(Header_SQL).append(CONFIRMED_BOOKSLOT_SQL);

        else if(status.equals(aeItem.SLOT_STATUS_PENDING))
            SQLBuf.append(Header_SQL).append(PENDING_BOOKSLOT_SQL);
/*        
        else if(status.equals(aeItem.SLOT_STATUS_AVAILABLE))
            SQLBuf.append(Header_SQL).append(AVAILABLE_BOOKSLOT_SQL);
*/        
        else if(status.equals(aeItem.SLOT_STATUS_ALL) || status.equals(aeItem.SLOT_STATUS_AVAILABLE)) {
            SQLBuf.append(Header_SQL).append(CONFIRMED_BOOKSLOT_SQL);
            SQLBuf.append(" Union ");
            SQLBuf.append(Header_SQL).append(PENDING_BOOKSLOT_SQL);
            SQLBuf.append(" Union ");
            SQLBuf.append(Header_SQL).append(AVAILABLE_BOOKSLOT_SQL);
        }
        SQLBuf.append(" Order by itm_eff_start_datetime asc ");
        String SQL = new String(SQLBuf);
        PreparedStatement stmt = con.prepareStatement(SQL);
        int index = 1;
        if(status.equals(aeItem.SLOT_STATUS_ALL) || status.equals(aeItem.SLOT_STATUS_AVAILABLE) || status.equals(aeItem.SLOT_STATUS_CONFIRMED)) {
            stmt.setString(index++, aeItem.SLOT_STATUS_CONFIRMED);
            stmt.setLong(index++, ire_parent_itm_id);
            stmt.setTimestamp(index++, start_date);
            stmt.setTimestamp(index++, end_date);
            if(child_itm_type != null && child_itm_type.length() > 0)
                stmt.setString(index++, child_itm_type);
        }
        if(status.equals(aeItem.SLOT_STATUS_ALL) || status.equals(aeItem.SLOT_STATUS_AVAILABLE) || status.equals(aeItem.SLOT_STATUS_PENDING)) {
            stmt.setString(index++, aeItem.SLOT_STATUS_PENDING);
            stmt.setLong(index++, ire_parent_itm_id);
            stmt.setTimestamp(index++, start_date);
            stmt.setTimestamp(index++, end_date);
            if(child_itm_type != null && child_itm_type.length() > 0)
                stmt.setString(index++, child_itm_type);
        }
        if(status.equals(aeItem.SLOT_STATUS_ALL) || status.equals(aeItem.SLOT_STATUS_AVAILABLE)) {
            stmt.setString(index++, aeItem.SLOT_STATUS_AVAILABLE);
            stmt.setLong(index++, ire_parent_itm_id);
            stmt.setTimestamp(index++, start_date);
            stmt.setTimestamp(index++, end_date);
            if(child_itm_type != null && child_itm_type.length() > 0)
                stmt.setString(index++, child_itm_type);
        }
                
        ResultSet rs = stmt.executeQuery();
        while(rs.next()) {
            v_slotTimestamp.addElement(rs.getTimestamp("itm_eff_start_datetime"));
            //get the slot status
            v_slotStatus.addElement(rs.getString("status"));
        }
        stmt.close();
    }        
    
    public void getAllSlot(Connection con, String status, Timestamp start_date, Timestamp end_date
                          ,String child_itm_type, Vector v_slotTimestamp, Vector v_slotStatus) 
      throws SQLException {
        Vector v_existingSlotTimestamp = new Vector();
        Vector v_existingSlotStatus = new Vector();
        
        getExistingSlot(con, status, start_date, end_date, child_itm_type, v_existingSlotTimestamp, v_existingSlotStatus);
        
        if(status.equals(aeItem.SLOT_STATUS_ALL) || status.equals(aeItem.SLOT_STATUS_AVAILABLE)) {
        //need to fill in not existing book slot
            Timestamp[] temp;
            if(v_existingSlotTimestamp.size() == 0) {
            // if there are no existing bookslot, just generate full range timestamps
                temp = fillSlot(start_date, end_date, true, true);
                for(int i=0;i<temp.length;i++) {
                    v_slotTimestamp.addElement(temp[i]);
                    v_slotStatus.addElement(aeItem.SLOT_STATUS_AVAILABLE);
                }
            }
            else
            {
                //generate bookslot between start_end and the 1st existing bookSlot
                Timestamp fillStartDate;
                Timestamp fillEndDate = (Timestamp) v_existingSlotTimestamp.elementAt(0);
                temp = fillSlot(start_date, fillEndDate, true, false);
                for(int i=0;i<temp.length;i++) {
                    v_slotTimestamp.addElement(temp[i]);
                    v_slotStatus.addElement(aeItem.SLOT_STATUS_AVAILABLE);
                }
                
                //generate bookslot between existing bookslots
                for(int i=0;i<v_existingSlotStatus.size()-1;i++) {
                    fillStartDate = (Timestamp) v_existingSlotTimestamp.elementAt(i);
                    fillEndDate = (Timestamp) v_existingSlotTimestamp.elementAt(i+1);
                    temp = fillSlot(fillStartDate, fillEndDate, false, false);
                    if(status.equals(aeItem.SLOT_STATUS_ALL) || ((String) v_existingSlotStatus.elementAt(i)).equals(aeItem.SLOT_STATUS_AVAILABLE)) {
                        v_slotTimestamp.addElement(fillStartDate);
                        v_slotStatus.addElement((String) v_existingSlotStatus.elementAt(i));
                    }
                    for(int j=0;j<temp.length;j++) {
                        v_slotTimestamp.addElement(temp[j]);
                        v_slotStatus.addElement(aeItem.SLOT_STATUS_AVAILABLE);
                    }
                }
                
                //generate bookslot between the last existing bookslot and end_date
                fillStartDate = (Timestamp) v_existingSlotTimestamp.elementAt(v_existingSlotTimestamp.size()-1);
                temp = fillSlot(fillStartDate, end_date, false, true);
                if(status.equals(aeItem.SLOT_STATUS_ALL) || ((String) v_existingSlotStatus.elementAt(v_existingSlotStatus.size()-1)).equals(aeItem.SLOT_STATUS_AVAILABLE)) {
                    v_slotTimestamp.addElement(fillStartDate);
                    v_slotStatus.addElement((String) v_existingSlotStatus.elementAt(v_existingSlotStatus.size()-1));
                }
                for(int i=0;i<temp.length;i++) {
                    v_slotTimestamp.addElement(temp[i]);
                    v_slotStatus.addElement(aeItem.SLOT_STATUS_AVAILABLE);
                }
            }
        }
        else {
            for(int i=0;i<v_existingSlotTimestamp.size();i++) {
                v_slotTimestamp.addElement((Timestamp) v_existingSlotTimestamp.elementAt(i));
                v_slotStatus.addElement((String) v_existingSlotStatus.elementAt(i));
            }
        }
        return;        
    }    
    
    
    
    
    public long getChildCount(Connection con)
        throws SQLException {
            
            PreparedStatement stmt = con.prepareStatement(GET_CHILD_COUNT_SQL);
            stmt.setLong(1, ire_parent_itm_id);
            ResultSet rs = stmt.executeQuery();
            long count = 0;
            if( rs.next() )
                count = rs.getLong(1);
            stmt.close();
            return count;
        }
    
    /**
     * @tableName contains all courses specified by user
     */
    public static List getRunnableItemId(Connection conn,String tableName,long rootId) throws SQLException {
        List runnableItem = new Vector();
        StringBuffer sql = new StringBuffer();
        if(tableName != null){
            sql.append("select itm_id from aeItem where itm_id in ( select * from ").append(tableName).append(" ) AND itm_run_ind = 0");
        }
        else{
        	sql.append("select itm_id from aeItem where itm_run_ind = 0");
        }
        sql.append(" and itm_owner_ent_id = ?");
		PreparedStatement stmt = conn.prepareStatement(sql.toString());
		stmt.setLong(1,rootId);
		ResultSet rs = stmt.executeQuery();
		while(rs.next()){
			runnableItem.add(new Long(rs.getLong("itm_id")));
		}
		stmt.close();
		return runnableItem;
    }
    /**
     * @tableName contains all courses specified by user
     */
    public static List getAllRunnableItemId(Connection conn,String tableName,long rootId) throws SQLException {
        List runnableItem = new Vector();
        StringBuffer sql = new StringBuffer();
        if(tableName != null){
            sql.append(" select itm_id from aeItem where itm_id in ( select * from ").append(tableName).append(" ) AND itm_run_ind = 0 ")
               .append(" and itm_owner_ent_id = ? ")
               .append(" Union select itm_id from aeItem, aeItemRelation where ire_child_itm_id in ( select * from ")
               .append(tableName).append(" ) AND ire_parent_itm_id = itm_id AND  itm_run_ind = 0 ");
        }
        else{
        	sql.append("select itm_id from aeItem where itm_run_ind = 0 ");
        }
        sql.append(" and itm_owner_ent_id = ? ");
		PreparedStatement stmt = conn.prepareStatement(sql.toString());
		int index = 1;
		if(tableName != null){
			stmt.setLong(index++,rootId);			
		}
		stmt.setLong(index++,rootId);
		ResultSet rs = stmt.executeQuery();
		while(rs.next()){
			runnableItem.add(new Long(rs.getLong("itm_id")));
		}
		stmt.close();
		return runnableItem;
    }
    public Hashtable getParentNChildId(Connection con, Vector v_child_id)
        throws SQLException {
            
            Hashtable h_item_id = new Hashtable();
            if( v_child_id == null || v_child_id.isEmpty() )
                return h_item_id;

            String SQL = " SELECT ire_parent_itm_id, ire_child_itm_id "
                       + " FROM aeItemRelation "
                       + " WHERE ire_child_itm_id IN " + cwUtils.vector2list(v_child_id);
            PreparedStatement stmt = con.prepareStatement(SQL);
            ResultSet rs = stmt.executeQuery();
            while(rs.next())
                h_item_id.put(new Long(rs.getLong("ire_child_itm_id")), new Long(rs.getLong("ire_parent_itm_id")));
            stmt.close();
            return h_item_id;
    } 
    public static Vector getItemIdByParent(Connection con, Vector v_parent_id) throws SQLException  {
        Vector h_item_id = new Vector();
        if( v_parent_id == null || v_parent_id.isEmpty() )
            return h_item_id;

        String pItmTableName = null;
    	String pItmIdColName = "p_itm_ids";
    	
    	pItmTableName = cwSQL.createSimpleTemptable(con, pItmIdColName, cwSQL.COL_TYPE_LONG, 0);
		if(pItmTableName != null) {
			cwSQL.insertSimpleTempTable(con, pItmTableName, v_parent_id, cwSQL.COL_TYPE_LONG);
		}
		
        String SQL = " SELECT ire_child_itm_id "
                   + " FROM aeItemRelation "
                   + " WHERE ire_parent_itm_id in (select "+pItmIdColName+" from "+pItmTableName+")";
        PreparedStatement stmt = con.prepareStatement(SQL);
        ResultSet rs = stmt.executeQuery();
        while(rs.next()){
        	h_item_id.add(new Long(rs.getLong("ire_child_itm_id")));
        }
        stmt.close();
		if (pItmTableName != null) {
			cwSQL.dropTempTable(con, pItmTableName);
		}
        return h_item_id;
        }    
    
    //return course id list
    public static List getParentIdByModId(Connection con, List modIds) throws SQLException {
    	List itmIds = new ArrayList();
    	if (modIds == null || modIds.size() == 0) {
    		return itmIds;
    	}
    	String modTableName = null;
    	String modIdColName = "mod_ids";
    	
    	modTableName = cwSQL.createSimpleTemptable(con, modIdColName, cwSQL.COL_TYPE_LONG, 0);
		if(modTableName != null) {
			cwSQL.insertSimpleTempTable(con, modTableName, modIds, cwSQL.COL_TYPE_STRING);
		}
    	String sql = " SELECT ire_parent_itm_id, cos_itm_id FROM resourcecontent "
    			   + " INNER JOIN Course ON (cos_res_id = rcn_res_id) "
    			   + " LEFT JOIN aeItemRelation ON (ire_child_itm_id = cos_itm_id) "
    			   + " WHERE EXISTS (SELECT " + modIdColName + " FROM " + modTableName + " WHERE " + modIdColName + " = rcn_res_id_content) ";
    	PreparedStatement stmt = null;
    	ResultSet rs = null;
    	long itm_id = 0;
    	Long itmIdObj = null;
    	try {
    		stmt = con.prepareStatement(sql);
    		rs = stmt.executeQuery();
    	    while (rs.next()) {
    	    	itm_id = rs.getLong("ire_parent_itm_id");
    	    	if (itm_id == 0) {
    	    		itm_id = rs.getLong("cos_itm_id"); 
    	    	}
    	    	itmIdObj = new Long(itm_id);
    	    	if (itmIds.indexOf(itmIdObj) == -1) {
    	    		itmIds.add(new Long(itm_id));
    	    	}
    	    }
    	} finally {
    		if(modTableName != null){
    			cwSQL.dropTempTable(con, modTableName);
            }
    		cwSQL.cleanUp(rs, stmt);
    	}
    	return itmIds;
    }
}