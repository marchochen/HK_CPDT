<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<%@ include file="../../common/template.jsp"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet"
	href="${ctx}/static/js/jquery-table/css/jquery-table-admin.css" />
<title></title>
<script >
	var showtab = cwn.getUrlParam('showtab');
	var userEntId = ${userEntId};
	var isView = ${isView};
</script>

</head>
<body>
	<input type="hidden" name="belong_module" value="FTN_AMD_SNS_MGT" />

	<title:get function="global.FTN_AMD_SNS_MGT" />

	<ol class="breadcrumb wzb-breadcrumb">
		<li><a href="javascript:wb_utils_gen_home(true);"><i
				class="fa wzb-breadcrumb-home fa-home"></i>
			<lb:get key="global.lab_menu_started" /></a></li>
		<li class="active">
			<c:choose>
				<c:when test="${isView == true }">
					<lb:get key="label_cm.label_core_community_management_209" />
				</c:when>
				<c:otherwise>
					<lb:get key="label_cm.label_core_community_management_60" />
				</c:otherwise>
			</c:choose>
		</li>
	</ol>
	<!-- wzb-breadcrumb End-->

	<div class="panel wzb-panel">
		<div class="panel-heading">
			<lb:get key="label_cm.label_core_community_management_60" />
		</div>

		<div class="panel-body">
			<form class="form-search" onsubmit="return false;">
				<input type="text" name="searchContent" class="form-control"
					placeholder="<lb:get key='search_type_qunzu'/>"><input
					type="button" class="form-submit" onclick="searchGroup()" value="">
				<button type="button" id="addgroup" class="btn wzb-btn-yellow">
					<lb:get key="label_cm.label_core_community_management_61" />
				</button>
			</form>

			<ul class="nav nav-tabs page-tabs" role="tablist">
				<li role="presentation" class="active"><a
					aria-controls="mygroup" role="tab" data-toggle="tab"
					href="#mygroup"><lb:get key="group_my" /></a></li>
				<li role="presentation"><a aria-controls="joingroup" role="tab"
					data-toggle="tab" href="#joingroup"><lb:get key="group_find" /></a></li>
				<li role="presentation"><a aria-controls="browseropengroup"
					role="tab" data-toggle="tab" href="#browseropengroup"><lb:get
							key="group_openMenu" /></a></li>
			</ul>


			<!-- Tab panes -->
			<div class="tab-content">
				<div role="tabpanel" class="tab-pane active" id="mygroup">
					<div class="wzb-model-1">
						<span id="grouplist"> </span>
						<!--
						我的群组 
						 -->	
					</div>
				</div>

				<div role="tabpanel" class="tab-pane" id="joingroup">
					<div class="wzb-model-1">
						<span id="joingrouplist"> </span>
				
					</div>
				</div>
				
				<div role="tabpanel" class="tab-pane " id="browseropengroup">
					<div class="wzb-model-1">
		 					<span id="opengrouplist"> </span>
						<!--
						我的群组 
						 -->	
					</div>
				</div>

			</div>
	
		</div>


		

	</div>
	<!-- wzb-panel End-->
	<script type="text/javascript"
		src="${ctx}/static/js/i18n/${lang}/label_cm_${lang}.js"></script>
	<script type="text/javascript"
		src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
	<script type="text/javascript" src="${ctx}/static/admin/js/group.js"></script>
	<script type="text/javascript">
	

	function applyJoinGroup(thisObj, s_grp_id){
		$.ajax({
			url : '/app/group/applyJoinGroup/' + s_grp_id,
			type : 'POST',
			dataType : 'json',
			success : function(data){
				
				if(data != null && data.message != null)
				{
					if(data.message=='')
					{
						Dialog.alert(fetchLabel('label_core_community_management_148'));
					}
					else
					{
						Dialog.alert(fetchLabel('label_core_community_management_147'));
					}
				
					$(thisObj).css("background-color","#999");
					$(thisObj).attr("disabled",true);
				}
				

			}
		});
	}
	</script>
	
	<script id="text-operate-template" type="text/x-jsrender">



<button type="button" class="btn wzb-btn-blue " onclick="javascript:go('${ctx}/app/admin/group/detail/{{:s_grp_id}}?tab=4');">
    <lb:get key='global.button_update' />
</button>
<button type="button" class="btn wzb-btn-blue " onclick="del({{:s_grp_id}})">
    <lb:get key='global.button_del' />
</button>
</script>
<script id="button-template" type="text/x-jsrender">
<input type="button" class="btn wzb-btn-orange" onclick="{{>event}}" value='<lb:get key="group_join"/>' {{if applyInd == true}}style="background-color:#999;" disabled="disabled"{{/if}}/>
</script>

</body>
</html>