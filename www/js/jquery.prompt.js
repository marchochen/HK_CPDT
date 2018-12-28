/* 用于输入框默认显示提示信息，鼠标点击后清空提示信息，鼠标移开后，如果未输入内容，那么重新显示提示信息 */
(function($) {
	$.fn.prompt = function(options) {
		$(this).each(function() {
			var prompt = this;
			prompt.options = {
				text : ''
			};
			
			//判断是字符串直接给对象加上prompt值
			if(Object.prototype.toString.call(options) === "[object String]"){
				if(options) {
					$(this).attr('prompt', options);
				}
			} else {
				prompt.options = $.extend({}, options);
				if(prompt.options && prompt.options.text) {
					$(this).attr('prompt', prompt.options.text);
				}
			}

			if ($.trim($(this).val()) == '') {
				$(this).val($(this).attr('prompt'));
			}
			$(prompt).click(function() {
				if ($.trim($(this).val()) == $(this).attr('prompt')) {
					$(this).val('');	
				}
			});
			$(prompt).blur(function() {
				if ($.trim($(this).val()) == '') {
					$(this).val($(this).attr('prompt'));		
				}
			});
		});
		return this;
	};
})(jQuery);