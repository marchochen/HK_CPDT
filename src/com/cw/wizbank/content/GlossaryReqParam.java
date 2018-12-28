package com.cw.wizbank.content;


import javax.servlet.ServletRequest;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;

import com.cw.wizbank.db.DbCtGlossary;
import com.cw.wizbank.*;
import com.cw.wizbank.util.*;
import com.cw.wizbank.qdb.*;

public class GlossaryReqParam extends ReqParam {


    public String letter;
    public Timestamp indexTimestamp;
    
    public DbCtGlossary dbGlossary;
    public dbModule myDbModule;    
    
    public final static String RES_ID           =   "glo_res_id";
    public final static String GLO_KEY          =   "glo_key";
    public final static String GLO_DEF          =   "glo_def";
    public final static String GLO_ID           =   "glo_id";
    public final static String FIRST_CHAR       =   "glo_first_char";
    public final static String INDEX_TIMESTAMP  =   "glo_timestamp";
    public final static String TKH_ID           =   "tkh_id";
    
    public GlossaryReqParam(ServletRequest inReq, String clientEnc_, String encoding_) 
        throws cwException {        
            this.req = inReq;
            this.clientEnc = clientEnc_; 
            this.encoding = encoding_;        
            common();
            return;
    }    

    
    
    public void ins_key() 
        throws cwException, UnsupportedEncodingException {
        
        dbGlossary = new DbCtGlossary();
        
        dbGlossary.glo_res_id = getLongParameter(RES_ID);

        dbGlossary.glo_keyword = dbUtils.unicodeFrom(getStringParameter(GLO_KEY), clientEnc, encoding);

        dbGlossary.glo_definition = dbUtils.unicodeFrom(getStringParameter(GLO_DEF), clientEnc, encoding);

        return;        
    }
    
    public void upd_key() 
        throws cwException, UnsupportedEncodingException {
        
        dbGlossary = new DbCtGlossary();

        dbGlossary.glo_keyword = dbUtils.unicodeFrom(getStringParameter(GLO_KEY), clientEnc, encoding);

        dbGlossary.glo_definition = dbUtils.unicodeFrom(getStringParameter(GLO_DEF), clientEnc, encoding);
                
        dbGlossary.glo_id = getLongParameter(GLO_ID);        
    
        return;
    }
    
    
    public void del_key() 
        throws cwException {
        
        dbGlossary = new DbCtGlossary();
        
        dbGlossary.glo_id = getLongParameter(GLO_ID);

        return;
    }
    

    public void get_key() 
        throws cwException {
        
        dbGlossary = new DbCtGlossary();

        dbGlossary.glo_id = getLongParameter(GLO_ID);

        return;
    }
    
    
    public void get_keys() 
        throws cwException {
        
        dbGlossary = new DbCtGlossary();

        myDbModule = new dbModule();
        
        dbGlossary.glo_id = getLongParameter(GLO_ID);
        
        dbGlossary.glo_res_id = getLongParameter(RES_ID);
        
        myDbModule.mod_res_id = getLongParameter(RES_ID);
        
        myDbModule.tkh_id = getLongParameter(TKH_ID);
        
        letter = getStringParameter(FIRST_CHAR);
        
        indexTimestamp = getTimestampParameter(INDEX_TIMESTAMP);
        
        return;
    }
    
}