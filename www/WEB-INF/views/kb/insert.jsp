<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="../common/meta.kindeditor.jsp"%>
<%@ include file="../common/meta.tree.jsp"%>
<script type="text/javascript" src="${ctx}/static/js/jquery.uploadify/jquery.uploadify.js"></script>
<link rel="stylesheet" type="text/css" href="${ctx }/static/js/jquery.uploadify/uploadify.css" />
<script type="text/javascript" src="${ctx}/js/jquery.uploader.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery.selector.js"></script>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-learner.css" />
<script type="text/javascript">
	sessionid = '${pageContext.session.id}';
	$(function() {
		// 最简单的加载数据模式
		$('#catalog-selector').wzbSelector({
			simple : {
				enable : true,
				init : true,
				filter : true,
				sessionid : sessionid,
				url : '${ctx}/app/tree/catalogListJson'
			},
			message : {
				title : fetchLabel('lab_kb_catalog')
			}
		});

		$.getJSON('${ctx}/app/kb/center/getDefaultImages', function(rData) {
			var str = '';
			$.each(rData, function(index, obj) {
				var p = {
					file : obj.kba_file,
					filename : obj.kba_filename
				}
				str += $('#image-template').render(p);
			})
			$('#defaultImages').html(str);
		})

		// 最简单的加载数据模式
		$('#tag-selector').wzbSelector({
			simple : {
				enable : true,
				init : true,
				filter : true,
				sessionid : sessionid,
				url : '${ctx}/app/tree/tagListJson'
			},
			message : {
				title : fetchLabel('lab_tag_selection')
			}
		});

		$('#image-input').wzbUploader({
			type : 'image',
			sessionid : sessionid,
			fieldname : 'kbi_image',
			multiple : false
		});

		$('#doc-input').wzbUploader({
			type : 'document',
			sessionid : sessionid,
			fieldname : 'kbi_content',
			multiple : false
		});

		$('#image-content-input').wzbUploader({
			type : 'image',
			sessionid : sessionid,
			fieldname : 'kbi_content'
		});

		$('#vedio-input').wzbUploader({
			type : 'vedio',
			sessionid : sessionid,
			fieldname : 'kbi_content',
			multiple : false
		});
		
		$('#wzb-uploader-li-2').click(function(){
			$('input[name=kbi_online]').val('ONLINE');
			$('#video-error-id').addClass('video-error');
		});
		
		$('#wzb-uploader-li-1').click(function(){
			$('input[name=kbi_online]').val('OFFLINE');
			$('#video-error-id').removeClass('video-error');
		});
		<c:if test="${!empty kbItem.kbi_online}">
			if('${kbItem.kbi_online}' == 'ONLINE'){
				$('#wzb-uploader-li-1').removeClass('active');
				$('#kbi_content_tab_div').removeClass('active').removeClass('in');
				$('#wzb-uploader-li-2').addClass('active');
				$('#kbi_content_url_tab_div').addClass('active').addClass('in');
				$('#video-error-id').addClass('video-error');
				<c:if test="${!empty kbItem.kbi_content}">
					$('input[name=kbi_content]').val('${kbItem.kbi_content}');
				</c:if>
			}
		</c:if>

		$('#define')
				.click(
						function() {
							var image = $('#defaultImages .cur img')
									.attr('src');
							var tpl = '';
							if (image) {
								tpl += '<div class="wzb-uploader-prewview wzb-uploader-prewview-image">';
								tpl += '<img src="${ctx}' + image + '"/>';
								tpl += '<a class="wzb-uploader-prewview-remove"><i class="glyphicon glyphicon-remove"></i></a>';
								tpl += '</div>';
								var e = $(tpl);
								e.find('a.wzb-uploader-prewview-remove').hide();
								e.hover(function() {
									e.find('a.wzb-uploader-prewview-remove')
											.show();
								}, function() {
									e.find('a.wzb-uploader-prewview-remove')
											.hide();
								});
								$('i.glyphicon-remove', e).click(function() {
									$(this).parent().parent().remove();
									$('#kbi_default_image').val('');
								});
								$('#image-input .wzb-uploader-file-list').html(
										e);
								$('#kbi_default_image').val(image);
								tb_remove()
							}
						})
	});
	function changeImage(thisObj) {
		$("#" + $(thisObj).parent().attr("id") + " .cur").removeClass("cur");
		$(thisObj).addClass("cur");
	}
</script>
<script id="image-template" type="text/x-jsrender">
		<a href="###" title="" onclick="changeImage(this)">
			<img width="116" height="70" src="${ctx}/imglib/knowledge/{{>file}}" alt="{{>file}}" />
			<div class="thickbox-bg"></div>
		</a>
</script>
<style type="text/css">
.video-error {
	margin-top: 60px;
}

#menuTabs a {
	outline: none;
}

table a {
	color: #000
}

.clickCatalog {
	background-color: gray;
}
</style>
</head>
<body>
	<div class="container bs-docs-container">
		<div class="panel panel-success">
			<ul class="nav nav-tabs" id="menuTabs">
				<c:choose>
					<c:when test="${empty source}">
						<li role="presentation" class="active"><a href="${ctx}/app/kb/admin/index">
								<lb:get key="lab_kb_menu_knowledge" />
							</a></li>
					</c:when>
					<c:otherwise>
						<li role="presentation"><a href="${ctx}/app/kb/admin/index">
								<lb:get key="lab_kb_menu_knowledge" />
							</a></li>
					</c:otherwise>
				</c:choose>
				<li role="presentation"><a href="${ctx}/app/kb/catalog/admin/index">
						<lb:get key="lab_kb_menu_catalog" />
					</a></li>
				<li role="presentation"><a href="${ctx}/app/kb/tag/admin/index">
						<lb:get key="lab_kb_menu_tag" />
					</a></li>
				<c:choose>
					<c:when test="${source == 'approval'}">
						<li role="presentation" class="active"><a href="${ctx}/app/kb/admin/approval">
								<lb:get key="lab_kb_menu_approval" />
							</a></li>
					</c:when>
					<c:otherwise>
						<li role="presentation"><a href="${ctx}/app/kb/admin/approval">
								<lb:get key="lab_kb_menu_approval" />
							</a></li>
					</c:otherwise>
				</c:choose>
			</ul>
			<h1 class="messtit fontfamily" style="margin-left: 30px; margin-top: 10px">
				<lb:get key="lab_kb_menu_knowledge_${type}_${kbItem.kbi_type}" />
			</h1>
			<div class="panel-body">
				<form:form modelAttribute="kbItem" action="${ctx}/app/kb/admin/insert?source=${source}" cssClass="form-horizontal" enctype="multipart/form-data">
					<form:input type="hidden" cssStyle="width: 300px;" cssClass="form-control" path="kbi_id" />
					<form:input type="hidden" cssStyle="width: 300px;" cssClass="form-control" path="kbi_online" />
					<form:input type="hidden" cssStyle="width: 300px;" cssClass="form-control" path="kbi_type" />
					<input type="hidden" name="source" value="${source}" />
					<form:input type="hidden" id="kbi_default_image" cssStyle="width: 300px;" cssClass="form-control" path="kbi_default_image" />
					<div class="form-group">
						<form:label cssClass="col-sm-2 control-label" path="kbi_title">
							<span>*</span>
							<lb:get key="lab_kb_title" />
							<span>：</span>
						</form:label>
						<div class="col-sm-10">
							<form:input cssStyle="width: 300px;" cssClass="form-control" path="kbi_title" />
							<form:errors path="kbi_title" cssClass="error" />
						</div>
					</div>
					<div class="form-group">
						<form:label cssClass="col-sm-2 control-label" path="kbi_desc">
							<span>*</span>
							<lb:get key="lab_kb_desc" />
							<span>：</span>
						</form:label>
						<div class="col-sm-10">
							<form:textarea cssStyle="width: 300px;" cssClass="form-control" path="kbi_desc"></form:textarea>
							<form:errors path="kbi_desc" cssClass="error" />
						</div>
					</div>

					<hr />
					<div class="form-group">
						<link rel="stylesheet" href="${ctx}/static/css/thickbox.css" />
						<script src="${ctx}/static/js/thickbox-compressed.js" type="text/javascript"></script>
						<label class="col-sm-2 control-label" name="kbi_imgeFile"><span>*</span> <lb:get key="lab_kb_image" /> <span>：</span></label>
						<div class="col-sm-10">
							<div class="wzb-uploader" id="image-input">
								<c:if test="${kbItem.imageAttachment != null && kbItem.imageAttachment.kba_id !=null}">
									<ul>
										<li id="${kbItem.imageAttachment.kba_id}" url="${kbItem.imageAttachment.kba_url}" filename="${kbItem.imageAttachment.kba_filename}">${kbItem.imageAttachment.kba_filename}</li>
									</ul>
								</c:if>
							</div>
							<div>
								<span><a href="#TB_inline?height=480&width=600&inlineId=myOnPageContent" class="thickbox"><lb:get key="lab_kb_default_images"/></a></span> <span><form:errors path="kbi_image" cssClass="error thickbox" style="margin-left:30px;" /></span>
							</div>
						</div>
						<div id="myOnPageContent" style="display: none;">
							<div class="thickbox-tit">
								<lb:get key="lab_default_images" />
							</div>

							<div class="thickbox-cont clearfix" id="defaultImages"></div>

							<div class="norm-border">
								<input id="define" type="text" class="norm-btn-1 mr20 skin-bg" name="pertxt" value="<lb:get key='lab_kb_submit'/>" />
								<input type="text" class="norm-btn-1 TB_closeWindowButton skin-bg" name="pertxt" value="<lb:get key='lab_kb_cancel'/>" />
							</div>
						</div>
					</div>
					<div class="form-group">
						<form:label cssClass="col-sm-2 control-label" path="kbi_content">
							<span>*</span>
							<lb:get key="lab_kb_content" />
							<span>：</span>
						</form:label>

						<div class="col-sm-10">
							<c:choose>
								<c:when test="${kbItem.kbi_type=='ARTICLE'}">
									<form:textarea id="editor" cssStyle="width: 300px;" cssClass="form-control" path="kbi_content"></form:textarea>
									<script type="text/javascript">
										var editor;
										KindEditor.ready(function(K) {
											editor = K.create('#editor',
													minKindeditorOptions);
										});
									</script>
								</c:when>
								<c:when test="${kbItem.kbi_type=='DOCUMENT'}">
									<div class="wzb-uploader" id="doc-input">
										<c:if test="${fn:length(kbItem.attachments) > 0}">
											<ul>
												<c:forEach items="${kbItem.attachments}" var="attachment" varStatus="vs">
													<li id="${attachment.kba_id}" url="${attachment.kba_url}" filename="${attachment.kba_filename}">${attachment.kba_filename}</li>
												</c:forEach>
											</ul>
										</c:if>
									</div>
									<span style="color:#999"><lb:get key="lab_kb_Notice" /></span>
								</c:when>
								<c:when test="${kbItem.kbi_type=='VEDIO'}">
									<div class="wzb-uploader" id="vedio-input">
										<c:if test="${fn:length(kbItem.attachments) > 0}">
											<ul>
												<c:forEach items="${kbItem.attachments}" var="attachment" varStatus="vs">
													<li id="${attachment.kba_id}" url="${attachment.kba_url}" filename="${attachment.kba_filename}">${attachment.kba_filename}</li>
												</c:forEach>
											</ul>
										</c:if>
									</div>
									<span style="color:#999"><lb:get key="lab_kb_Notice" /></span>
								</c:when>
								<c:when test="${kbItem.kbi_type=='IMAGE'}">
									<div class="wzb-uploader" id="image-content-input">
										<c:if test="${fn:length(kbItem.attachments) > 0}">
											<ul>
												<c:forEach items="${kbItem.attachments}" var="attachment" varStatus="vs">
													<li id="${attachment.kba_id}" url="${attachment.kba_url}" filename="${attachment.kba_filename}">${attachment.kba_filename}</li>
												</c:forEach>
											</ul>
										</c:if>
									</div>
								</c:when>
							</c:choose>
							<div id="video-error-id">
								<form:errors path="kbi_content" cssClass="error" />
							</div>
						</div>
					</div>
					<c:if test="${kbItem.kbi_type =='DOCUMENT' || kbItem.kbi_type =='VEDIO'}">
						<div class="form-group">
							<form:label cssClass="col-sm-2 control-label" path="kbi_download">
								<span>*</span>
								<lb:get key="lab_kb_DOWNLOAD" />
								<span>：</span>
							</form:label>
							<div class="col-sm-10">
								<form:select cssStyle="width: 120px;" cssClass="form-control" path="kbi_download">
									<form:option value="ALLOW">
										<lb:get key="lab_kb_DOWNLOAD_ALLOW" />
									</form:option>
									<form:option value="INTERDICT">
										<lb:get key="lab_kb_DOWNLOAD_INTERDICT" />
									</form:option>
								</form:select>
								<form:errors path="kbi_download" cssClass="error" />
							</div>
						</div>
					</c:if>
					<div class="form-group">
						<form:label cssClass="col-sm-2 control-label" path="kbi_catalog_ids">
							<span>*</span>
							<lb:get key="lab_kb_know_catalog" />
							<span>：</span>
						</form:label>
						<div class="col-sm-10">
							<form:select id="catalog-selector" cssStyle="width: 120px;" cssClass="form-control" path="kbi_catalog_ids">
								<c:if test="${fn:length(kbItem.catalogues) > 0}">
									<c:forEach items="${kbItem.catalogues}" var="catalogue" varStatus="vs">
										<form:option value="${catalogue.kbc_id}" label="${catalogue.kbc_title}" />
									</c:forEach>
								</c:if>
							</form:select>
							<form:errors path="kbi_catalog_ids" cssClass="error" />
						</div>
					</div>
					<div class="form-group">
						<form:label cssClass="col-sm-2 control-label" path="kbi_tag_ids">
							<lb:get key="lab_kb_know_tag" />
							<span>：</span>
						</form:label>
						<div class="col-sm-10">
							<form:select id="tag-selector" cssStyle="width: 120px;" cssClass="form-control" path="kbi_tag_ids">
								<c:if test="${fn:length(kbItem.tags) > 0}">
									<c:forEach items="${kbItem.tags}" var="tag" varStatus="vs">
										<form:option value="${tag.tag_id}" label="${tag.tag_title}" />
									</c:forEach>
								</c:if>
							</form:select>
							<form:errors path="kbi_tag_ids" cssClass="error" />
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-2 col-sm-10">
							<button type="submit" class="btn btn-default">
								<lb:get key="button_ok" />
							</button>
							<c:choose>
								<c:when test="${source=='approval'}">
									<button type="button" class="btn btn-default navbar-btn" onclick="javascript:go('${ctx}/app/kb/admin/approval');">
										<lb:get key="button_cancel" />
									</button>
								</c:when>
								<c:otherwise>
									<button type="button" class="btn btn-default navbar-btn" onclick="javascript:go('${ctx}/app/kb/admin/index');">
										<lb:get key="button_cancel" />
									</button>
								</c:otherwise>
							</c:choose>
						</div>
					</div>
				</form:form>
				<div class="clear"></div>
			</div>
			<div class="clear"></div>
		</div>
	</div>
</body>
</html>