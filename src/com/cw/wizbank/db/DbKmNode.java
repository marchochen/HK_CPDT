package com.cw.wizbank.db;

import java.sql.*;
import java.util.*;

import com.cw.wizbank.util.*;

/**
A database class to manage table kmNode
*/
public class DbKmNode {

    public static final String ID_SEPARATOR = " , ";

    /* Possible values for nod_type */
    public static final String NODE_TYPE_OBJECT = "OBJECT";
    public static final String NODE_TYPE_FOLDER = "FOLDER";
    public static final String NODE_TYPE_LINK = "LINK";

    
    public long         nod_id;
    public String       nod_type;
    public long         nod_order;
    public long         nod_parent_nod_id;
    public String       nod_ancestor;
    public String       nod_create_usr_id;
    public Timestamp    nod_create_timestamp;
    public long         nod_owner_ent_id;
    public boolean      nod_acl_inherit_ind;
    public int          nod_display_option_ind;
    
    private static final String SQL_INS_KMNODE  = " INSERT INTO kmNode ( " 
                                                + "  nod_type, nod_order, "
                                                + "  nod_parent_nod_id, " 
                                                + "  nod_ancestor, nod_create_usr_id, nod_create_timestamp, "
                                                + "  nod_owner_ent_id, "
                                                + "  nod_acl_inherit_ind, "
                                                + "  nod_display_option_ind "
                                                + "  ) "
                                                + " VALUES (?,?,?,?,?,?,?,?,?) ";

    private static final String SQL_GET_KMNODE  = " SELECT nod_type, " 
                                                + "  nod_order, nod_parent_nod_id, "
                                                + "  nod_ancestor, " 
                                                + "  nod_create_usr_id, nod_create_timestamp, "
                                                + "  nod_owner_ent_id, "
                                                + "  nod_acl_inherit_ind, "
                                                + "  nod_display_option_ind "
                                                + " FROM kmNode WHERE nod_id = ? ";

    private static final String SQL_GET_ANCESTOR  = " SELECT nod_id, nod_ancestor " 
                                                + " FROM kmNode WHERE nod_id IN ";

    private static final String SQL_GET_PARENT_ID  = " SELECT nod_parent_nod_id " 
                                                + " FROM kmNode WHERE nod_id = ? ";
                                                
    private static final String SQL_GET_CHILD_CNT  = " SELECT count(nod_id) " 
                                                + " FROM kmNode WHERE nod_parent_nod_id = ? ";
                                                
    private static final String SQL_UPD_KMNODE  = " UPDATE kmNode SET "
                                                + "  nod_order = ? , "
                                                + "  nod_acl_inherit_ind = ?, "
                                                + "  nod_display_option_ind = ? "
                                                + " WHERE nod_id = ? ";
                                  
    private static final String SQL_DEL_KMNODE  = " DELETE FROM kmNode " 
                                                + " WHERE nod_id = ? ";

    private static final String SQL_DEL_KMNODE_LST  = " DELETE FROM kmNode " 
                                                + " WHERE nod_id IN ";

    private static final String SQL_UPD_ANCESTOR_KMNODE  = " UPDATE kmNode SET "
                                                         + " nod_parent_nod_id = ?, "
                                                         + " nod_ancestor = ? "
                                                         + " WHERE nod_id = ? ";

    /**
    Inser a new node
    @return the row count for INSERT
    */
    public int ins(Connection con)
        throws SQLException, cwSysMessage {
        
        PreparedStatement stmt = null;
        int code = 0;
        try {
            //get database time
            if(nod_create_timestamp == null) {
                nod_create_timestamp = cwSQL.getTime(con);
            }
            
            if (nod_parent_nod_id > 0) {
                nod_ancestor = getAncestor(con, nod_parent_nod_id);
                if (nod_ancestor == null) {
                    nod_ancestor = new String(" "); 
                }else {
                    nod_ancestor += ", ";
                }
                nod_ancestor += nod_parent_nod_id + " " ;
            }
            
            //try to insert 
            int index =1;
            stmt = con.prepareStatement(SQL_INS_KMNODE, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(index++, nod_type);
            cwSQL.setLong(stmt, index++, nod_order);
            cwSQL.setLong(stmt, index++, nod_parent_nod_id);
            stmt.setString(index++, nod_ancestor);
            stmt.setString(index++, nod_create_usr_id);
            stmt.setTimestamp(index++, nod_create_timestamp);
            stmt.setLong(index++, nod_owner_ent_id);
            stmt.setBoolean(index++, nod_acl_inherit_ind);
            stmt.setInt(index++, nod_display_option_ind);
            code = stmt.executeUpdate();
            nod_id = cwSQL.getAutoId(con, stmt, "kmNode", "nod_id");
        } finally {
            if(stmt!=null) stmt.close();
        }

        return code;
    }

    /**
    Get all field of the specified node from the database
    */
    public void get(Connection con)
        throws SQLException, cwSysMessage {

        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(SQL_GET_KMNODE);
            stmt.setLong(1, nod_id);

            ResultSet rs = stmt.executeQuery();
            if( rs.next() ) {
                nod_type                = rs.getString("nod_type");
                nod_order               = rs.getLong("nod_order");
                nod_parent_nod_id       = rs.getLong("nod_parent_nod_id");
                nod_ancestor            = rs.getString("nod_ancestor");
                nod_create_usr_id       = rs.getString("nod_create_usr_id");
                nod_create_timestamp    = rs.getTimestamp("nod_create_timestamp");
                nod_acl_inherit_ind     = rs.getBoolean("nod_acl_inherit_ind");
                nod_display_option_ind  = rs.getInt("nod_display_option_ind");
            }
            else {
                throw new cwSysMessage(cwSysMessage.GEN_REC_NOT_FOUND, " node id = " + nod_id);
            }
        } finally {
            if(stmt!=null) stmt.close();
        }
        return;
    }

    /**
    Get the ancestor of the specified node
    */
    public static String getAncestor(Connection con, long node_id) throws SQLException {
        Vector nodeVec = new Vector();
        nodeVec.addElement(new Long(node_id));
        Hashtable nodeHash = getAncestor(con, nodeVec);
        return ((String) nodeHash.get(new Long(node_id)));
    }
    
    
    /**
    Get the ancestor of the a list of nodes
    */
    public static Hashtable getAncestor(Connection con, Vector  nodeVec)
        throws SQLException {
        
        Hashtable ancestorHash  = new Hashtable();
        if (nodeVec == null || nodeVec.size() ==0) {
            return ancestorHash;
        }
        
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(SQL_GET_ANCESTOR + cwUtils.vector2list(nodeVec));

            ResultSet rs = stmt.executeQuery();
            while( rs.next() ) {
                Long nodeID = new Long(rs.getLong("nod_id"));
                String nodeAncestor = rs.getString("nod_ancestor");
                if (nodeAncestor != null) {
                    ancestorHash.put(nodeID, nodeAncestor);
                }
            }
        } finally {
            if(stmt!=null) stmt.close();
        }
        return ancestorHash;
    }

        

    /**
    Update a specified node from the database
    @return the row count for UPDATE    
    */
    public int upd(Connection con)
        throws SQLException {
        PreparedStatement stmt=null;
        int code=0;
        try {
            int index =1;
            stmt = con.prepareStatement(SQL_UPD_KMNODE);
            cwSQL.setLong(stmt, index++, nod_order);
            stmt.setBoolean(index++,  nod_acl_inherit_ind);
            stmt.setInt(index++, nod_display_option_ind);
            stmt.setLong(index++, nod_id);
            code = stmt.executeUpdate();
        } finally {
            if(stmt!=null) stmt.close();
        }

        return code;
    }
    

    /**
    Delete a specified node from the database
    @return the row count for DELETE    
    */
    public int del(Connection con)
        throws SQLException {
            
        PreparedStatement stmt=null;
        int code=0;
        try {
            stmt = con.prepareStatement(SQL_DEL_KMNODE);
            stmt.setLong(1, nod_id);
            
            code = stmt.executeUpdate();
        } finally {
            if(stmt!=null) stmt.close();
        }        
        return code;
    }

    /**
    Delete a list of node from the database
    @return the row count for DELETE    
    */
    public static int delAll(Connection con, Vector idVec)
        throws SQLException {
            
        PreparedStatement stmt=null;
        int code=0;
        try {
            String id_lst = cwUtils.vector2list(idVec);
            stmt = con.prepareStatement(SQL_DEL_KMNODE_LST + id_lst);
            
            code = stmt.executeUpdate();
        } finally {
            if(stmt!=null) stmt.close();
        }        
        return code;
    }

    /**
    Check wether a node has child attached
    @return true if it has child
    */
    public static boolean hasChild(Connection con, long node_id)
        throws SQLException {
            
        PreparedStatement stmt=null;
        int cnt=0;
        try {
            stmt = con.prepareStatement(SQL_GET_CHILD_CNT);
            stmt.setLong(1, node_id);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                cnt = rs.getInt(1);
            }
        } finally {
            if(stmt!=null) stmt.close();
        }        
        if (cnt > 0) {
            return true;
        }else {
            return false;
        }
    }

    /**
    Get the parent id of a node
    */
    public static long getParentID(Connection con, long node_id)
        throws SQLException {
            
        PreparedStatement stmt=null;
        long parent_nod_id=0;
        try {
            stmt = con.prepareStatement(SQL_GET_PARENT_ID);
            stmt.setLong(1, node_id);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                parent_nod_id = rs.getLong("nod_parent_nod_id");
            }
        } finally {
            if(stmt!=null) stmt.close();
        }
        
        return parent_nod_id;
    }

    /**
    Set the attributes of this node
    @param vNodColName Vector of kmNode column names 
    @param vNodColValue Vector of kmNode column values
    */
    private void setAll(Vector vNodColName, Vector vNodColValue)  {
        
        int index = 0;

        index = vNodColName.indexOf("nod_type");
        if(index > -1) {
            this.nod_type = ((String)vNodColValue.elementAt(index));
        }
        
        index = vNodColName.indexOf("nod_order");
        if(index > -1) {
            this.nod_order = ((Long)vNodColValue.elementAt(index)).longValue();
        }

        index = vNodColName.indexOf("nod_parent_nod_id");
        if(index > -1) {
            this.nod_parent_nod_id = ((Long)vNodColValue.elementAt(index)).longValue();
        }

        index = vNodColName.indexOf("nod_ancestor");
        if(index > -1) {
            this.nod_ancestor = ((String)vNodColValue.elementAt(index));
        }

        index = vNodColName.indexOf("nod_create_timestamp");
        if(index > -1) {
            this.nod_create_timestamp = ((Timestamp)vNodColValue.elementAt(index));
        }

        index = vNodColName.indexOf("nod_create_usr_id");
        if(index > -1) {
            this.nod_create_usr_id = ((String)vNodColValue.elementAt(index));
        }

        index = vNodColName.indexOf("nod_owner_ent_id");
        if(index > -1) {
            this.nod_owner_ent_id = ((Long)vNodColValue.elementAt(index)).longValue();
        }

        index = vNodColName.indexOf("nod_acl_inherit_ind");
        if(index > -1) {
            this.nod_acl_inherit_ind = ((Boolean)vNodColValue.elementAt(index)).booleanValue();
        }

        return;
    }


    /**
    Insert the input arguments into database as a kmObject
    @param con Connection to database
    @param vNodColName Vector of kmNode column names 
    @param vNodColType Vector of kmNode column types
    @param vNodColValue Vector of kmNode column values
    @return number of rows updates
    */
    public int ins(Connection con, 
                   Vector vNodColName, Vector vNodColType, Vector vNodColValue) 
                   throws SQLException, cwSysMessage {


        //get ancestor
        int index = vNodColName.indexOf("nod_parent_nod_id");
        long parent_nod_id = ((Long)vNodColValue.elementAt(index)).longValue();
        if (parent_nod_id > 0) {
            String ancestor = getAncestor(con, parent_nod_id);
            if (ancestor == null) {
                ancestor = new String(" "); 
            }else {
                ancestor += ", ";
            }
            ancestor += parent_nod_id + " " ;
            vNodColName.addElement("nod_ancestor");
            vNodColType.addElement(DbTable.COL_TYPE_STRING);
            vNodColValue.addElement(ancestor);
        }

        //insert into kmObject
        DbTable dbTab = new DbTable(con);
        PreparedStatement stmt = dbTab.ins4AutoId("kmNode", vNodColName, vNodColType, vNodColValue);
        this.nod_id = cwSQL.getAutoId(con, stmt, "kmNode", "nod_id");
        if (stmt != null) {
        	stmt.close();
        }
        //set values to this node's attributes
        setAll(vNodColName, vNodColValue);

        return 1;
    }

    /**
    Update ancestor of a node
    @return the row count for UPDATE
    */
    public int updAncestor(Connection con)
        throws SQLException, cwSysMessage {        
        PreparedStatement stmt = null;
        int code = 0;
        try {
            //get database time
            if(nod_create_timestamp == null) {
                nod_create_timestamp = cwSQL.getTime(con);
            }
            
            if (nod_parent_nod_id > 0) {
                nod_ancestor = getAncestor(con, nod_parent_nod_id);
                if (nod_ancestor == null) {
                    nod_ancestor = new String(" "); 
                }else {
                    nod_ancestor += ", ";
                }
                nod_ancestor += nod_parent_nod_id + " " ;
            }
            
            //try to insert 
            int index =1;
            stmt = con.prepareStatement(SQL_UPD_ANCESTOR_KMNODE);
            cwSQL.setLong(stmt, index++, nod_parent_nod_id);
            stmt.setString(index++, nod_ancestor);
            stmt.setLong(index++, nod_id);
            code = stmt.executeUpdate();
        } finally {
            if(stmt!=null) stmt.close();
        }

        return code;
    }
}

