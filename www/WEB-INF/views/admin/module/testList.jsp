<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<%@ include file="../../common/template.jsp"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-admin.css" />
<title></title>
</head>
<body>

<title:get function="global.FTN_AMD_TRAINING_MGT"/>
<ol class="breadcrumb wzb-breadcrumb">
  <li><a href="javascript:wb_utils_gen_home(true);"><i class="fa wzb-breadcrumb-home fa-home"></i> <lb:get key="global.lab_menu_started"/> </a></li>
  <li class="active">
<!--   试卷批改 -->
    <lb:get key="global.lab_exam_correction"/>
  </li>
</ol> <!-- wzb-breadcrumb End-->

<div class="panel wzb-panel">
<div class="panel-heading">
<!-- 试卷批改 -->
    <lb:get key="global.lab_exam_correction"/>
</div>

<div class="panel-body">
    <span id="assList"></span>
</div>
</div>  <!-- wzb-panel End-->
<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<script type="text/javascript" src="${ctx}/js/wb_item.js"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_tm_${lang}.js"></script>
<script type="text/javascript">
var dt;
var itm_lst = new wbItem;
$(function(){
    var url = "/app/admin/module/testPage";
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
    format : function(data) {
        p = {
            text : data.user.usr_display_bil
        };
        return $('#text-template').render(p);
    }
}, {
    name : '"itm.itm_title"',
    display : cwn.getLabel('label_core_training_management_364'),
    width : '20%',
    sortable : true,
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
    display : cwn.getLabel('label_core_training_management_77'),
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
    name : '"mov.mov_update_timestamp"',
    display : cwn.getLabel('label_core_training_management_76'),
    width : '15%',
    sortable : true,
    align : "left",
    format : function(data) {
        p = {
            text : data.mov.mov_update_timestamp
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

function grade_submission(rpt_usr_id,mod_id,attempt_nbr,que_id_lst,tkh_id){
	if(que_id_lst == null || que_id_lst == ''){
		que_id_lst = 0;
	}
	var url = wb_utils_invoke_servlet('cmd','get_rpt_usr','rpt_usr_id',rpt_usr_id,'mod_id',mod_id,'attempt_nbr',attempt_nbr,'que_id_lst',que_id_lst,'tkh_id',tkh_id,'stylesheet','tst_grade.xsl','isExcludes', false)
	window.location.href = url;
}
</script>

<script id="text-operate-template" type="text/x-jsrender">
<button type="button" class="btn wzb-btn-blue btn-xs" onclick="javascript:grade_submission( {{:user.usr_ent_id}}, {{:res_id}}, {{:pgr_attempt_nbr}}, 0, {{:mov.mov_tkh_id}});">
    <lb:get key='global.global_appraise_score' />
</button>
</script>
</body>
</html>