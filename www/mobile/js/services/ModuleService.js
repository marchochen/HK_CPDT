var commiting = false;
appModule.service('moduleService',['Ajax',function(Ajax){
	
	return {
		startSlider : function(src){
			var url = src+'.js';
			var pages;
			var width;
			var height;
			var items =" [ ";
			$.ajaxSettings.async = false; 
			$.getJSON(url, function(data) {
				if(typeof(data)== "string"){
					data =$.parseJSON(data);
				}
				 
				pages = data[0].pages;
				width = data[0].width;
				height = data[0].height;
				 
				for(var i=1;i<=pages;i++){
						
					var str_json = " { href:'javascript:toggleHead()', ";
					str_json += " src: '" + src + "_"+i+".jpg',"+"w:"+width+",h:"+height+"}";
					
					if(i!=pages){
						str_json += ",";
					}
					items += str_json;
				}
				items += "]";
				items = eval("("+items+")");
				
				var openPhotoSwipe = function() {
					var pswpElement = document.querySelectorAll('.pswp')[0];
					var options = {
				        history: false,
				        focus: false,
				        showAnimationDuration: 0,
				        hideAnimationDuration: 0,
				        loop:true,
						closeOnScroll : false,
						closeOnVerticalDrag : false,
						pinchToClose : false,
						escKey: false,
						arrowKeys: false,
						barsSize: {top:46, bottom:'auto'},
						shareEl : false,
						closeEl: false,
						tapToToggleControls : false,
						closeElClasses: [],
						arrowEl : false
				    };
					var gallery = new PhotoSwipe(pswpElement, PhotoSwipeUI_Default, items, options);
					gallery.init();
					
					gallery.framework.bind( gallery.scrollWrap,'pswpTap', function(e) {
						
						if(e.detail.pointerType === "touch"){
							
							var display = "";
							
							if(document.querySelector("header").style.display == "none"){
								display = "block";
							}else{
								display = "none";
							}
							
							document.querySelector("header").style.display = display;
							document.querySelector(".pswp__top-bar").style.display = display;
							
						}
					});
					
				}
				
				openPhotoSwipe();
				
			 });
		},

		saveRecord : function(alertService, data, mod_id, res_id, tkh_id, cur_time,unlockOrientation,type){//unlockOrientation表示是否需要解锁屏幕
			
			if(commiting){
				return;
			}
			
			commiting = true;
			
			if(appInd){
				var wait = plus.nativeUI.showWaiting();
				alertService.add('danger', 'mod_save_record');
			}
			var cur_datetime = new Date();
			var org_time = new Date(cur_time);
			var d = (cur_datetime - org_time)/1000;
			var duration = timeToStr(d);
			var status = data.aicc_data[0].attempt[0].status  == "null"? 'I' : data.aicc_data[0].attempt[0].status;
			var params = {
				cmd : 'putparam',
				course_id : res_id,
				student_id : data.cur_usr[0].ent_id,
				lesson_id : mod_id,
				lesson_location : 'default',
				lesson_status : status,
				time : duration,
				start_time : data.start_time,
				encrypted_start_time : data.encrypted_start_time,
				tkh_id : tkh_id
			};
			$.ajax({
				url : serverHost + '/servlet/qdbAction?env=wizb',
				data : params,
				method : 'POST',
				success : function(){
					
					commiting = false;
					
					if(appInd){
						changeWebviewDetail('item' + plus.storage.getItem("nowItmId"), function(){
							
							if(unlockOrientation){
								window.unlockOrientation();//解锁屏幕
							}
							wait.close();
							back();							
						});
					} else {
						back();
					}
				}
			});
		}
	};
	
}]);							
