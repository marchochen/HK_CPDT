package com.cw.wizbank.competency;

import javax.servlet.ServletRequest;
import java.sql.Timestamp;
import java.util.*;
import com.cw.wizbank.*;
import com.cw.wizbank.db.*;
import com.cw.wizbank.util.*;
import com.cw.wizbank.qdb.dbUserGroup;

public class CompetencyReqParam extends ReqParam {
    
    private final static String DELIMITER   = "~";
    private final static String COMMA       = ",";
    private final static String RESOLVED    = "RESOLVED";
    private final static String BEHAV_DELIMITER = ":_:_:";
    
//    public DbCmSkillTreeNode    node = null;
    public DbCmSkill            skill = null;
    public DbCmSkillSet         skillSet = null;
    public DbCmSkillScale       skillScale = null;
    public DbCmSkillTreeNode	skillTreeNode = null;
    public CmSkillSetManager    skillSetMgr = null;
    public DbCmSkillSetCoverage skillCoverage = null;
    public Vector               skillLevelVec = null;
    public dbUserGroup          dbUsg = null;
    public CmAssessmentManager  assMgr = null;
    public DbCmAssessment       dbAss = null;
        
    /**
    Pagination and sorting parameters
    */
    public Timestamp    pagetime = null;
    public int          cur_page;
    public int          pagesize;
    public String       order_by;
    public String       sort_by;
    
    public boolean      showAll;    
    public boolean      refresh;
    public String       picked_skl_id_lst;
    public String       level_lst;
    public String       priority_lst;
    public String       skl_id_lst;
    public String       sks_title;
    public boolean      sks_comment_ind;
    public String       sks_id_lst;
    public String       assessorQ;
    public String       assesseeQ;
    public String       usr_ent_id_lst;
    public String       usr_type_lst;
    public String       usr_weight_lst;
    public long         skl_id;
    public long         sks_id;    
    public long         usr_ent_id;
    public Timestamp    review_start;
    public Timestamp    review_end;
    public Timestamp    notify_date;
    public Timestamp    due_date;
    public boolean      status_prepared;
    public boolean      status_collected;
    public boolean      status_notified;
    public boolean      status_resolved;
    public long         asm_id;
    public Vector       commentVec;
    public Vector       sectionVec;
    public Vector       answerVec;
    public boolean      submit;
    public boolean      self_ass;
    public String       score_lst;
    public boolean      showSkillPath;
    public long[]       mgmt_assessor_ent_id_list;
    public String[]     assessorQList;
    public String[]     assesseeQList;
    public boolean[]    commentIndList;
	public String[]		behav_desc;
	public long[]		behav_order;
	public long[]		behav_skb_id;
	    
    // for skill gap
    public long         source_id;
    public long         target_id;
    public Timestamp    source_date;
    public Timestamp    target_date;
    public long         source_asm_id;
    public long         target_asm_id;
    public boolean      source_show_all;
    public boolean      target_show_all;
    public String       source_type;
    public String       target_type;
    public boolean      show_course_recomm;
    public long         itm_id;

    //skill search
    public String       skb_description;
    public String       skb_title;
    public String       ssl_title;
    public String       sle_label;
    public String       sle_description;
    public int          sle_level;
    public String       ssl_share;
    
    //for prepInsSkill
    public long[]       scale_id_lst;
    
    //for assessment notification
    public String sender_usr_id;
    public String notify_msg_subject;
    public String collect_msg_subject;

    //for test assessment submission
    public long mod_res_id;
    
    //for user search 
    public Timestamp reviewDate;
    public long[] skillIdList;
    public float[] answerList;
    public float[] levelList;
    public String[] typeList;
    public String comment;
    public long skillSetId;
        
	public Hashtable h_id_value = null;        
        
    // batch assessment
    public DbCmAssessment[]     batchAsm = null;
    public String               usr_type = null;
    public String[]             assessorRoleList = null;    // for prep
    public Timestamp            assessorNotifyDate = null;  // for prep
    public Timestamp            assessorDueDate = null;     // for prep
    public String[]             resolverRoleList = null;    // for prep
    public Timestamp            resolverNotifyDate = null;  // for prep
    public Timestamp            resolverDueDate = null;     // for prep
    public Timestamp            selfNotifyDate = null;
    public Timestamp            selfDueDate = null;
    public long[]               assessorEntIdList = null;   // for ins    
    public String[]             assessorTypeList = null;    // for ins
    public String[]             auaTypeList = null;         // for ins, update
    public Timestamp[]          auaNotifyDateList = null;   // for ins, update
    public Timestamp[]          auaDueDateList = null;      // for ins, update
    public long[][]             assessorTypeEntIdList = null; // for update
    public long[]               weightList = null;
    public boolean              multi_ins = false;

    public Timestamp last_upd_timestamp;
    
    public CompetencyReqParam(ServletRequest inReq, String clientEnc_, String encoding_)
        throws cwException {
            this.req = inReq;
            this.clientEnc = clientEnc_;
            this.encoding = encoding_;
            
            //Print submited param
            Enumeration enumeration = req.getParameterNames();
            while( enumeration.hasMoreElements() ) {
                String name = (String) enumeration.nextElement();
                String[] values = req.getParameterValues(name);
//                if( values != null )
//                    for(int i=0; i<values.length; i++)
//                        System.out.println(name + " (" + i + "):" + values[i]);
            }
    		//$$ competencyReqParam1.move(0,0);
}
    
    /*
    public void getNode() throws cwException {
                
        node = new DbCmSkillTreeNode();
        node.stn_skb_id = getLongParameter("node_id");
        node.skb_id = node.stn_skb_id;
        node.stn_title = unicode(getStringParameter("title"));
        node.skb_update_timestamp = getTimestampParameter("timestamp");
        node.stn_stn_skb_id_parent = getLongParameter("parent_id");
    }
    */
    
    public void getSkill() throws cwException {
        skill = new DbCmSkill();
        
        skill.skl_skb_id = getLongParameter("skill_id");
        skill.skb_id = skill.skl_skb_id;

        skill.skb_title = unicode(getStringParameter("title"));
        skill.skb_description = unicode(getStringParameter("description"));
        skill.skb_ssl_id = getLongParameter("scale_id");
        skill.skb_update_timestamp = getTimestampParameter("timestamp");
        skill.skb_parent_skb_id = getLongParameter("parent_id");
        skill.assesseeQ = unicode(getStringParameter("assesseeQ"));
        skill.assessorQ = unicode(getStringParameter("assessorQ"));
        skill.skl_xml = skill.formatSkillXML();
        /* get the skl_type. 
        if skl_type is not given, take it as SKILL for compatibility purpose
        */
        skill.skl_type = getStringParameter("type");
        if(skill.skl_type == null || skill.skl_type.length() == 0) {
            skill.skl_type = DbCmSkillBase.COMPETENCY_SKILL;
        }
        skill.skb_type = skill.skl_type;
        showAll  = getBooleanParameter("all");
        
    }

    public void getSkillSet() throws cwException {
        skillSet = new DbCmSkillSet();
        skillSet.sks_skb_id = getLongParameter("sks_id");
        if( skillSet.sks_skb_id == Long.MIN_VALUE )
            skillSet.sks_skb_id = 0;

        refresh = getBooleanParameter("refresh");

        showSkillPath = getBooleanParameter("show_skill_path");
        /*
        skillSet.skb_id = skillSet.sks_skb_id;
        skillSet.sks_type = getStringParameter("type");
        skillSet.sks_title = unicode(getStringParameter("title"));
        skillSet.skb_update_timestamp = getTimestampParameter("timestamp");
        */
    }
    
    public void getSkillSetList()
        throws cwException {
            skillSet = new DbCmSkillSet();
            skillSet.sks_type = getStringParameter("sks_type");
            return;            
        }
    
    public void getScale() throws cwException {
        skillScale = new DbCmSkillScale();
        skillLevelVec = new Vector();
        
        skillScale.ssl_id = getLongParameter("scale_id");
        skillScale.ssl_title = unicode(getStringParameter("scale_title"));
        skillScale.isNew = getBooleanParameter("new");
        skillScale.ssl_share_ind = getBooleanParameter("standard");
        skillScale.ssl_update_timestamp = getTimestampParameter("timestamp");
        int level_cnt = getIntParameter("level_total");
        if (level_cnt > 0) {
            for (int i=1;i<=level_cnt;i++) {
//System.out.println("Loop " + i );                
                DbCmSkillLevel level = new DbCmSkillLevel();
                level.sle_level = getIntParameter("level_value_" + i);
                level.sle_label = unicode(getStringParameter("level_label_" + i));
                level.sle_description = unicode(getStringParameter("level_desc_" + i));
                skillLevelVec.addElement(level);
            }
        }
        
    }
    
    public void refreshAssList()
        throws cwException {
            
            status_prepared = getBooleanParameter("prepared");
            status_collected = getBooleanParameter("collected");
            status_notified = getBooleanParameter("notified");
            status_resolved = getBooleanParameter("resolved");
            return;
            
        }
    
    public void getOrder() throws cwException {
        
        showAll  = getBooleanParameter("all");
        pagetime = getTimestampParameter("timestamp");
        
        cur_page = getIntParameter("cur_page");
        if (cur_page == Integer.MIN_VALUE)  {
            cur_page = 1;
        }
        
        pagesize = getIntParameter("pagesize");
        if (pagesize == Integer.MIN_VALUE) {
            pagesize = 10;
        }
        
        order_by = getStringParameter("order_by");
        sort_by = getStringParameter("sort_by");
        if (sort_by == null) {
            sort_by = " ASC ";
        }
    }

    public void searchUserBySkill()
        throws cwException {
            pickSkill();
            reviewDate = getTimestampParameter("review_date");
            skillIdList = getLongArrayParameter("skill_id_lst", DELIMITER);
            levelList = getFloatArrayParameter("level_lst", DELIMITER);
            typeList = getStrArrayParameter("type_lst", DELIMITER);
            return;
            
        }

    public void searchUserBySkillSet()
        throws cwException {
            pickSkill();
            reviewDate = getTimestampParameter("review_date");
            skillSetId = getLongParameter("skill_set_id");
            return;
            
        }
    
    public void pickSkill()
        throws cwException {
            
            picked_skl_id_lst = getStringParameter("picked_skl_id_lst");
            
            level_lst = getStringParameter("level_lst");
            
            priority_lst = getStringParameter("priority_lst");
            
            skl_id_lst = getStringParameter("skl_id_lst");
            
            sks_title = unicode(getStringParameter("sks_title"));
            
            if( sks_title == null )
                sks_title = "";
            
            sks_comment_ind = getBooleanParameter("sks_comment_ind");
            
            return;
            
        }
        
    public void updSkillSet()
        throws cwException {
            
            //skillSet = new DbCmSkillSet();
            
            level_lst = getStringParameter("level_lst");
            
            priority_lst = getStringParameter("priority_lst");
            
            skl_id_lst = getStringParameter("skl_id_lst");
            
            sks_title = unicode(getStringParameter("sks_title"));
            if( sks_title == null )
                sks_title = "";
            
            skillSetMgr = new CmSkillSetManager();
            skillSetMgr.sks_skb_id = getLongParameter("sks_id");
            
            last_upd_timestamp = getTimestampParameter("last_upd_timestamp");
            return;
            
        }
    
    public void delSkillSet()
        throws cwException {
            
            sks_id_lst = getStringParameter("sks_id_lst");
            
            return;
        }
    
    public void delSurvey()
        throws cwException {
            
            skillSetMgr = new CmSkillSetManager();
            skillSetMgr.sks_skb_id = getLongParameter("sks_id");
            if( skillSetMgr.sks_skb_id == Long.MIN_VALUE )
                skillSetMgr.sks_skb_id = 0;
            
            return;
            
        }
    
    public void pickSkillSet()
        throws cwException {
            
            skillSetMgr = new CmSkillSetManager();
            
            
            
            level_lst = getStringParameter("level_lst");
            
            skillSetMgr.sks_skb_id = getLongParameter("sks_id");
            if( skillSetMgr.sks_skb_id == Long.MIN_VALUE )
                skillSetMgr.sks_skb_id = 0;

            picked_skl_id_lst = getStringParameter("picked_skl_id_lst");
                        
            sks_title = unicode(getStringParameter("sks_title"));
            if( sks_title == null )
                sks_title = "";

            sks_comment_ind = getBooleanParameter("sks_comment_ind");
            
            skl_id_lst = getStringParameter("skl_id_lst");
            
            return;
        }
    
    public void saveSkillNQ() 
        throws cwException {

        pickSkillSet();
        
        skillIdList = getLongArrayParameter("skl_id_lst", DELIMITER);
            
        assessorQList = split(unicode(getStringParameter("assessorQ_lst")), DELIMITER);
            
        assesseeQList = split(unicode(getStringParameter("assesseeQ_lst")), DELIMITER);
            
        commentIndList = getBooleanArrayParameter("comment_ind_lst", DELIMITER);
    }
    
    public void saveQ()
        throws cwException {
            
            skill = new DbCmSkill();
            skill.skl_skb_id = getLongParameter("skl_id");
            skill.skb_id = skill.skl_skb_id;
            
            assessorQ = unicode(getStringParameter("assessorQ"));
            
            assesseeQ = unicode(getStringParameter("assesseeQ"));
            
            sks_comment_ind = getBooleanParameter("comment_ind");
        
            return;        
        }
        
    public void getQ()
        throws cwException {

            skl_id = getLongParameter("skl_id");            

            sks_id = getLongParameter("sks_id");
            if( sks_id == Long.MIN_VALUE )
                sks_id = 0;

            return;
            
        }
        
    public void updSurvey()
        throws cwException {
        
            skillCoverage = new DbCmSkillSetCoverage();
            skillCoverage.ssc_sks_skb_id = getLongParameter("sks_id");
            
            sks_title = unicode(getStringParameter("sks_title"));
            
            sks_comment_ind = getBooleanParameter("sks_comment_ind");
            
            skl_id_lst = getStringParameter("skl_id_lst");
            
            last_upd_timestamp = getTimestampParameter("last_upd_timestamp");
            return;        
        }
        
        
    public void getEntList()
        throws cwException {
            
            cur_page = getIntParameter("cur_page");
            if (cur_page == Integer.MIN_VALUE)  {
                cur_page = 1;
            }
        
            pagesize = getIntParameter("pagesize");
            if (pagesize == Integer.MIN_VALUE) {
                pagesize = 10;
            }

            pagetime = getTimestampParameter("timestamp");
            
            dbUsg = new dbUserGroup();
            dbUsg.usg_ent_id = getLongParameter("usg_ent_id");
            if( dbUsg.usg_ent_id == Long.MIN_VALUE )
                dbUsg.usg_ent_id = 0;
                
            asm_id = getLongParameter("asm_id");
                
            return;
            
        }

    public void editAssessment()
        throws cwException {
        
            assMgr = new CmAssessmentManager();
            assMgr.asm_id = getLongParameter("asm_id");
            if( assMgr.asm_id == Long.MIN_VALUE )
                assMgr.asm_id = 0;
            
            refresh = getBooleanParameter("refresh");            
        
            return;        
        }
        
    public void pickUser()
        throws cwException {
            
            usr_ent_id = getLongParameter("usr_ent_id");
            if( usr_ent_id == Long.MIN_VALUE )
                usr_ent_id = 0;
             
            sks_id = getLongParameter("sks_id");
            if( sks_id == Long.MIN_VALUE )
                sks_id = 0;
            
            sks_title = unicode(getStringParameter("sks_title"));
            if( sks_title == null )
                sks_title = "";
            
            review_start = getTimestampParameter("review_start");
            review_end = getTimestampParameter("review_end");
            notify_date = getTimestampParameter("notify_date");
            due_date = getTimestampParameter("due_date");
            
            asm_id = getLongParameter("asm_id");
            
            usr_ent_id_lst = getStringParameter("usr_ent_id_lst");
            usr_type_lst = getStringParameter("usr_type_lst");
            usr_weight_lst = getStringParameter("usr_weight_lst");
            
            last_upd_timestamp = getTimestampParameter("last_upd_timestamp");
            return;
        }
        
    
    public void updAssessment()
        throws cwException {
            
            dbAss = new DbCmAssessment();
            dbAss.asm_id = getLongParameter("asm_id");
            dbAss.asm_ent_id = getLongParameter("usr_ent_id");
            dbAss.asm_sks_skb_id = getLongParameter("sks_id");
            dbAss.asm_title = unicode(getStringParameter("sks_title"));
            dbAss.asm_review_start_datetime = getTimestampParameter("review_start");
            dbAss.asm_review_end_datetime = getTimestampParameter("review_end");
            dbAss.asm_eff_start_datetime = getTimestampParameter("notify_date");
            dbAss.asm_eff_end_datetime = getTimestampParameter("due_date");
            dbAss.asm_update_timestamp = getTimestampParameter("last_upd_timestamp");
            dbAss.asm_auto_resolved_ind = getBooleanParameter("auto_resolved_ind");
            self_ass = getBooleanParameter("self_assess");
            try {
                mgmt_assessor_ent_id_list = 
                    getLongArrayParameter("mgmt_assessor_ent_id_lst", DELIMITER);
            } catch (cwException e) {
                mgmt_assessor_ent_id_list = null;
            }
            
            sender_usr_id = getStringParameter("sender_usr_id");
            notify_msg_subject = getStringParameter("notify_msg_subject");
            collect_msg_subject = getStringParameter("collect_msg_subject");
            return;            
            
        }

    public void submitTstAss() 
        throws cwException {
        mod_res_id = getLongParameter("mod_res_id");
    }
    
    public void submitSurvey()
        throws cwException {
            asm_id = getLongParameter("asm_id");

            submit = getBooleanParameter("ass_submit");
            
            last_upd_timestamp = getTimestampParameter("last_upd_timestamp");
            
            int totalSection = getIntParameter("total_section");
            int totalQuestion = getIntParameter("total_question");
            
            comment = unicode(getStringParameter("comment"));
            skillIdList = getLongArrayParameter("skill_id_lst", DELIMITER);
            answerList = getFloatArrayParameter("answer_lst", DELIMITER);
            
            return;
        }


    public void getAssessorResult()
        throws cwException {
            
            asm_id = getLongParameter("asm_id");
            
            usr_ent_id = getLongParameter("usr_ent_id");
            
            return;
            
        }

    public void getSkillGap()
        throws cwException {
            
            source_id = getLongParameter("source_id");            
            target_id = getLongParameter("target_id");
            
            source_date = getTimestampParameter("source_date");
            target_date = getTimestampParameter("target_date");

            target_asm_id = getLongParameter("target_asm_id");
            source_asm_id = getLongParameter("source_asm_id");

            source_show_all = getBooleanParameter("source_show_all");
            target_show_all = getBooleanParameter("target_show_all");
            
            source_type = getStringParameter("source_type");
            target_type = getStringParameter("target_type");
            
            show_course_recomm = getBooleanParameter("show_course_recomm");
            
            return;
            
        }

    public void viewSkillGap()
        throws cwException {
            
            source_id = getLongParameter("source_id");            
            target_id = getLongParameter("target_id");
            
            source_date = getTimestampParameter("source_date");
            target_date = getTimestampParameter("target_date");

            target_asm_id = getLongParameter("target_asm_id");
            source_asm_id = getLongParameter("source_asm_id");

            source_show_all = getBooleanParameter("source_show_all");
            target_show_all = getBooleanParameter("target_show_all");
            
            source_type = getStringParameter("source_type");
            target_type = getStringParameter("target_type");
            
            show_course_recomm = getBooleanParameter("show_course_recomm");

            skl_id = getLongParameter("skill_id");
            return;
            
        }

    public void viewUsrSkill()
        throws cwException {
            
            source_id = getLongParameter("usr_ent_id");
            
            source_asm_id = getLongParameter("asm_id");
            
            source_date = getTimestampParameter("resolved_date");
            
            return;
        }

    public void viewEntSkill()
        throws cwException {
            
            source_id = getLongParameter("ent_id");
            
            source_asm_id = getLongParameter("asm_id");
            
            source_date = getTimestampParameter("resolved_date");
            
            skl_id = getLongParameter("skill_id");
            return;
        }

    public void saveResolved()
        throws cwException {
            
            asm_id = getLongParameter("asm_id");
            score_lst = getStringParameter("score_lst");
			skl_id_lst = getStringParameter("skb_id_lst");

			float[] score = cwUtils.splitToFloat(score_lst, DELIMITER);
			long[] skb_id = cwUtils.splitToLong(skl_id_lst, DELIMITER);
            
			h_id_value = new Hashtable();
			for(int i=0; i<skb_id.length; i++){
				h_id_value.put(new Long(skb_id[i]), new Float(score[i]));
			}
            
            last_upd_timestamp = getTimestampParameter("last_upd_timestamp");
            
            return;
            
        }

    public void setUserSearch()
        throws cwException {
            
            refresh = getBooleanParameter("refresh");
            
            return;
        }
        
    public void getUserSkill()
        throws cwException {
            
            usr_ent_id = getLongParameter("usr_ent_id");
            
            source_show_all = getBooleanParameter("show_all");
            
            return;
            
        }

    public void skillSearch()
        throws cwException{
            
            pagetime = getTimestampParameter("timestamp");
        
            sle_level = getIntParameter("sle_level");
            
            cur_page = getIntParameter("cur_page");
            if (cur_page == Integer.MIN_VALUE)  {
                cur_page = 1;
            }
        
            pagesize = getIntParameter("pagesize");
            if (pagesize == Integer.MIN_VALUE) {
                pagesize = 10;
            }
        
            order_by = getStringParameter("order_by");
            sort_by = getStringParameter("sort_by");
            if (sort_by == null) {
                sort_by = " ASC ";
            }
            
            skb_title = unicode(getStringParameter("skl_title"));
            skb_description = unicode(getStringParameter("skl_description"));
            sle_label = unicode(getStringParameter("sle_label"));
            sle_description = unicode(getStringParameter("sle_description"));
            
        }


    public void scaleSearch()
        throws cwException{
            
            pagetime = getTimestampParameter("timestamp");
        
            sle_level = getIntParameter("sle_level");
            
            cur_page = getIntParameter("cur_page");
            if (cur_page == Integer.MIN_VALUE)  {
                cur_page = 1;
            }
        
            pagesize = getIntParameter("pagesize");
            if (pagesize == Integer.MIN_VALUE) {
                pagesize = 10;
            }
        
            order_by = getStringParameter("order_by");
            if( order_by == null )
                order_by = "ssl_title";
                
            sort_by = getStringParameter("sort_by");
            if (sort_by == null)
                sort_by = " ASC ";                                    
            
            ssl_title = unicode(getStringParameter("ssl_title"));
            ssl_share = getStringParameter("ssl_share");
            sle_label = unicode(getStringParameter("sle_label"));
            sle_description = unicode(getStringParameter("sle_description"));
            sle_level = getIntParameter("sle_level");
            
        }


    public void getAssignedAss()
        throws cwException {
            
            usr_ent_id = getLongParameter("usr_ent_id");
            
            return;
            
        }

    public void updAssessmentUnits()
        throws cwException {
            
            assMgr = new CmAssessmentManager();
            assMgr.asm_id = getLongParameter("asm_id");
            
            usr_ent_id_lst = getStringParameter("usr_ent_id_lst");
            
            usr_type_lst = getStringParameter("usr_type_lst");
            
            usr_weight_lst = getStringParameter("usr_weight_lst");
            
            return;
            
        }
        
    public void getItemSkill()
        throws cwException {
            
            itm_id = getLongParameter("itm_id");
            
            return;
            
        }

    public void getCompSkillContent() throws cwException {
        skill = new DbCmSkill();
        skill.skl_skb_id = getLongParameter("skill_id");
        if (skill.skl_skb_id == Long.MIN_VALUE)  {
            skill.skl_skb_id = 0;
        }
        skill.skb_id = skill.skl_skb_id;
        /*
        cur_page = getIntParameter("cur_page");
        if (cur_page == Integer.MIN_VALUE)  {
            cur_page = 1;
        }
        
        pagesize = getIntParameter("pagesize");
        if (pagesize == Integer.MIN_VALUE) {
            pagesize = 10;
        }

        pagetime = getTimestampParameter("timestamp");
        */
    }

    public void prepInsSkill() {
        try {
            scale_id_lst = getLongArrayParameter("scale_id_lst", DELIMITER);
        } catch (cwException e) {
            scale_id_lst = null;
        }
    }

    public void prepUpdSkill() throws cwException {
        skill = new DbCmSkill();
        skill.skl_skb_id = getLongParameter("skill_id");
        if (skill.skl_skb_id == Long.MIN_VALUE)  {
            skill.skl_skb_id = 0;
        }
        skill.skb_id = skill.skl_skb_id;

        try {
            scale_id_lst = getLongArrayParameter("scale_id_lst", DELIMITER);
        } catch (cwException e) {
            scale_id_lst = null;
        }
    }
    
    public void insSkill() throws cwException {
        skill = new DbCmSkill();
        
        skill.skb_title = unicode(getStringParameter("title"));
        skill.skb_description = unicode(getStringParameter("description"));
        skill.skb_ssl_id = getLongParameter("scale_id");
        skill.skb_parent_skb_id = getLongParameter("parent_id");
        skill.skl_derive_rule = getStringParameter("derive_rule");
        skill.assesseeQ = unicode(getStringParameter("assesseeQ"));
        skill.assessorQ = unicode(getStringParameter("assessorQ"));
        skill.skl_xml = skill.formatSkillXML();
        /* 
        get the skl_type. 
        if skl_type is not given, take it as SKILL for compatibility purpose
        */
        skill.skl_type = getStringParameter("type");
        if(skill.skl_type == null || skill.skl_type.length() == 0) {
            skill.skl_type = DbCmSkillBase.COMPETENCY_SKILL;
        }
        skill.skb_type = skill.skl_type;
    }

    public void updSkill() throws cwException {
        skill = new DbCmSkill();
        
        skill.skl_skb_id = getLongParameter("skill_id");
        skill.skb_id = skill.skl_skb_id;
        skill.skb_title = unicode(getStringParameter("title"));
        skill.skb_description = unicode(getStringParameter("description"));
        skill.skb_ssl_id = getLongParameter("scale_id");
        skill.skb_update_timestamp = getTimestampParameter("timestamp");
        skill.assesseeQ = unicode(getStringParameter("assesseeQ"));
        skill.assessorQ = unicode(getStringParameter("assessorQ"));
        skill.skl_xml = skill.formatSkillXML();
        skill.skl_derive_rule = getStringParameter("derive_rule");
    }

    public void assessmentNotify()
        throws cwException{
           
           asm_id = getLongParameter("asm_id");
           sender_usr_id = getStringParameter("sender_id");
           usr_ent_id = getLongParameter("ent_ids");
           return;
            
        }

    public void getRecommendItm() throws cwException {
        usr_ent_id = getLongParameter("usr_ent_id");
        skl_id = getLongParameter("skl_id");
    }

    public void batchPrepAssessment() throws cwException {
            assMgr = new CmAssessmentManager();
            Timestamp maxDate = null;
            Timestamp minDate = null;
            long[] assesseeEntIds = null;

            self_ass = getBooleanParameter("self_assess");
            if(self_ass) {
                selfNotifyDate = getTimestampParameter("self_notify_date");
                selfDueDate = getTimestampParameter("self_due_date");
            } 
            

            try {
                assessorRoleList = getStrArrayParameter("approver_role_lst", DELIMITER);
                resolverRoleList = getStrArrayParameter("resolver_role_lst", DELIMITER);
            } catch (cwException e) {;}
            
            if(assessorRoleList!=null) {
                assessorNotifyDate = getTimestampParameter("approver_role_notify_date");
                assessorDueDate = getTimestampParameter("approver_role_due_date");
            } 
            if(resolverRoleList != null) {
                resolverNotifyDate = getTimestampParameter("resolver_role_notify_date");
                resolverDueDate = getTimestampParameter("resolver_role_due_date");
            }

            // get the min and max date
            if(selfNotifyDate != null && assessorNotifyDate == null) {
                minDate = selfNotifyDate;
            } else if(selfNotifyDate == null && assessorNotifyDate != null) {
                minDate = assessorNotifyDate;
            } else if(selfNotifyDate != null && assessorNotifyDate!= null && (selfNotifyDate.before(assessorNotifyDate) || selfNotifyDate.equals(assessorNotifyDate))) {
                minDate = selfNotifyDate;
            } else if(selfNotifyDate != null && assessorNotifyDate!= null && selfNotifyDate.after(assessorNotifyDate)) {
                minDate = assessorNotifyDate;
            } else if(selfNotifyDate == null && assessorNotifyDate == null) {
                throw new cwException("asm_eff_start_datetime cannot be null");
            }

            if(selfDueDate != null && assessorDueDate == null) {
                maxDate = selfDueDate;
            } else if(selfDueDate == null && assessorDueDate != null) {
                maxDate = assessorDueDate;
            } else if(selfDueDate != null && assessorDueDate!= null && (selfDueDate.before(assessorDueDate) || selfDueDate.equals(assessorDueDate))) {
                maxDate = selfDueDate;
            } else if(selfDueDate != null && assessorDueDate!= null && selfDueDate.after(assessorDueDate)) {
                maxDate = assessorDueDate;
            } else if(selfDueDate == null && assessorDueDate == null) {
                throw new cwException("asm_eff_end_datetime cannot be null");
            }

            try {
                assesseeEntIds = getLongArrayParameter("assessee_ent_id_lst", DELIMITER);
            } catch (cwException e) {
                assesseeEntIds = null;
            }

            if(assesseeEntIds != null) {
                batchAsm = new DbCmAssessment[assesseeEntIds.length];
                for(int i=0; i<assesseeEntIds.length; i++) {
                    batchAsm[i] = new DbCmAssessment();
                    batchAsm[i].asm_ent_id = assesseeEntIds[i];
                    batchAsm[i].asm_sks_skb_id = getLongParameter("sks_id");
                    batchAsm[i].asm_title = unicode(getStringParameter("sks_title"));
                    batchAsm[i].asm_review_start_datetime = getTimestampParameter("review_start");
                    batchAsm[i].asm_review_end_datetime = getTimestampParameter("review_end");
                    batchAsm[i].asm_update_timestamp = getTimestampParameter("last_upd_timestamp");
                    batchAsm[i].asm_auto_resolved_ind = getBooleanParameter("auto_resolved_ind");
                    batchAsm[i].asm_eff_start_datetime = minDate;
                    batchAsm[i].asm_eff_end_datetime = maxDate;
                }
            }

            sender_usr_id = getStringParameter("sender_usr_id");
            notify_msg_subject = getStringParameter("notify_msg_subject");
            collect_msg_subject = getStringParameter("collect_msg_subject");

            return;            
    }

    public void batchInsAssessment() throws cwException {
            assMgr = new CmAssessmentManager();
            long[] assesseeEntIds = null;

            self_ass = getBooleanParameter("self_assess");
            selfNotifyDate = getTimestampParameter("self_notify_date");
            selfDueDate = getTimestampParameter("self_due_date");

            assesseeEntIds = new long[1];
            assesseeEntIds[0] = getLongParameter("assessee_ent_id");

            try {
                assessorEntIdList = getLongArrayParameter("assessor_ent_id_lst", DELIMITER);
                assessorTypeList = getStrArrayParameter("assessor_type_lst", DELIMITER);
            } catch (cwException e) {
                assessorEntIdList = null;
                assessorTypeList = null;
            }

            try {
                auaTypeList = getStrArrayParameter("aua_type_lst", DELIMITER);
                auaNotifyDateList = getTimestampArrayParameter("aua_notify_date_lst", DELIMITER);
                auaDueDateList = getTimestampArrayParameter("aua_due_date_lst", DELIMITER);
            } catch (cwException e) {
                auaTypeList = null;
                auaNotifyDateList = null;
                auaDueDateList = null;
            }

            if(assesseeEntIds != null) {
                batchAsm = new DbCmAssessment[assesseeEntIds.length];
                for(int i=0; i<assesseeEntIds.length; i++) {
                    batchAsm[i] = new DbCmAssessment();
                    batchAsm[i].asm_ent_id = assesseeEntIds[i];
                    batchAsm[i].asm_sks_skb_id = getLongParameter("sks_id");
                    batchAsm[i].asm_title = unicode(getStringParameter("sks_title"));
                    batchAsm[i].asm_review_start_datetime = getTimestampParameter("review_start");
                    batchAsm[i].asm_review_end_datetime = getTimestampParameter("review_end");
                    batchAsm[i].asm_update_timestamp = getTimestampParameter("last_upd_timestamp");
                    batchAsm[i].asm_auto_resolved_ind = getBooleanParameter("auto_resolved_ind");
                    batchAsm[i].asm_eff_start_datetime = getTimestampParameter("asm_notify_date");
                    batchAsm[i].asm_eff_end_datetime = getTimestampParameter("asm_due_date");
                }
            }

            sender_usr_id = getStringParameter("sender_usr_id");
            notify_msg_subject = getStringParameter("notify_msg_subject");
            collect_msg_subject = getStringParameter("collect_msg_subject");
            multi_ins = getBooleanParameter("multi_ins");

            return;            
    }
    
    public void batchUpdateAssessment() throws cwException {
            assMgr = new CmAssessmentManager();
            Timestamp maxDate = null;
            Timestamp minDate = null;
            long[] assesseeEntIds = null;

            assesseeEntIds = new long[1];
            assesseeEntIds[0] = getLongParameter("assessee_ent_id");

            try {
                String[] assessorTypeEntLst = getStrArrayParameter("assessor_ent_id_lst", DELIMITER);
                
                assessorTypeEntIdList = new long[assessorTypeEntLst.length][];
                for(int i=0; i<assessorTypeEntLst.length; i++) {
                    assessorTypeEntIdList[i] = cwUtils.splitToLong(assessorTypeEntLst[i], COMMA);
                }
                assessorTypeList = getStrArrayParameter("assessor_type_lst", DELIMITER);
                weightList = getLongArrayParameter("weight_lst", DELIMITER);
            } catch (cwException e) {
                assessorTypeEntIdList = null;
                assessorTypeList = null;
                weightList = null;
            }

            try {
                auaTypeList = getStrArrayParameter("aua_type_lst", DELIMITER);
                auaNotifyDateList = getTimestampArrayParameter("aua_notify_date_lst", DELIMITER);
                auaDueDateList = getTimestampArrayParameter("aua_due_date_lst", DELIMITER);
            } catch (cwException e) {
                auaTypeList = null;
                auaNotifyDateList = null;
                auaDueDateList = null;
            }

            if(auaNotifyDateList != null) {
                for(int i=0; i<auaNotifyDateList.length; i++) {
                    if(auaNotifyDateList[i] != null) {
                        if(!auaTypeList[i].equalsIgnoreCase(RESOLVED)) {
                            if(minDate == null) 
                                minDate = auaNotifyDateList[i];
                            if(auaNotifyDateList[i].before(minDate)) 
                                minDate = auaNotifyDateList[i];
                        }
                    }
                }
            }
            if(auaDueDateList != null) {
                for(int i=0; i<auaDueDateList.length; i++) {
                    if(auaDueDateList[i] != null) {
                        if(!auaTypeList[i].equalsIgnoreCase(RESOLVED)) {
                            if(maxDate == null) 
                                maxDate = auaDueDateList[i];
                            if(auaNotifyDateList[i].after(maxDate)) 
                                maxDate = auaDueDateList[i];
                        }
                    }
                }
            }
            if(minDate == null || maxDate == null) {
                throw new cwException("asm_eff_start_datetime or asm_eff_end_datetime cannot be null");
            }
            
            if(assesseeEntIds != null) {
                batchAsm = new DbCmAssessment[assesseeEntIds.length];
                for(int i=0; i<assesseeEntIds.length; i++) {
                    batchAsm[i] = new DbCmAssessment();
                    batchAsm[i].asm_id = getLongParameter("asm_id");
                    batchAsm[i].asm_ent_id = assesseeEntIds[i];
                    batchAsm[i].asm_sks_skb_id = getLongParameter("sks_id");
                    batchAsm[i].asm_title = unicode(getStringParameter("sks_title"));
                    batchAsm[i].asm_review_start_datetime = getTimestampParameter("review_start");
                    batchAsm[i].asm_review_end_datetime = getTimestampParameter("review_end");
                    batchAsm[i].asm_auto_resolved_ind = getBooleanParameter("auto_resolved_ind");
                    batchAsm[i].asm_eff_start_datetime = minDate;
                    batchAsm[i].asm_eff_end_datetime = maxDate;
                }
            }
            last_upd_timestamp = getTimestampParameter("last_upd_timestamp");

            sender_usr_id = getStringParameter("sender_usr_id");
            notify_msg_subject = getStringParameter("notify_msg_subject");
            collect_msg_subject = getStringParameter("collect_msg_subject");

            return;            
    }

    private Timestamp[] getTimestampArrayParameter(String paramName, String delimiter)
        throws cwException {
        String[] str = getStrArrayParameter(paramName, delimiter);
        if(str==null) {
            return null;
        }
        Timestamp[] t = null;
        int size;
        if(str.length < 6) {
            size = 6;
            t = new Timestamp[size];
        } else {
            size = str.length;
            t = new Timestamp[str.length];
        }
        for(int i=0; i<str.length; i++) {
            if(str[i] == null || str[i].equals("")) {
                t[i] = null;
            } else {
                t[i] = Timestamp.valueOf(str[i]);
            }
        }
        return t;
    }


	public void insCompGroup()
		throws cwException {

			skillTreeNode = new DbCmSkillTreeNode();
			skillTreeNode.skb_title = unicode(getStringParameter("skb_title"));
			skillTreeNode.skb_description = unicode(getStringParameter("skb_description")); 
			skillTreeNode.skb_ssl_id = getLongParameter("skb_ssl_id");
			return;
		}
	
	public void updCompGroup()
		throws cwException {
			
			skillTreeNode = new DbCmSkillTreeNode();
			skillTreeNode.stn_skb_id = getLongParameter("stn_skb_id");
			skillTreeNode.skb_id = skillTreeNode.stn_skb_id;
			skillTreeNode.skb_update_timestamp = getTimestampParameter("skb_update_timestamp");
			skillTreeNode.skb_title = unicode(getStringParameter("skb_title"));
			skillTreeNode.skb_description = unicode(getStringParameter("skb_description")); 
			skillTreeNode.skb_ssl_id = getLongParameter("skb_ssl_id");
			
			return;
		}
	
	public void getCompGroup()
		throws cwException {

			skillTreeNode = new DbCmSkillTreeNode();
			skillTreeNode.stn_skb_id = getLongParameter("stn_skb_id");
			skillTreeNode.skb_id = skillTreeNode.stn_skb_id;
			return;

		}
	
	public void delCompGroup()
		throws cwException {

			skillTreeNode = new DbCmSkillTreeNode();
			skillTreeNode.stn_skb_id = getLongParameter("stn_skb_id");
			skillTreeNode.skb_id = skillTreeNode.stn_skb_id;
			skillTreeNode.skb_update_timestamp = getTimestampParameter("skb_update_timestamp");
			return;

		}	
	
	public void getCompSkill()
		throws cwException {
			
			skill = new DbCmSkill();
			skill.skb_id = getLongParameter("skl_skb_id");
			skill.skl_skb_id = skill.skb_id;
			return;
			
		}
		
	public void delCompSkill()
		throws cwException {
			
			skill = new DbCmSkill();
			skill.skb_id = getLongParameter("skl_skb_id");
			skill.skl_skb_id = skill.skb_id;
			skill.skb_update_timestamp = getTimestampParameter("skb_update_timestamp");
			skillIdList = getLongArrayParameter("skb_id_lst", DELIMITER); 
			return;
			
		}		
		
		
	public void insCompSkill()
		throws cwException {
			
			skill = new DbCmSkill();
			skill.skb_title = unicode(getStringParameter("skb_title"));
			skill.skb_description = unicode(getStringParameter("skb_description"));
			skill.skb_parent_skb_id = getLongParameter("skb_parent_skb_id");
			skill.skl_rating_ind = getBooleanParameter("skl_rating_ind");
			skill.skl_derive_rule = getStringParameter("skl_derive_rule");
			behav_desc = split( unicode(getStringParameter("behav_description")), BEHAV_DELIMITER);
			return;			
		}
		
	public void updCompSkill()
		throws cwException {
			
			skill = new DbCmSkill();
			skill.skb_id = getLongParameter("skb_id");
			skill.skb_title = unicode(getStringParameter("skb_title"));
			skill.skb_description = unicode(getStringParameter("skb_description"));
			skill.skl_rating_ind = getBooleanParameter("skl_rating_ind");
			skill.skl_derive_rule = getStringParameter("skl_derive_rule");
			skill.skb_update_timestamp = getTimestampParameter("skb_update_timestamp");
			behav_desc = split( unicode(getStringParameter("behav_description")), BEHAV_DELIMITER);
			if( behav_desc == null ) {
				behav_desc = new String[0];
			}
			behav_order = getLongArrayParameter("behav_order", BEHAV_DELIMITER);
			if( behav_order == null ) {
				behav_order = new long[0];
			}
			behav_skb_id = getLongArrayParameter("behav_skb_id", BEHAV_DELIMITER);
			if( behav_skb_id == null ) {
				behav_skb_id = new long[0];
			}
			return;

		}
	
}
