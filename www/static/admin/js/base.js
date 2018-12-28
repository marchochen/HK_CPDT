var wzbResize = {
	 pushMenu : function(a){
	    var b =  {
	        xs: 480,
	        sm: 768,
	        md: 992,
	        lg: 1200
	    }
	    $(a).click(function(a){
	       a.preventDefault(),
	       $(window).width() > b.sm - 1 ? $("body").toggleClass("sidebar-collapse") :
			$("body").hasClass("sidebar-open") ? ($("body").removeClass("sidebar-open"), $("body").removeClass("sidebar-collapse")) 
  				   : ($("body").addClass("sidebar-open") );
		    var showing = localStorage.getItem("showing");
		    
	    	if(showing == undefined)
	    	{
	    		// 第一次 
	    		localStorage.setItem("showing","false"); 
	    		return ;
	    	}
	    	if(showing == "true")
	    	{
	    		localStorage.setItem("showing","false");
	    	}
	    	else
	    	{
	    		localStorage.setItem("showing","true");
	    	}
	    	
		 }),
	     $(".wzb-wrapper").click(function(){
	       $(window).width() <= b.sm - 1 && $("body").hasClass("sidebar-open") && $("body").removeClass("sidebar-open");
	     })
	 }
}
$(function(){
	var showing = localStorage.getItem("showing");
	    
 	if(showing != undefined)
 	{
 		// 第一次 
 	 	if(showing == "true")
 	 	{
 	 	 $("body").removeClass("sidebar-collapse")
 	 	}
 	 	else
 	 	{
 	 		$("body").toggleClass("sidebar-collapse")
 	 	}
 	}

 	//下拉菜单1
 	$(function(){
 		var initCurElement = $(".wzb-menu ul li.cur");
 		 $(".wzb-menu ul li").mouseover(function(e){e.stopPropagation();}).hover(function(){
 			if($(this).children("ul").is(":hidden")){						
 		        $(this).children("ul").show();
 				$(this).addClass("cur");
 				$(this).siblings().children("ul").hide();
 				$(this).siblings().removeClass("cur");
 		    }else{
 			    $(this).children("ul").hide();
 			    $(this).removeClass("cur");
 			}
 			
 			if(!initCurElement.hasClass("cur")){
 				initCurElement.addClass("cur");
 			}
 			
 		}).children("ul").find("li").click(function(){
 			$(this).parents('.wzb-menu ul li').removeClass("cur")
 			$(this).parents('.wzb-menu ul li').children("ul").hide()
 		});
 		 
 		
 		for(var i=0;i<$(".wzb-menu ul li").length;i++){
 			var el = $($(".wzb-menu ul li")[i]);
 			el.removeClass("cur");
 			if(!initCurElement.hasClass("cur")){
 				initCurElement.addClass("cur");
 			}
 			el.children("ul").hide();
 		}
 	});
 
	$(".wzb-title-1").click(function(){
	     $(this).siblings(".wzb-list-3").slideToggle();
	});
	
	$(".wzb-display-01").hover(function(){
	     $(this).find(".wzb-transfer").show();
		 $(this).find(".wzb-delete-01").show();
	},function(){
	     $(this).find(".wzb-transfer").hide();
		 $(this).find(".wzb-delete-01").hide();
	});
	
	//会员头像
	$('.wzb-user').hover(function(){
		var el = $(this);
		el.find('div').stop().animate({width:200,height:200},'slow',function(){
			el.find('p').fadeIn('fast');
		});
	
	},function(){
		var el = $(this);
		el.find('p').stop(true,true).hide();
		el.find('div').stop().animate({width:60,height:60},'fast');
	}).click(function(){
		if($(this).find('a').attr('href') != undefined){
			window.open($(this).find('a').attr('href'));
		}
	});
	
	//切换
	$(".wzb-plus-add").click(function(){
		
		$(this).parents("tr").next("tr.wzb-ui-table-son").slideToggle(300).siblings("tr.wzb-ui-table-son").slideUp("fast");
	});
	
	var belong_module = $("input[name='belong_module']").val();
	$(".wzb-menu-son").each(function(){
	   if(belong_module && belong_module == $(this).attr("data")){
		 $(this).next("ul").css("display", "none");
	  	 $(this).parent("li").addClass("cur").siblings().removeClass("cur");
	   }
	});

	wzbResize.pushMenu($(".wzb-top-toggle"));
    $("button[data-toggle='cwn-dropdown']").on('click', function(){
        var parent = $(this).parent();
        var dropdownMenu = $(this).next();
        $(parent).toggleClass("open");
        $(dropdownMenu).offset({'left':$(parent).offset().left, "top": $(this).offset().top + $(this).outerHeight()}).focus()
    })
    $(".cwn-dropdown .cwn-dropdown-menu").on('mouseleave', function(){
        $(this).parent().removeClass("open");
    })
    
    
    var checked_css = "fa-circle";
    var unchecked_css = "fa-circle-thin";
    var datagroup_params = {};
    $("dl[data-group]").each(function(){
        var datagroup = this;
        $(datagroup).children("dd:eq(0)").find("a").prepend('<i class="fa ' + checked_css + '"/>');//获取第一个元素设置为默认选中
        $(datagroup).children("dd:gt(0)").find("a").prepend('<i class="fa '+ unchecked_css + '"/>');//获取其他袁术不选中
        var datagroup_value = $(datagroup).attr("data-value");
        if(datagroup_value != '' && datagroup_value != undefined) {
            $(datagroup).children("dd").find("a").each(function(i, val){
            	var this_ = this;
            	if($(this_).attr("data-value") == datagroup_value){
            		datagroup_check($(this_).find("i"));//只要dd的data-value属性和dl的data-value属性相同
            	}
            })
        }
    })
    $("dl[data-group] dd a i").on('click',function(){
        var children_i = this;
        datagroup_check(children_i);
    })
    $("dl[data-group] dd a").on('click',function(){
        var children_i = $(this).find("i");
        datagroup_check(children_i);
    })
    function datagroup_check(children_i){
        if($(children_i).hasClass(checked_css)) {
            return;//有选中样式返回
        } else {
            $(children_i).addClass(checked_css).removeClass(unchecked_css);//添加选中样式，移除未选中样式
            $(children_i).parents("dd").siblings().find("i").removeClass(checked_css).addClass(unchecked_css);//移除其他选中样式
            var datagroup = $(children_i).parents("dl[data-group]");
            var dgparams = $(datagroup).attr("data-group")
            var dtable = eval($(datagroup).attr("data-table"));
            var column = $(datagroup).attr("data-column");
            var value = $(children_i).parent("a").attr("data-value");
            var p = '{'+ column + ':"' + value + '"}'
            var params = eval("(" + p + ")");
            eval(dgparams + " = $.extend(eval(dgparams), params);");
            $(dtable).reloadTable({
            	params : eval(dgparams)
            });
        }
    }
});
$(window, ".wzb-body").resize(function(){
	 wzbResize.pushMenu($(".wzb-top-toggle"));
});
//搜索框回车事件
$(function(){
	$('.form-control').keydown(function(e){
		var ev = document.all ? window.event : e;
    	if(ev.keyCode==13) {
		$(this).next(".form-submit").trigger("click");
    	}
	})
});