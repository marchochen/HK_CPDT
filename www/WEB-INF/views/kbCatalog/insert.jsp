<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="../common/meta.tree.jsp"%>
<script type="text/javascript" src="${ctx}/js/jquery.selector.js"></script>
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
						url : '${ctx}/app/tree/tcListJson/noHead'
					}
				}
			},
			message : {
				title : fetchLabel('lab_tcr_select')
			}
		});
	});
</script>
</head>
<body>
	<div class="container bs-docs-container">
		<div class="panel panel-success">
			<ul class="nav nav-tabs" id="menuTabs">
				<li role="presentation"><a href="${ctx}/app/kb/admin/index">
						<lb:get key="lab_kb_menu_knowledge" />
					</a></li>
				<li role="presentation" class="active"><a href="${ctx}/app/kb/catalog/admin/index">
						<lb:get key="lab_kb_menu_catalog" />
					</a></li>
				<li role="presentation"><a href="${ctx}/app/kb/tag/admin/index">
						<lb:get key="lab_kb_menu_tag" />
					</a></li>
				<li role="presentation"><a href="${ctx}/app/kb/admin/approval">
						<lb:get key="lab_kb_menu_approval" />
					</a></li>
			</ul>
			<h1 class="messtit fontfamily" style="margin-left: 30px; margin-top: 10px">
				<lb:get key="lab_kb_menu_catalog_${type}" />
			</h1>
			<div class="panel-body">
				<form:form modelAttribute="kbCatalog" cssClass="form-horizontal" enctype="multipart/form-data">
					<form:input type="hidden" cssStyle="width: 300px;" cssClass="form-control" path="kbc_id" />
					<div class="form-group">
						<form:label cssClass="col-sm-2 control-label" path="kbc_title">
							<span>*</span>
							<lb:get key="lab_kb_title" />
							<span>:</span>
						</form:label>
						<div class="col-sm-10">
							<form:input cssStyle="width: 300px;" cssClass="form-control" path="kbc_title" />
							<form:errors path="kbc_title" cssClass="error" />
						</div>
					</div>
					<div class="form-group">
						<form:label cssClass="col-sm-2 control-label" path="kbc_desc">
							<lb:get key="lab_kb_desc" />
							<span>:</span>
						</form:label>
						<div class="col-sm-10">
							<form:textarea cssStyle="width: 300px;" cssClass="form-control" path="kbc_desc"></form:textarea>
							<form:errors path="kbc_desc" cssClass="error" />
						</div>
					</div>
					<div class="form-group">
						<form:label cssClass="col-sm-2 control-label" path="tcTrainingCenter.tcr_id">
							<span>*</span>
							<lb:get key="traning_center" />
							<span>:</span>
						</form:label>
						<div class="col-sm-10">
							<form:select id="tc-selector-single" cssStyle="width: 120px;" cssClass="form-control" path="tcTrainingCenter.tcr_id">
								<c:if test="${not empty kbCatalog.tcTrainingCenter.tcr_id}">
									<form:option value="${kbCatalog.tcTrainingCenter.tcr_id}" label="${kbCatalog.tcTrainingCenter.tcr_title}" />
								</c:if>
							</form:select>
							<form:errors path="tcTrainingCenter.tcr_id" cssClass="error" />
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-2 col-sm-10">
							<button type="submit" class="btn btn-default">
								<lb:get key="button_ok" />
							</button>
							<button type="button" class="btn btn-default navbar-btn" onclick="javascript:go('${ctx}/app/kb/catalog/admin/index');">
								<lb:get key="button_cancel" />
							</button>
						</div>
					</div>
				</form:form>
				<div class="clear"></div>
			</div>
			<div class="clear"></div>
		</div>
	</div>
</body>
</html>