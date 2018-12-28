function doNavLink(pType, subType, total) {
	for (var i = 1; i <= total; i++) {
		$("#" + pType + i).css("display", "none");
		$("#" + pType + total + i).removeClass('wzb-btn-circle-blue');
		$("#" + pType + total + i).addClass('wzb-btn-circle-gray');
	}
	$("#" + pType + subType).css("display", "");
	$("#" + pType + total + subType).removeClass('wzb-btn-circle-gray');
	$("#" + pType + total + subType).addClass('wzb-btn-circle-blue');
}

function doHandleString(str) {
	var sTem = str;
	if (str != "" && str.length > 10) {
		sTem = str.substring(0, 10) + "...";
	}
	return sTem;
}

var p = "?page=1&rp=4&t" + new Date().getTime();

function doGetTabContent(id, tabType, dataUrl, emptyKey) {
	$.getJSON(dataUrl + p, function(rData) {
		if (tabType == 'Exam') {
			if (rData.rows.length > 0) {
				$("#" + id).html(createDataTable(tabType, dataUrl, rData.rows));
			} else {
				$("#" + id).html($('#emptyExamTemplate').render());
			}
		} else {
			$("#" + id).html(createDataTable(tabType, dataUrl, rData.rows));
		}
	});
}

function createDataTable(tabType, dataUrl, rows) {
	var str = "";
	var cnt = 0;
	$.each(rows, function(index, obj) {
		var cov_progress = 0;
		if (obj.app_status == 'Admitted' && ((tabType != "Exam" && obj.comp_criteria != undefined && obj.comp_criteria != null && obj.comp_criteria.is_available == true))) {
			cov_progress = obj.cov_progress;
		}
		link = "";
		if(obj.itm_type == 'VIDEO') {
			link = wb_utils_controller_base + 'course/video_course_main?res_id=' + obj.cos_res_id + '&tkh_id=' + obj.cov_tkh_id
		} else {
			link = wb_utils_controller_base + 'course/course_home?itm_id=' + obj.app_itm_id + '&tkh_id=' + obj.cov_tkh_id + '&res_id=' + obj.cos_res_id
		}
		var p = {
			image : Wzb.getCourseImage(obj.itm_icon, tabType == 'INTEGRATED' ? false : true),
			link : link,
			title : doHandleString(obj.itm_title),
			progress : ' ' + cov_progress,
			date : Wzb.displayTimeDefValue(obj.app_upd_timestamp, Wzb.time_format_ymd, '--')
		};

		if (tabType == 'Course' && index < 3) {
			str += '<div class="grid-u-1-3">' + $('#cosTemplate').render(p) + '</div>';
		}
		if (tabType == 'Exam' && index < 4) {
			str += $('#examTemplate').render(p);
		}
		if (tabType == 'INTEGRATED' && index < 4) {
			str += '<div class="grid-u-1-2">' + $('#inteTemplate').render(p) + '</div>';
			if (index % 2 == 1) {
				str += '<div class="clear"></div>';
			}
		}
		cnt++;
	});

	if (tabType == 'Course' && cnt < 3) {
		for (var i = cnt; i < 3; i++) {
			str += '<div onclick="javascript: nav_go(\'' + wb_utils_controller_base + 'course/course_center\');" class="grid-u-1-3"><div class="empty-course empty-course-cos"></div></div>';
		}
	}
	if (tabType == 'INTEGRATED' && cnt < 4) {
		for (var i = cnt; i < 4; i++) {
			str += '<div onclick="javascript: nav_go(\'' + wb_utils_controller_base + 'course/course_center\');" class="grid-u-1-2"><div class="empty-course empty-course-inte"></div></div>';
		}
	}
	if (tabType == 'Exam') {
		var html = '<table style="width: 100%;" class="simpletable">';
		html += str;
		html += '</table>';
		str = html;
	}
	return str;
}

function getCourseImage(itm_icon, is_small) {
	var path = '<div class="cos-icon' + (is_small ? ' cos-icon-small' : '') + '">';
	if (itm_icon !== undefined && itm_icon !== '') {
		path += '<img class="cos-icon-img" src="' + Wzb.getAbsoluteImagePath(itm_icon) + '"/>';
	} else {
		path += '<div class="cos-icon-default">&nbsp;</div>';
	}
	path += '</div>';
	return path;
}

function autoScollAnnList() {
	var lWidth = 0;
	$("#ann_list_ul li").each(function() {
		lWidth += $(this).outerWidth();
	});
	var dWidth = $('#ann_list').outerWidth();

	$("#ann_list_ul").width(lWidth * 2);
	if (lWidth > dWidth) {
		var mLeft = 0;
		$("#ann_list_ul li").clone().appendTo("#ann_list_ul");

		function rollText() {
			mLeft++;
			if (mLeft > lWidth) {
				mLeft = mLeft - lWidth;
				$("#ann_list_ul").css("margin-left", -mLeft);
			} else {
				$("#ann_list_ul").css("margin-left", -mLeft);
			}
		}
		var int = setInterval(rollText, 30);
		$("#ann_list_ul").hover(function() {
			clearInterval(int);
		}, function() {
			int = setInterval(rollText, 30);
		});
	}
}

$(document).ready(function() {
	setTimeout(function() {
		autoScollAnnList();
	}, 2000);

	doGetTabContent('cos-panel-content', 'Course', wb_utils_controller_base + '/course/learning_center/json/COS/current', '617');
	//doGetTabContent('integ-panel-content', 'INTEGRATED', wb_utils_controller_base + '/course/learning_center/json/INTEGRATED/current', '617');
	//doGetTabContent('exam-panel-content', 'Exam', wb_utils_controller_base + '/course/learning_center/json/EXAM/current', '618');

});