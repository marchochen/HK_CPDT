package com.cw.wizbank.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.apache.commons.lang.text.StrBuilder;

import com.cw.wizbank.accesscontrol.AcPageVariant;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.position.UserPositionReqParam;
import com.cw.wizbank.position.UserPostion;
import com.cw.wizbank.qdb.dbUserGroup;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbAction;
import com.cw.wizbank.util.EntityFullPath;
import com.cw.wizbank.util.cwPagination;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwUtils;

public class DbUserPosition {
	//获取岗位名称
    public static Hashtable getDisplayNames(Connection con ,String upt_id_lst)throws SQLException{
        String sql = "select upt_id, upt_title from UserPosition where upt_id in (" + upt_id_lst + ")";
        PreparedStatement stmt = con.prepareStatement(sql);

        ResultSet rs =stmt.executeQuery();
        Hashtable disHs =new Hashtable();
        while(rs.next()){
            disHs.put(rs.getLong("upt_id"), rs.getString("upt_title"));
        }
        if(stmt!=null)stmt.close();
        return disHs;   
    }
	
    public String getMetaXML(Connection con, loginProfile prof, WizbiniLoader wizbini, Hashtable xslQuestions, String stylesheet) throws SQLException{
    	AcPageVariant acPageVariant = new AcPageVariant(con);
        acPageVariant.instance_id = prof.usr_ent_id;
        acPageVariant.ent_id = prof.usr_ent_id;
        acPageVariant.rol_ext_id = prof.current_role;
        acPageVariant.root_id = prof.root_id;
        acPageVariant.tc_enable_ind = wizbini.cfgTcEnabled;
        acPageVariant.setWizbiniLoader(qdbAction.wizbini);
        return acPageVariant.answerPageVariantAsXML((String[]) xslQuestions.get(stylesheet));
    }
    
	public String getUptListXml(Connection con, cwPagination page, String search_info, loginProfile prof, boolean tc_independent) throws SQLException {
		if(page.curPage <= 0){
			page.curPage = 1;
		}
		if(page.pageSize <= 0){
			page.pageSize = 10;
		}
		List<UserPostion> upt_list = getUptList(con, page, search_info, prof, tc_independent);
		StringBuffer xml = new StringBuffer();
		xml.append("<upt_list cur_page=\"").append(page.curPage).append("\" page_size=\"").append(page.pageSize)
			.append("\" total=\"").append(page.totalRec).append("\">").append(cwUtils.NEWL);
		for(UserPostion upt : upt_list){
			xml.append("<upt code=\"").append(upt.getUpt_code()).append("\" title=\"").append(upt.getUpt_title())
				.append("\" upd_time=\"").append(upt.getPfs_update_time()).append("\"/>").append(cwUtils.NEWL);
		}
		xml.append("</upt_list>");
		return xml.toString();
	}
	
	//获取岗位列表
	public List<UserPostion> getUptList(Connection con, cwPagination page, String search_info, loginProfile prof, boolean tc_independent) throws SQLException{
		List<UserPostion> upt_list = new ArrayList<UserPostion>();
		String sql = " select upt_code, upt_title, upt_desc, pfs_update_time from UserPosition where 1 = 1 ";
		if(search_info != null){
			sql += " and (upt_code like ? or upt_title like ?) ";
		}
		//不是以系统管理进入岗位维护和设置了二级培训中心独立，就根据所管理的培训中心进行查询
		if(!prof.common_role_id.equalsIgnoreCase("ADM") && tc_independent){
			sql += " and (upt_tcr_id = ? or upt_tcr_id in (select tcn_child_tcr_id from tcrelation where tcn_ancestor = ?)) ";
		}
		sql += " order by pfs_update_time desc ";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int index = 1;
		try{
			stmt = con.prepareStatement(sql);
			if(search_info != null){
				stmt.setString(index++, "%" + search_info + "%");
				stmt.setString(index++, "%" + search_info + "%");
			}
			if(!prof.common_role_id.equalsIgnoreCase("ADM") && tc_independent){
				stmt.setLong(index++, prof.my_top_tc_id);
				stmt.setLong(index++, prof.my_top_tc_id);
			}
			rs = stmt.executeQuery();
			while(rs.next()){
				if(page.totalRec >= (page.curPage - 1) * page.pageSize && page.totalRec < page.curPage * page.pageSize){
					UserPostion upt = new UserPostion();
					upt.setUpt_code(rs.getString("upt_code"));
					upt.setUpt_title(rs.getString("upt_title"));
					upt.setUpt_desc(rs.getString("upt_desc"));
					upt.setPfs_update_time(rs.getTimestamp("pfs_update_time"));
					upt_list.add(upt);
				}
				page.totalRec++;
			}
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return upt_list;
	}
    
	//插入新岗位信息
	public long insertUserPosition(Connection con, UserPositionReqParam modParam, long usr_ent_id) throws SQLException{
		String sql = " insert into UserPosition (upt_code,upt_title,upt_desc,upt_tcr_id,pfs_update_usr_id,pfs_update_time) values (?,?,?,?,?,?) ";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Timestamp curTime = cwSQL.getTime(con);
		int index = 1;
		try{
			stmt = con.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);
			stmt.setString(index++, modParam.getUpt_code());
			stmt.setString(index++, modParam.getUpt_title());
			stmt.setString(index++, modParam.getUpt_desc());
			stmt.setString(index++, modParam.getUpt_tcr_id());
			stmt.setLong(index++, usr_ent_id);
			stmt.setTimestamp(index++, curTime);
			stmt.executeUpdate();
			return cwSQL.getAutoId(con, stmt, "UserPosition", "upt_id");
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
	}
	
	//检测岗位是否存在
	public boolean checkUserPosition(Connection con, String upt_code) throws SQLException{
		boolean upt_ind = false;
		String sql = " select * from UserPosition where upt_code = ? ";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
			stmt = con.prepareStatement(sql);
			stmt.setString(1, upt_code);
			rs = stmt.executeQuery();
			if(rs.next()){
				upt_ind = true;
			}
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return upt_ind;
	}
	
	public String getUserPositionDetailed(Connection con, String upt_code) throws SQLException{
		String sql = " select upt_code, upt_title, upt_tcr_id, tcr_title, upt_desc from UserPosition"
					+ " left join tcTrainingCenter on tcr_id = upt_tcr_id where upt_code = ? ";
		StringBuffer xml = new StringBuffer();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
			stmt = con.prepareStatement(sql);
			stmt.setString(1, upt_code);
			rs = stmt.executeQuery();
			if(rs.next()){
				xml.append("<upt upt_code=\"").append(rs.getString("upt_code")).append("\" upt_title=\"").append(rs.getString("upt_title"))
					.append("\" upt_tcr_id=\"").append(rs.getString("upt_tcr_id")).append("\" upt_tcr_title=\"").append(rs.getString("tcr_title"))
					.append("\" upt_desc=\"").append(rs.getString("upt_desc")).append("\"/>").append(cwUtils.NEWL);
			}
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return xml.toString();
	}
	
	//更新岗位信息
	public void updateUserPosition(Connection con, UserPositionReqParam modParam, long usr_ent_id) throws SQLException{
		String sql = " update UserPosition set upt_title = ?, upt_desc = ?, upt_tcr_id = ?, pfs_update_usr_id = ?, pfs_update_time = ? where upt_code = ? ";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Timestamp curTime = cwSQL.getTime(con);
		int index = 1;
		try{
			stmt = con.prepareStatement(sql);
			stmt.setString(index++, modParam.getUpt_title());
			stmt.setString(index++, modParam.getUpt_desc());
			stmt.setString(index++, modParam.getUpt_tcr_id());
			stmt.setLong(index++, usr_ent_id);
			stmt.setTimestamp(index++, curTime);
			stmt.setString(index++, modParam.getUpt_code());
			stmt.executeUpdate();
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
	}
	
	public String getDelUptAffectUsrXml(Connection con, String[] upt_code_list) throws SQLException{
		StringBuffer xml = new StringBuffer();
		xml.append("<del_upt>").append(getUptTitleStr(con, upt_code_list)).append("</del_upt>");
		xml.append("<user_list>");
		
		String upt_code_str = "(";
		for(String upt_code:upt_code_list){
			upt_code_str += "N'" + upt_code + "',";
		}
		upt_code_str = upt_code_str.substring(0, upt_code_str.length() - 1) + ")";
		String sql = " select usr_ste_usr_id,usr_display_bil,ern_ancestor_ent_id from UserPosition "
					+ " inner join UserPositionRelation on upr_upt_id = upt_id "
					+ " inner join reguser on usr_ent_id = upr_usr_ent_id "
					+ " inner join entityrelation on ern_child_ent_id = usr_ent_id and ern_type = 'USR_PARENT_USG' and ern_parent_ind = 1 "
					+ " where upt_code in " + upt_code_str;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		EntityFullPath entityfullpath = EntityFullPath.getInstance(con);
		try{
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			while(rs.next()){
				xml.append("<user usr_ste_usr_id=\"").append(rs.getString("usr_ste_usr_id")).append("\" usr_display_bil=\"")
					.append(rs.getString("usr_display_bil")).append("\" user_group=\"")
					.append(entityfullpath.getFullPath(con, rs.getLong("ern_ancestor_ent_id"))).append("\"/>");
			}
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		xml.append("</user_list>");
		return xml.toString();
	}
	
	public String getUptTitleStr(Connection con, String[] upt_code_list) throws SQLException{
		String upt_title_str = "";
		String upt_code_str = "(";
		for(String upt_code:upt_code_list){
			upt_code_str += "N'" + upt_code + "',";
		}
		upt_code_str = upt_code_str.substring(0, upt_code_str.length() - 1) + ")";
		String sql = " select upt_title from UserPosition where upt_code in " + upt_code_str;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
			stmt =con.prepareStatement(sql);
			rs = stmt.executeQuery();
			while(rs.next()){
				upt_title_str += rs.getString("upt_title") + ",";
			}
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return upt_title_str.substring(0, upt_title_str.length() - 1);
	}
	
	//删除岗位与用户的关系
	public void deleteUserPositionRelation(Connection con, String[] upt_code_list) throws SQLException{
		String upt_code_str = "(";
		for(String upt_code:upt_code_list){
			upt_code_str += "N'" + upt_code + "',";
		}
		upt_code_str = upt_code_str.substring(0, upt_code_str.length() - 1) + ")";
		String sql = " delete UserPositionRelation where upr_upt_id in (select upt_id from UserPosition where upt_code in " + upt_code_str + ") ";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
			stmt = con.prepareStatement(sql);
			stmt.executeUpdate();
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
	}
	
	//删除岗位
	public void deleteUserPosition(Connection con, String[] upt_code_list) throws SQLException{
		String upt_code_str = "(";
		for(String upt_code:upt_code_list){
			upt_code_str += "N'" + upt_code + "',";
		}
		upt_code_str = upt_code_str.substring(0, upt_code_str.length() - 1) + ")";
		String sql = " delete UserPosition where upt_code in " + upt_code_str;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
			stmt = con.prepareStatement(sql);
			stmt.executeUpdate();
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
	}
	/**
	 * 根据岗位编号获取岗位id，没有的话插入一个新的岗位
	 * @param usr_ent_id 用户id
	 * @param code 编号 
	 * @param tcr_id 培训中心id
	 */
	public long getUptIdByCode(Connection con, long usr_ent_id, String code, String tcr_id) throws SQLException {
		long upt_id = 0;
		String sql = " select upt_id from UserPosition where upt_code = ? and upt_tcr_id = ? ";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
			stmt = con.prepareStatement(sql);
			stmt.setString(1, code);
			stmt.setString(2, tcr_id);
			rs = stmt.executeQuery();
			if(rs.next()){
				upt_id = rs.getLong("upt_id");
			}
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		
		return upt_id;
	}

	//插入新岗位信息
	public long insertUserPosition(Connection con, long usr_ent_id, String code, String title, String desc, String tcr_id) throws SQLException{
		String sql = " insert into UserPosition (upt_code,upt_title,upt_desc,upt_tcr_id,pfs_update_usr_id,pfs_update_time) values (?,?,?,?,?,?) ";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Timestamp curTime = cwSQL.getTime(con);
		long upt_id;
		int index = 1;
		try{
			stmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			stmt.setString(index++, code);
			stmt.setString(index++, title);
			stmt.setString(index++, desc);
			stmt.setString(index++, tcr_id);
			stmt.setLong(index++, usr_ent_id);
			stmt.setTimestamp(index++, curTime);
			if(stmt.executeUpdate() != 1){
                stmt.close();
                con.rollback();
            }
			upt_id = cwSQL.getAutoId(con, stmt, "UserPosition", "upt_id");
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return upt_id;
	}
	
}
