(function(w){
	
	var courseAndExamModule = {};
	w.courseAndExamModule = courseAndExamModule;
	
	//推荐类型集合
	courseAndExamModule.getRecommendTypes = function(){
		var recommendTypes = [];
	    recommendTypes.push({id : 'all', value:'', title:'cos_type_all', select : true});
	    recommendTypes.push({id : 'tadm', value:'tadm', title:'cos_recommend_tadm'});
	    recommendTypes.push({id : 'grade', value:'grade', title:'cos_recommend_grade'});
	    recommendTypes.push({id : 'group', value:'group', title:'cos_recommend_group'});
	    recommendTypes.push({id : 'sup', value:'sup', title:'cos_recommend_sup'});
	    recommendTypes.push({id : 'position', value:'position', title:'cos_recommend_position'});
	    return recommendTypes;
	};
	
	//是否必修选修
	courseAndExamModule.getCompulsoryTypes = function(){
		var compulsoryTypes = [];
	    compulsoryTypes.push({id:'required',value:'1',title:'cos_required',select:false,cla:'wiz-bixiu'});
	    compulsoryTypes.push({id:'elective',value:'0',title:'cos_elective',select:false,cla:''});
	    return compulsoryTypes;
	};
	
	//学习状态类型集合
	courseAndExamModule.getAppStatus = function(){
		var appStatus = [];
		appStatus.push({id : 'all', value:'', title : 'text_all', cla:'',select : true});
		appStatus.push({id : 'Waiting', value : 'Waiting', title : 'cos_in_wait_list',cla:'wiz-wait'});
		appStatus.push({id : 'Pending', value : 'Pending', title : 'cos_app_pending',cla:'wiz-approveing'});
		appStatus.push({id : 'I', value : 'I', title : 'cos_app_inprogress',cla:'wiz-learning'});
		appStatus.push({id : 'C', value:'C', title : 'cos_app_completed', cla:'wiz-finish'});
		appStatus.push({id : 'F', value : 'F', title : 'cos_app_fail',cla:'wiz-unfinish'});
		appStatus.push({id : 'W', value : 'W', title : 'cos_app_withdrawn',cla:'wiz-giveup'});
		appStatus.push({id : 'Rejected', value : 'Rejected', title : 'cos_app_rejected',cla:'wiz-rejected'});
		
		return appStatus; 
	};
	
	//学习状态类型简单集合
	courseAndExamModule.getSimpleAppStatus = function(){
		var appStatus = [];
		appStatus.push({id : 'I', value : 'I', title : 'cos_app_inprogress',cla:'wiz-learning'});
		appStatus.push({id : 'C', value:'C', title : 'cos_app_completed', cla:'wiz-finish'});
		appStatus.push({id : 'notIAndC', value : 'notIAndC', title : 'global_other',cla:'wiz-other'});
		return appStatus; 
	};
	
	//课程类型集合
	courseAndExamModule.getItemTypes = function(){
		var itemTypes = [];//课程类型集合
		itemTypes.push({id : 'all', value:'', title : 'cos_type_all', cla:'',select : true});
		itemTypes.push({id : 'selfstudy', value : 'selfstudy', title : 'cos_type_selfstudy',cla:'wiz-internet'});
		itemTypes.push({id : 'classroom', value : 'classroom', title : 'cos_type_classroom',cla:'wiz-facetoface'});
		//itemTypes.push({id : 'integrated', value : 'integrated', title : 'cos_type_integrated',cla:'wiz-project'});
		return itemTypes;
	};
	
	//考试类型集合
	courseAndExamModule.getExamTypes = function(){
		var examTypes = [];//考试类型集合
		examTypes.push({id : 'all', value:'', title : 'cos_type_all', select : true});
		examTypes.push({id : 'selfstudy', value : 'selfstudy', title : 'exam_type_selfstudy',cla : 'wiz-online-exams'});
		examTypes.push({id : 'classroom', value : 'classroom', title : 'exam_type_classroom',cla : 'wiz-offline-exams'});
		return examTypes;
	};
	
	//获取课程或考试列表每一项的类型图标
	//@param cosType : exam or course
	//@itemType : 	CLASSROOM(离线) or SELFSTUDY(网上) or INTEGRATED(项目式培训)
	courseAndExamModule.getItemIcon = function(cosType,itemType){
		var result;
		if("exam" === cosType){
			if(itemType === 'SELFSTUDY'){//网上考试
				result = "list-tool-1-onlineexams";
			}else{//离线考试
				result = "list-tool-1-offlineexams";
			}
		}else{
			if(itemType === 'SELFSTUDY'){//网上课程
				result = "list-tool-1-wangshang";
			}else if(itemType === 'CLASSROOM'){//面授课程(离线)
				result = "list-tool-1-mianshou";
			}else{//项目式培训
				result = "list-tool-1-xiangmushi";
			}
		}
		return result;
	};
	
	//获取课程或考试详情和选择条件的类型图标
	//@param cosType : exam or course
	//@itemType : 	CLASSROOM(离线) or SELFSTUDY(网上) or INTEGRATED(项目式培训)
	courseAndExamModule.getItemIconDetail = function(cosType,itemType){
		var result;
		if("exam" === cosType){
			if(itemType === 'SELFSTUDY'){//网上考试
				result = "wiz-online-exams";
			}else{//离线考试
				result = "wiz-offline-exams";
			}
		}else{
			if(itemType === 'SELFSTUDY'){//网上课程
				result = "wiz-internet";
			}else if(itemType === 'CLASSROOM'){//面授课程(离线)
				result = "wiz-facetoface";
			}else{//项目式培训
				result = "wiz-project";
			}
		}
		return result;
	};
	
	//获取学习状态国际化字段和图标
	courseAndExamModule.getAppStatusJson = function(appStatus, type){
		var appStatusArr = courseAndExamModule.getAppStatus();
		   var result = {};
		   if(!appStatus) return;
		   
		   switch(appStatus){
		   case "Waiting" :
			   result = appStatusArr[1];
			   break;
		   case "Pending" :
			   result = appStatusArr[2];
			   break;
		   case "I" :
			   result = appStatusArr[3];
			   break;
		   case "C" :
			   result = appStatusArr[4];
			   break;
		   case "F" :
			   result = appStatusArr[5];
			   break;
		   case "W" :
			   result = appStatusArr[6];
			   break;
		   case "Rejected" :
			   result = appStatusArr[7];
			   break;
		   }
		   
		return result;
	}
	
	
})(window);