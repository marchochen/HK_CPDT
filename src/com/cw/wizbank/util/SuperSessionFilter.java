package com.cw.wizbank.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.util.StringUtils;

import com.cw.wizbank.Application;
import com.cw.wizbank.accesscontrol.acSite;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbAction;
import com.cwn.wizbank.utils.CommonLog;




/*
public class SuperSessionFilter implements Filter {


    ServletContext servletContext = null;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.servletContext = filterConfig.getServletContext();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String appContext = "EES";
        String path = request.getRequestURI();
        path = path.substring(path.indexOf(appContext) + appContext.length());
//        System.out.println("1. Path=" + path);
//        System.out.println(servletContext.getRealPath(""));
        HttpSession sess = request.getSession(false);
        loginProfile prof = null;
        if(sess != null){
	        try {
	            prof = (loginProfile)sess.getAttribute(qdbAction.AUTH_LOGIN_PROFILE);
	        }
	        catch(ClassCastException e) {
	            prof = new loginProfile();
	            prof.readSession(sess);
	            sess.setAttribute(qdbAction.AUTH_LOGIN_PROFILE, prof);
	        }
        }

        if (prof ==null || prof.usr_id == null) {
            response.sendError(response.SC_NOT_FOUND);
            return;
        }

        File target = new File(servletContext.getRealPath(""), path);
        if (target.exists()) {
            FileInputStream fin = null;
            OutputStream binout = null;
            BufferedInputStream bin = null;
            int size = 0;
            try {
                fin = new FileInputStream(target);
                byte[] buffer = new byte[4096];
                bin = new BufferedInputStream(fin, 4096);
                binout = response.getOutputStream();
                while ((size = bin.read(buffer, 0, buffer.length)) != -1) {
                    binout.write(buffer, 0, size);
                }
            } catch (FileNotFoundException e) {
                try {
                    response.sendError(404, e.getMessage());
                } catch (Exception ex) {
                }
            } catch (IOException e) {
                try {
                    response.sendError(500, e.getMessage());
                } catch (Exception ex) {
                }
            } finally {
                try {
                    if (bin != null) bin.close();
                } catch (Exception ex) {
                }
                try {
                    if (fin != null) fin.close();
                } catch (Exception ex) {
                }
                try {
                    if (binout != null) binout.close();
                } catch (Exception ex) {
                }
            }
        }
        return;
    }
    

    @Override
    public void destroy() {

    }
}
*/



public class SuperSessionFilter implements Filter {
	
	 ServletContext servletContext = null;

	    @Override
	    public void init(FilterConfig filterConfig) throws ServletException {
	        this.servletContext = filterConfig.getServletContext();
	    }
	
		
	public void doFilter(ServletRequest servletRequest,ServletResponse servletResponse, FilterChain chain)throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		HttpSession sess = request.getSession(false);
		loginProfile prof = null;
		WizbiniLoader wizbini = WizbiniLoader.getInstance();
		final String regxpForHtml = "<([^>]*)>"; // 所有以<开头以>结尾的标签   

		if (sess != null) {
			//  取到登录的标识
			
			try {
				prof = (loginProfile) sess.getAttribute(qdbAction.AUTH_LOGIN_PROFILE);
			} catch (ClassCastException e) {
				prof = new loginProfile();
				prof.readSession(sess);
				sess.setAttribute(qdbAction.AUTH_LOGIN_PROFILE, prof);
			}
		}
		// 请求的url
		StringBuffer url = request.getRequestURL();
		String fullPath = url.toString();
		if(fullPath.indexOf("/app/") != -1
				|| fullPath.indexOf("/qdbAction") != -1
				|| fullPath.indexOf("/aeAction") != -1
				|| fullPath.indexOf("/Dispatcher") != -1
			) {
			Enumeration e = request.getParameterNames();
			StringBuilder p = new StringBuilder();
			int index = 1;
			while(e.hasMoreElements()){
				String name = (String) e.nextElement();
				String value = request.getParameter(name);
				if(index > 1){
					p.append("&");
				}
				if("usr_pwd".equals(name)){
	                p.append(name).append("=").append("   ");
				}else{
                    p.append(name).append("=").append(value);
				}
				index++;
			}
			System.out.println("Action   :                    " + request.getRequestURI() + "    " + p.toString());
		}
	
		//防御CSRF(跨站请求伪造)
		//在 Filter 中验证 Referer
		String cmd = request.getParameter("cmd");
		String referer = request.getHeader("Referer");
		String token = request.getParameter("token");
		String callback = request.getParameter("callback");
		
		/*
		 * 这里做过滤 callback 的字符过滤，过滤所有以<开头 以>结尾的标签 , 此处做法是防御XSS,如：
		 * callback=angular.callbacks._0%27"()%26%25<acx><ScRiPt%20>C8se(955%204)</ScRiPt>&developer=mobile
		 */
		if(callback!=null && callback.length()>0){
			callback = cwUtils.unescHTML(callback);
			Pattern p =  Pattern.compile(regxpForHtml); 
			Matcher  m = p.matcher(callback); 
			boolean IllegalCharacter = m.find(); 

		if(IllegalCharacter){
				response.sendError(response.SC_NOT_FOUND);
				return;
			}
		}
		
		String host  = null;
		if(fullPath != null && fullPath.indexOf("//") > 0){
			host = fullPath.substring(fullPath.indexOf("//") + 2);
			if(host.indexOf("/") > 0){
				host = host.substring(0,host.indexOf("/"));
			}
		}
		
		if((token != null && tokenValidate(token)) || (fullPath != null && fullPath.indexOf("mobile") >=0)){
			//移动端如果带有效的token访问资源也放行
			//移动端静态HTML页面放行
		}
		else{
			//cmd==null 是学员端,cmd=='aff_auth'是排除SSO登录  (HK7-11没使用SSO登录,但此处先保留排除)
			if((cmd == null) || (cmd != null && !cmd.equalsIgnoreCase("aff_auth")) ){
	    
	            // Check if Request.getRemoteHost() and Request.getHeader("REFERER") contains
	            // the string stored in acSite.ste_domain which stored a list of valid sites
	            // separaotr by the "[|]" delimiter
	       
	            // 这里的trust_domain来源于acSite表中的ste_domain列的值
				/*
				 出于安全的验证的考虑，要使LMS接收第三方发过来请求，必须在预先在LMS中设定好第三方系统的域名（如果有多个，则用[|]分开），设置方法如下：
				update acsite set ste_trusted = 1 , ste_domain = 'google.ocm[|]baidu.com' where ste_ent_id = 1
				 */
				if (referer != null) {
				     boolean bValid = false;
				     //referer判断访问来源
				     if (referer != null && referer.toLowerCase().indexOf(host) >= 0) {
	                     bValid = true;
	                 }else{
			            if (qdbAction.trust_domain != null && !qdbAction.trust_domain.trim().equals("")) { 
			                String[] validDomains = dbUtils.split(qdbAction.trust_domain, acSite.DOMAIN_SEPARATOR);
			                for (int i=0; i<validDomains.length; i++) {
			                    if (referer != null && referer.toLowerCase().indexOf(validDomains[i].toLowerCase()) >= 0) {
			                        bValid = true;
			                    }
			                }
			            }else{
			            	bValid = true;
			            }
	                 }
					  if(!bValid){
							System.out.println("fullPath:"+fullPath);
							System.out.println("Referer:"+referer);
							System.out.println("qdbAction.trust_domain:"+qdbAction.trust_domain);
							response.sendError(response.SC_NOT_FOUND);
							return;
					  }
				} 
			}
		}

		boolean is_target_res = false;
		if(fullPath.indexOf("/app/") != -1){
			System.out.println("                          " + request.getRequestURI());
		}

		// 拦截没有登录用户访问平台资源
		
		
		if (fullPath.indexOf("/sgp_resource/") > 0
				|| fullPath.indexOf("/item/") > 0
				|| fullPath.indexOf("/facility/") > 0
				|| fullPath.indexOf("/object/") > 0
				//|| fullPath.indexOf("/content/") > 0  解决IOS10 保存不了session的bug
				//|| fullPath.indexOf("/resource/") > 0
				|| fullPath.indexOf("/attachment/") > 0
				|| fullPath.indexOf("/plan/") > 0) {
			
			is_target_res = true;
			
		}

		if (is_target_res && (prof == null || prof.usr_id == null)) {
			
			if(tokenValidate(token) ||  //移动端如果带有效的token访问资源也放行
				//由于i doc view 插件需要远程访问resource资源故如果是i doc view服务器访问的放行
				(org.apache.commons.lang.StringUtils.isEmpty(Application.I_DOC_VIEW_PREVIEW_IP.trim()) ||
				Application.I_DOC_VIEW_PREVIEW_IP.trim().equalsIgnoreCase(request.getRemoteAddr()))
			 ){
				chain.doFilter(request, response);
			}else{
				response.sendError(response.SC_NOT_FOUND);
				return;
			}
			
		} else {
			chain.doFilter(request, response);
		}

	}
	
	/**
	 * 判断移动端token是否有效
	 * @param token
	 * @return
	 * @throws ServletException 
	 */
	private boolean tokenValidate(String token) throws ServletException {
		
		if(StringUtils.isEmpty(token)){
			return false;
		}
		
		cwSQL sqlCon = new cwSQL();
		Connection con = null;
		try {
			sqlCon.setParam(qdbAction.wizbini);
			con = sqlCon.openDB(false);
			return dbRegUser.isValidaToken(con, token);
		} catch (Exception e) {
			CommonLog.error(e.getMessage(),e);
		} finally{
			if(con != null){
				try{
					if(!con.isClosed()){
						con.close();
					}
				}catch(SQLException se){
					throw new ServletException(se);
				}finally{
					con = null;
				}
			}
		}
		return false;
	}


	@Override
    public void destroy() {
    }
}

/*
public class SuperSessionFilter extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public static final String PARAM_BUFFER_SIZE = "bufferSize";
	public static final String PARAM_FILES_ROOT = "fileRoot";

	private static WizbiniLoader wizbini = null;

	private static final boolean REQUIRE_CONTENT_LENGTH = true;
	private int defaultBufferSize;
	private int cacheTime;
	private String appContext;
	private Hashtable<String, String> hasPath = null;

	public SuperSessionFilter() {
		defaultBufferSize = 4096;
		cacheTime = 30;
	}

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		System.out.println("SampleDownloader.init() START...");

		// get buffer size parameter
		String s = getInitParameter(PARAM_BUFFER_SIZE);
		if (s != null) {
			try {
				int intBufferSize = Integer.parseInt(s);
				defaultBufferSize = intBufferSize;
			} catch (NumberFormatException numberformatexception) {
			}
		}
		try {
			wizbini = WizbiniLoader.getInstance(config);

			hasPath = new Hashtable<String, String>();
			hasPath.put("resource", wizbini.getFileUploadResDirAbs());
			hasPath.put("sgp_resource", wizbini.getFileUploadSgpResDisAbs());
			hasPath.put("item", wizbini.getFileUploadItmDirAbs());
			hasPath.put("facility", wizbini.getFileUploadFacDirAbs());
			hasPath.put("object", wizbini.getFileUploadObjDirAbs());
			hasPath.put("content", wizbini.getFileUpdateContentDirAbs());
			hasPath.put("temp", wizbini.getFileUploadTmpDirAbs());
			hasPath.put("user", wizbini.getFileUploadUsrDirAbs());
			hasPath.put("announ", wizbini.getFileUploadAnnDirAbs());
			hasPath.put("certificate", wizbini.getFileCertImgDirAbs());
			hasPath.put("editor", wizbini.getFileEditorDirAbs());
			hasPath.put("poster", wizbini.getFilePosterDirAbs());
			hasPath.put("plan", wizbini.getFilePlanDirAbs());
			hasPath.put("log", wizbini.getSystemLogDirAbs());

			appContext = "EES";
		} catch (cwException e) {
			e.printStackTrace();
			System.out.println("init() exception :" + e.getMessage());
			throw new ServletException(e.getMessage());
		}
		Enumeration<String> hasPathEnum = hasPath.keys();
		while (hasPathEnum.hasMoreElements()) {
			String hasPathKey = (String) hasPathEnum.nextElement();
			System.out.println("hasPath[" + hasPathKey + "]=" + hasPath.get(hasPathKey));
		}

		System.out.println("SampleDownloader.init() END");
	}

	private boolean setResponseHeaders(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse, File file, boolean flag, boolean octetflag) throws ServletException, IOException {
		httpservletresponse.setHeader("Pragma", "No-cache");
		httpservletresponse.setHeader("Cache-Control", "max-age=" + cacheTime);
		httpservletresponse.setDateHeader("Expires", 1);

		String s = getServletContext().getMimeType(file.getAbsolutePath());

		if (s != null && !octetflag)
			httpservletresponse.setContentType(s);
		else
			httpservletresponse.setContentType("application/download");
		httpservletresponse.setContentLength((new Long(file.length())).intValue());
		return true;
	}

	//Process the HTTP Post request
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	//Process the HTTP Get request
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String context = appContext;

		String filename = null;

		// set the target
		File target = null;
		String path = request.getRequestURI();
		System.out.println("[SampleDownloader] 1. Path=" + path);
		path = path.substring(path.indexOf(context) + context.length());
		System.out.println("[SampleDownloader] 2. Path=" + path);
		int start_index = path.indexOf("/") + 1;
		int end_index = path.indexOf("/", start_index);
		if (start_index > 0 && end_index > 0) {
			path = path.substring(start_index, end_index);
		}
		System.out.println("[SampleDownloader] 3. Path=" + path);
		filename = new String(request.getPathInfo());
		System.out.println("[SampleDownloader] filename=" + filename);
		System.out.println("[SampleDownloader] hasPath.get(path)=" + hasPath.get(path));
		target = new File(hasPath.get(path) + filename);

		setResponseHeaders(request, response, target, !REQUIRE_CONTENT_LENGTH, true);
		writeFile(request, response, target);
	}

	private synchronized void writeFile(HttpServletRequest request, HttpServletResponse response, File target) throws ServletException, IOException {
		FileInputStream fin = null;
		OutputStream binout = null;
		BufferedInputStream bin = null;
		int size = 0;

		try {
			fin = new FileInputStream(target);
			byte[] buffer = new byte[defaultBufferSize];
			bin = new BufferedInputStream(fin, defaultBufferSize);
			binout = response.getOutputStream();
			while ((size = bin.read(buffer, 0, buffer.length)) != -1) {
				binout.write(buffer, 0, size);
			}
		} catch (FileNotFoundException e) {
			try {
				response.sendError(404, e.getMessage());
			} catch (Exception ex) {
			}
		} catch (IOException e) {
			try {
				response.sendError(500, e.getMessage());
			} catch (Exception ex) {
			}
		} finally {
			try {
				if (bin != null)
					bin.close();
			} catch (Exception ex) {
			}
			try {
				if (fin != null)
					fin.close();
			} catch (Exception ex) {
			}
			try {
				if (binout != null)
					binout.close();
			} catch (Exception ex) {
			}
		}

	}

	public void destroy() {
	}
}
*/