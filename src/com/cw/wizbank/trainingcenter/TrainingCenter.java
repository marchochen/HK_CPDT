/*
 * Created on 2004-9-24
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.cw.wizbank.trainingcenter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.xml.sax.SAXException;

import com.cw.wizbank.JsonMod.commonBean.TCBean;
import com.cw.wizbank.JsonMod.eip.EIPEmptyData;
import com.cw.wizbank.JsonMod.eip.dao.EnterpriseInfoPortalDao;
import com.cw.wizbank.JsonMod.tcrTemplate.TcrTemplateLogic;
import com.cw.wizbank.accesscontrol.AcTrainingCenter;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.cert.Certificate;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.db.DbTcRelation;
import com.cw.wizbank.db.DbTrainOfficer;
import com.cw.wizbank.db.DbTrainTargetEntity;
import com.cw.wizbank.db.DbTrainingCenter;
import com.cw.wizbank.db.sql.SqlStatements;
import com.cw.wizbank.db.view.ViewTrainingCenter;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.tree.cwTree;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwPagination;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.exception.cwMessageException;
import com.cwn.wizbank.security.AclFunction;

/**
 * @author donaldl
 * 
 *         To change the template for this generated type comment go to Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class TrainingCenter {
	private static final String tc_list = "training_center_list";
	private static final String tc = "training_center";
	public static final String ins_mod = "INS";
	public static final String upd_mod = "UPD";
	private static final int viewAll = 1;
	private static final int viewEff = 0;
	public final static String SMSG_NO_TCR_MSG = "TC002";
	public final static String EXCEED_USG_SCOPE_MSG = "TC003";
	public final static String LESSTHAN_CHILD_USG_SCOPE_MSG = "TC004";
	public final static String HAS_CHILD_TC_MSG = "TC005";
	public final static String HAS_RESOURCE_MSG = "TC006";
	public final static String HAS_KB_CATALOG_MSG = "TC020";
	public final static String HAS_KB_TAG_MSG = "TC021";
	public final static String NO_PERMISSION_MSG = "ACL002";
	public final static String TCR_OCCUPANCY_MSG = "EIP008";

	private static final String[] style_indicator = new String[] { "wzb-header", "wzb-footer" };
	private static final String[] style_clsname_keys = new String[] { "banner_image_cls_name", "footer_image_cls_name" };

	private static final String semicolon = ";";
	private static final String BG_CSS_URL_LEFT = "url(";
	private static final String BG_CSS_URL_RIGHT = ")";
	private static final String STYLE_PATH_KEY = "default_style_ind";
	private final TcrTemplateLogic tcrTemplateLogic = TcrTemplateLogic.getInstance();

	public String getEffTcLst(Connection con, long ste_id, WizbiniLoader wizbini) {
		StringBuffer sb = new StringBuffer(512);
		List list = ViewTrainingCenter.getEffTrainingCenters(con, ste_id,  wizbini);
		sb.append(getTrainingCenterLstXml(con, list, viewEff));
		return sb.toString();
	}

	public String getAllTcrLst(Connection con, long ste_id) {
		StringBuffer sb = new StringBuffer(512);
		List list = ViewTrainingCenter.getAllTcLst(con, ste_id);
		sb.append(getTrainingCenterLstXml(con, list, viewAll));
		return sb.toString();
	}

	public String getTcDetailData(Connection con, DbTrainingCenter dbTc, loginProfile prof, TrainingCenterReqParam urlp) throws cwSysMessage, SQLException, cwException {
		boolean isSuperTc = false;
		boolean isSupOrTopLevelTc = false;
		Vector tcList = null;
		long sup_tcr_id = DbTrainingCenter.getSuperTcId(con, prof.root_ent_id);
		if (dbTc == null || dbTc.getTcr_id() == 0) {
			dbTc = new DbTrainingCenter();
			dbTc.setTcr_ste_ent_id(prof.root_ent_id);
			dbTc.tcr_id = ViewTrainingCenter.getDefaultTc(con, prof);
			if(AccessControlWZB.isRoleTcInd(prof.current_role)){
				if (dbTc.tcr_id == 0) {
					throw new cwSysMessage(SMSG_NO_TCR_MSG);
				}
			}else{
				dbTc.tcr_id = prof.my_top_tc_id;
			}

		}
		dbTc.fillData(con);
		StringBuffer sb = new StringBuffer(128);
		if (dbTc.getTcr_id() == sup_tcr_id) {
			isSuperTc = true;
			isSupOrTopLevelTc = true;
		} else {
			if(AccessControlWZB.isRoleTcInd(prof.current_role)){
				if (tcList == null) {
					tcList = ViewTrainingCenter.getTopLevelTCList(con, prof);
				}
				for (int i = 0; i < tcList.size(); i++) {
					if (dbTc.tcr_id == ((Long) tcList.elementAt(i)).longValue()) {
						isSupOrTopLevelTc = true;
					}
				}
			}
		}
		cwTree tree = new cwTree();
		sb.append(tree.genNavTrainingCenterTree(con, prof, false));
		sb.append("<acControl hasUpdBtn=\"").append(hasUpdBtn(con, prof, isSupOrTopLevelTc)).append("\" hasSetAdmBtn=\"").append(hasSetAdmBtn(con, prof, isSupOrTopLevelTc)).append("\" hasTempBtn=\"")
				.append(hasTempBtn(con, prof)).append("\" hasDelBtn=\"").append(!isSupOrTopLevelTc).append("\"/>");
		sb.append("<").append(tc).append(" ");
		sb.append("id=\"").append(dbTc.getTcr_id()).append("\" code=\"").append(cwUtils.esc4XML(dbTc.getTcr_code())).append("\" user_mgt_ind=\"").append(dbTc.tcr_user_mgt_ind).append("\" is_super=\"").append(isSuperTc)
				.append("\">");
		if (!isSuperTc) {
			long parent_tcr_id = DbTrainingCenter.getParentTcId(con, dbTc.getTcr_id());
			sb.append("<parent_tcr id=\"").append(parent_tcr_id).append("\"/>");
		}

		sb.append(getNavigatorAsXML(con, dbTc.tcr_id, prof));

		sb.append(dbTc.objDetailXml(con));

		Map roles = tcAdminRole(con, dbTc.getTcr_ste_ent_id());
		List officers = dbTc.getRelate_officer(con);
		sb.append(getOfficersXml(roles, officers));

		List entity_group = dbTc.getRelate_entity_group(con);
		sb.append(getTargetEntityXml(entity_group));

		sb.append(tcAdminRoleXml(roles));

		if (!urlp.is_tc_upd) {
			Vector child_tc_lst = dbTc.getChildTcList(con);
			sb.append(childTcLstXML(child_tc_lst));
		}

		sb.append("</").append(tc).append(">");
		return sb.toString();
	}

	public String getTcInsPrep(Connection con, long root_ent_id, DbTrainingCenter dbTc, boolean getOfficer) throws SQLException {
		dbTc.fillData(con);
		Map roles = tcAdminRole(con, root_ent_id);
		StringBuffer sb = new StringBuffer(128);
		sb.append("<").append(tc).append(" id=\"").append(dbTc.tcr_id).append("\" user_mgt_ind=\"").append(dbTc.tcr_user_mgt_ind).append("\">");
		sb.append("<parent_tcr id=\"").append(dbTc.tcr_id).append("\"/>");
		if (getOfficer) {
			List officers = dbTc.getRelate_officer(con);
			sb.append(getOfficersXml(roles, officers));
			sb.append(dbTc.objDetailXml(con));
		}
		sb.append(tcAdminRoleXml(roles));
		sb.append("</").append(tc).append(">");
		return sb.toString();
	}

	public DbTrainingCenter addNewTc(Connection con, TrainingCenterReqParam urlp) throws cwSysMessage, SQLException {
		DbTrainingCenter obj = urlp.getObj();
		Timestamp cur_time = cwSQL.getTime(con);
		// if the code has been existed,throw exception.
		ViewTrainingCenter.isDupCode(con, obj.getTcr_code(), obj.getTcr_ste_ent_id());
		Map availableRoles = tcAdminRole(con, obj.getTcr_ste_ent_id());
		Map pageData = urlp.getOfficers();
		long[] t_group = urlp.getTarget_group();
		long sup_tcr_id = DbTrainingCenter.getSuperTcId(con, obj.getTcr_ste_ent_id());
		if ((t_group != null) && (t_group.length != 0) && (obj.parent_tcr_id != sup_tcr_id) && ViewTrainingCenter.chkExceedParentTcUsgScope(con, obj.parent_tcr_id, obj.getTcr_ste_ent_id(), t_group)) {
			throw new cwSysMessage(EXCEED_USG_SCOPE_MSG);
		}
		// insert tc
		obj.setTcr_status(DbTrainingCenter.STATUS_OK);
		obj.dbstore(con, cur_time);
		// insert tcRelation
		if (obj.parent_tcr_id != 0) {
			DbTcRelation.insert(con, obj.parent_tcr_id, obj.getTcr_id(), obj.getTcr_create_usr_id(), cur_time);
		}
		// insert data "officers"
		insRelateOfficer(con, obj, availableRoles, pageData);
		// insert data "target group"
		if (t_group != null && t_group.length != 0) {
			obj.storeTargetEntity(con, t_group);
		}
		tcrTemplateLogic.initTcrTemplate(con);
		return obj;
	}

	public String updTc(Connection con, TrainingCenterReqParam urlp, boolean checkSoleItem) throws cwSysMessage, SQLException, SAXException, IOException {
		DbTrainingCenter obj = urlp.getObj();
		DbTrainingCenter dbData = DbTrainingCenter.getInstance(con, obj.getTcr_id());
		if (!obj.getTcr_code().equalsIgnoreCase(dbData.getTcr_code())) {
			ViewTrainingCenter.isDupCode(con, obj.getTcr_code(), obj.getTcr_ste_ent_id());
		}

		Map availableRoles = tcAdminRole(con, obj.getTcr_ste_ent_id());
		Map pageData = urlp.getOfficers();

		// check update timestamp
		if (!obj.equalData(dbData)) {
			throw new cwSysMessage(TrainingCenterModule.SMSG_UPDED_MSG);
		}

		long[] t_group = urlp.getTarget_group();

		long sup_tcr_id = DbTrainingCenter.getSuperTcId(con, obj.getTcr_ste_ent_id());
		if (obj.tcr_id != sup_tcr_id) {
			if ((obj.parent_tcr_id != sup_tcr_id) && ViewTrainingCenter.chkExceedParentTcUsgScope(con, obj.parent_tcr_id, obj.getTcr_ste_ent_id(), t_group)) {
				throw new cwSysMessage(EXCEED_USG_SCOPE_MSG);
			} else if (ViewTrainingCenter.chkLessThanChildTcUsgScope(con, obj.tcr_id, obj.getTcr_ste_ent_id(), t_group)) {
				throw new cwSysMessage(LESSTHAN_CHILD_USG_SCOPE_MSG);
			}
		}
		// check if the update on training center officer will result in
		// items have no officer
		if (checkSoleItem) {
			Set keySet = availableRoles.keySet();
			StringBuffer roleBuf = new StringBuffer();
			for (Iterator iter = keySet.iterator(); iter.hasNext();) {
				// get officer role xml
				String rol_ext_id = iter.next().toString();
				roleBuf.append((String) availableRoles.get(rol_ext_id));
				long[] usr_ent_id_lst = (long[]) pageData.get(rol_ext_id);
				Vector vSoleItem = ViewTrainingCenter.getSoleOfficerItem(con, obj.getTcr_id(), usr_ent_id_lst, rol_ext_id);
				if (vSoleItem != null) {
					StringBuffer result = new StringBuffer();
					for (int i = 0; i < vSoleItem.size(); i++) {
						//result.append("<li>").append((String) vSoleItem.elementAt(i)).append("</li>");
						result.append((String) vSoleItem.elementAt(i));
						if(i < vSoleItem.size()-1){
							result.append(", ");
						}
					}
					return result.toString();
				}
				ViewTrainingCenter.delRelateItemByUsr(con, obj.getTcr_id(), usr_ent_id_lst, rol_ext_id);
			}
		}

		// start to upd data
		obj.dbupdate(con);

		insRelateOfficer(con, obj, availableRoles, pageData);
		if (obj.tcr_id != sup_tcr_id) {
			obj.delRelateEntiry(con);
			if (t_group != null && t_group.length != 0) {
				obj.storeTargetEntity(con, t_group);
			}
		}
		return null;
	}

	public void deleteTcData(Connection con, DbTrainingCenter dbTc, WizbiniLoader wizbini, loginProfile prof) throws qdbException, cwSysMessage, SQLException, cwException, qdbErrMessage, cwMessageException {
		List<String> label_list = new ArrayList<String>();
		boolean flag = false;
		label_list.add("label_core_system_setting_217");
		
		//子培训中心
		if (DbTrainingCenter.chkHasChildTc(con, dbTc.getTcr_id())) {
			throw new cwSysMessage(HAS_CHILD_TC_MSG);
		}
		//检查培训中心是否与企业关联
		if(EnterpriseInfoPortalDao.checkTcrOccupancy(con, dbTc.getTcr_id())){
			throw new cwSysMessage(TCR_OCCUPANCY_MSG);
		}
		
		//公告
		if (ViewTrainingCenter.hasRelateMessage(con, dbTc.getTcr_id())){
			label_list.add("label_core_information_management_6");
			flag = true;
		}
		//资讯
		if (ViewTrainingCenter.hasRelateArticle(con, dbTc.getTcr_id())){
			label_list.add("menu_article");
			flag = true;
		}
		//检查是否有投票
		if (DbTrainingCenter.chkHasVoting(con, dbTc.getTcr_id())) {
			label_list.add("label_core_requirements_management_2");
			flag = true;
		}
		//公共调查问卷
		if (ViewTrainingCenter.hasRelateEvaluation(con, dbTc.getTcr_id())){
			label_list.add("public_evaluation");
			flag = true;
		}
		//课程/考试/公开课
		if (ViewTrainingCenter.hasRelateItem(con, dbTc.getTcr_id())){
			label_list.add("label_core_system_setting_218");
			flag = true;
		}
		//课程评估问卷
		if (ViewTrainingCenter.hasRelateCosEvaluation(con, dbTc.getTcr_id())){
			label_list.add("lab_mod");
			flag = true;
		}
		//目录管理
		if (ViewTrainingCenter.hasRelateCatalog(con, dbTc.getTcr_id())){
			label_list.add("lab_kb_menu_catalog");
			flag = true;
		}
		//资源目录
		if (ViewTrainingCenter.hasRelateObjective(con, dbTc.getTcr_id())){
			label_list.add("label_core_system_setting_220");
			flag = true;
		}
		//学习小组（已去掉该功能）
		if (ViewTrainingCenter.hasRelateStudyGroup(con, dbTc.getTcr_id())) {
			label_list.add("lab_ftn_STUDY_GROUP_VIEW");
			flag = true;
		}
		//知识目录
		if (DbTrainingCenter.chkHasKBcatalog(con, dbTc.getTcr_id())) {
			label_list.add("label_core_system_setting_65");
			flag = true;
		}
		//知识标签
		if (DbTrainingCenter.chkHasTag(con, dbTc.getTcr_id())) {
			label_list.add("label_core_system_setting_66");
			flag = true;
		}
		//直播
		if (DbTrainingCenter.chkHasLive(con, dbTc.getTcr_id())) {
			label_list.add("label_live");
			flag = true;
		}
		if(flag){
			label_list.add("label_core_system_setting_219");
			throw new cwMessageException(label_list);
		}
		
		// delete all template and tcrTemplateModule of this tcr
		tcrTemplateLogic.delete_tcr_template(con, dbTc.getTcr_id(), HAS_RESOURCE_MSG);

		// del relate data of officers and target_group.
		dbTc.delRelateEntiry(con);
		dbTc.delRelateOfficer(con);
		// del tcRelation
		dbTc.delTcRelation(con);
		//解除关系后,执行检查并清空
		EIPEmptyData tcEmptyData = new EIPEmptyData();
		tcEmptyData.emptyTcrData(con, wizbini, prof, dbTc.getTcr_id());
		dbTc.delete(con, DbTrainingCenter.DEL);
	}

	public String tcRelateOfficers(Connection con, TrainingCenterReqParam urlp, long ste_id) {
		cwPagination page = urlp.cwPage;
		if (page.sortCol == null || page.sortCol.length() == 0) {
			page.sortCol = "usr_ste_usr_id";
		}

		if (page.sortOrder == null || page.sortOrder.length() == 0) {
			page.sortOrder = "asc";
		}
		if (page.pageSize <= 0) {
			page.pageSize = Integer.MAX_VALUE;
		}
		if (page.curPage <= 0) {
			page.curPage = 1;
		}
		StringBuffer sb = new StringBuffer(256);
		List list = ViewTrainingCenter.getRelateOfficerInfo(con, urlp, ste_id);
		sb.append("<group_member_list>");
		for (int i = 0, n = list.size(); i < n; i++) {
			sb.append(ViewTrainingCenter.getSingleOfficerXml(list.get(i)));
		}
		sb.append(page.asXML());
		sb.append("</group_member_list>");

		return sb.toString();
	}

	
	

	private void insRelateOfficer(Connection con, DbTrainingCenter obj, Map availRoles, Map pageData) {
		Set keySet = availRoles.keySet();
		for (Iterator iter = keySet.iterator(); iter.hasNext();) {
			String key = iter.next().toString();
			long[] tmp_lst = (long[]) pageData.get(key);
			obj.delRelateOfficer(con, key);
			if (tmp_lst != null) {
				obj.storeRelateOfficer(con, tmp_lst, key);
			}
		}
	}


	private String getTrainingCenterLstXml(Connection con, List list, int viewMode) {
		StringBuffer sb = new StringBuffer(128);
		DbTrainingCenter obj = null;
		sb.append("<").append(tc_list).append(" view_mode=\"").append(viewMode).append("\">");
		for (int i = 0, n = list.size(); i < n; i++) {
			obj = (DbTrainingCenter) list.get(i);
			sb.append(obj.obj2Xml(con, true));
		}
		sb.append("</").append(tc_list).append(">");
		return sb.toString();
	}

	private String getOfficersXml(Map map, List list) {
		StringBuffer sb = new StringBuffer(128);
		Set keySet = map.keySet();
		String key = null;
		List tmpList = null;
		sb.append("<officer_list>");
		if (list != null) {
			Map classified = classifyRoles(list);
			for (Iterator iter = keySet.iterator(); iter.hasNext();) {
				key = iter.next().toString();
				tmpList = (List) classified.get(key);
				if (tmpList != null) {
					sb.append(getOneRoleXml(tmpList, key));
				}
			}
		}
		sb.append("</officer_list>");
		return sb.toString();
	}

	private Map classifyRoles(List list) {
		Map map = new HashMap();
		String key = null;
		List tmpList = null;
		DbTrainOfficer tmpObj = null;
		for (int i = 0, n = list.size(); i < n; i++) {
			tmpObj = (DbTrainOfficer) list.get(i);
			if ((tmpList = (List) map.get(tmpObj.getTco_rol_ext_id())) != null) {
				tmpList.add(tmpObj);
			} else {
				tmpList = new ArrayList();
				tmpList.add(tmpObj);
				map.put(tmpObj.getTco_rol_ext_id(), tmpList);
			}
		}
		return map;
	}

	private String getOneRoleXml(List list, String rol) {
		StringBuffer sb = new StringBuffer(64);
		DbTrainOfficer obj = null;
		sb.append("<role id=\"").append(rol).append("\">");
		for (int i = 0, n = list.size(); i < n; i++) {
			obj = (DbTrainOfficer) list.get(i);
			sb.append(obj.obj2SimpleXml());
		}
		sb.append("</role>");
		return sb.toString();
	}

	private String getTargetEntityXml(List list) {
		StringBuffer sb = new StringBuffer(128);
		DbTrainTargetEntity obj = null;
		sb.append("<target_entity_list>");
		for (int i = 0, n = list.size(); i < n; i++) {
			obj = (DbTrainTargetEntity) list.get(i);
			sb.append(obj.obj2SimpleXml());
		}
		sb.append("</target_entity_list>");
		return sb.toString();
	}

	private String tcAdminRoleXml(Map map) {
		StringBuffer sb = new StringBuffer(128);
		String key = null;
		sb.append("<role_list>");
		Set keySet = map.keySet();
		for (Iterator iter = keySet.iterator(); iter.hasNext();) {
			key = iter.next().toString();
			sb.append(map.get(key).toString());
		}
		sb.append("</role_list>");
		return sb.toString();
	}

	private Map tcAdminRole(Connection con, long root_ent_id) {
		AccessControlWZB acl = new AccessControlWZB();
		return acl.getTcOfficerRole(con, root_ent_id);
	}

	private String childTcLstXML(Vector child_tc_lst) {
		StringBuffer sb = new StringBuffer();
		sb.append("<child_tc_list>");
		for (int i = 0; i < child_tc_lst.size(); i++) {
			DbTrainingCenter tc = (DbTrainingCenter) child_tc_lst.elementAt(i);
			sb.append("<training_center id=\"").append(tc.tcr_id).append("\">").append("<name>").append(cwUtils.esc4XML(tc.tcr_title)).append("</name>").append("</training_center>");
		}
		sb.append("</child_tc_list>");
		return sb.toString();
	}

	private boolean hasUpdBtn(Connection con, loginProfile prof, boolean isSupOrTopLevelTc) throws SQLException {
		boolean result = false;
		if (AccessControlWZB.hasRolePrivilege( prof.current_role,AclFunction.FTN_AMD_TC_MAIN) && !AccessControlWZB.isRoleTcInd(prof.current_role)) {
			result = true;
		} else if (AccessControlWZB.hasRolePrivilege( prof.current_role,AclFunction.FTN_AMD_TC_MAIN) && !isSupOrTopLevelTc) {
			result = true;
		}
		return result;
	}

	private boolean hasSetAdmBtn(Connection con, loginProfile prof, boolean isSupOrTopLevelTc) throws SQLException {
		boolean result = false;
		if (AccessControlWZB.hasRolePrivilege( prof.current_role,AclFunction.FTN_AMD_TC_MAIN) && isSupOrTopLevelTc) {
			result = true;
		}
		return result;
	}

	private boolean hasTempBtn(Connection con, loginProfile prof) throws SQLException {
		boolean result = false;
		if (AccessControlWZB.hasRolePrivilege( prof.current_role,AclFunction.FTN_AMD_TC_MAIN)) {
			result = true;
		}
		return result;
	}

	public String setTa(Connection con, TrainingCenterReqParam urlp, String usr_id) throws cwSysMessage, SQLException, SAXException, IOException {
		DbTrainingCenter obj = urlp.getObj();
		DbTrainingCenter dbData = DbTrainingCenter.getInstance(con, obj.getTcr_id());
		// check update timestamp
		if (!obj.equalData(dbData)) {
			throw new cwSysMessage(TrainingCenterModule.SMSG_UPDED_MSG);
		}
		Map availableRoles = tcAdminRole(con, obj.getTcr_ste_ent_id());
		Map pageData = urlp.getOfficers();
		// check if the update on training center officer will result in
		// items have no officer
		Set keySet = availableRoles.keySet();
		StringBuffer roleBuf = new StringBuffer();
		for (Iterator iter = keySet.iterator(); iter.hasNext();) {
			// get officer role xml
			String rol_ext_id = iter.next().toString();
			roleBuf.append((String) availableRoles.get(rol_ext_id));
			long[] usr_ent_id_lst = (long[]) pageData.get(rol_ext_id);
			Vector vSoleItem = ViewTrainingCenter.getSoleOfficerItem(con, obj.getTcr_id(), usr_ent_id_lst, rol_ext_id);
			if (vSoleItem != null) {
				StringBuffer result = new StringBuffer();
				for (int i = 0; i < vSoleItem.size(); i++) {
					result.append((String) vSoleItem.elementAt(i));
					if(i < vSoleItem.size()-1){
						result.append("，");
					}
				}
				return result.toString();
			}
			ViewTrainingCenter.delRelateItemByUsr(con, obj.getTcr_id(), usr_ent_id_lst, rol_ext_id);
		}
		Timestamp cur_time = cwSQL.getTime(con);
		DbTrainingCenter.updTimeAndUser(con, obj.getTcr_id(), usr_id, cur_time);
		insRelateOfficer(con, obj, availableRoles, pageData);
		return null;
	}

	private String getNavigatorAsXML(Connection con, long tcr_id, loginProfile prof) throws SQLException {
		StringBuffer navXML = new StringBuffer();
		String ancestor_sql = "select tcr_id, tcr_title from tcTrainingCenter, tcRelation where tcn_child_tcr_id = ? and tcn_ancestor = tcr_id and tcr_status = ? ";
		PreparedStatement stmt = null;
		Vector vtc = new Vector();
		int index = 1;
		stmt = con.prepareStatement(ancestor_sql);
		stmt.setLong(index++, tcr_id);
		stmt.setString(index++, DbTrainingCenter.STATUS_OK);
		ResultSet rs = stmt.executeQuery();
		AcTrainingCenter actc = new AcTrainingCenter(con);
		while (rs.next()) {
			long ancestor_tcr_id = rs.getLong("tcr_id");
			if (actc.canMgtTc(prof.usr_ent_id, prof.current_role, prof.root_ent_id, ancestor_tcr_id)) {
				Map map = new HashMap();
				map.put("tcr_id", new Long(ancestor_tcr_id));
				map.put("tcr_title", rs.getString("tcr_title"));
				vtc.add(map);
			}
		}
		stmt.close();
		navXML.append("<ancestor_node_list>");
		for (int i = 0; i < vtc.size(); i++) {
			HashMap temp = (HashMap) vtc.elementAt(i);
			navXML.append("<node id=\"").append(temp.get("tcr_id")).append("\">");
			navXML.append("<title>").append(dbUtils.esc4XML((String) temp.get("tcr_title"))).append("</title>");
			navXML.append("</node>");
		}
		navXML.append("</ancestor_node_list>");
		return navXML.toString();
	}

	public static String getTcrAsXml(Connection con, long tcr_id, long root_id) throws SQLException {
		String sql = "select tcr_id, tcr_title from tcTrainingCenter where tcr_id = ? and tcr_ste_ent_id = ? ";
		PreparedStatement stmt = null;
		StringBuffer data = new StringBuffer();
		try {
			stmt = con.prepareStatement(sql);
			int index = 1;
			stmt.setLong(index++, tcr_id);
			stmt.setLong(index++, root_id);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				data.append("<cur_training_center id=\"").append(rs.getLong("tcr_id")).append("\">").append("<title>").append(cwUtils.esc4XML(rs.getString("tcr_title"))).append("</title>")
						.append("</cur_training_center>");
			}
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}

		return data.toString();
	}

	/**
	 * 获取用户相关的培训中心
	 * 
	 * @param con
	 * @param usr_ent_id
	 * @return tc Vector
	 * @throws SQLException
	 */
	public Vector getTrainingCenterByTargetUser(Connection con, long usr_ent_id) throws SQLException {
		Vector vc = new Vector();
		PreparedStatement stmt = null;
		try {
			StringBuffer SQLBuf = new StringBuffer();
			SQLBuf.append(SqlStatements.SQL_GET_TCR_BY_TARGET_USR + " order by tcr_title");
			stmt = con.prepareStatement(SQLBuf.toString());
			stmt.setLong(1, usr_ent_id);
			stmt.setString(2, "USR_PARENT_USG");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				TCBean tc = new TCBean();
				tc.setTcr_id(rs.getLong("tcr_id"));
				tc.setTcr_title(rs.getString("tcr_title"));
				tc.setTcr_parent_tcr_id(rs.getInt("tcr_parent_tcr_id"));
				if (rs.getInt("tcr_parent_tcr_id") == 0) { // 若为总培训中心
					vc.add(0, tc); // 则设置成员变量tcr_parent_tcr_id为0，用以判断是否为总培训中心
				} else {
					vc.add(tc);
				}
			}
		} finally {
			if (stmt != null)
				stmt.close();
		}
		return vc;
	}

	public static String getTcStyleXml(Connection con, loginProfile prof, TrainingCenterReqParam urlp, WizbiniLoader wizbini) throws SQLException, cwSysMessage, cwException, IOException {
		StringBuffer xml = new StringBuffer();
		DbTrainingCenter dbTc = new DbTrainingCenter();
		dbTc.tcr_id = urlp.tcr_id;
		dbTc.fillData(con);
		cwTree tree = new cwTree();
		xml.append(tree.genNavTrainingCenterTree(con, prof, false)).append("<").append(tc).append(" id=\"").append(dbTc.tcr_id).append("\" title=\"").append(cwUtils.esc4XML(dbTc.getTcr_title())).append("\">")
				.append("<style_lang>").append(urlp.style_lang).append("</style_lang>");
		Hashtable styleHash = readStyleCss(wizbini, urlp);
		xml.append("<default_style_ind>").append(styleHash.get(STYLE_PATH_KEY)).append("</default_style_ind>");
		Enumeration e = styleHash.keys();
		while (e.hasMoreElements()) {
			String style_indicator = (String) e.nextElement();
			boolean addXml = true;
			for (int i = 0; i < style_clsname_keys.length; i++) {
				if (style_indicator.equals(style_clsname_keys[i])) {
					addXml = false;
					break;
				}
			}
			if (addXml && style_indicator.equals(STYLE_PATH_KEY)) {
				addXml = false;
			}
			if (addXml) {
				xml.append("<").append(style_indicator).append(">");
				Hashtable t = (Hashtable) styleHash.get(style_indicator);
				Enumeration te = t.keys();
				while (te.hasMoreElements()) {
					String sub_style_indicator = (String) te.nextElement();
					xml.append("<").append(sub_style_indicator).append(">");
					String sub_style_value = (String) t.get(sub_style_indicator);
					int leftIndex = sub_style_value.indexOf(BG_CSS_URL_LEFT);
					if (leftIndex != -1) {
						xml.append(sub_style_value.substring(leftIndex + BG_CSS_URL_LEFT.length(), sub_style_value.indexOf(BG_CSS_URL_RIGHT)));
					} else {
						xml.append(sub_style_value);
					}
					xml.append("</").append(sub_style_indicator).append(">");
				}
				xml.append("</").append(style_indicator).append(">");
			}
		}
		TrainingCenter tcObj = new TrainingCenter();
		xml.append(tcObj.getNavigatorAsXML(con, urlp.tcr_id, prof));
		xml.append("</").append(tc).append(">");
		xml.append(getDefaultStyleXml(wizbini));
		return xml.toString();
	}

	private static String getDefaultStyleXml(WizbiniLoader wizbini) throws IOException {
		StringBuffer xml = new StringBuffer();
		xml.append("<default_style>");
		Hashtable defaultStyleHash = readDefaultStyleCss(wizbini);
		Enumeration e = defaultStyleHash.keys();
		while (e.hasMoreElements()) {
			String style_indicator = (String) e.nextElement();
			boolean addXml = true;
			for (int i = 0; i < style_clsname_keys.length; i++) {
				if (style_indicator.equals(style_clsname_keys[i])) {
					addXml = false;
					break;
				}
			}
			if (addXml) {
				xml.append("<").append(style_indicator).append(">");
				Hashtable t = (Hashtable) defaultStyleHash.get(style_indicator);
				Enumeration te = t.keys();
				while (te.hasMoreElements()) {
					String sub_style_indicator = (String) te.nextElement();
					xml.append("<").append(sub_style_indicator).append(">");
					String sub_style_value = (String) t.get(sub_style_indicator);
					int leftIndex = sub_style_value.indexOf(BG_CSS_URL_LEFT);
					if (leftIndex != -1) {
						xml.append(sub_style_value.substring(leftIndex + BG_CSS_URL_LEFT.length(), sub_style_value.indexOf(BG_CSS_URL_RIGHT)));
					} else {
						xml.append(sub_style_value);
					}
					xml.append("</").append(sub_style_indicator).append(">");
				}
				xml.append("</").append(style_indicator).append(">");
			}
		}
		xml.append("</default_style>");
		return xml.toString();
	}

	private static Hashtable readStyleCss(WizbiniLoader wizbini, TrainingCenterReqParam urlp) throws IOException {
		Hashtable styleHash = new Hashtable();
		styleHash.put(STYLE_PATH_KEY, "false");
		String filename = cwUtils.transPathSeperator4Linux(genTcStylePath(wizbini, urlp));
		File f = new File(filename);
		if (!f.exists()) {
			filename = cwUtils.transPathSeperator4Linux(genDefaultStylePath(wizbini));
			styleHash.put(STYLE_PATH_KEY, "true");
		}
		readSpecifiedCss(filename, styleHash);
		return styleHash;
	}

	private static Hashtable readDefaultStyleCss(WizbiniLoader wizbini) throws IOException {
		Hashtable styleHash = new Hashtable();
		String filename = genDefaultStylePath(wizbini);
		readSpecifiedCss(filename, styleHash);
		return styleHash;
	}

	private static void readSpecifiedCss(String filename, Hashtable styleHash) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(filename), cwUtils.ENC_UTF));
		String line = in.readLine();
		while (line != null) {
			for (int i = 0; i < style_indicator.length; i++) {
				if (line.indexOf("." + style_indicator[i]) != -1) {// get css class
					styleHash.put(style_clsname_keys[i], line);// this will be used in write tc css.

					line = in.readLine();
					Hashtable tmp = null;
					while (line.indexOf("}") == -1) {
						if (tmp == null) {
							tmp = new Hashtable();
						}
						if (line.indexOf(":") != -1) {
							String css_key = (line.substring(0, line.indexOf(":"))).trim();
							String css_value = line.substring(line.indexOf(":") + 1, line.length()).trim();
							tmp.put(css_key, css_value);
						}
						line = in.readLine();
					}
					if (tmp != null) {
						styleHash.put(style_indicator[i], tmp);
					}
				}
			}
			line = in.readLine();
		}
		in.close();
	}

	public static void setTcStyle(TrainingCenterReqParam urlp, WizbiniLoader wizbini) throws cwException, IOException {
		if (urlp.tcr_id > 0) {
			String filename = genTcStylePath(wizbini, urlp);
			File file = new File(filename);
			String content = genTcStyleContent(wizbini, urlp);
			FileOutputStream fop = new FileOutputStream(file);
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fop, cwUtils.ENC_UTF));
			out.write(content);
			out.flush();
			out.close();
		} else {
			throw new cwException("invalid training center.");
		}
	}

	public static String genTcStylePath(WizbiniLoader wizbini, TrainingCenterReqParam urlp) {
		String filename = genTcStyleDir(wizbini, urlp);
		File f = new File(filename);
		if (!f.exists()) {
			f.mkdirs();
		}
		filename += cwUtils.SLASH + "tc_style.css";
		return filename;
	}

	private static String genTcStyleDir(WizbiniLoader wizbini, TrainingCenterReqParam urlp) {
		String css_dir = wizbini.getAppnRoot() + cwUtils.SLASH + wizbini.cfgSysSetupadv.getTcStyle().getCssDir();
		String fileDir = css_dir;
		if (!fileDir.endsWith(cwUtils.SLASH)) {
			fileDir += cwUtils.SLASH;
		}
		fileDir += urlp.tcr_id + cwUtils.SLASH + urlp.style_lang;
		return fileDir;
	}

	private static String genDefaultStyleDir(WizbiniLoader wizbini) {
		String path = wizbini.getAppnRoot() + cwUtils.SLASH + wizbini.cfgSysSetupadv.getTcStyle().getCssDir();
		if (!path.endsWith(cwUtils.SLASH)) {
			path += cwUtils.SLASH;
		}
		return path;
	}

	private static String genDefaultStylePath(WizbiniLoader wizbini) {
		String path = genDefaultStyleDir(wizbini);
		path += wizbini.cfgSysSetupadv.getTcStyle().getCssFile();
		return path;
	}

	private static String genTcStyleContent(WizbiniLoader wizbini, TrainingCenterReqParam urlp) throws IOException {
		StringBuffer content = new StringBuffer();
		Hashtable style_hash = readStyleCss(wizbini, urlp);
		String cssValue = "";
		String br = "\r\n";
		if(style_clsname_keys != null && style_hash != null){
			for (int i = 0; i < style_clsname_keys.length; i++) {
				content.append(style_hash.get(style_clsname_keys[i])).append(br);
					if(style_hash.get(style_indicator[i]) != null && ((Hashtable) style_hash.get(style_indicator[i])).get("background-image") != null){
					if (style_clsname_keys[i].equalsIgnoreCase("banner_image_cls_name")) {
						content.append("background-image: ");
						switch (urlp.banner_image) {
						case 0:// Keep Media File.
							cssValue = (String) ((Hashtable) style_hash.get(style_indicator[i])).get("background-image");
							if(cssValue != null){
								if (!imageExist(wizbini, urlp, cssValue)) {
									cssValue = "";
								}
								content.append(cssValue).append(cssValue.endsWith(semicolon) ? "" : semicolon).append(br);
							}
							break;
						case 1:// Use wizBank default.
							content.append(br);
							break;
						case 2:// new uploaded image.
							cssValue = BG_CSS_URL_LEFT + urlp.banner_image_dir + BG_CSS_URL_RIGHT + semicolon;
							if(cssValue != null){
								content.append(cssValue).append(cssValue.endsWith(semicolon) ? "" : semicolon).append(br);
							}
							break;
						}
					} else if (style_clsname_keys[i].equalsIgnoreCase("footer_image_cls_name")) {
						content.append("background-image: ");
						switch (urlp.footer_image) {
						case 0:// Keep Media File.
							cssValue = (String) ((Hashtable) style_hash.get(style_indicator[i])).get("background-image");
							if(cssValue != null){
								content.append(cssValue).append(cssValue.endsWith(semicolon) ? "" : semicolon).append(br);
							}
							break;
						case 1:// Use wizBank default.
							content.append(br);
							break;
						case 2:// new uploaded image.
							cssValue = BG_CSS_URL_LEFT + urlp.footer_image_dir + BG_CSS_URL_RIGHT + semicolon;
							if(cssValue != null){
								content.append(cssValue).append(cssValue.endsWith(semicolon) ? "" : semicolon).append(br);
							}
							break;
						}
					}
				}
				content.append("}").append(br);
			}
		}
	
		return content.toString();
	}

	private static boolean imageExist(WizbiniLoader wizbini, TrainingCenterReqParam urlp, String cssValue) {
		boolean exist = true;
		int leftIndex = cssValue.indexOf(BG_CSS_URL_LEFT);
		int rightIndex = cssValue.indexOf(BG_CSS_URL_RIGHT);
		if (leftIndex != -1 && rightIndex != -1) {
			String imagePath = genTcStyleDir(wizbini, urlp) + cwUtils.SLASH + cssValue.substring(leftIndex + BG_CSS_URL_LEFT.length(), rightIndex);
			File f = new File(imagePath);
			if (!f.exists()) {
				exist = false;
			}
		}
		return exist;
	}
}