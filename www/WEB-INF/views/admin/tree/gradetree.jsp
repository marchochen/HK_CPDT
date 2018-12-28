<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
 <script type="text/javascript" src="${ctx}/js/jquery.js"></script>
 <script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<link rel="stylesheet" href="${ctx}/js/tree/css/ztree.css" />
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-admin.css" />
<link rel="stylesheet" href="${ctx}/static/admin/css/admin.css" />
<link rel="stylesheet" href="${ctx}/static/css/base.css" />
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/global_${lang}.js"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_tm_${lang}.js"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_lm_${lang}.js"></script>
	<script type="text/javascript" src="${ctx}/static/js/layer/layer.js"></script> 
<script type="text/javascript" src="${ctx}/js/tree/js/jquery.ztree.core.js"></script>
<script type="text/javascript" src="${ctx}/js/wb_item.js"></script>
<script type="text/javascript" src="${ctx}/js/wb_utils.js"></script>
<script type="text/javascript" src="${ctx}/js/wb_application.js"></script>
<script type="text/javascript" src="${ctx}/static/js/cwn_utils.js"></script>
<script type="text/javascript" src="${ctx}/static/js/jsrender.js"></script>

  <script type="text/javascript">
  var dt, tcrId, itmParams;
  var type='course';
  var cos_type='true';
  var itm_lst = new wbItem;
  var app = new wbApplication;
  var productIframe;
  itmParams = {itm_type : type};

	if(cos_type) {
		itmParams = $.extend(itmParams, {cos_type : cos_type})
	}
    var catalog_setting = {
    	async: {
    		    enable: true,//启用异步加载
    		    url:"${ctx}/app/admin/tree/gradeTreeJson", //异步请求地址
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
			onAsyncSuccess: onAsyncSuccess,
		    beforeClick: function(treeId, treeNode) {
		    var zTree = $.fn.zTree.getZTreeObj("ztree");
		    if(treeNode.name!=cwn.getLabel('label_core_training_management_10')){
		    	 $('#id').val(treeNode.id);
		    	 $('#name').val(treeNode.name);
		      return true;
		    }else{
		    	 return false;
		    }
		  /*    if (treeNode.isParent) {
		    	 alert(treeNode.id+"----"+treeNode.name);
		     // zTree.expandNode(treeNode);
		      return false;
		     } else {
		    	 $('#id').val(treeNode.id);
		    	 $('#name').val(treeNode.name);
		     // productIframe.attr("src",treeNode.url);
		      return true;
		     } */
		}}
	};
    function onAsyncSuccess(event, treeId, treeNode, msg) {
    	   cancelHalf(treeNode);
    	  }
    	  function cancelHalf(treeNode) {
    	   var zTree = $.fn.zTree.getZTreeObj("ztree");
    	   treeNode.halfCheck = false;
    	  // zTree.updateNode(treeNode);   //异步加载成功后刷新树节点
    	  }
    function initCatalogTree(){
        var catalog_tree_url = "${ctx}/app/admin/tree/gradeTreeJson";
		$.getJSON(catalog_tree_url, function(result) {
			
			if(true){
				result.push({id:5,pid:null,name:fetchLabel("label_core_training_management_10"),isParent:true,open:1});
			}
			$.fn.zTree.init($("#zTree-catalog"), catalog_setting, result);
		});
    }
    $(function(){
        initCatalogTree();
    })
</script>

</head>
<body>
<div class="panel wzb-panel" style="margin-top: 0;">
<div>
                       
<div style="overflow-y: auto;height: 360px;">
   		<ul id="zTree-catalog" class="ztree"></ul>
	</div> 
                 
                 <input type="hidden" id="id" name="id">
                 <input type="hidden" id="name" name="name">
<div class="wzb-bar" >
  <input style="border:0;" value="<lb:get key='global.button_ok'/>" name="" class="btn wzb-btn-blue wzb-btn-big  margin-right10" onclick="doSub()" type="button">
  <input style="border:0;" value="<lb:get key='global.button_cancel'/>" name="" class="TB_closeWindowButton wzb-btn-big  btn wzb-btn-blue " onclick="closeLayer()" type="button">
</div>
</div>
</div>
<script type="text/javascript">
function closeLayer(){
	var index = parent.layer.getFrameIndex(window.name); 
    	 parent.layer.close(index);
	   
   }
function doSub() {
	var id=$("#id").val();
	var name=$("#name").val();
	if(id==""&&name==""){
		layer.alert(cwn.getLabel('label_core_learning_map_101'));
	}else{
		
	var sid=parent.$('#sid').val();
	var si="#name"+sid;
	var gi="#n"+sid;
	parent.$(si).val(name);
    	 parent.$(gi).find('div').html('<input type="hidden" id="g'+sid+'" name="gid" value="'+id+'">');
    	 closeLayer();
	}
}
</script>


</body>
</html>