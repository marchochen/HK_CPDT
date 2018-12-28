package com.cw.wizbank.db.view;

import java.sql.*;
import java.util.*;

import com.cw.wizbank.util.*;
import com.cw.wizbank.db.DbKmObject;
import com.cw.wizbank.db.DbKmFolder;
import com.cw.wizbank.db.DbKmBaseObject;
import com.cw.wizbank.db.DbKmLink;
import com.cw.wizbank.db.DbKmNode;
import com.cw.wizbank.db.sql.OuterJoinSqlStatements;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.dbAttachment;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.ae.aeTemplate;
import com.cwn.wizbank.utils.CommonLog;


/**
A database class to represent table kmObject and kmObjectHistory
*/
public class ViewKmObject {

    public DbKmObject dbObject;
    public dbRegUser updateUser;
    public Vector vTemplate;
    public Vector vAttachment;

    // For search result
    public float hits_score;
    
    private Boolean isVersionActive;
    private long my_obj_bob_nod_id;
    private String my_obj_version;
    
    public static final String SQL_UPDATE_OBJ_STATUS_WITH_CONTROL = 
        " UPDATE kmObject SET " + 
        " obj_status = ? " +
        " ,obj_update_usr_id = ? " + 
        " ,obj_update_timestamp = ? " +
        " WHERE obj_bob_nod_id = ? " +
        " AND obj_version = ? " +
        " AND obj_update_timestamp = ? ";

    public static final String SQL_GET_LATEST_VERSION = 
        " SELECT obj_version FROM kmObject " +
        " WHERE obj_bob_nod_id = ? " + 
        " AND obj_latest_ind = ? ";

    public static final String SQL_GET_PUBLISHED_VERSION = 
        " SELECT obj_version FROM kmObject " +
        " WHERE obj_bob_nod_id = ? " + 
        " AND obj_publish_ind = ? ";

    public static final String SQL_GET_FROM_OBJ = 
        " SELECT obj_bob_nod_id FROM kmObject " + 
        " WHERE obj_bob_nod_id = ? " +
        " AND obj_version = ? ";

    public static final String SQL_GET_FROM_OJH = 
        " SELECT ojh_obj_bob_nod_id FROM kmObjectHistory " + 
        " WHERE ojh_obj_bob_nod_id = ? " +
        " AND ojh_version = ? ";

    private static final String SQL_FLD_COL = 
        " fld_nod_id, fld_type, fld_title, fld_desc, " +
        " fld_obj_cnt, fld_update_usr_id, fld_update_timestamp ";

    private static final String SQL_NOD_COL = 
        " nod_id, nod_type, nod_order, nod_parent_nod_id, " +
        " nod_ancestor, nod_create_timestamp, nod_create_usr_id, " +
        " nod_owner_ent_id, nod_acl_inherit_ind ";

    private static final String SQL_USR_COL = 
            " usr_id "
            + " ,usr_ste_usr_id "
            + " ,usr_ent_id "
            + " ,usr_ste_ent_id "
            + " ,usr_pwd "
            + " ,usr_email "
            + " ,usr_email_2 "
            + " ,usr_full_name_bil "
            + " ,usr_initial_name_bil "
            + " ,usr_last_name_bil "
            + " ,usr_first_name_bil "
            + " ,usr_display_bil "
            + " ,usr_gender "
            + " ,usr_bday "
//            + " ,usr_bplace_bil "
            + " ,usr_hkid "
            + " ,usr_other_id_no "
            + " ,usr_other_id_type "
            + " ,usr_tel_1 "
            + " ,usr_tel_2 "
            + " ,usr_fax_1 "
            + " ,usr_country_bil "
            + " ,usr_postal_code_bil "
            + " ,usr_state_bil "
//            + " ,usr_city_bil "
            + " ,usr_address_bil "
//            + " ,usr_occupation_bil "
//            + " ,usr_income_level "
//            + " ,usr_edu_role "
//            + " ,usr_edu_level "
//            + " ,usr_school_bil "
            + " ,usr_class "
            + " ,usr_class_number "
            + " ,usr_signup_date "
            + " ,usr_last_login_date "
//            + " ,usr_special_date_1 "
            + " ,usr_status "
            + " ,usr_upd_date "
            + " ,usr_extra_1 "
            + " ,usr_extra_2 "
            + " ,usr_extra_3 "
            + " ,usr_extra_4 "
            + " ,usr_extra_5 "
            + " ,usr_extra_6 "
            + " ,usr_extra_7 "
            + " ,usr_extra_8 "
            + " ,usr_extra_9 "
            + " ,usr_extra_10 "
            + " ,usr_cost_center ";


    private static final String SQL_OJH_COL = 
        " ojh_obj_bob_nod_id as obj_bob_nod_id, ojh_version as obj_version, " +
        " ojh_publish_ind as obj_publish_ind, ojh_latest_ind as obj_latest_ind, " +
        " ojh_type as obj_type, ojh_title as obj_title, ojh_desc as obj_desc, " + 
        " ojh_status as obj_status, ojh_keywords as obj_keywords, " +
        " ojh_update_usr_id as obj_update_usr_id, " + 
        " ojh_update_timestamp as obj_update_timestamp, " +
        " ojh_comment as obj_comment, ojh_author as obj_author, " + 
        " ojh_xml as obj_xml ";

    private static final String SQL_OBJ_COL = 
        " obj_bob_nod_id, obj_version, obj_publish_ind, obj_latest_ind, " +
        " obj_type, obj_title, obj_desc, obj_status, obj_keywords, " +
        " obj_update_usr_id, obj_update_timestamp, obj_comment, " +
        " obj_author, obj_xml ";
    
    private static final String SQL_TPL_COL = 
        " tpl_id, tpl_ttp_id, tpl_title, tpl_xml, " +
        " tpl_owner_ent_id, tpl_create_timestamp, " +
        " tpl_create_usr_id, tpl_upd_timestamp, tpl_upd_usr_id ";

    private static final String SQL_ATT_COL = 
        " att_id, att_type, att_filename, att_desc, att_att_id_parent ";

    public static final String SQL_GET_PARENT_FOLDER_ID = 
        " SELECT fld_nod_id " +
        " FROM kmLink , kmNode link, kmNode folder, kmFolder " +
        " WHERE lnk_target_nod_id = ? " +
        " AND lnk_nod_id = link.nod_id " +
        " AND link.nod_parent_nod_id = folder.nod_id " +
        " AND folder.nod_id = fld_nod_id " +
        " ORDER BY fld_title ASC ";
    
    public static final String SQL_GET_PARENT_DOMAIN_ID = 
        " SELECT fld_nod_id " +
        " FROM kmLink , kmNode link, kmNode folder, kmFolder " +
        " WHERE lnk_target_nod_id = ? " +
        " AND lnk_nod_id = link.nod_id " +
        " AND link.nod_parent_nod_id = folder.nod_id " +
        " AND folder.nod_id = fld_nod_id " +
        " AND fld_type = ? " +
        " ORDER BY fld_title ASC ";

    public static final String SQL_GET_PARENT_DOMAIN = 
        " SELECT " + 
        " folder.nod_id, folder.nod_type, folder.nod_order, folder.nod_parent_nod_id, " +
        " folder.nod_ancestor, folder.nod_create_timestamp, folder.nod_create_usr_id, " +
        " folder.nod_owner_ent_id, folder.nod_acl_inherit_ind " +
        ", " + SQL_FLD_COL +
        " FROM kmLink , kmNode link, kmNode folder, kmFolder " +
        " WHERE lnk_target_nod_id = ? " +
        " AND lnk_nod_id = link.nod_id " +
        " AND link.nod_parent_nod_id = folder.nod_id " +
        " AND folder.nod_id = fld_nod_id " +
        " AND fld_type = ? " + 
        " ORDER BY fld_title ASC ";

    public static final String SQL_GET_OBJ_ATT = 
        " SELECT " + SQL_ATT_COL + 
        " FROM kmObjectAttachment, Attachment " +
        " WHERE oat_obj_bob_nod_id = ? " +
        " AND oat_obj_version = ? " +
        " AND oat_att_id = att_id ";

    public static final String SQL_GET_OBJ_TPL = 
        " SELECT " + SQL_TPL_COL + ", ttp_title " + 
        " FROM kmObjectTemplate, aeTemplate, aeTemplateType " +
        " WHERE ojt_obj_bob_nod_id = ? " +
        " and ojt_obj_version = ? " +
        " and ojt_tpl_id = tpl_id " + 
        " and ojt_ttp_id = tpl_ttp_id " +
        " and tpl_ttp_id = ttp_id " ; 

    public static final String SQL_GET_OJH_TPL = 
        " SELECT " + SQL_TPL_COL + ", ttp_title " + 
        " FROM kmObjectHistory, aeTemplate, aeTemplateType " +
        " WHERE ojh_obj_bob_nod_id = ? " +
        " and ojh_version = ? " +
        " and ojh_tpl_id = tpl_id " + 
        " and ojh_ttp_id = tpl_ttp_id " +
        " and tpl_ttp_id = ttp_id " ; 

    public static final String SQL_GET_OBJ_BASIC = 
        " SELECT " + SQL_NOD_COL + "," + SQL_OBJ_COL +
        " FROM kmNode, kmObject " + 
        " WHERE nod_id = obj_bob_nod_id " +
        " AND nod_id = ? " +
        " AND obj_version = ? ";

    public static final String SQL_GET_OJH_BASIC = 
        " SELECT " + SQL_NOD_COL + "," + SQL_OJH_COL +
        " FROM kmNode, kmObjectHistory " + 
        " WHERE nod_id = ojh_obj_bob_nod_id " +
        " AND nod_id = ? " +
        " AND ojh_version = ? ";

    public static final String SQL_INS_OJT = 
        " INSERT INTO kmObjectTemplate " +
        " (ojt_obj_bob_nod_id, ojt_obj_version, ojt_ttp_id, ojt_tpl_id) " + 
        " VALUES " +
        " (?, ?, ?, ?) ";

    public static final String SQL_INS_OAT = 
        " INSERT INTO kmObjectAttachment " +
        " (oat_obj_bob_nod_id, oat_obj_version, oat_att_id) " + 
        " VALUES " +
        " (?, ?, ?) ";
    
    /*
    public static final String SQL_GET_PUBLISHED_VERSION_SET = 
        " SELECT " + SQL_NOD_COL + " , " + SQL_OBJ_COL + " , " + SQL_ATT_COL +
        " FROM kmObject, kmObjectAttachment, Attachment, kmNode " +
        " WHERE obj_publish_ind = ? " +
        " AND att_id = oat_att_id " + 
        " AND oat_obj_bob_nod_id = obj_bob_nod_id " +
        " AND oat_obj_version = obj_version " +
        " AND nod_id = obj_bob_nod_id " + 
        " AND obj_bob_nod_id in " ;

    public static final String SQL_GET_LATEST_VERSION_SET = 
        " SELECT " + SQL_NOD_COL + " , " + SQL_OBJ_COL + " , " + SQL_ATT_COL +
        " FROM kmObject, kmObjectAttachment, Attachment, kmNode " +
        " WHERE obj_latest_ind = ? " +
        " AND att_id = oat_att_id " + 
        " AND oat_obj_bob_nod_id = obj_bob_nod_id " +
        " AND oat_obj_version = obj_version " +
        " AND nod_id = obj_bob_nod_id " +
        " AND obj_bob_nod_id in " ;
    */
    
    public static final String SQL_GET_OAT_ATT_ID = 
        " SELECT oat_att_id FROM kmObjectAttachment " + 
        " WHERE oat_obj_bob_nod_id = ? ";

    public static final String SQL_DEL_OAT = 
        " DELETE FROM kmObjectAttachment " + 
        " WHERE oat_obj_bob_nod_id = ? ";
        
    public static final String SQL_DEL_ATT = 
        " DELETE FROM Attachment " + 
        " WHERE att_id in ";

    public static final String SQL_DEL_OJT = 
        " DELETE FROM kmObjectTemplate " + 
        " WHERE ojt_obj_bob_nod_id = ? ";

    public static final String SQL_DEL_OJH = 
        " DELETE FROM kmObjectHistory " + 
        " WHERE ojh_obj_bob_nod_id = ? ";

    public static final String SQL_GET_LINK_ID = 
        " SELECT lnk_nod_id FROM kmLink " + 
        " WHERE lnk_target_nod_id = ? ";

    public static final String SQL_DEL_OBJ2 = 
        " DELETE FROM kmObject " + 
        " WHERE obj_bob_nod_id = ? ";

    public static final String SQL_DEL_NOD2 = 
        " DELETE FROM kmNode " + 
        " WHERE nod_id = ? ";

    public static final String SQL_GET_LAST_PUB_OJH =
        " SELECT ojh_version FROM kmObjectHistory a " + 
        " WHERE a.ojh_publish_ind = ? " + 
        " AND a.ojh_obj_bob_nod_id = ? " +
        " AND a.ojh_update_timestamp = (SELECT MAX(ojh_update_timestamp) " +
        "                               FROM kmObjectHistory b " +
        "                               WHERE b.ojh_publish_ind = a.ojh_publish_ind " +
        "                               AND b.ojh_obj_bob_nod_id = a.ojh_obj_bob_nod_id) ";

    /*
    public static final String SQL_GET_OBJ_WITH_ATT = 
        " SELECT " + SQL_NOD_COL + " , " + SQL_OBJ_COL + ", " + SQL_ATT_COL + 
        " FROM kmNode, kmObject, kmObjectAttachment, Attachment " +
        " WHERE oat_obj_bob_nod_id = ? " +
        " AND oat_obj_version = ? " +
        " AND oat_att_id = att_id " +
        " AND obj_bob_nod_id = oat_obj_bob_nod_id " + 
        " AND obj_version = oat_obj_version " + 
        " AND obj_bob_nod_id = nod_id ";
    */
    
    public static final String SQL_UPD_FLD_OBJ_CNT_BY = 
        " UPDATE kmFolder SET " +
        " fld_obj_cnt = fld_obj_cnt + ? " + 
        " WHERE fld_nod_id in ";
        
    /**
    Insert the input arguments into database as a kmObject
    @param con Connection to database
    @param vNodColName Vector of kmNode column names 
    @param vNodColType Vector of kmNode column types
    @param vNodColValue Vector of kmNode column values
    @param vObjColName Vector of kmObject column names
    @param vObjColType Vector of kmObject column types
    @param vObjColValue Vector of kmObject column values
    @param vObjClobColName Vector of kmObject Clob column names
    @param vObjClobColValue Vector of kmObject Clob column values
    @param vTemplate Vector of aeTemplate
    @param vAttachment Vector of dbAttachment
    */
    public void ins(Connection con, 
                    Vector vNodColName, Vector vNodColType, Vector vNodColValue,
                    Vector vObjColName, Vector vObjColType, Vector vObjColValue,
                    Vector vObjClobColName, Vector vObjClobColValue, 
                    Vector vTemplate, Vector vAttachment, String bob_code) 
                    throws SQLException, cwSysMessage, cwException {

        this.dbObject = new DbKmObject();
        this.vTemplate = vTemplate;
        this.vAttachment = vAttachment;
                
        //insert into kmNode and kmObject
        this.dbObject.ins(con, vNodColName, vNodColType, vNodColValue, 
                          vObjColName, vObjColType, vObjColValue, vObjClobColName, vObjClobColValue, bob_code);
        
        //insert into kmObjectTemplate
        if(vTemplate != null) {
            //for each Template, insert into kmObjectTemplate 
            for(int i=0; i<vTemplate.size(); i++) {
                PreparedStatement stmt = null;
                try {
                    stmt = con.prepareStatement(SQL_INS_OJT);
                    aeTemplate tpl = (aeTemplate) vTemplate.elementAt(i);
                    stmt.setLong(1, this.dbObject.obj_bob_nod_id);
                    stmt.setString(2, this.dbObject.obj_version);
                    stmt.setLong(3, tpl.tpl_ttp_id);
                    stmt.setLong(4, tpl.tpl_id);
                    stmt.executeUpdate();
                } finally {
                    if(stmt!=null) stmt.close();
                }
            }
        }        
        //insert into kmObjectAttachment and Attachment
        if(vAttachment != null) {
            for(int i=0; i<vAttachment.size(); i++) {
                PreparedStatement stmt = null;
                try {
                    dbAttachment att = (dbAttachment) vAttachment.elementAt(i);
                    //insert into Attachment
                    if(att.att_type == null) {
                        att.att_type = dbAttachment.ATT_TYPE_KMOBJECT;
                    }
                    att.ins(con);
                    
                    //insert into kmObjectAttachment
                    stmt = con.prepareStatement(SQL_INS_OAT);
                    stmt.setLong(1, this.dbObject.obj_bob_nod_id);
                    stmt.setString(2, this.dbObject.obj_version);
                    stmt.setLong(3, att.att_id);
                    stmt.executeUpdate();
                } catch(qdbException e) {
                    throw new cwException("ViewKmObject.ins() :" + e.getMessage());
                } finally {
                    if(stmt!=null) stmt.close();
                }
            }
        }
        
        //update folder object count
        int index = vNodColName.indexOf("nod_parent_nod_id");
        Vector vFolderId = new Vector();
        vFolderId.addElement((Long)vNodColValue.elementAt(index));
        updateFolderObjCount(con, 1, vFolderId);
        
        return;
    }
    
    /**
    Update the fld_obj_cnt of the input folder by an amount
    @param con Connection to database
    @param amount amount to be added to the folder object count
    @param vFolderId Vector of Long which stores the folder node id
    */
    public void updateFolderObjCount(Connection con, long amount, Vector vFolderId) 
        throws SQLException {
        
        if(vFolderId != null && vFolderId.size() > 0) {
            String sql_fld_id_list = cwUtils.vector2list(vFolderId);
            PreparedStatement stmt = null;
            try {
                stmt = con.prepareStatement(SQL_UPD_FLD_OBJ_CNT_BY + sql_fld_id_list);
                CommonLog.debug("sql: " + SQL_UPD_FLD_OBJ_CNT_BY + sql_fld_id_list);
        
                stmt.setLong(1, amount);
                stmt.executeUpdate();
            } finally {
                if(stmt!=null) stmt.close();
            }
        }
        return;
    }
    
    
    /**
    Load the kmObject's data into this ViewKmObject.
    this.dbObject, this.vAttachment will be loaded.
    this.dbTemplate will be set to null.
    @param con Connection to database
    @param objId object node id
    @param version object node version
    */
    public void getWithAtt(Connection con, long objId, String version) 
        throws SQLException, cwSysMessage {
        
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(OuterJoinSqlStatements.getObjWithAtt(SQL_NOD_COL + " , " + SQL_OBJ_COL + ", " + SQL_ATT_COL));
            stmt.setLong(1, objId);
            stmt.setString(2, version);
            ResultSet rs = stmt.executeQuery();
            this.vAttachment = new Vector();
            this.vTemplate = null;
            boolean first = true;
            while(rs.next()) {
                if(first) {
                    getBasicFromRS(con, rs);
                }
                dbAttachment att = getAttachmentFromRS(con,rs);
                if(att != null) {
                    this.vAttachment.addElement(att);
                }
                first = false;
            }
        } finally {
            if(stmt!=null) stmt.close();
        }
        
    }
    
    /**
    Load the kmObject's data into this ViewKmObject.
    this.dbObject, this.vTemplate, this.vAttachment, this.updateUser will be loaded.
    @param con Connection to database
    @param objId object node id
    @param version object node version
    */
    public void getAll(Connection con, long objId, String version) 
        throws SQLException, cwSysMessage, cwException {

        //get this.dbObject
        getBasic(con, objId, version);
        
        //get this.vTemplate
        getTemplate(con, objId, version);
        
        //get this.vAttachment
        getAttachment(con, objId, version);

        //get this.updateUser
        try {
            this.updateUser = new dbRegUser();
            this.updateUser.get(con, this.dbObject.obj_update_usr_id);
        } catch(qdbException e) {
            throw new cwException(e.getMessage());
        }
            
        return;
    }

    
    /**
    Load the kmObject's data into this ViewKmObject.
    this.dbObject, this.vTemplate, this.vAttachment will be loaded.
    @param con Connection to database
    @param objId object node id
    @param version object node version
    */
    public void getWithTplNAtt(Connection con, long objId, String version) 
        throws SQLException, cwSysMessage {

        //get this.dbObject
        getBasic(con, objId, version);
        
        //get this.vTemplate
        getTemplate(con, objId, version);
        
        //get this.vAttachment
        getAttachment(con, objId, version);
        
        return;
    }

    /**
    Load the kmObject's data into this ViewKmObject.
    this.dbObject will be loaded.
    @param con Connection to database
    @param objId object node id
    @param version object node version
    */
    public void getBasic(Connection con, long objId, String version) 
        throws SQLException, cwSysMessage {
        
        PreparedStatement stmt = null;
        if(isVersionActive(con, objId, version)) {
            stmt = con.prepareStatement(SQL_GET_OBJ_BASIC);
        } else {
            stmt = con.prepareStatement(SQL_GET_OJH_BASIC);
        }
        try {
            stmt.setLong(1, objId);
            stmt.setString(2, version);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                getBasicFromRS(con, rs);
            }
        } finally {
            if(stmt!=null) stmt.close();
        }
        return;
    }

    /**
    Load this.dbObject from a ResultSet
    @param con Connection to database
    @param rs ResultSet contains all information to be loaded into this.dbObject
    */
    private void getBasicFromRS(Connection con, ResultSet rs) throws SQLException {
        this.dbObject = new DbKmObject();
        this.dbObject.nod_id = rs.getLong("nod_id");
        this.dbObject.nod_type = rs.getString("nod_type");
        this.dbObject.nod_parent_nod_id = rs.getLong("nod_parent_nod_id");
        this.dbObject.nod_ancestor = rs.getString("nod_ancestor");
        this.dbObject.nod_create_timestamp = rs.getTimestamp("nod_create_timestamp");
        this.dbObject.nod_create_usr_id = rs.getString("nod_create_usr_id");
        this.dbObject.nod_owner_ent_id = rs.getLong("nod_owner_ent_id");
        this.dbObject.nod_acl_inherit_ind = rs.getBoolean("nod_acl_inherit_ind");
        this.dbObject.obj_bob_nod_id = rs.getLong("obj_bob_nod_id");
        this.dbObject.obj_version = rs.getString("obj_version");
        this.dbObject.obj_publish_ind = rs.getBoolean("obj_publish_ind");
        this.dbObject.obj_latest_ind = rs.getBoolean("obj_latest_ind");
        this.dbObject.obj_type = rs.getString("obj_type");
        this.dbObject.obj_title = rs.getString("obj_title");
        this.dbObject.obj_desc = rs.getString("obj_desc");
        this.dbObject.obj_status = rs.getString("obj_status");
        this.dbObject.obj_keywords = rs.getString("obj_keywords");
        this.dbObject.obj_update_usr_id = rs.getString("obj_update_usr_id");
        try{
            this.dbObject.obj_update_usr_display_bil = dbRegUser.getUserName(con, this.dbObject.obj_update_usr_id);
        }catch(cwSysMessage e){
            throw new SQLException(e.getMessage());
        }
        this.dbObject.obj_update_timestamp = rs.getTimestamp("obj_update_timestamp");
        this.dbObject.obj_comment = rs.getString("obj_comment");
        this.dbObject.obj_author = rs.getString("obj_author");
        this.dbObject.obj_xml = cwSQL.getClobValue(rs, "obj_xml");        

        DbKmBaseObject baseObj = new DbKmBaseObject();
        baseObj.bob_nod_id = this.dbObject.nod_id;
        baseObj.get(con);
        this.dbObject.obj_nature = baseObj.bob_nature;
        this.dbObject.obj_code = baseObj.bob_code;
        this.dbObject.obj_delete_usr_id = baseObj.bob_delete_usr_id;
        this.dbObject.obj_delete_timestamp = baseObj.bob_delete_timestamp;

        return;
    }

    /**
    Load the kmObject's template into this.vTemplate
    @param con Connection to database
    @param objId object node id
    @param version object node version
    */
    public void getTemplate(Connection con, long objId, String version) 
        throws SQLException, cwSysMessage {
        
        PreparedStatement stmt = null;
        if(isVersionActive(con, objId, version)) {
            stmt = con.prepareStatement(SQL_GET_OBJ_TPL);
        } else {
            stmt = con.prepareStatement(SQL_GET_OJH_TPL);
        }
        try {
            stmt.setLong(1, objId);
            stmt.setString(2, version);
            ResultSet rs = stmt.executeQuery();
            this.vTemplate = null;
            while(rs.next()) {
                if(this.vTemplate == null) {
                    this.vTemplate = new Vector();
                }
                this.vTemplate.addElement(getTemplateFromRS(con,rs));
            }
        } finally {
            if(stmt!=null) stmt.close();
        }
        return;
    }

    /**
    Load one of the Templates from a ResultSet into aeTemplate
    @param con Connection to database
    @param rs ResultSet contains all information to be loaded into aeTemplate
    @return instance of aeTemplate
    */
    private aeTemplate getTemplateFromRS(Connection con, ResultSet rs) throws SQLException {
        aeTemplate tpl = new aeTemplate();
        tpl.tpl_id = rs.getLong("tpl_id");
        tpl.tpl_ttp_id = rs.getLong("tpl_ttp_id");
        tpl.tpl_title = rs.getString("tpl_title");
        tpl.tpl_owner_ent_id = rs.getLong("tpl_owner_ent_id");
        tpl.tpl_create_timestamp = rs.getTimestamp("tpl_create_timestamp");
        tpl.tpl_create_usr_id = rs.getString("tpl_create_usr_id");
        tpl.tpl_upd_timestamp = rs.getTimestamp("tpl_upd_timestamp");
        tpl.tpl_upd_usr_id = rs.getString("tpl_upd_usr_id");
        tpl.tpl_type = rs.getString("ttp_title");
        tpl.tpl_xml = cwSQL.getClobValue(rs, "tpl_xml");
        return tpl;
    }

    
    /**
    Load the kmObject's attachment into this.vAttachment
    @param con Connection to database
    @param objId object node id
    @param version object node version
    */
    public void getAttachment(Connection con, long objId, String version) 
        throws SQLException, cwSysMessage {

        PreparedStatement stmt = null;
        //check the existence of an version
        isVersionActive(con, objId, version);
        try {
            stmt = con.prepareStatement(SQL_GET_OBJ_ATT);
            stmt.setLong(1, objId);
            stmt.setString(2, version);
            ResultSet rs = stmt.executeQuery();
            this.vAttachment = null;
            while(rs.next()) {
                if(this.vAttachment == null) {
                    this.vAttachment = new Vector();
                }
                dbAttachment att = getAttachmentFromRS(con, rs);
                if(att != null) {
                    this.vAttachment.addElement(att);
                }
            }
        } finally {
            if(stmt!=null) stmt.close();
        }
        return;
    }

    /**
    Load one of the Attachments from a ResultSet into dbAttachment
    @param con Connection to database
    @param rs ResultSet contains all information to be loaded into dbAttachment
    @return instance of dbAttachment
    */
    private dbAttachment getAttachmentFromRS(Connection con, ResultSet rs) throws SQLException {
        dbAttachment att = new dbAttachment();
        att.att_id = rs.getLong("att_id");
        att.att_type = rs.getString("att_type");
        att.att_filename = rs.getString("att_filename");
        att.att_desc = rs.getString("att_desc");
        att.att_att_id_parent = rs.getLong("att_att_id_parent");
        if(att.att_id == 0) {
            att = null;
        }
        return att;
    }

    /**
    Test if this input version of object is an active version.
    Active version is defined as any version can be found in kmObject.
    Inactive version is defined as any version can be found in kmObjectHistory.
    It takes an assumption that a version can only exist in either kmObject or kmObjectHistory.
    This method can also be used to check the existence of the given object version.
    */
    public boolean isVersionActive(Connection con, long objId, String version) 
        throws SQLException, cwSysMessage {
        
        if((this.isVersionActive == null) 
            || (this.my_obj_bob_nod_id != objId )
            || ((this.my_obj_version == null) || (!this.my_obj_version.equals(version)))) {
            
            PreparedStatement stmt = null;
            PreparedStatement stmt2 = null;
            try {
                stmt = con.prepareStatement(SQL_GET_FROM_OBJ);
                stmt.setLong(1, objId);
                stmt.setString(2, version);
                ResultSet rs = stmt.executeQuery();
                if(rs.next()) { 
                    this.isVersionActive = new Boolean(true);
                } else {
                    stmt2 = con.prepareStatement(SQL_GET_FROM_OJH);
                    stmt2.setLong(1, objId);
                    stmt2.setString(2, version);
                    ResultSet rs2 = stmt2.executeQuery();
                    if(rs2.next()) {
                        this.isVersionActive = new Boolean(false);
                    } else {
                        throw new cwSysMessage("GEN005", "object node id: " + objId + ", version: " + version);
                    }
                }
            } finally {
                if(stmt!=null) stmt.close();
                if(stmt2!=null) stmt2.close();
            }
            this.my_obj_bob_nod_id = objId;
            this.my_obj_version = version;
        }
        return this.isVersionActive.booleanValue();
    }

    /**
    Get latest version of an object
    @param con Connection to database
    @param objId object node id 
    @return latest version of this object
    */
    public static String getLatestVersion(Connection con, long objId) throws SQLException {
        
        ViewKmObject object = new ViewKmObject();
        object.dbObject = new DbKmObject();
        object.dbObject.obj_bob_nod_id = objId;
        return object.getLatestVersion(con);
    }
    
    /**
    Get latest version of this object
    @param con Connection to database
    @return latest version of this object
    */
    public String getLatestVersion(Connection con) throws SQLException {
        
        PreparedStatement stmt = null;
        String latest_version = null;
        if(this.dbObject != null) {
            try {
                stmt = con.prepareStatement(SQL_GET_LATEST_VERSION);
                stmt.setLong(1, this.dbObject.obj_bob_nod_id);
                stmt.setBoolean(2, true);
                //stmt.setInt(2, 1);
                
                ResultSet rs = stmt.executeQuery();
                
                if(rs.next()) {
                    latest_version = rs.getString("obj_version");
                } 
            } finally {
                if(stmt!=null) stmt.close();
            }
        }
        return latest_version;
    }

    /**
    Get the published version of input objects
    @param con Connectin to database
    @param vObjId Vector of Long objects of object id
    @return Hashtable of ViewKmObject with dbObject and vAttachment populated.
            And the Hashtable has Long objects of node id as key
    */
    public static Hashtable getPublishedVersion(Connection con, Vector vObjId) 
                                                throws SQLException {
        Hashtable h = null;
        if(vObjId != null) {
            h = new Hashtable();
            if(vObjId.size() > 0) {
                PreparedStatement stmt = null;
                StringBuffer SQLBuf = new StringBuffer();
                SQLBuf.append(OuterJoinSqlStatements.getPublishedObjVersionSet(SQL_NOD_COL + " , " + SQL_OBJ_COL + " , " + SQL_ATT_COL))
                      .append(cwUtils.vector2list(vObjId))
                      .append(" ORDER BY obj_bob_nod_id ASC ");
                try {
                    stmt = con.prepareStatement(SQLBuf.toString());
                    stmt.setBoolean(1, true);
                    ResultSet rs = stmt.executeQuery();
                    long lastObjectId = -1;
                    ViewKmObject object = new ViewKmObject();
                    //for each record returned, group by obj_bob_nod_id
                    while(rs.next()) {
                        long thisObjectId = rs.getLong("obj_bob_nod_id");
                        if(thisObjectId != lastObjectId) {
                            if(lastObjectId != -1) {
                                Long key = new Long(lastObjectId);
                                h.put(key, object);
                            }
                            object = new ViewKmObject();
                            object.getBasicFromRS(con, rs);
                            object.vAttachment = new Vector();
                        }

                        dbAttachment att = object.getAttachmentFromRS(con,rs);
                        if(att!=null) {
                            object.vAttachment.addElement(att);
                        }
                        lastObjectId = thisObjectId;
                    }
                    //add the last one to Hashtable
                    if(lastObjectId != -1) {
                        h.put(new Long(lastObjectId), object);
                    }
                } finally {
                    if(stmt!=null) stmt.close();
                }
            }
        }
        return h;
    }

    /**
    Get the latest version of input objects
    @param con Connectin to database
    @param vObjId Vector of Long objects of object id
    @return Hashtable of ViewKmObject with dbObject and vAttachment populated.
            And the Hashtable has Long objects of node id as key
    */
    public static Hashtable getLatestVersion(Connection con, Vector vObjId) 
                                             throws SQLException {
        Hashtable h = null;
        if(vObjId != null) {
            h = new Hashtable();
            if(vObjId.size() > 0) {
                PreparedStatement stmt = null;
                StringBuffer SQLBuf = new StringBuffer();
                SQLBuf.append(OuterJoinSqlStatements.getLatestObjVersionSet(SQL_NOD_COL + " , " + SQL_OBJ_COL + " , " + SQL_ATT_COL))
                      .append(cwUtils.vector2list(vObjId))
                      .append(" ORDER BY obj_bob_nod_id ASC ");
                try {
                    stmt = con.prepareStatement(SQLBuf.toString());
                    stmt.setBoolean(1, true);
                    ResultSet rs = stmt.executeQuery();
                    long lastObjectId = -1;
                    ViewKmObject object = new ViewKmObject();
                    //for each record returned, group by obj_bob_nod_id
                    while(rs.next()) {
                        long thisObjectId = rs.getLong("obj_bob_nod_id");
                        if(thisObjectId != lastObjectId) {
                            if(lastObjectId != -1) {
                                Long key = new Long(lastObjectId);
                                h.put(key, object);
                            }
                            object = new ViewKmObject();
                            object.getBasicFromRS(con, rs);
                            object.vAttachment = new Vector();
                        }
                        dbAttachment att = object.getAttachmentFromRS(con,rs);
                        if(att!=null) {
                            object.vAttachment.addElement(att);
                        }
                        lastObjectId = thisObjectId;
                    }
                    //add the last one to Hashtable
                    if(lastObjectId != -1) {
                        h.put(new Long(lastObjectId), object);
                    }
                } finally {
                    if(stmt!=null) stmt.close();
                }
            }
        }
        return h;
    }

    /**
    Get published version an object
    @param con Connection to database
    @param objId object node id
    @return published version of an object
    */
    public static String getPublishedVersion(Connection con, long objId) throws SQLException {
        
        ViewKmObject object = new ViewKmObject();
        object.dbObject = new DbKmObject();
        object.dbObject.obj_bob_nod_id = objId;
        return object.getPublishedVersion(con);
    }

    /**
    Get published version of this object
    @param con Connection to database
    @return published version of this object
    */
    public String getPublishedVersion(Connection con) throws SQLException {
        
        PreparedStatement stmt = null;
        String published_version = null;
        if(this.dbObject != null) {
            try {
                stmt = con.prepareStatement(SQL_GET_PUBLISHED_VERSION);
                stmt.setLong(1, this.dbObject.obj_bob_nod_id);
                stmt.setBoolean(2, true);
                ResultSet rs = stmt.executeQuery();
                if(rs.next()) {
                    published_version = rs.getString("obj_version");
                } 
            } finally {
                if(stmt!=null) stmt.close();
            }
        }
        return published_version;
    }

    /**
    Get the node id of this object's parent domain
    @param con Connection to database
    @return Vector of DbKmFolder contains this object's parent domain node 
    */
    public Vector getParentDomain(Connection con) throws SQLException {
        
        PreparedStatement stmt = null;
        Vector v = new Vector();
        if(this.dbObject != null) {
            try {
                stmt = con.prepareStatement(SQL_GET_PARENT_DOMAIN);
                stmt.setLong(1, this.dbObject.obj_bob_nod_id);
                stmt.setString(2, DbKmFolder.FOLDER_TYPE_DOMAIN);
                ResultSet rs = stmt.executeQuery();
                while(rs.next()) {
                    DbKmFolder folder = new DbKmFolder();
                    folder.nod_id = rs.getLong("nod_id");
                    folder.nod_type = rs.getString("nod_type");
                    folder.nod_order = rs.getLong("nod_order");
                    folder.nod_parent_nod_id = rs.getLong("nod_parent_nod_id");
                    folder.nod_ancestor = rs.getString("nod_ancestor");
                    folder.nod_create_timestamp = rs.getTimestamp("nod_create_timestamp");
                    folder.nod_create_usr_id = rs.getString("nod_create_usr_id");
                    folder.nod_owner_ent_id = rs.getLong("nod_owner_ent_id");
                    folder.nod_acl_inherit_ind = rs.getBoolean("nod_acl_inherit_ind");
                    folder.fld_nod_id = rs.getLong("fld_nod_id");
                    folder.fld_type = rs.getString("fld_type");
                    folder.fld_title = rs.getString("fld_title");
                    folder.fld_desc = rs.getString("fld_desc");
                    folder.fld_obj_cnt = rs.getLong("fld_obj_cnt");
                    folder.fld_update_usr_id = rs.getString("fld_update_usr_id");
                    folder.fld_update_timestamp = rs.getTimestamp("fld_update_timestamp");
                    v.addElement(folder);
                }
            } finally {
                if(stmt!=null) stmt.close();
            }
        }
        return v;
    }

    /**
    Get the node id of this object's parent folder (domain and workfolder)
    @param con Connection to database
    @return Vector of Long objects contains this object's parent domain node id
    */
    public Vector getParentFolderId(Connection con) throws SQLException {
        
        PreparedStatement stmt = null;
        Vector v = new Vector();
        if(this.dbObject != null) {
            try {
                stmt = con.prepareStatement(SQL_GET_PARENT_FOLDER_ID);
                stmt.setLong(1, this.dbObject.obj_bob_nod_id);
                ResultSet rs = stmt.executeQuery();
                while(rs.next()) {
                    v.addElement(new Long(rs.getLong("fld_nod_id")));
                }
            } finally {
                if(stmt!=null) stmt.close();
            }
        }
        return v;
    }
    
      /**
    Get the node id of this object's parent folder (workfolder only)
    @param con Connection to database
    @return Vector of Long objects contains this object's parent domain node id
    */
    public Vector getParentWorkFolderId(Connection con) throws SQLException {
        
        PreparedStatement stmt = null;
        Vector v = new Vector();
        String sql = "SELECT fld_nod_id FROM kmNode link, kmFolder " + 
            " where link.nod_parent_nod_id = fld_nod_id "+
            " and link.nod_id = ?"; 
        if(this.dbObject != null) {
            try {
                stmt = con.prepareStatement(sql);
                stmt.setLong(1, this.dbObject.obj_bob_nod_id);
                ResultSet rs = stmt.executeQuery();
                while(rs.next()) {
                    v.addElement(new Long(rs.getLong("fld_nod_id")));
                }
            } finally {
                if(stmt!=null) stmt.close();
            }
        }
        return v;
    }
    
    
    
    /**
    Get the node id of this object's parent domain
    @param con Connection to database
    @return Vector of Long objects contains this object's parent domain node id
    */
    public Vector getParentDomainId(Connection con) throws SQLException {
        
        PreparedStatement stmt = null;
        Vector v = new Vector();
        if(this.dbObject != null) {
            try {
                stmt = con.prepareStatement(SQL_GET_PARENT_DOMAIN_ID);
                stmt.setLong(1, this.dbObject.obj_bob_nod_id);
                stmt.setString(2, DbKmFolder.FOLDER_TYPE_DOMAIN);
                ResultSet rs = stmt.executeQuery();
                while(rs.next()) {
                    v.addElement(new Long(rs.getLong("fld_nod_id")));
                }
            } finally {
                if(stmt!=null) stmt.close();
            }
        }
        return v;
    }

    /**
    Check out this object with concurrenct control
    @param con Connection to database
    @param objStatus new object status
    @param userId user id of who are performing this check out action
    @param lastUpdTimestamp last update timestamp for concurrence control
    @return number of rows updated
    */
    public int updateObjStatus(Connection con, String objStatus,
                               String userId, Timestamp lastUpdTimestamp) 
                               throws SQLException {
        
        PreparedStatement stmt = null;
        try {
            Timestamp curTime = cwSQL.getTime(con);
            stmt = con.prepareStatement(SQL_UPDATE_OBJ_STATUS_WITH_CONTROL);
            stmt.setString(1, objStatus);
            stmt.setString(2, userId);
            stmt.setTimestamp(3, curTime);
            stmt.setLong(4, this.dbObject.obj_bob_nod_id);
            stmt.setString(5, this.dbObject.obj_version);
            stmt.setTimestamp(6, lastUpdTimestamp);
            return stmt.executeUpdate();
        } finally {
            if(stmt!=null) stmt.close();
        }
    }

    public static final String SQL_MOVE_OBJ_HISTORY = 
        " INSERT INTO kmObjectHistory " + 
        " (ojh_obj_bob_nod_id , ojh_version , " +
        " ojh_publish_ind , ojh_latest_ind , " +
        " ojh_type , ojh_title , ojh_desc , " + 
        " ojh_status , ojh_keywords , " +
        " ojh_update_usr_id , " + 
        " ojh_update_timestamp , " +
        " ojh_comment , ojh_author , " + 
        " ojh_xml, ojh_ttp_id, ojh_tpl_id) " + 
        " SELECT " +
        " obj_bob_nod_id, obj_version, obj_publish_ind, 0, " +
        " obj_type, obj_title, obj_desc, obj_status, obj_keywords, " +
        " obj_update_usr_id, obj_update_timestamp, obj_comment, " +
        " obj_author, obj_xml, ojt_ttp_id, ojt_tpl_id " +
        " FROM kmObject, kmObjectTemplate, aeTemplateType " +
        " WHERE obj_bob_nod_id = ? " +
        " AND obj_version = ? " +
        " AND ojt_obj_bob_nod_id = obj_bob_nod_id " +
        " AND ojt_obj_version = obj_version " +
        " AND ojt_ttp_id = ttp_id " +
        " AND ttp_title = ? ";

    public static final String SQL_DEL_OBJ_TPL = 
        " DELETE FROM kmObjectTemplate " + 
        " WHERE ojt_obj_bob_nod_id = ? " + 
        " AND ojt_obj_version = ? ";

    public static final String SQL_DEL_OBJ = 
        " DELETE FROM kmObject " + 
        " WHERE obj_bob_nod_id = ? " + 
        " AND obj_version = ? ";

    /**
    Create a new version of the input object.
    This method is synchronized because it has a high potential of deadlock
    @param con Connection to database
    @param lastUpdTimestamp last update timestamp of this object for concurrence control
    @param object ViewKmObject to be deprecated
    @param vObjColName Vector of kmObject column names for new version
    @param vObjColType Vector of kmObject column types for new version
    @param vObjColValue Vector of kmObject column values for new version
    @param vObjClobColName Vector of kmObject Clob column names for new version
    @param vObjClobColValue Vector of kmObject Clob column values for new version
    @param vTemplate Vector of aeTemplate for new version
    @param vAttachment Vector of dbAttachment for new version
    @param vDomainIdList Vector of Long objects of domain node id that the new version will be attached to
    @return a new version object, or null of cannot pass concurrence control.
    */
    public static  ViewKmObject synNewVersion(Connection con, Timestamp lastUpdTimestamp, ViewKmObject object,
                                                          Vector vObjColName, Vector vObjColType, Vector vObjColValue,
                                                          Vector vObjClobColName, Vector vObjClobColValue, 
                                                          Vector vTemplate, Vector vAttachment,
                                                          Vector vDomainIdList) 
                                                          throws SQLException, cwException, cwSysMessage {

        return object.newVersion(con, lastUpdTimestamp, vObjColName, vObjColType, vObjColValue,
                                 vObjClobColName, vObjClobColValue, 
                                 vTemplate, vAttachment, vDomainIdList);
    }

    public static final String SQL_UPD_OBJ_LATEST_IND = 
        " UPDATE kmObject " + 
        " SET obj_latest_ind = ? " +
        " WHERE obj_bob_nod_id = ? " + 
        " AND obj_version = ? ";
    
    /**
    Create a new version of this object
    @param con Connection to database
    @param lastUpdTimestamp last update timestamp of this object for concurrence control
    @param vObjColName Vector of kmObject column names for new version
    @param vObjColType Vector of kmObject column types for new version
    @param vObjColValue Vector of kmObject column values for new version
    @param vObjClobColName Vector of kmObject Clob column names for new version
    @param vObjClobColValue Vector of kmObject Clob column values for new version
    @param vTemplate Vector of aeTemplate for new version
    @param vAttachment Vector of dbAttachment for new version
    @param vDomainIdList Vector of Long objects of domain node id that the new version will be attached to
    @return a new version object, or null of cannot pass concurrence control.
    */
    private ViewKmObject newVersion(Connection con, Timestamp lastUpdTimestamp,
                                   Vector vObjColName, Vector vObjColType, Vector vObjColValue,
                                   Vector vObjClobColName, Vector vObjClobColValue, 
                                   Vector vTemplate, Vector vAttachment, Vector vDomainIdList) 
                                   throws SQLException, cwException, cwSysMessage {        
        
        //get XLock on underly record in KmObject
        if(!this.dbObject.getXLock(con, lastUpdTimestamp)) {
            return null;
        }

        //test if the new version is published version
        int index = vObjColName.indexOf("obj_publish_ind");
        boolean isNewPublishedVersion = ((Boolean)vObjColValue.elementAt(index)).booleanValue();
        CommonLog.debug("in new version: isNewPublishedVersion: " + isNewPublishedVersion);
        CommonLog.debug("in new version: this.dbObject.obj_publish_ind: " + this.dbObject.obj_publish_ind);
        
        //get published version
        String publishedVersion = null;
        if(this.dbObject.obj_publish_ind) {
            publishedVersion = this.dbObject.obj_version;
        } else {
            publishedVersion = getPublishedVersion(con);
        }
        
        //get original parent domain id list
        Vector vOrgDomainIdList = getParentDomainId(con);
        
        //move this version (latest version) to kmObjectHistory
        PreparedStatement stmt = null;
        if(isNewPublishedVersion || !this.dbObject.obj_publish_ind) {
            try {
                stmt = con.prepareStatement(SQL_MOVE_OBJ_HISTORY);
                stmt.setLong(1, this.dbObject.obj_bob_nod_id);
                stmt.setString(2, this.dbObject.obj_version);
                stmt.setString(3, aeTemplate.ITEM);
                stmt.executeUpdate();
            } finally {
                if(stmt!=null) stmt.close();
            }
        } else {
            //update old version's latest version indicator to false
            try {
                stmt = con.prepareStatement(SQL_UPD_OBJ_LATEST_IND);
                stmt.setBoolean(1, false);
                stmt.setLong(2, this.dbObject.obj_bob_nod_id);
                stmt.setString(3, this.dbObject.obj_version);
                stmt.executeUpdate();
            } finally {
                if(stmt!=null) stmt.close();
            }
        }
        // move the published version to kmObjectHistory
        if(isNewPublishedVersion && !(this.dbObject.obj_publish_ind) && publishedVersion != null) {
            PreparedStatement stmt2 = null;
            try {
                stmt2 = con.prepareStatement(SQL_MOVE_OBJ_HISTORY);
                stmt2.setLong(1, this.dbObject.obj_bob_nod_id);
                stmt2.setString(2, publishedVersion);
                stmt2.setString(3, aeTemplate.ITEM);
                stmt2.executeUpdate();
            } finally {
                if(stmt2!=null) stmt2.close();
            }
        }
        
        //insert new version into kmObject
        //insert into kmNode and kmObject
        DbKmObject newVersionObject = new DbKmObject();
        newVersionObject.ins(con,
                             vObjColName, vObjColType, vObjColValue, 
                             vObjClobColName, vObjClobColValue);
        
        //insert into kmObjectTemplate
        if(vTemplate != null) {
            //for each Template, insert into kmObjectTemplate 
            for(int i=0; i<vTemplate.size(); i++) {
                PreparedStatement stmt3 = null;
                try {
                    stmt3 = con.prepareStatement(SQL_INS_OJT);
                    aeTemplate tpl = (aeTemplate) vTemplate.elementAt(i);
                    stmt3.setLong(1, newVersionObject.obj_bob_nod_id);
                    stmt3.setString(2, newVersionObject.obj_version);
                    stmt3.setLong(3, tpl.tpl_ttp_id);
                    stmt3.setLong(4, tpl.tpl_id);
                    stmt3.executeUpdate();
                } finally {
                    if(stmt3!=null) stmt3.close();
                }
            }
        }
        //insert into kmObjectAttachment and Attachment
        if(vAttachment != null) {
            for(int i=0; i<vAttachment.size(); i++) {
                PreparedStatement stmt4 = null;
                try {
                    dbAttachment att = (dbAttachment) vAttachment.elementAt(i);
                    //insert into Attachment
                    if(att.att_type == null) {
                        att.att_type = dbAttachment.ATT_TYPE_KMOBJECT;
                    }
                    att.ins(con);
                    
                    //insert into kmObjectAttachment
                    stmt4 = con.prepareStatement(SQL_INS_OAT);
                    stmt4.setLong(1, newVersionObject.obj_bob_nod_id);
                    stmt4.setString(2, newVersionObject.obj_version);
                    stmt4.setLong(3, att.att_id);
                    stmt4.executeUpdate();
                } catch(qdbException e) {
                    throw new cwException("ViewKmObject.newVersion() :" + e.getMessage());
                } finally {
                    if(stmt4!=null) stmt4.close();
                }
            }
        }
        
        //delete old version(s) from kmObjectTemplate
        PreparedStatement stmt5 = null;
        if(isNewPublishedVersion || !this.dbObject.obj_publish_ind) {
            try {
                stmt5 = con.prepareStatement(SQL_DEL_OBJ_TPL);
                stmt5.setLong(1, this.dbObject.obj_bob_nod_id);
                stmt5.setString(2, this.dbObject.obj_version);
                stmt5.executeUpdate();
            } finally {
                if(stmt5!=null) stmt5.close();
            }
        }
        if(isNewPublishedVersion && !(this.dbObject.obj_publish_ind) && publishedVersion != null) {
            PreparedStatement stmt6 = null;
            try {
                stmt6 = con.prepareStatement(SQL_DEL_OBJ_TPL);
                stmt6.setLong(1, this.dbObject.obj_bob_nod_id);
                stmt6.setString(2, publishedVersion);
                stmt6.executeUpdate();
            } finally {
                if(stmt6!=null) stmt6.close();
            }
        }
        
        //delete old version(s) from kmObject
        int rowCount = 0;
        PreparedStatement stmt7 = null;
        if(isNewPublishedVersion || !this.dbObject.obj_publish_ind) {
            try {
                stmt7 = con.prepareStatement(SQL_DEL_OBJ);
                stmt7.setLong(1, this.dbObject.obj_bob_nod_id);
                stmt7.setString(2, this.dbObject.obj_version);
                rowCount += stmt7.executeUpdate();
            } finally {
                if(stmt7!=null) stmt7.close();
            }
        }
        if(isNewPublishedVersion && !(this.dbObject.obj_publish_ind) && publishedVersion != null) {
            PreparedStatement stmt8 = null;
            try {
                stmt8 = con.prepareStatement(SQL_DEL_OBJ);
                stmt8.setLong(1, this.dbObject.obj_bob_nod_id);
                stmt8.setString(2, publishedVersion);
                rowCount += stmt8.executeUpdate();
            } finally {
                if(stmt8!=null) stmt8.close();
            }
        }
        
        //create the newVersion Object and return
        ViewKmObject viewNewVersionObject = new ViewKmObject();
        viewNewVersionObject.dbObject = newVersionObject;
        viewNewVersionObject.vTemplate = vTemplate;
        viewNewVersionObject.vAttachment = vAttachment;
        viewNewVersionObject.dbObject.nod_id = this.dbObject.nod_id;
        viewNewVersionObject.dbObject.nod_type = this.dbObject.nod_type;
        viewNewVersionObject.dbObject.nod_order = this.dbObject.nod_order;
        viewNewVersionObject.dbObject.nod_parent_nod_id = this.dbObject.nod_parent_nod_id;
        viewNewVersionObject.dbObject.nod_ancestor = this.dbObject.nod_ancestor;
        viewNewVersionObject.dbObject.nod_create_usr_id = this.dbObject.nod_create_usr_id;
        viewNewVersionObject.dbObject.nod_create_timestamp = this.dbObject.nod_create_timestamp;
        viewNewVersionObject.dbObject.nod_owner_ent_id = this.dbObject.nod_owner_ent_id;
        viewNewVersionObject.dbObject.nod_acl_inherit_ind = this.dbObject.nod_acl_inherit_ind;
        this.dbObject.obj_latest_ind = false;

        //attach the new version to domains
        if(viewNewVersionObject.dbObject.obj_publish_ind) {
            
            DbKmLink.delTarget(con, viewNewVersionObject.dbObject.obj_bob_nod_id);
            
            if(vDomainIdList != null) {
                Timestamp curTime = cwSQL.getTime(con);
                String userId = viewNewVersionObject.dbObject.obj_update_usr_id;
                long siteId = viewNewVersionObject.dbObject.nod_owner_ent_id;
                
                for(int i=0; i<vDomainIdList.size(); i++) {
                    DbKmLink link = new DbKmLink();
                    link.nod_type = DbKmNode.NODE_TYPE_LINK;
                    link.nod_parent_nod_id = ((Long)vDomainIdList.elementAt(i)).longValue();
                    link.nod_create_timestamp = curTime;
                    link.nod_create_usr_id = userId;
                    link.nod_owner_ent_id = siteId;
                    link.nod_acl_inherit_ind = true;
                    link.lnk_type = DbKmLink.LINK_TYPE_OBJECT;
                    link.lnk_title = viewNewVersionObject.dbObject.obj_title;
                    link.lnk_target_nod_id = viewNewVersionObject.dbObject.obj_bob_nod_id;
                    link.ins(con);
                }
            }            
        }
        
        //update domain object count
        //-1 from fld_obj_cnt
        if(isNewPublishedVersion) {
            Vector vMinusFolderId = new Vector();
            if(vOrgDomainIdList != null) {
                if(vDomainIdList != null) {
                    for(int i=0; i<vOrgDomainIdList.size(); i++) {
                        if(vDomainIdList.indexOf((Long)vOrgDomainIdList.elementAt(i)) < 0) {
                            vMinusFolderId.addElement((Long)vOrgDomainIdList.elementAt(i));
                        }
                    }
                } else {
                    vMinusFolderId = vOrgDomainIdList;
                }
                updateFolderObjCount(con, -1, vMinusFolderId);
            }
            //+1 to fld_obj_cnt
            Vector vPlusFolderId = new Vector();
            if(vDomainIdList != null) {
                if(vOrgDomainIdList != null) {
                    for(int i=0; i<vDomainIdList.size(); i++) {
                        if(vOrgDomainIdList.indexOf((Long)vDomainIdList.elementAt(i)) < 0) {
                            vPlusFolderId.addElement((Long)vDomainIdList.elementAt(i));
                        }
                    }
                } else {
                    vPlusFolderId = vDomainIdList;
                }
                updateFolderObjCount(con, 1, vPlusFolderId);
            }
        }
        
        return viewNewVersionObject;
    }

    public void addDomain(Connection con, Timestamp lastUpdTimestamp,
                                   Vector vDomainIdList) 
                                   throws SQLException, cwException, cwSysMessage {
        
        //get XLock on underly record in KmObject
        if(!this.dbObject.getXLock(con, lastUpdTimestamp)) {
           // return null;
        }
        
        //get original parent domain id list
        Vector vOrgDomainIdList = getParentDomainId(con);
        
        
          //attach the new version to domains
            
        DbKmLink.delTarget(con, this.dbObject.obj_bob_nod_id);
            
        if(vDomainIdList != null) {
            Timestamp curTime = cwSQL.getTime(con);
            String userId = this.dbObject.obj_update_usr_id;
            long siteId = this.dbObject.nod_owner_ent_id;
                
            for(int i=0; i<vDomainIdList.size(); i++) {
                DbKmLink link = new DbKmLink();
                link.nod_type = DbKmNode.NODE_TYPE_LINK;
                link.nod_parent_nod_id = ((Long)vDomainIdList.elementAt(i)).longValue();
                link.nod_create_timestamp = curTime;
                link.nod_create_usr_id = userId;
                link.nod_owner_ent_id = siteId;
                link.nod_acl_inherit_ind = true;
                link.lnk_type = DbKmLink.LINK_TYPE_OBJECT;
                link.lnk_title = this.dbObject.obj_title;
                link.lnk_target_nod_id = this.dbObject.obj_bob_nod_id;
                link.ins(con);
            }
        }            
        
        //update domain object count
        //-1 from fld_obj_cnt
        //if(isNewPublishedVersion) {
            Vector vMinusFolderId = new Vector();
            if(vOrgDomainIdList != null) {
                if(vDomainIdList != null) {
                    for(int i=0; i<vOrgDomainIdList.size(); i++) {
                        if(vDomainIdList.indexOf((Long)vOrgDomainIdList.elementAt(i)) < 0) {
                            vMinusFolderId.addElement((Long)vOrgDomainIdList.elementAt(i));
                        }
                    }
                } else {
                    vMinusFolderId = vOrgDomainIdList;
                }
                updateFolderObjCount(con, -1, vMinusFolderId);
            }
            //+1 to fld_obj_cnt
            Vector vPlusFolderId = new Vector();
            if(vDomainIdList != null) {
                if(vOrgDomainIdList != null) {
                    for(int i=0; i<vDomainIdList.size(); i++) {
                        if(vOrgDomainIdList.indexOf((Long)vDomainIdList.elementAt(i)) < 0) {
                            vPlusFolderId.addElement((Long)vDomainIdList.elementAt(i));
                        }
                    }
                } else {
                    vPlusFolderId = vDomainIdList;
                }
                updateFolderObjCount(con, 1, vPlusFolderId);
            }
        //}
        
        //return viewNewVersionObject;
    }


    public static final String SQL_GET_OBJ_VERSION_LIST = 
        " SELECT " + SQL_USR_COL + " , " + SQL_NOD_COL + 
        " ,obj_bob_nod_id, obj_version, obj_publish_ind, obj_latest_ind " +
        " ,obj_type, obj_title, obj_desc, obj_status, obj_keywords " +
        " ,obj_update_usr_id, obj_update_timestamp, obj_comment, obj_author " + 
        " FROM kmObject, kmNode, RegUser " + 
        " WHERE obj_bob_nod_id = ? " + 
        " AND obj_bob_nod_id = nod_id " +
        " AND obj_update_usr_id = usr_id " +
        " UNION " +
        " SELECT " + SQL_USR_COL + " , " + SQL_NOD_COL + 
        " ,ojh_obj_bob_nod_id as obj_bob_nod_id, ojh_version as obj_version " + 
        " ,ojh_publish_ind as obj_publish_ind, ojh_latest_ind as obj_latest_ind " + 
        " ,ojh_type as obj_type, ojh_title as obj_title, ojh_desc as obj_desc " +
        " ,ojh_status as obj_status, ojh_keywords as obj_keywords " + 
        " ,ojh_update_usr_id as obj_update_usr_id, ojh_update_timestamp as obj_update_timestamp " +
        " ,ojh_comment as obj_comment, ojh_author as obj_author " + 
        " FROM kmObjectHistory, kmNode, RegUser " + 
        " WHERE ojh_obj_bob_nod_id = ? " + 
        " AND ojh_obj_bob_nod_id = nod_id " +
        " AND ojh_update_usr_id = usr_id " +
        " ORDER BY obj_update_timestamp DESC ";

    /**
    Get the version history of an object from kmObject and kmObjectHistory
    Clob columns will not be populated as these columns cannot be selected from an UNION
    @param con Connection to database
    @param objId object node id 
    @return Vector of ViewKmObject order by update timestamp desc
    */
    public static Vector getVersionList(Connection con, long objId) throws SQLException {
        
        Vector v = new Vector();
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(SQL_GET_OBJ_VERSION_LIST);
            stmt.setLong(1, objId);
            stmt.setLong(2, objId);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                ViewKmObject object = new ViewKmObject();
                object.dbObject = new DbKmObject();
                object.dbObject.nod_id = rs.getLong("nod_id");
                object.dbObject.nod_type = rs.getString("nod_type");
                object.dbObject.nod_parent_nod_id = rs.getLong("nod_parent_nod_id");
                object.dbObject.nod_ancestor = rs.getString("nod_ancestor");
                object.dbObject.nod_create_timestamp = rs.getTimestamp("nod_create_timestamp");
                object.dbObject.nod_create_usr_id = rs.getString("nod_create_usr_id");
                object.dbObject.nod_owner_ent_id = rs.getLong("nod_owner_ent_id");
                object.dbObject.nod_acl_inherit_ind = rs.getBoolean("nod_acl_inherit_ind");
                object.dbObject.obj_bob_nod_id = rs.getLong("obj_bob_nod_id");
                object.dbObject.obj_version = rs.getString("obj_version");
                object.dbObject.obj_publish_ind = rs.getBoolean("obj_publish_ind");
                object.dbObject.obj_latest_ind = rs.getBoolean("obj_latest_ind");
                object.dbObject.obj_type = rs.getString("obj_type");
                object.dbObject.obj_title = rs.getString("obj_title");
                object.dbObject.obj_desc = rs.getString("obj_desc");
                object.dbObject.obj_status = rs.getString("obj_status");
                object.dbObject.obj_keywords = rs.getString("obj_keywords");
                object.dbObject.obj_update_usr_id = rs.getString("obj_update_usr_id");
                object.dbObject.obj_update_timestamp = rs.getTimestamp("obj_update_timestamp");
                object.dbObject.obj_comment = rs.getString("obj_comment");
                object.dbObject.obj_author = rs.getString("obj_author");
                object.updateUser = new dbRegUser();
                object.updateUser.usr_id = rs.getString("usr_id");
                object.updateUser.usr_ste_usr_id = rs.getString("usr_ste_usr_id");
                object.updateUser.usr_ent_id = rs.getLong("usr_ent_id");
                object.updateUser.usr_ste_ent_id = rs.getLong("usr_ste_ent_id");
                object.updateUser.usr_pwd = rs.getString("usr_pwd");
                object.updateUser.usr_email = rs.getString("usr_email");
                object.updateUser.usr_email_2 = rs.getString("usr_email_2");
                object.updateUser.usr_full_name_bil = rs.getString("usr_full_name_bil");
                object.updateUser.usr_initial_name_bil = rs.getString("usr_initial_name_bil");
                object.updateUser.usr_last_name_bil = rs.getString("usr_last_name_bil");
                object.updateUser.usr_first_name_bil = rs.getString("usr_first_name_bil");
                object.updateUser.usr_display_bil = rs.getString("usr_display_bil");
                object.updateUser.usr_gender = rs.getString("usr_gender");
                object.updateUser.usr_bday = rs.getTimestamp("usr_bday");
//                object.updateUser.usr_bplace_bil = rs.getString("usr_bplace_bil");
                object.updateUser.usr_hkid = rs.getString("usr_hkid");
                object.updateUser.usr_other_id_no = rs.getString("usr_other_id_no");
                object.updateUser.usr_other_id_type = rs.getString("usr_other_id_type");
                object.updateUser.usr_tel_1 = rs.getString("usr_tel_1");
                object.updateUser.usr_tel_2 = rs.getString("usr_tel_2");
                object.updateUser.usr_fax_1 = rs.getString("usr_fax_1");
                object.updateUser.usr_country_bil = rs.getString("usr_country_bil");
                object.updateUser.usr_postal_code_bil = rs.getString("usr_postal_code_bil");
                object.updateUser.usr_state_bil = rs.getString("usr_state_bil");
//                object.updateUser.usr_city_bil = rs.getString("usr_city_bil");
                object.updateUser.usr_address_bil = rs.getString("usr_address_bil");
//                object.updateUser.usr_occupation_bil = rs.getString("usr_occupation_bil");
//                object.updateUser.usr_income_level = rs.getString("usr_income_level");
//                object.updateUser.usr_edu_role = rs.getString("usr_edu_role");
//                object.updateUser.usr_edu_level = rs.getString("usr_edu_level");
//                object.updateUser.usr_school_bil = rs.getString("usr_school_bil");
                object.updateUser.usr_class = rs.getString("usr_class");
                object.updateUser.usr_class_number = rs.getString("usr_class_number");
                object.updateUser.usr_signup_date = rs.getTimestamp("usr_signup_date");
                object.updateUser.usr_last_login_date = rs.getTimestamp("usr_last_login_date");
//                object.updateUser.usr_special_date_1 = rs.getTimestamp("usr_special_date_1");
                object.updateUser.usr_status = rs.getString("usr_status");
                object.updateUser.usr_upd_date = rs.getTimestamp("usr_upd_date");
                object.updateUser.usr_extra_1 = rs.getString("usr_extra_1");
                object.updateUser.usr_extra_2 = rs.getString("usr_extra_2");
                object.updateUser.usr_extra_3 = rs.getString("usr_extra_3");
                object.updateUser.usr_extra_4 = rs.getString("usr_extra_4");
                object.updateUser.usr_extra_5 = rs.getString("usr_extra_5");
                object.updateUser.usr_extra_6 = rs.getString("usr_extra_6");
                object.updateUser.usr_extra_7 = rs.getString("usr_extra_7");
                object.updateUser.usr_extra_8 = rs.getString("usr_extra_8");
                object.updateUser.usr_extra_9 = rs.getString("usr_extra_9");
                object.updateUser.usr_extra_10 = rs.getString("usr_extra_10");
                object.updateUser.usr_cost_center = rs.getString("usr_cost_center");
                
                v.addElement(object);
            }
        } finally {
            if(stmt!=null) stmt.close();
        }
        return v;
    }

    /**
    Delete object and all of its versions
    @param con Connection to database
    @param lastUpdTimestamp object last update time for concurrence control
    @return number of row(s) deleted in kmNode. return 0 if cannot pass concurrence control
    */
    public int del(Connection con, Timestamp lastUpdTimestamp) throws SQLException {
        
        //get XLock on underly record in KmObject
        if(!this.dbObject.getXLock(con, lastUpdTimestamp)) {
            return 0;
        }
        
        //get Attachment id
        Vector vAttId = new Vector();
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(SQL_GET_OAT_ATT_ID);
            stmt.setLong(1, this.dbObject.obj_bob_nod_id);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                vAttId.addElement(new Long(rs.getLong("oat_att_id")));
            }
        } finally {
            if(stmt!=null) stmt.close();
        }
        String sql_att_id_list = null;
        if(vAttId.size() > 0) {
            sql_att_id_list = cwUtils.vector2list(vAttId);
        }
        
        //get Parent Folder Id List
        Vector vParentFolderId = getParentWorkFolderId(con);
        vParentFolderId.addAll(getParentFolderId(con));
          
        //del ObjectAttachment
        PreparedStatement stmt2 = null;
        if(sql_att_id_list != null) {
            try {
                stmt2 = con.prepareStatement(SQL_DEL_OAT);
                stmt2.setLong(1, this.dbObject.obj_bob_nod_id);
                stmt2.executeUpdate();
            } finally {
                if(stmt2!=null) stmt2.close();
            }
        }
        
        //del Attachment
        PreparedStatement stmt3 = null;
        if(sql_att_id_list != null) {
            try {
                stmt3 = con.prepareStatement(SQL_DEL_ATT + sql_att_id_list);
                stmt3.executeUpdate();                
            } finally {
                if(stmt3!=null) stmt3.close();
            }
        }

        //del kmObjectTemplate
        PreparedStatement stmt4 = null;
        try {
            stmt4 = con.prepareStatement(SQL_DEL_OJT);
            stmt4.setLong(1, this.dbObject.obj_bob_nod_id);
            stmt4.executeUpdate();
        } finally {
            if(stmt4!=null) stmt4.close();
        }
        
        //del ObjectHistory
        PreparedStatement stmt5 = null;
        try {
            stmt5 = con.prepareStatement(SQL_DEL_OJH);
            stmt5.setLong(1, this.dbObject.obj_bob_nod_id);
            stmt5.executeUpdate();
        } finally {
            if(stmt5!=null) stmt5.close();
        }

        //del kmLink
        DbKmLink.delTarget(con, this.dbObject.obj_bob_nod_id);
        
        //del kmObject
        PreparedStatement stmt7 = null;
        try {
            stmt7 = con.prepareStatement(SQL_DEL_OBJ2);
            stmt7.setLong(1, this.dbObject.obj_bob_nod_id);
            stmt7.executeUpdate();
        } finally {
            if(stmt7!=null) stmt7.close();
        }
        
         // remove base object
        DbKmBaseObject dbbo = new DbKmBaseObject();
        dbbo.bob_nod_id = this.dbObject.obj_bob_nod_id;
        dbbo.del(con);

        //del kmNode
        PreparedStatement stmt8 = null;
        int row = 0;
        try {
            stmt8 = con.prepareStatement(SQL_DEL_NOD2);
            stmt8.setLong(1, this.dbObject.obj_bob_nod_id);
            row = stmt8.executeUpdate();
        } finally {
            if(stmt8!=null) stmt8.close();
        }
        
        //update folder object count
        updateFolderObjCount(con, -1, vParentFolderId);
        
        return row;
    }

     /**
    Delete object and all of its versions
    @param con Connection to database
    @param lastUpdTimestamp object last update time for concurrence control
    @return number of row(s) deleted in kmNode. return 0 if cannot pass concurrence control
    */
    public int markDel(Connection con, Timestamp lastUpdTimestamp, String delete_usr_id) throws SQLException {
        
        //get XLock on underly record in KmObject
        /*
        if(!this.dbObject.getXLock(con, lastUpdTimestamp)) {
            return 0;
        }*/
        
        //get Attachment id
        Vector vAttId = new Vector();
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(SQL_GET_OAT_ATT_ID);
            stmt.setLong(1, this.dbObject.obj_bob_nod_id);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                vAttId.addElement(new Long(rs.getLong("oat_att_id")));
            }
        } finally {
            if(stmt!=null) stmt.close();
        }
        String sql_att_id_list = null;
        if(vAttId.size() > 0) {
            sql_att_id_list = cwUtils.vector2list(vAttId);
        }
        
        //get Parent Folder Id List
        Vector vParentFolderId = getParentWorkFolderId(con);
        vParentFolderId.addAll(getParentFolderId(con));
        
        //del ObjectAttachment
        /*
        PreparedStatement stmt2 = null;
        if(sql_att_id_list != null) {
            try {
                stmt2 = con.prepareStatement(SQL_DEL_OAT);
                stmt2.setLong(1, this.dbObject.obj_bob_nod_id);
                stmt2.executeUpdate();
            } finally {
                if(stmt2!=null) stmt2.close();
            }
        }*/
        
        //del Attachment
        /*
        PreparedStatement stmt3 = null;
        if(sql_att_id_list != null) {
            try {
                stmt3 = con.prepareStatement(SQL_DEL_ATT + sql_att_id_list);
                stmt3.executeUpdate();                
            } finally {
                if(stmt3!=null) stmt3.close();
            }
        }

        //del kmObjectTemplate
        PreparedStatement stmt4 = null;
        try {
            stmt4 = con.prepareStatement(SQL_DEL_OJT);
            stmt4.setLong(1, this.dbObject.obj_bob_nod_id);
            stmt4.executeUpdate();
        } finally {
            if(stmt4!=null) stmt4.close();
        }
        
        //del ObjectHistory
        PreparedStatement stmt5 = null;
        try {
            stmt5 = con.prepareStatement(SQL_DEL_OJH);
            stmt5.setLong(1, this.dbObject.obj_bob_nod_id);
            stmt5.executeUpdate();
        } finally {
            if(stmt5!=null) stmt5.close();
        }
        */
        //del kmLink
        DbKmLink.delTarget(con, this.dbObject.obj_bob_nod_id);
        
        //del kmObject
        /*
        PreparedStatement stmt7 = null;
        try {
            stmt7 = con.prepareStatement(SQL_DEL_OBJ2);
            stmt7.setLong(1, this.dbObject.obj_bob_nod_id);
            stmt7.executeUpdate();
        } finally {
            if(stmt7!=null) stmt7.close();
        }*/
        
       // remove base object
        CommonLog.debug("delete base obj: " + this.dbObject.obj_bob_nod_id);
        DbKmBaseObject dbbo = new DbKmBaseObject();
        dbbo.bob_nod_id = this.dbObject.obj_bob_nod_id;
        dbbo.bob_delete_usr_id = delete_usr_id;
        dbbo.bob_delete_timestamp = cwSQL.getTime(con);
        dbbo.markDel(con);

        //del kmNode
        /*
        PreparedStatement stmt8 = null;
        int row = 0;
        try {
            stmt8 = con.prepareStatement(SQL_DEL_NOD2);
            stmt8.setLong(1, this.dbObject.obj_bob_nod_id);
            row = stmt8.executeUpdate();
        } finally {
            if(stmt8!=null) stmt8.close();
        }
        */
        //update folder object count
        CommonLog.debug("vParentFolderId: " + vParentFolderId);
        
        updateFolderObjCount(con, -1, vParentFolderId);
        
        return 0;
    }


    /**
    Get the last published version from history
    @param con Connection to database
    @return version code of the last published version in History.
            return NULL of no such version is found
    */
    public String getLastPublishedVersionFromHist(Connection con) throws SQLException {
        
        String version = null;
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(SQL_GET_LAST_PUB_OJH);
            stmt.setBoolean(1, true);
            stmt.setLong(2, this.dbObject.obj_bob_nod_id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                version = rs.getString("ojh_version");
            }
        } finally {
            if(stmt!=null) stmt.close();
        }
        return version;
    }

}