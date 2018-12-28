package com.cw.wizbank.JsonMod.links;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;

import com.cw.wizbank.JsonMod.links.bean.FriendshipLinkBean;
import com.cw.wizbank.JsonMod.links.dao.FriendshipLinkDAO;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.util.cwPagination;
import com.cw.wizbank.util.cwUtils;

public class Link {

	public static final String FTN_FRIENDSHIP_LINK_MAIN="FS_LINK_MAIN";
	
	public String getAllFslinksAsXml(Connection con, cwPagination cwPage, boolean isLrn) throws SQLException {
		StringBuffer result = new StringBuffer();
		FriendshipLinkBean fs_link= null;
		Vector fsLinkVec = FriendshipLinkDAO.getAllFsLink(con, cwPage, false);
		result.append("<fs_links>");
		for (int i=0; i<fsLinkVec.size(); i++) {
			fs_link = (FriendshipLinkBean)fsLinkVec.get(i);
			result.append("<link id=\"").append(fs_link.getFsl_id()).append("\">")
			      .append("<title>").append(cwUtils.esc4XML(fs_link.getFsl_title())).append("</title>")
			      .append("<url>").append(cwUtils.esc4XML(fs_link.getFsl_url())).append("</url>")
			      .append("<status>").append(fs_link.getFsl_status()).append("</status>")
			      .append("<update_user>").append(cwUtils.esc4XML(fs_link.getUpdate_usr_display_bil())).append("</update_user>")
			      .append("<update_timestamp>").append(fs_link.getFsl_update_timestamp()).append("</update_timestamp>")
			      .append("</link>");
		}
		result.append("</fs_links>");
		result.append(cwPage.asXML());
		return result.toString();
	}

	public String getFsLinkInfoXml(Connection con, LinksModuleParam modParam) throws SQLException {
		StringBuffer result = new StringBuffer();
		FriendshipLinkBean fs_link= FriendshipLinkDAO.getLinkByID(con, modParam.getFsl_id());
		if(fs_link != null){
			result.append("<link id=\"").append(modParam.getFsl_id()).append("\">")
		      .append("<title>").append(cwUtils.esc4XML(fs_link.getFsl_title())).append("</title>")
		      .append("<url>").append(cwUtils.esc4XML(fs_link.getFsl_url())).append("</url>")
		      .append("<status>").append(fs_link.getFsl_status()).append("</status>")
		      .append("<update_timestamp>").append(fs_link.getFsl_update_timestamp()).append("</update_timestamp>")
		      .append("</link>");
		}
		return result.toString();
	}

	public long addLink(Connection con, loginProfile prof, LinksModuleParam modParam) throws SQLException {
		FriendshipLinkDAO linkDAO= new FriendshipLinkDAO();
		FriendshipLinkBean link= new FriendshipLinkBean();
		link.setFsl_title(modParam.getFsl_title());
		link.setFsl_url(modParam.getFsl_url());
		link.setFsl_status(modParam.getFsl_status());
		link.setFsl_create_usr_id(prof.usr_id);
		link.setFsl_create_timestamp(modParam.getCur_time());
		link.setFsl_update_usr_id(prof.usr_id);
		link.setFsl_update_timestamp(modParam.getCur_time());
		long cur_fsl_id = linkDAO.ins(con, link);
		return cur_fsl_id;
	}

	public void updLink(Connection con, loginProfile prof, LinksModuleParam modParam) throws SQLException {
		FriendshipLinkDAO linkDAO= new FriendshipLinkDAO();
		FriendshipLinkBean link= new FriendshipLinkBean();
		link.setFsl_title(modParam.getFsl_title());
		link.setFsl_url(modParam.getFsl_url());
		link.setFsl_status(modParam.getFsl_status());
		link.setFsl_update_usr_id(prof.usr_id);
		link.setFsl_update_timestamp(modParam.getCur_time());
		link.setFsl_id(modParam.getFsl_id());
		linkDAO.upd(con, link);
	} 	
}