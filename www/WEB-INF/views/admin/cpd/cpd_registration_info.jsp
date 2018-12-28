<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.cwn.wizbank.entity.*,java.util.*,com.cw.wizbank.util.*, com.cw.wizbank.qdb.*"%>
<%@ include file="../../common/taglibs.jsp"%>
<%@ include file="../../common/template.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<script type="text/javascript" src="${ctx}/js/jquery.selector.js"></script>
	<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
	<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_cpd_${lang}.js"></script>
	<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-admin.css" />
	
	<script type="text/javascript">
        
	     function formSubmit(){
	    	 $.ajax({
			        url : "${ctx}/app/admin/cpdManagement/save",
			        type : 'POST',
			        data:  $('#formCpdInfo').serialize(),
			        dataType : 'json',
			        traditional : true,
			        success : function(data) {
			        	alert(1);
			        }
			     });
	     }
	     
	    
	
	</script>
	
</head>
<body>
	<input type="hidden" name="belong_module" value="FTN_AMD_CPT_D_MGT"/>
    <input type="hidden"  id="endDate" value="${endDate}"/>
	<title:get function="global.FTN_AMD_CPT_D_MGT"/>
    <ol class="breadcrumb wzb-breadcrumb">
        <li><a href="javascript:wb_utils_gen_home(true);"><i class="fa wzb-breadcrumb-home fa-home"></i><lb:get key="global.lab_menu_started" /></a></li>
        <li><a href="/app/admin/cpdRegistrationMgt/list"><lb:get key="global.FTN_AMD_CPT_D_LICENSE_LIST" /></a></li>
        <li class="active">${detail.user.usr_ste_usr_id}</li>
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
        <table cellpadding="0" cellspacing="0" style="border-bottom:1px solid #ccc;">
            <tbody>
                <tr>
                    <td class="text-left" width="50%">
                        <span class="wzb-before"><lb:get key="label_cpd.label_core_cpt_d_management_64" /></span>
                    </td>
                    <td class="text-right" width="50%">
                        <input type="button" value="<lb:get key='global.button_update' />" class="btn wzb-btn-orange margin-right4" onclick="javascript:go('${ctx}/app/admin/cpdRegistrationMgt/update?cr_id=${detail.cr_id}&ct_id=${detail.cr_ct_id}&usr_ent_id=${detail.user.usr_ent_id}');"><input type="button" value="<lb:get key='global.button_del' />" class="btn wzb-btn-orange" id="delete">
                    </td>
                </tr>
            </tbody>
        </table>

        <table cellpadding="0" cellspacing="0" class="margin-top20">
            <tbody>
                <tr>
                    <td class="wzb-form-label"><lb:get key="label_cpd.label_core_cpt_d_management_58" />：</td>
                    <td class="wzb-form-control">${detail.user.usr_ste_usr_id}</td>
                </tr>
                <tr>
                    <td class="wzb-form-label"><lb:get key="label_cpd.label_core_cpt_d_management_59" />：</td>
                    <td class="wzb-form-control">${detail.user.usr_display_bil}</td>
                </tr>
                <tr>
                    <td class="wzb-form-label"><lb:get key="label_cpd.label_core_cpt_d_management_3" />：</td>
                    <td class="wzb-form-control">${detail.cpdType.ct_license_type}</td>
                </tr>
                <tr>
                    <td class="wzb-form-label"><lb:get key="label_cpd.label_core_cpt_d_management_67" />：</td>
                    <td class="wzb-form-control">${detail.cpdType.ct_license_alias}</td>
                </tr>
                <tr>
                    <td class="wzb-form-label"><lb:get key="label_cpd.label_core_cpt_d_management_60" />：</td>
                    <td class="wzb-form-control">${detail.cr_reg_number}</td>
                </tr>
                <tr>
                    <td class="wzb-form-label"><lb:get key="label_cpd.label_core_cpt_d_management_61" />：</td>
                    <td class="wzb-form-control"><fmt:formatDate value="${detail.cr_reg_datetime}" pattern="yyyy-MM-dd"/></td>
                </tr>
                <tr>
                    <td class="wzb-form-label"><lb:get key="label_cpd.label_core_cpt_d_management_62" />：</td>
                    <td class="wzb-form-control">
                    	<c:choose>
                    		<c:when test="${not empty detail.cr_de_reg_datetime }">
                    			<fmt:formatDate value="${detail.cr_de_reg_datetime}" pattern="yyyy-MM-dd"/>
                    		</c:when>
                    		<c:otherwise>
                    			--
                    		</c:otherwise>
                    	</c:choose>
                    </td>
                </tr>
            </tbody>
        </table>  

        <!-- 组别信息开始 -->
        <table cellpadding="0" cellspacing="0" class="margin-top20" style="border-bottom:1px solid #ccc;">
            <tbody>
                <tr>
                    <td class="text-left">
                        <div class="wzb-before"><lb:get key="label_cpd.label_core_cpt_d_management_68" /></div>
                    </td>
                </tr>
            </tbody>
        </table>
        
        
        <div class="datatable" style="margin-top:20px;">
            <div class="datatable-body">
                <div id="my_cpd_group_list"></div>
            </div>
        </div>
        
        <form class="form-search">*<span class="wzb-form-label"><lb:get key="label_cpd.label_core_cpt_d_management_77" /></span></form>
        <!-- 组别信息结束 -->
    </div>
    <!-- 内容添加结束 -->

    <script type="text/javascript">
        
    
    
        $("#delete").click(function(){
          /*   layer.open({
            type: 1,
            btn:['确定'],
            area: ['400px', '240px'], //宽高
            content: '<div class="pop-up-word">是否永久删除用户注册信息？</div>'
            }); */
            
            Dialog.confirm({text:"<lb:get key='label_cpd.label_core_cpt_d_management_79' />", callback: function (answer) {
                if(!answer){
                    return;
                }else{
                    go('${ctx}/app/admin/cpdRegistrationMgt/delRegistration?cr_id='+${detail.cr_id});
                } 
	       }
	       }); 
            
        })

        function updateClick(core_hours,non_core_hours,cgrh_id,cg_id,cr_id){
        	layer.open({
                type: 1,
                closeBtn: 0,
                title: '<lb:get key="label_cpd.label_core_cpt_d_management_76" />',
                btn:['<lb:get key="global.button_ok" />','<lb:get key="global.button_cancel" />'],
                area: ['500px', '240px'], //宽高
                content: '<div class="pop-up-word">'+
                    '<div class="margin-bottom20">'+
                        '<span class="wzb-form-label" style="width:200px;text-align:right;display:inline-block;"><lb:get key="label_cpd.label_core_cpt_d_management_74" />：</span>'+
                        '<span style="width:200px;text-align:left;">'+
                            '<input class="wzb-inputText" value="'+core_hours+'" name="cgrh_manul_core_hours" id="cgrh_manul_core_hours" type="text" style="width:150px;">'+
                        '</span>'+
                    '</div>'+
                    '<div>'+
                        '<span class="wzb-form-label" style="width:200px;text-align:right;display:inline-block;"><lb:get key="label_cpd.label_core_cpt_d_management_75" />：</span>'+
                        '<span style="width:200px;text-align:left;">'+
                            '<input class="wzb-inputText" value="'+non_core_hours+'" id="cgrh_manul_non_core_hours" name="cgrh_manul_non_core_hours" type="text" style="width:150px;">'+
                        '</span>'+
                    '</div>'+
                '</div>',
                yes:function(index, layero){
                	//alert($(#cgrh_manul_non_core_hours).val)
                	var cgrh_manul_non_core_hours = $('#cgrh_manul_non_core_hours').val();
                	var cgrh_manul_core_hours = $('#cgrh_manul_core_hours').val();
                	if(cgrh_manul_core_hours == '' ){
                		//Dialog.alert('不能为空');
                		cgrh_manul_core_hours = 0;
                		//layer.msg('不能为空');
                		//return;
                	}
                	//执行手动修改核心时数 开始
                		
                		var  positiveNum = /^[0-9]+\.?[0-9]{0,2}$/;
               		    var pNum  = new RegExp(positiveNum);
	               	    if(!pNum.test(cgrh_manul_core_hours) ){
	               	    	layer.msg('<lb:get key="label_cpd.label_core_cpt_d_management_206" />');
	          			   return;
	          		    }else if(cgrh_manul_non_core_hours != '' && !pNum.test(cgrh_manul_non_core_hours) ){
	          		    	layer.msg('<lb:get key="label_cpd.label_core_cpt_d_management_207" />');
		          		   return;
	          		    }
	               	    if(cgrh_manul_core_hours>=100){
                            layer.msg('<lb:get key="label_cpd.label_core_cpt_d_management_206" />');
                            return;
	               	    }
                        if(cgrh_manul_non_core_hours != '' && cgrh_manul_non_core_hours>=100){
                            layer.msg('<lb:get key="label_cpd.label_core_cpt_d_management_207" />');
                            return;
                        }
	               	    
                		$.ajax({
      		              url : "${ctx}/app/admin/cpdGroupRegistration/updateGroupRegHours",
      		              data : {'core_hours' : cgrh_manul_core_hours,
      		            	      'non_core_hours' : cgrh_manul_non_core_hours,
      		            	      'cgrh_id' : cgrh_id,
      		            	      'cg_id' : cg_id,
      		            	      'cr_id' : cr_id},
      		              dataType:"json",
      		              success : function(data){
      		                  if(data.success == true){
      		                	  reloadTable();
      		                	  layer.close(index);
      		                  }
      		              }
      		          }); 
                		
                	//执行手动修改核心时数 结束
                 } //弹出层 点击确定执行  结束 
               });//弹出层 
        }
    </script>

<script type="text/javascript">
var searchTb;
var usr_ent_id =  ${detail.user.usr_ent_id};
var cr_id = ${detail.cr_id};
var ct_id =${detail.cr_ct_id};
$(function() {
    searchTb = $("#my_cpd_group_list").table({
        url : '${ctx}/app/admin/cpdGroupRegistration/listJson',
        params : {'cgr_usr_ent_id' : usr_ent_id ,
        	      'cgr_cr_id' : cr_id,
        	      'ct_id' : ct_id},
        dataType : 'json',
        colModel : colModel,
        nexth : thmod,
        rp : 10,
        showpager : 3,
        isCpdInfo : true,
        usepager : true,
        onSuccess : function(data){
        	
        }
    });
    
    
   /*  jQuery("#my_cpd_group_list").jqGrid('setGroupHeaders', {
    	  useColSpanStyle: true, 
    	  groupHeaders:[
    	    {startColumnName: 'cg_code', numberOfColumns: 2, titleText: '测试合并'}
    	  ]
    	}); */
    
  /*   $("#my_cpd_group_list").setGroupHeaders(
     	   {
     		  useColSpanStyle : true,
     		  groupHeader: [{"numberOfColumns":2 , "titleText":"测试合并","startColumnName":"cg_code"}]
     	   }); */
    
})
     var thmod = [{
	        name : 'test',
	        display : '',
	        colspan : 4,
	        align : 'center'
          },
            {
		        name : 'test',
		        display : '<lb:get key="label_cpd.label_core_cpt_d_management_105" />',
    	        colspan : 2,
    	        align : 'center'
           }]


     var colModel = [
                    {
                    	sortable : false,
                        name : 'cg_code',
                        display : '<lb:get key="label_cpd.label_core_cpt_d_management_69" />',
                        width : '15%',
                        align : 'left',
                        rowspan : 2,
                        format : function(data) {
                            p = {
                            		text : data.cpdGroup.cg_alias
                            };
                            return $('#text-left-template').render(p);
                        }
                    },
                    {
                        sortable : false,
                        name : 'cgr_actual_date',
                        display : '<lb:get key="label_cpd.label_core_cpt_d_management_72" />',
                        width : '10%',
                        align : 'center',
                        rowspan : 2,
                        format : function(data) {
                            p = {
                                    text : ${detail.cpdType.ct_recover_hours_period}==0? "--":data.cgr_actual_date.substring(0,10)
                            };
                            return $('#text-center-template').render(p);
                        }
                    },
                    {
                    	sortable : false,
                        name : 'cgr_initial_date',
                        display : '<lb:get key="label_cpd.label_core_cpt_d_management_70" />',
                        width : '15%',
                        align : 'center',
                        rowspan : 2,
                        format : function(data) {
                            p = {
                            		text : data.cgr_initial_date.substring(0,10)
                            };
                            return $('#text-center-template').render(p);
                        }
                    },
                    {
                    	sortable : false,
                        name : 'cgr_expiry_date',
                        display : '<lb:get key="label_cpd.label_core_cpt_d_management_71" />',
                        width : '10%',
                        align : 'center',
                        rowspan : 2,
                        format : function(data) {
                            p = {
                            		text : data.cgr_expiry_date==null?"--":data.cgr_expiry_date.substring(0, 10)
                            };
                            return $('#text-center-template').render(p);
                        }
                    },
                    /* {
                    	sortable : false,
                        name : 'cg_display_order',
                        display : 'Current assessment period require',
                        width : '40%',
                        align : 'left',
                        format : function(data) {
                            p = {
                                title : data.cg_display_order
                            };
                            return $('#text-center-template').render(p);
                        }
                    }, */
                    {
                    	sortable : false,
                        name : 'cgrh_execute_core_hours',
                        display : '<lb:get key="label_cpd.label_core_cpt_d_management_74" />',
                        width : '20%',
                        align : 'center',
                        format : function(data) {
                            var valHours = "--";
                            if(data.cpdGroupRegHours!=null){
                            	if(data.cgr_expiry_date!=null){
                                    cgrExpiryDate = new Date(data.cgr_expiry_date.substring(0, 10));
                                    endDateInput = new Date($('#endDate').val());
                                    if(cgrExpiryDate.getTime() > endDateInput.getTime()){
                                    	valHours = data.cpdGroupRegHours.cgrh_execute_core_hours;
                                    }
                                }else{
                                	valHours = data.cpdGroupRegHours.cgrh_execute_core_hours;
                                }
                            }
                            
                            p = {
                                text  : valHours== null ? "0.0" : valHours,
                                cgrh_manul_ind : data.cpdGroupRegHours == null ? "0" : data.cpdGroupRegHours.cgrh_manul_ind
                            };
                            return $('#text-hours-template').render(p);
                        }
                    },
                    {
                    	sortable : false, 
                        name : 'cgrh_execute_non_core_hours',
                        display : '<lb:get key="label_cpd.label_core_cpt_d_management_75" />',
                        align : 'center',
                        Width : '20%',
                        format : function(data) {
                        	var valHours = "--";
                            if(data.cpdGroupRegHours!=null){
                                if(data.cgr_expiry_date!=null){
                                    cgrExpiryDate = new Date(data.cgr_expiry_date.substring(0, 10));
                                    endDateInput = new Date($('#endDate').val());
                                    if(cgrExpiryDate.getTime() > endDateInput.getTime()){
                                        valHours = data.cpdGroupRegHours.cgrh_execute_non_core_hours;
                                    }
                                }else{
                                	if(data.cpdGroup.cg_contain_non_core_ind==0){
                                		valHours = "--";
                                	}else{
                                		valHours = data.cpdGroupRegHours.cgrh_execute_non_core_hours;
                                	}
                                    
                                }
                            }
                            
                            p = {
                                text : valHours== null ? "--" : valHours,
                                cgrh_manul_ind : data.cpdGroupRegHours == null ? "0" : data.cpdGroupRegHours.cgrh_manul_ind
                            }
                            return $('#text-hours-template').render(p);
                        }
                    }, {
                        display : '',
                        width : '10%',
                        align : 'right',
                        rowspan : 2,
                        format : function(data) {
                        	if(data.cpdGroupRegHours!=null){
                        		if(data.cpdGroupRegHours.cgrh_execute_non_core_hours == undefined || data.cpdGroupRegHours.cgrh_execute_non_core_hours == ''){
                                    data.cpdGroupRegHours.cgrh_execute_non_core_hours = 0;
                                }
                        	}
                            p = {
                                core_hours : data.cpdGroupRegHours == null ? "0" : data.cpdGroupRegHours.cgrh_execute_core_hours,
                                non_core_hours : data.cpdGroupRegHours == null? "0" : data.cpdGroupRegHours.cgrh_execute_non_core_hours,
                                cgrh_id : data.cpdGroupRegHours == null ? "0" :data.cpdGroupRegHours.cgrh_id,
                                cg_id : data.cgr_cg_id,
                                cr_id : cr_id
                            };

                            if(data.cpdGroupRegHours!=null){
                                if(data.cgr_expiry_date!=null){
                                    cgrExpiryDate = new Date(data.cgr_expiry_date.substring(0, 10));
                                    endDateInput = new Date($('#endDate').val());
                                    if(cgrExpiryDate.getTime() > endDateInput.getTime()){
                                    	return $('#text-operate-template').render(p);
                                    }else{
                                        return "";
                                    }
                                }else{
                                	return $('#text-operate-template').render(p);
                                }
                            }else{
                                return "";
                            }
                            
                            
                        }
                    } ];

function reloadTable() {
    $(searchTb).reloadTable({
    	url : '${ctx}/app/admin/cpdGroupRegistration/listJson',
        params : {'cgr_usr_ent_id' : usr_ent_id ,
  	              'cgr_cr_id' : cr_id,
  	              'ct_id' : ct_id},
        dataType : 'json'
    });
};

</script>



</div>  <!-- wzb-panel End-->

        <!-- wzb-body End-->
<script id="text-left-template" type="text/x-jsrender">
		<div class="datatable-table-column text-left">{{>text}}</div>
</script>

<script id="text-right-template" type="text/x-jsrender">
		<div class="datatable-table-column text-right">{{>text}}</div>
</script>

<script id="text-hours-template" type="text/x-jsrender">
		<div class="datatable-table-column text-center">
          {{>text}}
          {{if cgrh_manul_ind == 1 }}
            <span>*</span>
          {{/if}}
       </div>
</script>

<script id="text-operate-template" type="text/x-jsrender">
<button type="button"class="btn wzb-btn-blue margin-right4" onclick="javascript:updateClick({{>core_hours}},{{>non_core_hours}},{{>cgrh_id}},{{>cg_id}},{{>cr_id}});">
	<lb:get key="label_cpd.label_core_cpt_d_management_76" />
 </button>
</script>

</body>
</html> 