appModule.factory('SliderEvent', function () {
    var sliderEvent = function (option){
        var slider = option.slider;
        var startPost = {}; //用来存储开始位置
        var endPos = {};    //用来存储滑动的距离
        var startListener = option.startListener;
        var moveListener = option.moveListener;
        var endListener = option.endListener;   
        this.handleEvent = function(event) {
            var self = this;                                   
            switch (event.type){
                case 'touchstart':
                    self.start(event);
                    break;
                case 'touchmove':
                    self.move(event);
                    break;
                case 'touchend':
                    self.end(event);
                    break;
                case 'mousedown':
                    self.mouseStart(event);
                    break;
                case 'mousemove':
                    self.mouseMove(event);
                    break;
                case 'mouseup':
                    self.mouseEnd(event);
                    break;
            }
        };
        this.start = function(event) {              
            event.preventDefault();                      // 阻止触摸事件的默认动作,即阻止滚屏
            var touch = event.touches[0];                // touches数组对象获得屏幕上所有的touch，取第一个touch
            startPos = {                                 // 取第一个touch的坐标值
                x : touch.pageX,
                y : touch.pageY,
                time: +new Date()
            };  
            // 绑定事件
            slider.addEventListener('touchmove', this, false);
            slider.addEventListener('touchend', this, false);
            if(startListener)startListener();               
        };
        this.mouseStart = function(event){
             event.preventDefault();
             startPos = {                                 // 取第一个touch的坐标值
                x : event.clientX,
                y : event.clientY,
                time: +new Date()
            };  
            // 绑定事件
            slider.addEventListener('mousemove', this, false);
            slider.addEventListener('mouseup', this, false);
            if(startListener)startListener();
        };
        this.move = function(event) {           
            event.preventDefault();                      // 阻止触摸事件的默认行为，即阻止滚屏               
            // 当屏幕有多个touch或者页面被缩放过，就不执行move操作
            if (event.touches.length > 1 || event.scale && event.scale !== 1) return;
            var touch = event.touches[0];
            //触摸点移动的距离
            endPos = {
                x : touch.pageX - startPos.x,
                y : touch.pageY - startPos.y
            };
            //执行操作。。。
            if(moveListener)moveListener(endPos);
        };
        this.mouseMove = function(event){
            event.preventDefault();
            endPos = {
                x : event.clientX - startPos.x,
                y : event.clientY - startPos.y
            };
            //执行操作。。。
            if(moveListener)moveListener(endPos);
        };
        this.end = function(event) {
            var duration = +new Date - startPos.time;    // 滑动的持续时间       
            if(endListener)endListener(endPos);
            // 解绑事件            
            slider.removeEventListener('touchmove', this, false);
            slider.removeEventListener('touchend', this, false);            
        };   
        this.mouseEnd = function(event){
            if(endListener)endListener(endPos);
            slider.removeEventListener('mousemove', this, false);
            slider.removeEventListener('mouseup', this, false);
        };
        (function(slider, eventObj){
            if(('ontouchstart' in window) || window.DocumentTouch && document instanceof DocumentTouch){
                slider.addEventListener('touchstart', eventObj, false);
            }else{
                slider.addEventListener('mousedown', eventObj, false);
            }
        })(slider,this);
    };
    return sliderEvent;
 });