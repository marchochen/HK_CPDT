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
        <li><a href="javascript:wb_utils_gen_home(true);">
            <i class="fa wzb-breadcrumb-home fa-home"></i>
            <lb:get key="global.lab_menu_started" /></a>
            <wzb:text></wzb:text>
        </li>
        <li class="active"><a href="javascript:cpdAwardCancel();"><lb:get key="global.FTN_AMD_CPT_D_IMPORT_AWARDED_HOURS" /></a> </li>
        <li class="active"> <lb:get key="label_cpd.label_core_cpt_d_management_236" /></li>
    </ol>
    <!-- wzb-breadcrumb End-->

    <div class="panel wzb-panel">
        <div class="panel-heading">
        <lb:get key="label_cpd.label_core_cpt_d_management_2" />
        </div>

        <div class="panel-body">
             <div  >
                <span class="wzb-ui-module-text"><lb:get key="label_cpd.label_core_cpt_d_management_257" />：<br/>
				<lb:get key="label_cpd.label_core_cpt_d_management_258" /><br/>
				<lb:get key="label_cpd.label_core_cpt_d_management_259" /><br/>
				</span>
			</div>
            <div id="cpdAwardHoursLog_list" ></div>

        </div>
    </div>
    <!-- wzb-panel End-->
    <script type="text/javascript">
    var searchTb;
    var encrytor = wbEncrytor();
    $(function() {
         searchTb = $("#cpdAwardHoursLog_list").table({
            url : '${ctx}/app/admin/cpdtImportAwardedHours/listJsonLog',
            dataType : 'json',
            colModel : colModel,
            rp : 10,
            showpager : 3,
            usepager : true
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
                name : 'ilg_create_timestamp',
                display : '<lb:get key="label_cpd.label_core_cpt_d_management_260" />',
                width : '15%',
                align : 'left',
                format : function(data) {
                    p = {
                        text : data.ilg_create_timestamp==null?"--":data.ilg_create_timestamp.substring(0, 16)
                    };
                    return $('#text-center-template').render(p);
                }
            },
            {
                sortable : false,
                name : 'ilg_process',
                display : '<lb:get key="label_cpd.label_core_cpt_d_management_261" />',
                width : '10%',
                align : 'center',
                format : function(data) {
                	process = '';
                	if(data.ilg_process=='IMPORT'){
                        process = '<lb:get key="label_cpd.label_core_cpt_d_management_262" />';
                	}else if(data.ilg_process=='EXPORT'){
                		process = '<lb:get key="label_cpd.label_core_cpt_d_management_130" />';
                	}
                    p = {
                        text : process
                    };
                    return $('#text-center-template').render(p);
                }
            },
            {
                sortable : false, 
                name : 'ilg_filename',
                display : '<lb:get key="label_cpd.label_core_cpt_d_management_263" />',
                align : 'center',
                Width : '25%',
                format : function(data) {
                    p = {
                        href : data.file_uri,
                        text : data.ilg_filename
                    }
                    if(data.file_uri!=''){
                        return $('#cpdInfo-template').render(p);
                    }else{
                    	return $('#text-center-template').render(p);
                    }
                    
                }
            },
            {
                sortable : false,
                name : 'ilg_desc',
                display : '<lb:get key="label_cpd.label_core_cpt_d_management_264" />',
                align : "center",
                width : '27%',
                format : function(data) {
                    p = {
                        text :data.ilg_desc
                    };
                    return $('#text-center-template').render(p);
                }
            }, 
            {
                sortable : false,
                name : 'usr_display_bil',
                display : '<lb:get key="label_cpd.label_core_cpt_d_management_265" />',
                align : "center",
                width : '15%',
                format : function(data) {
                    p = {
                        text :data.usr_display_bil
                    };
                    return $('#text-center-template').render(p);
                }
            },
            {
                sortable : false,
                name : 'operate',
                display : '<lb:get key="label_cpd.label_core_cpt_d_management_266" />',
                align : "center",
                width : '8%',
                format : function(data) {
                    p = {
                    	success_href:data.success_file_uri==null?'':data.success_file_uri,
                        unsuccess_href:data.unsuccess_file_uri==null?'':data.unsuccess_file_uri
                    };
                    return $('#text-operate-template').render(p);
                }
            }];

    function reloadTable() {
        $(searchTb).reloadTable({
            url : '${ctx}/app/admin/cpdImportAwardedHours/listJsonLog',
            params : {
                searchText : '',
            },
            dataType : 'json'
        });
    };
    
    function cpdAwardCancel(){
        url = wb_utils_controller_base + 'admin/cpdtImportAwardedHours/toCpdtHoursAwaededImport';
        window.location.href =  url;
    }
    
    
</script>
<script id="text-operate-template" type="text/x-jsrender">
{{if success_href}}
 <span class="datatable-table-column text-center"> <a href="{{>success_href}}"  target="_blank">success.txt</a>   </span>
{{/if}}
{{if success_href}}
    {{if unsuccess_href}}
    ,
    {{/if}}
{{/if}}
{{if unsuccess_href}}
 <span class="datatable-table-column text-center"> <a href="{{>unsuccess_href}}"  target="_blank">failure.txt</a>   </span>
{{/if}}
</script>

<script id="text-center-template" type="text/x-jsrender">
        <div>{{>text}}</div>
</script>

<script id="cpdInfo-template" type="text/x-jsrender">
 <span class="datatable-table-column text-center"> <a href="javascript:go('{{>href}}');">{{>text}}</a>   </span>
</script>
</body>
</html>