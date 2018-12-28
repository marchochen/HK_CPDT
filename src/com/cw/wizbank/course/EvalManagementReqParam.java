/**
 * @author Christ Qiu
 * ReqParam for "Evaluation Management"
 * a class to receive front-end data(params)
 * and do nothing else just to get,store and process
 * front-end input or front-end income
 */

package com.cw.wizbank.course;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletRequest;

import com.cw.wizbank.ReqParam;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.db.DbMeasurementEvaluation;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.util.cwUtils;

public class EvalManagementReqParam extends ReqParam {

	private String tmpUploadDir;
	public String[] all_cmt_id;
	// attd_rate var
	public String src_filename = null;
	public boolean download;
	public int att_status;
	public String att_rate;
	public String att_rate_remark;
	public long app_id;
	public long[] app_id_lst;
	public String remark;
	public boolean show_approval_ent_only = false;
	public String msg_subject_1;
	public String msg_subject_2;
	public String msg_subject_3;
	public long cmt_id;
	public long itm_id;
	public long ils_id;
	public String user_code;
	public EvalManagement evalmgt = new EvalManagement();
    private static WizbiniLoader wizbini = null;
    
	public EvalManagementReqParam(
		ServletRequest inReq,
		String clientEnc_,
		String encoding_)
		throws cwException {
		this.req = inReq;
		this.clientEnc = clientEnc_;
		this.encoding = encoding_;
		super.common();
	}
	public void getCmtId() throws cwException, UnsupportedEncodingException {
		this.cmt_id = getLongParameter("cmt_id");
	}
	public void getItmId() throws cwException, UnsupportedEncodingException {
		this.itm_id = getLongParameter("itm_id");
	}
	
	public String getLrnName() throws cwException, UnsupportedEncodingException {
		return unicode(getStringParameter("lrn_name"));
	}
    
	public String getLrnEntId() throws cwException, UnsupportedEncodingException {
		return unicode(getStringParameter("lrn_ent_id"));
	}

	public DbMeasurementEvaluation get4UpdScore() throws cwException {
		DbMeasurementEvaluation MeasureEval = new DbMeasurementEvaluation();
		MeasureEval.setMtv_ent_id(getLongParameter("lrn_ent_id"));
		MeasureEval.setMtv_cmt_id(getLongParameter("cmt_id"));
		MeasureEval.setMtv_tkh_id(getLongParameter("cmt_tkh_id"));
		MeasureEval.setMtv_score(getFloatParameter("cmt_score"));
		MeasureEval.setCmt_max_score(getFloatParameter("cmt_max_score"));
        MeasureEval.app_id = getLongParameter("app_id");
		return MeasureEval;
	}
	public void uploadAttdRate() throws cwException {
		this.src_filename = unicode(getStringParameter("src_filename"));
		this.itm_id = getLongParameter("itm_id");
	}
	public void updAttdRate(String clientEnc, String env_encoding)
		throws cwException {
		String var;
		this.itm_id = getLongParameter("itm_id");
		this.att_rate = getStringParameter("attd_rate");
		this.att_rate_remark = unicode(getStringParameter("attd_remark"));
		var = req.getParameter("app_id_lst");
		if (var != null && var.length() != 0)
			app_id_lst = cwUtils.splitToLong(var, "~");
		else
			app_id_lst = null;
		var = req.getParameter("first_no_show_subject");
		if (var != null && var.length() != 0)
			msg_subject_1 = var;

		var = req.getParameter("second_no_show_subject");
		if (var != null && var.length() != 0)
			msg_subject_2 = var;

		var = req.getParameter("no_show_subject");
		if (var != null && var.length() != 0)
			msg_subject_3 = var;

	}
	public void attendance(String clientEnc, String env_encoding)
		throws cwException {
		String var;

		this.itm_id = getLongParameter("itm_id");
		var = req.getParameter("att_status");
		if (var != null && var.length() > 0) {
			try {
				att_status = Integer.parseInt(var);
			} catch (NumberFormatException e) {
				att_status = -1;
			}
		} else {
			att_status = 0;
		}

		var = req.getParameter("app_id");
		if (var != null && var.length() > 0) {
			try {
				app_id = Long.parseLong(var);
			} catch (NumberFormatException e) {
				app_id = 0;
			}
		} else {
			app_id = 0;
		}
		
		var = req.getParameter("ils_id");
		if (var != null && var.length() > 0) {
			try {
				ils_id = Long.parseLong(var);
			} catch (NumberFormatException e) {
				ils_id = 0;
			}
		} else {
			ils_id = 0;
		}
		
		var = req.getParameter("download");
		if (var != null && var.length() != 0)
			download = Boolean.valueOf(var).booleanValue();
		else
			download = false;
		try {
			var = req.getParameter("remark");
			if (var != null && var.length() > 0) {
				remark =
					dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);
			}
		} catch (UnsupportedEncodingException e) {
			remark = null;
		}
		
		var =req.getParameter("user_code");
		if(var!=null&&var.length()!=0)
			this.user_code=var;
		else 
			this.user_code=null;
		return;
	}
    public void getAllCmtId(ServletRequest request) throws cwException, IOException {
        //boolean bMultiPart = false;
        String conType = request.getContentType();
        this.all_cmt_id = (bMultipart) ? multi.getParameterValues("all_cmt_id"):request.getParameterValues("all_cmt_id");
    }
    public void getSrcFilename() throws cwException{
        this.src_filename = getStringParameter("src_filename");
    }
}
