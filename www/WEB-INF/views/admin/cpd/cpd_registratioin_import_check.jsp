<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<%@ include file="../../common/template.jsp"%>
<!DOCTYPE html>
<html>
<head>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_cpd_${lang}.js"></script>
<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-admin.css" />



</head>
<body>
<input type="hidden" name="belong_module" value="FTN_AMD_CPT_D_MGT"/>

	 <title:get function="global.FTN_AMD_CPT_D_MGT"/>
    <ol class="breadcrumb wzb-breadcrumb">
        <li><a href="javascript:wb_utils_gen_home(true);"><i class="fa wzb-breadcrumb-home fa-home"></i><lb:get key="global.lab_menu_started" /></a></li>
        <li><a href="/app/admin/cpdRegistrationMgt/list"><lb:get key="global.FTN_AMD_CPT_D_LICENSE_LIST" /></a></li>
        <li class="active"><lb:get key="label_cpd.label_core_cpt_d_management_269"/></li>
    </ol>
    <!-- wzb-breadcrumb End-->

<div class="panel wzb-panel textleft">
	<div class="panel-heading">用户信息导入和导出记录<lb:get key=""/></div>

	<div class="panel-body">

		<form name="frmXml">
			<input value="FTN_AMD_USR_INFO_MGT" name="belong_module" type="hidden" />
			<input value="FTN_AMD_USR_INFO" name="parent_code" type="hidden" />
			<table width="984" cellspacing="0" cellpadding="2" border="0">
				<tbody>
					<tr>
						<td style="padding: 2px;">
							<div class="wb-ui-title-wihe">
								<div style="display: none;"
									class="wzb-title-11 wzb-banner-bg14 work_input_desc wzb-module-title"
									align="left">用户信息导入和导出记录<lb:get key=""/></div>
							</div>
						</td>
					</tr>
				</tbody>
			</table>
			<div class="clear"></div>
			<table>
				<tbody>
					<tr>
						<td width="14"></td>
						<td width="978">
							<div class="NavLink" style="display: none;">
								<span class="NavLink">
									<a href="javascript:Batch.User.Import.prep()">导入用户信息<lb:get key=""/></a>
								</span>
								&gt;
								<span class="NavLink">查看记录<lb:get key="label_cpd."/></span>
							</div>
						</td>
					</tr>
				</tbody>
			</table>
			<table class="wzb-ui-desc">
				<tbody>
					<tr>
						<td>
							<div class="wzb-ui-module-text">
								<lb:get key="label_cpd.label_core_cpt_d_management_257"/>：
								<br />
								&nbsp;
								<lb:get key="label_cpd.label_core_cpt_d_management_258"/>
								<br />
								&nbsp;
								<lb:get key="label_cpd.label_core_cpt_d_management_259"/>
								<br />
							</div>
						</td>
					</tr>
				</tbody>
			</table>
			<table class="tabel wzb-ui-table">
				<tbody>
					<tr class="wzb-ui-table-head">
						<td width="15%"><lb:get key="label_cpd.label_core_cpt_d_management_260"/></td>
						<td style="padding-right:-20px;" width="10%"><lb:get key="label_cpd.label_core_cpt_d_management_261"/></td>
						<td width="25%" align="left"><lb:get key="label_cpd.label_core_cpt_d_management_263"/></td>
						<td width="27%" align="left"><lb:get key="label_cpd.label_core_cpt_d_management_264"/></td>
						<td width="15%" align="left"><lb:get key="label_cpd.label_core_cpt_d_management_265"/></td>
						<td width="8%" align="right"><lb:get key="label_cpd.label_core_cpt_d_management_266"/></td>
					</tr>
					<c:forEach items="${dms }" var="d">
						<tr>
							<td style="width:10%">${d.ilg_create_timestamp}</td>
							<td style="algin:left;"><lb:get key="label_core_cpt_d_management_262"/></td>
							<td>
								<a target="_blank"
									href="../../../log/cpd/${d.ilg_id}/${d.ilg_filename}"
									class="font gray4e">${d.ilg_filename}</a>
							</td>
							<td></td>
							<td>Administrator</td>
							<td>
							<c:if test="${d.success_txt!='' }">
                                <a title="显示成功导入的记录的细节" target="_blank" href="${d.success_txt}">success.txt</a>
                            </c:if>
                            <c:if test="${d.failure_txt!='' }">
                                <a title="显示成功导入的记录的细节" target="_blank" href="${d.failure_txt}">failure.txt</a>
                            </c:if>
								<%-- <a title="显示成功导入的记录的细节" target="_blank" href="../../../log/cpd/${d.ilg_id}/success.txt">success.txt</a>
								<a title="显示失败导入的记录的细节" target="_blank" href="../../../log/cpd/${d.ilg_id}/failure.txt">failure.txt</a> --%>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<div class="">
				<table id="wzb-ui-table-pager">
					<tbody>
						<tr>
							<td colspan="4">
								<div
									style=";border-top: 1px solid #EEE;verflow: hidden;text-align: center;padding: 5px 0;">
									<p style="padding: 4px;display: inline-block;margin: 0;"><lb:get key="label_cpd.label_core_cpt_d_management_267"/>${fn:length(dms)}<lb:get key="label_cpd.label_core_cpt_d_management_268"/></p>
								</div>
							</td>
						</tr>
					</tbody>
				</table>
				<script type="text/javascript" language="JavaScript">
					$(function(){
						//由于XSL中数据表格是在页面中手动写的，现在想做到表格最后一行的border去掉，为了避免每个XSL都要写，所以加了这段脚本
						var $_dataTable = $("#wzb-ui-table-pager").prev();
						$_dataTable.find("tr:last").children().css("border-bottom","none");
						$_dataTable.addClass("margin-top28");
					});
				</script>
			</div>
		</form>

	</div>
</div> 

</body>
</html>