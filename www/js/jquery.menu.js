(function($) {
	$.fn.menu = function(options) {
		options = $.extend({
			transition : {
				overtime : 300,
				outtime : 300
			},
			down : [ 'downimgclass', 'menu_down.gif', 35 ],
			right : [ 'rightimgclass', 'menu_right.gif', 35 ],
			wb_image : '',
			width : 200
		}, options);

		var menu = $(this);
		$(menu).find("ul").parent().each(function(i) {
			var parentLi = $(this).css({
				zIndex : 999 - i
			});
			var currentUl = $(this).find('ul:eq(0)').css({
				width : options.width + 'px',
				display : 'block'
			});
			this._dimensions = {
				w : menu.outerWidth(),
				h : menu.outerHeight(),
				cw : currentUl.outerWidth(),
				ch : currentUl.outerHeight()
			};
			this._offsets = {
				left : currentUl.offset().left,
				top : currentUl.offset().top
			};
			this.istop = currentUl.parents("ul").length == 1 ? true : false;

			// parentLi.children("a:eq(0)").css(this.istop ? {
			// } : {}).append('<img src="' + options.wb_image + (this.istop ?
			// options.down[1] : options.right[1]) + '" class="' + (this.istop ?
			// options.down[0] : options.right[0]) + '"/>');

			parentLi.hover(function(e) {
				var target = $(this).children("ul:eq(0)");
				var offset_left = (this.istop) ? 0 : this._dimensions.cw;
				var offset_top = (this.istop) ? this._dimensions.h : 0;

				if (target.queue().length <= 1) {
					target.css({
						left : offset_left + "px",
						top : offset_top + "px",
						width : this._dimensions.cw + 'px'
					});

					if ($.browser.msie && $.browser.version == 6.0) {
						target.show();
					} else {
						target.animate({
							height : 'show',
							opacity : 'show'
						}, 300);
					}
				}
			}, function(e) {
				var target = $(this).children("ul:eq(0)");

				if ($.browser.msie && $.browser.version == 6.0) {
					target.hide();
				} else {
					target.animate({
						height : 'hide',
						opacity : 'hide'
					}, 300);
				}
			});
		});

		$(menu).find("ul").css({
			display : 'none',
			visibility : 'visible'
		});
		return this;
	};
})(jQuery);