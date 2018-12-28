<!-- #include file = "utils/utils.asp" -->
<%Response.Expires=0%>
<html>
    <head>
    	<meta http-equiv="Content-Type" content="text/html; charset=<%= encoding %>"/>
    </head>
    <body onload="document.form1.submit();">
        <form method="post" name="form1" action="<%= lmsQdbServletURL %>">
            <input type="hidden" name="cmd" value="aff_auth"/>
            <input type="hidden" name="mode" value="BASIC"/>
            <input type="hidden" name="usr_id" value="<%= usrID %>"/>
            <input type="hidden" name="site_id" value="<%= siteId %>"/>
            <input type="hidden" name="url_failure" value="<%= urlFailure %>"/>
            <input type="hidden" name="login_role" value="<%= lrnRole %>"/>
            <input type="hidden" name="url_success" value="<%=request("url_success")%>"/>
            <input type="hidden" name="module" value="<%= module %>">
        </form>
    </body>
</html>