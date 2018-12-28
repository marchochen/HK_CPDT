<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<link rel="stylesheet" href="${ctx}/static/js/bootstrap/css/bootstrap.css"/>
	<link rel="stylesheet" href="${ctx}/static/css/base.css">
    <script type="text/javascript" src="${ctx}/static/js/jquery.js"></script>
	<script type="text/javascript" src="${ctx}/static/js/bootstrap/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="${ctx}/static/js/cwn_utils.js"></script>
	<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/global_${lang}.js"></script>
	<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_lvm_${lang}.js"></script>
    <script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
    <script type="text/javascript" src="${ctx}/static/js/jquery.dialogue.js"></script>
	<script type="text/javascript" src="${ctx}/static/js/jquery.qtip/jquery.qtip.js"></script>
	<link rel="stylesheet" href="${ctx}/static/js/jquery.qtip/jquery.qtip.css" />
    <meta charset="UTF-8">
    <title>${lv.lv_title}</title>
    <script>
		$(function(){
			if('${lv.lv_type}' != '3'){
				$(window).bind('beforeunload',function(){
					$.ajax({ 
						url: "${ctx }/app/admin/live/endlive/${lv.lv_enc_id}", 
						data:{  
							
						}, 
						dataType:'json',
						type:'post',
						success: function(data){
							
		      			}
					});
					return fetchLabel("label_core_live_management_24");
				});
			}
		});
    </script>
</head>
<body>
    <div class="" style="position:fixed;top:0;left:0;width:100%;height: 100%;">
	    <div class="clearfix" style="min-width: 984px;width:100%;height:100%;background: #ffffff;">
	    	<div class="" style="width:100%;height: 100%;">
	    		<c:choose>
	    			<c:when test="${!empty lv && lv.lv_mode_type eq 'GENSEE' }">
	    				<iframe id="liveIframe" name="liveIframe" frameborder="no" border="0" src="" width="100%" style="position: absolute;height: 100%;border: 0;"></iframe>
	    				<script>
	    					$('#liveIframe').attr('src', '${lv.lv_teacher_join_url }?nickname=' + encodeURIComponent('${prof.usr_display_bil }') + '&token=${lv.lv_teacher_token}');
	    				</script>
	    			</c:when>
	    			<c:otherwise>
		   				<iframe name="liveIframe" frameborder="no" border="0" src="${lv.lv_url }" width="100%" style="position: absolute;height: 100%;border: 0;"></iframe>
	    			</c:otherwise>
	    		</c:choose>
		    </div>
		</div><!-- wbtab end -->		    
    </div>
    
</body>
</html>