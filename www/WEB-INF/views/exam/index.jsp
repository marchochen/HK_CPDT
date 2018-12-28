<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
</head>
<body>

<div class="xyd-wrapper">
<div id="main" class="xyd-main clearfix">
<div class="exam_top">
     <div class="exam_tit">
<!--      欢迎来到 --> <lb:get key="exam_welcome"/>
     <span class="skin-color f30 mL10">
<!--      考试 -->	<lb:get key="exam_name"/>
     </span>，
     
<!--      快行动吧！ --><lb:get key="exam_doing_quickly"/>
     </div>
     
     <div class="exam_tool clearfix">
 		<jsp:include page="examMenu.jsp"></jsp:include>
     </div>
</div>

<div class="exam_down"></div>

</div>
</div> <!-- xyd-wrapper End-->


</body>
</html> 