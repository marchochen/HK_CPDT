function wbSSO() {	
	this.get_sso_link = wbGetSSOLinkQuery
	this.get_tnd_sso_link = wbGetTndSSOLink
	this.get_itm_sso_link = wbGetItmSSOLink
	this.get_mod_sso_link = wbGetModSSOLink
}


function wbGetSSOLinkQuery (frm) {
var sso_url = '-1';
var url_success;
    if (frm.sso_link.length > 0) {
        obj = frm.sso_link;
        for (i=0;i<obj.length;i++) {
            if (obj[i].value == 1) {
                if (document.getElementById('sso_link_0').checked) {
                    sso_url = handelLink(1, frm);
                }
            } else if (obj[i].value == 2) {
                if (document.getElementById('sso_link_1').checked) {
                    sso_url = handelLink(2, frm);
                }
            } else if (obj[i].value == 3) {
                if (document.getElementById('sso_link_2').checked) {
                    sso_url = handelLink(3, frm);
                }
            } else if (obj[i].value == 4) {
                if (document.getElementById('sso_link_3').checked) {
                    sso_url = handelLink(4, frm);
                }
            } else if (obj[i].value == 5) {
                if (document.getElementById('sso_link_4').checked) {
                    sso_url = handelLink(5, frm);
                }
            }
        }
    } else {
        obj = frm.sso_link;
        if (obj.value == 1) {
            if (document.getElementById('sso_link_0').checked) {
                sso_url = handelLink(1, frm);
            }
        } else if (obj.value == 2) {
            if (document.getElementById('sso_link_1').checked) {
                sso_url = handelLink(2, frm);
            }
        } else if (obj.value == 3) {
            if (document.getElementById('sso_link_2').checked) {
                sso_url = handelLink(3, frm);
            }
        } else if (obj.value == 4) {
            if (document.getElementById('sso_link_3').checked) {
                sso_url = handelLink(4, frm);
            }
        } else if (obj.value == 5) {
            if (document.getElementById('sso_link_4').check) {
                sso_url = handelLink(5, frm);
            }
        }
    }
    //copy sso link to clipboard
    //if sso_url=='-2' means redio checked but no value,if sso_url=='-1' means no redio checked
    if (sso_url!='-2' && sso_url!= '-1') {
        frm.sso_link_text.value = sso_url;
        copyStr(frm.sso_link_text);
    }else if (sso_url == '-1'){
        alert(wb_pls_specify_sso_type);
    }
}

function handelLink(link_no, frm) {
    var sso_url;
    switch(link_no) {
        case 1:
            url_success = wb_utils_gen_home_url(true);
            sso_url = frm.root.value + frm.learner_home.value + 'url_success=' + escape(url_success);
            break;
        case 2:
            url_success = 'cur_page=' + '1';
            sso_url = frm.root.value + frm.approval_list.value + url_success;
            break;
        case 3:
            if (frm.tnd_id.options[0].value != '') {
                url_success = 'tnd_id=' + frm.tnd_id.options[0].value;
            } else {
                alert(wb_pls_specify_tnd);
                return -2;
            }
            sso_url = frm.root.value + frm.learner_catalog.value + url_success;
            break;
        case 4:
            if (frm.itm_id.options[0].value != '') {
                url_success = 'itm_id=' + frm.itm_id.options[0].value;
            } else {
                alert(wb_pls_specify_itm);
                return -2;
            }
            sso_url = frm.root.value + frm.learner_course_home.value + url_success;
            break;
        case 5:
            if (frm.mod_id.value != '') {
                url_success = 'mod_id=' + frm.mod_id.value;
            } else {
                alert(wb_pls_specify_mod);
                return -2;
            }
            sso_url = frm.root.value + frm.learner_module.value + url_success;
            break;
    }
    return sso_url;
}



function copyStr(str_text) {
    regin = str_text.createTextRange();
    regin.execCommand("Copy");
    regin.collapse(false);
    alert(wb_sso_link_copied);
    return;
}

function wbGetTndSSOLink(tnd_id, frm) {
    sso_url = frm.root.value + frm.learner_catalog.value + 'tnd_id=' + tnd_id;
    frm.sso_link.value = sso_url;
    copyStr(frm.sso_link);
}

function wbGetItmSSOLink(itm_id, frm) {
    sso_url = frm.root.value + frm.learner_course_home.value + 'itm_id=' + itm_id;
    frm.sso_link.value = sso_url;
    copyStr(frm.sso_link);
}

function wbGetModSSOLink(mod_id, frm) {
    sso_url = frm.root.value + frm.learner_module.value + 'mod_id=' + mod_id;
    frm.sso_link.value = sso_url;
    copyStr(frm.sso_link);
}