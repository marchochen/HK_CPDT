<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.cwn.wizbank.entity.*,java.util.*,com.cw.wizbank.util.*, com.cw.wizbank.qdb.*"%>
<%@ include file="../../common/taglibs.jsp"%>

<!DOCTYPE html>
<html>
<head>
    <script type="text/javascript" src="${ctx}/js/wb_utils.js"></script>
	<script type="text/javascript" src="${ctx}/js/jquery.selector.js"></script>
	<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_cpd_${lang}.js"></script>
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
                        $("#ct_license_alias").html(data.cpdType.ct_license_alias);
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
		
	   var updating = false;//防止重复提交，开始为false
	   function formSubmit(){
	      /*  if(updating){
	           return;
	       } */
		    
	        var frm = document.formCpdInfo;
	        
	        
	        //检查注册用户是否已选择
	        if(frm.cr_usr_ent_id.value==''){
	        	Dialog.alert('<lb:get key="label_cpd.label_core_cpt_d_management_65" /> ');
	        	return;
	        }

	        //检查注册牌照类型是否已选择
            if($('#cr_ct_id option:selected') .val()==0){
                Dialog.alert('<lb:get key="label_cpd.label_core_cpt_d_management_139" /> ');
                return;
            }
	        
	        
            if(frm.cr_reg_number.value==''){
                Dialog.alert('<lb:get key="label_cpd.label_core_cpt_d_management_116" /> ');
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
		        	Dialog.alert('<lb:get key="label_cpd.label_core_cpt_d_management_109" /> ');
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
             
             if(!isSelectGroup) {
            	 Dialog.alert('<lb:get key="label_cpd.label_core_cpt_d_management_283" /> ');
            	 return false;
             }
             
            //$.toJSON(cpdGroupRegistration)
            //JSON.stringify(cpdGroupRegistration)
	    	       $.ajax({
			        url : "${ctx}/app/admin/cpdRegistrationMgt/save",
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
	                         $.ajax({
	                            url : "${ctx}/app/admin/cpdRegistrationMgt/saveCpdGroupRegi",
	                            type : 'POST',
	                            data:JSON.stringify(cpdGroupRegistration),
	                            contentType: 'application/json',
	                            traditional : true,
	                            success : function(result) {
                                    //Dialog.alert("记录添加成功");
                                    go('${ctx}/app/admin/cpdRegistrationMgt/info?cr_id='+data.cr_id);
	                            	//url = wb_utils_controller_base + "admin/cpdRegistrationMgt/list";
	                            	//window.location.href =  url;
	                            }
	                         });     
			        	}else if(data.status==2){
			        		Dialog.alert('<lb:get key="label_cpd.label_core_cpt_d_management_113" /> ');//重复记录
			        	}else if(data.status==3){
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
        <li class="active">
        	<c:choose>
	        	<c:when test="${type == 'update' }">
	        		<%-- <lb:get key="label_cpd.label_core_cpt_d_management_12" /> --%>
	        	</c:when>
	        	<c:otherwise>
	        		<lb:get key="label_cpd.label_core_cpt_d_management_63" /> 
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
          <form id="formCpdInfo"  name="formCpdInfo" method="post" cssClass="form-horizontal"  action="${ctx}/app/admin/cpdRegistrationMgt/save" >
          
                              
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
                               <input type="hidden" name="cr_usr_ent_id" id="cr_usr_ent_id" value=""/>
                               <input  type="text" class="form-control valid"  id="userName" placeholder="<lb:get key="label_cpd.label_core_cpt_d_management_65" />" readonly="readonly" name="name" value="" style="width:260px;height: 30px"><input type="button"  id="user_choose_btn"  name="" value="<lb:get key="button_select"/>" class="btn wzb-btn-blue margin-right4 xueyuan" style=" margin-bottom:2px;height:30px;margin-top:0;">
                              <label class="error" for="gid" id="error_g1" style="display:none"></label>
                      </td>
                </tr>
                <tr>
                    <td class="wzb-form-label"><lb:get key="label_cpd.label_core_cpt_d_management_66" />：</td>
                    <td class="wzb-form-control"><span id="userIdFullName">--</span>
                    </td>
                </tr>
                <tr>
                    <td class="wzb-form-label"><span class="wzb-form-star">*</span><lb:get key="label_cpd.label_core_cpt_d_management_3" />：</td>
                    <td class="wzb-form-control">
                        <select name="cr_ct_id"  id = "cr_ct_id"  onchange="reloadTable()">
                        <option value="0"><lb:get key="label_cpd.label_core_cpt_d_management_139" /></option>
	                    <c:forEach items="${cpdTypeList}"  var="cpdType">
	                      <option value="${cpdType.ct_id }">${cpdType.ct_license_type }</option>
	                    </c:forEach>
                </select>
                    </td>
                </tr>
                <tr>
                    <td class="wzb-form-label"><span class="wzb-form-star">*</span><lb:get key="label_cpd.label_core_cpt_d_management_67" />：    </td>
                    <td class="wzb-form-control"><span id="ct_license_alias" name="ct_license_alias">--</span></td>
                </tr>
                <tr>
                    <td class="wzb-form-label"><span class="wzb-form-star">*</span><lb:get key="label_cpd.label_core_cpt_d_management_60" />：</td>
                    <td class="wzb-form-control" >
                              <input class="wzb-inputText"   value=""   name="cr_reg_number"    type="text"  style="width:300px;">
                      </td>
                </tr>
                <tr>
                    <td class="wzb-form-label"><span class="wzb-form-star">*</span><lb:get key="label_cpd.label_core_cpt_d_management_61" />：</td>
                    <td class="wzb-form-control" >
                              <input type="hidden" id="cr_reg_datetime" value="" name="cr_reg_datetime" />
                              <input type="text" id="cr_reg_datetime_yy" onkeyup="auto_focus_field('cr_reg_datetime_yy',4,'cr_reg_datetime_mm')" size="4" maxlength="4" class="wzb-inputText" value="" name="cr_reg_datetime_yy" />  -
                              <input type="text" id="cr_reg_datetime_mm" onkeyup="auto_focus_field('cr_reg_datetime_mm',2,'cr_reg_datetime_dd')" size="2" maxlength="2" class="wzb-inputText" value="" name="cr_reg_datetime_mm" /> -
                              <input type="text" id="cr_reg_datetime_dd" size="2" maxlength="2" class="wzb-inputText" value="" name="cr_reg_datetime_dd" />
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
                              <input type="text" id="cr_de_reg_datetime_yy" onkeyup="auto_focus_field('cr_de_reg_datetime_yy',4,'cr_de_reg_datetime_mm')" size="4" maxlength="4" class="wzb-inputText" value="" name="cr_de_reg_datetime_yy" />  -
                              <input type="text" id="cr_de_reg_datetime_mm" onkeyup="auto_focus_field('cr_de_reg_datetime_mm',2,'cr_de_reg_datetime_dd')" size="2" maxlength="2" class="wzb-inputText" value="" name="cr_de_reg_datetime_mm" /> -
                              <input type="text" id="cr_de_reg_datetime_dd" size="2" maxlength="2" class="wzb-inputText" value="" name="cr_de_reg_datetime_dd" />
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
                    <tr><td colspan="10"><div class="losedata"><i class="fa fa-folder-open-o"></i><p><lb:get key="lab_table_empty_cpd" /></p></div></td></tr>
                    
	                    <%-- <c:forEach items="${cpdGroupList}"  var="cpdGroup">
	                          <tr class="datatable-table-row">
		                            <td class="datatable-table-column text-left"><input type="checkbox"  value="${cpdGroup.cg_id }"  name="cg_id"  onclick="javascript:checkOneActive(this.value,this.checked);"></td>
		                            <td class="datatable-table-column text-left">${cpdGroup.cg_alias }</td>
		                            <td class="datatable-table-column text-center">
		                                <input type="hidden" id="initial_Date_time${cpdGroup.cg_id}" value="" name="initial_Date_time${cpdGroup.cg_id}" />
                                        <input type="text" id="initial_Date_time${cpdGroup.cg_id }_yy" size="4" maxlength="4" class="wzb-inputText" value=""   disabled="disabled"  name="initial_Date_time${cpdGroup.cg_id }_yy" />  -
                                        <input type="text" id="initial_Date_time${cpdGroup.cg_id }_mm" size="2" maxlength="2" class="wzb-inputText" value=""   disabled="disabled"  name="initial_Date_time${cpdGroup.cg_id }_mm" /> -
                                        <input type="text" id="initial_Date_time${cpdGroup.cg_id }_dd" size="2" maxlength="2" class="wzb-inputText" value=""   disabled="disabled" name="initial_Date_time${cpdGroup.cg_id }_dd" />
		                                 年-月-日&nbsp;
			                             <a onmouseout="window.status='';return true;" onmouseover="window.status='Date Picker';return true;"  id="iniDate_${cpdGroup.cg_id}"  onclick="return false;"
	                                        href="javascript:show_calendar('initial_Date_time'+${cpdGroup.cg_id }, '','','','${lang}','${ctx}../../../cw/skin4/images/gb/css/wb_ui.css');">
	                                     <img src="../../../wb_image/btn_calendar.gif" border="0" />
	                                    </a>
		                            </td>
		                            <td class="datatable-table-column text-center">
		                                <input type="hidden" id="expiry_Date_time${cpdGroup.cg_id}" value="" name="expiry_Date_time${cpdGroup.cg_id}" />
                                        <input type="text" id="expiry_Date_time${cpdGroup.cg_id }_yy" size="4" maxlength="4" class="wzb-inputText" value=""   disabled="disabled" name="expiry_Date_time${cpdGroup.cg_id }_yy" />  -
                                        <input type="text" id="expiry_Date_time${cpdGroup.cg_id }_mm" size="2" maxlength="2" class="wzb-inputText" value=""   disabled="disabled" name="expiry_Date_time${cpdGroup.cg_id }_mm" /> -
                                        <input type="text" id="expiry_Date_time${cpdGroup.cg_id }_dd" size="2" maxlength="2" class="wzb-inputText" value=""   disabled="disabled" name="expiry_Date_time${cpdGroup.cg_id }_dd" />
                                         年-月-日&nbsp;
                                         <a onmouseout="window.status='';return true;" onmouseover="window.status='Date Picker';return true;"    id="excDate_${cpdGroup.cg_id}"  onclick="return false;"
                                            href="javascript:show_calendar('expiry_Date_time'+${cpdGroup.cg_id }, '','','','${lang}','${ctx}../../../cw/skin4/images/gb/css/wb_ui.css');">
                                         <img src="../../../wb_image/btn_calendar.gif" border="0" />
                                        </a>
		                            </td>
		                        </tr>
	                    </c:forEach> --%>
                    </tbody>
                </table>
            </div>
        </div>
        <div class="wzb-bar">
            <button type="button" onclick="javascript:formSubmit();" class="btn wzb-btn-blue margin-right10 wzb-btn-big">
				<lb:get key="global.button_ok" />
			</button>
			<button type="button"  onclick="javascript:go('${ctx}/app/admin/cpdRegistrationMgt/list')" class="btn wzb-btn-blue wzb-btn-big">
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
             <td class="datatable-table-column text-left"><input type="checkbox" value="{{>cg_id}}"  onclick="javascript:checkOneActive(this.value,this.checked);"  name="cg_id"></td>
             <td class="datatable-table-column text-left">{{>cg_alias}}</td>
              <td class="datatable-table-column text-center">
                          <input type="hidden" id="initial_Date_time{{>cg_id}}" value="" name="initial_Date_time{{>cg_id}}" />
                          <input type="text" id="initial_Date_time{{>cg_id}}_yy" onkeyup="auto_focus_field('initial_Date_time{{>cg_id}}_yy',4,'initial_Date_time{{>cg_id}}_mm')" size="4" maxlength="4" class="wzb-inputText" value="" disabled="disabled"   name="initial_Date_time{{>cg_id}}_yy" />  -
                          <input type="text" id="initial_Date_time{{>cg_id}}_mm" onkeyup="auto_focus_field('initial_Date_time{{>cg_id}}_mm',2,'initial_Date_time{{>cg_id}}_dd')" size="2" maxlength="2" class="wzb-inputText" value=""  disabled="disabled"  name="initial_Date_time{{>cg_id}}_mm" /> -
                          <input type="text" id="initial_Date_time{{>cg_id}}_dd" size="2" maxlength="2" class="wzb-inputText" value=""  disabled="disabled"  name="initial_Date_time{{>cg_id}}_dd" />
                           <lb:get key="label_cpd.label_core_cpt_d_management_272" />&nbsp;
                          <a onmouseout="window.status='';return true;" onmouseover="window.status='Date Picker';return true;"  id="iniDate_{{>cg_id}}"  onclick="return false;"
                            href="javascript:show_calendar('initial_Date_time{{>cg_id}}', '','','','${lang}','${ctx}../../../cw/skin4/images/gb/css/wb_ui.css');">
                          <img src="../../../wb_image/btn_calendar.gif" border="0" />
                          </a>
             </td>
             <td class="datatable-table-column text-center">
                          <input type="hidden" id="expiry_Date_time{{>cg_id}}" value="" name="expiry_Date_time{{>cg_id}}" />
                          <input type="text" id="expiry_Date_time{{>cg_id}}_yy" onkeyup="auto_focus_field('expiry_Date_time{{>cg_id}}_yy',4,'expiry_Date_time{{>cg_id}}_mm')" size="4" maxlength="4" class="wzb-inputText" value=""  disabled="disabled"  name="expiry_Date_time{{>cg_id}}_yy" />  -
                          <input type="text" id="expiry_Date_time{{>cg_id}}_mm" onkeyup="auto_focus_field('expiry_Date_time{{>cg_id}}_mm',2,'expiry_Date_time{{>cg_id}}_dd')" size="2" maxlength="2" class="wzb-inputText" value="" disabled="disabled"   name="expiry_Date_time{{>cg_id}}_mm" /> -
                          <input type="text" id="expiry_Date_time{{>cg_id}}_dd" size="2" maxlength="2" class="wzb-inputText" value=""  disabled="disabled"  name="expiry_Date_time{{>cg_id}}_dd" />
                         <lb:get key="label_cpd.label_core_cpt_d_management_272" />&nbsp;
                          <a onmouseout="window.status='';return true;" onmouseover="window.status='Date Picker';return true;"    id="excDate_{{>cg_id}}"  onclick="return false;"
                            href="javascript:show_calendar('expiry_Date_time{{>cg_id}}', '','','','${lang}','${ctx}../../../cw/skin4/images/gb/css/wb_ui.css');">
                           <img src="../../../wb_image/btn_calendar.gif" border="0" />
                           </a>
                 </td>
                 </tr>

</script>
</body>

</html>