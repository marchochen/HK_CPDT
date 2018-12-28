<%@ page isELIgnored="false" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.cw.wizbank.util.LangLabel, com.cw.wizbank.qdb.*"%>
<%@ include file="../../common/taglibs.jsp"%>

<script type="text/javascript" src="../../../js/tree/js/jquery.ztree.core.js"></script>
<script type="text/javascript" src="../../../js/tree/js/jquery.ztree.excheck.js"></script>
<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/global_${lang}.js"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_um_${lang}.js"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_um_${lang}.js"></script>
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
<script id="catalog_selector_content" type="text/x-jquery-tmpl">
<div class="wzb-area" style="padding:2px 15px 0;">
	<div class="wzb-area-left" style="overflow-y:auto;height:440px;width:380px;">
        <ul id="catalogTree" class="ztree"></ul>
	</div>
</div>
</script>

<script type="text/javascript">

var catalog_selector_setting = {
      	async: {
      		    enable: true,//启用异步加载
      		    url: "${ctx}/app/admin/tree/getCourseTreeJson?rad="+Math.random(), //异步请求地址
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
 			 	   var zTree = $.fn.zTree.getZTreeObj("catalogTree");
 			 	   treeNode.halfCheck = false;
  			}
  		},
  		check: {
            enable: true,
            chkStyle: "checkbox",
            nocheckInherit: false ,
            chkboxType : { "Y" : "s", "N" : "s" }
        }
};

var catalogSelector = function() {
	   var obj = new Object();
	   
	   obj.selectedCallBack = null;
	   
	   obj.selectedIds = null;
	   
	   obj.layerIndex = null;
	   
	   obj.show = function(callback) {
		   var html = $('#catalog_selector_content').render({});
		   obj.layerIndex = layer.open({
			   	  type: 1,//弹出类型 
				  btn: [fetchLabel('button_ok'), fetchLabel('button_cancel')],//按钮 
	              area: ['400px', '550px'], //宽高
	              title : fetchLabel('label_core_user_management_56'),//标题 
				  content: html,
				  yes: function(index, layero){
					    obj.selectedIds = new Array();
					    obj.selectedIds = $.fn.zTree.getZTreeObj("catalogTree").getCheckedNodes(true);
					 	obj.selectedCallBack(obj.selectedIds);
					 	obj.close();
				  }
			});
		   obj.initUsrCatalog();
		   if(typeof(callback)=="function"){
			   obj.selectedCallBack = callback;
		   }
	   };
	   
	   obj.initUsrCatalog = function(){
		   var url =  "${ctx}/app/admin/tree/getCourseTreeJson?firstLevel=true&rad="+Math.random();
			$.getJSON(url, function(result) {
		        $.fn.zTree.init($("#catalogTree"), catalog_selector_setting, result);
			});
	   };
	   
	   obj.close = function closeLayer(){
			layer.close(obj.layerIndex);
		};
	   
	   return obj;
}

</script>