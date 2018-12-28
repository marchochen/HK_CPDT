Dialog = {
	alert : function(txt,callback) {
		var alert = this;
 		var message = $('');
		var ok = $('<button/>', {
			text : fetchLabel('button_ok'),
			id : 'true',
			'class' : 'btn wzb-btn-blue margin-right10 wzb-btn-big'
		});
 		var btnBar = $("<div class='wzb-bar'/>");
 		btnBar.html(ok);
		var textStr = (txt + "").split("~");
		for(var i = 0 ; i< textStr.length; i++) {
			var pt = $('<div />',{
				text : textStr[i],
				'class' : 'pop-up-word'
			});
			message = message.add(pt);
		}
		
		var titleHtmlStr = fetchLabel('warning_notice')+"<i class='pop-up-sanjiao'></i>";
		
		alert.options = {
			title : titleHtmlStr,
			buttons : message.add(btnBar)
		};
		
		if(callback){
			alert.options.callback = callback;
		}
		
		Dialog.dialogue(alert.options);
	},
	confirm : function(options) {
		var confirm = this;
		var message = $('<div />', {
			text : options.text ? options.text : "",
			'class' : 'pop-up-word'
		});
		var ok = $('<button/>', {
			text : fetchLabel('button_ok'),
			id : true,
			'class' : 'btn wzb-btn-blue margin-right10 wzb-btn-big true'
		});
		var cancel = $('<button/>', {
			text : fetchLabel('button_cancel'),
			id : false,
			'class' : 'btn wzb-btn-blue margin-right10 wzb-btn-big false'
		});
		var btnBar = $("<div class='wzb-bar'/>");
		
		btnBar.append(ok).append(cancel);
		
		confirm.options = {
			callback : false,
			title : fetchLabel('warning_notice'),
			buttons : message.add(btnBar)
		};
		$.extend(confirm.options, options);
		Dialog.dialogue(confirm.options);
	},
	dialogue : function(options) {
		dialog = this;
		dialog.options = {
			content : '',	
			title : '',
		};
		$.extend(dialog.options, options);
		var content = $('<div/>');
		content.html(options.buttons);
		$.extend(dialog.options,{content:content});
		$('#qtip-noticeTip').qtip('destroy');
		$('#qtip-shareTip').hide();
	    $("<div/>").qtip({
	    	id : 'noticeTip',
	        content: {
	            text: dialog.options.content,
	            title: dialog.options.title
	        },
	        position: {
	            my: 'center',
	            at: 'center',
	            target: $(window)
	        },
	        show: {
	            ready: true,
	            modal: {
	                on: true,
	                blur: true
	            }
	        },
	        hide: false,
	        style: {
	            classes: 'wzb-qtip',
	            width: {
	            	min : 100
	            }
	        },
	        events: {
	            render: function(event, api) {
	                $('button', api.elements.content).click(function(e) {
		            	  if(options.callback){
		          			  var flag = ($(this).hasClass("true")) ? true : false;
		        			  options.callback(flag);
		        		  }
		                  api.hide(e);
	                });
	            },
	            hide: function(event, api) { api.destroy(); }
	        }
	    });
	},
	//显示操作信息反馈框  
	//@param url 点击确定按钮跳转的链接 
	//@param type 提示类型（optional） ， success：操作成功（默认，可不传），error，操作失败
	showSuccessMessage : function(url,type){
		
		var message = fetchLabel("global_operate_success");
		var styleClass = "";
		if("error" === type){
			message = fetchLabel("global_operate_error");
			styleClass = "losepage-3";
		}
		
		var tip = message + fetchLabel("lab_operate_forward_message");
		
		var btnLabel = fetchLabel("button_ok");
		
		var html = 
			"<div class='mode'>" +
	            "<div class='losepage-2 "+styleClass+" sessionTimeOut' style='margin:100px auto 100px auto'>" +
	                 "<div class='losepage_tit'>"+message+"</div>" +
	                 "<div class='losepage_info'>" + tip +
	                 "<div class='losepage_desc margin-top10'><input id='sureBtn' type='button' class='btn wzb-btn-orange'  value='"+btnLabel+"'></div>" +
	            "</div>" +
	       "</div>";
		
		layer.open({
			  type: 1,
			  area: ['600px', '350px'],
			  title : false,
			  closeBtn: 0,
			  content: html
		});
		
		var go = function(){
			if(timer != null){
				clearInterval(timer);
				timer = null;
			}
			window.location.href = url;
		}
		
		//倒计时操作
		var maxtime = 3; //单位：秒
		var timer = setInterval(function(){
			if (maxtime > 0) {
		    	document.getElementById("second").innerHTML = maxtime;
		        --maxtime;
		    }
		    else {
		        go();
		    }
		}, 1000);
		
		$("#sureBtn").unbind("click");
		$("#sureBtn").bind("click",function(){
			go();
		});
		
	}
};
