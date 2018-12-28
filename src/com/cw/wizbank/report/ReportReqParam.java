package com.cw.wizbank.report;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.Hashtable;
import java.util.Vector;

import com.cw.wizbank.*;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwUtils;

public class ReportReqParam extends ReqParam {

    public long usr_ent_id;
    public long itm_id;
    public String[] ent_id_lst;
    public String[] tnd_id_lst;
    public String[] mod_id_lst;
    public String calendar_year;
    public int calendar_view;
    public String itm_title;
    public int itm_title_partial_ind;
    public String[] ats_id_lst;
    public Timestamp start_datetime;
    public Timestamp end_datetime;
    public Timestamp itm_start_datetime;
    public Timestamp itm_end_datetime;
    public Timestamp att_create_start_datetime;
    public Timestamp att_create_end_datetime;
    public Timestamp att_start_datetime;
    public Timestamp att_end_datetime;
    public long rsp_id;
    public String[] rpt_type_lst;
    public String[] spec_name;
    public String[] spec_value;
    public long rte_id;
    public String rsp_title;
    public String rpt_type;
    public long download;
    public String itm_code;
    public String[] itm_type;
    public int show_run_ind;
    public String[] content;
    public String rpt_name;
    public boolean show_public;
    public String window_name;
    public String str_spec_name;
    public String str_spec_value;
    // added for REPORT_VIEW
    public String tvw_id;
    //added for override of searching approval group only for apporver in Learner Report
    public boolean override_appr_usg;
    public long que_id;

    public long[] cos_id_lst;
    public String[] mod_type_lst;
    public LearnerReport report;
    
    public boolean isMyStaff;
    public boolean export_stat_only;
    
    //for learner report of specified course
    public long cos_id;
    public boolean all_mod_ind;
    public boolean all_enrolled_lrn_ind;
    
    public final static String USR_ENT_ID = "usr_ent_id";
    public final static String ITM_ID = "itm_id";
    public final static String ENT_ID_LST = "ent_id_lst";
    public final static String TND_ID_LST = "tnd_id_lst";
    public final static String MOD_ID_LST = "mod_id_lst";
    public final static String CALENDAR_YEAR = "calendar_year";
    public final static String CALENDAR_VIEW = "calendar_view";
    public final static String ITM_TITLE = "itm_title";
    public final static String ITM_TITLE_PARTIAL_IND = "itm_title_partial_ind";
    public final static String ATS_ID_LST = "ats_id_lst";
    public final static String START_DATETIME = "start_datetime";
    public final static String END_DATETIME = "end_datetime";
    public final static String ITM_START_DATETIME = "itm_start_datetime";
    public final static String ITM_END_DATETIME = "itm_end_datetime";
    public final static String ATT_CREATE_START_DATETIME = "att_create_start_datetime";
    public final static String ATT_CREATE_END_DATETIME = "att_create_end_datetime";
    public final static String ATT_START_DATETIME = "att_start_datetime";
    public final static String ATT_END_DATETIME = "att_end_datetime";
    public final static String RSP_ID = "rsp_id";
    public final static String RPT_TYPE_LST = "rpt_type_lst";
    public final static String SPEC_NAME = "spec_name";
    public final static String SPEC_VALUE = "spec_value";
    public final static String RTE_ID = "rte_id";
    public final static String RSP_TITLE = "rsp_title";
    public final static String RPT_TYPE = "rpt_type";
    public final static String DOWNLOAD = "download";
    public final static String ITM_CODE = "itm_code";
    public final static String ITM_TYPE = "itm_type";
    public final static String SHOW_RUN_IND = "show_run_ind";
    public final static String CONTENT = "content";
    public final static String RPT_NAME = "rpt_name";
    public final static String SHOW_PUBLIC = "show_public";
    public final static String WINDOW_NAME = "window_name";
    public final static String IS_MY_STAFF = "is_my_staff";
    public final static String EXPORT_STAT_ONLY = "export_stat_only";
    // added for REPORT_VIEW
    public final static String TVW_ID = "tvw_id";
    //added for override of searching approval group only for apporver in Learner Report
    public final static String OVERRIDE_APPR_USG = "override_appr_usg";
    
    public final static String DELIMITER = "~";
    public final static String STR_DELIMITER = ":_:_:";
    
    public long ils_id;
    public long ils_itm_id;
    
    public ReportReqParam(ServletRequest inReq, String clientEnc_, String encoding_) 
        throws cwException {        
            this.req = inReq;
            this.clientEnc = clientEnc_; 
            this.encoding = encoding_;        
            common();
            return;
    }  
    
    public void save_sees_spec() throws cwException, UnsupportedEncodingException {
        
        String val = null;
        
        str_spec_name = getStringParameter(SPEC_NAME);

        str_spec_value = unicode(getStringParameter(SPEC_VALUE));

        window_name = getStringParameter(WINDOW_NAME);
    }
    
    public void lrn_report() 
        throws cwException, UnsupportedEncodingException {

        usr_ent_id = getLongParameter(USR_ENT_ID);
        ent_id_lst = getStrArrayParameter(ENT_ID_LST, DELIMITER);
        tnd_id_lst = getStrArrayParameter(TND_ID_LST, DELIMITER);
        mod_id_lst = getStrArrayParameter(MOD_ID_LST, DELIMITER);
        itm_id = getLongParameter(ITM_ID);
        calendar_year = getStringParameter(CALENDAR_YEAR);
        calendar_view = getIntParameter(CALENDAR_VIEW);
        itm_title = unicode(getStringParameter(ITM_TITLE));
        itm_title_partial_ind = getIntParameter(ITM_TITLE_PARTIAL_IND);
        ats_id_lst = getStrArrayParameter(ATS_ID_LST, DELIMITER);
        itm_start_datetime = getTimestampParameter(ITM_START_DATETIME);
        itm_end_datetime = getTimestampParameter(ITM_END_DATETIME);
        att_create_start_datetime = getTimestampParameter(ATT_CREATE_START_DATETIME);
        att_create_end_datetime = getTimestampParameter(ATT_CREATE_END_DATETIME);
        att_start_datetime = getTimestampParameter(ATT_START_DATETIME);
        att_end_datetime = getTimestampParameter(ATT_END_DATETIME);
        rsp_id = getLongParameter(RSP_ID);
        rte_id = getLongParameter(RTE_ID);
        isMyStaff =getBooleanParameter(IS_MY_STAFF);
        export_stat_only = getBooleanParameter(EXPORT_STAT_ONLY);
        ils_id =  getLongParameter("ils_id");
        ils_itm_id =  getLongParameter("ils_itm_id");
        return;        
    }    


    public void self_report() 
        throws cwException, UnsupportedEncodingException {
        
        String val;
        String _spec_name = (str_spec_name == null) ? getStringParameter(SPEC_NAME) : str_spec_name;
        boolean flag = false;
        if( _spec_name.indexOf("ent_id") == -1 ) {
            _spec_name += STR_DELIMITER + "ent_id";
            flag = true;
        }
        spec_name = cwUtils.splitToString(_spec_name, STR_DELIMITER);
        
        
        val = (str_spec_value == null) ? unicode(getStringParameter(SPEC_VALUE)) : str_spec_value;
        if( flag )
            val += STR_DELIMITER;
        spec_value = cwUtils.splitToString(val, STR_DELIMITER);
        
        report = new LearnerReport();
        usr_ent_id = getLongParameter(USR_ENT_ID);        
        report.DEFAULT_SORT_COL_ORDER = new String[6];
        report.DEFAULT_SORT_COL_ORDER[2] = "usr_display_bil";
        report.DEFAULT_SORT_COL_ORDER[3] = "t_title";
        report.DEFAULT_SORT_COL_ORDER[0] = "item1.itm_eff_start_datetime";
        report.DEFAULT_SORT_COL_ORDER[1] = "t_code";
        report.DEFAULT_SORT_COL_ORDER[4] = "item1.itm_eff_end_datetime";
        report.DEFAULT_SORT_COL_ORDER[5] = "t_unit";        
        return;        
        
    }    

    
    public void self_cpt_report() 
        throws cwException, UnsupportedEncodingException {
        
        String val;
        String _spec_name = (str_spec_name == null) ? getStringParameter(SPEC_NAME) : str_spec_name;
        int count = 0;
//System.out.println("_spec name = " + _spec_name);
        if( SPEC_NAME.indexOf("ent_id") == -1 ) {
            _spec_name += STR_DELIMITER + "ent_id";
            count++;
        }
//System.out.println("_spec name = " + _spec_name);
        if( SPEC_NAME.indexOf("show_itm_credit") == -1 ) {
            _spec_name += STR_DELIMITER + "show_itm_credit";
            count++;
        }
//System.out.println("_spec name = " + _spec_name);
        spec_name = cwUtils.splitToString(_spec_name, STR_DELIMITER);
        
        val = (str_spec_value == null) ? unicode(getStringParameter(SPEC_VALUE)) : str_spec_value;
//System.out.println("value = " + val);
        for(int i=0; i<count; i++)
            val += STR_DELIMITER;
        spec_value = cwUtils.splitToString(val, STR_DELIMITER);
//System.out.println("value = " + spec_value);
        report = new LearnerReport();
        usr_ent_id = getLongParameter(USR_ENT_ID);        
        report.DEFAULT_SORT_COL_ORDER = new String[6];
        report.DEFAULT_SORT_COL_ORDER[2] = "usr_display_bil";
        report.DEFAULT_SORT_COL_ORDER[3] = "t_title";
        report.DEFAULT_SORT_COL_ORDER[0] = "item1.itm_eff_start_datetime";
        report.DEFAULT_SORT_COL_ORDER[1] = "t_code";
        report.DEFAULT_SORT_COL_ORDER[4] = "item1.itm_eff_end_datetime";
        report.DEFAULT_SORT_COL_ORDER[5] = "t_unit";        
        return;        
        
    }    


    public void group_report()
        throws cwException, UnsupportedEncodingException {

        String val;
        String _spec_name = (str_spec_name == null) ? getStringParameter(SPEC_NAME) : str_spec_name;
        boolean flag = false;
        if( SPEC_NAME.indexOf("ent_id") == -1 ) {
            _spec_name += STR_DELIMITER + "ent_id";
            flag = true;
        }            
        spec_name = cwUtils.splitToString(_spec_name, STR_DELIMITER);
                
        val = (str_spec_value == null) ? unicode(getStringParameter(SPEC_VALUE)) : str_spec_value;
        if( flag )
            val += STR_DELIMITER;
        spec_value = cwUtils.splitToString(val, STR_DELIMITER);

        report = new LearnerReport();
        usr_ent_id = getLongParameter(USR_ENT_ID);        
        report.DEFAULT_SORT_COL_ORDER = new String[6];
        report.DEFAULT_SORT_COL_ORDER[2] = "usr_display_bil";
        report.DEFAULT_SORT_COL_ORDER[3] = "t_title";
        report.DEFAULT_SORT_COL_ORDER[0] = "item1.itm_eff_start_datetime";
        report.DEFAULT_SORT_COL_ORDER[1] = "t_code";
        report.DEFAULT_SORT_COL_ORDER[4] = "item1.itm_eff_end_datetime";
        report.DEFAULT_SORT_COL_ORDER[5] = "t_unit";        
        return;        
        
    }    

    
    public void group_cpt_report() 
        throws cwException, UnsupportedEncodingException {
        
        String val;
        String _spec_name = (str_spec_name == null) ? getStringParameter(SPEC_NAME) : str_spec_name;
        
        int count = 0;
        if( SPEC_NAME.indexOf("ent_id") == -1 ) {
            _spec_name += STR_DELIMITER + "ent_id";
            count++;
        }
        if( SPEC_NAME.indexOf("show_itm_credit") == -1 ) {
            _spec_name += STR_DELIMITER + "show_itm_credit";
            count++;
        }                
        spec_name = cwUtils.splitToString(_spec_name, STR_DELIMITER);

        val = (str_spec_value == null) ? unicode(getStringParameter(SPEC_VALUE)) : str_spec_value;
        for(int i=0; i<count; i++)
            val += STR_DELIMITER;
        spec_value = cwUtils.splitToString(val, STR_DELIMITER);
        
        
        report = new LearnerReport();
        usr_ent_id = getLongParameter(USR_ENT_ID);        
        report.DEFAULT_SORT_COL_ORDER = new String[6];
        report.DEFAULT_SORT_COL_ORDER[2] = "usr_display_bil";
        report.DEFAULT_SORT_COL_ORDER[3] = "t_title";
        report.DEFAULT_SORT_COL_ORDER[0] = "item1.itm_eff_start_datetime";
        report.DEFAULT_SORT_COL_ORDER[1] = "t_code";
        report.DEFAULT_SORT_COL_ORDER[4] = "item1.itm_eff_end_datetime";
        report.DEFAULT_SORT_COL_ORDER[5] = "t_unit";        
        return;        
        
    }    
    
    
    
/*    public void cos_report()
        throws cwException, UnsupportedEncodingException {

        tnd_id_lst = getStrArrayParameter(TND_ID_LST, DELIMITER);
        itm_code = getStringParameter(ITM_CODE);
        itm_title = getStringParameter(ITM_TITLE);
        itm_title_partial_ind = getIntParameter(ITM_TITLE_PARTIAL_IND);
        itm_type = getStrArrayParameter(ITM_TYPE, DELIMITER);
        show_run_ind = getIntParameter(SHOW_RUN_IND);
        content = getStrArrayParameter(CONTENT, DELIMITER);
        start_datetime = getTimestampParameter(START_DATETIME);
        end_datetime = getTimestampParameter(END_DATETIME);

        return;
    }*/
    
    public void report(HttpSession sess) 
        throws cwException, UnsupportedEncodingException {
        String val;
        
        rpt_type_lst = getStrArrayParameter(RPT_TYPE_LST, DELIMITER);
        window_name = getStringParameter(WINDOW_NAME);
        String name_key = window_name;
        String[] nameStrings= cwUtils.splitToString(window_name, "__AA__");
        if(nameStrings!= null && nameStrings.length > 0){
        	name_key = nameStrings[0];
        }
        
        Hashtable hSpec = (Hashtable) sess.getAttribute(ReportModule.SESS_SPEC_HASH);
        if(hSpec != null && name_key != null) {
            Vector vSpec = (Vector) hSpec.get(name_key);
            if (vSpec != null && vSpec.size() == 2) {
                str_spec_name = (String)vSpec.elementAt(0);
                if(str_spec_name != null) {
                    spec_name =cwUtils.splitToString(str_spec_name, STR_DELIMITER);
                }
                str_spec_value = (String)vSpec.elementAt(1);
                if(str_spec_value != null) {
                    spec_value =cwUtils.splitToString(str_spec_value, STR_DELIMITER);
                }
            }
        }
        
        if(spec_name == null || spec_name.length == 0) {
            spec_name = getStrArrayParameter(SPEC_NAME, STR_DELIMITER);
        }
        if(spec_value == null || spec_value.length == 0) {
            val = unicode(getStringParameter(SPEC_VALUE));
            spec_value = cwUtils.splitToString(val, STR_DELIMITER);
        }
        
        rte_id = getLongParameter(RTE_ID);
        rsp_title = unicode(getStringParameter(RSP_TITLE));
        rsp_id = getLongParameter(RSP_ID);
        rpt_type = getStringParameter(RPT_TYPE);
        download = getLongParameter(DOWNLOAD);
        rpt_name = unicode(getStringParameter(RPT_NAME));
        show_public = getBooleanParameter(SHOW_PUBLIC);
        // added for REPORT_VIEW
        this.tvw_id = getStringParameter(this.TVW_ID);
        //added for override of searching approval group only for apporver in Learner Report
        this.override_appr_usg = getBooleanParameter(OVERRIDE_APPR_USG);
        return;
    }

    public void module() 
        throws cwException, UnsupportedEncodingException {
        
        cos_id_lst = getLongArrayParameter("cos_id_lst", DELIMITER);
        mod_type_lst = getStrArrayParameter("mod_type_lst", DELIMITER);
        rte_id = getLongParameter(RTE_ID);    
        
        return;
    }
    
    public void que_report_by_no() throws cwException {
        que_id = getLongParameter("que_id");
    }
    
    public void getModuleEvnOfCosParams() throws cwException {
    	cos_id = getLongParameter("cos_id");
    	start_datetime = getTimestampParameter(START_DATETIME);
    	end_datetime = getTimestampParameter(END_DATETIME);
    	all_mod_ind = getBooleanParameter("all_mod_ind");
    	all_enrolled_lrn_ind = getBooleanParameter("all_enrolled_lrn_ind");
    	Vector specNameVec = new Vector();
    	Vector specValueVec = new Vector();
    	if(cos_id > 0) {
    		specNameVec.addElement("cos_id");
    		specValueVec.addElement(cos_id + "");
    	}
    	if(start_datetime != null) {
    		specNameVec.addElement("start_datetime");
    		specValueVec.addElement(start_datetime.toString());
    	}
    	if(end_datetime != null) {
    		specNameVec.addElement("end_datetime");
    		specValueVec.addElement(end_datetime.toString());
    	}
    	if(all_mod_ind){
    		specNameVec.addElement("all_mod_ind");
    		specValueVec.addElement("1");
    	}
    	if(all_enrolled_lrn_ind){
    		specNameVec.addElement("all_enrolled_lrn_ind");
    		specValueVec.addElement("1");
    	}
    	if(specNameVec.size() > 0) {
    		spec_name = cwUtils.vec2strArray(specNameVec);
    		spec_value = cwUtils.vec2strArray(specValueVec);
    	}
    }
}

