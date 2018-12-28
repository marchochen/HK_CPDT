function getNominateCourse(showInMyStaff, userId, dataStore, pending_plan) {
	var panelWidth = Wzb.util_total_width;
	if (showInMyStaff !== undefined && showInMyStaff !== '' && showInMyStaff) {
		panelWidth = Wzb.util_col_width2;
	} else {
		showInMyStaff = false;
	}

	function renderNominateTitle(value, metadata, record, rowIndex, colIndex,
			store) {
		var date = '';
		var label_date = '';
		if (record.data.pitm_type == Wzb.util_item_type_classroom
				&& record.data.recent_start_classes !== undefined
				&& record.data.recent_start_classes.start_classes !== undefined) {
			var datetemp = '';
			for (var i = 0; i < record.data.recent_start_classes.start_classes.length; i++) {
				if (i > 0) {
					datetemp += ', ';
				}

				datetemp += Wzb.displayTime(
						record.data.recent_start_classes.start_classes[i].start_date,
						Wzb.time_format_ymd)
						+ ' ('
						+ record.data.recent_start_classes.start_classes[i].itm_title
						+ ')';
			}
			if(record.data.recent_start_classes.class_count > 2){
				datetemp += '...';
			}
			label_date = Wzb.l('361') + ': '
			date = datetemp;
		}
		var pitmObj = {
			1 : Wzb.getCourseImage(record.data.pitm_icon,
					record.data.pitm_dummy_type),
			2 : 'course_detail.htm?itm_id=' + record.data.pitm_id,
			3 : record.data.pitm_title,
			4 : record.data.label_pitm_type,
			5 : record.data.pitm_desc,
			6 : label_date,
			7 : date
		};
		var str = WzbHtm.share_renderNominateTitle(pitmObj);
		return str;
	}
	function renderNominatePlanInd(value, metadata, record, rowIndex, colIndex,
			store) {
		var str = '';
		if (!showInMyStaff) {
			if (record.data.plan_ind) {
				str = '<span class=\'wzb-common-text\'>' + Wzb.l('430')
						+ '</span>';
			} else {
				var planObj = {
					1 : 'javascript:addItemToPlan('
							+ record.data.pitm_id + ',' + userId + ')',
					2 : Wzb.getHtmImgSrc(Wzb.getCommonImagePath('ico_add_to_plan.gif')),
					3 : Wzb.l('359')
				}
				str = WzbHtm.renderNominatePlanInd(planObj);
			}
		}
		return str;
	}
	function renderStatus(value, metadata, record, rowIndex, colIndex, store) {
		var str = '<span class=\'wzb-common-text\'>'
				+ Wzb.getLearningStatus(record.data.ats_cov_status,
						record.data.status, record.data.pitm_ref_ind)
				+ '</span>'
		return str;
	}
	function getWidthForMyStaff() {
		var width = panelWidth * 0.1;
		if (showInMyStaff) {
			width = panelWidth * 0.2;
		}
		return width;
	}
	var colArray = [{
		header : Wzb.l('79'),
		resizable : false,
		dataIndex : 'pitm_title',
		width : panelWidth * 0.6,
		renderer : renderNominateTitle
	}, {
		header : Wzb.l('80'),
		dataIndex : 'pitm_comment_avg_score',
		width : getWidthForMyStaff(),
		renderer : renderGrade
	}, {
		header : Wzb.l('100'),
		resizable : false,
		dataIndex : 'status',
		width : getWidthForMyStaff(),
		renderer : renderStatus
	}]
	if (!showInMyStaff) {
		colArray.push({
			header : '',
			resizable : false,
			dataIndex : 'plan_ind',
			width : panelWidth * 0.2,
			renderer : renderNominatePlanInd
		});
	}
	var colModel = new Ext.grid.ColumnModel(colArray);

	var reader = new Ext.data.JsonReader({
		// totalProperty : 'recommended_itm_lst["total"]',
		root : 'recommended_itm_lst'
	}, ['pitm_type', 'pitm_id', 'pitm_title', 'pitm_icon', 'pitm_ref_ind',
			'label_pitm_type', 'pitm_dummy_type', 'pitm_tcr_id',
			'pitm_tcr_title', 'pitm_desc', 'status', 'appn_start_timestamp',
			'appn_end_timestamp', 'maxEnd_timestamp', 'minStart_timestamp',
			'att_timestamp', 'ats_cov_status', 'pitm_retake_ind', 'plan_ind',
			'recent_start_classes', 'pitm_comment_avg_score','pitm_exam_ind']);

	var nomonateStore = new Ext.data.Store({
		// remoteSort : true,
		proxy : showInMyStaff ? new Ext.data.HttpProxy({
			url : Wzb.getDisUrl('module', 'JsonMod.study.StudyModule', 'cmd',
					'get_my_cos', 'pending_plan', pending_plan, 'usr_ent_id',
					userId)
		}) : new Ext.data.MemoryProxy(dataStore.reader.jsonData),
		reader : reader
	});
	var nomonateGridPan = new Wzb.GridPanel({
		title : Wzb.l('270'),
		type : 1,
		border : true,
		store : nomonateStore,
		colModel : colModel,
		width : panelWidth,
		autoHeight : true,
		isChangeProxy : true,
		url : Wzb.getDisUrl('module', 'JsonMod.study.StudyModule', 'cmd',
				'get_my_cos', 'pending_plan', pending_plan, 'usr_ent_id',
				userId),
		viewConfig : {
			emptyText : Wzb.getEmptyText()
		}
	});
	nomonateGridPan.addListener({
		'render' : Wzb.renderEventFn
	})

	return nomonateGridPan;
}

// 评分列
function renderGrade(value, p, record) {
	if (record.data.pitm_ref_ind == false && record.data.pitm_exam_ind == false) {
		var startDimObj = {
			1 : Wzb.getCommonImagePath('star_dim.gif')
		}
		var startObj = {
			1 : Wzb.getCommonImagePath('star.gif')
		}
		var str = '';
		value = Math.round(value);
		if (!value) {
			value = 0;
		}
		for (var i = 1; i <= value; i++) {
			str += WzbHtm.share_renderGrade(startDimObj);
		}
		for (var i = 1; i <= 5 - value; i++) {
			str += WzbHtm.share_renderGrade(startObj);
		}
	} else {
		str = '--';
	}

	return str;

}

function addItemToPlan(pitm_id, userId) {
	Ext.MessageBox.buttonText.yes = Wzb.l('329');
	Ext.MessageBox.buttonText.no = Wzb.l('330');
	if (confirm(Wzb.l('451'))) {
		var url_success = window.location.href;
		var url_failure = window.location.href;
		window.location.href = Wzb.getDisUrl('module',
				'JsonMod.study.StudyModule', 'cmd', 'ins_lrn_soln', 'itm_id',
				pitm_id, 'usr_ent_id', userId, 'url_success', url_success,
				'url_failure', url_failure);
	}
}