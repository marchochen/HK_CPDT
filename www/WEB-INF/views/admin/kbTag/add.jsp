<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.cwn.wizbank.entity.*,java.util.*,com.cw.wizbank.util.*, com.cw.wizbank.qdb.*"%>
<%@ include file="../../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../../common/meta.tree.jsp"%>
	<script type="text/javascript" src="${ctx}/js/jquery.selector.js"></script>
	<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_km_${lang}.js"></script>
</head>
<body>
	<input type="hidden" name="belong_module" value="FTN_AMD_KNOWLEDGE_MGT"/>
	<title:get function="global.FTN_AMD_KNOWLEDGE_MGT"/>

    <ol class="breadcrumb wzb-breadcrumb">
        <li><a href="javascript:wb_utils_gen_home(true);"><i class="fa wzb-breadcrumb-home fa-home"></i><lb:get key="global.lab_menu_started" /></a></li>
        <li><a href="/app/admin/kbTag/index"><lb:get key="label_km.label_core_knowledge_management_43" /></a></li>
        <li class="active">
        	<c:choose>
	        	<c:when test="${type == 'update' }">
	        		<lb:get key="label_km.label_core_knowledge_management_91" />
	        	</c:when>
	        	<c:otherwise>
	        		<lb:get key="label_km.label_core_knowledge_management_89" />
	        	</c:otherwise>
        	</c:choose>
       	</li>
    </ol>
    
    <div class="panel wzb-panel">
        <div class="panel-heading">
        	<c:choose>
	        	<c:when test="${type == 'update' }">
	        		<lb:get key="label_km.label_core_knowledge_management_91" />
	        	</c:when>
	        	<c:otherwise>
	        		<lb:get key="label_km.label_core_knowledge_management_89" />
	        	</c:otherwise>
        	</c:choose>
        </div>

        <div class="panel-body">
            <form:form modelAttribute="tag" action="${ctx}/app/admin/kbTag/save" method="post" cssClass="form-horizontal">
				<form:hidden path="tag_id" />

				<div class="form-group">
					<div class="quest">
						<form:label cssClass="col-sm-2 control-label" path="tag_title">
							<span class="wzb-form-star">*</span>
							<lb:get key="label_km.label_core_knowledge_management_73" />
							<span>：</span>
						</form:label>
						<div class="col-sm-10">
							<form:input cssStyle="width: 400px;" cssClass="form-control" path="tag_title" />
							<form:errors path="tag_title" cssClass="has-error error" />
							<!-- has-error -->
						</div>
					</div>
				</div>
				<div class="form-group">
					<form:label cssClass="col-sm-2 control-label" path="tcTrainingCenter.tcr_id">
						<span class="wzb-form-star">*</span>
						<lb:get key="label_km.label_core_knowledge_management_67" />
						<span>：</span>
					</form:label>
					<div class="col-sm-10 wzb-selector-div">
						<form:select id="tc-selector-single" cssStyle="width: 120px;" cssClass="form-control" path="tcTrainingCenter.tcr_id">
							<c:if test="${not empty tag.tcTrainingCenter.tcr_id}">
								<form:option value="${tag.tcTrainingCenter.tcr_id}" label="${tag.tcTrainingCenter.tcr_title}" />
							</c:if>
						</form:select>
						<form:errors path="tcTrainingCenter.tcr_id" cssClass="error" style="margin-left: 3px;" />
					</div>
				</div>
				<div class="form-group">
					<div class="col-sm-2"></div>
					<div class="col-sm-10"><span class="wzb-form-star">*</span>
					<!-- 为必填 -->
					<lb:get key="label_rm.label_core_requirements_management_35" /></div>
				</div>
				<hr/>
				<div>
					<div class="form-group">
						<div class="col-sm-offset-2 col-sm-10">
							<button type="submit" class="btn wzb-btn-blue  wzb-btn-big margin-right10">
								<lb:get key="global.button_ok" />
							</button>
							<button type="button" onclick="javascript:go('${ctx}/app/admin/kbTag/index')" class="btn wzb-btn-blue wzb-btn-big">
								<lb:get key="global.button_cancel" />
							</button>
						</div>
					</div>
				</div>
			</form:form>
        </div>
    </div>
    <!-- wzb-panel End-->
    <script type="text/javascript">
    $(function() {
        // 单选的树模式
        $('#tc-selector-single').wzbSelector({
            type : 'single',
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