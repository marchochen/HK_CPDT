/**
 * 
 */
package com.cw.wizbank.article;

import java.io.Reader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.cw.wizbank.accesscontrol.AcMessage;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.db.DbTrainingCenter;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.util.cwPagination;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.entity.Article;
import com.cwn.wizbank.entity.RegUser;
import com.cwn.wizbank.entity.TcTrainingCenter;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.utils.CommonLog;

/**
 * @author Leon.li 2013-5-24 5:15:05 message Article module dao support
 */
public class ArticleDao {

	/**
	 * get all the Article object from database
	 * 
	 * @param con
	 *            : database resource
	 * @param cwPage
	 * @param isLrn
	 * @return
	 * @throws SQLException
	 */
	public List<Article> list(Connection con, ArticleModuleParam param, loginProfile prof, String term) throws SQLException {
		List<Article> list = new ArrayList<Article>();
		String[] tcr_id_array = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		cwPagination cwPage = param.getCwPage();

		if (cwPage.sortCol == null || cwPage.sortCol.length() == 0) {
			cwPage.sortCol = "art_create_datetime";
		}
		if (cwPage.sortOrder == null || cwPage.sortOrder.length() == 0) {
			cwPage.sortOrder = "desc";
		}

		StringBuffer sql = new StringBuffer("select");
		sql.append(" art_id,");
		sql.append(" art_title,");
		sql.append(" art_introduction,");
		sql.append(" art_keywords,");
		sql.append(" art_content,");
		sql.append(" art_user_id,");
		sql.append(" art_create_datetime,");
		sql.append(" art_update_datetime,");
		sql.append(" art_location,");
		sql.append(" art_type,");
		sql.append(" art_status,");
		sql.append(" art_tcr_id,");
		sql.append(" art_push_mobile,");
		sql.append(" art_is_html,");
		sql.append(" art_update_user_id,");
		sql.append(" usr_display_bil,");
		sql.append(" aty_title");
		sql.append(" from article, regUser, articleType");
		sql.append(" where art_user_id = usr_ent_id and usr_status <> 'DELETED' and aty_id = art_type ");

		String search_by_tc = "";
        if (!AccessControlWZB.isRoleTcInd(prof.current_role)) {
        	search_by_tc = "no_filter";
        } else {
        	search_by_tc = "ta_filter";
        }

        if (search_by_tc.equalsIgnoreCase("ta_filter")) {
			sql.append(" and art_tcr_id in (")
			   .append(" select distinct tcr_id ")
			   .append(" from tcTrainingCenter, tcTrainingCenterOfficer left join tcRelation on (tco_tcr_id = tcn_ancestor) ")
			   .append(" where tco_usr_ent_id = ? ")
			   .append(" and (tcn_child_tcr_id = tcr_id or tco_tcr_id = tcr_id) ")
			   .append(" and tcr_status = ? ")
			   .append(")");
		}else{
			sql.append(" and art_tcr_id in (")
			   .append(" select distinct tcr_id ")
			   .append(" from tcTrainingCenter  left join tcRelation on (tcr_id = tcn_child_tcr_id) ")
			   .append(" where tcn_ancestor in ( ")
			   .append(" select distinct tcn_ancestor from tcRelation where tcn_child_tcr_id in ")
			   .append(" (select tco_tcr_id from tcTrainingCenterOfficer where tco_usr_ent_id = ?) or tcn_ancestor = ?")
			   .append(" ) ")
			   .append(" or tcr_id = ? and tcr_status = ? ")
			   .append(")");
		}
		
		if(param.getArt_tcr_id() > 0) {
			sql.append(" and art_tcr_id = ?");
		}
		
		if(param.getArt_type()!=null && param.getArt_type().trim()!="" && !param.getArt_type().trim().equals("0") ) {
			sql.append(" and art_type = ?");
		}
		
		if(param.getArt_keywords()!=null) {
			String art_keywords = null;
			try {
				art_keywords = new String( param.getArt_keywords()).trim();
			} catch (Exception e) {
				CommonLog.error(e.getMessage(),e);
			}
			sql.append(" and ( art_title like N\'%").append(art_keywords).append("%\' or usr_display_bil like N\'%").append(art_keywords)
			   .append("%\' or art_keywords like N\'%").append(art_keywords).append("%\' ) ") ;
		}
		
		sql.append(" order by " + cwPage.sortCol + " " + cwPage.sortOrder);
		try {
			stmt = con.prepareStatement(sql.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			int index = 1;
			if (search_by_tc.equalsIgnoreCase("ta_filter")) {
				stmt.setLong(index++, prof.usr_ent_id);
				stmt.setString(index++, "OK");
			}else{
				stmt.setLong(index++, prof.usr_ent_id);
				stmt.setLong(index++, prof.my_top_tc_id);
				stmt.setLong(index++, prof.my_top_tc_id);
				stmt.setString(index++, "OK");
			}
			
			if(param.getArt_tcr_id() > 0) {
				stmt.setInt(index++, param.getArt_tcr_id());
			}

			if(param.getArt_type()!=null && param.getArt_type().trim()!="" && !param.getArt_type().trim().equals("0") ) {
				stmt.setString(index++, param.getArt_type());
			}
				
			rs = stmt.executeQuery();
			int cnt = 0;
			rs.last();
			cwPage.totalRec = rs.getRow();
			rs.beforeFirst();
			Article art;
			while (rs.next()) {
				cnt++;
				if ((cnt > (cwPage.curPage - 1) * cwPage.pageSize && cnt <= (cwPage.curPage) * cwPage.pageSize)) {
					art = new Article();
					art.setArt_content(rs.getString("art_content"));
					art.setArt_create_datetime(rs.getTimestamp("art_create_datetime"));
					art.setArt_id(rs.getInt("art_id"));
					art.setArt_introduction(rs.getString("art_introduction"));
					art.setArt_is_html(rs.getInt("art_is_html"));
					art.setArt_keywords(rs.getString("art_keywords"));
					art.setArt_location(rs.getString("art_location"));
					art.setArt_push_mobile(rs.getInt("art_push_mobile"));
					art.setArt_status(rs.getInt("art_status"));
					art.setArt_tcr_id(rs.getInt("art_tcr_id"));
					art.setArt_title(rs.getString("art_title"));
					art.setArt_type(rs.getString("aty_title"));
					art.setArt_update_datetime(rs.getTimestamp("art_update_datetime"));
					art.setArt_update_user_id(rs.getInt("art_update_user_id"));
					art.setArt_user_id(rs.getInt("art_user_id"));
					RegUser user = new RegUser();
					user.setUsr_display_bil(rs.getString("usr_display_bil"));
					art.setUser(user);
					list.add(art);
				}
			}
		}finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return list;
	}

	/**
	 * is this data have in database?
	 * 
	 * @param con
	 * @param splId
	 * @param upd_time
	 * @return
	 * @throws SQLException
	 */
	public boolean isExist(Connection con, Integer splId, String del) throws SQLException {
		boolean exist = false;
		String sql = "select art_id from Article where art_id = ? ";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(sql);
			int index = 1;
			stmt.setLong(index++, splId);
			// stmt.setTimestamp(index++, upd_time);
			rs = stmt.executeQuery();
			if (rs.next()) {
				exist = true;
			}
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return exist;
	}

	/**
	 * insert data
	 * 
	 * @param con
	 * @param sp
	 * @return
	 * @throws SQLException
	 */
	public long insert(Connection con, Article art) throws SQLException {
		StringBuffer sql = new StringBuffer("INSERT INTO Article (");
		sql.append(" art_title,");
		sql.append(" art_icon_file,");
		sql.append(" art_introduction,");
		sql.append(" art_keywords,");
		sql.append(" art_content,");
		sql.append(" art_user_id,");
		sql.append(" art_create_datetime,");
		sql.append(" art_update_datetime,");
		sql.append(" art_location,");
		sql.append(" art_type,");
		sql.append(" art_status,");
		sql.append(" art_tcr_id,");
		sql.append(" art_push_mobile,");
		sql.append(" art_is_html,");
		sql.append(" art_update_user_id");
		sql.append(" ) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

		PreparedStatement stmt = null;
		Timestamp curTime = cwSQL.getTime(con);
		long count = 0;
		try {
			stmt = con.prepareStatement(sql.toString(), PreparedStatement.RETURN_GENERATED_KEYS);
			int index = 1;
			stmt.setString(index++, art.getArt_title());
			stmt.setString(index++, art.getArt_icon_file());
			stmt.setString(index++, art.getArt_introduction());
			stmt.setString(index++, art.getArt_keywords());
			//stmt.setString(index++, art.getArt_content());
			//插入clob类型
			Reader clobReader = new StringReader(art.getArt_content()); // 将Str转成流形式  
	        stmt.setCharacterStream(index++, clobReader, art.getArt_content().length());
	        
			stmt.setInt(index++, art.getArt_user_id());
			stmt.setTimestamp(index++, curTime);
			stmt.setTimestamp(index++, curTime);
			stmt.setString(index++, art.getArt_location());
			stmt.setString(index++, art.getArt_type());
			stmt.setInt(index++, art.getArt_status());
			stmt.setInt(index++, art.getArt_tcr_id());
			stmt.setInt(index++, art.getArt_push_mobile());
			stmt.setInt(index++, art.getArt_is_html());
			stmt.setInt(index++, art.getArt_update_user_id());
			
			stmt.executeUpdate();
			count = cwSQL.getAutoId(con, stmt, "Article", "art_id");
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
	public boolean updateDel(Connection con, Integer artId, String del) throws SQLException {
		String sql = "UPDATE Article SET art_status = ? WHERE  art_id = ?";
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(sql);
			int index = 1;
			stmt.setString(index++, del);
			stmt.setLong(index, artId);
			stmt.executeUpdate();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
		return true;
	}

	public boolean delete(Connection con, String[] artIds) throws SQLException {
		String sql = "DELETE FROM Article WHERE  art_id in ( ";
		StringBuffer paramStr = new StringBuffer();
		for(String str : artIds) {
			paramStr.append(str).append(",");
		}
		sql += paramStr.substring(0,paramStr.length()-1);
		sql += ")";
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(sql);
			stmt.executeUpdate();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
		return true;
	}

	/**
	 * update Article
	 * 
	 * @param con
	 * @param sp
	 * @throws SQLException
	 */
	public void update(Connection con, Article art) throws SQLException {
		StringBuffer sql = new StringBuffer("UPDATE Article");
		sql.append(" SET");
		sql.append(" art_title = ?,");
		sql.append(" art_icon_file = ?,");
		sql.append(" art_introduction = ?,");
		sql.append(" art_keywords =?,");
		sql.append(" art_content = ?,");
		sql.append(" art_user_id = ?,");
		sql.append(" art_update_datetime =?,");
		sql.append(" art_location = ?,");
		sql.append(" art_type = ?,");
		sql.append(" art_status = ?,");
		sql.append(" art_tcr_id = ?,");
		sql.append(" art_push_mobile = ?,");
		sql.append(" art_is_html = ?");
		sql.append(" WHERE art_id = ?");

		PreparedStatement stmt = null;
		Timestamp curTime = cwSQL.getTime(con);

		try {
			stmt = con.prepareStatement(sql.toString());
			int index = 1;
			stmt.setString(index++, art.getArt_title());
			stmt.setString(index++, art.getArt_icon_file());
			stmt.setString(index++, art.getArt_introduction());
			stmt.setString(index++, art.getArt_keywords());
			//stmt.setString(index++, art.getArt_content());
			//插入clob类型
			Reader clobReader = new StringReader(art.getArt_content()); // 将Str转成流形式  
	        stmt.setCharacterStream(index++, clobReader, art.getArt_content().length());
	        
			stmt.setInt(index++, art.getArt_user_id());
			stmt.setTimestamp(index++, curTime);
			stmt.setString(index++, art.getArt_location());
			stmt.setString(index++, art.getArt_type());
			stmt.setInt(index++, art.getArt_status());
			stmt.setInt(index++, art.getArt_tcr_id());
			stmt.setInt(index++, art.getArt_push_mobile());
			stmt.setInt(index++, art.getArt_is_html());
			
			stmt.setInt(index++, art.getArt_id());
			stmt.executeUpdate();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}

	public Article get(Connection con, int art_id, String del) throws SQLException {

		PreparedStatement stmt = null;
		ResultSet rs = null;
		int index = 1;

		StringBuffer sql = new StringBuffer("select");
		sql.append(" art_id,");
		sql.append(" art_title,");
		sql.append(" art_icon_file,");
		sql.append(" art_introduction,");
		sql.append(" art_keywords,");
		sql.append(" art_content,");
		sql.append(" art_user_id,");
		sql.append(" art_create_datetime,");
		sql.append(" art_update_datetime,");
		sql.append(" art_location,");
		sql.append(" art_type,");
		sql.append(" art_status,");
		sql.append(" art_tcr_id,");
		sql.append(" art_push_mobile,");
		sql.append(" art_is_html,");
		sql.append(" art_update_user_id,");
		sql.append(" usr_display_bil,");
		sql.append(" tcr_title");
		sql.append(" from article, regUser, tcTrainingCenter");
		sql.append(" where art_user_id = usr_ent_id and tcr_id = art_tcr_id ");
		sql.append(" and usr_status <> 'DELETED' and art_id = ?");
		Article art = null;
		try {
			stmt = con.prepareStatement(sql.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

			stmt.setInt(index++, art_id);
			rs = stmt.executeQuery();
			while (rs.next()) {
					art = new Article();
					art.setArt_content(rs.getString("art_content"));
					art.setArt_create_datetime(rs.getTimestamp("art_create_datetime"));
					art.setArt_id(rs.getInt("art_id"));
					art.setArt_introduction(rs.getString("art_introduction"));
					art.setArt_icon_file(rs.getString("art_icon_file"));
					art.setArt_is_html(rs.getInt("art_is_html"));
					art.setArt_keywords(rs.getString("art_keywords"));
					art.setArt_location(rs.getString("art_location"));
					art.setArt_push_mobile(rs.getInt("art_push_mobile"));
					art.setArt_status(rs.getInt("art_status"));
					art.setArt_tcr_id(rs.getInt("art_tcr_id"));
					art.setArt_title(rs.getString("art_title"));
					art.setArt_type(rs.getString("art_type"));
					art.setArt_update_datetime(rs.getTimestamp("art_update_datetime"));
					art.setArt_update_user_id(rs.getInt("art_update_user_id"));
					art.setArt_user_id(rs.getInt("art_user_id"));
					RegUser user = new RegUser();
					user.setUsr_display_bil(rs.getString("usr_display_bil"));
					art.setUser(user);
					TcTrainingCenter tcenter = new TcTrainingCenter();
					tcenter.setTcr_title(rs.getString("tcr_title"));
					art.setTcenter(tcenter);
					return art;
				}
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return art;
	}
	
	public String getArticleTypeXML(Connection con, loginProfile prof,ArticleModuleParam modParam, boolean isAll) throws SQLException {
		StringBuffer xml = new StringBuffer();
		xml.append("<aty_list>");
		String sql = " select aty_id, aty_title, aty_create_datetime, aty_update_datetime from articleType ";
		
		sql += "where aty_tcr_id = ?";
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		long curPage = modParam.getCwPage().curPage;
		long pageSize = modParam.getCwPage().pageSize;
		if(isAll){
			curPage = 1;
			pageSize = 9999;
		}
		try{
			stmt = con.prepareStatement(sql);
			stmt.setLong(1, prof.my_top_tc_id);
			rs = stmt.executeQuery();
			long count = 1;
			while(rs.next()){
				if(count > pageSize * (curPage - 1) && count <= pageSize * curPage){
					xml.append("<aty id=\"").append(rs.getString("aty_id")).append("\" title=\"").append(cwUtils.esc4XML(rs.getString("aty_title")))
						.append("\" create_date=\"").append(rs.getTimestamp("aty_create_datetime")).append("\" update_date=\"")
						.append(rs.getTimestamp("aty_update_datetime")).append("\" />");
				}
				count++;
				modParam.getCwPage().totalRec++;
			}
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		xml.append("</aty_list>");
		if(!isAll){
			xml.append("<page cur_page=\"").append(curPage).append("\" page_size=\"").append(pageSize).append("\" total_rec=\"")
				.append(modParam.getCwPage().totalRec).append("\" />");
		}
		return xml.toString();
	}
	
	public boolean checkArticleTypeExist(Connection con,loginProfile prof, String aty_title, long aty_id) throws SQLException {
		boolean article_type_exist = false;
		String sql = " select aty_id from articleType where aty_title = ?  and aty_tcr_id = ?";
		if(aty_id > 0 ){
			sql += " and aty_id <> ? ";
		}
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
			stmt = con.prepareStatement(sql);
			int index = 1;
			stmt.setString(index++, aty_title);

			stmt.setLong(index++, prof.my_top_tc_id);
			if(aty_id > 0 ){
				stmt.setLong(index++, aty_id);
			}
			rs = stmt.executeQuery();
			if(rs.next()){
				article_type_exist = true;
			}
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return article_type_exist;
	}
	
	public boolean checkArticleTypeExist(Connection con,loginProfile prof) throws SQLException {
		boolean article_type_exist = false;
		String sql = " select aty_id from articleType where aty_tcr_id = ?";

		PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
			stmt = con.prepareStatement(sql);
			int index = 1;

			stmt.setLong(index++, prof.my_top_tc_id);

			rs = stmt.executeQuery();
			if(rs.next()){
				article_type_exist = true;
			}
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return article_type_exist;
	}
	
	public long insertArticleType(Connection con, loginProfile prof, ArticleModuleParam modParam) throws SQLException {
		String sql = " insert into ArticleType(aty_title, aty_tcr_id, aty_create_user_id, aty_create_datetime)values(?,?,?,?)";
		PreparedStatement stmt = null;
		long count;
		try{
			Timestamp curTime = cwSQL.getTime(con);
			int index = 1;
			stmt = con.prepareStatement(sql);
			stmt.setString(index++, modParam.aty_title);
			stmt.setLong(index++, prof.my_top_tc_id);
			stmt.setString(index++, prof.usr_id);
			stmt.setTimestamp(index++, curTime);
			count = stmt.executeUpdate();
		} finally {
			cwSQL.cleanUp(null, stmt);
		}
		return count;
	}
	
	public String getArticleTypeDetail(Connection con, long aty_id) throws SQLException {
		StringBuffer xml = new StringBuffer();
		String sql = "select aty_id, aty_title, aty_tcr_id from ArticleType where aty_id = ? ";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
			int index = 1;
			stmt = con.prepareStatement(sql);
			stmt.setLong(index++, aty_id);
			rs = stmt.executeQuery();
			while(rs.next()){
				xml.append("<aty id=\"").append(rs.getLong("aty_id")).append("\" title=\"").append(rs.getString("aty_title"))
					.append("\" tcr_id=\"").append(rs.getLong("aty_tcr_id")).append("\" tcr_title=\"")
					.append(DbTrainingCenter.getTcrTitle(con, rs.getLong("aty_tcr_id"))).append("\" />");
			}
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return xml.toString();
	}
	
	public void updateArticleType(Connection con, loginProfile prof, ArticleModuleParam modParam)  throws SQLException {
		String sql = " update ArticleType set aty_title = ?, aty_update_user_id = ?, aty_update_datetime = ? where aty_id = ? ";
		PreparedStatement stmt = null;
		try{
			Timestamp curTime = cwSQL.getTime(con);
			int index = 1;
			stmt = con.prepareStatement(sql);
			stmt.setString(index++, modParam.aty_title);
			stmt.setString(index++, prof.usr_id);
			stmt.setTimestamp(index++, curTime);
			stmt.setLong(index++, modParam.aty_id);
			stmt.executeUpdate();
		} finally {
			cwSQL.cleanUp(null, stmt);
		}
	}
	
	public void deleteArticleType(Connection con, String aty_id_list) throws SQLException {
		String sql = " delete from ArticleType where aty_id in (" + aty_id_list + ")";
		PreparedStatement stmt = null;
		try{
			stmt = con.prepareStatement(sql);
			stmt.executeUpdate();
		} finally {
			cwSQL.cleanUp(null, stmt);
		}
	}
	
	public boolean isArticleTypeUse(Connection con, String aty_id_list) throws SQLException {
		boolean type_use_ind = false;
		String sql = " select * from articleType inner join article on art_type = aty_id where aty_id in (" + aty_id_list + ")";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			if(rs.next()){
				type_use_ind = true;
			}
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return type_use_ind;
	}
	

	public String getTitleById(Connection con, Long splId) throws SQLException {
		String title = null;
		String sql = "select art_title from Article where art_id = ? ";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(sql);
			int index = 1;
			stmt.setLong(index++, splId);
			rs = stmt.executeQuery();
			if (rs.next()) {
				title = rs.getString("art_title");
			}
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return title;
	}
}
