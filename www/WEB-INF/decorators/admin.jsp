<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../views/common/taglibs.jsp"%>
<%@ include file="../views/admin/common/meta.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title><decorator:title default="Wizbank" /></title>
<decorator:head />
</head>
<body <decorator:getProperty property="body.onload" writeEntireProperty="true" /> >
    <div class="wzb-body">
        <jsp:include page="../views/admin/common/menu.jsp"></jsp:include>

        <div class="wzb-wrapper">
            <div class="wzb-main">
                <decorator:body />

                <!-- wzb-panel End-->
            </div>
            <!-- wzb-main End-->

        </div>
        <!-- wzb-wrapper End-->

      <%@ include file="../views/common/footer.jsp"%>
    </div>
</body>
</html>