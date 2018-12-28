package com.cw.wizbank.content;

import java.sql.*;
import java.io.*;
import java.util.Vector;
import java.util.StringTokenizer;
import com.cw.wizbank.util.*;
import com.cw.wizbank.qdb.*;
import com.cw.wizbank.db.*;
//import com.cw.wizbank.db.view.*;

public class Reference {
    public dbModule myDbModule;
    public DbCtReference myDbReference;

    public Reference() {
    }

    public Reference(dbModule inDbModule) {
        myDbModule = inDbModule;
    }
    
    public Reference(DbCtReference inDbReference, dbModule inDbModule) {
        myDbReference = inDbReference;
        myDbModule = inDbModule;
    }
    
    public int insReference(Connection con, loginProfile prof) throws cwException, cwSysMessage, IOException, SQLException{
        try {
            myDbModule.get(con);
            // check the modify permission first
//            myDbModule.checkModifyPermission(con, prof);
            // check the timestamp first
            myDbModule.checkTimeStamp(con);
            
            myDbReference.ref_create_usr_id = prof.usr_id;
            myDbReference.ref_update_usr_id = prof.usr_id;
            myDbReference.ins(con);
            
            // update the resource update timestamp
            myDbModule.updateTimeStamp(con);
            
            return myDbReference.ref_id;
        } catch(qdbException e) {
            throw new cwException(e.toString());
        } catch(qdbErrMessage e) {
            throw new cwSysMessage(e.toString());
        }
    }

    public int updReference(Connection con, loginProfile prof) throws cwException, cwSysMessage, IOException, SQLException{
        try {
            myDbModule.get(con);
            // check the modify permission first
//            myDbModule.checkModifyPermission(con, prof);
            // check the timestamp first
            myDbModule.checkTimeStamp(con);
            
            myDbReference.ref_update_usr_id = prof.usr_id;
            myDbReference.upd(con);
            
            // update the resource update timestamp
            myDbModule.updateTimeStamp(con);
            
            return myDbReference.ref_id;
        } catch(qdbException e) {
            throw new cwException(e.toString());
        } catch(qdbErrMessage e) {
            throw new cwSysMessage(e.toString());
        }
    }

    public int delReference(Connection con, loginProfile prof, String ref_id_list) throws cwException, cwSysMessage, IOException, SQLException{
        DbCtReference myDbReference = null;
        try {
            myDbModule.get(con);
            // check the modify permission first
//            myDbModule.checkModifyPermission(con, prof);
            // check the timestamp first
            myDbModule.checkTimeStamp(con);
            
            StringTokenizer st = new StringTokenizer(ref_id_list, "~");
            while (st.hasMoreTokens()) {
                myDbReference = new DbCtReference();
                myDbReference.ref_id = Integer.parseInt(st.nextToken());                
                myDbReference.delete(con);
            }
            
            // update the resource update timestamp
            myDbModule.updateTimeStamp(con);
            
            return 0;
        } catch(qdbException e) {
            throw new cwException(e.toString());
        } catch(qdbErrMessage e) {
            throw new cwSysMessage(e.toString());
        }
    }
    
    public String getReference(Connection con, loginProfile prof) throws cwException, cwSysMessage, IOException, SQLException{
        try {
            myDbModule.get(con);
            
            String result = "<?xml version=\"1.0\" encoding=\"" + dbUtils.ENC_UTF + "\" standalone=\"no\" ?>" + dbUtils.NEWL;
            result += "<module id=\""+ myDbModule.res_id + "\" language=\"" + myDbModule.res_lan + "\" timestamp=\"" + myDbModule.res_upd_date +"\">" + dbUtils.NEWL;
                
            // author's information
            result += prof.asXML() + dbUtils.NEWL;
                
            result += dbResourcePermission.aclAsXML(con,myDbModule.res_id,prof);
            
            myDbReference.get(con);
                
            result += "<reference " 
                    + "ref_id=\"" + Integer.toString(myDbReference.ref_id) 
                    + "\" ref_res_id=\"" + Integer.toString(myDbReference.ref_res_id);
            result += "\" ref_type=\"";
            if (myDbReference.ref_type != null) {
                result += myDbReference.ref_type;
            }
            result += "\" ref_title=\"" + dbUtils.esc4XML(myDbReference.ref_title);
            result += "\" ref_url=\"";
            if (myDbReference.ref_url != null) {
                result += dbUtils.esc4XML(myDbReference.ref_url);
            }
            result += "\" ref_create_usr_id=\"" + myDbReference.ref_create_usr_id
                    + "\" ref_create_timestamp=\"" + myDbReference.ref_create_timestamp.toString()
                    + "\" ref_update_usr_id=\"" + myDbReference.ref_update_usr_id
                    + "\" ref_update_timestamp=\"" + myDbReference.ref_update_timestamp.toString() + "\">"
                    + dbUtils.NEWL;
                        
            result += "<description>";
            if (myDbReference.ref_description != null) {
                result += dbUtils.esc4XML(myDbReference.ref_description);
            }
            result += "</description>" + dbUtils.NEWL;
            result += "</reference>" + dbUtils.NEWL;

            result += "</module>";
            
            return result;
            
        } catch(qdbException e) {
            throw new cwException(e.toString());
        }
    }
    
    public String getReferenceList(Connection con, loginProfile prof) throws cwException, cwSysMessage, IOException, SQLException{
        Vector vtReferenceList = null;
        DbCtReference myDbReference = null;
        Integer int_ref_id = null;
        try {
            myDbModule.get(con);
            
            String result = "<?xml version=\"1.0\" encoding=\"" + dbUtils.ENC_UTF + "\" standalone=\"no\" ?>" + dbUtils.NEWL;
            result += "<module id=\""+ myDbModule.res_id + "\" language=\"" + myDbModule.res_lan + "\" timestamp=\"" + myDbModule.res_upd_date +"\">" + dbUtils.NEWL;
                
            // author's information
            result += prof.asXML() + dbUtils.NEWL;
                
            result += dbResourcePermission.aclAsXML(con,myDbModule.res_id,prof);
            
            vtReferenceList = DbCtReference.getModuleReferenceList(con, (int)myDbModule.mod_res_id);
            
            result += "<reference_list>" + dbUtils.NEWL;

            for (int i=0; i<vtReferenceList.size(); i++) {                
                myDbReference = (DbCtReference)vtReferenceList.elementAt(i);
                
                result += "<reference " 
                        + "ref_id=\"" + Integer.toString(myDbReference.ref_id) 
                        + "\" ref_res_id=\"" + Integer.toString(myDbReference.ref_res_id);
                result += "\" ref_type=\"";
                if (myDbReference.ref_type != null) {
                    result += myDbReference.ref_type;
                }
                result += "\" ref_title=\"" + dbUtils.esc4XML(myDbReference.ref_title);
                result += "\" ref_url=\"";
                if (myDbReference.ref_url != null) {
                    result += dbUtils.esc4XML(myDbReference.ref_url);
                }
                result += "\" ref_create_usr_id=\"" + myDbReference.ref_create_usr_id
                        + "\" ref_create_timestamp=\"" + myDbReference.ref_create_timestamp.toString()
                        + "\" ref_update_usr_id=\"" + myDbReference.ref_update_usr_id
                        + "\" ref_update_timestamp=\"" + myDbReference.ref_update_timestamp.toString() + "\">"
                        + dbUtils.NEWL;
                        
                result += "<description>";
                if (myDbReference.ref_description != null) {
                    result += dbUtils.esc4XML(myDbReference.ref_description);
                }
                result += "</description>" + dbUtils.NEWL;
                result += "</reference>" + dbUtils.NEWL;
            }

            result += "</reference_list>" + dbUtils.NEWL;
            result += "</module>";
            
            return result;
            
        } catch(qdbException e) {
            throw new cwException(e.toString());
        }
    }
}