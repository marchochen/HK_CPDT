
function startStatusCheck(frm, lang, isExcludes) {
	layer.load(0, {shade: [0.1,'#fff']});
	cn = setInterval('getStatus("' + frm.url_success.value + '","'+frm.url_failure.value+'","' + lang + '","' + isExcludes +'")', 1000);
	return true;
}

function getStatus(url_success, url_failure,  lang, isExcludes) {
	$.ajax({
				type : "POST",
				url : "../servlet/Dispatcher?isExcludes=true",
				data : {
					cmd : 'get_status',
					module : 'JsonMod.upload.UploadModule'
				},
				success : function(data) {
					var udata = eval("(" + data + ')')['upload'];
					if (udata.new_res_id && udata.new_res_id > 0) {
						wb_utils_set_cookie('mod_id', udata.new_res_id);
					}
					if (udata.currentStatus == 'error') {
						clearInterval(cn);
						wb_utils_close_preloading();
						document.location.href = wb_utils_invoke_disp_servlet('cmd', 'show_sysmsg', 'module', 'JsonMod.upload.UploadModule', 'sysmsg_id', udata.returnMsg,
								'cos_id', udata.cos_id, 'new_res_id', udata.new_res_id, 'url_success', url_failure,'status','ERROR','isExcludes', isExcludes
						);
					} else if (udata.currentStatus == 'finish') {
						clearInterval(cn);
						layer.closeAll('loading');
						$.ajax({
									url : '../servlet/Dispatcher',
									data : {
										cmd : 'finish',
										module : 'JsonMod.upload.UploadModule'
									}
								});
						document.location.href = wb_utils_invoke_disp_servlet('cmd', 'show_sysmsg', 'module', 'JsonMod.upload.UploadModule', 'sysmsg_id', udata.returnMsg,
								'cos_id', udata.cos_id, 'new_res_id', udata.new_res_id, 'url_success', url_success,'isExcludes', isExcludes
						);
					}
				}
			});
}