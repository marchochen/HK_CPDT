var ans_max_len = 10000;
var ans_ref_max_len = 255;
var que_desc_max_len = 500;
var que_title_max_len = 60;
var que_lst_page_size = 40;

function renderActionLink() {
	var p = new Wzb.Panel({
		border : false,
		width : Wzb.util_total_width,
		html : WzbHtm.share_action_link({
			1 : getActionLink()
		})
	});
	p.render('container');
}

function getActionLink() {
	var recordurl = 'window.location.href = \'my_know_record.htm\'';
	var helpurl = 'window.location.href = \'my_know_help.htm\'';
	var askurl = 'window.location.href = \'ask_que.htm\'';
	var str = '<table>' + '<tr>' + '<td>'
			+ Wzb.getButton(askurl, Wzb.l('220')) + '</td>' + '<td>'
			+ Wzb.getButton(recordurl, Wzb.l('221')) + '</td>' + '<td>'
			+ Wzb.getButton(helpurl, Wzb.l('222')) + '</td>' + '</tr>'
			+ '</table>';
	return str;
}

// course content
function getClassifiedContentPanel(store) {
	var mainData = store.reader.jsonData;
	var catData = mainData['know_catalog'];
	var classifiedPanel;
	if (catData !== undefined) {
		var classifiedQueStore = new Ext.data.Store({
			proxy : new Ext.data.MemoryProxy(catData),
			autoLoad : true,
			reader : new Ext.data.JsonReader({
				root : 'catalog_structure'
			}, [{
				name : 'tnd_id',
				mapping : 'id'
			}, {
				name : 'tnd_title',
				mapping : 'text'
			}, {
				name : 'que_count'
			}, {
				name : 'children'
			}])
		});

		var p1 = new Wzb.GridPanel({
			store : classifiedQueStore,
			hideHeaders : true,
			columns : [{
				width : Wzb.util_col_width1,
				renderer : function(value, meta, record) {
					var cat_url = 'my_know_classify.htm?kca_id='
							+ record.data.tnd_id;
					var tnd_lst = record.data['children'];
					var sub_tnd = '';
					var len = tnd_lst.length;
					for (var i = 0; i < tnd_lst.length; i++) {
						var tnd_url = 'my_know_classify.htm?kca_id='
								+ tnd_lst[i]['id'];
						if (i < 3) {
							sub_tnd += '<a class=\'wzb-second-level-link\' href=\''
									+ tnd_url
									+ '\'>'
									+ tnd_lst[i]['text']
									+ '</a>'
									+ '<span class=\'wzb-common-text\'> ('
									+ tnd_lst[i]['que_count'] + ')</span>';
						} else {
							sub_tnd += '...';
							break;
						}
						if (i < 3 && i !== len - 1) {
							sub_tnd += Wzb.link_sep
						}
					}
					var str = '';
					str += '<table border="0" cellspacing="3">' + '<tr>'
							+ '<td>' + '<a class="wzb-first-level-link" href="'
							+ cat_url + '">' + record.data['tnd_title']
							+ '</a><span class="wzb-common-text"> ('
							+ record.data['que_count'] + ')</span>' + '</td>'
							+ '</tr>' + '<tr>' + '<td>' + sub_tnd + '</td>'
							+ '</tr>' + '</table>'
					return str;
				}
			}],
			viewConfig : {
				emptyText : Wzb.getEmptyText()
			}
		});
		var backLabel = '';
		var backUrl = '';
		var labSeparator = '';
		var p_id = catData['parent_id'];
		if (p_id !== undefined) {
			backLabel = Wzb.l('55');
			if (p_id === 0 || p_id === '0') {
				backUrl = 'my_know.htm';
			} else {
				backUrl = 'my_know_classify.htm';
			}
			backUrl += '?kca_id=' + p_id;
			labSeparator = Wzb.link_sep;
		}
		var kcaDetaiLabel = Wzb.l('56');
		var kcaDetailUrl = 'my_know_classify_list.htm';
		var p2 = new Wzb.Panel({
			bodyStyle : Wzb.style_inner_space,
			html : WzbHtm.back({
				4 : backLabel,
				3 : backUrl,
				5 : labSeparator,
				2 : kcaDetaiLabel,
				1 : kcaDetailUrl
			})
		});

		classifiedPanel = new Wzb.Panel({
			hideBorders : true,
			title : Wzb.l('215'),
			type : 5,
			items : [p1, p2]
		});
		classifiedPanel.addClass('bottom');
	} else {
		classifiedPanel = new Wzb.Panel({
			border : false
		});
	}
	return classifiedPanel;
}

// unsolved question/solved question/popular/faq question tab pane
function get_questions_panel(mainStore, activetab, kca_id) {
	var getQueContentPan = function(tabId, title) {
		var queConStore = getQueListStore(mainStore, tabId, activetab, kca_id);
		function showClassify(value, meta, record) {
			var url = 'my_know_classify.htm?kca_id=' + record.data.kca_id;
			var str = '<a class="wzb-common-link" href="' + url + '">' + value
					+ '</a>';
			return str;
		};
		function showQuestion(value, metadata, record) {
			var url = '';
			url += 'que_detail.htm?que_id=' + record.data.que_id;
			var str = '<a class="wzb-common-link" href="' + url + '">' + value
					+ '</a>';
			return str;
		};
		var colModel = new Ext.grid.ColumnModel([{
			dataIndex : 'kca_title',
			header : Wzb.l('217'),
			sortable : true,
			width : Wzb.util_col_width2 * 0.2,
			renderer : showClassify
		}, {
			dataIndex : 'que_title',
			header : Wzb.l('79'),
			sortable : true,
			width : Wzb.util_col_width2 * 0.4,
			renderer : showQuestion
		}, Wzb.createGridCol({
			dataIndex : 'ans_count',
			sortable : true,
			header : Wzb.l('218'),
			width : Wzb.util_col_width2 * 0.15,
			renderer : function(value) {
				var str = '<span class="wzb-common-text">' + value
						+ '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>';
				return str;
			}
		}, Wzb.colNumber), {
			dataIndex : 'que_create_timestamp',
			sortable : true,
			header : Wzb.l('219'),
			width : Wzb.util_col_width2 * 0.25,
			renderer : function(value) {
				var str = '<span class="wzb-common-text">' + Wzb.displayTime(value)
						+ '</span>';
				return str;
			}
		}]);
		var bbar = Wzb.getPagingToolbar(queConStore, 1, que_lst_page_size);
		var gridPan = new Wzb.GridPanel({
			id : tabId,
			title : title,
			store : queConStore,
			colModel : colModel,
			border : false,
			bbar : bbar,
			viewConfig : {
				emptyText : Wzb.getEmptyText()
			}
		});
		gridPan.addListener('render', function() {
			queConStore.load({
				params : {
					start : 0,
					limit : que_lst_page_size
				}
			});
			if (activetab === tabId) {
				queConStore.proxy = new Ext.data.HttpProxy({
					url : getQueTypeUrl(kca_id, 'que_unans_lst')
				});
			}
		});
		return gridPan;
	};

	var tabs = new Wzb.TabPanel({
		activeTab : activetab,
		items : [getQueContentPan('que_unans_lst', Wzb.l('214')),
				getQueContentPan('que_ansed_lst', Wzb.l('213')),
				getQueContentPan('que_popular_lst', Wzb.l('211')),
				getQueContentPan('que_faq_lst', Wzb.l('684'))]
	});
	return tabs;
}

function getSearchPanel(config) {
	var all_cata_id = '0';
	var catalogStore = new Ext.data.Store({
		proxy : new Ext.data.MemoryProxy(config.store.reader.jsonData['top_catalog_lst']),
		reader : new Ext.data.JsonReader({
			root : 'catalog_structure'
		}, [{
			name : 'id'
		}, {
			name : 'text'
		}])
	});
	catalogStore.load({
		callback : function() {
			catalogStore.insert(0, new Ext.data.Record({
				id : all_cata_id,
				text : Wzb.l('223')
			}));
		}
	});
	var doSearch = function() {
		var frm = Ext.getCmp('know_search_formpanel').getForm();
		frm.getEl().dom.action = Wzb.uri_dis;
		frm.submit();
	}
	var search_panel = new Wzb.FormPanel({
		id : 'know_search_formpanel',
		layout : 'table',
		width : Wzb.util_total_width,
		standardSubmit : true,
		layoutConfig : {
			columns : 4
		},
		defaults : {
			bodyStyle : Wzb.style_inner_space
		},
		hideBorders : true,
		autoHeight : true,
		frame : true,
		keys : [{
			key : Ext.EventObject.ENTER,
			fn : doSearch
		}],
		items : [{
			columnWidth : .25,
			items : [new Ext.form.TextField({
				name : 'srh_key',
				width : 240,
				value : function() {
					var val = '';
					var srh_criteria = config.store.reader.jsonData['srh_criteria'];
					if (srh_criteria !== undefined) {
						val = srh_criteria['srh_key'];
					}
					return val;
				}()
			})]
		}, {
			columnWidth : .18,
			items : [new Wzb.ComboBox({
				name : 'srh_catalog_id',
				hiddenName : 'srh_catalog_id',
				editable : false,
				store : catalogStore,
				valueField : 'id',
				displayField : 'text',
				mode : 'local',
				triggerAction : 'all',
				value : function() {
					var val = all_cata_id;
					var srh_criteria = config.store.reader.jsonData['srh_criteria'];
					if (srh_criteria !== undefined) {
						val = srh_criteria['srh_catalog_id'];
					}
					return val;
				}(),
				selectOnFocus : true
			})]
		}, {
			columnWidth : .06,
			items : [new Ext.Button({
				text : Wzb.l('400'),
				handler : doSearch
			})]
		}, {
			columnWidth : .51,
			style : 'padding-top:4px;',
			html : '<a class=\"wzb-common-link\" href=\"javascript:Wzb.headerAdvanceSearch(1)\">'
					+ Wzb.l('317') + '</a>'
		}, {
			xtype : 'hidden',
			name : 'cmd',
			value : 'save_search'
		}, {
			xtype : 'hidden',
			name : 'module',
			value : 'JsonMod.Wzb'
		}, {
			xtype : 'hidden',
			name : 'srh_simple_srh_ind',
			value : true
		}, {
			xtype : 'hidden',
			name : 'url_success',
			value : '../htm/my_know_search_result.htm'
		}],
		cls : 'bottom'
	})
	search_panel.addClass('panel-maincontainer');
	return search_panel;
}

function getSearchUrl(search_id, cur_tab) {
	if (cur_tab === undefined) {
		cur_tab = '';
	}
	return Wzb.getDisUrl('module', 'JsonMod.know.KnowModule', 'cmd',
			'search_que', 'search_id', search_id, 'activetab', cur_tab);
}
function renderKnowSearchResult(search_id) {
	var p = new Wzb.Panel({
		border : false
	});
	var storeObj = new Ext.data.JsonStore({
		url : getSearchUrl(search_id)
	});
	storeObj.load({
		callback : function() {
			p.add(get_search_result_tabpanel(this, search_id));
			p.doLayout();
		}
	});
	return p;
}

function get_search_result_tabpanel(mainStore, search_id) {
	var activetab = mainStore.reader.jsonData['active_tab'];
	var getResultContentPan = function(tabId, title) {
		var getSearchQueListStore = function(mainStore, storeRootid, activetab) {
			var storeProxy;
			var storeSortInfo;
			if (storeRootid === activetab) {
				storeProxy = new Ext.data.MemoryProxy(mainStore.reader.jsonData);
				storeSortInfo = {
					field : mainStore.reader.jsonData['sort_col'],
					direction : mainStore.reader.jsonData['sort_order']
				};
			} else {
				storeProxy = new Ext.data.HttpProxy({
					url : function() {
						var urlStr;
						switch (storeRootid) {
							case 'que_unans_lst' :
								urlStr = getSearchUrl(search_id,
										'que_unans_lst');
								break;
							case 'que_ansed_lst' :
								urlStr = getSearchUrl(search_id,
										'que_ansed_lst');
								break;
							case 'que_popular_lst' :
								urlStr = getSearchUrl(search_id,
										'que_popular_lst');
								break;
							case 'que_faq_lst' :
								urlStr = getSearchUrl(search_id,
										'que_faq_lst');
								break;
						}
						return urlStr;
					}()
				});
				storeSortInfo = {
					field : 'que_create_timestamp',
					direction : 'DESC'
				};
			}
			var store = new Ext.data.Store({
				proxy : storeProxy,
				remoteSort : true,
				sortInfo : storeSortInfo,
				reader : getQueReader(storeRootid)
			});
			return store;
		};

		var queConStore = getSearchQueListStore(mainStore, tabId, activetab);

		var showSearchResult = function(value, metadata, record) {
			var que_url = 'que_detail.htm?que_id=' + record.data.que_id;
			var cat_url = 'my_know_classify.htm?kca_id=' + record.data.kca_id;
			var usr_name_url = 'javascript:showUserInfoWin('
					+ record.data.que_create_ent_id + ')';
			var divId = 'que_detail';
			if (tabId === 'que_unans_lst' || tabId === 'que_faq_lst') {
				divId = 'que_detail_unsolved';
			}
			var content_cls = '';
			if (record.data.que_content === '') {
				content_cls = 'displayNone';
			}
			var str = eval('WzbHtm.'+divId)( {
				1 : que_url,
				2 : record.data.que_title,
				3 : Wzb.unescapeHtmlLineFeed(record.data.que_content),
				4 : Wzb.l('224'),
				5 : record.data.ans_vote_for_rate,
				6 : Wzb.l('225'),
				7 : record.data.ans_vote_total,
				8 : Wzb.l('217'),
				9 : cat_url,
				10 : record.data.kca_title,
				11 : Wzb.l('408'),
				12 : Wzb.displayTime(record.data.que_create_timestamp),
				13 : Wzb.l('533'),
				14 : usr_name_url,
				15 : record.data.usr_nickname,
				16 : content_cls,
				17 : Wzb.style_width1
			});
			return str;
		};
		var sortByStore = new Ext.data.SimpleStore({
			fields : ['value', 'display'],
			data : function() {
				var simpleData = [['que_title', Wzb.l('79')],
						['que_create_timestamp', Wzb.l('219')],
						['kca_title', Wzb.l('217')]];
				if (tabId !== 'que_unans_lst' && tabId !== 'que_faq_lst' ) {
					simpleData.push(['ans_vote_total', Wzb.l('225')]);
				}
				return simpleData;
			}()
		});
		var sortByCombo = new Wzb.ComboBox({
			id : 'sortByCombo' + tabId,
			store : sortByStore,
			valueField : 'value',
			displayField : 'display',
			mode : 'local',
			triggerAction : 'all',
			width : 80,
			selectOnFocus : true,
			listeners : {
				'select' : sortResult
			}
		});
		var sortOrderStore = new Ext.data.SimpleStore({
			fields : ['value', 'display'],
			data : [['ASC', Wzb.l('255')], ['DESC', Wzb.l('256')]]
		});
		var sortOrderCombo = new Wzb.ComboBox({
			id : 'sortOrderCombo' + tabId,
			store : sortOrderStore,
			valueField : 'value',
			displayField : 'display',
			mode : 'local',
			triggerAction : 'all',
			width : 85,
			selectOnFocus : true,
			listeners : {
				'select' : sortResult
			}
		});
		var sortToolbar = new Ext.Toolbar({
			items : ['->', sortByCombo, sortOrderCombo]
		});

		var gridPan = new Wzb.GridPanel({
			id : tabId,
			title : title,
			store : queConStore,
			hideHeaders : true,
			columns : [{
				width : '100%',
				renderer : showSearchResult
			}],
			tbar : sortToolbar,
			bbar : Wzb.getPagingToolbar(queConStore),
			viewConfig : {
				emptyText : Wzb.getEmptyText()
			}
		});
		gridPan.addListener('render', function() {
			queConStore.load({
				callback : function() {
					var tmpStore;
					if (tabId === activetab) {
						tmpStore = mainStore;
					} else {
						tmpStore = queConStore;
					}
					var curTabData = tmpStore.reader.jsonData[tabId];
					sortByCombo.setValue(curTabData['sort_col']);
					sortOrderCombo.setValue(curTabData['sort_order'])
				}
			});
			if (tabId === activetab) {
				queConStore.proxy = new Ext.data.HttpProxy({
					url : getSearchUrl(search_id, activetab)
				})
			}
		});
		function sortResult(combo, record, index) {
			var sortOrderCombo = Ext.getCmp('sortOrderCombo' + tabId);
			var sortByCombo = Ext.getCmp('sortByCombo' + tabId);
			Ext.getCmp(tabId).getStore().sort(sortByCombo.getValue(),
					sortOrderCombo.getValue());
		}

		return gridPan;
	};

	var tabs = new Wzb.TabPanel({
		activeTab : activetab,
		hideBorders : true,
		items : [getResultContentPan('que_unans_lst', Wzb.l('214')),
				getResultContentPan('que_ansed_lst', Wzb.l('213')),
				getResultContentPan('que_popular_lst', Wzb.l('211')),
				getResultContentPan('que_faq_lst', Wzb.l('684'))]
	});
	return tabs;
}

function getQueListStore(mainStore, storeRootid, activetab, kca_id) {
	var storeProxy;
	var storeSortInfo;
	if (storeRootid === activetab) {
		storeProxy = new Ext.data.MemoryProxy(mainStore.reader.jsonData);
		storeSortInfo = {
			field : mainStore.reader.jsonData['sort_col'],
			direction : mainStore.reader.jsonData['sort_order']
		};
	} else {
		storeProxy = new Ext.data.HttpProxy({
			url : function() {
				var urlStr;
				switch (storeRootid) {
					case 'que_unans_lst' :
						urlStr = getQueTypeUrl(kca_id, 'que_unans_lst');
						storeSortInfo = {
							field : 'que_create_timestamp',
							direction : 'DESC'
						};
						break;
					case 'que_ansed_lst' :
						urlStr = getQueTypeUrl(kca_id, 'que_ansed_lst');
						storeSortInfo = {
							field : 'que_create_timestamp',
							direction : 'DESC'
						};
						break;
					case 'que_popular_lst' :
						urlStr = getQueTypeUrl(kca_id, 'que_popular_lst');
						storeSortInfo = {
							field : 'ans_vote_for',
							direction : 'DESC'
						};
						break;
					case 'que_faq_lst' :
						urlStr = getQueTypeUrl(kca_id, 'que_faq_lst');
						storeSortInfo = {
							field : 'que_create_timestamp',
							direction : 'DESC'
						};
						break;
				}
				return urlStr;
			}()
		});
	}
	var queReader = getQueReader(storeRootid);
	var store = new Ext.data.Store({
		proxy : storeProxy,
		remoteSort : true,
		sortInfo : storeSortInfo,
		reader : queReader
	});
	return store;
}

function getQueReader(storeRootid) {
	var queReader = new Ext.data.JsonReader({
		totalProperty : storeRootid + '["total"]',
		root : storeRootid + '["que_lst"]'
	}, [{
		name : 'kca_id'
	}, {
		name : 'kca_title'
	}, {
		name : 'que_id'
	}, {
		name : 'que_title'
	}, {
		name : 'que_content'
	}, {
		name : 'que_create_ent_id'
	}, {
		name : 'usr_nickname'
	}, {
		name : 'ans_vote_total'
	}, {
		name : 'ans_vote_for'
	}, {
		name : 'ans_vote_down'
	}, {
		name : 'ans_vote_for_rate'
	}, {
		name : 'ans_vote_down_rate'
	}, {
		name : 'que_create_timestamp'
	}, {
		name : 'ans_count'
	}]);
	return queReader;
}

function getRelatedQue(store, width) {
	if (width === undefined || width === '') {
		width = Wzb.util_col_width1;
	}
	var relatedQueGrid;
	var queStore = new Ext.data.Store({
		proxy : new Ext.data.MemoryProxy(store.reader.jsonData),
		autoLoad : true,
		reader : new Ext.data.JsonReader({
			root : 'que_relation_lst'
		}, [{
			name : 'que_id'
		}, {
			name : 'que_title'
		}, {
			name : 'que_type'
		}])
	});
	relatedQueGrid = new Wzb.GridPanel({
		title : Wzb.l('226'),
		width : width,
		hideHeaders : true,
		store : queStore,
		cls : 'bottom',
		columns : [{
			width : width,
			renderer : contentRenderer
		}],
		viewConfig : {
			emptyText : Wzb.getEmptyText()
		},
		type : 5
	});

	function contentRenderer(value, meta, record) {
		var url = '';
		url += 'que_detail.htm?que_id=' + record.data.que_id;
		var str = '';
		str += '<table>'
		str += '<tr>'
		str += '<td>'
		str += '<a class="wzb-common-link" href="' + url + '">';
		str += record.data.que_title
		str += '</a>';
		str += '</td>'
		str += '</tr>'
		str += '</table>'
		return str;
	}
	return relatedQueGrid;
}

function getKnowNav(store, title) {
	var url = 'my_know_classify.htm?kca_id='
	var str = '<a class="wzb-common-link" href="my_know.htm">'
			+ Wzb.l('223') + '</a>' + Wzb.nav_path_sep;
	if (store !== undefined && store !== '') {
		var navData = store.reader.jsonData['nav_link'];
		if (navData !== undefined) {
			var parentNav = navData['parent_nav'];
			for (var i = 0; i < parentNav.length; i++) {
				var id = parentNav[i]['id'];
				var title = parentNav[i]['title'];
				str += '<a class="wzb-common-link" href="' + url + id + '">'
						+ title + '</a>' + Wzb.nav_path_sep;
			}
			str += '<span class="wzb-common-text">' + navData['title']
					+ '</span>';
		}
	} else if (title !== undefined && title !== '') {
		str += '<span class="wzb-common-text">' + title
					+ '</span>';
	}
	return str;
}

function getQueTypeUrl(kca_id, que_type) {
	return Wzb.getDisUrl('module', 'JsonMod.know.KnowModule', 'cmd',
			'get_que_lst', 'kca_id', kca_id, 'home_ind', false, 'activetab',
			que_type, 'dir', 'desc');
}