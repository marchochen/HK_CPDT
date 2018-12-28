(function($) {
	$.fn.datepicker = function(options, params, not_merge_params) {
		$(this).each(function() {
			$(this).addClass('Wdate');

			$(this).focus(function() {
				$.extend(options, {
					lang : wb_lang,
					el : $(this).attr('id')
				});
				laydate(options);
			})
		});
	}
})(jQuery);