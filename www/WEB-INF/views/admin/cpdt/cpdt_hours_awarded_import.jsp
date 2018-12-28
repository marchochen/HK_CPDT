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
        <li class="active"><lb:get key="global.FTN_AMD_CPT_D_IMPORT_AWARDED_HOURS" /></li>
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
                <input type="button" onclick="cpdHoursAwardedImportGetTpl('${lang}')" class="btn wzb-btn-orange margin-right4" value="<lb:get key="label_core_cpt_d_management_234" />"> 
                <input type="button" onclick="cpdHoursAwardedImportGetInstr()"  class="btn wzb-btn-orange margin-right4" value="<lb:get key="label_core_cpt_d_management_235" />"> 
                <input type="button"  onclick="cpdHoursAwardedImportLog()" class="btn wzb-btn-orange margin-right4" value="<lb:get key="label_core_cpt_d_management_236" />"> 
                </td>
            </tr>

        </table>

        <form  name="frmXml"  id="frmXml"  enctype="multipart/form-data" method="POST">
        <table>
            <tr>
                <td class="wzb-form-label" align="right" valign="top">
                    <span class="wzb-form-star">* </span><lb:get key="label_core_cpt_d_management_270" />：
                </td>
                <td class="wzb-form-control">
                     <div class="file"  >
                        <input id="src_filename"  name="src_filename"  value="{$value}" onclick="{$onclick}" onBlur="{$onBlur}" onfocus="{$onfocus}"  class="file_file" name="{$name}" type="file" onchange="$(this).siblings('.file_txt').val(this.value);{$onchange}"/>
                        <input  class="file_txt" value="<lb:get key="label_core_cpt_d_management_237"/>"/>
                        <div id="file_button" class="file_button-blue" style="height:28px"><lb:get key="label_core_cpt_d_management_238" /></div>
                    </div> 
                </td>
            </tr>
            <tr>
                <td></td>
                <td class="wzb-form-control">
                    <span class="wzb-ui-desc-text" >
	                    <c:if test="${lang=='zh-cn'}">
	                        上载文件内的记录数目上限为 ${maxUploadCount} 。如要上载更多的记录，请分批上载。
	                    </c:if>
	                    <c:if test="${lang=='zh-hk'}">
	                        上載文檔內的記錄數目上限為${maxUploadCount}。如要上載更多的記錄，請分批上載
	                    </c:if>
	                    <c:if test="${lang=='en-us'}">
	                        Maximum number of records allowed in the file is ${maxUploadCount}. Please upload in separate batches if more records are intended.
	                    </c:if>
                       <%--<lb:get key="label_core_cpt_d_management_239" />--%>
                       <br/>
                       <lb:get key="label_core_cpt_d_management_240" />
                    </span>
                 </td>
            </tr>
            <tr>
                <td align="right" valign="top" class="wzb-form-label">
                    <lb:get key="label_core_cpt_d_management_241" />：
                </td>
                <td class="wzb-form-control">
                    <textarea class="wzb-inputTextArea" name="upload_desc"  id="upload_desc" style="width:300px;" rows="4"></textarea>
                    <br/>
                    <span class="wzb-ui-desc-text">
                       <lb:get key="label_core_cpt_d_management_242" />
                    </span>
                </td>
            </tr>
            <tr>
                <td class="wzb-form-label" align="right">
                </td>
                <td class="wzb-ui-module-text">
                    <span class="wzb-form-star">*</span><lb:get key="label_core_cpt_d_management_243" />
                </td>
            </tr>
        </table>
        </form>
        <div class="wzb-bar">
                <input type="button" onclick="cpdAwardImportExec('${lang}')" class="btn wzb-btn-blue margin-right10 wzb-btn-big" value="<lb:get key="label_core_cpt_d_management_244" />"> 
                <%-- <input type="button" class="btn wzb-btn-blue margin-right10 wzb-btn-big" value="<lb:get key="label_core_cpt_d_management_245" />">  --%>
        </div>

        </div>
    </div>
    <!-- wzb-panel End-->




<script type="text/javascript">

$(function(){
	/* 修改id为src_filename的title文字 */
	var $values = $("#file_button").text();
	if($values == "browse"){
		$("#src_filename").attr("title","No file was selected");
	}else if($values == "浏览"){
		$("#src_filename").attr("title","未选择任何文件");
	}else{
		$("#src_filename").attr("title","未選擇任何文件");
	}
	
});

function cpdHoursAwardedImportGetTpl(lang){
    url = '../../../htm/import_template/import_cptd_awarded_hours_template.xls';
    wbUtilsOpenWin(url, '');
}

function cpdHoursAwardedImportGetInstr(){
    url = wb_utils_controller_base + 'admin/cpdtImportAwardedHours/toCpdtHoursAwaededImportInstr?isExcludes=true';
    wbUtilsOpenWin(url, '');
}

function cpdHoursAwardedImportLog(){
    url = wb_utils_controller_base + 'admin/cpdtImportAwardedHours/cpdtHoursAwaedImportLog';
    //url = wb_utils_invoke_disp_servlet('cmd','get_log_history','module','upload.UploadModule','log_type','CPDAWARDHOURS','stylesheet','bp_cpd_hours_log_get_log.xsl')
	window.location.href = url;
	
    //url = wb_utils_controller_base + 'admin/cpdImportAwardedHours/cpdHoursAwaedImportLog';
    //window.location.href=url;
}


function cpdAwardImportExec(lang){
     _txtFileName = $('#src_filename').val();
     _upload_desc = $('#upload_desc').val();
     if(lang=='zh-cn'){
         lang="gb";
     }else if(lang=='en-us'){
         lang="en";
     }else if(lang=='zh-hk'){
         lang="ch";
     }
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
        document.getElementById("frmXml").action= wb_utils_controller_base +"admin/cpdtImportAwardedHours/filesUpload";
        document.getElementById("frmXml").submit();
    }
}


</script>
</body>
</html>