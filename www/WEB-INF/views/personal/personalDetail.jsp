<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-learner.css" />
<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<script type="text/javascript" src="${ctx}/static/js/front/course.js"></script>
<jsp:include page="../common/meta.datepicker.jsp"></jsp:include>
<title></title>
<script type="text/javascript">
	$(function() {
		if(${isTADM}){
			$("#header").remove();
			$("#menu").remove();
			$("#footer").remove();
			$("#left_menu").remove();
			$("#main").css({width: "738px", "min-width": "738px"});
			$(".col-xs-9").removeClass("col-xs-9").addClass("col-xs-12");
		}
		
		if(${isMeInd}){
			$("option[value='${regUser.usr_gender}']").attr("selected", true);
			$("input[name='birthday']").datepicker();
			$("input[name='join_datetime']").datepicker();
		}
	})

	function changeHead(thisObj, img_name){
		if($(thisObj).val() == 2){
			$("input[name='image']").attr("disabled", false);
		} else {
			//$("input[name='image']").attr("disabled", true);
			$("input[name='userExt.urx_extra_43']").val(img_name);
		}
	}

	function submitPersonal(){
		if($("input[name='usr_display_bil']").val() == ''){
			Dialog.alert(fetchLabel('usr_full_name') +" "+ fetchLabel('usr_is_not_null'));
			return;
		}
		if(getChars($("input[name='usr_display_bil']").val()) > 80){
			Dialog.alert(fetchLabel('usr_full_name') +" "+ fetchLabel('label_title_length_warn_80'));
			return;
		}
		if($("#demo").val() != '')  {
			var birth = new Date($("#demo").val()).getTime();
			var now = new Date().getTime();
			if(birth > now) {
				Dialog.alert(fetchLabel('label_pls_spc_a_valid') +" "+ fetchLabel('usr_birthday'));
				return;
			}
		}
		if($("#demo1").val() != '')  {
			var birth = new Date($("#demo1").val()).getTime();
			var now = new Date().getTime();
			if(birth > now) {
				Dialog.alert(fetchLabel('label_pls_spc_a_valid') +" "+ fetchLabel('usr_join_datetime'));
				return;
			}
		}
		if($("input[name='usr_email']").val() !='' && !/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test($("input[name='usr_email']").val())){
			Dialog.alert(fetchLabel('usr_mail_error'));
			return;
		}
		if(getChars($("input[name='usr_email']").val()) > 80){
			Dialog.alert(fetchLabel('usr_mail') +" "+ fetchLabel('label_title_length_warn_80'));
			return;
		}
		if($("input[name='usr_tel_1']").val() !='' && !/^((\d{11})|((\d{7,8})|(\d{4}|\d{3})-(\d{7,8})|(\d{4}|\d{3})-(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1})|(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1})))$/.test($("input[name='usr_tel_1']").val())){
			Dialog.alert(fetchLabel('usr_tel_error'));
			return;
		}
		if(getChars($("input[name='usr_fax_1']").val()) > 80){
			Dialog.alert(fetchLabel('usr_fax') +" "+ fetchLabel('label_title_length_warn_80'));
			return;
		}
		if(getChars($("input[name='usr_extra_2']").val()) > 80){
			Dialog.alert(fetchLabel('usr_staff_no') +" "+ fetchLabel('label_title_length_warn_80'));
			return;
		}
		if(getChars($("input[name='userExt.urx_extra_42']").val()) > 80){
			Dialog.alert(fetchLabel('usr_weixin') +" "+ fetchLabel('label_title_length_warn_80'));
			return;
		}
		if(getChars($("input[name='usr_job_title']").val()) > 80){
			Dialog.alert(fetchLabel('usr_job_title') +" "+ fetchLabel('label_title_length_warn_80'));
			return;
		}
		if(getChars($("input[name='usr_nickname']").val()) > 80){
			Dialog.alert(fetchLabel('usr_nickname') +" "+ fetchLabel('label_title_length_warn_80'));
			return;
		}
		if(getChars($("textarea[name='userExt.urx_extra_44']").val()) > 400){
			Dialog.alert(fetchLabel('usr_personal_description') +" "+ fetchLabel('label_title_length_warn_400'));
			return;
		}
		if(getChars($("textarea[name='userExt.urx_extra_45']").val()) > 400){
			Dialog.alert(fetchLabel('usr_interest') +" "+ fetchLabel('label_title_length_warn_400'));
			return;
		}
		if($("input[name='head']:checked").val() == 2){
			if($("input[name='image']").val() != ''){
				var file_ext = $("input[name='image']").val().substring($("input[name='image']").val().lastIndexOf(".") + 1);
				if(file_ext != 'jpg' && file_ext != 'gif' && file_ext != 'png' && file_ext != 'jpeg'){
					Dialog.alert(fetchLabel('usr_img_type_limit'));
					return;
				}
			} else {
				Dialog.alert(fetchLabel('usr_select_upload_head'));
				return;
			}
		}
		if ($("input[name='group_card']:checked").val() == 2) {
			if ($("input[name='image']").val() != '') {
				var file_ext = $("input[name='image']").val().substring(
						$("input[name='image']").val().lastIndexOf(".") + 1);
				if (file_ext != 'jpg' && file_ext != 'gif' && file_ext != 'png'
						&& file_ext != 'jpeg') {
					//alert(1);
					inorout = false ;
					Dialog.alert(fetchLabel('usr_img_type_limit'));
					return;
				}else{
					inorout = true ;
				}
			} else {
				Dialog.alert(fetchLabel('usr_select_upload_head'));
				return;
			}
		}else{
			$("input[name='image']").attr("disabled", true);
		}
		
		$("#userDetailForm").submit();
	}

	var u_img = '';
	//选择自定义头像之后  在左边预览
	function previewLocalImage(obj) {
		var img_obj = document.getElementById("extension_43_id");
		var types = ["jpg", "jpeg","gif", "png"];
		var file_type = '0';
		if(obj.files[0] == undefined)
		{
			u_img = '';
		}else{
			file_type = obj.files[0].name.substring(obj.files[0].name.lastIndexOf('.') + 1).toLowerCase();
		}
		var ret = false;
		//alert(obj.files[0].name.substring(obj.files[0].name.lastIndexOf('.') + 1).toLowerCase());
	    for(var i=0; i < types.length; i++) {
			if(file_type == types[i]) {
				ret = true;
			}
		}
		if(ret){
		 u_img = window.URL.createObjectURL(obj.files[0]);
		 img_obj.src = window.URL.createObjectURL(obj.files[0]);
		}
		
	}
	
	function usrImg(src,isU)
	{
		var img_obj = document.getElementById("extension_43_id");
		if(isU)
		{
			if(u_img != ''){
			  img_obj.src = u_img;
			}
		}else{
			img_obj.src = src;
		}
	}
</script>


</head>
<body>
<div class="xyd-wrapper">
	<div id="main" class="xyd-main clearfix">
		<jsp:include page="personalMenu.jsp"></jsp:include>

		<div class="xyd-article">
			<div class="wzb-title-12"><lb:get key="usr_detail"/></div>

			<form id="userDetailForm" action="${ctx}/app/personal/personalDetail/update/userDetail" method="post" enctype="multipart/form-data">
			<table class="margin-top15">
				<tr>
					 <td class="wzb-form-label" valign="top"><lb:get key="usr_profile_picture"/> :</td>

					 <td class="wzb-form-control">
						 <dl class="wzb-list-8">
							 <dd>
								 <img src="${ctx}${regUser.usr_photo}"  id="extension_43_id"/>
								 <c:if test="${isMeInd == 'true'}">
									 <p><label for="psda"><input type="radio" id="psda" value="0" onclick="changeHead(this,'${regUser.userExt.urx_extra_43}');usrImg('${ctx}${regUser.usr_photo}')" checked="checked" name="group_card"><lb:get key="usr_keep_head"/></label></p>
									 <p><label for="psdb"><input type="radio" id="psdb" value="1" onclick="changeHead(this,'');usrImg('/user/user.png')" name="group_card"><lb:get key="usr_default_head"/></label></p>
									 <p><label for="psdc"><input type="radio" id="psdc" value="2" onclick="changeHead(this,'');usrImg('',true)" name="group_card"><lb:get key="usr_upload_head"/></p>
									 <div class="file" onclick="document.getElementById('psdc').checked=true;">
										  <input id="file"  class="file_file" name="image" type="file"  onchange="$('#textfield').html(this.value);$('#textfield').attr('title',this.value);previewLocalImage(this);"/>
										  <div id="textfield" class="file_txt" value='<lb:get key="no_select_file"/>'></div>
										  <div class="file_button"><lb:get key="usr_browse"/></div>
									  </div>
									  <input type="hidden" name="userExt.urx_extra_43" value="${regUser.userExt.urx_extra_43}"/>
								 </c:if>
							 </dd>
						 </dl>
					 </td>
				</tr>

				<tr>
					 <td class="wzb-form-label"><lb:get key="usr_name"/> :</td>

					 <td class="wzb-form-control">
						 ${regUser.usr_ste_usr_id}
					 </td>
				</tr>
				
				<!-- 根据档案设置显示 -->	
				<c:if test="${isMeInd == 'true' or snsSetting == null or (snsSetting.s_set_my_files == null or snsSetting.s_set_my_files == 0 or (snsSetting.s_set_my_files == 1 and snsSetting.snsAttention.s_att_id != null))}">
				
				<tr>
					 <td class="wzb-form-label"><c:if test="${isMeInd == 'true'}"><span class="wzb-form-star">*</span></c:if><lb:get key="usr_full_name"/> :</td>

					 <td class="wzb-form-control">
						 <c:choose>
							 <c:when test="${isMeInd == 'true'}">
								 <input name="usr_display_bil" type="text" class="form-control wzb-text-06" value="${regUser.usr_display_bil}"/>
							 </c:when>
							 <c:otherwise>
								<c:choose>
									<c:when test="${regUser.usr_display_bil != null and regUser.usr_display_bil != ''}">
										${regUser.usr_display_bil}
									</c:when>
									<c:otherwise>
										--
									</c:otherwise>
								</c:choose>
							</c:otherwise>
						 </c:choose>
					 </td>
				</tr>

				<tr>
					 <td class="wzb-form-label"><lb:get key="usr_gender"/>  :</td>

					 <td class="wzb-form-control">
		   				<div class="per_select_border">  
							<div class="per_container">
								 <c:choose>
									 <c:when test="${isMeInd == 'true'}">
										 <select class="form-control wzb-text-per per_select" name="usr_gender" style="width: 105px;"><option value=""><lb:get key="usr_gender_null"/></option><option value="M"><lb:get key="usr_gender_M"/></option><option value="F"><lb:get key="usr_gender_F"/></option></select>
									 </c:when>
									 <c:otherwise>
										 <c:choose>
											 <c:when test="${regUser.usr_gender != null and fn:trim(regUser.usr_gender) != ''}">
												 <lb:get key="usr_gender_${regUser.usr_gender}"/>
											 </c:when>
											 <c:otherwise>
												 <lb:get key="usr_gender_null"/>
											 </c:otherwise>
										 </c:choose>
									 </c:otherwise>
								 </c:choose>
							</div>  
						</div> 
					 </td>
				</tr>

				<tr>
					 <td class="wzb-form-label"><lb:get key="usr_birthday"/> :</td>

					 <td class="wzb-form-control">
						 <c:choose>
							 <c:when test="${isMeInd == 'true'}">
							 <input type="text"  id="demo" class="laydate-icon form-control wzb-text-02" name="birthday" value="<fmt:formatDate value="${regUser.usr_bday}" pattern="yyyy-MM-dd"/>" /> 
							  
							 </c:when>
							 
							 <c:otherwise>
								 <c:choose>
									 <c:when test="${regUser.usr_bday != null and regUser.usr_bday != ''}">
										 <fmt:formatDate value="${regUser.usr_bday}" pattern="yyyy-MM-dd"/>
									 </c:when>
									 <c:otherwise>
										 --
									 </c:otherwise>
								 </c:choose>
							</c:otherwise>
						 </c:choose>
					 </td>

				</tr>

				<c:if test="${isMeInd == 'true'}">
					<tr>
						 <td class="wzb-form-label">&nbsp;</td>
	
						 <td class="wzb-form-control">
							 <span class="wzb-form-star">*</span><lb:get key="usr_required"/>
						 </td>
					</tr>
				</c:if>
			</c:if>
			</table>
			
			<c:if test="${isMeInd == 'true' or snsSetting == null or (snsSetting.s_set_my_files == null or snsSetting.s_set_my_files == 0 or (snsSetting.s_set_my_files == 1 and snsSetting.snsAttention.s_att_id != null))}">

			<hr>

			<div class="wzb-title-12"><lb:get key="usr_contact_information"/></div>

			<table class="margin-top15">
				<tr>
					 <td class="wzb-form-label"><lb:get key="usr_mail"/> :</td>

					 <td class="wzb-form-control">
						 <c:choose>
							<c:when test="${isMeInd == 'true'}">
								<input type="text" class="form-control wzb-text-06" name="usr_email" value="${regUser.usr_email}"/>
							</c:when>
							<c:otherwise>
								<c:choose>
									<c:when test="${regUser.usr_email != null and regUser.usr_email != ''}">
										${regUser.usr_email}
									</c:when>
									<c:otherwise>
										--
									</c:otherwise>
								</c:choose>
							</c:otherwise>
						</c:choose>
					 </td>
				</tr>

				<tr>
					 <td class="wzb-form-label"><lb:get key="usr_tel"/> :</td>

					 <td class="wzb-form-control">
						 <c:choose>
							<c:when test="${isMeInd == 'true'}">
								<input type="text" class="form-control wzb-text-06" name="usr_tel_1" value="${regUser.usr_tel_1}"/>
							</c:when>
							<c:otherwise>
								<c:choose>
									<c:when test="${regUser.usr_tel_1 != null and regUser.usr_tel_1 != ''}">
										${regUser.usr_tel_1}
									</c:when>
									<c:otherwise>
										--
									</c:otherwise>
								</c:choose>
							</c:otherwise>
						</c:choose>
					 </td>
				</tr>

				<tr>
					 <td class="wzb-form-label"><lb:get key="usr_fax"/> :</td>

					 <td class="wzb-form-control">
						 <c:choose>
							<c:when test="${isMeInd == 'true'}">
								<input type="text" class="form-control wzb-text-06" name="usr_fax_1" value="${regUser.usr_fax_1}"/>
							</c:when>
							<c:otherwise>
								<c:choose>
									<c:when test="${regUser.usr_fax_1 != null and regUser.usr_fax_1 != ''}">
										${regUser.usr_fax_1}
									</c:when>
									<c:otherwise>
										--
									</c:otherwise>
								</c:choose>
							</c:otherwise>
						</c:choose>
					 </td>
				</tr>

				<tr>
					 <td class="wzb-form-label"><lb:get key="usr_staff_no"/> :</td>

					 <td class="wzb-form-control">
						 <c:choose>
							<c:when test="${isMeInd == 'true'}">
								<input type="text" class="form-control wzb-text-06" name="usr_extra_2" value="${regUser.usr_extra_2}"/>
							</c:when>
							<c:otherwise>
								<c:choose>
									<c:when test="${regUser.usr_extra_2 != null and regUser.usr_extra_2 != ''}">
										${regUser.usr_extra_2}
									</c:when>
									<c:otherwise>
										--
									</c:otherwise>
								</c:choose>
							</c:otherwise>
						</c:choose>
					 </td>
				</tr>

				<tr>
					 <td class="wzb-form-label"><lb:get key="usr_weixin"/> :</td>

					 <td class="wzb-form-control">
						 <c:choose>
							<c:when test="${isMeInd == 'true'}">
								<input type="text" class="form-control wzb-text-06" name="userExt.urx_extra_42" value="${regUser.userExt.urx_extra_42}"/>
							</c:when>
							<c:otherwise>
								<c:choose>
									<c:when test="${regUser.userExt.urx_extra_42 != null and regUser.userExt.urx_extra_42 != ''}">
										${regUser.userExt.urx_extra_42}
									</c:when>
									<c:otherwise>
										--
									</c:otherwise>
								</c:choose>
							</c:otherwise>
						</c:choose>
					 </td>
				</tr>
			</table>

			<hr>

			<div class="wzb-title-12"><lb:get key="usr_grade_information"/></div>

			<table class="margin-top15">
				<c:choose>
				<c:when test="${mySupervise != null and mySupervise != '[]'}">
				<tr>
					 <td class="wzb-form-label" valign="top"><lb:get key="usr_supervise"/> :</td>

					 <td class="wzb-form-control">
						 <c:forEach items="${mySupervise}" var="list" varStatus="status">
							<c:if test="${!status.first}">,</c:if>
							<a class="color-gray333" href="javascript:;">
								${list.usr_display_bil}
							</a>
						</c:forEach>
					 </td>
				</tr>
				</c:when>
				</c:choose>

				<tr>
					 <td class="wzb-form-label"><lb:get key="usr_job_title"/> :</td>

					 <td class="wzb-form-control">
						 <c:choose>
							<c:when test="${isMeInd == 'true'}">
								<input type="text" class="form-control wzb-text-06" name="usr_job_title" value="${regUser.usr_job_title}"/>
							</c:when>
							<c:otherwise>
								<c:choose>
									<c:when test="${regUser.usr_job_title != null and regUser.usr_job_title != ''}">
										${regUser.usr_job_title}
									</c:when>
									<c:otherwise>
										--
									</c:otherwise>
								</c:choose>
							</c:otherwise>
						</c:choose>
					 </td>
				</tr>

				<tr>
					 <td class="wzb-form-label"><lb:get key="usr_join_datetime"/> :</td>

					 <td class="wzb-form-control">
						 <c:choose>
							<c:when test="${isMeInd == 'true'}">
								<input  id="demo1" type="text" class="form-control wzb-text-02" name="join_datetime" value="<fmt:formatDate value="${regUser.usr_join_datetime}" pattern="yyyy-MM-dd"/>" />
							</c:when>
							<c:otherwise>
								<c:choose>
									<c:when test="${regUser.usr_join_datetime != null and regUser.usr_join_datetime != ''}">
										<fmt:formatDate value="${regUser.usr_join_datetime}" pattern="yyyy-MM-dd"/>
									</c:when>
									<c:otherwise>
										--
									</c:otherwise>
								</c:choose>
							</c:otherwise>
						</c:choose>
					 </td>
				</tr>

				<tr>
					 <td class="wzb-form-label"><lb:get key="usr_grade"/> :</td>

					 <td class="wzb-form-control">
						 ${regUser.ugr_display_bil}
					 </td>
				</tr>

				<tr>
					 <td class="wzb-form-label"><lb:get key="usr_group"/> :</td>

					 <td class="wzb-form-control">
						 ${regUser.usg_display_bil}
					 </td>
				</tr>
			</table>

			<hr>

			<div class="wzb-title-12"><lb:get key="usr_other_information"/></div>

			<table class="margin-top15">
				<tr>
					 <td class="wzb-form-label"><lb:get key="usr_nickname"/> :</td>

					 <td class="wzb-form-control">
						 <c:choose>
							<c:when test="${isMeInd == 'true'}">
								<input type="text" class="form-control wzb-text-06" name="usr_nickname" value="${regUser.usr_nickname}"/>
							</c:when>
							<c:otherwise>
								<c:choose>
									<c:when test="${regUser.usr_nickname != null and regUser.usr_nickname != ''}">
										${regUser.usr_nickname}
									</c:when>
									<c:otherwise>
										--
									</c:otherwise>
								</c:choose>
							</c:otherwise>
						</c:choose>
					 </td>
				</tr>

				<tr>
					 <td class="wzb-form-label" valign="top"><lb:get key="usr_personal_description"/> :</td>

					 <td class="wzb-form-control">
						 <c:choose>
							<c:when test="${isMeInd == 'true'}">
								<textarea type="text" class="form-control wzb-textarea-05" name="userExt.urx_extra_44">${regUser.userExt.urx_extra_44}</textarea>
							</c:when>
							<c:otherwise>
								<c:choose>
									<c:when test="${regUser.userExt.urx_extra_44 != null and regUser.userExt.urx_extra_44 != ''}">
										${regUser.userExt.urx_extra_44}
									</c:when>
									<c:otherwise>
										--
									</c:otherwise>
								</c:choose>
		</div>
							</c:otherwise>
						</c:choose>
					 </td>
				</tr>

				<tr>
					 <td class="wzb-form-label" valign="top"><lb:get key="usr_interest"/> :</td>

					 <td class="wzb-form-control">
						 <c:choose>
							<c:when test="${isMeInd == 'true'}">
								<textarea type="text" class="form-control wzb-textarea-05" name="userExt.urx_extra_45">${regUser.userExt.urx_extra_45}</textarea>
							</c:when>
							<c:otherwise>
								<c:choose>
									<c:when test="${regUser.userExt.urx_extra_45 != null and regUser.userExt.urx_extra_45 != ''}">
										${regUser.userExt.urx_extra_45}
									</c:when>
									<c:otherwise>
										--
									</c:otherwise>
								</c:choose>
							</c:otherwise>
						</c:choose>
					 </td>
				</tr>
			</table>

			<c:if test="${isMeInd == 'true'}">
			<div class="wzb-bar">
				 <input type="button" class="btn wzb-btn-orange wzb-btn-big margin-right15" value='<lb:get key="button_ok"/>' onclick="submitPersonal()" name="frmSubmitBtn">
				<input type="button" class="btn wzb-btn-orange wzb-btn-big" name="pertxt" value='<lb:get key="btn_cancel"/>' onclick="location.href='${ctx}/app/personal/personalDetail/${regUser.usr_ent_id} '"/>
			</div>
			</c:if>
			</c:if>
			</form>

		</div> <!-- xyd-article End-->
	</div>
</div> <!-- xyd-wrapper End-->

<!-- 日期控件 start-->
<script>
!function(){
laydate.skin('molv');//切换皮肤，请查看skins下面皮肤库
laydate({elem: '#demo'});//绑定元素
laydate({elem:'#demo1'})
}();
//日期范围限制
var start = {
    elem: '#start',
    format: 'YYYY-MM-DD',
    min: laydate.now(), //设定最小日期为当前日期
    max: '2099-06-16', //最大日期
    istime: true,
    istoday: false,
    choose: function(datas){
         end.min = datas; //开始日选好后，重置结束日的最小日期
         end.start = datas //将结束日的初始值设定为开始日
    }
};
var end = {
    elem: '#end',
    format: 'YYYY-MM-DD',
    min: laydate.now(),
    max: '2099-06-16',
    istime: true,
    istoday: false,
    choose: function(datas){
        start.max = datas; //结束日选好后，充值开始日的最大日期
    }
};
laydate(start);
laydate(end);
//自定义日期格式
laydate({
    elem: '#test1',
    format: 'YYYY年MM月DD日',
    festival: true, //显示节日
    choose: function(datas){ //选择日期完毕的回调
        alert('得到：'+datas);
    }
});
//日期范围限定在昨天到明天
laydate({
    elem: '#hello3',
    min: laydate.now(-1), //-1代表昨天，-2代表前天，以此类推
    max: laydate.now(+1) //+1代表明天，+2代表后天，以此类推
});

window.onload=function(){
    var aqing=document.getElementById("qing")
    //console.log(aqing)
    var body2 = document.getElementById("by");
    

}

</script>
<!-- 日期控件 end-->

</body>
</html>