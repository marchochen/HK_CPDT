package com.cw.wizbank.fm;

import java.util.Vector;
import java.util.Enumeration;
import java.util.Hashtable;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.db.view.*;
/**
 * class FMAuditTrail(Connection con);
 * for access control
 * Usage Tips:
 * 1.before getRsvCreateUsrID(),getRsvOwnerEntID(), setRsvAuditTrail(int rsv_id) first
 * 2.before getFshCreateUsrID(),getFshOwnerEntID(), setFshAuditTrail(int fsh_fac_id,Timestamp fsh_start_time) first
 * 3.before getFacCreateUsrID(),getFacOwnerEntID(), setFacAuditTrail(int fac_id) first
 * <p>Title: fm</p>
 * <p>Description: facility management system</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: cyberwisdom</p>
 * @author ro
 * @version 1.0
 */

public class FMAuditTrail {

    private Connection con = null;
    private Vector rsvAuditTrail = new Vector();
    private Vector fshAuditTrail = new Vector();
    private Vector facAuditTrail = new Vector();

    private String rsv_create_usr_id = null;
    private int rsv_owner_ent_id = 0;
    /* DENNIS BEGIN */    
    private Timestamp rsv_upd_timestamp = null;
    /* DENNIS END */
    private String fsh_create_usr_id = null;
    private int fsh_owner_ent_id = 0;
    private String fac_create_usr_id = null;
    private int fac_owner_ent_id = 0;

    /**
     * construcor FMAuditTrail(Connection con)
     * @param con
     * @throws SQLException
     */
    public FMAuditTrail(Connection con) throws SQLException {
        if (con == null)
            throw new SQLException("connection not available");
        else
            this.con = con;
    }
    //
    public String getFacCreateUsrID() {
        return fac_create_usr_id;
    }
    public int getFacOwnerEntID() {
        return fac_owner_ent_id;
    }

    public String getRsvCreateUsrID() {
        return rsv_create_usr_id;
    }
    public int getRsvOwnerEntID() {
        return rsv_owner_ent_id;
    }
   /* DENNIS BEGIN */
    public Timestamp getRsvUpdTimestamp() {
        return  rsv_upd_timestamp;
    }
    /* DENNIS END */
    public String getFshCreateUsrID() {
        return fsh_create_usr_id;
    }
    public int getFshOwnerEntID() {
        return fsh_owner_ent_id;
    }
    // for rsvAuditTrail
    /**
     *getRsvAuditTrail
     * @return
     */
    public Vector getRsvAuditTrail() {
        return rsvAuditTrail;
    }
    public Vector getFacAuditTrail() {
        return facAuditTrail;
    }
    public void setFacAuditTrail(int fac_id)
           throws SQLException ,cwSysMessage {
        ViewFmReservation viewRsv = new ViewFmReservation(con);
        facAuditTrail = viewRsv.getFacAuditTrail(fac_id);
        setFacTrail();
    }
    /**
     * setRsvAuditTrail
     * @param rsv_id
     * @return
     */
    public void setRsvAuditTrail(int rsv_id)
           throws SQLException ,cwSysMessage {
        ViewFmReservation viewRsv = new ViewFmReservation(con);
        rsvAuditTrail = viewRsv.getRsvAuditTrail(rsv_id);
        setRsvTrail();
    }
    // for fshAuditTrail
    /**
     * getFshAuditTrail
     * @return Vector
     */
    public Vector getFshAuditTrail() {
        return fshAuditTrail;
    }
    /**
     *
     * @param fsh_id
     * @param fsh_start_time
     * @return
     */
    public void setFshAuditTrail(int fsh_fac_id,Timestamp fsh_start_time)
           throws SQLException ,cwSysMessage  {
        ViewFmFacilitySchedule viewFsh = new ViewFmFacilitySchedule(con);
        fshAuditTrail = viewFsh.getFshAuditTrail(fsh_fac_id,fsh_start_time);
        setFshTrail();
    }
    private void setFacTrail() {
        Enumeration e = facAuditTrail.elements();
        Hashtable h = new Hashtable();
        while (e.hasMoreElements()) {
            h = (Hashtable)(e.nextElement());
            fac_create_usr_id = h.get("fac_create_usr_id").toString();
            fac_owner_ent_id = (int)(((Integer)(h.get("fac_owner_ent_id"))).intValue());
            // e.nextElement();
        }
    }
    private void setRsvTrail() {
        Enumeration e = rsvAuditTrail.elements();
        Hashtable h = new Hashtable();
        while (e.hasMoreElements()) {
            h = (Hashtable)(e.nextElement());
            rsv_create_usr_id = h.get("rsv_create_usr_id").toString();
            rsv_owner_ent_id = (int)(((Integer)(h.get("rsv_owner_ent_id"))).intValue());
            /* DENNIS BEGIN */
            rsv_upd_timestamp = (Timestamp) h.get("rsv_upd_timestamp");
            /* DENNIS END */
            // e.nextElement();
        }
    }
    private void setFshTrail() {
        Enumeration e = fshAuditTrail.elements();
        Hashtable h = new Hashtable();
        while (e.hasMoreElements()) {
            h = (Hashtable)(e.nextElement());
            fsh_create_usr_id = h.get("fsh_create_usr_id").toString();
            fsh_owner_ent_id = (int)(((Integer)(h.get("fsh_owner_ent_id"))).intValue());
            // e.nextElement();
        }
    }

}