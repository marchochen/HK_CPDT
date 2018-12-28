// ------------------ WizBank GoldenMan Object ---------------------
// Convention:
//   public functions : use "wbGoldenMan" prefix
//   private functions: use "_wbGoldenMan" prefix
// ------------------------------------------------------------
/* constructor */
//Class wbGoldenMan==========

function wbGoldenMan(){
	this.opentree = wbGoldenManOpenTreeWin
	this.openitemaccwin = wbGoldenManOpenItemAccessWin
	this.openitemaccwininst = wbGoldenManOpenItemAccessWinInstr
	this.openitemacc = wbGoldenManOpenItemAccessURL
	this.pickitemacc = wbGoldenManPickItemAccess
	this.openitemaccsearchwin = wbGoldenManOpenItemAccessSearchWin
	this.openitemaccsearchwinexe = wbGoldenManOpenItemAccessSearchWinExe
	this.moveItemUp = wbGoldenManMoveItemUp
	this.moveItemDown = wbGoldenManMoveItemDown
	this.pickitemtcrwin = wbGoldenManOpenTcrWin
	this.pickitemtcr = wbGoldenManPickTcr
	this.pickmultitcrwin = wbGoldenManOpenMultiTcrWin
	this.pickmultitcr = wbGoldenManPickMultiTcr

	this.pickappr = wbGoldenManPickApprover

	this.set_global_catalog_label = wbGoldenManSetGlobalCataLabel
}

//======================================================================================//
function wbGoldenManPickApprover(frm,size){
	//var str='user_group_and_user'
	var str = '';
	var delimiter = "~%~";
	var sub_delimiter = "[|]";
	for(i=0;i<frm.elements.length;i++){
		flag = false;
		if( size == 1 ) {
			if((frm.elements[i].type == 'radio') && (frm.elements[i].checked == true))
				flag = true;
		} else {
			if((frm.elements[i].type == 'checkbox') && (frm.elements[i].checked == true))
				flag = true;
		}
		if( flag ){
			str += delimiter + eval("usr_" +frm.elements[i].value + "_id") + sub_delimiter + eval("usr_" +frm.elements[i].value + "_role")
			    + delimiter + eval("usr_" +frm.elements[i].value + "_name")
			    + delimiter ;
		}
	}

	if( str == '' ) {
		self.close();
		return;
	}
	js_name = getUrlParam('js_name');

	if(js_name!= null){
		my_function = new Function();
		my_function = eval('window.opener.' + js_name);
		my_function(str);
		self.close();
	}else{
		alert('add_fail');
	}
	return;
}


function wbGoldenManMoveItemUp(ele){
	var idx = ele.selectedIndex;
	if(idx<1)
		return;
	var tmpValue = ele.options[idx-1].value;
	var tmpText = ele.options[idx-1].text;
	ele.options[idx-1].value = ele.options[idx].value;
	ele.options[idx-1].text = ele.options[idx].text;
	ele.options[idx].value = tmpValue;
	ele.options[idx].text = tmpText;
	ele.selectedIndex--;
}

function wbGoldenManMoveItemDown(ele){
	var idx = ele.selectedIndex;
	if(idx<0 || idx>=ele.options.length-1)
		return;
	var tmpValue = ele.options[idx+1].value;
	var tmpText = ele.options[idx+1].text;
	ele.options[idx+1].value = ele.options[idx].value;
	ele.options[idx+1].text = ele.options[idx].text;
	ele.options[idx].value = tmpValue;
	ele.options[idx].text = tmpText;
	ele.selectedIndex++;
}

//获取url指定参数的值
function GetUrlParam(paraName) {
　　　　var url = document.location.toString();
　　　　var arrObj = url.split("?");

　　　　if (arrObj.length > 1) {
　　　　　　var arrPara = arrObj[1].split("&");
　　　　　　var arr;

　　　　　　for (var i = 0; i < arrPara.length; i++) {
　　　　　　　　arr = arrPara[i].split("=");

　　　　　　　　if (arr != null && arr[0] == paraName) {
　　　　　　　　　　return arr[1];
　　　　　　　　}
　　　　　　}
　　　　　　return "";
　　　　}
　　　　else {
　　　　　　return "";
　　　　}
　　}


function wbGoldenManOpenTreeWin(tree_type,pick_method,js,item_type_list,pick_leave,approved_list,flag,close_option,pick_root,override_appr_usg, tree_subtype, get_supervise_group,complusory_tree,get_direct_supervise,ftn_ext_id,isReturnUrl, parent_tcr_id, filter_user_group, confirm_function, confirm_msg, show_bil_nickname,sgp_id, itm_id,rol_ext_id,tc_id){
	if(pick_leave == null || pick_leave == '') {pick_leave = '1';}
	if(pick_root == null || pick_root == "") {pick_root = "1";}
	if (close_option == null || close_option == '') {close_option = '1';}
	if(sgp_id ==null || sgp_id==''){sgp_id = '0';}
	if(itm_id ==null || itm_id==''){itm_id = '0';}
	url = wb_utils_invoke_servlet('cmd','gen_tree','tree_type',tree_type,'tree_subtype',tree_subtype,'stylesheet','gen_tree.xsl','js',js,'sgp_id',sgp_id, 'itm_id', itm_id)

	if (approved_list != '' && approved_list != null) { url += '&approved_list=' + approved_list;}
	if (flag != '' && flag != null) { url += '&flag=' + flag;}
	default_height = 450;
	default_width = 680;

	switch(tree_type){
		case 'targeted_learner' :
			width = default_width;height = default_height;url += "&pick_root="+pick_root;break;
		case 'grade' :
			width = default_width;height = default_height;url += "&pick_root="+pick_root;break;
		case 'industry' :
			width = default_width;height = default_height;url += "&pick_root="+pick_root;break;
		case 'competence' :
			width = default_width;height = default_height;
			url+='&pick_leave='+pick_leave;
			//pick_method = 1 ;
			url += "&pick_root="+pick_root;break;
		case 'user_group_and_user' :
			width = default_width;height = default_height;
			url+='&pick_leave='+pick_leave;
			url += "&pick_root="+pick_root;
			url += "&show_bil_nickname="+show_bil_nickname;
			//pick_method = 1 ;
			break;
		case 'catalog' :
			width = default_width;height = default_height;
			url+= '&pick_root=0'
			if(item_type_list){
			url+='&item_type_lst=' + item_type_list
			};
			break;
		case 'knowledge_object' :
			width = default_width;height = default_height;
			url+='&pick_leave='+pick_leave;
			pick_method = 1 ;
			url += "&pick_root="+pick_root;
			break;
		case 'item' :
			width = default_width; height = default_height;
			url += "&pick_root="+pick_root;
			if(item_type_list){
			url+='&item_type_lst=' + item_type_list
			};
			break;
		case 'item_from_catalog' :	// return item_id from a catalog instead of tnd_id
			width = default_width; height = default_height;
			url += "&pick_root="+pick_root;
			if(item_type_list){
			url+='&item_type_lst=' + item_type_list
			};
			break;
		case 'km_domain_and_object' :
			width = default_width; height = default_height;
			url += "&pick_root="+pick_root;
			break;
		default :
			width = default_width; height = default_height; url += "&pick_root="+pick_root;break;
	}

		feature = 'location='		+ 'no'
		+ ',width=' 				+ width
		+ ',height=' 				+ height
		+ ',scrollbars='			+ 'no'
		+ ',resizable='				+ 'no'
		+ ',screenX='				+ '100'
		+ ',screenY='				+ '100'
		+ ',status='				+ 'no'

	url += '&pick_method=' + pick_method;
	url += '&close_option=' + close_option;
	url += '&override_appr_usg=' + override_appr_usg;
	url += '&get_supervise_group=' + get_supervise_group;
	if(ftn_ext_id != null) {
		url += '&ftn_ext_id=' + ftn_ext_id;
	}
	if(complusory_tree != null){
		url+='&complusory_tree=' + complusory_tree;
	}
	if(get_direct_supervise != null){
		url+='&get_direct_supervise=' + get_direct_supervise;
	}
	if(parent_tcr_id != null){
		url+='&parent_tcr_id=' + parent_tcr_id;
	}
	if(filter_user_group != null){
		url+='&filter_user_group=' + filter_user_group;
	}
	if(confirm_function != null){
		url+='&confirm_function=' + confirm_function;
	}
	if(confirm_msg != null){
		url+='&confirm_msg=' + confirm_msg;
	}
	if(rol_ext_id != null) {
		url += '&search_rol_ext_id=' + rol_ext_id;
	}
	//LN模式下，修改和创建时树的区分
	var groupType = GetUrlParam("cmd");
	if(groupType == 'gen_tree'){
		groupType = GetUrlParam("groupType");
	}
	//添加上2个参数，用于功能实现
	if(isReturnUrl){
		return url  + "&tc_id=" + tc_id + "&groupType=" + groupType;
	}else{
		url = url + "&create_tree=y"  + "&tc_id=" + tc_id + "&groupType=" + groupType;
		goldenman_open_tree_win = wbUtilsOpenWin(url, 'win', false, feature);
		return goldenman_open_tree_win;
	}

}

function wbGoldenManOpenItemAccessWin(root_ent_id,role_lst,js_name,max_select,lang,tcr_id,tcr_name){
	/*if(tcr_id == null) {
		//the item access box does not have training center connection
		tcr_id = 0
	}else*/ if(tcr_id == null || tcr_id == 0 || tcr_id == '') {
		//the item access box has training center connection
		//but no training center selected
		/*if(tcr_name =='' || tcr_name ==null){*/
			alert(wb_msg_tc);
		/*} else {
			alert(eval('wb_msg_'+ lang +'_sel') + ' ' + tcr_name)
		}*/
		return false
	}

	url = wb_utils_invoke_servlet('cmd','search_ent_lst','ent_id',root_ent_id,'stylesheet','itm_usr_add_list.xsl','s_role_types',role_lst,'s_sort_by' ,'usr_display_bil','js_name',js_name,'max_select', max_select,'s_tcr_id',tcr_id)
		feature = 'toolbar='		+ 'no'
		+ ',location='				+ 'no'
		+ ',width=' 				+ 520
		+ ',height=' 				+ 400
		+ ',scrollbars='			+ 'yes'
		+ ',resizable='				+ 'yes'
		+ ',screenX='				+ '100'
		+ ',screenY='				+ '100'
		+ ',status='				+ 'no'

	wbUtilsOpenWin(url, 'win',false,feature)


}

function wbGoldenManOpenItemAccessWinInstr(root_ent_id,role_lst,js_name,max_select,lang,dependant_field,tcr_name){
	var type = 'IN';
		if(dependant_field){
			if(dependant_field[1].checked == true){
				type ='EXT';
			}
		}

    stylesheet = 'instructor_list.xsl'; 	
	instrSearchPoup(root_ent_id,js_name,max_select,lang,type, 'false',0);
}


function wbGoldenManOpenItemAccessURL(order_by){
	url = setUrlParam('s_order_by', order_by, window.location.href)
	window.location.href= url;
}
//获取url参数
function getQueryString(name) { 
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i"); 
	var r = window.location.search.substr(1).match(reg); 
	if (r != null) return unescape(r[2]); return null; 
} 

function wbGoldenManPickItemAccess(frm,max_select){
	var i=0;j=0;str='user_group_and_user'
	var delimiter = "~%~"
	if(max_select != '1'){
		var check_num = 0;
		for(i=0;i<frm.elements.length;i++){
			if((frm.elements[i].type == 'checkbox') && (frm.elements[i].checked == true) && (frm.elements[i].name != 'sel_all_checkbox')){
				check_num++;
				str+= delimiter + eval("usr_" +frm.elements[i].value + "_id") + delimiter + eval("usr_" +frm.elements[i].value + "_name") + delimiter + "USR"
			}
		}
		if(check_num == 0){
			var js_name = getQueryString("js_name");
			if(js_name == "item_access_TADM_box0" || js_name == "item_access_TADM_box"){//培训管理员
				alert(eval('wb_msg_ils_no_TAMD'));
			}
			if(js_name == "item_access_INSTR_box1"){//讲师
				alert(eval('wb_msg_ils_no_instructor'));
			}
			if(js_name == "item_access_EXA_box2"){//考试监管员
				alert(eval('wb_msg_ils_no_exam_super'));
			}
			return;
		}
	}else{
		if(frm.usr.length){
		for(i=0;i<frm.usr.length;i++){
			if(frm.usr[i].checked == true){
				j = i
				break;
			}
		}
		str+= delimiter + eval("usr_" +frm.usr[j].value + "_id")+ delimiter + eval("usr_" +frm.usr[j].value + "_name") + delimiter + "USR"
		}else{
			if(frm.usr.checked == true){
				str+= delimiter + eval("usr_" +frm.usr.value + "_id")+ delimiter + eval("usr_" +frm.usr.value + "_name") + delimiter + "USR"
			}

		}
	}
	js_name = getUrlParam('js_name');
	if(js_name!= null){
	my_function = new Function()
	my_function = eval('window.opener.' + js_name)
	my_function(str)
	self.close()
	}else{
		alert('add_fail');
	}
}

function wbGoldenManOpenItemAccessSearchWin(root_ent_id,role_lst,js_name,lang,max_select,tcr_id,tcr_name,selectOpt,refreshOpt){
	/*if(tcr_id == null) {
		//the item access box does not have training center connection
		tcr_id = 0
	}else*/ if(tcr_id == null || tcr_id == 0 || tcr_id == '') {
		//the item access box has training center connection
		//but no training center selected
		/*if(tcr_name =='' || tcr_name ==null){*/
			alert(wb_msg_tc);
		/*} else {
			alert(eval('wb_msg_'+ lang +'_sel') + ' ' + tcr_name)
		}*/
		return false
	}


	if(max_select==null)
		max_select = "";

	if(refreshOpt == null || refreshOpt == ''){
		refreshOpt = '0';
	}

	feature = 'toolbar='				+ 'no'
		+ ',location='				+ 'no'
		+ ',width=' 				+ 520
		+ ',height=' 				+ 400
		+ ',scrollbars='			+ 'yes'
		+ ',resizable='				+ 'yes'
		+ ',screenX='				+ '100'
		+ ',screenY='				+ '100'
		+ ',status='				+ 'no'

	url = wb_utils_invoke_servlet('cmd','get_meta','ent_id',root_ent_id,'stylesheet','itm_usr_simple_search.xsl','s_role_types',role_lst,'s_sort_by' ,'usr_display_bil','js_name',js_name,'max_select',max_select,'sel_opt', selectOpt,'refresh_opt',refreshOpt);
		if(tcr_id != null && tcr_id!=''){
	       url += '&s_tcr_id=' + tcr_id;
	}
	wbUtilsOpenWin(url,'popup_usr_search',false,feature);
}

function wbGoldenManOpenItemAccessSearchWinExe(frm, lang){
    if(_wbUserGroupValidateSearchFrm(frm, lang)){
		if(frm.s_grade && frm.s_grade_lst.options[0]){
			frm.s_grade.value = frm.s_grade_lst.options[0].value;
		}

		if(frm.usr_group_lst.options[0]){
			frm.ent_id.value = frm.usr_group_lst.options[0].value;
		}

		if(frm.s_idc_fcs && frm.s_idc_fcs_lst.options[0]){
			frm.s_idc_fcs.value = frm.s_idc_fcs_lst.options[0].value;
		}

		if(frm.s_idc_int && frm.s_idc_int_lst.options[0]){
			frm.s_idc_int.value = frm.s_idc_int_lst.options[0].value;
		}

		if(frm.fld){
			if(getUrlParam('fld')){
				frm.fld.value = getUrlParam('fld');
			}
		}

		if(frm.sel_opt){
			if(getUrlParam('sel_opt')){
				frm.sel_opt.value = getUrlParam('sel_opt');
			}
		}

		if(frm.close_opt){
			if(getUrlParam('close_opt')){
				frm.close_opt.value = getUrlParam('close_opt');
			}
		}

		if(frm.ent_id && frm.s_group_lst){
			if(frm.s_group_lst.options[0]){
				frm.ent_id.value = frm.s_group_lst.options[0].value;
			}
		}

		if(frm.s_role_types){
			frm.s_role_types.value = _wbUserGroupAdvSearchRoleLst(frm);
		}

		if(frm.s_itm_id && getUrlParam("s_itm_id") != ""){
			frm.s_itm_id.value = getUrlParam("s_itm_id");
		}

		if(frm.s_search_enrolled && getUrlParam("s_search_enrolled") != ""){
			frm.s_search_enrolled.value = getUrlParam("s_search_enrolled");
		}

		if(frm.s_search_role && getUrlParam('s_search_role') != ""){
			frm.s_search_role.value = getUrlParam("s_search_role");
		}

		if(frm.refresh_opt && getUrlParam('refresh_opt') != ""){
			frm.refresh_opt.value = getUrlParam("refresh_opt");
		}

		if(frm.s_ftn_ext_id && getUrlParam('ftn_ext_id') != ""){
			frm.s_ftn_ext_id.value = getUrlParam("ftn_ext_id");
		}

		if(frm.disabled_opt && getUrlParam('disabled_opt') != ""){
			frm.disabled_opt.value = getUrlParam("disabled_opt");
		}

		frm.action = wb_utils_servlet_url

		frm.stylesheet.value = 'itm_usr_srh_result.xsl';
		if( frm.auto_enroll_ind && getUrlParam('auto_enroll_ind') != "") {
			frm.auto_enroll_ind.value = getUrlParam('auto_enroll_ind');
		}
		if(frm.s_tcr_id && getUrlParam('s_tcr_id')!=""){
		   frm.s_tcr_id.value = getUrlParam('s_tcr_id');
		}
		if(frm.s_role_types&&getUrlParam('s_role_types')!=""){
		   frm.s_role_types.value = getUrlParam('s_role_types');
		}
		if(frm.js_name && getUrlParam('js_name')!=""){
		   frm.js_name.value = getUrlParam('js_name');
		}
		frm.cmd.value = 'search_ent_lst'
		frm.method = "get"
		frm.submit()
	}
}



function wbGoldenManSetGlobalCataLabel(args,lang){
    if(args[1] != null){
	var i=0;
	var j=0;
	var new_args = new Array();
	var labeled_args = "";
	//modify args[1] label with global cata

	var split_val = args[1].split("~%~")
		for(j=0;j<split_val.length;j++){
			//alert(split_val[j])
			if(j%3 == 2){//label

				labeled_args +=  "~%~" + split_val[j] + " " + eval("wb_msg_" + lang + "_global")
			}else{
				if(j!=0){
					labeled_args +=  "~%~"
				}
				labeled_args += split_val[j]
			}
		}
	//alert(labeled_args)
	//contruct new args
	for(i=0;i<args.length;i++){
		if(i != 1){
		new_args[i] += args[i];
		}else{
			new_args[i] = labeled_args
		}
	}
	return new_args;
   }else{
	return args;
   }
}

function wbGoldenManOpenMultiTcrWin(js_name){
	url = wb_utils_invoke_disp_servlet('module', 'trainingcenter.TrainingCenterModule', 'cmd', 'tc_lst', 'js_name',js_name, 'stylesheet', 'itm_search_tcr_add_list.xsl');
	//url = '../htm/out.htm?js_name='+js_name
		feature = 'toolbar='		+ 'no'
		+ ',location='				+ 'no'
		+ ',width=' 				+ 520
		+ ',height=' 				+ 400
		+ ',scrollbars='			+ 'yes'
		+ ',resizable='				+ 'yes'
		+ ',screenX='				+ '100'
		+ ',screenY='				+ '100'
		+ ',status='				+ 'no'

	wbUtilsOpenWin(url, 'win',false,feature)
}

function wbGoldenManPickMultiTcr(frm){
	var i=0;j=0;str='TrainingCenter'
	var delimiter = "~%~"
	if(frm.int_chk.length){
		for(i=0;i<frm.int_chk.length;i++){
			if(frm.int_chk[i].checked == true){
				j = i
				str+= delimiter + eval("inttp_" +frm.int_chk[j].value + "_id")+ delimiter + eval("inttp_" +frm.int_chk[j].value + "_name") + delimiter + "TCR"
			}
		}
	}else{
		if(frm.int_chk.checked == true){
			str+= delimiter + eval("inttp_" +frm.int_chk.value + "_id")+ delimiter + eval("inttp_" +frm.int_chk.value + "_name") + delimiter + "TCR"
		}
	}
	js_name = getUrlParam('js_name');
	if(str=='TrainingCenter') {
		self.close();
	} else if(js_name!= null){
		my_function = new Function()
		my_function = eval('window.opener.' + js_name)
		my_function(str)
		self.close()
	}else{
		alert('add_fail');
	}
}

function wbGoldenManOpenTcrWin(js_name){
	url = wb_utils_invoke_disp_servlet('module', 'trainingcenter.TrainingCenterModule', 'cmd', 'tc_lst', 'js_name',js_name, 'stylesheet', 'itm_tcr_add_list.xsl');
	//url = '../htm/out.htm?js_name='+js_name
		feature = 'toolbar='		+ 'no'
		+ ',location='				+ 'no'
		+ ',width=' 				+ 520
		+ ',height=' 				+ 400
		+ ',scrollbars='			+ 'yes'
		+ ',resizable='				+ 'yes'
		+ ',screenX='				+ '100'
		+ ',screenY='				+ '100'
		+ ',status='				+ 'no'

	wbUtilsOpenWin(url, 'win',false,feature)
}

function wbGoldenManPickTcr(frm){
	var i=0;j=0;str='TrainingCenter'
	var delimiter = "~%~"
	if(frm.int_radio.length){
		for(i=0;i<frm.int_radio.length;i++){
			if(frm.int_radio[i].checked == true){
				j = i
				str+= delimiter + eval("inttp_" +frm.int_radio[j].value + "_id")+ delimiter + eval("inttp_" +frm.int_radio[j].value + "_name") + delimiter + "TCR"
				break;
			}
		}
	}else{
		if(frm.int_radio.checked == true){
			str+= delimiter + eval("inttp_" +frm.int_radio.value + "_id")+ delimiter + eval("inttp_" +frm.int_radio.value + "_name") + delimiter + "TCR"
		}
	}
	js_name = getUrlParam('js_name');
	if(str=='TrainingCenter') {
		self.close();
	} else if(js_name!= null){
		my_function = new Function()
		my_function = eval('window.opener.' + js_name)
		my_function(str)
		self.close()
	}else{
		alert('add_fail');
	}
}

function wbSingleGoldenMan(){
	this.options = new Array()
	this.options[0] =  new wbGoldenOption()

}
function wbGoldenOption(){
	this.length = 1;
	this.value =  null;
	this.text = null;
}


function TreeVal(){
	this.type = null;
	this.items = new Array()
	this.init = _TreeValInitTree
}
function TreeValItem(){
	this.id = null;
	this.name = null;
	this.type = null;
}
//Method
function _TreeValInitTree(rawargs){
	args = new String(rawargs)
	var split_val = new Array()
	var i=0;
	split_val = args.split("~%~")
	//args type
	this.type = split_val[0];

	var group_num = Math.floor((split_val.length - 1)/3)
	//put item value
	for(i=0;i<group_num;i++){
		if(split_val[1+ ((i*3) +1 )] != null){
		this.items[i] = new TreeValItem
		this.items[i].id = split_val[1 +(i * 3)]
		this.items[i].name = split_val[1+ ((i*3) +1 )]
		this.items[i].type = split_val[1+((i*3) +2)]
		}
	}
}

function AddSingleOption(frm,str){
	var i=0;
	frm.options[0].value = ''
	frm.options[0].text = ''
	var split_val = new Array()
	for(i=0;i<str.length;i++){
		a = new String(unescape(str[i]));
		split_val = a.split('~%~');
		frm.options[0].value += split_val[1]
		frm.options[0].text += split_val[2]
		if (i < str.length - 1 ){
			frm.options[0].value += '~'
			frm.options[0].text += ','
		}
	}
}

function RemoveSingleOption(singlefield,frm){
	singlefield.value = '';
	frm.options[0].text = '';
	frm.options[0].value = '';
}

function RemoveAllOptions(allOptionfield,frm){
	var i=0
	var n, ele
	n = allOptionfield.options.length;
	while(allOptionfield.options.length > 0) {
		ele = allOptionfield.options[i]
		ele.text = '';
		ele.value = '';
		allOptionfield.options[i] = null;
	}
}

function AddTreeOption(frm,size,args,args_type){
			var set = new Array()
			var row_set = new Array()
			var i=0;
			var j=0;
			var k=0;
			//Variable "args_type" : "row" or "col" , Default : "col"
			//eg. args_type = "row"
			//	+----------------+
			//	|args[0]         |
			//	|args[1]         |
			//	+----------------+
			//eg. args_type = "col"
			//	+----------------+
			//	|args[0],args[1] |
			//	+----------------+
			//

			if(args_type == "row" ){
				//row case:
				set[0] = new TreeVal
				for(i=0;i<args.length;i++){
					row_set[i] =  new TreeVal
					row_set[i].init(unescape(args[i]))
					for(l=0;l<row_set[i].items.length;l++){
						set[0].items[set[0].items.length] = row_set[i].items[l]
					}
				}
				set[0].type = row_set[0].type;
			}else{
				//col case:
				for(i=0;i<args.length;i++){
					set[i] =  new TreeVal
					set[i].init(unescape(args[i]))
				}
			}


			if(args.length <=1 || args_type == 'row'){
				for(i=0;i<set[0].items.length;i++){
					var can_add = true;
					if(frm.type && frm.type=="custom")
						tempOption = new wbMultipleGoldenOption()
					else
						tempOption =  new Option
					tempOption.text = set[0].items[i].name;
					tempOption.value = set[0].items[i].id
					current_option_length = frm.options.length
					for(j=0;j<current_option_length;j++){
						if(tempOption.value == frm.options[j].value){
							can_add = false
							break;
						}
					}
					if(size != 0){
						if((can_add == true) && (frm.options.length < size)){
							addOption(frm,tempOption)
						}
					}
					else
					{
						if(can_add == true){
							addOption(frm,tempOption)
						}
					}
				}

			}
			else
			{
				var can_add = true;
				var str = '';
				if(frm.type && frm.type=="custom")
					tempOption = new wbMultipleGoldenOption()
				else
					tempOption =  new Option
				tempOption.text = ''
				tempOption.value =''
				for(k=0;k<set.length;k++){
					if(set[k].items[0] != null){
						str+= set[k].items[0].name + ", ";
						tempOption.value+= set[k].items[0].id + "~"
					}
				}
				str = str.substring(0,(str.length -2))
				tempOption.text = str;
				tempOption.value = tempOption.value.substring(0,(tempOption.value.length -1))
				current_option_length = frm.options.length
				//check for repeat value
				for(j=0;j<current_option_length;j++){

					mychecklist =  new Array()
					frmvaluelist =  new Array()
					mychecklist = tempOption.value.split("~")
					frmvaluelist = frm.options[j].value.split("~")
					var idx=0;
					var count=0;
					if(frmvaluelist.length == mychecklist.length){
						for(idx=0;idx < mychecklist.length;idx++){
							if(frmvaluelist[idx] == mychecklist[idx]){
								count ++;
							}
						}
					}
					if(count == mychecklist.length){
					can_add = false
					break;
					}
				}
				if(size != 0){
					if((can_add == true) && (frm.options.length < size)){
						addOption(frm,tempOption)
					}
				}
				else
				{
					if(can_add == true){
						addOption(frm,tempOption)
					}
				}

			}
	}

function wbMultipleGoldenMan(){
	this.options = new Array();
	this.type = "custom";
}

function wbMultipleGoldenOption(){
	this.selected = false;
	this.value = null;
	this.text = null;
}

