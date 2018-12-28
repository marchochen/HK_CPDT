package com.cw.wizbank.db;

import java.sql.*;
import java.util.*;

import com.cw.wizbank.util.*;

/**
A database class to manage table kmNodeAccess
*/
public class DbKmNodeAccess {

    /* Possible values for nod_access_type */
    public static final String ACCESS_TYPE_READER   = "READER";
    public static final String ACCESS_TYPE_AUTHOR   = "AUTHOR";
    public static final String ACCESS_TYPE_OWNER    = "OWNER";
    public static final String NATURE_LIBRARY = "LIBRARY";

    public long         nac_nod_id;
    public String       nac_access_type;
    public long         nac_ent_id;
    
    private static final String SQL_INS_KMNODEACCESS    = " INSERT INTO kmNodeAccess ( " 
                                                + "  nac_nod_id, nac_access_type, "
                                                + "  nac_ent_id " 
                                                + "  ) "
                                                + " VALUES (?,?,?) ";

    private static final String SQL_DEL_BY_NODE  = " DELETE FROM kmNodeAccess " 
                                                + " WHERE nac_nod_id = ? ";


    private static final String SQL_GET_ALL  = " SELECT nac_access_type, nac_ent_id FROM kmNodeAccess " 
                                                + " WHERE nac_nod_id = ? ORDER by nac_access_type ";

    /**
    Inser a new node access
    @return the row count for INSERT
    */
    public int ins(Connection con)
        throws SQLException {
        
        PreparedStatement stmt = null;
        int code = 0;
        try {
            int index =1;
            stmt = con.prepareStatement(SQL_INS_KMNODEACCESS);
            stmt.setLong(index++, nac_nod_id);
            stmt.setString(index++, nac_access_type);
            stmt.setLong(index++, nac_ent_id);

            code = stmt.executeUpdate();
        } finally {
            if(stmt!=null) stmt.close();
        }

        return code;
    }

    /**
    Get all the access record of a node
    @return Hashtable contains a list of vector 
    */
    public static Hashtable getAll(Connection con, long node_id)
        throws SQLException {
        
        Hashtable accessTypeHash  = new Hashtable();
        Vector entVec  = null;
        PreparedStatement stmt = null;
        int code = 0;
        try {
            int index =1;
            stmt = con.prepareStatement(SQL_GET_ALL);
            stmt.setLong(1, node_id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String access_type = rs.getString("nac_access_type");
                entVec = (Vector) accessTypeHash.get(access_type);
                if (entVec == null) {
                    entVec = new Vector();
                }
                entVec.addElement(new Long(rs.getLong("nac_ent_id")));
                accessTypeHash.put(access_type, entVec);
            }
        } finally {
            if(stmt!=null) stmt.close();
        }

        return accessTypeHash;
    }

    /**
    Delete all node access of a node
    @return the row count for DELETE    
    */
    public static int delByNode(Connection con, long node_id)
        throws SQLException {
            
        PreparedStatement stmt=null;
        int code=0;
        try {
            stmt = con.prepareStatement(SQL_DEL_BY_NODE);
            stmt.setLong(1, node_id);
            
            code = stmt.executeUpdate();
        } finally {
            if(stmt!=null) stmt.close();
        }        
        return code;
    }
    
    /**
    Insert a list of entity withe the same access type on the node
    @return the number of record inserted
    */
    public static int insByType(Connection con, long node_id, String access_type, long[] ent_id_lst) 
        throws SQLException {

        int cnt = 0;         
        if (ent_id_lst == null || ent_id_lst.length ==0){
            return cnt;
        }
        

        DbKmNodeAccess nac = null;
        for (int i=0;i<ent_id_lst.length;i++) {
            nac = new DbKmNodeAccess();
            nac.nac_nod_id = node_id;
            nac.nac_access_type = access_type;
            nac.nac_ent_id = ent_id_lst[i];
            nac.ins(con);
            cnt ++;
        }
        return cnt;
    }

    /**
    Check whether the usr has the right to edit the node (edit/delete node and add objects)
    @param node_id id of the node
    @param usrGroupsList all the usergroups that the user belongs to (his/own entity id in inclued in the vector)
    @return boolean whether the user has right or not
    */
    public static boolean hasEditNodeRight(Connection con, long node_id, String usrGroupsList)
        throws SQLException {

        String accessTypes = "('" + ACCESS_TYPE_OWNER + "')";
        Vector nodeVec = new Vector();
        nodeVec.addElement(new Long(node_id));
        Vector accessibleNode = getAccessibleNode(con, nodeVec, accessTypes, usrGroupsList);
        if (accessibleNode.contains(new Long(node_id))) {
            return true;
        }else {
            return false;
        }
    }
    
    /**
    Check whether the usr has the right to add sub-node
    @param node_id id of the node
    @param usrGroupsList all the usergroups that the user belongs to (his/own entity id in inclued in the vector)
    @return boolean whether the user has right or not
    */
    public static boolean hasAddNodeRight(Connection con, long node_id, String usrGroupsList)
        throws SQLException {

        String accessTypes = "('" + ACCESS_TYPE_AUTHOR + "','" + ACCESS_TYPE_OWNER + "')";
        Vector nodeVec = new Vector();
        nodeVec.addElement(new Long(node_id));
        Vector accessibleNode = getAccessibleNode(con, nodeVec, accessTypes, usrGroupsList);
        if (accessibleNode.contains(new Long(node_id))) {
            return true;
        }else {
            return false;
        }
    }

    /**
    Check whether the usr has the right to read the node
    @param node_id id of the node
    @param usrGroupsList all the usergroups that the user belongs to (his/own entity id in inclued in the vector)
    @return boolean whether the user has right or not
    */
    public static boolean hasReadNodeRight(Connection con, long node_id, String usrGroupsList)
        throws SQLException {

        Vector nodeVec = new Vector();
        nodeVec.addElement(new Long(node_id));
        Vector accessibleNode = getAccessibleNode(con, nodeVec, null, usrGroupsList);
        if (accessibleNode.contains(new Long(node_id))) {
            return true;
        }else {
            return false;
        }
    }

    /*
    Get the acccessible node 
    @param nodeVec check which node in the nodeVec are accessible
    @param usrGroupsList all the usergroups that the user belongs to (his/her own entity id in included in the vector)
    @return the vector of node that are accessible by the user
    */
    public static Vector getReadableNode(Connection con, Vector nodeVec, String usrGroupsList) 
        throws SQLException {
        
        // new logic: not count owner, author, only count reader
        String accessTypes = "('" + ACCESS_TYPE_READER + "')";
        return (getAccessibleNode(con, nodeVec, accessTypes, usrGroupsList));
//        return (getAccessibleNode(con, nodeVec, null, usrGroupsList));
    }

    /*
    Get the acccessible node 
    @param usrGroupsList all the usergroups that the user belongs to (his/her own entity id in included in the vector)
    @return the vector of node that are accessible by the user
    */
    public static Vector getReadableNode(Connection con, String usrGroupsList) 
        throws SQLException {
        
        // new logic: not count owner, author, only count reader
        String accessTypes = "('" + ACCESS_TYPE_READER + "')";
        return (getAccessibleNode(con, null, accessTypes, usrGroupsList));
//        return (getAccessibleNode(con, null, null, usrGroupsList));
    }

    /*
    Get the editable node 
    @param usrGroupsList all the usergroups that the user belongs to (his/her own entity id in included in the vector)
    */
    public static Vector getEditableNode(Connection con, Vector nodeVec, String usrGroupsList) 
        throws SQLException {
        
        String accessTypes = "('" + ACCESS_TYPE_OWNER + "')";
        return (getAccessibleNode(con, nodeVec, accessTypes, usrGroupsList));
    }


    /*
    Get the acccessible node 
    @param nodeVec check which node in the nodeVec are accessible
    @param accessTypes the access type
    @param usrGroupsList all the usergroups that the user belongs to (his/her own entity id in included in the vector)
    */
    public static Vector getAccessibleNode(Connection con, Vector nodeVec, String accessTypes, String usrGroupsList) 
        throws SQLException {

        String sql = " SELECT distinct(nac_nod_id) From kmNodeAccess " 
                   + " WHERE nac_ent_id IN " + usrGroupsList;
                   
        if (nodeVec != null && nodeVec.size() > 0) {
            sql += " AND nac_nod_id IN " + cwUtils.vector2list(nodeVec);
        }                   
        if (accessTypes != null) {
            sql += " AND nac_access_type IN " + accessTypes;
        }

        PreparedStatement stmt = null;
        Vector accessibleVec = new Vector();
        try {
            stmt = con.prepareStatement(sql);
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                 accessibleVec.addElement(new Long(rs.getLong("nac_nod_id")));      
            }
            
        } finally {
            if(stmt!=null) stmt.close();
        }

        if(accessTypes == null || accessTypes.indexOf(ACCESS_TYPE_READER)>-1) {
            sql = " SELECT nod_id From kmNode " 
                + " WHERE nod_display_option_ind = ? "
                + " AND nod_id NOT IN ( " 
                + " SELECT distinct(nac_nod_id) FROM kmNodeAccess "
                + " WHERE nac_access_type = ? )";
            try {
                stmt = con.prepareStatement(sql);
                stmt.setInt(1, 1);
                stmt.setString(2, ACCESS_TYPE_READER);
                
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    accessibleVec.addElement(new Long(rs.getLong("nod_id")));      
                }
                
            } finally {
                if(stmt!=null) stmt.close();
            }
        }

        cwUtils.removeDuplicate(accessibleVec);
        return accessibleVec;

    }

    public static final String SQL_HAS_OBJ_CHECK_IN_PRIVILEGE = 
        " SELECT obj_bob_nod_id " + 
        " FROM kmObject, RegUser " + 
        " WHERE obj_update_usr_id = usr_id " + 
        " AND obj_bob_nod_id = ? " +
        " AND usr_ent_id = ? " + 
        " AND obj_status = ? " + 
        " AND obj_latest_ind = ? ";

    /**
    Check if the object is checked out by the user
    @param con Connection to database
    @param node_id object node id
    @param user_ent_id user entity id
    @return true if the user can check in the object, else return false
    */
    public static boolean isCheckedOutByUser(Connection con, long node_id, long user_ent_id) throws SQLException {
        
        PreparedStatement stmt = null; 
        boolean b;
        try {
            stmt = con.prepareStatement(SQL_HAS_OBJ_CHECK_IN_PRIVILEGE);
            stmt.setLong(1, node_id);
            stmt.setLong(2, user_ent_id);
            stmt.setString(3, DbKmObject.OBJ_STATUS_CHECKED_OUT);
            stmt.setBoolean(4, true);
            ResultSet rs = stmt.executeQuery();
            b = rs.next();
        } finally {
            if(stmt!=null) stmt.close();
        }
        return b;
    }

    public static boolean isLibraryType(Connection con, long node_id)
        throws SQLException {

        String nature = DbKmBaseObject.getNature(con,node_id); 
        if(nature != null && nature.equals(NATURE_LIBRARY)){
            return true;
        }else {
            return false;
        }
    }

}

