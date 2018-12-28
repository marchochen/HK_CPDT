package com.cw.wizbank.JsonMod.mobile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.cw.wizbank.JsonMod.Course.bean.ModBean;
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
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbAction;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.tree.cwTree;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwPagination;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.utils.CommonLog;

public class Mobile {

    private MobileModuleParam modParam = null;

    private loginProfile prof = null;

    private WizbiniLoader wizbIni = null;
    
    private HashMap resultMap = null;
    
    public static String SLASH = "/";
    
    public Mobile() {
        ;
    }

    /**
     * constructor
     * 
     * @param knowModParam
     * @param prof
     */
    public Mobile(MobileModuleParam modParam, loginProfile prof, HashMap resultMap) {
        this.modParam = modParam;
        this.prof = prof;
        this.resultMap = resultMap;
    }

    /**
     * constructor
     * 
     * @param knowModParam
     * @param prof
     * @param wizbIni
     */
    public Mobile(MobileModuleParam modParam, loginProfile prof,
            WizbiniLoader wizbIni) {
        this.modParam = modParam;
        this.prof = prof;
        this.wizbIni = wizbIni;
    }
    
    
    /**
	 * 登录
	 * @param con
	 * @param prof当前用户信息，如果登录成功需要赋值(重要)
	 * @param modParam 参数类
	 * @return
	 * @throws SQLException
	 * @throws qdbException
	 */
	public void login(Connection con, HttpSession sess, String encoding) throws SQLException, qdbException {
		dbRegUser regUser = new dbRegUser();
		regUser.getByUsrSteUsrId(con, modParam.getSite_id(), modParam.getUsr_id());
		String login_code = dbRegUser.CODE_OFF_LOGIN_SUCCESS;
		if(regUser.usr_ent_id <= 0){
			login_code = dbRegUser.CODE_OFF_LOGIN_FAILURE0;
		} else {
			String c_paswd = dbRegUser.encrypt(modParam.getUsr_pwd(), new StringBuffer(regUser.usr_ste_usr_id).reverse().toString());
			if(c_paswd == null || !c_paswd.equals(regUser.usr_pwd)){
				login_code = dbRegUser.CODE_OFF_LOGIN_FAILURE1;
			}
		}
		//这步很重要，登录成功之后把当前用户的相关信息放入prof，并保存到session中，用于调用平台现有API.
    	if(login_code.equals(dbRegUser.CODE_OFF_LOGIN_SUCCESS)){
    		prof.usr_ent_id = regUser.usr_ent_id;
    		prof.usr_id = regUser.usr_id;
    		
    		acSite as = new acSite();
    		as.ste_ent_id = modParam.getSite_id();
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
    		
            genResultObj(resultMap, true, login_code, null);
    	} else {
    		genResultObj(resultMap, false, login_code, null);
    	}
	}
	/**
	 * 返回操作状态（成功与否）
	 * @param isOk
	 * @param code
	 * @param msg
	 * @return
	 */
	public static void genResultObj(HashMap resultMap, boolean isOk, String code, String msg){
		resultMap.put("isSuccess", isOk);
		resultMap.put("msg", cwUtils.escNull(msg));
		resultMap.put("code", cwUtils.escNull(code));
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
	public void getNoticeList(Connection con, boolean tc_enabled, WizbiniLoader wizbini) throws SQLException, qdbException {
		 //wb_gen_ann
        dbMessage dbmsg = new dbMessage();
        dbmsg.msg_type = dbMessage.MSG_TYPE_SYS;

        PreparedStatement stmt = null;
        ResultSet rs = null;
        if (tc_enabled) {
        	dbmsg.msg_readonly = true;
        	stmt = dbmsg.getAnnForRead(con, prof, " msg_begin_date ", " desc ", null,  wizbini,null);
        	rs = stmt.executeQuery();
        	Vector v = new Vector();
        	while (rs.next()) {
        		HashMap<String, String> h = new HashMap<String, String>();
        		h.put("title", cwUtils.esc4XML(rs.getString("msg_title")));
        		String url = "servlet/qdbAction?env=wizb&cmd=get_msg&stylesheet=announ_dtl.xsl&msg_type=SYS&msg_readonly=true&code=lcms&msg_id="+rs.getLong("msg_id");
        		h.put("content", url);
        		v.add(h);
        	}
        	resultMap.put("notice_lst", v);
        } else {
        	//暂不考虑无培训中心的情况
        }
        _closeStmtAndRs(stmt, rs);
	}
	
	/**
	 * 查询用户所能看到的所有课程目录及目录下的课程（调用平台现有课程目录功能模块中的API实现）
	 * @param con
	 * @return
	 * @throws SQLException
	 * @throws qdbException
	 * @throws cwException 
	 */
	public void getCourseList(Connection con) throws SQLException{
		// 找到学员所在的培训中心
		ViewEntityToTree entity2Tree = new ViewEntityToTree();
		Vector my_tcr_lst = entity2Tree.getSuperTcInfo(con, prof, true, false, true, false);
		Vector<MFolder> folder_lst = new Vector<MFolder>();
		for (int i = 0; i < my_tcr_lst.size(); i++) {
			entityInfo info = (entityInfo) my_tcr_lst.elementAt(i);
			folder_lst = getCourseListByTcr(con, info.ent_id);
		}
		resultMap.put("folder_lst", folder_lst);
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
	private Vector<MFolder> getCourseListByTcr(Connection con, long tcr_id) throws SQLException {
		Vector<MFolder> folder_lst = new Vector<MFolder>();
		ViewEntityToTree entity2Tree = new ViewEntityToTree();
		//查找培训中心下的子培训中心和一级目录
		Vector subTcr_cat_lst = entity2Tree.getSubTcAndCatalogInfo(con, tcr_id, prof, false, false, 0);
		for (int j = 0; j < subTcr_cat_lst.size(); j++) {
			entityInfo subTcr_cat_info = (entityInfo) subTcr_cat_lst.elementAt(j);
			if (subTcr_cat_info.node_type.endsWith(cwTree.NODE_TYPE_TC)) {
				//如果是子培训中心则循环查找
				folder_lst.addAll(getCourseListByTcr(con, subTcr_cat_info.ent_id));
			} else if (subTcr_cat_info.node_type.endsWith(cwTree.NODE_TYPE_CATALOG)) {
				//如果是一级目录则查找该目录下的子目录和课程
				folder_lst.add(getCourseListByNode(con, subTcr_cat_info));
			}
		}
		return folder_lst;
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
	private MFolder getCourseListByNode(Connection con, entityInfo info) throws SQLException {
		
		MFolder f = new MFolder();
		f.setId(info.ent_id);
		f.setName(info.title);
		f.setFolders(new Vector<MFolder>());
		f.setCourses(new Vector<MCourse>());
		
		//查找子目录
		ViewEntityToTree entity2Tree = new ViewEntityToTree();
		Vector subNode_lst = entity2Tree.getChildAndItemNode(con, info.ent_id, prof, false, false, false, false, false);
		//循环查找子目录的子目录及其下的课程
		for (int j = 0; j < subNode_lst.size(); j++) {
			entityInfo subNode_info = (entityInfo) subNode_lst.elementAt(j);			
			f.getFolders().add(getCourseListByNode(con, subNode_info));
			
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
			if (itm_type.equals(aeItem.ITM_TYPE_MOBILE)) {
				long itm_id = rs.getLong("tnd_itm_id");
				String itm_title = cwUtils.esc4XML(rs.getString("tnd_title"));
				
				MCourse cos = new MCourse();
				cos.setId(itm_id);
				cos.setName(itm_title);
				f.getCourses().add(cos);
			}
		}
		_closeStmtAndRs(stmt, rs);
		return f;
	}
	/**
	 * 课程信息
	 * @param con
	 * @param prof
	 * @param info
	 * @return
	 * @throws SQLException
	 * @throws qdbException 
	 * @throws qdbException
	 * @throws cwSysMessage 
	 * @throws cwException 
	 * @throws cwException 
	 */
	public void getCourseInfo(Connection con) throws SQLException, qdbException, cwSysMessage, cwException {
				
		aeItem item = new aeItem();
		item.itm_id = modParam.getItm_id();
		item.get(con);

		MCourse cos = new MCourse();
		cos.setId(item.itm_id);
		cos.setName(item.itm_title);
		cos.setStart_time(_formatTime(cwUtils.escNull(item.itm_content_eff_start_datetime)));
		cos.setEnd_time(_formatTime(cwUtils.escNull(item.itm_content_eff_end_datetime)));
		if(item.itm_app_approval_type != null && item.itm_app_approval_type.length() > 0){
			cos.setApproval_required(true);
		} else {
			cos.setApproval_required(false);
		}
		cos.setContent(item.itm_desc);
		
		long latest_app_id = aeApplication.getLatestApplicationId(con, item.itm_id, prof.usr_ent_id);
		if (latest_app_id > 0) {// 学员有报名
			aeApplication app = new aeApplication();
			app.app_id = latest_app_id;
			app.get(con);
			// 有成功报名
			if (app.app_tkh_id > 0 && app.app_status.equals(aeApplication.ADMITTED)) {
				cos.setApp_status(aeApplication.ADMITTED);
				
				Vector<Long> itm_lst = new Vector<Long>();
				itm_lst.addElement(item.itm_id);
				// 查找学习记录
				Hashtable item_evaluation = dbCourseEvaluation.getCourseEvaluation(con, prof.usr_ent_id, itm_lst);
				// 没有报名的课程不显示离线内容
				if (item_evaluation != null && item_evaluation.get(item.itm_id) != null) {
					dbCourseEvaluation dbCosEval = (dbCourseEvaluation) item_evaluation.get(item.itm_id);
					if (app.app_tkh_id == dbCosEval.cov_tkh_id) {// 验证两个tkh_id是否一致，不一致则提示错误
						
						dbResource resource = new dbResource();
						resource.res_id = dbCosEval.cov_cos_id;
						resource.tkh_id = dbCosEval.cov_tkh_id;
						Vector modList = resource.getCosContentList(con, prof, null, true, null, new Hashtable(), new Hashtable(), null, null,null, false);
						for (int i = 0; i < modList.size(); i++) {
							ModBean mod = (ModBean)modList.elementAt(i);
								if(mod.getSubtype().equals(dbResource.RES_SUBTYPE_MBL)){//************暂时只取一个
								// 移动课件下载路径
								String offline_pkg_path = "resource" + SLASH + mod.getId() + SLASH + mod.getRes_src_link();
								cos.setPath(offline_pkg_path);
								cos.setMod_id(mod.getId());//mod_id
							}
						}
						
						cos.setCos_id(dbCosEval.cov_cos_id);//cos_id
						cos.setTkh_id(dbCosEval.cov_tkh_id);//tkh_id
						cos.setCov_status(dbCosEval.cov_status);
						cos.setCov_last_acc_datetime(_formatTime(cwUtils.escNull(dbCosEval.cov_last_acc_datetime)));
						cos.setCov_total_time(_formatTime(dbAiccPath.getTime(dbCosEval.cov_total_time)));
					} else {
						cos.setTkh_id(0);
						CommonLog.error("Get dbCourseEvaluation error : " + app.app_tkh_id + "!=" + dbCosEval.cov_tkh_id);
					}
				}
			} else {// 没有报名成功
				cos.setApp_status(app.app_status);
			}
		} else {// 学员没有报名
			cos.setApp_status("");
		}
		resultMap.put("course", cos);
	}
	/**
	 * 学员报名（本方法参考自aeAction cmd=ae_direct_ins_appn）
	 * @param con
	 * @param prof
	 * @param modParam : itm_id, usr_ste_usr_id
	 * @return
	 */
	public void enrollCourse(Connection con) {
		try {
			aeQueueManager qm = new aeQueueManager();
			aeApplication aeApp = null;
			aeApp = qm.insApplication(con, "<detail><field01></field01></detail>", prof.usr_ent_id, modParam.getItm_id(), prof, 0, 0, 0, 0, null, null, true, 0, null, null);
			
			if (aeApp.app_id > 0) {
				genResultObj(resultMap, true, null, null);
			} else {
				genResultObj(resultMap, false, null, null);
			}
		} catch (Exception e) {
			CommonLog.error(e.getMessage(),e);
			genResultObj(resultMap, false, null, "error when enroll course :  " + e.getMessage());
		}
	}
	/**
	 * 上传用户学习状态----信息包含在modParam.setJson_obj中传递
	 * @param con
	 * @param prof
	 * @param modParam
	 * @return
	 */
	public void uploadStatus(Connection con) {

modParam.setJson_obj("{'course_lst':[{'cos_id':10,'tkh_id':10,'mod_lst':[{'mod_id':12,'status':'passed','score':100,'last_access':'2012-10-19 05:42:04','time_spent':'5'}]}]}");
		try {
			if(modParam.getJson_obj() == null || modParam.getJson_obj().length() == 0){
				genResultObj(resultMap, false, null, "status xml is null");
			} else {
//System.out.println("***********************************************modParam.getJson_obj()="+modParam.getJson_obj());
				JSONObject jsonObject = JSONObject.fromObject(modParam.getJson_obj());
				JSONArray cos_ary = jsonObject.getJSONArray("course_lst");
				if(cos_ary != null && cos_ary.size() > 0){
				    for(Object cos : cos_ary) {
					    JSONObject co = (JSONObject) cos;
					    long cos_id = 0;
						long tkh_id = 0;
						long mod_id = 0;
						if (co.getString("cos_id") != null && co.getString("cos_id").length() > 0) {
							cos_id = Long.parseLong(co.getString("cos_id"));
						}
						if (co.getString("tkh_id") != null && co.getString("tkh_id").length() > 0) {
							tkh_id = Long.parseLong(co.getString("tkh_id"));
						}
						JSONArray mod_ary = co.getJSONArray("mod_lst");
						if(mod_ary != null && mod_ary.size() > 0){
						    for(Object mod : mod_ary) {
							    JSONObject md = (JSONObject) mod;
							    mod_id = Long.parseLong(md.getString("mod_id"));

								if (cos_id > 0 && tkh_id > 0 && mod_id > 0) {
									dbModuleEvaluation dbmov = new dbModuleEvaluation();
									dbmov.mov_tkh_id = tkh_id;
									dbmov.mov_cos_id = cos_id;
									dbmov.mov_ent_id = prof.usr_ent_id;
				
									//long id = Long.parseLong(item_attr.getAttribute("id"));
									String last_access = md.getString("last_access");
									String time_spent = md.getString("time_spent");
									String status = md.getString("status");
									String score = md.getString("score");
									if(score == null || score.length() == 0){
										score = "0";
									}
									dbmov.mov_mod_id = mod_id;
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
										dbmov.mov_status = dbmov.mov_status.substring(0,1).toUpperCase();
									}
									dbmov.mov_score = score;
									//时间大于0且记录唯一才更新
									if (dbmov.mod_time > 0 && DbTrackingHistory.getAppTrackingIDByCos(con, dbmov.mov_tkh_id, prof.usr_ent_id, dbModule.getCosId(con, dbmov.mov_mod_id), dbmov.mov_mod_id) == 1) {
//System.out.println("********************************************************mov_cos_id="+cos_id +"mov_tkh_id="+tkh_id+"mov_ent_id="+prof.usr_ent_id+"mov_mod_id="+id + " lass_access="+last_access + " time_spent="+time_spent + " status="+status + " score="+score);
										dbmov.save(con, prof, false);
									}
								}
						    }
						}
				    }
					genResultObj(resultMap, true, null, "update successful, count="+cos_ary.size());
				} else {
					genResultObj(resultMap, true, null, "No course update.");
				}
			}
		} catch (Exception e) {
			CommonLog.error(e.getMessage(),e);
			genResultObj(resultMap, false, null, "update fail:"+e.getMessage());
		}
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
			CommonLog.error("ResultSet close error.--" +e.getMessage(),e);
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