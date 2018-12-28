(function($) {
	$.createTable = function(dt, p) {
		p = $.extend({
			width : '100%',
			url : false,
			method : 'POST',
			view : 'list',
			useView : true,
			usepager : true,
			simplepager : false,
			hideHeader : false,
			page : 1,
			total : 1,
			rp : 8,
			sortname : '',
			sortorder : '',
			params : [],
			preProcess : false,
			onSuccess : false,
			message : {
				lab_table_first : getLabel('lab_table_first'),
				lab_table_prev : getLabel('lab_table_prev'),
				lab_table_next : getLabel('lab_table_next'),
				lab_table_last : getLabel('lab_table_last'),
				lab_table_refresh : getLabel('lab_table_refresh'),
				lab_table_loading : getLabel('lab_table_loading'),
				lab_table_error : getLabel('lab_table_error'),
				lab_table_empty : getLabel('lab_table_empty')
			}
		}, p);

		dt.addData = function(data) {
			if (!data) {
				dt.loadError();
				return false;
			}

			data = $.extend({
				rows : [],
				page : 0,
				total : 0
			}, data);

			if (p.preProcess) {
				data = p.preProcess(data);
			}

			$('.datatable-pager-btn-reload', dt.pager).removeClass('datatable-pager-btn-reload-loading');

			dt.loading = false;
			//alert(data.total);
			p.total = data.total;
			p.page = data.page;
			p.pages = Math.ceil(p.total / p.rp);
			$(dt.stat).hide();
			dt.buildpager(data.total <= 0 ? true : false);

			var tbody = $("tbody:first", dt.table).get(0);
			if (!tbody) {
				tbody = document.createElement('tbody');
				$(dt.table).append(tbody);
			}
			if (!$(tbody).hasClass('datatable-table-tbody')) {
				$(tbody).addClass('datatable-table-tbody');
			}
			$(tbody).empty();
			
			if (p.total == 0) {
				$('tr, a, td, div', dt).unbind();
				if (dt.stat) {
					$(dt.stat).show();
					$('span', dt.stat).html(p.message.lab_table_empty).show();
				}
			} else {

				if (p.view && p.view === 'grid') {
					$('.datatable-table-thead', dt.table).hide();

					var tr = document.createElement('tr');
					var td = document.createElement('td');
					var grid = $('<div class="datatable-table-grid-contrainer"></div>');
					$.each(data.rows, function(i, row) {
						if (typeof p.gridTemplate == 'function') {
							$(grid).append("<div class='datatable-table-grid'><div class='datatable-table-grid-content'>" + dt.htmlDecode(p.gridTemplate(row)) + "</div></div>");
						}
						if ((i + 1) % 4 == 0) {
							$(grid).append("<div class='clear'></div>");
						}
					});
					$(td).append(grid);
					$(tr).append(td);
					$(tbody).append(tr);
				} else {
					$('.datatable-table-thead', dt.table).show();

					$.each(data.rows, function(i, row) {
						var tr = document.createElement('tr');

						if (row.id) {
							tr.id = 'row' + row.id;
						}

						for (var i = 0; i < p.colModel.length; i++) {
							var cm = p.colModel[i];

							var td = document.createElement('td');
							if (cm.align) {
								td.align = cm.align;

								if (cm.align == 'center') {
									$(td).addClass('text-center');
								} else if (cm.align == 'right') {
									$(td).addClass('text-right');
								} else {
									$(td).addClass('text-left');
								}
							}

							if (typeof cm.format == 'function') {
								td.innerHTML = dt.htmlDecode(cm.format(row));
							} else {
								if (typeof cm.name !== undefined) {
									td.innerHTML = dt.htmlDecode(row[cm.name]);
								}
							}
							$(td).addClass('datatable-table-column');
							$(tr).append(td);

							td = null;
							cm = null;
						}
						$(tr).addClass('datatable-table-row');
						$(tbody).append(tr);
						tr = null;
					});
					var tr = $("tr.datatable-table-row:last", tbody);
					if (!$(tr).hasClass('datatable-table-row-last')) {
						$(tr).addClass('datatable-table-row-last');
					}
				}
			}

			try {
				r();
			} catch (e) {
			}

			if (p.onSuccess) {// 完成后的回调函数
				p.onSuccess(this);
			}
		};

		dt.htmlEncode = function(value) {
			return !value ? value : String(value).replace(/&/g, "&amp;").replace(/>/g, "&gt;").replace(/</g, "&lt;").replace(/"/g, "&quot;");
		};

		dt.htmlDecode = function(value) {
			return !value ? value : String(value).replace(/&amp;/g, "&").replace(/&gt;/g, ">").replace(/&lt;/g, "<").replace(/&quot;/g, '"');
		};

		dt.loadError = function() {
			$('span', dt.stat).html(p.message.lab_table_error);
			$(dt.stat).show();
			$(dt.body).empty();

			dt.loading = false;
			$('.datatable-pager-btn-reload', dt.pager).removeClass('datatable-pager-btn-reload-loading');
		};

		dt.loadData = function() {
			if (dt.loading) {
				return true;
			}

			dt.loading = true;
			if (!p.url) {
				return false;
			}

			$('.datatable-pager-btn-reload', dt.pager).addClass('datatable-pager-btn-reload-loading');

			if (!p.newp) {
				p.newp = 1;
			}
			var param = [ {
				name : 'pageNo',
				value : p.newp
			}, {
				name : 'pageSize',
				value : p.rp
			}, {
				name : 'sortname',
				value : p.sortname
			}, {
				name : 'sortorder',
				value : p.sortorder
			} ];

			if (p.params) {
				for (var pi = 0; pi < p.params.length; pi++) {
					param[param.length] = p.params[pi];
				}
			}

			$.ajax({
				type : 'POST',
				dataType : 'json',
				url : p.url,
				data : param,
				success : function(data) {
					dt.addData(data);
				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					try {
						dt.loadError();
						if (p.onError) {
							p.onError(XMLHttpRequest, textStatus, errorThrown);
						}
					} catch (e) {
					}
				}
			});
		};

		dt.changePage = function(type, num) {
			if (this.loading) {
				return true;
			}
			switch (type) {
			case 'first':
				p.newp = 1;
				break;
			case 'prev':
				if (p.page > 1) {
					p.newp = parseInt(p.page) - 1;
				}
				break;
			case 'next':
				if (p.page < p.pages) {
					p.newp = parseInt(p.page) + 1;
				}
				break;
			case 'last':
				p.newp = p.pages;
				break;
			case 'num':
				var nv = isNaN(num) ? 1 : num;
				nv = (nv < 1) ? 1 : nv;
				nv = (nv > p.pages) ? p.pages : nv;
				p.newp = nv;
				break;
			}
			if (p.newp == p.page) {
				return false;
			}
			dt.loadData();
		};

		dt.changeView = function(view) {
			if (this.loading) {
				return true;
			}
			p.view = view;
			dt.loadData();
		};

		dt.changeSort = function(th) {
			if (dt.loading) {
				return true;
			}
			if (p.sortname == $(th).attr('abbr')) {
				if (p.sortorder == 'asc') {
					p.sortorder = 'desc';
				} else {
					p.sortorder = 'asc';
				}
			} else {
				if (p.sortorder == '') {
					p.sortorder = 'asc';
				}
			}
			p.sortname = $(th).attr('abbr');

			$('.sort', dt.table).removeClass('sort');
			$('.sortasc', dt.table).removeClass('sortasc');
			$('.sortdesc', dt.table).removeClass('sortdesc');
			$(th).addClass('sort' + p.sortorder);

			dt.loadData();
		};

		dt.buildpager = function(empty) {
			var html = "";
			if (empty == undefined || !empty) {
				html += "	<a class='datatable-pager-btn datatable-pager-btn-first'></a>";
				html += "	<a class='datatable-pager-btn datatable-pager-btn-prev'></a>";
				var start = ((p.page - 2) > 0) ? (p.page - 2) : 1;
				var end = ((p.page + 2) < p.pages) ? (p.page + 2) : p.pages;
				if (start - 1 >= 3) {
					html += "	<a num='1' class='datatable-pager-btn datatable-pager-btn-num'>" + 1 + "</a>";
					html += "	<span class='datatable-pager-text'>...</span>";
				}
				for (var i = start; i <= end; i++) {
					html += "	<a num='" + i + "' class='datatable-pager-btn datatable-pager-btn-num " + (p.page == i ? "active" : "") + "'>" + i + "</a>";
				}
				if (p.pages - end >= 3) {
					html += "	<span class='datatable-pager-text'>...</span>";
					html += "	<a num='" + p.pages + "' class='datatable-pager-btn datatable-pager-btn-num'>" + p.pages + "</a>";
				}
				html += "	<a class='datatable-pager-btn datatable-pager-btn-next'></a>";
				html += "	<a class='datatable-pager-btn datatable-pager-btn-last'></a>";
				html += "	<a class='datatable-pager-btn datatable-pager-btn-reload'></a>";
				if (p.colModel && p.colModel.length > 0 && p.useView && p.gridTemplate) {
					html += "	<div class='datatable-pager-separator'></div>";
					html += "<a view='list' class='datatable-pager-switch datatable-pager-switch-list " + ((p.view && p.view === 'list') ? "active" : "") + "'></a>";
					html += "<a view='grid' class='datatable-pager-switch datatable-pager-switch-grid " + ((p.view && p.view === 'grid') ? "active" : "") + "'></a>";
				}
			} else {
				html += "	<a class='datatable-pager-btn datatable-pager-btn-reload'></a>";
			}
			$(dt.pager).html(html);

			$('.datatable-pager-btn-reload', dt.pager).click(function() {
				dt.loadData();
			});
			$('.datatable-pager-btn-first', dt.pager).click(function() {
				dt.changePage('first');
			});
			$('.datatable-pager-btn-prev', dt.pager).click(function() {
				dt.changePage('prev');
			});
			$('.datatable-pager-btn-next', dt.pager).click(function() {
				dt.changePage('next');
			});
			$('.datatable-pager-btn-last', dt.pager).click(function() {
				dt.changePage('last');
			});
			$('.datatable-pager-btn-num', dt.pager).click(function() {
				dt.changePage('num', $(this).attr('num'));
			});
			$('.datatable-pager-switch', dt.pager).click(function() {
				dt.changeView($(this).attr('view'));
			});
		};

		$(dt).addClass('datatable');
		if (p.width != 'auto') {
			$(dt).width(p.width);
		}

		// buttons
		if (p.buttons) {
			dt.toolbar = document.createElement('div');
			$(dt.toolbar).addClass('datatable-toolbar');

			for (var i = 0; i < p.buttons.length; i++) {
				var btn = p.buttons[i];
				if (!btn.separator) {
					var button = document.createElement('button');
					button.innerHTML = btn.name;
					button.name = btn.name;
					button.onpress = btn.onpress;

					if (btn.onpress) {
						$(button).click(function() {
							this.onpress(this.name);
						});
					}

					$(button).addClass('wzb-button-out');
					$(button).hover(function() {
						$(this).addClass('wzb-button-over');
					}, function() {
						$(this).removeClass('wzb-button-over');
					});
					$(dt.toolbar).append(button);
				} else {
					$(dt.toolbar).append("<div class='datatable-toolbar-separator'></div>");
				}
			}
			$(dt.toolbar).append("<div style='clear:both;'></div>");
			$(dt).append(dt.toolbar);
		}

		dt.body = document.createElement('div');
		$(dt.body).addClass('datatable-body');

		dt.table = document.createElement('table');
		dt.table.cellPadding = 0;
		dt.table.cellSpacing = 0;
		$(dt.table).addClass('datatable-table');

		if (p.colModel && !p.hideHeader) {
			var thead = document.createElement('thead');
			$(thead).addClass('datatable-table-thead');

			var tr = document.createElement('tr');
			for (var i = 0; i < p.colModel.length; i++) {
				var cm = p.colModel[i];
				var th = document.createElement('th');
				th.innerHTML = '&nbsp;' + cm.display;
				if (cm.name && cm.sortable) {
					$(th).attr('abbr', cm.name);

					$(th).click(function() {
						dt.changeSort(this);
					});

					if ($(th).attr('abbr') == p.sortname) {
						$(th).addClass('sort' + p.sortorder);
					}
				}

				if (cm.align) {
					th.align = cm.align;
					if (cm.align == 'center') {
						$(th).addClass('text-center');
					} else if (cm.align == 'right') {
						$(th).addClass('text-right');
					} else {
						$(th).addClass('text-left');
					}
				}
				if (cm.width) {
					$(th).attr('width', cm.width);
				}

				if (p.sortorder == '') {
					p.sortorder = 'asc';
				}

				$(th).addClass('datatable-table-column-header');

				if (cm.sortable) {
					$(th).hover(function() {
						$(this).addClass('datatable-table-column-header-hover');

						if ($(this).attr('abbr') && $(this).attr('abbr') != p.sortname) {
							$(this).addClass('sort' + p.sortorder);
						} else if ($(this).attr('abbr') && $(this).attr('abbr') == p.sortname) {
							var no = (p.sortorder == 'asc') ? 'desc' : 'asc';
							$(this).removeClass('sort' + p.sortorder).addClass('sort' + no);
						}
					}, function() {
						$(this).removeClass('datatable-table-column-header-hover');

						if ($(this).attr('abbr') && $(this).attr('abbr') != p.sortname) {
							$(this).removeClass('sort' + p.sortorder);
						} else if ($(this).attr('abbr') && $(this).attr('abbr') == p.sortname) {
							var no = (p.sortorder == 'asc') ? 'desc' : 'asc';
							$(this).addClass('sort' + p.sortorder).removeClass('sort' + no);
						}
					});
				}
				$(tr).append(th);

				cm = null;
				th = null;
			}
			$(tr).addClass('datatable-table-row');

			$(thead).append(tr);
			$(dt.table).append(thead);
		}

		if (p.view && p.view === 'grid') {
			$('.datatable-table-thead', dt.table).hide();
		}
		$(dt.body).append(dt.table);
		$(dt).append(dt.body);

		// Loading
		dt.stat = document.createElement('div');
		$(dt.stat).addClass('datatable-stat').append('<span class="wzb-common-text">' + p.message.lab_table_loading + '</span>');
		$(dt).append(dt.stat);

		// 分页
		if (p.usepager) {
			dt.pager = document.createElement('div');
			$(dt.pager).addClass('datatable-pager').html("<a class='datatable-pager-btn datatable-pager-btn-reload'></a>");
			$(dt).append(dt.pager);
			$('.datatable-pager-btn-reload', dt.pager).click(function() {
				dt.loadData();
			});
		}
		$(dt).append("<div style='clear:both;'></div>");

		dt.params = p;
		dt.datatable = dt;

		if (p.url) {
			dt.loadData();
		}
		return dt;
	};

	// 修改配置参数
	$.fn.setParams = function(p) {
		p = $.extend({
			page : 1,
			newp : 1
		}, p);

		return this.each(function() {
			if (this.datatable) {
				$.extend(this.params, p);
			}
		});
	};

	// 根据新的参数重新加载列表
	$.fn.reloadTable = function(params) {
		if (params !== undefined && params.params !== undefined) {
			params.params = $.buildTableParams(params.params);
		}

		$(this).setParams(params);
		return this.each(function() {
			if (this.datatable && this.params.url) {
				this.datatable.loadData();
			}
		});
	};

	$.fn.table = function(p, not_merge_params) {
		p = p || {};

		if (p.params !== undefined) {
			p.params = $.buildTableParams(p.params);
		}

		$(this).each(function() {
			$.createTable(this, p);
		});
		return this;
	};
})(jQuery);

jQuery.buildTableParams = function(params) {
	var ps = [];
	if (params !== undefined) {
		$.each(params, function(i, n) {
			var p = {
				name : i,
				value : n
			};
			ps[ps.length] = p;
		});
	}
	return ps;
};