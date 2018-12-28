<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<script src="${ctx}/static/js/layer/layer.js"></script>
<link rel="stylesheet" href="${ctx}/js/tree/css/ztree.css" />
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-admin.css" />
<link rel="stylesheet" href="${ctx}/static/js/layer/skin/layer.css" />

</head>
<body>
<input type="hidden" name="belong_module" value="FTN_AMD_DEMAND_MGT"/>

 	 <title:get function="global.FTN_AMD_DEMAND_MGT"/>

	<ol class="breadcrumb wzb-breadcrumb">
		<li><a href="javascript:wb_utils_gen_home(true);"><i
				class="fa wzb-breadcrumb-home fa-home"></i>
			<lb:get key="label_rm.label_core_requirements_management_1" /></a></li>
		<li class="active"><lb:get key="label_rm.label_core_requirements_management_2" /><!-- 投票--></li>
	</ol>
	<!-- wzb-breadcrumb End-->
	<div class="panel wzb-panel">
		<div class="panel-heading">
			<lb:get key="label_rm.label_core_requirements_management_3" /><!-- 投票管理 -->
		</div>

		<div class="panel-body">
			
			<form class="form-search">
				<div style="display: inline;float: left;">
					<lb:get key="label_core_training_management_53"/>：
					<wzb:tcr-admin id="tcr" event="function(e, treeId, treeNode){treeClick(e, treeId, treeNode)}"></wzb:tcr-admin>
				</div>
				<input name="searchText" id="searchText" type="text" class="form-control" 
				placeholder="<lb:get key="label_rm.label_core_requirements_management_4" />">
				<!-- 请输入标题搜索 -->
				<input type="button" class="form-submit" value="" id="searchBtn">
				
				<input type="button" class="btn wzb-btn-yellow"
				value="<lb:get key="label_rm.label_core_requirements_management_5"/>" id="createVotingBtn"><!-- 创建投票活动 -->
			</form>

			<span id="itemList" style="margin-top:28px;">
				
			</span>
			<link rel="stylesheet" href="css/thickbox.css">
			<script src="js/thickbox-compressed.js" type="text/javascript"></script>
			<div id="myOnPageContent" style="display: none;">
				<div class="wzb-model-3">
					<p class="margin-top15 margin-bottom25">
						<lb:get key="label_rm.label_core_requirements_management_6"/><!-- 删除投票活动将导致学员无法查看到该投票活动的所有信息 --><br><lb:get key="global.warning_delete_notice"/><!-- 确认删除？ -->
					</p>
					<div class="wzb-bar">
						<input type="button" onclick="javascript:void(0);"
							class="btn wzb-btn-blue wzb-btn-big margin-right15" value="<lb:get key="global.button_ok"/>" name="frmSubmitBtn"><!-- 确定 --> <input type="button"
							onclick="javascript:void(0);"
							class="btn wzb-btn-blue wzb-btn-big TB_closeWindowButton"
							value="<lb:get key="global.button_cancel"/>" name="frmSubmitBtn"><!-- 取消 -->
					</div>
				</div>
			</div>

		</div>
	</div>
	<!-- wzb-panel End-->
	<script type="text/javascript"
		src="${ctx}/static/js/i18n/${lang}/label_rm_${lang}.js"></script>
	<script type="text/javascript"
		src="${ctx}/js/tree/js/jquery.ztree.core.js"></script>
	<script type="text/javascript"
		src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
	<script type="text/javascript" src="${ctx}/js/wb_item.js"></script>
	<script type="text/javascript" src="${ctx}/js/wb_application.js"></script>
	<script type="text/javascript" src="${ctx}/static/admin/js/voting/voting.js"></script>
	<script id="text-template" type="text/x-jsrender">
		{{>text}}
	</script>
	<script type="text/javascript">
	
	</script>
	<script id="operateBtnTemplate" type="text/x-jsrender">
		{{if (!responseCount || responseCount <= 0) && status != 'ON'}}
			<input onclick="updateVotingBtn(this,{{>id}})" type="button" name="frmSubmitBtn" value="<lb:get key="global.button_update"/>" class="btn wzb-btn-blue"><!--修改-->
		{{/if}} 
		
		<input onclick="viewResultBtn({{>id}})" type="button" name="frmSubmitBtn" value="<lb:get key="label_rm.label_core_requirements_management_7"/>" class="btn wzb-btn-blue"><!--查看结果-->
		
		{{if status != 'ON'}}
			<a onclick="deleteVoting({{>id}})" class="btn wzb-btn-blue thickbox" href="javascript:void(0)"><lb:get key="global.button_del"/></a><!--删除-->
		{{/if}} 
		{{if status != 'OFF'}}
			<input onclick="cancelPublished({{>id}})" type="button" name="frmSubmitBtn" value="<lb:get key="global.global_unpublish"/>" class="btn wzb-btn-blue"><!--取消发布-->
		{{/if}}
		{{if status != 'ON'}}
			<input onclick="cancelPublished({{>id}})" type="button" name="frmSubmitBtn" value="<lb:get key="global.global_publish_ok"/>" class="btn wzb-btn-blue"><!--马上发布-->
		{{/if}}  
	</script>
</body>
</html>