<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-learner.css" />
<script type="text/javascript"
	src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<script type="text/javascript" src="${ctx}/js/urlparam.js"></script>
<script src="/js/wb_announcement.js" type="text/javascript" ></script>
<script src="/js/wb_item.js" type="text/javascript" ></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_im_${lang}.js"></script>

<title></title>
<style type="text/css">
.datatable .datatable-pager { width: 227px; margin-left: -17px; }
</style>
<script type="text/javascript">
	var announcement = new wbAnnouncement;
	var itm_lst = new wbItem;
	var encrytor = wbEncrytor();
	// 公告
	var dt = null;
	$(function(){
		detail(${id})
	})

	

	function detail(id){
		if(id == '' || id == undefined) {
			return;
		}
		$.getJSON("${ctx}/app/admin/announce/detailJson/" + id, function(result){
			var data = result.detail;
			data.msg_body = data.msg_body.replace(/<img/g,'<img ');
			var p = {
	 			msg_id : data.msg_id,
	 			content : Wzb.htmlDecode(data.msg_body),
	 			title : data.msg_title,
	 			username : data.user.usr_display_bil,
	 			time : data.msg_begin_date,
	 			hasReceipt : data.msg_receipt,
	 			status : data.msg_status,
	 			msg_type : data.msg_type,
	 			msg_res_id : data.msg_res_id,
	 			isRead : result.isReadOnly,
	 			exam_ind : '${msg_belong_exam_ind}'
	 	 	}
	 	 	var html = $('#announceDetailTemplate').render(p);
	 	 	$("#announceCont").html(Wzb.htmlDecode(html));
	 	 	$("#announceTitle").html(data.msg_title);
	 	 	$(".breadcrumb .active").html(data.msg_title);
	 	 	
	 	 	if(result.isReceipted){
	 	 		changReceiptBtn();
	 	 	}
		})
		//选中的图标
		$("#announce_list li").removeClass("now");
		$("#msg" + id).parent().addClass("now");
	}
	
	function changeTcrId(){
		$("#announce_list").html('');
		dt = $("#announce_list").table({
			url : '${ctx}/app/admin/announce/pageJson/' + $("#tcr_id_list :selected").val(),
			colModel : colModel,
			rp : 10,
			showpager : 3,
			hideHeader : true,
			usepager : true,
			trLine : false,
			onSuccess : function(){
				$("#announceCont").html('');
				detail(${id});	//选中第一个
			}
		});
	}
	
	function receipt(msgId){
		var result;
		$.ajax({
			url:'${ctx}/app/admin/announce/getReceipt/'+msgId,
			type:'post',
			dataType : 'json',
			async:false,
			success:function(data){
				result = data;
			}
		});
		return result;
	}
	
	//公告回执按钮
	function insReceipt(msgId){
		$.ajax({
			url:'${ctx}/app/admin/announce/insReceipt/' + msgId,
			type:'post',
			async:false,
			success:function(){
				changReceiptBtn();
			}
		});
	}
	
	function changReceiptBtn(){
		$("#annReceiptBtn").css({"background":"#999999"});
		$("#annReceiptBtn").attr("disabled","true");
	}
</script>

<!-- template start -->
<script id="announceTemplate" type="text/x-jquery-tmpl">
<li>
	<a class="wzb-link02 f16" href="javascript:;" id="msg{{>msg_id}}" onclick="detail({{>msg_id}})" title="{{>title}}"><i class="glyphicon glyphicon-volume-up"></i>{{>title}}</a>
	<p>{{>time.substring(0,10)}}</p>
</li>
</script>
<script id="announceDetailTemplate" type="text/x-jquery-tmpl">
<div class="entity-box">
	<h1 class="entity-title font-size24">{{>title}}</h1>
	<div class="entity-info" >
		<span class="f14" style="color:#999;">{{>time}}</span>
        <span class="f14" style="color:#999;">
             {{if status == 'ON'}}
         	    (<lb:get key='label_im.label_core_information_management_13'/>)
             {{/if}}
             {{if status == 'OFF'}}
         	    (<lb:get key='label_im.label_core_information_management_14'/>)
             {{/if}}
        </span>
        {{if isRead}}
           <span  style="float:right;margin-left: 5px;" id="receiptBtn">
			   <input type="button" class="btn wzb-btn-orange"  value="<lb:get key='label_im.label_core_information_management_3'/>" onclick="{{if msg_type == 'RES'}}announcement.upd_sys_ann_lst('{{>msg_type}}',encrytor.cwnEncrypt('{{>msg_id}}'),'{{>msg_res_id}}',true,'','','',{{>exam_ind}})
					{{else}}wbAnnouncementUpdSysAnnLst('{{>msg_type}}',encrytor.cwnEncrypt('{{>msg_id}}'),'{{>msg_res_id}}','','','true',''){{/if}}"/>
		   </span>
           {{else}}
             {{if hasReceipt}}
			   <span  style="float:right" id="receiptBtn">
				   <input type="button" class="btn wzb-btn-orange" id="annReceiptBtn" value="<lb:get key='label_im.label_core_information_management_21'/>" onclick="insReceipt({{>msg_id}})"/>
			   </span>
		     {{/if}}
        {{/if}}
        
        
	</div>
</div>

<div class="cont show-li">				
	<div class="messcont cont-em">
		<p style="align:left; text-indent: 0em;"><div id="content" style="display:none;">{{>content}}</div></p>
	</div>
</div>
</script>
<!-- template end -->
</head>
<body>
	<c:choose>
		
		<c:when test="${type eq 'RES' }">
			<title:get function="global.FTN_AMD_TRAINING_MGT"/>	
			<ol class="breadcrumb wzb-breadcrumb">
				<li><a href="javascript:wb_utils_gen_home(true);"><i class="fa wzb-breadcrumb-home fa-home"></i><lb:get key="label_im.label_core_information_management_24"/> </a></li>
				<li><a href="javascript:wb_utils_nav_go('FTN_AMD_ITM_COS_MAIN', '${loginUser.usr_ent_id }', '${label_lan }')"><lb:get key="label_tm.label_core_training_management_2"/></a></li>
				<li><a href="javascript:itm_lst.get_item_detail(${aeItem.itm_id})">${aeItem.itm_title}</a></li>
				<li><a href="javascript:announcement.sys_lst('all','RES','${aeItem.course.cos_res_id}','','','','','',true)">
					<c:choose>
						<c:when test="${msg_belong_exam_ind}">
							<lb:get key="label_tm.label_core_training_management_287"/>
						</c:when>
						<c:otherwise>
							<lb:get key="label_tm.label_core_training_management_236"/>
						</c:otherwise>
					</c:choose>
				</a></li>
				<li class="active"><lb:get key="label_im.label_core_information_management_22"/></li>
			</ol>
		</c:when>
	 	<c:otherwise>
			<title:get function="global.FTN_AMD_ARTICLE_MGT"/>	
			<ol class="breadcrumb wzb-breadcrumb">
				<li><a href="javascript:wb_utils_gen_home(true);"><i class="fa wzb-breadcrumb-home fa-home"></i><lb:get key="label_im.label_core_information_management_24"/> </a></li>
				<c:choose>
					<c:when test="${type eq 'read_only' }">
						<li><a href="javascript:wb_utils_nav_go('FTN_AMD_SYS_MSG_LIST', '${loginUser.usr_ent_id }', '${label_lan }')"><lb:get key="label_im.label_core_information_management_6"/></a></li>
					</c:when>
					<c:otherwise>
						<li><a href="javascript:wb_utils_nav_go('FTN_AMD_MSG_MAIN', '${loginUser.usr_ent_id }', '${label_lan }')"><lb:get key="label_im.label_core_information_management_23"/></a></li>
					</c:otherwise>
				</c:choose>
				<li class="active"><lb:get key="label_im.label_core_information_management_22"/> </li>
			</ol>
		</c:otherwise>
	</c:choose>
	<!-- wzb-breadcrumb End-->

	<div class="panel wzb-panel">
		<div class="panel-heading" id="announceTitle"></div>

		<div class="panel-body" >
		<!-- 
			<form class="form-search">
				<a type="button" class="btn wzb-btn-yellow" href="javascript:announcement.upd_sys_ann_lst('SYS','${param.id}','', '', '','true', 'false')" ><lb:get key="global.button_update"/> </a> 
				<a type="button" class="btn wzb-btn-yellow" href="javascript:announcement.show_receipt_views('${param.id}')" ><lb:get key="label_im.label_core_information_management_4"/> </a>
			</form>
		-->
			<div class="panel-content1" id="announceCont" >

			</div>

		</div>
	</div>
	<script type="text/javascript">
	 // 解决显示编辑器编辑好的字体代码时不受*{}全局影响
	 var time = setInterval(function(){
		 var contentHtml = $('#content').html();
		 //var height = $('#content').height() + 30;
		 if(contentHtml != null && contentHtml != 'undefined' && contentHtml.length > 0){
			 clearInterval(time);
			 $('#content').html('<iframe id="iframe"  src ="" width="100%" scrolling="no" frameborder="0"></iframe>');
			 $("#iframe").contents().find("body").append(contentHtml);
			 var bodyContentHeight = $('#iframe').contents().find('body').height();
			 $('#iframe').height(bodyContentHeight + 15);
		 }
	 },100);
	</script>
	<!-- wzb-panel End-->
</body>
</html>