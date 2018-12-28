<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<%@ include file="../common/meta.jsp"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" type="text/css" href="${ctx}/static/css/base.css"/>
<link rel="stylesheet" type="text/css" href="${ctx}/static/admin/css/admin.css"/>
<c:if test="${lang eq 'en-us'}">
	<!-- 兼容英文的css -->
	<link rel="stylesheet" href="${ctx}/static/css/base-en.css"/>
	<link rel="stylesheet" href="${ctx}/static/admin/css/admin-en.css"/>
</c:if>
<link rel="stylesheet" href="${ctx}/js/tree/css/ztree.css" />
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-admin.css" />
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-learner.css" />
<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<script type="text/javascript" src="${ctx}/static/js/front/sns.js"></script>
<script type="text/javascript" src="${ctx}/static/js/ckplayer/ckplayer.js" charset="utf-8"></script>
<script type="text/javascript" src="${ctx}/static/js/ckplayer/js/offlights.js" charset="utf-8"></script>
<script type="text/javascript" src="${ctx}/js/jquery.prompt.js"></script>
<script type="text/javascript" src="${ctx}/static/js/front/jquery.sns.js"></script>
<script type="text/javascript" src="${ctx}/static/js/image_doing.js"></script>
<script  type="text/javascript" src="${ctx}/js/wb_item.js"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_cm_${lang}.js"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_tm_${lang}.js"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_cpd_${lang}.js"></script>
<script type="text/javascript" src="${ctx}/static/js/cwn_utils.js"></script>
<script type="text/javascript" src="${ctx}/js/date-picker.js"></script>  


<script>
itm_lst = new wbItem;
wbEncrytor = new wbEncrytor;
var opens;
var tndId;
var itmId =wbEncrytor.cwnDecrypt(window.location.href.replace(/.*\//,""));
var tkhId = -1;
var sns = new Sns();
var targetId = itmId;
var module = 'Course';
itm_lst = new wbItem;
window.onload = function(){
	var exam_ind = ${item.itm_exam_ind};
	if(exam_ind == 0){
		highLightMainMenu("FTN_AMD_TRAINING_MGT");//高亮菜单
	}else{
		highLightMainMenu("FTN_AMD_EXAM_MGT");
	}
	// 加载评论列表
	loadNavigationTemplate();
	
	var itm_name = '${item.itm_title}';
	if(${item.itm_run_ind}  == '1'){
		itm_name = '${item.parent.itm_title}'+' > '+'${item.itm_title}';
	}
	$('#itm_name').val(itm_name);
}


</script>

</head>
<body>

<input type="hidden" id="cur_node_id" value="">
	<c:choose>
    	<c:when test="${item.itm_exam_ind == '1'}">
    		<title:get function="global.FTN_AMD_EXAM_MGT"/> 
    	</c:when>
    	<c:otherwise>
    		<title:get function="global.FTN_AMD_TRAINING_MGT"/>
    	</c:otherwise>
    </c:choose>
     
     
      <ol class="breadcrumb wzb-breadcrumb textleft  heder-nav">
            <li>
            	<a href="javascript:wb_utils_gen_home(true);">
            		<i class="fa wzb-breadcrumb-home fa-home"></i>
            		<lb:get key="label_lm.label_core_learning_map_1" />
            	</a>
            </li>
            <li>
              <c:if test="${item.itm_exam_ind == '1'}">
		         <a href="javascript:wb_utils_nav_go('FTN_AMD_EXAM_MGT',${prof.usr_ent_id},'${label_lan}');">
	               <lb:get key="global.FTN_AMD_EXAM_MGT" />
	              </a>
		      </c:if>
		      <c:if test="${item.itm_exam_ind == '0'}">
		          <a href="javascript:wb_utils_nav_go('FTN_AMD_ITM_COS_MAIN',${prof.usr_ent_id},'${label_lan}');">
	               <lb:get key="global.FTN_AMD_ITM_COS_MAIN" />
	              </a>
		      </c:if>
            </li>
            <c:if test="${item.itm_run_ind == '1'}">
               <li><a href="javascript:itm_lst.get_item_detail(${item.parent.itm_id});">
	                   ${item.parent.itm_title}
	                </a>
	           </li>
               <li>
                  <c:if test="${item.itm_exam_ind == '1'}">
                    <a href="javascript:itm_lst.get_item_run_list(${item.parent.itm_id});">
	                   <lb:get key="label_core_training_management_468_ex" />
	                </a>
			      </c:if>
			      <c:if test="${item.itm_exam_ind == '0'}">
			          <a href="javascript:itm_lst.get_item_run_list(${item.parent.itm_id});">
		               <lb:get key="label_core_training_management_246" />
		              </a>
			      </c:if>
               </li>
            </c:if>
            <li><a href="javascript:itm_lst.get_item_detail(${item.itm_id})">${item.itm_title}</a></li>
            <li class="active"><lb:get key="label_cpd.label_core_cpt_d_management_196" /></li>
        </ol>
        
     <c:if test="run_ind != false">   
     </c:if> 
     
     
        
        <div class="panel wzb-panel">
            <div class="panel-body">
				
			  <jsp:include page="../../admin/common/itm_gen_action_nav_share_new.jsp">
				<jsp:param value="119" name="cur_node_id"/>
			   </jsp:include>
				<!--内容开始  -->
				    <div class="wzb-item-main margin-top28">
				      <form id="formIemCpdHours" name="formIemCpdHours" method="post" >
			            <c:if  test="${empty item.parent.itm_id}">
				            <table cellpadding="0" cellspacing="0" style="border-bottom:1px solid #ccc;">
				                <tbody>
				                    <tr>
				                        <td class="text-left" width="50%">
				                            <span style="" class="wzb-before"><lb:get key="detail_cpdHours_information"/></span>
				                        </td>
				                    </tr>
				                </tbody>
				            </table>
				         </c:if>   
			
			            <table cellpadding="0" cellspacing="0" class="margin-top20">
			                <tbody>
			                    <tr> 
			                        <td> 
			                          <input type="hidden" name="aci_id" id="aci_id" value="${aeItemCPDItem.aci_id}">
			                          <input type="hidden" name="aci_itm_id" id="aci_itm_id" value="${item.itm_id}">
			                          <input type="hidden" name="itm_name" id="itm_name">
			                          <input type="hidden" name="itm_code" id="itm_code" value="${item.itm_code}">
			                          <c:if  test="${not empty item.parent.itm_id}">
			                              <input value="${aeItemCPDItem.aci_accreditation_code}" id="aci_accreditation_code" name="aci_accreditation_code" type="hidden" >
			                          </c:if>
			                        </td>  
			                    </tr>
			                    <c:if  test="${empty item.parent.itm_id}">
				                    <tr>
				                        <td class="wzb-form-label"><lb:get key="label_cpd.label_core_cpt_d_management_208" />：</td>
				                        <td class="wzb-form-control">
				                          <input class="wzb-inputText" value="${aeItemCPDItem.aci_accreditation_code}" id="aci_accreditation_code" name="aci_accreditation_code" type="text" style="width:300px;">
				                        </td>
				                    </tr>
			                    </c:if>
			                    <c:if test="${item.itm_run_ind != '1' and item.itm_type ne 'CLASSROOM'}">
				                    <tr>
				                        <td class="wzb-form-label"><lb:get key="detail_cpdHours_end_time"/>：</td>
				                        <td class="wzb-form-control">
				                            <label class="wzb-form-checkbox">
				                                    <input name="aci_end_date" value="0" type="radio"    onclick="checkRadio(0);" checked="checked"><lb:get key="detail_cpdHours_unlimied"/>
				                            </label>
				                        </td>
				                    </tr>
				                    <tr>
				                        <td class="wzb-form-label"></td>
				                        <td class="wzb-form-control" >
				                            <label class="wzb-form-checkbox"  onclick="checkRadio(1);">
				                                    <input name="aci_end_date" value="1" type="radio" <c:if test="${not empty aeItemCPDItem.aci_hours_end_date}"> checked="checked" </c:if>  ><lb:get key="label_cpd.label_core_cpt_d_management_199" />
				                                       
							                              <input type="hidden" id="aci_hours_end_date" value="${aeItemCPDItem.aci_hours_end_date}" name="aci_hours_end_date" />
							                              <input type="text" id="aci_hours_end_date_yy" size="4" maxlength="4" class="wzb-inputText" value="<fmt:formatDate value="${aeItemCPDItem.aci_hours_end_date }" pattern="yyyy"/>" name="aci_hours_end_date_yy" />  -
							                              <input type="text" id="aci_hours_end_date_mm" size="2" maxlength="2" class="wzb-inputText" value="<fmt:formatDate value="${aeItemCPDItem.aci_hours_end_date }" pattern="MM"/>" name="aci_hours_end_date_mm" /> -
							                              <input type="text" id="aci_hours_end_date_dd" size="2" maxlength="2" class="wzb-inputText" value="<fmt:formatDate value="${aeItemCPDItem.aci_hours_end_date }" pattern="dd"/>" name="aci_hours_end_date_dd" />
							                                 <lb:get key="label_cpd.label_core_cpt_d_management_272" /> &nbsp;
							                              <a onmouseout="window.status='';return true;" onmouseover="window.status='Date Picker';return true;"
							                               href="javascript:show_calendar('aci_hours_end_date', '','','','${lang}','${ctx}/cw/skin4/images/gb/css/wb_ui.css');">
							                               <img src="../../../../wb_image/btn_calendar.gif" border="0" />
							                               </a>
				                                    <!-- <input type="text" name="field03_1_yy" maxlength="4" size="4" class="wzb-inputText" value="">-<input type="text" name="field03_1_mm" maxlength="2" size="2" class="wzb-inputText" value="">-<input type="text" name="field03_1_dd" maxlength="2" size="2" class="wzb-inputText" value=""> 年-月-日&nbsp;<script src="../js/date-picker.js" language="JavaScript"></script><a href="javascript:show_calendar('document.frmXml.field03_1', '','','','gb','../cw/skin4/images/gb/css/wb_ui.css');"><img align="absmiddle" border="0" src="../../../wb_image/btn_calendar.gif"></a>
				                                    --><span class="ask-in margin-left10 allhint-style-1" id="shijian"></span> 
				                            </label>
				                        </td>
				                    </tr>
			                    </c:if>
			                </tbody>
			            </table>
			 
			
			            <table cellpadding="0" cellspacing="0" class="margin-top20" style="border-bottom:1px solid #ccc;">
			                <tbody>
			                    <tr>
			                        <td class="text-left">
			                            <div class=""><lb:get key="detail_award_hours"/><span class="ask-in margin4 allhint-style-1"  id="ask"></span></div>
			                        </td>
			                    </tr>
			                </tbody>
			            </table>
			            
						 <div class="wzb-ui-desc-text">   
			                <span style="color:#64BF0F;"><lb:get key="warning_notice"/>：</span><lb:get key="label_cpd.label_core_cpt_d_management_198" />
			            </div>
			            
			            <table cellpadding="0" cellspacing="0" class="margin-top20">
			                <tbody>
			                    <tr id="cpt_set_hours_list">
			                        <td class="wzb-form-label"></td>
			                        <td class="wzb-form-control text-center" width="20%"><lb:get key="detail_core_hours"/></td>
			                        <td class="wzb-form-control text-center" width="20%"><lb:get key="detail_non_core_hours"/></td>
			                        <td class="wzb-form-control" width="40%"></td>
			                    </tr>
				                 
				                   <c:if test="${not empty aeItemCPDItem}">
					                   <c:forEach var="aci" items="${aeItemCPDItem.aeCPDGourpItemList}" varStatus="status">
						                      <tr>
							                        <td class="wzb-form-label">
							                           <input type="hidden" value="${aeItemCPDItem.aeCPDGourpItemList[ status.index].cpdGroup.cg_id}" name="aeCPDGourpItemList[${status.index}].acgi_cg_id"  id="aeCPDGourpItemList[${status.index}].acgi_cg_id"/>
							                           <input type="hidden" value="${aeItemCPDItem.aeCPDGourpItemList[ status.index].acgi_id}" name="aeCPDGourpItemList[${status.index}].acgi_id"  id="aeCPDGourpItemList[${status.index}].acgi_id"/>
							                           <input type="hidden" value="${item.itm_id}" name="aeCPDGourpItemList[${status.index}].acgi_itm_id"  id="aeCPDGourpItemList[${status.index}].acgi_itm_id"/>
							                           
							                            <input type="hidden" value="${aeItemCPDItem.aeCPDGourpItemList[ status.index].cpdGroup.cg_code}" name="aeCPDGourpItemList[${status.index}].cpdGroup.cg_code"  id="aeCPDGourpItemList[${status.index}].cpdGroup.cg_code"/>
							                            <input type="hidden" value="${aeItemCPDItem.aeCPDGourpItemList[ status.index].cpdGroup.cg_alias}" name="aeCPDGourpItemList[${status.index}].cpdGroup.cg_alias"  id="aeCPDGourpItemList[${status.index}].cpdGroup.cg_alias"/>
							                           
							                           ${aeItemCPDItem.aeCPDGourpItemList[ status.index].cpdGroup.cg_alias} (${aeItemCPDItem.aeCPDGourpItemList[ status.index].cpdGroup.cg_code})：
							                        </td>
							                        <td class="wzb-form-control text-center"><input class="wzb-inputText" value="${aeItemCPDItem.aeCPDGourpItemList[ status.index].acgi_award_core_hours}" id="aeCPDGourpItemList[${status.index}].acgi_award_core_hours" name="aeCPDGourpItemList[${status.index}].acgi_award_core_hours" type="text" style="width:100px;"> hour(s)</td>
							                        <td class="wzb-form-control text-center">
							                          <c:if test="${aeItemCPDItem.aeCPDGourpItemList[ status.index].cpdGroup.cg_contain_non_core_ind == '0'}"> 
							                           <input disabled class="wzb-inputText" value="${aeItemCPDItem.aeCPDGourpItemList[ status.index].acgi_award_non_core_hours}"  type="text" style="width:100px;"> hour(s)
							                           <input value="${aeItemCPDItem.aeCPDGourpItemList[ status.index].acgi_award_non_core_hours}" id="aeCPDGourpItemList[${status.index}].acgi_award_non_core_hours" name="aeCPDGourpItemList[${status.index}].acgi_award_non_core_hours" type="hidden"> 
							                          </c:if>
							                          <c:if test="${aeItemCPDItem.aeCPDGourpItemList[ status.index].cpdGroup.cg_contain_non_core_ind == '1'}"> 
							                           <input class="wzb-inputText" value="${aeItemCPDItem.aeCPDGourpItemList[ status.index].acgi_award_non_core_hours}" id="aeCPDGourpItemList[${status.index}].acgi_award_non_core_hours" name="aeCPDGourpItemList[${status.index}].acgi_award_non_core_hours"  type="text" style="width:100px;"> hour(s)
							                          </c:if>
							                        </td>
							                        <td class="wzb-form-control" width="40%"></td>
							                    </tr> 
					                    </c:forEach> 
				                   </c:if>
				                 
                        
			                </tbody>
			            </table>
			            
			           <!--  <div id="item_cpd_list" cellpadding="0" cellspacing="0" class="margin-top20"></div> -->
			            <div class="wzb-bar">
			                <input name=" " value="<lb:get key='button_save'/>" type="button" class="btn wzb-btn-blue margin-right10 wzb-btn-big" onclick="javascript:frmSubmit();">
			                <input name=" " value="<lb:get key='button_cancel'/>" type="button" class="btn wzb-btn-blue wzb-btn-big" onclick="itm_lst.set_itm_cpd_gourp_hour(wbencrytor.cwnEncrypt(${item.itm_id}))">
			            </div>
			          </form>  
			        </div> <!-- 内容结束 -->
            </div>  <!-- panel-body End-->
        </div>  <!-- panel End-->
   
 <script type="text/javascript">
     
     function frmSubmit(){
    	 
    	 var aci_accreditation_code = $('#aci_accreditation_code').val();
    	 if(undefined == aci_accreditation_code || aci_accreditation_code == ''){
    		// Dialog.alert('<lb:get key="label_cpd.label_core_cpt_d_management_209" />');
    		// return;
    	 }else{
    		 if(getChars(aci_accreditation_code) > 80){
    		  Dialog.alert('<lb:get key="label_cpd.label_core_cpt_d_management_210" />');
    			 return;
    		 }
    	 }
    	 
    	var aci_end_date = $("input[name='aci_end_date']:checked").val();
    	if(aci_end_date != undefined && aci_end_date != '' && aci_end_date == 1){
    		   var aci_hours_end_date_yy = $('#aci_hours_end_date_yy').val();
    		   var aci_hours_end_date_mm = $('#aci_hours_end_date_mm').val();
    		   var aci_hours_end_date_dd = $('#aci_hours_end_date_dd').val();
    		  if(aci_hours_end_date_yy && aci_hours_end_date_mm && aci_hours_end_date_dd){
  		        if (aci_hours_end_date_yy != '' || aci_hours_end_date_mm != '' || aci_hours_end_date_dd != '') {
  		            if (!wbUtilsValidateDate("document.formIemCpdHours.aci_hours_end_date", '<lb:get key="detail_cpdHours_end_time"/>')) {
  		            	return;
  		            }
  		        }
  		        if(aci_hours_end_date_yy != '' && aci_hours_end_date_mm != '' && aci_hours_end_date_dd!= '') {
  		        	$("#aci_hours_end_date").val(aci_hours_end_date_yy + "-" + aci_hours_end_date_mm + "-" + aci_hours_end_date_dd); 
  		        }
  		     }
	   		  if($("#aci_hours_end_date").val() == ''){
		        	Dialog.alert('<lb:get key="label_cpd.label_core_cpt_d_management_211" />');
		        	return;
		        }
    	  } 
    	 var length = ${fn:length(cpdGroupList)};
    	
    	 var  positiveNum = /^[0-9]+\.?[0-9]{0,2}$/;
		 var pNum  = new RegExp(positiveNum);
	     for(var i = 0; i < length; i++){
		   var num	= document.getElementById('aeCPDGourpItemList['+i+'].acgi_award_core_hours').value;
		   var non_num	= document.getElementById('aeCPDGourpItemList['+i+'].acgi_award_non_core_hours').value;
		   if(num == '' && non_num == ''){
			   continue;
		   }
		   if(num != ''){
			   if(!pNum.test(num) || num > 99.99){
	   			   Dialog.alert('<lb:get key="label_cpd.label_core_cpt_d_management_205" />');
	   			   return;
			   }
		   }
		   if(non_num != ''){
			   if(!pNum.test(non_num) || non_num > 99.99){
   			   Dialog.alert('<lb:get key="label_cpd.label_core_cpt_d_management_205" />');
   			   return;
   		     }
		   }
		 } 
    	
    	 $.ajax({
		        url : "${ctx}/app/admin/aeItemCPDItem/save",
		        type : 'POST',
		        data:  $('#formIemCpdHours').serialize(),
		        dataType : 'json',
		        traditional : true,
		        success : function(data) {
		        	if(data.success == true){
			        	if(data.map.success == false){
			        		Dialog.alert('<lb:get key="label_cpd.label_core_cpt_d_management_213" />'+data.map.usr_name+'<lb:get key="label_cpd.label_core_cpt_d_management_214" />'+data.map.cg_alias+'<lb:get key="label_cpd.label_core_cpt_d_management_215" />');
			        	}else{
			        		itm_lst.set_itm_cpd_gourp_hour(wbencrytor.cwnEncrypt(${item.itm_id}));
			        	}
		        	}else{
		        		Dialog.alert('<lb:get key="label_cpd.label_core_cpt_d_management_212" />');
		        	}
		        }
		     });
     }

     function checkRadio(str){
    	 if(str == 0){
    		 $('#aci_hours_end_date_yy').val('');
    		 $('#aci_hours_end_date_mm').val('');
    		 $('#aci_hours_end_date_dd').val('');
    		 $('#aci_hours_end_date').val('');
    	 }else{
    		 $("input[name='aci_end_date']:eq(1)").attr("checked",'checked'); 
    	 }
     }
     
</script>
 
 <script type="text/javascript">
   function goAdd(itm_id){
	 var  url = wb_utils_controller_base +"course/admin/update_cpd_hours/"+itm_id;
	 self.location.href=url;
   }
 </script> 

<script id="text-info-template" type="text/x-jsrender">
             <tr>
			  <td class="wzb-form-label">{{>cg_alias}} ({{>cg_code}})：</td>
			  <td class="wzb-form-control text-center">{{>acgi_award_core_hours}} hour(s)</td>
			  <td class="wzb-form-control text-center">{{>acgi_award_non_core_hours}} hour(s)</td>
			  <td class="wzb-form-control" width="40%"></td>
			 </tr>
</script>
<script type="text/javascript">
    $("#ask").mouseenter(function(){
          layer.tips('<lb:get key="label_cpd.label_core_cpt_d_management_197" />','#ask', {
        tips: [2,'rgba(128,128,128,0.9)'],
        time:50000
          });
      });
      $("#ask").mouseleave(function(){
          layer.tips()
      });

    $("#shijian").mouseenter(function(){
          layer.tips('<lb:get key="label_cpd.label_core_cpt_d_management_181" />','#shijian', {
        tips: [2,'rgba(128,128,128,0.9)'],
        time:50000
          });
      });
      $("#shijian").mouseleave(function(){
          layer.tips()
      });
</script>
</body>
</html>