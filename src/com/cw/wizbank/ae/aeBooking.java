package com.cw.wizbank.ae;

import java.sql.*;
import java.util.Vector;
import java.util.Calendar;
import java.io.IOException;
import javax.servlet.http.HttpSession;

import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.qdbException;

// class for handling booking of item slots (2001.04.22 wai)
public class aeBooking {
    // session keys
    private final String SLOT_BOOKED_SESS  = "SLOT_BOOKED";
    private final String SLOT_APPXML_SESS  = "SLOT_APPXML";
    private final String SLOT_APPEXT1_SESS = "SLOT_APPEXT1";
    private final String SLOT_APPEXT3_SESS = "SLOT_APPEXT3";
    // shared constants
    public static final String ITEM_TYPE_UNIT = "BOOKUNIT";
    public static final String ITEM_TYPE_SLOT = "BOOKSLOT";
    public static final String SLOT_AVAIL     = "AVAILABLE";
    public static final String SLOT_PEND      = "PENDING";
    public static final String SLOT_BOOKED    = "CONFIRMED";
    public static final String SLOT_DUP       = "DUPLICATED";
    public static final String SLOT_ERR       = "INVALID";
    // validation constants
    private final String SPECIAL_USER = "pnsagency";
    private final int RANGE_START_UNIT = Calendar.WEEK_OF_MONTH;
    private final int RANGE_START_AMT  = 4;
    private final int RANGE_END_UNIT   = Calendar.MONTH;
    private final int RANGE_END_AMT    = 3;
    // code table constants
    private final String CODE_SUPPLIER = "SUPPLIER";
    private final String CODE_PRODUCTTYPE = "PRODUCTTYPE";
    // constants
    private final String BLANK = "";
    // sqls
    private final String sql_get_unit_ =
        "SELECT itm_title, itm_ext1, itm_owner_ent_id FROM aeItem WHERE itm_id = ? AND itm_status = ? AND itm_type = ? ";
    private final String sql_get_slot_ =
        "SELECT itm_id FROM aeItemRelation, aeItem WHERE ire_child_itm_id = itm_id AND ire_parent_itm_id = ? AND itm_type = ? AND itm_eff_start_datetime = ? ";
    private final String sql_get_available_slot_app_ =
        "SELECT app_id FROM aeApplication WHERE app_itm_id = ? AND (app_status = 'Pending' OR app_status = 'Waiting' OR app_status = 'Confirmed' OR app_status = 'Pending Cancel') ";
    private final String sql_get_confirmed_slot_app_ =
        "SELECT app_id FROM aeApplication WHERE app_itm_id = ? AND (app_status = 'Confirmed' OR app_status = 'Pending Cancel') ";
    private final String sql_get_booked_app_ =
        "SELECT app_id FROM aeApplication WHERE app_itm_id = ? AND app_ent_id = ? AND app_status != 'Cancelled' AND app_status != 'Withdrawn'";
    private final String sql_get_itm_tpl_ =
        "SELECT tpl_xml FROM aeItemTemplate, aeTemplateType, aeTemplate WHERE itp_itm_id = ? AND ttp_title = ? AND itp_ttp_id = ttp_id AND itp_tpl_id = tpl_id ";
    private final String sql_get_tnd_ =
        "SELECT tnd_title, tnd_ext2 FROM aeTreeNode WHERE tnd_id = ? AND tnd_status = ? ";
    private final String sql_get_code_id_ =
        "SELECT ctb_id FROM CodeTable WHERE ctb_type = ? AND ctb_title = ? ";
    private final String sql_get_app_itm_producttype_ =
        "SELECT bookunit.itm_ext1 "
      + "FROM   aeApplication, aeItem bookslot, aeItemRelation, aeItem bookunit "
      + "WHERE  app_id = ? AND app_itm_id = bookslot.itm_id AND bookslot.itm_type = ? AND bookslot.itm_id = ire_child_itm_id AND ire_parent_itm_id = bookunit.itm_id AND bookunit.itm_type = ? ";
    private final String sql_upd_app_ext3_ =
        "UPDATE aeApplication SET app_ext3 = ?, app_upd_timestamp = ?, app_upd_usr_id = ? WHERE app_id = ? AND app_upd_timestamp = ? ";
//    private final String sql_upd_app_xml_ =
//        "SELECT app_xml FROM aeApplication WHERE app_id = ? FOR UPDATE ";

    // database connection
    private Connection con;
    // array of slots to be booked
    private aeBookSlot slots[];
    // application details XML
    private String appXML;
    // extension column (supplier name)
    private String appExt1;
    // extension column (product type)
    private String appExt3;
    // id of application
    private int appID;

    // initialize the slots data object from raw parameters
    public aeBooking(Connection inCon, int inUnitID[], Timestamp inStartTime[], int inParentNodeID[], String inAppXML) {
        this.con = inCon;
        this.appXML = inAppXML;

        int slotSize = inUnitID.length;
        slots = new aeBookSlot[slotSize];
        for (int i = 0; i < slotSize; i++) {
            slots[i] = new aeBookSlot();
            slots[i].unitID = inUnitID[i];
            if (inParentNodeID.length > i)
                slots[i].unitParentNodeID = inParentNodeID[i];
            if (inStartTime.length > i)
                slots[i].slotStartTime = inStartTime[i];
            slots[i].slotStatus = SLOT_ERR;
        }
    }

    // initialize the slots data object from existing slots and existing application XML
    public aeBooking(Connection inCon, HttpSession inSess) {
        this.con     = inCon;
        // take it only if it is really exists in the session
        Object inObj = inSess.getAttribute(SLOT_BOOKED_SESS);
        if (inObj != null)
            this.slots = (aeBookSlot[])inObj;
        else
            this.slots = new aeBookSlot[0];
        this.appXML  = (String)inSess.getAttribute(SLOT_APPXML_SESS);
        this.appExt1 = (String)inSess.getAttribute(SLOT_APPEXT1_SESS);
        this.appExt3 = (String)inSess.getAttribute(SLOT_APPEXT3_SESS);
    }

    public aeBooking(Connection inCon) {
        this.con = inCon;
    }

    // validate each slots
    public void validateSlots(String inUsrID) throws SQLException {
        PreparedStatement stmt1 = con.prepareStatement(sql_get_unit_);
        PreparedStatement stmt2 = con.prepareStatement(sql_get_slot_);
        PreparedStatement stmt3 = con.prepareStatement(sql_get_available_slot_app_);
        PreparedStatement stmt4 = con.prepareStatement(sql_get_confirmed_slot_app_);
        PreparedStatement stmt5 = con.prepareStatement(sql_get_tnd_);
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        ResultSet rs3 = null;
        ResultSet rs4 = null;
        ResultSet rs5 = null;
        int col = 0;

        // get the valid range of dates for slots
        Calendar thisDate    = Calendar.getInstance();
        thisDate.setTime(getTime());
        Calendar rangeStart = (Calendar)thisDate.clone();
        Calendar rangeEnd   = (Calendar)thisDate.clone();
        rangeStart.add(RANGE_START_UNIT, RANGE_START_AMT);
        rangeEnd.add(RANGE_END_UNIT, RANGE_END_AMT);

        for (int i = 0; i < slots.length; i++) {
            // check if the unit is valid (default is invalid)
            slots[i].unitValid = false;
            if (slots[i].unitID > 0) {
                col = 1;
                stmt1.setInt(col++, slots[i].unitID);
                stmt1.setString(col++, aeItem.ITM_STATUS_ON);
                stmt1.setString(col++, ITEM_TYPE_UNIT);
                rs1 = stmt1.executeQuery();
                if (rs1.next()) {
                    col = 1;
                    slots[i].unitTitle   = rs1.getString(col++);
                    if (rs1.wasNull()) slots[i].unitTitle = BLANK;
                    slots[i].unitExt1    = rs1.getString(col++);
                    if (rs1.wasNull()) slots[i].unitExt1 = BLANK;
                    slots[i].unitType    = ITEM_TYPE_UNIT;
                    slots[i].unitOwnerID = rs1.getInt(col++);
                    slots[i].unitValid   = true;
                }
            }
            // check if the slot is existing (default is not existing)
            slots[i].slotID = 0;
            if (slots[i].unitValid && slots[i].slotStartTime != null) {
                col = 1;
                stmt2.setInt(col++, slots[i].unitID);
                stmt2.setString(col++, ITEM_TYPE_SLOT);
                stmt2.setTimestamp(col++, slots[i].slotStartTime);
                rs2 = stmt2.executeQuery();
                col = 1;
                if (rs2.next()) slots[i].slotID = rs2.getInt(col++);
            }
            // check the slot status (default is error)
            slots[i].slotStatus = SLOT_ERR;
            // slot start time has to be within valid range(except for a special user[hardcode!!!])
            boolean validRange = true;
            if (!inUsrID.equals(SPECIAL_USER) && slots[i].unitValid && slots[i].slotStartTime != null) {
                thisDate.setTime(slots[i].slotStartTime);
                if (thisDate.before(rangeStart) || thisDate.after(rangeEnd))
                    validRange = false;
            }
            if (validRange && slots[i].unitValid && slots[i].slotStartTime != null) {
                if (slots[i].slotID == 0) {
                    slots[i].slotStatus = SLOT_AVAIL;
                } else {
                    col = 1;
                    stmt3.setInt(col++, slots[i].slotID);
                    rs3 = stmt3.executeQuery();
                    if (!rs3.next()) {
                        slots[i].slotStatus = SLOT_AVAIL;
                    } else {
                        col = 1;
                        stmt4.setInt(col++, slots[i].slotID);
                        rs4 = stmt4.executeQuery();
                        if (rs4.next())
                            slots[i].slotStatus = SLOT_BOOKED;
                        else
                            slots[i].slotStatus = SLOT_PEND;
                    }
                }
            }
            // check the parent node
            if (slots[i].unitValid) {
                col = 1;
                stmt5.setInt(col++, slots[i].unitParentNodeID);
                stmt5.setString(col++, aeItem.ITM_STATUS_ON);
                rs5 = stmt5.executeQuery();
                if (rs5.next()) {
                    col = 1;
                    slots[i].unitParentNodeTitle = rs5.getString(col++);
                    if (rs5.wasNull()) slots[i].unitParentNodeTitle = BLANK;
                    slots[i].unitExt2 = rs5.getString(col++);
                    if (rs5.wasNull()) slots[i].unitExt2 = BLANK;
                }
            }
        }

        stmt1.close();stmt2.close();stmt3.close();stmt4.close();stmt5.close();
    }

    // check if the entity has double booked any slots before or during this session
    public void checkDupSlots(int inEntID) throws SQLException {
        PreparedStatement stmt1 = con.prepareStatement(sql_get_booked_app_);
        ResultSet rs1 = null;
        int col = 0;

        for (int i = 0; i < slots.length; i++) {
            if (inEntID > 0 && slots[i].slotID > 0 && slots[i].slotStatus != null && (slots[i].slotStatus.equals(SLOT_PEND) || slots[i].slotStatus.equals(SLOT_BOOKED))) {
                col = 1;
                stmt1.setInt(col++, slots[i].slotID);
                stmt1.setInt(col++, inEntID);
                rs1 = stmt1.executeQuery();
                if (rs1.next())
                    slots[i].slotStatus = SLOT_DUP;
            }
            for (int j = i+1; j < slots.length; j++) {
                if (slots[j].slotStatus == null || slots[j].slotStatus.equals(SLOT_DUP))
                    continue;
                if (slots[i].unitID == slots[j].unitID && slots[i].slotStartTime != null && slots[j].slotStartTime != null && slots[i].slotStartTime.equals(slots[j].slotStartTime))
                    slots[j].slotStatus = SLOT_DUP;
            }
        }

        stmt1.close();
    }

    // application form validation for external use
    public StringBuffer checkAppAsXML() throws SQLException {
        return checkAppAsXML(checkApp());
    }
    // call the application form validation function and return the result in XML
    private StringBuffer checkAppAsXML(Vector err) throws SQLException {
        StringBuffer result = new StringBuffer();
        if (err.size() > 0) {
            result.append("<valued_template_error>");
            for (int i = 0; i < err.size(); i++)
                result.append("<field name=\"").append((String)err.elementAt(i)).append("\"/>");
            result.append("</valued_template_error>");
        }

        return result;
    }
    // check some fields within the submitted application form (hardcode!!)
    // return a vector containing any error fields, size()=0 if no error
    private Vector checkApp() throws SQLException {
        PreparedStatement stmt1 = con.prepareStatement(sql_get_code_id_);
        ResultSet rs1 = null;
        int col = 0;
        Vector errField = new Vector();
        // check supplier name
        if (appExt1 != null) {
            col = 1;
            stmt1.setString(col++, CODE_SUPPLIER);
            stmt1.setString(col++, appExt1);
            rs1 = stmt1.executeQuery();
            if (!rs1.next())
                errField.addElement("Supplier Name");
        }
        // check product type
        if (appExt3 != null) {
            col = 1;
            stmt1.setString(col++, CODE_PRODUCTTYPE);
            stmt1.setString(col++, appExt3);
            rs1 = stmt1.executeQuery();
            if (!rs1.next())
                errField.addElement("Product Type");
            else {
                for (int i = 0; i < slots.length; i++) {
                    if (!slots[i].unitExt1.equals("All") && !slots[i].unitExt1.equals(appExt3)) {
                        errField.addElement("Product Type");
                        break;
                    }
                }
            }
        }
        stmt1.close();
        return errField;
    }

    // since there may be many slots, so only get the template from the first valid slot
    public String getFirstTemplate(String inTplType) throws SQLException {
        String outXML = null;
        for (int i = 0; i < slots.length; i++) {
            if (slots[i].unitValid) {
                outXML = getItemTemplate(slots[i].unitID, inTplType);
                break;
            }
        }
        return outXML;
    }

    // get a specific type of template of an item
    private String getItemTemplate(int inItmID, String inTplType) throws SQLException {
        String result = null;
        PreparedStatement stmt = con.prepareStatement(sql_get_itm_tpl_);
        ResultSet rs = null;
        int col = 1;

        stmt.setInt(col++, inItmID);
        stmt.setString(col++, inTplType);
        rs = stmt.executeQuery();
        if (rs.next()) result = cwSQL.getClobValue(rs, "tpl_xml");
        stmt.close();

        return result;
    }

    // return the slots in XML
    public StringBuffer getSlotsAsXML() {
        StringBuffer xml = new StringBuffer(4096);
        xml.append("<items>");
        for (int i = 0; i < slots.length; i ++)
            xml.append(slots[i].unitAsXML());
        xml.append("</items>");
        return xml;
    }

    public void setAppXML(String inAppXML) {
        this.appXML = inAppXML;
    }

    public String getAppXML() {
        return this.appXML;
    }

    public void setAppExt1(String in) {
        this.appExt1 = in;
    }

    public void setAppExt3(String in) {
        this.appExt3 = in;
    }

    // put the slots into the session
    public void storeInSession(HttpSession inSess) {
        if (slots != null)   inSess.setAttribute(SLOT_BOOKED_SESS,  slots);
        if (appXML != null)  inSess.setAttribute(SLOT_APPXML_SESS,  appXML);
        if (appExt1 != null) inSess.setAttribute(SLOT_APPEXT1_SESS, appExt1);
        if (appExt3 != null) inSess.setAttribute(SLOT_APPEXT3_SESS, appExt3);
    }

    // get the current and convert any exception into SQLException
    private Timestamp getTime() throws SQLException {
        try {
            return dbUtils.getTime(this.con);
        } catch (qdbException e) {
            throw new SQLException(e.getMessage());
        }
    }

    // book the slots for the specified user
    // return: book result in XML
    public StringBuffer make(int inEntID, String inUsrID) throws SQLException ,cwSysMessage{
        String resultPrefix = "<book result=\"";
        String resultSuffix = "\"/>";
        String result = null;
        boolean slotsValid = true;
        // check application form
        Vector appErr = checkApp();
        if (appErr.size() > 0) slotsValid = false;
        // check each selected slot
        validateSlots(inUsrID);
        checkDupSlots(inEntID);
        int i = 0;
        for (; i < slots.length; i++)
            if (!slots[i].unitValid || slots[i].slotStatus.equals(SLOT_DUP) || slots[i].slotStatus.equals(SLOT_ERR))
                break;
        if (i < slots.length) slotsValid = false;
        // carry out booking if all valid
        if (slotsValid) {
            bookSlots(inEntID, inUsrID);
            result = "OK";
        } else
            result = "NG";
        // remove session values????
        StringBuffer out = new StringBuffer();
        out.append(checkAppAsXML(appErr))
           .append(resultPrefix).append(result).append(resultSuffix);
        return out;
    }

    // book each slot, without any checking
    private void bookSlots(int inEntID, String inUsrID) throws SQLException ,cwSysMessage{
        try {
            aeWorkFlow wf          = new aeWorkFlow(dbUtils.xmlHeader);
            aeApplication app      = new aeApplication();
            aeAppnActnHistory actn = new aeAppnActnHistory();
            Timestamp cur_time     = getTime();

            for (int i = 0; i < slots.length; i++) {
                // create the time slot if not exists
                if (slots[i].slotID == 0) slots[i].dbCreate(this.con, inUsrID, cur_time);
                // initialize application status XML
                String wfTemplate = getItemTemplate(slots[i].unitID, aeTemplate.WORKFLOW);
                String initStatus = wf.initStatus(wfTemplate);
                // get the initial action that should be taken
                Vector args = null;
                if (slots[i].slotStatus.equals(SLOT_AVAIL) || slots[i].slotStatus.equals(SLOT_PEND))
                    args = wf.getAction("<applyeasy>" + wfTemplate + "</applyeasy>", "queue", true);
                else
                    args = wf.getAction("<applyeasy>" + wfTemplate + "</applyeasy>", "queue", false);
                if (args == null || args.size() != 6)
                    throw new cwException("workFlow error");
                app.actn_process_id = Long.parseLong((String)args.elementAt(0));
                app.actn_status_id  = Long.parseLong((String)args.elementAt(1));
                app.actn_action_id  = Long.parseLong((String)args.elementAt(2));
                app.actn_fr         = (String)args.elementAt(3);
                app.actn_to         = (String)args.elementAt(4);
                app.actn_verb       = (String)args.elementAt(5);
                StringBuffer action = new StringBuffer();
                action.append("<current process_id=\"").append(app.actn_process_id)
                    .append("\" status_id=\"").append(app.actn_status_id)
                    .append("\" action_id=\"").append(app.actn_action_id)
                    .append("\"/>");
                // issue the action into the workflow (check the action first!)
                // and get the new workflow status
                if (wf.checkAction(action.toString(), initStatus, wfTemplate))
                    app.app_process_xml     = wf.checkStatus(action.toString(), initStatus, wfTemplate);
                else
                    throw new cwException("invalid action");
                // get the new application status from the new workflow status
                slots[i].slotStatus         = wf.returnQueue(app.app_process_xml, wfTemplate);
                // other application attributes
                app.app_ent_id              = inEntID;
                app.app_itm_id              = slots[i].slotID;
                app.app_status              = slots[i].slotStatus;
                app.app_xml                 = this.appXML;
                app.app_create_usr_id       = inUsrID;
                app.app_create_timestamp    = cur_time;
                app.app_upd_usr_id          = inUsrID;
                app.app_upd_timestamp       = cur_time;
                app.app_ext1                = this.appExt1;
                app.app_ext2                = slots[i].unitParentNodeID;
                app.app_ext3                = this.appExt3;
                // insert this application
                app.ins(this.con);
                // and then setup attributes for saving action history
                actn.aah_app_id             = app.app_id;
                actn.aah_process_id         = app.actn_process_id;
                actn.aah_fr                 = app.actn_fr;
                actn.aah_to                 = app.actn_to;
                actn.aah_verb               = app.actn_verb;
                actn.aah_action_id          = app.actn_action_id;
                actn.aah_create_usr_id      = inUsrID;
                actn.aah_create_timestamp   = cur_time;
                actn.aah_upd_usr_id         = inUsrID;
                actn.aah_upd_timestamp      = cur_time;
                // insert this action history
                actn.ins(con);
            }
        } catch (qdbException e) {
            throw new SQLException(e.getMessage());
        } catch (cwException e) {
            throw new SQLException(e.getMessage());
        } catch (IOException e) {
            throw new SQLException(e.getMessage());
        }
    }

    // update the application form XML of a slot of a unit
    public void updAppXML(int inID, Timestamp inTS, String inUsrID) throws SQLException, cwException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int rc = 0;
        int col = 0;
        // column variables
        String itmProdType;
        // get the product type of the booking unit
        stmt = con.prepareStatement(sql_get_app_itm_producttype_);
        col = 1;
        stmt.setInt(col++, inID);
        stmt.setString(col++, ITEM_TYPE_SLOT);
        stmt.setString(col++, ITEM_TYPE_UNIT);
        rs = stmt.executeQuery();
        col = 1;
        if (rs.next()) {
            itmProdType = rs.getString(col++);
            stmt.close();
        } else {
            stmt.close();
            throw new cwException("Book Unit not found");
        }
        // check product type
        stmt = con.prepareStatement(sql_get_code_id_);
        col = 1;
        if (appExt3 != null) {
            col = 1;
            stmt.setString(col++, CODE_PRODUCTTYPE);
            stmt.setString(col++, appExt3);
            rs = stmt.executeQuery();
            if (!rs.next()) {
                stmt.close();
                throw new cwException("Product Type not found");
            } else {
                stmt.close();
            }
            if (!itmProdType.equals("All") && !itmProdType.equals(appExt3))
                throw new cwException("Book Unit Product Type not match");
        }
        // update the timestamp
        stmt = con.prepareStatement(sql_upd_app_ext3_);
        col = 1;
        stmt.setString(col++, appExt3);
        stmt.setTimestamp(col++, getTime());
        stmt.setString(col++, inUsrID);
        stmt.setInt(col++, inID);
        stmt.setTimestamp(col++, inTS);
        rc = stmt.executeUpdate();
        stmt.close();
        if (rc != 1)
            throw new cwException("Update Application failed");
        // Update appXML
        // for oracle clob
        String condition = "app_id = " + inID;
        String tableName = "aeApplication";
        String[] colName = {"app_xml"};
        String[] colValue = {appXML};
        cwSQL.updateClobFields(con, tableName, colName, colValue, condition);


//        stmt = con.prepareStatement(sql_upd_app_xml_, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
//        col = 1;
//        stmt.setInt(col++, inID);
//        rs = stmt.executeQuery();
//        if (rs.next()) {
//            cwSQL.setClobValue(con, rs, "app_xml", appXML);
//            rs.updateRow();
//            stmt.close();
//        } else {
//            stmt.close();
//            throw new cwException("Update Application failed");
//        }
    }
}