<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../common/taglibs.jsp"%>

<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-admin.css" />
<link rel="stylesheet" href="${ctx }/static/js/jquery.uploadify/uploadify.css"/>
<title></title>

<script type="text/javascript">

window.onload = function(){
	var showComment="${isGroupMember != null and isGroupMember == true or isNormal != true}"
	if("${param.status}" == 'success'){
		Dialog.alert(fetchLabel("label_core_community_management_190"));
	}
}
$(function(){
	var len = 250;
	var desc = $("#grp_desc").attr("data");
	var group_desc_sub = desc ? substr(desc, 0, len) : "";
	if(desc == undefined || getChars(desc) < len) {
		$("#grp_desc").next().css("display","none");
	}
	$("#grp_desc").html(group_desc_sub);
	
	//简介展开收起
	$("a.open_desc").live('click',function() {
		var sub = $(this).children("i");
		if($(sub).hasClass("fa-angle-down")) {
			$(sub).removeClass("fa-angle-down").addClass("fa-angle-up").prev("span").html(fetchLabel('click_up'));
			var pv = $(this).prev();
			var data = $(pv).attr("data");
			if(data != undefined && data != '') {
				$(pv).empty().append(data);
			}
		} else {
			$(sub).removeClass("fa-angle-up").addClass("fa-angle-down").prev("span").html(fetchLabel('click_down'));
			var pv = $(this).prev();
			var data = $(pv).attr("data");
			if(data != undefined && data != '') {
				$(pv).empty().append(substr(data, 0, len));
			}
		}
	});
});
</script>
</head>
<body>
<input type="hidden" name="belong_module" value="FTN_AMD_SNS_MGT"/>


<title:get function="global.FTN_AMD_SNS_MGT"/>	

<ol class="breadcrumb wzb-breadcrumb">
  <li><a href="javascript:wb_utils_gen_home(true);"><i class="fa wzb-breadcrumb-home fa-home"></i><lb:get key="global.lab_menu_started" /></a></li>
  <li><a id="amd_group" href="${ctx }/app/admin/group">
			<lb:get key="label_cm.label_core_community_management_60" /></a>
  </li>
  <li class="active">${snsGroup.s_grp_title }</li>
</ol> <!-- wzb-breadcrumb End-->

<div class="panel wzb-panel">
<div class="panel-heading">${snsGroup.s_grp_title }</div>

<div class="panel-body" >
<div role="tabpanel" class="wzb-tab-1"  >
<!-- Nav tabs -->

 
<ul class="nav nav-tabs page-tabs" role="tablist">
    <li role="presentation" class="active"><a href="#home" aria-controls="home" role="tab" data-toggle="tab" ><lb:get key="label_cm.label_core_community_management_81"/> </a></li>
     <c:if test="${s_grp_private!=2 and (isGroupMember != null and isGroupMember == true or isNormal != true)}">
     	<!--  当不是任何人都能加入的时候 只要是该 成员或者说是有管理权限的就显示 -->
         <li role="presentation"><a href="#profile" aria-controls="profile" role="tab" data-toggle="tab"><lb:get key="label_cm.label_core_community_management_82"/> </a></li>
         <c:if test="${isManager != null and isManager == true or isNormal != true}">
         	<!--  管理员就显示 -->
             <li role="presentation"><a href="#approval" aria-controls="approval" role="tab" data-toggle="tab"><lb:get key="label_cm.label_core_community_management_83"/> </a></li>
             <li role="presentation"><a href="#settings" aria-controls="settings" role="tab" data-toggle="tab"><lb:get key="label_cm.label_core_community_management_84"/></a></li>
         </c:if>
     </c:if>
     
     <c:if test="${s_grp_private==2 and (isManager != null and isManager == true or isNormal != true)}">
     	<!--  任何人都能加入的时候  只有是管理员 或者有管理权限的就显示 -->
    <li role="presentation"><a href="#settings" aria-controls="settings" role="tab" data-toggle="tab"><lb:get key="label_cm.label_core_community_management_84"/></a></li>
     </c:if>
</ul>

 


<!-- Tab panes -->
<div class="tab-content">
     <div role="tabpanel" class="tab-pane active" id="home">
        <div class="wzb-model-1">
          <dl class="wzb-list-2 clearfix">
              <dd>
                   <img class="psdpic" src="${snsGroup.card_actual_path}">
<!--                    <input type="button" onclick="javascript:void(0);" class="btn wzb-btn-blue" value="解散群组" name="frmSubmitBtn">-->                                  
                        <c:if test="${isManager == true or s_grp_private!=2}">
                            <span class="pjia">                 
                            <c:choose>
                                <c:when test="${isNormal != true or isManager == true}">
                                    <input type="button" class="btn wzb-btn-blue" href="javascript:;" value="<lb:get key='label_cm.label_core_community_management_85'/>" onclick="signOutGroup('label_core_community_management_111')">
                                </c:when>
                                <c:when test="${snsGroup.s_gpm.s_gpm_status == 0}">
                                    <input type="button" class="btn wzb-btn-blue" value="<lb:get key='label_cm.label_core_community_management_149'/>"  onclick="signOutGroup('label_core_community_management_180')">
                                </c:when>
                                <c:when test="${snsGroup.s_gpm.s_gpm_status == 3 or snsGroup.s_gpm.s_gpm_status == null}">
                                    <input type="button" class="btn wzb-btn-blue"  value="<lb:get key='label_cm.label_core_community_management_87'/>" onclick="applyJoinGroup()">
                                </c:when>
                                <c:when test="${snsGroup.s_gpm.s_gpm_status == 1}">
                                    <input type="button" class="btn wzb-btn-blue" value="<lb:get key='label_cm.label_core_community_management_88'/>" onclick="signOutGroup('label_core_community_management_181')">
                                </c:when>
                            </c:choose>
                            </span>
                        </c:if>
              </dd>
              <dt>
                   <p><span class="color-gray999"><lb:get key="label_cm.label_core_community_management_67"/>：</span><fmt:formatDate value="${snsGroup.s_grp_create_datetime}" pattern="yyyy-MM-dd"/></p>
                   <p><span class="color-gray999"><lb:get key="label_cm.label_core_community_management_66"/>：</span>
                   				 ${snsGroup.user.usr_display_bil}</p>
					<p>
						<span class="color-gray999"><lb:get key="group_desc"/>：</span>
						<span id="grp_desc" data="${snsGroup.s_grp_desc}"></span>
			            <a class="wzb-show skin-color open_desc" style="" >
							<span><lb:get key="click_down"/></span>
							<i class="fa fa-angle-down"></i>
					 	</a>
					</p>
              </dt>
          </dl>
   
          
          <c:if test="${(isGroupMember != null and isGroupMember == true or isNormal != true) or snsGroup.s_grp_private == '2'}">
          			<div class="wzb-title-2" style="font-weight: normal;"><lb:get key="label_cm.label_core_community_management_90"/>...</div>
          
          	
          			<div class="wzb-send" id="editer" style="width:540px;"></div>
          	</c:if>
           <c:if test="${(isGroupMember != null and isGroupMember == true ) or snsGroup.s_grp_private == '2' or is_admin == true}">
            	<div class="wzb-title-2" style="font-weight: normal;"><lb:get key="label_cm.label_core_community_management_91"/> </div>
            	<div id="commentList"></div>
      		</c:if>
        </div>  
     </div>
     
     <div role="tabpanel" class="tab-pane" id="profile">
        <div class="wzb-model-2"  >  
          <form class="form-search form-tool"onsubmit="return false">                 
              <input type="text" class="form-control" placeholder="<lb:get key='label_cm.label_core_community_management_92' />"><input type="button"  class="form-submit" value="">

              <div class="form-tool-right">
                	<c:if test="${isManager == true}">
                   <a class="btn wzb-btn-yellow" data-toggle="modal" data-target="#popupAddContent" ><lb:get key="label_cm.label_core_community_management_93"/> </a>
                 
                        <button type="button" class="btn wzb-btn-yellow"> <lb:get key="label_cm.label_core_community_management_94"/> </button>
                   </c:if>
              </div>  
          </form>
 
          <div class="wzb-percent clearfix datatable" style="border-bottom:0;">            
<!--                     成员信息在此 -->
<!--                     成员信息在此 -->
          </div>  
                 
       </div>   
     </div>
        <div id="popupAddContent" class="modal fade " tabindex="-1" role="dialog"  aria-labelledby="addLabel"
            aria-hidden="true" >
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true" style="font-size:30px">&times;</span>
                        </button>
                        <h4 class="modal-title" id="addLabel"><lb:get key="label_cm.label_core_community_management_95"/></h4>
                    </div>
                    <div class="modal-body" style="height:552px;overflow-y: auto;">
                           <!--  <div class="wzb-model-3"> -->
                                   <form class="form-search form-tool">
                                       <input type="text" class="form-control" placeholder="<lb:get key='label_cm.label_core_community_management_92'/>">
                                       <input type="button" class="form-submit" value="">
                                   </form>

                                   <div class="form-tool wzb-title-3">
<!--                                                                 筛选结果 --><lb:get key="label_cm.label_core_community_management_96"/>
                                       <div class="form-tool-right">
                                           <c:if test="${snsGroup.s_grp_private != '3' }">
                                           <wb:hasRole rolExtIds="INSTR_1,ADM_1,TADM_1">
                                           <button type="button" class="btn wzb-btn-yellow" id="instrOnly"><lb:get key="label_cm.label_core_community_management_97"/> </button>
                                           </wb:hasRole>
                                           </c:if>
                                           <button type="button" class="btn wzb-btn-yellow" id="addAll"><lb:get key="label_cm.label_core_community_management_98"/> </button>
                                       </div>
                                   </div>

                                   <div class="wzb-percent clearfix" style="border-bottom:0;">

                                   </div>
                            <!-- </div> -->
                    </div>
                    <%-- <div class="modal-footer" style="margin-top:0px;">
                        <button type="button" class="btn btn-default" data-dismiss="modal"><lb:get key="button_close" /></button>
                    </div> --%>
                </div>
            </div>
        </div>

<!-- 审批 -->
        <div role="tabpanel" class="tab-pane" id="approval">
          <div role="tabpanel" class="wzb-tab-3" style="margin-left:0px">
               <!-- Nav tabs -->
               <ul class="nav nav-tabs" role="tablist">
                   <li role="presentation" class="active"><a href="#waiting" aria-controls="waiting" role="tab" data-toggle="tab" style="padding-left:0px"><lb:get key="label_cm.label_core_community_management_99"/> </a></li>
                   <li role="presentation"><a href="#passing" aria-controls="passing" role="tab" data-toggle="tab"><lb:get key="label_cm.label_core_community_management_100"/></a></li>
                   <li role="presentation"><a href="#refusal" aria-controls="refusal" role="tab" data-toggle="tab"><lb:get key="label_cm.label_core_community_management_101"/></a></li>
               </ul>
                
               <!-- Tab panes -->
               <div class="tab-content">
                    <div role="tabpanel" class="tab-pane active" id="waiting">
                        <div class="tabcontent wzb-table-input">
                        
                        </div>                        
                        <div class="wzb-bar">
                             <input id="approve" type="button" onclick="javascript:void(0);" class="btn wzb-btn-blue wzb-btn-big margin-right10" value='<lb:get key="global.button_approve"/>' name="frmSubmitBtn">
                             <input id="reject" type="button" onclick="javascript:void(0);" class="btn wzb-btn-blue wzb-btn-big " value='<lb:get key="global.button_reject"/>' name="frmSubmitBtn">
                        </div>
                    </div>
                    <div role="tabpanel" class="tab-pane" id="passing">
                        <div class="tabcontent wzb-table-input">
                        
                        </div>    
                    </div>
                    
                    <div role="tabpanel" class="tab-pane" id="refusal">
                        <div class="tabcontent wzb-table-input">
                        
                        </div>    
                    </div>
               </div>
          </div>
     </div>
     
     <div role="tabpanel" class="tab-pane" id="settings">
        <div class="wzb-model-2">  
             <form:form action="${ctx}/app/admin/group/detail/update" modelAttribute="snsGroup" method="post" enctype="multipart/form-data" id="groupForm">
             <input name="s_grp_id" type="hidden" value="${snsGroup.s_grp_id}">
             <table>
                    <tr>
                         <td class="wzb-form-label"><span class="wzb-form-star">*</span><lb:get key="label_cm.label_core_community_management_63"/>：</td>
                         
                         <td class="wzb-form-control">
                             <div class="wzb-selector" style="width:600px">
                             	<form:input type="text" class="form-control" style="width:400px;"   path="s_grp_title" id="s_grp_title"/>
                             	&nbsp;<span  class="error" >${param.error }</span>
                             </div>
                         </td>
                    </tr>
                    
                    <tr>
                         <td class="wzb-form-label" valign="top"><lb:get key="label_cm.label_core_community_management_69"/>：</td>
                         
                         <td class="wzb-form-control">
                             <div class="wzb-selector">
                                <form:textarea id="s_grp_desc" path="s_grp_desc" class="form-control"></form:textarea>
                             </div>
                         </td>
                    </tr>
                    
                    <tr>
                         <td class="wzb-form-label" valign="top"><lb:get key="label_cm.label_core_community_management_70"/> ：</td>
                         
                         <td class="wzb-form-control">
                             <dl class="wzb-list-8">
                                 <dd>
									<img id="group_image" class="fl psdpic mr10" src="${ctx}${snsGroup.card_actual_path}"/>
									<p class="psda"><label for="psda" class="radiosite"><input type="radio" name="group_card" value="0" onclick="changeGroupCard(this,'${snsGroup.s_grp_card}')" checked="checked" id="psda"><lb:get key="label_cm.label_core_community_management_102"/></label></p>
									<p class="psdb"><label for="psdb" class="radiosite"><input type="radio" name="group_card" value="1" onclick="changeGroupCard(this,'')" id="psdb"><lb:get key="label_cm.label_core_community_management_103"/></label></p>
									<p class="psdc"><label for="psdc" class="radiosite"><input type="radio" name="group_card" value="2" onclick="changeGroupCard(this,'')" id="psdc"><lb:get key="label_cm.label_core_community_management_104"/></label></p>
									<div class="file" style="margin-left: 15px;" onclick=" document.getElementById('psdc').checked=true;">
	   									<input id="file" class="file_file" name="image" type="file"  onchange="$('#textfield').val(this.value);$('#textfield').attr('title',this.value);"/>
										<input id="textfield" class="file_txt" value='<lb:get key="label_cm.label_core_community_management_105"/>'/>
										<div class="file_button-blue"><lb:get key="label_cm.label_core_community_management_75"/></div>
									</div>
								  	<input type="hidden" name="s_grp_card" value='${snsGroup.s_grp_card}'>
                                 </dd> 
                             </dl>      
                         </td>
                    </tr>
                    
                    <tr>
                         <td class="wzb-form-label" valign="top"><lb:get key="label_cm.label_core_community_management_76"/>：</td>
                         
                         <td class="wzb-form-control" >
                             <c:if test="${snsGroup.s_grp_private != '2' }">
                             <label class="wzb-input-label" for="psde"><input type="radio" id="psde" class="s_grp_private" checked="checked" value="0" name="s_grp_private"><lb:get key="label_cm.label_core_community_management_77"/> </label>
                             <%-- <wb:hasRole rolExtIds="INSTR_1,ADM_1,TADM_1" > --%>
                                <label class="wzb-input-label" for="psdh"><input type="radio" id="psdh" class="s_grp_private"  value="3" name="s_grp_private"><lb:get key="label_cm.label_core_community_management_78"/></label>
                             <%-- </wb:hasRole>   --%>       
                             <label class="wzb-input-label" for="psdf"><input type="radio" id="psdf" class="s_grp_private"  value="1" name="s_grp_private"><lb:get key="label_cm.label_core_community_management_79"/></label>
                             </c:if>
                             <label class="wzb-input-label" for="psdg"><input type="radio" id="psdg" class="s_grp_private"  value="2" name="s_grp_private"><lb:get key="label_cm.label_core_community_management_106"/></label>
                         </td>
                    </tr>
                     <tr>
				         <td class="wzb-form-label" valign="top"><span class="wzb-form-star">*</span></td>
				         <td class="wzb-form-control" >
				             	<!-- 为必填 --><lb:get key="label_rm.label_core_requirements_management_35" />
				         </td>
				    </tr>
                </table>
                
                <div class="wzb-bar">
                     <input type="button" class="btn wzb-btn-blue wzb-btn-big margin-right15" onclick="updateSnsGroup()" value='<lb:get key="global.button_ok"/>' name="frmSubmitBtn">
                   <!--  <input type="button"  onclick="javascript:history.go(-1);" class="btn wzb-btn-blue wzb-btn-big" value="<lb:get key="global.button_cancel"/>" name="frmSubmitBtn">  --> 
                     <a class="btn wzb-btn-blue wzb-btn-big" href="${ctx }/app/admin/group"><lb:get key="global.button_cancel"/></a>
                </div>
                </form:form>
        </div>  
     </div>     
</div>
</div>

</div>
</div>  <!-- wzb-panel End-->
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_cm_${lang}.js"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_${lang}.js"></script>
<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<script type="text/javascript" src="${ctx}/static/js/front/sns.js"></script>
<script type="text/javascript" src="${ctx}/static/js/front/jquery.sns.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery.prompt.js"></script>
<script type="text/javascript" src="${ctx}/static/js/jquery.uploadify/jquery.uploadify.js"></script>
<%-- <script type="text/javascript" src="${ctx}/static/js/jquery.comments.js"></script> --%>
<script type="text/javascript"> 
var sessionid = '${pageContext.session.id}';
var sns = new Sns();
var targetId;
var isNormal = '${isNormal}';
var showComment="${isGroupMember != null and isGroupMember == true or isNormal != true}";

var showtab = cwn.getUrlParam('showtab');
if(showtab=='opengroup'){
	showComment = 'true';
}
$("#amd_group").attr("href",contextPath+"/app/admin/group?showtab="+showtab+"&isView="+cwn.getUrlParam('isView'));
if(cwn.getUrlParam('isView') == 'true'){
	$("#amd_group").html("<lb:get key='label_cm.label_core_community_management_209' />");
}

var meId = '${regUser.usr_ent_id}';
var module = "Group";
var grpId = '${s_grp_id}';
//memeber
var isManager = '${isManager}';
var grpUid = '${snsGroup.s_grp_uid}';
var tab = '${tab}' || cwn.getUrlParam("tab");
var grpPrivate = '${snsGroup.s_grp_private}';
</script>
<script type="text/javascript" src="${ctx}/static/admin/js/group_detail.js"></script>
<script type="text/javascript" src="${ctx}/static/js/jquery.cwn.js"></script>
<script type="text/javascript" src="${ctx}/static/js/cwn_util.js"></script>

<script id="doingTemplate" type="text/x-jsrender">
 <div class="wzb-trend clearfix" did="{{>s_doi_id}}">
     <div class="wzb-user wzb-user68">
        {{if s_doi_target_type == '1' && operator}}
            <a {{if isNormal == true}}href="${ctx}/app/personal/{{>operator.usr_ent_id}}"{{/if}}> <img class="wzb-pic" src="{{>operator.usr_photo}}"/></a>
        {{else}}
            <a {{if isNormal == true}}href="${ctx}/app/personal/{{>user.usr_ent_id}}"{{/if}}> <img class="wzb-pic" src="{{>user.usr_photo}}"/></a>
        {{/if}}
        {{if isNormal == true}}
            <p class="companyInfo"><lb:get key="label_cm.label_core_community_management_29"/></p>
            <div class="cornerTL">&nbsp;</div>
            <div class="cornerTR">&nbsp;</div>
            <div class="cornerBL">&nbsp;</div>
            <div class="cornerBR">&nbsp;</div>
        {{/if}}
     </div>
        
     <div class="wzb-trend-content">
         {{if s_doi_target_type == '1'}}
            <div class="color-gray999"><a href="${ctx }/app/personal/{{if operator}}{{>operator.usr_ent_id}}{{else}}{{>user.usr_ent_id}}{{/if}}" class="wzb-link04"  title="">
            {{if operator}}{{>operator.usr_display_bil}}{{else}}{{>user.usr_display_bil}}{{/if}}
            </a><lb:get key="label_cm.label_core_community_management_107"/> {{>s_doi_create_datetime}} {{>s_doi_act_str}}:</div>
         {{else}}
            <div class="color-gray999">{{if isNormal == true}}<a href="${ctx }/app/personal/{{>user.usr_ent_id}}" class="wzb-link04" title="">{{/if}}<span class="wzb-link04" >{{>user.usr_display_bil}}</span>{{if isNormal == true}}</a>{{/if}}
            {{>s_doi_create_datetime}} {{>s_doi_act_str}}:</div>
         {{/if}}
        
          <p>{{>s_doi_title}}</p>
       
          <p>
                {{if s_doi_target_type != '1'}}
     				{{if  showComment == 'true'}}
                    <a class="wzb-link03 margin-right15 review wzb-sns-comment" uname="{{>user.usr_display_bil}}" uid="{{>user.usr_ent_id}}" href="javascript:void(0);"><i class="fa skin-color mr5 fa-comment"></i><lb:get key="label_cm.label_core_community_management_108"/>(<span>{{>replies.length}}</span>)</a>
 					{{/if}}
                {{/if}}
                {{if s_doi_uid == '${prof.usr_ent_id}' || isNormal == 'false'}}
                    <a class="wzb-link03 wzb-sns-del-doing" data="{{>s_doi_id}}" href="javascript:;"><i class="glyphicon skin-color mr5 glyphicon-remove"></i><lb:get key="global.button_del"/></a>
                {{/if}}
          </p>
     </div>

     <div class="wzb-trend-parcel">
 		{{for replies }}
			{{include tmpl="#replyTemplate" /}}
    	{{/for}}
		
     </div>

</div>
</script>
<script id="replyTemplate" type="text/x-jsrender">
<div class="wzb-reply clearfix" >
    <a class="wzb-reply-pic" href="${ctx}/app/personal/{{>user.usr_ent_id}}"> <img src="{{>user.usr_photo}}"/></a>
    
    <div class="wzb-reply-content">
        <p class="color-gray999">
            {{if user && isNormal}}
                    <a href="${ctx }/app/personal/{{>user.usr_ent_id}}" class="wzb-link04" title="" target="">{{>user.usr_display_bil}}</a>
                {{else}}
                    <span class="wzb-link04">{{>user.usr_display_bil}}</span>
            {{/if}}
            {{if toUser}}
                <span class="grayC999"><lb:get key="label_cm.label_core_community_management_109"/></span>
                {{if isNormal}}
                    <a href="${ctx }/app/personal/{{>toUser.usr_ent_id}}" class="wzb-link04" title="" target="_blank">{{>toUser.usr_display_bil}}</a>
                {{else}}
                    <span class="wzb-link04">{{>toUser.usr_display_bil}}</span>
                {{/if}}
            {{/if}}
			：{{:s_cmt_content}}
        </p>
         <p>
			<span class="margin-right15 color-gray999">{{>s_cmt_create_datetime}}</span>
			
			<a class="margin-right15 grayC666" href="javascript:void(0);"><i uname="{{>user.usr_display_bil}}" uid="{{>s_cmt_uid}}" did="{{>s_cmt_id}}" class="fa color-blue00a fa-comment"></i></a>
			{{if s_cmt_uid == '${prof.usr_ent_id}' || isNormal == 'false'}}
				<a class="grayC666" href="javascript:;"><i data="{{>s_cmt_id}}" class="fa color-blue00a fa-times"></i></a>
			{{/if}}
		 </p>
    </div>
</div>
</script>
<script id="replyFormTemplate" type="text/x-jsrender">
<form class="wbedit" method="post" action="#">
  <textarea class="wzb-textarea-03 align-bottom margin-right10"></textarea>
  <button class="btn wzb-btn-yellow align-bottom" type="button"><lb:get key="global.button_reply"/></button>
</form>
</script>
<script id="memberInfoTemplate" type="text/x-jsrender">
<dl class="wzb-list-7 clearfix" {{if add != true}}style="margin: 10px 0 5px 10px;"{{/if}}>
		<dd style="width:80px;">
			<div class="wzb-user wzb-user68">
				<a {{if isNormal == true}}href="{{>href}}"{{/if}} class="mypic"> <img class="wzb-pic" src="{{>image}}"></a>
				{{if isNormal == true}} 
					<p class="companyInfo" style="display: none;"><lb:get key="label_cm.label_core_community_management_29"/></p>
				 	<div class="cornerTL" style="width: 60px; height: 60px;">&nbsp;</div>
				 	<div class="cornerTR" style="width: 60px; height: 60px;">&nbsp;</div>
				 	<div class="cornerBL" style="width: 60px; height: 60px;">&nbsp;</div>
				 	<div class="cornerBR" style="width: 60px; height: 60px;">&nbsp;</div>
				{{/if}}
			</div>
		</dd>
       <dt>
			<span >
				{{>usr_display_bil}}	
     		</span>
	 		<p>{{>usg_display_bil}}</p>
	 		<p>{{if ugr_display_bil != 'Unspecified'}}{{>ugr_display_bil}}{{/if}}</p>
       </dt>
</dl>
</script>
<script id="checkbox-template" type="text/x-jsrender">
     <input type="checkbox" class="qzsel" onclick="{{>event}}" name='{{>name}}' value="{{>val}}"/>
</script>
<script id="text-template" type="text/x-jsrender">
        {{>text}}
</script>
<script id="text-center-template" type="text/x-jsrender">
    <div class="text-center">{{>text}}</div>
</script>
<!-- 成员 -->
<script id="memberTemplate" type="text/x-jsrender">
<div class="wzb-percent-4 wzb-display-01"> 
  {{include tmpl="#memberInfoTemplate" /}}
  {{if (group_manager_id == meId && group_manager_id != usr_ent_id) || (isManager == 'true' && group_manager_id != usr_ent_id)}}
  <input type="hidden" value="{{>usr_ent_id}}" name="groupuser">
  <input type="button" class="btn wzb-btn-blue wzb-transfer" data="{{>usr_ent_id}}" value="<lb:get key='label_cm.label_core_community_management_110'/>" name="frmSubmitBtn">
  <a class="wzb-delete-01" href="javascript:;" title="" data="{{>usr_ent_id}}" style="margin: 5px 0px 0px 18px;"><i class="fa color-blue00a fa-times"></i></a>
  {{/if}}
</div>
</script>
<!-- add成员 -->
<script id="addMemberTemplate" type="text/x-jsrender">
<div class="wzb-percent-4 wzb-display-01"> 
  {{include tmpl="#memberInfoTemplate" /}}
  <input type="hidden" value="{{>usr_ent_id}}" name="groupuser">
	<span id="user_{{>usr_ent_id}}" class="wzb-user-plus">
  <a class="wzb-link04 wzb-add-group-member" id="add_member_{{>usr_ent_id}}" data="{{>usr_ent_id}}" href="javascript:;" onclick="addUser({{>usr_ent_id}},{{>grp_id}})"> <i class="fa fa-plus"></i> <lb:get key="global.button_add"/></a>
	</span>
</div>
</script>
<script id="add-user-button" type="text/x-jsrender">
	<a class="wzb-link04 wzb-add-group-member" id="add_member_{{>usr_ent_id}}" data="{{>usr_ent_id}}" href="javascript:;" onclick="addUser({{>usr_ent_id}},{{>grp_id}})"> <i class="fa fa-plus"></i> <lb:get key="global.button_add"/></a>
</script>
<script id="cancel-user-button" type="text/x-jsrender">
	<span class="skin-color">
		<i class="fa f14 fbold mr5 fa-check"></i>
		<lb:get key="group_add_ok"/>
	</span> | 
	<a name="clearGroup" id="delete_member_{{>usr_ent_id}}" class="grayC666" href="javascript:;" onclick="cancelUser({{>usr_ent_id}},{{>grp_id}})"><lb:get key="global.button_cancel"/></a>
</script>

</body>
</html>