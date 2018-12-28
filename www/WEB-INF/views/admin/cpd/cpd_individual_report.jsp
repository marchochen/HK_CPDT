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
<script type="text/javascript" src="${ctx}/static/admin/js/cpd/cpd_individual_report_ta.js"></script>
<script type="text/javascript">

function submitRes(){
	 var cghiCtIdArray = new Array();
	 var usertIdArray = new Array();
	 var groupInArray = new Array();
   var period = $('#cghi_period option:selected') .val();
   var cghi_ct_ids = document.getElementsByName("cghi_ct_id");
   var exportUser = $("input[name='exportUser']:checked").val();

   if(checkSubmit(exportUser,cghi_ct_ids)){
  	 	var exportUserIds = document.getElementById("exportUserIds").options;
  	 	var exportGroupIds = document.getElementById("exportGroupIds").options;
	    	if(exportUser==1){
              for(i=0;i<exportUserIds.length;i++){
              	usertIdArray[i] = exportUserIds[i].value;
              }
	 	 	}else if(exportUser==2){
              for(i=0;i<exportGroupIds.length;i++){
              	groupInArray[i] = exportGroupIds[i].value;
              }
	 	    }
	         var j = 0;
	         for( i=0;i<cghi_ct_ids.length;i++){
	             if(cghi_ct_ids[i].checked == true){
	                 cghiCtIdArray[j] = cghi_ct_ids[i].value;
	                 j++;
	             }
	         }
	    	layer.load();
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
		                    url : '${ctx}/app/admin/cpdIndividualReport/cpdIndividualReportPdf',
		                    type : 'POST',
		                    data:{
		                    	"period":period,
		                    	"cghiCtIdArray":cghiCtIdArray,
		                    	"exportUser":exportUser,
		                    	"userId":usertIdArray,
		                    	"groupId":groupInArray,
		                    	"formatType":$('input:radio[name="formatType"]:checked').val(),
		                    	"sortType":$('input:radio[name="sortType"]:checked').val()
		                    },
		                    dataType : 'json',
		                    traditional : true,
		                    success : function(data) {
		                    	//注册人数
		                    	var regUserNum = data.regUser
		                    	layer.closeAll("loading");
		                        $('#download_loading').hide();
		                    	if(regUserNum>0){
			                        downloadPath = data.fileUri;
			                        $('#successMsg').show();
			                        $('#downloadBtn').removeAttr("disabled");
			                        $('#downloadBtn').click(function(){
			                            if(downloadPath!=''){
			                            	window.open(downloadPath,'_blank' );
			                            }
			                        }); 
		                    	}else{
		                    		$(successMsg).html('<lb:get key="label_cpd.label_core_cpt_d_management_232"/>');
		                    		$('#successMsg').show();
		                    		$('#downloadBtn').hide();
		                    	}
		                    }
		                 });
		          }
		    });
   }
	    
}

function checkSubmit(exportUser,cghi_ct_ids){
	
	 if(exportUser==1){
	 		if($("#exportUserIds option").size()<=0){
	 			layer.alert(fetchLabel("label_core_report_141"),{title:fetchLabel("label_core_report_145")});
	 			return false;
	 		}
	 	}else if(exportUser==2){
	 		if($("#exportGroupIds option").size()<=0){
	 			layer.alert(fetchLabel("label_core_report_142"),{title:fetchLabel("label_core_report_145")});
	 			return false;
	 		}
	 }
	 
   var j = 0;
   for( i=0;i<cghi_ct_ids.length;i++){
       if(cghi_ct_ids[i].checked == true){
           j++;
       }
   }
   if(j==0){
       Dialog.alert(fetchLabel("label_core_cpt_d_management_139"));
       return false;
   }
   return true;
}
</script>

</head>
<body>

<input type="hidden" name="belong_module" value="FTN_AMD_TRAINING_REPORT_MGT"/>
   

	 <title:get function="global.FTN_AMD_TRAINING_REPORT_MGT"/>
    <ol class="breadcrumb wzb-breadcrumb">
        <li><a href="javascript:wb_utils_gen_home(true);"><i class="fa wzb-breadcrumb-home fa-home"></i><lb:get key="global.lab_menu_started" /></a></li>
        <li><a href="javascript:wb_utils_nav_go('FTN_AMD_TRAINING_REPORT_MGT',${prof.usr_ent_id},'${label_lan}');"><lb:get key="global.FTN_AMD_TRAINING_REPORT_MGT" /></a></li>
        <li class="active"><lb:get key="label_cpd.label_core_cpt_d_management_177" /></li>
    </ol>
    <!-- wzb-breadcrumb End-->

 <div class="panel wzb-panel">
    <!-- 内容添加开始 -->
	<div class="panel-body">
		<table class="margin-top15">
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
                   <td class="wzb-form-label" valign="top"><lb:get key="label_cpd.label_core_cpt_d_management_150" />：</td>
                    
                   <td class="wzb-form-control">
                       <table>
                           <tbody>
                               <tr>  
                                   <td>
                                       <label class="wzb-form-checkbox">
                                           <input name="exportUser" value="0" type="radio" checked="checked"><lb:get key="label_cpd.label_core_cpt_d_management_151" />
                                       </label>
                                   </td>
                               </tr>
                               <tr>
                                   <td>
                                       <label class="wzb-form-checkbox">
                                           <input name="exportUser" value="1" type="radio"><lb:get key="label_cpd.label_core_cpt_d_management_134" />
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
                                           <input name="exportUser" value="2" type="radio"><lb:get key="label_cpd.label_core_cpt_d_management_152" />
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
                      <table>
                          <tbody  id="tableContent">
                           <c:forEach items="${cpdType}"  var="cpdTypes">
                              <tr>
                                  <td><input name="cghi_ct_id" value="${cpdTypes.ct_id }" type="radio">
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
               	 <td class="wzb-form-label" valign="top">
               	 		<lb:get key="label_cpd.label_core_cpt_d_management_165" />：
               	 </td>
               	 <td class="wzb-form-control">
                       <table>
                           <tbody  id="tableContent">
                               <tr>
                                   <td><input name="formatType" value="0" type="radio" checked="checked"><lb:get key="label_cpd.label_core_cpt_d_management_166" /></td>
                               </tr> 
                              	<tr>
                                   <td><input name="formatType" value="1" type="radio"><lb:get key="label_cpd.label_core_cpt_d_management_167" /></td>
                               </tr> 
                           </tbody>
                       </table>
                   </td>
               </tr>
               
               <tr>
               	 <td class="wzb-form-label" valign="top">
               	 		<lb:get key="label_cpd.label_core_cpt_d_management_168" />：
               	 </td>
               	 <td class="wzb-form-control">
                       <table>
                           <tbody  id="tableContent">
                               <tr>
                                   <td><input name="sortType" value="0" type="radio" checked="checked"><lb:get key="label_cpd.label_core_cpt_d_management_169" /></td>
                               </tr> 
                              	<tr>
                                   <td><input name="sortType" value="1" type="radio"><lb:get key="label_cpd.label_core_cpt_d_management_170" /></td>
                               </tr> 
                           </tbody>
                       </table>
                   </td>
               </tr>
               
               <tr>
                   <td class="wzb-form-label"></td>
                   <td class="wzb-form-control"><span class="wzb-form-star">*</span><lb:get key="label_cpd.label_core_cpt_d_management_136" /></td>
               </tr>
		</table>
		
		<div class="wzb-bar">
            <input type="button" name=" " value="<lb:get key="label_cpd.label_core_cpt_d_management_130" />" class="btn wzb-btn-blue wzb-btn-big margin-right15" onclick="submitRes();">
            <input type="button" name=" " value="<lb:get key="label_cpd.label_core_cpt_d_management_137" />" class="btn wzb-btn-blue wzb-btn-big"  onclick="wb_utils_nav_go('FTN_AMD_TRAINING_REPORT_MGT',${prof.usr_ent_id},'${label_lan}');">
	    </div>

    </div>
    <!-- 内容添加结束 -->


</div>  <!-- wzb-panel End-->

<!-- 日期控件 start-->
<script>
!function(){
laydate.skin('molv');//切换皮肤，请查看skins下面皮肤库
laydate({elem: '#demo'});//绑定元素
laydate({elem:'#demo1'})
}();
//日期范围限制
var start = {
    elem: '#start',
    format: 'YYYY-MM-DD',
    min: laydate.now(), //设定最小日期为当前日期
    max: '2099-06-16', //最大日期
    istime: true,
    istoday: false,
    choose: function(datas){
         end.min = datas; //开始日选好后，重置结束日的最小日期
         end.start = datas //将结束日的初始值设定为开始日
    }
};
var end = {
    elem: '#end',
    format: 'YYYY-MM-DD',
    min: laydate.now(),
    max: '2099-06-16',
    istime: true,
    istoday: false,
    choose: function(datas){
        start.max = datas; //结束日选好后，充值开始日的最大日期
    }
};
laydate(start);
laydate(end);
//自定义日期格式
laydate({
    elem: '#test1',
    format: 'YYYY年MM月DD日',
    festival: true, //显示节日
    choose: function(datas){ //选择日期完毕的回调
        alert('得到：'+datas);
    }
});
//日期范围限定在昨天到明天
laydate({
    elem: '#hello3',
    min: laydate.now(-1), //-1代表昨天，-2代表前天，以此类推
    max: laydate.now(+1) //+1代表明天，+2代表后天，以此类推
});

window.onload=function(){
    var aqing=document.getElementById("qing")
    //console.log(aqing)
    var body2 = document.getElementById("by");
    

}

</script>
<!-- 日期控件 end-->

<script id="cpdType-template" type="text/x-jsrender">
<tr>
         <td><input name="cghi_ct_id" value="{{>cghi_ct_id}}" type="radio" >{{>cghi_license_alias}}
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
    
    function reloadTable() {
	    var period = $('#cghi_period option:selected') .val();
	    $.ajax({
	        url : '${ctx}/app/cpdIndividualReport/getcpdTypeRegHistory?period='+period,
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
	             var cpdTypes = $("input[name='cghi_ct_id']").get(0);
	             if ( typeof(cpdTypes)!="undefined" ){ 
	             	$("input[name='cghi_ct_id']").get(0).checked=true; 
	             }
	        }
	    });
	};
</script>


</body>
</html>