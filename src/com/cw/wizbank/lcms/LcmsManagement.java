package com.cw.wizbank.lcms;

import java.io.StringReader;
import java.sql.*;
import java.util.*;

import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.cw.wizbank.JsonMod.Course.bean.ModBean;
import com.cw.wizbank.JsonMod.courseware.Courseware;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.accesscontrol.acSite;
import com.cw.wizbank.ae.aeApplication;
import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.ae.aeQueueManager;
import com.cw.wizbank.ae.aeTreeNode;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.db.DbTrackingHistory;
import com.cw.wizbank.db.view.ViewEntityToTree;
import com.cw.wizbank.db.view.ViewEntityToTree.entityInfo;
import com.cw.wizbank.qdb.dbAiccPath;
import com.cw.wizbank.qdb.dbCourseEvaluation;
import com.cw.wizbank.qdb.dbEntity;
import com.cw.wizbank.qdb.dbMessage;
import com.cw.wizbank.qdb.dbModule;
import com.cw.wizbank.qdb.dbModuleEvaluation;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.dbResource;
import com.cw.wizbank.qdb.dbUserGroup;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbAction;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.tree.cwTree;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwPagination;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.utils.CommonLog;

/**
 * 为了避免重复开发和出现不必要的bug,本类中的大部分方法都是调用平台现有功能中的API
 * @author harveytan
 *
 */
public class LcmsManagement {
	
	/**
	 * 登录
	 * @param con
	 * @param prof当前用户信息，如果登录成功需要赋值(重要)
	 * @param modParam 参数类
	 * @return
	 * @throws SQLException
	 * @throws qdbException
	 */
	public String login(Connection con, loginProfile prof, LcmsModuleParam modParam, HttpSession sess, String encoding) throws SQLException, qdbException {
		String xml;
		dbRegUser regUser = new dbRegUser();
		regUser.getByUsrSteUsrId(con, modParam.site_id, modParam.usr_ste_usr_id);
		String login_code = dbRegUser.CODE_OFF_LOGIN_SUCCESS;
		if(regUser.usr_ent_id <= 0){
			login_code = dbRegUser.CODE_OFF_LOGIN_FAILURE0;
		} else {
			String c_paswd = dbRegUser.encrypt(modParam.usr_pwd, new StringBuffer(regUser.usr_ste_usr_id).reverse().toString());
			if(c_paswd == null || !c_paswd.equals(regUser.usr_pwd)){
				login_code = dbRegUser.CODE_OFF_LOGIN_FAILURE1;
			}
		}
		//这步很重要，登录成功之后把当前用户的相关信息放入prof，并保存到session中，用于调用平台现有API.
    	if(login_code.equals(dbRegUser.CODE_OFF_LOGIN_SUCCESS)){
    		prof.usr_ent_id = regUser.usr_ent_id;
    		prof.usr_id = regUser.usr_id;
    		
    		acSite as = new acSite();
    		as.ste_ent_id = modParam.site_id;
    		as.get(con);
			prof.root_ent_id = as.ste_ent_id;
			prof.root_id = as.ste_id;
			
			prof.current_role = AccessControlWZB.ROL_EXT_ID_NLRN;
			prof.usrGroups = dbUserGroup.traceParentID(con, prof.usr_ent_id, dbEntity.ENT_TYPE_USER);
			prof.usrGrades = dbUserGroup.traceParentID(con, prof.usr_ent_id, dbEntity.ENT_TYPE_USER_GRADE);
			prof.encoding  = encoding;
            sess.setAttribute(qdbAction.AUTH_LOGIN_PROFILE, prof);
            String SID = prof.usr_id + Math.round(Math.random() * 100000) ;
            sess.setAttribute(qdbAction.AUTH_LOGIN_SID, SID);
            prof.label_lan = "GB2312";
            prof.cur_lan = prof.getCurLan(prof.label_lan);
            //prof.writeSession(sess);
    		
    		xml = _genResultXml(true, login_code, null);
    	} else {
    		xml = _genResultXml(false, login_code, null);
    	}
		return xml;
	}
	/**
	 * 公告列表（调用平台首页公告模块现有API实现）
	 * @param con
	 * @param prof
	 * @param tc_enabled系统是否有配置培训中心，这里暂时只考虑下有培训中心的情况
	 * @return
	 * @throws SQLException
	 * @throws qdbException
	 * @throws cwSysMessage
	 */
	public String getNoticeList(Connection con, loginProfile prof, boolean tc_enabled, WizbiniLoader wizbini) throws SQLException, qdbException, cwSysMessage {
		StringBuffer xml = new StringBuffer();
		 //wb_gen_ann
        dbMessage dbmsg = new dbMessage();
        dbmsg.msg_type = dbMessage.MSG_TYPE_SYS;

        PreparedStatement stmt = null;
        ResultSet rs = null;
        if (tc_enabled) {
        	dbmsg.msg_readonly = true;
        	stmt = dbmsg.getAnnForRead(con, prof, " msg_begin_date ", " desc ", null,  wizbini,null);
        	rs = stmt.executeQuery();
        	while (rs.next()) {
            	xml.append("<notice>");
            	xml.append("<title>").append(rs.getString("msg_title")).append("</title>");
            	xml.append("<content>").append("servlet/qdbAction?env=wizb&cmd=get_msg&stylesheet=announ_dtl.xsl&msg_type=SYS&msg_readonly=true&code=lcms&msg_id=").append(rs.getLong("msg_id")).append("</content>");
            	xml.append("</notice>");
        	}
        } else {
        	//暂不考虑无培训中心的情况
        }
        _closeStmtAndRs(stmt, rs);
		return _genResultXml(xml.toString());
	}
	/**
	 * 学员报名（本方法参考自aeAction cmd=ae_direct_ins_appn）
	 * @param con
	 * @param prof
	 * @param modParam : itm_id, usr_ste_usr_id
	 * @return
	 */
	public String enrollCourse(Connection con, loginProfile prof, LcmsModuleParam modParam) {
		String xml = "";
		try {
			long usr_ent_id = dbRegUser.getEntId(con, modParam.usr_ste_usr_id, prof.root_ent_id);
			if (usr_ent_id == prof.usr_ent_id) {
				aeItem item = new aeItem();
				item.itm_id = modParam.itm_id;
				item.getItem(con);
				aeQueueManager qm = new aeQueueManager();
				aeApplication aeApp = null;
				aeApp = qm.insApplication(con, "<detail><field01></field01></detail>", usr_ent_id, modParam.itm_id, prof, 0, 0, 0, 0, null, null, true, 0, null, null);
				
				if (aeApp.app_id > 0) {
					xml = _genResultXml(true, null, null);
				} else {
					xml = _genResultXml(false, null, "error when enroll course : app_id <= 0.");
				}
			}
		} catch (Exception e) {
			CommonLog.error(e.getMessage(),e);
			xml = _genResultXml(false, null, "error when enroll course :  " + e.getMessage());
		}
		return xml;
	}
	/**
	 * 上传用户离线学习状态----信息包含在modParam.status中传递
	 * @param con
	 * @param prof
	 * @param modParam
	 * @return
	 */
	public String uploadStatus(Connection con, loginProfile prof, LcmsModuleParam modParam) {
		String xml = "";
//modParam.status = "<courses><course  id=\"224\" cos_id=\"433\" tkh_id=\"1864\"><item  id=\"434\" status=\"passed\" score=\"100\" last_access=\"2010-10-19 05:42:04\" time_spent=\"0:0:38\"/></course></courses>";
		try {
			if(modParam.status == null || modParam.status.length() == 0){
				xml = _genResultXml(false, null, "status xml is null");
			} else {		
//System.out.println("***********************************************modParam.status="+modParam.status);
				//解析离线助手传过来的学习记录XML字符串
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				StringReader sr = new StringReader(modParam.status);
				InputSource is = new InputSource(sr);
				Document doc = builder.parse(is);
				doc.normalize();
				// 得到所有的course节点
				NodeList courses = doc.getElementsByTagName("course");
				for (int i = 0; i < courses.getLength(); i++) {
					Element course_attr = (Element) courses.item(i);
					long cos_id = 0;
					long tkh_id = 0;
					if (course_attr.getAttribute("cos_id") != null && course_attr.getAttribute("cos_id").length() > 0) {
						cos_id = Long.parseLong(course_attr.getAttribute("cos_id"));
					}
					if (course_attr.getAttribute("tkh_id") != null && course_attr.getAttribute("tkh_id").length() > 0) {
						tkh_id = Long.parseLong(course_attr.getAttribute("tkh_id"));
					}
					if (cos_id > 0 && tkh_id > 0) {
						dbModuleEvaluation dbmov = new dbModuleEvaluation();
						dbmov.mov_tkh_id = tkh_id;
						dbmov.mov_cos_id = cos_id;
						dbmov.mov_ent_id = prof.usr_ent_id;
	
						NodeList items = course_attr.getElementsByTagName("item");
						for (int j = 0; j < items.getLength(); j++) {
							Element item_attr = (Element) items.item(j);
							long id = Long.parseLong(item_attr.getAttribute("id"));
							String last_access = item_attr.getAttribute("last_access");
							String time_spent = item_attr.getAttribute("time_spent");
							String status = item_attr.getAttribute("status");
							String score = item_attr.getAttribute("score");
							if(score == null || score.length() == 0){
								score = "0";
							}
							dbmov.mov_mod_id = id;
							float time_spent_float = 0;
							if(time_spent != null && time_spent.length() > 0){
								if(time_spent.indexOf(":") > -1){
									//if time_spent format hh:mm:ss
									time_spent_float = dbAiccPath.convert2Second(time_spent);
								} else {
									//if time_spent format ssss
									time_spent_float = Float.parseFloat(time_spent);
								}
							}
							dbmov.mod_time = time_spent_float;
							dbmov.mov_last_acc_datetime = (last_access == null || last_access.length() == 0) ? null : Timestamp.valueOf(last_access);
							dbmov.mov_status = (status == null ? "" : status);
							//截取第一个字符并大写
							if(dbmov.mov_status.length() > 1){
								dbmov.mov_status = dbmov.mov_status.substring(0,1);
							}
							dbmov.mov_status = dbmov.mov_status.toUpperCase();
							dbmov.mov_score = score;
							//时间大于0且记录唯一才更新
							if (dbmov.mod_time > 0 && DbTrackingHistory.getAppTrackingIDByCos(con, dbmov.mov_tkh_id, prof.usr_ent_id, dbModule.getCosId(con, dbmov.mov_mod_id), dbmov.mov_mod_id) == 1) {
//System.out.println("********************************************************mov_cos_id="+cos_id +"mov_tkh_id="+tkh_id+"mov_ent_id="+prof.usr_ent_id+"mov_mod_id="+id + " lass_access="+last_access + " time_spent="+time_spent + " status="+status + " score="+score);
								dbmov.save(con, prof, false);
							}
						}
	
					}
				}
				xml = _genResultXml(true, null, null);
			}
		} catch (Exception e) {
			CommonLog.error(e.getMessage(),e);
			xml = _genResultXml(false, null, "error when upload status :  " + e.getMessage());
		}
		return xml;
	}
	/**
	 * 查询用户所能看到的所有课程目录及目录下的课程（调用平台现有课程目录功能模块中的API实现）
	 * @param con
	 * @param prof
	 * @param modParam
	 * @return
	 * @throws SQLException
	 * @throws qdbException
	 * @throws cwException 
	 */
	public String getCourseList(Connection con, loginProfile prof, LcmsModuleParam modParam) throws SQLException, qdbException, cwException {
		StringBuffer xml = new StringBuffer();
		// 找到学员所在的培训中心
		ViewEntityToTree entity2Tree = new ViewEntityToTree();
		Vector my_tcr_lst = entity2Tree.getSuperTcInfo(con, prof, true, false, true, false);
		for (int i = 0; i < my_tcr_lst.size(); i++) {
			entityInfo info = (entityInfo) my_tcr_lst.elementAt(i);
			xml.append(getCourseListByTcr(con, prof, info.ent_id));
		}
		return _genResultXml(xml.toString());
	}
	/**
	 * 查找某培训中心下的学员可见的课程目录及课程
	 * @param con
	 * @param prof
	 * @param tcr_id培训中心ID
	 * @return
	 * @throws SQLException
	 * @throws qdbException
	 * @throws cwException 
	 */
	private String getCourseListByTcr(Connection con, loginProfile prof, long tcr_id) throws SQLException, qdbException, cwException {
		StringBuffer xml = new StringBuffer();
		ViewEntityToTree entity2Tree = new ViewEntityToTree();
		//查找培训中心下的子培训中心和一级目录
		Vector subTcr_cat_lst = entity2Tree.getSubTcAndCatalogInfo(con, tcr_id, prof, false, false, 0);
		for (int j = 0; j < subTcr_cat_lst.size(); j++) {
			entityInfo subTcr_cat_info = (entityInfo) subTcr_cat_lst.elementAt(j);
			if (subTcr_cat_info.node_type.endsWith(cwTree.NODE_TYPE_TC)) {
				//如果是子培训中心则循环查找
				xml.append(getCourseListByTcr(con, prof, subTcr_cat_info.ent_id));
			} else if (subTcr_cat_info.node_type.endsWith(cwTree.NODE_TYPE_CATALOG)) {
				//如果是一级目录则查找该目录下的子目录和课程
				xml.append(getCourseListByNode(con, prof, subTcr_cat_info));
			}
		}
		return xml.toString();
	}
	/**
	 * 查找某课程目录下的学员可见的子课程目录及课程
	 * @param con
	 * @param prof
	 * @param info
	 * @return
	 * @throws SQLException
	 * @throws qdbException
	 * @throws cwException 
	 */
	private String getCourseListByNode(Connection con, loginProfile prof, entityInfo info) throws SQLException, qdbException, cwException {
		StringBuffer nodeXml = new StringBuffer();
		nodeXml.append("<folder name=\"").append(info.title).append("\">");
		//查找子目录
		ViewEntityToTree entity2Tree = new ViewEntityToTree();
		Vector subNode_lst = entity2Tree.getChildAndItemNode(con, info.ent_id, prof, false, false, false, false, false);
		//循环查找子目录的子目录及其下的课程
		for (int j = 0; j < subNode_lst.size(); j++) {
			entityInfo subNode_info = (entityInfo) subNode_lst.elementAt(j);
			nodeXml.append(getCourseListByNode(con, prof, subNode_info));
		}
		//查找目录下学员可见的课程
		aeTreeNode tree_node = new aeTreeNode();
		tree_node.tnd_id = info.ent_id;
		cwPagination page = new cwPagination();
		PreparedStatement stmt = tree_node.getItemNodesAsXML_stmt(con, true, null, page, prof.usr_ent_id, true);
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			String itm_type = rs.getString("itm_type");
			//只有网络课程才可以上传离线学习内容，所以暂时只显示网络课程(后续会改进到面授，混合课程)
			if (itm_type.equals(aeItem.ITM_TYPE_SELFSTUDY)) {
				String offline_pkg = rs.getString("itm_offline_pkg");
				// 没有离线课件包的课程不显示
				if (offline_pkg != null && offline_pkg.length() > 0) {
					long itm_id = rs.getLong("tnd_itm_id");
					Vector sco_aicc_id_lst = _getCosScoAiccResId(con, itm_id);
					// 没有离线内容的课程不显示（这个条件和上面的条件一致）
					if (sco_aicc_id_lst.size() > 0) {
						StringBuffer courseXml = new StringBuffer();
						// 离线内容下载路径
						if (offline_pkg != null && offline_pkg.length() > 0) {
							offline_pkg = Courseware.FILE_UPLOAD_TYPE_CP + "/" + itm_id + "/" + offline_pkg;
						} else {
							offline_pkg = "";
						}
						courseXml.append("<course id=\"").append(itm_id);
						courseXml.append("\" name=\"").append(rs.getString("tnd_title"));
						courseXml.append("\" approval_required=\"").append(cwUtils.escNull(rs.getString("itm_app_approval_type")));// 审批类型
						courseXml.append("\" path=\"").append(offline_pkg);
						// 查看当前学员是否有报名(不管成功与否)
						long latest_app_id = aeApplication.getLatestApplicationId(con, itm_id, prof.usr_ent_id);
						if (latest_app_id > 0) {// 学员有报名
							aeApplication app = new aeApplication();
							app.app_id = latest_app_id;
							app.get(con);
							// 有成功报名
							if (app.app_tkh_id > 0 && app.app_status.equals(aeApplication.ADMITTED)) {
								courseXml.append("\" status=\"").append(aeApplication.ADMITTED);
								Vector itm_lst = new Vector();
								itm_lst.addElement(new Long(itm_id));
								// 查找学习记录
								Hashtable item_evaluation = dbCourseEvaluation.getCourseEvaluation(con, prof.usr_ent_id, itm_lst);
								// 没有报名的课程不显示离线内容
								if (item_evaluation != null && item_evaluation.get(new Long(itm_id)) != null) {
									dbCourseEvaluation dbCosEval = (dbCourseEvaluation) item_evaluation.get(new Long(itm_id));
									if (app.app_tkh_id == dbCosEval.cov_tkh_id) {// 验证两个tkh_id是否一致，不一致则提示错误
										courseXml.append("\" cos_id=\"").append(dbCosEval.cov_cos_id);
										courseXml.append("\" tkh_id=\"").append(dbCosEval.cov_tkh_id);
										String last_acc_date_time = _formatTime(cwUtils.escNull(dbCosEval.cov_last_acc_datetime));
										courseXml.append("\" last_access=\"").append(last_acc_date_time);// 上次访问时间
										String total_time = _formatTime(dbAiccPath.getTime(dbCosEval.cov_total_time));
										courseXml.append("\" time_spent=\"").append(total_time);// 累计时长
										courseXml.append("\">");
										courseXml.append(getContentItem(con, prof, dbCosEval.cov_cos_id, dbCosEval.cov_tkh_id, sco_aicc_id_lst));
									} else {
										CommonLog.error("Get dbCourseEvaluation error : " + app.app_tkh_id + "!=" + dbCosEval.cov_tkh_id);
									}
								}
							} else {// 没有成功报名
								courseXml.append("\" status=\"").append(app.app_status);
								courseXml.append("\">");
							}
						} else {// 学员没有报名
							courseXml.append("\" status=\"").append("");
							courseXml.append("\" tkh_id=\"").append(0);
							courseXml.append("\">");
						}

						courseXml.append("</course>");
						nodeXml.append(courseXml);
					}
				}
			}
		}
		_closeStmtAndRs(stmt, rs);
		nodeXml.append("</folder>");
		return nodeXml.toString();
	}
	/**
	 * 去掉时间字符串后面小数点及后面的数字
	 * @param time
	 * @return
	 */
	private String _formatTime(String time){
		if(time != null && time.lastIndexOf(".") > -1){
			time = time.substring(0, time.lastIndexOf("."));
		}
		return time;
	}
	/**
	 * 查找离线内容（scorm, aicc）的相关信息
	 * @param con
	 * @param prof
	 * @param cov_cos_id课程cos_id
	 * @param cov_tkh_id学员跟踪id
	 * @param sco_aicc_id_lst离线内容id
	 * @return
	 * @throws SQLException
	 * @throws qdbException
	 * @throws cwException 
	 */
	public String getContentItem(Connection con, loginProfile prof, long cov_cos_id, long cov_tkh_id, Vector sco_aicc_id_lst) throws SQLException, qdbException, cwException {

		StringBuffer cosXml = new StringBuffer();
		dbResource resource = new dbResource();
		resource.res_id = cov_cos_id;
		resource.tkh_id = cov_tkh_id;
		Vector modList = resource.getCosContentList(con, prof, null, true, sco_aicc_id_lst, new Hashtable(), new Hashtable(), null, null,null, false);
		for (int i = 0; i < modList.size(); i++) {
			ModBean mod = (ModBean)modList.elementAt(i);
			StringBuffer xmlBody = new StringBuffer();
			
			xmlBody.append("<item id=\"").append(mod.getId()).append("\" type=\"").append(mod.getType()).append("\" subtype=\"").append(mod.getSubtype());
			xmlBody.append("\" name=\"").append(mod.getTitle());//课件名称
			
			String last_acc_date_time = _formatTime(cwUtils.escNull(mod.getAicc_data().getLast_acc_datetime()));
			xmlBody.append("\" last_access=\"").append(last_acc_date_time);//上次访问时间

			String total_time = _formatTime(mod.getAicc_data().getUsed_time());
			xmlBody.append("\" time_spent=\"").append(total_time);//累计时长
			xmlBody.append("\" status=\"").append(cwUtils.escNull(mod.getAicc_data().getStatus()));//状态
			xmlBody.append("\" score=\"").append(mod.getAicc_data().getScore());//分数
			
			xmlBody.append("\"/>").append(dbUtils.NEWL);
			
			cosXml.append(xmlBody);
		}
		
		return cosXml.toString();
	}
	/**
	 * 查询课程中发布的离线内容课程（sco, aicc_au）
	 * @param con
	 * @param itm_id
	 * @return
	 * @throws SQLException
	 */
	public Vector _getCosScoAiccResId(Connection con, long itm_id) throws SQLException{
		Vector mod_id_lst = new Vector();
		StringBuffer sql = new StringBuffer();
		sql.append("select resources.res_id")
			.append(" from resources")
			.append(" inner join resourcecontent on rcn_res_id_content = res_id")
			.append(" inner join course on cos_res_id = rcn_res_id")
			.append(" where cos_itm_id = ?")
			.append(" and res_type = ? and (res_subtype=? or res_subtype=?) and res_status=?");
		
		PreparedStatement stmt = con.prepareStatement(sql.toString());
		int index = 1;
		stmt.setLong(index++, itm_id);
		stmt.setString(index++, dbResource.RES_TYPE_MOD);
		stmt.setString(index++, "SCO");
		stmt.setString(index++, "AICC_AU");
		stmt.setString(index++, dbResource.RES_STATUS_ON);
		ResultSet rs = stmt.executeQuery();
		while(rs.next()){
			mod_id_lst.add(new Long(rs.getLong("res_id")));
		}
		_closeStmtAndRs(stmt, rs);
		return mod_id_lst;
	}
	/**
	 * 返回操作状态（成功与否）
	 * @param isOk
	 * @param code
	 * @param msg
	 * @return
	 */
	public static String _genResultXml(boolean isOk, String code, String msg){
		StringBuffer xml = new StringBuffer();
		xml.append("<rsp>");
		if(isOk){
			xml.append("<true/>");
		} else {
			xml.append("<false/>");
		}
		if(code != null && code.length() > 0){
			xml.append("<code>").append(cwUtils.escNull(code)).append("</code>");
		}
		if(msg != null && msg.length() > 0){
			xml.append("<msg>").append(cwUtils.escNull(msg)).append("</msg>");
		}
		xml.append("</rsp>");
		return xml.toString();
	}
	/**
	 * 返回操作数据（XML数据）
	 * @param isOk
	 * @param code
	 * @param msg
	 * @return
	 */
	public static String _genResultXml(String r){
		StringBuffer xml = new StringBuffer();
		xml.append("<rsp>");
		xml.append(cwUtils.escNull(r));
		xml.append("</rsp>");
		return xml.toString();
	}
	/**
	 * 关闭连接和结果集
	 * @param stmt
	 * @param rs
	 */
	private void _closeStmtAndRs(PreparedStatement stmt, ResultSet rs) {

		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			CommonLog.error("ResultSet close error.--" + e.getMessage());
		}
		
		try {
			if (stmt != null) {
				stmt.close();
			}
		} catch (SQLException e) {
			CommonLog.error("PreparedStatement close error.--" + e.getMessage(),e);
		}
		
	}

}
