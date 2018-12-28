package com.cw.wizbank.lcms;

import java.io.PrintWriter;

import javax.servlet.http.HttpSession;

import com.cw.wizbank.ServletModule;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbAction;
import com.cw.wizbank.util.cwException;
import com.cwn.wizbank.utils.CommonLog;

public class LcmsModule extends ServletModule
{
	
	public final static String FTN_LCMS_MAIN = "LCMS_MAIN";
	public final static String FTN_LCMS_READ = "LCMS_READ";
	
	public void process() throws cwException {
		try {
			HttpSession sess = request.getSession(true);
		    String encoding = wizbini.cfgSysSetupadv.getEncoding();
			PrintWriter out = response.getWriter();
			String result = null;
			
			if(sess != null && sess.getAttribute(qdbAction.AUTH_LOGIN_PROFILE) != null){
				prof = (loginProfile)sess.getAttribute(qdbAction.AUTH_LOGIN_PROFILE);
			} else {
				prof = new loginProfile();
			}
			
			LcmsModuleParam modParam = new LcmsModuleParam(request, clientEnc, static_env.ENCODING);
			LcmsManagement lm = new LcmsManagement();
			
			if (modParam.cmd.equalsIgnoreCase("login")) {
				modParam.getLoginInfo();
				//会在下面方法中初始化当前用户的必须信息
				result = lm.login(con, prof, modParam, sess, encoding);				
			} else {
				//不能保存session的情况下大部分cmd需要验证用户才允许执行
				if (prof == null || prof.usr_ent_id <= 0) {
					if ((modParam.usr_ste_usr_id != null && modParam.usr_ste_usr_id.length() > 0) && (modParam.usr_pwd != null && modParam.usr_pwd.length() > 0)) {
						//下面的登录操作成功后会给prof赋值，不成功则prof.usr_ent_id不会大于0,后面的cmd不会执行
						result = lm.login(con, prof, modParam, sess, encoding);
					} else {
						result = LcmsManagement._genResultXml(false, "USR", "usr_ste_usr_id or usr_pwd is null.");
					}
				}
				//当前用户是合法用户时下面的cmd才会执行，直接输出错误提示信息result.
				if(prof != null && prof.usr_ent_id > 0){
					if (modParam.cmd.equalsIgnoreCase("get_notice_list")) {
						result = lm.getNoticeList(con, prof, wizbini.cfgTcEnabled,  wizbini);
					} else if (modParam.cmd.equalsIgnoreCase("get_course_list")) {
						result = lm.getCourseList(con, prof, modParam);
					} else if (modParam.cmd.equalsIgnoreCase("enroll_course")) {
						modParam.getEnrollCourseInfo();
						result = lm.enrollCourse(con, prof, modParam);
					} else if (modParam.cmd.equalsIgnoreCase("upload_status")) {
						modParam.getUploadStatusInfo();
						result = lm.uploadStatus(con, prof, modParam);
					}
				}
			}
			//输出操作结果(XML格式)
			outPutResult(out, result);
		} catch (Exception e) {
			CommonLog.error(e.getMessage(),e);
			throw new cwException(e.getMessage());
		}

	}
	
	public void outPutResult(PrintWriter out, String xml){
		out.println(xml);
	}

}
