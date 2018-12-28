<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<%@ include file="../common/template.jsp"%>
<!DOCTYPE html>
<html>
<head>

<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-learner.css" />

<script type="text/javascript">
	var searchTb;

	$(function() {
		searchTb = $("#tag_list").table({
			url : '${ctx}/app/kb/tag/admin/listJson',
			dataType : 'json',
			colModel : colModel,
			rp : 10,
			showpager : 3,
			usepager : true
		});
	})

	var colModel = [
			{
				display : fetchLabel('lab_kb_title'),
				width : '20%',
				format : function(data) {
					p = {
						className : 'wzb-link02',
						href : '${ctx}/app/kb/tag/admin/detail/' + data.tag_id,
						title : data.tag_title
					};
					return $('#a-template').render(p);
				}
			},
			{
				display : fetchLabel('traning_center'),
				align : 'center',
				tdWidth : '20%',
				format : function(data) {
					p = {
						text : data.tcTrainingCenter.tcr_title
					}
					return $('#text-center-template').render(p);
				}
			},
			{
				display : fetchLabel('lab_kb_knowledge_number'),
				align : "center",
				width : '15%',
				format : function(data) {
					p = {
						text : data.tag_knowledge_number ? data.tag_knowledge_number
								: 0
					};
					return $('#text-center-template').render(p);
				}
			}, {
				display : fetchLabel('lab_create_datetime'),
				align : "center",
				width : '15%',
				format : function(data) {
					p = {
						text : data.tag_create_datetime.substring(0, 10)
					};
					return $('#text-center-template').render(p);
				}
			}, {
				display : fetchLabel('lab_kb_opera'),
				width : '30%',
				align : 'right',
				format : function(data) {
					p = {
						tag_id : data.tag_id,
						num : data.tag_knowledge_number ? data.tag_knowledge_number : 0
					};
					return $('#text-operate-template').render(p);
				}
			} ];

	function reloadTable() {
		var tag_title = $('#searchText').attr("value");
		$(searchTb).reloadTable({
			url : '${ctx}/app/kb/tag/admin/listJson',
			params : {
				tag_title : tag_title
			},
			dataType : 'json'
		});
	};

	function operat(type, tag_id, num) {
		if (type == 'delete') {
			var text = fetchLabel('lab_kb_confirm');
			if (num > 0) {
				text = fetchLabel('lab_kb_comfirm_del_tag');
			}
			if (confirm(text)) {
				$.ajax({
					url : "${ctx}/app/kb/tag/admin/detele?tag_id=" + tag_id,
					success : function(data) {
						reloadTable();
					}
				});
			}
		}
		return false;
	};
</script>
<script id="text-operate-template" type="text/x-jsrender">
<button type="button" class="btn btn-default btn-xs" onclick="javascript:go('${ctx}/app/kb/tag/admin/insert?tag_id={{>tag_id}}');">
	<lb:get key='lab_kb_update' />
</button>
<button type="button" class="btn btn-default btn-xs" onclick="operat('delete',{{>tag_id}},{{>num}})">
	<lb:get key='lab_kb_delete' />
</button>
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
			<h1 class="messtit fontfamily" style="margin-left: 30px; margin-top: 10px">
				<lb:get key="lab_kb_menu_tag" />
			</h1>
			<div class="panel-body">
				<div class="bs-docs-section">
					<nav class="navbar navbar-default">
						<div class="container-fluid">
							<div class="collapse navbar-collapse" style="margin-right:-30px" id="bs-example-navbar-collapse-1">
								<form class="navbar-form" role="search">
									<div class="navbar-right">
										<div class="form-group">
											<input type="text" id="searchText" name="searchText" class="form-control" placeholder="<lb:get key="lab_kb_prompt"/>" />
										</div>
										<button type="button" class="btn btn-default" id="searchBtn" onclick="reloadTable()">
											<lb:get key="button_search" />
										</button>
										<button type="button" class="btn btn-default" onclick="javascript:go('${ctx}/app/kb/tag/admin/insert')">
											<lb:get key="button_add" />
										</button>
									</div>
								</form>
							</div>
						</div>
					</nav>
				</div>
				<div id="tag_list"></div>
			</div>
		</div>
	</div>
</body>
</html>