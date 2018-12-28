package com.cw.wizbank.quebank;

import javax.servlet.ServletRequest;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import com.cw.wizbank.qdb.dbTemplate;

import com.cw.wizbank.db.DbQueContainerSpec;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.ServletUtils;

import com.cw.wizbank.ReqParam;
import com.cw.wizbank.util.cwException;

// public class for processing any parameters at the request URL
// constructor requires a servlet request object
// method "common()" for the set of shared parameters
// add your own method for your own set of parameters
public class QueBankReqParam extends ReqParam {
    //	private ServletRequest req;
    //	private String clientEnc;
    //	private String encoding;
    //	private MultipartRequest multi;
    //	private boolean bMultiPart = false;

    public long res_id;
    public String res_type;
    public String res_subtype;
    public long obj_id;
    public String res_title;
    public String res_desc;
    public float res_duration;
    public String res_lan;
    public int res_difficulty;
    public String res_privilege;
    public String res_status;
    public String[] que_id_lst;
    public String[] que_order_lst;
    public Timestamp res_upd_timestamp;
    public Timestamp res_timestamp;

    public long qcs_id = 0;
    public String qcs_type;
    public long qcs_score = 0;
    public long qcs_difficulty = 0;
    public String qcs_privilege;
    public float qcs_duration = 0;
    public long qcs_qcount = 0;
    public long qcs_obj_id = 0;

    public String qct_select_logic;
    public int qct_allow_shuffle_ind = 0;

    // for template
    public String dpo_view = null;
    public long course_id = 0;
    public dbTemplate dbtpl = null;

    public String mode;

    ServletUtils sutils = new ServletUtils();

    public QueBankReqParam(boolean bMultiPart_, ServletRequest inReq, MultipartRequest multi_, String clientEnc_, String encoding_) {
        this.req = inReq;
        this.clientEnc = clientEnc_;
        this.encoding = encoding_;
        this.bMultipart = bMultiPart_;
        this.multi = multi_;
        common();
    }

    // common parameters needed in all commands
    public void common() {
        String var;

        // command
        var = (bMultipart) ? multi.getParameter("cmd") : req.getParameter("cmd");
        if (var != null && var.length() > 0)
            cmd = var;
        else
            cmd = null;
        // stylesheet filename
        var = (bMultipart) ? multi.getParameter("stylesheet") : req.getParameter("stylesheet");
        if (var != null && var.length() > 0)
            stylesheet = var;
        else
            stylesheet = null;
        // url success
        var = (bMultipart) ? multi.getParameter("url_success") : req.getParameter("url_success");
        if (var != null && var.length() > 0)
            url_success = var;
        else
            url_success = null;
        // url failure
        var = (bMultipart) ? multi.getParameter("url_failure") : req.getParameter("url_failure");
        if (var != null && var.length() > 0)
            url_failure = var;
        else
            url_failure = null;
    }
    //parameters needed in question container
    public void que_container() throws UnsupportedEncodingException, cwException {
        String var;

        var = (bMultipart) ? multi.getParameter("res_id") : req.getParameter("res_id");
        if (var != null && var.length() > 0) {
            res_id = Integer.parseInt(var);
        } else {
            res_id = -1;
        }

        var = (bMultipart) ? multi.getParameter("res_type") : req.getParameter("res_type");
        if (var != null && var.length() > 0) {
            res_type = var;
        } else {
            res_type = null;
        }

        var = (bMultipart) ? multi.getParameter("res_subtype") : req.getParameter("res_subtype");
        if (var != null && var.length() > 0) {
            res_subtype = var;
        } else {
            res_subtype = null;
        }

        var = (bMultipart) ? multi.getParameter("obj_id") : req.getParameter("obj_id");
        if (var != null && var.length() > 0) {
            obj_id = Integer.parseInt(var);
        } else {
            obj_id = -1;
        }

        var = (bMultipart) ? multi.getParameter("res_title") : req.getParameter("res_title");
        if (var != null && var.length() > 0) {
            res_title = unicode(var);
        } else {
            res_title = null;
        }

        var = (bMultipart) ? multi.getParameter("res_desc") : req.getParameter("res_desc");
        if (var != null && var.length() > 0) {
            res_desc = unicode(var);
        } else {
            res_desc = null;
        }

        var = (bMultipart) ? multi.getParameter("res_duration") : req.getParameter("res_duration");
        if (var != null && var.length() > 0) {
            res_duration = Float.valueOf(var).floatValue();
        } else {
            res_duration = -1;
        }

        var = (bMultipart) ? multi.getParameter("res_lan") : req.getParameter("res_lan");
        if (var != null && var.length() > 0) {
            res_lan = var;
        } else {
            res_lan = null;
        }

        var = (bMultipart) ? multi.getParameter("res_privilege") : req.getParameter("res_privilege");
        if (var != null && var.length() > 0) {
            res_privilege = var;
        } else {
            res_privilege = null;
        }

        var = (bMultipart) ? multi.getParameter("res_status") : req.getParameter("res_status");
        if (var != null && var.length() > 0) {
            res_status = var;
        } else {
            res_status = null;
        }

        var = (bMultipart) ? multi.getParameter("res_difficulty") : req.getParameter("res_difficulty");
        if (var != null && var.length() > 0) {
            res_difficulty = Integer.parseInt(var);
        } else {
            res_difficulty = -1;
        }

        var = (bMultipart) ? multi.getParameter("qct_allow_shuffle_ind") : req.getParameter("qct_allow_shuffle_ind");
        if (var != null && var.length() > 0) {
            if (Integer.parseInt(var) == 1) {
                qct_allow_shuffle_ind = 1;
            } else {
                qct_allow_shuffle_ind = 0;
            }
        } else {
            qct_allow_shuffle_ind = 0;
        }

        var = (bMultipart) ? multi.getParameter("qct_select_logic") : req.getParameter("qct_select_logic");
        if (var != null && var.length() > 0) {
            qct_select_logic = var;
        } else {
            qct_select_logic = null;
        }

        var = (bMultipart) ? multi.getParameter("res_timestamp") : req.getParameter("res_timestamp");
        if (var != null && var.length() > 0) {
            res_timestamp = Timestamp.valueOf(var);
        } else {
            res_timestamp = null;
        }

    }

    //parameters needed in fixed question container
    public void fixed_que_container() throws UnsupportedEncodingException, cwException {
        String var;

        que_container();

        var = (bMultipart) ? multi.getParameter("que_id_lst") : req.getParameter("que_id_lst");
        if (var != null && var.length() > 0) {
            que_id_lst = sutils.split(var, "~");
        } else {
            que_id_lst = null;
        }

        var = (bMultipart) ? multi.getParameter("mode") : req.getParameter("mode");
        if (var != null && var.length() > 0) {
            mode = var;
        }

    }

    //parameters needed in dynamic question container
    public void dynamic_que_container() throws UnsupportedEncodingException, cwException {
        String var;

        que_container();

        var = (bMultipart) ? multi.getParameter("qcs_id") : req.getParameter("qcs_id");
        if (var != null && var.length() > 0) {
            qcs_id = Long.parseLong(var);
        } else {
            qcs_id = 0;
        }

        var = (bMultipart) ? multi.getParameter("res_id") : req.getParameter("res_id");
        if (var != null && var.length() > 0) {
            res_id = Long.parseLong(var);
        } else {
            res_id = 0;
        }

        var = (bMultipart) ? multi.getParameter("qcs_type") : req.getParameter("qcs_type");
        if (var != null && var.length() > 0) {
            qcs_type = var;
        } else {
            qcs_type = null;
        }

        var = (bMultipart) ? multi.getParameter("qcs_score") : req.getParameter("qcs_score");
        if (var != null && var.length() > 0) {
            qcs_score = Long.parseLong(var);
        } else {
            qcs_score = 0;
        }

        var = (bMultipart) ? multi.getParameter("qcs_difficulty") : req.getParameter("qcs_difficulty");
        if (var != null && var.length() > 0) {
            qcs_difficulty = Long.parseLong(var);
        } else {
            qcs_difficulty = 0;
        }

        var = (bMultipart) ? multi.getParameter("qcs_privilege") : req.getParameter("qcs_privilege");
        if (var != null && var.length() > 0) {
            qcs_privilege = var;
        } else {
            qcs_privilege = null;
        }

        var = (bMultipart) ? multi.getParameter("qcs_duration") : req.getParameter("qcs_duration");
        if (var != null && var.length() > 0) {
            qcs_duration = Float.parseFloat(var);
        } else {
            qcs_duration = DbQueContainerSpec.EMPTY;
        }

        var = (bMultipart) ? multi.getParameter("qcs_qcount") : req.getParameter("qcs_qcount");
        if (var != null && var.length() > 0) {
            qcs_qcount = Long.parseLong(var);
        } else {
            qcs_qcount = 0;
        }

        var = (bMultipart) ? multi.getParameter("qcs_obj_id") : req.getParameter("qcs_obj_id");
        if (var != null && var.length() > 0) {
            qcs_obj_id = Long.parseLong(var);
        } else {
            qcs_obj_id = 0;
        }

    }

    public void getFSCContent() {
        String var = null;

        var = (bMultipart) ? multi.getParameter("res_id") : req.getParameter("res_id");
        if (var != null && var.length() > 0) {
            this.res_id = Integer.parseInt(var);
        } else {
            this.res_id = 0;
        }
    }

    public void getDSCContent() {
        String var = null;

        var = (bMultipart) ? multi.getParameter("res_id") : req.getParameter("res_id");
        if (var != null && var.length() > 0) {
            this.res_id = Integer.parseInt(var);
        } else {
            this.res_id = 0;
        }
    }

    public void updFSCShuffleInd() {
        //res_id
        //qct_allow_shuffle_ind
        //res_timestamp
        String var = null;

        var = (bMultipart) ? multi.getParameter("res_id") : req.getParameter("res_id");
        if (var != null && var.length() > 0) {
            this.res_id = Integer.parseInt(var);
        } else {
            this.res_id = 0;
        }

        var = (bMultipart) ? multi.getParameter("res_upd_timestamp") : req.getParameter("res_upd_timestamp");
        if (var != null && var.length() > 0) {
            this.res_upd_timestamp = Timestamp.valueOf(var);
        } else {
            this.res_upd_timestamp = null;
        }

        var = (bMultipart) ? multi.getParameter("qct_allow_shuffle_ind") : req.getParameter("qct_allow_shuffle_ind");
        if (var != null && var.length() > 0) {
            this.qct_allow_shuffle_ind = Integer.parseInt(var);
        } else {
            this.qct_allow_shuffle_ind = 0;
        }

        return;
    }

    public void reorderQue() {
        String var = null;

        var = (bMultipart) ? multi.getParameter("res_id") : req.getParameter("res_id");
        if (var != null && var.length() > 0) {
            this.res_id = Integer.parseInt(var);
        } else {
            this.res_id = 0;
        }

        var = (bMultipart) ? multi.getParameter("res_upd_timestamp") : req.getParameter("res_upd_timestamp");
        if (var != null && var.length() > 0) {
            this.res_upd_timestamp = Timestamp.valueOf(var);
        } else {
            this.res_upd_timestamp = null;
        }

        var = (bMultipart) ? multi.getParameter("que_id_lst") : req.getParameter("que_id_lst");
        if (var != null && var.length() > 0) {
            this.que_id_lst = sutils.split(var, "~");
        } else {
            this.que_id_lst = null;
        }

        var = (bMultipart) ? multi.getParameter("que_order_lst") : req.getParameter("que_order_lst");
        if (var != null && var.length() > 0) {
            this.que_order_lst = sutils.split(var, "~");
        } else {
            this.que_order_lst = null;
        }

        return;
    }

    public void getDSCCritSpec() {
        String var = null;

        var = (bMultipart) ? multi.getParameter("qcs_id") : req.getParameter("qcs_id");
        if (var != null && var.length() > 0) {
            qcs_id = Long.parseLong(var);
        } else {
            qcs_id = 0;
        }

        var = (bMultipart) ? multi.getParameter("res_id") : req.getParameter("res_id");
        if (var != null && var.length() > 0) {
            res_id = Integer.parseInt(var);
        } else {
            res_id = -1;
        }
    }

    public void updDSCCritSpec() throws UnsupportedEncodingException {
        String var = null;

        var = (bMultipart) ? multi.getParameter("res_id") : req.getParameter("res_id");
        if (var != null && var.length() > 0) {
            res_id = Integer.parseInt(var);
        } else {
            res_id = -1;
        }

        var = (bMultipart) ? multi.getParameter("res_type") : req.getParameter("res_type");
        if (var != null && var.length() > 0) {
            res_type = var;
        } else {
            res_type = null;
        }

        var = (bMultipart) ? multi.getParameter("res_subtype") : req.getParameter("res_subtype");
        if (var != null && var.length() > 0) {
            res_subtype = var;
        } else {
            res_subtype = null;
        }

        var = (bMultipart) ? multi.getParameter("qcs_id") : req.getParameter("qcs_id");
        if (var != null && var.length() > 0) {
            qcs_id = Long.parseLong(var);
        } else {
            qcs_id = 0;
        }

        var = (bMultipart) ? multi.getParameter("qcs_type") : req.getParameter("qcs_type");
        if (var != null && var.length() > 0) {
            qcs_type = var;
        } else {
            qcs_type = null;
        }

        var = (bMultipart) ? multi.getParameter("qcs_score") : req.getParameter("qcs_score");
        if (var != null && var.length() > 0) {
            qcs_score = Long.parseLong(var);
        } else {
            qcs_score = 0;
        }

        var = (bMultipart) ? multi.getParameter("qcs_difficulty") : req.getParameter("qcs_difficulty");
        if (var != null && var.length() > 0) {
            qcs_difficulty = Long.parseLong(var);
        } else {
            qcs_difficulty = 0;
        }

        var = (bMultipart) ? multi.getParameter("qcs_privilege") : req.getParameter("qcs_privilege");
        if (var != null && var.length() > 0) {
            qcs_privilege = var;
        } else {
            qcs_privilege = null;
        }

        var = (bMultipart) ? multi.getParameter("qcs_duration") : req.getParameter("qcs_duration");
        if (var != null && var.length() > 0) {
            qcs_duration = Float.parseFloat(var);
        } else {
            qcs_duration = DbQueContainerSpec.EMPTY;
        }

        var = (bMultipart) ? multi.getParameter("qcs_qcount") : req.getParameter("qcs_qcount");
        if (var != null && var.length() > 0) {
            qcs_qcount = Long.parseLong(var);
        } else {
            qcs_qcount = 0;
        }

        var = (bMultipart) ? multi.getParameter("qcs_obj_id") : req.getParameter("qcs_obj_id");
        if (var != null && var.length() > 0) {
            qcs_obj_id = Long.parseLong(var);
        } else {
            qcs_obj_id = 0;
        }

    }

    public void get_tpl() throws cwException {
        String var = (bMultipart) ? multi.getParameter("dpo_view") : req.getParameter("dpo_view");
        if (var != null && var.length() > 0) {
            dpo_view = var;
        } else {
            dpo_view = null;
        }

        var = (bMultipart) ? multi.getParameter("course_id") : req.getParameter("course_id");
        if (var != null && var.length() > 0) {
            course_id = Long.parseLong(var);
        } else {
            course_id = 0;
        }

        var = (bMultipart) ? multi.getParameter("mod_id") : req.getParameter("mod_id");
        if (var != null && var.length() > 0) {
            res_id = Long.parseLong(var);
        }

        dbtpl = new dbTemplate();

        var = (bMultipart) ? multi.getParameter("tpl_type") : req.getParameter("tpl_type");
        if (var != null && var.length() > 0) {
            dbtpl.tpl_type = var;
        } else {
            dbtpl.tpl_type = null;
        }
    }

}