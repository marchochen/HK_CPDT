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
	var tcr_id;
	var timer = null;
	$(function() {
		//知识点
		var dtModel = [
				{
					display : fetchLabel('lab_kb_title'),
					tdWidth : '26%',
					format : function(data) {
						p = {
							text : data.kbc_title,
							title_all : data.kbc_title,
							kbc_id : data.kbc_id
						}
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
					align : 'center',
					tdWidth : '8%',
					format : function(data) {
						p = {
							text : data.kbc_knowledge_number ? data.kbc_knowledge_number
									: 0
						}
						return $('#text-center-template').render(p);
					}
				},
				{
					display : fetchLabel('lab_kb_status'),
					align : 'center',
					tdWidth : '8%',
					format : function(data) {
						p = {
							text : fetchLabel('lab_STATUS_' + data.kbc_status)
						}
						return $('#text-center-template').render(p);
					}
				},
				{
					display : fetchLabel('lab_create_datetime'),
					align : 'center',
					tdWidth : '8%',
					format : function(data) {
						p = {
							text : data.kbc_create_datetime.substring(0, 10)
						}
						return $('#text-center-template').render(p);
					}
				},
				{
					display : fetchLabel('lab_kb_opera'),
					align : 'right',
					tdWidth : '25%',
					format : function(data) {
						var str = '';
						if (data.kbc_status == 'ON') {
							str = '<button type="button" class="btn btn-default btn-xs" onclick="operat(\'unpublish\','
									+ data.kbc_id + ')">';
							str = str + fetchLabel('global_unpublish');
							str = str + '</button>';
						} else {
							str = '<button type="button" class="btn btn-default btn-xs" onclick="operat(\'publish\','
									+ data.kbc_id + ')">';
							str = str + fetchLabel('lab_kb_publish');
							str = str + '</button>';
						}
						p = {
							kbc_id : data.kbc_id,
							kbc_status : data.kbc_status
						}
						if(data.kbc_type){
							return '';
						} else {
							return str + $('#text-operate-template').render(p);
						}
					}
				} ]

		dt = $("#kbcList").table({
			url : '${ctx}/app/kb/catalog/admin/indexJson',
			colModel : dtModel,
			rp : 10,
			hideHeader : false,
			usepager : true
		});
	});

	function reloadTable() {
		var status = $('#kbc_admin_status').attr("value");
		var title = $('#kbc_admin_title').attr("value");
		$(dt).reloadTable({
			url : '${ctx}/app/kb/catalog/admin/indexJson',
			params : {
				kbc_status : status,
				kbc_title : title,
				tcr_id : tcr_id
			}
		});
	};

	function operat(type, kbc_id) {
		if (type == 'delete') {
			if (confirm(fetchLabel('lab_kb_confirm'))) {
				$.getJSON(
						"${ctx}/app/kb/catalog/admin/delete?kbc_id=" + kbc_id,
						function(data) {
							if (!data.success) {
								alert(fetchLabel('lab_do_not_del_catalog'));
							} else {
								reloadTable();
							}
						});
			}
			return false;
		} else if (type == 'publish') {
			$.ajax({
				url : "${ctx}/app/kb/catalog/admin/publish",
				data : {
					kbc_id : kbc_id,
					kbc_status : "ON"
				},
				success : function(data) {
					reloadTable();
				}
			});
		} else if (type == 'unpublish') {
			$.ajax({
				url : "${ctx}/app/kb/catalog/admin/publish",
				data : {
					kbc_id : kbc_id,
					kbc_status : "OFF"
				},
				success : function(data) {
					reloadTable();
				}
			});
		}
		return false;
	};

	function showTcr() {
		var cityObj = $("#tcrSel");
		var cityOffset = $("#tcrSel").offset();
		$("#tcrContent").css({
			left : cityOffset.left + "px",
			top : cityOffset.top + cityObj.outerHeight() + "px"
		}).slideDown("fast");
	};

	var setting = {
		view : {
			selectedMulti : false,
			dblClickExpand : false
		},
		data : {
			simpleData : {
				enable : true
			}
		},
		async : {
			enable : true,
			url : "${ctx}/app/tree/tcListJson/withHead",
			autoParam : [ "id" ]
		},
		callback : {
			onClick : onClick
		},
		onClick : {

		}
	};

	$(document).ready(function() {
		$.getJSON("${ctx}/app/tree/tcListJson/withHead", function(result) {
			$.fn.zTree.init($("#tcrTree"), setting, result);
		});
	});

	function onClick(e, treeId, treeNode) {
		var zTree = $.fn.zTree.getZTreeObj("tcrTree");
		nodes = zTree.getSelectedNodes();
		var v = '';
		for (var i = 0, l = nodes.length; i < l; i++) {
			v += nodes[i].name + ",";
		}
		if (v.length > 0)
			v = v.substring(0, v.length - 1);
		var cityObj = $("#tcrSel");
		cityObj.html(v);
		hideMenu();

		$('#kbc_admin_status').attr("value", '');
		$('#kbc_admin_title').attr("value", '');
		tcr_id = treeNode.id;
		selectType = "";
		itemType = "";
		periods = "";
		var p = {
			tcr_id : tcr_id
		}
		$(dt).reloadTable({
			url : '${ctx}/app/kb/catalog/admin/indexJson',
			params : p
		});
	};

	function hideMenu() {
		$("#tcrContent").fadeOut("fast");
	};
</script>
<script id="text-center-template" type="text/x-jsrender">
		<div class="text-center">{{>text}}</div>
</script>
<script id="a-template" type="text/x-jsrender">
<div><a href="${ctx}/app/kb/admin/index?kbc_id={{>kbc_id}}" title="{{>title_all}}">{{>text}}</a></div>
</script>
<script id="text-operate-template" type="text/x-jsrender">
<button type="button" class="btn btn-default btn-xs" onclick="javascript:go('${ctx}/app/kb/catalog/admin/insert?kbc_id={{>kbc_id}}');">
	<lb:get key='lab_kb_update' />
</button>
<button type="button" class="btn btn-default btn-xs" onclick="operat('delete',{{>kbc_id}})">
	<lb:get key='lab_kb_delete' />
</button>
</script>
<style type="text/css">
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
				<lb:get key="lab_kb_menu_catalog" />
			</h1>
			<div class="panel-body">
				<div class="bs-docs-section">
					<nav class="navbar navbar-default">
						<div class="container-fluid">
							<div class="collapse navbar-collapse" style="margin-right:-30px" id="bs-example-navbar-collapse-1">
								<form class="navbar-form" role="search">
									<div class="navbar-left">
										<div class="input-group">
											<span id="tcrSel" onmouseover="showTcr();clearTimeout(timer)" onmouseout="timer=setTimeout('hideMenu()',500)" class="form-control" style="width: 200px"><lb:get key="traning_center" /></span><span
												class="input-group-addon" onmouseover="showTcr();clearTimeout(timer)" onmouseout="timer=setTimeout('hideMenu()',500)"><div class="glyphicon glyphicon-chevron-down"></div></span>
										</div>
									</div>
									<div class="navbar-right">
										<div class="form-group">
											<select id="kbc_admin_status" style="width: 125px;" class="form-control" onchange="reloadTable()">
												<option value=""><lb:get key='lab_kb_status' /></option>
												<option value="OFF"><lb:get key='lab_STATUS_OFF' /></option>
												<option value="ON"><lb:get key='lab_STATUS_ON' /></option>
											</select>
										</div>
										<div class="form-group">
											<input type="text" id="kbc_admin_title" class="form-control" placeholder="<lb:get key="lab_kb_prompt"/>" />
										</div>
										<button type="button" class="btn btn-default" onclick="reloadTable()">
											<lb:get key="lab_kb_search" />
										</button>
										<button type="button" class="btn btn-default" onclick="javascript:go('${ctx}/app/kb/catalog/admin/insert');">
											<lb:get key="button_add" />
										</button>
									</div>
								</form>
							</div>
						</div>
					</nav>
				</div>
				<div class="clear"></div>
				<div id="kbcList"></div>
			</div>
		</div>
	</div>
	<div id="tcrContent" onmouseover="clearTimeout(timer);" onmouseout="timer=setTimeout('hideMenu()',500)" class="tcrContent" style="display: none; position: absolute;">
		<ul id="tcrTree" class="ztree" style="margin-top: 0; width: 160px;"></ul>
	</div>
</body>
</html>