<%@ page isELIgnored="false" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.cw.wizbank.qdb.*"%>
<%@ include file="../common/taglibs.jsp"%>
<%@ include file="../common/meta.jsp"%>
<!DOCTYPE html>
<html>
	<head>
		<script type="text/javascript">
			function go() {
				<%
				    String url = qdbAction.static_env.URL_RELOGIN;
				    if (url != null) {
				        if (url.startsWith("http://") || url.startsWith("https://")) {
				        } else {
				            url = url.substring(url.lastIndexOf("../") + 3);
				        }
				    }
				%>
				var url = '<%=url%>';
				if(!url.indexOf("http://") == 0 && !url.indexOf("https://") == 0){
					url ='${ctx}' + '/' + url;
				}
			    window.location.href = url;
			}
		</script>
	</head>
	<body>
		<div>
			<div class="clearfix" style="background: #fff">
				<div class="mode">
				     <div class="losepage sessionTimeOut losepage-out">
				          <div class="losepage_tit"><lb:get key="global_operate_error" /></div>
				          <div class="losepage_info"><lb:get key="error_landed_somewhere_else" /></div>
				          <div class="losepage_desc"><input name="frmSubmitBtn" value="<lb:get key="button_ok"/>" type="button" class="btn wzb-btn-blue margin-right10 wzb-btn-big" onclick="go()"></div>
				     </div>
				</div>
			</div>
		</div>
	</body>
</html>