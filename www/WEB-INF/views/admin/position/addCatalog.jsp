<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="../../common/meta.tree.jsp"%>
<%@ include file="../../common/meta.kindeditor.jsp"%>
<%@ include file="../../common/meta.datepicker.jsp"%>
</head>
<body>
<input type="hidden" name="belong_module" value="FTN_AMD_USR_INFO_MGT"/>
<input type="hidden" id="sid"/>
 	 <title:get function="global.FTN_AMD_USR_INFO_MGT"/>
	<ol class="breadcrumb wzb-breadcrumb">
		<li><a href="javascript:wb_utils_gen_home(true);"><i
				class="fa wzb-breadcrumb-home fa-home"></i> 
				<lb:get key="label_lm.label_core_learning_map_1" /></a></li>
		<li><a href="${ctx}/app/admin/position"><!-- 岗位 -->
		<lb:get key="label_lm.label_core_learning_map_85" /></a></li>
		<li class="active">
		
				<c:choose>
				<c:when test="${type =='update'}">
					<lb:get key="label_lm.label_core_learning_map_92"/> <!-- 修改岗位 -->
				</c:when>
				<c:otherwise>
					<lb:get key="label_lm.label_core_learning_map_90"/><!-- 创建岗位 -->
				</c:otherwise>
			</c:choose>
		
		
		</li>
	</ol>
	<!-- wzb-breadcrumb End-->
	<div class="panel wzb-panel">
		<c:choose>
				<c:when test="${type =='update'}">
		<form id="adminForm" method="post" action="${ctx}/app/admin/position/updateCatalog">
				</c:when>
				<c:otherwise>
		<form id="adminForm" method="post" action="${ctx}/app/admin/position/addCatalog">
				</c:otherwise>
			</c:choose>
		<div class="panel-heading">
		
		
					<c:choose>
				<c:when test="${type =='update'}">
<input type="hidden" value="${positionCatalog.upc_id }" name="upc_id" id="old_upc_id"/>
					<lb:get key="label_lm.label_core_learning_map_92"/> <!-- 修改岗位 -->
				</c:when>
				<c:otherwise>
					<lb:get key="label_lm.label_core_learning_map_90"/><!-- 创建岗位 -->
				</c:otherwise>
			</c:choose>
		
				
			 </div>
	 

	<div class="panel-body">
            <table>
                <tbody>
                    <tr>
                        <td valign="top" class="wzb-form-label">
                          <span class="wzb-form-star">*</span><lb:get key="label_lm.label_core_learning_map_132"/> :
                        </td>
                        <td class="wzb-form-control">
                             <input class="wzb-inputText" id="upc_title" name="upc_title" onblur="this.value=this.value.trim();"  
                             	value="${positionCatalog.upc_title }" type="text" style="width:300px">
                         <input  value="${positionCatalog.upc_title }" id="old_title"  type="hidden">
                        <label for="upc_code" class="error" id="error_title" style="display:none"></label>
                        </td>
                    </tr>
                    <tr>
                        <td align="right" width="35%"></td>
                        <td style="padding: 10px 0 10px 10px;font-size:12px;" align="left" width="65%">
                            <span class="wzb-form-star">*</span><lb:get key="label_lm.label_core_learning_map_53"/>
                        </td>
                    </tr>
                </tbody>
            </table>
            <div class="wzb-bar">
                 <input id="createBtn" type="submit" name="frmSubmitBtn" value="<lb:get key='global.button_ok'/>"
					class="btn wzb-btn-blue wzb-btn-big margin-right15"
					onclick="javascript:void(0);"><!-- 确定 --> <input type="button"
					name="frmSubmitBtn" value="<lb:get key='global.button_cancel'/>" class="btn wzb-btn-blue wzb-btn-big"
					onclick="javascript:window.history.go(-1)"><!-- 取消 -->
            </div>
            </div>
    </form>
</div>
 <script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_lm_${lang}.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery.selector.js"></script>
	<script type="text/javascript" src="${ctx}/static/js/layer/layer.js"></script>
	<script type="text/javascript" src="${ctx}/js/wb_item.js"></script>
	<script type="text/javascript" src="${ctx}/js/wb_application.js"></script>
	<script type="text/javascript" src="${ctx}/js/jquery.validate.js"></script>
<script type="text/javascript" src="${ctx}/static/admin/js/position/addCatalog.js"></script>
</body>
</html>