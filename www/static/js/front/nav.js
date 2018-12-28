function nav(){
	$(".xyd-menu ul>li").mouseenter(
		function(){
			if(!$(this).children("ul").is(":animated")){
				//$(this).children("ul").show().parent().siblings("li").children('ul').hide();
				$(this).children("ul").parent().siblings("li").removeClass('cur');
			}
			//选中当li,移除同级li的样式，新增父li的选中，移除父类同级的选中
			$(this).addClass('cur').siblings().removeClass('cur')	
			.parents("li").addClass("cur").siblings().removeClass('cur');
		}
	);

	$(".xyd-menu ul>li").mouseleave(
		function(){
			//$(this).children("ul").hide();
			$(this).removeClass("cur");	
			//如果当前父节点选中，则不给其他的列选中
			if(!$(this).parents("li").hasClass("cur")){
				checkMenu();
			}
		}
	);
}

//根据链接选中菜单
function checkMenu() {
	var url = window.location.pathname.replace('#/g',"");
//	var page = $(".menu_yhm ul a[href='" + url + "']");
	var page = $(".xyd-menu ul a[href='" + url + "']");
	//标记当前子li是否有子li
	var parent = $(page).closest("ul");
	var length = $(parent).length;
	
	if (length != undefined && length > 0 && url.indexOf('/app/home') < 0) {
		parent = $(parent).parent("li");
	} else {
		parent = $(page).parent("li");
	}
//	if(url.indexOf("/app/exam/") > -1){
//		parent = $(".menu_yhm ul a[href='/app/exam/signup']").parent("li");
//	}
	$(".xyd-menu ul li").removeClass("cur");
	$(parent).addClass("cur");	
}