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
        <li class="active"><a href="javascript:cpdAwardCancel();"><lb:get key="global.FTN_AMD_CPT_D_IMPORT_AWARDED_HOURS" /></a> </li>
        <li class="active">   <lb:get key="label_cpd.label_core_cpt_d_management_276" /></li>
    </ol>
    <!-- wzb-breadcrumb End-->

    <div class="panel wzb-panel">
    <form name="frmXml"  id="frmXml"  method="post">
    <input  name="filePath" value="${filePath}"  type="hidden"  />
    <input  name="upload_desc" value="${upload_desc}"  type="hidden"  />
    </form>
        <div class="panel-heading"><lb:get key="label_cpd.label_core_cpt_d_management_2" /></div>

        <div class="panel-body">
        <table width="{$wb_gen_table_width}" style="margin-top: 20px;" border="0" cellspacing="0" cellpadding="3" class="Bg">
                    <tr>
                        <td width="20">
                        </td>
                        <td>
				        <table cellpadding="3" cellspacing="0" border="0" width="100%">
				            <tr>
				                <td width="30%" align="center"><lb:get key="label_core_cpt_d_management_278" /> : ${success_total}</td>
				                <td width="70%">
				                <a class="font  gray4e" href="${success_href}"  target="_blank"><lb:get key="label_core_cpt_d_management_279" /></a>
				                   <%--  <span class="Text">
				                     <lb:get key="label_core_cpt_d_management_253" />    <br/>
									 <lb:get key="label_core_cpt_d_management_254" />  <br/>
									 <lb:get key="label_core_cpt_d_management_255" />    <br/>
				                    </span> --%>
				                </td>
				            </tr>
				        </table>
                        </td>
                    </tr>
                    <tr>
                        <td>
                        </td>
                        <td>
                            <table cellpadding="3" cellspacing="0" border="0" width="100%">
                                <tr>
                                    <td>
                                        <span class="Text">
                                        </span>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>       
        
        <div class="wzb-bar">
                <input type="button" onclick="cpdAwardCancel()" class="btn wzb-btn-blue margin-right10 wzb-btn-big" value="<lb:get key="label_core_cpt_d_management_256" />"> 
        </div>

        </div>
    </div>
    <!-- wzb-panel End-->




<script type="text/javascript">
function cpdAwardImport(){
        document.getElementById("frmXml").action= wb_utils_controller_base +"admin/cpdtImportAwardedHours/comfirmUpload";
        document.getElementById("frmXml").submit();
}

function cpdAwardCancel(){
    url = wb_utils_controller_base + 'admin/cpdtImportAwardedHours/toCpdtHoursAwaededImport';
    window.location.href =  url;
}


</script>
</body>
</html>