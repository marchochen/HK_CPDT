var search_id = Wzb.getUrlParam('search_id');
new Wzb.PageRenderer({
	url : '',
	fn : onready,
	wzbLayout : Wzb.layout.TYPE4
});

var fieldsetWidth = 600;
var searchTextWidth = 233;
var str = Wzb.getUrlParam('expand');
var expand = false;
if (str !== '') {
	expand = Boolean(str);
}
var activetab = Wzb.getUrlParam('activetab');
if (activetab === undefined || activetab === '') {
	activetab = 0;
}

function onready(mainStore) {

	Wzb.renderPageTitle(Wzb.l('317'));

	var main_container = new Wzb.TabPanel({
		width : Wzb.util_total_width,
		border : true,
		autoHeight : true,
		defaults : {
			autoHeight : true
		},
		layoutOnTabChange : true,
		activeTab : activetab,
		items : [{
			title : Wzb.l('355'),
			listeners : {
				render : function(component) {
					showCourseAdvanceForm(component);
				}
			}
		}, {
			title : Wzb.l('345'),
			listeners : {
				render : function(component) {
					showKnowAdvanceForm(component);
				}
			}
		}, {
			title : Wzb.l('32'),
			listeners : {
				render : function(component) {
					showForumAdvanceForm(component);
				}
			}
		}]
	});

	if(Wzb.has_material_center_view){
		main_container.add({
			title : Wzb.l('691'),
			listeners : {
				render : function(component) {
					showMaterialAdvanceForm(component);
				}
			}
		});
	}
	main_container.render('container');
	main_container.addClass('bottom');
	main_container.addClass('panel-maincontainer');
	// 培训
	function showCourseAdvanceForm(component) {
		var cosSrhStore = new Ext.data.JsonStore({
			url : Wzb.getDisUrl('module', 'JsonMod.Course.CourseModule', 'cmd',
					'adv_srh_itm_prep')
		});

		var hasCompetence = false;

		cosSrhStore.load({
			callback : function(r, o, s) {
				var courseAdvanceForm = new Wzb.FormPanel({
					id : 'courseAdvanceForm',
					standardSubmit : true,
					labelAlign : 'left',
					labelWidth : 56,
					autoHeight : true,
					defaults : {
						autoHeight : true
					},
					bodyStyle : 'padding:8px 0px 0px 50px',
					border : false,
					keys : [{
						key : Ext.EventObject.ENTER,
						fn : handlerCosFn
					}],
					items : [{
						xtype : 'textfield',
						width : searchTextWidth,
						fieldLabel : Wzb.l('388'),
						name : 'itm_code'
					}, getSearchKeywordObj(), {
						xtype : 'fieldset',
						width : fieldsetWidth,
						checkboxToggle : false,
						title : Wzb.l('265'),
						items : [Wzb.getItmTypeLst(
								cosSrhStore.reader.jsonData['srh_meta'],
								new Object({
									itm_dummy_type : '',
									label : '-- ' + Wzb.l('64') + ' --'
								}))]
					}, {
						xtype : 'fieldset',
						width : fieldsetWidth,
						checkboxToggle : false,
						title : Wzb.l('394'),
						items : [getNodeTree(Wzb.l('428'), cosSrhStore,
								'tc_cat_lst')]
					}, {
						xtype : 'fieldset',
						width : fieldsetWidth,
						checkboxToggle : false,
						title : Wzb.l('396'),
						items : [Wzb.getDateFieldRange({
							flabel : Wzb.l('361'),
							tlabel : Wzb.l('383'),
							fhname : 'srh_appn_start_datetime',
							thname : 'srh_appn_end_datetime',
							fwidth : 180,
							flwidth : 80,
							tlwidth : 16
						})]
					}, {
						xtype : 'hidden',
						name : 'module',
						value : 'JsonMod.Wzb'
					}, {
						xtype : 'hidden',
						name : 'cmd',
						value : 'save_search'
					}, {
						xtype : 'hidden',
						name : 'url_success'
					}, {
						xtype : 'hidden',
						name : 'ske_id_lst'
					}, {
						xtype : 'hidden',
						name : 'tnd_id_lst'
					}, {
						xtype : 'hidden',
						name : 'tcr_id'
					}],
					buttons : [{
						text : Wzb.l('400'),
						handler : handlerCosFn
					}]
				});
				courseAdvanceForm.addListener('afterlayout', function(obj) {
					obj.items.get(0).focus();
				});
				if (this.reader.jsonData['competence_tree'] !== undefined
						&& this.reader.jsonData['competence_tree'].length > 0) {
					hasCompetence = true;
					var competence = {
						xtype : 'fieldset',
						width : fieldsetWidth,
						checkboxToggle : false,
						title : Wzb.l('50'),
						items : [getNodeTree(Wzb.l('360'), cosSrhStore,
								'competence_tree')]
					};
					courseAdvanceForm.insert(3, competence);
				}
				component.add(courseAdvanceForm);
				component.doLayout();
			}
		});
		function handlerCosFn() {
			var basicForm = Ext.getCmp('courseAdvanceForm').getForm();
			basicForm.setValues({
				url_success : '../htm/advance_search_result_course.htm'
			});
			// 选择的能力分类
			if (hasCompetence === true) {
				var selectedCompetence = Ext.getCmp('competence_tree')
						.getSelectionModel().getSelectedNode();
				if (selectedCompetence !== null && selectedCompetence.attributes.ske_id !== '') {
					basicForm.setValues({
						ske_id_lst : selectedCompetence.attributes.ske_id
					});
				}
			}
			// 选择的目录
			var selectedCosTypeNode = Ext.getCmp('tc_cat_lst')
					.getSelectionModel().getSelectedNode();
			if (selectedCosTypeNode !== null
					&& selectedCosTypeNode.attributes.id !== '') {
				if (selectedCosTypeNode.attributes.type === 'TC') {
					//培训中心树节点的ID有前缀“TC~”，先去掉
					var tcrId = selectedCosTypeNode.id.substring(selectedCosTypeNode.id.indexOf('~') + 1);
					basicForm.setValues({
						tcr_id : tcrId
					});
				} else if (selectedCosTypeNode.attributes.type === 'CATALOG') {
					basicForm.setValues({
						tnd_id_lst : selectedCosTypeNode.attributes.id
					});
				}
			}
			basicForm.getEl().dom.action = Wzb.uri_dis;
			basicForm.submit();
		}
	}
	// 知道
	function showKnowAdvanceForm(component) {
		var knowSrhStore = new Ext.data.JsonStore({
			url : Wzb.getDisUrl('module', 'JsonMod.know.KnowModule', 'cmd',
					'get_catalog_struct')
		});
		var remark = {
			1 : Wzb.l('318')
		};
		knowSrhStore.load({
			callback : function(r, o, s) {
				var knowAdvanceForm = new Wzb.FormPanel({
					id : 'knowAdvanceForm',
					standardSubmit : true,
					labelAlign : 'left',
					autoHeight : true,
					defaults : {
						autoHeight : true
					},
					bodyStyle : 'padding:8px 0px 0px 50px',
					border : false,
					keys : [{
						key : Ext.EventObject.ENTER,
						fn : handlerKnowFn
					}],
					items : [getSearchKeywordObj(), {
						xtype : 'fieldset',
						width : fieldsetWidth,
						checkboxToggle : false,
						title : Wzb.l('319'),
						items : [{
							layout : 'column',
							border : false,
							defaults : {
								layout : 'form',
								border : false,
								columnWidth : .2
							},
							items : [{
								items : [{
									xtype : 'checkbox',
									boxLabel : Wzb.l('252'),
									hideLabel : true,
									name : 'srh_que_type',
									inputValue : 'que_unans_lst'
								}]
							}, {
								items : [{
									xtype : 'checkbox',
									boxLabel : Wzb.l('253'),
									hideLabel : true,
									name : 'srh_que_type',
									inputValue : 'que_ansed_lst'
								}]
							}, {
								items : [{
									xtype : 'checkbox',
									boxLabel : Wzb.l('254'),
									hideLabel : true,
									name : 'srh_que_type',
									inputValue : 'que_popular_lst'
								}]
							}, {
								items : [{
									xtype : 'checkbox',
									boxLabel : Wzb.l('684'),
									hideLabel : true,
									name : 'srh_que_type',
									inputValue : 'que_faq_lst'
								}]
							}]
						}]
					}, {
						xtype : 'fieldset',
						width : fieldsetWidth,
						checkboxToggle : false,
						title : Wzb.l('217'),
						items : [
								getNodeTree(Wzb.l('223'), knowSrhStore,
										'catalog_structure'), {
									id : 'knotemsg',
									border : false,
									html : WzbHtm.remark(remark)
								}]
					}, {
						xtype : 'fieldset',
						width : fieldsetWidth,
						checkboxToggle : false,
						title : Wzb.l('408'),
						items : [{
							xtype : 'panel',
							border : false,
							style : 'padding:3px',
							width : 230,
							items : [new Wzb.ComboBox({
								store : Wzb.getPeriodBeforeStore(),
								hideLabel : true,
								displayField : 'display',
								valueField : 'value',
								typeAhead : false,
								mode : 'local',
								triggerAction : 'all',
								emptyText : Wzb.l('70'),
								hiddenName : 'srh_que_start_period'
							})]
						}]
					}, {
						xtype : 'hidden',
						name : 'module',
						value : 'JsonMod.Wzb'
					}, {
						xtype : 'hidden',
						name : 'cmd',
						value : 'save_search'
					}, {
						xtype : 'hidden',
						name : 'url_success'
					}, {
						xtype : 'hidden',
						name : 'srh_catalog_id'
					}],
					buttons : [{
						text : Wzb.l('400'),
						handler : handlerKnowFn
					}]
				});
				knowAdvanceForm.items.get(0).addListener('afterlayout',
						function(obj) {
							obj.items.get(0).focus();
						});

				component.add(knowAdvanceForm);
				component.doLayout();
			}
		})
		function handlerKnowFn() {
			var basicForm = Ext.getCmp('knowAdvanceForm').getForm();
			basicForm.setValues({
				url_success : '../htm/my_know_search_result.htm'
			});
			// 选择的分类
			var selectedCataStru = Ext.getCmp('catalog_structure')
					.getSelectionModel().getSelectedNodes();
			if (selectedCataStru !== null && selectedCataStru.length > 0) {
				var srh_catalog_id = '';
				for (var i = 0; i < selectedCataStru.length; i++) {
					if (i === 0) {
						srh_catalog_id = selectedCataStru[i].id;
					} else {
						srh_catalog_id += '~' + selectedCataStru[i].id;
					}
				}
				basicForm.setValues({
					srh_catalog_id : srh_catalog_id
				});
			}
			basicForm.getEl().dom.action = Wzb.uri_dis;
			basicForm.submit();
		}
	}
	// 讨论区
	function showForumAdvanceForm(component) {

		var srhTime = [[Wzb.l('434'), '-1'], [Wzb.l('435'), '1'],
				[Wzb.l('436'), '5'], [Wzb.l('437'), '10'],
				[Wzb.l('438'), '30']];
		var srhTimeStore = new Ext.data.SimpleStore({
			fields : ['display', 'value'],
			data : srhTime
		});
		var forumAdvanceForm = new Wzb.FormPanel({
			id : 'forumAdvanceForm',
			standardSubmit : true,
			labelAlign : 'left',
			autoHeight : true,
			defaults : {
				autoHeight : true
			},
			bodyStyle : 'padding:8px 0px 0px 50px',
			border : false,
			labelWidth : 56,
			keys : [{
				key : Ext.EventObject.ENTER,
				fn : handlerForumFn
			}],
			items : [{
				xtype : 'textfield',
				width : searchTextWidth,
				maxLength : 120,
				fieldLabel : Wzb.l('324'),
				name : 'created_by'
			}, {
				xtype : 'fieldset',
				width : fieldsetWidth,
				checkboxToggle : false,
				title : Wzb.l('412'),
				autoHeight : true,
				items : [{
					xtype : 'textfield',
					maxLength : 120,
					width : searchTextWidth,
					hideLabel : true,
					name : 'phrase'
				}, {
					xtype : 'checkbox',
					hideLabel : true,
					boxLabel : Wzb.l('541'),
					checked : true,
					itemCls : 'float-pos2',
					clearCls : 'allow-float',
					name : 'search_type_topic',
					inputValue : '1'
				}, {
					xtype : 'checkbox',
					hideLabel : true,
					checked : true,
					boxLabel : Wzb.l('415'),
					name : 'search_type_msg',
					inputValue : '1'
				}]
			}, {
				xtype : 'fieldset',
				width : fieldsetWidth,
				checkboxToggle : false,
				title : Wzb.l('320'),
				items : [new Wzb.ComboBox({
					store : srhTimeStore,
					hideLabel : true,
					displayField : 'display',
					valueField : 'value',
					typeAhead : false,
					mode : 'local',
					triggerAction : 'all',
					emptyText : Wzb.l('70'),
					hiddenName : 'created_after_days',
					value : srhTime[0][0]
				})]
			}, {
				xtype : 'fieldset',
				width : fieldsetWidth,
				checkboxToggle : false,
				title : Wzb.l('321'),
				items : [{
					xtype : 'radio',
					hideLabel : true,
					boxLabel : Wzb.l('255'),
					checked : true,
					itemCls : 'float-pos2',
					clearCls : 'allow-float',
					name : 'sort_order',
					inputValue : 'ASC'
				}, {
					xtype : 'radio',
					hideLabel : true,
					boxLabel : Wzb.l('256'),
					name : 'sort_order',
					inputValue : 'DESC'
				}]
			}, {
				xtype : 'hidden',
				name : 'module',
				value : 'JsonMod.Wzb'
			}, {
				xtype : 'hidden',
				name : 'cmd',
				value : 'save_search'
			}, {
				xtype : 'hidden',
				name : 'url_success'
			}],
			buttons : [{
				text : Wzb.l('400'),
				handler : handlerForumFn
			}]
		});
		forumAdvanceForm.addListener('afterlayout', function(obj) {
			obj.items.get(0).focus();
		});
		component.add(forumAdvanceForm);
		component.doLayout();

		function handlerForumFn() {
			var basicForm = Ext.getCmp('forumAdvanceForm').getForm();
			basicForm.setValues({
				url_success : '../htm/advance_search_result_forum.htm'
			});
			basicForm.getEl().dom.action = Wzb.uri_dis;
			basicForm.submit();
		}
	}
	// 关键字搜索条件模块
	function getSearchKeywordObj() {
		var p = {
			xtype : 'fieldset',
			width : fieldsetWidth,
			checkboxToggle : false,
			title : Wzb.l('412'),
			autoHeight : true,
			items : [{
				xtype : 'textfield',
				width : searchTextWidth,
				hideLabel : true,
				name : 'srh_key'
			}, {
				xtype : 'radio',
				hideLabel : true,
				boxLabel : Wzb.l('62'),
				checked : true,
				itemCls : 'float-pos2',
				clearCls : 'allow-float',
				name : 'srh_key_type',
				inputValue : 'TITLE'
			}, {
				xtype : 'radio',
				hideLabel : true,
				boxLabel : Wzb.l('63'),
				name : 'srh_key_type',
				inputValue : 'FULLTEXT'
			}]
		};
		return p;
	}
	function getNodeTree(rootText, srhStore, treeId) {
		/**
		 * treeId === competence_tree : 培训-能力类型树; treeId === tc_cat_lst :
		 * 培训-目录树; treeId === catalog_structure : 知道-分类树;
		 * treeId === tc_res_cat_lst : 知识-目录树
		 */
		var data;
		var selModel = new Ext.tree.DefaultSelectionModel();;
		if (treeId === 'catalog_structure') {
			data = srhStore.reader.jsonData['know_catalog'][treeId]
			selModel = new Ext.tree.MultiSelectionModel();// 多选
		} else {
			data = srhStore.reader.jsonData[treeId]
		}
		//培训中心ID可能会和目录ID冲突，所以在这里把培训中心树节点的ID加上前缀“TC~”
		if ((treeId === 'tc_cat_lst' || treeId === 'tc_res_cat_lst') && data !== undefined && data.length > 0) {
			for (var i = 0; i < data.length; i++) {
				var node = data[i];
				node.id = node.type + '~' + node.id;
				data[i] = node;
			}
		}
		var tree = new Wzb.TreePanel({
			id : treeId,
			border : true,
			selModel : selModel,
			loader : new Ext.tree.TreeLoader()
		});
		var root = new Ext.tree.AsyncTreeNode({
			id : '0',
			text : rootText,
			draggable : false,
			children : data
		});
		tree.setRootNode(root);
		if (treeId === 'competence_tree') {
			// 某些节点不可选
			tree.addListener('click', function(node, event) {
				if (node.attributes.choice === false) {
					return false;
				}
			});
		};
		if (treeId === 'tc_cat_lst') {
			// 课程的目录树不是一次全部查出，点击节点时再查询
			tree.addListener('beforeload', function(node) {
				var nodeId = node.id;
				//培训中心树节点的ID有前缀“TC~”，先去掉
				if (node.attributes.type !== undefined
						&& node.attributes.type === 'TC') {
					nodeId = node.id.substring(node.id.indexOf('~') + 1);
				} 
				if (nodeId !== undefined && nodeId !== null && nodeId > 0) {
					tree.getLoader().dataUrl = Wzb.getDisUrl('module',
							'JsonMod.Course.CourseModule', 'cmd',
							'get_tc_catalog_tree', 'node_id', nodeId,
							'node_type', node.attributes.type);
				}
			});
		};
		if (treeId === 'tc_res_cat_lst') {
			// 资源的目录树不是一次全部查出，点击节点时再查询
			tree.addListener('beforeload', function(node) {
				var nodeId = node.id;
				//培训中心树节点的ID有前缀“TC~”，先去掉
				if (node.attributes.type !== undefined
						&& node.attributes.type === 'TC') {
					nodeId = node.id.substring(node.id.indexOf('~') + 1);
				} 
				if (nodeId !== undefined && nodeId !== null && nodeId > 0) {
					tree.getLoader().dataUrl = Wzb.getDisUrl('module',
							'JsonMod.studyMaterial.StudyMaterialModule', 'cmd',
							'get_tc_cat_tree', 'node_id', nodeId,
							'node_type', node.attributes.type);
				}
			});
		};
		if (treeId === 'catalog_structure' || treeId === 'competence_tree') {
			// 所有分类已经在第一次查询时全部查出，这个给个没有实际用途的URL来得到去掉文件夹图标前+号的效果
			tree.addListener('beforeload', function(node, event) {
				if (node.attributes.children === undefined
						|| node.attributes.children.length === 0) {
					tree.getLoader().dataUrl = Wzb.getDisUrl('module',
							'JsonMod.Wzb', 'cmd', 'get_prof');
				}
			})
		}
		return tree;
	}

	// 知识
	function showMaterialAdvanceForm(component) {
		var resSrhStore = new Ext.data.JsonStore({
			url : Wzb.getDisUrl('module', 'JsonMod.studyMaterial.StudyMaterialModule', 'cmd',
					'adv_srh_res_prep')
		});

		resSrhStore.load({
			callback : function(r, o, s) {
				var materialAdvanceForm = new Wzb.FormPanel({
					id : 'materialAdvanceForm',
					standardSubmit : true,
					labelAlign : 'left',
					labelWidth : 56,
					autoHeight : true,
					defaults : {
						autoHeight : true
					},
					bodyStyle : 'padding:8px 0px 0px 50px',
					border : false,
					keys : [{
						key : Ext.EventObject.ENTER,
						fn : handlerResFn
					}],
					items : [
						getResSearchKeywordObj(), 
						getResTypeObj(), {
						xtype : 'fieldset',
						width : fieldsetWidth,
						checkboxToggle : false,
						title : Wzb.l('394'),
						items : [getNodeTree(Wzb.l('428'), resSrhStore,
								'tc_res_cat_lst')]
					}, getResDifficultyObj(), {
						xtype : 'hidden',
						name : 'module',
						value : 'JsonMod.Wzb'
					}, {
						xtype : 'hidden',
						name : 'cmd',
						value : 'save_search'
					}, {
						xtype : 'hidden',
						name : 'url_success'
					}, {
						xtype : 'hidden',
						name : 'cat_id'
					}, {
						xtype : 'hidden',
						name : 'tcr_id'
					}],
					buttons : [{
						text : Wzb.l('400'),
						handler : handlerResFn
					}]
				});
				materialAdvanceForm.addListener('afterlayout', function(obj) {
					obj.items.get(0).focus();
				});
				component.add(materialAdvanceForm);
				component.doLayout();
			}
		});
		function handlerResFn() {
			var basicForm = Ext.getCmp('materialAdvanceForm').getForm();
			basicForm.setValues({
				url_success : '../htm/advance_search_result_material.htm'
			});
			// 选择的目录
			var selectedResCatNode = Ext.getCmp('tc_res_cat_lst')
					.getSelectionModel().getSelectedNode();
			if (selectedResCatNode !== null
					&& selectedResCatNode.attributes.id !== '') {
				if (selectedResCatNode.attributes.type === 'TC') {
					//培训中心树节点的ID有前缀“TC~”，先去掉
					var tcrId = selectedResCatNode.id.substring(selectedResCatNode.id.indexOf('~') + 1);
					basicForm.setValues({
						tcr_id : tcrId
					});
				} else if (selectedResCatNode.attributes.type === 'CATALOG') {
					basicForm.setValues({
						cat_id : selectedResCatNode.attributes.id
					});
				}
			}
			basicForm.getEl().dom.action = Wzb.uri_dis;
			basicForm.submit();
		}
	}
	
	// 知识关键字搜索条件
	function getResSearchKeywordObj() {
		var p = {
			xtype : 'fieldset',
			width : fieldsetWidth,
			checkboxToggle : false,
			title : Wzb.l('412'),
			autoHeight : true,
			items : [{
				xtype : 'textfield',
				width : searchTextWidth,
				hideLabel : true,
				name : 'srh_key'
			}, {
				xtype : 'checkbox',
				hideLabel : true,
				boxLabel : Wzb.l('388'),
				checked : true,
				itemCls : 'float-pos2',
				clearCls : 'allow-float',
				name : 'key_type_lst',
				inputValue : 'CODE'
			}, {
				xtype : 'checkbox',
				hideLabel : true,
				boxLabel : Wzb.l('593'),
				checked : true,
				itemCls : 'float-pos2',
				clearCls : 'allow-float',
				name : 'key_type_lst',
				inputValue : 'TITLE'
			}, {
				xtype : 'checkbox',
				hideLabel : true,
				boxLabel : Wzb.l('wb_imp_tem_description'),
				checked : true,
				itemCls : 'float-pos2',
				name : 'key_type_lst',
				inputValue : 'DESC'
			}]
		};
		return p;
	}
	// 知识类型
	function getResTypeObj() {
		var resTypePn = {
			xtype : 'fieldset',
			width : fieldsetWidth,
			checkboxToggle : false,
			title : Wzb.l('265'),
			autoHeight : true,
			hideBorders : true,
			defaults : {
				layout : 'form',
				columnWidth : .9
			},
			items : [{
				defaults : {
					xtype : 'checkbox',
					hideLabel : true
				},
				items : [{
					boxLabel : Wzb.l('lab_wct'),
					itemCls : 'float-pos2',
					clearCls : 'allow-float',
					name : 'res_subtype_lst',
					inputValue : 'WCT'
				}, {
					boxLabel : Wzb.l('686'),
					itemCls : 'float-pos2',
					clearCls : 'allow-float',
					name : 'res_subtype_lst',
					inputValue : 'SSC'
				}, {
					boxLabel :　Wzb.l('687'),
					name : 'res_subtype_lst',
					itemCls : 'float-pos2',
					clearCls : 'allow-float',
					inputValue : 'RES_SCO'
				}, {
					boxLabel : Wzb.l('688'),
					itemCls : 'float-pos2',
					clearCls : 'allow-float',
					name : 'res_subtype_lst',
					inputValue : 'RES_NETG_COK'
				}]
			}]
		}
		return resTypePn;
	}
	
	// 知识难度
	function getResDifficultyObj() {
		var resTypePn = {
			xtype : 'fieldset',
			width : fieldsetWidth,
			checkboxToggle : false,
			title : Wzb.l('lab_que_diff'),
			autoHeight : true,
			hideBorders : true,
			defaults : {
				layout : 'form',
				columnWidth : .9
			},
			items : [{
				defaults : {
					xtype : 'checkbox',
					hideLabel : true
				},
				items : [{
					boxLabel : Wzb.l('lab_easy'),
					itemCls : 'float-pos2',
					clearCls : 'allow-float',
					name : 'difficulty_lst',
					inputValue : 1
				}, {
					boxLabel : Wzb.l('lab_normal'),
					itemCls : 'float-pos2',
					clearCls : 'allow-float',
					name : 'difficulty_lst',
					inputValue : 2
				}, {
					boxLabel :　Wzb.l('lab_hard'),
					itemCls : 'float-pos2',
					clearCls : 'allow-float',
					name : 'difficulty_lst',
					inputValue : 3
				}]
				
			}]
		}
		return resTypePn;
	}
}