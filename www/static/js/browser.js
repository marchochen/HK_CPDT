getBrowserType = function()
{
    var ua= navigator.userAgent, tem, 
    M= ua.match(/(opera|chrome|safari|firefox|msie|trident(?=\/))\/?\s*(\d+)/i) || [];
    if(/trident/i.test(M[1])){
        tem=  /\brv[ :]+(\d+)/g.exec(ua) || [];
        return ['msie',tem[1] || ''];
    }
    if(M[1]=== 'Chrome'){
        tem= ua.match(/\b(OPR|Edge)\/(\d+)/);
        if(tem!= null) return tem.slice(1).join(' ').replace('OPR', 'Opera');
    }
    M= M[2]? [M[1], M[2]]: [navigator.appName, navigator.appVersion, '-?'];
    if((tem= ua.match(/version\/(\d+)/i))!= null) M.splice(1, 1, tem[1]);
    return M
};
	
	
function process()
{
	type = getBrowserType();

	var flag = false;
	var url = null;
	if(type != undefined) {
		if(type[0] == undefined){
			return;
		}
		if(type[0].toLocaleLowerCase()=='chrome'){
			if(type[1]!=undefined&&parseInt(type[1])>=40){
				flag = true;	
			}
			else{
				url = text_label.lab_url_chrome;
			}
		}
		else if(type[0].toLocaleLowerCase()=='safari'){
			if(type[1]!=undefined&&parseInt(type[1])>=6){
				flag = true;
			}
			else{
				url = text_label.lab_url_safari;
			}
		}
		else if(type[0].toLocaleLowerCase()=='firefox'){
			if(type[1]!=undefined&&parseInt(type[1])>=40){
				flag = true;
			}
			else{
				url = text_label.lab_url_firefox;
			}
		}
		else if(type[0].toLocaleLowerCase()=='msie'){
			if(type[1]!=undefined&&parseInt(type[1])>=9){
				flag = true;
			}
			else{
				url = text_label.lab_url_ie;
			}
		}else if(type.indexOf("Edge") > -1){
			flag = true;
		}
	}
	 
	 if(flag == false){
		//alert("支持的浏览器类型");
		 
		 $("#xyd-move-pagination").css('display','none'); 
		 
  
		 $('#favoriteFunsModal').modal({show:true});		 
		  
	 
		 $("#favoriteFunsSubmit").click(function(){
//				window.location.href=url;
			 window.close();
			 // $('#favoriteFunsModal').modal("hide");
			// window.open(url);
		 })
				 
		 $('#favoriteFunsModal').on('hide.bs.modal', function () {
			 	// 执行一些动作...
			 $("#xyd-move-pagination").css('display','block'); 
		 })
	 }
}
