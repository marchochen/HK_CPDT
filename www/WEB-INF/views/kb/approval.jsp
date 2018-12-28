<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${ctx}/js/tree/css/ztree.css" />
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-learner.css" />
<script type="text/javascript" src="${ctx}/js/tree/js/jquery.ztree.core.js"></script>
<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<script type="text/javascript">
	var dt;
	$(function() {
		//知识点
		var dtModel = [
				{
					display : fetchLabel('lab_kb_title'),
					width : '24%',
					format : function(data) {
						var title = '';
						if (data.kbi_title.length > 15) {
							title = data.kbi_title.substring(0, 15) + '...'
						} else {
							title = data.kbi_title
						}
						p = {
							text : title,
							title_all : data.kbi_title,
							kbi_id : data.kbi_id
						}
						return $('#a-template').render(p);
					}
				},
				{
					display : fetchLabel('lab_kb_type'),
					align : 'center',
					width : '8%',
					format : function(data) {
						p = {
							text : fetchLabel('lab_TYPE_' + data.kbi_type)
						}
						return $('#text-center-template').render(p);
					}
				},
				{
					display : fetchLabel('lab_kb_share_person'),
					align : 'center',
					width : '8%',
					format : function(data) {
						p = {
							text : data.usr_display_bil
						}
						return $('#text-center-template').render(p);
					}
				},
				{
					display : fetchLabel('lab_kb_share_time'),
					align : 'center',
					width : '10%',
					format : function(data) {
						p = {
							text : data.kbi_create_datetime.substring(0, 10)
						}
						return $('#text-center-template').render(p);
					}
				},
				{
					display : fetchLabel('lab_kb_app_status'),
					align : 'center',
					width : '12%',
					format : function(data) {
						p = {
							text : fetchLabel('lab_APP_STATUS_'
									+ data.kbi_app_status)
						}
						return $('#text-center-template').render(p);
					}
				},
				{
					display : fetchLabel('lab_kb_app_person'),
					align : 'center',
					width : '8%',
					format : function(data) {
						p = {
							text : data.kbi_approve_usr_display_bil ? data.kbi_approve_usr_display_bil
									: '--'
						}
						return $('#text-center-template').render(p);
					}
				},
				{
					display : fetchLabel('lab_kb_app_time'),
					align : 'center',
					width : '10%',
					format : function(data) {
						p = {
							text : data.kbi_approve_datetime ? data.kbi_approve_datetime
									.substring(0, 10)
									: '--'
						}
						return $('#text-center-template').render(p);
					}
				},
				{
					display : fetchLabel('lab_kb_opera'),
					align : 'right',
					width : '20%',
					format : function(data) {
						p = {
							kbi_id : data.kbi_id
						}
						if (data.kbi_app_status == 'PENDING') {
							return $('#text-operate-template').render(p);
						} else if (data.kbi_app_status == 'REAPPROVAL') {
							return $('#update-operate-template').render(p);
						} else {
							var str = '';
							if (data.kbi_status == 'ON') {
								str = '<button type="button" class="btn btn-default btn-xs" onclick="operat(\'unpublish\','
										+ data.kbi_id + ')">';
								str = str + fetchLabel('global_unpublish');
								str = str + '</button>';
							} else {
								str = '<button type="button" class="btn btn-default btn-xs" onclick="operat(\'publish\','
										+ data.kbi_id + ')">';
								str = str + fetchLabel('lab_kb_publish');
								str = str + '</button>';
							}
							return str
									+ $('#update-operate-template').render(p);
						}
					}
				} ]

		dt = $("#kbiList").table({
			url : '${ctx}/app/kb/admin/indexJson',
			colModel : dtModel,
			rp : 10,
			hideHeader : false,
			usepager : true,
			params : {
				source : 'approval'
			}
		});
	})

	function reloadTable() {
		var app_status = $('#kb_admin_app_status').attr("value");
		var type = $('#kb_admin_type').attr("value");
		var title = $('#kb_admin_title').attr("value");
		$(dt).reloadTable({
			url : '${ctx}/app/kb/admin/indexJson',
			params : {
				kbi_app_status : app_status,
				kbi_type : type,
				kbi_title : title,
				source : 'approval'
			}
		});
	};

	function operat(type, kbi_id) {
		if (type == 'delete') {
			if (confirm(fetchLabel('lab_kb_confirm'))) {
				$.ajax({
					url : "${ctx}/app/kb/admin/delete?kbi_id=" + kbi_id,
					success : function(data) {
						reloadTable();
					}
				});
			}
		} else if (type == 'publish') {
			$.ajax({
				url : "${ctx}/app/kb/admin/publish",
				data : {
					kbi_id : kbi_id,
					kbi_status : "ON"
				},
				success : function(data) {
					reloadTable();
				}
			});

		} else if (type == 'unpublish') {
			$.ajax({
				url : "${ctx}/app/kb/admin/publish",
				data : {
					kbi_id : kbi_id,
					kbi_status : "OFF"
				},
				success : function(data) {
					reloadTable();
				}
			});
		} else {
			$.ajax({
				url : "${ctx}/app/kb/admin/approval",
				method : 'post',
				data : {
					kbi_id : kbi_id,
					kbi_app_status : type
				},
				success : function(data) {
					reloadTable();
				}
			});
		}
		return false;
	};
</script>
<script id="image-center-template" type="text/x-jsrender">
		<div class="text-center"><img src="${ctx}/{{>text}}" alt=""/></div>
</script>
<script id="text-center-template" type="text/x-jsrender">
		<div class="text-center">{{>text}}</div>
</script>
<script id="a-template" type="text/x-jsrender">
		<div><a href="${ctx}/app/kb/admin/view?source=approval&kbi_id={{>kbi_id}}" title="{{>title_all}}">{{>text}}</a></div>
</script>
<!-- 操作模版 -->
<script id="text-operate-template" type="text/x-jsrender">
<button type="button" class="btn btn-default btn-xs" onclick="operat('APPROVED',{{>kbi_id}})">
	<lb:get key='lab_kb_thr_approval' />
</button>
<button type="button" class="btn btn-default btn-xs" onclick="operat('REAPPROVAL',{{>kbi_id}})">
	<lb:get key='lab_kb_re_approval' />
</button>
<button type="button" class="btn btn-default btn-xs" onclick="javascript:go('${ctx}/app/kb/admin/insert?kbi_id={{>kbi_id}}&source=approval');">
	<lb:get key='lab_kb_update' />
</button>
</script>
<script id="update-operate-template" type="text/x-jsrender">
<button type="button" class="btn btn-default btn-xs" onclick="javascript:go('${ctx}/app/kb/admin/insert?kbi_id={{>kbi_id}}&source=approval');">
	<lb:get key='lab_kb_update' />
</button>
<button type="button" class="btn btn-default btn-xs" onclick="operat('delete',{{>kbi_id}})">
	<lb:get key='lab_kb_delete' />
</button>
</script>
<style type="text/css">
#menuTabs a {
	outline: none;
}

table a {
	color: #000
}
</style>
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
				<li role="presentation"><a href="${ctx}/app/kb/tag/admin/index">
						<lb:get key="lab_kb_menu_tag" />
					</a></li>
				<li role="presentation" class="active"><a href="${ctx}/app/kb/admin/approval">
						<lb:get key="lab_kb_menu_approval" />
					</a></li>
			</ul>
			<h1 class="messtit fontfamily" style="margin-left: 30px; margin-top: 10px">
				<lb:get key="lab_kb_menu_approval" />
			</h1>
			<div class="panel-body">
				<div class="bs-docs-section">
					<!-- 查询开始 -->
					<nav class="navbar navbar-default">
						<div class="container-fluid">
							<div class="collapse navbar-collapse" style="margin-right:-15px" id="bs-example-navbar-collapse-1">
								<form class="navbar-form navbar-right" role="search">
									<!-- 类型 -->
									<div class="form-group">
										<select id="kb_admin_type" style="width: 90px;" class="form-control" onchange="reloadTable()">
											<option value="">
												<lb:get key='lab_kb_type' />
											</option>
											<option value="ARTICLE"><lb:get key='lab_TYPE_ARTICLE' /></option>
											<option value="DOCUMENT"><lb:get key='lab_TYPE_DOCUMENT' /></option>
											<option value="VEDIO"><lb:get key='lab_TYPE_VEDIO' /></option>
											<option value="IMAGE"><lb:get key='lab_TYPE_IMAGE' /></option>
										</select>
									</div>
									<!-- 状态 -->
									<div class="form-group">
										<select id="kb_admin_app_status" style="width: 150px;" class="form-control" onchange="reloadTable()">
											<option value="">
												<lb:get key='lab_kb_app_status' />
											</option>
											<option value="PENDING"><lb:get key='lab_APP_STATUS_PENDING' /></option>
											<option value="APPROVED"><lb:get key='lab_APP_STATUS_APPROVED' /></option>
											<option value="REAPPROVAL"><lb:get key='lab_APP_STATUS_REAPPROVAL' /></option>
										</select>
									</div>
									<!-- 标题 -->
									<div class="form-group">
										<input type="text" id="kb_admin_title" class="form-control" placeholder="<lb:get key="lab_kb_prompt"/>" />
									</div>
									<button type="button" class="btn btn-default" onclick="reloadTable()">
										<lb:get key="lab_kb_search" />
									</button>
								</form>
							</div>
						</div>
					</nav>
					<!-- 查询结束 -->
					<div class="clear"></div>
					<div id="kbiList"></div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>