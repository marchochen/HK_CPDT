<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ include file="../common/taglibs.jsp"%>

<!DOCTYPE html>
<html>
    
<div class="wzb-model-2">
<div class="xyd-exam clearfix">
     <div class="xyd-exam-icon">
          <a class="xyd-exam-one" href="${ctx }/app/exam/courseCatalog" title='<lb:get key="exam_catalog"/>'>
             <i class="fa fa-sitemap"></i>
             <p><lb:get key="exam_catalog"/></p>
          </a>
     </div>
  
     <div class="xyd-exam-omitted"></div>
    
     <div class="xyd-exam-icon">
          <a class="xyd-exam-two" href="${ctx }/app/exam/recommend" title='<lb:get key="recommend_exam"/>'>
             <i class="fa fa-align-center"></i>
             <p><lb:get key="recommend_exam"/></p>
          </a>
     </div>
  
     <div class="xyd-exam-omitted"></div>
    
     <div class="xyd-exam-icon">
          <a class="xyd-exam-three" href="${ctx }/app/exam/signup" title='<lb:get key="exam_signup"/>'>
             <i class="fa fa-columns"></i>
             <p><lb:get key="exam_signup"/></p>
          </a>
     </div>
</div>
</div>
<script type="text/javascript">
	var command = '${command}';
	if(command == "recommend"){
		$(".xyd-exam-two").addClass("cur");
	}else if(command == "signup"){
		$(".xyd-exam-three").addClass("cur");
	}else{
		$(".xyd-exam-one").addClass("cur");
	}
</script>
</html>