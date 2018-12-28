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
	    	 var ct_license_type = $('#ct_license_type').val();
	    	 var ct_license_alias = $('#ct_license_alias').val();
	    	 var ct_recover_hours_period = $('#ct_recover_hours_period').val();
	    	 
	    	 if(undefined == ct_license_type || wbUtilsTrimString(ct_license_type) == ''){
	    		 Dialog.alert(fetchLabel('label_core_cpt_d_management_3')+fetchLabel('label_core_cpt_d_management_48'));
	    		 return;
	    	 }else{
	    		 if(getChars(ct_license_type) > 20){
	    		  Dialog.alert(fetchLabel('label_core_cpt_d_management_3')+fetchLabel('label_core_cpt_d_management_50'));
	    			 return;
	    		 }
	    	 }
	    	 if(undefined == ct_license_alias || wbUtilsTrimString(ct_license_alias) == ''){
	    		 Dialog.alert(fetchLabel('label_core_cpt_d_management_4')+fetchLabel('label_core_cpt_d_management_48'));
	    		 return;
	    	 }else{
	    		 if(getChars(ct_license_alias) > 80){
	    		  Dialog.alert(fetchLabel('label_core_cpt_d_management_4')+fetchLabel('label_core_cpt_d_management_49'));
	    			 return;
	    		 }
	    	 }
	    	 
	    	 if(undefined != ct_recover_hours_period  && ct_recover_hours_period != ''){
	    		 var res = /^(0|[1-9][0-9]*)$/;
	    		 var re = new RegExp(res);
	    		 if(!re.test(ct_recover_hours_period) || ct_recover_hours_period > 999){
	    			 Dialog.alert(fetchLabel('label_core_cpt_d_management_51'));
	    			 return; 
	    		 }
	    	 }
	    	
	    	 var ct_trigger_email_type = $("input[name='ct_trigger_email_type']:checked").val();
	    	 if(ct_trigger_email_type == 2){
	    		 var ct_trigger_email_month_1 = $('#ct_trigger_email_month_1').attr('value');
	    		 var ct_trigger_email_month_2 = $('#ct_trigger_email_month_2').attr('value');
	    		 var ct_trigger_email_month_3 = $('#ct_trigger_email_month_3').attr('value');
	    		 var ct_trigger_email_date_1 = $('#ct_trigger_email_date_1').attr('value');
	    		 var ct_trigger_email_date_2 = $('#ct_trigger_email_date_2').attr('value');
	    		 var ct_trigger_email_date_3 = $('#ct_trigger_email_date_3').attr('value');
	    		 
	    		 var  check_time_flg = true;
	    		 if((ct_trigger_email_month_1 == '' ||ct_trigger_email_date_1 == '') && (ct_trigger_email_month_2 == '' ||ct_trigger_email_date_2 == '') &&(ct_trigger_email_month_3 == '' ||ct_trigger_email_date_3 == '') ){
	    			 check_time_flg = false;
	    		 }
	    		 if(ct_trigger_email_month_1 != '' || ct_trigger_email_date_1 != ''){
	    			 if(ct_trigger_email_month_1 == '' || ct_trigger_email_date_1 == ''){
	    			   check_time_flg = false;
	    			 }
	    		 }
	    		 if(ct_trigger_email_month_2 != '' || ct_trigger_email_date_2 != ''){
	    			 if(ct_trigger_email_month_2 == '' || ct_trigger_email_date_2 == ''){
	    			   check_time_flg = false;
	    			 }
	    		 }
	    		 if(ct_trigger_email_month_3 != '' || ct_trigger_email_date_3 != ''){
	    			 if(ct_trigger_email_month_3 == '' || ct_trigger_email_date_3 == ''){
	    			   check_time_flg = false;
	    			 }
	    		 }
	    		 if(!check_time_flg){
	    			 Dialog.alert(fetchLabel('label_core_cpt_d_management_52'));
	    			 return;
	    		 }
	    		 
	    		 if(ct_trigger_email_month_1!='' && ct_trigger_email_month_2!='' && ct_trigger_email_month_3!=''){
	                 if((ct_trigger_email_month_1+"-"+ct_trigger_email_date_1) == (ct_trigger_email_month_2+"-"+ct_trigger_email_date_2) ||
	                         (ct_trigger_email_month_1+"-"+ct_trigger_email_date_1) == (ct_trigger_email_month_3+"-"+ct_trigger_email_date_3) ||
	                            (ct_trigger_email_month_2+"-"+ct_trigger_email_date_2) == (ct_trigger_email_month_3+"-"+ct_trigger_email_date_3)){
	                     Dialog.alert(fetchLabel('label_core_cpt_d_management_204'));
	                     return;
	                 }
	    		 }
                 if(ct_trigger_email_month_1!='' && ct_trigger_email_month_2!='' && ct_trigger_email_month_3==''){
                     if((ct_trigger_email_month_1+"-"+ct_trigger_email_date_1) == (ct_trigger_email_month_2+"-"+ct_trigger_email_date_2)){
                         Dialog.alert(fetchLabel('label_core_cpt_d_management_204'));
                         return;
                     }
                 }
                 if(ct_trigger_email_month_1!='' && ct_trigger_email_month_2=='' && ct_trigger_email_month_3!=''){
                     if((ct_trigger_email_month_1+"-"+ct_trigger_email_date_1) == (ct_trigger_email_month_3+"-"+ct_trigger_email_date_3)){
                         Dialog.alert(fetchLabel('label_core_cpt_d_management_204'));
                         return;
                     }
                 }
                 if(ct_trigger_email_month_1=='' && ct_trigger_email_month_2!='' && ct_trigger_email_month_3!=''){
                     if((ct_trigger_email_month_2+"-"+ct_trigger_email_date_2) == (ct_trigger_email_month_3+"-"+ct_trigger_email_date_3)){
                         Dialog.alert(fetchLabel('label_core_cpt_d_management_204'));
                         return;
                     }
                 }
	    		 
	    	 }
	    	 
	    	 $.ajax({
			        url : "${ctx}/app/admin/cpdManagement/save",
			        type : 'POST',
			        data:  $('#formCpdInfo').serialize(),
			        dataType : 'json',
			        traditional : true,
			        success : function(data) {
			        	//console.log(data)
			        	if(data.success == false){
			        		Dialog.alert(fetchLabel('label_core_cpt_d_management_29'));
			        	}else{
			        		if(data.type == 'update'){
			        			go('${ctx}/app/admin/cpdManagement/detail/'+wbEncrytor().cwnEncrypt(data.ct_id));	
			        		}else{
			        		    go('${ctx}/app/admin/cpdManagement/index');
			        		}
			        	
			        	}
			        }
			     });
	     }
	
	     function check_ct_award_hours_type(text){
	    	 if(text == 0){
	    		 $('select[name="ct_trigger_email_month_1"]').val('');
	    		 $('select[name="ct_trigger_email_date_1"]').val('');
	    		 $('select[name="ct_trigger_email_month_2"]').val('');
	    		 $('select[name="ct_trigger_email_date_2"]').val('');
	    		 $('select[name="ct_trigger_email_month_3"]').val('');
	    		 $('select[name="ct_trigger_email_date_3"]').val('');

                 $('select[name="ct_trigger_email_date_'+1+'"]').attr("disabled","disabled");
                 $('select[name="ct_trigger_email_date_'+2+'"]').attr("disabled","disabled");
                 $('select[name="ct_trigger_email_date_'+3+'"]').attr("disabled","disabled");
	    	 }else{
	    		 var name = 'ct_trigger_email_month_'+text;
		    	 var date_val = $('select[name="'+name+'"]').val();
		    	 if(date_val == ''){
		    		 $('select[name="ct_trigger_email_date_'+text+'"]').val('');
		    		 $('select[name="ct_trigger_email_date_'+text+'"]').attr("disabled","disabled");
		    	 }else{
		    		 var dayNum = 31;
		    		 $('select[name="ct_trigger_email_date_'+text+'"]').empty(); 
		    		 if(date_val == 2){
		    			 dayNum = 29;
		    		 }
		    		 if(date_val == 4 || date_val == 6 || date_val == 9 || date_val == 11){
		    		 	 dayNum = 30;
		    		 }
		    		 $('select[name="ct_trigger_email_date_'+text+'"]').append("<option value=''>--</option>");
		    		 for(var i = 1; i <= dayNum; i++ ){
		    			 $('select[name="ct_trigger_email_date_'+text+'"]').append("<option value='"+i+"'>"+i+"</option>"); 
		    		 }
		    		
		    		 $('select[name="ct_trigger_email_date_'+text+'"]').removeAttr("disabled");  
		    	 }
	    		 
	    		 $("input:radio[name='ct_trigger_email_type']").eq(1).attr("checked",'checked');
	    	 }
	     }
	</script>
	
	<script>
		$(function(){ 
	    	var date_val_1 = $('select[name="ct_trigger_email_month_1"]').val();　
	    	var date_val_2 = $('select[name="ct_trigger_email_month_2"]').val();　　
	    	var date_val_3 = $('select[name="ct_trigger_email_month_3"]').val();　　　
			
	    	removeOptionByMonth_1(date_val_1);
	    	removeOptionByMonth_2(date_val_2);
	    	removeOptionByMonth_3(date_val_3);
			
		}); 
		
		function removeOptionByMonth_1(date_val){
			if(date_val == 2 || date_val == 4 || date_val == 6 || date_val == 9 || date_val == 11 ){
				if(date_val == 2){
					$("select[name='ct_trigger_email_date_1'] option[value='30']").remove();
				}
				$("select[name='ct_trigger_email_date_1'] option[value='31']").remove();
			}
		}
		function removeOptionByMonth_2(date_val){
			if(date_val == 2 || date_val == 4 || date_val == 6 || date_val == 9 || date_val == 11 ){
				if(date_val == 2){
					$("select[name='ct_trigger_email_date_2'] option[value='30']").remove();
				}
				$("select[name='ct_trigger_email_date_2'] option[value='31']").remove();
			}
		}
		function removeOptionByMonth_3(date_val){
			if(date_val == 2 || date_val == 4 || date_val == 6 || date_val == 9 || date_val == 11 ){
				if(date_val == 2){
					$("select[name='ct_trigger_email_date_3'] option[value='30']").remove();
				}
				$("select[name='ct_trigger_email_date_3'] option[value='31']").remove();
			}
		}
		
	</script>
	
</head>
<body>
	<input type="hidden" name="belong_module" value="FTN_AMD_CPT_D_MGT"/>
	<title:get function="global.FTN_AMD_CPT_D_MGT"/>

    <ol class="breadcrumb wzb-breadcrumb">
        <li><a href="javascript:wb_utils_gen_home(true);"><i class="fa wzb-breadcrumb-home fa-home"></i><lb:get key="global.lab_menu_started" /></a></li>
        <li><a href="/app/admin/cpdManagement/index"><lb:get key="global.FTN_AMD_CPT_D_LIST" /></a></li>
        <li class="active">
        	<c:choose>
	        	<c:when test="${type == 'update' }">
	        		<lb:get key="label_cpd.label_core_cpt_d_management_12" />
	        	</c:when>
	        	<c:otherwise>
	        		<lb:get key="label_cpd.label_core_cpt_d_management_11" />
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
          <form id="formCpdInfo"  method="post" cssClass="form-horizontal">
            <table cellpadding="0" cellspacing="0">
            <tbody>
                <tr>
                    <td class="wzb-form-label"><span class="wzb-form-star">*</span><lb:get key="label_cpd.label_core_cpt_d_management_3" />：</td>
                    <td class="wzb-form-control"><input class="wzb-inputText" value="${cpdType.ct_license_type}" name="ct_license_type" id="ct_license_type" type="text" style="width:300px;"></span></td>
                </tr>
                <tr>
                    <td class="wzb-form-label"><span class="wzb-form-star">*</span><lb:get key="label_cpd.label_core_cpt_d_management_4" />：</td>
                    <td class="wzb-form-control">
                        <input value="${cpdType.ct_id}" type="hidden" name="ct_id" id="ct_id"/> 
                        
                        <input class="wzb-inputText" value="${cpdType.ct_license_alias}" name="ct_license_alias" id="ct_license_alias" type="text" style="width:300px;"></span>
                    </td>
                </tr>
                <tr>
                    <td class="wzb-form-label"><span class="wzb-form-star">*</span><lb:get key="label_cpd.label_core_cpt_d_management_13" />：</td>
                    <td class="wzb-form-control">
                        <c:if test="${not empty cpdType.ct_starting_month}">
                           <c:choose>
                           	   <c:when test="${cpdType.ct_starting_month == 1}"><lb:get key="label_cpd.label_core_cpt_d_management_222" /></c:when>
                          	   <c:when test="${cpdType.ct_starting_month == 2}"><lb:get key="label_cpd.label_core_cpt_d_management_223" /></c:when>
                               <c:when test="${cpdType.ct_starting_month == 3}"><lb:get key="label_cpd.label_core_cpt_d_management_224" /></c:when>
                          	   <c:when test="${cpdType.ct_starting_month == 4}"><lb:get key="label_cpd.label_core_cpt_d_management_225" /></c:when>
                          	   <c:when test="${cpdType.ct_starting_month == 5}"><lb:get key="label_cpd.label_core_cpt_d_management_226" /></c:when>
                          	   <c:when test="${cpdType.ct_starting_month == 6}"><lb:get key="label_cpd.label_core_cpt_d_management_227" /></c:when>
                          	   <c:when test="${cpdType.ct_starting_month == 7}"><lb:get key="label_cpd.label_core_cpt_d_management_228" /></c:when>
                          	   <c:when test="${cpdType.ct_starting_month == 8}"><lb:get key="label_cpd.label_core_cpt_d_management_229" /></c:when>
                          	   <c:when test="${cpdType.ct_starting_month == 9}"><lb:get key="label_cpd.label_core_cpt_d_management_230" /></c:when>
                          	   <c:when test="${cpdType.ct_starting_month == 10}"><lb:get key="label_cpd.label_core_cpt_d_management_42" /></c:when>
                          	   <c:when test="${cpdType.ct_starting_month == 11}"><lb:get key="label_cpd.label_core_cpt_d_management_43" /></c:when>
                          	   <c:when test="${cpdType.ct_starting_month == 12}"><lb:get key="label_cpd.label_core_cpt_d_management_44" /></c:when>
                           </c:choose>
                          </c:if>
                        <c:if test="${empty cpdType.ct_starting_month}">
	                        <select name="ct_starting_month" id="ct_starting_month">
	                            <option value="1"><lb:get key="label_cpd.label_core_cpt_d_management_222" /></option>
	                            <option value="2"><lb:get key="label_cpd.label_core_cpt_d_management_223" /></option>
	                            <option value="3"><lb:get key="label_cpd.label_core_cpt_d_management_224" /></option>
	                            <option value="4"><lb:get key="label_cpd.label_core_cpt_d_management_225" /></option>
	                            <option value="5"><lb:get key="label_cpd.label_core_cpt_d_management_226" /></option>
	                            <option value="6"><lb:get key="label_cpd.label_core_cpt_d_management_227" /></option>
	                            <option value="7"><lb:get key="label_cpd.label_core_cpt_d_management_228" /></option>
	                            <option value="8"><lb:get key="label_cpd.label_core_cpt_d_management_229" /></option>
	                            <option value="9"><lb:get key="label_cpd.label_core_cpt_d_management_230" /></option>
	                            <option value="10"><lb:get key="label_cpd.label_core_cpt_d_management_42" /></option>
	                            <option value="11"><lb:get key="label_cpd.label_core_cpt_d_management_43" /></option>
	                            <option value="12"><lb:get key="label_cpd.label_core_cpt_d_management_44" /></option>
	                        </select>
	                    </c:if>
                    </td>
                </tr>
                <tr>
                    <td class="wzb-form-label" valign="top"><span class="wzb-form-star">*</span><lb:get key="label_cpd.label_core_cpt_d_management_14" />：</td>
                    <td class="wzb-form-control">
                        <table>
                            <tbody>
                            	<c:choose>
						        	<c:when test="${type == 'update' }">
						        		<tr>
		                                    <td>
		                                        <label class="wzb-form-checkbox">
		                                            <input name="ct_award_hours_type" value="1" type="radio" <c:if test="${cpdType.ct_award_hours_type == 1}">checked="checked"</c:if> disabled="disabled"><lb:get key="label_cpd.label_core_cpt_d_management_18" />
		                                        </label>
		                                    </td>
		                                </tr>
		                                <tr>
		                                    <td>
		                                        <label class="wzb-form-checkbox">
		                                            <input name="ct_award_hours_type" value="2" type="radio" <c:if test="${cpdType.ct_award_hours_type == 2}">checked="checked"</c:if> disabled="disabled"><lb:get key="label_cpd.label_core_cpt_d_management_19" /><span class="ask-in margin4 allhint-style-1" id="assessment-cycle"></span>
		                                        </label>
		                                    </td>
		                                </tr>
		                                <tr>
		                                    <td>
		                                        <label class="wzb-form-checkbox">
		                                            <input name="ct_award_hours_type" value="3" type="radio" <c:if test="${cpdType.ct_award_hours_type == 3}">checked="checked"</c:if> disabled="disabled"><lb:get key="label_cpd.label_core_cpt_d_management_20" />
		                                        </label>
		                                    </td>
		                                </tr>
						        	</c:when>
						        	<c:otherwise>
						        		<tr>
		                                    <td>
		                                        <label class="wzb-form-checkbox">
		                                            <input name="ct_award_hours_type" value="1" type="radio" checked="checked"><lb:get key="label_cpd.label_core_cpt_d_management_18" />
		                                        </label>
		                                    </td>
		                                </tr>
		                                <tr>
		                                    <td>
		                                        <label class="wzb-form-checkbox">
		                                            <input name="ct_award_hours_type" value="2" type="radio" ><lb:get key="label_cpd.label_core_cpt_d_management_19" /><span class="ask-in margin4 allhint-style-1" id="assessment-cycle"></span>
		                                        </label>
		                                    </td>
		                                </tr>
		                                <tr>
		                                    <td>
		                                        <label class="wzb-form-checkbox">
		                                            <input name="ct_award_hours_type" value="3" type="radio" ><lb:get key="label_cpd.label_core_cpt_d_management_20" />
		                                        </label>
		                                    </td>
		                                </tr>
						        	</c:otherwise>
					        	</c:choose>
                            </tbody>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td class="wzb-form-label" valign="top"><lb:get key="label_cpd.label_core_cpt_d_management_15" />：</td>
                    <td class="wzb-form-control">
                        <table>
                            <tbody>
                                <tr>
                                    <td>
                                        <label class="wzb-form-checkbox">
                                            <input onclick="check_ct_award_hours_type(0);" name="ct_trigger_email_type" value="1" type="radio" checked="checked"><lb:get key="label_cpd.label_core_cpt_d_management_21" />
                                        </label>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <label class="wzb-form-checkbox">
                                            <input name="ct_trigger_email_type" value="2" type="radio"  <c:if test="${cpdType.ct_trigger_email_type == 2}">checked="checked"</c:if>><lb:get key="label_cpd.label_core_cpt_d_management_22" /><span class="ask-in margin4 allhint-style-1" id="mail-date"></span>
                                        </label>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td class="wzb-form-label"></td>
                    <td class="wzb-form-control" style="padding-left:30px;">
                        <select name="ct_trigger_email_month_1" id="ct_trigger_email_month_1" onchange="check_ct_award_hours_type(1)">
                            <option value="">--</option>
                            
                            <c:forEach var="i" begin="1" end="12"> 
                               <option value="${i}" <c:if test="${cpdType.ct_trigger_email_month_1 == i}">selected="selected"</c:if> >${i}</option>
                            </c:forEach> 
                            
                        </select>
                        <select <c:if test="${cpdType.ct_trigger_email_month_1 == null}">disabled="disabled"</c:if>  name="ct_trigger_email_date_1" id="ct_trigger_email_date_1" onchange="">
                            <option value="">--</option>
                            <c:forEach var="i" begin="1" end="31"> 
                               <option value="${i}" <c:if test="${cpdType.ct_trigger_email_date_1 == i}">selected="selected"</c:if>  >${i}</option>
                            </c:forEach>                               
                        </select>
                         <lb:get key="label_cpd.label_core_cpt_d_management_47" />
                    </td>
                </tr>
                <tr>
                    <td class="wzb-form-label"></td>
                    <td class="wzb-form-control" style="padding-left:30px;">
                        <select name="ct_trigger_email_month_2" id="ct_trigger_email_month_2" onchange="check_ct_award_hours_type(2)">
                            <option value="">--</option>
                             <c:forEach var="i" begin="1" end="12"> 
                               <option value="${i}" <c:if test="${cpdType.ct_trigger_email_month_2 == i}">selected="selected"</c:if>  >${i}</option>
                            </c:forEach> 
                        </select>
                        <select <c:if test="${cpdType.ct_trigger_email_month_2 == null}">disabled="disabled"</c:if>  name="ct_trigger_email_date_2" id="ct_trigger_email_date_2" onchange="">
                            <option value="">--</option>
                             <c:forEach var="i" begin="1" end="31"> 
                               <option value="${i}" <c:if test="${cpdType.ct_trigger_email_date_2 == i}">selected="selected"</c:if>  >${i}</option>
                            </c:forEach> 
                        </select>
                         <lb:get key="label_cpd.label_core_cpt_d_management_47" />
                    </td>
                </tr>
                
                 <tr>
                    <td class="wzb-form-label"></td>
                    <td class="wzb-form-control" style="padding-left:30px;">
                        <select name="ct_trigger_email_month_3" id="ct_trigger_email_month_3" onchange="check_ct_award_hours_type(3)">
                            <option value="">--</option>
                             <c:forEach var="i" begin="1" end="12"> 
                               <option value="${i}" <c:if test="${cpdType.ct_trigger_email_month_3 == i}">selected="selected"</c:if>  >${i}</option>
                            </c:forEach> 
                        </select>
                        <select <c:if test="${cpdType.ct_trigger_email_month_3 == null}">disabled="disabled"</c:if>  name="ct_trigger_email_date_3" id="ct_trigger_email_date_3" onchange="">
                            <option value="">--</option>
                             <c:forEach var="i" begin="1" end="31"> 
                               <option value="${i}" <c:if test="${cpdType.ct_trigger_email_date_3 == i}">selected="selected"</c:if>  >${i}</option>
                            </c:forEach> 
                        </select>
                         <lb:get key="label_cpd.label_core_cpt_d_management_47" />
                    </td>
                </tr>
                
                <%-- <tr>
                    <td class="wzb-form-label"></td>
                    <td class="wzb-form-control" style="padding-left:30px;" onchange="check_ct_award_hours_type(3)">
                        <select name="ct_trigger_email_month_3" id="ct_trigger_email_month_3">
                            <option value="">--</option>
                             <c:forEach var="i" begin="1" end="12"> 
                               <option value="${i}" <c:if test="${cpdType.ct_trigger_email_month_3 == i}">selected="selected"</c:if> >${i}</option>
                            </c:forEach> 
                        </select>
                        <select   <c:if test="${cpdType.ct_trigger_email_month_3 == null}">disabled="disabled"</c:if> name="ct_trigger_email_date_3" id="ct_trigger_email_date_3" onchange="">
                            <option value="">--</option>
                             <c:forEach var="i" begin="1" end="31"> 
                               <option value="${i}" <c:if test="${cpdType.ct_trigger_email_date_3 == i}">selected="selected"</c:if>  >${i}</option>
                            </c:forEach> 
                        </select>
                          <lb:get key="label_cpd.label_core_cpt_d_management_47" />
                    </td>
                </tr> --%>
                <tr>
                    <td class="wzb-form-label" valign="top"><lb:get key="label_cpd.label_core_cpt_d_management_16" />：</td>
                    <td class="wzb-form-control">
                        <table>
                            <tbody>
                            	<c:choose>
						        	<c:when test="${type == 'update' }">
						        		<tr>
		                                    <td>
		                                        <label class="wzb-form-checkbox">
		                                            <input name="ct_cal_before_ind"  value="1" type="radio" <c:if test="${cpdType.ct_cal_before_ind == 1}">checked="checked"</c:if> disabled="disabled"><lb:get key="label_cpd.label_core_cpt_d_management_23" />
		                                        </label>
		                                    </td>
		                                </tr>
		                                <tr>
		                                    <td>
		                                        <label class="wzb-form-checkbox">
		                                            <input name="ct_cal_before_ind" value="2" type="radio" <c:if test="${cpdType.ct_cal_before_ind == 2}">checked="checked"</c:if> disabled="disabled" ><lb:get key="label_cpd.label_core_cpt_d_management_24" />
		                                        </label>
		                                    </td>
		                                </tr>
						        	</c:when>
						        	<c:otherwise>
						        		<tr>
		                                    <td>
		                                        <label class="wzb-form-checkbox">
		                                            <input name="ct_cal_before_ind"  value="1" type="radio" checked="checked"><lb:get key="label_cpd.label_core_cpt_d_management_23" />
		                                        </label>
		                                    </td>
		                                </tr>
		                                <tr>
		                                    <td>
		                                        <label class="wzb-form-checkbox">
		                                            <input name="ct_cal_before_ind" value="2" type="radio"><lb:get key="label_cpd.label_core_cpt_d_management_24" />
		                                        </label>
		                                    </td>
		                                </tr>
						        	</c:otherwise>
					        	</c:choose>
                                
                            </tbody>
                        </table>
                    </td>
                </tr>
                <%-- <tr>
                    <td class="wzb-form-label" valign="top"><lb:get key="label_cpd.label_core_cpt_d_management_17" />：</td>
                    <td class="wzb-form-control">
                        <table>
                            <tbody>
                                <tr>
                                    <td><input class="wzb-inputText" maxlength="3" value="${cpdType.ct_recover_hours_period}" id="ct_recover_hours_period" name="ct_recover_hours_period" type="text" style="width:100px;">&nbsp;&nbsp;<lb:get key="label_cpd.label_core_cpt_d_management_53" /><span class="ask-in margin4 allhint-style-1" id="alias"></td>
                                </tr>
                            </tbody>
                        </table>
                    </td>
                </tr> --%>
                <tr>
                    <td class="wzb-form-label" valign="top"></td>
                    <td class="wzb-form-control">
                        <span class="wzb-form-star">*<lb:get key="label_rm.label_core_requirements_management_35" /></span>
                    </td>
                </tr>
            </tbody>
        </table>
        <div class="wzb-bar">
            <button type="button" onclick="javascript:formSubmit();" class="btn wzb-btn-blue margin-right10 wzb-btn-big">
				<lb:get key="global.button_ok" />
			</button>
				<c:choose>
	        	<c:when test="${type == 'update' }">
	        		<button type="button" onclick="javascript:go('${ctx}/app/admin/cpdManagement/detail/'+wbEncrytor().cwnEncrypt(${cpdType.ct_id})+'');" class="btn wzb-btn-blue wzb-btn-big">
						<lb:get key="global.button_cancel" />
					</button>
	        	</c:when>
	        	<c:otherwise>
	        		<button type="button" onclick="javascript:go('${ctx}/app/admin/cpdManagement/index')" class="btn wzb-btn-blue wzb-btn-big">
						<lb:get key="global.button_cancel" />
					</button>
	        	</c:otherwise>
        	</c:choose>
        </div>  
			</form>
        </div>
         <!-- 内容添加结束 -->
    </div>
    <!-- wzb-panel End-->
     <script type="text/javascript">
         $("#alias").mouseenter(function(){
              layer.tips(fetchLabel('label_core_cpt_d_management_202'),'#alias', {
            tips: [2,'rgba(128,128,128,0.9)'],
            time:50000
              });
          });
          $("#alias").mouseleave(function(){
              layer.tips()
          });

         $("#assessment-cycle").mouseenter(function(){
              layer.tips(fetchLabel('label_core_cpt_d_management_201'),'#assessment-cycle', {
            tips: [2, 'rgba(128,128,128,0.9)'],
            time:50000
              });
          });
          $("#assessment-cycle").mouseleave(function(){
              layer.tips()
          });

         $("#mail-date").mouseenter(function(){
              layer.tips(fetchLabel('label_core_cpt_d_management_200') ,'#mail-date', {
            tips: [2, 'rgba(128,128,128,0.9)'],
            time:50000
              });
          });
          $("#mail-date").mouseleave(function(){
              layer.tips()
          });
  
    </script>
</body>

</html>