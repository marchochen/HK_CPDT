package com.cw.wizbank.ae.db;

import java.sql.*;
import java.util.Vector;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.ae.db.sql.SqlStatements;
import com.cw.wizbank.accesscontrol.acSite;

public class DbLearningSoln{

    public static final String LSN_TYPE_USER_REGISTRATION = "USER_REGISTRATION";
    public static final String LSN_TYPE_LEARNING_PLAN = "LEARNING_PLAN";

    public long lsn_ent_id;
    public long lsn_itm_id;
    public long lsn_period_id;
    public String lsn_ent_id_lst;
    public String lsn_create_usr_id;
    public Timestamp lsn_create_timestamp;
    public String lsn_upd_usr_id;
    public Timestamp lsn_upd_timestamp;
    public String lsn_type;
    
    public DbLearningSoln() {
        lsn_ent_id = 0;
        lsn_itm_id = 0;
        lsn_period_id = 0;
        lsn_ent_id_lst = null;
        lsn_create_usr_id = null;
        lsn_create_timestamp = null;
        lsn_upd_usr_id = null;
        lsn_upd_timestamp = null;
    }
    /**
    get instances of DbLearningSoln by the input lsn_ent_id and lsn_type
    @param con Connection 
    @param lsn_ent_id user entity id
    @param lsn_type type of plan
    @return array of DbLearningSoln
    */
    public static DbLearningSoln[] getInstance(Connection con, long lsn_ent_id, String lsn_type) throws SQLException {
        PreparedStatement stmt = null;
        Vector v = new Vector();
        DbLearningSoln[] learningSolns = null;
        try {
            stmt = con.prepareStatement(SqlStatements.SQL_GET_LSN_BY_ENT_ID_N_TYPE);
            stmt.setLong(1, lsn_ent_id);
            stmt.setString(2, lsn_type);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                DbLearningSoln lsn = new DbLearningSoln();
                lsn.lsn_ent_id = lsn_ent_id;
                lsn.lsn_type = lsn_type;
                lsn.lsn_itm_id = rs.getLong("lsn_itm_id");
                lsn.lsn_period_id = rs.getLong("lsn_period_id");
                lsn.lsn_ent_id_lst = rs.getString("lsn_ent_id_lst");
                lsn.lsn_create_usr_id = rs.getString("lsn_create_usr_id");
                lsn.lsn_create_timestamp = rs.getTimestamp("lsn_create_timestamp");
                lsn.lsn_upd_usr_id = rs.getString("lsn_upd_usr_id");
                lsn.lsn_upd_timestamp = rs.getTimestamp("lsn_upd_timestamp");
                v.addElement(lsn);
            }
        } finally {
            if(stmt!=null) stmt.close();
        }
        int size = v.size();
        learningSolns = new DbLearningSoln[size];
        for(int i=0; i<size; i++) {
            learningSolns[i] = (DbLearningSoln)v.elementAt(i);
        }
        return learningSolns;
    }

    public void ins(Connection con) throws SQLException {
        
        if(lsn_create_timestamp == null) {
            lsn_create_timestamp = cwSQL.getTime(con);
            lsn_upd_timestamp = lsn_create_timestamp;
        }
        if(lsn_type == null) {
            lsn_type = LSN_TYPE_LEARNING_PLAN;
        }
        
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_ins_learning_soln);
        stmt.setLong(1, lsn_ent_id);
        stmt.setLong(2, lsn_itm_id);

        if (lsn_period_id != 0) {
            stmt.setLong(3, lsn_period_id);
        } else {
            stmt.setNull(3, java.sql.Types.INTEGER);
        }
        
        stmt.setString(4, lsn_ent_id_lst);

        stmt.setString(5, lsn_create_usr_id);
        stmt.setTimestamp(6, lsn_create_timestamp);
        stmt.setString(7, lsn_upd_usr_id);
        stmt.setTimestamp(8, lsn_upd_timestamp);
        stmt.setString(9, lsn_type);
        stmt.setLong(10, 1);
        stmt.executeUpdate();
        stmt.close();
    }
    
    public void upd(Connection con, long ent_id, long site_id) throws SQLException {
        if (lsn_upd_timestamp == null) {
            lsn_upd_timestamp = cwSQL.getTime(con);
        }
        PreparedStatement stmt = null;
        if( acSite.isTargetByPeer( con, site_id ) ){
            stmt = con.prepareStatement(SqlStatements.sql_upd_learning_soln);

            if (lsn_period_id != 0) {
                stmt.setLong(1, lsn_period_id);
            } else {
                stmt.setNull(1, java.sql.Types.INTEGER);
            }
            
            stmt.setString(2, lsn_upd_usr_id);
            stmt.setTimestamp(3, lsn_upd_timestamp);
            stmt.setLong(4, lsn_ent_id);
            stmt.setLong(5, lsn_itm_id);
            stmt.setString(6, "% " + ent_id + " %");
            stmt.setString(7, LSN_TYPE_LEARNING_PLAN);
        } else {
            stmt = con.prepareStatement(SqlStatements.sql_upd_learning_soln_non_peer);

            if (lsn_period_id != 0) {
                stmt.setLong(1, lsn_period_id);
            } else {
                stmt.setNull(1, java.sql.Types.INTEGER);
            }
            
            stmt.setString(2, lsn_upd_usr_id);
            stmt.setTimestamp(3, lsn_upd_timestamp);
            stmt.setLong(4, lsn_ent_id);
            stmt.setLong(5, lsn_itm_id);            
            stmt.setString(6, LSN_TYPE_LEARNING_PLAN);
        }
        stmt.executeUpdate();
        stmt.close();
    }

    public void disable(Connection con, long ent_id, long site_id, Timestamp curr_lsn_upd_timestamp, Timestamp last_lsn_upd_timestamp) throws SQLException {
        PreparedStatement stmt = null;
        if( acSite.isTargetByPeer( con, site_id ) ){
            stmt = con.prepareStatement(SqlStatements.sql_disable_learning_soln);
            stmt.setTimestamp(1, curr_lsn_upd_timestamp);
            stmt.setLong(2, lsn_ent_id);
            stmt.setLong(3, lsn_itm_id);
            stmt.setString(4, "% " + ent_id + " %");
            stmt.setString(5, LSN_TYPE_LEARNING_PLAN);
            stmt.setTimestamp(6, last_lsn_upd_timestamp);
        }else {
            stmt = con.prepareStatement(SqlStatements.sql_disable_learning_soln_non_peer);
            stmt.setTimestamp(1, curr_lsn_upd_timestamp);
            stmt.setLong(2, lsn_ent_id);
            stmt.setLong(3, lsn_itm_id);
            stmt.setString(4, LSN_TYPE_LEARNING_PLAN);
            stmt.setTimestamp(5, last_lsn_upd_timestamp);
        }
        stmt.executeUpdate();
        stmt.close();
    }

    public void enable(Connection con, long ent_id, long site_id, Timestamp curr_lsn_upd_timestamp, Timestamp last_lsn_upd_timestamp) throws SQLException {
        PreparedStatement stmt = null;
        if( acSite.isTargetByPeer( con, site_id ) ){
            stmt = con.prepareStatement(SqlStatements.sql_enable_learning_soln);
            stmt.setTimestamp(1, curr_lsn_upd_timestamp);
            stmt.setLong(2, lsn_ent_id);
            stmt.setLong(3, lsn_itm_id);
            stmt.setString(4, "% " + ent_id + " %");
            stmt.setString(5, LSN_TYPE_LEARNING_PLAN);
            stmt.setTimestamp(6, last_lsn_upd_timestamp);
        }else {
            stmt = con.prepareStatement(SqlStatements.sql_enable_learning_soln_non_peer);
            stmt.setTimestamp(1, curr_lsn_upd_timestamp);
            stmt.setLong(2, lsn_ent_id);
            stmt.setLong(3, lsn_itm_id);
            stmt.setString(4, LSN_TYPE_LEARNING_PLAN);
            stmt.setTimestamp(5, last_lsn_upd_timestamp);
        }
        stmt.executeUpdate();
        stmt.close();
    }

    public void del(Connection con, long ent_id, long site_id) throws SQLException {
        del(con, null, ent_id, site_id, 0);
    }

    public void del(Connection con, String usr_id, long ent_id, long site_id, long upd_usr_ent_id) throws SQLException {
        PreparedStatement stmt = null;

        String query_suffix = "";
        if (upd_usr_ent_id != 0) {
            if (lsn_ent_id == upd_usr_ent_id) { 
                // delete the learning solution that is added by the learner himself
                query_suffix += " AND lsn_create_usr_id = ? ";
            }
            else {
                // delete the learning solution that is recommended by others
                query_suffix += " AND lsn_create_usr_id <> ? ";
            }
        }
        if( acSite.isTargetByPeer( con, site_id ) ){
            stmt = con.prepareStatement(SqlStatements.sql_del_learning_soln + query_suffix);
            stmt.setLong(1, lsn_ent_id);
            stmt.setLong(2, lsn_itm_id);
            stmt.setString(3, "% " + ent_id + " %");
            stmt.setString(4, LSN_TYPE_LEARNING_PLAN);
            if (upd_usr_ent_id != 0) {
                stmt.setString(5, usr_id);
            }
        }else {
            stmt = con.prepareStatement(SqlStatements.sql_del_learning_soln_non_peer + query_suffix);
            stmt.setLong(1, lsn_ent_id);
            stmt.setLong(2, lsn_itm_id);
            stmt.setString(3, LSN_TYPE_LEARNING_PLAN);
            if (upd_usr_ent_id != 0) {
                stmt.setString(4, usr_id);
            }
        }
        stmt.executeUpdate();
        stmt.close();
    }
    
    public static void delByItem(Connection con, long itm_id) throws SQLException {
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(SqlStatements.sql_del_learning_soln_by_item);
            stmt.setLong(1, itm_id);
            stmt.executeUpdate();
        } finally {
            if(stmt!=null)stmt.close();
        }
    }
    
    public static boolean isExist(Connection con, long itm_id, long usr_ent_id) throws SQLException {
        PreparedStatement stmt = null;
        boolean has_record = false;
        try {
            stmt = con.prepareStatement("select lsn_ent_id from aeLearningSoln where lsn_ent_id = ? and  lsn_itm_id = ? ");
            stmt.setLong(1, usr_ent_id);
            stmt.setLong(2, itm_id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                has_record = true;
            }
        } finally {
            if(stmt!=null)stmt.close();
        }
        return has_record;
    }
}