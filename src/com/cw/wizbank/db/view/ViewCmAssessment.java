package com.cw.wizbank.db.view;

import java.sql.*;
import java.util.Vector;
import java.util.Hashtable;

import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.util.*;
import com.cw.wizbank.JsonMod.user.bean.SkillEvaluationBean;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.db.DbCmSkillSet;
import com.cw.wizbank.db.sql.OuterJoinSqlStatements;

/**
A class to deal with competency assessment
*/
public class ViewCmAssessment {
    
    private static final String ENT_TYPE_USERGROUP  =   "USG";
    private static final String COMMA               =   ",";
    private static final String RESOLVED            =   "RESOLVED";
    private static final String DELETED             =   "DELETED";
    private static final String ASSESSMENT_RESULT   =   "ASR";

    private static final String SQL_GET_ASSESSED_USER = " select distinct(asm_ent_id) "
                                                      + " from cmAssessment, RegUser "
                                                      + " where asm_status = ? "
                                                      + " and usr_ent_id = asm_ent_id "
                                                      + " and usr_ste_ent_id = ? ";

    private static final String SQL_CHECK_SCALE_IN_ASR = " SELECT count (*) FROM "
                                                       + " cmSkillBase, cmSkill, cmSkillSetCoverage, cmSkillSet, cmAssessmentUnit, cmAssessment "
                                                       + " WHERE skb_ssl_id = ? " 
                                                       + " AND skb_id = skl_skb_id "
                                                       + " AND skl_skb_id = ssc_skb_id "
                                                       + " AND ssc_sks_skb_id = sks_skb_id "
                                                       + " AND sks_type = ? "
                                                       + " AND sks_skb_id = asu_sks_skb_id "
                                                       + " AND asu_asm_id = asm_id "
                                                       + " AND asm_status <> ? ";
                                                       
    private static String SQL_CHECK_SKILL_OR_SUCCESSOR_IN_USE = 
    " SELECT count(*) " +
    " FROM cmSkillSetCoverage, cmSkillSet , cmSkillBase, cmSkill, cmAssessment " + 
    " WHERE sks_skb_id = ssc_sks_skb_id " + 
    " AND sks_type = ? " + 
    " AND skb_id = skl_skb_id " +
    " AND skl_skb_id = ssc_skb_id " +
    " AND sks_skb_id = asm_sks_skb_id " +
    " AND asm_status <> ? " +
    " AND skb_delete_timestamp IS NULL AND (skb_ancestor LIKE ? or skb_id = ? )";

    /**
    class variables
    */
    

    /**
    Check the rator start to do the assessment
    @return boolean         
    */
    public static boolean ratorStarted(Connection con, long asm_id)
        throws SQLException, cwException {
            
            StringBuffer SQL = new StringBuffer();
            SQL.append(" SELECT COUNT(*) FROM cmAssessmentUnit ")
               .append(" WHERE asu_asm_id = ? ")
               .append(" AND   asu_sks_skb_id IS NOT NULL ");
            
            PreparedStatement stmt = con.prepareStatement(SQL.toString());
            stmt.setLong(1, asm_id);
            ResultSet rs = stmt.executeQuery();
            boolean flag = false;
            if( rs.next() ) {
                if( rs.getLong(1) > 0 )
                    flag = true;
            } else 
                throw new cwException("Failed to check the assessment, id = " + asm_id );
                
            stmt.close();
            return flag;            
        }




    /**
    Get assessment list result set
    @param boolean prepeared ( show prepared assessment )
    @param boolean collected ( show collected assessment )    
    @param boolean notified ( show notified assessment )
    @param boolean resolved ( show resolved assessment )
    @return result of the require filed
    */
    public ResultSet assList(Connection con, boolean prepared, boolean collected, boolean notified, boolean resolved, String order_by, String sort_by, Timestamp curTime, long root_ent_id,boolean hasTc,String tcr_id_lst)
        throws SQLException {
            
            StringBuffer SQL = new StringBuffer();
			SQL.append(OuterJoinSqlStatements.viewCMAssessmentAssList(hasTc, tcr_id_lst));
//			  SQL.append(" SELECT asm_id, COUNT(asu_asm_id) AS total, usr_display_bil, asm_status, ")
//				 .append(" asm_title, asm_eff_start_datetime, asm_eff_end_datetime ")
//				 .append(" FROM RegUser, cmAssessment, cmAssessmentUnit, cmSkillSet ")
//				 .append(" WHERE asu_asm_id ").append(cwSQL.get_right_join(con)).append(" asm_id ")
//				 .append(" AND asm_ent_id = usr_ent_id ")
//				 .append(" AND asm_sks_skb_id = sks_skb_id ")
//				 .append(" AND asu_type <> ? ")
//				 .append(" AND asm_status <> ? ")
//				 .append(" AND sks_owner_ent_id = ? ");
                        
            if( prepared || collected || notified || resolved )
                SQL.append(" AND ( " );
            
            if( prepared )
                SQL.append(" asm_eff_start_datetime > ? ");
            
            if( notified ) {
                if( prepared )
                    SQL.append(" OR ");
                SQL.append(" ( asm_eff_start_datetime < ? AND asm_eff_end_datetime > ? ) ");
            }
            
            if( collected ) {
                if( prepared || notified)
                    SQL.append(" OR ");
                SQL.append(" asm_eff_end_datetime < ? ");
            }
            
            if( resolved ) {
                if( prepared || notified || collected )
                    SQL.append(" OR " );
                SQL.append(" asm_status = ? ");
            }
            
            if( prepared || collected || notified || resolved )
                SQL.append(" ) " );                                            
            
            if( !resolved )
                SQL.append(" AND ( asm_status != ? )");

            
            SQL.append(" GROUP BY asm_id, usr_display_bil, asm_title, asm_status, ")
               .append(" asm_eff_start_datetime, asm_eff_end_datetime ")
               .append(" ORDER BY " ).append(order_by).append("  ").append(sort_by);
            
            
            int index = 1;
            PreparedStatement stmt = con.prepareStatement(SQL.toString());
            stmt.setString(index++, "RESOLVED");
            stmt.setString(index++, DELETED);
            stmt.setLong(index++, root_ent_id);
            if( prepared )
                stmt.setTimestamp(index++, curTime);
            if( notified ) {
                stmt.setTimestamp(index++, curTime);
                stmt.setTimestamp(index++, curTime);
            }
            if( collected )
                stmt.setTimestamp(index++, curTime);
            //if( resolved )
            stmt.setString(index++, RESOLVED);
                

            ResultSet rs = stmt.executeQuery();
            return rs;
            
        }
        
        
        
    /**
    Get the assessment list within the specified assessment id
    */
    
    public ResultSet assList(Connection con, long[] ids, String order_by, String sort_by, long root_ent_id,boolean hasTc,String tcr_id_lst)
        throws SQLException {
            
            StringBuffer idStr = new StringBuffer().append("(0");
            for(int i=0; i<ids.length; i++)
                idStr.append(COMMA).append(ids[i]);
            idStr.append(")");    
            
            StringBuffer SQL = new StringBuffer();
			SQL.append(OuterJoinSqlStatements.viewCMAssessmentAssList2(hasTc, tcr_id_lst));
//			  SQL.append(" SELECT asm_id, COUNT(asu_asm_id) AS total, usr_display_bil, asm_status, ")
//				 .append(" asm_title, asm_eff_start_datetime, asm_eff_end_datetime ")
//				 .append(" FROM RegUser, cmAssessment, cmAssessmentUnit, cmSkillSet ")
//				 .append(" WHERE asu_asm_id ").append(cwSQL.get_right_join(con)).append(" asm_id ")
//				 .append(" AND asm_ent_id = usr_ent_id ")
//				 .append(" AND asm_sks_skb_id = sks_skb_id ")
//				 .append(" AND sks_owner_ent_id = ? ")
//				 .append(" AND asu_type <> ? ")
			SQL.append(" AND asm_id IN ")
               .append(idStr.toString())
               .append(" GROUP BY asm_id, usr_display_bil, asm_title, asm_status, ")
               .append(" asm_eff_start_datetime, asm_eff_end_datetime ")
               .append(" ORDER BY " ).append(order_by).append("  ").append(sort_by);

            PreparedStatement stmt = con.prepareStatement(SQL.toString());
            stmt.setLong(1, root_ent_id);
            stmt.setString(2, "RESOLVED");
            ResultSet rs = stmt.executeQuery();
            return rs;
        }


    public ResultSet getAssessmentUnits(Connection con, long asm_id)
        throws SQLException {
            return getAssessmentUnits(con, asm_id, false);
    }


    /**
    Get the assessment unit of the specified assessment id
    @param long value of assessment id
    @return result set of the require field
    */
    public ResultSet getAssessmentUnits(Connection con, long asm_id, boolean getResolved)
        throws SQLException {
            
            StringBuffer SQL = new StringBuffer();
            SQL.append(" SELECT asu_asm_id, asu_ent_id, asu_weight, asu_sks_skb_id, ")
               .append("asu_type, asu_submit_ind, usr_display_bil, aua_eff_start_timestamp, sks_update_timestamp ")
               .append("FROM cmAssessmentUnit ")
               .append("INNER JOIN RegUser ON (asu_ent_id = usr_ent_id) ")
               .append(" Left JOIN cmAsmUnitTypeAttr ON (asu_asm_id = aua_asm_id AND asu_type = aua_asu_type) ")
               .append(" Left join cmSkillSet on (asu_sks_skb_id = sks_skb_id) ")
               .append("WHERE asu_asm_id = ? ");
            if(!getResolved) {   
               SQL.append(" AND asu_type <> ? ");
            }
            
            int idx = 1;
            PreparedStatement stmt = con.prepareStatement(SQL.toString());
            stmt.setLong(idx++, asm_id);
            if(!getResolved) {
                stmt.setString(idx++, RESOLVED);
            }
            ResultSet rs = stmt.executeQuery();
            return rs;            
        }

    

    /**
    Get assessment info of the specified assessee
    @param long of assessee entity id
    @return ResultSet
    */
    public static ResultSet getAssesseeInfo(Connection con, long ent_id)
        throws SQLException {
            
            StringBuffer SQL = new StringBuffer();
            SQL.append(" SELECT asm_id, asm_title, sks_update_timestamp ")
               .append(" FROM cmAssessment, cmAssessmentUnit, cmSkillSet ")
               .append(" WHERE asm_ent_id = ? " )
               .append(" AND   asm_id = asu_asm_id " )
               .append(" AND   asm_status != ? ")
               .append(" AND   asu_type = ? " )
               .append(" AND   asu_sks_skb_id = sks_skb_id " );               
            
            PreparedStatement stmt = con.prepareStatement(SQL.toString());
            stmt.setLong(1, ent_id);
            stmt.setString(2, DELETED);
            stmt.setString(3, RESOLVED);
            
            ResultSet rs = stmt.executeQuery();
            return rs;
            
        }


    /**
    Get the latest resolved date of the specified assessment
    @param long value of assessment id
    @return timestamp
    */
    public static Timestamp getResolvedDate(Connection con, long asm_id)
        throws SQLException {
            
            String SQL = " SELECT sks_update_timestamp "
                       + " FROM cmAssessment, cmAssessmentUnit, cmSkillSet "
                       + " WHERE asm_id = ? "
                       + " AND   asu_asm_id = asm_id "
                       + " AND   asu_type = ? "
                       + " AND   sks_skb_id = asu_sks_skb_id "
                       + " AND   asm_status != ? ";
            
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, asm_id);
            stmt.setString(2, RESOLVED);
            stmt.setString(3, DELETED);
            ResultSet rs = stmt.executeQuery();
            Timestamp resolved_date = null;
            if( rs.next() )
                resolved_date = rs.getTimestamp("sks_update_timestamp");
                
            stmt.close();
            return resolved_date;            
        }

    /**
    Get the assessment info which assigned to the specified user
    @param long value of user entity id
    @return result set
    */
    public ResultSet getAssignedAss(Connection con, long usr_ent_id)
        throws SQLException {
        
        return getAssignedAss(con, usr_ent_id, false);
    }
    
    /**
    Get the assessment info which assigned to the specified user
    @param long value of user entity id
    @param withResolved the result set will also contain assessments assigned as resolver if withResolved = true
           else will only contain assessments assigned as assessors
    @return result set
    */
    public ResultSet getAssignedAss(Connection con, long usr_ent_id, boolean withResolved)
        throws SQLException {
            
            Timestamp curTime = cwSQL.getTime(con);
            
            StringBuffer SQL = new StringBuffer();
            SQL.append(" SELECT asm_id, asm_title, asm_type, asm_mod_res_id, asu_sks_skb_id ")
               .append(" ,aua_eff_end_timestamp as asm_eff_end_datetime, asu_type ")
               .append(" ,usr_display_bil ")
               .append(" FROM cmAssessment, cmAssessmentUnit, cmAsmUnitTypeAttr, RegUser ")
               .append(" WHERE asm_id = asu_asm_id ")
               .append(" AND asm_id = aua_asm_id ")
               .append(" AND asu_type = aua_asu_type ")
               .append(" AND asm_status <> ? ")
               .append(" AND asu_ent_id = ? ")
               .append(" AND aua_eff_start_timestamp <= ? ")
               .append(" AND aua_eff_end_timestamp >= ? ")
               .append(" AND asu_submit_ind = ? ")
               .append(" AND asm_ent_id = usr_ent_id ");
            if(!withResolved) {
                SQL.append(" AND asu_type <> ? " );
            }
            SQL.append(" ORDER BY aua_eff_end_timestamp ASC ");
               
            PreparedStatement stmt = con.prepareStatement(SQL.toString());
            stmt.setString(1, DELETED);
            stmt.setLong(2, usr_ent_id);            
            stmt.setTimestamp(3, curTime);
            stmt.setTimestamp(4, curTime);
            stmt.setBoolean(5, false);
            if(!withResolved) {
                stmt.setString(6, RESOLVED);
            }
            ResultSet rs = stmt.executeQuery();
            return rs;            
        }
    
    private static String getMySkillEvalSql(String conditionSql) {
    	String sql = " SELECT asm_id, asm_title, asm_type, asm_mod_res_id, asu_sks_skb_id," +
    			" aua_eff_end_timestamp as asm_eff_end_datetime, asu_type, usr_display_bil" +
    			" FROM cmAssessment, cmAssessmentUnit, cmAsmUnitTypeAttr, RegUser" +
    			" WHERE asm_id = asu_asm_id AND asm_id = aua_asm_id AND asu_type = aua_asu_type" +
    			" AND asm_status <> ? AND asu_ent_id = ? AND aua_eff_start_timestamp <= ? AND aua_eff_end_timestamp >= ?" +
    			" AND asu_submit_ind = ? AND asm_ent_id = usr_ent_id";
    	if(conditionSql != null) {
    		sql += " " + conditionSql;
    	}
    	sql += " ORDER BY aua_eff_end_timestamp ASC";
    	return sql;
    }

    public static Vector getMySkillEvaluation(Connection con, long usr_ent_id, boolean withResolved) throws SQLException {
    	Vector skillEvalVec = null;
    	Timestamp curTime = cwSQL.getTime(con);
		String conditionSql = "";
		if(!withResolved) {
			conditionSql += "AND asu_type <> ? ";
		}
    	
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(getMySkillEvalSql(conditionSql));
			int index = 1;
			stmt.setString(index++, DELETED);
            stmt.setLong(index++, usr_ent_id);            
            stmt.setTimestamp(index++, curTime);
            stmt.setTimestamp(index++, curTime);
            stmt.setBoolean(index++, false);
            if(!withResolved) {
                stmt.setString(index++, RESOLVED);
            }
            rs = stmt.executeQuery();
            while(rs.next()) {
            	if(skillEvalVec == null) {
            		skillEvalVec = new Vector();
            	}
            	SkillEvaluationBean skillEvalBean = new SkillEvaluationBean();
            	skillEvalBean.setAsm_id(rs.getLong("asm_id"));
            	skillEvalBean.setAsm_title(rs.getString("asm_title"));
            	skillEvalBean.setAsm_type(rs.getString("asm_type"));
            	skillEvalBean.setAsu_type(rs.getString("asu_type"));
            	skillEvalBean.setAsm_eff_end_datetime(rs.getTimestamp("asm_eff_end_datetime"));
            	skillEvalBean.setAssessee(rs.getString("usr_display_bil"));
            	skillEvalBean.setAsu_sks_skb_id(rs.getLong("asu_sks_skb_id"));
            	skillEvalVec.addElement(skillEvalBean);
            }
			
		} finally {
			if(stmt != null) {
				stmt.close();
			}
		}
        
		return skillEvalVec;
	}
    

    /**
    Check if a scale is used in any skill which in any Assessment result
    @return boolean
    */
    public static boolean isScaleInASR(Connection con, long scale_id)
        throws SQLException, cwException {
        
        PreparedStatement stmt = null;
        boolean isUsed = false;
        try {
            stmt = con.prepareStatement(SQL_CHECK_SCALE_IN_ASR);
            stmt.setLong(1, scale_id);
            stmt.setString(2, ASSESSMENT_RESULT);
            stmt.setString(3, DELETED);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                if (rs.getInt(1) > 0) {
                    isUsed = true;
                }
            }else {
                throw new cwException ("Failed to check if a scale is used.");
            }
        } finally {
            if(stmt!=null) stmt.close();
        }    
        return isUsed;
    }


    /**
    * Find all messages related to the assessment
    @return hastable - message id as a key, long array containing xsltempate id as a value
    */
    public static Hashtable getMsgTemplateId(Connection con, long asm_id)
        throws SQLException, cwException{
            
            String SQL = " SELECT asn_msg_id, mst_xtp_id "
                       + " FROM cmAssessmentNotify, xslMgSelectedTemplate "
                       + " WHERE asn_msg_id = mst_msg_id "
                       + " AND asn_asm_id = ? ";
                       //+ " ORDER BY asn_msg_id ";
            
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, asm_id);
            Hashtable msgXtpTable = new Hashtable();
            Vector xtpIdVec = null;
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                Long msgId = new Long(rs.getLong("asn_msg_id"));
                if( msgXtpTable.contains(msgId) )
                    xtpIdVec = (Vector)msgXtpTable.get(msgId);
                else 
                    xtpIdVec = new Vector();
                
                xtpIdVec.addElement(new Long(rs.getLong("mst_xtp_id")));
                msgXtpTable.put(msgId, xtpIdVec);                
            }
            stmt.close();
            return msgXtpTable;
        }

    /**
    * Find all messages related to the one of assessment's assessment unit
    @return Hastable - message id as a key, long array containing xsltempate id as a value
    */
    public static Hashtable getMsgTemplateId(Connection con, long asm_id, String asu_type)
        throws SQLException, cwException{
            
            final String SQL = " SELECT asn_msg_id, mst_xtp_id "
                             + " FROM cmAssessmentNotify, xslMgSelectedTemplate "
                             + " WHERE asn_msg_id = mst_msg_id "
                             + " AND asn_asm_id = ? "
                             + " AND asn_asu_type = ? ";
            
            PreparedStatement stmt = con.prepareStatement(SQL);
            Hashtable msgXtpTable = new Hashtable();
            try {
                stmt.setLong(1, asm_id);
                stmt.setString(2, asu_type);
                Vector xtpIdVec = null;
                ResultSet rs = stmt.executeQuery();
                while(rs.next()){
                    Long msgId = new Long(rs.getLong("asn_msg_id"));
                    if( msgXtpTable.contains(msgId) )
                        xtpIdVec = (Vector)msgXtpTable.get(msgId);
                    else 
                        xtpIdVec = new Vector();
                    
                    xtpIdVec.addElement(new Long(rs.getLong("mst_xtp_id")));
                    msgXtpTable.put(msgId, xtpIdVec);                
                }
            } finally {
                if(stmt!=null) stmt.close();
            }
            return msgXtpTable;
        }
        
    /*
        return true if there exist a successor (or itself) that is active and consist in an active assessmentResult
    */
    public static boolean checkSkillSuccessorInASY(Connection con, long skill_id) throws SQLException, cwException{
        boolean isUsed = false;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(SQL_CHECK_SKILL_OR_SUCCESSOR_IN_USE);
        stmt.setString(1, DbCmSkillSet.ASSESSMENT_SURVEY);
        stmt.setString(2 ,DELETED);
        stmt.setString(3, "% " + skill_id + " %");
        stmt.setLong(4, skill_id);
        
            rs = stmt.executeQuery();
        if (rs.next()) {
            if (rs.getInt(1) > 0) {
                isUsed = true;
            }
        }else {
            throw new cwException ("Failed to check the skill (and its successor) is in used.");
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        return isUsed; 
    }



    public static long numberOfNotify(Connection con, long asm_id, String mg_xtp_type, String mgh_status)
        throws SQLException{
            
            String SQL = " SELECT COUNT(mgh_rec_id) "
                       + " FROM cmAssessmentNotify, xslTemplate, mgRecHistory "
                       + " WHERE asn_asm_id = ? "                       
                       + " AND asn_msg_id = mgh_mst_msg_id "
                       + " AND mgh_mst_xtp_id = xtp_id "
                       + " AND xtp_type = ? "
                       + " AND mgh_status = ? ";
            
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, asm_id);
            stmt.setString(2, "ASSESSMENT_NOTIFICATION");
            stmt.setString(3, mgh_status);
            ResultSet rs = stmt.executeQuery();
            long count = 0;
            if(rs.next())
                count = rs.getLong(1);
            stmt.close();
            return count;
        }
        
    private static final String SQL_GET_RESOLVED_SKS_UPD_TIMESTAMP = 
        " select sks_update_timestamp from cmAssessment, cmAssessmentUnit, cmSkillSet " +
        " where asm_id = asu_asm_id " +
        " and asu_sks_skb_id = sks_skb_id " +
        " and asu_type = ? " +
        " and asm_id = ? ";
    
    public static Timestamp getResolvedSkillSetTimestamp(Connection con, long asm_id) throws SQLException {
        PreparedStatement stmt = null;
        Timestamp time = null;
        try {
            stmt = con.prepareStatement(SQL_GET_RESOLVED_SKS_UPD_TIMESTAMP);
            stmt.setString(1, RESOLVED);
            stmt.setLong(2, asm_id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                time = rs.getTimestamp("sks_update_timestamp");
            }
        } finally {
            if(stmt!=null) stmt.close();
        }
        return time;
    }

    /**
    Get the user entity id of users who have assessment data of an organization
    */
    public static Vector searchUserHasAssData(Connection con, long owner_ent_id, loginProfile prof, WizbiniLoader wizbini) throws SQLException {
        String sql = " select distinct(asm_ent_id) "
            + " from cmAssessment, RegUser ";
        if(wizbini.cfgTcEnabled){
    		sql+=" ,entityrelation,tcTrainingCenterTargetEntity,tcTrainingCenterOfficer ";		
    	}   
        sql+= " where asm_status = ? "
            + " and usr_ent_id = asm_ent_id "
            + " and usr_ste_ent_id = ? ";
        if(wizbini.cfgTcEnabled){
        	sql+=" and ern_ancestor_ent_id= tce_ent_id" +
			" and ern_type = 'USR_PARENT_USG'" +
			" and tco_usr_ent_id=?" +
			" and tce_tcr_id = tco_tcr_id" +
			" and ern_child_ent_id = usr_ent_id";	
        }
    	PreparedStatement stmt = null;
        Vector vResult = new Vector();
        try {
            stmt = con.prepareStatement(sql);
            stmt.setString(1, RESOLVED);
            stmt.setLong(2, owner_ent_id);
            if(wizbini.cfgTcEnabled){
        		stmt.setLong(3, prof.usr_ent_id);
        	}
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                vResult.addElement(new Long(rs.getLong(1)));
            }
        } finally {
            if(stmt!=null) stmt.close();
        }
        return vResult;
    }

    private static final String SQL_GET_RESOLVED_USR_N_TIME = 
        " select usr_display_bil, sks_update_timestamp " +
        " from RegUser, cmSkillSet, cmAssessmentUnit " +
        " where asu_asm_id = ? " +
        " and asu_type = ? " +
        " and asu_sks_skb_id = sks_skb_id " +
        " and sks_update_usr_id = usr_id ";
    /**
    Get the last resolved user display bil and timestamp of the input assessment.
    If the assessment has not been resolved, return an empty Vector.
    @param con Connection to database
    @param asm_id Assessment id
    @return Vector contains last resolved user display bil at element 0, and timestamp at element 1.
    */
    public static Vector getResolvedUserNTime(Connection con, long asm_id) throws SQLException {
        PreparedStatement stmt = null;
        Vector v = new Vector();
        try {
            stmt = con.prepareStatement(SQL_GET_RESOLVED_USR_N_TIME);
            stmt.setLong(1, asm_id);
            stmt.setString(2, RESOLVED);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                v.addElement(rs.getString("usr_display_bil"));
                v.addElement(rs.getTimestamp("sks_update_timestamp"));
            }
        } finally {
            if(stmt!=null) stmt.close();
        }
        return v;
    }

}
