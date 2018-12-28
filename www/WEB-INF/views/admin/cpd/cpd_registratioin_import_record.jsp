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
			<div class="panel-heading">导入用户信息 - 第二步：确认<lb:get key=""/></div>
	
			<div class="panel-body">
	
				<form onsubmit="return status()" name="frmXml" id="frmXml">
					<input name="cmd" type="hidden" />
					<input value="" name="module" type="hidden" />
					<input name="url_success" type="hidden" />
					<input name="url_failure" type="hidden" />
					<input value="" name="stylesheet" type="hidden" />
					<input value="${scrFile}" name="srcFile" type="hidden" />
					<input value="${upload_desc}" name="upload_desc" type="hidden" />
					<input value="FTN_AMD_USR_INFO_MGT" name="belong_module" type="hidden" />
					<input value="FTN_AMD_USR_INFO" name="parent_code" type="hidden" />
					<table width="984" cellspacing="0" cellpadding="2" border="0">
						<tbody>
							<tr>
								<td style="padding: 2px;">
									<div class="wb-ui-title-wihe">
										<div style="display: none;"
											class="wzb-title-11 wzb-banner-bg14 work_input_desc wzb-module-title"
											align="left">导入用户信息 - 第二步：确认<lb:get key=""/></div>
									</div>
								</td>
							</tr>
						</tbody>
					</table>
					<div class="clear"></div>
					<div style="margin-bottom:10px;width: 100%; height: 1px;" class="wzb-ui-line"></div>
					<table width="984" cellspacing="0" cellpadding="3" border="0">
						<tbody>
							<tr>
								<td style="padding: 3px;" width="4">
								</td>
								<td style="padding: 3px;"></td>
								<td rowspan="3" style="padding: 3px;" align="right" height="10">
										<a class="btn wzb-btn-blue margin-right10 wzb-btn-big" href="${down_file }" target="_blank"><lb:get key="label_cpd.label_core_cpt_d_management_247"/></a>
								</td>
							</tr>
							<tr>
								<td style="padding: 3px;" width="4">
								</td>
								<td style="padding: 3px;" height="10"><lb:get key="label_cpd.label_core_cpt_d_management_246"/>：[${totalNumber }]
								</td>
								<td style="padding: 3px;"></td>
							</tr>
							<tr>
								<td style="padding: 3px;" width="4">
								</td>
								<td style="padding: 3px;" height="10"><lb:get key="label_cpd.label_core_cpt_d_management_248"/>：[${successNumber }]
	
								</td>
								<td style="padding: 3px;"></td>
							</tr>
							<tr>
								<td style="padding: 3px;" width="4">
								</td>
								<td style="padding: 3px;" height="10"><lb:get key="label_cpd.label_core_cpt_d_management_249"/>：[${errorNumber }]
								</td>
							</tr>
							<c:forEach items="${errorLog }" var="log">
								<tr>
									<td style="padding: 3px;" width="4">
									</td>
									<td style="padding-left: 33px;" height="10">${log }
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					<div style="margin-bottom:10px;width: 100%; height: 1px;" class="wzb-ui-line"></div>
					<table width="984" cellspacing="0" cellpadding="3" border="0">
						<tbody>
							<tr>
								<td style="padding: 3px;" align="center">
									<input style="" name="frmSubmitBtn" value="<lb:get key="label_cpd.label_core_cpt_d_management_251"/>"
										class="btn wzb-btn-blue margin-right10 wzb-btn-big" onclick="javascript:cpdAwardImportExec1('${lang}')"
										type="button" ></input>
									<c:if test="${fn:length(errorLog) == 0 }">
										<input style="" name="frmSubmitBtn" value="<lb:get key="label_cpd.label_core_cpt_d_management_252"/>"
											class="btn wzb-btn-blue margin-right10 wzb-btn-big"
											onclick="javascript:cpdAwardImportExec('${lang}')"
											type="button"></input>
									</c:if>
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
		function wbBatchProcessUserImportGetSourceFile(url){
			url = wb_utils_controller_base +"admin/cpdRegistrationMgt/down_file";
			wbUtilsOpenWin(url, '');
		}
		function cpdAwardImportExec(lang){
		        document.getElementById("frmXml").action= wb_utils_controller_base +"admin/cpdRegistrationMgt/upload_result";
		        document.getElementById("frmXml").submit();
		}
		function cpdAwardImportExec1(lang){
		    document.getElementById("frmXml").action= wb_utils_controller_base +"admin/cpdRegistrationMgt/importCPDRegistration";
		    document.getElementById("frmXml").submit();
		}
		// 该页面屏蔽语言筛选
		function changeLang(lang){};
	</script>
</body>
</html>