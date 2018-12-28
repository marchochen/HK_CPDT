package com.cwn.wizbank.web.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;

import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.services.AcRoleService;
import com.cwn.wizbank.web.WzbApplicationContext;

public class RoleTag extends TagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String rolExtIds;
	private boolean onlyModel;
	private boolean when;

	/**
	 * EVAL_BODY_INCLUDE：告诉服务器正文的内容，并把这些内容送入输出流 SKIP_BODY：告诉服务器不要处理正文内容
	 * EVAL_PAGE：让服务器继续执行页面 SKIP_PAGE：让服务器不要处理剩余的页面
	 * EVAL_BODY_AGAIN：让服务器继续处理正文内容，只有doAfterBody方法可以返回
	 * EVAL_BODY_BUFFERED：BodyTag接口的字段，在doStartTag()返回
	 * EVAL_BODY_INCLUDE、SKIP_BODY一般由doStartTag
	 * ()返回，而EVAL_PAPGE、SKIP_PAGE由doEndTag()返回。
	 */
	@Override
	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}

	@Override
	public int doStartTag() throws JspException {
		AcRoleService acRoleService = (AcRoleService) WzbApplicationContext.getBean("acRoleService");
		loginProfile prof = (loginProfile) this.pageContext.getSession().getAttribute("auth_login_profile");
		if (prof != null) {
			if (!StringUtils.isEmpty(rolExtIds)) {
				if(onlyModel) {
					if(acRoleService.isRoleOnly(prof.usr_ent_id, rolExtIds) == when){
						return EVAL_BODY_INCLUDE;
					}
				} else {
					String[] rols = rolExtIds.split(",");
					if (acRoleService.hasRole(prof.usr_ent_id, rols)) {
						return EVAL_BODY_INCLUDE;
					}
				}
			}
		}
		return SKIP_BODY;
	}

	@Override
	public void release() {
		super.release();
		rolExtIds = null;
		onlyModel = false;
	}

	public String getRolExtIds() {
		return rolExtIds;
	}

	public void setRolExtIds(String rolExtIds) {
		this.rolExtIds = rolExtIds;
	}

	public boolean isOnlyModel() {
		return onlyModel;
	}

	public void setOnlyModel(boolean onlyModel) {
		this.onlyModel = onlyModel;
	}

	public boolean isWhen() {
		return when;
	}

	public void setWhen(boolean when) {
		this.when = when;
	}

}
