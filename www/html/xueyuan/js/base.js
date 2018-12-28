$(function(){
//下拉菜单
$(".xyd-menu ul li").hover(
	function(){
		$(this).children("ul").show();
		$(this).addClass("cur");
	},
	function(){
		$(this).children("ul").hide();
		$(this).removeClass("cur");
});


//导航顶部贴片
if($(".xyd-fixbox").offset() !== undefined){
	var v_modTop = $(".xyd-fixbox").offset().top;
	var on_scroll = function(){
		var top = $(document).scrollTop();
		if(top == 0){
			top = document.documentElement.scrollTop;
		}
	
		if(top > v_modTop) {
			if("undefined" == typeof(document.body.style.maxHeight)) {   //兼容iE6
				$(".xyd-fixcut").addClass("abs");
				$(".xyd-fixcut").css("top",$(window).scrollTop());
			} else {
				$(".xyd-fixcut").addClass("fixed ie");
			}
		} else {
			$(".xyd-fixcut").removeClass("abs");
			$(".xyd-fixcut").removeClass("fixed ie");
		}
	}
	$(window).scroll(on_scroll);
	on_scroll();
};


//会员头像
$(".wzb-user").hover(function(){
	var el = $(this);
	el.find("div").stop().animate({width:200,height:200},"slow",function(){
		el.find("p").fadeIn("fast");
	});

},function(){
	var el = $(this);
	el.find("p").stop(true,true).hide();
	el.find("div").stop().animate({width:60,height:60},"fast");
}).click(function(){
	if($(this).find("a").attr("href") != undefined){
		window.open($(this).find("a").attr("href"));
	}
});


//wzb-list-hover
$(".wzb-list-hover").hover(function(){
	   $(this).find(".subcont").stop().animate({bottom:"0px"});
	   $(this).find(".subbox").stop().animate({bottom:-$(this).find(".subbox").outerHeight(true)});
	},function(){
	   $(this).find(".subcont").stop().animate({bottom:"-260px"});
	   $(this).find(".subbox").stop().animate({bottom:"0px"});
});


//wzb-list-hover
$(".xyd-train-list").hover(function(){
	$(this).find(".xyd-train-info").toggle();
});


//xyd-path-list
$(".xyd-path-list").css("width",$(".xyd-path-biao").width()+$(".xyd-path-info").width()+5);


//课程列表必须选修
$(".xyd-pane .xyd-pane-title").click(function(){
	$(this).children("span").toggleClass("skin-color");
	$(this).find("span i").removeClass("fa-plus-square").removeClass("fa-minus-square").toggleClass($(this).next(".menuInfo").is(":hidden") ? "fa-minus-square" : "fa-plus-square");
	
	$(this).next(".xyd-pane-info").slideToggle(300).siblings(".xyd-pane-info").slideUp("slow");
	
	$(this).siblings().children("span").removeClass("skin-color");
	$(this).siblings().find("span i").removeClass("fa-plus-square").removeClass("fa-minus-square").toggleClass($(this).next(".xyd-pane-info").is(":hidden") ? "fa-minus-square" : "fa-plus-square");
	
});


//课程列表必须选修
$(".xyd-wang .xyd-wang-title .xyd-mwd06 i").click(function(){
	$(this).toggleClass("skin-color");
	$(this).removeClass("fa-chevron-circle-up").removeClass("fa-chevron-circle-down").toggleClass($(this).next(".xyd-pane-info").is(":hidden") ? "fa-chevron-circle-up" : "fa-chevron-circle-down");
	
	$(this).parents(".xyd-wang-title").next(".xyd-wang-info").slideToggle(300).siblings(".xyd-wang-info").slideUp("slow");
	
	$(this).parents(".xyd-wang-title").siblings().find(".xyd-mwd06 i").removeClass("skin-color");
	$(this).parents(".xyd-wang-title").siblings().find(".xyd-mwd06 i").removeClass("fa-chevron-circle-up").removeClass("fa-chevron-circle-down").toggleClass($(this).next(".xyd-wang-info").is(":hidden") ? "fa-chevron-circle-up" : "fa-chevron-circle-down");
});


//学习日程表
$(".xyd-learn-content .xyd-learn-title").click(function(){
	$(this).find("span i").removeClass("fa-plus-square").removeClass("fa-minus-square").toggleClass($(this).next(".xyd-learn-main").is(":hidden") ? "fa-minus-square" : "fa-plus-square");
	
	$(this).next(".xyd-learn-main").toggle();
});

$(".xyd-learn-content").hover(function(){
    $(".xyd-learn-prev").css("height",$(".xyd-learn-content").outerHeight(true));
    $(".xyd-learn-next").css("height",$(".xyd-learn-content").outerHeight(true));
    $(".xyd-learn-prev").show();
	$(".xyd-learn-next").show();
},function(){
    $(".xyd-learn-prev").hide();
	$(".xyd-learn-next").hide();
});


//登录页xyd-toggle
$(".xyd-toggle").hover(function(){
    $(this).find(".xyd-erwm").show();
},function(){
    $(this).find(".xyd-erwm").hide();
});


//我要分享下拉
$(".xyd-share").click(function(){
	$(this).find(".xyd-share-info").toggle();
});


//问答提示弹出框
$(".wzb-tip").click(function(){
	$(".wzb-tip-box").toggle();
});
$(".wzb-tip-button").click(function(){
	$(".wzb-tip-box").hide();
});

//wzb-transfer-sub
$(".wzb-transfer-sub").hover(function(){
     $(this).children(".wzb-transfer").show();
},function(){
     $(this).children(".wzb-transfer").hide();
});


//秒停页
$(".xyd-whole-move").stop().animate({top:70,left:395},2000,function(){
    $(".xyd-whole-up").stop().animate({opacity:1},0,function(){
	    $(".xyd-whole-up").animate({top:182},1000,function(){
		    setTimeout(function(){self.location.href="http://www.baidu.com/"},800);
		});
    });	
});


//调查问卷展示更多
var dc = $(".xyd-survey-list li:gt(2)");
    dc.hide();
$(".xyd-survey-more").show();
$(".xyd-survey-more").click(function(){
	$(".xyd-survey-more").remove();
	if(dc.is(":visible")){
	    dc.slideUp("fast");
	}else{
	    dc.slideDown("fast");
	}
});


//我的下属，选人弹出框
$(".xyd-choose-click").click(function(){
	$(this).parents(".perclick").find(".permod").show();
});
$(".perclose").click(function(){
	$(this).parents(".permod").hide();
});



});