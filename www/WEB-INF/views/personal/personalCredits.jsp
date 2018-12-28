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
		$(".creditsTable").table({
			url : '${ctx}/app/personal/getCreditDetail/${usrEntId}',
			colModel : colModel,
			rp : 10,
			showpager : 5,
			usepager : true
		})
	})
	
	colModel = [{
		display : fetchLabel('credits_name'),
		width : '18%',
		format : function(data) {
			var credits_name = data.creditsType.cty_title;
			if(data.ucl_relation_type != undefined && credits_name != 'ITM_INTEGRAL_EMPTY'){
				credits_name = fetchLabel('credits_' + credits_name);
			}
			if(credits_name == 'INTEGRAL_EMPTY' || credits_name == 'ITM_INTEGRAL_EMPTY' ){
				credits_name = fetchLabel("credits_INTEGRAL_EMPTY");
			}
			p = {
				text : credits_name
			};
			return $('#text-template').render(p);
		}
	}, {
		display : fetchLabel('credits_source'),
		width : '10%',
		align : 'center',
		format : function(data) {
			var text = '--';
			if(data.aeItem != undefined){
				text = data.aeItem.itm_title;
			}
			p = {
				text : text
			};
			return $('#text-template').render(p);
		}
	}, {
		display : fetchLabel('credits_point'),
		width : '7%',
		align : 'center',
		format : function(data) {
			p = {
				text : data.ucl_point
			};
			return $('#text-template').render(p);
		}
	}
	/*, {
		display : fetchLabel('credits_operate_type'),
		width : '11%',
		align : 'center',
		format : function(data) {
			var text = fetchLabel('credits_add');
			if(data.creditsType.cty_deduction_ind == 1){
				text = fetchLabel('credits_deduction');
			}
			p = {
				text : text
			};
			return $('#text-template').render(p);
		}
	}*/, {
		display : fetchLabel('credits_zd_ind'),
		width : '11%',
		align : 'center',
		format : function(data) {
			var text = fetchLabel('credits_automatic');
			if(data.ucl_relation_type == undefined || (data.creditsType != undefined && data.creditsType != null && (data.creditsType.cty_title == "ITM_INTEGRAL_EMPTY" || data.creditsType.cty_title == "ITM_IMPORT_CREDIT" || data.creditsType.cty_title == "API_UPDATE_CREDITS"))){
				text = fetchLabel('credits_manual');
			}
			p = {
				text : text
			};
			return $('#text-template').render(p);
		}
	}, {
		display : fetchLabel('credits_type'),
		width : '11%',
		align : 'center',
		format : function(data) {
			var text = fetchLabel('credits_activity');
			if(data.ucl_relation_type == 'COS'){
				text = fetchLabel('credits_train');
			}else if(data.ucl_relation_type == 'API'){
				text = fetchLabel('credits_API_UPDATE_CREDITS');
			}
			p = {
				text : text
			};
			return $('#text-template').render(p);
		}
	}, {
		display : fetchLabel('credits_time'),
		width : '20%',
		align : 'center',
		format : function(data) {
			p = {
				text : data.ucl_create_timestamp.substring(0,16)
			};
			return $('#text-template').render(p);
		}
	}]
</script>
</head>
<body>
<input type="hidden" value="Course" id="s_clt_module">
<div class="xyd-wrapper">
	<div id="main" class="xyd-main clearfix">
		<jsp:include page="personalMenu.jsp"></jsp:include>
		
		<div class="xyd-article"><!-- wzb-title-2  去除粗体 -->
             <div class="wzb-title-12"><lb:get key="credits_information"/></div>

			 <div class="xyd-jifen-title"><lb:get key="usr_credit"/>: <span class="font-size38">${total_credits}</span></div>

			 <div class="xyd-jifen-content">
				<div class="col-xs-6 xyd-jifen-peixun">
					<span class="xyd-jifen-size" style="width:155px;overflow:hidden;text-overflow:ellipsis;white-space:nowrap;" title="${train_credits}">${train_credits}</span>
					<lb:get key="credits_train"/>
				</div>
				<div class="col-xs-6 xyd-jifen-huodong">
				  	<span class="xyd-jifen-size" style="width:155px;overflow:hidden;text-overflow:ellipsis;white-space:nowrap;" title="${activity_credits}">${activity_credits}</span>
				  	 <lb:get key="credits_activity"/>
			  	 </div>
			 </div>
										
			 <div class="wzb-title-3"><lb:get key="credits_records"/></div>

             <table class="wbtable">
                 <tr><td><div class="creditsTable"></div></td></tr>
             </table>
		</div> <!-- xyd-article End-->
	</div>
</div> <!-- xyd-wrapper End-->
</body>
</html>