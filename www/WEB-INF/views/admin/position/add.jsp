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
					<lb:get key="label_lm.label_core_learning_map_91"/> <!-- 修改岗位 -->
				</c:when>
				<c:otherwise>
					<lb:get key="label_lm.label_core_learning_map_89"/><!-- 创建岗位 -->
				</c:otherwise>
			</c:choose>
		
		
		</li>
	</ol>
	<!-- wzb-breadcrumb End-->
	<div class="panel wzb-panel">
		<c:choose>
				<c:when test="${type =='update'}">
		<form id="adminForm" method="post" action="${ctx}/app/admin/position/update">
				</c:when>
				<c:otherwise>
		<form id="adminForm" method="post" action="${ctx}/app/admin/position/add">
				</c:otherwise>
			</c:choose>
		<div class="panel-heading">
		
			<c:choose>
				<c:when test="${type =='update'}">
		<input type="hidden" value="${position.upt_id }" name="upt_id" id="old_upt_id" />
				<lb:get key="label_lm.label_core_learning_map_91"/> <!-- 修改岗位 -->
				</c:when>
				<c:otherwise>
					<lb:get key="label_pm.label_core_learning_map_89"/><!-- 创建岗位 -->
				</c:otherwise>
			</c:choose>
				
			 </div>
	 

	<div class="panel-body">
            <table>
                <tbody>
                    <tr>
                        <td class="wzb-form-label">
                            <span class="wzb-form-star">*</span><lb:get key="label_lm.label_core_learning_map_15"/>：
                        </td>
                        <td class="wzb-form-control">
                            <input class="wzb-inputText" value="${position.upt_code }" id="upt_code" onblur="this.value=this.value.trim();"  name="upt_code" type="text" style="width:300px">
                            <input  value="${position.upt_code }" id="old_code"  type="hidden">                  
                            <label for="upt_code" class="error" id="error_code" style="display:none"></label>
                        </td>
                    </tr>
                    <tr>
                        <td class="wzb-form-label">
                            <span class="wzb-form-star">*</span><lb:get key="label_lm.label_core_learning_map_93"/>：
                        </td>
                        <td class="wzb-form-control">
                            <input value="${position.upt_title }" id="upt_title" name="upt_title" type="text"  onblur="this.value=this.value.trim();"  class="wzb-inputText" style="width:300px">
                            <input  value="${position.upt_title }" id="old_title"  type="hidden">
                        <label for="upt_title" class="error" id="error_title" style="display: display:none"></label>
                        </td>
                    </tr>
                    <%-- <tr>
                        <td class="wzb-form-label">
                           <lb:get key="label_lm.label_core_learning_map_16"/>：
                        </td>
                        <td class="wzb-form-control">
                               <select class="wzb-select" name="catalogId" style="margin-right:80px;" id="catalogId">
                                    <option value="0"><lb:get key="global.please_select"/></option>
                                <c:forEach items="${catalogs }" var="catalog">
                                <c:choose>
                                <c:when test="${catalog.upc_id==position.upt_upc_id }">
                                 <option selected="selected" value="${catalog.upc_id }">${catalog.upc_title }</option>
                                 </c:when>
                                <c:otherwise>
                                    <option value="${catalog.upc_id }">${catalog.upc_title }</option>
                                
                                </c:otherwise>
                                </c:choose>
                                </c:forEach>
                                </select>
                                 <input id="upc_id" name="upt_upc_id"  type="hidden">
                        </td>
                    </tr> --%>
                   <%--  屏蔽岗位管理的description
                   <tr>
                        <td valign="top" class="wzb-form-label">
                           <span class="wzb-form-star">*</span><lb:get key="label_lm.label_core_learning_map_57"/>：
                        </td>
                        <td class="wzb-form-control">
                            <textarea value="" id="upt_desc" name="upt_desc" cols="30" style="width:300px;" wrap="VIRTUAL" rows="6"  >${position.upt_desc }</textarea>
                        <label for="upt_desc" class="error" id="error_desc" style="display:none"></label></td>
                    </tr> 
                    --%>
                    <tr>
                        <td align="right" ></td>
                        <td style="padding: 10px 0 10px 10px;font-size:12px;" align="left">
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
<script type="text/javascript" src="${ctx}/static/admin/js/position/add.js"></script>
</body>
</html>