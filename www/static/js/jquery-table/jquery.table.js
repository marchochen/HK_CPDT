(function ($) {
    $.createTable = function (dt, p) {
        p = $.extend({
            width: '100%',
            url: false,
            method: 'POST',
            view: 'list',
            rowSize: 4,
            useView: true,
            usepager: true,
            loadmore: false,
            showpager: 10, // 表示:分页条下面一共会有多少个页
            simplepager: false,
            hideHeader: false,
            page: 1,
            total: 1,
            rp: 8,
            sortname: '',
            sortorder: '',
            params: [],
            preProcess: false,
            onSuccess: false,
            completed: false,
            rowCallback: false,
            async: true,
            tdWidth: '100%',
            trLine: true,
            useCss: '',
            message: {
                lab_table_first: fetchLabel('lab_table_first'),
                lab_table_prev: fetchLabel('lab_table_prev'),
                lab_table_next: fetchLabel('lab_table_next'),
                lab_table_last: fetchLabel('lab_table_last'),
                lab_table_refresh: fetchLabel('lab_table_refresh'),
                lab_table_loading: fetchLabel('lab_table_loading'),
                lab_table_error: fetchLabel('lab_table_error'),
                lab_table_empty: fetchLabel('lab_table_empty'),
                lab_table_data_null: fetchLabel('lab_table_data_null'),
                lab_table_data_no_more: fetchLabel('lab_table_data_no_more'),
                lab_table_btn_load_more: fetchLabel('lab_table_btn_load_more')
            }
        }, p);

        dt.addData = function (data) {
            if (!data) {
                dt.loadError();
                return false;
            }

            data = $.extend({
                rows: [],
                page: 0,
                total: 0
            }, data);

            if (p.preProcess) {
                data = p.preProcess(data);
            }

            //$('.datatable-pager-btn-reload', dt.pager).removeClass('datatable-pager-btn-reload-loading');

            dt.loading = false;
            p.total = data.total;	//总搜
            p.page = data.page;	//页码
            p.pages = Math.ceil(p.total / p.rp);	//页数

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

            if (!p.loadmore || p.isReload) {
                $(tbody).empty();
                $(dt.loadMoreMenu).remove();
                p.isReload = false;
            }

            if (p.total == 0) {
                $('tr, a, td, div', dt).unbind();
                $(dt.pager).hide();
                if (dt.stat) {
                    $(dt.stat).show();

                    $(dt.stat).html('<div class="losedata"><i class="fa fa-folder-open-o"></i><p>' + p.message.lab_table_empty + '</p></div>');

                    $('span', dt.stat).html('').show();
                }
            } else {
                $(dt.pager).show();
                if (p.view && p.view === 'grid') {
                    $('.datatable-table-thead', dt.table).hide();

                    var tr = document.createElement('tr');
                    var td = document.createElement('td');
                    if (p.rowCallback) {
                        $(tbody).append(tr);
                        $(tbody).find("tr:last").append(td);
                    }
                    var grid = $('<div class="datatable-table-grid-contrainer"></div>');
                    $.each(data.rows, function (i, row) {
                        if (typeof p.gridTemplate == 'function') {
                            $(grid).append("<div class='datatable-table-grid-" + p.rowSize + "'><div class='datatable-table-grid-content'>" + dt.htmlDecode(p.gridTemplate(row)) + "</div></div>");
                        }
                        if ((i + 1) % p.rowSize == 0) {
                            $(grid).append("<div class='clear'></div>");
                        }

                        if (p.rowCallback) {
                            $(tbody).find("tr:last").find("td").append(grid);
                            p.rowCallback(row);
                        }
                    });
                    if (!p.rowCallback) {
                        $(td).append(grid);
                        $(tr).append(td);
                        $(tbody).append(tr);
                    }
                } else {
                    $('.datatable-table-thead', dt.table).show();
                    if (p.completed) {// 完成后的回调函数
                        p.completed(data);
                    }
                    $.each(data.rows, function (i, row) {
                        var tr = document.createElement('tr');

                        if (row.id) {
                            tr.id = 'row' + row.id;
                        }
                        if (p.rowCallback) {
                            if (p.trLine) {
                                $(tr).addClass('datatable-table-row');
                            }
                            $(tbody).append(tr);
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

                            if (cm.tdWidth) {
                                $(td).attr('width', cm.tdWidth);
                            }

                            if (typeof cm.format == 'function') {
                                td.innerHTML = dt.htmlDecode(cm.format(row));
                            } else {
                                if (typeof cm.name !== undefined) {
                                    td.innerHTML = dt.htmlDecode(row[cm.name]);
                                }
                            }
                            $(td).addClass('datatable-table-column');
                            if (p.rowCallback) {// 完成后的回调函数
                                $(tbody).find("tr:last").append(td);
                                p.rowCallback(row);
                            } else {
                                $(tr).append(td);
                            }

                            td = null;
                            cm = null;
                        }

                        if (!p.rowCallback) {
                            if (p.trLine) {
                                $(tr).addClass('datatable-table-row');
                            }
                            $(tbody).append(tr);
                        }
                        tr = null;
                    });
                    var tr = $("tr.datatable-table-row:last", tbody);
                    if (!$(tr).hasClass('datatable-table-row-last')) {
                        $(tr).addClass('datatable-table-row-last');
                    }
                }
            }

            if (p.loadmore) {
                $(dt.pager).hide();
                dt.loadMore();
            }

            try {
                r();
            } catch (e) {
            }

            if (p.onSuccess) {// 完成后的回调函数
                p.onSuccess(this, p);
            }
        };

        dt.htmlEncode = function (value) {
            return !value ? value : String(value).replace(/&/g, "&amp;").replace(/>/g, "&gt;").replace(/</g, "&lt;").replace(/"/g, "&quot;");
        };

        dt.htmlDecode = function (value) {
            return !value ? value : String(value).replace(/&amp;/g, "&").replace(/&gt;/g, ">").replace(/&lt;/g, "<").replace(/&quot;/g, '"');
        };

        dt.loadError = function (XMLHttpRequest) {
            $(dt.stat).show();
            var errmsg = p.message.lab_table_error;
            var showerror = true;
            if (XMLHttpRequest) {
                if (XMLHttpRequest.responseText.indexOf("SessionTimeOutException") > -1) {
                    showerror = false;
                } else {
                    errmsg += "   " + XMLHttpRequest.responseText;
                }
            }
            if (showerror) {
                $('.losedata', dt.stat).html(errmsg);
            }
            $(".datatable-table-tbody", dt.body).empty();

            dt.loading = false;
            //$('.datatable-pager-btn-reload', dt.pager).removeClass('datatable-pager-btn-reload-loading');
        };

        dt.loadData = function () {
            if (dt.loading) {
                return true;
            }

            dt.loading = true;
            if (!p.url) {
                return false;
            }

            $(dt.stat).hide();
            $('.datatable-pager-btn-reload', dt.pager).addClass('datatable-pager-btn-reload-loading').after(p.message.lab_table_loading);
            $('.datatable-pager-btn-reload', dt.pager).siblings().hide();
            if (!p.loadmore) {
                $('.datatable-table-tbody', dt.table).remove();
            }

            if (p.loadmore) {
                $(dt.pager).hide();
            }

            if (!p.newp) {
                p.newp = 1;
            }
            var param = [];

            param.push({
                name: 'pageNo',
                value: p.newp
            });
            param.push({
                name: 'pageSize',
                value: p.rp
            });
            if (p.sortname != undefined && p.sortname != '') {
                param.push({
                    name: 'sortname',
                    value: p.sortname
                });
            }
            if (p.sortorder != undefined && p.sortorder != '') {
                param.push({
                    name: 'sortorder',
                    value: p.sortorder
                });
            }

            if (p.params) {
                for (var pi = 0; pi < p.params.length; pi++) {
                    param[param.length] = p.params[pi];
                }
            }

            $.ajax({
                type: 'POST',
                dataType: 'json',
                url: p.url,
                data: param,
                async: p.async,
                success: function (data) {
                    dt.addData(data);
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    try {
                        dt.loadError(XMLHttpRequest);
                        if (p.onError) {
                            p.onError(XMLHttpRequest, textStatus, errorThrown);
                        }
                    } catch (e) {
                    }
                }
            });
        };

        dt.changePage = function (type, num) {
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

        dt.changeView = function (view) {
            if (this.loading) {
                return true;
            }
            p.view = view;
            dt.loadData();
        };

        dt.changeSort = function (th) {
            flg = false;
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

            $('.fa-caret-up', dt.table).removeClass("fa-caret-up");
            $('.fa-caret-down', dt.table).removeClass('fa-caret-down');
            $(th).find("i").addClass("fa-caret-" + ((p.sortorder == 'asc') ? 'up' : 'down'));

            dt.loadData();
        };

        dt.removeSort = function (th) {

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

            $(th).find("i").removeClass("fa-caret-" + ((p.sortorder == 'asc') ? 'down' : 'up'));
        };

        dt.addSort = function (th) {
            if (p.sortname == $(th).attr('abbr')) {
/*                if (p.sortorder == 'asc') {
                    p.sortorder = 'desc';
                } else {
                    p.sortorder = 'asc';
                }*/
            } else {
                if (p.sortorder == '') {
                    p.sortorder = 'asc';
                }
            }
            p.sortname = $(th).attr('abbr');

            $(th).find("i").addClass("fa-caret-" + ((p.sortorder == 'asc') ? 'up' : 'down'));
        };


        dt.loadMore = function () {
            //加载更多
            if (p.loadmore) {
                if (p.rp * p.page < p.total) {
                    if ($(dt).find(".datatable-pager-load-more").length < 1) {
                        dt.loadMoreMenu = document.createElement('div');
                        $(dt.loadMoreMenu).addClass("datatable-pager-load-more").html('<div style="width:100%;text-align:center;"><span><a href="javascript:;" class="datatable-pager-btn-load-more">' + p.message.lab_table_btn_load_more + '</a></span></div>');
                        $(dt).append(dt.loadMoreMenu);
                        $('.datatable-pager-btn-load-more', dt.loadMoreMenu).click(function () {
                            dt.changePage('next');
                        });
                    }
                } else {
                    if (p.page > 1 && $(dt).find(".datatable-pager-load-more").length > 0) {
                        $(dt).find(".datatable-pager-load-more").remove();
//去除没有更多的数据加载显示
//						dt.loadMoreMenu = document.createElement('div');
//						$(dt.loadMoreMenu).addClass("datatable-pager-load-more-text").html('<div style="width:100%;text-align:center;"><span><a href="javascript:;" class="">'+ p.message.lab_table_data_no_more + '</a></span></div>');
//						$(dt).append(dt.loadMoreMenu);
                    }
                }
            }
        };

        dt.buildpager = function (empty) {

            var lab_table_total_results_unit = fetchLabel("lab_table_total_results_unit").replace("{total}", p.total);
            var totalPageHtml = "<br/><p style='padding: 0 6px;display: inline-block;margin: 0;margin-left: 10px;vertical-align: middle;'>" + lab_table_total_results_unit + "</p>";

            //不满一页
            if (p.pages < 2) {
                $(dt.pager).html(totalPageHtml);
                return;
            }
            var html = "";
            if (empty == undefined || !empty) {

                var showpagenum = (p.showpager - 1);	//显示的page数量

                var start = ((p.page - 1) > 0) ? (p.page - 1) : 1;
                var end = ((start + showpagenum) < p.pages) ? (start + showpagenum) : p.pages;

                if ($(".datatable-pager-btn-num").length > 0) {
//					var firstnum = $(".datatable-pager-btn-num:eq(0)").attr("num");
//					var endnum = $(".datatable-pager-btn-num:last").attr("num");
                    _this = this;
                    var firstnum = $(this).find(".datatable-pager-btn-num:eq(0)").attr("num");
                    var endnum = $(this).find(".datatable-pager-btn-num:last").attr("num");
                    if (p.page == endnum) {	//按了页码的最后一个，页码往后走
                        if (end == p.pages && (end - start) < showpagenum) {
                            start = end - showpagenum;
                            if (start <= 0) start = 1;
                        }
                    } else if (p.page == firstnum && p.page != 1) {	//按了页码的第一个，页码往回走
                        //end = p.page;
                        start = end - showpagenum;
                        if (start <= 0) {
                            end = showpagenum - start;
                            start = 1;
                        }
                    } else if (start == end) {	//按了往最后页码
                        end = p.page;
                        start = end - showpagenum;
                        if (start <= 0) start = 1;
                    } else if (start != 1) {	//排除按了往最前页码
                        start = start;
                        end = end;
                        if (end - start < showpagenum) {
                            start = start - (showpagenum - (end - start));
                            if (start <= 0) {
                                start = 1;
                            }
                        }
                    }
                }
                start = parseInt(start); //开始页数
                end = parseInt(end);// 结束页数

                if (p.page > 1) {
                    html += "	<a class='datatable-pager-btn datatable-pager-btn-first'></a>";
                    html += "	<a class='datatable-pager-btn datatable-pager-btn-prev'></a>";
                }

                for (var i = start; i <= end; i++) {
                    html += "	<a num='" + i + "' class='datatable-pager-btn datatable-pager-btn-num " + (p.page == i ? "active" : "") + "'>" + i + "</a>";
                }


                if (p.page < p.pages) {
                    html += "	<a class='datatable-pager-btn datatable-pager-btn-next'></a>";
                    html += "	<a class='datatable-pager-btn datatable-pager-btn-last'></a>";
                    //html += "	<a class='datatable-pager-btn datatable-pager-btn-reload'></a>";
                }

                if (p.colModel && p.colModel.length > 0 && p.useView && p.gridTemplate) {
                    html += "	<div class='datatable-pager-separator'></div>";
                    html += "<a view='list' class='datatable-pager-switch datatable-pager-switch-list " + ((p.view && p.view === 'list') ? "active" : "") + "'></a>";
                    html += "<a view='grid' class='datatable-pager-switch datatable-pager-switch-grid " + ((p.view && p.view === 'grid') ? "active" : "") + "'></a>";
                }
            } else {
                html += "	<a class='datatable-pager-btn datatable-pager-btn-reload'></a>";
            }

            html += totalPageHtml;

            $(dt.pager).html(html);

            $('.datatable-pager-btn-reload', dt.pager).click(function () {
                dt.loadData();
            });
            $('.datatable-pager-btn-first', dt.pager).click(function () {
                dt.changePage('first');
            });
            $('.datatable-pager-btn-prev', dt.pager).click(function () {
                dt.changePage('prev');
            });
            $('.datatable-pager-btn-next', dt.pager).click(function () {
                dt.changePage('next');
            });
            $('.datatable-pager-btn-last', dt.pager).click(function () {
                dt.changePage('last');
            });
            $('.datatable-pager-btn-num', dt.pager).click(function () {
                dt.changePage('num', $(this).attr('num'));
            });
            $('.datatable-pager-switch', dt.pager).click(function () {
                dt.changeView($(this).attr('view'));
            });
        };
        if (p.useCss) {
            $(dt).addClass(p.useCss);
        } else {
            $(dt).addClass('datatable');
        }
        if (p.width != 'auto') {
            $(dt).css('width', p.width);
            //.width();莫名其妙会变成200%
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
                        $(button).click(function () {
                            this.onpress(this.name);
                        });
                    }

                    $(button).addClass('wzb-button-out');
                    $(button).hover(function () {
                        $(this).addClass('wzb-button-over');
                    }, function () {
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
                th.innerHTML = cm.display;
                if (cm.name && cm.sortable) {
                    $(th).attr('abbr', cm.name);

                    th.innerHTML += '<i class="fa mL3 skin-color"></i>'
                    $(th).click(function () {
                        dt.changeSort(this);
                    });

                    if ($(th).attr('abbr') == p.sortname) {
                        var no = p.sortorder == 'asc' ? 'up' : 'down';
                        $(th).find("i").addClass("fa-caret-" + no);
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

                var flg = true;
                if (cm.sortable) {
                    //$(th).css({'cursor':'pointer'})
                    $(th).hover(function () {
                        flg = true;
                        var className = $(this).find("i").attr("class");
                        if (className.indexOf("fa-caret") > 0) {
                            flg = false;
                        }
                        $(this).addClass('datatable-table-column-header-hover');
                        if(!p.sortorder) {
                            dt.addSort(this);
                        }
                        if ($(this).attr('abbr') && $(this).attr('abbr') != p.sortname) {
                            $(this).addClass('sort' + p.sortorder);
                        } else if ($(this).attr('abbr') && $(this).attr('abbr') == p.sortname) {
                            var no = (p.sortorder == 'asc') ? 'desc' : 'asc';
                            $(this).removeClass('sort' + p.sortorder).addClass('sort' + no);
                        }
                    }, function () {
                        if (flg) {
                            if(!p.sortorder) {
                                dt.removeSort(this);
                            }
                        }
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

        //if no data
        dt.stat = document.createElement('div');
        $(dt.stat).addClass('datatable-stat').append('<div class="losedata"><i class="fa fa-folder-open-o"/><p>' + p.message.lab_table_data_null + '</p></div>');
        $(dt).append(dt.stat);

        // 分页
        if (p.usepager) {
            dt.pager = document.createElement('div');
            $(dt.pager).addClass('datatable-pager').html("<a class='datatable-pager-btn datatable-pager-btn-reload'></a>");
            $(dt).append(dt.pager);
            $('.datatable-pager-btn-reload', dt.pager).click(function () {
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
    $.fn.setParams = function (p) {
        p = $.extend({
            page: 1,
            newp: 1
        }, p);

        return this.each(function () {
            if (this.datatable) {
                $.extend(this.params, p);
            }
        });
    };

    // 根据新的参数重新加载列表
    $.fn.reloadTable = function (params) {
        $.extend(params, {isReload: true});
        if (params !== undefined && params.params !== undefined) {
            params.params = $.buildTableParams(params.params);
        }

        $(this).setParams(params);
        return this.each(function () {
            if (this.datatable && this.params.url) {
                this.datatable.loadData();
            }
        });
    };

    $.fn.table = function (p, not_merge_params) {
        p = p || {};

        if (p.params !== undefined) {
            p.params = $.buildTableParams(p.params);
        }

        $(this).each(function () {
            $.createTable(this, p);
        });
        return this;
    };
})(jQuery);

jQuery.buildTableParams = function (params) {
    var ps = [];
    if (params !== undefined) {
        $.each(params, function (i, n) {
            var p = {
                name: i,
                value: n
            };
            ps[ps.length] = p;
        });
    }
    return ps;
};