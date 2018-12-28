<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>

<!DOCTYPE html>
<html>
<head>
<title></title>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-learner.css" />
<script type="text/javascript" src="${ctx}/js/jquery.prompt.js"></script>
<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<script type="text/javascript" src="${ctx}/static/js/front/course.js"></script>
<script type="text/javascript" src="${ctx}/static/js/front/sns.js"></script>
<script type="text/javascript" src="${ctx}/static/js/front/jquery.sns.js"></script>
<script type="text/javascript">
var meId = '${prof.usr_ent_id}';
$(function() {
    $("#doingList").table({
		url : '${ctx}/app/personal/getLikeList/${usrEntId}',
		colModel : dtModel,
		rp : 10,
		hideHeader : true,
		usepager : true
	});
});

var dtModel = [ {
	format : function(data) {
		$.extend(data,
				{crtTime : data.crtTime.substring(0,16),
			     title :data.title?data.title.replace(/"/g,""):''})
		return $('#likeTemplate').render(data);
	}
} ];

</script>
<!-- 动态模板 -->
<jsp:include page="../common/doingTemplate.jsp"></jsp:include>
</head>

<body>
<div class="xyd-wrapper">
	<div id="main" class="xyd-main clearfix">
		<jsp:include page="personalMenu.jsp"></jsp:include>
		
		<div class="xyd-article">
             <div class="wzb-title-12"><lb:get key="valuation_list"/></div>
             
             <div id="doingList"></div>
		</div> <!-- xyd-article End-->
	
	</div>
</div> <!-- xyd-wrapper End-->
</body>
</html>