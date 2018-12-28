var from_login_change = Wzb.getUrlParam('from_login_change');
new Wzb.PageRenderer({
	url : Wzb.getDisUrl('module', 'JsonMod.Wzb', 'cmd', 'get_prof'),
	fn : onready,
	wzbLayout : Wzb.layout.TYPE4,
	noHeadFoot : from_login_change
});

function onready(mainStore) {
	Wzb.renderPageTitle(Wzb.l('314'));

	var pwdData = getPwdAttData(mainStore);
	var minLen = getPwdMinLen(pwdData);
	var maxLen = getPwdMaxLen(pwdData);
	var blankText = Wzb.l('338');
	var minLengthText = Wzb.l('340') + minLen;
	var maxLengthText = Wzb.l('339') + maxLen;

	var metaData = mainStore.reader.jsonData['meta'];

	Ext.apply(Ext.form.VTypes, {
		passwordconfirm : function(val, field) {
			if (field.confirmTo) {
				var pwd = Ext.getCmp(field.confirmTo);
				return (val === pwd.getValue());
			}
			return true;
		}
	});

	var pForm = new Wzb.FormPanel({
		labelWidth : 320,
		standardSubmit : true,
		bodyStyle : Wzb.style_inner_space,
		border : false,
		waitMsgTarget : true,
		defaults : {
			width : 300
		},
		defaultType : 'textfield',
		labelAlign : 'right',
		items : [{
			fieldLabel : Wzb.l('535'),
			inputType : 'text',
			name : 'sUsrName',
			allowBlank : false,
			value : metaData['id'],
			readOnly : true,
			fieldClass : 'NullFieldText'
		}, {
			fieldLabel : Wzb.l('536'),
			id : 'usr_old_pwd_id',
			name : 'usr_old_pwd',
			inputType : 'password',
			allowBlank : false,
			blankText : blankText,
			minLength : minLen,
			minLengthText : minLengthText,
			maxLength : maxLen,
			maxLengthText : maxLengthText
		}, {
			fieldLabel : Wzb.l('537'),
			name : 'usr_new_pwd',
			id : 'newpwd1',
			inputType : 'password',
			allowBlank : false,
			blankText : blankText,
			minLength : minLen,
			minLengthText : minLengthText,
			maxLength : maxLen,
			maxLengthText : maxLengthText,
			regex : Wzb.reg_exp_text1,
			regexText : Wzb.l('336')
		}, {
			fieldLabel : Wzb.l('538'),
			name : 'confirm_usr_pwd',
			confirmTo : 'newpwd1',
			vtype : 'passwordconfirm',
			vtypeText : Wzb.l('333'),
			inputType : 'password',
			allowBlank : false,
			blankText : blankText,
			minLength : minLen,
			minLengthText : minLengthText,
			maxLength : maxLen,
			maxLengthText : maxLengthText
		}, {
			xtype : 'panel',
			border : false,
			width : '100%',
			bodyStyle : 'padding-left:325px',
			html : WzbHtm.required({
				1 : Wzb.l('335')
			})
		}, {
			xtype : 'hidden',
			name : 'cmd',
			value : 'upd_usr_pwd'
		}, {
			xtype : 'hidden',
			name : 'url_success',
			value : Wzb.getQdbUrl('cmd', 'go_home')
		}, {
			xtype : 'hidden',
			name : 'url_failure',
			value : self.location.href
		}, {
			xtype : 'hidden',
			name : 'usr_ent_id',
			value : metaData['ent_id']
		}],
		buttons : [{
			text : Wzb.l('329'),
			handler : function() {
				var frm = pForm.getForm();
				frm.getEl().dom.action = Wzb.uri_qdb;
				if (frm.isValid()) {
					frm.submit();
				}
			}
		}, {
			text : Wzb.l('330'),
			handler : Wzb.goHome,
			hidden : from_login_change
		}]
	});

	var main_container = new Wzb.Panel({
		title : Wzb.l('301'),
		width : Wzb.util_total_width,
		cls : 'bottom',
		items : [pForm]
	});
	main_container.render('container');
	Ext.getCmp('usr_old_pwd_id').focus();
	main_container.addClass('panel-maincontainer');
}

function getPwdMinLen(pwdData) {
	var len = 3;
	if (pwdData !== null) {
		len = Number(pwdData['min_length']);
	}
	return len;
}

function getPwdMaxLen(pwdData) {
	var len = 8;
	if (pwdData !== null) {
		len = Number(pwdData['max_length']);
	}
	return len;
}

function getPwdAttData(mainStore) {
	var pwdData = null;
	if (mainStore !== null) {
		var prof_attr_lst = mainStore.reader.jsonData['prof_attr_lst'];
		if (prof_attr_lst !== undefined) {
			for (var i = 0; i < prof_attr_lst.length; i++) {
				var curAttrLstData = prof_attr_lst[i]['attr_lst'];
				for (var j = 0; j < curAttrLstData.length; j++) {
					var curData = curAttrLstData[j];
					if (curData['name'] === 'password') {
						pwdData = curData;
						break;
					}
				}
			}
		}
	}
	return pwdData;
}
