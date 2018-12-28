var itm_id = Wzb.getUrlParam('itm_id');
var page_readonly = function() {
	if(Wzb.getUrlParam('page_readonly') === 'true') {
		return true;
	} else {
		return false;
	}
}()
new Wzb.PageRenderer({
	url : Wzb.getDisUrl('module', 'JsonMod.Course.CourseModule', 'cmd', 'get_itm', 'itm_id', itm_id, 'tvw_id', 'LRN_VIEW', 'show_run_ind', true, 'show_session_ind', true, 'stylesheet', 'itm_lrn_details_learner.xsl', 'page_readonly', page_readonly, 'url_failure', Wzb.home_url),
	fn : onready,
	wzbLayout : Wzb.layout.TYPE3,
	isRegister : page_readonly
});
var app = new wbApplication;
var course_lst = new wbCourse;	

function onready(mainStore) {
	var mainData = mainStore.reader.jsonData;
	var need_approval;

	var title = mainData['itm']['itm_title'];
	var img = Wzb.getCourseImage(mainData['itm']['itm_icon'],
			mainData['itm']['itm_dummy_type']);

	var titleHtml = WzbHtm.cos_title({
		1 : img,
		2 : title
	})
	Wzb.renderPageTitle(titleHtml);
	main_container = new Wzb.Panel({
		width : Wzb.util_total_width,
		renderTo : 'container',
		border : false,
		layout : 'column',
		items : [getItemDetailPanel(), getItemRightPanel()]
	});
	main_container.addClass('bottom');
	main_container.addClass('panel-maincontainer');
	function getItemDetailPanel() {
		var panel = new Wzb.Panel({
			width : Wzb.util_col_width2,
			bodyStyle : Wzb.style_inner_space,
			hideBorders : true,
			title : Wzb.l('122'),
			cls : 'right',
			items : [{
				html : mainStore.reader.jsonData['course_detail']
			}]
		});
		return panel;
	}

	function getItemRightPanel() {
		var rightPanelArr = [];
		if(page_readonly === false) {
			rightPanelArr.push(getActionLinkPanel());
		}
		rightPanelArr.push(getTndNodePanel());
		rightPanelArr.push(getItemLink());
		if(page_readonly === false) {
			rightPanelArr.push(Wzb.getRelatedCosGrid(mainStore));
		}
		var panel = new Wzb.Panel({
			width : Wzb.util_col_width1,
			border : false,
			items : rightPanelArr
		});
		return panel;
	}

	function getActionLinkPanel() {
		var hasPlanned = false;
		var itmData = mainData['itm'];
		if(itmData !== undefined) {
			hasPlanned = itmData['planned'];
		}
		var p = new Wzb.Panel({
			cls : 'bottom',
			bodyStyle : Wzb.style_inner_space,
			html : function() {
				var str = '';
				if(hasPlanned) {
					str = WzbHtm.added_to_plan( {
						1 : Wzb.l('358')
					})
				} else {
					str = WzbHtm.action_link({
						1 : Wzb.getCommonImagePath('ico_add_to_plan.gif'),
						2 : Wzb.l('359'),
						3 : itm_id
					})
				}
				return str;
			}()
		});
		return p;
	}

	function getItemLink() {
		var p = new Wzb.Panel({
			cls : 'bottom',
			title : Wzb.l('125'),
			bodyStyle : Wzb.style_inner_space,
			layout : 'form',
			items : [
				{
					xtype : 'textfield',
					width : '100%',
					selectOnFocus : true,
					value : window.location.href,
					labelSeparator : '',
					hideLabel : true
				}
			],
			type : 5
		});
		return p;
	}
	function getTndNodePanel() {
		var tndNodeRenderer = function(value, meta, record) {
			var formatId = 'tndNodeReadonly';
			var formatObj = {
				1 : Wzb.getFolderImagePath(),
				2 : record.data.tnd_title
			}
			if(page_readonly === false) {
				formatId = 'tndNode';
				formatObj['3'] = record.data.tnd_id;
			}
			var str = eval('WzbHtm.'+formatId)(formatObj);
			return str;
		};
		var tndNodeStore = new Ext.data.Store({
			proxy : new Ext.data.MemoryProxy(mainStore.reader.jsonData),
			autoLoad : true,
			reader : new Ext.data.JsonReader({
				root : 'tnd_lst'
			}, [{
				name : 'tnd_id'
			}, {
				name : 'tnd_title'
			}])
		});
		var panel = new Wzb.GridPanel({
			hideBorders : true,
			title : Wzb.l('124'),
			hideHeaders : true,
			cls : 'bottom',
			store : tndNodeStore,
			columns : [{
				width : '100%',
				renderer : tndNodeRenderer
			}],
			type : 5,
			viewConfig : {
				emptyText : Wzb.getEmptyText()
			}
		});
		return panel;
	}

}

function addToPlan(itm_id) {
	var url_success = '../htm/course_detail.htm?itm_id=' + itm_id;
	var url_failure = url_success;
	window.location.href = Wzb.getDisUrl('module', 'JsonMod.study.StudyModule', 'cmd', 'ins_lrn_soln', 'itm_id', itm_id, 'url_success', url_success, 'url_failure', url_failure);
}

function startLearn(itm_id, tkh_id, res_id) {
	window.location.href = '../htm/course_home.htm?itm_id=' + itm_id + '&tkh_id=' + tkh_id + '&res_id=' + res_id;
}

function course_center(tnd_id) {
	var url_failure = '../htm/course_detail.htm?itm_id=' + itm_id;
	window.location.href = '../htm/course_center.htm?tnd_id=' + tnd_id + '&url_failure=' + url_failure;
}

function showEnrolConfirm(itm_id, ent_id) {
	var str_feature = 'toolbar=' + 'no' + ',width=' + '780' + ',height=' + '400' + ',scrollbars=' + 'yes' + ',resizable=' + 'yes';
	var url = wbApplicationGetFormUrl(itm_id,ent_id, true)
	var win_name = itm_id + '_' + ent_id;
	wbUtilsOpenWin(url, win_name, false, str_feature);
}

function showCancelConfirm(frm,app_id,process_id,status_id,action_id,org_status,to_status,verb,lang) {
	app.lrn_action_exec(frm,app_id,process_id,status_id,action_id,org_status,to_status,verb,lang, true);
}