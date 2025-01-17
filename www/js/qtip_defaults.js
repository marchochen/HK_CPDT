$.fn.qtip.defaults = {
   // 页面加载完成就创建提示信息的元素  
   prerender: false,   
   // 为提示信息设置id，如设置为myTooltip  
   // 就可以通过ui-tooltip-myTooltip访问这个提示信息  
   id: false,   
   // 每次显示提示都删除上一次的提示  
   overwrite: true,   
   // 通过元素属性创建提示  
   // 如a[title]，把原有的title重命名为oldtitle  
   suppress: true,  
   // 内容相关的设置   
   content: {
      // 提示信息的内容  
      // 如果只设置内容可以直接 content: "提示信息"  
      // 而不需要 content: { text: { "提示信息" } }  
      text: true,   
      // 提示信息使用的元素属性  
      attr: 'title',   
      // ajax插件  
      ajax: false,  
      title: {
         // 提示信息的标题  
         // 如果只设置标题可以直接 title: "标题"  
         text: false,   
         // 提示信息的关闭按钮  
         // 如button:"x",button:"关闭"  
         // 都可以启用关闭按钮  
         button: false  
      }  
   },  
   // 位置相关的设置  
   position: {
      // 提示信息的位置  
      // 如提示的目标元素的右下角(at属性)  
      // 对应 提示信息的左上角(my属性)  
      my: 'top left',   
      at: 'bottom right',  
      // 提示的目标元素，默认为选择器  
      target: FALSE,  
      // 提示信息默认添加到的容器  
      container: FALSE,  
      // 使提示信息在指定目标内可见，不会超出边界  
      viewport: FALSE,        
      adjust: {  
         // 提示信息位置偏移  
         x: 0, y: 0,  
         mouse: TRUE,  
         resize: TRUE,  
         method: 'flip flip'  
      },  
      // 特效  
      effect: function(api, pos, viewport) {
         $(this).animate(pos, {
            duration: 200,  
            queue: FALSE  
         });  
      }  
   },  
   // 显示提示的相关设置  
   show: {
      // 触发事件的目标元素  
      // 默认为选择器  
      target: false,  
      // 事件名称，默认为鼠标移到时  
      // 可以改为click点击  
      event: 'mouseenter',  
      // 特效  
      effect: true,  
      // 延迟显示时间  
      delay: 90,  
      // 隐藏其他提示  
      solo: false,  
      // 在页面加载完就显示提示  
      ready: false,  
      modal: {
         // 启用模态对话框效果  
         on: false,  
         // 特效  
         effect: true,  
         blur: true,  
         escape: true  
      }  
   },  
   // 隐藏提示的相关设置  
   // 参考show  
   hide: {
      target: false,  
      event: 'mouseleave',  
      effect: true,  
      delay: 0,  
      // 设置为true时，不会隐藏  
      fixed: false,  
      inactive: false,  
      leave: 'window',  
      distance: false  
   },  
   // 样式相关  
   style: {
      // 样式名称  
      classes: '',  
      widget: false,  
      width: false,  
      height: false,  
      // tip插件，箭头相关设置  
      tip: {
         corner: true,  
         mimic: false,  
         width: 8,  
         height: 8,  
         border: true,  
         offset: 0  
      }  
   },  
   // 相关事件绑定  
   events: {
      render: null,  
      move: null,  
      show: null,  
      hide: null,  
      toggle: null,  
      visible: null,  
      focus: null,  
      blur: null  
   }
//   对于显示的位置，有以下参数可以设置： 
//   my = [ 
//   'top left', 'top right', 'top center', 
//   'bottom left', 'bottom right', 'bottom center', 
//   'right center', 'right top', 'right bottom', 
//   'left center', 'left top', 'left bottom', 'center' 
//   ] 
//   at = [ 
//   'bottom left', 'bottom right', 'bottom center', 
//   'top left', 'top right', 'top center', 
//   'left center', 'left top', 'left bottom', 
//   'right center', 'right top', 'right bottom', 'center' 
//   ] 
};  