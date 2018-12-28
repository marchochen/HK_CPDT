package com.cw.wizbank.filter;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cw.wizbank.Application;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.CurrentActiveUser;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbAction;
import com.cw.wizbank.qdb.qdbEnv;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cwn.wizbank.utils.CommonLog;
/**
 *  
 *  禁止同一用户重复登录
 *  	用户登录，在LoginModule>SessionListener>CurrentActiveUser>中会清空上一次登录的Session
 *      在此类中会判断是否存在当前session。如果不存在，则跳到首页
 * @author Leon.li
 */
public class LoginFilter implements Filter {
	
	FilterConfig config = null;
    private static WizbiniLoader wizbini = null;
    private static qdbEnv static_env = null;
    private static boolean flag = false;
    
	public void init(FilterConfig conf) throws ServletException {
		this.config = conf;
        try {
            wizbini = WizbiniLoader.getInstance(config);
			
			static_env = (qdbEnv) config.getServletContext().getAttribute(WizbiniLoader.SCXT_STATIC_ENV);
			
			if (static_env == null) {
				static_env = new qdbEnv();
				static_env.init(wizbini);
				config.getServletContext().setAttribute(WizbiniLoader.SCXT_STATIC_ENV, static_env);
			}
		} catch (cwException e) {
			CommonLog.error(e.getMessage(),e);
			throw new ServletException(e.getMessage());
		}
	}

	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		
		if(Application.MULTIPLE_LOGIN){
			chain.doFilter(req, res);
			return;
		}

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		HttpSession session = request.getSession();
		Connection con = null;

		loginProfile  prof = (loginProfile)session.getAttribute(qdbAction.AUTH_LOGIN_PROFILE);

		/*  系统用户设置的原则 <=8 系统用户 
		 *  prof is null user_ent_id > 8 
		 */	
		if(prof == null || prof.usr_ent_id <= 8){
			chain.doFilter(req, res);
			return;
		}
    	try {
             cwSQL sqlCon = new cwSQL();
             sqlCon.setParam(wizbini);
             con = sqlCon.openDB(false);
            //此处执行逻辑，判断当前session是否存在，不存在则转到首页
			if(prof != null && CurrentActiveUser.userLoginCount(con,prof.usr_ent_id) > 0 && ! CurrentActiveUser.isSessionExisted(con,session.getId())) {
				CommonLog.info("session不存在则转到首页");

	            if (session != null)
	            	session.invalidate();
	           
				response.sendRedirect(static_env.URL_SESSION_TIME_OUT);
				return;		
			}
		}catch(Exception e){
			CommonLog.error(e.getMessage(),e);
			try {
				con.rollback();
			} catch (SQLException re) {
				throw new ServletException(re.getMessage());
			}
        }finally {
            try {
                con.commit();
                if(con != null && !con.isClosed())
                con.close();
            }
            catch (SQLException sqle) {
            	throw new ServletException(sqle.getMessage());
            }
        }
		chain.doFilter(req, res);
	}

	public void destroy() {

	}

}
