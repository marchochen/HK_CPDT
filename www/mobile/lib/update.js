//app升级
(function(w){
var server="/mobile/update/update.json";//获取升级描述文件服务器地址
var keyUpdate="updateCheck";//取消升级键名
var updateInd=true;
var checkInterval=86400000;

/**
 * 准备升级操作
 * 创建升级文件保存目录
 */
w.initUpdate = function(callback){
	var lastcheck = plus.storage.getItem(keyUpdate);
	if(lastcheck){
		var dc = parseInt(lastcheck);
		var dn = (new Date()).getTime();
		if(dn-dc<checkInterval){	// 未超过上次升级检测间隔，不需要进行升级检查
			callback && callback();
			return;
		}
	}
	if(updateInd){
		server = serverHost + server;
		
		if(server && server.indexOf("http")<0){
			callback && callback();
			return;
		}
		
		updateInd=false;
		plus.nativeUI.showWaiting(filter('translate')('update_check') + "...");
		$.ajax({
			url : server,
		 	type : 'HEAD',
			success: function() {
				plus.storage.setItem(keyUpdate, (new Date()).getTime().toString());
	    		var xhr=new XMLHttpRequest();
	    		xhr.onreadystatechange=function(){
	        		switch(xhr.readyState){
	            		case 4:
	            			if(xhr.status==200){
	            				try{
									data=JSON.parse(xhr.responseText);
								}catch(e){
									plus.nativeUI.toast(filter('translate')('updaet_data_error'));
									plus.nativeUI.closeWaiting();
									callback && callback();
									return;
								}
								checkUpdateData(data,callback);
	            			}else{
	                			plus.nativeUI.toast(filter('translate')('update_check_fail'));
	                			plus.nativeUI.closeWaiting();
	                			callback && callback();
	            			}
	            			break;
						default:
	            			break;
	        		}
	    		}
	    		xhr.open('GET',server);
	   			xhr.send();
			},
			error : function() {
				plus.nativeUI.toast(filter('translate')('update_check_fail'));
	            plus.nativeUI.closeWaiting();
	            callback && callback();
				return;
			}
		});
	}else{
		callback && callback();
	}
};

/**
 * 检查升级数据
 */
function checkUpdateData(j,callback){
	var curVer = plus.runtime.version;
	var inf = j[plus.os.name];
	if(inf){
		var srvVer = inf.version;
		// 判断是否需要升级
		if(compareVersion(curVer,srvVer)){
			if(inf.url.slice(-3) == 'wgt'){
				downWgt(inf.url);
			} else {
				// 提示用户是否升级
				plus.nativeUI.confirm(inf.note, function(i){
					plus.nativeUI.closeWaiting();
					if (0==i.index){
						plus.runtime.openURL(inf.url);
					}
					callback && callback();
				}, inf.title, [filter('translate')('update_now'), filter('translate')('btn_cancel')]);
			}
		} else {
			plus.nativeUI.closeWaiting();
			callback && callback();
		}
	} else {
		plus.nativeUI.closeWaiting();
		callback && callback();
	}
}

//下载wgt文件
function downWgt(url){
	plus.nativeUI.closeWaiting();
    plus.nativeUI.showWaiting(filter('translate')('update_download') + "...");
    plus.downloader.createDownload( url, {filename:"_doc/update/"}, function(d,status){
        if(status == 200){ 
            installWgt(d.filename); // 安装wgt包
        } else {
        	plus.nativeUI.closeWaiting();
            plus.nativeUI.toast(filter('translate')('update_download_fail'));
        }
    }).start();
}

//更新应用资源
function installWgt(path){
    plus.runtime.install(path, {}, function(){
        plus.nativeUI.closeWaiting();
        plus.nativeUI.alert(filter('translate')('update_finish'), function(){
            plus.runtime.restart();
        });
    },function(e){
        plus.nativeUI.closeWaiting();
        plus.nativeUI.toast(filter('translate')('update_wgt_fail') + "["+e.code+"]："+e.message);
    });
}

/**
 * 比较版本大小，如果新版本nv大于旧版本ov则返回true，否则返回false
 * @param {String} ov
 * @param {String} nv
 * @return {Boolean} 
 */
function compareVersion(ov, nv){
	if(!ov || !nv || ov=="" || nv==""){
		return false;
	}
	var b = false;
	var ova = ov.split(".",4);
	var nva = nv.split(".",4);
	for(var i=0; i < ova.length && i < nva.length; i++){
		var so = ova[i];
		var no = parseInt(so);
		var sn = nva[i];
		var nn = parseInt(sn);
		if(nn > no || sn.length > so.length){
			return true;
		} else if(nn < no){
			return false;
		}
	}
	if(nva.length > ova.length && 0 == nv.indexOf(ov)){
		return true;
	}
}

})(window);