<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
    <meta charset="UTF-8">
    <title></title>
</head>
<body>
	
	<input type="hidden" name="belong_module" value="FTN_AMD_LIVE_MAIN" />

	<title:get function="global.FTN_AMD_LIVE_MAIN" />

	<ol class="breadcrumb wzb-breadcrumb">
		<li><a href="javascript:wb_utils_gen_home(true);"><i
				class="fa wzb-breadcrumb-home fa-home"></i>
			<lb:get key="global.lab_menu_started" /></a></li>
		<li><a href="${ctx }/app/admin/live/list?mode_type=${lv.lv_mode_type }"><lb:get key="global.FTN_AMD_LIVE_MAIN"/> </a></li>
		<li class="active">
			${lv.lv_title}
		</li>
	</ol>
	<!-- wzb-breadcrumb End-->
	
	<div class="panel wzb-panel">

		<div class="panel-body" style="padding:15px;">
	    	<div class="" style="width:100%;height: 100%;">
	    		<c:choose>
	    			<c:when test="${lv.lv_mode_type == 'GENSEE'}">
	    				<iframe id="liveIframe" name="liveIframe" frameborder="no" border="0" src="" width="100%" style="height: 100%;border: 0;"></iframe>
		    			<script>
	    					$('#liveIframe').attr('src', '${lv.lv_gensee_record_url }?nickname=' + encodeURIComponent('${prof.usr_display_bil }'));
	    				</script>
	    			</c:when>
	    			<c:otherwise>
	    				<iframe name="liveIframe" frameborder="no" border="0" src="${lv.lv_url }" width="100%" style="height: 100%;border: 0;"></iframe>
					</c:otherwise>
	    		</c:choose>
		    </div>
		</div>
	</div>
    
</body>
</html>