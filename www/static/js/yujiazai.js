

	
	
	
	
	/*function ifname(name){
			var showname = this.showname//留住当前this指向
			console.log(name.getAttribute("show-msg-code"))
			$.ajax({
				 	type:'get',      
				    url: "/app/help/question/"+name.getAttribute("show-msg-code"),
				    cache:false,  
				    dataType:'json',  
				    success:function(data){
			    	console.log(data)
			    	showname(data,name)//调用ifnamde的showname方法
			    }
			});   
	}
	//弹出层的方法
	ifname.prototype.showname = function(data,name){
				console.log(name)
		 		var indextop = name.getAttribute("show-msg-code");//获取指data的编号
				
				
				
		 		显示内容判断
		 		var language = name.getAttribute('show-msg-language');
		 		var datacontent;
		 		if(language == 'us'){
		 			datacontent = Wzb.htmlDecode(data.hq_content_us)
		 		}else{
		 			datacontent = Wzb.htmlDecode(data.hq_content_cn)
		 		}
		 				
		 				var nameshowWidth = data.hq_template.split(',')[0]==1?100:data.hq_template.split(',')[0];
		 				var nameshowHeight = data.hq_template.split(',')[1]==1?100:data.hq_template.split(',')[1];
		 				
		 				var fangshi = data.hq_template.split(',')[3];
		 				
		 				var typeOpen;//打开方式
		 				
		 				switch(fangshi){
		 					case 'yuyin':
		 						typeOpen = 2;
		 					break
		 				}
		 				
		 				if(typeOpen == 2||fangshi == 'yuyin'){
		 					var shijian = name.childNodes[0].childNodes[0].duration/60
		 				
		 					name.childNodes[0].childNodes[0].play()
		 					layer.tips(shijian.toFixed(2)+'点击播放', name, {
		 						  tips: [1, '#0FA6D8'] //还可配置颜色
		 						});
		 				}else if(typeOpen == 2){
		 					layer.tips(datacontent, name, {
		 						  tips: [1, '#0FA6D8'] //还可配置颜色
		 						});
		 				}else{
		 					layer.open({
			 					type: 1,
			 				  	title: false,
			 				  	closeBtn: 1,
			 				  	type:1,
			 				  	shadeClose: true,
			 				 	skin: 'demo-class', //加上边框
			 			 		area: [nameshowWidth+"px",nameshowHeight+'px'], //宽高
			 			 		content: '<div style="padding:20px 20px 0 20px;width:100%">\
			 			 					'+datacontent+'\
			 			 						<p class="res" style="color:#fff;position: absolute;top:0px">\
			 			 							'+indextop+'\
			 			 						</p>\
			 			 				  </div>'
			 			 		});
		 					
		 				}
		};	
	ifname1 = function(name){//调用new返回函数
		return new ifname(name);
	}
	window.ifname1 = ifname1;
	*/
	
	
	
	
	//展示页面
function helpshow(){
	/*var showname  = this.showname

		var a = document.querySelectorAll('a')
		var name = [];
		for(var i = 0;i < a.length;i++){
			name[i] = a[i]
			if(a[i].getAttribute("show-msg-code")){
				
				$.ajax({
				 	type:'get',      
				    url: "/app/help/question/"+a[i].getAttribute("show-msg-code"),
				    cache:false,  
				    dataType:'json',  
				    success:function(data){
				    	showname(data,name)
				    }
				});
				
				
			}
		}*/
}


helpshow.prototype.show = function(){
		var t = this
		var a = document.querySelectorAll('a')
		var name = [];
		for(var i = 0;i < a.length;i++){
			name[i] = a[i]
			
			if(a[i].getAttribute("show-msg-code")){
				$.ajax({
				 	type:'get',      
				    url: "/app/help/question/"+a[i].getAttribute("show-msg-code"),
				    cache:false,  
				    dataType:'json',  
				    success:function(data){
				    	t.showname(data,name)
				    }
				});
			}
			
		}
		
	
}




helpshow.prototype.showname  = function(indeData,name){
	var t1 = this;
	console.log(indeData.hq_template.split(',')[3])
	console.log(name)
	
	if(indeData.hq_template.split(',')[3] == 'yuyin'){
		for(var i =0;i<name.length;i++){
			if(name[i].getAttribute("show-msg-code")){
				
				var yuyan;
				if(name[i].getAttribute('show-msg-language') == 'cn'){
					yuyan = Wzb.htmlDecode(indeData.hq_content_cn)
				}else{
					yuyan = Wzb.htmlDecode(indeData.hq_content_us)
				}
				name[i].className = 'sound-small'
				name[i].innerHTML = yuyan;
				name[i].childNodes[0].style.display = 'none';
				name[i].onclick = function(){
					this.childNodes[0].childNodes[0].play()
				}
				name[i].onmouseover = function(){
					t1.indexLayer(this)
				}
			}
		}
	}else if(indeData.hq_template.split(',')[3] == 'left'){
		for(var i =0;i<name.length;i++){
			if(name[i].getAttribute("show-msg-code")){
				name[i].className = 'ask-in'
				var yuyan;
				if(name[i].getAttribute('show-msg-language') == 'cn'){
					yuyan = Wzb.htmlDecode(indeData.hq_content_cn)
				}else{
					yuyan = Wzb.htmlDecode(indeData.hq_content_us)
				}
				name[i].onmouseover=function(){}
				name[i].onclick = function(){
					t1.indexLayer(this,yuyan,4)
				}
				
			}
		}
	}else if(indeData.hq_template.split(',')[3] == 'right'){
		for(var i =0;i<name.length;i++){
			if(name[i].getAttribute("show-msg-code")){
				var yuyan;
				if(name[i].getAttribute('show-msg-language') == 'cn'){
					yuyan = Wzb.htmlDecode(indeData.hq_content_cn)
				}else{
					yuyan = Wzb.htmlDecode(indeData.hq_content_us)
				}
				name[i].className = 'ask-in'
				Wzb.htmlDecode(indeData.hq_content_cn);
				name[i].onmouseover=function(){}
				name[i].onclick = function(){
					t1.indexLayer(this,yuyan,2)
				}
				
			}
		}
	}else if(indeData.hq_template.split(',')[3] == 'yellow'){
		for(var i =0;i<name.length;i++){
			if(name[i].getAttribute("show-msg-code")){
				name[i].className = 'yellowtan'
					var yuyan;
				if(name[i].getAttribute('show-msg-language') == 'cn'){
					yuyan = Wzb.htmlDecode(indeData.hq_content_cn)
				}else{
					yuyan = Wzb.htmlDecode(indeData.hq_content_us)
				}
				name[i].onmouseover=function(){}
				name[i].onclick = function(){
					t1.indexLayer(this,yuyan,1)
				}
				
			}
		}
	}else if(indeData.hq_template.split(',')[3] == 'red'){
		for(var i =0;i<name.length;i++){
			if(name[i].getAttribute("show-msg-code")){
				name[i].className = 'redtan'
					name[i].onmouseover=function(){}
				var yuyan;
				if(name[i].getAttribute('show-msg-language') == 'cn'){
					yuyan = Wzb.htmlDecode(indeData.hq_content_cn)
				}else{
					yuyan = Wzb.htmlDecode(indeData.hq_content_us)
				}
				name[i].innerHTML =  yuyan
			}
		}
	}else if(indeData.hq_template.split(',')[3] == 'fankui'){
		for(var i =0;i<name.length;i++){
			if(name[i].getAttribute("show-msg-code")){
				//name[i].className = 'redtan'
					name[i].onmouseover=function(){}
					var yuyan;
					if(name[i].getAttribute('show-msg-language') == 'cn'){
						yuyan = Wzb.htmlDecode(indeData.hq_content_cn)
					}else{
						yuyan = Wzb.htmlDecode(indeData.hq_content_us)
					}
				name[i].innerHTML = yuyan  
			}
		}
	}else if(indeData.hq_template.split(',')[3] == 'butong'){
		for(var i =0;i<name.length;i++){
			if(name[i].getAttribute("show-msg-code")){
				name[i].className = ' '
					name[i].onmouseover=function(){}
					var yuyan;
					if(name[i].getAttribute('show-msg-language') == 'cn'){
						yuyan = Wzb.htmlDecode(indeData.hq_content_cn)
					}else{
						yuyan = Wzb.htmlDecode(indeData.hq_content_us)
					}
				//name[i].innerHTML = '普通电机'
				name[i].onclick = function(){
					layer.open({
	 					type: 1,
	 				  	title: false,
	 				  	closeBtn: 1,
	 				  	type:1,
	 				  	shadeClose: true,
	 				 	skin: 'demo-class', //加上边框
	 			 		area: [indeData.hq_width+"px",indeData.hq_height+'px'], //宽高
	 			 		content: '<div style="padding:20px 20px 0 20px;width:100%">\
	 			 					'+yuyan+'\
	 			 				  </div>'
	 			 		});
				}
			}
		}
	}else if(indeData.hq_template.split(',')[3] == 'gong'){
		for(var i =0;i<name.length;i++){
			if(name[i].getAttribute("show-msg-code")){
				//name[i].className = 'redtan'
					name[i].onmouseover=function(){}
					var yuyan;
					if(name[i].getAttribute('show-msg-language') == 'cn'){
						yuyan = Wzb.htmlDecode(indeData.hq_content_cn)
					}else{
						yuyan = Wzb.htmlDecode(indeData.hq_content_us)
					}
				name[i].innerHTML = yuyan
			}
		}
	}
	
	
	
	
	
	
}


helpshow.prototype.indexLayer = function(name,moban,fangxiang){
	
	if(moban){
			layer.tips(moban, name, {
				  tips: [fangxiang, 'rgba(128,128,128,0.9)'] //还可配置颜色
			});
		
		
		
	}else{
		var  shijian = name.childNodes[0].childNodes[0].duration/60
		layer.tips(shijian.toFixed(2)+'点击播放', name, {
			  tips: [1, 'rgba(128,128,128,0.9)'] //还可配置颜色
		});
	}
	
	
	
	
}




	
	
	






	 
	 
	 	
