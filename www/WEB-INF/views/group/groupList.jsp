<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<%@ include file="../common/template.jsp"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-learner.css" />
<script type="text/javascript"
	src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<script type="text/javascript" src="${ctx}/static/js/front/course.js"></script>
<title></title>
<script type="text/javascript">
	var isNormal = $
	{
		isNormal
	};
	$(function() {
		//TODO ?????
		/* if (isNormal != true) {
			$("div#menu,div#header").remove();
		} */
		$(".wbtable").table({
			url : '${ctx}/app/group/getSnsGroupList/my/${usrEntId}',
			colModel : colModel,
			rp : 10,
			showpager : 5,
			usepager : true
		})
	})

	colModel = [ {
		display : fetchLabel('group_name'),
		tdWidth : '28%',
		format : function(data) {
			var enc_s_grp_id = wbEncrytor().cwnEncrypt(data.s_grp_id);
			p = {
				className : 'wzb-link02',
				title : data.s_grp_title,
				href : '${ctx}/app/group/groupDetail/' + enc_s_grp_id+'?a_id=groupList'
			}
			return $('#a-template').render(p);
		}
	}, {
		display : fetchLabel('group_scale'),
		align : "center",
		tdWidth : '15%',
		format : function(data) {
			p = {
				text : data.member_total
			}
			return $('#text-center-template').render(p);
		}
	}, {
		display : cwn.getLabel('group_wait_app'),
		align : "center",
		tdWidth : '16%',
		format : function(data) {
			p = {
				text : data.member_wait
			}
			return $('#text-center-template').render(p);
		}
	}, {
		display : fetchLabel('group_manager'),
		align : "center",
		tdWidth : '24%',
		format : function(data) {
			p = {
				text : data.user.usr_display_bil
			}
			return $('#text-center-template').render(p);
		}
	}, {
		display : fetchLabel('group_create_time'),
		align : "center",
		tdWidth : '24%',
		format : function(data) {
			p = {
				text : data.s_grp_create_datetime.substring(0, 10)
			}
			return $('#text-center-template').render(p);
		}
	} ]

	function searchGroup() {
		var searchContent = $("input[name='searchContent']").val();
		if (searchContent == fetchLabel('search_type_qunzu')) {
			searchContent = '';
		}
		$(".wbtable").html('');
		$(".wbtable").table({
			url : '${ctx}/app/group/getSnsGroupList/my/${usrEntId}',
			params : {
				searchContent : searchContent
			},
			colModel : colModel,
			rp : 10,
			showpager : 5,
			usepager : true
		})
	}
	
</script>
</head>
<body>
	<div class="xyd-wrapper">
		<div id="main" class="xyd-main clearfix">
			<jsp:include page="groupMenu.jsp"></jsp:include>

			<div class="xyd-article">
                 <div class="wzb-title-2">
                      <lb:get key="group_list" />
                      <form class="form-search form-souso pull-right">     
                            <input name="searchContent" type="text" class="form-control" placeholder="<lb:get key='search_type_qunzu' />"/><input type="button" class="form-button" value="" onclick="searchGroup()"/>
                      </form>
                 </div>
				 
                 <div class="pfind">
					  <div class="wbtable"></div>
				 </div>
			</div>

		</div>
	</div>
	<!-- xyd-wrapper End-->
</body>
</html>