<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${ctx}/js/tree/css/ztree.css" />
<script type="text/javascript" src="${ctx}/js/tree/js/jquery.ztree.core.js"></script>
<title></title>
<script type="text/javascript">
	var setting = {
		view : {
			selectedMulti : false
		},
		data : {
			simpleData : {
				enable : true
			}
		}
	};
	
	var catalog_tree = null;
	
	$(function(){
		$.getJSON("${ctx}/app/know/getknowCatalogTree", function(result) {
			catalog_tree = $.fn.zTree.init($("#zTree"), setting, result.aeTreeNodeVoList);
		});
	});
	
	function changeCatalog(){
		if(catalog_tree.getSelectedNodes() == ''){
			Dialog.alert(fetchLabel('know_select_catalog_error'));
		} else {
			node = catalog_tree.getSelectedNodes()[0];
			if(node.pId == null){
				kcaIdOne = node.id;
				kcaIdTwo = 0;
			} else {
				kcaIdOne = node.pId;
				kcaIdTwo = node.id;
			}
			$.ajax({
	 			url : '${ctx}/app/know/changeKnowCatalog/${que_id_lst}/' + kcaIdOne + '/' + kcaIdTwo,
	 			type : 'POST',
	 			success : function() {
	 				var parent = window.parent.opener;
	 				var pa = parent.parent.opener;
	 				if (pa != undefined) {
	 					var detail_prep_win = pa.detail_prep_win;
	 					detail_prep_win.opener.location.href = detail_prep_win.opener.location.href;
// 	 					detail_prep_win.cos();
	 					parent.close();
	 				} else {
	 					parent.location.href = parent.location.href;
	 				}
	 				window.close();
	 			}
	 		})
		}
	}
</script>
<body>
	<div class="cont" style="min-height: 400px;">
		<div class="modal-header">
<!-- 		     <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button> -->
	     	<h4 class="modal-title" id="myModalLabel"><div class="pfindtit" style="text-align: left;"><lb:get key="know_class_catalog"/></div></h4>
	   	</div>
	   	<div id="zTree" class="ztree" style="overflow-x:auto;padding-bottom:15px;min-height: 300px;"></div>
 		<div class="psdcont">
 			<input type="button" onclick="changeCatalog()" value='<lb:get key="button_ok"/>' name="pertxt" class="wbtj mr20 fontfamily skin-bg">
 		</div>
 	</div>
</body>
</html>