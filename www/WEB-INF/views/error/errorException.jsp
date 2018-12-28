<%@ page isELIgnored="false" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.io.*"%>
<%@ include file="../common/taglibs.jsp"%>
<%@ include file="../common/meta.jsp"%>

<!DOCTYPE html>
<html>
<head>
<script type="text/javascript">
	function go() {
		history.go(-1);
	}
</script>
</head>
<body>
<%		
		loginProfile prof = (loginProfile) pageContext.getAttribute("prof");
		String msg = "server error";
		Exception ex = (Exception) request.getAttribute("exception");
		if (ex != null) {
			msg = ex.getMessage();
		}
	%>
<div class="" style="background:#fff;height:85%;${prof.current_role == 'NLRN_1'?'margin:200px 0 0 0;':''}">
<div class="clearfix" style="padding-left:30px;${prof.current_role != 'NLRN_1'?'padding:200px 0;':''}">
<div class="mod">
     <div class="losepage authority" style="height:140px;padding-top:12px;">
          <div class="losepage_tit"><%=msg %></div>
          <div class="losepage_info"></div>
          <div class="losepage_desc"><a href="javascript:go()" title="<lb:get key='button_back'/>"><lb:get key="button_back"/> </a></div>
     </div>
</div> <!-- mod End-->
</div>

</div> <!-- xyd-wrapper End-->

</body>
</html>