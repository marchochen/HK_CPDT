<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<script type="text/javascript">
	$(function() {
		$("#${command}").addClass("cur");
	})

	function addTaAttention(s_att_target_uid, attention_id){
		$.ajax({
			url : '${ctx}/app/personal/addAttention/' + s_att_target_uid,
			type : 'POST',
			success : function(){
				var attention_status = fetchLabel('attention_now');
				var attention_class = 'fa-check';
				if(attention_id > 0){
					attention_status = fetchLabel('attention_mutual');
					attention_class = 'fa-exchange';
				}
				var html = '<span class="skin-color"><i class="fa f14 fbold ' + attention_class + '"></i>' + attention_status
						+ '</span>|<a class="grayC666" href="javascript:;" title="' + attention_status + '" onclick="cancelTaAttention('
						+ s_att_target_uid + ',' + attention_id +')">' + fetchLabel('button_cancel') + '</a>';
				$("#attention_" + s_att_target_uid).html(html);
			}
		});
	}

	function cancelTaAttention(s_att_target_uid, attention_id){
		$.ajax({
			url : '${ctx}/app/personal/cancelAttention/' + s_att_target_uid,
			type : 'POST',
			success : function(){
				var html = '<a class="grayC333" href="javascript:;" title="' + fetchLabel('personal_attention') + '" onclick="addTaAttention(' + s_att_target_uid
						+ ',' + attention_id + ')"><i class="fa f14 grayC999 mr5 fa-plus"></i>' + fetchLabel('personal_attention') + '</a>';
				$("#attention_" + s_att_target_uid).html(html);
			}
		});
	}

	$(function(){
		//站内信
		$("#personalSendMesg").qtip({
			id : 'uploadMesgBoxQitp',
		    content: {
		        text: $("#uploadMesgBox")
		    },
		    position: {
		        at: 'center center',
		        my: 'left center',
		        adjust: {
		            x: 40
		        }
		    },
		    show: {
		        event: 'click'
		    },
		    hide: 'unfocus',
		    style: {
		        classes: 'qtip-bootstrap',
		        width: 480
		    },
		});
		$('#clear').click(function(){
			$('#wmsg_content').val('');
			$('div[role="alert"]:visible').hide();
		});
		//提交
		$("#formSendSubmit").click(function(){
			var note = $("#wmsg_content").val();
		 	if(note=="" || note.trim()==""){
				$("#error").show();
				return;
			}

			$.ajax({
				url : '${ctx}/app/subordinate/sendWebMessage/' + ${regUser.usr_ent_id},
				data : {
					wmsg_subject : '${lang}'=='en-us'? fetchLabel('message_Pri_Frm') + '${prof.usr_display_bil}' : fetchLabel('lab_kb_from')+ '${prof.usr_display_bil}' + fetchLabel('message_suffix'),
					wmsg_content : note
				},
				type : 'POST',
				success : function() {
					$('#qtip-uploadMesgBoxQitp').hide();
					Dialog.alert(fetchLabel('subordinate_send_ok'));
				//	window.location.href = "${ctx}/app/personal/personalDoing/" + ${regUser.usr_ent_id} ;
				}
			});
		});
	 })

	 function changehide(){
		$("#error").hide();
	}

	function addClass(){
		$("#${command}").removeClass("cur");
		$("#personalSendMesg").addClass("cur");
	}
</script>

<div id="uploadMesgBox" style="display:none;width:300;">
	<div class="send clearfix" >
		<form method="post" action="#">
			<div style="padding-bottom:8px;">
				<lb:get key="sns_say_something" />
				<a id="clear" href="javascript:;" class="pull-right wzb-link02">X</a>
				<textarea class="form-control margin-top10" onfocus="changehide()" id="wmsg_content"></textarea>
				<input name="wmsg_subject"  type="hidden" class="pertxt"/>
				<div id="error" style="display:none; border:0; color:red"><lb:get key="detail_comment_not_empty" /></div>
			</div>
			<div style="padding-bottom:8px;">
				<input type="button" class="btn wzb-btn-orange" id="formSendSubmit" value='<lb:get key="subordinate_send"/>' />
			</div>
		</form>
	</div>
</div>

<div class="xyd-sidebar">
<div class="xyd-user">
<div class="xyd-user-box clearfix">
<div class="wzb-user wzb-user82">
     <a href="javascript:;" style="cursor:default;"><img class="wzb-pic" src="${regUser.usr_photo}" alt='${regUser.usr_photo}' /></a>
     <%-- <p class="companyInfo">${regUser.usr_photo}</p> --%>
     <!-- <div class="cornerTL">&nbsp;</div>
     <div class="cornerTR">&nbsp;</div>
     <div class="cornerBL">&nbsp;</div>
     <div class="cornerBR">&nbsp;</div> -->
</div>

<div class="xyd-user-content">
     <a href="javascript:;" style="font-size:14px; color:#00aeef; text-decoration:none;cursor:default;" title="${regUser.usr_display_bil}">${regUser.usr_display_bil}</a>
     <p>${regUser.usg_display_bil}</p>
	 <p><c:if test="${regUser.ugr_display_bil != 'Unspecified'}">${regUser.ugr_display_bil}</c:if></p>
	 <p>
	 <c:choose>
		<c:when test="${isMeInd == 'true'}">
			<%-- <a class="wzb-link04 margin-right10" href="${ctx}/app/personal/personalPrivacySet/${encUsrEntId}" title='<lb:get key="usr_privacy_set"/>'><i class="fa font-size18 fa-cog"></i></a> --%>
			
			<a class="wzb-link04" href="${ctx}/app/personal/passwordModify" title='<lb:get key="usr_change_psd"/>'><i class="fa font-size18 fa-lock"></i></a>
		</c:when>
		<c:when test="${sns_enabled == true}">
			<span id="attention_${usrEntId}">
				<c:choose>
					<c:when test="${regUser.snsAttention.s_att_source_uid > 0}">
						<c:choose>
							<c:when test="${regUser.snsAttention.s_att_target_uid == 0}">
								<span class="skin-color"><i class="fa font-size14 fa-check"></i><lb:get key="attention_now"/></span>|<a class="color-gray666" href="javascript:;" title='<lb:get key="button_cancel"/>' onclick="cancelTaAttention(${usrEntId}, ${regUser.snsAttention.s_att_target_uid})"><lb:get key="button_cancel"/></a>
							</c:when>
							<c:when test="${regUser.snsAttention.s_att_target_uid > 0}">
								<span class="skin-color"><i class="fa font-size14 fa-exchange"></i><lb:get key="attention_mutual"/></span>|<a class="color-gray666" href="javascript:;" title='<lb:get key="button_cancel"/>' onclick="cancelTaAttention(${usrEntId}, ${regUser.snsAttention.s_att_target_uid})"><lb:get key="button_cancel"/></a>
							</c:when>
						</c:choose>
					</c:when>
					<c:otherwise>
						<a class="color-gray333" href="javascript:;" title='<lb:get key="personal_attention"/>' onclick="addTaAttention(${usrEntId}, ${regUser.snsAttention.s_att_target_uid})"><i class="fa font-size14 color-gray999 fa-plus"></i><lb:get key="personal_attention"/></a>
					</c:otherwise>
				</c:choose>
			</span>
		</c:when>
	</c:choose>
    </p>
</div>
</div> <!-- usermess End-->

<p class="color-gray666">
<c:choose>
    <c:when test="${isMeInd == 'true'}">
        <lb:get key="personal_my_credit"/>：<span class="skin-color"><a href="${ctx}/app/personal/personalCredits/${encUsrEntId}"  class="skin-color">${total_credits}</a></span>
        <P><lb:get key="course_credit"/>：<span class="">${learn_credits}</span></P>
    </c:when>
    <c:otherwise>
        <lb:get key="personal_ta_credit"/>：<span class="skin-color"><a class="skin-color">${total_credits}</a></span>
    </c:otherwise>
</c:choose>
</p>
</div>

<%-- <div class="wzb-model-7">
<c:if test="${sns_enabled == true}">
<div class="wzb-percent-biao">
     <div class="wzb-percent-3" style="height:75px;">
          <c:choose>
              <c:when test="${isMeInd == 'true' or snsSetting == null or (snsSetting.s_set_like == null or snsSetting.s_set_like == 0 or (snsSetting.s_set_like == 1 and snsSetting.snsAttention.s_att_id != null))}">
                  <a class="wzb-percent-link" href="${ctx}/app/personal/personalValuation/${encUsrEntId}">
                     <em>${likes}</em>
                     <span><lb:get key="rank_be_praise"/></span>
                  </a>
              </c:when>
              <c:otherwise>
                   <em>${likes}</em>
                   <span><lb:get key="rank_be_praise"/></span>
              </c:otherwise>
          </c:choose>
     </div>

     <div class="wzb-percent-3" style="height:75px;">
          <c:choose>
              <c:when test="${isMeInd == 'true' or snsSetting == null or (snsSetting.s_set_my_follow == null or snsSetting.s_set_my_follow == 0 or (snsSetting.s_set_my_follow == 1 and snsSetting.snsAttention.s_att_id != null))}">
                  <a class="wzb-percent-link" href="${ctx}/app/personal/personalUserList/attention/${encUsrEntId}">
                     <em>${attent}</em>
                     <span><lb:get key="personal_attention"/></span>
                  </a>
              </c:when>
              <c:otherwise>
                   <em>${attent}</em>
                   <span><lb:get key="personal_attention"/></span>
              </c:otherwise>
          </c:choose>
     </div>

     <div class="wzb-percent-3" style="height:75px;">
          <c:if test="${sns_enabled == true}">
                <c:choose>
                    <c:when test="${isMeInd == 'true' or snsSetting == null or (snsSetting.s_set_my_fans == null or snsSetting.s_set_my_fans == 0 or (snsSetting.s_set_my_fans == 1 and snsSetting.snsAttention.s_att_id != null))}">
                        <a class="wzb-percent-link" href="${ctx}/app/personal/personalUserList/fans/${encUsrEntId}">
                            <em>${fans}</em>
                            <span><lb:get key="personal_fans"/></span>
                        </a>
                    </c:when>
                    <c:otherwise>
                        <em>${fans}</em>
                        <span><lb:get key="personal_fans"/></span>
                    </c:otherwise>
                </c:choose>
          </c:if>
     </div>
</div>
</c:if>
</div> --%>

<ul class="wzb-list-15">
    <c:choose>
        <c:when test="${isMeInd == 'true'}">
       
        <li><a id="personalDetail" href="${ctx}/app/personal/personalDetail/${encUsrEntId}"><i class="fa fa-graduation-cap"></i>&nbsp;<lb:get key="personal_my_files"/></a></li>
         <li><a id="personalCredits" href="${ctx}/app/personal/personalCredits/${encUsrEntId}"><i class="fa fa-bar-chart-o"></i>&nbsp;<lb:get key="personal_my_credit"/></a></li>
        <li><a id="personalCollect" href="${ctx}/app/personal/personalCollect/${encUsrEntId}"><i class="fa fa-star"></i>&nbsp;<lb:get key="personal_my_collection"/></a></li>
        <c:if test="${sns_enabled == true}">
<%--             <li><a id="personalGroup" href="${ctx}/app/personal/personalGroup/${encUsrEntId}"><i class="fa fa-users"></i> <lb:get key="personal_my_group"/></a></li> --%>   
           <%-- <li><a id="personalValuation" href="${ctx}/app/personal/personalValuation/${encUsrEntId}"><i class="fa fa-thumbs-o-up"></i> <lb:get key="personal_ta_praise"/></a></li>
            <li><a id="attention" href="${ctx}/app/personal/personalUserList/attention/${encUsrEntId}"><i class="fa fa-eye"></i>&nbsp;<lb:get key="personal_my_attention"/></a></li>
            <li><a id="fans" href="${ctx}/app/personal/personalUserList/fans/${encUsrEntId}"><i class="fa fa-male"></i>&nbsp;<lb:get key="personal_my_fans"/></a></li> --%>
        </c:if>
        <li><a id="personalCertificate" href="${ctx}/app/personal/personalCertificate/${encUsrEntId}"><i class="fa fa-newspaper-o"></i>&nbsp;<lb:get key="menu_certificate"/></a></li>
        <li><a id="voting" href="${ctx}/app/voting" title="<lb:get key='label_rm.label_core_requirements_management_2'/>"><i class="fa fa-building-o"></i>&nbsp;<lb:get key='label_rm.label_core_requirements_management_2'/></a></li>
        <li><a id="personalEvaluation" href="${ctx}/app/personal/personalEvaluation/${encUsrEntId}"><i class="fa fa-pencil-square-o"></i>&nbsp;<lb:get key="personal_evaluation"/></a></li>
        <li><a id="personalHistory" href="${ctx}/app/personal/personalHistory/${encUsrEntId}"><i class="fa fa-delicious"></i>&nbsp;<lb:get key="personal_my_learning_record"/></a></li>
        <li><a id="learningSituation" href="${ctx}/app/personal/learningSituation/${encUsrEntId}"><i class="fa fa-codepen"></i>&nbsp;<lb:get key="personal_my_learning_situation"/></a></li>
        <c:if test="${cpd_enable == true}">
          <li>
          	<a id="cpdIndividualReport" href="${ctx}/app/cpdIndividualReport/personalIndividualReport/${encUsrEntId}">
          		<i class="fa fa-compass"></i><lb:get key="label_core_cpt_d_management_280"/>
          	</a>
          </li>
        </c:if>
        </c:when>
        <c:otherwise>
        <li><a id="personalSendMesg" href="javascript:;" onclick="addClass()"><lb:get key="personal_ta_sendmesg"/></a></li>
        <c:if test="${snsSetting == null or (snsSetting.s_set_my_files == null or snsSetting.s_set_my_files == 0 or (snsSetting.s_set_my_files == 1 and snsSetting.snsAttention.s_att_id != null))}">
            <li><a id="personalDetail" href="${ctx}/app/personal/personalDetail/${encUsrEntId}"><i class="fa fa-graduation-cap"></i>&nbsp;<lb:get key="personal_ta_files"/></a></li>
        </c:if>
         <c:if test="${snsSetting == null or (snsSetting.s_set_my_credit == null or snsSetting.s_set_my_credit == 0 or (snsSetting.s_set_my_credit == 1 and snsSetting.snsAttention.s_att_id != null))}">
            <li><a id="personalCredits" href="${ctx}/app/personal/personalCredits/${encUsrEntId}"><i class="fa fa-bar-chart-o"></i>&nbsp;<lb:get key="personal_ta_credit"/></a></li>
        </c:if>
        <c:if test="${snsSetting == null or (snsSetting.s_set_my_collection == null or snsSetting.s_set_my_collection == 0 or (snsSetting.s_set_my_collection == 1 and snsSetting.snsAttention.s_att_id != null))}">
            <li><a id="personalCollect" href="${ctx}/app/personal/personalCollect/${encUsrEntId}"><i class="fa fa-star"></i>&nbsp;<lb:get key="personal_ta_collection"/></a></li>
        </c:if>
        <c:if test="${sns_enabled == true and (snsSetting == null or (snsSetting.s_set_group == null or snsSetting.s_set_group == 0 or (snsSetting.s_set_group == 1 and snsSetting.snsAttention.s_att_id != null)))}">
            <li><a id="personalGroup" href="${ctx}/app/personal/personalGroup/${encUsrEntId}"><i class="fa fa-users"></i> <lb:get key="personal_ta_group"/></a></li>
        </c:if>
        <%-- <c:if test="${sns_enabled == true and (snsSetting == null or (snsSetting.s_set_like == null or snsSetting.s_set_like == 0 or (snsSetting.s_set_like == 1 and snsSetting.snsAttention.s_att_id != null)))}">
            <li><a id="personalValuation" href="${ctx}/app/personal/personalValuation/${encUsrEntId}"><i class="fa fa-thumbs-o-up"></i> <lb:get key="personal_ta_praise"/></a></li>
        </c:if>
        <c:if test="${sns_enabled == true and (snsSetting == null or (snsSetting.s_set_my_follow == null or snsSetting.s_set_my_follow == 0 or (snsSetting.s_set_my_follow == 1 and snsSetting.snsAttention.s_att_id != null)))}">
            <li><a id="attention" href="${ctx}/app/personal/personalUserList/attention/${encUsrEntId}"><i class="fa fa-eye"></i>&nbsp;<lb:get key="personal_ta_attention"/></a></li>
        </c:if>
        <c:if test="${sns_enabled == true and (snsSetting == null or (snsSetting.s_set_my_fans == null or snsSetting.s_set_my_fans == 0 or (snsSetting.s_set_my_fans == 1 and snsSetting.snsAttention.s_att_id != null)))}">
            <li><a id="fans" href="${ctx}/app/personal/personalUserList/fans/${encUsrEntId}"><i class="fa fa-male"></i>&nbsp;<lb:get key="personal_ta_fans"/></a></li>
        </c:if> --%>
       <%--  <c:if test="${sns_enabled == true and (snsSetting == null or (snsSetting.s_set_voting == null or snsSetting.s_set_voting == 0 or (snsSetting.s_set_voting == 1 and snsSetting.snsAttention.s_att_id != null)))}">
            <li><a id="voting" href="${ctx}/app/voting/${usrEntId}" title="<lb:get key='label_rm.label_core_requirements_management_2'/>"><i class="fa fa-building-o"></i><lb:get key='label_rm.label_core_requirements_management_2'/></a></li>
        </c:if> --%>
        <c:if test="${snsSetting == null or ((snsSetting.spt_source_usr_ent_id != null && snsSetting.spt_source_usr_ent_id > 0) or snsSetting.s_set_my_learning_record == null or snsSetting.s_set_my_learning_record == 0 or (snsSetting.s_set_my_learning_record == 1 and snsSetting.snsAttention.s_att_id != null))}">
            <li><a id="personalHistory" href="${ctx}/app/personal/personalHistory/${encUsrEntId}"><i class="fa fa-delicious"></i>&nbsp;<lb:get key="personal_ta_learning_record"/></a></li>
        </c:if>
        <c:if test="${snsSetting == null or ((snsSetting.spt_source_usr_ent_id != null && snsSetting.spt_source_usr_ent_id > 0) or snsSetting.s_set_my_learning_situation == null or snsSetting.s_set_my_learning_situation == 0 or (snsSetting.s_set_my_learning_situation == 1 and snsSetting.snsAttention.s_att_id != null))}">
            <li><a id="learningSituation" href="${ctx}/app/personal/learningSituation/${encUsrEntId}"><i class="fa fa-codepen"></i>&nbsp;<lb:get key="personal_ta_learning_situation"/></a></li>
        </c:if>
        <c:if test="${cpd_enable == true}">
          <li>
          	<a id="cpdIndividualReport" href="${ctx}/app/cpdIndividualReport/personalIndividualReport/${encUsrEntId}">
          		<i class="fa fa-compass"></i>&nbsp;<lb:get key="label_core_cpt_d_management_280"/>
          	</a>
          </li>
        </c:if>
        </c:otherwise>
        </c:choose>
</ul>
</div> <!-- xyd-sidebar End-->