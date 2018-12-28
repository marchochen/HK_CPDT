appModule.service('wizUpload', function(){
	return uploadFile;
});

function uploadFile(option){
	if(!option) return;
	var file = option.file;            //上传的文件文件
	
	if(file && file.name != decodeURI(file.name)){
		app.alert('上传的文档请不要使用中文文件名', 2000);//无需国际化
		return;
	}
	
	var url = option.url;              //请求的url
	var onprogress = option.loading;   //正在上传时的回调函数，方便记录上传量
	var success = option.success;      //上传成功之后的回调函数
	var fail = option.fail;      //上传失败之后的回调函数
	var data = option.data;            //上传额外数据
	var showProgress = option.showProgress; //是否显示上传进度条
	
	if(url && url != ''){
		var formData = new FormData();
		if(data && data instanceof Object ){
			for(var key in data){
				formData.append(key, data[key]);
			}
		}
		formData.append('file', file);
		var token = null;
		if(window.localStorage){
			token = window.localStorage.getItem("token") || window.sessionStorage.getItem("token");
		}
		formData.append('token', token);
		formData.append('developer', 'mobile');
		formData.append('uploadType', 'ajax');
		//formData.append('callback', 'JSON_CALLBACK');
		var xhr = new XMLHttpRequest();
		xhr.open("POST",url, true);
   		xhr.setRequestHeader('X-Requested-With', 'XMLHttpRequest', 'Content-Type', 'multipart/form-data;');
   		xhr.setRequestHeader('Accept', 'application/json');
   		
   		var template , body , progressEl;
   		
   		if(showProgress){//显示进度条
			template = '<div class="header-overlay" style="display:block;"><div class="jiazai-mobile"><div class="jiazai-wbload"><span id="upload-progress-bar" style="width:0%;"></span></div></div></div>';
			body = document.body;
			progressEl = document.createElement("div");
			progressEl.innerHTML = template;
			body.appendChild(progressEl);
		}
   		
   		xhr.upload.onprogress = function(evt){
   			
   			if(showProgress){
   				//设置进度
   				var progressBar = document.getElementById("upload-progress-bar");
   				var loaded = evt.loaded;     //已经上传大小情况 
   				var tot = evt.total;      //附件总大小 
   				var per = Math.floor(100*loaded/tot);  //已经上传的百分比
   				
   				progressBar.style.width = per+"%";
   			}
   			
   			if(onprogress && typeof(onprogress) == 'function'){
   				onprogress(evt);//进度回调
   			}
   		}
   		
   		xhr.send(formData);
   		
   		xhr.onload = function(e){
   			
   			if(body && progressEl){
   				window.setTimeout(function(){//延时取消，让用户看到效果
   					body.removeChild(progressEl);
   				},1000);
   			}
   			
     		if(this.status == 200){
     			var responseJson = eval('(' + this.responseText + ')');
     			if(responseJson.status == 'fail'){
     				app.alert(responseJson.errorMsg);
     				if(fail && typeof(fail) == 'function'){
     					fail();
           			}
     			}else{
     				if(success && typeof(success) == 'function'){
           				success($.parseJSON(this.responseText));
           			}
     			}
       		} else if(this.status == 500){
       			if(fail && typeof(fail) == 'function'){
 					fail();
       			}
       		} else if(this.status == 404){
       			if(fail && typeof(fail) == 'function'){
 					fail();
       			}
       		}
       	};
	};
}