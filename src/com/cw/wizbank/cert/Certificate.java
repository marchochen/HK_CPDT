package com.cw.wizbank.cert;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Vector;

import com.cw.wizbank.db.DbTrainingCenter;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.trainingcenter.TrainingCenter;
import com.cw.wizbank.util.cwPagination;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.utils.CommonLog;


public class Certificate {
	public String getAllCertitfcateAsXml(Connection con,loginProfile prof,  boolean status_active, long tcr_id,int page,int page_size) throws SQLException {
		StringBuffer result = new StringBuffer();
		CertificateBean cerBean = null;

		Vector cerVec = CertificateDao.getAllCertificate(con,prof,status_active,tcr_id);
		if (page == 0) {
            page = 1;
        }
        if (page_size == 0) {
            page_size = 10;
        }
		result.append("<e_certificate >");
		
		int start = page_size * (page-1);
		int count = 0; 
		for (int i = 0; i < cerVec.size(); i++) {
			
			 if (count >= start && count < start+page_size) {
				 cerBean = (CertificateBean) cerVec.get(i);
					dbRegUser usr= new dbRegUser();
					result.append("<certificate id=\"").append(cerBean.getCfc_id()).append("\">");
					result.append("<title>").append(cwUtils.esc4XML(cerBean.getCfc_title())).append("</title>");
					result.append("<img_url>").append(cwUtils.esc4XML(cerBean.getCfc_img())).append("</img_url>");
					result.append("<cfc_tcr_id>").append(cerBean.getCfc_tcr_id()).append("</cfc_tcr_id>");
					result.append("<status>").append(cerBean.getCfc_status()).append("</status>");
					result.append("<cfc_create_datetime>").append(cerBean.getCfc_create_datetime()).append("</cfc_create_datetime>");
					result.append("<cfc_create_user_id>").append(cwUtils.esc4XML(cerBean.getCfc_create_user_id())).append("</cfc_create_user_id>");
					result.append("<cfc_update_datetime>").append(cerBean.getCfc_update_datetime()).append("</cfc_update_datetime>");
					result.append("<cfc_update_user_id>").append(cwUtils.esc4XML(cerBean.getCfc_update_user_id())).append("</cfc_update_user_id>");
					result.append("<update_user>").append(cwUtils.esc4XML(cerBean.getCfc_update_user_id())).append("</update_user>");
					result.append("<update_timestamp>").append(cerBean.getCfc_update_datetime()).append("</update_timestamp>");
					result.append("<cfc_core>").append(cwUtils.esc4XML(cerBean.getCfc_code())).append("</cfc_core>");
					Timestamp ts = new Timestamp(System.currentTimeMillis());   
					int  cfc_end_datetime_id= 0;
					if(ts.after(cerBean.getCfc_end_datetime())){
						cfc_end_datetime_id =1;
					}
					result.append("<cfc_end_datetime id=\"").append(cfc_end_datetime_id).append("\" >").append(cerBean.getCfc_end_datetime()).append("</cfc_end_datetime>");
					try {
						String user  = usr.getUserName(con,cerBean.getCfc_create_user_id());
						result.append("<cfc_create_user_name>").append(cwUtils.esc4XML(user)).append("</cfc_create_user_name>");
						user  = usr.getUserName(con,cerBean.getCfc_update_user_id());
						result.append("<cfc_update_user_name>").append(cwUtils.esc4XML(user)).append("</cfc_update_user_name>");
						
					} catch (cwSysMessage e) {
						// TODO Auto-generated catch block
						CommonLog.error(e.getMessage(),e);
					}
					
						
					
					result.append("</certificate>");
			 }
			 count++;
		}
		result.append("</e_certificate>");
		// current training center
		DbTrainingCenter dbTrainingCenter = DbTrainingCenter.getInstance(con, tcr_id);
		result.append("<cur_training_center id=\"").append(tcr_id).append("\">");
		if(dbTrainingCenter == null) {
			result.append("<title/>");
		} else {
			result.append("<title>").append(cwUtils.esc4XML(dbTrainingCenter.getTcr_title())).append("</title>");
		}
		result.append("</cur_training_center>");
		cwPagination pagn = new cwPagination();
        pagn.totalRec = count;
        pagn.totalPage = (int)Math.ceil((float)count/page_size);
        pagn.pageSize = page_size;
        pagn.curPage = page;
        pagn.sortCol = "";
        pagn.sortOrder = "";
        pagn.ts = null;
        result.append(pagn.asXML());
		return result.toString();
	}
	public long addCertcate(Connection con, loginProfile prof, CertificateParam modParam) throws SQLException,ParseException {
		CertificateDao certDAO = new CertificateDao();
		CertificateBean cert = new CertificateBean();
		
		cert.setCfc_title(modParam.getCert_title());
		cert.setCfc_img(modParam.getCert_img());
		cert.setCfc_tcr_id(modParam.getCert_tc_id());
		cert.setCfc_status(modParam.getMod_status_ind());
		cert.setCfc_create_datetime(modParam.getCur_time());
		cert.setCfc_create_user_id(prof.usr_id);
		cert.setCfc_update_datetime(modParam.getCur_time());
		cert.setCfc_update_user_id(prof.usr_id);
		cert.setCfc_code(modParam.getCert_core());
		

		Timestamp ts = cwUtils.parse(modParam.getCert_end());
		cert.setCfc_end_datetime(ts);
		
		long cert_id = certDAO.ins(con, cert);
		return cert_id;
	}
	
	public boolean hasICertExist(Connection con,CertificateParam modParam,boolean isTcIndependent)throws SQLException{
        String sql = "select * from certificate where cfc_code = ? and cfc_delete_user_id is null";
        if(isTcIndependent){ //LN模式，只需要保证同一个企业内的唯一
        	sql += " and cfc_tcr_id = ? ";
        }
        if (modParam.getCert_id() > 0) {
        	sql += " and cfc_id != ? ";
        }
        PreparedStatement pstmt = null;
        try {
        	int index = 1;
            pstmt = con.prepareStatement(sql);
            pstmt.setString(index++, modParam.getCert_core());
            if(isTcIndependent){
            	 pstmt.setLong(index++, modParam.getCert_tc_id());
            }
            if (modParam.getCert_id() > 0) {
            	pstmt.setLong(index++, modParam.getCert_id());
            }
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return true;
            }
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
        return false;
    }
	
	public String getCertInfoXml(Connection con, CertificateParam modParam,String saveDirPath,loginProfile prof) throws SQLException {
		StringBuffer result = new StringBuffer();
		CertificateBean cert = CertificateDao.getCertByID(con, modParam.getCert_id());
		String tcr_info = TrainingCenter.getTcrAsXml(con, cert.getCfc_tcr_id(), prof.root_ent_id);
		if(cert != null){
			result.append("<certInfo id=\"").append(modParam.getCert_id()).append("\">");
		    result.append("<title>").append(cwUtils.esc4XML(cert.getCfc_title())).append("</title>");
		    result.append("<img_url>").append(saveDirPath+"/"+cwUtils.esc4XML(cert.getCfc_img())).append("</img_url>");
		    result.append("<tcr_id>").append(cert.getCfc_tcr_id()).append("</tcr_id>");
		    result.append("<status>").append(cert.getCfc_status()).append("</status>");
		    result.append("<cfc_create_datetime>").append(cert.getCfc_create_datetime()).append("</cfc_create_datetime>");
			result.append("<cfc_create_user_id>").append(cwUtils.esc4XML(cert.getCfc_create_user_id())).append("</cfc_create_user_id>");
			result.append("<cfc_update_datetime>").append(cert.getCfc_update_datetime()).append("</cfc_update_datetime>");
			result.append("<cfc_update_user_id>").append(cwUtils.esc4XML(cert.getCfc_update_user_id())).append("</cfc_update_user_id>");
			result.append("<cfc_core>").append(cwUtils.esc4XML(cert.getCfc_code())).append("</cfc_core>");
			result.append("<cfc_end_datetime>").append(cert.getCfc_end_datetime()).append("</cfc_end_datetime>");
			result.append("</certInfo>");
			result.append(tcr_info);
		}
		return result.toString();
	}
	public void updCert(Connection con, loginProfile prof, CertificateParam modParam) throws SQLException ,ParseException {
		CertificateDao certDAO = new CertificateDao();
		CertificateBean cert = new CertificateBean();
		cert.setCfc_id(modParam.getCert_id());
		cert.setCfc_title(modParam.getCert_title());
		cert.setCfc_img(modParam.getCert_img());
		cert.setCfc_tcr_id(modParam.getCert_tc_id());
		cert.setCfc_status(modParam.getMod_status_ind());
		cert.setCfc_update_datetime(modParam.getCur_time());
		cert.setCfc_update_user_id(prof.usr_id);
		cert.setCfc_code(modParam.getCert_core());
		Timestamp ts = cwUtils.parse(modParam.getCert_end());
		cert.setCfc_end_datetime(ts);
		certDAO.upd(con, cert);
	} 
	
	public void delCert(Connection con, loginProfile prof, CertificateParam modParam) throws SQLException {
		CertificateDao certDAO = new CertificateDao();
		CertificateBean cert = new CertificateBean();
		cert.setCfc_id(modParam.getCert_id());
		cert.setCfc_delete_datetime(modParam.getCur_time());
		cert.setCfc_delete_user_id(prof.usr_id);
		certDAO.del(con, cert);
	}
	
	public static void TCdelCert(Connection con, DbTrainingCenter dbTc, Timestamp tsp) throws SQLException {
		CertificateDao certDAO = new CertificateDao();
		CertificateBean cert = new CertificateBean();
		cert.setCfc_tcr_id(dbTc.getTcr_id());
		cert.setCfc_delete_datetime(tsp);
		cert.setCfc_delete_user_id(dbTc.getTcr_update_usr_id());
		certDAO.tcDel(con, cert);
	}
	
	  public String getAllCertXml(Connection con, long cfc_id)throws SQLException{
	        String sql = "select * from certificate where cfc_status = 'ON' and cfc_end_date >" + cwSQL.getDate()+" and cfc_delete_datetime is null  or cfc_id = ? ";
	        StringBuffer xml = new StringBuffer();
	        PreparedStatement pstmt = null;
	        try {
	            pstmt = con.prepareStatement(sql);
	            pstmt.setLong(1, cfc_id);
	            ResultSet rs = pstmt.executeQuery();
	            xml.append("<cert_lst>");
	            while (rs.next()) {
	                xml.append("<cert cfc_id =\"").append(rs.getLong("cfc_id"))
	                .append("\" cfc_title=\"").append(cwUtils.esc4XML(rs.getString("cfc_title")))
	                .append("\" cfc_tcr_id=\"").append(rs.getLong("cfc_tcr_id"))
	                .append("\"/>");
	            }
	            xml.append("</cert_lst>");
	        } finally {
	        	if (pstmt != null) {
	                pstmt.close();
	            }
	        }
	        return xml.toString();
	       // return "<cert_lst><cert cfc_id =\"2\" cfc_title=\"cert2\" cfc_tcr_id=\"1\"/><cert cfc_id =\"3\" cfc_title=\"cert3\" cfc_tcr_id=\"1\"/><cert cfc_id =\"4\" cfc_title=\"cert4\" cfc_tcr_id=\"2\"/></cert_lst>";
	    }
	    public boolean isICertExist(Connection con,long cfc_id)throws SQLException{
	        String sql = "select * from certificate where cfc_id = ? and cfc_status = 'ON' ";
	        PreparedStatement pstmt = null;
	        try {
	            pstmt = con.prepareStatement(sql);
	            pstmt.setLong(1, cfc_id);
	            ResultSet rs = pstmt.executeQuery();
	            if (rs.next()) {
	                return true;
	            }
	        } finally {
	            if (pstmt != null) {
	                pstmt.close();
	            }
	        }
	        return false;
	    }
	    
	    public String getsearchCertitfcateAsXml(Connection con,loginProfile prof,String cert_status_sear, int page,int page_size,long tcr_id,String cert_core,String cert_title) throws SQLException {
			StringBuffer result = new StringBuffer();
			CertificateBean cerBean = null;

			Vector cerVec = CertificateDao.searchAllCertificate(con,prof,cert_status_sear,cert_core,cert_title,tcr_id);
			if (page == 0) {
	            page = 1;
	        }
	        if (page_size == 0) {
	            page_size = 10;
	        }
	        result.append("<search status=\"").append(cert_status_sear);
	        result .append("\" core=\"").append(cwUtils.esc4XML(cert_core));
	        result .append("\" title=\"").append(cwUtils.esc4XML(cert_title)).append("\" />");
			result.append("<e_certificate >");
			
			int start = page_size * (page-1);
			int count = 0; 
			for (int i = 0; i < cerVec.size(); i++) {
				
				 if (count >= start && count < start+page_size) {
					 cerBean = (CertificateBean) cerVec.get(i);
						dbRegUser usr= new dbRegUser();
						result.append("<certificate id=\"").append(cerBean.getCfc_id()).append("\">");
						result.append("<title>").append(cwUtils.esc4XML(cerBean.getCfc_title())).append("</title>");
						result.append("<img_url>").append(cwUtils.esc4XML(cerBean.getCfc_img())).append("</img_url>");
						result.append("<cfc_tcr_id>").append(cerBean.getCfc_tcr_id()).append("</cfc_tcr_id>");
						result.append("<status>").append(cerBean.getCfc_status()).append("</status>");
						result.append("<cfc_create_datetime>").append(cerBean.getCfc_create_datetime()).append("</cfc_create_datetime>");
						result.append("<cfc_create_user_id>").append(cwUtils.esc4XML(cerBean.getCfc_create_user_id())).append("</cfc_create_user_id>");
						result.append("<cfc_update_datetime>").append(cerBean.getCfc_update_datetime()).append("</cfc_update_datetime>");
						result.append("<cfc_update_user_id>").append(cwUtils.esc4XML(cerBean.getCfc_update_user_id())).append("</cfc_update_user_id>");
						result.append("<update_user>").append(cwUtils.esc4XML(cerBean.getCfc_update_user_id())).append("</update_user>");
						result.append("<update_timestamp>").append(cerBean.getCfc_update_datetime()).append("</update_timestamp>");
						result.append("<cfc_core>").append(cwUtils.esc4XML(cerBean.getCfc_code())).append("</cfc_core>");
						Timestamp ts = new Timestamp(System.currentTimeMillis());   
						int  cfc_end_datetime_id= 0;
						if(ts.after(cerBean.getCfc_end_datetime())){
							cfc_end_datetime_id =1;
						}
						result.append("<cfc_end_datetime id=\"").append(cfc_end_datetime_id).append("\" >").append(cerBean.getCfc_end_datetime()).append("</cfc_end_datetime>");
						try {
							String user  = usr.getUserName(con,cerBean.getCfc_create_user_id());
							result.append("<cfc_create_user_name>").append(cwUtils.esc4XML(user)).append("</cfc_create_user_name>");
							user  = usr.getUserName(con,cerBean.getCfc_update_user_id());
							result.append("<cfc_update_user_name>").append(cwUtils.esc4XML(user)).append("</cfc_update_user_name>");
							
						} catch (cwSysMessage e) {
							// TODO Auto-generated catch block
							CommonLog.error(e.getMessage(),e);
						}
						
							
						
						result.append("</certificate>");
				 }
				 count++;
			}
			result.append("</e_certificate>");
			// current training center
			DbTrainingCenter dbTrainingCenter = DbTrainingCenter.getInstance(con, tcr_id);
			result.append("<cur_training_center id=\"").append(tcr_id).append("\">");
			if(dbTrainingCenter == null) {
				result.append("<title/>");
			} else {
				result.append("<title>").append(cwUtils.esc4XML(dbTrainingCenter.getTcr_title())).append("</title>");
			}
			result.append("</cur_training_center>");
			cwPagination pagn = new cwPagination();
	        pagn.totalRec = count;
	        pagn.totalPage = (int)Math.ceil((float)count/page_size);
	        pagn.pageSize = page_size;
	        pagn.curPage = page;
	        pagn.sortCol = "";
	        pagn.sortOrder = "";
	        pagn.ts = null;
	        result.append(pagn.asXML());
			return result.toString();
		}
		public String getTcInfoByTcid(Connection con, long cert_tc_id) {
			String sql = "select tcr_title from tcTrainingCenter where tcr_id = ?";
	        StringBuffer xml = new StringBuffer();
	        PreparedStatement pstmt = null;
	        try {
	            pstmt = con.prepareStatement(sql);
	            pstmt.setLong(1, cert_tc_id);
	            ResultSet rs = pstmt.executeQuery();
	            while (rs.next()) {
	            	xml.append("<cur_training_center id=\"").append(cert_tc_id)
	            		.append("\"><title>").append(rs.getString("tcr_title")).append("</title></cur_training_center>");
	            }
	        } catch(SQLException e) {
	        	e.printStackTrace();
	        } finally {
	        	try {
					if (pstmt != null) {
					    pstmt.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
	        }
	        return xml.toString();
		}
}
