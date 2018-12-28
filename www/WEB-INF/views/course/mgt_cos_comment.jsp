<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<%@ include file="../admin/common/meta.jsp"%>
<!DOCTYPE html>
<html>
<head>
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
<title></title>
<script type="text/javascript">
 wbEncrytor = new wbEncrytor;
	var opens;
	var tndId;
	var itmId =wbEncrytor.cwnDecrypt(window.location.href.replace(/.*\//,""));
	var tkhId = -1;
	var sns = new Sns();
	var targetId = itmId;
	var module = 'Course';
	itm_lst = new wbItem;
	$(function(){
		// 加载评论列表
		loadComment(module);
	})
</script>
</head>
<body>

 <ol class="breadcrumb wzb-breadcrumb textleft">
<li>
${item.itm_title} > <lb:get key="label_cm.label_core_community_management_174"/> 
</ol>
<div class="panel wzb-panel">
	<div class="panel-body">

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
		            <jsp:include page="../common/comment.jsp"/>
		<!--              评论在此处 -->
		        </div>
		    </div>
		</div>

	</div>
</div>
 <!-- xyd-wrapper End-->
</body>
</html>