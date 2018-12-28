<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<%@ include file="../../common/template.jsp"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-admin.css" />
<title></title>
</head>
<body>
<input type="hidden" name="belong_module" value="FTN_AMD_SNS_MGT"/>
	 <title:get function="global.FTN_AMD_SNS_MGT"/>
<ol class="breadcrumb wzb-breadcrumb">
  <li><a href="javascript:wb_utils_gen_home(true);"><i class="fa wzb-breadcrumb-home fa-home"></i><lb:get key="global.lab_menu_started" /></a></li>
  <li><a href="javascript:wb_utils_nav_go('${ftn_type }', '${prof.usr_ent_id }', '${label_lan }')">
  	<c:choose>
	  	<c:when test="${ftn_type == 'FTN_AMD_Q_AND_A_VIEW' }">
			<wb:has-any-permission permission="FTN_AMD_Q_AND_A_VIEW">
				<lb:get key="label_cm.label_core_community_management_211"/><!-- 问答 -->
			</wb:has-any-permission>
	  	</c:when>
	  	<c:otherwise>
			<wb:has-any-permission permission="FTN_AMD_Q_AND_A_MAIN">
				<lb:get key="label_cm.label_core_community_management_1"/><!-- 问答管理 -->
			</wb:has-any-permission>
	  	</c:otherwise>
  	</c:choose>
  </a></li>
  <li class="active">
<!--   全部问题 --><lb:get key="label_cm.label_core_community_management_2"/>
  </li>
</ol> <!-- wzb-breadcrumb End-->

<div class="panel wzb-panel">
<div class="panel-heading">
<!-- 全部问题 --><lb:get key="label_cm.label_core_community_management_2"/>
</div>

<div class="panel-body">
<wb:has-any-permission permission="FTN_AMD_Q_AND_A_MAIN">
<div class="wzb-percent wzb-percent-wenda clearfix">
    <a href="${ctx }/app/admin/know/allKnow">
	    <div class="wzb-percent-5">
	    	<div><img src="${ctx}/static/admin/images/wzb-quanbu.png" alt="wizbank" /></div>
	        <lb:get key="label_cm.label_core_community_management_2"/><!-- 全部问题 -->
	    </div>
    </a>
    
    <div class="wzb-percent-6 text-center">
         <img src="${ctx}/static/admin/images/wzb-dian.png" alt="" />
    </div>
    
    <a href="${ctx }/app/admin/know/classify">
	    <div class="wzb-percent-5">
	         <div><img src="${ctx}/static/admin/images/wzb-fenlei.png" alt="" /></div> 
			 <lb:get key="label_cm.label_core_community_management_3"/><!-- 分类管理 -->
	    </div>
    </a>
    <div class="wzb-percent-6 text-center">
         <img src="${ctx}/static/admin/images/wzb-dian.png" alt="" />
    </div>
    <a href="${ctx }/app/admin/know/add?type=FAQ">
	    <div class="wzb-percent-5">
	         <div><img src="${ctx}/static/admin/images/wzb-faq.png" alt="" /></div> 
			 <lb:get key="label_cm.label_core_community_management_4"/><!-- 添加FAQ -->
	    </div>
    </a>
</div> <!-- wzb-percent End-->
</wb:has-any-permission>
<form class="form-search" onsubmit="return false;">     
      <input type="text" class="form-control" id="search_content" /><input type="button" class="form-submit" value=""/>
      <wb:has-any-permission permission="FTN_AMD_Q_AND_A_VIEW">
      <a type="button" class="btn wzb-btn-yellow" href="${ctx }/app/admin/know/add?isView=${isView }">
<!--       我要提问 --><lb:get key="label_cm.label_core_community_management_5"/>
      </a>
      </wb:has-any-permission>
</form>

<div class="wzb-model-4">
    <dl class="wzb-list-9 wzb-border-bottom clearfix">
         <dt>
<!--          分类 -->
            <lb:get key="label_cm.label_core_community_management_6"/>：
         </dt>
         <dd  id="parent_catalog" >
            <a class="cur" title='<lb:get key="label_cm.label_core_community_management_6"/>' href="javascript:;" onclick="changeCatalog(this, 0, 'CATALOG')" value="0">
            <lb:get key="label_cm.label_core_community_management_7"/></a> 
         </dd>
    </dl>
    
    <dl class="wzb-list-9 clearfix">
         <dt>
<!--          子分类 -->
             <lb:get key="label_cm.label_core_community_management_8"/>：
         </dt>
         <dd  id="child_catalog" ></dd>
    </dl>
</div>

<div role="tabpanel" class="wzb-tab-1">
<!-- Nav tabs -->
<ul class="nav nav-tabs page-tabs" role="tablist">
    <li role="presentation" class="active"><a href="#home"   aria-controls="home" role="tab" data-toggle="tab">
<!--         待解决问题 --><lb:get key="label_cm.label_core_community_management_9"/>
    </a></li>
    <li role="presentation"><a href="#resolved"  aria-controls="resolved" role="tab" data-toggle="tab">
<!--         已解决问题 --><lb:get key="label_cm.label_core_community_management_10"/>
    </a></li>
    
    <!--         精选问题 
    <li role="presentation"><a href="#choice"  aria-controls="choice" role="tab" data-toggle="tab">
		<lb:get key="label_cm.label_core_community_management_11"/>
    </a></li>
    -->
    <li role="presentation"><a href="#faq"  aria-controls="faq" role="tab" data-toggle="tab">
<!--     FAQ --><lb:get key="label_cm.label_core_community_management_12"/>
    </a></li>
  
  <wb:has-any-permission permission="FTN_AMD_Q_AND_A_VIEW">
    <li role="presentation"><a href="#myquestion"  aria-controls="myquestion" role="tab" data-toggle="tab">
<!--     我的提问 --><lb:get key="label_cm.label_core_community_management_13"/>
    </a></li>
    <li role="presentation"><a href="#myanswer"  aria-controls="myanswer" role="tab" data-toggle="tab">
<!--     我的回答 --><lb:get key="label_cm.label_core_community_management_14"/>
    </a></li>
  </wb:has-any-permission>
    <li role="presentation"><a href="#myknowhelp"  aria-controls="myknowhelp" role="tab" data-toggle="tab">
<!--     求助于我 --><lb:get key="label_cm.label_core_community_management_192"/>
    </a></li>
</ul>

<!-- Tab panes -->
<div class="tab-content">
     <div role="tabpanel" class="tab-pane active" name="UNSOLVED" id="home">
          
     </div>
     
     <div role="tabpanel" class="tab-pane" name="SOLVED" id="resolved">
          
     </div>
     
     <div role="tabpanel" class="tab-pane" name="POPULAR" id="choice">
          
     </div>
     
     <div role="tabpanel" class="tab-pane" name="FAQ" id="faq">
          
     </div>
     
     <div role="tabpanel" class="tab-pane" name="my_question" id="myquestion">
          
     </div>
     
     <div role="tabpanel" class="tab-pane" name="my_answer" id="myanswer">
          
     </div>
     
     <div role="tabpanel" class="tab-pane" name="my_know_help" id="myknowhelp">
          
     </div>
</div>
</div>


</div>
</div>  <!-- wzb-panel End-->
<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery.prompt.js"></script>
<script type="text/javascript" src="${ctx}/static/js/front/course.js"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_cm_${lang}.js"></script>
<script type="text/javascript" src="${ctx}/static/admin/js/know.js"></script>
<script type="text/javascript">
var type = '${type}';
var command = '${command}';
var tab = "${tab}" || cwn.getUrlParam("tab");
var ftn_type = '${ftn_type}';
var isView = '${isView}';
</script>
</body>
</html>