<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<%@ include file="../../common/template.jsp"%>
<!DOCTYPE html>
<html>
<head>

<script type="text/javascript" src="${ctx}/static/js/layer/layer.js"></script> 
<script type="text/javascript" src="${ctx}/js/datepicker/laydate/laydate.js"></script>
<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-admin.css" />

<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_cpd_${lang}.js"></script>
<script type="text/javascript" src="${ctx}/static/js/cwn_utils.js"></script>
<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<script type="text/javascript">

function valForm(){
	var outstandingRemark = $("#outstandingRemark").val();
	var individualRemark = $("#individualRemark").val();
	var awardedRemark = $("#awardedRemark").val();
	var outstandingRemark = $("#outstandingRemark").val();
	
	if(outstandingRemark!=''){
		if(getChars(outstandingRemark)>2000){
			$("#outstandingRemark").focus();
			 Dialog.alert(fetchLabel("label_core_cpt_d_management_180"));
			 return false;
		}
	}
	if(individualRemark!=''){
		if(getChars(individualRemark)>2000){
			$("#individualRemark").focus();
			 Dialog.alert(fetchLabel("label_core_cpt_d_management_180"));
			 return false;
		}
	}
	if(awardedRemark!=''){
		if(getChars(awardedRemark)>2000){
			$("#awardedRemark").focus();
			 Dialog.alert(fetchLabel("label_core_cpt_d_management_180"));
			 return false;
		}
	}
	if(licenseRegRemark!=''){
		if(getChars(licenseRegRemark)>2000){
			$("#licenseRegRemark").focus();
			 Dialog.alert(fetchLabel("label_core_cpt_d_management_180"));
			 return false;
		}
	}
	return true;
	
}

</script>

<style type="text/css">
  textarea {
  	//resize: none;
  	width: 400px;
	height: 200px;
	//max-width: 400px;
	//max-height: 200px;
	display:block;
	margin:0 auto;
  }
  .text-algin{
  	text-align: center;
  }
</style>
</head>
<body>
<input type="hidden" name="belong_module" value="FTN_AMD_CPT_D_MGT"/>

	 <title:get function="global.FTN_AMD_CPT_D_MGT"/>
    <ol class="breadcrumb wzb-breadcrumb">
        <li><a href="javascript:wb_utils_gen_home(true);"><i class="fa wzb-breadcrumb-home fa-home"></i><lb:get key="global.lab_menu_started" /></a></li>
        <li class="active"><lb:get key="global.FTN_AMD_CPT_D_NOTE" /></li>
    </ol>
    <!-- wzb-breadcrumb End-->

    <div class="panel wzb-panel">
		<form action="${ctx}/app/admin/cpdReportRemark/save" method="post" onsubmit="return valForm();">
        <div class="panel-body">
			<table>
				<tr>
					<div class='text-algin' ><lb:get key="label_cpd.label_core_cpt_d_management_127" />&nbsp;<lb:get key="label_cpd.label_core_cpt_d_management_179" /></div>
					<div>
						<textarea id="outstandingRemark" name="outstandingRemark" rows="" cols="">${outstanding_remark}</textarea>
					</div>
				</tr>
				<tr>
					<div class='text-algin'><lb:get key="label_cpd.label_core_cpt_d_management_177" />&nbsp;<lb:get key="label_cpd.label_core_cpt_d_management_179" /></div>
					<div>
						<textarea id="individualRemark" name="individualRemark" rows="" cols="">${individual_remark}</textarea>
					</div>
				</tr>
				<tr>
					<div class='text-algin'><lb:get key="label_cpd.label_core_cpt_d_management_149" />&nbsp;<lb:get key="label_cpd.label_core_cpt_d_management_179" /></div>
					<div>
						<textarea id="awardedRemark" name="awardedRemark" rows="" cols="" >${awarded_remark}</textarea>
					</div>
				</tr>
				<tr>
					<div class='text-algin'><lb:get key="label_cpd.label_core_cpt_d_management_163" />&nbsp;<lb:get key="label_cpd.label_core_cpt_d_management_179" /></div>
					<div>
						<textarea class='' id="licenseRegRemark" name="licenseRegRemark" rows="" cols="" >${license_registration}</textarea>
					</div>
				</tr>
			</table>
			<div class='text-algin'>
			<button type="submit"  class="btn wzb-btn-blue wzb-btn-big">
				<lb:get key="button_save" />
			</button>
			</div>
        </div>
        </form>
    </div>


</body>
</html>