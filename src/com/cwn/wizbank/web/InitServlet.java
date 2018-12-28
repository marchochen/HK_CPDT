/**
 * 
 */
package com.cwn.wizbank.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.utils.CommonLog;
import com.cwn.wizbank.utils.LabelContent;

/**
 * 初始加载操作
 * @author leon.li
 * 2014-7-29 上午10:24:32
 */
public class InitServlet extends HttpServlet {

	public static Logger logger = LoggerFactory.getLogger(LabelContent.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void destroy() {
		super.destroy();
	} 

	@Override
	public void init() throws ServletException {
		String filePath= this.getServletConfig().getServletContext().getRealPath("/"); 
		//System.out.println("filePath>>>>>>>>"+filePath);
		filePath += cwUtils.SLASH+"static"+cwUtils.SLASH+"js"+cwUtils.SLASH+"i18n";
		//初始化label
		try {
			new LabelContent(filePath);
		} catch (Exception e) {
			CommonLog.error(e.getMessage(),e);
		}
		
		super.init();
	}

	
}
