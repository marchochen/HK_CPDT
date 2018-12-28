function tab(){
	$("ul.ul_tab>li").click(
		function(){
			$(this).addClass("cur1").siblings().removeClass("cur1");
			$(this).parent("ul").siblings(".cont_tab1").children().eq($(this).index()).show().siblings().hide();
	});
}	

$(function(){	
//内容页选项卡
$(".wbtabcont li.now").show();
$(".wbtab .wbtabnav li").click(function(){
    $(this).parent(".wbtabnav").find("li").removeClass("now");
    $(this).addClass("now");
    $(this).parents(".wbtab").find(".wbtabcont").hide();
    $(this).parents(".wbtab").find(".wbtabcont:eq("+$(this).parent(".wbtabnav").find("li").index($(this))+")").show();
});


//表格滑动切换背景颜色
$(".wbtable tr").mouseover(function(){
    $(this).css("background-color","#f9f9f9");
});	
$(".wbtable tr").mouseleave(function(){
    $(this).css("background-color","#fff");
});	

//$(".path_list").css("width",$(".path_biao").width()+$(".path_info").width()+5);


$(".nav .smallnav ul li a").css("width",$(".smallnav").outerWidth(true));

//下拉菜单
$(".menu_yhm ul li").hover(
	function(){
		$(this).children("ul").show();
		$(this).addClass("cur");
	},
	function(){
		$(this).children("ul").hide();
		$(this).removeClass("cur");
});


$(window).resize(function(){
	var txta = $(".loadInfo").outerHeight(true);
	var txtb = $(".loadInfo").length;
	$(".loadprev div").css("height",txta*txtb);
	$(".loadnext div").css("height",txta*txtb);
});



//课程列表必须选修
$(".menuPane .menuTit").click(function(){
	$(this).children("span").toggleClass("skin-color");
	$(this).find("span i").removeClass("fa-plus-square").removeClass("fa-minus-square").toggleClass($(this).next(".menuInfo").is(":hidden") ? 'fa-minus-square' : 'fa-plus-square');
	
	$(this).find(".mwd06 i").toggleClass("skin-color");
	$(this).find(".mwd06 i").removeClass("fa-chevron-circle-up").removeClass("fa-chevron-circle-down").toggleClass($(this).next(".menuInfo").is(":hidden") ? 'fa-chevron-circle-up' : 'fa-chevron-circle-down');
	
	$(this).next(".menuInfo").slideToggle(300).siblings(".menuInfo").slideUp("slow");
	
	$(this).siblings().children("span").removeClass("skin-color");
	$(this).siblings().find("span i").removeClass("fa-plus-square").removeClass("fa-minus-square").toggleClass($(this).next(".menuInfo").is(":hidden") ? 'fa-minus-square' : 'fa-plus-square');
	
	$(this).siblings().find(".mwd06 i").removeClass("skin-color");
	$(this).siblings().find(".mwd06 i").removeClass("fa-chevron-circle-up").removeClass("fa-chevron-circle-down").toggleClass($(this).next(".menuInfo").is(":hidden") ? 'fa-chevron-circle-up' : 'fa-chevron-circle-down');
});

$(".loadCont .loadTit").click(function(){
	$(this).find("span i").removeClass("fa-plus-square").removeClass("fa-minus-square").toggleClass($(this).next(".loadInfo").is(":hidden") ? 'fa-minus-square' : 'fa-plus-square');
	
	$(this).next(".loadInfo").toggle();
});

$(".xyd-share").click(function(){
	$(this).find(".xyd-share-info").toggle();
});

$(".train_desc").hover(function(){
	$(this).find(".train_area").toggle();
});


//网上内容提示
$(".mwd02 .toolcolor").hover(function(){
	  $(this).children(".mwdtip").show();
},function(){
	  $(this).children(".mwdtip").hide();
});


//登录页载入效果
//$(".wizbox").animate({left:"0",opacity:'1',width:'380px'},1300);


//问答提示弹出框
$(".questip").click(function(){
	$(".questipList").toggle();
});
$(".questj").click(function(){
	$(".questipList").hide();
});

//我的下属，选人弹出框
$(".percheck").click(function(){
	$(this).parents(".perclick").find(".permod").show();
});
$(".perclose").click(function(){
	$(this).parents(".permod").hide();
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
		//禁止弹出一个新窗口
		//window.open($(this).find('a').attr('href'));
	}
});	
	
	
//秒停页
$(".mtwel").stop().animate({top:70,left:395},2000,function(){
$(".mtup").stop().animate({opacity:1},0,function(){
	$(".mtup").animate({top:182},1000,function(){
		setTimeout(function(){self.location.href='http://www.baidu.com/'},800);
		});
});	

});



//调查问卷展示更多
var dc = $('.diaocha li:gt(2)');
dc.hide();
$(".dcmore").show();
$(".dcmore").click(function(){
	$(".dcmore").remove();
	if(dc.is(":visible")){
	    dc.slideUp('fast');
	}else{
	    dc.slideDown('fast');
	}
});


//输入框获取或移除焦点时显示效果
$(".showhide").focus(function(){
   var value = $(this).val();
   if(value == this.defaultValue){ $(this).val("");}
});

$(".showhide").blur(function(){
   var value = $(this).val();
   if(value == ''){ $(this).val(this.defaultValue);}
});


//评论回复功能
$(".review").click(function(){
    $(this).parents(".parcel").find(".queen").toggle();
});


//我的学习地图
$(".loadCont").hover(function(){
    $(".loadprev").css("height",$(".loadCont").outerHeight(true));
    $(".loadnext").css("height",$(".loadCont").outerHeight(true));
    $(".loadprev").show();
	$(".loadnext").show();
},function(){
    $(".loadprev").hide();
	$(".loadnext").hide();
});


//群组转让管理员及删除
$(".pfindlist").hover(function(){
     $(this).children(".qhover").show();
},function(){
     $(this).children(".qhover").hide();
});

$(".xyd-toggle").hover(function(){
    $(this).find(".xyd-erwm").show();
},function(){
    $(this).find(".xyd-erwm").hide();
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

$(".xyd-people .xyd-menu ul li ul li").not(".xyd-people .xyd-menu ul li ul li:nth-child(2)").hover(
		function(){
			$(this).children("ul").show();
			//$(this).parent(".nav-top").css({"overflow":"visible"})
			
		},
		function(){
			$(this).children("ul").hide();
			//$(this).parent(".nav-top").css({"overflow":"hidden"})
	});
$(".xyd-people").hover(
	function(){
		$(this).find(".xyd-menu>ul>li>ul").stop();
		$(this).find(".xyd-menu>ul>li>ul").show().animate({"height":"400px"},300)
	},
	function(){
		$(this).find(".xyd-menu>ul>li>ul").stop();
		$(this).find(".xyd-menu>ul>li>ul").animate({"height":"0px"},300).hide();
	}
);


$(".xyd-sub .xyd-menu ul li").hover(
        function(){
            //$(this).children("ul").css("height",$(this).children("ul").children().length*47+"px");
        	$(this).children("ul").show();
             $(this).addClass("cur");
         
        },
        function(){
            // $(this).children("ul").css("height","0px");
        	$(this).children("ul").hide();
           $(this).removeClass("cur");
        });

});