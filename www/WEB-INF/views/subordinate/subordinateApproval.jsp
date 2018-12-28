<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<%@ include file="../common/template.jsp"%>
<%@ page import="com.cw.wizbank.qdb.*"%>
<%@ page import="com.cw.wizbank.util.cwXSL"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-learner.css" />
<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<script type="text/javascript" src="${ctx}/static/js/front/course.js"></script>
<script type="text/javascript" src="${ctx}/js/wb_goldenman.js"></script>
<script type="text/javascript" src="${ctx}/js/wb_mgt_rpt.js"></script>
<script type="text/javascript" src="${ctx}/js/wb_mystaff.js"></script>
<script type="text/javascript" src="${ctx}/js/my_staff_report.js"></script>
<title></title>
<script type="text/javascript">
	var workflowData = ${workflowData};
	$(function (){
		$(".active").click();
	})

	function changeTab(thisObj){
		$(".active").removeClass("active");
		$(thisObj).addClass("active");
		if($(thisObj).attr("name") == 'PENDING'){
			getApprovalList(true);
			$("#approval_operate").show();
		} else {
			getApprovalList(true);
			$("#approval_operate").hide();
		}
	}
	
	function getApprovalList(usepager){
		var colModel = approvalColModel;
		if($(".active").attr("name") == 'PENDING'){
			colModel = pendingColModel;
		}
		$(".wbtable").empty().table({
			url : '${ctx}/app/subordinate/getApprovalList/' + $(".active").attr("name"),
			colModel : colModel,
			rp : 10,
			showpager : 5,
			usepager : true,
			onSuccess: function(data){
				var total = data.params.total;
				if(total == 0){
					$("#approval_operate").hide();
				}
			}
		})
	}
	
	var pendingColModel = [ {
		display : '<input type="checkbox" class="qzsel" onclick="selectAll(this)"/>',
		width : '3%',
		format : function(data) {
			p = {
				app_id : data.app_id,
				app_upd_timestamp : data.app_upd_timestamp,
				usr_display_bil : data.user.usr_display_bil
			}
			return $('#checkTemplate').render(p);
		}
	}, {
		display : fetchLabel('usr_name'),
		width : '15%',
		format : function(data) {
			p = {
				text : data.user.usr_ste_usr_id
			};
			return $('#text-template').render(p);
		}
	}, {
		display : fetchLabel('usr_full_name'),
		width : '15%',
		format : function(data) {
			p = {
				text : data.user.usr_display_bil,
			};
			return $('#text-template').render(p);
		}
	}, {
		display : fetchLabel('subordinate_train_name'),
		width : '25%',
		format : function(data) {
			p = {
				text : data.item.itm_title
			};
			return $('#text-template').render(p);
		}
	}, {
		display : fetchLabel('cos_start_time'),
		width : '15%',
		align : 'center',
		format : function(data) {
			var start_time = fetchLabel('subordinate_complete_approval');
			if(data.item.itm_eff_start_datetime != undefined){
				start = data.item.itm_eff_start_datetime.substring(0, 10);
			}
			p = {
				text : start_time
			};
			return $('#text-template').render(p);
		}
	}, {
		display : fetchLabel('subordinate_apply_time'),
		width : '16%',
		align : "center",
		format : function(data) {
			p = {
				text : data.app_create_timestamp.substring(0, 10)
			};
			return $('#text-center-template').render(p);
		}
	}]
	
	var approvalColModel = [ {
		display : fetchLabel('usr_name'),
		width : '15%',
		format : function(data) {
			p = {
				text : data.user.usr_display_bil
			};
			return $('#text-template').render(p);
		}
	}, {
		display : fetchLabel('usr_full_name'),
		width : '15%',
		format : function(data) {
			p = {
				text : data.user.usr_ste_usr_id
			};
			return $('#text-template').render(p);
		}
	}, {
		display : fetchLabel('subordinate_train_name'),
		width : '20%',
		format : function(data) {
			p = {
				text : data.item.itm_title
			};
			return $('#text-template').render(p);
		}
	}, {
		display : fetchLabel('lab_kb_status'),
		width : '10%',
		format : function(data) {
			if(data.aal_action_taken == 'DISAPPROVED') {
				status = fetchLabel('subordinate_DISAPPROVED2');
			} else if(data.aal_action_taken == 'APPROVED') {
				status = fetchLabel('subordinate_APPROVED2');
			}
			p = {
				text : status
			};
			return $('#text-template').render(p);
		}
	}, {
		display : fetchLabel('cos_start_time'),
		width : '15%',
		align : 'center',
		format : function(data) {
			var start_time = fetchLabel('subordinate_complete_approval');
			if(data.item.itm_eff_start_datetime != undefined){
				start = data.item.itm_eff_start_datetime.substring(0, 10);
			}
			p = {
				text : start_time
			};
			return $('#text-template').render(p);
		}
	}]
	
	function pass() {
		var app_id_lst = getSelectedAppId();
		var frm = document.frmAction;
		if (app_id_lst !== '') {
			str_feature = 'toolbar=' + 'no' + ',width=' + '450' + ',height=' + '200' + ',scrollbars=' + 'no' + ',resizable=' + 'no' + ',status=' + 'yes';
			wb_utils_set_cookie('appn_usr_name', '');
			wb_utils_set_cookie('current', '');
			wb_utils_set_cookie('total', '');
			var itm_id = 0;
			var lang = wb_label_lan;
			var process_id = 1;
			var status_id = 1;
			var action_id = 1;
			var fr = Wzb.workflow.getActnHistoryFr(workflowData, process_id, status_id, action_id);
			var to = Wzb.workflow.getActnHistoryTo(workflowData, process_id, status_id, action_id);
			var verb = Wzb.workflow.getActnHistoryVerb(workflowData, process_id, status_id, action_id);
			frm.verb.value = verb;
			frm.fr.value = fr;
			frm.to.value = to;
			url = '${ctx}/htm/application_frame_window.htm?run_id=' + itm_id + '&lang=' + lang + '&process_id=' + process_id + '&status_id=' + status_id + '&action_id=' + action_id + '&fr=' + fr + '&to=' + to + '&verb='
					+ verb + '&functionName=doFeedParam&processEndFunction=reload_me';
			winob = wbUtilsOpenWin(url, 'application_enrollment_status', false, str_feature);
		}
	}

	function refuse() {
		var app_id_lst = getSelectedAppId();
		var frm = document.frmAction;
		if (app_id_lst !== '') {
			str_feature = 'toolbar=' + 'no' + ',width=' + '450' + ',height=' + '200' + ',scrollbars=' + 'no' + ',resizable=' + 'no' + ',status=' + 'yes';
			wb_utils_set_cookie('appn_usr_name', '');
			wb_utils_set_cookie('current', '');
			wb_utils_set_cookie('total', '');
			var itm_id = 0;
			var lang = wb_label_lan;
			var process_id = 1;
			var status_id = 1;
			var action_id = 2;
			var fr = Wzb.workflow.getActnHistoryFr(workflowData, process_id, status_id, action_id);
			var to = Wzb.workflow.getActnHistoryTo(workflowData, process_id, status_id, action_id);
			var verb = Wzb.workflow.getActnHistoryVerb(workflowData, process_id, status_id, action_id);
			frm.verb.value = verb;
			frm.fr.value = fr;
			frm.to.value = to;
			url = '${ctx}/htm/application_frame_window.htm?run_id=' + itm_id + '&lang=' + lang + '&process_id=' + process_id + '&status_id=' + status_id + '&action_id=' + action_id + '&fr=' + fr + '&to=' + to + '&verb='
					+ verb + '&functionName=doFeedParam&processEndFunction=reload_me';
			winob = wbUtilsOpenWin(url, 'application_enrollment_status', false, str_feature);

		}
	}
	
	function getSelectedAppId() {
		var listObj = $("input[name='app_id_list']:checked");
		if(listObj.length == 0){
			Dialog.alert(fetchLabel('subordinate_please_select'));
			return '';
		} else {
			var app_id_lst = listObj.eq(0).val();
			for(var i=1;i<listObj.length;i++){
				app_id_lst += '~' + listObj.eq(i).val();
			}
			return app_id_lst;
		}
	}
	
	function selectAll(thisObj){
		if($(thisObj).attr("checked") == 'checked'){
			$("input[name='app_id_list']").attr("checked", true);
		} else {
			$("input[name='app_id_list']").attr("checked", false);
		}
	}
	
	function doFeedParam() {
		param = new Array();
		tmpObj1 = new Array();
		tmpObj2 = new Array();
		tmpObj3 = new Array();
		tmpObj4 = new Array();
		tmpObj5 = new Array();

		tmpObj1[0] = 'cmd';
		tmpObj1[1] = 'ae_make_multi_actn';
		param[param.length] = tmpObj1;

		tmpObj2[0] = 'app_id_lst';
		app_id_lst = getSelectedAppId();
		tmpObj2[1] = app_id_lst.split('~');
		param[param.length] = tmpObj2;

		tmpObj3[0] = 'app_nm_lst';
		app_nm_lst = getSelectedActionNameLst();
		tmpObj3[1] = app_nm_lst.split('~');
		param[param.length] = tmpObj3;

		tmpObj4[0] = 'app_upd_timestamp_lst';
		app_upd_timestamp_lst = getSelectedActionTimestampLst();
		tmpObj4[1] = app_upd_timestamp_lst.split('~');
		param[param.length] = tmpObj4;

		tmpObj5[0] = 'content';
		var content = $(".showhide").val();
		if(content == fetchLabel('subordinate_approval_reason')){
			content = '';
		}
		tmpObj5[1] = content;
		param[param.length] = tmpObj5;

		return param;
	}
	
	function getSelectedActionNameLst() {
		var nm_lst = '';
		var sel = document.getElementsByName("names");
		if (sel.length == 0) {
			alert(eval('wb_msg_' + wb_label_lan + '_select_applicant'));
			return;
		}
		var selid = getSelections();
		var len = sel.length;
		for (var i = 0; i < len; i++) {
			if (selid[i].checked) {
				nm_lst += sel[i].value;
				if (i !== len - 1) {
					nm_lst += '~';
				}
			}
		}

		if (nm_lst.lastIndexOf('~') > 0 && nm_lst.lastIndexOf('~') == nm_lst.length - 1) {
			nm_lst = nm_lst.substring(0, nm_lst.lastIndexOf('~'));
		}
		return nm_lst;
	}
	
	function getSelectedActionTimestampLst() {

		var nm_lst = '';
		var sel = document.getElementsByName("times");
		if (sel.length == 0) {
			alert(eval('wb_msg_' + wb_label_lan + '_select_applicant'));
			return;
		}
		var selid = getSelections();
		var len = sel.length;
		for (var i = 0; i < len; i++) {
			if (selid[i].checked) {
				nm_lst += sel[i].value;
				if (i !== len - 1) {
					nm_lst += '~';
				}
			}
		}
		if (nm_lst.lastIndexOf('~') > 0 && nm_lst.lastIndexOf('~') == nm_lst.length - 1) {
			nm_lst = nm_lst.substring(0, nm_lst.lastIndexOf('~'));
		}
		return nm_lst;
	}
	
	function getSelections() {
		var arrSon = document.getElementsByName("app_id_list");
		return arrSon;
	}
	
	function reload_me() {
		$(".showhide").val(fetchLabel('subordinate_approval_reason'));
		self.location.reload();
		winob.close();
	}
	
</script>
<script id="checkTemplate" type="text/x-jsrender">
<input type="checkbox" class="qzsel" name="app_id_list" value="{{>app_id}}"/>
<input type="hidden" name="times" value="{{>app_upd_timestamp}}"/>
<input type="hidden" name="names" value="{{>usr_display_bil}}"/>
</script>
</head>
<body>
<div class="xyd-wrapper">
	<div id="main" class="xyd-main clearfix">
		<jsp:include page="subordinateMenu.jsp"></jsp:include>
		
		<div class="xyd-article">
            <div role="tabpanel" class="wzb-tab-3">
				<!-- Nav tabs -->
				<ul class="nav nav-tabs wblearn" role="tablist">
					<li role="presentation" class="active" name="PENDING" onclick="changeTab(this)"><a href="#home" aria-controls="home" role="tab" data-toggle="tab" style="padding-left:0;"><lb:get key="subordinate_pending"/></a></li>
					<li role="presentation" name="HISTORY" onclick="changeTab(this)"><a href="#profile" aria-controls="profile" role="tab" data-toggle="tab"><lb:get key="subordinate_approved"/></a></li>
				</ul>
						
                <div class="tab-content">
                    <div class="wbtabcont">
                        <div class="wbtable mt15"></div>  
                        
                        <div id="approval_operate">
                            <form class="mt30" method="post" action="#">
                                <%-- <textarea name="" class="wzb-textarea-01 align-bottom margin-right10" onfocus="if(value==fetchLabel('subordinate_approval_reason')){value=''}" onblur="if (value ==''){value=fetchLabel('subordinate_approval_reason')}"><lb:get key="subordinate_approval_reason"/></textarea> --%>
                            </form>
                                  
                            <div class="qzcont"><input type="button" class="btn wzb-btn-blue wzb-btn-big margin-right15" name="pertxt" value='<lb:get key="subordinate_APPROVED"/>' onclick="pass()"/><input type="button" class="btn wzb-btn-blue wzb-btn-big" name="pertxt" value='<lb:get key="subordinate_CANCELLED"/>' onclick="refuse()"/></div>
                        </div>  
                    </div>
                </div>
            </div> <!-- wbtab end --> 
		</div> <!-- xyd-article End-->
		
		<form name="frmAction" id="frmAction">
			<input type="hidden" name="app_id_lst" value="" />
			<input type="hidden" name="content" value="" />
			<input type="hidden" name="verb" value="" />
			<input type="hidden" name="to" value="" />
			<input type="hidden" name="fr" value="" />
		</form>
		
	</div>
</div> <!-- xyd-wrapper End-->
</body>
</html>