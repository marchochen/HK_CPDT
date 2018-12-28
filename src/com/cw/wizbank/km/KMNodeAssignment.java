package com.cw.wizbank.km;

import java.sql.*;
import java.util.*;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.*;
import com.cw.wizbank.db.*;
import com.cw.wizbank.db.view.*;

public class KMNodeAssignment{
    
    public void addToMyWorkplace(Connection con, loginProfile prof, DbKmNodeAssignment dbNodeAss)
        throws SQLException {
            
            dbNodeAss.nam_type = DbKmNodeAssignment.NODE_ASSIGNMENT_TYPE_SELFADD;
            dbNodeAss.nam_ent_id = prof.usr_ent_id;
            dbNodeAss.nam_create_usr_id = prof.usr_id;
            this.insNodeAssignment(con, dbNodeAss);
            return;
            
        }

    public void removeFromMyWorkplace(Connection con, loginProfile prof, DbKmNodeAssignment dbNodeAss)
        throws SQLException {
            
            dbNodeAss.nam_type = DbKmNodeAssignment.NODE_ASSIGNMENT_TYPE_SELFADD;
            dbNodeAss.nam_ent_id = prof.usr_ent_id;    
            dbNodeAss.del(con);
            return;
            
        }
        
        
    public void assignWorkplace(Connection con, loginProfile prof, DbKmNodeAssignment dbNodeAss, long[] a_ent_id)
        throws SQLException {
            
            dbNodeAss.nam_type = DbKmNodeAssignment.NODE_ASSIGNMENT_TYPE_ASSIGN;
            if( a_ent_id == null || a_ent_id.length == 0 ) {
                dbNodeAss.delAll(con);
            } else {
                dbNodeAss.delNotExist(con, a_ent_id);
                dbNodeAss.nam_create_usr_id = prof.usr_id;
                dbNodeAss.nam_create_timestamp = cwSQL.getTime(con);
                for(int i=0; i<a_ent_id.length; i++){
                    dbNodeAss.nam_ent_id = a_ent_id[i];
                    this.insNodeAssignment(con, dbNodeAss);
                }
            }
            return;
            
        }
        
    public void insNodeAssignment(Connection con, DbKmNodeAssignment dbNodeAss)
        throws SQLException {
            
            if( !dbNodeAss.isExisted(con) )
                dbNodeAss.ins(con);
            
            return;
        }
        
        
        

        
        
        
        
    public String getFolderAssignedWorkplaceAsXML(Connection con, long nod_id) throws SQLException, cwSysMessage {

        DbKmFolder folder = new DbKmFolder();
        folder.fld_nod_id = nod_id;
        folder.get(con);
        
        String ancestor_xml = ViewKmFolderManager.getFolderAncestorAsXML(con, folder.fld_nod_id);

        StringBuffer xml = new StringBuffer(1024);
        xml.append("<node id=\"").append(folder.fld_nod_id).append("\" type=\"")
           .append(folder.fld_type).append("\" obj_cnt=\"").append(folder.fld_obj_cnt)
           .append("\" parent_nod_id=\"").append(folder.nod_parent_nod_id)
           .append("\">");
        xml.append(ancestor_xml);
        xml.append("<title>").append(cwUtils.esc4XML(folder.fld_title)).append("</title>");
        xml.append("<desc>").append(cwUtils.esc4XML(folder.fld_desc)).append("</desc>");
        xml.append("<create usr_id=\"").append(folder.nod_create_usr_id)
           .append("\" timestamp=\"").append(folder.nod_create_timestamp).append("\"/>");
        xml.append("<update usr_id=\"").append(folder.fld_update_usr_id)
           .append("\" timestamp=\"").append(folder.fld_update_timestamp).append("\"/>");
        
        xml.append("<assigned_entity_list>");
        Vector v_view_node_ass = ViewKmNodeAssignment.getNodeAssignedEntity(con, nod_id);
        ViewKmNodeAssignment viewAssNode = null;
        for(int i=0; i<v_view_node_ass.size(); i++){
            viewAssNode = (ViewKmNodeAssignment)v_view_node_ass.elementAt(i);
            xml.append("<entity id=\"").append(viewAssNode.nam_ent_id)
               .append("\" name=\"").append(cwUtils.esc4XML(viewAssNode.nam_display_bil))
               .append("\"/>");
        }
        xml.append("</assigned_entity_list>");
        xml.append("</node>");
        
        return xml.toString();
    }
            
    
    public String getObjAssignedWorkplaceAsXML(Connection con, long nod_id) 
        throws SQLException, cwSysMessage, cwException {
            
            String version = ViewKmObject.getLatestVersion(con, nod_id);
            if(version == null) {
                throw new cwSysMessage("GEN005", "Object node id: " + nod_id + ", version: latest");
            }

            //get object details
            ViewKmObject object = new ViewKmObject();
            object.getAll(con, nod_id, version);
            long parent_nod_id = object.dbObject.nod_parent_nod_id;
        
            //get ancestor node list
            String ancestorXML = (parent_nod_id > 0) ?
                ViewKmFolderManager.getVirtualObjectAncestorAsXML(con, parent_nod_id) :
                new String(); 
        
            StringBuffer xml = new StringBuffer(1024);
            xml.append("<node ");
            xml.append("id=\"").append(object.dbObject.obj_bob_nod_id).append("\"");
            xml.append(" type=\"").append(DbKmNode.NODE_TYPE_OBJECT).append("\">");
            xml.append("<version>").append(object.dbObject.obj_version).append("</version>")
               .append("<title>").append(cwUtils.esc4XML(object.dbObject.obj_title)).append("</title>")
               .append("<publish_ind>").append(object.dbObject.obj_publish_ind).append("</publish_ind>")
               .append("<latest_ind>").append(object.dbObject.obj_latest_ind).append("</latest_ind>")
               .append("<type>").append(object.dbObject.obj_type).append("</type>")
               .append("<desc>").append(cwUtils.esc4XML(object.dbObject.obj_desc)).append("</desc>")
               .append("<status>").append(object.dbObject.obj_status).append("</status>")
               .append("<keywords>").append(cwUtils.esc4XML(object.dbObject.obj_keywords)).append("</keywords>")
               .append("<comment>").append(cwUtils.esc4XML(object.dbObject.obj_comment)).append("</comment>")
               .append("<author>").append(cwUtils.esc4XML(object.dbObject.obj_author)).append("</author>")
               .append("<last_update_timestamp>").append(object.dbObject.obj_update_timestamp).append("</last_update_timestamp>")
               .append("<last_update_usr_id>").append(object.dbObject.obj_update_usr_id).append("</last_update_usr_id>")
               .append("<last_update_user_display_bil>").append(object.dbObject.obj_update_usr_display_bil).append("</last_update_user_display_bil>");
            
            xml.append("<assigned_entity_list>");
            Vector v_view_node_ass = ViewKmNodeAssignment.getNodeSelfAddEntity(con, nod_id);
            ViewKmNodeAssignment viewAssNode = null;
            for(int i=0; i<v_view_node_ass.size(); i++){
                viewAssNode = (ViewKmNodeAssignment)v_view_node_ass.elementAt(i);
                xml.append("<entity id=\"").append(viewAssNode.nam_ent_id)
                   .append("\" name=\"").append(cwUtils.esc4XML(viewAssNode.nam_display_bil))
                   .append("\"/>");
            }
            xml.append("</assigned_entity_list>");                  
            xml.append("</node>");
            return xml.toString();
        
        }
 
    
    
    
    public String getMyWorkplace(Connection con, loginProfile prof)
        throws SQLException {
            
            ResultSet rs = ViewKmNodeAssignment.getMyWorkplace(con, prof.usr_ent_id, prof.usrGroupsList());
            StringBuffer workXml = new StringBuffer();
            workXml.append("<work_node_list>");
            
            
            StringBuffer domainXml = new StringBuffer();
            domainXml.append("<domain_node_list>");
            
            String xml = "<workplace_node_list>";
            Long nam_nod_id;
            Vector v_nam_nod_id = new Vector();
            while(rs.next()){
                StringBuffer buf = new StringBuffer();
                nam_nod_id = new Long(rs.getLong("nam_nod_id"));
                if( v_nam_nod_id.indexOf(nam_nod_id) == -1 ) {
                    buf.append("<node ")
                    .append(" id=\"").append(nam_nod_id).append("\" ")
                    .append(" source=\"").append(rs.getString("nam_type")).append("\" ")
                    .append(">")
                    .append( ViewKmFolderManager.getFolderAncestorAsXML(con, nam_nod_id.longValue()) )
                    .append("<title>").append(cwUtils.esc4XML(rs.getString("fld_title"))).append("</title>")
                    .append("<desc>").append(cwUtils.esc4XML(rs.getString("fld_desc"))).append("</desc>")
                    .append("</node>");
                    if( rs.getString("fld_type").equalsIgnoreCase(DbKmFolder.FOLDER_TYPE_DOMAIN) ) {
                        domainXml.append(buf);
                    } else if( rs.getString("fld_type").equalsIgnoreCase(DbKmFolder.FOLDER_TYPE_WORK) ) {
                        workXml.append(buf);
                    }
                    v_nam_nod_id.addElement(nam_nod_id);
                }
            }
            
            workXml.append("</work_node_list>");
            domainXml.append("</domain_node_list>");
            
            xml += domainXml.toString() + workXml.toString() 
                + "</workplace_node_list>";
            rs.close();
            return xml;
        }
 
    
    
    public String getUserAsXMLByUsrId(Connection con, String usr_id, loginProfile prof)
        throws SQLException, cwException {

            dbRegUser dbUsr = new dbRegUser();
            dbUsr.usr_id = usr_id;
            String xml;
            try{
                dbUsr.get(con, usr_id);
            }catch(qdbException e){
                throw new cwException(e.getMessage());
            }            
            return getUserAsXML(con, dbUsr, prof);
        }
        
        
    public String getUserAsXMLByEntId(Connection con, long usr_ent_id, loginProfile prof)
        throws SQLException, cwException {

            dbRegUser dbUsr = new dbRegUser();
            dbUsr.usr_ent_id = usr_ent_id;
            String xml;
            try{
                dbUsr.get(con);
            }catch(qdbException e){
                throw new cwException(e.getMessage());
            }
            return getUserAsXML(con, dbUsr, prof);
        }
    
    public String getUserAsXML(Connection con, dbRegUser dbUsr, loginProfile prof)
        throws SQLException, cwException {
            
            StringBuffer xml = new StringBuffer();
            try{
                xml.append(dbUsr.getUserXML(con, prof));
            }catch(qdbException e){
                throw new cwException(e.getMessage());
            }
            
            xml.append(KMFolderManager.getUserOwnedDomain(con, dbUsr.usr_ent_id));
            return xml.toString();
        }
        
}