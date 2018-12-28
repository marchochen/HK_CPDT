package com.cw.wizbank.qdb;

import java.util.*;
import java.sql.*;

import javax.servlet.http.*;

import com.cw.wizbank.db.view.ViewKmObject;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.util.cwPagination;
import com.cwn.wizbank.utils.CommonLog;

public class dbForum extends dbModule {
    public static final String MOD_TYPE_FOR = "FOR";
    public static final String FOR_TYPE = "FOR_TYPE";
    public static final String FOR_ID = "FOR_ID";
    public static final String FOR_TOPIC = "FOR_TOPIC";
    public static final String FOR_MSG = "FOR_MSG";
    public static final String FOR_CMD = "FOR_CMD";
    public static final String FOR_VIEW = "FOR_VIEW";
    public static final String FOR_SEARCH = "FOR_SEARCH";
    public static final String FOR_RESULT = "FOR_RESULT";
    public static final String FOR_MSG_RESULT = "FOR_MSG_RESULT";
    public static final String FOR_SESS_PAGE = "FOR_SESS_PAGE";
    public static final int PAGE_SIZE = 10;
    public int MARK_MSG;

    public int totalTopicNum;
    public int totalMsgNum;
    public int totalUnreadMsgNum;

    public List dbFaqTopic;
    public dbForum() {
        super();
        MARK_MSG = 0;
        dbFaqTopic = new ArrayList();
    }

    public dbForum(dbModule dbmod) {
        MARK_MSG = 0;

        mod_res_id = dbmod.mod_res_id;
        mod_type = dbmod.mod_type;
        //mod_max_score = dbmod.mod_max_score;
        //mod_pass_score = dbmod.mod_pass_score;
        mod_instruct = dbmod.mod_instruct;
        //mod_max_attempt = dbmod.mod_max_attempt;
        //mod_max_usr_attempt = 1;
        //mod_score_ind = dbmod.mod_score_ind;
        //mod_score_reset = dbmod.mod_score_reset;
        mod_in_eff_start_datetime = dbmod.mod_in_eff_start_datetime;
        mod_in_eff_end_datetime = dbmod.mod_in_eff_end_datetime;
        // mod_usr_id_instructor should be get from dbModule when new dbForum()
        mod_usr_id_instructor = dbmod.mod_usr_id_instructor;
        //mod_logic = dbmod.mod_logic;

        mod_eff_start_datetime = dbmod.mod_eff_start_datetime;
        mod_eff_end_datetime = dbmod.mod_eff_end_datetime;

        res_id = dbmod.res_id;
        res_lan = dbmod.res_lan;
        res_title = dbmod.res_title;
        res_desc = dbmod.res_desc;
        res_type = RES_TYPE_MOD; // override
        res_subtype = MOD_TYPE_FOR; // override
        //res_annotation = dbmod.res_annotation;
        //res_format = dbmod.res_format;
        //res_difficulty = dbmod.res_difficulty;
        //res_duration = dbmod.res_duration;
        res_privilege = dbmod.res_privilege;
        res_usr_id_owner = dbmod.res_usr_id_owner;
        res_tpl_name = dbmod.res_tpl_name;
        //res_id_root = dbmod.res_id_root;
        res_mod_res_id_test = dbmod.res_mod_res_id_test;
        res_status = dbmod.res_status;
        //res_create_date = dbmod.res_create_date;
        res_upd_user = dbmod.res_upd_user;
        res_upd_date = dbmod.res_upd_date;
        res_src_type = dbmod.res_src_type;
        res_src_link = dbmod.res_src_link;
        //res_url = dbmod.res_url;
        //res_filename = dbmod.res_filename;
        //res_code = dbmod.res_code;
        res_instructor_name = dbmod.res_instructor_name;
        res_instructor_organization = dbmod.res_instructor_organization;
    }

    public void del(Connection con, loginProfile prof)
        throws qdbException, qdbErrMessage ,cwSysMessage
    {
        // check User Right

        //if (!dbResourcePermission.hasPermission(con, res_id, prof,
        //                                dbResourcePermission.RIGHT_WRITE)) {
        //    throw new qdbErrMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);
        //}

        dbForumTopic.delAllTopics(con, prof, res_id);
        try{
        	dbModuleEvaluationHistory.delAllHistory(con, res_id);
        }catch(SQLException e){
        	CommonLog.error(e.getMessage(),e);
        	throw new qdbException(e.getMessage());
        }
        if (mod_is_public){
            dbModuleEvaluation.delByMod(con, res_id);        
        }
        
        //super.delChild(con);
        super.del(con, prof);
        return;
    }

    //do not check moditify privilege at all
    public void aeDel(Connection con, loginProfile prof)
        throws qdbException, qdbErrMessage ,cwSysMessage
    {

        dbForumTopic.delAllTopics(con, prof, res_id);

        super.del(con);

        return;
    }

    public String outputTopicsAsXML(Connection con, HttpSession sess, loginProfile prof, String dpo_view, long cos_id, cwPagination page, String ssoXml)
        throws qdbException, cwSysMessage, SQLException, cwException
    {
        //String sess_type = (String)sess.getAttribute(FOR_TYPE);
        //String sess_cmd = (String)sess.getAttribute(FOR_CMD);
        long[] topicLst;
        cwPagination sess_page = (cwPagination)sess.getAttribute(FOR_SESS_PAGE);
        //Long tempId = (Long)sess.getAttribute(FOR_ID);
        //long id = 0;

        //if (tempId != null) {
        //    id = tempId.longValue();
        //}

        if (sess_page != null && sess_page.ts.equals(page.ts)) {
/*        if (sess_type != null && sess_type.equals(FOR_TOPIC) && 
            sess_cmd != null && sess_cmd.equals(FOR_VIEW) &&
            page.curPage != 0 && id == res_id) {*/
            topicLst = (long[])sess.getAttribute(FOR_RESULT);            
            sess_page.curPage = page.curPage;            
            page = sess_page;            
        } else {
            topicLst = dbForumTopic.getAllTopicIDs(con, res_id, page.sortCol, page.sortOrder); 
            //sess.setAttribute(FOR_TYPE, FOR_TOPIC);
            //sess.setAttribute(FOR_CMD, FOR_VIEW);
            //sess.setAttribute(FOR_ID, new Long(res_id));

            if (topicLst != null && topicLst.length > 0) {
                sess.setAttribute(FOR_RESULT, topicLst);
            } else {
                sess.setAttribute(FOR_RESULT, new long[0]);
            }
            

            if (page.curPage == 0) {
                page.curPage = 1;
            }

            if (page.pageSize == 0) {
                page.pageSize = cwPagination.defaultPageSize;
            }
            
            if (page.sortCol == null || page.sortCol.length() == 0) {
                page.sortCol = "fto_title";
            }
            
            if (page.sortOrder == null || page.sortOrder.length() == 0) {
                page.sortOrder = "asc";
            }

            page.ts = dbUtils.getTime(con);

            if (topicLst != null) {
                page.totalRec = topicLst.length;
            } else {
                page.totalRec = 0;
            }
            
            page.totalPage = page.totalRec/page.pageSize;

            if (page.totalRec%page.pageSize !=0) {
                page.totalPage++;
            }

            sess.setAttribute(FOR_SESS_PAGE, page);            
        }

        StringBuffer result = new StringBuffer();
        result.append(dbUtils.xmlHeader);
        result.append("<module id=\"").append(res_id).append("\" res_status=\"").append(res_status).append("\" language=\"").append(res_lan).append("\" timestamp=\"").append(res_upd_date).append("\" mark_msg=\"").append(MARK_MSG).append("\">");



        // author's information
        result.append(prof.asXML());
//        result += dbResourcePermission.aclAsXML(con, res_id, prof);

        //for sso_link info
        result.append(ssoXml);

        // Forum Header
        // Show detail of Forum Header 
        result.append(getModHeader(con, prof, cos_id, true));
        result.append(getModuleEval(con, res_id, prof.usr_ent_id));

        result.append(getDisplayOption(con, dpo_view));
        result.append(getIsEnrollmentRelatedAsXML(con));

        if (topicLst != null) {
            result.append("<stat num_of_topic=\"").append(topicLst.length);
        } else {
            result.append("<stat num_of_topic=\"0");
        }

        result.append("\" page_size=\"").append(page.pageSize).append("\" cur_page=\"").append(page.curPage).append("\"/>");

        result.append("<moderator_list>");
        Vector vtIstLst = dbResourcePermission.getModeratorLst(con, res_id);
        for (int i=0; i<vtIstLst.size(); i++){
            dbRegUser usr = new dbRegUser();
            usr.usr_ent_id = ((Long)vtIstLst.elementAt(i)).longValue();
            usr.get(con);
           
            result.append("<moderator>").append( (usr.getUserShortXML(con, false, false, false)).toString()).append("</moderator>");
        }

        result.append("</moderator_list>");
        result.append("<body>");

        if (topicLst != null) {
            int count = (page.curPage-1) * page.pageSize;
            Vector topic_vec = new Vector();

            for (int i=count; i<topicLst.length && i<count+page.pageSize; i++) {
                topic_vec.addElement(new Long(topicLst[i]));
//                topic = new dbForumTopic();
//                topic.fto_id = topicLst[i];
//                topic.get(con);
//                result.append(topic.contentWithoutMsgsAsXML(con, prof, MARK_MSG));
            }

            if (topic_vec.size() != 0) {
                result.append(dbForumTopic.contentWithoutMsgsAsXML(con, prof, MARK_MSG, cwUtils.vector2list(topic_vec), page.sortCol, page.sortOrder));
            }
        }

        result.append("</body>");
        result.append(page.asXML().toString());
        result.append("</module>");

        return result.toString();
    }

    public String outputMsgsAsXML(Connection con, HttpSession sess, loginProfile prof, String dpo_view, long topic_id, boolean unthread, String order, int page, int page_size, int msg_length, long cos_id, String ssoXml)
        throws qdbException, cwSysMessage, SQLException
    {
        if (page_size == 0){
            page_size = PAGE_SIZE;
        }
        StringBuffer result = new StringBuffer();

        result.append(dbUtils.xmlHeader);
        result.append("<module id=\"").append(res_id).append("\" res_status=\"").append(res_status).append("\" language=\"").append(res_lan).append("\" timestamp=\"").append(res_upd_date).append("\" mark_msg=\"").append(MARK_MSG).append("\">");


        // author's information
        result.append(prof.asXML());
//        result += dbResourcePermission.aclAsXML(con, res_id, prof);
        result.append(ssoXml);

        // Forum Header
        result.append(getModHeader(con, prof, cos_id));
        result.append(getModuleEval(con, res_id, prof.usr_ent_id));

        result.append(getDisplayOption(con, dpo_view));
        result.append(getIsEnrollmentRelatedAsXML(con));

        result.append("<body>");

//        dbForumTopic topic = new dbForumTopic();

//        topic.fto_id = topic_id;
//        topic.get(con);
        result.append(dbForumTopic.contentWithMsgsAsXML(con, sess, prof, topic_id, unthread, MARK_MSG, order, page, page_size, msg_length));

        result.append("</body>");
        result.append("</module>");

        return result.toString();
    }
    public String outputGivenMsgsAsXML(Connection con, loginProfile prof, String dpo_view, long topic_id, String[] msg_lst, long cos_id)
        throws qdbException, cwSysMessage, SQLException
    {
        return outputGivenMsgsAsXML(con, prof, dpo_view, topic_id, msg_lst, cos_id, false);
    }
    
    public String outputGivenMsgsAsXML(Connection con, loginProfile prof, String dpo_view, long topic_id, String[] msg_lst, long cos_id, boolean reply_ind)
        throws qdbException, cwSysMessage, SQLException
    {
        StringBuffer result = new StringBuffer();

//        if (msg_lst == null || msg_lst.length == 0) {
//            return asXML(con, prof, dpo_view, topic_id, true, true, null);
//        }

        result.append(dbUtils.xmlHeader);
        result.append("<module id=\"").append(res_id).append("\" language=\"").append(res_lan).append("\" timestamp=\"");
        result.append(res_upd_date).append("\" mark_msg=\"").append(MARK_MSG).append("\">");

        // author's information
        result.append(prof.asXML());
//        result.append(dbResourcePermission.aclAsXML(con, res_id, prof));

        // Forum Header
        result.append(getModHeader(con, prof, cos_id));
        result.append(getModuleEval(con, res_id, prof.usr_ent_id));
//        result += getModHeader(con, prof);
//        result += dbModuleEvaluation.getModuleEvalAsXML(con, res_id, prof.usr_ent_id);

        result.append(getDisplayOption(con, dpo_view));
        result.append("<body>");

        if (msg_lst != null) {
            long[] msgIds = new long[msg_lst.length];

            for (int i=0; i<msg_lst.length; i++) {
                msgIds[i] = Long.parseLong(msg_lst[i]);
            }

            dbForumTopic topic = new dbForumTopic();

            topic.fto_id = topic_id;
            topic.get(con);
            result.append(topic.contentShowAllAsXML(con, prof, msgIds, MARK_MSG, reply_ind));
        }

        result.append("</body>");
        result.append("</module>");
        return result.toString();
    }

    public String searchTopicsAsXML(Connection con, HttpSession sess, loginProfile prof, String dpo_view,
                                  String phrase, int phrase_cond, String created_by, int created_by_cond,
                                  Timestamp created_after, Timestamp created_before, String order, int page, long cos_id, int page_size)
        throws qdbException, cwSysMessage, SQLException
    {
    	if( page_size == 0 ) {
    		page_size = PAGE_SIZE;
    	}
        String sess_type = (String)sess.getAttribute(FOR_TYPE);
        String sess_cmd = (String)sess.getAttribute(FOR_CMD);
        long[] topicLst;
        Long tempId = (Long)sess.getAttribute(FOR_ID);
        long id = 0;

        if (tempId != null) {
            id = tempId.longValue();
        }

//        if (sess_type != null && sess_type.equals(FOR_TOPIC) &&
//            sess_cmd != null && sess_cmd.equals(FOR_SEARCH) &&
//            page != 0 && id == res_id) {
//            topicLst = (long[])sess.getAttribute(FOR_RESULT);
//            
//        } else {
            topicLst = dbForumTopic.searchTopics(con, res_id, phrase, phrase_cond, created_by, created_by_cond, created_after, created_before, order);
            sess.setAttribute(FOR_TYPE, FOR_TOPIC);
            sess.setAttribute(FOR_CMD, FOR_SEARCH);
            sess.setAttribute(FOR_ID, new Long(res_id));

            if (topicLst != null) {
                sess.setAttribute(FOR_RESULT, topicLst);
            }
   //     }

        String result = "<?xml version=\"1.0\" encoding=\"" + dbUtils.ENC_UTF +
                   "\" standalone=\"no\" ?>" + dbUtils.NEWL;

        result += "<module id=\""+ res_id + "\" language=\"" + res_lan + "\" timestamp=\"" +
                  res_upd_date + "\" mark_msg=\"" + MARK_MSG + "\">" + dbUtils.NEWL;

        // author's information
        result += prof.asXML() + dbUtils.NEWL;
//        result += dbResourcePermission.aclAsXML(con, res_id, prof);

        // Forum Header
        result += getModHeader(con, prof, cos_id);
        result += getModuleEval(con, res_id, prof.usr_ent_id);
//        result += getModHeader(con, prof);
//        result += dbModuleEvaluation.getModuleEvalAsXML(con, res_id, prof.usr_ent_id);
        result += getDisplayOption(con, dpo_view);

        if (topicLst != null) {
            result += "<stat num_of_topic=\"" + topicLst.length;
        } else {
            result += "<stat num_of_topic=\"0";
        }

        if (page == 0) {
            page = 1;
        }

        result += "\" page_size=\"" + page_size + "\" cur_page=\"" + page + "\"/>" + dbUtils.NEWL;
        result += "<body>" + dbUtils.NEWL;
        dbForumTopic topic;

        if (topicLst != null) {
            int count = (page-1) * page_size;
            Vector topic_vec = new Vector();

            for (int i=count; i<topicLst.length && i<count+page_size; i++) {
                topic_vec.addElement(new Long(topicLst[i]));
//                topic = new dbForumTopic();
//                topic.fto_id = topicLst[i];
//                topic.get(con);
//                result += topic.contentWithoutMsgsAsXML(con, prof, MARK_MSG);
            }

            if (topic_vec.size() != 0) {
                result += dbForumTopic.contentWithoutMsgsAsXML(con, prof, MARK_MSG, topic_vec);
            }
        }

        result += "</body>" + dbUtils.NEWL;
        result += "</module>";

        return result;
    }

    public String searchForumMsgsAsXML(Connection con, HttpSession sess, loginProfile prof, String dpo_view,
                                  String phrase, int phrase_cond, String created_by, int created_by_cond,
                                  Timestamp created_after, Timestamp created_before, String order, int page, long cos_id, int page_size)
        throws qdbException, cwSysMessage, SQLException
    {
    	if( page_size == 0 ) {
    		page_size = PAGE_SIZE;
    	}
        String sess_type = (String)sess.getAttribute(FOR_TYPE);
        String sess_cmd = (String)sess.getAttribute(FOR_CMD);
        long[] topicLst;
        Long tempId = (Long)sess.getAttribute(FOR_ID);
        long id = 0;

        if (tempId != null) {
            id = tempId.longValue();
        }

//        if (sess_type != null && sess_type.equals(FOR_TOPIC) &&
//            sess_cmd != null && sess_cmd.equals(FOR_SEARCH) &&
//            page != 0 && id == res_id) {
//            topicLst = (long[])sess.getAttribute(FOR_RESULT);
//        } else {
            dbForumTopic topic;
            long[] lst;
            long[] msgLst = null;
            Long temp;
            Vector v_lst = new Vector();

             lst = dbForumTopic.getAllTopicIDs(con, res_id, null, order);

            if (lst != null) {
                for (int i=0; i<lst.length; i++) {
                    topic = new dbForumTopic();
                    topic.fto_id = lst[i];
                    topic.get(con);
                    msgLst = dbForumMessage.searchMsgs(con, lst[i], phrase, phrase_cond, created_by, created_by_cond, created_after, created_before, order);

                    if (msgLst != null && msgLst.length != 0) {
                        v_lst.addElement(new Long(lst[i]));
                    }
                }
            }

            topicLst = new long[v_lst.size()];

            for (int i=0; i<v_lst.size(); i++) {
                temp = (Long)v_lst.elementAt(i);
                topicLst[i] = temp.longValue();
            }

            sess.setAttribute(FOR_TYPE, FOR_TOPIC);
            sess.setAttribute(FOR_CMD, FOR_SEARCH);
            sess.setAttribute(FOR_ID, new Long(res_id));

            if (topicLst != null) {
                sess.setAttribute(FOR_RESULT, topicLst);
            }
 //       }

        StringBuffer result = new StringBuffer();

        result.append(dbUtils.xmlHeader);
        result.append("<module id=\"").append(res_id).append("\" language=\"").append(res_lan).append("\" timestamp=\"");
        result.append(res_upd_date).append("\" mark_msg=\"").append(MARK_MSG).append("\">");

        // author's information
        result.append(prof.asXML());
//        result += dbResourcePermission.aclAsXML(con, res_id, prof);

        // Forum Header
        result.append(getModHeader(con, prof, cos_id));
        result.append(getModuleEval(con, res_id, prof.usr_ent_id));
//        result += getModHeader(con, prof);
//        result += dbModuleEvaluation.getModuleEvalAsXML(con, res_id, prof.usr_ent_id);
        result.append(getDisplayOption(con, dpo_view));

        if (topicLst != null) {
            result.append("<stat num_of_topic=\"").append(topicLst.length);
        } else {
            result.append("<stat num_of_topic=\"0");
        }

        if (page == 0) {
            page = 1;
        }

        result.append("\" page_size=\"").append(page_size).append("\" cur_page=\"").append(page).append("\"/>");
        result.append("<body>");
        //dbForumTopic topic;

        if (topicLst != null) {
            int count = (page-1) * page_size;
            Vector topic_vec = new Vector();

            for (int i=count; i<topicLst.length && i<count+page_size; i++) {
                topic_vec.addElement(new Long(topicLst[i]));
/*                topic = new dbForumTopic();
                topic.fto_id = topicLst[i];
                topic.get(con);
                result += topic.contentWithoutMsgsAsXML(con, prof, MARK_MSG);*/
            }

            if (topic_vec.size() != 0) {
                result.append(dbForumTopic.contentWithoutMsgsAsXML(con, prof, MARK_MSG, topic_vec));
            }
        }

        result.append("</body>");
        result.append("</module>");

        return result.toString();
    }

    public String searchForumAsXML(Connection con, HttpSession sess, loginProfile prof, String dpo_view,
                                  String phrase, int phrase_cond, String created_by, int created_by_cond,
                                  Timestamp created_after, Timestamp created_before, String order, int page, long cos_id, int page_size)
        throws qdbException, cwSysMessage, SQLException
    {
    	if( page_size == 0 ){
    		page_size = PAGE_SIZE;
    	}
        String sess_type = (String)sess.getAttribute(FOR_TYPE);
        String sess_cmd = (String)sess.getAttribute(FOR_CMD);
        Long temp;
        long[] topicLst = null;
        Long tempId = (Long)sess.getAttribute(FOR_ID);
        long id = 0;

        if (tempId != null) {
            id = tempId.longValue();
        }

//        if (sess_type != null && sess_type.equals(FOR_TOPIC) &&
//            sess_cmd != null && sess_cmd.equals(FOR_SEARCH) &&
//            page != 0 && id == res_id) {
//            topicLst = (long[])sess.getAttribute(FOR_RESULT);
//            System.out.println("if" + topicLst.length);
            
//        } else {
            dbForumTopic topic;
            long[] lstMatched = null;
            long[] lst = null;
            long[] msgLst = null;
            Vector v_lst = new Vector();
            Vector v_lstMatched = new Vector();

            lst = dbForumTopic.getAllTopicIDs(con, res_id, null, order);

            if (lst != null) {
                lstMatched = dbForumTopic.searchTopics(con, res_id, phrase, phrase_cond, created_by, created_by_cond, created_after, created_before, order);

                if (lstMatched != null) {
                    for (int i=0; i<lstMatched.length; i++) {
                        v_lstMatched.addElement(new Long(lstMatched[i]));
                    }
                }

                if (lst.length > v_lstMatched.size()) {
                    for (int i=0; i<lst.length; i++) {

                        if (v_lstMatched.contains(new Long(lst[i]))) {
                            v_lst.addElement(new Long(lst[i]));
                        } else {
                            topic = new dbForumTopic();
                            topic.fto_id = lst[i];
                            topic.get(con);
                            msgLst = dbForumMessage.searchMsgs(con, lst[i], phrase, phrase_cond, created_by, created_by_cond, created_after, created_before, order);

                            if (msgLst != null && msgLst.length != 0) {
                                v_lst.addElement(new Long(lst[i]));
                            }
                        }
                    }

                    topicLst = new long[v_lst.size()];
                    for (int i=0; i<v_lst.size(); i++) {
                        temp = (Long)v_lst.elementAt(i);
                        topicLst[i] = temp.longValue();
                    }
                } else {
                    topicLst = lst;
                }
            }


            sess.setAttribute(FOR_TYPE, FOR_TOPIC);
            sess.setAttribute(FOR_CMD, FOR_SEARCH);
            sess.setAttribute(FOR_ID, new Long(res_id));

            if (topicLst != null) {
                sess.setAttribute(FOR_RESULT, topicLst);
            }
//        }

        StringBuffer result = new StringBuffer();

        result.append(dbUtils.xmlHeader);
        result.append("<module id=\"").append(res_id).append("\" language=\"").append(res_lan).append("\" timestamp=\"");
        result.append(res_upd_date).append("\" mark_msg=\"").append(MARK_MSG).append("\">");

        // author's information
        result.append(prof.asXML());
//        result += dbResourcePermission.aclAsXML(con, res_id, prof);

        // Forum Header
        result.append(getModHeader(con, prof, cos_id));
        result.append(getModuleEval(con, res_id, prof.usr_ent_id));
//        result += getModHeader(con, prof);
//        result += dbModuleEvaluation.getModuleEvalAsXML(con, res_id, prof.usr_ent_id);
        result.append(getDisplayOption(con, dpo_view));

        if (topicLst != null) {
            result.append("<stat num_of_topic=\"").append(topicLst.length);
        } else {
            result.append("<stat num_of_topic=\"0");
        }

        if (page == 0) {
            page = 1;
        }

        result.append("\" page_size=\"").append(page_size).append("\" cur_page=\"").append(page + "\"/>");
        result.append("<body>");

        if (topicLst != null) {
            int count = (page-1) * page_size;
            Vector topic_vec = new Vector();

            for (int i=count; i<topicLst.length && i<count+page_size; i++) {
                topic_vec.addElement(new Long(topicLst[i]));
/*                topic = new dbForumTopic();
                topic.fto_id = topicLst[i];
                topic.get(con);
                result += topic.contentWithoutMsgsAsXML(con, prof, MARK_MSG);*/
            }

            if (topic_vec.size() != 0) {
                result.append(dbForumTopic.contentWithoutMsgsAsXML(con, prof, MARK_MSG, topic_vec));
            }
        }

        result.append("</body>");
        result.append("</module>");

        return result.toString();
    }

    public String searchMsgsAsXML(Connection con, HttpSession sess, loginProfile prof, String dpo_view, long topic_id,
                                  String phrase, int phrase_cond, String created_by, int created_by_cond,
                                  Timestamp created_after, Timestamp created_before, String order, int page, long cos_id)
        throws qdbException, cwSysMessage, SQLException
    {
        String sess_type = (String)sess.getAttribute(FOR_TYPE);
        String sess_cmd = (String)sess.getAttribute(FOR_CMD);
        long[] msgLst;
        Long tempId = (Long)sess.getAttribute(FOR_ID);
        long id = 0;

        if (tempId != null) {
            id = tempId.longValue();
        }

        if (sess_type != null && sess_type.equals(FOR_MSG) &&
            sess_cmd != null && sess_cmd.equals(FOR_SEARCH) &&
            page != 0 && id == topic_id) {
            msgLst = (long[])sess.getAttribute(FOR_RESULT);
        } else {
            msgLst = dbForumMessage.searchMsgs(con, topic_id, phrase, phrase_cond, created_by, created_by_cond, created_after, created_before, order);
            sess.setAttribute(FOR_TYPE, FOR_MSG);
            sess.setAttribute(FOR_CMD, FOR_SEARCH);
            sess.setAttribute(FOR_ID, new Long(topic_id));

            if (msgLst != null) {
                sess.setAttribute(FOR_RESULT, msgLst);
            }
        }

        dbForumTopic topic = new dbForumTopic();
        topic.fto_id = topic_id;
        topic.get(con);

        String result = "<?xml version=\"1.0\" encoding=\"" + dbUtils.ENC_UTF +
                   "\" standalone=\"no\" ?>" + dbUtils.NEWL;

        result += "<module id=\""+ res_id + "\" language=\"" + res_lan + "\" timestamp=\"" +
                  res_upd_date + "\" mark_msg=\"" + MARK_MSG + "\">" + dbUtils.NEWL;

        // author's information
        result += prof.asXML() + dbUtils.NEWL;
//        result += dbResourcePermission.aclAsXML(con, res_id, prof);

        // Forum Header
        result += getModHeader(con, prof, cos_id);
        result += getModuleEval(con, res_id, prof.usr_ent_id);
//        result += getModHeader(con, prof);
//        result += dbModuleEvaluation.getModuleEvalAsXML(con, res_id, prof.usr_ent_id);
        result += getDisplayOption(con, dpo_view);
        result += "<body>" + dbUtils.NEWL;
        result += topic.contentAsXML(con, prof, msgLst, MARK_MSG, page);
        result += "</body>" + dbUtils.NEWL;
        result += "</module>";

        return result;
    }


    public String getModHeader(Connection con, loginProfile prof, long cos_id) throws qdbException, cwSysMessage{
        // Default header show the minimal information
        return getModHeader(con, prof, cos_id, false);
    }    
    
    public String getModHeader(Connection con, loginProfile prof, long cos_id, boolean bDetailView) throws qdbException, cwSysMessage{

        StringBuffer result = new StringBuffer();
        String tmp_end_datetime = null;  //Dennis, store the output end datetime as it may == "UNLIMITED"
        Timestamp cur_time = dbUtils.getTime(con);

/*        String ATTEMPTED = RES_ATTEMPTED_FALSE;
        // Check if the module was attempted
        long cnt = dbProgress.attemptNum(con , mod_res_id);
        if (cnt>0)
            ATTEMPTED = RES_ATTEMPTED_TRUE;*/

        //check if the end_datetime need to be converted to "UNLIMITED"
        if(mod_eff_end_datetime != null)
            if(dbUtils.isMaxTimestamp(mod_eff_end_datetime) == true)
                tmp_end_datetime = dbUtils.UNLIMITED; //convert to String to "UNLIMITED"
            else
                tmp_end_datetime = mod_eff_end_datetime.toString();


        //Dennis, display option, get the attributes that needed to be displayed
/*        Timestamp PSTART = null;
        Timestamp PCOMPLETE = null;
        Timestamp PLASTACC = null;
        long PATTEMPTNBR = 0;

        dbProgress pgr = new dbProgress();
        pgr.pgr_usr_id = prof.usr_id;
        pgr.pgr_res_id = mod_res_id;
        PATTEMPTNBR = pgr.usrAttemptNum(con,mod_res_id,prof.usr_id);
        if(PATTEMPTNBR > 0) {
            try {
                pgr.get(con,1);
                PSTART = pgr.pgr_start_datetime;

                pgr.get(con);
                PCOMPLETE = pgr.pgr_complete_datetime;
                PLASTACC = pgr.pgr_last_acc_datetime;
            }
            catch(qdbErrMessage e) {
                throw new qdbException (e.getMessage());
            }
        }*/

        //Dennis, get the Event body
        dbEvent dbevt;
        String evtBody = "";
        if(dbEvent.isEventType(res_subtype)) {
            dbevt = new dbEvent();
            dbevt.evt_res_id = mod_res_id;
            evtBody = dbevt.getEventAsXML(con);
        }

        // Course id
//        long cosId = dbModule.getCosId(con, mod_res_id);

        result.append("<header course_id=\"").append(cos_id).append("\" difficulty=\"").append(res_difficulty);
        result.append("\" duration=\"").append(res_duration);
        result.append("\" time_limit=\"").append(res_duration);
        result.append("\" suggested_time=\"").append(res_duration);
        result.append("\" max_score=\"" + mod_max_score);
        result.append("\" pass_score=\"").append(mod_pass_score).append("\" privilege=\"");
                                                             // should be change to res_type
        result.append(res_privilege).append("\" status=\"").append(res_status).append("\" type=\"").append(res_type);
        result.append("\" subtype=\"").append(res_subtype);
        result.append("\" max_attempt=\"").append(mod_max_attempt).append("\" max_usr_attempt=\"").append(mod_max_usr_attempt);
        result.append("\" score_ind=\"").append(mod_score_ind);
        result.append("\" score_reset=\"").append(mod_score_reset);
        result.append("\" logic=\"").append(mod_logic);

        if( res_subtype!=null && ( res_subtype.equals("CHT") || res_subtype.equals("VCR") ) ){
            result.append("\" ts_host=\"").append(mod_tshost);
            result.append("\" ts_port=\"").append(mod_tsport);
            result.append("\" www_port=\"").append(mod_wwwport);
        }

        result.append("\" eff_start_datetime=\"").append(mod_eff_start_datetime);
        result.append("\" eff_end_datetime=\"").append(tmp_end_datetime);
        result.append("\" cur_time=\"").append(cur_time);
        result.append("\">");

/*        result.append("\" attempted=\"").append(ATTEMPTED);
        result.append("\" attempt_nbr=\"").append(PATTEMPTNBR);
        result.append("\" pgr_start=\"").append(PSTART);
        result.append("\" pgr_complete=\"").append(PCOMPLETE);*/

        // for AICC only
/*        this.getAicc(con);
        if (mod_web_launch != null) {
            result += "\" mod_web_launch=\"" + dbUtils.esc4XML(mod_web_launch);
        }
        else {
            result += "\" mod_web_launch=\"" + "";
        }
        if (res_src_type != null) {
            result += "\" res_src_type=\"" + res_src_type;
        }
        else {
            result += "\" res_src_type=\"" + "";
        }
        if (res_src_link != null) {
            //result += "\" res_src_link=\"" + res_src_link;
            result += "\" res_src_link=\"" + dbUtils.esc4XML(res_src_link);
        }
        else {
            result += "\" res_src_link=\"" + "";
        }*/
        //

//        result +=       "\" pgr_last_acc=\"" + PLASTACC + "\">";
        //Dennis, display option here
/*        result += dbUtils.NEWL;
        result += "<source type=\"" + res_src_type + "\">"+ dbUtils.esc4XML(res_src_link) + "</source>" + dbUtils.NEWL; */
        //result += "<url>" + dbUtils.esc4XML(res_url) + "</url>" + dbUtils.NEWL;
        //result += "<filename>" + dbUtils.esc4XML(res_filename) + "</filename>" + dbUtils.NEWL;
        result.append("<title>").append(dbUtils.esc4XML(res_title)).append("</title>");
        result.append("<desc>").append(dbUtils.esc4XML(res_desc)).append("</desc>");
        result.append("<annotation>").append(res_annotation).append("</annotation>");
        result.append("<instruction>").append(dbUtils.esc4XML(mod_instruct)).append("</instruction>");
        result.append("<instructor>").append(dbUtils.esc4XML(res_instructor_name)).append("</instructor>");

        if (bDetailView) {
            result.append(dbCourse.getInstructorList(con, cos_id, mod_usr_id_instructor, prof.root_ent_id));
        }

        //result += "<moderator>" + dbUtils.esc4XML(getOwnerName(con)) + "</moderator>" + dbUtils.NEWL;
        result.append("<organization>").append(dbUtils.esc4XML(res_instructor_organization)).append("</organization>");

        if(dbEvent.isEventType(res_subtype))
            result.append(evtBody);

/*        result += "<template_list cur_tpl=\"" + res_tpl_name + "\">" + dbUtils.NEWL;

        String tpl_type = "";
            tpl_type = mod_type;

        result += dbTemplate.tplListContentXML(con, prof, tpl_type);
        result += "</template_list>" + dbUtils.NEWL; ;*/

        result.append("</header>");

        return result.toString();
    }

    public String getModuleEval(Connection con, long mod_res_id, long usr_ent_id) throws SQLException {
        StringBuffer result = new StringBuffer();
        PreparedStatement stmt = con.prepareStatement("select mov_cos_id, mov_last_acc_datetime, mov_status from ModuleEvaluation where mov_ent_id = ? and mov_mod_id = ?");
        stmt.setLong(1, usr_ent_id);
        stmt.setLong(2, mod_res_id);
        ResultSet rs = stmt.executeQuery();
        String cos_id = null;
        Timestamp last_acc = null;
        String status = null;

        if (rs.next()) {
            cos_id = rs.getString("mov_cos_id");
            last_acc = rs.getTimestamp("mov_last_acc_datetime");
            status = rs.getString("mov_status");
        }

        stmt.close();

//        if (cos_id != null) {
            result.append("<aicc_data>");
            result.append("<attempt course_id=\"").append(cos_id).append("\" student_id=\"").append(usr_ent_id);
            result.append("\" lesson_id=\"").append(mod_res_id).append("\" last_acc_datetime=\"").append(last_acc).append("\" status=\"").append(status).append("\"/>");
            result.append("</aicc_data>");
//        }

        return result.toString();
    }

    public static Hashtable getNumByForum(Connection con, String sql) throws SQLException{
        Hashtable newHT = new Hashtable();
        PreparedStatement stmt = con.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()){
            newHT.put(new Long(rs.getLong(2)), new Long(rs.getLong(1)));
        }
        stmt.close();
        return newHT;
    }

    public static final String SQL_GET_MSG_NUM_BY_FORUM = "select count(fmg_id), fmg_fto_res_id from forumMessage group by fmg_fto_res_id ORDER BY fmg_fto_res_id ";
    public static final String SQL_GET_TOPIC_NUM_BY_FORUM = "select count(fto_id), fto_res_id from forumTopic group by fto_res_id order by fto_res_id ";

    public static String getUnreadMsgSQL(long usrEntId){
        StringBuffer sql = new StringBuffer();
        sql.append("select count(fmg_id), fmg_fto_res_id  from forumMessage , moduleEvaluation ");
        sql.append("where mov_mod_id = fmg_fto_res_id and mov_ent_id = ").append(usrEntId).append(" and fmg_create_datetime > mov_last_acc_datetime ");
        sql.append("group by fmg_fto_res_id ");
        sql.append("union ");
        sql.append("select count(fmg_id), fmg_fto_res_id  from forumMessage ");
        sql.append("where fmg_fto_res_id not in (select mov_mod_id from moduleEvaluation where mov_ent_id = ").append(usrEntId).append(")");
        sql.append("group by fmg_fto_res_id ");
        sql.append("order by fmg_fto_res_id ");
        return sql.toString();
    }

    public static String getPublicForumAsXML(Connection con, loginProfile prof, boolean isMaintain ,boolean isStudyGroupMod ,long sgp_id) throws cwException, SQLException, qdbException{
        Hashtable topicNum = getNumByForum(con, SQL_GET_TOPIC_NUM_BY_FORUM);
        Hashtable msgNum = getNumByForum(con, SQL_GET_MSG_NUM_BY_FORUM);
        Hashtable unreadMsgNum = getNumByForum(con, getUnreadMsgSQL(prof.usr_ent_id));
        // if isMaintain, no need to check status, and vera vice
        Vector vtMod = dbModule.getPublicModLst(con, prof.root_ent_id, "FOR", !isMaintain, isStudyGroupMod ,sgp_id, null);
        StringBuffer result = new StringBuffer();

        result.append("<forum_list>").append(cwUtils.NEWL);
        for (int i=0; i<vtMod.size();i++){
            dbModule dbmod = (dbModule) vtMod.elementAt(i);
            result.append("<forum id=\"").append(dbmod.mod_res_id);
            result.append("\" eff_start_datetime=\"").append(dbmod.mod_eff_start_datetime);
            result.append("\" eff_end_datetime=\"").append(dbmod.mod_eff_end_datetime);
            result.append("\" status=\"").append(dbmod.res_status);
            result.append("\" timestamp=\"").append(dbmod.res_upd_date);
            Object total = topicNum.get(new Long(dbmod.mod_res_id));
            result.append("\" num_of_topic=\"").append((total != null)?total.toString():"0");
            total = msgNum.get(new Long(dbmod.mod_res_id));
            result.append("\" num_of_msg=\"").append((total != null)?total.toString():"0");
            total = unreadMsgNum.get(new Long(dbmod.mod_res_id));
            result.append("\" num_of_unread=\"").append((total != null)?total.toString():"0");
            result.append("\" >").append(cwUtils.NEWL);

            result.append("<title>").append(dbUtils.esc4XML(dbmod.res_title)).append("</title>");
            result.append(dbResourcePermission.aclAsXML(con, dbmod.mod_res_id, prof));

            /*
            if (dbmod.mod_usr_id_instructor != null){
                dbRegUser usr = new dbRegUser();
                usr.usr_ent_id = dbRegUser.getEntId(con, dbmod.mod_usr_id_instructor);
                usr.get(con);

                result.append("<moderator>").append(usr.getUserShortXML(con, false, false)).append("</moderator>");
            }
            */
            result.append("</forum>");
        }
        result.append("</forum_list>");
        return result.toString();
    }
    
    public static final String MOD_TYPE_REVIEW = "REVIEW";
    
	public static String getBookReviewAsXmlByReviewId(Connection con, long review_id) throws cwException, SQLException {
		StringBuffer result = new StringBuffer();
		dbForumMessage fm = dbForumMessage.messageOfBookReview(con, review_id);
		if (fm != null) {
			result.append("<book_review title=\"").append(cwUtils.esc4XML(fm.fmg_title)).append("\"");
			result.append(" res_id=\"").append(fm.fmg_fto_res_id).append("\"");
			result.append(" fto_id=\"").append(fm.fmg_fto_id).append("\"");
			result.append(" review_id=\"").append(fm.fmg_id).append("\"");
			result.append(" fmg_usr_id=\"").append(fm.fmg_usr_id).append("\"");
//			result.append(" update_time=\"").append(fm.fmg_update_datetime).append("\"");
//			result.append(" fmg_rating=\"").append(fm.fmg_rating).append("\"");
//			result.append(" usr_bill=\"").append(cwUtils.esc4XML(fm.usr_bill)).append("\"");
			result.append(" content=\"").append(cwUtils.esc4XML(fm.fmg_msg)).append("\"");
//			result.append(" node_id=\"").append(fm.fto_nod_id).append("\"");
//			result.append(" itm_title=\"").append(cwUtils.esc4XML(fm.itm_title)).append("\"");
			result.append("/>");
		}
		return result.toString();
	}
}