<!-- #include file = "utils/utils.asp" -->
<%Response.Expires=0%>
<html>
    <head>
    	<meta http-equiv="Content-Type" content="text/html; charset=<%= encoding %>"/>
        <script language="javascript">
            var URL_SUCCESS = '../htm/course_detail.htm?itm_id=<%=request("itm_id")%>';
            function submitForm(){
                document.form1.url_success.value = URL_SUCCESS;
                document.form1.submit();
            }
        </script>
    </head>
    <body onload="submitForm()">
        <form method="post" name="form1" action="<%= lmsQdbServletURL %>">
            <input type="hidden" name="cmd" value="aff_auth">
            <input type="hidden" name="mode" value="BASIC">
            <input type="hidden" name="site_id" value="<%= siteId %>">
            <input type="hidden" name="usr_id" value="<%= usrID %>">
            <input type="hidden" name="url_failure" value="<%= urlFailure %>">
            <input type="hidden" name="login_role" value="<%= lrnRole %>">
            <input type="hidden" name="url_success" value=""/>
            <input type="hidden" name="module" value="<%= module %>">
        </form>
    </body>
</html>