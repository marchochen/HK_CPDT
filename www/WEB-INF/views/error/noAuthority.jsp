<%@ page isELIgnored="false" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.io.*"%>
<%@ include file="../common/taglibs.jsp"%>
<%@ include file="../common/meta.jsp"%>

<!DOCTYPE html>
<html>
<head>
<script type="text/javascript">
	function go() {
		history.go(-1);
	}
</script>
</head>
<body>

<div class="">
<div class="clearfix" style="padding:200px 0 400px;">
<div class="mod">
     <div class="losepage authority">
          <div class="losepage_tit"><lb:get key="error_no_authority"/></div>
          <div class="losepage_info"><lb:get key="error_no_authority_desc"/> </div>
          <div class="losepage_desc"><a href="javascript:go()" title="<lb:get key='button_back'/>"><lb:get key="button_back"/> </a></div>
     </div>
</div> <!-- mod End-->
</div>

</div> <!-- xyd-wrapper End-->

</body>
</html>