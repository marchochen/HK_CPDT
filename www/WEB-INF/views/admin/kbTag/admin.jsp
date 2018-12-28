<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<%@ include file="../../common/template.jsp"%>
<!DOCTYPE html>
<html>
<head>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_km_${lang}.js"></script>
<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-admin.css" />
</head>
<body>
<input type="hidden" name="belong_module" value="FTN_AMD_KNOWLEDGE_MGT"/>

   

	 <title:get function="global.FTN_AMD_KNOWLEDGE_MGT"/>
    <ol class="breadcrumb wzb-breadcrumb">
        <li><a href="javascript:wb_utils_gen_home(true);"><i class="fa wzb-breadcrumb-home fa-home"></i><lb:get key="global.lab_menu_started" /></a></li>
        
        <!-- 
                <li class="active"> <!--  <a href="javascript:void(0)"> </a><lb:get key="global.FTN_AMD_KNOWLEDGE_MGT"/></li>
         -->

        <li class="active"><lb:get key="label_km.label_core_knowledge_management_43" /></li>
    </ol>
    <!-- wzb-breadcrumb End-->

    <div class="panel wzb-panel">
        <div class="panel-heading"><lb:get key="label_km.label_core_knowledge_management_43" /></div>

        <div class="panel-body">
            <form class="form-search" onsubmit="return false;">
                <input type="text" id="searchText" name="searchText" class="form-control"
                    placeholder="<lb:get key="label_km.label_core_knowledge_management_64"/>" /> <input type="button" class="form-submit" value=""
                    onclick="reloadTable()">
                <button type="button" class="btn wzb-btn-yellow"
                    onclick="javascript:go('${ctx}/app/admin/kbTag/insert')">
                    <lb:get key="global.button_add" />
                </button>
            </form>

            <div id="tag_list"></div>

        </div>
    </div>
    <!-- wzb-panel End-->
    <script type="text/javascript">
    var searchTb;
    var encrytor = wbEncrytor();
    $(function() {
        searchTb = $("#tag_list").table({
            url : '${ctx}/app/admin/kbTag/listJson',
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
                name : 'tag_title',
                display : fetchLabel('label_core_knowledge_management_17'),
                width : '30%',
                format : function(data) {
                    p = {
                        className : 'text',
                        //href : '${ctx}/app/admin/kbTag/detail/' + data.tag_id,
                        title : data.tag_title
                    };
                    return $('#kbTagTitle-template').render(p);
                }
            },
            {
            	sortable : true,
                name : 'tcr_title',
                display : fetchLabel('global_traning_center'),
                align : 'left',
                Width : '20%',
                format : function(data) {
                    p = {
                        text : data.tcTrainingCenter.tcr_title
                    }
                    return $('#text-center-template').render(p);
                }
            },
            {
            	sortable : true,
                name : 'tag_knowledge_number',
                display : fetchLabel('label_core_knowledge_management_68'),
                align : "left",
                width : '15%',
                format : function(data) {
                    p = {
                        text : data.tag_knowledge_number ? data.tag_knowledge_number
                                : 0
                    };
                    return $('#text-center-template').render(p);
                }
            }, {
            	sortable : true,
                name : 'tag_create_datetime',
                display : fetchLabel('label_core_knowledge_management_69'),
                align : "left",
                width : '15%',
                format : function(data) {
                    p = {
                        text : data.tag_create_datetime.substring(0, 10)
                    };
                    return $('#text-center-template').render(p);
                }
            }, {
                display : '',
                width : '20%',
                align : 'right',
                format : function(data) {
                    p = {
                        tag_id : data.tag_id,
                        num : data.tag_knowledge_number ? data.tag_knowledge_number : 0
                    };
                    return $('#text-operate-template').render(p);
                }
            } ];

    function reloadTable() {
        var tag_title = $('#searchText').attr("value");
        $(searchTb).reloadTable({
            url : '${ctx}/app/admin/kbTag/listJson',
            params : {
                tag_title : tag_title
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
<button type="button" class="btn wzb-btn-blue" onclick="javascript:go('${ctx}/app/admin/kbTag/insert?encrypt_tag_id='+encrytor.cwnEncrypt('{{>tag_id}}'));">
    <lb:get key='global.button_update' />
</button>
<button type="button" class="btn wzb-btn-blue" onclick="operat('delete',{{>tag_id}},{{>num}})">
    <lb:get key='global.button_del' />
</button>
</script>
</body>
</html>