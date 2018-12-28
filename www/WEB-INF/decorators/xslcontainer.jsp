<!DOCTYPE html>
<html>
<head>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../views/common/taglibs.jsp"%>
<%@ include file="../views/admin/common/meta.jsp"%>

<title><decorator:title default="Wizbank" /></title>
<decorator:head />
<style type="text/css">
.wzb-module-title {display:none}
</style>
<script type="text/javascript">

$(function(){

    var wzbMoudleTitle = $("div.wzb-module-title").html();
    if(wzbMoudleTitle != undefined && wzbMoudleTitle != '') {
        $(".panel-heading").html(wzbMoudleTitle);
        $("div.wzb-module-title").hide();
    }

	$(".SecBg").parents("table").css("margin-top", "10px");
	$('body').append($(".modal"));//保证弹出框的正常运行
    /**
     * 旧平台的样式替换
     */
    $('.TitleText').addClass('font').removeClass('TitleText');
    $('.Text').addClass('font').addClass('gray4e').removeClass('Text');
    $('.InputFrm[Type="text"]').addClass('wzb-inputText').removeClass('InputFrm');
    $('textarea[class="InputFrm"]').addClass('wzb-inputTextArea').removeClass('InputFrm');
    $('.TitleTextBold').addClass('font').addClass('boldText').removeClass('TitleTextBold');
    $('.SmallText').addClass('font').removeClass('SmallText');
    $('.Select').addClass('wzb-select').removeClass('Select');
    $('table').each(function() {
        var padding = $(this).attr('cellpadding');
        if(padding && padding != 0){
            if($(this).children('tbody').children('tr')){
                $(this).children('tbody').children('tr').children('td').css({
                    padding: padding
                });
            }else if($(this).children('tr')){
                $(this).children('tr').children('td').css({
                    padding: padding
                });
            }else{
                 $(this).children('td').css({
                    padding: padding
                });
            }
        }
    });
    var belong_module = $("input[name='belong_module']").val();
    var parent_code = $("input[name='parent_code']").val();
    var page_title = $("input[name='page_title']").val();
    var bannerJson = {//页面banner标题，图标，背景的设置项
    	
    	/** 培训管理 **/
    	'FTN_AMD_TRAINING_MGT' : {
    		background : "#00cc33",
    		icon : "fa-calendar",
    		gif : "wzb-b05.gif"
    	},
    	/**考试管理**/
    	'FTN_AMD_EXAM_MGT' : {
    		background : "#00cc33",
    		icon : "fa-columns",
    		gif : "wzb-b14.gif"
    	},
    	/** 资讯管理  **/
    	'FTN_AMD_ARTICLE_MGT' : {
    		background : "#cc0099",
    		icon : "fa-globe",
    		gif : "wzb-b01.gif"
    	},
    	/** 需求管理  **/
    	'FTN_AMD_DEMAND_MGT' : {
    		background : "#663300",
    		icon : "fa-cubes",
    		gif : "wzb-b02.gif"
    	},
    	
    	/** 计划管理  **/
    	'FTN_AMD_PLAN_MGT' : {
    		background : "#006600",
    		icon : "fa-list-alt",
    		gif : "wzb-b03.gif"
    	},
    	/** 社区管理  **/
    	'FTN_AMD_SNS_MGT' : {
    		background : "",
    		icon : "fa-comments",
    		gif : "wzb-b06.gif"
    	},
    	/** 用户管理  **/	
    	'FTN_AMD_USR_INFO_MGT' : {
    		background : "#336666",
    		icon : "fa-child",
    		gif : "wzb-b09.gif"
    	},
    	/** 基础数据管理  **/	
    	'FTN_AMD_BASE_DATA_MGT' : {
    		background : "#999900",
    		icon : "fa-suitcase",
    		gif : "wzb-b10.gif"
    	},
    	/** 系统设置  **/	
    	'FTN_AMD_SYSTEM_SETTING_MGT' : {
    		background : "#19b2ed",
    		icon : "fa-wrench",
    		gif : "wzb-b12.gif"
    	},
    	/** 学习地图管理  **/	
    	'FTN_AMD_STUDY_MAP_MGT' : {
    		background : "#996699",
    		icon : "fa-maxcdn",
    		gif : "wzb-b12.gif"
    	},
    	/** 设备管理  **/	
    	'FTN_AMD_FACILITY_MGT' : {
    		background : "",
    		icon : "fa-road",
    		gif : "wzb-b11.gif"
    	},
    	/** 培训报表管理  **/	
    	'FTN_AMD_TRAINING_REPORT_MGT' : {
    		background : "#996699",
    		icon : "fa fa-table",
    		gif : "wzb-b08.gif"
    	},
    	/** 任务  **/	
    	'TASK_MANAGEMENT' : {
    		background : "#13e1d5",
    		icon : "fa fa-flag-o",
    		gif : ""
    	}
    	
    };
    
    if(belong_module && belong_module != ""){
    	$("#main_banner").css("background",bannerJson[belong_module].background)
    					 .find(".fa.wzb-banner-icon").addClass(bannerJson[belong_module].icon);
        $("#banner_title").text(fetchLabel(belong_module)).addClass("font-size30");
        $(".banner_gif")[0].src = "${ctx}/static/images/"+bannerJson[belong_module].gif;
    }
    
    if(parent_code && parent_code != ""){
    	$("a#parent_title").text(fetchLabel(parent_code));
    	var FTN_AMD_SYS_SETTING_LOG = ('<lb:get key="FTN_AMD_SYS_SETTING_LOG"/>');//系统运作日志
    	var FTN_AMD_SYS_SETTING_MAIN = ('<lb:get key="FTN_AMD_SYS_SETTING_MAIN"/>');//系统设置
    	var parent_title = $("a#parent_title").text();
    		$("a#parent_title").unbind("click");
    		if(parent_title == FTN_AMD_SYS_SETTING_LOG || parent_title == FTN_AMD_SYS_SETTING_MAIN) {
    			$("a#parent_title").css("cursor","default");
    		} else {
	        	$("a#parent_title").bind("click",function(){
	        		eval("javascript:wb_utils_nav_go('"+parent_code+"', '${prof.usr_ent_id }', '${label_lan }')");
	        	});
    		}
    }else{
    	$("a#parent_title").parent().first().hide();
    }
    
    var nav_ = $("div.NavLink").html();
    if(nav_ != undefined && nav_ != '') {
    	var target=$("li#target_page_title");
    	target.html(nav_).show();
    	target.removeClass('active');
     $("div.NavLink").hide();
    }
    else if(page_title && page_title != ""){
    	$("li#target_page_title").text(page_title);
    }else{
    	$("li#target_page_title").text($(".panel.wzb-panel.textleft").find(".panel-heading").text());
    }
    
    
   $("#main_banner").fadeTo(1000,1);
   $(".wzb-menu-son").each(function(){
	   if(belong_module == $(this).attr("data")){
		 $(this).next("ul").css("display", "block");
	  	 $(this).parent("li").addClass("cur").siblings().removeClass("cur")
	   }
   });
   if($("#msg_box").val()=='wb_msg_box'){
	   var height=$(".wzb-banner").height()+$(".breadcrumb").height()+$(".panel-heading").height();
	   $("#main_banner").hide();
	   $(".wzb-banner").hide();
	   $(".breadcrumb").hide();
	   $(".panel-heading").hide();
	   $(".panel-body").height($(".panel-body").height()+height); 
   }
   
   //由于xsl 中 alert 比较多 样式比较丑，所以 在这里直接替换掉
   window.alert = function(text){
	   Dialog.alert(text);
   }
})
</script>
</head>
<body <decorator:getProperty property="body.onload" writeEntireProperty="true" /> >
    <div class="wzb-body">
        <jsp:include page="../views/admin/common/menu.jsp"></jsp:include>
        <div class="wzb-wrapper">
            <div class="wzb-main" >
                
                <div class="wzb-banner-bg01" style="opacity:1;background:rgb(25, 178, 237);" id="main_banner">
		            <div class="wzb-banner"><i class="fa wzb-banner-icon"></i><span id="banner_title"></span></div>
		            <span class="wzb-banner-right"><img class="banner_gif" src="../../static/images/wzb-b12.gif" alt=""></span>
		        </div>

                <ol class="breadcrumb wzb-breadcrumb textleft  heder-nav">
                    <li class='header-index heder-nav'><a href="javascript:wb_utils_gen_home(true);"><i class="fa wzb-breadcrumb-home fa-home"></i><lb:get key="global.lab_menu_started" /><!-- 首页--></a></li>
                    <li><a href="javascript:void(0)" id="parent_title"></a></li>
                    <li class="active" id="target_page_title" ></li>
                </ol>
                <!-- wzb-breadcrumb End-->

                <div class="panel wzb-panel textleft">
                     <div class="panel-heading"> </div>

                    <div class="panel-body">
                        <decorator:body />
                    </div>
                </div>
                <!-- wzb-panel End-->

            </div>
            <!-- wzb-main End-->

        </div>
        <!-- wzb-wrapper End-->

        <%@ include file="../views/common/footer.jsp"%>
    </div>
</body>
</html>