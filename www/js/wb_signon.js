function wbSignOn(){
	this.get_signon_lst = wbSignOnGetList;
	this.get_signon_win = wbSignOnGetWindow;
}

function wbSignOnGetList(){
	url = wb_utils_invoke_servlet("cmd","get_signon_lst","stylesheet","signon_lst.xsl");
	parent.location.href = url;
}

function wbSignOnGetWindow(id,openNewWin){
	url = wb_utils_invoke_servlet("cmd","signon","slk_id",id,"stylesheet","signon.xsl");
	if(openNewWin==null){
		parent.location.href = url;
	}else{
		var str_feature = 'width=' 				+ '780'
			+ ',height=' 				+ '500'
			+ ',scrollbars='				+ 'yes'
			+ ',resizable='				+ 'yes'
			+ ',toolbar='				+ 'no'
			+ ',screenX='				+ '10'
			+ ',screenY='				+ '10'
			+ ',status='				+ 'yes';
		if(document.all){
			str_feature += ',top='		+ '10';
			str_feature += ',left='		+ '10';
		}
		signon_win = wbUtilsOpenWin(url,'signon',false, str_feature);
	}
}
