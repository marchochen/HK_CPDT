(function($) {
	$.fn.wzbSelector = function(p) {
		var WzbSelector = function(element, options) {
			this.options = options;
			this.$element = $(element);
		};

		// 默认选项
		WzbSelector.defaults = {
			// 类型
			// multiple -> 默认值，多选
			// single -> 单选
			type : 'multiple',
			// 点击OK是否直接关闭选择框，默认是关闭
			// true -> 关闭
			// false -> 保留
			auto_close : true,
			// 简单模式，直接根据URL通过Ajax获取数据显示到页面供选择
			simple : {
				// 是否启用简单模式
				enable : false,
				// 是否直接显示数据，
				// true -> 显示弹出层时直接加载数据，
				// false -> 显示弹出层时不加载数据，通过过滤框查询加载
				init : false,
				// 是否显示过滤框
				filter : false,
				// 加载数据的URL
				url : false
			},
			// 树模式，通过树供选择
			tree : {
				enable : false,
				setting : false
			},
			// 远程模式，加载页面做自定义查询
			remote : {
				enable : false,
				url : false
			},
			// 文本信息
			message : {
				title : '',
				button_close : fetchLabel('button_close'),
				button_ok : fetchLabel('button_ok'),
				losedata_title : fetchLabel('lab_table_data_null')
			}
		};

		WzbSelector.prototype.addItem = function(id, value) {
			var self = this;

			if (this.options.type === 'single') {
				this.$ui.empty();
				this.$element.empty();
			} else {
				if ($('a.wzb-selector-item[value=' + id + ']', this.$ui).size() > 0) {
					return;
				}
			}

			var li = $('<li class="wzb-selector-li">').append('<a class="wzb-selector-item" value="' + id + '" href="#">' + value + ' <i class="glyphicon glyphicon-remove"></i></a>');
			$('i.glyphicon-remove', li).click(function() {
				$('option[value=' + ($(this).parents('a.wzb-selector-item').attr('value')) + ']', self.$element).remove();
				$(this).parent().parent().remove();
			});
			this.$ui.append(li);

			var opt = $('<option selected value="' + id + '">' + value + '</option>');
			this.$element.append(opt);
		};

		WzbSelector.prototype.removeItem = function() {

		};

		WzbSelector.prototype.openModal = function() {
			
			var self = this;
			
			var bodyStyle = this.options.bodyStyle || "";

			var tpl = '';
			tpl += '<div class="modal fade" id="wzbSelectorModal" tabindex="-1" role="dialog" aria-labelledby="wzbSelectorModalLabel" aria-hidden="true">';
			tpl += '	<div class="modal-dialog">';
			tpl += '		<div class="modal-content">';
			tpl += '			<div class="modal-header">';
			tpl += '				<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>';
			tpl += '				<h4 class="modal-title" id="wzbSelectorModal">'+this.options.message['title']+'</h4>';
			tpl += '			</div>';
			tpl += '			<div class="modal-body" style="'+bodyStyle+'">';
			tpl += '			</div>';
			tpl += '			<div class="modal-footer">';
			tpl += '				 <button type="button" class="modal-btn-ok btn btn-primary">'+fetchLabel('button_ok')+'</button>';
			tpl += '	             <button type="button" class="modal-btn-close btn btn-default" data-dismiss="modal">'+fetchLabel('button_cancel')+'</button>';
			tpl += '			</div>';
			tpl += '		</div>';
			tpl += '	</div>';
			tpl += '</div>';

			$('#wzbSelectorModal').remove();

			$(tpl).appendTo($('body'));

			$('#wzbSelectorModal').on('shown.bs.modal', function() {
				self.initModal(this);
			});

			$('#wzbSelectorModal').modal({
				show : true
			});
		};

		WzbSelector.prototype.initModal = function(modal) {
			var self = this;

			$(modal).find('.modal-title').html(self.options.message['title'])
			$(modal).find('.modal-btn-close').html(fetchLabel('button_cancel'))
			$(modal).find('.modal-btn-ok').html(fetchLabel('button_ok'))
			$(modal).find('.modal-body').empty();

			$(modal).find('.modal-btn-ok').click(function() {
				self.getSelectedItems(modal);
			});

			if (self.options.simple.enable === true) {
				self.initSimpleContent(modal);
			} else if (self.options.remote.enable === true) {
				self.initRemoteContent(modal);
			} else if (self.options.tree.enable === true) {
				self.initTreeContent(modal);
			}
		};

		WzbSelector.prototype.loadSimpleData = function(modal, q) {
			var self = this;
			
			var params = [];
			if (q != undefined) {
				params = [ {
					name : 'q',
					value : q
				} ];
			}

			$.ajax({
				dataType : 'json',
				url : self.options.simple.url,
				data : params
			}).done(function(data) {
				if (data && data.records) {
					$(modal).find('.wzb-selector-simple-items').empty();

					if(data.records.length > 0){
						$(data.records).each(function() {
							var htmTitle = this.title ? this.title : this.TITLE;
							var htmId = this.id ? this.id : this.ID;
							var htm = '<li class="wzb-selector-simple-item" style="margin:4px;" role="presentation">';
							htm += '<label class="checkbox-inline">' + htmTitle;
							htm += '<input class="wzb-selector-simple-checkbox checkbox" type="checkbox" data-title="' + htmTitle + '" value="' + htmId + '"> ';
							htm += '</label>';
							htm += '</li>';
							
							$(modal).find('.wzb-selector-simple-items').append($(htm));
						});
					}else{
						var losedata_title = self.options.message.losedata_title || fetchLabel("lab_table_data_null");
						$(modal).find('.wzb-selector-simple-items').append('<div class="losedata"><i class="fa fa-folder-open-o"></i><p>'+losedata_title+'</p></div>');
					}
				}
			});
		}

		WzbSelector.prototype.initSimpleContent = function(modal) {
			var self = this;

			var tpl = '';
			tpl += '<div class="form-inline text-right" role="form">';
			tpl += '	<div class="form-group">';
			tpl += '		<div class="input-group">';
			tpl += '			<input type="text" class="wzb-selector-simple-q form-control">';
			tpl += '			<div class="input-group-btn">';
			tpl += '				<button type="button" style="height:28px;margin-top:0;" class="wzb-selector-simple-btn btn btn-default" tabindex="-1"><i style="top:-1px" class="glyphicon glyphicon-search"></i></button>';
			tpl += '			</div>';
			tpl += '		</div>';
			tpl += '	</div>';
			tpl += '</div>';
			tpl += '<hr />';
			tpl += '<div class="wzb-selector-simple-container">';
			tpl += '<ul class="wzb-selector-simple-items nav nav-pills"></ul>';
			tpl += '</div>';

			$(modal).find('.modal-body').empty().append($(tpl));

			$('button.wzb-selector-simple-btn', $(modal).find('.modal-body')).click(function() {
				var q = $('input.wzb-selector-simple-q', $(modal)).val();
				self.loadSimpleData(modal, q);
			});
			//按下确认键搜索
			window.document.onkeydown = disableRefresh;
			function disableRefresh(evt){
				evt = (evt) ? evt : window.event
				if (evt.keyCode) {
				   if(evt.keyCode == 13){
					   var q = $('input.wzb-selector-simple-q', $(modal)).val();
					   self.loadSimpleData(modal, q);
				   }
				}
			}

			self.loadSimpleData(modal);
		};

		WzbSelector.prototype.initTreeContent = function(modal) {
			var self = this;

			var tpl = '';
			tpl += '<div class="wzb-selector-tree-container">';
			tpl += '<ul id="wzb-selector-tree" class="ztree"></ul>';
			tpl += '</div>';

			$(modal).find('.modal-body').empty().append($(tpl));

			var data = (self.options.tree.data) ? self.options.tree.data : null;
			var settings = self.options.tree.setting;
			if (self.options.tree.type === 'multiple') {
				settings = $.extend(settings, {
					check : {
						enable : true,
						chkboxType : {
							"Y" : "",
							"N" : ""
						}
					}
				});
			} else if (self.options.tree.type === 'single') {
				settings = $.extend(settings, {
					check : {
						enable : false
					}
				});
			}
			$.fn.zTree.init($("#wzb-selector-tree"), settings, data);
		};

		WzbSelector.prototype.initRemoteContent = function(modal) {
			var self = this;

			var tpl = '<div class="wzb-selector-remote-container"></div>';

			$(modal).find('.modal-body').empty().append($(tpl));

			$('div.wzb-selector-remote-container', $(modal).find('.modal-body')).load(self.options.remote.url);
		};

		WzbSelector.prototype.getSelectedItems = function(modal) {
			var self = this;

			if (self.options.simple.enable === true) {
				if ($(modal).find('.wzb-selector-simple-checkbox:checked').size() > 0) {
					$(modal).find('.wzb-selector-simple-checkbox:checked').each(function() {
						self.addItem($(this).attr('value'), $(this).attr('data-title'));
					})
				}
			} else if (self.options.remote.enable === true) {
				if (typeof self.options.remote.callback == 'function') {
					var items = self.options.remote.callback();
					$(items).each(function() {
						var htmTitle = this.title ? this.title : this.TITLE;
						var htmId = this.id ? this.id : this.ID;
						self.addItem(htmId, htmTitle);
					})
				}
			} else if (self.options.tree.enable === true) {
				var wzbSelectorTree = $.fn.zTree.getZTreeObj("wzb-selector-tree");
				var selectedNodes = wzbSelectorTree.getSelectedNodes();
				if (selectedNodes.length > 0) {
					for (var i = 0; i < selectedNodes.length; i++) {
						
						var node = selectedNodes[i];
						
						if(self.options.ignoreRootNode && node.level == 0){
							continue;
						}
						self.addItem(node.id, node.name);
					}
				}
			}

			if (self.options.auto_close === true) {
				$('#wzbSelectorModal').modal('hide');
			}
		};

		WzbSelector.prototype.init = function() {
			var self = this;

			this.$body = $('<div>').addClass('wzb-selector');
			this.$ui = $('<ul class="wzb-selector-ul">').addClass('clearfix');
			this.$btn = $('<button type="button" class="wzb-selector-btn btn"><i  class=\'glyphicon glyphicon-plus\'></button>');
			this.$btn.click(function() {
				self.openModal();
			});

			this.$body.append(this.$ui).append(this.$btn).insertBefore(this.$element);

			this.$element.hide();

			$('option', this.$element).each(function() {
				self.addItem($(this).attr('value'), $(this).text());
			});
		};

		p = p || {};

		$(this).each(function() {
			var options = $.extend(WzbSelector.defaults, p);
			var wzbSelector = new WzbSelector(this, options);
			wzbSelector.init(options);
		});
		return this;
	};
})(jQuery);