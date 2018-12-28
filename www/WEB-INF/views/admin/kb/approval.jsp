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
#menuTabs a {
	outline: none;
}

</style>
</head>
<body>

<input type="hidden" name="belong_module" value="FTN_AMD_KNOWLEDGE_MGT"/>



	 <title:get function="global.FTN_AMD_KNOWLEDGE_MGT"/>
    <ol class="breadcrumb wzb-breadcrumb">
        <li><a href="javascript:wb_utils_gen_home(true);"><i class="fa wzb-breadcrumb-home fa-home"></i><lb:get key="label_km.label_core_knowledge_management_1"/></a></li>
		<!-- 
				<li class="active"> <!-- <a href="javascript:void(0)">  </a><lb:get key="global.FTN_AMD_KNOWLEDGE_MGT"/></li>
		 -->

        <li class="active"><lb:get key="label_km.label_core_knowledge_management_45"/> </li>
    </ol>
    <!-- wzb-breadcrumb End-->

    <div class="panel wzb-panel">
        <div class="panel-heading">
<!--         知识审批 --><lb:get key="label_km.label_core_knowledge_management_45"/>
        </div>
        <div class="panel-body">
            <form class="form-search" onsubmit="return false;">
                <table>
                	<tbody>
	                	<tr>
		                	<td align="left">
			                	<!-- 类型 -->
				                <select id="kb_admin_type" style="width: 90px;margin-right:4px" class=" form-control " onchange="reloadTable()">
				                    <option value="">
				                        <lb:get key='label_km.label_core_knowledge_management_4' />
				                    </option>
				                    <option value="ARTICLE"><lb:get key='label_km.label_core_knowledge_management_5' /></option>
				                    <option value="DOCUMENT"><lb:get key='label_km.label_core_knowledge_management_6' /></option>
				                    <option value="VEDIO"><lb:get key='label_km.label_core_knowledge_management_7' /></option>
				                    <option value="IMAGE"><lb:get key='label_km.label_core_knowledge_management_8' /></option>
				                </select>
				                <!-- 状态 -->
				                <select id="kb_admin_app_status" style="width: 150px; margin-right:4px" class="form-control margin-right4" onchange="reloadTable()">
				                    <option value="">
				                        <lb:get key='label_km.label_core_knowledge_management_60' />
				                    </option>
				                    <option value="PENDING"><lb:get key='label_km.label_core_knowledge_management_61' /></option>
				                    <option value="APPROVED"><lb:get key='label_km.label_core_knowledge_management_62' /></option>
				                    <option value="REAPPROVAL"><lb:get key='label_km.label_core_knowledge_management_63' /></option>
				                </select>
		                    </td>
		                    <td align="right">
			                    <!-- 标题 -->
	                			<input type="text" id="kb_admin_title" class="form-control" placeholder="<lb:get key="label_km.label_core_knowledge_management_12"/>" />
	                			<input type="button" class="form-submit" onclick="reloadTable()" value="">
		                    </td>
	                    </tr>
                    </tbody>
                </table>
            </form>

            <div id="kbiList"></div>
        </div>
    </div>
    <!-- wzb-panel End-->

<script type="text/javascript">
    var dt;
    var encrytor = wbEncrytor();
    $(function() {
        //知识点
        var dtModel = [
                {
                	sortable : true,
                    name : 'kbi_title',
                    display : fetchLabel('label_core_knowledge_management_17'),
                    width : '20%',
                    format : function(data) {
                        var title = '';
                        if (data.kbi_title.length > 15) {
                            title = data.kbi_title  //.substring(0, 15) + '...'
                        } else {
                            title = data.kbi_title
                        }
                        p = {
                            text : title,
                            title_all : data.kbi_title,
                            kbi_id : encrytor.cwnEncrypt(data.kbi_id)
                        }
                        return $('#a-template').render(p);
                    }
                },
                {
                	sortable : true,
                    name : 'kbi_type',
                    display : fetchLabel('label_core_knowledge_management_18'),
                    align : 'left',
                    width : '6%',
                    format : function(data) {
                    	
						var type;
                    	
                    	if(data.kbi_type == 'DOCUMENT'){
                    		type = "label_core_knowledge_management_6"
                    	}
                    	else if (data.kbi_type == 'VEDIO'){
                    		type = "label_core_knowledge_management_7"
                    	}
                    	else if(data.kbi_type == 'IMAGE'){
                    		type = "label_core_knowledge_management_8"
                    	}
                    	else{
                    		type = "label_core_knowledge_management_5"
                    	}
                    		
                    	
                    	
                        p = {
                            text : fetchLabel(type)
                        }
                        return $('#text-center-template').render(p);
                    }
                },
                {
                	sortable : true,
                    name : 'usr_display_bil',
                    display : fetchLabel('label_core_knowledge_management_82'),
                    align : 'left',
                    width : '10%',
                    format : function(data) {
                        p = {
                            text : data.usr_display_bil
                        }
                        return $('#text-center-template').render(p);
                    }
                },
                {
                	sortable : true,
                    name : 'kbi_create_datetime',
                    display : fetchLabel('label_core_knowledge_management_83'),
                    align : 'left',
                    width : '10%',
                    format : function(data) {
                        p = {
                            text : data.kbi_create_datetime.substring(0, 10)
                        }
                        return $('#text-center-template').render(p);
                    }
                },
                {
                	sortable : true,
                    name : 'kbi_app_status',
                    display : fetchLabel('label_core_knowledge_management_60'),
                    align : 'left',
                    width : '8%',
                    format : function(data) {
                    	
        
                    	
                		var status;
                		
                		if(data.kbi_app_status == 'PENDING'){
                			
                			status = "label_core_knowledge_management_61"
                		}
                		else if(data.kbi_app_status == 'APPROVED'){
                			status = "label_core_knowledge_management_62"
                		}
                		else{
                			status = "label_core_knowledge_management_63"
                		}
                    	
                        p = {
                            text : fetchLabel(status)
                        }
                        return $('#text-center-template').render(p);
                    }
                },
                {
                	sortable : true,
                    name : 'kbi_approve_usr_display_bil',
                    display : fetchLabel('label_core_knowledge_management_84'),
                    align : 'left',
                    width : '16%',
                    format : function(data) {
                        p = {
                            text : data.kbi_approve_usr_display_bil ? data.kbi_approve_usr_display_bil
                                    : '--'
                        }
                        return $('#text-center-template').render(p);
                    }
                },
                {
                	sortable : true,
                    name : 'kbi_approve_datetime',
                    display : fetchLabel('label_core_knowledge_management_72'),
                    align : 'left',
                    width : '10%',
                    format : function(data) {
                        p = {
                            text : data.kbi_approve_datetime ? data.kbi_approve_datetime
                                    .substring(0, 10)
                                    : '--'
                        }
                        return $('#text-center-template').render(p);
                    }
                },
                {
                    display : '',
                    align : 'right',
                    width : '20%',
                    format : function(data) {
                        p = {
                            kbi_id : data.kbi_id
                        }
                        if (data.kbi_app_status == 'PENDING') {
                            return $('#text-operate-template').render(p);
                        } else if (data.kbi_app_status == 'REAPPROVAL') {
                            return $('#update-operate-template').render(p);
                        } else {
                            var str = '';
                            if (data.kbi_status == 'ON') {
                                str = '<button type="button" class="btn wzb-btn-blue" onclick="operat(\'unpublish\','
                                        + data.kbi_id + ')">';
                                str = str + fetchLabel('global_unpublish');
                                str = str + '</button>';
                            } else {
                                str = '<button type="button" class="btn wzb-btn-blue" onclick="operat(\'publish\','
                                        + data.kbi_id + ')">';
                                str = str + fetchLabel('global_publish');
                                str = str + '</button>';
                            }
                            return str
                                    + $('#update-operate-template').render(p);
                        }
                    }
                } ]
		
        var app_status = cwn.getUrlParam("app_status")?cwn.getUrlParam("app_status"):"";
        if(app_status != ""){
        	$("#kb_admin_app_status").find("option[value='"+app_status+"']").attr("selected",true);
        }
        
        dt = $("#kbiList").table({
            url : '${ctx}/app/admin/kb/indexJson',
            colModel : dtModel,
            rp : 10,
            hideHeader : false,
            usepager : true,
            params : {
            	kbi_app_status : app_status,
                source : 'approval'
            }
        });
        
        $(document).keydown(function(event){
        	  if(event.keyCode ==13){
        		  reloadTable();
        	  }
       	});
    })

    function reloadTable() {
        var app_status = $('#kb_admin_app_status').attr("value");
        var type = $('#kb_admin_type').attr("value");
        var title = $('#kb_admin_title').attr("value");
        $(dt).reloadTable({
            url : '${ctx}/app/admin/kb/indexJson',
            params : {
                kbi_app_status : app_status,
                kbi_type : type,
                kbi_title : title,
                source : 'approval'
            }
        });
    };

    function operat(type, kbi_id) {
        if (type == 'delete') {
            if (confirm(fetchLabel('label_core_knowledge_management_25'))) {
                $.ajax({
                    url : "${ctx}/app/admin/kb/delete?encrypt_kbi_id=" + encrytor.cwnEncrypt(kbi_id),
                    success : function(data) {
                        reloadTable();
                    }
                });
            }
        } else if (type == 'publish') {
            $.ajax({
                url : "${ctx}/app/admin/kb/publish",
                data : {
                	encrypt_kbi_id : encrytor.cwnEncrypt(kbi_id),
                    kbi_status : "ON"
                },
                success : function(data) {
                    reloadTable();
                }
            });

        } else if (type == 'unpublish') {
            $.ajax({
                url : "${ctx}/app/admin/kb/publish",
                data : {
                	encrypt_kbi_id : encrytor.cwnEncrypt(kbi_id),
                    kbi_status : "OFF"
                },
                success : function(data) {
                    reloadTable();
                }
            });
        } else {
            $.ajax({
                url : "${ctx}/app/admin/kb/approval",
                method : 'post',
                data : {
                    kbi_id : kbi_id,
                    kbi_app_status : type
                },
                success : function(data) {
                    reloadTable();
                }
            });
        }
        return false;
    };
</script>
<script id="image-center-template" type="text/x-jsrender">
        <div class=" "><img src="${ctx}/{{>text}}" alt=""/></div>
</script>
<script id="text-center-template" type="text/x-jsrender">
        <div class=" ">{{>text}}</div>
</script>
<script id="a-template" type="text/x-jsrender">
        <div><a href="${ctx}/app/admin/kb/view?source=approval&kbi_id={{>kbi_id}}" title="{{>title_all}}">{{>text}}</a></div>
</script>
<!-- 操作模版 -->
<script id="text-operate-template" type="text/x-jsrender">
<button type="button" class="btn wzb-btn-blue" onclick="operat('APPROVED',{{>kbi_id}})">
    <lb:get key='label_km.label_core_knowledge_management_65' />
</button>
<button type="button" class="btn wzb-btn-blue" onclick="operat('REAPPROVAL',{{>kbi_id}})">
    <lb:get key='label_km.label_core_knowledge_management_66' />
</button>
<button type="button" class="btn wzb-btn-blue" onclick="javascript:go('${ctx}/app/admin/kb/insert?encrypt_kbi_id='+encrytor.cwnEncrypt('{{>kbi_id}}')+'&source=approval');">
    <lb:get key='global.button_update' />
</button>
</script>
<script id="update-operate-template" type="text/x-jsrender">
<button type="button" class="btn wzb-btn-blue" onclick="javascript:go('${ctx}/app/admin/kb/insert?encrypt_kbi_id='+encrytor.cwnEncrypt('{{>kbi_id}}')+'&source=approval');">
    <lb:get key='global.button_update' />
</button>
<button type="button" class="btn wzb-btn-blue" onclick="operat('delete',{{>kbi_id}})">
    <lb:get key='global.button_del' />
</button>
</script>
</body>
</html>