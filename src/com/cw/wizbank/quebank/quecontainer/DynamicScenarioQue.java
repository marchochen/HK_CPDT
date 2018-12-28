package com.cw.wizbank.quebank.quecontainer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Vector;
import java.util.Hashtable;

import com.cw.wizbank.qdb.dbModule;
import com.cw.wizbank.qdb.dbQuestion;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.dbResource;
import com.cw.wizbank.qdb.dbResourceObjective;
import com.cw.wizbank.qdb.dbInteraction;
import com.cw.wizbank.qdb.dbResourcePermission;
import com.cw.wizbank.qdb.dbTemplate;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.dbResourceContent;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.db.sql.SqlStatements;
import com.cw.wizbank.db.view.ViewQueContainer;
import com.cw.wizbank.db.view.ViewResources;
import com.cw.wizbank.db.DbQueContainerSpec;
import com.cwn.wizbank.utils.CommonLog;

public class DynamicScenarioQue extends DynamicQueContainer {

    private dbQuestion que;

    public DynamicScenarioQue() {}

    /**
    Update the Dynamic Scenario Question
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
    Create a new Dynamic Scenario Question
    @con Connection to database
    @robs String array of the question's objective id
    @prof loginProfile of the user
    @dbque dbQuestion of the this scenario scenario question
    */
    public void ins(Connection con, String[] robs, loginProfile prof, dbQuestion dbque) throws SQLException, qdbException, qdbErrMessage {

        //insert new question
        dbque.ins(con, robs, prof, RES_TYPE_QUE);
        loadQuestion(dbque);

        //insert into QueContainer
        ViewQueContainer queContainer = new ViewQueContainer();
        queContainer.res_id = dbque.res_id;
        queContainer.qct_select_logic = qct_select_logic;
        queContainer.qct_allow_shuffle_ind = qct_allow_shuffle_ind;
        queContainer.ins(con);

        return;
    }

    /**
    Get the question and container details of a Dynamic Scenario Question.
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
    Get the question and container details of a Dynamic Scenario Question.
    Need to pre-define this.res_id
    @con Connection to database
    @dbque given the dbQuestion of the Dynamic Scenario Question so that the method will not get it again from database
    */
    public void get(Connection con, dbQuestion dbque) throws qdbException, cwSysMessage {
        //get question details
        this.que = dbque;

        //get container details
        super.get(con);
    }

    public void del(Connection con, loginProfile prof) throws qdbException, qdbErrMessage, cwSysMessage, SQLException {
        // delete the child questions and the related records
        Vector vtResourceContent = dbResourceContent.getChildAss(con, res_id);
        String[] id_lst = new String[vtResourceContent.size()];
        for (int i = 0; i < vtResourceContent.size(); i++) {
            dbResourceContent myDbResourceContent = (dbResourceContent)vtResourceContent.elementAt(i);
            id_lst[i] = Long.toString(myDbResourceContent.rcn_res_id_content);
        }

        dbResourceContent myDbResourceContent = new dbResourceContent();
        myDbResourceContent.rcn_res_id = res_id;
        myDbResourceContent.delRes(con, id_lst);

        for (int i = 0; i < id_lst.length; i++) {
            dbQuestion dbque = new dbQuestion();
            dbque.que_res_id = Long.parseLong(id_lst[i]);
            dbque.del(con);
        }

        // delete the question container and the related records
        del_container(con, prof);

        // delete the question object for the ocntainer itself
        dbQuestion myDbQuestion = new dbQuestion();
        myDbQuestion.que_res_id = res_id;
        myDbQuestion.res_id = res_id;
        myDbQuestion.res_upd_user = prof.usr_id;
        myDbQuestion.del(con);
    }

    /**
     * Get scenario child question count which meet all the pre-defined requirements. (Only check score)
     * @param con
     * @return Number of question.
     * @throws SQLException
     */
    public int getChildQueCount(Connection con) throws SQLException {
        DbQueContainerSpec myDbQueContainerSpec = new DbQueContainerSpec();
        Vector vtDbQueContainerSpec = DbQueContainerSpec.getQueContainerSpecs(con, this.res_id);
        return DynamicScenarioQue.getChildQueCount(con, vtDbQueContainerSpec);
    }

    /**
     * Get scenario child question count which meet the specified requirement. (Only check score)
     * @param con
     * @param queSpec Vector of DbQueContainerSpec.
     * @return Number of question.
     * @throws SQLException
     */
    public static int getChildQueCount(Connection con, Vector queSpec) throws SQLException {

        Vector v_que_id = new Vector();
        ViewResources viewRes = new ViewResources();
        int count = 0;
        for (int i = 0; i < queSpec.size(); i++) {
            DbQueContainerSpec spec = (DbQueContainerSpec)queSpec.elementAt(i);
            Vector tmp_v_que_id = viewRes.getDynamicScenarioChildQueId(con, spec.qcs_res_id, spec.qcs_score, v_que_id);
            count += tmp_v_que_id.size();
        }

        return count;
    }

    /**
     * Get scenario child question id which meet all pre-defined requirements. (Only check score)
     * @param con
     * @return Vector of question id in Long.
     * @throws SQLException
     * @throws cwSysMessage Thrown if not enough question.
     */

    public Vector getChildQueId(Connection con) throws SQLException, cwSysMessage {
        DbQueContainerSpec myDbQueContainerSpec = new DbQueContainerSpec();
        Vector vtDbQueContainerSpec = DbQueContainerSpec.getQueContainerSpecs(con, this.res_id);
        return DynamicScenarioQue.getChildQueId(con, vtDbQueContainerSpec);
    }

    /**
     * Get scenario child question id which meet the specified requirement. (Only check score)
     * @param con
     * @param queSpec Vector of DbQueContainerSpec.
     * @return Vector of question id in Long.
     * @throws SQLException
     * @throws cwSysMessage Thrown if not enough question.
     */

    public static Vector getChildQueId(Connection con, Vector queSpec) throws SQLException, cwSysMessage {

        Vector v_que_id = new Vector();
        ViewResources viewRes = new ViewResources();
        for (int i = 0; i < queSpec.size(); i++) {
            DbQueContainerSpec spec = (DbQueContainerSpec)queSpec.elementAt(i);
            Vector tmp_v_que_id = viewRes.getDynamicScenarioChildQueId(con, spec.qcs_res_id, spec.qcs_score, v_que_id);
            if (spec.qcs_qcount > tmp_v_que_id.size()) {
                throw new cwSysMessage("MSP002");
            }
            try {
                v_que_id.addAll(cwUtils.randomDrawFromVec(tmp_v_que_id, spec.qcs_qcount));
            } catch (cwException e) {
                throw new cwSysMessage("MSP002");
            }
        }

        //randomize the question id in the vector.
        v_que_id = cwUtils.randomVec(v_que_id);
        return v_que_id;
    }

    /**
     * Generate a question and its child question xml used in test. 
     * The child question is pre-defined in the vector 
     * and the xml will use the order in the vector to generate the xml.
     * @param con
     * @param order Qestion order in the test.
     * @param queId The selected child question of the Dynamic Scenario Question.
     * @return String of the question xml.
     * @throws cwSysMessage Thrown if failed to get objective xml of the question.
     * @throws qdbException
     */
    public String asXMLinTest(Connection con, long order, Vector queId, boolean isShuffleMCQue) throws qdbException, cwSysMessage, SQLException {
        return asXMLinTest(con, order, queId, null, isShuffleMCQue);
    }

    // overload to allow the child qu sort by the order in rcn
    public String asXMLinTest(Connection con, long order, Vector queId, Hashtable orderHash, boolean isShuffleMCQue) throws qdbException, cwSysMessage, SQLException {

        StringBuffer xml = new StringBuffer();
        xml.append("<question ").append(" id=\"").append(this.res_id).append("\" ").append(" order=\"").append(order).append("\" ").append(" language=\"").append(this.res_lan).append("\" ").append(" res_upd_date=\"").append(this.res_upd_date).append("\" ").append(" score=\"").append(this.que.que_score).append("\" ").append(">");

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

    /**
    Get XML of this Dynamic Scenario Question.
    The XML contains the questions and the criteria it has 
    @param con Connection to database
    @param prof loginProfile of the user
    @param checkResStatus true if need to filter OFF questions; false otherwise
    @return XML of this Dynamic Scenario Question.
    */
    public String asXML(Connection con, loginProfile prof) throws cwException, qdbException, SQLException, cwSysMessage {

        StringBuffer xmlBuf = new StringBuffer(super.asXML(con, prof, null));
        StringBuffer containerXmlBuf = new StringBuffer(1024);
        containerXmlBuf.append(getQuestionsXML(con));
        //.append(getSpecXML(con, prof));

        int index = xmlBuf.toString().indexOf("</dynamic_que_container>");
        xmlBuf.insert(index, containerXmlBuf);
        return xmlBuf.toString();
    }

    public String detailsAsXML(Connection con, loginProfile prof, String cur_sthlesheet) throws cwException, qdbException, SQLException, cwSysMessage {

        StringBuffer xmlBuf = new StringBuffer();
        xmlBuf.append(queAsXML(con, null, null, false, cur_sthlesheet));
        StringBuffer queXML = new StringBuffer();
        queXML.append(getSpecXML(con, prof));
        queXML.append(getQuestionsDetailsXML(con));
        int index = xmlBuf.toString().indexOf("</question>");
        xmlBuf.insert(index, queXML);
        return xmlBuf.toString();
    }
    /**
    Add a criterion to the criteria spec of this dynamic scenario
    @parma con Connection con
    @param spec_type question type
    @param spec_difficulty 1-easy; 2-normal; 3-hard
    @param spec_privilege CW-Public; AUTHOR-Private
    @param spec_duration question's duration
    @param spec_qcount number of questions
    @param spec_obj_id questions' objective id
    */
    public void addSpec(Connection con, String spec_type, long spec_score, long spec_difficulty, String spec_privilege, float spec_duration, long spec_qcount, long spec_obj_id, String usr_id) throws cwSysMessage, qdbException {

        if (!validateSpecBeforeInsert(con, spec_score)) {
        	CommonLog.error("insert spec failed");
            throw new cwSysMessage("DSC001");
        }

        if (res_subtype == null) {
            res_subtype = spec_type;
        }
        super.addSpec(con, spec_type, spec_score, spec_difficulty, spec_privilege, spec_duration, spec_qcount, spec_obj_id, usr_id);
        if (spec_score != 0 && spec_qcount != 0) {
            if (this.que == null) {
                this.que = new dbQuestion();
                this.que.que_res_id = this.res_id;
                this.que.res_id = this.res_id;
                this.que.get(con);
            }
            this.que.que_score += (spec_score * spec_qcount);
            try {
                this.que.updScore(con);
            } catch (SQLException e) {
                throw new qdbException(e.getMessage());
            }
        }
        return;
    }

    public void updSpec(Connection con, long spec_id, String spec_type, long spec_score, long spec_difficulty, String spec_privilege, float spec_duration, long spec_qcount, String usr_id) throws cwSysMessage, qdbException {

        if (!validateSpecBeforeUpdate(con, spec_id, spec_score)) {
        	CommonLog.error("update spec failed");
            throw new cwSysMessage("DSC001");
        }

        //get the score details of the criterion before update
        DbQueContainerSpec oldCriterion = new DbQueContainerSpec();
        oldCriterion.qcs_id = spec_id;
        oldCriterion.get(con);

        //update the criterion
        super.updSpec(con, spec_id, spec_type, spec_score, spec_difficulty, spec_privilege, spec_duration, spec_qcount, usr_id);

        //update the dynamic scenario question's score if necessary
        if ((oldCriterion.qcs_score * oldCriterion.qcs_qcount) != (spec_score * spec_qcount)) {
            if (this.que == null) {
                this.que = new dbQuestion();
                this.que.que_res_id = this.res_id;
                this.que.res_id = this.res_id;
                this.que.get(con);
            }
            this.que.que_score += ((spec_score * spec_qcount) - (oldCriterion.qcs_score * oldCriterion.qcs_qcount));
            try {
                this.que.updScore(con);
            } catch (SQLException e) {
                throw new qdbException(e.getMessage());
            }
        }
    }

    /**
    Remove the input criterion from this Dynamic Scenario Question
    @param con Connection to database
    @param spec_id criterion id to be removed
    */
    public void removeSpec(Connection con, long spec_id) throws cwSysMessage, qdbException {
        //get the criterion details
        DbQueContainerSpec myDbQueContainerSpec = new DbQueContainerSpec();
        myDbQueContainerSpec.qcs_id = spec_id;
        myDbQueContainerSpec.get(con);

        //update the score of the dynamic scenario question if necessary
        if (myDbQueContainerSpec.qcs_score != 0 && myDbQueContainerSpec.qcs_qcount != 0) {
            if (this.que == null) {
                this.que = new dbQuestion();
                this.que.que_res_id = this.res_id;
                this.que.res_id = this.res_id;
                this.que.get(con);
            }
            this.que.que_score -= (myDbQueContainerSpec.qcs_score * myDbQueContainerSpec.qcs_qcount);
            try {
                this.que.updScore(con);
            } catch (SQLException e) {
                throw new qdbException(e.getMessage());
            }
        }

        myDbQueContainerSpec.del(con, res_subtype);
        super.updateTimeStamp(con);
        return;
    }
    
    public void removeAllSpec(Connection con) throws cwSysMessage, qdbException, SQLException {
        if (this.que == null) {
            this.que = new dbQuestion();
            this.que.que_res_id = this.res_id;
            this.que.res_id = this.res_id;
            this.que.get(con);
        }
        this.que.que_score = 0;
        this.que.updScore(con);
        
        DbQueContainerSpec myDbQueContainerSpec = new DbQueContainerSpec();
        myDbQueContainerSpec.removeAll(con, this.res_id);
        
        super.updateTimeStamp(con);
        return;
    }

    public String queAsXML(Connection con, loginProfile prof, String mod_type, String cur_stylesheet) throws qdbException, cwSysMessage, SQLException {
        return queAsXML(con, prof, mod_type, true, cur_stylesheet);
    }
    /**
    Generate XML for this Dynamic Scenario Question's meta information.
    Need to pre-define this.res_id
    @con Connection to database
    @prof loginProfile of the user
    @mod_type required for dbQuestion.queAsXML(Connection, loginProfile, mod_type)
    @showXmlHeader boolean to control show the xml header or not
    @return XML for this Dynamic Scenario Question's meta information.
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

    public DynamicScenarioQue copyMySelf(Connection con, loginProfile prof, long toObjId, String uploadDir) throws SQLException, qdbErrMessage, qdbException, cwSysMessage {
        return cloneMySelf(con, prof, toObjId, uploadDir, false);
    }

    public DynamicScenarioQue cloneMySelf(Connection con, loginProfile prof, long toObjId, String uploadDir, boolean boolWithChildObj) throws SQLException, qdbErrMessage, qdbException, cwSysMessage {

        Timestamp curTime = cwSQL.getTime(con);

        //copy the question itself
        DynamicScenarioQue newQue = new DynamicScenarioQue();
        newQue.que = new dbQuestion();
        newQue.qct_select_logic = this.qct_select_logic;
        newQue.qct_allow_shuffle_ind = this.qct_allow_shuffle_ind;
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
        newQue.ins(con, new String[] { Long.toString(toObjId)}, prof, newQue.que);

        //copy the resourceFiles
        dbUtils.copyDir(uploadDir + dbUtils.SLASH + this.res_id, uploadDir + dbUtils.SLASH + newQue.res_id);

        //copy the QueContainerSpec
        DbQueContainerSpec queContainerSpec = new DbQueContainerSpec();
        Vector v = queContainerSpec.getQueContainerSpecs(con, this.res_id);
        for (int i = 0; i < v.size(); i++) {
            DbQueContainerSpec criterion = (DbQueContainerSpec)v.elementAt(i);
            newQue.addSpec(con, criterion.qcs_type, criterion.qcs_score, criterion.qcs_difficulty, criterion.qcs_privilege, criterion.qcs_duration, criterion.qcs_qcount, criterion.qcs_obj_id, prof.usr_id);
        }

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
            dbque.res_usr_id_owner = prof.usr_id;
            dbque.res_upd_user = prof.usr_id;
            dbque.res_upd_date = curTime;
            dbque.res_mod_res_id_test = newQue.res_id;
            /*
            if (boolWithChildObj) {
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
            if (boolWithChildObj) {
                dbResourceObjective myDbResourceObjective = new dbResourceObjective();
                myDbResourceObjective.insResObj(con, new long[] { newQueId }, new long[] { toObjId });
                dbResource.updResIdRoot(con, newQueId, oldQueId);
            }
            dbUtils.copyDir(uploadDir + dbUtils.SLASH + oldQueId, uploadDir + dbUtils.SLASH + newQueId);

        }

        return newQue;
    }

    private String getQuestionsDetailsXML(Connection con) throws SQLException, cwSysMessage, qdbException {
        ViewQueContainer queContainer = new ViewQueContainer();
        queContainer.res_id = this.res_id;
        Vector vQue = queContainer.getQuestions(con, ViewQueContainer.orderByScoreNTitle);
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

    private String getSpecXML(Connection con, loginProfile prof) throws SQLException, qdbException {

        StringBuffer xmlBuf = new StringBuffer(512);
        DbQueContainerSpec myDbQueContainerSpec = new DbQueContainerSpec();
        Vector vtDbQueContainerSpec = myDbQueContainerSpec.getQueContainerSpecs(con, this.res_id);
        xmlBuf.append("<criterion_list>");
        for (int i = 0; i < vtDbQueContainerSpec.size(); i++) {
            DbQueContainerSpec tempDbQueContainerSpec = (DbQueContainerSpec)vtDbQueContainerSpec.elementAt(i);
            xmlBuf.append(tempDbQueContainerSpec.asXML(con, prof));
        }
        xmlBuf.append("</criterion_list>");
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
    Validate whether a new spec (with score only) is valid or not.
    The spec is considered invalid if and only if there exists any spec with the same score as it
    @param con Connection to database
    @param score score of the spec
    @return true if the spec is validate; false otherwise
    */
    private boolean validateSpecBeforeInsert(Connection con, long score) throws qdbException {

        try {
            DbQueContainerSpec qcs = new DbQueContainerSpec();
            qcs.qcs_res_id = this.res_id;
            qcs.qcs_score = score;
            return qcs.validateDSCSpecBeforeInsert(con);
        } catch (SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    /**
    Validate whether a new spec (with score only) is valid or not.
    The spec is considered invalid if and only if there exists any spec with the same score as it
    @param con Connection to database
    @param spec_id id of the spec to be updated
    @param score new score of the spec to be updated
    @return true if the spec is validate; false otherwise
    */
    private boolean validateSpecBeforeUpdate(Connection con, long spec_id, long score) throws qdbException {

        try {
            DbQueContainerSpec qcs = new DbQueContainerSpec();
            qcs.qcs_res_id = this.res_id;
            qcs.qcs_score = score;
            qcs.qcs_id = spec_id;
            return qcs.validateDSCSpecBeforeUpdate(con);
        } catch (SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
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
        result += "<dynamic_scenario id=\"" + res_id + "\" language=\"" + res_lan + "\" last_modified=\"" + res_upd_user + "\" timestamp=\"" + res_upd_date + "\" owner=\"" + dbUtils.esc4XML(dbRegUser.usrId2SteUsrId(con, res_usr_id_owner)) + "\">" + dbUtils.NEWL;
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
        result += getSpecXML(con, prof);

        // use the Module object to generate the objective XML
        result += dbModule.tstObjLstAsXML(con, res_id);
        result += getQuestionsXML(con);
        result += "</dynamic_scenario>" + dbUtils.NEWL;

        return result;
    }

    /**
     * @return
     */
    public String getQueXml()  {
        StringBuffer title = new StringBuffer(); 
        title.append("<title>").append(cwUtils.esc4XML(this.que.res_title)).append("</title>").append(this.que.que_xml);
        return title.toString();
    }
    
    private String getContainerAttrXML() {
        StringBuffer containerXmlBuf = new StringBuffer(256);
        containerXmlBuf.append("<container_attribute>").append("<allow_shuffle_ind>").append(this.qct_allow_shuffle_ind).append("</allow_shuffle_ind>").append("</container_attribute>");
        return containerXmlBuf.toString();
    }
    
    public String asXMLinTest_test(Connection con, long order, Vector queId, Hashtable orderHash, boolean isShuffleMCQue,String uploadDir, Vector resIdVec, long tkh_id) throws qdbException, cwSysMessage, SQLException, qdbErrMessage {
        StringBuffer xml = new StringBuffer();
        xml.append("<question ").append(" id=\"").append(this.res_id).append("\" ").append(" order=\"").append(order).append("\" ").append(" language=\"").append(this.res_lan).append("\" ").append(" res_upd_date=\"").append(this.res_upd_date).append("\" ").append(" score=\"").append(this.que.que_score).append("\" ").append(">");

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
        }*/
        xml.append("</question_list>");
        xml.append("</question>");
        return xml.toString();
    }
    
    private String sql_get_child_que_id =
        "Select res_id "
            + "From ResourceContent, Resources "
            + "Where rcn_res_id = ? "
            + "And rcn_tkh_id = ? "
            + "And rcn_res_id_content = res_id "
            + "And res_status = ? "
            + "ORDER BY rcn_order ASC, res_title ASC";

    public Vector getChildQueIdforRestore(Connection con, long rcn_res_id, long tkh_id)  throws SQLException {
	    Vector v_que_id = new Vector();
	    PreparedStatement stmt = con.prepareStatement(sql_get_child_que_id);
	    int idx = 1;
	    stmt.setLong(idx++, rcn_res_id);
	    stmt.setLong(idx++, tkh_id);
	    stmt.setString(idx++, dbResource.RES_STATUS_ON);
	    ResultSet rs = stmt.executeQuery();
	    while (rs.next()) {
	        v_que_id.addElement(new Long(rs.getLong("res_id")));
	    }
	    stmt.close();
    return v_que_id;
}
   
}