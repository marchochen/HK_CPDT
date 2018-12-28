package com.cw.wizbank.entity;


import javax.servlet.ServletRequest;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import com.cw.wizbank.entity.IndustryCode;
//import com.cw.wizbank.entity.EntityDefinition;
//import com.cw.wizbank.entity.RegUser;
import com.cw.wizbank.*;
import com.cw.wizbank.util.*;
import com.cw.wizbank.qdb.*;

public class EntityReqParam extends ReqParam {

    public long[] idc_ent_ids;
    public Timestamp[] idc_upd_timestamps;
    public String entityType;
    public String relationType;
        
    public final static String ROL_EXT_ID = "rol_ext_id";
    public final static String FTN_EXT_ID_LST = "ftn_ext_id_lst";    

    public IndustryCode idc;
    //public EntityDefinition end;
    public dbRegUser user;

    public EntityReqParam(ServletRequest inReq, String clientEnc_, String encoding_) 
        throws cwException {        
            this.req = inReq;
            this.clientEnc = clientEnc_; 
            this.encoding = encoding_;        
            common();
            return;
    }    

    public void myCommon() throws cwException {
        
        this.entityType = getStringParameter("ent_type");
    }
    
    public void get_idc_tree() 
        throws cwException, UnsupportedEncodingException {
        
        if(idc == null) {
            idc = new IndustryCode();
        }
        
        idc.userEntId = getLongParameter("usr_ent_id");
        
        return;        
    }
    
    public void assign_idc()
        throws cwException, UnsupportedEncodingException {

        if(idc == null) {
            idc = new IndustryCode();
        }
        
        idc.userEntId = getLongParameter("usr_ent_id");
    
        idc_ent_ids = getLongArrayParameter("ent_id_lst", "~");
        
        relationType = getStringParameter("gpm_type");
        
        return;        
    }

    public void get_idc_cnt_lst()
        throws cwException, UnsupportedEncodingException {

        if(idc == null) {
            idc = new IndustryCode();
        }
            
        idc.ent_id = getLongParameter("ent_id");

        idc.parentEntId = getLongParameter("parent_ent_id");
        
        return;        
    }

    public void add_idc()
        throws cwException, UnsupportedEncodingException {

        if(idc == null) {
            idc = new IndustryCode();
        }
            
        idc.name = dbUtils.unicodeFrom(getStringParameter("display_bil"), clientEnc, encoding, false);
        
        idc.parentEntId = getLongParameter("parent_ent_id");
        
        return;        
    }

    public void get_idc()
        throws cwException, UnsupportedEncodingException {

        if(idc == null) {
            idc = new IndustryCode();
        }
        
        idc.ent_id = getLongParameter("ent_id");
        
        return;        
    }

    public void upd_idc()
        throws cwException, UnsupportedEncodingException {

        if(idc == null) {
            idc = new IndustryCode();
        }
            
        idc.name = dbUtils.unicodeFrom(getStringParameter("display_bil"), clientEnc, encoding, false);
        
        idc.ent_id = getLongParameter("ent_id");
        
        idc.ent_upd_date = getTimestampParameter("upd_timestamp");
        
        return;        
    }

    public void del_idc()
        throws cwException, UnsupportedEncodingException {

        if(idc == null) {
            idc = new IndustryCode();
        }
            
        idc_ent_ids = getLongArrayParameter("ent_id_lst", "~");
        
        idc_upd_timestamps = getTimestampArrayParameter("upd_timestamp_lst", "~");
        
        return;        
    }
    /*
    public void get_ent_def() 
        throws cwException, UnsupportedEncodingException {

        if(end == null) {
            end = new EntityDefinition();
        }
            
        end.entityType = getStringParameter("ent_type");

        return;        
    }
    */
    public void get_usr_rol() 
        throws cwException, UnsupportedEncodingException {

        if(user == null) {
            user = new dbRegUser();
        }
            
        user.usr_ent_id = getLongParameter("usr_ent_id");

        return;        
    }

    public void get_assigned_rol()
        throws cwException, UnsupportedEncodingException {
        
        if(user == null) {
            user = new dbRegUser();
        }
        
        user.usr_ent_id = getLongParameter("usr_ent_id");
 
        return;        
    }

    // convert a String[] to Timestamp[]
    protected Timestamp[] getTimestampArrayParameter(String paraname, String separator) 
        throws cwException {

        try {
            String[] s = getStrArrayParameter(paraname, separator);
            if(s != null) {
                Timestamp[] t = new Timestamp[s.length];
                
                for(int i=0; i<s.length; i++) {
                    t[i] = Timestamp.valueOf(s[i]);
                }
                return t;
            }
            else
                return null;
        }
        catch(IllegalArgumentException e) {
            throw new cwException("EntityCodeReqParam.getLongArrayParameter: Cannot convert string to number");
        }
    }

    
    // convert a String[] to long[]
    protected long[] getLongArrayParameter(String paraname, String separator) 
        throws cwException {

        try {
            String[] s = getStrArrayParameter(paraname, separator);
            if(s != null) {
                long[] l = new long[s.length];
                
                for(int i=0; i<s.length; i++) {
                    l[i] = Long.parseLong(s[i]);
                }
                return l;
            }
            else
                return null;
        }
        catch(NumberFormatException e) {
            throw new cwException("EntityCodeReqParam.getLongArrayParameter: Cannot convert string to number");
        }
    }
}