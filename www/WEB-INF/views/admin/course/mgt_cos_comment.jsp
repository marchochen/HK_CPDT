<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<%@ include file="../common/meta.jsp"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" type="text/css" href="${ctx}/static/css/base.css"/>
<link rel="stylesheet" type="text/css" href="${ctx}/static/admin/css/admin.css"/>
<c:if test="${lang eq 'en-us'}">
	<!-- 兼容英文的css -->
	<link rel="stylesheet" href="${ctx}/static/css/base-en.css"/>
	<link rel="stylesheet" href="${ctx}/static/admin/css/admin-en.css"/>
</c:if>
<link rel="stylesheet" href="${ctx}/js/tree/css/ztree.css" />
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-admin.css" />
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-learner.css" />
<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<script type="text/javascript" src="${ctx}/static/js/front/sns.js"></script>
<script type="text/javascript" src="${ctx}/static/js/ckplayer/ckplayer.js" charset="utf-8"></script>
<script type="text/javascript" src="${ctx}/static/js/ckplayer/js/offlights.js" charset="utf-8"></script>
<script type="text/javascript" src="${ctx}/js/jquery.prompt.js"></script>
<script type="text/javascript" src="${ctx}/static/js/front/jquery.sns.js"></script>
<script type="text/javascript" src="${ctx}/static/js/image_doing.js"></script>
<script  type="text/javascript" src="${ctx}/js/wb_item.js"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_cm_${lang}.js"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_tm_${lang}.js"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_cpd_${lang}.js"></script>


<script>
itm_lst = new wbItem;
wbEncrytor = new wbEncrytor;
var opens;
var tndId;
var itmId =wbEncrytor.cwnDecrypt(window.location.href.replace(/.*\//,""));
var tkhId = -1;
var sns = new Sns();
var targetId = itmId;
var module = 'Course';
itm_lst = new wbItem;
window.onload = function(){
	var exam_ind = ${item.itm_exam_ind};
	if(exam_ind == 0){
		highLightMainMenu("FTN_AMD_TRAINING_MGT");//高亮菜单
	}else{
		highLightMainMenu("FTN_AMD_EXAM_MGT");
	}
	// 加载评论列表
	loadComment(module);
	loadNavigationTemplate();
}


</script>

</head>
<body>

<input type="hidden" id="cur_node_id" value="">
	<c:choose>
    	<c:when test="${item.itm_exam_ind == '1'}">
    		<title:get function="global.FTN_AMD_EXAM_MGT"/> 
    	</c:when>
    	<c:otherwise>
    		<title:get function="global.FTN_AMD_TRAINING_MGT"/>
    	</c:otherwise>
    </c:choose>
     
      <ol class="breadcrumb wzb-breadcrumb textleft  heder-nav">
            <li>
            	<a href="javascript:wb_utils_gen_home(true);">
            		<i class="fa wzb-breadcrumb-home fa-home"></i>
            		<lb:get key="label_lm.label_core_learning_map_1" />
            	</a>
            </li>
            <li>
            	<c:choose>
            		<c:when test="${item.itm_type == 'AUDIOVIDEO'}">
	            		<a href="javascript:wb_utils_nav_go('FTN_AMD_OPEN_COS_MAIN',${prof.usr_ent_id}, '${label_lan}')">
			                <lb:get key="global.FTN_AMD_OPEN_COS_MAIN" />
			            </a>
            		</c:when>
            		<c:when test="${item.itm_exam_ind == '1'}">
            			 <a href="javascript:wb_utils_nav_go('FTN_AMD_EXAM_MGT',${prof.usr_ent_id},'${label_lan}');">
			                 <lb:get key="global.FTN_AMD_EXAM_MGT" />
			             </a>
            		</c:when>
            		<c:when test="${item.itm_exam_ind == '0' }">
            			<a href="javascript:wb_utils_nav_go('FTN_AMD_ITM_COS_MAIN',${prof.usr_ent_id},'${label_lan}');">
			               <lb:get key="global.FTN_AMD_ITM_COS_MAIN" />
			            </a>
            		</c:when>
            	</c:choose>
            </li>
            <c:if test="${item.itm_run_ind == '1'}">
               <li><a href="javascript:itm_lst.get_item_detail(${item.parent.itm_id});">
	                   ${item.parent.itm_title}
	                </a>
	           </li>
               <li>
                  <c:if test="${item.itm_exam_ind == '1'}">
                    <a href="javascript:itm_lst.get_item_run_list(${item.parent.itm_id});">
	                   <lb:get key="label_core_training_management_468_ex" />
	                </a>
			      </c:if>
			      <c:if test="${item.itm_exam_ind == '0'}">
			          <a href="javascript:itm_lst.get_item_run_list(${item.parent.itm_id});">
		               <lb:get key="label_core_training_management_246" />
		              </a>
			      </c:if>
               </li>
            </c:if>
            <li><a href="javascript:itm_lst.get_item_detail(${item.itm_id})">${item.itm_title}</a></li>
            <li class="active"><lb:get key="label_tm.label_core_training_management_244" /></li>
        </ol>
        
     <c:if test="run_ind != false">   
     </c:if> 
     
     
        
 <form id="formCondition" action="${ctx}/app/admin/learnerReport/getLearnerReportByUser"  method="post" onsubmit="preSubmit()">
        <div class="panel wzb-panel">
            <div class="panel-body">
				
			  <jsp:include page="../../admin/common/itm_gen_action_nav_share_new.jsp">
				<jsp:param value="118" name="cur_node_id"/>
			   </jsp:include>
				
				<div class="wbtab mt20 wbtog">
				    <div class="wbbox">        
				        <div class="panel-content" id="comment_lst"  >
				             <form method="post" action="#" >
				                   <textarea class="wzb-textarea-01 align-bottom margin-right10" id="courseComment"></textarea>
				                   <input type="button" class="btn wzb-btn-yellow wzb-btn-big align-bottom" id="courseCommentSubmit" value='<lb:get key="btn_submit"/>'/>
				             </form>
				             <div class="wzb-title-2 margin-top15" id="commentCount">
				               	<span></span>
				               	</div>
							<div id="comment_lst_content" class="wzb-trend clearfix"></div>
				            <jsp:include page="../../common/comment.jsp"/>
							<!--评论在此处 -->
				        </div>
				    </div>
				</div>

            </div>  <!-- panel-body End-->
        </div>  <!-- panel End-->
   
   </form>
  
  
</body>
</html>