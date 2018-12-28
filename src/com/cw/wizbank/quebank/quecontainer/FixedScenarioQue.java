package com.cw.wizbank.quebank.quecontainer;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Vector;
import java.util.Hashtable;

import com.cw.wizbank.qdb.dbModule;
import com.cw.wizbank.qdb.dbQuestion;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.dbResource;
import com.cw.wizbank.qdb.dbResourceObjective;
import com.cw.wizbank.qdb.dbResourcePermission;
import com.cw.wizbank.qdb.dbTemplate;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.dbInteraction;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.db.view.ViewQueContainer;
import com.cw.wizbank.db.view.ViewResources;
import com.cw.wizbank.db.DbQueContainerSpec;

public class FixedScenarioQue extends FixedQueContainer {

    private dbQuestion que;

    public FixedScenarioQue() {}

    /**
    Update the Fixed Scenario Question
    @param con Connection to database
    @param robs String array of objective id
    @param prof loginProfile of the user
    @param dbque dbQuestion of the scenario question
    */
    public void upd(Connection con, String[] robs, loginProfile prof, dbQuestion dbque) throws qdbException, qdbErrMessage, cwSysMessage, SQLException {
        //get the original score
        this.que = new dbQuestion();
        this.que.que_res_id = dbque.que_res_id;
        this.que.res_id = dbque.res_id;
        this.que.get(con);
        int myScore = this.que.que_score;

        //update the question
        this.que = dbque;
        this.que.upd(con, robs, prof);

        //update back the score
        this.que.que_score = myScore;
        this.que.updScore(con);

        return;
    }

    /**
    Create a new Fixed Scenario Question
    @con Connection to database
    @robs String array of the question's objective id
    @prof loginProfile of the user
    @dbque dbQuestion of the this fixed scenario question
    */
    public void ins(Connection con, String[] robs, loginProfile prof, dbQuestion dbque) throws SQLException, qdbException, qdbErrMessage {

        //insert new question
        dbque.ins(con, robs, prof, RES_TYPE_QUE);
        loadQuestion(dbque);

        //insert into QueContainer
        ViewQueContainer queContainer = new ViewQueContainer();
        queContainer.res_id = dbque.res_id;
        queContainer.qct_select_logic = qct_select_logic;
        if (dbque.que_sc_shuffle) {
            queContainer.qct_allow_shuffle_ind = 1;
        }

        queContainer.ins(con);

        return;
    }

    /**
    Get the question and container details of a Fixed Scenario Question.
    Need to pre-define this.res_id
    @con Connection to database
    */
    public void get(Connection con) throws qdbException, cwSysMessage {
        //get question details
        this.que = new dbQuestion();
        this.que.res_id = this.res_id;
        this.que.que_res_id = this.res_id;
        this.que.get(con);

        //get container details
        super.get(con);
    }

    /**
    Get the question and container details of a Fixed Scenario Question.
    Need to pre-define this.res_id
    @con Connection to database
    @dbque given the dbQuestion of the Fixed Scenario Question so that the method will not get it again from database
    */
    public void get(Connection con, dbQuestion dbque) throws qdbException, cwSysMessage {
        //get question details
        this.que = dbque;

        //get container details
        super.get(con);
    }

    public void del(Connection con, loginProfile prof) throws qdbException, qdbErrMessage, cwSysMessage, SQLException {
        // delete the question container spec related records
        DbQueContainerSpec myDbQueContainerSpec = new DbQueContainerSpec();
        myDbQueContainerSpec.qcs_res_id = res_id;
        myDbQueContainerSpec.del_frm_res_id(con);

        // delete fixed question container
        del_container(con, prof);

        // delete the question object for the container itself
        dbQuestion myDbQuestion = new dbQuestion();
        myDbQuestion.que_res_id = res_id;
        myDbQuestion.res_upd_user = prof.usr_id;
        myDbQuestion.del(con);
    }

    public String queAsXML(Connection con, loginProfile prof, String mod_type, String cur_stylesheet) throws qdbException, cwSysMessage, SQLException {
        return queAsXML(con, prof, mod_type, true, cur_stylesheet);
    }

    /**
    Generate XML for this Fixed Scenario Question's meta information.
    Need to pre-define this.res_id
    @con Connection to database
    @prof loginProfile of the user
    @mod_type required for dbQuestion.queAsXML(Connection, loginProfile, mod_type)
    @showXmlHeader boolean to control show the xml header or not
    @return XML for this Fixed Scenario Question's meta information.
    */
    public String queAsXML(Connection con, loginProfile prof, String mod_type, boolean showXmlHeader, String cur_stylesheet) throws qdbException, cwSysMessage, SQLException {

        if (this.que == null) {
            get(con);
        }
        StringBuffer xmlBuf = new StringBuffer(this.que.queAsXML(con, prof, mod_type, showXmlHeader, cur_stylesheet));
        String containerXml = getContainerAttrXML();
        int index = xmlBuf.toString().indexOf("</header>");
        xmlBuf.insert(index, containerXml);
        return xmlBuf.toString();
    }

    public String asXML(Connection con, loginProfile prof) throws cwException, qdbException, SQLException {

        StringBuffer xmlBuf = new StringBuffer(super.asXML(con, prof, false));
        String queXML = getQuestionsXML(con);
        int index = xmlBuf.toString().indexOf("</que_container_extra_info>");
        xmlBuf.insert(index, queXML);
        return xmlBuf.toString();
    }

    public String detailsAsXML(Connection con, loginProfile prof, String cur_stylesheet) throws cwException, qdbException, SQLException, cwSysMessage {

        StringBuffer xmlBuf = new StringBuffer();
        xmlBuf.append(queAsXML(con, null, null, false, cur_stylesheet));
        StringBuffer queXML = new StringBuffer();
        queXML.append(getQuestionsDetailsXML(con));

        ViewQueContainer myViewQueContainer = new ViewQueContainer();
        myViewQueContainer.res_id = res_id;
        myViewQueContainer.get(con);

        queXML.append("<que_container_extra_info ");
        queXML.append("res_id=\"");
        queXML.append(Long.toString(myViewQueContainer.res_id));
        queXML.append("\" allow_shuffle_ind=\"");
        queXML.append(Integer.toString(myViewQueContainer.qct_allow_shuffle_ind));
        queXML.append("\" selection_logic=\"");
        queXML.append(myViewQueContainer.qct_select_logic);
        queXML.append("\">");
        queXML.append("</que_container_extra_info>");

        int index = xmlBuf.toString().indexOf("</question>");
        xmlBuf.insert(index, queXML);
        return xmlBuf.toString();
    }

    private String getContainerAttrXML() {
        StringBuffer containerXmlBuf = new StringBuffer(256);
        containerXmlBuf.append("<container_attribute>").append("<allow_shuffle_ind>").append(this.qct_allow_shuffle_ind).append("</allow_shuffle_ind>").append("</container_attribute>");
        return containerXmlBuf.toString();
    }

    private String getQuestionsDetailsXML(Connection con) throws qdbException, SQLException, cwSysMessage {
        //get associated questions
        ViewQueContainer queContainer = new ViewQueContainer();
        queContainer.res_id = this.res_id;
        Vector vQue = queContainer.getQuestions(con, ViewQueContainer.orderByOrderNTitle);
        Vector vtQueId = new Vector();
        for (int i = 0; i < vQue.size(); i++) {
            dbQuestion que = (dbQuestion)vQue.elementAt(i);
            vtQueId.addElement(new Long(que.que_res_id));
        }
        Hashtable htInteraction = dbInteraction.getInteractions(con, vtQueId);

        StringBuffer xmlBuf = new StringBuffer(1024);
        xmlBuf.append("<question_list>");
        for (int i = 0; i < vQue.size(); i++) {
            dbQuestion que = (dbQuestion)vQue.elementAt(i);
            que.ints = (Vector)htInteraction.get(new Long(que.que_res_id));
            if (que.ints == null) {
                que.ints = new Vector();
            }
            xmlBuf.append(que.queAsXML(con, null, null, false, null));
        }
        xmlBuf.append("</question_list>");
        return xmlBuf.toString();
    }

    private String getQuestionsXML(Connection con) throws qdbException {
        //get associated questions
        ViewQueContainer queContainer = new ViewQueContainer();
        queContainer.res_id = this.res_id;
        Vector vQue = queContainer.getQuestions(con, ViewQueContainer.orderByOrderNTitle);

        StringBuffer xmlBuf = new StringBuffer(1024);
        xmlBuf.append("<question_list>");
        for (int i = 0; i < vQue.size(); i++) {
            dbQuestion que = (dbQuestion)vQue.elementAt(i);
            xmlBuf.append("<question id=\"").append(que.que_res_id).append("\"").append(" title=\"").append(cwUtils.esc4XML(que.res_title)).append("\"").append(" score=\"").append(que.que_score).append("\"").append(" int_count=\"").append(que.que_int_count).append("\"").append(" prog_lang=\"").append(que.que_prog_lang).append("\"").append(" media_ind=\"").append(que.que_media_ind).append("\"");
            if (que.que_attempted == true) {
                xmlBuf.append(" attempted=\"TRUE\"");
            } else {
                xmlBuf.append(" attempted=\"FALSE\"");
            }
            xmlBuf.append("/>");
        }
        xmlBuf.append("</question_list>");
        return xmlBuf.toString();
    }

    private void loadQuestion(dbQuestion dbque) {
        this.que = dbque;
        this.res_id = dbque.res_id;
        this.res_lan = dbque.res_lan;
        this.res_title = dbque.res_title;
        this.res_desc = dbque.res_desc;
        this.res_type = dbque.res_type;
        this.res_subtype = dbque.res_subtype;
        this.res_annotation = dbque.res_annotation;
        this.res_format = dbque.res_format;
        this.res_difficulty = dbque.res_difficulty;
        this.res_duration = dbque.res_duration;
        this.res_privilege = dbque.res_privilege;
        this.res_status = dbque.res_status;
        this.res_usr_id_owner = dbque.res_usr_id_owner;
        this.res_tpl_name = dbque.res_tpl_name;
        this.res_res_id_root = dbque.res_res_id_root;
        this.res_mod_res_id_test = dbque.res_mod_res_id_test;
        this.res_upd_user = dbque.res_upd_user;
        this.res_upd_date = dbque.res_upd_date;
        this.res_src_type = dbque.res_src_type;
        this.res_src_link = dbque.res_src_link;
        this.res_instructor_name = dbque.res_instructor_name;
        this.res_instructor_organization = dbque.res_instructor_organization;
        this.res_create_date = dbque.res_create_date;
        this.res_cnt_subtype = dbque.res_cnt_subtype;
        this.res_in_subtype = dbque.res_in_subtype;
        this.mod_usr_id_instructor = dbque.mod_usr_id_instructor;
        this.location = dbque.location;
        return;
    }

    /**
     * Get child question id order by pre-defined order, resource title.
     * @param con
     * @return Vector of question id in Long.
     * @throws SQLException
     */
    public Vector getChildQueId(Connection con) throws SQLException {
        ViewResources viewRes = new ViewResources();
        return viewRes.getChildQueId(con, this.res_id);
    }

    /**
     * Generate a question and its child question xml used in test.  
     * @param con
     * @param order Qestion order in the test.
     * @param queId A vector of question to be generated in xml.
     * @return String of the question xml.
     * @throws cwSysMessage Thrown if failed to get objective xml of the question.
     * @throws qdbException
     */
    public String asXMLinTest(Connection con, long order, Vector queId, boolean isShuffleMCQue) throws qdbException, cwSysMessage, SQLException {
        return asXMLinTest(con, order, queId, null, isShuffleMCQue);
    }

    // overload to allow the child qu sort by the order in rcn
    public String asXMLinTest(Connection con, long order, Vector queId, Hashtable orderHash, boolean isShuffleMCQue) throws qdbException, cwSysMessage {

        StringBuffer xml = new StringBuffer();
        xml.append("<question ").append(" id=\"").append(this.res_id).append("\" ").append(" order=\"").append(order).append("\" ").append(" language=\"").append(this.res_lan).append("\" ").append(" score=\"").append(this.que.que_score).append("\" ").append(" res_upd_date=\"").append(this.res_upd_date).append("\" ").append(">");

        xml.append("<header ").append(" difficulty=\"").append(this.res_difficulty).append("\" ").append(" duration=\"").append(this.res_duration).append("\" ").append(" privilege=\"").append(this.res_privilege).append("\" ").append(" status=\"").append(this.res_status).append("\" ").append(" type=\"").append(this.res_subtype).append("\" ").append(" res_lan=\"").append(this.res_lan).append(
            "\"").append(
            ">");

        xml.append("<title>").append(cwUtils.esc4XML(this.res_title)).append("</title>");
        xml.append("<desc>").append(cwUtils.esc4XML(this.res_desc)).append("</desc>");
        xml.append(this.objAsXML(con));
        xml.append("</header>");
        xml.append(this.que.que_xml);

        //get associated questions

        xml.append("<question_list>");
        for (int i = 0; i < queId.size(); i++) {
            dbQuestion que = new dbQuestion();
            que.que_res_id = ((Long)queId.elementAt(i)).longValue();
            que.get(con);
            if (orderHash != null) {
                Long qOrder = (Long)orderHash.get(new Long(que.que_res_id));
                xml.append(que.asXML(con, qOrder.longValue(), isShuffleMCQue));
            } else {
                xml.append(que.asXML(con, (i + 1), isShuffleMCQue));
            }
        }
        xml.append("</question_list>");
        xml.append("</question>");
        return xml.toString();
    }

    public FixedScenarioQue copyMySelf(Connection con, loginProfile prof, long toObjId, String uploadDir) throws SQLException, qdbErrMessage, qdbException, cwSysMessage {
        return cloneMySelf(con, prof, toObjId, uploadDir, false);
    }

    public FixedScenarioQue cloneMySelf(Connection con, loginProfile prof, long toObjId, String uploadDir, boolean boolWithChildObj) throws SQLException, qdbErrMessage, qdbException, cwSysMessage {

        Timestamp curTime = cwSQL.getTime(con);

        //copy the question itself
        FixedScenarioQue newQue = new FixedScenarioQue();
        newQue.qct_select_logic = this.qct_select_logic;
        newQue.qct_allow_shuffle_ind = this.qct_allow_shuffle_ind;
        newQue.que = new dbQuestion();
        newQue.que.res_lan = this.res_lan;
        newQue.que.res_title = this.res_title;
        newQue.que.res_desc = this.res_desc;
        newQue.que.res_type = this.res_type;
        newQue.que.res_subtype = this.res_subtype;
        newQue.que.res_annotation = this.res_annotation;
        newQue.que.res_format = this.res_format;
        newQue.que.res_difficulty = this.res_difficulty;
        newQue.que.res_duration = this.res_duration;
        newQue.que.res_privilege = this.res_privilege;
        newQue.que.res_status = this.res_status;
        newQue.que.res_tpl_name = this.res_tpl_name;
        newQue.que.res_src_type = this.res_src_type;
        newQue.que.res_src_link = this.res_src_link;
        newQue.que.res_instructor_name = this.res_instructor_name;
        newQue.que.res_instructor_organization = this.res_instructor_organization;
        newQue.que.res_cnt_subtype = this.res_cnt_subtype;
        newQue.que.res_in_subtype = this.res_in_subtype;
        newQue.que.mod_usr_id_instructor = this.mod_usr_id_instructor;
        newQue.que.location = this.location;
        newQue.que.que_type = this.que.que_type;
        newQue.que.que_xml = this.que.que_xml;
        //newQue.que.que_score = this.que.que_score;
        newQue.que.que_int_count = this.que.que_int_count;
        newQue.que.que_prog_lang = this.que.que_prog_lang;
        newQue.que.que_media_ind = this.que.que_media_ind;
        newQue.qct_allow_shuffle_ind = this.qct_allow_shuffle_ind;
        newQue.qct_select_logic = this.qct_select_logic;
        newQue.que.res_res_id_root = 0;
        newQue.que.res_mod_res_id_test = 0;
        newQue.que.res_upd_user = prof.usr_id;
        newQue.que.res_upd_date = curTime;
        newQue.que.res_usr_id_owner = prof.usr_id;
        newQue.que.res_create_date = curTime;
        newQue.loadQuestion(newQue.que);
        if(newQue.qct_allow_shuffle_ind == 1){
        	newQue.que.que_sc_shuffle = true;
        }
        newQue.ins(con, new String[] { Long.toString(toObjId)}, prof, newQue.que);

        //copy the resourceFiles
        dbUtils.copyDir(uploadDir + dbUtils.SLASH + this.res_id, uploadDir + dbUtils.SLASH + newQue.res_id);

        //copy child questions
        //get associated questions
        ViewQueContainer queContainer = new ViewQueContainer();
        queContainer.res_id = this.res_id;
        Vector vQue = queContainer.getQuestions(con, ViewQueContainer.orderByScoreNTitle);
        for (int i = 0; i < vQue.size(); i++) {
            dbQuestion dbque = (dbQuestion)vQue.elementAt(i);
            //get question interaction
            dbque.ints = dbInteraction.getQInteraction(con, dbque.que_res_id);

            long oldQueId = dbque.que_res_id;
            long oldQueOrder = getQueContentOrder(con, oldQueId);
            dbque.res_usr_id_owner = prof.usr_id;
            dbque.res_upd_user = prof.usr_id;
            dbque.res_upd_date = curTime;
            dbque.res_mod_res_id_test = newQue.res_id;
            /*if (boolWithChildObj) {
            	dbque.res_id = dbque.que_res_id;
            	dbQuestion.mirrorQue(con, dbque, new String[] {Long.toString(toObjId)}, uploadDir);
            }
            else {
            	dbque.ins(con, new String[] {null}, prof, RES_TYPE_QUE);
            	long newQueId = dbque.que_res_id;
            	dbUtils.copyDir(uploadDir + dbUtils.SLASH + oldQueId, uploadDir + dbUtils.SLASH + newQueId); 
            }
            */

            dbque.ins(con, new String[] { null }, prof, RES_TYPE_QUE);
            long newQueId = dbque.que_res_id;
            if (oldQueOrder > 0) {
                dbque.updQueContainerOrder(con, newQue.res_id, newQueId, oldQueOrder);
            }
            if (boolWithChildObj) {
                dbResourceObjective myDbResourceObjective = new dbResourceObjective();
                myDbResourceObjective.insResObj(con, new long[] { newQueId }, new long[] { toObjId });
                dbResource.updResIdRoot(con, newQueId, oldQueId);
            }
            dbUtils.copyDir(uploadDir + dbUtils.SLASH + oldQueId, uploadDir + dbUtils.SLASH + newQueId);
        }

        return newQue;
    }
    
    private long getQueContentOrder(Connection con, long old_que_id) throws SQLException {
        return dbResource.getQueOrder(con, this.res_id, old_que_id);
    }

    public String getObjAsXML(Connection con, loginProfile prof) throws qdbException, cwSysMessage, cwException, SQLException {
    String result = "";

    boolean bTemplate = true;
    String dpo_view = null;

    ViewQueContainer myViewQueContainer = new ViewQueContainer();
    myViewQueContainer.res_id = res_id;
    myViewQueContainer.get(con);

    // xml header
    result += dbUtils.xmlHeader;
    result += "<fixed_scenario id=\"" + res_id + "\" language=\"" + res_lan + "\" last_modified=\"" + res_upd_user + "\" timestamp=\"" + res_upd_date + "\" owner=\"" + dbUtils.esc4XML(dbRegUser.usrId2SteUsrId(con, res_usr_id_owner)) + "\">" + dbUtils.NEWL;
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
    result += getQuestionsXML(con);
    result += "</fixed_scenario>" + dbUtils.NEWL;

    return result;
}

public String getQueXml() {
    StringBuffer title = new StringBuffer(); 
    title.append("<title>").append(cwUtils.esc4XML(this.que.res_title)).append("</title>").append(this.que.que_xml);
    return title.toString();
}

    public String asXMLinTest_test(Connection con, long order, Vector queId, Hashtable orderHash, boolean isShuffleMCQue,String uploadDir, Vector resIdVec, long tkh_id) throws qdbException, cwSysMessage,SQLException, qdbErrMessage {
    
        StringBuffer xml = new StringBuffer();
        xml.append("<question ").append(" id=\"").append(this.res_id).append("\" ").append(" order=\"").append(order).append("\" ").append(" language=\"").append(this.res_lan).append("\" ").append(" score=\"").append(this.que.que_score).append("\" ").append(" res_upd_date=\"").append(this.res_upd_date).append("\" ").append(">");
    
        xml.append("<header ").append(" difficulty=\"").append(this.res_difficulty).append("\" ").append(" duration=\"").append(this.res_duration).append("\" ").append(" privilege=\"").append(this.res_privilege).append("\" ").append(" status=\"").append(this.res_status).append("\" ").append(" type=\"").append(this.res_subtype).append("\" ").append(" res_lan=\"").append(this.res_lan).append(
            "\"").append(
            ">");
    
        xml.append("<title>").append(cwUtils.esc4XML(this.res_title)).append("</title>");
        xml.append("<desc>").append(cwUtils.esc4XML(this.res_desc)).append("</desc>");
        //xml.append(this.objAsXML(con));
        xml.append("</header>");
        xml.append(this.que.que_xml);
    
        //get associated questions
    
        xml.append("<question_list>");
        xml.append(que.getQueAsXML_test( con,  uploadDir,  queId,  resIdVec, dbModule.MOD_TYPE_TST,  isShuffleMCQue, tkh_id, 0, null));
        /*
        for (int i = 0; i < queId.size(); i++) {
            dbQuestion que = new dbQuestion();
            que.que_res_id = ((Long)queId.elementAt(i)).longValue();
            que.get(con);
            if (orderHash != null) {
                Long qOrder = (Long)orderHash.get(new Long(que.que_res_id));
                xml.append(que.asXML(con, qOrder.longValue(), isShuffleMCQue));
            } else {
                xml.append(que.asXML(con, (i + 1), isShuffleMCQue));
            }
        }
        */
        xml.append("</question_list>");
        xml.append("</question>");
        return xml.toString();
    }
    
    public static String asXMLinTest_test(dbQuestion dbq, Hashtable subQue, Vector queId, long order,  
    		long tkh_id, String uploadDir, Connection con, boolean restoreQue, boolean isShuffleMCQue, 
    		Vector resIdVec, String que_type) throws qdbException, cwSysMessage,SQLException, cwException {
        
        StringBuffer xml = new StringBuffer();
        xml.append("<question ").append(" id=\"").append(dbq.res_id).append("\" ").append(" order=\"").append(order).append("\" ").append(" language=\"").append(dbq.res_lan).append("\" ").append(" score=\"").append(dbq.que_score).append("\" ").append(" res_upd_date=\"").append(dbq.res_upd_date).append("\" ").append(">");
    
        xml.append("<header ").append(" difficulty=\"").append(dbq.res_difficulty).append("\" ").append(" duration=\"").append(dbq.res_duration).append("\" ").append(" privilege=\"").append(dbq.res_privilege).append("\" ").append(" status=\"").append(dbq.res_status).append("\" ").append(" type=\"").append(dbq.res_subtype).append("\" ").append(" res_lan=\"").append(dbq.res_lan).append(
            "\"").append(
            ">");
    
        xml.append("<title>").append(cwUtils.esc4XML(dbq.res_title)).append("</title>");
        xml.append("<desc>").append(cwUtils.esc4XML(dbq.res_desc)).append("</desc>");
        xml.append("</header>");
        xml.append(dbq.que_xml);
        xml.append("<question_list>");
        xml.append(dbq.getQueXML_test(subQue, queId, dbq.que_res_id, que_type, tkh_id, uploadDir, con, restoreQue, isShuffleMCQue, resIdVec, true, null));
        xml.append("</question_list>");
        xml.append("</question>");
        return xml.toString();
    }
    
    public String asXMLinTest_testforDyn(Connection con, long order, Vector queId, Hashtable orderHash, boolean isShuffleMCQue,String uploadDir, Vector resIdVec, long tkh_id, long mod_id) throws qdbException, cwSysMessage,SQLException, qdbErrMessage {
	    
        StringBuffer xml = new StringBuffer();
        xml.append("<question ").append(" id=\"").append(this.res_id).append("\" ").append(" order=\"").append(order).append("\" ").append(" language=\"").append(this.res_lan).append("\" ").append(" score=\"").append(this.que.que_score).append("\" ").append(" res_upd_date=\"").append(this.res_upd_date).append("\" ").append(">");
    
        xml.append("<header ").append(" difficulty=\"").append(this.res_difficulty).append("\" ").append(" duration=\"").append(this.res_duration).append("\" ").append(" privilege=\"").append(this.res_privilege).append("\" ").append(" status=\"").append(this.res_status).append("\" ").append(" type=\"").append(this.res_subtype).append("\" ").append(" res_lan=\"").append(this.res_lan).append("\">");
    
        xml.append("<title>").append(cwUtils.esc4XML(this.res_title)).append("</title>");
        xml.append("<desc>").append(cwUtils.esc4XML(this.res_desc)).append("</desc>");
        xml.append("</header>");
        xml.append(this.que.que_xml);
         
        xml.append("<question_list>");
        xml.append(que.getQueAsXMLforRestoredDyn( con,  uploadDir,  queId,  resIdVec, dbModule.MOD_TYPE_TST,  isShuffleMCQue, tkh_id, mod_id, null));

        xml.append("</question_list>");
        xml.append("</question>");
        return xml.toString();
    }

}