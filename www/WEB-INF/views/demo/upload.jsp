<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="../common/meta.tree.jsp"%>
<script type="text/javascript" src="${ctx}/static/js/jquery.uploadify/jquery.uploadify.js"></script>
<link rel="stylesheet" type="text/css" href="${ctx }/static/js/jquery.uploadify/uploadify.css" />
<script type="text/javascript" src="${ctx}/js/jquery.uploader.js"></script>
<script type="text/javascript">
	sessionid = '${pageContext.session.id}';

	$(function() {
		$('#file-input').wzbUploader({
			type : 'file',
			sessionid : sessionid,
			fieldname : 'kbfiles'
		});

		$('#image-input').wzbUploader({
			type : 'image',
			sessionid : sessionid,
			fieldname : 'kbimages'
		});

		$('#doc-input').wzbUploader({
			type : 'document',
			sessionid : sessionid,
			fieldname : 'kbdocs'
		});

		$('#vedio-input').wzbUploader({
			type : 'vedio',
			sessionid : sessionid,
			fieldname : 'kbvedio'
		});
	});
</script>
</head>
<body>
	<div class="container">
		<div class="panel panel-default">
			<div class="panel-heading">
				<h3 class="panel-title">文件上传测试</h3>
			</div>
			<div class="panel-body">
				<form class="form-horizontal" role="form">
					<div class="form-group">
						<label for="tc" class="col-lg-3 control-label">名称</label>
						<div class="col-lg-9">
							<div class="wzb-uploader" id="file-input">
								<ul>
									<li id="45" url="/attachment/45/1420683674176.gif" filename="AAA.gif">AAA</li>
									<li id="46" url="/attachment/46/1420683698684.gif" filename="BBB.gif">BBB</li>
									<li id="47" url="/attachment/47/1420684041239.gif" filename="CCC.gif">CCC</li>
								</ul>
							</div>
							<hr />

							<div class="wzb-uploader" id="image-input">
								<ul>
									<li id="45" url="/attachment/45/1420683674176.gif" filename="AAA.gif">AAA</li>
									<li id="46" url="/attachment/46/1420683698684.gif" filename="BBB.gif">BBB</li>
									<li id="47" url="/attachment/47/1420684041239.gif" filename="CCC.gif">CCC</li>
								</ul>
							</div>
							<hr />

							<div class="wzb-uploader" id="doc-input"></div>
							<hr />

							<div class="wzb-uploader" id="vedio-input"></div>
							<hr />
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</body>
</html>