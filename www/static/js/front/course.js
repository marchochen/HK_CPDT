var len = 250;		//课程的简介显示的字数

$(function(){
	//课程简介展开收起
	$("a.open_desc").live('click',function() {
		var sub = $(this).children("i");
		if($(sub).hasClass("fa-angle-down")) {
			$(sub).removeClass("fa-angle-down").addClass("fa-angle-up").prev("span").html(fetchLabel('click_up'));
			var pv = $(this).prev();
			var data = $(pv).attr("data");
			if(data != undefined && data != '') {
				$(pv).empty().append(data.replace(/\r\n/g,"<br />").replace(/\n/g,"<br />"));
			}
		} else {
			$(sub).removeClass("fa-angle-up").addClass("fa-angle-down").prev("span").html(fetchLabel('click_down'));
			var pv = $(this).prev();
			var data = $(pv).attr("data");
			if(data != undefined && data != '') {
			    var schar_str="";  
                var schar_len=0;  
                var schar;  
                var re = /[^\u4e00-\u9fa5]/;  
                for(var i=0;schar=data.charAt(i);i++){  
                    schar_str+=schar;  
                    schar_len+=(re.test(schar) ? 1:2);  
                    if(schar_len >= len){
                        if(schar==" "  ||  !re.test(schar)){
                        	schar_str+="..."; 
                               break;
                         }
                    }  
                }
                $(pv).empty().append(schar_str.replace(/\r\n/g,"<br />").replace(/\n/g,"<br />"));
				//$(pv).empty().append(substr(data, 0, len));
			}
		}
	});
});