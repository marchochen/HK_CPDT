package com.cw.wizbank.db;

import java.sql.*;
import java.util.*;

import com.cw.wizbank.util.*;

/**
A database class to manage table kmObject and kmObjectHistory
*/
public class DbKmObject extends DbKmNode{

    public static final String OBJ_STATUS_CHECKED_IN = "CHECKED_IN";
    public static final String OBJ_STATUS_CHECKED_OUT = "CHECKED_OUT";
    public static final String OBJ_STATUS_DELETE = "DELETED";
    

    public long         obj_bob_nod_id;
    public String       obj_version;
    public boolean      obj_publish_ind;
    public boolean      obj_latest_ind;
    public String       obj_type;
    public String       obj_title;
    public String       obj_desc;
    public String       obj_status;
    public String       obj_keywords;
    public String       obj_xml;
    public String       obj_comment;
    public String       obj_author;
    public String       obj_update_usr_id;
    public Timestamp    obj_update_timestamp;

    // from BaseObject
    public String       obj_nature;
    public String       obj_code;
    public String       obj_delete_usr_id;
    public Timestamp    obj_delete_timestamp;
    
    //used only when this DbKmObject is representing a kmLink
    public long         target_nod_id;
        
    //non-db column
    public String       obj_update_usr_display_bil;
    
    /**
    Set the attributes of this object
    @param vObjColName Vector of kmObject column names 
    @param vObjColValue Vector of kmObject column values
    @param vObjClobColName Vector of kmObject Clob column names
    @param vObjClobColValue Vector of kmObject Clob column values
    */
    private void setAll(Vector vObjColName, Vector vObjColValue, Vector vObjClobColName, Vector vObjClobColValue)  {
        
        int index = 0;

        index = vObjColName.indexOf("obj_bob_nod_id");
        if(index > -1) {
            this.obj_bob_nod_id = ((Long)vObjColValue.elementAt(index)).longValue();
        }

        index = vObjColName.indexOf("obj_version");
        if(index > -1) {
            this.obj_version = ((String)vObjColValue.elementAt(index));
        }
        
        index = vObjColName.indexOf("obj_publish_ind");
        if(index > -1) {
            this.obj_publish_ind = ((Boolean)vObjColValue.elementAt(index)).booleanValue();
        }

        index = vObjColName.indexOf("obj_latest_ind");
        if(index > -1) {
            this.obj_latest_ind = ((Boolean)vObjColValue.elementAt(index)).booleanValue();
        }

        index = vObjColName.indexOf("obj_type");
        if(index > -1) {
            this.obj_type = ((String)vObjColValue.elementAt(index));
        }

        index = vObjColName.indexOf("obj_title");
        if(index > -1) {
            this.obj_title = ((String)vObjColValue.elementAt(index));
        }

        index = vObjColName.indexOf("obj_desc");
        if(index > -1) {
            this.obj_desc = ((String)vObjColValue.elementAt(index));
        }

        index = vObjColName.indexOf("obj_status");
        if(index > -1) {
            this.obj_status = ((String)vObjColValue.elementAt(index));
        }

        index = vObjColName.indexOf("obj_keywords");
        if(index > -1) {
            this.obj_keywords = ((String)vObjColValue.elementAt(index));
        }

        index = vObjColName.indexOf("obj_update_usr_id");
        if(index > -1) {
            this.obj_update_usr_id = ((String)vObjColValue.elementAt(index));
        }

        index = vObjColName.indexOf("obj_update_timestamp");
        if(index > -1) {
            this.obj_update_timestamp= ((Timestamp)vObjColValue.elementAt(index));
        }

        index = vObjColName.indexOf("obj_comment");
        if(index > -1) {
            this.obj_comment= ((String)vObjColValue.elementAt(index));
        }

        index = vObjColName.indexOf("obj_author");
        if(index > -1) {
            this.obj_author = ((String)vObjColValue.elementAt(index));
        }

        index = vObjClobColName.indexOf("obj_xml");
        if(index > -1) {
            this.obj_xml = ((String)vObjClobColValue.elementAt(index));
        }

        return;
    }

    /**
    Insert the input arguments into database as kmObject and kmNode
    @param con Connection to database
    @param vNodColName Vector of kmNode column names 
    @param vNodColType Vector of kmNode column types
    @param vNodColValue Vector of kmNode column values
    @param vObjColName Vector of kmObject column names
    @param vObjColType Vector of kmObject column types
    @param vObjColValue Vector of kmObject column values
    @param vObjClobColName Vector of kmObject Clob column names
    @param vObjClobColValue Vector of kmObject Clob column values
    @return number of rows updates
    */
    public int ins(Connection con, 
                   Vector vNodColName, Vector vNodColType, Vector vNodColValue,
                   Vector vObjColName, Vector vObjColType, Vector vObjColValue,
                   Vector vObjClobColName, Vector vObjClobColValue, String bob_code) 
                   throws SQLException, cwSysMessage {

        //set database column values
        Timestamp curTime = null;
        if(vNodColName.indexOf("nod_type") < 0) {
            vNodColName.addElement("nod_type");
            vNodColType.addElement(DbTable.COL_TYPE_STRING);
            vNodColValue.addElement(NODE_TYPE_OBJECT);
        }
        if(vNodColName.indexOf("nod_create_timestamp") < 0) {
            if(curTime == null) {
                curTime = cwSQL.getTime(con);
            }
            vNodColName.addElement("nod_create_timestamp");
            vNodColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
            vNodColValue.addElement(curTime);
        }
        if(vObjColName.indexOf("obj_update_timestamp") < 0) {
            if(curTime == null) {
                curTime = cwSQL.getTime(con);
            }
            vObjColName.addElement("obj_update_timestamp");
            vObjColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
            vObjColValue.addElement(curTime);
        }

        //insert into kmNode
        int row = super.ins(con, vNodColName, vNodColType, vNodColValue);
        int index = vObjColName.indexOf("obj_type");
        if(index > -1) {
            this.obj_type = ((String)vObjColValue.elementAt(index));
        }
                
        //set obj_bob_nod_id
        vObjColName.addElement("obj_bob_nod_id");
        vObjColType.addElement(DbTable.COL_TYPE_LONG);
        vObjColValue.addElement(new Long(this.nod_id));

         //insert into kmBaseObject
        DbKmBaseObject baseObj = new DbKmBaseObject();
        baseObj.bob_nod_id = this.nod_id;
        baseObj.bob_nature = DbKmObjectType.getNature(con, this.obj_type);
        baseObj.bob_code = bob_code;
        baseObj.ins(con);          
        
        this.obj_bob_nod_id = nod_id;

        //insert into kmObject
        DbTable dbTab = new DbTable(con);
        dbTab.ins("kmObject", vObjColName, vObjColType, vObjColValue);
        
        // for other clob columns
        if (vObjClobColName != null && vObjClobColName.size() > 0) {
            String columnName[]  = new String[vObjClobColName.size()];
            String columnValue[] = new String[vObjClobColName.size()];
            for(int i = 0; i < vObjClobColName.size(); i++) {
                columnName[i]  = (String)vObjClobColName.elementAt(i);
                columnValue[i] = (String)vObjClobColValue.elementAt(i);
            }
            String tableName = "kmObject";
            String condition = "obj_bob_nod_id = " + this.obj_bob_nod_id;
            cwSQL.updateClobFields(con, tableName, columnName, columnValue, condition);
        }
        
        setAll(vObjColName, vObjColValue, vObjClobColName, vObjClobColValue);
        return row;
    }


    /**
    Insert the input arguments into database as a kmObject
    @param con Connection to database
    @param vObjColName Vector of kmObject column names
    @param vObjColType Vector of kmObject column types
    @param vObjColValue Vector of kmObject column values
    @param vObjClobColName Vector of kmObject Clob column names
    @param vObjClobColValue Vector of kmObject Clob column values
    @return number of rows updates
    */
    public void ins(Connection con, 
                    Vector vObjColName, Vector vObjColType, Vector vObjColValue,
                    Vector vObjClobColName, Vector vObjClobColValue) 
                    throws SQLException {

        //set database column values
        Timestamp curTime = null;
        if(vObjColName.indexOf("obj_update_timestamp") < 0) {
            if(curTime == null) {
                curTime = cwSQL.getTime(con);
            }
            vObjColName.addElement("obj_update_timestamp");
            vObjColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
            vObjColValue.addElement(curTime);
        }

        //set all param into this object
        setAll(vObjColName, vObjColValue, vObjClobColName, vObjClobColValue);

        //insert into kmObject
        DbTable dbTab = new DbTable(con);
        dbTab.ins("kmObject", vObjColName, vObjColType, vObjColValue);

        // for other clob columns
        if (vObjClobColName != null && vObjClobColName.size() > 0) {
            String columnName[]  = new String[vObjClobColName.size()];
            String columnValue[] = new String[vObjClobColName.size()];
            for(int i = 0; i < vObjClobColName.size(); i++) {
                columnName[i]  = (String)vObjClobColName.elementAt(i);
                columnValue[i] = (String)vObjClobColValue.elementAt(i);
            }
            String tableName = "kmObject";
            String condition = "obj_bob_nod_id = " + this.obj_bob_nod_id + " AND obj_version = " + this.obj_version;
            cwSQL.updateClobFields(con, tableName, columnName, columnValue, condition);
        }     
        
        return;
    }

    public static final String SQL_GET_OBJ_XLOCK = 
        " UPDATE kmObject SET " + 
        " obj_type = obj_type " + 
        " WHERE obj_bob_nod_id = ? " + 
        " AND obj_version = ? " + 
        " AND obj_update_timestamp = ? ";

    /**
    Get an exclusive lock to underlying table kmObjecgt record
    @param con Connection to database
    @param lastUpdTimestamp timestamp for concurrence control
    @return true if record is locked successfully,
            false if cannot pass concurrence control
    */
    public boolean getXLock(Connection con, Timestamp lastUpdTimestamp) throws SQLException {
        PreparedStatement stmt = null;
        int rowCount = 0;
        try {
            stmt = con.prepareStatement(SQL_GET_OBJ_XLOCK);
            stmt.setLong(1, this.obj_bob_nod_id);
            stmt.setString(2, this.obj_version);
            stmt.setTimestamp(3, lastUpdTimestamp);
            rowCount = stmt.executeUpdate();
        } finally {
            if(stmt!=null) stmt.close();
        }
        return (rowCount > 0);
    }
    
    
    public Timestamp getObjUpdTimestamp(Connection con)
        throws SQLException {
            
            String SQL = " SELECT obj_update_timestamp FROM kmObject "
                       + " WHERE obj_bob_nod_id = ? AND obj_version = ? ";
            PreparedStatement stmt = con.prepareStatement(SQL);
            int index = 1;
            stmt.setLong(index++, this.obj_bob_nod_id);
            stmt.setString(index++, this.obj_version);
            ResultSet rs = stmt.executeQuery();
            if(rs.next())
                this.obj_update_timestamp = rs.getTimestamp("obj_update_timestamp");
            stmt.close();
            return this.obj_update_timestamp;
        }
    
}