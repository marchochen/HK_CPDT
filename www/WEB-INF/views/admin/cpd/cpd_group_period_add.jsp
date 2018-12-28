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
	    	 var cg_contain_non_core_ind =  ${cpdGroup.cg_contain_non_core_ind};
	    	 var statrData =  $('#statrData').val();
	    	 var res = /^(0|[1-9][0-9]*)$/;
    		 var re = new RegExp(res);
    		 if(statrData == null || statrData == ''  || statrData == undefined){
    			 Dialog.alert(fetchLabel('label_core_cpt_d_management_219'));
    			 return; 
    		 }
    		 if(!re.test(statrData) || statrData > 9999 || statrData < 1000){
    			 Dialog.alert(fetchLabel('label_core_cpt_d_management_90'));
    			 return; 
    		 }
    		 
    		 var isflg = true;
    		 var  positiveNum = /^[0-9]+\.?[0-9]{0,2}$/;
    		 var pNum  = new RegExp(positiveNum);
    	     for(var i = 1; i <= 12; i++){
    		   var num	= document.getElementById('cpdGroupHours['+i+'].cgh_core_hours').value;
    		   var non_num	= document.getElementById('cpdGroupHours['+i+'].cgh_non_core_hours').value;
    		   if(num == ''){
    			   document.getElementById('cpdGroupHours['+i+'].cgh_core_hours').value = 0; 
    		   }
    		   if(non_num == '' && cg_contain_non_core_ind == 1){
    			   document.getElementById('cpdGroupHours['+i+'].cgh_non_core_hours').value = 0; 
    		   }
    		   if(num == '' && non_num == ''){
    			   continue;
    		   }
    		   if(num != ''){
    			   if(!pNum.test(num) || num > 999.99){
        			   Dialog.alert(fetchLabel('label_core_cpt_d_management_205'));
        			   return;
        		   }else if(num > 0){
        			   isflg = false;
        		   }
    		   }
    		   if(non_num != ''){
    			   if(!pNum.test(non_num) || non_num > 999.99){
        			   Dialog.alert(fetchLabel('label_core_cpt_d_management_205'));
        			   return;
        		   }
    		   }
    		 } 
    	     if(isflg){
    	    	 Dialog.alert(fetchLabel('label_core_cpt_d_management_203'));
    	    	 return;
    	     }
    	     
	    	  $.ajax({
			        url : "${ctx}/app/admin/cpdGroupPeriod/save",
			        type : 'POST',
			        data:  $('#formGroupPeriod').serialize(),
			        dataType : 'json',
			        traditional : true,
			        success : function(data) {
			        	//console.log(data)
			        	if(data.success == false){
			        		Dialog.alert(fetchLabel('label_core_cpt_d_management_91'));
			        	}else{
			        		go('${ctx}/app/admin/cpdGroup/groupPeriod?cg_id='+wbEncrytor().cwnEncrypt(${cpdGroup.cg_id }))
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
        <li><a onclick="javascript:go('${ctx}/app/admin/cpdManagement/detail/'+wbEncrytor().cwnEncrypt(${cpdGroup.cpdType.ct_id})+'')"  href="#">${cpdGroup.cpdType.ct_license_type}</a></li>
         
        <li><a onclick="javascript:go('${ctx}/app/admin/cpdManagement/detail/'+wbEncrytor().cwnEncrypt(${cpdGroup.cpdType.ct_id})+'')"  href="#">${cpdGroup.cg_code}</a></li>
        <li><a onclick="javascript:go('${ctx}/app/admin/cpdGroup/groupPeriod?cg_id='+wbEncrytor().cwnEncrypt(${cpdGroup.cg_id })+'')"  href="#"><lb:get key="label_cpd.label_core_cpt_d_management_92" /></a></li>
        
        <li class="active">
        	<c:choose>
	        	<c:when test="${type == 'update' }">
	        		<lb:get key="label_cpd.label_core_cpt_d_management_93" />
	        	</c:when>
	        	<c:otherwise>
	        		<lb:get key="label_cpd.label_core_cpt_d_management_94" />
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
          <form id="formGroupPeriod"  method="post" cssClass="form-horizontal">
            <table cellpadding="0" cellspacing="0">
              <tbody>
                <tr>
                    <td class="wzb-form-label"><span class="wzb-form-star">*</span><lb:get key="label_cpd.label_core_cpt_d_management_95" />：</td>
                    <td class="wzb-form-control" colspan="3">
                        <input value="${cpdGroup.cg_id}" type="hidden" name="cgp_cg_id" id="cgp_cg_id"/>
                        <input value="${cpdGroup.cpdType.ct_id}" type="hidden" name="cgp_ct_id" id="cgp_ct_id"/>
                        
                        <input value="${cpdGroup.cg_code}" type="hidden" name="cg_name" id="cg_name"/>
                        <input value="${cpdGroup.cpdType.ct_license_type}" type="hidden" name="ct_name" id="ct_name"/>
                        
                        <input value="${cpdGroupPeriod.cgp_id}" type="hidden" name="cgp_id" id="cgp_id"/>
                        <input value="${cpdGroup.cpdType.ct_starting_month}" type="hidden" name="statrMonth" id="statrMonth"/>
                        <c:choose>
				        	<c:when test="${type == 'update' }">
				        	    <input value="${cgp_effective_time}" id="statrData" name="statrData" type="hidden">
				        	</c:when>
				        	<c:otherwise>
				        		<input class="wzb-inputText" value="" id="statrData" name="statrData" type="text" style="width:100px;">
				        	</c:otherwise>
			        	</c:choose>
			        	<c:choose>
				        	<c:when test="${cpdGroup.cpdType.ct_starting_month <= 9 }">
				        		${cgp_effective_time}-0${cpdGroup.cpdType.ct_starting_month}-01
				        	</c:when>
				        	<c:otherwise>
				        		${cgp_effective_time}-${cpdGroup.cpdType.ct_starting_month}-01
				        	</c:otherwise>
			        	</c:choose><span class="color-gray999 margin-left20">（YYYY-MM-DD）</span>
                    </td>
                </tr>
                <tr>
                    <td class="wzb-form-label"><lb:get key="label_cpd.label_core_cpt_d_management_96" />：</td>
                    <td class="wzb-form-control" colspan="3"></td>
                </tr>
                <tr>
                    <td class="wzb-form-label"></td>
                    <td class="wzb-form-control text-center" width="12%"><lb:get key="label_cpd.label_core_cpt_d_management_97" /></td>
                    <td class="wzb-form-control text-center" width="18%"><lb:get key="label_cpd.label_core_cpt_d_management_98" /></td>
                    <td class="wzb-form-control" width="50%"><lb:get key="label_cpd.label_core_cpt_d_management_99" /></td>
                </tr>
               
                <c:if test="${fn:length(cpdGroupPeriod.cpdGroupHours) == 0}">
	                <c:forEach var="i" begin="1" end="12"> 
	                    <tr>
	                    <td class="wzb-form-label"></td>
	                    <td class="wzb-form-control text-center">
	                        <input value="${13-i}" type="hidden" id="cpdGroupHours[${13-i}].cgh_declare_month" name="cpdGroupHours[${13-i}].cgh_declare_month"/>
	                        ${13-i}</td>
	                    <td class="wzb-form-control text-center"><input class="wzb-inputText" value="0.0" id="cpdGroupHours[${13-i}].cgh_core_hours" name="cpdGroupHours[${13-i}].cgh_core_hours"  type="text" style="width:100px;"></td>
	                    <td class="wzb-form-control">
	                      <input class="wzb-inputText" <c:if test="${cpdGroup.cg_contain_non_core_ind == 0}">disabled="true"</c:if>  value="0.0" id="cpdGroupHours[${13-i}].cgh_non_core_hours" name="cpdGroupHours[${13-i}].cgh_non_core_hours" type="text" style="width:100px;">
	                    </td>
	                </tr>
	                </c:forEach>
	             </c:if>   
                
                <c:if test="${fn:length(cpdGroupPeriod.cpdGroupHours) > 0}">
	                <c:forEach var="hours" items="${cpdGroupPeriod.cpdGroupHours}" varStatus="status"> 
	                    <tr>
	                    <td class="wzb-form-label"></td>
	                    <td class="wzb-form-control text-center">
	                        <input value="${hours.cgh_declare_month}" type="hidden" id="cpdGroupHours[${12-status.index}].cgh_declare_month" name="cpdGroupHours[${12-status.index}].cgh_declare_month"/>
	                         <input value="${hours.cgh_id}" type="hidden" id="cpdGroupHours[${12-status.index}].cgh_id" name="cpdGroupHours[${12-status.index}].cgh_id"/>
	                        ${hours.cgh_declare_month}</td>
	                    <td class="wzb-form-control text-center"><input class="wzb-inputText" value="${hours.cgh_core_hours}" id="cpdGroupHours[${12-status.index}].cgh_core_hours" name="cpdGroupHours[${12-status.index}].cgh_core_hours"  type="text" style="width:100px;"></td>
	                    <td class="wzb-form-control"><input class="wzb-inputText" <c:if test="${cpdGroup.cg_contain_non_core_ind == 0}">disabled="true"</c:if> value="${hours.cgh_non_core_hours}" id="cpdGroupHours[${12-status.index}].cgh_non_core_hours" name="cpdGroupHours[${12-status.index}].cgh_non_core_hours" type="text" style="width:100px;"></td>
	                </tr>
	                </c:forEach>
                </c:if>
                
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
			<button type="button" onclick="javascript:go('${ctx}/app/admin/cpdGroup/groupPeriod?cg_id='+wbEncrytor().cwnEncrypt(${cpdGroup.cg_id })+'')" class="btn wzb-btn-blue wzb-btn-big">
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