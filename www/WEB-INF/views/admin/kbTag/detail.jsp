<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_km_${lang}.js"></script>

</head>
<body>
<input type="hidden" name="belong_module" value="FTN_AMD_KNOWLEDGE_MGT"/>


	 <title:get function="global.FTN_AMD_KNOWLEDGE_MGT"/>
<ol class="breadcrumb wzb-breadcrumb">
  <li><a href="javascript:wb_utils_gen_home(true);"><i class="fa wzb-breadcrumb-home fa-home"></i><lb:get key="global.lab_menu_started" /></a></li>
<!--   <li><a href="#">知识管理</a></li> -->
  <li class="active"><lb:get key="label_km.label_core_knowledge_management_43" /></li>
</ol> <!-- wzb-breadcrumb End-->

<div class="panel wzb-panel">
<div class="panel-heading"><lb:get key="label_km.label_core_knowledge_management_43" /></div>

<div class="panel-body">
<div>
                    <div class="text-right">
                        <div class="col-sm-offset-2 col-sm-10">
                            <button type="button" onclick="javascript:go('${ctx}/app/admin/kbTag/insert?tag_id=${tag.tag_id}')" class="btn wzb-btn-blue">
                                <lb:get key="global.button_update" />
                            </button>
                            <button type="button" onclick="operat('delete',${tag.tag_id})" class="btn wzb-btn-blue">
                                <lb:get key="global.button_del" />
                            </button>
                            <button type="button" onclick="javascript:history.go(-1)" class="btn wzb-btn-blue">
                                <lb:get key="button_back" />
                            </button>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <div class="quest">
                        <label class="col-sm-3  text-right"><lb:get key="label_km.label_core_knowledge_management_73" />:</label>
                        <div class="col-sm-7">
                            <label class="control-label">${tag.tag_title}</label>
                        </div>
                    </div>
                </div>
</div>  <!-- wzb-panel End-->
<script type="text/javascript">
    function operat(type, tag_id) {
        if (type == 'delete') {
            if (confirm(fetchLabel('label_core_knowledge_management_25'))) {
                $.getJSON("${ctx}/app/kb/tag/admin/detele?tag_id=" + tag_id,
                        function(data) {
                            if (!data.success) {
                                alert('不能删除该目录！');
                            } else {
                                go('${ctx}/app/kb/tag/admin/index')
                            }
                        });
            }
        }
        return false;
    };
</script>
</body>
</html>