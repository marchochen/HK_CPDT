<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<%@ include file="../common/template.jsp"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-learner.css" />
<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<script type="text/javascript" src="${ctx}/js/wb_evaluation.js"></script>
<title></title>
<script type="text/javascript">
	survey = new wbEvaluation();
	$(function(){
		$(".wbtable").table({
			url : '${ctx}/app/personal/evaluationList',
			params : {
				status:0
			},
			colModel : colModel,
			rp : 10,
			showpager : 5,
			usepager : true
		})
	})	

    var tabName ='';
	function changeTab(thisObj){
		tabName = $(thisObj).attr('name');
		$(".active").removeClass("active");
		$(thisObj).addClass("active");
		$(".wbtable").html('');
		$(".wbtable").table({
			url : '${ctx}/app/personal/evaluationList',
			params : {
				status:$(thisObj).attr('name')=='wait_anwser'? 0 : 1
			},
			colModel : colModel,
			rp : 10,
			showpager : 5,
			usepager : true
		})
	}
	var colModel = [ {
		display : fetchLabel('global_title'),
		width : '30%',
		format : function(data) {
			var href = "javascript:survey.evn_opn('EVN', '" + data.res_id + "', 'svy_player.xsl')";
			if(data.mov != undefined && data.mov.mov_status != undefined){
				href = "javascript:;";
			}
			p = {
				className : 'wzb-link02',
				href : href,
				title : data.res_title
			};
			if(tabName=='anwsered'){
				return $('#span-no-template').render(p);
			}
			return $('#a-template').render(p);
		}
	}, {
		display : fetchLabel('personal_deadline'),
		width : '20%',
		align : "center",
		format : function(data) {
			p = {
				text : data.mod.mod_eff_end_datetime.substring(0,11)
			};
			return $('#text-center-template').render(p);
		}
	}, {
		display : fetchLabel('personal_submit_time'),
		width : '20%',
		align : "center",
		format : function(data) {
			p = {
				text : data.mov == undefined || data.mov.mov_create_timestamp == undefined? "--" : data.mov.mov_create_timestamp.substring(0,11)
			};
			return $('#text-center-template').render(p);
		}
	}, {
		display : fetchLabel('personal_submit_detail'),
		width : '20%',
		align : "right",
		format : function(data) {
			var status = fetchLabel('personal_no_submit');
			if(data.mov != undefined && data.mov.mov_status == 'C'){
				status = fetchLabel('personal_submit_ok');
			}
			p = {
				text : status
			};
			return $('#text-center-template').render(p);
		}
	} ]
</script>
</head>
<body>
	<div class="xyd-wrapper">
		<div id="main" class="xyd-main clearfix">
			<jsp:include page="personalMenu.jsp"></jsp:include>
		
			<div class="xyd-article">
                <div class="wzb-title-12"><lb:get key="personal_evaluation_list"/></div>
                
				<div role="tabpanel" class="wzb-tab-3 margin-top20">
					<!-- Nav tabs -->
					<ul class="nav nav-tabs" role="tablist">
						<li role="presentation" class="active" onclick="changeTab(this)" name="wait_anwser"><a href="#home" aria-controls="home" role="tab" data-toggle="tab" style="padding-left:0;"><lb:get key="personal_evaluation_wait_anwser"/></a></li>
						<li role="presentation" onclick="changeTab(this)" name="anwsered"><a href="#profile" aria-controls="profile" role="tab" data-toggle="tab"><lb:get key="personal_evaluation_anwsered"/></a></li>
					</ul>	

				    <div class="tab-content">
						<div class="wbtable margin-top15"></div>				        
				    </div>
				</div> <!-- wzb-tab-3 end --> 
			</div> <!-- xyd-article End-->
		</div>
	</div> <!-- xyd-wrapper End-->
</body>
</html>