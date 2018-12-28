package com.cw.wizbank.accesscontrol;

import java.sql.*;

public class AcKmLibrary extends AcInstance {

    /**
    function ext id
    */
    public static final String FTN_KML_CHECKIN = "KML_CHECKIN";
    public static final String FTN_KML_CHECKOUT = "KML_CHECKOUT";
    public static final String FTN_KML_RENEW = "KML_RENEW";
    public static final String FTN_KML_RESERVE = "KML_RESERVE";
    public static final String FTN_KML_BORROW = "KML_BORROW";
    public static final String FTN_KML_USER_HISTORY = "KML_USER_HISTORY";
    public static final String FTN_KML_COPY_HISTORY = "KML_COPY_HISTORY";
    public static final String FTN_KML_CANCEL = "KML_CANCEL";

    public long usr_ent_id;
    
    /**
    Contructor
    @param usr_ent_id_ entity id of the user
    */
    public AcKmLibrary(Connection con, long usr_ent_id_) {
        super(con);
        usr_ent_id = usr_ent_id_;
    }
    
    /**
    check if the user, role has the privilege to check in
    */
    public boolean hasCheckinPrivilege(String rol_ext_id) throws SQLException {
	    return true;
    }

    /**
    check if the user, role has the privilege to check out
    */
    public boolean hasCheckoutPrivilege(String rol_ext_id) throws SQLException {
	    return true;
    }

    /**
    check if the user, role has the privilege to renew
    */
    public boolean hasRenewPrivilege(String rol_ext_id) throws SQLException {
	      return true;
    }

    /**
    check if the user, role has the privilege to reserve
    */
    public boolean hasReservePrivilege(String rol_ext_id) throws SQLException {
    	 return true;
    }

    /**
    check if the user, role has the privilege to borrow
    */
    public boolean hasBorrowPrivilege(String rol_ext_id) throws SQLException {
    	 return true;
    }

    /**
    check if the user, role has the privilege to view user history
    */
    public boolean hasUserHistoryPrivilege(String rol_ext_id) throws SQLException {
    	 return true;
    }

    /**
    check if the user, role has the privilege to view copy history
    */
    public boolean hasCopyHistoryPrivilege(String rol_ext_id) throws SQLException {
    	 return true;
    }

    /**
    check if the user, role has the privilege to cancel reserve/borrow
    */
    public boolean hasCancelPrivilege(String rol_ext_id) throws SQLException {
    	 return true;
    }
}