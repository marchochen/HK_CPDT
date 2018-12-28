<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<%@ include file="../../common/template.jsp"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-admin.css" />
<link rel="stylesheet" href="${ctx }/static/js/jquery.uploadify/uploadify.css"/>
<title></title>
</head>
<body>

<input type="hidden" name="belong_module" value="FTN_AMD_SNS_MGT"/>

 	 <title:get function="global.FTN_AMD_SNS_MGT"/>
<c:set var="titleValue">
    <c:choose>
    <c:when test="${type eq 'FAQ' && id gt 0}">
    	<!--             修改FQA --><lb:get key="label_cm.label_core_community_management_40"/>
    </c:when>
    <c:when test="${type eq 'FAQ'}">
<!--             添加FAQ --><lb:get key="label_cm.label_core_community_management_39"/>
    </c:when>
    <c:when test="${type eq 'UNSOLVED'}">
    	<!--             修改问题 --><lb:get key="label_cm.label_core_community_management_38"/>
    </c:when>
    <c:when test="${type eq 'SOLVED'}">
    	<!--             修改问题 --><lb:get key="label_cm.label_core_community_management_38"/>
    </c:when>     
    <c:otherwise>
<!--             添加问题 --><lb:get key="label_cm.label_core_community_management_41"/>
    </c:otherwise>
    </c:choose>
</c:set>

<c:set var="knowBountyShowed" value='false'>
</c:set>

<ol class="breadcrumb wzb-breadcrumb">
 	<li><a href="javascript:wb_utils_gen_home(true);"><i class="fa wzb-breadcrumb-home fa-home"></i><lb:get key="global.lab_menu_started" /></a></li>
  	<c:choose>
  	    <c:when test="${isView == false }">
  	      <li><a href="javascript:wb_utils_nav_go('FTN_AMD_Q_AND_A_MAIN', '${prof.usr_ent_id }', '${label_lan }')">
		  <lb:get key="global.FTN_AMD_Q_AND_A_MAIN"/><!-- 问答管理 -->
		  </a></li>
  	    </c:when>
  	    <c:otherwise>
  	      <li><a href="javascript:wb_utils_nav_go('FTN_AMD_Q_AND_A_VIEW', '${prof.usr_ent_id }', '${label_lan }')">
		  <lb:get key="global.FTN_AMD_Q_AND_A_VIEW"/><!-- 问答 -->
		  </a></li>
  	    </c:otherwise>
  	</c:choose>
  	
  
  <li class="active">${titleValue }</li>
</ol> <!-- wzb-breadcrumb End-->

<div class="panel wzb-panel">
<div class="panel-heading">${titleValue }</div>

<div class="panel-body">
<form:form modelAttribute="question" action="${ctx }/app/admin/know/addQuestion?isView=${isView }" id="questionForm">
<c:if test="${id gt 0 }">
<input type="hidden" name="que_id" value="${id}">
</c:if>
<table>
    <tr>
         <td class="wzb-form-label" valign="top"><span class="wzb-form-star">*</span>
<!--          标题 -->
              <lb:get key="global.global_title"/>：
         </td>
         
         <td class="wzb-form-control">
             <div class="wzb-selector">
             	<form:input onkeydown="return noMaxlength(event,this,400);" cssClass="form-control" name="que_title" path="que_title" /><form:errors path="que_title" cssClass="error"/>
             </div>
             
             <span class="color-gray999">
<!--             问题说明越详细，回答也会越准确，限140个字以内 -->
                <lb:get key="label_cm.label_core_community_management_42"/>
             </span>
         </td>
    </tr>
    
    <tr>
         <td class="wzb-form-label" valign="top">
<!--          补充说明 -->
              <lb:get key="label_cm.label_core_community_management_43"/>：
         </td>
         
         <td class="wzb-form-control">
             <div class="wzb-selector">
             	<form:textarea onkeydown="return noMaxlength(event,this,2000);"  rows="3" path="que_content" name="que_content" class="form-control wzb-textarea-04"></form:textarea><form:errors path="que_content" cssClass="error"/>
           	</div>
             <div class="color-gray999">
<!--              问题说明越详细，回答也会越准确，限500个字以内 -->
                <lb:get key="label_cm.label_core_community_management_44"/>
             </div>
             <div class="" id="editer" style="width:540px;margin-top:-10px;"></div>
         </td>
    </tr>
    
    <tr>
         <td class="wzb-form-label" valign="top"><span class="wzb-form-star">*</span>
<!--          分类 -->
         <lb:get key="label_cm.label_core_community_management_6"/>：
         </td>
         
         <td class="wzb-form-control">
             <select name="kcaIdOne" onchange="changeCatalog()" id="parent_catalog" class="form-control" multiple="multiple" style="width:130px; height:120px;">

             </select>
             
             <span class="wzb-form-arrow"><i class="fa fa-arrow-right color-blue00a"></i></span>
             
             <select name="kcaIdTwo" class="form-control" id="child_catalog" multiple="multiple" style="width:130px; height:120px;">
             
             </select>
             
             <p class="color-gray999">
<!--              如果您的问题无法归入任何子分类，您可以只选择一级分类  请选择正确的分类，以使您的问题尽快得到解答 -->
                  <lb:get key="label_cm.label_core_community_management_45"/>
             </p>
         </td>
    </tr>    
    <wb:has-any-permission permission="FTN_AMD_Q_AND_A_MAIN">
    <c:choose>
        <c:when test="${type eq 'FAQ'}">
            <input type="hidden" name="que_type" value="FAQ" />
            <tr>
                 <td class="wzb-form-label" valign="top"><span class="wzb-form-star">*</span>
<!--                  答案 -->
                    <lb:get key="label_cm.label_core_community_management_46"/>：
                 </td>
                 
                 <td class="wzb-form-control">
                     <div class="wzb-selector"><form:textarea path="knowAnswer.ans_content" rows="3" class="form-control wzb-textarea-04" id="ansContent"></form:textarea><form:label path="knowAnswer.ans_content" cssClass="error"/></div>
                 </td>
            </tr>
        </c:when>
        <c:when test="${type eq 'SOLVED'}">
          <input type="hidden" name="que_type" value="SOLVED" />
         </c:when>
        <c:otherwise>
            <input type="hidden" name="que_type" value="UNSOLVED" />
            <tr>
                 <td class="wzb-form-label" valign="top"><span class="wzb-form-star"></span>
<!--                  悬赏积分 -->
                    <lb:get key="label_cm.label_core_community_management_47"/>：
                 </td>
                 
                 <td class="wzb-form-control">
                     <div class="wzb-selector"><form:input path="que_bounty" id="queBounty" class="form-control"/><form:label path="que_bounty" cssClass="error"/></div>
                     <span class="color-gray999">
                          <!-- 您当前的积分 -->
                        <lb:get key="label_cm.label_core_community_management_212"/>：${credits}<br/>
<!--                      当选择最佳答案后，会从您的积分中扣除悬赏积分 -->
                        <lb:get key="label_cm.label_core_community_management_48"/>
                     </span>
                 </td>
            </tr>
            <c:choose>
    	<c:when test="${type eq 'FAQ'}">
         </c:when>
          <c:otherwise>
              <tr>
         <td class="wzb-form-label" valign="top">
<!--          邀请讲师回答 -->
            <lb:get key="label_cm.label_core_community_management_49"/>：
         </td>
         
         <td class="wzb-form-control">
             <div class="wzb-choose-box" style="width: 400px;">
                <c:forEach items="${users }" var="user">
                	<div id="send_user_${user.usr_ent_id }" class="wzb-choose-info" value="${user.usr_ent_id }">
						<span class="wzb-choose-detail">${user.usr_display_bil }</span>
						<a class="wzb-choose-area" href="javascript:;" onclick="cancelUser(${user.usr_ent_id },'${user.usr_display_bil }')">
							<i class="fa fa-remove"></i>
						</a>
					</div>
                	<%-- addSendUser(${user.usr_ent_id },'${user.usr_display_bil }') --%>
					<%-- <div id="send_user_${user.usr_ent_id }" class="sendinfo" value="${user.usr_ent_id }">
					</div> --%>
				</c:forEach>
             <form:hidden path="que_ask_ent_ids" ></form:hidden>
             </div>
             <div class="margin-top10">
				<a class="btn wzb-btn-blue thickbox margin-right4" data-toggle="modal"
					data-target="#addInstructorModal"><lb:get key="global.button_search"/></a>
				<a class="btn wzb-btn-blue " 
				href="javascript:;" onclick="clearAll()">
					<lb:get key="label_im.label_core_information_management_29"/>
				</a>
			</div>
            <div style="" class="modal fade" id="addInstructorModal"  tabindex="-1"  role="dialog" aria-labelledby="addInstructorModalLabel" aria-hidden="true">
                    <div class="modal-dialog">
						<div class="modal-content cont">
                    		<div class="modal-header">
                          		<button type="button" class="close" data-dismiss="modal" aria-hidden="true" >&times;</button>
                          		<h4 class="modal-title" id="myModalLabel"><lb:get key="label_cm.label_core_community_management_49"/></h4>
                        	</div>
                                        
                           <div class="modal-body" style="height:552px;overflow-y: auto;">
                                <div class="form-search form-tool">                 
                                     <input type="text" name="search_add_member" class="form-control" value='<lb:get key="attention_find_desc"/>' onfocus="if(value==fetchLabel('attention_find_desc')){value=''}" onblur="if (value ==''){value=fetchLabel('attention_find_desc')}"/><input type="button" class="form-submit" value="" onclick="searchUserList()"/>
                                </div>
	                            <div class="form-tool wzb-title-3" style="font-weight: normal;">                 
	                                  <lb:get key="subordinate_filter_result"/>
	                                  
	                                  <div class="form-tool-right">
	                                       <input type="button" class="btn wzb-btn-yellow" value='<lb:get key="button_add_all"/>' onclick="addAll()"/>
	                                       
	                                       <%-- <wb:hasRole rolExtIds="INSTR_1,ADM_1,TADM_1"> --%>
	                                       <input type="button" value='<lb:get key="group_instr_only"/>' class="btn wzb-btn-yellow margin-left15" id="instrOnly"/>
	                                       <%-- </wb:hasRole> --%>
	                                 </div>
	                           </div>
                                            
								<div class="wzb-percent clearfix " id="wzb-pop-1-main"></div>
                            </div>
                                        
                            <%-- <div class="modal-footer">
								<button type="button" class="btn wzb-btn-blue wzb-btn-big" data-dismiss="modal"><lb:get key="button_close"/></button>
							</div> --%>
                      	</div>
					</div>
                    
                </div>
         </td>
    </tr>
	    </c:otherwise>
	    </c:choose>
            <c:set var="knowBountyShowed" value='true'>
			</c:set>
            
        </c:otherwise>
    </c:choose>
    </wb:has-any-permission>
    
    <c:if test="${!knowBountyShowed}">
     <c:choose>
    	<c:when test="${type eq 'FAQ'}">
        </c:when>
        <c:otherwise>
		    <tr>
		         <td class="wzb-form-label" valign="top"><span class="wzb-form-star"></span>
		         <!--                  悬赏积分 -->
		             <lb:get key="label_cm.label_core_community_management_47"/>：
		         </td>
		         <td class="wzb-form-control">
		             <div class="wzb-selector"><form:input path="que_bounty" id="queBounty" class="form-control"/><form:label path="que_bounty" cssClass="error"/></div>
		             <span class="color-gray999" id="bountyMsg">
		             		<!-- 您当前的积分 -->
	                        <lb:get key="label_cm.label_core_community_management_212"/>：${credits}<br/>
							<!-- 当选择最佳答案后，会从您的积分中扣除悬赏积分 -->
		                    <lb:get key="label_cm.label_core_community_management_48"/>
		             </span>
		         </td>
		    </tr>
	    </c:otherwise>
	   </c:choose>
	 </c:if>
    
</table>
</form:form>
<div class="wzb-bar">
     <input type="submit" id="formSubmit" name="frmSubmitBtn" value='<lb:get key="global.button_ok"/>' class="btn wzb-btn-blue wzb-btn-big margin-right15" >
     <input type="button" name="frmSubmitBtn" value='<lb:get key="global.button_cancel"/>' class="btn wzb-btn-blue wzb-btn-big" onclick="javascript:history.go(-1);">
</div>

</div>
</div>  <!-- wzb-panel End-->
<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery.prompt.js"></script>
<script type="text/javascript" src="${ctx}/static/js/front/course.js"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_cm_${lang}.js"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_${lang}.js"></script>
<script type="text/javascript" src="${ctx}/static/js/jquery.uploadify/jquery.uploadify.js"></script>
<script type="text/javascript" src="${ctx}/static/js/jquery.cwn.js"></script>

<script type="text/javascript" src="${ctx}/static/js/cwn_utils.js"></script>

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
<script type="text/javascript">
var parentIds = '${parentIds}';
if(parentIds != '') {
   parentIds = eval(parentIds);
}
$(function(){
    $.ajax({
        url : '${ctx}/app/know/allKnow/kca/CATALOG/0',
        type : 'POST',
        async : false,
        dataType : 'json',
        success : function(data){
            for(var i=0;i<data.kca.length;i++){
                var html = '<option value="' + data.kca[i].kca_id + '" title="' + data.kca[i].kca_title + '">' + data.kca[i].kca_title + '</option>';
                $("#parent_catalog").append(html);
            }
            if(parentIds.length > 0) {
                $("#parent_catalog").val(parentIds); 
            } else {
                $("#parent_catalog").children().eq(0).attr("selected",true); 
            }
        }
    });
    
	if($("#parent_catalog").children().length > 0){
		if($("#parent_catalog :selected").length > 0){
			$.ajax({
				url : '${ctx}/app/know/allKnow/kca/NORMAL/' + $("#parent_catalog :selected").attr("value"),
				type : 'POST',
				async : false,
				dataType : 'json',
				success : function(data){
					var html = '<option value="0" title="<lb:get key="label_cm.label_core_community_management_50" />"><lb:get key="label_cm.label_core_community_management_50" /></option>';
					for(var i=0;i<data.kca.length;i++){
						html += '<option value="' + data.kca[i].kca_id + '" title="' + data.kca[i].kca_title + '">' + data.kca[i].kca_title + '</option>';
					}
					$("#child_catalog").html(html);
		            if(parentIds && parentIds.length > 0) {
		                $("#child_catalog").val(parentIds); 
		            } else {
		                $("#child_catalog").children().eq(0).attr("selected",true); 
		            }
				}
			});
		}
	}

    $("#formSubmit").click(function(){
        var entIds = [];
        $(".wzb-choose-box .wzb-choose-info").each(function(){
          	 entIds.push($(this).attr("value"));
          	})
        $("input[name=que_ask_ent_ids]").val(entIds.join(','));
    	if($("input[name=que_title]").val().length<=0){
    		Dialog.alert(fetchLabel("label_core_community_management_187"));
    		return;
    	}
    	if("${type}"=="FAQ"){
    		if($("#ansContent").val().length<=0){
    		Dialog.alert(fetchLabel("label_core_community_management_188"));
    		return;
    		}
    	}
    	if(getChars($("input[name=que_title]").val()) > 400){
    		Dialog.alert(fetchLabel("label_core_community_management_210"));
    		return;
    	}
    	if(getChars($("textarea[name=que_content]").val()) > 2000){
    		Dialog.alert(fetchLabel("label_core_community_management_208"));
    		return;
    	}
    	if(getChars($("textarea[id=ansContent]").val()) > 20000){
    		Dialog.alert(fetchLabel("label_core_community_management_220"));
    		return;
    	}
    	var obj=creditsConfirm();
		if(!obj.flag && "${type}"!="FAQ"){
			$('#queBounty').focus();
			Dialog.alert(obj.msg);
			return;
		}
		if($("#parent_catalog :selected").attr("value") == undefined){
			Dialog.alert(fetchLabel('know_select_catalog_error'));
			return;
		}
   		$("#questionForm").submit();
		   
    })

    $("#instrDel").click(function(){
        $("select[name=ask_ent_ids] option:selected").remove();
    })
    
    $("#addInstructorModal").on('show.bs.modal', function (e) {
    	addUser();
    });
    
	//只显示讲师
    $("#instrOnly").live('click', function(){
		if($(this).hasClass("instrOnly")) {
			$(this).removeClass("instrOnly");
			addUser('', false)
			$(this).val(fetchLabel('group_show_instr_only'));
		}else {
			addUser('', true)
			$(this).addClass("instrOnly");
			$(this).val(fetchLabel('group_all_users'));
		}
    });
    
})

	function addUser(searchContent,instrOnly){
		$(".modal-dialog #wzb-pop-1-main").html('');
        $(".modal-dialog").find("#wzb-pop-1-main").empty().table({
        	url : contextPath + '/app/user/getInstructors',
        	params : {
				searchContent : searchContent,
				instrOnly : instrOnly
			},
    		gridTemplate : function(data){
    			p = {
    				image : contextPath + data.usr_photo,
    				usr_display_bil : data.usr_display_bil,
    				usg_display_bil : data.usg_display_bil,
    				ugr_display_bil : data.ugr_display_bil,
    				href : contextPath + '/app/personal/' + data.usr_ent_id,
    				usr_ent_id : data.usr_ent_id,
    				add : true,
    				iti_level : fetchLabel("know_teacher_"+data.iti_level),
    				iti_score : data.iti_score
    			}
    			return $('#instructorTemplate').render(p);
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
	
	//选择所有用户
	function addAll(){
		$("a[name='addUser']").click();
	}
	//搜索用户
	function searchUserList(){
		searchContent = $("input[name='search_add_member']").val();
		if(fetchLabel('attention_find_desc') == searchContent){
			searchContent =  "";
		}
		addUser(searchContent);
	}

	//添加选中的讲师
	function addSendUser(usr_ent_id,usr_display_bil){
		var p = {
				usr_ent_id : usr_ent_id,
				usr_display_bil : usr_display_bil
		}
		$("#user_" + usr_ent_id).html($("#cancel-user-button").render(p));
		
		$(".wzb-choose-box").html($(".wzb-choose-box").html() + $("#detele-user-button").render(p));
	}
	//取消已选中的讲师
	function cancelUser(usr_ent_id, usr_display_bil){
		var p = {
			usr_ent_id : usr_ent_id,
			usr_display_bil : usr_display_bil
		}
		$("#user_" + usr_ent_id).html($("#add-user-button").render(p));
		
		$("#send_user_" + usr_ent_id).remove();
	}
	
	function clearAll(){
		$("a[name='clearAll()']").click();
		$(".wzb-choose-box").text('');
	}

function getInstructorLevel(level){
    if(level == 'J') {
        return cwn.getLabel('label_core_community_management_155');
    } else if(level == 'M') {
        return cwn.getLabel('label_core_community_management_156');
    } else if(level == 'S') {
        return cwn.getLabel('label_core_community_management_157');
    } else if(level == 'D') {
        return cwn.getLabel('label_core_community_management_158');
    } 
}
//切换分类
function changeCatalog(){
    $.ajax({
        url : '${ctx}/app/know/allKnow/kca/NORMAL/' + $("#parent_catalog :selected").attr("value"),
        type : 'POST',
        dataType : 'json',
        success : function(data){
            var html = '<option value="0">' + fetchLabel("label_core_community_management_50") + '</option>';
            for(var i=0;i<data.kca.length;i++){
                html += '<option value="' + data.kca[i].kca_id + '">' + data.kca[i].kca_title + '</option>';
            }
            $("#child_catalog").html(html);
            $("#child_catalog").children().eq(0).attr("selected",true); 
        }
    });
}

/* ==================================================================================== */
//添加图片选择
$("#editer").cwnEditer({
       sessionid : '${pageContext.session.id}' ,
       btnTitle : cwn.getLabel("label_core_community_management_113"),
       fileInitUrl : contextPath +  '/app/upload/KnowQuestion/uncommit',
       fileDelUrl : contextPath +  '/app/upload/del/',
       uploadBtns : [
           {id:'uploadImg', type : 'image', popup : true, name:'<i class="fa fa-file-image-o"></i>&nbsp;' + cwn.getLabel('label_core_community_management_115'), uploadUrl : contextPath + '/app/upload/handle?module=KnowQuestion&type=Img', onlineUrl : contextPath + '/app/upload/online?module=KnowQuestion&type=Img'}
       ],
       module : 'KnowQuestion',
       onlyImg : true
   });
/* ==================================================================================== */

function creditsConfirm(){
		var bounty=$('#queBounty').val();
		if(bounty == null || bounty == undefined || bounty == ''){
			bounty = 0;
			$('#queBounty').attr("value",'0');
		}
		var reg=/^[0-9]*[1-9][0-9]*$/;
		var returnObj={
				flag:true,
		        msg:'<lb:get key="know_inputBouty_tip1"/>'
		};
		if(bounty!=''&&bounty!='0')	{			
		    if(reg.test(bounty)){		    	
		    	$('#bountyMsg span').text(returnObj.msg);
		    	$('#bountyMsg span').css({'color':'black'});
		    	if(bounty>${credits}){
		    		returnObj.msg='<lb:get key="know_inputBouty_tip2"/>';
		    		$('#bountyMsg span').text(returnObj.msg);
			    	$('#bountyMsg span').css({'color':'red'});
			    	returnObj.flag=false;
		    	}
		    }else{
		    	returnObj.msg='<lb:get key="know_inputBouty_tip5"/>';
		    	$('#bountyMsg span').text(returnObj.msg);
		    	$('#bountyMsg span').css({'color':'red'});
		    	returnObj.flag=false;
		    }
		}
		return returnObj;
	}

</script>
<script type="text/x-jsrender" id="instructorTemplate">
<div class="wzb-percent-4 wzb-display-01">
<div><!-- 固定高度，对齐-->
   <dl class="wzb-list-7 clearfix">
       <dd>
			<div class="wzb-user wzb-user68">
				<a {{if isNormal == true}}href="{{>href}}"{{/if}} class="mypic"> <img src="{{>image}}" class="wzb-pic"></a>
				{{if isNormal == true}} 
					<p class="companyInfo" style="display: none;"><lb:get key="label_cm.label_core_community_management_29_ta"/></p>
				 	<div class="cornerTL" style="width: 60px; height: 60px;">&nbsp;</div>
				 	<div class="cornerTR" style="width: 60px; height: 60px;">&nbsp;</div>
				 	<div class="cornerBL" style="width: 60px; height: 60px;">&nbsp;</div>
				 	<div class="cornerBR" style="width: 60px; height: 60px;">&nbsp;</div>
				{{/if}}
			</div>
       </dd>
      
       <dt>
            <a class="wzb-link04" href="#" title="">{{>usr_display_bil}}</a>
			<p>{{>usg_display_bil}}</p>
			<p>{{if ugr_display_bil != 'Unspecified'}}{{>ugr_display_bil}}{{/if}}</p>
       </dt>
  </dl>
</div>
<span id="user_{{>usr_ent_id}}" class="wzb-user-plus">
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
</body>
</html>