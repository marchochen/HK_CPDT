<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="../views/common/taglibs.jsp"%>
<%@ attribute name="id" required="false"%>
<%@ attribute name="text" required="false"%>
<%@ attribute name="title" required="false"%>
<%@ attribute name="type" required="false"%>
<%@ attribute name="event" required="false"%>
<%@ attribute name="showRoot" required="false" %>
<c:if test="${id == null or id == '' }">
    <c:set var="id">
        catalog
    </c:set>
</c:if>

<c:if test="${showRoot == null or showRoot == '' }">
    <c:set var="showRoot" value="false"></c:set>
</c:if>

<div id="zTree-${id }" class="ztree" style="overflow-x:auto;padding-bottom:15px;min-height:190px; "></div>

<script type="text/javascript">
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
			onClick : ${event}
		}
	};

    function initCatalogTree(){
        var catalog_tree_url = "${ctx}/app/catalog/adminTreeJson";
        if(typeof tcrId != 'undefined' && tcrId) {
            catalog_tree_url += "?tcrId=" + tcrId;
            getTreeInfo(tcrId);
        }
        if('${type}' != '') {
            if(catalog_tree_url.indexOf('?') > -1){
                catalog_tree_url += "&cosType=" + '${type}';
            } else {
                catalog_tree_url += "?cosType=" + '${type}';
            }
        }
		$.getJSON(catalog_tree_url, function(result) {
			
			if(${showRoot}){
				result.push({id:0,pid:null,name:fetchLabel("label_core_training_management_82"),isParent:true,open:1});
			}
			$.fn.zTree.init($("#zTree-${id }"), catalog_setting, result);
		});
    }
    function getTreeInfo(tcrId) {
    	var tree_info_url = "${ctx}/app/catalog/getTreeInfo/" + tcrId;
    	$.getJSON(tree_info_url, function(data) {
    		$("#sel_tcr_id").val(data.tcrId);
    	});
    }
    $(function(){
        initCatalogTree();
    })
</script>