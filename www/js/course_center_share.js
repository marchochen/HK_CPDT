/**
 * from must defined in parent page(course_center.js /
 * advance_search_result_course.js / search_result.js)
 */
function getCosAndType(tcrId, tcrTitle, isActiveTab, showTndGrid) {
	// 导航栏
	var navigationPn = new Wzb.Panel({
		id : 'navigationPn' + getCusId(tcrId),
		border : false,
		width : Wzb.util_total_width
	})
	navigationPn.addClass('bottom');
	var tcCosPn = new Wzb.Panel({
		title : tcrTitle,
		layout : 'column',
		border : false,
		autoHeight : true,
		defaults : {
			autoHeight : true
		},
		hideBorders : true,
		items : [navigationPn, {
			id : 'tcCosPnLeft' + getCusId(tcrId),
			width : Wzb.util_col_width1,
			cls : 'right',
			defaults : {
				width : Wzb.util_col_width1
			},
			items : [showTndGrid ? getCosTypeGrid(tcrId, isActiveTab) : {id : 'temppan'}]
		}, {
			id : 'tcCosPnRight' + getCusId(tcrId),
			width : Wzb.util_col_width2,
			defaults : {
				width : Wzb.util_col_width2
			},
			items : [getCosListGrid(tcrId, isActiveTab,showTndGrid)]
		}]
	});
	/*
	 * if (isActiveTab){ setSearchMsg(tcrId, mainData); }
	 */
	setNavigation(tcrId, tcrTitle, mainData, isActiveTab);
	return tcCosPn;
}

// 标题列
function renderTitle(value, p, record) {
	var date = '';
	if (record.data.itm_type == Wzb.util_item_type_classroom
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
		if (record.data.recent_start_classes.class_count > 2) {
			datetemp += '...';
		}
		date = Wzb.l('361') + ': ' + datetemp;
	}
	var itmObj = {
		1 : Wzb.getCourseImage(
				record.data.itm_icon, record.data.itm_dummy_type),
		2 : 'course_detail.htm?itm_id=' + record.data.itm_id,
		3 : record.data.itm_title,
		label_itm_type : Wzb.l('265') + ':',
		4 : record.data.lab_itm_type,
		5 : record.data.itm_desc,
		6 : date
	};
	var str = WzbHtm.share_renderTitle(itmObj);
	return str;
}
// 评分列
function renderGrade(value, p, record) {
	if (record.data.itm_dummy_type.indexOf('EXAM') == -1
			&& record.data.itm_dummy_type.indexOf('REF') == -1) {
		var startDimObj = {
			1 : Wzb.getCommonImagePath('star_dim.gif')
		}
		var startObj = {
			1 : Wzb.getCommonImagePath('star.gif')
		}
		var str = '';
		value = Math.round(value);
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
// 培训列表gridPanel
function getCosListGrid(tcrId, isActiveTab, showTndGrid) {
	var cm = new Ext.grid.ColumnModel([{
		header : Wzb.l('79'),
		dataIndex : 'itm_title',
		width : Wzb.util_col_width2 * 0.8,
		valign : 'middle',
		renderer : renderTitle
	}, {
		header : Wzb.l('80'),
		dataIndex : 'itm_comment_avg_score',
		width : Wzb.util_col_width2 * 0.2,
		renderer : renderGrade
	}]);
	var cosListReader = new Ext.data.JsonReader({
		totalProperty : 'items["total"]',
		root : 'items["itm_lst"]'
	}, ['itm_id', 'itm_type', 'lab_itm_type', 'itm_title', 'itm_icon',
			'start_date', 'itm_comment_avg_score', 'itm_desc',
			'itm_dummy_type', 'recent_start_classes']);

	// isActiveTab只有在课程目录才有用(有培训中心)，如果是高级搜索的话直接给true(因为没有培训中心).
	var cosListProxy = null;
	var cosListProxyUrl;
	if (from === 'lrn_center') {
		cosListProxyUrl = Wzb.getDisUrl('module',
				'JsonMod.Course.CourseModule', 'cmd', 'get_lrn_center', 'from',
				from, 'tcr_id', tcrId, 'srh_key', courseCenter.srh_key);
	} else if (from === 'adv_itm_srh') {
		// 在高级搜索页面search_result.js页面取得search_id
		cosListProxyUrl = Wzb.getDisUrl('module',
				'JsonMod.Course.CourseModule', 'cmd', 'srh_itm', 'from', from,
				'search_id', search_id);
	}
	if (isActiveTab) {
		cosListProxy = new Ext.data.MemoryProxy(mainData);
	} else {
		cosListProxy = new Ext.data.HttpProxy({
			url : cosListProxyUrl
		});
	}
	var cosListStore = new Ext.data.Store({
		proxy : cosListProxy,
		reader : cosListReader
	});
	/*
	if (from === 'lrn_center') {
		cosListStore.baseParams = {
			ske_id_lst : courseCenter.cur_ske_id_lst
		}
	}*/
	var bbar = Wzb.getPagingToolbar(cosListStore);
	var cosListGrid = new Wzb.GridPanel({
		type : 1,
		id : 'cosListGrid' + getCusId(tcrId),
		title : Wzb.l('78'),
		autoHeight : true,
		store : cosListStore,
		cm : cm,
		loadMask : true,
		bbar : bbar,
		isChangeProxy : isActiveTab,
		url : cosListProxyUrl,
		viewConfig : {
			emptyText : Wzb.getEmptyText()
		}

	});
	cosListGrid.addListener('render', function(component) {
		// setDetailsBtn(component);
		Wzb.renderEventFn(component);
	})
	if (showTndGrid != false) {
		// 类别GridPanel中的store是从培训列表GridPanel的store取的(即只发送一个请求)
		cosListStore.addListener('load', function(s, r, o) {
			// 更新类别列表
			var store = Ext.getCmp('cosTypeGrid' + getCusId(tcrId)).getStore();
			store.proxy = new Ext.data.MemoryProxy(s.reader.jsonData);
			store.load();
		})
	}
	return cosListGrid;
}
// 在分页栏中加入"详细资料"按钮
function setDetailsBtn(component) {
	var details = Wzb.l('363');
	var simplify_info = Wzb.l('364');

	var btn = new Ext.Button({
		text : simplify_info,
		pressed : true,
		enableToggle : true,
		toggleHandler : toggleDetails
	});

	component.getBottomToolbar().add('-', btn);

	function toggleDetails(btn, pressed) {
		if (pressed) {
			btn.setText(simplify_info);
		} else {
			btn.setText(details);
		}
		btn.pressed = pressed;
		var view = component.getView();
		view.showPreview = pressed;
		view.refresh();
	}
}
// 类别列表gridPanel
function getCosTypeGrid(tcrId, isActiveTab) {

	// 类别列()
	function renderTypeCos(value, p, record) {
		var tndObj = {
			1 : Wzb.getFolderImagePath(),
			2 : 'javascript:getCosInTnd(' + tcrId + ','
					+ record.data.tnd_id + ')',
			3 : record.data.tnd_title,
			4 : '',
			5 : record.data.count
		}
		var str = WzbHtm.share_renderType(tndObj);
		return str;
	}
	function renderTypeAdv(value, p, record) {
		var tndObj = {
			1 : Wzb.getFolderImagePath(),
			2 : 'javascript:getCosInTnd(' + tcrId + ','
					+ record.data.tnd_id + ')',
			3 : record.data.tnd_title,
			4 : record.data.tcr_title !== undefined
					? record.data.tcr_title
					: '',
			5 : record.data.count
		}
		var str = WzbHtm.share_renderType(tndObj);
		return str;
	}

	var cosTypeReader = new Ext.data.JsonReader({
		root : 'tnd["sub_tnd_lst"]'
	}, ['tnd_id', 'tcr_id', 'tnd_title', 'tcr_title', 'count']);

	var typeStore = new Ext.data.Store({
		proxy : new Ext.data.MemoryProxy(),// 默认为空
		// load的callback中添加
		autoLoad : true,
		reader : cosTypeReader
	});
	var renderer;
	if (from === 'lrn_center') {
		renderer = renderTypeCos;
	} else if (from === 'adv_itm_srh') {
		renderer = renderTypeAdv;
	}
	var cosTypeGrid = new Wzb.GridPanel({
		id : 'cosTypeGrid' + getCusId(tcrId),
		store : typeStore,
		hideHeaders : true,
		autoHeight : true,
		columns : [{
			id : 'name',
			width : Wzb.util_col_width1,
			sortable : true,
			dataIndex : 'tnd_title',
			renderer : renderer
		}],
		viewConfig : {
			emptyText : Wzb.getEmptyText()
		}
	});
	// 类别框
	var typePn = new Wzb.Panel({
		title : Wzb.l('365'),
		type : 4,
		hideBorders : true,
		border : true,
		cls : 'bottom',
		items : [cosTypeGrid]
	});
	// 如果是高级搜索结果则没有typeLink
	if (from === 'lrn_center') {
		var typeLinkPn = new Wzb.Panel({
			id : 'typeLinkPn' + getCusId(tcrId),
			hideBorders : true,
			border : false
		});
		typePn.add(typeLinkPn);
		setTypeLink(tcrId, mainData, isActiveTab);
	}
	return typePn;
}
function getCosInTnd(tcr_id, tnd_id) {
	var cosListGrid = Ext.getCmp('cosListGrid' + getCusId(tcr_id));
	// set the tnd id for the reduceSrh
	cosListGrid.tnd_id = tnd_id;
	var store = cosListGrid.getStore();
	store.proxy.conn.url = Wzb.setUrlParam('tnd_id', tnd_id, store.proxy.conn.url);
	store.proxy.conn.url = Wzb.setUrlParam('tnd_id_lst', tnd_id,
			store.proxy.conn.url);
	store.load({
		params : {
			from : from,
			tcr_id : tcr_id,
			tnd_id : tnd_id,
			tnd_id_lst : tnd_id,
			start : 0,
			// for simple search
			srh_key : from === 'lrn_center' ? courseCenter.srh_key : '',
			limit : cosListGrid.getBottomToolbar().pageSize
		},
		callback : function(r, o, s) {
			var title;
			if(store.reader.jsonData['cur_tcr'] !== undefined) {
			if (from === 'lrn_center') {
				title = store.reader.jsonData['cur_tcr']['tcr_title'];
			} else if (from === 'adv_itm_srh') {
				title = Wzb.l('360');
			}

			setNavigation(tcr_id,
					store.reader.jsonData['cur_tcr'] === undefined
							? ''
							: store.reader.jsonData['cur_tcr']['tcr_title'],
					store.reader.jsonData, true);
			if (from === 'lrn_center') {
				setTypeLink(tcr_id, store.reader.jsonData, true);
			}
			}
		}
	});
}
// 在培训列表上方显示本次搜索的条件(如果是搜索操作)
/*
 * function setSearchMsg(tcrId, criteria) { var html = ''; if (criteria !==
 * undefined && criteria !== null) { if (criteria.srh_keyword !== undefined &&
 * criteria.srh_keyword !== '') { html += '在"' +
 * mainData['tnd_lst']['parent_node'].name + '"找到' + mainData['itm'].total +
 * '个符合[' + criteria.srh_keyword + ']的培训'; }
 * 
 * if (criteria.srh_itm_type_lst !== undefined &&
 * criteria.srh_itm_type_lst.length > 0) {
 * 
 * html += '<br>类型：'; for (var i = 0; i < criteria.srh_itm_type_lst.length;
 * i++) { html += Wzb.l(criteria.srh_itm_type_lst[i]) + ' '; } } if
 * (criteria.srh_lang !== undefined && criteria.srh_lang.length > 0) { html += '<br>语言：';
 * for (var i = 0; i < criteria.srh_lang.length; i++) { html +=
 * Wzb.l(criteria.srh_lang[i]) + ' '; } } if (criteria.srh_start_period
 * !== undefined) { html += '<br>开始日期：'; }
 * 
 * var searchCrPn = new Wzb.Panel({ border : false, html : html }); //
 * 如果是搜索结果则在培训列表上面显示搜索条件 var tcCosPnRight = Ext.getCmp('tcCosPnRight'+
 * getCusId(tcrId)); if(tcCosPnRight.items.length > 1){
 * tcCosPnRight.items.removeAt(0); } tcCosPnRight.items.insert(0, searchCrPn); } }
 */
// 更新导航
function setNavigation(tcrId, tcrTitle, storeData, isActiveTab) {

	var navigationStr = '';
	var navLinkData = storeData['nav_link'];
	var hasNav = false;
	if (navLinkData !== undefined && navLinkData !== null && isActiveTab) {
		hasNav = true;
	}

	if (from === 'lrn_center') {
		if (hasNav) {
			navigationStr = WzbHtm.share_navigation({
				1 : 'javascript:getCosInTnd(' + tcrId + ',0)',
				2 : tcrTitle
			});
		} else {
			navigationStr = '<span class="wzb-common-text">' + tcrTitle
					+ '</span>';
		}
	} else if (from === 'adv_itm_srh') {
		if (storeData['cur_tcr'] !== undefined && storeData['cur_tcr'] !== null) {
			if (tcrTitle === undefined || tcrTitle.length == 0) {
				tcrTitle = storeData['cur_tcr']['tcr_title'];
			}
			navigationStr = WzbHtm.share_navigation({
				1 : 'javascript:getCosInTnd(0,0)',
				2 : Wzb.l('366')
			});
			if (hasNav) {
				navigationStr += Wzb.nav_path_sep + WzbHtm.share_navigation({
					1 : 'javascript:getCosInTnd('
							+ storeData['cur_tcr']['tcr_id'] + ',0)',
					2 : '<span class="wzb-common-link">'
							+ tcrTitle + '</span>'
				});
			} else {
				navigationStr += Wzb.nav_path_sep
						+ '<span class="wzb-common-text">' + tcrTitle
						+ '</span>';
			}
		} else {
			navigationStr = '<span class="wzb-common-text">'
					+ Wzb.l('366') + '</span>';
		}
	}
	if (hasNav) {
		var parentNav = navLinkData['parent_nav'];
		if (parentNav !== undefined) {
			for (var i = 0; i < parentNav.length; i++) {
				navigationStr += Wzb.nav_path_sep + WzbHtm.share_navigation({
					1 : 'javascript:getCosInTnd(' + tcrId + ','
							+ parentNav[i]['tnd_id'] + ')',
					2 : parentNav[i]['tnd_title']
				})
			}
		}
		if (navLinkData['cur_node'] !== undefined) {
			navigationStr += Wzb.nav_path_sep + '<span class="wzb-common-text">'
					+ navLinkData['cur_node']['tnd_title'] + '</span>';
		}
	}

	var nav = Ext.getCmp('navigationPn' + getCusId(tcrId));
	if (nav.findById('navigation' + getCusId(tcrId)) !== null) {
		nav.remove('navigation' + getCusId(tcrId));
	}
	nav.add({
		id : 'navigation' + getCusId(tcrId),
		border : false,
		html : navigationStr
	});
	nav.doLayout();

}
// just use in from='lrn_center'
function setTypeLink(tcrId, storeData, isActiveTab) {

	var hasNav = false;// 是否有父目录
	var navLinkData = storeData['nav_link'];
	if (navLinkData !== undefined && navLinkData !== null && isActiveTab) {
		hasNav = true;
	}
	var isSrh = false;// 是否是搜索结果
	var srhCriData = storeData['srh_criteria'];
	if (srhCriData !== undefined && srhCriData !== null && isActiveTab) {
		isSrh = true;
	}

	var typeLinkObj = {
		1 : '',
		2 : '',
		3 : '',
		4 : 'catalog_list.htm?tcr_id=' + tcrId,
		5 : Wzb.l('56')
	};
	if (isActiveTab) {
		if (isSrh) {
			typeLinkObj['4'] = '';
			typeLinkObj['5'] = '';
			if (hasNav) {
				typeLinkObj['2'] = Wzb.l('367');
				if (storeData['tnd']['parent_node'] !== undefined
						&& storeData['tnd']['parent_node'] !== null) {
					typeLinkObj['1'] = 'javascript:getCosInTnd('
							+ tcrId + ','
							+ storeData['tnd']['parent_node']['tnd_id'] + ')';
				} else {
					typeLinkObj['1'] = 'javascript:getCosInTnd('
							+ tcrId + ',0)';
				}
				typeLinkObj['3'] = '|';
				typeLinkObj['4'] = 'javascript:getCosInTnd('
						+ tcrId + ',0)';
				typeLinkObj['5']  = Wzb.l('368');
			}
		} else {
			if (hasNav) {
				typeLinkObj['3'] = '|';
				typeLinkObj['2'] = Wzb.l('369');
				if (storeData['tnd']['parent_node'] !== undefined
						&& storeData['tnd']['parent_node'] !== null) {
					typeLinkObj['1'] = 'javascript:getCosInTnd('
							+ tcrId + ','
							+ storeData['tnd']['parent_node']['tnd_id'] + ')';
				} else {
					typeLinkObj['1'] = 'javascript:getCosInTnd('
							+ tcrId + ',0)';
				}
			}
		}
	}
	var typeLinkStr = WzbHtm.typeLink(typeLinkObj);
	var link = Ext.getCmp('typeLinkPn' + getCusId(tcrId));
	if (link != null && link.findById('typeLink' + getCusId(tcrId)) !== null) {
		link.remove('typeLink' + getCusId(tcrId));
	}
	if (link != null) {
		link.add(new Wzb.Panel({
			id : 'typeLink' + getCusId(tcrId),
			border : false,
			html : typeLinkStr,
			bodyStyle : Wzb.style_inner_space
		}));
		link.doLayout();
	}
}
// 这个Id不代表当前培训中心Id,只是用来标示特定的gardget，因为高级搜索的结果页面是不区分培训中心的，所以在高级搜索结果页面这个Id一直等于0
function getCusId(tcrId) {
	if (from !== undefined && from === 'adv_itm_srh') {
		return 0;
	} else {
		return tcrId;
	}
}