package com.cw.wizbank.km.library;

import java.sql.Timestamp;
import javax.servlet.ServletRequest;

import com.cw.wizbank.util.*;
import com.cw.wizbank.km.KMReqParam;

public class KMLibraryReqParam extends KMReqParam {
    
    public static final String DELIMITER = "~"; 
    
    // for reserve/borrow notify
    public long lobId;
    public String senderUsrId;

    // for check-in/out/history/renew/reserve/borrow/reserve
    public String callNum;
    public String copyNum;

    public long callId;
    public long copyId;
    public long usrEntId;

    public long loc_id;

    public long recEntId;
    public long[] bccEntIds;
    public long[] ccEntIds;
    public String mesgSubject;
    public long site_id;
    
    // Library Object Copy
    public String loc_copy;
    public String loc_desc;
    public Timestamp loc_update_timestamp;

    
    //for user inquiry
    public long usr_ent_id;
    
    
    //Generic
    public String sort_by;
    public String order_by;
    public long nod_id;
    public String call_num;
    
    //for learner Searching
    public String obj_title;
    public String obj_author;
    public String words;
    public long[] nod_id_lst;
    public String[] obj_type_list;
    public boolean show_deleted;
    
    public KMLibraryReqParam(ServletRequest inReq, String clientEnc_, String encoding_)
        throws cwException {
/*            this.req = inReq;
            this.clientEnc = clientEnc_;
            this.encoding = encoding_;
            
            Enumeration enumeration = req.getParameterNames();
            while( enumeration.hasMoreElements() ) {
                String name = (String) enumeration.nextElement();
                String[] values = req.getParameterValues(name);
            }*/
        super(inReq, clientEnc_, encoding_);
    }
    
    public void checkin() throws cwException {
        this.callNum = getStringParameter("call_num");
        this.copyNum = getStringParameter("copy_num");
    }

    public void checkout() throws cwException {
        this.callId = getLongParameter("call_id");
        this.copyId = getLongParameter("copy_id");
        this.usrEntId = getLongParameter("usr_ent_id");

        if(getStringParameter("rec_usr_ent_id") != null) {
            this.recEntId = getLongParameter("rec_usr_ent_id");
        } else {
            this.recEntId = -1L;
        }
        this.ccEntIds = getLongArrayParameter("cc_usr_ent_ids", DELIMITER);
        this.bccEntIds = getLongArrayParameter("bcc_usr_ent_ids", DELIMITER);
        this.mesgSubject = unicode( getStringParameter("message_subject") );
        this.senderUsrId = getStringParameter("sender_usr_id");
    }
    public void checkoutNotify() throws cwException {
        this.lobId = getLongParameter("lob_id");
        this.usrEntId = getLongParameter("usr_ent_id");
        this.senderUsrId = getStringParameter("sender_id");
        this.site_id = getLongParameter("site_id");
    }
    
    public void history() throws cwException {
        this.copyId = getLongParameter("loc_id");
        this.usrEntId = getLongParameter("usr_ent_id");
    }
    
    public void renew() throws cwException {
        this.usrEntId = getLongParameter("usr_ent_id");
        this.callId = getLongParameter("call_id");
        this.copyId = getLongParameter("loc_id");
        this.lobId = getLongParameter("lob_id");
    }

    public void reserve() throws cwException {
        this.callId = getLongParameter("call_id");
        this.usrEntId = getLongParameter("usr_ent_id");
        if(getStringParameter("rec_usr_ent_id") != null) {
            this.recEntId = getLongParameter("rec_usr_ent_id");
        } else {
            this.recEntId = -1L;
        }
        this.ccEntIds = getLongArrayParameter("cc_usr_ent_ids", DELIMITER);
        this.bccEntIds = getLongArrayParameter("bcc_usr_ent_ids", DELIMITER);
        this.mesgSubject = unicode( getStringParameter("message_subject") );
    }

    public void reserveCancel() throws cwException {
        this.callId = getLongParameter("call_id");
        this.usrEntId = getLongParameter("usr_ent_id");
    }

    public void reserveNotify() throws cwException {
        this.lobId = getLongParameter("lob_id");
        this.usrEntId = getLongParameter("usr_ent_id");
        this.senderUsrId = getStringParameter("sender_id");
        this.site_id = getLongParameter("site_id");
    }

    public void borrow() throws cwException {
        this.callId = getLongParameter("call_id");
        this.usrEntId = getLongParameter("usr_ent_id");
        if(getStringParameter("rec_usr_ent_id") != null) {
            this.recEntId = getLongParameter("rec_usr_ent_id");
        } else {
            this.recEntId = -1L;
        }
        this.ccEntIds = getLongArrayParameter("cc_usr_ent_ids", DELIMITER);
        this.bccEntIds = getLongArrayParameter("bcc_usr_ent_ids", DELIMITER);
        this.mesgSubject = unicode( getStringParameter("message_subject") );
    }

    public void borrowCancel() throws cwException {
        this.callId = getLongParameter("call_id");
        this.usrEntId = getLongParameter("usr_ent_id");
    }

    public void borrowNotify() throws cwException {
        this.lobId = getLongParameter("lob_id");
        this.usrEntId = getLongParameter("usr_ent_id");
        this.senderUsrId = getStringParameter("sender_id");
        this.site_id = getLongParameter("site_id");
    }

    public void getObject() throws cwException {
        super.getObject();
        
        this.loc_id = getLongParameter("loc_id");   
    }

    public void insObjectCopy() throws cwException {
        this.obj_bob_nod_id = getLongParameter("obj_nod_id"); 
        this.loc_copy = getStringParameter("loc_copy");
        this.loc_desc = getStringParameter("loc_desc");
    }

    public void updObjectCopy() throws cwException {
        this.obj_bob_nod_id = getLongParameter("obj_nod_id"); 
        this.loc_id = getLongParameter("loc_id");
        this.loc_copy = getStringParameter("loc_copy");
        this.loc_desc = getStringParameter("loc_desc");
        this.loc_update_timestamp = getTimestampParameter("loc_update_timestamp");
    }

    public void delObjectCopy() throws cwException {
        this.obj_bob_nod_id = getLongParameter("obj_nod_id"); 
        this.loc_id = getLongParameter("loc_id");
        this.loc_update_timestamp = getTimestampParameter("loc_update_timestamp");
    }


    
    public void getUserRec() throws cwException {
        this.sort_by = getStringParameter("sort_by");
        this.order_by = getStringParameter("order_by");
        this.usr_ent_id = getLongParameter("usr_ent_id");
        return;

    }
    
    
    public void getItemRec() throws cwException {
        this.call_num = getStringParameter("call_num");
        this.nod_id = getLongParameter("nod_id");
        this.tvw_id = getStringParameter("tvw_id");
        return;
    }

    public void getItemList() throws cwException {
        this.nod_id = getLongParameter("nod_id");
        this.usr_ent_id = getLongParameter("usr_ent_id");
        return;
    }

    public void advSearch() throws cwException{
        this.obj_title = getStringParameter("obj_title");
        this.obj_author = getStringParameter("obj_author");
        this.words = unicode( getStringParameter("key_words") );
        this.nod_id_lst = getLongArrayParameter("nod_id_lst", DELIMITER);
        this.obj_type_list = cwUtils.splitToString( getStringParameter("obj_type_lst"), DELIMITER );
        this.show_deleted = getBooleanParameter("show_deleted");
        this.call_num = getStringParameter("call_num");
        return;
    }    
}
