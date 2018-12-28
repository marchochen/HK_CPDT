package com.cw.wizbank.message;


import javax.servlet.ServletRequest;

import java.sql.Timestamp;
import java.util.*;
import com.cw.wizbank.db.DbMgMessage;
import com.cw.wizbank.*;
import com.cw.wizbank.util.*;

public class MessageReqParam extends ReqParam {

    
    public DbMgMessage dbMsg;
    public Vector params;
    
    public long msg_id;
    public String msg_type;      
    public String msg_subtype;
    public String ent_id_str;
    public String cc_ent_id_str;
    public long itm_id;
    public long id;
    public String id_type;
    public Timestamp send_date;
    public Timestamp rem_send_date;
    public String url_cmd;
    public String url_redirect;
    public int rem_status;
    public int ji_status;
    public String reminder_subject;
    public String usr_id;


    public MessageReqParam(ServletRequest inReq, String clientEnc_, String encoding_) 
        throws cwException {        
            this.req = inReq;
            this.clientEnc = clientEnc_; 
            this.encoding = encoding_;        
            common();
            
            
            //Print submited param
            Enumeration enumeration = req.getParameterNames();
            while( enumeration.hasMoreElements() ) {
                String name = (String) enumeration.nextElement();
                String[] values = req.getParameterValues(name);
                if( values != null )
                    for(int i=0; i<values.length; i++){
//                        System.out.println(name + " (" + i + "):" + values[i]);
                    }
            }            
            
            return;
    }

    public void send_msg() 
        throws cwException {
        
            msg_id = getLongParameter("msg_id");

            return;

        }
    
    public void ins_ji() 
        throws cwException {
                        
            params = new Vector();
            Vector paramsName = new Vector();
            Vector paramsType = new Vector();
            Vector paramsValue = new Vector();
            Vector remParamsValue = new Vector();
            
            dbMsg = new DbMgMessage();
            
            dbMsg.msg_subject = unicode(getStringParameter("msg_subject"));
            //dbMsg.msg_subject = "Joining Instruction";
            reminder_subject = unicode(getStringParameter("reminder_subject"));
            
            dbMsg.msg_addition_note = unicode(getStringParameter("msg_body"));
            
            dbMsg.msg_id = getLongParameter("msg_id");

            dbMsg.msg_bcc_sys_ind = getBooleanParameter("msg_bcc_sys_ind");

            msg_type = "JI";//getStringParameter(MESSAGE_TYPE);

            msg_subtype = getStringParameter("msg_subtype");

            itm_id  = getLongParameter("itm_id");

            usr_id = getStringParameter("usr_id");

            int index = 1;
            String var = getStringParameter("param" + index + "_name");
            while( var != null && var.length() > 0  ) {
                paramsName.addElement(getStringParameter("param" + index + "_name"));
                paramsType.addElement(getStringParameter("param" + index + "_type"));
                paramsValue.addElement(getStringParameter("param" + index + "_value"));
                remParamsValue.addElement(getStringParameter("rem_param" + index + "_value"));
                index++;
                var = getStringParameter("param" + index + "_name");
            }
            params.addElement(paramsName);
            params.addElement(paramsType);
            params.addElement(paramsValue);
            params.addElement(remParamsValue);
            
            return;
        
        }

       
    public void ins_recip()
        throws cwException {
            
            itm_id = getLongParameter("itm_id");
            
            ent_id_str = getStringParameter("ent_ids");
                        
            cc_ent_id_str = getStringParameter("cc_ent_ids");
            
            send_date = getTimestampParameter("msg_datetime");
            
            rem_send_date = getTimestampParameter("rem_send_date");
            
            msg_type = getStringParameter("msg_type");
            
            rem_status = getIntParameter("rem_status");
            
            ji_status = getIntParameter("ji_status");                        
            
            msg_subtype = getStringParameter("msg_subtype");
            
            
            return;
            
        }

    public void preview()
        throws cwException {
            
            dbMsg = new DbMgMessage();
            
            itm_id = getLongParameter("itm_id");
            
            msg_type = getStringParameter("msg_type");
            
            msg_subtype = getStringParameter("msg_subtype");
            
            dbMsg.msg_subject = unicode(getStringParameter("msg_subject"));
            
            dbMsg.msg_addition_note = unicode(getStringParameter("msg_body"));
            
            id = getLongParameter("id");
            
            id_type = getStringParameter("id_type");
            
            url_cmd = getStringParameter("url_cmd");      //  tna_xml : get detial of the TNA for the message
            
            url_redirect = getStringParameter("url_redirect");
            
            
            return;
            
            
        }

    public void del_recip()
        throws cwException {
            
            itm_id = getLongParameter("itm_id");
            
            ent_id_str = getStringParameter("ent_ids");
            
            return;
        
        }
    
    
    
    
    public void ins_link_notify() 
        throws cwException {
            
            params = new Vector();
            Vector paramsName = new Vector();
            Vector paramsType = new Vector();
            Vector paramsValue = new Vector();
            
            dbMsg = new DbMgMessage();

            dbMsg.msg_subject = unicode(getStringParameter("msg_subject"));
            
            if( getStringParameter("msg_body") != null )
                dbMsg.msg_addition_note = unicode(getStringParameter("msg_body"));
            
            dbMsg.msg_target_datetime = getTimestampParameter("msg_datetime");
            
            dbMsg.msg_bcc_sys_ind = getBooleanParameter("msg_bcc_sys_ind");
            
            ent_id_str = getStringParameter("ent_ids");
            
            msg_type = getStringParameter("msg_type");
            
            msg_subtype = getStringParameter("msg_subtype");
            
            cc_ent_id_str = getStringParameter("cc_ent_ids");
            
            usr_id = getStringParameter("usr_id");


            int index = 1;
            String var = getStringParameter("param" + index + "_name");
            while( var != null && var.length() > 0  ) {
                paramsName.addElement(getStringParameter("param" + index + "_name"));
                paramsType.addElement(getStringParameter("param" + index + "_type"));
                paramsValue.addElement(getStringParameter("param" + index + "_value"));
                index++;
                var = getStringParameter("param" + index + "_name");
            }

            
            params.addElement(paramsName);
            params.addElement(paramsType);
            params.addElement(paramsValue);
            
            return;
        }

    
    public void ins_notify() 
        throws cwException {

            params = new Vector();
            Vector paramsName = new Vector();
            Vector paramsType = new Vector();
            Vector paramsValue = new Vector();            

            dbMsg = new DbMgMessage();
            
            String value = getStringParameter("msg_subject");
            if (value != null && value.length() > 0) {
                dbMsg.msg_subject = unicode(value);
            }
            
            value = getStringParameter("msg_body");
            if(value != null && value.length() > 0) {
                dbMsg.msg_addition_note = unicode(value);
            }
            
            dbMsg.msg_target_datetime = getTimestampParameter("msg_datetime");                        
            
            dbMsg.msg_bcc_sys_ind = getBooleanParameter("msg_bcc_sys_ind");
            
            ent_id_str = getStringParameter("ent_ids");
            
            msg_type = getStringParameter("msg_type");
            
            msg_subtype = getStringParameter("msg_subtype");
            
            cc_ent_id_str = getStringParameter("cc_ent_ids");

            itm_id  = getLongParameter("itm_id");
            
            usr_id  = getStringParameter("usr_id");
            
            
            int index = 1;
            String var = getStringParameter("param" + index + "_name");
            while( var != null && var.length() > 0  ) {
                paramsName.addElement(getStringParameter("param" + index + "_name"));
                paramsType.addElement(getStringParameter("param" + index + "_type"));
                paramsValue.addElement(getStringParameter("param" + index + "_value"));
                index++;
                var = getStringParameter("param" + index + "_name");
            }
            
            

            params.addElement(paramsName);
            params.addElement(paramsType);
            params.addElement(paramsValue);

            
            return;
        }
    
    public void getItmMsgStatus()
        throws cwException {
            
            itm_id  = getLongParameter("itm_id");
            
            ent_id_str = getStringParameter("ent_ids");
            
            return;
        }
    


}