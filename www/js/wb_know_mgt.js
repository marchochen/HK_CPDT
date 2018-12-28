function wbKnowMgt(){
	this.add_kca_prep = wbKcaAddPrep;
	this.add_upd_kca_exec = wbKcaAddUpdExec;
	this.upd_kca_prep = wbKcaUpdPrep;
	this.del_kca_exec = wbKcaDelExec;
	this.kca_detail = wbKcaDetail;
	this.kca_change_prep = wbKcaChangePrep;
	this.kca_search_exec = wbKcaSearchExec;
	this.que_detail = wbQueDetail;
	this.del_que = wbQueDel;
	this.add_faq_prep = wbAddFaqPrep;
	this.add_faq = wbAddFaq;
	this.get_faq = wbGetFaqById;
	this.upd_faq = wbUpdateFaq;
}

function wbKcaAddPrep(parent_kca_id){
	window.location.href = wb_utils_invoke_disp_servlet("module","JsonMod.know.KnowModule","cmd","ins_kca_prep","stylesheet","know_edit_cata.xsl", "parent_kca_id", parent_kca_id);
}

function wbKcaAddUpdExec(frm, lang){
	var kca_id;
	
	frm.kca_title.value = wbUtilsTrimString(frm.kca_title.value);
	if(!wbUtilsValidateEmptyField(frm.kca_title, frm.lab_kca_title.value)){
		frm.kca_title.focus()
		return;
	}
	frm.action = wb_utils_disp_servlet_url;
	
	var stylesheet;
	if(frm.is_upd.value === "true"){
		frm.cmd.value = "upd_kca_exec";
		kca_id = frm.kca_id.value;
		stylesheet = 'know_catalog_detail.xsl';
		if(frm.parent_kca_id.value === '0') {
			frm.parent_kca_id.value = kca_id;
		}
	} else {
		frm.cmd.value = "ins_kca_exec";
		kca_id = frm.parent_kca_id.value;
		
		if (kca_id <= 0) {
			stylesheet = 'know_catalog_lst.xsl';
		} else {
			stylesheet = 'know_catalog_detail.xsl';
		}
	}
	frm.module.value = "JsonMod.know.KnowModule";
	frm.url_success.value = wb_utils_invoke_disp_servlet('module', 'JsonMod.know.KnowModule', 'cmd', 'get_kca_lst', 'stylesheet', stylesheet, 'kca_id', kca_id);
	frm.url_failure.value = wb_utils_invoke_disp_servlet('module', 'JsonMod.know.KnowModule', 'cmd', 'get_kca_lst', 'stylesheet', stylesheet, 'kca_id', frm.parent_kca_id.value);
	frm.submit();
}

function wbKcaUpdPrep(kca_id){
	window.location.href = wb_utils_invoke_disp_servlet("module","JsonMod.know.KnowModule","cmd","upd_kca_prep","stylesheet","know_edit_cata.xsl", "kca_id", kca_id);
}

function wbKcaDelExec(kca_id, parent_kca_id, kca_upd_timestamp){
	if(confirm(wb_msg_confirm))	 {
		var url_success = _getKcaDetailUrl(parent_kca_id);
		var url_failure = _getKcaDetailUrl(kca_id);
		window.location.href = wb_utils_invoke_disp_servlet("module","JsonMod.know.KnowModule","cmd","del_kca", "kca_id", kca_id, "kca_upd_timestamp", kca_upd_timestamp, "url_success", url_success, "url_failure", url_failure);
	}
}

function wbKcaDetail(kca_id, sort_col, sort_order, cur_page, page_size){
	window.location.href = _getKcaDetailUrl(kca_id, sort_col, sort_order, cur_page, page_size);
}

function wbKcaChangePrep(frm) {
	if(!isQueIdEmpty(frm)) {
		if (frm.que_id_lst.value === '') {
			alert(getLabel("511"));
			return;

		}
		var iTop = (window.screen.availHeight-30-500)/2;
		var iLeft = (window.screen.availWidth-10-500)/2;   
		str_feature = 'width=' + 450 + ',height=' + 500 + ',scrollbars=' + 'yes' + ',resizable=' + 'yes'+ ',top='+ iTop + ',left='+iLeft;
		prep_win = wbUtilsOpenWin('', 'tree_prep', false, str_feature);
		prep_win.location.href = '../app/know/knowCatalogTree?que_id_lst=' + frm.que_id_lst.value;
		prep_win.focus();
	}
}

function wbKcaSearchExec(frm, lang, sort_col, sort_order, cur_page, page_size, chkEmpty) {
	if( chkEmpty === undefined || chkEmpty === "") {
		chkEmpty = true;
	}
	if(sort_col == null){
		if(sort_order == null){
			sort_order = "desc"
		}
		frm.sort_col.value = "que_create_timestamp";
		frm.sort_order.value = sort_order;
	}else{
		if (sort_order == null){
			sort_order = "asc";
		}
		frm.sort_col.value = sort_col
		frm.sort_order.value = sort_order;
	}
	if(cur_page == null || cur_page == ''){
		frm.cur_page.value = "0";
	}

	if(page_size == null || page_size == ''){
		frm.page_size.value = "10";
	} else {
		frm.page_size.value = page_size;
	}
	if(chkEmpty === true && !wbUtilsValidateEmptyField(frm.srh_key, eval('wb_msg_' + lang + '_search_field'), lang)){
		frm.srh_key.focus()
		return;
	}else{
		frm.action = wb_utils_disp_servlet_url;
		frm.module.value = "JsonMod.know.KnowModule";
		frm.cmd.value = "search_que_for_mgt";
		frm.stylesheet.value = "know_search_result.xsl";
		frm.submit();
	}
}

function wbQueDetail(frm, que_id, que_type) {
	if (que_id !== undefined && que_id > 0) {
		var iTop = (window.screen.availHeight-30-800)/2;
		var iLeft = (window.screen.availWidth-10-1024)/2;   
		str_feature = 'width=' + 1024 + ',height=' + 800 + ',scrollbars=' + 'yes' + ',resizable=' + 'yes'+ ',top='+ iTop + ',left='+iLeft;
		detail_prep_win = wbUtilsOpenWin('', 'Detail_tree_prep', false, str_feature);
		detail_prep_win.location.href = '../app/know/knowDetail/' + que_type + '/' + que_id;
		detail_prep_win.focus();
	}
	return;
}

function wbQueDel(frm) {
	if(!isQueIdEmpty(frm))	 {
		_delQue(frm);
	}
}

function _delQue(frm) {
	if(confirm(wb_msg_confirm)) {
		var stylesheet = 'know_catalog_lst.xsl';
		frm.url_success.value = window.location.href;
		frm.url_failure.value = frm.url_success.value;
		frm.action = wb_utils_disp_servlet_url;
		frm.module.value = "JsonMod.know.KnowModule";
		frm.cmd.value = "del_que";
		frm.method = "post";
		frm.submit();
	}
}

function _getKcaDetailUrl(kca_id, sort_col, sort_order, cur_page, page_size){
	if(sort_col == null){
		if(sort_order == null){
			sort_order = "desc"
		}
		sort_col = "que_create_timestamp"
	}else{
		if(sort_order == null){
			sort_order = "asc"
		}
	}

	if(cur_page == null || cur_page == ''){
		cur_page = "0";
	}

	if(page_size == null || page_size == ''){
		page_size = "10";
	}
	
	var stylesheet = (kca_id > 0) ? 'know_catalog_detail.xsl' : 'know_catalog_lst.xsl';
	
	return wb_utils_invoke_disp_servlet("module","JsonMod.know.KnowModule","cmd","get_kca_lst","stylesheet",stylesheet, "kca_id", kca_id, "sort_col", sort_col, "sort_order", sort_order, "page_size", page_size, "cur_page", cur_page);
}

function isQueIdEmpty(frm) {
	var emptyInd = true;
	var kcr_time = new Array();
	var que_id = new Array();
	if (frm.que_id_chk.checked) {
		kcr_time.push(eval("frm.kcr_create_timestamp_" + frm.que_id_chk.value + ".value"));
		que_id.push(frm.que_id_chk.value);
		emptyInd = false;
	} else {
		for(var i=0; i<frm.que_id_chk.length; i++) {
			if(frm.que_id_chk[i].checked) {
				kcr_time.push(eval("frm.kcr_create_timestamp_" + frm.que_id_chk[i].value + ".value"));
				que_id.push(frm.que_id_chk[i].value);
				emptyInd = false;
			}
		}		
	}

	if(emptyInd) {
		alert(getLabel("507"));
		return true;
	} else {
		var time_tmp = "";
		var que_id_lst_tmp = "";
		for(var i=0; i<kcr_time.length; i++) {
			time_tmp += kcr_time[i];
			que_id_lst_tmp += que_id[i];
			if(i !== kcr_time.length - 1) {
				time_tmp += "~";
				que_id_lst_tmp += "~";
			}
		}
		frm.kcr_create_timestamp_lst.value = time_tmp;
		frm.que_id_lst.value = que_id_lst_tmp;
		return false;
	}
}

function showChangeCataTree(frm) {
	var root = new Ext.tree.AsyncTreeNode({
        text : Wzb.l("215"),
        expanded : true,
        disabled : true,
        draggable : false
    });
	var treePanel = new Wzb.TreePanel({
		id : "kca_tree",
		useArrows : true,
		root : root,
		autoScroll : true,
    	loader: new Ext.tree.TreeLoader({
    		 url : wb_utils_invoke_disp_servlet("module","JsonMod.know.KnowModule","cmd","get_kca_tree"),
    		 listeners : {
    		 	"load" : function() {
    		 		Ext.getCmp("kca_tree").addListener("beforeload", function() {
	    				return false;
	    			});
    		 	}
    		 }
    	}),
    	listeners: {
	       	"click" : function(node) {
	       		if(Number(node.id) > 0) {
			    	frm.kca_id.value = node.id;
	       		}
	       	}
    	}
	});
	var treeWin = new Wzb.Window({
		width : 400,
		hideBorders : true,
		title : Wzb.l("501"),
		items : [
			{
				html : WzbHtm.change_catalog({
					1 : Wzb.l("506")
				})
			},
			treePanel
		],
		buttons : [
			{
				text : Wzb.l("329"),
				handler : function() {
					if(frm.kca_id.value === "") {
						alert(Wzb.l("511"));
						return;
					}
					frm.action = Wzb.uri_dis;
					frm.module.value = "JsonMod.know.KnowModule";
					frm.cmd.value = "change_kca_exec";
					frm.url_success.value = _getKcaDetailUrl(frm.cur_kca_id.value);
					frm.url_failure.value = frm.url_success.value;
					frm.submit();
				}
			},
			{
				text : Wzb.l("330"),
				handler : function() {
					treeWin.close();
				}
			}
		],
		buttonAlign : "center"
	});
	treeWin.show();
}

function status(frm, lang){
	wbKcaSearchExec(frm, lang);
	return false;
}

function wbAddFaqPrep(kca_id) {
	window.location.href = wb_utils_invoke_disp_servlet("module","JsonMod.know.KnowModule","cmd","ins_faq_prep","stylesheet","know_faq_ins_upd.xsl", "kca_id", kca_id);
}

function wbAddFaq(frm) {
	if(checkFaqInput(frm)) {
		frm.url_success.value = _getKcaDetailUrl(frm.que_kca_id.value);
		frm.url_failure.value = window.location.href;
		frm.module.value = "JsonMod.know.KnowModule";
		frm.cmd.value = "ins_faq";
		frm.action = wb_utils_disp_servlet_url;
		frm.method = 'post';
		frm.submit();
	}
}

function wbUpdateFaq(frm) {
	
	if(checkFaqInput(frm)) {
		frm.url_success.value = _getKcaDetailUrl(frm.que_kca_id.value);
		frm.url_failure.value = window.location.href;
		frm.module.value = "JsonMod.know.KnowModule";
		frm.cmd.value = "upd_faq";
		frm.action = wb_utils_disp_servlet_url;
		frm.method = 'post';
		frm.submit();
	}
}

function checkFaqInput(frm) {
	if(frm.que_title) {
		frm.que_title.value = wbUtilsTrimString(frm.que_title.value);
		if (!wbUtilsValidateEmptyField(frm.que_title, frm.lab_que_title.value)){
			return false;
		}
		if(frm.que_title.value.length > 60) {
			alert(frm.lab_que_title_desc.value);
			return false;
		}
	}
	if(frm.que_content) {
		frm.que_content.value = wbUtilsTrimString(frm.que_content.value);
		if(frm.que_content.value.length > 500) {
			alert(frm.lab_que_content_desc.value);
			return false;
		}
	}
	if(frm.ans_content) {
		frm.ans_content.value = wbUtilsTrimString(frm.ans_content.value);
		if (!wbUtilsValidateEmptyField(frm.ans_content, frm.lab_ans_content.value)){
			return false;
		}
		if(frm.ans_content.value.length > 2000) {
			alert(frm.lab_ans_content_desc.value);
			return false;
		}
	}
	return true;
}


function wbGetFaqById(que_id) {
	window.location.href = wb_utils_invoke_disp_servlet("module","JsonMod.know.KnowModule","cmd","get_faq","stylesheet","know_faq_ins_upd.xsl", "que_id", que_id);
}