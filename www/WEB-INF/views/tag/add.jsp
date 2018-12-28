<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.cwn.wizbank.entity.*,java.util.*,com.cw.wizbank.util.*, com.cw.wizbank.qdb.*"%>
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
	<div class="container">
		<div class="panel panel-success">
			<ul class="nav nav-tabs" id="menuTabs">
				<li role="presentation"><a href="${ctx}/app/kb/admin/index">
						<lb:get key="lab_kb_menu_knowledge" />
					</a></li>
				<li role="presentation"><a href="${ctx}/app/kb/catalog/admin/index">
						<lb:get key="lab_kb_menu_catalog" />
					</a></li>
				<li role="presentation" class="active"><a href="${ctx}/app/kb/tag/admin/index">
						<lb:get key="lab_kb_menu_tag" />
					</a></li>
				<li role="presentation"><a href="${ctx}/app/kb/admin/approval">
						<lb:get key="lab_kb_menu_approval" />
					</a></li>
			</ul>
			<h1 class="messtit fontfamily" style="margin-left: 30px; margin-top: 10px">
				<lb:get key="lab_kb_menu_tag_${type}" />
			</h1>
			<div class="panel-body">
				<form:form modelAttribute="tag" action="${ctx}/app/kb/tag/admin/save" method="post" cssClass="form-horizontal">
					<form:hidden path="tag_id" />

					<div class="form-group">
						<div class="quest">
							<form:label cssClass="col-sm-2 control-label" path="tag_title">
								<span>*</span>
								<lb:get key="tag_title" />
								<span>:</span>
							</form:label>
							<div class="col-sm-10">
								<form:input cssStyle="width: 300px;" cssClass="form-control" path="tag_title" />
								<form:errors path="tag_title" cssClass="control-label has-error error" />
								<!-- has-error -->
							</div>
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
								<c:if test="${not empty tag.tcTrainingCenter.tcr_id}">
									<form:option value="${tag.tcTrainingCenter.tcr_id}" label="${tag.tcTrainingCenter.tcr_title}" />
								</c:if>
							</form:select>
							<form:errors path="tcTrainingCenter.tcr_id" cssClass="error" />
						</div>
					</div>
					<div>
						<div class="form-group">
							<div class="col-sm-offset-2 col-sm-10">
								<button type="submit" class="btn btn-default">
									<lb:get key="button_ok" />
								</button>
								<button type="button" onclick="javascript:go('${ctx}/app/kb/tag/admin/index')" class="btn btn-default">
									<lb:get key="button_cancel" />
								</button>
							</div>
						</div>
					</div>
				</form:form>
			</div>
		</div>
	</div>
</body>
</html>