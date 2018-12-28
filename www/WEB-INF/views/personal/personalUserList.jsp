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
		searchUserList();
	})
		
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
				var html = '<span class="skin-color"><i class="fa f14 fbold mr3 ' + attention_class + '"></i>' + attention_status
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
				if($("#command").val() != 'attention' || ${isMeInd} == false){
					var html = '<a class="wzb-link04" href="javascript:;" title="' + fetchLabel('personal_attention') + '" onclick="addAttention(' + s_att_target_uid 
							+ ',' + attention_id + ')"><i class="fa fa-plus"></i>' + fetchLabel('personal_attention') + '</a>';
					$("#attention_" + s_att_target_uid).html(html);
				} else {
					searchUserList();
				}
			}
		});
	}
	
	function searchUserList(notEqualGroup){
		if(notEqualGroup == undefined ||　notEqualGroup　== ''){
			notEqualGroup = false;
		}
		$(".wzb-find").html('');
		var searchContent = $("input[name='searchContent']").val();
		if(searchContent == fetchLabel('attention_find_desc')){
			searchContent = '';
		}
		$(".wzb-find").table({
			url : '${ctx}/app/personal/getUserList/${command}/${usrEntId}',
			params : {
				searchContent : searchContent,
				notEqualGroup : notEqualGroup
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
					isMeInd : ${isMeInd},
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
			usepager : true,
			trLine : false
		})
	}
</script>
</head>
<body>
<input type="hidden" value="${command}" id="command">
<div class="xyd-wrapper">
	<div id="main" class="xyd-main clearfix">
		<jsp:include page="personalMenu.jsp"></jsp:include>
		
		<div class="xyd-article">
            <c:if test="${command == 'find'}">
                <div class="wzb-title-12">
                     <i class="fa font-size24 margin-right10 fa-user"></i><lb:get key="attention_find"/>
                     <form class="form-search form-souso " style="margin-left: 15px;">   <!-- pull-right 右浮动 -->  
                           <input type="text" name="searchContent" class="form-control" value='<lb:get key="attention_find_desc"/>' onfocus="if(value==fetchLabel('attention_find_desc')){value=''}" onblur="if (value ==''){value=fetchLabel('attention_find_desc')}"/><input type="button" value="" name="mybtn" class="form-button" onclick="searchUserList(true)"/>
                     </form>
                </div>
            </c:if>
            
            <c:if test="${command != 'find'}">
                <div class="wzb-title-12"> <!--wzb-title-2    <i class="fa font-size18 fa-user"></i>   -->
                    <lb:get key="personal_${command}"/><lb:get key="know_list"/>
                     <form class="form-search form-souso margin-left10">     
                           <input type="text" name="searchContent" class="form-control" value='<lb:get key="attention_find_desc"/>' onfocus="if(value==fetchLabel('attention_find_desc')){value=''}" onblur="if (value ==''){value=fetchLabel('attention_find_desc')}"/><input type="button" value="" name="mybtn" class="form-button" onclick="searchUserList()"/>
                     </form>
                </div>
            </c:if>

            <div>
                <c:if test="${command == 'find'}">
                    <div class="wzb-title-5"><lb:get key="personal_${command}"/></div>
                </c:if>
                
                <div class="wzb-find clearfix" style="margin-top: 28px;"></div>
            </div>
		</div> <!-- xyd-article End-->
	
	</div>
</div> <!-- xyd-wrapper End-->
</body>
</html>