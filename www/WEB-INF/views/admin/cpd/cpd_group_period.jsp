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
        <li><a href="/app/admin/cpdManagement/index"><lb:get key="global.FTN_AMD_CPT_D_LIST" /></a></li>
        <li><a onclick="javascript:go('${ctx}/app/admin/cpdManagement/detail/'+wbEncrytor().cwnEncrypt(${cpdGroup.cpdType.ct_id})+'')" href="#">${cpdGroup.cpdType.ct_license_type}</a></li>
         
        <li><a onclick="javascript:go('${ctx}/app/admin/cpdManagement/detail/'+wbEncrytor().cwnEncrypt(${cpdGroup.cpdType.ct_id})+'')" href="#">${cpdGroup.cg_code}</a></li>
        
        <li class="active"><lb:get key="label_cpd.label_core_cpt_d_management_92" /></li>
    </ol>
    <!-- wzb-breadcrumb End-->

    <div class="panel wzb-panel">
        <div class="panel-heading"><lb:get key="label_cpd.label_core_cpt_d_management_2" /></div>

        <div class="panel-body">
            <form class="form-search" onsubmit="return false;">
                <button type="button" class="btn wzb-btn-yellow"
                    onclick="javascript:go('${ctx}/app/admin/cpdGroupPeriod/insert?cg_id='+wbEncrytor().cwnEncrypt(${cpdGroup.cg_id})+'')">
                    <lb:get key="label_cpd.label_core_cpt_d_management_282" />
                </button>
            </form>

            <div id="cpd_group_period_list"></div>

        </div>
    </div>
    <!-- wzb-panel End-->
    <script type="text/javascript">
    var searchTb;
    var cg_id = ${cpdGroup.cg_id}
    $(function() {
        searchTb = $("#cpd_group_period_list").table({
            url : '${ctx}/app/admin/cpdGroupPeriod/listJson',
            params : {'cg_id' : cg_id },
            dataType : 'json',
            colModel : colModel,
            rp : 10,
            showpager : 3,
            usepager : true,
            onSuccess : function(data){
            	
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
            	sortable : true,
                name : 'cgp_effective_time',
                display : fetchLabel('label_core_cpt_d_management_95'),
                width : '50%',
                align : 'left',
                format : function(data) {
                	if(data.cgp_effective_time.length > 10){
                		data.cgp_effective_time = data.cgp_effective_time.substring(0,10) 
                	}
                    p = {
                        title : data.cgp_effective_time,
                    };
                    return $('#text-time-template').render(p);
                }
            }, {
                display : '',
                width : '50%',
                align : 'right',
                format : function(data) {
                    p = {
                    		cgp_id : data.cgp_id,
                    		period : data.cgp_effective_time.substring(0,4)
                    };
                    return $('#text-operate-template').render(p);
                }
            } ];

    
    function reloadTable() {
    	sortIndex = 1;
        $(searchTb).reloadTable({
            url : '${ctx}/app/admin/cpdGroupPeriod/listJson',
            dataType : 'json'
        });
    };

	     	
    function del(id,period){
  	  Dialog.confirm({text:fetchLabel('label_core_cpt_d_management_100'), callback: function (answer) {
  			if(answer){
  				$.ajax({
  		              url : "${ctx}/app/admin/cpdGroupPeriod/detele",
  		              data : {'cgp_id' : id ,
  		            	      'ct_name' : '${cpdGroup.cpdType.ct_license_type}',
  		            	      'cg_name' : '${cpdGroup.cg_code}',
  		            	      'period' : period
  		            	      },
  		              dataType:"json",
  		              success : function(data){
  		                  if(data.success == true){
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
 <button type="button" onclick="javascript:go('${ctx}/app/admin/cpdGroupPeriod/insert?cgp_id='+wbEncrytor().cwnEncrypt({{>cgp_id}})+'')" class="btn wzb-btn-blue margin-right4">
	<lb:get key="global.button_update" />
 </button>
 <button type="button" class="btn wzb-btn-blue delete" onclick="javascript:del({{>cgp_id}},{{>period}})">
	<lb:get key="global.button_del" />
 </button>
</script>

<script id="text-time-template" type="text/x-jsrender">
 <span class="datatable-table-column text-left">{{>title}}</span>
</script>

<script id="cpdInfo-template" type="text/x-jsrender">
 <span class="datatable-table-column text-center"> <a href="javascript:go('{{>href}}');">{{>text}}</a>   </span>
</script>

</body>
</html>