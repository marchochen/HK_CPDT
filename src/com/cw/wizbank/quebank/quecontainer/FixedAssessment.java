package com.cw.wizbank.quebank.quecontainer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

import java.util.Vector;

import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.dbModule;
import com.cw.wizbank.qdb.dbQuestion;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.dbResourceContent;
import com.cw.wizbank.qdb.dbResourcePermission;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.dbTemplate;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.db.DbQueContainerSpec;
import com.cw.wizbank.db.DbTrainingCenter;
import com.cw.wizbank.db.view.ViewQueContainer;
import com.cw.wizbank.db.view.ViewResources;

public class FixedAssessment extends FixedQueContainer {

    public FixedAssessment() {}

    public void addObj(Connection con, long objId, String res_upd_user) throws qdbException, qdbErrMessage, cwSysMessage {
        ViewQueContainer myViewQueContainer = new ViewQueContainer();
        myViewQueContainer.res_id = res_id;
        myViewQueContainer.insObj(con, objId, res_upd_user);
    }

    public void delObj(Connection con, long objId, loginProfile prof) throws SQLException, qdbException, qdbErrMessage, cwSysMessage {
        DbQueContainerSpec myDbQueContainerSpec = new DbQueContainerSpec();
        myDbQueContainerSpec.qcs_res_id = res_id;
        myDbQueContainerSpec.qcs_obj_id = objId;
        myDbQueContainerSpec.del_frm_res_obj_id(con);

        // delete all the question with this objective
        PreparedStatement stmt1 = con.prepareStatement(" SELECT rcn_res_id_content FROM ResourceContent " + " where rcn_res_id = ? and rcn_obj_id_content = ? ");

        stmt1.setLong(1, res_id);
        stmt1.setLong(2, objId);

        ResultSet rs1 = stmt1.executeQuery();
        dbResourceContent resCon = new dbResourceContent();

        while (rs1.next()) {
            resCon.rcn_res_id = res_id;
            resCon.rcn_res_id_content = rs1.getLong("rcn_res_id_content");

            resCon.del(con);

            dbQuestion dbque = new dbQuestion();
            dbque.que_res_id = resCon.rcn_res_id_content;
            dbque.res_id = resCon.rcn_res_id_content;
            dbque.get(con);
            if (dbque.res_subtype.equalsIgnoreCase(RES_SUBTYPE_FSC)) {
                FixedScenarioQue myFixedScenarioQue = new FixedScenarioQue();
                myFixedScenarioQue.res_id = resCon.rcn_res_id_content;
                myFixedScenarioQue.del(con, prof);
            } else if (dbque.res_subtype.equalsIgnoreCase(RES_SUBTYPE_DSC)) {
                DynamicScenarioQue myDynamicScenarioQue = new DynamicScenarioQue();
                myDynamicScenarioQue.res_id = resCon.rcn_res_id_content;
                myDynamicScenarioQue.del(con, prof);
            } else {
                dbque.res_upd_user = prof.usr_id;
                dbque.del(con);
            }

        }
        cwSQL.cleanUp(rs1, stmt1);
        this.res_upd_user = prof.usr_id;
        super.updateTimeStamp(con);
    }

    public String getObjAsXML(Connection con, loginProfile prof, String mode) throws qdbException, cwSysMessage, cwException, SQLException {
        String result = "";

        boolean bTemplate = true;
        String dpo_view = null;

        ViewQueContainer myViewQueContainer = new ViewQueContainer();
        myViewQueContainer.res_id = res_id;
        myViewQueContainer.get(con);

        // xml header
        result += dbUtils.xmlHeader;
        result += "<fixed_assessment id=\"" + res_id + "\" language=\"" + res_lan + "\" last_modified=\"" + res_upd_user + "\" timestamp=\"" + res_upd_date + "\" owner=\"" + dbUtils.esc4XML(dbRegUser.usrId2SteUsrId(con, res_usr_id_owner)) + "\" view_mode=\"" + mode + "\">" + dbUtils.NEWL;
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
        long tcr_id = DbTrainingCenter.getResTopFolderTcrId(con, res_id);
        result += "<training_center id=\"" + tcr_id +"\"/>" + dbUtils.NEWL;
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

        // use the Module object to generate the objective XML
        result += dbModule.tstObjLstAsXML(con, res_id);

        result += "</fixed_assessment>" + dbUtils.NEWL;

        return result;
    }

    /**
     * Get child question id.
     * @param con
     * @return Vector of question id in Long.
     * @throws SQLException
     */
    public Vector getChildQueId(Connection con) throws SQLException, cwSysMessage {
        ViewResources viewRes = new ViewResources();
        Vector v_que_id = viewRes.getChildQueId(con, this.res_id);
        if (this.qct_allow_shuffle_ind == 1) {
            v_que_id = cwUtils.randomVec(v_que_id);
        }
        return v_que_id;
    }

}