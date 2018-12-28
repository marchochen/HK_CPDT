package com.cw.wizbank.competency;

import java.sql.*;
import java.util.*;
import javax.servlet.http.HttpSession;

import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.util.*;
import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.ae.db.DbItemTargetRuleDetail;
import com.cw.wizbank.db.*;
import com.cw.wizbank.db.view.*;
import com.cw.wizbank.util.cwSysMessage;

public class CmSkillSetManager{
    
    private static final String COMMA               =   ",";
    public static final String SKILL_PROFILE       =   "SKP";
    private static final String SKILL_TEMPLATE      =   "SKT";
    private static final String ASSESSMENT_RESULT   =   "ASR";
    private static final String SKILL_SECTION       =   "SCT";
    private static final String SKILL_SET           =   "SKILL_SET";
    private static final String SKILL               =   "SKILL";
    
    
    private static final String HASH_TIMESTAMP      = "page_timestamp";
    private static final String HASH_SKS_ID_VEC     = "sks_id_vec";
    private static final String HASH_ORDERBY        = "order_by";
    private static final String HASH_SORTBY         = "sort_by";
    
    public static final String HASH_SKILL_SET_TITLE         = "skill_set_title";
    public static final String HASH_SKILL_LEVEL_LIST        = "skill_level_list";
    public static final String HASH_SKILL_ID_LIST           = "skill_id_list";
    public static final String HASH_SKILL_PRIORITY_LIST     = "skill_priority_list";
    public static final String HASH_SKILL_COMMENT_IND       = "skill_set_comment_ind";
    
    public long sks_skb_id;
    /*
    public CmSkillSetManager(long sks_skb_id) {
        this.sks_skb_id = sks_skb_id;
        return;
    }
    */
    
    
    
    
    /**
    Insert Skill Set Coverage into a Skill Set
    @param Array of DbCmSkillSetCoverage
    @return row count of the INSERT
    */    
    public int insCoverage(Connection con, DbCmSkillSetCoverage[] DbSsc)
        throws SQLException {
            
            int count = 0;
            if( DbSsc == null || DbSsc.length == 0 )
                return 0;
            
            for(int i=0; i<DbSsc.length; i++) {
                if( DbSsc[i] == null || DbSsc[i].ssc_sks_skb_id == 0 )
                    DbSsc[i].ssc_sks_skb_id = sks_skb_id;
                if( DbSsc[i].ins(con) == 1 )
                    count++;
            }
            return count;            
        }
    
    
    
    

    
    
    /**
    Update Skill Set Coverage in a Skill Set
    @param Array of DbCmSkillSetCoverage
    @param insert ( true : insert Skill Set Coverage if not exist )
    @param delete ( true : delete Skill Set Coverage if not in Array of DbCmSkillSetCoverage )
    @return row count of the INSERT and UPDATE
    */
    
    public int updCoverage(Connection con, DbCmSkillSetCoverage[] DbSsc, boolean insert, boolean delete)
        throws SQLException {

            if(delete) {
                long[] ids = new long[DbSsc.length];
                for(int i=0; i<DbSsc.length; i++)
                    ids[i] = DbSsc[i].ssc_skb_id;                
                DbCmSkillSetCoverage _DbSsc = new DbCmSkillSetCoverage();
                _DbSsc.ssc_sks_skb_id = sks_skb_id;
                _DbSsc.delNotInList(con, ids);
            }
            
            int count = 0;
            if( DbSsc == null || DbSsc.length == 0 )
                return 0;
            
            boolean bUpd = false;
            for(int i=0; i<DbSsc.length; i++) {
                bUpd = false;
                if( DbSsc[i].ssc_sks_skb_id == 0 )
                    DbSsc[i].ssc_sks_skb_id = sks_skb_id;
                    
                if( DbSsc[i].upd(con) == 1 )                        
                        count++;
                else {                    
                    if( insert ) {                        
                        if( DbSsc[i].ins(con) == 1 )
                            count++;                        
                    }
                }
            }

            return count;            
        }
    
    
    
    /*
    public int modifySurveyCoverage(Connection con, HttpSession sess, DbCmSkillSetCoverage[] DbSsc)
        throws SQLException, cwException{
            Hashtable data = null;
            if( sess != null )
                data = (Hashtable)sess.getAttribute("Competency_SKILL_SET");    
            
            String[] str = new String[3];
            if( data != null ) {
                for(int i=0; i<DbSsc.length; i++) {
                    str = (String[])data.get(new Long(DbSsc[i].ssc_skb_id));
                    if( str != null ) {
                        DbSsc[i].ssc_xml = "<question_type>" + cwUtils.NEWL 
                                         + "<assessee>" + str[0] + "</assessee>" + cwUtils.NEWL
                                         + "<assessor>" + str[1] + "</assessor>" + cwUtils.NEWL
                                         + "</question_type>" + cwUtils.NEWL;
                        if( str[2].equalsIgnoreCase("Y") )
                            DbSsc[i].ssc_comment_ind = true;
                        else
                            DbSsc[i].ssc_comment_ind = false;                        
                    }
                }
            }
            
            int count = insCoverage(con, DbSsc);                        
            return count;
        }
    */
    
    
    /**
    Pcik a Skill into a Skill Set (Only make the relation between cmSkillSetCoverage and cmSkillSet)
    @param skb_id the array of the skill id
    @param delFlag (true - insert items in list and delete items not in list. false - insert items in list)
    @param orderFlag (true - give the priority value to the coverage. false - ssc_priority is null)
    @return row count of INSERT
    */
    public int pickCoverage(Connection con, long[] skb_id, boolean delFlag, boolean orderFlag)
        throws SQLException, cwException {
        
            if( skb_id == null )
                return 0;

            DbCmSkillSetCoverage DbSsc = new DbCmSkillSetCoverage();
            DbSsc.ssc_sks_skb_id = sks_skb_id;
            
            if(delFlag) {
                DbSsc.delNotInList(con, skb_id);
            }
            
            long order = 0;
            if(orderFlag) {
                order = DbSsc.getMaxPriority(con);
                order++;             //new coverage insert start at this order
            }
            
            int count = 0;
            for(int i=0; i<skb_id.length; i++) {
                DbSsc.ssc_skb_id = skb_id[i];
                if(!DbSsc.exist(con)) {
                    if(orderFlag)
                        DbSsc.ssc_priority = order + count;
                    if(DbSsc.ins(con) == 1)
                        count++;
                }
            }
            return count;
        }
    
    
    
    
    
    
    
    
    
    
    /**
    Coverage reorder in the Skill Set
    @param ssc_skb_id array of the skill base id 
    @param order array of order corresponding to the ssc_skb_id array    
    */
    /*
    public void reorder(Connection con, long[] ssc_skb_id, int[] order)
        throws SQLException {
            
            if(ssc_skb_id == null || ssc_skb_id.length == 0)
                return;
            
            DbCmSkillSetCoverage DbSsc = new DbCmSkillSetCoverage();
            DbSsc.ssc_sks_skb_id = sks_skb_id;
            for(int i=0; i< ssc_skb_id.length; i++) {
                DbSsc.ssc_skb_id = ssc_skb_id[i];
                DbSsc.ssc_priority = order[i];
                DbSsc.updatePriority(con);
            }
            return;
        }
    */
    
    
    
    

    
    
    
    
    
    
    //Put the question into session
    public static void saveQuestionToSess(Connection con, Hashtable data, long skb_id, String assesseeQ, String assessorQ, boolean comment_ind)
        throws SQLException, cwSysMessage {
            
            DbCmSkill skill = new DbCmSkill();
            if( assesseeQ == null || assessorQ == null ) {
                skill.skl_skb_id = skb_id;
                skill.skb_id = skb_id;
                skill.get(con);
            }
            
            StringBuffer xml = new StringBuffer();
            xml.append("<body>").append(cwUtils.NEWL);
            if( comment_ind )
                xml.append("<question comment=\"Y\">").append(cwUtils.NEWL);
            else
                xml.append("<question comment=\"N\">").append(cwUtils.NEWL);
            
            xml.append("<assessee>");
            if( assesseeQ != null && assesseeQ.length() > 0 )
                xml.append(cwUtils.esc4XML(assesseeQ));
            else
                xml.append(cwUtils.esc4XML(skill.skb_title));
            xml.append("</assessee>").append(cwUtils.NEWL);
            
            xml.append("<assessor>");
            if( assessorQ != null && assessorQ.length() > 0 )
                xml.append(cwUtils.esc4XML(assessorQ));
            else
                xml.append(cwUtils.esc4XML(skill.skb_title));
            xml.append("</assessor>").append(cwUtils.NEWL);
                
            xml.append("</question>").append(cwUtils.NEWL);
            xml.append("</body>").append(cwUtils.NEWL);
            
            data.put(new Long(skb_id), xml.toString());
            return;
        }
        
    //Get the question as XML from session
    public static String getQuestionFromSess(Connection con, Hashtable data, long skb_id, long sks_skb_id)
        throws SQLException, cwSysMessage {
            
            String xml = null;
            if( data != null )
                xml = (String)data.get(new Long(skb_id));
            if( xml != null ) {
                return xml;
            }else {
                DbCmSkillSetCoverage DbSsc = new DbCmSkillSetCoverage();
                DbSsc.ssc_sks_skb_id = sks_skb_id;
                DbSsc.ssc_skb_id = skb_id;
                DbSsc.get(con);
                
                if ( DbSsc.ssc_xml != null && DbSsc.ssc_xml.length() > 0 )
                    return DbSsc.ssc_xml;
                else {
                    DbCmSkill dbSkill = new DbCmSkill();
                    dbSkill.skl_skb_id = skb_id;
                    dbSkill.skb_id = skb_id;
                    dbSkill.get(con);
                    //if(dbSkill.skl_xml != null && dbSkill.skl_xml.length() > 0 )
                    return dbSkill.skl_xml;
                    /*
                    else {
                        StringBuffer xmlBody = new StringBuffer();
                        xmlBody.append("<body>").append(cwUtils.NEWL)
                               .append("<question comment=\"N\">").append(cwUtils.NEWL)            
                               .append("<assessee></assessee>").append(cwUtils.NEWL)
                               .append("<assessor></assessor>").append(cwUtils.NEWL)
                               .append("</question>").append(cwUtils.NEWL)
                               .append("</body>").append(cwUtils.NEWL);
                        return xmlBody.toString();
                    }
                    */
                }
            }
        }
    
    
    
    
    
    
    




    /**
    Delete Survey. Action rejected if the survey is taken by the assessment
    */
    public void delSurvey(Connection con)
        throws SQLException {
            
            ViewCmSkillCoverage VSc = new ViewCmSkillCoverage();
            DbCmSkillSetCoverage[] dbSsc = VSc.getAllSkillsSetCoverage(con, sks_skb_id);
            
            Vector skillSetIdVec = new Vector();
            skillSetIdVec.addElement(new Long(sks_skb_id));
            for(int i=0; i<dbSsc.length; i++) {
                if( dbSsc[i].skb_type.equalsIgnoreCase(SKILL_SET) )
                    skillSetIdVec.addElement(new Long(dbSsc[i].ssc_sks_skb_id));
                dbSsc[i].del(con);
            }
            
            long[] ids = new long[skillSetIdVec.size()];
            for(int i=0; i<ids.length; i++)
                ids[i] = ((Long)skillSetIdVec.elementAt(i)).longValue();
    
            DbCmSkillSet skillSet = new DbCmSkillSet();
            skillSet.del(con, ids);
            
            return;    
        }
    
    
    
    
    
    
    //Construct a survey XML by the data get from session
    public String editSurveyXML(Connection con, long root_id, HttpSession sess, boolean showSkillPath)
        throws SQLException, cwSysMessage {

            Hashtable data = (Hashtable) sess.getAttribute("Competency_SKILL_SET");
            String title = "";
            boolean comment_ind = false;
            Vector vec = new Vector();
            
            if( sess != null) {
                if( data != null ) {
                    vec = (Vector)data.get(HASH_SKILL_ID_LIST);
                    title = (String)data.get(HASH_SKILL_SET_TITLE);
                    comment_ind = ((Boolean)data.get(HASH_SKILL_COMMENT_IND)).booleanValue();
                } 
            } 
            
            
            ViewCmSkillCoverage Vcsc = new ViewCmSkillCoverage();
            DbCmSkillSetCoverage[] DbSsc = new DbCmSkillSetCoverage[vec.size()];
            for(int i=0; i< DbSsc.length; i++) {
                DbSsc[i] = new DbCmSkillSetCoverage();
                DbSsc[i].skb_type = SKILL;
                DbSsc[i].level = 2;
                DbSsc[i].ssc_skb_id = ((Long)vec.elementAt(i)).longValue();
                DbSsc[i].ssc_xml = (String) data.get((Long)vec.elementAt(i));
                //DbSsc[i].ssc_comment_ind = false;
            }
            
            StringBuffer xml = new StringBuffer();
            ViewCmSkillCoverage ViewSc = new ViewCmSkillCoverage();
            ResultSet rs = ViewSc.getSkillSetInfo(con, SKILL_PROFILE, null, 3, "ASC", root_id);
            xml.append("<skill_sets>").append(cwUtils.NEWL);
            while( rs.next() ) {
            xml.append("<skill_set id=\"").append(rs.getLong(2)).append("\" ")
               .append(" title=\"").append(cwUtils.esc4XML(rs.getString(3))).append("\" ")
               .append(" count=\"").append(rs.getLong(1)).append("\"/>");
            }
            xml.append("</skill_sets>").append(cwUtils.NEWL);


            if( sks_skb_id == 0 )
                xml.append("<survey id=\"\" ");
            else {   
                xml.append("<survey id=\"").append(sks_skb_id).append("\" ")
                   .append(" last_upd_timestamp=\"").append(DbCmSkillSet.getUpdTimestamp(con, sks_skb_id)).append("\" ");
            }
            
            xml.append(" type=\"SKT\" ")               
               .append(" title=\"").append(cwUtils.esc4XML(title)).append("\" ")
               .append(" sess=\"false\" >");
            if( comment_ind )
                xml.append("<comment provide=\"Y\"/>").append(cwUtils.NEWL);
            else
                xml.append("<comment provide=\"N\"/>").append(cwUtils.NEWL);
            
            
            
            xml.append(surveyXML(con, DbSsc, false, showSkillPath));
            xml.append("</survey>").append(cwUtils.NEWL);
            rs.close();
            return xml.toString();
        }

    
    
    
    
    
    
    
    
    
    
    
    /**
    Construct a XML of the survey for edition
    @return String of XML
    */
    public String editSurveyXML(Connection con, long root_id, boolean showSkillPath)
        throws SQLException, cwSysMessage {
            
                        
            StringBuffer xml = new StringBuffer();
            ViewCmSkillCoverage ViewSc = new ViewCmSkillCoverage();
            ResultSet rs = ViewSc.getSkillSetInfo(con, SKILL_PROFILE, null, 3, "ASC", root_id);
            xml.append("<skill_sets>").append(cwUtils.NEWL);
            while( rs.next() ) {
            xml.append("<skill_set id=\"").append(rs.getLong(2)).append("\" ")
               .append(" title=\"").append(cwUtils.esc4XML(rs.getString(3))).append("\" ")
               .append(" count=\"").append(rs.getLong(1)).append("\"/>");
            }
            xml.append("</skill_sets>").append(cwUtils.NEWL);

            if( sks_skb_id > 0 ) {
                ViewCmSkillCoverage Vcsc = new ViewCmSkillCoverage();
                DbCmSkillSetCoverage[] DbSsc = Vcsc.getAllSkillsSetCoverage(con, sks_skb_id);
                xml.append(surveyXML(con, DbSsc, true, showSkillPath));
            }
            rs.close();
            return xml.toString();
        }
        
        
        
        
        
        
    /**
    Construct a XML of the Survey
    @return String of XML
    */
    public String getSurveyXML(Connection con) 
        throws SQLException, cwSysMessage {
        
            ViewCmSkillCoverage Vcsc = new ViewCmSkillCoverage();
            DbCmSkillSetCoverage[] DbSsc = Vcsc.getAllSkillsSetCoverage(con, sks_skb_id);
            return surveyXML(con, DbSsc, true, false);
        
    }




    public String surveyXML(Connection con, DbCmSkillSetCoverage[] DbSsc, boolean surveyHeader, boolean showSkillPath)
        throws SQLException, cwSysMessage {


            DbCmSkillSet DbSs = new DbCmSkillSet();

            StringBuffer xml = new StringBuffer();
            if( surveyHeader ) {

                DbSs.sks_skb_id = sks_skb_id;
                DbSs.get(con);
                String type = DbSs.sks_type;


                xml.append("<survey id=\"").append(DbSs.sks_skb_id).append("\" ")
                   .append(" last_upd_timestamp=\"").append(DbSs.sks_update_timestamp).append("\" ");
                //DbSs.sks_skb_id = DbSsc[0].ssc_sks_skb_id;
                //DbSs.get(con);
                xml.append(" type=\"").append(type).append("\" ")
                   .append(" title=\"").append(cwUtils.esc4XML(DbSs.sks_title)).append("\" ")
                   .append(" sess=\"true\">");
                if(DbSs.sks_xml != null && DbSs.sks_xml.length() > 0)
                    xml.append(DbSs.sks_xml);                    
                else
                    xml.append("<comment provide=\"N\"/>");
                //xml.append(DbSsc[0].ssc_xml);
            }

            long no = 1;
            long level = 1;
            long section = 1;
            
            // get the skill path
            Hashtable skillPath = null;
            Vector stnIdVec = new Vector();
            if( showSkillPath ) {
                for(int i=0; i<DbSsc.length; i++)
                    if( !DbSsc[i].skb_type.equalsIgnoreCase(SKILL_SET) )
                        stnIdVec.addElement(new Long(DbSsc[i].ssc_skb_id));
                if( !stnIdVec.isEmpty() )
                    skillPath = ViewCmSkill.getSkillPath(con, stnIdVec);
            }
            
            for(int i=0; i<DbSsc.length; i++) {

                if( DbSsc[i].level < level ) {
                    section--;
                    xml.append("</section>").append(cwUtils.NEWL);
                }
                level = DbSsc[i].level;

                if( DbSsc[i].skb_type.equalsIgnoreCase(SKILL_SET) ) {
                    section++;

                    DbSs.sks_skb_id = DbSsc[i].ssc_skb_id;
                    DbSs.get(con);
                    xml.append("<section title=\"").append(cwUtils.esc4XML(DbSs.sks_title)).append("\">");

                    if(DbSs.sks_xml != null && DbSs.sks_xml.length() > 0)
                        xml.append(DbSs.sks_xml);                    
                    else
                        xml.append("<comment provide=\"N\"/>");                    

                } else {                    
                    xml.append("<question no=\"").append(no++).append("\" >").append(cwUtils.NEWL);                    
                    
                    //Skill detial
                    DbCmSkill DbSkill = new DbCmSkill();
                    DbSkill.skl_skb_id = DbSsc[i].ssc_skb_id;
                    DbSkill.skb_id = DbSsc[i].ssc_skb_id;
                    DbSkill.get(con);
                    xml.append("<skill id=\"").append(DbSkill.skl_skb_id).append("\">");
                    
                    if( showSkillPath && skillPath != null )
                        DbSkill.skb_title = ViewCmSkill.getSkillPathTitle(skillPath, DbSkill.skl_skb_id) + DbSkill.skb_title;
                
                    xml.append("<title>").append(cwUtils.esc4XML(DbSkill.skb_title)).append("</title>").append(cwUtils.NEWL)
                       .append("<desc>").append(cwUtils.esc4XML(DbSkill.skb_description)).append("</desc>").append(cwUtils.NEWL);

                    if( DbSsc[i].ssc_xml != null && DbSsc[i].ssc_xml.length() > 0 ) {
                        xml.append(DbSsc[i].ssc_xml );
                    }
                    xml.append("</skill>").append(cwUtils.NEWL);

                    
                    //Scale detial
                    DbCmSkillLevel skillLevel = new DbCmSkillLevel();
                    skillLevel.sle_ssl_id = DbSkill.skb_ssl_id;
                    Vector vec = skillLevel.getById(con);

                    StringBuffer temp = new StringBuffer(512);
                    long max_level = 0;
                    for(int j=0; j<vec.size(); j++) {
                        DbCmSkillLevel tempLevel = (DbCmSkillLevel)vec.elementAt(j);
                        temp.append(tempLevel.asXML());
                        if(max_level < tempLevel.sle_level) {
                            max_level = tempLevel.sle_level;
                        }
                    }

                    xml.append("<scale max_level=\"").append(max_level).append("\" >")
                    .append(cwUtils.NEWL)
                    .append(temp.toString());

                    xml.append("</scale>").append(cwUtils.NEWL);                                        
                    xml.append("</question>").append(cwUtils.NEWL);
                }
            }
            //if( section < level )
            for(int i=1; i<section; i++)
                xml.append("</section>").append(cwUtils.NEWL);
            if( surveyHeader )
                xml.append("</survey>").append(cwUtils.NEWL);
//System.out.println(xml);
            return xml.toString();
        }
        

        
        
    /**
    Get Skill Set(Skill Profile) XML from session
    @return XML of the skill set detial
    */
    public String getSkillSetAsXML(Connection con, HttpSession sess)
        throws SQLException, cwSysMessage, cwException {
            
            DbCmSkillSet Ss = new DbCmSkillSet();
            DbCmSkill skill = new DbCmSkill();
            DbCmSkillLevel skillLevel = new DbCmSkillLevel();
            ResultSet rs = null;
            Hashtable data = null;
            Vector skb_id;// = (long[])sess.getAttribute("Competency_SKILL_BASE_ID");
            float[] ssc_level;// = (long[])sess.getAttribute("Competency_COVERAGE_LEVEL");
            long[] ssc_priority;// = (long[])sess.getAttribute("Competency_COVERAGE_PRIORITY");
            String sks_title;// = (String)sess.getAttribute("Competency_SKILL_SET_TITLE");

            if( sess != null) {
                data = (Hashtable)sess.getAttribute("Competency_SKILL_SET");
                if( data == null )
                    return "";
            }
            else
                return "";

            
            //skb_id = (long[])data.get(HASH_SKILL_ID_LIST);
            skb_id = (Vector)data.get(HASH_SKILL_ID_LIST);
            ssc_level = (float[])data.get(HASH_SKILL_LEVEL_LIST);
            ssc_priority = (long[])data.get(HASH_SKILL_PRIORITY_LIST);
            sks_title = (String)data.get(HASH_SKILL_SET_TITLE);

            if( sks_skb_id > 0 ) {
                Ss.sks_skb_id = sks_skb_id;
                rs = Ss.getSkillSet(con);
                if( !rs.next() )
                    throw new cwException("Failed to get skill set id = " + sks_skb_id);
            }

            Hashtable skillPath = null;
            if( !skb_id.isEmpty() ) 
                skillPath = ViewCmSkill.getSkillPath(con, skb_id);
            

            StringBuffer xml = new StringBuffer();
            xml.append("<skill_set id=\"");

            if( sks_skb_id > 0 )
                xml.append(rs.getLong("sks_skb_id"));
            xml.append("\" last_upd_timestamp=\"");
            
            if( sks_skb_id > 0 )
                xml.append(rs.getTimestamp("sks_update_timestamp"));
            xml.append("\" type=\"");

            if( sks_skb_id > 0 )
                xml.append(rs.getString("sks_type"));
            xml.append("\" title=\"");

            if( sks_title != null )
                xml.append(cwUtils.esc4XML(sks_title));
            else    
                xml.append(rs.getString("sks_title"));
            xml.append("\">")
               .append(cwUtils.NEWL);

            //for(int i=0; i<skb_id.length; i++) {
            for(int i=0; i<skb_id.size(); i++) {
                xml.append("<set_coverage level=\"");
                if( ssc_level != null && i < ssc_level.length )
                    xml.append(ssc_level[i]);
                else
                    xml.append("1");
                xml.append("\" priority=\"");
                if( ssc_priority != null && i < ssc_priority.length )
                    xml.append(ssc_priority[i]);
                else
                    xml.append("1");
                xml.append("\" >").append(cwUtils.NEWL);
            
                skill.skl_skb_id = ((Long)skb_id.elementAt(i)).longValue();//skb_id[i];
                skill.skb_id = skill.skl_skb_id;
                skill.get(con);
                if( skillPath != null )
                    skill.skb_title = cwUtils.esc4XML(ViewCmSkill.getSkillPathTitle(skillPath, skill.skl_skb_id) + skill.skb_title);
                
                xml.append(skill.asXML());
                skillLevel.sle_ssl_id = skill.skb_ssl_id;

                Vector vec = skillLevel.getById(con);
                
                StringBuffer temp = new StringBuffer(512);
                long max_level = 0;
                for(int j=0; j<vec.size(); j++) {
                    DbCmSkillLevel level = (DbCmSkillLevel)vec.elementAt(j);
                    temp.append(level.asXML());
                    if(max_level < level.sle_level) {
                        max_level = level.sle_level;
                    }
                }
                xml.append("<scale max_level=\"").append(max_level).append("\" >")
                   .append(cwUtils.NEWL)
                   .append(temp.toString());
                
                xml.append("</scale>").append(cwUtils.NEWL);
                xml.append("</set_coverage>").append(cwUtils.NEWL);
            }
            
            xml.append("</skill_set>");
            if( sks_skb_id > 0 )
                rs.close();
            return xml.toString();

        }


    /**
    Get skill set (competenct profile and survey ) list as XML
    */
    public String getSkillSetListAsXML(Connection con, HttpSession sess, int cur_page, int pagesize, Timestamp pagetime, String order_by, String sks_type, String sort_by, long root_id)
        throws SQLException, cwSysMessage , cwException {                        

            Hashtable data = null;
            boolean useSess = false;
            Timestamp sess_pagetime = null;
            String sess_order = null;
            String sess_sort = null;
            if (sess !=null) {
                data = (Hashtable) sess.getAttribute("Competency_SKILL_SET_LIST");
                if (data !=null) {
                    sess_pagetime = (Timestamp) data.get(HASH_TIMESTAMP);
                    sess_order = (String) data.get(HASH_ORDERBY);
                    sess_sort = (String) data.get(HASH_SORTBY);
                    if ( sess_pagetime.equals(pagetime) )
                        useSess = true;
                } else {
                    data = new Hashtable();
                }
            }
                       
            // determine the SQL statement order by
            int order = 3;
            if( order_by != null && order_by.equalsIgnoreCase("title") )
                order = 3;
            else if( order_by != null && order_by.equalsIgnoreCase("count") )
                order = 1;
            
            int start = (cur_page-1) * pagesize + 1;
            int end  = cur_page * pagesize;

            Vector idVecFromSess = new Vector();
            Vector idVecFromDb = new Vector();
            ResultSet rs = null;
            if( useSess ) {
                idVecFromSess = (Vector)data.get(HASH_SKS_ID_VEC);
                //change order by or sort by will goto page 1 of the serach result
                if( ( sess_order != null && !sess_order.equalsIgnoreCase(order_by) ) ||
                    ( sess_sort != null && !sess_sort.equalsIgnoreCase(sort_by) )   ) {
                        start = 1;
                        end = idVecFromSess.size();
                        cur_page = 1;
                    }
                long[] id = new long[end - start + 1];
                for(int i=start; i<=idVecFromSess.size() && i<=end; i++)
                    id[i-start] = ((Long)idVecFromSess.elementAt(i-1)).longValue();
                ViewCmSkillCoverage Vsc = new ViewCmSkillCoverage();
                if( sks_type.equalsIgnoreCase(SKILL_PROFILE) || sks_type.equalsIgnoreCase(SKILL_TEMPLATE) )
                    rs = Vsc.getSkillSetInfo(con, sks_type, id, order, sort_by, 0);
                    
            } else {
                ViewCmSkillCoverage Vsc = new ViewCmSkillCoverage();
                if( sks_type.equalsIgnoreCase(SKILL_PROFILE) || sks_type.equalsIgnoreCase(SKILL_TEMPLATE) )
                    rs = Vsc.getSkillSetInfo(con, sks_type, null, order, sort_by, root_id);
            }


            StringBuffer xmlBody = new StringBuffer();
            while( rs.next() ) {
                if( idVecFromDb.size() < pagesize ) {
                    long ss_id = rs.getLong(2);                    
                    xmlBody.append("<skill_set id=\"").append(ss_id).append("\" ")
                           .append(" title=\"").append(cwUtils.esc4XML(rs.getString(3))).append("\" ")
                           .append(" count=\"").append(rs.getLong(1)).append("\" ");
                    
                    if( sks_type.equalsIgnoreCase(SKILL_TEMPLATE) ) {
                        if( DbCmAssessment.checkSurvey(con, ss_id) )
                            xmlBody.append(" attempt=\"Y\" ");
                        else
                            xmlBody.append(" attempt=\"N\" ");                        
                    }

                    xmlBody.append(" />").append(cwUtils.NEWL);
                }                
                idVecFromDb.addElement(new Long(rs.getLong(2)));
            }
            
            int count = 0;
            if( useSess ) {
                count = idVecFromSess.size();
                if( ( sess_order != null && !sess_order.equalsIgnoreCase(order_by) ) ||
                    ( sess_sort != null && !sess_sort.equalsIgnoreCase(sort_by) )) 
                        data.put(HASH_SKS_ID_VEC, idVecFromDb);    
            } else {
                count = idVecFromDb.size();
                pagetime = cwSQL.getTime(con);
                data.put(HASH_TIMESTAMP, pagetime);
                data.put(HASH_SKS_ID_VEC, idVecFromDb);
            }                                
            data.put(HASH_ORDERBY, order_by);
            data.put(HASH_SORTBY, sort_by);            
            sess.setAttribute("Competency_SKILL_SET_LIST", data);
            
            
            StringBuffer xml = new StringBuffer();
            xml.append("<skill_sets total=\"").append(count).append("\" ")
               .append(" cur_page=\"").append(cur_page).append("\" ")
               .append(" pagesize=\"").append(pagesize).append("\" ")
               .append(" timestamp=\"").append(pagetime).append("\" ")
               .append(" order_by=\"").append(order_by).append("\" ")
               .append(" sort_by=\"").append(sort_by).append("\" ")
               .append(" >").append(cwUtils.NEWL)
               .append(xmlBody)
               .append("</skill_sets>").append(cwUtils.NEWL);
               
            rs.close();  
            
            return xml.toString();
        }

        
        
        
    //append the picked skill set into the vector
    public void pickSkillSet(Connection con, Vector vec)
        throws SQLException, cwSysMessage {
                    
            DbCmSkillSetCoverage DbSsc = new DbCmSkillSetCoverage();
            DbSsc.ssc_sks_skb_id = sks_skb_id;
            Vector idVec = DbSsc.getSkillBaseId(con);
            for(int i=0; i<idVec.size(); i++) {                
                if( !vec.contains((Long)idVec.elementAt(i)) )
                    vec.addElement((Long)idVec.elementAt(i));
            }
            
            return;
        }


        
        
        
        
        
        
    /**
    Create Survey
    @param type of the skill set
    @param title of the survey
    @param comment_ind overall comment of the survey
    @return long value of the Skill Set id which the coverage connected to it
    */
    public long createSurvey(Connection con, String type, String title, boolean comment_ind, long root_id, String usr_id)
        throws SQLException, cwSysMessage {
            
            
            DbCmSkillSet DbSs = new DbCmSkillSet();
            DbSs.sks_type = type;
            DbSs.sks_title = title;
            //DbSs.skb_type = SKILL_SET;
            if( comment_ind )
                DbSs.sks_xml = "<comment provide=\"Y\"/>";
            else
                DbSs.sks_xml = "<comment provide=\"N\"/>";            
            DbSs.sks_owner_ent_id = root_id;
            DbSs.sks_create_usr_id = usr_id;
            DbSs.sks_update_usr_id = usr_id;
            DbSs.ins(con);

            return DbSs.sks_skb_id;
/*
            sks_skb_id = DbSs.skb_id;
            DbSs.sks_type = SKILL_SECTION;
            DbSs.sks_title = "";//title;
            DbSs.ins(con);

            DbCmSkillSetCoverage DbSsc = new DbCmSkillSetCoverage();
            DbSsc.ssc_sks_skb_id = sks_skb_id;
            DbSsc.ssc_skb_id = DbSs.skb_id;
            
            if( comment_ind )
                DbSsc.ssc_xml = "<comment>Y</comment>";
            else
                DbSsc.ssc_xml = "<comment>N</comment>";

            DbSsc.ins(con);

            return DbSs.skb_id;
*/        
        }

    /**
    Clone the input skill set to a Assessment Survey
    @sks_id skill set id to be clone
    @usr_id used as create_usr_id and update_usr_id of the Assessment Survey
    @return Vector with 1st element: cloned Assessment Survey as DbCmSkillSet
                        2nd element: cloned SkillSetCoverage as DbCmSkillSetCoverage[]
    */
    public Vector clone2ASY(Connection con, long sks_id, String usr_id) 
        throws SQLException, cwSysMessage {
        //Get DB Time
        Timestamp curTime = cwSQL.getTime(con);
            
        //Get SKT details
        DbCmSkillSet skillSet = new DbCmSkillSet();
        skillSet.sks_skb_id = sks_id;
        skillSet.get(con);
        
        //Get SKT's SkillSetCoverage
        ViewCmSkillCoverage Vcsc = new ViewCmSkillCoverage();
        DbCmSkillSetCoverage[] DbSsc = Vcsc.getAllSkillsSetCoverage(con, skillSet.sks_skb_id);
        
        //Clone SKT to ASY and return ASY's sks_skb_id
        skillSet.sks_skb_id = 0;
        skillSet.sks_create_usr_id = usr_id;
        skillSet.sks_create_timestamp = curTime;
        skillSet.sks_update_usr_id = usr_id;
        skillSet.sks_update_timestamp = curTime;
        skillSet.sks_type = DbCmSkillSet.ASSESSMENT_SURVEY;
        skillSet.ins(con);
        
        //Clone SkillSetCoverage with ASY's sks_skb_id
        for(int i=0; i<DbSsc.length; i++) {
            DbSsc[i].ssc_sks_skb_id = skillSet.sks_skb_id;
            DbSsc[i].ins(con);
        }
        Vector v = new Vector();
        v.addElement(skillSet);
        v.addElement(DbSsc);
        return v;
    }

    /**
    Get a list of skill set as XML
    @param con Connection to database
    @param type type of skill sets to be search
    @param root_ent_id organization site id of skill set to be search
    @return XML of a list of skill set 
    */
    public String getSkillSetListAsXML(Connection con, String type, long root_ent_id) throws SQLException {
        ViewCmSkillCoverage Vsc = new ViewCmSkillCoverage();
        StringBuffer xmlBuf = new StringBuffer(512);
        xmlBuf.append("<skill_set_list>");
        ResultSet rs = null;
        try {
            rs = Vsc.getSkillSetInfo(con, SKILL_TEMPLATE, null, 3, "asc", root_ent_id);
            while(rs.next()) {
                xmlBuf.append("<skill_set id=\"").append(rs.getLong(2)).append("\" ")
                      .append(" title=\"").append(cwUtils.esc4XML(rs.getString(3))).append("\" ")
                      .append(" count=\"").append(rs.getLong(1)).append("\"/>");
		    }
        } finally {
            if(rs!=null) rs.close();
        }
        xmlBuf.append("</skill_set_list>");
        return xmlBuf.toString();
    }

    /**
    Get XML for the purpose of viewing a survey
    Predefine variable: this.sks_skb_id
    @param con Connection to database
    @return XML for the purpose of viewing a survey
    */
    public String viewSurveyAsXML(Connection con) throws SQLException, cwSysMessage {

        StringBuffer xmlBuf = new StringBuffer(1024);
        
        if( this.sks_skb_id > 0 ) {
            //get SkillSet details
            DbCmSkillSet DbSs = new DbCmSkillSet();
            DbSs.sks_skb_id = sks_skb_id;
            DbSs.get(con);
            
            xmlBuf.append("<survey id=\"").append(DbSs.sks_skb_id).append("\"")
                  .append(" type=\"").append(DbSs.sks_type).append("\">")
                  .append("<title>").append(cwUtils.esc4XML(DbSs.sks_title)).append("</title>")
                  .append("<last_upd_timestamp>").append(DbSs.sks_update_timestamp).append("</last_upd_timestamp>");
            if(DbSs.sks_xml != null && DbSs.sks_xml.length() > 0) {
                xmlBuf.append(DbSs.sks_xml);                    
            } else {
                xmlBuf.append("<comment provide=\"N\"/>");
            }

            //get SkillSetCoverage details
            xmlBuf.append(getSurveyContentAsXML(con));
            
            xmlBuf.append("</survey>");
        }
        return xmlBuf.toString();
    }

    /**
    Get the questions and scales used in the survey
    Predefine variable: this.sks_skb_id
    @param con Conncetion to database
    @return XML showing questions and scale used in the survey
    */
    public String getSurveyContentAsXML(Connection con) throws SQLException, cwSysMessage {
        StringBuffer xmlBuf = new StringBuffer(1024);
        ViewCmSkillCoverage Vcsc = new ViewCmSkillCoverage();
        DbCmSkillSetCoverage[] DbSsc = Vcsc.getAllSkillsSetCoverage(con, this.sks_skb_id);
        
        xmlBuf.append("<content>");
        for(int i=0; i<DbSsc.length; i++) {
            //get skill details
            DbCmSkill DbSkill = new DbCmSkill();
            DbSkill.skl_skb_id = DbSsc[i].ssc_skb_id;
            DbSkill.skb_id = DbSsc[i].ssc_skb_id;
            DbSkill.get(con);
            
            xmlBuf.append("<composite_skill id=\"").append(DbSkill.skl_skb_id).append("\">")
                  .append("<title>").append(cwUtils.esc4XML(DbSkill.skb_title)).append("</title>")
                  .append("<desc>").append(cwUtils.esc4XML(DbSkill.skb_description)).append("</desc>")
                  .append("<rating_ind>").append(DbSkill.skl_rating_ind).append("</rating_ind>");

            //get skill's scale details
            DbCmSkillLevel skillLevel = new DbCmSkillLevel();
            skillLevel.sle_ssl_id = DbSkill.skb_ssl_id;
            Vector vec = skillLevel.getById(con);            
            xmlBuf.append("<scale max_level=\"").append(vec.size()).append("\" >").append(cwUtils.NEWL);
            for(int j=0; j<vec.size(); j++) {
                xmlBuf.append(((DbCmSkillLevel)vec.elementAt(j)).asXML());
            }
            xmlBuf.append("</scale>").append(cwUtils.NEWL);                                        
            
            //get the child skills if DbSkill is not for rating
            if(!DbSkill.skl_rating_ind) {
                xmlBuf.append("<skill_list>");
                Vector vSkillChildren = DbSkill.getSkillChild(con);
                for(int k=0; k<vSkillChildren.size(); k++) {
                    DbCmSkill skillChild = (DbCmSkill)vSkillChildren.elementAt(k);
                    xmlBuf.append("<skill id=\"").append(skillChild.skb_id).append("\">")
                          .append("<desc>").append(cwUtils.esc4XML(skillChild.skb_description)).append("</desc>")
                          .append("</skill>");
                }
                xmlBuf.append("</skill_list>");
            }
            xmlBuf.append("</composite_skill>");
        }
        xmlBuf.append("</content>");

        return xmlBuf.toString();
    }

    
    public String getItmAndUsrRelationSkillSetXml(Connection con, long sks_id_lst[]) throws SQLException{
    	String xml="";
    	String idLst="(0";
    	if(sks_id_lst.length>0){
    		for(int i=0; i<sks_id_lst.length; i++){
    			idLst+=",";
    			idLst+=sks_id_lst[i];
    		}
    	}
    	idLst+=")";
    	Vector itmVc=DbItemTargetRuleDetail.getItmBySkillSet(con, idLst);
    	xml="<itm_lst>";
    	if(itmVc!=null && !itmVc.isEmpty()){
    		for(int i=0; i<itmVc.size(); i++){
    			aeItem itm =(aeItem)itmVc.elementAt(i);
    			xml=xml+"<itm id=\""+itm.itm_id+"\" title=\""+ cwUtils.esc4XML(itm.itm_title)+"\" code=\""+ cwUtils.esc4XML(itm.itm_code)+"\"/>";
    		}
    	}
    	xml+="</itm_lst>";
    	Vector usrVc=DbUserSkillSet.getUserBysKillSet(con, idLst);
    	xml+="<usr_lst>";
    	if(usrVc!=null && !usrVc.isEmpty()){
    		for(int i=0; i<usrVc.size(); i++){
    			loginProfile usr=(loginProfile)usrVc.elementAt(i);
    			xml=xml+"<usr id=\""+usr.usr_ent_id+"\" display_bil=\""+ cwUtils.esc4XML(usr.usr_display_bil)+"\" group=\""+ cwUtils.esc4XML(usr.directGroup)+"\" usr_id=\""+ cwUtils.esc4XML(usr.usr_ste_usr_id)+"\"/>";
    		}
    	}
    	xml+="</usr_lst>";
    	xml+=DbUserSkillSet.getSkillSetXmlById(con, idLst);
		return xml;	
    }



}
