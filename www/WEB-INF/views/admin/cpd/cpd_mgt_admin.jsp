<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<%@ include file="../../common/template.jsp"%>
<!DOCTYPE html>
<html>
<head>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_cpd_${lang}.js"></script>
<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-admin.css" />
</head>
<body>
<input type="hidden" name="belong_module" value="FTN_AMD_CPT_D_MGT"/>
   

	 <title:get function="global.FTN_AMD_CPT_D_MGT"/>
    <ol class="breadcrumb wzb-breadcrumb">
        <li><a href="javascript:wb_utils_gen_home(true);"><i class="fa wzb-breadcrumb-home fa-home"></i><lb:get key="global.lab_menu_started" /></a></li>
        <li class="active"><lb:get key="global.FTN_AMD_CPT_D_LIST" /></li>
    </ol>
    <!-- wzb-breadcrumb End-->

    <div class="panel wzb-panel">
        <div class="panel-heading"><lb:get key="label_cpd.label_core_cpt_d_management_2" /></div>

        <div class="panel-body">
            <form class="form-search" onsubmit="return false;">
                <button type="button" class="btn wzb-btn-yellow"
                    onclick="javascript:go('${ctx}/app/admin/cpdManagement/insert')">
                    <lb:get key="global.button_add" />
                </button>
            </form>

            <div id="cpd_list"></div>

        </div>
    </div>
    <!-- wzb-panel End-->
    <script type="text/javascript">
    var searchTb;
    var sortIndex = 1;
    $(function() {
        searchTb = $("#cpd_list").table({
            url : '${ctx}/app/admin/cpdManagement/listJson',
            dataType : 'json',
            colModel : colModel,
            rp : 100,
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
        
        $(document).keydown(function(event){
      	  if(event.keyCode ==13){
      		  reloadTable();
      	  }
     	});
        
    })

    var colModel = [
            {
            	sortable : false,
                name : 'ct_display_order',
                display : fetchLabel('label_core_cpt_d_management_6'),
                width : '12%',
                align : 'left',
                format : function(data) {
                	//console.log(arguments);
                    p = {
                        title : data.ct_display_order,
                        order : sortIndex++,
                        ctId  : data.ct_id
                    };
                    return $('#cpdNum-template').render(p);
                }
            },
            {
            	sortable : false,
                name : 'ct_license_type',
                display : fetchLabel('label_core_cpt_d_management_3'),
                width : '10%',
                align : 'center',
                format : function(data) {
                    p = {
                    	href : '${ctx}/app/admin/cpdManagement/detail/'+wbEncrytor().cwnEncrypt(data.ct_id),
                        text : data.ct_license_type
                    };
                    return $('#cpdInfo-template').render(p);
                }
            },
            {
            	sortable : false, 
                name : 'ct_license_alias',
                display : fetchLabel('label_core_cpt_d_management_4'),
                align : 'center',
                Width : '24%',
                format : function(data) {
                    p = {
                        text : data.ct_license_alias
                    }
                    return $('#text-center-template').render(p);
                }
            },
            {
            	sortable : false,
                name : 'ct_starting_month',
                display : fetchLabel('label_core_cpt_d_management_5'),
                align : "center",
                width : '12%',
                format : function(data) {
                	var month = get_month_label(data.ct_starting_month);
                    p = {
                        text : month
                    };
                    return $('#text-center-template').render(p);
                }
            }, {
            	sortable : false,
                name : 'ct_last_email_send_time',
                display : fetchLabel('label_core_cpt_d_management_10'),
                align : "center",
                width : '20%',
                format : function(data) {
                	if(data.ct_last_email_send_time == '' || data.ct_last_email_send_time == null){
                		data.ct_last_email_send_time = '--'
                	}
                	
                    p = {
                        text : data.ct_last_email_send_time
                    };
                    return $('#text-center-template').render(p);
                }
            }, {
                display : '',
                width : '22%',
                align : 'right',
                format : function(data) {
                    p = {
                    	ct_id :	data.ct_id,
                        num : '1'//替换为函数需要的参数 
                        
                    };
                    if(data.ct_trigger_email_type=='1'){
                    	return $('#text-operate-template').render(p);
                    }else{
                    	return $('#text-no-operate-template').render(p);
                    }
                    
                }
            } ];

    
    function reloadTable() {
    	sortIndex = 1;
        $(searchTb).reloadTable({
            url : '${ctx}/app/admin/cpdManagement/listJson',
            dataType : 'json'
        });
    };

    function cpdListSort(index,isUp){
    	var ct_type_Sort = '';
    	for(var i=1;i<sortIndex;i++){
    		var ct_type = '';
    		if(isUp == true){
    			if(i == index-1){
    				ct_type = $("table").find("tr").eq(i+1).find("td").eq(0).find("input").val();
    				ct_type_Sort += ct_type + ',';
            		ct_type = $("table").find("tr").eq(i).find("td").eq(0).find("input").val();
            		ct_type_Sort += ct_type + ',';
    			}else if(i != index-1 && i != index){
    				ct_type = $("table").find("tr").eq(i).find("td").eq(0).find("input").val();
    				ct_type_Sort += ct_type + ',';
    			}
    		}else if(isUp == false){
    			if(i == index){
    				ct_type = $("table").find("tr").eq(i+1).find("td").eq(0).find("input").val();
    				ct_type_Sort += ct_type + ',';
            		ct_type = $("table").find("tr").eq(i).find("td").eq(0).find("input").val();
            		ct_type_Sort += ct_type + ',';
    			}else if(i != index+1 && i != index){
    				ct_type = $("table").find("tr").eq(i).find("td").eq(0).find("input").val();
    				ct_type_Sort += ct_type + ',';
    			}
    		}
    		
    	}
    	$.ajax({
            url : "${ctx}/app/admin/cpdManagement/infoSort",
            data : {"ct_type_Sort" : ct_type_Sort},
            success : function(data) {
                reloadTable();
            }
        });
    	
    }
    	
	     	
    function sendEmail(cg_ct_id){
    	Dialog.confirm({text: fetchLabel('label_core_cpt_d_management_9'), callback: function (answer) {//请确定是否要向所有未完成的学员及其上司发送提醒邮件？
            if(!answer){
                return;
            }else{
            	var sendBtn = document.getElementById(cg_ct_id); 
            	sendBtn.setAttribute("class", "btn"); 
                sendBtn.disabled=true; 
                sendBtn.innerHTML="";
            	sendBtn.innerHTML='<lb:get key="label_cpd.label_core_cpt_d_management_176" />';
            	$.ajax({
                    url : "${ctx}/app/cpdOutstandingEmail/addEmailContentCpdOutstanding",
                    type : 'POST',
                    data:  {cg_ct_id:cg_ct_id},
                    dataType : 'json',
                    traditional : true,
                    success : function(result) {
                        //Dialog.alert("记录添加成功");
                    }
                 });     
            }
        }
       });
    	
    }
    
    function get_month_label(month){
    	var month_label = '';
    	switch(month){
	    	case 1:
	    		month_label = fetchLabel('label_core_cpt_d_management_222');
	        	break;
	    	case 2:
	    		month_label = fetchLabel('label_core_cpt_d_management_223');
	            break;
	    	case 3:
	    		month_label = fetchLabel('label_core_cpt_d_management_224');
	            break;
	    	case 4:
	    		month_label = fetchLabel('label_core_cpt_d_management_225');
	            break;
	    	case 5:
	    		month_label = fetchLabel('label_core_cpt_d_management_226');
	            break;
	    	case 6:
	    		month_label = fetchLabel('label_core_cpt_d_management_227');
	            break;
	    	case 7:
	    		month_label = fetchLabel('label_core_cpt_d_management_228');
	            break;
	    	case 8:
	    		month_label = fetchLabel('label_core_cpt_d_management_229');
	            break;
	    	case 9:
	    		month_label = fetchLabel('label_core_cpt_d_management_230');
	            break;
	    	case 10:
	    		month_label = fetchLabel('label_core_cpt_d_management_42');
	            break;
	    	case 11:
	    		month_label = fetchLabel('label_core_cpt_d_management_43');
	            break;
	    	case 12:
	    		month_label = fetchLabel('label_core_cpt_d_management_44');
	            break;
	    	default:
	    		month_label = '--'
	            break;
	    	}
    	
    	return month_label;
    }
    
</script>
<script id="text-no-operate-template" type="text/x-jsrender">
<button type="button" class="btn" onclick="javascript:sendEmail({{>ct_id}});" id="{{>ct_id}}" style="width:250px" disabled="true">
    <lb:get key='label_cpd.label_core_cpt_d_management_216' />
</button>
</script>
<script id="text-operate-template" type="text/x-jsrender">
<button type="button" class="btn wzb-btn-blue" onclick="javascript:sendEmail({{>ct_id}});" id="{{>ct_id}}" style="width:250px">
    <lb:get key='label_cpd.label_core_cpt_d_management_8' />
</button>
</script>

<script id="cpdNum-template" type="text/x-jsrender">
 <div class="datatable-table-column text-left"> {{>order}}
    
    <a class="wzb-images-down-b" onclick="Javascript:;" href="javascript:cpdListSort({{>order}},true);" ></a>
    <a class="wzb-images-up-b" onclick="Javascript:;" href="javascript:cpdListSort({{>order}},false);"></a>
    <input type="hidden" name="ct_id" value="{{>ctId}}"/>    
 </div>  
</script>

<script id="cpdInfo-template" type="text/x-jsrender">
 <span class="datatable-table-column text-center"> <a href="javascript:go('{{>href}}');">{{>text}}</a>   </span>
</script>

</body>
</html>