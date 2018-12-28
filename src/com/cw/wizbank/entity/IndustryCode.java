package com.cw.wizbank.entity;

import java.sql.*;
import java.util.Vector;

import com.cw.wizbank.qdb.dbEntity;
import com.cw.wizbank.qdb.dbEntityRelation;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.db.DbIndustryCode;
import com.cw.wizbank.db.view.ViewEntityRelation;
import com.cwn.wizbank.utils.CommonLog;

/**
Business Layer of Industry Code
long ent_id, Timestamp ent_upd_date are inherit from dbEntity
*/
public class IndustryCode extends dbEntity {
    
    public String name;
    
    public long rootEntId;
    
    /**
    user entity id who has this industry code
    */
    public long userEntId;
    
    public long parentEntId;
    
    private DbIndustryCode dbIndCode;
    
    private ViewEntityRelation view;
    
    
    /**
    get an XML describes an Industry Code
    pre-define variables:<BR>
    <ul>
    <li>ent_id
    </ul>
    */
    public String asXML(Connection con) throws SQLException {
        
        getIndustryCode(con);
        return asXML();
    }
    
    /**
    get an XML describes an Industry Code
    pre-define variables:<BR>
    <ul>
    <li>ent_id
    <li>name
    <li>ent_upd_date
    </ul>
    */
    public String asXML() throws SQLException {
        
        StringBuffer xmlBuf = new StringBuffer(256);
        
        xmlBuf.append("<entity type=\"").append(ENT_TYPE_INDUSTRY_CODE).append("\"");
        xmlBuf.append(" id=\"").append(this.ent_id).append("\"");
        xmlBuf.append(" display_bil=\"").append(dbUtils.esc4XML(this.name)).append("\"");
        xmlBuf.append(" last_upd_timestamp=\"").append(this.ent_upd_date).append("\"/>");
        return xmlBuf.toString();
    }
    
    /**
    get the industry code (together with entity) into this object<BR>
    pre-define variables:<BR>
    <ul>
    <li>ent_id
    </ul>
    */
    public void getIndustryCode(Connection con)  throws SQLException {
        try {
            super.get(con);

            if(dbIndCode == null) {
                dbIndCode = new DbIndustryCode();
            }
            dbIndCode.idc_ent_id = this.ent_id;
            dbIndCode.get(con);
            this.name = dbIndCode.idc_display_bil;
            this.rootEntId = dbIndCode.idc_ent_id_root;
        }
        catch(qdbException e) {
            throw new SQLException(e.getMessage());
        }
    }

    /**
    get a xml of the child industry code given a parent industry code ent id. 
    if parentEntId is 0, it will find out the child indusrty code of the organization root indusrty code<BR>
    pre-define variables:<BR>
    <ul>
    <li>ent_id
    <li>parentEntId
    <li>rootEntId
    </ul>
    */
    public String getMemberIndustryCodeAsXML(Connection con) 
        throws SQLException {
    
        if(view == null) {
            view = new ViewEntityRelation();
        }
        if(dbIndCode == null) {
            dbIndCode = new DbIndustryCode();
        }
            
        StringBuffer xmlBuf = new StringBuffer(512);
                        
        if(this.ent_id == EntityReqParam.LONG_PARAMETER_NOT_FOUND) {
                    
            //get the root industry code of the organization by default
            dbIndCode.idc_ent_id_root = rootEntId;
            dbIndCode.getRootIndustryCode(con);
        }
        else {
            dbIndCode.idc_ent_id = this.ent_id;
            dbIndCode.get(con);
        }
                
        xmlBuf.append(getMemberIndustryCodeAsXML(con, dbIndCode));
        return xmlBuf.toString();
    }
    
    /**
    get a xml of the child industry code given a parent industry code object. <BR>
    pre-define variables:<BR>
    <ul>
    <li>parentEntId
    <li>rootEntId
    </ul>
    */
    public String getMemberIndustryCodeAsXML(Connection con, DbIndustryCode dbidc) 
        throws SQLException {
        try {
            if(view == null) {
                view = new ViewEntityRelation();
            }

            view.rootEntId = rootEntId;
            view.groupEntId = dbidc.idc_ent_id;
            Vector v_members = view.getMemberIndustryCode(con);
            dbEntity ent = new dbEntity();
            DbIndustryCode member;
            StringBuffer xmlBuf = new StringBuffer(512);
            
            //get the industry code's ent_upd_date
            ent.ent_id = dbidc.idc_ent_id;
            ent.get(con);
                        
            xmlBuf.append("<entity type=\"").append(ENT_TYPE_INDUSTRY_CODE).append("\"");
            xmlBuf.append(" id=\"").append(dbidc.idc_ent_id).append("\"");
            xmlBuf.append(" parent_ent_id=\"").append(this.parentEntId).append("\"");
            xmlBuf.append(" display_bil=\"").append(dbUtils.esc4XML(dbidc.idc_display_bil)).append("\"").append(cwUtils.NEWL);
            xmlBuf.append(" last_update_timestamp=\"").append(ent.ent_upd_date).append("\">").append(cwUtils.NEWL);
            
            for(int i=0; i<v_members.size(); i++) {
                member = (DbIndustryCode) v_members.elementAt(i);
                ent.ent_id = member.idc_ent_id;
                ent.get(con);
                
                xmlBuf.append("<entity type=\"").append(ENT_TYPE_INDUSTRY_CODE).append("\"");
                xmlBuf.append(" id=\"").append(member.idc_ent_id).append("\"");
                xmlBuf.append(" display_bil=\"").append(dbUtils.esc4XML(member.idc_display_bil)).append("\"").append(cwUtils.NEWL);
                xmlBuf.append(" last_update_timestamp=\"").append(ent.ent_upd_date).append("\">").append(cwUtils.NEWL);
                xmlBuf.append("</entity>");
            }
            xmlBuf.append("</entity>").append(cwUtils.NEWL);
            return xmlBuf.toString();
        }
        catch(qdbException e) {
            throw new SQLException(e.getMessage());
        }
    }

    /**
    get a flat structure xml of user industry code with an indicator showing the entity relation type
    pre-define variables:<BR>
    <ul>
    <li>userEntId
    </ul>
    */
    public String getUserIndustryCodeAsXML(Connection con) throws SQLException {

        if(view == null) {
            view = new ViewEntityRelation();
        }
        StringBuffer xmlBuf = new StringBuffer(1024);
        
        view.memberEntId = userEntId;
        
        Vector v_vienr = view.getParentIndustryCode(con);
        ViewEntityRelation vienr;
        
        xmlBuf.append("<assigned_attributes type=\"").append(ENT_TYPE_INDUSTRY_CODE).append("\">");
        for(int i=0; i< v_vienr.size(); i++) {
            
            vienr = (ViewEntityRelation) v_vienr.elementAt(i);
            xmlBuf.append("<entity type=\"").append(ENT_TYPE_INDUSTRY_CODE).append("\"");
            xmlBuf.append(" id=\"").append(vienr.groupEntId).append("\"");
            xmlBuf.append(" display_bil=\"").append(dbUtils.esc4XML(vienr.groupName)).append("\"");
            xmlBuf.append(" relation_type=\"").append(vienr.relationType).append("\"/>");
        }
        xmlBuf.append("</assigned_attributes>");
        return xmlBuf.toString();
    }

    /**
    get a tree of industry code starting from the input DbIndustryCode and search for "depth" level
    if the user (userEntId) has that industry code, a flag in XML will turn true<BR>
    pre-define variable:<BR>
    <ul>
    <li>userEntId
    <li>rootEntId
    </ul>
    */
    public String getUserIndustryCodeAsXML(Connection con, long beginEntId, int depth) 
        throws SQLException {
                
            if(view == null) {
                view = new ViewEntityRelation();
            }
            if(dbIndCode == null) {
                dbIndCode = new DbIndustryCode();
            }
            
            StringBuffer xmlBuf = new StringBuffer(512);
            xmlBuf.append(getUserAsXML(con));
            
/*            
            //get the user information
            dbRegUser user = new dbRegUser();
            user.usr_ent_id = userEntId;
            user.get(con);
            xmlBuf.append("<entity type=\"").append(ENT_TYPE_USER).append("\"");
            xmlBuf.append(" ent_id=\"").append(user.usr_ent_id).append("\"");
            xmlBuf.append(" id=\"").append(user.usr_ste_usr_id).append("\"");
            xmlBuf.append(" display_bil=\"").append(user.usr_display_bil).append("\">");
*/            
            if(depth > 0) {
                if(beginEntId == EntityReqParam.LONG_PARAMETER_NOT_FOUND) {
                    //get the root industry code of the organization by default
                    dbIndCode.idc_ent_id_root = rootEntId;
                    dbIndCode.getRootIndustryCode(con);
                }
                else {
                	CommonLog.info("non-root");
                    dbIndCode.idc_ent_id = beginEntId;
                    dbIndCode.get(con);
                }
                
                xmlBuf.append(getUserIndustryCodeTreeAsXML(con, dbIndCode, 10));
            }
            xmlBuf.append("</entity>");
            return xmlBuf.toString();
    }

    /**
    get a tree of industry code starting from the input DbIndustryCode and search for "depth" level
    if the user (userEntId) has that industry code, a flag in XML will turn true<BR>
    pre-define variable:<BR>
    <ul>
    <li>userEntId
    </ul>
    */
    private String getUserIndustryCodeTreeAsXML(Connection con, DbIndustryCode dbidc, int depth)
        throws SQLException {
        
        StringBuffer xmlBuf = new StringBuffer(512);
        boolean picked;
        Vector v_members;
        DbIndustryCode member;
        ViewEntityRelation myView;
        
        if(depth > 0 && dbidc != null) {
            myView = new ViewEntityRelation();
            myView.groupEntId = dbidc.idc_ent_id;
            myView.memberEntId = this.userEntId;
            picked = myView.isRelationExist(con);
            v_members = myView.getMemberIndustryCode(con);
            depth = depth - 1;
            
            xmlBuf.append("<entity type=\"").append(ENT_TYPE_INDUSTRY_CODE).append("\"");
            xmlBuf.append(" id=\"").append(dbidc.idc_ent_id).append("\"");
            xmlBuf.append(" display_bil=\"").append(dbUtils.esc4XML(dbidc.idc_display_bil)).append("\"");
            xmlBuf.append(" picked=\"").append(picked).append("\">").append(cwUtils.NEWL);
            
            for(int i=0; i<v_members.size(); i++) {
                member = (DbIndustryCode) v_members.elementAt(i);
                xmlBuf.append(getUserIndustryCodeTreeAsXML(con, member, depth));
            }
            xmlBuf.append("</entity>").append(cwUtils.NEWL);
        }
        return xmlBuf.toString();
    }
    
    /**
    assign industry codes to a user. 1st remove all EntityRelation records. 2nd insert new EntityRelation records<BR>
    pre-define variable:<BR>
    <ul>
    <li>userEntId
    </ul>
    */
    public void assignUserIndustryCode(Connection con, long[] idc_ent_ids, String relationType, String usr_id) 
        throws SQLException {
        
        //clear all user industry code records
        if(view == null) {
            view = new ViewEntityRelation();
        }
        view.memberEntId = this.userEntId;
        view.relationType = relationType;
        view.clearUserIndustryCode(con, usr_id);
        
        //insert EntityRelation records
        for(int i=0; i<idc_ent_ids.length; i++) {
            view.groupEntId = idc_ent_ids[i];
            view.insEntityRelation(con, usr_id);
        }
        return;
    }
    
    /**
    add an industry code<BR>
    pre-define variables:
    <ul>
    <li>name
    <li>rootEntId
    <li>parentEntId
    </ul>
    */
    public void addIndustryCode(Connection con, String usr_id) throws SQLException, cwSysMessage {
        
        //insert into Entity
        try {
            this.ent_type = ENT_TYPE_INDUSTRY_CODE;        
            super.ins(con);
        }
        catch (qdbException e) {
            throw new SQLException(e.getMessage());
        }
        catch(qdbErrMessage ee) {
            throw new cwSysMessage(ee.getId());
        }
        
        if(dbIndCode == null) {
            dbIndCode = new DbIndustryCode();
        }
        
        //insert into IndustryCode
        dbIndCode.idc_ent_id = this.ent_id;
        dbIndCode.idc_display_bil = this.name;
        dbIndCode.idc_ent_id_root = this.rootEntId;
        dbIndCode.ins(con);
        
        //insert into EntityRelation
        if(view == null) {
            view = new ViewEntityRelation();
        }
        view.groupEntId = this.parentEntId;
        view.memberEntId = this.ent_id;
        view.relationType = dbEntityRelation.ERN_TYPE_IDC_PARENT_IDC;
        view.insEntityRelation(con, usr_id);
        
        return;
    }
    
    /**
    delete an Industry Code<BR>
    pre-define variables:
    <ul>
    <li>ent_id
    <li>ent_upd_date
    </ul>
    */
    public void delIndustryCode(Connection con, String ent_delete_usr_id) throws SQLException, cwSysMessage {

        try {
            //check update timestamp
            super.checkTimeStamp(con);

            if(view == null) {
                view = new ViewEntityRelation();
            }
            if(dbIndCode == null) {
                dbIndCode = new DbIndustryCode();
            }
            
            view.groupEntId = this.ent_id;
            if(view.hasMember(con)) {
                throw new cwSysMessage("IDC001");
            }
            //harveyTime
            Timestamp deleteTime = cwSQL.getTime(con);
            //delete from IndustryCode
            dbIndCode.idc_ent_id = this.ent_id;
            dbIndCode.del(con, ent_delete_usr_id);
            
            //delete from Entity
            super.del(con, ent_delete_usr_id, deleteTime);
            dbEntityRelation dbEr = new dbEntityRelation();
            dbEr.ern_child_ent_id = this.ent_id;
            dbEr.ern_type = dbEntityRelation.ERN_TYPE_IDC_PARENT_IDC;
            dbEr.delAsChild(con, ent_delete_usr_id, deleteTime);
            
            return;
        }
        catch(qdbException e) {
            throw new SQLException(e.getMessage());
        }
        catch(qdbErrMessage ee) {
            throw new cwSysMessage(ee.getId());
        }
    }

    /**
    update an industry code<BR>
    pre-define variables:
    <ul>
    <li>ent_id
    <li>name
    </ul>
    */
    public void updIndustryCode(Connection con, String usr_id) throws SQLException, cwSysMessage {

        try {
            //check update timestamp
            super.checkTimeStamp(con);
            
            if(dbIndCode == null) {
                dbIndCode = new DbIndustryCode();
            }
            
            //update Industry Code
            dbIndCode.idc_ent_id = this.ent_id;
            dbIndCode.idc_display_bil = this.name;
            dbIndCode.upd(con, usr_id);
            
            //update Entity
            super.upd(con);
            
            return;
        }
        catch(qdbException e) {
            throw new SQLException(e.getMessage());
        }
        catch(qdbErrMessage ee) {
            throw new cwSysMessage(ee.getId());
        }
    }   

    /**
    get a tree of industry code starting from the input DbIndustryCode and search for "depth" level
    <BR>
    pre-define variable:<BR>
    <ul>
    <li>rootEntId
    </ul>
    */
    public String getIndustryCodeTreeAsXML(Connection con, long beginEntId, int depth) 
        throws SQLException {
                
            if(view == null) {
                view = new ViewEntityRelation();
            }
            if(dbIndCode == null) {
                dbIndCode = new DbIndustryCode();
            }
            
            StringBuffer xmlBuf = new StringBuffer(512);
            xmlBuf.append("<attribute_tree type=\"").append(ENT_TYPE_INDUSTRY_CODE).append("\">");
            
            if(depth > 0) {
                if(beginEntId == EntityReqParam.LONG_PARAMETER_NOT_FOUND) {
                    //get the root industry code of the organization by default
                    dbIndCode.idc_ent_id_root = rootEntId;
                    dbIndCode.getRootIndustryCode(con);
                }
                else {
                    dbIndCode.idc_ent_id = beginEntId;
                    dbIndCode.get(con);
                }
                
                xmlBuf.append(getIndustryCodeTreeAsXML(con, dbIndCode, 10));
            }
            xmlBuf.append("</attribute_tree>");
            
            return xmlBuf.toString();
    }

    /**
    get a tree of industry code starting from the input DbIndustryCode and search for "depth" level
    if the user (userEntId) has that industry code, a flag in XML will turn true<BR>
    pre-define variable:<BR>
    */
    private String getIndustryCodeTreeAsXML(Connection con, DbIndustryCode dbidc, int depth)
        throws SQLException {
        
        StringBuffer xmlBuf = new StringBuffer(512);
        boolean picked;
        Vector v_members;
        DbIndustryCode member;
        ViewEntityRelation myView;
        
        if(depth > 0 && dbidc != null) {
            myView = new ViewEntityRelation();
            myView.groupEntId = dbidc.idc_ent_id;
            v_members = myView.getMemberIndustryCode(con);
            depth = depth - 1;
            
            xmlBuf.append("<entity type=\"").append(ENT_TYPE_INDUSTRY_CODE).append("\"");
            xmlBuf.append(" id=\"").append(dbidc.idc_ent_id).append("\"");
            xmlBuf.append(" display_bil=\"").append(dbUtils.esc4XML(dbidc.idc_display_bil)).append("\">").append(cwUtils.NEWL);
            
            for(int i=0; i<v_members.size(); i++) {
                member = (DbIndustryCode) v_members.elementAt(i);
                xmlBuf.append(getIndustryCodeTreeAsXML(con, member, depth));
            }
            xmlBuf.append("</entity>").append(cwUtils.NEWL);
        }
        return xmlBuf.toString();
    }
    
    /**
    get a simple user xml contains ent_id, ste_usr_id and display_bil only<BR>
    pre-define variables:<BR>
    <ul>
    <li>userEntId
    </ul>
    */
    public String getUserAsXML(Connection con) throws SQLException {
        dbRegUser user = new dbRegUser();
        user.usr_ent_id = this.userEntId;
        return user.getUserAsXML(con);
    }
    
    /**
    get the root industry code of an organization
    */
    public static IndustryCode getRootIndustryCode(Connection con, long rootEntId) 
        throws SQLException {
                
        //get the root industry code of the organization
        DbIndustryCode dbidc = new DbIndustryCode();
        dbidc.idc_ent_id_root = rootEntId;
        dbidc.getRootIndustryCode(con);
        
        IndustryCode idc = new IndustryCode();
        idc.rootEntId = rootEntId;
        idc.name = dbidc.idc_display_bil;
        idc.ent_id = dbidc.idc_ent_id;
        return idc;
    }

    /**
    get xml of root industry code of an organization
    */
    public static String getRootIndustryCodeAsXML(Connection con, long rootEntId) 
        throws SQLException {
        
        IndustryCode idc = getRootIndustryCode(con, rootEntId);
        StringBuffer xmlBuf = new StringBuffer(1024);
        xmlBuf.append("<root_entity>");
        xmlBuf.append(idc.asXML());
        xmlBuf.append("</root_entity>");
        return xmlBuf.toString();
    }
    
    /*
    *   get EntId by ent_ste_uid and the organisation
    */
    public boolean getBySteUid(Connection con) throws SQLException{
        if(dbIndCode == null) {
            dbIndCode = new DbIndustryCode();
        }
        dbIndCode.idc_ent_id_root = rootEntId;
        boolean result = dbIndCode.getBySteUid(con, ent_ste_uid);
        ent_id = dbIndCode.idc_ent_id;
        name = dbIndCode.idc_display_bil;
        return result;
    }

} 