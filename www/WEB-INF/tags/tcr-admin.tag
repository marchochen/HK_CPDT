<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="../views/common/taglibs.jsp"%>
<%@ attribute name="id" required="false"%>
<%@ attribute name="text" required="false"%>
<%@ attribute name="title" required="false"%>
<%@ attribute name="style" required="false"%>
<%@ attribute name="event" required="false"%>
<c:if test="${id == null or id == '' }">
    <c:set var="id">
        tcr
    </c:set>
</c:if>
<style type="text/css">
.cwn-dropdown {position: relative;}
.cwn-dropdown-menu {display: none; z-index:1000; position: absolute; background: white; border: 1px solid #7f9db9; min-width:196px;}
.open .cwn-dropdown-menu { display: block;}
.cwn-dropdown button .caret {margin-left:70px;}
.butt{ width:210px;height:28px;}
.select-img{width:30px;margin-top:-34px;margin-left:171px;height:30px;}
.tcr-text{width:180px;text-align:left;height:30px;line-height:20px;overflow: hidden;}
</style>

<span class="cwn-dropdown">
    <button id="${id }" class="btn btn-default butt" type="button" data-toggle="cwn-dropdown">
        <div id="tcr_text" class="tcr-text"><lb:get key="label_tm.label_core_training_management_54" /></div>
        <div class="select-img"><img  src="/static/admin/images/wzb-select.png"></div>
    </button> 

    <div id="${id }_tcrContent" tabindex="-1" class="cwn-dropdown-menu" >
        <ul id="${id }_tcrTree" class="ztree" style="margin-top: 0;"></ul>
    </div>
</span>
<script type="text/javascript">
	if(navigator.appName == "Microsoft Internet Explorer" && navigator.appVersion .split(";")[1].replace(/[ ]/g,"")=="MSIE9.0") 
	{ 
		$(".select-img").css("margin-top",-36);
	} 
    var setting = {
        view : {
            selectedMulti : false,
            dblClickExpand : false
        },
        data : {
            simpleData : {
                enable : true
            }
        },
        async : {
            enable : true,
            url : "${ctx}/app/tree/tcListJson/noHead",
            autoParam : [ "id" ]
        },
        callback : {
            onClick : ${event}
        },
        onClick : {

        }
    };
    $(document).ready(function() {
        $.getJSON("${ctx}/app/tree/tcListJson/withHead", function(result) {
            $.fn.zTree.init($("#${id }_tcrTree"), setting, result);
        });
    });
</script>