<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<%@ include file="../../common/template.jsp"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" type="text/css" href="${ctx}/static/css/base.css"/>
<link rel="stylesheet" type="text/css" href="${ctx}/static/admin/css/admin.css"/>
<link rel="stylesheet" href="${ctx}/js/tree/css/ztree.css" />
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-admin.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/js/datepicker/skin/WdatePicker.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/js/datepicker/laydate/skins/molv/laydate.css"/>
<script type="text/javascript" src="${ctx}/static/js/layer/layer.js"></script> 
<script type="text/javascript" src="${ctx}/js/datepicker/laydate/laydate.js"></script>
<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-admin.css" />

<jsp:include page="../common/userTree.jsp"></jsp:include>
<jsp:include page="../common/groupTree.jsp"></jsp:include>

<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_rp_${lang}.js"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_cpd_${lang}.js"></script>
<script type="text/javascript" src="${ctx}/static/js/cwn_utils.js"></script>
<script type="text/javascript" src="${ctx}/static/admin/js/cpd/cpd_awarded_hours_report_ta.js"></script>

</head>
<body>

<input type="hidden" name="belong_module" value="FTN_AMD_TRAINING_REPORT_MGT"/>
   

	 <title:get function="global.FTN_AMD_TRAINING_REPORT_MGT"/>
    <ol class="breadcrumb wzb-breadcrumb">
        <li><a href="javascript:wb_utils_gen_home(true);"><i class="fa wzb-breadcrumb-home fa-home"></i><lb:get key="global.lab_menu_started" /></a></li>
        <li><a href="javascript:wb_utils_nav_go('FTN_AMD_TRAINING_REPORT_MGT',${prof.usr_ent_id},'${label_lan}');"><lb:get key="global.FTN_AMD_TRAINING_REPORT_MGT" /></a></li>
        <li class="active"><lb:get key="label_cpd.label_core_cpt_d_management_149" /></li>
    </ol>
    <!-- wzb-breadcrumb End-->

 <div class="panel wzb-panel">
    <!-- 内容添加开始 -->
    <div class="panel-body">
          <form  id="formCondition" action=""  method="post" onsubmit="preSubmit()">
            <input type="hidden" id="exportUserNames" name="exportUserNames" value=""/>
		    <input type="hidden" id="exportGroupNames" name="exportGroupNames" value=""/>
                <table class="margin-top15">
                    <tr>
                        <td class="wzb-form-label" valign="top"><span class="wzb-form-star">*</span><lb:get key="label_cpd.label_core_cpt_d_management_128" />：</td>
                        <td class="wzb-form-control">
                            <select name="cghi_period" id="cghi_period" onchange="reloadTable();">
                                <c:forEach var="i" items="${year}">
                                     <option value="${i}">${i}</option>
                                </c:forEach>
                            </select>
                            <span class="ask-in margin4 allhint-style-1" id="ask"></span>
                        </td>
                    </tr>
                      
                    <tr>
                        <td class="wzb-form-label" valign="top"><lb:get key="label_cpd.label_core_cpt_d_management_150" />：</td>
                         
                        <td class="wzb-form-control">
                            <table>
                                <tbody>
                                    <tr>  
                                        <td>
                                            <label class="wzb-form-checkbox">
                                                <input name="exportUser" value="1" type="radio" checked="checked"><lb:get key="label_cpd.label_core_cpt_d_management_151" />
                                            </label>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <label class="wzb-form-checkbox">
                                                <input name="exportUser" value="2" type="radio"><lb:get key="label_cpd.label_core_cpt_d_management_134" />
                                            </label>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td valign="top">
                                            <span class="margin-left20">
                                                <select name="exportUserIds" id="exportUserIds" class="wzb-select" style="width:400px;height:120px;overflow-x:auto;" width="400" multiple="multiple">
                                                 
                                                </select>
                                            </span>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="padding-left:20px;">
                                            <input id="user_choose_btn" type="button" name="" value="<lb:get key="button_select"/>" class="btn wzb-btn-blue margin-right4 xueyuan">
                                            <input type="button" name=" " value="<lb:get key="button_del"/>" onclick="removeSelectedOptionsById('exportUserIds');" class="btn wzb-btn-blue margin-right4">
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <label class="wzb-form-checkbox">
                                                <input name="exportUser" value="3" type="radio"><lb:get key="label_cpd.label_core_cpt_d_management_152" />
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
                                        <td style="padding-left:20px;">
                                            <input id="group_choose_btn" type="button" name="" value="<lb:get key="button_select"/>" class="btn wzb-btn-blue margin-right4 xueyuan">
                                            <input type="button" name=" " value="<lb:get key="button_del"/>" onclick="removeSelectedOptionsById('exportGroupIds');" class="btn wzb-btn-blue margin-right4">
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </td>
                    </tr>

                    <tr>
                        <td class="wzb-form-label" valign="top"><span class="wzb-form-star">*</span><lb:get key="label_cpd.label_core_cpt_d_management_129" />：</td>
                           
                        <td class="wzb-form-control">
                            <table id="tableContent">
                                <tbody>
                                   <c:forEach items="${typelist}"  var="cpdTypes">
	                                    <tr>
	                                        <td><input name="exportCpdTypeIds" value="${cpdTypes.ct_id }" type="checkbox" checked="checked">
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
                        <td class="wzb-form-label" valign="top"><span class="wzb-form-star">*</span><lb:get key="label_cpd.label_core_cpt_d_management_158" />：</td>
                           
                        <td class="wzb-form-control">
                            <select name="sortOrderName" id="sortOrderName">
                                <option value="usr_ste_usr_id" selected="selected"><lb:get key="label_cpd.label_core_cpt_d_management_58" /></option>
                                <option value="usr_display_bil"><lb:get key="label_cpd.label_core_cpt_d_management_59" /></option>
                            </select>
                            <select name="sortOrderBy" id="sortOrderBy">
                                <option value="ASC" selected="selected"><lb:get key="label_cpd.label_core_cpt_d_management_159" /></option>
                                <option value="DESC"><lb:get key="label_cpd.label_core_cpt_d_management_160" /></option>
                            </select>
                        </td>
                    </tr>
                      
                    <tr>
                        <td class="wzb-form-label"></td>
                        <td class="wzb-form-control"><span class="wzb-form-star">*</span><lb:get key="label_cpd.label_core_cpt_d_management_136" /></td>
                    </tr>
                </table>
                  
                <div class="wzb-bar">
                     <input type="button" onclick="down_excel()" value="<lb:get key="global_export"/>" class="btn wzb-btn-blue wzb-btn-big margin-right15">
                     <input type="button" value="<lb:get key="button_cancel"/>" onclick="wb_utils_nav_go('FTN_AMD_TRAINING_REPORT_MGT',${prof.usr_ent_id},'${label_lan}');" class="btn wzb-btn-blue wzb-btn-big">
                </div>
            </form>    

    </div>
    <!-- 内容添加结束 -->

<script type="text/javascript">
    $("#ask").mousemove(function(){
        layer.tips(fetchLabel('label_core_cpt_d_management_135'),'#ask', {
        tips: [2,'rgba(128,128,128,0.9)'],
        time:50000
        });
    })
    $("#ask").mouseleave(function(){
        layer.tips();
    })
    
   function reloadTable() {
            var period = $('#cghi_period option:selected') .val();
            $.ajax({
                url : '${ctx}/app/admin/cpdAwardedHoursReport/getGroupTypeByPeriod',
                data:  {'period' : period},
                dataType : 'json',
                   type : 'POST',
                success : function(data) {
                    $("#tableContent").html("");
                     for(var i=0;i<data.cpdTypelist.length;i++){
                         p = {
                        		 cghi_ct_id : data.cpdTypelist[i].ct_id,
                                 cghi_license_alias : data.cpdTypelist[i].ct_license_alias,
                                 startTime : data.cpdTypelist[i].cpdPeriodVO.startDate,
                                 endTime : data.cpdTypelist[i].cpdPeriodVO.endDate
                         };
                         $("#tableContent").append($('#cpdType-template').render(p));
                     }
                }
            });
        };
</script>

</div>  <!-- wzb-panel End-->

<script type="text/javascript">
function down_excel(){
	var exportUser = $("input[name='exportUser']:checked").val();
	if(exportUser==2){
		if($("#exportUserIds option").size()<=0){
			Dialog.alert(fetchLabel('label_core_cpt_d_management_138'));
			return false;
		}
	}else if(exportUser==3){
		if($("#exportGroupIds option").size()<=0){
			//layer.alert('请选择用户组');
			Dialog.alert(fetchLabel('label_core_cpt_d_management_156'));
			return false;
		}
	}
	var type_length = $("input[name='exportCpdTypeIds']:checked").length;
	if(type_length == 0){
		Dialog.alert(fetchLabel('label_core_cpt_d_management_139'));
		return false;
	}
	
	 $("#exportUserIds option").each(function(index){  
	      $(this).attr('selected','selected'); 
	 })  
	 $("#exportGroupIds option").each(function(index){  
	      $(this).attr('selected','selected'); 
	 }) 
	
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
			  //console.log($('#formCondition').serialize());
				$.ajax({
			        url : "${ctx}/app/admin/cpdAwardedHoursReport/export",
			        type : 'POST',
			        data:  $('#formCondition').serialize(),
			        dataType : 'json',
			        traditional : true,
			        success : function(data) {
			        	downloadPath = data.fileUri;
			        	$('#download_loading').hide();
			        	$('#successMsg').show();
			        	$('#downloadBtn').removeAttr("disabled");
			        	$('#downloadBtn').click(function(){
			        		if(downloadPath!=''){
			        			window.open(downloadPath,'_blank' );
			        		}
			        	});
			        }
			     });
		  }
	});
}

function preSubmit(){
	
	return true;
}


</script>

<script id="cpdType-template" type="text/x-jsrender">
      <tr>
         <td><input name="exportCpdTypeIds" value="{{>cghi_ct_id}}" type="checkbox" checked="checked">
              {{>cghi_license_alias}}
              ( {{>startTime}} - {{>endTime}} )
         </td>
     </tr>
</script>
</body>
</html>