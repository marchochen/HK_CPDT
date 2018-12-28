package com.cw.wizbank.JsonMod.mobile;

import java.io.IOException;
import java.sql.SQLException;
import com.cw.wizbank.ServletModule;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbAction;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSysMessage;

public class MobileModule extends ServletModule {
	   
    MobileModuleParam modParam;
    
    public MobileModule() {
        super();
        modParam = new MobileModuleParam();
        param = modParam;
    }

    public void process() throws SQLException, IOException, cwException {
        try {
        	if(sess != null && sess.getAttribute(qdbAction.AUTH_LOGIN_PROFILE) != null){
				prof = (loginProfile)sess.getAttribute(qdbAction.AUTH_LOGIN_PROFILE);
			} else {
				prof = new loginProfile();
			}
			
        	Mobile mb = new Mobile(modParam, prof, resultJson);
        	modParam.setSite_id(1);//carefully!!设置固定机构
        	hasMetaAndSkin=false;//返回的json不需要Meta信息
        	
			if (modParam.getCmd().equalsIgnoreCase("login")) {
				//会在下面方法中初始化当前用户的必须信息
				mb.login(con, sess, encoding);				
			} else {
				//不能保存session的情况下大部分cmd需要验证用户才允许执行
				if (prof == null || prof.usr_ent_id <= 0) {
					if ((modParam.getUsr_id() != null && modParam.getUsr_id().length() > 0) && (modParam.getUsr_pwd() != null && modParam.getUsr_pwd().length() > 0)) {
						//下面的登录操作成功后会给prof赋值，不成功则prof.usr_ent_id不会大于0,后面的cmd不会执行
						mb.login(con, sess, encoding);
					} else {
						Mobile.genResultObj(resultJson, false, "USR", "usr_ste_usr_id or usr_pwd is null.");
					}
				}
				//当前用户是合法用户时下面的cmd才会执行，直接输出错误提示信息result.
				if(prof != null && prof.usr_ent_id > 0){
					if (modParam.getCmd().equalsIgnoreCase("get_notice_list")) {
						mb.getNoticeList(con, wizbini.cfgTcEnabled, wizbini);
					} else if (modParam.getCmd().equalsIgnoreCase("get_course_list")) {
						mb.getCourseList(con);
					} else if (modParam.getCmd().equalsIgnoreCase("get_course_info")) {
						mb.getCourseInfo(con);
					} else if (modParam.getCmd().equalsIgnoreCase("enroll_course")) {
						mb.enrollCourse(con);
					} else if (modParam.getCmd().equalsIgnoreCase("upload_status")) {
						mb.uploadStatus(con);
					}
				}
			}
        } catch (cwSysMessage ce) {
            try {
                con.rollback();
                msgBox(ServletModule.MSG_STATUS, ce, modParam.getUrl_failure(),
                        out);
            } catch (SQLException sqle) {
                out.println("SQL error: " + sqle.getMessage());
            }
        } catch (qdbException qe) {
            try {
                con.rollback();
                cwSysMessage e = new cwSysMessage(qe.getMessage());
                msgBox(ServletModule.MSG_STATUS, e, modParam.getUrl_failure(),
                        out);
            } catch (SQLException sqle) {
                out.println("SQL error: " + sqle.getMessage());
            }
        } 
    }
}
