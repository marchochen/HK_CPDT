new Wzb.PageRenderer({
	url : Wzb
			.getDisUrl('module', 'JsonMod.credit.CreditModule', 'cmd', 'get_credit_history'),
	fn : onready,
	wzbLayout : Wzb.layout.TYPE4
});

function onready(mainStore) {
	Wzb.renderPageTitle(Wzb.l('853'));
	var my_credit_pn = new Wzb.Panel({
			border : false,
			cls : 'wzb-page-title-text',
			html : renderMyCredit()
		});
	my_credit_pn.render('container');
	var colModel = new Ext.grid.ColumnModel([{
		id : 'cty_code',
		header : Wzb.l('823'),
		dataIndex : 'cty_code',
		menuDisabled : true,
		resizable : false,
		sortable : false,
		renderer : renderCytCode,
		width : Wzb.util_total_width * 0.15
	}, {
		id : 'source',
		header : Wzb.l('824'),
		dataIndex : 'source',
		menuDisabled : true,
		resizable : false,
		sortable : false,
		width : Wzb.util_total_width * 0.15,
		renderer : renderSource
	}, {
		id : 'ucl_point',
		header : Wzb.l('563'),
		dataIndex : 'ucl_point',
		menuDisabled : true,
		resizable : false,
		sortable : false,
		width : Wzb.util_total_width * 0.1,
		renderer : renderScore
	}, {
		id : 'cty_deduction_ind',
		header : Wzb.l('820'),
		dataIndex : 'cty_deduction_ind',
		menuDisabled : true,
		resizable : false,
		sortable : false,
		width : Wzb.util_total_width * 0.17,
		renderer : renderOperation
	}, {
		id : 'cty_manual_ind',
		header : Wzb.l('821'),
		dataIndex : 'cty_manual_ind',
		menuDisabled : true,
		resizable : false,
		sortable : false,
		width : Wzb.util_total_width * 0.13,
		renderer : renderCtyManual
	}, {
		id : 'type',
		header : Wzb.l('822'),
		dataIndex : 'type',
		menuDisabled : true,
		resizable : false,
		sortable : false,
		width : Wzb.util_total_width * 0.15,
		renderer : renderType
	}, {
		id : 'ucl_create_timestamp',
		header : Wzb.l('825'),
		dataIndex : 'ucl_create_timestamp',
		menuDisabled : true,
		resizable : false,
		sortable : false,
		width : Wzb.util_total_width * 0.15,
		renderer : renderCreateTime
	}]);

	var main_container = new Wzb.Panel({
		border : false,
		layout : 'column',
		width : Wzb.util_total_width,
		defaults : {
			autoHeight : true,
			cellCls : 'valign-top'
		},
		items : [getMyCreditPn()]
	});

	main_container.render('container');
	main_container.addClass('bottom');
	main_container.addClass('panel-maincontainer');
	main_container.doLayout();
	//积分名称
	function renderCytCode(value, metadata, record, rowIndex, colIndex, store) {
		var manualInd = record.data.cty_manual_ind;
		var labCtyCode = record.data.cty_code;
		var type = record.data.type;
		if((manualInd !== undefined && !manualInd) || labCtyCode === 'ITM_IMPORT_CREDIT'||(type !== undefined && type === 'E'&&labCtyCode==='INTEGRAL_EMPTY')) {
			labCtyCode = Wzb.l('lab_cty_' + labCtyCode)
		} 
		return WzbHtm.renderCredit({
			1 : labCtyCode
		});
	}
	//积分来源
	function renderSource(value, metadata, record, rowIndex, colIndex, store) {
		var source = record.data.source;
		if(source === undefined || source === '') {
			source = '--';
		}
		return WzbHtm.renderCredit({
			1 : source
		});
	}
	
	//积分
	function renderScore(value, metadata, record, rowIndex, colIndex, store) {
		var labCtyCode = record.data.cty_code;
		var type = record.data.type;
		if(type !== undefined && type === 'E'&&labCtyCode==='INTEGRAL_EMPTY') {
		return WzbHtm.renderCredit({
				1 : '--'
			});
		}else{
			return WzbHtm.renderCredit({
				1 : Math.round(record.data.ucl_point*100)/100
			});
		}
	}
	//积分操作类型
	function renderOperation(value, metadata, record, rowIndex, colIndex, store) {
		var ctyDeductionInd = record.data.cty_deduction_ind;
		var labCtyCode = record.data.cty_code;
		var type = record.data.type;
		var labOperation;
		if(ctyDeductionInd !== undefined && ctyDeductionInd) {
			labOperation = Wzb.l('831');
		} else if(type !== undefined && type === 'E'&&labCtyCode==='INTEGRAL_EMPTY') {
		    labOperation = Wzb.l('lab_cty_EMPTY'); 
		} else if(ctyDeductionInd !== undefined && !ctyDeductionInd) {
			labOperation = Wzb.l('830');
		} else {
			labOperation = '--';
		}
		if(type !== undefined && type === 'E'&&labCtyCode==='INTEGRAL_EMPTY'){
		      labOperation = Wzb.l('lab_cty_EMPTY');
		}
		return WzbHtm.renderCredit({
			1 : labOperation
		});
	}
	
	function renderCtyManual(value, metadata, record, rowIndex, colIndex, store) {
		var ctyManualInd = record.data.cty_manual_ind;
		var labManual;
		if(ctyManualInd !== undefined && ctyManualInd) {
			labManual = Wzb.l('826');
		} else if(ctyManualInd !== undefined && !ctyManualInd) {
			labManual = Wzb.l('827');
		} else {
			labManual = '--';
		}
		return WzbHtm.renderCredit({
			1 : labManual
		});
	}
	//积分类型
	function renderType(value, metadata, record, rowIndex, colIndex, store) {
		var type = record.data.type;
		var labType;
		if(type !== undefined && type === 'T') {
			labType = Wzb.l('829');
		} else if(type !== undefined) {
			labType = Wzb.l('828');
		} else {
			labType = '--';
		}
		return WzbHtm.renderCredit({
			1 : labType
		});
	}
	
	function renderCreateTime(value, metadata, record, rowIndex, colIndex, store) {
		var createTime = record.data.ucl_create_timestamp;
		var labCreateTime;
		if(createTime !== undefined) {
			labCreateTime = Wzb.displayTime(createTime, Wzb.time_format_ymdhm);
		} else {
			labCreateTime = '--';
		}
		
		return WzbHtm.renderCredit({
			1 : labCreateTime
		});
	}
	
	function renderMyCredit(value, metadata, record, rowIndex, colIndex, store) {
		var train_score = mainStore.reader.jsonData['my_credit']['training_score'];
		var activity_score = mainStore.reader.jsonData['my_credit']['activity_score'];
		if(train_score === undefined) {
			train_score = '--';
		}
		if(activity_score === undefined) {
			activity_score = '--';
		}
		var obj = {
			1 : Wzb.l('829'),
			2 : train_score,
			3 : Wzb.l('828'),
			4 : Math.round(activity_score*100)/100
		};
		str = WzbHtm.renderMyCredit(obj);
		return str;
	}

	function getMyCreditPn() {
		var reader = new Ext.data.JsonReader({
			totalProperty : 'my_credit["total"]',
			root : 'my_credit["credit_lst"]'
		}, ['cty_code', 'source', 'ucl_point'
		, 'cty_manual_ind', 'cty_deduction_ind', 'type', 'ucl_create_timestamp']);
		var creditStore = new Ext.data.Store({
				remoteSort : true,
				proxy : new Ext.data.MemoryProxy(mainStore.reader.jsonData),
				reader : reader
			});
		var creditBbar = Wzb.getPagingToolbar(creditStore);
		var creditCenterGridPan = new Wzb.GridPanel({
			store : creditStore,
			colModel : colModel,
			autoHeight : true,
			width : Wzb.util_total_width,
			border : true,
			cls : 'wzb-right-1',
			bbar : creditBbar,
			isChangeProxy : true,
			url : Wzb.getDisUrl('module', 'JsonMod.credit.CreditModule', 'cmd', 'get_credit_history'),
			viewConfig : {
				emptyText : Wzb.l('685')
			}
		});
		creditCenterGridPan.addListener({
			'render' : Wzb.renderEventFn
		})
		return creditCenterGridPan;
	}
}