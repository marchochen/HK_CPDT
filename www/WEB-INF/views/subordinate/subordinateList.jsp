<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<%@ include file="../common/template.jsp"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-learner.css" />
<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<title></title>
<script type="text/javascript">
	$(function() {
		$(".active").click();
	})
	
	function changeTab(thisObj){
		$(".active").removeClass("active");
		$(thisObj).addClass("active");
		searchSubordinate();
	}
	
	function getDataTable(searchContent){
		$(".wzb-find").html('');
		$(".wzb-find").table({
			url : '${ctx}/app/subordinate/getSubordinateList/' + $(".active").attr("name"),
			params : {
				searchContent : searchContent
			},
			gridTemplate : function(data){
				
				var attention_status = fetchLabel('attention_now');
				var attention_class = 'fa-check';
				var attention_id = 0;
				if(data.snsAttention != undefined && data.snsAttention.s_att_source_uid != undefined && data.snsAttention.s_att_target_uid != undefined){
					attention_status = fetchLabel('attention_mutual');
					attention_class = 'fa-exchange';
					attention_id = data.snsAttention.s_att_source_uid;
				}
				
				p = {
					image : data.usr_photo,
					usr_ent_id : data.usr_ent_id,
					usr_display_bil : data.usr_display_bil,
					usg_display_bil : data.usg_display_bil,
					ugr_display_bil : data.ugr_display_bil,
					attention_status : attention_status,
					attention_class : attention_class,
					attention_id : attention_id,
					a : 'javascript:;',
					isMeInd : true,
					sns_enabled : ${sns_enabled},
					my_ent_id : ${prof.usr_ent_id}
				}
				if(data.snsAttention != undefined && data.snsAttention.s_att_target_uid != undefined){
					return $('#attention-template').render(p);
				} else {
					return $('#unattention-template').render(p);
				}
			},
			rowCallback : function(){
				photoDoing();
			},
			view : 'grid',
			rowSize : 3,
			rp : 12,
			showpager : 5,
			hideHeader : true,
			usepager : true
		});
	}
	
	function searchSubordinate(){
		var searchContent = $("input[name='searchContent']").val();
		if(searchContent == fetchLabel('attention_find_desc')){
			searchContent = '';
		}
		getDataTable(searchContent);
	}
	
	function addAttention(s_att_target_uid, attention_id){
		$.ajax({
			url : '${ctx}/app/personal/addAttention/' + s_att_target_uid,
			type : 'POST',
			success : function(){
				var attention_status = fetchLabel('attention_now');
				var attention_class = 'fa-check';
				if(attention_id > 0 || '${command}' == 'fans'){
					attention_status = fetchLabel('attention_mutual');
					attention_class = 'fa-exchange';
				}
				var html = '<span class="skin-color"><i class="fa f14 fbold mr5 ' + attention_class + '"></i> ' + attention_status
						+ '</span> | <a class="grayC666" href="javascript:;" title="' + attention_status + '" onclick="cancelAttention(' 
						+ s_att_target_uid + ',' + attention_id +')">' + fetchLabel('button_cancel') + '</a>';
				$("#attention_" + s_att_target_uid).html(html);
			}
		});
	}
	
	function cancelAttention(s_att_target_uid, attention_id){
		$.ajax({
			url : '${ctx}/app/personal/cancelAttention/' + s_att_target_uid,
			type : 'POST',
			success : function(){
				var html = '<a class="grayC333" href="javascript:;" title="' + fetchLabel('personal_attention') + '" onclick="addAttention(' + s_att_target_uid 
						+ ',' + attention_id + ')"><i class="fa f14 grayC999 mr5 fa-plus"></i> ' + fetchLabel('personal_attention') + '</a>';
				$("#attention_" + s_att_target_uid).html(html);
			}
		});
	}
	
</script>
</head>
<body>
<div class="xyd-wrapper">
	<div id="main" class="xyd-main clearfix">
		<jsp:include page="subordinateMenu.jsp"></jsp:include>
		
		<div class="xyd-article">            
			<div role="tabpanel" class="wzb-tab-3">
				<!-- Nav tabs -->
				<ul class="nav nav-tabs" role="tablist">
					<li role="presentation" class="active" name="all" onclick="changeTab(this)"><a href="#home" aria-controls="home" role="tab" data-toggle="tab" style="padding-left:0;"><lb:get key="subordinate_all"/></a></li>
					<li role="presentation" name="direct" onclick="changeTab(this)"><a href="#profile" aria-controls="profile" role="tab" data-toggle="tab"><lb:get key="subordinate_direct"/></a></li>
					<li role="presentation" name="group" onclick="changeTab(this)"><a href="#follower" aria-controls="follower" role="tab" data-toggle="tab"><lb:get key="subordinate_group"/></a></li>
				</ul>
        
                <div class="tab-content">                       
                     <form class="form-search form-souso margin-top30">     
                           <input type="text" class="form-control" name="searchContent" value='<lb:get key="attention_find_desc"/>' onfocus="if(value==fetchLabel('attention_find_desc')){value=''}" onblur="if (value ==''){value=fetchLabel('attention_find_desc')}"/><input type="button" class="form-button" value="" onclick="searchSubordinate()"/>
                     </form>
                 
                     <div class="wzb-title-2 margin-top30"><lb:get key="subordinate_filter_result"/></div>
        
                     <div class="wzb-find clearfix"></div>
                </div>
            </div> <!-- wbtab end --> 
		</div> <!-- xyd-article End-->
	</div>
</div> <!-- xyd-wrapper End-->
</body>
</html>