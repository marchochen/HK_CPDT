package com.cw.wizbank.competency;

import java.sql.*;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.util.cwXSL;
import com.cw.wizbank.db.DbCmSkillBase;
import com.cw.wizbank.db.DbCmSkill;
import com.cw.wizbank.db.view.ViewCmSkillGap;

public class CmSkillLevelCalculator {

    private static final String getLevelFromMarkingSchemeXSL = "<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\"><xsl:output method=\"text\"/><xsl:template match=\"/competency\"><xsl:apply-templates select=\"marking_scheme/score\"><xsl:with-param name=\"score\"><xsl:value-of select=\"test/@score\"/></xsl:with-param></xsl:apply-templates></xsl:template><xsl:template match=\"marking_scheme/score\"><xsl:param name=\"score\"/><xsl:if test=\"@from&lt;=$score and @to&gt;=$score\"><xsl:value-of select=\"level/@id\"/></xsl:if></xsl:template></xsl:stylesheet>";
    
    public static final float NOT_ATTEMPTED = -10f;
    public static final float UNKNOWN_DERIVE_RULE = -20f;
    public static final String DERIVE_RULE_MIN = "MIN";
    public static final String DERIVE_RULE_MAX = "MAX";
    public static final String DERIVE_RULE_AVG = "AVG";

    private DbCmSkill skill;
    private dbRegUser user;
    private float score;
    private Timestamp reviewDate;

    private void setReviewDate(Timestamp reviewDate) {
        this.reviewDate = reviewDate;
    }

    private void setUser(dbRegUser user) {
        this.user = user;
    }

    private void setSkill(Connection con, long skill_id) throws SQLException, cwSysMessage {
        this.skill = new DbCmSkill();
        this.skill.skl_skb_id = skill_id;
        this.skill.skb_id = skill_id;
        this.skill.get(con);
        return;        
    }

    private void setUser(Connection con, long usr_ent_id) throws SQLException, cwException {
        try {
            this.user = new dbRegUser();
            this.user.usr_ent_id = usr_ent_id;
            this.user.get(con);
        } catch (qdbException e) {
            throw new cwException (e.getMessage());
        }
        return;
    }
    
    public static String getDeriveRuleListAsXML() {
        StringBuffer xmlBuf = new StringBuffer(256);
        xmlBuf.append("<derive_rule_list>")
              .append("<derive_rule>").append(DERIVE_RULE_MIN).append("</derive_rule>")
              .append("<derive_rule>").append(DERIVE_RULE_MAX).append("</derive_rule>")
              .append("<derive_rule>").append(DERIVE_RULE_AVG).append("</derive_rule>")
              .append("</derive_rule_list>");
        return xmlBuf.toString();
    }

    public float getRollUpLevel(Connection con, long skill_id, long[] usr_ent_id_list, Timestamp reviewDate)
        throws SQLException, cwSysMessage, cwException {
        
        setSkill(con, skill_id);
        setReviewDate(reviewDate);
        float[] score_list = new float[usr_ent_id_list.length];
        for(int i=0; i<usr_ent_id_list.length; i++) {
            setUser(con, usr_ent_id_list[i]);
            score_list[i] = getRollUpLevel(con);
        }
        return averageScore(score_list);
    }

    public float getRollUpLevel(Connection con, long skill_id, long usr_ent_id, Timestamp reviewDate) 
        throws SQLException, cwSysMessage, cwException {
        
        setSkill(con, skill_id);
        setUser(con, usr_ent_id);
        setReviewDate(reviewDate);
        getRollUpLevel(con);
        return this.score;
    }

    public float getRollUpLevel(Connection con, long skill_id, dbRegUser user, Timestamp reviewDate) 
        throws SQLException, cwException, cwSysMessage {
        if(user == null || user.usr_ent_id <= 0) {
            this.score = NOT_ATTEMPTED;
        } else {
            setSkill(con, skill_id);
            setUser(user);
            setReviewDate(reviewDate);
            getRollUpLevel(con);
        }
        return this.score;
    }

    private float getRollUpLevel(Connection con) 
        throws SQLException, cwException, cwSysMessage {
        ViewCmSkillGap userLevel = new ViewCmSkillGap();
        this.score = 0;
        if(this.skill.skl_type.equals(DbCmSkillBase.COMPETENCY_COMPOSITE_SKILL) && !this.skill.skl_rating_ind) {
            // get Elementary Skills and calculate their scores
            Vector vCompScore = new Vector();
            Vector vEleSkillId = this.skill.getChildRatingSkillId(con);
            Hashtable hEleScore = (vEleSkillId==null || vEleSkillId.size()==0) ?
                                   new Hashtable() :
                                   userLevel.getUserSkillLevel(con, this.user.usr_ent_id, this.reviewDate, vEleSkillId);
            if(hasChildElementarySkillNA(hEleScore)) {
                this.score = NOT_ATTEMPTED;
            }
            //if this.score is already NA, no need to proceed to the child COMPOSITE_SKILL
            if(this.score != NOT_ATTEMPTED) {
                Vector vCompSkillId = this.skill.getChildNonRatingSkillId(con);
                CmSkillLevelCalculator calc = null;
                if(vCompSkillId.size() > 0) {
                    calc = new CmSkillLevelCalculator();
                }
                for(int i=0; i<vCompSkillId.size(); i++) {
                    long c_skill_id = ((Long)vCompSkillId.elementAt(i)).longValue();
                    float c_score = calc.getRollUpLevel(con, c_skill_id, this.user, reviewDate);
                    if(c_score == NOT_ATTEMPTED) {
                        this.score = NOT_ATTEMPTED;
                        break;
                    } else {
                        vCompScore.addElement(new Float(c_score));
                    }
                }
            }
            //if this.score is already NA, no need to calculate this.socre from dervie rule
            if(this.score != NOT_ATTEMPTED) {
                deriveScore(hEleScore, vCompScore);
            }
        } else {
            Vector vSkillId = new Vector();
            vSkillId.addElement(new Long(this.skill.skl_skb_id));
            userLevel.getUserSkillLevel(con, this.user.usr_ent_id, this.reviewDate, vSkillId);
            Hashtable scores = userLevel.getScores();
            this.score = ((Float)scores.get(new Long(this.skill.skl_skb_id))).intValue();
        }
        return this.score;
    }
    
    /**
    Check if one of the child elementary skill not attempted
    @param childScores Hashtable returned from ViewCmSkillGap
                       key:     skill id as Long
                       value:   score as Integer
    */
    private boolean hasChildElementarySkillNA(Hashtable childScores) {
        
        boolean result=false;
        Enumeration keys = childScores.keys();
        while(keys.hasMoreElements()) {
            Long key = (Long)keys.nextElement();
            Float score = (Float)childScores.get(key);
            if (score.equals(ViewCmSkillGap.NOT_ATTEMPTED)) {
                result = true;
                break;
            }
        }
        return result;
    }
    
    private float deriveScore(Hashtable hEleScore, Vector vCompScore) {
        if(this.skill.skl_derive_rule==null) {
            this.score = UNKNOWN_DERIVE_RULE;
        } else {
            float[] childScore = formatChildScore2Float(hEleScore, vCompScore);
            if(childScore.length == 0) {
                this.score = NOT_ATTEMPTED;
            } else if(this.skill.skl_derive_rule.equals(DERIVE_RULE_MIN)) {
                this.score = childScore[0];
                for(int i=1; i<childScore.length; i++) {
                    if (this.score > childScore[i]) this.score = childScore[i];
                }
            } else if(this.skill.skl_derive_rule.equals(DERIVE_RULE_MAX)) {
                this.score = childScore[0];
                for(int i=1; i<childScore.length; i++) {
                    if (this.score < childScore[i]) this.score = childScore[i];
                }
            } else if(this.skill.skl_derive_rule.equals(DERIVE_RULE_AVG)) {
                this.score = 0;
                for(int i=0; i<childScore.length; i++) {
                    this.score+=childScore[i];
                }
                this.score = this.score/childScore.length;
            } else {
                this.score = UNKNOWN_DERIVE_RULE;
            }
        }
        return this.score;
    }
                    
    private float[] formatChildScore2Float(Hashtable hEleScore, Vector vCompScore) {
        
        float[] childScore = new float[hEleScore.size() + vCompScore.size()];
        int i = 0;
        Enumeration keys = hEleScore.keys();
        while(keys.hasMoreElements()) {
            Long key = (Long)keys.nextElement();
            Float eleScore = (Float)hEleScore.get(key);
            childScore[i] = eleScore.floatValue();
            i++;
        }
        for(int j=0; j<vCompScore.size(); j++) {
            childScore[i] = ((Float)vCompScore.elementAt(j)).floatValue();
            i++;
        }
        return childScore;
    }

    private static float averageScore(float[] score_list) {
        float sum=0;
        int size=0;
        for(int i=0;i<score_list.length; i++) {
            if(score_list[i] >= 0) {
                sum+=score_list[i];
                size++;
            }
        }
        if(size==0) 
            return NOT_ATTEMPTED;
        else 
            return sum/size;
    }
    
    public float getLevelFromMarkingScheme(float testScore, String markingSchemeXML)
        throws cwException {
        float result;
        
        result = Float.parseFloat(
                     cwXSL.processFromString(
                         getMarkingXML(testScore, markingSchemeXML)
                        ,getLevelFromMarkingSchemeXSL
                     )
                 );
        
        return result;
    }

    /**
    Vendor XML containing test score and marking scheme
    */
    private String getMarkingXML(float testScore, String markingSchemeXML) {
        StringBuffer xmlBuf = new StringBuffer(640);
        xmlBuf.append("<competency>").append(cwUtils.NEWL);
        xmlBuf.append("<test score=\"").append(testScore).append("\"/>").append(cwUtils.NEWL);
        xmlBuf.append(markingSchemeXML).append(cwUtils.NEWL);
        xmlBuf.append("</competency>").append(cwUtils.NEWL);
        return xmlBuf.toString();
    }
}
