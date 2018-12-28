$(function(){
	
	//初始化页面权限，无权限的链接点击无效
	adminIndexModule.auth.init();
	
	//初始化快速通道常用功能
	adminIndexModule.favoriteFunction.init();
});

var adminIndexModule = (function(){
	
	var module = {};
	
	module.favoriteFunction = {//常用功能
		init : function(){
			
			if($("#addFavoriteFunction").length <= 0){//如果沒有快速功能入口，直接返回
				return;
			}
			
			//加载添加的常用功能
			$.ajax({
				url : contextPath + '/app/admin/home/favoriteFunJson',
				dataType : 'JSON',
				type : 'POST',
				success : function(result){
					var container = $("#favorite_funs");
					container.empty();
					$.each(result, function(i,val){
						if(val.acFunction.ftn_ext_id!='FTN_AMD_EXAM_MGT'){
							var label = cwn.getLabel(val.acFunction.ftn_ext_id);
							val.ftn_name = label;
							val.icon = this.functionIcon[val.acFunction.ftn_ext_id];
							val.url = "javascript:wb_utils_nav_go('"+val.acFunction.ftn_ext_id+"',"+val.uff_usr_ent_id+",'gb')";
							container.append($("#fun_item").render(val));
						}

					}.bind(this));
				}.bind(this)
			});
			
			var btnOk = fetchLabel("button_ok");
			var btnCancel = fetchLabel("button_cancel");
			var addFastTrack = fetchLabel("lab_index_add_fast_track");
			//绑定添加常用功能的点击事件
			$("#addFavoriteFunction").unbind("click");
			$("#addFavoriteFunction").bind("click",function(){
				layer.load(0, {shade: [0.1,'#fff']});
				$.ajax({
					url : contextPath + '/app/admin/home/markFavoriteFunctionJson',
					dataType : 'JSON',
					type : 'post',
					success : function(result){
						var htmlTemplate = "<div style='padding:20px;'>{{templateContent}}</div>";
						$.each(result, function(i,val){
							var label = cwn.getLabel(val.ftn_ext_id);
							val.ftn_name = label;
							if(val.subFunctions.length == 0){//如果没有子功能，则子功能处，显示父功能的label，如考试管理
								val.subFunctions.push(val);
							}
							$.each(val.subFunctions, function(j, sval){
								var lab = cwn.getLabel(sval.ftn_ext_id);
								sval.ftn_name = lab;
								sval.icon = module.favoriteFunction.functionIcon[sval.ftn_ext_id];
								if(sval.uff_fun_id > 0){//如果子功能被选中，则父功能也选中
									val.checked = true;
								}
							});
						});
						htmlTemplate = htmlTemplate.replace("{{templateContent}}",$("#favorite_fun").render(result));
						layer.open({
			                type: 1,
			                area: ['470px', '600px'], //宽高
			                title : addFastTrack,
			                content: htmlTemplate,
			                btn: [btnOk, btnCancel],
			                yes: function(index, layero){
			                	var ids = [];
			            		$(".wzb-box-pupup a[name='fun'].current").each(function(){ 
			            			ids.push($(this).attr("value"));
			                    });
			            		$.ajax({
			            			url : contextPath + '/app/admin/home/addMtpFavorite',
			            			dataType : 'JSON',
			            			data : {
			            				ids : ids.join(",")
			            			},
			            			type : 'post',
			            			success : function(result){
			            				layer.close(index);
			            				module.favoriteFunction.init();
			            			}
			            		});
			                }
						});
						
						$(".wzb-box-pupup .wzb-box-1-4").unbind("click");
						$(".wzb-box-pupup .wzb-box-1-4").bind("click",function(){
							$(this).toggleClass("current");
							if($(this).hasClass("current")){
								$(this).parent(".wzb-box-pubg").prev().children("em").addClass("quan-yes");
							}else{
								var sb = $(this).siblings();
								var i = 0;
								for(i = 0;i<sb.length;i++){
									var item = sb[i];
									if($(item).hasClass("current")){
										break;
									}
								}
								
								if(i == sb.length){
									$(this).parent(".wzb-box-pubg").prev().children("em").removeClass("quan-yes");
								}
								
							}
						});
						
						$(".wzb-box-pupup h4").unbind("click");
						$(".wzb-box-pupup h4").bind("click",function(){
							$(this).children("em").toggleClass("quan-yes");
							if($(this).children("em").hasClass("quan-yes")){
								$(this).next(".wzb-box-pubg").children().addClass("current");
							}else{
								$(this).next(".wzb-box-pubg").children().removeClass("current");
							}
						});
						layer.closeAll('loading');//close loading
					}
				});
				
			});
		},
		functionIcon : {//功能图标
			'FTN_AMD_SYS_MSG_LIST' : "Ggao",// 公告
			'FTN_AMD_MSG_MAIN' : "Gggli",// 公告管理
			'FTN_AMD_ARTICLE_MAIN' : "Zxgli",// 资讯管理
			'FTN_AMD_VOTING_MAIN' : "Tpiao",// 投票
			'FTN_AMD_EVN_MAIN' : "Ggtcwjuan",// 公共调查问卷
			'FTN_AMD_PLAN_CARRY_OUT' : "Jhsshi",// 计划实施
			'FTN_AMD_YEAR_PALN' : "Ndjhua",// 年度计划
			'FTN_AMD_MAKEUP_PLAN' : "Bwjhua",// 编外计划
			'FTN_AMD_YEAR_PLAN_APPR' : "Ndjhspi",// 年度计划审批
			'FTN_AMD_MAKEUP_PLAN_APPR' : "Bwjhspi",// 编外计划审批
			'FTN_AMD_YEAR_SETTING' : "Ndszhi",// 年度设置
			'FTN_AMD_SPECIALTOPIC_MAIN' : "Ztpxun",// 专题培训
			'FTN_AMD_POSITION_MAP_MAIN' : "Gjgwxxdtgli",// 关键岗位学习地图管理
			'FTN_AMD_PROFESSION_MAIN' : "Zjxxdtgli",// 职级学习地图管理
			'FTN_AMD_ITM_COS_MAIN' : "Kcgli",// 课程管理
			'FTN_AMD_CERTIFICATE_MAIN' : "Zsgli",// 证书管理
			'FTN_AMD_CAT_MAIN' : "Kcmlgli",// 课程目录管理
			'FTN_AMD_OPEN_COS_MAIN' : "Gkkgli",// 公开课管理
			'FTN_AMD_RES_MAIN' : "Zygli",// 资源管理
			'FTN_AMD_COS_EVN_MAIN' : "Kcpgwjgli",// 课程评估问卷管理
			'FTN_AMD_EXAM_MGT' : "Ksgli",// 考试管理
			'FTN_AMD_Q_AND_A_VIEW' : "Wda",// 问答
			'FTN_AMD_Q_AND_A_MAIN' : "Wdgli",// 问答管理
			'FTN_AMD_SNS_GROUP_MAIN' : "Qzgli",// 群组管理
			'FTN_AMD_SNS_GROUP_VIEW' : "Qzu",// 群组
			'FTN_AMD_KNOWLEDGE_STOREGE' : "Zsku",// 知识库
			'FTN_AMD_KNOWLEDEG_CATALOG' : "Zsmlu",// 知识目录
			'FTN_AMD_KNOWLEDEG_TAG' : "Zsbqian",// 知识标签
			'FTN_AMD_KNOWLEDEG_APP' : "Zsspi",// 知识审批
			'FTN_AMD_TRAINING_REPORT_MGT' : "Pxbbiao",// 培训报表
			'FTN_AMD_USR_INFO' : "Yhxxi",// 用户信息
			'FTN_AMD_POSITION_MAIN' : "Gwgli",// 岗位管理
			'FTN_AMD_USR_REGIETER_APP' : "Zcspi",// 注册审批
			'FTN_AMD_USR_ACTIVATE' : "Yhjhuo",// 用户激活
			'FTN_AMD_GRADE_MAIN' : "Zjgli",// 职级管理
			'FTN_AMD_INT_INSTRUCTOR_MAIN' : "Nbjsgli",// 内部讲师管理
			'FTN_AMD_EXT_INSTRUCTOR_MAIN' : "Wbjsgli",// 外部讲师管理
			'FTN_AMD_SUPPLIER_MAIN' : "Gysgli",// 供应商管理
			'FTN_AMD_MESSAGE_TEMPLATE_MAIN' : "Yjmbgli",// 邮件模板管理
			'FTN_AMD_CREDIT_SETTING_MAIN' : "Yhxxi",// 积分管理
			'FTN_AMD_FACILITY_BOOK_CREATE' : "Xzyding",// 新增预定
			'FTN_AMD_FACILITY_BOOK_CALENDAR' : "Ydrli",// 预定日历
			'FTN_AMD_FACILITY_BOOK_HISTORY' : "Ydjlu",// 预订记录
			'FTN_AMD_FACILITY_INFO' : "Ssxxi",// 设施信息
			'FTN_AMD_POSTER_MAIN' : "Yhxxi",// PC样式管理
			'FTN_AMD_MOBILE_POSTER_MAIN' : "Ydgli",// 移动管理
			'FTN_AMD_TC_MAIN' : "Pxzxgli",// 培训中心管理
			'FTN_AMD_LIVE_MAIN' : "Zhibo",// 直播
			'FTN_AMD_CPT_D_LIST' : "Cptdgli",//CPTD牌照管理
			'FTN_AMD_CPT_D_LICENSE_LIST' : 'Cptdpzzcgli',//CPTD牌照注册管理
			'FTN_AMD_CPT_D_NOTE' : 'Cptdbbbzwhu',//CPT/D 报表备注维护
			'FTN_AMD_CPT_D_IMPORT_AWARDED_HOURS' : 'Dryhhdcptdsshu'//导入用户获得CPT/D时数
				
		}
	};
	
	module.auth = {//页面权限
		init : function(){//初始化事件，如果没有权限，则对应管理功能按钮链接点击无效
			/**
			 * 屏蔽原生点击事件
			 * 禁掉功能链接和滑动效果，后面有权限的则放开
			 */
			$("[auth]").each(function(index,item){
				item.onclick = null;
				$(item).addClass("off-all");
			});
			$("[auth]").click(function(event){
				event.preventDefault();
			});
			
			//请求用户权限，如果有权限，则放开对应的按钮链接
			$.ajax({
				url : contextPath + '/app/admin/role/getCurrentRoleFunctions',
				dataType : 'JSON',
				type : 'post',
				success : function(result){
					if(!result){
						return;
					}
					
					var executeAction = function(target){
						var href = target.attr("href");
						if(href){
							window.location.href = href;
						}else{
							
							var clickF = target.attr("onclick");
							if(clickF){
								eval(clickF);
							}
						}
					};
					
					for(var i=0;i<result.length;i++){
						var item = result[i];
						
						//放开用户有权限的跳转链接
						$("[auth='"+item.ftn_ext_id+"']").removeClass("off-all");
						$("[auth='"+item.ftn_ext_id+"']").unbind("click");
						$("[auth='"+item.ftn_ext_id+"']").bind("click",function(){
							executeAction($(this));
						});
						$.each(item.subFunctions, function(j, sval){
							$("[auth='"+sval.ftn_ext_id+"']").removeClass("off-all");
							$("[auth='"+sval.ftn_ext_id+"']").unbind("click");
							$("[auth='"+sval.ftn_ext_id+"']").bind("click",function(){
								executeAction($(this));
							});
						});
					}
				}
			});
		}	
	};
	
	return module;
	
})();
