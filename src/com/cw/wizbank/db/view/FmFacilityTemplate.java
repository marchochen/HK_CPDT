package com.cw.wizbank.db.view;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Hashtable;
import java.sql.Connection;

import com.cw.wizbank.util.cwSysMessage;

public abstract class FmFacilityTemplate {

    protected Connection con = null;

    public void setConnection(Connection con) throws SQLException {
        if (con == null)
            throw new SQLException("connection not available");
        else
            this.con = con;
    }

    public abstract int insert(Hashtable facility) throws SQLException, cwSysMessage;

    public abstract void update(Hashtable facility) throws SQLException, cwSysMessage;

    public abstract void delete(int fac_id, Timestamp upd_timestamp, String usr_id) throws SQLException, cwSysMessage;

    public abstract Hashtable get(int facility_id) throws SQLException;
}