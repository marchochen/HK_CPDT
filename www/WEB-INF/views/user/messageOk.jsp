<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-learner.css" />
<title></title>
<script type="text/javascript">
	
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
						<lb:get key="login_find_password" />
					</c:otherwise>
				</c:choose>
				<i class="fa fa-sanzuo fa-caret-up"></i>
			</div>

			<div class="xyd-whole-main">
				<div class="xyd-whole-top">
					<span class="pull-left"><strong
						class="margin-right15 color-gray333"><lb:get
								key="login_email_find" /></strong> <lb:get key="login_find_pwd_desc" /></span> 
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
										<lb:get key="login_pwd_email_send" />
									</c:when>
									<c:otherwise>
										<lb:get key="login_update_pwd_ok" />
									</c:otherwise>
								</c:choose>
							</span>
							<p class="margin-top15 margin-bottom20"></p>
						</c:otherwise>
					</c:choose>

					<a class="btn wzb-btn-orange" style="width:69px;height:29px;" href="${ctx}/app/user/userLogin/$?lang=${lang}"><lb:get
							key="button_ok" /></a>
				</div>

			</div>
			<!-- xyd-whole-main End-->

		</div>
	</div>
	<!-- xyd-wrap End-->
</body>
</html>