package com.cw.wizbank.trunkinterface;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.cw.wizbank.JsonMod.Course.Course;
import com.cw.wizbank.JsonMod.Course.CourseModuleParam;
import com.cw.wizbank.JsonMod.Course.bean.CosCommentBean;
import com.cw.wizbank.JsonMod.Course.bean.CourseBean;
import com.cw.wizbank.JsonMod.Course.bean.ModBean;
import com.cw.wizbank.accesscontrol.AcModule;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.accesscontrol.acSite;
import com.cw.wizbank.ae.aeApplication;
import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.ae.aeLearningPlan;
import com.cw.wizbank.ae.aeLearningPlan.myLearningPlan;
import com.cw.wizbank.ae.aeQueueManager;
import com.cw.wizbank.ae.aeTreeNode;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.config.organization.learningplan.LearningPlan;
import com.cw.wizbank.credit.view.ViewCreditsDAO;
import com.cw.wizbank.db.DbTrackingHistory;
import com.cw.wizbank.db.view.ViewTrainingCenter;
import com.cw.wizbank.qdb.SitePoster;
import com.cw.wizbank.qdb.TestMemory;
import com.cw.wizbank.qdb.dbAiccPath;
import com.cw.wizbank.qdb.dbCourse;
import com.cw.wizbank.qdb.dbCourseEvaluation;
import com.cw.wizbank.qdb.dbEntityRelation;
import com.cw.wizbank.qdb.dbInteraction;
import com.cw.wizbank.qdb.dbMessage;
import com.cw.wizbank.qdb.dbModule;
import com.cw.wizbank.qdb.dbModuleEvaluation;
import com.cw.wizbank.qdb.dbPoster;
import com.cw.wizbank.qdb.dbProgress;
import com.cw.wizbank.qdb.dbProgressAttempt;
import com.cw.wizbank.qdb.dbProgressAttemptSave;
import com.cw.wizbank.qdb.dbQuestion;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.dbResource;
import com.cw.wizbank.qdb.dbResourceContent;
import com.cw.wizbank.qdb.dbResourcePermission;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbAction;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.qdb.qdbQueInstance;
import com.cw.wizbank.qdb.qdbTestInstance;
import com.cw.wizbank.report.ExportController;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.utils.CommonLog;

/**
 * @author harvey.tan
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class InterfaceManagement {

    public static long SITE_ID = 1;

    public static String STATUS_SUCCESS = "success";

    public InterfaceModuleParam param;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	public static String SPLIT_STR = "[|]";
	public static String SPLIT_STR_2 = "\\[\\|\\]";
    public static String REPLACE_STR = "[||]";
	
	public InterfaceManagement(InterfaceModuleParam p){
        param = p;
    }

    /**
     * 登录
     * 
     * @param con
     * @param prof当前用户信息
     *            ，如果登录成功需要赋值(重要)
     */
	public void auth(Connection con, HashMap resultMap, int status,boolean isCheckPwd, boolean isRem_me) {
		HashMap auth_back = new HashMap();
		auth_normal(con, isCheckPwd, auth_back, isRem_me);
        resultMap.put("login_status", auth_back.get("login_status"));
        resultMap.put("token", auth_back.get("token"));
    }

    /**
     * 登录
     * 
     * @param con
     * @param prof当前用户信息
     *            ，如果登录成功需要赋值(重要)
     */

	
	private void auth_normal(Connection con, boolean isCheckPwd, HashMap auth_back, boolean isRem_me) {
        if (auth_back == null) {
            auth_back = new HashMap();
        }
        String login_status = dbRegUser.CODE_LOGIN_SUCCESS;
        String token = "";
        dbRegUser regUser = new dbRegUser();

        try {
            regUser.getByUsrSteUsrId(con, SITE_ID, param.getUsr_id());
            if (regUser.usr_ent_id <= 0) {
                login_status = dbRegUser.CODE_USER_NOT_EXIST;// 用户不存在
            }
            if (login_status.equals(dbRegUser.CODE_LOGIN_SUCCESS) && isCheckPwd) {
                String c_paswd = dbRegUser.encrypt(param.usr_pwd, new StringBuffer(regUser.usr_ste_usr_id).reverse().toString());
                if (c_paswd == null || !c_paswd.equals(regUser.usr_pwd)) {
                    login_status = dbRegUser.CODE_PWD_INVALID;// 密码不正确
                }
            }

            if (login_status.equals(dbRegUser.CODE_LOGIN_SUCCESS)) {
                // 生成token
                ApiToken atk = new ApiToken();
				token = cwUtils.getRandomString(ApiToken.TOKEN_LENGTH);//生成token id
                atk.setAtk_id(token);
                atk.setAtk_usr_id(param.getUsr_id());
                atk.setAtk_usr_ent_id(regUser.usr_ent_id);
                atk.setAtk_create_timestamp(param.getCur_time());
				if(isRem_me){
					atk.setAtk_expiry_timestamp(new Timestamp(param.getCur_time().getTime()+30*24*60*60*1000));
				}else{
					atk.setAtk_expiry_timestamp(new Timestamp(param.getCur_time().getTime()+DeveloperConfig.getDeveloperTimeOut(param.getDeveloper_id())*60*1000));
				}
                atk.setAtk_developer_id(param.getDeveloper_id());
                atk.insToken(con);
            }
        } catch (Exception e) {
            CommonLog.error(e.getMessage(),e);
            try {
                con.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            token = "";
            login_status = dbRegUser.CODE_UNKOWN_ERROR + ":" + e.getMessage();// 未知错误
        }
        auth_back.put("token", token);
        auth_back.put("login_status", login_status);
        if (auth_back.get("usr_ent_id") != null) {
            auth_back.put("usr_ent_id", regUser.usr_ent_id);
        }
    }
    /**
     * 微信账号绑定
     * @param con
     * @param resultMap
     */
	
    public void accountBinding(Connection con, HashMap resultMap) {
        String status = "";
        String token = "";
        if (param.getWeixin_id() != null && param.getWeixin_id().length() > 0) {
            // 先做登录操作，检验用户名密码等
            HashMap auth_back = new HashMap();
            auth_back.put("usr_ent_id", "");
			auth_normal(con, true, auth_back, false);
            status = (String) auth_back.get("login_status");
            token = (String) auth_back.get("token");
            // 验证成功后绑定微信号
            try {
                long usr_ent_id = (Long) auth_back.get("usr_ent_id");
                if (status != null && status.length() > 0 && token != null && token.length() > 0 && usr_ent_id > 0) {
                    dbRegUser user = new dbRegUser();
                    user.usr_ent_id = usr_ent_id;
                    user.usr_weixin_id = param.getWeixin_id();
                    user.updUsrWeixinId(con, null);
                }
            } catch (Exception e) {
                CommonLog.error(e.getMessage(),e);
                try {
                    con.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                token = "";
                status = dbRegUser.CODE_UNKOWN_ERROR + ":" + e.getMessage();// 未知错误
            }
        } else {
            token = "";
            status = "Error: weixin Id is null";
        }

        resultMap.put("status", status);
        resultMap.put("token", token);
    }

    /**
     * 微信账号解除绑定
     * 
     * @param con
     * @param resultMap
     */
	
    public void accountUnBinding(Connection con, HashMap resultMap) {
        String status = STATUS_SUCCESS;
        if (param.getUsr_id() != null && param.getUsr_id().length() > 0) {
            try {
                long usr_ent_id = dbRegUser.getEntId(con, param.getUsr_id(), SITE_ID);
                dbRegUser user = new dbRegUser();
                user.usr_ent_id = usr_ent_id;
                user.usr_weixin_id = "";
                user.updUsrWeixinId(con, param.weixin_id);
            } catch (Exception e) {
                CommonLog.error(e.getMessage(),e);
                try {
                    con.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                status = "Error:" + e.getMessage();// 未知错误
            }
        }
        resultMap.put("status", status);
    }

    /**
     * 微信账号绑定
     * 
     * @param con
     * @param resultMap
     */
	
    public void createSession(Connection con, HashMap resultMap) {
        String status = "";
        String token = "";
        try {
            // 先清理一次过期的TOKEN
            ApiToken.delToken(con, param.getCur_time());

            if (param.getUsr_id() != null && param.getUsr_id().length() > 0) {
                long usr_ent_id = dbRegUser.getEntId(con, param.getUsr_id(), SITE_ID);
                if (dbRegUser.checkUsrWeixinId(con, usr_ent_id, param.getWeixin_id())) {// 检查是否绑定账号
                    // 重新做一次登录
                    HashMap auth_back = new HashMap();
					auth_normal(con, false, auth_back, false);
                    status = (String) auth_back.get("login_status");
                    token = (String) auth_back.get("token");
                } else {
                    token = "";
                    status = "Error: usr_id do not binding the weixin Id.";
                }
            } else {
                token = "";
                status = "Error: usr_id is null.";// 未知错误
            }

        } catch (Exception e) {
            CommonLog.error(e.getMessage(),e);
            token = "";
            status = "Error:" + e.getMessage();// 未知错误
        }

        resultMap.put("status", status);
        resultMap.put("token", token);
    }

    public static void initLoginProfile(Connection con, loginProfile prof, String usr_id, long usr_ent_id) throws cwException {
        try {
            prof.usr_ent_id = usr_ent_id;
            prof.usr_id = usr_id;

            acSite as = new acSite();
            as.ste_ent_id = SITE_ID;
            as.get(con);
            prof.root_ent_id = as.ste_ent_id;
            prof.root_id = as.ste_id;
            prof.usr_id = "s" + as.ste_ent_id + "u" + usr_ent_id;
            prof.usr_ste_usr_id = usr_id;

            prof.current_role = AccessControlWZB.ROL_EXT_ID_NLRN;
            // prof.usrGroups = dbUserGroup.traceParentID(con, prof.usr_ent_id,
            // dbEntity.ENT_TYPE_USER);
            // prof.usrGrades = dbUserGroup.traceParentID(con, prof.usr_ent_id,
            // dbEntity.ENT_TYPE_USER_GRADE);
            prof.encoding = qdbAction.getWizbini().cfgSysSetupadv.getEncoding();
            prof.label_lan = "GB2312";
            prof.cur_lan = prof.getCurLan(prof.label_lan);
        } catch (Exception e) {
            throw new cwException("Error in get acSite or set Prof : " + e.getMessage());
        }
    }

    /**
     * 公告列表
     * 
     * @param con
     * @param prof
     * @throws cwException
     */
	
    public void getAnnouncementList(Connection con, loginProfile prof, HashMap resultMap, WizbiniLoader wizbini) throws cwException {

        dbMessage dbmsg = new dbMessage();
        dbmsg.msg_type = dbMessage.MSG_TYPE_SYS;

        PreparedStatement stmt = null;
        ResultSet rs = null;
        dbmsg.msg_readonly = true;
        try {
			stmt = dbmsg.getAnnForRead(con, prof, " msg_begin_date ", " desc ", null, true,  wizbini,null);
			//stmt = dbmsg.getAnnForRead(con, prof, " msg_begin_date ", " desc ", null, false);
            rs = stmt.executeQuery();
            ArrayList announcement_list = new ArrayList();
            while (rs.next()) {
                HashMap announcement = new HashMap();
                announcement.put("id", rs.getLong("msg_id"));
                announcement.put("title", rs.getString("msg_title"));
	    		announcement.put("thumbnail", rs.getString("msg_icon"));
                announcement_list.add(announcement);
            }
            resultMap.put("announcement_list", announcement_list);
        } catch (Exception e) {
            throw new cwException(e.getMessage());
        } finally {
            cwSQL.cleanUp(rs, stmt);
        }
    }

    /**
     * 公告详细信息
     * 
     * @param con
     * @param prof
     * @throws cwException
     */
	
    public void getAnnouncement(Connection con, HashMap resultMap) throws cwException {

        PreparedStatement stmt = null;
        ResultSet rs = null;
        HashMap announcement = new HashMap();
        try {
            stmt = con.prepareStatement("select * from message where msg_id=?");
            stmt.setLong(1, param.getMsg_id());
            rs = stmt.executeQuery();

            if (rs.next()) {
                announcement.put("id", rs.getLong("msg_id"));
                announcement.put("title", rs.getString("msg_title"));
                announcement.put("begin_date", rs.getTimestamp("msg_begin_date"));
                // announcement.put("end_date",
                // rs.getTimestamp("msg_end_date"));
	    		announcement.put("thumbnail", rs.getString("msg_icon"));
                announcement.put("body", rs.getString("msg_body"));
            }
	    	//对公告内容进行处理限制图片的宽度
	    	int index = 0;
	    	int maxWidth = 280; //图片极限大
	    	String htmlCode = announcement.get("body").toString();
	    	StringBuilder builder = new StringBuilder(htmlCode);
	    	while((index = htmlCode.indexOf("<img", index)) != -1){
	    		int endIndex = htmlCode.indexOf("/>", index);
	    		String subStr = htmlCode.substring(index, endIndex + 2);
	    		Document dom = DocumentHelper.parseText(subStr);
	    		Element el = dom.getRootElement();
	    		String width = el.valueOf("@width");
	    		String height = el.valueOf("@height");
	    		String src = el.valueOf("@src");
	    		String style = el.valueOf("@style");
	    		if(!"".equals(width)){
	    			double temp = Double.valueOf(width);
	    			if(temp > maxWidth){
	    				double dev = temp - maxWidth;
	    				 temp = Double.valueOf(height);
	    				 temp -= dev;
	    				if(temp < 0){
	    					temp = Double.valueOf(height);
	    				}
	    				el.addAttribute("style",  String.format("height:%spx;width:%spx", temp, maxWidth));
	    			}
	    		}else if(!"".equals(style)){
	    			if(style.indexOf("width") != -1){
	    				StringBuilder tempBuild = new StringBuilder(style);
	    				int widthIndex = style.indexOf("width:");
	    	    		int widthPx = style.indexOf("px", widthIndex);
	    	    		double tempWidth = 0;
	    	    		try {
	    	    			tempWidth = Double.parseDouble(htmlCode.substring(widthIndex + 6, widthPx));
	    				} catch (NumberFormatException e) {
	    					index++;
	    					continue;
	    				}
	    	    		if(tempWidth > maxWidth){
	    	    			double dev = tempWidth - maxWidth;
	    	    			tempBuild.replace(widthIndex + 6, widthPx,  String.valueOf(maxWidth));
	    	    			int heightIndex = style.indexOf("height:");
	    		    		int heightPx = htmlCode.indexOf("px", heightIndex);
	    		    		double tempHeight = 0;
	    		    		try {
	    		    			tempHeight = Double.parseDouble(htmlCode.substring(heightIndex + 7, heightPx));
	    					} catch (NumberFormatException e) {
	    						index++;
	    						continue;
	    					}
	    		    		tempHeight -= dev;
	    		    		if(tempHeight > 0){
	    		    			tempBuild.replace(heightIndex + 7,  heightPx,  String.valueOf(tempHeight));
	    		    		}
	    	    		}
	    	    		el.addAttribute("style",  tempBuild.toString());
	    			}
	    		}
	    		//去掉http头
	    		src = src.replaceAll("http://","");
	    		src = src.substring(src.indexOf("/"));
	    		//更改成相对路径
	    		src = "../../.." + src;
	    		el.addAttribute("src",  String.valueOf(src));
	    		builder.replace(index, endIndex + 2, el.asXML());
	    		index++;
	    	}
	    	announcement.put("body", builder.toString());
	    	
            resultMap.put("announcement", announcement);
        } catch (Exception e) {
            throw new cwException(e.getMessage() + " : announcement_id=" + param.getMsg_id());
        } finally {
            cwSQL.cleanUp(rs, stmt);
        }
    }
	

    /**
	 * 移动目录列表
	 * @param con
	 * @param resultMap
	 * @throws cwException 
	 */
	public void getMobileCosCatLst(Connection con, HashMap resultMap, long usr_ent_id, WizbiniLoader wizbini) throws cwException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List mobileCosCatLst = new ArrayList();

		try {
			StringBuffer mobileCosCatSql = new StringBuffer(2000);

			mobileCosCatSql.append("SELECT tnd.tnd_id, tnd.tnd_title ")
							.append("FROM aeTreeNode tnd ")
							.append("WHERE tnd.tnd_parent_tnd_id IN (SELECT tnd.tnd_id ")
							.append("FROM aeTreeNode tnd ")
							.append("INNER JOIN aeCatalog cat ON (cat.cat_id = tnd.tnd_cat_id) ")
							.append("WHERE tnd.tnd_parent_tnd_id IS NULL ")
							.append(" and cat.cat_tcr_id in (").append(ViewTrainingCenter.getLrnFliter( wizbini)).append(")")
							.append("AND cat.cat_status = 'ON' ")
							.append("AND cat.cat_mobile_ind = 1) ")
							.append("AND tnd.tnd_type = 'NORMAL' ")
							.append("ORDER BY tnd.tnd_id ");
			
			stmt = con.prepareStatement(mobileCosCatSql.toString());
			stmt.setLong(1, usr_ent_id);
			stmt.setString(2, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
			rs = stmt.executeQuery();

			while (rs.next()) {
				HashMap<String, Object> mobileCatMap = new HashMap<String, Object>();
				mobileCatMap.put("id", rs.getLong("tnd_id"));
				mobileCatMap.put("title", rs.getString("tnd_title"));
				mobileCosCatLst.add(mobileCatMap);
			}

			resultMap.put("folder_list", mobileCosCatLst);
		} catch (Exception e) {
			throw new cwException("getMobileCosCatLst exception : " + "\n" + e.getMessage());
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
	}
	
	/**
	 * 待办的移动学习任务统计
	 * @param con
	 * @param prof
	 * @param resultMap
	 * @throws cwException 
	 */
	public void getMyMobileLrnCount(Connection con, loginProfile prof, HashMap resultMap,  WizbiniLoader wizbini) throws cwException {
		
		HashMap myMobileCountInfo = new HashMap();
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ArrayList result = get_course_list(con, prof, prof.usr_ent_id, 0, 0, 0, 0);
			int count = 0; 
			if(result != null){
				for(int i = 0; i < result.size(); i++){
					Map data = (Map)result.get(i);
					if("I".equalsIgnoreCase(data.get("status").toString())){
						count++;
					}
				}
				myMobileCountInfo.put("my_cos", count);
			}else{
				myMobileCountInfo.put("my_cos", 0);
			}
			result = get_course_list(con, prof, prof.usr_ent_id, 0, 0, 1,0);
			count = 0; 
			if(result != null){
				for(int i = 0; i < result.size(); i++){
					Map data = (Map)result.get(i);
					if("I".equalsIgnoreCase(data.get("status").toString())){
						count++;
					}
				}
				myMobileCountInfo.put("my_exam", count);
			}else{
				myMobileCountInfo.put("my_exam", 0);
			}
			Vector modVec = dbModule.getPublicEvaluationList(con, prof.root_ent_id, prof.usr_ent_id, true, false, 0, true,  true,  wizbini);
			myMobileCountInfo.put("my_eval",modVec == null ? 0 : modVec.size());
		} catch (Exception e) {
			throw new cwException("getMyMobileLrnCount exception : " + "\n" + e.getMessage());
		} finally {
			cwSQL.cleanUp(rs, ps);
		}
		
		resultMap.put("count_info", myMobileCountInfo);
	}
	
	public void addApp(Connection con, loginProfile prof, InterfaceModuleParam modParam, HashMap resultMap)
			throws SQLException, qdbException, IOException, cwSysMessage, cwException {
		aeQueueManager qm = new aeQueueManager();

		// 检查该学员是否已经成功报名该课程
		long app_id = aeApplication.getAppId(con, modParam.itm_id, prof.usr_ent_id, true);
		if (app_id > 0) {
			aeApplication app = new aeApplication();
			app.app_id = app_id;
			app.getWithItem(con);
			if(app.app_status != null && app.app_status.equalsIgnoreCase(aeApplication.ADMITTED)) {
				throw new cwException("Enroll faied : This learner has enrolled this course.");
			}
		}
		
		aeApplication aeApp = qm.insApplication(con, "<detail><field01></field01></detail>", prof.usr_ent_id, modParam.itm_id, prof, 0, 0, 0, 0, null, null, true, 0, null, null);
		
		if (aeApp == null || aeApp.app_id <= 0) {
			throw new cwException("Enroll faied.");
		}
		getCourseDetail(con, prof, resultMap, modParam.itm_id);
	}
	
	/**
     * 学员报名（本方法参考自aeAction cmd=ae_direct_ins_appn）
     * 
     * @param con
     * @param prof
     * @param modParam
     *            : itm_id, usr_ste_usr_id
     * @return
     */
    public String enrollCourse(Connection con, loginProfile prof, InterfaceModuleParam modParam) {
        String xml = "";
        try {
            long usr_ent_id = dbRegUser.getEntId(con, modParam.usr_id, prof.root_ent_id);
            if (usr_ent_id == prof.usr_ent_id) {
                aeItem item = new aeItem();
                item.itm_id = modParam.getCos_id();
                item.getItem(con);
                aeQueueManager qm = new aeQueueManager();
                aeApplication aeApp = null;
                aeApp = qm.insApplication(con, "<detail><field01></field01></detail>", usr_ent_id, modParam.getCos_id(), prof, 0, 0, 0, 0, null, null, true, 0, null, null);

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
     * 
     * @param con
     * @param prof
     * @param modParam
     * @return
     */
    /*
     * public String uploadStatus(Connection con, loginProfile prof,
     * InterfaceModuleParam modParam) { String xml = ""; StringBuffer reback =
     * new StringBuffer(); //modParam.status =
     * "<req><item  ent_id=\"12\" cos_id=\"1\" tkh_id=\"6\"><mod id=\"4\" status=\"I\" score=\"67\" last_access=\"2013-05-19 15:42:04\" ele_loc =\"\" time_spent=\"0:1:38\"/></item></req>"
     * ; try { if(modParam.status == null || modParam.status.length() == 0){ xml
     * = _genResultXml(false, null, "status xml is null"); } else {
     * //解析离线助手传过来的学习记录XML字符串 DocumentBuilderFactory factory =
     * DocumentBuilderFactory.newInstance(); DocumentBuilder builder =
     * factory.newDocumentBuilder(); StringReader sr = new
     * StringReader(modParam.status); InputSource is = new InputSource(sr);
     * Document doc = builder.parse(is); doc.normalize(); // 得到所有的course节点
     * NodeList courses = doc.getElementsByTagName("item"); for (int i = 0; i <
     * courses.getLength(); i++) { Element course_attr = (Element)
     * courses.item(i); long cos_id = 0; long tkh_id = 0; if
     * (course_attr.getAttribute("cos_id") != null &&
     * course_attr.getAttribute("cos_id").length() > 0) { cos_id =
     * Long.parseLong(course_attr.getAttribute("cos_id")); } if
     * (course_attr.getAttribute("tkh_id") != null &&
     * course_attr.getAttribute("tkh_id").length() > 0) { tkh_id =
     * Long.parseLong(course_attr.getAttribute("tkh_id")); } if (cos_id > 0 &&
     * tkh_id > 0) { dbModuleEvaluation dbmov = new dbModuleEvaluation();
     * dbmov.mov_tkh_id = tkh_id; dbmov.mov_cos_id = cos_id; dbmov.mov_ent_id =
     * prof.usr_ent_id;
     * 
     * NodeList mods = course_attr.getElementsByTagName("mod"); for (int j = 0;
     * j < mods.getLength(); j++) { Element item_attr = (Element) mods.item(j);
     * long id = Long.parseLong(item_attr.getAttribute("id")); String
     * last_access = item_attr.getAttribute("last_access"); String time_spent =
     * item_attr.getAttribute("time_spent"); String status =
     * item_attr.getAttribute("status"); String score =
     * item_attr.getAttribute("score"); String ele_loc =
     * item_attr.getAttribute("ele_loc"); if(score == null || score.length() ==
     * 0){ score = "0"; } dbmov.mov_mod_id = id; float time_spent_float = 0;
     * if(time_spent != null && time_spent.length() > 0){
     * if(time_spent.indexOf(":") > -1){ //if time_spent format hh:mm:ss
     * time_spent_float = dbAiccPath.convert2Second(time_spent); } else { //if
     * time_spent format ssss time_spent_float = Float.parseFloat(time_spent); }
     * } dbmov.mod_time = time_spent_float; dbmov.mov_last_acc_datetime =
     * (last_access == null || last_access.length() == 0) ? null :
     * Timestamp.valueOf(last_access); dbmov.mov_status = (status == null ? "" :
     * status); dbmov.mov_ele_loc = (ele_loc == null ? "" : ele_loc);
     * //截取第一个字符并大写 if(dbmov.mov_status.length() > 1){ dbmov.mov_status =
     * dbmov.mov_status.substring(0,1); } dbmov.mov_status =
     * dbmov.mov_status.toUpperCase(); dbmov.mov_score = score; //时间大于0且记录唯一才更新
     * if (dbmov.mod_time > 0 && DbTrackingHistory.getAppTrackingIDByCos(con,
     * dbmov.mov_tkh_id, prof.usr_ent_id, dbModule.getCosId(con,
     * dbmov.mov_mod_id), dbmov.mov_mod_id) == 1) { dbmov.save(con, prof,
     * false); } } //如果需要返回更新后的数据 if(modParam.reback != null &&
     * modParam.reback.equals("true")){ reback.append(get_course_list_xml(con,
     * prof, cos_id, tkh_id)); } } } if(modParam.reback != null &&
     * modParam.reback.equals("true")){ xml = _genResultXml(reback.toString());
     * } else { xml = _genResultXml(true, null, null); } } } catch (Exception e)
     * { e.printStackTrace(); xml = _genResultXml(false, null,
     * "error when upload status :  " + e.getMessage()); } return xml; }
     */

    /**
     * 去掉时间字符串后面小数点及后面的数字
     * 
     * @param time
     * @return
     */
    private String _formatTime(String time) {
        if (time != null && time.lastIndexOf(".") > -1) {
            time = time.substring(0, time.lastIndexOf("."));
        }
        return time;
    }

    /**
     * 查询课程学习内容
     * 
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
        Vector modList = resource.getCosContentList(con, prof, null, true, sco_aicc_id_lst, new Hashtable(), new Hashtable(), null, null, null, false);
        for (int i = 0; i < modList.size(); i++) {
            ModBean mod = (ModBean) modList.elementAt(i);
            StringBuffer xmlBody = new StringBuffer();

            xmlBody.append("<mod id=\"").append(mod.getId());
            xmlBody.append("\" name=\"").append(mod.getTitle());// 课件名称

            String last_acc_date_time = _formatTime(cwUtils.escNull(mod.getAicc_data().getLast_acc_datetime()));
            xmlBody.append("\" last_access=\"").append(last_acc_date_time);// 上次访问时间

            String res_src_link = mod.getRes_src_link();
            String zip_path = "";
            if (res_src_link != null && res_src_link.length() > 7 && res_src_link.indexOf("content") > 0) {// 7是content的字符串长度
                res_src_link = res_src_link.substring(res_src_link.indexOf("content") + 8);// 去掉content/
                zip_path = "content_zip/" + res_src_link.substring(0, res_src_link.indexOf("/")) + ".zip";
            }
            String total_time = _formatTime(mod.getAicc_data().getUsed_time());

            xmlBody.append("\" time_spent=\"").append(total_time);// 累计时长
            xmlBody.append("\" status=\"").append(cwUtils.escNull(mod.getAicc_data().getStatus()));// 状态
            xmlBody.append("\" score=\"").append(mod.getAicc_data().getScore());// 分数
            xmlBody.append("\" ele_loc=\"").append(mod.getAicc_data().getLocation());// 当前进度？
            xmlBody.append("\" scr_link=\"").append(res_src_link);// 课件压缩包名称
            xmlBody.append("\" zip_path=\"").append(zip_path);// 课件压缩包名称

            xmlBody.append("\"/>").append(dbUtils.NEWL);

            cosXml.append(xmlBody);
        }

        return cosXml.toString();
    }

    /**
     * 查询课程中发布的学习内容（SCO, VOD, RDG）
     * 
     * @param con
     * @param itm_id
     * @return
     * @throws SQLException
     */
	
    public Vector _getCosScoAiccResId(Connection con, long cos_res_id) throws SQLException {
        Vector mod_id_lst = new Vector();
        StringBuffer sql = new StringBuffer();
        sql.append("select resources.res_id").append(" from resources").append(" inner join resourcecontent on rcn_res_id_content = res_id").append(
                " inner join course on cos_res_id = rcn_res_id").append(" where cos_res_id = ?").append(
                " and res_type = ? and (res_subtype=? or res_subtype=? or res_subtype=? or res_subtype=? or res_subtype=?)and res_status=? ");
        // .append(" and res_off_allow=?");

        PreparedStatement stmt = con.prepareStatement(sql.toString());
        int index = 1;
        stmt.setLong(index++, cos_res_id);
        stmt.setString(index++, dbResource.RES_TYPE_MOD);
        stmt.setString(index++, dbModule.MOD_TYPE_SCO);
        stmt.setString(index++, dbModule.MOD_TYPE_VOD);
        stmt.setString(index++, dbModule.MOD_TYPE_RDG);
        stmt.setString(index++, dbModule.MOD_TYPE_TST);
        stmt.setString(index++, dbModule.MOD_TYPE_DXT);
        stmt.setString(index++, dbResource.RES_STATUS_ON);
        // stmt.setBoolean(index++, true);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            mod_id_lst.add(new Long(rs.getLong("res_id")));
        }
        cwSQL.cleanUp(rs, stmt);
        return mod_id_lst;
    }

    /**
     * 返回操作状态（成功与否）
     * 
     * @param isOk
     * @param code
     * @param msg
     * @return
     */
    public static String _genResultXml(boolean isOk, String code, String msg) {
        StringBuffer xml = new StringBuffer();
        xml.append("<rsp>");
        if (isOk) {
            xml.append("<true/>");
        } else {
            xml.append("<false/>");
        }
        if (code != null && code.length() > 0) {
            xml.append("<code>").append(cwUtils.escNull(code)).append("</code>");
        }
        if (msg != null && msg.length() > 0) {
            xml.append("<msg>").append(cwUtils.escNull(msg)).append("</msg>");
        }
        xml.append("</rsp>");
        return xml.toString();
    }

    /**
     * 返回操作数据（XML数据）
     * 
     * @param isOk
     * @param code
     * @param msg
     * @return
     */
    public static String _genResultXml(String r) {
        StringBuffer xml = new StringBuffer();
        xml.append("<rsp>");
        xml.append(cwUtils.escNull(r));
        xml.append("</rsp>");
        return xml.toString();
    }

    public void getCourseList(Connection con, loginProfile prof, HashMap resultMap) throws cwException {
		ArrayList result = get_course_list(con, prof, prof.usr_ent_id, 0, 0,param.getExam_ind(), 0);
        resultMap.put("course_list", result);
    }
	

    @SuppressWarnings("unchecked")
    public void getCourse(Connection con, loginProfile prof, HashMap resultMap) throws cwException {
		ArrayList result = get_course_list(con, prof, prof.usr_ent_id, param.getCos_id(), param.getTkh_id(),param.getExam_ind(), param.itm_id);
		if(result.size() > 0){
            resultMap.put("course", result.get(0));
        } else {
            resultMap.put("course", null);
        }
		resultMap.put("id", prof.usr_ent_id);
    }

    /**
     * 查询学员学习中的课程
     * 
     * @param con
     * @param usr_ent_id
     * @return
     * @throws cwException
     */
	
    public ArrayList get_course_list(Connection con, loginProfile prof, long usr_ent_id, long cos_id, long tkh_id, int exam_ind, long itm_id) throws cwException {

		ArrayList cos_lst = new ArrayList();

		String sql = "select distinct itm_id, itm_type, itm_title, itm_icon, itm_desc, itm_content_eff_end_datetime "
				+ " , att_create_timestamp, app_tkh_id, cos_res_id, ats_cov_status "
				+ " , cov_total_time, cov_score, cov_last_acc_datetime, cov_progress, cov_commence_datetime "
				+ " , iti_score "
				+ " from aeApplication "
				+ " inner join aeItem on itm_id = app_itm_id"
				+ " inner join aeAttendance on att_app_id = app_id "
				+ " inner join aeAttendanceStatus on ats_id = att_ats_id "
				+ " inner join Course on cos_itm_id = itm_id "
				+ " inner join CourseEvaluation on (app_ent_id = cov_ent_id and cos_res_id = cov_cos_id and app_tkh_id = cov_tkh_id) "
				+ " left join v_item_info on iti_itm_id = itm_id "
				+ " where app_ent_id = ? and app_status in ('Admitted') "// 已成功报名的课程
				+ " and itm_type = 'MOBILE' "
				+ " and itm_status = 'ON' and itm_exam_ind = ? and itm_ref_ind = 0 and itm_integrated_ind = 0 ";// 只查询在线课程
				
		
		if(cos_id > 0){
			sql += " and cos_res_id=" + cos_id;
		}
		if(tkh_id > 0){
			sql += " and app_tkh_id=" + tkh_id;
		}
		
		if(itm_id > 0){
			sql += " and itm_id=" + itm_id;
		}
		//测试有重复记录
		sql += " order by cov_last_acc_datetime desc ";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(sql);
			stmt.setLong(1, usr_ent_id);
			stmt.setInt(2, exam_ind);
			rs = stmt.executeQuery();
			while (rs.next()) {
				HashMap cos = new HashMap();

				cos.put("cos_id", rs.getLong("cos_res_id"));
				long app_tkh_id = rs.getLong("app_tkh_id");
				cos.put("tkh_id", app_tkh_id);
				cos.put("title", rs.getString("itm_title"));// 课程标题
				long temp_itm_id = rs.getLong("itm_id");
				String itm_icon = cwUtils.escNull(rs.getString("itm_icon"));
				if(itm_icon != null && itm_icon.trim().length() > 0){
					itm_icon = ".."+cwUtils.SLASH+"item"+cwUtils.SLASH+temp_itm_id+cwUtils.SLASH+itm_icon;
				}
				cos.put("thumbnail", itm_icon);// 缩略图
				cos.put("total_time", _formatTime(dbAiccPath.getTime(rs.getFloat("cov_total_time"))));// 学习时长
				cos.put("status", rs.getString("ats_cov_status"));// 学习状态
				cos.put("last_acc_datetime", cwUtils.escNull(rs.getTimestamp("cov_last_acc_datetime")));// 上次访问时间
				cos.put("type", rs.getString("itm_type"));// 课程类型
				cos.put("progress", rs.getFloat("cov_progress"));// 学习进度
				cos.put("description",cwUtils.escNull(rs.getString("itm_desc")));// 课程简介
				cos.put("enrollment_datetime", cwUtils.escNull(rs.getTimestamp("att_create_timestamp")));// 录取日期
				cos.put("end_datetime", cwUtils.escNull(rs.getTimestamp("itm_content_eff_end_datetime")));// 截止日期（还不完善）
				//单个课程则显示详信息
				if(cos_id > 0 || tkh_id > 0 || itm_id > 0){
					//查询目录信息
					if(itm_id == 0){
						itm_id = dbCourse.getCosItemId(con, cos_id);
					}
					List nodes = aeTreeNode.getMobileItemParentNode(con, itm_id);
					StringBuilder folder = new StringBuilder();
					for(int i = 0; i < nodes.size(); i++){
						aeTreeNode node = (aeTreeNode)nodes.get(i);
						folder.append(node.tnd_title + "/");
					}
					if(folder.length() > 0){
						folder.deleteCharAt(folder.length() - 1);
					}
					cos.put("folder", folder.toString());// 课程文件夹
					/*if(tkh_id > 0){*/
						//统一显示评分的平均分
						Course course = new Course();
						/**
						 * 更改评论到sns_comment 2014-04-21 by leon
						 * 评分只能评1次
						 */
/*						List<CosCommentBean> cb = course.getCosCommentInfoByUserList(con,temp_itm_id);
						double total = 0;
						ArrayList<HashMap<String, String>> comList = new ArrayList<HashMap<String, String>>();
						dbRegUser regUser = new dbRegUser();
						for(CosCommentBean temp : cb){
							HashMap<String, String> tempMap = new HashMap<String, String>();
							tempMap.put("comment", temp.getIct_comment());
							tempMap.put("score", String.valueOf(temp.getIct_score()));
							tempMap.put("create_time", dateFormat2.format(temp.getIct_create_timestamp()));
							regUser.usr_ent_id = temp.getIct_ent_id();
							regUser.get(con);
							tempMap.put("userName", String.valueOf(regUser.usr_display_bil));
							if(regUser.urx_extra_43 != null){
								tempMap.put("user_ico", String.valueOf("/user/" + regUser.ent_id + "/" + regUser.urx_extra_43));
							}else{
								tempMap.put("user_ico", "");
							}
							comList.add(tempMap);
							total += temp.getIct_score();
						}
						DecimalFormat df = new DecimalFormat("#.#");
						cos.put("ict_score", cb.size() == 0 ? 0 : df.format(total / cb.size()));
						cos.put("ict_comment", comList);*/
					/*}*/
					List<CosCommentBean> cb = course.getCosCommentItmId(con,temp_itm_id);
					double total = 0;
					ArrayList<HashMap<String, String>> comList = new ArrayList<HashMap<String, String>>();
					dbRegUser regUser = new dbRegUser();
					String comment = "";
					Timestamp comment_time = null;
					/** 以下分类的目的：手机端评分同时打了星星的一起显示，pc只点击了星星的单独显示**/
					List<CosCommentBean> repeat = new ArrayList<CosCommentBean>();
					outsite:for(int i=0; i<cb.size(); i++) {
						CosCommentBean ccb = cb.get(i);
						for(int j=i+1;j<cb.size();j++) {
							if(ccb.getIct_create_timestamp().equals(cb.get(j).getIct_create_timestamp())) {
								repeat.add(ccb);
								continue outsite;
							}
						}
					}
					cb.removeAll(repeat);
					for(CosCommentBean temp : cb){
						//所有不为空的就是评论，为空的就是星星评分
							//找到没有标记的那条跳过
							HashMap<String, String> tempMap = new HashMap<String, String>();
							tempMap.put("comment", temp.getIct_comment());
							Long score = course.getCountScoreById(con, temp.getItm_id(),temp.getIct_ent_id(),temp.getIct_create_timestamp());
							tempMap.put("score", String.valueOf(score));
							tempMap.put("create_time", dateFormat2.format(temp.getIct_create_timestamp()));
							regUser.usr_ent_id = temp.getIct_ent_id();
							regUser.get(con);
							tempMap.put("userName", String.valueOf(regUser.usr_display_bil));
							if(regUser.urx_extra_43 != null){
								tempMap.put("user_ico", String.valueOf("/user/" + regUser.ent_id + "/" + regUser.urx_extra_43));
							}else{
								tempMap.put("user_ico", "");
							}
							comList.add(tempMap);
							total += score;
					}
					
					DecimalFormat df = new DecimalFormat("#.#");
					//cos.put("ict_score", cb.size() == 0 ? 0 : df.format(total / cb.size()));
					cos.put("ict_score", rs.getString("iti_score"));
					//Collections.reverse(comList);	//倒序
					cos.put("ict_comment",  comList);
						
					Vector sco_aicc_id_lst = _getCosScoAiccResId(con, cos_id);
					ArrayList module_list = new ArrayList();
					if(sco_aicc_id_lst != null && sco_aicc_id_lst.size() > 0){
						dbResource resource = new dbResource();
						resource.res_id = cos_id;
						resource.tkh_id = tkh_id;
						Vector modList = resource.getCosContentList(con, prof, null, true, sco_aicc_id_lst, new Hashtable(), new Hashtable(), null, null,null, false);
						for (int i = 0; i < modList.size(); i++) {
							ModBean mb = (ModBean)modList.elementAt(i);
							String scr_link = mb.getRes_src_link();
							//如果是视频点播或者是教材并且是上传文件的话则拼出路径返回
							if(mb.getSubtype() != null && mb.getRes_src_type() != null && mb.getRes_src_type().equals(dbModule.SRC_TYPE_FILE) && (mb.getSubtype().equals(dbModule.MOD_TYPE_VOD)||mb.getSubtype().equals(dbModule.MOD_TYPE_RDG))){
								scr_link = ".."+"/"+"resource"+"/"+mb.getId()+"/"+mb.getRes_src_link();
							}
							
							HashMap mod = new HashMap();
							mod.put("id", mb.getId());
							mod.put("title", mb.getTitle());//标题
							mod.put("order", mb.getOrder());//排序
							mod.put("subtype", mb.getSubtype());//模块类型
							mod.put("last_acc_datetime",cwUtils.escNull(mb.getAicc_data().getLast_acc_datetime()));//上次访问时间
							mod.put("scr_link", scr_link);//课件访问路径
							mod.put("scr_type", mb.getRes_src_type());//文件类型
							mod.put("used_time", _formatTime(cwUtils.escNull(mb.getAicc_data().getUsed_time())));//课件访问总时间
							mod.put("status", cwUtils.escNull(mb.getAicc_data().getStatus()));//课件学习状态
							mod.put("score", mb.getAicc_data().getScore());//课件学习分数
							mod.put("ele_loc", cwUtils.escNull(mb.getAicc_data().getLocation()));//课件断点
							mod.put("desc", cwUtils.escNull(mb.getDesc()));//简介
							mod.put("total_time", cwUtils.escNull(mb.getMod_required_time()));//必修时长
							mod.put("down", String.valueOf(mb.isMod_download_ind()));//是否能下载
							mod.put("max_score", cwUtils.escNull(mb.getMax_score()));//最高分
							mod.put("pass_score", cwUtils.escNull(mb.getPass_score()));//及格分数
							mod.put("sco_version", cwUtils.escNull(mb.getSco_version()));
							boolean canStart = true;//开始考试
							long mod_max = mb.getMax_user_attempt();
							mod.put("mod_max", mod_max);
							if(mb.getAicc_data().getNumber() !=null){
								long haved_max = Long.parseLong(mb.getAicc_data().getNumber());
								mod.put("haved_max", haved_max);
								if(haved_max >= mod_max && mod_max >0){
									canStart = false;
								}
							} else {
								mod.put("haved_max", 0);
							}
							mod.put("start_ind", canStart);
							module_list.add(mod);
						}
					}
					cos.put("module_list", module_list);
				}

				cos_lst.add(cos);
			}
		} catch (Exception e) {
			throw new cwException(e.getMessage() + " : usr_ent_id=" + usr_ent_id);
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return cos_lst;
	}

    
    public void geTst(Connection con, loginProfile prof, String uploadDir, HashMap resultMap) throws qdbException, qdbErrMessage, cwSysMessage, SQLException, cwException {
        long cos_res_id = param.getCos_id();
        long mod_id = param.getMod_id();
        long tkh_id = param.getTkh_id();

        // long cos_res_id = 65;
        // long mod_id = 77;
        // long tkh_id = 29;

        Hashtable tests_memory = new Hashtable();
        HashMap memory = qdbAction.tests_memory;
        ExportController controller = new ExportController();
        Vector resIdVec = new Vector();
        Vector orderId = new Vector();
        boolean restoreQue = false; // check if the dynamic que need to be
        // restored

        dbModule mod = new dbModule();
        mod.mod_res_id = mod_id;
        mod.get(con);

        dbProgressAttemptSave dbpas = new dbProgressAttemptSave();
        dbpas.pasTkhId = tkh_id;
        dbpas.pasResId = mod_id;
        if (dbpas.chkforExist(con)) {
            restoreQue = true;
        }
        controller.setTotalRow(100);
        controller.currentRow = 1;
        // pass the resIdVec as reference and store all the queId in the test

        if (mod.mod_type.equalsIgnoreCase(dbModule.MOD_TYPE_DXT) && restoreQue) {
            TestMemory tm = new TestMemory();
            Vector vec_res_id_lst = new Vector();
            Vector vec_test_score = dbQuestion.getQueForRestore_test(con, mod.mod_type, mod_id, tkh_id, vec_res_id_lst);
            tm.hs_tests_score.put(new Integer(1), vec_test_score);
            tests_memory.put(mod_id, tm);
        } else if (mod.mod_type.equalsIgnoreCase(dbModule.MOD_TYPE_DXT) && mod.mod_logic.equalsIgnoreCase(dbModule.MOD_LOGIC_ADT)) {
            TestMemory tm = new TestMemory();
            tm.setTest(mod, prof.usr_id, uploadDir, con, controller);
            tests_memory.put(mod_id, tm);
        } else {
            if (!memory.containsKey(mod_id)) {
                synchronized (dbModule.obj) {
                    // 得到同步锁之后再判断一次，预防等待同步锁时别人已经为Hashtable赋值了
                    if (!memory.containsKey(mod_id)) {
                        memory.put(mod_id, new TestMemory());
                    }
                }
            }
            TestMemory testMemory = (TestMemory) memory.get(mod_id);
            if (testMemory.hs_tests_score.size() == 0) {
                testMemory.beginSetTest(mod, prof.usr_id, uploadDir, con, controller);
            }
            dbResource dbr = new dbResource();
            dbr.res_id = mod_id;
            Timestamp updateTimeStamp = dbr.getUpdateTimeStamp(con);
            // 判断测验的修改时间检查当前测验是否被修改过
            if (updateTimeStamp != null && !updateTimeStamp.equals(testMemory.updateTimeStamp)) {
                testMemory.reSetTest(mod, updateTimeStamp, prof.usr_id, uploadDir, con, controller);
            }
            tests_memory.putAll(memory);
        }

        HashMap mod_hash = new HashMap();
        mod_hash.put("cos_id", cos_res_id);// 模块所在课程cos_res_id
        mod_hash.put("tkh_id", tkh_id);// 标题
        mod_hash.put("mod_id", mod_id);// 排序
        mod_hash.put("start_datetime", cwSQL.getTime(con));
        mod_hash.put("subtype", mod.mod_type);
        mod_hash.put("title", mod.res_title);
        mod_hash.put("pass_score", mod.mod_pass_score);
        mod_hash.put("duration", mod.res_duration);
        mod_hash.put("time_left", mod.res_duration);
        mod_hash.put("show_answer_ind", mod.mod_show_answer_ind);
        mod_hash.put("show_save_and_suspend_ind", mod.mod_show_save_and_suspend_ind);
        mod_hash.put("managed_ind", mod.mod_managed_ind);
        mod_hash.put("desc", mod.res_desc);

        String qList = mod.getTestQueXML(tests_memory, mod_id, mod.mod_type, tkh_id, uploadDir, con, restoreQue, true, resIdVec, null, orderId, controller);
        qList = "<lst>" + qList + "</lst>";
        mod_hash.put("que_list", dbQuestion.parseQuesXmlToHash(qList));
        resultMap.put("module", mod_hash);
    }

    
    public void sendTstResult(Connection con, loginProfile prof) throws Exception {
        long mod_id = param.getMod_id();
        long tkh_id = param.getTkh_id();
        Timestamp start_time = param.getStart_datetime();
        long[] que_id_lst = cwUtils.splitToLong(param.getQue_id_lst(), SPLIT_STR);
        String[] que_anwser_option_lst = cwUtils.splitToString(param.getQue_anwser_option_lst(), SPLIT_STR);
        long[] que_anwser_option_id_lst = cwUtils.splitToLong(param.getQue_anwser_option_id_lst(), SPLIT_STR);
        // long cos_res_id = 65;
        // long mod_id = 77;
        // long tkh_id = 29;
        // Timestamp start_time = cwSQL.getTime(con);
        // start_time.setMinutes(2);
        // long[] que_id_lst =cwUtils.splitToLong("78[|]79[|]80[|]81", "[|]");
        // String[] que_anwser_option_lst =
        // cwUtils.splitToString("1[|]1~2[|]1~2[|]2", "[|]");

        qdbTestInstance.markAndSaveMobile(con, prof, tkh_id, mod_id, que_id_lst,que_anwser_option_id_lst, que_anwser_option_lst, start_time);

    }

    
    public void sendModuleTrack(Connection con, loginProfile prof) throws Exception {
        long cos_res_id = param.getCos_id();
        long mod_id = param.getMod_id();
        long tkh_id = param.getTkh_id();
        long duration = param.getDuration();

        // long cos_res_id = 2;
        // long mod_id = 82;
        // long tkh_id = 2;
        // long duration = 600;

//        dbModuleEvaluation dbmov_old = new dbModuleEvaluation();
//        dbmov_old.mov_mod_id = mod_id;
//        dbmov_old.mov_tkh_id = tkh_id;
//        dbmov_old.mov_ent_id = prof.usr_ent_id;
//        dbmov_old.get(con);
//        if(param.getLast_time() != null && dbmov_old.mov_last_acc_datetime.after(param.getLast_time())){
//        	return;
//        }
        
        dbModuleEvaluation dbmov = new dbModuleEvaluation();
        dbmov.mod_time = duration;
        dbmov.mov_cos_id = cos_res_id;
        dbmov.mov_mod_id = mod_id;
        dbmov.mov_tkh_id = tkh_id;
        dbmov.mov_ent_id = prof.usr_ent_id;
        dbmov.mov_status = dbAiccPath.STATUS_INCOMPLETE;
        dbmov.mov_last_acc_datetime = param.getLast_time();
        dbmov.save(con, prof);

    }

    public void geTstResult(Connection con, loginProfile prof, String uploadDir, HashMap resultMap) throws qdbException, qdbErrMessage, cwSysMessage, SQLException {
        long cos_res_id = param.getCos_id();
        long mod_id = param.getMod_id();
        long tkh_id = param.getTkh_id();
        long attempt_nbr = 0;
        try {

            dbModule mod = new dbModule();
            mod.mod_res_id = mod_id;
            mod.get(con);
            HashMap mod_hash = new HashMap();
            HashMap lrn_result_hash = new HashMap();

            mod_hash.put("cos_id", cos_res_id);// 模块所在课程cos_res_id
            mod_hash.put("tkh_id", tkh_id);// 标题
            mod_hash.put("mod_id", mod_id);// 排序
            mod_hash.put("start_datetime", cwSQL.getTime(con));
            mod_hash.put("subtype", mod.mod_type);
            mod_hash.put("title", mod.res_title);
            mod_hash.put("pass_score", mod.mod_pass_score);
            mod_hash.put("duration", mod.res_duration);
            mod_hash.put("time_left", mod.res_duration);
            mod_hash.put("show_answer_ind", mod.mod_show_answer_ind);
            mod_hash.put("show_save_and_suspend_ind", mod.mod_show_save_and_suspend_ind);
            mod_hash.put("managed_ind", mod.mod_managed_ind);
            mod_hash.put("desc", mod.res_desc);

            dbProgress pgr = new dbProgress();
            pgr.pgr_usr_id = prof.usr_id;
            pgr.pgr_res_id = mod_id;
            pgr.pgr_tkh_id = tkh_id;
            if (attempt_nbr == 0)
                pgr.get(con);
            else
                pgr.get(con, attempt_nbr);

            lrn_result_hash.put("pgr_score", pgr.pgr_score);
            lrn_result_hash.put("pgr_status", pgr.pgr_status);
            lrn_result_hash.put("pgr_completion_status", pgr.pgr_completion_status);

            lrn_result_hash.put("pgr_start_datetime", pgr.pgr_start_datetime);
            lrn_result_hash.put("pgr_complete_datetime", pgr.pgr_complete_datetime);
            lrn_result_hash.put("pgr_attempt_nbr", pgr.pgr_attempt_nbr);

            // Get the list of question in the test
            int i;
            Vector qList = new Vector();
            qList = dbProgressAttempt.getChildAss(con, pgr.pgr_usr_id, pgr.pgr_res_id, pgr.pgr_attempt_nbr, pgr.pgr_tkh_id);

            dbResourceContent rcn = null;

            Hashtable qHash = new Hashtable();
            dbQuestion dbq = null;
            dbq = new dbQuestion();
            qHash = dbq.get_test(con, qList);

            Hashtable usrAttemptHash = new Hashtable();
            dbProgressAttempt atm = new dbProgressAttempt();

            atm.atm_pgr_usr_id = pgr.pgr_usr_id;
            atm.atm_pgr_res_id = pgr.pgr_res_id;
            atm.atm_pgr_attempt_nbr = pgr.pgr_attempt_nbr;
            atm.atm_tkh_id = pgr.pgr_tkh_id;

            usrAttemptHash = atm.getQAttempt(con, qHash);

            Long queId = null;

            StringBuffer result = new StringBuffer();
            result.append("<lst>");
            Vector lrn_que_result_lst = new Vector();
            for (i = 0; i < qList.size(); i++) {
                if (!usrAttemptHash.containsKey(new Long(((dbResourceContent) qList.elementAt(i)).rcn_res_id_content)))
                    continue;

                Vector atm_vec = (Vector) usrAttemptHash.get(new Long(((dbResourceContent) qList.elementAt(i)).rcn_res_id_content));
                if (atm_vec != null && atm_vec.size() > 0) {
                    for (int j = 0; j < atm_vec.size(); j++) {
                        atm = (dbProgressAttempt) atm_vec.get(j);
                        if (atm != null) {
                            HashMap lrn_que_result_detail_hash = new HashMap();
                            lrn_que_result_detail_hash.put("que_id", atm.atm_int_res_id);
                            lrn_que_result_detail_hash.put("int_order", atm.atm_int_order);
                            if (atm.atm_response_bil != null) {
                                lrn_que_result_detail_hash.put("response_bil", atm.atm_response_bil.replaceAll("[|]", "~"));
                            }

                            lrn_que_result_detail_hash.put("correct_ind", atm.atm_correct_ind);
                            lrn_que_result_lst.add(lrn_que_result_detail_hash);
                        }
                    }
                }
                rcn = (dbResourceContent) qList.elementAt(i);
                queId = new Long(rcn.rcn_res_id_content);
                dbq = (dbQuestion) qHash.get(queId);

                result.append(dbq.asXML_test(con, rcn.rcn_order, false, 0));

            }
            lrn_result_hash.put("lrn_que_result_lst", lrn_que_result_lst);
            result.append("</lst>");
            mod_hash.put("que_list", dbQuestion.parseQuesXmlToHash(result.toString()));
            mod_hash.put("lrn_result", lrn_result_hash);
            resultMap.put("module", mod_hash);

        } catch (SQLException e) {
            throw new qdbException(e.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    public void getCatalogLst(Connection con, loginProfile prof,  HashMap resultMap) throws SQLException {
        long tnd_id =param.getTnd_id();
        String SQL = "";
        // 查询某一培训中心下的所有顶层目录(注：这个(些)目录是已发布的)在aeTreeNode表中对应的tnd_id的SQL
        if (tnd_id == 0) {
            SQL = " select treeNode.tnd_id, treeNode.tnd_title "
                    + " from tcTrainingCenter tCenter, "
                    + "      aeCatalog cat, "
                    + "      aeTreeNode treeNode "
                    + " where cat.cat_tcr_id in(select distinct tcr_id from EntityRelation, tcTrainingCenterTargetEntity, tcTrainingCenter  where ern_ancestor_ent_id = tce_ent_id  and ern_child_ent_id = ? and ern_type = 'USR_PARENT_USG' and tce_tcr_id = tcr_id    ) " // 传入：培训中心tcTrainingCenter表的主键tcr_id
                    + "       and tCenter.tcr_id=cat.cat_tcr_id " + "       and cat_status='ON' " + "       and treeNode.tnd_parent_tnd_id is null "
                    + "       and treeNode.tnd_cat_id=cat.cat_id " + " order by tnd_title ";
        } else {
            // 查询某一目录下的所有已发布的一级子目录的SQL
            SQL = "select treeNode.tnd_id, treeNode.tnd_title" // 目录在aeTreeNode结点树表中的tnd_id，目录名
                    + " from aeTreeNodeRelation treeNodeR, " // 用来记录目录与目录、目录与课程的关系表
                    + "      aeTreeNode treeNode " // 课程与目录的关系结点树
                    + " where treeNodeR.tnr_ancestor_tnd_id=? " // 传入：某一目录ID(aeTreeNode表的tnd_id字段)
                    + "       and treeNodeR.tnr_child_tnd_id=treeNode.tnd_id " + "       and tnr_parent_ind=1 "
                    + "       and treeNodeR.tnr_type='TND_PARENT_TND' "
                    + "       and treeNode.tnd_status='ON' " // 该目录是已发布的;
                    + "       order by treeNode.tnd_title ";
        }

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        Vector topCatalogBeanVector = new Vector();
        try {

            if (tnd_id > 0) {
                // 取出目录下所有课程
                resultMap.put("itm_lst", getCosLstByTnd(con, prof.usr_ent_id, tnd_id));
            }
            pstmt = con.prepareStatement(SQL);
            if (tnd_id == 0) {
                pstmt.setLong(1, prof.usr_ent_id);
            } else {
                pstmt.setLong(1, tnd_id);
            }
            rs = pstmt.executeQuery();

            while (rs.next()) {
                HashMap tnd_hash = new HashMap();
                tnd_hash.put("tnd_id", rs.getLong("tnd_id")); // 目录ID(用于前台目录树展现)
                tnd_hash.put("tnd_title", rs.getString("tnd_title")); // 目录名
                topCatalogBeanVector.add(tnd_hash);

            } // end while

            resultMap.put("tnd_lst", topCatalogBeanVector);

        } finally {
            // 关闭相关的数据库操作
            if (rs != null) {
                rs.close();
                rs = null;
            }

            if (pstmt != null) {
                pstmt.close();
                pstmt = null;
            }
        }

    }
    
    /**
     * 课程评分
     * @param con
     * @param prof
     * @return
     * @throws Exception
     */
    public void addItemComment(Connection con, loginProfile prof) throws Exception {
      	Course cos = new Course();
      	long cos_res_id = param.getCos_id();
      	long itm_id = param.getItm_id();
      	if(itm_id ==0){
      		itm_id = dbCourse.getCosItemId(con, cos_res_id);
      	}
      	long tkh_id = param.getTkh_id();
      	long score = param.getScore();
      	String comment = param.getComment();
      	
      	cos.addCourseComment(con, itm_id, prof, tkh_id, score, comment);
    }
    
    public void getItemComment(Connection con, loginProfile prof) throws Exception {
    	
    }
    
    /**获取当前登录学员详细信息
     * @param con
     * @param prof
     * @param resultJson
     */
    public void getMyProfile(Connection con, loginProfile prof, HashMap resultJson)
    		throws SQLException, qdbException{
    	UserBean userBean = new UserBean();
    	userBean.my_profile(con, resultJson, new HashMap<String, Boolean>(), prof);
    	//对用户组进行特殊处理，只拿最后一层用户组
    	String usr_group = resultJson.get("usr_group") != null ? resultJson.get("usr_group").toString() : "";
    	if(!"".equals(usr_group)){
    		resultJson.put("usr_group", usr_group.substring(usr_group.lastIndexOf("/") + 1, usr_group.length()));
    	}
    	Map creditTotal = ViewCreditsDAO.getCreditTotalByType(con, prof.usr_ent_id);
    	resultJson.put("training_score", creditTotal.get(ViewCreditsDAO.MAP_KEY_TRAINNING_SCORE));
    	resultJson.put("activity_score", creditTotal.get(ViewCreditsDAO.MAP_KEY_ACTIVITY_SCORE));
    }
    
    /**获取移动端宣传栏信息
     * @param con
     * @param resultJson
     * @param ste_id
     * @throws SQLException
     */
    public void getPosterList(Connection con, HashMap resultJson, long ste_id) throws SQLException{
    	dbPoster poster = new dbPoster();
    	poster.sp_ste_id = ste_id;
    	List<SitePoster> posterlist = poster.getPoster(con, true);
    	List<Map<String, String>> publicPoster = new ArrayList<Map<String, String>>();
    	for(SitePoster sitePoster : posterlist){
    		Map<String, String> row = new HashMap<String, String>();
    		row.put("media_file", sitePoster.getSp_media_file());
    		row.put("url", sitePoster.getSp_url());
    		publicPoster.add(row);
    	}
    	resultJson.put("posters", publicPoster);
    }
    
    /**获取我的问卷列表
     * @param con
     * @param resultJson
     * @param prof
     * @param page
     * @param size
     */
    public void getMyPublicEval(Connection con, HashMap resultJson, loginProfile prof, int page, int size, WizbiniLoader wizbini)
    	throws SQLException{
    	//默认查询10条
    	if(size == 0){
    		size = 10;
    	}
    	int start = (page - 1) * size;
    	if(start < 0){
    		start = 0;
    	}
    	int end = start + size;
    	Vector modVec = dbModule.getPublicEvaluationList(con, prof.root_ent_id,prof.usr_ent_id, true, false, 0, true, true,  wizbini);
    	List<Map<String, String>> publicEvalList = new ArrayList<Map<String,String>>();
    	if (modVec != null && modVec.size() > 0) {
			for (int modIndex = 0; modIndex < modVec.size(); modIndex++) {
				if ( modIndex >= start && modIndex < end) {
					Map<String, String> row = new HashMap<String, String>();
					dbModule mod = (dbModule) modVec.get(modIndex);
					row.put("id", String.valueOf(mod.mod_res_id));
					row.put("title", mod.res_title);
					row.put("eff_start_datetime", dateFormat.format(mod.mod_eff_start_datetime));
					row.put("eff_end_datetime", dateFormat.format(mod.mod_eff_end_datetime));
					publicEvalList.add(row);
				}
			}
			resultJson.put("total", modVec.size());
			resultJson.put("public_eval_list", publicEvalList);
		}
    }
    
    public void getPublicEvalDetail(Connection con, HashMap resultJson, long mod_id) 
    		throws SQLException, cwSysMessage, qdbException{
    	dbResource resource = new dbResource();
    	dbModule module = new dbModule();
    	module.mod_res_id = mod_id;
    	module.get(con);
    	resource.res_id = mod_id;
    	resource.get(con);
    	Map<String, Object> evalInfo = new HashMap<String, Object>();
    	evalInfo.put("id", mod_id);
    	evalInfo.put("title", resource.res_title);
    	evalInfo.put("eff_start_datetime", dateFormat.format(module.mod_eff_start_datetime));
    	evalInfo.put("eff_end_datetime", dateFormat.format(module.mod_eff_end_datetime));
    	List<dbResourceContent> ques = dbResourceContent.getChildAss(con,mod_id);
    	evalInfo.put("total", ques.size());
    	List<Map<String, Object>> quesList = new ArrayList<Map<String,Object>>();
    	dbQuestion dbq = new dbQuestion();
    	// 处理题目信息
    	for(dbResourceContent content : ques){
    		Map<String, Object> temp = new HashMap<String, Object>();
    		temp.put("id", content.rcn_res_id_content);
    		dbq.res_id = dbq.que_res_id = content.rcn_res_id_content;
            dbq.get(con);
            try {
            	Document dom = DocumentHelper.parseText(dbq.que_xml);
            	List nodes = dom.selectNodes("body");
            	if(nodes.size() > 0){
            		Element body = (Element)nodes.get(0);
            		List<Map<String, String>> opList = new ArrayList<Map<String,String>>();
            		Element html = body.element("html");
            		if(html!=null) {
            			temp.put("body", html.getStringValue());
            		} else{
            			temp.put("body", body.getText());
            		}         		
            		dom.selectNodes("body");
            		Element interaction = body.element("interaction");
            		temp.put("queType",interaction.attribute("type").getText());
            		if(interaction.attribute("logic") != null){
            			temp.put("type", interaction.attribute("logic").getText());
                		Iterator iterator =  interaction.elementIterator("option");
                		//处理选项信息
                		while(iterator.hasNext()){
                			Map<String, String> optionInfo = new HashMap<String, String>(); 
                			Element option = (Element)iterator.next();
                			optionInfo.put("id", option.attribute("id").getText());
                			optionInfo.put("body", option.getText());
                			opList.add(optionInfo);
                		}
                		temp.put("options", opList);
            		}else{
            			
            		}
            		
            	}
				
			} catch (DocumentException e) {
				throw new cwSysMessage("GEN000");
			}
            quesList.add(temp);
    	}
    	evalInfo.put("questions", quesList);
    	resultJson.put("public_eval", evalInfo);
    } 
    
	public void submitPublicEval(Connection con, loginProfile prof,
			long mod_id, String que_id_lst, String que_anwser_option_lst,
			long used_time, Timestamp start_time)
    	throws SQLException, cwSysMessage, qdbException, cwException, CloneNotSupportedException, qdbErrMessage{
    	AcModule acmod = new AcModule(con);
 
    	String[] queResIds = que_id_lst.split(SPLIT_STR_2);//问卷题目集合
    	String[] answerOptions = que_anwser_option_lst.split(SPLIT_STR_2);//问卷答案集合
    	qdbTestInstance qdbTest = new qdbTestInstance();
    	if(queResIds.length > 0){
    		for(int i = 0; i < queResIds.length; i++){
    			qdbQueInstance queInstance = new qdbQueInstance();
    			try {
    				if(null == queResIds[i] || queResIds[i].equals("")){
    					continue;
    				}
    				queInstance.dbque.res_id = queInstance.dbque.que_res_id = Long.parseLong(queResIds[i]);
				} catch (NumberFormatException e) {
					throw new cwSysMessage("GEN000");
				}
    			dbInteraction intr = new dbInteraction();
                intr.int_res_id = queInstance.dbque.que_res_id;
                intr.int_order = 1;
                queInstance.dbque.ints.addElement(intr);
                //处理答案
                dbProgressAttempt atm = new dbProgressAttempt();
                atm.atm_int_res_id = queInstance.dbque.que_res_id;
                atm.atm_int_order = intr.int_order;

                atm.atm_response_bil = answerOptions[i].replace(REPLACE_STR, "").trim();

                // Matching type of response
                atm.atm_responses = dbUtils.split(atm.atm_response_bil , "~");
                queInstance.atms.addElement(atm);
                qdbTest.qdbQues.addElement(queInstance);
    		}
    		//提交问卷
    		qdbTest.pgr_status = "OK";
    		qdbTest.start_time = start_time;
    		qdbTest.used_time = used_time;
    		qdbTest.usr_id = prof.usr_id;
    		qdbTest.dbmod.mod_res_id = mod_id;
    		qdbTest.markAndSave(con, prof, DbTrackingHistory.TKH_ID_UNDEFINED);
    	}
    }
	
	/**移动课程搜索API
	 * @param con
	 * @param prof
	 * @param title
	 * @param tndId
	 * @param searchType
	 * @param page
	 * @param size
	 */
	public void searchMobileCourse(Connection con, loginProfile prof, HashMap resultJson,
			String title, long tndId, String searchType, int page, int size) throws SQLException,
			cwException, qdbException{
		List<Map<String, String>> courseList = new ArrayList<Map<String,String>>();
		CourseModuleParam param = new CourseModuleParam();
		param.setCur_page(page);
		param.setLimit(size == 0 ? 10 : size);
		param.setStart(page == 0 ? page : (page - 1) * param.getLimit());
		param.setMobile(true);
		param.setTnd_id(tndId);
		param.setComment(title);
		if(searchType == null || "".equalsIgnoreCase(searchType.trim())){
			searchType = "new";
		}
		if("target".equalsIgnoreCase(searchType)){
			String groupAncesterSql = dbEntityRelation.getAncestorListSql(prof.usr_ent_id, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
			String gradeAncesterSql = dbEntityRelation.getAncestorListSql(prof.usr_ent_id, dbEntityRelation.ERN_TYPE_USR_CURRENT_UGR);
			boolean isSelfInitiatedEnabled = ((LearningPlan) qdbAction.getWizbini().cfgOrgLearningPlan.get(prof.root_id)).getSelfInitiated().isEnabled();
			String itmDir = qdbAction.getWizbini().cfgSysSetupadv.getFileUpload().getItmDir().getUrl();
			List<myLearningPlan> cosVec = aeLearningPlan.getLearningPlan(con, isSelfInitiatedEnabled, prof.root_ent_id, prof.usr_ent_id, prof.usr_display_bil,
					groupAncesterSql, gradeAncesterSql, aeLearningPlan.RECOMMENDED, aeLearningPlan.JSON_FORMAT, "", false, null, null, null, prof.cur_lan, itmDir, null, null, param);
			int count = 0;
			for(myLearningPlan plan : cosVec){
				if (count >= param.getStart() && count < (param.getLimit() + param.getStart())) {
					Map<String, String> tempMap = new HashMap<String, String>();
					tempMap.put("itm_id", String.valueOf(plan.getPitm_id()));
					tempMap.put("tkh_id", String.valueOf(plan.getApp_tkh_id()));
					tempMap.put("title", plan.getPitm_title());// 课程标题
					String itm_icon = cwUtils.escNull(plan.getPitm_icon());
					if(itm_icon != null && itm_icon.length() > 0){
						itm_icon = "/" +itm_icon;
					}
					tempMap.put("thumbnail", itm_icon);// 缩略图
					tempMap.put("status", plan.getAts_cov_status());// 学习状态
					tempMap.put("type", plan.getPitm_type());// 课程类型
					courseList.add(tempMap);
				}
				count ++;
			}
		}else if("nice".equalsIgnoreCase(searchType) || "new".equalsIgnoreCase(searchType)){
			Course course = new Course();
			List coursesVc = null;
			if("nice".equalsIgnoreCase(searchType)){
				coursesVc = course.getSorCourses(con, param, prof, qdbAction.getWizbini());
			}else{
				coursesVc = course.getLastedCourses(con, param, prof, qdbAction.getWizbini());
			}
			for(int i = 0; i < coursesVc.size(); i++){
				CourseBean bean = (CourseBean)coursesVc.get(i);
				Map<String, String> tempMap = new HashMap<String, String>();
				tempMap.put("itm_id", String.valueOf(bean.getItm_id()));
				long app_id = aeApplication.getLatestAppId(con, bean.getItm_id(), prof.usr_ent_id);
				long tkh_id = 0;
				if(app_id > 0){
					tkh_id = aeApplication.getTkhId(con, app_id);
				}
				tempMap.put("tkh_id", String.valueOf(tkh_id));
				tempMap.put("title", bean.getItm_title());// 课程标题
				String itm_icon = cwUtils.escNull(bean.getItm_icon());
				if(itm_icon != null && itm_icon.length() > 0){
					itm_icon =  "/" + itm_icon;
				}
				tempMap.put("thumbnail", itm_icon);// 缩略图
				tempMap.put("status", bean.getCov_status());// 学习状态
				tempMap.put("type", bean.getItm_type());// 课程类型
				tempMap.put("description", bean.getItm_desc());// 课程类型
				courseList.add(tempMap);
			}
			
		}
		resultJson.put("rows", courseList);
	}
	
	public void getCourseDetail(Connection con, loginProfile prof, HashMap resultJson, long itm_id) throws cwException{
		
		String sql = "select itm_id, itm_type, itm_title, itm_icon, itm_desc, itm_content_eff_end_datetime,itm_exam_ind "
				+ " from aeItem where itm_id = ?";
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(sql);
			stmt.setLong(1, itm_id);
			rs = stmt.executeQuery();
			if (rs.next()) {
				HashMap cos = new HashMap();

				cos.put("title", rs.getString("itm_title"));// 课程标题
				cos.put("cos_id",dbCourse.getCosResId(con, itm_id) );// 课程标题
				String itm_icon = cwUtils.escNull(rs.getString("itm_icon"));
				if(itm_icon != null && itm_icon.length() > 0){
					itm_icon = ".."+cwUtils.SLASH+"item"+cwUtils.SLASH+itm_id+cwUtils.SLASH+itm_icon;
				}
				cos.put("is_exam", rs.getBoolean("itm_exam_ind"));
				cos.put("thumbnail", itm_icon);// 缩略图
				cos.put("type", rs.getString("itm_type"));// 课程类型
				//查询目录信息
				List nodes = aeTreeNode.getMobileItemParentNode(con, itm_id);
				StringBuilder folder = new StringBuilder();
				for(int i = 0; i < nodes.size(); i++){
					aeTreeNode node = (aeTreeNode)nodes.get(i);
					folder.append(node.tnd_title + "/");
				}
				if(folder.length() > 0){
					folder.deleteCharAt(folder.length() - 1);
				}
				cos.put("folder", folder.toString());// 课程文件夹
				cos.put("description",cwUtils.escNull(rs.getString("itm_desc")));// 课程简介
					//统一显示评分的平均分
				Course course = new Course();
				
				List<CosCommentBean> cb = course.getCosCommentInfoByUserList(con, itm_id);
				double total = 0;
				ArrayList<HashMap<String, String>> comList = new ArrayList<HashMap<String, String>>();
				dbRegUser regUser = new dbRegUser();
				for(CosCommentBean temp : cb){
					HashMap<String, String> tempMap = new HashMap<String, String>();
					tempMap.put("comment", temp.getIct_comment());
					tempMap.put("score", String.valueOf(temp.getIct_score()));
					tempMap.put("create_time", dateFormat2.format(temp.getIct_create_timestamp()));
					regUser.usr_ent_id = temp.getIct_ent_id();
					regUser.get(con);
					tempMap.put("userName", String.valueOf(regUser.usr_display_bil));
					tempMap.put("user_ico", String.valueOf("/user/" + regUser.ent_id + "/" + regUser.urx_extra_43));
					comList.add(tempMap);
					total += temp.getIct_score();
				}
				DecimalFormat df = new DecimalFormat("#.#");
				cos.put("ict_score", cb.size() == 0 ? 0 : df.format(total / cb.size()));
				cos.put("ict_comment", comList);
				//添加报名状态
				Vector itm_lst = new Vector();
				itm_lst.add(Long.valueOf(itm_id));
				Hashtable item_evaluation = dbCourseEvaluation.getCourseEvaluation(con, prof.usr_ent_id, itm_lst);
				dbCourseEvaluation dbCosEval = (dbCourseEvaluation)item_evaluation.get(new Long(itm_id));
				cos.put("tkh_id", dbCosEval.cov_tkh_id);
				long app_id = aeApplication.getAppIdByTkhId(con, dbCosEval.cov_tkh_id);
				if(dbCosEval != null){
					 
					 cos.put("app_id", app_id);
				}
				aeItem aeitem = new aeItem();
				long status = aeitem.itmButtonCon(con, itm_id, prof.usr_ent_id, null,app_id);
				cos.put("app_status", status);
				resultJson.put("course", cos);
			}
		} catch (Exception e) {
			throw new cwException(e.getMessage() + " : usr_ent_id=" + prof.getUsr_ent_id());
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
	}
	
    public void getVodInformation(Connection con, loginProfile prof,
			HashMap resultJson, String cosIds, String tkhIds) throws cwException {
		String[] cos_ids = cosIds.split(SPLIT_STR_2);
		String[] tkh_ids = tkhIds.split(SPLIT_STR_2);
		String sql = "select itm_id, itm_type, itm_title, itm_icon, itm_desc, itm_content_eff_end_datetime "
				+ " , att_create_timestamp, app_tkh_id, cos_res_id, ats_cov_status "
				+ " , cov_total_time, cov_score, cov_last_acc_datetime, cov_progress, cov_commence_datetime "
				+ " from aeApplication "
				+ " inner join aeItem on itm_id = app_itm_id"
				+ " inner join aeAttendance on att_app_id = app_id "
				+ " inner join aeAttendanceStatus on ats_id = att_ats_id "
				+ " inner join Course on cos_itm_id = itm_id "
				+ " inner join CourseEvaluation on (app_ent_id = cov_ent_id and cos_res_id = cov_cos_id and app_tkh_id = cov_tkh_id) "
				+ " where app_ent_id = ? and app_status in ('Admitted') "// 已成功报名的课程
				+ " and itm_type = 'MOBILE' and itm_status = 'ON' and itm_exam_ind = ? and itm_ref_ind = 0 and itm_integrated_ind = 0 ";// 只查询在线课程
		//避免数量过多时in查询出现问题
		StringBuilder cosSQL = new StringBuilder(" and (");
		for (String cos_id : cos_ids) {
			cosSQL.append(String.format(" cos_res_id = %s or", cos_id));
		}

		sql += cosSQL.substring(0, cosSQL.length() - 2) + " )";

		StringBuilder tkhSQL = new StringBuilder(" and (");
		for (String tkh_id : tkh_ids) {
			tkhSQL.append(String.format(" app_tkh_id = %s or", tkh_id));
		}
		sql += tkhSQL.substring(0, tkhSQL.length() - 2) + " )";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(sql);
			stmt.setLong(1, prof.usr_ent_id);
			stmt.setInt(2, 0);
			rs = stmt.executeQuery();
			Map cos_lst = new HashMap();
			while (rs.next()) {
				HashMap cos = new HashMap();
				long cos_id = rs.getLong("cos_res_id");
				long tkh_id = rs.getLong("app_tkh_id");
				cos.put("cos_id", String.valueOf(cos_id));
				cos.put("tkh_id", String.valueOf(tkh_id));
				cos.put("title", rs.getString("itm_title"));// 课程标题
				long temp_itm_id = rs.getLong("itm_id");
				String itm_icon = cwUtils.escNull(rs.getString("itm_icon"));
				if (itm_icon != null && itm_icon.trim().length() > 0) {
					itm_icon =  cwUtils.SLASH + "item"
							+ cwUtils.SLASH + temp_itm_id + cwUtils.SLASH
							+ itm_icon;
				}
				cos.put("thumbnail", itm_icon);// 缩略图
				cos.put("total_time", _formatTime(dbAiccPath.getTime(rs
						.getFloat("cov_total_time"))));// 学习时长
				cos.put("status", rs.getString("ats_cov_status"));// 学习状态
				cos.put("last_acc_datetime", cwUtils.escNull(rs
						.getTimestamp("cov_last_acc_datetime")));// 上次访问时间
				cos.put("type", rs.getString("itm_type"));// 课程类型
				cos.put("progress", rs.getFloat("cov_progress"));// 学习进度
				cos.put("description",
						cwUtils.escNull(rs.getString("itm_desc")));// 课程简介
				cos.put("enrollment_datetime", cwUtils.escNull(rs
						.getTimestamp("att_create_timestamp")));// 录取日期
				cos.put("end_datetime", cwUtils.escNull(rs
						.getTimestamp("itm_content_eff_end_datetime")));// 截止日期（还不完善）
				Vector sco_aicc_id_lst = _getCosScoAiccResId(con, cos_id);
				ArrayList module_list = new ArrayList();
				if (sco_aicc_id_lst != null && sco_aicc_id_lst.size() > 0) {
					dbResource resource = new dbResource();
					resource.res_id = cos_id;
					resource.tkh_id = tkh_id;
					Vector modList = resource.getCosContentList(con, prof,
							null, true, sco_aicc_id_lst, new Hashtable(),
							new Hashtable(), null, null, null, false);
					for (int i = 0; i < modList.size(); i++) {
						ModBean mb = (ModBean) modList.elementAt(i);
						//只查询视频信息
						if(!(mb.getSubtype().equals(dbModule.MOD_TYPE_VOD))){
							continue;
						}
						String scr_link = mb.getRes_src_link();
						// 如果是视频点播或者是教材并且是上传文件的话则拼出路径返回
						if (mb.getSubtype() != null&& mb.getRes_src_type() != null
								&& mb.getRes_src_type().equals(dbModule.SRC_TYPE_FILE)
								&& (mb.getSubtype().equals(dbModule.MOD_TYPE_VOD)
										|| mb.getSubtype().equals(dbModule.MOD_TYPE_RDG))) {
							scr_link = cwUtils.SLASH  + "resource" +
										cwUtils.SLASH + mb.getId() +
										cwUtils.SLASH  + mb.getRes_src_link();
						}
						HashMap mod = new HashMap();
						mod.put("id", mb.getId());
						mod.put("title", mb.getTitle());// 标题
						mod.put("order", mb.getOrder());// 排序
						mod.put("subtype", mb.getSubtype());// 模块类型
						mod.put("last_acc_datetime", cwUtils.escNull(mb
								.getAicc_data().getLast_acc_datetime()));// 上次访问时间
						mod.put("scr_link", scr_link);// 课件访问路径
						mod.put("scr_type", mb.getRes_src_type());// 文件类型
						mod.put("used_time", _formatTime(cwUtils.escNull(mb
								.getAicc_data().getUsed_time())));// 课件访问总时间
						mod.put("status", cwUtils.escNull(mb.getAicc_data()
								.getStatus()));// 课件学习状态
						mod.put("score", mb.getAicc_data().getScore());// 课件学习分数
						mod.put("ele_loc", cwUtils.escNull(mb
								.getAicc_data().getLocation()));// 课件断点
						mod.put("desc", cwUtils.escNull(mb.getDesc()));// 简介
						mod.put("total_time",
								cwUtils.escNull(mb.getMod_required_time()));// 必修时长
						mod.put("down",
								String.valueOf(mb.isMod_download_ind()));// 是否能下载
						mod.put("max_score",
								cwUtils.escNull(mb.getMax_score()));// 最高分
						mod.put("pass_score",
								cwUtils.escNull(mb.getPass_score()));// 及格分数
						boolean canStart = true;// 开始考试
						long mod_max = mb.getMax_user_attempt();
						mod.put("mod_max", mod_max);
						if (mb.getAicc_data().getNumber() != null) {
							long haved_max = Long.parseLong(mb
									.getAicc_data().getNumber());
							mod.put("haved_max", haved_max);
							if (haved_max >= mod_max && mod_max > 0) {
								canStart = false;
							}
						} else {
							mod.put("haved_max", 0);
						}
						mod.put("start_ind", canStart);
						module_list.add(mod);
					}
				}
				cos.put("module_list", module_list);

				cos_lst.put(String.valueOf(cos.get("cos_id")), cos);
			}
			resultJson.put("course", cos_lst);
		} catch (Exception e) {
			throw new cwException(e.getMessage() + " : usr_ent_id=" + prof.getUsr_ent_id());
		}finally{
			cwSQL.cleanUp(rs, stmt);
		}
	}

    @SuppressWarnings("unchecked")
    public Vector getCosLstByTnd(Connection con, long usr_ent_id, long tnd_id) throws SQLException {
        // 查询某一目录下(包括其子孙目录)的所有用户的可见课程的SQL
        Course course = new Course();
        String sql = course.getViewCoursesOfOneCatalogSql(false, true, new long[]{tnd_id});
        Vector courseVector = new Vector();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = con.prepareStatement(sql);
            int index =1;
            pstmt.setLong(index++, usr_ent_id);
            pstmt.setLong(index++, usr_ent_id);
            pstmt.setLong(index++, usr_ent_id);
            pstmt.setLong(index++, usr_ent_id);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                boolean itm_apply_ind = rs.getBoolean("itm_apply_ind");
                if (!itm_apply_ind) {
                    continue;
                }
                HashMap itm_hash = new HashMap();
                long itm_id = rs.getLong("itm_id");
                itm_hash.put("itm_id", rs.getLong("itm_id")); // 课程ID
                itm_hash.put("itm_title", rs.getString("itm_title")); // 课程名
                itm_hash.put("itm_type", rs.getString("itm_type")); // 课程类型
                itm_hash.put("itm_desc", rs.getString("itm_desc")); // 课程描述
                itm_hash.put("itm_end_datetime",rs.getTimestamp("itm_content_eff_end_datetime")); // 截止日期
                String itm_icon = cwUtils.escNull(rs.getString("itm_icon"));
                if (itm_icon != null && itm_icon.length() > 0) {
                    itm_icon = ".." + cwUtils.SLASH + "item" + cwUtils.SLASH + itm_id + cwUtils.SLASH + itm_icon;
                }
                itm_hash.put("thumbnail", itm_icon);// 缩略图

                courseVector.add(itm_hash);

            } // end while

        } catch (SQLException se) {
            CommonLog.error(se.getMessage(),se);
            throw se;
        } finally {
            // 关闭相关的数据库操作
            if (rs != null) {
                rs.close();
                rs = null;
            }
            if (pstmt != null) {
                pstmt.close();
                pstmt = null;
            }
        }

        return courseVector;
    }

    /** 
     * 获取课程评分
     * @param con
     * @param prof
     * @param resultJson
     * @throws SQLException
     */
	public void getItemScore(Connection con, loginProfile prof, HashMap resultJson) throws SQLException {
		Course course = new Course();
      	long cos_res_id = param.getCos_id();
      	long itm_id = param.getItm_id();
      	if(itm_id ==0){
      		itm_id = dbCourse.getCosItemId(con, cos_res_id);
      	}
      	long tkh_id = param.getTkh_id();
		Long score = course.getCountScore(con, itm_id, prof.usr_ent_id, tkh_id);
		resultJson.put("score", score);
	}

}
