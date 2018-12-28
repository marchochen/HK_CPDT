package com.cw.wizbank.studyGroup;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.cw.wizbank.JsonMod.BaseParam;
import com.cw.wizbank.JsonMod.commonBean.JsonTreeBean;
import com.cw.wizbank.JsonMod.commonBean.OptionBean;
import com.cw.wizbank.JsonMod.studyGroup.StudyGroupModuleParam;
import com.cw.wizbank.JsonMod.studyGroup.bean.ForumTopicBean;
import com.cw.wizbank.JsonMod.studyGroup.bean.ItmSgpForumBean;
import com.cw.wizbank.JsonMod.studyGroup.bean.StudyGroupBean;
import com.cw.wizbank.JsonMod.studyGroup.bean.StudyGroupMemItmBean;
import com.cw.wizbank.JsonMod.studyGroup.bean.StudyGroupMemUsrBean;
import com.cw.wizbank.JsonMod.studyGroup.bean.StudyGroupMgtBean;
import com.cw.wizbank.JsonMod.studyGroup.bean.StudyGroupResBean;
import com.cw.wizbank.JsonMod.supervise.Supervise;
import com.cw.wizbank.accesscontrol.AcTrainingCenter;
import com.cw.wizbank.ae.aeApplication;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.db.DbTrainingCenter;
import com.cw.wizbank.db.view.ViewTrainingCenter;
import com.cw.wizbank.qdb.dbEntityRelation;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwUtils;
import org.apache.commons.lang.StringUtils;

public class StudyGroupDAO {

	public static int SGP_TOPIC_SHOW_NUM =3;
	public static String SGR_TYPE_SGP_MANAGER="SGP_MANAGER";
	public static String SGR_TYPE_SGP_DISCUSS="SGP_DISCUSS";
	public static String SGR_TYPE_SGP_RESOURCE="SGP_RESOURCE";
	public static int SGP_PUBLIC_TYPE_All_CANNOT_SEE=0;
	public static int SGP_PUBLIC_TYPE_MEMBER_CAN_SEE=1;
	public static int SGP_PUBLIC_TYPE_All_CAN_SEE=2;

	public static String SGS_TYPE_DOC="DOC";
	public static String SGS_TYPE_LINK="LINK";
	public static String SGS_TYPE_COURSE="COURSE";

	public static String SGM_TYPE_USR="USR";
	public static String SGM_TYPE_USG="USG";
	public static String SGM_TYPE_ITM="ITM";

	/**
	 * 获取我参加的学习小组
	 * @param con
	 * @param prof
	 * @param param
	 * @param sgpVc
	 * @return
	 * @throws SQLException
	 */
	public Hashtable getMyJoinSgps(Connection con, loginProfile prof, StudyGroupModuleParam param, Vector sgpVc, boolean hasTc, String mgtIdStr) throws SQLException{
		StringBuffer sqlBuf=new StringBuffer();
		sqlBuf.append("SELECT sgp_id, sgp_title,sgp_upd_timestamp,sgp_public_type,sgp_desc FROM studyGroup")
			.append(" INNER JOIN studyGroupMember ON (sgm_sgp_id = sgp_id AND sgm_type = 'USR')")
			.append(" WHERE sgm_status = 'ADMITTED' AND sgm_ent_id = ?  ")
			.append(" AND (sgp_public_type = ? OR sgp_public_type = ?)");
		if(hasTc){
			sqlBuf.append(" AND sgp_tcr_id in ").append(param.getTcr_id_lst());
		}
		if(mgtIdStr!=null && mgtIdStr.length()>0){
			sqlBuf.append(" AND sgp_id not in (").append(mgtIdStr).append(")");
		}
		sqlBuf.append(" UNION ")
			.append(" SELECT DISTINCT(sgp_id), sgp_title,sgp_upd_timestamp,sgp_public_type,sgp_desc FROM studyGroup")
			.append(" INNER JOIN studyGroupMember ON (sgm_sgp_id = sgp_id AND sgm_type = 'USG')")
			.append(" INNER JOIN EntityRelation ON (ern_ancestor_ent_id = sgm_ent_id AND ern_type = 'USR_PARENT_USG')")
			.append(" WHERE ern_child_ent_id = ? ")
			.append(" AND (sgp_public_type = ? OR sgp_public_type = ?)");
		if(hasTc){
			sqlBuf.append(" AND sgp_tcr_id in ").append(param.getTcr_id_lst());
		}
		if(mgtIdStr!=null && mgtIdStr.length()>0){
			sqlBuf.append(" AND sgp_id not in (").append(mgtIdStr).append(")");
		}
		sqlBuf.append(" UNION ")
			.append(" SELECT DISTINCT(sgp_id), sgp_title,sgp_upd_timestamp,sgp_public_type,sgp_desc FROM studyGroup")
			.append(" INNER JOIN studyGroupMember ON (sgm_sgp_id = sgp_id AND sgm_type = 'ITM')")
			.append(" INNER JOIN aeApplication ON (app_itm_id = sgm_ent_id AND app_status = '"+aeApplication.ADMITTED+"')")
			.append(" INNER JOIN aeItem ON (itm_id = app_itm_id AND itm_status = 'ON')")
			.append(" WHERE app_ent_id = ? ")
			.append(" AND (sgp_public_type = ? OR sgp_public_type = ?)");
		if(mgtIdStr!=null && mgtIdStr.length()>0){
			sqlBuf.append(" AND sgp_id not in (").append(mgtIdStr).append(")");
		}
		sqlBuf.append(" ORDER BY sgp_upd_timestamp DESC");

		PreparedStatement stmt = null;
	    stmt = con.prepareStatement(sqlBuf.toString());
	    int index=1;
	    stmt.setLong(index++, prof.usr_ent_id);
	    stmt.setInt(index++, SGP_PUBLIC_TYPE_MEMBER_CAN_SEE);
	    stmt.setInt(index++, SGP_PUBLIC_TYPE_All_CAN_SEE);
	    stmt.setLong(index++, prof.usr_ent_id);
	    stmt.setInt(index++, SGP_PUBLIC_TYPE_MEMBER_CAN_SEE);
	    stmt.setInt(index++, SGP_PUBLIC_TYPE_All_CAN_SEE);
	    stmt.setLong(index++, prof.usr_ent_id);
	    stmt.setInt(index++, SGP_PUBLIC_TYPE_MEMBER_CAN_SEE);
	    stmt.setInt(index++, SGP_PUBLIC_TYPE_All_CAN_SEE);
	    ResultSet rs = stmt.executeQuery();
	    int count=0;
	    StringBuffer idBuf =new StringBuffer();
	    StringBuffer allIdBuf = new StringBuffer();
	    while(rs.next()){
	    	if(count >=param.getStart()&& count <(param.getStart()+param.getLimit())){
	    		StudyGroupBean sgp= new StudyGroupBean();
	    		sgp.setSgp_id(rs.getLong("sgp_id"));
	    		idBuf.append(",").append(sgp.getSgp_id());
	    		sgp.setSgp_title(rs.getString("sgp_title"));
	    		sgp.setSgp_desc(rs.getString("sgp_desc"));
	    		sgp.setSgp_public_type(rs.getInt("sgp_public_type"));
	    		sgp.setCan_view(true);
	    		sgpVc.add(sgp);
	    	}
	    	allIdBuf.append(",").append(rs.getLong("sgp_id"));
	    	count++;

	    }
	    Hashtable idStrHs =new Hashtable();
	    param.setTotal_rec(count);
	    if(stmt !=null)stmt.close();
	    if(idBuf.length()>0){
	    	idBuf=idBuf.deleteCharAt(0);
	    	idStrHs.put("idStr", idBuf.toString());
	    }
	    if(allIdBuf.length()>0){
	    	allIdBuf=allIdBuf.deleteCharAt(0);
	    	idStrHs.put("allIdStr", allIdBuf.toString());
	    }
		return idStrHs;
	}
	/**
	 * 获取我管理的学习中心
	 * @param con
	 * @param prof
	 * @param param
	 * @param sgpVc
	 * @return
	 * @throws SQLException
	 */
	public Hashtable getMyMgtSgps(Connection con, loginProfile prof, StudyGroupModuleParam param,Vector sgpVc ,boolean hasTc) throws SQLException{
		String sql="SELECT sgp_id, sgp_title ,sgp_public_type ,sgp_upd_timestamp,sgp_desc FROM studyGroup" +
				" INNER JOIN studyGroupRelation ON (sgr_sgp_id = sgp_id AND sgr_type = ?)" +
				" WHERE sgr_ent_id = ? " +
				" AND (sgp_public_type = ? OR sgp_public_type = ?)";
		if(hasTc){
			sql=sql+" AND sgp_tcr_id in "+param.getTcr_id_lst();
		}
		if(param.getSort()==null ||param.getSort().length()==0 ){
			sql=sql+"  ORDER BY sgp_upd_timestamp DESC";
		}else{
			sql=sql+"  ORDER BY "+param.getSort()+" "+param.getDir();
		}
		PreparedStatement stmt = null;
	    stmt = con.prepareStatement(sql);
	    int index=1;
	    stmt.setString(index++, SGR_TYPE_SGP_MANAGER);
	    stmt.setLong(index++, prof.usr_ent_id);
	    stmt.setInt(index++, SGP_PUBLIC_TYPE_MEMBER_CAN_SEE);
	    stmt.setInt(index++, SGP_PUBLIC_TYPE_All_CAN_SEE);
	    ResultSet rs = stmt.executeQuery();
	    int count=0;
	    StringBuffer idBuf =new StringBuffer();
	    StringBuffer allIdBuf = new StringBuffer();
	    while(rs.next()){
	    	if(count >=param.getStart()&& count <(param.getStart()+param.getLimit())){
	    		StudyGroupBean sgp= new StudyGroupBean();
	    		sgp.setSgp_id(rs.getLong("sgp_id"));
	    		idBuf.append(",").append(sgp.getSgp_id());
	    		sgp.setSgp_title(rs.getString("sgp_title"));
	    		sgp.setSgp_desc(rs.getString("sgp_desc"));
	    		sgp.setSgp_public_type(rs.getInt("sgp_public_type"));
	    		sgp.setCan_view(true);
	    		sgp.setSgp_upd_timestamp(rs.getTimestamp("sgp_upd_timestamp"));
	    		sgpVc.add(sgp);
	    	}
	    	allIdBuf.append(",").append(rs.getLong("sgp_id"));
	    	count++;
	    }
	    param.setTotal_rec(count);
	    Hashtable idStrHs =new Hashtable();
	    if(stmt !=null)stmt.close();
	    if(idBuf.length()>0){
	    	idBuf=idBuf.deleteCharAt(0);
	    	idStrHs.put("idStr", idBuf.toString());
	    }
	    if(allIdBuf.length()>0){
	    	allIdBuf=allIdBuf.deleteCharAt(0);
	    	idStrHs.put("allIdStr", allIdBuf.toString());
	    }
	    return idStrHs;
	}

	/**
	 * 获取成员个数
	 * @param con
	 * @param prof
	 * @param sgp_id_str
	 * @param memHs
	 * @throws SQLException
	 */
	public void getMemCntHs(Connection con, loginProfile prof, String sgp_id_str,Hashtable memHs)throws SQLException{
		StringBuffer sqlBuf=new StringBuffer();
		sqlBuf.append("SELECT sgm_sgp_id, COUNT(usr_ent_id) AS sgm_member_total FROM (")
			.append("  SELECT sgm_sgp_id, usr_ent_id FROM studyGroupMember")
			.append("  INNER JOIN regUser ON (usr_ent_id = sgm_ent_id AND usr_status = 'OK')")
			.append("  WHERE sgm_type = 'USR' AND sgm_sgp_id in ("+sgp_id_str+") ")
			.append("  UNION")
			.append("  SELECT sgm_sgp_id, usr_ent_id FROM studyGroupMember")
			.append("  INNER JOIN aeItem ON (sgm_ent_id = itm_id)")
			.append("  INNER JOIN aeApplication ON (app_itm_id = itm_id AND app_status = '"+aeApplication.ADMITTED+"')")
			.append("  INNER JOIN regUser ON (usr_ent_id = app_ent_id AND usr_status = 'OK')")
			.append("  WHERE sgm_type = 'ITM' AND sgm_sgp_id in ("+sgp_id_str+")")
			.append("  UNION")
			.append("  SELECT sgm_sgp_id, usr_ent_id FROM studyGroupMember ")
			.append("  INNER JOIN EntityRelation ON (ern_ancestor_ent_id = sgm_ent_id AND ern_type = 'USR_PARENT_USG')")
			.append("  INNER JOIN regUser ON (usr_ent_id = ern_child_ent_id AND usr_status = 'OK')")
			.append("  WHERE sgm_type = 'USG' AND sgm_sgp_id in ("+sgp_id_str+")")
			.append(" ) as tempSgm")
			.append(" GROUP BY sgm_sgp_id ");
		PreparedStatement stmt = null;
		stmt = con.prepareStatement(sqlBuf.toString());
	    ResultSet rs = stmt.executeQuery();
	    while(rs.next()){
	    	memHs.put(new Long(rs.getLong("sgm_sgp_id")), new Integer(rs.getInt("sgm_member_total")));
	    }
	    if(stmt !=null)stmt.close();
	}

	/**
	 * 获取信息个数
	 * @param con
	 * @param prof
	 * @param sgp_id_str
	 * @param topicHs
	 * @throws SQLException
	 */
	public void getTopicCntHs(Connection con, loginProfile prof, String sgp_id_str ,Hashtable topicHs)throws SQLException{
		StringBuffer sqlBuf=new StringBuffer();
		sqlBuf.append("SELECT sgr_sgp_id, count(fto_id) AS sgr_topic_total FROM studyGroupRelation")
			.append(" INNER JOIN forumTopic ON (fto_res_id = sgr_ent_id AND sgr_type = ?)")
			.append(" WHERE sgr_sgp_id in ("+sgp_id_str+") group by sgr_sgp_id");
		PreparedStatement stmt = null;
		stmt = con.prepareStatement(sqlBuf.toString());
		stmt.setString(1, SGR_TYPE_SGP_DISCUSS);
	    ResultSet rs = stmt.executeQuery();
	    while(rs.next()){
	    	topicHs.put(new Long(rs.getLong("sgr_sgp_id")), new Integer(rs.getInt("sgr_topic_total")));
	    }
	    if(stmt !=null)stmt.close();
	}

	/**
	 * 获取资源个数
	 * @param con
	 * @param prof
	 * @param sgp_id_str
	 * @param resHs
	 * @throws SQLException
	 */
	public void getResCntHs(Connection con, loginProfile prof, String sgp_id_str ,Hashtable resHs)throws SQLException{
		StringBuffer sqlBuf=new StringBuffer();
		sqlBuf.append("SELECT sgr_sgp_id, COUNT(sgr_ent_id) AS sgr_res_total FROM studyGroupRelation")
			.append(" WHERE sgr_type=? AND sgr_sgp_id in ("+sgp_id_str+") Group by sgr_sgp_id");
		PreparedStatement stmt = null;
		stmt = con.prepareStatement(sqlBuf.toString());
		stmt.setString(1, SGR_TYPE_SGP_RESOURCE);
	    ResultSet rs = stmt.executeQuery();
	    while(rs.next()){
	    	resHs.put(new Long(rs.getLong("sgr_sgp_id")), new Integer(rs.getInt("sgr_res_total")));
	    }
	    if(stmt !=null)stmt.close();
	}

	/**
	 * 获取学习小组组长的昵称和Email
	 * @param con
	 * @param sgp_id_str
	 * @param mailHs
	 * @throws SQLException
	 */
	public void getMgtEmailHs(Connection con, String sgp_id_str ,Hashtable mailHs) throws SQLException{
		String sql ="SELECT sgr_sgp_id,usr_ent_id, usr_display_bil, usr_nickname, usr_email FROM regUser" +
				" INNER JOIN studyGroupRelation ON (sgr_ent_id = usr_ent_id AND sgr_type = ?)" +
				" WHERE usr_status <> 'DELETED' AND sgr_sgp_id in ("+sgp_id_str+")";
		PreparedStatement stmt = null;
		stmt = con.prepareStatement(sql);
		stmt.setString(1, SGR_TYPE_SGP_MANAGER);
	    ResultSet rs = stmt.executeQuery();
	    while(rs.next()){
	    	StudyGroupMgtBean mgt =new StudyGroupMgtBean();
	    	Long idObj= new Long(rs.getLong("sgr_sgp_id"));
	    	mgt.setUsr_nickname(rs.getString("usr_nickname"));
	    	mgt.setUsr_display_bil(rs.getString("usr_display_bil"));
	    	mgt.setUsr_email(rs.getString("usr_email"));
	    	mgt.setUsr_ent_id(rs.getLong("usr_ent_id"));
	    	int mgtCut=1;
	    	Hashtable mgtHs = new Hashtable();
	    	if(mailHs.containsKey(idObj)){
	    		mgtHs= (Hashtable)mailHs.get(idObj);
	    		mgtCut=mgtHs.size();
	    		mgtHs.put(new Integer(mgtCut+1), mgt);
	    		mailHs.put(new Long(rs.getLong("sgr_sgp_id")), mgtHs);
	    	}else{
	    		mgtHs.put(new Integer(mgtCut), mgt);
	    		mailHs.put(new Long(rs.getLong("sgr_sgp_id")), mgtHs);
	    	}
	    }
	    if(stmt !=null)stmt.close();
	}
	/**
	 * 获取学习小组相关的培训中心培训管理员的昵称和Email
	 * @param con
	 * @param sgp_id_str
	 * @param mailHs
	 * @throws SQLException
	 */
	public void getSgpTaEmailHs(Connection con, String sgp_id_str ,Hashtable mailHs) throws SQLException{
		String sql ="select sgp_id,usr_ent_id, usr_nickname, usr_email from studygroup,tcTrainingCenterOfficer,reguser" +
				" where tco_tcr_id = sgp_tcr_id" +
				" and usr_ent_id = tco_usr_ent_id" +
				" and usr_status <> 'DELETED'" +
				" and sgp_id in ("+sgp_id_str+" )";
		PreparedStatement stmt = null;
		stmt = con.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();
		while(rs.next()){
			StudyGroupMgtBean mgt =new StudyGroupMgtBean();
			Long idObj= new Long(rs.getLong("sgp_id"));
			mgt.setUsr_nickname(rs.getString("usr_nickname"));
			mgt.setUsr_email(rs.getString("usr_email"));
			mgt.setUsr_ent_id(rs.getLong("usr_ent_id"));
			int mgtCut=1;
			Hashtable mgtHs = new Hashtable();
			if(mailHs.containsKey(idObj)){
				mgtHs= (Hashtable)mailHs.get(idObj);
				mgtCut=mgtHs.size();
				mgtHs.put(new Integer(mgtCut+1), mgt);
				mailHs.put(new Long(rs.getLong("sgp_id")), mgtHs);
			}else{
				mgtHs.put(new Integer(mgtCut), mgt);
				mailHs.put(new Long(rs.getLong("sgp_id")), mgtHs);
			}
		}
		if(stmt !=null)stmt.close();
	}

	public Vector getMgtEntIdLst(Connection con, long sgp_id) throws SQLException{
		String sql ="SELECT usr_ent_id, usr_nickname, usr_email FROM regUser" +
				" INNER JOIN studyGroupRelation ON (sgr_ent_id = usr_ent_id AND sgr_type = ?)" +
				" WHERE usr_status <> 'DELETED' AND sgr_sgp_id =?";
		PreparedStatement stmt = null;
		stmt = con.prepareStatement(sql);
		stmt.setString(1, SGR_TYPE_SGP_MANAGER);
		stmt.setLong(2,sgp_id );
		ResultSet rs = stmt.executeQuery();
		Vector entIdVc =new Vector();
		while(rs.next()){
			entIdVc.add(new Long(rs.getLong("usr_ent_id")));
		}
		if(stmt !=null)stmt.close();
		return entIdVc;
	}

	/**
	 * 获取其他的学习小组（除了我参加的和我管理的外）
	 * @param con
	 * @param prof
	 * @param param
	 * @param sgpVc
	 * @param NotInIdStr
	 * @return
	 * @throws SQLException
	 */
	public String getOtherSgps(Connection con, loginProfile prof, StudyGroupModuleParam param, Vector sgpVc, String NotInIdStr ,boolean hasTc)throws SQLException{
		StringBuffer sqlBuf=new StringBuffer();
		sqlBuf.append("SELECT sgp_id, sgp_title, sgp_upd_timestamp,sgm_status ,sgp_public_type,sgp_desc FROM studyGroup")
			.append(" INNER JOIN tcTrainingCenterTargetEntity ON (tce_tcr_id = sgp_tcr_id)")
			.append(" INNER JOIN tcTrainingCenter ON (tce_tcr_id = tcr_id AND tcr_status = 'OK')")
			.append(" INNER JOIN entityRelation ON (ern_ancestor_ent_id = tce_ent_id AND ern_type = 'USR_PARENT_USG')")
			.append(" LEFT JOIN studyGroupMember ON (sgm_sgp_id = sgp_id AND sgm_type = 'USR' AND sgm_status = 'PENDING' AND sgm_ent_id = ?) ")
			.append(" WHERE ern_child_ent_id = ? AND sgp_id NOT IN ("+NotInIdStr+") ")
			.append(" AND (sgp_public_type = ? OR sgp_public_type = ?)");
		if(hasTc){
			sqlBuf.append(" AND sgp_tcr_id in ").append(param.getTcr_id_lst());
		}
		sqlBuf.append(" UNION")
			.append(" SELECT sgp_id, sgp_title, sgp_upd_timestamp, sgm_status, sgp_public_type,sgp_desc FROM studyGroup")
			.append(" INNER JOIN tcRelation ON (tcn_ancestor = sgp_tcr_id)")
			.append(" INNER JOIN tcTrainingCenterTargetEntity ON (tce_tcr_id = tcn_child_tcr_id)")
			.append(" INNER JOIN tcTrainingCenter ON (tce_tcr_id = tcr_id AND tcr_status = 'OK')")
			.append(" INNER JOIN entityRelation ON (ern_ancestor_ent_id = tce_ent_id AND ern_type = 'USR_PARENT_USG')")
			.append(" LEFT JOIN studyGroupMember ON (sgm_sgp_id = sgp_id AND sgm_type = 'USR' AND sgm_status = 'PENDING' AND sgm_ent_id = ?)")
			.append(" WHERE ern_child_ent_id = ?  AND sgp_id NOT IN ("+NotInIdStr+") ")
			.append(" AND (sgp_public_type = ? OR sgp_public_type = ?)");
		if(hasTc){
			sqlBuf.append(" AND sgp_tcr_id in ").append(param.getTcr_id_lst());
		}

		PreparedStatement stmt = null;
		stmt = con.prepareStatement(sqlBuf.toString());
		int index =1;
		stmt.setLong(index++, prof.usr_ent_id);
		stmt.setLong(index++, prof.usr_ent_id);
		stmt.setInt(index++, SGP_PUBLIC_TYPE_MEMBER_CAN_SEE);
		stmt.setInt(index++, SGP_PUBLIC_TYPE_All_CAN_SEE);
		stmt.setLong(index++, prof.usr_ent_id);
		stmt.setLong(index++, prof.usr_ent_id);
		stmt.setInt(index++, SGP_PUBLIC_TYPE_MEMBER_CAN_SEE);
		stmt.setInt(index++, SGP_PUBLIC_TYPE_All_CAN_SEE);
		ResultSet rs = stmt.executeQuery();
	    int count=0;
	    StringBuffer idBuf =new StringBuffer();
	    while(rs.next()){
	    	if(count >=param.getStart()&& count <(param.getStart()+param.getLimit())){
	    		StudyGroupBean sgp= new StudyGroupBean();
	    		sgp.setSgp_id(rs.getLong("sgp_id"));
	    		idBuf.append(",").append(sgp.getSgp_id());
	    		sgp.setSgp_title(rs.getString("sgp_title"));
	    		sgp.setSgp_desc(rs.getString("sgp_desc"));
	    		sgp.setSgp_public_type(rs.getInt("sgp_public_type"));
	    		sgpVc.add(sgp);
	    	}
	    	count++;
	    }
	    param.setTotal_rec(count);
	    if(stmt !=null)stmt.close();
	    if(idBuf.length()>0)
	    	idBuf=idBuf.deleteCharAt(0);
		return idBuf.toString();
	}

	/**
	 * 搜索学习小组
	 * @param con
	 * @param prof
	 * @param param
	 * @param sgpVc
	 * @return
	 * @throws SQLException
	 */
	public String searchStudyGroup(Connection con, loginProfile prof, StudyGroupModuleParam param, Vector sgpVc, boolean hasTc) throws SQLException{
		String sql ="SELECT sgp_id, sgp_title, sgp_desc, sgp_upd_timestamp ,sgp_public_type FROM studyGroup" +
				" INNER JOIN tcTrainingCenterTargetEntity ON (tce_tcr_id = sgp_tcr_id)" +
				" INNER JOIN tcTrainingCenter ON (tce_tcr_id = tcr_id AND tcr_status = 'OK')" +
				" INNER JOIN entityRelation ON (ern_ancestor_ent_id = tce_ent_id AND ern_type = 'USR_PARENT_USG')" +
				" WHERE (LOWER(sgp_title) like ? OR LOWER(sgp_desc) like ? ) AND ern_child_ent_id = ?" +
				" AND (sgp_public_type = ? OR sgp_public_type = ?)" ;
		if(hasTc){
			sql+=" and sgp_tcr_id in "+param.getTcr_id_lst() ;

		}
		sql+=" UNION" +
				" SELECT sgp_id, sgp_title, sgp_desc, sgp_upd_timestamp ,sgp_public_type FROM studyGroup" +
				" INNER JOIN tcRelation ON (tcn_ancestor = sgp_tcr_id)" +
				" INNER JOIN tcTrainingCenterTargetEntity ON (tce_tcr_id = tcn_child_tcr_id)" +
				" INNER JOIN tcTrainingCenter ON (tce_tcr_id = tcr_id AND tcr_status = 'OK')" +
				" INNER JOIN entityRelation ON (ern_ancestor_ent_id = tce_ent_id AND ern_type = 'USR_PARENT_USG')" +
				" WHERE (LOWER(sgp_title) like ? OR LOWER(sgp_desc) like ? ) AND ern_child_ent_id = ?" +
				" AND (sgp_public_type = ? OR sgp_public_type = ?)" ;
		if(hasTc){
			sql+=" and sgp_tcr_id in "+param.getTcr_id_lst() ;

		}
		sql+=" UNION "
			+" SELECT DISTINCT(sgp_id), sgp_title,sgp_desc, sgp_upd_timestamp,sgp_public_type FROM studyGroup"
			+" INNER JOIN studyGroupMember ON (sgm_sgp_id = sgp_id AND sgm_type = 'ITM')"
			+" INNER JOIN aeApplication ON (app_itm_id = sgm_ent_id AND app_status = '"+aeApplication.ADMITTED+"')"
			+" INNER JOIN aeItem ON (itm_id = app_itm_id AND itm_status = 'ON')"
			+" WHERE (LOWER(sgp_title) like ? OR LOWER(sgp_desc) like ? ) AND app_ent_id = ? "
			+" AND (sgp_public_type = ? OR sgp_public_type = ?)";

		sql+=" ORDER BY sgp_upd_timestamp DESC" ;
		PreparedStatement stmt = null;
		stmt = con.prepareStatement(sql);
		int index=1;
		stmt.setString(index++ , "%"+param.getSgp_search_key()+"%");
		stmt.setString(index++ , "%"+param.getSgp_search_key()+"%");
		stmt.setLong(index++, prof.usr_ent_id);
		stmt.setInt(index++, SGP_PUBLIC_TYPE_MEMBER_CAN_SEE);
		stmt.setInt(index++, SGP_PUBLIC_TYPE_All_CAN_SEE);
		stmt.setString(index++ , "%"+param.getSgp_search_key()+"%");
		stmt.setString(index++ , "%"+param.getSgp_search_key()+"%");
		stmt.setLong(index++, prof.usr_ent_id);
		stmt.setInt(index++, SGP_PUBLIC_TYPE_MEMBER_CAN_SEE);
		stmt.setInt(index++, SGP_PUBLIC_TYPE_All_CAN_SEE);
		stmt.setString(index++ , "%"+param.getSgp_search_key()+"%");
		stmt.setString(index++ , "%"+param.getSgp_search_key()+"%");
		stmt.setLong(index++, prof.usr_ent_id);
		stmt.setInt(index++, SGP_PUBLIC_TYPE_MEMBER_CAN_SEE);
		stmt.setInt(index++, SGP_PUBLIC_TYPE_All_CAN_SEE);
		ResultSet rs = stmt.executeQuery();
		int count=0;
		StringBuffer idBuf =new StringBuffer();
		while(rs.next()){
			if(count >=param.getStart()&& count <(param.getStart()+param.getLimit())){
	    		StudyGroupBean sgp= new StudyGroupBean();
	    		sgp.setSgp_id(rs.getLong("sgp_id"));
	    		idBuf.append(",").append(sgp.getSgp_id());
	    		sgp.setSgp_title(rs.getString("sgp_title"));
	    		sgp.setSgp_desc(rs.getString("sgp_desc"));
	    		sgp.setSgp_public_type(rs.getInt("sgp_public_type"));
	    		sgp.setSgp_upd_timestamp(rs.getTimestamp("sgp_upd_timestamp"));
	    		sgpVc.add(sgp);
	    	}
	    	count++;
		}
		if(stmt !=null)stmt.close();
		param.setTotal_rec(count);
		if(idBuf.length()>0)
			idBuf=idBuf.deleteCharAt(0);
		return idBuf.toString();
	}

	/**
	 * 获取学习小组状态
	 * @param con
	 * @param prof
	 * @param sgp_id_str
	 * @param sgpStatusHt
	 * @throws SQLException
	 */
	public void getStudyGroupStatus(Connection con,loginProfile prof, String sgp_id_str ,Hashtable sgpStatusHt) throws SQLException{
		String sql ="SELECT sgp_id, sgm_status FROM studyGroup"+
				" INNER JOIN studyGroupMember ON (sgm_sgp_id = sgp_id AND sgm_type = 'USR')"+
				" WHERE sgm_ent_id = ? AND sgp_id IN ("+sgp_id_str+")"+
				" UNION "+
				" SELECT DISTINCT sgp_id, N'ADMITTED' AS sgm_status FROM studyGroup"+
				" INNER JOIN studyGroupMember ON (sgm_sgp_id = sgp_id AND sgm_type = 'USG')"+
				" INNER JOIN EntityRelation ON (ern_ancestor_ent_id = sgm_ent_id AND ern_type = 'USR_PARENT_USG')"+
				" WHERE ern_child_ent_id = ?  AND sgp_id IN ("+sgp_id_str+") "+
				" UNION "+
				" SELECT DISTINCT sgp_id,N'ADMITTED' AS sgm_status FROM studyGroup"+
				" INNER JOIN studyGroupMember ON (sgm_sgp_id = sgp_id AND sgm_type = 'ITM')"+
				" INNER JOIN aeApplication ON (app_itm_id = sgm_ent_id AND app_status = '"+aeApplication.ADMITTED+"')"+
				" INNER JOIN aeItem ON (itm_id = sgm_ent_id AND itm_status = 'ON')"+
				" WHERE app_ent_id = ?  AND sgp_id IN ("+sgp_id_str+") " +
				" union" +
				"  SELECT sgr_sgp_id AS sgp_id, N'ADMITTED' AS sgm_status FROM studyGroupRelation" +
				" WHERE sgr_type = ? AND sgr_ent_id = ? " +
				" AND sgr_sgp_id IN ("+sgp_id_str+")";
		PreparedStatement stmt = null;
		stmt = con.prepareStatement(sql);
		int index=1;
		stmt.setLong(index++, prof.usr_ent_id);
		stmt.setLong(index++, prof.usr_ent_id);
		stmt.setLong(index++, prof.usr_ent_id);
		stmt.setString(index++, SGR_TYPE_SGP_MANAGER);
		stmt.setLong(index++, prof.usr_ent_id);
		ResultSet rs = stmt.executeQuery();
		while(rs.next()){
			sgpStatusHt.put(new Long(rs.getLong("sgp_id")), rs.getString("sgm_status"));
		}
		if(stmt !=null)stmt.close();
	}

	/**
	 * 获取某个学习小组的详细信息
	 * @param con
	 * @param sgpId
	 * @return
	 * @throws SQLException
	 */
	public StudyGroupBean getStudyGroupDetail(Connection con, long sgpId) throws SQLException{
		String sql="SELECT sgp_id, sgp_title, sgp_desc,sgp_public_type,sgp_upd_timestamp, sgp_send_email_ind, tcr_title, tcr_id FROM studyGroup " +
				" INNER JOIN tcTrainingCenter ON (tcr_id = sgp_tcr_id)" +
				" WHERE sgp_id = ?";
		PreparedStatement stmt = null;
		stmt = con.prepareStatement(sql);
		stmt.setLong(1, sgpId);
		ResultSet rs = stmt.executeQuery();
		StudyGroupBean sgp =new StudyGroupBean();
		if(rs.next()){
			sgp.setSgp_id(rs.getLong("sgp_id"));
			sgp.setSgp_title(rs.getString("sgp_title"));
			sgp.setSgp_desc(rs.getString("sgp_desc"));
			sgp.setSgp_public_type(rs.getInt("sgp_public_type"));
			sgp.setTcr_title(rs.getString("tcr_title"));
			sgp.setTcr_id(rs.getLong("tcr_id"));
			sgp.setSgp_upd_timestamp(rs.getTimestamp("sgp_upd_timestamp"));
			sgp.setSgp_send_email_ind(rs.getInt("sgp_send_email_ind"));
			sgp.setSgp_title_noescape(sgp.getSgp_title());
			sgp.setSgp_desc_noescape(sgp.getSgp_desc());
		}
		if(stmt !=null)stmt.close();
		return sgp;
	}

	/**
	 * 判断当前用户是否是学习小组的组长
	 * @param con
	 * @param prof
	 * @param sgpId
	 * @return
	 * @throws SQLException
	 */
	public boolean isCurUsrMgt(Connection con, loginProfile prof, long sgpId) throws SQLException{
		boolean isMgt=false;
		String sql ="SELECT sgr_sgp_id, usr_nickname, usr_email FROM regUser" +
			" INNER JOIN studyGroupRelation ON (sgr_ent_id = usr_ent_id AND sgr_type = ?)" +
			" WHERE usr_status <> 'DELETED' AND sgr_sgp_id =? and usr_ent_id= ?";
		PreparedStatement stmt = null;
		stmt = con.prepareStatement(sql);
		int index=1;
		stmt.setString(index++, SGR_TYPE_SGP_MANAGER);
		stmt.setLong(index++, sgpId);
		stmt.setLong(index++, prof.usr_ent_id);
		ResultSet rs = stmt.executeQuery();
		if(rs.next()){
			isMgt=true;
		}
		if(stmt !=null) stmt.close();
		return isMgt;
	}

	/**
	 * 学习小组成员列表
	 * @param con
	 * @param sgpId
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	public Vector getStudyGroupMemLst(Connection con, loginProfile prof,StudyGroupModuleParam param, WizbiniLoader wizbini) throws SQLException{
		String sql=" SELECT sgm_sgp_id, usr_ent_id , usr_nickname, usr_display_bil,urx_extra_43 ,  0 AS is_group_leader FROM studyGroupMember"+
				"  INNER JOIN regUser ON (usr_ent_id = sgm_ent_id AND usr_status = 'OK')"+
				"  INNER JOIN RegUserExtension ON (usr_ent_id = urx_usr_ent_id)"+
				"  WHERE sgm_type = 'USR' AND sgm_sgp_id = ? "+
				"  UNION"+
				"  SELECT sgm_sgp_id, usr_ent_id , usr_nickname ,usr_display_bil,urx_extra_43 , 0 AS is_group_leader FROM studyGroupMember"+
				"  INNER JOIN aeItem ON (sgm_ent_id = itm_id)"+
				"  INNER JOIN aeApplication ON (app_itm_id = itm_id AND app_status = 'Admitted')"+
				"  INNER JOIN regUser ON (usr_ent_id = app_ent_id AND usr_status = 'OK')"+
				"  INNER JOIN RegUserExtension ON (usr_ent_id = urx_usr_ent_id)"+
				"  WHERE sgm_type = 'ITM' AND sgm_sgp_id = ? "+
				"  UNION"+
				"  SELECT sgm_sgp_id, usr_ent_id, usr_nickname ,usr_display_bil,urx_extra_43 , 0 AS is_group_leader FROM studyGroupMember "+
				"  INNER JOIN EntityRelation ON (ern_ancestor_ent_id = sgm_ent_id AND ern_type = 'USR_PARENT_USG')"+
				"  INNER JOIN regUser ON (usr_ent_id = ern_child_ent_id AND usr_status = 'OK')"+
				"  INNER JOIN RegUserExtension ON (usr_ent_id = urx_usr_ent_id)"+
				"  WHERE sgm_type = 'USG' AND sgm_sgp_id  = ? " +
				"  UNION" +
				"  SEleCT sgr_sgp_id, usr_ent_id, usr_nickname,usr_display_bil,urx_extra_43 , 1 AS is_group_leader FROM studyGroupRelation" +
				"  INNER JOIN regUser ON (usr_ent_id = sgr_ent_id AND usr_status = 'OK')" +
				"  INNER JOIN RegUserExtension ON (usr_ent_id = urx_usr_ent_id)"+
				"  WHERE sgr_type = ? AND sgr_sgp_id = ?" +
				" order by is_group_leader desc";

		PreparedStatement stmt = null;
		stmt = con.prepareStatement(sql);
		int index =1;
		stmt.setLong(index++, param.getSgp_id());
		stmt.setLong(index++, param.getSgp_id());
		stmt.setLong(index++, param.getSgp_id());
		stmt.setString(index++, SGR_TYPE_SGP_MANAGER);
		stmt.setLong(index++, param.getSgp_id());
		ResultSet rs = stmt.executeQuery();
		int count=0;
//		if(param.getLimit()==0 || param.getLimit()==BaseParam.LIMIT_NUM_TEN){
//			param.setLimit(5);
//		}
		Vector memVc = new Vector();
		Vector idVc = new Vector();
        StudyGroupMemUsrBean mem = null;
        String sTem = "";
		while(rs.next()){
			long usr_ent_id=rs.getLong("usr_ent_id");
			if(!idVc.contains(new Long(usr_ent_id))){
				idVc.add(new Long(usr_ent_id));
				if(count >=param.getStart()&& count <(param.getStart()+param.getLimit())){
					mem = new StudyGroupMemUsrBean();
					mem.setUsr_ent_id(usr_ent_id);
                    sTem = rs.getString("usr_nickname");
                    if(StringUtils.isBlank(sTem)){
                        sTem = rs.getString("usr_display_bil");
                    }
					mem.setUsr_nickname(sTem);
					mem.setIs_group_leader(rs.getBoolean("is_group_leader"));
					String photo=wizbini.getUserPhotoPath(prof, usr_ent_id, rs.getString("urx_extra_43"));
					mem.setUrx_extra_43(photo);
					memVc.add(mem);
				} else {
                    break;
                }
				count++;
			}
		}
		param.setTotal_rec(count);
		if(stmt !=null) stmt.close();
		return memVc;
	}

	/**
	 * 学习小组资源列表
	 * @param con
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	public Vector getStudyGroupResLst(Connection con, loginProfile prof, StudyGroupModuleParam param, WizbiniLoader wizbini) throws SQLException {
		String sql = "SELECT sgs_id,sgs_title, sgs_type, sgs_content,sgs_desc ,sgs_upd_timestamp ,sgs_create_usr_id FROM studyGroupResources"
				+ " INNER JOIN studyGroupRelation ON (sgr_ent_id = sgs_id AND sgr_type = ?)"
				+ " WHERE sgr_sgp_id = ?"
				+ " Order by sgs_create_timestamp desc";
		PreparedStatement stmt = null;
		stmt = con.prepareStatement(sql);
		int index = 1;
		stmt.setString(index++, SGR_TYPE_SGP_RESOURCE);
		stmt.setLong(index++, param.getSgp_id());
		ResultSet rs = stmt.executeQuery();
		int count = 0;
		if (param.getLimit() == 0) {
			param.setLimit(5);
		}
		Vector resVc = new Vector();
		while (rs.next()) {
			if (count >= param.getStart() && count < (param.getStart() + param.getLimit())) {
				StudyGroupResBean res = new StudyGroupResBean();
				res.setSgs_id(rs.getLong("sgs_id"));
				res.setSgs_title(rs.getString("sgs_title"));
				res.setSgs_type(rs.getString("sgs_type"));
				if (SGS_TYPE_DOC.equalsIgnoreCase(res.getSgs_type())) {
					String docDir = cwUtils.getFileURL(wizbini.cfgSysSetupadv.getFileUpload().getSgpResDir().getUrl()) + res.getSgs_id() + "/" + rs.getString("sgs_content");
					res.setSgs_content(docDir);
				} else {
					res.setSgs_content(rs.getString("sgs_content"));
				}
				res.setSgs_desc(rs.getString("sgs_desc"));
				res.setSgs_upd_timestamp(rs.getTimestamp("sgs_upd_timestamp"));
				if (prof.usr_ste_usr_id.equalsIgnoreCase(rs.getString("sgs_create_usr_id"))) {
					res.setIs_creator(true);
				} else {
					res.setIs_creator(false);
				}
				res.setSgs_title_noescape(res.getSgs_title());
				res.setSgs_desc_noescape(res.getSgs_desc());
				res.setSgs_content_noescape(res.getSgs_content());
				resVc.add(res);
			}
			count++;
		}
		param.setTotal_rec(count);
		if (stmt != null)
			stmt.close();
		return resVc;
	}
	/**
	 * 学习小组资源列表
	 * @param con
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	public Vector getStudyGroupResLst(Connection con, loginProfile prof, long sgp_id, WizbiniLoader wizbini) throws SQLException{
		String sql ="SELECT sgr_sgp_id, sgs_id,sgs_title, sgs_type, sgs_content,sgs_desc ,sgs_upd_timestamp ,sgs_create_usr_id FROM studyGroupResources" +
				" INNER JOIN studyGroupRelation ON (sgr_ent_id = sgs_id AND sgr_type = ?)" +
				" WHERE sgr_sgp_id = ?" +
				" Order by sgs_create_timestamp desc";
		PreparedStatement stmt = null;
		stmt = con.prepareStatement(sql);
		int index =1;
		stmt.setString(index++, SGR_TYPE_SGP_RESOURCE);
		stmt.setLong(index++, sgp_id);
		ResultSet rs = stmt.executeQuery();
		int count=0;
		int start = 0;
		int limit = 3;
		Vector resVc = new Vector();
		while(rs.next()){
			if(count >= start && count <(start + limit)){
				StudyGroupResBean res =new StudyGroupResBean();
				res.setSgs_id(rs.getLong("sgs_id"));
				res.setSgr_sgp_id(rs.getLong("sgr_sgp_id"));
				res.setSgs_title(rs.getString("sgs_title"));
				res.setSgs_type(rs.getString("sgs_type"));
				if(SGS_TYPE_DOC.equalsIgnoreCase(res.getSgs_type())){
					String docDir=cwUtils.getFileURL(wizbini.cfgSysSetupadv.getFileUpload().getSgpResDir().getUrl())+res.getSgs_id()+"/"+rs.getString("sgs_content");
					res.setSgs_content(docDir);
				}else{
					res.setSgs_content(rs.getString("sgs_content"));
				}
				res.setSgs_desc(rs.getString("sgs_desc"));
				res.setSgs_upd_timestamp(rs.getTimestamp("sgs_upd_timestamp"));
				if(prof.usr_ste_usr_id.equalsIgnoreCase(rs.getString("sgs_create_usr_id"))){
					res.setIs_creator(true);
				}else{
					res.setIs_creator(false);
				}
				res.setSgs_title_noescape(res.getSgs_title());
				res.setSgs_desc_noescape(res.getSgs_desc());
				res.setSgs_content_noescape(res.getSgs_content());
				resVc.add(res);
			}
			count++;
		}
		if(stmt !=null) stmt.close();
		return resVc;
	}
	public Vector getStudyGroupResLst(Connection con,  long sgp_id) throws SQLException{
		String sql ="SELECT sgs_id,sgs_title, sgs_type, sgs_content,sgs_desc ,sgs_upd_timestamp ,sgs_create_usr_id FROM studyGroupResources" +
		" INNER JOIN studyGroupRelation ON (sgr_ent_id = sgs_id AND sgr_type = ?)" +
		" WHERE sgr_sgp_id = ?";
		PreparedStatement stmt = null;
		stmt = con.prepareStatement(sql);
		int index =1;
		stmt.setString(index++, SGR_TYPE_SGP_RESOURCE);
		stmt.setLong(index++,sgp_id);
		ResultSet rs = stmt.executeQuery();
		int count=0;
		Vector resVc = new Vector();
		while(rs.next()){
			if(count<SGP_TOPIC_SHOW_NUM){
				StudyGroupResBean res =new StudyGroupResBean();
				res.setSgs_id(rs.getLong("sgs_id"));
				res.setSgs_title(rs.getString("sgs_title"));
				res.setSgs_type(rs.getString("sgs_type"));
				res.setSgs_content(rs.getString("sgs_content"));
				res.setSgs_desc(rs.getString("sgs_desc"));
				resVc.add(res);
			}
			count++;
		}
		if(stmt !=null) stmt.close();
		return resVc;
	}

	/**
	 * 编辑学习小组资料
	 * @param con
	 * @param sgp
	 * @throws SQLException
	 */
	public boolean updStudyGroupInfo(Connection con,StudyGroupDB sgp) throws SQLException{
		String sql=" update studygroup set sgp_title=?,sgp_tcr_id = ? ,sgp_desc=?, sgp_public_type=?, sgp_send_email_ind=? where sgp_id=?" ;
		PreparedStatement stmt = null;
		stmt = con.prepareStatement(sql);
		int index =1;
		stmt.setString(index++, sgp.sgp_title);
		stmt.setLong(index++, sgp.sgp_tcr_id);
		stmt.setString(index++, sgp.sgp_desc);
		stmt.setInt(index++, sgp.sgp_public_type);
		stmt.setInt(index++, sgp.sgp_send_email_ind);
		stmt.setLong(index++, sgp.sgp_id);
		int cut=stmt.executeUpdate();
		if(stmt !=null) stmt.close();
		if(cut>0)
			return true;
		else
			return false;
	}

	/**
	 * 添加资源
	 * @param con
	 * @param sgpRes
	 * @throws SQLException
	 * @throws qdbException
	 */
	public boolean addStudyGroupRes(Connection con,StudyGroupResDB sgpRes,long sgp_id) throws SQLException, qdbException{
		String sql="insert into studyGroupResources(sgs_title ,sgs_type  ,sgs_content   ,sgs_desc  ,sgs_create_timestamp ,sgs_create_usr_id, sgs_upd_timestamp,sgs_upd_usr_id )"+
				" values(?,?,?,?,?,?,?,?)";
		String sql2="insert into studyGroupRelation(sgr_sgp_id ,sgr_ent_id ,sgr_type,sgr_create_timestamp,sgr_create_usr_id)" +
		" values(?, ?, ?,?,?)";
		PreparedStatement stmt = null;
		stmt = con.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);
		int index =1;
		stmt.setString(index++, sgpRes.sgs_title);
		stmt.setString(index++, sgpRes.sgs_type);
		stmt.setString(index++, sgpRes.sgs_content);
		stmt.setString(index++, sgpRes.sgs_desc);
		stmt.setTimestamp(index++, sgpRes.sgs_create_timestamp);
		stmt.setString(index++, sgpRes.sgs_create_usr_id);
		stmt.setTimestamp(index++, sgpRes.sgs_upd_timestamp);
		stmt.setString(index++, sgpRes.sgs_upd_usr_id);
		int cut=stmt.executeUpdate();
		long  sgs_id= cwSQL.getAutoId(con, stmt, "studyGroupResources", "sgs_id");
		sgpRes.sgs_id=sgs_id;
		if(cut>0){
			if(stmt !=null) stmt.close();
			index=1;
			PreparedStatement stmt2 = con.prepareStatement(sql2);
			stmt2.setLong(index++, sgp_id);
			stmt2.setLong(index++, sgs_id);
			stmt2.setString(index++, SGR_TYPE_SGP_RESOURCE);
			stmt2.setTimestamp(index++, sgpRes.sgs_create_timestamp);
			stmt2.setString(index++, sgpRes.sgs_create_usr_id);
			int cut2=stmt2.executeUpdate();
			if(cut2 !=1){
	            stmt2.close();
	            con.rollback();
	            return false;
	        }else{
	        	if(stmt2 !=null) stmt2.close();

	        	return true;
	        }
		}else{
			stmt.close();
	        con.rollback();
			return false;
		}
	}

	/**
	 * 获取资源详细信息
	 * @param con
	 * @param sgs_id
	 * @return
	 * @throws SQLException
	 */
	public StudyGroupResBean getStudyGroupRes(Connection con, long sgs_id) throws SQLException{
		String sql="select sgs_id,sgs_title,sgs_type ,sgs_content ,sgs_desc ,sgs_create_timestamp ,sgs_create_usr_id ,sgs_upd_timestamp ,sgs_upd_usr_id  from studyGroupResources " +
				" where sgs_id=?";
		PreparedStatement stmt = null;
		stmt = con.prepareStatement(sql);
		int index =1;
		stmt.setLong(index++, sgs_id);
		ResultSet rs = stmt.executeQuery();
		StudyGroupResBean res =new StudyGroupResBean();
		while(rs.next()){
			res.setSgs_id(rs.getLong("sgs_id"));
			res.setSgs_title(rs.getString("sgs_title"));
			res.setSgs_desc(rs.getString("sgs_desc"));
			res.setSgs_content(rs.getString("sgs_content"));
			res.setSgs_type(rs.getString("sgs_type"));
			res.setSgs_upd_timestamp(rs.getTimestamp("sgs_upd_timestamp"));
		}

		if(stmt !=null)stmt.close();
		return res;
	}

	/**
	 * 修改资源
	 * @param con
	 * @param sgpRes
	 * @return
	 * @throws SQLException
	 */
	public boolean updStudyGroupRes(Connection con,StudyGroupResDB sgpRes) throws SQLException{
		String sql="update studyGroupResources set sgs_title=? ,sgs_type=? " ;
		if(sgpRes.sgs_content !=null && sgpRes.sgs_content.length()>0){
			sql +=" ,sgs_content=?  " ;
		}
		sql +=" ,sgs_desc=?  , sgs_upd_timestamp=?,sgs_upd_usr_id=?" +
				" where sgs_id=?";
		PreparedStatement stmt = null;
		stmt = con.prepareStatement(sql);
		int index =1;
		stmt.setString(index++, sgpRes.sgs_title);
		stmt.setString(index++, sgpRes.sgs_type);
		if(sgpRes.sgs_content !=null && sgpRes.sgs_content.length()>0){
			stmt.setString(index++, sgpRes.sgs_content);
		}
		stmt.setString(index++, sgpRes.sgs_desc);
		stmt.setTimestamp(index++, sgpRes.sgs_upd_timestamp);
		stmt.setString(index++, sgpRes.sgs_upd_usr_id);
		stmt.setLong(index++, sgpRes.sgs_id);
		int cut=stmt.executeUpdate();
		if(stmt !=null) stmt.close();
		if(cut>0)
			return true;
		else
			return false;
	}

	/**
	 * 删除资源
	 * @param con
	 * @param sgpRes
	 * @throws SQLException
	 */
	public boolean delStudyGroupRes(Connection con,StudyGroupResDB sgpRes) throws SQLException{
		boolean isUpd=false;
		String sqlRela="delete from studyGroupRelation where sgr_ent_id=? and sgr_type=?";
		PreparedStatement stmt1 = null;
		stmt1 = con.prepareStatement(sqlRela);
		stmt1.setLong(1, sgpRes.sgs_id);
		stmt1.setString(2, sgpRes.sgs_type);
		int cut=stmt1.executeUpdate();
		if(cut!=1){
			con.rollback();
		}else{
			String sql = "delete from studyGroupResources where sgs_id = ?";
			PreparedStatement stmt = null;
			stmt = con.prepareStatement(sql);
			stmt.setLong(1, sgpRes.sgs_id);
			int cut2=stmt.executeUpdate();
			if(stmt !=null) stmt.close();
			if(cut2>0){
				isUpd=true;
			}else{
				isUpd= false;
			}
		}
		stmt1.close();
		return isUpd;
	}

	/**
	 * 获取手动添加的成员
	 * @param con
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	public Vector getCustAddMem(Connection con ,StudyGroupModuleParam param) throws SQLException{
		String sql="SELECT usr_ent_id, usr_display_bil, usr_nickname FROM studyGroupMember "+
				" INNER JOIN reguser ON (usr_ent_id = sgm_ent_id AND usr_status = 'OK')"+
				" WHERE sgm_type = 'USR' AND sgm_sgp_id = ? ";
		PreparedStatement stmt = null;
		stmt = con.prepareStatement(sql);
		stmt.setLong(1, param.getSgp_id());
		ResultSet rs = stmt.executeQuery();
		Vector memVc = new Vector();
		String name = null;
		while(rs.next()){
			OptionBean opt =new OptionBean();
			opt.setId(rs.getLong("usr_ent_id"));

			name = rs.getString("usr_nickname");
			if (name != null && name.length() > 0) {
				name = rs.getString("usr_display_bil") + "(" + name + ")";
			} else {
				name = rs.getString("usr_display_bil");
			}
			opt.setText(name);
			/*StudyGroupMemUsrBean mem = new StudyGroupMemUsrBean();
			mem.setUsr_ent_id(rs.getLong("usr_ent_id"));
			mem.setUsr_nickname(rs.getString("usr_nickname"));*/
			memVc.add(opt);
		}
		if(stmt !=null) stmt.close();
		return memVc;
	}

	/**
	 * 删除学习小组的成员
	 * @param con
	 * @param sgpMem
	 * @throws SQLException
	 */
	public boolean delStudyGroupMem(Connection con ,StudyGroupMemDB sgpMem) throws SQLException{
		String sql ="delete from studyGroupMember where sgm_sgp_id= ? and sgm_type=?";
		PreparedStatement stmt = null;
		stmt = con.prepareStatement(sql);
		stmt.setLong(1, sgpMem.sgm_sgp_id);
		stmt.setString(2, sgpMem.sgm_type);
		int cut=stmt.executeUpdate();
		if(stmt !=null) stmt.close();
		if(cut>0)
			return true;
		else
			return false;
	}

	/**
	 * 添加成员
	 * @param con
	 * @param sgpMem
	 * @throws SQLException
	 */
	public boolean insStudyGroupMem(Connection con ,StudyGroupMemDB sgpMem) throws SQLException{
		String sql = "insert into studyGroupMember (sgm_sgp_id ,sgm_ent_id ,sgm_type ,sgm_status ,sgm_create_timestamp ,sgm_create_usr_id ,sgm_upd_timestamp,sgm_upd_usr_id)" +
				" values(?,?,?,?,?,?,?,?)";
		PreparedStatement stmt = null;
		stmt = con.prepareStatement(sql);
		int index =1;
		stmt.setLong(index++, sgpMem.sgm_sgp_id);
		stmt.setLong(index++,sgpMem.sgm_ent_id);
		stmt.setString(index++, sgpMem.sgm_type);
		stmt.setString(index++,sgpMem.sgm_status);
		stmt.setTimestamp(index++, sgpMem.sgm_create_timestamp);
		stmt.setString(index++, sgpMem.sgm_create_usr_id);
		stmt.setTimestamp(index++, sgpMem.sgm_upd_timestamp);
		stmt.setString(index++, sgpMem.sgm_upd_usr_id);
		int cut=stmt.executeUpdate();
		if(stmt !=null) stmt.close();
		if(cut>0)
			return true;
		else
			return false;
	}

	/**
	 * 获取通过用户组添加的成员
	 * @param con
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	public Vector getStudyGroupMemGroup(Connection con ,StudyGroupModuleParam param) throws SQLException{
		String sql ="SELECT usg_ent_id, usg_display_bil FROM userGroup" +
				" INNER JOIN studyGroupMember ON (sgm_ent_id = usg_ent_id AND sgm_type = 'USG')" +
				" WHERE sgm_sgp_id = ? ";
		PreparedStatement stmt = null;
		stmt = con.prepareStatement(sql);
		stmt.setLong(1, param.getSgp_id());
		ResultSet rs = stmt.executeQuery();
		Vector usgVc= new Vector();
		while(rs.next()){
			OptionBean opt =new OptionBean();
			opt.setId(rs.getLong("usg_ent_id"));
			opt.setText(rs.getString("usg_display_bil"));
			/*StudyGroupMemUsgBean usg =new StudyGroupMemUsgBean();
			usg.setUsg_ent_id(rs.getLong("usg_ent_id"));
			usg.setUsg_display_bil(rs.getString("usg_display_bil"));*/
			usgVc.add(opt);
		}
		if(stmt !=null)stmt.close();
		return usgVc;
	}

	/**
	 * 通过课程或班级添加的成员
	 * @param con
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	public Vector getStudyGroupMemItm(Connection con ,StudyGroupModuleParam param) throws SQLException{
		String sql="select itm_id, itm_title, "+cwSQL.get_null_sql(cwSQL.COL_TYPE_STRING)+" as parent_itm_title from aeitem" +
				" INNER JOIN studyGroupMember ON (sgm_ent_id = itm_id AND sgm_type = 'ITM' OR itm_type='VIDEO')" +
				" where itm_type='SELFSTUDY' AND sgm_sgp_id = ?" +
				" union " +
				" select c.itm_id, c.itm_title, p.itm_title as parent_itm_title from aeitem c" +
				" INNER JOIN studyGroupMember ON (sgm_ent_id = c.itm_id AND sgm_type = 'ITM')" +
				" inner join aeItemRelation on (ire_child_itm_id = c.itm_id)" +
				" inner join aeItem p on (p.itm_id = ire_parent_itm_id)" +
				" where c.itm_type='CLASSROOM' and c.itm_run_ind=1 AND sgm_sgp_id = ?" ;
		PreparedStatement stmt = null;
		stmt = con.prepareStatement(sql);
		stmt.setLong(1, param.getSgp_id());
		stmt.setLong(2, param.getSgp_id());
		ResultSet rs = stmt.executeQuery();
		Vector itmVc =new Vector();
		while(rs.next()){
			OptionBean opt =new OptionBean();
			opt.setId(rs.getLong("itm_id"));
			String itmTitle = rs.getString("itm_title");
			String parentItmTitle = rs.getString("parent_itm_title");
			if(parentItmTitle == null || "".equals(parentItmTitle)) {
				opt.setText(itmTitle);
			} else {
				opt.setText(itmTitle + "(" + parentItmTitle + ")");
			}
			itmVc.add(opt);
		}
		if(stmt !=null)stmt.close();
		return itmVc;
	}


	/**
	 * 获取学习中心的培训中心下的课程或班级
	 * @param con
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	public Vector getItmBySgp(Connection con, StudyGroupModuleParam param) throws SQLException{
		String sql="select   itm_id, itm_title from aeitem " +
				" INNER JOIN studyGroup on(sgp_tcr_id=itm_tcr_id)" +
				" where sgp_id =? and (itm_type='SELFSTUDY OR itm_type='VIDEO')'" +
				" union" +
				" select   itm_id, itm_title from aeitem " +
				" INNER JOIN studyGroup on(sgp_tcr_id=itm_tcr_id)" +
				" where sgp_id =? and  itm_type='CLASSROOM' and itm_run_ind=1" ;
		PreparedStatement stmt = null;
		stmt = con.prepareStatement(sql);
		stmt.setLong(1, param.getSgp_id());
		stmt.setLong(2, param.getSgp_id());
		ResultSet rs = stmt.executeQuery();
		Vector itmVc =new Vector();
		while(rs.next()){
			StudyGroupMemItmBean itm =new StudyGroupMemItmBean();
			itm.setItm_id(rs.getLong("itm_id"));
			itm.setItm_title(rs.getString("itm_title"));
			itmVc.add(itm);
		}
		if(stmt !=null)stmt.close();
		return itmVc;
	}

	/**
	 * 添加学习小组（同时添加学习小组组长）
	 * @param con
	 * @param prof
	 * @param sgp
	 * @param mgtVc
	 * @return
	 * @throws SQLException
	 * @throws qdbException
	 */
	public long insStudyGroup(Connection con ,loginProfile prof, StudyGroupDB sgp,Vector mgtVc) throws SQLException, qdbException{
		String sql=" insert into studyGroup (sgp_tcr_id,sgp_title,sgp_desc,sgp_public_type,sgp_create_usr_id, sgp_create_timestamp,sgp_upd_timestamp, sgp_send_email_ind)" +
				" values(?,?,?,?,?,?,?,?)" ;
		String sql2="insert into studyGroupRelation(sgr_sgp_id ,sgr_ent_id ,sgr_type,sgr_create_timestamp,sgr_create_usr_id)" +
				" values(?, ?, ?,?,?)";
		PreparedStatement stmt = null;
		stmt = con.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);
		Timestamp cur_time=cwSQL.getTime(con);
		int index=1;
		stmt.setLong(index++, sgp.sgp_tcr_id);
		stmt.setString(index++, sgp.sgp_title);
		stmt.setString(index++, sgp.sgp_desc);
		stmt.setInt(index++, sgp.sgp_public_type);
		stmt.setString(index++, prof.usr_ste_usr_id);
		stmt.setTimestamp(index++, cur_time);
		stmt.setTimestamp(index++, cur_time);
		stmt.setInt(index++, sgp.sgp_send_email_ind);
		int cut=stmt.executeUpdate();
		if(cut != 1 )
        {
            stmt.close();
            con.rollback();
            throw new qdbException("Fails to insert studyGroup");
        }
		long  sgp_id= cwSQL.getAutoId(con, stmt, "studyGroup", "sgp_id");

		if(mgtVc !=null && !mgtVc.isEmpty()){
			Iterator iter=mgtVc.iterator();
			while(iter.hasNext()){
				PreparedStatement stmt2 = con.prepareStatement(sql2);
				String idStr=(String)iter.next();
				long mgtId=Long.parseLong(idStr);
				stmt2.setLong(1,sgp_id);
				stmt2.setLong(2,mgtId);
				stmt2.setString(3, SGR_TYPE_SGP_MANAGER);
				stmt2.setTimestamp(4, cur_time);
				stmt2.setString(5, prof.usr_ste_usr_id);
				int cut_mgt=stmt2.executeUpdate();
				if(cut_mgt !=1){
		            stmt2.close();
		            con.rollback();
		            throw new qdbException("Fails to insert studyGroupRelation");
		        }
				if(stmt2!=null)stmt2.close();
			}
		}
		if(stmt!=null)stmt.close();
		return sgp_id;
	}


	public void insStudyGroupMgt(Connection con ,loginProfile prof,long sgp_id,Vector mgtVc) throws SQLException, qdbException{
		String sql2="insert into studyGroupRelation(sgr_sgp_id ,sgr_ent_id ,sgr_type,sgr_create_timestamp,sgr_create_usr_id)" +
		" values(?, ?, ?,?,?)";
		Timestamp cur_time=cwSQL.getTime(con);
		if(mgtVc !=null && !mgtVc.isEmpty()){
			Iterator iter=mgtVc.iterator();
			while(iter.hasNext()){
				PreparedStatement stmt2 = con.prepareStatement(sql2);
				String idStr=(String)iter.next();
				long mgtId=Long.parseLong(idStr);
				stmt2.setLong(1,sgp_id);
				stmt2.setLong(2,mgtId);
				stmt2.setString(3, SGR_TYPE_SGP_MANAGER);
				stmt2.setTimestamp(4, cur_time);
				stmt2.setString(5, prof.usr_ste_usr_id);
				int cut_mgt=stmt2.executeUpdate();
				if(cut_mgt !=1){
		            stmt2.close();
		            con.rollback();
		            throw new qdbException("Fails to insert studyGroupRelation");
		        }
				if(stmt2!=null)stmt2.close();
			}
		}

	}
	/**
	 * 添加学习小组讨论区
	 * @param con
	 * @param prof
	 * @param sgp_id
	 * @param res_id
	 * @throws SQLException
	 * @throws qdbException
	 */
	public void insForumSgpRelation(Connection con,loginProfile prof, long sgp_id ,long res_id) throws SQLException, qdbException{
		String sql="insert into studyGroupRelation(sgr_sgp_id ,sgr_ent_id ,sgr_type,sgr_create_timestamp,sgr_create_usr_id)" +
				"values(?,?,?,?,?)";
		Timestamp cur_time=cwSQL.getTime(con);
		PreparedStatement stmt = null;
		stmt = con.prepareStatement(sql);
		int index=1;
		stmt.setLong(index++, sgp_id);
		stmt.setLong(index++, res_id);
		stmt.setString(index++, SGR_TYPE_SGP_DISCUSS);
		stmt.setTimestamp(index++, cur_time);
		stmt.setString(index++,  prof.usr_ste_usr_id);
		int cut=stmt.executeUpdate();
		if(cut!=1){
            stmt.close();
            throw new qdbException("Fails to insert studyGroupRelation");
        }
		if(stmt!=null)stmt.close();
	}

	/**
	 * 获取学习小组的讨论区
	 * @param con
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	public Vector getSgpForumTopic(Connection con, long sgp_id, Map topicMap) throws SQLException {
		String sql = "SELECT fto_id,fto_res_id,fto_title,sgr_ent_id FROM studyGroupRelation "
				+ " LEFT JOIN ForumTopic ON (sgr_ent_id = fto_res_id)"
				+ " where sgr_sgp_id = ?  and sgr_type=? ORDER BY fto_last_post_datetime DESC";
		PreparedStatement stmt = null;
		stmt = con.prepareStatement(sql);
		stmt.setLong(1, sgp_id);
		stmt.setString(2, SGR_TYPE_SGP_DISCUSS);
		ResultSet rs = stmt.executeQuery();
		Vector topicVc =new Vector();
		int i=0;
		long mod_id=0;
		while(rs.next()){
			if(i<SGP_TOPIC_SHOW_NUM){
				ForumTopicBean topic =new ForumTopicBean();
				topic.setFto_id(rs.getLong("fto_id"));
				topic.setFto_res_id(rs.getLong("fto_res_id"));
				topic.setFto_title(rs.getString("fto_title"));
				mod_id=rs.getLong("sgr_ent_id");
				topic.setMod_id(mod_id);
				if(topic.getFto_id()!=0){
					topicVc.add(topic);
				}
			}
			i++;
		}
		if(stmt !=null)stmt.close();
		if(topicMap!=null){
			topicMap.put("mod_id", new Long(mod_id));
			topicMap.put("dis_lst", topicVc);
		}
		return topicVc;
	}

	/**
	 * 获取课程的所有的学习小组
	 * @param con
	 * @param itm_id
	 * @return
	 * @throws SQLException
	 */
	public Vector getItmStudyGroups(Connection con,long itm_id)throws SQLException{
		String sql="select sgp_id,sgp_title from studyGroupMember,studyGroup" +
				" where sgm_sgp_id=sgp_id and sgm_type='ITM' and sgm_ent_id=? order by sgp_id asc" ;
		PreparedStatement stmt = null;
		stmt = con.prepareStatement(sql);
		stmt.setLong(1, itm_id);
		ResultSet rs = stmt.executeQuery();
		Vector sgpVc =new Vector();
		while(rs.next()){
			StudyGroupBean sgp =new StudyGroupBean();
			sgp.setSgp_id(rs.getLong("sgp_id"));
			sgp.setSgp_title(rs.getString("sgp_title"));
			sgpVc.add(sgp);
		}
		if(stmt !=null)stmt.close();
		return sgpVc;
	}
	/**
	 * 获取课程所有的学习小组的讨论区信息
	 * @param con
	 * @param itm_id
	 * @return
	 * @throws SQLException
	 */
	public Vector getItmAllDis(Connection con,long itm_id) throws SQLException{
		String sql="select sgm_sgp_id,fto_id,fto_title,sgr_ent_id FROM studyGroupMember " +
				" inner join studyGroupRelation ON (sgr_sgp_id = sgm_sgp_id AND sgr_type = ?)" +
				" left join ForumTopic on(sgr_ent_id=fto_res_id)" +
				" where sgm_type='ITM' and sgm_ent_id=? ORDER BY fto_last_post_datetime DESC";
		PreparedStatement stmt = null;
		stmt = con.prepareStatement(sql);
		stmt.setString(1, SGR_TYPE_SGP_DISCUSS);
		stmt.setLong(2, itm_id);
		ResultSet rs = stmt.executeQuery();
		Hashtable sgpHs =new Hashtable();
		Vector sgpVc =new Vector();
		int resCnt=1;
		Hashtable disModHs= new Hashtable();
		while(rs.next()){
			Hashtable resHs =new Hashtable();
			ForumTopicBean topic =new ForumTopicBean();
			topic.setFto_id(rs.getLong("fto_id"));
			topic.setFto_title(rs.getString("fto_title"));
			Long idObj =new Long(rs.getLong("sgm_sgp_id"));
			topic.setMod_id(rs.getLong("sgr_ent_id"));
			disModHs.put(idObj, new Long(rs.getLong("sgr_ent_id")));
			if(sgpHs.containsKey(idObj)){
				resHs=(Hashtable)sgpHs.get(idObj);
				resCnt=resHs.size();
				if(resCnt<SGP_TOPIC_SHOW_NUM){
					resHs.put(new Integer(resCnt+1), topic);
					sgpHs.put(idObj, resHs);
				}
			}else{
				resHs.put(new Integer(resCnt), topic);
				sgpHs.put(idObj, resHs);
			}
		}
		if(stmt !=null)stmt.close();
		itmAllDisValues(sgpVc, sgpHs,disModHs);
		return sgpVc;
	}
	/**
	 * 匹配讨论区信息值
	 * @param sgpVc
	 * @param sgpHs
	 * @throws SQLException
	 */
	public void itmAllDisValues(Vector sgpVc ,Hashtable sgpHs,Hashtable disModHs) throws SQLException{
		if(sgpHs!=null && !sgpHs.isEmpty()){
			Enumeration Enum=sgpHs.keys();
			while(Enum.hasMoreElements()){
				Long idObj= (Long)Enum.nextElement();
				Hashtable resHs=(Hashtable)sgpHs.get(idObj);
				Vector resVc =new Vector();
				if(resHs !=null &&!resHs.isEmpty()){
					Enumeration Enum2=resHs.elements();
					while(Enum2.hasMoreElements()){
						ForumTopicBean topic =(ForumTopicBean)Enum2.nextElement();
						if(topic.getFto_id()!=0){
							resVc.add(topic);
						}

					}
				}
				ItmSgpForumBean sgp =new ItmSgpForumBean();
				sgp.setSgp_id(idObj.longValue());
				sgp.setTopicLst(resVc);
				Long mod_id_Long = (Long)disModHs.get(idObj);
				sgp.setMod_id(mod_id_Long.longValue());
				sgpVc.add(sgp);
			}
		}
	}

	/**
	 * 统计学习小组的成员数包括组长
	 * @param con
	 * @param sgp_id_lst
	 * @param memHs
	 * @throws SQLException
	 */
	public void getStudyGroupMemAndMgtNum(Connection con,String sgp_id_lst , Hashtable memHs)throws SQLException{
		StringBuffer sqlBuf=new StringBuffer();
		sqlBuf.append("SELECT sgp_id, COUNT(distinct usr_ent_id) AS sgm_member_total FROM (")
			  .append(" SELECT sgm_sgp_id sgp_id, usr_ent_id FROM studyGroupMember")
			  .append(" INNER JOIN regUser ON (usr_ent_id = sgm_ent_id AND usr_status = 'OK')")
			  .append(" WHERE sgm_type = ? AND sgm_sgp_id in ("+sgp_id_lst+") ")
			  .append(" UNION")
			  .append(" SELECT sgm_sgp_id sgp_id, usr_ent_id  FROM studyGroupMember")
			  .append(" INNER JOIN aeItem ON (sgm_ent_id = itm_id)")
			  .append(" INNER JOIN aeApplication ON (app_itm_id = itm_id AND app_status = ?)")
			  .append(" INNER JOIN regUser ON (usr_ent_id = app_ent_id AND usr_status = 'OK')")
			  .append(" WHERE sgm_type = ? AND sgm_sgp_id in ("+sgp_id_lst+")")
			  .append(" UNION")
			  .append(" SELECT sgm_sgp_id sgp_id, usr_ent_id FROM studyGroupMember ")
			  .append(" INNER JOIN EntityRelation ON (ern_ancestor_ent_id = sgm_ent_id AND ern_type = ?)")
			  .append(" INNER JOIN regUser ON (usr_ent_id = ern_child_ent_id AND usr_status = 'OK')")
			  .append(" WHERE sgm_type = ? AND sgm_sgp_id in ("+sgp_id_lst+")")
			  .append(" UNION")
			  .append(" SELECT sgr_sgp_id sgp_id, sgr_ent_id usr_ent_id FROM studygrouprelation")
			  .append(" INNER JOIN regUser ON (usr_ent_id = sgr_ent_id AND usr_status = 'OK')")
			  .append(" WHERE sgr_type=? AND sgr_sgp_id in("+sgp_id_lst+")")
			  .append(" ) mem GROUP BY sgp_id");
		PreparedStatement stmt = null;
		stmt = con.prepareStatement(sqlBuf.toString());
		int index =1;
		stmt.setString(index++,SGM_TYPE_USR);
		stmt.setString(index++,aeApplication.ADMITTED);
		stmt.setString(index++, SGM_TYPE_ITM);
		stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
		stmt.setString(index++, SGM_TYPE_USG);
		stmt.setString(index++, SGR_TYPE_SGP_MANAGER);
		ResultSet rs = stmt.executeQuery();
		while(rs.next()){
			memHs.put(new Long(rs.getLong("sgp_id")), new Integer(rs.getInt("sgm_member_total")));
		}
		if(stmt !=null)stmt.close();
	}
	/**
	 * 按培训中心取学习小组（管理员）
	 * @param con
	 * @param prof
	 * @param param
	 * @param sgpVc
	 * @param noTc
	 * @return
	 * @throws SQLException
	 */
	public Hashtable getTaMgtSgps(Connection con, loginProfile prof, StudyGroupModuleParam param,Vector sgpVc ,boolean noTc) throws SQLException{
		String sql="SELECT sgp_id, sgp_title ,sgp_public_type ,sgp_upd_timestamp,sgp_tcr_id FROM studyGroup" ;
		if(!noTc ){
			if(param.getTcr_id()!=0){
				sql=sql+" where sgp_tcr_id=?";
			}else {
				sql=sql+" where sgp_tcr_id in "+param.getTcr_id_lst();
			}
		}
		if(param.getSort()==null ||param.getSort().length()==0 ){
			sql=sql+"  ORDER BY sgp_upd_timestamp DESC";
		}else{
			sql=sql+"  ORDER BY "+param.getSort()+" "+param.getDir();
		}
		PreparedStatement stmt = null;
	    stmt = con.prepareStatement(sql);
	    int index=1;
	    if(!noTc){
	    	if(param.getTcr_id()!=0){
	    		stmt.setLong(index++, param.getTcr_id());
	    	}
	    }
	    ResultSet rs = stmt.executeQuery();
	    int count=0;
	    StringBuffer idBuf =new StringBuffer();
	    StringBuffer allIdBuf = new StringBuffer();
	    while(rs.next()){
	    	if(count >=param.getStart()&& count <(param.getStart()+param.getLimit())){
	    		StudyGroupBean sgp= new StudyGroupBean();
	    		sgp.setSgp_id(rs.getLong("sgp_id"));
	    		idBuf.append(",").append(sgp.getSgp_id());
	    		sgp.setSgp_title(rs.getString("sgp_title"));
	    		sgp.setSgp_public_type(rs.getInt("sgp_public_type"));
	    		sgp.setCan_view(true);
	    		sgp.setSgp_upd_timestamp(rs.getTimestamp("sgp_upd_timestamp"));
	    		sgp.setSgp_tcr_id(rs.getLong("sgp_tcr_id"));
	    		sgpVc.add(sgp);
	    	}
	    	allIdBuf.append(",").append(rs.getLong("sgp_id"));
	    	count++;
	    }
	    param.setTotal_rec(count);
	    Hashtable idStrHs =new Hashtable();
	    if(stmt !=null)stmt.close();
	    if(idBuf.length()>0){
	    	idBuf=idBuf.deleteCharAt(0);
	    	idStrHs.put("idStr", idBuf.toString());
	    }
	    if(allIdBuf.length()>0){
	    	allIdBuf=allIdBuf.deleteCharAt(0);
	    	idStrHs.put("allIdStr", allIdBuf.toString());
	    }
	    if(stmt !=null)stmt.close();
	    return idStrHs;
	}
	/**
	 * 获取学习小组的培训中心
	 * @param con
	 * @param sgp_id
	 * @return
	 * @throws SQLException
	 */
	public long getSgpTc(Connection con, long sgp_id) throws SQLException{
		String sql="select sgp_tcr_id from studygroup where sgp_id=?";
		PreparedStatement stmt = null;
		long tcr_id=0;
	    stmt = con.prepareStatement(sql);
	    int index=1;
	    stmt.setLong(index++, sgp_id);
	    ResultSet rs = stmt.executeQuery();
	    if(rs.next()){
	    	tcr_id=rs.getLong("sgp_tcr_id");
	    }
	    if(stmt !=null)stmt.close();
		return tcr_id;
	}
	/**
	 * 删除学习小组的组长
	 * @param con
	 * @param sgp_id
	 * @throws SQLException
	 * @throws qdbException
	 */
	public void delStudyGroupMgtBySgp(Connection con, long sgp_id)throws SQLException, qdbException{
		String sql="delete from studyGroupRelation where sgr_sgp_id=? and sgr_type=?";
		PreparedStatement stmt = null;
	    stmt = con.prepareStatement(sql);
	    int index=1;
	    stmt.setLong(index++, sgp_id);
	    stmt.setString(index++, SGR_TYPE_SGP_MANAGER);
	    int cnt = stmt.executeUpdate();
	    if(stmt !=null) stmt.close();
	}

	/**
	 * 删除学习小组关系表（是否包括学习小组的论坛）
	 * @param con
	 * @param sgp_id
	 * @param delForum
	 * @throws SQLException
	 * @throws qdbException
	 */
	public void delAllStudyGroupRelation(Connection con, long sgp_id, boolean delForum)throws SQLException, qdbException{
		String sql="delete from studyGroupRelation where sgr_sgp_id=?";
		if(!delForum){
			sql=sql+" and sgr_type not like ?";
		}
		PreparedStatement stmt = null;
	    stmt = con.prepareStatement(sql);
	    int index=1;
	    stmt.setLong(index++, sgp_id);
	    if(!delForum){
	    	stmt.setString(index++, SGR_TYPE_SGP_DISCUSS);
	    }
	    int cnt = stmt.executeUpdate();
	    if(stmt !=null) stmt.close();
	}

	/**
	 * 删除学校小组资源
	 * @param con
	 * @param sgp_id
	 * @throws SQLException
	 * @throws qdbException
	 */
	public void delStudyGroupResourceBySgp(Connection con, long sgp_id)throws SQLException, qdbException{
		String sql = "delete from studyGroupResources where sgs_id = ?";
		PreparedStatement stmt = null;
		stmt = con.prepareStatement(sql);
		stmt.setLong(1, sgp_id);
		int cnt=stmt.executeUpdate();
		if(stmt !=null) stmt.close();
	}

	/**
	 * 删除学习小组成员
	 * @param con
	 * @param sgp_id
	 * @throws SQLException
	 * @throws qdbException
	 */
	public void delStudyGroupMemberBySgp(Connection con, long sgp_id)throws SQLException, qdbException{
		String sql="delete from StudyGroupMember where sgm_sgp_id=?";
		PreparedStatement stmt = null;
	    stmt = con.prepareStatement(sql);
	    int index=1;
	    stmt.setLong(index++, sgp_id);
	    int cnt = stmt.executeUpdate();
	    if(stmt !=null) stmt.close();
	}

	/**
	 * 删除学习小组
	 * @param con
	 * @param sgp_id
	 * @throws SQLException
	 * @throws qdbException
	 */
	public void delStudyGroupById(Connection con, long sgp_id)throws SQLException, qdbException{
		String sql="delete from StudyGroup where sgp_id=?";
		PreparedStatement stmt = null;
	    stmt = con.prepareStatement(sql);
	    int index=1;
	    stmt.setLong(index++, sgp_id);
	    int cnt = stmt.executeUpdate();
	    if(stmt !=null) stmt.close();
	}

	/**
	 * 查询学习小组讨论区的id
	 * @param con
	 * @param sgp_id
	 * @return
	 * @throws SQLException
	 * @throws qdbException
	 */
	public long getSgpForumResId(Connection con, long sgp_id)throws SQLException, qdbException{
		String sql="select sgr_ent_id from studygrouprelation where sgr_sgp_id=? and sgr_type=?";
		PreparedStatement stmt = null;
	    stmt = con.prepareStatement(sql);
	    int index=1;
	    stmt.setLong(index++, sgp_id);
	    stmt.setString(index++, SGR_TYPE_SGP_DISCUSS);
	    long res_id=0;
	    ResultSet rs = stmt.executeQuery();
	    if(rs.next()){
	    	res_id=rs.getLong("sgr_ent_id");
	    }
	    if(stmt !=null)stmt.close();
		return res_id;

	}

	/**
	 * 学习小组是否存在(根据时间判断数据正确性,用于数据同步)
	 * @param con
	 * @param sgp_id
	 * @param upd_time
	 * @return
	 * @throws SQLException
	 * @throws qdbException
	 */
	public boolean isSgpIdExist(Connection con, long sgp_id, Timestamp upd_time)throws SQLException, qdbException{
		String sql = "select * from studygroup where sgp_id=? and sgp_upd_timestamp=?";
		PreparedStatement stmt = null;
	    stmt = con.prepareStatement(sql);
	    int index=1;
	    stmt.setLong(index++, sgp_id);
	    stmt.setTimestamp(index++, upd_time);
	    ResultSet rs = stmt.executeQuery();
	    boolean isExist=false;
	    if(rs.next()){
	    	isExist=true;
	    }
	    if(stmt !=null)stmt.close();
		return isExist;
	}

	/**
	 * 学习小组资源是否存在(根据时间判断数据正确性,用于数据同步)
	 * @param con
	 * @param sgs_id
	 * @param upd_time
	 * @return
	 * @throws SQLException
	 * @throws qdbException
	 */
	public boolean isSgsExist(Connection con, long sgs_id, Timestamp upd_time)throws SQLException, qdbException{
		String sql = "select * from studyGroupResources where sgs_id=? and sgs_upd_timestamp=?";
		PreparedStatement stmt = null;
	    stmt = con.prepareStatement(sql);
	    int index=1;
	    stmt.setLong(index++, sgs_id);
	    stmt.setTimestamp(index++, upd_time);
	    ResultSet rs = stmt.executeQuery();
	    boolean isExist=false;
	    if(rs.next()){
	    	isExist=true;
	    }
	    if(stmt !=null)stmt.close();
		return isExist;
	}

	/**
	 * 用户是否是学习小组资源的创建者
	 * @param con
	 * @param sgs_id
	 * @param usr_id
	 * @return
	 * @throws SQLException
	 * @throws qdbException
	 */
	public boolean isUsrSgpResCreator(Connection con, long sgs_id, String usr_id)throws SQLException, qdbException{
		String sql = "select * from studyGroupResources where sgs_id=? and sgs_create_usr_id=?";
		PreparedStatement stmt = null;
	    stmt = con.prepareStatement(sql);
	    int index=1;
	    stmt.setLong(index++, sgs_id);
	    stmt.setString(index++, usr_id);
	    ResultSet rs = stmt.executeQuery();
	    boolean isCreator=false;
	    if(rs.next()){
	    	isCreator=true;
	    }
	    if(stmt !=null)stmt.close();
		return isCreator;
	}

	/**
	 * 用户是否是学习小组的成员
	 * @param con
	 * @param sgp_id
	 * @param usr_ent_id
	 * @return
	 * @throws SQLException
	 * @throws qdbException
	 */
	public boolean isUsrSgpMem(Connection con, long sgp_id, long usr_ent_id)throws SQLException, qdbException{
		StringBuffer sqlBuf=new StringBuffer();
		sqlBuf.append("  SELECT sgm_sgp_id, usr_ent_id FROM studyGroupMember")
			.append("  INNER JOIN regUser ON (usr_ent_id = sgm_ent_id AND usr_status = 'OK')")
			.append("  WHERE sgm_type = 'USR' AND sgm_sgp_id = ? and usr_ent_id=? ")
			.append("  UNION")
			.append("  SELECT sgm_sgp_id, usr_ent_id FROM studyGroupMember")
			.append("  INNER JOIN aeItem ON (sgm_ent_id = itm_id)")
			.append("  INNER JOIN aeApplication ON (app_itm_id = itm_id AND app_status = '"+aeApplication.ADMITTED+"')")
			.append("  INNER JOIN regUser ON (usr_ent_id = app_ent_id AND usr_status = 'OK')")
			.append("  WHERE sgm_type = 'ITM' AND sgm_sgp_id =?  and usr_ent_id=?")
			.append("  UNION")
			.append("  SELECT sgm_sgp_id, usr_ent_id FROM studyGroupMember ")
			.append("  INNER JOIN EntityRelation ON (ern_ancestor_ent_id = sgm_ent_id AND ern_type = 'USR_PARENT_USG')")
			.append("  INNER JOIN regUser ON (usr_ent_id = ern_child_ent_id AND usr_status = 'OK')")
			.append("  WHERE sgm_type = 'USG' AND sgm_sgp_id =?  and usr_ent_id=?");

		PreparedStatement stmt = null;
	    stmt = con.prepareStatement(sqlBuf.toString());
	    int index=1;
	    stmt.setLong(index ++, sgp_id);
	    stmt.setLong(index ++, usr_ent_id);
	    stmt.setLong(index ++, sgp_id);
	    stmt.setLong(index ++, usr_ent_id);
	    stmt.setLong(index ++, sgp_id);
	    stmt.setLong(index ++, usr_ent_id);
	    ResultSet rs = stmt.executeQuery();
	    boolean isMem=false;
	    if(rs.next()){
	    	isMem=true;
	    }
	    if(stmt !=null)stmt.close();
		return isMem;
	}

	public String getMyStudyGroupSql(String conditionSql) {
		String sql = "SELECT sgp_id, sgp_title, sgp_tcr_id, sgp_upd_timestamp, sgp_create_timestamp FROM studyGroup" +
				" INNER JOIN studyGroupMember ON (sgm_sgp_id = sgp_id AND sgm_type = 'USR')" +
				" WHERE sgm_status = 'ADMITTED' AND sgm_ent_id = ? AND (sgp_public_type = ? OR sgp_public_type = ?) ";
		if(conditionSql != null) {
			sql += conditionSql;
		}

		sql += " UNION " +
				" SELECT DISTINCT(sgp_id), sgp_title, sgp_tcr_id, sgp_upd_timestamp, sgp_create_timestamp FROM studyGroup" +
				" INNER JOIN studyGroupMember ON (sgm_sgp_id = sgp_id AND sgm_type = 'USG')" +
				" INNER JOIN EntityRelation ON (ern_ancestor_ent_id = sgm_ent_id AND ern_type = 'USR_PARENT_USG')" +
				" WHERE ern_child_ent_id = ? AND (sgp_public_type = ? OR sgp_public_type = ?)";
		if(conditionSql != null) {
			sql += conditionSql;
		}

		sql +=	" UNION " +
				" SELECT DISTINCT(sgp_id), sgp_title, sgp_tcr_id, sgp_upd_timestamp, sgp_create_timestamp FROM studyGroup" +
				" INNER JOIN studyGroupMember ON (sgm_sgp_id = sgp_id AND sgm_type = 'ITM')" +
				" INNER JOIN aeApplication ON (app_itm_id = sgm_ent_id AND app_status = '"+ aeApplication.ADMITTED +"')" +
				" INNER JOIN aeItem ON (itm_id = app_itm_id AND itm_status = 'ON')" +
				" WHERE app_ent_id = ? AND (sgp_public_type = ? OR sgp_public_type = ?)";
		if (conditionSql != null) {
			sql += conditionSql;
		}

		sql +=" UNION" +
				" SELECT sgp_id, sgp_title, sgp_tcr_id, sgp_upd_timestamp, sgp_create_timestamp FROM studyGroup" +
				" INNER JOIN studyGroupRelation ON (sgr_sgp_id = sgp_id AND sgr_type = ?)" +
				" WHERE sgr_ent_id = ? AND (sgp_public_type = ? OR sgp_public_type = ?)";
		if (conditionSql != null) {
			sql += conditionSql;
		}
		sql += " ORDER BY sgp_create_timestamp desc";

		return sql;
	}
	/**
	 * 首页我的学习小组
	 * @param con
	 * @param userEntId
	 * @param start
	 * @param pageSize
	 * @return
	 * @throws SQLException
	 */
	public Vector getMyStudyGroup(Connection con, long userEntId, int start, int pageSize) throws SQLException {
		Vector myStudyGroupVec = new Vector();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(getMyStudyGroupSql(null));
			int index = 1;
			stmt.setLong(index++, userEntId);
			stmt.setInt(index++, SGP_PUBLIC_TYPE_MEMBER_CAN_SEE);
			stmt.setInt(index++, SGP_PUBLIC_TYPE_All_CAN_SEE);
			stmt.setLong(index++, userEntId);
			stmt.setInt(index++, SGP_PUBLIC_TYPE_MEMBER_CAN_SEE);
			stmt.setInt(index++, SGP_PUBLIC_TYPE_All_CAN_SEE);
			stmt.setLong(index++, userEntId);
			stmt.setInt(index++, SGP_PUBLIC_TYPE_MEMBER_CAN_SEE);
			stmt.setInt(index++, SGP_PUBLIC_TYPE_All_CAN_SEE);
			stmt.setString(index++, SGR_TYPE_SGP_MANAGER);
			stmt.setLong(index++, userEntId);
			stmt.setInt(index++, SGP_PUBLIC_TYPE_MEMBER_CAN_SEE);
			stmt.setInt(index++, SGP_PUBLIC_TYPE_All_CAN_SEE);

			rs = stmt.executeQuery();
			int count = 0;
			while(rs.next()) {
				if (count >= start && count < (start + pageSize)) {
					StudyGroupBean studyGroupBean = new StudyGroupBean();
					studyGroupBean.setSgp_id(rs.getLong("sgp_id"));
					studyGroupBean.setSgp_title(rs.getString("sgp_title"));
					studyGroupBean.setSgp_tcr_id(rs.getLong("sgp_tcr_id"));
//					studyGroupBean.setSgp_type(rs.getString("sgr_type"));
					myStudyGroupVec.addElement(studyGroupBean);
				}
				count++;
			}

		} finally {
			if(stmt != null) {
				stmt.close();
			}
		}

		return myStudyGroupVec;
	}

	/**
	 * 判断培训管理员是否管理学习小组的培训中心
	 * @param con
	 * @param sgp_id
	 * @param prof
	 * @return
	 * @throws SQLException
	 */
	public boolean isTaMgtSgpTc(Connection con, long sgp_id ,loginProfile prof) throws SQLException{
		StudyGroupBean sgpBean=getStudyGroupDetail(con, sgp_id);
		AcTrainingCenter acTc= new AcTrainingCenter(con);
		boolean result=false;
		result=acTc.canMgtTc(prof.usr_ent_id,  prof.current_role, prof.root_ent_id, sgpBean.getTcr_id());
		return result;
	}

	/**
	 * 是否存在学习小组的标题
	 * @param con
	 * @param sgp_title
	 * @return
	 * @throws SQLException
	 */
	public boolean isSgpTitleExist(Connection con, String sgp_title ) throws SQLException{
		String sql="select * from studyGroup where sgp_title =?";
		PreparedStatement stmt = null;
	    stmt = con.prepareStatement(sql);
	    boolean exist=false;
	    int index=1;
	    stmt.setString(index ++, sgp_title);
	    ResultSet rs = stmt.executeQuery();
	    if(rs.next()){
	    	exist=true;
	    }
	    if(stmt !=null)stmt.close();
	    return exist;
	}

	public static String getSgpTitleByID(Connection con, long sgp_id) throws SQLException{
		String sgp_title = "";
		String sql="select sgp_title from studyGroup where sgp_id =?";
		PreparedStatement stmt = null;
	    stmt = con.prepareStatement(sql);
	    int index=1;
	    stmt.setLong(index ++, sgp_id);
	    ResultSet rs = stmt.executeQuery();
	    if(rs.next()){
	    	sgp_title=rs.getString("sgp_title");
	    }
	    cwSQL.cleanUp(rs, stmt);
	    return sgp_title;
	}

	public static boolean isSendEmail(Connection con, long sgp_id) throws SQLException{
		boolean sgp_send_email_ind = false;
		String sql="select sgp_send_email_ind from studyGroup where sgp_id =?";
		PreparedStatement stmt = null;
	    stmt = con.prepareStatement(sql);
	    int index=1;
	    stmt.setLong(index ++, sgp_id);
	    ResultSet rs = stmt.executeQuery();
	    if(rs.next()){
	    	sgp_send_email_ind=rs.getBoolean("sgp_send_email_ind");
	    }
	    cwSQL.cleanUp(rs, stmt);
	    return sgp_send_email_ind;
	}

	public Vector getSgmEntID(Connection con, StudyGroupMemDB sgpMem) throws SQLException{
		Vector sgmEntIDVec = new Vector();
		String sql="select sgm_ent_id from studyGroupMember where sgm_sgp_id = ? and sgm_type = ?";
		PreparedStatement stmt = null;
	    stmt = con.prepareStatement(sql);
	    int index=1;
	    stmt.setLong(index ++, sgpMem.sgm_sgp_id);
	    stmt.setString(index ++, sgpMem.sgm_type);
	    ResultSet rs = stmt.executeQuery();
	    while(rs.next()){
	    	sgmEntIDVec.add(String.valueOf(rs.getLong("sgm_ent_id")));
	    }
	    cwSQL.cleanUp(rs, stmt);
	    return sgmEntIDVec;
	}
}
