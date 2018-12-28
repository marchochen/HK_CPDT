<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-learner.css" />
<script type="text/javascript"
	src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<script type="text/javascript" src="${ctx}/js/urlparam.js"></script>

<title></title>
<style type="text/css">
.datatable .datatable-pager { padding:5px 15px; }
</style>
<script type="text/javascript">
	// 公告
	var dt = null;
	$(function(){
		$.ajax({
			url : '${ctx}/app/announce/getTcrIdList',
			type : 'POST',
			dataType : 'json',
			async : false,
			success : function(data){
				var html = $("#tcr_id_list").html();
				for(var i=0;i<data.tcr_id_list.length;i++){
					html += '<option value="' + data.tcr_id_list[i].tcr_id + '">' + data.tcr_id_list[i].tcr_title + '</option>';
				}
				$("#tcr_id_list").html(html);
			}
		});
		
		dt = $("#announce_list").table({
			url : '${ctx}/app/announce/pageJson/' + $("#tcr_id_list :selected").val(),
			colModel : colModel,
			rp : 10,
			showpager : 3,
			hideHeader : true,
			usepager : true,
			onSuccess : function(){
				var id = getUrlParam("id");
				detail(id);	//选中第一个
			}
		});
		
	});

	var colModel = [ {
		format : function(data) {
			var p = {
				msg_id : data.msg_id,	
				title : data.msg_title,
				usr_display_bil : data.user.usr_display_bil,
				time : data.msg_upd_date,
				curuserisread : data.curUserIsRead
			};
			return $('#announceTemplate').render(p);
		}
	}];

	function getIsOrNotRead(msgid){
		$.getJSON("${ctx}/app/announce/CheckRead/" + msgid, function(result){
			var data = result.detail;
	 		if(result.isNotRead){
	 			$("#msg" +data.msg_id).css({"color":""});
	 		}else{
	 			$("#msg" +data.msg_id).css({"color":"#428bca"});
	 		}
		});
	}
	
	function detail(id){
		
		var sz = $("#announce_list li").size();
		if((id == '' || id == undefined) && sz > 0) {
			id = $("#announce_list li a:eq(0)").attr("id").replace("msg","");
		} else if(sz<1){
			return;
		}
		$.getJSON("${ctx}/app/announce/detailJson/" + id, function(result){
			var data = result.detail;
			data.msg_body = data.msg_body.replace(/<img/g,'<img');
			var p = {
	 			msg_id : data.msg_id,
	 			content : Wzb.htmlDecode(data.msg_body),
	 			title : data.msg_title,
	 			username : data.user.usr_display_bil,
	 			time : data.msg_begin_date,
	 			hasReceipt : data.msg_receipt
	 	 	};
	 	 	var html = $('#announceDetailTemplate').render(p);
			//console.log(data)
	 	 	$("#announceCont").html(Wzb.htmlDecode(html));
	 	 	if(result.isReceipted){
	 	 		changReceiptBtn();
	 	 	}
	 	 	if(!data.msg_receipt){
	 	 		if(result.isNotRead){
					$("#msg" + id).removeClass("skin-color");
					$("#msg" + id).children().removeClass("skin-color");
		 		}else{
		 			$("#msg" + id).css({"color":"#428bca"});
		 		}
	 	 	}
	 	 	
		});
		//选中的图标
		$("#announce_list li").removeClass("now");
		$("#msg" + id).parent().addClass("now");
		
	}
	
	function changeTcrId(){
		$("#announce_list").html('');
		dt = $("#announce_list").table({
			url : '${ctx}/app/announce/pageJson/' + $("#tcr_id_list :selected").val(),
			colModel : colModel,
			rp : 10,
			showpager : 3,
			hideHeader : true,
			usepager : true,
			trLine : false,
			onSuccess : function(){
				$("#announceCont").html('');
				detail("${param.id}");	//选中第一个
			}
		});
	}
	
	function receipt(msgId){
		var result;
		$.ajax({
			url:'${ctx}/app/announce/getReceipt/'+msgId,
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
			url:'${ctx}/app/announce/insReceipt/' + msgId,
			type:'post',
			async:false,
			success:function(){
				changReceiptBtn();
				getIsOrNotRead(msgId);//刷新当前属性用
				$("#msg" + msgId).removeClass("skin-color");
				$("#msg" + msgId).find("i").removeClass("skin-color");
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
     <a title="" href="javascript:;" class="{{if !curuserisread}}skin-color{{/if}}" id="msg{{>msg_id}}" onclick="detail({{>msg_id}})" title="{{>title}}">
        <i class="fa fa-volume-down {{if !curuserisread}}skin-color{{/if}}"></i>
        {{>title}}
     </a>
     <p>{{>time.substring(0,10)}}</p>
</li>
</script>

<script id="announceDetailTemplate" type="text/x-jquery-tmpl">
<div class="wzb-model-9">
	 <div class="wzb-entity-content">
		  <h1 class="wzb-entity-title">{{>title}}</h1>
		  <div class="wzb-entity-info">
			{{if hasReceipt}}
				<span class="pull-right" id="receiptBtn">
					<input type="button" class="btn wzb-btn-orange" id="annReceiptBtn" value="<lb:get key='announce_readed'/>" onclick="insReceipt({{>msg_id}})"/>
				</span>
			{{/if}} {{>time}}
		 </div>
	 </div>
</div>

<div class="wzb-model-10 show-li fuwenben-list">
	 <p style="text-indent: 0em; "><div id="content" >{{>content}}</div></p>
</div>
</script>

</head>
<body>

<div class="xyd-wrapper">
	<div class="xyd-main clearfix">
		<div class="xyd-sidebar">
			<h3 class="wzb-title-4 skin-bg">
				<lb:get key="menu_announce" /><i class="fa fa-sanzuo fa-caret-up"></i>
			</h3>

			<div class="wzb-model-8">
				<select style="padding-right:0px;" class="form-control" id="tcr_id_list" onchange="changeTcrId()">
					<option value="0"><lb:get key="status_all" /></option>
				</select>
			</div>

			<div class="wzb-model-7">
				<ul class="wzb-list-11" id="announce_list">
				</ul>
			</div>

		</div>
		<!-- xyd-sidebar End-->

		<div class="xyd-content" id="announceCont">
		</div>
		<!-- xyd-content End-->
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
</body>
</html>