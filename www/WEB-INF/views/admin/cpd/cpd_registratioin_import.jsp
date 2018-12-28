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

    <div class="panel wzb-panel">
        <div class="panel-heading"><lb:get key="label_cpd.label_core_cpt_d_management_2" /></div>

        <div class="panel-body">
            <table>
            <tr>
                <td height="10" colspan="2">
                </td>
            </tr>
            <tr>
                <td width="60%">
                </td>
                <td height="10" align="right">
                <input type="button" class="btn wzb-btn-orange margin-right4" value="<lb:get key="label_cpd.label_core_cpt_d_management_234" />" onclick="cpdImportFile('${lang}','cw')"></input> 
                <input type="button" class="btn wzb-btn-orange margin-right4" value="<lb:get key="label_cpd.label_core_cpt_d_management_235" />" onclick="cpdImportModel('${lang}','cw')"></input>
                <input type="button" class="btn wzb-btn-orange margin-right4" value="<lb:get key="label_cpd.label_core_cpt_d_management_236" />" onclick="cpdImportCheck('${lang}','cw')"></input>
                </td>
            </tr>

        </table>

        <form  name="frmXml"  id="frmXml"  enctype="multipart/form-data" method="POST">
        <table>
            <tr>
                <td class="wzb-form-label" align="right" valign="top">
                    <span class="wzb-form-star">* </span><lb:get key="label_cpd.label_core_cpt_d_management_270"/>：
                </td>
                <td class="wzb-form-control">
                     <div class="file"  >
                        <input id="src_filename"  name="src_filename"  value="" title=""   class="file_file" type="file" onchange="$(this).siblings('.file_txt').val(this.value);"/>
                        <input  class="file_txt" value="<lb:get key="label_cpd.label_core_cpt_d_management_237" />"/>
                        <div class="file_button-blue"><lb:get key="label_cpd.label_core_cpt_d_management_238"/> </div>
                    </div> 
                </td>
            </tr>
            <tr>
                <td></td>
                <td class="wzb-form-control">
                    <span class="wzb-ui-desc-text" >
                       <lb:get key="label_cpd.label_core_cpt_d_management_239"/>
                       <br/>
                        <lb:get key="label_cpd.label_core_cpt_d_management_240"/>
                    </span>
                 </td>
            </tr>
            <tr>
                <td align="right" valign="top" class="wzb-form-label">
                    <lb:get key="label_cpd.label_core_cpt_d_management_241"/>：
                </td>
                <td class="wzb-form-control">
                    <textarea class="wzb-inputTextArea" name="upload_desc"  id="upload_desc" style="width:300px;" rows="4"></textarea>
                    <br/>
                    <span class="wzb-ui-desc-text">
                        <lb:get key="label_cpd.label_core_cpt_d_management_242"/>
                    </span>
                </td>
            </tr>
            <tr>
                <td class="wzb-form-label" align="right">
                </td>
                <td class="wzb-ui-module-text">
                    <span class="wzb-form-star">*</span><lb:get key="label_cpd.label_core_cpt_d_management_243"/>
                </td>
            </tr>
        </table>
        </form>
        <div class="wzb-bar">
                <input type="button" onclick="cpdAwardImportExec('${lang}')" class="btn wzb-btn-blue margin-right10 wzb-btn-big" value="<lb:get key="label_cpd.label_core_cpt_d_management_244"/>"></input> 
                <input type="button" onclick="cpdAwardImportExec1('${lang}')" class="btn wzb-btn-blue margin-right10 wzb-btn-big" value="<lb:get key="label_cpd.label_core_cpt_d_management_245"/>"></input>
        </div>

        </div>
    </div>
    <!-- wzb-panel End-->




<script type="text/javascript">
function cpdAwardImportExec(lang){
     _txtFileName = $('#src_filename').val();
     _upload_desc = $('#upload_desc').val();
     lang="gb";
    /* _txtFileName = _wbBatchProcessUserImportGetFileName(frm.src_filename_path.value);
    alert(_txtFileName); */
    if (_txtFileName.substring(_txtFileName.lastIndexOf('.')+1).toLowerCase() != 'xls' && _txtFileName.substring(_txtFileName.lastIndexOf('.')+1).toLowerCase() != 'xlsx')  {
        alert(eval('wb_msg_' + lang + '_upload_valid_enro'))
    } else if (_txtFileName.length > 100)   {
        alert(eval('wb_msg_' + lang + '_upload_valid_usr_filename'))
    }else if (getChars(_upload_desc) > 2000)   {
        alert(eval('wb_msg_'+lang+'_desc_too_long'))
        frm.upload_desc.focus()
    }else{
        
        document.getElementById("frmXml").action= wb_utils_controller_base +"admin/cpdRegistrationMgt/filesUpload";
        document.getElementById("frmXml").submit();
    }
}
function cpdImportFile(lang, site){
	url = '../../../htm/import_template/wb_import_cpd_profile_template-' + lang + '.xls';
	wbUtilsOpenWin(url, '');
}
function cpdAwardImportExec1(lang, site){
    document.getElementById("frmXml").action= wb_utils_controller_base +"admin/cpdRegistrationMgt/list";
    document.getElementById("frmXml").submit();
}
function cpdImportModel(lang, site){
	url = wb_utils_controller_base +"admin/cpdRegistrationMgt/upload_model";
	wbUtilsOpenWin(url, '');
}
function cpdImportCheck(lang, site){
    document.getElementById("frmXml").action= wb_utils_controller_base +"admin/cpdRegistrationMgt/upload_check";
    document.getElementById("frmXml").submit();
}


</script>
</body>
</html>