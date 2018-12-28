<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.cwn.wizbank.entity.*,java.util.*,com.cw.wizbank.util.*, com.cw.wizbank.qdb.*"%>
<%@ include file="../../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<script type="text/javascript" src="${ctx}/js/jquery.selector.js"></script>
	<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_cpd_${lang}.js"></script>
	<script type="text/javascript" src="${ctx}/static/js/cwn_utils.js"></script>
	
	
	<script type="text/javascript">
	     function formSubmit(){
	    	 
	    	 var cg_code = $('#cg_code').val();
	    	 var cg_alias = $('#cg_alias').val();
	    	 
	    	 if(undefined == cg_code || cg_code == ''){
	    		 Dialog.alert(fetchLabel('label_core_cpt_d_management_83')+fetchLabel('label_core_cpt_d_management_48'));
	    		 return;
	    	 }else{
	    		 if(getChars(cg_code) > 20){
	    		  Dialog.alert(fetchLabel('label_core_cpt_d_management_83')+fetchLabel('label_core_cpt_d_management_50'));
	    			 return;
	    		 }
	    	 }
	    	 if(undefined == cg_alias || cg_alias == ''){
	    		 Dialog.alert(fetchLabel('label_core_cpt_d_management_4')+fetchLabel('label_core_cpt_d_management_48'));
	    		 return;
	    	 }else{
	    		 if(getChars(cg_alias) > 80){
	    		  Dialog.alert(fetchLabel('label_core_cpt_d_management_4')+fetchLabel('label_core_cpt_d_management_49'));
	    			 return;
	    		 }
	    	 }
	    	 
	    	 
	    	 $.ajax({
			        url : "${ctx}/app/admin/cpdGroup/save",
			        type : 'POST',
			        data:  $('#formCpdGroup').serialize(),
			        dataType : 'json',
			        traditional : true,
			        success : function(data) {
			        	//console.log(data)
			        	if(data.success == false){
			        		Dialog.alert(fetchLabel('label_core_cpt_d_management_218'));
			        	}else{
			        		go('${ctx}/app/admin/cpdManagement/detail/'+wbEncrytor().cwnEncrypt(${cpd.ct_id }));
			        	}
			        }
			     });
	     }
	
	     
	</script>
	
</head>
<body>
	<input type="hidden" name="belong_module" value="FTN_AMD_CPT_D_MGT"/>
	<title:get function="global.FTN_AMD_CPT_D_MGT"/>

    <ol class="breadcrumb wzb-breadcrumb">
        <li><a href="javascript:wb_utils_gen_home(true);"><i class="fa wzb-breadcrumb-home fa-home"></i><lb:get key="global.lab_menu_started" /></a></li>
        <li><a href="/app/admin/cpdManagement/index"><lb:get key="global.FTN_AMD_CPT_D_LIST" /></a></li>
        <li><a onclick="javascript:go('${ctx}/app/admin/cpdManagement/detail/'+wbEncrytor().cwnEncrypt(${cpd.ct_id })+'')"   href="#">${cpd.ct_license_type}</a></li>
        <li class="active">
        	<c:choose>
	        	<c:when test="${type == 'update' }">
	        		<lb:get key="label_cpd.label_core_cpt_d_management_84" />
	        	</c:when>
	        	<c:otherwise>
	        		<lb:get key="label_cpd.label_core_cpt_d_management_85" />
	        	</c:otherwise>
        	</c:choose>
       	</li>
    </ol>
    
    <div class="panel wzb-panel">
        <div class="panel-heading">
        	<c:choose>
	        	<c:when test="${type == 'update' }">
	        		<lb:get key="label_cpd.label_core_cpt_d_management_12" />
	        	</c:when>
	        	<c:otherwise>
	        		<lb:get key="label_cpd.label_core_cpt_d_management_11" />
	        	</c:otherwise>
        	</c:choose>
        </div>
    
     <!-- 内容添加开始 -->
        <div class="panel-body">
          <form id="formCpdGroup"  method="post" cssClass="form-horizontal">
            <table cellpadding="0" cellspacing="0">
              <tbody>
                <tr>
                    <td class="wzb-form-label"><span class="wzb-form-star">*</span><lb:get key="label_cpd.label_core_cpt_d_management_83" />：</td>
                    <td class="wzb-form-control">
                        <input value="${cpd.ct_id}" type="hidden" name="cg_ct_id" id="cg_ct_id"/> 
                        <input value="${cpdGroup.cg_id}" type="hidden" name="cg_id" id="cg_id"/>
                        <input class="wzb-inputText" value="${cpdGroup.cg_code}" name="cg_code" id="cg_code" type="text" style="width:300px;">
                    </td>
                </tr>
                <tr>
                    <td class="wzb-form-label"><span class="wzb-form-star">*</span><lb:get key="label_cpd.label_core_cpt_d_management_4" />：</td>
                    <td class="wzb-form-control">
                        <input class="wzb-inputText" value="${cpdGroup.cg_alias}" name="cg_alias" id="cg_alias" type="text" style="width:300px;">
                    </td>
                </tr>
                <tr>
                    <td class="wzb-form-label" valign="top"><span class="wzb-form-star">*</span><lb:get key="label_cpd.label_core_cpt_d_management_86" />：</td>
                    <td class="wzb-form-control">
                        <table>
                            <tbody>
                                <tr>
                                    <td><input name="cg_contain_non_core_ind" value="1" type="radio" <c:if test="${cpdGroup.cg_contain_non_core_ind == 1}">checked="checked"</c:if> ><lb:get key="label_cpd.label_core_cpt_d_management_87" /></td>
                                </tr>
                                <tr>
                                    <td><input name="cg_contain_non_core_ind" value="0" type="radio" <c:if test="${cpdGroup.cg_contain_non_core_ind != 1}">checked="checked"</c:if> ><lb:get key="label_cpd.label_core_cpt_d_management_88" /></td>
                                </tr>
                            </tbody>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td class="wzb-form-label" valign="top"><span class="wzb-form-star">*</span><lb:get key="label_cpd.label_core_cpt_d_management_89" />：</td>
                    <td class="wzb-form-control">
                        <table>
                            <tbody>
                                <tr>
                                    <td><input name="cg_display_in_report_ind" value="1" type="radio" checked="checked"><lb:get key="label_cpd.label_core_cpt_d_management_87" /></td>
                                </tr>
                                <tr>
                                    <td><input name="cg_display_in_report_ind" value="0" type="radio" <c:if test="${cpdGroup.cg_display_in_report_ind == 0}">checked="checked"</c:if>><lb:get key="label_cpd.label_core_cpt_d_management_88" /></td>
                                </tr>
                            </tbody>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td class="wzb-form-label"></td>
                    <td class="wzb-form-control"><span class="wzb-form-star">*<lb:get key="label_rm.label_core_requirements_management_35" /></span></td>
                </tr>
            </tbody>
        </table>
        <div class="wzb-bar">
            <button type="button" onclick="javascript:formSubmit();" class="btn wzb-btn-blue margin-right10 wzb-btn-big">
				<lb:get key="global.button_ok" />
			</button>
			<button type="button" onclick="javascript:go('${ctx}/app/admin/cpdManagement/detail/'+wbEncrytor().cwnEncrypt(${cpd.ct_id })+'')" class="btn wzb-btn-blue wzb-btn-big">
				<lb:get key="global.button_cancel" />
			</button>
        </div>  
			</form>
        </div>
         <!-- 内容添加结束 -->
    </div>
    <!-- wzb-panel End-->
   
</body>

</html>