<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
 <script type="text/javascript" src="${ctx}/js/jquery.js"></script>
 <script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
 <link rel="stylesheet" href="${ctx}/static/js/bootstrap/css/bootstrap.css">
<link rel="stylesheet" href="${ctx}/js/tree/css/ztree.css" />
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-default.css" />
<link rel="stylesheet" href="${ctx}/static/css/base.css" />
<link rel="stylesheet" href="${ctx}/static/admin/css/admin.css" />
<link rel="stylesheet" href="${ctx}/static/admin/css/font-awesome/css/font-awesome.min.css" />
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/global_${lang}.js"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_tm_${lang}.js"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_lm_${lang}.js"></script>
<script type="text/javascript" src="${ctx}/js/tree/js/jquery.ztree.core.js"></script>
<script type="text/javascript" src="${ctx}/js/wb_item.js"></script>
<script type="text/javascript" src="${ctx}/js/wb_utils.js"></script>
<script type="text/javascript" src="${ctx}/js/wb_application.js"></script>
<script type="text/javascript" src="${ctx}/static/js/cwn_utils.js"></script>
<script type="text/javascript" src="${ctx}/static/js/layer/layer.js"></script>
<script type="text/javascript" src="${ctx}/static/js/jsrender.js"></script>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-admin.css" />
  <script type="text/javascript">
  var dt, tcrId, itmParams;
  var type='course';
  var itm_lst = new wbItem;
  var app = new wbApplication;
  itmParams = {itm_type : type};
  $(function(){
	  
	  var sid=parent.$('#sid').val();
	  var entIds = [];
	  //遍历课程标题
	  parent.$("#s"+sid+" .wzb-choose-detail").each(function(){
	       	 entIds.push(parent.$(this).text().trim());
	       	})
			var cids=parent.$("#q"+sid).val();
	  if(cids!=""){
			$('#cid').attr("item",entIds);
			$('#cid').val(cids);
	  }
	  dt = $("#itemList").table({
		  url : '${ctx}/app/admin/course/pageCourseJson',
				colModel : dtModel,
		        rp : 5,
		        hideHeader : false,
				sortname : 'itm_publish_timestamp',
				sortorder : 'desc',
		        usepager : true,
		        params:itmParams
		    });
  
	  document.onkeydown = function(e){ 
		    var ev = document.all ? window.event : e;
		    if(ev.keyCode==13) {

		    	  var p = {
		                  searchText : $("input[name='searchText']").val()
		              }
		          itmParams = $.extend(itmParams, p);
		          $(dt).reloadTable({
		              params : itmParams
		          });
		     }
		}
  $("#searchBtn").click(function(){
      var p = {
              searchText : $("input[name='searchText']").val()
          }
      itmParams = $.extend(itmParams, p);
      $(dt).reloadTable({
          params : itmParams
      });
  })

  });
    var catalog_setting = {
		view : {
			selectedMulti : false,
			showIcon : false
		},
		data : {
			simpleData : {
				enable : true
			}
		},
		callback : {
			onClick : function(e, treeId, treeNode){treeCatalogClick(e, treeId, treeNode)}
		}
	};

    function initCatalogTree(){
        var catalog_tree_url = "${ctx}/app/catalog/adminCourseTreeJson?rad="+Math.random();
        if(typeof tcrId != 'undefined' && tcrId) {
            catalog_tree_url += "?tcrId=" + tcrId;
        }
        if('course' != '') {
            if(catalog_tree_url.indexOf('?') > -1){
                catalog_tree_url += "&cosType=" + 'course';
            } else {
                catalog_tree_url += "?cosType=" + 'course';
            }
        }
		$.getJSON(catalog_tree_url, function(result) {
			if(true){
				result.push({id:0,pid:null,name:fetchLabel("label_core_training_management_10"),isParent:true,open:1});
			}
			$.fn.zTree.init($("#zTree-catalog"), catalog_setting, result);
		});
    }
    $(function(){
        initCatalogTree();
    })
    var dtModel = [
           {
           		display : '<input id="cms_all" onclick="getCheckedIds()" type="checkbox" value="" name="cms_all_checkbox" />',//"",
           		align : 'left',
           		width : '1%',
           		sortable : false,
           		format : function(data) {
           		
           			var cid=$("#cid").val();
           			var cked="";
           			var arr=cid.split("~");
           		   for (var i = 0; i < arr.length; i++) {
           			if(arr[i]==data.itm_id){
           				
           			cked="checked='checked'";	
           			}
           		}
           			p = {
           				text : data.itm_id,
           				title : data.itm_title,
           				ck:cked
           			}
           			$("#cms_all").prop("checked", false);
           			return $('#input-template').render(p);
           		}
           	},   {
           		display : cwn.getLabel('global_name'),//"名称",
           		align : 'left',
           		width : '33%',
           		sortable : false,
           		name : 'itm_title',
           		format : function(data) {
           			var title;
           			if(data.itm_title.length>15){
           				title=data.itm_title.substring(0,15)+"...";
           			}else{
           				title=data.itm_title;
           			}
           			p = {
           				text : "<p title='"+data.itm_title+"'>"+title+"</p>"
           			}
           			return p.text;
           		}
           	}, 
{
    display : cwn.getLabel('global_code'),//"编号",
    align : 'left',
    width : '33%',
    sortable : false,
    name : 'itm_code',
		format : function(data) {
			var code;
   			if(getChars(data.itm_code)>8){
   				code=data.itm_code.substring(0,8)+"...";
   			}else{
   				code=data.itm_code;
   			}
   			p = {
   				text : "<p title='"+data.itm_code+"'>"+code+"</p>"
   			}
			return p.text;
		}
	},{
		display : cwn.getLabel('global_kind'),//"类型",
		align : 'left',
		width : '33%',
		sortable : false,
		name : 'itm_type',
		format : function(data) {
			data.itm_type_str = cwn.getItemType(data.itm_type, data.itm_exam_ind);
			p = {
				text : data.itm_type_str
			}
			return p.text;
		}
	} 

];
</script>

</head>
<body>
<div class="panel wzb-panel" style="margin-top: 0;">
<div class="panel-body">
   <div class="wzb-area clearfix">
     <div class="wzb-area-left" style="overflow-y: auto;height: 490px;">
		<dt><!--目录分类--><lb:get key='label_tm.label_core_training_management_7'/>：</dt>
   		<ul id="zTree-catalog" class="ztree"></ul>
	</div> 
 	<div class="wzb-area-content" style="overflow-y: auto;height: 490px;">
          <form class="form-search" onsubmit="return false;">
                        <input type="text" class="form-control" name="searchText" placeholder="<lb:get key='label_tm.label_core_training_management_32'/>">
                        <input type="button" class="form-submit" value=""  id="searchBtn">
          </form>
                       <input type="hidden" id="cid" value="" item="">
		<div role="tabpanel" class="wzb-tab-2" >
                        <ul class="nav nav-tabs" role="tablist">


                            <span><lb:get key="course_list"/> </span>
                            </ul>
              
 <span id="itemList">
 </span>
     
<div class="norm-border  thickbox-footer" style="padding-left: 30%;bottom: 50px;padding-top: 20px;border-top: 1px solid #eee;">
   <input value="<lb:get key='global.button_ok'/>" name="" class="btn wzb-btn-blue wzb-btn-big  margin-right10" onclick="addBxk()" type="button">
    <input value="<lb:get key='global.button_cancel'/>" name="" class="TB_closeWindowButton wzb-btn-big  btn wzb-btn-blue " onclick="closeLayer()" type="button">
</div>
            
 </div>
</div>
</div>
</div>
</div>
<script id="text-template" type="text/x-jsrender">
		{{>text}}
	</script>
	<script type="text/javascript">
	function treeCatalogClick(e, treeId, treeNode) {
		var p = {
			tndId : treeNode.id,
             searchText : $("input[name='searchText']") != undefined ? $("input[name='searchText']").val() : ""
		}
	    itmParams = $.extend(itmParams, p);
	    $(dt).reloadTable({
	        params : itmParams
	    });
	}
	</script>
	<script id="courseGridTemplate" type="text/x-jsrender">

 <dl class="wzb-list-6">
      <dd>
           <div class="main_img">
                  <img class="fwpic" src="{{:itm_icon}}">
                  <div class="show">
                       <span class="imgArea">
                             <a title="" href="javascript:itm_lst.get_item_detail({{:itm_id}})">
							 <lb:get key="label_tm.label_core_training_management_80"/></a>
                       </span>
                  </div>
            </div>
      </dd>
      <dt>
           <p>{{:itm_title}}</p>
           <div class="offheight clearfix">
				<div class="offwidth">
					<span class="color-gray999">
						<lb:get key="label_tm.label_core_training_management_34"/> 
					</span>{{:itm_type_str}}
				</div> 
				<div class="offwidth">
					<span class="color-gray999">
						<lb:get key="label_tm.label_core_training_management_35"/>
					</span>
					{{:itm_code}}
				</div>
			</div>
           <div class="offheight clearfix"><div class="offwidth"><span class="color-gray999">
				<lb:get key="label_tm.label_core_training_management_27"/></span>{{:itm_publish_timestamp}}</div></div>
		   <div class="margin-top15">
				{{include tmpl="#courseBtnTemplate" /}}
		   </div>
      </dt>
 </dl>
</script>
<script id="courseBtnTemplate" type="text/x-jsrender">
<span style="white-spacing:nowrap;">
<input type="button" name="frmSubmitBtn" value="<lb:get key='label_tm.label_core_training_management_29'/>" class="btn wzb-btn-blue" onclick="javascript:course_lst.edit_cos({{:cos_res_id}},'{{:itm_type}}','false','','false')"> 
{{if itm_type != 'CLASSROOM'}}
	<input type="button" name="frmSubmitBtn" value="<lb:get key='label_tm.label_core_training_management_30'/>" class="btn wzb-btn-blue" onclick="javascript:attn.get_grad_record({{:itm_id}});"> 
{{/if}}
{{if itm_type != 'AUDIOVIDEO'}}
<input type="button" name="frmSubmitBtn" value="<lb:get key='label_tm.label_core_training_management_31'/>" class="btn wzb-btn-blue" onclick="app.get_application_list('',{{:itm_id}},'','','','');">
{{/if}}
</span>
</script>
<script id="input-template" type="text/x-jsrender">
  <input type="checkbox" {{: ck}} name="cms_checkbox" onclick="onChangebox(this)" value="{{>text}}" item="{{>title}}" />
</script>
<script type="text/javascript">
function onChangebox(obj){
	
	if($('input[name="cms_checkbox"]:checked').length==5){
		$("#cms_all").prop("checked", true);
	}else{
		$("#cms_all").prop("checked", false);
	}
			
	  var isChecked = $(obj).is(":checked");
	  var ids=$('#cid').val();
		  var arr=ids.split("~");
	  var its=$('#cid').attr("item");
		  var ars=its.split(",");
		  var id=$(obj).attr("value");
		  var item=$(obj).attr("item");
	  if(isChecked){
		  if(ids==""){
			   $("#cid").val(id);
				   $("#cid").attr("item",item);
		  }else{
		  arr.push(id);
		  var ar=arr.join("~");
		   $("#cid").val(ar);
		   
		  ars.push(item);
		  var art=ars.join(",");
		   $("#cid").attr("item",art);
		  }
	  }else{
		   for (var i = 0; i < arr.length; i++) {
			if(arr[i]==id){
				arr.splice(i,1);
				ars.splice(i,1);
			}
		}
		   var ar=arr.join("~");
		   var art=ars.join(",");
		   $("#cid").val(ar);
		   $("#cid").attr("item",art);
		  
	  }
}
function getCheckedIds() {
	var isChecked = $('#cms_all').is(":checked");
	  var ids=$('#cid').val();
	  var arr=ids.split("~");
	  var its=$('#cid').attr("item");
	  var ars=its.split(",");
	if (!isChecked) {
		$('input[name="cms_checkbox"]').each(function() {
			
			 for (var i = 0; i < arr.length; i++) {
					if(arr[i]==$(this).val()){
						arr.splice(i,1);
						ars.splice(i,1);
					}
				}
				   var ar=arr.join("~");
				   $("#cid").val(ar);
				   var art=ars.join(",");
				   $("#cid").attr("item",art);
			$(this).prop("checked", false);
		});
	} else {
		$('input[name="cms_checkbox"]').each(function() {
			for (var i = 0; i < arr.length; i++) {
				if(arr[i]==$(this).val()){
					arr.splice(i,1);
					ars.splice(i,1);
				}
			}
			 if(ids==""){
				   arr.push($(this).val());
					  var ar=arr.join("~");
					  ar=ar.substring(1,ar.length);
					   $("#cid").val(ar);
					   
						  ars.push($(this).attr("item"));
						  var art=ars.join(",");
					  art=art.substring(1,art.length);
						   $("#cid").attr("item",art);
				  }else{
					  
			   arr.push($(this).val());
				  var ar=arr.join("~");
				   $("#cid").val(ar);
				   
					  ars.push($(this).attr("item"));
					  var art=ars.join(",");
					   $("#cid").attr("item",art);
				  } 
			$(this).prop("checked", true);
		});
	}
}
function closeLayer(){
	var index = parent.layer.getFrameIndex(window.name); 
	  parent.layer.close(index);
  }
function addBxk() {
	var sid=parent.$('#sid').val();
var text="";
var ids=$("#cid").attr("value");
var items=$("#cid").attr("item");
if(ids==''){
	layer.alert(fetchLabel('label_core_learning_map_34'));
}else{
var arr=ids.split("~");
var ars=items.split(",");
	  for (var i = 0; i < arr.length; i++) {
		  text+='<div id="send_user_'+arr[i]+'" class="wzb-choose-info" value="'+arr[i]+'">'+
	    	'<span class="wzb-choose-detail">'+ars[i]+'</span>' +'<a class="wzb-choose-area" href="javascript:void(0)" onclick="removeDiv('+arr[i]+','+sid+')">  <i class="fa fa-remove"></i> </a></div>';
	  }
   
    	text+='<input type="hidden" id="q'+sid+'" name="qid" value="'+ids+'">';
    	var si="#s"+sid;
    	 parent.$(si).html(text);
    	 closeLayer();
    
}
}
</script>

</body>
</html>