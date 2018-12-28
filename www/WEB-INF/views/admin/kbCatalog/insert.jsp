<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ include file="../../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
	<head>
		<%@ include file="../../common/meta.tree.jsp"%>
		<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_km_${lang}.js"></script>
		
		<script type="text/javascript" src="${ctx}/js/jquery.selector.js"></script>
		<script type="text/javascript" src="${ctx}/static/js/cwn_utils.js"></script>
	</head>
<body>
	<input type="hidden" name="belong_module" value="FTN_AMD_KNOWLEDGE_MGT"/>

	<title:get function="global.FTN_AMD_KNOWLEDGE_MGT"/>
	<ol class="breadcrumb wzb-breadcrumb">
		<li><a href="javascript:wb_utils_gen_home(true);"><i class="fa wzb-breadcrumb-home fa-home"></i><lb:get key="label_km.label_core_knowledge_management_1"/></a></li>
		<li><a href="${ctx }/app/admin/kbCatalog/index"><lb:get key="label_km.label_core_knowledge_management_3"/>${ctx} </a></li>
		<li class="active">
			<c:choose>
				<c:when test="${type=='update'}">
					<lb:get key="label_km.label_core_knowledge_management_71" />
				</c:when>
				<c:otherwise>
					<lb:get key="label_km.label_core_knowledge_management_70" />
				</c:otherwise>
			</c:choose>
		</li>
	</ol> <!-- wzb-breadcrumb End-->

	<div class="panel wzb-panel">
		<div class="panel-heading">
			<c:choose>
				<c:when test="${type=='update'}">
					<lb:get key="label_km.label_core_knowledge_management_71" />
				</c:when>
				<c:otherwise>
					<lb:get key="label_km.label_core_knowledge_management_70" />
				</c:otherwise>
			</c:choose>
		<!-- 添加目录 --> 
		</div>

        <div class="panel-body">
            <form:form modelAttribute="kbCatalog" cssClass="form-horizontal" enctype="multipart/form-data">
                <form:input type="hidden" cssStyle="width: 300px;" cssClass="form-control" path="kbc_id" />
                <div class="form-group">
                    <form:label cssClass="col-sm-2 control-label" path="kbc_title">
                        <span class="wzb-form-star">*</span>
                        <lb:get key="label_km.label_core_knowledge_management_17" />：
                    </form:label>
                    <div class="col-sm-10">
                        <form:input cssStyle="width: 400px;" cssClass="form-control" path="kbc_title" />
                        <form:errors path="kbc_title" cssClass="error" />
                    </div>
                </div>
                <div class="form-group">
                    <form:label cssClass="col-sm-2 control-label" path="tcTrainingCenter.tcr_id">
                        <span class="wzb-form-star">*</span>
                        <lb:get key="label_km.label_core_knowledge_management_67" />：
                    </form:label>
                    <div class="col-sm-10 wzb-selector-div">
                        <form:select id="tc-selector-single" cssStyle="width: 120px;" cssClass="form-control" path="tcTrainingCenter.tcr_id">
                        	<c:choose>
	                            <c:when test="${not empty kbCatalog.tcTrainingCenter.tcr_id}">
	                                <form:option value="${kbCatalog.tcTrainingCenter.tcr_id}" label="${kbCatalog.tcTrainingCenter.tcr_title}" />
	                            </c:when>
	                            <c:otherwise>
	                            	<c:if test="${tcr_id > 0 && not empty tcr_title }">
	                                	<form:option value="${tcr_id}" label="${tcr_title}" />
	                                </c:if>
	                            </c:otherwise>
                        	</c:choose>
                        </form:select>
                        <form:errors path="tcTrainingCenter.tcr_id" cssClass="error" cssStyle="margin-left: 3px;"/>
                    </div>
                </div>
                <div class="form-group">
                    <form:label cssClass="col-sm-2 control-label" path="kbc_desc">
                        <lb:get key="label_km.label_core_knowledge_management_34" />：
                    </form:label>
                    <div class="col-sm-10">
                        <form:textarea cssStyle="width: 400px;" cssClass="form-control" path="kbc_desc"></form:textarea>
                        <form:errors path="kbc_desc" cssClass="error" />
                    </div>
                </div>
                <div class="form-group">
					<div class="col-sm-2"></div>
					<div class="col-sm-10"><span class="wzb-form-star">*</span>
					<!-- 为必填 -->
					<lb:get key="label_rm.label_core_requirements_management_35" /></div>
				</div>
                <div class="wzb-bar">
                    <button type="submit" class="btn wzb-btn-blue wzb-btn-big margin-right10">
                         <lb:get key="global.button_ok" />
                     </button>
                     <button type="button" class="btn wzb-btn-blue wzb-btn-big" onclick="javascript:go('${ctx}/app/admin/kbCatalog/index');">
                         <lb:get key="global.button_cancel" />
                     </button>
                </div>
            </form:form>
            <div class="clear"></div>
        </div>
      	<div class="clear"></div>
	</div>
	
	<script type="text/javascript">
	    $(function() {
	        // 单选的树模式
	        $('#tc-selector-single').wzbSelector({
	            type : 'single',
	            ignoreRootNode : true,
	            tree : {
	                enable : true,
	                type : 'single',
	                setting : {
	                    async : {
	                        enable : true,
	                        autoParam : [ "id" ],
	                        url : '${ctx}/app/tree/tcListJson/withHead'
	                    },data : {
	                        simpleData : {
	                            enable : true
	                        }
	                    }
	                }
	            },
	            message : {
	                title : fetchLabel('label_core_knowledge_management_81')
	            }
	        });
	    });
	</script>
</body>
</html>