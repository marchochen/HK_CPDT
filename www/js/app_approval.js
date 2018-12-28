var activetab = Wzb.getUrlParam('activetab');
if (!activetab || activetab == '') {
	activetab = 'app_pend';
}

new Wzb.PageRenderer({
	url : Wzb.getDisUrl('module', 'JsonMod.supervise.SuperviseModule', 'cmd', 'get_app_pend'),
	fn : onready,
	wzbLayout : Wzb.layout.TYPE4
});

app =  new wbApplication;
var sm = new Ext.grid.CheckboxSelectionModel({
	singleSelect : false
});
function onready(mainStore) {
	Wzb.renderPageTitle(Wzb.l('29'));
	var workflowData = mainStore.reader.jsonData["workflow"];
	
	var mainContainer = new Wzb.TabPanel({
		width : Wzb.util_total_width,
		activeTab: activetab,
		plain : true,
		items : [
			getAppListPanel('app_pend', Wzb.l('276'))
			,getAppListPanel('app_approval', Wzb.l('277'))
		]
	});
	mainContainer.render('container');
	mainContainer.addClass('bottom');
	mainContainer.addClass('panel-maincontainer');
	function getAppListPanel(id, title) {
		var store = function() {
			var readerRoot = id + '["app_lst"]';
			var storeProxy;
			if(id == activetab) {
				storeProxy = new Ext.data.MemoryProxy(mainStore.reader.jsonData);
			} else {
				storeProxy = new Ext.data.HttpProxy({
					url: getTabUrl(id)
				});
			}
			
			var jsonSort = 'app_create_timestamp';
			var jsonDir = 'DESC';
			var curTabData = mainStore.reader.jsonData[id];
			if(curTabData !== undefined && curTabData['page'] !== undefined) {
				if(curTabData['sort'] !== undefined) {
					jsonSort = curTabData['sort'];
				}
				if(curTabData['dir'] !== undefined) {
					jsonDir = curTabData['dir'];
				}
			}
			var storeSortInfo = {
				field: jsonSort,
				direction: jsonDir
			};
			var pendReader = new Ext.data.JsonReader({
				totalProperty : id + '["page"]["total_rec"]',
				root : readerRoot
			}, [{
				name : 'app_create_timestamp'
			},{
				name : 'app_upd_timestamp'
			}, {
				name : 'itm_start_date'
			}, {
				name : 'itm_title'
			}, {
				name : 'usr_display_bil'
			}, {
				name : 'usr_ste_usr_id'
			}, {
				name : 'app_id'
			}, {
				name : 'usr_ent_id'
			}, {
				name : 'appr_lst'
			}, {
				name : 'nextApp'
			}]);
			var store = new Ext.data.Store({
				proxy : storeProxy,
				remoteSort: true,
				sortInfo:storeSortInfo,
				reader : pendReader
			});
			return store;
		}();

		var clmArr = new Array();
		if(id == 'app_pend') {
			clmArr.push(sm);
		}
		clmArr.push(Wzb.createGridCol({
				id : 'usr_ste_usr_id',
				dataIndex : 'usr_ste_usr_id',
				header : Wzb.l('535'),
				sortable:true,
				width : Wzb.util_total_width * 0.1,
				renderer : function(value, meta, record) {
					var str = '<span class="wzb-common-text">' + value + '</span>';
					return str;
				}
			}, Wzb.colString
		));
		clmArr.push(Wzb.createGridCol({
				id : 'usr_display_bil',
				dataIndex : 'usr_display_bil',
				header : Wzb.l('5'),
				sortable:true,
				renderer: renderDisplayBil,
				width : Wzb.util_total_width * 0.1
			}, Wzb.colString
		));
		clmArr.push(Wzb.createGridCol({
				id : 'itm_title',
				dataIndex : 'itm_title',
				header : Wzb.l('562'),
				sortable:true,
				width : Wzb.util_total_width * 0.2,
				renderer : function(value, meta, record) {
					var str = '<span class="wzb-common-text">' + value + '</span>';
					return str;
				}
			}, Wzb.colString
		));
		clmArr.push({
				id : 'itm_start_date',
				dataIndex : 'itm_start_date',
				header : Wzb.l('282'),
				sortable:true,
				width : Wzb.util_total_width * 0.1,
				renderer : function(value, meta, record) {
					var str = '<span class="wzb-common-text">';
					if(value === '') {
						str += Wzb.l('631');
					} else {
						str += Wzb.displayTime(value);
					}
					str += '</span>';
					return str;
				}
			});
		clmArr.push({
				id : 'app_create_timestamp',
				dataIndex : 'app_create_timestamp',
				header : Wzb.l('281'),
				sortable:true,
				width : Wzb.util_total_width * 0.13,
				renderer : function(value, meta, record) {
					var str = '<span class="wzb-common-text">' + Wzb.displayTime(value) + '</span>';
					return str;
				}
			}
		);
		clmArr.push(Wzb.createGridCol({
			id : 'appr_lst',
			dataIndex : 'appr_lst',
			header : Wzb.l('283'),
			width : function() {
				var width;
				if(id == 'app_approval') {
					width = Wzb.util_total_width * 0.27;
				} else {
					width = Wzb.util_total_width * 0.37;
				}
				return width;
			}(),
			sortable:false,
			renderer: renderAppHis
		}, Wzb.colString
		));
		if(id == 'app_approval') {
			clmArr.push(Wzb.createGridCol({
				header : Wzb.l('286'),
				width : Wzb.util_total_width * 0.1,
				sortable:false,
				renderer : renderNextAppTaker
			}, Wzb.colString
			));
		}
		var isChangeProxy = function(activeId, id) {
			if(activeId == id) {
				return true;
			} else {
				return false;
			}
		}
		var gridPan;
		if(id == 'app_pend') {
			gridPan = new Wzb.GridPanel({
				sm: sm,
				tbar : [
					'->',
					{
						text: Wzb.l('629'),
						handler: function() {
							frmAction.app_id_lst.value = getSelectedAppId();
							if(frmAction.app_id_lst.value !== '') {
								str_feature = 'toolbar='		+ 'no'
										+ ',width=' 				+ '450'
										+ ',height=' 				+ '200'
										+ ',scrollbars='			+ 'no'
										+ ',resizable='				+ 'no'
										+ ',status='				+ 'yes';
								wb_utils_set_cookie('appn_usr_name','');
								wb_utils_set_cookie('current','');
								wb_utils_set_cookie('total','');
								var itm_id = 0;
								var lang = Wzb.wb_lan;
								var process_id = 1;
								var status_id = 1;
								var action_id = 1;
								var fr = Wzb.workflow.getActnHistoryFr(workflowData, process_id, status_id, action_id);
								var to = Wzb.workflow.getActnHistoryTo(workflowData, process_id, status_id, action_id);
								var verb = Wzb.workflow.getActnHistoryVerb(workflowData, process_id, status_id, action_id);
								
								frmAction.verb.value=verb
								frmAction.fr.value=fr
								frmAction.to.value=to
								url = '../htm/application_frame_window.htm?run_id='+itm_id+'&lang='+lang+'&process_id='+process_id+'&status_id='+status_id+'&action_id='+action_id+'&fr='+fr+'&to='+to+'&verb='+verb+'&functionName=doFeedParam&processEndFunction=reload_me';
								wbUtilsOpenWin(url,'application_enrollment_status', false, str_feature);
							} else {
								alert(Wzb.l('626'));
							}
						}
					},
					'-',
					{
						text: Wzb.l('630'),
						handler: function() {
							frmAction.app_id_lst.value = getSelectedAppId();
							if(frmAction.app_id_lst.value !== '') {
								str_feature = 'toolbar='		+ 'no'
										+ ',width=' 				+ '450'
										+ ',height=' 				+ '200'
										+ ',scrollbars='			+ 'no'
										+ ',resizable='				+ 'no'
										+ ',status='				+ 'yes';
								wb_utils_set_cookie('appn_usr_name','');
								wb_utils_set_cookie('current','');
								wb_utils_set_cookie('total','');
								var itm_id = 0;
								var lang = Wzb.wb_lan;
								var process_id = 1;
								var status_id = 1;
								var action_id = 2;
								var fr = Wzb.workflow.getActnHistoryFr(workflowData, process_id, status_id, action_id);
								var to = Wzb.workflow.getActnHistoryTo(workflowData, process_id, status_id, action_id);
								var verb = Wzb.workflow.getActnHistoryVerb(workflowData, process_id, status_id, action_id);
								frmAction.verb.value=verb
								frmAction.fr.value=fr
								frmAction.to.value=to
								url = '../htm/application_frame_window.htm?run_id='+itm_id+'&lang='+lang+'&process_id='+process_id+'&status_id='+status_id+'&action_id='+action_id+'&fr='+fr+'&to='+to+'&verb='+verb+'&functionName=doFeedParam&processEndFunction=reload_me';
								wbUtilsOpenWin(url,'application_enrollment_status', false, str_feature);
							} else {
								alert(Wzb.l('626'));
							}
						}
					}
				],
				isChangeProxy : isChangeProxy(activetab, 'app_pend'),
				url : getTabUrl('get_app_pend'),
				bbar : Wzb.getPagingToolbar(store),
				viewConfig : {	emptyText : Wzb.getEmptyText()}
			});
			gridPan.addListener({
					'render' : Wzb.renderEventFn
			})
		} else {
			gridPan = new Wzb.GridPanel({
				bbar : Wzb.getPagingToolbar(store),
				isChangeProxy : isChangeProxy(activetab, 'app_approval'),
				url : getTabUrl('get_app_approval'),
				viewConfig : {	emptyText : Wzb.getEmptyText()}
			});
			gridPan.addListener({
				'render' : Wzb.renderEventFn
			})
			
		}
		Ext.apply(gridPan, {
			id : id,
			title : title,
			store : store,
			colModel : new Ext.grid.ColumnModel(clmArr),
			disableSelection: true,
			trackMouseOver:false,
			enableColumnMove:false,
			border : false,
			autoHeight : true
		});
		return gridPan;

		function renderAppHis(value, metadata, record) {
			var str = '';
			var apprLst = record.data.appr_lst;
			for(var i=0; i<apprLst.length; i++) {
				str += WzbHtm.appHis({
					2:Wzb.displayTime(apprLst[i]['aal_action_timestamp']),
					1:apprLst[i]['usr_display_bil'],
					3 : getActionLabel(apprLst[i]['action_taken'])
				})
			}
			if(id === 'app_approval' && record.data.nextApp.length === 0) {
				str += '<span class="wzb-common-text">' + Wzb.l('632')  + '</span>';
			}
			if(str === '') {
				str = '--';
			}
			return str;
		}
	}

	function getTabUrl(tabId) {
		var urlStr = Wzb.getDisUrl('module', 'JsonMod.supervise.SuperviseModule', 'cmd', 'get_app_pend');
		if(tabId == 'app_approval') {
			urlStr = Wzb.getDisUrl('module', 'JsonMod.supervise.SuperviseModule', 'cmd', 'get_app_approval');
		}
		return urlStr;
	}

	function renderDisplayBil(value, metadata, record) {
		var str = WzbHtm.displayBil({
			1 : 'javascript:showUserInfoWin(' + record.data.usr_ent_id + ')',
			2 : value
		})
		return str;
	}
	
	function renderNextAppTaker(value, metadata, record) {
		var str = '<span class="wzb-common-text">';
		var nextApp = record.data.nextApp;
		var len = nextApp.length;
		if(len === 0) {
			str += '--';			
		} else {
			for(var i=0; i<len; i++) {
				str += nextApp[i];
				if(i !== nextApp.length - 1) {
					str += ', ';
				}
			}
		}
		str += '</span>';
		return str;
	}
	
	function getActionLabel(type) {
		var str = '';
		switch (type) {
			case 'APPROVED' : 
				str = Wzb.l('278');
				break;
			case 'DISAPPROVED' : 
				str = Wzb.l('279');
				break;
		}
		return str;
	}
}

function getSelectedAppId() {
	var appIds = '';
	var sel = sm.getSelections();
	var len = sel.length;
	for(var i=0; i<len; i++) {
		appIds += sel[i].data.app_id;
		if(i !== len - 1) {
			appIds += '~';
		}
	}
	return appIds;
}

function getSelectedActionNameLst() {
	var nm_lst = '';
	var sel = sm.getSelections();
	if (sel.length == 0) {
		alert(eval('wb_msg_'+ Wzb.wb_lan +'_select_applicant'));
		return;
	}
	var len = sel.length;
	for(var i=0; i<len; i++) {
		nm_lst += sel[i].data.usr_display_bil;
		if(i !== len - 1) {
			nm_lst += '~';
		}
	}
	return nm_lst;
}

function getSelectedActionTimestampLst() {
	var nm_lst = '';
	var sel = sm.getSelections();
	if (sel.length == 0) {
		alert(eval('wb_msg_'+ Wzb.wb_lan +'_select_applicant'));
		return;
	}
	var len = sel.length;
	for(var i=0; i<len; i++) {
		nm_lst += sel[i].data.app_upd_timestamp;
		if(i !== len - 1) {
			nm_lst += '~';
		}
	}
	return nm_lst;
}

function doFeedParam(){
	param = new Array();
	tmpObj1 = new Array();
	tmpObj2 = new Array();
	tmpObj3 = new Array();
	tmpObj4 = new Array();
	tmpObj5 = new Array();
	
	tmpObj1[0] = 'cmd';
	tmpObj1[1] = 'ae_make_multi_actn';
	param[param.length] = tmpObj1;
	
	tmpObj2[0] = 'app_id_lst';
	app_id_lst = getSelectedAppId();
	tmpObj2[1] = app_id_lst.split('~');
	param[param.length] = tmpObj2;
	
	tmpObj3[0] = 'app_nm_lst';
	app_nm_lst = getSelectedActionNameLst();
	tmpObj3[1] = app_nm_lst.split('~');
	param[param.length] = tmpObj3;

	tmpObj4[0] = 'app_upd_timestamp_lst';
	app_upd_timestamp_lst = getSelectedActionTimestampLst();
	tmpObj4[1] = app_upd_timestamp_lst.split('~');
	param[param.length] = tmpObj4;
	
	tmpObj5[0] = 'content';
	tmpObj5[1] = frmAction.content.value;
	param[param.length] = tmpObj5;
	
	return param;
}

function reload_me() {
	self.location.reload();
}
