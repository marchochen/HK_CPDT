<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="../common/meta.kindeditor.jsp"%>
<script type="text/javascript" src="${ctx}/static/js/jquery.uploadify/jquery.uploadify.js"></script>
<link rel="stylesheet" type="text/css" href="${ctx }/static/js/jquery.uploadify/uploadify.css" />
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_km_${lang}.js" charset="utf-8"></script>
<script type="text/javascript" src="${ctx}/js/jquery.uploader.js"></script>
<title></title>
<script type="text/javascript">
	var minKindeditorOptions = $.extend({}, kindeditorOptions, {
		width : 500,
		height : 200,
		minWidth : 500,
		afterBlur: function(){this.sync();}
	});
	sessionid = '${pageContext.session.id}';
	$(function() {
		$
				.getJSON(
						'${ctx}/app/kb/catalog/admin/getCatalogJson?kbi_id='+${kbItem.kbi_id},
						function(rData) {
							//console.log(rData);
							var str = '';
							$.each(rData, function(index, obj) {
								var p = {
									id : obj.kbc_id,
									title : obj.kbc_title,
									name : 'kbi_catalog_ids'
								}
								if(obj.isChecked==1){
									str += $('#checkbox-checked-template').render(p);
								} else {
									str += $('#checkbox-template').render(p);
								}
							})
							if (str == '') {
								$('#knowledge-catalog-checkbox')
										.html(
												'<div class="norm-form-txt-3" style="color: #00aeef"><lb:get key="lab_kb_no_catalog" /></div>');
							} else {
								$('#knowledge-catalog-checkbox').prepend(str);
								$("#knowledge-catalog-checkbox div:gt(6)").hide();
								$("#knowledge-catalog-checkbox #norm-more-catalog").css("color","#428bca").hide();
								$("#knowledge-catalog-checkbox #norm-less-catalog").css("color","#428bca").hide();
								if ($("#knowledge-catalog-checkbox").children("div").length > 7) {
									$("#knowledge-catalog-checkbox #norm-more-catalog").show();
								}
								$("#knowledge-catalog-checkbox #norm-more-catalog").click(
									function() {
										$("#knowledge-catalog-checkbox div:gt(6)").show();
										$("#knowledge-catalog-checkbox #norm-more-catalog").hide();
										$("#knowledge-catalog-checkbox #norm-less-catalog").show();
									}
								)

								$("#knowledge-catalog-checkbox #norm-less-catalog")
										.click(
												function() {
													$(
															"#knowledge-catalog-checkbox div:gt(6)")
															.hide();
													$(
															"#knowledge-catalog-checkbox #norm-more-catalog")
															.show();
													$(
															"#knowledge-catalog-checkbox #norm-less-catalog")
															.hide();
												})
							}
						});

		$
				.getJSON(
						'${ctx}/app/kb/tag/admin/getTagJson?kbi_id='+${kbItem.kbi_id},
						function(rData) {
							var str = '';
							$.each(rData, function(index, obj) {
								var p = {
									id : obj.tag_id,
									title : obj.tag_title,
									name : 'kbi_tag_ids'
								}
								if(obj.isChecked==1){
									str += $('#checkbox-checked-template').render(p);
								} else {
									str += $('#checkbox-template').render(p);
								}
							})
							if (str == '') {
								$('#knowledge-tag-checkbox')
										.html(
												'<div class="norm-form-txt-3" style="color: #00aeef"><lb:get key="lab_kb_no_tag" /></div>');
							} else {
								$('#knowledge-tag-checkbox').prepend(str);
								$("#knowledge-tag-checkbox div:gt(6)").hide();
								$("#knowledge-tag-checkbox #norm-more-tag")
										.css("color","#428bca")
										.hide();
								$("#knowledge-tag-checkbox #norm-less-tag")
										.css("color","#428bca")
										.hide();
								if ($("#knowledge-tag-checkbox").children(
										"div").length > 7) {
									$("#knowledge-tag-checkbox #norm-more-tag")
											.show();
								}
								$("#knowledge-tag-checkbox #norm-more-tag")
										.click(
												function() {
													$(
															"#knowledge-tag-checkbox div:gt(6)")
															.show();
													$(
															"#knowledge-tag-checkbox #norm-more-tag")
															.hide();
													$(
															"#knowledge-tag-checkbox #norm-less-tag")
															.show();
												})

								$("#knowledge-tag-checkbox #norm-less-tag")
										.click(
												function() {
													$(
															"#knowledge-tag-checkbox div:gt(6)")
															.hide();
													$(
															"#knowledge-tag-checkbox #norm-more-tag")
															.show();
													$(
															"#knowledge-tag-checkbox #norm-less-tag")
															.hide();
												})
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
			multiple : false,
			target : "_blank"
		});
		
		
		$('#wzb-uploader-li-2').click(function(){
			$('input[name=kbi_online]').val('ONLINE');
			$('#kbi_content_error').addClass('video-error');
		});
		
		$('#wzb-uploader-li-1').click(function(){
			$('input[name=kbi_online]').val('OFFLINE');
			$('#kbi_content_error').removeClass('video-error');
		});
		<c:if test="${!empty kbItem.kbi_online}">
			if('${kbItem.kbi_online}' == 'ONLINE'){
				$('#wzb-uploader-li-1').removeClass('active');
				$('#kbi_content_tab_div').removeClass('active').removeClass('in');
				$('#wzb-uploader-li-2').addClass('active');
				$('#kbi_content_url_tab_div').addClass('active').addClass('in');
				$('#kbi_content_error').addClass('video-error');
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

	})

	function changeImage(thisObj) {
		$("#" + $(thisObj).parent().attr("id") + " .cur").removeClass("cur");
		$(thisObj).addClass("cur");
	}
</script>
<script id="checkbox-template" type="text/x-jsrender">
		<div class="norm-form-txt-3">
			<span><input type="checkbox" name="{{>name}}" value="{{>id}}" /></span><span>{{>title}}</span>
</div>
</script>
<script id="checkbox-checked-template" type="text/x-jsrender">
<div class="norm-form-txt-3">
			<span><input type="checkbox" name="{{>name}}" value="{{>id}}" checked="true" /></span><span>{{>title}}</span>
</div>
</script>
<script id="image-template" type="text/x-jsrender">
		<a href="###" title="" onclick="changeImage(this)">
			<img width="116" height="70" src="${ctx}/imglib/knowledge/{{>file}}" alt="{{>file}}" />
			<div class="thickbox-bg"></div>
		</a>
</script>
<!-- 验证表单 -->
<script type="text/javascript">
$(function() {
	$("#knowledgeForm").submit(function(e) {
		$(".error").remove();
		var ret = true;
		var title = $('input[name=kbi_title]').val();
		var desc = $('textarea[name=kbi_desc]').val();
		var image = $('input[name=kbi_image]').val();
		var default_image = $('input[name=kbi_default_image]').val();
		var type = $('input[name=kbi_type]').val();
		var online = $('input[name=kbi_online]').val();
		var content = '';
		if (type == 'ARTICLE') {
			content = $('textarea[name=kbi_content]').val();
		} else {
			content = $('input[name=kbi_content]').val();
		}
		
		if(undefined == title || title == ''){
    		Dialog.alert(fetchLabel('label_core_knowledge_management_17')+fetchLabel('usr_is_not_null'));
    		return false;
    	}
		if(getChars(title) > 80){
			Dialog.alert(fetchLabel('label_core_knowledge_management_95'));
    		return false;
    	}
		if(undefined == desc || desc == ''){
    		Dialog.alert(fetchLabel('label_core_knowledge_management_34')+fetchLabel('usr_is_not_null'));
    		return false;
    	}
		if(getChars(desc) > 400){
			Dialog.alert(fetchLabel('label_core_knowledge_management_96'));
    		return false;
    	}
		if(undefined == content || content == ''){
    		Dialog.alert(fetchLabel('label_core_knowledge_management_38')+fetchLabel('usr_is_not_null'));
    		return false;
    	}
		if(getChars(content) > 20000){
    		Dialog.alert(fetchLabel('label_core_knowledge_management_94'));
    		return false;
    	}
		
		/* if(title == '' || title.length > 255 || title.trim() == ''){
			ret = false;
			var error_title = fetchLabel('lab_kb_title_error');
			if(title.length > 255){
				error_title = fetchLabel('lab_kb_length_error');
			}
			$('input[name=kbi_title]').parent().append('<span class="error">'+error_title+'</span>')
		} 
		if(desc == '' || desc .length > 255 || desc.trim() == ''){
			ret = false;
			var error_content = fetchLabel('lab_kb_desc_error');
			if(desc .length > 255){
				error_content = fetchLabel('lab_kb_length_error');
			}
			$('textarea[name=kbi_desc]').parent().append('<span class="error">'+error_content+'</span>')
		}*/
		if((typeof(image) == "undefined" || image =='') && default_image == ''){
			ret = false;
			$('#kbi_image_error').append('<span class="error thickbox" style="margin-left:30px;">'+fetchLabel('lab_kb_img_error')+'</span>')
		}
		if(typeof(content) == "undefined" || content == '' || content.trim() == ''){
			ret = false;
			$('#kbi_content_error').append('<span class="error">'+fetchLabel('lab_kb_content_error')+'</span>')
		} else {
			if(online == 'ONLINE'){
				var regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
				var re=new RegExp(regex);
				if (re.test(content)) {
					var length = content.length;
					if(length > 7){
						var suffix = content.substring(length - 4, length);
						if(suffix == '.mp4' || suffix == ".MP4"){
						} else {
							ret = false;
							$('#kbi_content_error').append('<span class="error">'+fetchLabel('lab_kb_video_url_error')+'</span>');
						}
					} else {
						ret = false;
						$('#kbi_content_error').append('<span class="error">'+fetchLabel('lab_kb_video_url_error')+'</span>');
					}
				} else {
					ret = false;
					$('#kbi_content_error').append('<span class="error">'+fetchLabel('lab_kb_video_url_error')+'</span>');
				}
			}
		}
		return ret;
	});
})
</script>
<style type="text/css">
.video-error { margin-top: 60px; }
</style>
</head>
<body>
	<div class="xyd-wrapper">
		<div id="main" class="xyd-main clearfix">
			<form:form id="knowledgeForm" modelAttribute="kbItem" enctype="multipart/form-data">
				<form:input type="hidden" cssStyle="width: 300px;" cssClass="form-control" path="kbi_type" />
				<form:input type="hidden" cssStyle="width: 300px;" cssClass="form-control" path="kbi_online" />
				<form:input type="hidden" id="kbi_default_image" cssStyle="width: 300px;" cssClass="form-control" path="kbi_default_image" />
				<div class="wzb-model-10">
                    <div class="wzb-title-2" style="font-weight: normal;"><lb:get key="lab_share_${kbItem.kbi_type}" /></div>
                    
                    <table class="margin-top30">
                        <tr>
                             <td class="wzb-form-label"><span class="wzb-form-star">*</span><lb:get key="lab_kb_title" />：</td>
                             
                             <td class="wzb-form-control">
                                 <div class="wzb-selector" style="width: 600px;">
                                      <form:input cssStyle="width:400px;" cssClass="form-control" path="kbi_title" />
                                      <form:errors path="kbi_title" cssClass="error" />
                                 </div>
                             </td>
                        </tr>
                        
                        <tr>
                             <td class="wzb-form-label" valign="top"><span class="wzb-form-star">*</span><lb:get key="lab_kb_desc" />：</td>
                             
                             <td class="wzb-form-control">
                                 <form:textarea cssStyle="width:400px;" cssClass="form-control" path="kbi_desc"></form:textarea>
                                 <form:errors path="kbi_desc" cssClass="error" />
                             </td>
                        </tr>
                        
                        <tr>
                             <td class="wzb-form-label" valign="top"><span class="wzb-form-star">*</span><lb:get key="lab_kb_image" />：</td>
                             
                             <td class="wzb-form-control">
                                <link rel="stylesheet" href="${ctx}/static/css/thickbox.css" />
                                <script src="${ctx}/static/js/thickbox-compressed.js" type="text/javascript"></script>
                                <div class="clearfix">
                                    <div class="wzb-uploader" id="image-input">
                                        <c:if test="${kbItem.imageAttachment != null && kbItem.imageAttachment.kba_id !=null}">
                                            <ul>
                                                 <li id="${kbItem.imageAttachment.kba_id}" url="${ctx}${kbItem.imageAttachment.kba_url}" filename="${kbItem.imageAttachment.kba_filename}">${kbItem.imageAttachment.kba_filename}</li>
                                            </ul>
                                        </c:if>
                                    </div>
                                    <div id="kbi_image_error">
                                        <span><a href="#TB_inline?height=480&width=580&inlineId=myOnPageContent" class="thickbox">
                                                <lb:get key="lab_kb_default_images" />
                                            </a>
                                            </a> </span><span><form:errors path="kbi_image" cssClass="error thickbox" style="margin-left:30px;" /></span>
                                    </div>
                                </div>
			                    <div id="myOnPageContent" style="display: none;">
									<div class="thickbox-tit">
										<lb:get key="label_km.label_core_knowledge_management_37" />
									</div>
				
									<div class="thickbox-cont thickbox-user clearfix thickbox-content-2" id="defaultImages"></div>
				
									<div class="norm-border  thickbox-footer" style="padding-bottom:10px;">
										<input id="define" type="button" class="btn wzb-btn-blue wzb-btn-bigger margin-right15  wzb-btn-big" name="pertxt" value="<lb:get key='global.button_submit'/>" />
										<input type="button" class="btn wzb-btn-blue wzb-btn-bigger TB_closeWindowButton  wzb-btn-big" name="pertxt" value="<lb:get key='global.button_cancel'/>" />
									</div>
								</div>
                             </td>
                        </tr>   
                        
                        <tr>
                             <td class="wzb-form-label" valign="top"><span class="wzb-form-star">*</span><lb:get key="lab_kb_content" />：</td>
                             
                             <td class="wzb-form-control">
                                 <c:choose>
                                    <c:when test="${kbItem.kbi_type=='ARTICLE'}">
                                        <form:textarea id="editor" cssStyle="width:300px;" cssClass="form-control" path="kbi_content"></form:textarea>
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
                                                        <li id="${attachment.kba_id}" url="${ctx}${attachment.kba_url}" filename="${attachment.kba_filename}">${attachment.kba_filename}</li>
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
                                                        <li id="${attachment.kba_id}" url="${ctx}${attachment.kba_url}" filename="${attachment.kba_filename}">${attachment.kba_filename}</li>
                                                    </c:forEach>
                                                </ul>
                                            </c:if>
                                        </div>
                                    </c:when>
                                    <c:when test="${kbItem.kbi_type=='IMAGE'}">
                                        <div class="wzb-uploader" id="image-content-input">
                                            <c:if test="${fn:length(kbItem.attachments) > 0}">
                                                <ul>
                                                    <c:forEach items="${kbItem.attachments}" var="attachment" varStatus="vs">
                                                        <li id="${attachment.kba_id}" url="${ctx}${attachment.kba_url}" filename="${attachment.kba_filename}">${attachment.kba_filename}</li>
                                                    </c:forEach>
                                                </ul>
                                            </c:if>
                                        </div>
                                    </c:when>
                                </c:choose>
                                <div id="kbi_content_error">
                                    <form:errors path="kbi_content" cssClass="error" />
                                </div>
                             </td>
                        </tr>
                        
                        <tr>
                             <td class="wzb-form-label" valign="top"><lb:get key="article_catalog" />：</td>
                             
                             <td class="wzb-form-control" id="knowledge-catalog-checkbox">
                                 <a href="javascript:;" id="norm-more-catalog">
                                      <lb:get key="global_more" />
                                 </a>
                                 <a href="javascript:;" id="norm-less-catalog">
                                      <lb:get key="click_up" />
                                 </a>
                                
                                 <form:errors path="kbi_catalog_ids" cssClass="error" />
                             </td>
                        </tr>
                        
                        <tr>
                             <td class="wzb-form-label" valign="top"><lb:get key="lab_kb_tag" />：</td>
                             
                             <td class="wzb-form-control" id="knowledge-tag-checkbox">
                                 <a href="javascript:;" id="norm-more-tag">
                                      <lb:get key="global_more" />
                                 </a>
                                 <a href="javascript:;" id="norm-less-tag">
                                      <lb:get key="click_up" />
                                 </a>
                             </td>
                        </tr>
                    </table>
                    
                    <div class="wzb-bar">
                         <input type="submit" class="btn wzb-btn-orange wzb-btn-big margin-right15" value='<lb:get key="button_ok" />'>
                         <a type="button" class="btn wzb-btn-orange wzb-btn-big" href="${ctx}/app/kb/center/${source}?tab=${tab}"><lb:get key="button_cancel" /></a>
                    </div>
                    
                </div> <!-- wzb-model-10 End--> 
			</form:form>
		</div>
	</div>
	<!-- xyd-wrapper End-->
</body>
</html>