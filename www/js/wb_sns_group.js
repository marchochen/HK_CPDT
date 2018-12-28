function wbSnsGroup() {
	this.create = wbSnsGroupCreate;
	this.update = wbSnsGroupUpdate;
	this.destroy = wbSnsGroupDestroy;

	this.toggle = wbSnsGroupToggle;
	this.post = wbSnsGroupPost;
	this.reply = wbSnsGroupReply;
	this.cancel = wbSnsGroupCancel;
	this.toggle_reply = wbSnsGroupToggleReply;
	this.del_reply = wbSnsGroupDelReply;
	this.del_topic = wbSnsGroupDelTopic;
	this.clear = _wbSnsGroupClear;
	
	this.apply = wbSnsGroupApply;
}

//申请加入群组
function wbSnsGroupApply(id) {
	$.ajax({
		url : wb_utils_app_base + 'app/sns/group/apply/' + id,
		type : 'post',
		dataType : 'json',
		success: function(data){
			if (data.status == 'success') {
				alert(data.message);
				if (typeof reloadTable == 'function') {
					reloadTable();
				}
			} else{
				alert(data.message);
			}
		}
	});
}

// 添加群组
function wbSnsGroupCreate() {
	var html = $('#boxyTemplate').render({});
	wbSnsGroupDestroy();
	groupBox = new Boxy(html, {
		title : getLabel('LN245')
	});
	$("input[name=s_grp_title]").val('');
	$("textarea[name=s_grp_desc]").val('');
	$("select[name=s_grp_private]").val('0');
	$("input[name=type]").val('create');
}

// 修改群组
function wbSnsGroupUpdate(data) {
	wbSnsGroupCreate();
	groupBox.setTitle(getLabel('LN246'));
	$("input[name=type]").val('update');
	$("input[name=s_grp_title]").val(data.s_grp_title);
	$("textarea[name=s_grp_desc]").val(data.s_grp_desc);
	$("select[name=s_grp_private]").val(data.s_grp_private);
}

// 关闭窗口
function wbSnsGroupDestroy() {
	if (typeof groupBox == 'object' && groupBox != undefined) {
		groupBox.unload();
	}
}

// 发布动态
function wbSnsGroupToggle(type) {
	$('.group-doing-div').hide();
	if (type == 'group_image') {
		$('#doing-image').show();
	} else if (type == 'group_vedio') {
		$('#doing-vedio').show();
	} else if (type == 'group_document') {
		$('#doing-document').show();
	} else {
		$('#group-doing-btn').show();
		$('#group-doing-title').show();
	}
}

function wbSnsGroupPost(type, id) {
	var title = '';
	var url = '';

	var title_input = 'doing-title';
	var title_empty = true;
	if (type == 'group_image') {
		title_input = 'doing-image-title'

		// 检查分享图片
		image = $('#doing-image-file').val();
		if (image.length <= 0) {
			alert(getLabel('LN227'));
			return;
		}

		// 检查图片后缀名
		var file_type = image.substring(image.lastIndexOf('.') + 1).toLowerCase();
		var types = [ "jpg", "gif", "png" ];
		var len = types.length;
		var ret = false;
		var str = "";
		for ( var i = 0; i < len; i++) {
			if (file_type === types[i]) {
				ret = true;
			}
			str += types[i];
			if (i !== len - 1) {
				str += ", ";
			}
		}
		if (ret === false) {
			alert(getLabel('LN314') + str); //只支持一下文件
			return;
		}

		$('#doingform-action').val('group_image');
	} else if (type == 'group_document') {
		title_input = 'doing-document-title'

			// 检查分享文档
			doc = $('#doing-document-file').val();
			if (doc.length <= 0) {
				alert(getLabel('LN302'));
				return;
			}
			//.ppt、.pptx）、excel（.xls、.xlsx）、word（.doc、.docx）、txt
			var reg = /.*(\.(ppt)|(pptx)|(xls)|(xlsx)|(doc)|(docx)|(txt))$/; 
			var suffix = "ppt,pptx,xls,xlsx,doc,docx,txt";
			// 检查文档后缀名
			if(!reg.test(doc)) {
				alert(getLabel('LN314')  + suffix);
				return;
			}
			$('#doingform-action').val('group_document');
	} else if (type == 'group_vedio') {
		title_input = 'doing-vedio-title'

		// 检查视频网址
		url = $('#doing-vedio-url').val();
		if ($('#doing-vedio-url').attr('placeholdertext') == url) {
			alert(getLabel('LN226'));
			return;
		}
		if (url.length <= 0) {
			alert(getLabel('LN226'));
			return;
		}

		$('#doingform-action').val('group_vedio')
	} else {
		title_empty = false;

		$('#doingform-action').val('group_doing')
	}

	title = $('#' + title_input).val();
	if (title_empty) {
		if ($('#' + title_input).attr('placeholdertext') == title) {
			title = '';
		}
	} else {
		if ($('#' + title_input).attr('placeholdertext') == title) {
			alert(getLabel('LN231'));
			return;
		}
		if (title.length <= 0) {
			alert(getLabel('LN231'));
			return;
		}
	}

	$('#doingform-title').val(title);
	$('#doingform-url').val(url);

	$('#doingform').ajaxSubmit({
		dataType : 'json',
		success : function() {
			_wbSnsGroupClear();

			$("#doingform").resetForm();

			wbSnsGroupToggle('group_doing');

			if (reloadTable && typeof reloadTable == 'function') {
				reloadTable();
			}
		},
		error : function(data) {
			_wbSnsGroupClear();
			if (reloadTable && typeof reloadTable == 'function') {
				reloadTable();
			}
		}
	});
}

function _wbSnsGroupClear() {
	$('#doingform-action').val('');
	$('#doingform-title').val('');
	$('#doingform-url').val('');
	
	//$('#doing-image-file').val('');
	
	var doingFile = $('#doing-image-file').val('');
	$(doingFile).after($(doingFile).clone().val(''));
	$(doingFile).remove();
	
	$('#doing-image-title').val($('#doing-image-title').attr('placeholdertext'));
	$('#doing-image-text').val($('#doing-image-text').attr('placeholdertext'));
	$('#doing-vedio-url').val($('#doing-vedio-url').attr('placeholdertext'));
	$('#doing-vedio-title').val($('#doing-vedio-title').attr('placeholdertext'));
	$('#doing-title').val($('#doing-title').attr('placeholdertext'));
	
	
	var documentFile = $('#doing-document-file').val('');
	$(documentFile).after($(documentFile).clone().val(''));
	$(documentFile).remove();
	
	$('#doing-document-title').val($('#doing-document-title').attr('placeholdertext'));
	$('#doing-document-text').val($('#doing-document-text').attr('placeholdertext'));
}

function wbSnsGroupCancel() {
	$('.group-doing-div').hide();
	$('#group-doing-btn').show();
	$('#group-doing-title').show();

	_wbSnsGroupClear();
}

function wbSnsGroupReply(module, target, cmt_id) {
	var comment = '';
	if (cmt_id && cmt_id > 0 && target > 0) {
		comment = $('#comment-reply-input-' + cmt_id).val();

		if (comment == $('#comment-reply-input-' + cmt_id).attr('placeholdertext')) {
			comment = '';
		}
	} else if (target > 0) {
		comment = $('#doing-reply-input-' + target).val();
	}

	if ($.trim(comment) == '') {
		alert(getLabel('LN231'));

		if (cmt_id && cmt_id > 0 && target > 0) {
			$('#comment-reply-input-' + cmt_id).focus();
		} else if (target > 0) {
			$('#doing-reply-input-' + target).focus();
		}
		return;
	}

	$.ajax({
		url : wb_utils_app_base + 'app/sns/comment/post',
		type : 'post',
		dataType : 'json',
		data : {
			content : comment,
			module : module,
			reply_to : (cmt_id && cmt_id > 0 && target > 0) ? cmt_id : 0,
			target : target
		},
		success : function(data) {
			p = {
				usr_display_bil : data.comment.user.usr_display_bil,
				id : data.comment.s_cmt_id,
				doi_id : data.comment.s_cmt_target_id,
				title : data.comment.s_cmt_content,
				reply_text : getLabel('323') + data.comment.user.usr_display_bil + ':',
				time : Wzb.displayTime(data.comment.s_cmt_create_datetime, Wzb.time_format_ymdhm),
				show_del_btn : (is_owner == 'true') ? '' : (is_member == 'true' && data.comment.s_cmt_uid == data.prof.usr_ent_id ? '' : 'none'),
				show_reply_btn : (is_member == 'true' && data.comment.s_cmt_uid != data.prof.usr_ent_id) ? '' : 'none',
				link : '${ctx}/app/sns/user_center' + ((data.comment.user.uid != data.prof.usr_ent_id) ? '/' + data.comment.user.uid : '')
			};
			$('#group-doing-reply-list-' + target).append($('#replyTemplate').render(p)).show();

			if (cmt_id && cmt_id > 0 && target > 0) {
				comment = $('#comment-reply-input-' + cmt_id).val($('#comment-reply-input-' + cmt_id).attr('placeholdertext'));

				wbSnsGroupToggleReply('#comment-reply-' + cmt_id);
			} else {
				$('#doing-reply-input-' + target).val('');
			}
			// 清空输入框
			$("#doing-reply-input-" + target).val('');
		}
	});
}

function wbSnsGroupDelReply(id, target) {
	if (confirm(getLabel('LN267'))) {
		$.ajax({
			url :  wb_utils_app_base + 'app/sns/group/del/reply/' + id,
			type : 'post',
			dataType : 'json',
			async : false,
			success : function(data) {
				if (data.status == 'success') {
					if (data.ids) {
						$.each(data.ids, function(index, value) {
							$('#doing-reply-' + value).remove();
						});
					}
					$('#doing-reply-' + id).remove();

					if (target && target != null) {
						if ($('.group-doing-reply-detail', $('#group-doing-reply-list-' + target)).size() == 0) {
							$('#group-doing-reply-list-' + target).hide();
						}
					}
				} else if (data.status == 'error') {
					alert(data.message)
				}
			}
		});
	}
}

// 删除动态
function wbSnsGroupDelTopic(id) {
	if (confirm(getLabel('LN268'))) {
		$.ajax({
			url :  wb_utils_app_base + 'app/sns/group/del/' + id,
			type : 'post',
			dataType : 'json',
			success : function(data) {
				if (data.status == 'success') {

					if (typeof reloadTable == 'function') {
						reloadTable();
					}
				}
			}
		});
	}
}

function wbSnsGroupToggleReply(id) {
	$(id).toggle();
}