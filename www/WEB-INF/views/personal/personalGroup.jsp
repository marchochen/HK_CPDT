<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<%@ include file="../common/template.jsp"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-learner.css" />
<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<script type="text/javascript" src="${ctx}/static/js/front/course.js"></script>
<title></title>
<script type="text/javascript">
	$(function() {
		$(".wzb-find").table({
			url : '${ctx}/app/personal/getPersonalGroupList/${usrEntId}',
			gridTemplate : function(data){
				p = {
					image : data.card_actual_path,
					s_grp_title : data.s_grp_title,
					member_total : data.member_total,
					message_total : data.message_total,
					href : '${ctx}/app/group/groupDetail/' + wbEncrytor().cwnEncrypt(data.s_grp_id)
				}
				return $('#group-template').render(p);
				
			},
			rowCallback : function(){
				photoDoing();
			},
			view : 'grid',
			rowSize : 3,
			rp : 12,
			showpager : 5,
			hideHeader : true,
			usepager : true,
			trLine : false
		})
	})
	
</script>
</head>
<body>
<div class="xyd-wrapper">
	<div id="main" class="xyd-main clearfix">
		<jsp:include page="personalMenu.jsp"></jsp:include>
		
		<div class="xyd-article">
             <div class="wzb-title-12"><c:if test="${isMeInd == 'true'}"><a class="pull-right font-size14" href="${ctx}/app/group/groupFind/0"><lb:get key="group_find"/></a></c:if> <lb:get key="group_list"/></div>
            
             <div class="wzb-find clearfix" style="margin-top: 15px;"></div>
		</div> <!-- xyd-article End-->
	
	</div>
</div> <!-- xyd-wrapper End-->
</body>
</html>