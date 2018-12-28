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
  				   : ($("body").addClass("sidebar-open") )
		    }),
		    
		    $(".wzb-wrapper").click(function(){
		       $(window).width() <= b.sm - 1 && $("body").hasClass("sidebar-open") && $("body").removeClass("sidebar-open");
		    })
		 }
		}
		
		
$(function(){
//下拉菜单1
$(".wzb-menu ul li").bind("click",function(e){e.stopPropagation();}).bind("click",function(){
    if($(this).children("ul").is(":hidden")){
        $(this).children("ul").show();
		$(this).addClass("cur");
		$(this).siblings().children("ul").hide();
		$(this).siblings().removeClass("cur");
    }else{
	    $(this).children("ul").hide();
	    $(this).removeClass("cur");
	}
	$(document).bind("click", function(){
	    $(".wzb-menu ul li").children("ul").hide();
	    $(".wzb-menu ul li").removeClass("cur");
	});
   
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





	wzbResize.pushMenu($(".wzb-top-toggle"));
});

$(window, ".wzb-body").resize(function(){
	 wzbResize.pushMenu($(".wzb-top-toggle"));
});