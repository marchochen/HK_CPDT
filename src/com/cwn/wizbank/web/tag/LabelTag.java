package com.cwn.wizbank.web.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;

import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.utils.LabelContent;

public class LabelTag extends TagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String key;
	private String encoding;

	/**
	 * EVAL_BODY_INCLUDE：告诉服务器正文的内容，并把这些内容送入输出流 
	 * SKIP_BODY：告诉服务器不要处理正文内容 
	 * EVAL_PAGE：让服务器继续执行页面 
	 * SKIP_PAGE：让服务器不要处理剩余的页面 
	 * EVAL_BODY_AGAIN：让服务器继续处理正文内容，只有doAfterBody方法可以返回 
	 * EVAL_BODY_BUFFERED：BodyTag接口的字段，在doStartTag()返回 
	 * EVAL_BODY_INCLUDE、SKIP_BODY一般由doStartTag()返回，而EVAL_PAPGE、SKIP_PAGE由doEndTag()返回。
	 */
	@Override
	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}

	@Override
	public int doStartTag() throws JspException {
		try{
			JspWriter out = this.pageContext.getOut();
			if(StringUtils.isEmpty(key)) {
				out.println("");
			} else {
				loginProfile prof = (loginProfile) this.pageContext.getSession().getAttribute("auth_login_profile");
				if(StringUtils.isEmpty(encoding)) {
					if(prof != null){
						encoding = prof.cur_lan;
					} else {
						encoding = WizbiniLoader.getInstance(this.pageContext.getSession().getServletContext()).cfgSysSkinList.getDefaultLang();
					}
				}
				
				
				
				
				out.print(LabelContent.get(encoding, key));
				encoding = null;
			}
		} catch(Exception e) {
			 throw new JspException(e.getMessage());
		}
		return SKIP_BODY;
	}

	@Override
	public void release() {
		super.release();
		key = null;
		encoding = null;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	
	
}
