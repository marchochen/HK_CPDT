<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>

<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-admin.css" />
<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_km_${lang}.js"></script>

<style type="text/css">
#menuTabs a { outline:none;}
.clickCatalog { background-color:gray;}
</style>
</head>
<body>
<input type="hidden" name="belong_module" value="FTN_AMD_KNOWLEDGE_MGT"/>


	 <title:get function="global.FTN_AMD_KNOWLEDGE_MGT"/>
<ol class="breadcrumb wzb-breadcrumb">
  <li><a href="javascript:wb_utils_gen_home(true);"><i class="fa wzb-breadcrumb-home fa-home"></i><lb:get key="label_km.label_core_knowledge_management_1"/></a></li>
<!--   <li><a href="#">知识管理</a></li> -->

<!-- 
 <li class="active">
 	<!-- 
 	<a href="javascript:void(0)"> </a>
 <lb:get key="global.FTN_AMD_KNOWLEDGE_MGT"/></li>
 -->

  <li class="active"><lb:get key="label_km.label_core_knowledge_management_2"/></li>
</ol> <!-- wzb-breadcrumb End-->

<div class="panel wzb-panel">
<div class="panel-heading">
<!-- 知识库 --><lb:get key="label_km.label_core_knowledge_management_2"/>
</div>

<div class="panel-body">
<div class="wzb-area clearfix">
<div class="wzb-item-left">
<h4 onclick="changCatalog(this,0)"><lb:get key="label_km.label_core_knowledge_management_3"/></h4>

<ul class="datatable" id="kb_item_catalog">
</ul>

</div>

<div class="wzb-area-content">
<form class="form-search form-tool" onsubmit="return false;">
		<select id="kb_admin_type" style="width: 112px;margin-right: 20px;" class="form-control" onchange="reloadTable()">
			<option value="">
				<lb:get key='label_km.label_core_knowledge_management_4' />
			</option>
			<option value="ARTICLE"><lb:get key='label_km.label_core_knowledge_management_5' /></option>
			<option value="DOCUMENT"><lb:get key='label_km.label_core_knowledge_management_6' /></option>
			<option value="VEDIO"><lb:get key='label_km.label_core_knowledge_management_7' /></option>
			<option value="IMAGE"><lb:get key='label_km.label_core_knowledge_management_8' /></option>
		</select>
    
        <select id="kb_admin_status" style="width:125px;margin-right:20px;" class="form-control" onchange="reloadTable()">
            <option value="">
                <lb:get key='label_km.label_core_knowledge_management_9' />
            </option>
            <option value="OFF"><lb:get key='label_km.label_core_knowledge_management_10' /></option>
            <option value="ON"><lb:get key='label_km.label_core_knowledge_management_11' /></option>
        </select>
         
        <div class="form-tool-right">
        <input type="text" class="form-control" id="kb_admin_title" placeholder="<lb:get key='label_km.label_core_knowledge_management_12'/>"><input type="button" class="form-submit" onclick="reloadTable()" value="">
            <button class="btn wzb-btn-yellow dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown">
    			<lb:get key="global.button_add" />
    		</button>
    		<ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu3" style="left:163px">
                <c:set var="add_kb_url">
                    ${ctx}/app/admin/kb/insert
                </c:set>
    			<li role="presentation"><a href="${add_kb_url}?kbi_type=ARTICLE">
    					<lb:get key="label_km.label_core_knowledge_management_5" />
    				</a></li>
    			<li role="presentation"><a href="${add_kb_url}?kbi_type=DOCUMENT">
    					<lb:get key="label_km.label_core_knowledge_management_6" />
    				</a></li>
    			<li role="presentation"><a href="${add_kb_url}?kbi_type=VEDIO">
    					<lb:get key="label_km.label_core_knowledge_management_7" />
    				</a></li>
    			<li role="presentation"><a href="${add_kb_url}?kbi_type=IMAGE">
    					<lb:get key="label_km.label_core_knowledge_management_8" />
    				</a></li>
    		</ul>
           <button type="button" class="btn wzb-btn-yellow" onclick="delKnowledgeByIds()"><lb:get key="label_km.label_core_knowledge_management_13" /></button>
        </div>
</form>

<div id="kbiList" class="wzb-table-input"></div>                                              

</div>

</div>
</div>
</div>  <!-- wzb-panel End-->

    <script type="text/javascript">
    var dt;
    var catalog;
    var kbc_id = ${kbc_id};
    var encrytor = wbEncrytor();
    $(function() {
        //知识点
        var dtModel = [
				{
                    display : '<input id="kbi_all" onclick="getCheckedIds()" type="checkbox" value="" name="kbi_all_checkbox" />',
                    align : 'left',
                    width : '13px',
                    format : function(data) {
	                   	p = {
	                               kbi_id : encrytor.cwnEncrypt(data.kbi_id)
                           }
                        return $('#input-template').render(p);
                    }
                },
                {
                    display : fetchLabel('label_core_knowledge_management_17'),
                    align : 'left',
                    width : '33%',
                    sortable : true,
                    name : 'kbi_title',
                    format : function(data) {
                    	var title = '';
                       // if (data.kbi_title.length > 14) {
                       //     title = data.kbi_title.substring(0, 14) + '...'
                       // } else {
                            title = data.kbi_title
                       // }
                        p = {
                            text : title,
                            title_all : data.kbi_title,
                            kbi_id : encrytor.cwnEncrypt(data.kbi_id)
                        }
                        return $('#title-template').render(p);
                    }
                },
                {
                    display : fetchLabel('label_core_knowledge_management_18'),
                    align : 'left',
                    width : '8%',
                    sortable : true,
                    name : 'kbi_type',
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
                    display : fetchLabel('label_core_knowledge_management_19'),
                    align : 'left',
                    width : '10%',
                    sortable : true,
                    name : 'kbi_access_count',
                    format : function(data) {
                        p = {
                            text : data.kbi_access_count ? data.kbi_access_count
                                    : 0
                        }
                        return $('#text-center-template').render(p);
                    }
                },
                {
                    display : fetchLabel('label_core_knowledge_management_20'),
                    align : 'left',
                    width : '8%',
                    sortable : true,
                    name : 's_cnt_like_count',
                    format : function(data) {
                        p = {
                            text : data.s_cnt_like_count ? data.s_cnt_like_count
                                    : 0
                        }
                        return $('#text-center-template').render(p);
                    }
                },
                {
                    display : fetchLabel('label_core_knowledge_management_9'),
                    align : 'left',
                    width : '8%',
                    sortable : true,
                    name : 'kbi_status',
                    format : function(data) {
                    	
                    	var status;
                    	
                    	if(data.kbi_status == 'ALL'){
                    		status = "label_core_knowledge_management_85"
                    	} 	
                    	else if(data.kbi_status == 'ON'){
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
                    display : fetchLabel('label_core_knowledge_management_23'),
                    width : '33%',
                    align : 'right',
                    format : function(data) {
                        var str = '';
                        if (data.kbi_status == 'ON') {
                            str = '<button type="button" class="btn wzb-btn-blue" onclick="operat(\'unpublish\','
                                    + data.kbi_id + ')">';
                            str = str + fetchLabel('global_publish_cancel');
                            str = str + '</button>';
                        } else {
                            str = '<button type="button" class="btn wzb-btn-blue" onclick="operat(\'publish\','
                                    + data.kbi_id + ')">';
                            str = str + fetchLabel('global_publish_ok');
                            str = str + '</button>';
                        }
                        p = {
                            kbi_id : data.kbi_id,
                            kbi_status : data.kbi_status
                        }
                        return str + $('#text-operate-template').render(p);
                    }
                } ]

        //知识目录
        var catalogModel = [ {
            display : fetchLabel('label_core_knowledge_management_17'),
            format : function(data) {
                var title = '';
                var css = ''
               // if (data.kbc_title.length > 9) {
                //   title = data.kbc_title.substring(0, 9) + '...'
                //} else {
                    title = data.kbc_title
                //}
                if (kbc_id == data.kbc_id) {
                    css = 'active'
                }
                p = {
                    text : title,
                    id : data.kbc_id,
                    css : css,
                    title_all : data.kbc_title
                }
                return $('#catalog-template').render(p);
            }
        } ]

        dt = $("#kbiList").table({
            url : '${ctx}/app/admin/kb/indexJson',
            colModel : dtModel,
            rp : 10,
            hideHeader : false,
            usepager : true,
            params : {
                kbi_app_status : 'APPROVED',
                kbc_id : kbc_id
            },
            sortname : 'kbi_create_datetime',
            sortorder : 'desc'
        });

        catalog = $("#kb_item_catalog").table({
            url : '${ctx}/app/kb/catalog/admin/indexJson',
            colModel : catalogModel,
            rp : 10,
            showpager : 3,
            hideHeader : true,
            usepager : true,
            useCss : "list-group wzb-list-1"
        });
        
        $(document).keydown(function(event){
        	  if(event.keyCode ==13){
        		  reloadTable();
        	  }
        });
        
    })

    function reloadTable() {
        var status = $('#kb_admin_status').attr("value");
        var type = $('#kb_admin_type').attr("value");
        var title = $('#kb_admin_title').attr("value");
        var id = kbc_id;
        $(dt).reloadTable({
            params : {
                kbi_status : status,
                kbi_type : type,
                kbi_title : title,
                kbc_id : id,
                kbi_app_status : 'APPROVED'
            },
            sortname : 'kbi_create_datetime',
            sortorder : 'desc'
        });
    };

    function changCatalog(thisObj, id) {
        $('#kb_admin_status').attr("value", '');
        $('#kb_admin_type').attr("value", '');
        $('#kb_admin_title').attr("value", '');
        $('.nav-stacked li').removeClass('active');
        $(".list-group-item").removeClass('active');
        $(thisObj).addClass('active');
        kbc_id = id;
        reloadTable()
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
        }
        return false;
    };

    function getCheckedIds() {
        var isChecked = $('#kbi_all').attr("checked");
        if (!isChecked) {
            $('input[name="kbi_checkbox"]').each(function() {
                $(this).attr("checked", false);
            });
        } else {
            $('input[name="kbi_checkbox"]').each(function() {
                $(this).attr("checked", true);
            });
        }
    }

    function delKnowledgeByIds() {
        var ids = '';
        $('input[name="kbi_checkbox"]:checked').each(function() {
            ids += $(this).val() + ',';
        });
        if (ids == '') {
            Dialog.alert(fetchLabel('label_core_knowledge_management_24'));
        } else {
            if (confirm(fetchLabel('label_core_knowledge_management_25'))) {
                $.post("${ctx}/app/admin/kb/deleteByIds", {
                    ids : ids
                }, function() {
                    reloadTable();
                    $('input[name="kbi_all_checkbox"]').each(function() {
                        $(this).attr("checked", false);
                    });
                });
            }
        }
    }
</script>
<script id="text-center-template" type="text/x-jsrender">
        <div>{{>text}}</div>
</script>
<script id="a-template" type="text/x-jsrender">
        <div><a href="${ctx}/app/admin/kb/view?source=index&kbi_id={{>kbi_id}}" title="{{>title_all}}">{{>text}}</a></div>
</script>
<script id="input-template" type="text/x-jsrender">
  <input type="checkbox" name="kbi_checkbox" value="{{>kbi_id}}" />
</script>
<script id="title-template" type="text/x-jsrender">
  <a href="${ctx}/app/admin/kb/view?source=index&kbi_id={{>kbi_id}}" title="{{>title_all}}">{{>text}}</a>
</script>
<script id="catalog-template" type="text/x-jsrender">
<a href="javascript:;" title="{{>title_all}}"><li role="presentation" class="{{>css}} list-group-item" style="border:0;margin:0;padding:0;" onclick="changCatalog(this,{{>id}})">{{>text}}</li></a>
</script>
<!-- 操作模版 -->
<script id="text-operate-template" type="text/x-jsrender">
<button type="button" class="btn wzb-btn-blue" onclick="javascript:go('${ctx}/app/admin/kb/insert?encrypt_kbi_id='+ encrytor.cwnEncrypt('{{>kbi_id}}'));">
    <lb:get key='label_km.label_core_knowledge_management_14' />
</button>
<button type="button" class="btn wzb-btn-blue" onclick="operat('delete',{{>kbi_id}})">
    <lb:get key='label_km.label_core_knowledge_management_15' />
</button>
</script>
</body>

</html>