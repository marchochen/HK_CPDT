var isPreView = Wzb.getUrlParam('isPreView');
if(isPreView != 'true'){
	isPreView = 'false';
}
var tcr_id = Wzb.getUrlParam('tcr_id');//if is preview tcr_id is not undefined.

var cur_usr_ent_id;
new Wzb.PageRenderer({
	url : Wzb.getDisUrl('module', 'JsonMod.user.UserModule', 'cmd', 'home', 'isPreView', isPreView, 'tcr_id', tcr_id),
	fn : onready
});
var show_simplify = false;
var hasAdminLink = false;
page_width1 = 200;
page_width2 = 560;
var kk;

function onready(mainStore) {
	cur_usr_ent_id = mainStore.reader.jsonData['meta']['ent_id'];
	var cur_lang = mainStore.reader.jsonData['meta']['curLan'];
	var sys_warning = mainStore.reader.jsonData['sys_warning'];
	var supervise_app_pend = mainStore.reader.jsonData['supervise_app_pend'];
	var public_eval = mainStore.reader.jsonData['public_eval']['eval_lst'];
	var skill_eval = mainStore.reader.jsonData['skill_eval']['assessment_lst'];
	var ste_poster = mainStore.reader.jsonData['ste_poster'];
	var companyQQData = mainStore.reader.jsonData['companyQQData'];
    var defined_project_map_tm_code = mainStore.reader.jsonData['DEFINED_PROJECT_'];
	var template_infor = mainStore.reader.jsonData['template_infor'];
	var client_check;
	if (Wzb.getCookieStatus() < Wzb.status_ok || Wzb.getFlashStatus() < Wzb.status_ok ) {
		var clientObj = {
			1 : cur_lang,
			3 : Wzb.l('lab_browser_check_failed'),
			2 : ''
		}
		client_check = WzbHtm.client_check_div(clientObj);
	}

	var contentPan = new Wzb.Panel({
		border : false,
		hideBorders : true,
		layout : 'table',
		width : Wzb.util_total_width,
		html : client_check,
		layoutConfig : {
			columns : 3
		},
		defaults : {
			cellCls : 'valign-top'
		},
		items : getContentItem()
	});
	contentPan.render('container');
	contentPan.addClass('panel-maincontainer');
	
	function APPROVE_DEMAND(index){
		if (supervise_app_pend != null && supervise_app_pend != undefined && supervise_app_pend != '') {
			var app_pend_gadget = supervise_app_pend_gadget(page_width1, mainStore);
			contentPan.items.get(index).add(app_pend_gadget);
			app_pend_gadget.addClass('bottom');
		}
	}
	
	function RESEARCH_QUESTIONNAIRE(index){
		if (public_eval != null && public_eval != undefined && public_eval != '') {
			var diagnoses = diagnoses_gadget(page_width1, mainStore);
			contentPan.items.get(index).add(diagnoses);
			diagnoses.addClass('bottom');
		}
	}
	
	function ABILITY_EVALUATE_QUESTIONNAIRE(index){
		if (skill_eval != null && skill_eval != undefined && skill_eval != '') {
			var ability_que = ability_que_gadget(page_width1, mainStore);
			contentPan.items.get(index).add(ability_que);
			ability_que.addClass('bottom');
		}
	}
	
	function PUBLIC_NOTICE(index){
		var ann_gadget = announcements_gadget(page_width1, mainStore);
		contentPan.items.get(index).add(ann_gadget);
		ann_gadget.addClass('bottom');
	}
	
	function TRANING_CATALOG(index){
		var corse_gadget = course_gadget(page_width1, mainStore);
	    contentPan.items.get(index).add(corse_gadget);
	    corse_gadget.addClass('bottom');
	}
	
	function ONELINE_ANSWERS(index){
		var know_gadget = zhidao_panel(page_width1, mainStore);
	    contentPan.items.get(index).add(know_gadget);
	    know_gadget.addClass('bottom');
	}
	
	function KNOWLEDGE_CENTER(index){
		if(Wzb.has_material_center_view
			&& mainStore.reader.jsonData['material_cat']!==undefined
			&& mainStore.reader.jsonData['material_cat'].length > 0){
			var mater_gadget = material_gadget(page_width1, mainStore);
			contentPan.items.get(index).add(mater_gadget);
			mater_gadget.addClass('bottom');
		}
	}
	
    //用户职务
	function USER_POSTER(index){
		if(ste_poster !== null && ste_poster !== undefined) {
			var site_poster = site_poster_gadget(page_width2, mainStore);
			contentPan.items.get(index).add(site_poster);
			site_poster.addClass('bottom');
		}
	}
	
    //集成培训
	function INTEGRATED_TRANING(index){
		if (mainStore.reader.jsonData['integrated_learning'] !== undefined
			&& mainStore.reader.jsonData['integrated_learning']['itm_lst'] !== undefined
			&& mainStore.reader.jsonData['integrated_learning']['itm_lst'].length > 0) {
			var integrated_learning = integrated_learning_gadget(page_width2, mainStore);
			contentPan.items.get(index).add(integrated_learning);
			integrated_learning.addClass('bottom');
		}
	}
    //学习中心
	function LEARNING_CENTRE(index){
		var learn_center = learning_center_gadget(page_width2, mainStore);
	    contentPan.items.get(index).add(learn_center);
	    learn_center.addClass('bottom');
	}
	
    //考试中心
	function EXAM_CENTRE(index){
		var exam_center = exam_center_gadget(page_width2, mainStore);
	    contentPan.items.get(index).add(exam_center);
	    exam_center.addClass('bottom');
	}
	
    //学习小组
	function STUDY_GROUP(index){
		if(Wzb.has_my_team_view || isPreView == 'true'){
			try{
				var my_sgp = my_sgp_gadget(page_width2, mainStore);
			    contentPan.items.get(index).add(my_sgp);
			    my_sgp.addClass('bottom');
			}catch(e){
				if(isPreView == 'true'){
					alert('the study group had no my team view');
				}
			}
		}
	}
	
	function SYSTEM_INFORS(index){
		if (sys_warning == true) {
			var sys_warn = sys_warn_gadget(page_width1);
			contentPan.items.get(index).add(sys_warn);
			sys_warn.addClass('bottom');
		}
	}
    
	function PRIVATE_INFORS(index){
		var welcome = welcome_gadget(page_width1, mainStore);
		contentPan.items.get(index).add(welcome);
		welcome.addClass('bottom');
	}
	
	function CREDIT_RANKING(index){
		if (mainStore.reader.jsonData['credit_rank'] !== undefined
			&& mainStore.reader.jsonData['credit_rank'].length > 0) {
			var rank_credit = rank_credit_gadget(page_width1, mainStore);
			contentPan.items.get(index).add(rank_credit);
			rank_credit.addClass('bottom');
		}
	}
	

	/*var pending_waiting_item = pending_waiting_item_gadget(page_width1,
			mainStore);
	contentPan.items.get(2).add(pending_waiting_item);
	pending_waiting_item.addClass('bottom');*/
	function TRANING_RANKING(index){
		var rank_item = rank_item_gadget(page_width1, mainStore);
	    contentPan.items.get(index).add(rank_item);
	    rank_item.addClass('bottom');
	}
	
	function FRIEND_LINK(index){
		if (mainStore.reader.jsonData['fs_link'] !== undefined
		    && mainStore.reader.jsonData['fs_link'].length > 0) {
			var fs_link = fs_link_gadget(page_width1, mainStore);
			contentPan.items.get(index).add(fs_link);
			fs_link.addClass('bottom');
		}
	}
	
	function QQ_CONSULTATION(index){
		var companyQQPanel = companyQQTalk(page_width1, companyQQData);
        contentPan.items.get(index).add(companyQQPanel);
	    companyQQPanel.addClass('bottom');
	}
	
	/*
	 * defined project panel
	 */
	function defined_project_link(tm_code, index){
		var definedProjectInfor = defined_project_map_tm_code[tm_code];
		if(definedProjectInfor !== undefined && definedProjectInfor !== null && definedProjectInfor['defi_proj_link'].length > 0){
			var defiend_project = defiend_project_gadget(page_width1, definedProjectInfor);
	        contentPan.items.get(index).add(defiend_project);
		    defiend_project.addClass('bottom');
		}
	}
	
	/*
	 * display all panel
	 */
	var leftModuleLst = template_infor['LEFT'];
	var centreModuleLst = template_infor['CENTRE'];
	var rightModuleLst = template_infor['RIGHT'];
	
	for(var n=0; n<leftModuleLst.length; n++){
		try{
			if(leftModuleLst[n].indexOf('DEFINED_PROJECT_') == 0){
				defined_project_link(leftModuleLst[n], 0);
			}else{
				eval(leftModuleLst[n] + '(0)');
			}
		}catch(e){
			alert('not found function ' + leftModuleLst[n]);
		}
	}
	for(var n=0; n<centreModuleLst.length; n++){
		try{
			eval(centreModuleLst[n] + '(1)');
		}catch(e){
			alert('not found function ' + centreModuleLst[n]);
		}
	}
	for(var n=0; n<rightModuleLst.length; n++){
		try{
			if(rightModuleLst[n].indexOf('DEFINED_PROJECT_') == 0){
				defined_project_link(rightModuleLst[n], 2);
			}else{
				eval(rightModuleLst[n] + '(2)');
			}
		}catch(e){
			alert('not found function ' + rightModuleLst[n]);
		}
	}
	
    if(parseInt(template_infor['dis_fun_ind']) === 1){
		var botton_menu = Wzb.getBottomMenuPanel(mainStore);
		contentPan.items.get(3).add(botton_menu);
		botton_menu.addClass('bottom');
	}
	

	contentPan.doLayout();
}
function getContentItem() {
	var arr = new Array();
	arr.push({
		width : page_width1,
		cls : 'wzb-right-1'
	});
	arr.push({
		width : page_width2,
		cls : 'wzb-right-1'
	});
	arr.push({
		width : page_width1
	});
	arr.push({
		width : Wzb.util_total_width,
		colspan : 3
	});
	return arr;
}

// ??????????
function supervise_app_pend_gadget(total_width, mainStore) {

	function renderAppPend(value, metadata, record, rowIndex, colIndex, store) {
		var str = '';
		var obj = {
			1 : record.data.usr_display_bil,
			2 : record.data.itm_title
		}
		var str = WzbHtm.app_pend(obj);
		return str;
	}
	var app_pend_reader = mainStore.reader.jsonData;
	var store = new Ext.data.Store({
		proxy : new Ext.data.MemoryProxy(app_pend_reader),
		autoLoad : true,
		reader : new Ext.data.JsonReader({
			root : 'supervise_app_pend'
		}, ['app_create_timestamp', 'itm_title', 'usr_display_bil',
				'usr_ste_usr_id', 'app_id', 'app_upd_timestamp'])
	});
	var colModel = new Ext.grid.ColumnModel([{
		id : 'app_id',
		menuDisabled : true,
		resizable : false,
		dataIndex : 'app_id',
		renderer : renderAppPend,
		width : '100%'
	}]);
	var app_pendGridPan = new Wzb.GridPanel({
		store : store,
		colModel : colModel,
		autoHeight : true,
		hideHeaders : true,
		viewConfig : {
			emptyText : Wzb.getEmptyText()
		},
		bodyStyle : 'background:transparent'
	});

	var app_pendPanel = new Wzb.Panel({
		id : 'supervise_app_pend',
		title : Wzb.l('342'),
		collapsible : true,
		iconCls : 'supervise_app_pend',
		type : 2,
		tools : Wzb.getPanelTools(),
		hideBorders : true
	});
	app_pendPanel.add(app_pendGridPan);
	app_pendPanel
			.add(Wzb.getMoreInfoPanel('app_approval.htm', Wzb.l('344')));

	return app_pendPanel;
}

function post_capacity_req_gadget(total_width, mainStore) {
	function renderCapacityReq(value, p, record) {
		var obj = {
			1 : 'course_center.htm?ske_id_lst=' + record.data.ske_id,
			2 : record.data.text
		};
		var str = WzbHtm.ablt_div(obj);
		return str;
	}
	var lrn_cat_reader = mainStore.reader.jsonData['learning_catalog'];
	var store = new Ext.data.Store({
		proxy : new Ext.data.MemoryProxy(lrn_cat_reader),
		autoLoad : true,
		reader : new Ext.data.JsonReader({
			root : 'competence_list'
		}, ['text', 'choice', 'ske_id', 'id'])
	});
	var colModel = new Ext.grid.ColumnModel([{
		id : 'id',
		menuDisabled : true,
		resizable : false,
		dataIndex : 'id',
		renderer : renderCapacityReq,
		width : '100%',
		align : 'left'
	}]);
	var post_capacity_req_GridPan = new Wzb.GridPanel({
		id : 'post_capacity_req',
		collapsible : true,
		store : store,
		colModel : colModel,
		autoHeight : true,
		hideHeaders : true,
		viewConfig : {
			emptyText : Wzb.getEmptyText()
		},
		bodyStyle : 'background:transparent'
	});
	return post_capacity_req_GridPan;
}
// ???????
function course_gadget(total_width, mainStore) {
	var panel = new Wzb.Panel({
		id : 'course',
		title : Wzb.l('11'),
		collapsible : true,
		iconCls : 'course',
		hideBorders : true,
		tools : Wzb.getPanelTools(),
		type : 2
	});
	var cata_title_panel = Wzb.getSubTitlePanel(Wzb.l('37'));
	var pos_title_panel = Wzb.getSubTitlePanel(Wzb.l('38'));

	var competence = mainStore.reader.jsonData['learning_catalog']['competence_list'];
	var lrn_cata_panel = learning_catalog_gadget(total_width, mainStore);
	var pos_panel = post_capacity_req_gadget(total_width, mainStore);
	panel.add(cata_title_panel);
	panel.add(lrn_cata_panel);
	if (competence != null && competence != undefined && competence != '') {
		panel.add(pos_title_panel);
		panel.add(pos_panel);
	}
	panel.add(Wzb.getMoreInfoPanel('course_center.htm'));
	return panel;
}

function learning_catalog_gadget(total_width, mainStore) {
	function renderLrnCat(value, p, record) {
		if (!record.data.count) {
			record.data.count = 0;
		}
		var str = '';
		var sub_str = '';
		if (record.data.children !== undefined) {
			var obj = {};
			for (var i = 0; i < record.data.children.length; i++) {
				if (i === 3) {
					obj['2'] = '...';
					obj['1'] = 'course_center.htm?tnd_id='
							+ record.data.tnd_id;
					sub_str += WzbHtm.cata_div(obj);
					break;
				} else {
					if (i > 0) {
						sub_str += Wzb.link_sep;
					}
					obj['2'] = record.data.children[i].text;
					obj['1'] = 'course_center.htm?tnd_id='
							+ record.data.children[i].tnd_id;
					sub_str += WzbHtm.cata_div(obj);
				}
			}
		}
		var top_cata = {
			2 : record.data.text,
			3 : record.data.count,
			1 : 'course_center.htm?tnd_id=' + record.data.tnd_id,
			4 : sub_str
		}
		str = WzbHtm.top_cata(top_cata);

		return str;
	}
	var lrn_cat_reader = mainStore.reader.jsonData['learning_catalog'];
	var store = new Ext.data.Store({
		proxy : new Ext.data.MemoryProxy(lrn_cat_reader),
		autoLoad : true,
		reader : new Ext.data.JsonReader({
			root : 'tnd_lst'
		},
				['text', 'tnd_title', 'tnd_id', 'tcr_id', 'id', 'children',
						'count'])

	});

	var colModel = new Ext.grid.ColumnModel([{
		id : 'id',
		menuDisabled : true,
		resizable : false,
		dataIndex : 'name',
		renderer : renderLrnCat,
		width : '100%'
	}]);
	var lrnCatGridPan = new Wzb.GridPanel({
		id : 'learning_catalog',
		collapsible : true,
		store : store,
		colModel : colModel,
		autoHeight : true,
		hideHeaders : true,
		viewConfig : {
			emptyText : Wzb.getEmptyText()
		},
		bodyStyle : 'background:transparent'
	});
	return lrnCatGridPan;
}

function material_gadget(total_width, mainStore) {
	var panel = new Wzb.Panel({
		id : 'material',
		title : Wzb.l('695'),
		collapsible : true,
		iconCls : 'material',
		hideBorders : true,
		tools : Wzb.getPanelTools(),
		type : 2
	});
	var material_cata_panel = material_catalog_gadget(total_width, mainStore);
	panel.add(material_cata_panel);
	panel.add(Wzb.getMoreInfoPanel('material_center.htm'));
	return panel;
}

function material_catalog_gadget(total_width, mainStore) {
	function renderMaterialCat(value, p, record) {
		if (!record.data.count) {
			record.data.count = 0;
		}
		var str = '';
		var sub_str = '';
		if (record.data.children !== undefined) {
			var obj = {};
			for (var i = 0; i < record.data.children.length; i++) {
				if (i === 3) {
					obj['2'] = '...';
					obj['1'] = 'material_center.htm?cat_id='
							+ record.data.id;
					sub_str += WzbHtm.cata_div(obj);
					break;
				} else {
					if (i > 0) {
						sub_str += Wzb.link_sep;
					}
					obj['2'] = record.data.children[i].text;
					obj['1'] = 'material_center.htm?cat_id='
							+ record.data.children[i].id;
					sub_str += WzbHtm.cata_div(obj);
				}
			}
		}
		var top_cata = {
			2 : record.data.text,
			3 : record.data.count,
			1 : 'material_center.htm?cat_id=' + record.data.id,
			4 : sub_str
		}
		str = WzbHtm.top_cata(top_cata);

		return str;
	}
	var material_cat_reader = mainStore.reader.jsonData;
	var store = new Ext.data.Store({
		proxy : new Ext.data.MemoryProxy(material_cat_reader),
		autoLoad : true,
		reader : new Ext.data.JsonReader({
			root : 'material_cat'
		},
				['text', 'desc', 'id', 'tcr_id', 'children','count'])

	});

	var colModel = new Ext.grid.ColumnModel([{
		id : 'id',
		menuDisabled : true,
		resizable : false,
		dataIndex : 'name',
		renderer : renderMaterialCat,
		width : '100%'
	}]);
	var materialCatGridPan = new Wzb.GridPanel({
		id : 'material_catalog',
		collapsible : true,
		store : store,
		colModel : colModel,
		autoHeight : true,
		hideHeaders : true,
		viewConfig : {
			emptyText : Wzb.getEmptyText()
		},
		bodyStyle : 'background:transparent'
	});
	return materialCatGridPan;
}

// ?????
function zhidao_panel(total_width, mainStore) {
	var obj = {
		1 : Wzb.link_sep,
		2 : Wzb.l('252'),
		3 : 'javascript:getZhidaoPanel(\'pending\')',
		4: Wzb.l('253'),
		5 : 'javascript:getZhidaoPanel(\'solved\')',
		6 : Wzb.l('254'),
		7 : 'javascript:getZhidaoPanel(\'picked\')',
		8 : 'javascript:getZhidaoPanel(\'faq\')',
		9 : Wzb.l('684')
	};
	var knowHtml = WzbHtm.know(obj);
	var title_panel = new Wzb.Panel({
		id : 'know_title',
		collapsible : true,
		hideBorders : true,
		html : knowHtml,
		bodyStyle : 'background:transparent'
	});
	var panel = new Wzb.Panel({
		id : 'know',
		title : Wzb.l('345'),
		collapsible : true,
		iconCls : 'know',
		hideBorders : true,
		tools : Wzb.getPanelTools(),
		type : 2
	});
	panel.add(title_panel);
	var data_panel = zhidao_gadget(total_width, mainStore);
	panel.add(data_panel);

	return panel;
}

function getKnowReader(panelType) {
	var root;
	if (panelType == 'pending') {
		root = 'que_unans_lst["que_lst"]';
		Ext.getDom('pending').className = 'wzb-common-link-selected';
		Ext.getDom('solved').className = 'wzb-common-link';
		Ext.getDom('picked').className = 'wzb-common-link';
		Ext.getDom('faq').className = 'wzb-common-link';

	} else if (panelType == 'solved') {
		root = 'que_ansed_lst["que_lst"]';
		Ext.getDom('pending').className = 'wzb-common-link';
		Ext.getDom('solved').className = 'wzb-common-link-selected';
		Ext.getDom('picked').className = 'wzb-common-link';
		Ext.getDom('faq').className = 'wzb-common-link';
	} else if (panelType == 'picked') {
		root = 'que_popular_lst["que_lst"]';
		Ext.getDom('pending').className = 'wzb-common-link';
		Ext.getDom('solved').className = 'wzb-common-link';
		Ext.getDom('picked').className = 'wzb-common-link-selected';
		Ext.getDom('faq').className = 'wzb-common-link';
	} else if (panelType == 'faq') {
		root = 'que_faq_lst["que_lst"]';
		Ext.getDom('pending').className = 'wzb-common-link';
		Ext.getDom('solved').className = 'wzb-common-link';
		Ext.getDom('picked').className = 'wzb-common-link';
		Ext.getDom('faq').className = 'wzb-common-link-selected';
	}
	var reader = new Ext.data.JsonReader({
		root : root
	}, ['que_title', 'kca_title', 'que_id', 'kca_id']);

	return reader;
}

function getZhidaoPanel(panelType) {
	var grid = Ext.getCmp('zhidao_grid');
	var store = grid.getStore();
	store.panelType = panelType;

	if (panelType == 'pending') {
		store.proxy = new Ext.data.HttpProxy({
			url : Wzb.getDisUrl('module', 'JsonMod.user.UserModule', 'cmd',
					'home_gadget', 'type', 'que_unans_lst')
		})

	} else if (panelType == 'solved') {
		store.proxy = new Ext.data.HttpProxy({
			url : Wzb.getDisUrl('module', 'JsonMod.user.UserModule', 'cmd',
					'home_gadget', 'type', 'que_ansed_lst')
		});
	} else if (panelType == 'picked') {
		store.proxy = new Ext.data.HttpProxy({
			url : Wzb.getDisUrl('module', 'JsonMod.user.UserModule', 'cmd',
					'home_gadget', 'type', 'que_popular_lst')
		})
	} else if (panelType == 'faq') {
		store.proxy = new Ext.data.HttpProxy({
			url : Wzb.getDisUrl('module', 'JsonMod.user.UserModule', 'cmd',
					'home_gadget', 'type', 'que_faq_lst')
		})
	}
	store.reader = getKnowReader(panelType);
	store.load({
		callback : function() {
			grid.doLayout();
		}
	});

}

function zhidao_gadget(total_width, mainStore) {
	function renderQue(value, p, record, rowIndex, colIndex, store) {
		var obj = {
			1 : record.data.kca_title,
			4 : 'my_know_classify.htm?kca_id=' + record.data.kca_id,
			3 : record.data.que_title,
			2 : 'que_detail.htm?que_id=' + record.data.que_id
		}
		var str = WzbHtm.que_div(obj);
		return str;
	}
	var zhidao_pending_reader = mainStore.reader.jsonData;
	var store = new Ext.data.Store({
		activetab : 'que_unans_lst',
		proxy : new Ext.data.MemoryProxy(zhidao_pending_reader),
		autoLoad : true,
		reader : new Ext.data.JsonReader({
			root : 'que_unans_lst["que_lst"]'
		}, ['que_title', 'kca_title', 'que_id', 'kca_id'])
	});
	var colModel = new Ext.grid.ColumnModel([{
		id : 'que_id',
		menuDisabled : true,
		resizable : false,
		dataIndex : 'que_id',
		renderer : renderQue,
		width : '100%',
		align : 'left'
	}]);
	var zhidao_grid = new Wzb.GridPanel({
		id : 'zhidao_grid',
		border : false,
		store : store,
		autoHeight : true,
		hideHeaders : true,
		viewConfig : {
			scrollOffset : 2
		},
		colModel : colModel,
		viewConfig : {
			emptyText : Wzb.getEmptyText()
		},
		bodyStyle : 'background:transparent'
	});
	store.load();

	var panel = new Wzb.Panel({
		id : 'zhidao_pending',
		border : false,
		hideBorders : true,
		bodyStyle : 'background:transparent'
	});
	panel.add(zhidao_grid);
	panel.add(Wzb.getMoreInfoPanel('javascript:getKnowMoreLink()'));
	return panel;
}

function getKnowMoreLink() {
	var knowType = Ext.getCmp('zhidao_grid').store.panelType;
	var linkStr = 'my_know.htm';
	if (knowType == 'pending') {
		linkStr += '?activetab=que_unans_lst';
	} else if (knowType == 'solved') {
		linkStr += '?activetab=que_ansed_lst';
	} else if (knowType == 'picked') {
		linkStr += '?activetab=que_popular_lst';
	} else if (knowType == 'faq') {
		linkStr += '?activetab=que_faq_lst';
	}
	window.location.href = linkStr;
}

function announcements_gadget(total_width, mainStore) {

	function renderAnn(value, p, record) {
		var str_feature = 'toolbar=' + 'no' + ',width=' + 480
				+ ',height=' + 200
				+ ',resizable=' + 'yes' + ',status=' + 'no'
		var msg_url = Wzb.getQdbUrl('cmd', 'get_msg', 'msg_type',
				record.data.msg_type, 'msg_id', record.data.msg_id,
				'stylesheet', 'announ_dtl.xsl', 'url_failure',
				'../htm/close_window.htm')

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
		var str = WzbHtm.ann_div(obj);
		return str;
	}
	var ann_reader = mainStore.reader.jsonData['announcements']
	var store = new Ext.data.Store({
		proxy : new Ext.data.MemoryProxy(ann_reader),
		autoLoad : true,
		reader : new Ext.data.JsonReader({
			root : 'ann_lst'
		}, ['is_content_cut', 'msg_title', 'usr_display_bil', 'msg_begin_date',
				'msg_id', 'msg_body', 'msg_desc', 'newest_ind'])

	});
	var colModel = new Ext.grid.ColumnModel([{
		width : '100%',
		dataIndex : 'msg_title',
		renderer : renderAnn
	}]);

//	var ann_img = new Wzb.Panel({
//		border : false,
//		html : WzbHtm.ann_top_image()
//
//	})
	var panel = new Wzb.Panel({
		id : 'announcements',
		title : Wzb.l('31'),
		collapsible : true,
		iconCls : 'announcements',
		tools : Wzb.getPanelTools(),
		type : 2
	})
	var announce_grid = new Wzb.GridPanel({
		border : false,
		store : store,
		autoHeight : true,
		enableColumnMove : false,
		cm : colModel,
		hideHeaders : true,
		//items : [ann_img],
		viewConfig : {
			emptyText : Wzb.getEmptyText()
		},
		bodyStyle : 'background:transparent'
	});

	panel.add(announce_grid);
	panel.add(Wzb.getMoreInfoPanel('announcements.htm'));
	return panel;
}

function my_sgp_gadget(total_width, mainStore) {

	function renderMyTeamName(value, metadata, record, rowIndex, colIndex,
			store) {
		var obj = {
			1 : 'my_team_details.htm?sgp_id=' + record.data.sgp_id,
			2 : record.data.sgp_title
		};
		var str = WzbHtm.sgp_div(obj);
		return str;
	}

	var colModel = new Ext.grid.ColumnModel([{
		id : 'sgp',
		menuDisabled : true,
		resizable : false,
		dataIndex : 'sgp_name',
		renderer : renderMyTeamName,
		width : '100%'
	}]);
	var reader = new Ext.data.JsonReader({
		root : 'my_sgp["sgp_lst"]'
	}, ['sgp_id', 'sgp_title', 'sgp_type', 'sgp_tcr_id']);

	var mainData_sgp = mainStore.reader.jsonData;
	var mySgpStore = new Ext.data.Store({
		remoteSort : true,
		proxy : new Ext.data.MemoryProxy(mainData_sgp),
		reader : reader
	});
	var mySgpGridPan = new Wzb.GridPanel({
		id : 'mysgp',
		store : mySgpStore,
		colModel : colModel,
		autoHeight : true,
		hideHeaders : true,
		viewConfig : {
			emptyText : Wzb.getEmptyText()
		},
		bodyStyle : 'background:transparent',
		isChangeProxy : true,
		url : Wzb.getDisUrl('module', 'JsonMod.user.UserModule', 'cmd',
				'home_gadget', 'type', 'my_sgp')
	});
	mySgpGridPan.addListener({
		'render' : Wzb.renderEventFn
	});
	var panel = new Wzb.Panel({
		type : 2,
		id : 'my_sgp',
		title : Wzb.l('34'),
		collapsible : true,
		iconCls : 'my_sgp',
		hideBorders : true,
		tools : Wzb.getPanelTools(),
		autoHeight : true
	});
	panel.add(mySgpGridPan);
	panel.add(Wzb.getMoreInfoPanel('my_team_list.htm'));
	return panel;
}

// ??????
function learning_center_gadget(total_width, mainData) {
	function getCosSummary(id, itemData) {
		var getCosSummaryHtml = function(id, itm_lst) {
			var is_progress = (id === 'learning_center')? true : false;
			var is_finished = (id === 'cos_hist')? true : false;
			var is_pendding = (id === 'cos_pend')? true : false;

			var obj = {
				study_center_content_main_table : '',
				learning_center_1 : 'display:none',
				learning_center_2 : 'display:none',
				learning_center_3 : 'display:none',
				learning_center_4 : 'display:none',
				learning_center_separator : 'display:none'
			};
			var learning_center_str = '';
			if (itm_lst !== undefined && itm_lst.length > 0) {
				for (var i = 0; i < itm_lst.length; i++) {
					obj['progress_class_' + (i + 1)] = '';
					obj['10'] = '';
					if(is_progress) {
						obj['10'] = 'displayNone';
						if(itm_lst[i].comp_criteria !== undefined && itm_lst[i].comp_criteria.is_available === true) {
							obj['4'] = Wzb.l('39');
							obj['5_' + (i + 1)] = 'style=\'width:' + itm_lst[i].cov_progress + '%\'';
						} else {
							obj['progress_class_' + (i + 1)] = 'displayNone';
						}
					} else if (is_finished) {
						obj['progress_class_' + (i + 1)] = 'displayNone';
						obj['11_' + (i + 1)] = Wzb.l('113');
						obj['12_' + (i + 1)] = Wzb.displayTimeDefValue(itm_lst[i].att_timestamp, Wzb.time_format_ymd, '--');
					} else if(is_pendding) {
						obj['progress_class_' + (i + 1)] = 'displayNone';
						obj['11_' + (i + 1)] = Wzb.l('118');
						obj['12_' + (i + 1)] = (itm_lst[i].usr_display_bil === undefined || itm_lst[i].usr_display_bil === '') ? '--' : itm_lst[i].usr_display_bil;
					}
					obj['2_' + (i + 1)] = itm_lst[i].itm_title;
					obj['3_' + (i + 1)] = itm_lst[i].lab_itm_type;

					obj['6'] = Wzb.l('40');
					obj['7_' + (i + 1)] = Wzb.displayTimeDefValue(itm_lst[i].cov_last_acc_datetime, Wzb.time_format_ymd, '--');
					if(is_pendding) {
						obj['6'] = Wzb.l('119');//等待审批者
						obj['7_' + (i + 1)] = function() {
							var next_approver = '';
							for (var j = 0; j < itm_lst[i].next_approver.length; j++) {
								if (j > 0) {
									next_approver += ',' + itm_lst[i].next_approver[j].name;
								} else {
									next_approver += itm_lst[i].next_approver[j].name;
								}
							}
							return next_approver;
						}();
					}

					obj['1_' + (i + 1)] = Wzb.getCourseImage(
							itm_lst[i].itm_icon, itm_lst[i].itm_dummy_type);
					if (!itm_lst[i].cov_progress) {
						itm_lst[i].cov_progress = 0;
					}

					if(is_pendding) {
						obj['13'] = 'displayNone';
					} else {
						obj['13'] = '';
						var btn_label = Wzb.l('41');//进入
						if(is_finished) {
							btn_label = Wzb.l('389');//回顾
						}
						obj['9_' + (i + 1)] = Wzb.getButton(
								'location=\'course_home.htm?itm_id=' + itm_lst[i].app_itm_id
										+ '&tkh_id=' + itm_lst[i].cov_tkh_id + '&res_id='
										+ itm_lst[i].cos_res_id + '\'', btn_label);
					}

					obj['learning_center_' + (i + 1)] = '';
				}

				if (itm_lst.length > 2) {
					obj['learning_center_separator'] = '';
				}
				learning_center_str = WzbHtm.learning_center(obj);
			} else {
				obj['study_center_content_main_table'] = 'display:none';
				obj['6'] = Wzb.l('617');
				learning_center_str = WzbHtm.wzb_no_itm(obj);
			}

			return learning_center_str;
		}

		var titleObj = {};
		titleObj['learning_center'] = Wzb.l('19');
		titleObj['cos_pend'] = Wzb.l('46');
		titleObj['cos_hist'] = Wzb.l('401');

		var indexObj = {};
		indexObj['learning_center'] = 2;
		indexObj['cos_pend'] = 1;
		indexObj['cos_hist'] = 3;

		var p = new Wzb.Panel({
			id : id + '_COS',
			title : titleObj[id],
			autoHeight : true,
			border : false,
			hideBorders : true
		});
		var itm_lst = [];
		if(itemData !== undefined) {
			itm_lst = itemData.reader.jsonData[id]['itm_lst'];
		}
		var drawContent = function(data) {
			p.add({
				html : getCosSummaryHtml(id, data)
			});
			if (data.length != 0) {
				p.add(Wzb.getMoreInfoPanel('learning_center.htm?type=COS&activetab=' + indexObj[id]));
			}
			p.doLayout();
		}

		p.addListener('render', function() {
			if(id !== 'learning_center') {
				Ext.Ajax.request({
					url : Wzb.getLcUrl(indexObj[id], 'COS'),
					callback : function(options, success, response) {
						if(success) {
							var item_data = eval('(' + response.responseText + ')');
							itm_lst_data = item_data[id]['itm_lst'];
							drawContent(itm_lst_data);
						}
					}
				})
			} else {
				drawContent(itm_lst);
			}
		});
		return p ;
	}

	var lrnCenTabPanel = new Wzb.TabPanel({
		activeTab : 0,
		width: total_width,
		plain : true,
		type : 3,
		items : [
			getCosSummary('learning_center', mainData),
			getCosSummary('cos_pend'),
			getCosSummary('cos_hist')
		]
	});
	return lrnCenTabPanel;
}


function integrated_learning_gadget(total_width, mainData) {
	function getIntegratedLearningSummary(id, itemData) {
		var getIntegratedLearningSummaryHtml = function(id, itm_lst) {
			var is_progress = (id === 'integrated_learning')? true : false;
			var is_finished = (id === 'cos_hist')? true : false;
			var is_pendding = (id === 'cos_pend')? true : false;

			var obj = {
				integrated_learning_content_main_table : '',
				integrated_learning_1 : 'display:none',
				integrated_learning_2 : 'display:none',
				integrated_learning_3 : 'display:none',
				integrated_learning_4 : 'display:none',
				integrated_learning_separator : 'display:none'
			};
			var integrated_learning_str = '';
			if (itm_lst !== undefined && itm_lst.length > 0) {
				for (var i = 0; i < itm_lst.length; i++) {
					obj['progress_class_' + (i + 1)] = '';
					obj['10'] = '';
					if(is_progress) {
						obj['10'] = 'displayNone';
						if(itm_lst[i].cov_progress) {
							obj['4'] = Wzb.l('39');
							obj['5_' + (i + 1)] = 'style=\'width:' + itm_lst[i].cov_progress + '%\'';
						} else {
							obj['progress_class_' + (i + 1)] = 'displayNone';
						}
					} else if (is_finished) {
						obj['progress_class_' + (i + 1)] = 'displayNone';
						obj['11_' + (i + 1)] = Wzb.l('113');
						obj['12_' + (i + 1)] = Wzb.displayTimeDefValue(itm_lst[i].att_timestamp, Wzb.time_format_ymd, '--');
					} else if(is_pendding) {
						obj['progress_class_' + (i + 1)] = 'displayNone';
						obj['11_' + (i + 1)] = Wzb.l('118');
						obj['12_' + (i + 1)] = (itm_lst[i].usr_display_bil === undefined || itm_lst[i].usr_display_bil === '') ? '--' : itm_lst[i].usr_display_bil;
					}
					obj['2_' + (i + 1)] = itm_lst[i].itm_title;
					obj['3_' + (i + 1)] = itm_lst[i].lab_itm_type;

					obj['6'] = Wzb.l('40');
					obj['7_' + (i + 1)] = Wzb.displayTimeDefValue(itm_lst[i].cov_last_acc_datetime, Wzb.time_format_ymd, '--');
					if(is_pendding) {
						obj['6'] = Wzb.l('119');//等待审批者
						obj['7_' + (i + 1)] = function() {
							var next_approver = '';
							for (var j = 0; j < itm_lst[i].next_approver.length; j++) {
								if (j > 0) {
									next_approver += ',' + itm_lst[i].next_approver[j].name;
								} else {
									next_approver += itm_lst[i].next_approver[j].name;
								}
							}
							return next_approver;
						}();
					}

					obj['1_' + (i + 1)] = Wzb.getCourseImage(itm_lst[i].itm_icon, itm_lst[i].itm_dummy_type);
					if (!itm_lst[i].cov_progress) {
						itm_lst[i].cov_progress = 0;
					}

					if(is_pendding) {
						obj['13'] = 'displayNone';
					} else {
						obj['13'] = '';
						var btn_label = Wzb.l('41');//进入
						if(is_finished) {
							btn_label = Wzb.l('389');//回顾
						}
						obj['9_' + (i + 1)] = Wzb.getButton(
								'location=\'course_home.htm?itm_id=' + itm_lst[i].app_itm_id
										+ '&tkh_id=' + itm_lst[i].cov_tkh_id + '&res_id='
										+ itm_lst[i].cos_res_id + '\'', btn_label);
					}

					obj['integrated_learning_' + (i + 1)] = '';
				}

				if (itm_lst.length > 2) {
					obj['integrated_learning_separator'] = '';
				}
				inte_center_str = WzbHtm.integrated_learning(obj);
			} else {
				obj['integrated_learning_content_main_table'] = 'display:none';
				obj['6'] = Wzb.l('617');
				inte_center_str = WzbHtm.wzb_no_itm(obj);
			}
			return inte_center_str;
		}

		var titleObj = {};
		titleObj['integrated_learning'] = Wzb.l('908');
		titleObj['cos_pend'] = Wzb.l('46');
		titleObj['cos_hist'] = Wzb.l('401');

		var indexObj = {};
		indexObj['integrated_learning'] = 2;
		indexObj['cos_pend'] = 1;
		indexObj['cos_hist'] = 3;

		var p = new Wzb.Panel({
			id : id + '_COS',
			title : titleObj[id],
			autoHeight : true,
			border : false,
			hideBorders : true
		});
		var itm_lst = [];
		if(itemData !== undefined) {
			itm_lst = itemData.reader.jsonData[id]['itm_lst'];
		}
		var drawContent = function(data) {
			p.add({
				html : getIntegratedLearningSummaryHtml(id, data)
			});
			if (data.length != 0) {
				p.add(Wzb.getMoreInfoPanel('learning_center.htm?type=INTEGRATED&activetab=' + indexObj[id]));
			}
			p.doLayout();
		}

		p.addListener('render', function() {
			if(id !== 'integrated_learning') {
				Ext.Ajax.request({
					url : Wzb.getLcUrl(indexObj[id], 'INTEGRATED'),
					callback : function(options, success, response) {
						if(success) {
							var item_data = eval('(' + response.responseText + ')');
							itm_lst_data = item_data[id]['itm_lst'];
							drawContent(itm_lst_data);
						}
					}
				})
			} else {
				drawContent(itm_lst);
			}
		});
		return p ;
	}

	var lrnCenTabPanel = new Wzb.TabPanel({
		activeTab : 0,
		width: total_width,
		plain : true,
		type : 3,
		items : [
			getIntegratedLearningSummary('integrated_learning', mainData),
			getIntegratedLearningSummary('cos_pend'),
			getIntegratedLearningSummary('cos_hist')
		]
	});
	return lrnCenTabPanel;
}

// ????????
function exam_center_gadget(total_width, mainData) {
	function getExamSummary(id, itemData) {
		var getExamSummaryHtml = function(id, itm_lst) {
			var is_progress = (id === 'exam_center')? true : false;
			var is_finished = (id === 'cos_hist')? true : false;
			var is_pendding = (id === 'cos_pend')? true : false;

			var obj = {
				exam_center_content_main_table : '',
				exam_center_1 : 'display:none',
				exam_center_2 : 'display:none',
				exam_center_3 : 'display:none',
				exam_center_4 : 'display:none',
				exam_center_separator : 'display:none'
			};
			var exam_center_str = '';
			if (itm_lst !== undefined && itm_lst.length > 0) {
				for (var i = 0; i < itm_lst.length; i++) {
					obj['1_' + (i + 1)] = Wzb.getCourseImage(itm_lst[i].itm_icon, itm_lst[i].itm_dummy_type);
					obj['2_' + (i + 1)] = itm_lst[i].itm_title;
					obj['3_' + (i + 1)] = itm_lst[i].lab_itm_type;

					obj['8'] = Wzb.l('40');//上次访问时间
					obj['4_' + (i + 1)] = Wzb.displayTimeDefValue(itm_lst[i].cov_last_acc_datetime, Wzb.time_format_ymd, '--');
					if(is_pendding) {
						obj['8'] = Wzb.l('119');//等待审批者
						obj['4_' + (i + 1)] = function() {
							var next_approver = '';
							for (var j = 0; j < itm_lst[i].next_approver.length; j++) {
								if (j > 0) {
									next_approver += ',' + itm_lst[i].next_approver[j].name;
								} else {
									next_approver += itm_lst[i].next_approver[j].name;
								}
							}
							return next_approver;
						}();
					}

					if(is_pendding) {
						obj['6'] = 'displayNone';
					} else {
						obj['6'] = '';
						var btn_label = Wzb.l('41');//进入
						if(is_finished) {
							btn_label = Wzb.l('389');//回顾
						}
						obj['5_' + (i + 1)] = Wzb.getButton(
								'location=\'course_home.htm?itm_id=' + itm_lst[i].app_itm_id
										+ '&tkh_id=' + itm_lst[i].cov_tkh_id + '&res_id='
										+ itm_lst[i].cos_res_id + '\'', btn_label);
					}

					obj['exam_center_' + (i + 1)] = '';
				}

				if (itm_lst.length > 2) {
					obj['exam_center_separator'] = '';
				}
				exam_center_str = WzbHtm.exam_center(obj);
			} else {
				obj['exam_center_content_main_table'] = 'display:none';
				obj['6'] = Wzb.l('618');
				exam_center_str = WzbHtm.wzb_no_itm(obj);
			}

			return exam_center_str;
		}

		var titleObj = {};
		titleObj['exam_center'] = Wzb.l('24');
		titleObj['cos_pend'] = Wzb.l('46');
		titleObj['cos_hist'] = Wzb.l('401');

		var indexObj = {};
		indexObj['exam_center'] = 2;
		indexObj['cos_pend'] = 1;
		indexObj['cos_hist'] = 3;

		var p = new Wzb.Panel({
			id : id + '_EXAM',
			title : titleObj[id],
			autoHeight : true,
			border : false,
			hideBorders : true
		});
		var itm_lst = [];
		if(itemData !== undefined) {
			itm_lst = itemData.reader.jsonData[id]['itm_lst'];
		}
		var drawContent = function(data) {
			p.add({
				html : getExamSummaryHtml(id, data)
			});
			if (data.length != 0) {
				p.add(Wzb.getMoreInfoPanel('learning_center.htm?type=EXAM&activetab=' + indexObj[id]));
			}
			p.doLayout();
		}

		p.addListener('render', function() {
			if(id !== 'exam_center') {
				Ext.Ajax.request({
					url : Wzb.getLcUrl(indexObj[id], 'EXAM'),
					callback : function(options, success, response) {
						if(success) {
							var item_data = eval('(' + response.responseText + ')');
							itm_lst_data = item_data[id]['itm_lst'];
							drawContent(itm_lst_data);
						}
					}
				})
			} else {
				drawContent(itm_lst);
			}
		});
		return p ;
	}

	var examCenTabPanel = new Wzb.TabPanel({
		activeTab : 0,
		width: total_width,
		plain : true,
		items : [
			getExamSummary('exam_center', mainData),
			getExamSummary('cos_pend'),
			getExamSummary('cos_hist')
		]
	});
	return examCenTabPanel;
}

function sys_warn_gadget(total_width) {
	var obj = {
		1 : Wzb.l('347'),
		2 : Wzb.l('346')
	};
	var sys_warn_html = WzbHtm.system_warning(obj)
	var sys_warn_panel = new Wzb.Panel({
		header : false,
		border : false,
		html : sys_warn_html
	});
	return sys_warn_panel;
}

function welcome_gadget(total_width, mainData) {
	var usr_info = mainData.reader.jsonData['usr_info'];
	var obj = {};
	obj['1'] = Wzb.l('348');
	if (usr_info.last_login_timestamp == null
			|| usr_info.last_login_timestamp == undefined) {
		obj['6'] = '';
		obj['7'] = '';
		obj['8'] = '';
	} else {
		obj['6'] = Wzb.l('42') + ':';
		obj['7'] = usr_info.last_login_timestamp ? Wzb.displayTime(
				usr_info.last_login_timestamp, Wzb.time_format_ymdhm) : '--';
		obj['8'] = (usr_info.last_login_status == true) ? Wzb
				.l('43') : Wzb.l('44');
	}
	obj['5'] = Wzb.l('287');
	obj['4'] = 'my_profile.htm';
	obj['3'] = Wzb.getRelativeImagePath(usr_info.usr_avatar);
	obj['2'] = usr_info.usr_display_bil;
	obj['9'] = Wzb.l('842');
	obj['10'] = 'my_credit.htm';
	obj['11'] = Math.round(usr_info.my_credit*100)/100; //usr_info.my_credit;
	var welcomuser_str = WzbHtm.welcomuser(obj);

	var welcomePan = new Wzb.Panel({
		type : 2,
		title : Wzb.l('349'),
		id : 'welcomuser',
		width : total_width,
		hideBorders : true,
		border : true,
		bodyStyle : Wzb.style_inner_space,
		collapsible : true,
		iconCls : 'welcomuser',
		tools : Wzb.getPanelTools(),
		html : welcomuser_str
	});

	return welcomePan;
}

function rank_credit_gadget(total_width, mainStore) {
	var panel = new Wzb.Panel({
		id : 'credit_rank',
		title : Wzb.l('851'),
		collapsible : true,
		iconCls : 'credit_rank',
		hideBorders : true,
		border : true,
		tools : Wzb.getPanelTools(),
		type : 2
	});
	var credit = credit_rank_gadget(total_width, mainStore);
	panel.add(credit);
	panel.add(Wzb.getMoreInfoPanel('javascript:getCreditRankMoreLink()'));

	return panel;
}

function getCreditRankMoreLink() {
	window.location.href = 'credit_rank_lst.htm';
}

function credit_rank_gadget(total_width, mainStore) {
	function renderTitle(value, p, record, rowIndex, colIndex, store) {
		var str = '';
		var obj = {
			1 : Wzb.getNumberImg(Number(record.data.sort_id)),
			2 : record.data.usr_display_bil,
			3 : record.data.uct_total
		};
		str = WzbHtm.credit_rank(obj);
		return str;
	}
	var itm_reader = mainStore.reader.jsonData;
	var store = new Ext.data.Store({
		proxy : new Ext.data.MemoryProxy(itm_reader),
		autoLoad : true,
		reader : new Ext.data.JsonReader({
			root : 'credit_rank'
		}, ['sort_id', 'usr_display_bil', 'uct_total'])
	});

	var colModel = new Ext.grid.ColumnModel([{
		menuDisabled : true,
		resizable : false,
		dataIndex : 'usr_display_bil',
		renderer : renderTitle,
		width : '100%'
	}]);

	var grid = new Wzb.GridPanel({
		border : false,
		store : store,
		autoHeight : true,
		hideHeaders : true,
		colModel : colModel,
		viewConfig : {
			emptyText : Wzb.getEmptyText()
		},
		bodyStyle : 'background:transparent'
	});
	store.load();

	return grid;
}

// ??????а?
function rank_item_gadget(total_width, mainStore) {
	var panel = new Wzb.Panel({
		id : 'rank',
		title : Wzb.l('13'),
		collapsible : true,
		iconCls : 'rank',
		hideBorders : true,
		tools : Wzb.getPanelTools(),
		type : 2
	});
	var obj = {
		7 : Wzb.link_sep,
		1 : Wzb.l('87'),
		6 : 'javascript:getRankItem(\'pop\')',
		3 : 'javascript:getRankItem(\'estimated\')',
		2 : Wzb.l('84'),
		4 : 'javascript:getRankItem(\'newest\')',
		5 : Wzb.l('89')
	}
	var rankHtml = WzbHtm.rank(obj)
	var otherItem = new Wzb.Panel({
		html : rankHtml
	});
	panel.add(otherItem)

	var item = item_rank_gadget(total_width, mainStore);
	panel.add(item);
	panel.add(Wzb.getMoreInfoPanel('javascript:getItmRankMoreLink()'));

	return panel;
}

function getItmRankMoreLink() {
	var itmType = Ext.getCmp('itm_rank').store.itmType;
	var linkStr = 'pop_estimated_course.htm';
	if (itmType == 'pop') {
		linkStr += '?type=hot_course';
	} else if (itmType == 'estimated') {
		linkStr += '?type=comments_course';
	} else if (itmType == 'newest') {
		linkStr += '?type=new_course';
	}
	window.location.href = linkStr;
}

function getRankItem(itmType) {
	var grid = Ext.getCmp('itm_rank');
	var store = grid.getStore();
	store.itmType = itmType;
	if (itmType == 'pop') {
		store.proxy = new Ext.data.HttpProxy({
			url : Wzb.getDisUrl('module', 'JsonMod.user.UserModule', 'cmd',
					'home_gadget', 'type', 'pop_item')
		})
	} else if (itmType == 'estimated') {
		store.proxy = new Ext.data.HttpProxy({
			url : Wzb.getDisUrl('module', 'JsonMod.user.UserModule', 'cmd',
					'home_gadget', 'type', 'estimated_item')
		})
	} else if (itmType == 'newest') {
		store.proxy = new Ext.data.HttpProxy({
			url : Wzb.getDisUrl('module', 'JsonMod.user.UserModule', 'cmd',
					'home_gadget', 'type', 'newest_item')
		})
	}

	store.reader = getItmRankReader(itmType);
	store.load();
}

function getItmRankReader(itmType) {
	var root;
	if (itmType == 'pop') {
		root = 'pop_item["itm_lst"]';
		Ext.getDom('pop').className = 'wzb-common-link-selected';
		Ext.getDom('estimated').className = 'wzb-common-link';
		Ext.getDom('newest').className = 'wzb-common-link';

	} else if (itmType == 'estimated') {
		root = 'estimated_item["itm_lst"]';
		Ext.getDom('pop').className = 'wzb-common-link';
		Ext.getDom('estimated').className = 'wzb-common-link-selected';
		Ext.getDom('newest').className = 'wzb-common-link';
	} else if (itmType == 'newest') {
		root = 'newest_item["itm_lst"]';
		Ext.getDom('pop').className = 'wzb-common-link';
		Ext.getDom('estimated').className = 'wzb-common-link';
		Ext.getDom('newest').className = 'wzb-common-link-selected';
	}
	var reader = new Ext.data.JsonReader({
		root : root
	}, ['itm_id', 'itm_title', 'total_score', 'avg_score', 'itm_type',
			'total_count', 'order', 'itm_comment_avg_score', 'start_timestamp',
			'app_count']);
	return reader;
}

function item_rank_gadget(total_width, mainStore) {
	function renderTitle(value, p, record, rowIndex, colIndex, store) {
		var str = '';
		if (store.itmType == 'pop') {
			var obj = {
				5 : Wzb.getNumberImg(Number(record.data.order + 1)),
				1 : record.data.itm_title,
				2 : 'course_detail.htm?itm_id=' + record.data.itm_id
			};
			str = WzbHtm.pop_itm_div(obj);
		} else if (store.itmType == 'estimated') {
			var starImg = '';
			for (var i = 0; i < record.data.itm_comment_avg_score; i++) {
				starImg += WzbHtm.img({
					1 : Wzb.getCommonImagePath('star_dim.gif')
				})
			}
			for (var i = 0; i < 5 - record.data.itm_comment_avg_score; i++) {
				starImg += WzbHtm.img({
					1 : Wzb.getCommonImagePath('star.gif')
				})
			}
			var obj = {
				1 : Wzb.getNumberImg(Number(record.data.order + 1)),
				2 : record.data.itm_title,
				3 : starImg,
				4 : record.data.total_count,
				5 : 'course_detail.htm?itm_id=' + record.data.itm_id
			};
			str = WzbHtm.rank_div(obj);
		} else if (store.itmType == 'newest') {
			var obj = {
				2 : record.data.itm_title,
				1 : 'course_detail.htm?itm_id=' + record.data.itm_id
			};
			str = WzbHtm.new_itm_div(obj);
		}

		return str;
	}
	var itm_reader = mainStore.reader.jsonData['estimated_item'];
	var store = new Ext.data.Store({
		itmType : 'estimated',// ?????????
		proxy : new Ext.data.MemoryProxy(itm_reader),
		autoLoad : true,
		reader : new Ext.data.JsonReader({
			root : 'itm_lst'
		}, ['itm_id', 'itm_title', 'total_score', 'avg_score', 'itm_type',
				'total_count', 'order', 'itm_comment_avg_score',
				'start_timestamp'])
	});

	var colModel = new Ext.grid.ColumnModel([{
		menuDisabled : true,
		resizable : false,
		dataIndex : 'itm_title',
		renderer : renderTitle,
		width : '100%'
	}]);

	var grid = new Wzb.GridPanel({
		id : 'itm_rank',
		border : false,
		store : store,
		autoHeight : true,
		hideHeaders : true,
		colModel : colModel,
		viewConfig : {
			emptyText : Wzb.getEmptyText()
		},
		bodyStyle : 'background:transparent'
	});
	store.load();

	return grid;
}
// ??????(???????)
function pending_waiting_item_gadget(total_width, mainStore) {
	function renderItem(value, p, record) {
		var app_status = '';
		var app_status_img = '';
		if (record.data.app_status == 'Pending') {
			app_status = Wzb.l('392');
			app_status_img = 'wzb-wait-prove-detail-icon-2';
		} else {
			app_status = Wzb.l('45');
			app_status_img = 'wzb-wait-prove-detail-icon-1';
		}

		var obj = {
			2 : record.data.itm_title,
			3 : Wzb.displayTime(record.data.aal_action_timestamp),
			5 : app_status,
			4 : app_status_img,
			1 : 'course_detail.htm?itm_id=' + record.data.itm_id
		};
		var str = WzbHtm.pending_itm_div(obj);
		return str;
	}
	var pendingReader = mainStore.reader.jsonData['pending_waiting_item'];
	var store = new Ext.data.Store({
		proxy : new Ext.data.MemoryProxy(pendingReader),
		autoLoad : true,
		reader : new Ext.data.JsonReader({
			root : 'itm_lst'
		}, ['lab_itm_type', 'app_create_timestamp', 'itm_id', 'itm_title',
				'itm_icon', 'usr_display_bil', 'itm_dummy_type', 'itm_type',
				'aal_action_timestamp', 'app_status', 'app_id', 'app_count'])

	});
	var panel = new Wzb.Panel({
		type : 2,
		id : 'pending_waiting_item_panel',
		title : Wzb.l('350'),
		collapsible : true,
		iconCls : 'pending_waiting_item_panel',
		hideHeaders : true
	});

	var PWItem_grid = new Wzb.GridPanel({
		id : 'pending_waiting_item',
		collapsible : false,
		bodyStyle : 'background:transparent',
		autoHeight : true,
		store : store,
		hideHeaders : true,
		border : false,
		columns : [{
			width : '100%',
			dataIndex : 'title',
			renderer : renderItem
		}],
		viewConfig : {
			emptyText : Wzb.getEmptyText()
		}
	});
	panel.add(PWItem_grid);
	panel.add(Wzb.getMoreInfoPanel('learning_center.htm?type=COS&activetab=1'));
	return panel;
}

// ??????
function diagnoses_gadget(total_width, mainStore) {

	function renderDiagnoses(value, metadata, record, rowIndex, colIndex, store) {
		var diaUrl = Wzb.getQdbUrl('cmd', 'get_mod_status', 'mod_id',
				record.data.id, 'mod_type', record.data.type, 'tpl_use',
				'svy_player.xsl', 'url_failure', '../htm/close_window.htm',
				'stylesheet', 'start_module.xsl');

		var obj = {
			1 : diaUrl,
			2 : record.data.title,
			3 : Wzb.l('103'),
			4 : Wzb.displayTime(record.data.eff_end_datetime)
		};
		var str = WzbHtm.diagnoses_div(obj);
		return str;
	}
	var diagnoses_reader = mainStore.reader.jsonData['public_eval'];
	var store = new Ext.data.Store({
		proxy : new Ext.data.MemoryProxy(diagnoses_reader),
		autoLoad : true,
		reader : new Ext.data.JsonReader({
			root : 'eval_lst'
		}, ['title', 'type', 'eff_end_datetime', 'id'])

	});

	var colModel = new Ext.grid.ColumnModel([{
		id : 'type',
		menuDisabled : true,
		resizable : false,
		dataIndex : 'type',
		renderer : renderDiagnoses,
		width : '100%',
		align : 'left'
	}]);

	var desc_panel = Wzb.getSubTitlePanel(Wzb.l('351'));
	var diagnoses_panel = new Wzb.GridPanel({
		type : 2,
		id : 'diagnoses',
		title : Wzb.l('352'),
		collapsible : true,
		iconCls : 'diagnoses',
		store : store,
		colModel : colModel,
		autoHeight : true,
		hideHeaders : true,
		enableColumnMove : false,
		items : [desc_panel],
		tools : Wzb.getPanelTools(),
		viewConfig : {
			emptyText : Wzb.getEmptyText()
		}
	});
	return diagnoses_panel;
}

// ??f?6????
function ability_que_gadget(total_width, mainStore) {

	function renderAbility_que(value, metadata, record, rowIndex, colIndex,
			store) {
		var asmurl = Wzb.getDisUrl('module', 'competency.CompetencyModule',
				'cmd', 'get_assessor_result', 'asm_id', record.data.asm_id,
				'usr_ent_id', cur_usr_ent_id, 'stylesheet',
				'comp_ass_submit_ass.xsl', 'from', 'homepage');
		var obj = {
			1 : asmurl,
			2 : record.data.asm_title,
			3 : '',
			4 : '',
			5 : '',
			6 : Wzb.l('103'),
			7 : Wzb.displayTime(record.data.asm_eff_end_datetime)
		};
		if(record.data.assessee !== mainStore.reader.jsonData['meta']['display_bil']){
			obj['lab_assessee'] = Wzb.l('354');
			obj['assessee'] = record.data.assessee;
			obj['lab_colon'] = ': ';
		}
		var str = WzbHtm.ability_que_div(obj);
		return str;
	}
	var ability_que_reader = mainStore.reader.jsonData['skill_eval'];
	var store = new Ext.data.Store({
		proxy : new Ext.data.MemoryProxy(ability_que_reader),
		autoLoad : true,
		reader : new Ext.data.JsonReader({
			root : 'assessment_lst'
		}, ['asm_type', 'asm_id', 'asm_eff_end_datetime', 'assessee',
				'asm_title', 'asu_type', 'asu_sks_skb_id'])

	});

	var desc_panel = Wzb.getSubTitlePanel(Wzb.l('351'));
	var colModel = new Ext.grid.ColumnModel([{
		id : 'type',
		menuDisabled : true,
		resizable : false,
		dataIndex : 'type',
		renderer : renderAbility_que,
		width : '100%',
		align : 'left'
	}]);
	var ability_quePan = new Wzb.GridPanel({
		type : 2,
		id : 'ability_que',
		title : Wzb.l('353'),
		collapsible : true,
		iconCls : 'ability_que',
		store : store,
		colModel : colModel,
		autoHeight : true,
		width : '100%',
		hideHeaders : true,
		items : [desc_panel],
		tools : Wzb.getPanelTools(),
		viewConfig : {
			emptyText : Wzb.getEmptyText()
		}
	});
	return ability_quePan;
}

// ////////////////////////////////

function showProgress(value, metadata, record, rowIndex, colIndex, store) {
	var str = '';
	var imgName = '';
	switch (value) {
		case '0' :
			imgName = '';
			break;
		case '1' :
			imgName = 'image/progress1.gif';
			break;
		case '2' :
			imgName = 'image/progress2.gif';
			break;
		case '3' :
			imgName = 'image/progress3.gif';
			break;
		case '4' :
			imgName = 'image/progress4.gif';
			break;
	}
	if (imgName != '') {
		str = '<img src=' + imgName
				+ ' height="10" style="vertical-align:bottom">';
	}
	return str;
}

function showLastUpd(value, metadata, record, rowIndex, colIndex, store) {
	var str = '<p style="vertical-align:bottom;text-align:left">' + value
			+ '</p>';
	return str;
}

function showBtn(value, metadata, record, rowIndex, colIndex, store) {
	var btnName = Wzb.l('41');
	var btnUrl;
	if (record.data.type == 'EXAM') {
		btnName = Wzb.l('102');
		btnUrl = 'javascript:window.location.href = \'course_home.htm?title='
				+ record.data.title + '&cos_type=' + record.data.type
				+ '&exam_line=' + record.data.exam_type + '\'';
	} else {
		btnUrl = 'javascript:window.location.href = \'course_home.htm?title='
				+ record.data.title + '&cos_type=' + record.data.type + '\'';
	}
	var str = '';
	if (record.data.type != 'EXAM' || record.data.title.length > 0) {
		str = genBtn(btnName, btnUrl);
	}
	return str;
}

function showExamName(value, metadata, record, rowIndex, colIndex, store) {
	var line = Wzb.l('356');
	if (record.data.exam_line == 'EXAM_OFFLINE') {
		line = Wzb.l('357');
	}
	var str = '<p style="vertical-align:bottom">' + value + '(' + line + ')'
			+ '</p>';
	return str;
}

function renderGrade(value, p, record) {
	var s = '';
	for (var i = 1; i <= value; i++) {
		s += '<img src=\'image/star_dim.gif\'/>'
	}
	for (var i = 1; i <= 5 - value; i++) {
		s += '<img src=\'image/star.gif\'/>'
	}
	return s;
}

function resizeImg(img_obj, gadget_width, gadget_height){
	if(img_obj.width > gadget_width) {
		img_obj.width = gadget_width;
	}
	if(img_obj.height > gadget_height) {
		img_obj.height = gadget_height;
	}
}

function site_poster_gadget(total_width, mainData) {
	var gadget_height = 200;
	var site_poster = mainData.reader.jsonData['ste_poster'];
	var obj_html = '';
	var objUrl = site_poster.sp_media_file;
	if(objUrl !== null && objUrl !== undefined && objUrl !== '') {
		var objType = objUrl.substring(objUrl.lastIndexOf('.') + 1).toLowerCase();
		if(objType === 'swf') {
			obj_html = '<OBJECT name=\'{$site_name}_movie\' classid=\'clsid:D27CDB6E-AE6D-11cf-96B8-444553540000\' codebase=\'http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=5,0,0,0\' width=' + total_width + ' height=' + gadget_height + '>'
			+'	<PARAM NAME=\'movie\' VALUE=\'' + objUrl + '\'/>'
			+'	<PARAM NAME=\'wmode\' VALUE=\'transparent\'/>'
			+'	<EMBED src=\'' + objUrl +'\' TYPE=\'application/x-shockwave-flash\' PLUGINSPAGE=\'http://www.macromedia.com/shockwave/download/index.cgi?P1_Prod_Version=ShockwaveFlash\' width=' + total_width + ' height=' + gadget_height + '/>'
			+'</OBJECT>'
		} else {
			obj_html = '<img '+ Wzb.getHtmImgSrc(objUrl) + ' onload=\'resizeImg(this, ' + total_width + ', ' + gadget_height + ')\'/>';
			if(site_poster.sp_url !== undefined && site_poster.sp_url !== '' && site_poster.sp_url !== 'http://') {
				obj_html = '<a target=\'_blank\' href=\'' + site_poster.sp_url + '\'>' + obj_html + '</a>';
			}
		}
		obj_html = '<table width="100%" border="0" cellspacing="0" cellpadding="0">' +
				'<tr><td align="center">' + obj_html + '</td></tr></table>';
	}

	var sitePosterPan = new Wzb.Panel({
		type : 2,
		id : 'siteposter',
		width : total_width,
		height : gadget_height,
		hideBorders : true,
		border : true,
		collapsible : false,
		align : 'center',
		html : obj_html
	});

	return sitePosterPan;
}

function fs_link_gadget(total_width, mainData) {
	function renderFsLinks(value, metadata, record, rowIndex, colIndex, store) {
		var str = '<a class=\'wzb-common-link\' target=\'_blank\' href=\'' + record.data.fsl_url +'\'>'
			+ record.data.fsl_title + '</a><br/>'
		return str;
	}
	var fs_link_reader = mainData.reader.jsonData;
	var store = new Ext.data.Store({
		proxy : new Ext.data.MemoryProxy(fs_link_reader),
		autoLoad : true,
		reader : new Ext.data.JsonReader({
			root : 'fs_link'
		}, ['fsl_title', 'fsl_url'])

	});

	var colModel = new Ext.grid.ColumnModel([{
		id : 'type',
		menuDisabled : true,
		resizable : false,
		dataIndex : 'type',
		renderer : renderFsLinks,
		width : '100%',
		align : 'left'
	}]);

	var fsLinkPan = new Wzb.GridPanel({
		type : 2,
		id : 'fslink',
		title : Wzb.l('700'),
		collapsible : true,
		iconCls : 'fslink',
		store : store,
		colModel : colModel,
		autoHeight : true,
		hideHeaders : true,
		enableColumnMove : false,
		tools : Wzb.getPanelTools(),
		viewConfig : {
			emptyText : Wzb.getEmptyText()
		}
	});
	return fsLinkPan;
}

/*
 * display company QQ Talk
 */
function companyQQTalk(total_width, companyQQData) {
	function companyQQNumbers(value, metadata, record, rowIndex, colIndex, store) {
		var qqObject = {
			qqNumber : record.data.cpq_number,
			qqTitle : record.data.cpq_title
		};
		return WzbHtm.qq_consultation(qqObject);
	}
	
	var store = new Ext.data.Store({
		proxy : new Ext.data.MemoryProxy(companyQQData),
		autoLoad : true,
		reader : new Ext.data.JsonReader({
			root : 'company_qq_list'
		}, ['cpq_title', 'cpq_number', 'cpq_desc'])

	});

	var colModel = new Ext.grid.ColumnModel([{
		id : 'type',
		menuDisabled : true,
		resizable : false,
		dataIndex : 'type',
		renderer : companyQQNumbers,
		width : '100%',
		align : 'left'
	}]);

	var company_qq_panel = new Wzb.GridPanel({
		type : 2,
		id : 'QQ_consultation',
		title : Wzb.l('lab_ftn_QQ_CONSULTATION'),
		collapsible : true,
		iconCls : 'QQ_consultation',
		store : store,
		colModel : colModel,
		autoHeight : true,
		hideHeaders : true,
		enableColumnMove : false,
		tools : Wzb.getPanelTools(),
		viewConfig : {
			emptyText : Wzb.getEmptyText()
		}
	});
	return company_qq_panel;
}

/*
 * display defined project
 */
function defiend_project_gadget(total_width, defined_project) {
	function definedProjectLinks(value, metadata, record, rowIndex, colIndex, store) {
		var str = '<a class=\'wzb-common-link\' target=\'_blank\' href=\'' + record.data.pjl_url +'\'>'
			+ record.data.pjl_title + '</a><br/>'
		return str;
	}
	
	var store = new Ext.data.Store({
		proxy : new Ext.data.MemoryProxy(defined_project),
		autoLoad : true,
		reader : new Ext.data.JsonReader({
			root : 'defi_proj_link'
		}, ['pjl_title', 'pjl_url'])

	});

	var colModel = new Ext.grid.ColumnModel([{
		id : 'type' + defined_project.dpt_id,
		menuDisabled : true,
		resizable : false,
		dataIndex : 'type',
		renderer : definedProjectLinks,
		width : '100%',
		align : 'left'
	}]);

	var defined_project_panel = new Wzb.GridPanel({
		type : 2,
		id : 'defined_project_' + defined_project.dpt_id,
		title : defined_project.dpt_title,
		collapsible : true,
		iconCls : 'fslink',
		store : store,
		colModel : colModel,
		autoHeight : true,
		hideHeaders : true,
		enableColumnMove : false,
		tools : Wzb.getPanelTools(),
		viewConfig : {
			emptyText : Wzb.getEmptyText()
		}
	});
	return defined_project_panel;
}
