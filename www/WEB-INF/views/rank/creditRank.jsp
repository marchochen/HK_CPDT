<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<%@ include file="../common/template.jsp"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-learner.css" />
<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<script type="text/javascript" src="${ctx}/static/js/front/course.js"></script>
<title></title>
<script type="text/javascript">
	$(function() {
		course = $(".wbtable").table({
			url : '${ctx}/app/rank/credit_rank',
			colModel : colModel,
			rp : 10,
			showpager : 10,
			usepager : true,
			completed : function(data){
				$.each(data.rows,function(i, val){
					var usr_ste_usr_id=val.user.usr_ste_usr_id;
					val.user.usr_ste_usr_id=""+usr_ste_usr_id+"";
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
		display : fetchLabel('usr_group'),
		width : '30%',
		sortable : false,
		format : function(data) {
			p = {
				text : data.usg_name
			};
			return $('#text-template').render(p);
		}
	}, {
		display : fetchLabel('usr_credit_rank'),
		width : '15%',
		align : 'center',
		sortable : false,
		format : function(data) {
			p = {
				text : data.uct_total
			};
			return $('#text-center-template').render(p);
		}
	} ]
	
	$(document).ready(function() {
		$.ajax({
			url : '${ctx}/app/rank/my_credit',
			type : 'POST',
			dataType : 'json',
			success : function(data){
				if(data.my_credit_detail && data.my_credit_detail.rownum) {
					$("#my_credit").text(data.my_credit_detail.uct_total);
					$("#my_rank").text(data.my_credit_detail.rownum);
					$("#update_time").text(data.my_credit_detail.uct_update_timestamp.substring(0,16));
				} else {
					$("#my_credit").text('--');
					$("#my_rank").text('--');
					$("#update_time").text('--');
				}
			}
		});
	});
</script>

</head>
<body>
	<div class="xyd-wrapper">
		<div id="main" class="xyd-main clearfix">
			<div class="wzb-model-10">
                <div class="wzb-title-6"><lb:get key="global_credit_rank" /><lb:get key="global_rank_notes"/></div>
		     		
		     	<div class="xyd-jifen-box">
					 <dl class="xyd-jifen-list xyd-jifen-green">
					     <dt><lb:get key="usr_credit"/></dt>
						 <dd id="my_credit">--</dd>
					 </dl>
					 <dl class="xyd-jifen-list xyd-jifen-red">
						 <dt><lb:get key="rank_ranking"/></dt>
						 <dd id="my_rank">--</dd>
				     </dl>
					 <div class="xyd-jifen-date"><span class="color-gray999"><lb:get key="last_update_time"/> : </span><em id="update_time">--</em></div>
			    </div>
		     		
				<div class="wbtable">                                                                                
		
				</div>
			</div> <!-- wzb-model-10 End-->
		</div>
	</div> <!-- xyd-wrapper End-->
</body>
</html>