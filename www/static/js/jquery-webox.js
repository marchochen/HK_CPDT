/**
*
*	Plugin: Jquery.webox
*	Developer: Blank
*	Version: 1.0 Beta
*	Update: 2012.07.08
*
**/
$.extend({
    webox: function (option) {
        var _x, _y, m, allscreen = false;
        if (!option) {
            alert('options can\'t be empty');
            return;
        };
        if (!option['html'] && !option['iframe']) {
            alert('html attribute and iframe attribute can\'t be both empty');
            return;
        };
        option['parent'] = 'webox';
        option['locked'] = 'locked';
        $(document).ready(function (e) {
            $('#webox').remove();
            $('#background').remove();
            var width = option['width'] ? option['width'] : 400;
            var height = option['height'] ? option['height'] : 240;
            $('body').append('<div id="background" style="display:none;"></div><div id="webox" style="min-width:' + width + 'px;height:' + height + 'px;display:none;"><div id="inside" class="fontfamily" style="height:' + parseInt(height - 17) + 'px;">' + (option['iframe'] ? '<iframe class="w_iframe" src="' + option['iframe'] + '" frameborder="0" width="100%" scrolling="yes" style="border:none;overflow-x:hidden;height:' + parseInt(height - 30) + 'px;"></iframe>' : option['html'] ? option['html'] : '') + '</div></div>');
            if (navigator.userAgent.indexOf('MSIE 7') > 0 || navigator.userAgent.indexOf('MSIE 8') > 0) {
                $('#webox').css({ 'filter': 'progid:DXImageTransform.Microsoft.gradient(startColorstr=#55000000,endColorstr=#55000000)' });
            } if (option['bgvisibel']) {
                $('#background').fadeTo('slow', 0.3);
            };
            $('#webox').css({ display: 'block' });
            $('#' + option['locked'] + ' .lockspan').click(function () {
                $('#webox').css({ display: 'none' });
                $('#background').css({ display: 'none' });
            });
            var marginLeft = parseInt(width / 2);
            var marginTop = parseInt(height / 2);
            var winWidth = parseInt($(window).width() / 2);
            var winHeight = parseInt($(window).height() / 2.2);
            var left = winWidth - marginLeft;
            var top = winHeight - marginTop;
            $('#webox').css({ left: left + 45, top: top });
            $('#' + option['locked']).mousedown(function (e) {
                if (e.which) {
                    m = true;
                    _x = e.pageX - parseInt($('#webox').css('left'));
                    _y = e.pageY - parseInt($('#webox').css('top'));
                }
            }).dblclick(function () {
                if (allscreen) {
                    $('#webox').css({ height: height, width: width });
                    $('#inside').height(height);
                    $('.w_iframe').height(height - 30);
                    $('#webox').css({ left: left, top: top });
                    allscreen = false;
                } else {
                    allscreen = true;
                    var screenHeight = $(window).height();
                    var screenWidth = $(window).width(); $
						('#webox').css({ 'width': screenWidth - 18, 'height': screenHeight - 18, 'top': '0px', 'left': '0px' });
                    $('#inside').height(screenHeight - 20);
                    $('.w_iframe').height(screenHeight - 50);
                }
            });
        }).mousemove(function (e) {
            if (m && !allscreen) {
                var x = e.pageX - _x;
                var y = e.pageY - _y;
                $('#webox').css({ left: x });
                $('#webox').css({ top: y });
            }
        }).mouseup(function () {
            m = false;
        });
        $(window).resize(function () {
            if (allscreen) {
                var screenHeight = $(window).height();
                var screenWidth = $(window).width();
                $('#webox').css({ 'width': screenWidth - 18, 'height': screenHeight - 18, 'top': '0px', 'left': '0px' });
                $('#inside').height(screenHeight - 20);
                $('.w_iframe').height(screenHeight - 50);
            }
        });


        var isIE = (document.all) ? true : false;
        var isIE6 = isIE && ([/MSIE (\d)\.0/i.exec(navigator.userAgent)][0][1] == 6);
        function background_iestyle() {
            background.style.width = document.documentElement.clientWidth + "px";
            background.style.height = document.documentElement.clientHeight + "px";
        }
        function webox_iestyle() {
            webox.style.top = document.documentElement.scrollTop + (document.documentElement.clientHeight - webox.offsetHeight) / 2 + "px";
            webox.style.left = document.documentElement.scrollLeft + (document.documentElement.clientWidth - webox.offsetWidth) / 2 + "px";
        }
        if (isIE6) {
            background_iestyle();
            webox_iestyle();
            window.attachEvent("onscroll", function () {
                webox_iestyle();
            })
            window.attachEvent("onresize", background_iestyle);
        }
    }
});