Wzb = {
	name : 'LearnNow',
	version : '5.5',
	img_path : '/wb_image/',
	time_format_ymd : 'yyyy-MM-dd',
	time_format_ymdhm : 'y-MM-dd HH:mm',
	time_format_ymdhms : 'y-MM-dd HH:mm:ss',
	time_format_date : 'yy-MM-dd',
	time_format_hm : 'HH:mm',
	time_format_hms : 'HH:mm:ss',
	min_timestamp : '1753-01-01 00:00:00.000',
	max_timestamp : '9999-12-31 23:59:59.000',
	util_item_type_classroom : 'CLASSROOM',
	util_item_type_selfstudy : 'SELFSTUDY',
	util_item_type_ode : 'ODE',
	util_item_type_ede : 'EDE',
	util_item_type_blended : 'BLENDED',
	util_item_type_exam : 'EXAM',
	util_item_type_book : 'BOOK',
	util_item_type_website : 'WEBSITE',
	util_item_type_audiovideo : 'AUDIOVIDEO',
	util_item_type_integrated : 'INTEGRATED',
	uri_qdb : wb_utils_servlet_url,
	uri_ae : wb_utils_ae_servlet_url,
	uri_dis : wb_utils_disp_servlet_url,
	home_url : wb_utils_app_base+'app/home/content',
	getServletUrl : function(uri, argv) {
		var url = uri;
		for (var i = 0; i < argv.length; i = i + 2) {
			var sep = '&';
			if (i === 0) {
				sep = '?';
			}
			url += sep + encodeURIComponent(argv[i]) + '=' + encodeURIComponent(argv[i + 1]);
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
	openWin : function(win_url, win_name, is_full_screen, str_feature) {
		var win_obj = null;
		if (str_feature === undefined || str_feature === null || str_feature.length === 0) {
			str_feature = 'location=0,menubar=0,resizable=1,scrollbars=1,status=1,toolbar=0';
		}
		win_obj = window.open(win_url, win_name, str_feature);
		if (is_full_screen) {
			win_obj.moveTo(0, 0);
			win_obj.resizeTo(screen.availWidth, screen.availHeight);
		}
		win_obj.focus();
	},
	getCourseImage : function(itm_icon, is_large, clazz) {
		clazz = clazz ? clazz : "";
		var path = '<div class="wzb-cos-div ' + (is_large ? ' wzb-cos-large-div' : 'wzb-cos-small-div') + " " + clazz + '">';
		if (itm_icon !== undefined && itm_icon !== '') {
			path += '<img class="wzb-cos-icon ' + (is_large ? 'wzb-cos-icon-large' : 'wzb-cos-icon-small') + '" src="' + Wzb.getAbsoluteImagePath(itm_icon) + '"/>';
		} else {
			path += '<img class="wzb-cos-icon ' + (is_large ? 'wzb-cos-icon-large' : 'wzb-cos-icon-small') + ' wzb-cos-icon-default" src="'+ wb_utils_app_base + 'theme/skin1/images/cos.jpg"/>';
		}
		path += '</div>';
		return path;
	},
	goldenMan : {
		getGoldenManHtml : function(store, gldManName) {
			var result = '';
			if (store != null && store.reader != null && store.reader.jsonData != null) {
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
			var js_str = html_str.substring(html_str.indexOf(script_start) + 53, html_str.indexOf(script_end) - 2);
			var el = document.createElement('script');
			el.text = js_str;
			document.body.appendChild(el);
		},
		setOptions : function(store, obj, gld_name) {
			if (store != null) {
				var gld_obj_arr = store['goldenmanoptions'];
				if (gld_obj_arr !== undefined) {
					for (var i = 0; i < gld_obj_arr.length; i++) {
						gld_obj = gld_obj_arr[i];
						if (gld_obj !== null && gld_obj['name'] === gld_name) {
							var opt_value_arr = gld_obj['value'];
							if (obj.options.length > 0) {
								var temp = true;
								for (var k = 0; k < obj.options.length; k++) {
									for (var j = 0; j < opt_value_arr.length; j++) {
										if (obj.options[k].value == opt_value_arr[j]['id']) {
											temp = false;
											break;
										}
									}
									if (temp) {
										if (opt_value_arr[j] !== undefined) {
											obj.options.add(new Option(opt_value_arr[j]['text'], opt_value_arr[j]['id']));
										}
									}
								}
							} else {
								for (var j = 0; j < opt_value_arr.length; j++) {
									if (opt_value_arr[j] !== undefined) {
										obj.options.add(new Option(opt_value_arr[j]['text'], opt_value_arr[j]['id']));
									}
								}
							}
							break;
						}
					}
				}
			}

		}
	},
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
	// 获取URL中的参数
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

		idx1 = str_param.indexOf(name + '=');

		if (idx1 == -1) {
			return '';
		}

		idx1 = idx1 + name.length + 1;
		idx2 = str_param.indexOf('&', idx1);

		if (idx2 != -1) {
			len = idx2 - idx1;
		} else {
			len = str_param.length;
		}
		return decodeURIComponent(str_param.substr(idx1, len));
	},
	// 添加/设置参数到url中
	setUrlParam : function(name, value, url) {
		if (url) {
			idx0 = url.indexOf('?');
			if (idx0 == -1) {
				str_param = '';
			} else {
				str_param = url.substr(idx0, url.length);
			}
		} else {
			str_param = window.location.search;
		}

		idx1 = str_param.indexOf(name + '=');
		if (idx1 == -1) {
			if (str_param == '') {
				str_param = '?' + name + '=' + value;
			} else {
				str_param = str_param + '&' + name + '=' + value;
			}
		} else {
			idx1 = idx1 + name.length + 1;
			idx2 = str_param.indexOf('&', idx1);
			if (idx2 == -1) {
				str_param = str_param.substr(0, idx1) + value;
			} else {
				suffx = str_param.substr(idx2, str_param.length);
				str_param = str_param.substr(0, idx1) + value + suffx;
			}
		}

		if (url) {
			if (idx0 == -1) {
				return url + str_param;
			} else {
				return url.substr(0, idx0) + str_param;
			}
		} else {
			return window.location.pathname + str_param;
		}
	},
	getHtmImgSrc : function(img_src) {
		return 'src="' + img_src + '"';
	},
	getFolderImagePath : function() {
		return Wzb.getCommonImagePath('tree/images/default/folder.png');
	},
	getRelativeImagePath : function(img_name) {
		return Wzb.genImagePath(img_name);
	},
	getAbsoluteImagePath : function(img_name) {
		if (img_name.toLowerCase().indexOf('http') == 0) {
			return img_name;
		}else if (img_name.toLowerCase().indexOf('/') == 0) {
			return img_name;
		} else if (img_name.indexOf('../') >= 0) {
			img_name = wb_utils_app_base + img_name.replace('../', '');
		} else {
			img_name = wb_utils_app_base + "/" + img_name;
		}
		img_name = img_name.replace("//", "/");
		return img_name;
	},
	genImagePath : function(img_name) {
		if (img_name.toLowerCase().indexOf('http') == 0) {
			return img_name;
		}
		var img_path = '';
		for (var i = 1; i < arguments.length; i++) {
			if (arguments[i].lastIndexOf('/') != arguments[i].length - 1) {
				arguments[i] += '/';
			}
			img_path += arguments[i];
		}
		img_path += img_name;
		img_path  = wb_utils_app_base + img_path;
		img_path = img_path.replace("//", "/");
		return img_path;
	},
	getCommonImagePath : function(img_name) {
		return Wzb.genImagePath(img_name, Wzb.img_path);
	},
	getNumberImg : function(num) {
		var className = "wzb-img-num-" + num;
		return '<div class="' + className + '">&nbsp;</div>';
	},
	getLearningStatus : function(ats_cov_status, status, itm_ref_ind) {
		if (itm_ref_ind !== undefined && itm_ref_ind) { // 参考类的状态应显示为"--"
			return '--';
		} else {
			if (ats_cov_status == 'C' && status == 'has_completed') {
				return getLabel('372');
			} else if (ats_cov_status == 'I' && status == 'has_enroll') {
				return getLabel('371');
			} else if (ats_cov_status == 'F' && status == 'can_not_enroll') {
				return getLabel('373');
			} else if (ats_cov_status == 'F' && status == 'open_enroll') {
				return getLabel('373');
			} else if (ats_cov_status == 'F' && status == 'next_enroll') {
				return getLabel('373');
			} else if (ats_cov_status == 'W' && status == 'can_not_enroll') {
				return getLabel('374');
			} else if (ats_cov_status == 'W' && status == 'open_enroll') {
				return getLabel('374');
			} else if (ats_cov_status == 'W' && status == 'next_enroll') {
				return getLabel('374');
			} else if ((ats_cov_status || ats_cov_status == undefined || ats_cov_status == '') && status == 'has_enroll') {
				return getLabel('371');
			} else if ((ats_cov_status || ats_cov_status == undefined || ats_cov_status == '') && status == 'open_enroll') {
				return getLabel('94');
			} else if ((ats_cov_status || ats_cov_status == undefined || ats_cov_status == '') && status == 'next_enroll') {
				return getLabel('94');
			} else if ((ats_cov_status || ats_cov_status == undefined || ats_cov_status == '') && status == 'can_not_enroll') {
				return getLabel('94');
			} else {
				return '';
			}
		}
	},
	validateFileType : function(path_name, types) {
		var ret = false;
		if (path_name !== undefined && path_name.length > 0) {
			if (types === undefined || types === '' || !types.length) {
				types = [ 'jpg', 'gif', 'png' ];
			}

			var str = '';
			var len = types.length;
			for (var i = 0; i < len; i++) {
				if (path_name.substring(path_name.lastIndexOf('.') + 1).toLowerCase() === types[i]) {
					ret = true;
				}
				str += types[i];
				if (i !== len - 1) {
					str += ', ';
				}
			}
			if (ret === false) {
				alert(getLabel('656') + ': ' + str);
			}
		} else {
			alert(getLabel('496'));
		}
		return ret;
	},
	unescapeHtmlLineFeed : function(content) {
		var val = '';
		if (content !== null && content !== undefined) {
			val = content.replace(/\n/g, '<br>');
		}
		return val;
	},
	workflow : {
		getProcessObj : function(workflowData, process_id) {
			var processObj = null;
			var processData = workflowData['workflow']['process'];
			if (processData != "undefined")
				if (processData !== null && Number(processData['id']) === process_id) {
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
			if (processObj !== null) {
				var statusData = processObj.status;
				if (statusData !== null) {
					for (var i = 0; i < statusData.length; i++) {
						var status_id_tmp = Number(statusData[i]['id']);
						if (status_id_tmp === status_id) {
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
			if (processObj !== null) {
				var statusObj = Wzb.workflow.getStatusObj(workflowData, process_id, status_id);
				if (statusObj !== null) {
					var actionData = statusObj.action;
					if (actionData != null) {
						for (var i = 0; i < actionData.length; i++) {
							var action_id_tmp = Number(actionData[i]['id']);
							if (action_id_tmp === action_id) {
								actionObj = {
									id : actionData[i]['id'],
									name : actionData[i]['name'],
									verb : actionData[i]['verb'],
									next_status : actionData[i]['next_status'],
									access : actionData[i]['access']
								};
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
			if (statusObj !== null) {
				fr = statusObj.name;
			}
			return fr;
		},
		getActnHistoryTo : function(workflowData, process_id, status_id, action_id) {
			var to = '';
			var actionObj = Wzb.workflow.getActionObj(workflowData, process_id, status_id, action_id);
			if (actionObj !== null) {
				var toStatusObj = Wzb.workflow.getStatusObj(workflowData, process_id, Number(actionObj.next_status));
				if (toStatusObj !== null) {
					to = toStatusObj.name;
				}
			}
			return to;
		},
		getActnHistoryVerb : function(workflowData, process_id, status_id, action_id) {
			var verb = '';
			var actionObj = Wzb.workflow.getActionObj(workflowData, process_id, status_id, action_id);
			if (actionObj !== null) {
				verb = actionObj.verb;
			}
			return verb;
		}
	},
	displayTime : function(input, format) {
		if (format === undefined || format === '') {
			format = Wzb.time_format_ymd;
		}
		var dis_time;
		if (input !== undefined && input !== '') {
			input = Wzb.formatTimeStr(input);
			var dt = new Date();
			dt = Wzb.parseDate(input, 'yyyy-MM-dd HH:mm:ss.u');
			dis_time = Wzb.formatDate(dt, format);
		} else {
			dis_time = '';
		}
		return dis_time;
	},
	formatTimeStr : function(input) {
		var tmp = input.split('.');
		for (!tmp[1] ? tmp[1] = '' : ''; tmp[1].length < 3;) {
			tmp[1] += '0';
		}
		input = tmp[0] + '.' + tmp[1].substr(0, 3);
		return input;
	},
	displayTimeDefValue : function(input, format, def_value) {
		var dis_time = Wzb.displayTime(input, format);
		if (dis_time === undefined || dis_time === '') {
			dis_time = def_value;
		}
		return dis_time;
	},
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
	getCustomItemType : function(itm_blend_ind, itm_create_run_ind, itm_run_ind, itm_ref_ind, itm_integrated_ind) {
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
	MONTH_NAMES : new Array('January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December', 'Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct',
			'Nov', 'Dec'),
	DAY_NAMES : new Array('Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'),
	formatDate : function formatDate(date, format) {
		format = format + "";
		var result = "";
		var i_format = 0;
		var c = "";
		var token = "";
		var y = date.getFullYear() + "";
		var M = date.getMonth() + 1;
		var d = date.getDate();
		var E = date.getDay();
		var H = date.getHours();
		var m = date.getMinutes();
		var s = date.getSeconds();
		var u = date.getMilliseconds();
		var yyyy, yy, MMM, MM, dd, hh, h, mm, ss, ampm, HH, H, KK, K, kk, k;
		// Convert real date parts into formatted versions
		var value = {};
		if (y.length < 4) {
			y = "" + (y - 0 + 1900);
		}
		value["y"] = "" + y;
		value["yyyy"] = "" + y;
		value["yy"] = y.substring(2, 4);
		value["M"] = M;
		value["MM"] = Wzb.LZ(M);
		value["MMM"] = Wzb.MONTH_NAMES[M - 1];
		value["NNN"] = Wzb.MONTH_NAMES[M + 11];
		value["d"] = d;
		value["dd"] = Wzb.LZ(d);
		value["E"] = Wzb.DAY_NAMES[E + 7];
		value["EE"] = Wzb.DAY_NAMES[E];
		value["H"] = H;
		value["HH"] = Wzb.LZ(H);
		if (H == 0) {
			value["h"] = 12;
		} else if (H > 12) {
			value["h"] = H - 12;
		} else {
			value["h"] = H;
		}
		value["hh"] = Wzb.LZ(value["h"]);
		if (H > 11) {
			value["K"] = H - 12;
		} else {
			value["K"] = H;
		}
		value["k"] = H + 1;
		value["KK"] = Wzb.LZ(value["K"]);
		value["kk"] = Wzb.LZ(value["k"]);
		if (H > 11) {
			value["a"] = "PM";
		} else {
			value["a"] = "AM";
		}
		value["m"] = m;
		value["mm"] = Wzb.LZ(m);
		value["s"] = s;
		value["ss"] = Wzb.LZ(s);
		value["u"] = u;
		while (i_format < format.length) {
			c = format.charAt(i_format);
			token = "";
			while ((format.charAt(i_format) == c) && (i_format < format.length)) {
				token += format.charAt(i_format++);
			}
			if (value[token] != null) {
				result += value[token];
			} else {
				result += token;
			}
		}
		return result;
	},
	parseDate : function(val, format) {
		val = val + "";
		format = format + "";
		var i_val = 0;
		var i_format = 0;
		var c = "";
		var token = "";
		var token2 = "";
		var x, y;
		var now = new Date();
		var year = now.getFullYear();
		var month = now.getMonth() + 1;
		var date = 1;
		var hh = now.getHours();
		var mm = now.getMinutes();
		var ss = now.getSeconds();
		var ampm = "";

		while (i_format < format.length) {
			// Get next token from format string
			c = format.charAt(i_format);
			token = "";
			while ((format.charAt(i_format) == c) && (i_format < format.length)) {
				token += format.charAt(i_format++);
			}
			// Extract contents of value based on format token
			if (token == "yyyy" || token == "yy" || token == "y") {
				if (token == "yyyy") {
					x = 4;
					y = 4;
				}
				if (token == "yy") {
					x = 2;
					y = 2;
				}
				if (token == "y") {
					x = 2;
					y = 4;
				}
				year = Wzb.getInt(val, i_val, x, y);
				if (year == null) {
					return 0;
				}
				i_val += year.length;
				if (year.length == 2) {
					if (year > 70) {
						year = 1900 + (year - 0);
					} else {
						year = 2000 + (year - 0);
					}
				}
			} else if (token == "MMM" || token == "NNN") {
				month = 0;
				for (var i = 0; i < Wzb.MONTH_NAMES.length; i++) {
					var month_name = Wzb.MONTH_NAMES[i];
					if (val.substring(i_val, i_val + month_name.length).toLowerCase() == month_name.toLowerCase()) {
						if (token == "MMM" || (token == "NNN" && i > 11)) {
							month = i + 1;
							if (month > 12) {
								month -= 12;
							}
							i_val += month_name.length;
							break;
						}
					}
				}
				if ((month < 1) || (month > 12)) {
					return 0;
				}
			} else if (token == "EE" || token == "E") {
				for (var i = 0; i < Wzb.DAY_NAMES.length; i++) {
					var day_name = Wzb.DAY_NAMES[i];
					if (val.substring(i_val, i_val + day_name.length).toLowerCase() == day_name.toLowerCase()) {
						i_val += day_name.length;
						break;
					}
				}
			} else if (token == "MM" || token == "M") {
				month = Wzb.getInt(val, i_val, token.length, 2);
				if (month == null || (month < 1) || (month > 12)) {
					return 0;
				}
				i_val += month.length;
			} else if (token == "dd" || token == "d") {
				date = Wzb.getInt(val, i_val, token.length, 2);
				if (date == null || (date < 1) || (date > 31)) {
					return 0;
				}
				i_val += date.length;
			} else if (token == "hh" || token == "h") {
				hh = Wzb.getInt(val, i_val, token.length, 2);
				if (hh == null || (hh < 1) || (hh > 12)) {
					return 0;
				}
				i_val += hh.length;
			} else if (token == "HH" || token == "H") {
				hh = Wzb.getInt(val, i_val, token.length, 2);
				if (hh == null || (hh < 0) || (hh > 23)) {
					return 0;
				}
				i_val += hh.length;
			} else if (token == "KK" || token == "K") {
				hh = Wzb.getInt(val, i_val, token.length, 2);
				if (hh == null || (hh < 0) || (hh > 11)) {
					return 0;
				}
				i_val += hh.length;
			} else if (token == "kk" || token == "k") {
				hh = Wzb.getInt(val, i_val, token.length, 2);
				if (hh == null || (hh < 1) || (hh > 24)) {
					return 0;
				}
				i_val += hh.length;
				hh--;
			} else if (token == "mm" || token == "m") {
				mm = Wzb.getInt(val, i_val, token.length, 2);
				if (mm == null || (mm < 0) || (mm > 59)) {
					return 0;
				}
				i_val += mm.length;
			} else if (token == "ss" || token == "s") {
				ss = Wzb.getInt(val, i_val, token.length, 2);
				if (ss == null || (ss < 0) || (ss > 59)) {
					return 0;
				}
				i_val += ss.length;
			} else if (token == "u") {
				u = Wzb.getInt(val, i_val, token.length, 3);
				if (u == null || (u < 0) || (u > 999)) {
					return 0;
				}
				i_val += u.length;
			} else if (token == "a") {
				if (val.substring(i_val, i_val + 2).toLowerCase() == "am") {
					ampm = "AM";
				} else if (val.substring(i_val, i_val + 2).toLowerCase() == "pm") {
					ampm = "PM";
				} else {
					return 0;
				}
				i_val += 2;
			} else {
				if (val.substring(i_val, i_val + token.length) != token) {
					return 0;
				} else {
					i_val += token.length;
				}
			}
		}
		// If there are any trailing characters left in the value, it doesn't
		// match
		if (i_val != val.length) {
			return 0;
		}
		// Is date valid for month?
		if (month == 2) {
			// Check for leap year
			if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) { // leap
				// year
				if (date > 29) {
					return 0;
				}
			} else {
				if (date > 28) {
					return 0;
				}
			}
		}
		if ((month == 4) || (month == 6) || (month == 9) || (month == 11)) {
			if (date > 30) {
				return 0;
			}
		}
		// Correct hours value
		if (hh < 12 && ampm == "PM") {
			hh = hh - 0 + 12;
		} else if (hh > 11 && ampm == "AM") {
			hh -= 12;
		}
		return new Date(year, month - 1, date, hh, mm, ss);
	},
	LZ : function(x) {
		return (x < 0 || x > 9 ? "" : "0") + x
	},
	isInteger : function(val) {
		return (new RegExp(/^\d+$/).test(val));
	},
	getInt : function(str, i, minlength, maxlength) {
		for (var x = maxlength; x >= minlength; x--) {
			var token = str.substring(i, i + x);
			if (token.length < minlength) {
				return null;
			}
			if (Wzb.isInteger(token)) {
				return token;
			}
		}
		return null;
	},
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
	htmlEncode : function(value) {
		return !value ? value : String(value).replace(/&/g, "&amp;").replace(/>/g, "&gt;").replace(/</g, "&lt;").replace(/"/g, "&quot;");
	},
	htmlDecode : function(value) {
		return !value ? value : String(value).replace(/&amp;/g, "&").replace(/&gt;/g, ">").replace(/&lt;/g, "<").replace(/&quot;/g, '"').replace(/&#34;/g,'"');
	},
	getButton : function(data, usr_ent_id, type) {
		
		var cancelLabe = "";
		if(type && type == "exam"){
			cancelLabe = fetchLabel("label_core_training_management_344");
		}else{
			cancelLabe = getLabel("LN079");
		}
		
		$.templates({
			btnIconTemplate : '<a class="btn wzb-btn-orange wzb-btn-big margin-right15" href="{{>handler}}" title="{{>label}}" >{{>label}}</a>',
			btnNoUseTemplate : '<a class="btn wzb-btn-orange wzb-btn-big margin-right15" href="javascript:;" title="{{>label}}" >{{>label}}</a>'
		});
		var btn = data.btn;
		if (btn == 0) {
		} else if (btn == 1) {
			if(!data.canApp) return;	//已经报名了
			return $.render.btnIconTemplate({
				label : getLabel('459'),
				handler : 'javascript: Wzb.enrol("' + data.itm_id + '", "' + data.itm_type + '")'
			});
		} else if (btn == 2) {
			//开始学习
			var html = $.render.btnIconTemplate({
				label : cancelLabe,
				handler : 'javascript: Wzb.cancel_enrol("' + data.app_id + '");'
			});
			return html;
		} else if (btn == 3) {
			//开始学习
			return;
			url = "";
			if(data.itm_type == 'VIDEO') {
				url = wb_utils_app_base + 'app/course/video_course_main?tkh_id=' + data.app_tkh_id + '&res_id=' + data.app_cos_id;
			} else {
				url = wb_utils_app_base + 'app/course/course_home?itm_id=' + data.itm_id + '&tkh_id=' + data.app_tkh_id + '&res_id=' + data.app_cos_id;
			}
			return $.render.btnIconTemplate({
				label : getLabel('LN112'),
				handler : 'javascript: location ="' + wb_utils_app_base + url + '"'
			});
		} else if (btn == 4) {
			return $.render.btnIconTemplate({
				label : getLabel('459'),
				handler : 'javascript: Wzb.enrol("' + data.itm_id + '", "' + data.itm_type + '")'
			});
		} else if (btn == 5) {
			return $.render.btnIconTemplate({
						label : cancelLabe,
						handler : 'javascript: Wzb.cancel_enrol("' + data.app_id + '");'
				   });
		} else if (btn == 6) {
			return $.render.btnIconTemplate({
					  label : cancelLabe,
				      handler : 'javascript: Wzb.cancel_enrol("' + data.app_id + '");'
				   });
		} else if (btn == 7) {
			return $.render.btnNoUseTemplate({
				label : getLabel('459')				
			});
		} else if (btn == -1 && data.appnTimeValide){
			return $.render.btnNoUseTemplate({
				label : getLabel('459')				
			});			
		}
		return "";
	},
	cancel_enrol : function(app_id) {
		$.ajax({
			type : 'POST',
			dataType : 'json',
			url : wb_utils_app_base + 'app/application/cancel?app_id=' + app_id,
			success : function(data) {
				if (typeof dataTable == 'object' && typeof dataTable.reloadTable == 'function') {
					dataTable.reloadTable();
				} else {
					var url = window.location.href;
					if(url != undefined && url.indexOf("?") >-1 ){
						url = url.substring(0,url.indexOf("?"));
						window.location.href = url;
					} else {
						window.location.reload(true);
					}
				}
			}
		});
	},
	enrol : function(itm_id, itm_type) {
		
		if(!window.enrolFlag){//防止重复提交
			
			window.enrolFlag = true;
			
			$.templates({
				textTemplate : '{{>text}}',
				btnIconTemplate : '<a class="swop_bg" href="{{>handler}}" title="{{>label}}" >{{>label}}</a>'
			});
			if (itm_id > 0) {
				$.ajax({
					type : 'POST',
					dataType : 'json',
					async : false,
					url : wb_utils_app_base + 'app/application/app?itm_id=' + itm_id,
					success : function(data) {
						if (data.status == -1 && data.msg) {
							Dialog.alert(data.msg);
							window.enrolFlag = false;
						}else{
							var url = window.location.href;
							if(url != undefined && url.indexOf("?") >-1 ){
								url = url.substring(0,url.indexOf("?"));
								window.location.href = url;
							} else {
								window.location.reload();
							}
							window.enrolFlag = false;
						}
					}
				});
			}
		
		}
		
	},
	subString : function(str, len) {
		len = len == null ? 10 : len;
		if (str != "" && str.length > len) {
			str = str.substring(0, len) + "...";
		}
		return str;
	}
}

Date.prototype.formatDate = function(format) {
	return Wzb.formatDate(this, format);
};

Date.prototype.parseDate = function(value, format) {
	return Wzb.parseDate(value, format);
}

String.prototype.parseDate = function(value, format) {
	return Wzb.parseDate(value, format);
}

String.prototype.trim = function() {
	return this.replace(/(^\s*)|(\s*$)|\r|\n/g, "");
}

function beforeContent(content) {
	while ((index = content.indexOf("\n")) != -1)
		content = content.substring(0, index - 1) + "<br/>" + content.substring(index + 1);
	while ((index = content.indexOf("&")) != -1)
		content = content.substring(0, index) + "$amp/$" + content.substring(index + 1);
	while ((index = content.indexOf("#")) != -1)
		content = content.substring(0, index) + "$sign/$" + content.substring(index + 1);
	while ((index = content.indexOf("%")) != -1)
		content = content.substring(0, index) + "$mod_/$" + content.substring(index + 1);
	while ((index = content.indexOf("+")) != -1)
		content = content.substring(0, index) + "$add/$" + content.substring(index + 1);
	while ((index = content.indexOf("'")) != -1)
		content = content.substring(0, index) + "$single/$" + content.substring(index + 1);
	while ((index = content.indexOf('"')) != -1)
		content = content.substring(0, index) + "$double/$" + content.substring(index + 1);
	return content;
}

function afterContent(content, noReplace) {
	if (!noReplace) {
		while ((index = content.indexOf("<br/>")) != -1)
			content = content.substring(0, index) + "\n" + content.substring(index + 5);
	}
	while ((index = content.indexOf("$amp/$")) != -1)
		content = content.substring(0, index) + "&" + content.substring(index + 6);
	while ((index = content.indexOf("$sign/$")) != -1)
		content = content.substring(0, index) + "#" + content.substring(index + 7);
	while ((index = content.indexOf("$mod_/$")) != -1)
		content = content.substring(0, index) + "%" + content.substring(index + 7);
	while ((index = content.indexOf("$add/$")) != -1)
		content = content.substring(0, index) + "+" + content.substring(index + 6);
	while ((index = content.indexOf("$single/$")) != -1)
		content = content.substring(0, index) + "'" + content.substring(index + 9);
	while ((index = content.indexOf("$double/$")) != -1)
		content = content.substring(0, index) + '"' + content.substring(index + 9);
	return content;
}

function panel_toggle(obj, panel_id) {
	if ($(obj).attr("class").indexOf("up") != -1) {
		$(obj).removeClass("wzb-panel-toggle-up").addClass("wzb-panel-toggle-down");
	} else {
		$(obj).removeClass("wzb-panel-toggle-down").addClass("wzb-panel-toggle-up");
	}

	$('div.wzb-panel-content', $('#' + panel_id)).parents('tr').toggle(200);
}

function purchaseItm(purchase_api_url, usr_ent_id, itm_id, itm_title, itm_fee, itm_icon) {
	if (itm_icon == null || itm_icon == 'undefined' || itm_icon == '') {
		itm_icon = '/theme/skin1/images/cos.jpg'
	} else {
		itm_icon = '/' + itm_icon;
	}
	url = purchase_api_url + 'cart/add';// ?name='+itm_title+'&img='+encodeURIComponent(itm_icon)+'&num='+1+'&price='+itm_fee+'&itm_id='+itm_id+'&type=APP'+'&usr_ent_id='+usr_ent_id;
	// url =
	// '/app/purchase/cart?name='+itm_title+'&img='+itm_icon+'&num='+1+'&price='+itm_fee+'&itm_id='+itm_id+'&type=APP'+'&usr_ent_id='+usr_ent_id;
	// alert(url)
	// alert("usr_ent_id:"+usr_ent_id);
	// alert("itm_id:"+itm_id);
	// alert("itm_title:"+itm_title);
	// alert("itm_fee:"+itm_fee);
	// alert("itm_icon:"+itm_icon);serializeArray();

	$.ajax({
		url : url,
		dataType : 'json',
		data : {
			name : itm_title,
			img : itm_icon,
			num : 1,
			price : itm_fee,
			itm_id : itm_id,
			type : 'APP',
			usr_ent_id : usr_ent_id
		},
		// data: { name:'title', img:'/theme/skin1/images/cos.jpg' , num:1,
		// price:0.1, itm_id:1, type:'APP', usr_ent_id:10},
		success : function(data) {
			if (data['status'] == 0) {
				alert(data['msg']);
			} else {
				alert(data['msg']);
			}
		},
		error : function(req, options) {
			alert('ERROR!')
		},
		type : 'POST'
	});

}

function purchaseItmDrect(purchase_api_url, usr_ent_id, itm_id, itm_title, itm_fee, itm_icon) {
	if (itm_icon == null || itm_icon == 'undefined' || itm_icon == '') {
		itm_icon = '/theme/skin1/images/cos.jpg'
	} else {
		itm_icon = '/' + itm_icon;
	}
	url = wb_utils_app_base + 'app/purchase/purchase_itm_pre?' + 'name=' + itm_title + '&img=' + itm_icon + '&num=' + 1 + '&price=' + itm_fee + '&itm_id=' + itm_id + '&type=APP' + '&usr_ent_id=' + usr_ent_id;
	var win_obj = null;

	str_feature = 'width=1100,height=500,location=0,menubar=0,resizable=1,scrollbars=1,status=1,toolbar=0';
	win_obj = window.open(url, 'my_purchase' + usr_ent_id, str_feature)
	win_obj.moveTo(0, 0);

	win_obj.focus();

}
function purchaseCar(usr_ent_id, purchase_api_url) {
	url = purchase_api_url + 'cart/lists/' + usr_ent_id;

	var win_obj = null;

	str_feature = 'width=1100,height=500,location=0,menubar=0,resizable=1,scrollbars=1,status=1,toolbar=0';
	win_obj = window.open(url, 'my_purchaseCar_' + usr_ent_id, str_feature)
	win_obj.moveTo(0, 0);

	win_obj.focus();
}

function replaceScript(str) {
	return str.replace(/(&lt;script)/g, "&amp;lt;script").replace(/&lt;\/script/, "&amp;lt;/script").replace(/(&lt;SCRIPT)/g, "&amp;lt;SCRIPT").replace(/&lt;\/SCRIPT/, "&amp;lt;/SCRIPT")
}