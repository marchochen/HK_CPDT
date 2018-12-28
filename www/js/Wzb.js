function StoreCallback(conn, response, options) {

	var header_name_location = 'Wzb-Location';
	if (response.getAllResponseHeaders.indexOf(header_name_location) > -1) {
		window.location.href = getResponseHeaderStr(
				response.getAllResponseHeaders, header_name_location);
	}

	var header_name_sysmsg = 'Wzb-System-Message';
	if (response.getAllResponseHeaders.indexOf(header_name_sysmsg) > -1) {
		var response_json = eval('(' + response.responseText + ')');
		if (response_json) {
			var err_msg_data = response_json['errMsg'];
			if (err_msg_data !== undefined) {
				Wzb.ext_ajax_request_complete_success = false;
				Ext.util.Observable.prototype.purgeListeners();
				var win = new Wzb.Window({
					title : function() {
						if (err_msg_data['status'] !== undefined 
								&& err_msg_data['status'] !== '') {
							str = response_json.label[err_msg_data['status']]
						} else {
							str = response_json.label['STATUS'];
						}
						return str;
					}(),
					height : 200,
					width : 500,
					layout : 'fit',
					html : err_msg_data['message'],
					buttons : [{
						xtype : 'button',
						text : response_json.label['OK'],
						handler : function() {
							var url = window.location.href;
							if (err_msg_data['url'] !== undefined
									&& err_msg_data['url'] !== '') {
								url = err_msg_data['url'];
							}
							window.location.href = url;
						}
					}]
				});
				win.show();
			}
		}
	}

	var show_json = Wzb.getUrlParam('json');
	if (show_json && show_json === 'y') {
		var t_obj = document.getElementById('json_textarea');
		if (t_obj === null) {
			t_obj = document.createElement('textarea');
			t_obj.id = 'json_textarea';
			t_obj.style.width = '100%';
			t_obj.style.height = '300px';
			document.body.appendChild(t_obj);
		}
		t_obj.value = t_obj.value + '\n----------' + options.url + '----------\n'
				+ response.responseText;
	}
}

function exceptionHandler(conn, response, options) {
	var html_str = '';
	var is_session_timeout = false;
	var header_name_timeout = 'Wzb-Session-Timeout';
	var header_name_reloginurl = 'Wzb-Relogin-Url';
	var header_value_reloginurl = getResponseHeaderStr(
			response.getAllResponseHeaders, header_name_reloginurl);
	var win_title = 'Error loading content';
	var win_button = 'Retry';
	if (response.getAllResponseHeaders.indexOf(header_name_timeout) > -1) {
		is_session_timeout = true;
		html_str = decodeURIComponent(getResponseHeaderStr(
				response.getAllResponseHeaders, header_name_timeout));
		html_str = html_str.replace(/\+/g, ' ');
		html_str = '<div style="padding-top:50px;text-align:center;"><img src="../wb_image/ico_warning.gif" align="absmiddle">' + html_str + '</div>';
		var header_name_title = 'Wzb-Message-Title';
		var header_name_button = 'Wzb-Message-Button';
		win_title = decodeURIComponent(getResponseHeaderStr(response.getAllResponseHeaders, header_name_title));
		win_button = decodeURIComponent(getResponseHeaderStr(response.getAllResponseHeaders, header_name_button));
	} else {
		html_str = 'Error Message:<br/>';
		for (var a in response) {
			if (response.hasOwnProperty(a)) {
				html_str = html_str + a + ':' + '<pre>' + response[a] + '</pre>';
			}
		}
	}
	var btnId = 'okbutton';
	var win = new Wzb.Window({
		title : win_title,
		height : 200,
		width : 500,
		html : html_str,
		defaultButton : btnId,
		buttons : [{
			xtype : 'button',
			id : btnId,
			text : win_button,
			handler : function() {
				if (is_session_timeout === true) {
					window.location.href = header_value_reloginurl;
				} else {
					conn.request(options);
				}
				win.close();
			}
		}]
	});
	win.show();
}

function getResponseHeaderStr(response_headers, response_header_name) {
	var response_header_value;
	var header_arr = response_headers.split('\n');
	for (var i = 0; i < header_arr.length; i++) {
		var timeout_index = header_arr[i].indexOf(response_header_name);
		if (timeout_index > -1) {
			response_header_value = header_arr[i].substring(timeout_index
					+ response_header_name.length + 1, header_arr[i].length)
			break;
		}
	}
	return response_header_value;
}

Wzb = {
	name : 'wizBank',
	version : '5.2',
	img_path : '../wb_image/',
	the_me_img_path : '',
	relative_path : '../',
	skin_style_id : 'skinstylecss',
	ext_ajax_request_complete_success : true,
	time_format_ymd : 'Y-m-d',
	time_format_ymdhm : 'Y-m-d H:i',
	time_format_ymdhms : 'Y-m-d H:i:s',
	open_win_height : 700,
	open_win_width : 1024,
	util_total_width : 974,
	util_total_width_all : 984,
	// Those two parameters will be initialed in Wzb.PageRenderer
	util_col_width1 : 0,
	util_col_width2 : 0,
	min_timestamp : '1753-01-01 00:00:00.000',
	max_timestamp : '9999-12-31 23:59:59.000',
	util_item_type_classroom : 'CLASSROOM',
	util_item_type_selfstudy : 'SELFSTUDY',
	util_item_type_blended : 'BLENDED',
	util_item_type_exam : 'EXAM',
	util_item_type_book : 'BOOK',
	util_item_type_website : 'WEBSITE',
	util_item_type_audiovideo : 'AUDIOVIDEO',
	util_item_type_integrated : 'INTEGRATED',
	status_ng : 0,
	status_up : 1,
	status_ok : 2,
	reg_exp_text1: /^[a-z0-9_-]*$/,
	has_my_team_view : false,
	has_material_center_view : false,
	has_lcms_read : false,
	checkBrowserName : function (name) {
	    var ver_str = navigator.appVersion;
	    var ver_no = 0;
	    var result = false;
	    if (ver_str.indexOf(name) != -1) {
	        temp_str = ver_str.split(name);
	        ver_no = parseFloat(temp_str[1]);
	        if (ver_no >= 6) {
	            result = true;
	        }
	    }
	    return result;
	},
	getBrowserStatus : function () {
	    var status = Wzb.status_ng;
	    if (!Wzb.checkBrowserName(' MSIE ') || !Wzb.checkBrowserName('MSIE ') || !Wzb.checkBrowserName('MSIE')) {
	        status = Wzb.status_ng;
	    } else {
	        status = Wzb.status_ok;
	    }
	    return status;
	},
	getCookieStatus : function () {
	    var status = Wzb.status_ng;
	    var cookie_str = 'wb_check=kcehc_bw';
	    document.cookie = cookie_str;
	    if (document.cookie.indexOf(cookie_str) > -1) {
	        status = Wzb.status_ok;
	        var date = new Date();
	        date.setTime(date.getTime() - 1000);
	        document.cookie = cookie_str + '; expires=' + date.toGMTString();
	    }
	    return status;
	},
	getJreStatus : function() {
	    var status = Wzb.status_ng;
        var jres = deployJava.getJREs();
        for(var i = 0; i < jres.length; i++){
            var jTem = jres[i].substring(0,3);
            if(jTem >= 1.5){
                status = Wzb.status_ok;
                break;
            }
        }
        return status;
	},

	getFlashStatus : function () {
	    var MinVer = 7;
        var status = Wzb.status_ng;
        if (navigator.plugins && navigator.plugins.length && navigator.plugins.length > 0) {
            var flashObj = navigator.plugins["Shockwave Flash"];
            if (flashObj && flashObj.length && flashObj.length > 0) {
                var flashMimeObj = flashObj["application/x-shockwave-flash"];
                if (flashMimeObj) {
                    var tempStr = flashObj.description.split(" Flash ");
                    var verNo = parseFloat(tempStr[1]);
                    if (verNo >= MinVer) {
                        status = Wzb.status_ok;
                    } else {
                        status = Wzb.status_up;
                    }
                }
            }
        }
        if (status == Wzb.status_ng) {
            for (var i = MinVer; i > 0; i--) {
                try {
                    var flashObj = new ActiveXObject("ShockwaveFlash.ShockwaveFlash." + i);
                    if (i == MinVer) {
                        status = Wzb.status_ok;
                    } else {
                        status = Wzb.status_up;
                    }
                    break;
                } catch(e) {
                    status = Wzb.status_ng;
                }
            }
        }
        return status;
	},
	displayTime : function(input, format) {
		if (format === undefined || format === '') {
			format = Wzb.time_format_ymd;
		}
		var dis_time;
		if (input !== undefined && input !== '') {
			input = Wzb.formatTimeStr(input);
			var dt = new Date();
			dt = Date.parseDate(input, 'Y-m-d H:i:s.u');
			dis_time = dt.format(format);
		} else {
			dis_time = '';
		}
		return dis_time;
	},
	displayTimeDefValue : function(input, format, def_value) {
		var dis_time = Wzb.displayTime(input, format);
		if (dis_time === undefined || dis_time === '') {
			dis_time = def_value;
		}
		return dis_time;
	},
	uri_qdb : '../servlet/qdbAction',
	uri_ae : '../servlet/aeAction',
	uri_dis : '../servlet/Dispatcher',
	nav_path_sep : '<span class="wzb-path-separator-text">&gt;</span>',
	link_sep : '<span class="wzb-link-separator-text">|</span>',
	header_link_sep : '<span class="wzb-header-link-separator-text">|</span>',
	style_inner_space : 'padding:5px;',
	style_tab_header_text : '10pt',
	style_width1 : '950', // 知道搜索结果，在firefox下不能撑100%，所以只能写死宽度
	getServletUrl : function(uri, argv) {
		var url = uri;
		for (i = 0; i < argv.length; i = i + 2) {
			var sep = '&';
			if (i === 0) {
				sep = '?';
			}
			url += sep + encodeURIComponent(argv[i]) + '='
					+ encodeURIComponent(argv[i + 1]);
		}
		return url;
	},
	getQdbUrl : function() {
		return this.getServletUrl(this.uri_qdb, arguments);
	},
	getAeUrl : function() {
		return this.getServletUrl(this.uri_ae, arguments);
	},
	getDisUrl : function() {
		return this.getServletUrl(this.uri_dis, arguments);
	},
	format : function(div_id, replace_obj) {
		var divInnerHTML = document.getElementById(div_id).innerHTML.trim();
		if (Ext.isGecko) {
			return divInnerHTML.replace(/(\{\$(\w+)\}|%7B\$(\w+)%7D)/g,
					function(f_str, f_no) {
						if (f_str.indexOf('%7B') === 0) {
							return replace_obj[f_str.slice(4, f_str.length - 3)]
						} else {
							return replace_obj[f_str.slice(2, f_str.length - 1)]
						}
					})
		} else {
			return divInnerHTML.replace(/\{\$(\w+)\}/g, function(f_str, f_no) {
				return replace_obj[f_str.slice(2, f_str.length - 1)]
			})
		}
	},
	getPeriodAfterStore : function() {
		var period_after2 = [[Wzb.l('71'), 'IMMEDIATE'],
				[Wzb.l('648'), 'LAST_1_WEEK'],
				[Wzb.l('649'), 'LAST_2_WEEK'],
				[Wzb.l('650'), 'LAST_1_MONTH'],
				[Wzb.l('651'), 'LAST_2_MONTH'],
				[Wzb.l('385'), 'UNLIMITED']];

		var simple_store = new Ext.data.SimpleStore({
			fields : ['display', 'value'],
			data : period_after2
		});
		return simple_store;
	},
	getPeriodBeforeStore : function() {
		var period_before = [[Wzb.l('647'), 'IMMEDIATE'],
				[Wzb.l('652'), 'LAST_1_WEEK'],
				[Wzb.l('653'), 'LAST_2_WEEK'],
				[Wzb.l('654'), 'LAST_1_MONTH'],
				[Wzb.l('655'), 'LAST_2_MONTH'],
				[Wzb.l('76'), 'UNLIMITED']];

		var simple_store = new Ext.data.SimpleStore({
			fields : ['display', 'value'],
			data : period_before
		});
		return simple_store;
	},

	colString : {
		align : 'left'
	},
	colNumber : {
		align : 'right'
	},
	colStatus : {
		align : 'center'
	},
	colDate : {
		renderer : function(value) {
			return Wzb.displayTime(value);
		}
	},
	colDateTime : {
		renderer : function(value) {
			return Wzb.displayTime(value, this.time_format_ymdhms);
		}
	},
	goldenMan : {
		getGoldenManHtml : function(store, gldManName) {
			var result = '';
			if (store != null && store.reader != null
					&& store.reader.jsonData != null) {
				var gld_obj_arr = store.reader.jsonData['goldenman'];
				for (var i = 0; i < gld_obj_arr.length; i++) {
					gld_obj = gld_obj_arr[i];
					if (gld_obj['name'] === gldManName) {
						result = gld_obj['value'];
						break;
					}
				}
			}
			return result;
		},
		applyJsFunction : function(html_str) {
			var script_start = '<SCRIPT';
			var script_end = 'SCRIPT>';
			var js_str = html_str.substring(html_str.indexOf(script_start) + 53,
					html_str.indexOf(script_end) - 2)
			var el = document.createElement('script');
			el.text = js_str;
			document.body.appendChild(el);
		},
		setOptions : function(store, obj, gld_name) {
			if (store != null && store.reader != null
					&& store.reader.jsonData != null) {
				var gld_obj_arr = store.reader.jsonData['goldenmanoptions'];
				if (gld_obj_arr !== undefined) {
					for (var i = 0; i < gld_obj_arr.length; i++) {
						gld_obj = gld_obj_arr[i];
						if (gld_obj !== null && gld_obj['name'] === gld_name) {
							var opt_value_arr = gld_obj['value'];
							for (var j = 0; j < opt_value_arr.length; j++) {
								obj.options.add(new Option(
										opt_value_arr[j]['text'],
										opt_value_arr[j]['id']));
							}
							break;
						}
					}
				}
			}

		}
	},
	l : function(lab_name, datas) {
		var result = '!!!' + lab_name;
		// the label object is defined in
		// label_zh-cn.js/label_en-us.js/label_zh-hk.js
		if (page_label.hasOwnProperty(lab_name)) {
			var count = 0;
			result = page_label[lab_name].replace(/\$data/g, function(str,
					index) {
				if (datas === undefined || datas === null) {
					return result;
				}
				return datas[count++];
			});
		}
		return result;
	},
	unescapeHtmlLineFeed : function(content) {
		var val = '';
		if (content !== null && content !== undefined) {
			val = content.replace(/\n/g, '<br>');
		}
		return val;
	},
	home_url : '../htm/home.htm',
	goHome : function() {
		window.location.href = Wzb.home_url;
	},
	login_url : '../login/index.htm',
	getEmptyText : function() {
		return Wzb.l('657');
	},
	componentAddClass : function(component) {
		if (component.type === undefined || component.type === null) {
			component.type = 1;
		}
		if (component.type === 1) {
			component.body.addClass('wzb-panel-body-1');
			if (component.header !== undefined && component.header !== false) {
				component.header.addClass('wzb-panel-header-1');
			}
		} else if (component.type === 2) {
			component.body.addClass('wzb-panel-body-2');
			if (component.header !== undefined && component.header !== false) {
				component.header.addClass('wzb-panel-header-2');
			}
		} else if (component.type === 3) {
			component.body.addClass('wzb-panel-body-3');
			if (component.header !== undefined && component.header !== false) {
				component.header.addClass('wzb-panel-header-3');
			}
		} else if (component.type === 4) {
			component.body.addClass('wzb-panel-body-4');
			if (component.header !== undefined && component.header !== false) {
				component.header.addClass('wzb-panel-header-4');
			}
		} else if (component.type === 5) {
			component.body.addClass('wzb-panel-body-5');
			if (component.header !== undefined && component.header !== false) {
				component.header.addClass('wzb-panel-header-5');
			}
		} else if (component.type === 6) {
			component.body.addClass('wzb-panel-body-6');
			if (component.header !== undefined && component.header !== false) {
				component.header.addClass('wzb-panel-header-6');
			}
		}

		var tools = component.tools;
		for(var t in tools) {
			tools[t].addClass('wzb-tool-' + component.type);
			tools[t].addClass('wzb-tool-' + t);
			tools[t].addClassOnOver('wzb-tool-' + t + '-over');
		}

		if (component.getBottomToolbar() !== undefined) {
			component.getBottomToolbar().getEl().parent()
					.addClass('wzb-toolbar-border-' + component.type);
			component.getBottomToolbar().getEl().parent()
					.addClass('wzb-no-border-top');
			component.getBottomToolbar().getEl()
					.addClass('wzb-toolbar-background-' + component.type);
		}
		if (component.getTopToolbar() !== undefined) {
			component.getTopToolbar().getEl().parent()
					.addClass('wzb-toolbar-border-0');
			component.getTopToolbar().getEl().parent()
					.addClass('wzb-no-border-bottom');
			component.getTopToolbar().getEl()
					.addClass('wzb-toolbar-background-0');
		}
		
		if(component.title === undefined && (component.border === true || component.border === undefined)) {
			component.body.addClass('wzb-panel-body-line');
		}
		
	},
	Panel : Ext.extend(Ext.Panel, {
		listeners : {
			render : function(component) {
				Wzb.componentAddClass(component);
			}
		}
	}),
	TabPanel : Ext.extend(Ext.TabPanel, {
		listeners : {
			render : function() {
				var lis = this.stripWrap.dom.getElementsByTagName('li');
				for (var i = 0, len = lis.length - 1; i < len; i++) {
					var li = lis[i];
					var title_obj = li.childNodes[1].firstChild.firstChild.firstChild;
					title_obj.style.fontSize = Wzb.style_tab_header_text;
				}
			}
		}
	}),
	Window : Ext.extend(Ext.Window, {
		cls : 'wzb-window',
		buttonAlign : 'center',
		autoScroll : true,
		maximizable : true,
		modal : true,
		listeners : {
			beforeshow : function(component) {
				component.body.addClass('wzb-window-body-0');
			}
		}
	}),
	TreePanel : Ext.extend(Ext.tree.TreePanel, {}),
	GridPanel : Ext.extend(Ext.grid.GridPanel, {
		disableSelection : true,
		trackMouseOver : false,
		enableHdMenu : false,
		enableColumnMove : false,
		enableColumnResize : false,
		autoHeight : true,
		listeners : {
			render : function(component) {
				Wzb.componentAddClass(component);
			}
		}
	}),
	FormPanel : Ext.extend(Ext.form.FormPanel, {
		listeners : {
			render : function(component) {
				Wzb.componentAddClass(component);
			}
		}
	}),
	ComboBox : Ext.extend(Ext.form.ComboBox, {
		editable : false,
		listClass : 'wzb-pull-down-menu'
	}),
	Menu : Ext.extend(Ext.menu.Menu, {
		createEl : function() {
			var style = 'x-menu';
			if (this.wzbStyle != undefined && this.wzbStyle != '') {
				style += ' ' + this.wzbStyle;
			}
			return new Ext.Layer({
				cls : style,
				shadow : this.shadow,
				constrain : false,
				parentEl : this.parentEl || document.body,
				zindex : 15000
			});
		}
	}),
	/*
	 * 得到一个按钮控件(HTML代码)
	 */
	getButton : function(handler, text) {
		if (handler === undefined) {
			handler = '';
		}
		var html = '<input type="button"' + ' class="wzb-button-out"'
				+ ' onmouseout="this.className=\'wzb-button-out\'"'
				+ ' onmouseover="this.className=\'wzb-button-over\'"'
				+ ' onclick="' + handler + '"' + ' value="' + text + '"/>';
		return html;
	},
	getFolderImagePath : function() {
		return Wzb.getCommonImagePath('tree/images/default/folder.png');
	},
	/*
	 * layout configuration
	 */
	layout : {
		TYPE1 : '1:2:1',
		TYPE2 : '1:2',
		TYPE3 : '2:1',
		TYPE4 : '1',
		configs : {
			'1:2:1' : {
				total : 974,
				col_width : [200, 560, 200]
			},
			'1:2' : {
				total_width : 974,
				col_width : [290, 674]
			},
			'2:1' : {
				total_width : 974,
				col_width : [674, 290]
			},
			'1' : {
				total_width : 974,
				col_width : [974]
			}
		}
	},
	/*
	 * 判断文件格式是否符合
	 */
	validateFileType : function(path_name, types) {
		var ret = false;
		if (path_name !== undefined && path_name.length > 0) {
			if (types === undefined || types === '' || !types.length) {
				types = ['jpg', 'gif', 'png'];
			}

			var str = '';
			var len = types.length;
			for (var i = 0; i < len; i++) {
				if (path_name.substring(path_name.lastIndexOf('.') + 1)
						.toLowerCase() === types[i]) {
					ret = true;
				}
				str += types[i];
				if (i !== len - 1) {
					str += ', ';
				}
			}
			if (ret === false) {
				alert(Wzb.l('656') + ': ' + str);
			}
		} else {
			alert(Wzb.l('496'));
		}
		return ret;
	},
	/*
	 * 退出系统
	 */
	doLogout : function() {
		if (confirm(Wzb.l('625'))) {
			url = Wzb.getQdbUrl('cmd', 'logout');
			self.location.href = url;
		}
	},
	/*
	 * 取得一个分页控件
	 */
	getPagingToolbar : function(store, type, pagesize) {

		if (type === undefined || type === '') {
			type = 1;
		}
		if (!pagesize || pagesize == '' || pagesize == 0) {
			pagesize = 10;
		}
		var page_size_array = [[pagesize, pagesize + Wzb.l('660')],
				[2 * pagesize, 2 * pagesize + Wzb.l('660')],
				[4 * pagesize, 4 * pagesize + Wzb.l('660')]];
		var toolbar_obj = {
			pageSize : pagesize,
			store : store,
			beforePageText : Wzb.l('661'),
			afterPageText : Wzb.l('662'),
			firstText : Wzb.l('659'),
			prevText : Wzb.l('663'),
			nextText : Wzb.l('664'),
			lastText : Wzb.l('665'),
			refreshText : Wzb.l('666')
		}

		switch (type) {
			case 1 :
				function comboResize(combobox, record, indexn) {
					loadPageData(parseInt(combobox.getValue()));
				}
				;
				var combo = new Wzb.ComboBox({
					store : new Ext.data.SimpleStore({
						fields : ['pagesize', 'pagesizeshow'],
						data : page_size_array
					}),
					valueField : 'pagesize',
					displayField : 'pagesizeshow',
					mode : 'local',
					triggerAction : 'all',
					value : pagesize + Wzb.l('660'),
					width : 85,
					editable : false,
					selectOnFocus : true
				});
				combo.on('select', comboResize);
				Ext.apply(toolbar_obj, {
					displayInfo : true,
					displayMsg : Wzb.l('667'),
					emptyMsg : '',
					items : ['-', combo]
				});
				break;
			case 2 :
				function btnResize(menuItem) {
					loadPageData(menuItem.value);
				}
				;
				var menu = new Wzb.Menu({
					items : [{
						text : page_size_array[0][1],
						checked : pagesize == page_size_array[0][0],
						group : 'pagesize',
						value : page_size_array[0][0],
						handler : btnResize
					}, {
						text : page_size_array[1][1],
						checked : pagesize == page_size_array[1][0],
						group : 'pagesize',
						value : page_size_array[1][0],
						handler : btnResize
					}, {
						text : page_size_array[2][1],
						checked : pagesize == page_size_array[2][0],
						group : 'pagesize',
						value : page_size_array[2][0],
						handler : btnResize
					}]
				})
				var btn = new Ext.Button({
					menu : menu
				});
				Ext.apply(toolbar_obj, {
					items : ['->', btn]
				});
				break;
			case 3 :
				Ext.apply(toolbar_obj, {
					listeners : {
						'render' : function(obj) {
							obj.loading.destroy();
						}
					}
				});
				break;
		}
		var bar = new Ext.PagingToolbar(toolbar_obj);
		function loadPageData(limit) {
			bar.pageSize = limit;
			store.load({
				params : {
					start : 0,
					limit : limit
				}
			});
		}
		return bar;
	},
	/*
	 * 打开一个新窗口
	 */
	openWin : function(win_url, win_name, is_full_screen, str_feature) {
		var win_obj = null;
		if (str_feature === undefined || str_feature === null
				|| str_feature.length === 0) {
			// use default window feature
			str_feature = 'location=0,menubar=0,resizable=1,scrollbars=1,status=1,toolbar=0';
		}
		win_obj = window.open(win_url, win_name, str_feature);
		if (is_full_screen) {
			win_obj.moveTo(0, 0);
			win_obj.resizeTo(screen.availWidth, screen.availHeight);
		}
		win_obj.focus();
	},
	/*
	 * 清空容器panel中的所以子容器
	 */
	clearPanel : function(panel) {
		if (panel.getComponent(0) != null) {
			panel.remove(panel.getComponent(0));
			Wzb.clearPanel(panel);
		} else {
			return;
		}
	},
	/*
	 * 取得课程的图示：未设置itm_icon的话则根据itm_dummy_type取得默认图示
	 */
	getCourseImage : function(itm_icon, itm_dummy_type) {
		var itm_dummy_type_delimiter = '_';
		var path = '';
		if (itm_icon !== undefined && itm_icon !== '') {
			path = '<img class="wzb-cos-icon" src="'
					+ Wzb.getRelativeImagePath(itm_icon) + '"/>';
		} else if (itm_dummy_type) {
			var typsBlocks = itm_dummy_type.split('|-|');
			var className;
			if (typsBlocks[0] === this.util_item_type_classroom) {
				if (typsBlocks[1] === 'BLEND') {
					if (typsBlocks[2] === 'COS') {
						className = 'wzb-cos-type-1';
					} else if (typsBlocks[2] === 'EXAM') {
						className = 'wzb-cos-type-2';
					}
				} else if (typsBlocks[1] === 'COS') {
					className = 'wzb-cos-type-3';
				} else if (typsBlocks[1] === 'EXAM') {
					className = 'wzb-cos-type-4';
				}
			} else if (typsBlocks[0] === this.util_item_type_selfstudy) {
				if (typsBlocks[1] === 'COS') {
					className = 'wzb-cos-type-5';
				} else if (typsBlocks[1] === 'EXAM') {
					className = 'wzb-cos-type-6';
				}
			} else if (typsBlocks[0] === this.util_item_type_book) {
				className = 'wzb-cos-type-7';
			} else if (typsBlocks[0] === this.util_item_type_website) {
				className = 'wzb-cos-type-8';
			} else if (typsBlocks[0] === this.util_item_type_audiovideo) {
				className = 'wzb-cos-type-9';
			} else if (typsBlocks[0] === this.util_item_type_integrated) {
				className = 'wzb-cos-type-10';
			}
			path = '<div class="' + className + '">&nbsp;</div>';
		}
		return path;
	},
	/*
	 * 培训分数的格式
	 */
	displayScore : function(score) {
		var result = 0;
		if (0.1 > score && score > -0.1) {
			result = 0;
		} else if (score > 0) {
			result = score - 0.05;
		} else if (score < 0) {
			result = score + 0.05;
		}
		result = result.toFixed(1);
		return result;
	},
	/*
	 * 切换语言
	 */
	changeLang : function(encoding, url_success) {
		if (url_success === undefined) {
			url_success = location.href;
		}
		var url = Wzb.getDisUrl('module', 'JsonMod.user.UserModule', 'cmd',
				'change_lang', 'lang', encoding, 'url_success', url_success);
		location.href = url;
	},
	/*
	 * 切换角色
	 */
	changeRole : function(combo, record, index) {
		location.href = Wzb.getQdbUrl('cmd', 'change_role', 'rol_ext_id',
				record.data.optionId);
	},
	getHtmImgSrc : function(img_src) {
		return 'src="' + img_src + '"';
	},
	/*
	 * 从win的url中取得参数
	 */

	getRelativeImagePath : function(img_name) {
		return Wzb.genImagePath(img_name, Wzb.relative_path);
	},
	getUrlParam : function(name, win) {
		if (name != null && name != '') {
			name = '&' + name;
		}
		if (win == null) {
			str_param = window.location.search;
		} else {
			str_param = eval(win + '.location.search');
		}

		if (str_param != null && str_param != '') {
			str_param = '&' + str_param.substring(1, str_param.length);
		}

		idx1 = str_param.indexOf(name + '=')

		if (idx1 == -1) {
			return '';
		}

		idx1 = idx1 + name.length + 1
		idx2 = str_param.indexOf('&', idx1)

		if (idx2 != -1) {
			len = idx2 - idx1;
		} else {
			len = str_param.length;
		}
		return decodeURIComponent(str_param.substr(idx1, len));
	},
	/*
	 * 添加/设置参数到url中
	 */
	setUrlParam : function(name, value, url) {
		if (url) {
			idx0 = url.indexOf('?')
			if (idx0 == -1) {
				str_param = ''
			} else {
				str_param = url.substr(idx0, url.length)
			}
		} else {
			str_param = window.location.search
		}

		idx1 = str_param.indexOf(name + '=')
		if (idx1 == -1) {
			if (str_param == '') {
				str_param = '?' + name + '=' + value
			} else {
				str_param = str_param + '&' + name + '=' + value
			}
		} else {
			idx1 = idx1 + name.length + 1
			idx2 = str_param.indexOf('&', idx1)
			if (idx2 == -1) {
				str_param = str_param.substr(0, idx1) + value
			} else {
				suffx = str_param.substr(idx2, str_param.length)
				str_param = str_param.substr(0, idx1) + value + suffx
			}
		}

		if (url) {
			if (idx0 == -1) {
				return url + str_param
			} else {
				return url.substr(0, idx0) + str_param
			}
		} else {
			return window.location.pathname + str_param
		}
	},
	/*
	 * 当前页面打开链接
	 */
	handMenuBtn : function(url) {
		if (url && url != '') {
			window.location.href = url;
		}
	},
	/*
	 * 页面footer
	 */
	renderFooter : function(main_store) {
		var foot_htm = '<table width="' + Wzb.util_total_width_all
				+ '" border="0" cellspacing="0" cellpadding="0" class="wzb-footer">';
		
		var meta_data = main_store.reader.jsonData['meta'];
		var sys_user_info = main_store.reader.jsonData['meta'] === undefined ? null : main_store.reader.jsonData['meta']['sys_user_info'];
		if(sys_user_info !== undefined && (sys_user_info.tel !== undefined || sys_user_info.email !== undefined)) {
			foot_htm += '<tr><td>';
			if(sys_user_info.tel !== undefined) {
				foot_htm += Wzb.l('lab_phone') + ': ' + sys_user_info.tel + ' ';
			}
			if(sys_user_info.email !== undefined) {
				foot_htm += 'Email: ' + sys_user_info.email;
			}
			foot_htm += '</td></tr>';
		}
		
		foot_htm += '<tr><td>' + Wzb.name + ' ' + Wzb.version
				+ '</td></tr></table>'
		var footer_panel = new Wzb.Panel({
			border : false,
			width : Wzb.util_total_width_all,
			html : foot_htm
		});
		footer_panel.render('container');
		return footer_panel;
	},
	getHeaderPanel : function(main_store, is_register) {
		var meta_data = main_store.reader.jsonData['meta'];
		var usr_html = '';
		var color_html = '&nbsp;';
		var lang_html = '';
		var search_arr = [{}];

		if (!is_register || is_register == false) {
			// 用户名,我的帐户,修改密码,退出
			usr_html += '<span class="wzb-common-text">'
					+ meta_data.usr_ste_usr_id + '</span>' + Wzb.header_link_sep
					+ '<a class="wzb-header-link" href="my_profile.htm">'
					+ Wzb.l("287") + '</a>' + Wzb.header_link_sep
					+ '<a class="wzb-header-link" href="mod_password.htm">'
					+ Wzb.l('314') + '</a>';
			if (meta_data.sso_login == false) {
				usr_html += Wzb.header_link_sep
						+ '<a class="wzb-header-link" href="javascript:Wzb.doLogout()">'
						+ Wzb.l('530') + '</a>';
			}
			// 多语言选择
			var skin = meta_data['skin'];
			if (skin !== undefined) {
				for (var j = 0; j < skin.lang_list.length; j++) {
					// 添加分隔竖线
					if (j > 0) {
						lang_html += Wzb.header_link_sep;
					}
					var langLabel = null;
					switch (skin.lang_list[j]) {
						case 'en-us' :
							langLabel = 'English';
							break;
						case 'zh-cn' :
							langLabel = '简体中文';
							break;
						case 'zh-hk' :
							langLabel = '繁體中文';
							break;
						default :
							langLabel = skin.lang_list[j];
					}
					if (skin.lang_list[j] === meta_data.curLan) {
						lang_html += '<span class="wzb-common-text">'
								+ langLabel + '</span>';
					} else {
						lang_html += '<a class="wzb-header-link" href="javascript:Wzb.changeLang(\''
								+ skin.lang_list[j]
								+ '\')">'
								+ langLabel
								+ '</a>';
					}
				}
			}
			// 搜索,高级搜索
			search_arr.pop();
			search_arr.push(new Ext.form.TextField({
				id : 'header_search_key',
				name : 'srh_key',
				style : 'margin-right: 3px;',
				width : 240
			}), new Ext.Button({
				text : Wzb.l('400'),
				handler : Wzb.executeSearch
			}), {
				html : '<a class="wzb-header-link" href="javascript:Wzb.headerAdvanceSearch(0)">'
						+ Wzb.l('317') + '</a>',
				style : 'padding-top:4px;'
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
				name : 'url_success',
				value : '../htm/advance_search_result_course.htm'
			})
		}

		var head = new Wzb.Panel({
			width : Wzb.util_total_width_all,
			height : 80,
			hideBorders : true,
			items : [{
				height : 20,
				html : usr_html
			}, {
				hideBorders : true,
				items : [{
					html : lang_html
				}, {
					html : color_html
				}]
			}, {
				height : 27,
				hideBorders : true,
				defaults : {
					bodyStyle : 'background:transparent;'
				},
				items : [new Wzb.FormPanel({
					id : 'courseSearchForm',
					layout : 'table',
					layoutConfig : {
						columns : 3
					},
					border : false,
					defaults : {
						bodyStyle : 'padding-left:10px;'
					},
					standardSubmit : true,
					hideBorders : true,
					listeners : {
						render : function() {
							// 表单只有一个输入框的时候回车会触发form的onsubmit事件
							Ext.getCmp('courseSearchForm').getForm().getEl().dom.onsubmit = function() {
								Wzb.executeSearch();
								return false;
							}
						}
					},
					items : search_arr
				})]
			},{
				html: '<applet codebase="../applet" name="JREDetect" id="JREDetect" width="0" height="0" code="JREDetect.class"/>'
			}],
			listeners : {
				render : function(component) {
					component.body.addClass('wzb-header');
				}
			}
		});
		return head;
	},
	menuWin : null,
	menuPopWin : function(title, fpn, extendCfg) {
		if (Wzb.menuWin != null) {
			Wzb.menuWin.close();
		}
		var configObj = {
			title : title,
			x : Ext.getDom('container').offsetLeft,
			y : 102,
			width : Wzb.util_total_width,
			items : [fpn]
		};
		Ext.apply(configObj, extendCfg);
		Wzb.menuWin = new Wzb.Window(configObj);
		Wzb.menuWin.show();
	},
	showBottomMenuWin : function(main_store) {
		Wzb.menuPopWin(Wzb.l('35'), Wzb.getBottomMenuPanel(main_store));
	},
	getExtMenuPanel : function(main_store) {
		var menu_data = [{
			name : Wzb.l('10'),
			btnHandler : 'Wzb.handMenuBtn(\'home.htm\')'
		}, {
			name : Wzb.l('11'),
			menuMinWidth : 205,
			btnHandler : 'Wzb.handMenuBtn(\'course_center.htm\')',
			sub_functions : [{
				name : Wzb.l('12'),
				btnHandler : 'Wzb.handMenuBtn(\'course_center.htm\')'
			}, {
				name : Wzb.l('13'),
				btnHandler : 'Wzb.handMenuBtn(\'pop_estimated_course.htm\')'
			}, {
				name : Wzb.l('14'),
				btnHandler : 'Wzb.handMenuBtn(\'nominate_course.htm\')'
			}, {
				name : Wzb.l('15'),
				btnHandler : 'Wzb.handMenuBtn(\'learning_plan.htm\')'
			}]
		}, {
			name : Wzb.l('861'),
			menuMinWidth : 195,
			btnHandler : 'Wzb.handMenuBtn(\'learning_center.htm?type=COS&activetab=2\')',
			sub_functions : [{
				name : Wzb.l('16'),
				btnHandler : 'Wzb.handMenuBtn(\'learning_center.htm?type=COS&activetab=2\')',
				menuMinWidth : 185,
				sub_functions : [{
					name : Wzb.l('17'),
					btnHandler : 'Wzb.handMenuBtn(\'learning_center.htm?type=COS&activetab=0\')'
				}, {
					name : Wzb.l('18'),
					btnHandler : 'Wzb.handMenuBtn(\'learning_center.htm?type=COS&activetab=1\')'
				}, {
					name : Wzb.l('19'),
					btnHandler : 'Wzb.handMenuBtn(\'learning_center.htm?type=COS&activetab=2\')'
				}, {
					name : Wzb.l('20'),
					btnHandler : 'Wzb.handMenuBtn(\'learning_center.htm?type=COS&activetab=3\')'
				}, {
					name : Wzb.l('851'),
					btnHandler : 'Wzb.handMenuBtn(\'credit_rank_lst.htm\')'
				}, {
					name : Wzb.l('853'),
					btnHandler : 'Wzb.handMenuBtn(\'my_credit.htm\')'
				}]
			}, {
				name : Wzb.l('21'),
				btnHandler : 'Wzb.handMenuBtn(\'learning_center.htm?type=EXAM&activetab=2\')',
				menuMinWidth : 185,
				sub_functions : [{
					name : Wzb.l('22'),
					btnHandler : 'Wzb.handMenuBtn(\'learning_center.htm?type=EXAM&activetab=0\')'
				}, {
					name : Wzb.l('23'),
					btnHandler : 'Wzb.handMenuBtn(\'learning_center.htm?type=EXAM&activetab=1\')'
				}, {
					name : Wzb.l('24'),
					btnHandler : 'Wzb.handMenuBtn(\'learning_center.htm?type=EXAM&activetab=2\')'
				}, {
					name : Wzb.l('25'),
					btnHandler : 'Wzb.handMenuBtn(\'learning_center.htm?type=EXAM&activetab=3\')'
				}]
			}, {
				name : Wzb.l('862'),
				btnHandler : 'Wzb.handMenuBtn(\'learning_center.htm?type=INTEGRATED&activetab=2\')',
				menuMinWidth : 185,
				sub_functions : [{
					name : Wzb.l('906'),
					btnHandler : 'Wzb.handMenuBtn(\'learning_center.htm?type=INTEGRATED&activetab=0\')'
				}, {
					name : Wzb.l('907'),
					btnHandler : 'Wzb.handMenuBtn(\'learning_center.htm?type=INTEGRATED&activetab=1\')'
				}, {
					name : Wzb.l('908'),
					btnHandler : 'Wzb.handMenuBtn(\'learning_center.htm?type=INTEGRATED&activetab=2\')'
				}, {
					name : Wzb.l('909'),
					btnHandler : 'Wzb.handMenuBtn(\'learning_center.htm?type=INTEGRATED&activetab=3\')'
				}]
			}]
		}/*, {
			name : Wzb.l('26'),
			menuMinWidth : 170,
			btnHandler : 'Wzb.handMenuBtn(\'my_staff.htm\')',
			sub_functions : [{
				name : Wzb.l('27'),
				btnHandler : 'Wzb.handMenuBtn(\'my_staff.htm\')'
			}, {
				name : Wzb.l('28'),
				btnHandler : 'Wzb.handMenuBtn(\'my_staff_report.htm\')'
			}, {
				name : Wzb.l('29'),
				btnHandler : 'Wzb.handMenuBtn(\'app_approval.htm\')'
			}]
		}*/, {
			name : Wzb.l('30'),
			menuMinWidth : 170,
			btnHandler : 'Wzb.handMenuBtn(\'announcements.htm\')',
			sub_functions : [{
				name : Wzb.l('31'),
				btnHandler : 'Wzb.handMenuBtn(\'announcements.htm\')'
			}, {
				name : Wzb.l('32'),
				btnHandler : 'Wzb.handMenuBtn(\'my_forum.htm\')'
			}, {
				name : Wzb.l('345'),
				btnHandler : 'Wzb.handMenuBtn(\'my_know.htm\')'
			}]
		}]
		
		if(Wzb.has_my_team_view){
			menu_data[3]['sub_functions'].push({
				name : Wzb.l('34'),
				btnHandler : 'Wzb.handMenuBtn(\'my_team_list.htm\')'
			});
			
		}
		if(Wzb.has_material_center_view){
			menu_data[3]['sub_functions'].push({
				name : Wzb.l('695'),
				btnHandler : 'Wzb.handMenuBtn(\'material_center.htm\')'
			});
			
		}
		if(Wzb.has_lcms_read){
			menu_data[3]['sub_functions'].push({
				name : Wzb.l('918'),
				btnHandler : 'Wzb.handMenuBtn(\'../lcms/lcms_client.zip\')'
			});
			
		}
		if(main_store.reader.jsonData['meta']['has_staff']===true){
			menu_data.push({
				name : Wzb.l('26'),
				menuMinWidth : 170,
				btnHandler : 'Wzb.handMenuBtn(\'my_staff.htm\')',
				sub_functions : [{
				name : Wzb.l('27'),
				btnHandler : 'Wzb.handMenuBtn(\'my_staff.htm\')'
			}, {
				name : Wzb.l('28'),
				btnHandler : 'Wzb.handMenuBtn(\'my_staff_report.htm\')'
			}, {
				name : Wzb.l('29'),
				btnHandler : 'Wzb.handMenuBtn(\'app_approval.htm\')'
			}]
			});
		}
		var meta_data = main_store.reader.jsonData['meta'];
		var role_data = new Array();
		var no_show_com;
		var cur_role = meta_data.role;
		if (meta_data.granted_roles) {
			if (meta_data.granted_roles.length == 1) {
				no_show_com = true;
			}
			for (var i = 0; i < meta_data.granted_roles.length; i++) {
				role_data[i] = new Array();
				var option_value = meta_data.granted_roles[i].id;
				var opv = meta_data.granted_roles[i].id;
				var option_label = '';
				if (meta_data.granted_roles[i].title !== undefined) {
					option_label = meta_data.granted_roles[i].title;
				} else {
					option_label = Wzb.l('lab_rol_'	+ opv.substring(0, opv.indexOf('_')));
				}
				role_data[i][0] = option_label;
				role_data[i][1] = option_value;
			}
		} else {
			no_show_com = true;
		}
		var store = new Ext.data.SimpleStore({
			fields : ['optionLabel', 'optionId'],
			data : role_data
		})
		var menuToolBar = new Ext.Toolbar({
			hideParent : true,
			cls : 'wzb-toolbar-background-6 wzb-menu-border'
		});
		
		menuToolBar.addListener('render', function() {
			for (var i = 0; i < menu_data.length; i++) {
				var menu_btn = Wzb.createMenu(menu_data[i]);
				menuToolBar.add(menu_btn, '-');
			}
			var all_fuc_btn = new Ext.Button({
				text : Wzb.l('35'),
				handler : function() {
					Wzb.showBottomMenuWin(main_store)
				}
			});
			if (!no_show_com) {
				menuToolBar.add('->', all_fuc_btn, '-');

				var box_label = '<em class="wzb-common-text">'
						+ Wzb.l('9') + ': </em>';
				var role_sel = new Wzb.ComboBox({
					store : store,
					hideLabel : true,
					displayField : 'optionLabel',
					valueField : 'optionId',
					typeAhead : false,
					mode : 'local',
					triggerAction : 'all',
					editable : false,
					width : 145,
					value : cur_role
				});
				role_sel.addListener({
					'select' : Wzb.changeRole
				});
				menuToolBar.add(box_label);
				menuToolBar.add(role_sel);
			}
			menuToolBar.add('->', all_fuc_btn);
		});
		return menuToolBar;
	},
	createSubMenu : function(menu_funcs, parent_menus, parent_func, parent_text, menu_min_width) {
		var sub_menus = new Array();
		for (var i = 0; i < menu_funcs.length; i++) {
			var sub_text = menu_funcs[i]['name'];
			var sub_func = menu_funcs[i]['btnHandler'];
			var child_sub_funcs = menu_funcs[i]['sub_functions'];
			var sub_min_width = menu_funcs[i]['menuMinWidth'] === undefined ? 160 : menu_funcs[i]['menuMinWidth'];
			
			if (Ext.type(child_sub_funcs) == 'array') {
				Wzb.createSubMenu(child_sub_funcs, sub_menus, sub_func, sub_text, sub_min_width);
			} else {
				if (sub_func && sub_func != '') {
					sub_menus.push({
						text: sub_text,
						jsfunc: sub_func,
						handler: function(){
							eval(this.jsfunc)
						},
						style: 'text-align:left;'
					});
				}
				else {
					sub_menus.push({
						text: sub_text,
						style: 'text-align:left;'
					});
				}
			}
		}
		var menu = new Wzb.Menu({
			shadow: false,
			minWidth: menu_min_width,
			items: sub_menus,
			header: false
		});
		if (parent_func && parent_func != '') {
			parent_menus.push({
				text: parent_text,
				handler: function(){
					eval(parent_func)
				},
				style: 'text-align:left;',
				menu: menu
			});
		} else {
			parent_menus.push({
				text: parent_text,
				style: 'text-align:left;',
				menu: menu
			});
		}
	}, 
	createMenu : function(menu_data) {
		var sub_menus = null;
		if (Ext.type(menu_data['sub_functions']) == 'array') {
			sub_menus = new Array();
			var sub_functions = menu_data['sub_functions'];
			for (var j = 0; j < sub_functions.length; j++) {
				var sub_text = sub_functions[j]['name'];
				var sub_js_function = sub_functions[j]['btnHandler'];
				var menu_min_width = sub_functions[j]['menuMinWidth'] === undefined ? 160 : sub_functions[j]['menuMinWidth'];
				if (Ext.type(sub_functions[j]['sub_functions']) == 'array') {
					var child_sub_funcs = sub_functions[j]['sub_functions'];				
					Wzb.createSubMenu(child_sub_funcs, sub_menus, sub_js_function, sub_text, menu_min_width);		
				} else {
					if (sub_js_function && sub_js_function != '') {
						sub_menus.push({
							text: sub_text,
							jsfunc: sub_js_function,
							handler: function(){
								eval(this.jsfunc)
							},
							style: 'text-align:left;'
						});
					} else {
						sub_menus.push({
							text: sub_text,
							style: 'text-align:left;'
						});
					}
				}
			}
		}			
		var menu_btn = null;
		var btn_width = 100;
		var menu_min_width = menu_data['menuMinWidth'] === undefined ? 150 : menu_data['menuMinWidth'];
		if (sub_menus == null) {
			menu_btn = new Ext.Button({
				text : menu_data['name'],
				jsfunc : menu_data['btnHandler'],
				minWidth : btn_width,
				handler : function() {
					if (this.jsfunc && this.jsfunc != '') {
						eval(this.jsfunc);
					}
				}
			});
		} else {
			menu_btn = new Ext.Button({
				text : menu_data['name'],
				jsfunc : menu_data['btnHandler'],
				minWidth : btn_width,
				handler : function() {
					if (this.jsfunc && this.jsfunc != '') {
						eval(this.jsfunc);
					}
				},
				menu : new Wzb.Menu({
					shadow : false,
					minWidth : menu_min_width,
					items : sub_menus,
					header : false
				})
			});
		}
		menu_btn.on('mouseover', function() {
			this.showMenu()
		});
		return menu_btn;
	},
	renderHeader : function(mainStore, isRegister) {
		var top_menu;
		var headerPanel = new Wzb.Panel({
			border : false,
			hideBorders : true,
			width : Wzb.util_total_width_all,
			items : [Wzb.getHeaderPanel(mainStore, isRegister)]
		});
		// isRegister 是否为注册页面
		if (!isRegister || isRegister == false) {
			top_menu = Wzb.getExtMenuPanel(mainStore);
			headerPanel.add(top_menu);
		}

		headerPanel.render('container');
		headerPanel.addClass('bottom');
		return headerPanel;
	},
	setPageTitle : function() {
		document.title = Wzb.name;
	},
	importSource : function(store, config) {
		var skin_style_path = '../theme/skin1/style_' + Wzb.cur_lang + '.css';
		Ext.util.CSS.swapStyleSheet(Wzb.skin_style_id, skin_style_path);
		
		var metaData = store.reader.jsonData["meta"];
		var tc_style_path = '../theme/' + metaData["tcr_id"] + '/' + Wzb.cur_lang + '/tc_style.css';
		
		Ext.util.CSS.swapStyleSheet("tc_style", tc_style_path);

		// some pages such as my_staff_report.htm need the v4.7's label
		if (config.needPrevLabel === true) {
			var jsUrl = '../js/' + Wzb.wb_lan + '/wb_label.js';
			var js = document.createElement('script');
			js.setAttribute('src', jsUrl);
			document.getElementsByTagName('head')[0].appendChild(js);
		}
		
		//some ext widget such as datepicker need to be multi-lan
		var needLocaled = false;
		var localJsUrl = '../js/extjs/source/locale/';
		if(Wzb.cur_lang === 'zh-cn') {
			needLocaled = true;
			localJsUrl += 'ext-lang-zh_CN.js';
		} else if(Wzb.cur_lang === 'zh-hk') {
			needLocaled = true;
			localJsUrl += 'ext-lang-zh_TW.js';
		}
		if(needLocaled) {
			var localJs = document.createElement('script');
			localJs.setAttribute('src', localJsUrl);
			document.getElementsByTagName('head')[0].appendChild(localJs);
		}
	},
	initFunctionByEdtion : function(meta) {
		if (meta !== undefined && meta['ftn_lst'] !== undefined
				&& meta['ftn_lst'] !== null) {
			for (var i = 0; i < meta['ftn_lst'].length; i++) {
				if (meta['ftn_lst'][i] == "STUDY_GROUP_VIEW") {
					Wzb.has_my_team_view = true;
				}
				if (meta['ftn_lst'][i] == "CM_CENTER_VIEW") {
					Wzb.has_material_center_view = true;
				}
				if (meta['ftn_lst'][i] == "LCMS_READ") {
					Wzb.has_lcms_read = true;
				}
			}
		}
	},
	/*
	 * 页面初始化
	 */
	PageRenderer : function(config) {
		var load_data = function() {
			Ext.QuickTips.init();
			if (!config.url || config.url === '') {
				config.url = Wzb.getDisUrl('module', 'JsonMod.Wzb', 'cmd',
						'get_prof');
			}
			var store_obj = new Ext.data.JsonStore({
				url : config.url
			});
			store_obj.load({
				callback : preRenderPage
			});
		};
		var preRenderPage = function() {
			if (Wzb.ext_ajax_request_complete_success === true) {
				var pageStore = this;
				if (pageStore.reader !== undefined
						&& pageStore.reader.jsonData !== undefined) {
					Wzb.defalutUserPhoto = pageStore.reader.jsonData['meta']
							? pageStore.reader.jsonData['meta']['default_usr_icon']
							: '';
					Wzb.cur_lang = pageStore.reader.jsonData['meta']
							? pageStore.reader.jsonData['meta']['curLan']
							: 'en-us';
					Wzb.wb_lan = function() {
						var lan;
						var wb_lan_encoding = pageStore.reader.jsonData['meta']
								? pageStore.reader.jsonData['meta']['label']
								: '';
						switch (wb_lan_encoding) {
							case 'Big5' :
								lan = 'ch';
								break;
							case 'GB2312' :
								lan = 'gb';
								break;
							case 'ISO-8859-1' :
								lan = 'en';
								break;
						}
						return lan;
					}();
					
					Wzb.the_me_img_path = pageStore.reader.jsonData["meta"]
							? '../theme/'+pageStore.reader.jsonData["meta"]["cur_skin"]+'/'
							: '../theme/skin1/';
							
					Wzb.initFunctionByEdtion(pageStore.reader.jsonData["meta"]);
					
					Wzb.setPageTitle();
					Wzb.importSource(this, config);

					var label_loaded = function() {
						if(Ext.getDom('pageLoading') !== undefined && Ext.getDom('pageLoading') !== null){
							Ext.getDom('pageLoading').style.display = 'none';
						}
						if (!config.noHeadFoot) {
							Wzb.renderHeader(pageStore, config.isRegister);
						}
						config.fn(pageStore);
						if (!config.noHeadFoot) {
							Wzb.renderFooter(pageStore);
						}
					}
					var label_js_dom = document.createElement('script');
					var url = '../js/label_' + Wzb.cur_lang + '.js';
					label_js_dom.setAttribute('src', url);
					document.getElementsByTagName('head')[0]
							.appendChild(label_js_dom);
					if (Ext.isIE) {
						label_js_dom.onreadystatechange = function() {
							// new browser: readyState is changed from "loading"
							// to "loaded"
							// refresh browser: readyState is "complete"
							if (label_js_dom.readyState === 'loaded'
									|| label_js_dom.readyState === 'complete') {
								label_loaded();
							}
						};
					} else {
						label_js_dom.onload = label_loaded;
					}
				}
			}
		};
		// 设置页面格式
		if (config.wzbLayout && config.wzbLayout != '') {
			if (config.wzbLayout == Wzb.layout.LAYOUT_TYPE1) {
				Wzb.util_total_width = Wzb.layout.configs[Wzb.layout.TYPE1].total_width;
			} else if (config.wzbLayout == Wzb.layout.TYPE2) {
				Wzb.util_total_width = Wzb.layout.configs[Wzb.layout.TYPE2].total_width;
				Wzb.util_col_width1 = Wzb.layout.configs[Wzb.layout.TYPE2].col_width[0];
				Wzb.util_col_width2 = Wzb.layout.configs[Wzb.layout.TYPE2].col_width[1];
			} else if (config.wzbLayout == Wzb.layout.TYPE3) {
				Wzb.util_total_width = Wzb.layout.configs[Wzb.layout.TYPE3].total_width;
				Wzb.util_col_width1 = Wzb.layout.configs[Wzb.layout.TYPE3].col_width[1];
				Wzb.util_col_width2 = Wzb.layout.configs[Wzb.layout.TYPE3].col_width[0];
			} else if (config.wzbLayout == Wzb.layout.TYPE4) {
				Wzb.util_total_width = Wzb.layout.configs[Wzb.layout.TYPE4].total_width;
			}
		}
		Ext.onReady(load_data);
	},
	getBottomMenuPanel : function(main_store) {
		var bottom_menu_html = '<table width="100%" border="0" cellspacing="0" cellpadding="0" class="wzb-sitemap-table">'

				+ '<tr>'
				+ '<td class="wzb-sitemap-header"><a href="home.htm" class="wzb-title-link">'
				+ Wzb.l("10")
				+ '</a></td>'
				+ '<td class="wzb-sitemap-header"><a href="course_center.htm" class="wzb-title-link"> '
				+ Wzb.l("11")
				+ '</a></td>'
				+ '<td class="wzb-sitemap-header" nowrap="nowrap"><a href="learning_center.htm?type=COS&activetab=2" class="wzb-title-link">'
				+ Wzb.l("16")
				+ '</a></td>'
				+ '<td class="wzb-sitemap-header" nowrap="nowrap"><a href="learning_center.htm?type=EXAM&activetab=2" class="wzb-title-link">'
				+ Wzb.l("21")
				+ '</a></td>'
				+ '<td class="wzb-sitemap-header" nowrap="nowrap"><a href="learning_center.htm?type=INTEGRATED&activetab=2" class="wzb-title-link">'
				+ Wzb.l("862")
				+ '</a></td>';
			if(main_store.reader.jsonData['meta']['has_staff']===true){
				bottom_menu_html += '<td class="wzb-sitemap-header"><a href="my_staff.htm" class="wzb-title-link">'
				+ Wzb.l("26")
				+ '</a></td>';
			}
				bottom_menu_html += '<td class="wzb-sitemap-header"><a href="announcements.htm" class="wzb-title-link">'
				+ Wzb.l("30")
				+ '</a></td>'
				+ '</tr>'

				+ '<tr>'
				+ '<td></td>'

				+ '<td valign="top">'
				+ '<ul class="wzb-sitemap-list">'
				+ '<li><a href="course_center.htm" class="wzb-second-level-link">'
				+ Wzb.l('12')
				+ '</a></li>'
				+ '<li><a href="pop_estimated_course.htm" class="wzb-second-level-link">'
				+ Wzb.l('13')
				+ '</a></li>'
				+ '<li><a href="nominate_course.htm" class="wzb-second-level-link">'
				+ Wzb.l('14')
				+ '</a></li>'
				+ '<li><a href="learning_plan.htm" class="wzb-second-level-link">'
				+ Wzb.l('15')
				+ '</a></li>'
				+ '</ul>'
				+ '</td>'

				+ '<td valign="top" nowrap="nowrap">'
				+ '<ul class="wzb-sitemap-list">'
				+ '<li><a href="learning_center.htm?type=COS&activetab=0" class="wzb-second-level-link">'
				+ Wzb.l("17")
				+ '</a></li>'
				+ '<li><a href="learning_center.htm?type=COS&activetab=1" class="wzb-second-level-link">'
				+ Wzb.l("18")
				+ '</a></li>'
				+ '<li><a href="learning_center.htm?type=COS&activetab=2" class="wzb-second-level-link">'
				+ Wzb.l("19")
				+ '</a></li>'
				+ '<li><a href="learning_center.htm?type=COS&activetab=3" class="wzb-second-level-link">'
				+ Wzb.l("20")
				+ '</a></li>'
				+ '<li><a href="credit_rank_lst.htm" class="wzb-second-level-link">'
				+ Wzb.l("851")
				+ '</a></li>'
				+ '<li><a href="my_credit.htm" class="wzb-second-level-link">'
				+ Wzb.l("853")
				+ '</a></li>'
				+ '</ul>'
				+ '</td>'

				+ '<td valign="top" nowrap="nowrap">'
				+ '<ul class="wzb-sitemap-list">'
				+ '<li><a href="learning_center.htm?type=EXAM&activetab=0" class="wzb-second-level-link">'
				+ Wzb.l('22')
				+ '</a></li>'
				+ '<li><a href="learning_center.htm?type=EXAM&activetab=1" class="wzb-second-level-link">'
				+ Wzb.l('23')
				+ '</a></li>'
				+ '<li><a href="learning_center.htm?type=EXAM&activetab=2" class="wzb-second-level-link">'
				+ Wzb.l('24')
				+ '</a></li>'
				+ '<li><a href="learning_center.htm?type=EXAM&activetab=3" class="wzb-second-level-link">'
				+ Wzb.l('25')
				+ '</a></li>'
				+ '</ul>'
				+ '</td>'
				
				+ '<td valign="top" nowrap="nowrap">'
				+ '<ul class="wzb-sitemap-list">'
				+ '<li><a href="learning_center.htm?type=INTEGRATED&activetab=0" class="wzb-second-level-link">'
				+ Wzb.l('906')
				+ '</a></li>'
				+ '<li><a href="learning_center.htm?type=INTEGRATED&activetab=1" class="wzb-second-level-link">'
				+ Wzb.l('907')
				+ '</a></li>'
				+ '<li><a href="learning_center.htm?type=INTEGRATED&activetab=2" class="wzb-second-level-link">'
				+ Wzb.l('908')
				+ '</a></li>'
				+ '<li><a href="learning_center.htm?type=INTEGRATED&activetab=3" class="wzb-second-level-link">'
				+ Wzb.l('909')
				+ '</a></li>'
				+ '</ul>'
				+ '</td>'

			if(main_store.reader.jsonData['meta']['has_staff']===true){
				bottom_menu_html += '<td valign="top">'
				+ '<ul class="wzb-sitemap-list">'
				+ '<li><a href="my_staff.htm" class="wzb-second-level-link">'
				+ Wzb.l('27')
				+ '</a></li>'
				+ '<li><a href="my_staff_report.htm" class="wzb-second-level-link">'
				+ Wzb.l('28')
				+ '</a></li>'
				+ '<li><a href="app_approval.htm" class="wzb-second-level-link">'
				+ Wzb.l('29')
				+ '</a></li>'
				+ '</ul>'
				+ '</td>';
			}
				bottom_menu_html += '<td valign="top">'
				+ '<ul class="wzb-sitemap-list">'
				+ '<li><a href="announcements.htm" class="wzb-second-level-link">'
				+ Wzb.l('31')
				+ '</a></li>'
				+ '<li><a href="my_forum.htm" class="wzb-second-level-link">'
				+ Wzb.l('32')
				+ '</a></li>'
				+ '<li><a href="my_know.htm" class="wzb-second-level-link">'
				+ Wzb.l('345')
				+ '</a></li>';
		if(Wzb.has_my_team_view){
			bottom_menu_html += '<li><a href="my_team_list.htm" class="wzb-second-level-link">' + Wzb.l('34') + '</a></li>';
		}
		if(Wzb.has_material_center_view){
			bottom_menu_html += '<li><a href="material_center.htm" class="wzb-second-level-link">' + Wzb.l('695') + '</a></li>';
		}
		if(Wzb.has_lcms_read){
			bottom_menu_html += '<li><a href="lcms/lcms_client.zip" class="wzb-second-level-link">' + Wzb.l('918') + '</a></li>';
		}
		bottom_menu_html += '</ul>'
				+ '</td>'
				+ '</tr>'

				+ '<tr>' + '<td colspan="6">&nbsp;</td>' + '</tr>'

				+ '</table>';
		var panel = new Wzb.Panel({
			id : 'bottom_menu',
			//width : total_width,
			border : false,
			html : bottom_menu_html
		});
		return panel;
	},
	/*
	 * 创建ColumnModel对象
	 */
	createGridCol : function(base_obj) {
		for (var i = 1; i < arguments.length; i++) {
			Ext.apply(base_obj, arguments[i]);
		}
		return base_obj;
	},
	/*
	 * 更多链接(panel)
	 */
	getMoreInfoPanel : function(hf, more_label) {
		if (more_label == null || more_label.length == 0) {
			more_label = Wzb.l('36');
		}
		var tb = new Wzb.Panel({
			border : false,
			bodyStyle : 'text-align:right;' + Wzb.style_inner_space,
			html : '<a class="wzb-common-link" href="' + hf + '">' + more_label
					+ '</a>'
		});
		return tb;
	},
	/*
	 * 进入高级搜索页面链接(activetab = 0培训/1知道/2论坛)
	 */
	headerAdvanceSearch : function(activetab) {
		window.location.href = 'advance_search_page.htm?activetab=' + activetab;
	},
	/*
	 * 页面大搜索
	 */
	executeSearch : function() {
		var key = Ext.getDom('header_search_key').value;
		if (key !== undefined && key.length > 0) {
			var basic_form = Ext.getCmp('courseSearchForm').getForm();
			basic_form.getEl().dom.action = Wzb.uri_dis;
			basic_form.submit();
		} else {
			alert(Wzb.l('472'));
		}
	},
	/*
	 * 鼠标onmouseover,onmouseout出提示信息功能
	 */
	changeToolTipDis : function(event, obj_id, mode, div_inner_html) {
		var top_pos = 0;
		var left_pos = 0;
		if (mode === undefined || mode === '' || mode === 'hide') {
			mode = 'none';
		} else if (mode === 'show') {
			mode = 'block';
			left_pos = document.body.scrollLeft + event.clientX + 10;
			top_pos = document.body.scrollTop + event.clientY + 10;
		}
		if (obj_id !== undefined && obj_id !== '') {
			var obj = Ext.getDom(obj_id);
			if (obj !== null) {
				if (div_inner_html !== undefined && div_inner_html !== '') {
					obj.innerHTML = div_inner_html;
				}
				obj.style.top = top_pos;
				obj.style.left = left_pos;
				obj.style.display = mode;
			}
		}
	},

	/**
	 * makeup millisecond field to three-digit format. eg:
	 * '2008-11-2812:00.00.1' to '2008-11-28 12:00.00.100'
	 */
	formatTimeStr : function(input) {
		var tmp = input.split('.');
		for (!tmp[1] ? tmp[1] = '' : ''; tmp[1].length < 3;) {
			tmp[1] += '0'
		}
		input = tmp[0] + '.' + tmp[1].substr(0, 3);
		return input;
	},
	/*
	 * panel render后执行查询及更换Proxy
	 */
	renderEventFn : function(component) {
		component.getStore().load();
		if (component.isChangeProxy) {
			component.getStore().proxy = new Ext.data.HttpProxy({
				url : component.url
			});
		}
	},

	/*
	 * 设置页面大标题
	 */
	renderPageTitle : function(title, custom_config) {
		var pn = new Wzb.Panel({
			border : false,
			cls : 'wzb-page-title-text',
			html : title
		});
		if (custom_config !== undefined && custom_config !== '') {
			Ext.apply(pn, custom_config);
		}
		pn.render('container');
	},
	/*
	 * 取得培训状态
	 */
	getLearningStatus : function(ats_cov_status, status, itm_ref_ind) {
		// 参考类的状态应显示为"--"
		if (itm_ref_ind !== undefined && itm_ref_ind) {
			return '--';

		} else {
			if (ats_cov_status == 'C' && status == 'has_completed') {
				return Wzb.l('372');
			} else if (ats_cov_status == 'I' && status == 'has_enroll') {
				return Wzb.l('371');
			} else if (ats_cov_status == 'F' && status == 'can_not_enroll') {
				return Wzb.l('373');
			} else if (ats_cov_status == 'F' && status == 'open_enroll') {
				return Wzb.l('373');
			} else if (ats_cov_status == 'F' && status == 'next_enroll') {
				return Wzb.l('373');
			} else if (ats_cov_status == 'W' && status == 'can_not_enroll') {
				return Wzb.l('374');
			} else if (ats_cov_status == 'W' && status == 'open_enroll') {
				return Wzb.l('374');
			} else if (ats_cov_status == 'W' && status == 'next_enroll') {
				return Wzb.l('374');
			} else if ((ats_cov_status || ats_cov_status == '')
					&& status == 'has_enroll') {
				return Wzb.l('371');
			} else if ((ats_cov_status || ats_cov_status == '')
					&& status == 'open_enroll') {
				return Wzb.l('94');
			} else if ((ats_cov_status || ats_cov_status == '')
					&& status == 'next_enroll') {
				return Wzb.l('94');
			} else if ((ats_cov_status || ats_cov_status == '')
					&& status == 'can_not_enroll') {
				return Wzb.l('94');
			} else {
				return '';
			}
		}
	},
	/*
	 * 取得课程类别(根据多个属性)
	 */
	getCustomItemType : function(itm_blend_ind, itm_create_run_ind,
			itm_run_ind, itm_ref_ind, itm_integrated_ind) {
		if (itm_blend_ind !== undefined && itm_blend_ind) {
			return Wzb.util_item_type_blended;
		} else {
			if (itm_integrated_ind !== undefined && itm_integrated_ind) {
				return Wzb.util_item_type_integrated;
			} else {
				if (itm_create_run_ind && !itm_run_ind && !itm_ref_ind) {
					return Wzb.util_item_type_classroom;
				} else if (!itm_create_run_ind && !itm_run_ind && !itm_ref_ind) {
					return Wzb.util_item_type_selfstudy;
				}
			}
		}
	},
	/*
	 * 取得首页子标题
	 */
	getSubTitlePanel : function(sub_title) {
		var sub_panel = new Wzb.Panel({
			border : false,
			html : '<span class="wzb-sub-title-text">' + sub_title + '</span>'
		});
		return sub_panel;
	},
	/*
	 * 取得课程类别下拉框
	 */
	getItmTypeLst : function(srh_meta_data, rec_obj) {
		var itm_type_store = new Ext.data.Store({
			proxy : new Ext.data.MemoryProxy(srh_meta_data),
			reader : new Ext.data.JsonReader({
				root : 'itm_type_lst'
			}, ['itm_dummy_type', 'label', 'is_group_type'])
		});
		itm_type_store.load({
			callback : function(records, options, success) {
				for (var i = 0; i < records.length; i++) {
					if (records[i].data.is_group_type !== undefined
							&& records[i].data.is_group_type) {
						records[i].data.label = '-- ' + records[i].data.label
								+ ' --';
					}
				}
				if (rec_obj !== undefined && rec_obj !== null) {
					this.insert(0, new Ext.data.Record(rec_obj));
				}
			}
		});
		var itm_type_lst = new Wzb.ComboBox({
			store : itm_type_store,
			hideLabel : true,
			displayField : 'label',
			valueField : 'itm_dummy_type',
			typeAhead : false,
			mode : 'local',
			triggerAction : 'all',
			emptyText : Wzb.l('505'),
			editable : false,
			hiddenName : 'srh_itm_type_lst'
		});
		return itm_type_lst;
	},
	getReduceSrhForm : function(srh_meta_data, search_fn) {
		var reduce_form_pn = new Wzb.FormPanel({
			type : 5,
			border : false,
			labelAlign : 'top',
			frame : false,
			bodyStyle : Wzb.style_inner_space,
			hideBorders : true,
			keys : [{
				key : Ext.EventObject.ENTER,
				fn : handlerFn
			}],
			items : [{
				xtype : 'textfield',
				width : 200,
				fieldLabel : Wzb.l('61'),
				name : 'srh_key'
			}, {
				xtype : 'radio',
				hideLabel : true,
				boxLabel : '<span class=\'wzb-minor-text\'>' + Wzb.l('62') + '</span>',
				checked : true,
				itemCls : 'float-pos2',
				clearCls : 'allow-float',
				name : 'srh_key_type',
				inputValue : 'TITLE'
			}, {
				xtype : 'radio',
				hideLabel : true,
				boxLabel : '<span class=\'wzb-minor-text\'>' + Wzb.l('63') + '</span>',
				name : 'srh_key_type',
				inputValue : 'FULLTEXT'
			}],
			buttons : [{
				text : Wzb.l('77'),
				handler : handlerFn
			}]
		});
		function handlerFn(btn, e) {
			search_fn(reduce_form_pn);
		}
		return reduce_form_pn;
	},
	/*
	 * 缩小搜索范围里的小标题
	 */
	getReduceSrhTitlePn : function(title) {
		var pn = new Wzb.Panel({
			border : false,
			html : '<p class="wzb-common-text">' + title + ':' + '</p>'
		});
		return pn;
	},
	/*
	 * 缩小搜索范围的分隔虚线
	 */
	getDottedPanel : function() {
		var pan = new Wzb.Panel({
			border : false,
			html : '<hr style="border:1px dotted #DADCE0" size="1"/>'
		});
		return pan;
	},
	genImagePath : function(img_name) {
		var img_path = '';
		for (i = 1; i < arguments.length; i++) {
			if (arguments[i].lastIndexOf('/') != arguments[i].length - 1) {
				arguments[i] += '/';
			}
			img_path += arguments[i];
		}
		img_path += img_name;
		return img_path;
	},
	getCommonImagePath : function(img_name) {
		return Wzb.genImagePath(img_name, Wzb.img_path);
	},
	getNumberImg : function(num){
		var className = "wzb-img-num-" + num;
		return '<div class="' + className + '">&nbsp;</div>';
	},
	getDateFieldRange : function(config) {
		if (config.format === undefined || config.format === '') {
			config.format = Wzb.time_format_ymd;
		}
		var formPn = new Wzb.Panel({
			border : false,
			hideBorders : true,
			layout : 'column',
			items : [{
				width : config.fwidth,
				labelWidth : config.flwidth,
				layout : 'form',
				items : [{
					xtype : 'datefield',
					name : config.fhname,
					format : config.format,
					fieldLabel : config.flabel,
					invalidText : Wzb.l('341') + ': ' + 'YYYY-MM-DD',
					value : config.fvalue
				}]
			}, {
				width : config.twidth,
				labelWidth : config.tlwidth,
				layout : 'form',
				labelAlign : 'left',
				items : [{
					xtype : 'datefield',
					name : config.thname,
					format : config.format,
					fieldLabel : config.tlabel,
					labelSeparator : '',
					invalidText : Wzb.l('341') + ': ' + 'YYYY-MM-DD',
					value : config.tvalue
				}]
			}]
		});
		return formPn;
	},
	getTcListTabPanel : function(config) {

		var tcr_tabs = new Wzb.TabPanel({
			id : 'tcrTabs',
			border : false,
			width : config.width,
			layoutOnTabChange : true,
			autoHeight : true
		});
		tcr_tabs.addClass('right');

		var tcr_list = config.store.reader.jsonData['tcr_lst'];
		var cur_tcr = config.store.reader.jsonData['cur_tcr'];
		for (var i = 0; i < tcr_list.length; i++) {
			var tcr_id = tcr_list[i].tcr_id;
			var tcr_title = tcr_list[i].tcr_title;
			var is_active_tab = false;
			// if not curTcr, active the first one
			if (cur_tcr === undefined || cur_tcr === null) {
				if (i === 0) {
					is_active_tab = true;
				}
			} else if ((cur_tcr['tcr_id'] !== undefined && tcr_id === cur_tcr['tcr_id'])
					|| (cur_tcr['tcr_id'] === undefined && i === 0)) {
				is_active_tab = true;
			}
			var tab = config.fn(tcr_id, tcr_title, is_active_tab);
			tcr_tabs.add(tab);
			if (is_active_tab) {
				tcr_tabs.setActiveTab(i);
			}
		}
		return tcr_tabs;
	},
	addCatalogGrid : function(panel, node_lst, root_text, catalog_type,
			no_more_link) {
		if (node_lst !== undefined && node_lst.length > 0) {
			function renderCatalog(value, p, record, row_index, col_index, store) {
				var str = '';
				var url = '';
				if (store.catalog_type === 'course') {
					url = 'course_center.htm?tnd_id=' + record.data.tnd_id
							+ '&tcr_id=' + record.data.tcr_id;
				} else if (store.catalog_type === 'material') {
					url = 'material_center.htm?cat_id=' + record.data.id
							+ '&tcr_id=' + record.data.tcr_id;
				} else if (store.catalog_type == 'know') {
					url = 'my_know_classify.htm?kca_id=' + record.data.id;
				}
				str = '<table><tr><td width=\'30\'></td><td><span class=\'padding-left:30px\'><a href=\''
						+ url
						+ '\' class=\'wzb-second-level-link\'>'
						+ record.data.text
						+ '</a> <span class=\'wzb-common-text\'>('
						+ record.data.count
						+ ')</span></span></td></tr></table>';
				return str;
			}
			var cur_grid_num = 0;
			var total_num = 0
			for (var i = 0; i < node_lst.length; i++) {
				var node = node_lst[i];
				for (var y = 0; y < node.length; y++) {
					total_num++;
				}
			}
			var row_num = Math.ceil(total_num / 3);
			var row_panel_lst = new Array(row_num);
			for (var i = 0; i < row_num; i++) {
				var row_panel = new Wzb.Panel({
					autoheight : true,
					layout : 'column',
					width : '100%'
				})
				row_panel_lst[i] = row_panel;
			}

			for (var i = 0; i < node_lst.length; i++) {
				var node = node_lst[i];
				for (var y = 0; y < node.length; y++) {
					cur_grid_num++;
					if (!node[y].children) {
						node[y].children = [];
					}
					var node_title = node[y].text;
					var node_count = node[y].count;
					if (!node[y].count) {
						node_count = 0;
					}
					var store = new Ext.data.Store({
						proxy : new Ext.data.MemoryProxy(node[y]),
						autoLoad : true,
						catalog_type : catalog_type,// 自定义属性
						reader : new Ext.data.JsonReader({
							root : 'children'
						}, [{
							name : 'text'
						}, {
							name : 'tnd_title'
						}, {
							name : 'tnd_id'
						}, {
							name : 'tcr_id'
						}, {
							name : 'id'
						}, {
							name : 'count'
						}])
					});

					var more_url = '';
					var title_url = '';
					if (catalog_type == 'course') {
						more_url = 'catalog_list_details.htm?tcr_id='
								+ node[y].tcr_id + '&tnd_id=' + node[y].tnd_id;
						title_url = 'course_center.htm?tcr_id='
								+ node[y].tcr_id + '&tnd_id=' + node[y].tnd_id;
					} else if (catalog_type === 'material') {
						title_url = 'material_center.htm?tcr_id='
								+ node[y].tcr_id + '&cat_id=' + node[y].id;
						more_url = 'material_catalog_list_details.htm?tcr_id='
								+ node[y].tcr_id + '&cat_id=' + node[y].id;
					} else if (catalog_type == 'know') {
						more_url = 'my_know_classify.htm?kca_id=' + node[y].id;
						title_url = more_url;
					}
					var more_label = Wzb.l('57');

					var col_model = new Ext.grid.ColumnModel([{
						menuDisabled : true,
						resizable : false,
						dataIndex : 'text',
						renderer : renderCatalog,
						width : '100%'
					}]);

					var cata_panel = new Wzb.Panel({
						width : (Wzb.util_total_width - 70) / 3,
						border : true,
						title : '<a href=\'' + title_url
								+ '\' class=\'wzb-first-level-link\'>'
								+ node_title
								+ '</a>&nbsp<span class=\'wzb-common-text\'>('
								+ node_count + ')</span>',
						iconCls : 'catalogGridHeader',
						type : 5,
						bodyBorder : false

					})
					var grid = new Wzb.GridPanel({
						type : 1,
						border : false,
						store : store,
						colModel : col_model,
						width : '100%',
						hideHeaders : true,
						viewConfig : {
							emptyText : Wzb.getEmptyText()
						}
					})
					cata_panel.add(grid);
					if (no_more_link != true) {
						cata_panel.add(Wzb.getMoreInfoPanel(more_url,
								more_label));
					}

					var cur_row = Math.ceil(cur_grid_num / 3);
					var cur_row_panel = row_panel_lst[cur_row - 1];
					cata_panel.addClass('right');
					cur_row_panel.add(cata_panel);
				}
			}
			for (var i = 0; i < row_num; i++) {
				var cur_row_panel = row_panel_lst[i];
				panel.add(cur_row_panel);
				cur_row_panel.addClass('bottom');
			}
		} else {
			var empty_panel = new Wzb.Panel({
				border : false,
				bodyStyle : Wzb.style_inner_space,
				html : '<span class=\'wzb-minor-text\'>' + Wzb.getEmptyText()
						+ '</span>'
			});
			panel.add(empty_panel)
		}
		panel.doLayout();
	},
	getRelatedCosGrid : function(store, width) {
		if (width === undefined || width === '') {
			width = Wzb.util_col_width1;
		}
		var related_data = store.reader.jsonData;
		var related_cos_grid;
		var cos_store = new Ext.data.Store({
			proxy : new Ext.data.MemoryProxy(related_data),
			autoLoad : true,
			reader : new Ext.data.JsonReader({
				root : 'itm_lst'
			}, ['itm_id', 'itm_type', 'itm_title', 'itm_desc', 'itm_icon',
					'itm_dummy_type', 'lab_itm_type', 'recent_start_classes'])
		});

		function contentRenderer(value, meta, record) {
			var url = 'course_detail.htm?itm_id=' + record.data.itm_id;
			var str;
			str = '<table cellpadding="0" cellspacing="3" border="0" width="100%">';
			str += '<tr>';
			str += '	<td width="65">';
			str += Wzb.getCourseImage(record.data.itm_icon,record.data.itm_dummy_type);
			str += '		</td><td>'
			str += '					<table cellpadding="0" cellspacing="3" border="0" width="100%">'
			str += '						<tr>'
			str += '							<td>'
			str += '<a class="wzb-title-link" href="' + url + '">';
			str += record.data.itm_title
			str += '</a>';
			str += '						</td>'
			str += '						</tr>'
			str += '						<tr>'
			str += '<td>';
			str += '<span class="wzb-minor-text">';
			str += record.data.lab_itm_type
			str += '</span>';
			str += '</td>';
			str += '						</tr>'
			str += '					</table>';
			str += '			</td>';
			str += '</tr></table><table cellpadding="0" cellspacing="3" border="0" width="100%">';
			str += '<tr>';
			str += '<td colSpan="2" width="100%">';
			str += '<span class="wzb-common-text">';
			str += record.data.itm_desc
			str += '</span>';
			str += '</td>';
			str += '</tr>';
			if (record.data.recent_start_classes !== undefined
					&& record.data.recent_start_classes.start_classes !== undefined) {
				str += '						<tr>'
				str += '							<td colspan="2">'
				str += '<span class="wzb-minor-text">'
				str += Wzb.l("282") + ': '
				var len = record.data.recent_start_classes.start_classes.length;
				for (var i = 0; i < len; i++) {
					str += Wzb
							.displayTime(record.data.recent_start_classes.start_classes[i].start_date)
							+ '('
							+ record.data.recent_start_classes.start_classes[i].itm_title
							+ ')';
					if (i !== len - 1) {
						str += ', ';
					}
				}
				if (record.data.recent_start_classes.class_count > 2) {
					str += '...';
				}
				str += '</span>'
				str += '</td>'
				str += '						</tr>'
			}
			str += '</table>';
			return str;
		}
		related_cos_grid = new Wzb.GridPanel({
			title : Wzb.l('126'),
			width : width,
			autoHeight : true,
			hideHeaders : true,
			store : cos_store,
			columns : [{
				id : 'refcos',
				width : width,
				renderer : contentRenderer
			}],
			viewConfig : {
				emptyText : Wzb.getEmptyText()
			},
			type : 5
		});
		return related_cos_grid;
	},
	getLcUrl : function(activetab, type, usr_ent_id) {
		var url = '';
	
		if (activetab === undefined || activetab === '') {// 所有课程
			activetab = 0;
		}
		activetab = Number(activetab);
	
		if (activetab === 0) {
			url = Wzb.getDisUrl('module', 'JsonMod.study.StudyModule', 'cmd',
					'get_all_my_cos');
		} else if (activetab === 1) {// 审批中
			url = Wzb.getDisUrl('module', 'JsonMod.study.StudyModule', 'cmd',
					'get_pending_waiting_cos');
		} else if (activetab === 2) {// 学习中
			url = Wzb.getDisUrl('module', 'JsonMod.study.StudyModule', 'cmd',
					'get_my_studying_cos');
		} else if (activetab === 3) {// 已结束
			url = Wzb.getDisUrl('module', 'JsonMod.study.StudyModule', 'cmd',
					'get_my_hist_cos');
		} else {
			alert('Please give the absolute activeTabIndex!');
			return;
		}
		url = Wzb.setUrlParam('type', type, url);// must set the type='COS' | 'EXAM'
		if (usr_ent_id !== undefined && usr_ent_id !== '') {
			url = Wzb.setUrlParam('usr_ent_id', usr_ent_id, url);
		}
		return url;
	},
	getHelp : function(event, toolEl, panel) {
		var panel_id_var = panel.id;
		if(panel_id_var !== undefined && panel_id_var !== null && panel_id_var.indexOf('defined_project_') === 0){
			panel_id_var = 'defined_project';
		}
		var helpPanel = new Wzb.Panel({
			border : false,
			autoLoad : Wzb.cur_lang + '/' + panel_id_var + '.htm'
		});
		var extendCfg = {
			height : 500,
			y : Ext.getDom('container').offsetY
		}
		Wzb.menuPopWin(Wzb.l('222'), helpPanel, extendCfg);
	},
	getPanelTools : function() {
		var toolArr = [];
		var helpObj = {
			id : 'help',
			handler : Wzb.getHelp
		};
		toolArr.push(helpObj);
		return toolArr;
	},
	workflow : {
		getProcessObj : function(workflowData, process_id) {
			var processObj = null;
			var processData = workflowData['workflow']['process'];
			if(processData !== null && Number(processData['id']) === process_id) {
				processObj = {
					id : processData['id'],
					name : processData['name'],
					type : processData['type'],
					status : processData['status']
				};
			}
			return processObj;
		},
		getStatusObj : function(workflowData, process_id, status_id) {
			var statusObj = null;
			var processObj = Wzb.workflow.getProcessObj(workflowData, process_id);
			if(processObj !== null) {
				var statusData = processObj.status;
				if(statusData !== null) {
					for(var i=0; i<statusData.length; i++) {
						var status_id_tmp = Number(statusData[i]['id']);
						if(status_id_tmp === status_id) {
							statusObj = {
								id : statusData[i]['id'],
								name : statusData[i]['name'],
								action : statusData[i]['action']
							};
							break;
						}
					}
				}
			}
			return statusObj;
		},
		getActionObj : function(workflowData, process_id, status_id, action_id) {
			var actionObj = null;
			var processObj = Wzb.workflow.getProcessObj(workflowData, process_id);
			if(processObj !== null) {
				var statusObj = Wzb.workflow.getStatusObj(workflowData, process_id, status_id);
				if(statusObj !== null) {
					var actionData = statusObj.action;
					if(actionData != null) {
						for(var i=0; i<actionData.length; i++) {
							var action_id_tmp = Number(actionData[i]['id']);
							if(action_id_tmp === action_id) {
								actionObj = {
									id : actionData[i]['id'],
									name : actionData[i]['name'],
									verb : actionData[i]['verb'],
									next_status : actionData[i]['next_status'],
									access : actionData[i]['access']
								}
								break;
							}
						}
					}
				}
			}
			return actionObj;
		},
		getActnHistoryFr : function(workflowData, process_id, status_id) {
			var fr = '';
			var statusObj = Wzb.workflow.getStatusObj(workflowData, process_id, status_id);
			if(statusObj !== null) {
				fr = statusObj.name;
			}
			return fr;
		},
		getActnHistoryTo : function(workflowData, process_id, status_id, action_id) {
			var to = '';
			var actionObj = Wzb.workflow.getActionObj(workflowData, process_id, status_id, action_id);
			if(actionObj !== null) {
				var toStatusObj = Wzb.workflow.getStatusObj(workflowData, process_id, Number(actionObj.next_status));
				if(toStatusObj !== null) {
					to = toStatusObj.name
				}
			}
			return to;
		},
		getActnHistoryVerb : function(workflowData, process_id, status_id, action_id) {
			var verb = '';
			var actionObj = Wzb.workflow.getActionObj(workflowData, process_id, status_id, action_id);
			if(actionObj !== null) {
				verb = actionObj.verb;
			}
			return verb;
		}
	}
};

Ext.reg(Ext.Panel.xtype, Wzb.Panel);
Ext.reg(Ext.grid.GridPanel.xtype, Wzb.GridPanel);
Ext.reg(Ext.TabPanel.xtype, Wzb.TabPanel);
Ext.reg(Ext.form.FormPanel.xtype, Wzb.FormPanel);
Ext.reg(Ext.tree.TreePanel.xtype, Wzb.TreePanel);
Ext.reg(Ext.form.ComboBox.xtype, Wzb.ComboBox);
Ext.reg(Ext.Window.xtype, Wzb.Window);
