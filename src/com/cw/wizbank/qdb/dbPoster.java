package com.cw.wizbank.qdb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwUtils;

public class dbPoster {
	public static final String POSTER_STATUS_ON="ON";
	public static final String POSTER_STATUS_OFF="OFF";
	public static final String SITE_POSTER_PATHNAME = "poster";
	public static final String SITE_POSTER_LOGINBG_PATHNAME = "loginPage";
	public static final String SITE_POSTER_GUIDE_PATHNAME = "guide";
	public long sp_ste_id;
	public String sp_media_file;
	public String sp_url;
	public String sp_status;
	public boolean sp_keep_media;
	public long sp_tcr_id; 
	
	public String sp_media_file1;
	public String sp_url1;
	public String sp_status1;	
	public boolean sp_keep_media1;
	
	public String sp_media_file2;
	public String sp_url2;
	public String sp_status2;
	public boolean sp_keep_media2;
	
	public String sp_media_file3;
	public String sp_url3;
	public String sp_status3;
	public boolean sp_keep_media3;
	
	public String sp_media_file4;
	public String sp_url4;
	public String sp_status4;
	public boolean sp_keep_media4;
	
	public String sp_logo_file_cn;	//简体中文板Logo
	public boolean sp_keep_logo_cn;
	
	public String sp_logo_file_hk;	//繁体中文板Logo
	public boolean sp_keep_logo_hk;
	
	public String sp_logo_file_us;	//英文板Logo
	public boolean sp_keep_logo_us;
	
	public boolean sp_login_show_header_ind;
	public boolean sp_all_show_footer_ind;
	
	public String login_bg_video; //登陆页视频路径
	public boolean islogin_bg_video; 	
	public String login_bg_type; // PIC : 图片背景； VOD：视频
	public boolean islogin_bg_type;
	
	public String login_bg_file1;
	public boolean islogin_bg_file1;
	public String login_bg_file2;
	public boolean islogin_bg_file2;
	public String login_bg_file3;
	public boolean islogin_bg_file3;
	public String login_bg_file4;
	public boolean islogin_bg_file4;
	public String login_bg_file5;	
	public boolean islogin_bg_file5;
	
	public String guide_file1;
	public boolean isGuide_file1;
	public String guide_file2;
	public boolean isGuide_file2;	
	public String guide_file3;
	public boolean isGuide_file3;
	
	public String sp_welcome_word;
	public String mb_welcome_word;
	
	public void ins(Connection con, String rpt_type, boolean isADM) throws SQLException{
		String sql=" insert into SitePoster(sp_ste_id, sp_media_file, sp_url, sp_status,sp_media_file1, sp_url1, "
				+ "	sp_status1,sp_media_file2, sp_url2, sp_status2,sp_media_file3, sp_url3, sp_status3,sp_media_file4,"
				+ " sp_url4, sp_status4, sp_mobile_ind, sp_logo_file_cn, sp_logo_file_hk, sp_logo_file_us, sp_tcr_id,"
				+ " login_bg_file1,login_bg_file2,login_bg_file3,login_bg_file4,login_bg_file5,guide_file1,guide_file2,"
				+ " guide_file3,sp_welcome_word,mb_welcome_word,login_bg_video,login_bg_type";
				if(isADM){
					sql += ",sp_login_show_header_ind,sp_all_show_footer_ind";
				}
				sql += ") values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?";
				if(isADM){
					sql += ",?,?";
				}
				sql += ")" ;
		PreparedStatement stmt = null; 
		try{
			stmt = con.prepareStatement(sql);
			int index=1;
			stmt.setLong(index++,   sp_ste_id);
			stmt.setString(index++, sp_media_file);
			stmt.setString(index++, sp_url);
			stmt.setString(index++, sp_status);
			stmt.setString(index++, sp_media_file1);
			stmt.setString(index++, sp_url1);
			stmt.setString(index++, sp_status1);
			stmt.setString(index++, sp_media_file2);
			stmt.setString(index++, sp_url2);
			stmt.setString(index++, sp_status2);
			stmt.setString(index++, sp_media_file3);
			stmt.setString(index++, sp_url3);
			stmt.setString(index++, sp_status3);
			stmt.setString(index++, sp_media_file4);
			stmt.setString(index++, sp_url4);
			stmt.setString(index++, sp_status4);
			//判断是否是手机端管理
			if(rpt_type.equalsIgnoreCase("MOBILE_POSTER_MAIN") || rpt_type.equalsIgnoreCase("FTN_AMD_MOBILE_POSTER_MAIN")){
		    	stmt.setInt(index++, 1);
		    }else{
		    	stmt.setInt(index++, 0);
		    }
			stmt.setString(index++, sp_logo_file_cn);
			stmt.setString(index++, sp_logo_file_hk);
			if(sp_logo_file_us == null){
				stmt.setString(index++, "");
			}else{
				stmt.setString(index++, sp_logo_file_us);
			}
			
			stmt.setLong(index++, sp_tcr_id);
			stmt.setString(index++, login_bg_file1);
			stmt.setString(index++, login_bg_file2);
			stmt.setString(index++, login_bg_file3);
			stmt.setString(index++, login_bg_file4);
			stmt.setString(index++, login_bg_file5);
			stmt.setString(index++, guide_file1);
			stmt.setString(index++, guide_file2);
			stmt.setString(index++, guide_file3);
			stmt.setString(index++, sp_welcome_word);
			stmt.setString(index++, mb_welcome_word);
			stmt.setString(index++, login_bg_video);
			stmt.setString(index++, login_bg_type);
			if(isADM){
				stmt.setBoolean(index++, sp_login_show_header_ind);
				stmt.setBoolean(index++, sp_all_show_footer_ind);
			}
			stmt.executeUpdate();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}

	public void upd(Connection con, String rpt_type, boolean isADM) throws SQLException{
		String sql=" update SitePoster set sp_media_file = ?, sp_url = ?, sp_status = ? ," ;
			   sql += " sp_media_file1 = ?, sp_url1 = ?, sp_status1 = ? ," ;
			   sql += " sp_media_file2 = ?, sp_url2 = ?, sp_status2 = ? ," ;
			   sql += " sp_media_file3 = ?, sp_url3 = ?, sp_status3 = ? ," ;
			   sql += " sp_media_file4 = ?, sp_url4 = ?, sp_status4 = ? ," ;
			   
			   if(sp_logo_file_cn != null && sp_logo_file_cn.trim().length() > 0){
				   sql += " sp_logo_file_cn = ?, ";
			   }
			   if(sp_logo_file_hk != null && sp_logo_file_hk.trim().length() > 0){
				   sql += " sp_logo_file_hk = ?, ";
			   }
			   if(sp_logo_file_us != null && sp_logo_file_us.trim().length() > 0){
				   sql += " sp_logo_file_us = ?, ";
			   }
//			   sql += " sp_logo_file_cn = ?, sp_logo_file_hk = ?, sp_logo_file_us = ?, ";
			   sql += " login_bg_file1 = ?, login_bg_file2 = ?, login_bg_file3 = ?, login_bg_file4 = ?,";
			   sql += " login_bg_file5 = ?,login_bg_video = ?, ";
			   sql += "  guide_file1 = ?, guide_file2 = ?, guide_file3 = ?, sp_welcome_word = ?, ";
			   sql += " mb_welcome_word = ? , login_bg_type = ? ";
			   
			   if(isADM){
				   sql += ", sp_login_show_header_ind = ?, sp_all_show_footer_ind = ? ";
			   }
			   sql += " where sp_ste_id = ? and sp_mobile_ind = ? and sp_tcr_id = ? " ;
		PreparedStatement stmt = null; 
		try {
			stmt = con.prepareStatement(sql);
			int index=1;
			stmt.setString(index++, sp_media_file);
			stmt.setString(index++, sp_url);
			stmt.setString(index++, sp_status);
			stmt.setString(index++, sp_media_file1);
			stmt.setString(index++, sp_url1);
			stmt.setString(index++, sp_status1);
			stmt.setString(index++, sp_media_file2);
			stmt.setString(index++, sp_url2);
			stmt.setString(index++, sp_status2);
			stmt.setString(index++, sp_media_file3);
			stmt.setString(index++, sp_url3);
			stmt.setString(index++, sp_status3);
			stmt.setString(index++, sp_media_file4);
			stmt.setString(index++, sp_url4);
			stmt.setString(index++, sp_status4);
			
			if(sp_logo_file_cn != null && sp_logo_file_cn.trim().length() > 0){
				stmt.setString(index++, sp_logo_file_cn);
		    }
		    if(sp_logo_file_hk != null && sp_logo_file_hk.trim().length() > 0){
			   stmt.setString(index++, sp_logo_file_hk);
		    }
		    if(sp_logo_file_us != null && sp_logo_file_us.trim().length() > 0){
			   stmt.setString(index++,  sp_logo_file_us);
		    }
			stmt.setString(index++, login_bg_file1);
			stmt.setString(index++, login_bg_file2);
			stmt.setString(index++, login_bg_file3);
			stmt.setString(index++, login_bg_file4);
	    	stmt.setString(index++, login_bg_file5);
			stmt.setString(index++, login_bg_video);
			stmt.setString(index++, guide_file1);
			stmt.setString(index++, guide_file2);
			stmt.setString(index++, guide_file3);
			stmt.setString(index++, sp_welcome_word);
			stmt.setString(index++, mb_welcome_word);
			stmt.setString(index++, login_bg_type);
			if(isADM){
				stmt.setBoolean(index++, sp_login_show_header_ind);
				stmt.setBoolean(index++, sp_all_show_footer_ind);
			}
			stmt.setLong(index++,   sp_ste_id);
			if(rpt_type.equalsIgnoreCase("FTN_AMD_POSTER_MAIN")){
		    	stmt.setInt(index++, 0);
		    } else if(rpt_type.equalsIgnoreCase("FTN_AMD_MOBILE_POSTER_MAIN")){
		    	stmt.setInt(index++, 1);
		    }
			stmt.setLong(index++, sp_tcr_id);
			stmt.executeUpdate();
			stmt.close();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}

	public boolean isPosterExist(Connection con, String rpt_type)throws SQLException{
		boolean exist = false;
		String sql = "select * from SitePoster where sp_ste_id = ? and sp_tcr_id = ? and sp_mobile_ind = ? ";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
		    stmt = con.prepareStatement(sql);
		    int index=1;
		    stmt.setLong(index++, sp_ste_id);
		    stmt.setLong(index++, sp_tcr_id);
		    if(rpt_type.equalsIgnoreCase("FTN_AMD_POSTER_MAIN")){
		    	stmt.setInt(index++, 0);
		    } else if(rpt_type.equalsIgnoreCase("FTN_AMD_MOBILE_POSTER_MAIN")){
		    	stmt.setInt(index++, 1);
		    }
		    rs = stmt.executeQuery();
		    if(rs.next()){
		    	exist=true;
		    }
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return exist;		
	}	

	public static String getPosterInfoXml(Connection con, long ste_id, String rpt_type, long tcr_id) throws SQLException {
		StringBuffer result = new StringBuffer();
		String sql = " select sp_media_file, sp_url, sp_status,sp_media_file1, sp_url1, sp_status1, sp_media_file2, "
					+ " sp_url2, sp_status2, sp_media_file3, sp_url3, sp_status3, sp_media_file4, sp_url4, sp_status4, "
					+ " sp_logo_file_cn, sp_logo_file_hk, sp_logo_file_us,login_bg_file1,login_bg_file2,login_bg_file3,login_bg_file4,login_bg_file5"
					+ " , guide_file1, guide_file2, guide_file3, sp_welcome_word, mb_welcome_word, sp_login_show_header_ind, sp_all_show_footer_ind,login_bg_type,login_bg_video "
					+ " from SitePoster where sp_ste_id = ? and sp_mobile_ind = ? and sp_tcr_id = ? ";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		result.append("<poster ste_id=\"").append(ste_id).append("\">");
		try {
		    stmt = con.prepareStatement(sql);
		    int index=1;
		    stmt.setLong(index++, ste_id);
		    if(rpt_type.equalsIgnoreCase("FTN_AMD_POSTER_MAIN")){
		    	stmt.setInt(index++, 0);
		    } else if(rpt_type.equalsIgnoreCase("FTN_AMD_MOBILE_POSTER_MAIN")){
		    	stmt.setInt(index++, 1);
		    }else{
		    	stmt.setInt(index++, -1);
		    }
		    stmt.setLong(index++, tcr_id);
		    rs = stmt.executeQuery();
		     
		    if(rs.next()){
		    	result.append("<logo_file_cn>").append(cwUtils.esc4XML(rs.getString("sp_logo_file_cn"))).append("</logo_file_cn>")
		    		.append("<logo_file_hk>").append(cwUtils.esc4XML(rs.getString("sp_logo_file_hk"))).append("</logo_file_hk>")
		    		.append("<logo_file_us>").append(cwUtils.esc4XML(rs.getString("sp_logo_file_us"))).append("</logo_file_us>");
		    	
		    	result.append("<media_file>").append(cwUtils.esc4XML(rs.getString("sp_media_file"))).append("</media_file>")
			      .append("<url>").append(cwUtils.esc4XML(rs.getString("sp_url"))).append("</url>")
			      .append("<status>").append(rs.getString("sp_status")).append("</status>")
			      .append("<rpt_type>").append(rpt_type).append("</rpt_type>");
		    	
		        for (int i = 1; i <=4; i++) {
			    	result.append("<media_file_").append(i).append(">").append(cwUtils.esc4XML(rs.getString("sp_media_file"+i))).append("</media_file_").append(i).append(">")
				      .append("<url_").append(i).append(">").append(cwUtils.esc4XML(rs.getString("sp_url"+i))).append("</url_").append(i).append(">")
				      .append("<status_").append(i).append(">").append(rs.getString("sp_status"+i)).append("</status_").append(i).append(">");
				}		       
		        for(int i=1;i<6;i++){
		        	result.append("<login_bg_file").append(i).append(">").append(cwUtils.esc4XML(rs.getString("login_bg_file"+i)))
		        	.append("</login_bg_file").append(i).append(">");
		        }
		        for(int i=1; i<4; i++){
		        	result.append("<guide_file").append(i).append(">").append(cwUtils.esc4XML(rs.getString("guide_file"+i)))
		        	.append("</guide_file").append(i).append(">");
		        }
		        result.append("<sp_welcome_word>").append(cwUtils.esc4XML(rs.getString("sp_welcome_word"))).append("</sp_welcome_word>");
		        result.append("<mb_welcome_word>").append(cwUtils.esc4XML(rs.getString("mb_welcome_word"))).append("</mb_welcome_word>");
		        result.append("<login_bg_video>").append(cwUtils.esc4XML(rs.getString("login_bg_video"))).append("</login_bg_video>");
		        result.append("<login_bg_type>").append(cwUtils.esc4XML(rs.getString("login_bg_type"))).append("</login_bg_type>");
		        result.append("<show_header_ind>").append(rs.getBoolean("sp_login_show_header_ind")).append("</show_header_ind>");
		        result.append("<show_footer_ind>").append(rs.getBoolean("sp_all_show_footer_ind")).append("</show_footer_ind>");
		    }
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
	    result.append("</poster>");
		return result.toString();
	}
	
	public Vector<SitePoster> getPoster(Connection con) throws SQLException {
		return getPoster(con, false);
	}
	
	public Vector<SitePoster> getPoster(Connection con, boolean isMobile) throws SQLException {
		Vector<SitePoster> vec = new Vector<SitePoster>();

		String sql = "select sp_media_file, sp_url, sp_status, ";
		sql += " sp_media_file1, sp_url1, sp_status1, ";
		sql += " sp_media_file2, sp_url2, sp_status2, ";
		sql += " sp_media_file3, sp_url3, sp_status3, ";
		sql += " sp_media_file4, sp_url4, sp_status4 ";
		sql += " from SitePoster where sp_ste_id = ? ";
		if(isMobile){
			sql += "and sp_mobile_ind = 1";
		}else{
			sql += "and sp_mobile_ind = 0";
		}
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(sql);
			int index = 1;
			stmt.setLong(index++, this.sp_ste_id);
			rs = stmt.executeQuery();
			while (rs.next()) {
				for (int i = 0; i <= 4; i++) {
					String fld_sp_status = "sp_status";
					String fld_sp_media_file = "sp_media_file";
					String fld_sp_url = "sp_url";
					if (i > 0) {
						fld_sp_status = "sp_status" + i;
						fld_sp_media_file = "sp_media_file" + i;
						fld_sp_url = "sp_url" + i;
					}

					if (rs.getString(fld_sp_status) != null && rs.getString(fld_sp_status).equals(POSTER_STATUS_ON)) {
						if (rs.getString(fld_sp_media_file) != null) {
							SitePoster p = new SitePoster();
							p.setSp_media_file(rs.getString(fld_sp_media_file));
							p.setSp_url(rs.getString(fld_sp_url));
							
							vec.add(p);
						}
					}
				}
			}
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return vec;
	}
	
	//该宣传栏是否已存在
	public static boolean checkPosterInd(Connection con, long ste_id, long tcr_id, String rpt_type) throws SQLException{
		boolean has_poster_ind = false;
		String sql = "select * from SitePoster where sp_ste_id = ? and sp_tcr_id = ?  and sp_mobile_ind = ? ";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int index = 1;
		try{
			stmt = con.prepareStatement(sql);
			stmt.setLong(index++, ste_id);
			stmt.setLong(index++, tcr_id);
			if(rpt_type.equalsIgnoreCase("FTN_AMD_POSTER_MAIN")){
		    	stmt.setInt(index++, 0);
		    } else if(rpt_type.equalsIgnoreCase("FTN_AMD_MOBILE_POSTER_MAIN")){
		    	stmt.setInt(index++, 1);
		    }
			rs = stmt.executeQuery();
			if(rs.next()){
				has_poster_ind = true;
			}
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return has_poster_ind;
	}
	
	public static void getHeaderAndFooterSetting(Connection con, WizbiniLoader wizbini) throws SQLException {
    	String sql = " select sp_login_show_header_ind, sp_all_show_footer_ind from sitePoster where sp_ste_id = 1 ";
    	PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			if(rs.next()){
				wizbini.show_login_header_ind = rs.getBoolean("sp_login_show_header_ind");
				wizbini.show_all_footer_ind = rs.getBoolean("sp_all_show_footer_ind");
			}
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
    }
	
}