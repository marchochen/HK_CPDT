package com.cw.wizbank.supervise;


import javax.servlet.ServletRequest;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.Vector;
import com.cw.wizbank.*;
import com.cw.wizbank.util.*;
import com.cw.wizbank.db.DbTable;
import com.cw.wizbank.qdb.dbUserGroup;
import com.cw.wizbank.qdb.dbUtils;

public class SuperviseReqParam extends ReqParam {

    public dbUserGroup usg;
    public int page;
    public int page_size;
    public long usr_ent_id;
    public Vector v_itm_type;
    public Vector v_soln_type;
    public int pgm_id;
    public int pgm_run_id;
    public String[] order_bys;
    public String[] sort_bys;
    public String[] targeted_itm_apply_method_lst;
    public boolean include_targeted_itm;
    public boolean all_ind;
    public String[] ent_id_lst;
    public String[] tnd_id_lst;
    public String calendar_year;
    public int calendar_view;
    public String itm_title;
    public int itm_title_partial_ind;
    public String[] ats_id_lst;
    public Timestamp itm_start_datetime;
    public Timestamp itm_end_datetime;
    public Timestamp att_start_datetime;
    public Timestamp att_end_datetime;
    public long rsp_id;
    public long rte_id;
    public String rpt_type;
    public long download;
    public String[] spec_name;
    public String[] spec_value;
    public boolean searchEmptyInd;
    public boolean searchInMyStaff;

    public SuperviseReqParam(ServletRequest inReq, String clientEnc_, String encoding_) 
        throws cwException {        
            this.req = inReq;
            this.clientEnc = clientEnc_; 
            this.encoding = encoding_;        
            common();
            return;
    }    

    public void myCommon() throws cwException {
        
    }
    
    public void searchEntLst() throws cwException, UnsupportedEncodingException {
        
        searchEmptyInd = false;
        
        usg = new dbUserGroup();
        Vector s_ext_col_names = new Vector();
        Vector s_ext_col_values = new Vector();
        Vector s_ext_col_types = new Vector();
        Enumeration attr_names = req.getParameterNames();
        while(attr_names.hasMoreElements()) {
            String att_name = (String)attr_names.nextElement();
            if (att_name.startsWith("s_ext_")) {
                s_ext_col_names.addElement(att_name);
                if (att_name.endsWith("_text")) {
                    s_ext_col_types.addElement(DbTable.COL_TYPE_STRING);
                    s_ext_col_values.addElement(dbUtils.unicodeFrom(req.getParameterValues(att_name)[0], clientEnc, encoding, false));
                }
                if (att_name.endsWith("_fr")) {
                    s_ext_col_types.addElement(DbTable.COL_TYPE_TIMESTAMP);
                    if (req.getParameterValues(att_name)[0].length() > 0) {
                        s_ext_col_values.addElement(Timestamp.valueOf(req.getParameterValues(att_name)[0]));
                    } else {
                        s_ext_col_values.addElement("");
                    }
                }
                if (att_name.endsWith("_to")) {
                    s_ext_col_types.addElement(DbTable.COL_TYPE_TIMESTAMP);
                    if (req.getParameterValues(att_name)[0].length() > 0) {
                        s_ext_col_values.addElement(Timestamp.valueOf(req.getParameterValues(att_name)[0]));
                    } else {
                        s_ext_col_values.addElement("");
                    }
                }
                if (att_name.endsWith("_select")) {
                    s_ext_col_types.addElement(DbTable.COL_TYPE_STRING);
                    s_ext_col_values.addElement(req.getParameterValues(att_name)[0]);
                }
                if (att_name.endsWith("_check")) {
                    s_ext_col_types.addElement(DbTable.COL_TYPE_STRING);
                    String[] checkValue = req.getParameterValues(att_name);
                    s_ext_col_values.addElement(checkValue);
                }
            }
        }
        
        usg.s_ext_col_names = s_ext_col_names;
        usg.s_ext_col_values = s_ext_col_values;
        usg.s_ext_col_types = s_ext_col_types;
        
        usg.s_usr_display_bil = unicode(getStringParameter("s_usr_display_bil"));
        usg.s_usr_gender = getStringParameter("s_usr_gender");
        usg.s_usr_id = getStringParameter("s_usr_id");
        usg.s_order_by = getStringParameter("s_order_by");
        usg.s_sort_by = getStringParameter("s_sort_by");
        usg.s_usg_ent_id_lst = getStrArrayParameter("s_usg_ent_id_lst", "~");
        usg.usg_ent_id = getLongParameter("ent_id");
         if(usg.usg_ent_id < 0) {
            usg.usg_ent_id = 0;
         }
        usg.ent_id = usg.usg_ent_id;
        usg.s_grade = getLongParameter("usr_grade");
        if(usg.s_grade < 0) {
            usg.s_grade = 0;
        }
        page = getIntParameter("cur_page");
        page_size = getIntParameter("page_size");
        

//        if(usg.s_usr_display_bil == null && usg.s_usr_id == null &&
//           (usg.s_usg_ent_id_lst == null || usg.s_usg_ent_id_lst.length == 0) &&
//           usg.usg_ent_id <= 0 && page <= 0 && page_size <= 0 &&
//           usg.s_order_by == null && usg.s_sort_by == null) {
//            searchEmptyInd = true;
//        }
        
        if(page_size < 0) {
            page_size = 10;
        }
        if(page < 0) {
            page = 1;
        }
        searchInMyStaff = getBooleanParameter("search_in_mystaff");
        
    }
    
    public void getUsr() throws cwException {
        usr_ent_id = getLongParameter("usr_ent_id");
    }
    
    public void aeLrnPlan() throws cwException {
        usr_ent_id = getLongParameter("usr_ent_id");
        v_itm_type = cwUtils.splitToVecString(getStringParameter("item_type"),"~");
        targeted_itm_apply_method_lst = getStrArrayParameter("targeted_item_apply_method_lst", "~");
        include_targeted_itm = getBooleanParameter("include_targeted_itm");
        
        pgm_id = getIntParameter("pgm_id");
        if(pgm_id < 0) {
            pgm_id = 0;
        }
        
        pgm_run_id = getIntParameter("pgm_run_ind");
        if(pgm_run_id < 0) {
            pgm_run_id = 0;
        }
    }
    
    public void aeLrnSoln() throws cwException {
        usr_ent_id = getLongParameter("usr_ent_id");
        v_itm_type = cwUtils.splitToVecString(getStringParameter("item_type"),"~");
        v_soln_type = cwUtils.splitToVecString(getStringParameter("soln_type"),"~");
        targeted_itm_apply_method_lst = getStrArrayParameter("targeted_item_apply_method_lst", "~");
        all_ind = getBooleanParameter("all_ind");
        order_bys = getStrArrayParameter("orber_by", "~");
        sort_bys = getStrArrayParameter("sort_by", "~");
    }

    public void getRpt() throws cwException {
        rpt_type = getStringParameter("rpt_type");
        download = getLongParameter("download");
        spec_name = getStrArrayParameter("spec_name", "~");

        String val = unicode(getStringParameter("spec_value"));
        spec_value = cwUtils.splitToString(val, "~");
    }

    public void getLrnRpt() 
        throws cwException, UnsupportedEncodingException {

        usr_ent_id = getLongParameter("usr_ent_id");
        ent_id_lst = getStrArrayParameter("ent_id_lst", "~");
        tnd_id_lst = getStrArrayParameter("tnd_id_lst", "~");
        calendar_year = getStringParameter("calendar_year");
        calendar_view = getIntParameter("calendar_view");
        itm_title = unicode(getStringParameter("itm_title"));
        itm_title_partial_ind = getIntParameter("itm_title_partial_ind");
        ats_id_lst = getStrArrayParameter("ats_is_lst", "~");
        itm_start_datetime = getTimestampParameter("itm_start_datetime");
        itm_end_datetime = getTimestampParameter("itm_end_datetime");
        att_start_datetime = getTimestampParameter("att_start_datetime");
        att_end_datetime = getTimestampParameter("att_end_datetime");
        rsp_id = getLongParameter("rsp_id");
        rte_id = getLongParameter("rte_id");

        return;        
    }    
    
}