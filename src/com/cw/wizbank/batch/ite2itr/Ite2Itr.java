package com.cw.wizbank.batch.ite2itr;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.cw.wizbank.ae.db.DbItemTargetRule;
import com.cw.wizbank.ae.db.DbItemTargetRuleDetail;
import com.cw.wizbank.batch.batchUtil.BatchUtils;
import com.cw.wizbank.db.DbUserGrade;
import com.cw.wizbank.qdb.dbEntity;
import com.cw.wizbank.util.cwSQL;
import com.cwn.wizbank.utils.CommonLog;

public class Ite2Itr {

	public final static String ITE_TYPE_TARGETED_LEARNER = "TARGETED_LEARNER";
    public final static String ITE_TYPE_TARGETED_ENROLMENT = "TARGETED_ENROLMENT";
    
	public static void main(String[] args) throws SQLException{
		Connection con = null;
		if(args.length <= 0){
			System.err.println("Error : Please insert parameter !!");
		} else {
			try{
				con = BatchUtils.openDB(args[0]);
				Timestamp sTime = cwSQL.getTime(con);
				System.out.println("Transfer start... -- " + sTime);
	  	        int count = transferIte2Itr(con, sTime);
	  	        if(count > 0){
		  	        System.out.println("copy data from aeItemTargetRule to aeItemTargetRuleDetail.");
		  	        copyItr2Itrd(con);
				}
	  	        con.commit();
	  	        Timestamp eTime = cwSQL.getTime(con);
	  	        System.out.println("Transfer end, total="+ count +". -- " + eTime);
			} catch (Exception e){
				con.rollback();
				CommonLog.error(e.getMessage(),e);
				//System.out.println(e.getMessage());
			}
		}

	}

	public static int transferIte2Itr(Connection con, Timestamp curTime) throws SQLException{
		int count = 0;
		
		String getsql = "select aeItemTargetEntity.*, ent_type, itm_owner_ent_id"
						+ " From aeItemTargetEntity,Entity,aeItem"
						+ " where ite_ent_id = ent_id"
						+ " and ite_itm_id = itm_id"
						+ " and ite_itm_id not in (select distinct itr_itm_id from aeItemTargetRule)"
						+ " order by ite_itm_id,ite_group_id,ent_type";
		
		PreparedStatement stmt = con.prepareStatement(getsql);
		ResultSet rs = stmt.executeQuery();
		
		long ite_itm_id_temp = 0;
		long ite_group_id_temp = 0;
		
		long group_ent_id = 0;
		long grade_ent_id = 0;
		
		DbItemTargetRule itrd = new DbItemTargetRule();
		
		while(rs.next()){
			count++;
			
			long itm_id = rs.getLong("ite_itm_id");
			long group_id = rs.getLong("ite_group_id");
			long ent_id = rs.getLong("ite_ent_id");
			String ite_type = rs.getString("ite_type");
			Timestamp create_time = rs.getTimestamp("ite_create_timestamp");
			String create_usr_id = rs.getString("ite_create_usr_id");
			String target_type = rs.getString("ent_type");
			long root_ent_id = rs.getLong("itm_owner_ent_id");
			
			if(itm_id != ite_itm_id_temp){
				
				if(group_ent_id > 0 || grade_ent_id > 0){
					if(group_ent_id == 0){
						group_ent_id = root_ent_id;
					}
					if(grade_ent_id == 0){
						grade_ent_id = DbUserGrade.getGradeRoot(con, root_ent_id);
					}
					
					itrd.itr_group_id = String.valueOf(group_ent_id);
					itrd.itr_grade_id = String.valueOf(grade_ent_id);				    
					itrd.ins(con);
					//con.commit();
					
				}
				//初始化
				grade_ent_id = 0;
				group_ent_id = 0;
				if(target_type.equals(dbEntity.ENT_TYPE_USER_GROUP)){
					group_ent_id = ent_id;
				} else if (target_type.equals(dbEntity.ENT_TYPE_USER_GRADE)){
					grade_ent_id = ent_id;
				}
				
				ite_itm_id_temp = itm_id;
				ite_group_id_temp = group_id;
				itrd = new DbItemTargetRule();
				itrd.itr_itm_id = itm_id;
				itrd.itr_type = getItrType(ite_type);
				itrd.itr_create_usr_id = create_usr_id;
				itrd.itr_create_timestamp = create_time;
				itrd.itr_update_usr_id = create_usr_id;
				itrd.itr_update_timestamp = curTime;
				
			} else {
								
				if(group_id == ite_group_id_temp){
					
					if(target_type.equals(dbEntity.ENT_TYPE_USER_GROUP)){
						group_ent_id = ent_id;
					} else if (target_type.equals(dbEntity.ENT_TYPE_USER_GRADE)){
						grade_ent_id = ent_id;
					}
					
				} else {
					
					if(group_ent_id > 0 || grade_ent_id > 0){
						if(group_ent_id == 0){
							group_ent_id = root_ent_id;
						}
						if(grade_ent_id == 0){
							grade_ent_id = grade_ent_id = DbUserGrade.getGradeRoot(con, root_ent_id);;
						}
						
						itrd.itr_group_id = String.valueOf(group_ent_id);
						itrd.itr_grade_id = String.valueOf(grade_ent_id);				    
						itrd.ins(con);
						//con.commit();
						
					}
					//初始化
					grade_ent_id = 0;
					group_ent_id = 0;
					if(target_type.equals(dbEntity.ENT_TYPE_USER_GROUP)){
						group_ent_id = ent_id;
					} else if (target_type.equals(dbEntity.ENT_TYPE_USER_GRADE)){
						grade_ent_id = ent_id;
					}
					
					//ite_itm_id_temp = itm_id;
					ite_group_id_temp = group_id;
					itrd = new DbItemTargetRule();
					itrd.itr_itm_id = itm_id;
					itrd.itr_type = getItrType(ite_type);
					itrd.itr_create_usr_id = create_usr_id;
					itrd.itr_create_timestamp = create_time;
					itrd.itr_update_usr_id = create_usr_id;
					itrd.itr_update_timestamp = curTime;
					
				}
				
			}
			if(rs.isLast()){
				if(group_ent_id > 0 || grade_ent_id > 0){
					if(group_ent_id == 0){
						group_ent_id = root_ent_id;
					}
					if(grade_ent_id == 0){
						grade_ent_id = grade_ent_id = DbUserGrade.getGradeRoot(con, root_ent_id);;
					}
					
					itrd.itr_group_id = String.valueOf(group_ent_id);
					itrd.itr_grade_id = String.valueOf(grade_ent_id);				    
					itrd.ins(con);
					//con.commit();
					
				}
			}
		}
		stmt.close();
		return count;
	}
	public static void copyItr2Itrd(Connection con) throws SQLException{
		String copySql = "insert into aeItemTargetRuleDetail"
						+ " (ird_itm_id,ird_itr_id,ird_group_id,ird_grade_id,ird_type,ird_create_usr_id,ird_create_timestamp,ird_update_usr_id,ird_update_timestamp)"
						+ " select itr_itm_id,itr_id,itr_group_id,itr_grade_id,itr_type,itr_create_usr_id,itr_create_timestamp,itr_update_usr_id,itr_update_timestamp"
						+ " from aeItemTargetRule";
		PreparedStatement stmt = con.prepareStatement(copySql);
		stmt.execute();
		stmt.close();
	}
	public static String getItrType(String ite_type){
		
		if(ite_type.equalsIgnoreCase(ITE_TYPE_TARGETED_LEARNER)){
			return DbItemTargetRuleDetail.IRD_TYPE_TARGET_LEARNER;
		} else if(ite_type.equalsIgnoreCase(ITE_TYPE_TARGETED_ENROLMENT)){
			return DbItemTargetRuleDetail.IRD_TYPE_TARGET_ENROLLMENT;
		} else 
			return ite_type;
	}
}
