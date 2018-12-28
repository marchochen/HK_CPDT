<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<%@ include file="../../common/template.jsp"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-admin.css" />
<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<title></title>
</head>
<body>
<input type="hidden" name="belong_module" value="FTN_AMD_SNS_MGT"/>

	 <title:get function="global.FTN_AMD_SNS_MGT"/>

<ol class="breadcrumb wzb-breadcrumb">
	<li><a href="javascript:wb_utils_gen_home(true);"><i class="fa wzb-breadcrumb-home fa-home"></i><lb:get key="global.lab_menu_started" /></a></li>
  	<li><a href="javascript:wb_utils_nav_go('FTN_AMD_Q_AND_A_MAIN', '${prof.usr_ent_id }', '${label_lan }')">
	<lb:get key="global.FTN_AMD_Q_AND_A_MAIN"/><!-- 问答管理 -->
  </a></li>
  <li class="active">
<!--   分类管理 --><lb:get key="label_cm.label_core_community_management_3"/>
  </li>
</ol> <!-- wzb-breadcrumb End-->

<div class="panel wzb-panel">
<div class="panel-heading">
<!-- 分类管理 --><lb:get key="label_cm.label_core_community_management_3"/>
</div>

<div class="panel-body">
<div class="form-search">
      <a class="btn wzb-btn-yellow"  id="addCatalog" >
<!--       添加分类 --><lb:get key="label_cm.label_core_community_management_55"/>
      </a>
</div>

<table class="table wzb-ui-table" id="tablelist">
     
</table>
<div class="modal fade" id="catalogModal"  tabindex="-1"  role="dialog" aria-labelledby="catalogModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg" style="width:600px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                    aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="catalogModalLabel">
<!--                 子分类管理 --><lb:get key="label_cm.label_core_community_management_56"/>
                </h4>
            </div>
            <div class="modal-body" style="height:300px;overflow-y:auto;">
                  <div class="">
                      <div class="form-search">
                           <button type="button" id="addSubCatalog" class="btn wzb-btn-yellow" >
<!--                            添加 --><lb:get key="global.button_add"/>
                           </button>
                      </div>
                      <div class="" id="subCatalog">
        
                      </div>  
                  </div>
            </div>
            <%-- <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal"><lb:get key="button_close"/></button>
            </div> --%>
        </div>
    </div>
</div>

</div>
</div>  <!-- wzb-panel End-->
<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery.prompt.js"></script>
<script type="text/javascript" src="${ctx}/static/js/front/course.js"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_cm_${lang}.js"></script>
<%-- <script type="text/javascript" src="${ctx}/static/admin/js/know_catalog.js"></script> --%>
<script type="text/javascript">
var dt, subDt, parentId;
$(function(){
	dt = $("#tablelist").table({
		url : '/app/admin/know/catalogPage',
		colModel : colModel,
		rp : 10,
		showpager : 5,
		sortname : 'kca_create_timestamp',
		sortorder : 'desc',
		usepager : true,
        params : {
            kcaId : 0,
            kcaType : 'CATALOG'
        }
	});
    
    $('#catalogModal').on('show.bs.modal', function (event) {
    	var button = $(event.relatedTarget);
    	parentId = $(button).attr("data-value");
    	subList();
    })

    $("#addSubCatalog").click(function(){
    	edit('', cwn.getLabel('label_core_community_management_138'));
    })
    
    $("#addCatalog").click(function(){
    	$('#catalogModal').modal('show');
        parentId = 0;
        edit('', cwn.getLabel('label_core_community_management_55'));

        //隐藏弹出
        $("#modalback").off('click').on('click', function(){
        	 $('#catalogModal').modal('hide');
        })
    })

    $(document).on('click', '.editCatalog', function(){
        $('#catalogModal').modal('show');
        parentId = -1;
        edit($(this).attr("data"));

        //隐藏弹出
        $("#modalback").off('click').on('click', function(){
             $('#catalogModal').modal('hide');
        })
    })
    
})
var Namecount;
function edit(id, title){
    $(this).hide();
    if(!title) {
    	title = cwn.getLabel('label_core_community_management_59')
    	if (parentId == 1) {
    		title = cwn.getLabel('label_core_community_management_193')
    	}
    }
    var p = { parentId : parentId};
    if(id && id > 0) {
        $.ajax({
            url : '/app/admin/know/catalog/detail/' + id,
            type : 'post',
            dataType : 'json',
            async : false,
            success : function(data){
                p = $.extend(p, data.catalog);
            }
        })
    }
    $("#subCatalog").empty().html($("#editTemplate").render(p));
    $(".kca_public_ind[value=" + p.kca_public_ind + "]").attr("checked", true);
    
	$("#addSubCatalog").hide();
    $("#catalogModalLabel").html(title);

    $("#modalback").off('click').on('click', function(){
    	subList();
    })

    $("#modalsure").off('click').on('click', function(){
    	
    	if($("input[name=kca_title]").val().length>0){
    		if(getChars($("input[name=kca_title]").val()) > 80)
        	{
        		Dialog.alert("<lb:get key='label_cm.label_core_community_management_216'/>");
        		return;
        	}
    		 $.ajax({
                 url : '/app/admin/know/catalog/checkCatalogName',
                 type : 'post',
                 dataType : 'json',
                 data : {
                     kca_title : $("input[name=kca_title]").val(),
                     kca_id : $("input[name=kca_id]").val(),
                 },
                 success : function(data){
                	if(data<1){
                		  $.ajax({
      		                url : '/app/admin/know/catalog/add',
      		                type : 'post',
      		                dataType : 'json',
      		                data : {
      		                    kca_title : $("input[name=kca_title]").val(),
      		                    kca_id : $("input[name=kca_id]").val(),
      		                    kca_public_ind : $("input[name=kca_public_ind]:checked").val(),
      		                    "knowCatalogRelation.kcr_ancestor_kca_id" : $("input[name=parent_kca_id]").val(),
      		                    kca_type : $("input[name=kca_type]").val()
      		                },
      		                success : function(data){
      		                    if(parentId < 1) {
      		                        $('#catalogModal').modal('hide');
      		                        $(dt).reloadTable();
      		                    } else {
      		                    	subList();
      		                    }
      		                }
      		            })
                	}else{
                		Dialog.alert("<lb:get key="label_cm.label_core_community_management_189"/>");
                		return;
                	}
                 }
             })
    	}else{
    		Dialog.alert("<lb:get key="label_cm.label_core_community_management_186"/>");
    		return;
    	}
    })
}

function subList(){
	$("#addSubCatalog").show();
	$("#catalogModalLabel").html(cwn.getLabel('label_core_community_management_56'));
    
	subDt = $("#subCatalog").empty().table({
        url : '/app/admin/know/catalogPage',
        colModel : subModel,
        rp : 5,
        showpager : 5,
        sortname : 'kca_create_timestamp',
        sortorder : 'desc',
        usepager : true,
        params : {
            kcaId : parentId,
            kcaType : 'NORMAL'
        }
    });
}


function del(kca_id, type) {
    if(!kca_id) return;
	Dialog.confirm({
		text : cwn.getLabel('warning_delete_notice'),
		callback : function(answer) {
			if (answer) {
				$.ajax({
					url : '/app/admin/know/catalog/del/' + kca_id,
					dataType : 'json',
					type : "post",
                    data : {
                        type : type
                    },
                    async : false,
					success : function(data) {
                        if(data.status == 'success') {
                            if(type == 'parent') {
                                $(dt).reloadTable();
                            } else if(type == 'son') {
                                subList();
                            }
                        }
					}
				})
			}
		}
	})
}

function publish(kca_id, type , isParent){
    $.ajax({
        url : '/app/admin/know/catalog/publish/' + kca_id,
        dataType : 'json',
        type : "post",
        data : {
            type : type
        },
        async : false,
        success : function(data) {
            if(data.status == 'success') {
                if(isParent == '1') {
                    $(dt).reloadTable();
                } else if(isParent == '0') {
                    subList();
                }
            }
        }
    })
}


var colModel = [
	{
		name : 'kca_title',
		display : cwn.getLabel('label_core_community_management_6'),
		width : '20%',
		sortable : true,
		format : function(data) {
/* 			p = {
				className : 'wzb-link02',
				href : '/app/admin/know/knowDetail/' + data.que_type
						+ '/' + data.que_id,
				title : data.kca_title
			}; */
			//return $('#a-template').render(p);
            var p = {
    				text : data.kca_title
                    }
            return $('#text-template').render(p);
		}
	}, {
		name : 'kca_que_count',
		display : cwn.getLabel('label_core_community_management_58'),
		width : '10%',
		sortable : true,
		format : function(data) {
			p = {
				text : data.kca_que_count
			};
			return $('#text-template').render(p);
		}
	}, {
		name : 'kca_public_ind',
		display : cwn.getLabel('global_status'),
		width : '10%',
		sortable : true,
		align : "center",
		format : function(data) {
			p = {
				text : data.kca_public_ind == 1 ?  cwn.getLabel('global_publish_on') : cwn.getLabel('global_publish_off')
			};
			return $('#text-center-template').render(p);
		}
	}, {
		name : 'kca_update_timestamp',
		display : cwn.getLabel('label_core_community_management_217'),
		width : '15%',
		sortable : true,
		align : "center",
		format : function(data) {
			p = {
				text : data.kca_update_timestamp.substring(0, 10)
			};
			return $('#text-center-template').render(p);
		}
	}, {
		display : cwn.getLabel('global_operation'),
		width : '20%',
		align : "center",
		format : function(data) {
			//var unclassified = cwn.getLabel('lab_kb_unclassified');
			if(data.kca_title == '未分类' || data.kca_title == '未分類' || data.kca_title == 'Unclassified'  ){
				data.isTemp = 1;
			}else{
				data.isTemp = 0;
	            if(data.kca_public_ind == 0) {
	                data.btn_name = cwn.getLabel('global_publish_ok');
	                data.btn_type = 1;    
	            } else {
	                data.btn_name = cwn.getLabel('global_publish_cancel');
	                data.btn_type = 0;    
	            }
			}
			return $('#buttonTemplate').render(data);
		}
	} ]

	var subModel = [ {
		name : 'kca_type',
		display : cwn.getLabel('label_core_community_management_17'),
		width : '20%',
		sortable : true,
		format : function(data) {
			/* 			p = {
			className : 'wzb-link02',
			href : '/app/admin/know/knowDetail/' + data.que_type
					+ '/' + data.que_id,
			title : data.kca_title
		}; */
		//return $('#a-template').render(p);
        var p = {
				text : data.kca_title
                }
        return $('#text-template').render(p);
		}
	}, {
		name : 'KCA_TITLE',
		display : cwn.getLabel('label_core_community_management_58'),
		width : '10%',
		sortable : true,
		format : function(data) {
			p = {
				text : data.kca_que_count
			};
			return $('#text-template').render(p);
		}
	}, {
		name : 'kca_public_ind',
		display :  cwn.getLabel('global_status'),
		width : '10%',
		sortable : true,
		align : "center",
		format : function(data) {
			p = {
				text : data.kca_public_ind == 1 ? cwn.getLabel('global_publish_on') : cwn.getLabel('global_publish_cancel')
			};
			return $('#text-center-template').render(p);
		}
	}, {
		name : 'kca_update_timestamp',
		display : cwn.getLabel('label_core_community_management_217'),
		width : '15%',
		sortable : true,
		align : "center",
		format : function(data) {
			p = {
				text : data.kca_update_timestamp.substring(0, 10)
			};
			return $('#text-center-template').render(p);
		}
	}, {
		display : cwn.getLabel('global_operation'),
		width : '20%',
		align : "center",
		format : function(data) {
            if(data.kca_public_ind == 0) {
                data.btn_name = cwn.getLabel('global_publish_ok');
                data.btn_type = 1;    
            } else {
                data.btn_name = cwn.getLabel('global_publish_cancel');
                data.btn_type = 0;    
            }
			return $('#subButtonTemplate').render(data);
		}
	} ]
</script>
<script type="text/x-jsrender" id="buttonTemplate">
 {{if isTemp == 0 }}
  <div style="white-space:nowrap; text-align:right">
    <input type="button" onclick="javascript:publish({{:kca_id}}, {{:btn_type}}, 1);" class="btn wzb-btn-blue" value="{{:btn_name}}" name="frmSubmitBtn">
    <input type="button" onclick="javascript:void(0);" class="btn wzb-btn-blue editCatalog" data="{{:kca_id}}" value="<lb:get key='global.button_update'/>" name="frmSubmitBtn">
    <input type="button" onclick="javascript:del({{:kca_id}}, 'parent');" class="btn wzb-btn-blue" value="<lb:get key='global.button_del'/>" name="frmSubmitBtn">
    <a class="btn wzb-btn-blue subcatalog" data-value="{{:kca_id}}" data-toggle="modal" data-target="#catalogModal" ><lb:get key='label_cm.label_core_community_management_56'/></a>
   </div>
 {{else}}
  <div style="white-space:nowrap; text-align:center">
	  <lb:get key='label_cm.label_core_community_management_222'/>
  </div>  
 {{/if}}
</div>
</script>
<script type="text/x-jsrender" id="subButtonTemplate">
<div style="white-space:nowrap; text-align:right">
<input type="button" onclick="javascript:publish({{:kca_id}}, {{:btn_type}}, 0);"  class="btn wzb-btn-blue" value="{{:btn_name}}" name="frmSubmitBtn">
<input type="button" onclick="javascript:edit({{:kca_id}});" class="btn wzb-btn-blue" value="<lb:get key='global.button_update'/>" name="frmSubmitBtn">
<input type="button" onclick="javascript:del({{:kca_id}}, 'son');" class="btn wzb-btn-blue" value="<lb:get key='global.button_del'/>" name="frmSubmitBtn">
</div>
</script>
<script type="text/x-jsrender" id="editTemplate">
    <div class="wzb-model-3">
        <table>
            <tr>
                 <td class="wzb-form-label"><span class="wzb-form-star">*</span><lb:get key='label_cm.label_core_community_management_57'/>：</td>
                 
                 <td class="wzb-form-control">
                     <div class="wzb-selector">
					 <input type="text" class="form-control" name="kca_title" id="" value="{{:kca_title}}">
					 <input type="hidden" name="kca_id" id="" value="{{:kca_id}}">
					 <input type="hidden" name="parent_kca_id" value="{{:parentId}}">
					 {{if parentId > 0 }}
					 <input type="hidden" name="kca_type" value="NORMAL">
					 {{else}}
					 <input type="hidden" name="kca_type" value="CATALOG">
					 {{/if}}
					 </div>
                 </td>
            </tr>
            
            <tr>
                 <td class="wzb-form-label"><span class="wzb-form-star">*</span><lb:get key='global.global_status'/>：</td>
                 
                 <td class="wzb-form-control">
                     <label for="psde" class="wzb-input-label"><input type="radio" class="kca_public_ind" name="kca_public_ind" value="1" checked="checked" ><lb:get key='global.global_publish_on'/></label>
                     <label for="psdf" class="wzb-input-label"><input type="radio" class="kca_public_ind" name="kca_public_ind" value="0" ><lb:get key='global.global_publish_off'/></label>
				 </td>
            </tr>
        </table>
        
    	<div class="wzb-bar">
         	<input type="button" id="modalsure" class="btn wzb-btn-blue wzb-btn-big margin-right15" value='<lb:get key="global.button_ok"/>' name="frmSubmitBtn">
         	<input type="button" id="modalback" class="btn wzb-btn-blue wzb-btn-big" value="<lb:get key="global.button_cancel"/>" name="frmSubmitBtn">
   		</div>
    </div>
</script>

</body>
</html>