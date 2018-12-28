<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../common/taglibs.jsp"%>
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
<link rel="stylesheet" type="text/css" href="${ctx}/js/datepicker/skin/WdatePicker.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/js/datepicker/laydate/skins/molv/laydate.css"/>
<script type="text/javascript" src="${ctx}/static/js/layer/layer.js"></script> 
<script type="text/javascript" src="${ctx}/js/datepicker/laydate/laydate.js"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_cpd_${lang}.js"></script>


<script>
window.onload = function(){
		$("input[name='exportUser']").eq(0).attr("checked", true);
}

</script>
<%@ include file="../common/userTree.jsp"%>
<%@ include file="../common/groupTree.jsp"%>
<%@ include file="../common/courseTree.jsp"%>
<%@ include file="../common/catalogTree.jsp"%>
<script type="text/javascript" src="${ctx}/js/date-picker.js"></script>

<script type="text/javascript" src="${ctx}/static/admin/js/learnerReport/index.js"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_lm_${lang}.js"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_${lang}.js"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_rp_${lang}.js"></script>
<script type="text/javascript">
function down_excel(){
	cghiCtIdArray = new Array();
    usertIdArray = new Array();
    exportGroupIdArray = new Array();
    exportUser= 0;
    var period = $('#cghi_period option:selected') .val();
     var cghi_ct_ids = document.getElementsByName("cghi_ct_id");
     var exportUsers = document.getElementsByName("exportUser");
     var exportUserIds = document.getElementById("exportUserIds").options;
     var exportGroupIds = document.getElementById("exportGroupIds").options;
     for( i=0;i<exportUsers.length;i++){
         if(exportUsers[i].checked == true){
             exportUser = exportUsers[i].value;
         }
     }
     if(exportUser==1){//指定学员
         if(exportUserIds.length>0){
             for(i=0;i<exportUserIds.length;i++){
                 usertIdArray[i] = exportUserIds[i].value;
             }
         }else{
             Dialog.alert('<lb:get key="label_cpd.label_core_cpt_d_management_138" />');
             //layer.alert('<lb:get key="label_cpd.label_core_cpt_d_management_138" />',{title:fetchLabel("label_core_report_145")});
             return;
         }
     }

     if(exportUser==2){//指定用户组
    	 if(exportGroupIds.length>0){
             for(i=0;i<exportGroupIds.length;i++){
            	 exportGroupIdArray[i] = exportGroupIds[i].value;
             }
         }else{
             Dialog.alert(fetchLabel("label_core_report_142"));
        	 //layer.alert(fetchLabel("label_core_report_142"),{title:fetchLabel("label_core_report_145")});
             return;
         }
    	 
     }

     j = 0;
     for( i=0;i<cghi_ct_ids.length;i++){
         if(cghi_ct_ids[i].checked == true){
             cghiCtIdArray[j] = cghi_ct_ids[i].value;
             j++;
         }
     }
     if(j==0){
         Dialog.alert('<lb:get key="label_cpd.label_core_cpt_d_management_139" />');
         //layer.alert('<lb:get key="label_cpd.label_core_cpt_d_management_139" />',{title:fetchLabel("label_core_report_145")});
         return;
     }
     
	/* var userNames=new Array();
	 $("#exportUserIds option").each(function(index){  
		  userNames[index]=$(this).text();
          $(this).attr('selected','selected'); 
     })  
     $('#exportUserNames').val(userNames);
     
	 var groupNames=new Array();
     $("#exportGroupIds option").each(function(index){  
    	 groupNames[index]=$(this).text();
          $(this).attr('selected','selected'); 
     })  
     $('#exportGroupNames').val(groupNames); */
     
 	var downloadPath ='';
	layer.open({
	 	  type: 1,//弹出类型 
	      area: ['500px', '274px'], //宽高
	      title : fetchLabel("label_core_report_140"),//标题 
		  content: '<div class="pop-up-word">'+
		 				'<span id="successMsg" style="display:none;"><lb:get key="label_rp.label_core_report_163"/></span>'+
		 				'<div id="download_loading" class="layer-loading"></div>'+
		 			'</div>'+
		 			'<div class="wzb-bar">'+
		 				'<input id="downloadBtn" disabled="disabled" value="<lb:get key="label_rp.label_core_report_139"/>" type="button" class="btn wzb-btn-blue margin-right10 wzb-btn-big">'+
		 			'</div>',
		  success: function(layero, index){
				$.ajax({
			        url : "${ctx}/app/admin/cpdOutstandingReportTa/cpdOutstandingReportTaExcel",
			        type : 'POST',
			        //data:  $('#formCondition').serialize(),
                    data:{"period":period,"cghiCtIdArray":cghiCtIdArray,"exportUser":exportUser,"usertIdArray":usertIdArray,"exportGroupIdArray":exportGroupIdArray},
			        dataType : 'json',
			        traditional : true,
			        success : function(data) {
			        	downloadPath = data.fileUri;
			        	$('#download_loading').hide();
			        	$('#successMsg').show();
			        	$('#downloadBtn').removeAttr("disabled");
			        	$('#downloadBtn').click(function(){
			        		if(downloadPath!=''){
			        			window.location.href = downloadPath;
			        		}
			        	});
			        }
			     });
		  }
	});
}

        

function reloadTable() {
      var period = $('#cghi_period option:selected') .val();
      $.ajax({
          url : '${ctx}/app/admin/cpdOutstandingReportTa/getcpdTypeRegHistoryTa?period='+period,
          dataType : 'json',
          type : 'POST',
          success : function(data) {
              $("#tableContent").html("");
               for(var i=0;i<data.cpdType.length;i++){
                   p = {
                           cghi_ct_id : data.cpdType[i].ct_id,
                           cghi_license_alias : data.cpdType[i].ct_license_alias,
                           startTime : data.cpdType[i].cpdPeriodVO.startDate,
                           endTime : data.cpdType[i].cpdPeriodVO.endDate
                   };
                   $("#tableContent").append($('#cpdType-template').render(p));
               }
          }
      });
  };


</script>

</head>
<body>
      <form id="formCondition" action=""  method="post" >
        <input type="hidden" name="belong_module" value=FTN_AMD_TRAINING_REPORT_MGT/>
		<input type="hidden" id="exportUserNames" name="exportUserNames" value=""/>
		<input type="hidden" id="exportGroupNames" name="exportGroupNames" value=""/>
		
 	 	<title:get function="global.FTN_AMD_TRAINING_REPORT_MGT"/>
    	
      
        <ol class="breadcrumb wzb-breadcrumb">
            <li>
            	<a href="javascript:wb_utils_gen_home(true);">
            		<i class="fa wzb-breadcrumb-home fa-home"></i>
            		<lb:get key="label_lm.label_core_learning_map_1" />
            	</a>
            </li>
            <li><a href="javascript:wb_utils_nav_go('FTN_AMD_TRAINING_REPORT_MGT',${prof.usr_ent_id},'${label_lan}');">
            <lb:get key="global.FTN_AMD_TRAINING_REPORT_MGT" /></a></li>
            <li class="active"><lb:get key="label_cpd.label_core_cpt_d_management_127" /></li>
        </ol>
        
        <div class="panel wzb-panel">
            <div class="panel-body">
                <table>
                    <tbody>
                      <tr>
                        <td class="wzb-form-label" valign="top"><span class="wzb-form-star">*</span><lb:get key="label_cpd.label_core_cpt_d_management_128" />：</td>
                        <td class="wzb-form-control">
                            <select name="cghi_period"  id = "cghi_period"  onchange="reloadTable()">
                                <c:forEach items="${periods}"  var="periodLists">
                                <option value="${periodLists.cghi_period }">${periodLists.cghi_period }</option>
                                </c:forEach>
                            </select>
                            
                            <span class="ask-in margin4 allhint-style-1" id="ask"></span>
                        </td>
                    </tr>
                    
                        <tr>
                            <td class="wzb-form-label" valign="top"><lb:get key="label_rp.label_core_report_45" />：</td>
                            <td class="wzb-form-control">
                                <table>
                                    <tbody>
                                        <tr>
                                            <td>
                                                <label class="wzb-form-checkbox"><!-- 所有学员 -->
                                                    <input name="exportUser" value="0" type="radio" onclick="checkedOrDisabledById('exportUser',0,'','includeDelUser')"><lb:get key="label_rp.label_core_report_46"/>
                                                </label>
                                            </td>
                                        </tr>
                                       <%--  <tr>
                                            <td>
                                                <label class="wzb-form-checkbox padding-left20">
                                                    <input name="includeDelUser" value="true" type="checkbox"><lb:get key="label_rp.label_core_report_47" />
                                                </label>
                                            </td>
                                        </tr> --%>
                                    </tbody>
                                </table>
                            </td>
                        </tr>
                        <tr>
                            <td class="wzb-form-label"></td>
                            <td class="wzb-form-control">
                                <table>
                                    <tbody>
                                        <tr>
                                            <td>
                                                <label class="wzb-form-checkbox"><!-- 指定学员 -->
                                                    <input name="exportUser" value="1" type="radio" onclick="checkedOrDisabledById('exportUser',1,'includeDelUser');"><lb:get key="label_rp.label_core_report_48"/>
                                                </label>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td valign="top">
                                                <span class="margin-left20">
                                                    <select name="exportUserIds" id="exportUserIds" class="wzb-select" 
                                                    style="width:400px;height:120px;overflow-x:auto;" width="400" multiple="multiple">
                                                      
                                                    </select> 
                                                </span>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td class="wzb-form-control" style="padding-left:20px;">
                                                <input id="user_choose_btn" type="button" name="" value="<lb:get key="button_select"/>"
                                                		class="btn wzb-btn-blue margin-right4 xueyuan">
                                                <input type="button" name=" " value="<lb:get key="button_delete"/>" class="btn wzb-btn-blue margin-right4" onclick="removeSelectedOptionsById('exportUserIds');">
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                            </td>
                        </tr>
                        <tr>
                            <td class="wzb-form-label"></td>
                            <td class="wzb-form-control">
                                <table>
                                    <tbody>
                                        <tr>
                                            <td>
                                                <label class="wzb-form-checkbox"><!-- 指定用户组 -->
                                                    <input name="exportUser" value="2" type="radio" onclick="checkedOrDisabledById('exportUser',2,'includeDelUser');"><lb:get key="label_rp.label_core_report_49"/>
                                                </label>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td valign="top">
                                                <span class="margin-left20">
                                                    <select name="exportGroupIds" id="exportGroupIds" class="wzb-select" style="width:400px;height:120px;overflow-x:auto;" width="400" multiple="">
                                                    </select>
                                                </span>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td class="wzb-form-control" style="padding-left:20px;">
                                                <input id="group_choose_btn" type="button" name=" " value="<lb:get key="button_select"/>" class="btn wzb-btn-blue margin-right4 xueyuan">
                                                <input type="button" name=" " value="<lb:get key="button_delete"/>" class="btn wzb-btn-blue margin-right4" onclick="removeSelectedOptionsById('exportGroupIds');">
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                            </td>
                        </tr>
                        <!-- 牌照类型 -->
                        <tr>
                        <td class="wzb-form-label" valign="top"><span class="wzb-form-star">*</span><lb:get key="label_cpd.label_core_cpt_d_management_129" />：</td>
                           
                        <td class="wzb-form-control">
                            <table>
                                <tbody  id="tableContent">
                                    <c:forEach items="${cpdType}"  var="cpdTypes">
                                    <tr>
                                        <td><input name="cghi_ct_id" value="${cpdTypes.ct_id }" type="checkbox" checked="checked">
                                        ${cpdTypes.ct_license_alias }
                                        ( ${cpdTypes.cpdPeriodVO.startDate}  -  ${cpdTypes.cpdPeriodVO.endDate} )
                                         </td>
                                    </tr> 
                                    
                                    </c:forEach>
                                </tbody>
                            </table>
                        </td>
                    </tr>
                      
                      
                    <tr>
                        <td class="wzb-form-label"></td>
                        <td class="wzb-form-control"><span class="wzb-form-star">*</span><lb:get key="label_cpd.label_core_cpt_d_management_136" /></td>
                    </tr>
                    </tbody>
                </table>


                <div class="wzb-bar">
                    <input style="" name="downExcel" onclick="down_excel()" value="<lb:get key="label_rp.label_core_report_109"/>" type="button" class="btn wzb-btn-blue margin-right10 wzb-btn-big" />
                    <input style="" name=" " value="<lb:get key="button_cancel"/>" onclick="javascript:wb_utils_nav_go('FTN_AMD_TRAINING_REPORT_MGT',${prof.usr_ent_id},'${label_lan}');" type="button" class="btn wzb-btn-blue margin-right10 wzb-btn-big">
                </div>


            </div>  <!-- panel-body End-->
        </div>  <!-- panel End-->
   </form
   >
<script id="cpdType-template" type="text/x-jsrender">
<tr>
         <td><input name="cghi_ct_id" value="{{>cghi_ct_id}}" type="checkbox" checked="checked">{{>cghi_license_alias}}
            ( {{>startTime}} - {{>endTime}} )
</td>
 </tr>
</script>
<script type="text/javascript">
    $("#ask").mousemove(function(){
        layer.tips('<lb:get key="label_cpd.label_core_cpt_d_management_135" />','#ask', {
        tips: [2,'rgba(128,128,128,0.9)'],
        time:50000
        });
    })
    $("#ask").mouseleave(function(){
        layer.tips();
    })
</script>
</body>
</html>