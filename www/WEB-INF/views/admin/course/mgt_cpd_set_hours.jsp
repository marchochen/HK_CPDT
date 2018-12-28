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
	
	loadNavigationTemplate();
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
            <c:choose>
            <c:when test="${item.itm_run_ind == '1'}">
                <li><a href="javascript:itm_lst.get_item_run_detail(${item.itm_id})">${item.itm_title}</a></li>
            </c:when>
            <c:otherwise>
                <li><a href="javascript:itm_lst.get_item_detail(${item.itm_id})">${item.itm_title}</a></li>
            </c:otherwise>
            </c:choose>       
            <li class="active"><lb:get key="label_cpd.label_core_cpt_d_management_195" /></li>
        </ol>
        
     <c:if test="run_ind != false">   
     </c:if> 
     
     
        
 <form id="formCondition" action="${ctx}/app/admin/learnerReport/getLearnerReportByUser"  method="post" onsubmit="preSubmit()">
        <div class="panel wzb-panel">
            <div class="panel-body">
				
			  <jsp:include page="../../admin/common/itm_gen_action_nav_share_new.jsp">
				<jsp:param value="119" name="cur_node_id"/>
			   </jsp:include>
				
				<!--内容开始  -->
				    <div class="wzb-item-main margin-top28">
				    
				       <c:if test="${empty item.parent.itm_id}">
			            <table cellpadding="0" cellspacing="0" style="border-bottom:1px solid #ccc;">
			                <tbody>
			                    <tr>
			                        <td class="text-left" width="50%">
			                            <span style="" class="wzb-before"><lb:get key="detail_cpdHours_information"/></span>
			                        </td>
			                        <td class="text-right" width="50%">
			                            <input type="button" value="<lb:get key='global.button_update' />" class="btn wzb-btn-orange" onclick="goAdd(wbencrytor.cwnEncrypt(${item.itm_id}))">
			                        </td>
			                    </tr>
			                </tbody>
			            </table>
						<table cellpadding="0" cellspacing="0" class="margin-top20">
						    <tbody>
						        <tr>
						            <td class="wzb-form-label"><lb:get key="label_cpd.label_core_cpt_d_management_208"/>：</td>
						            <td class="wzb-form-control"><span id="accredition_code">--</span></td>
						        </tr>
						         <c:if test="${item.itm_run_ind != '1'  and item.itm_type ne 'CLASSROOM'}">
						      <tr>
						             <td class="wzb-form-label"><lb:get key="detail_cpdHours_end_time"/>：</td>
						             <td class="wzb-form-control"><span id="aci_hours_end_date"><lb:get key="detail_cpdHours_unlimied"/></span></td>
						         </tr>
						   </c:if>
						    </tbody>
						</table>
					   </c:if> 
			
			            <table cellpadding="0" cellspacing="0" class="margin-top20" style="border-bottom:1px solid #ccc;">
			                <tbody>
			                    <tr>
			                        <td class="text-left" width="50%">
			                            <div class=""><lb:get key="detail_award_hours"/><span class="ask-in margin4 allhint-style-1" id="ask"></span></div>
			                        </td>
			                        <td class="text-right" width="50%">
			                        	<c:if test="${not empty item.parent.itm_id}">
			                            	<input type="button" value="<lb:get key='global.button_update' />" class="btn wzb-btn-orange" onclick="goAdd(wbencrytor.cwnEncrypt(${item.itm_id}))">
			                        	</c:if>
			                        </td>
			                    </tr>
			                </tbody>
			            </table>
			
			            <table cellpadding="0" cellspacing="0" class="margin-top20" >
			                <tbody>
			                    <tr id="cpt_set_hours_list">
			                        <td class="wzb-form-label"  width="30%"></td>
			                        <td  width="20%" style="color:#666;padding-left: 20px"><lb:get key="detail_core_hours"/></td>
			                        <td  width="20%" style="color:#666;padding-left: 20px"><lb:get key="detail_non_core_hours"/></td>
			                        <td class="wzb-form-control" width="30%"></td>
			                    </tr>
			                </tbody>
			            </table>
			        
			        </div>
			        <%--</div>--%>
				<!-- 内容结束 -->

            </div>  <!-- panel-body End-->
        </div>  <!-- panel End-->
   
   </form>
 
 <script type="text/javascript">
    var searchTb;
    var itm_id =${item.itm_id};
    $(function() {
    	
    	var parent_itm_id = '${item.parent.itm_id}';
    	if(parent_itm_id == ''){
    		parent_itm_id = 0;
    	}
    	
    	$.ajax({  
	        type:'post',      
		    url:'${ctx}/app/admin/aeItemCPDItem/getHoursListJson',  
		    data:{"itm_id":itm_id,
		    	  "parent_itm_id":parent_itm_id},  
		    dataType:'json',
		    success:function(data){  
			    	if(undefined != data.aeItemCPDItem){
			    		if('' != data.aeItemCPDItem.aci_accreditation_code){
			    			$("#accredition_code").text(data.aeItemCPDItem.aci_accreditation_code); 
			    		}
			    		if('' != data.end_date){
			    			$("#aci_hours_end_date").text(data.end_date);
			    		}
		    		 } 
			    	var cpdCoulmnNum = 0;
			    	
			    		// 用小牌列表排序 
			    		for(var i=data.cpdGroupList.length-1; i >= 0; i--){
			    			//if(undefined != data.aeItemCPDItem){
			    				for(var j=0; j < data.aeItemCPDItem.aeCPDGourpItemList.length; j++){
			    					if(data.aeItemCPDItem.aeCPDGourpItemList[j].acgi_cg_id ==  data.cpdGroupList[i].cg_id){
						    			 p = {
						    					 cg_alias : data.cpdGroupList[i].cg_alias,
							    				 cg_code : data.cpdGroupList[i].cg_code,
							    				 acgi_award_core_hours : data.aeItemCPDItem.aeCPDGourpItemList[j].acgi_award_core_hours,
							    				 acgi_award_non_core_hours : data.aeItemCPDItem.aeCPDGourpItemList[j].acgi_award_non_core_hours
							                    };
						    			 
						    			 if(data.aeItemCPDItem.aeCPDGourpItemList[j].acgi_award_core_hours > 0 || data.aeItemCPDItem.aeCPDGourpItemList[j].acgi_award_non_core_hours > 0 ){
						    				 cpdCoulmnNum++;
						    				 var html =  $('#text-info-template').render(p);
									    	 $('#cpt_set_hours_list').after(html);
						    			 }
							    		break;
			    					}
			    					if(j == data.aeItemCPDItem.aeCPDGourpItemList.length-1){
			    						p = {
						    					 cg_alias : data.cpdGroupList[i].cg_alias,
							    				 cg_code : data.cpdGroupList[i].cg_code,
							    				 acgi_award_core_hours : '0.00',
							    				 acgi_award_non_core_hours : '0.00'
							                    };
							            var html =  $('#text-info-template').render(p);
							    		$('#cpt_set_hours_list').after(html);
			    					}
					    		}
			    		}
			    	 if(cpdCoulmnNum == 0){
			    		    var html = "<div class='datatable-stat'>"+
							            "<div class='losedata'><i class='fa fa-folder-open-o'></i><p><lb:get key='lab_table_empty' /></p></div>"+
							            "</div>";
					    	document.getElementById('cpt_set_hours_list').innerHTML=html;
					    }
		       }
			    		
		    });  
    	
    })

</script>
 
 <script type="text/javascript">
   function goAdd(itm_id){
	 var  url = wb_utils_controller_base +"course/admin/update_cpd_hours/"+itm_id;
	 self.location.href=url;
   }
 </script> 

<script id="text-info-template" type="text/x-jsrender">
             <tr>
			  <td class="wzb-form-label"  width="30%">{{>cg_alias}} ({{>cg_code}})：</td>
			  <td class="wzb-form-control " width="20%"  style="padding-left: 20px">{{>acgi_award_core_hours}}&nbsp;hour(s)</td>
			  <td class="wzb-form-control " width="20%"  style="padding-left: 20px">{{>acgi_award_non_core_hours}}&nbsp;hour(s)</td>
			  <td class="wzb-form-control" width="30%"></td>
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
</script>
</body>
</html>