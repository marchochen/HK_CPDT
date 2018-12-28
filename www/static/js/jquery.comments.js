(function($) {

	var Comments = function(element, options) {
		this.type = this.options = this.enabled = this.timeout = this.hoverState = this.$element = null
		this.init('comments', element, options)
	}

	Comments.VERSION = '1.1.0'

	Comments.DEFAULTS = {
		title : '',
		delay : 0,
		html : true,
		container : true,
		url : ''
	}

	Comments.prototype.init = function(type, element, options) {
		this.enabled = true
		this.type = type
		this.$element = $(element)
		this.options = this.getOptions(options)
		this.$viewport = this.options.viewport
				&& $(this.options.viewport.selector || this.options.viewport)
		
		this.draw();
	}

	Comments.prototype.getOptions = function(options) {
		options = $.extend({}, this.getDefaults(), this.$element.data(),
				options)
		return options;
	}
	
	Comments.prototype.getDefaults = function () {
		return Comments.DEFAULTS
	}
	
	Comments.prototype.draw = function(){
		var $e = this.$element;
		var $options = this.options;
		
		 dt = $($e).table({
				url : contextPath + this.options.url,
				colModel : [{
					format : function(data) {
						$.extend(data, {
							isMeInd : (meId == data.s_doi_uid),
							isNormal : isNormal,
							s_doi_create_datetime : data.s_doi_create_datetime.substring(0,16),
							s_doi_title : data.s_doi_title.replace(/"/g,"")
						})
						
						return $('#commentTemplate').render(data);
					}
				}],
				rowCallback : function(data){
					
				},
				rp : 10,
				hideHeader : true,
				usepager : true
			})

	}
	
	function Plugin(option) {
		return this.each(function() {
			var $this = $(this)
			var options = typeof option == 'object' && option

			if (option == 'destroy')
				return;
			if (typeof option == 'string')
				data[option]()
			new Comments(this, options);
		})
	}

	$.fn.cwnComments = Plugin;
	$.fn.cwnComments.Constructor = Comments;

})(jQuery);
