package com.cw.wizbank.qdb;

import java.sql.*;
import java.util.*;

import com.cw.wizbank.util.*;
/*
import com.cw.wizbank.qdb.dbUtils;
*/
import com.cw.wizbank.message.*;
/*
import com.cw.wizbank.db.DbMgMessage;
*/
import com.cw.wizbank.ae.aeXMessage;
import com.cw.wizbank.accesscontrol.*;
import com.cwn.wizbank.utils.CommonLog;

import java.net.URLEncoder;

public class qdbXMessage {
    
    public final static String COMMA            =   ",";
    public final static String STATIC           =   "STATIC";
    public final static String DYNAMIC          =   "DYNAMIC";
    
    
    
    // recipient entity id
    public long ent_id;
    
    //sender user id
    public String sender_id;
    
    public String msg_type;
    public long id;
    public String id_type;
    public String reply_to;


    public String getReplyToXml(Connection con, String mail_account)
        throws cwException {

            boolean isNum = true;
            long entId = 0;
            try{
                entId = Long.parseLong(reply_to);
            }catch( NumberFormatException e ) {
                isNum = false;
            }
            if( isNum ) {
                dbRegUser dbUser = new dbRegUser();
                dbUser.usr_ent_id = entId;
                try{
                    dbUser.get(con);
                }catch( qdbException e ) {
                    throw new cwException("Failed to get Recipient detail, id=" + ent_id + " : " + e);
                }
                StringBuffer xml = new StringBuffer();
                String userMail = dbUser.usr_email;
                if(mail_account.equalsIgnoreCase("NOTES"))
                    userMail = dbUser.usr_email_2;
                xml.append("<reply_to>").append(cwUtils.NEWL)
                   .append("<entity ")
                   .append(" ent_id=\"").append(entId).append("\" ")
                   .append(" display_name=\"").append(dbUser.usr_display_bil).append("\" ")
                   .append(" email=\"").append(dbUtils.esc4XML(userMail)).append("\" ")
                   .append(" />").append(cwUtils.NEWL);
                xml.append("</reply_to>").append(cwUtils.NEWL);
                return xml.toString();
            } else {
                return new String();
            }
                        
        }
   
    public String getRecipientXml(Connection con, long ent_id, String mail_account, long DES_KEY)
        throws cwException {
            this.ent_id = ent_id;
            return getRecipientXml(con, mail_account, DES_KEY);
        }
        
    public String getRecipientXml(Connection con, String mail_account, long DES_KEY) 
        throws cwException {
                        

            if(ent_id == 0)
                return new String();
            
            StringBuffer xml = new StringBuffer();
            dbRegUser dbRecip = new dbRegUser();
            dbRecip.usr_ent_id = ent_id;

            try{
                dbRecip.get(con);
            }catch( qdbException e ) {
                throw new cwException("Failed to get Recipient detail, id=" + ent_id + " : " + e);
            }

            String userMail = dbRecip.usr_email;
            if(mail_account.equalsIgnoreCase("NOTES"))
                userMail = dbRecip.usr_email_2;
    
            xml.append("<recipient>").append(cwUtils.NEWL)
               .append("<entity ent_id=\"").append(dbRecip.usr_ent_id).append("\" ")
               .append(" usr_id=\"").append(dbUtils.esc4XML(dbRecip.usr_id)).append("\" ")
               .append(" display_name=\"").append(dbUtils.esc4XML(dbRecip.usr_display_bil)).append("\" ")
               .append(" email=\"").append(dbUtils.esc4XML(userMail)).append("\" ")
               .append(" ste_usr_id=\"").append(dbUtils.esc4XML(dbRecip.usr_ste_usr_id)).append("\" ");
            
            if( dbRecip.usr_pwd != null && dbRecip.usr_pwd.length() > 0 ) {
                //RSA_instance r = new RSA_instance();
                //RSA encoder = new RSA(dbRecip.usr_pwd, r, true);
                //xml.append(" usr_pwd=\"").append((encoder.encodeString()).trim()).append("\" ");                
            	CommonLog.debug("DES_KEY = " + DES_KEY);
                MessageCryptography msgCrypto = new MessageCryptography(DES_KEY);
                xml.append(" usr_pwd=\"").append(URLEncoder.encode(msgCrypto.encrypt(dbRecip.usr_pwd))).append("\" ");
            } else {
                xml.append(" usr_pwd=\"\" ");
            }
            xml.append("/>");
            xml.append("</recipient>").append(cwUtils.NEWL);            
            
            return xml.toString();
            
        }
        
        
    public String getSenderXml(Connection con, String sender_id, String mail_account)
        throws cwException {
            this.sender_id = sender_id;
            return getSenderXml(con, mail_account);            
        }

    public String getSenderXml(Connection con, String mail_account) 
        throws cwException {

            if(sender_id == null || sender_id.length() == 0)
                return "";

            StringBuffer xml = new StringBuffer();
            dbRegUser dbSender = new dbRegUser();
            dbSender.usr_id = sender_id;

            try{                
                dbSender.usr_ent_id = dbSender.getEntId(con);
                dbSender.get(con);
            }catch( qdbException e ) {
                throw new cwException("Failed to get Sender detail, id = " + sender_id + " : " + e);
            }

            String userMail = dbSender.usr_email;
            if(mail_account.equalsIgnoreCase("NOTES"))
                userMail = dbSender.usr_email_2;

            xml.append("<sender display_name=\"").append(dbUtils.esc4XML(dbSender.usr_display_bil)).append("\" ")
               .append(" usr_id=\"").append(dbUtils.esc4XML(dbSender.usr_id)).append("\" ")
               .append(" ste_usr_id=\"").append(dbUtils.esc4XML(dbSender.usr_ste_usr_id)).append("\" ")
               .append(" email=\"").append(dbUtils.esc4XML(userMail)).append("\" />").append(cwUtils.NEWL);
    
            return xml.toString();
    
        }
        
    public String getReourceXml(Connection con)
        throws cwException {
            
            StringBuffer xml = new StringBuffer();
            dbResource dbRes = new dbResource();
            dbRes.res_id = id;
            
            try{
                dbRes.get(con);
            }catch( Exception e) {
                throw new cwException("Failed to get the Resource detail, id = " + id + " : " + e);
            }
            
            xml.append("<resource id=\"").append(dbRes.res_id).append("\" ");
            xml.append(" type=\"").append(dbRes.res_type).append("\" ");
            xml.append(" subtype=\"").append(dbRes.res_subtype).append("\">").append(cwUtils.NEWL);
            xml.append("<title>").append(dbRes.res_title).append("</title>").append(cwUtils.NEWL);
            xml.append("<desc>").append(dbRes.res_desc).append("</desc>").append(cwUtils.NEWL);
            xml.append("</resource>").append(cwUtils.NEWL);
                                    
            return xml.toString();
            
        }
        


    public String getEmails(Connection con, long[] ent_ids, long[] cc_ent_ids, String mail_account)
        throws SQLException, cwException {
         
            StringBuffer xml = new StringBuffer();
            StringBuffer entStr = new StringBuffer().append("(0");
            for(int i=0; i<ent_ids.length; i++)
                entStr.append(COMMA).append(ent_ids[i]);
            entStr.append(")");

            StringBuffer ccEntStr = new StringBuffer().append("(0");
            for(int i=0; i<cc_ent_ids.length; i++)
                ccEntStr.append(COMMA).append(cc_ent_ids[i]);
            ccEntStr.append(")");                        

            String dbXMessage_GET_USR_EMAIL = " SELECT usr_ent_id, usr_ste_usr_id, usr_display_bil, usr_email USERMAIL, usr_id FROM RegUser WHERE usr_ent_id IN ";
            
            if(mail_account.equalsIgnoreCase("NOTES"))
                dbXMessage_GET_USR_EMAIL = " SELECT usr_ent_id, usr_ste_usr_id, usr_display_bil, usr_email_2 USERMAIL, usr_id FROM RegUser WHERE usr_ent_id IN ";
            
            xml.append("<recipient>").append(cwUtils.NEWL);

            PreparedStatement stmt = con.prepareStatement(dbXMessage_GET_USR_EMAIL + entStr.toString());
//System.out.println("SQL + " + dbXMessage_GET_USR_EMAIL + entStr.toString());            
            ResultSet rs = stmt.executeQuery();
            while( rs.next() ) {
                xml.append("<entity display_name=\"").append(dbUtils.esc4XML(rs.getString("usr_display_bil"))).append("\" ")
                   .append(" ent_id=\"").append(rs.getLong("usr_ent_id")).append("\" ")
                   .append(" usr_id=\"").append(rs.getString("usr_id")).append("\" ")                   
                   .append(" ste_usr_id=\"").append(dbUtils.esc4XML(rs.getString("usr_ste_usr_id"))).append("\" ")
                   .append(" email=\"").append(dbUtils.esc4XML(rs.getString("USERMAIL"))).append("\" />").append(cwUtils.NEWL);
            }
            rs.close();
            xml.append("</recipient>").append(cwUtils.NEWL);    

            xml.append("<carboncopy>").append(cwUtils.NEWL);    
            stmt = con.prepareStatement(dbXMessage_GET_USR_EMAIL + ccEntStr.toString());
            rs = stmt.executeQuery();
            while( rs.next() ) {
                xml.append("<entity display_name=\"").append(dbUtils.esc4XML(rs.getString("usr_display_bil"))).append("\" ")
                   .append(" ent_id=\"").append(rs.getLong("usr_ent_id")).append("\" ")
                   //.append(" usr_ste_usr_id=\"").append(dbUtils.esc4XML(rs.getString("usr_ste_usr_id"))).append("\" ")
                   .append(" email=\"").append(dbUtils.esc4XML(rs.getString("USERMAIL"))).append("\" />").append(cwUtils.NEWL);
            }
            xml.append("</carboncopy>").append(cwUtils.NEWL);
 
            stmt.close();
            return xml.toString();
            
        }
        
    public void addParam(Vector vec, String paramName, String paramType, String paramValue) {
        
        Vector paramsName = (Vector)vec.elementAt(0);
        Vector paramsType = (Vector)vec.elementAt(1);
        Vector paramsValue = (Vector)vec.elementAt(2);
        paramsName.addElement(paramName);
        paramsType.addElement(paramType);
        paramsValue.addElement(paramValue);
        vec.removeAllElements();
        vec.addElement(paramsName);
        vec.addElement(paramsType);
        vec.addElement(paramsValue);
        return;
        
    }


    public Vector notifyParams(String[] name, String[] type, String[] value) {
                
        Vector params = new Vector();
        Vector paramsName = new Vector();
        Vector paramsType = new Vector();
        Vector paramsValue = new Vector();
                
        for(int i=0; i<name.length; i++) {
            paramsName.addElement(name[i]);
            paramsType.addElement(type[i]);
            paramsValue.addElement(value[i]);
        }

        params.addElement(paramsName);
        params.addElement(paramsType);
        params.addElement(paramsValue);

        return params;
        
    }



    public String getSysUserEntId(Connection con, loginProfile prof)
        throws SQLException, cwException {
            
            PreparedStatement stmt = con.prepareStatement(
                                     " SELECT ste_default_sys_ent_id FROM acSite WHERE ste_ent_id = ? ");
            
            stmt.setLong(1, prof.root_ent_id);
            ResultSet rs = stmt.executeQuery();
            long entId = 0;
            if( rs.next() )
                entId = rs.getLong("ste_default_sys_ent_id");
            stmt.close();
            
            dbRegUser dbusr = new dbRegUser();
            dbusr.usr_ent_id = entId;
            try{
                dbusr.get(con);
            }catch(qdbException e) {
                throw new cwException(e.getMessage());
            }
            return dbusr.usr_id;
            
        }

    /**
    Insert a account suspension message into messaging table and send the mail to the specified user
    @param ent_id recipient entity id
    @param usr_ent_id suspended account entity id
    */
    public void sendAccSuspendNotify(Connection con, long[] ent_id, long usr_ent_id, String skin_root)
        throws SQLException, cwException {

            Hashtable params = new Hashtable();
            params.put("template_type", "USER_ACCOUNT_SUSPENSION_NOTIFICATION");
            params.put("template_subtype", "HTML");
            params.put("usr_ent_id", (new Long(usr_ent_id)).toString());
            
            
            dbRegUser dbUsr = new dbRegUser();
            dbUsr.usr_ent_id = usr_ent_id;
            CommonLog.debug("usr_ent_id = " + usr_ent_id);
            try{
                dbUsr.get(con);
            }catch(qdbException e){
                throw new cwException(e.getMessage());
            }
            Vector subjectToken = new Vector();
            String de_usr_display_bil = dbUsr.usr_display_bil;
            
            subjectToken.addElement(de_usr_display_bil);
            params.put("subject_token", subjectToken);
            
            loginProfile prof = new loginProfile();
            prof.usr_id = dbUsr.usr_id;
            prof.root_ent_id = dbUsr.usr_ste_ent_id;
            prof.skin_root = skin_root;
            
            aeXMessage aeXmsg = new aeXMessage();
            try{
                aeXmsg.insNotify(con, prof,
                                acSite.getSysUsrId(con, dbUsr.usr_ste_ent_id), ent_id,
                                null, null,
                                cwSQL.getTime(con), params, null);
            }catch(cwSysMessage e){
                throw new cwException(e.getMessage());
            }
            
            
            long[] self_ent_id = {usr_ent_id};
            params.put("template_type", "USER_ACCOUNT_SUSPENSION_SELF_NOTIFICATION");
            try{
                aeXmsg.insNotify(con, prof,
                                acSite.getSysUsrId(con, dbUsr.usr_ste_ent_id), self_ent_id,
                                null, null,
                                cwSQL.getTime(con), params, null);
            }catch(cwSysMessage e){
                throw new cwException(e.getMessage());
            }
            
            return;
        }
}