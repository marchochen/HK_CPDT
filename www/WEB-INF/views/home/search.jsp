<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-learner.css" />
<script type="text/javascript"
	src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<script type="text/javascript" src="${ctx}/static/js/front/sns.js"></script>
<script type="text/javascript" src="${ctx}/static/js/front/jquery.sns.js"></script>
<title></title>

</head>
<body>
	<div class="xyd-wrapper">
		<div id="main" class="xyd-main clearfix">
			<div class="xyd-sidebar">
                <h3 class="wzb-title-4 skin-bg"><lb:get key="search_result"/> <i class="fa fa-sanzuo fa-caret-up"></i></h3>
                
                <ul class="wzb-list-17" id="searchType">
                    <li><a href="javascript:;" data="All" class="cur" title='<lb:get key="search_type_all"/>'><lb:get key="search_type_all"/><em>(${searchResult["All"]})</em></a></li>
                    <li><a href="javascript:;" data="Course" title='<lb:get key="search_type_course"/>'><lb:get key="search_type_course"/><em>(${searchResult["Course"]})</em></a></li>
                   <%--  <li><a href="javascript:;" data="Exam" title='<lb:get key="search_type_exam"/>'><lb:get key="search_type_exam"/><em>(${searchResult["Exam"]})</em></a></li> --%>
                    <li><a href="javascript:;" data="Open" title='<lb:get key="search_type_open"/>'><lb:get key="search_type_open"/><em>(${searchResult["Open"]})</em></a></li>
                    <li><a href="javascript:;" data="Message" title='<lb:get key="search_type_message"/>'><lb:get key="search_type_message"/><em>(${searchResult["Message"]})</em></a></li>
                    <li><a href="javascript:;" data="Article" title='<lb:get key="search_type_article"/>'><lb:get key="search_type_article"/><em>(${searchResult["Article"]})</em></a></li>
                    <%-- <li><a href="javascript:;" data="Group" title='<lb:get key="search_type_group"/>'><lb:get key="search_type_group"/><em>(${searchResult["Group"]})</em></a></li> --%>
                   <%--  <li><a href="javascript:;" data="Answer" title='<lb:get key="search_type_answer"/>'><lb:get key="search_type_answer"/><em>(${searchResult["Answer"]})</em></a></li> --%>
                   <%--  <li><a href="javascript:;" data="Contacts" title='<lb:get key="search_type_contacts"/>'><lb:get key="search_type_contacts"/><em>(${searchResult["Contacts"]})</em></a></li> --%>
                   <%--  <li><a href="javascript:;" data="Knowledge" title='<lb:get key="search_type_knowledge"/>'><lb:get key="search_type_knowledge"/><em>(${searchResult["Knowledge"]})</em></a></li> --%>
                </ul>
            </div> <!-- xyd-sidebar End-->

			<div class="xyd-article">
                    <div class="wzb-title-2"><lb:get key="search_text"/>&nbsp;"<span id="searchTypeText" class="font-size16">${searchText}</span>"：<span id="resultCountText" class="font-size16">0</span></div>
                    
                    <div class="wzb-souso-title">
                         <span class="wzb-souso-left"><lb:get key="global_name"/></span><span class="wzb-souso-right"><lb:get key="global_kinds"/></span>
                    </div>

					<ul class="wzb-list-21" id="searchResult">

					</ul>
			</div> <!-- xyd-article End-->

		</div>
	</div>
	<!-- xyd-wrapper End-->
<script type="text/javascript">
var searchTb;
var type = '';
$(function(){
	searchTb = $("#searchResult").table({
		url : '${ctx}/app/search/Json',
		colModel : colModel,
		params : {
			type : type,
			searchValue : '${searchText}'
		},
		rp : 10,
		hideHeader : true,
		usepager : true,
		onSuccess : function(data){
			//写入总数
			var total = data.params.total;
			$("#resultCountText").html(fetchLabel("search_result_notice",[total]));
			photoDoing();
		}
	});

	$("#searchType li").click(function(){
		type = $(this).find("a").attr("data");
		$(this).find("a").addClass("cur");
		$(this).siblings().find("a").removeClass("cur");

		var typeStr = fetchLabel("search_type_" + type.toLowerCase());
		//写入类型
		//$("#searchTypeText").html(typeStr);
		
		$(searchTb).reloadTable({
			url : '${ctx}/app/search/Json',
			params : {
				type : type,
				searchValue : '${searchText}'
			}
		});
	});
	var typeStr = fetchLabel("search_type_all");	
})

var colModel = [{
	format : function(data) {
		var url;
		var dtype = data.type;
		var enc_id = wbEncrytor().cwnEncrypt(data.id);
		if(dtype == 'Exam' || dtype == 'Open' || dtype == 'Course'){
			url = "${ctx}/app/course/detail/" + enc_id
		} else if(dtype == 'Answer'){
			url = "${ctx}/app/know/detail/" + enc_id
		} else if(dtype == 'Article') {
			url = "${ctx}/app/article?id=" + enc_id
		} else if(dtype == 'Message') {
			url = "${ctx}/app/announce?id=" + enc_id
		} else if(dtype == 'Group') {
			url = "${ctx}/app/group/groupDetail/" + enc_id
		} else if(dtype == 'Contacts') {
			url = "${ctx}/app/personal/" + data.id;//不用写加密id，因为地址会调到一个中专地址，然后处理之后，再redirect
		} else if(dtype == 'Knowledge') {
			url = "${ctx}/app/kb/center/view?source=index&enc_kbi_id=" + enc_id
		}
		var typeStr = "";
		if(dtype){
			typeStr = fetchLabel("search_type_" + dtype.toLowerCase());
		}
		
		$.extend(data, {url : url, typeStr : typeStr})
		return $('#searchResultTemplate').render(data);
	}
}];
</script>

<script id="searchResultTemplate" type="text/x-jquery-tmpl">
<li class="clearfix">
{{if photo}}
	 <div class="wzb-user wzb-user68 pull-left margin-right15 trend">
		  <a href="{{>url}}"><img class="wzb-pic" src="${ctx}{{>photo}}"/></a>
		 <!-- <p class="companyInfo"><lb:get key="know_ta"/></p>
		  <div class="cornerTL">&nbsp;</div>
		  <div class="cornerTR">&nbsp;</div>
		  <div class="cornerBL">&nbsp;</div>
		  <div class="cornerBR">&nbsp;</div>-->
	 </div>	
{{/if}}

<div class="wzb-souso-left">
	 <a class="wzb-link01" href="{{>url}}">{{if (type=='Course'||type=='Exam')}}<img src="${ctx}/static/images/pc-icon-p.png" width="26px" height="22px"/>{{/if}}{{if itm_mobile_ind && itm_mobile_ind == 'yes' && (type=='Course'||type=='Exam')}}<img src="${ctx}/static/images/phone-icon-g.png" width="21px" height="22px"/>{{/if}}{{>title}}</a>
	 <p>{{>introduction}}</p>
</div>
  
<div class="wzb-souso-right">{{>typeStr}}</div>
</li>
</script>
</body>
</html>