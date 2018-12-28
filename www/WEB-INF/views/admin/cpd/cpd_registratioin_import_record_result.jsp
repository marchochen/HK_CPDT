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

<div class="wzb-main">

	 <title:get function="global.FTN_AMD_CPT_D_MGT"/>
    <ol class="breadcrumb wzb-breadcrumb">
        <li><a href="javascript:wb_utils_gen_home(true);"><i class="fa wzb-breadcrumb-home fa-home"></i><lb:get key="global.lab_menu_started" /></a></li>
        <li><a href="/app/admin/cpdRegistrationMgt/list"><lb:get key="global.FTN_AMD_CPT_D_LICENSE_LIST" /></a></li>
        <li class="active"><lb:get key="label_cpd.label_core_cpt_d_management_269"/></li>
    </ol>
	<!-- wzb-breadcrumb End -->

	<div class="panel wzb-panel textleft">
		<div class="panel-heading">导入用户信息 - 结果<lb:get key=""/></div>

		<div class="panel-body">

			<form name="frmXml" id="frmXml">
				<input value="FTN_AMD_USR_INFO_MGT" name="belong_module" type="hidden" />
				<input value="FTN_AMD_USR_INFO" name="parent_code" type="hidden" />
				<table width="984" cellspacing="0" cellpadding="2" border="0">
					<tbody>
						<tr>
							<td style="padding: 2px;">
								<div class="wb-ui-title-wihe">
									<div style="display: none;"
										class="wzb-title-11 wzb-banner-bg14 work_input_desc wzb-module-title"
										align="left">导入用户信息 - 结果<lb:get key=""/></div>
								</div>
							</td>
						</tr>
					</tbody>
				</table>
				<div class="clear"></div>
				<div style="margin-bottom:10px;width: 100%; height: 1px;" class="wzb-ui-line"></div>
				<table class="Bg" width="984" cellspacing="0" cellpadding="3"
					border="0">
					<tbody>
						<tr>
							<td colspan="2" style="padding: 3px;" height="10">
							</td>
						</tr>
						<tr>
							<td colspan="2" style="padding: 3px;">
							<table cellpadding="3" cellspacing="0" border="0" width="100%">
                            <tr>
                                <td width="30%" align="center"><lb:get key="label_core_cpt_d_management_278" /> : ${success_total}</td>
                                <td width="70%">
                                <a class="font  gray4e" href="${success_href}"  target="_blank"><lb:get key="label_core_cpt_d_management_279" /></a>
                                </td>
                             </tr>
                            </table>
							</td>
						</tr>
						<tr>
							<td colspan="3" style="padding: 3px;" height="10">
							</td>
						</tr>
					</tbody>
				</table>
				<div style="margin-bottom:10px;width: 100%; height: 1px;" class="wzb-ui-line"></div>
				<table width="984" cellspacing="0" cellpadding="3" border="0">
					<tbody>
						<tr>
							<td style="padding: 3px;" align="center">
								<input style="" name="frmSubmitBtn" value="<lb:get key="label_cpd.label_core_cpt_d_management_256"/>"
									class="btn wzb-btn-blue margin-right10 wzb-btn-big" onclick="javascript:cpdAwardImportExec('${lang}')"
									type="button"></input>
							</td>
						</tr>
					</tbody>
				</table>
			</form>

		</div>
	</div>
	<!-- wzb-panel End -->

</div>
<script type="text/javascript">
function cpdAwardImportExec(lang){
        document.getElementById("frmXml").action= wb_utils_controller_base +"admin/cpdRegistrationMgt/importCPDRegistration";
        document.getElementById("frmXml").submit();
}
//该页面屏蔽语言筛选
function changeLang(lang){};
</script>
</body>
</html>