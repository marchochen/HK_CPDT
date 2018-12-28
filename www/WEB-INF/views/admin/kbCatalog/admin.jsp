<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${ctx}/js/tree/css/ztree.css" />
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-admin.css" />
<script type="text/javascript" src="${ctx}/js/tree/js/jquery.ztree.core.js"></script>
<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_km_${lang}.js"></script>

<style type="text/css">
table a { color: #000 }
</style>
<script type="text/javascript">
function onClick(e, treeId, treeNode) {
    var zTree = $.fn.zTree.getZTreeObj("tcr_tcrTree");
    nodes = zTree.getSelectedNodes();
    var v = '';
    for (var i = 0, l = nodes.length; i < l; i++) {
        v += nodes[i].name + ",";
    }
    if (v.length > 0)
        v = v.substring(0, v.length - 1);
    var cityObj = $("#tcr");
    cityObj.html("<div id='tcr_text' class='tcr-text' style='overflow: hidden;'>"+v+"</div><div class='select-img'><img  src='/static/admin/images/wzb-select.png'></div>");
    $("#"+ treeId).parents(".cwn-dropdown").removeClass("open");

    $('#kbc_admin_status').attr("value", '');
    $('#kbc_admin_title').attr("value", '');
    tcr_id = treeNode.id;
    selectType = "";
    itemType = "";
    periods = "";
    if(treeNode.level == 0)
    {
    	tcr_id = 0; 
    } 
    var p = {
        tcr_id : tcr_id
    }
    $("#sel_tcr_id").val(tcr_id);
    $(dt).reloadTable({
        url : '${ctx}/app/admin/kbCatalog/indexJson',
        params : p
    });
    
    if(navigator.appName == "Microsoft Internet Explorer" && navigator.appVersion .split(";")[1].replace(/[ ]/g,"")=="MSIE9.0") 
    { 
    	$(".select-img").css("margin-top",-36);
    }
};

function insert() {
	var tcr_id = $("#sel_tcr_id").val();
	if(tcr_id == '') tcr_id = 0;
	window.location.href = '${ctx}/app/admin/kbCatalog/insert?tcr_id=' + tcr_id;
}
</script>
</head>
<body>
<input type="hidden" name="belong_module" value="FTN_AMD_KNOWLEDGE_MGT"/>



	 <title:get function="global.FTN_AMD_KNOWLEDGE_MGT"/>
<ol class="breadcrumb wzb-breadcrumb">
  <li><a href="javascript:wb_utils_gen_home(true);"><i class="fa wzb-breadcrumb-home fa-home"></i><lb:get key="label_km.label_core_knowledge_management_1"/> </a></li>
<!--   <li><a href="#">知识管理</a></li> -->
 
 <!-- 
   <li class="active"><!-- <a href="javascript:void(0)"> </a><lb:get key="global.FTN_AMD_KNOWLEDGE_MGT"/></li>
  -->

  <li class="active"><lb:get key="label_km.label_core_knowledge_management_3"/></li>
</ol> <!-- wzb-breadcrumb End-->

<div class="panel wzb-panel">
    <div class="panel-heading">
<!--     知识目录 --><lb:get key="label_km.label_core_knowledge_management_3"/>
    </div>
            <div class="panel-body">
                <form class="form-search" onsubmit="return false;">
                	<table>
                	<tr>
                	<td align="left">
                    <wzb:tcr-admin event="onClick" ></wzb:tcr-admin>
                    <input type="hidden" id="sel_tcr_id" value="">
					
                    <select id="kbc_admin_status" style="width: 125px;" class="form-control" onchange="reloadTable()">
                        <option value=""><lb:get key='label_km.label_core_knowledge_management_9' /></option>
                        <option value="OFF"><lb:get key='label_km.label_core_knowledge_management_10' /></option>
                        <option value="ON"><lb:get key='label_km.label_core_knowledge_management_11' /></option>
                    </select>
                    </td>
                    <td align="right">
                    <input type="text" id="kbc_admin_title" class="form-control" placeholder="<lb:get key="label_km.label_core_knowledge_management_64"/>" />
                    <input type="button"  onclick="reloadTable()" class="form-submit" value="">
                    <button type="button" class="btn wzb-btn-yellow" onclick="javascript:insert();">
                        <lb:get key="global.button_add" />
                    </button>
                    </td>
                    </tr>
                    </table>
                </form>
                <div class="clear"></div>
                <div id="kbcList"></div>
            </div>

</div>

<script type="text/javascript">
    var dt;
    var tcr_id;
    var timer = null;
    var encrytor = wbEncrytor();
    $(function() {
        //知识点
        var dtModel = [
                {
                	sortable : true,
                    name : 'kbc_title',
                    display : fetchLabel('label_core_knowledge_management_17'),
                    tdWidth : '25%',
                    format : function(data) {
                        p = {
                            text : data.kbc_title,
                            title_all : data.kbc_title,
                            kbc_id : data.kbc_id
                        }
                        return $('#a-template').render(p);
                    }
                },
                {
                	sortable : true,
                    name : 'tcr_title',
                    display : fetchLabel('global_traning_center'),
                    align : 'left',
                    tdWidth : '20%',
                    format : function(data) {
                        p = {
                            text : data.tcTrainingCenter.tcr_title
                        }
                        return $('#text-center-template').render(p);
                    }
                },
                {
                	sortable : true,
                    name : 'kbc_knowledge_number',
                    display : fetchLabel('label_core_knowledge_management_68'),
                    align : 'left',
                    tdWidth : '8%',
                    format : function(data) {
                        p = {
                            text : data.kbc_knowledge_number ? data.kbc_knowledge_number
                                    : 0
                        }
                        return $('#text-center-template').render(p);
                    }
                },
                {
                	sortable : true,
                    name : 'kbc_status',
                    display : fetchLabel('label_core_knowledge_management_9'),
                    align : 'left',
                    tdWidth : '8%',
                    format : function(data) {
                    	
                    	var status ;
                    	
                    	if(data.kbc_status == "ALL"){
                    		status = "label_core_knowledge_management_85"
                    	}
                    	else if(data.kbc_status == "ON"){
                    		status = "label_core_knowledge_management_11"
                    	}
                    	else{
                    		status = "label_core_knowledge_management_10"
                    	}
   
                        p = {
                            text : fetchLabel(status)
                        }
                        return $('#text-center-template').render(p);
                    }
                },
                {
                	sortable : true,
                    name : 'kbc_create_datetime',
                    display : fetchLabel('label_core_knowledge_management_69'),
                    align : 'left',
                    tdWidth : '10%',
                    format : function(data) {
                        p = {
                            text : data.kbc_create_datetime.substring(0, 10)
                        }
                        return $('#text-center-template').render(p);
                    }
                },
                {
                    display : fetchLabel('label_core_knowledge_management_23'),
                    align : 'right',
                    tdWidth : '24%',
                    format : function(data) {
                        var str = '';
                        if (data.kbc_status == 'ON') {
                            str = '<button type="button" class="btn wzb-btn-blue " onclick="operat(\'unpublish\','
                                    + data.kbc_id + ')">';
                            str = str + fetchLabel('global_unpublish');
                            str = str + '</button>';
                        } else {
                            str = '<button type="button" class="btn wzb-btn-blue " onclick="operat(\'publish\','
                                    + data.kbc_id + ')">';
                            str = str + fetchLabel('global_publish');
                            str = str + '</button>';
                        }
                        p = {
                            kbc_id : data.kbc_id,
                            kbc_status : data.kbc_status,
                            kbc_knowledge_number : data.kbc_knowledge_number ? data.kbc_knowledge_number : 0
                        }
                        if(data.kbc_type){
                            return '';
                        } else {
                            return str + $('#text-operate-template').render(p);
                        }
                    }
                } ]

        dt = $("#kbcList").table({
            url : '${ctx}/app/admin/kbCatalog/indexJson',
            colModel : dtModel,
            rp : 10,
            hideHeader : false,
            usepager : true
        });
        
        $(document).keydown(function(event){
        	  if(event.keyCode ==13){
        		  reloadTable();
        	  }
       });
        
    });

    function reloadTable() {
        var status = $('#kbc_admin_status').attr("value");
        var title = $('#kbc_admin_title').attr("value");
        $(dt).reloadTable({
            url : '${ctx}/app/admin/kbCatalog/indexJson',
            params : {
                kbc_status : status,
                kbc_title : title,
                tcr_id : tcr_id
            }
        });
    };

    function operat(type, kbc_id, kbc_knowledge_number) {
        if (type == 'delete') {
        	if(kbc_knowledge_number > 0){
        		 alert(fetchLabel('label_core_knowledge_management_87'));
        	}else{      		
	            if (confirm(fetchLabel('label_core_knowledge_management_25'))) {
	                $.getJSON(
	                        "${ctx}/app/admin/kbCatalog/delete?encrypt_kbc_id=" + encrytor.cwnEncrypt(kbc_id),
	                        function(data) {
	                            if (!data.success) {
	                                alert(fetchLabel('label_core_knowledge_management_87'));
	                            } else {
	                                reloadTable();
	                            }
	                        });
	            }
        	}
            return false;
        } else if (type == 'publish') {
            $.ajax({
                url : "${ctx}/app/admin/kbCatalog/publish",
                data : {
                	encrypt_kbc_id : encrytor.cwnEncrypt(kbc_id),
                    kbc_status : "ON"
                },
                success : function(data) {
                    reloadTable();
                }
            });
        } else if (type == 'unpublish') {
            $.ajax({
                url : "${ctx}/app/admin/kbCatalog/publish",
                data : {
                	encrypt_kbc_id : encrytor.cwnEncrypt(kbc_id),
                    kbc_status : "OFF"
                },
                success : function(data) {
                    reloadTable();
                }
            });
        }
        return false;
    };

</script>
<script id="text-center-template" type="text/x-jsrender">
        <div>{{>text}}</div>
</script>
<script id="a-template" type="text/x-jsrender">
<div>{{>text}}</div>
</script>
<script id="text-operate-template" type="text/x-jsrender">
<button type="button" class="btn wzb-btn-blue " onclick="javascript:go('${ctx}/app/admin/kbCatalog/insert?encrypt_kbc_id='+encrytor.cwnEncrypt('{{>kbc_id}}'));">
    <lb:get key='global.button_update' />
</button>
<button type="button" class="btn wzb-btn-blue " onclick="operat('delete',{{>kbc_id}},{{>kbc_knowledge_number}})">
    <lb:get key='global.button_del' />
</button>
</script>
</body>
</html>