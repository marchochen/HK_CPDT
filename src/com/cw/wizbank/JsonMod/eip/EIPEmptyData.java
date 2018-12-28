package com.cw.wizbank.JsonMod.eip;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.cw.wizbank.JsonMod.know.Know;
import com.cw.wizbank.JsonMod.studyGroup.StudyGroup;
import com.cw.wizbank.JsonMod.tcrTemplate.TcrTemplateLogic;
import com.cw.wizbank.accesscontrol.AcUserGroup;
import com.cw.wizbank.ae.aeCatalog;
import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.batch.user.DeleteUser;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.content.EvalAccess;
import com.cw.wizbank.course.ModulePrerequisiteManagement;
import com.cw.wizbank.db.DbKnowledge;
import com.cw.wizbank.db.DbSnsGroup;
import com.cw.wizbank.db.DbTrainingCenter;
import com.cw.wizbank.db.view.ViewObjectiveAccess;
import com.cw.wizbank.db.view.ViewTrainingCenter;
import com.cw.wizbank.know.dao.KnowCatalogDAO;
import com.cw.wizbank.qdb.dbCourse;
import com.cw.wizbank.qdb.dbModule;
import com.cw.wizbank.qdb.dbObjective;
import com.cw.wizbank.qdb.dbUserGroup;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cwn.wizbank.utils.CommonLog;

/**
 *  清空企业以前所建的数据
 */
public class EIPEmptyData {
	/**
     *  将list转为字符串
     */
    public String longList2String(List<Long> list){
    	String str = "(0,";
    	for(long id : list){
    		str += id + ",";
    	}
    	return str.substring(0, str.length() - 1) + ")";
    }
    
    /**
     *  获取该培训中心及其所有子培训中心id的集合
     *  @param tcr_id 培训中心id
     */
    public List<Long> getThisAndChildTcrIdList(Connection con, long tcr_id) throws SQLException {
    	String sql = " select tcn_child_tcr_id from tcRelation where tcn_ancestor = ? order by tcn_child_tcr_id desc ";
    	PreparedStatement stmt = null;
    	ResultSet rs = null;
    	List<Long> tcr_list = new ArrayList<Long>();
    	try{
    		stmt = con.prepareStatement(sql);
    		stmt.setLong(1, tcr_id);
    		rs = stmt.executeQuery();
    		while(rs.next()){
    			tcr_list.add(rs.getLong("tcn_child_tcr_id"));
    		}
    	} finally {
    		cwSQL.cleanUp(rs, stmt);
    	}
    	tcr_list.add(tcr_id);
    	return tcr_list;
    }
    
    /**
     *  获取培训中心id集合下的用户id集合
     *  @param tcr_id_list 培训中心id集合
     */
    public List<Long> getUsrIdListByTcrId(Connection con, List<Long> tcr_id_list) throws SQLException {
    	String sql = " select distinct u_id from V_usrTcRelation where tcr_id in " + longList2String(tcr_id_list)
    				+ " union select distinct erh_child_ent_id from EntityRelationHistory "
    				+ "	left join tcTrainingCenterTargetEntity on tce_ent_id = erh_ancestor_ent_id "
    				+ " inner join RegUser on usr_ent_id = erh_child_ent_id "
    				+ " where erh_type = 'USR_PARENT_USG' and tce_tcr_id in " + longList2String(tcr_id_list);
    	PreparedStatement stmt = null;
    	ResultSet rs = null;
    	List<Long> usr_id_list = new ArrayList<Long>();
    	try{
    		stmt = con.prepareStatement(sql);
    		rs = stmt.executeQuery();
    		while(rs.next()){
    			usr_id_list.add(rs.getLong("u_id"));
    		}
    	} finally {
    		cwSQL.cleanUp(rs, stmt);
    	}
    	return usr_id_list;
    }
    
    /**
     *  获取培训中心id集合下的用户组id集合
     *  @param tcr_id_list 培训中心id集合
     */
    public List<dbUserGroup> getUsgIdListByTcrId(Connection con, List<Long> tcr_id_list) throws SQLException {
    	String sql = "  select distinct ent_id, ent_upd_date from tcTrainingCenterTargetEntity "
    				+ " left join EntityRelation on ern_ancestor_ent_id = tce_ent_id and ern_type = 'USG_PARENT_USG' "
    				+ " left join Entity on ent_id = ern_child_ent_id or ent_id = tce_ent_id "
    				+ " where tce_tcr_id in " + longList2String(tcr_id_list) + " order by ent_id desc ";
    	PreparedStatement stmt = null;
    	ResultSet rs = null;
    	List<dbUserGroup> usg_list = new ArrayList<dbUserGroup>();
    	dbUserGroup usg = new dbUserGroup();
    	try{
    		stmt = con.prepareStatement(sql);
    		rs = stmt.executeQuery();
    		while(rs.next()){
    			usg = new dbUserGroup();
    			usg.usg_ent_id = rs.getLong("ent_id");
                usg.ent_id = rs.getLong("ent_id");
                usg.ent_upd_date = rs.getTimestamp("ent_upd_date");
    			usg_list.add(usg);
    		}
    	} finally {
    		cwSQL.cleanUp(rs, stmt);
    	}
    	return usg_list;
    }
    
    /**
     *  获取培训中心id集合下的课程id集合
     *  @param tcr_id_list 培训中心id集合
     */
    public List<Long> getItmIdListByTcrId(Connection con, List<Long> tcr_id_list) throws SQLException {
    	String sql = " select itm_id from aeItem where itm_tcr_id in " + longList2String(tcr_id_list) + " order by itm_id desc ";
    	PreparedStatement stmt = null;
    	ResultSet rs = null;
    	List<Long> itm_id_list = new ArrayList<Long>();
    	try{
    		stmt = con.prepareStatement(sql);
    		rs = stmt.executeQuery();
    		while(rs.next()){
    			itm_id_list.add(rs.getLong("itm_id"));
    		}
    	} finally {
    		cwSQL.cleanUp(rs, stmt);
    	}
    	return itm_id_list;
    }
    
    /**
     *  获取培训中心id集合下的培训目录id集合
     *  @param tcr_id_list 培训中心id集合
     */
    public List<Long> getCatIdListByTcrId(Connection con, List<Long> tcr_id_list) throws SQLException {
    	String sql = " select cat_id from aeCatalog where cat_tcr_id in " + longList2String(tcr_id_list) + " order by cat_id desc ";
    	PreparedStatement stmt = null;
    	ResultSet rs = null;
    	List<Long> cat_id_list = new ArrayList<Long>();
    	try{
    		stmt = con.prepareStatement(sql);
    		rs = stmt.executeQuery();
    		while(rs.next()){
    			cat_id_list.add(rs.getLong("cat_id"));
    		}
    	} finally {
    		cwSQL.cleanUp(rs, stmt);
    	}
    	return cat_id_list;
    }
    
    /**
     *  获取培训中心id集合下的问题目录id集合
     *  @param tcr_id_list 培训中心id集合
     */
    public List<Long> getKcaIdListByTcrId(Connection con, List<Long> tcr_id_list) throws SQLException {
    	String sql = " select kca_id from knowCatalog where kca_tcr_id in " + longList2String(tcr_id_list) + " order by kca_id desc ";
    	PreparedStatement stmt = null;
    	ResultSet rs = null;
    	List<Long> kca_id_list = new ArrayList<Long>();
    	try{
    		stmt = con.prepareStatement(sql);
    		rs = stmt.executeQuery();
    		while(rs.next()){
    			kca_id_list.add(rs.getLong("kca_id"));
    		}
    	} finally {
    		cwSQL.cleanUp(rs, stmt);
    	}
    	return kca_id_list;
    }
    
    /**
     *  获取问题目录id集合下的问题id集合
     *  @param tcr_id_list 培训中心id集合
     */
    public List<Long> getQueIdListByKcaId(Connection con, List<Long> kca_id_list) throws SQLException {
    	String sql = " select kcr_child_kca_id from knowCatalogRelation where kcr_type = 'QUE_PARENT_KCA' and kcr_ancestor_kca_id in " + longList2String(kca_id_list);
    	PreparedStatement stmt = null;
    	ResultSet rs = null;
    	List<Long> que_id_list = new ArrayList<Long>();
    	try{
    		stmt = con.prepareStatement(sql);
    		rs = stmt.executeQuery();
    		while(rs.next()){
    			que_id_list.add(rs.getLong("kcr_child_kca_id"));
    		}
    	} finally {
    		cwSQL.cleanUp(rs, stmt);
    	}
    	return que_id_list;
    }
    
    /**
     *  获取培训中心id集合下的知识目录id集合
     *  @param tcr_id_list 培训中心id集合
     */
    public List<Long> getKbcIdListByTcrId(Connection con, List<Long> tcr_id_list) throws SQLException {
    	String sql = " select kbc_id from kb_catalog where kbc_tcr_id in " + longList2String(tcr_id_list) + " order by kbc_id desc ";
    	PreparedStatement stmt = null;
    	ResultSet rs = null;
    	List<Long> kbc_id_list = new ArrayList<Long>();
    	try{
    		stmt = con.prepareStatement(sql);
    		rs = stmt.executeQuery();
    		while(rs.next()){
    			kbc_id_list.add(rs.getLong("kbc_id"));
    		}
    	} finally {
    		cwSQL.cleanUp(rs, stmt);
    	}
    	return kbc_id_list;
    }
    
    /**
     *  获取培训中心id集合下的标签id集合
     *  @param tcr_id_list 培训中心id集合
     */
    public List<Long> getTagIdListByTcrId(Connection con, List<Long> tcr_id_list) throws SQLException {
    	String sql = " select tag_id from Tag where tag_tcr_id in " + longList2String(tcr_id_list) + " order by tag_id desc ";
    	PreparedStatement stmt = null;
    	ResultSet rs = null;
    	List<Long> tag_id_list = new ArrayList<Long>();
    	try{
    		stmt = con.prepareStatement(sql);
    		rs = stmt.executeQuery();
    		while(rs.next()){
    			tag_id_list.add(rs.getLong("tag_id"));
    		}
    	} finally {
    		cwSQL.cleanUp(rs, stmt);
    	}
    	return tag_id_list;
    }
    
    /**
     *  获取培训中心id集合下的模块id集合
     *  @param tcr_id_list 培训中心id集合
     */
    public List<Long> getModIdListByTcrId(Connection con, List<Long> tcr_id_list) throws SQLException {
    	String sql = " select mtc_mod_id from ModuleTrainingCenter where mtc_tcr_id in " + longList2String(tcr_id_list);
    	PreparedStatement stmt = null;
    	ResultSet rs = null;
    	List<Long> mod_id_list = new ArrayList<Long>();
    	try{
    		stmt = con.prepareStatement(sql);
    		rs = stmt.executeQuery();
    		while(rs.next()){
    			mod_id_list.add(rs.getLong("mtc_mod_id"));
    		}
    	} finally {
    		cwSQL.cleanUp(rs, stmt);
    	}
    	return mod_id_list;
    }
    
    /**
     *  删除培训中心id集合下的公告
     *  @param tcr_id_list 培训中心id集合
     */
    public void delMessageByTcrId(Connection con, List<Long> tcr_id_list) throws SQLException {
    	String sql = " delete from Message where msg_tcr_id in " + longList2String(tcr_id_list);
    	PreparedStatement stmt = null;
    	try{
    		stmt = con.prepareStatement(sql);
    		stmt.executeUpdate();
    	} finally {
    		cwSQL.cleanUp(null, stmt);
    	}
    }
    
    /**
     *  删除培训中心id集合下的所有号外资讯
     *  @param tcr_id_list 培训中心id集合
     */
    public void delArtByTcrId(Connection con, List<Long> tcr_id_list) throws SQLException {
    	String sql = " delete from article where art_tcr_id in " + longList2String(tcr_id_list);
    	PreparedStatement stmt = null;
    	try{
    		stmt = con.prepareStatement(sql);
    		stmt.executeUpdate();
    	} finally {
    		cwSQL.cleanUp(null, stmt);
    	}
    }
    
    /**
     *  删除培训中心id集合下的所有号外资讯类型
     *  @param tcr_id_list 培训中心id集合
     */
    public void delArtTypeByTcrId(Connection con, List<Long> tcr_id_list) throws SQLException {
    	String sql = " delete from articleType where aty_tcr_id in " + longList2String(tcr_id_list);
    	PreparedStatement stmt = null;
    	try{
    		stmt = con.prepareStatement(sql);
    		stmt.executeUpdate();
    	} finally {
    		cwSQL.cleanUp(null, stmt);
    	}
    }
    
    /**
     *  删除培训中心id集合下的所有投票
     *  @param tcr_id_list 培训中心id集合
     */
    public void delVotByTcrId(Connection con, List<Long> tcr_id_list) throws SQLException {
    	String sql = " update voting set vot_status = 'DEL' where vot_tcr_id in " + longList2String(tcr_id_list);
    	PreparedStatement stmt = null;
    	try{
    		stmt = con.prepareStatement(sql);
    		stmt.executeUpdate();
    	} finally {
    		cwSQL.cleanUp(null, stmt);
    	}
    }
    
    /**
     *  删除培训中心id集合下的投票
     *  @param tcr_id_list 培训中心id集合
     */
    public void delVqtByVqtId(Connection con, long vtq_id) throws SQLException {
    	String sql = "update votequestion set vtq_status = 'DEL' where vtq_id = ?";
    	PreparedStatement stmt = null;
    	try{
    		stmt = con.prepareStatement(sql);
    		stmt.setLong(1, vtq_id);
    		stmt.executeUpdate();
    	} finally {
    		cwSQL.cleanUp(null, stmt);
    	}
    }
    
    /**
     *  删除培训中心id下的直播
     *  @param lv_id 直播id
     */
    public void delLiveByLiveId(Connection con, long lv_id) throws SQLException {
    	String sql = " delete from liveItem where lv_id = ?";
    	PreparedStatement stmt = null;
    	try{
    		stmt = con.prepareStatement(sql);
    		stmt.setLong(1, lv_id);
    		stmt.executeUpdate();
    	} finally {
    		cwSQL.cleanUp(null, stmt);
    	}
    }
    
    /**
     *  删除直播id下的所有观看记录
     *  @param lv_id 直播id
     */
    public void delLiveRecordsByLiveId(Connection con, long lv_id) throws SQLException {
    	String sql = " delete from liveRecords where lr_live_id = ? ";
    	PreparedStatement stmt = null;
    	try{
    		stmt = con.prepareStatement(sql);
    		stmt.setLong(1, lv_id);
    		stmt.executeUpdate();
    	} finally {
    		cwSQL.cleanUp(null, stmt);
    	}
    }    
    
    /**
     *  获取培训中心id集合下的投票id集合
     *  @param tcr_id_list 培训中心id集合
     */
    public List<Long> getVtqIdListByTcrId(Connection con, List<Long> tcr_id_list) throws SQLException {
    	String sql = " select vtq_id from voting,votequestion where vtq_vot_id = vot_id and vot_status != 'DEL' and vot_tcr_id in " + longList2String(tcr_id_list);
    	PreparedStatement stmt = null;
    	ResultSet rs = null;
    	List<Long> vtq_id_list = new ArrayList<Long>();
    	try{
    		stmt = con.prepareStatement(sql);
    		rs = stmt.executeQuery();
    		while(rs.next()){
    			vtq_id_list.add(rs.getLong("vtq_id"));
    		}
    	} finally {
    		cwSQL.cleanUp(rs, stmt);
    	}
    	return vtq_id_list;
    }
    
    /**
     *  获取培训中心id集合下的群组id集合
     *  @param tcr_id_list 培训中心id集合
     */
    public List<Long> getGrpIdListByTcrId(Connection con, List<Long> tcr_id_list) throws SQLException {
    	String sql = " select s_grp_id from sns_group where s_grp_tcr_id in " + longList2String(tcr_id_list);
    	PreparedStatement stmt = null;
    	ResultSet rs = null;
    	List<Long> grp_id_list = new ArrayList<Long>();
    	try{
    		stmt = con.prepareStatement(sql);
    		rs = stmt.executeQuery();
    		while(rs.next()){
    			grp_id_list.add(rs.getLong("s_grp_id"));
    		}
    	} finally {
    		cwSQL.cleanUp(rs, stmt);
    	}
    	return grp_id_list;
    }
    
    /**
     *  获取培训中心id集合下的直播id集合
     *  @param tcr_id_list 培训中心id集合
     */
    public List<Long> getLiveIdListByTcrId(Connection con, List<Long> tcr_id_list) throws SQLException {
    	String sql = " SELECT lv_id FROM liveItem WHERE lv_tcr_id in " + longList2String(tcr_id_list);
    	PreparedStatement stmt = null;
    	ResultSet rs = null;
    	List<Long> live_id_list = new ArrayList<Long>();
    	try{
    		stmt = con.prepareStatement(sql);
    		rs = stmt.executeQuery();
    		while(rs.next()){
    			live_id_list.add(rs.getLong("lv_id"));
    		}
    	} finally {
    		cwSQL.cleanUp(rs, stmt);
    	}
    	return live_id_list;
    }
    
    /**
     *  删除培训中心id集合下的所有证书
     *  @param tcr_id_list 培训中心id集合
     */
    public void delCertificateByTcrId(Connection con, List<Long> tcr_id_list) throws SQLException {
    	String sql = " delete from certificate where cfc_tcr_id in " + longList2String(tcr_id_list);
    	PreparedStatement stmt = null;
    	try{
    		stmt = con.prepareStatement(sql);
    		stmt.executeUpdate();
    	} finally {
    		cwSQL.cleanUp(null, stmt);
    	}
    }
    
    /**
     *  删除培训中心id集合下的所有培训中心计划
     *  @param tcr_id_list 培训中心id集合
     */
    public void delTrainingPlanByTcrId(Connection con, List<Long> tcr_id_list) throws SQLException {
    	String sql = " delete from tpTrainingPlan where tpn_tcr_id in " + longList2String(tcr_id_list);
    	PreparedStatement stmt = null;
    	try{
    		stmt = con.prepareStatement(sql);
    		stmt.executeUpdate();
    	} finally {
    		cwSQL.cleanUp(null, stmt);
    	}
    }
    
    /**
     *  删除培训中心id集合下的所有年计划
     *  @param tcr_id_list 培训中心id集合
     */
    public void delYearPlanByTcrId(Connection con, List<Long> tcr_id_list) throws SQLException {
    	String sql = " delete from tpYearPlan where ypn_tcr_id in " + longList2String(tcr_id_list);
    	PreparedStatement stmt = null;
    	try{
    		stmt = con.prepareStatement(sql);
    		stmt.executeUpdate();
    	} finally {
    		cwSQL.cleanUp(null, stmt);
    	}
    }
    
    /**
     *  删除培训中心下的所有objective
     *  @param tcr_id_list 培训中心id集合
     */
    public void delObjectiveByTcrId(Connection con, List<Long> tcr_id_list) throws SQLException {
    	
    	String sql_rob = " delete from resourceObjective where rob_obj_id in ( select obj_id from Objective where obj_tcr_id in " + longList2String(tcr_id_list) +")";
    	PreparedStatement stmt = null;
    	try{
    		stmt = con.prepareStatement(sql_rob);
    		stmt.executeUpdate();
    	} finally {
    		cwSQL.cleanUp(null, stmt);
    	}
    	
    	String sql_obj = " delete from Objective where obj_tcr_id in " + longList2String(tcr_id_list);
    	stmt = null;
    	try{
    		stmt = con.prepareStatement(sql_obj);
    		stmt.executeUpdate();
    	} finally {
    		cwSQL.cleanUp(null, stmt);
    	}
    }
    
    /**
     *  获取培训中心id集合下的资源目录id集合
     *  @param tcr_id_list 培训中心id集合
     */
    public List<Long> getObjIdListByTcrId(Connection con, List<Long> tcr_id_list) throws SQLException {
    	String likeInfo = "'% '+cast(p_obj.obj_id as varchar(255))+' %'";
    	if(cwSQL.DBVENDOR_ORACLE.equalsIgnoreCase(cwSQL.getDbType())){
    		likeInfo = "'% '||p_obj.obj_id||' %'";
    	}else if(cwSQL.DBVENDOR_MYSQL.equalsIgnoreCase(cwSQL.getDbType())){
    		likeInfo = "concat(concat('% ',p_obj.obj_id),' %')";
    	}
    	String sql = " select obj_id from Objective where obj_tcr_id in " + longList2String(tcr_id_list) 
    				+ " union select c_obj.obj_id from Objective c_obj "
    				+ " inner join Objective p_obj on c_obj.obj_ancester like " + likeInfo
    				+ " where p_obj.obj_tcr_id in " + longList2String(tcr_id_list)
    				+ " order by obj_id desc ";
    	PreparedStatement stmt = null;
    	ResultSet rs = null;
    	List<Long> obj_id_list = new ArrayList<Long>();
    	try{
    		stmt = con.prepareStatement(sql);
    		rs = stmt.executeQuery();
    		while(rs.next()){
    			obj_id_list.add(rs.getLong("obj_id"));
    		}
    	} finally {
    		cwSQL.cleanUp(rs, stmt);
    	}
    	return obj_id_list;
    }
    
    /**
     *  获取资源目录id集合下的资源id集合
     *  @param obj_id_list 资源目录id集合
     */
    public List<Long> getResIdListByObjId(Connection con, List<Long> obj_id_list) throws SQLException {
    	String sql = " select rob_res_id from resourceObjective inner join resources on res_id = rob_res_id "
    			+ " where res_res_id_root is null and rob_obj_id in " + longList2String(obj_id_list) + " order by rob_res_id desc ";
    	PreparedStatement stmt = null;
    	ResultSet rs = null;
    	List<Long> res_id_list = new ArrayList<Long>();
    	try{
    		stmt = con.prepareStatement(sql);
    		rs = stmt.executeQuery();
    		while(rs.next()){
    			res_id_list.add(rs.getLong("rob_res_id"));
    		}
    	} finally {
    		cwSQL.cleanUp(rs, stmt);
    	}
    	return res_id_list;
    }
    
    /**
     *  删除课程id集合下的所有课程和项目式培训的关联
     *  @param itm_id_list 课程id集合
     */
    public void delIntegCompleteConditionByTcrId(Connection con, List<Long> itm_id_list) throws SQLException {
    	String sql = " delete from IntegCompleteCondition where icd_icc_id in (select icc_id FROM IntegCourseCriteria "
    				+ " where icc_itm_id in " + longList2String(itm_id_list) + ") or icd_id in (select iri_icd_id from IntegRelationItem"
    				+ " where iri_relative_itm_id in " + longList2String(itm_id_list) + ") ";
    	PreparedStatement stmt = null;
    	try{
    		stmt = con.prepareStatement(sql);
    		stmt.executeUpdate();
    	} finally {
    		cwSQL.cleanUp(null, stmt);
    	}
    }
    
    /**
     *  删除课程id集合下的所有培训项目式培训课程
     *  @param itm_id_list 课程id集合
     */
    public void delIntegRelationItem(Connection con, List<Long> itm_id_list) throws SQLException {
    	String sql = " delete from IntegRelationItem where iri_relative_itm_id in " + longList2String(itm_id_list) + " or iri_icd_id in ("
    				+ " select icd_id from IntegCompleteCondition left join IntegCourseCriteria on icc_id = icd_icc_id where icc_itm_id in " + longList2String(itm_id_list) +") ";
    	PreparedStatement stmt = null;
    	try{
    		stmt = con.prepareStatement(sql);
    		stmt.executeUpdate();
    	} finally {
    		cwSQL.cleanUp(null, stmt);
    	}
    }
    
    /**
     *  删除培训中心id集合下的所有学习小组
     *  @param tcr_id_list 培训中心id集合
     */
    public List<Long> getSgpIdTypeByTcrId(Connection con, List<Long> tcr_id_list) throws SQLException {
    	String sql = " select sgp_id from studyGroup where sgp_tcr_id in " + longList2String(tcr_id_list);
    	PreparedStatement stmt = null;
    	ResultSet rs = null;
    	List<Long> sgp_id_list = new ArrayList<Long>();
    	try{
    		stmt = con.prepareStatement(sql);
    		rs = stmt.executeQuery();
    		while(rs.next()){
    			sgp_id_list.add(rs.getLong("sgp_id"));
    		}
    	} finally {
    		if(stmt != null){
    			stmt.close();
    		}
    		if(rs != null){
    			rs.close();
    		}
    	}
    	return sgp_id_list;
    }
    
    /**
     *  删除培训中心id集合下的所有职务
     *  @param tcr_id_list 培训中心id集合
     */
    public void delUserGradeByTcrId(Connection con, List<Long> tcr_id_list) throws SQLException {
    	String sql = " delete from UserGrade where ugr_tcr_id in " + longList2String(tcr_id_list);
    	PreparedStatement stmt = null;
    	try{
    		stmt = con.prepareStatement(sql);
    		stmt.executeUpdate();
    	} finally {
    		cwSQL.cleanUp(null, stmt);
    	}
    }
    
    /**
     *  删除培训中心id集合下的所有职位
     *  @param tcr_id_list 培训中心id集合
     */
    public void delUserPositionByTcrId(Connection con, List<Long> tcr_id_list) throws SQLException {
    	String sql = " delete from UserPosition where upt_tcr_id in " + longList2String(tcr_id_list);
    	PreparedStatement stmt = null;
    	try{
    		stmt = con.prepareStatement(sql);
    		stmt.executeUpdate();
    	} finally {
    		cwSQL.cleanUp(null, stmt);
    	}
    }
    
    /**
     * 清空培训中心的相关信息
     */
    public void emptyTcrData(Connection con, WizbiniLoader wizbini, loginProfile prof, long tcr_id) throws SQLException, qdbException, cwSysMessage, cwException, qdbErrMessage {
    	List<Long> tcr_id_list = getThisAndChildTcrIdList(con, tcr_id);

		CommonLog.info("Delete User/Group Start..."+(new Date(System.currentTimeMillis())).toString());
    	//删除所有属于该培训中心及其子培训中心的用户
		List<Long> usr_id_list = getUsrIdListByTcrId(con, tcr_id_list);
		long list_size = usr_id_list.size();
		for(int i=0;i<list_size;i++){
			DeleteUser.deleteUser(con, usr_id_list.get(i));
		}
		
		//删除所有属于该培训中心及其子培训中心的用户组
		List<dbUserGroup> usg_list = getUsgIdListByTcrId(con, tcr_id_list);
		AcUserGroup ug = new AcUserGroup(con);
		for(dbUserGroup usg : usg_list){
			if(!ug.canManageGroup(prof, usg.usg_ent_id, wizbini.cfgTcEnabled)) {
				throw new qdbErrMessage("USG002");
			}
            usg.delGroup(con, true, true, prof.usr_id);
		}
		con.commit();
		CommonLog.info("Delete user/group success."+(new Date(System.currentTimeMillis())).toString());
		//删除所有属于该培训中心及其子培训中心的课程
		List<Long> itm_id_list = getItmIdListByTcrId(con, tcr_id_list);
		delIntegRelationItem(con, itm_id_list);
		delIntegCompleteConditionByTcrId(con, itm_id_list);
		aeItem itm = new aeItem();
		dbCourse cos = new dbCourse();
		ModulePrerequisiteManagement itm_pre_mod = new ModulePrerequisiteManagement();
		for(long itm_id : itm_id_list){
			itm_pre_mod.delCosModPrerequisite(con, itm_id);
			itm.itm_id = itm_id;
			itm.is_complete_del = true;
			cos = new dbCourse();
            cos.cos_itm_id = itm.itm_id;
			itm.delWZBCourse(con, prof, cos);
			dbUtils.delDir(wizbini.getFileUploadItmDirAbs() + dbUtils.SLASH + Long.toString(itm_id));
		}
		con.commit();
		CommonLog.info("Delete item success."+(new Date(System.currentTimeMillis())).toString());
		//删除所有属于该培训中心及其子培训中心的培训目录
		List<Long> cat_id_list = getCatIdListByTcrId(con, tcr_id_list);
		aeCatalog cat = new aeCatalog();
		for(long cat_id : cat_id_list){
			cat.cat_id = cat_id;
			cat.delCatalogAllInfo(con);
		}
		
		//删除所有属于该培训中心及其子培训中心的问题目录及目录下问题
		List<Long> kca_id_list = getKcaIdListByTcrId(con, tcr_id_list);
		List<Long> que_id_list = getQueIdListByKcaId(con, kca_id_list);
		for(long que_id : que_id_list){
			Know.delKownQuestion(con, que_id);
		}
		for(long kca_id : kca_id_list){
			KnowCatalogDAO.del(con, kca_id);
		}
		
		//删除所有属于该培训中心及其子培训中心的知识目录及目录下知识
		List<Long> kbc_id_list = getKbcIdListByTcrId(con, tcr_id_list);
		List<Long> tag_id_list = getTagIdListByTcrId(con, tcr_id_list);
		for(long kbc_id : kbc_id_list){
			DbKnowledge.delKbItemAttachment(con, kbc_id);
			DbKnowledge.delKbItemCat(con, kbc_id);
			DbKnowledge.delKbCatalog(con, kbc_id);
			
			List<Long> kbi_id_list = DbKnowledge.getKbiIdByKbcId(con, kbc_id);
			List<Long> kba_id_list = DbKnowledge.getKbaIdByKbcId(con, kbc_id);
			for(long kba_id : kba_id_list){
				DbKnowledge.delKbAttachment(con, kba_id);
			}
			for(long kbi_id : kbi_id_list){
				DbKnowledge.delKbItem(con, kbi_id);
			}
		}
		for(long tag_id : tag_id_list){
			DbKnowledge.delKbItemTag(con, tag_id);
			DbKnowledge.delTag(con, tag_id);
		}
		
		//删除所有属于该培训中心及其子培训中心的问卷
		List<Long> mod_id_list = getModIdListByTcrId(con, tcr_id_list);
		EvalAccess evaAcc = new EvalAccess();
		dbModule dbMod = new dbModule();
		for(long mod_id : mod_id_list){
			dbMod.mod_res_id = mod_id;
	        dbMod.get(con);
			dbMod.del(con);
			evaAcc.eac_res_id = dbMod.res_id;
			evaAcc.delEvalAccessByRes_ID(con);
		}
		
		//删除所有属于该培训中心及其子培训中心的公告
		delMessageByTcrId(con, tcr_id_list);
		
		//删除所有属于该培训中心及其子培训中心的投票
		List<Long> vqt_id_list = getVtqIdListByTcrId(con, tcr_id_list);
		delVotByTcrId(con, tcr_id_list);
		for(long vqt_id : vqt_id_list){
			delVqtByVqtId(con, vqt_id);
		}		
		
		//删除所有属于该培训中心及其子培训中心的直播
		List<Long> live_id_list = getLiveIdListByTcrId(con, tcr_id_list);
		for(long lv_id : live_id_list){
			delLiveByLiveId(con, lv_id);
			delLiveRecordsByLiveId(con, lv_id);
		}
		
		//删除所有属于该培训中心及其子培训中心的号外资讯和号外资讯类型
		delArtByTcrId(con, tcr_id_list);
		delArtTypeByTcrId(con, tcr_id_list);
		
		//删除所有属于该培训中心及其子培训中心的群组
		List<Long> grp_id_list = getGrpIdListByTcrId(con, tcr_id_list);
		DbSnsGroup dbSnsGroup = new DbSnsGroup();
		for(long grp_id : grp_id_list){
			dbSnsGroup.completeDelSnsGroup(con, grp_id);
		}
		
		//删除所有属于该培训中心及其子培训中心的证书
		delCertificateByTcrId(con, tcr_id_list);
		
		//删除所有属于该培训中心及其子培训中心的计划
		delTrainingPlanByTcrId(con, tcr_id_list);
		delYearPlanByTcrId(con, tcr_id_list);
		con.commit();
		CommonLog.info("Delete delYearPlanByTcrId success."+(new Date(System.currentTimeMillis())).toString());
		
		//删除所有属于该培训中心及其子培训中心的资源目录和资源
		List<Long> obj_id_list = getObjIdListByTcrId(con, tcr_id_list);
		List<Long> res_id_list = getResIdListByObjId(con, obj_id_list);
		for(long res_id : res_id_list){
			dbObjective.delResources(con, wizbini, res_id);
		}
		for(long obj_id : obj_id_list){
			//还残留着copy资源的目录不直接删除，只去掉与培训中心的关联
			if(dbObjective.hasCopyRes(con, obj_id)){
				dbObjective.disassociatedObjAndTcr(con, obj_id);
			} else {
				ViewObjectiveAccess voa = new ViewObjectiveAccess();
				voa.delByObjId(con, obj_id);
				dbObjective.deleteObjByParentID(con,obj_id);
				dbObjective.deleteObj(con, obj_id, false);
			}
		}
		
		//删除所有属于该培训中心及其子培训中心的Objective
		delObjectiveByTcrId(con, tcr_id_list);
		
		//删除所有属于该培训中心及其子培训中心的学习小组
		List<Long> sgp_id_list = getSgpIdTypeByTcrId(con, tcr_id_list);
		StudyGroup sgp= new StudyGroup();
		for(long sgp_id : sgp_id_list){
			sgp.delSgp(con, prof, sgp_id);
		}
		
		//删除所有属于该培训中心及其子培训中心的职务和职位
		delUserGradeByTcrId(con, tcr_id_list);
		delUserPositionByTcrId(con, tcr_id_list);
		
		//删除属于其子培训中心
		DbTrainingCenter dbTc = new DbTrainingCenter();
		TcrTemplateLogic tcrTemplateLogic = TcrTemplateLogic.getInstance();
		for(int i=0;i<tcr_id_list.size()-1;i++){
			dbTc.setTcr_id(tcr_id_list.get(i));
			
			if (DbTrainingCenter.chkHasChildTc(con, dbTc.getTcr_id())) {
				throw new cwSysMessage("TC005");
			}
			if (ViewTrainingCenter.hasRelateItem(con, dbTc.getTcr_id()) || ViewTrainingCenter.hasRelateCatalog(con, dbTc.getTcr_id()) || ViewTrainingCenter.hasRelateObjective(con, dbTc.getTcr_id())
					|| ViewTrainingCenter.hasRelateMessage(con, dbTc.getTcr_id()) || ViewTrainingCenter.hasRelateEvaluation(con, dbTc.getTcr_id()) || ViewTrainingCenter.hasRelateStudyGroup(con, dbTc.getTcr_id())) {
				throw new cwSysMessage("TC006");
			}
			
			// delete all template and tcrTemplateModule of this tcr
			tcrTemplateLogic.delete_tcr_template(con, dbTc.getTcr_id(), "TC006");

			// del relate data of officers and target_group.
			dbTc.delRelateEntiry(con);
			dbTc.delRelateOfficer(con);
			// del tcRelation
			dbTc.delTcRelation(con);
			dbTc.delete(con, DbTrainingCenter.DEL);
		}
    }
}
