/**
 * 
 */
package com.cw.wizbank.supplier.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.cw.wizbank.qdb.qdbAction;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.supplier.SupplierComment;
import com.cw.wizbank.supplier.SupplierModuleParam;
import com.cw.wizbank.supplier.entity.Supplier;
import com.cw.wizbank.supplier.entity.SupplierCooperationExperience;
import com.cw.wizbank.supplier.entity.SupplierMainCourse;
import com.cw.wizbank.supplier.utils.SQLHelper;
import com.cw.wizbank.util.cwPagination;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwUtils;

/**
 * @author Leon.li
 * 2013-5-24 5:15:05
 * message  supplier module dao support
 */
public class SupplierDao {
	
	/**
	 * get all the supplier object from database
	 * @param con	 :	 database resource
	 * @param cwPage
	 * @param isLrn
	 * @return
	 * @throws SQLException
	 */
	public List<Supplier> getAllSupplierList(Connection con, SupplierModuleParam param,String term) throws SQLException{
		List<Supplier> list =  new ArrayList<Supplier>();
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
	    int index = 1; 
	    
	   
		cwPagination cwPage= param.getCwPage();	
		//System.out.println(" cur page :" +cwPage.curPage +"  pagezise :"+cwPage.pageSize);
		
		if (cwPage.sortCol == null || cwPage.sortCol.length() == 0) {
			cwPage.sortCol = "spl_status";
		}
		if (cwPage.sortOrder == null || cwPage.sortOrder.length() == 0) {
			cwPage.sortOrder = "desc";
		}
/*		if("scm_score".equals(cwPage.sortCol)){
			cwPage.sortCol = "score";
		}*/
		//StringBuffer sql = new StringBuffer("select spl.spl_id , spl.spl_status ,avg(scm.scm_design_score+scm.scm_management_score+scm.scm_teaching_score+scm.scm_price_score) score, spl.spl_name ,spl.spl_course ,spl.spl_address,spl.spl_update_datetime from supplier spl left join SupplierCooperationExperience sce on sce.sce_spl_id = spl.spl_id left join SupplierComment scm on scm.scm_spl_id = spl.spl_id ");
		

		StringBuffer sql = new StringBuffer("select spl.spl_id , spl.spl_status ,(case when avg(scm.scm_score) is null then 0 else avg(scm.scm_score) end) scm_score, spl.spl_name ,spl.spl_course ,spl.spl_address,spl.spl_update_datetime from supplier spl left join SupplierCooperationExperience sce on sce.sce_spl_id = spl.spl_id left join SupplierComment scm on scm.scm_spl_id = spl.spl_id ");
		sql.append(" where spl.spl_status != ? ");
		
		String splName = param.getSplName();
		String splStatus = param.getSplStatus();
		String sceItmName = param.getSceItmName();
		String splCourse = param.getSplCourse();
		String search_text = param.getSearch_text();
		
		//System.out.println("splName: "+splName +"  splCourse :"+splCourse);
		if(qdbAction.wizbini.cfgSysSetupadv.isTcIndependent()){
			sql.append(" and spl.spl_tcr_id in ( select tcn_child_tcr_id from tcRelation where tcn_ancestor = ? )or spl.spl_tcr_id = ? ");
		}
		if(splStatus!= null && !"".equals(splStatus)){
			sql.append(" and spl.spl_status = ? ");
		}
		if(splCourse!= null && !"".equals(splCourse)){
			sql.append(" and lower(spl.spl_course) like ? ESCAPE '\\'");
		}
		if(splName!= null && !"".equals(splName)){
			sql.append(" and lower(spl.spl_name) like ? ESCAPE '\\'");
		}
		if(sceItmName!= null && !"".equals(sceItmName)){
			sql.append(" and lower(sce.sce_itm_name) like ? ESCAPE '\\'");
		}
		if(search_text!= null && !"".equals(search_text)){
			sql.append(" and (lower(spl.spl_course) like ? ESCAPE '\\'  or lower(spl.spl_name) like ? ESCAPE '\\' or lower(sce.sce_itm_name) like ? ESCAPE '\\')");
		}
		sql.append(" group by spl.spl_id , spl.spl_status , spl.spl_name ,spl.spl_course ,spl.spl_address ,spl.spl_update_datetime");
		sql.append(" order by " + cwPage.sortCol + " " + cwPage.sortOrder);
		sql.append(" ,spl.spl_update_datetime desc");

/*		if("score".equals(cwPage.sortCol)){
			cwPage.sortCol = "scm_score";
		}*/
		//System.out.println(sql);
	    try {
		    stmt = con.prepareStatement(sql.toString(),ResultSet.TYPE_SCROLL_INSENSITIVE,  
                    ResultSet.CONCUR_READ_ONLY);
		    		    
		    stmt.setString(index++, term);
		    
		    if(qdbAction.wizbini.cfgSysSetupadv.isTcIndependent()){
		    	stmt.setLong(index++, param.getSpl_tcr_id());
		    	stmt.setLong(index++, param.getSpl_tcr_id());
		    }
		   
			if(splStatus!= null && !"".equals(splStatus)){
				stmt.setString(index++, splStatus);
			}
			if(splCourse!= null && !"".equals(splCourse)){
				stmt.setString(index++,'%' + SQLHelper.replaceSpecialChar(splCourse).toLowerCase() +'%' );			
			}
			if(splName!= null && !"".equals(splName)){
				stmt.setString(index++, '%' + SQLHelper.replaceSpecialChar(splName).toLowerCase() +'%' );
			}
			if(sceItmName!= null && !"".equals(sceItmName)){
				stmt.setString(index++, '%' + SQLHelper.replaceSpecialChar(sceItmName).toLowerCase() +'%');
			}
			if(search_text!= null && !"".equals(search_text)){
				stmt.setString(index++, '%' + SQLHelper.replaceSpecialChar(search_text).toLowerCase() +'%');
				stmt.setString(index++, '%' + SQLHelper.replaceSpecialChar(search_text).toLowerCase() +'%');
				stmt.setString(index++, '%' + SQLHelper.replaceSpecialChar(search_text).toLowerCase() +'%');
			}
		    rs = stmt.executeQuery();
		    
	        //rs.absolute(cwPage.pageSize * cwPage.curPage);  
	        //rs.relative(cwPage.curPage);  
	        int cnt=0;
	        rs.last();
	        cwPage.totalRec = rs.getRow();
	        rs.beforeFirst();
		    Supplier sp;
		    while(rs.next()){
		    	cnt ++; 
		    	if((cnt >= (cwPage.curPage-1)*cwPage.pageSize && cnt <= (cwPage.curPage)*cwPage.pageSize)){
			    	sp =  new Supplier();
			    	DecimalFormat dfm = new DecimalFormat("0.00");
			    	sp.setSplId(rs.getInt("spl_id"));
			    	sp.setSplAddress(rs.getString("spl_address"));
			    	float f = rs.getFloat("scm_score");
			    	if(f > 0) {
			    		f = new Float(dfm.format(f));
			    	}
			    	sp.setScmScore(f);
			    	sp.setSplName(rs.getString("spl_name"));
			    	sp.setSplCourse(rs.getString("spl_course"));
			    	sp.setSplStatus(rs.getString("spl_status"));
		    		list.add(sp);
		    	}
		    }
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return list;
	}
	
	/**
	 * get all the supplier object from database
	 * @param con	 :	 database resource
	 * @param cwPage
	 * @param isLrn
	 * @return
	 * @throws SQLException
	 */
	public List<Supplier> getSupplierDownList(Connection con, SupplierModuleParam param,String term) throws SQLException{
		List<Supplier> list =  new ArrayList<Supplier>();
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
	    int index = 1; 

		StringBuffer sql=new StringBuffer("SELECT spl_id")
		 .append(" ,spl_name")
	     .append(" ,spl_established_date")
	     .append(" ,spl_representative")
	     .append(" ,spl_registered_capital")
	     .append(" ,spl_type")
	     .append(" ,spl_address")
	     .append(" ,spl_post_num")
	     .append(" ,spl_contact")
	     .append(" ,spl_tel")
	     .append(" ,spl_mobile")
	     .append(" ,spl_email")
	     .append(" ,spl_total_staff")
	     .append(" ,spl_full_time_inst")
	     .append(" ,spl_part_time_inst")
	     .append(" ,spl_expertise")
	     .append(" ,spl_course")
	     .append(" ,spl_status")
	     .append(" ,spl_attachment_1")
	     .append(" ,spl_attachment_2")
	     .append(" ,spl_attachment_3")
	     .append(" ,spl_attachment_4")
	     .append(" ,spl_attachment_5")
	     .append(" ,spl_attachment_6")
	     .append(" ,spl_attachment_7")
	     .append(" ,spl_attachment_8")
	     .append(" ,spl_attachment_9")
	     .append(" ,spl_attachment_10")
	     .append(" ,spl_create_datetime")
	     .append(" ,spl_create_usr_id")
	     .append(" ,spl_update_datetime")
	     .append(" ,spl_update_usr_id")
	 .append(" FROM supplier");
		 sql.append(" where spl_status != ?"  ) ;
		
		//sql.append(" group by spl.spl_id , spl.spl_status , spl.spl_name ,spl.spl_course ,spl.spl_address,spl.spl_update_datetime");
		sql.append(" order by spl_status,spl_update_datetime desc");

	    try {
		    stmt = con.prepareStatement(sql.toString(),ResultSet.TYPE_SCROLL_INSENSITIVE,  
                    ResultSet.CONCUR_READ_ONLY);
		    
		    stmt.setString(index, term);
		    rs = stmt.executeQuery();
		    Supplier sp;
		    while(rs.next()){
			    	sp =  new Supplier();
			    	sp.setSplId(rs.getInt("spl_id"));
			    	sp.setSplAddress(rs.getString("spl_address"));
			    	sp.setSplName(rs.getString("spl_name"));
			    	sp.setSplRepresentative(rs.getString("spl_representative"));
			    	sp.setSplCourse(rs.getString("spl_course"));
			    	sp.setSplStatus(rs.getString("spl_status"));
			    	sp.setSplContact(rs.getString("spl_contact"));
			    	sp.setSplCourse(rs.getString("spl_course"));
			    	sp.setSplEmail(rs.getString("spl_email"));
			    	sp.setSplEstablishedDate(rs.getTimestamp("spl_established_date"));
			    	sp.setSplExpertise(rs.getString("spl_expertise"));
			    	sp.setSplFullTimeInst(rs.getInt("spl_full_time_inst"));
			    	sp.setSplMobile(rs.getString("spl_mobile"));
			    	sp.setSplPartTimeInst(rs.getInt("spl_part_time_inst"));
			    	sp.setSplPostNum(rs.getString("spl_post_num"));
			    	sp.setSplRegisteredCapital(rs.getString("spl_registered_capital"));
			    	sp.setSplTel(rs.getString("spl_tel"));
			    	sp.setSplTotalStaff(rs.getInt("spl_total_staff"));
			    	sp.setSplType(rs.getString("spl_type"));
		    		list.add(sp);
		    }
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return list;
	}
	
	
	/**
	 *  get some fields of Supplier object  from database by id
	 * @param con
	 * @param spl_id
	 * @return
	 * @throws SQLException
	 */
	public Supplier getSupplierListFieldsByID(Connection con, long spl_id) throws SQLException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
	    Supplier sp = null;
	    
		StringBuffer sql=new StringBuffer("select spl.spl_id , spl.spl_status , spl.spl_name ,spl.spl_course ,spl.spl_address ,spl_update_datetime from supplier spl ");
				sql.append(" where spl_id = ?" )
				.append(" order by spl.spl_status,spl.spl_update_datetime desc");

	    try {
		    stmt = con.prepareStatement(sql.toString());
		    int index = 1; 
		    stmt.setLong(index, spl_id);
		    rs = stmt.executeQuery();
		    while(rs.next()){
		    	sp =  new Supplier();
		    	sp.setSplId(rs.getInt("spl_id"));
		    	sp.setSplAddress(rs.getString("spl_address"));
		    	sp.setSplName(rs.getString("spl_name"));
		    	sp.setSplCourse(rs.getString("spl_course"));
		    	sp.setSplStatus(rs.getString("spl_status"));
		    }
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return sp;
	}

	/**
	 *  get all fields of Supplier object from database by id
	 * @param con
	 * @param spl_id
	 * @return
	 * @throws SQLException
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public Supplier getSupplierViewByID(Connection con, long spl_id,String del) throws SQLException, IllegalArgumentException, IllegalAccessException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
	    Supplier sp = null;
	    
		StringBuffer sql=new StringBuffer("SELECT spl_id")
			 .append(" ,spl_name")
		     .append(" ,spl_established_date")
		     .append(" ,spl_representative")
		     .append(" ,spl_registered_capital")
		     .append(" ,spl_type")
		     .append(" ,spl_address")
		     .append(" ,spl_post_num")
		     .append(" ,spl_contact")
		     .append(" ,spl_tel")
		     .append(" ,spl_mobile")
		     .append(" ,spl_email")
		     .append(" ,spl_total_staff")
		     .append(" ,spl_full_time_inst")
		     .append(" ,spl_part_time_inst")
		     .append(" ,spl_expertise")
		     .append(" ,spl_course")
		     .append(" ,spl_status")
		     .append(" ,spl_attachment_1")
		     .append(" ,spl_attachment_2")
		     .append(" ,spl_attachment_3")
		     .append(" ,spl_attachment_4")
		     .append(" ,spl_attachment_5")
		     .append(" ,spl_attachment_6")
		     .append(" ,spl_attachment_7")
		     .append(" ,spl_attachment_8")
		     .append(" ,spl_attachment_9")
		     .append(" ,spl_attachment_10")
		     .append(" ,spl_create_datetime")
		     .append(" ,spl_create_usr_id")
		     .append(" ,spl_update_datetime")
		     .append(" ,spl_update_usr_id")
		 .append(" FROM supplier");
			 sql.append(" where spl_id = ? and spl_status != ?"  ) ;
	    try {
		    stmt = con.prepareStatement(sql.toString());
		    int index = 1; 
		    stmt.setLong(index++, spl_id);
		    stmt.setString(index, del);
		    rs = stmt.executeQuery();
		    while(rs.next()){
		    	sp =  new Supplier();
/*		    	Field[] fs = sp.getClass().getDeclaredFields();
		    	for(Field f :fs){
		    		f.setAccessible(true);
		    		if (f.getType().isAssignableFrom(Timestamp.class)) {
		    			f.set(sp, rs.getTimestamp(XMLHelper.camel4underline(f.getName()) ) );		
		    		} else {
		    			f.set(sp, rs.getObject( XMLHelper.camel4underline(f.getName()) ) );		
		    		}
		    	}*/
		    	sp.setSplId(rs.getInt("spl_id"));
		    	sp.setSplAddress(rs.getString("spl_address"));
		    	sp.setSplName(rs.getString("spl_name"));
		    	sp.setSplRepresentative(rs.getString("spl_representative"));
		    	sp.setSplCourse(rs.getString("spl_course"));
		    	sp.setSplStatus(rs.getString("spl_status"));
		    	sp.setSplContact(rs.getString("spl_contact"));
		    	sp.setSplCourse(rs.getString("spl_course"));
		    	sp.setSplEmail(rs.getString("spl_email"));
		    	sp.setSplEstablishedDate(rs.getTimestamp("spl_established_date"));
		    	sp.setSplExpertise(rs.getString("spl_expertise"));
		    	sp.setSplFullTimeInst(rs.getInt("spl_full_time_inst"));
		    	sp.setSplMobile(rs.getString("spl_mobile"));
		    	sp.setSplPartTimeInst(rs.getInt("spl_part_time_inst"));
		    	sp.setSplPostNum(rs.getString("spl_post_num"));
		    	sp.setSplRegisteredCapital(rs.getString("spl_registered_capital"));
		    	sp.setSplTel(rs.getString("spl_tel"));
		    	sp.setSplTotalStaff(rs.getInt("spl_total_staff"));
		    	sp.setSplType(rs.getString("spl_type"));
		    	sp.setSplCreateUsrId(rs.getString("spl_create_usr_id"));
		    	sp.setSplAttachment1(rs.getString("spl_attachment_1"));
		    	sp.setSplAttachment2(rs.getString("spl_attachment_2"));
		    	sp.setSplAttachment3(rs.getString("spl_attachment_3"));
		    	sp.setSplAttachment4(rs.getString("spl_attachment_4"));
		    	sp.setSplAttachment5(rs.getString("spl_attachment_5"));
		    	sp.setSplAttachment6(rs.getString("spl_attachment_6"));
		    	sp.setSplAttachment7(rs.getString("spl_attachment_7"));
		    	sp.setSplAttachment8(rs.getString("spl_attachment_8"));
		    	sp.setSplAttachment9(rs.getString("spl_attachment_9"));
		    	sp.setSplAttachment10(rs.getString("spl_attachment_10"));
		    }
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return sp;
	}

	/**
	 *  is this data have in database?
	 * @param con
	 * @param splId
	 * @param upd_time
	 * @return
	 * @throws SQLException
	 */
	public boolean isSupplierIdExist(Connection con, Integer splId,String del) throws SQLException {
		boolean exist = false;
		String sql = "select spl_id from supplier where spl_id = ? and spl_status != ?";//and spl_update_datetime = ?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
		    stmt = con.prepareStatement(sql);
		    int index=1;
		    stmt.setLong(index++, splId);
		    stmt.setString(index++, del);

		   // stmt.setTimestamp(index++, upd_time);
		    rs = stmt.executeQuery();
		    if(rs.next()){
		    	exist=true;
		    }
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return exist;	
	}

	/**
	 * insert data
	 * @param con
	 * @param sp
	 * @return
	 * @throws SQLException
	 */
	public long insert(Connection con, Supplier sp) throws SQLException {
		StringBuffer sql = new StringBuffer("INSERT INTO supplier (")
           .append("spl_name")
           .append(",spl_established_date")
           .append(",spl_representative")
           .append(",spl_registered_capital")
           .append(",spl_type")
           .append(",spl_address")
           .append(",spl_post_num")
           .append(",spl_contact")
           .append(",spl_tel")
           .append(",spl_mobile")
           .append(",spl_email")
           .append(",spl_total_staff")
           .append(",spl_full_time_inst")
           .append(",spl_part_time_inst")
           .append(",spl_expertise")
           .append(",spl_course")
           .append(",spl_status")
           .append(",spl_attachment_1")
           .append(",spl_attachment_2")
           .append(",spl_attachment_3")
           .append(",spl_attachment_4")
           .append(",spl_attachment_5")
           .append(",spl_attachment_6")
           .append(",spl_attachment_7")
           .append(",spl_attachment_8")
           .append(",spl_attachment_9")
           .append(",spl_attachment_10")
           .append(",spl_create_datetime")
           .append(",spl_create_usr_id")
           .append(",spl_update_datetime")
           .append(",spl_update_usr_id")
           .append(",spl_tcr_id )")
    .append(" VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? )");
		
		PreparedStatement stmt = null; 
		long  count = 0;
		try{
			stmt = con.prepareStatement(sql.toString(),PreparedStatement.RETURN_GENERATED_KEYS);
			int index=1;
			stmt.setString(index++, sp.getSplName());
			stmt.setTimestamp(index++, sp.getSplEstablishedDate());	
			stmt.setString(index++,sp.getSplRepresentative());	     
			stmt.setString(index++, sp.getSplRegisteredCapital());
			stmt.setString(index++, sp.getSplType());
			stmt.setString(index++, sp.getSplAddress());
			stmt.setString(index++, sp.getSplPostNum());
			stmt.setString(index++, sp.getSplContact());
			stmt.setString(index++, sp.getSplTel());
			stmt.setString(index++, sp.getSplMobile());
			stmt.setString(index++, sp.getSplEmail());
			stmt.setInt(index++, sp.getSplTotalStaff());
			stmt.setInt(index++, sp.getSplFullTimeInst());
			stmt.setInt(index++, sp.getSplPartTimeInst());
			stmt.setString(index++, sp.getSplExpertise());
			stmt.setString(index++, sp.getSplCourse());
			stmt.setString(index++, sp.getSplStatus());
		
			stmt.setString(index++, sp.getSplAttachment1());
			stmt.setString(index++, sp.getSplAttachment2());
			stmt.setString(index++, sp.getSplAttachment3());
			stmt.setString(index++, sp.getSplAttachment4());
			stmt.setString(index++, sp.getSplAttachment5());
			stmt.setString(index++, sp.getSplAttachment6());
			stmt.setString(index++, sp.getSplAttachment7());
			stmt.setString(index++, sp.getSplAttachment8());
			stmt.setString(index++, sp.getSplAttachment9());
			stmt.setString(index++, sp.getSplAttachment10());
			
			stmt.setTimestamp(index++, sp.getSplCreateDatetime());
			stmt.setString(index++, sp.getSplCreateUsrId());
			stmt.setTimestamp(index++, sp.getSplUpdateDatetime());
			stmt.setString(index++, sp.getSplUpdateUsrId());
			stmt.setLong(index, sp.getSplTcrId());

			stmt.executeUpdate();
			count = cwSQL.getAutoId(con, stmt, "supplier", "spl_id");
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
		return count;
	}

	/**
	 * 
	 * @param con
	 * @param splId
	 * @param del
	 * @return
	 * @throws SQLException
	 */
	public boolean updateDel(Connection con ,Integer splId ,String del) throws SQLException {
		String sql = "UPDATE supplier SET spl_status = ? WHERE  spl_id = ?";
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(sql);
			int index = 1;
			stmt.setString(index++, del);
			stmt.setLong(index, splId);
			stmt.executeUpdate();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
		return true;
	}
	
	public boolean delete(Connection con ,Integer splId) throws SQLException {
		String sql = "DELETE FROM supplier WHERE  spl_id = ?";
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(sql);
			int index = 1;
			stmt.setLong(index, splId);
			stmt.executeUpdate();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
		return true;
	}
	/**
	 * update supplier
	 * @param con
	 * @param sp
	 * @throws SQLException
	 */
	public void update(Connection con, Supplier sp) throws SQLException {
		StringBuffer sql = new StringBuffer("UPDATE supplier")
		  .append(" SET spl_name = ? ")
	      .append(",spl_established_date = ? ")
	      .append(",spl_representative = ? ")
	      .append(",spl_registered_capital = ?")
	      .append(",spl_type = ?")
	      .append(",spl_address = ?")
	      .append(",spl_post_num = ?")
	      .append(",spl_contact = ?")
	      .append(",spl_tel = ?")
	      .append(",spl_mobile = ?")
	      .append(",spl_email = ?")
	      .append(",spl_total_staff = ?")
		  .append(",spl_full_time_inst = ?")
	      .append(",spl_part_time_inst = ?")
		  .append(",spl_expertise = ?")
	      .append(",spl_course = ?")
	      .append(",spl_status = ?")
	      .append(",spl_attachment_1 = ?")
	      .append(",spl_attachment_2 = ?")
	      .append(",spl_attachment_3 = ?")
	      .append(",spl_attachment_4 = ?")
	      .append(",spl_attachment_5 = ?")
	      .append(",spl_attachment_6 = ?")
	      .append(",spl_attachment_7 = ?")
		  .append(",spl_attachment_8 = ?")
	      .append(",spl_attachment_9 = ?")
	      .append(",spl_attachment_10 = ?")
	      .append(",spl_create_datetime = ?")
	      .append(",spl_create_usr_id = ?")
	      .append(",spl_update_datetime = ?")
	      .append(",spl_update_usr_id = ?")
	      .append(" WHERE spl_id = ?");
		
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(sql.toString());
			int index = 1;
			stmt.setString(index++, sp.getSplName());
			stmt.setTimestamp(index++, sp.getSplEstablishedDate());	
			stmt.setString(index++,sp.getSplRepresentative());
			stmt.setString(index++, sp.getSplRegisteredCapital());
			stmt.setString(index++, sp.getSplType());
			stmt.setString(index++, sp.getSplAddress());
			stmt.setString(index++, sp.getSplPostNum());
			stmt.setString(index++, sp.getSplContact());
			stmt.setString(index++, sp.getSplTel());
			stmt.setString(index++, sp.getSplMobile());
			stmt.setString(index++, sp.getSplEmail());
			stmt.setInt(index++, sp.getSplTotalStaff());
			stmt.setInt(index++, sp.getSplFullTimeInst());
			stmt.setInt(index++, sp.getSplPartTimeInst());
			stmt.setString(index++, sp.getSplExpertise());
			stmt.setString(index++, sp.getSplCourse());
			stmt.setString(index++, sp.getSplStatus());
		
			stmt.setString(index++, sp.getSplAttachment1());
			stmt.setString(index++, sp.getSplAttachment2());
			stmt.setString(index++, sp.getSplAttachment3());
			stmt.setString(index++, sp.getSplAttachment4());
			stmt.setString(index++, sp.getSplAttachment5());
			stmt.setString(index++, sp.getSplAttachment6());
			stmt.setString(index++, sp.getSplAttachment7());
			stmt.setString(index++, sp.getSplAttachment8());
			stmt.setString(index++, sp.getSplAttachment9());
			stmt.setString(index++, sp.getSplAttachment10());
			
			stmt.setTimestamp(index++, sp.getSplCreateDatetime());
			stmt.setString(index++, sp.getSplCreateUsrId());
			stmt.setTimestamp(index++, sp.getSplUpdateDatetime());
			stmt.setString(index++, sp.getSplUpdateUsrId());
			
			stmt.setLong(index, sp.getSplId());
			stmt.executeUpdate();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}

	/**
	 * insert supplier cooperation experience
	 * @param con
	 * @param sce
	 * @throws SQLException
	 */
	public synchronized void insertSce(Connection con,SupplierCooperationExperience sce) throws SQLException{
		StringBuffer sql = new StringBuffer("INSERT INTO supplierCooperationExperience (");
	  //sql.append(" sce_id")		
	  sql.append("sce_spl_id")
		 .append(",sce_itm_name")
		 .append(",sce_start_date")
		 .append(",sce_end_date")
		 .append(",sce_desc")
		 .append(",sce_dpt")
		 .append(",sce_update_datetime") 
		 .append(",sce_update_usr_id)")
	 .append("VALUES")
		 .append("( ?,?,?,?,?,?,?,? )");
		 
		PreparedStatement stmt = null; 
		try{
			stmt = con.prepareStatement(sql.toString(),PreparedStatement.RETURN_GENERATED_KEYS);
			int index=1;
			stmt.setInt(index++, sce.getSceSplId());
			stmt.setString(index++, sce.getSceItmName());
			stmt.setTimestamp(index++, sce.getSceStartDate()==null?null:new Timestamp(sce.getSceStartDate().getTime()));
			stmt.setTimestamp(index++, sce.getSceEndDate()==null?null:new Timestamp(sce.getSceEndDate().getTime()));
			stmt.setString(index++, sce.getSceDesc());
			stmt.setString(index++, sce.getSceDpt());
			stmt.setTimestamp(index++, new Timestamp(sce.getSceUpdateDatetime().getTime()));
			stmt.setString(index++, sce.getSceUpdateUsrId());
			stmt.executeUpdate();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}

	public List<SupplierComment> getSupplierComment(Connection con ,long spl_id) {
		ResultSet rs = null;
		PreparedStatement stmt = null;
		List<SupplierComment> v =new ArrayList<SupplierComment>();
			
			
		try {
			String sql = " ";
			sql += " select scm_spl_id,scm_ent_id,scm_design_score,scm_management_score,scm_teaching_score,scm_teaching_score,scm_price_score,scm_score,scm_comment,scm_update_datetime,usr_display_bil  " ;
			sql += " from supplierComment ";
			sql += " left join RegUser on  scm_ent_id =usr_ent_id ";
			sql += "  where scm_spl_id = ? ";
			sql += " order by scm_update_datetime desc " ; 
			

			int idx = 1;
			stmt = con.prepareStatement(sql);
			stmt.setLong(idx++, spl_id);
			rs = stmt.executeQuery();
			while (rs.next()) {
				SupplierComment spl_c = new SupplierComment();
				spl_c.setScm_spl_id(rs.getLong("scm_spl_id"));
				spl_c.setScm_ent_id(rs.getLong("scm_ent_id"));
				spl_c.setScm_design_score(rs.getFloat("scm_design_score"));
				spl_c.setScm_management_score(rs.getFloat("scm_management_score"));
				spl_c.setScm_teaching_score(rs.getFloat("scm_teaching_score"));
				spl_c.setScm_price_score(rs.getFloat("scm_price_score"));
				spl_c.setScm_score(rs.getFloat("scm_score"));
				spl_c.setScm_comment(cwUtils.esc4XML(rs.getString("scm_comment")));
				spl_c.setScm_update_datetime(rs.getTimestamp("scm_update_datetime"));
				spl_c.setUsr_display_bil(rs.getString("usr_display_bil"));
				v.add(spl_c);
			}
		} catch (SQLException e) {
			try {
				throw new qdbException("SQL Error: " + e.getMessage());
			} catch (qdbException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return v;
	}

	/**
	 * delete all SupplierCooperationExperience
	 * @param con
	 * @throws SQLException
	 */
	public void delAllSce(Connection con,Integer splId) throws SQLException {
		String sql = "DELETE FROM SupplierCooperationExperience WHERE sce_spl_id = ?";
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(sql);
			stmt.setInt(1, splId);
			stmt.executeUpdate();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}
	
	/**
	 * get all supplier list cooperation experience 
	 * @param con
	 * @param splId
	 * @return
	 * @throws SQLException
	 */
	public List<SupplierCooperationExperience> getAllSupplierCooperationExperience(Connection con, Integer splId) throws SQLException{
		List<SupplierCooperationExperience> list =  new ArrayList<SupplierCooperationExperience>();
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
	    int index = 1; 
		
		StringBuffer sql = new StringBuffer("SELECT sce_id")
		     .append(",sce_spl_id")
		     .append(",sce_itm_name")
		     .append(",sce_start_date")
		     .append(",sce_end_date")
		     .append(",sce_desc")
		     .append(",sce_dpt")
		     .append(",sce_update_datetime")
		    // .append(",sce_update_usr_id")
		 .append(" FROM supplierCooperationExperience")
		 .append(" WHERE sce_spl_id = ?")
		 .append(" ORDER BY sce_id asc");

		 //.append(" ORDER BY sce_update_datetime,sce_id asc");

		//System.out.println(sql.toString()  + splId );
	    try {
		    stmt = con.prepareStatement(sql.toString(),ResultSet.TYPE_SCROLL_INSENSITIVE,  
                    ResultSet.CONCUR_READ_ONLY);
		    		    
		    stmt.setInt(index++, splId);
		   
		    rs = stmt.executeQuery();
		    
	        SupplierCooperationExperience sce = null;
		    while(rs.next()){
		    	sce = new SupplierCooperationExperience();
		    	sce.setSceId(rs.getInt("sce_id"));
		    	sce.setSceSplId(rs.getInt("sce_spl_id"));
		    	sce.setSceItmName(rs.getString("sce_itm_name"));
		    	sce.setSceStartDate(rs.getTimestamp("sce_start_date"));
		    	sce.setSceEndDate(rs.getTimestamp("sce_end_date"));
		    	sce.setSceDesc(rs.getString("sce_desc"));
		    	sce.setSceDpt(rs.getString("sce_dpt"));
		    	sce.setSceUpdateDatetime(rs.getTimestamp("sce_update_datetime"));
		    	list.add(sce);
		    }
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return list;
	}

	
	/**
	 * get all supplier list cooperation experience 
	 * @param con
	 * @param splId
	 * @return
	 * @throws SQLException
	 */
	public List<SupplierCooperationExperience> getAllSupplierCooperationExperienceDownload(Connection con, Integer splId,String del) throws SQLException{
		List<SupplierCooperationExperience> list =  new ArrayList<SupplierCooperationExperience>();
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
	    int index = 1; 
		
		StringBuffer sql = new StringBuffer("SELECT sce.sce_id")
		.append(" ,sce.sce_spl_id")
		.append(" ,spl.spl_name")
		.append(" ,spl.spl_status")
		.append(" ,spl.spl_representative")
	    .append(" ,sce.sce_itm_name")
	    .append(" ,sce.sce_start_date")
	    .append(" ,sce.sce_end_date")
	    .append(" ,sce.sce_desc")
	    .append(" ,sce.sce_dpt")
	    .append(" ,sce.sce_update_datetime")
	    .append(" ,sce.sce_update_usr_id")
	    .append(" FROM supplierCooperationExperience sce")
	    .append(" LEFT JOIN supplier spl ON spl.spl_id = sce.sce_spl_id ")
	    .append(" WHERE spl.spl_status != ? ")
	   // .append(" ORDER BY sce.sce_update_datetime,sce_id asc");
	    .append(" ORDER BY sce_id asc");

		//.append(" WHERE sce_spl_id = ?")
		//System.out.println(sql.toString()  + splId );
	    try {
		    stmt = con.prepareStatement(sql.toString(),ResultSet.TYPE_SCROLL_INSENSITIVE,  
                    ResultSet.CONCUR_READ_ONLY);
		    		    
		    //stmt.setInt(index++, splId);
		    stmt.setString(index, del);
		    rs = stmt.executeQuery();
	        SupplierCooperationExperience sce = null;
		    while(rs.next()){
		    	sce = new SupplierCooperationExperience();
		    	sce.setSceId(rs.getInt("sce_id"));
		    	sce.setSceSplId(rs.getInt("sce_spl_id"));
		    	sce.setSceItmName(rs.getString("sce_itm_name"));
		    	sce.setSceStartDate(rs.getTimestamp("sce_start_date"));
		    	sce.setSceEndDate(rs.getTimestamp("sce_end_date"));
		    	sce.setSceDesc(rs.getString("sce_desc"));
		    	sce.setSceDpt(rs.getString("sce_dpt"));
		    	sce.setSplName(rs.getString("spl_name"));
		    	sce.setSplStatus(rs.getString("spl_status"));
		    	sce.setSplRepresentative(rs.getString("spl_representative"));
		    	sce.setSceUpdateDatetime(rs.getTimestamp("sce_update_datetime"));
		    	list.add(sce);
		    }
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return list;
	}
	
	/**
	 * delete supplier main course
	 * @param con
	 * @throws SQLException
	 */
	public void delAllCourse(Connection con,Integer splId) throws SQLException {
		String sql = "DELETE FROM SupplierMainCourse WHERE smc_spl_id = ?";
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(sql);
			stmt.setInt(1, splId);
			stmt.executeUpdate();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}		
	}
	
	/**
	 * add supplier main course
	 * @param con
	 * @param smc
	 * @throws SQLException
	 */
	public synchronized void insertCourse(Connection con,SupplierMainCourse smc) throws SQLException{
		StringBuffer sql = new StringBuffer("INSERT INTO supplierMainCourse (")
		            .append("smc_spl_id")
		            .append(",smc_name")
		            .append(",smc_inst")
		            .append(",smc_price")
		            .append(",smc_update_datetime")
		            .append(",smc_update_usr_id)")
		    .append(" VALUES (")
		    		.append(" ?,?,?,?,?,? )");
		 
		PreparedStatement stmt = null; 
		try{
			stmt = con.prepareStatement(sql.toString(),PreparedStatement.RETURN_GENERATED_KEYS);
			int index=1;
			stmt.setInt(index++, smc.getSmcSplId());
			stmt.setString(index++, smc.getSmcName());
			stmt.setString(index++, smc.getSmcInst());
			stmt.setDouble(index++, smc.getSmcPrice());
			stmt.setTimestamp(index++, smc.getSmcUpdateDatetime()==null?null:new Timestamp(smc.getSmcUpdateDatetime().getTime()));
			stmt.setString(index++, smc.getSmcUpdateUsrId());
			stmt.executeUpdate();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}
	
	/**
	 * get all supplier list cooperation experience 
	 * @param con
	 * @param splId
	 * @return
	 * @throws SQLException
	 */
	public List<SupplierMainCourse> getAllSupplierMainCourse(Connection con, Integer splId) throws SQLException{
		List<SupplierMainCourse> list =  new ArrayList<SupplierMainCourse>();
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
	    int index = 1; 
		
		StringBuffer sql = new StringBuffer("SELECT smc_id")
			      .append(",smc_spl_id")
			      .append(",smc_name")
			      .append(",smc_inst")
			      .append(",smc_price")
			      .append(",smc_update_datetime")
			      //.append(",smc_update_usr_id")
			 .append(" FROM supplierMainCourse")
			 .append(" WHERE smc_spl_id = ? ")
			 .append(" ORDER BY smc_id asc");

			 //.append(" ORDER BY smc_update_datetime,smc_id asc");

	    try {
		    stmt = con.prepareStatement(sql.toString(),ResultSet.TYPE_SCROLL_INSENSITIVE,  
                    ResultSet.CONCUR_READ_ONLY);
		    		    
		    stmt.setInt(index++, splId);
		    rs = stmt.executeQuery();
		    
		    SupplierMainCourse smc = null;
		    while(rs.next()){
		    	smc = new SupplierMainCourse();
		    	smc.setSmcId(rs.getInt("smc_id"));
		    	smc.setSmcSplId(rs.getInt("smc_spl_id"));
		    	smc.setSmcName(rs.getString("smc_name"));
		    	smc.setSmcPrice(rs.getDouble("smc_price"));
		    	smc.setSmcInst(rs.getString("smc_inst"));
		    	smc.setSmcUpdateDatetime(rs.getTimestamp("smc_update_datetime"));
		    	list.add(smc);
		    }
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return list;
	}

	

	public void insSupplierComment(Connection con ,SupplierComment supp_c) throws qdbException {
		ResultSet rs = null;
		PreparedStatement stmt = null;
			
			
		try {
			String sql = " ";
			sql += "insert into supplierComment ( scm_spl_id,scm_ent_id,scm_design_score,scm_management_score , ";
			sql += " scm_teaching_score,scm_price_score,scm_score,scm_comment,scm_update_datetime,scm_create_datetime) ";
			sql += " values (?,?,?,?,?,?,?,?,?,?) ";

			int idx = 1;
			stmt = con.prepareStatement(sql);
			stmt.setLong(idx++, supp_c.getScm_spl_id());
			stmt.setLong(idx++, supp_c.getScm_ent_id());
			stmt.setFloat(idx++, supp_c.getScm_design_score());
			stmt.setFloat(idx++, supp_c.getScm_management_score());
			stmt.setFloat(idx++, supp_c.getScm_teaching_score());
			stmt.setFloat(idx++, supp_c.getScm_price_score());
			stmt.setFloat(idx++, supp_c.getScm_score());
			stmt.setString(idx++, supp_c.getScm_comment());
			stmt.setTimestamp(idx++, supp_c.getScm_update_datetime());
			stmt.setTimestamp(idx++, supp_c.getScm_create_datetime());
			int i  = stmt.executeUpdate();
			if (i != 1) {
				throw new qdbException("Failed to insert SupplierComment.");
			}
		} catch (SQLException e) {
			try {
				throw new qdbException("SQL Error: " + e.getMessage());
			} catch (qdbException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
	}
	
	public SupplierComment getSupplierComment(Connection con ,long spl_id,long ent_id) {
		ResultSet rs = null;
		PreparedStatement stmt = null;
		SupplierComment spl_c = new SupplierComment();
			
			
		try {
			String sql = " ";
			sql += " select scm_id,scm_spl_id,scm_ent_id,scm_design_score,scm_teaching_score,scm_price_score,scm_score,scm_management_score,scm_comment from supplierComment ";
			sql += " where scm_spl_id = ? and scm_ent_id = ? ";
			
			int idx = 1;
			stmt = con.prepareStatement(sql);
			stmt.setLong(idx++, spl_id);
			stmt.setLong(idx++, ent_id);
			rs = stmt.executeQuery();
			if (rs.next()) {
				spl_c.setScm_spl_id(rs.getLong("scm_spl_id"));
				spl_c.setScm_ent_id(rs.getLong("scm_ent_id"));
				spl_c.setScm_design_score(rs.getFloat("scm_design_score"));
				spl_c.setScm_management_score(rs.getFloat("scm_management_score"));
				spl_c.setScm_teaching_score(rs.getFloat("scm_teaching_score"));
				spl_c.setScm_price_score(rs.getFloat("scm_price_score"));
				spl_c.setScm_score(rs.getFloat("scm_score"));
				spl_c.setScm_comment(cwUtils.esc4XML(rs.getString("scm_comment")));
			}
		} catch (SQLException e) {
			try {
				throw new qdbException("SQL Error: " + e.getMessage());
			} catch (qdbException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return spl_c;
	}
	
	public void updateSupplierComment(Connection con ,SupplierComment supp_c) throws qdbException {
		ResultSet rs = null;
		PreparedStatement stmt = null;
			
			
		try {
			String sql = " ";
			sql += "update supplierComment set scm_design_score =?,scm_management_score =?, ";
			sql += " scm_teaching_score =?,scm_price_score =?,scm_score =?,scm_comment =?,scm_update_datetime =? ";
			sql += " where scm_spl_id =? and  scm_ent_id =? ";

			int idx = 1;
			stmt = con.prepareStatement(sql);
			stmt.setFloat(idx++, supp_c.getScm_design_score());
			stmt.setFloat(idx++, supp_c.getScm_management_score());
			stmt.setFloat(idx++, supp_c.getScm_teaching_score());
			stmt.setFloat(idx++, supp_c.getScm_price_score());
			stmt.setFloat(idx++, supp_c.getScm_score());
			stmt.setString(idx++, supp_c.getScm_comment());
			stmt.setTimestamp(idx++, supp_c.getScm_update_datetime());
			stmt.setLong(idx++, supp_c.getScm_spl_id());
			stmt.setLong(idx++, supp_c.getScm_ent_id());
			int i  = stmt.executeUpdate();
			if (i != 1) {
				throw new qdbException("Failed to insert SupplierComment.");
			}
		} catch (SQLException e) {
			try {
				throw new qdbException("SQL Error: " + e.getMessage());
			} catch (qdbException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
	}
}
