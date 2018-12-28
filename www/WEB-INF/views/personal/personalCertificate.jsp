<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<%@ include file="../common/template.jsp"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-learner.css" />
<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<script type="text/javascript" src="${ctx}/js/wb_certificate.js"></script>
<title></title>
<script type="text/javascript">
	cert = new wbCertificate;
	$(function(){
		$(".wbtable").table({
			url : '${ctx}/app/personal/certificateList',
			colModel : colModel,
			rp : 10,
			showpager : 5,
			usepager : true
		})
	})
	
	var colModel = [ {
		display : fetchLabel('certificate_title'),
		width : '25%',
		format : function(data) {
			p = {
				text : data.cfc_title
			};
			return $('#text-template').render(p);
		}
	}, {
		display : fetchLabel('course_title'),
		width : '35%',
		format : function(data) {
			p = {
				className : 'wzb-link02',
				href : '${ctx}/app/course/detail/' + data.aeItem.itm_id,
				title : data.aeItem.itm_title
			};
			return $('#a-template').render(p);
		}
	}, {
		display : fetchLabel('certificate_period_of_validity'),
		width : '20%',
		align : "center",
		format : function(data) {
			p = {
				text : data.cfc_end_date.substring(0,10)
			};
			return $('#text-center-template').render(p);
		}
	}, {
		display : '',
		width : '20%',
		align : "center",
		format : function(data) {
			if(compareTime(data.cfc_end_date)){
				p = {
					event : "javascript: cert.download_cert('" + data.cfc_id + "', '" + data.aeItem.itm_id + "', '" + data.app_tkh_id + "')",
					status : fetchLabel('btn_download')
				};
			}else{
				p = {
					event : "javascript: ;",
					status : fetchLabel('certificate_download_error')
				};
			}
			if(p.status=='下载'||p.status=='Download'){
				return  $('#button-template').render(p);
			}else{
				return	$('#button-template-off').render(p);
			}
		}
	} ]
	
	function compareTime(date){
		var now=new Date()
		 y=now.getFullYear()
		 m=now.getMonth()+1
		 d=now.getDate()
		 m=m<10?"0"+m:m
		 d=d<10?"0"+d:d
	  	var nowtime=  y+"-"+m+"-"+d
		if((new Date(date.replace(/-/g,"\/"))) >= (new Date(nowtime.replace(/-/g,"\/")))){
			return true;
		}
		return false;
	}
</script>
<script id="button-template" type="text/x-jsrender">
	<div class="text-right">
		<input type="button" class="btn wzb-btn-orange" onclick="{{>event}}" value="{{>status}}"/>
	</div>
</script>
<script id="button-template-off" type="text/x-jsrender">
	<div class="text-right">
		<input type="button" class="btn" onclick="{{>event}}" value="{{>status}}"/>
	</div>
</script>

</head>
<body>
	<div class="xyd-wrapper">
		<div id="main" class="xyd-main clearfix">
			<jsp:include page="personalMenu.jsp"></jsp:include>
		
			<div class="xyd-article">
                 <div class="wzb-title-12"><lb:get key="certificate_list"/></div>
                 
                 <div class="wbtable"></div>
			</div> <!-- xyd-article End-->
		</div>
	</div> <!-- xyd-wrapper End-->
</body>
</html>