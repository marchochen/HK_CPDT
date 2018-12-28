var courseCenter = {
	cur_tcr_id : Wzb.getUrlParam('tcr_id'),
	cur_tnd_id : Wzb.getUrlParam('tnd_id'),
	cur_ske_id_lst : Wzb.getUrlParam('ske_id_lst'),
	url_failure : Wzb.getUrlParam('url_failure'),
	srh_key : Wzb.getUrlParam('srh_key')
}
var from = 'lrn_center';

new Wzb.PageRenderer({
	url : Wzb.getDisUrl('module', 'JsonMod.Course.CourseModule', 'cmd',
			'get_lrn_center', 'from', from, 'tcr_id', courseCenter.cur_tcr_id,
			'tnd_id', courseCenter.cur_tnd_id, 'ske_id_lst',
			courseCenter.cur_ske_id_lst, 'srh_key', courseCenter.srh_key, 'url_failure', courseCenter.url_failure),

	fn : onready,
	wzbLayout : Wzb.layout.TYPE2
});
var mainData;
function onready(mainStore) {
	Wzb.renderPageTitle(Wzb.l('11'));
	mainData = mainStore.reader.jsonData;
	var tab_pan = Wzb.getTcListTabPanel({
		store : mainStore,
		width : Wzb.util_total_width,
		fn : getCosTab
	});
	tab_pan.add(getSharedTab(-1,Wzb.l('913'),false));
	var main_container = new Wzb.Panel({
		border : false,
		width : Wzb.util_total_width,
		defaults : {
			cellCls : 'valign-top'
		},
		items : [tab_pan]
	});
	main_container.render('container');
	main_container.addClass('bottom');
	main_container.addClass('panel-maincontainer');
	main_container.doLayout();

}
// 构造每个培训中心(tab)里的内容
function getCosTab(tcrId, tcrTitle, isActiveTab) {
	var tcCosPn = getCosAndType(tcrId, tcrTitle, isActiveTab, true);
	// 简单搜索
	var simpleSrh = getSimpleSearchPanel(simpleSeachFn,
			'javascript:Wzb.headerAdvanceSearch(0)', tcrId);
	tcCosPn.insert(0, simpleSrh);
	// 能力分类
	if (mainData['competence_list'] !== undefined
			&& mainData['competence_list'].length > 0) {
		tcCosPn.findById('tcCosPnLeft' + tcrId).insert(0,
				getCompetenceList(tcrId, isActiveTab));
	}
	// 缩小搜索范围
	var reduceSrh = getCosCtrReduceSrhForm(mainData['srh_meta'],
			searchReduceFn, tcrId);
	var p_srh = new Wzb.Panel({
		type : 5,
		title : Wzb.l('60'),
		items : [reduceSrh]
	});
	tcCosPn.findById('tcCosPnLeft' + tcrId).add(p_srh);

	// 如果是简单搜索的搜索结果则回填关键字
	if (isActiveTab && courseCenter.srh_key !== undefined
			&& courseCenter.srh_key !== '') {
		simpleSrh.findById('search_key_id' + tcrId)
				.setValue(courseCenter.srh_key);
		reduceSrh.getComponent(0).setValue(courseCenter.srh_key);
	}

	// 培训中心下搜索栏的搜索function
	function simpleSeachFn(form) {
		var formObj = form.getForm().getValues(false);
		var url = Wzb.setUrlParam('tcr_id', formObj.key0);
		url = Wzb.setUrlParam('srh_key', formObj.srh_key, url);
		window.location.href = url;
	}
	// 缩小搜索范围function
	function searchReduceFn(form) {
		var formObj = form.getForm().getValues(false);
		// 关键字更新到简单搜索中
		simpleSrh.findById('search_key_id' + formObj.tcr_id)
				.setValue(formObj.srh_key);

		var cosListGrid = Ext.getCmp('cosListGrid' + formObj.tcr_id);
		var store = cosListGrid.getStore();
		var searchProxy = new Ext.data.HttpProxy({
			url : Wzb.getDisUrl('module', 'JsonMod.Course.CourseModule', 'cmd',
					'srh_itm', 'from', from, 'tcr_id', formObj.tcr_id,
					'tnd_id_lst', cosListGrid.tnd_id === undefined
							? 0
							: cosListGrid.tnd_id)
				// cosListGrid.tnd_id be set value in course_center_share.js
		});
		store.proxy = searchProxy;
		// 用apply避免清除掉能力分类
		Ext.apply(store.baseParams, formObj);
		store.load({
			params : {
				start : 0,
				limit : cosListGrid.getBottomToolbar().pageSize
			},
			callback : function(r, o, s) {
				setTypeLink(formObj.tcr_id, store.reader.jsonData, true);
			}
		});
	}
	return tcCosPn;

	function getSimpleSearchPanel(searchFn, advSearchHref, key0) {
		var searchPn = new Wzb.FormPanel({
			width : '100%',
			border : false,
			frame : true,
			layout : 'table',
			layoutConfig : {
				columns : 3
			},
			defaults : {
				bodyStyle : Wzb.style_inner_space
			},
			keys : [{
				key : Ext.EventObject.ENTER,
				fn : handlerFn
			}],
			items : [{
				columnWidth : .25,
				items : [{
					xtype : 'textfield',
					id : 'search_key_id' + key0,
					width : 244,
					style : 'margin-left:10px',
					hideLabel : true,
					name : 'srh_key'
				}]
			}, {
				columnWidth : .25,
				items : [new Ext.Button({
					text : Wzb.l('400'),
					cls : 'float-pos1',
					handler : handlerFn
				})]
			}, {
				columnWidth : .25,
				items : [{
					html : '<a class="wzb-common-link" href="' + advSearchHref
							+ '">' + Wzb.l('317') + '</a>',
					border : false
				}]
			}, {
				xtype : 'hidden',
				name : 'key0',
				value : key0
			}]
		});
		searchPn.addClass('bottom');
		function handlerFn(btn, e) {
			searchFn(searchPn);
		}
		return searchPn;
	}
}

// 能力分类下拉列表
function getCompetenceList(tcrId, isActiveTab) {

	var ctStore = new Ext.data.Store({
		proxy : new Ext.data.MemoryProxy(mainData),
		reader : new Ext.data.JsonReader({
			root : 'competence_list'
		}, ['ske_id', 'text'])
	});
	ctStore.load({
		callback : function() {
			ctStore.insert(0, new Ext.data.Record({
				ske_id : '0',
				id : 'all_ske',// 只是用于tree id不冲突
				text : Wzb.l('360')
			}));
			ctStore.insert(0, new Ext.data.Record({
				ske_id : '',
				id : 'no_ske',// 只是用于tree id不冲突
				text : '--' + Wzb.l('51') + '--'
			}));
		}
	});
	var competence = new Wzb.FormPanel({
		type : 4,
		labelAlign : 'top',
		frame : false,
		title : Wzb.l('50'),
		cls : 'bottom',
		bodyStyle : Wzb.style_inner_space,
		hideBorders : true,
		items : [new Wzb.ComboBox({
			store : ctStore,
			width : '200',
			hideLabel : true,
			displayField : 'text',
			valueField : 'ske_id',
			mode : 'local',
			triggerAction : 'all',
			// emptyText : '请选择...',
			editable : false,
			hiddenName : 'ske_id_lst',
			value : isActiveTab ? courseCenter.cur_ske_id_lst : '',
			listeners : {
				select : changeCompt
			}
		})]
	})

	function changeCompt(combo, record, index) {
		var cosListGrid = Ext.getCmp('cosListGrid' + tcrId);
		var store = cosListGrid.getStore();
		// 设置选中的能力分类
		if (store.baseParams === undefined) {
			store.baseParams = {
				ske_id_lst : combo.getValue()
			}
		} else {
			store.baseParams.ske_id_lst = combo.getValue();
		}
		store.load({
			params : {
				start : 0,
				limit : cosListGrid.getBottomToolbar().pageSize

			}
		});
	}
	return competence;
}
// 缩小搜索范围
function getCosCtrReduceSrhForm(srhMetaData, searchFn, tcr_id) {
	var form = Wzb.getReduceSrhForm(srhMetaData, searchFn);
	// 开始日期
	var StartTimePn = {
		autoHeight : true,
		items : [new Wzb.ComboBox({
			store : Wzb.getPeriodAfterStore(),
			hideLabel : true,
			displayField : 'display',
			valueField : 'value',
			typeAhead : false,
			mode : 'local',
			triggerAction : 'all',
			emptyText : Wzb.l('70'),
			editable : false,
			hiddenName : 'srh_start_period'
		})]
	};
	form.add(Wzb.getReduceSrhTitlePn(Wzb.l('265')));
	form.add(Wzb.getItmTypeLst(srhMetaData, new Object({
		itm_dummy_type : '',
		label : '-- ' + Wzb.l(64) +  '--'
	})));
	form.add(Wzb.getReduceSrhTitlePn(Wzb.l('81')));
	form.add(StartTimePn);
	form.add({
		xtype : 'hidden',
		name : 'tcr_id',
		value : tcr_id
	});

	return form;
}

function getSharedTab(tcrId,tcrTitle,isActiveTab) {
	var tcCosPn = getCosAndType(tcrId, tcrTitle, isActiveTab, false);
/*	// 简单搜索
	var simpleSrh = getSimpleSearchPanel(simpleSeachFn,
			'javascript:Wzb.headerAdvanceSearch(0)', tcrId);
	tcCosPn.insert(0, simpleSrh);*/
	// 缩小搜索范围
	var reduceSrh = getCosCtrReduceSrhForm(mainData['srh_meta'],
			searchReduceFn, tcrId);
	var p_srh = new Wzb.Panel({
		type : 5,
		title : Wzb.l('60'),
		items : [reduceSrh]
	});
	tcCosPn.findById('tcCosPnLeft' + tcrId).add(p_srh);
	tcCosPn.findById('tcCosPnLeft' + tcrId).findById('temppan').setVisible(false);
	tcCosPn.findById('navigationPn' + getCusId(tcrId)).setVisible(false);
/*	// 如果是简单搜索的搜索结果则回填关键字
	if (isActiveTab && courseCenter.srh_key !== undefined
			&& courseCenter.srh_key !== '') {
		simpleSrh.findById('search_key_id' + tcrId)
				.setValue(courseCenter.srh_key);
		reduceSrh.getComponent(0).setValue(courseCenter.srh_key);
	}*/

/*	// 培训中心下搜索栏的搜索function
	function simpleSeachFn(form) {
		var formObj = form.getForm().getValues(false);
		var url = Wzb.setUrlParam('tcr_id', formObj.key0);
		url = Wzb.setUrlParam('srh_key', formObj.srh_key, url);
		window.location.href = url;
	}*/
	// 缩小搜索范围function
	function searchReduceFn(form) {
		var formObj = form.getForm().getValues(false);
		// 关键字更新到简单搜索中
//		simpleSrh.findById('search_key_id' + formObj.tcr_id).setValue(formObj.srh_key);

		var cosListGrid = Ext.getCmp('cosListGrid' + formObj.tcr_id);
		var store = cosListGrid.getStore();
		var searchProxy = new Ext.data.HttpProxy({
			url : Wzb.getDisUrl('module', 'JsonMod.Course.CourseModule', 'cmd',
					'srh_itm', 'from', from, 'tcr_id', formObj.tcr_id,
					'tnd_id_lst', cosListGrid.tnd_id === undefined
							? 0
							: cosListGrid.tnd_id)
				// cosListGrid.tnd_id be set value in course_center_share.js
		});
		store.proxy = searchProxy;
		// 用apply避免清除掉能力分类
		Ext.apply(store.baseParams, formObj);
		store.load({
			params : {
				start : 0,
				limit : cosListGrid.getBottomToolbar().pageSize
			},
			callback : function(r, o, s) {
				setTypeLink(formObj.tcr_id, store.reader.jsonData, true);
			}
		});
	}
	return tcCosPn;

	function getSimpleSearchPanel(searchFn, advSearchHref, key0) {
		var searchPn = new Wzb.FormPanel({
			width : '100%',
			border : false,
			frame : true,
			layout : 'table',
			layoutConfig : {
				columns : 3
			},
			defaults : {
				bodyStyle : Wzb.style_inner_space
			},
			keys : [{
				key : Ext.EventObject.ENTER,
				fn : handlerFn
			}],
			items : [{
				columnWidth : .25,
				items : [{
					xtype : 'textfield',
					id : 'search_key_id' + key0,
					width : 244,
					style : 'margin-left:10px',
					hideLabel : true,
					name : 'srh_key'
				}]
			}, {
				columnWidth : .25,
				items : [new Ext.Button({
					text : Wzb.l('400'),
					cls : 'float-pos1',
					handler : handlerFn
				})]
			}, {
				columnWidth : .25,
				items : [{
					html : '<a class="wzb-common-link" href="' + advSearchHref
							+ '">' + Wzb.l('317') + '</a>',
					border : false
				}]
			}, {
				xtype : 'hidden',
				name : 'key0',
				value : key0
			}]
		});
		searchPn.addClass('bottom');
		function handlerFn(btn, e) {
			searchFn(searchPn);
		}
		return searchPn;
	}
}