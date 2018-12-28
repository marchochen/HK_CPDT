package com.cw.wizbank.dataMigrate.imp;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.cw.wizbank.Application;
import com.cw.wizbank.accesscontrol.AcObjective;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.ae.aeApplication;
import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.ae.aeItemAccess;
import com.cw.wizbank.ae.db.DbAppnApprovalList;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.db.DbAcRole;
import com.cw.wizbank.db.DbSuperviseTargetEntity;
import com.cw.wizbank.db.DbTable;
import com.cw.wizbank.db.DbUserGrade;
import com.cw.wizbank.db.DbUserPosition;
import com.cw.wizbank.db.view.ViewTrainingCenter;
import com.cw.wizbank.qdb.dbEntityRelation;
import com.cw.wizbank.qdb.dbEntityRelationHistory;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.dbUserGroup;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.dataMigrate.imp.bean.UserBean;
import com.cw.wizbank.upload.ImportTemplate;
import com.cw.wizbank.util.EntityFullPath;
import com.cw.wizbank.util.LangLabel;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.dataMigrate.imp.bean.ImportObject;
import com.cwn.wizbank.entity.ObjectActionLog;
import com.cwn.wizbank.systemLog.SystemLogContext;
import com.cwn.wizbank.systemLog.SystemLogTypeEnum;
import com.cwn.wizbank.utils.CommonLog;
import com.cwn.wizbank.utils.LabelContent;

public class ImpUser extends Imp {

	public static final String USG_LEVEL_1_CODE = "HKJC";
	public static final String USG_LEVEL_1_TITLE = "HKJC";
	public static final String UGR_LEVEL_1_CODE_PARTTIME = "PARTTIME";
	public static final String UGR_LEVEL_1_TITLE_PARTTIME = "Part Time";
	public static final String UGR_LEVEL_1_CODE_FULLTIME = "FULLTIME";
	public static final String UGR_LEVEL_1_TITLE_FULLTIME = "Full Time";
	public static String log_properties;
	public static int usg_level_total = 1;
	public static int ugr_level_total = 1;

	public static String input_file_name = "lms_user_intranet.txt";
	
	public static String sender_usr_id_administrator = "s1u3";//Sender from Administrator
	public static String sender_usr_id_system = "s1u4";//Sender from System

	public Map<String, String> empIdMap = new HashMap<String, String>();
	// public Map<String, String> hkIdMap = new HashMap<String, String>();
	public Map<String, String> userIdMap = new HashMap<String, String>();
	public Map<String, String> loginIdMap = new HashMap<String, String>();
	public Map<String, String> cardIdMap = new HashMap<String, String>();
	
	public Boolean usrPwdNeedChangeInd;
	public Boolean identicalUsrNoImport;
	public Boolean oldusrPwdNeedUpdateInd;

	public ImpUser(Connection conn, WizbiniLoader wizbiniLoader, String logDir, String file_path,Boolean usrPwdNeedChangeInd,Boolean identicalUsrNoImport,Boolean oldusrPwdNeedUpdateInd) {
		con = conn;
		wizbini = wizbiniLoader;
		warning_p = 1000;
		log_dir = logDir;
		input_file = file_path;

		this.int_file_pre = "fsm_agent";

		this.log_file_name = "import_user_";

		log = Logger.getLogger("LOG");
		logSuccess = Logger.getLogger("SUCCESS_LOG");
		logFailure=Logger.getLogger("FAILURE_LOG");
		fieldLabel = ImportTemplate.importTitle.toArray(new String[ImportTemplate.importTitle.size()]);
		data_long = fieldLabel.length;
		this.usrPwdNeedChangeInd = usrPwdNeedChangeInd;
		this.identicalUsrNoImport = identicalUsrNoImport;
		this.oldusrPwdNeedUpdateInd = oldusrPwdNeedUpdateInd;
	}

	public void finalCheck(Vector userVec, ImportStatus importUserStatus, boolean clear) throws Exception {
		Vector<Long> exclude_ent_ids = new Vector<Long>();

		for (int i = 0; i < userVec.size(); i++) {
			UserBean record = (UserBean) userVec.get(i);
			record.usr_ent_id = dbRegUser.getEntIdByUsrId(con, record.getUser_id());
			if (record.usr_ent_id > 0) {
				exclude_ent_ids.add(record.usr_ent_id);
			}
		}

		// 取出所有不在导入文件中的用户的ent_id 放到importUserStatus.del_usr_lst中。
		if (clear) {
			importUserStatus.del_id_lst.addAll(dbRegUser.getDelUsrEntIds(con, exclude_ent_ids));
		}
	
	}

	public void importData(Vector userVec, loginProfile prof, ImportStatus importStatus,boolean clear) throws Exception {
		Timestamp now = cwSQL.getTime(con);
		// 获取该用户可以管理的用户组
		Vector can_mgt_usg_vec = null;
		if(AccessControlWZB.isSysAdminRole(prof.current_role)){
			can_mgt_usg_vec = dbUserGroup.getAllGroupId( con) ;
		}else{
			can_mgt_usg_vec = dbUserGroup.getAllTargetGroupIdForOfficer(con, prof.usr_ent_id);
			if (ViewTrainingCenter.isSuperTA(con, prof.root_ent_id, prof.usr_ent_id, prof.current_role)) {
				can_mgt_usg_vec.add(DEFAULT_GROUP_ROOT);
			}
		}
		// userVec提取所有用户组
		Map<String, Long> synUserMap = new HashMap<String, Long>(); // 保存同步成后的用户

		Map<String, Long> synGroupMap = new HashMap<String, Long>();

		Map<String, Long> synGardeMap = new HashMap<String, Long>();

		Map<String, Long> synPositionMap = new HashMap<String, Long>();

		if (notEmptyVector(userVec)) {
			for (UserBean user : (Vector<UserBean>) userVec) {
				if (!user.valid) { // 如果用户检查不通过，那么应该直接跳过该记录
					continue;
				}
				synGroupMap = synGroup(user, now, prof, synGroupMap, DEFAULT_GROUP_ROOT, can_mgt_usg_vec); // 保存同步成后的用户组
				synGrade(user, now, prof, synGardeMap, DEFAULT_GRADE_ROOT);
				if(wizbini.cfgSysSetupadv.isTcIndependent()){
					synPosition(user, synPositionMap, prof.usr_ent_id, String.valueOf(prof.my_top_tc_id),prof);
				}else{
					synPosition(user, synPositionMap, prof.usr_ent_id, "1",prof);
				}
			}
		}

		Map<String, Long> synExtCodeMap = new HashMap<String, Long>(); // 保存同步成后的用户
		Map<String, Long> supMap = new HashMap<String, Long>(); // 保存设置过Group
																// Supervisor的用户组
		System.gc();

		// 同步用户
		if (notEmptyVector(userVec)) {
			for (int i = 0; i < userVec.size(); i++) {
				UserBean user = (UserBean) userVec.elementAt(i);
				if (user.valid) {
					synUser(user, synUserMap, synGroupMap, now, prof, importStatus, can_mgt_usg_vec);
				}
			}

			System.gc();
		}

		// 同步用户的直属上司
		if (notEmptyVector(userVec)) {
			for (int i = 0; i < userVec.size(); i++) {
				UserBean user = (UserBean) userVec.elementAt(i);
				if (user.valid) {
					synUserDirect_supervise(user, synUserMap, prof);
				}else{
					importStatus.addErr();
				}
			}

		}

		if(clear){
			// 导入后清理
			clear(con, prof, importStatus);
		}
	}


	private void setUserGroupSupervisor(Connection con, loginProfile prof, UserBean user, Map<String, Long> synUserMap, Map<String, Long> synGroupMap, Map<String, Long> supMap,
			ImportStatus importStatus) {
		try {
			// set Group Supervisor
			String GSM = "GSM";
			String DSM = "DSM";

			if (supMap == null || supMap.get(GSM_PREFIX + GSM) == null) {
				if (synGroupMap != null && synGroupMap.get(GSM_PREFIX + GSM) != null && synGroupMap.get(GSM_PREFIX + GSM) > 0 && synUserMap != null && synUserMap.get(GSM) != null
						&& synUserMap.get(GSM) > 0) {
					long usr_ent_id = synUserMap.get(GSM);
					long target_ent_id = synGroupMap.get(GSM_PREFIX + GSM);
					DbSuperviseTargetEntity.delByTargetEntId(con, target_ent_id, DbSuperviseTargetEntity.SPT_TYPE_SUPERVISE);
					DbSuperviseTargetEntity dbspt = new DbSuperviseTargetEntity();
					dbspt.spt_source_usr_ent_id = usr_ent_id;
					dbspt.spt_type = DbSuperviseTargetEntity.SPT_TYPE_SUPERVISE;
					dbspt.spt_target_ent_id = target_ent_id;
					dbspt.spt_eff_start_datetime = Timestamp.valueOf(dbUtils.MIN_TIMESTAMP);
					dbspt.spt_eff_end_datetime = Timestamp.valueOf(dbUtils.MAX_TIMESTAMP);
					dbspt.ins(con, prof.usr_id);
					if (supMap.get(GSM_PREFIX + GSM) == null) {
						supMap.put(GSM_PREFIX + GSM, target_ent_id);
					}
				} else {
					logFailure("Line " + user.lineno + ": GSM [" + GSM + "] is not a valid or active user, Group Supervisor was not set");
				}
			}
			if (DSM != null && GSM.length() > 0) {
				if (supMap == null || supMap.get(DSM_PREFIX + DSM) == null) {
					if (synGroupMap != null && synGroupMap.get(DSM_PREFIX + DSM) != null && synGroupMap.get(DSM_PREFIX + DSM) > 0 && synUserMap != null
							&& synUserMap.get(DSM) != null && synUserMap.get(DSM) > 0) {
						long usr_ent_id = synUserMap.get(DSM);
						long target_ent_id = synGroupMap.get(DSM_PREFIX + DSM);
						DbSuperviseTargetEntity.delByTargetEntId(con, target_ent_id, DbSuperviseTargetEntity.SPT_TYPE_SUPERVISE);
						DbSuperviseTargetEntity dbspt = new DbSuperviseTargetEntity();
						dbspt.spt_source_usr_ent_id = usr_ent_id;
						dbspt.spt_type = DbSuperviseTargetEntity.SPT_TYPE_SUPERVISE;
						dbspt.spt_target_ent_id = target_ent_id;
						dbspt.spt_eff_start_datetime = Timestamp.valueOf(dbUtils.MIN_TIMESTAMP);
						dbspt.spt_eff_end_datetime = Timestamp.valueOf(dbUtils.MAX_TIMESTAMP);
						dbspt.ins(con, prof.usr_id);
						if (supMap.get(DSM_PREFIX + DSM) == null) {
							supMap.put(DSM_PREFIX + DSM, target_ent_id);
						}
					} else {
						logFailure("Line " + user.lineno + ": DSM [" + DSM + "] is not a valid or active user, Group Supervisor was not set");
					}
				}
			}
		} catch (Exception e) {
			importStatus.addErr();
			importStatus.setReturnCode(code_w);
			logFailure("set user other Group Supervisor error: lineno ->" + user.lineno + ". " + e.getMessage());
			CommonLog.error(e.getMessage(),e);
			rollback(con);
		}
	}

	private void setUserGroupDisplayBil(Connection con, Map<String, Long> synUserMap, Map<String, Long> synGroupMap) throws qdbException, SQLException {
		if (synGroupMap != null && synGroupMap.size() > 0) {
			for (String key : synGroupMap.keySet()) {
				long usg_ent_id = synGroupMap.get(key);
				String usr_ste_usr_id = key.substring(1);
				if (synUserMap.get(usr_ste_usr_id) != null && synUserMap.get(usr_ste_usr_id) > 0) {
					dbRegUser usr = new dbRegUser();
					usr.usr_ent_id = synUserMap.get(usr_ste_usr_id);
					usr.get(con);
					// dbUserGroup.updUsgDisplayBil(con, usg_ent_id,
					// usr.usr_display_bil);

				}
			}
		}
	}

	// 删除所有过期用户
	private void clear(Connection con, loginProfile prof, ImportStatus importStatus) throws Exception {
		ResultSet rs = null;
		PreparedStatement stmt = null;

		int idx = 1;
		if (notEmptyVector(importStatus.del_id_lst)) {

			AccessControlWZB acl = new AccessControlWZB();
			Hashtable tadm = null;
			long ta_ent_id = 3;
			try {
				tadm = acl.getRole(con, prof.root_ent_id, AccessControlWZB.ROL_STE_UID_TADM);
				// ta_ent_id = dbRegUser.getEntId( con,
				// wizbini.cfgSysSetupadv.getUserIntegration().getDefaultCosTa(),
				// prof.root_ent_id);
			} catch (Exception e) {
			}
			for (int i = 0; i < importStatus.del_id_lst.size(); i++) {
				long ent_id = ((Long) importStatus.del_id_lst.elementAt(i)).longValue();
				dbRegUser delUser = new dbRegUser();

				try {

					delUser.usr_ent_id = ent_id;
					delUser.get(con);
					if (dbRegUser.USR_STATUS_DELETED.equalsIgnoreCase(delUser.usr_status)) {
						continue;
					}
					delUser.usr_upd_date = delUser.ent_upd_date;
					dbUserGroup parentGroup = dbEntityRelation.getParentUserGroup(con, ent_id);
					// delUser.setCheckPendAppn(true);
					// 检查用户是否有等待审批的报名记录，如果有等待审批的报名记录，不允许删除用户
					// boolean hasPendingAppn =
					// DbAppnApprovalList.hasPendingAppn(con,
					// delUser.usr_ent_id);
					// if (hasPendingAppn) {
					// delAllPendingAppn(prof, delUser.usr_ent_id);
					// }

					String adm_ext_id = (String) tadm.get("rol_ext_id");
					Vector vSoleTADMItem = aeItemAccess.getSoleAccessItem(con, ent_id, adm_ext_id);
					if (vSoleTADMItem.size() > 0) {
						// get training admin label
						for (int j = 0; j < vSoleTADMItem.size(); j++) {
							aeItem itm = new aeItem();
							itm.itm_id = ((aeItemAccess.ViewItemAccessGroupByItem) vSoleTADMItem.elementAt(j)).iac_itm_id;
							itm.get(con);
							try {
								if (ta_ent_id > 0) {
									// String[] iac_id_lst = {"TADM_"+
									// prof.root_ent_id +"~" + ta_ent_id};
									// itm.saveItemAccess( con, iac_id_lst,
									// prof.root_ent_id, true);

									aeItemAccess iac = new aeItemAccess();
									long[] entIds = { ta_ent_id };
									iac.iac_itm_id = itm.itm_id;
									iac.iac_access_id = "TADM_" + prof.root_ent_id;
									iac.saveByRole(con, entIds, itm.itm_qdb_ind);
								}
							} catch (cwException e) {
								throw new qdbErrMessage(e.getMessage());
							}
						}
					}
					if (parentGroup == null) {
						delUser.del(con, prof, 0);
					} else {
						delUser.del(con, prof, parentGroup.usg_ent_id);
					}
					con.commit();

					// logSuccess("user: id -> " + delUser.usr_ste_usr_id +
					// ", ent_id -> " + ent_id + " deleted.");
					logSuccess(delUser.usr_extra_8 + ": Deleted");
					importStatus.addDel();
					logSuccess(delUser.usr_extra_8 + ": Terminated");
					importStatus.addTer();
				} catch (cwSysMessage e) {
					importStatus.addErr();
					importStatus.setReturnCode(code_w);
					logFailure("failed to delete user[ent_id -> " + ent_id + "] :" + e.getSystemMessage("en-us"));
					CommonLog.error(e.getMessage(),e);
					rollback(con);
				} catch (qdbErrMessage e) {
					importStatus.setReturnCode(code_w);
					// 这个异常主要出现在学员已经有等待审批的报名记录后，同步时还修改最高报名审批用户组时出现的。
					importStatus.addErr();
					logFailure("failed to delete user[ent_id -> " + ent_id + "] :" + e.getSystemMessage("en-us"));
					CommonLog.error(e.getMessage(),e);
					rollback(con);
				} catch (Exception e) {
					importStatus.addErr();
					importStatus.setReturnCode(code_w);
					logFailure("failed to delete user[ent_id -> " + ent_id + "] :" + e.getMessage());
					CommonLog.error(e.getMessage(),e);
					rollback(con);
				}
			}
		}
		// 删除用户后，查询没有用户的用户组
		Vector<Long> groupVec = new Vector<Long>();
		String sql = "";
		sql += " select usg_ent_id from usergroup, entity, EntityRelation usg_ern ";
		sql += " where usg_ent_id = ent_id and usg_ern.ern_child_ent_id = usg_ent_id ";
		sql += "   and not exists ( select usr_ern.ern_ancestor_ent_id from EntityRelation usr_ern  ";
		sql += "  	where usr_ern.ern_type = ? ";
		sql += "   	  and usr_ern.ern_ancestor_ent_id = usg_ent_id ";
		sql += "   ) ";
		sql += "   and ent_delete_timestamp is null ";
		sql += "   and usg_ern.ern_type = ? ";
		sql += "   and usg_ern.ern_parent_ind = ? ";
		sql += " order by usg_ern.ern_order desc ";

		try {
			stmt = con.prepareStatement(sql.toString());
			idx = 1;
			stmt.setString(idx++, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
			stmt.setString(idx++, dbEntityRelation.ERN_TYPE_USG_PARENT_USG);
			stmt.setBoolean(idx++, true);
			rs = stmt.executeQuery();
			while (rs.next()) {
				Long ent_id = rs.getLong("usg_ent_id");
				if (!groupVec.contains(ent_id)) {
					groupVec.add(ent_id);
				}
			}
		} catch (SQLException e) {
			logFailure("get empty usergroup list error." + e.getMessage());
			CommonLog.error(e.getMessage(),e);
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		for (int i = 0; i < groupVec.size(); i++) { // 删除用户组
			long usg_ent_id = groupVec.elementAt(i).longValue();

			// 删除前检查用户组下面是否还有用户或者用户组，如果有，则不删除用户组
			sql = "";
			sql += "  select usg_ent_id from userGroup where ( ";
			sql += "  exists ( ";
			sql += "  	select ern_ancestor_ent_id from EntityRelation where ern_type = ? and ern_ancestor_ent_id = ? ";
			sql += "  ) ";
			sql += "  or exists ( ";
			sql += "  	select ern_ancestor_ent_id from EntityRelation where ern_type = ? and ern_ancestor_ent_id = ? ";
			sql += "  ) ) ";
			sql += "  and usg_ent_id = ? ";
			try {
				stmt = con.prepareStatement(sql.toString());
				idx = 1;
				stmt.setString(idx++, dbEntityRelation.ERN_TYPE_USG_PARENT_USG);
				stmt.setLong(idx++, usg_ent_id);
				stmt.setString(idx++, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
				stmt.setLong(idx++, usg_ent_id);
				stmt.setLong(idx++, usg_ent_id);
				rs = stmt.executeQuery();

				boolean hasSubGroupOrUser = false;
				if (rs.next()) {
					hasSubGroupOrUser = true;
				}

				// 删除用户组前，检查用户组是否有下属用户组和用户，有的话不删除用户组
				if (!hasSubGroupOrUser) {
					dbUserGroup userGroup = new dbUserGroup();
					userGroup.usg_ent_id = usg_ent_id;
					userGroup.ent_id = usg_ent_id;
					userGroup.delGroup(con, true, false, prof.usr_id);

					con.commit();
					logSuccess("usergoup: ent_id -> " + usg_ent_id + " deleted!");
				}
			} catch (Exception e) {
				logFailure("delete usergoup[ent_id -> " + usg_ent_id + "] error" + ":"+ e.getMessage());
				CommonLog.error(e.getMessage(),e);
				rollback(con);
			} finally {
				cwSQL.cleanUp(rs, stmt);
			}
		}

		log(" clear end! ");
	}

	// 删除用户所有等待审批的报名记录
	private void delAllPendingAppn(loginProfile prof, long usr_ent_id) throws Exception {
		prof.current_role = "TADM_1";
		prof.common_role_id = dbRegUser.getCommnonRoleId(prof.current_role);

		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			Vector app_id_lst_vec = new Vector();
			Vector app_itm_id = new Vector();

			Vector app_upd_timestamp_lst_vec = new Vector();

			String sql = "";
			sql += " select distinct app_id,app_itm_id,app_upd_timestamp from aeAppnApprovalList, aeApplication ";
			sql += " where app_id = aal_app_id  and aal_app_ent_id = ?  and aal_status = ? ";

			stmt = con.prepareStatement(sql);
			stmt.setLong(1, usr_ent_id);
			stmt.setString(2, DbAppnApprovalList.STATUS_PENDING);

			rs = stmt.executeQuery();
			while (rs.next()) {
				Long app_id = rs.getLong("app_id");
				Timestamp t = rs.getTimestamp("app_upd_timestamp");
				app_itm_id.add(rs.getLong("app_itm_id"));
				app_id_lst_vec.add(app_id);
				app_upd_timestamp_lst_vec.add(t);
			}

			if (app_id_lst_vec != null && app_id_lst_vec.size() > 0) {
				aeApplication app = new aeApplication();
				String app_ids_str = "";
				for (int i = 0; i < app_id_lst_vec.size(); i++) {
					app.app_id = ((Long) app_id_lst_vec.elementAt(i)).longValue();
					app.app_ent_id = usr_ent_id;
					app.app_itm_id = ((Long) app_itm_id.elementAt(i)).longValue();
					app.del(con);
				}
				log("The following enrollment(s) [" + app_ids_str + "] has been deleted");
			}
		} catch (Exception e) {
			throw e;
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
	}

	/**
	 * 同步用户的直属上司
	 */
	private void synUserDirect_supervise(UserBean user, Map<String, Long> userMap, loginProfile prof) {
		try {
			log(LangLabel.getValue(prof.cur_lan, "line_number")+"  "+ user.lineno+LangLabel.getValue(prof.cur_lan, "set_direct_spuer")+LangLabel.getValue(prof.cur_lan, "lab_user_id")+"[" + user.getUser_id() + "].");
			// 检查用户是否存在
			long ent_id = 0;
			dbRegUser regUser = new dbRegUser();
			ent_id=findUserByUsrId(userMap, user.getUser_id());
			if(ent_id==0){
				throw new Exception(LangLabel.getValue(prof.cur_lan, "lab_user_id")+"[" + user.getUser_id()+"]" + LangLabel.getValue(prof.cur_lan, "not_find"));
			}
			regUser.ent_id = ent_id;
			regUser.usr_ent_id = ent_id;
			long usr_tcr_id = ViewTrainingCenter.getTopTc(con, regUser.ent_id, wizbini.cfgSysSetupadv.isTcIndependent());
			if(user.getDirect_supervisors()==null||user.getDirect_supervisors().equals(""))
				return;
			// 获取所有的直属上司
			String[] direct_supervisors = user.getDirect_supervisors().trim().split(",");
			if (direct_supervisors == null || direct_supervisors.length <= 0) {
				return;
			}
			List<Long> usr_ent_ids = new ArrayList<Long>();
			for (int i = 0; i < direct_supervisors.length; i++) {
				if (direct_supervisors[i] == null || direct_supervisors[i].trim().equals("")) {
					continue;
				}
				// 检查每个直属上司是否存在
				long usr_ent_id = findUserByUsrId(userMap, direct_supervisors[i].trim());
				if (usr_ent_id == 0)
					throw new Exception(LangLabel.getValue(prof.cur_lan, "lab_user_id")+"：" + direct_supervisors[i].trim() + LangLabel.getValue(prof.cur_lan, "not_find"));
				else
					userMap.put(direct_supervisors[i], usr_ent_id);
				
				// 检查每个直属上司的培训中心是否和用户的培训中心是否一样
				long tcrId = ViewTrainingCenter.getTopTc(con, usr_ent_id, wizbini.cfgSysSetupadv.isTcIndependent());
				if (tcrId != usr_tcr_id) {
					throw new Exception(LangLabel.getValue(prof.cur_lan, "not_set_direct_spuer"));
				}
				usr_ent_ids.add(usr_ent_id);

			}
			// 删除以前的直属上司关系
			DbSuperviseTargetEntity.delByTargetEntId(con, ent_id, DbSuperviseTargetEntity.SPT_TYPE_DIRECT_SUPERVISE);
			// 设置直属上司
			for (long id : usr_ent_ids) {
				DbSuperviseTargetEntity dbspt = new DbSuperviseTargetEntity();
				dbspt.spt_source_usr_ent_id = id;
				dbspt.spt_type = DbSuperviseTargetEntity.SPT_TYPE_DIRECT_SUPERVISE;
				dbspt.spt_target_ent_id = ent_id;
				dbspt.spt_eff_start_datetime = Timestamp.valueOf(dbUtils.MIN_TIMESTAMP);
				dbspt.spt_eff_end_datetime = Timestamp.valueOf(dbUtils.MAX_TIMESTAMP);
				dbspt.ins(con, prof.usr_id);
			}
			con.commit();
			logSuccess(LangLabel.getValue(prof.cur_lan, "line_number")+ user.lineno +"  "+LangLabel.getValue(prof.cur_lan, "set_direct_spuer")+ LangLabel.getValue(prof.cur_lan, "lab_syn_success"));
		} catch (Exception e) {
			CommonLog.error(e.getMessage(),e);
			logFailure(LangLabel.getValue(prof.cur_lan, "line_number") + user.lineno+"  "+LangLabel.getValue(prof.cur_lan, "set_direct_spuer_err")+" " + e.getMessage());
			rollback(con);
		}

	}

	/**
	 * 同步用户
	 */
	private void synUser(UserBean user, Map<String, Long> userMap, Map<String, Long> groupMap, Timestamp now, loginProfile prof, ImportStatus importStatus,Vector can_mgt_usg_vec) {
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			boolean is_sa = AccessControlWZB.isSysAdminRole(prof.current_role);
			List<String> roleText=new ArrayList<String>();
		
			if (user.getRole() != null && user.getRole().trim().length() > 0) {
				String[] strRole=cwUtils.splitToString(user.getRole().trim(), ",");				
				for(String role_ste:strRole){
					String role_text=DbAcRole.getRoleExtId(con, role_ste.trim(), prof.root_ent_id);
					if(role_text==null){
						throw new Exception(role_ste+LangLabel.getValue(prof.cur_lan, "not_find"));
					} else if(roleText.contains(role_text)){
						//检查是否输入了重复的角色
						user.valid=false;
						logFailure(LangLabel.getValue(prof.cur_lan, "line_number") + user.lineno+"  "+ LangLabel.getValue( prof.cur_lan,"import_user_role_err") + " " );
						return ;
						//throw new Exception(LangLabel.getValue( prof.cur_lan,"import_user_role_err") + " " );
					} else {						
						roleText.add(role_text);
					}
					if(!is_sa){
						//非系统管理员不能为用户分配系统管理员角色
						if(AccessControlWZB.isSysAdminRole(role_text)){
							throw new Exception(LangLabel.getValue( prof.cur_lan,"import_user_cannot_ass_sa") + " " );
						}
					}
				}
				
	
			} 

			boolean is_add = false;

			// 检查用户是否存在
			long ent_id = 0;
			dbRegUser regUser = new dbRegUser();
			ent_id = findUserByUsrId(userMap,user.getUser_id());
			if (ent_id > 0) {
				// 对于已存在的用户是否做出错处理。
                if(null != identicalUsrNoImport && identicalUsrNoImport == true){
                	user.valid=false;   //LangLabel.getValue( prof.cur_lan,"line_number")+user.lineno+
					throw new Exception("  ["+user.getUser_id()+"]"+LabelContent.get(prof.cur_lan, "label_core_user_management_25")+".");
                }else{
                	regUser.ent_id = ent_id;
    				regUser.usr_ent_id = ent_id;
    				regUser.get(con);
    				//检查用户是否具备权限去修改用户信息
    				dbEntityRelation entityRealtion=new dbEntityRelation();
    				long old_usg_ent_id = 0;
    				if (regUser.ent_delete_timestamp != null) {
    					old_usg_ent_id = dbEntityRelationHistory.getParentId( con, ent_id);
    				}else{
    					old_usg_ent_id = entityRealtion.getParentId(con, ent_id, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
    				}
    				if(!can_mgt_usg_vec.contains(old_usg_ent_id) && !can_mgt_usg_vec.contains(DEFAULT_GROUP_ROOT)){
    					user.valid=false;
    					throw new Exception(LangLabel.getValue( prof.cur_lan,"line_number")+user.lineno+"  ["+user.getUser_id()+"]"+LangLabel.getValue(prof.cur_lan,"not_Jurisdiction_update_user" )+".");
    				}
    				if(dbRegUser.getRdByUsrId(con, ent_id)){
    				    user.valid=false;
                        throw new Exception(LangLabel.getValue(prof.cur_lan,"import_user_cannot_upd_sa" )+".");
    				}
                }
			} else {

				regUser.usr_ste_ent_id = DEFAULT_ENT_ID_ROOT;
			}

			setRegUserFieldsFromImportUser(con, regUser, user, now);
			Map map = getUserColMap(con, regUser);
			Vector vColName = (Vector) map.get("col_name");
			Vector vColType = (Vector) map.get("col_type");
			Vector vColValue = (Vector) map.get("col_value");
			Vector vExtColName = (Vector) map.get("ext_col_name");
			Vector vExtColType = (Vector) map.get("ext_col_type");
			Vector vExtColValue = (Vector) map.get("ext_col_value");

			// 岗位ID
			regUser.usr_ske_id = user.upt_id;

			// 最高审批用户组
			if (user.getApp_approval_usg_ent_id() != null && !user.getApp_approval_usg_ent_id().trim().equals("")) {
				long group_id=findGroupByCode(groupMap, user.getApp_approval_usg_ent_id());
				if(group_id<=0){
					user.valid=false;
					throw new Exception(LangLabel.getValue( prof.cur_lan,"app_approval")+"："+user.getApp_approval_usg_ent_id()+","+LangLabel.getValue( prof.cur_lan,"not_find")+".");
				}else{
					groupMap.put(user.getApp_approval_usg_ent_id(), group_id);
				}
				user.highest_approval_usg_id =group_id;
			}
			// 下属部门
			if (user.getSupervised_groups() != null && !user.getSupervised_groups().trim().equals("")) {
				String[] Supervised_groups = user.getSupervised_groups().trim().split(",");
				List<String> Supervised_groups_taget = new ArrayList<String>();
				for (int i = 0; i < Supervised_groups.length; i++) {
					if (Supervised_groups[i] != null && !Supervised_groups[i].trim().equals("")) {
						long group_id=findGroupByCode(groupMap, Supervised_groups[i].trim());
						if(group_id<=0){
							user.valid=false;
							throw new Exception(LangLabel.getValue(prof.cur_lan,"262" )+"["+Supervised_groups[i].trim()+"]"+LangLabel.getValue( prof.cur_lan,"not_find")+".");
						}else{
							groupMap.put(Supervised_groups[i], group_id);
						}
						Supervised_groups_taget.add(String.valueOf(group_id));
					}
				}
				if (Supervised_groups_taget.size() != 0) {
					user.supervise_target_ent_ids = Supervised_groups_taget.toArray(new String[Supervised_groups_taget.size()]);
				}
			}

			if(user.usr_ugr_id < DEFAULT_GRADE_ID){
				user.usr_ugr_id = DEFAULT_GRADE_ID;
			}
			// 用户关联的用户组和职务
			regUser.usr_attribute_ent_ids = new long[] { user.usr_usg_id, user.usr_ugr_id };
			regUser.usr_attribute_relation_types = new String[] { dbEntityRelation.ERN_TYPE_USR_PARENT_USG, dbEntityRelation.ERN_TYPE_USR_CURRENT_UGR };
			if (ent_id > 0) {
 
				// 是否需要进行最高审批报名用户组更新
				if (user.highest_approval_usg_id != 0) {
					// 检测是否报名
					boolean hasPendingAppn = DbAppnApprovalList.hasPendingAppn(con, ent_id);
					// 如果已经报名并且最高审评用户组不等于当前的同步的最高审评报名用户组
					if (hasPendingAppn && regUser.usr_app_approval_usg_ent_id != user.highest_approval_usg_id) {
						logFailure(LangLabel.getValue( prof.cur_lan,"line_number") + user.lineno + "  "+LangLabel.getValue( prof.cur_lan,"not_update_app_approval")+".");
					} else {
						// 添加最高报名审批用户组
						vColName.add("usr_app_approval_usg_ent_id");
						vColType.add(DbTable.COL_TYPE_LONG);
						vColValue.add(Long.valueOf(user.highest_approval_usg_id));
					}
				}
				// 下属部门
				regUser.supervise_target_ent_ids = user.supervise_target_ent_ids;
				// 如果用户删除就恢复
				if (regUser.ent_delete_timestamp != null) {
					regUser.unDelete(con);
				}
				// 更新用户，当选中“更新用户密码”且密码不为空时，更新当前用户密码。
				if (null != oldusrPwdNeedUpdateInd && oldusrPwdNeedUpdateInd == true && null != regUser.usr_pwd && !regUser.usr_pwd.equals("")) {
					vColName.add("usr_pwd");
					vColType.add(DbTable.COL_TYPE_STRING);
					vColValue.add(regUser.usr_pwd);
					//更新密码需要判断   --修改密码时间
					vColName.add("usr_pwd_upd_timestamp");
					vColType.add(DbTable.COL_TYPE_TIMESTAMP);
					vColValue.add(cwSQL.getTime(con));
				}
			
				
				// 修改角色，
				if (roleText != null && roleText.size() > 0) {
					if(!is_sa){
						//非系统管理员不能修改系统管理员角色的用户信息
						List roleArray = DbAcRole.getRolesCanLogin(con, ent_id, now);
						 if(roleArray.size()>0&&roleArray!=null){
				        	for (int i = 0; i < roleArray.size(); i++) {
				        		DbAcRole role = (DbAcRole)roleArray.get(i);
				        		if(role!=null){
				        			if(AccessControlWZB.isSysAdminRole(role.rol_ext_id)){
				        				throw new Exception(LangLabel.getValue( prof.cur_lan,"import_user_cannot_ass_sa") );
									
				        			}
				        		}
							}
						 }
					}
					
					//大陆版本在导入用户时，不允许修改用户的角色，但在HKO版本中需求，所以在HKO版本只需求把这段开发就可以。
					
					regUser.usr_roles =roleText.toArray(new String[roleText.size()]);
					regUser.usr_roles_ends = new String[regUser.usr_roles.length];
					regUser.usr_roles_starts = new String[regUser.usr_roles.length];
					for (int v = 0; v < regUser.usr_roles.length; v++) {
						regUser.usr_roles_ends[v] = "UNLIMITED";
						regUser.usr_roles_starts[v] = "IMMEDIATE";
					}
					//判断用该用户是否为课程的唯一管理员
					 Vector vec = regUser.checkTaRelative(con, prof.root_ent_id);
					
					 if(vec != null) {
						 user.valid=false;
						 throw new Exception(LangLabel.getValue( prof.cur_lan,"import_user_cannot_remove_ta")  );
				      }
				      

				} 
				
				// 更新用户
				regUser.updUser(con, prof, vColName, vColType, vColValue, null, null, vExtColName, vExtColType, vExtColValue, null, null, wizbini.cfgTcEnabled);

				is_add = false;
				
				ObjectActionLog log = new ObjectActionLog(user.usr_ent_id, 
						regUser.usr_ste_usr_id,
						user.getName(),
						ObjectActionLog.OBJECT_TYPE_USR,
						ObjectActionLog.OBJECT_ACTION_UPD,
						ObjectActionLog.OBJECT_ACTION_TYPE_IMPORT,
						prof.getUsr_ent_id(),
						prof.getUsr_last_login_date(),
						prof.getIp()
				);
				SystemLogContext.saveLog(log, SystemLogTypeEnum.OBJECT_ACTION_LOG);

			} else {
				//新用户登陆后是否需要修改密码
				if(null!=usrPwdNeedChangeInd && usrPwdNeedChangeInd==true){
					regUser.usr_app_approval_usg_ent_id = 1;
					vColName.add("usr_pwd_need_change_ind");
					vColType.add(DbTable.COL_TYPE_LONG);
					vColValue.add(regUser.usr_app_approval_usg_ent_id);
				}

				// 如果新增用户没有密码就取用户名的前六位数   -- 修改为：新增用户没有密码将会使用系统设置中的默认密码。
				if (regUser.usr_pwd == null || (regUser.usr_pwd != null && "".equalsIgnoreCase(regUser.usr_pwd.trim()))) {

					vColName.add("usr_pwd");
					vColType.add(DbTable.COL_TYPE_STRING);
					vColValue.add(Application.SYS_DEFAULT_USER_PASSWORD.trim());  
					/*if (regUser.usr_ste_usr_id.trim().length() < 6) {
						vColValue.add(regUser.usr_ste_usr_id.trim());
					} else {
						vColValue.add(regUser.usr_ste_usr_id.trim().substring(0, 6));
					}*/
				} else {
					vColName.add("usr_pwd");
					vColType.add(DbTable.COL_TYPE_STRING);
					vColValue.add(regUser.usr_pwd);
				}
				// 添加最高审批报名组
				if (user.highest_approval_usg_id != 0) {
					vColName.add("usr_app_approval_usg_ent_id");
					vColType.add(DbTable.COL_TYPE_LONG);
					vColValue.add(Long.valueOf(user.highest_approval_usg_id));
				}
				// 下属部门
				regUser.supervise_target_ent_ids = user.supervise_target_ent_ids;
				// 默认添加用户时赋予学员角色，
				if (roleText != null && roleText.size() > 0) {
					regUser.usr_roles =roleText.toArray(new String[roleText.size()]);
					regUser.usr_roles_ends = new String[regUser.usr_roles.length];
					regUser.usr_roles_starts = new String[regUser.usr_roles.length];
					for (int v = 0; v < regUser.usr_roles.length; v++) {
						regUser.usr_roles_ends[v] = "UNLIMITED";
						regUser.usr_roles_starts[v] = "IMMEDIATE";
					}

				} else {
					regUser.usr_roles = new String[] { "NLRN_1" };
					regUser.usr_roles_ends = new String[] { "UNLIMITED" };
					regUser.usr_roles_starts = new String[] { "IMMEDIATE" };
				}

				regUser.insUser(con, prof, vColName, vColType, vColValue, null, null, vExtColName, vExtColType, vExtColValue, null, null, null, wizbini.cfgTcEnabled);
				ent_id = regUser.usr_ent_id;
				is_add = true;
				ObjectActionLog log = new ObjectActionLog(regUser.usr_ent_id, 
						regUser.usr_ste_usr_id,
						user.getName(),
						ObjectActionLog.OBJECT_TYPE_USR,
						ObjectActionLog.OBJECT_ACTION_ADD,
						ObjectActionLog.OBJECT_ACTION_TYPE_IMPORT,
						prof.getUsr_ent_id(),
						prof.getUsr_last_login_date(),
						prof.getIp()
				);
				SystemLogContext.saveLog(log, SystemLogTypeEnum.OBJECT_ACTION_LOG);
			}

			userMap.put(user.getUser_id(), new Long(ent_id));
			if (is_add) {
				importStatus.addIns();
				logSuccess(LangLabel.getValue( prof.cur_lan,"line_number")+user.lineno+"  "+LangLabel.getValue( prof.cur_lan,"lab_user_id")+"["+user.getUser_id()+"]"+LangLabel.getValue(prof.cur_lan,"add_user_success")+".");
			} else {
				importStatus.addUpd();
				logSuccess(LangLabel.getValue(prof.cur_lan,"line_number" )+user.lineno+"  "+LangLabel.getValue( prof.cur_lan,"lab_user_id")+"["+user.getUser_id()+"]"+LangLabel.getValue( prof.cur_lan,"update_user_success")+".");
			}
			con.commit();
			importStatus.addSuccess();
		
		} catch (Exception e) {
			user.valid=false;
			logFailure(LangLabel.getValue( prof.cur_lan,"line_number")+user.lineno+"  "+LangLabel.getValue( prof.cur_lan,"import_user_err") + " "  + e.getMessage());
			CommonLog.error(e.getMessage(),e);
			rollback(con);
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
	}

	private void setRegUserFieldsFromImportUser(Connection con, dbRegUser regUser, UserBean user, Timestamp now) {
		// 是否同步
		regUser.ent_syn_ind = true;
		// 同步时间
		regUser.ent_syn_date = now;
		// 登录名
		regUser.usr_ste_usr_id = user.getUser_id();
		// 全名
		regUser.usr_display_bil = user.getName();
		// 密码
		regUser.usr_pwd = user.getPassword();
		// 加入公司日期
		if (user.getJoin_date() != null) {
			regUser.usr_join_datetime = user.getJoin_date();
		} else {
			regUser.usr_join_datetime = null;
		}
		// 性别
		regUser.usr_gender = user.getGender();
		// 来源
		regUser.usr_source = user.getSource();
		// 邮箱
		regUser.usr_email = user.getEmail();
		// 电话
		regUser.usr_tel_1 = user.getPhone();
		// 传真
		regUser.usr_fax_1 = user.getFax();
		// QQ
		regUser.urx_extra_41 = user.getExtension_41();
		// 微信
		regUser.urx_extra_42 = user.getExtension_42();
		// 职称
		regUser.usr_job_title = user.getJob_title();
		//昵称
		regUser.usr_nickname=user.getNickname();
		//出生日期
		regUser.usr_bday=user.getDate_of_birth();
		//个人描述
		regUser.urx_extra_44=user.getExtension_44();
		//兴趣
		regUser.urx_extra_45=user.getExtension_45();
		//账号有效期
		regUser.usr_extra_datetime_11=user.getExtension_11();
	}

	private Map getUserColMap(Connection con, dbRegUser regUser) throws SQLException {
		Map map = new HashMap();
		Vector vColName = new Vector();
		Vector vColType = new Vector();
		Vector vColValue = new Vector();
		// 用户登录名
		vColName.add("usr_ste_usr_id");
		vColType.add(DbTable.COL_TYPE_STRING);
		vColValue.add(regUser.usr_ste_usr_id);
		// 全名
		vColName.add("usr_display_bil");
		vColType.add(DbTable.COL_TYPE_STRING);
		vColValue.add(regUser.usr_display_bil);
		// 加入公司日期
		vColName.add("usr_join_datetime");
		vColType.add(DbTable.COL_TYPE_TIMESTAMP);
		vColValue.add(regUser.usr_join_datetime);
		// 性别
		vColName.add("usr_gender");
		vColType.add(DbTable.COL_TYPE_STRING);
		vColValue.add(regUser.usr_gender);
		// 邮箱
		vColName.add("usr_email");
		vColType.add(DbTable.COL_TYPE_STRING);
		vColValue.add(regUser.usr_email);
		// 电话
		vColName.add("usr_tel_1");
		vColType.add(DbTable.COL_TYPE_STRING);
		vColValue.add(regUser.usr_tel_1);
		// 传真
		vColName.add("usr_fax_1");
		vColType.add(DbTable.COL_TYPE_STRING);
		vColValue.add(regUser.usr_fax_1);
		// 职称
		vColName.add("usr_job_title");
		vColType.add(DbTable.COL_TYPE_STRING);
		vColValue.add(regUser.usr_job_title);

		// 用户状态默认为“OK”
		vColName.addElement("usr_status");
		vColType.addElement(DbTable.COL_TYPE_STRING);
		vColValue.addElement(dbRegUser.USR_STATUS_OK);
		
		//昵称
		vColName.addElement("usr_nickname");
		vColType.addElement(DbTable.COL_TYPE_STRING);
		vColValue.addElement(regUser.usr_nickname);


		//出生日期
		vColName.addElement("usr_bday");
		vColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
		vColValue.addElement(regUser.usr_bday);
		
		map.put("col_name", vColName);
		map.put("col_type", vColType);
		map.put("col_value", vColValue);

		// extension
		Vector vExtColName = new Vector();
		Vector vExtColType = new Vector();
		Vector vExtColValue = new Vector();


		//个人描述
		vExtColName.add("urx_extra_44");
		vExtColType.add(DbTable.COL_TYPE_STRING);
		vExtColValue.add(regUser.urx_extra_44);
		//兴趣
		vExtColName.add("urx_extra_45");
		vExtColType.add(DbTable.COL_TYPE_STRING);
		vExtColValue.add(regUser.urx_extra_45);
		// QQ
		vExtColName.add("urx_extra_41");
		vExtColType.add(DbTable.COL_TYPE_STRING);
		vExtColValue.add(regUser.urx_extra_41);
		// 微信
		vExtColName.add("urx_extra_42");
		vExtColType.add(DbTable.COL_TYPE_STRING);
		vExtColValue.add(regUser.urx_extra_42);
		
		if(!wizbini.cfgSysSetupadv.isTcIndependent()){
			//账号有效期
			vExtColName.add("urx_extra_datetime_11");
			vExtColType.add(DbTable.COL_TYPE_TIMESTAMP);
			vExtColValue.add(regUser.usr_extra_datetime_11);
		}
		
		map.put("ext_col_name", vExtColName);
		map.put("ext_col_type", vExtColType);
		map.put("ext_col_value", vExtColValue);
		return map;
	}

	/**
	 * 同步用户组
	 */
	private Map<String, Long> synGroup(UserBean user, Timestamp now, loginProfile prof, Map<String, Long> synGroupMap, long p_id, Vector can_mgt_usg_vec) {
		// user.highest_approval_usg_id = p_id;
		long parentId = p_id;
		String code = null;
		String title = null;
		for (int level = 0; level < usg_level_total; level++) {
			switch (level) {
			case 0:
				code = user.getGroup_code_level1();
				title = user.getGroup_title_level1();
				break;
			case 1:
				code = user.getGroup_code_level2();
				title = user.getGroup_title_level2();
				break;
			case 2:
				code = user.getGroup_code_level3();
				title = user.getGroup_title_level3();
				break;
			case 3:
				code = user.getGroup_code_level4();
				title = user.getGroup_title_level4();
				break;
			case 4:
				code = user.getGroup_code_level5();
				title = user.getGroup_title_level5();
				break;
			case 5:
				code = user.getGroup_code_level6();
				title = user.getGroup_title_level6();
				break;
			case 6:
				code = user.getGroup_code_level7();
				title = user.getGroup_title_level7();
				break;

			}
			if (code == null || ("").equals(code)) {
				break;
			}
			// 如果检查到用户组编号存在则不进行同步
			if (synGroupMap.get(code) != null) {
				parentId = ((Long) synGroupMap.get(code)).longValue();
			} else {
				parentId = synGroup(user, code, title, parentId, synGroupMap, now, prof, can_mgt_usg_vec);
			}
		}
		// 用户关联的用户组ID
		user.usr_usg_id = parentId;
		return synGroupMap;
	}

	/**
	 * 获取职务并进行同步
	 * @param user
	 * @param now
	 * @param prof
	 * @param synGradeMap
	 * @param p_id
	 * @return
	 */
	private Map<String, Long> synGrade(UserBean user, Timestamp now, loginProfile prof, Map<String, Long> synGradeMap, long p_id) {
		long parentId = p_id;
		String code = null;
		String title = null;
		for (int level = 0; level < ugr_level_total; level++) {
			switch (level) {
			case 0:
				code = user.getGrade_code_level1();
				title = user.getGrade_title_level1();
				break;
			case 1:
				code = user.getGrade_code_level2();
				title = user.getGrade_title_level2();
				break;
			case 2:
				code = user.getGrade_code_level3();
				title = user.getGrade_title_level3();
				break;
			}

			if (synGradeMap.get(code) != null) {
				parentId = synGradeMap.get(code);
			} else {
				if ("Unspecified".equals(code)) {
					parentId = DEFAULT_GRADE_ID;
				} else {
					if (code != null && code.trim().length() > 0) {
						parentId = synGarde(user,code, title, parentId, synGradeMap, now, prof);
					}
				}
			}
		}
		// 用户关联的职务ID
		user.usr_ugr_id = parentId;
		return synGradeMap;
	}
	/**
	 * 同步岗位
	 * @param user
	 * @param synPositionMap
	 * @param usr_ent_id
	 * @param tcr_id
	 * @return
	 */
	private Map<String, Long> synPosition(UserBean user, Map<String, Long> synPositionMap, long usr_ent_id, String tcr_id,loginProfile prof) {
		DbUserPosition upt = new DbUserPosition();
		String code = user.getCompetency();
		long upt_id = 0;
		if (code != null && code.length() > 0) {
			if (synPositionMap.get(code) != null) {
				upt_id = synPositionMap.get(code);
			} else {
				try {
					upt_id = upt.getUptIdByCode(con, usr_ent_id, code, tcr_id);
					if (upt_id == 0) {
						upt_id = upt.insertUserPosition(con, usr_ent_id, code, code, "", tcr_id);
						ObjectActionLog log = new ObjectActionLog(upt_id, 
								code,
								code,
								ObjectActionLog.OBJECT_TYPE_UPT,
								ObjectActionLog.OBJECT_ACTION_ADD,
								ObjectActionLog.OBJECT_ACTION_TYPE_IMPORT,
								usr_ent_id,
								prof.getUsr_last_login_date(),
								prof.getIp()
						);
						SystemLogContext.saveLog(log, SystemLogTypeEnum.OBJECT_ACTION_LOG);
					}
				} catch (SQLException e) {
					user.valid=false;
					logFailure(LangLabel.getValue(LangLabel.getValue( prof.cur_lan,"line_number")+user.lineno+"  "+ prof.cur_lan,"import_position_err")+ "  "+ e.getMessage());
					CommonLog.error(e.getMessage(),e);
				}
			}
		}
		// 用户关联的岗位ID
		user.upt_id = upt_id;
		return synPositionMap;
	}

	/**
	 * 同步用户组
	 * @param user
	 * @param code
	 * @param title
	 * @param parentGroupId
	 * @param synGroupMap
	 * @param now
	 * @param prof
	 * @param can_mgt_usg_vec
	 * @return
	 */
	private long synGroup(UserBean user, String code, String title, long parentGroupId, Map<String, Long> synGroupMap, Timestamp now, loginProfile prof, Vector can_mgt_usg_vec) {
		long ent_id = 0;

		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			log(LangLabel.getValue(prof.cur_lan,"import_user_group") + code);
			// 检查用户组是否已经在数据里面
			ent_id = findGroupByCode(synGroupMap, code);
			cwSQL.cleanUp(rs, stmt);
			/**
			 * 由于反馈觉得新方式不好用，所以把新的干掉，改成旧的    2016-07-07  bill
			 */
			// 更新
			if (ent_id > 0) {
				// 检查用户是否具备权限去修改用户组
				if (!can_mgt_usg_vec.contains(ent_id) && !can_mgt_usg_vec.contains(DEFAULT_GROUP_ROOT)) {
					user.valid = false;
					throw new Exception(LangLabel.getValue( prof.cur_lan,"lab_group")+LangLabel.getValue( prof.cur_lan,"lab_maohao")+code+" [ent_id ="+ ent_id+"], "+LangLabel.getValue( prof.cur_lan,"not_Jurisdiction_update_group")+".");
				}
				/*	dbUserGroup userGroup = new dbUserGroup();
				userGroup.ent_id = ent_id;
				userGroup.usg_ent_id = ent_id;
				userGroup.get(con);
				userGroup.ent_syn_ind = true;
				userGroup.ent_syn_date = now;
				userGroup.usg_display_bil = title;
				userGroup.upd(con, prof);

				// 如果用户组已经在系统中被手工删除，则还原该用户组
				if (userGroup.ent_delete_timestamp != null) {
					userGroup.unDelete(con);
				}

				long pre_usg_ent_id = dbEntityRelation.getParentId(con, ent_id, dbEntityRelation.ERN_TYPE_USG_PARENT_USG);
				boolean restruct = false;
				if (pre_usg_ent_id > 0 && pre_usg_ent_id != parentGroupId) {
					restruct = true;
					if (!can_mgt_usg_vec.contains(parentGroupId) || !can_mgt_usg_vec.contains(pre_usg_ent_id)) {
						user.valid = false;
						throw new Exception(LangLabel.getValue( prof.cur_lan,"line_number")+user.lineno+"  "+ LangLabel.getValue( prof.cur_lan,"lab_group")+"["+code+"] "+LangLabel.getValue( prof.cur_lan,"not_Jurisdiction_update_group")+".");
					}
				}
				long groupId = DEFAULT_GROUP_ROOT;
				if (parentGroupId > 0) {
					groupId = parentGroupId;
				}
				
				EntityFullPath entityfullpath = EntityFullPath.getInstance(con);
				
				if (groupId > 0) {
					userGroup.delEntityRelationAsChild(con, prof.usr_id, now);
					userGroup.insEntityRelation(con, groupId, prof.usr_id);
					entityfullpath.enclose(con, ent_id);
				}
				if (restruct) {
					// 如果用户组树结构发成变化，则需要重建该用户组下的后代结点的关系
					// 重构该用户组下的子用户组、用户关系
					String[] types = { dbEntityRelation.ERN_TYPE_USG_PARENT_USG, dbEntityRelation.ERN_TYPE_USR_PARENT_USG };
					Vector child_vec = dbEntityRelation.getChild(con, ent_id, types);
					if (child_vec != null && child_vec.size() > 0) {
						for (int i = 0; i < child_vec.size(); i++) {
							dbEntityRelation dbEr = (dbEntityRelation) child_vec.get(i);
							dbEr.ern_syn_timestamp = now;
							dbEr.insEr(con, prof.usr_id);
							if(dbEr.ern_type.equals(dbEntityRelation.ERN_TYPE_USG_PARENT_USG)){
								entityfullpath.enclose(con, dbEr.ern_child_ent_id);
							}
						}
					}
				}

				logSuccess(LangLabel.getValue( prof.cur_lan,"line_number")+user.lineno+"  "+LangLabel.getValue(prof.cur_lan,"update_group")+"[" + code+"]" +LangLabel.getValue(prof.cur_lan,"lab_syn_success" ) +".");
*/
			} else { // 添加
				if(AccessControlWZB.isRoleTcInd(prof.current_role)){
					if (!can_mgt_usg_vec.contains(parentGroupId)) {
						user.valid = false;
						throw new Exception(LangLabel.getValue( prof.cur_lan,"line_number")+user.lineno+"  "+ LangLabel.getValue( prof.cur_lan,"lab_group")+"["+code+"] "+LangLabel.getValue( prof.cur_lan,"not_Jurisdiction_update_group")+".");
					}
				}
			/*	dbUserGroup userGroup = new dbUserGroup();
				userGroup.ent_syn_ind = true;
				userGroup.ent_syn_date = now;
				userGroup.ent_ste_uid = code;
				userGroup.usg_code = code;
				userGroup.usg_display_bil = title;
				userGroup.usg_ent_id_root = DEFAULT_GROUP_ROOT;
				userGroup.ins(con, parentGroupId, prof.usr_id);
				ent_id = userGroup.usg_ent_id;

				if (!can_mgt_usg_vec.contains(ent_id)) {
					can_mgt_usg_vec.add(ent_id);
				}
				logSuccess(LangLabel.getValue( prof.cur_lan,"line_number")+user.lineno+"  "+LangLabel.getValue( prof.cur_lan,"add_group")+"[" + code + "]" +LangLabel.getValue( prof.cur_lan,"lab_syn_success") +".");
				*/
			}
			
			
			/**
			 * 旧的，只判断一个用户ID，没有就抛出异常
			 */
			if (ent_id > 0) {
			//	logSuccess(LangLabel.getValue( prof.cur_lan,"line_number")+user.lineno+"  "+LangLabel.getValue(prof.cur_lan,"update_group")+"[" + code+"]" +LangLabel.getValue(prof.cur_lan,"lab_syn_success" ) +".");
			}else{
				user.valid = false;
				throw new Exception(LangLabel.getValue( prof.cur_lan,"lab_group")+"["+code+"] "+LangLabel.getValue( prof.cur_lan,"lab_not_user_group")+".");
			}
			
			synGroupMap.put(code, new Long(ent_id));
			con.commit();
		} catch (Exception e) {
			user.valid = false;
			logFailure(LangLabel.getValue( prof.cur_lan,"line_number")+user.lineno+" " + e.getMessage());
			CommonLog.error(e.getMessage(),e);
			rollback(con);
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return ent_id;
	}

	/**
	 * 同步职务
	 * @param user
	 * @param code
	 * @param title
	 * @param parentGardeId
	 * @param synGardeMap
	 * @param now
	 * @param prof
	 * @return
	 */
	private long synGarde(UserBean user,String code, String title, long parentGardeId, Map<String, Long> synGardeMap, Timestamp now, loginProfile prof) {
		long ent_id = 0;
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			log(LangLabel.getValue( prof.cur_lan,"import_grade") + code);

			// 检查职务是否已经在数据里面
			ent_id = findGradeByCode(code);
			cwSQL.cleanUp(rs, stmt);
			/**
			 * 由于反馈觉得新方式不好用，所以把新的干掉，改成旧的    2016-07-07  bill
			 */
			/*if (ent_id > 0) { // 更新
				DbUserGrade userGrade = new DbUserGrade();
				userGrade.ent_id = ent_id;
				userGrade.ugr_ent_id = ent_id;
				userGrade.get(con);
				//判断培训中心是否相同
				if(userGrade.ugr_tcr_id!=prof.my_top_tc_id){
					user.valid=false;
					throw new Exception(LangLabel.getValue( prof.cur_lan,"lab_grade")+"："+code+", "+LangLabel.getValue( prof.cur_lan,"not_Jurisdiction_update_grade")+".");
				}
				userGrade.ent_syn_ind = true;
				userGrade.ent_syn_date = now;
				userGrade.ugr_display_bil = title;
				userGrade.upd(con, prof);
				// 如果用户组已经在系统中被手工删除，则还原该用户组
				if (userGrade.ent_delete_timestamp != null) {
					userGrade.unDelete(con);
				}
				long gradeId = DEFAULT_GRADE_ID;
				if (parentGardeId > 0) {
					gradeId = parentGardeId;
				}
				if (gradeId > 0) {
					userGrade.delEntityRelationAsChild(con, prof.usr_id, now);
					userGrade.insEntityRelation(con, gradeId, prof.usr_id);
				}
				logSuccess(LangLabel.getValue( prof.cur_lan,"line_number") + user.lineno + "  "+LangLabel.getValue( prof.cur_lan,"lab_grade")+"[" + code + "]"+LangLabel.getValue( prof.cur_lan,"update_grade")+LangLabel.getValue( prof.cur_lan,"lab_syn_success") +".");
			} else { // 添加
				DbUserGrade userGrade = new DbUserGrade();
				userGrade.ent_syn_ind = true;
				userGrade.ent_syn_date = now;
				userGrade.ent_ste_uid = code;
				userGrade.ugr_code = code;
				userGrade.ugr_display_bil = title;
				userGrade.ugr_ent_id_root = DEFAULT_GROUP_ROOT;
				userGrade.ugr_tcr_id = prof.my_top_tc_id;
				userGrade.ins(con, parentGardeId, prof.usr_id);
				ent_id = userGrade.ugr_ent_id;
				logSuccess(LangLabel.getValue( prof.cur_lan,"line_number") + user.lineno + "  "+LangLabel.getValue( prof.cur_lan,"lab_grade")+"[" + code + "]"+LangLabel.getValue( prof.cur_lan,"add_grade")+LangLabel.getValue( prof.cur_lan,"lab_syn_success") +".");
			}*/
			
			/**
			 * 旧的，只判断一个用户ID，没有就抛出异常
			 */
			if (ent_id > 0) {
				//logSuccess(LangLabel.getValue( prof.cur_lan,"line_number") + user.lineno + "  "+LangLabel.getValue( prof.cur_lan,"lab_grade")+"[" + code + "]"+LangLabel.getValue( prof.cur_lan,"add_grade")+LangLabel.getValue( prof.cur_lan,"lab_syn_success") +".");
			}else{
				user.valid = false;
				throw new Exception(LangLabel.getValue( prof.cur_lan,"lab_grade")+"："+code+", "+LangLabel.getValue( prof.cur_lan,"lab_not_garde")+".");
			}

			synGardeMap.put(code, new Long(ent_id));
			con.commit();
		} catch (Exception e) {
			user.valid=false;
			logFailure(LangLabel.getValue( prof.cur_lan,"line_number")+user.lineno+"  "+LangLabel.getValue( prof.cur_lan,"import_grade_err") +" " + e.getMessage());
			CommonLog.error(e.getMessage(),e);
			rollback(con);
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return ent_id;
	}

	/**
	 * 赋值
	 */
	public ImportObject putField(String[] value, ImportObject record_, int colIdx,loginProfile prof) {

		UserBean record = (UserBean) record_;
		for (int i = 0; i < fieldLabel.length; i++) {
			try {
				Field[] name = record.getClass().getDeclaredFields();
				for (Field fied : name) {
					String parm = fied.getName();
					if (parm.equals(fieldLabel[i])) {
						parm = parm.substring(0, 1).toUpperCase() + parm.substring(1);
						String type = fied.getGenericType().toString();
						if (type.equals("class java.lang.String")) {
							Method m = record.getClass().getMethod("set" + parm, String.class);
							m.invoke(record, value[i]);
						}
						if (type.equals("class java.sql.Timestamp")) {
							Method m = record.getClass().getMethod("set" + parm, Timestamp.class);
							if(value[i] != null){
								m.invoke(record,getTimestamp(value[i]));
							}
						}

					}
				}
			} catch (Exception e) {
				CommonLog.error(e.getMessage(),e);
				logFailure(LangLabel.getValue(prof.cur_lan,"line_number" ) + colIdx +"  "+ LangLabel.getValue( prof.cur_lan,"pack_data_err")+"."  + e.getMessage());
			}

		}

		return record;
	}

	public ImportObject getNewRecordBean() {
		ImportObject record = new UserBean();
		return record;
	}
	
	
	/**
	 * 查找用户是否存在，返回用户ID
	 * @param userMap
	 * @param usr_id(登录名)
	 * @return
	 * @throws Exception
	 */
	private long findUserByUsrId(Map<String, Long> userMap, String usr_id) throws Exception {
		long usr_ent_id = 0;
		if (userMap.get(usr_id) == null)
			usr_ent_id = dbRegUser.getEntIdByUsrId(con, usr_id);
		else
			usr_ent_id = userMap.get(usr_id);
		return usr_ent_id;
	}
	
	/**
	 * 查找用户组是否存在
	 * @param groupMap
	 * @param code(用户组编号)
	 * @return
	 * @throws Exception
	 */
	public static long findGroupByCode(Map<String, Long> groupMap, String code) throws Exception {
		ResultSet rs = null;
		PreparedStatement stmt = null;
		long ent_id = 0;
		try{
			if (groupMap.get(code) == null) {
				String sql = " select ent_id, usg_display_bil from entity,usergroup where ent_id = usg_ent_id and ent_type = 'USG' and ent_ste_uid = ? and ent_delete_usr_id IS NULL";
				stmt = con.prepareStatement(sql);
				stmt.setString(1, String.valueOf(code));
				rs = stmt.executeQuery();
				if (rs.next()) {
					ent_id = rs.getLong("ent_id");
				}
			} else
				ent_id = groupMap.get(code);
		}finally{
			cwSQL.cleanUp(rs, stmt);
		}
		return ent_id;
	}
	
	/**
	 * 查找用户职级是否存在
	 * @param code
	 * @return
	 * @throws SQLException
	 */
	public static long findGradeByCode(String code) throws SQLException{
		ResultSet rs = null;
		PreparedStatement stmt = null;
		long ent_id = 0;
		try{
			String sql = " select ent_id, ugr_display_bil from entity inner join userGrade on ent_id = ugr_ent_id where ugr_code = ? ";
			stmt = con.prepareStatement(sql);
			stmt.setString(1, code);
			rs = stmt.executeQuery();
			if (rs.next()) {
				ent_id = rs.getLong("ent_id");
			}
		}finally{
			cwSQL.cleanUp(rs, stmt);
		}
		return ent_id;
	}

}
