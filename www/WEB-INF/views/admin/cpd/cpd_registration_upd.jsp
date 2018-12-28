<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.cwn.wizbank.entity.*,java.util.*,com.cw.wizbank.util.*, com.cw.wizbank.qdb.*"%>
<%@ include file="../../common/taglibs.jsp"%>

<!DOCTYPE html>
<html>
<head>
    <script type="text/javascript" src="${ctx}/js/wb_utils.js"></script>
	<script type="text/javascript" src="${ctx}/js/jquery.selector.js"></script>
	<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_cpd_${lang}.js"></script>
   <script type="text/javascript" src="${ctx}/static/js/cwn_utils.js"></script>
	<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-admin.css" />
	
	<script type="text/javascript">
		window.onload = function(){
		    var uSelector = new userSelector();
            uSelector.multiSelect = false;
		    uSelector.selectedCallBack = function(data){
	            $("#userName").val(data[0].name);
	            $("#cr_usr_ent_id").val(data[0].id);
	            $("#userIdFullName").html(data[0].nameFull);
	            uSelector.close();
		    };
		    
		    $('#user_choose_btn').click(function(){
	            uSelector.show();
	            //checkedOrDisabledById('exportUser',1,'includeDelUser');
	        });
		}
	    
		
	    function reloadTable() {
	        var ct_id = $('#cr_ct_id option:selected') .val()
	    	$.ajax({
                url : '${ctx}/app/admin/cpdRegistrationMgt/getCpdGroupAndType?ct_id='+ct_id,
                dataType : 'json',
                success : function(data) {
                    $("#tableContent").html("");
                    if(data.cpdGroupList.length>0){
                        $("#ct_license_alias").html(data.cpdGroupList[0].cpdType.ct_license_alias);
                    }else{
                        $("#ct_license_alias").html("");
                    }
                	 for(var i=0;i<data.cpdGroupList.length;i++){
                         p = {
                        		 cg_id : data.cpdGroupList[i].cg_id,
                        		 cg_alias : data.cpdGroupList[i].cg_alias
                         };
                         $("#tableContent").append($('#group-template').render(p));
                     }
                }
            });
	    };
	    
	    
	       function checkOneActive(id,checked){
	            if(checked == true ){
	                document.getElementById("initial_Date_time"+id+"_yy").disabled = false;
	                document.getElementById("initial_Date_time"+id+"_mm").disabled = false;
	                document.getElementById("initial_Date_time"+id+"_dd").disabled = false;
	                
	                document.getElementById("expiry_Date_time"+id+"_yy").disabled = false;
	                document.getElementById("expiry_Date_time"+id+"_mm").disabled = false;
	                document.getElementById("expiry_Date_time"+id+"_dd").disabled = false;

	                $("#iniDate_"+id).removeAttr("onclick");
	                $("#excDate_"+id).removeAttr("onclick");
	                
	            }else{
	                document.getElementById("initial_Date_time"+id+"_yy").disabled = true;
	                document.getElementById("initial_Date_time"+id+"_mm").disabled = true;
	                document.getElementById("initial_Date_time"+id+"_dd").disabled = true;
	                
	                document.getElementById("expiry_Date_time"+id+"_yy").disabled = true;
	                document.getElementById("expiry_Date_time"+id+"_mm").disabled = true;
	                document.getElementById("expiry_Date_time"+id+"_dd").disabled = true;

	                document.getElementById("initial_Date_time"+id+"_yy").value = "";
	                document.getElementById("initial_Date_time"+id+"_mm").value = "";
	                document.getElementById("initial_Date_time"+id+"_dd").value = "";
	                
	                document.getElementById("expiry_Date_time"+id+"_yy").value = "";
	                document.getElementById("expiry_Date_time"+id+"_mm").value = "";
	                document.getElementById("expiry_Date_time"+id+"_dd").value = "";

	                
	                $("#iniDate_"+id).attr("onclick","return false");
	                $("#excDate_"+id).attr("onclick","return false");
	                
	            }
	            
	        }
	        
	       
	      
	       
         
	    
	        function checkAllGroup(){
	            var isChecked = $('#allGroupCheck').is(":checked");
	            var cg_id_name = document.getElementsByName("cg_id");
	            if (isChecked) {
	                for( i=0;i<cg_id_name.length;i++){
	                    cg_id_name[i].checked = true;
	                    id = cg_id_name[i].value;
	                    document.getElementById("initial_Date_time"+id+"_yy").disabled = false;
	                    document.getElementById("initial_Date_time"+id+"_mm").disabled = false;
	                    document.getElementById("initial_Date_time"+id+"_dd").disabled = false;
	                    
	                    document.getElementById("expiry_Date_time"+id+"_yy").disabled = false;
	                    document.getElementById("expiry_Date_time"+id+"_mm").disabled = false;
	                    document.getElementById("expiry_Date_time"+id+"_dd").disabled = false;

	                    $("#iniDate_"+id).removeAttr("onclick");
	                    $("#excDate_"+id).removeAttr("onclick");
	                }
	            }else{
	                for( i=0;i<cg_id_name.length;i++){
	                    cg_id_name[i].checked = false;
	                    id = cg_id_name[i].value;
	                    document.getElementById("initial_Date_time"+id+"_yy").disabled = true;
	                    document.getElementById("initial_Date_time"+id+"_mm").disabled = true;
	                    document.getElementById("initial_Date_time"+id+"_dd").disabled = true;
	                    
	                    document.getElementById("expiry_Date_time"+id+"_yy").disabled = true;
	                    document.getElementById("expiry_Date_time"+id+"_mm").disabled = true;
	                    document.getElementById("expiry_Date_time"+id+"_dd").disabled = true;
	                    

	                    document.getElementById("initial_Date_time"+id+"_yy").value = "";
	                    document.getElementById("initial_Date_time"+id+"_mm").value = "";
	                    document.getElementById("initial_Date_time"+id+"_dd").value = "";
	                    
	                    document.getElementById("expiry_Date_time"+id+"_yy").value = "";
	                    document.getElementById("expiry_Date_time"+id+"_mm").value = "";
	                    document.getElementById("expiry_Date_time"+id+"_dd").value = "";
	                    

	                    $("#iniDate_"+id).attr("onclick","return false");
	                    $("#excDate_"+id).attr("onclick","return false");
	                }
	            }
	            
	        }
	    
	    
	    function confirm1(cg_id_name){
	    	 var cpdGroupRegistrations = new Array();
	            for( i=0;i<cg_id_name.length;i++){
	                if( cg_id_name[i].checked){
	                   id_con = cg_id_name[i].value;
	                   cpdGroupRegistrations.push({cgr_cg_id: id_con, cgr_initial_date: $("#initial_Date_time"+id_con).val(), cgr_expiry_date: $("#expiry_Date_time"+id_con).val(),cgr_usr_ent_id:$("#cr_usr_ent_id").val(),cgr_cr_id:$("#cr_id").val() });
	                }
	            }
	            $.ajax({
	                url : "${ctx}/app/admin/cpdRegistrationMgt/hasUpdExpiryDate",
	                type : 'POST',
	                data:JSON.stringify(cpdGroupRegistrations),
	                contentType: 'application/json',
	                traditional : true,
	                success : function(data) {
	                    if(typeof(data)== "string"){
	                        data =$.parseJSON(data);
	                    }
	                    var manage111 = '<lb:get key="label_cpd.label_core_cpt_d_management_111" /> ';
	                    if(data!=''){
	                         Dialog.confirm({text:manage111.replace(/@data/g, data), callback: function (answer) {//有过期日期被修改
	                                 if(!answer){
	                                     return;
	                                 }else{
	                                     confirm2(cg_id_name);
	                                 }
	                        }
	                        }); 
	                    }else{
	                        confirm2(cg_id_name);
	                    }
	                }
	             }); 
            
        }
	    
	       function confirm2(cg_id_name){
	           var cpdGroupRegistration = new Array();
	           for( i=0;i<cg_id_name.length;i++){
	               if( cg_id_name[i].checked){
	                  id_con = cg_id_name[i].value;
	                  cpdGroupRegistration.push({cgr_cg_id: id_con, cgr_initial_date: $("#initial_Date_time"+id_con).val(), cgr_expiry_date: $("#expiry_Date_time"+id_con).val(),cgr_usr_ent_id:$("#cr_usr_ent_id").val(),cgr_cr_id:$("#cr_id").val() });
	               }
	           }
	           $.ajax({
	               url : "${ctx}/app/admin/cpdRegistrationMgt/hasDelRegi?cgr_cr_ids="+$("#cr_id").val(),
	               type : 'POST',
	               data:JSON.stringify(cpdGroupRegistration),
	               contentType: 'application/json',
	               traditional : true,
	               success : function(data) {
	                   if(typeof(data)== "string"){
	                       data =$.parseJSON(data);
	                   }
                       var manage117 = '<lb:get key="label_cpd.label_core_cpt_d_management_117" /> ';
	                   if(data!=''){
	                        Dialog.confirm({text:manage117.replace(/@data/g, data), callback: function (answer) {//有小牌数据被删除
	                            if(!answer){
	                                return;
	                            }else{
	                                 submitLast(cg_id_name);
	                            }
	                       }
	                       }); 
	                   }else{
	                       submitLast(cg_id_name);
	                   }
	               }
	            }); 
	           
	       } 
		
	
	   function formSubmit(){
		  // var manage110 = '<lb:get key="label_cpd.label_core_cpt_d_management_110" />';
		   
			//alert(manage110.replace(/@data/g, "W3School"));
		   

	         //  return;
	        var frm = document.formCpdInfo;
	        var submitting = true;
	        
	        //检查注册用户是否已选择
	        /* if(frm.cr_usr_ent_id.value==''){
	        	Dialog.alert("请选择用户");
	        	return;
	        } */
	        
	        //注册号码检查
            if(frm.cr_reg_number.value==''){
                Dialog.alert( '<lb:get key="label_cpd.label_core_cpt_d_management_116" /> ');
            　frm.cr_reg_number.focus();
                return;
            }else{
            	var  positiveNum = /^[0-9a-zA-Z-_]*$/g;
       		    var pNum  = new RegExp(positiveNum);
            	var check_reg_number = pNum.test(frm.cr_reg_number.value);
            	if (!check_reg_number) { 
		                　　　　  Dialog.alert( '<lb:get key="label_cpd.label_core_cpt_d_management_231" /> '); 
		                　　　　  frm.cr_reg_number.focus();
		                　　　　  return;
                } 
            	if(frm.cr_reg_number.value.length > 80){
            		Dialog.alert('<lb:get key="label_cpd.label_core_cpt_d_management_221" /> '); 
		                　　　    　frm.cr_reg_number.focus();
		                　　　　    return;
            	}
            }
	        //注册时间检查
		    if(frm.cr_reg_datetime_yy && frm.cr_reg_datetime_mm && frm.cr_reg_datetime_dd){
		        if (frm.cr_reg_datetime_yy.value != '' || frm.cr_reg_datetime_mm.value != '' || frm.cr_reg_datetime_dd.value != '') {
		            if (!wbUtilsValidateDate("document.formCpdInfo.cr_reg_datetime", label_cpd.label_core_cpt_d_management_61)) {
		            	return;
		            }
		        }
		        if(frm.cr_reg_datetime_yy.value != '' && frm.cr_reg_datetime_mm.value != '' && frm.cr_reg_datetime_dd.value != '') {
		            frm.cr_reg_datetime.value = frm.cr_reg_datetime_yy.value + "-" + frm.cr_reg_datetime_mm.value + "-" + frm.cr_reg_datetime_dd.value;
		        }
		        if(frm.cr_reg_datetime.value == ''){
		        	Dialog.alert( '<lb:get key="label_cpd.label_core_cpt_d_management_109" /> ');
		        	return;
		        }
		    } 
	        //除牌时间检查
            if(frm.cr_de_reg_datetime_yy && frm.cr_de_reg_datetime_mm && frm.cr_de_reg_datetime_dd){
                if (frm.cr_de_reg_datetime_yy.value != '' || frm.cr_de_reg_datetime_mm.value != '' || frm.cr_de_reg_datetime_dd.value != '') {
                    if (!wbUtilsValidateDate("document.formCpdInfo.cr_de_reg_datetime", label_cpd.label_core_cpt_d_management_62)) {
                        return;
                    }
                }
                if(frm.cr_de_reg_datetime_yy.value != '' && frm.cr_de_reg_datetime_mm.value != '' && frm.cr_de_reg_datetime_dd.value != '') {
                    frm.cr_de_reg_datetime.value = frm.cr_de_reg_datetime_yy.value + "-" + frm.cr_de_reg_datetime_mm.value + "-" + frm.cr_de_reg_datetime_dd.value;
                }else{
                    frm.cr_de_reg_datetime.value='';
                } 
                if(frm.cr_de_reg_datetime.value!=''){
                	 if(frm.cr_reg_datetime.value>=frm.cr_de_reg_datetime.value){
                         Dialog.alert('<lb:get key="label_cpd.label_core_cpt_d_management_115" /> ');
                         return;
                     }
                }
            }
            //检查和加入组别的数据
             //var cpdGroupRegistration = new Array();
            var cg_id_name = document.getElementsByName("cg_id");
            var isSelectGroup = false;
            for( i=0;i<cg_id_name.length;i++){
                if( cg_id_name[i].checked){
                   id = cg_id_name[i].value;
                   
                   if($("#initial_Date_time"+id+"_yy") && $("#initial_Date_time"+id+"_mm") && $("#initial_Date_time"+id+"_dd")){
                       if ($("#initial_Date_time"+id+"_yy").val() != '' || $("#initial_Date_time"+id+"_mm").val() != '' || $("#initial_Date_time"+id+"_dd").val() != '') {
                           if (!wbUtilsValidateDate("document.formCpdInfo.initial_Date_time"+id, '<lb:get key="label_cpd.label_core_cpt_d_management_70" />')) {
                               return;
                           }
                       }
                       if($("#initial_Date_time"+id+"_yy").val() != '' && $("#initial_Date_time"+id+"_mm").val()  != '' && $("#initial_Date_time"+id+"_dd").val() != '') {
                           $("#initial_Date_time"+id).val($("#initial_Date_time"+id+"_yy").val() + "-" + $("#initial_Date_time"+id+"_mm").val()  + "-" + $("#initial_Date_time"+id+"_dd").val())+ ' 00:00:00.000';
                       }else{
                    	   $("#initial_Date_time"+id).val('');
                       }
                       if($("#initial_Date_time"+id).val() == '' && $("#initial_Date_time"+id).val() != ' 00:00:00.000'){
                           Dialog.alert('<lb:get key="label_cpd.label_core_cpt_d_management_114" /> ');
                           return;
                       }else if(frm.cr_reg_datetime.value>$("#initial_Date_time"+id).val()){
                           Dialog.alert('<lb:get key="label_cpd.label_core_cpt_d_management_80" /> ');
                           return;
                       }
                   } 
                   if($("#expiry_Date_time"+id+"_yy") && $("#expiry_Date_time"+id+"_mm") && $("#expiry_Date_time"+id+"_dd")){
                       if ($("#expiry_Date_time"+id+"_yy").val() != '' || $("#expiry_Date_time"+id+"_mm").val() != '' || $("#expiry_Date_time"+id+"_dd").val() != '') {
                           if (!wbUtilsValidateDate("document.formCpdInfo.expiry_Date_time"+id, '<lb:get key="label_cpd.label_core_cpt_d_management_71" />')) {
                               return;
                           }
                       }
                       if($("#expiry_Date_time"+id+"_yy").val() != '' && $("#expiry_Date_time"+id+"_mm").val()  != '' && $("#expiry_Date_time"+id+"_dd").val() != '') {
                           $("#expiry_Date_time"+id).val($("#expiry_Date_time"+id+"_yy").val() + "-" + $("#expiry_Date_time"+id+"_mm").val()  + "-" + $("#expiry_Date_time"+id+"_dd").val())+ ' 00:00:00.000';
                       }else{
                           $("#expiry_Date_time"+id).val('');
                       }
                       if(frm.cr_de_reg_datetime.value!=''){
                           if($("#expiry_Date_time"+id).val() == '' && $("#expiry_Date_time"+id).val() != ' 00:00:00.000'){
                               Dialog.alert('<lb:get key="label_cpd.label_core_cpt_d_management_81" /> ');//除牌日常不为空时，被选中的组别的到期日期不能为空
                               return;
                           }else if($("#expiry_Date_time"+id).val()>frm.cr_de_reg_datetime.value){
                               Dialog.alert('<lb:get key="label_cpd.label_core_cpt_d_management_81" /> ');//D、   所有组别的“到期日期”都不可以大于大牌“除牌日期”
                               return;
                           }
                       }
                       if($("#expiry_Date_time"+id).val() != '' && $("#expiry_Date_time"+id).val() != ' 00:00:00.000'){
                           if($("#initial_Date_time"+id).val()>=$("#expiry_Date_time"+id).val()){
                               Dialog.alert('<lb:get key="label_cpd.label_core_cpt_d_management_112" /> ');
                               return;
                           }
                       }
                   } 
                isSelectGroup = true;
               }
           } 

            //var expiry_Date_time=new Date(parseInt($("#expiry_Date_time"+id).val()));
            
             if(!isSelectGroup) {
            	 Dialog.alert('<lb:get key="label_cpd.label_core_cpt_d_management_283" /> ');
            	 return false;
             }
            
            var cpdGroupRegistrations = new Array();
            for( i=0;i<cg_id_name.length;i++){
                if( cg_id_name[i].checked){
                   id_con = cg_id_name[i].value;
                   cpdGroupRegistrations.push({cgr_cg_id: id_con, cgr_initial_date: $("#initial_Date_time"+id_con).val(), cgr_expiry_date: $("#expiry_Date_time"+id_con).val(),cgr_usr_ent_id:$("#cr_usr_ent_id").val(),cgr_cr_id:$("#cr_id").val() });
                }
            }
            $.ajax({
                url : "${ctx}/app/admin/cpdRegistrationMgt/hasUpdInitialDate",
                type : 'POST',
                data:JSON.stringify(cpdGroupRegistrations),
                contentType: 'application/json',
                traditional : true,
                success : function(data) {
                    if(typeof(data)== "string"){
                        data =$.parseJSON(data);
                    }
                    var manage110 = '<lb:get key="label_cpd.label_core_cpt_d_management_110" />';
                    if(data!=''){
                         Dialog.confirm({text: manage110.replace(/@data/g, data), callback: function (answer) {//小牌日期被修改
                                 if(!answer){
                                	 return;
                                 }else{
                                	 confirm1(cg_id_name);
                                 }
                        }
                        }); 
                    }else{
                    	confirm1(cg_id_name);
                    }
                }
             }); 
            
            
                        
            //$.toJSON(cpdGroupRegistration)
            //JSON.stringify(cpdGroupRegistration)
	    	      
	     }
	   
	   
	   function submitLast(cg_id_name){
		   $.ajax({
               url : "${ctx}/app/admin/cpdRegistrationMgt/updCpdRegistration",
               type : 'POST',
               data:$('#formCpdInfo').serialize(),
               dataType : 'json',
               traditional : true,
               success : function(data) {
                    if(data.status==1){
                        var cpdGroupRegistration = new Array();
                        for( i=0;i<cg_id_name.length;i++){
                            if( cg_id_name[i].checked){
                               id = cg_id_name[i].value;
                               cpdGroupRegistration.push({cgr_cg_id: id, cgr_initial_date: $("#initial_Date_time"+id).val(), cgr_expiry_date: $("#expiry_Date_time"+id).val(),cgr_usr_ent_id:$("#cr_usr_ent_id").val(),cgr_cr_id:data.cr_id });
                            }
                        }
                        if(cpdGroupRegistration.length>0){
                            $.ajax({
                                url : "${ctx}/app/admin/cpdRegistrationMgt/updCpdGroupRegistration",
                                type : 'POST',
                                data:JSON.stringify(cpdGroupRegistration),
                                contentType: 'application/json',
                                traditional : true,
                                success : function(result) {
                                    go('${ctx}/app/admin/cpdRegistrationMgt/info?cr_id='+data.cr_id);
                                    //Dialog.showSuccessMessage(location.href);
                                    //Dialog.alert("记录更新成功");
                                }
                             });      
                        }else{//没有被选中的小牌，删除该大牌注册记录下所有已注册的小牌记录
                            $.ajax({
                                url : "${ctx}/app/admin/cpdRegistrationMgt/delGroupRegistration",
                                type : 'POST',
                                data:{cgr_cr_id:data.cr_id},
                                dataType : 'json',
                                traditional : true,
                                success : function(result) {
                                    go('${ctx}/app/admin/cpdRegistrationMgt/info?cr_id='+data.cr_id);
                                    //Dialog.alert("记录更新成功");
                                }
                             });      
                        }
                         
                   }else if(data.status==2){
                       Dialog.alert('<lb:get key="label_cpd.label_core_cpt_d_management_113" /> ');//重复记录
                   } else if(data.status==3){
                       Dialog.alert('<lb:get key="label_cpd.label_core_cpt_d_management_31" /> ');//时间重叠
                   }
                    
               }
            });          
	   }

	   
	   function auto_focus_field(org_field,length,focus_field){
		   var org = $('#'+org_field).val();
		   if(document.all || document.getElementById!=null){
		   	 if ( event.keyCode != 9 && event.keyCode != 16 ){
		   		if ( org.length == length ){
		   			$('#'+focus_field).focus();
		   			$('#'+focus_field).select();
		   		}
		   	  } 
		    }
		 }
               
/*        var updating = false;//防止重复提交，开始为false
       if(updating){
           return;
       }
       
       updating = true; */
       
	</script>
	
</head>
<body>
	<%@ include file="../common/userTree.jsp"%>
    <script type="text/javascript" src="${ctx}/js/date-picker.js"></script>  
    
	<input type="hidden" name="belong_module" value="FTN_AMD_CPT_D_MGT"/>
	<title:get function="global.FTN_AMD_CPT_D_MGT"/>
    <ol class="breadcrumb wzb-breadcrumb">
        <li><a href="javascript:wb_utils_gen_home(true);"><i class="fa wzb-breadcrumb-home fa-home"></i><lb:get key="global.lab_menu_started" /></a></li>
        <li><a href="/app/admin/cpdRegistrationMgt/list"><lb:get key="global.FTN_AMD_CPT_D_LICENSE_LIST" /></a></li>
        <li><a href="javascript:go('/app/admin/cpdRegistrationMgt/info?cr_id=${detail.cr_id}');">${detail.user.usr_ste_usr_id} </a></li>
        <li class="active"><lb:get key="label_cpd.label_core_cpt_d_management_118" /></li>
       	
       	
       	
       	
       	
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
          <form id="formCpdInfo"  name="formCpdInfo" method="post" cssClass="form-horizontal"  action="${ctx}/app/admin/cpdRegistrationMgt/save" >
          
                              
                               <input type="hidden" name="cr_id" id="cr_id" value="${detail.cr_id}"/>
                               <input type="hidden" name="cr_ct_id" id="cr_ct_id" value="${detail.cr_ct_id}"/>
	         <table cellpadding="0" cellspacing="0" style="border-bottom:1px solid #ccc;">
	            <tbody>
	                <tr>
	                    <td class="text-left">
	                        <div style="" class="wzb-before"><lb:get key="label_cpd.label_core_cpt_d_management_64" /> </div>
	                    </td>
	                </tr>
	            </tbody>
	        </table>
        
        
            <table cellpadding="0" cellspacing="0"  class="margin-top20">
            <tbody>
                <tr>
                    <td class="wzb-form-label"><span class="wzb-form-star">*</span><lb:get key="label_cpd.label_core_cpt_d_management_58" />：</td>
                    <td class="wzb-form-control" >
                               <input type="hidden" name="cr_usr_ent_id" id="cr_usr_ent_id" value="${detail.cr_usr_ent_id}"/>
                               ${detail.user.usr_ste_usr_id}
                      </td>
                </tr>
                <tr>
                    <td class="wzb-form-label"><lb:get key="label_cpd.label_core_cpt_d_management_66" />：</td>
                    <td class="wzb-form-control"><span id="userIdFullName">${detail.user.usr_display_bil}</span>
                    </td>
                </tr>
                <tr>
                    <td class="wzb-form-label"><span class="wzb-form-star">*</span><lb:get key="label_cpd.label_core_cpt_d_management_3" />：</td>
                    <td class="wzb-form-control">
                        ${detail.cpdType.ct_license_type}
                    </td>
                </tr>
                <tr>
                    <td class="wzb-form-label"><span class="wzb-form-star">*</span><lb:get key="label_cpd.label_core_cpt_d_management_67" />：    </td>
                    <td class="wzb-form-control"><span id="ct_license_alias" name="ct_license_alias">${detail.cpdType.ct_license_alias} </span></td>
                </tr>
                <tr>
                    <td class="wzb-form-label"><span class="wzb-form-star">*</span><lb:get key="label_cpd.label_core_cpt_d_management_60" />：</td>
                    <td class="wzb-form-control" >
                              <input class="wzb-inputText"   value="${detail.cr_reg_number}"   name="cr_reg_number"    type="text"  style="width:300px;">
                      </td>
                </tr>
                <tr>
                    <td class="wzb-form-label"><span class="wzb-form-star">*</span><lb:get key="label_cpd.label_core_cpt_d_management_61" />：</td>
                    <td class="wzb-form-control" >
                              <input type="hidden" id="cr_reg_datetime" value="${detail.cr_reg_datetime}" name="cr_reg_datetime" />
                              <input type="text" id="cr_reg_datetime_yy" onkeyup="auto_focus_field('cr_reg_datetime_yy',4,'cr_reg_datetime_mm')" size="4" maxlength="4" class="wzb-inputText" value="<fmt:formatDate value="${detail.cr_reg_datetime }" pattern="yyyy"/>" name="cr_reg_datetime_yy" />  -
                              <input type="text" id="cr_reg_datetime_mm" onkeyup="auto_focus_field('cr_reg_datetime_mm',2,'cr_reg_datetime_dd')" size="2" maxlength="2" class="wzb-inputText" value="<fmt:formatDate value="${detail.cr_reg_datetime }" pattern="MM"/>" name="cr_reg_datetime_mm" /> -
                              <input type="text" id="cr_reg_datetime_dd" size="2" maxlength="2" class="wzb-inputText" value="<fmt:formatDate value="${detail.cr_reg_datetime }" pattern="dd"/>" name="cr_reg_datetime_dd" />
                              <lb:get key="label_cpd.label_core_cpt_d_management_272" />&nbsp;
                              <a onmouseout="window.status='';return true;" onmouseover="window.status='Date Picker';return true;"
                               href="javascript:show_calendar('cr_reg_datetime', '','','','${lang}','${ctx}../../../cw/skin4/images/gb/css/wb_ui.css');">
                               <img src="../../../wb_image/btn_calendar.gif" border="0" />
                               </a>
                      </td>
                </tr>
                <tr>
                    <td class="wzb-form-label"><lb:get key="label_cpd.label_core_cpt_d_management_62" />：</td>
                    <td class="wzb-form-control" >
                              <input type="hidden" id="cr_de_reg_datetime" value="" name="cr_de_reg_datetime" /> 
                              <input type="text" id="cr_de_reg_datetime_yy" onkeyup="auto_focus_field('cr_de_reg_datetime_yy',4,'cr_de_reg_datetime_mm')"  size="4" maxlength="4" class="wzb-inputText" 
                              value="<c:if test="${detail.cr_de_reg_datetime!=null}"><fmt:formatDate value="${detail.cr_de_reg_datetime }" pattern="yyyy"/></c:if>"
                               name="cr_de_reg_datetime_yy" />  -
                              <input type="text" id="cr_de_reg_datetime_mm" onkeyup="auto_focus_field('cr_de_reg_datetime_mm',2,'cr_de_reg_datetime_dd')"  size="2" maxlength="2" class="wzb-inputText" 
                              value="<c:if test="${detail.cr_de_reg_datetime!=null}"><fmt:formatDate value="${detail.cr_de_reg_datetime }" pattern="MM"/></c:if>" 
                              name="cr_de_reg_datetime_mm" /> -
                              <input type="text" id="cr_de_reg_datetime_dd" size="2" maxlength="2" class="wzb-inputText"
                              value="<c:if test="${detail.cr_de_reg_datetime!=null}"><fmt:formatDate value="${detail.cr_de_reg_datetime }" pattern="dd"/></c:if>"
                               name="cr_de_reg_datetime_dd" />
                              <lb:get key="label_cpd.label_core_cpt_d_management_272" />&nbsp;
                              <a onmouseout="window.status='';return true;" onmouseover="window.status='Date Picker';return true;"
                               href="javascript:show_calendar('cr_de_reg_datetime', '','','','${lang}','${ctx}../../../cw/skin4/images/gb/css/wb_ui.css');">
                               <img src="../../../wb_image/btn_calendar.gif" border="0" />
                               </a>
                      </td>
                </tr>
                <tr>
                    <td class="wzb-form-label" valign="top"></td>
                    <td class="wzb-form-control">
                        <span class="wzb-form-star">*<lb:get key="label_rm.label_core_requirements_management_35" /></span>
                    </td>
                </tr>
            </tbody>
        </table>
        <table cellpadding="0" cellspacing="0" style="border-bottom:1px solid #ccc;">
            <tbody>
                <tr>
                    <td class="text-left">
                        <div class="wzb-before "><lb:get key="label_cpd.label_core_cpt_d_management_68" /></div>
                    </td>
                </tr>
            </tbody>
        </table>

        
        <div class="datatable" style="margin-top:20px;">
            <div class="datatable-body">
                <table cellpadding="0" cellspacing="0" class="datatable-table">
                    <thead class="datatable-table-thead">
                        <tr class="datatable-table-row">
                            <th align="left" class="datatable-table-column-header text-left" width="1%"><input type="checkbox" value=""  id="allGroupCheck"  onclick="checkAllGroup()"></th>
                            <th width="33%" class="datatable-table-column-header text-left"><lb:get key="label_cpd.label_core_cpt_d_management_69" /></th>
                            <th width="33%" class="datatable-table-column-header text-center"><lb:get key="label_cpd.label_core_cpt_d_management_70" /></th>
                            <th width="33%" class="datatable-table-column-header text-center"><lb:get key="label_cpd.label_core_cpt_d_management_71" /></th>
                        </tr>
                    </thead>
                    <tbody class="datatable-table-tbody"  id="tableContent">
	                    <c:forEach items="${cList}"  var="cpdGroup">
	                    <c:choose>
	                    <c:when test="${cpdGroup.cpdGroupRegistration.cgr_usr_ent_id !=0 }"><!-- 已报名的小牌 -->
	                           <tr class="datatable-table-row">
                                    <td class="datatable-table-column text-left"><input type="checkbox"  checked="checked" value="${cpdGroup.cg_id }" onclick="javascript:checkOneActive(this.value,this.checked);"  name="cg_id"></td>
                                    <td class="datatable-table-column text-left">${cpdGroup.cg_alias }</td>
                                    <td class="datatable-table-column text-center">
                                        <input type="hidden" id="initial_Date_time${cpdGroup.cg_id}" value="${cpdGroup.cpdGroupRegistration.cgr_initial_date}" name="initial_Date_time${cpdGroup.cg_id}" />
                                        <input type="text" id="initial_Date_time${cpdGroup.cg_id }_yy" onkeyup="auto_focus_field('initial_Date_time${cpdGroup.cg_id }_yy',4,'initial_Date_time${cpdGroup.cg_id }_mm')" size="4" maxlength="4" class="wzb-inputText" value="<c:if test="${cpdGroup.cpdGroupRegistration.cgr_initial_date!=null}"><fmt:formatDate value="${cpdGroup.cpdGroupRegistration.cgr_initial_date }" pattern="yyyy"/></c:if>" name="initial_Date_time${cpdGroup.cg_id }_yy" />  -
                                        <input type="text" id="initial_Date_time${cpdGroup.cg_id }_mm" onkeyup="auto_focus_field('initial_Date_time${cpdGroup.cg_id }_mm',2,'initial_Date_time${cpdGroup.cg_id }_dd')" size="2" maxlength="2" class="wzb-inputText" value="<c:if test="${cpdGroup.cpdGroupRegistration.cgr_initial_date!=null}"><fmt:formatDate value="${cpdGroup.cpdGroupRegistration.cgr_initial_date }" pattern="MM"/></c:if>" name="initial_Date_time${cpdGroup.cg_id }_mm" /> -
                                        <input type="text" id="initial_Date_time${cpdGroup.cg_id }_dd" size="2" maxlength="2" class="wzb-inputText" value="<c:if test="${cpdGroup.cpdGroupRegistration.cgr_initial_date!=null}"><fmt:formatDate value="${cpdGroup.cpdGroupRegistration.cgr_initial_date }" pattern="dd"/></c:if>" name="initial_Date_time${cpdGroup.cg_id }_dd" />
                                         <lb:get key="label_cpd.label_core_cpt_d_management_272" />&nbsp;
                                         <a onmouseout="window.status='';return true;" onmouseover="window.status='Date Picker';return true;"   id="iniDate_${cpdGroup.cg_id}"  
                                            href="javascript:show_calendar('initial_Date_time'+${cpdGroup.cg_id }, '','','','${lang}','${ctx}../../../cw/skin4/images/gb/css/wb_ui.css');">
                                         <img src="../../../wb_image/btn_calendar.gif" border="0" />
                                        </a>
                                    </td>
                                    <td class="datatable-table-column text-center">
                                        <input type="hidden" id="expiry_Date_time${cpdGroup.cg_id}" value="" name="expiry_Date_time${cpdGroup.cg_id}" />
                                        <input type="text" id="expiry_Date_time${cpdGroup.cg_id }_yy" onkeyup="auto_focus_field('expiry_Date_time${cpdGroup.cg_id }_yy',4,'expiry_Date_time${cpdGroup.cg_id }_mm')" size="4" maxlength="4" class="wzb-inputText" value="<c:if test="${cpdGroup.cpdGroupRegistration.cgr_expiry_date!=null}"><fmt:formatDate value="${cpdGroup.cpdGroupRegistration.cgr_expiry_date}" pattern="yyyy"/></c:if>" name="expiry_Date_time${cpdGroup.cg_id }_yy" />  -
                                        <input type="text" id="expiry_Date_time${cpdGroup.cg_id }_mm" onkeyup="auto_focus_field('expiry_Date_time${cpdGroup.cg_id }_mm',2,'expiry_Date_time${cpdGroup.cg_id }_dd')" size="2" maxlength="2" class="wzb-inputText" value="<c:if test="${cpdGroup.cpdGroupRegistration.cgr_expiry_date!=null}"><fmt:formatDate value="${cpdGroup.cpdGroupRegistration.cgr_expiry_date }" pattern="MM"/></c:if>" name="expiry_Date_time${cpdGroup.cg_id }_mm" /> -
                                        <input type="text" id="expiry_Date_time${cpdGroup.cg_id }_dd" size="2" maxlength="2" class="wzb-inputText" value="<c:if test="${cpdGroup.cpdGroupRegistration.cgr_expiry_date!=null}"><fmt:formatDate value="${cpdGroup.cpdGroupRegistration.cgr_expiry_date }" pattern="dd"/></c:if>" name="expiry_Date_time${cpdGroup.cg_id }_dd" />
                                         <lb:get key="label_cpd.label_core_cpt_d_management_272" />&nbsp;
                                         <a onmouseout="window.status='';return true;" onmouseover="window.status='Date Picker';return true;"  id="excDate_${cpdGroup.cg_id}" 
                                            href="javascript:show_calendar('expiry_Date_time'+${cpdGroup.cg_id }, '','','','${lang}','${ctx}../../../cw/skin4/images/gb/css/wb_ui.css');">
                                         <img src="../../../wb_image/btn_calendar.gif" border="0" />
                                        </a>
                                    </td>
                                </tr>
	                    </c:when>
	                    <c:otherwise><!-- 未报名的小牌 -->
	                           <tr class="datatable-table-row">
                                    <td class="datatable-table-column text-left"><input type="checkbox"  value="${cpdGroup.cg_id }"  onclick="javascript:checkOneActive(this.value,this.checked);"  name="cg_id"></td>
                                    <td class="datatable-table-column text-left">${cpdGroup.cg_alias }</td>
                                    <td class="datatable-table-column text-center">
                                        <input type="hidden" id="initial_Date_time${cpdGroup.cg_id}" value="" name="initial_Date_time${cpdGroup.cg_id}" />
                                        <input type="text" id="initial_Date_time${cpdGroup.cg_id }_yy" size="4" maxlength="4" class="wzb-inputText" value=""  disabled="disabled"  name="initial_Date_time${cpdGroup.cg_id }_yy" />  -
                                        <input type="text" id="initial_Date_time${cpdGroup.cg_id }_mm" size="2" maxlength="2" class="wzb-inputText" value="" disabled="disabled"  name="initial_Date_time${cpdGroup.cg_id }_mm" /> -
                                        <input type="text" id="initial_Date_time${cpdGroup.cg_id }_dd" size="2" maxlength="2" class="wzb-inputText" value=""  disabled="disabled"  name="initial_Date_time${cpdGroup.cg_id }_dd" />
                                         <lb:get key="label_cpd.label_core_cpt_d_management_272" />&nbsp;
                                         <a onmouseout="window.status='';return true;" onmouseover="window.status='Date Picker';return true;"   id="iniDate_${cpdGroup.cg_id}"  onclick="return false;"
                                            href="javascript:show_calendar('initial_Date_time'+${cpdGroup.cg_id }, '','','','${lang}','${ctx}../../../cw/skin4/images/gb/css/wb_ui.css');">
                                         <img src="../../../wb_image/btn_calendar.gif" border="0" />
                                        </a>
                                    </td>
                                    <td class="datatable-table-column text-center">
                                        <input type="hidden" id="expiry_Date_time${cpdGroup.cg_id}" value="" name="expiry_Date_time${cpdGroup.cg_id}" />
                                        <input type="text" id="expiry_Date_time${cpdGroup.cg_id }_yy" size="4" maxlength="4" class="wzb-inputText" value=""  disabled="disabled"   name="expiry_Date_time${cpdGroup.cg_id }_yy" />  -
                                        <input type="text" id="expiry_Date_time${cpdGroup.cg_id }_mm" size="2" maxlength="2" class="wzb-inputText" value=""  disabled="disabled"   name="expiry_Date_time${cpdGroup.cg_id }_mm" /> -
                                        <input type="text" id="expiry_Date_time${cpdGroup.cg_id }_dd" size="2" maxlength="2" class="wzb-inputText" value=""  disabled="disabled"   name="expiry_Date_time${cpdGroup.cg_id }_dd" />
                                         <lb:get key="label_cpd.label_core_cpt_d_management_272" />&nbsp;
                                         <a onmouseout="window.status='';return true;" onmouseover="window.status='Date Picker';return true;"  id="excDate_${cpdGroup.cg_id}"  onclick="return false;"
                                            href="javascript:show_calendar('expiry_Date_time'+${cpdGroup.cg_id }, '','','','${lang}','${ctx}../../../cw/skin4/images/gb/css/wb_ui.css');">
                                         <img src="../../../wb_image/btn_calendar.gif" border="0" />
                                        </a>
                                    </td>
                                </tr>
	                    </c:otherwise>
	                    </c:choose>
	                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
        <div class="wzb-bar">
            <button type="button" onclick="javascript:formSubmit();" class="btn wzb-btn-blue margin-right10 wzb-btn-big">
				<lb:get key="global.button_ok" />
			</button>
			<button type="button" onclick="javascript:go('/app/admin/cpdRegistrationMgt/info?cr_id=${detail.cr_id}');" class="btn wzb-btn-blue wzb-btn-big">
				<lb:get key="global.button_cancel" />
			</button>
        </div>  
		</form>
        </div>
         <!-- 内容添加结束 -->
    </div>
    <!-- wzb-panel End-->
    
<script id="group-template" type="text/x-jsrender">
             <tr class="datatable-table-row">
             <td class="datatable-table-column text-left"><input type="checkbox" value="{{>cg_id}}"  name="cg_id"></td>
             <td class="datatable-table-column text-left">{{>cg_alias}}</td>
              <td class="datatable-table-column text-center">
                          <input type="hidden" id="initial_Date_time{{>cg_id}}" value="" name="initial_Date_time{{>cg_id}}" />
                          <input type="text" id="initial_Date_time{{>cg_id}}_yy" size="4" maxlength="4" class="wzb-inputText" value="" name="initial_Date_time{{>cg_id}}_yy" />  -
                          <input type="text" id="initial_Date_time{{>cg_id}}_mm" size="2" maxlength="2" class="wzb-inputText" value="" name="initial_Date_time{{>cg_id}}_mm" /> -
                          <input type="text" id="initial_Date_time{{>cg_id}}_dd" size="2" maxlength="2" class="wzb-inputText" value="" name="initial_Date_time{{>cg_id}}_dd" />
                           <lb:get key="label_cpd.label_core_cpt_d_management_272" />&nbsp;
                          <a onmouseout="window.status='';return true;" onmouseover="window.status='Date Picker';return true;"
                            href="javascript:show_calendar('initial_Date_time{{>cg_id}}', '','','','${lang}','${ctx}../../../cw/skin4/images/gb/css/wb_ui.css');">
                          <img src="../../../wb_image/btn_calendar.gif" border="0" />
                          </a>
             </td>
             <td class="datatable-table-column text-center">
                          <input type="hidden" id="expiry_Date_time{{>cg_id}}" value="" name="expiry_Date_time{{>cg_id}}" />
                          <input type="text" id="expiry_Date_time{{>cg_id}}_yy" size="4" maxlength="4" class="wzb-inputText" value="" name="expiry_Date_time{{>cg_id}}_yy" />  -
                          <input type="text" id="expiry_Date_time{{>cg_id}}_mm" size="2" maxlength="2" class="wzb-inputText" value="" name="expiry_Date_time{{>cg_id}}_mm" /> -
                          <input type="text" id="expiry_Date_time{{>cg_id}}_dd" size="2" maxlength="2" class="wzb-inputText" value="" name="expiry_Date_time{{>cg_id}}_dd" />
                          <lb:get key="label_cpd.label_core_cpt_d_management_272" />&nbsp;
                          <a onmouseout="window.status='';return true;" onmouseover="window.status='Date Picker';return true;"
                            href="javascript:show_calendar('expiry_Date_time{{>cg_id}}', '','','','${lang}','${ctx}../../../cw/skin4/images/gb/css/wb_ui.css');">
                           <img src="../../../wb_image/btn_calendar.gif" border="0" />
                           </a>
                 </td>
                 </tr>

</script>
</body>

</html>