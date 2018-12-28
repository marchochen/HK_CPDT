<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-learner.css" />
<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<script type="text/javascript" src="${ctx}/static/js/front/sns.js"></script>
<script type="text/javascript" src="${ctx}/static/js/front/jquery.sns.js"></script>
<title></title>
<script type="text/javascript">
var opens;
var tndId;
var sns = new Sns();

$(function(){
	opens = $("#open_lst").table({
		url : '${ctx}/app/course/openJson',
		gridTemplate : function(data){
			$.extend(data,{encItmId : wbEncrytor().cwnEncrypt(data.itm_id) , itm_publish_timestamp : Wzb.displayTime(data.itm_publish_timestamp, Wzb.time_format_ymd)})
			return $('#openTemplate').render(data);
		},
		rowCallback : function(data){
			$("#open_lst .like:last").like({
				count : data.snsCount?data.snsCount.s_cnt_like_count:0,
				flag : data.userLike,
				id : data.itm_id,
				module: 'Course',
				tkhId : 0
			})
			courseDoing();
		},
		view : 'grid',
		rp : 16,
		width : '103%',
		hideHeader : true,
		usepager : true
	});
	
	$.templates({
		catalogSingle : '<a class="" style="margin: 0 15px 15px 0;" data="{{>tnd_id}}" title="{{>tnd_title}}" href="javascript:;">{{>tnd_title}}</a>'
	});
	
	//获取目录
	getCatalog('', true);
	//点击选中目录
	$(".tnd").live('click', function(e){
		if(!$(this).hasClass("cur")){
			tndId = $(this).attr("data");
			$(this).addClass("cur").siblings().removeClass("cur");
			if(!$(this).hasClass("second")){
				getCatalog(tndId, false);
			}
		} else {
			//tndId = "";
			//$(this).removeClass("cur");
			//if($(this).hasClass("second")){
			//	tndId = $("#catalogFirst").find(".cur").attr("data");
			//} else {
				tndId = $("#catalogSecond").find(".cur").attr("data");
				//$("#catalogSecond").empty();
				//var lab_STATUS_ALL = '<a class="cur" data="0" href="#" title="'+fetchLabel('lab_STATUS_ALL')+'" onclick="">'+fetchLabel('lab_STATUS_ALL')+'</a>'
				//$("#catalogSecond").append(lab_STATUS_ALL);
				//$("#catalogSecondText").hide();
			//}
		}
		if(tndId == 0)
		{
			tndId = $('#catalogFirst .cur').attr('data');
		}
		$(opens).reloadTable({
			url : '${ctx}/app/course/openJson',
			params : {
				tndId : tndId
			}
		});

	})
	
	$(".wblearn li").click(function(){
		$(this).addClass("active").siblings().removeClass("active");
		var p = {
			tndId : tndId
		}
		var sortname,sortorder = 'desc';
		var index = $(this).index();
		if(index==0){
			sortname = 'ies_access_count';
		} else if(index==1){
			sortname = 'itm_publish_timestamp';
		} else if(index==2){
			sortname = '"snsCount.s_cnt_like_count"';
		}
		$(opens).reloadTable({
			url : '${ctx}/app/course/openJson',
			params : p,
			sortname : sortname,
			sortorder : sortorder
		});		
	})
	
	
})

function getCatalog(tndId, flag){
	//获取目录
	var url = "${ctx}/app/catalog/openJson?cos_type=public";
	if(tndId && tndId>0) url += "&tndId=" + tndId;
	$.getJSON(url, function(result) {
		var catalogFirst = result.catalog.first;
		var catalogSecond = result.catalog.second;
		if(flag) {
			$("#catalogFirst").empty();
			var lab_STATUS_ALL = '<a class="cur" style="margin: 0 15px 15px 0;" data="0" title="'+fetchLabel('lab_STATUS_ALL')+'" href="#" onclick="">'+fetchLabel('lab_STATUS_ALL')+'</a>'
			$("#catalogFirst").append(lab_STATUS_ALL); 
			$("#catalogFirst").append($.render.catalogSingle(catalogFirst));
			$("#catalogFirst a").addClass("tnd");
		}
		if(catalogSecond){
			$("#catalogSecond").empty();
			var lab_STATUS_ALL = '<a class="cur" style="margin: 0 15px 15px 0;" data="0" title="'+fetchLabel('lab_STATUS_ALL')+'" href="#" onclick="">'+fetchLabel('lab_STATUS_ALL')+'</a>'
			$("#catalogSecond").append(lab_STATUS_ALL);
			$("#catalogSecond").append($.render.catalogSingle(catalogSecond));
			$("#catalogSecond a").addClass("tnd second");
			/* if(catalogSecond.length>0){
				$("#catalogSecondText").show();
			}else{
				$("#catalogSecondText").hide();
			} */
		}
	});
}
</script>
 <!-- template start -->

<script id="openTemplate" type="text/x-jsrender">
<li>
	<div class="wzb-list-hover">
		<div class="subim {{>colorhome}}">
			 <a class="subxian" href="${ctx}/app/course/detail/{{>encItmId}}"><img src="${ctx}{{>itm_icon}}"></a>
			 <a class="subbox" href="${ctx}/app/course/detail/{{>encItmId}}" title="">{{>itm_title}}</a>
	
			 <div class="subcont">
				  <div class="subarea">
					   <div class="subbar">
							<a class="subinfo" href="${ctx}/app/course/detail/{{>encItmId}}" title=""><em>{{>itm_title}}</em></a>
							<div class="subdesc">
<!--<a title="" href="javascript:;" class="pull-left color-gray666 like"><i class="fa skin-color fa-thumbs-o-up"></i> <span>0</span></a>-->
 <span class="pull-left"><i class="fa skin-color fa-clock-o"></i>{{>itm_publish_timestamp}}</span></div>
					   </div>
					   <div class="subfilter"></div>
				  </div>
			 </div>
		</div>
	</div>	
</li>
</script>

 <!-- template end -->
</head>
<body>

<div class="xyd-wrapper">
<div id="main" class="xyd-main clearfix" style="min-height:550px;" >
<div class="xyd-hold xyd-hold-red clearfix">
<div class="xyd-hold-left">
     <i class="fa fa-youtube-play"></i>
     <strong><lb:get key="open_course"/></strong>
</div>
          
<div class="xyd-hold-right">  
    <dl class="wzb-list-9 wzb-border-bottom clearfix" style="padding: 10px 0 15px 100px;">
         <dt style="height:28px;line-height:28px;"><lb:get key="open_catalog"/>：</dt>
         <dd id="catalogFirst"></dd>
    </dl>
    
    <dl class="wzb-list-9 clearfix" style="padding: 10px 0 15px 100px;">
         <dt id="catalogSecondText" style="height:28px;line-height:28px;"><lb:get key="open_sub_catalog"/>：</dt>
         <dd id="catalogSecond">
          <a class="cur" style="margin: 0 15px 15px 0;" data="0"  href="#" onclick=""><lb:get key="lab_STATUS_ALL"/></a>
         </dd>
    </dl>
   <%--  <dl class="wzb-list-9 clearfix">
         <dt id="catalogSecondText" style="display:none;padding:0"><lb:get key="open_sub_catalog"/>：</dt>
         <dd id="catalogSecond"></dd>
    </dl> --%>
</div>     
</div> <!-- xyd-hold End-->

<div role="tabpanel" class="wzb-tab-3">
<ul class="nav nav-tabs wblearn" role="tablist">
    <li role="presentation" class="active"><a href="#zuire" aria-controls="zuire" role="tab" data-toggle="tab" title=""><lb:get key="open_the_hottest"/></a></li>
    <li role="presentation"><a href="#zuixin" aria-controls="zuixin" role="tab" data-toggle="tab" title=""><lb:get key="open_the_new"/></a></li>
    <%-- <li role="presentation"><a href="#renqi" aria-controls="renqi" role="tab" data-toggle="tab" title=""><lb:get key="open_popularity"/></a></li> --%>
</ul>

<div class="tab-content">
     <div class="wbtabcont">
          <ul class="wzb-list-14 wzb-width-14" id="open_lst">     
          </ul>
     </div>
</div>
</div> <!-- wbtab end --> 

</div>
</div> <!-- xyd-wrapper End-->


</body>
</html>