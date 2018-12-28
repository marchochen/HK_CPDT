submitted = false;
locked_for_single_submit = false;

//Submit ES upload file form
//function submit_que_file(){
function submit_que_file(lang){	 
    var stylesheet = 'lrn_tst_rpt_usr_header.xsl';
	if(UploadQueSet && UploadQueSet.length){
		//select last question set , and upload the file
		var CurQue = UploadQueSet[UploadQueSet.length - 1]
		UploadQueSet.length-- 
		var CurFrm = eval('document.frm' + CurQue.id)
		CurFrm.target = 'file_submit'
		CurFrm.cmd.value = 'send_tst_file'
		CurFrm.action = wb_utils_servlet_url;
		CurFrm.stylesheet.value = 'submit_que_file.xsl'//this file will trigger next que set
		CurFrm.submit();
	}else{
			//when no question set left, close the submit window and show report
			frm = window.document.frmResult
			//modify to optimize test module for learner
			var cmd = 'get_rpt_usr';
			if(frm.mod_type.value == 'TST'|| frm.mod_type.value == 'DXT'){
				cmd = 'get_rpt_usr_test'; 
                stylesheet = 'lrn_tst_rpt_usr_header.xsl';
			}
			if(frm.mod_type.value == 'TST'|| frm.mod_type.value == 'DXT'){
				window.parent.location.href = '../htm/tst_submitt_succeed.htm?lang=' + lang + '&tkh_id=' + frm.tkh_id.value;
				return;
			}
			window.location.href = wb_utils_invoke_servlet('cmd',cmd,'rpt_usr_id',frm.pgr_usr_id.value,'mod_id',frm.pgr_mod_id.value,'que_id_lst',0,'attempt_nbr','0','stylesheet',stylesheet,'tkh_id', frm.tkh_id.value)
			//window.location.href = wb_utils_invoke_servlet('cmd','get_rpt_usr','rpt_usr_id',frm.pgr_usr_id.value,'mod_id',frm.pgr_mod_id.value,'que_id_lst',0,'attempt_nbr','0','stylesheet','lrn_tst_rpt_usr_header.xsl')
	}
}

function check_es_que_file_empty(q){
	//if empty , return true, 
	var empty = true;
	if(document.layers){
	//sorry, netscape 4 not supported
		return empty;
	}if(UploadQueSet && UploadQueSet.length){
		var k=0;
			var CurFrm = eval('document.frm' + q)
			for(k=0;k<CurFrm.elements.length;k++){
				if(CurFrm.elements[k].type == 'file'){
					//alert(CurFrm.elements[k].name)
					if(CurFrm.elements[k].value != ''){
						empty = false;
						break;
					}
				}
			}
	}else{
		empty = true;
	}
	return empty;
}

//Submit form 
function CwQuestionSubmit(args) {
	
	function do_scuess(){
        //if sso login,and opener is diff domain with lms.
        var diff_domain = wb_utils_get_cookie('sso_diff_domain_' + frm.tkh_id.value);
        gen_del_cookie('sso_diff_domain_' + frm.tkh_id.value);


		if ( frm.mod_type.value != 'SVY' && frm.mod_type.value != 'EVN' ){			
			frm.pgr_used_time.value = Math.round((testDuration - timeLeft)/60)
			if(hasSubmitFile == null || !hasSubmitFile || document.layers){
				//No ES file submit, show report
				frm.url_success.value =  wb_utils_invoke_servlet('cmd','get_rpt_usr','rpt_usr_id',frm.pgr_usr_id.value,'mod_id',frm.pgr_mod_id.value,'que_id_lst',0,'attempt_nbr','0','stylesheet','lrn_tst_rpt_usr_header.xsl','tkh_id',frm.tkh_id.value)
				frm.target = '_parent'
			}else{
				//Has ES file submit, submit test and file.
				frm.target = 'file_submit';
				frm.url_success.value = '../htm/submit_que_file.htm?lang=' + frm.wb_lang.value
			}
		}else{
			frm.target = '_parent'	
			if(frm.is_cos_eval && frm.is_cos_eval.value == 'true' && frm.mod_type.value != 'EVN'){
                if (diff_domain != 'true') {
    				frm.url_success.value = '../htm/close_and_reload_window.htm?tkh_id=' + frm.tkh_id.value;
                } else {
    				frm.url_success.value = '../htm/close_and_reload_window.htm?no_reload=true&tkh_id=' + frm.tkh_id.value;
                }
			} else if(frm.mod_type.value == 'EVN') {
				frm.url_success.value = '../htm/close_and_reload_window.htm?no_reload=false&tkh_id=' + frm.tkh_id.value;
			}else{
				frm.url_success.value =  wb_utils_invoke_servlet('cmd','get_rpt_usr','rpt_usr_id',frm.pgr_usr_id.value,'mod_id',frm.pgr_mod_id.value,'que_id_lst',0,'attempt_nbr','0','stylesheet','lrn_tst_rpt_usr_header.xsl','tkh_id',frm.tkh_id.value)	
			}			
		}
		
		//中断测试
		if (frm.isTerminateExam && frm.isTerminateExam.value === 'true') {
			frm.target = '_parent';
		}
		
		frm.url_failure.value = '../htm/close_window.htm?tkh_id=' + frm.tkh_id.value;
         // modify to optimize test module for learner
		frm.method = 'post'
		if(frm.mod_type.value == 'TST'|| frm.mod_type.value == 'DXT'){
			frm.cmd.value = 'send_tst_result_test'	
		}else{
			frm.cmd.value = 'send_tst_result'
	    }	
		if( frm.pgr_usr_id.value == '' ){
			frm.pgr_usr_id.value = Wzb.getUrlParam('ent_id');
		}
		if(parent.window.opener && submitted != true && frm.mod_type.value != 'EVN'){

	        if ((parent.window.opener) && (parent.window.opener!=null) && (typeof(parent.window.opener) != 'undefined') && (!parent.window.opener.closed)) {
	        	var ua = navigator.userAgent.toLowerCase();
	        	if (diff_domain != 'true' && !((ua.indexOf("android") > -1) || ((ua.match(/iphone/i)) || (ua.match(/ipod/i)) || (ua.match(/ipad/i))) || ua.match(/Windows Phone/i) || ua.match(/BlackBerry/i) || ua.match(/webOS/i))) {
	                parent.window.opener.location.reload();
	            }
	        }


			if(frm.mod_type.value == 'TST'|| frm.mod_type.value == 'DXT'){
				//alert("not need reload parent");
			}
		}	
		submitted = true;	
		
		//these are added for the progress bar
		if ((window.parent.progress_bar) && (window.parent.progress_bar!=null)) {
			var window_name = 'test_player' + frm.pgr_usr_id.value + '_' + frm.tkh_id.value + '_' + frm.pgr_mod_id.value
			var newurl = wb_utils_invoke_servlet('cmd', 'get_prof', 'stylesheet', 'submit_result_progress.xsl', 'window_name', window_name);
			window.parent.progress_bar.location = newurl;
			frm.window_name.value = window_name;
		}
		
		//提交考试禁止掉提交按钮
		var topMenus = getClass('submit-test'); 
		for(var i=0;i < topMenus.length; i++) {
			topMenus[i].setAttribute("disabled", true);
		}
		frm.submit();
	}
	
	if(locked_for_single_submit){
		//qset.loadQuestion(currentQue); 
			
		return;
	}
	locked_for_single_submit = true;
	qset.unloadQuestion(currentQue); 
	if(document.getElementById('titlediv')!=null){
		document.getElementById('titlediv').style.display='none';
	}
	if(document.getElementById('testBottom')!=null){
		document.getElementById('testBottom').style.display='none';
	}
	if(submitted == false){
		
		
		//bypass progress tracking 
		if (document.all || document.getElementById) {
			x = window.parent.track;
			if( x != null )
			window.parent.track.unset_onunload();
			}
		
		frm = window.document.frmResult
		var n = qset.getSize();
		if(n==0) {locked_for_single_submit = false;return;}
		var error_count = 0;
		var empty_ans = 0;
		for (var i = 1; i <= n; i++){			
			que = qset.getQuestion(i);		
			que_id = que.getId();
			eval('frm.atm_int_res_id_' + i +'.value=\'' + que_id +'\'')
			eval('frm.atm_flag_' + i +'.value=\'' + que.getFlag() +'\'')
			//alert('[debug]getQuestion: ' + que.cookie_token_nm + ' / getSize: ' + qset.getSize() + ' / getId: ' +  que_id + ' / getFlag: ' + que.getFlag() + ' / getType: ' + que.interactions[0].getType())			
			var m = que.getSize();			
			
			if ( que.interactions[0].getType() == 'MT' ){						
				for ( var k = 1; k<= eval('G0' + i + '.totalTaget'); k++){				
					var ans = '';
					for(var j=0;j<que.answer_array.length;j++){
						if(que.answer_array[j]!=null){
							var sourceid=que.answer_array[j].substring(0,que.answer_array[j].indexOf('_'));
							var targetid=que.answer_array[j].substring(que.answer_array[j].indexOf('_')+1,que.answer_array[j].length);
							if(targetid==k){
								ans+=sourceid+'~';
							}
						}
					}
					eval('frm.atm_int_order_' + i +'_' + k +'.value=\'' + k +'\'')
					eval('frm.atm_response_' + i +'_' + k +'.value=\'' + ans +'\'')					
				}
			}else{				
				for (var j = 1; j <= m; j++){								
					interaction = que.getInteraction(j);					
					int_id = interaction.getId();					
					int_ans = interaction.getAnswer();		
					int_ans_ext = interaction.getAnswerExtend();
					//alert('[debug]int_ans: ' + int_ans + ' : length: ' + int_ans.length)
					if(que.interactions[0].getType() == 'ES'){
						if(hasSubmitFile == null || !hasSubmitFile){
							if(int_ans==''){
								//alert('que' + i +' not answer1')
								empty_ans++;
							} 
						}else{
						//is SubmitFile Type ES
							if(check_es_que_file_empty(i) == true){
								if(int_ans==''){
									//alert('que' + i +' not answer2')
									empty_ans++;
								} 
							}
						}
					}else if(int_ans==''){
						empty_ans++;
					}

					if( que.interactions[0].getType() == 'FB' && (frm.mod_type.value == 'SVY' || frm.mod_type.value == 'EVN') ) {
						if( int_ans.length > 1000 ) {
							Dialog.alert(wb_msg_question  + i + wb_msg_enter_less_char);
							//if(forceSubmit == true){
							//if Force Submit (Timeup) trim the answser to max length
							//int_ans = int_ans.substring(0,1000)
							//}else{	
								error_count++;
							//}
						}
					}		
					if( que.interactions[0].getType() == 'ES') {
						if( int_ans.length > 1000 ) {
							Dialog.alert(wb_msg_question + i + wb_msg_enter_less_char);
							//if(forceSubmit == true){
							//if Force Submit (Timeup) trim the answser to max length
							//int_ans = int_ans.substring(0,4000)
							//}else{
								error_count++;
							//}
						}
					}						
					eval('frm.atm_int_order_' + i +'_' + j +'.value= int_id ')
					eval('frm.atm_response_' + i +'_' + j +'.value= int_ans ')			
					if(eval('frm.atm_response_ext_' + i +'_' + j) != undefined) {
						eval('frm.atm_response_ext_' + i +'_' + j +'.value= int_ans_ext ')
					}
				}
			}
		}		
		
		if(error_count!=0){
			qset.loadQuestion(currentQue); 
			if(document.getElementById('titlediv') != null) {
				document.getElementById('titlediv').style.display='block';
			}
			if(document.getElementById('testBottom')!=null){
				document.getElementById('testBottom').style.display='block';
			}
			locked_for_single_submit = false;
			return;
		}
     if(args != 1){
			if(empty_ans != 0 /*&& forceSubmit != true*/){
				Dialog.confirm({text:wb_msg_que_no_ans, callback: function (answer) {
						if(!answer){
							qset.loadQuestion(currentQue); 	
							if(document.getElementById('titlediv') != null) {
								document.getElementById('titlediv').style.display='block';
							}
							if(document.getElementById('testBottom')!=null){
								document.getElementById('testBottom').style.display='block';
							}
							locked_for_single_submit = false;
							return;
						}else{
							do_scuess();
						}
					}
				});
			}else{
				do_scuess();
			}
		}else{
			do_scuess();
		}

	}
}

// Goto a specified question 
function CwQuestionGoto (qNum,args) {
	if (submit_test) {
		CwQuestionAutoSave (currentQue);
	}
	CwQuestionChange(qNum); 
	
	
   if (qNum <= qset.getSize() && qNum > 0) {
	 	if(submit_test){
		window.clearInterval(autoSaveIntervalObj); 
		autoSaveIntervalObj = window.setInterval('CwQuestionAutoSave(' + qNum + ')',300000);
		currentQue = qNum; 
	    }
	}
	if(args == '0' && submit_test == true){CwQuestionSubmit(args);}
	
}

function CwQuestionChange(qNum) {
	var n = qset.getSize()
	
	if (qNum <= n && qNum > 0 ) {
		qset.unloadQuestion(currentQue); 	
		qset.loadQuestion(qNum); 	
	}
}

function getControlString(nm,pos,order){
	return 'document.frm' + pos + '.'+ nm + pos + '_i' + order;
}

function CwQuestionAutoSave (currentQue, save_by_usr) {
	//为了保证时长的有效性在关闭窗口时会触发自动保存，此处判断若提交则不再进行自动保存
	if(submitted){
		return;
	}
	var n, i, j
	var str_value, cur_dbflag, interaction_order, interaction_answer;
	var frm = eval('frm' + currentQue);
	var can_submit = false;
	que = qset.getQuestion(currentQue);
	frm.flag.value = gen_get_cookie(COOKIE_QUE_FLG);
	frm.time_left.value = timeLeft;
	frmResult.flag.value = gen_get_cookie(COOKIE_QUE_FLG);//alert(frmResult.cur_tkh_id.value);
	frmResult.time_left.value = timeLeft;
	if (save_by_usr) {
		frm.save_by_usr.value = true;
		can_submit = true; 
	}
	n = que.getSize();
	frm.int_size.value = n;
	if (que.interactions[0].getType() == 'MT') {
		//alert(eval("G0" + currentQue + ".totalTaget"))
		for ( var k = 1; k<= eval('G0' + currentQue + '.totalTaget'); k++){
		//for ( var k = 1; k<= eval("G0" + currentQue + ".totalTaget"); k++){
				var ans = '';
				for(var j=0;j<que.ans_cnt;j++){
					if(que.answer_array[j]!=null){
						var tempstr=que.answer_array[j];			
						var sourceid=que.answer_array[j].substring(0,que.answer_array[j].indexOf('_'));
						var targetid=que.answer_array[j].substring(que.answer_array[j].indexOf('_')+1,que.answer_array[j].length);
						if(targetid==k){
							ans+=sourceid+'~';
						}
					}
				}
			interaction_order = k;
			interaction_answer = ans;
			var hid_name = eval('frm' + currentQue + '.autosave_q' + currentQue + '_i' + interaction_order);
			if (interaction_answer == '') {
				if (hid_name.value != '') {
					cur_dbflag = 'del';	
					interaction_answer = 'blank';
					can_submit = true;
				} else {
					interaction_answer = 'blank';
					cur_dbflag = 'blank';	
				}	
			}else {
				if (hid_name.value == '') {
					cur_dbflag = 'ins';
					can_submit = true;
				} else if (interaction_answer != hid_name.value){
					cur_dbflag = 'upd';
					can_submit = true;
				} else {
					cur_dbflag = 'blank';	
				}
			}
			str_value = interaction_order + '_' + interaction_answer + '_' + cur_dbflag;
			if (interaction_answer == 'blank') {
				hid_name.value = '';	
			} else {
				hid_name.value = interaction_answer;
			}
			var hid_responsebil = eval('frm' + currentQue + '.responsebil_order_dbflag' + interaction_order);
			hid_responsebil.value = str_value; 
			//alert(hid_responsebil.value)
		}			
	} else {
		for (i = 1; i <= n; i++) {	
			interaction = que.getInteraction(i);
			interaction_order = interaction.getId();
			interaction_answer = interaction.getAnswer();
			var hid_name = eval('frm' + currentQue + '.autosave_q' + currentQue + '_i' + interaction_order);
			if (interaction_answer == '') {
				if (hid_name.value != '') {
					cur_dbflag = 'del';	
					interaction_answer = 'blank';
					can_submit = true;
				} else {
					interaction_answer = 'blank';
					cur_dbflag = 'blank';	
				}	
			}else {
				if (hid_name.value == '') {
					cur_dbflag = 'ins';
					can_submit = true;
				} else if (interaction_answer != hid_name.value){
					cur_dbflag = 'upd';
					can_submit = true;
				} else {
					cur_dbflag = 'blank';	
				}
			}
			str_value = interaction_order + '_' + interaction_answer + '_' + cur_dbflag;	
			if (interaction_answer == 'blank') {
				hid_name.value = '';	
			} else {
				hid_name.value = interaction_answer;
			}
			var hid_responsebil = eval('frm' + currentQue + '.responsebil_order_dbflag' + interaction_order);
			hid_responsebil.value = str_value;
		}	 
		if (que.interactions[0].getType() == 'ES') {
			var id = frm.que_id.value;
			var spanObj = document.getElementById('es_file_' + id);
			if(spanObj.childNodes.length) {
				for (j=1; j<=spanObj.childNodes.length; j++) {
					var file_name = eval('frm' + currentQue + '.file_'+ id + '_' + j);
					file_name.disabled = true;
				}
			}
		}
	}	
	if(frmResult.db_flag_cookie.value != frm.flag.value) {
		frmResult.db_flag_cookie.value = frm.flag.value;
		can_submit = true;			
	}
	if (can_submit) {
		frm.target = 'file_submit';
		frm.method = 'post';
		frm.module.value = 'autosave.AutoSaveModule';
		frm.cmd.value = 'auto_save';
		frm.action = wb_utils_disp_servlet_url;
		frm.url_failure.value = '../htm/close_window.htm';
		jQuery.ajax({
			url:frm.action,
			data:$(frm).serialize(),
			type:"POST"
		});
		//frm.submit();
	}
	if (que.interactions[0].getType() == 'ES') {
		var id = frm.que_id.value;
		var spanObj = document.getElementById('es_file_' + id);
		if(spanObj.childNodes.length) {
			for (j=1; j<=spanObj.childNodes.length; j++) {
				var file_name = eval('frm' + currentQue + '.file_'+ id + '_' + j);
				file_name.disabled = false;
			}
		}
	}
}

// ----- for test player in dhtml -----

var FrmPrefix = 'document.frmResult.';
//var QueIndUnattempt = '../wb_image/test_que_unattempted.gif';
//var QueIndAttempted = '../wb_image/test_que_attempted.gif';
//var QueIndFlagged = '../wb_image/test_que_flagged.gif';
//var QueIndAttemptedAndFlagged = '../wb_image/test_que_attempted_and_flag.gif';
var QueIndUnattempt = '../static/images/tm03.gif';
var QueIndAttempted = '../static/images/tm01.gif';
var QueIndFlagged = '../static/images/tm02.gif';
var QueIndAttemptedAndFlagged = '../static/images/tm01.gif';
var QueIndUnflag = '../wb_image/tp.gif';
var QueIndSet = 'yes';
var QueIndUnset = '';
var SlotSize = 500; //一页条数。如果题目选择有错误，把这个加大
var answer=new Array();

var FadeColor = new Array(9);
FadeColor[0] = '#FFFF00';
FadeColor[1] = '#FFFF01';
FadeColor[2] = '#FFFF03';
FadeColor[3] = '#FFFF07';
FadeColor[4] = '#FFFF0F';
FadeColor[5] = '#FFFF1F';
FadeColor[6] = '#FFFF3F';
FadeColor[7] = '#FFFF7F';
FadeColor[8] = '#FFFFFF';

var frmObj;
var curIdxObj;
var timeRemainIntervalObj;
var autoSaveIntervalObj;
var preSlotID;

function setFocusColor(obj) {

    obj.style.backgroundColor = '#FFFF00';
}

function unsetFocusColor(obj) {
    obj.style.backgroundColor = '';
}

function convertSlot2Que(slot_id) {
    return (SlotSize * (Number(frmObj.cur_page_offset.value)-1) + Number(slot_id));
}

function convertQue2Slot(que_id) {
    return (Number(que_id) - SlotSize * (Number(frmObj.cur_page_offset.value)-1));
}

function redrawSlot() {	
	for (var i = 1; i <= SlotSize; i++) {
		var objtemp=eval(FrmPrefix + 'atm' + convertSlot2Que(i));
		if(objtemp!=null){
			if(i){
			}
		}else{
		}
	}
	redrawSlotInd();
}

function redrawSlotInd() {
	if(document.getElementById('unattempt_value')){
		document.getElementById('unattempt_value').innerHTML = frmObj.cur_unatm_cnt.value;
	}
	if(document.getElementById('attempted_value')){
		document.getElementById('attempted_value').innerHTML = frmObj.cur_atm_cnt.value;
	}
	if(document.getElementById('flagged_value')){
		document.getElementById('flagged_value').innerHTML = frmObj.cur_flag_cnt.value;
	}
}

function changeSlot(slot_id) {
    changeQue(convertSlot2Que(slot_id));
    redrawSlotInd();
}

function changeQue(que_id) {
	var curPageOffset = Number(frmObj.cur_page_offset.value);
	var pageStart = (curPageOffset-1) * SlotSize + 1;
	var pageEnd = (curPageOffset) * SlotSize;
	var targetQueID = Number(que_id);
	var total_que=Number(frmObj.total_que.value);
	var total_page=Math.ceil(total_que/SlotSize);
	currentQue=frmObj.cur_que_id.value;
	
	if (qset.getQuestion(frmObj.cur_que_id.value).interactions[0].getType() == 'MT') {
		if(qset.getQuestion(frmObj.cur_que_id.value).answer_array.length >=1){
			attemptQue(frmObj.cur_que_id.value);
		}else{
			unattemptQue(frmObj.cur_que_id.value);
		}
	}else{
		if (qset.getQuestionStatus(frmObj.cur_que_id.value) == STS_COMPLETE) {
			attemptQue(frmObj.cur_que_id.value);
		}
	}
	/*document.getElementById('slot_idx' + currentQue).className = 'UnSelectedQue';
	document.getElementById('slot_idx' + que_id).className = 'SelectedQue'; */ 
	if (qset.getQuestion(que_id).interactions[0].getType() == 'MT') {
		matchingInit('show',que_id);
	}
	CwQuestionGoto(que_id);
	frmObj.cur_que_id.value = que_id;
	
	drawBottomDiv(que_id);
	
	document.getElementById('nextQue'+total_que).style.visibility = 'hidden';
	document.getElementById('nextQue'+total_que).style.display = 'none';
	document.getElementById('prevQue'+1).style.visibility = 'hidden';
	document.getElementById('submitQue'+total_que).style.visibility = 'visible';
	document.getElementById('showMsg'+total_que).style.display = '';
}

function recalPageOffset(que_id) {
    var targetQueID = Number(que_id);
    frmObj.cur_page_offset.value = Math.ceil(targetQueID / SlotSize);
    redrawSlot();
}

function nextQue() {
	var parent = window.parent.opener;
	if (parent != null && parent && parent.timings) {
		parent.timings();
	}
	var totalQue = Number(frmObj.total_que.value);
	var curQue = Number(frmObj.cur_que_id.value);
	if (curQue < totalQue) {
		if((curQue%SlotSize)==0){
			frmObj.cur_page_offset.value=Number(frmObj.cur_page_offset.value)+1;
		}
		changeQue(Number(frmObj.cur_que_id.value) + 1);
	}else if(curQue == totalQue){
		changeQue(Number(frmObj.cur_que_id.value));
	}
	redrawSlotInd();
}

function prevQue() {
    var parent = window.parent.opener;
	if (parent != null && parent && parent.timings) {
		parent.timings();
	}
	if (Number(frmObj.cur_que_id.value) > 1) {
		var curQue = Number(frmObj.cur_que_id.value);
		if((curQue%SlotSize)==1){
			frmObj.cur_page_offset.value=Number(frmObj.cur_page_offset.value)-1;
		}
		changeQue(Number(frmObj.cur_que_id.value) - 1);  
	}else if(Number(frmObj.cur_que_id.value) == 1 ){
		changeQue(Number(frmObj.cur_que_id.value));
	}
	redrawSlotInd();
}

function nextPage() {
	var totalQue = Number(frmObj.total_que.value);
	var newPageOffset = Number(frmObj.cur_page_offset.value) + 1;
	
	var pageStart = (newPageOffset-1) * SlotSize + 1;
	var curQue = Number(frmObj.cur_que_id.value);
	if (pageStart <= totalQue) { 
		frmObj.cur_page_offset.value=newPageOffset;
		changeQue(pageStart);
	}
	redrawSlotInd();
}

function prevPage() {
	var curPageOffset = Number(frmObj.cur_page_offset.value);
	var curQue = Number(frmObj.cur_que_id.value);
	if (curPageOffset > 1) {
		var newPageOffset = curPageOffset - 1;
		frmObj.cur_page_offset.value=newPageOffset;
		var pageStart = (newPageOffset-1) * SlotSize + 1;
		var pageEnd = (newPageOffset ) * SlotSize;
		if (pageStart >= 1) {
			changeQue(pageEnd);
		}
	}
	redrawSlotInd();
}

function flagQue(cur_que_id) {
	curIdxObj = eval(FrmPrefix + 'flag' + cur_que_id);
    flagObj = $("#flagStar" + cur_que_id);
    if (curIdxObj.value == '') {
        curIdxObj.value = QueIndSet;
        frmObj.cur_flag_cnt.value = Number(frmObj.cur_flag_cnt.value) + 1;
        qset.setFlag(cur_que_id, true);
//				document.getElementById('slot_idx' + cur_que_id).style.color = 'black';
//				document.getElementById('flag_layer' + cur_que_id).src = QueRedFlagged;
    } 
//    else {
//        curIdxObj.value = QueIndUnset;
//        frmObj.cur_flag_cnt.value = Number(frmObj.cur_flag_cnt.value) - 1;
//        qset.setFlag(cur_que_id, false);
//				document.getElementById('slot_td_idx' + cur_que_id).style.backgroundColor = '';
//				document.getElementById('flag_layer' + cur_que_id).src = QueNomalFlagged;
//    }
    if(eval(FrmPrefix + 'atm' + cur_que_id).value == ''){
    	document.getElementById('slot_td_idx' + cur_que_id).className = 'border-bottom-red graycolor';
    }else{
    	document.getElementById('slot_td_idx' + cur_que_id).className = 'border-bottom-red yellowcolor';
    }
    flagObj.find("i").addClass("cur");
	flagObj.attr("onclick","javascript:unflagQue(" + cur_que_id + ");redrawSlotInd()");
	redrawSlotInd();
}

function unflagQue(cur_que_id) {
	curIdxObj = eval(FrmPrefix + 'flag' + cur_que_id);
    flagObj = $("#flagStar" + cur_que_id);
    if (curIdxObj.value != '') {
        curIdxObj.value = QueIndUnset;
        frmObj.cur_flag_cnt.value = Number(frmObj.cur_flag_cnt.value) - 1;
        qset.setFlag(cur_que_id, false);
        if(eval(FrmPrefix + 'atm' + cur_que_id).value == ''){
        	document.getElementById('slot_td_idx' + cur_que_id).className = 'graycolor';
        }else{
        	document.getElementById('slot_td_idx' + cur_que_id).className = 'yellowcolor';
        }
        flagObj.find("i").removeClass("cur");
    }
    flagObj.attr("onclick","javascript:flagQue(" + cur_que_id + ");redrawSlotInd()");
    redrawSlotInd();
}

function attemptQue(que_id) {
    var thisOffset = 0;
    curIdxObj =eval(FrmPrefix + 'atm' + que_id);
    if (curIdxObj.value == '') {
        curIdxObj.value = QueIndSet;
        thisOffset = 1;
    }
    frmObj.cur_atm_cnt.value = Number(frmObj.cur_atm_cnt.value) + thisOffset;
    frmObj.cur_unatm_cnt.value = Number(frmObj.total_que.value) - Number(frmObj.cur_atm_cnt.value);
    
    document.getElementById('slot_td_idx' + que_id).className = 'yellowcolor';
    curIdxObj = eval(FrmPrefix + 'flag' + que_id);
    if(curIdxObj.value != ''){
    	document.getElementById('slot_td_idx' + que_id).className = 'border-bottom-red yellowcolor';
    }
}

function unattemptQue(que_id){
	var thisOffset = 0;
    curIdxObj = eval(FrmPrefix + 'atm' + que_id);
    if (curIdxObj.value != '') {
        curIdxObj.value = '';
        thisOffset = 1 ;
    }
    frmObj.cur_atm_cnt.value = Number(frmObj.cur_atm_cnt.value) - thisOffset;
    frmObj.cur_unatm_cnt.value = Number(frmObj.total_que.value) - Number(frmObj.cur_atm_cnt.value);
}

function nextUnattemptQue() {
    var totalQue = Number(frmObj.total_que.value);
    var curQueID = Number(frmObj.cur_que_id.value);
    var i = 0;
    var j = 0;
    for (i = curQueID+1; i <= totalQue; i++) {
        if (eval(FrmPrefix + 'atm' + i).value == '') {
            break;
        }
    }
    if (i <= totalQue) {
    		curpage=Math.ceil(i/SlotSize);	   	
     		frmObj.cur_page_offset.value=curpage;   	
        changeQue(i);
    } else {
        for (j = 1; j <= curQueID; j++) {
            if (eval(FrmPrefix + 'atm' + j).value == '') {
                break;
            }
        }
        if (j <= curQueID) {
        	curpage=Math.ceil(j/SlotSize);	   	
     			frmObj.cur_page_offset.value=curpage;   	
            changeQue(j);
        } else {
            setInterMsg(wb_msg_noattempt_ans);
        }
    }
    redrawSlotInd();
}

function nextFlaggedQue() {
    var totalQue = Number(frmObj.total_que.value);
    var curQueID = Number(frmObj.cur_que_id.value);
    var i = 0;
    var j = 0;
    for (i = curQueID+1; i <= totalQue; i++) {
        if (eval(FrmPrefix + 'flag' + i).value != '') {
            break;
        }
    }
    if (i <= totalQue) {
        changeQue(i);
    } else {
        for (j = 1; j <= curQueID; j++) {
            if (eval(FrmPrefix + 'flag' + j).value != '') {
                break;
            }
        }
        if (j <= curQueID) {
            changeQue(j);
        } else {
            setInterMsg(wb_msg_noflag_ans);
        }
    }
    redrawSlotInd();
}

function setInterMsg(msg) {
    document.getElementById('inter_msg').innerHTML = msg;
    fadeCell('inter_msg');
    setTimeout('clearInterMsg()', 3000);
    $("#inter_msg").show();
}

function clearInterMsg() {
    document.getElementById('inter_msg').innerHTML = '';
    $("#inter_msg").hide();
}

function formatTimeRemain(timeRemain) {
	var secPart = Number(timeRemain) % 60;
	var minPart = parseInt(timeRemain / 60);
	var result = '';
	if(timeRemain != -60) {
		if (minPart < 10) {
			result = result + '0';
		}
		result = result + minPart + ':';
		if (secPart < 10) {
			result = result + '0';
		}
		result = result + secPart ;
	} else {
		result = "--";
	}
    return (result);
}

function fadeCell(cellName, idx) {
    if (idx == null) {
        idx = 0;
    }
    if (idx < FadeColor.length) {
        //document.getElementById(cellName).style.backgroundColor = FadeColor[idx++];
        setTimeout('fadeCell(\'' + cellName + '\', ' + idx + ')', 50);
    }
}

function refreshTimeRemain() {
	if(timeLeft == -60) {
		document.getElementById('time_remain_value').innerHTML = "--";
	} else {
		if (timeLeft <= 0) {
			clearInterval(timeRemainIntervalObj);
		} else {
			timeLeft--;
		}
		document.getElementById('time_remain_value').innerHTML = formatTimeRemain(timeLeft);
		fadeCell('time_remain_value');
		if(timeLeft == '0' && submit_test == true){
			changeflag();
			CwQuestionSubmit(1);
		}
		
	}
}

function toggleTimeRemain() {
    if (document.getElementById('time_remain_text').style.visibility == 'hidden') {
        document.getElementById('time_remain_text').style.visibility = 'visible';
        document.getElementById('time_remain_value').style.visibility = 'visible';
    } else {
        document.getElementById('time_remain_text').style.visibility = 'hidden';
        document.getElementById('time_remain_value').style.visibility = 'hidden';
    }
}

function submitTest() {
	if(submit_test == true) {
		 CwQuestionSubmit()
	}else{
		Dialog.alert(wb_msg_preview_mode);
	}
}

//类名className的元素
function getClass(className) {
	//支持这个函数
	if(document.getElementsByClassName){
		return document.getElementsByClassName(className);
	} else {       
		var tagArr=[];//用于返回类名为className的元素
		var tags=document.getElementsByTagName("a");//获取标签
		for(var i=0;i < tags.length; i++) {
			if(tags[i].class == className) {
				tagArr[tagArr.length] = tags[i];//保存满足条件的元素
			}
		}
		var tags=document.getElementsByTagName("input");//获取标签
		for(var i=0;i < tags.length; i++) {
			if(tags[i].class == className) {
				tagArr[tagArr.length] = tags[i];//保存满足条件的元素
			}
		}
		return tagArr;
	}
}

function init() {
	var ua = navigator.userAgent.toLowerCase();
	if (((ua.indexOf("android") > -1) || ((ua.match(/iphone/i)) || (ua.match(/ipod/i)) || (ua.match(/ipad/i))) || ua.match(/Windows Phone/i) || ua.match(/BlackBerry/i) || ua.match(/webOS/i))) {
		$("#help_btn").remove();
	}
	$(".left").hide();
    frmObj = document.frmResult;  
	var i, n, curIdexObj;
	n = qset.getSize();	
	drawHidden(frmObj.total_que.value);
	
	for (i = 1; i <=n; i++) {
		qset.getQuestion(i).queLayerTop= document.getElementById('head_td').offsetHeight; 
	    curIdxObj = eval(FrmPrefix + 'flag' + i);
		// check flag
		if (qset.getFlag(i) == true) {	
			curIdxObj.value = QueIndSet;
			flagQue(i);
			frmObj.cur_flag_cnt.value++;
			$("#layer" + i).find("i").addClass("cur");
		}
		
		// check answer(s)
	    curIdxObj = eval(FrmPrefix + 'atm' + i);
		if (qset.getQuestionStatus(i) == STS_COMPLETE) {
			curIdxObj.value = QueIndSet;
			attemptQue(i);
			frmObj.cur_atm_cnt.value++;
		}
		
		if (qset.getQuestion(i).interactions[0].getType() == 'MT') {
			if(qset.getQuestion(i).answer_array.length >0){
				curIdxObj.value = QueIndSet;
				attemptQue(i);
			}else{
				unattemptQue(i);
			}
		}
	}
	
    document.getElementById('time_remain_value').innerHTML = formatTimeRemain(timeLeft);
    document.getElementById('total_que_value').innerHTML = frmObj.total_que.value;
    document.getElementById('attempted_value').innerHTML = frmObj.cur_atm_cnt.value;
    document.getElementById('flagged_value').innerHTML = frmObj.cur_flag_cnt.value; 
    preSlotID = convertQue2Slot(frmObj.cur_que_id.value); 
    changeQue(frmObj.cur_que_id.value);
    redrawSlot();
    document.getElementById('slot_idx' + preSlotID).className = 'SelectedQue';
    timeRemainIntervalObj = setInterval('refreshTimeRemain()', 1000);
    //drawHidden(); 
	if (browserIE) {
		OptionTextWidth = OptionWidth + OptionBorderWidth + OptionBorderWidth;
		ResultTextWidth = OptionMidSize + OptionBorderWidth + OptionBorderWidth;
	} else {
		OptionTextWidth = OptionWidth;
		ResultTextWidth = OptionMidSize;
	}
	OptionImgWidth = OptionWidth;
	OptionImgHeight = OptionHeight;
}

function drawHidden(total_que_value) {
	var queobj=document.getElementById('hidden_que_varible');
	var hidden_obj = document.createElement('div');
	var str;
	for(var i=1;i<=total_que_value;i++){
		str=str+'<input type=\'hidden\' name=\'atm'+i+'\' value=\'\'/><input type=\'hidden\' name=\'flag'+i+'\' value=\'\'/>';
		hidden_obj.innerHTML=str;
		queobj.appendChild(hidden_obj);
	}		
}

// ----- for matching que in dhtml test player -----

var OptionMinSize = 40;
var OptionMidSize = 80;
var OptionMaxSize = 160;
var OptionBorderWidth = 1;

var OptionWidth = OptionMidSize;
var OptionHeight = OptionMidSize;

var OptionImgWidth = 0;
var OptionImgHeight = 0;;

var OptionTextWidth = 0;
var OptionTextHeight = 25;
var ResultTextWidth = 0;

var DefColor = '#c0c0c0';
var OverColor = '#0000ff';
var DragedDivCursorOffset = 5;
var ResultOutBackClor = '#ffffff';
var TextBackClor = '#ffffff';

var OptionMouseOutZIndex = 0;
var OptionMouseOverZIndex = 1;
var DragedDivZIndex = 2;

var int_id = 0;

var sourcenum;
var targetnum;

var sourceImg = '';
var targetImg = '';
var sourceTextHtml = '';
var targetTextHtml = '';
var sourceOptionId = '';
var targetOptionId = '';

var type = new Array(2);
type[0] = new Array('source', 0);
type[1] = new Array('target', 0);

function matchingInit(visible,posId) {
	var q=qset.getQuestion(posId);
	if (browserIE) {
		OptionTextWidth = OptionWidth + OptionBorderWidth + OptionBorderWidth;
		ResultTextWidth = OptionMidSize + OptionBorderWidth + OptionBorderWidth;
	}
	else {
		OptionTextWidth = OptionWidth;
		ResultTextWidth = OptionMidSize;
	}
		OptionImgWidth = OptionWidth;
		OptionImgHeight = OptionHeight;
	var cur_que_id=eval('document.frm'+posId+'.cur_que_id.value');
	
	if (visible == 'hide') {
//		var divObj = null;
//		divObj = document.getElementById('layer'+posId);
//		divObj.style.visibility = 'hidden';	
		$('#layer'+posId).hide();
	}
	else {
//		var divObj = null;
//		divObj = document.getElementById('layer'+posId);	  
//		divObj.style.visibility = 'visible';
		$('#layer'+posId).show();
		clearRadio(posId);
	}
	
	divObj = document.getElementById('G0'+posId+'DragedDiv');
	if(divObj!=null)
	divObj.style.visibility = 'hidden';

	sourcenum = Number(eval('frm'+posId+'.sourcenum.value'));
	targetnum = Number(eval('frm'+posId+'.targetnum.value'));
	target_radio_width=Number(eval('frm'+posId+'.target_radio_width.value'));
	source_td_width=Number(eval('frm'+posId+'.source_td_width.value'));	
	q.ans_cnt = sourcenum * targetnum;
	type[0][1] = sourcenum;
	type[1][1] = targetnum;
	var curImgTop = 0;
	
	for (var typeIdx = 0; typeIdx < type.length; typeIdx++) {
		var imgCnt = 0;
		for(var i=1;i<=type[typeIdx][1];i++){
			var tdObj=document.getElementById('G0' +posId+ type[typeIdx][0] +'Td_' + i);
			if (tdObj != null) {
				tdObj.width = OptionTextWidth  + "px";
			}
			var srcImgObj = document.getElementById('G0' +posId+ type[typeIdx][0] + 'Img_' + i);
			if (srcImgObj != null) {
				imgCnt++;
				srcImgObj.style.height = OptionImgHeight  + "px";
				srcImgObj.style.width = OptionImgWidth  + "px";
				srcImgObj.style.borderWidth = OptionBorderWidth  + "px";
				srcImgObj.style.borderStyle = 'solid';
				srcImgObj.style.borderColor = DefColor;
				srcImgObj.style.borderBottomStyle = 'none';
			}
			var srcObj = document.getElementById('G0' +posId+ type[typeIdx][0] + 'Text_' + i);
			if (srcObj != null) {
//				srcObj.style.position = 'absolute';
//				srcObj.style.background = TextBackClor;
//				srcObj.style.height = OptionTextHeight  + "px";
//				srcObj.style.width = OptionTextWidth  + "px";
//				srcObj.style.overflow = 'hidden';
//				srcObj.style.borderWidth = OptionBorderWidth  + "px";
//				srcObj.style.borderStyle = 'solid';
//				srcObj.style.borderColor = DefColor;
			}
		}
	}
	
	var dragedObj = document.getElementById('G0'+posId+'DragedDiv');
	if(dragedObj!=null){
		if(browserIE){
			dragedObj.style.filter = 'alpha(opacity=50)';	
		}else{
			dragedObj.style.opacity = '0.5';				
		}
	}
	
	var sizeId = 2;
	switch (OptionWidth) {
		case OptionMinSize:
			sizeId = 1;
			break;
		case OptionMidSize:
			sizeId = 2;
			break;
		case OptionMaxSize:
			sizeId = 3;
			break;
	}
//	var sizeOptObj = document.getElementById('opt_size_' + sizeId + '_' + posId);
//	sizeOptObj.checked = true;
}

function overOption(type, optionId, posId) {
	var srcObj = null;
	srcObj = document.getElementById('G0' +posId+ type + 'Img_' + optionId);
	if(srcObj != null) {
		srcObj.style.borderColor = OverColor;
	}
	srcObj = document.getElementById('G0' +posId+ type + 'Text_' + optionId);
	if(srcObj != null) {
//		srcObj.style.borderColor = OverColor;
//		srcObj.style.zIndex      = OptionMouseOverZIndex;
//		srcObj.style.width       = OptionTextWidth + OptionTextWidth  + "px";
//		srcObj.style.height      = OptionTextHeight * 5  + "px";
//		srcObj.style.overflow    = 'auto';
	}
}

function outOption(type, optionId, posId) {
	var srcObj = null;
	srcObj = document.getElementById('G0' +posId+ type + 'Img_' + optionId);
	if(srcObj != null) {
		srcObj.style.borderColor = DefColor;
	}
	srcObj = document.getElementById('G0' +posId+ type + 'Text_' + optionId);
	if(srcObj != null) {
//		srcObj.style.borderColor = DefColor;
//		srcObj.style.zIndex      = OptionMouseOutZIndex;
//		srcObj.style.width       = OptionTextWidth  + "px";
//		srcObj.style.height      = OptionTextHeight  + "px";
//		srcObj.style.overflow    = 'hidden';
	}
}

function getLeft(obj, posId) {
	var thisValue = 0;
	if (obj != null && obj.id != 'G0'+posId+'OptionTable') {
		if(browserIE){
			thisValue = obj.offsetLeft + getLeft(obj.offsetParent, posId);
		}else{
			if (obj.nodeName == 'DIV') {
				thisValue = obj.offsetLeft;
			}else {
				thisValue = getLeft(obj.offsetParent, posId);
			}
		}
	}
	return thisValue;
}

function getTop(obj, posId) {
	var thisValue = 0;
	if (obj!=null&&obj.id != 'G0'+posId+'OptionTable') {
		if(browserIE){
			thisValue = obj.offsetTop + getTop(obj.offsetParent,posId);
		}else{
			if (obj.nodeName == 'DIV') {
				thisValue = obj.offsetTop;
			}else {
				thisValue = getTop(obj.offsetParent,posId);
			}
		}
	}
	return thisValue;
}

function changeDragedPosition(event,posId) {
	// in ff, onmousemove event is not triggered if the mouse does not move
	// in ie, onmousemove event is still triggered even if the mouse does not move
	var dragedObj = document.getElementById('G0'+posId+'DragedDiv');
	if(dragedObj.style.visibility == 'visible') {
		var srcEle = null;
		var X, Y, eventObj;
		if(browserIE) {
			X = event.offsetX;
			Y = event.offsetY;
			eventObj = event.srcElement;
		} else {
			X = event.layerX;
			Y = event.layerY;
			eventObj = event.target;
		}
		dragedObj.style.left = getLeft(eventObj, posId) + X + DragedDivCursorOffset + "px";
		dragedObj.style.top = getTop(eventObj, posId) + Y + DragedDivCursorOffset + "px";
	}
}

function showHideDragedDiv(event,posId) {
	// show the dragged layer only when a radio button, the image/text of an option is clicked
	var showDragedDiv = false;
	var srcEle = null;
	if(browserIE) {
		srcEle = event.srcElement;
	} else {
		srcEle = event.target;
	}
	if(srcEle.type == 'radio' || srcEle.nodeName == 'IMG') {
		showDragedDiv = true;
	} else {
		srcEle = getParentObj(srcEle, 'DIV', 'OptionTable');
		if(srcEle != null) {
			showDragedDiv = true;		
		}
	}
	
	// check the corresponding radio button
	var type = '';
	var optionId = 0;
	if(showDragedDiv) {
		var tempstr=String(posId);
		var startindex=2+Number(tempstr.length);
		var endindex=8+Number(tempstr.length);
		type = srcEle.id.substring(startindex, endindex);
		optionId = srcEle.id.substring(srcEle.id.indexOf('_') + 1, srcEle.id.length);
		if(selectRadioOption(posId, type, optionId)) {
			showDragedDiv = false;
		}
	}
	if(!showDragedDiv) {
		clearRadio(posId);
	}
	
	var dragedObj = document.getElementById('G0'+posId+'DragedDiv');
	if(showDragedDiv) {
		// construct the content and show the dragged layer
		var dragedImgObj  = document.getElementById('G0'+posId+'DragedImg');
		var dragedTextObj = document.getElementById('G0'+posId+'DragedText');
		dragedImgObj.src = '';
		dragedTextObj.innerHTML = '';
		if(type == 'source') {
			if(sourceImg == ''){
				dragedImgObj.style.display='none';
			}else{
				dragedImgObj.src = sourceImg;
				dragedImgObj.style.display='block';
			}
			dragedTextObj.innerHTML = sourceTextHtml;
		} else if(type == 'target') {
			if(targetImg == ''){
				dragedImgObj.style.display='none';
			}else{
				dragedImgObj.src = targetImg;
				dragedImgObj.style.display='block';
			}
			dragedTextObj.innerHTML = targetTextHtml;
		}

		dragedImgObj.style.width             = OptionImgWidth  + "px";
		dragedImgObj.style.height            = OptionImgHeight  + "px";
		dragedImgObj.style.borderWidth       = OptionBorderWidth  + "px";
		dragedImgObj.style.borderStyle       = 'solid';
		dragedImgObj.style.borderColor       = DefColor;
		dragedImgObj.style.borderBottomStyle = 'none';
		dragedTextObj.style.width       = OptionTextWidth  + "px";
		dragedTextObj.style.height      = OptionTextHeight  + "px";
		dragedTextObj.style.borderWidth = OptionBorderWidth  + "px";
		dragedTextObj.style.borderStyle = 'solid';
		dragedTextObj.style.borderColor = DefColor;
		dragedObj.style.height     = OptionImgHeight + OptionTextHeight + OptionBorderWidth  + "px";
		dragedObj.style.width      = OptionTextWidth  + "px";
		dragedObj.style.zIndex     = DragedDivZIndex;
		dragedObj.style.visibility = 'visible';
		changeDragedPosition(event,posId);
	} else {
		// hide the dragged layer
		dragedObj.style.visibility = 'hidden';
	}
}

// check an option and return whether an answer has been selected or not
function selectRadioOption(posId, type, optionId) {
	var radioObj = document.getElementById('G0'+posId+ type + 'Radio_' + optionId);
	if(radioObj != null) {
		radioObj.checked = true;
	}
	var imgObj = document.getElementById('G0' +posId+ type + 'Img_' + optionId);
	if(imgObj != null) {
		if(type == 'source') {
			sourceImg = imgObj.src;	
		} else if(type == 'target') {
			targetImg = imgObj.src;	
		}
	} else {
		if(type == 'source'){
			sourceImg = '';	
		}
		if(type == 'target'){
			targetImg = '';	
		}
	}
	if(type == 'source') {
		sourceTextHtml = document.getElementById('G0' +posId+ type + 'Text_' + optionId).innerHTML;
		sourceOptionId = optionId;
	} else if(type == 'target') {
		targetTextHtml = document.getElementById('G0' +posId + type + 'Text_' + optionId).innerHTML;
		targetOptionId = optionId;
	}
	
	var isAnsSelected = false;
	if(sourceOptionId != '' && targetOptionId != '') {
		var q = qset.getQuestion(posId);
		// check if the target or the answer has been selected already
		var s_t_id = sourceOptionId + '_' + targetOptionId;
		var templength=q.answer_array.length;
		for(var i=0; i<templength; i++) {
			if(q.answer_array[i] !=null){
				var sourcestr=q.answer_array[i].substring(0,q.answer_array[i].indexOf('_'));
				if(sourcestr == sourceOptionId){
					Dialog.alert(wb_msg_source_selected);
					isAnsSelected = true;
					break;
				}
			}
		}
		if(isAnsSelected == false) {
			// the answer has NOT been selected yet
			int_id++;		
			q.answer_array[q.answer_array.length] = s_t_id;
			drawResult(posId);
			isAnsSelected = true;
		}
	}
	return isAnsSelected;
}

function drawResult(posId) {
	var q = qset.getQuestion(posId);
	
	var ansObj = document.getElementById('G0'+posId+'AnswerTable');
	var ansObjClone = document.getElementById('G0'+posId+'cloneTable');
	var newObj = ansObjClone.cloneNode(true);
	// fix the width to a suitable size
//	var tdObj = newObj.getElementsByTagName('TD');
//	for (var i = 0; i < tdObj.length; i++) {
//		tdObj[i].style.width = OptionMidSize;
//	}
	// set the image to the selected option
	var imgObj = newObj.getElementsByTagName('IMG');
	if (sourceImg != '') {
		imgObj[0].src = sourceImg;
		imgObj[0].style.width = OptionMidSize  + "px";
		imgObj[0].style.height = OptionMidSize  + "px";
		imgObj[0].style.borderWidth = OptionBorderWidth  + "px";
		imgObj[0].style.borderStyle = 'solid';
		imgObj[0].style.borderColor = DefColor;
		imgObj[0].style.borderBottomStyle = 'none';
		imgObj[0].style.display = 'block';
	} else {
		imgObj[0].style.display = 'none';
	}
	if (targetImg != '') {
		imgObj[1].src = targetImg;
		imgObj[1].style.width = OptionMidSize  + "px";
		imgObj[1].style.height = OptionMidSize  + "px";
		imgObj[1].style.borderWidth = OptionBorderWidth  + "px";
		imgObj[1].style.borderStyle = 'solid';
		imgObj[1].style.borderColor = DefColor;
		imgObj[1].style.borderBottomStyle = 'none';
		imgObj[1].style.display = 'block';
	} else {
		imgObj[1].style.display = 'none';
	}
	// set the text to the selected option
	var txtObj = newObj.getElementsByTagName('DIV');
	txtObj[0].style.background = TextBackClor;
	//txtObj[0].style.height = OptionTextHeight + "px";
	txtObj[0].style.width = ResultTextWidth + "px";
	txtObj[0].style.borderWidth = OptionBorderWidth + "px";
	txtObj[0].style.borderStyle = 'solid';
	txtObj[0].style.borderColor = DefColor;
	txtObj[0].style.zIndex = ansObj.childNodes.length;
	txtObj[0].innerHTML = sourceTextHtml;
	txtObj[1].style.background = TextBackClor;
	//txtObj[1].style.height = OptionTextHeight  + "px";
	txtObj[1].style.width = ResultTextWidth  + "px";
	txtObj[1].style.borderWidth = OptionBorderWidth  + "px";
	txtObj[1].style.borderStyle = 'solid';
	txtObj[1].style.borderColor = DefColor;
	txtObj[1].style.zIndex =ansObj.childNodes.length;
	txtObj[1].innerHTML = targetTextHtml;
	newObj.id = '';
	newObj.style.borderWidth = OptionBorderWidth  + "px";
	newObj.style.borderStyle = 'solid';
	newObj.style.borderColor = ResultOutBackClor;
	newObj.style.display = 'block';
	ansObj.insertBefore(newObj, ansObj.firstChild);
	
}

function overResult(event, srcObj) {
	srcObj.style.borderColor = OverColor;
	
	var srcEle = null;
	if(browserIE) {
		srcEle = event.srcElement;
	} else {
		srcEle = event.target;
	}
	srcEle = getParentObj(srcEle, 'DIV', 'AnswerTable');
	if(srcEle != null) {
//		srcEle.style.overflow = 'auto';
//		srcEle.style.height   = OptionTextHeight * 5  + "px";
	}
}

function outResult(event, srcObj) {
	srcObj.style.borderColor = ResultOutBackClor;

	var srcEle = null;
	if(browserIE) {
		srcEle = event.srcElement;
	} else {
		srcEle = event.target;
	}
	srcEle = getParentObj(srcEle, 'DIV', 'AnswerTable');
	if(srcEle != null) {
//		srcEle.style.overflow = 'hidden';
//		srcEle.style.height   = OptionTextHeight + "px";
	}
}

function deleteOneOption(posId, obj) {
	var ansObj = document.getElementById('G0'+posId+'AnswerTable');
	obj = getParentObj(obj, 'TABLE', 'AnswerTable');
	for (var i = 0; i < ansObj.childNodes.length; i++) {
		if (ansObj.childNodes[i] == obj) {
			i = ansObj.childNodes.length - 1 - i; // because new answer is added to the top of the list
			var q = qset.getQuestion(posId);
			for (var j = i; j < (q.answer_array.length - 1); j++) {
				q.answer_array[j] = q.answer_array[j+1];
			}
			q.answer_array.length--;
			ansObj.removeChild(obj);
			break;
		}
	}
}

function deleteAllOption(posId){
	var ansObj = document.getElementById('G0'+posId+'AnswerTable');
	while (ansObj.childNodes.length > 0) {
		ansObj.removeChild(ansObj.lastChild);
	}
	var q = qset.getQuestion(posId);
	q.answer_array.length = 0;
}

function clearRadio(posId) {
	sourceOptionId = '';
	targetOptionId = '';
	sourceImg = '';
	targetImg = '';
	sourceTextHtml = '';
	targetTextHtml = '';
	for(var i=1;i<=sourcenum;i++){
		var sourceRadObj = document.getElementById('G0'+posId+'sourceRadio_' + i);
		if(sourceRadObj != null && sourceRadObj.checked) {
			sourceRadObj.checked = false;
		}
	}
	for(var i=1;i<=targetnum;i++){
		var targetRadObj = document.getElementById('G0'+posId+'targetRadio_' + i);
		if(targetRadObj != null && targetRadObj.checked) {
			targetRadObj.checked = false;
		}
	}
}

function changeOptionSize(size,posId) {
	OptionWidth = size;
	OptionHeight = size;
	matchingInit('show',posId);
}

function getParentObj(childObj, parentTagName, parentBoundId) {
	var result = null;
	if (childObj.id.indexOf(parentBoundId) < 0) {
		if (childObj.nodeName == parentTagName) {
			result = childObj;
		}
		else {
			result = getParentObj(childObj.parentElement, parentTagName, parentBoundId);
		}
	}
	return result;	
}

function showMtAns(posId){
	var q=qset.getQuestion(posId);
	var templength=q.answer_array.length;
	for(var i=0;i<templength;i++){
		int_id++;
		sourceId=q.answer_array[i].substring(0,q.answer_array[i].indexOf('_'));
		targetId=q.answer_array[i].substring(q.answer_array[i].indexOf('_')+1,q.answer_array[i].length);
		sourceImgObj = document.getElementById('G0' +posId+ 'sourceImg_' + sourceId);
		targetImgObj = document.getElementById('G0' +posId+ 'targetImg_' + targetId);
		//alert(posId);
		
		if(sourceImgObj != null) {
				sourceImg = sourceImgObj.src;	
		} else {
				sourceImg = '';	
		}
		if(targetImgObj != null) {
				targetImg = sourceImgObj.src;	
		} else {
				targetImg = '';	
		}
		
		if(document.getElementById('G0'+posId+ 'sourceText_' + sourceId)!=null){
			sourceTextHtml = document.getElementById('G0'+posId+ 'sourceText_' + sourceId).innerHTML;
		}
		if(document.getElementById('G0'+posId + 'targetText_' + targetId)!=null){
			targetTextHtml = document.getElementById('G0'+posId + 'targetText_' + targetId).innerHTML;
		}
		drawResult(posId);
	}
}

function drawBottomDiv(que_id) {
	var div = document.getElementById('layer' + que_id);
	var bottomDiv = document.getElementById('testBottom');
	bottomDiv.style.top = getTop(div) + div.clientHeight;
	bottomDiv.style.left = 0;
//	bottomDiv.style.height = '340px';
//	bottomDiv.style.width = div.offsetWidth;
	bottomDiv.style.visibility = 'visible';
}