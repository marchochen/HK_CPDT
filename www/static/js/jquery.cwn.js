(function($) {

	var Editer = function(element, options) {
		this.type = this.options = this.enabled = this.timeout = this.hoverState = this.$element = null
		this.init('editer', element, options)
	}

	Editer.VERSION = '1.0.0'

	Editer.DEFAULTS = {
		title : '',
		delay : 0,
		html : true,
		container : true,
		fileRegion : 'fileRegion',
		uploadBtns : [    
		               {id:'uploadImg', type : 'image', name:'<i class="fa fa-file-image-o"></i>image'},
		               {id:'uploadDoc', type : 'document', name:'<i class="fa fa-file-word-o"></i>document'},
		               {id:'uploadVideo', type : 'video', name:'<i class="fa fa-file-video-o"></i>video'}
		              ]
	}

	Editer.prototype.init = function(type, element, options) {
		this.enabled = true
		this.type = type
		this.$element = $(element)
		this.options = this.getOptions(options)
		
		this.draw();
		this.eachBtns();
		
		if(this.options.fileInitUrl && this.options.fileInitUrl != '')
			this.initFiles();
	}

	Editer.prototype.getOptions = function(options) {
		options = $.extend({}, this.getDefaults(), this.$element.data(),
				options)
		return options;
	}
	
	Editer.prototype.getDefaults = function () {
		return Editer.DEFAULTS
	}
	
	//编辑器基本样式
	Editer.prototype.draw = function(){
		var $e = this.$element;
		var $options = this.options;
		var fileProgressAreaId = $e.attr("id") + $options.fileRegion;
		var editHtml =   
		'<div class="header-overlay-pc" id="header-overlay-pc'+$e.attr("id")+'" style="display: none;"></div>' + 
		 '<div class="wzb-send-text">'
		    +'<textarea class="wzb-textarea-02 cwn-editer-textarea" style="width:100%" placeholder="'+cwn.getLabel("validate_max_400")+'"></textarea>'
		+'</div>';
		
		if($options.promptInformation != undefined){
			editHtml = editHtml + '<span class="color-gray666">' + $options.promptInformation + '</span>';
		}
		
		editHtml = editHtml + '<div class="margin-top10 clearfix"> '
		        +'<div class="pull-left upload_btn">'
		        	+'<span class="color-gray666">' + $options.btnTitle + '：</span>'
		        	+'<div class=""></div>'
		        +'</div>'
		        +'<div class="pull-right">'
		        	+'<button class="btn wzb-btn-yellow wzb-btn-submit" type="button" >' + cwn.getLabel("button_submit") +'</button>'
		        	+'<button class="btn wzb-btn-yellow wzb-btn-cancel" style="margin-left:5px;" type="button">'+cwn.getLabel("button_cancel") +'</button>'
		        +'</div>      '
		+'</div>'
		+'<div class="wzb-uploader" id="'+ fileProgressAreaId +'" >'+'</div>';
		
		if($options.onlyImg != undefined && $options.onlyImg == true){
			editHtml = '<div class="header-overlay-pc" id="header-overlay-pc'+$e.attr("id")+'" style="display: none;"></div>' + 
				'<div class="margin-top10 clearfix"> '
					        +'<div class="pull-left upload_btn">'
					        	+'<span class="color-gray666">' + $options.btnTitle + '：</span>'
					        	+'<div class=""></div>'
					        +'</div>'
						+'</div>'
						+'<div class="wzb-uploader" id="'+ fileProgressAreaId +'" >'+'</div>'
		}
		
		$e.html(editHtml);
	}
	
	//初始化文件
	Editer.prototype.initFiles = function(){
		var $this = this;
		var $e = this.$element;
		var $options = this.options;
		var $upload = this.upload;
		var fileProgressAreaId = $e.attr("id") + $options.fileRegion
		$e.find(".wzb-uploader").empty();
		$.getJSON($options.fileInitUrl ,function(result){
			var attachments = result.attachments;
			if(attachments != undefined){
	            $.each(attachments,function(i,val){
	            	$this.addFile(val, fileProgressAreaId);
		        })
			}
			$("#"+$e.attr("id")).find(".wzb-uploader-prewview-remove").trigger("click"); //进来或刷新先删除掉以前上传的照片
		});
	}
	
	//遍历出来按钮
	Editer.prototype.eachBtns = function(){
		var self = this;
		var $e = this.$element;
		var $options = this.options;
		var $this = this;
		var fileProgressAreaId = $e.attr("id") + $options.fileRegion;
		$.each($options.uploadBtns, function(i, btn) {
			var obj = $("<a/>", {id : btn.id, 'class' : 'margin-right15 cwn-uploadify wzb-link03', 'data-hastip' : 'uploadImgBoxQitp' , 'aria-describedby': "qtip-uploadImgBoxQitp", "href":"javascript:;","onclick":"$('#header-overlay-pc"+$e.attr("id")+"').show();"})
			$(obj).html(btn.name);
			
			$e.find(".upload_btn ").append(obj);
			$e.find(".cwn-editer-textarea").prompt();
			
			btn.popup ? $(obj).on('click', $this.popup(btn, self, fileProgressAreaId) ) :
			$(obj).on('click', $this.upload(btn, self, fileProgressAreaId) );
		})
	}
	
	//qitp 框出来，给input上传事件
	Editer.prototype.popup = function(btn, editer, fileProgressAreaId){
		var $e = this.$element;
		$('#header-overlay-pc'+$e.attr("id")).click(function(){
			$("#qtip-"+btn.id+"BoxQitp").hide();
			$("#qtip-uploadVideoBoxQitp").hide();
			$("#qtip-uploadDocBoxQitp").hide();
			$('#header-overlay-pc'+$e.attr("id")).hide();
		})
		
		var upload_tip = fetchLabel("upload_tip_image");
		var upload_tip_online = fetchLabel("lab_group_online_addr");
		var lab_group_upload = fetchLabel("online_img");
		var listyle = "";
		lab_group_online = fetchLabel("upload_img");
		if(btn.type==='image'){
			upload_tip = fetchLabel("upload_tip_image");
			lab_group_upload = fetchLabel("upload_img");
			lab_group_online = fetchLabel("online_img");
		}else if(btn.type==='video'){
			upload_tip = fetchLabel("upload_tip_vedio");
			lab_group_upload  = fetchLabel("upload_video");
			lab_group_online = fetchLabel("online_video");
		}else if(btn.type="document"){
			upload_tip = fetchLabel("upload_tip_doc");
			lab_group_upload = fetchLabel("upload_doc");
			listyle = "display:none;";
		}
	var boxQtipHtml = 
		' <div>'
		+'<ul class="nav nav-tabs">'
		+' <li role="presentation" class="active"><a href="javascript:;">'+lab_group_upload+'</a></li>'
		+' <li role="presentation" style="'+listyle+'"><a href="javascript:;">'+lab_group_online+'</a></li>'
		+'</ul>'
		+'</div>'
		+'<div class="nav-contents">'
		+'  <div class="line" style="border:1px solid;"></div>'
		+'	<div class="nav-content" style="padding: 10px 0 10px 0;" >'
		+'		<p class="mb10 color-gray666">'+upload_tip+'</p>'
		+'		<input type="file" name="file" id="' + btn.id + '_input" class="fl"/>'
		+'	</div>'
		+'	<div class="nav-content" style="padding: 10px 0 10px 0;display: none;" >'
		+'		<p class="mb10 color-gray666">'+upload_tip_online+'</p>'
		+'		URL：<input type="text" class="wzb-inputText" name="url" size="26" class="fl"/>'
		+'		<input type="button" class="btn wzb-btn-yellow btn-xs" value="'+fetchLabel("button_submit")+'" style="height: auto;padding: 3px 8px;margin-bottom:5px;"/>'
		+'	</div>'
		+'</div>';
	
		$("#" + btn.id).qtip({
		    id : btn.id + 'BoxQitp',
		    content: {
		        text: boxQtipHtml
		    },
		    position: {
		        at: 'center left',
		        my: 'left center',
		        adjust: {
		            x: 40
		        }
		    },
		    show: {  
		        event: 'click'
		    },
		    hide: '',
		    style: {
		        classes: 'qtip-bootstrap',
		        width: 353
		    },
		    events: {
		        show: function(event, api) {
		        	//tab选中
		        	$(event.target).find(".nav-tabs li").on('click', function(){
		        		if(!$(this).hasClass("active")){
		        			$(this).addClass("active").siblings().removeClass("active");
		        			$(event.target).find(".nav-contents").find(".nav-content").eq($(this).index()).show().siblings(".nav-content").hide();
		        		}
		        	});
		        	$(event.target).unbind('click');
		        	$(event.target).on('click', '.nav-content input[type="button"]', function(){
		        		var url = $(this).prev("input").val();
		        		if(url == undefined || url == ''){
		        			Dialog.alert(fetchLabel("upload_tips_online"));
		        			$(event.target).hide();
		        			$('#header-overlay-pc'+$e.attr("id")).hide();
		        			return;
		        		}
		        		$.ajax({
		        			url : btn.onlineUrl,
		        			data : {
		        				url : url
		        			},
		        			success : function(data){
		        				data = eval('(' + data + ')');
		        				data = jQuery.parseJSON( data );
		        				if (data && data.status === 'success') {
		        					if (data.attachment) {
		        						editer.addFile(data.attachment, fileProgressAreaId);
		        						$(event.target).hide();
		        						$('#header-overlay-pc'+$e.attr("id")).hide();
		        					}
		        				}
		        			}
		        		})
		        	})
		        	//input 点击上传
		        	editer.upload(btn, editer, fileProgressAreaId, $("#" + btn.id + "_input"))
		        }
		    }
		});
		
	}
	
	//给对象上传事件
	Editer.prototype.upload = function(btn, editer, fileProgressAreaId, obj){
		var $e = this.$element;
		var this_ = obj ? obj : ("#" + btn.id);
		var options = editer.options;
		var url = btn.uploadUrl;
		var buttonClass = "cwn-uploadify-button";
		var buttonText = ""
		if(btn.popup) {
			buttonClass = "";
			buttonText = "选择文件";
		}
		$(this_).uploadify({
			'uploader' : url = url.substring(0, url.indexOf("?")) + ';jsessionid=' + options.sessionid + url.substring(url.indexOf("?"), url.length),
			'swf' : '/static/js/jquery.uploadify/uploadify.swf?var=' + (new Date()).getTime(),
			'cancelImg' : '/static/js/jquery.uploadify/uploadify-cancel.png',
			'buttonText' : buttonText,
			'fileObjName' : 'file',
			'buttonClass' : buttonClass,
			'multi' : false,
			'auto' : true,
			'queueID' : fileProgressAreaId,
			'fileTypeExts' : editer.getFileType(btn, options),
			'formData' : {
				'jsessionid' : options.sessionid ,
				'uploadType':'ajax'
			},
			'onUploadStart':function(file){
				$("#header-overlay-pc"+$e.attr("id")).unbind("click");
	            if(file.size > 200*1024*1024){ //定义允许文件的大小为50M
	            	var upload_tip = "upload_tip_vedio";
	            	if(btn.type === 'image'){
	            		upload_tip = "upload_tip_image";
	            	}else if(btn.type === 'video'){
	            		upload_tip = "upload_tip_vedio";
	            	}else if(btn.type === 'document'){
	            		upload_tip = "upload_tip_doc";
	            	}
	            	Dialog.alert(fetchLabel(upload_tip));
		            $(this_).uploadify('stop');
		            $(this_).uploadify('cancel', '*');
	            }
			},
			'onUploadComplete' : function(file) {
				if(btn.type === 'image')
					$("#qtip-"+btn.id+"BoxQitp").hide();
				if(btn.type === 'video')
					$("#qtip-uploadVideoBoxQitp").hide();
				if(btn.type === 'document')
					$("#qtip-uploadDocBoxQitp").hide();
				
				$('#header-overlay-pc'+$e.attr("id")).hide();
			},
			'onUploadSuccess' : function(file, data, response) {
	        	data = eval('(' + data + ')');
				if(data.status == 'fail'){
					Dialog.alert(data.errorMsg);
				}else{
					data = jQuery.parseJSON( data );
					if (data && data.status === 'success') {
						if (data.attachment) {
							editer.addFile(data.attachment, fileProgressAreaId);
						}
					}
					$('#header-overlay-pc'+$e.attr("id")).click(function(){
						$("#qtip-"+btn.id+"BoxQitp").hide();
						$("#qtip-uploadVideoBoxQitp").hide();
						$("#qtip-uploadDocBoxQitp").hide();
	  			    	$('#header-overlay-pc'+$e.attr("id")).hide();
	  			    })
				}
			}
		});
		if(!btn.popup) {
			$(this_).attr("style","").find("div").attr("style","").html(btn.name);
			$(this_).find("object").css("width", $("#" + btn.id).width());
		}
	}
	
	//获取允许文件类型
	Editer.prototype.getFileType = function(obj, options) {
		var fileTypeExts = '';
		if (obj.type) {
//			fileTypeExts = obj.fileTypeExt;
//		} else {
			if (obj.type === 'image') {
				fileTypeExts = '*.gif; *.jpg; *.png; *.jpeg';
			} else if (obj.type === 'document') {
				fileTypeExts = '*.doc; *.docx; *.xls; *.xlsx; *.ppt; *.pdt; *.pdf';
			} else if (obj.type === 'video') {
				fileTypeExts = '*.mp4';
			} else {
				fileTypeExts = '*.doc; *.docx; *.xls; *.xlsx; *.ppt; *.pdt; *.pdf; *.gif; *.jpg; *.png; *.jpeg; *.mp4';
			}
		}
		return fileTypeExts;
	}
	
	//给dom加上文件显示
	Editer.prototype.addFile = function(data, fileProgressAreaId) {
		var $e = this.$element;
		var $options = this.options;
		var fileProgressArea = $("#" + fileProgressAreaId);
		//处理url，在火狐不能正常显示
		data.url = data.url.replace(/\\/g,"/");
		var tpl = '';
		if (data.type === 'Img') {
			tpl += '<div class="wzb-uploader-prewview wzb-uploader-prewview-image">';
			tpl += '<img src="' + data.url + '"/>';
			tpl += '<input type="hidden" name="' + data.file_name + '" value="' + data.id + '"/>';
			tpl += '<a class="wzb-uploader-prewview-remove"><i class="glyphicon glyphicon-remove"></i></a>';
			tpl += '</div>';
		} else {
			tpl += '<div class="wzb-uploader-prewview wzb-uploader-prewview-file">';
			tpl += '<a href="' + data.url + '">' + data.file_name + '</a>';
			tpl += '<input type="hidden" name="' + data.file_name + '" value="' + data.id + '"/>';
			tpl += '<a class="wzb-uploader-prewview-remove"><i class="glyphicon glyphicon-remove"></i></a>';
			tpl += '</div>';
		}

		var e = $(tpl);
		e.find('a.wzb-uploader-prewview-remove').hide();
		e.hover(function() {
			e.find('a.wzb-uploader-prewview-remove').show();
		}, function() {
			e.find('a.wzb-uploader-prewview-remove').hide();
		});
		$('a.wzb-uploader-prewview-remove', e).click(function() {
			$(this).parent().remove();
			var id = $(this).prev("input").val();
			if(id && id != '') {
	 			$.ajax({
					url : $options.fileDelUrl + id ,
					success : function(result){
						//$(obj).remove();
					}
				})
			}
		});
		
		$(fileProgressArea).append(e);
		
	}
	
	function Plugin(option) {
		return this.each(function() {
			var $this = $(this)
			var options = typeof option == 'object' && option

			if (option == 'destroy')
				return;
			if (typeof option == 'string')
				data[option]()
			new Editer(this, options);
		})
	}

	$.fn.cwnEditer = Plugin;
	$.fn.cwnEditer.Constructor = Editer;

})(jQuery);











+(function($) {

	var Doing = function(element, options) {
		this.datatable;
		this.type = this.options = this.enabled = this.timeout = this.hoverState = this.$element = null
		this.init('doing', element, options)
	}

	Doing.VERSION = '1.0.0'

	Doing.DEFAULTS = {
		title : '',
		delay : 0,
		html : true,
		container : true,

	}

	Doing.prototype.init = function(type, element, options) {
		this.enabled = true
		this.type = type
		this.$element = $(element)
		this.options = this.getOptions(options)
		
		this.draw();
		this.onEditer();
		this.onEvent();
	}

	Doing.prototype.getOptions = function(options) {
		options = $.extend({}, this.getDefaults(), this.$element.data(),
				options)
		return options;
	}
	
	Doing.prototype.getDefaults = function () {
		return Doing.DEFAULTS
	}
	
	Doing.prototype.draw = function(){
		var $e = this.$element;
		var $options = this.options;
		this.loadComments();
	}
	

	Doing.prototype.loadComments = function(){
		var $options = this.options;
		var $this = this;
		var $e = this.$element;
		var $params = $options.params;
		$this.datatable = $this.$element.table({
			url : contextPath + this.options.commentUrl,
			colModel : [{
				format : function(data) {
					$.extend(data, {
						isMeInd : (meId == data.s_doi_uid),
						isNormal : isNormal,
						showComment:showComment,
						s_doi_create_datetime : data.s_doi_create_datetime.substring(0,16),
						s_doi_title : data.s_doi_title.replace(/"/g,"")
					})
					for ( var i in data.replies) {
						var replie = data.replies[i];
						$.extend(replie,{isNormal : isNormal});
					}
					return $('#doingTemplate').render(data);
				}
			}],
			rowCallback : function(data){
				$this.loadFiles(data);
				$e.find(".wzb-sns-like:last").like({
					count : data.snsCount?data.snsCount.s_cnt_like_count:0,
					flag : data.is_user_like,
					id : data.s_doi_id,
					module: $params.module,
					tkhId : 0
				});
				for( var i in data.replies) {
					var replie = data.replies[i];
					$("#art_like_count_" + replie.s_cmt_id).like({
						count : replie.snsCount ? replie.snsCount.s_cnt_like_count:0,
						flag : replie.is_user_like,
						id : replie.s_cmt_id,
						module: 'Group',
						isComment : 1,
						tkhId : 0
					});
				}
			},
			rp : 10,
			hideHeader : true,
			usepager : true,
			onSuccess : function(){
				if($options.commentDisplay) {
					if($options.displayNum && $options.displayNum > 0) {
						var index = ($options.displayNum - 1);
						$e.find(".wzb-trend-parcel:lt(" + index + ") .wzb-reply").show();
						$e.find(".wzb-trend-parcel:gt(" + index + ") .wzb-reply").hide();
					} else {
						$e.find(".wzb-trend-parcel .wzb-reply").show();
					}
					
					if($options.commentAble == 'false')
					{
						
					}
					
					
				} else {
					$e.find(".wzb-trend-parcel .wzb-reply").hide();
				}
			}
		})

	}
	
	Doing.prototype.onEditer = function(){
		var $this = this;
		var $options = $this.options;
		var $params = $options.params;
		var editer = $("#" + $options.editerId);
		editer.find(".wzb-btn-submit").on('click', function(){
			var textarea = editer.find(".cwn-editer-textarea");
			var note = $(textarea).val();
			if(note == $(textarea).attr('prompt') || note == '' || note == undefined){
				note = '';
				Dialog.alert(fetchLabel("detail_comment_not_empty")); //'评论不能为空！'
				return;
			} else {
				if(getChars(note) > 400 ){
					Dialog.alert(fetchLabel("validate_max_400")); //'不能超过400个字符'
					return;
				}
			}
			sns.doing.add(note, $params.action, $params.module, $params.id, function(result){
		 		if(result.status=='success'){
		 			var url = contextPath + '/app/doing/user/json/' + $params.userId  + '/' + $params.module + '/' + $params.id;
		 			$($this.datatable).reloadTable({
						url : url
					}); //成功刷新
		 			$(".wzb-uploader").empty();
		 			$(textarea).val('');
		 		}
			});
		})
		//取消
		editer.find(".wzb-btn-cancel").on('click', function(){
			var textarea = $(".cwn-editer-textarea").val();
			if(textarea == null || textarea == '' || textarea == undefined){
				return;
			}
			var module = "Group";
			var text = fetchLabel('alert_clear_text');//"确认清空吗？";
			Dialog.confirm({text:text, callback: function (answer) {
				if(answer){
		 			$.ajax({
						url : contextPath + '/app/upload/delete/'+ module,
						dataType : 'json',
						type : 'post',
						async : false,
						success : function(result){
							//$(obj).remove();
							if(result.status == 'success'){
								$("#editerfileRegion .wzb-uploader-prewview-image").empty();
								$("#editerfileRegion .wzb-uploader-prewview-file").empty();
								$(".cwn-editer-textarea").val('');
							}
						}
					})
				}
				}
			});
		})
		
	}
	
	var ifname = true;
	//事件委派
	Doing.prototype.onEvent = function(){
		var $e = this.$element;
		var $this = this;
		var $options = $this.options;
		var $params = $options.params;
		
		//删除评论
		$e.on('click', ".wzb-reply-content i.fa-times", function(){
			var reply = $(this);
			var parcel = $(this).closest(".wzb-trend-parcel");
			var topParentId = $(this).closest(".wzb-trend-parcel").parent().attr('did');
			sns.comment.del($(this).attr("data"), topParentId,function(result){
				if(result && result.status == 'success'){
					//$(reply).parents(".wzb-reply").remove();
					$(parcel).empty().html($("#replyTemplate").render(result.replies));
					
					if($(parcel).prev(".wzb-trend-content").find("p:eq(2) .wzb-sns-comment span").length>0)
					{
						$(parcel).prev(".wzb-trend-content").find("p:eq(2) .wzb-sns-comment span").html(result.replies?result.replies.length:0);
					}
					else
					{
						$(parcel).prev(".wzb-trend-content").find("p:eq(1) .wzb-sns-comment span").html(result.replies?result.replies.length:0);
					}
					
					
					
				}
			});
		});
		//点击评论
		$e.on('click', ".wzb-sns-comment", function(){
			var parcel = $(this).parents(".wzb-trend-content").next(".wzb-trend-parcel");
			var text = "";
			if($(this).attr("uname")){;
				 text = fetchLabel("detail_comment_to") + $(this).attr("uname") + ":"; //"回复"
			} else {
				 text = fetchLabel("doing_publish_comment");
			}
			
			if(text == $(parcel).find("form").find("textarea").val()){
				$(parcel).find("form").remove();
				$(parcel).children().hide();
			} else {
				$(parcel).find("form").remove();
				$(parcel).children().show();
				$(parcel).append($("#replyFormTemplate").render());
				$(parcel).find("textarea").prompt(text).attr("uid", $(this).attr("uid")).attr('did',$(this).attr("did"));;
			}
			
		})
		//点击回复
		$e.on('click',".wzb-reply-content i.fa-comment", function(){
			var parent = $(this).parents(".wzb-trend-parcel");
			$(this).parents(".wzb-trend-parcel").find("form").remove();
			$(this).parents(".wzb-trend-parcel").append($("#replyFormTemplate").render());
			var text = "";
			if($(this).attr("uname")){;
				 text = fetchLabel("detail_comment_to") + $(this).attr("uname") + ":"; //"回复"
			} else {
				 text = fetchLabel("doing_publish_comment");
			}
			$(parent).find("textarea").val(text);
			$(parent).find("textarea").prompt(text).attr("uid", $(this).attr("uid")).attr('did',$(this).attr("did"));;
		})
		//点击回复  -ok
		$e.on('click', "form.wbedit button", function(){
			var button = this;
			var replyToId = $(this).closest(".wzb-trend").attr("did");
			var textarea = $(this).prev();
			var toUserId = $(textarea).attr("uid");
			var note = $(textarea).val();
			var replyId = $(textarea).attr("did");
			if(note == $(textarea).attr('prompt')){
				note = '';
				Dialog.alert(fetchLabel('detail_comment_not_empty')); //'评论不能为空！'
				return;
			} else {
				if(getChars(note) > 400 ){
					Dialog.alert(fetchLabel('validate_max_400')); //'不能超过400个字符'
					return;
				}
			}
			//新增
	 		sns.comment.add(replyToId, replyId?replyId:replyToId, toUserId,  $params.tkhId ? $params.tkhId:0, $params.module, note, function(result){
				if(result.replies){
					var parcel = $(button).parents(".wzb-trend-parcel");
					$(parcel).empty().html($("#replyTemplate").render(result.replies));
					
					//数量增加
					
					if($(parcel).prev(".wzb-trend-content").find("p:eq(1)").length>0)
					{
						$(parcel).prev(".wzb-trend-content").find("p:eq(1) .wzb-sns-comment span").html(result.replies?result.replies.length:0);
					}
					else
					{
						$(parcel).prev(".wzb-trend-content").find("p:eq(2) .wzb-sns-comment span").html(result.replies?result.replies.length:0);
					}
					
					
					
				}
				
			}); 
		});
		
		//删除动态
		
		
		/*$e.on('click', ".wzb-trend-content .wzb-sns-del-doing", function(){
			$('.wzb-trend-content .wzb-sns-del-doing').unbind('click')
			var reply = $(this);
			sns.doing.del($(this).attr("data"), function(result){
				$this.datatable.reloadTable({
					url : contextPath + '/app/doing/user/json/' + $params.userId  + '/'+ $params.module +'/' + $params.id
				});
			});
		});*/
			if(ifname){
				ifname = false;
				$(document.body).on('click','.wzb-trend-content .wzb-sns-del-doing',function(){
						var reply = $(this);
						sns.doing.del($(this).attr("data"), function(result){
							$this.datatable.reloadTable({
								url : contextPath + '/app/doing/user/json/' + $params.userId  + '/'+ $params.module +'/' + $params.id
							});
						});	
				})
			}
		
		
		
		
		
	}
	
	Doing.prototype.loadFiles = function(data){
		var $this = this;
		$.templates({
			fileTemplate : '<p class="mt10 f14"><a href="{{>url}}" target="_blank">{{>name}}</a></p>',
			imgTemplate : '<p class="mt10 f14"><a href="{{>url}}" target="_blank"><img width="60" src="{{>url}}"/></a></p>'
		})
		var fileList = data.fileList;
		if(fileList != undefined){
			$.each(fileList, function(i, val){
				var name = val.mtf_file_name;
				var url = val.mtf_url;
				var fileHtml ;
				val.mtf_file_type == 'url' ? name = val.mtf_url : url = url + "/" + val.mtf_file_rename;
				var p = {
					name : name,
					url : url		
				}
				val.mtf_type == 'Img' ? fileHtml = $.render.imgTemplate(p) : fileHtml = $.render.fileTemplate(p);
				$this.$element.find(".wzb-trend-content:last p:eq(1)").after(fileHtml);
			});
		}
		
	}
	
	function Plugin(option) {
		return this.each(function() {
			var $this = $(this)
			var options = typeof option == 'object' && option

			if (option == 'destroy')
				return;
			if (typeof option == 'string')
				data[option]()
			new Doing(this, options);
		})
	}

	$.fn.cwnDoing = Plugin;
	$.fn.cwnDoing.Constructor = Doing;

})(jQuery);





+(function($) {

	var Commnent = function(element, options) {
		this.datatable;
		this.type = this.options = this.enabled = this.timeout = this.hoverState = this.$element = null
		this.init('commnent', element, options)
	}

	Commnent.VERSION = '1.0.0'

	Commnent.DEFAULTS = {
		title : '',
		delay : 0,
		html : true,
		container : true,

	}

	Commnent.prototype.init = function(type, element, options) {
		this.enabled = true
		this.type = type
		this.$element = $(element)
		this.options = this.getOptions(options)
		
		this.draw();
		this.onEditer();
		this.onEvent();
	}

	Commnent.prototype.getOptions = function(options) {
		options = $.extend({}, this.getDefaults(), this.$element.data(),
				options)
		return options;
	}
	
	Commnent.prototype.getDefaults = function () {
		return Commnent.DEFAULTS
	}
	
	Commnent.prototype.draw = function(){
		var $e = this.$element;
		var $options = this.options;
		this.loadComments();
	}
	

	Commnent.prototype.loadComments = function(){
		var $options = this.options;
		var $this = this;
		var $e = this.$element;
		var $params = $options.params;
		$this.datatable = $this.$element.table({
			url : contextPath + this.options.commentUrl,
			colModel : [{
				format : function(data) {
					$.extend(data, {
						isMeInd : (meId == data.s_cmt_uid),
						isNormal : isNormal,
						s_cmt_create_datetime : data.s_cmt_create_datetime.substring(0,16),
						s_cmt_content : data.s_cmt_content.replace(/"/g,"")
					})
					return $('#commentTemplate').render(data);
				}
			}],
			rowCallback : function(data){
				$this.loadFiles(data);
				$e.find(".wzb-sns-like:last").like({
					count : data.snsCount?data.snsCount.s_cnt_like_count:0,
					flag : data.is_user_like,
					id : data.s_cmt_id,
					module: $params.module,
					tkhId : 0, 
					isComment : $params.isComment
				})
			},
			rp : 10,
			hideHeader : true,
			usepager : true,
			onSuccess : function(data){
				if($options.commentDisplay) {
					if($options.displayNum && $options.displayNum > 0) {
						var index = ($options.displayNum - 1);
						$e.find(".wzb-trend-parcel:lt(" + index + ") .wzb-reply").show();
						$e.find(".wzb-trend-parcel:gt(" + index + ") .wzb-reply").hide();
					} else {
						$e.find(".wzb-trend-parcel .wzb-reply").show();
					}
				} else {
					$e.find(".wzb-trend-parcel .wzb-reply").hide();
				}
				var editer = $("#" + $options.editerId);
				$(editer).next().find(".wzb-comment-count").html(data.params.total);
			}
		})

	}
	
	Commnent.prototype.onEditer = function(){
		var $this = this;
		var $options = $this.options;
		var $params = {tkhId : 0};
		var editer = $("#" + $options.editerId);
		$params = $.extend($params, $options.params);
		editer.find(".wzb-btn-submit").unbind("click");
		editer.find(".wzb-btn-submit").bind("click",function(){
			var textarea = editer.find(".cwn-editer-textarea");
			var note = $(textarea).val();
			if(note == $(textarea).attr('prompt') || note == '' || note == undefined){
				note = '';
				Dialog.alert(fetchLabel('detail_comment_not_empty')); //'评论不能为空！'
				return;
			} else {
				if(getChars(note) > 400 ){
					Dialog.alert(fetchLabel('validate_max_400')); //'不能超过400个字符'
					return;
				}
			}
			sns.comment.add($params.targetId, 0, 0, $params.thkId, $params.module, note, function(result){
				if(result.status=='success') {		
		 			$($this.datatable).reloadTable({
						url : contextPath + '/app/comment/' + $params.module + '/commentPageJson/' + $params.targetId
					}); //成功刷新
		 			$(textarea).val('');
				}
			});
		});
		
	}
	//事件委派
	Commnent.prototype.onEvent = function(){
		var $e = this.$element;
		var $this = this;
		var $options = $this.options;
		var $params = $options.params;
		
		//删除评论
		$e.on('click', ".wzb-reply-content i.fa-times", function(){
			var reply = $(this);
			var topParentId = $(this).closest(".wzb-trend-parcel").parent().attr('did');
			var parcel = $(this).closest(".wzb-trend-parcel");
			sns.comment.del($(this).attr("data"), topParentId,function(result){
				if(result && result.status == 'success'){
					//$(reply).parents(".wzb-reply").remove();
					$(parcel).empty().html($("#replyTemplate").render(result.replies));
					$(parcel).prev(".wzb-trend-content").find(".wzb-sns-comment span").html(result.replies?result.replies.length:0);
					
					

				}
			});
		});
		
		//删除一级评论
		$e.on('click', ".wzb-trend-content a.delete", function(){
			var reply = $(this);
			var topParentId = $(this).attr("data");
			var parcel = $(this).closest(".wzb-trend-parcel");
			sns.comment.del($(this).attr("data"), topParentId,function(result){
				if(result && result.status == 'success'){
					//$(parcel).empty().html($("#replyTemplate").render(result.replies));
					$($this.datatable).reloadTable({
						url : contextPath + '/app/comment/' + $params.module + '/commentPageJson/' + $params.targetId
					}); //成功刷新
				}
			});
		});
		
		//点击评论
		$e.on('click', ".wzb-sns-comment", function(){
			var parcel = $(this).parents(".wzb-trend-content").next(".wzb-trend-parcel");
			var text = "";
			if($(this).attr("uname")){;
				 text = fetchLabel("detail_comment_to") + $(this).attr("uname") + ":"; //"回复"
			} else {
				 text = fetchLabel("doing_publish_comment");
			}
			
			if(text == $(parcel).find("form").find("textarea").val()){
				$(parcel).find("form").remove();
				$(parcel).children().hide();
			} else {
				$(parcel).find("form").remove();
				$(parcel).children().show();
				$(parcel).append($("#replyFormTemplate").render());
				$(parcel).find("textarea").prompt(text).attr("uid", $(this).attr("uid")).attr('did',$(this).attr("did"));;
			}
			
		})
		//点击回复
		$e.on('click',".wzb-reply-content i.fa-comment", function(){
			var parent = $(this).parents(".wzb-trend-parcel");
			$(this).parents(".wzb-trend-parcel").find("form").remove();
			$(this).parents(".wzb-trend-parcel").append($("#replyFormTemplate").render());
			var text = "";
			if($(this).attr("uname")){
				 text = fetchLabel("detail_comment_to") + $(this).attr("uname") + ":"; //"回复"
			} else {
				 text = fetchLabel("doing_publish_comment");
			}
			$(parent).find("textarea").val(text);
			$(parent).find("textarea").prompt(text).attr("uid", $(this).attr("uid")).attr('did',$(this).attr("did"));;
		})
		//点击回复  -ok
		$e.on('click', "form.wbedit button", function(){
			var button = this;
			var replyToId = $(this).closest(".wzb-trend").attr("did");
			var textarea = $(this).prev();
			var toUserId = $(textarea).attr("uid");
			var note = $(textarea).val();
			var replyId = $(textarea).attr("did");
			if(note == $(textarea).attr('prompt')){
				note = '';
				Dialog.alert(fetchLabel('detail_comment_not_empty')); //'评论不能为空！'
				return;
			} else {
				if(getChars(note) > 400 ){
					Dialog.alert(fetchLabel('validate_max_400')); //'不能超过400个字符'
					return;
				}
			}
			//新增
	 		sns.comment.add(replyToId, replyId?replyId:replyToId, toUserId,  $params.tkhId ? $params.tkhId:0, $params.module, note, function(result){
				if(result.replies){
					var parcel = $(button).parents(".wzb-trend-parcel");
					$(parcel).empty().html($("#replyTemplate").render(result.replies));
					
					//数量增加
					$(parcel).prev(".wzb-trend-content").find(".wzb-sns-comment span").html(result.replies?result.replies.length:0);
				}
				
			}); 
		});
		
		//删除动态
		$e.on('click', ".wzb-trend-content .wzb-sns-del-doing", function(){
			var reply = $(this);
			sns.doing.del($(this).attr("data"), function(result){
				$this.datatable.reloadTable({
					url : contextPath + '/app/doing/user/json/' + $params.userId  + '/'+ $params.module +'/' + $params.id
				});
			});
		});
		
	}
	
	Commnent.prototype.loadFiles = function(data){
		var $this = this;
		$.templates({
			fileTemplate : '<p class="mt10 f14"><a href="{{>url}}" target="_blank">{{>name}}</a></p>',
			imgTemplate : '<p class="mt10 f14"><a href="{{>url}}" target="_blank"><img width="60" src="{{>url}}"/></a></p>'
		})
		var fileList = data.fileList;
		if(fileList != undefined){
			$.each(fileList, function(i, val){
				var name = val.mtf_file_name;
				var url = val.mtf_url;
				var fileHtml ;
				val.mtf_file_type == 'url' ? name = val.mtf_url : url = url + "/" + val.mtf_file_rename;
				var p = {
					name : name,
					url : url		
				}
				val.mtf_type == 'Img' ? fileHtml = $.render.imgTemplate(p) : fileHtml = $.render.fileTemplate(p);
				$this.$element.find(".wzb-trend-content:last p:eq(1)").after(fileHtml);
			});
		}
		
	}
	
	function Plugin(option) {
		this.commentOption = option; 
		return this.each(function() {
			var $this = $(this)
			var options = typeof option == 'object' && option

			if (option == 'destroy')
				return;
			if (typeof option == 'string')
				data[option]()
			new Commnent(this, options);
		})
	}

	$.fn.cwnCommnent = Plugin;
	$.fn.cwnCommnent.Constructor = Commnent;
//	$.fn.reloadComment = function(){
//		this.each(function() {// clear
//			$(this).empty();
//		});
//		this.cwnCommnent(this.commentOption);
//	}
})(jQuery);

