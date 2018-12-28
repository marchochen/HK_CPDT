<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${ctx}/js/tree/css/ztree.css" />
<link rel="stylesheet"
	href="${ctx}/static/js/jquery-table/css/jquery-table-admin.css" />
<script type="text/javascript" src="${ctx}/static/js/layer/layer.js"></script> 

</head>
<body>
<input type="hidden" name="belong_module" value="FTN_AMD_TRAINING_MGT"/>

 	 <title:get function="global.FTN_AMD_TRAINING_MGT"/>

	<ol class="breadcrumb wzb-breadcrumb">
		<li><a href="javascript:wb_utils_gen_home(true);"><i
				class="fa wzb-breadcrumb-home fa-home"></i>
			<lb:get key="label_lm.label_core_learning_map_1" /></a></li>
		<li class="active"><lb:get key="label_lm.label_core_learning_map_31" /><!-- 专题培训管理--></li>
	</ol>
	<!-- wzb-breadcrumb End-->
	<div class="panel wzb-panel">
		<div class="panel-heading">
			<lb:get key="label_lm.label_core_learning_map_31" /><!--专题培训管理 -->
		</div>

		<div class="panel-body">
		<!-- 内容添加开始 -->
        <div class="wzb-ui-desc-text">   
         <span style="color:#64BF0F;">
          <lb:get key="label_lm.label_core_learning_map_114" /> <!-- 提示 -->
         </span>
          <lb:get key="label_lm.label_core_learning_map_73" />
        </div>
                        <form class="form-search" onsubmit="return false;">
                            <input name="searchText" type="text" class="form-control" placeholder="<lb:get key='label_lm.label_core_learning_map_58' />">
                            <input type="button" class="form-submit" value="" id="searchBtn"> 
                            <input
					type="button" class="btn wzb-btn-yellow"
					 value="<lb:get key="global.global_publish"/>" onclick="batchPublishTopic();"><!-- 发布 -->
		 <input
					type="button" class="btn wzb-btn-yellow"
					 value="<lb:get key="global.global_unpublish"/>" onclick="batchCancelPublishTopic();"><!-- 取消发布 -->
		 <input
					type="button" class="btn wzb-btn-yellow"
					 value="<lb:get key="global.button_del"/>" onclick="batchdelTopic();"><!-- 删除-->
                            <input
					type="button" class="btn wzb-btn-yellow"
					 value="<lb:get key="label_core_learning_map_30"/>" id="addSte"><!-- 添加 -->
                        </form>
			<span id="itemList"  class="datatable wzb-table-input">
				
			</span>
			<div id="myOnPageContent" style="display: none;">
				<div class="wzb-model-3">
					<p class="margin-top15 margin-bottom25">
						<lb:get key="label_lm.label_core_learning_map_7"/><!-- 删除 --><br><lb:get key="global.warning_delete_notice"/><!-- 确认删除？ -->
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
		src="${ctx}/static/js/i18n/${lang}/label_lm_${lang}.js"></script>
	<script type="text/javascript"
		src="${ctx}/static/js/i18n/${lang}/label_${lang}.js"></script>
	<script type="text/javascript"
		src="${ctx}/js/tree/js/jquery.ztree.core.js"></script>
	<script type="text/javascript"
		src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
	<script type="text/javascript" src="${ctx}/js/wb_item.js"></script>
	<script type="text/javascript" src="${ctx}/js/wb_application.js"></script>
	<script type="text/javascript" src="${ctx}/static/admin/js/specialTopic/index.js"></script>
	<script id="text-template" type="text/x-jsrender">
		{{>text}}
	</script>
	<script id="input-template" type="text/x-jsrender">
  <input type="checkbox"  name="cms_checkbox" value="{{>text}}" />
</script>
	<script id="operateBtnTemplate" type="text/x-jsrender">
			<input style="margin-bottom:2px" onclick="updateSteBtn(this,{{>id}})" type="button" name="frmSubmitBtn" value="<lb:get key="global.button_update"/>" class="btn wzb-btn-blue"><!--修改-->
			<input style="margin-bottom:2px" onclick="viewResultBtn({{>id}})" type="button" name="frmSubmitBtn" value="<lb:get key="label_lm.label_core_learning_map_81"/>" class="btn wzb-btn-blue"><!--查看结果-->
			<a  style="margin-bottom:2px" onclick="deleteSte({{>id}})" class="btn wzb-btn-blue thickbox" href="javascript:void(0)"><lb:get key="global.button_del"/></a><!--删除-->
		{{if status !=1}}
			<input style="margin-bottom:2px" onclick="published({{>id}})" type="button" name="frmSubmitBtn" value="<lb:get key="global.global_publish"/>" class="btn wzb-btn-blue"><!--马上发布-->
		{{else}}
			<input style="margin-bottom:2px" onclick="cancelPublished({{>id}})" type="button" name="frmSubmitBtn" value="<lb:get key="global.global_unpublish"/>" class="btn wzb-btn-blue"><!--取消发布-->
     {{/if}}
	</script>
</body>
</html>