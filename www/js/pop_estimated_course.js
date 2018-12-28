var showType = Wzb.getUrlParam('type');
if (showType === '') {
	showType = 'hot_course';
}
new Wzb.PageRenderer({
	url : Wzb.getDisUrl('module', 'JsonMod.Course.CourseModule', 'cmd',
			'get_course_rank_lst', 'activetab', showType),
	fn : onready,
	wzbLayout : Wzb.layout.TYPE4
});
function onready(mainStore) {
	Wzb.renderPageTitle(Wzb.l('13'));
	switch (showType) {
		case 'hot_course' :
			tabIndex = 0;
			break;
		case 'comments_course' :
			tabIndex = 1;
			break;
		case 'new_course' :
			tabIndex = 2;
			break;
		default :
			tabIndex = 0;
	}
	// 热门课程
	var colModelObj = new Ext.grid.ColumnModel([{
		header : Wzb.l('85'),

		width : Wzb.util_total_width * 0.05,
		align : 'right',
		renderer : renderOrder
	}, {
		header : Wzb.l('79'),
		width : Wzb.util_total_width * 0.67,
		renderer : renderTitle
	}, {
		header : Wzb.l('80'),
		width : Wzb.util_total_width * 0.16,
		renderer : renderGrade
	}, {
		header : Wzb.l('88'),
		align : 'center',
		width : Wzb.util_total_width * 0.12,
		dataIndex : 'app_count',
		renderer : function(value) {
			var str = '<span class=\'wzb-common-text\'>' + value + '</span>';
			return str;
		}
	}]);
	var readerObj = new Ext.data.JsonReader({
		root : 'hot_course["hot_course_lst"]'
	}, ['order', 'itm_id', 'itm_title', 'itm_type', 'total_score',
			'total_count', 'itm_comment_avg_score', 'app_count', 'duration',
			'start_timestamp', 'itm_desc', 'lab_itm_type',
			'recent_start_classes', 'itm_dummy_type', 'itm_icon']);
	var storeObj = new Ext.data.Store({
		remoteSort : true,
		proxy : getProxy(showType, 'hot_course', mainStore),
		reader : readerObj,
		sortInfo : {field: 'app_count', direction: 'DESC'}
	});
	
	var popGrid = new Wzb.GridPanel({
		title : Wzb.l('87'),
		width : Wzb.util_total_width,
		store : storeObj,
		colModel : colModelObj,
		viewConfig : {
			emptyText : Wzb.getEmptyText()
		},
		border : true
	});
	popGrid.addListener({
		'render' : function(t) {
			t.getStore().load();
		}
	});
	// 最佳课程
	var colModelObj1 = new Ext.grid.ColumnModel([{
		header : Wzb.l('85'),
		width : Wzb.util_total_width * 0.05,
		align : 'right',
		renderer : renderOrder
	}, {
		header : Wzb.l('79'),
		width : Wzb.util_total_width * 0.67,
		renderer : renderTitle
	}, {
		header : Wzb.l('80'),
		width : Wzb.util_total_width * 0.16,
		dataIndex : 'itm_comment_avg_score',
		renderer : renderGrade
	}, {
		header : Wzb.l('88'),
		width : Wzb.util_total_width * 0.12,
		align : 'center',
		dataIndex : 'app_count',
		renderer : function(value) {
			var str = '<span class=\'wzb-common-text\'>' + value + '</span>';
			return str;
		}
	}]);
	var readerObj1 = new Ext.data.JsonReader({
		root : 'comments_course["comments_course_lst"]'
	}, ['order', 'itm_id', 'itm_title', 'itm_type', 'total_score',
			'total_count', 'itm_comment_avg_score', 'app_count', 'duration',
			'start_timestamp', 'itm_desc', 'lab_itm_type',
			'recent_start_classes', 'itm_dummy_type', 'itm_icon']);
	var storeObj1 = new Ext.data.Store({
		remoteSort : true,
		proxy : getProxy(showType, 'comments_course', mainStore),
		reader : readerObj1,
		sortInfo : {field: 'itm_comment_avg_score', direction: 'DESC'}
	});
	var estimatedGrid = new Wzb.GridPanel({
		title : Wzb.l('84'),
		width : Wzb.util_total_width,
		viewConfig : {
			emptyText : Wzb.getEmptyText()
		},
		store : storeObj1,
		colModel : colModelObj1,
		border : true
	});
	estimatedGrid.addListener({
		'render' : function(t) {
			t.getStore().load();
		}
	});
	// 最新培训
	var colModelObj2 = new Ext.grid.ColumnModel([{
		header : Wzb.l('79'),
		width : Wzb.util_total_width * 0.85,
		renderer : renderTitle
	}, {
		header : Wzb.l('534'),
		width : Wzb.util_total_width * 0.15,
		renderer : showTime
	}]);
	var readerObj2 = new Ext.data.JsonReader({
		root : 'new_course["new_course_lst"]'
	}, ['order', 'itm_id', 'itm_title', 'itm_type', 'total_score',
			'total_count', 'itm_comment_avg_score', 'app_count', 'duration',
			'recent_start_classes', 'start_timestamp', 'issue_data',
			'itm_desc', 'itm_publish_timestamp', 'lab_itm_type',
			'itm_dummy_type', 'itm_icon']);
	var storeObj2 = new Ext.data.Store({
		url : Wzb.getDisUrl('module', 'JsonMod.Course.CourseModule', 'cmd',
				'get_course_rank_lst', 'activetab', 'new_course'),
		remoteSort : true,
		proxy : getProxy(showType, 'new_course', mainStore),
		reader : readerObj2
	});
	var newGrid = new Wzb.GridPanel({
		title : Wzb.l('89'),
		viewConfig : {
			emptyText : Wzb.getEmptyText()
		},
		width : Wzb.util_total_width,
		store : storeObj2,
		colModel : colModelObj2,
		border : true
	});
	newGrid.addListener({
		'render' : function(t) {
			t.getStore().load();
		}
	});
	// 显示出来
	var tabs = new Wzb.TabPanel({
		id : 'maintab',
		autoHeight : true,
		border : true,
		width : Wzb.util_total_width,
		layoutOnTabChange : true,
		type : 4
	});
	tabs.add(popGrid);
	tabs.add(estimatedGrid);
	tabs.add(newGrid);
	tabs.activate(tabIndex);
	tabs.addClass('bottom');
	tabs.render('container');
	tabs.addClass('panel-maincontainer');
}

function renderOrder(value, metadata, record, rowIndex, colIndex, store) {
	var order = Wzb.getNumberImg(Number(value + 1));
	var obj = {
		1 : order
	};
	var str =  WzbHtm.cos_sort(obj);

	return str;
}
// 星图
function renderGrade(value, metadata, record, rowIndex, colIndex, store) {
	var s = '';
	for (var i = 1; i <= record.data.itm_comment_avg_score; i++) {
		s += WzbHtm.scorestar0({
			1 : Wzb.getCommonImagePath('star_dim.gif')
		})
	}
	for (var i = 1; i <= 5 - record.data.itm_comment_avg_score; i++) {
		s += WzbHtm.scorestar1({
			1 : Wzb.getCommonImagePath('star.gif')
		})
	}
	s += '<span class="wzb-common-text"> (' + record.data.total_count
			+ ')</span>';
	return s;
}
function renderTitle(value, metadata, record, rowIndex, colIndex, store) {
	var date = '';
	if (record.data.itm_type == Wzb.util_item_type_classroom
			&& record.data.recent_start_classes !== undefined
			&& record.data.recent_start_classes.start_classes !== undefined) {
		var datetemp = '';
		for (var i = 0; i < record.data.recent_start_classes.start_classes.length; i++) {
			if (i > 0) {
				datetemp += ', ';
			}
			datetemp += Wzb.displayTime(
					record.data.recent_start_classes.start_classes[i].start_date,
					Wzb.time_format_ymd)
					+ ' ('
					+ record.data.recent_start_classes.start_classes[i].itm_title
					+ ')';
		}
		if(record.data.recent_start_classes.class_count > 2){
			datetemp += '...';
		}
		date = Wzb.l('361') + ': ' + datetemp;
	}
	var itmObj = {
		1 : Wzb.getCourseImage(record.data.itm_icon,
				record.data.itm_dummy_type),
		2 : 'course_detail.htm?itm_id=' + record.data.itm_id,
		3 : record.data.itm_title,
		label_itm_type : Wzb.l('265') + ':',
		4 : record.data.lab_itm_type,
		5 : record.data.itm_desc,
		6 : date
	};
	var str = WzbHtm.share_renderTitle(itmObj);
	return str;
}

function getProxy(curType, myType, mainStore) {
	var bb = null;
	if (curType == myType) {
		bb = new Ext.data.MemoryProxy(mainStore.reader.jsonData);
	} else {
		bb = new Ext.data.HttpProxy({
			url : Wzb.getDisUrl('module', 'JsonMod.Course.CourseModule', 'cmd',
					'get_course_rank_lst', 'activetab', myType)
		});
	}
	return bb;
}
function showTime(value, metadata, record, rowIndex, colIndex, store) {
	var str = '<span class=\'wzb-common-text\'>'
			+ Wzb.displayTime(record.data.itm_publish_timestamp) + '</span>';
	return str;
}