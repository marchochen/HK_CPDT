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
        <li class="active">
        	<lb:get key="global.FTN_AMD_CPT_D_LICENSE_LIST" />
        </li>
    </ol>
    <!-- wzb-breadcrumb End-->

    <div class="panel wzb-panel">
        <div class="panel-heading">
        <lb:get key="label_cpd.label_core_cpt_d_management_2" />
        </div>

        <div class="panel-body">
        <form class="form-search" onsubmit="return false;">
            <div style="float:left;"><lb:get key="label_cpd.label_core_cpt_d_management_3" />：
                <select name="ct_id_select"  id = "ct_id_select"  onchange="reloadTable()">
                    <option value="0"><lb:get key="label_cpd.label_core_cpt_d_management_54" /></option>
                    <c:forEach items="${cpdTypeList}"  var="cpdType">
				      <option value="${cpdType.ct_id }">${cpdType.ct_license_type }</option>
				    </c:forEach>
                </select>
                
            </div>
            <c:choose>
                <c:when test="${lang == 'en-us' }">
                    <input name="searchText" id = "searchText"      type="text" class="form-control" >
                </c:when>
                <c:otherwise>
                    <input name="searchText" id = "searchText"    type="text" class="form-control"  placeholder="<lb:get key="label_cpd.label_core_cpt_d_management_55" />">
                </c:otherwise>
            </c:choose>
                <input type="button" class="form-submit margin-right4"  onclick="reloadTable()">
                <button type="button" class="btn wzb-btn-yellow"  onclick="javascript:go('${ctx}/app/admin/cpdRegistrationMgt/insert')">
                  <lb:get key="label_cpd.label_core_cpt_d_management_56" />
                </button>
                <button type="button" class="btn wzb-btn-yellow"  onclick="javascript:go('${ctx}/app/admin/cpdRegistrationMgt/importCPDRegistration')">
                  <lb:get key="label_cpd.label_core_cpt_d_management_57" />
                </button>
                <%-- 
                <input type="button" class="btn wzb-btn-yellow margin-right4" value="<lb:get key="label_cpd.label_core_cpt_d_management_57" />">
                 --%>
            </form>

            <div id="cpdRegistrationMgt_list"></div>

        </div>
    </div>
    <!-- wzb-panel End-->
    <script type="text/javascript">
    var searchTb;
    var encrytor = wbEncrytor();
    $(function() {
         searchTb = $("#cpdRegistrationMgt_list").table({
            url : '${ctx}/app/admin/cpdRegistrationMgt/listJson',
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
            	sortable : true,
                name : 'usr_ste_usr_id',
                display : '<lb:get key="label_cpd.label_core_cpt_d_management_58" />',
                width : '12%',
                align : 'left',
                format : function(data) {
                    p = {
                        //className : 'text',
                        //title : data.cr_create_usr_ent_id
                        href : '${ctx}/app/admin/cpdRegistrationMgt/info?cr_id='+data.cr_id,
                        text : data.user.usr_ste_usr_id
                    };
                    return $('#cpdInfo-template').render(p);
                }
            },
            {
            	sortable : true,
                name : 'usr_display_bil',
                display : '<lb:get key="label_cpd.label_core_cpt_d_management_59" />',
                width : '10%',
                align : 'center',
                format : function(data) {
                    p = {
                        text : data.user.usr_display_bil
                    };
                    return $('#text-center-template').render(p);
                }
            },
            {
            	sortable : true, 
                name : 'ct_license_type',
                display : '<lb:get key="label_cpd.label_core_cpt_d_management_3" />',
                align : 'center',
                Width : '24%',
                format : function(data) {
                    p = {
                        text : data.cpdType.ct_license_type
                    }
                    return $('#text-center-template').render(p);
                }
            },
            {
            	sortable : true,
                name : 'cr_reg_number',
                display : '<lb:get key="label_cpd.label_core_cpt_d_management_60" />',
                align : "center",
                width : '12%',
                format : function(data) {
                    p = {
                        text :data.cr_reg_number
                    };
                    return $('#text-center-template').render(p);
                }
            }, 
            {
            	sortable : true,
                name : 'cr_reg_datetime',
                display : '<lb:get key="label_cpd.label_core_cpt_d_management_61" />',
                align : "center",
                width : '12%',
                format : function(data) {
                    p = {
                        text :data.cr_reg_datetime==null?"--":data.cr_reg_datetime.substring(0, 10)
                    };
                    return $('#text-center-template').render(p);
                }
            },
            {
                sortable : true,
                name : 'cr_de_reg_datetime',
                display : '<lb:get key="label_cpd.label_core_cpt_d_management_62" />',
                align : "center",
                width : '12%',
                format : function(data) {
                    p = {
                        text : data.cr_de_reg_datetime==null? "--":data.cr_de_reg_datetime.substring(0, 10)
                    };
                    return $('#text-center-template').render(p);
                }
            },
            {
                display : '',
                width : '12%',
                align : 'right',
                format : function(data) {
                    p = {
                    		cr_id:data.cr_id,
                    		ct_id:data.cpdType.ct_id,
                    		usr_ent_id : data.user.usr_ent_id,
                    };
                    return $('#text-operate-template').render(p);
                }
            } ];

    function reloadTable() {
        var searchText = $('#searchText').attr("value");
        var ct_id = $('#ct_id_select option:selected') .val()
        $(searchTb).reloadTable({
            url : '${ctx}/app/admin/cpdRegistrationMgt/listJson',
            params : {
            	searchText : searchText,
                ct_id:ct_id
            },
            dataType : 'json'
        });
    };
    
    function operat(type, tag_id, num) {
        if (type == 'delete') {
            var text = fetchLabel('label_core_knowledge_management_25');
            if (num > 0) {
                text = fetchLabel('label_core_knowledge_management_88');
            }
            if (confirm(text)) {
                $.ajax({
                    url : "${ctx}/app/admin/kbTag/detele?encrypt_tag_id=" + encrytor.cwnEncrypt(tag_id),
                    success : function(data) {
                        reloadTable();
                    }
                });
            }
        }
        return false;
    };
</script>
<script id="text-operate-template" type="text/x-jsrender">
<button type="button" class="btn wzb-btn-blue " onclick="javascript:go('${ctx}/app/admin/cpdRegistrationMgt/update?cr_id={{>cr_id}}&ct_id={{>ct_id}}&usr_ent_id={{>usr_ent_id}}');">
    <lb:get key='global.button_update' />
</button>
</script>

<script id="text-center-template" type="text/x-jsrender">
        <div>{{>text}}</div>
</script>
<script id="cpdNum-template" type="text/x-jsrender">
 <div class="datatable-table-column text-left"> 1
    <a class="wzb-images-down-b wzb-images-down-g" onclick="Javascript:;" href="javascript:void();" style="cursor:default;"></a>
    <a class="wzb-images-up-b" onclick="Javascript:;" href="javascript:void();" title="排序下移"></a>
 </div>  
</script>

<script id="cpdInfo-template" type="text/x-jsrender">
 <span class="datatable-table-column text-center"> <a href="javascript:go('{{>href}}');">{{>text}}</a>   </span>
</script>
</body>
</html>