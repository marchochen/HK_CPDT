<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-learner.css" />
<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<script type="text/javascript">
	var dt;
	var catalog;
	var kbc_id = ${kbc_id};
	$(function() {
		//知识点
		var dtModel = [
				{
					display : '<div style="margin-bottom:6px"><input id="kbi_all" onclick="getCheckedIds()" type="checkbox" value="" name="kbi_all_checkbox" /></div>',
					align : 'center',
					width : '6%',
					format : function(data) {
						p = {
							text : data.kbi_id
						}
						return $('#input-template').render(p);
					}
				},
				{
					display : fetchLabel('lab_kb_title'),
					align : 'left',
					width : '33%',
					sortable : true,
					name : 'kbi_title',
					format : function(data) {
						var title = '';
						if (data.kbi_title.length > 14) {
							title = data.kbi_title.substring(0, 14) + '...'
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
					sortable : true,
					name : 'kbi_type',
					format : function(data) {
						p = {
							text : fetchLabel('lab_TYPE_' + data.kbi_type)
						}
						return $('#text-center-template').render(p);
					}
				},
				{
					display : fetchLabel('lab_kb_count_view'),
					align : 'center',
					width : '10%',
					sortable : true,
					name : 'kbi_access_count',
					format : function(data) {
						p = {
							text : data.kbi_access_count ? data.kbi_access_count
									: 0
						}
						return $('#text-center-template').render(p);
					}
				},
				{
					display : fetchLabel('lab_kb_click_praise'),
					align : 'center',
					width : '8%',
					sortable : true,
					name : 's_cnt_like_count',
					format : function(data) {
						p = {
							text : data.s_cnt_like_count ? data.s_cnt_like_count
									: 0
						}
						return $('#text-center-template').render(p);
					}
				},
				{
					display : fetchLabel('lab_kb_status'),
					align : 'center',
					width : '8%',
					sortable : true,
					name : 'kbi_status',
					format : function(data) {
						p = {
							text : fetchLabel('lab_STATUS_' + data.kbi_status)
						}
						return $('#text-center-template').render(p);
					}
				},
				{
					display : fetchLabel('lab_kb_opera'),
					width : '27%',
					align : 'right',
					format : function(data) {
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
						p = {
							kbi_id : data.kbi_id,
							kbi_status : data.kbi_status
						}
						return str + $('#text-operate-template').render(p);
					}
				} ]

		//知识目录
		var catalogModel = [ {
			display : fetchLabel('lab_kb_title'),
			format : function(data) {
				var title = '';
				var css = ''
				if (data.kbc_title.length > 9) {
					title = data.kbc_title.substring(0, 9) + '...'
				} else {
					title = data.kbc_title
				}
				if (kbc_id == data.kbc_id) {
					css = 'active'
				}
				p = {
					text : title,
					id : data.kbc_id,
					css : css,
					title_all : data.kbc_title
				}
				return $('#catalog-template').render(p);
			}
		} ]

		dt = $("#kbiList").table({
			url : '${ctx}/app/kb/admin/indexJson',
			colModel : dtModel,
			rp : 10,
			hideHeader : false,
			usepager : true,
			params : {
				kbi_app_status : 'APPROVED',
				kbc_id : kbc_id
			}
		});

		catalog = $("#kb_item_catalog").table({
			url : '${ctx}/app/kb/catalog/admin/indexJson',
			colModel : catalogModel,
			rp : 7,
			hideHeader : true,
			usepager : true
		});
	})

	function reloadTable() {
		var status = $('#kb_admin_status').attr("value");
		var type = $('#kb_admin_type').attr("value");
		var title = $('#kb_admin_title').attr("value");
		var id = kbc_id;
		$(dt).reloadTable({
			params : {
				kbi_status : status,
				kbi_type : type,
				kbi_title : title,
				kbc_id : id,
				kbi_app_status : 'APPROVED'
			}
		});
	};

	function changCatalog(thisObj, id) {
		$('#kb_admin_status').attr("value", '');
		$('#kb_admin_type').attr("value", '');
		$('#kb_admin_title').attr("value", '');
		$('.nav-stacked li').removeClass('active');
		$(thisObj).addClass('active');
		kbc_id = id;
		reloadTable()
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
		}
		return false;
	};

	function getCheckedIds() {
		var isChecked = $('#kbi_all').attr("checked");
		if (!isChecked) {
			$('input[name="kbi_checkbox"]').each(function() {
				$(this).attr("checked", false);
			});
		} else {
			$('input[name="kbi_checkbox"]').each(function() {
				$(this).attr("checked", true);
			});
		}
	}

	function delKnowledgeByIds() {
		var ids = '';
		$('input[name="kbi_checkbox"]:checked').each(function() {
			ids += $(this).val() + ',';
		});
		if (ids == '') {
			Dialog.alert(fetchLabel('lab_kb_del_knowledge_no_ids'));
		} else {
			if (confirm(fetchLabel('lab_kb_confirm'))) {
				$.post("${ctx}/app/kb/admin/deleteByIds", {
					ids : ids
				}, function() {
					reloadTable();
					$('input[name="kbi_all_checkbox"]').each(function() {
						$(this).attr("checked", false);
					});
				});
			}
		}
	}
</script>
<script id="text-center-template" type="text/x-jsrender">
		<div class="text-center">{{>text}}</div>
</script>
<script id="a-template" type="text/x-jsrender">
		<div><a href="${ctx}/app/kb/admin/view?source=index&kbi_id={{>kbi_id}}" title="{{>title_all}}">{{>text}}</a></div>
</script>
<script id="input-template" type="text/x-jsrender">
  <div style="margin-bottom:6px"><input type="checkbox" name="kbi_checkbox" value="{{>text}}" /></div>
</script>
<script id="catalog-template" type="text/x-jsrender">
<ul class="nav nav-pills nav-stacked" > 
<li role="presentation" class="{{>css}}" onclick="changCatalog(this,{{>id}})"><a href="#" title="{{>title_all}}">{{>text}}</a></li>
</ul>
</script>
<!-- 操作模版 -->
<script id="text-operate-template" type="text/x-jsrender">
<button type="button" class="btn btn-default btn-xs" onclick="javascript:go('${ctx}/app/kb/admin/insert?kbi_id={{>kbi_id}}');">
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

.clickCatalog {
	background-color: gray;
}
</style>
</head>
<body>
	<div class="container bs-docs-container">
		<div class="panel panel-success">
			<ul class="nav nav-tabs" id="menuTabs">
				<li role="presentation" class="active"><a href="${ctx}/app/kb/admin/index">
						<lb:get key="lab_kb_menu_knowledge" />
					</a></li>
				<li role="presentation"><a href="${ctx}/app/kb/catalog/admin/index">
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
				<lb:get key="lab_kb_menu_knowledge_manager" />
			</h1>
			<div class="panel-body">
				<div class="col-md-3">
					<div class="panel panel-default">
						<div class="panel-heading">
							<div onclick="changCatalog(0)">
								<a href="#">
									<lb:get key="lab_kb_catalog" />
								</a>
							</div>
						</div>
						<div class="panel-body">
							<div id="kb_item_catalog"></div>
						</div>
					</div>
				</div>
				<div class="col-md-9" role="main">
					<div class="bs-docs-section">
						<!-- 查询开始 -->
						<nav class="navbar navbar-default">
							<div class="container-fluid">

								<div class="collapse navbar-collapse" style="margin-left:-30px;margin-right:-25px" id="bs-example-navbar-collapse-1">
									<form class="navbar-form navbar-right" role="search">
										<!-- 状态 -->
										<div class="form-group">
											<select id="kb_admin_status" style="width: 125px;" class="form-control" onchange="reloadTable()">
												<option value="">
													<lb:get key='lab_kb_status' />
												</option>
												<option value="OFF"><lb:get key='lab_STATUS_OFF' /></option>
												<option value="ON"><lb:get key='lab_STATUS_ON' /></option>
											</select>
										</div>
										<!-- 类型 -->
										<div class="form-group">
											<select id="kb_admin_type" style="width: 112px;" class="form-control" onchange="reloadTable()">
												<option value="">
													<lb:get key='lab_kb_type' />
												</option>
												<option value="ARTICLE"><lb:get key='lab_TYPE_ARTICLE' /></option>
												<option value="DOCUMENT"><lb:get key='lab_TYPE_DOCUMENT' /></option>
												<option value="VEDIO"><lb:get key='lab_TYPE_VEDIO' /></option>
												<option value="IMAGE"><lb:get key='lab_TYPE_IMAGE' /></option>
											</select>
										</div>
										<!-- 标题 -->
										<div class="form-group">
											<input type="text" style="width:155px" id="kb_admin_title" class="form-control" placeholder="<lb:get key="lab_kb_prompt_title_desc_content"/>" />
										</div>
										<button type="button" class="btn btn-default" onclick="reloadTable()">
											<lb:get key="lab_kb_search" />
										</button>
										<div class="form-group">
											<div class="dropdown">
												<button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown">
													<lb:get key="button_add" />
													<span class="caret"></span>
												</button>
												<ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu3">
													<li role="presentation"><a href="${ctx}/app/kb/admin/insert?kbi_type=ARTICLE">
															<lb:get key="lab_TYPE_ARTICLE" />
														</a></li>
													<li role="presentation"><a href="${ctx}/app/kb/admin/insert?kbi_type=DOCUMENT">
															<lb:get key="lab_TYPE_DOCUMENT" />
														</a></li>
													<li role="presentation"><a href="${ctx}/app/kb/admin/insert?kbi_type=VEDIO">
															<lb:get key="lab_TYPE_VEDIO" />
														</a></li>
													<li role="presentation"><a href="${ctx}/app/kb/admin/insert?kbi_type=IMAGE">
															<lb:get key="lab_TYPE_IMAGE" />
														</a></li>
												</ul>
											</div>
										</div>
										<button type="button" class="btn btn-default" onclick="delKnowledgeByIds()">
											<lb:get key="lab_kb_del_knowledge_by_ids" />
										</button>
									</form>
								</div>
							</div>
						</nav>
						<!-- 查询结束 -->
					</div>
					<div class="clear"></div>
					<div id="kbiList"></div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>