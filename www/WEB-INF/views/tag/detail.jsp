<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<script type="text/javascript">
	function operat(type, tag_id) {
		if (type == 'delete') {
			if (confirm(fetchLabel('lab_kb_confirm'))) {
				$.getJSON("${ctx}/app/kb/tag/admin/detele?tag_id=" + tag_id,
						function(data) {
							if (!data.success) {
								alert('不能删除该目录！');
							} else {
								go('${ctx}/app/kb/tag/admin/index')
							}
						});
			}
		}
		return false;
	};
</script>
</head>
<body>
	<div class="container bs-docs-container">
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
			<h1 class="messtit fontfamily" style="margin-left: 30px;margin-top:10px">
				<lb:get key="lab_kb_menu_tag_${type}" />
			</h1>
			<div class="panel-body">
				<div>
					<div class="text-right">
						<div class="col-sm-offset-2 col-sm-10">
							<button type="button" onclick="javascript:go('${ctx}/app/kb/tag/admin/insert?tag_id=${tag.tag_id}')" class="btn btn-default">
								<lb:get key="button_update" />
							</button>
							<button type="button" onclick="operat('delete',${tag.tag_id})" class="btn btn-default">
								<lb:get key="button_del" />
							</button>
							<button type="button" onclick="javascript:go('${ctx}/app/kb/tag/admin/index')" class="btn btn-default">
								<lb:get key="button_back" />
							</button>
						</div>
					</div>
				</div>
				<div class="form-group">
					<div class="quest">
						<label class="col-sm-3  text-right"><lb:get key="tag_title" />:</label>
						<div class="col-sm-7">
							<label class="control-label">${tag.tag_title}</label>
						</div>
					</div>
				</div>
			</div>
		</div>
</body>
</html>