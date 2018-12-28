package com.cwn.wizbank.web.tag;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;

import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.utils.LabelContent;

public class TitleTag extends TagSupport {
	
	/**
	 *  对应的功能名字
	 */
	private String function;
	
	private String encoding;

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	@Override
	public int doStartTag() throws JspException {
		
		try{
				JspWriter out = this.pageContext.getOut();
				HttpServletRequest request = (HttpServletRequest)this.pageContext.getRequest();
				String contextPath = request.getContextPath();
				
				if(StringUtils.isEmpty(function)){
					out.println("");
				}
				
				else{
					String htmlTemplate = "<div class=\"wzb-banner-bg01\" style=\"opacity:1;background:{{background}};\">" +
											    "<div class=\"wzb-banner\"><i class=\"fa wzb-banner-icon {{icon}}\"></i>{{label}}</div>" +
											    "<span class=\"wzb-banner-right\"><img src=\""+contextPath+"/static/images/{{gif}}\"></span>"+
										  "</div>";
					
					loginProfile prof = (loginProfile) this.pageContext.getSession().getAttribute("auth_login_profile");
					if(StringUtils.isEmpty(encoding)) {
						if(prof != null){
							encoding = prof.cur_lan;
						} else {
							encoding = WizbiniLoader.getInstance(this.pageContext.getSession().getServletContext()).cfgSysSkinList.getDefaultLang();
						}
					}
					
					String bg = "",icon = "",gif="";
					String functionName = LabelContent.get(encoding, function);
					encoding = null;
					
					if("global.FTN_AMD_SNS_MGT".equalsIgnoreCase(function.trim())) // 社区管理 
					{
						bg = "";
						icon = "fa-comments";
						gif = "wzb-b06.gif";
		
					}
					else if("global.FTN_AMD_DEMAND_MGT".equalsIgnoreCase(function.trim()))// 需求管理
					{
						bg = "#663300";
						icon = "fa-cubes";
						gif = "wzb-b02.gif";
					}
					else if("global.FTN_AMD_TRAINING_MGT".equalsIgnoreCase(function.trim()))//培训管理
					{
						bg = "#00cc33";
						icon = "fa-calendar";
						gif = "wzb-b05.gif";
					}
					else if("global.FTN_AMD_EXAM_MGT".equalsIgnoreCase(function.trim()))//考试管理
					{
						bg = "#00cc33";
						icon = "fa-columns";
						gif = "wzb-b14.gif";
					}
					else if("global.FTN_AMD_ARTICLE_MGT".equalsIgnoreCase(function.trim()))//资讯管理
					{
						bg = "#cc0099";
						icon = "fa-globe";
						gif = "wzb-b01.gif";
					}
					else if("global.FTN_AMD_PLAN_MGT".equalsIgnoreCase(function.trim()))//计划管理
					{
						bg = "#006600";
						icon = "fa-list-alt";
						gif = "wzb-b03.gif";
					}
					else if("global.FTN_AMD_USR_INFO_MGT".equalsIgnoreCase(function.trim()))//用户管理
					{
						bg = "#336666";
						icon = "fa-child";
						gif = "wzb-b09.gif";
					}
					else if("global.FTN_AMD_BASE_DATA_MGT".equalsIgnoreCase(function.trim()))//基础数据管理
					{
						bg = "#999900";
						icon = "fa-suitcase";
						gif = "wzb-b10.gif";
					}
					else if("global.FTN_AMD_SYSTEM_SETTING_MGT".equalsIgnoreCase(function.trim()))//系统设置
					{
						bg = "#19b2ed";
						icon = "fa-wrench";
						gif = "wzb-b12.gif";
					}
					else if("global.FTN_AMD_STUDY_MAP_MGT".equalsIgnoreCase(function.trim()))//学习地图管理
					{
						bg = "#996699";
						icon = "fa-maxcdn";
						gif = "wzb-b04.gif";
					}
					else if("global.FTN_AMD_FACILITY_MGT".equalsIgnoreCase(function.trim())) // 设施管理
					{
						bg = "#996699";
						icon = "fa-suitcase";
						gif = "wzb-b11.gif";
					}
					else if("global.FTN_AMD_TRAINING_REPORT_MGT".equalsIgnoreCase(function.trim()))//培训报表管理
					{
						bg = "rgb(153, 102, 153)";
						icon = "fa-table";
						gif = "wzb-b08.gif";
					}
					else if("global.FTN_AMD_KNOWLEDGE_MGT".equalsIgnoreCase(function.trim())) // 知识管理。。。。
					{
						bg = "#83c62b";
						icon = "fa-life-ring";
						gif = "wzb-b07.gif";
					}
					else if("global.FTN_SYS_MESSAGE".equalsIgnoreCase(function.trim())) // 站内信。。。。
					{
						bg = "#83c62b";
						icon = "fa-envelope-o";
						gif = "wzb-b13.gif";
					}
					else if("global.FTN_AMD_LIVE_MAIN".equalsIgnoreCase(function.trim())) // 直播管理 
					{
						bg = "#00cc33";
						icon = "fa-video-camera";
						gif = "wzb-b05.gif";
					}
					else if("global.FTN_AMD_CPT_D_MGT".equalsIgnoreCase(function.trim())) // CPT/D 牌照注册管理
					{
						bg = "#00cc33";
						icon = "fa-compass";
						gif = "wzb-b05.gif";
					}
					
					htmlTemplate = htmlTemplate.replace("{{background}}", bg)
							                   .replace("{{label}}", functionName)
							                   .replace("{{icon}}", icon)
							                   .replace("{{gif}}", gif);
					out.print(htmlTemplate);
				}			
		} catch(Exception e) {
			 throw new JspException(e.getMessage());
		}
		return SKIP_BODY;	
		
	}
	
    @Override
    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }
 
    @Override
    public void release() {
        super.release();
        this.encoding=null;
        this.function = null;
    }
}
