<%@ page isELIgnored="false" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.cw.wizbank.util.LangLabel, com.cw.wizbank.qdb.*"%>
<%@ include file="../../common/taglibs.jsp"%>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<meta name="keywords" content="CyberWisdom 59871687287224436811 E-learning" />
<meta http-equiv="Cache-Control" content="no-store" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<title><lb:get key="global.page_title" /></title>
<link rel="icon" href="${ctx}/static/images/favicon.ico" type="image/x-icon" />
<script type="text/javascript">
	wb_image = '${wb_image}', wb_lang = '${lang}', wb_label_lan = '${label_lan}';
	contextPath = '${ctx}'; globalPageSize = '${sessionScope.globalPageSize}', loginUserId = '${auth_login_profile.usr_id}'
</script>


<script type="text/javascript" src="${ctx}/static/js/jquery.js"></script>

<script type="text/javascript" src="${ctx}/static/js/jquery.dialogue.js"></script>
<script type="text/javascript" src="${ctx}/static/js/jquery.qtip/jquery.qtip.js"></script>
<link rel="stylesheet" href="${ctx}/static/js/jquery.qtip/jquery.qtip.css" />

<script type="text/javascript" src="${ctx}/static/js/wb_encrypt_util.js"></script>

<!-- layer -->
<script type="text/javascript" src="${ctx}/static/js/layer/layer.js"></script>
<link rel="stylesheet" href="${ctx}/static/js/layer/skin/layer.css"/>


<script type="text/javascript" src="${ctx}/js/jquery.migrate.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery.bgiframe.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery.menu.js"></script>
<script type="text/javascript" src="${ctx}/js/label_${lang}.js"></script>
<script type="text/javascript" src="${ctx}/js/${label_lan}/wb_label.js"></script>
<script type="text/javascript" src="${ctx}/js/gen_utils.js"></script>
<script type="text/javascript" src="${ctx}/js/wb_utils.js"></script>
<script type="text/javascript" src="${ctx}/js/wzb_utils.js"></script>

<!-- new -->
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/global_${lang}.js"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_${lang}.js"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_project_${lang}.js"></script>
<script type="text/javascript" src="${ctx}/static/js/cwn_utils.js"></script>

<script type="text/javascript" src="${ctx}/static/js/jsrender.js"></script>
<script type="text/javascript" src="${ctx}/static/js/image_doing.js"></script>

<!-- bootstrap -->
<script type="text/javascript" src="${ctx}/static/js/bootstrap/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="${ctx}/static/js/bootstrap/css/bootstrap.css"/>

<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-default.css"/>

<!--[if lt IE 9]>
    <script type="text/javascript" src="${ctx}/static/js/bootstrap/js/html5shiv.min.js"></script>
	<script type="text/javascript" src="${ctx}/static/js/bootstrap/js/respond.min.js"></script>
<![endif]-->

<link rel="stylesheet" href="${ctx}/js/tree/css/ztree.css" />
<link rel="stylesheet" href="${ctx}/static/admin/css/font-awesome/css/font-awesome.min.css" />
<link rel="stylesheet" href="${ctx}/static/css/base.css">
<link rel="stylesheet" href="${ctx}/static/admin/css/admin.css">

<c:if test="${lang eq 'en-us'}">
	<!-- 兼容英文的css -->
	<link rel="stylesheet" href="${ctx}/static/css/base-en.css"/>
	<link rel="stylesheet" href="${ctx}/static/admin/css/admin-en.css"/>
</c:if>

<script type="text/javascript" src="${ctx}/static/admin/js/base.js"></script>

<script id="errorTemplate" type="text/x-jquery-tmpl">
<div class="alert-parent" style="opacity: 1;padding: 10px; position: fixed; z-index:100000000; top:0; width:100%">
    <div class="alert alert-danger alert-dismissible fade in" role="alert">
      <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">×</span></button>
      <div class="content">{{>msg}}<div>
    </div>
<div>
</script>
<script type="text/javascript">
$(function(){
    // 设置jQuery Ajax全局的参数  
    $(document).ajaxSend(function(event,xhr,settings){
        var pdt = new Date();
        if(settings.type == 'POST'){
            if(settings.data != undefined && settings.data.length >0){
                settings.data += "&pdate=" + pdt.getTime();
            }else{
                settings.data = "pdate=" + pdt.getTime();
            }
        }else{
            if(settings.url.lastIndexOf("?") > 0){
                settings.url += "&pdate=" + pdt.getTime();
            } else {
                settings.url += "?pdate=" + pdt.getTime();
            }
        }
    }).ajaxSuccess(function(event,xhr,settings){
        var msg = $.parseJSON(xhr.responseText);
        if(msg != undefined && msg != null && msg.status == 'error'){
            Dialog.alert(msg.msg);
        } 
    }).ajaxError(function(event,xhr,settings){
        if(loginUserId != '' && xhr.status == 500) {
            if(xhr.responseText.indexOf('SessionTimeOutException') > -1) {
                $('.wzb-top').before($('#errorTemplate').render({
                    msg : fetchLabel('error_session_timeout') 
                }));
                $('.alert').on('closed.bs.alert', function () {
                    $(".alert-parent").remove();
                    window.location.href = contextPath + "/app/user/userLogin/$";
                });
            }else if(xhr.responseText.indexOf('MultipleLoginException') > -1){
            	 $('.wzb-top').before($('#errorTemplate').render({
                     msg : fetchLabel('error_landed_somewhere_else') 
                 }));
                 $('.alert').on('closed.bs.alert', function () {
                     $(".alert-parent").remove();
                     window.location.href = contextPath + "/app/user/userLogin/$";
                 });
            }else {
                $('.wzb-top').before($('#errorTemplate').render({
                    msg : fetchLabel('error_server_error')
                }));
                $('.alert').on('closed.bs.alert', function () {
                    $(".alert-parent").remove();
                });
            }
            $(".alert").fadeTo(1000,1);
        } 
    });
});
</script>