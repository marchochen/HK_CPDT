<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<%@ include file="../../common/template.jsp"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-admin.css" />
<script type="text/javascript" src="${ctx}/js/wb_item.js"></script>

<title></title>
</head>
<body>
<title:get function="global.FTN_AMD_TRAINING_MGT"/>


<ol class="breadcrumb wzb-breadcrumb">
  <li><a href="javascript:wb_utils_gen_home(true);"><i class="fa wzb-breadcrumb-home fa-home"></i><lb:get key="global.lab_menu_started"/> </a></li>
  <li class="active">
<!--   作业批改 -->    <lb:get key="global.lab_assignment_correction"/>
  </li>
</ol> <!-- wzb-breadcrumb End-->

<div class="panel wzb-panel">
<div class="panel-heading">
<!-- 作业批改 -->
    <lb:get key="global.lab_assignment_correction"/>
</div>

<div class="panel-body">
    <span id="assList"></span>
</div>
</div>  <!-- wzb-panel End-->
<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_tm_${lang}.js"></script>

<script type="text/javascript">
var dt;
var itm_lst = new wbItem;
$(function(){
    var url = "/app/admin/module/assPage";
    dt = $("#assList").table({
        url : url,
        params : {
        },
        colModel : colModel,
        rp : 10,
        showpager : 5,
        usepager : true
    })
})
var colModel = [ {
    name : '"user.usr_display_bil"',
    display : cwn.getLabel('label_core_training_management_79'),
    width : '15%',
    sortable : true,
    align : "left",
    format : function(data) {
        p = {
            text : data.user.usr_display_bil
        };
        return $('#text-template').render(p);
    }
}, {
    name : '"itm.itm_title"',
    display : cwn.getLabel('label_core_training_management_74'),
    width : '20%',
    sortable : true,
    align : "left",
    format : function(data) {
        p = {
            className : 'wzb-link02',
            href : 'javascript:itm_lst.get_item_detail('+ data.itm.itm_id +')',
            title : data.itm.itm_title
        };
        return $('#a-template').render(p);
    }
}, {
    name : 'res_title',
    display : cwn.getLabel('label_core_training_management_75'),
    width : '15%',
    sortable : true,
    align : "left",
    format : function(data) {
        p = {
            text : data.res_title
        };
        return p.text;
    }
}, {
    name : 'mov_update_timestamp',
    display : cwn.getLabel('label_core_training_management_76'),
    width : '15%',
    sortable : true,
    align : "left",
    format : function(data) {
    	var time="--";
    	if(data.mov.mov_last_acc_datetime){
    		time=data.mov.mov_last_acc_datetime;
    	}
        p = {
        	
            text : time
        };
        return p.text;
    }
}, {
    display : cwn.getLabel('global_operation'),
    width : '25%',
    align : "right",
    format : function(data) {
        return $('#text-operate-template').render(data);
    }
} ]

function wbAssignmentGrade(id, usr_id, tkh_id){
    url = wb_utils_invoke_servlet('cmd','get_ass_rpt_usr','mod_id',id,'rpt_usr_id',usr_id,'attempt_nbr',1, 'stylesheet','ass_grading.xsl', 'tkh_id', tkh_id)
    window.location.href = url
}
</script>

<script id="text-operate-template" type="text/x-jsrender">
<button type="button" class="btn wzb-btn-blue btn-xs" onclick="javascript:wbAssignmentGrade({{:res_id}}, {{:user.usr_ent_id}}, {{:mov.mov_tkh_id}})">
    <lb:get key='global.global_appraise_score' />
</button>
</script>
</body>
</html>