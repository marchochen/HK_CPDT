function wbSns() {
	this.common = new wbSnsCommon();
	this.share = new wbSnsShare();
	this.collect = new wbSnsCollect();
	this.attent = new wbSnsAttent();
	this.comment = new wbSnsComment();
	this.valuation = new wbSnsValuation();
	this.doing = new wbSnsDoing();
}

// 公用
function wbSnsCommon() {
	this.reload = wbSnsCommonReload;
	this.reload_valuation = wbSnsCommonReloadCourseValuation;
}

// 评论
function wbSnsComment() {
	this.add = wbSnsCommentAdd;
	this.del = wbSnsCommentDel;
	this.reply = wbSnsCommentReply;
	this.post = wbSnsCommentPost;
	this.toggle = wbSnsCommentToggle;
	this.del_sns = wbTASnsCommentDel;
}

// 评价
function wbSnsValuation() {
	this.add = wbSnsValuationAdd;
	this.cancel = wbSnsValuationCancel;
}

// 分享
function wbSnsShare() {
	this.open = wbSnsShareOpen;
	this.close = wbSnsShareClose;
	this.add = wbSnsShareAdd;
	this.cancel = wbSnsShareCancel;
}

// 收藏
function wbSnsCollect() {
	this.add = wbSnsCollectAdd;
	this.cancel = wbSnsCollectCancel;
}

// 关注
function wbSnsAttent() {
	this.add = wbSnsAttentAdd;
	this.cancel = wbSnsAttentCancel;
}

// 动态
function wbSnsDoing() {
	this.add = wbSnsDoingAdd;
	this.del = wbSnsDoingDel;
}

// 发布动态
function wbSnsDoingAdd() {
	var content = $('#doing_title').val();
	if ($('#doing_title').attr('text') == content) {
		alert(getLabel('LN211'));
		return;
	}
	if (content.length <= 0) {
		alert(getLabel('LN211'));
		return;
	}
	if (content.length > 200) {
		alert(getLabel('339') + ' 200');
		return;
	}

	$.ajax({
		url :  wb_utils_app_base + 'app/sns/doing/post',
		type : 'post',
		dataType : 'json',
		data : {
			content : content
		},
		success : function(data) {
			if (data.status == 'success') {
	
				$("#doing_title").val($("#doing_title").attr('pt'));
				if (Wzb.getUrlParam('tab') == 4) {
					if (typeof reloadTable == 'function') {
						reloadTable();
					}
				} else { // 如果当前在非动态的tab，那么调整到我的动态，显示最新发布的动态
					location.href = Wzb.setUrlParam('tab', 4);
				}
			}
		}
	});
}

// 删除动态
function wbSnsDoingDel(id) {
	if (confirm(getLabel('LN267'))) {
		$.ajax({
			url :  wb_utils_app_base + 'app/sns/doing/del/' + id,
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

// 发表评论
function wbSnsCommonReload(course) {
	$('#count-like').html(getLabel('LN158') + '(' + course.iti_like_count + ')');
	$('#count-collect').html(getLabel('LN159') + '(' + course.iti_collect_count + ')');
	$('#count-share').html(getLabel('LN160') + '(' + course.iti_share_count + ')');

	$('.count-cmt').html(course.iti_cmt_count);
	$('.score-avg-star').html(course.iti_score);

	if (course.iti_score > 0) {
		$('img', $('.cos-star-score')).each(function(index) {
			if (index < course.iti_score) {
				$(this).attr('src', $(this).attr('over'));
			} else {
				$(this).attr('src', $(this).attr('out'));
			}
		});
	}
}

function wbSnsCommonReloadCourseValuation() {
	$('img', $('#cos-star-rate')).each(function(index) {
		$(this).attr('src', $(this).attr('out'));
	});
}

// 发表评论
function wbSnsCommentAdd(module, itm_id, comment) {
	$.ajax({
		url :  wb_utils_app_base + 'app/sns/comment/post',
		type : 'post',
		dataType : 'json',
		data : {
			content : comment,
			module : module,
			target : itm_id
		},
		success : function(data) {
			if (typeof dt == 'object' && typeof dt.reloadTable == 'function') {
				dt.reloadTable();
			} else if (typeof reloadTable == 'function') {
				reloadTable();
			}
			$('#my_comment').val($('#my_comment').attr('text'));
		}
	});
}

// 删除评论
function wbSnsCommentDel(id, module) {
	if (confirm(getLabel('LN267'))) {
		$.ajax({
			url :  wb_utils_app_base + 'app/sns/comment/del/' + id,
			type : 'post',
			dataType : 'json',
			async : false,
			success : function(data) {
				if (data.status == 'success') {
					if (module == 'Course') {
						// 更新评论数量，默认为0，因为当最后一条时删除不会执行success
						$('#comment-count-div span').html(getLabel('LN155'));
						_wbSnsCommentReload();
					} else {
						// 更新数量
						var target;
						if($("#comment-detail-" + id).closest(".doing-reply-list").attr("id")){
							target = $("#comment-detail-" + id).closest(".doing-reply-list").attr("id").replace('doing-reply-list-', '');
						}
						if (data.comment_count || data.comment_count == 0) {
							$("#cmt-toggle-" + target).attr("cnt", data.comment_count);
							$("#cmt-toggle-" + target).text(getLabel('LN187') + '(' + $("#cmt-toggle-" + target).attr("cnt") + ')');
						}

						if (data.ids) {
							$.each(data.ids, function(index, value) {
								$('#comment-detail-' + value).remove();
							});
						}
						$('#comment-detail-' + id).remove();
						
						if (typeof reloadTable == 'function') {
							reloadTable();
						}
					}
				} else if (data.status == 'error') {
					alert(data.message)
				}
			}
		});
	}
}

// 添加回复
function wbSnsCommentReply(id, username) {
	$("#comment-reply-input-" + id).val(getLabel('323') + username + ":");
	$("#comment-reply-" + id).toggle();
}

// 发表评论
function wbSnsCommentPost(module, target, cmt_id) {
	var comment = '';
	if (cmt_id && cmt_id > 0 && target > 0) {
		comment = $('#comment-reply-input-' + cmt_id).val();
	} else if (target > 0) {
		comment = $('#doing-reply-input-' + target).val();
	}
	if(comment == ''){
		alert(getLabel('LN211'));
		return;
	}
	$.ajax({
		url :  wb_utils_app_base + 'app/sns/comment/post',
		type : 'post',
		dataType : 'json',
		data : {
			content : comment,
			module : module,
			reply_to : (cmt_id && cmt_id > 0 && target > 0) ? cmt_id : 0,
			target : target
		},
		success : function(data) {
			if (data.status == 'success') {
				if (module == 'Course') {
					_wbSnsCommentReload();
				} else {
					$("#doing-reply-list-desc-" + target).remove();
					if ($("#doing-reply-list-" + target).attr('init') != "true") {
						wbSnsCommentToggle(target);
					} else {
						_wbSnsCommentAddRow(data.comment, target, data.prof);
						$("#comment-reply-" + cmt_id).attr('show', 'false');
					}

					if (data.comment_count) {
						$("#cmt-toggle-" + target).attr("cnt", data.comment_count)
						if ($("#cmt-toggle-" + target).attr("show") == 'true') {
						} else {
							$("#cmt-toggle-" + target).attr("show", 'true');
						}
						$("#cmt-toggle-" + target).text(getLabel('LN187') + '(' + $("#cmt-toggle-" + target).attr("cnt") + ')');
						$("#doing-reply-list-" + target).show().attr('init', 'true');
					}
				}
			} else if (data.status == 'error') {
				alert(data.message)
			}

			if (cmt_id && cmt_id > 0 && target > 0) {
				// 回复评论后情况内容，隐藏输入框
				$('#comment-reply-input-' + cmt_id).val('');
				$('#comment-reply-' + cmt_id).hide();
			} else if (target > 0) {
				$('#doing-reply-input-' + target).val('');
			}
		}
	});
}

// 展开评论
function wbSnsCommentToggle(doi_id) {
	if ($("#cmt-toggle-" + doi_id).attr("show") == 'true') {
		$("#doing-reply-list-" + doi_id).hide();
		$("#cmt-toggle-" + doi_id).attr("show", 'false');
		$("#cmt-toggle-" + doi_id).text(getLabel('LN186') + '(' + $("#cmt-toggle-" + doi_id).attr("cnt") + ')');
	} else {
		$("#cmt-toggle-" + doi_id).attr("show", 'true');
		$("#cmt-toggle-" + doi_id).text(getLabel('LN187') + '(' + $("#cmt-toggle-" + doi_id).attr("cnt") + ')');
		if ($("#doing-reply-list-" + doi_id).attr('init') != "true") {
			$.ajax({
				url :  wb_utils_app_base + 'app/sns/comment/find_by_module?module=Doing&target=' + doi_id,
				type : 'post',
				async : false,
				dataType : 'json',
				success : function(data) {
					if (data.rows && data.rows.length > 0) {
						$("#doing-reply-list-" + doi_id).empty();

						for ( var i = 0; i < data.rows.length; i++) {
							_wbSnsCommentAddRow(data.rows[i], doi_id, data.prof);
						}
					}
					$("#doing-reply-list-" + doi_id).show();
				}
			});
		}
		$("#doing-reply-list-" + doi_id).show().attr('init', 'true');
	}
}

function _wbSnsCommentReload(data, doi_id) {
	if (typeof dt == 'object' && typeof dt.reloadTable == 'function') {
		dt.reloadTable();
	} else	if (typeof dataTable == 'object' && typeof dataTable.reloadTable == 'function') {
		dataTable.reloadTable();
	} else if (typeof reloadTable == 'function') {
		reloadTable();
	}
}

function _wbSnsCommentAddRow(data, doi_id, prof) {
	p = {
		usr_display_bil : data.user.usr_display_bil,
		usr_photo : data.user.usr_photo == undefined ? "../user/user.png" : data.user.usr_photo,
		usr_photo_width : data.user.usr_photo_width,
		usr_photo_height : data.user.usr_photo_height,
		id : data.s_cmt_id,
		doi_id : data.s_cmt_target_id,
		title : data.s_cmt_content,
		reply_text : getLabel('323') + data.user.usr_display_bil + ':',
		comment : getLabel('LN149'),
		show_del_btn : data.s_cmt_uid == prof.usr_ent_id ? 'block' : 'none',
		show_reply_btn : data.s_cmt_uid != prof.usr_ent_id ? 'block' : 'none',
		time : Wzb.displayTime(data.s_cmt_create_datetime, Wzb.time_format_ymdhm),
		link :  wb_utils_app_base + 'app/sns/user_center' + ((data.user.uid != prof.usr_ent_id) ? '/' + data.user.uid : '')
	};
	$($('#commentTemplate').render(p)).appendTo("#doing-reply-list-" + doi_id);
}

// 发表评价
function wbSnsValuationAdd(module, target, type, score, tkh_id) {
	$.ajax({
		url :  wb_utils_app_base + 'app/sns/valuation/add/' + module + '/' + type + '/' + target + '/' + score + ((tkh_id && tkh_id > 0) ? '?tkh_id=' + tkh_id : ''),
		type : 'post',
		dataType : 'json',
		success : function(data) {
			if (data.status == 'success') {
				location.reload();
			} else if (data.status == 'error') {
				alert(data.message)
			}
			wbSnsCommonReloadCourseValuation();
		}
	});
}

// 删除评价
function wbSnsValuationCancel(id) {
		var url =  wb_utils_app_base + 'app/sns/valuation/del/';
		wbSnsCancel(url, id);
}

var snsShareBox = null;
function wbSnsShareOpen(target) {
	var html = $('#boxyTemplate').render({});
	snsShareBox = new Boxy(html, {
		title : getLabel('LN070'),
		afterHide : function() {
			$('#' + target).remove();
		}
	});

	$("#share_title").click(function() {
		if($(this).val() == $(this).attr('text') ){
			$(this).val('');
		}
	}).blur(function() {
		if ($(this).val() == '') {
			$(this).val($(this).attr('text'));
		}
	});
}

function wbSnsShareClose() {
	if (snsShareBox != null) {
		snsShareBox.hide();
	}
}

function wbSnsShareAdd(itm_id) {
	var title = $('#share_title').val();
	if ($('#share_title').attr('text') == title) {
		title = '';
	}
	if (title.length > 200) {
		alert(getLabel('339') + ' 200');
		return;
	}

	$.ajax({
		url :  wb_utils_app_base + 'app/sns/share/add/' + itm_id,
		type : 'post',
		dataType : 'json',
		data : {
			title : title
		},
		success : function(data) {
			wbSnsShareClose();
			if (data.status == 'success') {
//				alert(data.message)
			} else if (data.status == 'error') {
				alert(data.message)
			}
			location.reload();
		}
	});
}

function wbSnsShareCancel(id) {
		var url =  wb_utils_app_base + 'app/sns/share/del/';
		wbSnsCancel(url, id);
}

function wbSnsCollectAdd(itm_id, objClass) {
	$.ajax({
		url :  wb_utils_app_base + 'app/sns/collect/add/' + itm_id,
		type : 'post',
		dataType : 'json',
		success : function(data) {
			if (data.status == 'success') {
//				alert(data.message)

				wbSnsCommonReload(data.course);
			} else if (data.status == 'error') {
				alert(data.message)
			}
		}
	});
}

function wbSnsCollectCancel(id) {
		var url =  wb_utils_app_base + 'app/sns/collect/del/';
		wbSnsCancel(url, id);

}

function wbSnsCancel(url, id) {
	if (confirm(getLabel('LN267'))) {
		$.ajax({
			url : url + id,
			type : 'post',
			async : true,
			dataType : 'json',
			success : function(res) {
				if (res.result == 'success') {
					// alert("删除" + wb_msg_success + "！");
					if (typeof reloadTable == 'function') {
						reloadTable();
					}
				} else if (res.result == 'input') {
					alert("权限不足！");
				} else {
					alert("删除" + wb_msg_fail + "！");
				}
			}
		});
	}
}

function wbSnsAttentAdd(user_id, callback) {
	$.ajax({
		url :  wb_utils_app_base + 'app/sns/attention/add/' + user_id,
		type : 'post',
		async : true,
		dataType : 'json',
		success : function(data) {
			if (data.status == 'success') {
				if (callback && callback == 'reloadTable') {
					if (typeof reloadTable == 'function') {
						reloadTable();
					}
				}
				$('#user-attent-add-' + user_id).hide();
				$('#user-attent-cancel-' + user_id).show();
			} else {
				alert(data.message);
			}
		}
	});
}

// type == cancel 取消关注
// type == remove 移除粉丝
function wbSnsAttentCancel(user_id, type) {
	if (confirm(getLabel('LN267'))) {
		$.ajax({
			url : wb_utils_app_base + 'app/sns/attention/cacel/' + user_id + '?type=' + ((type == 'remove' ? 'remove' : 'cancel')),
			type : 'post',
			async : true,
			dataType : 'json',
			success : function(data) {
				if (data.status == 'success') {
					$('#user-attent-add-' + user_id).show();
					$('#user-attent-cancel-' + user_id).hide();
					if (typeof dataTable == 'object' && typeof dataTable.reloadTable == 'function') {
						dataTable.reloadTable();
					} else if (typeof reloadTable == 'function') {
						reloadTable();
					}
				} else {
					alert(data.status);
				}
			}
		});
	}
}
function wbTASnsCommentDel(itm_id,id,type,lang,comment_usr_id){
	if(confirm(eval("wb_msg_"+lang+"_del_it"))){
		var url_success = wb_utils_invoke_ae_servlet('cmd', 'ae_get_itm_comment_lst', 'itm_id', itm_id, 'stylesheet', 'ae_item_comment_lst.xsl');
		url = wb_utils_invoke_ae_servlet('cmd', 'ae_del_sns', 'sns_type', type, 's_cmt_id', id,'ent_id',comment_usr_id,'url_success',url_success);
		parent.location.href = url;
	}
}