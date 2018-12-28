package com.cw.wizbank.db.view;

import java.sql.*;
import java.util.Vector;
import java.util.Hashtable;

import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.dbEntity;
import com.cw.wizbank.qdb.dbUserGroup;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbException;

import com.cw.wizbank.util.*;

public class ViewCmSkillGap {

    public static final Float NOT_ATTEMPTED = new Float(-10);
    
    private long ent_id;
    private Timestamp reviewDate;
    private Hashtable scores;

    public long getEnt_id() {
        return this.ent_id;
    }
    public Hashtable getScores() {
        return this.scores;
    }
    public Timestamp getReviewDate() {
        return this.reviewDate;
    }
    
    private final static String RESOLVED        =   "RESOLVED";
    private final static String DELETED         =   "DELETED";

    private static final String SQL_GET_USER_LEVEL = 
            " SELECT "
          + " p_cmAssessment.asm_ent_id, "
          + " p_cmSkillsetCoverage.ssc_skb_id, "
          + " p_cmSkillsetCoverage.ssc_level "
          + " FROM "
          + " cmAssessment p_cmAssessment, "
          + " cmAssessmentUnit p_cmAssessmentUnit, "
          + " cmSkillsetCoverage p_cmSkillsetCoverage, "
          + " cmSkillSet p_cmSkillSet "
          + " WHERE p_cmAssessment.asm_ent_id = ? "
          + " AND p_cmAssessmentUnit.asu_asm_id = p_cmAssessment.asm_id "
          + " AND p_cmAssessment.asm_status != ? "
          + " AND p_cmAssessmentUnit.asu_type = ? "
          + " AND p_cmSkillsetCoverage.ssc_sks_skb_id = p_cmAssessmentUnit.asu_sks_skb_id "          
          + " AND p_cmSkillSet.sks_skb_id = p_cmAssessmentUnit.asu_sks_skb_id ";

    private static final String SQL_GET_USER_LEVEL_MAX_TS =           
            " AND p_cmSkillSet.sks_update_timestamp = ("
          + " SELECT "
          + " max(c_cmSkillSet.sks_update_timestamp) "
          + " FROM "
          + " cmAssessment c_cmAssessment, "
          + " cmAssessmentUnit c_cmAssessmentUnit, "
          + " cmSkillsetCoverage c_cmSkillsetCoverage, "
          + " cmSkillSet c_cmSkillSet "
          + " WHERE c_cmAssessment.asm_ent_id = p_cmAssessment.asm_ent_id "
          + " AND c_cmAssessmentUnit.asu_asm_id = c_cmAssessment.asm_id "
          + " AND c_cmAssessment.asm_status != ? "
          + " AND c_cmAssessmentUnit.asu_type = ? "
          + " AND c_cmSkillsetCoverage.ssc_sks_skb_id = c_cmAssessmentUnit.asu_sks_skb_id "          
          + " AND c_cmSkillSet.sks_skb_id = c_cmAssessmentUnit.asu_sks_skb_id "
          + " AND c_cmSkillsetcoverage.ssc_skb_id = p_cmSkillsetcoverage.ssc_skb_id "
          + " AND c_cmSkillSet.sks_update_timestamp <= ? "
          +")";
    
    private static final String SQL_GET_BY_USER =   
            " SELECT asm_ent_id, ssc_skb_id, ssc_level "
          + " FROM cmAssessment, cmAssessmentUnit, cmSkillsetCoverage, cmSkillSet, RegUser "
          + " WHERE asm_ent_id = usr_ent_id "
          + " AND asu_asm_id = asm_id "
          + " AND asm_status != ? "
          + " AND asu_type = ? "
          + " AND ssc_sks_skb_id = asu_sks_skb_id "          
          + " AND sks_skb_id = asu_sks_skb_id "
          + " AND sks_update_timestamp <= ? "
          + " AND usr_ent_id = ? ";




    private static final String SQL_GET_BY_GROUP =   
            " SELECT asm_ent_id, ssc_skb_id, ssc_level "
          + " FROM cmAssessment, cmAssessmentUnit, cmSkillsetCoverage, "
          + "      RegUser, EntityRelation, cmSkillSet "
          + " WHERE "
          + " asm_ent_id = usr_ent_id "
          + " AND asu_asm_id = asm_id "
          + " AND asm_status != ? "
          + " AND asu_type = ? "
          + " AND ssc_sks_skb_id = asu_sks_skb_id "
          + " AND sks_skb_id = asu_sks_skb_id "
          + " AND sks_update_timestamp <= ? "          
          + " AND usr_ent_id = ern_child_ent_id "
          + " AND ern_parent_ind = ? "
          + " AND ern_ancestor_ent_id IN ";

    /**
    Get User Skill Level 
    */
    public Hashtable getUserSkillLevel(Connection con, long ent_id, Timestamp reviewDate, Vector skbIdVec) 
        throws SQLException, cwException    {

        this.scores = new Hashtable();
        this.ent_id = ent_id;
        this.reviewDate = reviewDate;
        PreparedStatement stmt = null;
        try {
            ResultSet rs = null;
            StringBuffer SQLBuf  = new StringBuffer();
                
            SQLBuf.append(SQL_GET_USER_LEVEL);
            if( skbIdVec != null && skbIdVec.size() > 0 )
                SQLBuf.append(" AND p_cmSkillsetCoverage.ssc_skb_id IN ").append(cwUtils.vector2list(skbIdVec));
            SQLBuf.append(SQL_GET_USER_LEVEL_MAX_TS);
            stmt = con.prepareStatement(SQLBuf.toString());
            stmt.setLong(1, this.ent_id);
            stmt.setString(2, DELETED);
            stmt.setString(3, RESOLVED);
            stmt.setString(4, DELETED);
            stmt.setString(5, RESOLVED);
            stmt.setTimestamp(6, this.reviewDate);
            rs = stmt.executeQuery();
            long skill_id;
            float level;
            while (rs.next()) {
                skill_id = rs.getLong("ssc_skb_id");
                level = rs.getFloat("ssc_level");
                this.scores.put(new Long(skill_id), new Float(level));
            }
        } finally {
            if(stmt!=null) stmt.close();
        }
        return fillScore(skbIdVec);
    }

    /**
    @param vSrcSkill Vector of Long objects of skill id
    @param vScore Vector of ViewCmSkillGap of scored level of this.ent_id
    */
    private Hashtable fillScore(Vector vSrcSkill) {
        if(this.scores==null) this.scores = new Hashtable();
        Long skillId;
        for(int i=0; i<vSrcSkill.size(); i++) {
            skillId = (Long)vSrcSkill.elementAt(i);
            if(!this.scores.containsKey(skillId)) {
                this.scores.put(skillId, NOT_ATTEMPTED);
            }
        }
        return this.scores;
    }

    
    /**
    Get the set of skills that a entity archieved before the end_date
    @param ent_id entity id of the user / usergroup
    @param end_date the last date of the skill archieved
    @return rs ResultSet that contains all the skill
    */
    public static ResultSet getEntitySkills(Connection con, long ent_id, Timestamp end_date, Vector skbIdVec) 
        throws SQLException, cwException    {
    
        try {            

            dbEntity ent = new dbEntity();
            ent.ent_id = ent_id;
            ent.getById(con);
                
            PreparedStatement stmt = null;
            ResultSet rs = null;
            StringBuffer SQLBuf  = new StringBuffer();
            
            if (ent.ent_type.equalsIgnoreCase(dbEntity.ENT_TYPE_USER)) {                
                
                SQLBuf.append(SQL_GET_BY_USER);
                if( skbIdVec != null && skbIdVec.size() > 0 )
                    SQLBuf.append(" AND ssc_skb_id IN ").append(cwUtils.vector2list(skbIdVec));
                SQLBuf.append(" ORDER BY sks_update_timestamp DESC " );

                stmt = con.prepareStatement(SQLBuf.toString());
                stmt.setString(1, DELETED);
                stmt.setString(2, RESOLVED);
                stmt.setTimestamp(3, end_date);
                stmt.setLong(4, ent_id);
                
            }else if (ent.ent_type.equalsIgnoreCase(dbEntity.ENT_TYPE_USER_GROUP)) {
                
                Vector allgroups = dbUserGroup.getChildGroupVec(con, ent_id);
                allgroups.addElement(new Long(ent_id));
                SQLBuf.append(SQL_GET_BY_GROUP)
                    .append(cwUtils.vector2list(allgroups))
                    .append(" ORDER BY asm_ent_id DESC, sks_update_timestamp DESC ");
                stmt = con.prepareStatement(SQLBuf.toString());
                stmt.setString(1, DELETED);
                stmt.setString(2, RESOLVED);
                stmt.setTimestamp(3, end_date);
                stmt.setBoolean(4, true);
                
            }else {
                throw new cwException("Invalid entity id : " + ent_id);
            }
            
            rs = stmt.executeQuery();
            
            return rs;
        }catch (qdbException e) {
            throw new cwException(e.getMessage());
        }
    }

    

    
    /**
    Get the user who containing the skills specified
    @param Vector of user entity id
    */
    public static Vector searchUserBySkills(Connection con, Vector skillIdVec, loginProfile prof, WizbiniLoader wizbini)
        throws SQLException {
            
            String SQL_SEARCH_USER = " SELECT asm_ent_id FROM "
                                   + " ( SELECT DISTINCT asm_ent_id, ssc_skb_id FROM "
                                   + "   cmAssessment, cmAssessmentUnit, cmSkillSetCoverage ";
            if(wizbini.cfgTcEnabled){
            	SQL_SEARCH_USER +=" ,entityrelation,tcTrainingCenterTargetEntity,tcTrainingCenterOfficer ";
            }
            
            SQL_SEARCH_USER += "   WHERE asm_id = asu_asm_id AND asu_type = ? "
                                   + "   AND asm_status != ?  AND ssc_sks_skb_id = asu_sks_skb_id "
                                   + "   AND ssc_skb_id IN "
                                   +     cwUtils.vector2list(skillIdVec);
            if(wizbini.cfgTcEnabled){
            	 SQL_SEARCH_USER +=" and ern_ancestor_ent_id= tce_ent_id" +
					     			" and ern_type = 'USR_PARENT_USG'" +
					    			" and tco_usr_ent_id=?" +
					    			" and tce_tcr_id = tco_tcr_id" +
					    			" and ern_child_ent_id = asm_ent_id";	
            }                                 
            SQL_SEARCH_USER += " ) USERTEMP ";           
            SQL_SEARCH_USER +=" GROUP BY asm_ent_id "
                                   + " HAVING COUNT(asm_ent_id) >= ? ";
                                   
            PreparedStatement stmt = con.prepareStatement(SQL_SEARCH_USER);
            int index =1;
            stmt.setString(index++, RESOLVED);
            stmt.setString(index++, DELETED);
            if(wizbini.cfgTcEnabled){
            	stmt.setLong(index++, prof.usr_ent_id);
            }
            stmt.setLong(index++, skillIdVec.size());
            ResultSet rs = stmt.executeQuery();
            Vector entIdVec = new Vector();
            while( rs.next() )
                entIdVec.addElement(new Long(rs.getLong("asm_ent_id")));
            
            stmt.close();
            return entIdVec;
        }  
}
