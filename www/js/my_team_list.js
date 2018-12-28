new Wzb.PageRenderer({
	url : Wzb.getDisUrl('module', 'JsonMod.studyGroup.StudyGroupModule', 'cmd',
			'get_std_grp'),

	fn : onready,
	wzbLayout : Wzb.layout.TYPE3
});

function onready(mainStore) {
	var mainData = mainStore.reader.jsonData;
	Wzb.renderPageTitle(Wzb.l('34'));

	var colModel = new Ext.grid.ColumnModel([{
		menuDisabled : true,
		resizable : false,
		dataIndex : 'sgp_title',
		renderer : renderTitle,
		width : Wzb.util_col_width2
	}]);
	var colModelSearch = new Ext.grid.ColumnModel([{
		menuDisabled : true,
		resizable : false,
		dataIndex : 'sgp_title',
		renderer : renderTitleSearch,
		width : Wzb.util_col_width1
	}]);
	function renderTitleSearch(value, metadata, record, rowIndex, colIndex,
			store) {
		var sObj = {
			1 : '',
			4 : record.data.sgp_desc,
	//		label_add : '',
			2 : '',
			3 : ''
			
		};
		if (record.data.can_view) {
			sObj['1'] = '<a class="wzb-common-link" href="'
					+ 'my_team_details.htm?sgp_id=' + record.data.sgp_id + '">'
					+ value + '</a>';
		} else {
			sObj['1'] = '<span class="wzb-common-text">' + value
					+ '</span>';
			var toolTipInnerHTML = _getManagerEamail(record);
			if (toolTipInnerHTML != null && toolTipInnerHTML.length > 0) {
				sObj['2'] = Wzb.l('469') + ':';
			}
			sObj['3'] = toolTipInnerHTML;
		}
		var str = WzbHtm.renderTitleSearch(sObj);
		return str;
	}
	function renderTitle(value, metadata, record, rowIndex, colIndex, store) {

		var obj = {
			1 : '',
			6 : '',
			7 : Wzb.l('170', [record.data.sgm_member_total]),
			8 : Wzb.l('171', [record.data.sgr_topic_total]),
			9 : Wzb.l('172', [record.data.sgr_res_total]),
//			label_add : '',
//			toolTipInnerHTML : '',
			2 : 'displayNone',
			5 : 'displayNone'
		};
		if (record.data.sgp_desc !== undefined
				&& record.data.sgp_desc.length > 0) {
			obj['6'] = record.data.sgp_desc;
			obj['5'] = '';
		}
		if (record.data.can_view) {
			obj['1'] = '<a class="wzb-title-link" href="'
					+ 'my_team_details.htm?sgp_id=' + record.data.sgp_id + '">'
					+ value + '</a>';
		} else {
			obj['1'] = '<span class="wzb-common-text">' + value
					+ '</span>';

			var toolTipInnerHTML = _getManagerEamail(record);
			if (toolTipInnerHTML != null && toolTipInnerHTML.length > 0) {
				obj['3'] = Wzb.l('469') + ': ';
				obj['2'] = '';
			}
			obj['4'] = toolTipInnerHTML;
		}
		var str = WzbHtm.renderTitle(obj);
		return str;

	}
	function _getManagerEamail(record) {
		var toolTipInnerHTML = '';
		// 只显示一个email
		if (record.data.mgtVc !== undefined && record.data.mgtVc.length > 0) {
			for (var i = 0; i < record.data.mgtVc.length; i++) {
				if (record.data.mgtVc[i].usr_email !== undefined
						&& record.data.mgtVc[i].usr_email !== '') {
					toolTipInnerHTML += record.data.mgtVc[i].usr_email;
					break;
				}
			}
		}
		return toolTipInnerHTML;
	}

	var sgpPanelLst = new Array();
	var jsonMgtSgp = mainStore.reader.jsonData['mgt_sgp']['sgp_lst'];
	if (jsonMgtSgp != null && jsonMgtSgp != undefined && jsonMgtSgp != '') {
		sgpPanelLst.push(getMgtSgpList());
	}
	sgpPanelLst.push(getJoinSgpList());
	sgpPanelLst.push(getPubSgpList());
	var leftPanel = new Wzb.Panel({
		id : 'leftPart',
		title : '',
		autoHeight : true,
		width : Wzb.util_col_width2,
		defaults : {
			width : Wzb.util_col_width2,
			autoHeight : true
		},
		items : sgpPanelLst
	});
	leftPanel.addClass('right');

	var main_container = new Wzb.Panel({
		layout : 'column',
		hideBorders : true,
		border : false,
		width : Wzb.util_total_width,
		defaults : {
			cellCls : 'valign-top'
		},
		items : [leftPanel, new Wzb.Panel({
			type : 5,
			title : Wzb.l('169'),
			id : 'rightPart',
			width : Wzb.util_col_width1,
			autoHeight : true,
			defaults : {
				autoHeight : true,
				width : Wzb.util_col_width1
			},
			border : true,
			items : [getSearchPn(), getSearchResultGrid()]
		})]
	});
	main_container.render('container');
	main_container.addClass('bottom');
	main_container.addClass('panel-maincontainer');
	main_container.doLayout();

	// 管理中的学习小组
	function getMgtSgpList() {
		var reader = new Ext.data.JsonReader({
			totalProperty : 'mgt_sgp["total"]',
			root : 'mgt_sgp["sgp_lst"]'
		}, ['sgp_id', 'sgp_title', 'sgp_desc', 'sgp_public_type',
				'sgm_member_total', 'sgr_topic_total', 'sgr_res_total',
				'can_view']);

		var manageStore;
		manageStore = new Ext.data.Store({
			remoteSort : true,
			proxy : new Ext.data.MemoryProxy(mainStore.reader.jsonData),
			reader : reader
		});

		var bbar = Wzb.getPagingToolbar(manageStore);
		var manageGridPn = new Wzb.GridPanel({
			// id : 'manageGridPn',
			type : 4,
			title : Wzb.l('166'),
			store : manageStore,
			colModel : colModel,
			bbar : bbar,
			hideHeaders : true,
			url : Wzb
					.getDisUrl('module', 'JsonMod.studyGroup.StudyGroupModule',
							'cmd', 'get_mgt_sgp'),
			isChangeProxy : true,
			viewConfig : {
				emptyText : Wzb.getEmptyText()
			}
		});
		manageGridPn.addListener({
			'render' : Wzb.renderEventFn
		});
		manageGridPn.addClass('bottom');
		return manageGridPn;
	}

	// 已参加的学习小组
	function getJoinSgpList() {
		var reader = new Ext.data.JsonReader({
			totalProperty : 'join_sgp["total"]',
			root : 'join_sgp["sgp_lst"]'
		}, ['sgp_id', 'sgp_title', 'sgp_desc', 'sgp_public_type',
				'sgm_member_total', 'sgr_topic_total', 'sgr_res_total',
				'can_view']);

		var joinStore;
		joinStore = new Ext.data.Store({
			remoteSort : true,
			proxy : new Ext.data.MemoryProxy(mainStore.reader.jsonData),
			reader : reader
		});

		var bbar = Wzb.getPagingToolbar(joinStore);
		var joinGridPn = new Wzb.GridPanel({
			// id : 'joinGridPn',
			type : 1,
			title : Wzb.l('167'),
			store : joinStore,
			colModel : colModel,
			bbar : bbar,
			hideHeaders : true,
			url : Wzb.getDisUrl('module',
					'JsonMod.studyGroup.StudyGroupModule', 'cmd',
					'get_join_sgp'),
			isChangeProxy : true,
			viewConfig : {
				emptyText : Wzb.getEmptyText()
			}
		});
		joinGridPn.addListener({
			'render' : Wzb.renderEventFn
		});
		joinGridPn.addClass('bottom');
		return joinGridPn;
	}
	// 其它公开的学习小组
	function getPubSgpList() {
		var reader = new Ext.data.JsonReader({
			totalProperty : 'pub_sgp["total"]',
			root : 'pub_sgp["sgp_lst"]'
		}, ['sgp_id', 'sgp_title', 'sgp_desc', 'sgp_public_type',
				'sgm_member_total', 'sgr_topic_total', 'sgr_res_total',
				'mgtVc', 'can_view']);

		var otherStore;
		otherStore = new Ext.data.Store({
			remoteSort : true,
			proxy : new Ext.data.MemoryProxy(mainStore.reader.jsonData),
			reader : reader
		});

		var bbar = Wzb.getPagingToolbar(otherStore);
		var otherGridPn = new Wzb.GridPanel({
			// id : 'otherGridPn',
			type : 5,
			title : Wzb.l('168'),
			store : otherStore,
			colModel : colModel,
			bbar : bbar,
			hideHeaders : true,
			url : Wzb.getDisUrl('module',
					'JsonMod.studyGroup.StudyGroupModule', 'cmd',
					'get_other_sgp'),
			isChangeProxy : true,
			viewConfig : {
				emptyText : Wzb.getEmptyText()
			}
		});
		otherGridPn.addListener({
			'render' : Wzb.renderEventFn
		});
		return otherGridPn;
	}

	// 搜索条件
	function getSearchPn() {
		var searchPn = new Wzb.FormPanel({
			labelAlign : 'left',
			frame : false,
			bodyStyle : Wzb.style_inner_space,
			border : false,
			keys : [{
				key : Ext.EventObject.ENTER,
				fn : handlerFn
			}],
			items : [{
				xtype : 'textfield',
				width : 180,
				hideLabel : true,
				clearCls : 'allow-float',
				itemCls : 'float-pos1',
				id : 'sgp_search_key'
			}, new Ext.Button({
				text : Wzb.l('400'),
				cls : 'float-pos2',
				handler : handlerFn
			})]
		})
		function handlerFn() {
			var sgp_search_key = Ext.getDom('sgp_search_key').value;
			var srhStore = Ext.getCmp('searchResultGrid').getStore();
			if (sgp_search_key != null && sgp_search_key.length > 0) {
				srhStore.baseParams = {
					sgp_search_key : sgp_search_key
				};
				srhStore.load({
					params : {
						start : 0,
						limit : 10
					}
				});
			} else {
				alert(Wzb.l('472'));
			}
		}
		searchPn.addClass('bottom');
		return searchPn;
	}
	// 搜索结果
	function getSearchResultGrid() {
		var reader = new Ext.data.JsonReader({
			totalProperty : 'srh_sgp["total"]',
			root : 'srh_sgp["sgp_lst"]'
		}, ['sgp_id', 'sgp_title', 'sgp_desc', 'sgp_public_type',
				'sgm_member_total', 'sgr_topic_total', 'sgr_res_total',
				'mgtVc', 'can_view']);

		var searchStore = new Ext.data.Store({
			remoteSort : true,
			proxy : new Ext.data.HttpProxy({
				url : Wzb.getDisUrl('module',
						'JsonMod.studyGroup.StudyGroupModule', 'cmd',
						'search_std_grp')

			}),
			reader : reader
		});
		var bbar = Wzb.getPagingToolbar(searchStore, 2);
		var searchResultGrid = new Wzb.GridPanel({
			type : 5,
			id : 'searchResultGrid',
			colModel : colModelSearch,
			store : searchStore,
			border : false,
			hideHeaders : true,
			bbar : bbar,
			viewConfig : {
				emptyText : Wzb.getEmptyText()
			}
		});
		return searchResultGrid;
	}
}