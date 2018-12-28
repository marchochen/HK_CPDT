function tab(){
	$("ul.ul_tab>li").click(
		function(){
			$(this).addClass("cur1").siblings().removeClass("cur1");
			$(this).parent("ul").siblings(".cont_tab1").children().eq($(this).index()).show().siblings().hide();
	});
}	