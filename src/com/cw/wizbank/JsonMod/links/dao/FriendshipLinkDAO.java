package com.cw.wizbank.JsonMod.links.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Vector;

import com.cw.wizbank.JsonMod.links.bean.FriendshipLinkBean;
import com.cw.wizbank.util.cwPagination;
import com.cw.wizbank.util.cwSQL;

public class FriendshipLinkDAO {

	public static final String FSLINK_STATUS_ON="ON";
	public static final String FSLINK_STATUS_OFF="OFF";

	public long ins(Connection con , FriendshipLinkBean fslink) throws SQLException{
		String sql=" insert into FriendshipLink(fsl_title, fsl_url, fsl_status, fsl_create_usr_id, fsl_create_timestamp, fsl_update_usr_id, fsl_update_timestamp)" +
				" values(?,?,?,?,?,?,?)" ;
		PreparedStatement stmt = null; 
		long  fsl_id = 0;
		try{
			stmt = con.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);
			int index=1;
			stmt.setString(index++, fslink.getFsl_title());
			stmt.setString(index++, fslink.getFsl_url());
			stmt.setString(index++, fslink.getFsl_status());
			stmt.setString(index++, fslink.getFsl_create_usr_id());
			stmt.setTimestamp(index++, fslink.getFsl_create_timestamp());
			stmt.setString(index++, fslink.getFsl_update_usr_id());
			stmt.setTimestamp(index++, fslink.getFsl_update_timestamp());
			stmt.executeUpdate();
			fsl_id= cwSQL.getAutoId(con, stmt, "friendshiplink", "fsl_id");
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
			return fsl_id;
		}

	public void upd(Connection con , FriendshipLinkBean fslink) throws SQLException{
		String sql=" update FriendshipLink set fsl_title = ?, fsl_url = ?, fsl_status = ?, fsl_update_usr_id = ?, fsl_update_timestamp = ? " +
				" where fsl_id = ?" ;
		PreparedStatement stmt = null; 
		try {
			stmt = con.prepareStatement(sql);
			int index=1;
			stmt.setString(index++, fslink.getFsl_title());
			stmt.setString(index++, fslink.getFsl_url());
			stmt.setString(index++, fslink.getFsl_status());
			stmt.setString(index++, fslink.getFsl_update_usr_id());
			stmt.setTimestamp(index++, fslink.getFsl_update_timestamp());
			stmt.setLong(index++, fslink.getFsl_id());
			stmt.executeUpdate();
			stmt.close();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}

    public static void del(Connection con, long fslId) throws SQLException {
		String sql=" delete FriendshipLink where fsl_id = ?" ;
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(sql);
			int index = 1;
			stmt.setLong(index++, fslId);
			stmt.executeUpdate();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}
    
	public static Vector getAllFsLink(Connection con, cwPagination cwPage, boolean isLrn) throws SQLException{
		Vector fsLinkVec =  new Vector();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		if (cwPage.sortCol == null || cwPage.sortCol.length() == 0) {
			cwPage.sortCol = "fsl_update_timestamp";
		}
		if (cwPage.sortOrder == null || cwPage.sortOrder.length() == 0) {
			cwPage.sortOrder = "desc";
		}
		String sql="select fsl_id, fsl_title, fsl_url, fsl_status, usr_display_bil, fsl_update_timestamp from FriendshipLink, Reguser " +
				" where usr_id = fsl_update_usr_id" ;
	    if(isLrn){
	    	sql += " where fsl_status = ?";
	    }
	    sql += " order by " + cwPage.sortCol + " " + cwPage.sortOrder;
	    try {
		    stmt = con.prepareStatement(sql);
		    int index = 1; 
		    if(isLrn){
		    	stmt.setString(index, FSLINK_STATUS_ON);
		    }
		    rs = stmt.executeQuery();
		    while(rs.next()){
		    	FriendshipLinkBean fs_link= new FriendshipLinkBean();
		    	fs_link.setFsl_id(rs.getLong("fsl_id"));
		    	fs_link.setFsl_title(rs.getString("fsl_title"));
		    	fs_link.setFsl_url(rs.getString("fsl_url"));
		    	fs_link.setFsl_status(rs.getString("fsl_status"));
		    	fs_link.setUpdate_usr_display_bil(rs.getString("usr_display_bil"));
		    	fs_link.setFsl_update_timestamp(rs.getTimestamp("fsl_update_timestamp"));
	    		fsLinkVec.add(fs_link);
		    }
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return fsLinkVec;
	}

	public static FriendshipLinkBean getLinkByID(Connection con, long fsl_id) throws SQLException {
		FriendshipLinkBean fs_link= new FriendshipLinkBean();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql="select fsl_title, fsl_url, fsl_status, fsl_update_timestamp from FriendshipLink " +
				" where fsl_id = ?" ;

	    try {
		    stmt = con.prepareStatement(sql);
		    int index = 1; 
		    stmt.setLong(index, fsl_id);
		    rs = stmt.executeQuery();
		    while(rs.next()){
		    	
		    	fs_link.setFsl_title(rs.getString("fsl_title"));
		    	fs_link.setFsl_url(rs.getString("fsl_url"));
		    	fs_link.setFsl_status(rs.getString("fsl_status"));
		    	fs_link.setFsl_update_timestamp(rs.getTimestamp("fsl_update_timestamp"));
		    }
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return fs_link;
	}
	
	public boolean isLinkTitleExist(Connection con, String fsl_title, long fsl_id ) throws SQLException{
	    boolean exist = false;
		String sql="select * from FriendshipLink where fsl_title =?";
		if(fsl_id > 0){
			sql = sql + " and fsl_id <> ?";
		}
		PreparedStatement stmt = null; 
		ResultSet rs = null;
	    try {
			stmt = con.prepareStatement(sql);
		    int index=1;
		    stmt.setString(index ++, fsl_title);
		    if(fsl_id > 0){
		    	stmt.setLong(index++, fsl_id);
		    }
		    rs = stmt.executeQuery();
		    if(rs.next()){
		    	exist = true;
		    }
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
	    return exist;
	}

	public boolean isLinkIdExist(Connection con, long fsl_id, Timestamp upd_time)throws SQLException{
		boolean exist = false;
		String sql = "select * from FriendshipLink where fsl_id = ? and fsl_update_timestamp = ?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
		    stmt = con.prepareStatement(sql);
		    int index=1;
		    stmt.setLong(index++, fsl_id);
		    stmt.setTimestamp(index++, upd_time);
		    rs = stmt.executeQuery();
		    if(rs.next()){
		    	exist=true;
		    }
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return exist;		
	}
	
	public static Vector getLinkList(Connection con,int max_size) throws SQLException {
		Vector linkVec = new Vector();
		
		String sql = "select fsl_title, fsl_url from FriendshipLink where fsl_status = ? order by fsl_update_timestamp desc";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(sql);
			int index = 1;
			stmt.setString(index++, FSLINK_STATUS_ON);
			rs = stmt.executeQuery();
			while(rs.next()) {
				FriendshipLinkBean fs_link= new FriendshipLinkBean();
				fs_link.setFsl_title(rs.getString("fsl_title"));
		    	fs_link.setFsl_url(rs.getString("fsl_url"));
		    	if(linkVec.size()<max_size){
		    		linkVec.add(fs_link);
		    	}
			}
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		
		return linkVec;
	}
	
}
