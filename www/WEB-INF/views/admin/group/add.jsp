<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
</head>
<body>
<input type="hidden" name="belong_module" value="FTN_AMD_SNS_MGT"/>


<title:get function="global.FTN_AMD_SNS_MGT"/>	

<ol class="breadcrumb wzb-breadcrumb">
    <li><a href="javascript:wb_utils_gen_home(true);"><i class="fa wzb-breadcrumb-home fa-home"></i><lb:get key="global.lab_menu_started" /></a></li>
  <li><a id="amd_group" href="${ctx }/app/admin/group"><lb:get key="label_cm.label_core_community_management_60"/></a></li>
  <li class="active"><lb:get key="label_cm.label_core_community_management_61"/> </li>
</ol> <!-- wzb-breadcrumb End-->

<div class="panel wzb-panel">
<div class="panel-heading"><lb:get key="label_cm.label_core_community_management_61"/> </div>

<div class="panel-body">
<table>
    <form:form action="${ctx}/app/admin/group/add" method="post" enctype="multipart/form-data" modelAttribute="snsGroup" >

    <tr>
         <td class="wzb-form-label"><span class="wzb-form-star">*</span><lb:get key="label_cm.label_core_community_management_63"/>：</td>
         
         <td class="wzb-form-control">
             <div class="wzb-selector">
             <form:input type="text" cssClass="form-control" path="s_grp_title" id="s_grp_title" prompt=""/><form:errors path="s_grp_title" cssClass="error" />
             </div>
         </td>
    </tr>
    
    <tr>
         <td class="wzb-form-label" valign="top"><lb:get key="label_cm.label_core_community_management_69"/>：</td>
         
         <td class="wzb-form-control">
             <div class="wzb-selector"><form:textarea onkeydown="return noMaxlength(window.event,this,400);" id="s_grp_desc" path="s_grp_desc" value="" prompt="" cssClass="form-control"></form:textarea></div>
         </td>
    </tr>
    
    <tr>
         <td class="wzb-form-label" valign="top"><lb:get key="label_cm.label_core_community_management_70"/>：</td>
         
         <td class="wzb-form-control">
             <dl class="wzb-list-8">
                 <dd>
					<img id="group_image" class="fl psdpic mr10" src="${ctx }/static/images/qunzu.jpg"/>
					<%--  新增时  不显示“保留现有头像”					
					<p class="psda"><label for="psda" class="radiosite"><input type="radio" name="group_card" value="0" onclick="changeGroupCard(this,'')" checked="checked" id="psda"><lb:get key="label_cm.label_core_community_management_71"/></label></p>
				    --%>					
					<p class="psdb"><label for="psdb" class="radiosite"><input type="radio" name="group_card" value="1" onclick="changeGroupCard(this,'')" id="psdb" checked="checked"><lb:get key="label_cm.label_core_community_management_72"/></label></p>
					<p class="psdc"><label for="psdc" class="radiosite"><input type="radio" name="group_card" value="2" onclick="changeGroupCard(this,'')" id="psdc"><lb:get key="label_cm.label_core_community_management_73"/></label></p>
					<div class="file" style="margin-left: 15px;" onclick=" document.getElementById('psdc').checked=true;">  <!-- disabled="true" -->
						<input id="file" class="file_file" name="image" type="file"  onchange="$('#textfield').val(this.value);$('#textfield').attr('title',this.value);"/>
						<input  id="textfield" class="file_txt" value='<lb:get key="label_cm.label_core_community_management_74"/>'/>
						<div class="file_button-blue"><lb:get key="label_cm.label_core_community_management_75"/></div>
					</div>
				  	<form:hidden  path="s_grp_card" />
                 </dd> 
             </dl>      
         </td>
    </tr>
    
    <tr>
         <td class="wzb-form-label" valign="top"><lb:get key="label_cm.label_core_community_management_76"/>：</td>
         
         <td class="wzb-form-control" >
             <label class="wzb-input-label" for="psde"><input type="radio" id="psde" class="s_grp_private" checked="checked" value="0" name="s_grp_private"> <lb:get key="label_cm.label_core_community_management_77"/> </label>
             <c:if test="${hasInstrRole == true }">
             	<label class="wzb-input-label" for="psdh"><input type="radio" id="psdh" class="s_grp_private"  value="3" name="s_grp_private"><lb:get key="label_cm.label_core_community_management_78"/> </label>
             </c:if>
             
             <label class="wzb-input-label" for="psdf"><input type="radio" id="psdf" class="s_grp_private"  value="2" name="s_grp_private"><lb:get key="label_cm.label_core_community_management_182"/></label>
             <label class="wzb-input-label" for="psdg"><input type="radio" id="psdg" class="s_grp_private"  value="1" name="s_grp_private"><lb:get key="label_cm.label_core_community_management_79"/></label>
         </td>
    </tr>
     <tr>
         <td class="wzb-form-label" valign="top"><span class="wzb-form-star">*</span></td>
         <td class="wzb-form-control" >
             	<!-- 为必填 --><lb:get key="label_rm.label_core_requirements_management_35" />
         </td>
    </tr>
     </form:form>
</table>
   
<div class="wzb-bar">
     <input type="button" onclick="javascript:submitForm();" class="btn wzb-btn-blue wzb-btn-big margin-right10 " value="<lb:get key='global.button_ok'/>" name="frmSubmitBtn">
     <input type="button" onclick="javascript:history.go(-1);" class="btn wzb-btn-blue wzb-btn-big " value="<lb:get key='global.button_cancel'/>" name="frmSubmitBtn">
</div>

</div>
</div>  <!-- wzb-panel End-->
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_cm_${lang}.js"></script>
<script type="text/javascript" src="${ctx}/static/js/cwn_utils.js"></script>
<script type="text/javascript">
var isNormal = ${isNormal};
$(function(){
	$("#amd_group").attr("href",contextPath+"/app/admin/group?isView="+cwn.getUrlParam('isView'));
	if(cwn.getUrlParam('isView') == 'true'){
		$("#amd_group").html("<lb:get key='label_cm.label_core_community_management_209' />");
	}
});

function changeGroupCard(thisObj, img_name){
	 if($(thisObj).val() == 2){
		$("input[name='image']").attr("disabled", false);
		$("#textfield").css("border", " 1px #AAAAAA solid");
	} else {
	//	$("input[name='image']").attr("disabled", true)
		$("input[name='s_grp_card']").val(img_name);
	//	$("#textfield").css("border", " 1px #ebebeb solid");
	} 
}

function submitForm(){
	if($("input[name='s_grp_title']").val() == ''){
		Dialog.alert(fetchLabel('label_core_community_management_80'));
        $("input[name='s_grp_title']").focus();
		return false;
	}
	if(getChars($("input[name='s_grp_title']").val()) > 80){
		Dialog.alert(fetchLabel('label_core_community_management_218'));
        $("input[name='s_grp_title']").focus();
		return false;
	}
	
	if($("input[name='group_card']:checked").val() == 2){
		if($("input[name='image']").val() != ''){
			var file_ext = $("input[name='image']").val().substring($("input[name='image']").val().lastIndexOf(".") + 1);
			if(file_ext != 'jpg' && file_ext != 'gif' && file_ext != 'png' && file_ext != 'jpeg'){
				Dialog.alert(fetchLabel('label_core_community_management_143'));
				return;
			}
		} else {
			Dialog.alert(fetchLabel('label_core_community_management_144'));
			return;
		}
	}else{
		$("input[name='image']").attr("disabled", true);
	}
	$("form").submit();
}
</script>
</body>
</html>