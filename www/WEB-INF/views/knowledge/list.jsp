<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-learner.css" />
<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<title></title>
<style>
	a.toolcolor:hover{color:#428bca;}
	a.toolcolor{color:#333;}
</style>
<script type="text/javascript">
	var dt;
	var tab_app = '${tab}';
	$(function() {
		var model = knowledgeModel;
		if (tab_app == 'APPROVED') {
			model = dtModel;
			console.log(dtModel);
		} else if (tab_app == 'REAPPROVAL') {
			model = oprateModel;
		}
		dt = $("#knowledge-list").table({
			url : '${ctx}/app/kb/center/index/my_share',
			colModel : model,
			rp : 10,
			hideHeader : false,
			usepager : true,
			
			params : {
				kbi_app_status : tab_app
			}
			
		});
		$('.wbtabinfo').find('li').removeClass('active');
		$('#'+tab_app).addClass("active");
	});
	var dtModel = [ {//標題
		sortable : true,
		name : 'kbi_title',
		display : fetchLabel('lab_kb_title'),
		width : '30%',
		format : function(data) {
			var title = '';
			if (getChars(data.kbi_title) > 24) {
				title = substr(data.kbi_title, 0, 24) + '...'
			} else {
				title = data.kbi_title
			}
			p = {
				text : title,
				id : data.kbi_id,
				enc_kbi_id : wbEncrytor().cwnEncrypt(data.kbi_id),
				title_all : data.kbi_title,
				tab : 'APPROVED'
			}
			return $('#a-template').render(p);
		}
	}, {//類型
		sortable : true,
		name : 'kbi_type',
		display : fetchLabel('lab_kb_type'),
		align : 'center',
		width : '10%',
		format : function(data) {
			p = {
				text : fetchLabel('lab_TYPE_' + data.kbi_type)
			}
			return $('#text-template').render(p);
		}
	}, {//狀態
		sortable : true,
		name : 'kbi_app_status',
		display : fetchLabel('lab_kb_status'),
		align : 'center',
		width : '15%',
		format : function(data) {
			p = {
				text : fetchLabel('lab_APP_STATUS_' + data.kbi_app_status)
			}
			return $('#text-template').render(p);
		}
	}, {//分享日期
		sortable : true,
		name : 'kbi_create_datetime',
		display : fetchLabel('lab_kb_share_time'),
		align : 'center',
		width : '20%',
		format : function(data) {
			p = {
				text : data.kbi_create_datetime.substring(0, 10)
			}
			return $('#text-template').render(p);
		}
	},/*  {//贊
		sortable : true,
		name : 's_cnt_like_count',
		display : fetchLabel('lab_kb_praise'),
		align : 'center',
		width : '10%',
		format : function(data) {
			p = {
				text : data.s_cnt_like_count ? data.s_cnt_like_count : 0
			}
			return $('#text-template').render(p);
		}
	}, */ {//瀏覽量
		sortable : true,
		name : 'kbi_access_count',
		display : fetchLabel('lab_kb_count_view'),
		align : 'right',
		width : '15%',
		format : function(data) {
			p = {
				text : data.kbi_access_count ? data.kbi_access_count : 0
			}
			return $('#text-template').render(p);
		}
	} ];
	console.log(dtModel);

	var knowledgeModel = [ {
		sortable : true,
		name : 'kbi_title',
		display : fetchLabel('lab_kb_title'),
		width : '30%',
		format : function(data) {
			var title = '';
			if (getChars(data.kbi_title) > 24) {
				title = substr(data.kbi_title, 0, 24) + '...'
			} else {
				title = data.kbi_title
			}
			p = {
				text : title,
				title_all : data.kbi_title,
				id : data.kbi_id,
				enc_kbi_id : wbEncrytor().cwnEncrypt(data.kbi_id),
				tab : 'PENDING'
			}
			return $('#a-template').render(p);
		}
	}, {
		sortable : true,
		name : 'kbi_type',
		display : fetchLabel('lab_kb_type'),
		align : 'center',
		width : '15%',
		format : function(data) {
			p = {
				text : fetchLabel('lab_TYPE_' + data.kbi_type)
			}
			return $('#text-template').render(p);
		}
	}, {
		sortable : true,
		name : 'kbi_app_status',
		display : fetchLabel('lab_kb_status'),
		align : 'center',
		width : '15%',
		format : function(data) {
			p = {
				text : fetchLabel('lab_APP_STATUS_' + data.kbi_app_status)
			}
			return $('#text-template').render(p);
		}
	}, {
		sortable : true,
		name : 'kbi_create_datetime',
		display : fetchLabel('lab_kb_share_time'),
		align : 'center',
		width : '20%',
		format : function(data) {
			p = {
				text : data.kbi_create_datetime.substring(0, 10)
			}
			return $('#text-template').render(p);
		}
	}, {
		display : fetchLabel('lab_kb_opera'),
		width : '20%',
		align : 'right',
		format : function(data) {
			p = {
				kbi_id : data.kbi_id,
				kbi_status : data.kbi_status
			}
			return $('#update-operate-template').render(p);
		}
	} ];

	var oprateModel = [
			{
				sortable : true,
				name : 'kbi_title',
				display : fetchLabel('lab_kb_title'),
				width : '30%',
				format : function(data) {
					var title = '';
					if (getChars(data.kbi_title) > 24) {
						title = substr(data.kbi_title, 0, 24) + '...'
					} else {
						title = data.kbi_title
					}
					p = {
						text : title,
						title_all : data.kbi_title,
						id : data.kbi_id,
						enc_kbi_id : wbEncrytor().cwnEncrypt(data.kbi_id),
						tab : 'REAPPROVAL'
					}
					return $('#a-template').render(p);
				}
			},
			{
				sortable : true,
				name : 'kbi_type',
				display : fetchLabel('lab_kb_type'),
				align : 'center',
				width : '12%',
				format : function(data) {
					p = {
						text : fetchLabel('lab_TYPE_' + data.kbi_type)
					}
					return $('#text-template').render(p);
				}
			},
			{
				sortable : true,
				name : 'kbi_app_status',
				display : fetchLabel('lab_kb_status'),
				align : 'center',
				width : '12%',
				format : function(data) {
					p = {
						text : fetchLabel('lab_APP_STATUS_'
								+ data.kbi_app_status)
					}
					return $('#text-template').render(p);
				}
			},
			{
				sortable : true,
				name : 'kbi_create_datetime',
				display : fetchLabel('lab_kb_share_time'),
				align : 'center',
				width : '12%',
				format : function(data) {
					p = {
						text : data.kbi_create_datetime.substring(0, 10)
					}
					return $('#text-template').render(p);
				}
			},
			{
				sortable : true,
				name : 'kbi_approve_usr_display_bil',
				display : fetchLabel('lab_kb_app_person'),
				align : 'center',
				width : '12%',
				format : function(data) {
					p = {
						text : data.kbi_approve_usr_display_bil ? data.kbi_approve_usr_display_bil
								: '--'
					}
					return $('#text-template').render(p);
				}
			},
			{
				sortable : true,
				name : 'kbi_approve_datetime',
				display : fetchLabel('lab_kb_app_time'),
				align : 'center',
				width : '11%',
				format : function(data) {
					p = {
						text : data.kbi_approve_datetime ? data.kbi_approve_datetime
								.substring(0, 10)
								: '--'
					}
					return $('#text-template').render(p);
				}
			}, {
				display : fetchLabel('lab_kb_opera'),
				width : '16%',
				align : 'right',
				format : function(data) {
					p = {
						kbi_id : data.kbi_id,
						kbi_status : data.kbi_status
					}
					return $('#text-operate-template').render(p);
				}
			} ];

	function reloadTable(status) {
		$("#knowledge-list").html('');
	    $('#APPROVED').removeAttr("onclick");
		$('#REAPPROVAL').removeAttr("onclick");
		$('#PENDING').removeAttr("onclick");  
		
		var model = knowledgeModel;
		if (status == 'APPROVED') {
			model = dtModel;
		} else if (status == 'REAPPROVAL') {
			model = oprateModel;
		}
		dt = $("#knowledge-list").table({
			url : '${ctx}/app/kb/center/index/my_share',
			colModel : model,
			rp : 10,
			hideHeader : false,
			usepager : true,
			params : {
				kbi_app_status : status
			},
			onSuccess : function(data){
				remove_active(status);
			    $('#APPROVED').attr("onclick","reloadTable('APPROVED')");
				$('#REAPPROVAL').attr("onclick","reloadTable('REAPPROVAL')");
				$('#PENDING').attr("onclick","reloadTable('PENDING')");  
			}
		});
	};

	function remove_active(status){
		if (status == 'APPROVED') {
			$('#REAPPROVAL').removeAttr("class");
			$('#PENDING').removeAttr("class");
			$('#APPROVED').attr("class","active");
		} else if (status == 'REAPPROVAL') {
			$('#REAPPROVAL').attr("class","active");
			$('#PENDING').removeAttr("class");
			$('#APPROVED').removeAttr("class");
		}else{
			$('#REAPPROVAL').removeAttr("class");
			$('#PENDING').attr("class","active");
			$('#APPROVED').removeAttr("class");
		}
	}
	
	function operat(type, kbi_id) {
		if (type == 'delete') {
			if (confirm(fetchLabel('lab_kb_confirm'))) {
				$.ajax({
					url : "${ctx}/app/kb/admin/delete?kbi_id=" + kbi_id,
					success : function(data) {
						reloadTable('REAPPROVAL');
					}
				});
			}
		}
		return false;
	};
</script>
<script id="blank-template" type="text/x-jsrender">
<div class="losedata"><i class="fa fa-folder-open-o"></i><p><lb:get key="lab_table_empty"/></p></div>
</script>
<script id="text-template" type="text/x-jsrender">
{{>text}}
</script>
<script id="a-template" type="text/x-jsrender">
<a class="toolcolor"  href="${ctx}/app/kb/center/view?source=list&enc_kbi_id={{>enc_kbi_id}}&tab={{>tab}}" title="{{>title_all}}">{{>text}}</a>
</script>
<!-- 操作模版 -->
<script id="text-operate-template" type="text/x-jsrender">
<button type="button" class="btn wzb-btn-orange" onclick="javascript:go('${ctx}/app/kb/center/insert?source=list&tab=REAPPROVAL&kbi_id={{>kbi_id}}');">
	<lb:get key='lab_kb_update' />
</button>
<button type="button" class="btn wzb-btn-orange" onclick="operat('delete',{{>kbi_id}})">
	<lb:get key='lab_kb_delete' />
</button>
</script>
<script id="update-operate-template" type="text/x-jsrender">
<button type="button" class="btn wzb-btn-orange" onclick="javascript:go('${ctx}/app/kb/center/insert?source=list&tab=PENDING&kbi_id={{>kbi_id}}');">
	<lb:get key='lab_kb_update' />
</button>
</script>
</head>
<body>
	<div class="xyd-wrapper">
		<div id="main" class="xyd-main clearfix">
			<div class="wzb-model-10">
					<div class="wbtabinfo wzb-tab-3">
						<ul class="nav nav-tabs" role="tablist">
							<li role="presentation" id="APPROVED" class="active" onclick="reloadTable('APPROVED')"><a href="#waiting" aria-controls="waiting" role="tab" data-toggle="tab" style="padding-left:0;"><lb:get key="lab_APP_STATUS_APPROVED" /></a></li>
							<li role="presentation" id="PENDING" onclick="reloadTable('PENDING')"><a href="#passing" aria-controls="passing" role="tab" data-toggle="tab"><lb:get key="subordinate_pending" /></a></li>
							<li role="presentation" id="REAPPROVAL" onclick="reloadTable('REAPPROVAL')"><a href="#refusal" aria-controls="refusal" role="tab" data-toggle="tab"><lb:get key="lab_APP_STATUS_REAPPROVAL" /></a></li>
						</ul>
					</div>
					<div id="knowledge-list" class="wbtable"></div>
			</div>
		</div>
		<!-- xyd-wrapper End-->
	</div>
</body>
</html>