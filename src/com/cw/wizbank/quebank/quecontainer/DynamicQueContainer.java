package com.cw.wizbank.quebank.quecontainer;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;

import com.cw.wizbank.qdb.dbResource;
import com.cw.wizbank.qdb.dbResourceContent;
import com.cw.wizbank.qdb.dbQuestion;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.dbTemplate;
import com.cw.wizbank.qdb.dbResourcePermission;
import com.cw.wizbank.qdb.dbObjective;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.db.DbQueContainerSpec;
import com.cw.wizbank.db.view.ViewQueContainer;

public class DynamicQueContainer extends dbResource {
    public String qct_select_logic;
    public int qct_allow_shuffle_ind = 0;

    public DynamicQueContainer() {}

    public void get(Connection con) throws qdbException, cwSysMessage {
        ViewQueContainer myViewQueContainer = new ViewQueContainer();
        myViewQueContainer.res_id = res_id;
        myViewQueContainer.get(con);
        qct_select_logic = myViewQueContainer.qct_select_logic;
        qct_allow_shuffle_ind = myViewQueContainer.qct_allow_shuffle_ind;

        super.get(con);
    }

    public void ins(Connection con, long obj_id, loginProfile prof) throws qdbException, qdbErrMessage {
        String[] robs = new String[1];
        robs[0] = Long.toString(obj_id);
        super.ins_res(con, robs, prof);

        ViewQueContainer myViewQueContainer = new ViewQueContainer();
        myViewQueContainer.res_id = res_id;
        myViewQueContainer.qct_select_logic = qct_select_logic;
        myViewQueContainer.qct_allow_shuffle_ind = qct_allow_shuffle_ind;
        myViewQueContainer.ins(con);
    }

    public void upd(Connection con, loginProfile prof) throws qdbException, qdbErrMessage, cwSysMessage {
        super.upd_res(con, prof, true);

        ViewQueContainer myViewQueContainer = new ViewQueContainer();
        myViewQueContainer.res_id = res_id;
        myViewQueContainer.qct_select_logic = qct_select_logic;
        myViewQueContainer.qct_allow_shuffle_ind = qct_allow_shuffle_ind;
        myViewQueContainer.upd(con);
    }

    public void del_container(Connection con, loginProfile prof) throws qdbException, qdbErrMessage, cwSysMessage, SQLException {
        ViewQueContainer myViewQueContainer = new ViewQueContainer();
        myViewQueContainer.res_id = res_id;
        myViewQueContainer.del(con);

        DbQueContainerSpec myDbQueContainerSpec = new DbQueContainerSpec();
        myDbQueContainerSpec.qcs_res_id = res_id;
        myDbQueContainerSpec.del_frm_res_id(con);
    }

    public void del(Connection con, loginProfile prof) throws qdbException, qdbErrMessage, cwSysMessage, SQLException {
        del_container(con, prof);
        super.del_res(con, prof);
    }

    public void addSpec(Connection con, String spec_type, long spec_score, long spec_difficulty, String spec_privilege, float spec_duration, long spec_qcount, long spec_obj_id, String usr_id) throws cwSysMessage, qdbException {
        DbQueContainerSpec myDbQueContainerSpec = new DbQueContainerSpec();
        myDbQueContainerSpec.qcs_type = spec_type;
        myDbQueContainerSpec.qcs_score = spec_score;
        myDbQueContainerSpec.qcs_difficulty = spec_difficulty;
        myDbQueContainerSpec.qcs_privilege = spec_privilege;
        myDbQueContainerSpec.qcs_duration = spec_duration;
        myDbQueContainerSpec.qcs_qcount = spec_qcount;
        myDbQueContainerSpec.qcs_obj_id = spec_obj_id;
        myDbQueContainerSpec.qcs_res_id = res_id;

        myDbQueContainerSpec.ins(con, res_subtype, usr_id);

        super.updateTimeStamp(con);
    }

    public void addSpec(Connection con, Vector vSpec, String usr_id) throws cwSysMessage, qdbException {
        for (int i = 0; i < vSpec.size(); i++) {
            DbQueContainerSpec criterion = (DbQueContainerSpec)vSpec.elementAt(i);
            criterion.qcs_res_id = this.res_id;
            criterion.ins(con, this.res_subtype, usr_id);
        }
        if (vSpec.size() > 0) {
            super.updateTimeStamp(con);
        }
    }

    public void updSpec(Connection con, long spec_id, String spec_type, long spec_score, long spec_difficulty, String spec_privilege, float spec_duration, long spec_qcount, String usr_id) throws cwSysMessage, qdbException {
        DbQueContainerSpec myDbQueContainerSpec = new DbQueContainerSpec();
        myDbQueContainerSpec.qcs_id = spec_id;
        myDbQueContainerSpec.qcs_type = spec_type;
        myDbQueContainerSpec.qcs_score = spec_score;
        myDbQueContainerSpec.qcs_difficulty = spec_difficulty;
        myDbQueContainerSpec.qcs_privilege = spec_privilege;
        myDbQueContainerSpec.qcs_duration = spec_duration;
        myDbQueContainerSpec.qcs_qcount = spec_qcount;

        myDbQueContainerSpec.upd(con, res_subtype, usr_id);

        super.updateTimeStamp(con);
    }

    public void removeSpec(Connection con, long spec_id) throws cwSysMessage, qdbException {
        DbQueContainerSpec myDbQueContainerSpec = new DbQueContainerSpec();
        myDbQueContainerSpec.qcs_id = spec_id;

        myDbQueContainerSpec.del(con, res_subtype);

        super.updateTimeStamp(con);
    }

    public String getSpecAsXML(Connection con, long qcs_id, loginProfile prof) throws qdbException, cwSysMessage, cwException, SQLException {
        String result = "";

        boolean bTemplate = true;
        String dpo_view = null;

        ViewQueContainer myViewQueContainer = new ViewQueContainer();
        myViewQueContainer.res_id = res_id;
        myViewQueContainer.get(con);

        // xml header
        result += dbUtils.xmlHeader;
        result += "<dynamic_que_container id=\"" + res_id + "\" language=\"" + res_lan + "\" last_modified=\"" + res_upd_user + "\" timestamp=\"" + res_upd_date + "\" owner=\"" + dbUtils.esc4XML(dbRegUser.usrId2SteUsrId(con, res_usr_id_owner)) + "\">" + dbUtils.NEWL;
        // author's information
        result += prof.asXML() + dbUtils.NEWL;

        result += "<cur_time>" + dbUtils.getTime(con) + "</cur_time>";
        result += "<header type=\"" + res_type + "\" subtype=\"" + res_subtype + "\" difficulty=\"" + res_difficulty + "\" duration=\"" + res_duration + "\" privilege=\"" + res_privilege + "\" status=\"" + res_status + "\" allow_shuffle_ind=\"" + Integer.toString(myViewQueContainer.qct_allow_shuffle_ind) + "\" selection_logic=\"" + myViewQueContainer.qct_select_logic + "\">" + dbUtils.NEWL;
        result += objAsXML(con);
        if (bTemplate) {
            result += "<template_list>" + dbUtils.NEWL;
            result += dbTemplate.tplListContentXML(con, prof, res_subtype);
            result += "</template_list>" + dbUtils.NEWL;
        }
        result += "</header>" + dbUtils.NEWL + dbUtils.NEWL;

        result += getDisplayOption(con, dpo_view);

        result += "<body>" + dbUtils.NEWL;
        result += "<title>" + dbUtils.esc4XML(res_title) + "</title>" + dbUtils.NEWL;
        result += "<desc>" + dbUtils.esc4XML(res_desc) + "</desc>" + dbUtils.NEWL;
        result += "</body>" + dbUtils.NEWL;
        result += dbResourcePermission.aclAsXML(con, res_id, prof);

        DbQueContainerSpec myDbQueContainerSpec = new DbQueContainerSpec();
        myDbQueContainerSpec.qcs_id = qcs_id;
        myDbQueContainerSpec.get(con);
        result += myDbQueContainerSpec.asXML(con, prof);

        result += "</dynamic_que_container>" + dbUtils.NEWL;

        return result;
    }

    public String asXML(Connection con, loginProfile prof, String mode) throws qdbException, cwSysMessage, cwException, SQLException {
        return asXML(con, prof, true, mode);
    }

    public String asXML(Connection con, loginProfile prof, boolean bShowHeader, String mode) throws qdbException, cwSysMessage, cwException, SQLException {
        String result = "";

        boolean bTemplate = true;
        String dpo_view = null;

        ViewQueContainer myViewQueContainer = new ViewQueContainer();
        myViewQueContainer.res_id = res_id;
        myViewQueContainer.get(con);

        DbQueContainerSpec myDbQueContainerSpec = new DbQueContainerSpec();
        Vector vtDbQueContainerSpec = myDbQueContainerSpec.getQueContainerSpecs(con, res_id);

        long total_score = 0;
        for (int i = 0; i < vtDbQueContainerSpec.size(); i++) {
            DbQueContainerSpec tempDbQueContainerSpec = (DbQueContainerSpec)vtDbQueContainerSpec.elementAt(i);
            total_score += (tempDbQueContainerSpec.qcs_score * tempDbQueContainerSpec.qcs_qcount);
        }

        // xml header
        if (bShowHeader) {
            result += dbUtils.xmlHeader;
        }
        result += "<dynamic_que_container id=\"" + res_id + "\" language=\"" + res_lan + "\" last_modified=\"" + res_upd_user + "\" timestamp=\"" + res_upd_date + "\" owner=\"" + dbUtils.esc4XML(dbRegUser.usrId2SteUsrId(con, res_usr_id_owner)) + "\" view_mode=\"" + mode + "\">" + dbUtils.NEWL;
        // author's information
        if (bShowHeader) {
            result += prof.asXML() + dbUtils.NEWL;
        }
        result += "<cur_time>" + dbUtils.getTime(con) + "</cur_time>";
        result += "<header type=\""
            + res_type
            + "\" subtype=\""
            + res_subtype
            + "\" difficulty=\""
            + res_difficulty
            + "\" duration=\""
            + res_duration
            + "\" privilege=\""
            + res_privilege
            + "\" status=\""
            + res_status
            + "\" allow_shuffle_ind=\""
            + Integer.toString(myViewQueContainer.qct_allow_shuffle_ind)
            + "\" selection_logic=\""
            + myViewQueContainer.qct_select_logic
            + "\" total_score=\""
            + total_score
            + "\">"
            + dbUtils.NEWL;
        result += objAsXML(con);
        if (bTemplate) {
            result += "<template_list>" + dbUtils.NEWL;
            result += dbTemplate.tplListContentXML(con, prof, res_subtype);
            result += "</template_list>" + dbUtils.NEWL;
        }
        result += "</header>" + dbUtils.NEWL + dbUtils.NEWL;

        result += getDisplayOption(con, dpo_view);

        result += "<body>" + dbUtils.NEWL;
        result += "<title>" + dbUtils.esc4XML(res_title) + "</title>" + dbUtils.NEWL;
        result += "<desc>" + dbUtils.esc4XML(res_desc) + "</desc>" + dbUtils.NEWL;
        result += "</body>" + dbUtils.NEWL;

        //creation details
        dbRegUser creator = new dbRegUser();
        creator.get(con, res_usr_id_owner);
        result += "<creation>";
        result += "<user id=\"" + creator.usr_ste_usr_id + "\"";
        result += " ent_id=\"" + creator.usr_ent_id + "\">";
        result += "<display_bil>" + cwUtils.esc4XML(creator.usr_display_bil) + "</display_bil>";
        result += "</user>";
        result += "<timestamp>" + this.res_create_date + "</timestamp>";
        result += "</creation>";

        //last update details
        dbRegUser lastAuthor = new dbRegUser();
        lastAuthor.get(con, this.res_upd_user);
        result += "<last_update>";
        result += "<user id=\"" + lastAuthor.usr_ste_usr_id + "\"";
        result += " ent_id=\"" + lastAuthor.usr_ent_id + "\">";
        result += "<display_bil>" + cwUtils.esc4XML(lastAuthor.usr_display_bil) + "</display_bil>";
        result += "</user>";
        result += "<timestamp>" + this.res_upd_date + "</timestamp>";
        result += "</last_update>";

        result += dbResourcePermission.aclAsXML(con, res_id, prof);

        for (int i = 0; i < vtDbQueContainerSpec.size(); i++) {
            DbQueContainerSpec tempDbQueContainerSpec = (DbQueContainerSpec)vtDbQueContainerSpec.elementAt(i);
            result += tempDbQueContainerSpec.asXML(con, prof);
        }

        result += "</dynamic_que_container>" + dbUtils.NEWL;

        return result;
    }

    public void delAssessment(Connection con, String strResFolder) throws qdbException, cwSysMessage, qdbErrMessage {
        Vector queLst = new Vector();
        dbResourceContent resCon = new dbResourceContent();
        queLst = dbResourceContent.getChildInfo(con, res_id);
        for (int i = 0; i < queLst.size(); i++) {
            resCon = (dbResourceContent)queLst.elementAt(i);
            dbQuestion dbque = new dbQuestion();
            if (resCon.rcn_temp_res_type.equals("FSC") || resCon.rcn_temp_res_type.equals("DSC")) {
                DynamicQueContainer queContainer = new DynamicQueContainer();
                queContainer.res_id = resCon.rcn_res_id_content;
                queContainer.delAssessment(con, strResFolder);
            }else {
                dbque.que_res_id = resCon.rcn_res_id_content;
                dbque.del(con);
            }
        }
        dbObjective dbobj = new dbObjective();
        dbobj.delRes(con, res_id, strResFolder);
    }

}