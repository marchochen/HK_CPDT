<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../views/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="../views/common/meta.jsp"%>
<title><decorator:title default="Wizbank" /></title>
<decorator:head />
</head>
<body>
<c:choose>
	<c:when  test="${hide_header_ind == true}">
	  
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
					
	</c:when>
	<c:otherwise>
			<%@ include file="../views/common/header.jsp"%>
	</c:otherwise>
</c:choose>

	<decorator:body />
	<c:if test="${show_all_footer_ind == true}">
		<%@ include file="../views/common/footer.jsp"%>
	</c:if>
</body>
</html>