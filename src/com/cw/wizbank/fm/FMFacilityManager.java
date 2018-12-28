package com.cw.wizbank.fm;

import java.util.Hashtable;
import java.util.List;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.db.*;
import com.cw.wizbank.db.view.*;
import com.cwn.wizbank.utils.CommonLog;

public class FMFacilityManager {
    private final static boolean DEBUG  = false;
    private final static String YES        = "YES";
    private final static String NO        = "NO";

    private Connection con = null;

    /**
     * constructor
     */
    public FMFacilityManager(Connection con) throws SQLException {
        if (con == null)
            throw new SQLException("connection not available");
        else
            this.con = con;
    }

    public int insFacility(Hashtable facility) throws SQLException, cwSysMessage, cwException {
        int type = ((Integer)facility.get("fac_type")).intValue();
        DbFmFacilityType dbFacilityType = new DbFmFacilityType(this.con);
        // retrieve the parent id for the facility
        int p_type = dbFacilityType.getParentType(type);
        if (this.DEBUG)
        	CommonLog.debug("type: " + type + ", parent type: " + p_type);
        facility.put("parent_ftp_id", new Integer(p_type));
        // retrieve the class name for the facility
        String className = dbFacilityType.getTypeClass(p_type);

        // instance the class
        FmFacilityTemplate facilityInstance = null;
        if (this.DEBUG)
        	CommonLog.debug("class name: " + className);
        try {
            facilityInstance = (FmFacilityTemplate)Class.forName(className).newInstance();
        } catch (Exception e) {
            throw new cwException("class not found!");
        }

        // execute the insertion
        facilityInstance.setConnection(this.con);
        return facilityInstance.insert(facility);
    }

    public void updFacility(Hashtable facility) throws SQLException, cwSysMessage, cwException {
        // retrieve the class name for the facility
        ViewFmFacility dbFacility = new ViewFmFacility(this.con);
        int type = dbFacility.getFacilityType(((Integer)facility.get("fac_id")).intValue());
        DbFmFacilityType dbFacilityType = new DbFmFacilityType(this.con);
        String className = dbFacilityType.getTypeClass(type);

        // instance the class
        FmFacilityTemplate facilityInstance = null;
        try {
            facilityInstance = (FmFacilityTemplate)Class.forName(className).newInstance();
        } catch (Exception e) {
            throw new cwException("class not found!");
        }

        // execute the update
        facilityInstance.setConnection(this.con);
        facilityInstance.update(facility);
    }

    public void removeFacility(Hashtable facility) throws SQLException, cwSysMessage, cwException {
        int fac_id = ((Integer)facility.get("fac_id")).intValue();
        // retrieve the class name for the facility
        ViewFmFacility dbFacility = new ViewFmFacility(this.con);
        int type = dbFacility.getFacilityType(fac_id);
        DbFmFacilityType dbFacilityType = new DbFmFacilityType(this.con);
        String className = dbFacilityType.getTypeClass(type);

        // instance the class
        FmFacilityTemplate facilityInstance = null;
        try {
            facilityInstance = (FmFacilityTemplate)Class.forName(className).newInstance();
        } catch (Exception e) {
            throw new cwException("class not found!");
        }

        // execute the deletion
        Timestamp upd_timestamp = (Timestamp)facility.get("fac_upd_timestamp");
        String usr_id = (String)facility.get("user_id");
        facilityInstance.setConnection(this.con);
        facilityInstance.delete(fac_id, upd_timestamp, usr_id);
    }

    public String getFacilityDetails(Hashtable param) throws SQLException, cwException {
        int fac_id = ((Integer)param.get("fac_id")).intValue();
        // retrieve the class name for the facility
        ViewFmFacility dbFacility = new ViewFmFacility(this.con);
        int type = dbFacility.getFacilityType(fac_id);

        DbFmFacilityType dbFacilityType = new DbFmFacilityType(this.con);
        String className = dbFacilityType.getTypeClass(type);

        // instance the class
        FmFacilityTemplate facilityInstance = null;
        try {
            facilityInstance = (FmFacilityTemplate)Class.forName(className).newInstance();
        } catch (Exception e) {
            throw new cwException("class not found!");
        }

        // execute the retrieving
        facilityInstance.setConnection(this.con);
        Hashtable facility = facilityInstance.get(fac_id);

        // generate the xml
        return this.facDetailAsXML(facility);
    }

    public String getFacilityList(Hashtable param) throws SQLException, cwException {
        int owner_id = ((Integer)param.get("owner_ent_id")).intValue();
        int fac_type = ((Integer)param.get("fac_type")).intValue();
        // retrieving the type
        DbFmFacilityType dbFacilityType = new DbFmFacilityType(this.con);
        Hashtable[] typeList = dbFacilityType.getAllType(owner_id);
        // retrieving the facility
        ViewFmFacility facilityInstance = new ViewFmFacility(this.con);
        Hashtable[] facilityList = facilityInstance.listFacility(owner_id, fac_type);

        // generate the xml
        return this.facListAsXML(fac_type, typeList, facilityList);
    }

    /**
     * generate the xml file for facility detail
     * @param;    Hashtable    -    the facility detail
     * @return:    String        -    the xml file
     */
    private String facDetailAsXML(Hashtable source) throws SQLException {
        if (source == null)
            return null;
        StringBuffer result = new StringBuffer("<facility id=\"");
        result.append((String)source.get("fac_id"));
        result.append("\" status=\"");
        result.append((String)source.get("fac_status"));
        result.append("\">");
        result.append("<facility_type id=\"").append((String)source.get("fac_ftp_id"));
        result.append("\" xsl_prefix=\"").append(cwUtils.esc4XML((String)source.get("ftp_xsl_prefix")));
        result.append("\">").append((String)source.get("ftp_title")).append("</facility_type>");
        result.append("<basic><title>");
        result.append(cwUtils.esc4XML((String)source.get("fac_title")));
        result.append("</title>");
        result.append("<facility_desc>").append(cwUtils.esc4XML(cwUtils.escNull(source.get("fac_desc")))).append("</facility_desc>");
        if (source.containsKey("fac_fee")) {
        	result.append("<fac_fee>").append(cwUtils.formatNumber(((Double)source.get("fac_fee")).doubleValue(), 2)).append("</fac_fee>");
        }
        result.append("<remarks>").append(cwUtils.esc4XML(cwUtils.escNull(source.get("fac_remarks")))).append("</remarks>");
        result.append("<url type=\"").append(cwUtils.esc4XML(cwUtils.escNull(source.get("fac_url_type"))));
        result.append("\">").append(cwUtils.esc4XML(cwUtils.escNull(source.get("fac_url")))).append("</url>");

        result.append("</basic>");
        // additional information
        if (source.get("fac_add_xml") != null && !((String)source.get("fac_add_xml")).equalsIgnoreCase("NULL"))
            result.append("<additional>").append((String)source.get("fac_add_xml")).append("</additional>");

        // update information
        result.append("<update_user id=\"");
        result.append((String)source.get("fac_upd_usr_id"));
        result.append("\" timestamp=\"");
        result.append((Timestamp)source.get("fac_upd_timestamp"));
        result.append("\"/>");

        result.append("</facility>");

        if (this.DEBUG)
        	CommonLog.debug(result.toString());
        return result.toString();
    }

    /**
     * generate the xml file for facility list
     * @param cur_type
     * @param typeList      -   the facility type list
     * @param facilityList  -   the facility list
     * @return String        -    the xml file
     * @throws SQLException
     */
    private String facListAsXML(int cur_type, Hashtable[] typeList,
                                Hashtable[] facilityList) throws SQLException {
        if (typeList == null)
            return null;
        // variable for facility type
        int lastType = 0, type = 0, lastSubType = 0, subType = 0;
        // variable for facility
        int index = 0, facType = 0, facSubType = 0;

        String ftp_main_status = null;
        StringBuffer result = new StringBuffer("<facility_list cur_type_id=\"");
        if (cur_type != Integer.MIN_VALUE)
            result.append(cur_type);
        result.append("\">");

        for (int i = 0; i < typeList.length; i++) {
            // check the type of the facility
            lastType = type;
            type = Integer.parseInt((String)typeList[i].get("type_id"));
            if (lastType != type) {
                if (lastSubType != 0)    // TO DO ... ?
                    result.append("</facility_subtype>");
                lastSubType = 0;
                subType = 0;
                if (lastType != 0)
                    result.append("</facility_type>");

                result.append("<facility_type");
                result.append(" id=\"").append((String)typeList[i].get("type_id"));
                ftp_main_status = (((String)typeList[i].get("ftp_main_indc")).equals("1")) ? this.YES : this.NO;
                result.append("\" main=\"").append(ftp_main_status);
                result.append("\" xsl_prefix=\"").append(cwUtils.esc4XML((String)typeList[i].get("ftp_xsl_prefix")));
                result.append("\"");
                result.append(">");
                // get the facility type name (in xml)
                result.append((String)typeList[i].get("type_title_xml"));
            }

            if (cur_type != Integer.MIN_VALUE && cur_type != type) {
                continue;
            }

            // check the sub_type of the facility
            lastSubType = subType;
            subType = Integer.parseInt((String)typeList[i].get("sub_type_id"));

            if (lastSubType != subType) {
                if (lastSubType != 0 && (cur_type == Integer.MIN_VALUE || lastType == cur_type))
                    result.append("</facility_subtype>");

                result.append("<facility_subtype");
                result.append(" id=\"").append(subType).append("\">");
                // get the facility sub_type name (in xml)
                result.append((String)typeList[i].get("sub_type_title_xml"));
            }

            // get the facility
            for (int k = index; k < facilityList.length; k++) {
                facType = new Integer((String)facilityList[k].get("fac_ftp_id")).intValue();
                facSubType = new Integer((String)facilityList[k].get("sub_type_id")).intValue();
                if (type == facType && subType == facSubType) {
                    result.append("<facility id=\"");
                    result.append((String)facilityList[k].get("fac_id"));
                    result.append("\" status=\"");
                    result.append((String)facilityList[k].get("fac_status"));
                    result.append("\" title=\"");
                    result.append(cwUtils.esc4XML((String)facilityList[k].get("fac_title")));
                    result.append("\"/>");
                } else {
                    index = k;
                    facType = 0;
                    facSubType = 0;
                    break;
                }
            }
        }

        if (subType != 0 && (cur_type == Integer.MIN_VALUE || cur_type == type)) {
            result.append("</facility_subtype>");
        }
        if (type != 0) {
            result.append("</facility_type>");
        }
        result.append("</facility_list>");
        if (this.DEBUG){
        	 CommonLog.debug(result.toString());
        }
        return result.toString();
    }
    
    public String getFMFeePrepAsXml(long owner_id) throws SQLException {
    	StringBuffer result = new StringBuffer();
    	DbFmFacilityType dbFacilityType = new DbFmFacilityType(this.con);
    	List typeLst = dbFacilityType.getAllMainType(owner_id);
    	for (int i = 0; i < typeLst.size(); i++) {
    		result.append("<facility_type id=\"").append(typeLst.get(i)).append("\"/>");
    	}
    	return result.toString();
    }
}