<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<script type="text/javascript" src="${ctx}/static/js/login_label.js"></script>
<style type="text/css">
	span.error{
		color : red;
		float : none;
		margin-left:5px;
		display: block;
	}
</style>
<script type="text/javascript">
	$(function(){
		$("#rsp_title").html(labels['${lang}']['login_reset_pwd']);
		$("#rsp_top1").html(labels['${lang}']['login_haved_account']);
		$("#rsp_top2").html(labels['${lang}']['login']);
		$("#rsp_usr_id").html(labels['${lang}']['usr_name']);
		$("#rsp_pwd").html(labels['${lang}']['usr_password']);
		$("#rsp_conf_pwd").html(labels['${lang}']['usr_confirm_password']);
		$("#rsp_ok").val(labels['${lang}']['button_ok']);
		$("#rsp_cancel").val(labels['${lang}']['button_cancel']);
	})
	
	function submitForm(){
		if($("input[name='usr_pwd']").val() == ''){
			Dialog.alert(fetchLabel('error_please_input') + fetchLabel('usr_password'));
			return;
		}
		if($("input[name='usr_pwd']").val().length < ${pwdMinLength} || $("input[name='usr_pwd']").val().length > ${pwdMaxLength}){
			Dialog.alert(fetchLabel('usr_password') + fetchLabel('error_over_length') + '${pwdMinLength}-${pwdMaxLength}');
			return;
		}
		checkPassword();
		if($("input[name='confirm_pwd']").next().html() != ''){
			Dialog.alert(fetchLabel('error_pwd_inconsistent'));
			return;
		}
		$("#resetPwdForm").submit();
	}
	
	function checkPassword(){
		if($("input[name='usr_pwd']").val() != $("input[name='confirm_pwd']").val()){
			$("input[name='confirm_pwd']").next().html(fetchLabel("error_pwd_inconsistent"));
		} else {
			$("input[name='confirm_pwd']").next().html('');
		}
	}
</script>
</head>
<body>
<div class="xyd-wrapper">
<div class="xyd-whole">
<div class="xyd-whole-title skin-bg">
     <span id="rsp_title"></span> <i class="fa fa-sanzuo fa-caret-up"></i>
</div>

<div class="xyd-whole-main">
<div class="xyd-whole-top">
     <span class="pull-right"><span id="rsp_top1"></span> <a href="${ctx}/app/user/userLogin/$"><span id="rsp_top2"></span></a></span>
</div> <!-- xyd-whole-top End-->

<form id="resetPwdForm" action="updatePwd" method="post">
<div class="wyd-whole-box">
<table class="margin-top15">
    <tr>
         <td class="wzb-form-label"><span id="rsp_usr_id"></span> :</td>
         
         <td class="wzb-form-control">
             ${usr_ste_usr_id_page}
         </td>
    </tr>
    
    <tr>
         <td class="wzb-form-label"><span id="rsp_pwd"></span> :</td>
         
         <td class="wzb-form-control">
             <input type="password" value="" name="usr_pwd" class="form-control wzb-text-05" maxlength="${pwdMaxLength}">
         </td>
    </tr>
    
    <tr>
         <td class="wzb-form-label"><span id="rsp_conf_pwd"></span> :</td>
         
         <td class="wzb-form-control">
             <input type="password" value="" name="confirm_pwd" class="form-control wzb-text-05" onblur="checkPassword()" maxlength="${pwdMaxLength}"><span class="error"></span>
         </td>
    </tr>
    
    <tr>
         <td class="wzb-form-label"></td>
         
         <td class="wzb-form-control">
             <input type="button" class="btn wzb-btn-yellow margin-right15" value='' id="rsp_ok" onclick="submitForm()">
             <input type="button" class="btn wzb-btn-yellow" value='' id="rsp_cancel" onclick="javascript:window.location.href='${ctx}/app/user/userLogin/$'">
         </td>
    </tr>
</table>
</div>
<input type="hidden" name="prh_id" value="${prh_id}">
<input type="hidden" name="usr_ste_usr_id" value="${usr_ste_usr_id}">
<input type="hidden" name="lang" value="${lang}">
</form>

</div> <!-- xyd-whole-main End-->

</div>
</div> <!-- xyd-wrap End-->
</body>
</html>