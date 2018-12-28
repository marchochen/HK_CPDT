<%@ page isELIgnored="false" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-learner.css" />
<script type="text/javascript"
	src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<script type="text/javascript" src="${ctx}/js/urlparam.js"></script>
<!--[if lt IE 9]>
    <script src="../static/js/bootstrap/js/html5shiv.min.js"></script>
    <script src="../static/js/bootstrap/js/respond.min.js"></script>
<![endif]-->
<script type="text/javascript">
	$(function($) {
		$(".path_list").css("width",
				$(".path_biao").width() + $(".path_info").width() + 5);
	});
</script>
</head>
<body>
	<div class="xyd-wrapper">
		<div class="xyd-main clearfix">
			<div class="wzb-model-13">
				<div class="xyd-path">
					<div class="xyd-path-title clearfix">
						<img class="pull-left" src="../static/images/lujingfl.gif" /><span><lb:get
								key="menu_profession" /></span><img class="pull-left"
							src="../static/images/lujingmd.gif" /><em><lb:get
								key="lab_pfs_new" /></em>
					</div>

				<c:if test="${pfs_list!= null || fn:length(pfs_list) > 0}">
					<div class="xyd-path-content">
						<c:forEach items="${pfs_list}" var="pfs" varStatus="status">
							<c:choose>
								<c:when test="${(status.index+1)%3 == 1}">
									<c:set value="xyd-path-list xyd-path-red" var="classname" />
								</c:when>
								<c:when test="${(status.index+1)%3 == 2}">
									<c:set value="xyd-path-list xyd-path-green" var="classname" />
								</c:when>
								<c:when test="${(status.index+1)%3 == 0}">
									<c:set value="xyd-path-list xyd-path-blue" var="classname" />
								</c:when>
							</c:choose>
							<div class="${classname } p ${status.index}">
								<div class="xyd-path-biao c1 ${status.index}">
									<span>${pfs.pfs_title}</span><strong><em></em></strong>
								</div>
								<div class="xyd-path-info c2 ${status.index}">
									<c:forEach items="${pfs.ugr_id_lst }" var="ugr_id"
										varStatus="ugr_status">
										<a class="xyd-path-area"
											href="profession/professionitem?pfs_ugr_id=${ugr_id }&pfs_id=${pfs.pfs_id }"
											title=""> ${pfs.ugr_title_lst[ugr_status.index] } <span
											class="xyd-path-tip"></span>
										</a>
									</c:forEach>
								</div>
							</div>
							
							<script>
								var width = $(".c1.${status.index}").width() + $(".c2.${status.index}").width();
								$(".p.${status.index}").css("width",width);
							</script>
							
						</c:forEach>
					</div>
				</c:if>
				<c:if test="${pfs_list== null || fn:length(pfs_list) == 0}">
					<div class="losedata"><i class="fa fa-folder-open-o"></i><p><lb:get key="lab_table_empty"/></p></div>
				</c:if>
				</div> <!-- xyd-path End-->
			</div>
		</div>
	</div> <!-- xyd-wrap End-->
</body>
</html>
