/**
 * Facility maintance
 *
 * @author  Emily
 * @version 1.1
 * @history created     11 Dec. 2001    by Emily
 *          update      19 Dec. 2001    by Emily
 */
package com.cw.wizbank.db.view;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Vector;
import java.util.Hashtable;

import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.db.sql.*;
import com.cwn.wizbank.utils.CommonLog;

public class ViewFmFacility {
    /**
     * CLOB column
     * Table:       fmFacility
     * Column:      fac_add_xml
     * Nullable:    YES
     */
	public final static String FAC_STATUS_ON = "ON";
	
    private final static boolean DEBUG = false;

    public final static String SMSG_FMT_UPDATE_FAILED = "FMT001";
    public final static String SMSG_FMT_DELETE_FAILED = "FMT002";
    public final static String SMSG_FMT_DUPLICATE     = "FMT008";
    private Connection con = null;

    /**
     * constructor: check connection
     */
    public ViewFmFacility(Connection con) throws SQLException {
        if (con == null)
            throw new SQLException("connection not available");
        else
            this.con = con;
    }

    public int insert(Hashtable facility) throws SQLException, cwSysMessage {
        int fac_ftp_id = ((Integer)facility.get("parent_ftp_id")).intValue();
        String fac_title = ((String)facility.get("fac_title"));
        String fac_desc = ((String)facility.get("fac_desc"));
        String fac_status = (String)facility.get("status");
        String fac_remarks = ((String)facility.get("fac_remarks"));
        String fac_url = ((String)facility.get("fac_url"));
        String fac_url_type = (String)facility.get("fac_url_type");
        String fac_add_xml = ((String)facility.get("fac_add_xml"));
        int fac_owner_ent_id = ((Integer)facility.get("owner_ent_id")).intValue();
        String fac_create_usr_id = (String)facility.get("user_id");
        double fac_fee = 0;
        if (facility.containsKey("fac_fee")) {
        	fac_fee = ((Double)facility.get("fac_fee")).doubleValue();
        }
        // NOT used at this phase
        int fac_loc_id = 0;
        int fac_order = 0;

        if (fac_ftp_id < 1 ||
            fac_title == null ||
            fac_status == null ||
            fac_owner_ent_id < 1 ||
            fac_create_usr_id == null)
            throw new SQLException("Can not insert new facility.");

        // check duplicate - fac_title
        if (this.checkDuplicate(fac_title, fac_owner_ent_id) != Integer.MIN_VALUE)
            throw new cwSysMessage(this.SMSG_FMT_DUPLICATE, fac_title);

        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_ins_facility, PreparedStatement.RETURN_GENERATED_KEYS);
        Timestamp fac_create_timestamp = null;
        fac_create_timestamp = cwSQL.getTime(this.con);

        int i = 1;
        stmt.setInt(i++, fac_ftp_id);
        stmt.setString(i++, fac_title);
        stmt.setString(i++, fac_desc);
        stmt.setString(i++, fac_remarks);
        stmt.setString(i++, fac_url);
        stmt.setString(i++, fac_url_type);
        // << BEGIN for oracle migration!
        //stmt.setString(i++, fac_add_xml);
        // >> END
        stmt.setString(i++, fac_status);
        stmt.setInt(i++, fac_loc_id);
        stmt.setInt(i++, fac_order);
        stmt.setInt(i++, fac_owner_ent_id);
        stmt.setTimestamp(i++, fac_create_timestamp);
        stmt.setString(i++, fac_create_usr_id);
        stmt.setTimestamp(i++, fac_create_timestamp);
        stmt.setString(i++, fac_create_usr_id);
        stmt.setDouble(i++, fac_fee);
        if (stmt.executeUpdate() != 1) {
            con.rollback();
            throw new SQLException("Failed to insert new facility.");
        }
        int fac_id = (int)cwSQL.getAutoId(con, stmt, "fmFacility", "fac_id");
        stmt.close();

        // << BEGIN for oracle migration!
        this.updFacilityAdditionalXML(con, fac_id, fac_add_xml);
        // >> END

        return fac_id;
    }

    public void update(Hashtable facility) throws SQLException, cwSysMessage {
        int fac_id = ((Integer)facility.get("fac_id")).intValue();
        String fac_title = (String)facility.get("fac_title");
        String fac_desc = (String)facility.get("fac_desc");
        String fac_status = (String)facility.get("status");
        String fac_remarks = (String)facility.get("fac_remarks");
        String fac_url = (String)facility.get("fac_url");
        String fac_url_type = (String)facility.get("fac_url_type");
        String fac_add_xml = (String)facility.get("fac_add_xml");
        String fac_update_usr_id = (String)facility.get("user_id");
        Timestamp fac_upd_timestamp = (Timestamp)facility.get("fac_upd_timestamp");
        int fac_owner_ent_id = ((Integer)facility.get("owner_ent_id")).intValue();
        double fac_fee = 0;
        if (facility.containsKey("fac_fee")) {
        	fac_fee = ((Double)facility.get("fac_fee")).doubleValue();
        }
        // NOT used at this phase
        int fac_loc_id = 0;
        int fac_order = 0;

        if (fac_id < 1 ||
            fac_update_usr_id == null ||
            fac_upd_timestamp == null ||
            fac_title == null ||
            fac_status == null)
            throw new SQLException("Can not update the facility.");

        // check duplicate - fac_title
        int tmp_fac_id = this.checkDuplicate(fac_title, fac_owner_ent_id);
        if (!(tmp_fac_id == Integer.MIN_VALUE || tmp_fac_id == fac_id))
            throw new cwSysMessage(this.SMSG_FMT_DUPLICATE, fac_title);
        StringBuffer sql = new StringBuffer();
        sql.append("UPDATE fmFacility SET fac_title = ?, fac_desc = ?, fac_remarks = ?, " );
        if (fac_url_type == null || fac_url_type.equalsIgnoreCase("UPLOAD")) {
        	sql.append(" fac_url =? , ");
        }
        sql.append(" fac_url_type = ?, fac_status = ?, fac_loc_id = ?, fac_order = ?, fac_upd_timestamp = ?, fac_upd_usr_id = ?, fac_fee = ? WHERE fac_id = ?");
        PreparedStatement stmt = con.prepareStatement(sql.toString());
        Timestamp facUpdTimestamp = null;
        facUpdTimestamp = cwSQL.getTime(this.con);

        int i = 1;
        stmt.setString(i++, fac_title);
        stmt.setString(i++, fac_desc);
        stmt.setString(i++, fac_remarks);
        if (fac_url_type == null || fac_url_type.equalsIgnoreCase("UPLOAD")) {
        	stmt.setString(i++, fac_url);
        }
        stmt.setString(i++, fac_url_type);
        // << BEGIN for oracle migration!
        //stmt.setString(i++, fac_add_xml);
        // >> END
        stmt.setString(i++, fac_status);
        stmt.setInt(i++, fac_loc_id);
        stmt.setInt(i++, fac_order);
        stmt.setTimestamp(i++, facUpdTimestamp);
        stmt.setString(i++, fac_update_usr_id);
        stmt.setDouble(i++, fac_fee);
        stmt.setInt(i++, fac_id);
        //stmt.setTimestamp(i++, fac_upd_timestamp);
        if (stmt.executeUpdate() != 1) {
            con.rollback();
            throw new cwSysMessage(this.SMSG_FMT_UPDATE_FAILED);
        }
        stmt.close();

        // << BEGIN for oracle migration!
        this.updFacilityAdditionalXML(con, fac_id, fac_add_xml);
        // >> END
    }

    /**
     * soft delete the faiclity
     * @param fac_id
     * @param fac_upd_timestamp
     * @param usr_id
     * @throws SQLException
     * @throws cwSysMessage
     */
    public void changeStatus(int fac_id, Timestamp fac_upd_timestamp, String usr_id)
        throws SQLException, cwSysMessage {
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_upd_facility_status);
        Timestamp facUpdTimestamp = null;
        facUpdTimestamp = cwSQL.getTime(this.con);

        int i = 1;
        stmt.setTimestamp(i++, facUpdTimestamp);
        stmt.setString(i++, usr_id);
        stmt.setInt(i++, fac_id);
        stmt.setTimestamp(i++, fac_upd_timestamp);

        if (stmt.executeUpdate() != 1) {
            con.rollback();
            throw new cwSysMessage(ViewFmFacility.SMSG_FMT_DELETE_FAILED);
        }
        stmt.close();
    }

    /**
     * remove the facility
     * @param fac_id
     * @param fac_upd_timestamp
     * @param usr_id
     * @throws SQLException
     * @throws cwSysMessage
     */
    public void delete(int fac_id, Timestamp fac_upd_timestamp)
        throws SQLException, cwSysMessage {
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_del_facility);

        int i = 1;
        stmt.setInt(i++, fac_id);
        stmt.setTimestamp(i++, fac_upd_timestamp);
        if (stmt.executeUpdate() != 1) {
            con.rollback();
            throw new cwSysMessage(ViewFmFacility.SMSG_FMT_DELETE_FAILED);
        }
        stmt.close();
    }

    /**
     * get all facilities with specified facility type
     * @param owner_id
     * @param type_id
     * @return
     * @throws SQLException
     */
    public Hashtable[] listFacility(int owner_id, int type_id) throws SQLException {
        int i = 1;
        PreparedStatement stmt = null;
        if (type_id == Integer.MIN_VALUE) {
            stmt = con.prepareStatement(SqlStatements.sql_get_all_facility);
            stmt.setInt(i++, owner_id);
            stmt.setInt(i++, owner_id);
        } else {
            stmt = con.prepareStatement(SqlStatements.sql_get_all_facility_by_type);
            stmt.setInt(i++, owner_id);
            stmt.setInt(i++, type_id);
            stmt.setInt(i++, owner_id);
            stmt.setInt(i++, type_id);
        }

        ResultSet rs = stmt.executeQuery();

        Vector result = new Vector();
        Hashtable record = null;
        while (rs.next()) {
            record = new Hashtable();
            // fac_id, fac_status, fac_title, fac_ftp_id, rom_ftp_id sub_type_id
            record.put("fac_id", rs.getString("fac_id"));
            record.put("sub_type_id", rs.getString("sub_type_id"));
            record.put("fac_ftp_id", rs.getString("fac_ftp_id"));
            record.put("fac_title", rs.getString("fac_title"));
            record.put("fac_status", rs.getString("fac_status"));
            result.addElement(record);
        }
        stmt.close();

        if (this.DEBUG)
        	CommonLog.debug(String.valueOf(result.size()));

        Hashtable[] resultArray = new Hashtable[result.size()];
        return (Hashtable[])result.toArray(resultArray);
    }

    public int getFacilityType(int fac_id) throws SQLException {
        if (fac_id < 1)
            throw new SQLException("Can not get the facility type");

        int type_id = 0;
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_facility_type);
        stmt.setInt(1, fac_id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next())
            type_id = rs.getInt("fac_ftp_id");

        stmt.close();
        return type_id;
    }

    private int checkDuplicate(String fac_title, int owner_ent_id) throws SQLException {
        int fac_id = Integer.MIN_VALUE;
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_check_duplicate);
        stmt.setInt(1, owner_ent_id);
        stmt.setString(2, fac_title);
        ResultSet rs = stmt.executeQuery();

        if (rs.next())
            fac_id = rs.getInt("fac_id");
        stmt.close();
        return fac_id;
    }

    // << BEGIN for oracle migration!
    private void updFacilityAdditionalXML(Connection con, int id, String add_xml)
        throws SQLException {
        // Update fac_add_xml
        // construct the condition
        String condition = "fac_id = " + id;
        // construct the column & value
        String[] columnName = new String[1];
        String[] columnValue = new String[1];
        columnName[0] = "fac_add_xml";
        columnValue[0] = add_xml;
        cwSQL.updateClobFields(con, "fmFacility", columnName, columnValue, condition);
    }
    // >> END
}