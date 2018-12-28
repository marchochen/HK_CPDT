<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<script type="text/javascript" src="${ctx}/static/js/login_label.js"></script>
<c:if test="${not empty message}">
<script type="text/javascript" src="${ctx}/static/js/jquery-webox.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			if($('#menu').attr("id") != undefined){
				//$('#menu').after($('#flashTemplate').render({
				//	msg : fetchLabel('${message}')
				//}));
				Dialog.alert(fetchLabel('${message}'));
			} else {
				//$('#top').after($('#flashTemplate').render({
				//	msg : fetchLabel('${message}')
				//}));
				Dialog.alert(fetchLabel('${message}'));
			}
            
			$(${focus}).focus();
            
		/* 	$.webox({
				height:140,
				width:500,
				bgvisibel:true,
				title:'',
				html:$("#flash").html(),
			}); */
			
			setTimeout(function(){
				 $("#background").fadeOut(2100);
			     $("#webox").fadeOut(2100);
			  },3000);
		});
		
	</script>
	<script id="flashTemplate" type="text/x-jquery-tmpl">
		<div class="ui-widget" id="flash">
			<div class="ui-state-error ui-corner-all" style="padding-right: 50px;">
				<p>{{>msg}}</p>
			</div>
		</div>
	</script>	
</c:if>
<script type="text/javascript">
	$(function(){
		$("#fpd_title span").html(labels['${lang}']['login_find_password']);
		$("#fpd_top strong").html(labels['${lang}']['login_email_find']);
		$("#fpd_top2").html(labels['${lang}']['login_find_pwd_desc']);
		$("#fpd_login").html(labels['${lang}']['login']);
		$("#fpd_usr_id").html(labels['${lang}']['usr_name']);
		$("#fpd_usr_pwd").html(labels['${lang}']['login_register_email']);
		$("#fpd_ok").val(labels['${lang}']['button_ok']);
		$("#fpd_cancel").val(labels['${lang}']['button_cancel']);
	})
	
	function submitForm() {
		if ($("input[name='usr_ste_usr_id']").val() == '') {
			Dialog.alert(fetchLabel('error_please_input')
					+ fetchLabel('usr_name'));
			return;
		}
		if ($("input[name='usr_email']").val() == '') {
			Dialog.alert(fetchLabel('login_register_email_error'));
			return;
		}
		$("#findPwdForm").submit();
	}
	window.onload = function(){
		$('#menu').height(0)
		//console.log($('#menu'))
	}
</script>
</head>
<body>
	<div class="xyd-wrapper">
		<div class="xyd-whole">
			<div class="xyd-whole-title skin-bg" id="fpd_title">
				<span></span>
				<i class="fa fa-sanzuo fa-caret-up"></i>
			</div>

			<div class="xyd-whole-main">
				<div class="xyd-whole-top">
					<span class="pull-left" id="fpd_top"><strong
						class="margin-right15 color-gray333"></strong><span id="fpd_top2"></span></span> <span
						class="pull-right"><a class="wzb-link04"
						href="${ctx}/app/user/userLogin/$" 
						target="_blank" id="fpd_login"></a></span>
				</div>
				<!-- xyd-whole-top End-->

				<form name="wyd-form" id="findPwdForm" action="findPassword" method="post">
					<div class="wyd-whole-box">
						<table class="margin-top15">
							<tr>
								<td class="wzb-form-label"><span id="fpd_usr_id"></span>：</td>

								<td class="wzb-form-control"><input type="text"
									class="form-control wzb-text-05" name="usr_ste_usr_id" value="">
								</td>
							</tr>

							<tr>
								<td class="wzb-form-label"><span id="fpd_usr_pwd"></span>：</td>

								<td class="wzb-form-control"><input type="text"
									class="form-control wzb-text-05" name="usr_email" value="">
								</td>
							</tr>

							<tr>
								<td class="wzb-form-label"></td>

								<td class="wzb-form-control"><input type="button"
									class="btn wzb-btn-yellow margin-right15"
									value='' onclick="submitForm()" id="fpd_ok">
									<input type="button" class="btn wzb-btn-yellow"
									onclick="javascript:window.location.href='${ctx}/app/user/userLogin/$'"
									value='' id="fpd_cancel"></td>
							</tr>
						</table>
					</div>
					<input type="hidden" name="lang" value="${lang }">
				</form>

			</div>
			<!-- xyd-whole-main End-->

		</div>
	</div>
	<!-- xyd-wrap End-->
	
	<div id="menu"></div>
	<div id="top"></div>
</body>
</html>