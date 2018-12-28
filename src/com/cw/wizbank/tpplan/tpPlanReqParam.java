package com.cw.wizbank.tpplan;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;

import javax.servlet.ServletRequest;

import com.cw.wizbank.ReqParam;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.tpplan.db.dbTpTrainingPlan;
import com.cw.wizbank.tpplan.db.dbTpYearSetting;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwPagination;
import com.cw.wizbank.util.cwUtils;

/**
 * @author jackyx
 * @date 2007-10-15
 */
public class tpPlanReqParam extends ReqParam {

    public final static String DELIMITER = "~";

	public final static String STR_DELIMITER = ":_:_:";

	public dbTpTrainingPlan tpTp;

	public String upload_type;

	public long tcr_id;

	public long tpn_id;

	public long tpn_date_year;

	public long tpn_date_month;

	public long search_tcr_id;

	public String tpn_status;
	
	public String tpn_type;

	public Timestamp tpn_update_timestamp;

	public long year;

	public boolean is_makeup;
	
	public String[] tpn_status_lst;
	
	public Timestamp upd_timestamp;

	public dbTpYearSetting tys;

	public long ypn_tcr_id;

	public Timestamp ypn_year;

	public String src_filename;

	public String sear_code_name;

	public String status;

	public String sel_tpn_id;

	public long[] tpn_id_list = null;

	public String sel_update_timestamp;

	public Timestamp[] update_timestamp;

	public String sel_tcr_id;

	public long[] tcr_id_list = null;

	public String plantype;

	public String entrance;

	public String sort_by;

	public String order_by;

	public int page;

	public int page_size;

	// add for search training plan or class by code
	boolean sear_by_code_name;
	
	public boolean read_only;
	public String window_name;
    public tpPlanReqParam(ServletRequest inReq, String clientEnc_,
            String encoding_) throws cwException {
        this.req = inReq;
        this.clientEnc = clientEnc_;
        this.encoding = encoding_;
        common();
        return;
    }
    
    public void plan() throws cwException, UnsupportedEncodingException {
        String var;
        // tpn_id
        var = req.getParameter("tpn_id");
        if (var != null && var.length() > 0) {
            try {
                tpn_id = Long.parseLong(var);
            } catch (NumberFormatException e) {
                tpn_id = 0;
            }
        } else {
            tpn_id = -1;
        }

        // tpn_tcr_id
        var = req.getParameter("tcr_id");
        if (var != null && var.length() > 0) {
            try {
                tcr_id = Long.parseLong(var);
            } catch (NumberFormatException e) {
                tcr_id = 0;
            }
        } else {
            tcr_id = -1;
        }
        
        // search_tcr_id
        var = req.getParameter("search_tcr_id");
        if (var != null && var.length() > 0) {
            try {
                search_tcr_id = Long.parseLong(var);
            } catch (NumberFormatException e) {
            	search_tcr_id = 0;
            }
        } else {
        	search_tcr_id = -1;
        }

        // tpn_date year
        var = req.getParameter("tpn_date_year");
        if (var != null && var.length() > 0) {
            try {
                tpn_date_year = Long.parseLong(var);
            } catch (NumberFormatException e) {
                tpn_date_year = 0;
            }
        } else {
            tpn_date_year = -1;
        }

        // tpn_date month
        var = req.getParameter("tpn_date_month");
        if (var != null && var.length() > 0) {
            try {
                tpn_date_month = Long.parseLong(var);
            } catch (NumberFormatException e) {
                tpn_date_month = 0;
            }
        } else {
            tpn_date_month = -1;
        }

        var = req.getParameter("tpn_status");
        if (var != null && var.length() > 0) {
            tpn_status = var;
        }

        var = req.getParameter("tpn_type");
        if (var != null && var.length() > 0) {
            tpn_type = var;
        }
        
        var = req.getParameter("tpn_update_timestamp");
        if (var != null && var.length() > 0) {
            tpn_update_timestamp = Timestamp.valueOf(var);
        } else
            tpn_update_timestamp = null;

        tpn_status_lst = getStrArrayParameter("tpn_status_lst", DELIMITER);
        
        is_makeup = getBooleanParameter("is_makeup");
    
    }

    public void tpPlanConfig() throws cwException {
        year = getLongParameter("year");
        tcr_id = getLongParameter("tcr_id");
        upd_timestamp = getTimestampParameter("ysg_update_timestamp");

        tys = new dbTpYearSetting();
        tys.ysg_child_tcr_id_lst = getStringParameter("ysg_child_tcr_id_lst");
        tys.ysg_submit_start_datetime = getTimestampParameter("submit_from");
        if (tys.ysg_submit_start_datetime == null) {
            tys.ysg_submit_start_datetime = Timestamp
                    .valueOf(cwUtils.MIN_TIMESTAMP);
        }
        tys.ysg_submit_end_datetime = getTimestampParameter("submit_to");
        if (tys.ysg_submit_end_datetime == null) {
            tys.ysg_submit_end_datetime = Timestamp
                    .valueOf(cwUtils.MAX_TIMESTAMP);
        }
        tys.ysg_year = year;
        tys.ysg_tcr_id = tcr_id;
    }

    public void yearPlan() throws cwException {
        tcr_id = getLongParameter("tcr_id");
        upd_timestamp = getTimestampParameter("ypn_update_timestamp");
        ypn_tcr_id = getLongParameter("ypn_tcr_id");
        year = getLongParameter("year");
    }
    

    public void getTrainingPlan() throws cwException,
            UnsupportedEncodingException {
        tpTp = new dbTpTrainingPlan();
        tpTp.tpn_id = getLongParameter("tpn_id");
        if (tpTp.tpn_id == LONG_PARAMETER_NOT_FOUND) {
            tpTp.tpn_id = 0;
        }
        tpTp.tpn_tcr_id = getLongParameter("tpn_tcr_id");
        if (tpTp.tpn_tcr_id == LONG_PARAMETER_NOT_FOUND) {
            tpTp.tpn_tcr_id = 0;
        }
        tpTp.tpn_date = getTimestampParameter("tpn_date");
        
        tpTp.tpn_lrn_count = getLongParameter("tpn_lrn_count");
        if (tpTp.tpn_lrn_count == LONG_PARAMETER_NOT_FOUND) {
            tpTp.tpn_lrn_count = 0;
        }
  
        tpTp.tpn_wb_start_date = getTimestampParameter("tpn_wb_start_date");
        tpTp.tpn_wb_end_date = getTimestampParameter("tpn_wb_end_date");
        tpTp.tpn_ftf_start_date = getTimestampParameter("tpn_ftf_start_date");
        tpTp.tpn_ftf_end_date = getTimestampParameter("tpn_ftf_end_date");
        tpTp.tpn_type = getStringParameter("tpn_type");
        tpTp.tpn_fee = getFloatParameter("tpn_fee");
        if (tpTp.tpn_fee == FLOAT_PARAMETER_NOT_FOUND) {
            tpTp.tpn_fee = 0;
        }
        tpTp.tpn_status = getStringParameter("tpn_status");
        tpTp.tpn_approve_usr_id = getStringParameter("tpn_approve_usr_id");
        tpTp.tpn_update_timestamp = getTimestampParameter("tpn_upd_timestamp");

        String val;
        val = getStringParameter("tpn_duration");
        if (val != null && val.length() > 0) {
            tpTp.tpn_duration = dbUtils.unicodeFrom(val, clientEnc, encoding, bMultipart);
        }
        val = getStringParameter("tpn_tnd_title");
        if (val != null && val.length() > 0) {
            tpTp.tpn_tnd_title = dbUtils.unicodeFrom(val, clientEnc, encoding, bMultipart);
        }
        val = getStringParameter("tpn_cos_type");
        if (val != null && val.length() > 0) {
            tpTp.tpn_cos_type = dbUtils.unicodeFrom(val, clientEnc, encoding, bMultipart);
        }
        val = getStringParameter("tpn_code");
        if (val != null && val.length() > 0) {
            tpTp.tpn_code = dbUtils.unicodeFrom(val, clientEnc, encoding, bMultipart);
        }
        val = getStringParameter("tpn_name");
        if (val != null && val.length() > 0) {
            tpTp.tpn_name = dbUtils.unicodeFrom(val, clientEnc, encoding, bMultipart);
        }
        val = getStringParameter("tpn_remark");
        if (val != null && val.length() > 0) {
            tpTp.tpn_remark = dbUtils.unicodeFrom(val, clientEnc, encoding, bMultipart);
        }

        val = getStringParameter("tpn_introduction");
        if (val != null && val.length() > 0) {
        	tpTp.tpn_introduction = dbUtils.unicodeFrom(val, clientEnc, encoding, bMultipart);
        }
        val = getStringParameter("tpn_aim");
        if (val != null && val.length() > 0) {
        	tpTp.tpn_aim = dbUtils.unicodeFrom(val, clientEnc, encoding, bMultipart);
        }
        val = getStringParameter("tpn_target");
        if (val != null && val.length() > 0) {
            tpTp.tpn_target = dbUtils.unicodeFrom(val, clientEnc, encoding, bMultipart);
        }
        val = getStringParameter("tpn_responser");
        if (val != null && val.length() > 0) {
            tpTp.tpn_responser = dbUtils.unicodeFrom(val, clientEnc, encoding, bMultipart);
        }


        // get current training center id
        tcr_id = getLongParameter("tcr_id");
        if (tcr_id == LONG_PARAMETER_NOT_FOUND) {
            tcr_id = 0;
        }
        tpn_id = getLongParameter("tpn_id");
        if (tpn_id == LONG_PARAMETER_NOT_FOUND) {
            tpn_id = 0;
        }

        entrance = getStringParameter("entrance");
    }

    public void getAddtpTrainingPlan() throws cwException {
        tcr_id = getLongParameter("tcr_id");
    }

    public void getUpLoadYearPrep() throws cwException {
        tcr_id = getLongParameter("tcr_id");
        year = getLongParameter("year");
        ypn_year = getTimestampParameter("ypn_year");
        upd_timestamp = getTimestampParameter("upd_timestamp");
    }

    public void getUploadYearConfirm() throws cwException {
        tcr_id = getLongParameter("tcr_id");
        year = getLongParameter("year");
        ypn_year = getTimestampParameter("ypn_year");
        src_filename = unicode(getStringParameter("src_filename"));
        upd_timestamp = getTimestampParameter("upd_timestamp");
    }

    public void tpPlanSearch() throws cwException {
        tcr_id = getLongParameter("tcr_id");
        status = getStringParameter("status");
        sear_code_name = unicode(getStringParameter("sear_code_name"));
    }

    public void tpUpdStaParam() throws cwException {
        tcr_id = getLongParameter("tcr_id");
        sel_tpn_id = getStringParameter("sel_tpn_id");
        tpn_id_list = cwUtils.splitToLong(sel_tpn_id, "~");
        sel_update_timestamp = getStringParameter("sel_update_timestamp");
        String[] temp = cwUtils.splitToString(sel_update_timestamp, "~");
        update_timestamp = new Timestamp[temp.length];
        for (int i = 0; i < temp.length; i++) {
            update_timestamp[i] = Timestamp.valueOf(temp[i]);
        }
    	status = getStringParameter("status");
    	plantype = getStringParameter("type");
    	sel_tcr_id = getStringParameter("sel_tcr_id");
    	tcr_id_list = cwUtils.splitToLong(sel_tcr_id, "~");
    }
    
    public void getPageParam() {
		String var;
		String prmMn;
		
		prmMn = "order_by";
	    var = req.getParameter(prmMn);
	    if(var !=null && var.length() > 0) {
		    this.cwPage.sortOrder = cwPagination.esc4SortSql(var);
	    }
	    prmMn = "sort_by";
		var = req.getParameter(prmMn);
		if(var != null && var.length() > 0) {
			this.cwPage.sortCol = cwPagination.esc4SortSql(var);
		}
    }
}
