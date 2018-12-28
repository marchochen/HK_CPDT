package com.cw.wizbank.accesscontrol;

import java.sql.*;
import java.util.Vector;
import java.util.Hashtable;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.util.cwUtils;

/**
this class provide functions on assigning functions to roles
*/
public class AcRoleFunction {
    
    /** 
    HashTable Key
    */
    public static final String FTN_TYPES = "FTN_TYPES";

    /** 
    HashTable Key
    */
    public static final String FTN_EXT_IDS = "FTN_EXT_IDS";
    
    /** 
    HashTable Key
    */
    public static final String FTN_IDS = "FTN_IDS";

    /** 
    HashTable Key
    */
    public static final String FTN_PICK_INDS = "FTN_PICK_INDS";
    
    private Connection con;
        
    public AcRoleFunction(Connection con) {
        super();
        setCon(con);
    }
    
    public void setCon(Connection con) {
        this.con = con;
    }
       
    public Connection getCon() {
        return this.con;
    }
    
    /**
    get Hashtable of functions that a role has
    @return A Hashtable contains 4 vectors<BR>
    <ul>
    <li>function type
    <li>function ext id
    <li>function id
    <li>true/false indicator to show weather the function is assigned to a role
    </ul>
    */
    public Hashtable getRoleFunction(String rol_ext_id) throws SQLException {
 
        Hashtable h = new Hashtable();
        Vector v_ftn_types = new Vector();
        Vector v_ftn_ext_ids = new Vector();
        Vector v_ftn_ids = new Vector();
        Vector v_ftn_pick_inds = new Vector();
        
        String SQL = "Select ftn_type, ftn_ext_id, ftn_id, 1 picked from acRole, acRoleFunction, acFunction " +
                    "Where rol_ext_id = ? " +
                    "And rol_id = rfn_rol_id " +
                    "And ftn_id = rfn_ftn_id " +
                    "union " +
                    "Select ftn_type, ftn_ext_id, ftn_id, 0 picked from acFunction " +
                    "Where Not Exists " +
                    "(select * from acRole, acRoleFunction Where " +
                    "rol_ext_id = ? "+
                    "And rol_id = rfn_rol_id " +
                    "And ftn_id = rfn_ftn_id ) " +
                    " order by ftn_type, ftn_ext_id ";
                    
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setString(1, rol_ext_id);
        stmt.setString(2, rol_ext_id);
        ResultSet rs = stmt.executeQuery();
        while(rs.next()) {
            v_ftn_types.addElement(rs.getString("ftn_type"));
            v_ftn_ext_ids.addElement(rs.getString("ftn_ext_id"));
            v_ftn_ids.addElement(rs.getString("ftn_id"));
            v_ftn_pick_inds.addElement(new Boolean(rs.getBoolean("picked")));
        }
        stmt.close();
        h.put(FTN_TYPES, v_ftn_types);
        h.put(FTN_EXT_IDS, v_ftn_ext_ids);
        h.put(FTN_IDS, v_ftn_ids);
        h.put(FTN_PICK_INDS, v_ftn_pick_inds);
        return h;
    }
    
    
    public static void updFunctionStatus(Connection con, String[] ftn_ext_ids, int status) throws SQLException {
    	if(ftn_ext_ids != null && ftn_ext_ids.length > 0){
	        String SQL = "update acFunction set ftn_status = ? where ftn_ext_id in " +cwUtils.array2list(ftn_ext_ids);
	                    
	        PreparedStatement stmt = con.prepareStatement(SQL);
	        stmt.setInt(1, status);
	        stmt.executeUpdate();
	        stmt.close();
    	}
    }
    
    /**
    save functions ftn_ext_ids to role rol_ext_id<BR>
    all functions from the role will be cleared first and then save the new functions
    */
    public void saveRoleFunction(String rol_ext_id, String[] ftn_ext_ids) throws SQLException {
        
        if(ftn_ext_ids != null) {
            AccessControlWZB acl = new AccessControlWZB();
            acl.delRoleFunctionOfRole(con, rol_ext_id);
            
            for(int i=0; i<ftn_ext_ids.length; i++) {
                acl.grantRolePrivilege(con, rol_ext_id, ftn_ext_ids[i]);
            }
        }
    }
}