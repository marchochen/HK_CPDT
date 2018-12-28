<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<%@ include file="../../common/template.jsp"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-admin.css" />
<title></title>
</head>
<body>
<input type="hidden" name="belong_module" value="FTN_AMD_SYSTEM_SETTING_MGT"/>


  <title:get function="global.FTN_AMD_SYSTEM_SETTING_MGT"/>

<ol class="breadcrumb wzb-breadcrumb">
  <li><a href="javascript:wb_utils_gen_home(true);"><i class="fa wzb-breadcrumb-home fa-home"></i><lb:get key="global.lab_menu_started" />
</a></li>
  <li class="active"><lb:get key="label_ss.label_core_system_setting_1"/></li>
</ol> <!-- wzb-breadcrumb End-->

<div class="panel wzb-panel">
	<div class="panel-heading"><lb:get key="label_ss.label_core_system_setting_1"/></div>
	
	<div class="panel-body">
		<form class="form-search">
		      <button type="button" class="btn wzb-btn-yellow" onclick="javascript:go('${ctx}/app/admin/role/addRole?role_id=0')">
		      	<lb:get key="label_ss.label_core_system_setting_3"/>
		      </button>
		      <button type="button" class="btn wzb-btn-yellow" onclick="delById();">
		      	<lb:get key="label_ss.label_core_system_setting_4"/>
		      </button>
		</form>
		<div id="rolelist"></div>
	</div>
</div>  <!-- wzb-panel End-->
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_ss_${lang}.js"></script>
<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<script type="text/javascript" src="${ctx}/static/admin/js/role.js"></script>
<script id="text-operate-template" type="text/x-jsrender">
 <span id="role_title_{{>id}}">{{:text}}</span>
</script>

<!-- 角色列表的checkbox，如果是系统管理员，则不显示，如果不是系统管理员，但是被用户关联到，则把checkbox disabled -->
<script id="input-checkbox-template" type="text/x-jsrender">
	<div style="display: inline-block;">
		<input  id="role_{{>id}}" type="checkbox" {{if type == 'SYSTEM'}}style="display:none"{{/if}} {{if type != 'SYSTEM' && isRefByUser}}disabled="disabled"{{/if}} name="rol_checkbox" value="{{>id}}" />		
	</div>
	<span id="role_title_{{>id}}">{{:text}}</span>
</script>
<script id="input-button-template" type="text/x-jsrender">
{{if ext_id !='NLRN_1' }}
	<div style="margin-bottom:6px" align="right">
		<button type="button" class="btn wzb-btn-blue" onclick="javascript:go('${ctx}/app/admin/role/update?role_id={{>id}}')">
			<lb:get key="label_ss.label_core_system_setting_5"/>
		</div>
	</div>
{{/if}}
</script>
</body>
</html>