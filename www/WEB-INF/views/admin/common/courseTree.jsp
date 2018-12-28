<%@ page isELIgnored="false" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.cw.wizbank.util.LangLabel, com.cw.wizbank.qdb.*"%>
<%@ include file="../../common/taglibs.jsp"%>
<script type="text/javascript" src="${ctx}/js/tree/js/jquery.ztree.core.js"></script>
<script type="text/javascript" src="${ctx}/js/tree/js/jquery.ztree.excheck.js"></script>
<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/global_${lang}.js"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_lm_${lang}.js"></script>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-default.css" />
<link rel="stylesheet" href="${ctx}/static/css/base.css" />
<link rel="stylesheet" href="${ctx}/static/admin/css/admin.css" />
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_tm_${lang}.js"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_um_${lang}.js"></script>
<c:if test="${lang eq 'en-us'}">
	<!-- 兼容英文的css -->
	<link rel="stylesheet" href="${ctx}/static/css/base-en.css"/>
	<link rel="stylesheet" href="${ctx}/static/admin/css/admin-en.css"/>
</c:if>
<script id="course_selector_content" type="text/x-jquery-tmpl">
<div class="wzb-area" style="padding:2px 15px 0;">
	<div class="wzb-area-left" style="overflow-y:auto;height:440px;">
		   <div class="wzb-area-left" style="overflow-y:auto;height:440px;">
                <ul id="courseCatalogTree" class="ztree"></ul>
			</div>
     </div>
	 <div class="wzb-area-content" style="overflow-y:auto;height:440px;margin-left:240px;">
                  <form class="form-search" >
                  	<input type="text" class="form-control" placeholder="<lb:get key='label_tm.label_core_training_management_470'/>" name="search_name"  id="search_name">
					<input type="button" class="form-submit" value="" onClick="getCourseByCalId('{{:type}}');">
					<input type="hidden" id="selCatalogId" value="" >
                  </form>
                  <div style="margin-left:10px; float:left; width:403px;">
					<input type="hidden" id="courseIds" value="" item="">
                  	 <span id="course_selector_item_list" class="wzb-table-input"></span>
                  </div>
    </div>
</div>
</script>

<script id="course_selector_input_template" type="text/x-jsrender">
  <input type="checkbox" {{: ck}} name="course_checkbox" onclick="onChangeCourseBox(this)" value="{{>text}}" item="{{>title}}" />
</script>

<script type="text/javascript">
var dt, tcrId, itmParams;

function getCourseByCalId(type){
	itmParams = {itm_type : type};
	var p = {
            searchText : $("input[name='search_name']").val()
        }
    itmParams = $.extend(itmParams, p);
    $(dt).reloadTable({
        params : itmParams
    });
}

function onChangeCourseBox(obj){
	if($('input[name="course_checkbox"]:checked').length==5){
		$("#course_all").prop("checked", true);
	}else{
		$("#course_all").prop("checked", false);
	}
	  var isChecked = $(obj).is(":checked");
	  var ids=$('#courseIds').val();
	  var arr=ids.split("~");
	  var its=$('#courseIds').attr("item");
	  var ars=its.split(",");
	  var id=$(obj).attr("value");
	  var item=$(obj).attr("item");
	  if(isChecked){
		  if(ids==""){
			   $("#courseIds").val(id);
				$("#courseIds").attr("item",item);
		  }else{
		  		arr.push(id);
		  		var ar=arr.join("~");
		   		$("#courseIds").val(ar);
		  		ars.push(item);
		  		var art=ars.join(",");
		   		$("#courseIds").attr("item",art);
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
		   $("#courseIds").val(ar);
		   $("#courseIds").attr("item",art);
		  
	  }
}

function getCheckedCourseIds() {
	var isChecked = $('#course_all').is(":checked");
	var ids=$('#courseIds').val();
	var arr=ids.split("~");
	var its=$('#courseIds').attr("item");
	var ars=its.split(",");
	if (!isChecked) {
		$('input[name="course_checkbox"]').each(function() {
			 for (var i = 0; i < arr.length; i++) {
					if(arr[i]==$(this).val()){
						arr.splice(i,1);
						ars.splice(i,1);
					}
				}
			var ar=arr.join("~");
			$("#courseIds").val(ar);
			var art=ars.join(",");
			$("#courseIds").attr("item",art);
			
			$(this).prop("checked", false);
		});
	} else {
		$('input[name="course_checkbox"]').each(function() {
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
				   $("#courseIds").val(ar);
				   ars.push($(this).attr("item"));
				   var art=ars.join(",");
				   art=art.substring(1,art.length);
				   $("#courseIds").attr("item",art);
			}else{
		  	 	arr.push($(this).val());
			  	var ar=arr.join("~");
			   	$("#courseIds").val(ar);
				ars.push($(this).attr("item"));
				var art=ars.join(",");
				$("#courseIds").attr("item",art);
			} 
			$(this).prop("checked", true);
		});
	}
}

var courseCatalog_setting = {
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
  			onClick:function(event,treeId,treeNode){
  				/* $("#selCatalogId").val(treeNode.id);
  				$("#course_selector_item_list").reloadTable({
  			        params:{
  			        	selCatalogId : treeNode.id
  			        }
  				}); */
  				var p = {
  						tndId : treeNode.id,
  						searchText : $("input[name='search_name']") != undefined ? $("input[name='search_name']").val() : ""
  					}
			        itmParams = $.extend(itmParams, p);
			        $(dt).reloadTable({
			           params : itmParams
			        });
			}
  	}
};
var courseTableModel = [
	{
		display : '<input id="course_all" onclick="getCheckedCourseIds()" type="checkbox" value="" name="course_all_checkbox" />',//"",
		align : 'left',
		width : '1%',
		sortable : false,
		format : function(data) {
			
			var courseIds=$("#courseIds").val();
			var cked="";	
			var arr=courseIds.split("~");
         	for (var i = 0; i < arr.length; i++) {
				if(arr[i]==data.usr_ent_id){
					cked="checked='checked'";	
				}
         	}
         	
			p = {
				text : data.itm_id,
				title : data.itm_title,
				ck:cked
			}
         	$("#course_all").prop("checked", false);
			return $('#course_selector_input_template').render(p);
		}
	},   
	{
   		display : fetchLabel('global_name'),
   		align : 'left',
   		width : '39%',
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
   		display : fetchLabel('global_code'),
   		align : 'left',
   		width : '30%',
   		sortable : false,
   		name : 'itm_code',
   		format : function(data) {
   			var code;
   			if(data.itm_code.length>15){
   				code=data.itm_code.substring(0,15)+"...";
   			}else{
   				code=data.itm_code;
   			}
   			p = {
   				text : "<p title='"+data.itm_code+"'>"+code+"</p>"
   			}
   			return p.text;
   		}
   	},
   	{
		display : cwn.getLabel('global_kind'),//"类型",
		align : 'left',
		width : '30%',
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
    

var courseSelector = function() {
	   var obj = new Object();
	   
	   obj.userTable = null;
	   
	   obj.selectedCallBack = null;
	   
	   obj.selectedIds = null;
	   
	   obj.layerIndex = null;
	   
	   obj.courseType='course';
	   obj.examType = 'exam';
	   obj.allType='all';
	   
	   obj.type = obj.allType;
	   
	   obj.show = function(callback,type) {
		   if(null!=type && typeof(type) != "undefined"){
			   obj.type = type;
		   }else{
			   obj.type = obj.allType;
		   }
		   var html = $('#course_selector_content').render({type:obj.type});
		   obj.layerIndex = layer.open({
			   	  type: 1,//弹出类型 
				  btn: [fetchLabel('button_ok'), fetchLabel('button_cancel')],//按钮 
	              area: ['700', '550px'], //宽高
	              title : fetchLabel('label_core_user_management_55'),//标题 
				  content: html,
				  yes: function(index, layero){
					  var ids = $('#courseIds').val();
					  var items = $('#courseIds').attr('item');
					  ids = ids.split("~");
					  items = items.split(",");
					  var result = new Array();
					  for(var i = 0 ; i < ids.length ; i++){
						  var usr = new Object();
						  usr.id = parseInt(ids[i]);
						  usr.name = items[i];
						  result[i] = usr;
					  }
					  obj.selectedIds = result;
					  obj.selectedCallBack(obj.selectedIds);
					  obj.close();
				  }
			});
		   obj.initCourseCatalog();
		   obj.initCourseList();
		   if(typeof(callback)=="function"){
			   obj.selectedCallBack = callback;
		   }
	   };
	   
	   obj.initCourseCatalog = function(){
		   var catalog_tree_url = "${ctx}/app/admin/tree/getCourseTreeJson?rad="+Math.random();
			$.getJSON(catalog_tree_url, function(result) {
				if(true){
					result.push({id:0,pid:null,name:fetchLabel("label_core_training_management_10"),isParent:true,open:1});
				}
		        $.fn.zTree.init($("#courseCatalogTree"), courseCatalog_setting, result);
			});
	   };
	   
	   obj.initCourseList = function(){
		   $("#selCatalogId").val(0);
		   itmParams = {itm_type : obj.type};
		   obj.userTable = $("#course_selector_item_list").table({
				 	url : '${ctx}/app/admin/course/pageCourseJson',
						colModel : courseTableModel,
				        rp : 5,
				        hideHeader : false,
						sortname : 'itm_publish_timestamp',
						sortorder : 'desc',
				        usepager : true,
				        params:{
				        	itm_type : obj.type
				        }
				    });
		   dt = obj.userTable;
	   };
	   
	   obj.close = function closeLayer(){
			layer.close(obj.layerIndex);
		};
	   
	   return obj;
}

</script>