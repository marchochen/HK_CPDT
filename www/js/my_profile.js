new Wzb.PageRenderer({
	url : Wzb.getDisUrl('module', 'JsonMod.user.UserModule', 'cmd', 'get_my_profile'),
	fn : onready,
	wzbLayout : Wzb.layout.TYPE4
});

function onready(mainStore) {
	Wzb.renderPageTitle(Wzb.l('287'));

	var getDescPanel = function() {
		var descPanel = new Wzb.Panel({
			border : false,
			width : Wzb.util_total_width,
			bodyStyle : Wzb.style_inner_space,
			html: WzbHtm.share_desc(
				{
					1 : Wzb.l('289')
				}
			)
		});
		return descPanel;
	}();

	var profFormId = 'frmProfile';
	var profilePanel = new Wzb.FormPanel({
		formId : profFormId,
		fileUpload : true,
		standardSubmit : true,
		bodyStyle : Wzb.style_inner_space,
		border : false,
		items : [
			getDescPanel,
			getUserDetailShare(mainStore, {isUpd : true, formId : profFormId}),
			{
				xtype : 'hidden',
				name : 'cmd',
				value : 'save_my_profile'
			},
			{
				xtype : 'hidden',
				name : 'url_success',
				value : Wzb.home_url
			},
			{
				xtype : 'hidden',
				name : 'url_failure',
				value : Wzb.home_url
			},
			{
				xtype : 'hidden',
				name : 'option_lst'
			},
			{
				xtype : 'hidden',
				name : 'usr_ent_id',
				value : mainStore.reader.jsonData['meta']['ent_id']
			},
			{
				xtype : 'hidden',
				name : 'usr_timestamp',
				value : mainStore.reader.jsonData['user_detail']['upd_date']
			}, {
				xtype : 'hidden',
				name : 'remain_photo_ind',
				value : 'false'
			}
		],
		buttons : [{
			text : Wzb.l('329'),
			handler : function() {
				var frm = profilePanel.getForm();
				frm.getEl().dom.action = Wzb.uri_qdb;
				var values = frm.getValues();
				if(values.image_radio === 'remain_photo') {
					frm.setValues({'remain_photo_ind' : 'true'});	
				}
				var tmp_str = '';
				if(values['option_chk'] !== undefined) {//if no checkbox is checked, the value is undefined
					for(var i=0; i<values['option_chk'].length; i++) {
						var option_chk_value = values['option_chk'][i];
						if(option_chk_value !== undefined) {
							tmp_str += option_chk_value + '~';
						}
					}
					if(tmp_str === '') {//only select one checkbox.
						tmp_str = values['option_chk'];
					}
				}
				frm.setValues({'option_lst' : tmp_str});
				var file_photo = Ext.getCmp('file_photo_url');
				if(frm.isValid() && (file_photo.disabled === true || Wzb.validateFileType(file_photo.getValue()))) {
					frm.submit();
				}
			}
		}, {
			text : Wzb.l('330'),
			handler : Wzb.goHome
		}]
	});
	
	var mainCotainer = new Wzb.Panel({
		title : Wzb.l('288'),
		renderTo : 'container',
		width : Wzb.util_total_width,
		cls : 'bottom',
		items : [
			profilePanel
		]
	});
	mainCotainer.addClass('panel-maincontainer');

	var usr_des_infor_element_lst = document.getElementsByName('usr_desc_infor');
	if(usr_des_infor_element_lst !== undefined && usr_des_infor_element_lst.length > 0){
		var usr_des_infor_element = usr_des_infor_element_lst[0];
		usr_des_infor_element.outerHTML = '<div style=\"padding-top:5px;\">' + usr_des_infor_element.value + '</div>';
	}
}
