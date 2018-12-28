function srhForumPanel(search_id) {
	function renderForumTitle(value, p, record) {
		var titleObj = {
			1 :  Wzb.getQdbUrl('cmd', 'get_mod', 'stylesheet','forum.xsl', 'mod_id', record.data.for_res_id),
			2 : record.data.for_res_title
		};
		var s = WzbHtm.renderForumTitleUrl(titleObj);
		return s;
	}
	function renderTopicTitle(value, p, record) {
		var titleObj = {
			1 : '../htm/forum.htm?1&mod_id=' + record.data.for_res_id
					+ '&topic_id=' + record.data.fto_id + '&course_id=',
			2 : record.data.fto_title
		};
		var s = WzbHtm.renderTopicTitleUrl(titleObj);
		return s;
	}
	function renderCreator(value, p, record) {
		var obj = {
			1 : Wzb.displayTime(record.data.fto_create_datetime,
					Wzb.time_format_ymdhm),
			2 : record.data.fto_create_user
		};
		var s = WzbHtm.renderCreatorPublish(obj);
		return s;
	}

	function renderPublish(value, p, record) {
		var obj = {
			1 : Wzb.displayTimeDefValue(record.data.fto_last_post_datetime,
					Wzb.time_format_ymdhm, '--'),
			2 : record.data.fto_last_post_user
		};
		var s = WzbHtm.renderCreatorPublish(obj);
		return s;
	}
	var cm = new Ext.grid.ColumnModel([{
		header : Wzb.l('325'),
		dataIndex : 'for_res_title',
		width : 200,
		renderer : renderForumTitle
	}, {
		header : Wzb.l('541'),
		dataIndex : 'fto_title',
		width : 200,
		renderer : renderTopicTitle
	}, {
		header : Wzb.l('331'),
		dataIndex : 'fto_create_user',
		width : 200,
		renderer : renderCreator
	}, {
		header : Wzb.l('323'),
		dataIndex : 'fto_msg_cnt',
		width : 200
	}, {
		header : Wzb.l('322'),
		dataIndex : 'fto_last_post_datetime',
		width : 200,
		renderer : renderPublish
	}]);

	var reader = new Ext.data.JsonReader({
		totalProperty : 'forum["total"]',
		root : 'forum["for_topic_lst"]'
	}, ['for_res_id', 'for_res_title', 'fto_id', 'fto_title', 'fto_msg_cnt',
			'fto_create_user', 'fto_create_datetime', 'fto_last_post_datetime',
			'fto_last_post_user']);

	var srhForumUrl = Wzb.getDisUrl('module', 'JsonMod.ForumModule', 'cmd',
			'srh_forum_topic', 'search_id', search_id);

	var forumListStore = new Ext.data.Store({
		remoteSort : true,
		proxy : new Ext.data.HttpProxy({
			url : srhForumUrl
		}),
		reader : reader
	});

	var bbar = Wzb.getPagingToolbar(forumListStore);
	var forumListGrid = new Wzb.GridPanel({
		id : 'forumListGrid',
		width : Wzb.util_total_width,
		autoHeight : true,
		border : false,
		store : forumListStore,
		cm : cm,
		loadMask : true,
		isChangeProxy : false,
		viewConfig : {
			emptyText : Wzb.getEmptyText()
		},
		bbar : bbar

	});
	forumListGrid.addListener({
		'render' : Wzb.renderEventFn
	});
	return forumListGrid;
}