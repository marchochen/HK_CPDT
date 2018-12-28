package com.cw.wizbank.upload;

import javax.servlet.ServletRequest;

import com.cw.wizbank.*;
import com.cw.wizbank.util.*;
import com.cw.wizbank.db.DbIMSLog;
import com.cw.wizbank.enterprise.IMSLog;


public class UploadReqParam extends ReqParam {

    public String src_filename = null;
    public String upload_file;
    
    public String que_type = null;
    public String que_lan = null;
    
    public long   obj_id = 0;
    public long   ulg_id = 0;
    
    // upload type would be ins / upd
    public String upload_type = null;
    public boolean allow_update = false;
    public DbIMSLog dbIlg;
    public String upload_desc = null;    
    
    //public String sort_col;
    //public String sort_order;
    public String log_type;
    public String log_process;
    
    //import enroll record
    public long itm_id;
    
    public String instr_type;
    
    public String template_url;
    
    public Boolean usr_pwd_need_change_ind; 
    public Boolean identical_usr_no_import;
    public Boolean oldusr_pwd_need_update_ind;
    
    public String mod_type;
    
    public UploadReqParam(ServletRequest inReq, String clientEnc_, String encoding_)
        throws cwException {
            this.req = inReq;
            this.clientEnc = clientEnc_;
            this.encoding = encoding_;
            super.common();
            String var = null;

            //Print submited param
            /*
            Enumeration enumeration = req.getParameterNames();
            while( enumeration.hasMoreElements() ) {
                String name = (String) enumeration.nextElement();
                String[] values = req.getParameterValues(name);
                if( values != null )
                    for(int i=0; i<values.length; i++)
                        System.out.println(name + " (" + i + "):" + values[i]);
            }
            */
    }


    public void uploadQue()
        throws cwException {
            
            que_type = getStringParameter("que_type");
            //que_lan = getStringParameter("que_lan");
            src_filename = unicode(getStringParameter("src_filename"));
            //obj_id = getLongParameter("obj_id");
            upload_desc = unicode(getStringParameter("ulg_desc"));
            allow_update = getBooleanParameter("allow_update");
            return;
        }

    public void getQueLogHistory()
        throws cwException {
            
            pagination();
            log_process = getStringParameter("ulg_pocess");
            que_type = getStringParameter("que_type");
            return;

        }

    public void uploadUser()
        throws cwException {
    	     
            src_filename = unicode(getStringParameter("src_filename"));
            upload_type = getStringParameter("upload_type");
            dbIlg = new DbIMSLog();
            dbIlg.ilg_desc = unicode(getStringParameter("upload_desc"));
            dbIlg.ilg_filename = src_filename;
            dbIlg.ilg_dup_data_update_ind = getBooleanParameter("allow_update");
            dbIlg.ilg_method = IMSLog.ACTION_TRIGGERED_BY_UI;
            usr_pwd_need_change_ind = Boolean.valueOf(getStringParameter("usr_pwd_need_change_ind"));
            identical_usr_no_import = Boolean.valueOf(getStringParameter("identical_usr_no_import"));
            oldusr_pwd_need_update_ind = Boolean.valueOf(getStringParameter("oldusr_pwd_need_update_ind"));
            return;
        }
        
    public void uploadEnrollment()
        throws cwException {
            
            src_filename = unicode(getStringParameter("src_filename"));
//            allow_update = getBooleanParameter("allow_update");
            upload_file = (bMultipart) ? multi.getFilesystemName("upload_file") : req.getParameter("upload_file");
            upload_type = getStringParameter("upload_type");
            dbIlg = new DbIMSLog();
            dbIlg.ilg_desc = unicode(getStringParameter("upload_desc"));
            dbIlg.ilg_filename = src_filename;
//            dbIlg.ilg_dup_data_update_ind = getBooleanParameter("allow_update");
            dbIlg.ilg_method = IMSLog.ACTION_TRIGGERED_BY_UI;
            itm_id = getLongParameter("itm_id");
            return;
        }


    public void cookQue()
        throws cwException {
            
            ulg_id = getLongParameter("ulg_id");
            que_type = getStringParameter("que_type");
            usr_pwd_need_change_ind = Boolean.valueOf(getStringParameter("usr_pwd_need_change_ind"));
            identical_usr_no_import = Boolean.valueOf(getStringParameter("identical_usr_no_import"));
            oldusr_pwd_need_update_ind = Boolean.valueOf(getStringParameter("oldusr_pwd_need_update_ind"));
            return;
        }
        
        int seed;
    public void random()
        throws cwException {
            seed = getIntParameter("seed");
        }

    public void getLogHistory()
        throws cwException {
            
            pagination();
            log_process = getStringParameter("log_pocess");
            log_type = getStringParameter("log_type");
            itm_id = getLongParameter("itm_id");
            return;

        }
    
    public void getInstr() throws cwException {
    	this.instr_type = getStringParameter("instr_type");
    	this.mod_type = getStringParameter("mod_type");
    }
    
    public void getCreditTpl() throws cwException {
    	this.template_url = getStringParameter("template_url");
    }
    
    public void uploadCredit() throws cwException {
        src_filename = unicode(getStringParameter("src_filename"));
        upload_type = getStringParameter("upload_type");
        dbIlg = new DbIMSLog();
        dbIlg.ilg_desc = unicode(getStringParameter("upload_desc"));
        dbIlg.ilg_filename = src_filename;
        dbIlg.ilg_dup_data_update_ind = getBooleanParameter("allow_update");
        dbIlg.ilg_method = IMSLog.ACTION_TRIGGERED_BY_UI;
    }

}