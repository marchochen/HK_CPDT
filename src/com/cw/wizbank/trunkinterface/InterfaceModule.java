package com.cw.wizbank.trunkinterface;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import com.cw.wizbank.Dispatcher;
import com.cw.wizbank.ServletModule;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.util.JsonHelper;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cwn.wizbank.utils.CommonLog;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class InterfaceModule extends ServletModule
{
	InterfaceModuleParam modParam;
	
	public InterfaceModule() {
		super();
		modParam = new InterfaceModuleParam();
		param = modParam;
	}
	
	public void process() throws cwException {
		try {
			//HttpSession sess = request.getSession(true);
			//PrintWriter out = response.getWriter();
			
			InterfaceManagement lm = new InterfaceManagement(modParam);
			hasMetaAndSkin=false;//Json中不需要返回Meta信息
			prof = new loginProfile();
			response.setHeader("Access-Control-Allow-Origin","*");
			
			//没有Developer Id
			if(modParam.getDeveloper_id() == null || modParam.getDeveloper_id().length() <= 0){
				throw new cwException("Developer Id is null");
			}
			//Developer Id无效
			if(!Dispatcher.developer.VALIDE_DEVELOPER_ID.contains(modParam.getDeveloper_id())){
				throw new cwException("Developer Id is invalid :" + modParam.getDeveloper_id());
			}
			//没有CMD
			if(modParam.getCmd() == null || modParam.getCmd().length() <= 0){
				throw new cwException("CMD is null");
			}
			//不合法的CMD
			if(!Dispatcher.developer.DEVELOPER_FUNCTIONS.get(modParam.getDeveloper_id()).contains(modParam.getCmd())){
				throw new cwException("CMD " + modParam.getCmd() + " is invalid in " + modParam.getDeveloper_id());
			}
			resultJson.put("status", "0");
			if (modParam.getCmd().equalsIgnoreCase("auth")) {
				  //默认登陆状态是0,如果是HR用户需要通过web service 验证，否则不需要
				int status = 0;
				lm.auth(con, resultJson,status,true, modParam.isRem_me());
				 String en_user = Base64.encode(modParam.getUsr_id().getBytes());//dbRegUser.encrypt(modParam.getUsr_id(),  new StringBuffer("wizbank").reverse().toString());
				 resultJson.put("en_user", en_user);
			} else if(modParam.getCmd().equalsIgnoreCase("check_user")){//查询用户是否有登录记录
				String  userName = new String(Base64.decode(modParam.getEn_user()));//dbRegUser.decrypt(modParam.getEn_user(), new StringBuffer("wizbank").reverse().toString());
				ApiToken token = ApiToken.getEffToken(con, userName);
				Timestamp now = cwSQL.getTime(con);
				if(token.getAtk_id() != null && token.getAtk_expiry_timestamp() .after(now)){
					resultJson.put("status", "0");
					resultJson.put("token", token.getAtk_id());
				}else{
					resultJson.put("status", "2");
				}
				
			} else if (modParam.getCmd().equalsIgnoreCase("account_binding")) {//账号绑定
				lm.accountBinding(con, resultJson);
			} else if (modParam.getCmd().equalsIgnoreCase("account_unbinding")) {//解除账号绑定
				lm.accountUnBinding(con, resultJson);
			} else if (modParam.getCmd().equalsIgnoreCase("create_session")) {//重新创建token
				lm.createSession(con, resultJson);
			} else {
				boolean is_expiry = false;
				try {
					ApiToken.initProfFromToken(con, prof, modParam.getToken() , modParam.getCur_time(), param.getDeveloper_id() );
				} catch (cwException e) {
					is_expiry = true;
					resultJson.put("status", "2");
					CommonLog.error(e.getMessage(),e);
					// throw new cwException(e.getMessage());
				}
				if (!is_expiry) {
					if (prof != null && prof.usr_ent_id > 0) {
						if (modParam.getCmd().equalsIgnoreCase(
								"get_announcement_list")) {// 公告列表
							lm.getAnnouncementList(con, prof, resultJson,  wizbini);
						} else if (modParam.getCmd().equalsIgnoreCase(
								"get_mobile_folder")) {// 移动目录列表
							lm.getMobileCosCatLst(con, resultJson, prof.usr_ent_id, wizbini);
						} else if (modParam.getCmd().equalsIgnoreCase(
								"get_my_count")) {// 待办的学习任务统计
							lm.getMyMobileLrnCount(con, prof, resultJson,  wizbini);
						} else if (modParam.getCmd()
								.equalsIgnoreCase("add_app")) {// 移动学习，添加课程（即学员报名课程）
							lm.addApp(con, prof, modParam, resultJson);
						} else if (modParam.getCmd().equalsIgnoreCase(
								"get_announcement")) {// 公告详细信息
							lm.getAnnouncement(con, resultJson);
							JsonHelper.disableEsc4Json("body", defJsonConfig);
						} else if (modParam.getCmd().equalsIgnoreCase(
								"get_course_list")) {// 课程列表
							lm.getCourseList(con, prof, resultJson);
						} else if (modParam.getCmd().equalsIgnoreCase(
								"get_course")) {// 课程详细信息
							lm.getCourse(con, prof, resultJson);
						} else if (modParam.getCmd().equalsIgnoreCase("get_tst")) {// 取考试试题
							lm.geTst(con, prof,wizbini.getFileUploadResDirAbs(),resultJson);
							JsonHelper.disableEsc4JsonAll(defJsonConfig);
						} else if (modParam.getCmd().equalsIgnoreCase("get_tst_result")) {// 取考试试题
							lm.geTstResult(con, prof,wizbini.getFileUploadResDirAbs(),resultJson);
						}else if (modParam.getCmd().equalsIgnoreCase("send_tst_result")) {// 提交测验
							lm.sendTstResult(con, prof);
						} else if (modParam.getCmd().equalsIgnoreCase("putparam")) {// 视屏模块跟踪
							lm.sendModuleTrack(con, prof);
						} else if (modParam.getCmd().equalsIgnoreCase("add_cos_comment")) {// 课程评论
							lm.addItemComment(con, prof);
						} else if ("get_cos_score".equalsIgnoreCase(modParam.getCmd())) {// 课程评分
							lm.getItemScore(con, prof, resultJson);
						} else if (modParam.getCmd().equalsIgnoreCase("get_tst_result")) {// 取考试试题
							lm.geTstResult(con, prof, wizbini.getFileUploadResDirAbs(),resultJson);
						} else if ("my_profile".equalsIgnoreCase(modParam.getCmd())) {// 获取登录学员详细信息
							lm.getMyProfile(con, prof, resultJson);
						} else if ("get_mobile_poster".equalsIgnoreCase(modParam.getCmd())) {// 获取移动端宣传栏
							lm.getPosterList(con, resultJson, prof.root_ent_id);
						} else if ("get_public_eval_lst".equalsIgnoreCase(modParam.getCmd())) {// 获取公共调查问卷列表
							lm.getMyPublicEval(con, resultJson, prof,modParam.getCur_page(),modParam.getCur_size(),  wizbini);
						} else if ("get_public_eval".equalsIgnoreCase(modParam.getCmd())) {// 获取公共调查问卷详细信息
							lm.getPublicEvalDetail(con, resultJson,modParam.mod_id);
						} else if ("submit_public_eval".equalsIgnoreCase(modParam.getCmd())) {// 提交公共调查问卷
							lm.submitPublicEval(con, prof, modParam.mod_id,modParam.que_id_lst,
									modParam.que_anwser_option_lst,modParam.getUsed_time(),modParam.start_datetime);
						} else if ("get_mobile_course".equalsIgnoreCase(modParam.getCmd())) {// 移动课程搜索
							lm.searchMobileCourse(con, prof, resultJson,modParam.getTitle(), modParam.getTnd_id(),
									modParam.getOrder(),modParam.getCur_page(),modParam.getCur_size());
						} else if ("get_course_detail".equalsIgnoreCase(modParam.getCmd())) {
							lm.getCourseDetail(con, prof, resultJson,modParam.itm_id);
						} else if ("get_vod_info".equalsIgnoreCase(modParam.getCmd())) {
							lm.getVodInformation(con, prof, resultJson,modParam.getCos_id_lst(),
									modParam.getTkh_id_lst());
						} else if ("batch_putparam".equalsIgnoreCase(modParam.getCmd())) {
							String[] cos_ids = modParam.getCos_id_lst().split(InterfaceManagement.SPLIT_STR_2);
							String[] tkh_ids = modParam.getTkh_id_lst().split(InterfaceManagement.SPLIT_STR_2);
							String[] mod_ids = modParam.getMod_id_lst().split(InterfaceManagement.SPLIT_STR_2);
							String[] durations = modParam.getDuration_lst().split(InterfaceManagement.SPLIT_STR_2);
							String[] last_last_times = modParam.getLast_time_lst().split(InterfaceManagement.SPLIT_STR_2);
							SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							for (int i = 0; i < cos_ids.length; i++) {
								modParam.setCos_id(Long.parseLong(cos_ids[i]));
								modParam.setTkh_id(Long.parseLong(tkh_ids[i]));
								modParam.setMod_id(Long.parseLong(mod_ids[i]));
								modParam.setDuration(Long.parseLong(durations[i]));
								modParam.setLast_time(new Timestamp(dateFormat.parse(last_last_times[i]).getTime()));
								lm.sendModuleTrack(con, prof);
							}
						} else if (modParam.getCmd().equalsIgnoreCase(
								"lrn_center")) {// 选课中心
							lm.getCatalogLst(con, prof, resultJson);
						}
						/*
						 * else if
						 * (modParam.getCmd().equalsIgnoreCase("enroll_course"))
						 * {//报名 lm.enrollCourse(con, prof, modParam); } else if
						 * (modParam.getCmd().equalsIgnoreCase("upload_status"))
						 * {//上传学习记录 //result = lm.uploadStatus(con, prof,
						 * modParam); }
						 */
					} else {
						throw new cwException("Token id is invalid :"
								+ modParam.getToken());
					}
				}
			}
		} catch (Exception e) {
		    CommonLog.error(e.getMessage(),e);
			if(e instanceof cwException){
				cwException cwe = (cwException)e;
				if(cwe.getMessage() != null && cwe.getMessage().indexOf("Token") != -1){
					resultJson.put("status", "2");
				}else{
		    resultJson.put("status", "1");
				}
			}else{
				resultJson.put("status", "1");
			}
			CommonLog.error(e.getMessage(),e);
		}
	}
}
