package com.cwn.wizbank.cpd.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.cw.wizbank.util.cwSQL;
import com.cwn.wizbank.entity.AeApplication;
import com.cwn.wizbank.entity.AeAttendance;
import com.cwn.wizbank.entity.AeItem;
import com.cwn.wizbank.entity.AeItemCPDGourpItem;
import com.cwn.wizbank.entity.AeItemCPDItem;
import com.cwn.wizbank.entity.CourseEvaluation;
import com.cwn.wizbank.entity.CpdGroup;
import com.cwn.wizbank.entity.CpdLrnAwardRecord;
import com.cwn.wizbank.entity.CpdType;
import com.cwn.wizbank.utils.CommonLog;

public class CpdDbUtilsForOld {

	public static List<CourseEvaluation> getCourseEvaluationByApp(long itm_id , long usr_ent_id , String covStatus ,Connection con ) throws SQLException{
		StringBuffer sql = new StringBuffer();
		sql.append("select cov_cos_id, cov_ent_id, cov_commence_datetime, cov_last_acc_datetime,cov_total_time,cov_status,  ")
			.append(" cov_status_ovrdn_ind,cov_score,cov_max_score,cov_comment,cov_final_ind,cov_complete_datetime, ")
			.append(" cov_update_timestamp,cov_tkh_id,cov_progress,app_id,att_timestamp ,app_ent_id ")
			.append(" from CourseEvaluation ce  ")
		   	.append(" left join aeApplication ae on ce.cov_tkh_id = ae.app_tkh_id ")
		   	.append(" left join aeAttendance att on att.att_app_id = ae.app_id ")
		   	.append(" where ae.app_itm_id = ? ")
			.append(" and ae.app_ent_id = ? ")
			.append(" and cov_status = ? ")
			.append(" order by cov_complete_datetime asc ");
			
		List<CourseEvaluation> result = new ArrayList<CourseEvaluation>();
		PreparedStatement stmt = null;
	    ResultSet rs = null;
		try{
			stmt = con.prepareStatement(sql.toString());
		    stmt.setLong(1, itm_id);
		    stmt.setLong(2, usr_ent_id);
		    stmt.setString(3,  CourseEvaluation.Completed);
		    rs = stmt.executeQuery();
		    
		    while(rs.next()){
		    	CourseEvaluation ce = new CourseEvaluation();
		    	ce.setCov_cos_id(rs.getLong("cov_cos_id"));
		    	ce.setCov_ent_id(rs.getLong("cov_ent_id"));
		    	ce.setCov_commence_datetime(rs.getDate("cov_commence_datetime"));
		    	ce.setCov_last_acc_datetime(rs.getDate("cov_last_acc_datetime"));
		    	ce.setCov_total_time(rs.getDouble("cov_total_time"));
		    	ce.setCov_status(rs.getString("cov_status"));
		    	ce.setCov_status_ovrdn_ind(rs.getBoolean("cov_status_ovrdn_ind"));
		    	ce.setCov_score(rs.getDouble("cov_score"));
		    	ce.setCov_max_score(rs.getDouble("cov_max_score"));
		    	ce.setCov_comment(rs.getString("cov_comment"));
		    	ce.setCov_final_ind(rs.getBoolean("cov_final_ind"));
		    	ce.setCov_complete_datetime(rs.getDate("cov_complete_datetime"));
		    	ce.setCov_update_timestamp(rs.getDate("cov_update_timestamp"));
		    	ce.setCov_tkh_id(rs.getLong("cov_tkh_id"));
		    	ce.setCov_progress(rs.getDouble("cov_progress"));
		    	AeApplication app = new AeApplication();
		    	app.setApp_id(rs.getLong("app_id"));
		    	app.setApp_ent_id(rs.getLong("app_ent_id"));
		    	ce.setAeApplication(app);
		    	AeAttendance att = new AeAttendance();
		    	att.setAtt_timestamp(rs.getDate("att_timestamp"));
		    	ce.setAtt(att);
		    	result.add(ce);
		    }
		    
		}finally {
			cwSQL.cleanUp(rs, stmt);
		}
        
		return result;
	}
	
	public static AeItem getAeItem(long itm_id ,Connection con) throws SQLException{
		
		StringBuffer sql = new StringBuffer();
		sql.append("select itm_id,itm_code,itm_title,itm_ref_ind,itm_type,itm_exam_ind  ")
		   	.append(" from  aeItem")
		   	.append(" where itm_id = ? ");
		
		AeItem result = null;
		PreparedStatement stmt = null;
	    ResultSet rs = null;
		try{
			stmt = con.prepareStatement(sql.toString());
		    stmt.setLong(1, itm_id);
		    rs = stmt.executeQuery();
		    
		    while(rs.next()){
		    	result = new AeItem();
		    	result.setItm_id(rs.getLong("itm_id"));
		    	result.setItm_code(rs.getString("itm_code"));
		    	result.setItm_title(rs.getString("itm_title"));
		    	result.setItm_ref_ind(rs.getLong("itm_ref_ind"));
		    	result.setItm_type(rs.getString("itm_type"));
		    	result.setItm_exam_ind(rs.getLong("itm_exam_ind"));
		    }
		}finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return result;
	}
	
	public static List<AeItemCPDItem> getAeItemCPDItem(long itm_id ,Connection con) throws SQLException{
		StringBuffer sql = new StringBuffer();
		sql.append("select  aci_id , aci_itm_id ,aci_accreditation_code ,aci_hours_end_date , aci_create_usr_ent_id ,  ")
			.append(" aci_create_datetime ,aci_update_usr_ent_id , aci_update_datetime ")
		   	.append(" from  aeItemCPDItem ")
		   	.append(" where aci_itm_id = ? ");
		
		List<AeItemCPDItem> result = new ArrayList<AeItemCPDItem>();
		PreparedStatement stmt = null;
	    ResultSet rs = null;
		try{
			stmt = con.prepareStatement(sql.toString());
		    stmt.setLong(1, itm_id);
		    rs = stmt.executeQuery();
		    
		    while(rs.next()){
		    	AeItemCPDItem itm = new AeItemCPDItem();
		    	itm.setAci_id(rs.getLong("aci_id"));
		    	itm.setAci_itm_id(rs.getLong("aci_itm_id"));
		    	itm.setAci_accreditation_code(rs.getString("aci_accreditation_code"));
		    	itm.setAci_hours_end_date(rs.getDate("aci_hours_end_date"));
		    	itm.setAci_create_usr_ent_id(rs.getLong("aci_create_usr_ent_id"));
		    	itm.setAci_create_datetime(rs.getDate("aci_create_datetime"));
		    	itm.setAci_update_usr_ent_id(rs.getLong("aci_update_usr_ent_id"));
		    	itm.setAci_update_datetime(rs.getDate("aci_update_datetime"));
		    	result.add(itm);
		    }
		}finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return result;
		
	}
	
	public static List<AeItemCPDGourpItem> getAeItemCPDGourpItem(Long itm_id , Long aci_id,Connection con) throws SQLException{
		StringBuffer sql = new StringBuffer();
		sql.append("select acgi_id ,acgi_cg_id ,acgi_aci_id ,acgi_itm_id ,acgi_award_core_hours ,acgi_award_non_core_hours ,  ")
			.append(" acgi_create_usr_ent_id ,acgi_create_datetime ,acgi_update_usr_ent_id ,acgi_update_datetime ,aci_id, ")
			.append(" aci_accreditation_code,cg_ct_id , aci_hours_end_date ")
		   	.append(" from  aeItemCPDGourpItem  left join aeItemCPDItem  on (acgi_aci_id = aci_id) left join cpdGroup on  cg_id = acgi_cg_id ")
		   	.append(" where 1=1 ");
		if(null!=itm_id){
			sql.append(" and acgi_itm_id = ? ");
		}
		if(null!=aci_id){
			sql.append(" and acgi_aci_id = ? ");
		}
		sql.append(" order by acgi_create_datetime asc ");
		
		List<AeItemCPDGourpItem> result = new ArrayList<AeItemCPDGourpItem>();
		PreparedStatement stmt = null;
	    ResultSet rs = null;
		try{
			stmt = con.prepareStatement(sql.toString());
			int index=1;
			if(null!=itm_id){
				stmt.setLong(index++, itm_id);
			}
			if(null!=aci_id){
				stmt.setLong(index++, aci_id);
			}
		    rs = stmt.executeQuery();
		    while(rs.next()){
		    	AeItemCPDGourpItem itm = new AeItemCPDGourpItem();
		    	itm.setAcgi_id(rs.getLong("acgi_id"));
		    	itm.setAcgi_cg_id(rs.getLong("acgi_cg_id"));
		    	itm.setAcgi_aci_id(rs.getLong("acgi_aci_id"));
		    	itm.setAcgi_itm_id(rs.getLong("acgi_itm_id"));
		    	itm.setAcgi_award_core_hours(rs.getFloat("acgi_award_core_hours"));
		    	itm.setAcgi_award_non_core_hours(rs.getFloat("acgi_award_non_core_hours"));
		    	itm.setAcgi_create_usr_ent_id(rs.getLong("acgi_create_usr_ent_id"));
		    	itm.setAcgi_create_datetime(rs.getDate("acgi_create_datetime"));
		    	itm.setAcgi_update_usr_ent_id(rs.getLong("acgi_update_usr_ent_id"));
		    	itm.setAcgi_update_datetime(rs.getDate("acgi_update_datetime"));
		    	AeItemCPDItem aci = new AeItemCPDItem();
		    	aci.setAci_id(rs.getLong("aci_id"));
		    	aci.setAci_accreditation_code(rs.getString("aci_accreditation_code"));
		    	aci.setAci_hours_end_date(rs.getDate("aci_hours_end_date"));
		    	itm.setAeItemCPDItem(aci);
		    	result.add(itm);
		    }
		}finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return result;
	}
	
	public static CpdType getCpdTypeById(Long ct_id , Connection con) throws SQLException{
		
		StringBuffer sql = new StringBuffer();
		sql.append("select * from cpdType  ")
		   	.append(" where ct_id = ? and ct_status = 'OK' ");

		CpdType result = null;
		PreparedStatement stmt = null;
	    ResultSet rs = null;
		try{
			stmt = con.prepareStatement(sql.toString());
			stmt.setLong(1, ct_id);
		    rs = stmt.executeQuery();
		    while(rs.next()){
		    	result = new CpdType();
		    	result.setCt_id(rs.getLong("ct_id"));
		    	result.setCt_license_type(rs.getString("ct_license_type"));
		    	result.setCt_license_alias(rs.getString("ct_license_alias"));
		    	result.setCt_starting_month(rs.getInt("ct_starting_month"));
		    	result.setCt_display_order(rs.getInt("ct_display_order"));
		    	result.setCt_award_hours_type(rs.getInt("ct_award_hours_type"));
		    	result.setCt_cal_before_ind(rs.getInt("ct_cal_before_ind"));
		    	result.setCt_trigger_email_type(rs.getInt("ct_trigger_email_type"));
		    	result.setCt_trigger_email_month_1(rs.getInt("ct_trigger_email_month_1"));
		    	result.setCt_trigger_email_date_1(rs.getInt("ct_trigger_email_date_1"));
		    	result.setCt_trigger_email_month_2(rs.getInt("ct_trigger_email_month_2"));
		    	result.setCt_trigger_email_date_2(rs.getInt("ct_trigger_email_date_2"));
		    	result.setCt_trigger_email_month_3(rs.getInt("ct_trigger_email_month_3"));
		    	result.setCt_trigger_email_date_3(rs.getInt("ct_trigger_email_date_3"));
		    	result.setCt_recover_hours_period(rs.getInt("ct_recover_hours_period"));
		    	result.setCt_create_usr_ent_id(rs.getLong("ct_create_usr_ent_id"));
		    	result.setCt_create_datetime(rs.getDate("ct_create_datetime"));
		    	result.setCt_update_usr_ent_id(rs.getLong("ct_update_usr_ent_id"));
		    	result.setCt_update_datetime(rs.getDate("ct_update_datetime"));
		    	result.setCt_status(rs.getString("ct_status"));
		    }
		}finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return result;
	}
	
	public static CpdGroup getCpdGroupById(Long cg_id , Connection con) throws SQLException{
		
		StringBuffer sql = new StringBuffer();
		sql.append("select cg_id,cg_code,cg_alias,cg_display_order, cg_contain_non_core_ind,cg_display_in_report_ind, ")
			.append(" cg_ct_id, cg_create_usr_ent_id, cg_create_datetime,cg_update_usr_ent_id, cg_update_datetime, ")
			.append(" cg_status, ct_id,ct_license_type, ct_license_alias,ct_starting_month ")
			.append(" from cpdGroup left join cpdType on ct_id = cg_ct_id ")
		   	.append(" where cg_id = ? and cg_status = 'OK' ");

		CpdGroup result = null;
		PreparedStatement stmt = null;
	    ResultSet rs = null;
		try{
			stmt = con.prepareStatement(sql.toString());
			stmt.setLong(1, cg_id);
		    rs = stmt.executeQuery();
		    while(rs.next()){
		    	CpdType ct = new CpdType();
		    	ct.setCt_id(rs.getLong("ct_id"));
		    	ct.setCt_license_type(rs.getString("ct_license_type"));
		    	ct.setCt_license_alias(rs.getString("ct_license_alias"));
		    	ct.setCt_starting_month(rs.getInt("ct_starting_month"));
		    	result = new CpdGroup();
		    	result.setCpdType(ct);
		    	result.setCg_id(rs.getLong("cg_id"));
		    	result.setCg_code(rs.getString("cg_code"));
		    	result.setCg_alias(rs.getString("cg_alias"));
		    	result.setCg_display_order(rs.getInt("cg_display_order"));
		    	result.setCg_contain_non_core_ind(rs.getInt("cg_contain_non_core_ind"));
		    	result.setCg_display_in_report_ind(rs.getInt("cg_display_in_report_ind"));
		    	result.setCg_ct_id(rs.getLong("cg_ct_id"));
		    	result.setCg_create_usr_ent_id(rs.getLong("cg_create_usr_ent_id"));
		    	result.setCg_create_datetime(rs.getDate("cg_create_datetime"));
		    	result.setCg_update_usr_ent_id(rs.getLong("cg_update_usr_ent_id"));
		    	result.setCg_update_datetime(rs.getDate("cg_update_datetime"));
		    	result.setCg_status(rs.getString("cg_status"));
		    }
		}finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return result;
	}
	
	public static List<CpdLrnAwardRecord> getCpdLrnAwardRecord(Long usr_ent_id , Long itm_id , Long cg_id, Connection con) throws SQLException{
		StringBuffer sql = new StringBuffer();
		sql.append("select clar_id ,clar_usr_ent_id ,clar_itm_id,clar_app_id ,clar_manul_ind,clar_ct_id ,clar_cg_id ,  ")
			.append(" clar_acgi_id ,clar_award_core_hours ,clar_award_non_core_hours ,clar_award_datetime ,clar_create_usr_ent_id , ")
			.append(" clar_create_datetime,clar_update_usr_ent_id ,clar_update_datetime ")
		   	.append(" from  cpdLrnAwardRecord   ")
		   	.append(" where 1=1 ");
		if(null!=usr_ent_id){
			sql.append(" and clar_usr_ent_id = ? ");
		}
		if(null!=itm_id){
			sql.append(" and ( clar_itm_id  in ( select ar.ire_child_itm_id from aeItemRelation ar ");
			sql.append(" inner join aeItemRelation ae on ae.ire_child_itm_id = ? ");
			sql.append(" and ar.ire_parent_itm_id = ae.ire_parent_itm_id) ");
			sql.append(" or clar_itm_id = ? ) ");
			//sql.append(" and clar_itm_id = ? ");
		}
		if(null!=cg_id){
			sql.append(" and clar_cg_id = ? ");
		}
		sql.append(" order by clar_award_datetime asc ");
		
		List<CpdLrnAwardRecord> result = new ArrayList<CpdLrnAwardRecord>();
		PreparedStatement stmt = null;
	    ResultSet rs = null;
		try{
			stmt = con.prepareStatement(sql.toString());
			int index=1;
			if(null!=usr_ent_id){
				stmt.setLong(index++, usr_ent_id);
			}
			if(null!=itm_id){
				stmt.setLong(index++, itm_id);
				stmt.setLong(index++, itm_id);
			}
			if(null!=cg_id){
				stmt.setLong(index++, cg_id);
			}
		    rs = stmt.executeQuery();
		    while(rs.next()){
		    	CpdLrnAwardRecord record = new CpdLrnAwardRecord();
		    	record.setClar_id(rs.getLong("clar_id"));
		    	record.setClar_usr_ent_id(rs.getLong("clar_usr_ent_id"));
		    	record.setClar_itm_id(rs.getLong("clar_itm_id"));
		    	record.setClar_app_id(rs.getLong("clar_app_id"));
		    	record.setClar_manul_ind(rs.getInt("clar_manul_ind"));
		    	record.setClar_ct_id(rs.getLong("clar_ct_id"));
		    	record.setClar_cg_id(rs.getLong("clar_cg_id"));
		    	record.setClar_acgi_id(rs.getLong("clar_acgi_id"));
		    	record.setClar_award_core_hours(rs.getFloat("clar_award_core_hours"));
		    	record.setClar_award_non_core_hours(rs.getFloat("clar_award_non_core_hours"));
		    	record.setClar_award_datetime(rs.getDate("clar_award_datetime"));
		    	record.setClar_create_usr_ent_id(rs.getLong("clar_create_usr_ent_id"));
		    	record.setClar_create_datetime(rs.getDate("clar_create_datetime"));
		    	record.setClar_update_usr_ent_id(rs.getLong("clar_update_usr_ent_id"));
		    	record.setClar_update_datetime(rs.getDate("clar_update_datetime"));
		    	result.add(record);
		    }
		}finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return result;
	}
	
	public static int updateCpdLrnAwardRecord(CpdLrnAwardRecord record ,Connection con) throws SQLException{
		StringBuffer sql = new StringBuffer();
		sql.append(" update cpdLrnAwardRecord ")
			.append(" set clar_usr_ent_id = ?, ")
			.append(" clar_itm_id = ? ,")
			.append(" clar_app_id = ? ,")
		   	.append(" clar_manul_ind = ? ,  ")
		   	.append(" clar_ct_id = ? , ")
		   	.append(" clar_cg_id = ? , ")
		   	.append(" clar_acgi_id = ?  , ")
		   	.append(" clar_award_core_hours = ? , ")
		   	.append(" clar_award_non_core_hours = ? , ")
		   	.append(" clar_award_datetime = ?,   ")
		   	.append(" clar_create_usr_ent_id = ?,   ")
		   	.append(" clar_create_datetime =  ?,   ")
		   	.append(" clar_update_usr_ent_id = ?,   ")
		   	.append(" clar_update_datetime = ?    ")
		   	.append(" where clar_id = ? ");
		
		PreparedStatement stmt = null;
	    ResultSet rs = null;
		try{
			stmt = con.prepareStatement(sql.toString());
			int index=1;
			stmt.setLong(index++, record.getClar_usr_ent_id());
			stmt.setLong(index++, record.getClar_itm_id());
			stmt.setLong(index++, record.getClar_acgi_id());
			stmt.setInt(index++, record.getClar_manul_ind());
			stmt.setLong(index++, record.getClar_ct_id());
			stmt.setLong(index++, record.getClar_cg_id());
			stmt.setLong(index++, record.getClar_acgi_id());
			stmt.setFloat(index++, record.getClar_award_core_hours());
			stmt.setFloat(index++, record.getClar_award_non_core_hours());
			stmt.setDate(index++, new java.sql.Date(record.getClar_award_datetime().getTime()));
			stmt.setLong(index++, record.getClar_create_usr_ent_id());
			stmt.setDate(index++, new java.sql.Date(record.getClar_create_datetime().getTime()));
			stmt.setLong(index++, record.getClar_update_usr_ent_id());
			stmt.setDate(index++, new java.sql.Date(new Date().getTime()));
			stmt.setLong(index++, record.getClar_id());
			return stmt.executeUpdate();
		   
		}finally {
			cwSQL.cleanUp(rs, stmt);
		}

	}
	
	public static int insertCpdLrnAwardRecord(CpdLrnAwardRecord record ,Connection con) throws SQLException{
	
		StringBuffer sql = new StringBuffer();
		sql.append(" insert into cpdLrnAwardRecord( ")
			.append(" clar_usr_ent_id,clar_itm_id,clar_app_id,clar_manul_ind, ")
			.append(" clar_ct_id,clar_cg_id,clar_acgi_id,clar_award_core_hours, ")
		   	.append(" clar_award_non_core_hours,clar_award_datetime,clar_create_usr_ent_id,clar_create_datetime, ")
		   	.append(" clar_update_usr_ent_id,clar_update_datetime ) ")
		   	.append(" values ( ")
			.append(" ?,?,?,?,?,?,?,?,?,?,?,?,?,? ) ");
		
		PreparedStatement stmt = null;
	    ResultSet rs = null;
		try{
			stmt = con.prepareStatement(sql.toString());
			int index=1;
			stmt.setLong(index++, record.getClar_usr_ent_id());
			stmt.setLong(index++, record.getClar_itm_id());
			stmt.setLong(index++, record.getClar_app_id());
			stmt.setInt(index++, record.getClar_manul_ind());
			stmt.setLong(index++, record.getClar_ct_id());
			stmt.setLong(index++, record.getClar_cg_id());
			stmt.setLong(index++, record.getClar_acgi_id());
			stmt.setFloat(index++, record.getClar_award_core_hours());
			stmt.setFloat(index++, record.getClar_award_non_core_hours());
			stmt.setDate(index++, new java.sql.Date(record.getClar_award_datetime().getTime()));
			stmt.setLong(index++, record.getClar_create_usr_ent_id());
			stmt.setDate(index++, new java.sql.Date(record.getClar_create_datetime().getTime()));
			stmt.setLong(index++, record.getClar_update_usr_ent_id());
			stmt.setDate(index++, new java.sql.Date(record.getClar_create_datetime().getTime()));
			return stmt.executeUpdate();
		}finally {
			cwSQL.cleanUp(rs, stmt);
		}

	}
	
	public static int insertAeItemCPDItem(AeItemCPDItem itm ,Connection con) throws SQLException{
		
		StringBuffer sql = new StringBuffer();
		sql.append(" insert into aeItemCPDItem( ")
			.append(" aci_itm_id ,aci_accreditation_code ,aci_hours_end_date , aci_create_usr_ent_id , ")
			.append(" aci_create_datetime ,aci_update_usr_ent_id , aci_update_datetime ")
		   	.append(" ) ")
		   	.append(" values ( ")
			.append(" ?,?,?,?,?,?,?)");
		
		PreparedStatement stmt = null;
	    ResultSet rs = null;
		try{
			String generatedColumns[] = { "ACI_ID" };  
			stmt = con.prepareStatement(sql.toString(),  generatedColumns);
			int index=1;
			stmt.setLong(index++, itm.getAci_itm_id());
			stmt.setString(index++, itm.getAci_accreditation_code());
			if(null != itm.getAci_hours_end_date()){
				stmt.setDate(index++, new java.sql.Date( itm.getAci_hours_end_date().getTime()));
			}else{
				stmt.setDate(index++, null);
			}
			stmt.setLong(index++, itm.getAci_create_usr_ent_id());
			stmt.setDate(index++, new java.sql.Date( itm.getAci_create_datetime().getTime()));
			stmt.setLong(index++, itm.getAci_update_usr_ent_id());
			if(null != itm.getAci_update_datetime()){
				stmt.setDate(index++, new java.sql.Date( itm.getAci_update_datetime().getTime()));
			}else{
				stmt.setDate(index++, null);
			}
			stmt.executeUpdate();
		    rs = stmt.getGeneratedKeys();
		    if (rs.next()) { 
		    	int id = rs.getInt(1); 
		    	return id;
		    } 
			return stmt.executeUpdate();
		}finally {
			cwSQL.cleanUp(rs, stmt);
		}
	}
	
	public static int insertAeItemCPDGourpItem(AeItemCPDGourpItem itm ,Connection con) throws SQLException{
		
		StringBuffer sql = new StringBuffer();
		sql.append(" insert into aeItemCPDGourpItem( ")
			.append(" acgi_cg_id ,acgi_aci_id ,acgi_itm_id ,acgi_award_core_hours ,acgi_award_non_core_hours , ")
			.append(" acgi_create_usr_ent_id ,acgi_create_datetime ,acgi_update_usr_ent_id ,acgi_update_datetime  ")
		   	.append(" ) ")
		   	.append(" values ( ")
			.append(" ?,?,?,?,?,?,?,?,?) ");
		
		PreparedStatement stmt = null;
	    ResultSet rs = null;
		try{
			stmt = con.prepareStatement(sql.toString());
			int index=1;
			stmt.setLong(index++, itm.getAcgi_cg_id());
			stmt.setLong(index++, itm.getAcgi_aci_id());
			stmt.setLong(index++, itm.getAcgi_itm_id());
			stmt.setFloat(index++, itm.getAcgi_award_core_hours());
			stmt.setFloat(index++, itm.getAcgi_award_non_core_hours());
			stmt.setLong(index++, itm.getAcgi_create_usr_ent_id());
			stmt.setDate(index++, new java.sql.Date( itm.getAcgi_create_datetime().getTime()));
			stmt.setLong(index++, itm.getAcgi_update_usr_ent_id());
			if(null != itm.getAcgi_update_datetime()){
				stmt.setDate(index++, new java.sql.Date( itm.getAcgi_update_datetime().getTime()));
			}else{
				stmt.setDate(index++, null);
			}
			return stmt.executeUpdate();
		}finally {
			cwSQL.cleanUp(rs, stmt);
		}
	}
	
    public static void delCourseCpd(Connection con, long itm_id)
            throws SQLException {
    	PreparedStatement stmt = null;
    	try{
	        String sql = "delete from aeItemCPDGourpItem where acgi_aci_id in ( select aci_id from aeItemCPDItem where aci_itm_id = ?  )";
	        stmt = con.prepareStatement(sql);
	        stmt.setLong(1, itm_id);
	        stmt.executeUpdate();
	        sql = "delete from aeItemCPDItem where aci_itm_id = ? ";
	        stmt = con.prepareStatement(sql);
	        stmt.setLong(1, itm_id);
	        stmt.executeUpdate();
	        sql = "delete from cpdLrnAwardRecord where clar_itm_id = ?";
	        stmt = con.prepareStatement(sql);
	        stmt.setLong(1, itm_id);
	        stmt.executeUpdate();
    	}catch(Exception ex){
    		CommonLog.error("CpdDbUtilsForOld.delCourseCpd error : "+ex.getMessage(), ex);
    	}finally {
    		if(null!=stmt){
    			stmt.close();
    		}
		}
    }
    
    public static void delUserCpd(Connection con, long usrEntId)
            throws SQLException {
    	PreparedStatement stmt = null;
    	try{
	        String sql = "delete from cpdRegistration where cr_usr_ent_id = ? ";
	        stmt = con.prepareStatement(sql);
	        stmt.setLong(1, usrEntId);
	        stmt.executeUpdate();
	        sql = "delete from cpdGroupRegistration where cgr_usr_ent_id = ?";
	        stmt = con.prepareStatement(sql);
	        stmt.setLong(1, usrEntId);
	        stmt.executeUpdate();
	        sql = "delete from cpdGroupRegHours where cgrh_usr_ent_id = ?";
	        stmt = con.prepareStatement(sql);
	        stmt.setLong(1, usrEntId);
	        stmt.executeUpdate();
	        sql = "delete from cpdLrnAwardRecord where clar_usr_ent_id = ?";
	        stmt = con.prepareStatement(sql);
	        stmt.setLong(1, usrEntId);
	        stmt.executeUpdate();
    	}catch(Exception ex){
    		CommonLog.error("CpdDbUtilsForOld.delUserCpd error : "+ex.getMessage(), ex);
    	}finally {
    		if(null!=stmt){
    			stmt.close();
    		}
		}
    }
}
