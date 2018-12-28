(function($) {
	$.fn.tabs = function(options) {
		$(this).each(function() {
			var tabs = this;
			tabs.options = $.extend({
				selected : options
			}, options);

			var tabNav = $(tabs).find('ul.wzb-tabs-nav');

			tabs.toggle = function(node) {
				$(tabNav).find('li').each(function(i) {
					$(this).removeClass("active");
					$($('a.wzb-tabs-header-text', this).attr('rel')).hide();
				});

				$(node).addClass("active");
				$($('a.wzb-tabs-header-text', node).attr('rel')).show();
			};

			$(tabNav).find('li').each(function(i) {
				if (tabs.options.selected == i) {
					tabs.toggle(this);
				}
			});

			$(tabNav).find('li').click(function() {
				tabs.toggle(this);
			});
		});
		return this;
	};
})(jQuery);