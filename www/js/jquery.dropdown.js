(function($) {
	$.fn.dropdown = function(options) {
		$(this).each(function() {
			options = $.extend({
				width : 150,
				top: 0,
				change : false,
				target : false
			}, options);

			var dropdown = this;
			this.options = options;
			this.list = $('#' + this.options.target);
			this.icon = $('img.wzb-dropdown-icon', this);
			this.value = null;

			this.show = function() {
				$('.wzb-dropdown').unload();

				$(this.icon).removeClass("wzb-dropdown-icon-down");
				$(this.icon).addClass("wzb-dropdown-icon-up");
				$(this).addClass("wzb-dropdown-pressed");

				var l = $(this).position().left;
				var t = $(this).position().top + $(this).outerHeight() + 3;

				this.list.css({
					zIndex : 9999,
					left : l,
					top : t + this.options.top + 'px',
					width : this.options.width + 'px'
				});

				this.list.animate({
					height : 'show',
					opacity : 'show'
				}, 300);

				$('html').one('click', function(event) {
					dropdown.unload();
				});
			};

			this.unload = function() {
				$(this.icon).removeClass("wzb-dropdown-icon-up");
				$(this.icon).addClass("wzb-dropdown-icon-down");
				$(this).removeClass("wzb-dropdown-pressed");

				this.list.animate({
					height : 'hide',
					opacity : 'hide'
				}, 300);
			};

			this.select = function() {
				var nv = $(this).attr('data-value');
				var is_change = dropdown.value !== nv;

				$('.wzb-dropdown-caption span', dropdown).html($(this).html());
				$('.wzb-dropdown-caption input:hidden', dropdown).val(dropdown.value);

				dropdown.value = nv;
				if (is_change && dropdown.options.change) {
					dropdown.options.change(nv, dropdown);
				}
				dropdown.unload();
			};

			this.init = function() {
				$(this).click(function(e) {
					if ($(this).hasClass("wzb-dropdown-pressed")) {
						dropdown.unload();
					} else {
						dropdown.show();
					}
					e.stopPropagation();
				});
				$('.wzb-dropdown-list-item', this.list).hover(function() {
					$(this).addClass('wzb-dropdown-list-item-hover');
				}, function() {
					$(this).removeClass("wzb-dropdown-list-item-hover");
				}).click(dropdown.select);
			};
			this.init();
		});
		return this;
	};
})(jQuery);