<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<script type="text/javascript" src="${ctx}/static/js/login_label.js"></script>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-learner.css" />
<title></title>
<script type="text/javascript">
	$(function(){
		$("#msg_title").html(labels['${lang}']['login_find_password']);
		$("#msg_top1").html(labels['${lang}']['login_email_find']);
		$("#msg_top2").html(labels['${lang}']['login_find_pwd_desc']);
		$("#msg_tip1").html(labels['${lang}']['login_pwd_email_send']);
		$("#msg_tip2").html(labels['${lang}']['login_update_pwd_ok']);
		$("#msg_ok").html(labels['${lang}']['button_ok']);
	})
</script>
</head>
<body>
	<div class="xyd-wrapper">
		<div class="xyd-whole">
			<div class="xyd-whole-title skin-bg">
				<c:choose>
					<c:when test="${type == 'register'}">
						<lb:get key="login_new_user_register" />
					</c:when>
					<c:otherwise>
						<span id="msg_title"></span>
					</c:otherwise>
				</c:choose>
				<i class="fa fa-sanzuo fa-caret-up"></i>
			</div>

			<div class="xyd-whole-main">
				<div class="xyd-whole-top">
					<span class="pull-left"><strong
						class="margin-right15 color-gray333"><span id="msg_top1"></span></strong> <span id="msg_top2"></span></span> 
				</div>
				<!-- xyd-whole-top End-->

				<div class="logcot">
					<c:choose>
						<c:when test="${type == 'register'}">
						<span class="fontfamily">
							<lb:get key="login_register_submited" /></span>
							<p class="mt15 mb20">
								<lb:get key="login_waiting_approval" />
							</p>
						</c:when>
						<c:otherwise>
							<span style="line-height: 28px;"> <c:choose>
									<c:when test="${type == 'find'}">
										<span id="msg_tip1"></span>
									</c:when>
									<c:otherwise>
										<span id="msg_tip2"></span>
									</c:otherwise>
								</c:choose>
							</span>
							<p class="margin-top15 margin-bottom20"></p>
						</c:otherwise>
					</c:choose>

					<a class="btn wzb-btn-orange" style="width:69px;height:35px;" href="${ctx}/app/user/userLogin/$"><span id="msg_ok"></span></a>
				</div>

			</div>
			<!-- xyd-whole-main End-->

		</div>
	</div>
	<!-- xyd-wrap End-->
</body>
</html>