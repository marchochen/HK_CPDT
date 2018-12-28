<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<%@ include file="../common/template.jsp"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-learner.css" />
<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<script type="text/javascript" src="${ctx}/static/js/front/course.js"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_cm_${lang}.js"></script>
<title></title>
<script type="text/javascript">
	$(function() {
		$(".wbtable").table({
			url : '${ctx}/app/group/getSnsGroupList/find/${usrEntId}',
			colModel : colModel,
			rp : 10,
			showpager : 5,
			usepager : true
		})
	})
	
	colModel = [{
		display : fetchLabel('group_name'),
		tdWidth : '30%',
		format : function(data) {
			var enc_s_grp_id = wbEncrytor().cwnEncrypt(data.s_grp_id);
			p = {
				className : 'wzb-link02',
				title : data.s_grp_title,
				href : '${ctx}/app/group/groupDetail/' + enc_s_grp_id + '?a_id=groupFind'
			}
			return $('#a-template').render(p);
		}
	}, {
		display : fetchLabel('group_scale'),
		align : "center",
		tdWidth : '16%',
		format : function(data) {
			p = {
				text : data.member_total
			}
			return $('#text-center-template').render(p);
		}
	}, {
		display : fetchLabel('group_manager'),
		align : "center",
		tdWidth : '16%',
		format : function(data) {
			p = {
				text : data.user.usr_display_bil
			}
			return $('#text-center-template').render(p);
		}
	}, {
		display : fetchLabel('group_create_time'),
		align : "center",
		tdWidth : '17%',
		format : function(data) {
			p = {
				text : data.s_grp_create_datetime.substring(0,10)
			}
			return $('#text-center-template').render(p);
		}
	}, {
		display : fetchLabel('group_operate'),
		align : "center",
		tdWidth : '20%',
		format : function(data) {
			var applyInd = true;
			if(data.s_gpm == undefined || data.s_gpm.s_gpm_status == 3){
				applyInd = false;
			}
			p = {
				event : 'applyJoinGroup(this,' + data.s_grp_id + ')',
				applyInd : applyInd
			}
			return $('#button-template').render(p);
		}
	}]
	
	function searchGroup(){
		var searchContent = $("input[name='searchContent']").val();
		if(searchContent == fetchLabel('search_type_qunzu')){
			searchContent = '';
		}
		$(".wbtable").html('');
		$(".wbtable").table({
			url : '${ctx}/app/group/getSnsGroupList/find/${usrEntId}',
			params : {
				searchContent : searchContent
			},
			colModel : colModel,
			rp : 10,
			showpager : 5,
			usepager : true
		})
	}
	
	function applyJoinGroup(thisObj, s_grp_id){
		$.ajax({
			url : '${ctx}/app/group/applyJoinGroup/' + s_grp_id,
			type : 'POST',
			dataType : 'json',
			success : function(data){
				
				Dialog.alert(fetchLabel(data.message));
									
				$(thisObj).attr("style","disabled=true"); 
				$(thisObj).css("background-color","#999");
				$(thisObj).attr("disabled",true);
			}
		});
	}
    
</script>
<script id="button-template" type="text/x-jsrender">
<input type="button" class="btn wzb-btn-orange" onclick="{{>event}}" value='<lb:get key="group_join"/>' {{if applyInd == true}}style="background-color:#999;" disabled="disabled"{{/if}}/>
</script>
</head>
<body>
<div class="xyd-wrapper">
	<div id="main" class="xyd-main clearfix">
		<jsp:include page="groupMenu.jsp"></jsp:include>
		
		<div class="xyd-article">
                <div class="wzb-title-2">
                     <lb:get key="group_list"/>
                     <form class="form-search form-souso pull-right">     
                           <input type="text" name="searchContent" class="form-control" placeholder="<lb:get key='search_type_qunzu' />"/><input type="button" class="form-button" onclick="javascript:searchGroup()"/>
                     </form>
                </div>

				<div class="pfind">
				     <div class="wbtable"></div>
				</div>
		</div> <!-- xyd-article End-->
	
	</div>
</div> <!-- xyd-wrapper End-->
</body>
</html>