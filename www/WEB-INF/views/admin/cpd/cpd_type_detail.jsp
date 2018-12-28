<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<%@ include file="../../common/template.jsp"%>
<!DOCTYPE html>
<html>
<head>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_cpd_${lang}.js"></script>
<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-admin.css" />
<script>
		$(function(){ 
		}); 
		
		window.onload = function(){
		    var month_1 = $('#month_1').val();　
		    var label_1 = getMonthLabel(month_1);
		    $("#div_month_1").text(label_1);
		     　  var month_2 = $('#month_2').val();　
		    var label_2 = getMonthLabel(month_2);
		    $("#div_month_2").text(label_2);
		   　    var month_3 = $('#month_3').val();　
	    	var label_3 = getMonthLabel(month_3);
	    	$("#div_month_3").text(label_3);
		} 
		
		
		function getMonthLabel(month){
			if(month == 1){
				return fetchLabel('label_core_cpt_d_management_222');
			}else if(month == 2){
				return fetchLabel('label_core_cpt_d_management_223');
			}else if(month == 3){
				return fetchLabel('label_core_cpt_d_management_224');
			}else if(month == 4){
				return fetchLabel('label_core_cpt_d_management_225');
			}else if(month == 5){
				return fetchLabel('label_core_cpt_d_management_226');
			}else if(month == 6){
				return fetchLabel('label_core_cpt_d_management_227');
			}else if(month == 7){
				return fetchLabel('label_core_cpt_d_management_228');
			}else if(month == 8){
				return fetchLabel('label_core_cpt_d_management_229');
			}else if(month == 9){
				return fetchLabel('label_core_cpt_d_management_230');
			}else if(month == 10){
				return fetchLabel('label_core_cpt_d_management_42');
			}else if(month == 11){
				return fetchLabel('label_core_cpt_d_management_43');
			}else if(month == 12){
				return fetchLabel('label_core_cpt_d_management_44');
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
  <li class="active">${cpd.ct_license_type}</li>
</ol> <!-- wzb-breadcrumb End-->

<div class="panel wzb-panel">
    <!-- 内容添加开始 -->
    <div class="panel-body">
        <table cellpadding="0" cellspacing="0" style="border-bottom:1px solid #ccc;">
            <tbody>
                <tr>
                    <td class="text-left" width="50%">
                        <span style="" class="wzb-before"><lb:get key="label_cpd.label_core_cpt_d_management_64" /></span>
                    </td>
                    <td class="text-right" width="50%">
                        <button class="btn wzb-btn-orange margin-right4" onclick="javascript:go('${ctx}/app/admin/cpdManagement/insert?ct_id='+wbEncrytor().cwnEncrypt(${cpd.ct_id})+'')">
                           <lb:get key="global.button_update" />   
                        </button>
                        <button class="btn wzb-btn-orange" id="delete" onclick="javascript:del('${cpd.ct_id}')">
                            <lb:get key="global.button_del" />
                        </button>
                    </td>
                </tr>
            </tbody>
        </table>

        <table cellpadding="0" cellspacing="0" class="margin-top20">
            <tbody>
                <tr>
                    <td class="wzb-form-label"><lb:get key="label_cpd.label_core_cpt_d_management_3" />：</td>
                    <td class="wzb-form-control">${cpd.ct_license_type}</td>
                </tr>
                <tr>
                    <td class="wzb-form-label"><lb:get key="label_cpd.label_core_cpt_d_management_4" />：</td>
                    <td class="wzb-form-control">${cpd.ct_license_alias}</td>
                </tr>
                <tr>
                    <td class="wzb-form-label"><lb:get key="label_cpd.label_core_cpt_d_management_5" />：</td>
                    <td class="wzb-form-control">
                    	 <c:choose>
                           	   <c:when test="${cpd.ct_starting_month == 1}"><lb:get key="label_cpd.label_core_cpt_d_management_222" /></c:when>
                          	   <c:when test="${cpd.ct_starting_month == 2}"><lb:get key="label_cpd.label_core_cpt_d_management_223" /></c:when>
                               <c:when test="${cpd.ct_starting_month == 3}"><lb:get key="label_cpd.label_core_cpt_d_management_224" /></c:when>
                          	   <c:when test="${cpd.ct_starting_month == 4}"><lb:get key="label_cpd.label_core_cpt_d_management_225" /></c:when>
                          	   <c:when test="${cpd.ct_starting_month == 5}"><lb:get key="label_cpd.label_core_cpt_d_management_226" /></c:when>
                          	   <c:when test="${cpd.ct_starting_month == 6}"><lb:get key="label_cpd.label_core_cpt_d_management_227" /></c:when>
                          	   <c:when test="${cpd.ct_starting_month == 7}"><lb:get key="label_cpd.label_core_cpt_d_management_228" /></c:when>
                          	   <c:when test="${cpd.ct_starting_month == 8}"><lb:get key="label_cpd.label_core_cpt_d_management_229" /></c:when>
                          	   <c:when test="${cpd.ct_starting_month == 9}"><lb:get key="label_cpd.label_core_cpt_d_management_230" /></c:when>
                          	   <c:when test="${cpd.ct_starting_month == 10}"><lb:get key="label_cpd.label_core_cpt_d_management_42" /></c:when>
                          	   <c:when test="${cpd.ct_starting_month == 11}"><lb:get key="label_cpd.label_core_cpt_d_management_43" /></c:when>
                          	   <c:when test="${cpd.ct_starting_month == 12}"><lb:get key="label_cpd.label_core_cpt_d_management_44" /></c:when>
                           </c:choose>
                    </td>
                </tr>
                <tr>
                    <td class="wzb-form-label" valign="top"><lb:get key="label_cpd.label_core_cpt_d_management_14" />：</td>
                    <td class="wzb-form-control">
                        <c:choose>
                           <c:when test="${cpd.ct_award_hours_type == 1}">
                               <lb:get key="label_cpd.label_core_cpt_d_management_18" />
                           </c:when>
                           <c:when test="${cpd.ct_award_hours_type == 2}">
                               <lb:get key="label_cpd.label_core_cpt_d_management_19" />
                           </c:when>
                           <c:when test="${cpd.ct_award_hours_type == 3}">
                               <lb:get key="label_cpd.label_core_cpt_d_management_20" />
                           </c:when>
                        </c:choose>
                    </td>
                </tr>
                <tr>
                    <td class="wzb-form-label" valign="top"><lb:get key="label_cpd.label_core_cpt_d_management_15" />：</td>
                    <td class="wzb-form-control">
                         <c:choose>
                           <c:when test="${cpd.ct_trigger_email_type == 1}">
                               <lb:get key="label_cpd.label_core_cpt_d_management_21" />
                           </c:when>
                           <c:when test="${cpd.ct_trigger_email_type == 2}">
                               <lb:get key="label_cpd.label_core_cpt_d_management_22" />
                           </c:when>
                        </c:choose>
                    </td>
                </tr>
                
                <c:if test="${cpd.ct_trigger_email_type == 2}">
	               <c:if test="${cpd.ct_trigger_email_month_1 != null}"> 
	                <tr>
	                    <td class="wzb-form-label" valign="top"></td>
	                    <td class="wzb-form-control">
							<input name="month_1" id="month_1" type="hidden" value = "${cpd.ct_trigger_email_month_1}">
	                    	<span id="div_month_1">
	                    		<%--  ${cpd.ct_trigger_email_month_1}<lb:get key="label_cpd.label_core_cpt_d_management_33" /> --%>
	                    	</span> ${cpd.ct_trigger_email_date_1}
	                    	<c:if test="${lang ne 'en-us'}"> 
	                    		<lb:get key="label_cpd.label_core_cpt_d_management_34" />
	                    	</c:if>
	                    </td>
	                </tr>
	               </c:if>
	                <c:if test="${cpd.ct_trigger_email_month_2 != null}"> 
	                <tr>
	                    <td class="wzb-form-label" valign="top"></td>
	                    <td class="wzb-form-control">
	                    	<input name="month_2" id="month_2" type="hidden" value = "${cpd.ct_trigger_email_month_2}">
	                    	<span id="div_month_2">
	                    		<%-- ${cpd.ct_trigger_email_month_2}<lb:get key="label_cpd.label_core_cpt_d_management_33" /> --%>
	                    	</span> ${cpd.ct_trigger_email_date_2}
	                        <c:if test="${lang ne 'en-us'}"> 
	                    		<lb:get key="label_cpd.label_core_cpt_d_management_34" />
	                    	</c:if>
	                   </td>
	                </tr>
	               </c:if>
	                <c:if test="${cpd.ct_trigger_email_month_3 != null}"> 
	                <tr>
	                    <td class="wzb-form-label" valign="top"></td>
	                    <td class="wzb-form-control">
	                    	<input name="month_3" id="month_3" type="hidden" value = "${cpd.ct_trigger_email_month_3}">
	                    	<span id="div_month_3">
	                    		<%-- ${cpd.ct_trigger_email_month_3}<lb:get key="label_cpd.label_core_cpt_d_management_33" /> --%>
	                    	</span>	${cpd.ct_trigger_email_date_3}
	                    	<c:if test="${lang ne 'en-us'}"> 
	                    		<lb:get key="label_cpd.label_core_cpt_d_management_34" />
	                    	</c:if>
	                    </td>
	                </tr>
	               </c:if>
	            </c:if>
                
                <tr>
                    <td class="wzb-form-label" valign="top"><lb:get key="label_cpd.label_core_cpt_d_management_16" />：</td>
                    <td class="wzb-form-control">
                         <c:choose>
                           <c:when test="${cpd.ct_cal_before_ind == 1}">
                               <lb:get key="label_cpd.label_core_cpt_d_management_23" />
                           </c:when>
                           <c:when test="${cpd.ct_cal_before_ind == 2}">
                               <lb:get key="label_cpd.label_core_cpt_d_management_24" />
                           </c:when>
                        </c:choose>
                    </td>
                </tr>
                <tr>
                    <td class="wzb-form-label" valign="top"><lb:get key="label_cpd.label_core_cpt_d_management_17" />：</td>
                    <td class="wzb-form-control">
                        <c:choose>
                        	<c:when test="${not empty cpd.ct_recover_hours_period}">
                        		${cpd.ct_recover_hours_period}
                        	</c:when>
                        	<c:otherwise>
                        		--
                        	</c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </tbody>
        </table>  

        <table cellpadding="0" cellspacing="0" style="border-bottom:1px solid #ccc;" class="margin-top28">
            <tbody>
                <tr>
                    <td class="text-left" width="50%">
                        <span style="" class="wzb-before"><lb:get key="label_cpd.label_core_cpt_d_management_35" /></span>
                    </td>
                    <td class="text-right" width="50%">
                         <button class="btn wzb-btn-orange" onclick="javascript:go('${ctx}/app/admin/cpdGroup/insert?ct_id='+wbEncrytor().cwnEncrypt(${cpd.ct_id})+'')">
                           <lb:get key="label_cpd.label_core_cpt_d_management_85" />
                         </button>
                    </td>
                </tr>
            </tbody>
        </table>
        
        <div class="datatable" style="margin-top:20px;">
             <div class="datatable-body" id="cpd_group_list">
             </div>
        </div>
        
    </div>
    <!-- 内容添加结束 -->
</div>

 <script type="text/javascript">
  function del(id){
	  Dialog.confirm({text:fetchLabel('label_core_cpt_d_management_36'), callback: function (answer) {
			if(answer){
				$.ajax({
		              url : "${ctx}/app/admin/cpdManagement/detele",
		              data : {'ct_id' : id },
		              dataType:"json",
		              success : function(data){
		                  if(data.success == false){
		                	  if(data.type == 1){
		                		  Dialog.alert(fetchLabel('label_core_cpt_d_management_37'));
		                	  }else if(data.type == 2){
		                		  Dialog.alert(fetchLabel('label_core_cpt_d_management_38'));
		                	  }
		                  }else{
		                	  go('${ctx}/app/admin/cpdManagement/index');
		                  }
		              }
		          });
			}
		}
	 });
	  
  }
 </script>

<script type="text/javascript">
var searchTb;
var sortIndex = 1;
var ct_id =  ${cpd.ct_id};
var cg_count = ${cg_count};
$(function() {
	if(cg_count == 0){
		cg_count = 10;	
	}
    searchTb = $("#cpd_group_list").table({
        url : '${ctx}/app/admin/cpdGroup/listJson',
        params : {'cg_ct_id' : ct_id },
        dataType : 'json',
        colModel : colModel,
        rp : cg_count,
        showpager : 3,
        usepager : true,
        onSuccess : function(data){
     		$(".wzb-images-up-b:last").addClass("wzb-images-up-g");
     	   	$(".wzb-images-down-b:first").addClass("wzb-images-down-g");
     	    $(".wzb-images-down-b:first").removeAttr("href");
     	    $(".wzb-images-up-b:last").removeAttr("href");
     	    $(".wzb-images-down-b:first").attr("disabled","true");
    	    $(".wzb-images-up-b:last").attr("disabled","true");
     	    $(".wzb-images-down-b:first").attr("style","cursor:default");
    	    $(".wzb-images-up-b:last").attr("style","cursor:default");
        }
    });
    
})

     var colModel = [
                    {
                    	sortable : false,
                        name : 'cg_display_order',
                        display : fetchLabel('label_core_cpt_d_management_6'),
                        width : '15%',
                        align : 'left',
                        format : function(data) {
                            p = {
                                title : data.cg_display_order,
                                order : sortIndex++
                            };
                            return $('#cpdNum-template').render(p);
                        }
                    },
                    {
                    	sortable : false,
                        name : 'cg_code',
                        display : fetchLabel('label_core_cpt_d_management_83'),
                        width : '20%',
                        align : 'center',
                        format : function(data) {
                            p = {
                                text : data.cg_code
                            };
                            return $('#text-center-template').render(p);
                        }
                    },
                    {
                    	sortable : false, 
                        name : 'cg_alias',
                        display : fetchLabel('label_core_cpt_d_management_4'),
                        align : 'center',
                        Width : '30%',
                        format : function(data) {
                            p = {
                                text : data.cg_alias
                            }
                            return $('#text-center-template').render(p);
                        }
                    }, {
                        display : '',
                        width : '35%',
                        align : 'right',
                        format : function(data) {
                            p = {
                                cg_id : data.cg_id,
                                cg_ct_id : data.cg_ct_id
                            };
                            return $('#text-operate-template').render(p);
                        }
                    } ];
                    
                    
function cpdListSort(index,isUp){
	var cg_display_order = '';
	for(var i=1;i<sortIndex;i++){
		var ct_type = '';
		if(isUp == true){
			if(i == index-1){
				ct_type = $("table:last").find("tr").eq(i+1).find("td").eq(1).text();
				cg_display_order += ct_type + ',';
        		ct_type = $("table:last").find("tr").eq(i).find("td").eq(1).text();
        		cg_display_order += ct_type + ',';
			}else if(i != index-1 && i != index){
				ct_type = $("table:last").find("tr").eq(i).find("td").eq(1).text();
				cg_display_order += ct_type + ',';
			}
		}else if(isUp == false){
			if(i == index){
				ct_type = $("table:last").find("tr").eq(i+1).find("td").eq(1).text();
				cg_display_order += ct_type + ',';
        		ct_type = $("table:last").find("tr").eq(i).find("td").eq(1).text();
        		cg_display_order += ct_type + ',';
			}else if(i != index+1 && i != index){
				ct_type = $("table:last").find("tr").eq(i).find("td").eq(1).text();
				cg_display_order += ct_type + ',';
			}
		}
		
	}
	
	$.ajax({
        url : "${ctx}/app/admin/cpdGroup/infoSort",
        data : {"cg_display_order" : cg_display_order,
        	    "cg_ct_id" : ct_id
        	    },
        success : function(data) {
            reloadTable();
        }
    });
	
}                

function reloadTable() {
	sortIndex = 1;
    $(searchTb).reloadTable({
    	url : '${ctx}/app/admin/cpdGroup/listJson',
        params : {'cg_ct_id' : ct_id },
        dataType : 'json'
    });
};

function delCpdGroup(id){
	  Dialog.confirm({text:fetchLabel('label_core_cpt_d_management_39'), callback: function (answer) {
			if(answer){
				$.ajax({
		              url : "${ctx}/app/admin/cpdGroup/detele",
		              data : {'cg_id' : id },
		              dataType:"json",
		              success : function(data){
		                  if(data.success == false){
		                	  if(data.type == 1){
		                		  Dialog.alert(fetchLabel('label_core_cpt_d_management_40'));
		                	  }else if(data.type == 2){
		                		  Dialog.alert(fetchLabel('label_core_cpt_d_management_41'));
		                	  }
		                  }else{
		                	  reloadTable();
		                  }
		              }
		          });
			}
		}
	 });
	  
}
</script>

<script id="text-operate-template" type="text/x-jsrender">
<button type="button"class="btn wzb-btn-blue margin-right4" onclick="javascript:go('${ctx}/app/admin/cpdGroup/insert?cg_id='+wbEncrytor().cwnEncrypt({{>cg_id}})+'')">
	<lb:get key="global.button_update" />
 </button>
 <button type="button"  class="btn wzb-btn-blue margin-right4" onclick="javascript:go('${ctx}/app/admin/cpdGroup/groupPeriod?cg_id='+wbEncrytor().cwnEncrypt({{>cg_id}})+'')">
	<lb:get key="label_cpd.label_core_cpt_d_management_92" />
 </button>
 <button type="button"  class="btn wzb-btn-blue delete" onclick="javascript:delCpdGroup({{>cg_id}});">
	<lb:get key="global.button_del" />
 </button>
</script>

<script id="cpdNum-template" type="text/x-jsrender">
 <div class="datatable-table-column text-left"> {{>order}}
    
    <a class="wzb-images-down-b" onclick="Javascript:;" href="javascript:cpdListSort({{>order}},true);" ></a>
    <a class="wzb-images-up-b" onclick="Javascript:;" href="javascript:cpdListSort({{>order}},false);"></a>
 </div>  
</script>

</body>
</html>