package com.cw.wizbank.ae.db;

import java.sql.*;
import java.util.*;

import com.cw.wizbank.ae.db.sql.SqlStatements;
import com.cw.wizbank.accesscontrol.AccessControlWZB;

public class DbAppnApprovalList {
    
    public static final String ROLE_DIRECT_SUPERVISE = "DIRECT_SUPERVISE";
    public static final String ROLE_SUPERVISE = "SUPERVISE";
    public static final String ROLE_TADM = AccessControlWZB.ROL_STE_UID_TADM;
    public static final String ROLE_LRNR = AccessControlWZB.ROL_STE_UID_LRNR;
    
    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_HISTORY = "HISTORY";
    
    public static final String ACTION_APPROVED = "APPROVED";
    public static final String ACTION_DISAPPROVED = "DISAPPROVED";
    public static final String ACTION_ENROLLED = "ENROLLED";
    public static final String ACTION_CANCELLED = "CANCELLED";
    
	public long aal_id;
	public long aal_usr_ent_id;
	public long aal_app_id;
	public long aal_app_ent_id;
	public String aal_approval_role;
	public long aal_approval_usg_ent_id;
	public String aal_status;
	public Timestamp aal_create_timestamp;
	public long aal_action_taker_usr_ent_id;
	public String aal_action_taker_approval_role;
	public String aal_action_taken;
	public Timestamp aal_action_timestamp;

	/**
	Get the entity id of the participated supervise approvers
	@param con Connection to database
	@param app_id application id
	@return Vector of Long which contains the entity id of the participated supervise approvers
	*/	
    public static Vector getParticipatedSupervise(Connection con, long app_id) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs =null;
        Vector v = new Vector();
        try {
            stmt = con.prepareStatement(SqlStatements.SQL_GET_PARTICIPATED_ROLE_APPROVERS);
            stmt.setLong(1, app_id);
            stmt.setString(2, STATUS_HISTORY);
            stmt.setString(3, ROLE_SUPERVISE);
            rs = stmt.executeQuery();
            while(rs.next()) {
                v.addElement(new Long(rs.getLong(1)));
            }
        } finally {
            if(stmt!=null) stmt.close();
            if(rs!=null)rs.close();
        }
        return v;
    }
	
	/**
	Get the entity id of the participated direct supervise approvers
	@param con Connection to database
	@param app_id application id
	@return Vector of Long which contains the entity id of the participated direct supervise approvers
	*/	
    public static Vector getParticipatedDirectSupervise(Connection con, long app_id) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs =null;
        Vector v = new Vector();
        try {
            stmt = con.prepareStatement(SqlStatements.SQL_GET_PARTICIPATED_ROLE_APPROVERS);
            stmt.setLong(1, app_id);
            stmt.setString(2, STATUS_HISTORY);
            stmt.setString(3, ROLE_DIRECT_SUPERVISE);
            rs = stmt.executeQuery();
            while(rs.next()) {
                v.addElement(new Long(rs.getLong(1)));
            }
        } finally {
            if(stmt!=null) stmt.close();
            if(rs!=null)rs.close();
        }
        return v;
    }
	
	/**
	Get the entity id of the participated TADM approvers
	@param con Connection to database
	@param app_id application id
	@return Vector of Long which contains the entity id of the participated TADM approvers
	*/	
    public static Vector getParticipatedTADM(Connection con, long app_id) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs =null;
        Vector v = new Vector();
        try {
            stmt = con.prepareStatement(SqlStatements.SQL_GET_PARTICIPATED_ROLE_APPROVERS);
            stmt.setLong(1, app_id);
            stmt.setString(2, STATUS_HISTORY);
            stmt.setString(3, ROLE_TADM);
            rs = stmt.executeQuery();
            while(rs.next()) {
                v.addElement(new Long(rs.getLong(1)));
            }
        } finally {
            if(stmt!=null) stmt.close();
            if(rs!=null)rs.close();
        }
        return v;
    }
	

	/**
	Get the entity id of the participated approvers
	@param con Connection to database
	@param app_id application id
	@return Vector of Long which contains the entity id of the participated approvers
	*/	
    public static Vector getParticipatedApprovers(Connection con, long app_id) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs =null;
        Vector v = new Vector();
        try {
            stmt = con.prepareStatement(SqlStatements.SQL_GET_PARTICIPATED_APPROVERS);
            stmt.setLong(1, app_id);
            stmt.setString(2, STATUS_HISTORY);
            stmt.setString(3, ROLE_LRNR);
            rs = stmt.executeQuery();
            while(rs.next()) {
                v.addElement(new Long(rs.getLong(1)));
            }
        } finally {
            if(stmt!=null) stmt.close();
            if(rs!=null)rs.close();
        }
        return v;
    }
	
	/**
	Get the entity id of the previous approvers
	@param con Connection to database
	@param app_id application id
	@return Vector of Long which contains the entity id of the previous step approvers
	*/	
    public static Vector getPreviousApprovers(Connection con, long app_id) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs =null;
        Vector v = new Vector();
        try {
            stmt = con.prepareStatement(SqlStatements.SQL_GET_PREVIOUS_APPROVERS);
            stmt.setLong(1, app_id);
            stmt.setString(2, STATUS_HISTORY);
            stmt.setLong(3, app_id);
            stmt.setString(4, STATUS_HISTORY);
            rs = stmt.executeQuery();
            while(rs.next()) {
                v.addElement(new Long(rs.getLong("aal_usr_ent_id")));
            }
        } finally {
            if(stmt!=null) stmt.close();
            if(rs!=null)rs.close();
        }
        return v;
    }
	
	/**
	Get the entity id of the current approvers
	@param con Connection to database
	@param app_id application id
	@return Vector of Long which contains the entity id of the current step approvers
	*/	
	public static Vector getCurrentApprovers(Connection con, long app_id) throws SQLException {
	    PreparedStatement stmt = null;
	    ResultSet rs =null;
	    Vector v = new Vector();
	    try {
	        stmt = con.prepareStatement(SqlStatements.SQL_GET_CURRENT_APPROVERS);
	        stmt.setLong(1, app_id);
	        stmt.setString(2, STATUS_PENDING);
	        rs = stmt.executeQuery();
	        while(rs.next()) {
	            v.addElement(new Long(rs.getLong("aal_usr_ent_id")));
	        }
	    } finally {
	        if(stmt!=null) stmt.close();
	        if(rs!=null)rs.close();
	    }
	    return v;
	}
	
	/**
	Check if the input user is one of the current approver
	@param con Connection to database
	@param app_id application id
	@param usr_ent_id user entity id
	@return true if the input user is one of the current approver, false otherwise
	*/
	public static boolean isCurrentApprover(Connection con, long app_id, long usr_ent_id) throws SQLException {
	    PreparedStatement stmt = null;
	    ResultSet rs =null;
	    boolean result=false;
	    try {
	        stmt = con.prepareStatement(SqlStatements.SQL_IS_CURRENT_APPROVER);
	        stmt.setLong(1, app_id);
	        stmt.setLong(2, usr_ent_id);
	        stmt.setString(3, STATUS_PENDING);
	        rs = stmt.executeQuery();
	        result = rs.next();
	    } finally {
	    	if(rs!=null)rs.close();
	        if(stmt!=null) stmt.close();
	    }
	    return result;
	}
	
	/**
	Delete all approval records of the input application
	@param con Connection to database
	@param app_id application id
	*/
	public static void delAppnApprovalList(Connection con, long app_id) throws SQLException {
	    PreparedStatement stmt = null;
	    try {
	        stmt = con.prepareStatement(SqlStatements.SQL_DEL_APPN_APPROVAL_LIST);
	        stmt.setLong(1, app_id);
	        stmt.executeUpdate();
	    } finally {
	        if(stmt!=null) stmt.close();
	    }
	    return;
	}
	
	/**
	Get the application's current supervised group entity id
	@param con Connection to database
	@param app_id application id
	@return entity id of the supervised group
	*/
	public static long getCurrentSupervisedGroupEntId(Connection con, long app_id) throws SQLException {
	    PreparedStatement stmt = null;
	    ResultSet rs =null;
	    long usg_ent_id=0;
	    try {
	        stmt = con.prepareStatement(SqlStatements.SQL_GET_CURRENT_SUPERVISED_GROUP_ENT_ID);
	        stmt.setLong(1, app_id);
	        stmt.setString(2, STATUS_PENDING);
	        stmt.setString(3, ROLE_SUPERVISE);
	        rs = stmt.executeQuery();
	        if(rs.next()) {
	            usg_ent_id = rs.getLong(1);
	        }
	    } finally {
	    	if(rs!=null)rs.close();
	        if(stmt!=null) stmt.close();
	    }
	    return usg_ent_id;
	}
	
	/**
	Get the approver type of the current step approver
	@param con Connection to database 
	@param app_id application id
	@return current step approver type
	*/
	public static String getCurrentStepApproverType(Connection con, long app_id) throws SQLException {
	    PreparedStatement stmt = null;
	    ResultSet rs =null;
	    String approverType = null;
	    
	    try {
	        stmt = con.prepareStatement(SqlStatements.SQL_GET_CURRENT_STEP_APPROVER_TYPE);
	        stmt.setLong(1, app_id);
	        stmt.setLong(2, app_id);
	        rs = stmt.executeQuery();
	        if(rs.next()) {
	            approverType = rs.getString(1);
	        }
	    } finally {
	    	if(rs!=null)rs.close();
	        if(stmt!=null) stmt.close();
	    }
	    return approverType;
	}

    /**
    Make approval action on an application and hence turn the PENDING approval list to HISTORY
    @param con Connection to database
    @param app_id application id
    @param taker_usr_ent_id user entity id of the action taker
    @param taker_role approval role of the action taker
    @param action_taken action taken 
    @parma action_timestamp action taken time
    */
    public static void makeAction(Connection con, long app_id, long aah_id
                                 ,long taker_usr_ent_id, String taker_role
                                 ,String action_taken, Timestamp action_timestamp) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs =null;
        try {

            stmt = con.prepareStatement(SqlStatements.SQL_MAKE_APP_APPROVAL_ACTION);
            int index = 1;
            stmt.setString(index++, STATUS_HISTORY);
            stmt.setLong(index++, aah_id);
            stmt.setLong(index++, taker_usr_ent_id);
            stmt.setString(index++, taker_role);
            stmt.setString(index++, action_taken);
            stmt.setTimestamp(index++, action_timestamp);
            stmt.setString(index++, STATUS_PENDING);
            stmt.setLong(index++, app_id);
            stmt.executeUpdate();

        } finally {
            if(stmt!=null) stmt.close();
            if(rs!=null)rs.close();
        }
        return;
    }

    /**
    Insert a new record in aeAppnApprovalList
    @param con Connection to datbase
    */
	public void ins(Connection con) throws SQLException {
	    PreparedStatement stmt = null;
	    try {
	        stmt = con.prepareStatement(SqlStatements.SQL_INS_APPN_APPROVAL);
	        int index = 1;
	        stmt.setLong(index++, this.aal_usr_ent_id);
	        stmt.setLong(index++, this.aal_app_id);
	        stmt.setLong(index++, this.aal_app_ent_id);
	        stmt.setString(index++, this.aal_approval_role);
	        if(this.aal_approval_usg_ent_id > 0) {
	            stmt.setLong(index++, this.aal_approval_usg_ent_id);
            } else {
                stmt.setNull(index++, java.sql.Types.INTEGER);
            }
	        stmt.setString(index++, this.aal_status);
	        stmt.setTimestamp(index++, this.aal_create_timestamp);
	        if(this.aal_action_taker_usr_ent_id > 0) {
	            stmt.setLong(index++, this.aal_action_taker_usr_ent_id);
            } else {
                stmt.setNull(index++, java.sql.Types.INTEGER);
            }
	        stmt.setString(index++, this.aal_action_taker_approval_role);
	        stmt.setString(index++, this.aal_action_taken);
	        stmt.setTimestamp(index++, this.aal_action_timestamp);
	        stmt.executeUpdate();
	    } finally {
	        if(stmt!=null) stmt.close();
	    }
	    return;
	}
	
	
	/**
	Get the current approval role of this approval record
	Pre-define variables: aal_app_id, aal_usr_ent_id
	@param con Connection to database
	@return null if the user does not have a PENDING approval, else the current approval role
	*/
	public String getCurrentApprovalRole(Connection con) throws SQLException {
	    PreparedStatement stmt = null;
	    ResultSet rs =null;
	    try {
	        stmt = con.prepareStatement(SqlStatements.SQL_GET_CURRENT_APPROVAL_ROLE);
	        stmt.setLong(1, this.aal_app_id);
	        stmt.setLong(2, this.aal_usr_ent_id);
	        stmt.setString(3, STATUS_PENDING);
	        rs = stmt.executeQuery();
	        if(rs.next()) {
	            this.aal_approval_role = rs.getString("aal_approval_role");
	        } else {
	            this.aal_approval_role = null;
	        }
	    } finally {
	        if(stmt!=null) stmt.close();
	        if(rs!=null)rs.close();
	    }
	    return this.aal_approval_role;
	}
	
    // return true if has pending approval rec or approval history
	public static boolean hasApprovalList(Connection con, long usr_ent_id) throws SQLException{
	    boolean bResult = false;
	    PreparedStatement stmt = con.prepareStatement(SqlStatements.SQL_HAS_APPROVAL_LIST); 
        stmt.setLong(1, usr_ent_id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()){
            int cnt = rs.getInt("cnt");
            if (cnt > 0){
                bResult = true;
            }
        }
        stmt.close();
        return bResult; 
    }
    
    public static boolean hasPendingAppn(Connection con, long app_ent_id) throws SQLException{
        boolean bResult = false;
        PreparedStatement stmt = con.prepareStatement(SqlStatements.SQL_HAS_PENDING_APPN);
        stmt.setLong(1, app_ent_id);
        stmt.setString(2, STATUS_PENDING);
        
        ResultSet rs = stmt.executeQuery();
        if (rs.next()){
            int cnt = rs.getInt("cnt");
            if (cnt > 0){
                bResult = true;
            }
        }
        stmt.close();
        return bResult; 
    }
	
    public static boolean hasPendingApprovalAppn(Connection con, long usr_ent_id) throws SQLException{
        boolean bResult = false;
        PreparedStatement stmt = con.prepareStatement(SqlStatements.SQL_HAS_PENDING_APPROVAL_APPN);
        stmt.setLong(1, usr_ent_id);
        stmt.setString(2, STATUS_PENDING);
        
        ResultSet rs = stmt.executeQuery();
        if (rs.next()){
            int cnt = rs.getInt("cnt");
            if (cnt > 0){
                bResult = true;
            }
        }
        stmt.close();
        return bResult; 
    }

    /**
    Get the input application's pending approval role
    @param con Connection to database
    @param app_id application id
    @return the application's pending approval role; or null if the application is not in pending approval status
    */
    public static String getAppnPendingApprovalRole(Connection con, long app_id) throws SQLException {
        String approval_role = null;
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(SqlStatements.SQL_GET_APPN_PENDING_APPROVAL_ROLE);
            stmt.setString(1, STATUS_PENDING);
            stmt.setLong(2, app_id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                approval_role = rs.getString(1);
            }
        } finally {
            if(stmt!=null) {stmt.close();}
        }
        return approval_role;
    }

    /**
    Get the input application action's approval role
    @param con Connection to database
    @param aah_id application action id
    @return the application action's approval role; or null if the application action does not exist
    */
    public static String getAppnActnApprovalRole(Connection con, long aah_id) throws SQLException {
        String approval_role = null;
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(SqlStatements.SQL_GET_ACTN_APPROVAL_ROLE);
            stmt.setLong(1, aah_id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                approval_role = rs.getString(1);
            }
        } finally {
            if(stmt!=null) {stmt.close();}
        }
        return approval_role;
    }

	public static Vector getDirectSupervisorsWithPendingApprovals(Connection con, long usr_ent_id) throws SQLException {
		PreparedStatement stmt = null;
		Vector v = new Vector();
		try {
			stmt = con.prepareStatement(SqlStatements.SQL_PENDING_APPROVAL_DIRECT_SUP);
			stmt.setLong(1, usr_ent_id);
			stmt.setString(2, ROLE_DIRECT_SUPERVISE);
			stmt.setString(3, STATUS_PENDING);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				v.addElement(new Long(rs.getLong(1)));
			}
		} finally {
			if(stmt!=null) stmt.close();
		}
		return v;
	}

	public static Vector getUserGroupsWithPendingApprovals(Connection con, long usr_ent_id) throws SQLException {
		PreparedStatement stmt = null;
		Vector v = new Vector();
		try {
			stmt = con.prepareStatement(SqlStatements.SQL_PENDING_APPROVAL_USER_GROUPS);
			stmt.setLong(1, usr_ent_id);
			stmt.setString(2, ROLE_SUPERVISE);
			stmt.setString(3, STATUS_PENDING);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				v.addElement(new Long(rs.getLong(1)));
			}
		} finally {
			if(stmt!=null) stmt.close();
		}
		return v;
	}
    
    public static boolean isPreviousApprover(Connection con, long app_id, long usr_ent_id) 
    throws SQLException {

        boolean isPreviousApprover = false;	 
        PreparedStatement stmt = null;
        ResultSet rs =null;	 
        try {
	        stmt = con.prepareStatement(SqlStatements.SQL_IS_PREVIOUS_APPROVER);
	        stmt.setLong(1, app_id);
	        stmt.setLong(2, usr_ent_id);
	        stmt.setString(3, DbAppnApprovalList.STATUS_HISTORY);
	        stmt.setString(4, ACTION_APPROVED);
	
	        rs = stmt.executeQuery();
	        while (rs.next()) {
	            if (rs.getInt(1) > 0) {
	                isPreviousApprover = true;
	            } else {
	                isPreviousApprover = false;
	            }
	        }
		} finally {
        	if(rs!=null)rs.close();
            if(stmt!=null) stmt.close();
        }
        stmt.close();
        return isPreviousApprover;
    }


}