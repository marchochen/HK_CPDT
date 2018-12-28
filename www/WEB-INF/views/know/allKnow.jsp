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
		if(${type == null or type == ''}){
			changeTab($(".active"));
		} else {
			changeTab($("li[name='${type}']"));
		}
		
		$("#search_content").prompt("<lb:get key='search_type_question' />" );
	})
	var kcaId = 0;
	//切换Tab
	function changeTab(thisObj){
		$(".active").removeClass("active");
		$(thisObj).addClass("active");
		var queType = $(thisObj).attr("name");		
		var url = '${ctx}/app/know/${command}/que/' + queType + '/' + kcaId;
		if(queType == 'my_know_help'){
			url = '${ctx}/app/know/myKnow/' + queType;
		}
		var searchContent = $("#search_content").val();
		if(searchContent == fetchLabel('search_type_question')){
			searchContent = "";
		}
		$(".wbtabcont").html("");
		$(".wbtabcont").table({
			url : url,
			params : {
				searchContent : searchContent
			},
			colModel : colModel,
			rp : 10,
			showpager : 5,
			sortname : 'que_create_timestamp',
			sortorder : 'desc',
			usepager : true
		})
	}
	
	var colModel = [ {
		name : 'que_title',
		display : fetchLabel('know_title'),
		width : '35%',
		sortable : true,
		format : function(data) {
			p = {
				className : 'wzb-link02',
				href : '${ctx}/app/know/knowDetail/' + data.que_type + '/' + wbEncrytor().cwnEncrypt(data.que_id),
				title : data.que_title
			};
			return $('#a-template').render(p);
		}
	},
	{
		name : 'kca_title',
		display : fetchLabel('know_catalog'),
		width : '20%',
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
		width : '20%',
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
		width : '25%',
		sortable : true,
		align : "center",
		format : function(data) {
			p = {
				text : data.que_create_timestamp.substring(0,10)
			};
			return $('#text-center-template').render(p);
		}
	} ]
	
	$(document).ready(function() {
		$.ajax({
			url : '${ctx}/app/know/allKnow/kca/CATALOG/0',
			type : 'POST',
			dataType : 'json',
			async : false,
			success : function(data){
				for(var i=0;i<data.kca.length;i++){
					var html = $("#parent_catalog").html() + '<a title="' + data.kca[i].kca_title + '" href="javascript:;" onclick="changeCatalog(this, ' 
							+ data.kca[i].kca_id + ', \'CATALOG\')" id="catalog_' + data.kca[i].kca_id + '" value="' + data.kca[i].kca_id + '">' + data.kca[i].kca_title + '</a>';
					$("#parent_catalog").html(html);
				}
			}
		});
		$.ajax({
			url : '${ctx}/app/know/allKnow/kca/NORMAL/0',
			type : 'POST',
			dataType : 'json',
			async : false,
			success : function(data){
				for(var i=0;i<data.kca.length;i++){
					var html = $("#child_catalog").html() + '<a title="' + data.kca[i].kca_title + '" href="javascript:;" onclick="changeCatalog(this, ' 
							+ data.kca[i].kca_id + ', \'NORMAL\')" id="catalog_' + data.kca[i].kca_id + '" value="' + data.kca[i].kca_id + '">' + data.kca[i].kca_title + '</a>';
					$("#child_catalog").html(html);
				}
				if(data.kca.length == 0){
					$("#child_catalog").html("<span class='grayC999' style='padding: 4px 8px;'>" + fetchLabel("know_no") + fetchLabel("know_child_catalog") + "</span>");
				}
			}
		});
		if(${parent_catalog_id != null}){
			$("#catalog_${parent_catalog_id}").click();
		}
		if(${child_catalog_id != null and child_catalog_id > 0}){
			$("#catalog_${child_catalog_id}").click();
		}
	});
	
	//切换分类
	function changeCatalog(thisObj, kca_id, kca_type){
		$("#" + $(thisObj).parent().attr("id") + " .cur").removeClass("cur");
		$(thisObj).addClass("cur");
		kcaId = kca_id;
		kcaChId = kca_id;
		$.ajax({
			url : '${ctx}/app/know/allKnow/kca/NORMAL/' + kca_id,
			type : 'POST',
			dataType : 'json',
			params : {
				kca_type : kca_type,
				kca_id : kca_id
			},
			success : function(data){
				if(kca_type == 'CATALOG'){
					$("#child_catalog").html("");
					for(var i=0;i<data.kca.length;i++){
						var html = $("#child_catalog").html() + '<a title="' + data.kca[i].kca_title + '" href="javascript:;" onclick="changeCatalog(this, ' 
								+ data.kca[i].kca_id + ', \'NORMAL\')" id="catalog_' + data.kca[i].kca_id + '" value="' + data.kca[i].kca_id + '">' + data.kca[i].kca_title + '</a>';
						$("#child_catalog").html(html);
						if(data.kca[i].kca_id == kcaChId){
							$("#catalog_"+kcaChId).addClass("cur");
						}
					}
					if(data.kca.length == 0){
						$("#child_catalog").html("<span class='grayC999' style='padding: 4px 8px;'>" + fetchLabel("know_no") + fetchLabel("know_child_catalog") + "</span>");
					}
				}
				
				var queType = $(".active").attr("name");
				var url = '${ctx}/app/know/${command}/que/' + queType + '/' + kca_id;
				var searchContent = $("#search_content").val();
				if(searchContent == fetchLabel('search_type_question')){
					searchContent = "";
				}
				$(".wbtabcont").html("");
				$(".wbtabcont").table({
					url : url,
					params : {
						searchContent : searchContent
					},
					colModel : colModel,
					rp : 10,
					showpager : 5,
					async : false,
					usepager : true
				})
			}
		});
	}
	
	//搜索问题
	function searchKnow(){
		var queType = $(".active").attr("name");
		var url = '${ctx}/app/know/${command}/que/' + queType + '/' + kcaId;
		if(queType == 'my_know_help'){
			url = '${ctx}/app/know/myKnow/' + queType;
		}
		//var url = '${ctx}/app/know/${command}/que/' + queType + '/' + kcaId;
		var searchContent = $("#search_content").val();
		if(searchContent == fetchLabel('search_type_question')){
			searchContent = "";
		}
		$(".wbtabcont").html("");
		$(".wbtabcont").table({
			url : url,
			params : {
				searchContent : searchContent
			},
			colModel : colModel,
			rp : 10,
			showpager : 5,
			usepager : true
		})
	}
</script>
</head>
<body>
	<div class="xyd-wrapper">
		<div id="main" class="xyd-main clearfix">
			<jsp:include page="knowMenu.jsp"></jsp:include>
			<div id="know_jsp" class="xyd-article">
					<%-- <div class="xyd-step clearfix">
                         <div class="xyd-step01">
                              <strong><lb:get key="know_put_question"/></strong>
                              <p><lb:get key="know_put_question_desc"/></p>
                         </div>
                         <div class="xyd-step02">
                              <strong><lb:get key="know_ask_num"/></strong>
                              <p><lb:get key="know_answer_desc"/></p>
                         </div>
                         <div class="xyd-step03">
                              <strong><lb:get key="know_explore"/></strong>
                              <p><lb:get key="know_explore_desc"/></p>
                         </div>
                    </div> --%><!-- 提问，回答数，发掘的图片模块 --> <!-- xyd-step End-->
                    
                    <div class="wzb-model-4">
                         <dl class="wzb-list-9 wzb-border-bottom clearfix">
                             <dt style="padding:0;"><lb:get key="know_catalog"/>：</dt>
                             <dd id="parent_catalog"><a class="cur" title='<lb:get key="status_all"/>' href="javascript:;" onclick="changeCatalog(this, 0, 'CATALOG')" value="0"><lb:get key="status_all"/></a></dd>
                        </dl>
                        
                        <dl class="wzb-list-9 clearfix">
                             <dt style="padding:0;"><lb:get key="know_child_catalog"/>：</dt>
                             <dd id="child_catalog">
					         </dd>
                        </dl>
                    </div> <!-- wzb-model-4 End-->
                        
				    <div class="wzb-title-2">
                         <form class="form-search form-souso pull-right">     
                               <input type="text" value="" class="form-control" id="search_content"><input type="button" value="" onclick="searchKnow()" class="form-button">
                         </form>
                    </div>

					<div role="tabpanel" class="wzb-tab-5 wzb-tab-5-2">
						<ul class="nav nav-tabs" role="tablist">
							<li role="presentation" class="active" onclick="changeTab(this)" name="UNSOLVED"><a href="#home" aria-controls="home" role="tab" data-toggle="tab"><lb:get key="know_UNSOLVED"/></a></li>
							<li role="presentation" onclick="changeTab(this)" name="SOLVED"><a href="#profile" aria-controls="profile" role="tab" data-toggle="tab"><lb:get key="know_SOLVED"/></a></li>
							<!-- <li role="presentation" onclick="changeTab(this)" name="POPULAR"><a href="#detail" aria-controls="detail" role="tab" data-toggle="tab"><lb:get key="know_POPULAR"/></a></li> -->
							<li role="presentation" onclick="changeTab(this)" name="FAQ"><a href="#notice" aria-controls="notice" role="tab" data-toggle="tab"><lb:get key="know_FAQ"/></a></li>
							<li role="presentation" onclick="changeTab(this)" name="my_know_help"><a href="#myknowhelp" aria-controls="myknowhelp" role="tab" data-toggle="tab"><lb:get key="lab_knowmenu_help"/></a></li>
						</ul>                                         
					                                                                                                              
					    <div class="wbbox tab-content">      
					        <div class="wbtabcont wbtable"> 	        
					        </div>
				    	</div>
					</div> <!-- wbtab end --> 
			</div> <!-- xyd-article End-->
		</div>
	</div>	<!-- xyd-wrapper End-->
</body>
</html>