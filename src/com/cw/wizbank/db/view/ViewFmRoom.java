package com.cw.wizbank.db.view;

import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Hashtable;
import java.util.Enumeration;

import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.db.sql.*;
import com.cwn.wizbank.utils.CommonLog;

public class ViewFmRoom extends FmFacilityTemplate {
    /**
     * CLOB column
     * Table:       fmFacility
     * Column:      fac_add_xml
     * Nullable:    YES
     */
    private final static boolean DEBUG = false;

    public int insert(Hashtable room) throws SQLException, cwSysMessage {
        int rom_ftp_id = ((Integer)room.get("fac_type")).intValue();

        // check the type of the room
        if (rom_ftp_id < 1)
            throw new SQLException("Can not insert new room.");

        /**
         * insert into the facility
         */
        ViewFmFacility facility = new ViewFmFacility(super.con);
        int rom_fac_id = 0;
        rom_fac_id = facility.insert(room);

        /**
         * insert into the room
         */
        PreparedStatement stmt = super.con.prepareStatement(SqlStatements.sql_ins_room);
        stmt.setInt(1, rom_fac_id);
        stmt.setInt(2, rom_ftp_id);
        if (stmt.executeUpdate() != 1) {
            super.con.rollback();
            throw new SQLException("Failed to insert new room.");
        }
        stmt.close();

        return rom_fac_id;
    }

    public void update(Hashtable room) throws SQLException, cwSysMessage {
        int rom_fac_id = ((Integer)room.get("fac_id")).intValue();

        if (rom_fac_id < 1)
            throw new SQLException("Can not update the room.");
        /**
         * update the facility
         */
        ViewFmFacility facility = new ViewFmFacility(super.con);
        facility.update(room);

        /**
         * update the room
         * NO data to be updated for the equipment
         */
/*      if (this.rom_fac_id < 1 ||
            this.rom_ftp_id < 1 ||
            this.rom_capacity == -1)
            throw new SQLException("Can not update the room.");

        PreparedStatement stmt = super.con.prepareStatement(SqlStatements.sql_upd_room);
        stmt.setInt(1, this.rom_ftp_id);
        stmt.setInt(2, this.rom_capacity);
        stmt.setInt(3, this.rom_fac_id);
        if (stmt.executeUpdate() != 1) {
            super.con.rollback();
            throw new SQLException("Failed to update the room.");
        }*/
    }

    public void delete(int rom_fac_id, Timestamp upd_timestamp, String usr_id)
        throws SQLException, cwSysMessage {
        if (rom_fac_id < 1)
            throw new SQLException("Can not delete the room.");

        // check whether there are facility schedule existed
        PreparedStatement stmt = super.con.prepareStatement(SqlStatements.sql_check_facility);
        stmt.setInt(1, rom_fac_id);
        ResultSet rs = stmt.executeQuery();
        Hashtable record = new Hashtable();

        ViewFmFacility facility = new ViewFmFacility(super.con);
        if (!rs.next()) {
            /**
             * the facility is not in use, remove it!
             */
            stmt = super.con.prepareStatement(SqlStatements.sql_del_room);
            stmt.setInt(1, rom_fac_id);
            if (stmt.executeUpdate() != 1) {
                con.rollback();
                throw new cwSysMessage(ViewFmFacility.SMSG_FMT_DELETE_FAILED);
            }
            facility.delete(rom_fac_id, upd_timestamp);
        } else
            /**
             * the facility is in use, soft delete it!
             */
            facility.changeStatus(rom_fac_id, upd_timestamp, usr_id);
        stmt.close();
    }

    public Hashtable get(int rom_fac_id) throws SQLException {
        PreparedStatement stmt = super.con.prepareStatement(SqlStatements.sql_get_room);
        stmt.setInt(1, rom_fac_id);
        ResultSet rs = stmt.executeQuery();
        Hashtable record = new Hashtable();
        if (rs.next()) {
            record.put("fac_id", rs.getString("rom_fac_id"));
            record.put("sub_ftp_id", rs.getString("rom_ftp_id"));
            record.put("ftp_title", rs.getString("ftp_title_xml"));
            record.put("ftp_xsl_prefix", rs.getString("ftp_xsl_prefix"));
//          record.put("rom_capacity", rs.getString("rom_capacity"));
            record.put("fac_ftp_id", rs.getString("fac_ftp_id"));
            record.put("fac_title", rs.getString("fac_title"));
            if (rs.getString("fac_desc") != null)
                record.put("fac_desc", rs.getString("fac_desc"));
            if (rs.getString("fac_remarks") != null)
                record.put("fac_remarks", rs.getString("fac_remarks"));
            record.put("fac_status", rs.getString("fac_status"));
            if (rs.getString("fac_url") != null)
                record.put("fac_url", rs.getString("fac_url"));
            if (rs.getString("fac_url_type") != null)
                record.put("fac_url_type", rs.getString("fac_url_type"));
            // << BEGIN for oracle migration!
            //if (rs.getString("fac_add_xml") != null)
            //    record.put("fac_add_xml", rs.getString("fac_add_xml"));
            String tmp_add_xml = cwSQL.getClobValue(rs, "fac_add_xml");
            if (tmp_add_xml != null)
                record.put("fac_add_xml", tmp_add_xml);
            // >> END
//          record.put("fac_loc_id", rs.getString("fac_loc_id"));
//          record.put("fac_order", rs.getString("fac_order"));
            record.put("fac_owner_ent_id", rs.getString("fac_owner_ent_id"));
            record.put("fac_create_timestamp", rs.getTimestamp("fac_create_timestamp"));
            record.put("fac_create_usr_id", rs.getString("fac_create_usr_id"));
            record.put("fac_upd_timestamp", rs.getTimestamp("fac_upd_timestamp"));
            record.put("fac_upd_usr_id", rs.getString("fac_upd_usr_id"));
            record.put("fac_fee", new Double(rs.getDouble("fac_fee")));
        } else {
            throw new SQLException("Fails to get the facility with the specified id.");
        }
        if (this.DEBUG) {
            Enumeration enumeration = record.keys();
            while (enumeration.hasMoreElements()) {
                String column = (String)enumeration.nextElement();
                CommonLog.debug(column + " = " + record.get(column));
            }
        }
        stmt.close();
        return record;
    }
}