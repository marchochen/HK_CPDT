package com.cw.wizbank.accesscontrol;


import javax.servlet.ServletRequest;
import java.io.UnsupportedEncodingException;
import com.cw.wizbank.*;
import com.cw.wizbank.util.*;

public class AccessControlReqParam extends ReqParam {
    
    public long instance_id;
    public String instance_type;
    public String rol_ext_id;
    public String[] ftn_ext_ids;
    public long[] ent_id_lst;
    public long usr_ent_id;

    public long app_id;
    public long pid;
    public long sid;
    public long aid;

    public final static String ROL_EXT_ID = "rol_ext_id";
    public final static String FTN_EXT_ID_LST = "ftn_ext_id_lst";    
    public final static String INSTANCE_ID = "instance_id";
    public final static String ENT_ID_LST = "ent_id_lst";
    public final static String INSTANCE_TYPE = "instance_type";
    public final static String USR_ENT_ID = "usr_ent_id";
    
    public AccessControlReqParam(ServletRequest inReq, String clientEnc_, String encoding_) 
        throws cwException {        
            this.req = inReq;
            this.clientEnc = clientEnc_; 
            this.encoding = encoding_;        
            common();
            return;
    }    

    public void get_granted_ftn() 
        throws cwException, UnsupportedEncodingException {
        
        rol_ext_id = getStringParameter(ROL_EXT_ID);

        return;        
    }

    public void get_rol_ftn() 
        throws cwException, UnsupportedEncodingException {
        
        rol_ext_id = getStringParameter(ROL_EXT_ID);

        return;        
    }
    
    public void save_rol_ftn() 
        throws cwException, UnsupportedEncodingException {
        
        rol_ext_id = getStringParameter(ROL_EXT_ID);
        ftn_ext_ids = getStrArrayParameter(FTN_EXT_ID_LST, "~");

        return;        
    }
    
    public void get_assigned_ent()
        throws cwException, UnsupportedEncodingException {
        
        rol_ext_id = getStringParameter(ROL_EXT_ID);
        instance_id = getLongParameter(INSTANCE_ID);
        instance_type = getStringParameter(INSTANCE_TYPE);
        
        return;        
    }

    public void assign_ent()
        throws cwException, UnsupportedEncodingException {
        
        rol_ext_id = getStringParameter(ROL_EXT_ID);
        instance_id = getLongParameter(INSTANCE_ID);
        //ent_type = getStringParameter(ENT_TYPE);
        ent_id_lst = getLongArrayParameter(ENT_ID_LST, "~");
        instance_type = getStringParameter(INSTANCE_TYPE);
 
        return;        
    }

    public void remove_ent()
        throws cwException, UnsupportedEncodingException {
        
        rol_ext_id = getStringParameter(ROL_EXT_ID);
        instance_id = getLongParameter(INSTANCE_ID);
        ent_id_lst = getLongArrayParameter(ENT_ID_LST, "~");
        instance_type = getStringParameter(INSTANCE_TYPE);
 
        return;        
    }

    public void get_rol_ftn_x()
        throws cwException, UnsupportedEncodingException {
        
        rol_ext_id = getStringParameter(ROL_EXT_ID);
 
        return;        
    }

    public void test_ac_wrk() throws cwException
    {
        app_id = getLongParameter("app_id");
        pid = getLongParameter("pid");
        sid = getLongParameter("sid");
        aid = getLongParameter("aid");
    }

    public void save_rol_ftn_x()
        throws cwException, UnsupportedEncodingException {
        
        rol_ext_id = getStringParameter(ROL_EXT_ID);
        ftn_ext_ids = req.getParameterValues("ftn_ext_id");
 
        return;        
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