new Wzb.PageRenderer({
	url :  Wzb
			.getDisUrl('module', 'JsonMod.ForumModule', 'cmd', 'get_public_forum'),
	fn : onready
	,wzbLayout : Wzb.layout.TYPE3
});

function onready(mainStore) {
	Wzb.renderPageTitle(Wzb.l('32'));
	mainContainer = new Wzb.Panel({
		border : false,
		layout : 'column',
		width : Wzb.util_total_width
	});
		var reader = new Ext.data.JsonReader({
			totalProperty : 'forum["total"]',
			root : 'forum["for_topic_lst"]'
		}, ['for_res_id', 'for_res_title', 'fto_total', 'fmg_total',
				'fmg_unread_total']);
		forumStore = new Ext.data.Store({
			remoteSort : true,
			proxy : new Ext.data.MemoryProxy(mainStore.reader.jsonData),
			reader : reader
		});
		var colModel = new Ext.grid.ColumnModel([{
			renderer : renderimg,
			width : Wzb.util_col_width2 * 0.05
		},{
			renderer : renderTitle,
			header : Wzb.l('325'),
			width : Wzb.util_col_width2 * 0.40
		}, {
			header : Wzb.l('403'),
			dataIndex : 'fto_total',
			renderer : renderfont,
			align : 'center',
			width : Wzb.util_col_width2 * 0.1
		}, {
			header : Wzb.l('431'),
			renderer : renderfont,
			align : 'center',
			dataIndex : 'fmg_total',
			width : Wzb.util_col_width2 * 0.15
		},{
			header : Wzb.l('462'),
			align : 'center',
			renderer : renderfont,
			dataIndex : 'fmg_unread_total',
			width : Wzb.util_col_width2 * 0.30
		}]);
	var forumBbar = Wzb.getPagingToolbar(forumStore);
	var forumGrid = new Wzb.GridPanel({
		store : forumStore,
		colModel : colModel,
		viewConfig : { emptyText : Wzb.getEmptyText()},
		autoHeight : true,
		width : Wzb.util_col_width2,
		bbar : forumBbar,
		border : false
		});
	forumGrid.addListener('render', function() {
		forumStore.load();
		forumStore.proxy = new Ext.data.HttpProxy({
			url : Wzb.getDisUrl('module', 'JsonMod.ForumModule', 'cmd', 'get_public_forum')
		});
	});
	var forumPanel = new Wzb.Panel({
			width : Wzb.util_col_width2 - 3,
//			bbar : forumBbar,
			type : 1
		});
		forumPanel.addClass('right');
		forumPanel.add(forumGrid);
	var qlObj = {
		1 : Wzb.l('161'),
		2 : Wzb.l('162') + Wzb.l('11'),
		3 : Wzb.l('162') + Wzb.l('16'),
		4 : Wzb.l('162') + Wzb.l('21'),
		5 : '',
		6 : Wzb.l('162') + Wzb.l('345')
	};
	if(Wzb.has_my_team_view){
		qlObj['5'] = Wzb.l('162') + Wzb.l('34');
	}
	var quickLinkPn = new Wzb.Panel({
		title : Wzb.l('160'),
		autoHeight : true,
		type : 5,
		width : Wzb.util_col_width1,
		border : true,
		bodyStyle : Wzb.style_inner_space,
		html : WzbHtm.share_quickLinkPn(qlObj)
	});
	mainContainer.add(forumPanel);
	mainContainer.add(quickLinkPn)
	mainContainer.render('container');
	mainContainer.addClass('bottom');
	mainContainer.addClass('panel-maincontainer');
	mainContainer.doLayout();
	function renderTitle(value, metadata, record, rowIndex, colIndex, store) {
	var ftoCol = {
		//1 :  Wzb.getQdbUrl('cmd', 'get_mod', 'stylesheet','forum.xsl', 'mod_id', record.data.for_res_id),
		1 :  Wzb.getQdbUrl('cmd', 'get_mod_status', 'stylesheet','start_module.xsl', 'mod_id', record.data.for_res_id, 'tkh_id', 0, 'mod_type', 'FOR', 'tpl_use', 'forum.xsl', 'url_failure', '../htm/close_window.htm'),
		2 : record.data.for_res_title
	};

	var str = WzbHtm.renderForumTitle(ftoCol);
	return str;
	}
	function renderimg(value, metadata, record, rowIndex, colIndex, store) {
	var str;
	str = WzbHtm.image({
			1 : Wzb.getCommonImagePath('forum.gif')
		})
		return str;

	}
	function renderfont(value, metadata, record, rowIndex, colIndex, store) {
		return '<span class="wzb-common-text">' + value + '</span>'
	}
}
