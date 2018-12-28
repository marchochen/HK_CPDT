cwn = {
	getLabel : 	function(lab_name, datas) {
		var result = '!!!' + lab_name;
		if (text_label.hasOwnProperty(lab_name)) {
			var count = 0;
			result = text_label[lab_name].replace(/\$data/g, function(str, index) {
				if (datas === undefined || datas === null) {
					return result;
				}
				return datas[count++];
				});
		}
		return result;
	},
	getItemType : function(type, itm_exam_ind){
		courseType = type.toLowerCase();
		if(itm_exam_ind > 0) {
			if ('classroom' == courseType) {
				return cwn.getLabel("label_core_training_management_36");
			} else if ("selfstudy" == courseType) {
				return cwn.getLabel("label_core_training_management_12");	
			}
		} else {
			if ('classroom' == courseType) {
				return cwn.getLabel("label_core_training_management_13");
			} else if ("integrated" == courseType) {
				return cwn.getLabel("label_core_training_management_14");		
			} else if ("selfstudy" == courseType) {
				return cwn.getLabel("label_core_training_management_11");	
			} else if("audiovideo" == courseType) {
				return cwn.getLabel("label_core_training_management_81");	
			}
		}
		return courseType;
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
	getStar : function(score){
		var str = '';
		if (score) {
			var value = Math.round(score);
			for ( var i = 1; i <= value; i++) {
				str += '<img src="/static/images/wzb-star-yes.png" alt=""/>';
			}
			for ( var i = 1; i <= 5 - value; i++) {
				str += '<img src="/static/images/wzb-star-no.png" alt=""/>';
			}
		} else {
			for ( var i = 1; i <= 5; i++) {
				str += '<img src="/static/images/wzb-star-no.png" alt=""/>';
			}
		}
		return str;
	},
	getInstructorLevel : function(level){
	    if(level == 'J') {
	        return cwn.getLabel('label_core_basic_data_management_5');
	    } else if(level == 'M') {
	        return cwn.getLabel('label_core_basic_data_management_6');
	    } else if(level == 'S') {
	        return cwn.getLabel('label_core_basic_data_management_7');
	    } else if(level == 'D') {
	        return cwn.getLabel('label_core_basic_data_management_8');
	    } 
	}
		
}

function fetchLabel(lab_name, datas) {
	return cwn.getLabel(lab_name, datas);
}

function getAppStatusStr(appStatus, type,trainingStatus){
	if(!appStatus) return "";
	appStatus = appStatus.toLowerCase();
	
	if(('i' == appStatus || 'c' == appStatus) && '[IFCP]' == trainingStatus && (type=='VOD' || type=='RDG' || type=='REF')) {
		return fetchLabel("status_viewed");	//已查閱
	}else if("i" == appStatus && (type=='RDG'  ||type=='FAQ' ||type=='GLO'||type=='REF')) {
		return fetchLabel("status_viewed");	//已查閱
	}else if("i" == appStatus || "n" == appStatus) {
		return fetchLabel("status_inprogress");	//进行中
	}else if("c" == appStatus && type=='SVY') {
		return fetchLabel("personal_submit_ok");	//已提交
	} else if("c" == appStatus) {
		return fetchLabel("status_completed");	//已完成
	} else if("f" == appStatus && (type=='TST' ||type=='DXT' ||type=='ASS')){
		return fetchLabel("status_flunk");	//fail,不合格
	} else if("f" == appStatus){
		return fetchLabel("status_fail");	//fail,未完成
	}else if("p" == appStatus && (type=='TST' ||type=='DXT' ||type=='ASS')){
		return fetchLabel("status_passed");	//pass,合格
	} else if("p" == appStatus){
		return fetchLabel("status_pass");	//pass,已通过
	} else if("w" == appStatus){
		return fetchLabel("status_withdrawn");	//已放弃
	} else if("notapp" == appStatus) {
		return fetchLabel("status_notapp");	//未报名
	} else if("pending" == appStatus) {
		return fetchLabel("status_pending");	//审批中
	} else if("admitted" == appStatus) {
		return fetchLabel("status_admitted");	//已报名
	}else if("rejected" == appStatus) {
		return fetchLabel("lab_APP_STATUS_REAPPROVAL");	//已报名
	}else if("waiting" == appStatus){
		return fetchLabel("status_waiting");	//等待队列
	}
	
	return fetchLabel("status_notapp");
}

function getCourseTypeStr(courseType) {
	courseType = courseType.toLowerCase();
	if ('classroom' == courseType) {
		return fetchLabel("classroom");
	} else if ("integrated" == courseType) {
		return fetchLabel("integrated");		
	} else if ("selfstudy" == courseType) {
		return fetchLabel("selfstudy");	
	}
	return courseType;
}

function getExamTypeStr(courseType) {
	courseType = courseType.toLowerCase();
	if ('classroom' == courseType) {
		return fetchLabel("exam_classroom");
	} else if ("selfstudy" == courseType) {
		return fetchLabel("exam_selfstudy");	
	}
	return courseType;
}
//截取指定字节长度的字符串，中文占2位
function substr(str, startp, endp) {
	var i = 0;
	c = 0;
	unicode = 0;
	rstr = '';
	var len = str.length;
	var sblen = getChars(str);
	if (startp < 0) {
		startp = sblen + startp;
	}
	if (endp < 1) {
		endp = sblen + endp;// - ((str.charCodeAt(len-1) < 127) ? 1 : 2);
	}
	// 寻找起点
	for (i = 0; i < len; i++) {
		if (c >= startp) {
			break;
		}
		var unicode = str.charCodeAt(i);
		if (unicode < 127) {
			c += 1;
		} else {
			c += 2;
		}
	}
	// 开始取
	for (; i < len; i++) {
		var unicode = str.charCodeAt(i);
		if (unicode < 127) {
			c += 1;
		} else {
			c += 2;
		}
		rstr += str.charAt(i);
		if (c >= endp) {
			break;
		}
	}
	return rstr;
}

function getChars(str) {
	var i = 0;
	var c = 0.0;
	var unicode = 0;
	var len = 0;
	if (str == null || str == "") {
		return 0;
	}
	len = str.length;
	for (i = 0; i < len; i++) {
		unicode = str.charCodeAt(i);
		if (unicode < 127) { //判断是单字符还是双字符
			c += 1;
		} else { //chinese
			c += 2;
		}
	}
	return c;
}

function newDate(str) { 
	str = str.split('-'); 
	var date = new Date(); 
	date.setUTCFullYear(str[0], str[1] - 1, str[2]); 
	date.setUTCHours(0, 0, 0, 0); 
	return date; 
}

/*
 * qtip仅文本提示层调用
 * */
function cwqtipText(obj){
	 $('#textcontent a[href]').qtip({
			content: "<div><span class='wzb-ui-module-text'>"+obj+"</span></div>"
		});
}

/*
 * qtip图片文本提示层调用
 * */
function cwqtipImg(text,img){
//	$("#div[data='testtip']")
	 $("#imgcontent a[href]").qtip({
	content:"<div ><span class='wzb-qtip-wiki'>"+text+"</span><br/>"+img+"</div>",
	position:{ my: 'bottom left', at: 'top center', }
});
	}

// event  this  maxlength   限制文本框输入字符长度
function noMaxlength(e,t,maxlength)
{
	var str=$(t).val();
	var keynum;
	if(window.event) 
		{
		keynum = e.keyCode;
		}
	else if(e.which)
		{
		keynum = e.which;
		}
	if(keynum==8) 
	{
		return true;
	}
	if(getChars(str)>maxlength-1)
	{
		return false;
	}
	return true;
}

//换行调用
function replaceVlaue(str){
	if(str!=undefined && str!='')
	{
		str=str.replace(/\r\n/g,"<br/>").replace(/\r/g,"<br/>").replace(/\n/g,"<br/>").replace(/\\r?\\n?/g,"<br/>");
	}
	return str;
}

function highLightMainMenu(menuCode){
	var leftMenu = $(".mCSB_container .wzb-menu-son");
	for(var i=0; i<$(leftMenu).length; i++) {
		$(leftMenu).each(function(index,data){ 
			if($( data ).attr('data')== menuCode ){
				$(data).parent().addClass('cur');
				$(data).css("background", "#000000").css("color", "#F8F8FF");
			}
		})
	}
}

//判断是否为数字
function isNumber(value){
	if ( !/^\d*\.?\d+$/.test( value ) ) {
	    return false;
	}
	return true;
}


/*
 * qtip基本INPUT输入框可用，自定义ID和TEXT
 * */
/*$(function(){
	$('#username').qtip({
	    content: {
	        text: '<div class="wzb-ui-hint-text">用户名只能输入英文字母或者数字</div>'
	    }
	});
//	
//	$('#username').click(function(){
//		$('#password').qtip('show');
//	});
	$('#password').qtip({
	    content: {
	        text: '<div class="wzb-ui-hint-text">密码为6-12位数</div>'
	    }
	});
});*/