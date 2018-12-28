<%@ page isELIgnored="false" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.cw.wizbank.util.LangLabel, com.cw.wizbank.qdb.*"%>
<%@ include file="../../common/taglibs.jsp"%>
<script type="text/javascript" src="${ctx}/js/tree/js/jquery.ztree.core.js"></script>
<script type="text/javascript" src="${ctx}/js/tree/js/jquery.ztree.excheck.js"></script>
<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/global_${lang}.js"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_um_${lang}.js"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_um_${lang}.js"></script>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-default.css" />
<link rel="stylesheet" href="${ctx}/static/css/base.css" />
<link rel="stylesheet" href="${ctx}/static/admin/css/admin.css" />
<c:if test="${lang eq 'en-us'}">
	<!-- 兼容英文的css -->
	<link rel="stylesheet" href="${ctx}/static/css/base-en.css"/>
	<link rel="stylesheet" href="${ctx}/static/admin/css/admin-en.css"/>
</c:if>
<script id="user_selector_user_content" type="text/x-jquery-tmpl">
<div class="wzb-area" style="padding:2px 15px 0;">
	<div class="wzb-area-left" style="overflow-y:auto;height:440px;">
		   <div class="wzb-area-left" style="overflow-y:auto;height:440px;">
                <ul id="userGroupTree" class="ztree"></ul>
			</div>
     </div>
	 <div class="wzb-area-content" style="overflow-y:auto;height:440px;margin-left:240px;">
                  <form class="form-search" >
                  	<input type="text" class="form-control" placeholder="<lb:get key='label_um.label_core_user_management_10'/>" id="search_name">
					<input type="button" class="form-submit" value="" onClick="getUserByName();">
					<input type="hidden" id="selGroupId" value="" >
                  </form>
                  <div style="margin-left:10px; float:left; width:403px;">
					<input type="hidden" id="userIds" value="" item="">
                  	 <span id="user_selector_item_list" class="wzb-table-input" ></span>
                  </div>
    </div>
</div>
</script>

<script id="input-checkbox-template" type="text/x-jsrender">
  <input type="checkbox" {{: ck}} name="user_checkbox" onclick="onChangeUserBox(this)" value="{{>text}}" item="{{>title}}" title="{{>usrDisplayBil}}" />
</script>
<script id="input-radio-template" type="text/x-jsrender">
  <input type="radio" {{: ck}} name="user_checkbox" onclick="onChangeUserBox(this)" value="{{>text}}" item="{{>title}}" title="{{>usrDisplayBil}}"/>
</script>

<script type="text/javascript">
var dt, itmParams, golbalShowSubordinate;
function getUserByName(){
	     var p = {
	    		selGroupId : $("#selGroupId").val(),
		        searchText : $("#search_name").val(),
		        showSubordinate : golbalShowSubordinate
         }
	     itmParams = $.extend(itmParams, p);
		$("#user_selector_item_list").reloadTable({
	        params:itmParams
		}); 
}

function onChangeUserBox(obj){
	if($('input[name="user_checkbox"]:checked').length==5){
		$("#user_all").prop("checked", true);
	}else{
		$("#user_all").prop("checked", false);
	}
	  var isChecked = $(obj).is(":checked");
	  var ids=$('#userIds').val();
	  var arr=ids.split("~");
	  var its=$('#userIds').attr("item");
      var itsName=$('#userIds').attr("title");//用户全名
	  var ars=its.split(",");
	  var id=$(obj).attr("value");
	  var item=$(obj).attr("item");
      var itemName=$(obj).attr("title");//用户全名
	  if(isChecked){
		  if(ids==""){
			   $("#userIds").val(id);
				$("#userIds").attr("item",item);
                $("#userIds").attr("title",itemName);//用户全名
		  }else{
		  		arr.push(id);
		  		var ar=arr.join("~");
		   		$("#userIds").val(ar);
		  		ars.push(item);
		  		var art=ars.join(",");
		   		$("#userIds").attr("item",art);
                $("#userIds").attr("title",art);//用户全名
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
		   $("#userIds").val(ar);
		   $("#userIds").attr("item",art);
           $("#userIds").attr("title",art);//用户全名
		  
	  }
}

function getCheckedUserIds() {
	var isChecked = $('#user_all').is(":checked");
	var ids=$('#userIds').val();
	var arr=ids.split("~");
	var its=$('#userIds').attr("item");
    var itsName=$('#userIds').attr("title");//用户全名
	var ars=its.split(",");
	if (!isChecked) {
		$('input[name="user_checkbox"]').each(function() {
			 for (var i = 0; i < arr.length; i++) {
					if(arr[i]==$(this).val()){
						arr.splice(i,1);
						ars.splice(i,1);
					}
				}
			var ar=arr.join("~");
			$("#userIds").val(ar);
			var art=ars.join(",");
			$("#userIds").attr("item",art);
            $("#userIds").attr("title",art);//用户全名
			
			$(this).prop("checked", false);
		});
	} else {
		$('input[name="user_checkbox"]').each(function() {
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
				   $("#userIds").val(ar);
				   ars.push($(this).attr("item"));
				   var art=ars.join(",");
				   art=art.substring(1,art.length);
				   $("#userIds").attr("item",art);
                   $("#userIds").attr("title",art);//用户全名
			}else{
		  	 	arr.push($(this).val());
			  	var ar=arr.join("~");
			   	$("#userIds").val(ar);
				ars.push($(this).attr("item"));
				var art=ars.join(",");
				$("#userIds").attr("item",art);
                $("#userIds").attr("title",art);//用户全名
			} 
			$(this).prop("checked", true);
		});
	}
}

var usg_setting = {
      	async: {
      		    enable: true,//启用异步加载
      		    dataType: 'json',
      		    type:"post",
      		    url:"${ctx}/app/admin/tree/getUserGroupLevelTree", //异步请求地址
      		    otherParam: {"showSubordinate": golbalShowSubordinate},
      		    autoParam:["id=treeId"] //需要传递的参数,为你在ztree中定义的参数名称
      	},
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
  			onAsyncSuccess: function(event, treeId, treeNode, msg){
 			 	   var zTree = $.fn.zTree.getZTreeObj("userGroupTree");
 			 	  // if(typeof( treeNode.halfCheck) != "undefined"){
 			 	//	 treeNode.halfCheck = false;
 			 	  // }
  			},
  		    beforeClick: function(treeId, treeNode) {
	   		    var zTree = $.fn.zTree.getZTreeObj("userGroupTree");
	   		    if(treeNode.name!=cwn.getLabel('label_core_training_management_10')){
	   		    	 $('#id').val(treeNode.id);
	   		    	 $('#name').val(treeNode.name);
	   		      	return true;
	   		    }else{
	   		    	return false;
	   		    }
  			},
  			onClick:function(event,treeId,treeNode){
  				$("#selGroupId").val(treeNode.id);
  				
  			    if($("#search_name").val() == ''){
  				  var p = {
  		  			       searchText : ""
  		  		         }
  				  itmParams = $.extend(itmParams, p);
  				}
  				
  				 var p = {
  					 selGroupId : treeNode.id,
  					 showSubordinate : golbalShowSubordinate
  		         }
  			     itmParams = $.extend(itmParams, p);
  				$("#user_selector_item_list").reloadTable({
  			        params:itmParams
  				});
			}
  	}
};
    

var userSelector = function() {
	   var obj = new Object();
	   
	   obj.userTable = null;
	   
	   obj.selectedCallBack = null;
	   
	   obj.selectedIds = null;
	   
	   obj.layerIndex = null;
	   
	   obj.multiSelect = true; //是否多选
	   
	   obj.showSubordinate = false; //显示下属用户
	   
	   obj.userTableModel = [
           	{
          		display : '',
          		align : 'left',
          		width : '1%',
          		sortable : false,
          		format : function(data) {
          			var userIds=$("#userIds").val();
          			var cked="";	
          			var arr=userIds.split("~");
                   	for (var i = 0; i < arr.length; i++) {
          				if(arr[i]==data.usr_ent_id){
          					cked="checked='checked'";	
          				}
                   	}
          			p = {
          				text : data.usr_ent_id,
          				title : data.usr_ste_usr_id,
                        usrDisplayBil : data.usr_display_bil,//用户全名
          				ck:cked
          			}
                   	$("#user_all").prop("checked", false);
          			if(obj.multiSelect == true){
          				return $('#input-checkbox-template').render(p);
          			}else{
          				return $('#input-radio-template').render(p);
          			}
          		}
          	},   
          	{
             		display : fetchLabel('label_core_user_management_11'),
             		align : 'left',
             		width : '49%',
             		sortable : false,
             		name : 'usr_display_bil',
             		format : function(data) {
             			var title;
             			if(data.usr_ste_usr_id.length>15){
             				title=data.usr_ste_usr_id.substring(0,15)+"...";
             			}else{
             				title=data.usr_ste_usr_id;
             			}
             			p = {
             				text : "<p title='"+data.usr_ste_usr_id+"'>"+title+"</p>"
             			}
             			return p.text;
             		}
             	},
          	{
             		display : fetchLabel('label_core_user_management_12'),
             		align : 'left',
             		width : '50%',
             		sortable : false,
             		name : 'usg_display_bil',
             		format : function(data) {
             			var title;
             			if(data.usg_display_bil.length>15){
             				title=data.usg_display_bil.substring(0,15)+"...";
             			}else{
             				title=data.usg_display_bil;
             			}
             			p = {
             				text : "<p title='"+data.usg_display_bil+"'>"+title+"</p>"
             			}
             			return p.text;
             		}
             	}

          ];
	   
	   obj.show = function(callback) {
		   var html = $('#user_selector_user_content').render({});
		   obj.layerIndex = layer.open({
			   	  type: 1,//弹出类型 
				  btn: [fetchLabel('button_ok'), fetchLabel('button_cancel')],//按钮 
	              area: ['700px', '550px'], //宽高
	              title : fetchLabel('label_core_user_management_13'),//标题 
				  content: html,
				  yes: function(index, layero){
					  var ids = $('#userIds').val();
					  var items = $('#userIds').attr('item');
                      var itemsName = $('#userIds').attr('title');//用户全名
					  ids = ids.split("~");
					  items = items.split(",");
                      itemsName = itemsName.split(",");
					  var result = new Array();
					  for(var i = 0 ; i < ids.length ; i++){
						  var usr = new Object();
						  usr.id = parseInt(ids[i]);
						  usr.name = items[i];
                          usr.nameFull = itemsName[i];//用户全名
						  result[i] = usr;
					  }
					  obj.selectedIds = result;
					  obj.selectedCallBack(obj.selectedIds);
					  obj.close();
				  }
			});
		   golbalShowSubordinate = obj.showSubordinate;
		   usg_setting.async.otherParam.showSubordinate = golbalShowSubordinate;
		   obj.initUsrGroup();
		   obj.initUserList();
		   if(typeof(callback)=="function"){
			   obj.selectedCallBack = callback;
		   }
	   };
	   
	   obj.initUsrGroup = function(){
		   var usg_tree_url = "${ctx}/app/admin/tree/getUserGroupLevelTree";
			$.getJSON(usg_tree_url,{'showSubordinate':golbalShowSubordinate}, function(result) {
		        $.fn.zTree.init($("#userGroupTree"), usg_setting, result);
			});
	   };
	   
	   obj.initUserList = function(){
		   $("#selGroupId").val(0);
		   if(obj.multiSelect==true){
			   obj.userTableModel[0].display='<input id="user_all" onclick="getCheckedUserIds()" type="checkbox" value="" name="user_all_checkbox" />';
		   }else{
			   obj.userTableModel[0].display='';
		   }
		   obj.userTable = $("#user_selector_item_list").table({
				 	url : '${ctx}/app/admin/user/pageGroupUserJson',
						colModel : obj.userTableModel,
				        rp : 5,
				        hideHeader : false,
						sortname : 'usr_ent_id',
						sortorder : 'desc',
				        usepager : true,
				        params:{
				        	selGroupId : 0,
				        	showSubordinate : golbalShowSubordinate
				        }
				    });
	   };
	   
	   obj.close = function closeLayer(){
			layer.close(obj.layerIndex);
		};
	   
	   return obj;
}

</script>