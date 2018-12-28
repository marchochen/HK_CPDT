new Wzb.PageRenderer({
	url : Wzb
			.getDisUrl('module', 'JsonMod.credit.CreditModule', 'cmd', 'get_credit_rank'),
	fn : onready,
	wzbLayout : Wzb.layout.TYPE4
});

function onready(mainStore) {
	Wzb.renderPageTitle(Wzb.l('851'));
	var my_credit_pn = new Wzb.Panel({
			border : false,
			cls : 'wzb-page-title-text',
			html : renderMyCredit()
		});
	my_credit_pn.render('container');
	var colModel = new Ext.grid.ColumnModel([{
		id : 'sort_id',
		header : Wzb.l('85'),
		dataIndex : 'sort_id',
		menuDisabled : true,
		resizable : false,
		sortable : false,
		renderer : renderOrder,
		width : Wzb.util_total_width * 0.1
	}, {
		id : 'usr_ste_usr_id',
		header : Wzb.l('535'),
		dataIndex : 'usr_ste_usr_id',
		menuDisabled : true,
		resizable : false,
		sortable : false,
		renderer : renderLoginName,
		width : Wzb.util_total_width * 0.25
	}, {
		id : 'usr_display_bil',
		header : Wzb.l('lab_name'),
		dataIndex : 'usr_display_bil',
		menuDisabled : true,
		resizable : false,
		sortable : false,
		width : Wzb.util_total_width * 0.25,
		renderer : renderName
	}, {
		id : 'usg_display_bil',
		header : Wzb.l('lab_group'),
		dataIndex : 'usg_display_bil',
		menuDisabled : true,
		resizable : false,
		sortable : false,
		width : Wzb.util_total_width * 0.25,
		renderer : renderGroup
	}, {
		id : 'uct_total',
		header : Wzb.l('836'),
		dataIndex : 'uct_total',
		menuDisabled : true,
		resizable : false,
		sortable : false,
		width : Wzb.util_total_width * 0.15,
		renderer : renderScore
	}]);

	var main_container = new Wzb.Panel({
		border : false,
		layout : 'column',
		width : Wzb.util_total_width,
		defaults : {
			autoHeight : true,
			cellCls : 'valign-top'
		},
		items : [getCreditRankPn()]
	});

	main_container.render('container');
	main_container.addClass('bottom');
	main_container.addClass('panel-maincontainer');
	main_container.doLayout();

	function renderOrder(value, metadata, record, rowIndex, colIndex, store) {
		var obj = {
			1 : record.data.sort_id
		};
		var str =  WzbHtm.rank_sort(obj);
	
		return str;
	}
	
	function renderLoginName(value, metadata, record, rowIndex, colIndex, store) {
		return WzbHtm.renderCreditRank({
			1 : record.data.usr_ste_usr_id
		});
	}

	function renderName(value, metadata, record, rowIndex, colIndex, store) {
		return WzbHtm.renderCreditRank({
			1 : record.data.usr_display_bil
		});
	}
	
	function renderGroup(value, metadata, record, rowIndex, colIndex, store) {
		return WzbHtm.renderCreditRank({
			1 : record.data.usg_display_bil
		});
	}
	
	function renderScore(value, metadata, record, rowIndex, colIndex, store) {
		return WzbHtm.renderCreditRank({
			1 : record.data.uct_total
		});
	}
	
	function renderMyCredit(value, metadata, record, rowIndex, colIndex, store) {
		var update_time = mainStore.reader.jsonData['credit_rank']['update_time'];
		if(update_time !== undefined) {
			update_time = Wzb.displayTime(update_time, Wzb.time_format_ymdhm);
		} else {
			update_time = '--';
		}
		var my_credit = mainStore.reader.jsonData['credit_rank']['my_credit'];
		if(my_credit === undefined) {
			my_credit = '--';
		}
		var my_sort = mainStore.reader.jsonData['credit_rank']['my_credit_sort'];
		if(my_sort === undefined) {
			my_sort = '--';
		}
		var obj = {
			1 : Wzb.l('836'),
			2 : my_credit,
			3 : Wzb.l('852'),
			4 : my_sort,
			5 : Wzb.util_total_width,
			6 : Wzb.l('115'),
			7 : update_time
		};
		var str =  WzbHtm.renderMyCredit(obj);
		return str;
	}

	function getCreditRankPn() {
		var reader = new Ext.data.JsonReader({
			totalProperty : 'credit_rank["total"]',
			root : 'credit_rank["credit_rank_lst"]'
		}, ['sort_id', 'usr_ent_id', 'usr_ste_usr_id'
		, 'usr_display_bil', 'usg_display_bil', 'uct_total']);
		var rankStore = new Ext.data.Store({
				remoteSort : true,
				proxy : new Ext.data.MemoryProxy(mainStore.reader.jsonData),
				reader : reader
			});
		var rankBbar = Wzb.getPagingToolbar(rankStore);
		var rankCenterGridPan = new Wzb.GridPanel({
			store : rankStore,
			colModel : colModel,
			autoHeight : true,
			width : Wzb.util_total_width,
			border : true,
			cls : 'wzb-right-1',
			bbar : rankBbar,
			isChangeProxy : true,
			url : Wzb.getDisUrl('module', 'JsonMod.credit.CreditModule', 'cmd', 'get_credit_rank'),
			viewConfig : {
				emptyText : Wzb.l('657')
			}
		});
		rankCenterGridPan.addListener({
			'render' : Wzb.renderEventFn
		})
		return rankCenterGridPan;
	}
}