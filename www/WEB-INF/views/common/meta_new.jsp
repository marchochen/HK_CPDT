<%@ page isELIgnored="false" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.cw.wizbank.util.LangLabel, com.cw.wizbank.qdb.*"%>
<%@ include file="../common/taglibs.jsp"%>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<meta http-equiv="Cache-Control" content="no-store" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<title><lb:get key="page_title" /></title>
<link rel="icon" href="${ctx}/static/images/favicon.ico" type="image/x-icon" />
<script type="text/javascript">
	wb_image = '${wb_image}',
    wb_lang = '${lang}', 
    wb_label_lan = '${label_lan}';
	contextPath = '${ctx}'; 
    globalPageSize = '${sessionScope.globalPageSize}', 
    loginUserId = '${auth_login_profile.usr_id}'
</script>
<script type="text/javascript" src="${ctx}/static/js/jquery.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery.migrate.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery.bgiframe.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery.menu.js"></script>
<script type="text/javascript" src="${ctx}/js/label_${lang}.js"></script>
<script type="text/javascript" src="${ctx}/js/${label_lan}/wb_label.js"></script>
<script type="text/javascript" src="${ctx}/js/gen_utils.js"></script>
<script type="text/javascript" src="${ctx}/js/wb_utils.js"></script>
<script type="text/javascript" src="${ctx}/js/wzb_utils.js"></script>

<!-- new -->
<script type="text/javascript" src="${ctx}/static/js/i18n/label_${lang}.js"></script>
<%-- <script type="text/javascript" src="${ctx}/static/js/i18n/label_zh-cn.js"></script> --%>
<script type="text/javascript" src="${ctx}/static/js/cwn_utils.js"></script>

<script type="text/javascript" src="${ctx}/static/js/front/tab.js"></script>
<script type="text/javascript" src="${ctx}/static/js/front/nav.js"></script>

<script type="text/javascript" src="${ctx}/static/js/jsrender.js"></script>
<script type="text/javascript" src="${ctx}/static/js/image_doing.js"></script>

<!-- bootstrap -->
<script type="text/javascript" src="${ctx}/static/js/bootstrap/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="${ctx}/static/js/bootstrap/css/bootstrap.css"/>
<link rel="stylesheet" href="${ctx}/static/js/font-awesome/css/font-awesome.min.css"/>

<script type="text/javascript" src="${ctx}/js/jquery.dropdown.js"></script>
<!--[if lt IE 9]>
    <script src="http://cdn.bootcss.com/html5shiv/3.7.0/html5shiv.min.js"></script>
    <script src="http://cdn.bootcss.com/respond.js/1.3.0/respond.min.js"></script>
<![endif]-->

<script type="text/javascript" src="${ctx}/static/js/jquery.dialogue.js"></script>
<script type="text/javascript" src="${ctx}/static/js/jquery.qtip/jquery.qtip.js"></script>

<!-- css -->
<link rel="stylesheet" href="${ctx}/static/admin/css/admin.css"/>
<link rel="stylesheet" href="${ctx}/static/theme/blue/css/style.css"/>
<link rel="stylesheet" href="${ctx}/static/js/jquery.qtip/jquery.qtip.css" />

<c:if test="${lang eq 'en-us'}">
<!-- 兼容英文的css -->
<link rel="stylesheet" href="${ctx}/static/css/other.css"/>
<link rel="stylesheet" href="${ctx}/static/css/base-en.css"/>
</c:if>

<%-- <wzb:tc-style /> --%>
<c:if test="${not empty message}">
<script type="text/javascript" src="${ctx}/static/js/jquery-webox.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			if($('#menu').attr("id") != undefined){
				$('#menu').after($('#flashTemplate').render({
					msg : fetchLabel('${message}')
				}));
			} else {
				$('#top').after($('#flashTemplate').render({
					msg : fetchLabel('${message}')
				}));
			}
            
			$(${focus}).focus();
            
			$.webox({
				height:140,
				width:500,
				bgvisibel:true,
				title:'',
				html:$("#flash").html(),
			});
			
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
<script id="globleMsgBoxTemplate" type="text/x-jquery-tmpl">
<div id="globleMsgBox" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
       <div class="modal-dialog" style="width:650px;">
         <div class="modal-content">
           <div class="modal-header">
             <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
             <h4 class="modal-title" id="myModalLabel"></h4>
           </div>
           <div class="modal-body" style="width:600px;overflow-x:auto;">
					<div class="error">
						<div class="error-title">
							<lb:get key="lab_error"/>
						</div>
						<wzb:hr />
						<div class="error-text">
							<lb:get key="lab_error"/>
						</div>
						<div class="error-text serverError" style="height: 380px;">
							<pre>Service Unavailable</pre>
						</div>
					</div>
           </div>
           <div class="modal-footer">
             <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
           </div>      
         </div>
       </div>
 </div>
</script>	
<script id="errorTemplate" type="text/x-jquery-tmpl">
<div class="alert-parent" style="opacity: 1;padding: 10px; background-color: #ebeff2; position: fixed; z-index:10000; top:0; width:100%">
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
                $('.xyd-wrapper').before($('#errorTemplate').render({
                    msg : fetchLabel('session_timeout_message') 
                }));
                $('.alert').on('closed.bs.alert', function () {
                    $(".alert-parent").remove();
                    window.location.href = contextPath + "/app/user/userLogin/$";
                })
            } else {
                $('.xyd-wrapper').before($('#errorTemplate').render({
                    msg : fetchLabel('error_server_error')
                }));
                $('.alert').on('closed.bs.alert', function () {
                    $(".alert-parent").remove();
                })
            }
            $(".alert").fadeTo(1000,1);

        } 
    });
});
</script>
 