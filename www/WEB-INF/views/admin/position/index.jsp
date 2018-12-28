<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${ctx}/js/tree/css/ztree.css" />
<link rel="stylesheet"
	href="${ctx}/static/js/jquery-table/css/jquery-table-admin.css" />
<script>
var tab = '<c:if test="${active == 'c'}">2</c:if>';
</script>
</head>
<body>
<input type="hidden" name="belong_module" value="FTN_AMD_USR_INFO_MGT"/>

 	 <title:get function="global.FTN_AMD_USR_INFO_MGT"/>

	<ol class="breadcrumb wzb-breadcrumb">
		<li><a href="javascript:wb_utils_gen_home(true);"><i
				class="fa wzb-breadcrumb-home fa-home"></i>
			<lb:get key="label_lm.label_core_learning_map_1" /></a></li>
		<li class="active"><lb:get key="label_lm.label_core_learning_map_85" /><!-- 岗位管理--></li>
	</ol>
	<!-- wzb-breadcrumb End-->
	<div class="panel wzb-panel">
		<div class="panel-heading">
			<lb:get key="label_lm.label_core_learning_map_85" /><!-- 岗位管理 -->
		</div>

		<div class="panel-body">
         
	<form onsubmit="return false;">
            <table>
                <tbody>
                    <tr>
                        <td width="30%"><div style="display:none;"></div>
                        </td>
                        <td align="right" width="60%">
                            <div class="wzb-form-search">
                            	<%-- <span id="grade_label"><lb:get key="label_lm.label_core_learning_map_16" />：</span>
                                <select class="wzb-select" name="grade-select" style="margin-right:80px;" id="grade-select">
                                    <option value="0" selected="selected"><lb:get key="global.please_select"/></option>
                                <c:forEach items="${catalogs }" var="catalog">
                                    <option value="${catalog.upc_id }">${catalog.upc_title }</option>
                                </c:forEach>
                                	<option value="-1"><lb:get key="label_lm.label_core_learning_map_84"/></option>
                                </select> --%>
                                <input name="searchText" id="searchText" placeholder="<lb:get key='label_lm.label_core_learning_map_138' />" class="form-control" type="text" style="margin-right:-4px;">
                                <input value="" id="searchBtn" class="form-submit" type="button">
                            </div>
                        </td>
                    </tr>
                </tbody>
            </table>
        </form>
        <form name="frmXml" style="margin-top:-30px;">
        
        
        <ul role="tablist" class="nav nav-tabs page-tabs">
                <li role="presentation" class="active">
                    <a aria-controls="grade" role="tab" data-toggle="tab" href="#grade"><lb:get key="label_lm.label_core_learning_map_85" /></a>
                </li>
                <%-- <li  role="presentation">
                    <a href="#gradeType" aria-controls="gradeType" role="tab" data-toggle="tab"><lb:get key="label_lm.label_core_learning_map_16" /></a>
                </li> --%>
            </ul>
            </form>
          <table>
          <tr>
                            <td align="right" colSpan="5" style="border:0;padding-top:10px;">
                             <input
					type="button" class="btn wzb-btn-yellow"
					 value="<lb:get key="label_lm.label_core_learning_map_3"/>" id="createBtn"><!-- 添加 -->
                                <a class="btn wzb-btn-orange" href="javascript:void(0);" onclick="batchdel();"><lb:get key='global.button_del'/></a>
                            </td>
                        </tr>
            </table>
	<span id="itemList">
			</span>
			


			<!-- Tab panes -->
			<div class="tab-content">
				<div role="tabpanel" class="tab-pane active" id="grade">
					<div class="wzb-model-1">
						<span id="gradeList" class="datatable wzb-table-input"> </span>
						
					</div>
				</div>

				<div role="tabpanel" class="tab-pane" id="gradeType">
					<div class="wzb-model-1">
						<span id="gradeTypeList" class="datatable wzb-table-input"> </span>
				
					</div>
				</div>
				
				</div>

	
		</div>
			<div id="myOnPageContent" style="display: none;">
				<div class="wzb-model-3">
					<p class="margin-top15 margin-bottom25">
						<lb:get key="label_lm.label_core_learning_map_88"/><br><lb:get key="global.warning_delete_notice"/><!-- 确认删除？ -->
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

<script type="text/javascript"
		src="${ctx}/static/js/i18n/${lang}/label_lm_${lang}.js"></script>
<script type="text/javascript"
		src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
			<script type="text/javascript" src="${ctx}/static/js/layer/layer.js"></script>
	<script type="text/javascript" src="${ctx}/js/wb_item.js"></script>
	<script type="text/javascript" src="${ctx}/js/wb_application.js"></script>
	<script type="text/javascript" src="${ctx}/static/admin/js/position/index.js"></script>
	<script id="text-template" type="text/x-jsrender">
		{{>text}}  
	</script>
	<script id="input-template" type="text/x-jsrender">
  <input type="checkbox" name="cms_checkbox" value="{{>text}}" />
</script>
	<script id="operateBtnTemplate" type="text/x-jsrender">
			<input onclick="updateBtn(this,{{>id}})" type="button" name="frmSubmitBtn" value="<lb:get key="global.button_update"/>" class="btn wzb-btn-blue"><!--修改-->
	</script>
</body>
</html>