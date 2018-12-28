package com.cw.wizbank.qdb;

import java.util.*;
import java.sql.*;
import javax.servlet.http.*;
import com.cw.wizbank.util.cwSysMessage;

public class dbFaq extends dbModule {
    public static final String MOD_TYPE_FAQ = "FAQ";
    public static final String FAQ_TYPE = "FAQ_TYPE";
    public static final String FAQ_ID = "FAQ_ID";
    public static final String FAQ_TOPIC = "FAQ_TOPIC";
    public static final String FAQ_MSG = "FAQ_MSG";
    public static final String FAQ_CMD = "FAQ_CMD";
    public static final String FAQ_VIEW = "FAQ_VIEW";
    public static final String FAQ_SEARCH = "FAQ_SEARCH";
    public static final String FAQ_RESULT = "FAQ_RESULT";
    public static final int PAGE_SIZE = 10;
    public int MARK_MSG;
    
    public List dbFqTopic;
    public dbFaq() {
        super();
        MARK_MSG = 0;
        dbFqTopic = new ArrayList();
    }  
    
    public dbFaq(dbModule dbmod) {
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
        //mod_logic = dbmod.mod_logic;
 
        res_id = dbmod.res_id;
        res_lan = dbmod.res_lan;
        res_title = dbmod.res_title;
        res_desc = dbmod.res_desc;
        res_type = MOD_TYPE_FAQ; // override
        res_subtype = MOD_TYPE_FAQ; // override
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
        
    //do not check moditify privilege at all
    public void aeDel(Connection con, loginProfile prof)
        throws qdbException, qdbErrMessage ,cwSysMessage
    {
        dbFaqTopic.delAllTopics(con, prof, res_id);
            
        super.del(con);
                
        return;
    } 

        
    public void del(Connection con, loginProfile prof)
        throws qdbException, qdbErrMessage ,cwSysMessage
    {
        // check User Right
        // Check at Module
        //if (!dbResourcePermission.hasPermission(con, res_id, prof,
        //                                dbResourcePermission.RIGHT_WRITE)) {
        //    throw new qdbErrMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);
        //}            
           
        dbFaqTopic.delAllTopics(con, prof, res_id);
            
        //super.delChild(con);
        super.del(con, prof);
                
        return;
    } 

    public String outputTopicsAsXML(Connection con, HttpSession sess, loginProfile prof, String dpo_view, String order, int page, int page_size, String ssoXml)
        throws qdbException, cwSysMessage, SQLException
    {
        String sess_type = (String)sess.getAttribute(FAQ_TYPE);
        String sess_cmd = (String)sess.getAttribute(FAQ_CMD);
        long[] topicLst;
        Long tempId = (Long)sess.getAttribute(FAQ_ID);
        long id = 0;
        
        if (tempId != null) {
            id = tempId.longValue();
        }
        
        if (sess_type != null && sess_type.equals(FAQ_TOPIC) && 
            sess_cmd != null && sess_cmd.equals(FAQ_VIEW) &&
            page != 0 && id == res_id) {
            topicLst = (long[])sess.getAttribute(FAQ_RESULT);
        } else {
            topicLst = dbFaqTopic.getAllTopicIDs(con, res_id, order); 
            sess.setAttribute(FAQ_TYPE, FAQ_TOPIC);
            sess.setAttribute(FAQ_CMD, FAQ_VIEW);
            sess.setAttribute(FAQ_ID, new Long(res_id));
            
            if (topicLst != null) {
                sess.setAttribute(FAQ_RESULT, topicLst);        
            }
            else {
                sess.removeAttribute(FAQ_RESULT);
            }
        }
          
        String result = "<?xml version=\"1.0\" encoding=\"" + dbUtils.ENC_UTF + 
                        "\" standalone=\"no\" ?>" + dbUtils.NEWL;
        
        result += "<module id=\""+ res_id + "\" res_status=\"" + res_status + "\" language=\"" + res_lan + "\" timestamp=\"" + 
                  res_upd_date + "\" mark_msg=\"" + MARK_MSG + "\">" + dbUtils.NEWL;
           
        // author's information
        result += prof.asXML() + dbUtils.NEWL;
        result += ssoXml;
        result += dbResourcePermission.aclAsXML(con, res_id, prof);

        // Faq Header 
        result += getModHeader(con, prof);
        result += dbModuleEvaluation.getModuleEvalAsXML(con, res_id, prof.usr_ent_id, tkh_id);  

        result += getDisplayOption(con, dpo_view);
        result += getIsEnrollmentRelatedAsXML(con);

        if (topicLst != null) {
            result += "<stat num_of_topic=\"" + topicLst.length;
        } else {
            result += "<stat num_of_topic=\"0";            
        }
        
        if (page == 0) {
            page = 1;
        }
        if (page_size <= 0) {
            page_size = PAGE_SIZE;
        }
        
        result += "\" page_size=\"" + page_size + "\" cur_page=\"" + page + "\"/>" + dbUtils.NEWL;
        result += "<body>" + dbUtils.NEWL;
        dbFaqTopic topic;

        if (topicLst != null) {                                    
            int count = (page-1) * page_size;    

            for (int i=count; i<topicLst.length && i<count+page_size; i++) {    
                topic = new dbFaqTopic();
                topic.fto_id = topicLst[i];
                topic.get(con);
                result += topic.contentWithoutMsgsAsXML(con, prof, MARK_MSG);
            }
        }
        
        result += "</body>" + dbUtils.NEWL;
        result += "</module>"; 
            
        return result;        
    }    
    
    public String outputMsgsAsXML(Connection con, HttpSession sess, loginProfile prof, String dpo_view, long topic_id, long msg_id, int commentOn, String order, int page, int page_size, String ssoXml)
        throws qdbException, cwSysMessage, SQLException
    {
        String result = "<?xml version=\"1.0\" encoding=\"" + dbUtils.ENC_UTF + 
                        "\" standalone=\"no\" ?>" + dbUtils.NEWL;
        
        result += "<module id=\""+ res_id + "\" res_status=\"" + res_status + "\" language=\"" + res_lan + "\" timestamp=\"" + 
                  res_upd_date + "\" mark_msg=\"" + MARK_MSG + "\">" + dbUtils.NEWL;
           
        // author's information
        result += prof.asXML() + dbUtils.NEWL;
        result += ssoXml;
        result += dbResourcePermission.aclAsXML(con, res_id, prof);

        // Faq Header 
        result += getModHeader(con, prof);
        result += dbModuleEvaluation.getModuleEvalAsXML(con, res_id, prof.usr_ent_id, tkh_id); 

        result += getDisplayOption(con, dpo_view);
        result += getIsEnrollmentRelatedAsXML(con);
        result += "<body>" + dbUtils.NEWL;

        dbFaqTopic topic = new dbFaqTopic();

        topic.fto_id = topic_id;
        topic.get(con);
        
        if (msg_id == 0) {
            result += topic.contentWithMsgsAsXML(con, sess, prof, MARK_MSG, 0, commentOn, order, page, page_size);
        } else {
            long[] lst = new long[1];
            
            lst[0] = msg_id;
            result += topic.contentAsXML(con, prof, lst, MARK_MSG, page, page_size, 1, 1);
        }
        
        result += "</body>" + dbUtils.NEWL;
        result += "</module>"; 
            
        return result;        
    }

    public String searchInFaqAsXML(Connection con, HttpSession sess, loginProfile prof, String dpo_view,
                                   String phrase, int phrase_cond, String created_by, int created_by_cond,
                                   Timestamp created_after, Timestamp created_before, String order, int page, int page_size,
                                   int search_que, int search_ans, int search_com)
        throws qdbException, cwSysMessage
    {
        String sess_type = (String)sess.getAttribute(FAQ_TYPE);
        String sess_cmd = (String)sess.getAttribute(FAQ_CMD);
        long[] topicLst;
        Long tempId = (Long)sess.getAttribute(FAQ_ID);
        long id = 0;
        
        if (tempId != null) {
            id = tempId.longValue();
        }
        
        if (sess_type != null && sess_type.equals(FAQ_TOPIC) && 
            sess_cmd != null && sess_cmd.equals(FAQ_SEARCH) &&
            page != 0 && id == res_id) {
            topicLst = (long[])sess.getAttribute(FAQ_RESULT);
        } else {
            dbFaqTopic topic;
            long[] lst;
            long[] msgLst = null;
            Long temp;
            Vector v_lst = new Vector();
            
            lst = dbFaqTopic.getAllTopicIDs(con, res_id, order);    

            if (lst != null) {
                for (int i=0; i<lst.length; i++) {
                    msgLst = dbFaqMessage.searchMsgs(con, lst[i], phrase, phrase_cond, created_by, created_by_cond, 
                                                     created_after, created_before, order, search_que, search_ans, search_com);
                    
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

            sess.setAttribute(FAQ_TYPE, FAQ_TOPIC);
            sess.setAttribute(FAQ_CMD, FAQ_SEARCH);
            sess.setAttribute(FAQ_ID, new Long(res_id));
            
            if (topicLst != null) {
                sess.setAttribute(FAQ_RESULT, topicLst);        
            }
            else {
                sess.removeAttribute(FAQ_RESULT);
            }
        }
                
        String result = "<?xml version=\"1.0\" encoding=\"" + dbUtils.ENC_UTF + 
                        "\" standalone=\"no\" ?>" + dbUtils.NEWL;
        
        result += "<module id=\""+ res_id + "\" language=\"" + res_lan + "\" timestamp=\"" + 
                  res_upd_date + "\" mark_msg=\"" + MARK_MSG + "\">" + dbUtils.NEWL;
           
        // author's information
        result += prof.asXML() + dbUtils.NEWL;                    
        result += dbResourcePermission.aclAsXML(con, res_id, prof);

        // Faq Header 
        result += getModHeader(con, prof);
        result += dbModuleEvaluation.getModuleEvalAsXML(con, res_id, prof.usr_ent_id, tkh_id);        
        result += getDisplayOption(con, dpo_view);
        
        if (topicLst != null) {
            result += "<stat num_of_topic=\"" + topicLst.length;
        } else {
            result += "<stat num_of_topic=\"0";            
        }

        if (page == 0) {
            page = 1;
        }
        if (page_size <= 0) {
            page_size = PAGE_SIZE;
        }

        result += "\" page_size=\"" + page_size + "\" cur_page=\"" + page + "\"/>" + dbUtils.NEWL;
        result += "<body>" + dbUtils.NEWL;
        dbFaqTopic topic;

        if (topicLst != null) {                                    
            int count = (page-1) * page_size;    

            for (int i=count; i<topicLst.length && i<count+page_size; i++) {    
                topic = new dbFaqTopic();
                topic.fto_id = topicLst[i];
                topic.get(con);
                result += topic.contentWithoutMsgsAsXML(con, prof, MARK_MSG);
            }
        }
        
        result += "</body>" + dbUtils.NEWL;
        result += "</module>"; 
            
        return result;
    }

    public String searchInTopicAsXML(Connection con, HttpSession sess, loginProfile prof, String dpo_view, long topic_id,
                                  String phrase, int phrase_cond, String created_by, int created_by_cond, 
                                  Timestamp created_after, Timestamp created_before, String order, int page, int page_size,
                                  int search_que, int search_ans, int search_com)
        throws qdbException, cwSysMessage
    {
        String sess_type = (String)sess.getAttribute(FAQ_TYPE);
        String sess_cmd = (String)sess.getAttribute(FAQ_CMD);
        long[] msgLst;
        Long tempId = (Long)sess.getAttribute(FAQ_ID);
        long id = 0;
        
        if (tempId != null) {
            id = tempId.longValue();
        }
        
        if (sess_type != null && sess_type.equals(FAQ_MSG) && 
            sess_cmd != null && sess_cmd.equals(FAQ_SEARCH) &&
            page != 0 && id == topic_id) {
            msgLst = (long[])sess.getAttribute(FAQ_RESULT);
        } else {
            msgLst = dbFaqMessage.searchMsgs(con, topic_id, phrase, phrase_cond, created_by, created_by_cond, 
                                                created_after, created_before, order, search_que, search_ans, search_com);
                                                
            sess.setAttribute(FAQ_TYPE, FAQ_MSG);
            sess.setAttribute(FAQ_CMD, FAQ_SEARCH);
            sess.setAttribute(FAQ_ID, new Long(topic_id));
            
            if (msgLst != null) {
                sess.setAttribute(FAQ_RESULT, msgLst);        
            }
            else {
                sess.removeAttribute(FAQ_RESULT);
            }
        }

        dbFaqTopic topic = new dbFaqTopic();
        topic.fto_id = topic_id;
        topic.get(con);                
                
        String result = "<?xml version=\"1.0\" encoding=\"" + dbUtils.ENC_UTF + 
                   "\" standalone=\"no\" ?>" + dbUtils.NEWL;
        
        result += "<module id=\""+ res_id + "\" language=\"" + res_lan + "\" timestamp=\"" + 
                  res_upd_date + "\" mark_msg=\"" + MARK_MSG + "\">" + dbUtils.NEWL;
           
        // author's information
        result += prof.asXML() + dbUtils.NEWL;                    
        result += dbResourcePermission.aclAsXML(con, res_id, prof);

        // Faq Header 
        result += getModHeader(con, prof);
        result += dbModuleEvaluation.getModuleEvalAsXML(con, res_id, prof.usr_ent_id, tkh_id);        
        result += getDisplayOption(con, dpo_view);
        result += "<body>" + dbUtils.NEWL;
        result += topic.contentAsXML(con, prof, msgLst, MARK_MSG, page, page_size, 0, 0);
        result += "</body>" + dbUtils.NEWL;
        result += "</module>"; 
            
        return result;        
    }
}