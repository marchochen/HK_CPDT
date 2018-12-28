function wbRoleManager() {
	this.get_rol_list = wbRolMgtGetRoleList
	this.get_rol_detail = wbRolMgtGetRoleDetail
	this.add_rol = wbRolMgtAddRole
	this.del_rol_checkbox = wbRolMgtDeleteRoleCheckbox
	this.del_rol = wbRolMgtDeleteRole
	this.add_rol_exec = wbRolMgtAddRoleExec
	this.cnl_add_rol = wbRolMgtCancelAddRole
	this.mod_rol = wbRolMgtModifyRole
}

function wbRolMgtAddRoleExec(frm, lang) {
	url_success = wb_utils_invoke_disp_servlet('module',
			'JsonMod.role.RoleModule', 'cmd', 'get_role_list', 'stylesheet',
			'role_list.xsl', 'page_size', '10');
	url_failure = self.location.href;
	if (frm.rol_title.value == "") {
		alert(eval("wb_msg_" + lang + "_null_name"));
		return;
	} else {
		var ftn_id_lst = _getFtnLst(frm)
		if (ftn_id_lst == "") {
			alert(eval("wb_msg_" + lang + "_null_fun"));
			return;
		}
		frm.rol_tc_ind.value = _wbRolMgtGetFtnTcInd(frm);
		frm.ftn_id_lst.value = ftn_id_lst;
		frm.cmd.value = 'add_role_exec';
		frm.module.value = 'JsonMod.role.RoleModule';
		frm.url_success.value = url_success;
		frm.url_failure.value = url_failure;
		frm.action = wb_utils_disp_servlet_url;
		frm.method = 'post';
		frm.submit();
	}
}

function _getFtnLst(frm){
   var i, n, ele, str
	str = ""
	n = frm.elements.length;
	for (i = 0; i < n; i++) {
		ele = frm.elements[i]
		if (ele.type == "checkbox" && ele.checked) {
			if (ele.value !="")
				str = str + ele.value + "~"     
		}
	}
	if (str != "") {str = str.substring(0, str.length-1);}
	return str;

}

function wbRolMgtModifyRole(frm,lang){
       url_success = wb_utils_invoke_disp_servlet('module','JsonMod.role.RoleModule','cmd', 'get_role_list', 'stylesheet', 'role_list.xsl','page_size','10') ;
       url_failure = wb_utils_invoke_disp_servlet('module','JsonMod.role.RoleModule','cmd', 'get_role_list', 'stylesheet', 'role_list.xsl','page_size','10') ;
       if(frm.rol_title.value ==""){
            alert(eval("wb_msg_"+ lang +"_null_name"));
	    return;
       }else{        
            var ftn_id_lst = _wbRolMgtModifyRole(frm)
            if(ftn_id_lst ==""){
                alert(eval("wb_msg_"+ lang +"_null_fun"));
	        return;
            }
            frm.rol_tc_ind.value = _wbRolMgtGetFtnTcInd(frm);
            frm.ftn_id_lst.value = ftn_id_lst;
            frm.cmd.value = 'modify_role';
            frm.module.value = 'JsonMod.role.RoleModule';
            frm.url_success.value = url_success;
            frm.url_failure.value = url_failure;
            frm.action = wb_utils_disp_servlet_url;
	    frm.method = 'post';
	    frm.submit();
      }
}

function _wbRolMgtModifyRole(frm){
   var i, n, ele, str
   var isTimeTable=false;
   var isUsrMgt=false;
	str = ""
	n = frm.elements.length;
	for (i = 0; i < n; i++) {
		ele = frm.elements[i]
		if (ele.type == "checkbox" && ele.checked) {
			if (ele.value !="")
				str = str + ele.value + "~" ;    
				if(ele.value =='ITEM_TIMETABLE_MGT'){
					isTimeTable=true;
				}
				if(ele.value =='USR_MGT'){
					isUsrMgt=true;
				}
		}
	}
	if(isTimeTable ==true && isUsrMgt==false){
				str = str +  "USR_MGT~" ;
	}			
	if (str != "") {str = str.substring(0, str.length-1);}
	return str;
}

function _wbRolMgtGetFtnTcInd(frm) {
	var tc_ind = false;
	n = frm.elements.length;
	for (i = 0; i < n; i++) {
		ele = frm.elements[i]
		if (ele.type == "checkbox" && ele.checked) {
			var ftn_name = ele.id;
			var el = document.getElementById(ftn_name + "_tc_related");
			if (el != undefined && el.value == 'true') {
				tc_ind = true;
				break;
			}
		}
	}
	return tc_ind;
}
function wbRolMgtGetRoleDetail(rol_id){
	url = wb_utils_invoke_disp_servlet('module','JsonMod.role.RoleModule','cmd','get_role_detail','rol_id',rol_id,'stylesheet','role_detail.xsl');
	window.location.href = url;
}

function wbRolMgtAddRole(){
	url = wb_utils_invoke_disp_servlet('module','JsonMod.role.RoleModule','cmd','add_role','stylesheet','role_add.xsl');
	window.location.href = url;
}

function wbRolMgtCancelAddRole(){
        url = wb_utils_invoke_disp_servlet('module','JsonMod.role.RoleModule','cmd', 'get_role_list', 'stylesheet', 'role_list.xsl','page_size','10') ;
        window.location.href = url; 
}

function wbRolMgtDeleteRoleCheckbox(frm,lang){   
     url = window.location.href;
     var rol_id_lst = _wbRolMgtDeleteRoleCheckbox(frm)
   
	  if(rol_id_lst == ''){
		alert(eval("wb_msg_"+ lang +"_select_role"));
		return;
	  }else{
		  frm.rol_id_lst.value = rol_id_lst
		  frm.cmd.value = 'del_muti_role';
                  frm.module.value = 'JsonMod.role.RoleModule';
                  frm.url_success.value = url;
                  frm.url_failure.value = url;
		  frm.action = wb_utils_disp_servlet_url;
		  frm.method = 'post';
		  if(confirm(wb_msg_remove_role_confirm)){
			frm.submit();
		  }
	  }
}

function _wbRolMgtDeleteRoleCheckbox(frm){
   var i, n, ele, str
	str = ""
	n = frm.elements.length;
	for (i = 0; i < n; i++) {
		ele = frm.elements[i]
		if (ele.type == "checkbox" && ele.checked && ele.name=='rol_id_lst') {
			if (ele.value !=""){
				str = str + ele.value + "~"  
                        }
		}
	}
	if (str != "") {str = str.substring(0, str.length-1);}
	return str;

}


function wbRolMgtDeleteRole(frm,lang){
   
          url_success = wb_utils_invoke_disp_servlet('module','JsonMod.role.RoleModule','cmd', 'get_role_list', 'stylesheet', 'role_list.xsl','page_size','10') ;
          url_failure = window.location.href ;
   	
          frm.cmd.value = 'del_role';
          frm.module.value = 'JsonMod.role.RoleModule';
          frm.url_success.value = url_success;
          frm.url_failure.value = url_failure;
          frm.action = wb_utils_disp_servlet_url;
	  frm.method = 'post';
	  if(confirm(wb_msg_remove_role_confirm)){
	     frm.submit();
	  }

}

// *** Role List ***
function wbRolMgtGetRoleList(order_by, sort_order, cur_page, page_size){
	var url = _wbRolMgtGetRoleListUrl(order_by, sort_order, cur_page, page_size);
		window.parent.location.href = url;
}

function _wbRolMgtGetRoleListUrl(order_by, sort_order, cur_page, page_size){
	var url_failure = '';
	var url = '';

		if(order_by == null){
			if(sort_order == null){
				sort_order = 'asc'
			}
			order_by = 'rol_title'
		}else{
			if(sort_order == null){
				sort_order = 'asc'
			}
		}

		if(cur_page == null || cur_page == ''){
			cur_page = '0';
		}

		if(page_size == null || page_size == ''){
			page_size = '10';
		}
		
		var show_all = '';
		if (document.frmSearch && document.frmSearch.show_all) {
		    show_all = document.frmSearch.show_all.value;
                }

       url = wb_utils_invoke_disp_servlet('module','JsonMod.role.RoleModule','cmd','get_role_list','stylesheet','role_list.xsl', 'sort_col', order_by, 'sort_order', sort_order, 'page_size', page_size, 'cur_page', cur_page, 'url_failure', url_failure, 'show_all', show_all)
	
       return url;
}
