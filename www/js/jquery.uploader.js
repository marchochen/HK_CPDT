(function($) {
	$.fn.wzbUploader = function(p) {
		var WzbUploader = function(element, options) {
			this.options = options;
			this.$element = $(element);
		};

		// 默认选项
		WzbUploader.defaults = {
			// 类型
			// image -> 图片
			// document -> 文档
			// vedio -> 视频
			// file -> 文件
			type : 'image',
			// 是否可以多选
			// true -> 默认值，多选
			// false -> 单选
			multiple : true,
			// 属性名
			fieldname : false,
			// 文件类型
			fileTypeExts : false,
			sessionid : false,
			message : {
				upload_file : fetchLabel('global_upload_file'),
				upload_image : fetchLabel('global_upload_image'),
				upload_document : fetchLabel('global_upload_document'),
				upload_vedio : fetchLabel('global_upload_vedio'),
				upload_vedio_online : fetchLabel('global_upload_vedio_online')
			}
		};

		WzbUploader.prototype.addFile = function(data) {
			
			var self = this;
			
			var contextPath = (wb_utils_app_base === "/" ? "":wb_utils_app_base);

			var tpl = '';
			if (self.options.type === 'image') {
				tpl += '<div class="wzb-uploader-prewview wzb-uploader-prewview-image">';
				tpl += '<img src="' + contextPath + data.kba_url + '"/>';
				tpl += '<input type="hidden" name="' + self.options.fieldname + '" value="' + data.kba_id + '"/>';
				tpl += '<a class="wzb-uploader-prewview-remove"><i class="glyphicon glyphicon-remove"></i></a>';
				tpl += '</div>';
			} else {
				tpl += '<div class="wzb-uploader-prewview wzb-uploader-prewview-file">';
				if(self.options.target != null && self.options.target != '' && self.options.target != 'undefined') {
					tpl += '<a href="' + contextPath + data.kba_url + '" target="' + self.options.target + '">' + data.kba_filename + '</a>';
				}else {
					tpl += '<a href="' + contextPath + data.kba_url + '">' + data.kba_filename + '</a>';
				}
				
				tpl += '<input type="hidden" name="' + self.options.fieldname + '" value="' + data.kba_id + '"/>';
				tpl += '<a class="wzb-uploader-prewview-remove"><i class="glyphicon glyphicon-remove"></i></a>';
				tpl += '</div>';
			}
			tpl += '<div id="jquery_uploader_message_tab"></div>';
			var e = $(tpl);
			e.find('a.wzb-uploader-prewview-remove').hide();
			e.hover(function() {
				e.find('a.wzb-uploader-prewview-remove').show();
			}, function() {
				e.find('a.wzb-uploader-prewview-remove').hide();
			});
			$('i.glyphicon-remove', e).click(function() {
				$(this).parent().parent().remove();
			});
			if(self.options.multiple){
				self.$files.append(e);
			} else {
				self.$files.html(e);
			}
			
		}

		WzbUploader.prototype.init = function(options) {
			var self = this;

			var fieldname = options.fieldname;

			self.$ul = $('ul', self.$element);
			self.$ul.hide();

			// 默认模版
			var tpl = '';
			var input_name = fieldname + '_input';
			var tab_div = fieldname + '_tab_div';
			var tab_name = fieldname + '_tab_name';
			if (self.options.type === 'file') {
				tpl += '<div class="wzb-uploader-file">';
				tpl += '<div class="wzb-uploader-file-list"></div>';
				tpl += '<div class="clearfix"></div>';
				tpl += '<input type="file" name="' + input_name + '" id="' + input_name + '" />';
				tpl += '</div>';
			} else if (self.options.type === 'image' || self.options.type === 'document') {
				var title = '';
				if (self.options.type === 'document') {
					title = options.message.upload_document;
				} else if (self.options.type === 'image') {
					title = options.message.upload_image;
				}
				tpl += '<div role="tabpanel">';
//				tpl += '	<ul class="nav nav-tabs" role="tablist">';
//				tpl += '		<li role="presentation" class="active">';
//				tpl += '			<a href="#' + tab_div + '" id="' + tab_name + '" role="tab" data-toggle="tab" aria-controls="' + tab_div + '" aria-expanded="true">' + title + '</a>';
//				tpl += '		</li>';
//				tpl += '	</ul>';
				tpl += '	<div class="tab-content">';
				tpl += '		<div class="wzb-uploader-file-list"></div>';
				tpl += '		<div role="tabpanel" class="tab-pane fade active in" id="' + tab_div + '" aria-labelledby="' + tab_name + '-tab">';
				tpl += '			<input type="file" name="' + input_name + '" id="' + input_name + '" />';
				tpl += '			<div class="clearfix"></div>';
				tpl += '		</div>';
				tpl += '	</div>';
				tpl += '</div>';
			} else if (self.options.type === 'vedio') {
				var url_input_name = fieldname + '_url_input';
				var url_tab_div = fieldname + '_url_tab_div';
				var url_tab_name = fieldname + '_url_tab_name';

				tpl += '<div role="tabpanel">';
				tpl += '	<ul class="nav nav-tabs" role="tablist">';
				tpl += '		<li id="wzb-uploader-li-1" role="presentation" class="active">';
				tpl += '			<a href="#' + tab_div + '" id="' + tab_name + '" role="tab" data-toggle="tab" aria-controls="' + tab_div + '" aria-expanded="true">' + options.message.upload_vedio + '</a>';
				tpl += '		</li>';
				tpl += '		<li id="wzb-uploader-li-2" role="presentation">';
				tpl += '			<a href="#' + url_tab_div + '" id="' + url_tab_name + '" role="tab" data-toggle="tab" aria-controls="' + url_tab_div + '" aria-expanded="false">' + options.message.upload_vedio_online + '</a>';
				tpl += '		</li>';
				tpl += '	</ul>';
				tpl += '	<div id="wzb-uploader-content" class="tab-content">';
				tpl += '		<div class="wzb-uploader-file-list"></div>';
				tpl += '		<div role="tabpanel" class="tab-pane fade active in" id="' + tab_div + '" aria-labelledby="' + tab_name + '">';
				tpl += '			<input type="file" name="' + input_name + '" id="' + input_name + '" />  ';
				tpl += '			<span> '+fetchLabel('label_core_knowledge_management_93')+'</span>  ';
				tpl += '		</div>';
				tpl += '		<div role="tabpanel" class="tab-pane fade" id="' + url_tab_div + '" aria-labelledby="' + url_tab_name + '">';
				tpl += '			<span style="margin-top:26px;float:left">' + fetchLabel('label_core_knowledge_management_90') + '：</span><span style="float:left"><input style="margin-top:20px;width:300px" class="form-control" name="' + fieldname + '"/></span>';
				tpl += '		</div>';
				tpl += '	</div>';
				tpl += '</div>';
			}
			
			// 文件内容
			var fileTypeExts = '';
			if (self.options.fileTypeExts) {
				fileTypeExts = self.options.fileTypeExts;
			} else {
				if (self.options.type === 'image') {
					fileTypeExts = '*.gif; *.jpg; *.png; *.jpeg';
				} else if (self.options.type === 'document') {
					fileTypeExts = '*.doc; *.docx; *.xls; *.xlsx; *.ppt; *.pptx; *.pdf';
				} else if (self.options.type === 'vedio') {
					fileTypeExts = '*.mp4';
				} else {
					fileTypeExts = '*.doc; *.docx; *.xls; *.xlsx; *.ppt; *.pptx; *.pdf; *.gif; *.jpg; *.png; *.jpeg; *.mp4';
				}
			}

			self.$element.append(tpl);
			self.$files = $('.wzb-uploader-file-list', self.$element);
			
			var str = '';
			var url = '';
			$('#wzb-uploader-li-2').click(function(){
				str = $('#wzb-uploader-content .wzb-uploader-file-list').html();
				$('#wzb-uploader-content .wzb-uploader-file-list').html('');
				$('#wzb-uploader-content input[name='+fieldname+']').val(url);
			});
			
			$('#wzb-uploader-li-1').click(function(){
				url = $('#wzb-uploader-content input[name='+fieldname+']').val();
				$('#wzb-uploader-content input[name='+fieldname+']').val('');
				var event = $(str);
				event.find('a.wzb-uploader-prewview-remove').hide();
				event.hover(function() {
					event.find('a.wzb-uploader-prewview-remove').show();
				}, function() {
					event.find('a.wzb-uploader-prewview-remove').hide();
				});
				$('i.glyphicon-remove', event).click(function() {
					$(this).parent().parent().remove();
				});
				$('#wzb-uploader-content .wzb-uploader-file-list').html(event);
			});

			$('#' + input_name).uploadify({
				'uploader' : wb_utils_app_base + 'app/kb/attachment/upload;jsessionid=' + self.options.sessionid + '?Func=uploadwallpaper2Dfs',
				'swf' : wb_utils_app_base + 'static/js/jquery.uploadify/uploadify.swf?var=' + (new Date()).getTime(),
				'cancelImg' : wb_utils_app_base + 'static/js/jquery.uploadify/uploadify-cancel.png',
				'buttonText' : fetchLabel('global_select_file'),
				'fileObjName' : 'file',
				'multi' : false,
				'auto' : true,
				'successTimeout'  : 0,
				'fileTypeExts' : fileTypeExts,
				'formData' : {
					'jsessionid' : self.options.sessionid,
					'uploadType' : 'ajax'
				},
				'onUploadProgress' : function(file){
					$('#' + file.id).find('.data').html('90%');
				},
				'onUploadComplete' : function(file) {
				},
				'onUploadSuccess' : function(file, data, response) {
					data = eval('(' + data + ')');
					if(data.status == 'fail'){
						Dialog.alert(data.errorMsg);
					}else if (data && data.status === 'success') {
						if (data.attachment) {
							self.addFile(data.attachment);
						}
					}
				}
			});

			// 初始化文件
			$('li', self.$ul).each(function() {
				var data = {
					kba_filename : $(this).attr('filename'),
					kba_url : $(this).attr('url'),
					kba_id : $(this).attr('id')
				}
				self.addFile(data);
			});
		};

		p = p || {};

		$(this).each(function() {
			var options = $.extend(WzbUploader.defaults, p);
			var wzbUploader = new WzbUploader(this, options);
			wzbUploader.init(options);
		});
		return this;
	};
})(jQuery);