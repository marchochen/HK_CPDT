<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<%@ include file="../../common/template.jsp"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-learner.css" />
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-admin.css" />
<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_im_${lang}.js"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_${lang}.js"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_rm_${lang}.js"></script>
<title></title>
<script type="text/javascript">
	//消息列表
	$(function(){
		$("a[data-target='#chooseUser']").live('click',function(){
			$(".form-submit").click();
		});
		changeTab("#rec_message");
	});

	var message_type;
	function changeTab(thisObj){
		message_type = $(thisObj).attr("name");
		$(thisObj).find(".wzb-list-11").empty().table({
			url : '${ctx}/app/admin/message/getMyWebMessage/' + message_type,
			colModel : colModel,
			rp : 5,
			hideHeader : true,
			usepager : true,
			userCss : 'wzb-list-11',
			onSuccess : function(){
				$(thisObj).find(".wzb-list-11 li:eq(0) a").click();
				//$(thisObj).find(".wzb-list-11 li:eq(0) a").attr('class','skin-color');
			}
		});
		$(thisObj).find(".wzb-btn-send").click();//wirteMessage('rec_writeMessage','','','');
	}

	var colModel = [{
		format : function(data){
			var status = 'Y';
			if(data.readHistory == undefined && message_type != 'send_message'){
				status = 'N';
			}
			
			var p = {
				msg_id : data.wmsg_id,
				title : data.sendUser.usr_display_bil,
				time : data.wmsg_create_timestamp,
				subject : data.wmsg_subject,
				type : data.wmsg_type,
				status : status,
				message_type : message_type
			}
			return $("#messageTemplate").render(p);
		}
	}];
	
	//转义字符
	function escapeJquery(srcString){
	    // 转义之后的结果
	    var escapseResult = srcString;
	    // javascript正则表达式中的特殊字符
	    var jsSpecialChars = ["\\", "^", "$", "*", "?", ".", "+", "(", ")", "[",
	            "]", "|", "{", "}"];
	    // jquery中的特殊字符,不是正则表达式中的特殊字符
	    var jquerySpecialChars = ["~", "`", "@", "#", "%", "&", "=", "'", "\"",
	            ":", ";", "<", ">", ",", "/"];
	    for (var i = 0; i < jsSpecialChars.length; i++) {
	        escapseResult = escapseResult.replace(new RegExp("\\"
	                                + jsSpecialChars[i], "g"), "\\"
	                        + jsSpecialChars[i]);
	    }
	    for (var i = 0; i < jquerySpecialChars.length; i++) {
	        escapseResult = escapseResult.replace(new RegExp(jquerySpecialChars[i],
	                        "g"), "\\" + jquerySpecialChars[i]);
	    }
	    return escapseResult;
	}

	function detail(id,title,time,status,type){
		
		if(status == 'N'){
			$.ajax({
				url : '${ctx}/app/admin/message/readWebMessage/' + id,
				type : 'POST',
				dataType : 'json',
				data : {
					message_type : message_type
				},
				success : function(data) {
					if(data.message == 'ok'){
						id = escapeJquery(id);
						$("#msg" + id).attr("onclick","detail(" + id + ",'" + title + "','" + time + "','Y','"+type+"')");
						$("#msg" + id).removeClass("color-blue00a");
						$("#msg" + id).children().attr("style"," ");
					}
				}
			});
		}

	 	$.ajax({
			url : '${ctx}/app/admin/message/getWebMessageDetail/' + id,
			type : 'POST',
			dataType : 'json',
			success : function(data){
				p = {
					title : title,
					time : time,
					type : type,
					msg_id : id,
					usr_id : data.webMessage.wmsg_create_ent_id,
					send_ent_id : data.webMessage.wmsg_send_ent_id,
					form_info : fetchLabel('label_core_information_management_46'),
					form : data.webMessage.wmsg_type=='SYS' ? '<lb:get key="label_im.label_core_information_management_25"/>' :  data.webMessage.sendUser.usr_display_bil ,
					content :replaceVlaue(Wzb.htmlDecode(data.webMessage.wmsg_admin_content_pc?data.webMessage.wmsg_admin_content_pc:data.webMessage.wmsg_content_pc)),
					message_type : message_type,
					usr_name : data.webMessage.sendUser.usr_display_bil,
					to_usr_name : data.webMessage.recUser.usr_display_bil,
					to_usr_id : data.webMessage.recUser.usr_ent_id,
					to_usr_name_label : fetchLabel('lab_message_to_user')
				}
				$("div[name='message_content']").html(Wzb.htmlDecode($('#contentTemplate').render(p)));
			}
		}); 
	}

	function delById(id,message_type){
		if(confirm(fetchLabel('label_core_information_management_40'))){
			$.ajax({
				url : '${ctx}/app/admin/message/delById',
				data : {
					id : id,
					message_type : message_type
				},
				type : 'POST',
				success : function(){
					Dialog.alert(fetchLabel('label_core_information_management_41'));
					window.location.href = "${ctx}/app/admin/message/main";
				}
			});
		}
	}


	//写信息
	function wirteMessage(message_type,usr_id,title,usr_name){
		var time = Wzb.formatDate(new Date(),'yyyy-MM-dd HH:mm:ss');
		p = {
				message_type : message_type,
				time : time,
				usr_id : usr_id,
				title : title,
				usr_name : usr_name
		}
		$("#rec_message div[name='message_content']").empty();
		$("#send_message div[name='message_content']").empty();
		if(message_type == "rec_writeMessage" || message_type == 'reply'){
			$("#rec_message div[name='message_content']").html(Wzb.htmlDecode($('#contentTemplate').render(p)));
		} else {
			$("#send_message div[name='message_content']").html(Wzb.htmlDecode($('#contentTemplate').render(p)));
		}
	}


	function searchUserList(){
		var search_name = $("input[name='search_add_member']").val();
		searchUser(search_name);
	}

	//搜索用户
	function searchUser(search_name){
		$("#add_member").html('');
		$("#add_member").table({
			url : '${ctx}/app/admin/message/findUserList',
			params : {
				search_name : search_name
			},
			gridTemplate : function(data){
				var add = true;
				if($("#send_user_" + data.usr_ent_id).attr("class") != undefined){
					add = false;
				}
				p = {
					image : '${ctx}' + data.usr_photo,
					usr_display_bil : data.usr_display_bil,
					usg_display_bil : data.usg_display_bil,
					ugr_display_bil : data.ugr_display_bil,
					usr_ent_id : data.usr_ent_id,
					add : add
				}
				return $('#user-template').render(p);
			},
			rowCallback : function(){
				photoDoing();
			},
			view : 'grid',
			rowSize : 4,
			rp : 12,
			showpager : 5,
			usepager : true,
			hideHeader : true,
			trLine : false,
			onSuccess : function(data){
                //给添加按钮事件
            	//默认选中的给予标记选中
             	 $(".wzb-choose-box .wzb-choose-info").each(function(){
             		var usr_ent_id = $(this).attr("value");
             		var usr_display_bil =  $(this).find(".wzb-choose-detail").text();
             		p = {
             			usr_ent_id : usr_ent_id,
             			usr_display_bil : usr_display_bil
             		}
             		$("#user_" + usr_ent_id).html($("#cancel-user-button").render(p));
                });
            }
		});
	}


	//添加选中的收件人
	function addSendUser(usr_ent_id,usr_display_bil){
		var p = {
				usr_ent_id : usr_ent_id,
				usr_display_bil : usr_display_bil
		}
		$("#user_" + usr_ent_id).html($("#cancel-user-button").render(p));

		$(".wzb-choose-box").html($(".wzb-choose-box").html() + $("#detele-user-button").render(p));
	}
	//取消已选中的人
	function cancelUser(usr_ent_id, usr_display_bil){
		var p = {
			usr_ent_id : usr_ent_id,
			usr_display_bil : usr_display_bil
		}
		$("#user_" + usr_ent_id).html($("#add-user-button").render(p));

		$("#send_user_" + usr_ent_id).remove();
	}

	//添加全部按钮
	function addAll(){
		$("a[name='addUser']").click();
	}

	function clearAll(){
		$("a[name='clearAll()']").click();
		$(".wzb-choose-box").html('');
		$("a[name='cancelUser']").click();
	}
	
	var sendingMsg = false;
	//发送消息
	function sendMessage(){
		
		//控制表单重复提交
		if(!sendingMsg){
			sendingMsg = true;
		}else{
			return;
		}
		
		var user_list = $(".wzb-choose-box").find("div");
		var usr_ent_id_str = user_list.eq(0).attr("value");
		for(var i=1;i<user_list.length;i++){
			usr_ent_id_str += '~' + user_list.eq(i).attr("value");
		}
		var title = $("#kbi_title").val();
		var formContent = $("#formText").val();
		var title = title.replace(/(^\s*)|(\s*$)/g, '');
		var formContent = formContent.replace(/(^\s*)|(\s*$)/g, '');
		if(user_list.length == 0){
			Dialog.alert(fetchLabel('label_core_information_management_42'));
			sendingMsg = false;
			return;
		}else if(title == '' || title == null){
			Dialog.alert(fetchLabel('label_core_information_management_43'));
			sendingMsg = false;
			return;
		}else if( getChars(title)>80 ){
			Dialog.alert(cwn.getLabel('label_core_requirements_management_55'));
			sendingMsg = false;
			return;
		}else if(formContent == '' || formContent == null){
			Dialog.alert(fetchLabel('label_core_information_management_44'));
			sendingMsg = false;
			return;
		}else if(getChars(formContent) > 2000){
			Dialog.alert(fetchLabel('label_core_requirements_management_65'));
			sendingMsg = false;
			return;
		}else{
			$.ajax({
				url : '${ctx}/app/admin/message/sendMessage',
				type : "post",	
				data :{
					usr_ent_id_str : usr_ent_id_str,
					wmsg_subject : title,
					wmsg_content_pc : formContent
				},
				success : function(){
					Dialog.alert(fetchLabel('label_core_information_management_45'));
					window.location.href = "${ctx}/app/admin/message/main";
				}
			});
		}
	}

	function changeWmsgType(){
		$("#message_list").html('');
		//$("#messageCont").html('');
		dt = $("#message_list").table({
			url : '${ctx}/app/admin/message/getMyWebMessage/' + $("#wmsg_type_list :selected").val(),
			colModel : colModel,
			rp : 5,
			showpager : 3,
			hideHeader : true,
			usepager : true,
			useCss : 'wzb-list-11',
			onSuccess : function(){
				$("#message_list li:eq(0) a").click();
			}
		});
	}
</script>

<script id="messageTemplate" type="text/x-jquery-tmpl">
  <li>
  	   <a class="{{if status == 'N'}}color-blue00a{{/if}}" id="msg{{>msg_id}}" href="javascript:void(0)" name="{{>message_type}}" onclick="detail('{{>msg_id}}','{{>subject}}','{{>time.substring(0,19)}}','{{>status}}','{{>type}}')" >
    	 <i class="fa fa-volume-down {{if status == 'N'}}skin-color{{/if}}" style="{{if status == 'N'}}color:#00aeef{{/if}}"></i>
         <span>{{>subject}}</span>
       </a>
    <p>{{>time.substring(0,10)}}</p>
  </li>
</script>

<script id="contentTemplate" type="text/x-jquery-tmpl">
{{if message_type == 'rec_message' || message_type == 'send_message'}}
	<div class="wzb-entity">
          <div class="wzb-entity-box">
               <h1 class="wzb-entity-title">{{>title}}</h1>
                <div class="wzb-entity-info">
					{{>time}}  <span class="margin-left20">{{>form_info}}：{{>form}}</span>
					<span class="margin-left20">{{>to_usr_name_label}}：{{>to_usr_name}}</span>
					<span>
						<a class="btn pull-right wzb-btn-yellow wzb-btn-normal" onclick="delById('{{>msg_id}}','{{>message_type}}')"><lb:get key="global.button_del"/></a>
						<a class="btn pull-right wzb-btn-yellow wzb-btn-normal margin-right10" onclick="wirteMessage('reply',{{>usr_id}},'{{>title}}','{{>usr_name}}')"
							{{if message_type =='send_message' || type=='SYS' }}style="display:none" {{/if}} href="javascript:void(0)">
							<lb:get key="label_cm.label_core_community_management_172"/>
						</a>
					</span>
				</div>
         </div>
     </div>

	<div class="panel-content">
			{{>content}}
	</div>
{{else}}
	<div class="xyd-content">
		    <div class="wzb-model-9">
		         <div class="wzb-entity">
		              <div class="wzb-entity-box">
		                   <h1 class="wzb-entity-title"><lb:get key="label_im.label_core_information_management_26"/></h1>
		                   <div class="wzb-entity-info">{{>time}}</div>
		             </div>
		         </div>
		    </div>

		     <div class="wzb-model-10">
		         <div class="wzb-choose">
		              <div class="wzb-choose-title"><span class="wzb-form-star">*</span><lb:get key="label_im.label_core_information_management_27"/></div>

		              <div class="wzb-choose-box">
						{{if message_type == 'reply'}}
							<div id="send_user_{{>usr_id}}" class="sendinfo" value="{{>usr_id}}">
								<span class="sendcont">{{>usr_name}}<span>
							</div>
						{{/if}}
					  </div>

		              <div class="wzb-choose-tool">
		                   <a data-toggle="modal" data-target="#chooseUser"  href="javascript:;" class="wzb-box-4-add btn wzb-btn-blue"><lb:get key="label_im.label_core_information_management_28"/></a> &nbsp;|&nbsp;<a class="skin-color" href="javascript:;" onclick="clearAll()"><lb:get key="label_im.label_core_information_management_29"/></a>

							<div class="modal fade" id="chooseUser" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
					         <div class="modal-dialog">
		                        <div class="modal-content">
		                            <div class="modal-header">
		                                <button type="button" class="close" data-dismiss="modal"
		                                    aria-label="Close">
		                                    <span aria-hidden="true" class="font-size28">&times;</span>
		                                </button>
		                                <h4 class="modal-title" id="myModalLabel"><lb:get key="label_im.label_core_information_management_28"/></h4>
		                            </div>
		                            <div class="modal-body" style="height:552px;overflow-y: auto;">
		                                 <form>
											<div class="form-search form-tool">
												<input type="text" name="search_add_member" class="form-control"
													placeholder='<lb:get key="label_im.label_core_information_management_30"/>' /><input type="button"
													class="form-submit" value="" onclick="searchUserList()"/>
											</div>

											<div class="form-tool wzb-title-3">
												<lb:get key="label_im.label_core_information_management_31"/>
												<div class="form-tool-right">
													<input type="button" class="btn wzb-btn-yellow" value="<lb:get key="label_im.label_core_information_management_32"/>"  onclick="addAll()"/>
												</div>
											</div>
											  <div class="wzb-percent clearfix">
											 	<span id="add_member"></span>
											 </div>
		                                  </form>
		                            </div>
		                        </div>
		                    </div>

						</div>
		              </div>
		         </div>


		         <div class="wzb-wenda-area">
		             <div class="wzb-wenda-title"><lb:get key="label_im.label_core_information_management_33"/></div>

		             <table>
		                <tr>
		                     <td class="wzb-form-label"><span class="wzb-form-star">*</span><lb:get key='label_im.label_core_information_management_7'/>：</td>

		                     <td class="wzb-form-control">
		                         <div class="wzb-selector"><input type="text" {{if message_type == 'reply'}}value="<lb:get key='label_cm.label_core_community_management_172'/> {{>title}}"{{/if}} id="kbi_title" name="kbi_title" class="form-control"></div>
		                     </td>
		                </tr>

		                <tr>
		                     <td class="wzb-form-label" valign="top"><span class="wzb-form-star">*</span><lb:get key='label_im.label_core_information_management_34'/>：</td>

		                     <td class="wzb-form-control">
		                         <textarea prompt=" " class="wzb-textarea-02" id="formText"></textarea>
		                     </td>
		                </tr>

                        <tr>
                          <td class="wzb-form-label" valign="top"><span class="wzb-form-star">*</span></td>
                          <td class="wzb-form-control" >
             	             <!-- 为必填 --><lb:get key="label_rm.label_core_requirements_management_35" />
                          </td>
                       </tr>

                         <tr>
		                     <td class="wzb-form-label"></td>

		                     <td class="wzb-form-control">
		                         <input type="button" onclick="javascript:sendMessage();" class="btn wzb-btn-blue wzb-btn-big margin-right15" value="<lb:get key='label_im.label_core_information_management_35'/>" name="frmSubmitBtn">
		                     </td>
		                </tr>
		            </table>
		        </div> <!-- wzb-wenda-area End-->
		    </div>
		</div> <!-- xyd-content End-->
{{/if}}
</script>

<script id="user-template" type="text/x-jquery-tmpl">
		<div class="wzb-percent-4 wzb-display-01">
			<dl class="wzb-list-7 clearfix">
				<dd>
					<div class=" wzb-user68">
						<a href="javascript:;" class="mypic">
							{{if image == 'undefined'}}
								<img class="wzb-pic" src="${ctx}/static/images/user68.jpg" alt="<lb:get key='label_cm.label_core_community_management_29'/>" />
							{{else}}
								<img class="wzb-pic" src="{{>image}}" alt="<lb:get key='label_cm.label_core_community_management_29'/>" />
							{{/if}}
						</a>
							<p class="companyInfo" style="display: none;"><lb:get key="label_cm.label_core_community_management_29"/></p>
						 	<div class="cornerTL">&nbsp;</div>
						 	<div class="cornerTR">&nbsp;</div>
						 	<div class="cornerBL">&nbsp;</div>
						 	<div class="cornerBR">&nbsp;</div>
					</div>
				</dd>
				<dt>
					<span class=""  href="javascript:void(0)">{{>usr_display_bil}}</span>
					<p>{{>usg_display_bil}}</p>
					<p>{{if ugr_display_bil != 'Unspecified'}}{{>ugr_display_bil}}{{/if}}</p>
				</dt>
			</dl>
			<span id="user_{{>usr_ent_id}}"  class="wzb-user-plus">
				{{if add == true}}
					<a name="addUser" class="wzb-link04" href="javascript:;" onclick="addSendUser({{>usr_ent_id}},'{{>usr_display_bil}}')">
						<i class="fa fa-plus"></i>
						<lb:get key="global.button_add"/>
					</a>
				{{else}}
					<a name="addUser" class="wzb-link04" href="javascript:;" onclick="cancelUser({{>usr_ent_id}},'{{>usr_display_bil}}')">
						<i class="fa fa-plus"></i>
						<lb:get key="global.button_add"/>
					</a>
				{{/if}}
			</span>
		</div>
</script>

<script id="add-user-button" type="text/x-jsrender">
	<a name="addUser" class="wzb-link04" href="javascript:;" onclick="addSendUser({{>usr_ent_id}}, '{{>usr_display_bil}}')">
		<i class="fa fa-plus"></i>
		<lb:get key="global.button_add"/>
	</a>
</script>
<script id="cancel-user-button" type="text/x-jsrender">
	<span class="skin-color">
		<i class="fa f14 fbold mr5 fa-check"></i>
		<lb:get key="label_im.label_core_information_management_36"/>
	</span> |
	<a name="cancelUser" class="grayC666" href="javascript:;" onclick="cancelUser({{>usr_ent_id}},'{{>usr_display_bil}}')"><lb:get key="global.button_cancel"/></a>
</script>

</head>
<body>
<div class="wzb-main">

	<!-- wzb-breadcrumb start-->
	<input type="hidden" name="belong_module" value="FUN_SYS_MESSAGE" />
	<title:get function="global.FTN_SYS_MESSAGE" />
	<ol class="breadcrumb wzb-breadcrumb">
		<li><a href="javascript:wb_utils_gen_home(true);">
				<i class="fa wzb-breadcrumb-home fa-home"></i>
				<lb:get key="label_km.label_core_knowledge_management_1" />
			</a>
		</li>
		<li class="active">
			<lb:get key="message_PERSON" />
		</li>
	</ol>
	<!-- wzb-breadcrumb End-->

<div class="panel wzb-panel">

<div class="panel-body">
<div role="tabpanel" class="wzb-tab-1">
<ul class="nav nav-tabs" role="tablist">
     <li role="presentation" class="active"><a href="#rec_message" aria-controls="rec_message" role="tab" data-toggle="tab" onclick=""><lb:get key="label_im.label_core_information_management_38"/></a></li>
		    <%-- <li role="presentation"><a href="#send_message" aria-controls="send_message" role="tab" data-toggle="tab" onclick="changeTab('#send_message')"><lb:get key="label_im.label_core_information_management_37"/></a></li> --%>
</ul>

<div class="tab-content">
    <div role="tabpanel" class="tab-pane active" id="rec_message"  name="rec_message" >
          <div class="wzb-area clearfix">
                <div class="wzb-area-left">
                    <%-- <a class="wzb-btn-send margin-bottom15" href="javascript:void(0)" onclick="wirteMessage('rec_writeMessage','','','')"><i class="fa wzb-send-icon fa-pencil-square-o"></i><lb:get key="label_im.label_core_information_management_26"/></a> --%>
                   <input type="text" readonly="readonly" class="messnav" value="<lb:get key="status_all"/>(${wmsg_person_total + wmsg_sys_total })"/>
                     <%-- <select class="messnav" id="wmsg_type_list" onchange="changeWmsgType()">
						<option value="ALL"><lb:get key="status_all"/>(${wmsg_person_total + wmsg_sys_total })</option>
						<option value="SYS"><lb:get key="message_SYS"/>(${wmsg_sys_total })</option>
						<option value="PERSON"><lb:get key="message_PERSON"/>(${ wmsg_person_total })</option>
					</select>  --%>
                    <ul class="wzb-list-11" id="message_list"></ul>
                </div>

                <div class="wzb-area-content">
                	<div name="message_content"></div>
              	</div>
          </div>
     </div>
     <div role="tabpanel" class="tab-pane" id="send_message"  name="send_message">
          <div class="wzb-area clearfix">
                <div class="wzb-area-left">
                    <a class="wzb-btn-send margin-bottom15" href="javascript:void(0)" onclick="wirteMessage('send_writeMessage','','','')"><i class="fa wzb-send-icon fa-pencil-square-o"></i><lb:get key="label_im.label_core_information_management_26"/></a>
                    <ul class="wzb-list-11"></ul>
                </div>

                <div class="wzb-area-content">
                	<div name="message_content"></div>
              </div>
          </div>
     </div>
</div>
</div>

</div>
</div>  <!-- wzb-panel End-->

</div>  <!-- wzb-main End-->

</body>
</html>