$(function(){
	
	//会员头像
	photoDoing();

	//导航顶部贴片
	if($(".fixbox").offset() !== undefined){
		var v_modTop = $(".fixbox").offset().top;
		var on_scroll = function(){
			var top = $(document).scrollTop();
			if(top == 0){
				top = document.documentElement.scrollTop;
			}
	
			if(top > v_modTop){
				if('undefined' == typeof(document.body.style.maxHeight)){   //兼容iE6
					$('.fix_nav').addClass('abs');
					$('.fix_nav').css('top',$(window).scrollTop());
				}else{
					$('.fix_nav').addClass('fixed ie');
				}
			}else{
				$('.fix_nav').removeClass('abs');
				$('.fix_nav').removeClass('fixed ie');
			}
		}
		$(window).scroll(on_scroll);
		on_scroll();
	}
	
	//我的学习地图
	$(".xyd-learn-content").hover(function(){
		$(".xyd-learn-prev").css("height",$(".xyd-learn-content").outerHeight(true));
		$(".xyd-learn-next").css("height",$(".xyd-learn-content").outerHeight(true));
		$(".xyd-learn-prev").show();
		$(".xyd-learn-next").show();
	},function(){
		$(".xyd-learn-prev").hide();
		$(".xyd-learn-next").hide();
	});

});

function photoDoing(){
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
			window.location.href = $(this).find('a').attr('href');
		}
	});	
	
	//wzb-transfer-sub
	$(".wzb-transfer-sub").hover(function(){
		 $(this).children(".wzb-transfer").show();
	},function(){
		 $(this).children(".wzb-transfer").hide();
	});
}

function courseDoing(){	
	$(".wzb-list-hover").hover(function(){
	   $(this).find(".subcont").stop().animate({bottom:"0px"});
	   $(this).find(".subbox").stop().animate({bottom:-$(this).find(".subbox").outerHeight(true)});
	},function(){
	   $(this).find(".subcont").stop().animate({bottom:"-260px"});
	   $(this).find(".subbox").stop().animate({bottom:"0px"});
	});
}
