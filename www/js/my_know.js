var activetab = Wzb.getUrlParam('activetab');
if (!activetab || activetab === '') {
	activetab = 'que_unans_lst';
}

new Wzb.PageRenderer({
	url : Wzb.getDisUrl('module', 'JsonMod.know.KnowModule', 'cmd',
			'get_know_home', 'activetab', activetab, 'dir', 'desc', 'limit',
			que_lst_page_size),// que_lst_page_size from know.js
	fn : onready,
	wzbLayout : Wzb.layout.TYPE2
});

function onready(mainStore) {
	var mainData = mainStore.reader.jsonData;
	Wzb.renderPageTitle(Wzb.l('345'));
	renderActionLink();

	getBriefPanel().render('container');

	getSearchPanel({
		store : mainStore
	}).render('container');

	var main_container = new Wzb.Panel({
		layout : 'column',
		hideBorders : true,
		width : Wzb.util_total_width,
		border : false,
		defaults : {
			cellCls : 'valign-top'
		},
		items : [{
			autoHeight : true,
			width : Wzb.util_col_width1,
			cls : 'right',
			defaults : {
				width : Wzb.util_col_width1
			},
			items : [getStatPanel(), getClassifiedContentPanel(mainStore)]
		}, {
			autoHeight : true,
			width : Wzb.util_col_width2,
			defaults : {
				width : Wzb.util_col_width2
			},
			items : [get_questions_panel(mainStore, activetab)]
		}]
	});
	main_container.render('container');
	main_container.addClass('bottom');
	main_container.addClass('panel-maincontainer');
	main_container.doLayout();

	function getCreditsPanel() {
		var creditStore = new Ext.data.Store({
			proxy : new Ext.data.MemoryProxy(mainStore.reader.jsonData['user_credits']),
			autoLoad : true,
			reader : new Ext.data.JsonReader({
				root : 'credits_lst'
			}, [{
				name : 'order'
			}, {
				name : 'id',
				mapping : 'usr_ent_id'
			}, {
				name : 'name',
				mapping : 'usr_nickname'
			}, {
				name : 'score',
				mapping : 'zd_total'
			}])
		});
		var creditPanel = new Wzb.GridPanel({
			title : Wzb.l('216'),
			store : creditStore,
			hideHeaders : true,
			columns : [{
				width : Wzb.util_col_width1 * 0.1,
				dataIndex : 'order',
				renderer : function(value) {
					var str = '<span class=\'wzb-common-text\'>' + value + '.'
							+ '</span>';
					return str;
				}
			}, {
				width : Wzb.util_col_width1 * 0.5,
				dataIndex : 'name',
				renderer : renderDisplayBil
			}, {
				width : Wzb.util_col_width1 * 0.4,
				dataIndex : 'score',
				renderer : function(value) {
					var str = '<span class=\'wzb-common-text\'>' + value
							+ '</span>';
					return str;
				}
			}],
			type : 5,
			viewConfig : {
				emptyText : Wzb.getEmptyText()
			}
		});
		creditPanel.addClass('right');
		creditPanel.addClass('bottom');
		return creditPanel;
	}

	function getStatPanel() {
		var statPanel = new Wzb.Panel({
			title : Wzb.l('212'),
			type : 4,
			bodyStyle : Wzb.style_inner_space,
			html : WzbHtm.stat({
				1 : Wzb.l('213'),
				2 : mainData['que_ans_count']['que_answered'],
				3 : Wzb.l('214'),
				4 : mainData['que_ans_count']['que_unanswered']
			})
		});
		statPanel.addClass('right');
		statPanel.addClass('bottom');
		return statPanel;
	}

	function getBriefPanel() {
		var askurl = 'window.location.href = \'ask_que.htm\'';
		var briefPanel = new Wzb.Panel({
			cls : 'bottom',
			width : Wzb.util_total_width,
			border : false,
			html : WzbHtm.brief({
				1 : Wzb.l('250'),
				2 : Wzb.l('517'),
				3 : Wzb.getButton(askurl, Wzb.l('220')),
				4 : Wzb.l('228'),
				5 : Wzb.l('520'),
				6 : Wzb.l('521'),
				7 : Wzb.l('524'),
				8 : Wzb.l('525'),
				9 : Wzb.l('526'),
				10 : Wzb.l('527'),
				11 : Wzb.l('529')
			})
		});
		briefPanel.addClass('panel-maincontainer');
		briefPanel.addClass('bottom');
		return briefPanel;
	}

	function renderDisplayBil(value, metadata, record) {
		var str = WzbHtm.displayBil({
			usr_display_bil_href : 'javascript:showUserInfoWin('
					+ record.data.id + ')',
			1 : value
		})
		return str;
	}
}



