<%@ page isELIgnored="false" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<%@ page import="com.cwn.wizbank.utils.LabelContent" %>
<%@ page import="com.cw.wizbank.qdb.loginProfile" %>
<%@ page import="com.cw.wizbank.config.WizbiniLoader" %>
<%
	loginProfile prof = (loginProfile) pageContext.getAttribute("prof");
	String lang;
	if (prof != null) {
		lang = prof.cur_lan;
	}else{
		lang =  WizbiniLoader.getInstance(pageContext.getSession().getServletContext()).cfgSysSkinList.getDefaultLang();
	}
%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${ctx}/static/css/base.css"/>
<script type="text/javascript">
	function go() {
		history.go(-1);
	}
	
	var maxtime = 3; //单位：秒

	setInterval(function (argument) {
		maxtime--;
		document.getElementById("second").innerHTML = maxtime;
		if(maxtime==0)
		{
			 go();
		}
	},1000)
	
</script>
</head>
<body>
	<%
		String msg = "server error";
		Exception ex = (Exception) request.getAttribute("exception");
		if (ex != null) {
			msg = ex.getMessage();
			if(msg != null) {
				msg = LabelContent.get(lang, msg);
			}
			
		}
	%>
	<div class="" style="background:#fff;">
		<div class="clearfix" style="padding-left:30px;height:100%">
		<div class="mod" style="margin: 20% 0;">
		     <div class="losepage authority">
		          <div class="losepage_tit"><lb:get key="global_operate_error"/></div>
		          <div class="losepage_info"><%=msg%></div>
		          <div class="losepage_info"><lb:get key="lab_operate_forward_message" /></div>
		          <div class="losepage_desc"><a style="background:#00aeef;padding: 5px 20px;" class="btn wzb-btn-blue" href="javascript:go()" title="<lb:get key='button_back'/>"><lb:get key="button_back"/> </a></div>
		     </div>
		</div> <!-- mod End-->
		</div>
		
	</div> <!-- xyd-wrapper End-->
</body>
</html>