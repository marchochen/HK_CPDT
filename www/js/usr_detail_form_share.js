var Usr = {
	comboXml : "comboBox",
	dateXml : "date",
	textXml : "text",
	chkboxXml : "checkBox",
	textareaXml : "textarea",
	textFieldWidth : 300,
	fieldSetWidth : 700,
	groupGradeLabelWidth : 157,
	userImgLabelWidth : 99,
	slotWidth : 12,
	photoWidth : 55,
	defaultTextLen : 255,
	textLen1 : 50,
	textLen2 : 500,
	textLen3 : 20,
	groupPersonal : "personal",
	groupContact : "contact",
	groupPosition : "position",
	groupOther : "other",
	pubNeverShow : "0",
	pubCustomedShow : "1",
	pubAlwaysShow : "2"
}

function showUserInfoWin(usr_ent_id) {
	var usrWin = new Wzb.Window({
		width : "80%",
		items : [
			getUserInfo(usr_ent_id)
		]
	});
	usrWin.show();
}

function getUserInfo(usr_ent_id) {
	var usrPan = new Wzb.Panel({
		bodyStyle : Wzb.style_inner_space,
		type:1,
		width : "100%",
		autoHeight : true
	});
	var storeObj = new Ext.data.JsonStore({
		url : Wzb.getDisUrl("module", "JsonMod.user.UserModule", "cmd", "get_usr_profile", "usr_ent_id", usr_ent_id)
	});
	storeObj.load({
		callback : function() {
			usrPan.add(getUserDetailShare(this, {isRead : true}));
			usrPan.doLayout();
		}
	});	
	return usrPan;
}

function getUserDetailShare(mainStore, config) {
	if(config.isUpd === undefined) {
		config.isUpd = false;
	}

	if(config.isReg === undefined) {
		config.isReg = false;
	}

	if(config.isRead === undefined) {
		config.isRead = false;
	}
	var usrPan;

	var valid = false;
	if((config.isUpd === true && config.isReg === false && config.isRead === false)
	  || (config.isUpd === false && config.isReg === true && config.isRead === false)
	  || (config.isUpd === false && config.isReg === false && config.isRead === true)) {
			valid = true;
	}
	if(valid === false) {
		usrPan = new Wzb.Panel({
			width : "100%",
			bodyStyle : Wzb.style_inner_space,
			html : Wzb.l("332")
		});
		return usrPan;
	}

	var userData = mainStore.reader.jsonData["user"];
	var attData = mainStore.reader.jsonData["prof_attr_lst"];

	usrPan = new Wzb.Panel({
		border : false,
		width : "100%"
	});
	
	if(config.isReg === true) {
		Ext.apply(Ext.form.VTypes,{
			passwordconfirm:function(val,field){
				if(field.confirmTo){
					var pwd = Ext.getCmp(field.confirmTo);
					return (val === pwd.getValue());
				}
				return true;
			}
		});
	}

	var user_template_tcr_infor = mainStore.reader.jsonData['user_choice_tcr_infor'];
	
	for (var i = 0; i < (attData.length + 1); i++) {//group circulation.
	    var curGroup = null;
	    if(i == attData.length){
			if(user_template_tcr_infor !== undefined && user_template_tcr_infor !== null){
				curGroup = user_template_tcr_infor;
			}else{
				break;
			}
		}else{
			curGroup = attData[i];
		}
		
		var fieldCnt = 0;
		if(config.isReg === true && curGroup["name"] === Usr.groupOther) {
			continue;
		}
		var infoPan = new Wzb.Panel({
			width : "80%",
			layout : "form",
			border : false,
			labelAlign : "right",
			defaults : {
				width : Usr.textFieldWidth
			}
		});
		var curFieldSet = new Ext.form.FieldSet({
			title : function() {
				var lab_name = "lab_prof_attr_grp_" + curGroup["name"];
				if(curGroup["name"] === "other" && config.isRead === true) {
					lab_name += "_read";
				}
				var str = Wzb.l(lab_name);
				return str;
			}(),
			width : "90%",
			autoHeight : true,
			border : true,
			items : [{
				layout : "column",
				border : false,
				items : [infoPan]
			}]
		});
		var optPan;//for checkbox option
		if(config.isUpd === true) {
			optPan = new Wzb.Panel({
				width : "20%",
				layout : "form",
				border : false,
				hideBorders : true
			});
			curFieldSet.items.get(0).add(optPan);
		}
		var curAttLst = curGroup["attr_lst"];
		for (var j = 0; j < curAttLst.length; j++) { //field circulation.
			var curItmData = curAttLst[j];
			var isPwd = false;
			if(config.isReg === true) {
				if(curItmData["registration"] === "false") {
					continue;
				}
			}
			var itm_name = curItmData["name"];
			if (itm_name === "extension_43") {//for user photo, need special process.
				infoPan.add(getUserImagePanel({needValue : true}, curItmData, userData, config));
			} else if(config.isReg === true && (itm_name === "group" || itm_name === "grade")) {//in registration page, the group and grade will be shown as goldenman.
				infoPan.add(getUsrAttribute(mainStore, curItmObj, curItmData, userData));
			} else {
				var curItmObj = {};
				curItmObj.needValue = true;
				curItmObj.itm_name = itm_name;
				curItmObj.org_type = curItmData["type"];
				if(itm_name === "password") {
					isPwd = true;
					curItmObj.id = "pwd1";
					curItmObj.inputType = "password";
				}
				if(config.isReg === true) {
					curItmObj.needValue = false;//needValue : wheather get the current user's value or not; in registration page never get the value.
				}
				var publicity = curItmData["publicity"];
				if (config.isRead === true) {
					if(isPwd === true) {
						continue;
					}
					curItmObj.readOnly = true;
					
					//the field will not be shown in read page with these conditions :
					//1. the field is setted as never shown in read page.(setted in usr_management.xml by publicity=0)
					//2. the field is setted as always shown in read page(publicity=2), and the field value is null.
					//3. (publicity=1), the field is setted as not public or otherwise the value is null.
					if(publicity === Usr.pubNeverShow
						|| (publicity === Usr.pubAlwaysShow && getFieldValue(curItmObj, curItmData, userData) === "")
						|| (publicity === Usr.pubCustomedShow && (getOptCheckedValue(curItmData, userData) === false || getFieldValue(curItmObj, curItmData, userData) === ""))) {
						continue;
					}
				} else if(config.isReg === true) {
					curItmObj.readOnly = false;
					if(curItmObj.itm_name === "user_id") {
						curItmObj.id = "user_id_id";//for focus in registration.
					}
				} else {
					if(isPwd === true) {
						continue;
					}
					curItmObj.readOnly = getReadOnly(curItmData["readonly"]);
				}
				fieldCnt++;

				if (curItmObj.readOnly === true) {
					curItmObj.fieldClass = "NullFieldText";
					curItmObj.xtype = "textfield";
				} else {
					curItmObj.xtype = getXtype(curItmObj.org_type);
				}
				curItmObj.required = false;
				curItmObj.fieldLabel = function() {
					var str = curItmData["label"];
					if(curItmObj.readOnly !== true && (curItmObj.itm_name === "user_id" || curItmObj.itm_name === "password" || curItmObj.itm_name === "name" || curItmObj.itm_name === "group" || curItmObj.itm_name === "grade")) {
						str = "* " + str;
						curItmObj.required = true;
					}
					return str;
				}();
				curItmObj.name = curItmData["fieldname"];
				if(curItmObj.org_type === Usr.dateXml) {
					curItmObj.name = curItmObj.name + "_date";//this name is used to display with the format Y-m-d
				}
				curItmObj.width = Usr.textFieldWidth;
				setCustomedProp(curItmObj, curItmData, userData, config);
				setInputInvalidChk(curItmObj, curItmData);

				infoPan.add(curItmObj);

				if(curItmObj.org_type === Usr.dateXml) {
					//this hidden is used to submit with the format Y-m-d H:i:s.u
					var dateHidObj = {};
					Ext.apply(dateHidObj, curItmObj);
					dateHidObj.xtype = "hidden";
					dateHidObj.name = curItmData["fieldname"];
					dateHidObj.value = getFieldValue(dateHidObj, curItmData, userData);
					infoPan.add(dateHidObj);
				}

				if(isPwd === true) {
					var confirmPwd = {};
					Ext.apply(confirmPwd, curItmObj);
					confirmPwd.id = "pwd2";
					confirmPwd.vtype = "passwordconfirm";
					confirmPwd.vtypeText = Wzb.l("333");
					confirmPwd.confirmTo = "pwd1";
					confirmPwd.name = "confirm_" + confirmPwd.name;
					confirmPwd.fieldLabel = confirmPwd.fieldLabel.slice(0, 1) + Wzb.l("334") + ' ' + confirmPwd.fieldLabel.slice(2);
					infoPan.add(confirmPwd);
				}
				
				if(config.isUpd === true) {
					var optObj = {};
					optObj.height = 25;
					if (publicity === Usr.pubCustomedShow) {
						optObj.xtype = "checkbox";
						optObj.name = "option_chk";
						optObj.inputValue  = curItmData["fieldname"];
						optObj.boxLabel = Wzb.l("290");
						optObj.hideLabel = true;
						optObj.checked = getOptCheckedValue(curItmData, userData);
					}
					optPan.add(optObj);
				}
			}
		}
		if(fieldCnt > 0) {
			usrPan.add(curFieldSet);
		}
	}
	if(config.isReg === true) {
		var req_msg = new Wzb.Panel({
			bodyStyle : Wzb.style_inner_space,
			border : false,
			html : "<span class='wzb-common-text'>" + Wzb.l("335") + "</span>"
		});
		usrPan.add(req_msg);
	}
	usrPan.doLayout();
	return usrPan;
}

function transformName(name) {
	var str = name;
	str = str.replace(/_\w{1}/g, function(c, index) {
		return str.charAt(index + 1).toUpperCase();
	});
	return str;
}

function setCustomedProp(itmObj, itmData, userData, config) {
	switch (itmObj.xtype) {
		case "textfield" :
			setTextFieldProp(itmObj, itmData, userData);
			break;
		case "combo" :
			setComboProp(itmObj, itmData, userData);
			break;
		case "datefield" :
			setDateFieldProp(itmObj, itmData, userData, config);
			break;
		case "checkbox" :
			//Sorry, not prepared.
			break;
		case "textarea" :
			setTextareaProp(itmObj, itmData, userData);
			break;
	}
}

function setTextFieldProp(itmObj, itmData, userData) {
	itmObj.value = getFieldValue(itmObj, itmData, userData);

	var orgType = itmData["type"];
	if (orgType === Usr.dateXml) {
		itmObj.value = Wzb.displayTime(itmObj.value);
	} else if (orgType === Usr.comboXml) {
		if(itmObj.value === "") {
			itmObj.value = Wzb.l("51");
		} else {
			for (var i = 0; i < itmData["option_lst"].length; i++) {
				var curOpt = itmData["option_lst"][i];
				if (curOpt["value"] === itmObj.value) {
					itmObj.value = curOpt["label"];
					break;
				}
			}
		}
	}
}

function setDateFieldProp(itmObj, itmData, userData, config) {
	itmObj.format = Wzb.time_format_ymd;
	itmObj.value = Wzb.displayTime(getFieldValue(itmObj, itmData, userData));
	itmObj.listeners = {
		change : function(obj, newValue, oldValue) {
			itmObj.value = (new Date(newValue)).format(Wzb.time_format_ymd) + " 00:00:00";
			var dateObj = eval(config.formId + "." + itmData["fieldname"]);
			dateObj.value = itmObj.value;
		}
	}
}

function setComboProp(itmObj, itmData, userData) {
	var comboStore = new Ext.data.SimpleStore({
		fields : ["value", "display"],
		data : function() {
			var comboData = new Array();
			comboData.push(["", Wzb.l("51")]);
			for (var i = 0; i < itmData["option_lst"].length; i++) {
				var curData = itmData["option_lst"][i];
				var selData = [curData["value"], curData["label"]];
				comboData.push(selData);
			}
			return comboData;
		}()
	});
	itmObj.hiddenName = itmData["fieldname"];
	itmObj.store = comboStore;
	itmObj.valueField = "value";
	itmObj.displayField = "display";
	itmObj.mode = "local";
	itmObj.triggerAction = "all";
	itmObj.editable = false;

	itmObj.value = getFieldValue(itmObj, itmData, userData);
}

function setTextareaProp(itmObj, itmData, userData) {
	itmObj.value = getFieldValue(itmObj, itmData, userData);
	itmObj.maxLength = 500;
}

function getXtype(type) {
	var str = "textfield";
	if (type === Usr.comboXml) {
		str = "combo";
	} else if (type === Usr.dateXml) {
		str = "datefield";
	} else if (type === Usr.chkboxXml) {
		str = "checkbox";
	} else if (type === Usr.textareaXml) {
		str = "textarea";
	}
	return str;
}

function getReadOnly(readonly) {
	var result = false;
	if (readonly === "true") {
		result = true;
	}
	return result;
}

function getFieldValue(itmObj, itmData, userData) {
	//do not set a default value such as "" or null. 
	//because if the field is not allow blank, the default value will triger the check blank event and then the field will show red.
	var val;
	if(itmObj.needValue === true) {
		var itmName = transformName(itmData["name"]);
		if (userData[itmName] !== undefined) {
			val = userData[itmName]["value"];
		}
	}
	return val;
}

function getOptCheckedValue(itmData, userData) {
	var chk = false;
	var itmName = transformName(itmData["name"]);
	if (userData[itmName] !== undefined) {
		chk = userData[itmName]["publicInd"];
	}
	return chk
}

function getUserImagePanel(itmObj, itmData, userData, config) {
	var panel = {
		xtype : "panel",
		layout : "column",
		width : Usr.fieldSetWidth,
		border : false,
		hideBorders : true,
		items : [{
			width : Usr.userImgLabelWidth,
			bodyStyle : "text-align:right",
			html : '<span class="wzb-common-text">' + itmData["label"] + ':</span>'
		}, {
			width : Usr.slotWidth
		}, {
			width : Usr.photoWidth,
			html : '<img class="wzb-user-icon" src=' + Wzb.getRelativeImagePath(getFieldValue(itmObj, itmData, userData)) + ' id="usr_photo" name="usr_photo_preview"/>'
		}]
	};
	if(config.isUpd === true) {
		function changeUserPhoto(radObj, checked) {
			if(checked === true) {
				var disabled = true;
				var img_src = "";
				if(radObj.id === "remain_photo") {
					disabled = true;
					img_src = Wzb.getRelativeImagePath(getFieldValue(itmObj, itmData, userData));
				} else if(radObj.id === "default_photo") {
					disabled = true;
					img_src = Wzb.getRelativeImagePath(Wzb.defalutUserPhoto);
				} else {
					disabled = false;
					img_src = "";
				}
				var fileObj = Ext.getCmp("file_photo_url");
				var imgObj = Ext.getDom("usr_photo");
				fileObj.setDisabled(disabled);
				if(img_src !== "") {
					imgObj.src = img_src;
				}
			}
		}

		var radPan = {
			width : "68%",
			layout : "form",
			defaults : {
				hideLabel : true,
				xtype : "radio",
				name : "image_radio",
				listeners : {
					check : changeUserPhoto
				}
			},
			items : [{
				boxLabel : Wzb.l("294"),
				id : "remain_photo",
				inputValue : "remain_photo",
				checked : true
			}, {
				boxLabel : Wzb.l("295"),
				inputValue : "default_photo",
				id : "default_photo"
			}, {
				boxLabel : Wzb.l("296"),
				id : "file_photo"
			}, {
				xtype : "textfield",
				inputType : "file",
				name : "usr_photo_param",
				disabled : true,
				id : "file_photo_url"
			}]
		};
		panel.items.push(radPan);

	}
	return panel;
}

function getUsrAttribute(mainStore, curItmObj, itmData, userData) {
	var panel = {
		xtype : "panel",
		id : itmData["name"],
		layout : "column",
		width : Usr.fieldSetWidth,
		border : false,
		hideBorders : true,
		items : [{
			width : Usr.groupGradeLabelWidth,
			bodyStyle : "text-align:right",
			html : '<span class="wzb-common-text">* ' + itmData["label"] + ':</span>'
		}, {
			width : Usr.slotWidth
		}]
	};
	var dataPan = getAttributeGoldenman(mainStore, "goldenman_reg_" + itmData["name"]);
	panel.items.push(dataPan);
	return panel;	
}

function getAttributeGoldenman(store, gldManName) {
	var gldHtml = Wzb.goldenMan.getGoldenManHtml(store, gldManName);
	var p = {
		xtype : "panel",
		border : false,
		html : gldHtml.replace(/\r\n/g, ""),
		listeners : {
			"render" : function() {
				Wzb.goldenMan.applyJsFunction(gldHtml);
			}
		}
	};
	return p;
}

function getMaxLength(itmObj) {
	var len = Usr.defaultTextLen;
	if(itmObj.org_type === Usr.textXml) {
		if(itmObj.itm_name === "extension_41" || itmObj.itm_name === "extension_42") {
			len = Usr.textLen2;
		} else if(itmObj.itm_name === "phone" || itmObj.itm_name === "fax") {
			len = Usr.textLen1;
		} else if(itmObj.itm_name === "nickname") {
			len = Usr.textLen3;
		}
	} else if(itmObj.org_type === Usr.textareaXml) {
		len = Usr.textLen2;
	}
	return len;
}

function setInputInvalidChk(itmObj, itmData) {
	if(itmObj.readOnly !== true && (itmObj.xtype === "textfield" || itmObj.xtype === "textarea")) {
		if(itmObj.name === "usr_id" || itmObj.name === "usr_pwd") {
			itmObj.regex = Wzb.reg_exp_text1;
			itmObj.regexText = Wzb.l("336");
		} else if(itmObj.name === "usr_email" || itmObj.name === "urx_extra_41") {
			itmObj.vtype = "email";
			itmObj.vtypeText = Wzb.l("337");
		}
		if(itmObj.required === true) {
			itmObj.allowBlank = false;
			itmObj.blankText = Wzb.l("338")
		}
		if(itmData["max_length"] !== undefined) {
			itmObj.maxLength = itmData["max_length"];
		} else {
			itmObj.maxLength = getMaxLength(itmObj);
		}
		itmObj.maxLengthText = Wzb.l("339") + itmObj.maxLength;
		if(itmData["min_length"] !== undefined) {
			itmObj.minLength = itmData["min_length"];
			itmObj.minLengthText = Wzb.l("340") + itmObj.minLength;
		}
	} else if(itmObj.xtype === "datefield") {
		 itmObj.invalidText = Wzb.l("341") + ": " + "YYYY-MM-DD";
	}
}
