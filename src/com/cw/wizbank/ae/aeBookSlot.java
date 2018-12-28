package com.cw.wizbank.ae;

import java.sql.Connection;
import java.sql.Timestamp;
import java.sql.SQLException;

import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwException;
// class for a single bookable time slot (2001.04.22 wai)
public class aeBookSlot {
    public int       unitID;
    public String    unitType;
    public String    unitTitle;
    public String    unitExt1;
    public int       unitOwnerID;
    public boolean   unitValid;

    public int       unitParentNodeID;
    public String    unitParentNodeTitle;
    public String    unitExt2;

    public int       slotID;
    public Timestamp slotStartTime;
    public String    slotStatus;

    public aeBookSlot() {
        unitID              = 0;
        unitType            = "";
        unitTitle           = "";
        unitExt1            = "";
        unitValid           = false;
        unitParentNodeID    = 0;
        unitParentNodeTitle = "";
        unitExt2            = "";
        slotID              = 0;
        slotStartTime       = null;
        slotStatus          = "";
    }

    // create this slot as a database record
    public void dbCreate(Connection inCon, String inCreateUsrID, Timestamp inCreateTime) throws SQLException ,cwSysMessage, cwException {
        // child item record
        aeItem dbSlot = new aeItem();
        dbSlot.itm_title              = aeBooking.ITEM_TYPE_SLOT;
        dbSlot.itm_type               = aeBooking.ITEM_TYPE_SLOT;
        dbSlot.itm_status             = aeItem.ITM_STATUS_ON;
        dbSlot.itm_eff_start_datetime = this.slotStartTime;
        dbSlot.itm_owner_ent_id       = this.unitOwnerID;
        dbSlot.itm_create_timestamp   = inCreateTime;
        dbSlot.itm_create_usr_id      = inCreateUsrID;
        dbSlot.itm_upd_timestamp      = inCreateTime;
        dbSlot.itm_upd_usr_id         = inCreateUsrID;
        dbSlot.insItem(inCon);
        this.slotID = (int)dbSlot.itm_id;
        // item relation between child(slot) and parent(unit)
        aeItemRelation dbUnitSlot = new aeItemRelation();
        dbUnitSlot.ire_parent_itm_id    = this.unitID;
        dbUnitSlot.ire_child_itm_id     = this.slotID;
        dbUnitSlot.ire_create_timestamp = inCreateTime;
        dbUnitSlot.ire_create_usr_id    = inCreateUsrID;
        dbUnitSlot.ins(inCon);
    }

    // return this slot as an XML
    public StringBuffer unitAsXML() {
        StringBuffer xml = new StringBuffer(300);
        xml.append("<item id=\"").append(unitID)
           .append("\" type=\"").append(unitType)
           .append("\" title=\"").append(dbUtils.esc4XML(unitTitle))
           .append("\" ext1=\"").append(dbUtils.esc4XML(unitExt1))
           .append("\" slot_start_datetime=\"");
        if (slotStartTime != null)
            xml.append(slotStartTime);
        xml.append("\" slot_status=\"").append(slotStatus)
           .append("\">").append(parentAsXML()).append("</item>");
       return xml;
    }

    // return the parent node of this slot as an XML
    public StringBuffer parentAsXML() {
        StringBuffer xml = new StringBuffer(100);
        xml.append("<parent_node id=\"").append(unitParentNodeID)
           .append("\" title=\"").append(dbUtils.esc4XML(unitParentNodeTitle))
           .append("\" ext2=\"").append(dbUtils.esc4XML(unitExt2))
           .append("\"/>");
        return xml;
    }
}