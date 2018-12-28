$(function(){
	
	//初始化页面脚本，如点击事件，显示效果
	indexModule.init();
	
	//加载上面轮播图片
	indexModule.banner.load();
	
	//加载公告
	indexModule.Announcement.load();
	
	//初始化课程列表
	indexModule.course.init();
	
	//加载精彩专题培训
	indexModule.topic.load();
	
	//如果没有关键岗位，则不显示关键岗位的图片
	indexModule.keyPosition.init();
	
	//如果没有职级发展，则不显示职级发展的图片
	indexModule.gradeMap.init();
	
	//加载评论公开课
	indexModule.course.loadOpen();
	
	//加载讲师列表
	indexModule.instructor.load();
	//人多的提示我不想要，把他干掉方法
	$("#Iknow").click(function(){
        $("#xyd_warning").animate({"bottom":"-110px"},2000);
    })
});

/**
 * 首页逻辑
 */
var indexModule = {
	
	init : function(){//初始化页面脚本，如点击事件，显示效果等。。。
		
		$(".wzb-tab-6>ul>li").hover(function(){
            $(this).find("i").removeClass("zhuan-3")
            $(this).find("i").addClass("zhuan-2")
            if($(this).index()==0){
                $(this).find("i").addClass("rcbiao-1")
                $(this).css({
                    "background":"#00D399",
                    color:"#fff"
                })
            }

            if($(this).index()==1){
                        $(this).find("i").addClass("zshi-1")
                        $(this).css({
                            "background":"#F3A800",
                            color:"#fff"
                        })
                    }

                    if($(this).index()==2){
                        $(this).find("i").addClass("wda-1")
                        $(this).css({
                            "background":"#4E93F8",
                            color:"#fff"
                        })
                    }
                    if($(this).index()==3){
                        $(this).find("i").addClass("qzu-1")
                        $(this).css({
                            "background":"#F05C52",
                            color:"#fff"
                        })
                    }
        }, function () {
            $(this).css({"background":"#fff",color:"#333"})
            $(this).find("i").addClass("zhuan-3")
            $(this).find("i").removeClass('zhuan-2 rcbiao-1 zshi-1 wda-1 qzu-1')


                }
        );
	},
	banner:{//banner模块
		
		load : function(){//加载banner
			
			//轮播图片的模板准备
			$.templates({
				posterTemplate : '<li {{if url}}onclick="window.open(\'{{>url}}\')"{{/if}} style="background:url({{>src}}) 50% 0 no-repeat;{{if url}}cursor:pointer;{{/if}}"></li>',
				posterNumTemplate : '<li></li>'
			});
			
			/**
			 * 添加轮播图片
			 * @param item ，格式为 {url:'',src:''}的json，src为图片的地址，url为图片点击去到的链接地址
			 */
			var appendBannerItem = function(item){
				$("#xyd-banner-pic").append($.render.posterTemplate(item));
			};
			
			/**
			 * 轮播代码
			 */
			var startSlide = function(){
				var $key=0;
                var timer=setInterval(autoplay, 5000);
	                $("#xyd-banner-pic li").eq($key).siblings().stop().fadeOut(0);
	                function autoplay(){
	                $("#xyd-banner-pic li").stop().fadeOut(600);
	                $key++;
	                $key=$key%$("#xyd-banner-pic li").length;
	
	                $("#xyd-banner-pic li").eq($key).stop().fadeIn(600);
	                $("#xyd-banner-pagination li").eq($key).addClass('current').siblings().removeClass('current');
                }
                $("#xyd-banner-pagination li").click(function(event) {
                    $(this).addClass('current').siblings().removeClass('current');
                    $("#xyd-banner-pic li").eq($(this).index()).stop().fadeIn(600).siblings().stop().fadeOut(600);
                    $key=$(this).index();
                });
			};
			
			$.getJSON(ctx+'/app/poster/Json',function(result){
				if(!result || !result.poster || result.poster.length<1) return;
				var poster = result.poster[0];
				
				if(poster.sp_media_file && poster.sp_status =='ON'){
					appendBannerItem({url : poster.sp_url ? ctx+poster.sp_url : "",src : ctx+poster.sp_media_file});
				}
				if(poster.sp_media_file1 && poster.sp_status1 =='ON'){
					appendBannerItem({url : poster.sp_url1 ? ctx+poster.sp_url1 : "",src : ctx+poster.sp_media_file1});
				}
				if(poster.sp_media_file2 && poster.sp_status2 =='ON'){
					appendBannerItem({url : poster.sp_url2 ? ctx+poster.sp_url2 : "",src : ctx+poster.sp_media_file2});
				}
				if(poster.sp_media_file3 && poster.sp_status3 =='ON'){
					appendBannerItem({url : poster.sp_url3 ? ctx+poster.sp_url3 : "",src : ctx+poster.sp_media_file3});
				}
				
				var len = $("#xyd-banner-pic li").length;
				var liHtml = [];
				for(var i=0;i<len;i++){
					liHtml.push($.render.posterNumTemplate({}));
				}
				/**
				 * 如果轮播图个数等于1，什么都不做，直接显示上面图片即可
				 * 如果轮播图个数大于1，显示下方小圆点，并轮播
				 * 如果轮播图个数小于1，直接隐藏掉整个轮播图区域
				 */
				if(len > 1){
					$("#xyd-banner-pagination").html(liHtml.join(''));
					$("#xyd-banner-pagination li:first").addClass("current");
					//执行轮播代码
					startSlide();
				}
				if(len < 1){
					$("#xyd-banner").hide();
				}
			});
		}
	},
	
	Announcement : {//公告模块
		
		scrolAnnounces : function(){//滚动公告代码
			
			var $this = $(".xyd-main-3");
            var scrollTimerr;
            $('.guanggao').hover(function() {
            	clearInterval(scrollTimerr);	
            }, function() {
            	  scrollTimerr = setInterval(function() {
                    scrollNews($this);
                }, 2000);
            }).trigger("mouseleave");
            
            function scrollNews(obj) {
                var $self = obj.find(".guanggao");
                var lineHeight = $self.find("li:first").height();
                $self.animate({
                    "marginTop": -lineHeight + "px"
                }, 1000, function() {
                    $self.css({
                        marginTop: 0
                    });
                    $(".guanggao li:first-child").appendTo($(".guanggao"));
                    $(".guanggao li:first-child").appendTo($(".guanggao"));
                });
            }
		},
		
		load : function(){//加载公告
			
			var size = 10;
			
			$.ajax({url: ctx + '/app/announce/pageJson/0?pageSize='+size ,dataType : 'json' ,success:function(data){
 
				var arrLength = data.rows.length;
				var hasNotRead = true;
				for(var i=0;i<arrLength;i++){//拼接加载的条目
					var item = data.rows[i];
					if(item.msg_title.length>18){
	                 	 item.msg_title=item.msg_title.substring(0,18)+'...';
					 }
					var p = {
						msg_title : item.msg_title,
						msg_id : item.msg_id,
						msg_upd_date : Wzb.displayTime(item.msg_upd_date, Wzb.time_format_ymd),
						curUserIsRead : item.curUserIsRead
					};
					var html = $('#announceTemplate').render(p);
					$("#announceList").append(html);
					if(!item.curUserIsRead){
						hasNotRead = false;
					}
				}
				
				if(arrLength == 0 || hasNotRead == true){//如果没公告或者有还未读的 ，隐藏整列
					$("#announceContainer").hide();
				}else{
					$("#announceContainer").show();
					if(arrLength > 2){
						this.scrolAnnounces();//滚动公告
					}
				}

			}.bind(this)});
			
		}
	},
	
	course : {//课程模块
		
		loadSize : 4,//课程最多显示加载的数目
		/**
		 * 初始化课程相关:
		 * 首面课程栏显示逻辑
			A. 如果当前用户有进行中的课程，那么就是显示过行中的课程。同时在页面上显示“我的课程｜我的必修课｜为你推荐 ｜最新课程” ；
			B. 如果没有进行中的课程则显“我的必修课”。同时在页面上显示“我的必修课｜为你推荐 ｜最新课程” ；
			C、依此类推。如果全部都没有，那就都不要显示了。
		 */
		init : function(){
			
			var couseModule = this;
			
			couseModule.loadMy(function(myCourse){
				
				if(myCourse.rows.length == 0){//如果没有我的课程，则显示我的必修课
					
					$("#myCourseBtn").hide();//隐藏我的课程按钮
					$("#compulsoryCourseBtn").show(); //显示必修课程
					$("#moreCourseBtn a").attr('href', $("#compulsoryCourseBtn a").attr('href')); //更改更多链接
					$("#compulsoryCourseBtn").addClass("active");//选中必修课程按钮
					
					couseModule.loadCompulsory(function(compulsoryCourse){
						
						if(compulsoryCourse.rows.length == 0){//如果没有我的必修课，则显示【为你推荐】课程
							
							$("#compulsoryCourseBtn").hide();//隐藏必修课程按钮
							$("#recommentCourseBtn").show(); //显示推荐
							$("#moreCourseBtn a").attr('href',$("#recommentCourseBtn a").attr('href')); //更改更多链接
							$("#recommentCourseBtn").addClass("active");//选中推荐课程按钮
							
/*							couseModule.loadRecomment(function(recommentCourse){
								
								if(recommentCourse.rows.length == 0){//如果没有推荐课程，则显示最新课程
									
									$("#recommentCourseBtn").hide();//隐藏推荐课程按钮
									$("#newestCourseBtn").show(); //显示最新课程
									$("#moreCourseBtn a").attr('href',$("#newestCourseBtn a").attr('href')); //更改更多链接
									$("#newestCourseBtn").addClass("active");//选中最新课程按钮
									
									couseModule.loadNewest(function(newestCouse){
										
										if(newestCouse.rows.length == 0){//如果没有最新课程，则整个课程区域都隐藏
											$("#courseBtnContainer").hide();
											$("#courseListContainer").hide();
										}else if(newestCouse.rows.length < couseModule.loadSize){//如果不够 size张，则有空图片填充
											couseModule.appendEmptyCourseItem(couseModule.loadSize - newestCouse.rows.length);
											$("#courseBtnContainer").show();
										}else{
											$("#courseBtnContainer").show();
										}
										
									});
									
								}else if(recommentCourse.rows.length < couseModule.loadSize){//如果不够 size张，则有空图片填充
									couseModule.appendEmptyCourseItem(couseModule.loadSize - recommentCourse.rows.length);
									$("#courseBtnContainer").show();
								}else{
									$("#courseBtnContainer").show();
								}
								
							});*/
							
						}else if(compulsoryCourse.rows.length < couseModule.loadSize){//如果不够 size张，则有空图片填充
							couseModule.appendEmptyCourseItem(couseModule.loadSize - compulsoryCourse.rows.length);
							$("#courseBtnContainer").show();
						}else{
							$("#courseBtnContainer").show();
						}
						
					});
					
				}else if(myCourse.rows.length < couseModule.loadSize){//如果不够 size张，则有空图片填充
					couseModule.appendEmptyCourseItem(couseModule.loadSize - myCourse.rows.length);
					$("#courseBtnContainer").show();
				}else{
					$("#courseBtnContainer").show();
				}
				
			}.bind(this));
		},
		//添加 【count】 个空图片占位
		appendEmptyCourseItem : function(count){
			var emptyImg = "<img width='226px' src="+ctx+"'/static/images/z-kecheng.png'/>";
			var html = "<li>"+emptyImg+"</li>";
			for(var i=0;i<count-1;i++){
				$("#courseList").append(html);
			}
			//最后一个需要 margin: 25px 0 0 0;
			$("#courseList").append("<li style='margin: 25px 0 0 0;'>"+emptyImg+"</li>");
		},
		
		//加载课程数据--共有方法
		loadData : function(url,param,callBack){
			var size = this.loadSize;
			$.ajax({url: url , data : param , dataType : 'json' , success:function(data){
				
				callBack(data);
				
			}});
		},
		
		loadMy : function(callback){ //我的课程
			var size = this.loadSize;
			this.loadData(ctx+"/app/course/getMyCourse",{pageSize:size,appStatus:'I'},function(data){
				
				var arrLength = data.rows.length;
				
				for(var i=0;i<arrLength;i++){//拼接加载的条目
					
					var item = data.rows[i];
					
					var p = {
						itm_id : item.item.itm_id,
						encItmId : wbEncrytor().cwnEncrypt(item.item.itm_id),
						app_tkh_id : item.app_tkh_id,
						itm_icon : item.item.itm_icon,
						itm_title : item.item.itm_title,
						app_status : item.app_status,
						app_status_str : getAppStatusStr(item.app_status)
					};
					
					if(item.cov && item.cov.cov_last_acc_datetime){//上次访问时间
						p.cov_last_acc_datetime = Wzb.displayTime(item.cov.cov_last_acc_datetime, Wzb.time_format_ymd);
					}else{
						p.cov_last_acc_datetime = "----";
					}
					
					if(item.cov && item.cov.cov_progress){//进度
						p.cov_progress = item.cov.cov_progress;
					}else if(item.app_tkh_id > 0){
						p.cov_progress = 10; 
					}else{
						p.cov_progress = 0; 
					}
					
					if(size == i+1){
						p.style = "margin: 25px 0 0 0";
					}
					
					var html =  $('#courseTemplate').render(p);
					$("#courseList").append(html);
				}
				
				callback(data);
				
	        });
		},
		
		loadCompulsory : function(callback){//必修课
			var size = this.loadSize;
			this.loadData(ctx+"/app/course/recommendJson",{pageSize:4,isCompulsory:1},function(data){
				
				var arrLength = data.rows.length;
				
				for(var i=0;i<arrLength;i++){//拼接加载的条目
					
					var item = data.rows[i];
					var course = item.item;
					
					var p = {
						itm_id : course.itm_id,
						encItmId : wbEncrytor().cwnEncrypt(course.itm_id),
						itm_icon : course.itm_icon,
						itm_title : course.itm_title,
						itm_publish_timestamp : Wzb.displayTime(course.itm_publish_timestamp, Wzb.time_format_ymd), 
						s_cnt_like_count : (course.snsCount && course.snsCount.s_cnt_like_count) ? course.snsCount.s_cnt_like_count : 0
					};
					
					if(item.cov){//上次访问时间
						p.cov_last_acc_datetime = Wzb.displayTime(item.cov.cov_last_acc_datetime, Wzb.time_format_ymd);
					}else{
						p.cov_last_acc_datetime = "----";
					}
					
					if(item.cov && item.cov.cov_progress){//进度
						p.cov_progress = item.cov.cov_progress;
					}else if(item.app.app_tkh_id > 0){
						p.cov_progress = 10; 
					}else{
						p.cov_progress = 0; 
					}
					
					if(size == i+1){
						p.style = "margin: 25px 0 0 0";
					}
					var html =  $('#compulsoryAndRecommend').render(p);
					$("#courseList").append(html);
				}
				
				callback(data);
			});
		},
		
		loadRecomment : function(callback){//推荐课程
			
			var size = this.loadSize;
			this.loadData(ctx+"/app/course/recommendJson",{pageSize:4},function(data){
				
				var arrLength = data.rows.length;
				
				for(var i=0;i<arrLength;i++){//拼接加载的条目
					
					var course = data.rows[i].item;
					
					var p = {
						itm_id : course.itm_id,
						encItmId : wbEncrytor().cwnEncrypt(course.itm_id),
						itm_icon : course.itm_icon,
						itm_title : course.itm_title,
						itm_publish_timestamp : Wzb.displayTime(course.itm_publish_timestamp, Wzb.time_format_ymd), 
						s_cnt_like_count : (course.snsCount && course.snsCount.s_cnt_like_count) ? course.snsCount.s_cnt_like_count : 0
					};
					
					if(size == i+1){
						p.style = "margin: 25px 0 0 0";
					}
					var html =  $('#compulsoryAndRecommend').render(p);
					$("#courseList").append(html);
				}
				
				callback(data);
			});
		},
		
		loadNewest : function(callback){//最新课程
			var size = this.loadSize;
			var paramsNews = {
	            sortname : 'itm_publish_timestamp',
	            sortorder : 'desc',
	            pageSize : 4
	        };
	        this.loadData(ctx+"/app/rank/course_rank",paramsNews,function(data){
	        	
	        	var arrLength = data.rows.length;
				
				for(var i=0;i<arrLength;i++){//拼接加载的条目
					
					var course = data.rows[i];
					
					var p = {
						itm_id : course.itm_id,
						itm_icon : course.itm_icon,
						encItmId : wbEncrytor().cwnEncrypt(course.itm_id),
						itm_title : course.itm_title,
						app_total : course.app.app_total,
						itm_publish_timestamp : Wzb.displayTime(course.itm_publish_timestamp, Wzb.time_format_ymd)
					};
					
					if(size == i+1){
						p.style = "margin: 25px 0 0 0";
					}
					var html =  $('#newestCourseTemplate').render(p);
					$("#courseList").append(html);
				}
				callback(data);
	        });
		},
		
		loadOpen : function(){//公开课
			
			var size = 4;//加载的数量，最多加载的条数
			
			$.ajax({url: ctx + '/app/home/openJson?pageSize='+size ,dataType : 'json' ,success:function(data){

				var arrLength = data.rows.length;
				
				for(var i=0;i<arrLength;i++){//拼接加载的条目
					var item = data.rows[i];
					var p = $.extend(item,{
						encItmId : wbEncrytor().cwnEncrypt(item.itm_id),
						itm_publish_timestamp : Wzb.displayTime(item.itm_publish_timestamp, Wzb.time_format_ymd), 
						s_cnt_like_count : (item.snsCount && item.snsCount.s_cnt_like_count) ? item.snsCount.s_cnt_like_count : 0
					});
					var html = $('#openTemplate').render(p);
					$("#open_lst").append(html);
				}
				
				for(var i=arrLength;i<size;i++){//如果没有 size 条，则后面显示暂无课程的图标
					var html = "<li><img width='226px' src="+ctx+"'/static/images/z-gongkaike.png'/></li>";
					$("#open_lst").append(html);
				}
				
				if(arrLength == 0){
					$("#open_title").hide();
					$("#open_lst").hide();
				}
				
				courseDoing();

			}});
			
		}
		
	},
	
	topic : {//专题模块
		load : function(){
			$.ajax({url: ctx + '/app/learningmap/getSpecialListForPCIndex',dataType : 'json',success:function(data){
				var arrLength = data.rows.length;
				for(var i=0;i<arrLength;i++){//拼接加载的条目
					var item = data.rows[i];
					var p = {
						abs_img:item.abs_img,
						ust_title:item.ust_title,
						ust_summary:item.ust_summary,
						ust_hits : item.ust_hits,
						ust_id : item.ust_id,
						enc_ust_id : wbEncrytor().cwnEncrypt(item.ust_id)
					};
					if(i % 2 != 0){
						p.style = "margin:25px 0 0 0";
					}
					var html = $('#topicTemplate').render(p);
					$("#topicList").append(html);
				}
				//如果为奇数 条，则后面显示暂无的图标
					if(arrLength % 2 != 0){
						var html = "<li style='margin:25px 0 0 0'><img style='width:480px;height:346px;cursor:default;' src="+ctx+"'/static/images/z-zhuanti.png'/></li>";
						$("#topicList").append(html);
					
				}
				
				if(arrLength == 0){
					$("#topicTitle").hide();
					$("#topicList").hide();
				}

			}});
		}
	},
	
	keyPosition : {//关键岗位模块
		init : function(){//如果没有关键岗位，则不显示关键岗位的图片
			
			$.ajax({url: ctx + '/app/learningmap/positionMapInfoJson', dataType : 'json' ,success:function(data){

				if(!(data && data.count > 0)){
					$("#keyPositionBanner").hide();
				}

			}});
			
		}
	},
	
	gradeMap : {//职级发展学习地图模块
		init : function(){
			$.ajax({url: ctx + '/app/learningmap/gradeMap/count',success:function(count){

				if(!count || count == 0){
					$("#gradeMapBanner").hide();
				}

			}});
		}
	},
	
	instructor : {//讲师模块
		load : function(){
			
			var size = 4;//加载的数量，最多加载的条数
			
			$.ajax({url: ctx + '/app/instr/page?pageSize='+size ,dataType : 'json' ,success:function(data){
				var arrLength = data.rows.length;
				for(var i=0;i<arrLength;i++){//拼接加载的条目
					var item = data.rows[i];
					if(item.iti_expertise_areas!=null&&item.iti_expertise_areas.length>50)
					{
						item.iti_expertise_areas=item.iti_expertise_areas.substring(0,50)+'...';
					}
					var p = {
						iti_ent_id : item.iti_ent_id,
						enc_iti_ent_id : wbEncrytor().cwnEncrypt(item.iti_ent_id),
						iti_img : item.iti_img,
						iti_name : item.iti_name,
						iti_level : item.iti_level,
						iti_expertise_areas : item.iti_expertise_areas,
						iti_score : item.iti_score
					};
					
					//设置讲师的国际化label
					if(p.iti_level == 'J'){
						p.iti_level = fetchLabel("label_core_basic_data_management_5");
					}else if(p.iti_level == 'M'){
						p.iti_level = fetchLabel("label_core_basic_data_management_6");
					}else if(p.iti_level == 'S'){
						p.iti_level = fetchLabel("label_core_basic_data_management_7");
					}else if(p.iti_level == 'D'){
						p.iti_level = fetchLabel("label_core_basic_data_management_8");
					}
					
					//设置样式颜色
					if(i == 0){
						p.color = "green";
					}else if(i == 1){
						p.color = "pink";
					}else if(i == 2){
						p.color = "blue";
					}else if(i == 3){
						p.color = "gold";
					}
					
					var html = $('#instructorTemplate').render(p);
					$("#instructorList").append(html);
					//讲师星星数
					$("#instructorList li .xyd-tool-pingfen").each(function(index,data){
						if(index==i){
							var html = "";
							if(item.iti_score==null){item.iti_score=0}
							for(var f=1;f<item.iti_score+1;f++)
							{
								if(item.iti_score+1>f+0.5)
								{
									html+="<i class='start-g start-g-full'></i>";
								}
							}
							for(var f=1;f<=5-item.iti_score+0.5;f++)
							{
									html+="<i class='start-g start-g'></i>";
							}
							$(this).append(html);
						}
					})
				}
				
				for(var i=arrLength;i<size;i++){//如果没有 size 条，则后面显示暂无的图标
					var html = "<li><img src="+ctx+"'/static/images/z-jiangshi.png'/></li>";
					$("#instructorList").append(html);
				}
				
				if(arrLength > 0){
					$("#instructorInfo").show();
					$("#instructorTitle").show();
				}

			}});
			
		}
	}
	
};
function announceContainer_hide()
	{
		    $("#announceContainer").animate({"marginTop":"-40px"},1000);
	}