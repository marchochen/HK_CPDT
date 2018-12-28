new Wzb.PageRenderer({
	url : Wzb.getDisUrl('module', 'JsonMod.supervise.SuperviseModule', 'cmd', 'get_my_staff'),
	fn : onready,
	wzbLayout : Wzb.layout.TYPE2
});

var rightContentId = 'rightContentId';
var staffLrnDetailId = 'staffLrnDetailId';
var treePanelId = 'treePanelId';
var advanceoptionId = 'advanceoption';
var allStaffPanelId = 'allStaffPanelId';
var staffLstId = 'staffLstId';
var staffLstReusltId = 'staffLstReusltId';

var treeSearchValueObj = {id:-1};

var main_container;
var pageStore;

function onready(mainStore) {
	pageStore = mainStore;
	Wzb.renderPageTitle(Wzb.l('26'));
	main_container = new Wzb.Panel({
		width : Wzb.util_total_width,
		renderTo: 'container',
		border : false,
		layout : 'column',
		items : [
			getStaffLeft() 
			,getStaffDetailPanel(mainStore)
		]
	});
	main_container.addClass('panel-maincontainer');
	
	var tree = Ext.getCmp(treePanelId);
	var treeRoot = tree.getRootNode();
	treeRoot.setText(getMyStaffLabel(-1))
    treeRoot.expand();
	treeRoot.eachChild(function(cNode) {
		cNode.setText(getMyStaffLabel(cNode.id));
	});
	
	function getStaffLeft() {
		var staffList = new Wzb.Panel({
			title : Wzb.l('452'),
			id: allStaffPanelId,
			autoHeight : true,
			items:[
				getGroupTreePanel(mainStore, treePanelId, false),
				getDirectStaffPanel(mainStore, staffLstId)
			]
		});

		var searchResult = new Wzb.Panel({
			border : false,
			hideBorders : true,
			items : [{}]
		});

		var doSearch = function () {
			var search_key = Ext.getDom('search_staff_str_id').value;
			if(search_key.length === 0) {
				alert(Wzb.l('472'));
				Ext.getDom('search_staff_str_id').focus();
			} else {
				Wzb.clearPanel(searchResult);
				var paramObj = {
					'search_staff_str' : search_key, 
					'group_id' : treeSearchValueObj.id
				}
				var schUrl = Wzb.getDisUrl('module', 'JsonMod.supervise.SuperviseModule', 'cmd', 'search_staff');
				var resultStore = new Ext.data.JsonStore({
					url: schUrl
				});
				resultStore.load({
					params : paramObj,
					callback: function() {
						var tmpPanel = getDirectStaffPanel(resultStore, staffLstReusltId, paramObj);
						tmpPanel.setTitle(Wzb.l('261'));
						searchResult.add(tmpPanel);
						searchResult.doLayout();
						
						changeUserLrnInfo(resultStore);
					}
				});
			}
		}
		
		var searchForm = new Wzb.FormPanel({
			border : false,
			hideBorders : true,
			bodyStyle : Wzb.style_inner_space,
			labelAlign : 'top',
			keys : [{
			    key : Ext.EventObject.ENTER,
			    fn : doSearch
			}],
			items : [{
				xtype : 'textfield',
				id: 'search_staff_str_id',
				name: 'search_staff_str',
				width : 250,
				fieldLabel : Wzb.l('454')
			}, {
				id : advanceoptionId,
				hideBorders : true,
				items : [{
					html : WzbHtm.advOption({1: Wzb.l('266') + ' >>'})
				}]
			}],
			buttonAlign : 'center',
			buttons : [{
				text : Wzb.l('400'),
				handler : doSearch
			}]
		});

		var staffSearch = new Wzb.Panel({
			title : Wzb.l('400'),
			autoHeight : true,
			border : false,
			items : [
				searchForm,
				searchResult
			]
		});
		var tabPan = new Wzb.TabPanel({
			width : Wzb.util_col_width1,
			activeTab : 0,
			items : [staffList, staffSearch]
		});
		tabPan.addListener('tabchange', function(a, b) {
			b.doLayout();
			Ext.getCmp('search_staff_str_id').focus();
		});
		tabPan.addClass('right');
		tabPan.addClass('bottom');
		return tabPan;
	}
}//onready end.

function getStaffDetailPanel(store) {
	var panel;
	if(store !== undefined && store.reader.jsonData['my_staff']['staff'] !== undefined) {
		var defaultSwitchId = 'learningCenter';//this value will be retrieved from store.
		var staffData = store.reader.jsonData['my_staff']['staff'];
		panel = new Wzb.Panel({
			width : Wzb.util_col_width2,
			id : rightContentId,
			hideBorders : true,
			title : Wzb.l('264')
		});
		
		var userPhoto = 'src=\'' + Wzb.getRelativeImagePath(staffData['usr_photo']) + '\'';
		var topPanel = new Wzb.Panel({
			bodyStyle : Wzb.style_inner_space,
			html: WzbHtm.userinfoheader(
				{
					8: Wzb.getRelativeImagePath(staffData['usr_photo']),
					9: staffData['usr_display_bil'],
					2: Wzb.l('268'),
					4: Wzb.l('16'),
					5: Wzb.l('21'),
					6: Wzb.l('269'),
					7: Wzb.l('267'),
					10: staffData['group'],
					11: staffData['ugr_display_bil'],
					1: staffData['usr_ent_id'],
					3 : Wzb.link_sep
				}
			),
			type:5
		});
		panel.add(topPanel);
	
		var bottomPan = new Wzb.Panel({
			hideBorders : true,
			id: staffLrnDetailId,
			items : [{}]
		});
		panel.add(bottomPan);
		changeSubPanel(defaultSwitchId, staffData['usr_ent_id']);
	} else {
		panel = new Wzb.Panel({
			width : Wzb.util_col_width2,
			id : rightContentId,
			hideBorders : true,
			bodyStyle : Wzb.style_inner_space,
			html: WzbHtm.noStaffLrnInfo({1:Wzb.l('456')})
		})
	}

	return panel;
}

function getDirectStaffPanel(store, panId, baseParamObj) {
	if(baseParamObj === undefined || baseParamObj === '') {
		baseParamObj = {
			'group_id' : -1
		};
	}
	var staffData = store.reader.jsonData;
	var store = new Ext.data.Store({
		proxy : new Ext.data.MemoryProxy(staffData),
		remoteSort : true,
		sortInfo : {
			field : 'usr_ste_usr_id',
			direction : 'ASC'
		},
		reader : new Ext.data.JsonReader({
			totalProperty : 'my_staff["total"]',
			root : 'my_staff["staff_lst"]'
		}, [{
			name : 'usr_ent_id'
		}, {
			name : 'usr_ste_usr_id'
		}, {
			name : 'usr_display_bil'
		}, {
			name : 'group'
		}, {
			name : 'type'
		}])
	});
	var grid = new Wzb.GridPanel({
		title : Wzb.l('263'),
		store : store,
		id: panId,
		border : false,
		autoHeight : true,
		columns : [{
			header : Wzb.l('535'),
			width : Wzb.util_col_width1 * 0.38,
			dataIndex : 'usr_ste_usr_id',
			sortable : true,
			renderer : rendererName
		}, {
			header : Wzb.l('5'),
			width : Wzb.util_col_width1 * 0.27,
			dataIndex : 'usr_display_bil',
			sortable : true,
			renderer : function(value) {
				var str = '<span class=\'wzb-common-text\'>' + value + '</span>';
				return str;
			}
		}, {
			header : Wzb.l('327'),
			width : Wzb.util_col_width1 * 0.35,
			dataIndex : 'group',
			renderer : function(value) {
				var str = '<span class=\'wzb-common-text\'>' + value + '</span>';
				return str;
			}
		}],
		viewConfig : {	emptyText : Wzb.getEmptyText()},
		bbar : Wzb.getPagingToolbar(store, 3)
	});
	grid.addListener('render', function() {
		store.load();
		store.baseParams = baseParamObj;
		var cmd = 'get_staff_lst';
		if(panId === staffLstReusltId) {
			cmd = 'search_staff';
		}
		store.proxy = new Ext.data.HttpProxy({
			url : Wzb.getDisUrl('module', 'JsonMod.supervise.SuperviseModule', 'cmd', cmd)
		});
	});

	function rendererName(value, metadata, record, rowIndex, colIndex, store) {
		var str = WzbHtm.staffNameLink(
			{
				1: record.data.usr_ent_id,
				2: record.data.usr_ste_usr_id
			}	
		);
		return str;
	}

	return grid;
}

function getGroupTreePanel(mainStore, treeId, isSearch) {
	var treeData = mainStore.reader.jsonData['my_staff']['group_lst'];
    var treeRoot = new Ext.tree.AsyncTreeNode(treeData);
    var tree = new Wzb.TreePanel({
    	loader: new Ext.tree.TreeLoader(),
		border : false,
		id: treeId,
		autoScroll : true,
		trackMouseOver : false,
		containerScroll : true,
		root: treeRoot,
    	listeners: {
    		'beforeload': changeLoader,
    		'click': function(node) {
    			if(isSearch === true) {
    				treeSearchValueObj.id = node.id;
    			} else {
					var paramObj = {
						'group_id' : node.id
					}
	    			var newStore = new Ext.data.JsonStore({
	    				url: Wzb.getDisUrl('module', 'JsonMod.supervise.SuperviseModule', 'cmd', 'get_staff_lst')
	    			})
	    			newStore.load({
	    				params : paramObj,
	    				callback: function() {
	    					//change the result list.
							Ext.getCmp(staffLstId).destroy();
							var allStaffPanel = Ext.getCmp(allStaffPanelId);
						    allStaffPanel.add(getDirectStaffPanel(newStore, staffLstId, paramObj));

				    		changeUserLrnInfo(newStore);
	    				}
	    			});
    			}
    		}
    	}
    });
    
    function changeLoader(node) {
    	tree.getLoader().dataUrl = Wzb.getDisUrl('module', 'JsonMod.supervise.SuperviseModule', 'cmd', 'get_staff_tree', 'group_id', node.id);
    }
	return tree;
}

function showAdvanceOptions(mainStore) {
	var p = Ext.getCmp(advanceoptionId);
	Wzb.clearPanel(p);
	var panel = new Wzb.Panel({
		border : false,
		items : [{
			border : false,
			columnWidth : .28,
			html : WzbHtm.schRange({1: Wzb.l('457')})
		}, getGroupTreePanel(mainStore, 'treePanelIdSearch', true)]
 	});
	p.add(panel);
	p.doLayout();
}

function showStaffList(type) {
	if (staffLeft.getActiveTab().getId() !== 'staffSearch') {
		var reloadedStore = staffLeft.items.get(0).items.get(1).getStore();
		reloadedStore.reader.meta.record = 'supervisors[@type=' + type
				+ ']/staff';
		reloadedStore.load();
	}
}

function changeSubPanel(switchId, usr_ent_id) {
	var lrnDetailPan = Ext.getCmp(staffLrnDetailId);
	Wzb.clearPanel(lrnDetailPan);
	
	var changedPanel;
	switch (switchId) {
		case 'userInfo' :
			changedPanel = getUserInfo(usr_ent_id);
			break;
		case 'recommondedItem' :
			changedPanel = getNominateCourse(true, usr_ent_id, null, false);
			break;
		case 'learningCenter' :
			changedPanel = getLearningCenterByUserId(2, 'COS', usr_ent_id);
			break;
		case 'examCenter' :
			changedPanel = getLearningCenterByUserId(2, 'EXAM', usr_ent_id);
			break;
		case 'learningPlan' :
			changedPanel = getLearningPlan(true, usr_ent_id, null);
			break;
	}
	
	changedPanel.border = false;
	changedPanel.addListener({render : function(){
		changeCurPanelInd(switchId);
	}});
	lrnDetailPan.add(changedPanel);
	lrnDetailPan.doLayout();
}

function changeCurPanelInd(id) {
	var linkNodes = Ext.query('a', Ext.getDom('actionLinks'));
	for(var i=0; i<linkNodes.length; i++) {
		var linkObj = linkNodes[i];
		if(linkObj.className === 'wzb-common-link-selected') {
			linkObj.className = 'wzb-common-link';
		}
	}
	Ext.getDom(id).className = 'wzb-common-link-selected';
}

function changeStaffInfo(usr_ent_id) {
	var url = Wzb.getDisUrl('module', 'JsonMod.supervise.SuperviseModule', 'cmd', 'get_staff_info', 'usr_ent_id', usr_ent_id);
	var staffInfoStore = new Ext.data.JsonStore({
		url : url
	})
	staffInfoStore.load({callback: function() {
		changeUserLrnInfo(staffInfoStore);
	}});
}

function changeUserLrnInfo(store) {
    Ext.getCmp(rightContentId).destroy();
	main_container.add(getStaffDetailPanel(store));
	main_container.doLayout();
}

function getMyStaffLabel(id) {
	var str = '';
	switch(id) {
		case -1:
			str = Wzb.l('452');
			break;
		case -3:
			str = Wzb.l('262');
			break;
		case -2:
			str = Wzb.l('458');
			break;
	}
	return str;
}