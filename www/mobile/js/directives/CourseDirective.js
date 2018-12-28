//先修条件
var prep_requeid = 
	'<div class="panel-list-cont-11" ng-if="item.resources.mod.pre_res_title">'+            
    	'<div class="panel-add-04">{{\'mod_prep\' | translate}}</div>'+	//先修模块
    	'<div class="panel-add-05">{{item.resources.mod.pre_res_title}}</div>'+
    '</div>';
//简介
var desc = 
	'<div ng-if="item.resources.res_desc" class="panel-list-cont-11 panel-list-cont-11-no">'+   
		'<div class="panel-add-04">{{\'desc\' | translate}}</div>'+ 	//简介
		'<div class="panel-add-05">{{item.resources.res_desc}}</div>'+ 
	'</div>'; 
var moduleDetail = 
//动态测验，静态测验
'<div ng-if="item.resources.res_subtype==\'TST\' || item.resources.res_subtype==\'DXT\'">'+
	prep_requeid +
	'<div class="panel-list-cont-11" ng-class="{\'panel-list-cont-11-no\':item.resources.mod.pre_res_title}">'+
		'<div class="panel-title-17"><div class="panel-add-06 clearfix"><em class="panel-num-2">{{item.resources.mod.mod_max_score}}</em>{{ \'mod_full_score\' | translate}}</div></div>'+	// 满分
		'<div class="panel-title-17"><div class="panel-add-06 clearfix"><em class="panel-num-2">{{item.resources.mod.mod_pass_score}}%</em>{{ \'mod_pass_rate\' | translate}}</div></div>'+	// 合格率
		'<div class="panel-title-17"><div class="panel-add-06 clearfix"><em class="panel-num-2">{{item.resources.res_duration == -1? "": ""+item.resources.res_duration | translate }}{{item.resources.res_duration == -1? \'lab_unlimited_time\': \'time_minute\' | translate }}</em>{{\'mod_time_limit\' | translate}}</div></div>'+
		'<div class="panel-title-17"><div class="panel-add-06 clearfix"><em class="panel-num-2">{{item.resources.mod.mod_max_usr_attempt == 0 ? \'mod_unlimit\' : ""+item.resources.mod.mod_max_usr_attempt | translate}}</em>{{\'mod_test_times\' | translate}}</div></div>'+	//测试次数限制		
		//'<div class="panel-title-17" ng-show="item.resources.pgr_attempt_nbr >0 && item.resources.pgr_status== \'NOT GRADED\' && item.resources.mov.mov_status"><div class="panel-add-06 clearfix"><em class="panel-num-2">{{\'cos_app_S\' | translate}}</em>{{ \'cos_module_status\' | translate}}</div></div>'+	// 状态
		//'<div class="panel-title-17" ng-show="!(item.resources.pgr_attempt_nbr >0 && item.resources.pgr_status == \'NOT GRADED\') && item.resources.mov.mov_status"><div class="panel-add-06 clearfix"><em class="panel-num-2">{{(item.resources.mov.mov_status == \'F\') ? (\'cos_app_flunk\'| translate) : (item.resources.mov.mov_status == \'P\') ? (\'cos_app_passed\'| translate) : (\'cos_app_\'+item.resources.mov.mov_status | translate)}}</em>{{ \'cos_module_status\' | translate}}</div></div>'+	// 状态
		
		'<div class="panel-title-17" ng-show="item.resources.mov.mov_score >-1"><div class="panel-add-06 clearfix"><em class="panel-num-2">{{item.resources.mov.mov_score}}</em>{{ \'mod_best_score\' | translate}}</div></div>'+	//最佳分数
		'<div class="panel-title-17"><div class="panel-add-06 clearfix"><em class="panel-num-2">{{item.resources.mov.mov_total_attempt ? item.resources.mov.mov_total_attempt:0}}</em>{{ \'mod_attempt_limit\' | translate}}</div></div>'+	// 尝试次数

		'<div class="panel-title-17"><div class="panel-add-06 clearfix" ng-if="item.resources.mov"><em class="panel-num-2">{{item.resources.mov.mov_last_acc_datetime}} </em>{{ \'mod_last_access_time\' | translate}}</div></div>'+	//最近访问
    	'<div class="panel-title-16"><div class="panel-add-06 clearfix">'+
			'<div class="cont-info-right">'+
				'<a class="xuan-info-down" ng-if=" item.resources.mov && item.resources.mov.mov_total_attempt > 0 && item.resources.submit_num && item.resources.submit_num > 0" href="{{{url:\'../module/tst_score.html\',itmId:itmId,tkhId:tkhId,modId:item.id,isGoDirectly:true} | appUrl}}" >{{typeKinds == \'cos\' ? \'exam_report\' : \'exam_report\' | translate}}</a>'+ // 学习报告
			'</div>'+
		'</div></div>'+
	'</div>'+
	desc +
'</div>'+
//作业item.attempt_nbr > 0 && item.aicc_data.pgr_status == 'NOT GRADED' && item.subtype == 'TST' && item.subtype == 'ASS
'<div ng-if="item.resources.res_subtype==\'ASS\'">'+
	prep_requeid +
	'<div class="panel-list-cont-11">'+
		'<div class="panel-title-17"><div class="panel-add-06 clearfix"><em class="panel-num-2">{{\'mod_\' + item.resources.res_subtype | translate}}</em>{{ \'mod_type\' | translate}}</div></div>'+	//模块类型
		'<div class="panel-title-17"><div class="panel-add-06 clearfix"><em class="panel-num-2">{{item.resources.mod.mod_eff_start_datetime}}<br ng-show="item.resources.mod.mod_eff_end_datetime != \'9999-12-31 23:59:59\'"/> {{\'time_to\' | translate}} <br ng-show="item.resources.mod.mod_eff_end_datetime != \'9999-12-31 23:59:59\'"/>{{item.resources.mod.mod_eff_end_datetime == \'9999-12-31 23:59:59\'? \'global_unlimit\': item.resources.mod.mod_eff_end_datetime | translate}}</em>{{ \'mod_rdg_duration\' | translate}}</div></div>'+	//提交时限
		
		'<div class="panel-title-17"><div class="panel-add-06 clearfix" ng-if="item.resources.mov"><em class="panel-num-2">{{item.resources.mov.mov_last_acc_datetime}} </em>{{ \'mod_last_access_time\' | translate}}</div></div>'+	//最后访问时间
		
		'<div class="panel-title-17" ng-show="item.resources.pgr_attempt_nbr >0 && item.resources.pgr_status== \'NOT GRADED\' && item.resources.mov.mov_status"><div class="panel-add-06 clearfix"><em class="panel-num-2">{{\'cos_app_S\' | translate}}</em>{{ \'cos_module_status\' | translate}}</div></div>'+	// 状态
		'<div class="panel-title-17" ng-show="!(item.resources.pgr_attempt_nbr >0 && item.resources.pgr_status== \'NOT GRADED\') && item.resources.mov.mov_status"><div class="panel-add-06 clearfix"><em class="panel-num-2">{{(item.resources.mov.mov_status == \'F\') ? (\'cos_app_flunk\'| translate) : (item.resources.mov.mov_status == \'P\') ? (\'cos_app_passed\'| translate) : (\'cos_app_\'+item.resources.mov.mov_status | translate)}}</em>{{ \'cos_module_status\' | translate}}</div></div>'+	// 状态

//		'<div class="panel-title-17"><div class="panel-add-06 clearfix"><em class="panel-num-2">{{item.resources.res_desc?item.resources.res_desc:\'global_null\' | translate}}</em>{{\'mod_desc\' | translate}}</div></div>'+	//简介
    	'<div class="panel-title-16" ><div class="panel-add-06 clearfix">'+
			'<div class="cont-info-right">'+
//				'<a class="xuan-info-down" ng-if="item.appOK && rcov.cov_status == \'I\' && !(item.resources.pgr_attempt_nbr > 0) && (item.resources.mov.mov_score == \'\' || item.resources.mov.mov_status !=\'P\') && timeValide && curDate >= item.resources.mod.mod_eff_start_datetime && curDate <= item.resources.mod.mod_eff_end_datetime && (!item.resources.mod.ass_due_datetime || curDate <= item.resources.mod.ass_due_datetime) && (item.resources.mod && (item.resources.mod.pre_mod_had_completed || item.resources.mod.pre_mod_had_completed == undefined || item.resources.mod.pre_res_title == undefined))" href="{{{app:app,url:\'../module/ass_submit1.html\',itmId:itmId,tkhId:tkhId,modId:item.id} | assSubmitUrl}}" >{{\'mod_submit_ass\' | translate}}</a>'+ // 提交作业
//				'<a class="xuan-info-down" ng-if="curDate >= item.resources.mod.mod_eff_start_datetime && curDate <= item.resources.mod.mod_eff_end_datetime && (item.resources.mov != null && item.resources.mov != \'\') && item.resources.mov.mov_total_attempt > 0 && (item.resources.submit_num != \'\' && item.resources.submit_num > 0)"  href="{{{url:\'../module/ass_score.html\',itmId:itmId,tkhId:tkhId,modId:item.id} | appUrl}}" >{{\'mod_score\' | translate}}</a>'+ // 成绩
			'</div>'+
		'</div></div>'+
	'</div>'+
	desc +
'</div>'+
//调查问卷
'<div ng-if="item.resources.res_subtype==\'SVY\'">'+
	prep_requeid +
	'<div class="panel-list-cont-11">'+
		'<div class="panel-title-17"><div class="panel-add-06 clearfix"><em class="panel-num-2">{{\'mod_\' + item.resources.res_subtype | translate}}</em>{{ \'mod_type\' | translate}}</div></div>'+	//模块类型

		'<div class="panel-title-17"><div class="panel-add-06 clearfix"><em class="panel-num-2">{{item.resources.mod.mod_eff_start_datetime}}<br ng-show="item.resources.mod.mod_eff_end_datetime != \'9999-12-31 23:59:59\'"/> {{\'time_to\' | translate}} <br ng-show="item.resources.mod.mod_eff_end_datetime != \'9999-12-31 23:59:59\'"/>{{item.resources.mod.mod_eff_end_datetime == \'9999-12-31 23:59:59\'? \'global_unlimit\': item.resources.mod.mod_eff_end_datetime | translate}}</em>{{ \'mod_rdg_duration\' | translate}}</div></div>'+	//提交时限
		'<div class="panel-title-17" ng-show="item.resources.mov.mov_status"><div class="panel-add-06 clearfix"><em class="panel-num-2">{{\'cos_app_\'+item.resources.mov.mov_status | translate}}</em>{{ \'cos_module_status\' | translate}}</div></div>'+	// 状态

		'<div class="panel-title-17"><div class="panel-add-06 clearfix" ng-if="item.resources.mov"><em class="panel-num-2">{{item.resources.mov.mov_last_acc_datetime}} </em>{{ \'mod_submit_time\' | translate}}</div></div>'+	//提交时间
		
	
//		'<div class="panel-title-17"><div class="panel-add-06 clearfix"><em class="panel-num-2">{{item.resources.res_desc?item.resources.res_desc:\'global_null\' | translate}}</em>{{\'mod_desc\' | translate}}</div></div>'+	//简介
	'</div>' +
	desc +
'</div>'+
//参考, GLO RDG
'<div ng-if="item.resources.res_subtype==\'REF\' || item.resources.res_subtype==\'GLO\' || item.resources.res_subtype==\'RDG\'">'+
	prep_requeid +

	'<div class="panel-list-cont-11">'+
	
		'<div class="panel-title-17"><div class="panel-add-06 clearfix"><em class="panel-num-2">{{\'mod_\' + item.resources.res_subtype | translate}}</em>{{ \'mod_type\' | translate}}</div></div>'+	//模块类型

//		'<div class="panel-title-17"><div class="panel-add-06 clearfix"><em class="panel-num-2">{{item.resources.mod.mod_eff_start_datetime}} {{\'time_to\' | translate}} {{item.resources.mod.mod_eff_end_datetime == \'9999-12-31 23:59:59\'? \'global_unlimit\': item.resources.mod.mod_eff_end_datetime | translate}}</em>{{ \'mod_rdg_duration\' | translate}}</div></div>'+	//提交时限

//		'<div class="panel-title-17"><div class="panel-add-06 clearfix"><em class="panel-num-2">{{item.resources.mod.mod_eff_start_datetime}}<br ng-show="item.resources.mod.mod_eff_end_datetime != \'9999-12-31 23:59:59\'"/> {{\'time_to\' | translate}} <br ng-show="item.resources.mod.mod_eff_end_datetime != \'9999-12-31 23:59:59\'"/>{{item.resources.mod.mod_eff_end_datetime == \'9999-12-31 23:59:59\'? \'global_unlimit\': item.resources.mod.mod_eff_end_datetime | translate}}</em>{{ \'mod_rdg_duration\' | translate}}</div></div>'+	//提交时限


		'<div class="panel-title-17" ng-show="item.resources.mov.mov_total_time >0"><div class="panel-add-06 clearfix"><em class="panel-num-2">{{item.resources.mov.mov_total_time_str}}</em>{{ \'mod_study_along_time\' | translate}}</div></div>'+	//学习时长
		'<div class="panel-title-17" ng-show="item.resources.mov.mov_status"><div class="panel-add-06 clearfix"><em class="panel-num-2">{{\'cos_app_\'+item.resources.mov.mov_status+\'_V\' | translate}}</em>{{ \'cos_module_status\' | translate}}</div></div>'+	// 状态

		'<div class="panel-title-17"><div class="panel-add-06 clearfix" ng-if="item.resources.mov"><em class="panel-num-2">{{item.resources.mov.mov_last_acc_datetime}} </em>{{ \'mod_last_access_time\' | translate}}</div></div>'+	//最后访问时间

//		'<div class="panel-title-17"><div class="panel-add-06 clearfix"><em class="panel-num-2">{{item.resources.res_desc?item.resources.res_desc:\'global_null\' | translate}}</em>{{\'mod_desc\' | translate}}</div></div>'+	//简介
    	'<div class="panel-title-16" ng-if="rcov.cov_status!=\'I\' && isApp" ><div class="panel-add-06 clearfix">'+
			'<div class="cont-info-right">'+
				'<a class="xuan-info-down" ng-if="appInd  && timeValide && item.resources.res_subtype==\'VOD\' && item.resources.mod.mod_download_ind == 1" href="javascript:download({{tkhId}},{{resId}},\'{{itmTitle}}\',{{item.resources}})" >{{\'personal_download\' | translate}}</a>'+	//下载
				'<a class="xuan-info-down" ng-if=" (item.resources.res_subtype==\'REF\' || item.resources.res_subtype==\'RDG\' || item.resources.res_subtype==\'GLO\') && !(rcov.cov_status == \'I\') && timeValide && item.resources.mod.mod_eff_start_datetime <= curDate && item.resources.mod.mod_eff_end_datetime >= curDate"  href="{{ {app:isApp, timeValide:timeValide, userEntId:userEntId, tkhId:tkhId, itmId:itmId, resId:resId, modId:item.id, item:item, previewInd:true} | moduleUrl}}" >{{\'cos_review\' | translate}}</a>'+ // 回顾
			'</div>'+
		'</div></div>'+
	'</div>' +
	desc +
'</div>'+
//视屏点播
'<div ng-if="item.resources.res_subtype==\'VOD\'">'+
prep_requeid +

'<div class="panel-list-cont-11">'+

	'<div class="panel-title-17"><div class="panel-add-06 clearfix"><em class="panel-num-2">{{\'mod_\' + item.resources.res_subtype | translate}}</em>{{ \'mod_type\' | translate}}</div></div>'+	//模块类型

	'<div class="panel-title-17" ng-show="item.resources.mov.mov_total_time >0"><div class="panel-add-06 clearfix"><em class="panel-num-2">{{item.resources.mov.mov_total_time_str}}</em>{{ \'mod_study_along_time\' | translate}}</div></div>'+	//学习时长
	'<div  class="panel-title-17" ng-if="item.resources.mov.mov_status==\'I\'" ng-show="item.resources.mov.mov_status"><div class="panel-add-06 clearfix"><em class="panel-num-2">{{\'cos_app_\'+item.resources.mov.mov_status+\'_V\' | translate}}</em>{{ \'cos_module_status\' | translate}}</div></div>'+	// 状态
	'<div  class="panel-title-17" ng-if="item.resources.mov.mov_status==\'B\'" ng-show="item.resources.mov.mov_status"><div class="panel-add-06 clearfix"><em class="panel-num-2">{{\'cos_app_I_V\' | translate}}</em>{{ \'cos_module_status\' | translate}}</div></div>'+	// 状态

	'<div  class="panel-title-17" ng-if="item.resources.mov.mov_status ==\'C\'" ng-show="item.resources.mov.mov_status"><div class="panel-add-06 clearfix"><em class="panel-num-2">{{\'cos_app_\'+item.resources.mov.mov_status+\'\' | translate}}</em>{{ \'cos_module_status\' | translate}}</div></div>'+	// 状态
	
	'<div class="panel-title-17"><div class="panel-add-06 clearfix" ng-if="item.resources.mov"><em class="panel-num-2">{{item.resources.mov.mov_last_acc_datetime}} </em>{{ \'mod_last_access_time\' | translate}}</div></div>'+	//最后访问时间

//	'<div class="panel-title-17"><div class="panel-add-06 clearfix"><em class="panel-num-2">{{item.resources.res_desc?item.resources.res_desc:\'global_null\' | translate}}</em>{{\'mod_desc\' | translate}}</div></div>'+	//简介
	'<div class="panel-title-16" ng-if="isApp && timeValide"><div class="panel-add-06 clearfix">'+
		'<div class="cont-info-right">'+
			'<a class="xuan-info-down" ng-if="appInd && item.resources.mod.mod_download_ind == 1" href="javascript:download({{tkhId}},{{resId}},\'{{itmTitle}}\',{{item.resources}})" >{{\'personal_download\' | translate}}</a>'+	//下载
			'<a class="xuan-info-down" ng-if="!(rcov.cov_status == \'I\') && timeValide && item.resources.mod.mod_eff_start_datetime <= curDate && item.resources.mod.mod_eff_end_datetime >= curDate" href="{{ {app:isApp, timeValide:timeValide, userEntId:userEntId, tkhId:tkhId, itmId:itmId, resId:resId, modId:item.id, item:item, previewInd:true} | moduleUrl}}" >{{\'cos_review\' | translate}}</a>'+ // 回顾
		'</div>'+
	'</div></div>'+
'</div>' +
desc +
'</div>'+
//AICC课件  发不到手机端已移除
'<div ng-if="item.resources.res_subtype==\'AICC_AU\'">'+
	prep_requeid +
	'<div class="panel-list-cont-11">'+
		'<div class="panel-title-17"><div class="panel-add-06 clearfix"><em class="panel-num-2">{{\'mod_\' + item.resources.res_subtype | translate}}</em>{{ \'mod_type\' | translate}}</div></div>'+	//模块类型
		'<div class="panel-title-17"><div class="panel-add-06 clearfix"><em class="panel-num-2">{{item.resources.mod.mod_max_score}}</em>{{\'mod_full_score\' | translate}}</div></div>'+	//满分
		'<div class="panel-title-17"><div class="panel-add-06 clearfix"><em class="panel-num-2">{{item.resources.mod.mod_pass_score}}</em>{{\'mod_passing_score\' | translate}}</div></div>'+	//合格分数
		'<div class="panel-title-17" ng-show="item.resources.mov.mov_score >0"><div class="panel-add-06 clearfix"><em class="panel-num-2">{{item.resources.mov.mov_score}}</em>{{ \'mod_best_score\' | translate}}</div></div>'+	//最佳分数
		'<div class="panel-title-17"><div class="panel-add-06 clearfix" ng-if="item.resources.mov"><em class="panel-num-2">{{item.resources.mov.mov_last_acc_datetime}} </em>{{ \'mod_last_access_time\' | translate}}</div></div>'+	//最近访问
		'<div class="panel-title-17" ng-show="item.resources.mov.mov_total_time >999"><div class="panel-add-06 clearfix"><em class="panel-num-2">{{item.resources.mov.mov_total_time_str}}</em>{{ \'mod_study_along_time\' | translate}}</div></div>'+	//学习时长
		'<div class="panel-title-17" ng-show="item.resources.mov.mov_status"><div class="panel-add-06 clearfix"><em class="panel-num-2">{{\'cos_app_\'+item.resources.mov.mov_status | translate}}</em>{{ \'cos_module_status\' | translate}}</div></div>'+	// 状态
		'<div ng-if="!item.isNotCompleted && item.isNoPremise && isApp">'+
        	'<div class="panel-title-16"><div class="panel-add-06 clearfix">'+
				'<div class="cont-info-right">'+
					'<a class="xuan-info-down" ng-if="item.resources.mov" href="{{{url:\'../module/aicc_report.html\',itmId:itmId,tkhId:tkhId,modId:item.id} | appUrl}}" >{{typeKinds == \'cos\' ? \'mod_report\' : \'exam_report\' | translate}}</a>'+ // 学习报告
					'<a class="xuan-info-down" ng-if=" timeValide" href="javascript:;" ng-click="preview_aicc_au(item.resources.res_src_link)" >{{\'cos_review\' | translate}}</a>'+
				'</div>'+
			'</div></div>'+
		'</div>'+
	'</div>'+
	desc +
'</div>'+
//scorm课件
'<div ng-if="item.resources.res_subtype==\'SCO\'">'+
	prep_requeid +
	'<div class="panel-list-cont-11">'+
	'<div class="panel-title-17"><div class="panel-add-06 clearfix"><em class="panel-num-2">{{\'mod_\' + item.resources.res_subtype | translate}}</em>{{ \'mod_type\' | translate}}</div></div>'+	//模块类型
	'<div class="panel-title-17"><div class="panel-add-06 clearfix"><em class="panel-num-2">{{item.resources.mod.mod_max_score}}</em>{{\'mod_full_score\' | translate}}</div></div>'+	//满分
	'<div class="panel-title-17"><div class="panel-add-06 clearfix"><em class="panel-num-2">{{item.resources.mod.mod_pass_score}}</em>{{\'mod_passing_score\' | translate}}</div></div>'+	//合格分数
	'<div class="panel-title-17" ng-show="item.resources.mov.mov_score >0"><div class="panel-add-06 clearfix"><em class="panel-num-2">{{item.resources.mov.mov_score}}</em>{{ \'mod_best_score\' | translate}}</div></div>'+	//最佳分数
	'<div class="panel-title-17"><div class="panel-add-06 clearfix" ng-if="item.resources.mov"><em class="panel-num-2">{{item.resources.mov.mov_last_acc_datetime}} </em>{{ \'mod_last_access_time\' | translate}}</div></div>'+	//最近访问
	'<div class="panel-title-17" ng-show="item.resources.mov.mov_total_time >999"><div class="panel-add-06 clearfix"><em class="panel-num-2">{{item.resources.mov.mov_total_time_str}}</em>{{ \'mod_study_along_time\' | translate}}</div></div>'+	//学习时长
	'<div class="panel-title-17" ng-show="item.resources.mov.mov_status"><div class="panel-add-06 clearfix"><em class="panel-num-2">{{\'cos_app_\'+item.resources.mov.mov_status | translate}}</em>{{ \'cos_module_status\' | translate}}</div></div>'+	// 状态

//		'<div class="panel-title-17"><div class="panel-add-06 clearfix"><em class="panel-num-2">{{item.resources.res_desc?item.resources.res_desc:\'--\'}}</em>{{\'mod_desc\' | translate}}</div></div>'+	//简介
    	'<div class="panel-title-16" ng-if="isApp && rcov.cov_status!=\'I\'"><div class="panel-add-06 clearfix">'+
			'<div class="cont-info-right">'+
				'<a class="xuan-info-down" ng-if="!(rcov.cov_status == \'I\') && timeValide && item.resources.mod.mod_eff_start_datetime <= curDate && item.resources.mod.mod_eff_end_datetime >= curDate" href="{{ {item:item, previewInd:true} | moduleUrl}}">{{\'cos_review\' | translate}}</a>'+	//回顾
			'</div>'+
		'</div></div>'+
	'</div>'+
	desc +
'</div>';


appModule.directive('dirModule', function(){
   var html =
	   	  '<div>'+
			  '<div ng-repeat="dir in dircontents" ng-show="dir.children.length>0" class="dircontent">'+
			  '<p class="slide-tit xuan-tit"><span class="slide-break slide-plus">{{dir.title}}</span></p>'+
			  '<div class="slide-info xuan-info">'+
		      '<div class="panel-list-cont-10">'+

			  '<span ng-repeat="item in dir.children" ng-show="item.resources.res_subtype != \'AICC_AU\'"> <div dircos-module item="item"></div> </span>'+
			  '</div>'+
			  
			  '</div>'+
			  '</div>'+
			  '<div class="panel-list-boxdate" ng-show="(!dircontents || dircontents.length<1) && (!modcontents || modcontents.length<1)"><div class="panel-list-nodate">{{\'loader_no_data\' | translate}}</div></div><div class="panel-list-foot" ng-show="(!dircontents || dircontents.length<1) && (!modcontents || modcontents.length<1)"></div>' //暂无数据
		  '</div>';
   
	return {
        restrict: 'AE',
        template: html,
        replace: false,
        link: function(scope, element, attrs) {
        	scope.$watch('dircontents',function(){
            	//显示第一个
            	element.find(".dircontent:eq(0)").find(".slide-info").show();
            	element.find(".dircontent:eq(0)").find(".slide-break").removeClass("slide-plus").addClass("slide-less");
            	var slideTit = element.find(".dircontent").find(".slide-tit");
            	$(slideTit).bind('click', function(){
            		var dircontent = $(this).parent();
            		//点击本个节点，如果是符号是减号就换加号
            		$(dircontent).find(".slide-break").removeClass("slide-less").removeClass("slide-plus").toggleClass($(dircontent).find(".slide-info").is(":hidden") ? 'slide-less' : 'slide-plus');
            		//展开收起互换
            		$(dircontent).find(".slide-info").slideToggle(300);
            		$(dircontent).siblings().find(".slide-info").slideUp("slow");
            		//其他节点的图标按照本节点是否显示来变换
        			$(dircontent).siblings().find(".slide-break").removeClass("slide-plus").removeClass("slide-less").toggleClass($(dircontent).find(".slide-info").is(":hidden") ? 'slide-less' : 'slide-plus');
            	});
            	


        	});
        	
        }
    };
}).directive('dircosModule', function() {
	   var html =
           '<div class="xuan-bar" ng-if="item.resources.res_subtype == \'SCO\' " ><div class="xuan-bar-break {{!$last?\'border-less\':\'\'}} clearfix"><span class="mcolor-font-size xuan-bar-tit">{{item.title}}</span><a class="xuan-bar-xiangqing" title=""></a><a class="xuan-bar-play" ng-if="rcov.cov_status ==\'I\'" href="{{ {userEntId:userEntId, tkhId:tkhId, itmId:itmId, resId:resId, modId:item.id, itmTitle:itmTitle, curDate:curDate, timeValide:timeValide, app:app, item:item, rcov:rcov} | moduleUrl}}" title=""></a></div></div>'+
           '<div class="xuan-bar" ng-if="item.resources.res_subtype != \'SCO\' " ><div class="xuan-bar-break {{!$last?\'border-less\':\'\'}} clearfix">' +
           '<a class="xuan-bar-xiangqing-cp" title=""></a><span class="mcolor-font-size xuan-bar-tit-cp">{{item.title}}</span>'+
           '<a class="xuan-bar-play margin-top-2" ng-if="rcov.cov_status ==\'I\' && item.resources.res_subtype != \'SCO\' && (!item.resources.mov || item.resources.mov.mov_total_attempt < item.resources.mod.mod_max_usr_attempt || item.resources.mod.mod_max_usr_attempt == 0 || item.resources.isRestore) " href="{{ {userEntId:userEntId, tkhId:tkhId, itmId:itmId, resId:resId, modId:item.id, itmTitle:itmTitle, curDate:curDate, timeValide:timeValide, app:app, item:item, rcov:rcov} | moduleUrl}}" title=""></a>'+
           
           '<div class="margin-top15">'+
           '<span ng-if="item.resources.trainingStatus">'+
           '<span class="course-status" ng-if="item.resources.trainingStatus.indexOf(\'[IFCP]\') != -1">'+
           '<span ng-if="item.resources.res_subtype == \'TST\' || item.resources.res_subtype == \'DXT\' || item.resources.res_subtype == \'ASS\'">{{\'mod_\' + item.resources.res_subtype | translate}} | {{\'cos_need_submit\' | translate}}</span>'+
		   '<span ng-if="item.resources.res_subtype == \'RDG\' || item.resources.res_subtype == \'REF\' || item.resources.res_subtype == \'VOD\'">{{\'mod_\' + item.resources.res_subtype | translate}} | {{\'cos_need_view\' | translate}}</span>'+
		   '<span ng-if="item.resources.res_subtype == \'AICC_AU\'">{{\'mod_\' + item.resources.res_subtype | translate}} | {{\'cos_need_attempt\' | translate}}</span>'+
           '</span>'+
           '<span class="course-status" ng-if="item.resources.trainingStatus.indexOf(\'[CP]\') != -1">'+
           '<span ng-if="item.resources.res_subtype == \'TST\' || item.resources.res_subtype == \'DXT\' || item.resources.res_subtype == \'ASS\'">{{\'mod_\' + item.resources.res_subtype | translate}} | {{\'cos_need_pass\' | translate}}</span>'+
		   '<span ng-if="item.resources.res_subtype != \'TST\' && item.resources.res_subtype != \'DXT\' && item.resources.res_subtype != \'ASS\'">{{\'mod_\' + item.resources.res_subtype | translate}} | {{\'cos_need_complete\' | translate}}</span>'+
           '</span>'+
           '<span class="course-status" ng-if="item.resources.trainingStatus.indexOf(\'[null]\') != -1">'+
           '<span>{{\'mod_\' + item.resources.res_subtype | translate}} | {{\'cos_no_need\' | translate}}</span>' +
           '</span>'+
           '</span>'+
           '<span ng-if="!item.resources.trainingStatus">'+
           '<span class="course-status">{{\'mod_\' + item.resources.res_subtype | translate}} | {{\'cos_no_need\' | translate}}</span>'+
           '</span>'+
           
           '<div style="margin-right:-50px; float:right;">'+
           '<span class="panel-num-2 course-status margin-right20" ng-if="item.resources.pgr_attempt_nbr >0 && item.resources.pgr_status== \'NOT GRADED\' && item.resources.mov.mov_status">'+
		   '<span ng-if="item.resources.isTrainingCourse && item.resources.trainingStatus.indexOf(\'[IFCP]\') != -1"><image src="../../images/C_.png" style="width:15px;height:15px;"> {{\'cos_fulfill\' | translate}}</span>' +
		   '<span ng-if="item.resources.isTrainingCourse && item.resources.trainingStatus.indexOf(\'[CP]\') != -1"><image src="../../images/N_C.png" style="width:15px;height:15px;"> {{\'cos_unfulfill\' | translate}}</span>' +
		   '<span ng-if="!item.resources.isTrainingCourse"></span>'+
		   '</span>'+
		   
		   '<span class="panel-num-2 course-status margin-right20" ng-if="!(item.resources.pgr_attempt_nbr >0 && item.resources.pgr_status == \'NOT GRADED\') && item.resources.mov.mov_status">'+
		   '<span ng-if="item.resources.isTrainingCourse && item.resources.trainingStatus.indexOf(\'[CP]\') != -1">'+
		   '<span ng-if="(item.resources.res_subtype == \'TST\' || item.resources.res_subtype == \'DXT\' || item.resources.res_subtype == \'ASS\') && item.resources.pgr_completion_status == \'P\'"><image src="../../images/C_.png" style="width:15px;height:15px;"> {{\'cos_fulfill\' | translate}}</span>'+
		   '<span ng-if="(item.resources.res_subtype == \'TST\' || item.resources.res_subtype == \'DXT\') && item.resources.pgr_completion_status != \'P\'"><image src="../../images/N_C.png" style="width:15px;height:15px;"> {{\'cos_unfulfill\' | translate}}</span>'+
		   '<span ng-if="(item.resources.res_subtype == \'ASS\') && item.resources.pgr_completion_status != \'P\'"><image src="../../images/N_C.png" style="width:15px;height:15px;"> {{\'cos_unfulfill\' | translate}}</span>'+
		   '<span ng-if="(item.resources.res_subtype != \'TST\' && item.resources.res_subtype != \'DXT\' && item.resources.res_subtype != \'ASS\') && (item.resources.mov.mov_status == \'C\' || item.resources.mov.mov_status == \'P\')"><image src="../../images/C_.png" style="width:15px;height:15px;"> {{\'cos_fulfill\' | translate}}</span>'+
		   '<span ng-if="(item.resources.res_subtype != \'TST\' && item.resources.res_subtype != \'DXT\' && item.resources.res_subtype != \'ASS\') && (item.resources.mov.mov_status != \'C\' && item.resources.mov.mov_status != \'P\')"><image src="../../images/N_C.png" style="width:15px;height:15px;"> {{\'cos_unfulfill\' | translate}}</span>'+
		   '</span>'+
		   '<span ng-if="item.resources.isTrainingCourse && item.resources.trainingStatus.indexOf(\'[IFCP]\') != -1"><image src="../../images/C_.png" style="width:15px;height:15px;"> {{\'cos_fulfill\' | translate}}</span>'+
		   '<span ng-if="!item.resources.isTrainingCourse"></span>'+
		   '</span>'+
		   
		   '<span ng-if="item.resources.mov == null && item.resources.trainingStatus.indexOf(\'[null]\') == -1     &&  tkhId>0 " class="panel-num-2 course-status margin-right20">'+
		   '<span ng-if="item.resources.isTrainingCourse"><image src="../../images/N_C.png" style="width:15px;height:15px;"> {{\'cos_unfulfill\' | translate}}</span>'+
		   '<span ng-if="!item.resources.isTrainingCourse"></span>'+
		   '</span>'+
		   '</div>'+
           
           '</div>'+
           '</div>'+'</div>'+
           
           '<div class="xuan-tip">'+
                '<div class="tipBox">'+
                     '<div class="hd"><em class="arrow arrowT"><i></i></em></div>'+
                     '<div class="bd clearfix">'+                           
                     		moduleDetail+
                     '</div>'+
                '</div>'+
           '</div>'
	   
		return {
	        restrict: 'AE',
	        template: html,
	        require : '^?dir-module',
	        link: function(scope, element, attrs) {
	        	scope.$watch('item',function(){
	            	element.find(".xuan-bar-tit,.xuan-bar-xiangqing,.xuan-bar-xiangqing-cp").bind('click', function(){
	            		if($(this).attr("class").indexOf("xuan-bar-xiangqing") > -1) {
            				$(this).toggleClass("active");
            			}else if($(this).attr("class").indexOf("xuan-bar-tit") > -1){
        					$(this).next().toggleClass("active");
            			}
	            		var xuanBar = $(this).parent().parent();
	            		var xuanBarBreak = $(this).parent();
	            	    $(xuanBarBreak).removeClass("border-less").removeClass("border-plus").toggleClass($(xuanBar).next().is(":hidden") ? 'border-plus' : 'border-less');
	            		
	            		$(xuanBar).next().slideToggle(300);
	            		$(xuanBar).parent().parent().siblings().find(".xuan-tip").slideUp("slow");
	            		
	            		$(xuanBar).parent().parent().siblings().find(".xuan-bar-break").removeClass("border-less").removeClass("border-plus").toggleClass($(xuanBar).next().is(":hidden") ? 'border-plus' : !scope.$last?'':'border-less');
	            		if(scope.$last){
	            			$(xuanBar).find(".xuan-bar-break").removeClass("border-less");
	            		}	
//	            		$(this).parents(".xuan-biao").next(".xuan-cont").slideToggle(300).siblings(".xuan-cont").slideUp("slow");
	            	});
	        	})

	        }
	    };
}).directive('cosModule', function() {
	   var html =
		   '<div ng-repeat="item in modcontents" class="cos-area">'+
		   '<div class="xuan-biao" ><div class="xuan-biao-break clearfix"><a class="xuan-bar-xiangqing" ng-class="{\'active\':$first}" title=""></a><a class="xuan-bar-play" ng-if="rcov.cov_status ==\'I\' && item.resources.res_subtype == \'SCO\' " href="{{ {userEntId:userEntId, tkhId:tkhId, itmId:itmId, resId:resId, modId:item.id, itmTitle:itmTitle, curDate:curDate, timeValide:timeValide, app:app, item:item} | moduleUrl}}" title=""></a>'+
		   '<span class="xuan-bar-tit">{{item.title}}</span>'+
		   '<a class="xuan-bar-play" ng-if="rcov.cov_status ==\'I\' && item.resources.res_subtype != \'SCO\' && ((!item.resources.mov || item.resources.mov.mov_total_attempt < item.resources.mod.mod_max_usr_attempt || item.resources.mod.mod_max_usr_attempt == 0 || item.resources.isRestore) || (item.resources.res_subtype == \'ASS\' && app.app_id > 0 && item.resources.mod && item.resources.mod.mod_eff_start_datetime <= curDate && item.resources.mod.mod_eff_end_datetime >= curDate)) " href="{{ {userEntId:userEntId, tkhId:tkhId, itmId:itmId, resId:resId, modId:item.id, curDate:curDate, timeValide:timeValide, app:app, item:item} | moduleUrl}}" title=""></a></div>'+
		   

		   '<div style="margin-left:2px;">'+

		   '<span ng-if="item.resources.trainingStatus">' +
		   '<span class="course-status" ng-if="item.resources.trainingStatus.indexOf(\'[IFCP]\') != -1">'+
		   '<span ng-if="item.resources.res_subtype == \'TST\' || item.resources.res_subtype == \'DXT\' || item.resources.res_subtype == \'ASS\'">{{\'mod_\' + item.resources.res_subtype | translate}} | {{\'cos_need_submit\' | translate}}</span>'+
		   '<span ng-if="item.resources.res_subtype == \'RDG\' || item.resources.res_subtype == \'REF\' || item.resources.res_subtype == \'VOD\'">{{\'mod_\' + item.resources.res_subtype | translate}} | {{\'cos_need_view\' | translate}}</span>'+
		   '<span ng-if="item.resources.res_subtype == \'AICC_AU\' || item.resources.res_subtype == \'SCO\'">{{\'mod_\' + item.resources.res_subtype | translate}} | {{\'cos_need_attempt\' | translate}}</span>'+
		   '</span>'+
		   '<span class="course-status" ng-if="item.resources.trainingStatus.indexOf(\'[CP]\') != -1">'+
		   '<span ng-if="item.resources.res_subtype == \'TST\' || item.resources.res_subtype == \'DXT\' || item.resources.res_subtype == \'ASS\'">{{\'mod_\' + item.resources.res_subtype | translate}} | {{\'cos_need_pass\' | translate}}</span>'+
		   '<span ng-if="item.resources.res_subtype != \'TST\' && item.resources.res_subtype != \'DXT\' && item.resources.res_subtype != \'ASS\'">{{\'mod_\' + item.resources.res_subtype | translate}} | {{\'cos_need_complete\' | translate}}</span>'+
		   '</span>'+
		   '<span class="course-status" ng-if="item.resources.trainingStatus.indexOf(\'[null]\') != -1">'+
		   '<span>{{\'mod_\' + item.resources.res_subtype | translate}} | {{\'cos_no_need\' | translate}}</span>' +
		   '</span>'+
		   '</span>'+
		   '<span ng-if="item.resources.trainingStatus == null">' +
		   '<span class="course-status">{{\'mod_\' + item.resources.res_subtype | translate}} | {{\'cos_no_need\' | translate}}</span>' +
		   '</span>' +
		   
		   '<span class="panel-num-2 course-status margin-right20" ng-if="item.resources.pgr_attempt_nbr >0 && item.resources.pgr_status== \'NOT GRADED\' && item.resources.mov.mov_status">'+
		   '<span ng-if="item.resources.isTrainingCourse && item.resources.trainingStatus.indexOf(\'[IFCP]\') != -1"><image src="../../images/C_.png" style="width:15px;height:15px;"> {{\'cos_fulfill\' | translate}}</span>' +
		   '<span ng-if="item.resources.isTrainingCourse && item.resources.trainingStatus.indexOf(\'[CP]\') != -1"><image src="../../images/N_C.png" style="width:15px;height:15px;"> {{\'cos_unfulfill\' | translate}}</span>' +
		   '<span ng-if="!item.resources.isTrainingCourse"></span>' +
		   '</span>'+
		   
		   '<span class="panel-num-2 course-status margin-right20" ng-if="!(item.resources.pgr_attempt_nbr >0 && item.resources.pgr_status == \'NOT GRADED\') && item.resources.mov.mov_status">'+
		   '<span ng-if="item.resources.isTrainingCourse && item.resources.trainingStatus.indexOf(\'[CP]\') != -1">'+
		   '<span ng-if="(item.resources.res_subtype == \'TST\' || item.resources.res_subtype == \'DXT\' || item.resources.res_subtype == \'ASS\') && item.resources.pgr_completion_status == \'P\'"><image src="../../images/C_.png" style="width:15px;height:15px;"> {{\'cos_fulfill\' | translate}}</span>'+
		   '<span ng-if="(item.resources.res_subtype == \'TST\' || item.resources.res_subtype == \'DXT\') && item.resources.pgr_completion_status != \'P\'"><image src="../../images/N_C.png" style="width:15px;height:15px;"> {{\'cos_unfulfill\' | translate}}</span>'+
		   '<span ng-if="(item.resources.res_subtype == \'ASS\') && item.resources.pgr_completion_status != \'P\'"><image src="../../images/N_C.png" style="width:15px;height:15px;"> {{\'cos_unfulfill\' | translate}}</span>'+
		   '<span ng-if="(item.resources.res_subtype != \'TST\' && item.resources.res_subtype != \'DXT\' && item.resources.res_subtype != \'ASS\') && (item.resources.mov.mov_status == \'C\' || item.resources.mov.mov_status == \'P\')"><image src="../../images/C_.png" style="width:15px;height:15px;"> {{\'cos_fulfill\' | translate}}</span>'+
		   '<span ng-if="(item.resources.res_subtype != \'TST\' && item.resources.res_subtype != \'DXT\' && item.resources.res_subtype != \'ASS\') && (item.resources.mov.mov_status != \'C\' && item.resources.mov.mov_status != \'P\')"><image src="../../images/N_C.png" style="width:15px;height:15px;"> {{\'cos_unfulfill\' | translate}}</span>'+
		   '</span>'+
		   '<span ng-if="item.resources.isTrainingCourse && item.resources.trainingStatus.indexOf(\'[IFCP]\') != -1"><image src="../../images/C_.png" style="width:15px;height:15px;"> {{\'cos_fulfill\' | translate}}</span>'+
		   '<span ng-if="!item.resources.isTrainingCourse"></span>'+
		   '</span>'+
		   
		   '<span ng-if="item.resources.mov == null && item.resources.trainingStatus.indexOf(\'[null]\') == -1   &&  tkhId>0" class="panel-num-2 course-status margin-right20">'+
		   '<span ng-if="item.resources.isTrainingCourse"><image src="../../images/N_C.png" style="width:15px;height:15px;"> {{\'cos_unfulfill\' | translate}}</span>'+
		   '<span ng-if="!item.resources.isTrainingCourse"></span>'+
		   '</span>'+
		   '</div></div>'+
		   
		   
		   '<div ng-style="{\'display\':($first ? \'block\' : \'none\')}" class="xuan-cont">'+
			   '<div class="tipBox">'+
	           '<div class="hd"><em class="arrow arrowT"><i></i></em></div>'+
	           '<div class="bd clearfix">'+
	           		moduleDetail+
	           '</div>'+
	      '</div>'+ 
		   '</div>'+
		   '<div>';
	   
		return {
	        restrict: 'AE',
	        template: html,
	        link: function(scope, element, attrs) {
	        	scope.$watch('modcontents', function(){
	            	element.find(".cos-area").find(".xuan-biao").find(".xuan-bar-tit,.xuan-bar-xiangqing").bind('click', function(){
	            		var xuanBar = $(this).parent().parent();
	            		if($(this).attr("class").indexOf("xuan-bar-xiangqing") > -1){
	            			$(this).toggleClass("active");
	            		}else if($(this).attr("class").indexOf("xuan-bar-tit") > -1 ){
            				$(this).prev().toggleClass("active");
            			}
	            		$(xuanBar).next().slideToggle(300).siblings(".xuan-cont").slideUp("slow");
	            	});
	        	});

	        }
	    };
}).directive('integratedModule', function(){
	   var html =
		   	  '<div>'+
				  '<div ng-repeat="dir in integratedcontents" class="dircontent">'+
				  '<p class="slide-tit xuan-tit"><span class="slide-break slide-plus">{{dir.icd_type}} {{\'cos_integrated_must_do\' | translate : {value : dir.icd_completed_item_count } }}</span></p>'+
				  '<div class="slide-info xuan-info">'+
				  
				        '<div ng-class="{\'panel-list-cont-4\':$first, \'panel-list-cont-5\':$index>0}" ng-repeat="item in dir.child">'+
				            '<a class="xuan-info-tit-2" href="javascript:;" forward="{tkhId:item.app_tkh_id, itmId: item.itm_id} | courseUrl" title=""><span class="mcolor-font-size"><i class="wzb-pc-icon" ng-show="!item.itm_mobile_ind || item.itm_mobile_ind == \'no\'"></i>{{item.title}} {{item.itm_title}}</span></a>'+
				            '<div class="panel-title-17"><div class="panel-add-03 clearfix"><em class="panel-num-2">{{item.itm_type}}</em>{{\'cos_type_short\' | translate}}</div></div>'+	//类型
				            '<div class="panel-title-17"><div class="panel-add-03 clearfix"><em class="panel-num-3">{{item.cov.cov_status?item.cov.cov_status:\'cos_app_notapp\' | translate}} </em>{{item.cov.cov_status?\'cos_module_status\':\'cos_module_status\' | translate}}</div></div>'+
				        '</div>'+
				        
				  '</div>'+
				  '</div>'+
			  '</div>';
	   
		return {
	        restrict: 'AE',
	        template: html,
	        replace: true,
	        link: function(scope, element, attrs) {
	        	scope.$watch('integratedcontents',function(){
	            	//显示第一个
	            	element.find(".dircontent:eq(0)").find(".slide-info").show();
	            	element.find(".dircontent:eq(0)").find(".slide-break").removeClass("slide-plus").addClass("slide-less");
	            	
	            	element.find(".dircontent > p").bind('click', function(){
	            		
	            		var el = $(this).parent(".dircontent");
	            		
	            		//点击本个节点，如果是符号是减号就换加号
	            		el.find(".slide-break").removeClass("slide-less").removeClass("slide-plus").toggleClass(el.find(".slide-info").is(":hidden") ? 'slide-less' : 'slide-plus');
	            		//展开收起互换
	            		el.find(".slide-info").slideToggle(300);
	            		el.siblings().find(".slide-info").slideUp("slow");
	            		//其他节点的图标按照本节点是否显示来变换
	            		el.siblings().find(".slide-break").removeClass("slide-plus").removeClass("slide-less").toggleClass(el.find(".slide-info").is(":hidden") ? 'slide-less' : 'slide-plus');
	            	})
	        	});
	        	
	        }
	    };
}).directive('courseButton',['Ajax', 'alertService', 'dialogService',function(Ajax, alertService,dialogService){
		var reload = function(){
			var url = window.location.href;
			if(url != undefined && url.indexOf("?") >-1 ){
				//url = url.substring(0,url.indexOf("?"));
				window.location.href = url;
			} else {
				window.location.reload();
			}
	   }
	   var cancelEnrol = function(appId){
	   		if(appInd){
	   			var wait = plus.nativeUI.showWaiting();
	    	}
			var url = '/app/application/cancel?app_id=' + appId;
			Ajax.post(url, null ,function(result){
				dialogService.modal("delete_enroll","o",function(){
				
					if(appInd){
						changeWebviewDetail('signup.html');
						changeWebviewDetail('courseIndex',function(){
							wait.close();
							reload();
						});
					} else {
						reload();
					}
					
				});
				
			})
		};
		var enrol = function(itm_id, itm_type) {
			if (itm_id > 0) {
				if(appInd){
					var wait = plus.nativeUI.showWaiting();
		    	}
				var url = '/app/application/app?itm_id=' + itm_id;
				Ajax.post(url, null, function(data){
					if (data.status == -1 && data.msg) {
						if(appInd){
							wait.close();
						}
				    	alertService.add('danger', data.msg);
					} else {
						dialogService.modal("enroll_success","o",function(){
							if(appInd){
								changeWebviewDetail('courseIndex',function(){
									wait.close();
									reload();
								});
							} else {
								reload();
							}
						});
					}
				})
			}
		
		}
	
		var html = '<span ><a class="list-tool-3-sign" href="javascript:;" ng-show="btnName" >{{btnName | translate}}</a><span style="line-height:35px;float:right;padding-right:5px;color:#E42E2E" ng-show="btnStatus">{{btnStatus | translate}}</span></span>';
		return {
	        restrict: 'AE',
	        template: html,
	        replace: true,
	        link: function(scope, element, attrs) {
	        	scope.$watch('btnParams',function(){
	        		var data = scope.btnParams || {};
		    		var btn = data.btn;
		    		if(btn == undefined || btn == null || btn == '') return;
	    			if (btn == 0) {
	    			} else if (btn == 1) {
	    				if(data.app_id != '' && data.app_id != undefined && !data.canApp) return;	
	    				scope.btnName = "cos_enroll";
	    				element.children(".list-tool-3-sign").bind('click', function(){
	    					enrol(data.itm_id, data.itm_type);
	    				})
	    			} else if (btn == 2) {
	    				//开始学习， cancel_enrol
	    				scope.btnName = "cos_cancel_enroll";
	    				element.children(".list-tool-3-sign").bind('click', function(){
	    					cancelEnrol(data.app_id);
	    				})
	    			} else if (btn == 3) {
	    				
	    			} else if (btn == 4) {
	    				//报名
	    				scope.btnName = "cos_enroll";
	    				element.children(".list-tool-3-sign").bind('click', function(){
	    					enrol(data.itm_id, data.itm_type);
	    				})
	    			} else if (btn == 5) {
	    				//等待审批
//	    				scope.btnStatus = 'cos_app_waiting';
	    				scope.btnClass = "list-tool-3-signq";//取消报名的class
	    				scope.btnName = "cos_cancel_enroll";
	    				element.children(".list-tool-3-sign").bind('click', function(){
	    					cancelEnrol(data.app_id);
	    				})
	    			} else if (btn == 6) {
	    				//等待队列
//	    				scope.btnStatus = 'cos_in_wait_list';
	    				scope.btnName = "cos_cancel_enroll";
	    				element.children(".list-tool-3-sign").bind('click', function(){
	    					cancelEnrol(data.app_id);
	    				})
	    			} else if (btn == 7) {
	    				//名額已滿
//	    				scope.btnStatus = 'cos_full_capacity';
	    				scope.btnName = "cos_enroll";
	    				element.children(".list-tool-3-sign").bind('click', function(){
	    					dialogService.modal("cos_full_capacity","o");
	    				})

	    			} else if (btn == -1 && data.appnTimeValide){
	    				//报名期限已过
//	    				scope.btnStatus = 'course_app_time_tip';
	    				scope.btnName = "cos_enroll";
	    				element.children(".list-tool-3-sign").bind('click', function(){
	    					dialogService.modal("course_app_time_tip","o");
	    				})
	    			}
	        	}); 
	        	
	        }
	    };
}]);