(function($) {
	$.fn.like = function(options) {
		$(this).each(function() {
			var likeObj = this;
			likeObj.options = {
				thkId : '0'
			};
			$.extend(likeObj.options, options);
			
			if(this.options.count){
				$(likeObj).find("span").html(this.options.count);
			}
			$(likeObj).find("i").addClass("skin-color");//初始化打开页面时，先变成蓝色
			var str=$(likeObj).html().replace(/<!--[^x00-xff]*-->/g,'');//有些在html中加注释了
			
			if(this.options.flag){
				$(likeObj).attr("data",'true');
				$(likeObj).attr("title",fetchLabel("sns_del_like"));
				$(likeObj).html(str.indexOf(fetchLabel('sns_liked')) != -1 ? str : str.replace(fetchLabel("sns_like"),fetchLabel("sns_liked")));
				$(likeObj).find("i").addClass("swop_color");//如果已经点赞,就先变成灰色
				$(likeObj).find("i").removeClass("skin-color");
			}else{
				$(likeObj).find("i").addClass("skin-color");//如果还没有点赞,就先变成蓝色
				$(likeObj).attr("title",fetchLabel("sns_like"));
				$(likeObj).html(str.replace(fetchLabel("sns_liked"),fetchLabel("sns_like")));
			}
			
			
			var ad_like = true;
			//赞事件
			$(likeObj).die("click");
			$(likeObj).live('click', function(){
				var isLike = $(likeObj).attr("data");
				if(isLike == 'true'){
					sns.praise.cancel(this.options.id, this.options.module,this.options.isComment, function(result){
						$(likeObj).attr("data","false");
						$(likeObj).attr("title",fetchLabel("sns_like"));
						var lcout = Number($(likeObj).find("span").html());
						if(lcout > 0) {
							lcout = lcout - 1;
						}
						$(likeObj).find("span").html(lcout);
						$(likeObj).find("i").addClass("skin-color");
						$(likeObj).find("i").removeClass("swop_color");
						$(likeObj).html($(likeObj).html().replace(/<!--[^x00-xff]*-->/g,'').replace(fetchLabel("sns_liked"),fetchLabel("sns_like")));
					});
				} else {
					if(ad_like){
						ad_like = false;
						sns.praise.add(this.options.id, this.options.module, this.options.tkhId, this.options.isComment, function(result){
							if(result.status == 'exists'){
								Dialog.alert(fetchLabel('sns_notice_already_like'));//"你已经赞过啦"
								return;
							}
							$(likeObj).attr("data","true");
							$(likeObj).attr("title",fetchLabel("sns_del_like"));
							$(likeObj).find("span").html(Number($(likeObj).find("span").html())+1);
							$(likeObj).find("i").removeClass("skin-color");
							$(likeObj).find("i").addClass("swop_color");
							$(likeObj).html($(likeObj).html().replace(/<!--[^x00-xff]*-->/g,'').replace(fetchLabel("sns_like"),fetchLabel("sns_liked")));
							ad_like = true;
					});
				}
				}
			});
			
		});
		return this;
	};
})(jQuery);


(function($) {
	$.fn.collect = function(options) {
		$(this).each(function() {

			var collectObj = this;
			
			if(options.count){
				$(collectObj).find("span").html(options.count);
			}
			
			if(options.flag != undefined){
				$(collectObj).attr("data",'true');
				$(collectObj).attr("title",fetchLabel("sns_del_collect"));
				$(collectObj).html($(collectObj).html().replace(/<!--[^x00-xff]*-->/g,'').replace(fetchLabel("sns_collect"),fetchLabel("sns_collected")));
				$(collectObj).find("i").addClass("swop_color"); //如果已经收藏,就先变成灰色
				$(collectObj).find("i").removeClass("skin-color");
			}else{//如果还没有收藏,就先变成蓝色
				$(collectObj).attr("title",fetchLabel("sns_collect"));
				$(collectObj).find("i").addClass("skin-color");
			}
			var ad_collect = true;
			//收藏
			$(collectObj).die("click");
			$(collectObj).live('click', function(){
				var isCollect = $(collectObj).attr("data");
				if(isCollect == 'true'){
					sns.collect.cancel(options.id, options.module, function(result){
						$(collectObj).attr("data","false");
						$(collectObj).attr("title",fetchLabel("sns_collect"));
						var ct = Number($(collectObj).find("span").html());
						if(ct > 0){
							ct = ct - 1;
						}
						$(collectObj).find("span").html(ct);
						$(collectObj).find("i").removeClass("swop_color");
						$(collectObj).find("i").addClass("skin-color");
						$(collectObj).html($(collectObj).html().replace(/<!--[^x00-xff]*-->/g,'').replace(fetchLabel("sns_collected"),fetchLabel("sns_collect")));
					});
				} else {
					if(ad_collect){
						ad_collect = false;
						sns.collect.add(options.id, options.module, options.tkhId, function(result){
							if(result.status == 'exists'){
								Dialog.alert(fetchLabel('sns_notice_already_collect'));//你已经收藏过啦
								return;
							}
							$(collectObj).attr("data","true");
							$(collectObj).attr("title",fetchLabel("sns_del_collect"));
							$(collectObj).find("span").html(Number($(collectObj).find("span").html())+1);
							$(collectObj).find("i").removeClass("skin-color");
							$(collectObj).find("i").addClass("swop_color");
							$(collectObj).html($(collectObj).html().replace(/<!--[^x00-xff]*-->/g,'').replace(fetchLabel("sns_collect"),fetchLabel("sns_collected")));
							ad_collect = true;
						});
					}
				}
			});
			
		});
		return this;
	};
})(jQuery);


(function($) {
	$.fn.share = function(options) {
		$(this).each(function() {
			var ad_share = true;
			
			var shareObj = this;
			if(options.count){
				$(shareObj).find("span").html(options.count);
			}
			if(options.flag){
				$(shareObj).attr("data",'true');
				$(shareObj).find("i").addClass("swop_color");//如果已经分享,就先变成灰色
				$(shareObj).find("i").removeClass("skin-color");
				$(shareObj).html($(shareObj).html().replace(/<!--[^x00-xff]*-->/g,'').replace(fetchLabel("sns_share"),fetchLabel("sns_shared")));
			}
			
			$.templates({
				shareQtieTemplate : 
					'<div id="shareQtip{{>tipId}}" style="display:none">'
						+'<span class="pop-up-title-2">'+fetchLabel('label_core_knowledge_management_92')+'</span>'
						+'<div>'
							+'<form class="wbedit" method="post" action="#">'
							+'<textarea class="wbpl replypl showhide" id="shareText'+options.id+'" style="width: 320px; margin: 10px 15px; height: 100px;"></textarea>'
							+'<div class="pop-up-btn"><input type="button" class="btn wzb-btn-orange wzb-btn-big" id="shareBtn'+options.id+'" value="'+fetchLabel('btn_submit')+'"/></div>'
							+'</form>'
						+'</div>'+
					'</div>'
			});

			$("body").append($.render.shareQtieTemplate({tipId : options.id}));
			if(ad_share){
				//分享层
				if(!options.flag){
					$(shareObj).qtip({
					    id : 'shareTip',
					    content: {
					        text: $("#shareQtip" + options.id)
					    },
					    position: {
					        at: 'bottom left',
					        my: 'top center',
					        adjust: {
					            x: options.width
					        }
					    },
					    show: 'click',
					    hide: 'unfocus',
					    style: {
					        classes: 'qtip-bootstrap',
					            width: 353, 
					            height: 200,
					    }
					});
				}
				$(".fa-share-alt").parent().click(function(){
					$("#shareText"+options.id).val("");
				});
				//分享提交
			
			
				$("#shareBtn" + options.id).click(function(){
					
					var textarea = $("#shareText" + options.id);
					if($(shareObj).attr("data") == 'true'){
						$('div[role="alert"]:visible').hide();
						Dialog.alert(fetchLabel('sns_notice_already_share')); //你已经分享过啦
						return;
					}
					var note = $(textarea).val();
					if(note == $(textarea).attr('prompt') || note == '' || note == undefined){
						note = '';
						Dialog.alert(fetchLabel('detail_comment_not_empty')); //'评论不能为空！'
						return;
					} else {
						if(getChars(note) > 200 ){
							Dialog.alert(fetchLabel('validate_max_200')); //'不能超过200个字符'
						}
					}
					if(ad_share){
						ad_share =false;
						sns.share.add(options.id, options.module, $('#shareText'+options.id).val(), options.tkhId, function(result){
							var tipId = $('div[role="alert"]:visible').attr('id');					
							$('div[role="alert"]:visible').hide();
							if(result.status == 'exists'){
								Dialog.alert(fetchLabel('sns_notice_already_share')); //你已经分享过啦
								return;
							} else if(result.status == 'success'){
								Dialog.alert(fetchLabel('sns_notice_share_success')); //分享成功
								$(shareObj).find("i").removeClass("skin-color");
								$(shareObj).attr("data","true");
								$(shareObj).attr("title",fetchLabel("sns_shared"));
								$(shareObj).find("span").html(Number($(shareObj).find("span").html())+1);
								$(shareObj).html($(shareObj).html().replace(/<!--[^x00-xff]*-->/g,'').replace(fetchLabel("sns_share"),fetchLabel("sns_shared")));
								$("#"+tipId).remove();
								return;
							}
							ad_share = true;
						});
					}
				});
			}
			
		});
		return this;
	};
})(jQuery);
