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
		course = $(".wbtable").table({
			url : '${ctx}/app/rank/learningSituationRank',
			colModel : colModel,
			rp : 10,
			showpager : 10,
			usepager : true,
			completed : function(data){
				$.each(data.rows,function(i, val){
					var user_id=val.user.usr_ste_usr_id;
					val.user.usr_ste_usr_id=user_id;
					$.extend(val,{index:((data.page-1)*10+i+1)})
				})
			}
		})
	})
	var colModel = [ {
		name : 'rank_num',
		display : fetchLabel('rank_ranking'),
		width : '15%',
		sortable : false,
		format : function(data) {
			if(data.index < 4){
				type = 'xyd-bg-orange';
			} else {
				type = 'xyd-bg-gray';
			}
			p = {
				rank : data.index,
				type : type
			};
			return $('#rank-template').render(p);
		}
	}, {
		display : fetchLabel('usr_name'),
		width : '20%',
		sortable : false,
		format : function(data) {
			p = {
				text : data.user.usr_ste_usr_id
			};
			return $('#text-template').render(p);
		}
	}, {
		display : fetchLabel('usr_full_name'),
		width : '20%',
		sortable : false,
		format : function(data) {
			p = {
				text : data.user.usr_display_bil == null? '--' : data.user.usr_display_bil
			};
			return $('#text-template').render(p);
		}
	}, {
		display : fetchLabel('label_core_community_management_121'),
		width : '30%',
		sortable : false,
		format : function(data) {
			p = {
				text : data.usg_name
			};
			return $('#text-template').render(p);
		}
	}, {
		display : fetchLabel('personal_total_learn_duration'),
		width : '15%',
		align : 'center',
		sortable : false,
		format : function(data) {
			p = {
				text : getFormatTime(data.ls_learn_duration)
			};
			return $('#text-center-template').render(p);
		}
	} ]
	
	$(document).ready(function() {
		$.ajax({
			url : '${ctx}/app/rank/myLearningSituation',
			type : 'POST',
			dataType : 'json',
			success : function(data){
				if(data.learnDetail == null){
					$("#my_learning").text('--');
					$("#my_rank").text('--');
					$("#update_time").text('--');
				} else {
					$("#my_learning").text(getFormatTime(data.learnDetail.ls_learn_duration));
					$("#my_rank").text(data.learnDetail.rownum);
					$("#update_time").text(data.learnDetail.ls_update_time.substring(0,16));
				}
			}
		});
	});
	
	function getFormatTime(s){
		var day = Math.floor(s/(60*60*24));
		var hours = Math.floor(s/(60*60)) -  day*24;
		var minutes = Math.floor(s/(60)) -hours*60 -day*24*60;
		var second = Math.floor(s) - minutes *60 -hours*60*60 -day*24*60*60;
		if(day>0){
			$("#my_learning").addClass("has-day");
			return day+fetchLabel('rank_day')+twoNum(hours)+":"+twoNum(minutes)+":"+twoNum(second);
		} else {
			return twoNum(hours)+":"+twoNum(minutes)+":"+twoNum(second);
		}
	}
	
	function twoNum(s){
		if(s<10)
			return "0"+s;
		return s;
	}
</script>

</head>
<body>
	<div class="xyd-wrapper">
		<div id="main" class="xyd-main clearfix">
			<div class="wzb-model-10">
                <div class="wzb-title-6"><lb:get key="global_learning_rank" /><lb:get key="global_rank_notes"/></div>
		     		
		     	<div class="xyd-jifen-box">
					 <dl class="xyd-jifen-list xyd-jifen-green">
						 <dt><lb:get key="personal_total_duration"/></dt>
						 <dd id="my_learning"></dd>
					 </dl>
					 <dl class="xyd-jifen-list xyd-jifen-red">
						 <dt><lb:get key="rank_ranking"/></dt>
						 <dd id="my_rank"></dd>
					 </dl>
					 <div class="xyd-jifen-date"><span class="color-gray999"><lb:get key="last_update_time"/> : </span><span id="update_time"></em></div>
				</div>	
		     		
				<div class="wbtable">                                                                                
		
				</div>
			</div> <!-- wzb-model-10 End-->
		</div>
	</div> <!-- xyd-wrapper End-->
</body>
</html>