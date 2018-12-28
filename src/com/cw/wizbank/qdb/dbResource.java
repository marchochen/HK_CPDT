package com.cw.wizbank.qdb;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.http.HttpSession;
import javax.xml.transform.TransformerException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;

import org.apache.commons.lang.StringUtils;
import org.apache.xerces.parsers.DOMParser;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.cw.wizbank.JsonMod.Course.Course;
import com.cw.wizbank.JsonMod.Course.bean.AiccDataBean;
import com.cw.wizbank.JsonMod.Course.bean.ModBean;
import com.cw.wizbank.JsonMod.Course.bean.ModRelationBean;
import com.cw.wizbank.JsonMod.Course.bean.PreModuleBean;
import com.cw.wizbank.JsonMod.Course.bean.ResourceBean;
import com.cw.wizbank.accesscontrol.AcObjective;
import com.cw.wizbank.accesscontrol.AcPageVariant;
import com.cw.wizbank.accesscontrol.AcXslQuestion;
import com.cw.wizbank.ae.aeAction;
import com.cw.wizbank.ae.aeItemDummyType;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.course.ModulePrerequisiteManagement;
import com.cw.wizbank.db.DbTrackingHistory;
import com.cw.wizbank.db.DbTrainingCenter;
import com.cw.wizbank.importcos.ImportCos;
import com.cw.wizbank.importcos.ScormContentParser;
import com.cw.wizbank.quebank.quecontainer.DynamicAssessment;
import com.cw.wizbank.quebank.quecontainer.DynamicScenarioQue;
import com.cw.wizbank.quebank.quecontainer.FixedAssessment;
import com.cw.wizbank.quebank.quecontainer.FixedScenarioQue;
import com.cw.wizbank.util.JsonPropertyFilter;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwPagination;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.entity.Question;

public class dbResource implements Serializable , Cloneable 
{
    //public static final String FCN_RES_MGT_PUBLIC = "RES_MGT_PUBLIC";

    public static final String INVALID_TIMESTAMP_MSG = "RES001"; //"The record was updated by other user.";
    public static final String RES_NOT_IN_TA_MGT_TC = "RES006";
    public static final String RES_NOT_IN_INSTR_TC = "RES007";
    public static final String RES_FOLDER_NOT_EXIST = "RES008";
    public static final String CAN_MGT_RES = "CAN_MGT_RES";
    public static final String RES_TYPE_GEN     = "GEN";
    public static final String RES_TYPE_QUE     = "QUE";
    public static final String RES_TYPE_SUQ     = "SUQ";
    public static final String RES_TYPE_AICC     = "AICC";
    public static final String RES_TYPE_ASM     = "ASM";
    public static final String RES_TYPE_MOD     = "MOD";
    public static final String RES_TYPE_COS     = "COS";
    public static final String RES_TYPE_OTHER   = "OTR";
    public static final String RES_SUBTYPE_FILE = "FILE";
    public static final String RES_SUBTYPE_DIS = "DIS";
    public static final String RES_TYPE_SCORM	= "SCORM";
    public static final String RES_TYPE_NETGCOK	= "NETGCOK";

    // Dynamic Assessment
    public static final String RES_SUBTYPE_DAS = "DAS";
    public static final String RES_SUBTYPE_RES_SCO = "RES_SCO";
    public static final String RES_SUBTYPE_RES_NETG_COK = "RES_NETG_COK";
 
    // Fixed Assessment
    public static final String RES_SUBTYPE_FAS = "FAS";
    // Dynamic Scenario Question
    public static final String RES_SUBTYPE_DSC = "DSC";
    // Fixed Scenario Question
    public static final String RES_SUBTYPE_FSC = "FSC";
    // Fixed Scenario Question
    public static final String RES_SUBTYPE_MBL = "MBL";
    
    public static final String SRC_TYPE_FILE = "FILE";
    public static final String SRC_TYPE_WIZPACK = "WIZPACK";
    public static final String SRC_TYPE_URL = "URL";

    public static final String RES_STATUS_ON = "ON";
    public static final String RES_STATUS_OFF = "OFF";
    public static final String RES_STATUS_DATE = "DATE";

    public static final String RES_ATTEMPTED_TRUE = "TRUE";
    public static final String RES_ATTEMPTED_FALSE = "FALSE";

    public static final String RES_PRIV_AUTHOR = "AUTHOR";
    public static final String RES_PRIV_CW = "CW";

    public static final String RES_DISPLAY_BIL_CW = "CW";

    public static final int RES_DESC_LENGTH = 1000;
    
    public static final String ENROLLMENT_RELATED_ALL   = "all";
    public static final String ENROLLMENT_RELATED_TRUE  = "true";
    public static final String ENROLLMENT_RELATED_FALSE = "false";

    public long res_id;
    public String res_lan;
    public String res_title;
    public String res_desc;
    public String res_type;
    public String res_subtype;
    public String res_annotation;
    public String res_format;
    public int res_difficulty;
    public float res_duration;
    public String res_privilege;
    public String res_status;
    public String res_usr_id_owner;
    public String res_tpl_name;
    public long res_res_id_root;
    public long res_res_id_child;
    public long res_mod_res_id_test;
    public String res_upd_user;
    public Timestamp res_upd_date;
    public String res_src_type;
    public String res_src_link;
    public String res_src_online_link;
    public String res_img_link;
    public int res_vod_duration;
    public String res_vod_main;
    //public String res_url;
    //public String res_filename;
    public String res_instructor_name;
    public String res_instructor_organization;
    public Timestamp res_create_date;
    public File vodFile;

    public String res_cnt_subtype;  //Dennis, not a DB column, used to constraint
                                    //the subtype of the resource content subtype

    public String res_in_subtype;  //Dennis used in get_res only

    public String mod_usr_id_instructor;    //Lun used in getContentLst

    public String location;         // CL : current location of a course

    public long tkh_id = DbTrackingHistory.TKH_ID_UNDEFINED;         //kim: to store tracking history id, not a db field
    
    //scorm version
    public String res_sco_version;
    public dbResource() {;}

    public Object clone() throws CloneNotSupportedException{
        dbResource newRes = (dbResource) super.clone();
        newRes.res_id = this.res_id;
        newRes.res_lan = this.res_lan; 
        newRes.res_title = this.res_title;
        newRes.res_desc = this.res_desc;
        newRes.res_type = this.res_type;
        newRes.res_subtype = this.res_subtype;
        newRes.res_annotation = this.res_annotation;
        newRes.res_format = this.res_format;
        newRes.res_difficulty = this.res_difficulty;
        newRes.res_duration = this.res_duration;
        newRes.res_privilege = this.res_privilege;
        newRes.res_status = this.res_status;
        newRes.res_usr_id_owner = this.res_usr_id_owner;
        newRes.res_tpl_name = this.res_tpl_name;
        newRes.res_res_id_root = this.res_res_id_root;
        newRes.res_mod_res_id_test = this.res_mod_res_id_test;
        newRes.res_upd_user = this.res_upd_user;
        newRes.res_upd_date = this.res_upd_date;
        newRes.res_src_type = this.res_src_type;
        newRes.res_src_link = this.res_src_link;
        newRes.res_src_online_link = this.res_src_online_link;
        newRes.res_instructor_name = this.res_instructor_name;
        newRes.res_instructor_organization = this.res_instructor_organization;
        newRes.res_create_date = this.res_create_date;
        newRes.res_cnt_subtype = this.res_cnt_subtype;
        newRes.res_in_subtype = this.res_in_subtype;
        newRes.res_sco_version = this.res_sco_version;
        newRes.mod_usr_id_instructor = this.mod_usr_id_instructor;
        newRes.location = this.location;
        newRes.tkh_id = this.tkh_id;
        newRes.vodFile = this.vodFile;
        return newRes;
    }
    
    // deprecated, should call ins_res2 which will not call con.commit()
    public void ins_res(Connection con, String[] robs, loginProfile prof)
        throws qdbException, qdbErrMessage
    {
        try {
            ins_res2(con, robs, prof);
            con.commit();
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    public void ins_res2(Connection con, String[] robs, loginProfile prof)
        throws qdbException, qdbErrMessage
    {
        try {
            // Only Expert admin, admin, expert course designer can insert public resource
            //if (res_privilege.equalsIgnoreCase(RES_PRIV_CW)) {
            //    checkPublicResPermission(con, prof);
            //}

            ins(con);

            PreparedStatement stmt = con.prepareStatement(
            " INSERT INTO ResourceObjective "
              + " ( rob_res_id , rob_obj_id ) VALUES "
            + " ( ?, ? ) ");

            if (robs[0]!=null && robs.length > 0) {
                for(int j=0;j<robs.length;j++) {
                    if (robs[j] == null) break;
                    stmt.setLong(1, res_id);
                    stmt.setLong(2, Long.parseLong(robs[j]));
                    if(stmt.executeUpdate() != 1 ) {
                    // insert fails, rollback
                        con.rollback();
                        throw new qdbException("Fails to insert ResourceObjective");
                    }
                 }
            }
            stmt.close();
            /*AcResources acres = new AcResources(con);
            boolean read = acres.hasResPermissionRead(prof.current_role);
            boolean write = acres.hasResPermissionWrite(prof.current_role);
            boolean exec = acres.hasResPermissionExec(prof.current_role);
            dbResourcePermission.save(con,res_id,prof.usr_id,prof.current_role,read,write,exec);*/
            //dbResourcePermission.save(con,res_id,prof.usr_id,true,true,false);

            // Public Folder
            /*if (res_privilege.equalsIgnoreCase(dbResource.RES_PRIV_CW)) {
                // CW Admin Group
                // open resource for all
                //if(prof.isPublic)
                if (dbObjective.isPublicObjective(con, Long.parseLong(robs[0])))
                    dbResourcePermission.save(con,res_id,0,null,true,false,false);
                // open resource for those within the organization
                else
                    dbResourcePermission.save(con,res_id,prof.root_ent_id,null,true,false,false);
            }*/

      } catch(SQLException e) {
        throw new qdbException("SQL Error: " + e.getMessage());
      }
    }

    public void create_SSC_info_file(String resFolderPath, String courseName) throws qdbException, qdbErrMessage {
        BufferedWriter out = null;

        try {
            // write the information file
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(resFolderPath + dbUtils.SLASH + Long.toString(res_id) + dbUtils.SLASH + "aicc_info.txt")));
            out.write(courseName.toCharArray());
            out.flush();
            out.close();
        } catch(IOException e) {
            throw new qdbException(e.toString());
        }
    }

    // insert a SkillSoft course as a resource into the resource manager
    public void ins_ssc(Connection con, String[] robs, loginProfile prof, String desFilePath, String auFilePath) throws qdbException, qdbErrMessage {
        try {
            // Only Expert admin, admin, expert course designer can insert public resource
            //if (res_privilege.equalsIgnoreCase(RES_PRIV_CW)) {
            //    checkPublicResPermission(con, prof);
            //}

            Vector vtAuRecordList = null;
            Vector vtCosDescriptor = null;
            Vector vtTemp = null;
            Hashtable htAuFieldOrder = null;
            Hashtable htAuFieldValue = null;

            // used for .au file
            String systemID = null;
            String type = null;
            String command_line = null;
            String file_name = null;
            String max_score = null;
            String mastery_score = null;
            String max_time_allowed = null;
            String time_limit_action = null;
            String system_vendor = null;
            String core_vendor = null;
            String web_launch = null;
            String au_password = null;

            // used for .des file
            String title = null;
            String description = null;

            try {
                // get the AU information from .au
                vtAuRecordList = dbCourse.buildAuRecord(auFilePath);
            } catch (IOException e) {
                throw new qdbException("error while reading the .au file:" + e.toString());
            }
            // there should be only 1 column record and 1 AU record, otherwis, error
            if (vtAuRecordList.size() > 2) {
                throw new qdbException("contain multiple AU records");
            }
            else {
                htAuFieldOrder = (Hashtable)vtAuRecordList.elementAt(0);
                htAuFieldValue = (Hashtable)vtAuRecordList.elementAt(1);

                Integer fieldIndex = null;

                fieldIndex = (Integer)htAuFieldOrder.get("system_id");
                systemID = (String)htAuFieldValue.get(fieldIndex);

                fieldIndex = (Integer)htAuFieldOrder.get("file_name");
                file_name = (String)htAuFieldValue.get(fieldIndex);

                fieldIndex = (Integer)htAuFieldOrder.get("max_time_allowed");
                max_time_allowed = (String)htAuFieldValue.get(fieldIndex);
            }

            try {
                // get the info. from .des file
                vtCosDescriptor = dbCourse.buildCosDescriptorVector(desFilePath);
            } catch (IOException e) {
                throw new qdbException("error while reading the .des file:" + e.toString());
            }
            vtTemp = null;
            for (int i=0; i<vtCosDescriptor.size(); i++) {
                vtTemp = (Vector)vtCosDescriptor.elementAt(i);
                if (systemID.equalsIgnoreCase((String)vtTemp.elementAt(0)) == true) {
                    break;
                }
                else {
                    vtTemp = null;
                }
            }
            if (vtTemp == null) {
                throw new qdbException("does not contain corresponding AU record in .des file");
            }
            else {
                title = (String)vtTemp.elementAt(2);
                description = (String)vtTemp.elementAt(3);
            }


            // overwrite the resource parameters with values from the description files
            if (max_time_allowed.equalsIgnoreCase("") == true || max_time_allowed.length() == 0) {
            }
            else {
                int hr = 0;
                int min = 0;
                int sec = 0;
                String strTime = null;
                StringTokenizer stTime = new StringTokenizer(max_time_allowed,":");

                strTime = stTime.nextToken();
                strTime = strTime.trim();
                hr = Integer.parseInt(strTime);

                strTime = stTime.nextToken();
                strTime = strTime.trim();
                min = Integer.parseInt(strTime);

                strTime = stTime.nextToken();
                strTime = strTime.trim();
                sec = Integer.parseInt(strTime);

                res_duration = hr*60 + min + sec/60;
            }
//            res_title = title;
//            res_desc = description;
            res_src_link = file_name;

            ins(con);

            PreparedStatement stmt = con.prepareStatement(
            " INSERT INTO ResourceObjective "
              + " ( rob_res_id , rob_obj_id ) VALUES "
            + " ( ?, ? ) ");

            if (robs[0]!=null && robs.length > 0) {
                for(int j=0;j<robs.length;j++) {
                    if (robs[j] == null) break;
                    stmt.setLong(1, res_id);
                    stmt.setLong(2, Long.parseLong(robs[j]));
                    if(stmt.executeUpdate() != 1 ) {
                        throw new qdbException("Fails to insert ResourceObjective");
                    }
                 }
            }
            stmt.close();
            /*AcResources acres = new AcResources(con);
            boolean read = acres.hasResPermissionRead(prof.current_role);
            boolean write = acres.hasResPermissionWrite(prof.current_role);
            boolean exec = acres.hasResPermissionExec(prof.current_role);
            dbResourcePermission.save(con,res_id,prof.usr_id,prof.current_role,read,write,exec);*/
            //dbResourcePermission.save(con,res_id,prof.usr_id,true,true,false);

            // Public Folder
            /*if (res_privilege.equalsIgnoreCase(dbResource.RES_PRIV_CW)) {
                // CW Admin Group
                // open resource for all
                //if(prof.isPublic)
                if (dbObjective.isPublicObjective(con, Long.parseLong(robs[0])))
                    dbResourcePermission.save(con,res_id,0,null,true,false,false);
                // open resource for those within the organization
                else
                    dbResourcePermission.save(con,res_id,prof.root_ent_id,null,true,false,false);
            }*/
      } catch(SQLException e) {
        throw new qdbException("SQL Error: " + e.getMessage());
      }
    }

    public void upd_ssc(Connection con, loginProfile prof, String desFilePath, String auFilePath)
        throws qdbErrMessage, qdbException ,cwSysMessage
    {
        try {
            res_upd_user = prof.usr_id;

//            checkResPermission(con, prof);
            //if (!dbResourcePermission.hasPermission(con, res_id, prof,
            //                            dbResourcePermission.RIGHT_WRITE))
            //    throw new qdbErrMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);

            Vector vtAuRecordList = null;
            Vector vtCosDescriptor = null;
            Vector vtTemp = null;
            Hashtable htAuFieldOrder = null;
            Hashtable htAuFieldValue = null;

            // used for .au file
            String systemID = null;
            String type = null;
            String command_line = null;
            String file_name = null;
            String max_score = null;
            String mastery_score = null;
            String max_time_allowed = null;
            String time_limit_action = null;
            String system_vendor = null;
            String core_vendor = null;
            String web_launch = null;
            String au_password = null;

            // used for .des file
            String title = null;
            String description = null;

            try {
                // get the AU information from .au
                vtAuRecordList = dbCourse.buildAuRecord(auFilePath);
            } catch (IOException e) {
                throw new qdbException("error while reading the .au file:" + e.toString());
            }
            // there should be only 1 column record and 1 AU record, otherwis, error
            if (vtAuRecordList.size() > 2) {
                throw new qdbException("contain multiple AU records");
            }
            else {
                htAuFieldOrder = (Hashtable)vtAuRecordList.elementAt(0);
                htAuFieldValue = (Hashtable)vtAuRecordList.elementAt(1);

                Integer fieldIndex = null;

                fieldIndex = (Integer)htAuFieldOrder.get("system_id");
                systemID = (String)htAuFieldValue.get(fieldIndex);

                fieldIndex = (Integer)htAuFieldOrder.get("file_name");
                file_name = (String)htAuFieldValue.get(fieldIndex);

                fieldIndex = (Integer)htAuFieldOrder.get("max_time_allowed");
                max_time_allowed = (String)htAuFieldValue.get(fieldIndex);
            }

            try {
                // get the info. from .des file
                vtCosDescriptor = dbCourse.buildCosDescriptorVector(desFilePath);
            } catch (IOException e) {
                throw new qdbException("error while reading the .des file:" + e.toString());
            }
            vtTemp = null;
            for (int i=0; i<vtCosDescriptor.size(); i++) {
                vtTemp = (Vector)vtCosDescriptor.elementAt(i);
                if (systemID.equalsIgnoreCase((String)vtTemp.elementAt(0)) == true) {
                    break;
                }
                else {
                    vtTemp = null;
                }
            }
            if (vtTemp == null) {
                throw new qdbException("does not contain corresponding AU record in .des file");
            }
            else {
                title = (String)vtTemp.elementAt(2);
                description = (String)vtTemp.elementAt(3);
            }


            // overwrite the resource parameters with values from the description files
            if (max_time_allowed.equalsIgnoreCase("") == true || max_time_allowed.length() == 0) {
            }
            else {
                int hr = 0;
                int min = 0;
                int sec = 0;
                String strTime = null;
                StringTokenizer stTime = new StringTokenizer(max_time_allowed,":");

                strTime = stTime.nextToken();
                strTime = strTime.trim();
                hr = Integer.parseInt(strTime);

                strTime = stTime.nextToken();
                strTime = strTime.trim();
                min = Integer.parseInt(strTime);

                strTime = stTime.nextToken();
                strTime = strTime.trim();
                sec = Integer.parseInt(strTime);

                res_duration = hr*60 + min + sec/60;
            }
//            res_title = title;
//            res_desc = description;
            res_src_link = file_name;

            checkTimeStamp(con);
            upd(con);
            //upd resource permission
            if(res_privilege.equals(RES_PRIV_CW))
                dbResourcePermission.save(con, res_id, prof.root_ent_id, null, true,false,false);
            else
                dbResourcePermission.del(con, res_id, prof.root_ent_id);

            con.commit();
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
       }
    }
    
    public void upd_res(Connection con, loginProfile prof, boolean chkTimestamp)
    throws qdbErrMessage, qdbException ,cwSysMessage {
    	try {
            res_upd_user = prof.usr_id;
            checkTimeStamp(con);
            upd(con);
            if(res_privilege.equals(RES_PRIV_CW))
                dbResourcePermission.save(con, res_id, prof.root_ent_id, null, true,false,false);
            else
                dbResourcePermission.del(con, res_id, prof.root_ent_id);

            con.commit();
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
       }	
    }

    static public void removeResLst(Connection con, String[] res_id_lst, loginProfile prof) throws qdbErrMessage, qdbException, cwSysMessage {
        try {

            String resIds = dbUtils.array2list(res_id_lst);

                PreparedStatement stmt = con.prepareStatement(" SELECT res_id, res_type, res_subtype FROM Resources " //change resources
        +" WHERE res_id IN " + resIds);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                if (rs.getString("res_type").equalsIgnoreCase(RES_TYPE_QUE)) {
                    if (rs.getString("res_subtype").equalsIgnoreCase(RES_SUBTYPE_FSC)) {
                        FixedScenarioQue myFixedScenarioQue = new FixedScenarioQue();
                        myFixedScenarioQue.res_id = rs.getLong("res_id");
                        myFixedScenarioQue.del(con, prof);
                    } else if (rs.getString("res_subtype").equalsIgnoreCase(RES_SUBTYPE_DSC)) {
                        DynamicScenarioQue myDynamicScenarioQue = new DynamicScenarioQue();
                        myDynamicScenarioQue.res_id = rs.getLong("res_id");
                        myDynamicScenarioQue.del(con, prof);
                    } else {
                        dbQuestion dbque = new dbQuestion();
                        dbque.que_res_id = rs.getLong("res_id");
                        dbque.res_id = rs.getLong("res_id");
                        dbque.res_upd_user = prof.usr_id;
                        dbque.del(con);
                    }
                    //if (!dbResourcePermission.hasPermission(con, dbque.que_res_id, prof,
                    //                        dbResourcePermission.RIGHT_WRITE))
                    //throw new qdbErrMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);
                    //                    dbque.checkResPermission(con, prof);

                } else if (rs.getString("res_type").equalsIgnoreCase(RES_TYPE_ASM)) {
                    // Dyanmic Assessment
                    if (rs.getString("res_subtype").equalsIgnoreCase(RES_SUBTYPE_DAS)) {
                        DynamicAssessment myDynamicAssessment = new DynamicAssessment();
                        myDynamicAssessment.res_id = rs.getLong("res_id");
                        myDynamicAssessment.del(con, prof);
                    }
                    // Fixed Assessment
                    else {
                        FixedAssessment myFixedAssessment = new FixedAssessment();
                        myFixedAssessment.res_id = rs.getLong("res_id");
                        myFixedAssessment.del(con, prof);
                    }
                } else if (rs.getString("res_type").equalsIgnoreCase(RES_TYPE_SCORM)) {
                	dbResource dbres = new dbResource();
                    dbres.res_id = rs.getLong("res_id");
                    dbScormResource srs = new dbScormResource();
                	srs.srs_res_id = dbres.res_id;
                	srs.del(con);
                	dbResourceContent rcn = new dbResourceContent();
                	rcn.rcn_res_id = dbres.res_id;
                	Vector child_res = dbResourceContent.getChildResources(con, dbres.res_id);
                	for (int i = 0; i < child_res.size(); i++) {
                		dbResource tmp_res = (dbResource) child_res.get(i);
                		dbModule mod = new dbModule();
                		mod.res_id = tmp_res.res_id;
                		mod.mod_res_id = tmp_res.res_id;
                		mod.res_upd_date = tmp_res.res_upd_date;
                		mod.del(con);
                	}
                	dbres.del_res(con, prof);
                } else {
                    dbResource dbres = new dbResource();
                    dbres.res_id = rs.getLong("res_id");
//                    dbres.checkResPermission(con, prof);

                    dbres.del_res(con, prof);
                }
            }
            stmt.close();
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
       }
    }

    static public void updResStatus(Connection con, String[] res_id_lst, String status, loginProfile prof)
        throws qdbErrMessage, qdbException ,cwSysMessage
    {
        for (int i=0;i<res_id_lst.length;i++) {
            dbResource dbres = new dbResource();
            dbres.res_id = Long.parseLong(res_id_lst[i]);
            dbres.res_upd_user = prof.usr_id;
            dbres.res_status = status;

//            dbres.checkResPermission(con, prof);
            //if (!dbResourcePermission.hasPermission(con, dbres.res_id, prof,
            //                            dbResourcePermission.RIGHT_WRITE))
            //    throw new qdbErrMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);

            dbres.updateStatus(con);
        }
    }

    public void del_res(Connection con, loginProfile prof)
        throws qdbErrMessage, qdbException, cwSysMessage
    {
            /*checkResPermission(con, prof);*/
            //if (!dbResourcePermission.hasPermission(con, res_id, prof,
            //                            dbResourcePermission.RIGHT_WRITE))
            //    throw new qdbErrMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);

            // delete access control list
            dbResourcePermission.delAll(con,res_id);

            del(con);

    }

	public String asXML(Connection con, boolean bTemplate, loginProfile prof, String dpo_view,String curr_stylesheet, boolean share_mode)
		  throws qdbException ,cwSysMessage, SQLException
	  {
		  StringBuffer result = new StringBuffer();
		  AcPageVariant acPageVariant = new AcPageVariant(con);
		  AcObjective acObj = new AcObjective(con);
		  Hashtable xslQuestions=AcXslQuestion.getQuestions();
		  long objId = dbObjective.getResObjRootId(con,res_id);
		  acPageVariant.ent_id = prof.usr_ent_id;
		  acPageVariant.obj_id = objId;
		  acPageVariant.rol_ext_id= prof.current_role;
		
		  //acPageVariant.admAccess = acObj.hasAdminPrivilege(prof.usr_ent_id,prof.current_role);

		  // xml header
		  result.append("<?xml version=\"1.0\" encoding=\"").append(dbUtils.ENC_UTF).append("\" standalone=\"no\" ?>").append(dbUtils.NEWL).append(dbUtils.NEWL)
		  .append("<resource id=\"").append(res_id).append("\" language=\"").append(res_lan).append("\" last_modified=\"").append(res_upd_user).append("\" timestamp=\"").append(res_upd_date).append("\" owner=\"")
		  .append(dbUtils.esc4XML(dbRegUser.usrId2SteUsrId(con,res_usr_id_owner))).append("\">").append(dbUtils.NEWL);
		  // author's information
		  result.append(prof.asXML()).append(dbUtils.NEWL);
		  // page variant
		  String metaXML = acPageVariant.answerPageVariantAsXML((String[]) xslQuestions.get(curr_stylesheet));
		  result.append(metaXML);
		  result.append("<cur_time>").append(dbUtils.getTime(con)).append("</cur_time>")
		  .append("<header type=\"").append(res_type).append("\" subtype=\"").append(res_subtype).append("\" difficulty=\"").append(res_difficulty).append("\" duration=\"").append(res_duration)
		  .append("\" privilege=\"").append(res_privilege).append("\" status=\"").append(res_status).append("\">").append(dbUtils.NEWL);
		  result.append(objAsXML(con));
		  if (bTemplate) {
			  result.append("<template_list>").append(dbUtils.NEWL)
			  .append(dbTemplate.tplListContentXML(con, prof, res_in_subtype))
			  .append("</template_list>").append(dbUtils.NEWL);
		  }
		  result.append("</header>").append(dbUtils.NEWL).append(dbUtils.NEWL);

		  result.append(getDisplayOption(con, dpo_view));
		  result.append("<share_mode>").append(share_mode).append("</share_mode>");
		  result.append("<body>")
		  .append("<title>").append(dbUtils.esc4XML(res_title)).append("</title>")
		  .append("<desc>").append(dbUtils.esc4XML(res_desc)).append("</desc>")
		  .append("<annotation>").append(res_annotation).append("</annotation>")
		  .append("<source type=\"").append(res_src_type).append("\">").append(dbUtils.esc4XML(res_src_link)).append("</source>")
		  //result += "<url>" + dbUtils.esc4XML(res_url) + "</url>" + dbUtils.NEWL;
		  //result += "<filename>" + dbUtils.esc4XML(res_filename) +  "</filename>" + dbUtils.NEWL;
		  .append("<instructor>").append(dbUtils.esc4XML(res_instructor_name)).append("</instructor>")
		  .append("<organization>").append(dbUtils.esc4XML(res_instructor_organization)).append("</organization>")
		  .append("<instructor>").append(dbUtils.esc4XML(res_instructor_name)).append("</instructor>")
		  .append("<moderator>").append(dbUtils.esc4XML(getOwnerName(con))).append("</moderator>")
		  .append("<organization>").append(dbUtils.esc4XML(res_instructor_organization)).append("</organization>");
		  if (res_type.equalsIgnoreCase(RES_TYPE_SCORM)) {
			  dbScormResource srs = new dbScormResource();
			  srs.srs_res_id = res_id;
			  srs.get(con);
			  result.append("<scorm_info  version=\"").append(res_sco_version).append("\">").append(srs.srs_structure_xml);
			  result.append("<child_res>");
			  Vector child_res_vec = dbResourceContent.getChildResources(con, res_id);
			  for (int i = 0; i < child_res_vec.size(); i ++) {
				  dbResource tmp_res = (dbResource) child_res_vec.get(i);
				  result.append("<res id=\"").append(tmp_res.res_id).append("\" src_link=\"").append(tmp_res.res_src_link).append("\" />");
			  }
			  result.append("</child_res></scorm_info>");
		  }
		  result.append("</body>");
		  //creation details
		  dbRegUser creator = new dbRegUser();
		  creator.get(con, res_usr_id_owner);
		  result.append("<creation>").append("<user id=\"").append(creator.usr_ste_usr_id).append("\"")
		  .append(" ent_id=\"").append(creator.usr_ent_id).append("\">")
		  .append("<display_bil>").append(cwUtils.esc4XML(creator.usr_display_bil)).append("</display_bil>")
		  .append("</user>").append("<timestamp>").append(this.res_create_date).append("</timestamp>").append("</creation>");
        
		  //last update details
		  dbRegUser lastAuthor = new dbRegUser();
		  lastAuthor.get(con, this.res_upd_user);
		  result.append("<last_update>").append("<user id=\"").append(lastAuthor.usr_ste_usr_id).append("\"")
		  .append(" ent_id=\"").append(lastAuthor.usr_ent_id).append("\">")
		  .append("<display_bil>").append(cwUtils.esc4XML(lastAuthor.usr_display_bil)).append("</display_bil>")
		  .append("</user>").append("<timestamp>").append(this.res_upd_date).append("</timestamp>").append("</last_update>")
		  .append(dbResourcePermission.aclAsXML(con,res_id,prof))
		  .append("</resource>");

		  return result.toString();
	  }

    // Get the display option of a resource
    public String getDisplayOption(Connection con, String view)
            throws qdbException
    {
        String xml = "";
        dbDisplayOption dpo = new dbDisplayOption();
        dpo.dpo_res_id = res_id;
        if(res_type != null && res_type.equalsIgnoreCase(RES_TYPE_COS)) {
            //get display option for a course
            dpo.dpo_res_type = res_type;
            dpo.dpo_res_subtype = res_type;
        }
        else if(res_in_subtype == null) {
            //get display option for a module
            dpo.dpo_res_type = res_type;
            dpo.dpo_res_subtype = res_subtype;
        }
        else {
            //get the display option for get_tpl and get_res as they do not have res_type
            dpo.dpo_res_type = RES_TYPE_MOD;
            dpo.dpo_res_subtype = res_in_subtype;
        }

        dpo.dpo_view = view;
        //xml = dpo.allViewAsXML(con);
        xml = dpo.getViewAsXML(con);
        return xml;

    }


    public void aeUpdStatus(Connection con)
        throws qdbErrMessage, SQLException
    {
        //checkTimeStamp(con);

        PreparedStatement stmt = con.prepareStatement(
            "UPDATE Resources set res_status = ?, res_upd_user = ? "  //changed, resources
            + " ,res_upd_date = ? "
            + " where res_id = ? "  );
        stmt.setString(1, res_status);
        stmt.setString(2, res_upd_user);
        stmt.setTimestamp(3, res_upd_date);
        stmt.setLong(4, res_id);

        if(stmt.executeUpdate()!=1)
        {
            con.rollback();
            throw new SQLException("Failed to update status. res_id = " + res_id);
        }
        else
        {
            stmt.close();
            return;
        }
    }

    public void aeUpdStatusNoTimestamp(Connection con)
        throws qdbErrMessage, SQLException
    {
        PreparedStatement stmt = con.prepareStatement(
            "UPDATE Resources set res_status = ? "  //changed, resources
            + " where res_id = ? "  );
        stmt.setString(1, res_status);
        stmt.setLong(2, res_id);

        if(stmt.executeUpdate()!=1)
        {
            con.rollback();
            throw new SQLException("Failed to update status. res_id = " + res_id);
        }
        else
        {
            stmt.close();
            return;
        }
    }

    public void ins(Connection con)
        throws qdbException
    {
        
        if(res_desc != null && res_desc.length() > RES_DESC_LENGTH) {
            res_desc = res_desc.substring(0,RES_DESC_LENGTH);
        }
        // assume connection is ready:
      try {
        PreparedStatement stmt = con.prepareStatement(
        "INSERT INTO Resources "  //changed resources
        + " ( res_lan "
        + " , res_title "
        + " , res_desc "
        + " , res_type "
        + " , res_subtype "
        + " , res_annotation "
        + " , res_format "
        + " , res_difficulty "
        + " , res_duration "
        + " , res_privilege "
        + " , res_status "
        + " , res_usr_id_owner "
        + " , res_tpl_name "
        + " , res_res_id_root "
        + " , res_mod_res_id_test "
        + " , res_create_date "
        + " , res_upd_user "
        + " , res_upd_date "
        + " , res_src_type "
        + " , res_src_link "
        + " , res_src_online_link "
        + " , res_instructor_name "
        + " , res_instructor_organization "
        + " , res_sco_version "
        + " , res_vod_duration "
        + " , res_img_link,res_vod_main "
        + ") values "
        + " ( ?, ?, ?, "
        + "   ?, ?, ?, NULL, "
        + "   ?, ?, ?, ?, "
        + "   ?, NULL, NULL, ?, " // usr_id_owner, tpl_name, res_id_root, mod_res_id_test
        + "   ?, ?, ?, "
        + "   ?, ?, "
        + "   ?, ?, ?, ?,"
        + "   ?, ?,"
        + "    NULL "
        + " ) ", PreparedStatement.RETURN_GENERATED_KEYS);

        Timestamp curTime = dbUtils.getTime(con);
        if(res_upd_date == null)
            res_upd_date = curTime;
        if(res_create_date == null)
            res_create_date = curTime;
        int count = 0;
        stmt.setString(++count, res_lan);
        stmt.setString(++count, res_title);
        stmt.setString(++count, res_desc);
        stmt.setString(++count, res_type);
        stmt.setString(++count, res_subtype);
        stmt.setString(++count, res_annotation);
        if (res_difficulty != 0) {
            stmt.setInt(++count, res_difficulty);
        } else {
            stmt.setInt(++count, 2);
        }
        stmt.setFloat(++count, res_duration);
        stmt.setString(++count, res_privilege);
        if (res_status != null) {
            stmt.setString(++count, res_status);
        } else {
            stmt.setString(++count, "ON");            
        }
        stmt.setString(++count, res_usr_id_owner);
        if (this.res_mod_res_id_test > 0) {
            stmt.setLong(++count, this.res_mod_res_id_test);
        } else {
            stmt.setNull(++count, java.sql.Types.INTEGER);
        }
        stmt.setTimestamp(++count, res_create_date);
        stmt.setString(++count, res_upd_user);
        stmt.setTimestamp(++count, res_upd_date);
        stmt.setString(++count, res_src_type);
        stmt.setString(++count, res_src_link);
        stmt.setString(++count, res_src_online_link);
        //if (res_filename != null && res_filename.length() > 0)
        //    res_url = null;
        //stmt.setString(17, res_url);
        //stmt.setString(18, res_filename);
        stmt.setString(++count, res_instructor_name);
        stmt.setString(++count, res_instructor_organization);
        stmt.setString(++count, res_sco_version);
        stmt.setLong(++count, res_vod_duration);
        stmt.setString(++count, res_img_link);
        /* insert */
        if(stmt.executeUpdate() != 1 )
        {
            // insert fails, rollback
            stmt.close();
            con.rollback();
            throw new qdbException("Fails to insert Resource");
        }
        /* get new id */
        res_id = cwSQL.getAutoId(con, stmt, "Resources", "res_id");
        stmt.close();
        if(res_vod_main != null){
        	String columnName[]={"res_vod_main"};
            String columnValue[]={res_vod_main};
            String condition = "res_id= " + res_id;
            cwSQL.updateClobFields(con, "Resources",columnName,columnValue, condition);
        }
      } catch(SQLException e) {
        throw new qdbException("SQL Error: " + e.getMessage());
      }

        return;
    }

    public void aeUpd(Connection con)
        throws qdbException, cwSysMessage
    {
        res_status = getResStatus(con, res_id);
        updRes(con, true);
    }

    public void upd(Connection con)
        throws qdbException
    {
        upd(con, true);
    }
    
    public void upd(Connection con, boolean isChangeStatus)
        throws qdbException
    {
        Timestamp curTime = dbUtils.getTime(con);
        res_upd_date = curTime;
        updRes(con, isChangeStatus);
    }

    public void updRes(Connection con, boolean isChangeStatus)
        throws qdbException
    {
        if(res_desc != null && res_desc.length() > RES_DESC_LENGTH) {
            res_desc = res_desc.substring(0,RES_DESC_LENGTH);
        }

      try {

        String upd_rSQL = "UPDATE Resources SET ";  //changed, resources

        upd_rSQL += " res_lan = ? "
                    + " , res_title = ? "
                    + " , res_desc = ? " 
                    + " , res_type = ? "      //3
                    + " , res_subtype = ? "
                    + " , res_annotation = ? "
                    + " , res_format = ? "
                    + " , res_difficulty = ? "
                    + " , res_duration = ? "
                    + " , res_privilege = ? ";
	    if (isChangeStatus) {
	        upd_rSQL += " , res_status = ? ";  
	    }        
	    upd_rSQL += " , res_upd_user = ? "
                    + " , res_upd_date = ? "
                    + " , res_src_type = ? ";
		          if (res_src_link != null && res_src_link.length() > 0) {
		              upd_rSQL += " , res_src_link = ? ";  
		          }  
	          upd_rSQL += " , res_instructor_name = ? " //15
                    + " , res_instructor_organization = ? "
                    + " , res_vod_duration = ? ";
          if(res_img_link != null && res_img_link.length() > 0){
          	upd_rSQL += " , res_img_link = ? ";
          }
          if(res_src_online_link != null && res_src_online_link.length() > 0) {
        	  upd_rSQL += " , res_src_online_link = ? ";
          }
          upd_rSQL   += " where res_id = ? ";         //17

        PreparedStatement stmt = con.prepareStatement(upd_rSQL);
        int index = 1;
        stmt.setString(index++, res_lan);
        stmt.setString(index++, res_title);
        stmt.setString(index++, res_desc);
        stmt.setString(index++, res_type);
        stmt.setString(index++, res_subtype);
        stmt.setString(index++, res_annotation);
        stmt.setString(index++, res_format);
        stmt.setInt(index++, res_difficulty);
        stmt.setFloat(index++, res_duration);
        stmt.setString(index++, res_privilege);
        if (isChangeStatus) {
            stmt.setString(index++, res_status);
        }
        stmt.setString(index++, res_upd_user);
        stmt.setTimestamp(index++, res_upd_date);
        stmt.setString(index++, res_src_type);
        if (res_src_link != null && res_src_link.length() > 0) {
        	stmt.setString(index++, res_src_link);
        }
        stmt.setString(index++, res_instructor_name);
        stmt.setString(index++, res_instructor_organization);
        stmt.setLong(index++, res_vod_duration);
        if(res_img_link != null && res_img_link.length() > 0){
        	stmt.setString(index++, res_img_link);
        }
        if(res_src_online_link != null && res_src_online_link.length() > 0) {
        	stmt.setString(index++, res_src_online_link);
        }
        stmt.setLong(index++, res_id);


        /* update */
        if(stmt.executeUpdate() != 1 )
        {
            // update fails, rollback
        	stmt.close();
            con.rollback();
            throw new qdbException("Fails to update Resource");
        }

        stmt.close();
        
        String columnName[]={"res_vod_main"};
        String columnValue[]={res_vod_main};
        String condition = "res_id= " + res_id;
        cwSQL.updateClobFields(con, "Resources",columnName,columnValue, condition);
      } catch(SQLException e) {
        throw new qdbException("SQL Error: " + e.getMessage());
      }

      return;

    }

    // 2001/6/26 by cliff
    // only update basic fields of the resource table
    public void updBasicInfo(Connection con, boolean isChangeStatus)
        throws qdbException, qdbErrMessage
    {
        Timestamp curTime = dbUtils.getTime(con);
        res_upd_date = curTime;
        updResBasicInfo(con, isChangeStatus);
    }

    public void updResBasicInfo(Connection con, boolean isChangeStatus)
        throws qdbException
    {
        if(res_desc != null && res_desc.length() > RES_DESC_LENGTH) {
            res_desc = res_desc.substring(0,RES_DESC_LENGTH);
        }

      try {
        String upd_rSQL = "UPDATE Resources SET ";  //changed, resources

        upd_rSQL += " res_lan = ? "
                 + " , res_title = ? "
                 + " , res_desc = ? "
                 + " , res_difficulty = ? "
                 + " , res_privilege = ? ";
        if (isChangeStatus) {
            upd_rSQL += " , res_status = ? ";
        }
        upd_rSQL += " , res_upd_user = ? "
                 + " , res_upd_date = ? "
                 + " where res_id = ? ";

        PreparedStatement stmt = con.prepareStatement(upd_rSQL);
        int index = 1;
        stmt.setString(index++, res_lan);
        stmt.setString(index++, res_title);
        stmt.setString(index++, res_desc);
        stmt.setInt(index++, res_difficulty);
        stmt.setString(index++, res_privilege);
        if (isChangeStatus) {
            stmt.setString(index++, res_status);
        }
        stmt.setString(index++, res_upd_user);
        stmt.setTimestamp(index++, res_upd_date);
        stmt.setLong(index++, res_id);


        /* update */
        if(stmt.executeUpdate() != 1 )
        {
            // update fails, rollback
            con.rollback();
            throw new qdbException("Fails to update Resource");
        }

        stmt.close();

      } catch(SQLException e) {
        throw new qdbException("SQL Error: " + e.getMessage());
      }

      return;

    }

    public void del(Connection con)
        throws qdbException, qdbErrMessage, cwSysMessage
    {

      try {

        PreparedStatement stmt = con.prepareStatement(
         "DELETE From ResourceObjective where rob_res_id = ?");

        stmt.setLong(1, res_id);
        stmt.executeUpdate();
        stmt.close();

        stmt = con.prepareStatement("DELETE From Resources where res_id = ?");  //changed, resouces
        stmt.setLong(1,res_id);
        /* delete */
        if(stmt.executeUpdate() != 1 )
        {
            // delete fails, rollback
            stmt.close();
            con.rollback();
            //throw new qdbException("Fails to delete Resource");
            throw new cwSysMessage(dbUtils.MSG_REC_NOT_FOUND, "Resource ID = " + res_id);
        }

        stmt.close();

      } catch(SQLException e) {
        throw new qdbException("SQL Error: " + e.getMessage());
      }

      return;

    }

    public String getOwnerName(Connection con) throws qdbException {
        try {
            String OwnerName = "";
            String SQL = "SELECT usr_display_bil "
                       + "FROM RegUser, Resources "
                       + "WHERE res_usr_id_owner = usr_id "
                       + "AND res_id = ? ";

            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, res_id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()==true)
                OwnerName = rs.getString("usr_display_bil");
            else
                throw new qdbException ("Cannot find owner. usr_id = " + res_usr_id_owner);

            stmt.close();
            return OwnerName;
        }
        catch(SQLException e) {
            throw new qdbException (e.getMessage());
        }
    }

    public void get(Connection con)
            throws qdbException, cwSysMessage
    {
        try {
            PreparedStatement stmt = con.prepareStatement(
            "SELECT "
            +   "res_lan "
            +   " , res_title "
            +   " , res_desc "
            +   " , res_type "
            +   " , res_subtype "
            +   " , res_annotation "
            +   " , res_format "
            +   " , res_difficulty "
            +   " , res_duration "
            +   " , res_privilege "
            +   " , res_status "
            +   " , res_usr_id_owner "
            +   " , res_create_date "
            +   " , res_tpl_name "
            +   " , res_res_id_root "
            +   " , res_mod_res_id_test "
            +   " , res_upd_user "
            +   " , res_upd_date "
            +   " , res_src_type "
            +   " , res_src_link "
            +   " , res_src_online_link "
            //+   " , res_url "
            //+   " , res_filename "
            +   " , res_instructor_name "
            +   " , res_instructor_organization "
            +   " , res_sco_version"
            +   " , res_vod_duration "
            +   " , res_img_link "
            +   " , res_vod_main"
            +   " from Resources"  //changed, resources
            +   " where res_id =?");

            // set the values for prepared statements
            stmt.setLong(1, res_id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next())
            {
                res_lan = rs.getString("res_lan");
                res_title = rs.getString("res_title");
                res_desc = rs.getString("res_desc");
                res_type = rs.getString("res_type");
                res_subtype = rs.getString("res_subtype");
                res_annotation = rs.getString("res_annotation");
                res_format = rs.getString("res_format");
                res_difficulty = rs.getInt("res_difficulty");
                res_duration = rs.getFloat("res_duration");
                res_privilege = rs.getString("res_privilege");
                res_status = rs.getString("res_status");
                res_usr_id_owner = rs.getString("res_usr_id_owner");
                res_create_date = rs.getTimestamp("res_create_date");
                res_tpl_name = rs.getString("res_tpl_name");
                res_res_id_root = rs.getLong("res_res_id_root");
                res_mod_res_id_test = rs.getLong("res_mod_res_id_test");
                res_upd_user = rs.getString("res_upd_user");
                res_upd_date = rs.getTimestamp("res_upd_date");
                res_src_type = rs.getString("res_src_type");
                res_src_link = rs.getString("res_src_link");
                res_src_online_link = rs.getString("res_src_online_link");
                //res_url = rs.getString("res_url");
                //res_filename = rs.getString("res_filename");
                res_instructor_name = rs.getString("res_instructor_name");
                res_instructor_organization = rs.getString("res_instructor_organization");
                res_sco_version = rs.getString("res_sco_version");
                res_vod_duration = rs.getInt("res_vod_duration");
                res_img_link = rs.getString("res_img_link");
                res_vod_main = cwSQL.getClobValue(con, rs, "res_vod_main");
            }
            else {
            	stmt.close();
                //throw new qdbException( "No data for resource. id = " + res_id );
                throw new cwSysMessage(dbUtils.MSG_REC_NOT_FOUND, "Resource ID = " + res_id);
            }

            stmt.close();
        } catch(SQLException e) {
             throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    public static String getResDesc(Connection con, long resId)
        throws qdbException
    {
        try {
            PreparedStatement stmt = con.prepareStatement(
            " SELECT res_desc FROM Resources "
            + " where res_id = ? ");

            // set the values for prepared statements
            stmt.setLong(1, resId);
            String desc = new String("");

            ResultSet rs = stmt.executeQuery();
            if (rs.next())
            {
                //xml = rs.getString("cos_structure_xml");
                desc = rs.getString("res_desc");
                //desc = cwSQL.getClobValue(con, rs, "res_desc");
            }
            else
                throw new qdbException( "No data for resource. id = " + resId );

            stmt.close();

            return desc;

        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    public static String getResTitle(Connection con, long resId)
        throws cwException
    {
        try {
            PreparedStatement stmt = con.prepareStatement(
            " SELECT res_title FROM Resources "
            + " where res_id = ? ");

            // set the values for prepared statements
            stmt.setLong(1, resId);
            String title = new String("");

            ResultSet rs = stmt.executeQuery();
            if (rs.next())
            {
                title = rs.getString("res_title");
            }
            else
                throw new cwException( "No data for resource. id = " + resId );

            stmt.close();

            return title;

        } catch(SQLException e) {
            throw new cwException("SQL Error: " + e.getMessage());
        }
    }
    
    public static String getResCode(Connection con, long resId)
            throws cwException
        {
            try {
                PreparedStatement stmt = con.prepareStatement(
                " SELECT res_code FROM Resources "
                + " where res_id = ? ");

                // set the values for prepared statements
                stmt.setLong(1, resId);
                String title = new String("");

                ResultSet rs = stmt.executeQuery();
                if (rs.next())
                {
                    title = rs.getString("res_code");
                }
                else
                    throw new cwException( "No data for resource. id = " + resId );

                stmt.close();

                return title;

            } catch(SQLException e) {
                throw new cwException("SQL Error: " + e.getMessage());
            }
        }

    public String objAsXML(Connection con)
        throws qdbException ,cwSysMessage
    {

        String result = "";
        Vector objIdVec = new Vector();
        objIdVec = dbResourceObjective.getObjId(con,res_id);

        try {
            // Get the root res id if the question is duplicated
            if (objIdVec.size() == 0 ) {
                PreparedStatement stmt1 = con.prepareStatement(
                " SELECT res_res_id_root "
                +   " from Resources "  //changed, resource
                +   " where res_id =? " );

                // set the values for prepared statements
                stmt1.setLong(1, res_id);

                ResultSet rs1 = stmt1.executeQuery();
                if(rs1.next()) {
                     long rootResId = rs1.getLong("res_res_id_root");
                     objIdVec =  dbResourceObjective.getObjId(con, rootResId);
                }else {
                     //throw new qdbException("Failed to get resource objective. ");
                     throw new cwSysMessage(dbUtils.MSG_REC_NOT_FOUND, "Resource ID = " + res_id);
                }

                stmt1.close();
            }

	        dbResourceObjective rob = new dbResourceObjective();
	        dbObjective obj = new dbObjective();
	
	        int i;
	        for (i=0;i<objIdVec.size();i++)
	        {
	            rob = (dbResourceObjective) objIdVec.elementAt(i);
	            obj.obj_id = rob.rob_obj_id;
	            obj.get(con);
	
	            result += "<objective id=\"" + obj.obj_id + "\" type=\"" + obj.obj_type + "\" status=\""+obj.obj_status ;
	            result += "\" >" + 
	            "<desc>"+dbUtils.esc4XML(obj.obj_desc) + "</desc>"+
	            obj.getObjPathAsXML(con)+	
	            "</objective>" + dbUtils.NEWL;
	            
	        }
        }
        catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }

        return result;
    }

    public void lock(Connection con)
        throws qdbException
    {
        try {
            PreparedStatement stmt = con.prepareStatement(
            " Update Resources Set res_title = res_title Where res_id = ? ");

            stmt.setLong(1, res_id);
            stmt.executeUpdate();
            stmt.close();
        }
        catch(SQLException e) {
            throw new qdbException(e.getMessage());
        }
    }


    public void checkTimeStamp(Connection con) // check last upd time first
        throws qdbException, qdbErrMessage
    {
       try {
            PreparedStatement stmt = con.prepareStatement(
            " SELECT res_upd_date from Resources where res_id = ? " );  //changed, resources

            stmt.setLong(1, res_id);
            ResultSet rs = stmt.executeQuery();
            boolean bTSOk = false;
            if(rs.next())
            {
                Timestamp t = rs.getTimestamp(1);
                Timestamp tTmp = t;
                if (res_upd_date == null) {
                    bTSOk = false;
                }else {
                    tTmp.setNanos(res_upd_date.getNanos());
                    if(tTmp.equals(res_upd_date))
                        bTSOk = true;
                }
            }

            if(!bTSOk) {
                con.rollback();
                throw new qdbErrMessage(INVALID_TIMESTAMP_MSG);
            }

            stmt.close();

        }catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }

    }

    public void updateTimeStamp(Connection con) // UPDATE usr_udp_date
        throws qdbException
    {
       try {
            Timestamp curTime = dbUtils.getTime(con);
            PreparedStatement stmt = con.prepareStatement(
                " UPDATE Resources SET res_upd_date = ? "  //changed, resources
              + "    , res_upd_user = ? "
              + " WHERE res_id = ?" );

            stmt.setTimestamp(1, curTime);
            stmt.setString(2, res_upd_user);
            stmt.setLong(3, res_id);
            if(stmt.executeUpdate()!=1)
            {
            	stmt.close();
                con.rollback();
                throw new qdbException("Failed to update status.");
            }
            stmt.close();

      }catch(SQLException e) {
        throw new qdbException("SQL Error: " + e.getMessage());
      }

    }

    // cliff, 2001/4/27
    public void updateTimeStamp(Connection con, Timestamp curTime) // UPDATE usr_udp_date
        throws qdbException
    {
       try {
            PreparedStatement stmt = con.prepareStatement(
                " UPDATE Resources SET res_upd_date = ? "  //changed, resources
              + "    , res_upd_user = ? "
              + " WHERE res_id = ?" );

            stmt.setTimestamp(1, curTime);
            stmt.setString(2, res_upd_user);
            stmt.setLong(3, res_id);
            if(stmt.executeUpdate()!=1)
            {
                con.rollback();
                throw new qdbException("Failed to update status.");
            }
            stmt.close();

      }catch(SQLException e) {
        throw new qdbException("SQL Error: " + e.getMessage());
      }

    }


    // Lun
    public Timestamp getUpdateTimeStamp(Connection con)
        throws qdbException, SQLException {

            PreparedStatement stmt = con.prepareStatement(
                " SELECT res_upd_date FROM Resources WHERE res_id = ? " );

            stmt.setLong(1, res_id);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()){
                Timestamp tempTime=rs.getTimestamp("res_upd_date");
                stmt.close();
                return tempTime;
            }else{
                stmt.close();
                throw new qdbException("Failed to get update timestamp");
            }


        }


    public String getContentListAsXMLNoHeader(Connection con, loginProfile prof, String order,
                                      int usr_record, String dpo_view, int cal_d, int cal_m, int cal_y,
                                      Timestamp start_datetime, Timestamp end_datetime,
                                      boolean checkStatusMod, boolean checkStatusRes)
        throws qdbException, cwSysMessage, cwException
    {
        String xml = getContentListAsXML(con, prof, order, usr_record, dpo_view, cal_d, cal_m, cal_y, start_datetime, end_datetime, checkStatusMod, checkStatusRes);
        return xml.substring(xml.indexOf("?>")+2);
    }

    // add for tracking history id
    public String getContentListAsXML(Connection con, loginProfile prof, String order,
                                      int usr_record, String dpo_view, int cal_d, int cal_m, int cal_y,
                                      Timestamp start_datetime, Timestamp end_datetime,
                                      boolean checkStatusMod, boolean checkStatusRes)
        throws qdbException, cwSysMessage, cwException
    {


        try {
            String SQL = "SELECT res_type RTYPE, res_subtype RSUBTYPE "
                        + " FROM Resources where "
                        + "     res_id = ? ";

            if(res_usr_id_owner !=null && res_usr_id_owner.length() > 0)
                SQL += " and res_usr_id_owner = '" + res_usr_id_owner + "' ";

            String resource_type = null;
            String resource_subtype=null;
            PreparedStatement stmt1 = con.prepareStatement(SQL);
            stmt1.setLong(1, res_id);
            ResultSet rs1 = stmt1.executeQuery();
            if(rs1.next())
            {
                resource_type = rs1.getString("RTYPE");
                resource_subtype = rs1.getString("RSUBTYPE");

            }
            stmt1.close();

            String xml = null;
            if(resource_type!=null && resource_type.equalsIgnoreCase(RES_TYPE_COS)) {
                xml = getCosContentListAsXML(con, prof, order, usr_record, dpo_view, cal_d, cal_m,  cal_y, start_datetime, end_datetime, checkStatusMod);
            }else if (resource_type!=null && resource_type.equalsIgnoreCase(RES_TYPE_MOD)) {
                xml = getModContentListAsXML(con, prof, order, usr_record, dpo_view, cal_d, cal_m,  cal_y, start_datetime, end_datetime, checkStatusRes);
            }else {
                xml = dbUtils.xmlHeader;
            }

            return xml;
        }
        //catch(qdbErrMessage ee) {
        //    throw new qdbException(ee.getMessage());
        // }
        catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    static final String sql_get_mod_content_header = "SELECT res_title RTITLE, res_type RTYPE, res_subtype RSUBTYPE, res_status RSTATUS, "
                    + " res_privilege RPRIV, res_upd_date RTIMESTAMP "
                    + " ,mod_eff_start_datetime MESDATE ,mod_eff_end_datetime MEEDATE "
                    + " FROM Resources, Module where "
                    + "    res_id = ? "
                    + " and mod_res_id = res_id ";

	static final String sql_get_res_content_header = "SELECT res_title RTITLE, res_type RTYPE, res_subtype RSUBTYPE, res_status RSTATUS, "
                    + " res_privilege RPRIV, res_upd_date RTIMESTAMP "
                    + " FROM Resources where " 
                    + "    res_id = ? ";

    static final String sql_get_mod_content =
            "SELECT res_id RID, res_type RTYPE, res_subtype RSUBTYPE, rcn_sub_nbr RSUBNBR, rcn_order RORDER, rcn_score_multiplier RMUL, "
        + "       res_privilege RPRIV, res_status RSTATUS, res_usr_id_owner ROWNER, "
        + "       res_lan RLAN, "
        + "       res_upd_date  RTIMESTAMP, res_title RTITLE, "
        + "       res_difficulty RDIFF, res_duration RDUR, res_desc , "
        + "       res_instructor_name RISTNAME, res_instructor_organization RISTORG, "
        + "       res_src_type RES_SRC_TYPE, res_src_link RES_SRC_LINK, "
        + "       res_tpl_name RTPLNAME "
        + "  FROM Resources, ResourceContent "
        + " WHERE res_id = rcn_res_id_content ";

	static final String sql_get_que_container_content = 
            "SELECT res_id RID, res_type RTYPE, res_subtype RSUBTYPE, rcn_sub_nbr RSUBNBR, rcn_order RORDER, rcn_score_multiplier RMUL, " 
        + "       res_privilege RPRIV, res_status RSTATUS, res_usr_id_owner ROWNER, "
        + "       res_lan RLAN, "
        + "       res_upd_date  RTIMESTAMP, res_title RTITLE, "
        + "       res_difficulty RDIFF, res_duration RDUR, res_desc , "   
        + "       res_instructor_name RISTNAME, res_instructor_organization RISTORG, "   
        + "       res_src_type RES_SRC_TYPE, res_src_link RES_SRC_LINK, "
        + "       res_tpl_name RTPLNAME "
        + "  FROM Resources, ResourceContent, Question "
        + " WHERE res_id = rcn_res_id_content and res_id = que_res_id ";


    private String getModContentListAsXML(Connection con, loginProfile prof, String order,
                                      int usr_record, String dpo_view, int cal_d, int cal_m, int cal_y,
                                      Timestamp start_datetime, Timestamp end_datetime, boolean checkStatus)
        throws qdbException, cwSysMessage, SQLException, cwException
    {
    	if (tkh_id == DbTrackingHistory.TKH_ID_UNDEFINED){
                tkh_id = DbTrackingHistory.getAppTrackingIDByMod(con, res_id, prof.usr_ent_id);
    	}

        String SQL = sql_get_mod_content_header;

        if(res_usr_id_owner !=null && res_usr_id_owner.length() > 0)
            SQL += " and res_usr_id_owner = '" + res_usr_id_owner + "' ";


        PreparedStatement stmt1 = con.prepareStatement(SQL);
        stmt1.setLong(1, res_id);
        ResultSet rs1 = stmt1.executeQuery();

        String resource_type = "";
        String resource_subtype=null;

        //Dennis, imple release date control
        Timestamp cur_time=null;
        Timestamp eff_start_datetime=null;
        Timestamp eff_end_datetime=null;
        String tmp_end_datetime = null; //use to store the eff_end_datetime as it may == "UNLIMITED"
        String tmp_res_type=null;

        StringBuffer xmlHeader = new StringBuffer();
        if(rs1.next())
        {
            resource_type = rs1.getString("RTYPE");
            resource_subtype = rs1.getString("RSUBTYPE");

            cur_time = dbUtils.getTime(con);
            eff_start_datetime = rs1.getTimestamp("MESDATE");
            eff_end_datetime = rs1.getTimestamp("MEEDATE");
            if(eff_end_datetime != null) {
                if(dbUtils.isMaxTimestamp(eff_end_datetime) == true)
                    tmp_end_datetime = dbUtils.UNLIMITED; //convert to String to "UNLIMITED"
                else
                    tmp_end_datetime = eff_end_datetime.toString();
            }

            //StringBuffer
            xmlHeader.append("<resource id=\"").append(res_id);
            xmlHeader.append("\" type=\"").append(rs1.getString("RTYPE"));
            xmlHeader.append("\" subtype=\"").append(rs1.getString("RSUBTYPE"));
            xmlHeader.append("\" status=\"").append(rs1.getString("RSTATUS")).append("\" privilege=\"").append(rs1.getString("RPRIV"));
            xmlHeader.append("\" eff_start_datetime=\"").append(eff_start_datetime);
            xmlHeader.append("\" eff_end_datetime=\"").append(tmp_end_datetime);
            xmlHeader.append("\" cur_time=\"").append(cur_time);

            xmlHeader.append("\" timestamp=\"" + rs1.getTimestamp("RTIMESTAMP") + "\">");
            xmlHeader.append(dbResourcePermission.aclAsXML(con,res_id, prof));
            xmlHeader.append("<title>" + dbUtils.esc4XML(rs1.getString("RTITLE")) + "</title>" + dbUtils.NEWL);
            xmlHeader.append("</resource>" + dbUtils.NEWL);

        }
        else
            throw new qdbException("Failed to get resource header.");

        stmt1.close();

        SQL = sql_get_mod_content;
        SQL += " AND rcn_tkh_id = -1 AND rcn_res_id = " + res_id;

        //Dennis, check for a specific resource content sub type
        if(res_cnt_subtype != null && res_cnt_subtype.length() > 0)
            SQL += " AND res_subtype = '" + res_cnt_subtype + "' ";

        //filter out the offline resources for students
        /*if(prof.sysRoles.contains(dbUserGroup.USG_ROLE_STUDENT)) */
        if(checkStatus)
            SQL += " AND res_status = '" + RES_STATUS_ON + "' ";

        SQL += "  ORDER BY rcn_order ASC, RTITLE ASC ";

        StringBuffer xmlBody = new StringBuffer(2048);

        PreparedStatement stmt = con.prepareStatement(SQL);

        ResultSet rs = stmt.executeQuery();
        Vector modList = new Vector();
        long RID, RSUBNBR, RMUL;
        int RORDER;
        String RLAN, RTYPE, RSUBTYPE, RTITLE, RPRIV, ROWNER, RSTATUS, RATTEMPTED;
        Timestamp RTIMESTAMP;
        int RDIFF;
        float RDUR;
        float MMAXSCORE;
        int MPASSSCORE;
        Timestamp PSTART, PCOMPLETE, PLASTACC;
        String MINSTRUCT;
        String RDESC;
        long PATTEMPTNBR;
        String RISTNAME, RISTORG, RTPLNAME;
        dbEvent dbevt;
        String evtBody;

        String RES_SRC_TYPE;
        String RES_SRC_LINK;

        // For AICC only
        String MOD_WEB_LAUNCH = "";
        String MOD_VENDOR = "";

        while(rs.next())
        {
            RID = rs.getLong("RID");
            RTYPE = rs.getString("RTYPE");
            RSUBTYPE = rs.getString("RSUBTYPE");
            //MTYPE = rs.getString("MTYPE");
            RORDER = rs.getInt("RORDER");
            RLAN = rs.getString("RLAN");
            RTITLE = dbUtils.esc4XML(rs.getString("RTITLE"));
            RSUBNBR = rs.getLong("RSUBNBR");
            RPRIV = rs.getString("RPRIV");
            ROWNER = rs.getString("ROWNER");
            RSTATUS = rs.getString("RSTATUS");
            RTIMESTAMP = rs.getTimestamp("RTIMESTAMP");
            RMUL   = rs.getLong("RMUL");
            RDIFF = rs.getInt("RDIFF");
            RDUR = rs.getFloat("RDUR");
            //RDESC = dbUtils.esc4XML(cwSQL.getClobValue(con, rs, "res_desc"));
            RDESC = dbUtils.esc4XML(rs.getString("res_desc"));
            RISTNAME = dbUtils.esc4XML(rs.getString("RISTNAME"));
            RISTORG = dbUtils.esc4XML(rs.getString("RISTORG"));
            RTPLNAME = rs.getString("RTPLNAME");

            // for AICC
            RES_SRC_TYPE = rs.getString("RES_SRC_TYPE");
            RES_SRC_LINK = rs.getString("RES_SRC_LINK");

            PreparedStatement stmt2 = null;
            ResultSet rs2 = null;
            try {
                stmt2 = con.prepareStatement(
                "SELECT COUNT(*) "
                + "  FROM ProgressAttempt where atm_pgr_res_id = ? or atm_int_res_id = ? and atm_tkh_id = ? "  );
                stmt2.setLong(1, RID);
                stmt2.setLong(2, RID);
                stmt2.setLong(3, tkh_id);
                rs2 = stmt2.executeQuery();
                if(rs2.next())
                {
                    int cnt = rs2.getInt(1);

                    if (cnt!=0)
                        RATTEMPTED = RES_ATTEMPTED_TRUE;
                    else
                        RATTEMPTED = RES_ATTEMPTED_FALSE;
                }
                else
                    throw new qdbException("Failed to get progress record.");
            } finally {
                if (stmt2 != null) {
                    stmt2.close();
                }
            }

            xmlBody.append("<item id=\"").append(RID).append("\" type=\"").append(RTYPE).append("\" subtype=\"").append(RSUBTYPE).append("\" order=\"")
                .append(RORDER).append("\" sub_num=\"").append(RSUBNBR)
                .append("\" score_multiplier=\"").append(RMUL).append("\" privilege=\"").append(RPRIV)
                .append("\" status=\"").append(RSTATUS).append("\" owner=\"").append(dbUtils.esc4XML(dbRegUser.usrId2SteUsrId(con,ROWNER)))
                .append("\" language=\"").append(RLAN)
                .append("\" attempted=\"").append(RATTEMPTED)
                .append("\" difficulty=\"").append(RDIFF)
                .append("\" duration=\"").append(RDUR)
                .append("\" time_limit=\"").append(RDUR)
                .append("\" suggested_time=\"").append(RDUR);

                xmlBody.append("\" res_src_type=\"");
                if (RES_SRC_TYPE != null) {
                    xmlBody.append(RES_SRC_TYPE);
                }
                else {
                    xmlBody.append("");
                }
                xmlBody.append("\" res_src_link=\"");
                if (RES_SRC_LINK != null) {
                    xmlBody.append(dbUtils.esc4XML(RES_SRC_LINK));
                }
                else {
                    xmlBody.append("");
                }

                xmlBody.append("\" timestamp=\"").append(RTIMESTAMP).append("\">").append(dbUtils.NEWL)
                .append("<title>").append(RTITLE).append("</title>").append(dbUtils.NEWL)
                .append("<desc>").append(RDESC).append("</desc>").append(dbUtils.NEWL);

                xmlBody.append("<organization>").append(RISTORG).append("</organization>").append(dbUtils.NEWL);


                if(RTPLNAME != null && RTPLNAME.trim().length() > 0) {
                    dbTemplate dbtpl = new dbTemplate();
                    dbtpl.tpl_name = RTPLNAME;
                    dbtpl.tpl_lan = prof.label_lan;
                    dbtpl.get(con);
                    xmlBody.append("<stylesheet>").append(dbtpl.tpl_stylesheet).append("</stylesheet>");
                }

            xmlBody.append("</item>").append(dbUtils.NEWL);

            modList.addElement(new Long(RID)) ;
        }
        stmt.close();

        StringBuffer result = new StringBuffer(2048);
        result.append(dbUtils.xmlHeader);
        result.append("<resource_content>").append(dbUtils.NEWL);
        //Dennis, check for a specific resource content sub type
        if(res_cnt_subtype != null && res_cnt_subtype.length() > 0)
            result.append("<content_subtype>").append(res_cnt_subtype).append("</content_subtype>").append(dbUtils.NEWL);

        result.append(prof.asXML() + dbUtils.NEWL);
        result.append("<cur_time>").append(dbUtils.getTime(con)).append("</cur_time>").append(dbUtils.NEWL);
        if (cal_d != -100 || cal_m != -100 || cal_y != -100) {
        //-100 means that the parma has not been passed in, so don't output it
            result.append("<cal ");
            if(cal_d != -100)
                result.append("d=\"").append(cal_d).append("\" ");
            if(cal_m != -100)
                result.append("m=\"").append(cal_m).append("\" ");
            if(cal_y != -100)
                result.append("y=\"").append(cal_y).append("\" ");
            result.append("/>").append(dbUtils.NEWL);
        }
        result.append("<header>").append(dbUtils.NEWL);
        result.append(xmlHeader.toString());
        result.append("</header>").append(dbUtils.NEWL);
        result.append(xmlBody.toString());

        result.append(dbResourcePermission.aclAsXML(con, modList, prof));
        result.append("</resource_content>").append(dbUtils.NEWL);

        return result.toString();

    }

    public String getQueContainerListAsXML(Connection con, loginProfile prof, boolean checkStatus) throws cwException, qdbException, SQLException {
        StringBuffer result = new StringBuffer(2048);
        result.append(dbUtils.xmlHeader).append(dbUtils.NEWL);
        result.append("<resource_content>").append(dbUtils.NEWL);
        result.append(getQueContainerListNoHeaderAsXML(con, prof, checkStatus)).append(dbUtils.NEWL);
        result.append("</resource_content>").append(dbUtils.NEWL);
        return result.toString();
    }

    public String getQueContainerListNoHeaderAsXML(Connection con, loginProfile prof, boolean checkStatus) throws cwException, qdbException, SQLException {
        return getQueContainerListNoHeaderAsXML(con, prof, checkStatus, "que_score");
    }

    public String getQueContainerListNoHeaderAsXML(Connection con, loginProfile prof, boolean checkStatus, String orderby) throws cwException, qdbException, SQLException {

        String SQL = "";
        SQL = sql_get_res_content_header;

        if (res_usr_id_owner != null && res_usr_id_owner.length() > 0)
            SQL += " and res_usr_id_owner = '" + res_usr_id_owner + "' ";

        PreparedStatement stmt1 = con.prepareStatement(SQL);
        stmt1.setLong(1, res_id);
        ResultSet rs1 = stmt1.executeQuery();

        String resource_type = "";
        String resource_subtype = null;

        //Dennis, imple release date control
        Timestamp cur_time = null;
        Timestamp eff_start_datetime = null;
        Timestamp eff_end_datetime = null;
        String tmp_end_datetime = null; //use to store the eff_end_datetime as it may == "UNLIMITED"
        String tmp_res_type = null;

        StringBuffer xmlHeader = new StringBuffer();
        if (rs1.next()) {
            resource_type = rs1.getString("RTYPE");
            resource_subtype = rs1.getString("RSUBTYPE");

            cur_time = dbUtils.getTime(con);

            //StringBuffer

            xmlHeader.append("<resource id=\"").append(res_id);
            xmlHeader.append("\" type=\"").append(rs1.getString("RTYPE"));
            xmlHeader.append("\" subtype=\"").append(rs1.getString("RSUBTYPE"));
            xmlHeader.append("\" status=\"").append(rs1.getString("RSTATUS")).append("\" privilege=\"").append(rs1.getString("RPRIV"));
            xmlHeader.append("\" cur_time=\"").append(cur_time);

            xmlHeader.append("\" timestamp=\"" + rs1.getTimestamp("RTIMESTAMP") + "\">");
            xmlHeader.append(dbResourcePermission.aclAsXML(con, res_id, prof));
            xmlHeader.append("<title>" + dbUtils.esc4XML(rs1.getString("RTITLE")) + "</title>" + dbUtils.NEWL);
            xmlHeader.append("</resource>" + dbUtils.NEWL);

        } else
            throw new cwException("Failed to get resource header.");

        stmt1.close();

        SQL = sql_get_que_container_content;
        SQL += " AND rcn_res_id = " + res_id;

        //Dennis, check for a specific resource content sub type
        if (res_cnt_subtype != null && res_cnt_subtype.length() > 0)
            SQL += " AND res_subtype = '" + res_cnt_subtype + "' ";

        //filter out the offline resources for students
        /*if(prof.sysRoles.contains(dbUserGroup.USG_ROLE_STUDENT)) */
        if (checkStatus)
            SQL += " AND res_status = '" + RES_STATUS_OFF + "' ";

        SQL += "  ORDER BY " + orderby + " ASC ";

        StringBuffer xmlBody = new StringBuffer(2048);

        PreparedStatement stmt = con.prepareStatement(SQL);

        ResultSet rs = stmt.executeQuery();
        Vector modList = new Vector();
        long RID, RSUBNBR, RMUL;
        int RORDER;
        String RLAN, RTYPE, RSUBTYPE, RTITLE, RPRIV, ROWNER, RSTATUS, RATTEMPTED;
        Timestamp RTIMESTAMP;
        int RDIFF;
        float RDUR;
        float MMAXSCORE;
        int MPASSSCORE;
        Timestamp PSTART, PCOMPLETE, PLASTACC;
        String MINSTRUCT;
        String RDESC;
        long PATTEMPTNBR;
        String RISTNAME, RISTORG, RTPLNAME;
        dbEvent dbevt;
        String evtBody;

        String RES_SRC_TYPE;
        String RES_SRC_LINK;

        // For AICC only
        String MOD_WEB_LAUNCH = "";
        String MOD_VENDOR = "";

        while (rs.next()) {
            RID = rs.getLong("RID");
            RTYPE = rs.getString("RTYPE");
            RSUBTYPE = rs.getString("RSUBTYPE");
            //MTYPE = rs.getString("MTYPE");
            RORDER = rs.getInt("RORDER");
            RLAN = rs.getString("RLAN");
            RTITLE = rs.getString("RTITLE");
            RSUBNBR = rs.getLong("RSUBNBR");
            RPRIV = rs.getString("RPRIV");
            ROWNER = rs.getString("ROWNER");
            RSTATUS = rs.getString("RSTATUS");
            RTIMESTAMP = rs.getTimestamp("RTIMESTAMP");
            RMUL = rs.getLong("RMUL");
            RDIFF = rs.getInt("RDIFF");
            RDUR = rs.getFloat("RDUR");
            RDESC = rs.getString("res_desc");
            RISTNAME = dbUtils.esc4XML(rs.getString("RISTNAME"));
            if (rs.getString("RISTORG") != null) {
                RISTORG = dbUtils.esc4XML(rs.getString("RISTORG"));
            } else {
                RISTORG = "";
            }
            RTPLNAME = rs.getString("RTPLNAME");

            // for AICC
            RES_SRC_TYPE = rs.getString("RES_SRC_TYPE");
            RES_SRC_LINK = rs.getString("RES_SRC_LINK");

            PreparedStatement stmt2 = con.prepareStatement("SELECT COUNT(*) " + "  FROM ProgressAttempt where atm_pgr_res_id = ? or atm_int_res_id = ? ");
            stmt2.setLong(1, RID);
            stmt2.setLong(2, RID);
            ResultSet rs2 = stmt2.executeQuery();
            if (rs2.next()) {
                int cnt = rs2.getInt(1);

                if (cnt != 0)
                    RATTEMPTED = RES_ATTEMPTED_TRUE;
                else
                    RATTEMPTED = RES_ATTEMPTED_FALSE;
            } else
                throw new cwException("Failed to get progress record.");
			xmlBody.append("<item id=\"").append(RID).append("\" type=\"").append(RTYPE).append("\" subtype=\"").append(RSUBTYPE)
                .append("\" order=\"").append(RORDER)
                .append("\" sub_num=\"").append(RSUBNBR)
				.append("\" score_multiplier=\"").append(RMUL).append("\" privilege=\"").append(RPRIV)
				.append("\" status=\"").append(RSTATUS).append("\" owner=\"").append(dbUtils.esc4XML(dbRegUser.usrId2SteUsrId(con,ROWNER)))
				.append("\" language=\"").append(RLAN)
				.append("\" attempted=\"").append(RATTEMPTED)
				.append("\" difficulty=\"").append(RDIFF)
				.append("\" duration=\"").append(RDUR)
				.append("\" time_limit=\"").append(RDUR)
				.append("\" suggested_time=\"").append(RDUR);  
                    
				xmlBody.append("\" res_src_type=\"");
				if (RES_SRC_TYPE != null) {
					xmlBody.append(RES_SRC_TYPE);
				}
				else {
					xmlBody.append("");
				}
				xmlBody.append("\" res_src_link=\"");
				if (RES_SRC_LINK != null) {
					xmlBody.append(dbUtils.esc4XML(RES_SRC_LINK));
				}
				else {
					xmlBody.append("");
				}

				xmlBody.append("\" timestamp=\"").append(RTIMESTAMP).append("\">").append(dbUtils.NEWL)
				.append("<title>").append(dbUtils.esc4XML(RTITLE)).append("</title>").append(dbUtils.NEWL)
				.append("<desc>").append(dbUtils.esc4XML(RDESC)).append("</desc>").append(dbUtils.NEWL);
                    
				xmlBody.append("<organization>").append(RISTORG).append("</organization>").append(dbUtils.NEWL);
                    
                    
				if(RTPLNAME != null && RTPLNAME.trim().length() > 0) {
					dbTemplate dbtpl = new dbTemplate();
					dbtpl.tpl_name = RTPLNAME;
					dbtpl.tpl_lan = prof.label_lan;
					dbtpl.get(con);
					xmlBody.append("<stylesheet>").append(dbtpl.tpl_stylesheet).append("</stylesheet>");
				}

			xmlBody.append("</item>").append(dbUtils.NEWL);
                    
			modList.addElement(new Long(RID)) ; 
		}
        stmt.close();                    
            
		StringBuffer result = new StringBuffer(2048);
		//result.append(dbUtils.xmlHeader);
		//result.append("<resource_content>").append(dbUtils.NEWL);
		//Dennis, check for a specific resource content sub type
		if(res_cnt_subtype != null && res_cnt_subtype.length() > 0) 
			result.append("<content_subtype>").append(res_cnt_subtype).append("</content_subtype>").append(dbUtils.NEWL);
            
		result.append(prof.asXML() + dbUtils.NEWL);
		result.append("<cur_time>").append(dbUtils.getTime(con)).append("</cur_time>").append(dbUtils.NEWL);
		result.append("<header>").append(dbUtils.NEWL);
		result.append(xmlHeader.toString());
		result.append("</header>").append(dbUtils.NEWL);
		result.append(xmlBody.toString());
            
		result.append(dbResourcePermission.aclAsXML(con, modList, prof));
		//result.append("</resource_content>").append(dbUtils.NEWL);

		return result.toString();
            
	}


    private static final String sql_get_cos_content_header =
            "SELECT res_title RTITLE, res_type RTYPE, res_subtype RSUBTYPE, res_status RSTATUS, "
            + " res_privilege RPRIV, res_upd_date RTIMESTAMP "
            + " ,cos_structure_xml,cos_structure_json "
            + " ,cos_eff_start_datetime CESDATE, cos_eff_end_datetime CEEDATE "
            + " ,childItem.itm_id itm_id, childItem.itm_run_ind itm_run_ind "
            + " ,childItem.itm_eff_start_datetime, att_create_timestamp "
            + " ,parentItem.itm_id pItm_id, parentItem.itm_title pItm_title "
            + " ,parentItem.itm_icon pItm_icon,childItem.itm_icon, childItem.itm_type ,childItem.itm_exam_ind ,childItem.itm_blend_ind ,childItem.itm_ref_ind "
            + " FROM Resources "
            + " INNER JOIN Course ON (res_id = cos_res_id) "
            + " INNER JOIN aeItem childItem ON (cos_itm_id = childItem.itm_id) "
            + " INNER JOIN aeApplication On ( childItem.itm_id = app_itm_id ) "
            + " INNER JOIN aeAttendance on ( app_id = att_app_id )"
            + " LEFT JOIN aeItemRelation ON (cos_itm_id = ire_child_itm_id) " 
            + " LEFT JOIN aeItem parentItem ON (ire_parent_itm_id = parentItem.itm_id) "
            + " WHERE  res_id = ? AND app_tkh_id = ? and app_ent_id = ?"; 

    private static final String sql_get_cos_content =
            "SELECT res_id RID, res_type RTYPE, res_subtype RSUBTYPE, rcn_sub_nbr RSUBNBR, rcn_order RORDER, rcn_score_multiplier RMUL, "
            + "       res_privilege RPRIV, res_status RSTATUS, res_usr_id_owner ROWNER, "
            + "       res_lan RLAN, "
            + "       res_upd_date  RTIMESTAMP, res_title RTITLE, "
            + "       res_difficulty RDIFF, res_duration RDUR, res_desc , "
            + "       res_instructor_name RISTNAME, res_instructor_organization RISTORG, "
            + "       res_src_type RES_SRC_TYPE, res_src_link RES_SRC_LINK, "
            + "       res_tpl_name RTPLNAME, "
            + "       mod_eff_start_datetime MESDATE, mod_eff_end_datetime MEEDATE "
            + "  FROM Resources, ResourceContent, Module "
            + " WHERE res_id = rcn_res_id_content "
            + "   AND mod_res_id = rcn_res_id_content ";

    private static String sql_get_cos_content1 = null;

    public String getCosContentSQL() {
        if (sql_get_cos_content1 == null) {
            sql_get_cos_content1 = "SELECT res.res_id RID, res.res_type RTYPE, res.res_subtype RSUBTYPE, rcn_sub_nbr RSUBNBR," 
            + " rcn_order RORDER, rcn_score_multiplier RMUL,"
            + " res.res_privilege RPRIV, res.res_status RSTATUS, res.res_usr_id_owner ROWNER,"
            + " res.res_lan RLAN,"
            + " res.res_upd_date RTIMESTAMP, res.res_title RTITLE, "
            + " res.res_difficulty RDIFF, res.res_duration RDUR, res.res_desc,"
            + " res.res_instructor_name RISTNAME, res.res_instructor_organization RISTORG, "
            + " res.res_src_type RES_SRC_TYPE, res.res_src_link RES_SRC_LINK,"
            + " res.res_tpl_name RTPLNAME,"
            + " res.res_sco_version sco_version,"
            + " mod_max_usr_attempt MMUATTEMPT,"
            + " mod_eff_start_datetime MESDATE, mod_eff_end_datetime MEEDATE,"
            + " mod_max_score MMAXSCORE, mod_pass_score MPASSSCORE, mod_instruct MINSTRUCT,"
            + " mod_web_launch MOD_WEB_LAUNCH, mod_vendor MOD_VENDOR,"
            + " mov.mov_score, mov.mov_ent_id, mov.mov_mod_id,"
            + " mov.mov_last_acc_datetime, mov.mov_update_timestamp,"
            + " mov.mov_total_time, mov.mov_total_attempt, mov.mov_cos_id,"
            + " mov.mov_status, mov.mov_ele_loc,"
            + " ass_due_date_day, ass_due_datetime,"
            + " pgr_attempt_nbr, pgr_status, pgr_completion_status, "
            + " rrq_req_res_id,preRes.res_title pre_res_title , rrq_status, preMov.mov_status pre_status,"
            + " tpl_stylesheet, mod_download_ind, mod_required_time"
            + " FROM Resources res"
            + " INNER JOIN ResourceContent " 
                + (cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType()) ? "with(nolock)":"") 
                + " On( res.res_id = rcn_res_id_content )"
            + " INNER JOIN Module On ( rcn_res_id_content = mod_res_id )"
            + " LEFT JOIN ModuleEvaluation mov "
                + (cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType()) ? "with(nolock)":"") 
                + " ON (mod_res_id = mov.mov_mod_id and mov.mov_tkh_id = ? )"
            + " LEFT JOIN Assignment ON ( res.res_id = ass_res_id )"
            + " LEFT JOIN Progress p1 ON ( res.res_id = p1.pgr_res_id  and p1.pgr_tkh_id = ? and p1.pgr_usr_id = ?) "
            + " LEFT JOIN ResourceRequirement ON ( res.res_id = rrq_res_id )"
            + " Left join Resources preRes on(rrq_req_res_id = preRes.res_id)"
            + " LEFT JOIN ModuleEvaluation preMov "
                + (cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType()) ? "with(nolock)":"") 
                + " ON ( rrq_req_res_id = preMov.mov_mod_id and preMov.mov_tkh_id = ? )"
            + " LEFT JOIN Template ON (res.res_tpl_name = tpl_name and tpl_lan = ? )"
            + " WHERE ";
        }
        return sql_get_cos_content1;
    }
    public String getCosContentListAsXML(Connection con, loginProfile prof, String order,
                                      int usr_record, String dpo_view, int cal_d, int cal_m, int cal_y,
                                      Timestamp start_datetime, Timestamp end_datetime, boolean checkStatus)
        throws qdbException, cwSysMessage, cwException
    {
        try {
            String tkh_type = DbTrackingHistory.TKH_TYPE_APPLICATION;
            String SQL = sql_get_cos_content_header;
            if(res_usr_id_owner !=null && res_usr_id_owner.length() > 0)
                SQL += " and res_usr_id_owner = '" + res_usr_id_owner + "' ";

            String resource_type = "";
            String resource_subtype=null;
            //Dennis, imple release date control
            Timestamp cur_time = dbUtils.getTime(con);
            Timestamp eff_start_datetime=null;
            Timestamp eff_end_datetime=null;
            String tmp_end_datetime = null; //use to store the eff_end_datetime as it may == "UNLIMITED"
            Timestamp itm_eff_start_datetime = null;
            Timestamp att_create_timestamp = null;
            boolean itm_create_run_ind = false;
            String cosStructXML = new String();

            StringBuffer xmlHeader = new StringBuffer(4096);
            PreparedStatement stmt1 = con.prepareStatement(SQL);
            int index1 = 1;
            stmt1.setLong(index1++, res_id);
            stmt1.setLong(index1++, tkh_id);
            stmt1.setLong(index1++, prof.usr_ent_id);
            ResultSet rs1 = stmt1.executeQuery();
            if(rs1.next())
            {
                resource_type = rs1.getString("RTYPE");
                resource_subtype = rs1.getString("RSUBTYPE");

                //Dennis, impl cos release control
                //output the cos_eff_start_datetime, cos_eff_end_datetime and current time to XML
                eff_start_datetime = rs1.getTimestamp("CESDATE");
                eff_end_datetime = rs1.getTimestamp("CEEDATE");
                if(eff_end_datetime != null) {
                    if(dbUtils.isMaxTimestamp(eff_end_datetime) == true)
                        tmp_end_datetime = dbUtils.UNLIMITED; //convert to String to "UNLIMITED"
                    else
                        tmp_end_datetime = eff_end_datetime.toString();
                }
                itm_create_run_ind = rs1.getBoolean("itm_run_ind");
                itm_eff_start_datetime = rs1.getTimestamp("itm_eff_start_datetime");
                att_create_timestamp = rs1.getTimestamp("att_create_timestamp");
                //StringBuffer
                xmlHeader.append("<resource id=\"").append(res_id);
                xmlHeader.append("\" type=\"").append(rs1.getString("RTYPE"));
                xmlHeader.append("\" subtype=\"").append(rs1.getString("RSUBTYPE"));
                xmlHeader.append("\" status=\"").append(rs1.getString("RSTATUS")).append("\" privilege=\"").append(rs1.getString("RPRIV"));
                xmlHeader.append("\" eff_start_datetime=\"").append(eff_start_datetime);
                xmlHeader.append("\" eff_end_datetime=\"").append(tmp_end_datetime);
                xmlHeader.append("\" cur_time=\"").append(cur_time);

                xmlHeader.append("\" timestamp=\"").append(rs1.getTimestamp("RTIMESTAMP")).append("\">");
                xmlHeader.append("<title>").append(dbUtils.esc4XML(rs1.getString("RTITLE"))).append("</title>").append(dbUtils.NEWL);			
                if(rs1.getBoolean("itm_run_ind")) {
					xmlHeader.append("<parent_item id=\"").append(rs1.getLong("pItm_id")).append("\">")
					         .append("<title>").append(cwUtils.esc4XML(rs1.getString("pItm_title"))).append("</title>")
					         .append("</parent_item>");
				}
                xmlHeader.append("</resource>").append(dbUtils.NEWL);

                // Course Structure
                cosStructXML = cwSQL.getClobValue(rs1, "cos_structure_xml");
                xmlHeader.append(cosStructXML);
                if (location ==null)
                    location = new String();
                xmlHeader.append("<location identifier=\"").append(location).append("\" />").append(dbUtils.NEWL);
                xmlHeader.append("<tracking_id>").append(tkh_id).append("</tracking_id>").append(dbUtils.NEWL);
            }
            else {
                stmt1.close();
                throw new qdbException("Failed to get resource header.");
            }
            stmt1.close();
			Vector modIdVec = dbResource.getNodeModuleIds(cosStructXML, location);
			String sql_mod_id_lst = null;
			String tableName = null;
			if(modIdVec != null) {
				if(modIdVec.size() == 0) {
					sql_mod_id_lst = "( 0 )";
				} else if(modIdVec.size() < 100) {
					 sql_mod_id_lst = cwUtils.vector2list(modIdVec);
				} else {
					 tableName= cwSQL.createSimpleTemptable(con, "tmp_res_id", cwSQL.COL_TYPE_LONG, 0);
					 cwSQL.insertSimpleTempTable(con, tableName, modIdVec, cwSQL.COL_TYPE_LONG);
					 sql_mod_id_lst = " ( select tmp_res_id from " + tableName + ") ";
				}
			}
            SQL = getCosContentSQL()
                + "  rcn_res_id_content IN " + sql_mod_id_lst
                +  " AND res.res_id = rcn_res_id_content "
                +  " AND mod_res_id = rcn_res_id_content "
                +  " AND rcn_res_id = " + res_id
                +  " AND (pgr_complete_datetime is null or pgr_complete_datetime = ( select max(pgr_complete_datetime) FROM Progress p2 WHERE p2.pgr_res_id = p1.pgr_res_id and p2.pgr_tkh_id = ? and p2.pgr_usr_id = ?))";

            //Dennis, check for a specific resource content sub type
            if(res_cnt_subtype != null && res_cnt_subtype.length() > 0)
                SQL += " AND res.res_subtype = '" + res_cnt_subtype + "' ";

            //filter out the offline modules for students
            if(checkStatus)
                SQL += " AND res.res_status <> '" + RES_STATUS_OFF + "' ";

            //Dennis, only retrieve the modules fall within the input range of time
            //used to speed up the calendar
            if(start_datetime != null && end_datetime != null) {
                SQL += "And NOT(mod_eff_end_datetime < ? ) "
                        + "And NOT(mod_eff_start_datetime > ?) ";
            }
            else if(start_datetime != null) {
                SQL += "And NOT(mod_eff_end_datetime < ? ) ";
            }
            else if(end_datetime != null) {
                SQL += "And NOT(mod_eff_start_datetime < ? ) ";
            }
            // Lun : only select the modules created by the instructor
            if(mod_usr_id_instructor != null && mod_usr_id_instructor.length() > 0)
                SQL += " And mod_usr_id_instructor = '" + mod_usr_id_instructor + "' ";


            SQL += "  ORDER BY mod_eff_start_datetime DESC ,";
            if (order !=null && !order.equalsIgnoreCase("res_title")) {
                if (order.equalsIgnoreCase("res_type"))
                    SQL += "  RSUBTYPE ASC,  " ;
                else
                    SQL +=  order + ",";
            }

            SQL += "  RTITLE ASC " ;
            if (cwSQL.DBVENDOR_DB2.equalsIgnoreCase(cwSQL.getDbType())) {
                SQL += " for read only";
            }
            StringBuffer xmlBody = new StringBuffer(4096);

            PreparedStatement stmt = con.prepareStatement(SQL);
            int index = 1;
            stmt.setLong(index++, tkh_id);
            stmt.setLong(index++, tkh_id);
            stmt.setString(index++, prof.usr_id);
            stmt.setLong(index++, tkh_id);
            stmt.setString(index++, prof.label_lan);
            stmt.setLong(index++, tkh_id);
            stmt.setString(index++, prof.usr_id);
            if(start_datetime != null) {
                stmt.setTimestamp(index++, start_datetime);
            }
            if(end_datetime != null) {
                stmt.setTimestamp(index++, end_datetime);
            }

            Vector modList = new Vector();
            long RID, RSUBNBR, RMUL, MMUATTEMPT;
            int RORDER;
            String RLAN, RTYPE, RSUBTYPE, RTITLE, RPRIV, ROWNER, RSTATUS, RATTEMPTED;
            Timestamp RTIMESTAMP;
            int RDIFF;
            float RDUR;
            float MMAXSCORE;
            int MPASSSCORE;
            Timestamp PSTART, PCOMPLETE, PLASTACC;
            String MINSTRUCT;
            String RDESC;
            long PATTEMPTNBR;
            String RISTNAME, RISTORG, RTPLNAME;
            dbEvent dbevt;
            String evtBody;
            String RES_SRC_TYPE;
            String RES_SRC_LINK;

            // For AICC only
            String MOD_WEB_LAUNCH = "";
            String MOD_VENDOR = "";
            ResultSet rs = stmt.executeQuery();
			Hashtable dpo_hash = aeAction.diplayOption_hash;
            while(rs.next())
            {   

            	RID = rs.getLong("RID");
                RTYPE = rs.getString("RTYPE");
                RSUBTYPE = rs.getString("RSUBTYPE");
                RORDER = rs.getInt("RORDER");
                RLAN = rs.getString("RLAN");
                RTITLE = dbUtils.esc4XML(rs.getString("RTITLE"));
                RSUBNBR = rs.getLong("RSUBNBR");
                RPRIV = rs.getString("RPRIV");
                ROWNER = rs.getString("ROWNER");
                RSTATUS = rs.getString("RSTATUS");
                RTIMESTAMP = rs.getTimestamp("RTIMESTAMP");
                RMUL   = rs.getLong("RMUL");
                RDIFF = rs.getInt("RDIFF");
                RDUR = rs.getFloat("RDUR");
                RDESC = dbUtils.esc4XML(rs.getString("res_desc"));
                RISTNAME = dbUtils.esc4XML(rs.getString("RISTNAME"));
                RISTORG = dbUtils.esc4XML(rs.getString("RISTORG"));
                RTPLNAME = rs.getString("RTPLNAME");
                MMUATTEMPT = rs.getLong("MMUATTEMPT");
           
                // for AICC
                RES_SRC_TYPE = rs.getString("RES_SRC_TYPE");
                RES_SRC_LINK = rs.getString("RES_SRC_LINK");

                String RSTAT = ""; // NEW
                eff_start_datetime=null;
                eff_end_datetime=null;
                tmp_end_datetime = null; //use to store the eff_end_datetime as it may == "UNLIMITED"

                eff_start_datetime = rs.getTimestamp("MESDATE");
                eff_end_datetime = rs.getTimestamp("MEEDATE");
                if(eff_end_datetime != null)
                    if(dbUtils.isMaxTimestamp(eff_end_datetime) == true)
                        tmp_end_datetime = dbUtils.UNLIMITED; //convert to String to "UNLIMITED"
                    else
                        tmp_end_datetime = eff_end_datetime.toString();

                //Dennis, display option, get the attributes that needed to be displayed
                MMAXSCORE = 0;
                MPASSSCORE = 0;
                MINSTRUCT = "";
                PSTART = null;
                PCOMPLETE = null;
                PLASTACC = null;
                PATTEMPTNBR = 0;
                //get from rs
                MMAXSCORE = rs.getFloat("MMAXSCORE");
                MPASSSCORE =rs.getInt("MPASSSCORE");
                MINSTRUCT = dbUtils.esc4XML(rs.getString("MINSTRUCT"));
                MOD_WEB_LAUNCH = rs.getString("MOD_WEB_LAUNCH");
                MOD_VENDOR = rs.getString("MOD_VENDOR");
                PATTEMPTNBR = rs.getInt("pgr_attempt_nbr");
                xmlBody.append("<item id=\"").append(RID).append("\" type=\"").append(RTYPE).append("\" subtype=\"").append(RSUBTYPE).append("\" order=\"")
                    .append(RORDER).append("\" sub_num=\"").append(RSUBNBR)
                    .append("\" score_multiplier=\"").append(RMUL).append("\" privilege=\"").append(RPRIV);
                xmlBody.append("\" status=\"").append(RSTATUS)
                   .append("\" language=\"").append(RLAN);

                xmlBody.append("\" max_user_attempt=\"").append(Long.toString(MMUATTEMPT));
                xmlBody.append("\" difficulty=\"").append(RDIFF)
                    .append("\" duration=\"").append(RDUR)
                    .append("\" time_limit=\"").append(RDUR)
                    .append("\" suggested_time=\"").append(RDUR);
                if (RSUBTYPE != null && RSUBTYPE.equalsIgnoreCase("ASS")) {
                    int ass_due_date_day = rs.getInt("ass_due_date_day");
                    Timestamp ass_due_datetime = rs.getTimestamp("ass_due_datetime");
                    String due_date = dbAssignment.getDueDate(ass_due_date_day, ass_due_datetime, itm_create_run_ind,itm_eff_start_datetime, att_create_timestamp);
                    if (due_date != null) {
                        xmlBody.append("\" due_datetime=\"").append(due_date);
                    }
                }
                xmlBody.append("\" eff_start_datetime=\"").append(eff_start_datetime);
                xmlBody.append("\" eff_end_datetime=\"").append(tmp_end_datetime);
                xmlBody.append("\" cur_time=\"").append(cur_time);
                xmlBody.append("\" max_score=\"").append(MMAXSCORE);
                xmlBody.append("\" pass_score=\"").append(MPASSSCORE);
                xmlBody.append("\" attempt_nbr=\"").append(PATTEMPTNBR);
                xmlBody.append("\" pgr_start=\"").append(PSTART);
                xmlBody.append("\" pgr_complete=\"").append(PCOMPLETE);
                xmlBody.append("\" pgr_last_acc=\"").append(PLASTACC);

                // for AICC
                xmlBody.append("\" mod_vendor=\"");
                if (MOD_VENDOR != null) {
                    xmlBody.append(MOD_VENDOR);
                }
                else {
                    xmlBody.append("");
                }
                xmlBody.append("\" mod_web_launch=\"");
                if (MOD_WEB_LAUNCH != null) {
                    xmlBody.append(dbUtils.esc4XML(MOD_WEB_LAUNCH));
                }
                else {
                    xmlBody.append("");
                }
                xmlBody.append("\" res_src_type=\"");
                if (RES_SRC_TYPE != null) {
                    xmlBody.append(RES_SRC_TYPE);
                }
                else {
                    xmlBody.append("");
                }
                xmlBody.append("\" res_src_link=\"");
                if (RES_SRC_LINK != null) {
                    xmlBody.append(dbUtils.esc4XML(RES_SRC_LINK));
                }
                else {
                    xmlBody.append("");
                }
                //
                xmlBody.append("\" timestamp=\"").append(RTIMESTAMP).append("\">").append(dbUtils.NEWL)
                .append("<title>").append(RTITLE).append("</title>").append(dbUtils.NEWL)
                .append("<desc>").append(RDESC).append("</desc>").append(dbUtils.NEWL);

                xmlBody.append("<organization>").append(RISTORG).append("</organization>").append(dbUtils.NEWL);

                 xmlBody.append("<instruct>").append(MINSTRUCT).append("</instruct>").append(dbUtils.NEWL);
                 if(RTPLNAME != null && RTPLNAME.trim().length() > 0) {
                    xmlBody.append("<stylesheet>").append(rs.getString("tpl_stylesheet")).append("</stylesheet>");
                 }
                //get display option
                dbDisplayOption dpo = new dbDisplayOption();
                dpo = (dbDisplayOption)dpo_hash.get(RSUBTYPE);
                if(dpo != null) {
                	xmlBody.append(dpo.getViewAsXML());	
                }
                if (RSTAT.length() != 0) {
                    xmlBody.append(RSTAT).append(dbUtils.NEWL);
                }
                //get  Module Prerequisite module information
                ModulePrerequisiteManagement modulemrerequisitemanagement= new ModulePrerequisiteManagement();
                long rrq_req_res_id = rs.getLong("rrq_req_res_id");
                String pre_status = rs.getString("rrq_status");
                String mov_status = rs.getString("pre_status");
                xmlBody.append("<pre_mod_inf>").append(dbUtils.NEWL);
                xmlBody.append("<complete_pre>")
                       .append(modulemrerequisitemanagement.hasCompletePreMod(rrq_req_res_id, pre_status, mov_status))
                       .append("</complete_pre>").append(dbUtils.NEWL);
                xmlBody.append(modulemrerequisitemanagement.getPreModXML( rrq_req_res_id, pre_status));
                xmlBody.append("</pre_mod_inf>").append(dbUtils.NEWL);
                xmlBody.append(dbModuleEvaluation.getAiccDataXML(rs, tkh_id));
                xmlBody.append("</item>").append(dbUtils.NEWL);
                modList.addElement(new Long(RID)) ;
            }
            stmt.close();
            StringBuffer result = new StringBuffer(4096);
            result.append(dbUtils.xmlHeader);
            result.append("<resource_content>").append(dbUtils.NEWL);
            //Dennis, check for a specific resource content sub type
            if(res_cnt_subtype != null && res_cnt_subtype.length() > 0)
                result.append("<content_subtype>").append(res_cnt_subtype).append("</content_subtype>").append(dbUtils.NEWL);

            result.append(prof.asXML()).append(dbUtils.NEWL);
            result.append("<cur_time>").append(cur_time).append("</cur_time>").append(dbUtils.NEWL);
            if (cal_d != -100 || cal_m != -100 || cal_y != -100) {
            //-100 means that the parma has not been passed in, so don't output it
                result.append("<cal ");
                if(cal_d != -100)
                    result.append("d=\"").append(cal_d).append("\" ");
                if(cal_m != -100)
                    result.append("m=\"").append(cal_m).append("\" ");
                if(cal_y != -100)
                    result.append("y=\"").append(cal_y).append("\" ");
                result.append("/>").append(dbUtils.NEWL);
            }
            result.append("<header>").append(dbUtils.NEWL);
            result.append(xmlHeader.toString());
            result.append("</header>").append(dbUtils.NEWL);
            result.append(xmlBody.toString());
            result.append("</resource_content>").append(dbUtils.NEWL);
            if(tableName != null) {
            	cwSQL.dropTempTable(con, tableName);
            }
            return result.toString();

        }
        catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }
    /**
     * 获取网上内容
     * @param con
     * @param prof
     * @param order
     * @param checkStatus
     * @param resultMap
     * @param defJsonConfig
     * @throws qdbException
     * @throws cwSysMessage
     * @throws cwException
     */
    public void getCosContentListAsJson(Connection con, loginProfile prof, String order,  boolean checkStatus,HashMap resultMap ,JsonConfig defJsonConfig ,Hashtable onLineStatusHs ,WizbiniLoader wizbini, String cov_status) throws qdbException,
			cwSysMessage, cwException {
		try {
			String tkh_type = DbTrackingHistory.TKH_TYPE_APPLICATION;
		
			String SQL =   "SELECT res_title RTITLE, res_type RTYPE, res_subtype RSUBTYPE, res_status RSTATUS, "
	            + " res_privilege RPRIV, res_upd_date RTIMESTAMP "
	            + " ,cos_structure_xml,cos_structure_json "
	            + " ,cos_eff_start_datetime CESDATE, cos_eff_end_datetime CEEDATE "
	            + " ,childItem.itm_id itm_id, childItem.itm_run_ind itm_run_ind "
	            + " ,childItem.itm_eff_start_datetime ,childItem.itm_content_eff_start_datetime ," 
	            + cwSQL.replaceNull("parentItem.itm_eff_end_datetime", "childItem.itm_eff_end_datetime")+" itm_eff_end_datetime,"
	            + cwSQL.replaceNull("parentItem.itm_content_eff_end_datetime", "childItem.itm_content_eff_end_datetime")+" itm_content_eff_end_datetime,"
	            + cwSQL.replaceNull("parentItem.itm_content_eff_duration", "childItem.itm_content_eff_duration")+" itm_content_eff_duration "
	            + " ,att_create_timestamp "
	            + " ,parentItem.itm_id pItm_id, parentItem.itm_title pItm_title "
	            + " ,parentItem.itm_icon pItm_icon,childItem.itm_icon, childItem.itm_type ,childItem.itm_exam_ind ,childItem.itm_blend_ind ,childItem.itm_ref_ind "
	            + " FROM Resources "
	            + " INNER JOIN Course ON (res_id = cos_res_id) "
	            + " INNER JOIN aeItem childItem ON (cos_itm_id = childItem.itm_id) "
	            + " INNER JOIN aeApplication On ( childItem.itm_id = app_itm_id ) "
	            + " INNER JOIN aeAttendance on ( app_id = att_app_id )"
	            + " LEFT JOIN aeItemRelation ON (cos_itm_id = ire_child_itm_id) " 
	            + " LEFT JOIN aeItem parentItem ON (ire_parent_itm_id = parentItem.itm_id) "
	            + " WHERE  res_id = ? AND app_tkh_id = ? and app_ent_id = ?"; 

			if (res_usr_id_owner != null && res_usr_id_owner.length() > 0)
				SQL += " and res_usr_id_owner = '" + res_usr_id_owner + "' ";

			String resource_type = "";
			String resource_subtype = null;
			Hashtable folderHs =new Hashtable();
			// Dennis, imple release date control
			Timestamp cur_time = dbUtils.getTime(con);
			Timestamp eff_start_datetime = null;
			Timestamp eff_end_datetime = null;
			String tmp_end_datetime = null; // use to store the eff_end_datetime
											// as it may == "UNLIMITED"
			Timestamp itm_eff_start_datetime = null;
			Timestamp itm_content_eff_start_datetime =null;
			Timestamp att_create_timestamp = null;
			boolean itm_create_run_ind = false;
			String cosStructXML = new String();
			StringBuffer xmlHeader = new StringBuffer(4096);
			PreparedStatement stmt1 = con.prepareStatement(SQL);
			int index1 = 1;
			stmt1.setLong(index1++, res_id);
			stmt1.setLong(index1++, tkh_id);
			stmt1.setLong(index1++, prof.usr_ent_id);
			ResultSet rs1 = stmt1.executeQuery();
			if (rs1.next()) {
				resource_type = rs1.getString("RTYPE");
				resource_subtype = rs1.getString("RSUBTYPE");
				eff_start_datetime = rs1.getTimestamp("CESDATE");
				eff_end_datetime = rs1.getTimestamp("CEEDATE");
				itm_create_run_ind = rs1.getBoolean("itm_run_ind");
				itm_eff_start_datetime = rs1
						.getTimestamp("itm_eff_start_datetime");
				itm_content_eff_start_datetime = rs1.getTimestamp("itm_content_eff_start_datetime");
				att_create_timestamp = rs1.getTimestamp("att_create_timestamp");
				ResourceBean res = new ResourceBean();
				res.setTkh_id(tkh_id);
				res.setCov_status(cov_status);
				res.setId(res_id);
				res.setType(rs1.getString("RTYPE"));
				res.setSubtype(rs1.getString("RSUBTYPE"));
				res.setStatus(rs1.getString("RSTATUS"));
				res.setPrivilege(rs1.getString("RPRIV"));
				res.setEff_start_datetime(eff_start_datetime);
				res.setEff_ent_datetime(eff_end_datetime);
				res.setCur_time(cur_time);
				res.setTimestamp(rs1.getTimestamp("RTIMESTAMP"));
				res.setTitle(rs1.getString("RTITLE"));
				res.setItm_content_eff_start_datetime(itm_content_eff_start_datetime);
				if (rs1.getBoolean("itm_run_ind")) {
					res.setParent_item_id(rs1.getLong("pItm_id"));
					res.setParent_item_title(rs1.getString("pItm_title"));
				}	
				String itmDir = wizbini.cfgSysSetupadv.getFileUpload().getItmDir().getName();
				long itemId = 0;
				String itmIcon = "";
				boolean run_ind = rs1.getBoolean("itm_run_ind");
				if(run_ind){
					itemId=rs1.getLong("pItm_id");
					itmIcon = rs1.getString("pItm_icon");
				}else{
					itemId=rs1.getLong("itm_id");
					itmIcon = rs1.getString("itm_icon");
				}
				String itmIconPath = Course.getItmIconPath(itmDir, itemId, itmIcon);
				res.setItm_icon(itmIconPath);
					
				String itm_type= rs1.getString("itm_type");
				boolean blend_ind =  rs1.getBoolean("itm_blend_ind");
				boolean exam_ind =  rs1.getBoolean("itm_exam_ind");
				boolean ref_ind =  rs1.getBoolean("itm_ref_ind");								
				String dummy_type = aeItemDummyType.getDummyItemType(itm_type, blend_ind, exam_ind, ref_ind);
				res.setItm_dummy_type(dummy_type);
				
				int itm_content_duration = rs1.getInt("itm_content_eff_duration");
				Timestamp itm_content_eff_end_datetime = rs1.getTimestamp("itm_content_eff_end_datetime");
				Timestamp itm_eff_end_datetime = rs1.getTimestamp("itm_eff_end_datetime");
				
				//Timestamp cur_time = cwSQL.getTime(con);
				res.setItm_content_eff_end_datetime(itm_content_eff_end_datetime);
				String content_status = getContentStatus(itm_type,cur_time,  blend_ind,run_ind,att_create_timestamp ,itm_content_duration, itm_content_eff_end_datetime, itm_eff_end_datetime);
				res.setContent_status(content_status);
				
				resultMap.put("resource", res);				
				// Course Structure
				cosStructXML = cwSQL.getClobValue(rs1, "cos_structure_xml");
				String cosStructJson=cwSQL.getClobValue(rs1, "cos_structure_json");
				if(cosStructJson!=null){
					JSONObject strucObj = JSONObject.fromObject(cosStructJson);
					resultMap.put("mod_structure", strucObj);
					if(strucObj!=null &&strucObj.getJSONArray("mod_relation")!=null){
						JSONArray rltObj= strucObj.getJSONArray("mod_relation");
						Object obj  =JSONSerializer.toJava(rltObj);
						List list =(List)obj;
						if(list!=null && !list.isEmpty()){
							Iterator iter = list.iterator();
							while(iter.hasNext()){
								JSONObject jobj =JSONObject.fromObject(iter.next());
								ModRelationBean modR=(ModRelationBean)JSONObject.toBean(jobj,ModRelationBean.class);
								folderHs.put(new Long (modR.getMod_id()), modR.getFolder());
							}
							
						}
					}
				}
			} else {
				stmt1.close();
				throw new qdbException("Failed to get resource header.");
			}
			stmt1.close();
			
			Vector modList = getCosContentList(con, prof, order, checkStatus, null, onLineStatusHs, folderHs, cur_time, itm_eff_start_datetime, att_create_timestamp, itm_create_run_ind);
			Vector modJsonList = new Vector();
			for(int i = 0; i < modList.size(); i++){
				ModBean mod = (ModBean)modList.elementAt(i);
				JsonPropertyFilter jf=(JsonPropertyFilter)defJsonConfig.getJsonPropertyFilter();
				jf.addFilters(mod.getFilterLst());
				JSONObject jobj = JSONObject.fromObject(mod,defJsonConfig);
				modJsonList.add(jobj);
				jf.removeAllFilters();
			}
			resultMap.put("mod_lst", modJsonList);
			
		} catch (SQLException e) {
			throw new qdbException("SQL Error: " + e.getMessage());
		}
	}
    
    public Vector getCosContentList(Connection con, loginProfile prof, String order,  boolean checkStatus, Vector modIdVec, Hashtable onLineStatusHs,
    		Hashtable folderHs, Timestamp cur_time, Timestamp itm_eff_start_datetime, Timestamp att_create_timestamp, boolean itm_create_run_ind) throws cwException, qdbException {
		try {

			String sql_mod_id_lst = null;
			String tableName = null;
			if (modIdVec != null) {
				if (modIdVec.size() == 0) {
					sql_mod_id_lst = "( 0 )";
				} else if (modIdVec.size() < 100) {
					sql_mod_id_lst = cwUtils.vector2list(modIdVec);
				} else {
					tableName = cwSQL.createSimpleTemptable(con, "tmp_res_id", cwSQL.COL_TYPE_LONG, 0);
					cwSQL.insertSimpleTempTable(con, tableName, modIdVec, cwSQL.COL_TYPE_LONG);
					sql_mod_id_lst = " ( select tmp_res_id from " + tableName + ") ";
				}
			}

			String SQL = getCosContentSQL() 
					+ " res.res_id = rcn_res_id_content " 
					+ " AND mod_res_id = rcn_res_id_content " 
					+ " AND rcn_res_id = " + res_id 
					+ " AND (pgr_complete_datetime is null or pgr_complete_datetime = ( select max(pgr_complete_datetime) FROM Progress p2 WHERE p2.pgr_res_id = p1.pgr_res_id and p2.pgr_tkh_id = ? and p2.pgr_usr_id = ?))";

			if (modIdVec != null) {
				SQL += " AND rcn_res_id_content IN " + sql_mod_id_lst;
			}
			// Dennis, check for a specific resource content sub type
			if (res_cnt_subtype != null && res_cnt_subtype.length() > 0)
				SQL += " AND res.res_subtype = '" + res_cnt_subtype + "' ";

			// filter out the offline modules for students
			if (checkStatus)
				SQL += " AND res.res_status <> '" + RES_STATUS_OFF + "' ";

			// Lun : only select the modules created by the instructor
			if (mod_usr_id_instructor != null && mod_usr_id_instructor.length() > 0)
				SQL += " And mod_usr_id_instructor = '" + mod_usr_id_instructor + "' ";

			SQL += "  ORDER BY mod_eff_start_datetime DESC ,";
			if (order != null && order.length() != 0 && !order.equalsIgnoreCase("res_title")) {
				if (order.equalsIgnoreCase("res_type"))
					SQL += "  RSUBTYPE ASC,  ";
				else
					SQL += order + ",";
			}

			SQL += "  RTITLE ASC ";
			if (cwSQL.DBVENDOR_DB2.equalsIgnoreCase(cwSQL.getDbType())) {
				SQL += " for read only";
			}
			PreparedStatement stmt = con.prepareStatement(SQL);
			int index = 1;
			stmt.setLong(index++, tkh_id);
			stmt.setLong(index++, tkh_id);
			stmt.setString(index++, prof.usr_id);
			stmt.setLong(index++, tkh_id);
			stmt.setString(index++, prof.label_lan);
			stmt.setLong(index++, tkh_id);
			stmt.setString(index++, prof.usr_id);
			Vector modList = new Vector();
			long RID, RSUBNBR, RMUL, MMUATTEMPT;
			int RORDER;
			String RLAN, RTYPE, RSUBTYPE, RTITLE, RPRIV, ROWNER, RSTATUS, RATTEMPTED;
			Timestamp RTIMESTAMP;
			int RDIFF;
			float RDUR;
			float MMAXSCORE;
			int MPASSSCORE;
			Timestamp PSTART, PCOMPLETE, PLASTACC;
			String MINSTRUCT;
			String RDESC;
			long PATTEMPTNBR;
			String RISTNAME, RISTORG, RTPLNAME;
			dbEvent dbevt;
			String evtBody;
			String RES_SRC_TYPE;
			String RES_SRC_LINK;
			// For AICC only
			String MOD_WEB_LAUNCH = "";
			String MOD_VENDOR = "";
			ResultSet rs = stmt.executeQuery();
			Hashtable dpo_hash = aeAction.diplayOption_hash;
			boolean mod_download_ind = false;
			long mod_required_time;
			while (rs.next()) {
				RID = rs.getLong("RID");
				RTYPE = rs.getString("RTYPE");
				RSUBTYPE = rs.getString("RSUBTYPE");
				RORDER = rs.getInt("RORDER");
				RLAN = rs.getString("RLAN");
				RTITLE = rs.getString("RTITLE");
				RSUBNBR = rs.getLong("RSUBNBR");
				RPRIV = rs.getString("RPRIV");
				ROWNER = rs.getString("ROWNER");
				RSTATUS = rs.getString("RSTATUS");
				RTIMESTAMP = rs.getTimestamp("RTIMESTAMP");
				RMUL = rs.getLong("RMUL");
				RDIFF = rs.getInt("RDIFF");
				RDUR = rs.getFloat("RDUR");
				RDESC = dbUtils.esc4XML(rs.getString("res_desc"));
				RISTNAME = dbUtils.esc4XML(rs.getString("RISTNAME"));
				RISTORG = dbUtils.esc4XML(rs.getString("RISTORG"));
				RTPLNAME = rs.getString("RTPLNAME");
				MMUATTEMPT = rs.getLong("MMUATTEMPT");
				// for AICC
				RES_SRC_TYPE = rs.getString("RES_SRC_TYPE");
				RES_SRC_LINK = rs.getString("RES_SRC_LINK");

				Timestamp eff_start_datetime = rs.getTimestamp("MESDATE");
				Timestamp eff_end_datetime = rs.getTimestamp("MEEDATE");
				MMAXSCORE = 0;
				MPASSSCORE = 0;
				MINSTRUCT = "";
				PSTART = null;
				PCOMPLETE = null;
				PLASTACC = null;
				PATTEMPTNBR = 0;
				// get from rs
				MMAXSCORE = rs.getFloat("MMAXSCORE");
				MPASSSCORE = rs.getInt("MPASSSCORE");
				MINSTRUCT = dbUtils.esc4XML(rs.getString("MINSTRUCT"));
				MOD_WEB_LAUNCH = rs.getString("MOD_WEB_LAUNCH");
				MOD_VENDOR = rs.getString("MOD_VENDOR");
				PATTEMPTNBR = rs.getInt("pgr_attempt_nbr");
				mod_download_ind = rs.getBoolean("mod_download_ind");
				mod_required_time = rs.getLong("mod_required_time");
				dbDisplayOption dpo = new dbDisplayOption();
				dpo = (dbDisplayOption) dpo_hash.get(RSUBTYPE);
				ModBean mod = new ModBean();
				mod.setId(RID);
				mod.setFilterLst(new ArrayList());
				if (!folderHs.containsKey(new Long(RID))) {
					mod.setMod_folder((String) folderHs.get(new Long(RID)));
				}

				if (onLineStatusHs.containsKey(new Long(RID))) {
					mod.setCmr_status((String) onLineStatusHs.get(new Long(RID)));
				}

				if (!dpo.dpo_title_ind) {
					mod.getFilterLst().add("title");
				}
				mod.setTitle(RTITLE);

				if (!dpo.dpo_desc_ind) {
					mod.getFilterLst().add("desc");
				}
				mod.setDesc(RDESC);
				if (!dpo.dpo_organization_ind) {
					mod.getFilterLst().add("organization");
				}
				mod.setOrganization(RISTORG);
				if (!dpo.dpo_instruct_ind) {
					mod.getFilterLst().add("instruct");
				}
				mod.setInstruct(MINSTRUCT);
				mod.setType(RTYPE);
				mod.setSubtype(RSUBTYPE);
				mod.setOrder(RORDER);
				mod.setSub_num(RSUBNBR);
				mod.setScore_multiplier(RMUL);
				mod.setPrivilege(RPRIV);

				/*
				 * if(!dpo.dpo_status_ind){//影响到aicc对象的输出
				 * mod.getFilterLst().add("status"); }
				 */
				mod.setStatus(RSTATUS);
				if (!dpo.dpo_lan_ind) {
					mod.getFilterLst().add("language");
				}
				mod.setLanguage(RLAN);
				if (!dpo.dpo_max_usr_attempt_ind) {
					mod.getFilterLst().add("max_user_attempt");
				}
				mod.setMax_user_attempt(MMUATTEMPT);
				if (!dpo.dpo_difficulty_ind) {
					mod.getFilterLst().add("difficulty");
				}
				mod.setDifficulty(RDIFF);
				if (!dpo.dpo_duration_ind) {
					mod.getFilterLst().add("duration");
				}
				mod.setDuration(RDUR);
				if (!dpo.dpo_time_limit_ind) {
					mod.getFilterLst().add("time_limit");
				}
				mod.setTime_limit(RDUR);
				if (!dpo.dpo_suggested_time_ind) {
					mod.getFilterLst().add("suggested_time");
				}
				mod.setSuggested_time(RDUR);
				if (RSUBTYPE != null && RSUBTYPE.equalsIgnoreCase("ASS")) {
					int ass_due_date_day = rs.getInt("ass_due_date_day");
					Timestamp ass_due_datetime = rs.getTimestamp("ass_due_datetime");
					String due_date = dbAssignment.getDueDate(ass_due_date_day, ass_due_datetime, itm_create_run_ind, itm_eff_start_datetime, att_create_timestamp);
					if (due_date != null) {
						mod.setDue_datetime(due_date);
					}
				}
				/*
				 * if(!dpo.dpo_eff_start_datetime_ind){
				 * mod.getFilterLst().add("eff_start_datetime"); }
				 */
				mod.setEff_start_datetime(eff_start_datetime);
				/*
				 * if(!dpo.dpo_eff_end_datetime_ind){
				 * mod.getFilterLst().add("eff_end_datetime"); }
				 */
				mod.setEff_end_datetime(eff_end_datetime);
				mod.setCur_time(cur_time);
				if (!dpo.dpo_max_score_ind) {
					mod.getFilterLst().add("max_score");
				}
				mod.setMax_score(MMAXSCORE);
				if (!dpo.dpo_pass_score_ind) {
					mod.getFilterLst().add("pass_score");
				}
				mod.setPass_score(MPASSSCORE);
				mod.setAttempt_nbr(PATTEMPTNBR);
				if (!dpo.dpo_pgr_start_datetime_ind) {
					mod.getFilterLst().add("pgr_start");
				}
				mod.setPgr_start(PSTART);
				if (!dpo.dpo_pgr_complete_datetime_ind) {
					mod.getFilterLst().add("pgr_complete");
				}
				mod.setPgr_complete(PCOMPLETE);
				if (!dpo.dpo_pgr_last_acc_datetime_ind) {
					mod.getFilterLst().add("pgr_last_acc");
				}
				mod.setPgr_last_acc(PLASTACC);
				// for AICC
				if (MOD_VENDOR != null) {
					mod.setMod_vendor(MOD_VENDOR);
				} else {
					mod.setMod_vendor("");
				}
				if (MOD_WEB_LAUNCH != null) {
					mod.setMod_web_launch(MOD_WEB_LAUNCH);
				} else {
					mod.setMod_web_launch("");
				}
				if (RES_SRC_TYPE != null) {
					mod.setRes_src_type(RES_SRC_TYPE);
				} else {
					mod.setRes_src_type("");
				}
				if (RES_SRC_LINK != null) {
					mod.setRes_src_link(RES_SRC_LINK);
				} else {
					mod.setRes_src_link("");
				}
				mod.setTimestamp(RTIMESTAMP);

				if (RTPLNAME != null && RTPLNAME.trim().length() > 0) {
					mod.setStylesheet(rs.getString("tpl_stylesheet"));
				}
				mod.setSco_version(rs.getString("sco_version"));
				mod.setMod_download_ind(mod_download_ind);
				mod.setMod_required_time(mod_required_time);
				ModulePrerequisiteManagement modulemrerequisitemanagement = new ModulePrerequisiteManagement();
				long rrq_req_res_id = rs.getLong("rrq_req_res_id");
				String pre_res_title = rs.getString("pre_res_title");
				String pre_status = rs.getString("rrq_status");
				String mov_status = rs.getString("pre_status");
				PreModuleBean preMod = new PreModuleBean();
				if (rrq_req_res_id > 0) {
					preMod.setId(rrq_req_res_id);
					preMod.setChecked_status(pre_status);
					boolean complete_pre = modulemrerequisitemanagement.hasCompletePreMod(rrq_req_res_id, pre_status, mov_status);
					preMod.setComplete_pre(complete_pre);
					preMod.setPre_res_title(pre_res_title);
					mod.setPre_mod_inf(preMod);
				}
				AiccDataBean aicc = new AiccDataBean();
				aicc.setTkh_id(tkh_id);
				aicc.setCourse_id(rs.getLong("mov_cos_id"));
				aicc.setStudent_id(rs.getLong("mov_ent_id"));
				aicc.setLesson_id(rs.getLong("mov_mod_id"));
				aicc.setLast_acc_datetime(rs.getTimestamp("mov_last_acc_datetime"));
				aicc.setLast_update_timestamp(rs.getTimestamp("mov_update_timestamp"));
				aicc.setUsed_time(dbAiccPath.getTime(rs.getFloat("mov_total_time")));
				aicc.setNumber(rs.getString("mov_total_attempt"));
				aicc.setStatus(rs.getString("mov_status"));
				aicc.setPgr_status(rs.getString("pgr_status"));
				aicc.setPgr_completion_status(rs.getString("pgr_completion_status"));
				String score_s = null;
				if (rs.getString("mov_score") != null) {
					score_s = dbModuleEvaluation.convertScore(rs.getFloat("mov_score"));
				} else {
					score_s = "";
				}
				aicc.setScore(score_s);
				aicc.setLocation(rs.getString("mov_ele_loc"));
				mod.setAicc_data(aicc);
				modList.add(mod);
			}
			stmt.close();
			return modList;
		} catch (SQLException e) {
			throw new qdbException("SQL Error: " + e.getMessage());
		}
	}

    public static String getResStatus(Connection con, long res_id)
      throws qdbException, cwSysMessage {
        String SQL;
        String res_status=null;

        try {
            SQL = "Select res_status from Resources where res_id = ?"; //changed, resources

            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1,res_id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()==true)
                res_status = rs.getString(1);
            else
                //throw new qdbException("Cannot find Resource: " + res_id );
                throw new cwSysMessage(dbUtils.MSG_REC_NOT_FOUND, "Resource ID = " + res_id);

            stmt.close();
            return res_status;
       }
       catch(SQLException e) {
            throw new qdbException(e.getMessage());
       }
    }

    public static String getResType(Connection con, long res_id)
      throws qdbException, cwSysMessage {
        String SQL;
        String res_type=null;

        try {
            SQL = "Select res_type from Resources where res_id = ?"; //changed, resources

            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1,res_id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()==true)
                res_type = rs.getString(1);
            else{
                //throw new qdbException("Cannot find Resource: " + res_id );
            	stmt.close();
                throw new cwSysMessage(dbUtils.MSG_REC_NOT_FOUND, "Resource ID = " + res_id);
            }

            stmt.close();
            return res_type;
       }
       catch(SQLException e) {
            throw new qdbException(e.getMessage());
       }
    }

    public static String getResPrivilege(Connection con, long res_id)
      throws qdbException ,cwSysMessage
    {
        String SQL;
        String res_privilege=null;

        try {
            SQL = "Select res_privilege from Resources where res_id = ?"; //changed, resources

            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1,res_id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()==true)
                res_privilege = rs.getString(1);
            else
                //throw new qdbException("Cannot find Resource: " + res_id );
                throw new cwSysMessage(dbUtils.MSG_REC_NOT_FOUND, "Resource ID = " + res_id);

            stmt.close();
            return res_privilege;
       }
       catch(SQLException e) {
            throw new qdbException(e.getMessage());
       }
    }

    public static String getResUsrIdOwner(Connection con, long res_id)
      throws qdbException ,cwSysMessage
    {
        String SQL;
        String res_usr_id_owner=null;

        try {
            SQL = "Select res_usr_id_owner from Resources where res_id = ?";

            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1,res_id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()==true)
                res_usr_id_owner = rs.getString(1);
            else
                //throw new qdbException("Cannot find Resource: " + res_id );
                throw new cwSysMessage(dbUtils.MSG_REC_NOT_FOUND, "Resource ID = " + res_id);

            stmt.close();
            return res_usr_id_owner;
       }
       catch(SQLException e) {
            throw new qdbException(e.getMessage());
       }
    }

    public static String getResSubType(Connection con, long res_id)
      throws qdbException ,cwSysMessage {
        String SQL;
        String res_subtype=null;

        try {
            SQL = "Select res_subtype from Resources where res_id = ?";  //changed, resources

            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1,res_id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()==true)
                res_subtype = rs.getString(1);
            else
                //throw new qdbException("Cannot find Resource: " + res_id );
                throw new cwSysMessage(dbUtils.MSG_REC_NOT_FOUND, "Resource ID = " + res_id);

            stmt.close();
            return res_subtype;
       }
       catch(SQLException e) {
            throw new qdbException(e.getMessage());
       }
    }


    public void updateStatus(Connection con)
        throws qdbException, qdbErrMessage
    {
        //checkTimeStamp(con);

        try {
            PreparedStatement stmt = con.prepareStatement(
                "UPDATE Resources set res_status = ?, res_upd_user = ? "  //changed, resources
              + " where res_id = ? "  );
            stmt.setString(1, res_status);
            stmt.setString(2, res_upd_user);
            stmt.setLong(3, res_id);

            if(stmt.executeUpdate()!=1)
            {
                con.rollback();
                throw new qdbException("Failed to update status.");
            }
            else
            {
                stmt.close();
                updateTimeStamp(con);
                return;
            }
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    public String getResListAsXML(Connection con, HttpSession sess, long objId, String[] types, 
                                  String[] subtypes, loginProfile prof, cwPagination cwPage,String cur_stylesheet, boolean share_mode)
        throws qdbException
    {
        StringBuffer xmlBuf = new StringBuffer();
        StringBuffer resultList = new StringBuffer();
        Vector resIdVec = null;

        //set up pagination variables
        if(cwPage == null) {
            cwPage = new cwPagination();
        }
        if(cwPage.sortCol == null || cwPage.sortCol.length() ==0) {
            cwPage.sortCol = "res_upd_date";
        }
        if(cwPage.sortOrder == null || cwPage.sortOrder.length() == 0) {
            cwPage.sortOrder = "asc";
        }
        if (cwPage.curPage == 0) {
            cwPage.curPage = 1;
        }
        if (cwPage.pageSize == 0) {
            //cwPage.pageSize = cwPagination.defaultPageSize;
        	//6.5新需求要求不显示分页信息故讲分页条数调大
            cwPage.pageSize = 100000;
        }
        
        Timestamp sess_timestamp = null;
        if(sess != null) {
            sess_timestamp = (Timestamp) sess.getAttribute(qdbAction.SESS_RES_LIST_TS);
            if(sess_timestamp != null && cwPage.ts != null && sess_timestamp.equals(cwPage.ts)) {
                resIdVec = (Vector) sess.getAttribute(qdbAction.SESS_RES_LIST_RES_ID);
            }
        }

       try {
        dbObjective dbobj = new dbObjective();
        dbobj.obj_id = objId;
        dbobj.get(con);
        dbSyllabus dbsyl = new dbSyllabus();
        dbsyl.syl_id = dbobj.obj_syl_id;
        dbsyl.get(con);
        
        if(resIdVec == null) {
            String aobjs = "(" + objId + ") ";
            resIdVec = getResIdByObj(con, aobjs);
            Timestamp curTime = cwSQL.getTime(con);
            cwPage.ts = curTime;
            sess.setAttribute(qdbAction.SESS_RES_LIST_TS, curTime);
            sess.setAttribute(qdbAction.SESS_RES_LIST_RES_ID, resIdVec);
            /*Vector rpmVec = dbResourcePermission.getResPermission(con, resIdVec, prof);*/
        }

        // << BEGIN for ORACLE migration
        String tableName = cwSQL.createSimpleTemptable(con, "tmp_res_id", cwSQL.COL_TYPE_LONG, 0);
        cwSQL.insertSimpleTempTable(con, tableName, resIdVec, cwSQL.COL_TYPE_LONG);
        // >> END

        String SQL =
                " SELECT res_id "
        + "       ,res_lan "
        + "       ,res_title "
        + "       ,res_status "
        + "       ,usr_ste_usr_id "
        + "       ,res_type "
        + "       ,res_subtype "
        + "  FROM Resources, RegUser "  //changed, resources
        + " WHERE res_res_id_root is null "
        + "   AND res_usr_id_owner = usr_id "
//        + "   AND res_id IN " + resIdLst;
        + "   AND res_id IN (SELECT tmp_res_id FROM " + tableName + ")";

        if(types!=null && types.length > 0 && types[0] !=null && types[0].length() > 0) {
            SQL += " AND ( " ;
            for (int i=0;i<types.length;i++) {
                if (i != 0)
                    SQL += " OR " ;

                SQL += " res_type = '" + types[i] + "' ";
            }
            SQL += " ) ";
        }
        if(subtypes!=null && subtypes.length > 0 && subtypes[0] !=null && subtypes[0].length() > 0) {
            SQL += " AND ( " ;
            for (int i=0;i<subtypes.length;i++) {
                if (i != 0)
                    SQL += " OR " ;

                SQL += " res_subtype = '" + subtypes[i] + "' ";
            }
            SQL += " ) ";
        }

        if(res_lan !=null && res_lan.length()>0)
            SQL += "   AND res_lan = '" + res_lan + "' ";

         /*
          * In the creator,(LearningResourcesMangement),if choose All then res_privilege is null )
          * otherwise when you choose Myself ,res_privilege is  Author,not null.
          * */
        if (res_privilege != null && res_privilege.length() > 0) {
				if (res_privilege.equals(RES_PRIV_AUTHOR)) {
					SQL += " AND res_usr_id_owner = '" + prof.usr_id + "'";
				}
		}
        if(res_status!=null && res_status.length()>0)
            SQL += "   AND res_status = '" + res_status + "' ";
        if(res_difficulty > 0)
            SQL += "   AND res_difficulty = " + res_difficulty ;
        if(res_duration > 0.0)
            SQL += "   AND res_duration = " + res_duration ;

        SQL += " ORDER BY " + cwPage.sortCol + " " + cwPage.sortOrder;

        PreparedStatement stmt2 = con.prepareStatement(SQL);
        ResultSet rs2 = stmt2.executeQuery();
        long rid = 0;
        String rlan;
        String title = "";
        String type = "";
        String subtype = "";
        String sta = "";
        String own = "";
        Vector queList = new Vector();
        int resCount = 0;
        int begin = (cwPage.curPage-1) * cwPage.pageSize + 1;
        int end = begin+cwPage.pageSize;
        while(rs2.next())
        {
            resCount++;
            if (resCount >= begin && resCount < end) {
                rid = rs2.getLong("res_id");
                rlan = rs2.getString("res_lan");
                title = dbUtils.esc4XML(rs2.getString("res_title"));
                sta = rs2.getString("res_status");
                own = rs2.getString("usr_ste_usr_id");
                subtype = rs2.getString("res_subtype");
                type = rs2.getString("res_type");

                // format result
                resultList.append("<item id=\"").append(rid)
                    .append("\" language=\"").append(rlan)
                    .append("\" type=\"").append(type)
                    .append("\" subtype=\"").append(subtype)
                    .append("\" status=\"").append(sta)
                    .append("\" owner=\"").append(own).append("\">")
                    .append(title)
                    .append("</item>").append(dbUtils.NEWL);
                queList.addElement(new Long(rid)) ;
            }
        }
        stmt2.close();

        //set up pagination variables
        cwPage.totalRec = resCount;
        cwPage.totalPage = cwPage.totalRec/cwPage.pageSize;
        if (cwPage.totalRec%cwPage.pageSize !=0) {
            cwPage.totalPage++;
        }

        // << BEGIN for ORACLE migration
        cwSQL.dropTempTable(con, tableName);
        // >> END

        // prepare header
        xmlBuf.append(dbUtils.xmlHeader);
        xmlBuf.append("<list>").append(dbUtils.NEWL);
        // add environment
        xmlBuf.append(prof.asXML());
        //pagination XML
        xmlBuf.append(cwPage.asXML());
        //object access and page variant
		AcObjective acObj = new AcObjective(con);
		AcPageVariant acPageVariant = new AcPageVariant(con); 
	    Hashtable xslQuestions=AcXslQuestion.getQuestions();
		String access = acObj.getObjectiveAccess(objId,prof.usr_ent_id);
		xmlBuf.append("<obj_access>").append(access).append("</obj_access>");
		xmlBuf.append("<share_mode>").append(share_mode).append("</share_mode>");
		acPageVariant.obj_id = objId;
		acPageVariant.ent_id = prof.usr_ent_id;
		acPageVariant.rol_ext_id= prof.current_role;
		//acPageVariant.admAccess = acObj.hasAdminPrivilege(prof.usr_ent_id,prof.current_role);
		String metaXML = acPageVariant.answerPageVariantAsXML((String[]) xslQuestions.get(cur_stylesheet));
		xmlBuf.append(metaXML);
        // format header
        xmlBuf.append("<header privilege=\"").append(res_privilege).append("\" sort_order=\"")
              .append(cwPage.sortCol).append("\" duration=\"").append(res_duration)
              .append("\" difficulty=\"").append(res_difficulty)
              .append("\" status=\"").append(res_status).append("\">").append(dbUtils.NEWL)
              .append("<resource total=\"").append(resCount).append("\" />").append(dbUtils.NEWL);

        xmlBuf.append("<objective id=\"").append(dbobj.obj_id).append("\" type=\"")
                .append(dbobj.obj_type).append("\">").append(dbUtils.NEWL);

        xmlBuf.append("<syllabus id=\"").append(dbsyl.syl_id)
                  .append("\" privilege=\"").append(dbsyl.syl_privilege)
                  .append("\" locale=\"").append(dbsyl.syl_locale)
                  .append("\" root_ent_id=\"").append(dbsyl.syl_ent_id_root)
                  .append("\">").append(dbUtils.NEWL);
        xmlBuf.append("<desc>").append(dbUtils.esc4XML(dbsyl.syl_desc)).append("</desc>").append(dbUtils.NEWL)
              .append("</syllabus>").append(dbUtils.NEWL);

        xmlBuf.append("<desc>").append(dbUtils.esc4XML(dbobj.obj_desc)).append("</desc>").append(dbUtils.NEWL);
        xmlBuf.append("</objective>").append(dbUtils.NEWL);
        long tcr_id = DbTrainingCenter.getObjTopTcrId(con, dbobj.obj_id);
        xmlBuf.append("<training_center id = \"").append(tcr_id).append("\"/>");
        xmlBuf.append("</header>").append(dbUtils.NEWL)
              .append("<body>").append(dbUtils.NEWL);

        xmlBuf.append(resultList);

        // access control list
        //xmlBuf.append(dbResourcePermission.aclAsXML(con, queList, prof));

        xmlBuf.append("</body>").append(dbUtils.NEWL);
        xmlBuf.append("</list>").append(dbUtils.NEWL);

        return xmlBuf.toString();

       } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
       }
    }

    public String getResListAsXML(Connection con, HttpSession sess, long objId, String[] types, 
            String[] subtypes, loginProfile prof, cwPagination cwPage,String cur_stylesheet, boolean share_mode, boolean flag) throws qdbException{
    	StringBuffer xmlBuf = new StringBuffer();
        StringBuffer resultList = new StringBuffer();
        Vector resIdVec = null;

        //set up pagination variables
        if(cwPage == null) {
            cwPage = new cwPagination();
        }
        if(cwPage.sortCol == null || cwPage.sortCol.length() ==0) {
            cwPage.sortCol = "res_upd_date";
        }
        if(cwPage.sortOrder == null || cwPage.sortOrder.length() == 0) {
            cwPage.sortOrder = "desc";
        }
        
        if (cwPage.curPage == 0) {
            cwPage.curPage = 1;
        }
        if (cwPage.pageSize == 0) {
            cwPage.pageSize = cwPagination.defaultPageSize;
        }
        
        Timestamp sess_timestamp = null;
        if(sess != null) {
            sess_timestamp = (Timestamp) sess.getAttribute(qdbAction.SESS_RES_LIST_TS);
            if(sess_timestamp != null && cwPage.ts != null && sess_timestamp.equals(cwPage.ts)) {
                resIdVec = (Vector) sess.getAttribute(qdbAction.SESS_RES_LIST_RES_ID);
            }
        }

       try {
        dbObjective dbobj = new dbObjective();
        dbobj.obj_id = objId;
        dbobj.get(con);
        dbSyllabus dbsyl = new dbSyllabus();
        dbsyl.syl_id = dbobj.obj_syl_id;
        dbsyl.get(con);
        
        if(resIdVec == null) {
            String aobjs = "(" + objId + ") ";
            resIdVec = getResIdByObj(con, aobjs);
            Timestamp curTime = cwSQL.getTime(con);
            cwPage.ts = curTime;
            sess.setAttribute(qdbAction.SESS_RES_LIST_TS, curTime);
            sess.setAttribute(qdbAction.SESS_RES_LIST_RES_ID, resIdVec);
            /*Vector rpmVec = dbResourcePermission.getResPermission(con, resIdVec, prof);*/
        }

        // << BEGIN for ORACLE migration
        String tableName = cwSQL.createSimpleTemptable(con, "tmp_res_id", cwSQL.COL_TYPE_LONG, 0);
        cwSQL.insertSimpleTempTable(con, tableName, resIdVec, cwSQL.COL_TYPE_LONG);
        // >> END

        String SQL =
                " SELECT res_id "
        + "       ,res_lan "
        + "       ,res_title "
        + "       ,res_status "
        + "       ,usr_ste_usr_id "
        + "       ,res_type "
        + "       ,res_subtype "
        + "       ,res_upd_date "
        + "  FROM Resources, RegUser "  //changed, resources
        + " WHERE res_res_id_root is null "
        + "   AND res_usr_id_owner = usr_id "
//        + "   AND res_id IN " + resIdLst;
        + "   AND res_id IN (SELECT tmp_res_id FROM " + tableName + ")";

        if(types!=null && types.length > 0 && types[0] !=null && types[0].length() > 0) {
            SQL += " AND ( " ;
            for (int i=0;i<types.length;i++) {
                if (i != 0)
                    SQL += " OR " ;

                SQL += " res_type = '" + types[i] + "' ";
            }
            SQL += " ) ";
        }
        if(subtypes!=null && subtypes.length > 0 && subtypes[0] !=null && subtypes[0].length() > 0) {
            SQL += " AND ( " ;
            for (int i=0;i<subtypes.length;i++) {
                if (i != 0)
                    SQL += " OR " ;

                SQL += " res_subtype = '" + subtypes[i] + "' ";
            }
            SQL += " ) ";
        }

        if(res_lan !=null && res_lan.length()>0)
            SQL += "   AND res_lan = '" + res_lan + "' ";

         /*
          * In the creator,(LearningResourcesMangement),if choose All then res_privilege is null )
          * otherwise when you choose Myself ,res_privilege is  Author,not null.
          * */
        if (res_privilege != null && res_privilege.length() > 0) {
				if (res_privilege.equals(RES_PRIV_AUTHOR)) {
					SQL += " AND res_usr_id_owner = '" + prof.usr_id + "'";
				}
		}
        if(res_status!=null && res_status.length()>0)
            SQL += "   AND res_status = '" + res_status + "' ";
        if(res_difficulty > 0)
            SQL += "   AND res_difficulty = " + res_difficulty ;
        if(res_duration > 0.0)
            SQL += "   AND res_duration = " + res_duration ;

        SQL += " ORDER BY " + cwPage.sortCol + " " + cwPage.sortOrder;

        PreparedStatement stmt2 = con.prepareStatement(SQL);
        ResultSet rs2 = stmt2.executeQuery();
        long rid = 0;
        String rlan;
        String title = "";
        String type = "";
        String subtype = "";
        String sta = "";
        String own = "";
        Vector queList = new Vector();
        int resCount = 0;
        int begin = (cwPage.curPage-1) * cwPage.pageSize + 1;
        int end = begin+cwPage.pageSize;
        while(rs2.next())
        {
            resCount++;
            if (resCount >= begin && resCount < end) {
                rid = rs2.getLong("res_id");
                rlan = rs2.getString("res_lan");
                title = dbUtils.esc4XML(rs2.getString("res_title"));
                sta = rs2.getString("res_status");
                own = rs2.getString("usr_ste_usr_id");
                subtype = rs2.getString("res_subtype");
                type = rs2.getString("res_type");          
                // format result
                resultList.append("<item id=\"").append(rid)
                    .append("\" language=\"").append(rlan)
                    .append("\" type=\"").append(type)
                    .append("\" subtype=\"").append(subtype)
                    .append("\" status=\"").append(sta)
                    .append("\" owner=\"").append(own)
                    .append("\" date=\"").append(StringUtils.isNotEmpty(rs2.getTimestamp("res_upd_date").toString()) ? rs2.getTimestamp("res_upd_date").toString().substring(0, 16) : "").append("\">")
                    .append(title)
                    .append("</item>").append(dbUtils.NEWL);
                queList.addElement(new Long(rid)) ;
            }
        }
        stmt2.close();

        //set up pagination variables
        cwPage.totalRec = resCount;
        cwPage.totalPage = cwPage.totalRec/cwPage.pageSize;
        if (cwPage.totalRec%cwPage.pageSize !=0) {
            cwPage.totalPage++;
        }

        // << BEGIN for ORACLE migration
        cwSQL.dropTempTable(con, tableName);
        // >> END
        if(flag){
        // prepare header
        	xmlBuf.append(dbUtils.xmlHeader);
        }
        xmlBuf.append("<list>").append(dbUtils.NEWL);
        // add environment
        xmlBuf.append(prof.asXML());
        //pagination XML
        xmlBuf.append(cwPage.asXML());
        //object access and page variant
		AcObjective acObj = new AcObjective(con);
		AcPageVariant acPageVariant = new AcPageVariant(con); 
	    Hashtable xslQuestions=AcXslQuestion.getQuestions();
		String access = acObj.getObjectiveAccess(objId,prof.usr_ent_id);
		xmlBuf.append("<obj_access>").append(access).append("</obj_access>");
		xmlBuf.append("<share_mode>").append(share_mode).append("</share_mode>");
		acPageVariant.obj_id = objId;
		acPageVariant.ent_id = prof.usr_ent_id;
		acPageVariant.rol_ext_id= prof.current_role;
		//acPageVariant.admAccess = acObj.hasAdminPrivilege(prof.usr_ent_id,prof.current_role);
		String metaXML = acPageVariant.answerPageVariantAsXML((String[]) xslQuestions.get(cur_stylesheet));
		xmlBuf.append(metaXML);
        // format header
        xmlBuf.append("<header privilege=\"").append(res_privilege).append("\" sort_order=\"")
              .append(cwPage.sortCol).append("\" duration=\"").append(res_duration)
              .append("\" difficulty=\"").append(res_difficulty)
              .append("\" status=\"").append(res_status).append("\">").append(dbUtils.NEWL)
              .append("<resource total=\"").append(resCount).append("\" />").append(dbUtils.NEWL);

        xmlBuf.append("<objective id=\"").append(dbobj.obj_id).append("\" type=\"")
                .append(dbobj.obj_type).append("\">").append(dbUtils.NEWL);

        xmlBuf.append("<syllabus id=\"").append(dbsyl.syl_id)
                  .append("\" privilege=\"").append(dbsyl.syl_privilege)
                  .append("\" locale=\"").append(dbsyl.syl_locale)
                  .append("\" root_ent_id=\"").append(dbsyl.syl_ent_id_root)
                  .append("\">").append(dbUtils.NEWL);
        xmlBuf.append("<desc>").append(dbUtils.esc4XML(dbsyl.syl_desc)).append("</desc>").append(dbUtils.NEWL)
              .append("</syllabus>").append(dbUtils.NEWL);

        xmlBuf.append("<desc>").append(dbUtils.esc4XML(dbobj.obj_desc)).append("</desc>").append(dbUtils.NEWL);
        xmlBuf.append("</objective>").append(dbUtils.NEWL);
        long tcr_id = DbTrainingCenter.getObjTopTcrId(con, dbobj.obj_id);
        xmlBuf.append("<training_center id = \"").append(tcr_id).append("\"/>");
        xmlBuf.append("</header>").append(dbUtils.NEWL)
              .append("<body>").append(dbUtils.NEWL);

        xmlBuf.append(resultList);

        // access control list
        //xmlBuf.append(dbResourcePermission.aclAsXML(con, queList, prof));

        xmlBuf.append("</body>").append(dbUtils.NEWL);
        xmlBuf.append("</list>").append(dbUtils.NEWL);

        return xmlBuf.toString();

       } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
       }
    }
    public String getSuqListAsXML(Connection con, long svyId, loginProfile prof, long begin_num, long end_num)
        throws qdbException
    {
        String result = "";
        String resultList = "";

        // prepare header
        result = "<?xml version=\"1.0\" encoding=\"" + dbUtils.ENC_UTF + "\" standalone=\"no\" ?>" + dbUtils.NEWL;

        result += "<list>" + dbUtils.NEWL;
        // add environment
        result += prof.asXML() + dbUtils.NEWL;

        // get from db
       try {

        String SQL =
                " SELECT res_id "
        + "       ,res_lan "
        + "       ,res_title "
        + "       ,res_status "
        + "       ,res_usr_id_owner "
        + "       ,res_type "
        + "       ,res_subtype "
        + "  FROM Resources, ResourceContent "  //changed, resources
        + " WHERE res_res_id_root is null "
        + " and rcn_res_id_content = res_id "
        + " and rcn_res_id = ? ";

        PreparedStatement stmt2 = con.prepareStatement(SQL);
        stmt2.setLong(1, svyId);

        ResultSet rs2 = stmt2.executeQuery();

        String rlan;
        String title = "";
        String type = "";
        String subtype = "";
        String sta = "";
        String own = "";
        Vector queList = new Vector();
        long i = 0;
        while(rs2.next())
        {
            i++;
            if ((i >= begin_num && i <= end_num) || (begin_num ==0 && end_num == 0)) {
                rlan = rs2.getString("res_lan");
                title = dbUtils.esc4XML(rs2.getString("res_title"));
                sta = rs2.getString("res_status");
                own = rs2.getString("res_usr_id_owner");
                subtype = rs2.getString("res_subtype");
                type = rs2.getString("res_type");

                // format result
                resultList += "<item id=\"" + rs2.getLong("res_id")
                    + "\" language=\"" + rlan
                    + "\" type=\"" + type
                    + "\" subtype=\"" + subtype
                    + "\" status=\"" + sta
                    + "\" owner=\"" + dbUtils.esc4XML(dbRegUser.usrId2SteUsrId(con,own))
                    + "\">";

                resultList += title + "</item>" + dbUtils.NEWL;
                queList.addElement(new Long(rs2.getLong("res_id"))) ;
            }
        }

        long resCount = i;

        // format header
        result += "<header privilege=\"" + res_privilege + "\" duration=\"" + res_duration + "\" difficulty=\"" + res_difficulty
               + "\" status=\"" + res_status + "\">" + dbUtils.NEWL;
        result += "<resource total=\"" + resCount + "\" begin=\"" + begin_num + "\" end=\"" + end_num + "\" />" + dbUtils.NEWL;
        result += "</header>" + dbUtils.NEWL;
        result += "<body>" + dbUtils.NEWL;

        // add body of list
        result += resultList;

        // access control list
//        result += dbResourcePermission.aclAsXML(con, queList, prof);
        // close
        result += "</body>" + dbUtils.NEWL;
        result += "</list>" + dbUtils.NEWL;

        stmt2.close();
        return result;

       } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
       }
    }

    static public Vector<Long> getResIdByObj(Connection con, String aobjs)
        throws qdbException
    {
       try{
            Vector<Long> resIdVec = new Vector<Long>();

            String SQL =
                " SELECT distinct res_id "
                + "  FROM Resources, ResourceObjective "  //changed, resources
                + " WHERE res_id = rob_res_id "
                // 27/10/2000 : Resource Manager
                //+ "   AND res_type = '" + RES_TYPE_QUE + "' "
                + "   AND rob_obj_id IN " + aobjs + " order by res_id desc ";

                PreparedStatement stmt = con.prepareStatement(SQL);

                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    Long resId = new Long(rs.getLong("res_id"));
                    resIdVec.addElement(resId);
                }
                stmt.close();
                return resIdVec;
       } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
       }
    }

    // Check wether a user has right to modify a resource
    // Case 1 : User has "write" permission in the resource permission table
    // Case 2 : Expert Admin, Admin, Expert Course Designers have permission on the modify all others public resources
    /*
    public void checkResPermission(Connection con, loginProfile prof)
        throws qdbException, qdbErrMessage ,cwSysMessage
    {
            try {


                boolean hasRight = false;

                // User has write permission on the resoure
                if (dbResourcePermission.hasPermission(con, res_id, prof,
                                                    dbResourcePermission.RIGHT_WRITE))
                {
                        hasRight = true;
                }else {

                    String resPriv = new String();
                    String resOwnerId = new String();

                    PreparedStatement stmt = con.prepareStatement(
                            " SELECT res_usr_id_owner, res_privilege FROM Resources "
                        +"   WHERE res_id = ? ");

                    stmt.setLong(1,res_id);
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        resPriv = rs.getString("res_privilege");
                        resOwnerId = rs.getString("res_usr_id_owner");
                    }else {
                        //throw new qdbException("Failed to get resource id = " + res_id);
                        throw new cwSysMessage(dbUtils.MSG_REC_NOT_FOUND, "Resource ID = " + res_id);
                    }
                    stmt.close();

                    if (resPriv.equalsIgnoreCase(RES_PRIV_CW)) {
                        long rootId = dbRegUser.getRootGpId(con, resOwnerId);
                        if (rootId == prof.root_ent_id) // Same Organization
                        {
                            AccessControlWZB acl = new AccessControlWZB();
                            if( acl.hasUserPrivilege(con, prof.usr_ent_id, FCN_RES_MGT_PUBLIC))
                                hasRight = true;
                        }
                    }
                }
                if (!hasRight){
                    throw new qdbErrMessage("RES002");
                }

            }catch (SQLException e) {
                throw new qdbException("SQL Error : " + e.getMessage());
            }
    }

    // Check wether a user has right to on public resource
    // Case 2 : Expert Admin, Admin, Expert Course Designers have permission on the public resource
    public void checkPublicResPermission(Connection con, loginProfile prof)
        throws qdbException, qdbErrMessage
    {
            try {
                AccessControlWZB acl = new AccessControlWZB();
                if(!acl.hasUserPrivilege(con, prof.usr_ent_id, FCN_RES_MGT_PUBLIC))
                       throw new qdbErrMessage("RES003");

            }catch (SQLException e) {
                throw new qdbException("SQL Error : " + e.getMessage());
            }
    }
    */
    public static Vector getNodeModuleIds(String cosStructXML, String id)
        throws qdbException
    {
        try {
            Vector modIdVec = new Vector();

            // For new course that don't have course structure xml
            if (cosStructXML == null || cosStructXML.length() ==0) {
                return modIdVec;
            }

            cosStructXML = dbUtils.xmlHeader + cosStructXML;

            InputSource in = new InputSource(new StringReader(cosStructXML));

            // Use a DOMParser from Xerces so we get a complete DOM from the document
            DOMParser parser = new DOMParser();
            parser.parse(in);

            // Get the documentElement from the parser, which is what the selectNodeList method expects
            Node root = parser.getDocument().getDocumentElement();

            if (id == null || id.length() ==0) {
                dbResource.getModIds(root, modIdVec);
            }else {
                Vector matchNode = new Vector();
                dbResource.getMatchNode(root, id, matchNode);
                if (matchNode.size() ==1) {
                    dbResource.getModIds((Node) matchNode.elementAt(0), modIdVec);
                }
            }
            return modIdVec;
        } catch (SAXException saxe) {
            throw new qdbException("SAXException :" + saxe.getMessage());
        } catch (IOException ioe) {
            throw new qdbException("IOException :" + ioe.getMessage());
        }
    }

    public static void getMatchNode(Node curNode, String id, Vector matchNode) throws qdbException
    {
        if (curNode == null) {
            return ;
        }

        NamedNodeMap kAttrs = curNode.getAttributes();
        Attr kidAttr = (Attr)kAttrs.getNamedItem("identifier");

        NodeList nList = null;
        try {
            nList = XPathAPI.selectNodeList(curNode, "item");
        } catch (TransformerException e) {
            throw new qdbException(e.getMessage());
        }
        for (int i=0;i<nList.getLength();i++) {
            Node pNode = nList.item(i);

            NamedNodeMap pAttrs = pNode.getAttributes();
            Attr idAttr = (Attr)pAttrs.getNamedItem("identifier");
            if( idAttr.getValue().equals(id)) {
                matchNode.addElement(pNode);
                i = nList.getLength();
            }else {
                NodeList nl = null;
                try {
                    nl = XPathAPI.selectNodeList(pNode, "itemtype");
                } catch (TransformerException e) {
                    throw new qdbException(e.getMessage());
                }
                String nodeType = nl.item(0).getFirstChild().getNodeValue();
                if (nodeType.equals("FDR")) {
                    getMatchNode(pNode, id, matchNode);
                }
            }
        }
    }

    public static void getModIds(Node curNode, Vector modIdVec) throws qdbException
    {
        if (curNode == null)
            return;
        NodeList nList = null;
        try {
            nList = XPathAPI.selectNodeList(curNode, "item");
        } catch (TransformerException e) {
            throw new qdbException(e.getMessage());
        }
        for (int i=0;i<nList.getLength();i++) {

            Node pNode = nList.item(i);
            NamedNodeMap pAttrs = pNode.getAttributes();

            NodeList nl = null;
            try {
                nl = XPathAPI.selectNodeList(pNode, "itemtype");
            } catch (TransformerException e) {
                throw new qdbException(e.getMessage());
            }
            String nodeType = nl.item(0).getFirstChild().getNodeValue();
            if (!nodeType.equals("FDR")) {
                Attr idAttr = (Attr)pAttrs.getNamedItem("identifierref");
                Long modId = new Long(idAttr.getValue());
                modIdVec.addElement(modId);
            }
        }
    }


    /**
    * Update Assessment releated field ( title and desc )
    * @param database connection
    * @param user profile
    */
    public void updAsmReleated(Connection con, loginProfile prof)
        throws SQLException, cwException {

            if(res_desc != null && res_desc.length() > RES_DESC_LENGTH) {
                res_desc = res_desc.substring(0,RES_DESC_LENGTH);
            }

            Timestamp curTime = cwSQL.getTime(con);
            String SQL = " UPDATE Resources "
                       + " SET res_title = ?, res_desc = ?, "
                       + " res_upd_user = ?, res_upd_date = ?, "
                       + " res_duration = ?, res_tpl_name = ? "
                       + " WHERE res_id = ? ";

            PreparedStatement stmt = con.prepareStatement(SQL);
            int index = 1;
            stmt.setString(index++, res_title);
            stmt.setString(index++, res_desc);
            stmt.setString(index++, prof.usr_id);
            stmt.setTimestamp(index++, curTime);
            stmt.setFloat(index++, res_duration);
            stmt.setString(index++, res_tpl_name);
            stmt.setLong(index++, res_id);
            if( stmt.executeUpdate() != 1 )
                throw new cwException("Failed to update resource, id = " + res_id);

            stmt.close();
            return;
        }

    public static Hashtable getResTypeHash(Connection con, String[] res_id_lst)
      throws qdbException {
        String SQL;
        String res_type=null;

        try {
            Hashtable resHash = new Hashtable();
            if (res_id_lst == null || res_id_lst.length ==0)
                return resHash;

            String idLst = dbUtils.array2list(res_id_lst);

            SQL = "Select res_id, res_type from Resources where res_id IN "  + idLst ;

            PreparedStatement stmt = con.prepareStatement(SQL);
            ResultSet rs = stmt.executeQuery();
            Long resID = null;
            String resType = null;
            while(rs.next())  {
                resID = new Long(rs.getLong("res_id"));
                resType = rs.getString("res_type");
                resHash.put(resID, resType);
            }
            stmt.close();

            return resHash;
       }
       catch(SQLException e) {
            throw new qdbException(e.getMessage());
       }
    }


    private static final String SQL_GET_TPL_XSL =
        " SELECT tpl_stylesheet FROM Resources, Template " +
        " WHERE res_tpl_name = tpl_name " +
        " AND res_id = ? and tpl_lan = res_lan ";

    /**
    Get this resource's tpl stylesheet as XML
    Pre-define variable:
        res_id
    */
    public String getTplXslAsXML(Connection con) throws SQLException {
        PreparedStatement stmt = null;
        StringBuffer xmlBuf = new StringBuffer(64);
        try {
            stmt = con.prepareStatement(SQL_GET_TPL_XSL);
            stmt.setLong(1, res_id);
            ResultSet rs = stmt.executeQuery();
            xmlBuf.append("<stylesheet>");
            if(rs.next()) {
                xmlBuf.append(dbUtils.esc4XML(rs.getString("tpl_stylesheet")));
            }
            xmlBuf.append("</stylesheet>").append(dbUtils.NEWL);
        } finally {
            if(stmt!=null) stmt.close();
        }
        return xmlBuf.toString();
    }

    public static void updResIdRoot(Connection con, long resId, long res_res_id_root)
        throws qdbException {
        try {

            PreparedStatement stmt =
                con.prepareStatement(
                    " UPDATE Resources " + "   SET res_res_id_root = ?  " + "  WHERE res_id  = ? ");

            stmt.setLong(1, res_res_id_root);
            stmt.setLong(2, resId);

            if (stmt.executeUpdate() != 1) {
                con.rollback();
                stmt.close();
                throw new qdbException("Failed to update resource.");
            }

            stmt.close();

        } catch (SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    // size of "res_id_lst" must be less than 1000 to avoid error in "IN" clause
        public static Hashtable getResSubTypeHash(Connection con, String[] res_id_lst)
            throws qdbException {
            String SQL;
            String res_type = null;

            try {
                Hashtable resHash = new Hashtable();
                if (res_id_lst == null || res_id_lst.length == 0)
                    return resHash;

                String idLst = dbUtils.array2list(res_id_lst);

                SQL = "Select res_id, res_subtype from Resources where res_id IN " + idLst;

                PreparedStatement stmt = con.prepareStatement(SQL);
                ResultSet rs = stmt.executeQuery();
                Long resID = null;
                String resType = null;
                while (rs.next()) {
                    resID = new Long(rs.getLong("res_id"));
                    resType = rs.getString("res_subtype");
                    resHash.put(resID, resType);
                }
                stmt.close();

                return resHash;
            } catch (SQLException e) {
                throw new qdbException(e.getMessage());
            }
        }

        // size queList must be less than 1000, to avoid error in "IN" clause 
        public static int countScenarioQueInList(Connection con, Vector queList)
            throws SQLException {

            if (queList.size() == 0) {
                return 0;
            } else {
                String SQL =
                    " Select Count(res_id) "
                        + "From Resources "
                        + "Where res_subtype In ( ?, ? ) "
                        + "And res_id In "
                        + cwUtils.vector2list(queList);
                PreparedStatement stmt = con.prepareStatement(SQL);
                stmt.setString(1, dbResource.RES_SUBTYPE_DSC);
                stmt.setString(2, dbResource.RES_SUBTYPE_FSC);
                ResultSet rs = stmt.executeQuery();
                int count = 0;
                if (rs.next()) {
                    count = rs.getInt(1);
                }
                stmt.close();
                return count;
            }
        }

        // size of res_id_lst must be less than 1000, to avoid error in "IN" cluase
        public static Hashtable getResourceTitle(Connection con, String res_id_lst)
            throws SQLException {

            String SQL =
                "Select res_id, res_title From Resources Where res_id In "
                    + res_id_lst
                    + " Order By res_title Asc ";
            PreparedStatement stmt = con.prepareStatement(SQL);
            ResultSet rs = stmt.executeQuery();
            Hashtable h_res_title = new Hashtable();
            while (rs.next()) {
                h_res_title.put(new Long(rs.getLong("res_id")), rs.getString("res_title"));
            }
            stmt.close();
            return h_res_title;
        }

        public StringBuffer getResShortXML(Connection con)
            throws qdbException, SQLException, cwSysMessage {
            StringBuffer result = new StringBuffer();

            result
                .append("<resource id=\"")
                .append(res_id)
                .append("\" language=\"")
                .append(res_lan)
                .append("\" last_modified=\"")
                .append(res_upd_user)
                .append("\" timestamp=\"")
                .append(res_upd_date)
                .append("\" owner=\"")
                .append(dbUtils.esc4XML(dbRegUser.usrId2SteUsrId(con, res_usr_id_owner)))
                .append("\">")
                .append(dbUtils.NEWL);
            result
                .append("<header type=\"")
                .append(res_type)
                .append("\" subtype=\"")
                .append(res_subtype)
                .append("\" difficulty=\"")
                .append(res_difficulty)
                .append("\" duration=\"")
                .append(res_duration)
                .append("\" privilege=\"")
                .append(res_privilege)
                .append("\" status=\"")
                .append(res_status)
                .append("\">")
                .append(dbUtils.NEWL);
            result.append(objAsXML(con));
            result.append("</header>").append(dbUtils.NEWL).append(dbUtils.NEWL);
            result
                .append("<body>")
                .append("<title>")
                .append(dbUtils.esc4XML(res_title))
                .append("</title>")
                .append("<desc>")
                .append(dbUtils.esc4XML(res_desc))
                .append("</desc>")
                .append("<annotation>")
                .append(res_annotation)
                .append("</annotation>")
                .append("<source type=\"")
                .append(res_src_type)
                .append("\">")
                .append(dbUtils.esc4XML(res_src_link))
                .append("</source>")
                .append("<instructor>")
                .append(dbUtils.esc4XML(res_instructor_name))
                .append("</instructor>")
                .append("<organization>")
                .append(dbUtils.esc4XML(res_instructor_organization))
                .append("</organization>")
                .append("<instructor>")
                .append(dbUtils.esc4XML(res_instructor_name))
                .append("</instructor>")
                .append("<moderator>")
                .append(dbUtils.esc4XML(getOwnerName(con)))
                .append("</moderator>")
                .append("<organization>")
                .append(dbUtils.esc4XML(res_instructor_organization))
                .append("</organization>")
                .append("</body>")
                .append("</resource>")
                .append(dbUtils.NEWL);
            return result;
        }
        
        public static long getQueOrder(Connection con, long container_id, long old_que_id) throws SQLException {
            String sql = "select rcn_order from resourceContent where rcn_res_id = ? and rcn_res_id_content = ? ";
            long order = 0;
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setLong(1, container_id);
            stmt.setLong(2, old_que_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                order = rs.getLong("rcn_order");
            }
            stmt.close();
            return order;
        }

    String SQL_getIsEnrollmentRelatedAsXML;
    public StringBuffer getIsEnrollmentRelatedAsXML(Connection con) throws SQLException {
        String result = null;
        if (!getIsEnrollmentRelated(con).equals(ENROLLMENT_RELATED_ALL)) {
            result = getIsEnrollmentRelated(con);
        }
        StringBuffer output = new StringBuffer();
        if (result != null) {
            output.append("<enrollment_related>").append(result).append("</enrollment_related>");
        }
        return output;
    }
    
    public String getIsEnrollmentRelated(Connection con) throws SQLException {
        String result = ENROLLMENT_RELATED_ALL;
        PreparedStatement stmt = null;
        try {
            int idx = 1;
            stmt = con.prepareStatement(SQL_getIsEnrollmentRelatedAsXML);
            stmt.setLong(idx++, this.res_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                if (rs.getBoolean(1)) {
                    result = ENROLLMENT_RELATED_TRUE;
                } else {
                    result = ENROLLMENT_RELATED_FALSE;
                }
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        return result;
    }
    
    public static boolean mod_is_visited(Connection con, long que_id) throws SQLException {
        String sql = "Select res_mod_res_id_test from Resources where res_id = ? ";
        long mod_res_id = 0;
        boolean isVisited = false;
        PreparedStatement stmt = null;
        try {
            int idx = 1;
            stmt = con.prepareStatement(sql);
            stmt.setLong(idx++, que_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                mod_res_id = rs.getLong("res_mod_res_id_test");
            }

            if (mod_res_id != 0) {
                if (dbModuleEvaluation.getModuleAttemptCount(con, mod_res_id) > 0) {
                    isVisited = true;
                }
            }
        }
        finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        return isVisited;
    }

    public String scQueObjAsXML(Connection con) throws qdbException, cwSysMessage {

        String result = "";
        Vector objIdVec = new Vector();
        objIdVec = dbResourceObjective.getObjId(con,res_id);

        try {
            // Get the root res id if the question is duplicated
            if (objIdVec.size() == 0 ) {
                PreparedStatement stmt1 = con.prepareStatement(
                " SELECT rcn_res_id "
                +   " from Resourcecontent "  //changed, resource
                +   " where rcn_res_id_content =? " );

                // set the values for prepared statements
                stmt1.setLong(1, res_id);

                ResultSet rs1 = stmt1.executeQuery();
                if(rs1.next()) {
                     long res_id = rs1.getLong("rcn_res_id");
                     objIdVec =  dbResourceObjective.getObjId(con, res_id);
                }else {
                     //throw new qdbException("Failed to get resource objective. ");
                     throw new cwSysMessage(dbUtils.MSG_REC_NOT_FOUND, "Resource ID = " + res_id);
                }

                stmt1.close();
            }

        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }

        dbResourceObjective rob = new dbResourceObjective();
        dbObjective obj = new dbObjective();

        int i;
        for (i=0;i<objIdVec.size();i++)
        {
            rob = (dbResourceObjective) objIdVec.elementAt(i);
            obj.obj_id = rob.rob_obj_id;
            obj.get(con);

            result += "<objective id=\"" + obj.obj_id + "\" type=\"" + obj.obj_type + "\" status=\""+obj.obj_status ;
            result += "\">" + dbUtils.esc4XML(obj.obj_desc) + "</objective>" + dbUtils.NEWL;
        }

        return result;
    }
    
    public String getResType(Connection con) throws SQLException{
	  String SQL;
	  String res_type = null;
	  PreparedStatement stmt = null;
	  try {
	      SQL = "Select res_type from Resources where res_id = ?"; 
          stmt = con.prepareStatement(SQL);
          stmt.setLong(1,this.res_id);
          ResultSet rs = stmt.executeQuery();
          if(rs.next()) {
              res_type = rs.getString(1);
          }
	     } finally {
	    	 stmt.close();
	     }
	     return res_type;
	}
    
    public long getResModResId(Connection con) throws SQLException{
        String SQL;
        long res_mod_res_id_test = 0;
        PreparedStatement stmt = null;
        try {
            SQL = "Select res_mod_res_id_test from Resources where res_id = ?"; 
            stmt = con.prepareStatement(SQL);
            stmt.setLong(1,this.res_id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
            	res_mod_res_id_test = rs.getLong(1);
            }
	    } finally {
		    stmt.close();
		}
	       return res_mod_res_id_test;
	}  
    
    public static long getResDuration (Connection con , long res_id) throws SQLException {
    	long duration = 0;
    	String sql = "select res_duration from Resources where res_id = ?";
    	PreparedStatement stmt = null;
    	try {
    		stmt = con.prepareStatement(sql);
    		stmt.setLong(1, res_id);
    		ResultSet rs = stmt.executeQuery();
    		if (rs.next()) {
    			duration = rs.getLong("res_duration");
    		}
    	} finally {
    		stmt.close();
    	}
    	return duration;
    }
    /**
     * 判断文件夹是否存在
     * @param con
     * @param obj_id
     * @return
     * @throws SQLException
     */
    public static boolean isExistObective(Connection con, long obj_id) throws SQLException {
        boolean result = false;
        String sql = "select count(*) as cnt From OBjective where obj_id = ? ";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setLong(1, obj_id);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()) {
            if(rs.getLong("cnt") > 0) {
                result = true;
            }
        }
        rs.close();
        stmt.close();
        return result;
    }
    /**
     * Insert a scorm courseware as a resource
     * @param con
     * @param prof
     * @param cos_id
     * @param imsmanifestPath
     * @param cosUrlPrefix
     * @throws SQLException
     * @throws IOException
     * @throws qdbException
     * @throws qdbErrMessage
     * @throws cwSysMessage
     * @throws cwException 
     */
    public void ins_res_scorm(Connection con, loginProfile prof, long res_id , String imsmanifestPath, String cosUrlPrefix, boolean fromzip, WizbiniLoader wizbini) throws SQLException, IOException, qdbException, qdbErrMessage, cwSysMessage, cwException {
        ImportCos myImportCos = new ImportCos();
        
        dbCourse myDbCourse = new dbCourse();
        myDbCourse.cos_res_id = res_id;
        myDbCourse.res_id = res_id;
       
         myDbCourse.res_title = "SCORM";
        
            
        if (ScormContentParser.SCORM_VERSION_2004.equals(res_sco_version)) {
        	myImportCos.importScorm2004(con, prof, (int) res_id, imsmanifestPath, cosUrlPrefix, true, false, fromzip, wizbini,myDbCourse);
        } else {
        	myImportCos.importScorm1_2(con, prof, (int) res_id, imsmanifestPath, cosUrlPrefix, true, false, fromzip,myDbCourse);
        }
    }
    
    /**
     * Insert a scorm courseware as a resource
     * @param con
     * @param prof
     * @param cos_id
     * @param imsmanifestPath
     * @param cosUrlPrefix
     * @throws SQLException
     * @throws IOException
     * @throws qdbException
     * @throws qdbErrMessage
     * @throws cwSysMessage
     * @throws cwException 
     */
    public void upd_res_scorm(Connection con, loginProfile prof, long res_id , String imsmanifestPath, String cosUrlPrefix, boolean fromzip, WizbiniLoader wizbini) throws SQLException, IOException, qdbException, qdbErrMessage, cwSysMessage, cwException {
        ImportCos myImportCos = new ImportCos();
        dbScormResource srs = new dbScormResource();
    	srs.srs_res_id = res_id;
    	srs.del(con);
    	dbResourceContent rcn = new dbResourceContent();
    	rcn.rcn_res_id = res_id;
    	Vector child_res = dbResourceContent.getChildResources(con, res_id);
    	for (int i = 0; i < child_res.size(); i++) {
    		dbResource tmp_res = (dbResource) child_res.get(i);
    		dbModule mod = new dbModule();
    		mod.res_id = tmp_res.res_id;
    		mod.mod_res_id = tmp_res.res_id;
    		mod.res_upd_date = tmp_res.res_upd_date;
    		mod.del(con);
    	}
        dbCourse myDbCourse = new dbCourse();
        myDbCourse.cos_res_id = res_id;
        myDbCourse.res_id = res_id;
        myDbCourse.res_title = "SCORM";
        this.get(con);

        
    	if (ScormContentParser.SCORM_VERSION_2004.equals(res_sco_version)) {
    		myImportCos.importScorm2004(con, prof, (int) res_id, imsmanifestPath, cosUrlPrefix, true, true, fromzip,   wizbini,myDbCourse);
    	} else {
    		myImportCos.importScorm1_2(con, prof, (int) res_id, imsmanifestPath, cosUrlPrefix, true, true, fromzip, myDbCourse);    		
    	}

    }
    public void updSrcLing(Connection con, long res_id, String src_link,long first_mod_id, String identifier) throws SQLException {
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement("update resources set res_src_link = ?,res_first_res_id=?, res_scor_identifier=? where res_id = ?");
            stmt.setString(1, src_link);
            stmt.setLong(2, first_mod_id);
            stmt.setString(3, identifier);
            stmt.setLong(4, res_id);
            stmt.executeUpdate();
        } finally {
            if (stmt != null) stmt.close();
        }
    }
    
    public String getContentStatus(String itm_type, Timestamp cur_time, boolean blend_ind, boolean run_ind,Timestamp att_create_timestamp, int itm_content_duration, Timestamp itm_content_eff_end_datetime, Timestamp itm_eff_end_datetime){
    	String status=null;
    	if(blend_ind || "SELFSTUDY".equalsIgnoreCase(itm_type) || "VIDEO".equalsIgnoreCase(itm_type)){
    		if(blend_ind){
    			if(cur_time.after(itm_content_eff_end_datetime)){
    				status ="end";
    			}	    			
    		}
    		if("SELFSTUDY".equalsIgnoreCase(itm_type) || "VIDEO".equalsIgnoreCase(itm_type)){
    			Timestamp ent_time=null;
    			if(itm_content_duration !=0){
    				Calendar c = Calendar.getInstance();   
    				c.setTimeInMillis(att_create_timestamp.getTime());   
    				c.add(Calendar.DAY_OF_MONTH, itm_content_duration);
    				ent_time = new Timestamp(c.getTimeInMillis()); 
    			}else{
    				ent_time= itm_content_eff_end_datetime;
    			}
    			if(ent_time !=null){
    				if(cur_time.after(ent_time)){
    					status ="end";
    				}    				
    			}
    		}
    	}else{
    		if (itm_eff_end_datetime != null) {
	    		if(cur_time.after(itm_eff_end_datetime)){
	    			status ="end";
	    		}
    		}
    	}
    	return status;
    }
    
    public static List getResIdByItmIds(Connection con, List itmIds, String[] res_type) throws SQLException {
    	List modLst = new ArrayList();
    	if (itmIds == null || itmIds.size() == 0) {
    		return modLst;
    	}
    	PreparedStatement stmt = null;
		ResultSet rs = null;
    	String itmTableName = null;
    	String itmIdColName = "itm_ids";
    	
    	try {
	    	itmTableName = cwSQL.createSimpleTemptable(con, itmIdColName, cwSQL.COL_TYPE_LONG, 0);
			if(itmTableName != null) {
				cwSQL.insertSimpleTempTable(con, itmTableName, itmIds, cwSQL.COL_TYPE_LONG);
			}
	    	
	    	String sql = " SELECT mod_res_id FROM Course " +
					     " INNER JOIN resourcecontent ON (rcn_res_id = cos_res_id) " +
				         " INNER JOIN module ON (mod_res_id = rcn_res_id_content ";
	    	if (res_type != null && res_type.length > 0) {
	    		sql += " AND (";
	    		for (int i = 0; i < res_type.length; i++) {
	    			if (i > 0) {
	    				sql += " OR ";
	    			}
	    			sql += " mod_type = '" + res_type[i] + "'";
	    		}
	    		sql += " ) ";	
	    	}
	    	sql += ") WHERE EXISTS (SELECT " + itmIdColName + " FROM " + itmTableName + " WHERE " + itmIdColName + " = cos_itm_id)";
		
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				modLst.add(rs.getString("mod_res_id"));
			}
		} finally {
			if (itmTableName != null) {
				cwSQL.dropTempTable(con, itmTableName);
			}
			cwSQL.cleanUp(rs, stmt);
		}
    	return modLst;
    }
    
    public List<Question> getQuesByParentResId(Connection con, long res_id) throws SQLException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Question> db_lst = new ArrayList<Question>();
		try {
			String sql = "select int_res_id, int_xml_outcome, que_score from Interaction"
					+ " inner join Resources on int_res_id = res_id"
					+ " inner join Question on que_res_id = int_res_id"
					+ " where res_mod_res_id_test = ? order by int_res_id asc";
			stmt = con.prepareStatement(sql);
			int index = 1;
			stmt.setLong(index++, res_id);
			rs = stmt.executeQuery();
			while(rs.next()) {
				Question question = new Question();
				question.setQue_res_id(rs.getLong("int_res_id"));
				question.setQue_xml(rs.getString("int_xml_outcome"));
				question.setQue_score(rs.getLong("que_score"));
				db_lst.add(question);
			}
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}		
		return db_lst;
	}
    
    public static String getResTitle(Connection con, String[] resId) throws SQLException {
    	PreparedStatement stmt = null;
    	ResultSet rs = null;
    	StringBuffer result = new StringBuffer();
    	if (resId == null || resId.length == 0) {
    		return result.toString();
    	}
    	
    	List modIds = new ArrayList();
        String tableName = null;
        String colName = null;
        for(int i = 0;i < resId.length; i++) {
        	modIds.add(new Long(resId[i]));
        }
        colName = "mod_ids";
        tableName = cwSQL.createSimpleTemptable(con, colName, cwSQL.COL_TYPE_LONG, 0);
        cwSQL.insertSimpleTempTable(con, tableName, modIds, cwSQL.COL_TYPE_LONG);
        
    	String sql = " SELECT res_title FROM Resources "
            	   + " WHERE EXISTS (SELECT " + colName + " FROM " + tableName + " WHERE " + colName + " = res_id)";
	    try {
	        stmt = con.prepareStatement(sql);
	        rs = stmt.executeQuery();
	        boolean isFirst = true;
	        while (rs.next()) {
	        	if (!isFirst) {
	        		result.append(", ");
	        	}
	        	result.append(rs.getString("res_title"));
	        	isFirst = false;
	        }
	    } finally {
	    	if (tableName != null) {
	    		cwSQL.dropTempTable(con, tableName);
	    	}
	    	cwSQL.cleanUp(rs, stmt);
	    }
	    return result.toString();
	}
    
    private static final String sql_upd_res_src_link = "update resources set res_src_link=? where res_id = ?";
    public void updResSrcLink(Connection con, long res_id, String link) throws SQLException {
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(sql_upd_res_src_link);
            stmt.setString(1, link);
            stmt.setLong(2, res_id);
            stmt.executeUpdate();
        } finally {
            if (stmt != null) stmt.close();
        }
    }
    
    private static final String sql_check_res_obj_shared = "select obj_share_ind from resourceObjective,Objective where rob_obj_id = obj_id and rob_res_id = ? ";
    public static boolean checkResObjShared(Connection con, long res_id) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean result = false;
        try {
            stmt = con.prepareStatement(sql_check_res_obj_shared);
            stmt.setLong(1, res_id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                result = rs.getBoolean(1);
            }
            return result;
        } finally {
            if (stmt != null) stmt.close();
        }

    }
    	
    public static void updResUpdDate(Connection con, long resId, Timestamp curTime, String usr_id) throws qdbException {
        try {

            PreparedStatement stmt = con.prepareStatement(" UPDATE Resources "
                    + "   SET res_upd_date = ?, res_upd_user = ?  "
                    + "  WHERE res_id  = ? ");

            stmt.setTimestamp(1, curTime);
            stmt.setString(2, usr_id);
            stmt.setLong(3, resId);

            if (stmt.executeUpdate() != 1) {
                con.rollback();
                stmt.close();
                throw new qdbException("Failed to update resource.");
            }

            stmt.close();

        } catch (SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }

    }

    /**
     * modify "file_name" fileld in aicc au file.
     * @param f au file
     * @param new_src_url
     * @throws qdbException
     * @throws IOException
     */
    public static void modifyAiccAuFile(File f, String new_src_url) throws qdbException, IOException {
        Vector vec = dbCourse.buildAuRecord(f.getAbsolutePath());
        Hashtable col = (Hashtable) vec.get(0);
        Hashtable val = (Hashtable) vec.get(1);
        Set set = col.keySet();
//        dbUtils.copyFile(f.getAbsolutePath(), f.getAbsolutePath() + ".bak");
        PrintWriter ow = new PrintWriter(new OutputStreamWriter(new FileOutputStream(f.getAbsolutePath(), false),cwUtils.ENC_UTF));
        Hashtable keys = new Hashtable();
        int count = 1;
        for(Iterator itr = set.iterator(); itr.hasNext();) {
            String key = (String)itr.next();
            Integer idx = (Integer) col.get(key);
            if (key.equalsIgnoreCase("file_name")) {
                val.put(idx, new_src_url);
            }
            keys.put(idx, key);
            count ++;
        }
        for (int i = 1; i < count; i++) {
            ow.write("\"" + keys.get(new Integer(i)) + "\"" + (i == count -1 ?  "":","));
        }
        ow.write("\n\r");
        for (int i = 1; i < count; i++) {
            ow.write("\"" + val.get(new Integer(i)) + "\"" + (i == count -1 ?  "":","));
        }
        ow.flush();
        ow.close();       
    }
    
    public String getVodResMainInfo(Connection con){
		StringBuffer xml = new StringBuffer();
//		xml.append(dbUtils.xmlHeader);
        xml.append("<res>").append(dbUtils.NEWL);
          if(res_vod_main != null && res_vod_main.length() > 0){
        	  xml.append("<res_vod_main>").append(dbUtils.NEWL);
        	  xml.append(dbUtils.esc4XML(res_vod_main));
        	  xml.append(dbUtils.NEWL).append("</res_vod_main>");
          }
          xml.append(dbUtils.NEWL).append("</res>");
        
        return xml.toString();
		
	} 

class ResBean {
	public String MOD_VENDOR;
	public Timestamp PLASTACC = null;
	public Timestamp PCOMPLETE = null;
	public Timestamp PSTART = null;
	public long RID;
	public String RTYPE;
	public String RSUBTYPE;
	public int RORDER;
	public String RLAN;
	public String RTITLE;
	public long RSUBNBR;
	public String RPRIV;
	public String ROWNER;
	public String RSTATUS;
	public Timestamp RTIMESTAMP;
	public long RMUL;
	public int RDIFF;
	public float RDUR;
	public String RDESC;
	public String RISTNAME;
	public String RISTORG;
	public String RTPLNAME;
	public long MMUATTEMPT;
	public String RES_SRC_TYPE;
	public String RES_SRC_LINK;
	public String RES_SRC_ONLINE_LINK;
	public Timestamp eff_start_datetime;
	public Timestamp eff_end_datetime;
	public String tmp_end_datetime;
	public float MMAXSCORE;
	public int MPASSSCORE;
	public String MINSTRUCT;
	public String MOD_WEB_LAUNCH;
	public int PATTEMPTNBR;
	public int ass_due_date_day;
	public Timestamp ass_due_datetime;
	public long rrq_req_res_id;
	public String pre_status;
	public String mov_status;
	public String tpl_stylesheet;
	public String aicc_data_xml;
}

}
