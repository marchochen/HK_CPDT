<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<%@ include file="../common/template.jsp"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-learner.css" />
<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<script type="text/javascript" src="${ctx}/static/js/front/course.js"></script>
<script type="text/javascript" src="${ctx}/js/urlparam.js"></script>
<title></title>
<script type="text/javascript">
	$(function() {
		changeTab();
	})
	
	//切换Tab
	function changeTab(){
		$(".wbtabcont").html("");
		$(".wbtabcont").table({
			url : '${ctx}/app/know/myKnow/my_know_help',
			colModel : colModel,
			rp : 10,
			showpager : 5,
			usepager : true
		})
	} 
	
	var colModel = [ {
		name : 'que_title',
		display : fetchLabel('know_title'),
		width : '50%',
		sortable : true,
		format : function(data) {
			p = {
				className : 'wzb-link02',
				href : '${ctx}/app/know/knowDetail/' + data.que_type + '/' + wbEncrytor().cwnEncrypt(data.que_id),
				title : data.que_title
			};
			return $('#a-template').render(p);
		}
	},{
		name : 'kca_title',
		display : fetchLabel('know_catalog'),
		width : '10%',
		sortable : true,
		format : function(data) {
			p = {
				text : data.knowCatalog.kca_title
			};
			return $('#text-template').render(p);
		}
	}, {
		name : 'ask_num',
		display : fetchLabel('know_ask_num'),
		width : '9%',
		sortable : true,
		align : "center",
		format : function(data) {
			p = {
				text : data.ask_num
			};
			return $('#text-center-template').render(p);
		}
	}, {
		name : 'que_create_timestamp',
		display : fetchLabel('know_time'),
		width : '18%',
		sortable : true,
		align : "center",
		format : function(data) {
			p = {
				text : data.que_create_timestamp.substring(0,10)
			};
			return $('#text-center-template').render(p);
		}
	}, {
		name : 'que_type',
		display : fetchLabel('know_status'),
		width : '14%',
		sortable : true,
		align : "right",
		format : function(data) {
			p = {
				text : fetchLabel('know_' + data.que_type)
			};
			return $('#text-template').render(p);
		}
	} ]
	
</script>
</head>
<body>
	<div class="xyd-wrapper">
		<div id="main" class="xyd-main clearfix">
			<jsp:include page="knowMenu.jsp"></jsp:include>
			<div id="know_jsp" class="xyd-article">
                    <div class="wzb-title-2"><lb:get key="know_put_question_head"/><lb:get key="know_list"/></div>

					<div role="tabpanel" class="wzb-tab-5 wzb-tab-5-2">
						<%-- <ul class="nav nav-tabs" role="tablist">
							<li role="presentation" class="active" name="my_question" onclick="changeTab(this)"><a href="#review" aria-controls="review" role="tab" data-toggle="tab"><lb:get key="know_my"/><lb:get key="know_question_content"/></a></li>
							<li role="presentation" name="my_answer" onclick="changeTab(this)"><a href="#score" aria-controls="score" role="tab" data-toggle="tab"><lb:get key="know_my"/><lb:get key="know_answer"/></a></li>
						</ul>  --%>                                         
					                                                                                                              
					    <div class="tab-content">      
					        <div class="wbtabcont wbtable"></div>
					    </div>
					</div> <!-- wbtab end --> 
			</div>	<!-- xyd-article End-->
		</div>
	</div>	<!-- xyd-wrapper End-->
</body>