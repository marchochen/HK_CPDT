new Wzb.PageRenderer({
	url : Wzb
			.getDisUrl('module', 'JsonMod.Ann.AnnModule', 'cmd', 'get_all_ann'),
	fn : onready,
	wzbLayout : Wzb.layout.TYPE3
});

function onready(mainStore) {

	mainStore.sortInfo = {
		field : 'msg_title',
		direction : 'DESC'
	};
	Wzb.renderPageTitle(Wzb.l('31'));

	var colModel = new Ext.grid.ColumnModel([{
		id : 'annimg',
		renderer : renderAnnImg,
		width : Wzb.util_col_width2 * 0.05
	}, {
		id : 'title',
		header : Wzb.l('79'),
		dataIndex : 'msg_title',
		menuDisabled : true,
		resizable : false,
		sortable : true,
		renderer : renderTitle,
		width : Wzb.util_col_width2 * 0.75
	}, {
		id : 'state',
		header : Wzb.l('163'),
		dataIndex : 'usr_display_bil',
		menuDisabled : true,
		resizable : false,
		sortable : true,
		width : Wzb.util_col_width2 * 0.2,
		renderer : renderAuthor
	}]);

	var main_container = new Wzb.Panel({
		border : false,
		layout : 'column',
		width : Wzb.util_total_width,
		// ctCls : 'maincontainer-left',
		defaults : {
			autoHeight : true,
			cellCls : 'valign-top'
		},
		items : [Wzb.getTcListTabPanel({
			store : mainStore,
			width : Wzb.util_col_width2,
			fn : getAnnTab
		}), getQuickLinkPn()]
	});

	main_container.render('container');
	main_container.addClass('bottom');
	main_container.addClass('panel-maincontainer');
	main_container.doLayout();

	function renderTitle(value, metadata, record, rowIndex, colIndex, store) {
		var str_feature = 'toolbar=' + 'no' + ',width=' + 480
				+ ',height=' + 200 + ',scrollbars=' + 'auto'
				+ ',resizable=' + 'yes' + ',status=' + 'no';

		var msg_url = Wzb.getQdbUrl('cmd', 'get_msg', 'msg_type',
				record.data.msg_type, 'msg_id', record.data.msg_id,
				'stylesheet', 'announ_dtl.xsl', 'url_failure',
				'../htm/close_window.htm');
				
		var newest_img = '';
		if(record.data.newest_ind) {
			newest_img = '<img '+ Wzb.getHtmImgSrc('../wb_image/ann_new.gif') + '>'; 
		}
		var msg_desc = '';
		if (record.data.msg_desc != '') {
			msg_desc = record.data.msg_desc;
		}
		
		var obj = {
			1 : msg_url,
			2 : record.data.msg_title,
			3 : newest_img,
			4 : msg_desc,
			5 : str_feature
		};
		var str = WzbHtm.renderTitle(obj);
		return str;
	}

	function renderAnnImg(value, metadata, record, rowIndex, colIndex, store) {
		var str = WzbHtm.renderImg();
		return str;
	}

	function renderAuthor(value, metadata, record, rowIndex, colIndex, store) {
		return WzbHtm.renderAuthor({
			1 : record.data.usr_display_bil,
			2 : Wzb.displayTime(record.data.msg_begin_date)
		});
	}

	function getAnnTab(tabId, tabTitle, isActiveTab) {
		var reader = new Ext.data.JsonReader({
			totalProperty : 'ann["total"]',
			root : 'ann["ann_lst"]'
		}, ['msg_id', 'msg_title', 'msg_body', 'usr_display_bil',
				'is_content_cut', 'msg_begin_date', 'msg_desc', 'newest_ind']);
		var annStore;

		if (isActiveTab) {
			annStore = new Ext.data.Store({
				remoteSort : true,
				proxy : new Ext.data.MemoryProxy(mainStore.reader.jsonData),
				reader : reader
			});
		} else {
			annStore = new Ext.data.Store({
				remoteSort : true,
				proxy : new Ext.data.HttpProxy({
					url : Wzb.getDisUrl('module', 'JsonMod.Ann.AnnModule',
							'cmd', 'get_all_ann', 'tcr_id', tabId)
				}),
				reader : reader
			});
		}
		var annBbar = Wzb.getPagingToolbar(annStore);
		var annCenterGridPan = new Wzb.GridPanel({
			store : annStore,
			title : tabTitle,
			colModel : colModel,
			autoHeight : true,
			width : Wzb.util_col_width2,
			border : true,
			isChangeProxy : isActiveTab,
			url : Wzb.getDisUrl('module', 'JsonMod.Ann.AnnModule', 'cmd',
					'get_all_ann', 'tcr_id', tabId),
			bbar : annBbar,
			viewConfig : {
				emptyText : Wzb.l('164')
			}
		});
		annCenterGridPan.addListener({
			'render' : Wzb.renderEventFn
		})
		return annCenterGridPan;
	}

	function getQuickLinkPn() {
		var qlObj = {
			1 : Wzb.l('161'),
			2 : Wzb.l('162') + Wzb.l('11'),
			3 : Wzb.l('162') + Wzb.l('16'),
			4 : Wzb.l('162') + Wzb.l('21'),
			5 : '',
			6 : Wzb.l('162') + Wzb.l('345')
		};
		if (Wzb.has_my_team_view) {
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
		return quickLinkPn;
	}
}