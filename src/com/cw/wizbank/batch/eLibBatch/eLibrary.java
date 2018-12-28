package com.cw.wizbank.batch.eLibBatch;

import java.io.*;
import java.net.*;
import java.util.*;
import java.sql.*;

import com.cw.wizbank.util.*;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.message.*;
import com.cw.wizbank.db.view.ViewKmLibraryObject;
import com.cwn.wizbank.utils.CommonLog;

public class eLibrary{

    Connection con = null;
    eLibraryMailer eLibMailer = null;
    

    protected final static String DELIMITER     =   "~";
    protected final static String NOT_OVERDUED  =   "NOT_OVERDUED";
    protected final static String OVERDUED      =   "OVERDUED";
    
    
    protected void sendMessage() throws SQLException, cwException  {
        
        try{
            //Get the overdue period to be checked
            int dateAfter = Integer.parseInt(eLibraryApp.cwIni.getValue("NUM_OF_DAY_BEFORE_DUE"));
            Timestamp periodStart = utils.getDateAfter(eLibraryApp.lastActionTimestamp, dateAfter) ;
            Timestamp periodEnd = utils.getDateEnd( utils.getDateAfter(cwSQL.getTime(con), dateAfter) );


            if( eLibraryApp.debugMode ) {
            	CommonLog.debug("Book will be due on " + periodStart + " to " + periodEnd);
            }

            //Get the overdued and not overovered borrow object within the perioed checked
            Hashtable h_lob_id = getLibraryObjectBorrowByDuedate(con, eLibraryApp.siteId, periodStart, periodEnd);
            Vector v_lob_id = (Vector)h_lob_id.get(NOT_OVERDUED);
            Vector v_overdued_lob_id = (Vector)h_lob_id.get(OVERDUED);



            eLibraryApp.logWriter.println("Library Object Borrow id (Not Overdued)= " + v_lob_id);
            eLibraryApp.logWriter.println("Total = " + v_lob_id.size());
            eLibraryApp.logWriter.println("Library Object Borrow id (Overdued)= " + v_overdued_lob_id);
            eLibraryApp.logWriter.println("Total = " + v_overdued_lob_id.size());
            eLibraryApp.logWriter.flush();

            if( eLibraryApp.debugMode ) {
            	CommonLog.debug("Library Object Borrow id (Not Overdued)= " + v_lob_id);
            	CommonLog.debug("Total = " + v_lob_id.size());
            	CommonLog.debug("Library Object Borrow id (Overdued)= " + v_overdued_lob_id);
            	CommonLog.debug("Total = " + v_overdued_lob_id.size());
            }
            
            
            if( v_lob_id != null && !v_lob_id.isEmpty() ) {
                if( eLibraryApp.debugMode ) {
                	CommonLog.debug("Send not overdued mail ...");
                }
                sendNotOverduedMessage(v_lob_id);
            }
            if( v_overdued_lob_id != null && !v_overdued_lob_id.isEmpty() ) {
                if( eLibraryApp.debugMode ) {
                	CommonLog.debug("Send overdued mail ...");
                }
                sendOverduedMessage(v_overdued_lob_id);
            }
            
        }catch(Exception e){
            eLibraryApp.logWriter.println("Failed to send mail");
            e.printStackTrace(eLibraryApp.logWriter);
            CommonLog.error(e.getMessage(),e);
            eLibraryApp.logWriter.flush();
        }
        return;    
    }
            
    
    public Hashtable getLibraryObjectBorrowByDuedate(Connection con, long site_id, Timestamp periodStart, Timestamp periodEnd)
        throws SQLException {
            Hashtable hashtable = new Hashtable();
            Vector v_lob_id = new Vector();
            Vector v_overdued_lob_id = new Vector();
            ResultSet rs = ViewKmLibraryObject.getLibraryObjectBorrowByDuedate(con, site_id, periodStart, periodEnd);
            Timestamp cur_time = cwSQL.getTime(con);
            while(rs.next()){
                if( rs.getTimestamp("lob_due_timestamp").getTime() <= cur_time.getTime() ){
                    v_overdued_lob_id.addElement(new Long(rs.getLong("lob_id")));
                } else {
                    v_lob_id.addElement(new Long(rs.getLong("lob_id")));
                }
            }
            rs.close();
            hashtable.put(OVERDUED, v_overdued_lob_id);
            hashtable.put(NOT_OVERDUED, v_lob_id);
            return hashtable;
        }

    
    public void sendNotOverduedMessage(Vector v_lob_id) throws Exception {
        String xslFilename = eLibraryApp.cwIni.getValue("XSL_FOLDER")
                            + File.separator
                            + eLibraryApp.cwIni.getValue("NOT_OVERDUED_MAIL_TEMPLATE_XSL");
        
        dbRegUser dbSender = new dbRegUser();
        dbSender.usr_ent_id = Long.parseLong( eLibraryApp.cwIni.getValue("MAIL_SENDER_ENT_ID") );
        try{
            dbSender.get(con);
        }catch(qdbException e){
            eLibraryApp.logWriter.println("Failed to get Sender info., ent id = " + dbSender.usr_ent_id);
            e.printStackTrace(eLibraryApp.logWriter);
            CommonLog.error(e.getMessage(),e);
            eLibraryApp.logWriter.flush();
            throw new Exception(e.getMessage());
        }
        dbRegUser dbRecip = null;
        String metaXML = getMetaXML();
        String contentXml = null;
        Hashtable contentHash = getMailContentHash(v_lob_id);
        for(int i=0; i<v_lob_id.size(); i++){
            StringBuffer xml = new StringBuffer();
            contentXml = (String)((Vector)contentHash.get((Long)v_lob_id.elementAt(i))).elementAt(0);
            dbRecip = (dbRegUser)((Vector)contentHash.get((Long)v_lob_id.elementAt(i))).elementAt(1);
            xml.append("<content>")
               .append(metaXML)
               .append(contentXml)
               .append("</content>");
               
            if( eLibraryApp.debugMode ) {
            	CommonLog.debug("Mail to " + dbRecip.usr_display_bil + " ( " + dbRecip.usr_ent_id + " ) : lob_id = " + v_lob_id.elementAt(i) );
            }
            
            eLibraryApp.logWriter.println("Mail to " + dbRecip.usr_display_bil + " ( " + dbRecip.usr_ent_id + " ) : lob_id = " + v_lob_id.elementAt(i) );
            eLibraryApp.logWriter.flush();
            
            eLibMailer.setSender(dbSender.usr_display_bil, dbSender.usr_email);
            if( eLibraryApp.defaultMailAccount != null )
                eLibMailer.setRecipient(dbRecip.usr_display_bil, eLibraryApp.defaultMailAccount);
            else
                eLibMailer.setRecipient(dbRecip.usr_display_bil, dbRecip.usr_email);
                
            eLibMailer.setSubject(new String((eLibraryApp.cwIni.getValue("NOT_OVERDUED_MAIL_SUBJECT")).getBytes(eLibraryApp.enc)));// + " : " + (String)((Vector)contentHash.get((Long)v_lob_id.elementAt(i))).elementAt(2));
            if( eLibraryApp.sendXML )
                eLibMailer.setContent(xml.toString());
            else
                eLibMailer.setContent(cwXSL.processFromFile(xml.toString(), xslFilename));
            eLibMailer.send();
            
        }
        return;
    }
    
    public void sendOverduedMessage(Vector v_lob_id) throws Exception {
        String xslFilename = eLibraryApp.cwIni.getValue("XSL_FOLDER")
                            + File.separator
                            + eLibraryApp.cwIni.getValue("OVERDUED_MAIL_TEMPLATE_XSL");
        
        dbRegUser dbSender = new dbRegUser();
        dbSender.usr_ent_id = Long.parseLong( eLibraryApp.cwIni.getValue("MAIL_SENDER_ENT_ID") );
        try{
            dbSender.get(con);
        }catch(qdbException e){
            eLibraryApp.logWriter.println("Failed to get Sender info., ent id = " + dbSender.usr_ent_id);
            e.printStackTrace(eLibraryApp.logWriter);
            CommonLog.error(e.getMessage(),e);
            eLibraryApp.logWriter.flush();
            throw new Exception(e.getMessage());
        }
        dbRegUser dbRecip = null;
        String metaXML = getMetaXML();
        String contentXml = null;
        Hashtable contentHash = getMailContentHash(v_lob_id);
        for(int i=0; i<v_lob_id.size(); i++){
            StringBuffer xml = new StringBuffer();
            contentXml = (String)((Vector)contentHash.get((Long)v_lob_id.elementAt(i))).elementAt(0);
            dbRecip = (dbRegUser)((Vector)contentHash.get((Long)v_lob_id.elementAt(i))).elementAt(1);
            xml.append("<content>")
               .append(metaXML)
               .append(contentXml)
               .append("</content>");
               
            if( eLibraryApp.debugMode ) {
            	CommonLog.debug("Mail to " + dbRecip.usr_display_bil + " ( " + dbRecip.usr_ent_id + " ) : lob_id = " + v_lob_id.elementAt(i) );
            }
            eLibraryApp.logWriter.println("Mail to " + dbRecip.usr_display_bil + " ( " + dbRecip.usr_ent_id + " ) : lob_id = " + v_lob_id.elementAt(i) );
            eLibMailer.setSender(dbSender.usr_display_bil, dbSender.usr_email);
            if( eLibraryApp.defaultMailAccount != null )
                eLibMailer.setRecipient(dbRecip.usr_display_bil, eLibraryApp.defaultMailAccount);
            else
                eLibMailer.setRecipient(dbRecip.usr_display_bil, dbRecip.usr_email);
                
            eLibMailer.setSubject(new String((eLibraryApp.cwIni.getValue("OVERDUED_MAIL_SUBJECT")).getBytes(eLibraryApp.enc)) );// + " : " + (String)((Vector)contentHash.get((Long)v_lob_id.elementAt(i))).elementAt(2));
            if( eLibraryApp.sendXML )
                eLibMailer.setContent(xml.toString());
            else
                eLibMailer.setContent(cwXSL.processFromFile(xml.toString(), xslFilename));
            eLibMailer.send();
        }
        return;
    }

    /**
    * Get the mail content and recipient info.
    * return a hashtable, lob_id as key and vector as value
    * vector : xml, dbRegUser, obj_title, call_num
    */
    public Hashtable getMailContentHash(Vector v_lob_id)
        throws Exception {

            Hashtable hash = new Hashtable();
            MessageCryptography msgCrypto = new MessageCryptography((new Long(eLibraryApp.cwIni.getValue("DES_KEY"))).longValue());
            ResultSet rs = ViewKmLibraryObject.getBorrowedObjectById(con, v_lob_id);
            while(rs.next()){
                StringBuffer xml = new StringBuffer();
                
                xml.append("<recipient>")
                    .append("<user ")
                    .append(" ent_id=\"").append(rs.getLong("usr_ent_id")).append("\" ")
                    .append(" id=\"").append(rs.getString("usr_id")).append("\" ")
                    .append(" ste_usr_id=\"").append(rs.getString("usr_ste_usr_id")).append("\" ")
                    .append(" pwd=\"").append(URLEncoder.encode(msgCrypto.encrypt(rs.getString("usr_pwd")))).append("\" ")
                    .append(" display_bil=\"").append(cwUtils.esc4XML(rs.getString("usr_display_bil"))).append("\" ")
                    .append(" email_1=\"").append(rs.getString("usr_email")).append("\" ")
                    .append(" email_2=\"").append(rs.getString("usr_email_2")).append("\" ")
                    .append("/>")
                    .append("</recipient>");

                xml.append("<object ")
                   .append(" lob_id=\"").append(rs.getLong("lob_id")).append("\" ")
                   .append(" nod_id=\"").append(rs.getLong("lob_lio_bob_nod_id")).append("\" ")
                   .append(">")
                   .append("<obj_title>").append(cwUtils.esc4XML(rs.getString("obj_title"))).append("</obj_title>")
                   .append("<obj_type>").append(cwUtils.esc4XML(rs.getString("obj_type"))).append("</obj_type>")
                   .append("<loc_title>").append(cwUtils.esc4XML(rs.getString("loc_copy"))).append("</loc_title>")
                   .append("<due_timestamp>").append((rs.getTimestamp("lob_due_timestamp")).toString()).append("</due_timestamp>")
                   .append("<renew_no>").append(cwUtils.esc4XML(rs.getString("lob_renew_no"))).append("</renew_no>")
                   .append("<call_num>").append(rs.getString("bob_code")).append("</call_num>")
                   .append("</object>");
                
                dbRegUser dbRecip = new dbRegUser();
                dbRecip.usr_ent_id = rs.getLong("usr_ent_id");
                dbRecip.usr_id = rs.getString("usr_id");
                dbRecip.usr_ste_usr_id = rs.getString("usr_ste_usr_id");
                dbRecip.usr_display_bil = rs.getString("usr_display_bil");
                dbRecip.usr_email = rs.getString("usr_email");
                dbRecip.usr_email_2 = rs.getString("usr_email_2");
                
                Vector vec = new Vector();
                vec.addElement(xml.toString());
                vec.addElement(dbRecip);
                
                vec.addElement(rs.getString("obj_title"));
                vec.addElement(rs.getString("bob_code"));
                hash.put(new Long(rs.getLong("lob_id")), vec);
            }
            rs.close();
            return hash;

        }

        
    public String getMetaXML(){
        StringBuffer xml = new StringBuffer();
        xml.append("<meta>");
        if(eLibraryApp.cwIni.getValue("APP_HOST") != null)
            xml.append("<app_host>").append(eLibraryApp.cwIni.getValue("APP_HOST")).append("</app_host>");
        if(eLibraryApp.cwIni.getValue("APP_PORT") != null)
            xml.append("<app_port>").append(eLibraryApp.cwIni.getValue("APP_PORT")).append("</app_port>");
        if(eLibraryApp.cwIni.getValue("STYLE") != null)
            xml.append("<style>").append(eLibraryApp.cwIni.getValue("STYLE")).append("</style>");
        if(eLibraryApp.cwIni.getValue("ENCODING") != null)
            xml.append("<encoding>").append(eLibraryApp.cwIni.getValue("ENCODING")).append("</encoding>");
        if(eLibraryApp.cwIni.getValue("RECIPIENT_LOGIN_ROLE_EXT_ID") != null)
            xml.append("<login_role>").append(eLibraryApp.cwIni.getValue("RECIPIENT_LOGIN_ROLE_EXT_ID")).append("</login_role>");
        if(eLibraryApp.cwIni.getValue("SITEID") != null)
            xml.append("<site_id>").append(eLibraryApp.cwIni.getValue("SITEID")).append("</site_id>");
        if(eLibraryApp.cwIni.getValue("SINGLE_SIGNON_URL") != null)
            xml.append("<single_signon>").append(eLibraryApp.cwIni.getValue("SINGLE_SIGNON_URL")).append("</single_signon>");
        xml.append("</meta>");
        return xml.toString();
    }
        
}
