package com.cw.wizbank.qdb;

import java.sql.*;
import java.util.Vector;
import java.util.Hashtable;
import com.cw.wizbank.util.*;
import com.cw.wizbank.config.WizbiniLoader;

public class dbModuleEvaluationHistory
{
    public long mvh_ent_id;
    public long mvh_mod_id;
    public Timestamp mvh_last_acc_datetime;
    public long mvh_tkh_id;
    public String mvh_ele_loc;
    public long mvh_total_attempt = -1;
    public float mvh_total_time;
    public String mvh_status;
    public String mvh_score;
    public String mvh_create_usr_id;
    public Timestamp mvh_create_timestamp;

    public void ins(Connection con) throws qdbException {
        try {
            String SQL = " INSERT INTO ModuleEvaluationHistory "
                    + " (mvh_ent_id, mvh_mod_id, mvh_last_acc_datetime, mvh_tkh_id, "
                    + "  mvh_ele_loc, mvh_total_attempt, mvh_total_time, mvh_status, mvh_score, "
                    + "  mvh_create_usr_id, mvh_create_timestamp)"
                    + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement stmt = con.prepareStatement(SQL);
            int index = 1;
            stmt.setLong(index++, this.mvh_ent_id);
            stmt.setLong(index++, this.mvh_mod_id);
            stmt.setTimestamp(index++, this.mvh_last_acc_datetime);
            stmt.setLong(index++, this.mvh_tkh_id);
            stmt.setString(index++, this.mvh_ele_loc);
            if (mvh_total_attempt != -1) {
                stmt.setLong(index++, mvh_total_attempt);
            }
            else {
                stmt.setLong(index++, 1);
            }
            stmt.setFloat(index++, this.mvh_total_time);
            stmt.setString(index++, this.mvh_status);
            if (this.mvh_score == null){
                stmt.setNull(index++, java.sql.Types.VARCHAR);
            }else{
                int curIdx = index++;
                try {
                    stmt.setFloat(curIdx, Float.parseFloat(this.mvh_score));
                } catch (NumberFormatException e) {
                    stmt.setNull(curIdx, java.sql.Types.VARCHAR);
                }
            }
            stmt.setString(index++, this.mvh_create_usr_id);
            stmt.setTimestamp(index++, this.mvh_create_timestamp);

            int stmtResult=stmt.executeUpdate();
            stmt.close();
            if ( stmtResult!=1)
            {
                con.rollback();
                throw new qdbException("Failed to insert module evaluation history.");
            }
            
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }
    
    public static String getModEvalHistAsXML(Connection con, long cosId, String tkhType, Timestamp startTimestamp, Timestamp endTimestamp, WizbiniLoader wizbini) throws qdbException {
        try {
            String xml = new String();
            String SQL = " SELECT res_title, res_subtype, usr_display_bil, mvh_ent_id, mvh_mod_id, "
                       + " mvh_last_acc_datetime, mvh_tkh_id, mvh_ele_loc, mvh_total_attempt, mvh_total_time, "
                       + " mvh_status, mvh_score, mvh_create_usr_id, mvh_create_timestamp, ste_name,ern_ancestor_ent_id "
                       + " FROM moduleEvaluationHistory, resources, trackingHistory, regUser, EntityRelation, acSite "
                       + " WHERE mvh_mod_id = res_id "
                       + " AND mvh_tkh_id = tkh_id "
                       + " AND usr_ent_id = mvh_ent_id "
                       + " AND usr_ent_id = ern_child_ent_id "
                       + " AND ern_type = ? "
                       + " AND ern_parent_ind = ? "
                       + " AND ste_ent_id = usr_ste_ent_id "
                       + " AND mvh_mod_id IN "
                       + " ( SELECT mov_mod_id FROM moduleEvaluation WHERE mov_cos_id = ? ) "
                       + " AND tkh_type = ? ";
            if(startTimestamp != null) {
                SQL += " AND mvh_create_timestamp > ? ";
            }
            if(endTimestamp != null) {
                SQL += " AND mvh_create_timestamp < ? ";
            }
            if(startTimestamp != null) {
                SQL += " AND mvh_last_acc_datetime > ? ";
            }
            if(endTimestamp != null) {
                SQL += " AND mvh_last_acc_datetime < ? ";
            }
            SQL += " ORDER BY usr_display_bil, res_title, mvh_last_acc_datetime, mvh_create_timestamp ";
            
            PreparedStatement stmt = con.prepareStatement(SQL);
            int index = 1;
            stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
            stmt.setBoolean(index++, true);
            stmt.setLong(index++, cosId);
            stmt.setString(index++, tkhType);
            if(startTimestamp != null) {
                stmt.setTimestamp(index++, startTimestamp);
            } 
            if(endTimestamp != null) {
                stmt.setTimestamp(index++, endTimestamp);
            }
            if(startTimestamp != null) {
                stmt.setTimestamp(index++, startTimestamp);
            } 
            if(endTimestamp != null) {
                stmt.setTimestamp(index++, endTimestamp);
            }
            ResultSet rs = stmt.executeQuery();
            xml = generateXML(con, rs, cosId, dbCourse.getCosTitle(con, cosId), wizbini);
            stmt.close();
            return xml;
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }
    
    static String maxScore = " select max(mvh_score) max_score from moduleEvaluationHistory where mvh_tkh_id = ? and mvh_mod_id = ? ";
    public static String getEverMaxScore(Connection con,long mod_id,long tkh_id){
    	PreparedStatement pst = null;
    	ResultSet rs = null;
        String max = null;
    	try{
    		pst = con.prepareStatement(maxScore);
    		pst.setLong(1,tkh_id);
    		pst.setLong(2,mod_id);
    		rs = pst.executeQuery();
    		if(rs.next()){
    			float f = rs.getFloat(1);
                if(rs.wasNull()) {
                    max = null;
                } else {
                    max = new Float(f).toString();
                }
    		}
    	}catch(SQLException e){
    		throw new RuntimeException("Server error: "+e.getMessage());
    	}finally{
    		cwSQL.cleanUp(rs,pst);
    	}
    	return max;
    }
  
    static String existHis = "select mvh_id from moduleEvaluationHistory where mvh_tkh_id = ? and mvh_mod_id = ? ";
    public static boolean existHis(Connection con,long mod_id,long tkh_id){
		PreparedStatement pst = null;
				ResultSet rs = null;
				try{
					pst = con.prepareStatement(existHis);
					pst.setLong(1,tkh_id);
					pst.setLong(2,mod_id);
					rs = pst.executeQuery();
					if(rs.next()){
						return true;
					}
				}catch(SQLException e){
					throw new RuntimeException("Server error: "+e.getMessage());
				}finally{
					cwSQL.cleanUp(rs,pst);
				}
				return false;
    }
    
    public static boolean existHis(Connection con, long mod_id){
    	String sql = " select mvh_id from moduleEvaluationHistory where mvh_mod_id = ? ";
		PreparedStatement pst = null;
				ResultSet rs = null;
				try{
					pst = con.prepareStatement(sql);
					pst.setLong(1,mod_id);
					rs = pst.executeQuery();
					if(rs.next()){
						return true;
					}
				}catch(SQLException e){
					throw new RuntimeException("Server error: "+e.getMessage());
				}finally{
					cwSQL.cleanUp(rs,pst);
				}
				return false;
    }
    
    static String hasStatus = "select mvh_status from moduleEvaluationHistory where mvh_tkh_id = ? and mvh_mod_id = ? and ( mvh_status = ? or mvh_status = ?)";
    public static String everStatus(Connection con,long mod_id,long tkh_id,String status1,String status2){
    	PreparedStatement pst = null;
    	ResultSet rs = null;
    	try{
    		pst = con.prepareStatement(hasStatus);
    		pst.setLong(1,tkh_id);
    		pst.setLong(2,mod_id);
    		pst.setString(3,status1);
    		pst.setString(4,status2);
    		rs = pst.executeQuery();
    		if(rs.next()){
    			return rs.getString(1);
    		}
    		
    	}catch(SQLException e){
    		throw new RuntimeException("Server error: "+e.getMessage());
    	}finally{
    		cwSQL.cleanUp(rs,pst);
    	}
    	return null;
    }

    private static String generateXML(Connection con, ResultSet rs, long cosId, String cosTitle, WizbiniLoader wizbini) throws qdbException {
        try {
            Vector vtReport = new Vector();
            Timestamp start_acc_datetime = null;
            float timeUsed = 0;
            long totalAttempt = 0;
            boolean bShowOrgName = wizbini.cfgSysSetupadv.getOrganization().isMultiple();
            String fullPathSeparator = wizbini.cfgSysSetupadv.getGroupMemberFullPathSeparator();
            StringBuffer xmlBuf = new StringBuffer(2048);
            xmlBuf.append("<module_evaluation_history course_id=\"" + cosId + "\" course_title=\"" + cwUtils.esc4XML(cosTitle) + "\">").append(dbUtils.NEWL);
            EntityFullPath entityfullpath = EntityFullPath.getInstance(con);
            // store the query result in a vector of hashtable first
            while (rs.next()) {
            	long ancestor_id = rs.getLong("ern_ancestor_ent_id");
                Hashtable htReport = new Hashtable();
                if (rs.getString("mvh_score")!= null){
                    htReport.put("mvh_score", new Float(rs.getFloat("mvh_score")));
                }
                else {
                    htReport.put("mvh_score", new Float(-1));
                }
                htReport.put("mvh_last_acc_datetime", rs.getTimestamp("mvh_last_acc_datetime"));
                htReport.put("mvh_total_time", new Float(rs.getFloat("mvh_total_time")));
                htReport.put("mvh_ent_id", new Long(rs.getLong("mvh_ent_id")));
                htReport.put("usr_display_bil", rs.getString("usr_display_bil"));
                htReport.put("usg_display_bil", entityfullpath.getEntityName(con, ancestor_id));
                htReport.put("mvh_mod_id", new Long(rs.getLong("mvh_mod_id")));
                htReport.put("res_subtype", rs.getString("res_subtype"));
                htReport.put("res_title", rs.getString("res_title"));               
                htReport.put("ste_name", rs.getString("ste_name"));
                htReport.put("gpm_full_path", entityfullpath.getFullPath(con, ancestor_id));
                if (rs.getString("mvh_status") != null) {
                    htReport.put("mvh_status", rs.getString("mvh_status"));
                }
                else {
                    htReport.put("mvh_status", "");
                }
                if (rs.getString("mvh_ele_loc") != null) {
                    htReport.put("mvh_ele_loc", rs.getString("mvh_ele_loc"));
                }
                else {
                    htReport.put("mvh_ele_loc", "");
                }
                htReport.put("mvh_total_attempt", new Long(rs.getLong("mvh_total_attempt")));
                
                vtReport.addElement(htReport);
            }
            
            for (int i=0; i<vtReport.size(); i++) {                
                Hashtable htReport = (Hashtable)vtReport.elementAt(i);
                Long longTotalAttempt = (Long)htReport.get("mvh_total_attempt");
                totalAttempt = longTotalAttempt.longValue();
                
                // find the fist record of a launch
                if (totalAttempt == 1 || i == 0) {
                    Float floatTimeUsed = (Float)htReport.get("mvh_total_time");
                    timeUsed = floatTimeUsed.floatValue();

                    Timestamp last_acc_datetime = (Timestamp)htReport.get("mvh_last_acc_datetime");
                    
                    // use the start datetime of the first record of the launch
                    start_acc_datetime = new Timestamp(last_acc_datetime.getTime() - (long) (Math.round(timeUsed)*1000));
                    
                    //test if "i" rearch the end of vtReport
                    //if true, set i = i - 1 so that
                    //we can enter the "j" for-loop
                    boolean dummy = false;
                    if(i == vtReport.size() - 1) {
                        i = i - 1;
                        dummy = true;
                    }
                    // find the last record of the launch
                    for (int j=i+1; j<vtReport.size(); j++) {
                        htReport = (Hashtable)vtReport.elementAt(j);
                        longTotalAttempt = (Long)htReport.get("mvh_total_attempt");
                        totalAttempt = longTotalAttempt.longValue();
                        
                        if (totalAttempt == 1 || j == vtReport.size()-1) {
                            // hit the first record of the next launch
                            if (totalAttempt == 1 && dummy == false) {
                                htReport = (Hashtable)vtReport.elementAt(j-1);
                                i = j-1;
                            }
                            // hit the last record
                            else {
                                htReport = (Hashtable)vtReport.elementAt(j);
                                //dummy == false means that this is the last record of
                                //vtReport. so no need to accumulate the time used
                                if(dummy == false) {
                                    floatTimeUsed = (Float)htReport.get("mvh_total_time");
                                    timeUsed += floatTimeUsed.floatValue();
                                }
                                i = j;
                            } 
                                
                            String score_s  =  null;
                            Float float_mvh_score = (Float)htReport.get("mvh_score");
                            float mvh_score = float_mvh_score.floatValue();
                            if (mvh_score >= 0){
                                score_s = dbModuleEvaluation.convertScore(mvh_score);
                            } else {
                            	String res_subtype = (String)htReport.get("res_subtype");
                            	if(dbAssignment.MOD_TYPE_ASS.equalsIgnoreCase(res_subtype)){                          		
                            		  if (mvh_score ==-101){
                            			  score_s="A+";
                            		  }else if(mvh_score ==-102){
                            			  score_s="A";
                            		  }else if(mvh_score ==-103){
                            			  score_s="A-";
                            		  }else if(mvh_score ==-104){
                            			  score_s="B+";
                            		  }else if(mvh_score ==-105){
                            			  score_s="B";
                            		  }else if(mvh_score ==-106){
                            			  score_s="B-";
                            		  }else if(mvh_score ==-107){
                            			  score_s="C+";
                            		  }else if(mvh_score ==-108){
                            			  score_s="C";
                            		  }else if(mvh_score ==-109){
                            			  score_s="C-";
                            		  }else if(mvh_score ==-110){
                            			  score_s="D";
                            		  }else if(mvh_score ==-111){
                            			  score_s="F";
                            		  }                            	
                            	}else{
                            		score_s  =  "";                            		
                            	}
                            }
                            
                            Timestamp temp_last_acc_datetime = (Timestamp)htReport.get("mvh_last_acc_datetime");
                            if(temp_last_acc_datetime != null) {
                                last_acc_datetime = temp_last_acc_datetime;
                            }
                            
                            Long mvh_ent_id = (Long)htReport.get("mvh_ent_id");
                            xmlBuf.append("<attempt usr_ent_id=\"").append(mvh_ent_id.longValue());
                            xmlBuf.append("\" usr_display_bil=\"").append(cwUtils.esc4XML((String)htReport.get("usr_display_bil")));
                            xmlBuf.append("\" usg_display_bil=\"").append(cwUtils.esc4XML((String)htReport.get("usg_display_bil")));
                            
                            if(bShowOrgName) {
                                xmlBuf.append("\" full_path=\"").append(cwUtils.esc4XML((String)htReport.get("ste_name")))
                                      .append(cwUtils.esc4XML(fullPathSeparator))
                                      .append(cwUtils.esc4XML((String)htReport.get("gpm_full_path")));
                            } else {
                                xmlBuf.append("\" full_path=\"").append(cwUtils.esc4XML((String)htReport.get("gpm_full_path")));
                            }
                            
                            Long mvh_mod_id = (Long)htReport.get("mvh_mod_id");
                            xmlBuf.append("\" module_id=\"").append(mvh_mod_id.longValue());
                            xmlBuf.append("\" module_type=\"").append(cwUtils.esc4XML((String)htReport.get("res_subtype")));
                            xmlBuf.append("\" module_title=\"").append(cwUtils.esc4XML((String)htReport.get("res_title")));
                            xmlBuf.append("\" start_acc_datetime=\"").append(start_acc_datetime);
                            xmlBuf.append("\" last_acc_datetime=\"").append(last_acc_datetime);
                            xmlBuf.append("\" used_time=\"").append(dbAiccPath.getTime(Math.round(timeUsed)));
                            xmlBuf.append("\" status=\"").append((String)htReport.get("mvh_status"));
                            xmlBuf.append("\" score=\"").append(score_s);
                            xmlBuf.append("\" location=\"").append(dbUtils.esc4XML((String)htReport.get("mvh_ele_loc")));
                            xmlBuf.append("\">").append(dbUtils.NEWL);
                            dbRegUser dbuser = new dbRegUser();
                            dbuser.usr_ent_id = mvh_ent_id.longValue();
                            dbuser.get(con);
                            xmlBuf.append(dbuser.getUserShortXML(con, false, true, false, false));
                            xmlBuf.append("</attempt>");     
                                
                            break;                            
                        }
                        else {
                            // accumulate the time spent
                            floatTimeUsed = (Float)htReport.get("mvh_total_time");
                            timeUsed += floatTimeUsed.floatValue();
                        }
                    }
                }
                
            }

            xmlBuf.append("</module_evaluation_history>").append(dbUtils.NEWL);
            return xmlBuf.toString();

        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        } catch(cwException e) {
            throw new qdbException("cwException: " + e.getMessage());
        }
    }
    
    /**
     * Delete all user evaluation history related to the module
     * @param con database connection
     * @param mod_id module id 
     * @throws SQLException
     */
    public static void delAllHistory(Connection con, long mod_id)
    	throws SQLException{
    		String SQL = "DELETE FROM ModuleEvaluationHistory WHERE mvh_mod_id = ? ";
    		PreparedStatement stmt = con.prepareStatement(SQL);
    		stmt.setLong(1, mod_id);
    		stmt.executeUpdate();
    		stmt.close();
    		return;
    	}
    
    public void updScore(Connection con) throws SQLException {
    	String sql = null;
    	if(cwSQL.DBVENDOR_MYSQL.equalsIgnoreCase(cwSQL.getDbType())){
        	 sql = "update moduleEvaluationHistory set mvh_score = ?, mvh_status = ? where mvh_id = " +
        			" (select max_mvh_id from ( select max(mvh_id) as max_mvh_id from moduleEvaluationHistory where mvh_mod_id = ? and mvh_ent_id = ? and mvh_tkh_id = ?) as tmp )";
    	}else{
        	 sql = "update moduleEvaluationHistory set mvh_score = ?, mvh_status = ? where mvh_id = " +
        			"(select max(mvh_id) from moduleEvaluationHistory where mvh_mod_id = ? and mvh_ent_id = ? and mvh_tkh_id = ?)";
    	}

    	PreparedStatement stmt = null;
		stmt = con.prepareStatement(sql);
		int index = 1;
		stmt.setString(index++, mvh_score);
		stmt.setString(index++, mvh_status);
		stmt.setLong(index++, mvh_mod_id);
		stmt.setLong(index++, mvh_ent_id);
		stmt.setLong(index++, mvh_tkh_id);
		stmt.executeUpdate();
		stmt.close();  	
    }
}