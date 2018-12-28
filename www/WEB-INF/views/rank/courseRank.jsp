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
			url : '${ctx}/app/rank/course_rank',
			colModel : colModel,
			rp : 10,
			showpager : 10,
			usepager : true,
			<c:choose>
			<c:when test="${empty sort}">
			   sortname : 'itm_publish_timestamp',  //设置默认排序列
			</c:when>
			<c:otherwise>
			   sortname : '"app.app_total"',
			</c:otherwise>
			</c:choose>			
			sortorder : 'desc',
			completed : function(data){
				$.each(data.rows,function(i, val){
					$.extend(val,{index:((data.page-1)*10+i+1)})
				})
			}
		})
	})
	var colModel = [ {
		name : 'rank_num',
		display : fetchLabel('rank_ranking'),
		width : '10%',
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
		name : 'itm_title',
		display : fetchLabel('course_title'),
		width : '40%',
		sortable : false,
		format : function(data) {
			p = {
				className : 'wzb-link02',
				title : data.itm_title,
				href : '${ctx}/app/course/detail/' + wbEncrytor().cwnEncrypt(data.itm_id)
			}
			return $('#a-template').render(p);
		}
	}, 
	/* {
		name : 's_cnt_like_count',
		display : fetchLabel('rank_praise'),
		width : '15%',
		align : 'center',
		sortable : true,
		format : function(data) {
			p = {
				text : data.s_cnt_like_count
			};
			return $('#text-center-template').render(p);
		}
	},  */
	{
		name : 'itm_publish_timestamp',
		display : fetchLabel('course_publish_date'),
		width : '20%',
		align : 'center',
		sortable : true,
		format : function(data) {
			p = {
				text : data.itm_publish_timestamp.substring(0,10)
			};
			return $('#text-center-template').render(p);
		}
	}, {
		name : '"app.app_total"',
		display : fetchLabel('course_number'),
		width : '15%',
		align : 'center',
		sortable : true,
		format : function(data) {
			p = {
				text : data.app.app_total
			};
			return $('#text-center-template').render(p);
		}
	} ]
</script>

</head>
<body>
	<div class="xyd-wrapper">
		<div id="main" class="xyd-main clearfix">
			<div class="wzb-model-10">
                 <div class="wzb-title-6"><lb:get key="global_course_top" /></div>
		     		
				 <div class="wbtable">                                                                                
		
				 </div>
			</div> <!-- wzb-model-10 End-->
		</div>
	</div> <!-- xyd-wrapper End-->
</body>
</html>