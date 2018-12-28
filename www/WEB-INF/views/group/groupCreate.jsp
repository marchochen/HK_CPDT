<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<%@ include file="../common/template.jsp"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-learner.css" />
<script type="text/javascript"
	src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<script type="text/javascript" src="${ctx}/static/js/front/course.js"></script>
<script type="text/javascript" src="${ctx}/static/js/cwn_utils.js"></script>
<title></title>
<script type="text/javascript">
	var isNormal = $
	{
		isNormal
	};
	$(function() {
		
		//TODO
		/* if (isNormal != true) {
			$("div#menu,div#header").remove();
		} */
	});
	function changeGroupCard(thisObj, img_name) {
		if ($(thisObj).val() == 2) {
			//$("input[name='image']").attr("disabled", false);
		} else {
			//$("input[name='image']").attr("disabled", true);
			$("input[name='s_grp_card']").val(img_name);
		}
	}

	var inorout  = false;
	function checkName(){
			$.ajax({
				url : '${ctx}/app/group/groupCreate/checkGroupName',
				type : 'POST',
				dataType : 'json',
				  data : {
					  s_grp_title : $("input[name='s_grp_title']").val(),
	                 },
				success : function(data) {
					if(data>=1){
						Dialog.alert(fetchLabel('usr_check_grp_name'));
						inorout = false ;
					}else{
						inorout = true ;
					}
				}
			});
	}
	
	function submitForm() {
		if ($("input[name='s_grp_title']").val() == '') {
			Dialog.alert(fetchLabel('group_name_not_null'));
			inorout = false ;
			return;
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
		
		$.ajax({
			url : '${ctx}/app/group/groupCreate/checkGroupName',
			type : 'POST',
			dataType : 'json',
			data : {
				  s_grp_title : $("input[name='s_grp_title']").val(),
                 },
			success : function(data) {
				if(data>=1){
					Dialog.alert(fetchLabel('usr_check_grp_name'));
					inorout = false ;
				}else{
					inorout = true ;
					$("form").submit();
				}
			}
		});
		
	}
	
</script>
</head>
<body>

	<div class="xyd-wrapper">
		<div id="main" class="xyd-main clearfix">
			<jsp:include page="groupMenu.jsp"></jsp:include>

			<div class="xyd-article">
				<div class="wzb-title-2">
					<lb:get key="group_create" />
				</div>

				<form action="${ctx}/app/group/groupCreate/add/snsGroup"
					method="post" enctype="multipart/form-data">
					<table class="margin-top15">
						<tr>
							<td class="wzb-form-label"><span class="wzb-form-star">*</span><lb:get key="group_name" />：</td>

							<td class="wzb-form-control">
								<div class="wzb-selector">
									<input type="text" class="form-control" name="s_grp_title"
									  onkeydown="return noMaxlength(window.event,this,80);"	value="${s_grp_title}">
								</div>
							</td>
						</tr>

						<tr>
							<td class="wzb-form-label" valign="top"><lb:get
									key="group_desc" />：</td>

							<td class="wzb-form-control">
								<div class="wzb-selector">
									<textarea onkeydown="return noMaxlength(window.event,this,400);" name="s_grp_desc" class="form-control">${s_grp_desc}</textarea>
								</div>
							</td>
						</tr>

						<tr>
							<td class="wzb-form-label" valign="top"><lb:get
									key="group_card" />：</td>

							<td class="wzb-form-control">
								<dl class="wzb-list-8">
									<dd>
										<img src="${ctx}/static/images/qunzu.jpg" id="group_image">
										<%-- <p>
											<label for="psda"><input type="radio" id="psda"
												checked="checked" onclick="changeGroupCard(this,'')"
												value="0" name="group_card">
											<lb:get key="usr_keep_head" /></label>
										</p> --%>
										<p>
											<label for="psdb"><input type="radio" id="psdb"
											    checked="checked" onclick="changeGroupCard(this,'')" value="1"
												name="group_card">
											<lb:get key="usr_default_head" /></label>
										</p>
										<p>
											<label for="psdc"><input type="radio" id="psdc"
												onclick="changeGroupCard(this,'')" value="2"
												name="group_card"><span class=" margin-right15"><lb:get
														key="usr_upload_head" /></span></label>
										</p>
										<div class="file" onclick="document.getElementById('psdc').checked=true;">
											<input id="file" class="file_file" name="image" type="file" onchange="$('#textfield').val(this.value);$('#textfield').attr('title',this.value);" />
											<input id="textfield" class="file_txt"
												value='<lb:get key="no_select_file"/>'/>
											<div class="file_button">
												<lb:get key="usr_browse" />
											</div>
										</div>
										<input type="hidden" name="s_grp_card" value=''>
									</dd>
								</dl>
							</td>
						</tr>

						<tr>
							<td class="wzb-form-label" valign="top"><lb:get
									key="group_privacy" />：</td>

							<td class="wzb-form-control"><c:if
									test="${snsGroup.s_grp_private !='2' }">
									<label class="wzb-input-label" for="psde"><input type="radio" id="psde" class="s_grp_private" checked="checked" value="0" name="s_grp_private"> <lb:get key="label_cm.label_core_community_management_77"/> </label><br/>
									 <label class="wzb-input-label" for="psdf"><input type="radio" id="psdf" class="s_grp_private"  value="2" name="s_grp_private"><lb:get key="label_cm.label_core_community_management_182"/></label><br/>
									<label class="wzb-input-label" for="psdg"><input type="radio" id="psdg" class="s_grp_private"  value="1" name="s_grp_private"><lb:get key="label_cm.label_core_community_management_79"/></label><br/>
								</c:if></td>
						</tr>
						 <tr>
					         <td class="wzb-form-label" valign="top"><span class="wzb-form-star">*</span></td>
					         <td class="wzb-form-control" >
					             	<!-- 为必填 --><lb:get key="label_rm.label_core_requirements_management_35" />
					         </td>
					    </tr>
					</table>

                               <!--旧方式  <c:if
									test="${snsGroup.s_grp_private !='2' }">
									<label class="wzb-input-label" for="psde"><input
										type="radio" id="psde" checked="checked" onclick="" value="0"
										name="s_grp_private">
									<lb:get key="group_public" /></label>
									<wb:hasRole rolExtIds="INSTR_1,ADM_1,TADM_1">
										<label class="wzb-input-label" for="psdh"><input
											type="radio" id="psdh" class="s_grp_private" value="3"
											name="s_grp_private">仅讲师可加入</label>
									</wb:hasRole>
									<label for="psdopenb" class="wzb-input-label"><input
										id="psdopenb" type="radio" name="s_grp_private" value="1">
									<lb:get key="group_not_public" /></label>
								</c:if> <label for="psdopenc" class="wzb-input-label"><input
									id="psdopenc" type="radio" name="s_grp_private" value="2">
								<lb:get key="group_open" /></label>-->

					<div class="wzb-bar">
						<input type="button" onclick="submitForm()"
							class="btn wzb-btn-orange wzb-btn-big margin-right15"
							value='<lb:get key="button_ok"/>' name="frmSubmitBtn" /> <input
							type="button" class="btn wzb-btn-orange wzb-btn-big"
							value='<lb:get key="button_cancel"/>'
							onclick="javascript:window.location.href = '${ctx }/app/group/groupList/0'"
							name="frmSubmitBtn" />
					</div>
				</form>

			</div>
			<!-- xyd-article End-->

		</div>
	</div>
	<!-- xyd-wrapper End-->
</body>
</html>