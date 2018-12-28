package com.cw.wizbank.competency;

import java.sql.*;
import java.util.*;
import com.cw.wizbank.ae.aeItemCompetency;
import com.cw.wizbank.util.*;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.db.DbCmSkillBase;
import com.cw.wizbank.db.DbCmSkill;
import com.cw.wizbank.db.DbCmSkillLevel;
import com.cw.wizbank.db.DbCmSkillTreeNode;
import com.cw.wizbank.db.view.ViewCmSkillGap;
import com.cw.wizbank.db.view.ViewCmAssessment;
import com.cw.wizbank.db.view.ViewCmSkill;
import com.cw.wizbank.db.view.ViewCmSkillCoverage;
import com.cw.wizbank.db.view.ViewRoleTargetGroup;
import com.cw.wizbank.qdb.dbEntityRelation;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.dbEntity;
import com.cw.wizbank.qdb.dbUserGroup;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.ae.aeLearningSoln;

public class CmSkillGapAnalyzer {


    private final static String HASH_SKILL_ID_VEC       =   "skill_id_vector";
    private final static String HASH_LEVEL_VEC          =   "level_vector";
    private final static String SKILL_PROFILE           =   "SKP";
    private final static String USR                     =   "USR";
    private final static String PROF                    =   "PROF";
    private final static String NOT_ATTEMPTED           =   "NOT_ATTEMPTED";
    private final static String UNKNOWN_DERIVE_RULE     =   "UNKNOWN_DERIVE_RULE";

    private boolean sourceCmpProf = false;
    private boolean sourceCmpEntity = false;
    private boolean targetCmpProf = false;
    private boolean targetCmpEntity = false;
    private long root_id;
    private long source_id;
    private long target_id;
    private long source_asm_id;
    private long target_asm_id;
    private String source_type;
    private String target_type;
    private String source_ent_type;
    private String target_ent_type;
    private Timestamp source_date;
    private Timestamp target_date;
    private Timestamp source_resolved_timestamp;
    private Timestamp target_resolved_timestamp;
    private boolean source_show_all;
    private boolean target_show_all;
    private boolean show_course_recomm;

    public CmSkillGapAnalyzer() {;}

    public void setParam(Connection con, long root_id, 
                         long source_id, long target_id, 
                         long source_asm_id, long target_asm_id, 
                         String source_type, String target_type, 
                         Timestamp source_date, Timestamp target_date, 
                         boolean source_show_all, boolean target_show_all,
                         boolean show_course_recomm) throws SQLException, cwException {
        
        dbEntity entity;
        if(source_date == null && source_type != null && source_type.equalsIgnoreCase(USR)) 
            source_date = cwSQL.getTime(con);
        if(target_date == null && target_type != null && target_type.equalsIgnoreCase(USR)) 
            target_date = cwSQL.getTime(con);
        this.root_id = root_id;
        this.source_id = source_id;
        this.target_id = target_id;
        this.source_asm_id = source_asm_id;
        this.target_asm_id = target_asm_id;
        this.source_date = source_date;
        this.target_date = target_date;
        this.source_type = source_type;
        this.target_type = target_type;
        this.source_show_all = source_show_all;
        this.target_show_all = target_show_all;
        this.show_course_recomm = show_course_recomm;
        try {
            if(this.source_type != null && this.source_type.length() > 0 ) {
                if(this.source_type.equalsIgnoreCase(USR) ) {
                    this.sourceCmpEntity = true;
                    this.sourceCmpProf = false;
                    entity = new dbEntity();
                    entity.ent_id = source_id;
                    entity.getById(con);
                    this.source_ent_type = entity.ent_type;
                }
                else if( source_type.equalsIgnoreCase(PROF) ) {
                    this.sourceCmpEntity = false;
                    this.sourceCmpProf = true;
                    this.source_ent_type = null;
                }
            }
            if(this.target_type != null && this.target_type.length() > 0 ) {
                if(this.target_type.equalsIgnoreCase(USR) ) {
                    this.targetCmpEntity = true;
                    this.targetCmpProf = false;
                    entity = new dbEntity();
                    entity.ent_id = target_id;
                    entity.getById(con);
                    this.target_ent_type = entity.ent_type;
                }
                else if(target_type.equalsIgnoreCase(PROF) ) {
                    this.targetCmpProf = true;
                    this.targetCmpEntity = false;
                    this.target_ent_type = null;
                }
            }
        } catch (qdbException e) {
            throw new cwException (e.getMessage());
        }
        if(this.source_asm_id > 0 )
            this.source_resolved_timestamp = ViewCmAssessment.getResolvedDate(con, this.source_asm_id);
        else
            this.source_resolved_timestamp = this.source_date;
                        
        if( this.target_asm_id > 0 )
            this.target_resolved_timestamp = ViewCmAssessment.getResolvedDate(con, this.target_asm_id);
        else
            this.target_resolved_timestamp = this.target_date;
    
    }


    /**
    Get the entity info with corresponding assessment info as XML
    */
    public String getEntityInfoAsXML(Connection con, long ent_id, long selected_id, Timestamp resolved_date)
        throws SQLException, cwException {

            try{
                dbEntity ent = new dbEntity();
                ent.ent_id = ent_id;
                ent.getById(con);

                StringBuffer xml = new StringBuffer();
                if (ent.ent_type.equalsIgnoreCase(dbEntity.ENT_TYPE_USER)) {
                    dbRegUser dbUser = new dbRegUser();
                    dbUser.usr_ent_id = ent_id;
                    dbUser.getByEntId(con);

                    xml.append("<USR")
                       .append(" id=\"").append(ent_id).append("\" ")
                       .append(" ent_id=\"").append(dbUser.usr_ent_id).append("\" ")
                       .append(" display_name=\"").append(cwUtils.esc4XML(dbUser.usr_display_bil)).append("\" >")
                       .append(cwUtils.NEWL);

                    ResultSet rs = ViewCmAssessment.getAssesseeInfo(con, ent_id);
                    if( selected_id > 0 )
                        xml.append("<assessments selected_id=\"").append(selected_id).append("\">").append(cwUtils.NEWL);
                    else
                        xml.append("<assessments>").append(cwUtils.NEWL);
                    while( rs.next() ) {
                        xml.append("<assessment id=\"").append(rs.getLong("asm_id")).append("\" ")
                           .append(" title=\"").append(cwUtils.esc4XML(rs.getString("asm_title"))).append("\" ")
                           .append(" resolved_date=\"").append(rs.getTimestamp("sks_update_timestamp")).append("\"/>")
                           .append(cwUtils.NEWL);
                    }
                    xml.append("</assessments>").append(cwUtils.NEWL);
                    rs.close();

                    xml.append("</USR>").append(cwUtils.NEWL);

                } else {
                    dbUserGroup dbGroup = new dbUserGroup();
                    dbGroup.usg_ent_id = ent_id;
                    dbGroup.get(con);

                    xml.append("<USG")
                       .append(" ent_id=\"").append(ent_id).append("\" ")
                       .append(" display_bil=\"").append(cwUtils.esc4XML(dbGroup.usg_display_bil)).append("\">")
                       .append(cwUtils.NEWL);

                    if( resolved_date != null )
                        xml.append("<assessments timestamp=\"").append(resolved_date).append("\"/>").append(cwUtils.NEWL);

                    xml.append("</USG>").append(cwUtils.NEWL);
                }                

                return xml.toString();
            }catch( qdbException e) {
                throw new cwException(e.getMessage());
            }

        }




    /**
    Get competency profile as XML
    @param root_id
    @return string of xml
    */
    public String getCmpProfileInfoAsXML(Connection con, long sks_id, long root_id)
        throws SQLException {

            StringBuffer xml = new StringBuffer();
            xml.append("<SKILL_SETS ");
            if( sks_id > 0 )
                xml.append("selected_id=\"").append(sks_id).append("\"");
            xml.append(">").append(cwUtils.NEWL);

            xml.append(getSkillSetInfoAsXML(con, root_id));

            xml.append("</SKILL_SETS>").append(cwUtils.NEWL);

            return xml.toString();
        }

    public String getEntitySkillLevelAsXML(Connection con, long ent_id,
                                           Timestamp reviewDate, long asm_id,
                                           long skill_id)
                                           throws SQLException, cwException, cwSysMessage {
        StringBuffer xmlBuf=new StringBuffer(1024);
        try {
            if (reviewDate == null ) {
                reviewDate = cwSQL.getTime(con);
            }
            //vendor xml for skill ancestor
            xmlBuf.append(getSkillAsXML(con, skill_id));
            dbEntity ent = new dbEntity();
            ent.ent_id = ent_id;            
            ent.getById(con);
            if (ent.ent_type.equalsIgnoreCase(dbEntity.ENT_TYPE_USER)) {
                dbRegUser dbUser = new dbRegUser();
                dbUser.usr_ent_id = ent_id;                
                dbUser.getByEntId(con);
                xmlBuf.append(getUserSkillLevelAsXML(con, dbUser, 
                                             reviewDate, asm_id,
                                             skill_id));
            } else {
                dbUserGroup dbGroup = new dbUserGroup();
                dbGroup.usg_ent_id = ent_id;
                dbGroup.get(con);
                xmlBuf.append(getUserGroupSkillLevelAsXML(con, dbGroup, 
                                                  reviewDate, asm_id,
                                                  skill_id));
            }
        } catch(qdbException e) {
            throw new cwException(e.getMessage());
        }
        return xmlBuf.toString();
    }


    /**
    Get User's Skill Level of the child skills of input skill id
    Capable of calculating COMPOSITE_SKILL
    @parem skill_id if <= 0, means get top level skills
    */
    private String getUserSkillLevelAsXML(Connection con, dbRegUser dbUser, 
                                          Timestamp reviewDate, long asm_id,
                                          long skill_id) 
                                          throws SQLException, cwException, cwSysMessage {
        StringBuffer xml = new StringBuffer();
        //vendor xml header
        xml.append("<USR timestamp=\"").append(reviewDate).append("\"")
            .append(" id=\"").append(dbUser.usr_id).append("\" ")
            .append(" ent_id=\"").append(dbUser.usr_ent_id).append("\" ")
            .append(" display_name=\"").append(cwUtils.esc4XML(dbUser.usr_display_bil)).append("\" >")
            .append(cwUtils.NEWL);

        if(skill_id <= 0) {
            xml.append(DbCmSkillTreeNode.getTitleListAsXML(con, dbUser.usr_ste_ent_id));
        } else {
            //get skill's children
            DbCmSkill skill = new DbCmSkill();
            skill.skl_skb_id = skill_id;
            skill.skb_id = skill_id;
            Vector vChildSkill = skill.getSkillChild(con);
            CmSkillLevelCalculator calc = new CmSkillLevelCalculator();
            //get scale max level.
            //if skill_id>0, non-top level skills
            //can get the scale max level here as all
            //child skill are sharing the same scale
            int scale_max_level = 0;
            if(vChildSkill.size()>0) {
                if(skill_id>0) {
                    scale_max_level = DbCmSkillLevel.getMaxLevel(con, ((DbCmSkill)vChildSkill.elementAt(0)).skb_ssl_id);
                }
            }
            //for each child, calculate user's score and vendor XML
            for(int i=0; i<vChildSkill.size(); i++) {
                DbCmSkill childSkill = (DbCmSkill)vChildSkill.elementAt(i);
                float score = calc.getRollUpLevel(con, childSkill.skl_skb_id, 
                                                dbUser, reviewDate);

                //get scale max level.
                //if skill_id<=0, looking at top level
                //need to get each skill's scale max level 
                //as top level skills may use different scale
                if(skill_id<=0) {
                    scale_max_level = DbCmSkillLevel.getMaxLevel(con, childSkill.skb_ssl_id);
                }
                xml.append("<skill id=\"").append(childSkill.skl_skb_id).append("\" ")
                .append(" type=\"").append(childSkill.skb_type).append("\" ")
                .append(" max_level=\"").append(scale_max_level).append("\" ")
                .append(" level=\"");
                if(score == CmSkillLevelCalculator.NOT_ATTEMPTED) 
                    xml.append(NOT_ATTEMPTED);
                else if(score == CmSkillLevelCalculator.UNKNOWN_DERIVE_RULE)
                    xml.append(UNKNOWN_DERIVE_RULE);
                else 
                    xml.append(score);
                xml.append("\" >").append(cwUtils.NEWL)
                .append("<title>").append(cwUtils.esc4XML(childSkill.skb_title)).append("</title>").append(cwUtils.NEWL)
                .append("<desc>").append(cwUtils.esc4XML(childSkill.skb_description)).append("</desc>").append(cwUtils.NEWL)
                .append("<rating_ind>").append(childSkill.skl_rating_ind).append("</rating_ind>")
                .append("</skill>").append(cwUtils.NEWL);                
            }
        }

        //vendor xml footor
        xml.append("</USR>").append(cwUtils.NEWL);
        ResultSet rs = ViewCmAssessment.getAssesseeInfo(con, dbUser.usr_ent_id);
        if( asm_id > 0 )
            xml.append("<assessments selected_id=\"").append(asm_id).append("\">").append(cwUtils.NEWL);
        else
            xml.append("<assessments>").append(cwUtils.NEWL);
        while( rs.next() ) {
            xml.append("<assessment id=\"").append(rs.getLong("asm_id")).append("\" ")
                .append(" title=\"").append(cwUtils.esc4XML(rs.getString("asm_title"))).append("\" ")
                .append(" resolved_date=\"").append(rs.getTimestamp("sks_update_timestamp")).append("\"/>")
                .append(cwUtils.NEWL);
        }
        xml.append("</assessments>").append(cwUtils.NEWL);                

        return xml.toString();
    }


    /**
    Get UserGroup's average Skill Level of the child skills of input skill id
    Capable of calculating COMPOSITE_SKILL
    @parem skill_id if <= 0, means get top level skills
    */
    private String getUserGroupSkillLevelAsXML(Connection con, dbUserGroup dbGroup, 
                                               Timestamp reviewDate, long asm_id,
                                               long skill_id) 
                                               throws SQLException, cwException, cwSysMessage {
        StringBuffer xml = new StringBuffer();
        xml.append("<USG timestamp=\"").append(reviewDate).append("\"")
            .append(" ent_id=\"").append(dbGroup.usg_ent_id).append("\" ")
            .append(" display_bil=\"").append(cwUtils.esc4XML(dbGroup.usg_display_bil)).append("\">")
            .append(cwUtils.NEWL);

        if(skill_id <= 0) {
            xml.append(DbCmSkillTreeNode.getTitleListAsXML(con, dbGroup.usg_ent_id_root));
        } else {
            //get users under input user group
        	dbEntityRelation dbEr = new dbEntityRelation();
        	dbEr.ern_ancestor_ent_id = dbGroup.usg_ent_id;
        	dbEr.ern_type = dbEntityRelation.ERN_TYPE_USR_PARENT_USG;
            Vector vMemUserEntId = dbEr.getChildUser(con);
            long[] memUserEntId = cwUtils.vec2longArray(vMemUserEntId);
    
            //get skill's children
            DbCmSkill skill = new DbCmSkill();
            skill.skb_id = skill_id;
            Vector vChildSkill = skill.getSkillChild(con);
            CmSkillLevelCalculator calc = new CmSkillLevelCalculator();
            //get scale max level.
            //if skill_id>0, non-top level skills
            //can get the scale max level here as all
            //child skill are sharing the same scale
            int scale_max_level = 0;
            if(vChildSkill.size()>0) {
                if(skill_id>0) {
                    scale_max_level = DbCmSkillLevel.getMaxLevel(con, ((DbCmSkill)vChildSkill.elementAt(0)).skb_ssl_id);
                }
            }
            //for each child, calculate usergroup's average score and vendor XML
            for(int i=0; i<vChildSkill.size(); i++) {
                DbCmSkill childSkill = (DbCmSkill)vChildSkill.elementAt(i);
                float score = calc.getRollUpLevel(con, childSkill.skl_skb_id, 
                                                  memUserEntId, reviewDate);
                //get scale max level.
                //if skill_id<=0, looking at top level
                //need to get each skill's scale max level 
                //as top level skills may use different scale
                if(skill_id<=0) {
                    scale_max_level = DbCmSkillLevel.getMaxLevel(con, childSkill.skb_ssl_id);
                }
                xml.append("<skill id=\"").append(childSkill.skl_skb_id).append("\" ")
                   .append(" type=\"").append(childSkill.skl_type).append("\" ")
                   .append(" max_level=\"").append(scale_max_level).append("\" ")
                   .append(" level=\"");
                if(score == CmSkillLevelCalculator.NOT_ATTEMPTED) 
                    xml.append(NOT_ATTEMPTED);
                else if(score == CmSkillLevelCalculator.UNKNOWN_DERIVE_RULE)
                    xml.append(UNKNOWN_DERIVE_RULE);
                else 
                    xml.append(score);
                xml.append("\" >").append(cwUtils.NEWL)
                   .append("<title>").append(cwUtils.esc4XML(childSkill.skb_title)).append("</title>").append(cwUtils.NEWL)
                   .append("<desc>").append(cwUtils.esc4XML(childSkill.skb_description)).append("</desc>").append(cwUtils.NEWL)
                   .append("</skill>").append(cwUtils.NEWL);                
            }
        }
        xml.append("</USG>").append(cwUtils.NEWL);
        return xml.toString();
    }

    /**
    Get user/user group skill
    */
    public String viewUsrSkill(Connection con, long source_id, long selected_id, Timestamp source_date)
        throws SQLException, cwException {

        try{
            StringBuffer xml = new StringBuffer();
                        
            dbEntity ent = new dbEntity();
            ent.ent_id = source_id;            
            ent.getById(con);
                        
            if (ent.ent_type.equalsIgnoreCase(dbEntity.ENT_TYPE_USER)) {
                dbRegUser dbUser = new dbRegUser();
                dbUser.usr_ent_id = source_id;                
                dbUser.getByEntId(con);
                

                xml.append("<USR timestamp=\"").append(cwSQL.getTime(con)).append("\"")
                   .append(" id=\"").append(dbUser.usr_id).append("\" ")
                   .append(" ent_id=\"").append(dbUser.usr_ent_id).append("\" ")
                   .append(" display_name=\"").append(cwUtils.esc4XML(dbUser.usr_display_bil)).append("\" >")
                   .append(cwUtils.NEWL);
            } else {
                dbUserGroup dbGroup = new dbUserGroup();
                dbGroup.usg_ent_id = source_id;
                dbGroup.get(con);

                xml.append("<USG timestamp=\"").append(cwSQL.getTime(con)).append("\"")
                   .append(" ent_id=\"").append(source_id).append("\" ")
                   .append(" display_bil=\"").append(cwUtils.esc4XML(dbGroup.usg_display_bil)).append("\">")
                   .append(cwUtils.NEWL);
            }
            
            Hashtable sourceSkill = new Hashtable();
            Vector skillIdVec = new Vector();
            ResultSet rs = null;
            if( source_date != null ) {
                sourceSkill = getEntitySkills(con, source_id, source_date, null);            
                Enumeration enumeration = sourceSkill.keys();
                skillIdVec = cwUtils.enum2vector(enumeration);

                Long skill_id;
                float level;
                if( !skillIdVec.isEmpty() ) {
                    rs = ViewCmSkill.getByIds(con, "ASC", skillIdVec);
                    while( rs.next() ) {
                        skill_id = new Long(rs.getLong("skb_id"));
                        level = ((Float)sourceSkill.get(skill_id)).floatValue();

                        xml.append("<skill id=\"").append(skill_id).append("\" ")
                           .append(" max_level=\"").append(rs.getLong("max_level")).append("\" ")
                           .append(" level=\"").append(level).append("\" >").append(cwUtils.NEWL)
                           .append("<title>").append(cwUtils.esc4XML(rs.getString("skb_title"))).append("</title>").append(cwUtils.NEWL)
                           .append("<desc>").append(cwUtils.esc4XML(rs.getString("skb_description"))).append("</desc>").append(cwUtils.NEWL)
                           .append("</skill>").append(cwUtils.NEWL);                
                    }
                }
            }
            if (ent.ent_type.equalsIgnoreCase(dbEntity.ENT_TYPE_USER)) {
                xml.append("</USR>").append(cwUtils.NEWL);

                rs = ViewCmAssessment.getAssesseeInfo(con, source_id);
                    if( selected_id > 0 )
                        xml.append("<assessments selected_id=\"").append(selected_id).append("\">").append(cwUtils.NEWL);
                    else
                        xml.append("<assessments>").append(cwUtils.NEWL);
                    while( rs.next() ) {
                        xml.append("<assessment id=\"").append(rs.getLong("asm_id")).append("\" ")
                           .append(" title=\"").append(cwUtils.esc4XML(rs.getString("asm_title"))).append("\" ")
                           .append(" resolved_date=\"").append(rs.getTimestamp("sks_update_timestamp")).append("\"/>")
                           .append(cwUtils.NEWL);
                    }
                    xml.append("</assessments>").append(cwUtils.NEWL);                
            }
            else
                xml.append("</USG>").append(cwUtils.NEWL);

            return xml.toString();
        }catch( qdbException e) {
            throw new cwException ( e.getMessage() );
        }
        }


    public String getSkillGapHeaderAsXML(Connection con)
                                         throws cwException, SQLException {
        StringBuffer xml = new StringBuffer();
        xml.append("<compare_items timestamp=\"").append(cwSQL.getTime(con)).append("\" >").append(cwUtils.NEWL);
        // source detial as xml
        xml.append("<source ");
        if(this.sourceCmpEntity) {
            xml.append(" type=\"USR\" ");
        } else if(this.sourceCmpProf) {
            xml.append(" type=\"PROF\" ");
        }
        xml.append(">").append(cwUtils.NEWL);
        if(this.sourceCmpEntity && this.source_id > 0 ) {
            xml.append(getEntityInfoAsXML(con, this.source_id, this.source_asm_id, this.source_date)).append(cwUtils.NEWL);
            xml.append(getCmpProfileInfoAsXML(con, 0, this.root_id)).append(cwUtils.NEWL);
        }else
            xml.append(getCmpProfileInfoAsXML(con, this.source_id, this.root_id)).append(cwUtils.NEWL);
        xml.append("</source>").append(cwUtils.NEWL);

        //target detial as xml
        xml.append("<target ");
        if(this.targetCmpEntity) {
            xml.append(" type=\"USR\" ");
        } else if(this.targetCmpProf) {
            xml.append(" type=\"PROF\" ");
        }
        xml.append(">").append(cwUtils.NEWL);
        if( this.targetCmpEntity && this.target_id > 0 ) {
            xml.append(getEntityInfoAsXML(con, this.target_id, this.target_asm_id, this.target_date)).append(cwUtils.NEWL);
            xml.append(getCmpProfileInfoAsXML(con, 0, this.root_id)).append(cwUtils.NEWL);
        } else
            xml.append(getCmpProfileInfoAsXML(con, this.target_id, this.root_id)).append(cwUtils.NEWL);
        xml.append("</target>").append(cwUtils.NEWL);
        xml.append("</compare_items>").append(cwUtils.NEWL);

        return xml.toString();
    }

    /**
    Do Skill Gap Analysis 
    And vendor the result xml <comparison>
    */
    private String getSkillGapCmpAsXML(Connection con, long skill_id) 
        throws SQLException, cwException, cwSysMessage {

//System.out.println("D: skill_id = " + skill_id);        
        
        StringBuffer xml = new StringBuffer(512);
        if(this.sourceCmpEntity && this.targetCmpEntity) {
            if( this.source_id > 0 && this.source_resolved_timestamp != null
            &&  this.target_id > 0 && this.target_resolved_timestamp != null ) {
                xml.append(cmpEntities(con, skill_id));
            }                
        } 
        else if((this.sourceCmpEntity && this.targetCmpProf)) {
            if( this.source_id > 0 && this.source_resolved_timestamp != null
                &&  this.target_id > 0 ) {
                xml.append(cmpEntitiesProfile(con));
            }                             
        } 
        else if( this.sourceCmpProf && this.targetCmpEntity ) {
            if( this.target_id > 0 && this.target_resolved_timestamp != null
                &&  this.source_id > 0 ) {
                xml.append(cmpProfileEntities(con));
            }             
        } 
        else if( this.sourceCmpProf && this.targetCmpProf ) {
                if( this.source_id > 0 && this.target_id > 0 )
                    xml.append(cmpProfile(con, this.source_id, this.target_id, this.source_show_all, this.target_show_all, this.show_course_recomm));
        }
        return xml.toString();
    }
    
    /**
    Do skill gap analysis on input source and target
    */
    public String getSkillGapAsXML(Connection con, long root_id, 
                                   long source_id, long target_id, 
                                   long source_asm_id, long target_asm_id, 
                                   String source_type, String target_type, 
                                   Timestamp source_date, Timestamp target_date, 
                                   boolean source_show_all, boolean target_show_all,
                                   boolean show_course_recomm, long skill_id) 
                                   throws cwException, SQLException, cwSysMessage {

        StringBuffer xml = new StringBuffer(1024);
        setParam(con, root_id, source_id, target_id, source_asm_id, target_asm_id,
                 source_type, target_type, source_date, target_date, 
                 source_show_all, target_show_all, show_course_recomm);
        //vendor xml for skill ancestor
        xml.append(getSkillAsXML(con, skill_id));
        //vendor xml header (<compare_items>...</compare_items>)
        xml.append(getSkillGapHeaderAsXML(con)) 
           .append(cwUtils.NEWL);
        //do comparison
        xml.append(getSkillGapCmpAsXML(con, skill_id))
           .append(cwUtils.NEWL);
        
        return xml.toString();
    }



    /**
    Get the skill gap between source and target
    */
    public String getSkillGap(Connection con, long root_id, long source_id, long target_id, 
                              long source_asm_id, long target_asm_id, String source_type, 
                              String target_type, Timestamp source_date, Timestamp target_date, 
                              boolean source_show_all, boolean target_show_all,
                              boolean show_course_recomm)
        throws SQLException, cwException {
            
            boolean sourceCmpEntity = false;
            boolean sourceCmpProf = false;
            
            StringBuffer xml = new StringBuffer();
            xml.append("<compare_items timestamp=\"").append(cwSQL.getTime(con)).append("\" >").append(cwUtils.NEWL);

            // source detial as xml
            xml.append("<source ");
            if( source_type != null && source_type.length() > 0 ) {
                if( source_type.equalsIgnoreCase(USR) ) {
                    xml.append(" type=\"USR\" ");
                    sourceCmpEntity = true;
                }
                else if( source_type.equalsIgnoreCase(PROF) ) {
                    xml.append(" type=\"PROF\" ");
                    sourceCmpProf = true;
                }
            }
            xml.append(">").append(cwUtils.NEWL);
            
            if( sourceCmpEntity && source_id > 0 ) {
                xml.append(getEntityInfoAsXML(con, source_id, source_asm_id, source_date)).append(cwUtils.NEWL);
                xml.append(getCmpProfileInfoAsXML(con, 0, root_id)).append(cwUtils.NEWL);
            }else
                xml.append(getCmpProfileInfoAsXML(con, source_id, root_id)).append(cwUtils.NEWL);
                
            xml.append("</source>").append(cwUtils.NEWL);


            boolean targetCmpProf = false;
            boolean targetCmpEntity = false;
            //target detial as xml
            xml.append("<target ");
            if( target_type != null && target_type.length() > 0 ) {
                if( target_type.equalsIgnoreCase(USR) ) {
                    xml.append(" type=\"USR\" ");
                    targetCmpEntity = true;
                }
                else if( target_type.equalsIgnoreCase(PROF) ) {
                    xml.append(" type=\"PROF\" ");
                    targetCmpProf = true;
                }
            }
            xml.append(">").append(cwUtils.NEWL);
            
            if( targetCmpEntity && target_id > 0 ) {
                xml.append(getEntityInfoAsXML(con, target_id, target_asm_id, target_date)).append(cwUtils.NEWL);
                xml.append(getCmpProfileInfoAsXML(con, 0, root_id)).append(cwUtils.NEWL);
            } else
                xml.append(getCmpProfileInfoAsXML(con, target_id, root_id)).append(cwUtils.NEWL);
                
            xml.append("</target>").append(cwUtils.NEWL);
            xml.append("</compare_items>").append(cwUtils.NEWL);

            
            Timestamp source_resolved_timestamp;
            Timestamp target_resolved_timestamp;
            
            
            
            if( sourceCmpEntity && targetCmpEntity) {
                if( source_id > 0 && ( source_asm_id > 0 || source_date != null )
                &&  target_id > 0 && ( target_asm_id > 0 || target_date != null ) ) {
                    
                    if( source_asm_id > 0 )
                        source_resolved_timestamp = ViewCmAssessment.getResolvedDate(con, source_asm_id);
                    else
                        source_resolved_timestamp = source_date;
                    
                    if( target_asm_id > 0 )
                        target_resolved_timestamp = ViewCmAssessment.getResolvedDate(con, target_asm_id);
                    else
                        target_resolved_timestamp = target_date;
                    
                    xml.append(cmpEntities(con, source_id, target_id, source_resolved_timestamp, target_resolved_timestamp, source_show_all, target_show_all, show_course_recomm));
                }                
                
            } else if( sourceCmpEntity && targetCmpProf ) {
                if( source_id > 0 && ( source_asm_id > 0 || source_date != null )
                &&  target_id > 0 ) {
                    
                    if( source_asm_id > 0 )
                        source_resolved_timestamp = ViewCmAssessment.getResolvedDate(con, source_asm_id);
                    else
                        source_resolved_timestamp = source_date;
                    
                    xml.append(cmpEntitiesProfile(con, source_id, target_id, source_resolved_timestamp, source_show_all, target_show_all, show_course_recomm));
                }                             
            } else if( sourceCmpProf && targetCmpEntity ) {
                if( target_id > 0 && ( target_asm_id > 0 || target_date != null )
                &&  source_id > 0 ) {
                    
                    if( target_asm_id > 0 )
                        target_resolved_timestamp = ViewCmAssessment.getResolvedDate(con, target_asm_id);
                    else
                        target_resolved_timestamp = target_date;
                    
                    xml.append(cmpProfileEntities(con, source_id, target_id, target_resolved_timestamp, source_show_all, target_show_all, show_course_recomm));
                }             
            } else if( sourceCmpProf && targetCmpProf ) {
                    if( source_id > 0 && target_id > 0 )
                        xml.append(cmpProfile(con, source_id, target_id, source_show_all, target_show_all, show_course_recomm));
            }
            return xml.toString();

        }




    /**
    Skill Gap Analysis between USER/USERGROUP AND PROFILE
    */
    public String cmpEntitiesProfile(Connection con)
        throws SQLException, cwException, cwSysMessage {

        ResultSet rs = null;
        Hashtable courseRecomm = new Hashtable();
        StringBuffer xml = new StringBuffer();
        Long skill_id;
        float target_level = 0;
        float source_level = 0;
        dbRegUser source_user = null;
        Vector vMemUserEntId = null;
        long[] source_member_user_id = null;

        try {
            rs = ViewCmSkillCoverage.getRelation(con, this.target_id, null);
            xml.append("<comparison>");
            CmSkillLevelCalculator calc = new CmSkillLevelCalculator();
            //get the entity details
            if(this.source_ent_type.equals(dbEntity.ENT_TYPE_USER)) {
                //get source user details
                source_user = new dbRegUser();
                source_user.usr_ent_id = this.source_id;
                source_user.get(con);
            } else if(this.source_ent_type.equals(dbEntity.ENT_TYPE_USER_GROUP)) {
                //get users under source user group
            	dbEntityRelation dbEr = new dbEntityRelation();
            	dbEr.ern_ancestor_ent_id = this.source_id;
            	dbEr.ern_type = dbEntityRelation.ERN_TYPE_USR_PARENT_USG;
                vMemUserEntId = dbEr.getChildUser(con);
                source_member_user_id = cwUtils.vec2longArray(vMemUserEntId);
            }
            while (rs.next()) {
                skill_id = new Long(rs.getLong("ssc_skb_id"));
                target_level = rs.getFloat("ssc_level");
                //get source entity score on this skill
                if(source_user != null) {
                    source_level = calc.getRollUpLevel(con, 
                                                    skill_id.longValue(), 
                                                    source_user, 
                                                    this.source_resolved_timestamp);
                } else if (source_member_user_id != null) {
                    source_level = calc.getRollUpLevel(con, 
                                                    skill_id.longValue(), 
                                                    source_member_user_id, 
                                                    this.source_resolved_timestamp);
                } else {
                    source_level = 0;
                }
                xml.append(skillGapAsXML(skill_id.longValue(), rs.getLong("max_level"), rs.getLong("ssc_priority"), rs.getString("skb_title"),
                                            rs.getString("skb_description"), source_level, target_level));
                                             
                if( target_level > source_level )
                    courseRecomm.put(skill_id, new Float(target_level));
            }
            xml.append("</comparison>").append(cwUtils.NEWL);
            
            //get course recommendation
            if( show_course_recomm && !courseRecomm.isEmpty()) {
                aeItemCompetency aeItmCm = new aeItemCompetency();
                xml.append(aeItmCm.itmSkillXML(con, courseRecomm, source_id, "ASC"));
            }
        } catch(qdbException e) {
            throw new cwException(e.getMessage());
        } finally {
            if(rs!=null) rs.close();
        }
        return xml.toString();
    }





    /**
    Skill Gap Analysis between USER/USERGROUP AND PROFILE
    @param source id is user/usergroup entity id
    @param target id is profile id
    @param scoure date : user/usergroup resolved score before these date
    */
    public String cmpEntitiesProfile(Connection con, long source_id, long target_id, Timestamp source_date, boolean source_show_all, boolean target_show_all, boolean show_course_recomm)
        throws SQLException, cwException {

            ResultSet rs = ViewCmSkillCoverage.getRelation(con, target_id, null);
            Hashtable sourceSkill = new Hashtable();
            Vector skillIdVec = new Vector();
            Vector _target = new Vector();
            Hashtable courseRecomm = new Hashtable();

            if( source_show_all ) {
                sourceSkill = getEntitySkills(con, source_id, source_date, null);
            } else {
                while( rs.next() )
                    skillIdVec.addElement(new Long(rs.getLong("ssc_skb_id")));
                rs.beforeFirst();
                sourceSkill = getEntitySkills(con, source_id, source_date, skillIdVec);
            }

            StringBuffer xml = new StringBuffer();
            Long skill_id;
            float target_level = 0;
            float source_level = 0;

            xml.append("<comparison>");
            while (rs.next()) {
                skill_id = new Long(rs.getLong("ssc_skb_id"));

                if( !sourceSkill.containsKey(skill_id) ) {
                    _target.addElement(skill_id);
                    continue;
                }
                else {
                    source_level = ((Float) sourceSkill.get(skill_id)).floatValue();
                    sourceSkill.remove(skill_id);
                }
                target_level = rs.getFloat("ssc_level");

                xml.append(skillGapAsXML(skill_id.longValue(), rs.getLong("max_level"), rs.getLong("ssc_priority"), rs.getString("skb_title"),
                                         rs.getString("skb_description"), source_level, target_level));
                                         
                if( target_level > source_level )
                    courseRecomm.put(skill_id, new Float(target_level));
            }
            xml.append("</comparison>").append(cwUtils.NEWL);


            if( target_show_all ) {                
                xml.append("<target_skills>").append(cwUtils.NEWL);
                rs.beforeFirst();
                while( rs.next() ) {
                    skill_id = new Long(rs.getLong("ssc_skb_id"));
                    if( !_target.remove(skill_id) )
                        continue;
                    
                    xml.append(skillGapAsXML(skill_id.longValue(), rs.getLong("max_level"), rs.getLong("ssc_priority"), rs.getString("skb_title"),
                                             rs.getString("skb_description"), Float.MIN_VALUE, rs.getFloat("ssc_level")));
                    
                    if( _target.isEmpty() )
                        break;
                }
                xml.append("</target_skills>").append(cwUtils.NEWL);
            }
            
            if( source_show_all ) {
                xml.append("<source_skills>").append(cwUtils.NEWL);
                Enumeration enumeration = sourceSkill.keys();
                skillIdVec = cwUtils.enum2vector(enumeration);
                if( !skillIdVec.isEmpty() ) {
                    ResultSet _rs = ViewCmSkill.getByIds(con, "ASC", skillIdVec);                

                    while( _rs.next() ) {
                        skill_id = new Long(_rs.getLong("skb_id"));
                        source_level = ((Float)sourceSkill.get(skill_id)).floatValue();

                        xml.append(skillGapAsXML(skill_id.longValue(), _rs.getLong("max_level"), Long.MIN_VALUE, _rs.getString("skb_title"),
                                                 _rs.getString("skb_description"), source_level, Float.MIN_VALUE));                    
                    }
                    _rs.close();
                    xml.append("</source_skills>").append(cwUtils.NEWL);
                }
            }
            
            if( show_course_recomm && !courseRecomm.isEmpty()) {
                aeItemCompetency aeItmCm = new aeItemCompetency();
                xml.append(aeItmCm.itmSkillXML(con, courseRecomm, source_id, "ASC"));
            }
            
            rs.close();
            

            return xml.toString();
        }


    /**
    Skill Gap Analysis between USER/USERGROUP AND PROFILE
    */
    public String cmpProfileEntities(Connection con)
        throws SQLException, cwException, cwSysMessage {

        ResultSet rs = null;
        Hashtable courseRecomm = new Hashtable();
        StringBuffer xml = new StringBuffer();
        Long skill_id;
        float target_level = 0;
        float source_level = 0;
        dbRegUser target_user = null;
        Vector vMemUserEntId = null;
        long[] target_member_user_id = null;

        try {
            rs = ViewCmSkillCoverage.getRelation(con, this.source_id, null);
            xml.append("<comparison>");
            CmSkillLevelCalculator calc = new CmSkillLevelCalculator();
            //get the entity details
            if(this.target_ent_type.equals(dbEntity.ENT_TYPE_USER)) {
                //get source user details
                target_user = new dbRegUser();
                target_user.usr_ent_id = this.target_id;
                target_user.get(con);
            } else if(this.target_ent_type.equals(dbEntity.ENT_TYPE_USER_GROUP)) {
                //get users under source user group
            	dbEntityRelation dbEr = new dbEntityRelation();
            	dbEr.ern_ancestor_ent_id = this.target_id;
            	dbEr.ern_type = dbEntityRelation.ERN_TYPE_USR_PARENT_USG;
                vMemUserEntId = dbEr.getChildUser(con);
                target_member_user_id = cwUtils.vec2longArray(vMemUserEntId);
            }
            while (rs.next()) {
                skill_id = new Long(rs.getLong("ssc_skb_id"));
                source_level = rs.getFloat("ssc_level");
                //get target entity score on this skill
                if(target_user != null) {
                    target_level = calc.getRollUpLevel(con, 
                                                    skill_id.longValue(), 
                                                    target_user, 
                                                    this.target_resolved_timestamp);
                } else if (target_member_user_id != null) {
                    target_level = calc.getRollUpLevel(con, 
                                                    skill_id.longValue(), 
                                                    target_member_user_id, 
                                                    this.target_resolved_timestamp);
                } else {
                    target_level = 0;
                }
                xml.append(skillGapAsXML(skill_id.longValue(), rs.getLong("max_level"), rs.getLong("ssc_priority"), rs.getString("skb_title"),
                                            rs.getString("skb_description"), source_level, target_level));
                                             
                if( target_level > source_level )
                    courseRecomm.put(skill_id, new Float(target_level));
            }
            xml.append("</comparison>").append(cwUtils.NEWL);
            
            //get course recommendation
            if( show_course_recomm && !courseRecomm.isEmpty()) {
                aeItemCompetency aeItmCm = new aeItemCompetency();
                xml.append(aeItmCm.itmSkillXML(con, courseRecomm, source_id, "ASC"));
            }
        } catch(qdbException e) {
            throw new cwException(e.getMessage());
        } finally {
            if(rs!=null) rs.close();
        }
        return xml.toString();
    }


    /**
    Skill Gap Analysis between PROFILE AND USER/USERGROUP
    @param source id is profile id
    @param target id is user/usergroup entity id
    @param target date : user/usergroup resolved score before these date
    */
    public String cmpProfileEntities(Connection con, long source_id, long target_id, Timestamp target_date, boolean source_show_all, boolean target_show_all, boolean show_course_recomm)
        throws SQLException, cwException {

            ResultSet rs = ViewCmSkillCoverage.getRelation(con, source_id, null);
            Hashtable targetSkill = new Hashtable();
            Vector skillIdVec = new Vector();
            Vector _source = new Vector();

            if( target_show_all ) {
                targetSkill = getEntitySkills(con, target_id, target_date, null);
            } else {
                while( rs.next() )
                    skillIdVec.addElement(new Long(rs.getLong("ssc_skb_id")));
                rs.beforeFirst();
                targetSkill = getEntitySkills(con, target_id, target_date, skillIdVec);
            }

            StringBuffer xml = new StringBuffer();
            Long skill_id;
            float target_level = 0;
            float source_level = 0;
            Hashtable courseRecomm = new Hashtable();
            
            xml.append("<comparison>");
            while (rs.next()) {
                skill_id = new Long(rs.getLong("ssc_skb_id"));

                if( !targetSkill.containsKey(skill_id) ) {
                    _source.addElement(skill_id);
                    continue;
                }
                else {
                    target_level = ((Float) targetSkill.get(skill_id)).floatValue();
                    targetSkill.remove(skill_id);
                }
                source_level = rs.getFloat("ssc_level");

                if( target_level > source_level )
                    courseRecomm.put(skill_id, new Float(target_level));


                xml.append(skillGapAsXML(skill_id.longValue(), rs.getLong("max_level"), rs.getLong("ssc_priority"), rs.getString("skb_title"),
                                         rs.getString("skb_description"), source_level, target_level));
            }
            xml.append("</comparison>").append(cwUtils.NEWL);
                        
            
            if( target_show_all ) {
                xml.append("<target_skills>").append(cwUtils.NEWL);
                Enumeration enumeration = targetSkill.keys();
                skillIdVec = cwUtils.enum2vector(enumeration);
                if( !skillIdVec.isEmpty() ) {
                    ResultSet _rs = ViewCmSkill.getByIds(con, "ASC", skillIdVec);                

                    while( _rs.next() ) {
                        skill_id = new Long(_rs.getLong("skb_id"));
                        target_level = ((Float)targetSkill.get(skill_id)).floatValue();

                        xml.append(skillGapAsXML(skill_id.longValue(), _rs.getLong("max_level"), Long.MIN_VALUE, _rs.getString("skb_title"),
                                                 _rs.getString("skb_description"), Float.MIN_VALUE, target_level));
                    }
                    _rs.close();
                    xml.append("</target_skills>").append(cwUtils.NEWL);
                }
            }

            if( source_show_all ) {
                xml.append("<source_skills>").append(cwUtils.NEWL);
                rs.beforeFirst();
                while( rs.next() ) {
                    skill_id = new Long(rs.getLong("ssc_skb_id"));
                    if( !_source.remove(skill_id) )
                        continue;
                    
                    xml.append(skillGapAsXML(skill_id.longValue(), rs.getLong("max_level"), rs.getLong("ssc_priority"), rs.getString("skb_title"),
                                             rs.getString("skb_description"), rs.getFloat("ssc_level"), Float.MIN_VALUE));
                    
                    if( _source.isEmpty() )
                        break;
                }
                xml.append("</source_skills>").append(cwUtils.NEWL);
            }            
            rs.close();
            
            
            if( show_course_recomm && !courseRecomm.isEmpty()) {
                aeItemCompetency aeItmCm = new aeItemCompetency();
                xml.append(aeItmCm.itmSkillXML(con, courseRecomm, 0, "ASC"));
            }
            

            return xml.toString();
        }







    /**
    Skill Gap analysis between Competency Profile
    */
    public String cmpProfile(Connection con, long source_id, long target_id, boolean source_show_all, boolean target_show_all, boolean show_course_recomm)
        throws SQLException, cwException {

            ResultSet rs = ViewCmSkillCoverage.getRelation(con, target_id, null);
            Vector skillIdVec = new Vector();
            Hashtable sourceSkill;
            if( !source_show_all ) {
                while( rs.next() )
                    skillIdVec.addElement(new Long(rs.getLong("ssc_skb_id")));
                //rs.beforeFirst();
                rs=ViewCmSkillCoverage.getRelation(con, target_id, null);
                sourceSkill = ViewCmSkillCoverage.getRelationHash(con, source_id, skillIdVec);
            } else
                sourceSkill = ViewCmSkillCoverage.getRelationHash(con, source_id, null);

            StringBuffer xml = new StringBuffer();

            Long skill_id;
            float target_level = 0;
            float source_level = 0;
            Vector _target = new Vector();
            Hashtable courseRecomm = new Hashtable();

            xml.append("<comparison>");
            while (rs.next()) {
                skill_id = new Long(rs.getLong("ssc_skb_id"));

                // only show comparable items
                if( !sourceSkill.containsKey(skill_id) ) {
                    //save the unshowed skill id in target to vector
                    _target.addElement(skill_id);
                    continue;
                } else {
                    source_level = ((Float) sourceSkill.get(skill_id)).floatValue();
                    sourceSkill.remove(skill_id);
                }
                target_level = rs.getFloat("ssc_level");

                if( target_level > source_level )
                    courseRecomm.put(skill_id, new Float(target_level));

                xml.append(skillGapAsXML(skill_id.longValue(), rs.getLong("max_level"), rs.getLong("ssc_priority"), rs.getString("skb_title"),
                                         rs.getString("skb_description"), source_level, target_level));
            }
            xml.append("</comparison>").append(cwUtils.NEWL);
            
            
            // show remaining item in the target
            if( target_show_all ) {
                xml.append("<target_skills>").append(cwUtils.NEWL);
                rs.beforeFirst();
                while( rs.next() ) {
                    skill_id = new Long(rs.getLong("ssc_skb_id"));
                    if( !_target.remove(skill_id) )
                        continue;
                    target_level = rs.getFloat("ssc_level");
                    xml.append(skillGapAsXML(skill_id.longValue(), rs.getLong("max_level"), rs.getLong("ssc_priority"), rs.getString("skb_title"),
                                         rs.getString("skb_description"), Float.MIN_VALUE, target_level));
                    
                    if( _target.isEmpty() )
                        break;
                }
                xml.append("</target_skills>").append(cwUtils.NEWL);
            }

            //show remaining item in the source
            if( source_show_all ) {
                xml.append("<source_skills>").append(cwUtils.NEWL);
                Enumeration enumeration = sourceSkill.keys();
                Vector idVec = cwUtils.enum2vector(enumeration);
                if( !idVec.isEmpty() ) {
                    ResultSet _rs = ViewCmSkillCoverage.getRelation(con, source_id, idVec);
                    while( _rs.next() ) {
                        skill_id = new Long(_rs.getLong("ssc_skb_id"));
                        source_level = _rs.getFloat("ssc_level");

                        xml.append(skillGapAsXML(skill_id.longValue(), _rs.getLong("max_level"), _rs.getLong("ssc_priority"), _rs.getString("skb_title"),
                                                _rs.getString("skb_description"), source_level, Float.MIN_VALUE));
                    }
                    _rs.close();
                }
                xml.append("</source_skills>").append(cwUtils.NEWL);
            }
            

            rs.close();
            
            if( show_course_recomm && !courseRecomm.isEmpty()) {
                aeItemCompetency aeItmCm = new aeItemCompetency();
                xml.append(aeItmCm.itmSkillXML(con, courseRecomm, 0, "ASC"));
            }
            
            
            return xml.toString();
        }


    /**
    Skill Gap Analysis between USER/USERGROUP AND USER/USERGROUP of 
    the input skill_id's child skills
    */
    public String cmpEntities(Connection con, long skill_id)
        throws SQLException, cwException, cwSysMessage {

        StringBuffer xml = new StringBuffer(1024);
        Hashtable courseRecomm = new Hashtable();
        dbRegUser source_user = null;
        dbRegUser target_user = null;
        long[] source_member_user_id = null;
        long[] target_member_user_id = null;
        Vector vMemUserEntId = null;
        float source_score = 0;
        float target_score = 0;
        long root_ent_id = 0;

        //get entity details
        try {
            if(this.source_ent_type.equals(dbEntity.ENT_TYPE_USER)) {
                //get source user details
                source_user = new dbRegUser();
                source_user.usr_ent_id = this.source_id;
                source_user.get(con);
                //get root_ent_id
                root_ent_id = source_user.usr_ste_ent_id;
            } else if(this.source_ent_type.equals(dbEntity.ENT_TYPE_USER_GROUP)) {
                //get users under source user group
            	dbEntityRelation dbEr = new dbEntityRelation();
            	dbEr.ern_ancestor_ent_id = this.source_id;
            	dbEr.ern_type = dbEntityRelation.ERN_TYPE_USR_PARENT_USG;
                vMemUserEntId = dbEr.getChildUser(con);
                source_member_user_id = cwUtils.vec2longArray(vMemUserEntId);
                //get root_ent_id
                dbUserGroup usg = new dbUserGroup();
                usg.usg_ent_id = this.source_id;
                usg.get(con);
                root_ent_id = usg.usg_ent_id_root;
            }
            if(this.target_ent_type.equals(dbEntity.ENT_TYPE_USER)) {
                //get target user details
                target_user = new dbRegUser();
                target_user.usr_ent_id = this.target_id;
                target_user.get(con);
            } else if(this.target_ent_type.equals(dbEntity.ENT_TYPE_USER_GROUP)) {
                //get users under target user group
            	dbEntityRelation dbEr = new dbEntityRelation();
            	dbEr.ern_ancestor_ent_id = this.target_id;
            	dbEr.ern_type = dbEntityRelation.ERN_TYPE_USR_PARENT_USG;
                vMemUserEntId = dbEr.getChildUser(con);
                target_member_user_id = cwUtils.vec2longArray(vMemUserEntId);
            }
        } catch(qdbException e) {
            throw new cwException(e.getMessage());
        }

        if(skill_id <= 0) {
            xml.append(DbCmSkillTreeNode.getTitleListAsXML(con, root_ent_id));
        } else {
            
            //get skill's children
            DbCmSkill skill = new DbCmSkill();
            skill.skl_skb_id = skill_id;
            skill.skb_id = skill_id;
            Vector vChildSkill = skill.getSkillChild(con);
            CmSkillLevelCalculator calc = new CmSkillLevelCalculator();
            //get scale max level.
            //if skill_id>0, non-top level skills
            //can get the scale max level here as all
            //child skill are sharing the same scale
            int scale_max_level = 0;
            if(vChildSkill.size()>0) {
                if(skill_id>0) {
                    scale_max_level = DbCmSkillLevel.getMaxLevel(con, ((DbCmSkill)vChildSkill.elementAt(0)).skb_ssl_id);
                }
            }
            //for each child, calculate user's score and vendor XML
            xml.append("<comparison>").append(cwUtils.NEWL);
            for(int i=0; i<vChildSkill.size(); i++) {
                DbCmSkill childSkill = (DbCmSkill)vChildSkill.elementAt(i);
                //get source entity score on this skill
                if(source_user != null) {
                    source_score = calc.getRollUpLevel(con, 
                                                    childSkill.skl_skb_id, 
                                                    source_user, 
                                                    this.source_resolved_timestamp);
                } else if (source_member_user_id != null) {
                    source_score = calc.getRollUpLevel(con, 
                                                    childSkill.skl_skb_id, 
                                                    source_member_user_id, 
                                                    this.source_resolved_timestamp);
                } else {
                    source_score = 0;
                }
                //get target entity score on this skill
                if(target_user != null) {
                    target_score = calc.getRollUpLevel(con, 
                                                    childSkill.skl_skb_id, 
                                                    target_user, 
                                                    this.target_resolved_timestamp);
                } else if (target_member_user_id != null) {
                    target_score = calc.getRollUpLevel(con, 
                                                    childSkill.skl_skb_id, 
                                                    target_member_user_id, 
                                                    this.target_resolved_timestamp);
                } else {
                    target_score = 0;
                }
                //get scale max level.
                //if skill_id<=0, looking at top level
                //need to get each skill's scale max level 
                //as top level skills may use different scale
                if(skill_id<=0) {
                    scale_max_level = DbCmSkillLevel.getMaxLevel(con, childSkill.skb_ssl_id);
                }
                //put skill_id into recommended course list if target > source
                if(target_score > source_score ) {
                    courseRecomm.put(new Long(childSkill.skl_skb_id), new Float(target_score));
                }
                xml.append(skillGapAsXML(childSkill.skl_skb_id, childSkill.skl_type, 
                                        childSkill.skl_rating_ind, scale_max_level, 
                                        Long.MIN_VALUE, childSkill.skb_title, 
                                        childSkill.skb_description, 
                                        source_score, target_score));
            }
            xml.append("</comparison>");
            if( show_course_recomm && !courseRecomm.isEmpty()) {
                aeItemCompetency aeItmCm = new aeItemCompetency();
                xml.append(aeItmCm.itmSkillXML(con, courseRecomm, 0, "ASC"));
            }
        }
        return xml.toString();
    }
    
    /**
    Skill Gap Analysis between USER/USERGROUP AND USER/USERGROUP
    */
    public String cmpEntities(Connection con, long source_id, long target_id, Timestamp source_date, Timestamp target_date, boolean source_show_all, boolean target_show_all, boolean show_course_recomm)
        throws SQLException, cwException {

            Hashtable targetSkill = getEntitySkills(con, target_id, target_date, null);
            Enumeration enumeration = targetSkill.keys();
            Vector skillIdVec = cwUtils.enum2vector(enumeration);
            ResultSet rs = null;
            Hashtable sourceSkill;
            
            if( source_show_all )
                sourceSkill = getEntitySkills(con, source_id, source_date, null);
            else {                
                sourceSkill = getEntitySkills(con, source_id, source_date, skillIdVec);
            }
            
            if( !skillIdVec.isEmpty() )
                rs = ViewCmSkill.getByIds(con, "ASC", skillIdVec);
            
            StringBuffer xml = new StringBuffer();
            Long skill_id;
            float target_level = 0;
            float source_level = 0;
            Hashtable courseRecomm = new Hashtable();

            xml.append("<comparison>").append(cwUtils.NEWL);
            if( rs != null ) {
                while (rs.next()) {
                    skill_id = new Long(rs.getLong("skb_id"));
                    
                    if( !sourceSkill.containsKey(skill_id) )
                        continue;                
                
                    source_level = ((Float) sourceSkill.get(skill_id)).floatValue();
                    target_level = ((Float) targetSkill.get(skill_id)).floatValue();
                
                    if( target_level > source_level )
                        courseRecomm.put(skill_id, new Float(target_level));

                
                    xml.append(skillGapAsXML(skill_id.longValue(), rs.getLong("max_level"), Long.MIN_VALUE, rs.getString("skb_title"),
                                                rs.getString("skb_description"), source_level, target_level));
                                
                    sourceSkill.remove(skill_id);
                    targetSkill.remove(skill_id);
                }
            }
            xml.append("</comparison>");
            
            if( target_show_all ) {
                xml.append("<target_skills>").append(cwUtils.NEWL);
                if( rs != null ) {
                    rs.beforeFirst();
                    while( rs.next() ) {
                        skill_id = new Long(rs.getLong("skb_id"));
                        if( !targetSkill.containsKey(skill_id) )
                            continue;
                    
                        target_level = ((Float) targetSkill.remove(skill_id)).floatValue();
                        xml.append(skillGapAsXML(skill_id.longValue(), rs.getLong("max_level"), Long.MIN_VALUE, rs.getString("skb_title"),
                                                 rs.getString("skb_description"), Float.MIN_VALUE, target_level));
                    
                        if( targetSkill.isEmpty() )
                            break;
                    }
                }
                xml.append("<target_skills>").append(cwUtils.NEWL);
            }
            
            if( source_show_all ) {
                xml.append("<source_skills>").append(cwUtils.NEWL);
                enumeration = sourceSkill.keys();
                skillIdVec = cwUtils.enum2vector(enumeration);
                if( !skillIdVec.isEmpty() ) {
                    rs = ViewCmSkill.getByIds(con, "ASC", skillIdVec);
                    while( rs.next() ) {
                        skill_id = new Long(rs.getLong("skb_id"));
                        source_level = ((Float) sourceSkill.remove(skill_id)).floatValue();
                        xml.append(skillGapAsXML(skill_id.longValue(), rs.getLong("max_level"), Long.MIN_VALUE, rs.getString("skb_title"),
                                                 rs.getString("skb_description"), source_level, Float.MIN_VALUE));                                         
                    }
                }
                xml.append("</source_skills>").append(cwUtils.NEWL);
            }
            
            if( rs != null )
                rs.close();


            if( show_course_recomm && !courseRecomm.isEmpty()) {
                aeItemCompetency aeItmCm = new aeItemCompetency();
                xml.append(aeItmCm.itmSkillXML(con, courseRecomm, 0, "ASC"));
            }

            return xml.toString();
        }




    /**
    Compare user skill level and specified skills
    @param long of user entity id
    @param Hashtable ( key : skill id , value : search level )
    @param String of XML
    */
    public String cmpSearch(Connection con, long ent_id, Hashtable skillLevelHash, boolean show_all)
        throws SQLException, cwException, cwSysMessage {
            
            Enumeration enumeration = skillLevelHash.keys();
            Vector skillIdVec = cwUtils.enum2vector(enumeration);
            Hashtable userSkill;
            Timestamp curTime = cwSQL.getTime(con);
            if( show_all )
                userSkill = getEntitySkills(con, ent_id, curTime, null);
            else {                
                userSkill = getEntitySkills(con, ent_id, curTime, skillIdVec);
            }
            dbRegUser user = null;
            ResultSet rs = null;
            if( !skillIdVec.isEmpty() )
                rs = ViewCmSkill.getByIds(con, "ASC", skillIdVec);
            StringBuffer xml = new StringBuffer();
            float target_level;
            float source_level;
            Long skill_id;
            xml.append("<comparison>").append(cwUtils.NEWL);
            CmSkillLevelCalculator calc = new CmSkillLevelCalculator();
            if( rs != null ) {
                while( rs.next() ) {
                    skill_id = new Long(rs.getLong("skb_id"));
                    target_level = ((Long)skillLevelHash.get(skill_id)).longValue();
                    if((Float)userSkill.get(skill_id)!=null) {
                        source_level = ((Float)userSkill.get(skill_id)).floatValue();
                    } else {
                        if(user==null) {
                            user = new dbRegUser();
                            user.usr_ent_id = ent_id;
                            try {
                                user.get(con);
                            } catch(qdbException e) {
                                throw new cwException (e.getMessage());
                            }
                        }
                        source_level = calc.getRollUpLevel(con, skill_id.longValue(), user, curTime);
                    }
                    userSkill.remove(skill_id);
                    xml.append(skillGapAsXML(skill_id.longValue(), rs.getLong("max_level"), Long.MIN_VALUE, rs.getString("skb_title"),
                                             rs.getString("skb_description"), source_level, target_level));
                }
                rs.close();
            }
            xml.append("</comparison>").append(cwUtils.NEWL);
            
            if( show_all ) {
                xml.append("<user_skills>").append(cwUtils.NEWL);
                enumeration = userSkill.keys();
                skillIdVec = cwUtils.enum2vector(enumeration);
                if( !skillIdVec.isEmpty() ) {
                    rs = ViewCmSkill.getByIds(con, "ASC", skillIdVec);
                    while( rs.next() ) {
                        skill_id = new Long(rs.getLong("skb_id"));
                        source_level = ((Float)userSkill.get(skill_id)).floatValue();
                        xml.append(skillGapAsXML(skill_id.longValue(), rs.getLong("max_level"), Long.MIN_VALUE, rs.getString("skb_title"),
                                                 rs.getString("skb_description"), source_level, Float.MIN_VALUE));
                    }
                    rs.close();
                }                
                xml.append("</user_skills>").append(cwUtils.NEWL);
            }
            return xml.toString();
        }








































    /**
    Skill Gap Analysis between Competency Profiles
    */
    public static Hashtable getEntitySkills(Connection con, long ent_id, Timestamp end_date, Vector skbIdVec)
        throws SQLException, cwException {

            Hashtable hashUserSkill = new Hashtable();
            Hashtable hashAllUserSkill = new Hashtable();
            ResultSet rs = ViewCmSkillGap.getEntitySkills(con, ent_id, end_date, skbIdVec);

            long prev_ent_id = 0;
            long cur_ent_id = 0;
            long user_cnt = 0;
            Long skill_id = null;
            Float skill_level = null;            

            while( rs.next() ) {
                cur_ent_id = rs.getLong("asm_ent_id");

                skill_id = new Long(rs.getLong("ssc_skb_id"));
                skill_level = new Float(rs.getFloat("ssc_level"));
                // only get the latest resolved score of the skill
                if( cur_ent_id == prev_ent_id ) {
                    if(hashUserSkill.containsKey(skill_id))
                        continue;
                }else {
                    combineSkills(hashUserSkill, hashAllUserSkill);//, user_cnt);
                    hashUserSkill.clear();
                }
                hashUserSkill.put(skill_id, skill_level);

                prev_ent_id = cur_ent_id;
            }

            combineSkills(hashUserSkill, hashAllUserSkill);//, user_cnt);
            
            Vector allgroups;
            try{
                allgroups = dbUserGroup.getChildGroupVec(con, ent_id);
            }catch (qdbException e) {
                throw new cwException(e.getMessage());
            }
            allgroups.addElement(new Long(ent_id));
            long totalUser = dbUserGroup.getTotalUser(con, allgroups);
            
            if( totalUser > 1 ) {
                float total_level;
                float ave_level;
                Enumeration enumeration = hashAllUserSkill.keys();
                while (enumeration.hasMoreElements()) {
                    skill_id = (Long) enumeration.nextElement();
                    total_level = ((Float)hashAllUserSkill.get(skill_id)).floatValue();
                    ave_level = total_level / totalUser ;
                    hashAllUserSkill.put(skill_id, new Float(ave_level));
                }
            }
            return hashAllUserSkill;
        }
        
        
/*        

    public static Hashtable getEntitySkills(Connection con, long ent_id, Timestamp end_date)
        throws SQLException, cwException {

        Hashtable hashAllUserSkill = new Hashtable();
        Hashtable hashUserSkill = new Hashtable();
        ResultSet rs = ViewCmSkillGap.getEntitySkills(con, ent_id, end_date);
        long prev_ent_id = 0;
        long cur_ent_id = 0;
        long user_cnt = 0;
        Long skill_id = null;
        Float skill_level = null;

        while (rs.next()) {
            cur_ent_id = rs.getLong("asm_ent_id");
            skill_id = new Long(rs.getLong("ssc_skb_id"));
            skill_level = new Float(rs.getFloat("ssc_level"));

            if (cur_ent_id != prev_ent_id) {
                combineSkills(hashUserSkill, hashAllUserSkill, user_cnt);
                user_cnt ++;
                hashUserSkill.clear();
            }else {
                // All the skill are order by the date and descending
                if (!hashUserSkill.contains(skill_id)) {
                    hashUserSkill.put(skill_id, skill_level);
                }
            }
        }

        combineSkills(hashUserSkill, hashAllUserSkill, user_cnt);
        rs.close();

        return hashAllUserSkill;
    }
*/



    /**
    All the skill level from the source to target hastable<BR/>
    @param source Source hashtable storing the skill id and skill level
    @param target Target hashtable storing the skill id and skill level
    @return target_size The number of users contributing the target hashtable
    */

    private static void combineSkills (Hashtable source, Hashtable target)//, long target_size)
    {

        Enumeration enumeration = source.keys();
        Long skill_id = null;
        Float source_level = null;
        Float target_level = null;
        Float new_level = null;
        while (enumeration.hasMoreElements()) {
            skill_id = (Long) enumeration.nextElement();
            if (target.containsKey(skill_id)) {
                source_level = (Float) source.get(skill_id);
                target_level = (Float) target.get(skill_id);
                //new_level = new Float((source_level.floatValue() + target_level.floatValue()*(target_size-1))/ (target_size));
                new_level = new Float(source_level.floatValue() + target_level.floatValue());
                target.put(skill_id, new_level);
            } else {
                source_level = (Float) source.get(skill_id);
                new_level = new Float( source_level.floatValue() );
                target.put(skill_id, new_level);
            }
            
        }
        return;
    }



    /**
    Get all skill sets info belong to the specified organization in XML
    @param long value of root id
    */
    public String getSkillSetInfoAsXML(Connection con, long root_id)
        throws SQLException {

            StringBuffer xml = new StringBuffer();
            ViewCmSkillCoverage ViewSc = new ViewCmSkillCoverage();
            ResultSet rs = ViewSc.getSkillSetInfo(con, SKILL_PROFILE, null, 3, "ASC", root_id);

            while( rs.next() ) {
            xml.append("<skill_set id=\"").append(rs.getLong(2)).append("\" ")
               .append(" title=\"").append(cwUtils.esc4XML(rs.getString(3))).append("\" ")
               .append(" count=\"").append(rs.getLong(1)).append("\"/>");
            }

            rs.close();

            return xml.toString();
        }

    /**
     *Generate a skill gap XML
     */
    public String skillGapAsXML(long skill_id, long max_level, long priority, String title, String description, float source, float target)
        {

                if(source == CmSkillLevelCalculator.NOT_ATTEMPTED ||
                   source == CmSkillLevelCalculator.UNKNOWN_DERIVE_RULE) {
                    source = Float.MIN_VALUE;
                }
                if(target == CmSkillLevelCalculator.NOT_ATTEMPTED ||
                   target == CmSkillLevelCalculator.UNKNOWN_DERIVE_RULE) {
                    target = Float.MIN_VALUE;
                }
                
                StringBuffer xml = new StringBuffer();
                xml.append("<skill id=\"").append(skill_id).append("\" ");
                
                if( max_level != Long.MIN_VALUE )
                    xml.append(" max_level=\"").append(max_level).append("\"");

                if( priority != Long.MIN_VALUE )
                   xml.append(" priority=\"").append(priority).append("\"");
                   
                xml.append(">").append(cwUtils.NEWL);

                xml.append("<title>").append(cwUtils.esc4XML(title)).append("</title>").append(cwUtils.NEWL)
                   .append("<desc>").append(cwUtils.esc4XML(description)).append("</desc>").append(cwUtils.NEWL);

                if( source != Float.MIN_VALUE )
                    xml.append("<level source=\"").append(cwUtils.roundingFloat(source,2)).append("\" ");
                else
                    xml.append("<level source=\"NA\" ");

                if( target != Float.MIN_VALUE )
                    xml.append(" target=\"").append(cwUtils.roundingFloat(target,2)).append("\" ");
                else
                    xml.append(" target=\"NA\" ");

                if( source != Float.MIN_VALUE && target != Float.MIN_VALUE )
                    xml.append(" difference=\"").append(cwUtils.roundingFloat(source-target,2)).append("\"/>").append(cwUtils.NEWL);
                else
                    xml.append(" difference=\"NA\"/>").append(cwUtils.NEWL);

                xml.append("</skill>").append(cwUtils.NEWL);
                return xml.toString();
        }
        
    /**
     *Generate a skill gap XML
     */
    public String skillGapAsXML(long skill_id, String skill_type, boolean rating_ind, long max_level, long priority, String title, String description, float source, float target)
        {
                StringBuffer xml = new StringBuffer();
                xml.append("<skill id=\"").append(skill_id).append("\" ")
                   .append(" type=\"").append(skill_type).append("\" ");
                   
                if( max_level != Long.MIN_VALUE )
                    xml.append(" max_level=\"").append(max_level).append("\"");

                if( priority != Long.MIN_VALUE )
                   xml.append(" priority=\"").append(priority).append("\"");
                   
                xml.append(">").append(cwUtils.NEWL);

                xml.append("<title>").append(cwUtils.esc4XML(title)).append("</title>").append(cwUtils.NEWL)
                   .append("<desc>").append(cwUtils.esc4XML(description)).append("</desc>").append(cwUtils.NEWL)
                   .append("<rating_ind>").append(rating_ind).append("</rating_ind>");

                if( source > 0 )
                    xml.append("<level source=\"").append(cwUtils.roundingFloat(source,2)).append("\" ");
                else if( source == CmSkillLevelCalculator.NOT_ATTEMPTED)
                    xml.append("<level source=\"").append(NOT_ATTEMPTED).append("\"");
                else if( source == CmSkillLevelCalculator.UNKNOWN_DERIVE_RULE)
                    xml.append("<level source=\"").append(UNKNOWN_DERIVE_RULE).append("\"");

                if( target > 0 )
                    xml.append(" target=\"").append(cwUtils.roundingFloat(target,2)).append("\" ");
                else if( target == CmSkillLevelCalculator.NOT_ATTEMPTED)
                    xml.append(" target=\"").append(NOT_ATTEMPTED).append("\"");
                else if( target == CmSkillLevelCalculator.UNKNOWN_DERIVE_RULE)
                    xml.append(" target=\"").append(UNKNOWN_DERIVE_RULE).append("\"");

                if( source > 0 && target > 0 )
                    xml.append(" difference=\"").append(cwUtils.roundingFloat(source-target,2)).append("\"/>").append(cwUtils.NEWL);
                else
                    xml.append(" difference=\"NA\"/>").append(cwUtils.NEWL);

                xml.append("</skill>").append(cwUtils.NEWL);
                return xml.toString();
        }

    public String searchUserBySkillSetAsXML(Connection con, loginProfile prof, long skillSetId, Timestamp reviewDate, WizbiniLoader wizbini) 
        throws SQLException, cwException, cwSysMessage {
        Vector[] eleSkill = ViewCmSkillCoverage.getSkillSetSkillByType(con, skillSetId, DbCmSkillBase.COMPETENCY_SKILL);
        Vector[] compSkill = ViewCmSkillCoverage.getSkillSetSkillByType(con, skillSetId, DbCmSkillBase.COMPETENCY_COMPOSITE_SKILL);
        StringBuffer xmlBuf = new StringBuffer(1024);
        xmlBuf.append("<skill_set id=\"").append(skillSetId).append("\"/>");
        xmlBuf.append(searchUserBySkillAsXML(con, prof, eleSkill[0], eleSkill[1], compSkill[0], compSkill[1], reviewDate, wizbini));
        return xmlBuf.toString();
    }

    private Vector[] divideSkill4UserSearch(Vector skillIdVec, Vector levelVec, Vector typeVec) {
        Vector[] vArray = new Vector[4];
        Vector eleSkillIdVec = new Vector();
        Vector eleLevelVec = new Vector();
        Vector compSkillIdVec = new Vector();
        Vector compLevelVec = new Vector();
        
        for(int i=0; i<skillIdVec.size(); i++) {
            String type = (String)typeVec.elementAt(i);
            if(type.equals(DbCmSkillBase.COMPETENCY_SKILL)) {
                eleSkillIdVec.addElement(skillIdVec.elementAt(i));
                eleLevelVec.addElement(levelVec.elementAt(i));
            } else if(type.equals(DbCmSkillBase.COMPETENCY_COMPOSITE_SKILL)) {
                compSkillIdVec.addElement(skillIdVec.elementAt(i));
                compLevelVec.addElement(levelVec.elementAt(i));
            }
        }
        vArray[0] = eleSkillIdVec;
        vArray[1] = eleLevelVec;
        vArray[2] = compSkillIdVec;
        vArray[3] = compLevelVec;
        return vArray;
    }

    public String searchUserBySkillAsXML(Connection con, 
                                         loginProfile prof,
                                         Vector skillIdVec, 
                                         Vector levelVec, 
                                         Vector typeVec,
                                         Timestamp reviewDate, WizbiniLoader wizbini) 
                                         throws SQLException, cwException, cwSysMessage {

        Vector[] vArray = divideSkill4UserSearch(skillIdVec, levelVec, typeVec);
        return searchUserBySkillAsXML(con, prof,
                                      vArray[0], vArray[1],
                                      vArray[2], vArray[3],
                                      reviewDate,wizbini);
    }
    
    /**
    Search users matches the given Elementary Skills level and Composite Skills level
    @param con Connection to database
    @param prof login profile
    @param eleSkillIdVec Elementary Skill id 
    @param eleLevelVec target levels of corresponding Elementary Skill
    @param compSkillIdVec Composite Skill id
    @param compLevelVec target levels of corresponding Composite Skill
    @param reviewDate time that users' assessment data to be considered
    @return well-formed XML
    */
    private String searchUserBySkillAsXML(Connection con, loginProfile prof,
                                          Vector eleSkillIdVec, Vector eleLevelVec,
                                          Vector compSkillIdVec, Vector compLevelVec,
                                          Timestamp reviewDate,WizbiniLoader wizbini) 
                                          throws SQLException, cwException, cwSysMessage {

        if(reviewDate == null) {
            reviewDate = cwSQL.getTime(con);
        }
        Vector userEntIdVec = searchUserBySkill(con, prof, eleSkillIdVec, eleLevelVec, compSkillIdVec, compLevelVec,reviewDate,wizbini);
        StringBuffer xmlBuf = new StringBuffer(1024);
        xmlBuf.append("<search_result>");
        if(userEntIdVec.size() > 0) {
            Vector userVec = dbRegUser.getByEntIdsAsVec(con, userEntIdVec);
            for(int i=0; i<userVec.size(); i++) {
                dbRegUser user = (dbRegUser)userVec.elementAt(i);
                xmlBuf.append("<usr id=\"").append(user.usr_id).append("\" ")
                    .append(" ent_id=\"").append(user.usr_ent_id).append("\" ")
                    .append(" display_bil=\"").append(cwUtils.esc4XML(user.usr_display_bil)).append("\" ")
                    .append(" status=\"ABOVE\">").append(cwUtils.NEWL);
                xmlBuf.append(user.getParentUserGroupListAsXML(con));    
                xmlBuf.append("</usr>");
            }
        }
        xmlBuf.append("</search_result>");
        return xmlBuf.toString();
    }
    
    
    /**
    Search users matches the given Elementary Skills level and Composite Skills level
    @param con Connection to database
    @param prof login profile
    @param eleSkillIdVec Elementary Skill id 
    @param eleLevelVec target levels of corresponding Elementary Skill
    @param compSkillIdVec Composite Skill id
    @param compLevelVec target levels of corresponding Composite Skill
    @param reviewDate time that users' assessment data to be considered
    @return Vector of Long, user entity id
    */
    private Vector searchUserBySkill(Connection con, loginProfile prof,
                                     Vector eleSkillIdVec, Vector eleLevelVec,
                                     Vector compSkillIdVec, Vector compLevelVec,
                                     Timestamp reviewDate, WizbiniLoader wizbini) 
                                     throws SQLException, cwException, cwSysMessage {
        
        Vector userEntIdVec;
        if(eleSkillIdVec != null && eleSkillIdVec.size() > 0) {
            userEntIdVec = searchUserByElementarySkill(con, eleSkillIdVec, eleLevelVec, reviewDate , prof, wizbini);
        } else {
            userEntIdVec = ViewCmAssessment.searchUserHasAssData(con, prof.root_ent_id, prof ,wizbini);
        }
        //if the prof is approver, intersect the searched user with the subordinates
        if(userEntIdVec.size() > 0 && dbUtils.isUserApprRole(con, prof.usr_ent_id, prof.current_role)) {
            Vector apprGroupVec = 
                ViewRoleTargetGroup.getTargetGroupsLrn(con, prof.usr_ent_id, prof.current_role, false);
            userEntIdVec = cwUtils.intersectVectors(userEntIdVec, apprGroupVec);
        }
        
        
        return searchUserByCompositeSkill(con, compSkillIdVec, compLevelVec, userEntIdVec, reviewDate);
    }
    
    /**
    Given a list of Composite skills and target levels and user entity id list,
    search users who match the target levels on each Composite skill
    @param skillIdVec skill id of Composite Skills
    @param levelVec target levels of corresponding Composite Skill
    @param userEntIdVec user entity level to be considered
    @param reviewDate time that users' assessment data to be considered
    */
    private Vector searchUserByCompositeSkill(Connection con, Vector skillIdVec, Vector levelVec, Vector userEntIdVec, Timestamp reviewDate)
        throws SQLException, cwException, cwSysMessage {
        
        Vector vResult = new Vector();
        CmSkillLevelCalculator calc = new CmSkillLevelCalculator();
        //for each user, check if they match all skills' target levels
loopUser:        
        for(int i=0; i<userEntIdVec.size(); i++) {
            //get user's details
            long userEntId = ((Long)userEntIdVec.elementAt(i)).longValue();
            dbRegUser user = new dbRegUser();
            user.usr_ent_id = userEntId;
            try {
                user.get(con);
            } catch (qdbException e) {
                throw new cwException (e.getMessage());
            }
            //for each skill, check this user's level matches the target level
            for(int j=0; j<skillIdVec.size(); j++) {
                long skillId = ((Long)skillIdVec.elementAt(j)).longValue();
                float targetLevel = ((Float)levelVec.elementAt(j)).floatValue();
                float userLevel = calc.getRollUpLevel(con, skillId, user, reviewDate);
                if(userLevel < targetLevel) {
                    //if one of the user's skill level < target level,
                    //loop next user
                    continue loopUser;
                }
            }
            vResult.addElement((Long)userEntIdVec.elementAt(i));
        }
        return vResult;
    }
    
    /**
    Given a list of elementary skills and target levels,
    search users who match the target levels on each elementary skill
    @param skillIdVec skill id of Elementary Skills
    @param levelVec target levels of corresponding Elementary Skill
    @param reviewDate time that users' assessment data to be considered
    */
    private Vector searchUserByElementarySkill(Connection con, Vector skillIdVec, Vector levelVec, Timestamp reviewDate, loginProfile prof, WizbiniLoader wizbini) 
        throws SQLException, cwException {
        
        Vector vResult = new Vector();
        //get users has assessment data on elementary skills
        Vector entIdVec = ViewCmSkillGap.searchUserBySkills(con, skillIdVec,prof, wizbini);
        if(reviewDate == null) {
            reviewDate = cwSQL.getTime(con);
        }
        //for each user, check if they match all skills' target levels
loopUser:
        for(int i=0; i<entIdVec.size(); i++) {
            Hashtable userSkillLevel = getEntitySkills(con, ((Long)entIdVec.elementAt(i)).longValue(), reviewDate, skillIdVec);
            for(int j=0; j<skillIdVec.size(); j++) {
                if( ((Float)userSkillLevel.get(skillIdVec.elementAt(j))).floatValue() < ((Float)levelVec.elementAt(j)).floatValue() ) {
                    //if one of the user's skill level < target level,
                    //loop next user
                    continue loopUser;
                }
            }
            //add this user to Vector as s/he matches all skills' target level
            vResult.addElement((Long)entIdVec.elementAt(i));
        }
        return vResult;
    }


    /**
    Serach users contain the skills specified
    @param Vecotr of skills id 
    @param Vector of level require of the corresponding skill
    @return Hashtable ( key : user entity id, value : ABOVE/BELOW the skills level requirement )
    */
    public static Hashtable searchUser(Connection con, Vector skillIdVec, Vector levelVec, loginProfile prof, WizbiniLoader wizbini)
        throws SQLException, cwException {
            
            Hashtable resultHash = new Hashtable();
            Vector entIdVec = ViewCmSkillGap.searchUserBySkills(con, skillIdVec,prof , wizbini);
            Hashtable userSkillLevel;
            boolean bAbove;
            for(int i=0; i<entIdVec.size(); i++) {
                bAbove = true;
                userSkillLevel = getEntitySkills(con, ((Long)entIdVec.elementAt(i)).longValue(), cwSQL.getTime(con), skillIdVec);
                for(int j=0; j<skillIdVec.size(); j++) {
                    if( ((Float)userSkillLevel.get(skillIdVec.elementAt(j))).floatValue() < ((Long)levelVec.elementAt(j)).longValue() ) {
                        bAbove = false;
                        break;
                    }
                }
                if( bAbove )
                    resultHash.put(entIdVec.elementAt(i), new Boolean(true));
                else
                    resultHash.put(entIdVec.elementAt(i), new Boolean(false));
            }
            
            return resultHash;
        }

    private String getSkillAsXML(Connection con, long skillId) throws SQLException, cwSysMessage {
        StringBuffer xmlBuf = new StringBuffer(512);
        if(skillId > 0) {
            DbCmSkillBase skill = new DbCmSkillBase();
            skill.skb_id = skillId;
            skill.get(con,false);
            xmlBuf.append("<skill id=\"").append(skill.skb_id).append("\"")
                  .append(" title=\"").append(cwUtils.esc4XML(skill.skb_title)).append("\"")
                  .append(">").append(cwUtils.NEWL);
            CmSkillManager mgr = new CmSkillManager();
            xmlBuf.append(mgr.getSkillAncestorAsXML(con, skill))
                  .append(cwUtils.NEWL);
            xmlBuf.append("</skill>");
        } 
        return xmlBuf.toString();
    }

    public String getRecommendItemAsXML(Connection con, long ent_id, long skb_id, long owner_ent_id) 
        throws SQLException, cwException, cwSysMessage {
        
        //get a set of recommended items (course)
        Vector v_itm_id = getRecommendItem(con, skb_id);
        
        //get user-item relation
        aeLearningSoln soln = new aeLearningSoln();
        Hashtable h_itemStatus = soln.getUserItemStatus(con, ent_id, owner_ent_id, v_itm_id);
        
        //format XML
        StringBuffer xmlBuf = new StringBuffer(1024);
        xmlBuf.append("<item_recommendation>");
        //User / UserGroup info
        try {
            dbEntity ent = new dbEntity();
            ent.ent_id = ent_id;
            ent.getById(con);
            if(ent.ent_type.equalsIgnoreCase(dbEntity.ENT_TYPE_USER)) {
                dbRegUser usr = new dbRegUser();
                usr.usr_ent_id = ent_id;
                usr.get(con);
                xmlBuf.append(usr.getUserShortXML(con, false, false));
            } else if(ent.ent_type.equalsIgnoreCase(dbEntity.ENT_TYPE_USER_GROUP)) {
                dbUserGroup usg = new dbUserGroup();
                usg.usg_ent_id = ent_id;
                usg.get(con);
                xmlBuf.append("<user_group id=\"").append(usg.usg_ent_id).append("\">")
                      .append("<title>").append(cwUtils.esc4XML(usg.usg_display_bil)).append("</title>")
                      .append("</user_group>");
            }
        } 
        catch(qdbException cwe) {;}
        //Skill info
        ViewCmSkill vSkill = new ViewCmSkill();
        xmlBuf.append(vSkill.getSkillAsXML(con, skb_id, 0));
        //recommended item info
        xmlBuf.append("<item_list>");
        Enumeration enumeration = h_itemStatus.keys();
        while(enumeration.hasMoreElements()) {
            Long itm_id = (Long)enumeration.nextElement();
            String[] status = (String[])h_itemStatus.get(itm_id);
            xmlBuf.append("<item id=\"").append(itm_id).append("\"")
                  .append(" status=\"").append(status[0]).append("\">")
                  .append("<title>").append(cwUtils.esc4XML(status[1])).append("</title>")
                  .append("</item>");
        }
        xmlBuf.append("</item_list>");
        xmlBuf.append("</item_recommendation>");
        return xmlBuf.toString();
    }
    
    public static final String sql_get_itm_by_skb =
        " select itm_id " +
        " from aeItemCompetency, aeItem " +
        " where itc_skl_skb_id = ? " +
        " and itm_id = itc_itm_id " +
        " and itm_deprecated_ind = ? ";
    /**
    Get a Vector of itm_id of non-deprecated items match with the input skill
    */
    private Vector getRecommendItem(Connection con, long skb_id) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(sql_get_itm_by_skb);
        stmt.setLong(1, skb_id);
        stmt.setBoolean(2, false);
        ResultSet rs = stmt.executeQuery();
        Vector v_itm_id = new Vector();
        while(rs.next()) {
            v_itm_id.addElement(new Long(rs.getLong("itm_id")));
        }
        stmt.close();
        return v_itm_id;
    }

}
