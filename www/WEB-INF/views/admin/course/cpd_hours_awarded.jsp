<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<%@ include file="../../common/template.jsp"%>
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
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_rp_${lang}.js"></script>
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
            <li class="active"><lb:get key="label_cpd.label_core_cpt_d_management_119" /></li>
        </ol>
    <!-- wzb-breadcrumb End-->
    <div class="panel wzb-panel">
        <div class="panel-heading"><lb:get key="label_cpd.label_core_cpt_d_management_2" /></div>

        <div class="panel-body">
            <jsp:include page="../../admin/common/itm_gen_action_nav_share_new.jsp">
				<jsp:param value="121" name="cur_node_id"/>
			</jsp:include>
			           
            <div class="wzb-item-main margin-top28">
            <div class="wzb-ui-desc-text">   
            	<span style="color:#64BF0F;"><lb:get key="label_cpd.label_core_cpt_d_management_121" />：</span><lb:get key="label_cpd.label_core_cpt_d_management_120" />
            </div>

            <form class="form-search">
                <input id="search_txt" type="text" class="form-control" placeholder="<lb:get key="global.global_user_name_full_name" />">
                <input type="button" class="form-submit margin-right4" value="" onclick="reloadTable();">
                <input type="button" class="btn wzb-btn-orange margin-right4" id="cxjs" value="<lb:get key="label_cpd.label_core_cpt_d_management_122" />">
                <input type="button" class="btn wzb-btn-orange margin-right4" id="import" onclick="down_excel();"  value="<lb:get key="global.global_export" />">
                <!-- <input type="button" class="btn wzb-btn-orange" value="导入"> -->
            </form>
            
            <div style="overflow-x:auto;">
                <div class="datatable">
                    <div id ="cpdLrnAwardRecordTable" class="datatable-body">
                    
                    </div>
                </div>
            </div>

            <form class="form-search">*<span style="color:gray;"><lb:get key="label_cpd.label_core_cpt_d_management_126" /></span></form>

          </div>
              
            <div id="cpd_list"></div>

        </div><!-- 内容添加结束 -->
            
    </div>
    <!-- wzb-panel End-->
    <script type="text/javascript">
    var searchTb;
    var itm_id =${item.itm_id};
    var aeCPDGourpItemList ;
    $(function() {
    	
    	var parent_itm_id = '${item.parent.itm_id}';
    	if(parent_itm_id == ''){
    		parent_itm_id = 0;
    	}
    	
    	$.ajax({  
	        type:'post',      
		    url:'${ctx}/app/admin/aeItemCPDItem/listJson',  
		    data:{"itm_id":itm_id},  
		    dataType:'json',
		    success:function(data){ 
		    	var tableColumn = data.tableColumn;
		    	if(!jQuery.isEmptyObject(tableColumn)){
		    		for(var i = 0; i<tableColumn.length; i++){
		    			 var arr  =
		    	         {
		    			   sortable : false,
		                   name : tableColumn[i].columnKey,
		                   display : tableColumn[i].columnText,
		                   width : '200',
		                   align : 'center',
		                   format : function(data) {
		                	   var hoursMap = data.hoursMap;
		                	   var txt = hoursMap[this.name];
		                	   if(typeof(txt) == "undefined"){
		                		  txt = '0.0';
		                	   }
		                       p = {
		                           text : txt
		                       };
		                       return $('#text-center-template').render(p);
		                   }
		    	        };
		    	       colModel.push(arr);
		    		}

		    	}
		    	
		    	var lastArr = {
	                display : '',
	                width : '100',
	                align : 'right',
	                format : function(data) {
	                	var maplength  = tableColumn.length;
	                    p = {
	                		clar_usr_ent_id : data.clar_usr_ent_id,
	                		clar_itm_id : data.clar_itm_id,
	                		app_id : data.clar_app_id,
	                		maplength : maplength
	                    };
	                    return $('#text-operate-template').render(p);
	                }
			    };
	    	   colModel.push(lastArr);
	    	   loadTable();
		       }
		    	
		    }); 
    	
    	 $(document).keydown(function(event){
       	  if(event.keyCode ==13){
       		    reloadTable();
       		    return false;
       	  }
      	});
    	 
    });

    var colModel = [
            {
            	sortable : false,
                name : 'ct_display_order',
                display : '<lb:get key="label_cpd.label_core_cpt_d_management_58" />',
                width : '150',
                align : 'left',
                format : function(data) {
                	var usrName = data.usr_ste_usr_id ;
                	if(data.clar_manul_ind==1){
                		usrName='*'+usrName;
                	}
                    p = {
                    	text : usrName
                    };
                    return $('#text-center-template').render(p);
                }
            },
            {
            	sortable : false,
                name : 'ct_license_type',
                display : '<lb:get key="global.global_user_full_name" />',
                width : '150',
                align : 'center',
                format : function(data) {
                    p = {
                    		text : data.usr_display_bil
                    };
                    return $('#text-center-template').render(p);
                }
            },
            {
            	sortable : false, 
                name : 'ct_license_alias',
                display : '<lb:get key="global.global_user_group" />',
                align : 'center',
                width : '180',
                format : function(data) {
                    p = {
                    		text : data.usr_group
                    };
                    return $('#text-center-template').render(p);
                }
            }];

    
    function reloadTable() {
    	sortIndex = 1;
        $(searchTb).reloadTable({
            url : '${ctx}/app/admin/cpdLrnAwardRecord/listJson',
            params:{
            		"itm_id":itm_id,
            		"search_txt":$('#search_txt').val()
            		},
            dataType : 'json'
        });
	}
	function loadTable(){
        searchTb = $("#cpdLrnAwardRecordTable").table({
        url : '${ctx}/app/admin/cpdLrnAwardRecord/listJson',
        params:{'itm_id' : itm_id},
        dataType : 'json',
        colModel : colModel,
        rp : 10,
        showpager : 3,
        style:'scroll',
        usepager : true,
        onSuccess : function(data){
        	
        }
      }); 
   }
    
    $("#cxjs").click(function(){
        layer.open({
        type: 1,
        closeBtn: 0,
        title: '<lb:get key="label_cpd.label_core_cpt_d_management_121" />',
        btn:['<lb:get key="global.button_ok" />','<lb:get key="global.button_cancel" />'],
        area: ['500px', '280px'], //宽高
        content: '<div class="pop-up-word"><lb:get key="label_cpd.label_core_cpt_d_management_123" /></div>',
        yes: function(index){
			$.ajax({
				url : '${ctx}/app/admin/cpdLrnAwardRecord/reCalAllAwardCpd',
		        type : 'POST',
		        data:  {'itm_id' : itm_id},
		        dataType : 'json',
		        traditional : true,
		        success : function(data) {
		       		if(data.result=='OK'){
			            layer.msg('<lb:get key="label_cpd.label_core_cpt_d_management_124" />', {
			                icon: 16
			               // ,btn: ['OK']
			           		,yes: function(index){
			           		//	layer.close(index);
			           		//	document.location.reload(); 
			           		}
			            });
			             layer.close(index);
	           			document.location.reload(); 
		       		}else{
			            layer.msg('<lb:get key="label_cpd.label_core_cpt_d_management_125" />', {
			                icon: 2
			                ,btn: ['OK']
			            });
		       		}

		        }
		     });
          }
        
        });
    });


    	
	     	
    
    
</script>
    <script type="text/javascript">
     var usr_record_list_length = 0;
      function edit(usr_ent_id,itm_id,clar_app_id){
    	  
    	  $.ajax({  
  	        type:'post',      
  		    url:'${ctx}/app/admin/cpdLrnAwardRecord/getInfoByAppID',  
  		    data:{"app_id":clar_app_id,
  		    	  "usr_id":usr_ent_id,
  		    	  "itm_id":itm_id},  
  		    dataType:'json',
  		    success:function(data){ 
  		    	//console.log(data);
  		    	layer_open(data);
  		       }
  		    }); 
      }
      
      function layer_open(data){
      
    	  var list =data.info.cpdLrnAwardRecordList;
    	  usr_record_list_length = list.length;
    	  var infoHtml = '';
    	  //aeItemCPDGourpItem.acgi_award_core_hours
    	  for(var i = 0; i<usr_record_list_length; i++){
    		  p = {
    			 cg_alias : list[i].cg_alias,
    			 clar_award_core_hours : list[i].clar_award_core_hours,
    			 clar_award_non_core_hours : list[i].clar_award_non_core_hours,
    			 show_award_core_hours : list[i].show_award_core_hours,
    			 show_award_non_core_hours : list[i].show_award_non_core_hours,
    			 clar_id : list[i].clar_id,
    			 clar_cg_id : list[i].clar_cg_id,
    			 clar_ct_id : list[i].clar_ct_id,
    			 clar_acgi_id : list[i].clar_acgi_id,
    			 i : i,
    			 max_core_hours : list[i].aeItemCPDGourpItem.acgi_award_core_hours,
    			 max_non_core_hours : list[i].aeItemCPDGourpItem.acgi_award_non_core_hours
              };
    		  infoHtml += $('#text-input-template').render(p);
    	  }
    	  
	      layer.open({
	        type: 1,
	        closeBtn: 0,
	        title: '<lb:get key="global.button_update" /><lb:get key="label_cpd.label_core_cpt_d_management_119" />',
	        btn:['<lb:get key="global.button_ok" />','<lb:get key="global.button_cancel" />'],
	        area: ['620px', '600px'], //宽高
	        content: '<div style="padding:0 10px 12px;">'+
	                   '<form id="usrCPDRecods"  method="post">'+
	                    '<table cellpadding="0" cellspacing="0" class="margin-top20">'+
	                        '<tbody>'+
	                            '<tr>'+
	                                '<td class="wzb-form-label" width="60%"><lb:get key="global.global_user_name" />：</td>'+
	                                '<td class="wzb-form-control  text-left" width="20%">'+data.info.usr_ste_usr_id+
	                                '  <input type="hidden" id="clar_usr_ent_id" name="clar_usr_ent_id" value="'+data.info.clar_usr_ent_id+'">'+ 
	                                '  <input type="hidden" id="clar_itm_id" name="clar_itm_id" value="'+data.info.clar_itm_id+'">'+ 
	                                '  <input type="hidden" id="clar_app_id" name="clar_app_id" value="'+data.info.clar_app_id+'">'+ 
	                                '  <input type="hidden" id="clar_award_datetime" name="clar_award_datetime" value="'+data.info.clar_award_datetime.substring(0,10)+'">'+ 
	                                '</td>'+
	                                '<td class="wzb-form-control text-left" width="20%"></td>'+
	                            '</tr>'+
	                            '<tr>'+
	                                '<td class="wzb-form-label" width="60%"><lb:get key="global.global_user_full_name" />：</td>'+
	                                '<td class="wzb-form-control text-left" width="20%">'+data.info.usr_display_bil+'</td>'+
	                                '<td class="wzb-form-control text-left" width="20%"></td>'+
	                            '</tr>'+
	                        '</tbody>'+
	                    '</table>'+
	                    '<table cellpadding="0" cellspacing="0" class="margin-top20">'+
	                        '<tbody>'+
	                            '<tr>'+
	                                '<td class="wzb-form-label" width="60%"></td>'+
	                                '<td class="wzb-form-control text-left" width="20%"><lb:get key="label_cpd.label_core_cpt_d_management_193" /></td>'+
	                                '<td class="wzb-form-control text-left" width="20%"><lb:get key="label_cpd.label_core_cpt_d_management_194" /></td>'+
	                            '</tr>'+
	                               infoHtml +
	                        '</tbody>'+
	                    '</table>'+
	                '</form> </div>',
	              yes:function(index, layero){
		             	 var  positiveNum = /^[0-9]+\.?[0-9]{0,2}$/;
		         		 var pNum  = new RegExp(positiveNum);
					     var flag=false;
		         	     for(var i = 0; i < usr_record_list_length; i++){
		         		   var num	= document.getElementById('cpdLrnAwardRecordList['+i+'].clar_award_core_hours').value;
		         		   var non_num	= document.getElementById('cpdLrnAwardRecordList['+i+'].clar_award_non_core_hours').value;
							 var max_core_hours = $('#max_core_hours' + i).val();
							 var max_non_core_hours = $('#max_non_core_hours' + i).val();
							 var show = $('#show_award_core_hours'+i).val();
		         		   var non_show = $('#show_award_non_core_hours'+i).val();
		         		   if(num == "" && show == 'true'){
		         			  document.getElementById('cpdLrnAwardRecordList['+i+'].clar_award_core_hours').value = '0';
		         		   }
		         		   if(non_num == "" && non_show == 'true'){
		         			  document.getElementById('cpdLrnAwardRecordList['+i+'].clar_award_non_core_hours').value = '0';
		         		   }
		         		   var cg_name =$("#cg_name"+i).text();
		         		   if(num != '' && show == 'true'){
		         			   if(!pNum.test(num) || parseFloat(num) > parseFloat(max_core_hours)){
		         				  <%--layer.msg(cg_name+':'+'<lb:get key="label_cpd.label_core_cpt_d_management_193"/>'+','+'<lb:get key="label_cpd.label_core_cpt_d_management_217" />'+max_core_hours);--%>
		         	   			   <%--return;--%>
								   flag=true;
		         			   }
		         		   }
		         		   if(non_num != '' && non_show == 'true'){
		         			   if(!pNum.test(non_num) || parseFloat(non_num) > parseFloat(max_non_core_hours)){
		         				  <%--layer.msg(cg_name+':'+'<lb:get key="label_cpd.label_core_cpt_d_management_194"/>'+','+'<lb:get key="label_cpd.label_core_cpt_d_management_217" />'+max_non_core_hours);--%>
		            			   <%--return;--%>
								   flag=true;
		            		     }
		         		   }
		         		 }
					  if(flag){
						  layer.open({
							  type: 1,//弹出类型
							  area: ['360px', '220px'], //宽高
							  title : '<lb:get key="global.warning_notice"/>',//标题
							  content:'<div style="padding: 15px;">'+'<lb:get key="up_cptd"/>'+'</div>',
							  btn:['<lb:get key="global.button_ok" />','<lb:get key="global.button_cancel" />'],
							  yes:function(aa, layero){
								  $.ajax({
									  url : "${ctx}/app/admin/cpdLrnAwardRecord/updateCpdLrnAwardRecord",
									  data : $('#usrCPDRecods').serialize(),
									  dataType:"json",
									  success : function(data){
										  if(data.success == true){
											  layer.close(aa);
											  layer.close(index);
											  reloadTable();
//
										  }
									  }
								  });
							  }
						  });
					  }else{
						  $.ajax({
							  url : "${ctx}/app/admin/cpdLrnAwardRecord/updateCpdLrnAwardRecord",
							  data : $('#usrCPDRecods').serialize(),
							  dataType:"json",
							  success : function(data){
								  if(data.success == true){
									  reloadTable();
									  layer.close(index);
								  }
							  }
						  });
					  }
	            		
	             } //弹出层 点击确定执行  结束 
	        });   
       }
    </script>

<script id="text-operate-template" type="text/x-jsrender">
{{if maplength > 0}}
  <button type="button" class="btn wzb-btn-blue edit" onclick="javascript:edit({{>clar_usr_ent_id}},{{>clar_itm_id}},{{>app_id}});">
             <lb:get key="global.button_update" />
  </button>
{{/if}}
</script>

<script id="text-input-template" type="text/x-jsrender">
             <tr>
	           <td class="wzb-form-label" width="60%"> 
                  <apan id="cg_name{{>i}}">{{> cg_alias }}</span>
                  <input type="hidden" id="cpdLrnAwardRecordList[{{>i}}].clar_id" name="cpdLrnAwardRecordList[{{>i}}].clar_id" value="{{>clar_id}}">
                  <input type="hidden" id="cpdLrnAwardRecordList[{{>i}}].clar_acgi_id" name="cpdLrnAwardRecordList[{{>i}}].clar_acgi_id" value="{{>clar_acgi_id}}">
                  <input type="hidden" id="cpdLrnAwardRecordList[{{>i}}].clar_ct_id" name="cpdLrnAwardRecordList[{{>i}}].clar_ct_id" value="{{>clar_ct_id}}">
                  <input type="hidden" id="cpdLrnAwardRecordList[{{>i}}].clar_cg_id" name="cpdLrnAwardRecordList[{{>i}}].clar_cg_id" value="{{>clar_cg_id}}">
               </td>
	           <td class="wzb-form-control text-left" width="20%">
                    <input value="{{>show_award_core_hours}}" name="show_award_core_hours{{>i}}" id="show_award_core_hours{{>i}}"  type="hidden">
                    <input value="{{>max_core_hours}}" name="max_core_hours{{>i}}" id="max_core_hours{{>i}}"  type="hidden">
                    <input {{if !show_award_core_hours }} disabled {{/if}} class="wzb-inputText"  value="{{>clar_award_core_hours}}" name="cpdLrnAwardRecordList[{{>i}}].clar_award_core_hours" id="cpdLrnAwardRecordList[{{>i}}].clar_award_core_hours"  type="text" style="width:80px;"> hour(s)
               </td>
	           <td class="wzb-form-control text-left" width="20%">
                    <input value="{{>max_non_core_hours}}" name="max_non_core_hours{{>i}}" id="max_non_core_hours{{>i}}"  type="hidden"> 
                    <input value="{{>show_award_non_core_hours}}" name="show_award_non_core_hours{{>i}}" id="show_award_non_core_hours{{>i}}"  type="hidden">
                    <input {{if !show_award_non_core_hours }} disabled {{/if}} class="wzb-inputText" value="{{>clar_award_non_core_hours}}" name="cpdLrnAwardRecordList[{{>i}}].clar_award_non_core_hours" id="cpdLrnAwardRecordList[{{>i}}].clar_award_non_core_hours"  type="text" style="width:80px;"> hour(s)
               </td>
	        </tr>
</script>

<script type="text/javascript">
function down_excel(){
	
	var downloadPath ='';
	layer.open({
	 	  type: 1,//弹出类型 
	      area: ['500px', '274px'], //宽高
	      title : fetchLabel('label_core_report_140'),//标题 
		  content: '<div class="pop-up-word">'+
		 				'<span id="successMsg" style="display:none;"><lb:get key="label_rp.label_core_report_163"/></span>'+
		 				'<div id="download_loading" class="layer-loading"></div>'+
		 			'</div>'+
		 			'<div class="wzb-bar">'+
		 				'<input id="downloadBtn" disabled="disabled" value="<lb:get key="global.global_export"/>" type="button" class="btn wzb-btn-blue margin-right10 wzb-btn-big">'+
		 			'</div>',
		  success: function(layero, index){
				 $.ajax({
			        url : "${ctx}/app/admin/cpdLrnAwardRecord/export",
			        type : 'POST',
			        data:  {
	            		'itm_id':itm_id,
	            		'search_txt':$('#search_txt').val()
	            		},
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

</script>

</body>
</html>