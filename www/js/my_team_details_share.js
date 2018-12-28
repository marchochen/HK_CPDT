var sgpId;// 小组Id
var sgpTcrId;// 小组所在培训中心Id
var sgpTitle;// 小组标题
var sgpTitle_noescape;
var sgpDesc;// 小组描述
var sgpDesc_noescape;
var sgpUpdTimestamp;// 小组更新时间
var sgpPublicType;// 内容公开设定
var isManager;// 是否小组组长管理员
var isTa;// 是否培训管理员
var mainData;
var isFromCore47 = false;
var goldenman = new wbGoldenMan;

function paintTeamDetails(mainStore, isRender, isSetTitle) {

	isFromCore47 = mainStore.isFromCore47;
	mainData = mainStore.reader.jsonData;
	isManager = mainData['cur_usr']['is_manager'];
	isTa = mainData['cur_usr']['is_Ta'];
	sgpId = mainData['sgp_info']['sgp_id'];
	sgpTcrId = mainData['sgp_info']['tcr_id'];
	sgpTitle = mainData['sgp_info']['sgp_title'];
	sgpTitle_noescape = mainData['sgp_info']['sgp_title_noescape'];
	sgpDesc = mainData['sgp_info']['sgp_desc'];
	sgpDesc_noescape = mainData['sgp_info']['sgp_desc_noescape'];
	sgpUpdTimestamp = mainData['sgp_info']['sgp_upd_timestamp'];
	sgpPublicType = mainData['sgp_info']['sgp_public_type'];
	if (isFromCore47) {
		Wzb.util_col_width2 = 660;
		Wzb.util_col_width1 = 290;
		Wzb.util_total_width = 960;
	}
	if (isSetTitle === undefined || isSetTitle) {
		Wzb.renderPageTitle(sgpTitle);
	}
	var main_container = new Wzb.Panel({
		layout : 'column',
		hideBorders : true,
		border : false,
		width : Wzb.util_total_width,
		defaults : {
			cellCls : 'valign-top'
		},
		items : [{
			id : 'leftPart',
			width : Wzb.util_col_width2,
			cls : 'right',
			border : false,
			autoHeight : true,
			defaults : {
				width : Wzb.util_col_width2,
				autoHeight : true
			},
			items : [getTeamSummaryPn(), getResourceGrid()]
		}, {
			id : 'rightPart',
			width : Wzb.util_col_width1,
			border : false,
			autoHeight : true,
			defaults : {
				width : Wzb.util_col_width1,
				autoHeight : true
			},
			items : [getMemberGrid(), getDiscussGrid()]
		}]
	});

	if (isRender != undefined && isRender) {
		main_container.render('container');
	}
	main_container.addClass('bottom');
	main_container.addClass('panel-maincontainer');
	return main_container;
}
// 小组资料panel
function getTeamSummaryPn() {
	var teamSumObj = {
		3 : '',
		1 : '',
		2 : ''
	};
	if (sgpDesc !== undefined && sgpDesc !== '') {
		teamSumObj['2'] = Wzb.unescapeHtmlLineFeed(sgpDesc);
		teamSumObj['1'] = 'wzb-common-text';
	} else {
		teamSumObj['2'] = Wzb.l('476');
		teamSumObj['1'] = 'wzb-minor-text';
	}
	if (isManager || isTa) {
		teamSumObj['3'] = Wzb.getButton(
				'javascript:editTeamSumPage()', Wzb.l('175'));
	}
	var teamSummaryPn = new Wzb.Panel({
		type : 5,
		header : false,
		bodyStyle : Wzb.style_inner_space,
		html : WzbHtm.teamSynopsisPn(teamSumObj)
	});
	teamSummaryPn.addClass('bottom');
	return teamSummaryPn;
}

// 资源分享Grid
function getResourceGrid() {
	var colModel = new Ext.grid.ColumnModel([{
		menuDisabled : true,
		resizable : false,
		dataIndex : 'sgs_title',
		renderer : renderSgsTitle,
		width : Wzb.util_col_width2
	}]);
	var reader = new Ext.data.JsonReader({
		totalProperty : 'sgp_res["total"]',
		root : 'sgp_res["res_lst"]'
	}, ['sgs_id', 'sgs_title', 'sgs_type', 'sgs_content', 'sgs_desc',
			'sgs_upd_timestamp', 'is_creator','sgs_title_noescape','sgs_content_noescape','sgs_desc_noescape']);

	var resourceStore = new Ext.data.Store({
		// remoteSort : true,
		proxy : new Ext.data.MemoryProxy(mainData),
		reader : reader
	});

	var bbar = Wzb.getPagingToolbar(resourceStore);
	var resourceGrid = new Wzb.GridPanel({
		id : 'resourceGrid',
		title : Wzb.l('181'),
		store : resourceStore,
		colModel : colModel,
		hideHeaders : true,
		isChangeProxy : true,
		url : Wzb.getDisUrl('module', 'JsonMod.studyGroup.StudyGroupModule',
				'cmd', 'get_res_lst', 'sgp_id', sgpId),
		tbar : [
				new Ext.Toolbar.TextItem('<div class="wzb-study-group-add-link"></div><a class="wzb-common-link" href="javascript:insUpdLinkWin()">'
						+ Wzb.l('182') + '</a>' + Wzb.link_sep),
				new Ext.Toolbar.TextItem('<div class="wzb-study-group-add-file"></div><a class="wzb-common-link" href="javascript:insUpdDocWin()">'
						+ Wzb.l('186') + '</a>' + Wzb.link_sep),
				new Ext.Toolbar.TextItem('<div class="wzb-study-group-add-subject"></div><a class="wzb-common-link" href="javascript:insUpdCosWin()">'
						+ Wzb.l('188') + '</a>')],
		bbar : bbar,
		viewConfig : {
			emptyText : Wzb.getEmptyText()
		}
	});
	resourceGrid.addListener({
		'render' : Wzb.renderEventFn
	})
	return resourceGrid;
}
// 成员Grid
function getMemberGrid() {
	var colModel = new Ext.grid.ColumnModel([{
		menuDisabled : true,
		resizable : false,
		dataIndex : 'usr_nickname',
		renderer : renderSgmName,
		width : Wzb.util_col_width1
	}]);

	var reader = new Ext.data.JsonReader({
		totalProperty : 'sgp_usr["total"]',
		root : 'sgp_usr["usr_lst"]'
	}, ['usr_ent_id', 'usr_nickname', 'urx_extra_43', 'is_group_leader']);

	var memberStore = new Ext.data.Store({
		// remoteSort : true,
		proxy : new Ext.data.MemoryProxy(mainData),
		reader : reader
	});

	var simp_page = Wzb.getPagingToolbar(memberStore, 2, 5);

	var memberGridPn = new Wzb.GridPanel({
		type : 3,
		id : 'memberGridPn',
		store : memberStore,
		colModel : colModel,
		border : false,
		hideHeaders : true,
		autoHeight : true,
		isChangeProxy : true,
		url : Wzb.getDisUrl('module', 'JsonMod.studyGroup.StudyGroupModule',
				'cmd', 'get_mem_lst', 'sgp_id', sgpId),
		bbar : simp_page,
		bodyStyle : 'background:transparent;',
		viewConfig : {
			emptyText : Wzb.getEmptyText()
		}
	});
	memberGridPn.addListener({
		'render' : Wzb.renderEventFn
	});

	var memberPn = new Wzb.Panel({
		type : 4,
		id : 'memberPn',
		title : Wzb.l('193'),
		items : [memberGridPn]
	});
	memberPn.addClass('bottom');
	// 如果是管理员则可自动/手动添加成员
	if (isTa) {
		var addMemObj = {
			1 : 'javascript:setMemberAutoWin()',
			2 : Wzb.l('195'),
			3 : Wzb.link_sep,
			4 : 'javascript:setMemberCustWin()',
			5 : Wzb.l('198')
		};
		var addMemberPn = new Wzb.Panel({
			border : false,
			bodyStyle : Wzb.style_inner_space + 'text-align:right;',
			html : WzbHtm.addMemberPn(addMemObj)
		});
		memberPn.add(addMemberPn);
	}
	return memberPn;
}
// 讨论区grid
function getDiscussGrid() {
	var discussPn = new Wzb.Panel({
		type : 5,
		title : Wzb.l('200'),
		width : Wzb.util_col_width1
	});

	var colModel = new Ext.grid.ColumnModel([{
		menuDisabled : true,
		resizable : false,
		dataIndex : 'fto_title',
		width : Wzb.util_col_width1,
		renderer : renderForumTitle
	}]);
	var reader = new Ext.data.JsonReader({
		totalProperty : 'discuss["total"]',
		root : 'discuss["dis_lst"]'
	}, ['fto_id', 'fto_title', 'fto_res_id', 'mod_id']);

	var forumStore = new Ext.data.Store({
		// remoteSort : true,
		proxy : new Ext.data.MemoryProxy(mainData),
		reader : reader
	});
	var forumGrid = new Wzb.GridPanel({
		store : forumStore,
		hideHeaders : true,
		autoHeight : true,
		border : false,
		cm : colModel,
		viewConfig : {
			emptyText : Wzb.getEmptyText()
		}
	});
	forumGrid.addListener({
		'render' : Wzb.renderEventFn
	});
	var enterForumHref = 'javascript:Wzb.openWin(\''
			+ Wzb.getQdbUrl('cmd', 'get_mod', 'stylesheet', 'forum.xsl',
					'mod_id', mainData['discuss']['mod_id'])
			+ '\',\'forum_player\',true)'
	var forumGridLink = Wzb.getMoreInfoPanel(enterForumHref, Wzb.l('201'));
	discussPn.add(forumGrid);
	discussPn.add(forumGridLink);
	return discussPn;
}
// 论坛列
function renderForumTitle(value, metadata, record, rowIndex, colIndex, store) {
	var ftoCol = {
		1 : Wzb.getQdbUrl('cmd', 'get_mod', 'stylesheet',
				'forum2.xsl', 'extend', '1', 'mod_id', record.data.mod_id,
				'topic_id', record.data.fto_id),
		2 : record.data.fto_title,
		label_fto_title : record.data.fto_title,
		str_feature:str_feature
	};

	var str = WzbHtm.renderForumTitle(ftoCol);
	return str;
}
// 成员列
function renderSgmName(value, metadata, record, rowIndex, colIndex, store) {
	var sgmCol = {
		1 : Wzb.getRelativeImagePath(record.data.urx_extra_43),
		2 : 'javascript:showUserInfoWin('
				+ record.data.usr_ent_id + ')',
		3 : record.data.usr_nickname,
		4 : ''
//		del_mem_href : '',
//		del_mem : ''
	};
	if (record.data.is_group_leader) {
		sgmCol['4'] = ' <span class="wzb-sub-title-text">('
				+ Wzb.l('194') + ')</span>';
	}
	var str = WzbHtm.renderSgmName(sgmCol);
	return str;
}
// 资源列
var resObj = {};
function renderSgsTitle(value, metadata, record, rowIndex, colIndex, store) {
	var sgs_title_href = record.data.sgs_content;
	if(record.data.sgs_type === 'COURSE' && isTa) {
		sgs_title_href += '&page_readonly=true';
	}
	var sgsCol = {
		study_group_resourse_icon : '',
		2 : sgs_title_href,
		3 : record.data.sgs_title,
		6 : Wzb.unescapeHtmlLineFeed(record.data.sgs_desc),
		4 : '',
		5 : ''
	};
	if (record.data.sgs_type === 'LINK') {
		sgsCol['1'] = 'wzb-study-group-resource_link';
	} else if (record.data.sgs_type === 'DOC') {
		sgsCol['1'] = 'wzb-study-group-resource_doc';
	} else if (record.data.sgs_type === 'COURSE') {
		sgsCol['1'] = 'wzb-study-group-resource_course';
	}
	if (isManager || record.data.is_creator || isTa) {
		resObj[record.data.sgs_id] = record.data;
		var editHref = '#';
		if (record.data.sgs_type === 'LINK') {
			editHref = 'javascript:insUpdLinkWin(' + record.data.sgs_id + ')';
		} else if (record.data.sgs_type === 'DOC') {
			editHref = 'javascript:insUpdDocWin(' + record.data.sgs_id + ')';
		} else if (record.data.sgs_type === 'COURSE') {
			editHref = 'javascript:insUpdCosWin(' + record.data.sgs_id + ')';
		}
		sgsCol['4'] = Wzb.getButton('javascript:delResource('
				+ record.data.sgs_id + ',\'' + record.data.sgs_upd_timestamp
				+ '\')', Wzb.l('257'));
		sgsCol['5'] = Wzb.getButton(editHref, Wzb.l('192'));
	}
	var str = WzbHtm.renderSgsTitle(sgsCol);
	return str;
}

// 删除资源
function delResource(sgs_id, sgs_upd_timestamp) {
	Ext.MessageBox.buttonText.yes = Wzb.l('329');
	Ext.MessageBox.buttonText.no = Wzb.l('330');
	if(confirm(Wzb.l('481'))) {
		var store = new Ext.data.JsonStore({
			url : Wzb.getDisUrl('module',
					'JsonMod.studyGroup.StudyGroupModule', 'cmd',
					'del_sgp_res', 'sgs_id', sgs_id, 'sgp_id',
					sgpId, 'sgs_upd_timestamp', sgs_upd_timestamp)
		})
		store.load({
			callback : function() {
				Ext.getCmp('resourceGrid').getStore().load();
			}
		});
	}

}
// 编辑小组资料
function editTeamSumPage() {
	var fpn = new Wzb.FormPanel({
		id : 'editTeamSumForm',
		labelAlign : 'top',
		autoHeight : true,
		frame : false,
		labelWidth : 80,
		bodyStyle : Wzb.style_inner_space,
		items : [{
			xtype : 'textfield',
			name : 'sgp_title',
			id : 'sgp_title_id',
			allowBlank : false,
			blankText : Wzb.l('482'),
			maxLength : 50,
			maxLengthText : Wzb.l('483'),
			width : 300,
			fieldLabel : '*' + Wzb.l('176'),
			value : sgpTitle_noescape
		}, {
			xtype : 'textarea',
			name : 'sgp_desc',
			maxLength : 255,
			maxLengthText : Wzb.l('484'),
			width : 300,
			height : 90,
			fieldLabel : Wzb.l('177'),
			value : sgpDesc_noescape
		}, {
			xtype : 'radio',
			name : 'sgp_public_type',
			fieldLabel : Wzb.l('178'),
			boxLabel : Wzb.l('179'),
			checked : sgpPublicType === 0,
			inputValue : 0
		}, {
			xtype : 'radio',
			name : 'sgp_public_type',
			hideLabel : true,
			boxLabel : Wzb.l('180'),
			checked : sgpPublicType === 1,
			inputValue : 1
		}, {
			xtype : 'hidden',
			name : 'sgp_upd_timestamp',
			value : sgpUpdTimestamp
		}],
		buttons : [new Ext.Button({
			text : Wzb.l('329'),
			handler : function() {
				var form = Ext.getCmp('editTeamSumForm').getForm();
				var values = form.getValues();
				form.setValues({
					sgp_title : (values.sgp_title).trim(),
					sgp_desc : (values.sgp_desc).trim()
				});
				if (form.isValid()) {
					form.submit({
						url : Wzb.getDisUrl('module',
								'JsonMod.studyGroup.StudyGroupModule', 'cmd',
								'upd_sgp_info', 'sgp_id', sgpId, 'tcr_id',
								sgpTcrId),
						waitMsg : Wzb.l('485') + '...',
						success : success,
						failure : failure
					});
				}
			}
		}), new Ext.Button({
			text : Wzb.l('330'),
			handler : winClose
		})]
	});

	popWin(Wzb.l('175'), fpn);
}

function success(form, action) {
	// alert(action.failureType + "=s=" + action.result.errors.clientCode)
	winClose();
	if (isFromCore47 !== undefined && isFromCore47) {
		show_sgp(sgpId);
	} else {
		window.location.href = window.location.href;
	}
}
function failure(form, action) {
	// alert(action.result.msg.codelabel);
	winClose();
	if (isFromCore47 !== undefined && isFromCore47) {
		show_sgp(sgpId);
	} else {
		window.location.href = window.location.href;
	}
}
// 添加/编辑链接
function insUpdLinkWin(sgs_id) {
	if (resObj[sgs_id] === undefined) {
		resObj[sgs_id] = {};
	}
	var fpn = new Wzb.FormPanel({
		id : 'addLinkForm',
		standardSubmit : true,
		labelAlign : 'top',
		autoHeight : true,
		frame : false,
		labelWidth : 80,
		bodyStyle : Wzb.style_inner_space,
		border : true,
		items : [{
			xtype : 'textfield',
			vtype : 'url',
			vtypeText : Wzb.l('486'),
			name : 'sgs_content',
			fieldLabel : '*' + Wzb.l('487'),
			allowBlank : false,
			blankText : Wzb.l('488'),
			maxLength : 255,
			maxLengthText : Wzb.l('489'),
			width : 300,
			value : resObj[sgs_id]['sgs_content_noescape']
		}, {
			xtype : 'textfield',
			name : 'sgs_title',
			fieldLabel : '*' + Wzb.l('79'),
			allowBlank : false,
			blankText : Wzb.l('490'),
			maxLength : 50,
			maxLengthText : Wzb.l('491'),
			width : 300,
			value : resObj[sgs_id]['sgs_title_noescape']
		}, {
			xtype : 'textarea',
			name : 'sgs_desc',
			fieldLabel : Wzb.l('492'),
			maxLength : 255,
			maxLengthText : Wzb.l('493'),
			width : 300,
			height : 90,
			value : resObj[sgs_id]['sgs_desc_noescape']
		}, {
			xtype : 'hidden',
			name : 'sgs_type',
			value : 'LINK'
		}, {
			xtype : 'hidden',
			name : 'module',
			value : 'JsonMod.studyGroup.StudyGroupModule'
		}, {
			xtype : 'hidden',
			name : 'cmd',
			value : ''
		}, {
			xtype : 'hidden',
			name : 'sgp_id',
			value : sgpId
		}, {
			xtype : 'hidden',
			name : 'sgs_id',
			value : sgs_id
		}, {
			xtype : 'hidden',
			name : 'url_success',
			value : self.location.href
		}, {
			xtype : 'hidden',
			name : 'url_failure',
			value : self.location.href
		}, {
			xtype : 'hidden',
			name : 'sgs_upd_timestamp',
			value : resObj[sgs_id]['sgs_upd_timestamp']
		}],
		buttons : [new Ext.Button({
			text : Wzb.l('329'),
			handler : function() {
				formSubmit('addLinkForm', sgs_id);
			}
		}), new Ext.Button({
			text : Wzb.l('330'),
			handler : winClose
		})]
	});
	popWin((sgs_id !== undefined && sgs_id > 0) ? Wzb.l('494') : Wzb
			.l('182'), fpn);
}

// 添加/编辑档案
function insUpdDocWin(sgs_id) {

	if (resObj[sgs_id] === undefined) {
		resObj[sgs_id] = {};
	}
	var fpn = new Wzb.FormPanel({
		id : 'addDocForm',
		standardSubmit : true,
		labelAlign : 'top',
		autoHeight : true,
		frame : false,
		bodyStyle : Wzb.style_inner_space,
		fileUpload : true,
		items : [{
			xtype : 'textfield',
			name : 'sgs_content',
			inputType : 'file',
			fieldLabel : '*' + Wzb.l('495'),
			allowBlank : false,
			blankText : Wzb.l('338')
		}, {
			xtype : 'textfield',
			name : 'sgs_title',
			fieldLabel : '*' + Wzb.l('79'),
			allowBlank : false,
			blankText : Wzb.l('338'),
			maxLength : 50,
			maxLengthText : Wzb.l('491'),
			width : 300,
			value : resObj[sgs_id]['sgs_title_noescape']
		}, {
			xtype : 'textarea',
			name : 'sgs_desc',
			maxLength : 255,
			maxLengthText : Wzb.l('493'),
			width : 300,
			height : 90,
			fieldLabel : Wzb.l('492'),
			value : resObj[sgs_id]['sgs_desc_noescape']
		}, {
			xtype : 'hidden',
			name : 'sgs_type',
			value : 'DOC'
		}, {
			xtype : 'hidden',
			name : 'module',
			value : 'JsonMod.studyGroup.StudyGroupModule'
		}, {
			xtype : 'hidden',
			name : 'cmd',
			value : ''
		}, {
			xtype : 'hidden',
			name : 'sgp_id',
			value : sgpId
		}, {
			xtype : 'hidden',
			name : 'sgs_id',
			value : sgs_id
		}, {
			xtype : 'hidden',
			name : 'url_success',
			value : self.location.href
		}, {
			xtype : 'hidden',
			name : 'url_failure',
			value : self.location.href
		}, {
			xtype : 'hidden',
			name : 'sgs_upd_timestamp',
			value : resObj[sgs_id]['sgs_upd_timestamp']
		}],
		buttons : [new Ext.Button({
			text : Wzb.l('329'),
			handler : function() {
				formSubmit('addDocForm', sgs_id);
			}
		}), new Ext.Button({
			text : Wzb.l('330'),
			handler : winClose
		})]
	});
	if (sgs_id !== undefined && sgs_id > 0) {
		var keep = {
			xtype : 'radio',
			name : 'is_keep_doc',
			hideLabel : true,
			boxLabel : Wzb.l('502'),
			itemCls : 'float-pos2',
			clearCls : 'allow-float',
			checked : true,
			inputValue : true,
			listeners : {
				check : function(radio, checked) {
					if (checked) {
						fpn.getForm().findField('sgs_content').disable();
					} else {
						fpn.getForm().findField('sgs_content').enable();
					}
				}
			}
		};
		var update = {
			xtype : 'radio',
			name : 'is_keep_doc',
			hideLabel : true,
			boxLabel : Wzb.l('503'),
			inputValue : false
		}
		fpn.insert(1, update);
		fpn.insert(1, keep);
	}
	popWin((sgs_id !== undefined && sgs_id > 0) ? Wzb.l('503') : Wzb
			.l('186'), fpn);
}
// 添加/编辑课程
function insUpdCosWin(sgs_id) {
	if (resObj[sgs_id] === undefined) {
		resObj[sgs_id] = {};
	}
	var fpn = new Wzb.FormPanel({
		id : 'addCourseForm',
		standardSubmit : true,
		labelAlign : 'top',
		autoHeight : true,
		frame : false,
		bodyStyle : Wzb.style_inner_space,
		items : [{
			xtype : 'textfield',
			name : 'sgs_content',
			allowBlank : false,
			blankText : Wzb.l('508'),
			maxLength : 255,
			maxLengthText : Wzb.l('510'),
			width : 300,
			fieldLabel : '*'
					+ Wzb.l('512')
					+ '(<span class="wzb-common-link" onmouseover="Wzb.changeToolTipDis(event, \'tooltip\', \'show\',\''
					+ Wzb.l('191')
					+ '\')" onmouseout="Wzb.changeToolTipDis(event, \'tooltip\', \'hide\')">'
					+ Wzb.l('190') + '</span>)',
			value : resObj[sgs_id]['sgs_content_noescape']
		}, {
			xtype : 'textfield',
			fieldLabel : '*' + Wzb.l('79'),
			name : 'sgs_title',
			allowBlank : false,
			blankText : Wzb.l('490'),
			maxLength : 50,
			maxLengthText : Wzb.l('491'),
			width : 300,
			value : resObj[sgs_id]['sgs_title_noescape']
		}, {
			xtype : 'textarea',
			name : 'sgs_desc',
			maxLength : 255,
			maxLengthText : Wzb.l('493'),
			width : 300,
			height : 90,
			fieldLabel : Wzb.l('492'),
			value : resObj[sgs_id]['sgs_desc_noescape']
		}, {
			xtype : 'hidden',
			name : 'sgs_type',
			value : 'COURSE'
		}, {
			xtype : 'hidden',
			name : 'module',
			value : 'JsonMod.studyGroup.StudyGroupModule'
		}, {
			xtype : 'hidden',
			name : 'cmd',
			value : ''
		}, {
			xtype : 'hidden',
			name : 'sgp_id',
			value : sgpId
		}, {
			xtype : 'hidden',
			name : 'sgs_id',
			value : sgs_id
		}, {
			xtype : 'hidden',
			name : 'url_success',
			value : self.location.href
		}, {
			xtype : 'hidden',
			name : 'url_failure',
			value : self.location.href
		}, {
			xtype : 'hidden',
			name : 'sgs_upd_timestamp',
			value : resObj[sgs_id]['sgs_upd_timestamp']
		}],
		buttons : [new Ext.Button({
			text : Wzb.l('329'),
			handler : function() {
				formSubmit('addCourseForm', sgs_id);
			}
		}), new Ext.Button({
			text : Wzb.l('330'),
			handler : winClose
		})]
	});
	popWin((sgs_id !== undefined && sgs_id > 0) ? Wzb.l('518') : Wzb
			.l('188'), fpn);
}

// form 提交
function formSubmit(formId, sgs_id) {
	var form = Ext.getCmp(formId).getForm();
	var values = form.getValues();
	form.setValues({
		sgs_title : (values.sgs_title).trim(),
		sgs_desc : (values.sgs_desc).trim()
	});
	if (form.isValid()) {
		if (isFromCore47 !== undefined && isFromCore47) {
			//用来控制是否弹出学习小组详细信息窗口(培训管理员学习小组管理页面)
			wb_utils_set_cookie('sgp_id', sgpId);
		}

		if (sgs_id !== undefined && sgs_id > 0) {
			form.setValues({
				cmd : 'upd_sgp_res'
			});
		} else {
			form.setValues({
				cmd : 'ins_sgp_res'
			});
		}
		form.getEl().dom.action = Wzb.uri_dis;
		form.submit();
	}
}

// 自动添加成员设定
function setMemberAutoWin() {

	var autoGldmanParam = 'goldenman_myteamdetails_item,goldenman_myteamdetails_group';
	var autoStore = new Ext.data.JsonStore({
		url : Wzb.getDisUrl('module', 'JsonMod.studyGroup.StudyGroupModule',
				'cmd', 'get_auto_sgp_mem', 'goldenman_param', autoGldmanParam,
				'sgp_id', sgpId)
	});
	autoStore.load({
		callback : function(s, r, o) {
			var groupGoldenMan = getGoldenmanPanel(autoStore,
					'goldenman_myteamdetails_group');
			var fpn = new Wzb.FormPanel({
				id : 'formTeamMemAutoId',
				formId : 'formTeamMemAuto',
				border : false,
				frame : false,
				items : [Wzb.getReduceSrhTitlePn(Wzb.l('196')),
						groupGoldenMan, {
							xtype : 'hidden',
							name : 'cmd',
							value : 'set_auto_sgp_mem'
						}, {
							xtype : 'hidden',
							name : 'module',
							value : 'JsonMod.studyGroup.StudyGroupModule'
						}, {
							xtype : 'hidden',
							name : 'sgp_id',
							value : sgpId
						}, {
							xtype : 'hidden',
							name : 'sgp_tcr_id',
							value : sgpTcrId
						}, {
							xtype : 'hidden',
							name : 'sgm_ent_id_usg'
						}],
				buttons : [new Ext.Button({
					text : Wzb.l('329'),
					handler : function() {
						formTeamMemAutoSubmit();
					}
				}), new Ext.Button({
					text : Wzb.l('330'),
					handler : winClose
				})]
			});
			if (isTa) {
				var itemGoldenMan = getGoldenmanPanel(autoStore,
						'goldenman_myteamdetails_item');
				fpn
						.add(Wzb.getReduceSrhTitlePn(Wzb.l('197')),
								itemGoldenMan);
				fpn.add({
					xtype : 'hidden',
					name : 'sgm_ent_id_itm'
				});
				fpn.add({
					xtype : 'hidden',
					name : 'ta',
					value : isTa
				});
			}
			popWin(Wzb.l('195'), fpn, autoStore);

		}
	});
}
// 手动添加成员
function setMemberCustWin() {
	var custGldmanParam = 'goldenman_myteamdetails_user';
	var custStore = new Ext.data.JsonStore({
		url : Wzb.getDisUrl('module', 'JsonMod.studyGroup.StudyGroupModule',
				'cmd', 'get_cust_sgp_mem', 'goldenman_param', custGldmanParam,
				'sgp_id', sgpId)
	});
	custStore.load({
		callback : function() {
			var groupGoldenMan = getGoldenmanPanel(custStore,
					'goldenman_myteamdetails_user');
			var fpn = new Wzb.FormPanel({
				id : 'formTeamMemCustId',
				formId : 'formTeamMemCust',
				border : false,
				frame : false,
				items : [Wzb.getReduceSrhTitlePn(Wzb.l('199')),
						groupGoldenMan, {
							xtype : 'hidden',
							name : 'cmd',
							value : 'set_cust_sgp_mem'
						}, {
							xtype : 'hidden',
							name : 'module',
							value : 'JsonMod.studyGroup.StudyGroupModule'
						}, {
							xtype : 'hidden',
							name : 'sgp_id',
							value : sgpId
						}, {
							xtype : 'hidden',
							name : 'is',
							value : sgpId
						}, {
							xtype : 'hidden',
							name : 'sgp_tcr_id',
							value : sgpTcrId
						}, {
							xtype : 'hidden',
							name : 'sgm_ent_id_usr'
						}],
				buttons : [new Ext.Button({
					text : Wzb.l('329'),
					handler : function() {
						formTeamMemCustSubmit();
					}
				}), new Ext.Button({
					text : Wzb.l('330'),
					handler : winClose
				})]
			});
			popWin(Wzb.l('198'), fpn, custStore);
		}
	});
}
// 提交成员设置(自动)
function formTeamMemAutoSubmit(type) {

	var form = Ext.getDom('formTeamMemAuto');
	form.method = 'post';
	form.action = Wzb.uri_dis;
	form.sgm_ent_id_usg.value = getGoldenmanIdStr(form.usg_ent_id_lst);
	if (isTa) {
		form.sgm_ent_id_itm.value = getGoldenmanIdStr(form.itm_id_lst);
	}

	Ext.getCmp('formTeamMemAutoId').getForm().submit({
		success : function() {
			closeWinAndReloadGrid('memberGridPn');
		},
		failure : function(f, a) {
			// alert(a.result.msg.codelabel);
			closeWinAndReloadGrid('memberGridPn');
		}
	});

}
// 自动添加成员设置--用户组树
function openGroupTreeInTc() {
	goldenman.opentree('user_group', 1, 'usg_ent_id_lst', '', '0', '', '', '1',
			'0', '0', '0', '0', '1', '0', '', '', sgpTcrId, 0)
}
// 自动添加成员设置--课程树
function openItemTreeInTc() {
	goldenman.opentree('tc_catalog_item_and_run', 4, 'itm_id_lst', '', '0', '',
			'', '1', '0', '0', '0', '0', '1', '0', '', '', sgpTcrId, 0,'','','', sgpId)
}
// 手动添加成员--成员树
function openUserTreeInTc() {
	goldenman.opentree('user_group_and_user', 4, 'usr_ent_id_lst', '', '0', '',
			'', '1', '0', '0', '0', '0', '1', '0', '', '', sgpTcrId, 0, '', '',
			'1')
}
// 手动添加成员--搜索
function popupUserSearchPrep() {
	goldenman.openitemaccsearchwin('1', 'NLRN_1', 'usr_ent_id_lst', '', '5',
			sgpTcrId, '')
}

// 提交成员设置(手动)
function formTeamMemCustSubmit() {
	var form = Ext.getDom('formTeamMemCust');
	form.method = 'post';
	form.action = Wzb.uri_dis;
	form.sgm_ent_id_usr.value = getGoldenmanIdStr(form.usr_ent_id_lst);

	Ext.getCmp('formTeamMemCustId').getForm().submit({
		success : function() {
			closeWinAndReloadGrid('memberGridPn');
		},
		failure : function(f, a) {
			// alert(a.result.msg.codelabel);
			closeWinAndReloadGrid('memberGridPn');
		}
	});
}
function getGoldenmanIdStr(ele) {
	var str = '';
	if (ele && ele.options && ele.options.length > 0) {
		var len = ele.options.length;
		for (i = 0; i < len; i++) {
			str += ele.options[i].value;
			if (i !== len - 1) {
				str += '~';
			}
		}
	}
	return str;
}

function getGoldenmanPanel(store, gldName) {
	var gldHtml = '';
	gldHtml = Wzb.goldenMan.getGoldenManHtml(store, gldName);
	var p = new Wzb.Panel({
		border : false,
		html : gldHtml
	});
	Wzb.goldenMan.applyJsFunction(gldHtml);
	return p;
}

// 弹出编辑窗口
var win = null;
function popWin(tl, fpn, store) {
	if (win != null) {
		win.close();
	}
	win = new Wzb.Window({
		title : tl,
		width : 400,
		height : 360,
		items : [fpn]
	});
	win.show();

	if (fpn.getId() === 'formTeamMemAutoId') {
		Wzb.goldenMan.setOptions(store,
				Ext.getDom('formTeamMemAuto').usg_ent_id_lst,
				'goldenman_myteamdetails_group');
		if (isTa) {
			Wzb.goldenMan.setOptions(store,
					Ext.getDom('formTeamMemAuto').itm_id_lst,
					'goldenman_myteamdetails_item');
		}
	}
	if (fpn.getId() === 'formTeamMemCustId') {
		Wzb.goldenMan.setOptions(store,
				Ext.getDom('formTeamMemCust').usr_ent_id_lst,
				'goldenman_myteamdetails_user');
	}
}
// 关闭弹出窗口
function winClose() {
	if (win !== null && win !== undefined) {
		win.close();
	}
}
// 关闭弹出窗口&刷新列表
function closeWinAndReloadGrid(gridId) {
	winClose();
	var grid = Ext.getCmp(gridId);
	grid.getStore().load({
		params : {
			start : 0,
			limit : grid.getBottomToolbar().pageSize
		}

	});
}
