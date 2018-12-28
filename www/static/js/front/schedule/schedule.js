$(function () {
  richeng.DateLoading(module.ajaxDate, wb_lang);

  
  $(".wzb-title-15").click(function () {
    $(this).children('.wzb-jiantou').toggleClass('wzb-jiantou-2');
    $('#schedule-' + $(this).children('.wzb-jiantou').attr('data') + '-content').toggle();
  });
  $('.nav-tabs li').click(function () {
	  $('#losedata').remove()
	  $('.losedata').show();
    $('.nav-tabs li').removeClass('active').children('span').hide()
    $(this).addClass('active').children('span').show();
    $('.wzb-title-15').hide();
    $('.aboluo-rightdiv-content div[data=class]').hide()
    $('#schedule-' + $(this).attr('data') + '-content').show();
    if (!$(this).attr('data')) {
    	if($('.wzb-tuwen-list-1').length!=0){
    		$('.losedata').hide();
    	}else{
    			$('.losedata').hide();
    			$('.losedata').eq(0).show();
    }
    	
    	
      $('.wzb-title-15').show();
      $('.aboluo-rightdiv-content div[data=class]').show()
    }
  })
  
  $("#change_content_1").click(function(){
	  $("#check_content").val(1);
	  $("span[class^='aboluo-wangshang1']").addClass("aboluo-wangshang");
	  $("span[class^='aboluo-wangshang1']").removeClass("aboluo-wangshang1");
	  $("span[class^='aboluo-mianshou1']").addClass("aboluo-mianshou");
	  $("span[class^='aboluo-mianshou1']").removeClass("aboluo-mianshou1");
  })
  $("#change_content_2").click(function(){
	  $("#check_content").val(2);
	  $("span[class^='aboluo-wangshang']").addClass("aboluo-wangshang1");
	  $("span[class^='aboluo-wangshang']").removeClass("aboluo-wangshang");
	  $("span[class^='aboluo-mianshou1']").addClass("aboluo-mianshou");
	  $("span[class^='aboluo-mianshou1']").removeClass("aboluo-mianshou1");
  })
  $("#change_content_3").click(function(){
	  $("#check_content").val(3);
	  $("span[class^='aboluo-wangshang1']").addClass("aboluo-wangshang");
	  $("span[class^='aboluo-wangshang1']").removeClass("aboluo-wangshang1");
	  $("span[class^='aboluo-mianshou']").addClass("aboluo-mianshou1");
	  $("span[class^='aboluo-mianshou']").removeClass("aboluo-mianshou");
  })
  
});
var module = (function () {
  var todaydate = null;
  //获取时间的代码,注意IE下不支持“2017-08-14”这种时间格式，只支持“2017/08/14”
  function getSelect(d){
    if (isIE()){
      d = d.replace('-','/');
      d = d.replace('-','/');
    }
    d =  new Date(d);
    var nian = d.getFullYear();
    var yue = d.getMonth()+1
    var day = d.getDate();
    return isIE()?new Date( nian +'/'+ yue+'/'+day ).getTime():new Date( nian +'-'+ yue+'-'+day ).getTime()
  }

  function isIE() { //ie?
    if (!!window.ActiveXObject || "ActiveXObject" in window)
      return true;
    else
      return false;
  }



  function ajaxDate(ifClass, today, call,lang) {
	  this.lang =lang;
	  this.dataArr = $('.aboluo-rilitbody a');
    if (ifClass && todaydate) {
      new showClass(todaydate, call,this.lang,this.dataArr)
    } else {
      var selft = this;
      $.ajax({
        type: "POST",
        contenType: 'application/json; charset=utf-8',
        url: './scheduleItems',
        data: {
          startDate: $('.aboluo-rilitbody a:first').attr('date'),
          endDate: $('.aboluo-rilitbody a:last').attr('date')
        },
        success: function (data) {
          var dateList = JSON.parse(data)
          todaydate = dateList
          for (var i = 0; i < dateList.length; i++) {
            var startTime = dateList[i].itm_eff_start_datetime;
            var endTime = dateList[i].itm_eff_end_datetime;
            selft.getDate(dateList[i], startTime, endTime,dateList)
          }
          if (call) {
            new showClass(todaydate, call,selft.lang,selft.dataArr)
          }
        }
      })
    }
  }
  ajaxDate.prototype.getDate = function (date, startTime, endTime,allDate) {
    var A = $('.aboluo-rilitbody a');
    
    
   /*循环查询课程*/
    var miaclass =[] , wangclss =[] , xiangclass =[];
    for(var i=0;i<allDate.length;i++){
    	switch(allDate[i].itm_type){
    		case 'SELFSTUDY':
    			wangclss.push(allDate[i])
    			break;
    		case 'CLASSROOM':
    			miaclass.push(allDate[i])
    			break;
    		case 'INTEGRATED':
    			xiangclass.push(allDate[i])
    			break;
    			
    	}
    }
    
    var cc = $("#check_content").val();
    var mianshou_class = '';
    var wangshang_class = '';
    if(cc == 1) {
    	mianshou_class = 'aboluo-mianshou';
    	wangshang_class = 'aboluo-wangshang';
    } else if(cc == 2) {
    	mianshou_class = 'aboluo-mianshou';
    	wangshang_class = 'aboluo-wangshang1';
    } else {
    	mianshou_class = 'aboluo-mianshou1';
    	wangshang_class = 'aboluo-wangshang';
    }

    for (var i = 0; i < A.length; i++) {
    	
      var nowTime = A[i].getAttribute('date');
      var div =   document.createElement("div");
      div.className = 'ri-box';
      if(document.querySelectorAll('a[date="' + A[i].getAttribute('date') + '"] .ri-box').length == 0){
    	  A[i].appendChild(div)
      }
      if (endTime == null) {
        if (getSelect(nowTime) >= getSelect(startTime.split(' ')[0])) {
          switch (date.itm_type) {
          case "SELFSTUDY":this.lang.SELFSTUDY
            if (document.querySelectorAll('a[date="' + A[i].getAttribute('date') + '"] .' + wangshang_class).length == 0) {
              var spanEletwo = document.createElement('span');
              var lang = this.lang.SELFSTUDY;
              spanEletwo.onmouseover  = function(){
            	    var arrwang = [];
            	 for(var a=0;a<wangclss.length;a++){
            		 if(getSelect(wangclss[a].itm_eff_start_datetime)<=getSelect($(this).parents('a').attr('date'))){
	            		 if(wangclss[a].itm_eff_end_datetime!=null){
	        		         if(getSelect(wangclss[a].itm_eff_end_datetime)>=getSelect($(this).parents('a').attr('date'))){
	                         arrwang.push(wangclss[a])
	                         $(this).attr('title',''+lang+'('+arrwang.length+')')
	                         }
	        		     }else{
	        		         arrwang.push(wangclss[a])
	                         $(this).attr('title',''+lang+'('+arrwang.length+')')
	        		     }
            		 }
            	 }
              }
        	  A[i].children[0].appendChild(spanEletwo);
        	  spanEletwo.className = wangshang_class;
            }
            break;
          case "CLASSROOM":
            if (document.querySelectorAll('a[date="' + A[i].getAttribute('date') + '"] .' + mianshou_class).length == 0) {
              var spanEletwo1 = document.createElement('span');
              //spanEletwo1.title =''+this.lang.CLASSROOM+'('+miaclass.length+')'
              var lang = this.lang.CLASSROOM;
              spanEletwo1.onmouseover  = function(){
            	    var arrwang = [];
            	 for(var a=0;a<miaclass.length;a++){
            		 if(getSelect(wangclss[a].itm_eff_start_datetime)<=getSelect($(this).parents('a').attr('date'))){
            			 arrwang.push(wangclss[a])
            			 $(this).attr('title',''+lang+'('+arrwang.length+')')
            		 }
            	 }
              }
              A[i].children[0].appendChild(spanEletwo1);
              spanEletwo1.className = mianshou_class;
            }
            break;
          case "INTEGRATED":
            if (document.querySelectorAll('a[date="' + A[i].getAttribute('date') + '"] .aboluo-xiangmushi').length == 0) {
              var spanEletwo2 = document.createElement('span');
            //  spanEletwo2.title =''+this.lang.INTEGRATED+'('+xiangclass.length+')'
              var lang = this.lang.INTEGRATED;
              spanEletwo2.onmouseover  = function(){
            	    var arrwang = [];
            	  
            	 for(var a=0;a<xiangclass.length;a++){
            		 if(getSelect(xiangclass[a].itm_eff_start_datetime)<=getSelect($(this).parents('a').attr('date'))){
	            		 if(xiangclass[a].itm_eff_end_datetime!=null){
	        		         if(getSelect(xiangclass[a].itm_eff_end_datetime)>=getSelect($(this).parents('a').attr('date'))){
	                         arrwang.push(xiangclass[a])
	                         $(this).attr('title',''+lang+'('+arrwang.length+')')
	                         }
	        		     }else{
	        		         arrwang.push(wangclss[a])
	                         $(this).attr('title',''+lang+'('+arrwang.length+')')
	        		     }
            		 }
            	 }
              }
              A[i].children[0].appendChild(spanEletwo2);
              spanEletwo2.className = 'aboluo-xiangmushi';
            }
            break;
          }
        }
      } else {
        if (getSelect(nowTime) >= getSelect(startTime.split(' ')[0]) && getSelect(nowTime) <= getSelect(endTime.split(' ')[0])) {
          switch (date.itm_type) {
          case "SELFSTUDY":
            if (document.querySelectorAll('a[date="' + A[i].getAttribute('date') + '"] .' + wangshang_class).length == 0) {
              var spanEletwo = document.createElement('span');
              var lang = this.lang.SELFSTUDY;
              spanEletwo.onmouseover  = function(){
            	    var arrwang = [];
            	 for(var a=0;a<wangclss.length;a++){
            		 if(getSelect(wangclss[a].itm_eff_start_datetime)<=getSelect($(this).parents('a').attr('date'))){
	            		 if(wangclss[a].itm_eff_end_datetime!=null){
	        		         if(getSelect(wangclss[a].itm_eff_end_datetime)>=getSelect($(this).parents('a').attr('date'))){
	                         arrwang.push(wangclss[a])
	                         $(this).attr('title',''+lang+'('+arrwang.length+')')
	                         }
	        		     }else{
	        		         arrwang.push(wangclss[a])
	                         $(this).attr('title',''+lang+'('+arrwang.length+')')
	        		     }
            		 }
            	 }
              }
              A[i].children[0].appendChild(spanEletwo);
              spanEletwo.className = wangshang_class;
            }
            break;
          case "CLASSROOM":
            if (document.querySelectorAll('a[date="' + A[i].getAttribute('date') + '"] .' + mianshou_class).length == 0) {
              var spanEletwo1 = document.createElement('span');
              var lang = this.lang.CLASSROOM;
              spanEletwo1.onmouseover  = function(){
            	    var arrwang = [];
            	 for(var a=0;a<miaclass.length;a++){
            		 if(getSelect(miaclass[a].itm_eff_start_datetime)<=getSelect($(this).parents('a').attr('date'))
            				 && getSelect(miaclass[a].itm_eff_end_datetime)>=getSelect($(this).parents('a').attr('date'))){
            			 arrwang.push(miaclass[a])
            			 $(this).attr('title',''+lang+'('+arrwang.length+')')
            		 }
            	 }
              }
              A[i].children[0].appendChild(spanEletwo1);
              spanEletwo1.className = mianshou_class;
            }
            break;
          case "INTEGRATED":
            if (document.querySelectorAll('a[date="' + A[i].getAttribute('date') + '"] .aboluo-xiangmushi').length == 0) {
              var spanEletwo2 = document.createElement('span');
              var lang = this.lang.INTEGRATED;
              spanEletwo2.onmouseover  = function(){
            	    var arrwang = [];
            	 for(var a=0;a<xiangclass.length;a++){
            		 if(getSelect(xiangclass[a].itm_eff_start_datetime)<=getSelect($(this).parents('a').attr('date'))){
            			 arrwang.push(xiangclass[a])
            			 $(this).attr('title',''+lang+'('+arrwang.length+')')
            		 }
            	 }
              }
              A[i].children[0].appendChild(spanEletwo2);
              spanEletwo2.className = 'aboluo-xiangmushi';
            }
            break;
          }
        }
      }
    }
  }

  function showClass(nowDate, today,lang,dataArr) {
	  //$('.losedata[data=show]').remove()
	  	$('#schedule-wangshang-content').html('<div data="show" class="losedata"><i class="fa fa-folder-open-o"></i><p>'+lang.prompt+'</p></div>');
	    $('#schedule-mianshou-content').html('<div data="show" class="losedata"><i class="fa fa-folder-open-o"></i><p>'+lang.prompt+'</p></div>');
	    $('#schedule-xiangmu-content').html('<div data="show" class="losedata"><i class="fa fa-folder-open-o"></i><p>'+lang.prompt+'</p></div>');
	  if(document.querySelector('.tap-all.active')){
		  	if($('a[date='+today+']').children().children().length!=0){
		  		$('.losedata[data=show]').hide();
		  	}else{
		  		$('.losedata[data=show]').hide()
		  		 $('.aboluo-rightdiv-content').append('<div data="show" id="losedata" class="losedata"><i class="fa fa-folder-open-o"></i><p>'+lang.prompt+'</p></div>')
		  	}
		 
	  }
    $('.wzb-jiantou').html(0)
    var selft = this;
    for (var i = 0; i < nowDate.length; i++) {
      var startTime = nowDate[i].itm_eff_start_datetime;
      nowDate[i].encItmId = wbEncrytor().cwnEncrypt(nowDate[i].itm_id);
      nowDate[i].appStatusStr = getAppStatusStr(nowDate[i].app ? nowDate[i].app.app_status : "notapp");
      var endTime = nowDate[i].itm_eff_end_datetime;
      if (endTime == null) {
        if (getSelect(today) >= getSelect(startTime.split(' ')[0])) {
          selft.find(nowDate[i]);
        }
      } else {
        if (getSelect(today) >= getSelect(startTime.split(' ')[0]) && getSelect(today) <= getSelect(endTime.split(' ')[0])) {
          selft.find(nowDate[i]);
        }
      }
    }
    
    
    $('.tap-mianshou').hover(function(){
    	$(this).attr('title',lang.CLASSROOM+'('+$('#schedule-mianshou-content .wzb-tuwen-list-1').length+')')
    })
    
    
    
 
 
    $('.tap-wangshang').hover(function(){
    	$(this).attr('title',lang.SELFSTUDY+'('+$('#schedule-wangshang-content .wzb-tuwen-list-1').length+')')
    })
    $('.tap-xiangmushi').hover(function(){
    	$(this).attr('title',lang.INTEGRATED+'('+$('#schedule-xiangmu-content .wzb-tuwen-list-1').length+')')
    })
    $('.tap-all').hover(function(){
    	$(this).attr('title',lang.All+'('+$('.aboluo-rightdiv-content .wzb-tuwen-list-1').length+')')
    })
    
  }
  showClass.prototype.find = function (today) {
	  
	 
	 
    switch (today.itm_type) {
    case "SELFSTUDY":
    $('#schedule-wangshang-content div[data=show]').remove()
      $('#schedule-wangshang-content').append($('#schedule-class-content-show').render(today));
      $('.wzb-jiantou[data=wangshang]').html($('#schedule-wangshang-content').children().length);
      break;
    case "CLASSROOM":
    $('#schedule-mianshou-content div[data=show]').remove()
      $('#schedule-mianshou-content').append($('#schedule-class-content-show').render(today));
      $('.wzb-jiantou[data=mianshou]').html($('#schedule-mianshou-content').children().length);
      break;
    case "INTEGRATED":
    	$('#schedule-xiangmu-content div[data=show]').remove()
      $('#schedule-xiangmu-content').append($('#schedule-class-content-show').render(today));
      $('.wzb-jiantou[data=xiangmu]').html($('#schedule-xiangmu-content').children().length);
      break;
    }
  }
  return {
    ajaxDate: ajaxDate
  }
})()