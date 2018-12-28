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
	$(function (){
		getDataTable("");
	})
	
/* 	function changeTab(thisObj){
		$(".now").removeClass("now");
		$(thisObj).addClass("now");
		searchSubordinate();
	} */
	
	function getDataTable(searchContent){
		$(".wzb-percent").html('');
		$(".wzb-percent").table({
			url : '${ctx}/app/subordinate/getSubordinateList/all',
			params : {
				searchContent : searchContent
			},
			gridTemplate : function(data){
				var addInd = true;
				if($("#send_user_" + data.usr_ent_id).attr("class") != undefined){
					addInd = false;
				}
				p = {
					image : data.usr_photo,
					usr_ent_id : data.usr_ent_id,
					usr_display_bil : data.usr_display_bil,
					usg_display_bil : data.usg_display_bil,
					ugr_display_bil : data.ugr_display_bil,
					a : 'javascript:',
					addInd : addInd
				}
				return $('#add-user-template').render(p);
			},
			rowCallback : function(){
				photoDoing();
			},
			view : 'grid',
			rowSize : 3,
			rp : 6,
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
	
	function showSelectUser(){
		$("#userList").modal("show");
	}
	
	function addUser(usr_ent_id, usr_display_bil){
		var p = {
			usr_ent_id : usr_ent_id,
			usr_display_bil : usr_display_bil
		}
		$("#user_" + usr_ent_id).html($("#cancel-user-button").render(p));
		
		$(".wzb-choose-box").html($(".wzb-choose-box").html() + $("#detele-user-button").render(p));
	}
	
	function cancelUser(usr_ent_id, usr_display_bil){
		var p = {
			usr_ent_id : usr_ent_id,
			usr_display_bil : usr_display_bil
		}
		$("#user_" + usr_ent_id).html($("#add-user-button").render(p));
		
		$("#send_user_" + usr_ent_id).remove();
	}
	
	function clearAll(){
		$("a[name='cancelUser']").click();
		$(".wzb-choose-box").html('');
	}
	
	function addAll(){
		$("a[name='addUser']").click();
	}
	
	function sendWebMessage(){
		var user_list = $(".wzb-choose-box").find("div");
		if(user_list.length == 0){
			Dialog.alert(fetchLabel('subordinate_please_select_user'));
			return;
		} 
		if($("input[name='wmsg_subject']").val() == ''){
			Dialog.alert(fetchLabel('subordinate_input_title'));
			return;
		}
		if($("textarea[name='wmsg_content']").val() == fetchLabel('subordinate_message_content_desc') || $("textarea[name='wmsg_content']").val() == ''){
			Dialog.alert(fetchLabel('subordinate_input_content'));
			return;
		}
		var usr_ent_id_str = user_list.eq(0).attr("value");
		for(var i=1;i<user_list.length;i++){
			usr_ent_id_str += '~' + user_list.eq(i).attr("value");
		}
		$.ajax({
			url : '${ctx}/app/subordinate/sendWebMessage/' + usr_ent_id_str,
			data : {
				wmsg_subject : $("input[name='wmsg_subject']").val(),
				wmsg_content : $("textarea[name='wmsg_content']").val()
			},
			type : 'POST',
			success : function() {
				window.location.href = "${ctx}/app/subordinate/subordinateList";
			}
		});
	}
	
</script>
<script id="add-user-button" type="text/x-jsrender">
	<a name="addUser" class="btn wzb-btn-blue" href="javascript:;" onclick="addUser({{>usr_ent_id}}, '{{>usr_display_bil}}')">
		<i class="fa fa-plus"></i>
		<lb:get key="button_add"/>
	</a>

</script>
<script id="cancel-user-button" type="text/x-jsrender">

	<em class="wzb-user-plus" style="width: 140px;">
		<span title="<lb:get key="group_add_ok"/>" href="#" class="wzb-link04">
			<i class="fa fa-check"></i>
			<lb:get key="group_add_ok"/>
		</span>
		&nbsp;|&nbsp;
		<a name="cancelUser" class="color-gray666" href="javascript:;" onclick="cancelUser({{>usr_ent_id}},'{{>usr_display_bil}}')"><lb:get key="button_cancel"/></a>
	</em>

</script>

<script id="add-user-template" type="text/x-jsrender">
	<div class="wzb-percent-3 wzb-display-01"> 
         <dl class="wzb-list-7 clearfix">
               <dd>
                   <div class="wzb-user wzb-user68">
                       <a href="{{>a}}"><img class="wzb-pic" src="${ctx}{{>image}}" alt="<lb:get key="know_ta"/>"></a>
                       <p class="companyInfo" style="display: none;"><lb:get key="know_ta"/></p>
                       <div class="cornerTL" style="width: 60px; height: 60px;">&nbsp;</div>
                       <div class="cornerTR" style="width: 60px; height: 60px;">&nbsp;</div>
                       <div class="cornerBL" style="width: 60px; height: 60px;">&nbsp;</div>
                       <div class="cornerBR" style="width: 60px; height: 60px;">&nbsp;</div>
                   </div>
               </dd>
                              
               <dt>
                   <a class="wzb-link04" href="{{>a}}" title="">{{>usr_display_bil}}</a>
                   <p>{{>usg_display_bil}}</p>
                   <p>{{if ugr_display_bil != 'Unspecified'}}{{>ugr_display_bil}}{{/if}}</p>
              	</dt>
         </dl>
         
		<span id="user_{{>usr_ent_id}}">
		{{if addInd == true}}
			<a name="addUser" class="btn wzb-btn-blue" href="javascript:;" onclick="addUser({{>usr_ent_id}}, '{{>usr_display_bil}}')">
					<i class="fa fa-plus"></i>
					<lb:get key="button_add"/>
			</a>

		{{else}}
			
			<em class="wzb-user-plus" style="width: 140px;">
				<a title="<lb:get key="group_add_ok"/>" href="#" class="wzb-link04">
					<i class="fa fa-check"></i>
					<lb:get key="group_add_ok"/>
				</a>
				&nbsp;|&nbsp;
				<a name="cancelUser" class="color-gray666" href="javascript:;" onclick="cancelUser({{>usr_ent_id}},'{{>usr_display_bil}}')"><lb:get key="button_cancel"/></a>
			</em>


		{{/if}}
		</span>
         
     </div>	

</script>
</head>
<body>
<div class="xyd-wrapper">
	<div id="main" class="xyd-main clearfix">
		<jsp:include page="subordinateMenu.jsp"></jsp:include>
		
		<div class="xyd-article">
            <div class="wzb-choose clearfix">
            	<div class="wzb-title-2"><lb:get key="subordinate_select_user_name"/></div>
                <div class="wzb-choose-box"></div>

                <div class="wzb-choose-tool">
                    <a href="javascript:void(0)" class="wzb-choose-click skin-bg-orange thickbox" onclick="showSelectUser()"><lb:get key="subordinate_select_user"/></a>
					<a class="skin-color" href="javascript:;" onclick="clearAll()"><lb:get key="subordinate_clear_all"/></a>
                </div>
            </div>     

            <div class="wzb-wenda-area">
                 <div class="wzb-wenda-title"><lb:get key="subordinate_send_remind_message"/></div>
                 
                 <form method="post" action="#">
                 <table>
                    <tr>
                         <td class="wzb-form-label"><lb:get key="global_title"/>：</td>
                         
                         <td class="wzb-form-control">
                             <div class="wzb-selector"><input type="text" value="" name="wmsg_subject" class="form-control"></div>
                         </td>
                    </tr>
                     
                    <tr>
                         <td class="wzb-form-label" valign="top"><lb:get key="global_content"/>：</td>
                         
                         <td class="wzb-form-control">
                             <textarea name="wmsg_content" class="wzb-textarea-02" onfocus="if(value==fetchLabel('subordinate_message_content_desc')){value=''}" onblur="if (value ==''){value=fetchLabel('subordinate_message_content_desc')}"><lb:get key="subordinate_message_content_desc"/></textarea>
                         </td>
                    </tr>
                    
                    <tr>
                         <td class="wzb-form-label"></td>
                         
                         <td class="wzb-form-control">
                             <input type="button" class="btn wzb-btn-orange wzb-btn-big margin-right15" value='<lb:get key="btn_submit"/>' onclick="sendWebMessage()"/>
                         </td>
                    </tr>
                 </table>
                 </form>
            </div> <!-- wzb-wenda-area End-->
		</div> <!-- xyd-article End-->
	</div>
</div> <!-- xyd-wrapper End-->


<div class="modal fade" tabindex="-1"  role="dialog" aria-labelledby="" aria-hidden="true" id="userList">
	<div id="TB_window" style="margin-left: -340px; width: 680px; margin-top: -310px; display:block;height:">
			<div id="TB_title">
				<div id="TB_ajaxWindowTitle"></div>
				<div id="TB_closeAjaxWindow">
					<a aria-label="Close" data-dismiss="modal" href="javascript:void(0)" class="TB_closeWindowButton"></a>
				</div>
			</div>
			<div id="TB_ajaxContent" style="width: 680px; height: 450px">
				<div class="wzb-model-3">
					<div class="form-search form-tool">
						<input name="searchContent" type="text" class="form-control" placeholder="<lb:get key="attention_find_desc"/>"><input
							type="button" class="form-submit" value="" onclick="searchSubordinate()">
					</div>
	
					<div class="form-tool wzb-title-3">
						<lb:get key="subordinate_filter_result"/>
	
						<div class="form-tool-right">
							<input onclick="addAll()" type="button" class="btn wzb-btn-yellow" value="<lb:get key="button_add_all"/>">
						</div>
					</div>
	
					<div class="wzb-percent clearfix">
	
					</div>
	
				</div>
			</div>
		</div>
</div>

	
</body>
</html>