appModule.service('InstructorService',['$filter','Loader','Ajax',function($filter,Loader,Ajax){
	
	var service = new Object();
	
	var MAX_SCORE = 5;
	
	// private method 私有方法
	// 获取讲师分数对应的星星数组
	// item 为传入参数，方法执行之后，item有两个数组属性
	var injectStartArr2 = function(item){
		item.iti_scope = item.iti_scope || 0;
		item.solidStarArr = [];//讲师评分级别，页面用实心圆表示，最高分 5 分
		item.hollowStarArr = [];//空心圆 = 5 - 实心圆个数
		for(var i=0;i<item.iti_scope;i++){
			item.solidStarArr.push(i);
		}
		for(var i=0;i<MAX_SCORE-item.iti_scope;i++){
			item.hollowStarArr.push(i);
		}
	}; 
	
	service.getInstructorLoader = function(param){
		
		var param = param || {};
		
		var url = "/app/instr/page";
		var instructorListLoader = new Loader(url,param,function(data){
			for(var i in data.items){
				var item = data.items[i];
				
				item.enc_iti_ent_id = wbEncryptor.encrypt(item.iti_ent_id);
				
				if(!item.iti_level_inited){//不要重复获取讲师级别的label
					item.iti_level = this.getInstructorLevelLabel(item.iti_level);
				}
				item.iti_level_inited = true;
				
				injectStartArr2(item);
				
				//星星 
				if(item.iti_score == undefined){
					item.iti_score = 0;
				}
				item.iti_score_star = [];
				item.iti_score_star_2 = [];
				for(var i = 0; i < Math.round(item.iti_score);i++){
					item.iti_score_star.push({"star":"start-w start-w-full"});
					item.iti_score_star_2.push({"star":"start-g start-g-full"});
				}
				for(var i = 0; i < 5 - Math.round(item.iti_score);i++){
					item.iti_score_star.push({"star":"start-w"});
					item.iti_score_star_2.push({"star":"start-g"});
				}
			}
		}.bind(this));
		return instructorListLoader;
	};
	
	/**
	 * 根据 【level】 字符串获取对应的讲师级别
	 */
	service.getInstructorLevelLabel = function(level){
		
		var instructorLabel;
		
		if(level == 'J') {
			instructorLabel = 'instructor_level_junior';
	    } else if(level == 'M') {
	    	instructorLabel = 'instructor_level_middle';
	    } else if(level == 'S') {
	    	instructorLabel = 'instructor_level_senior';
	    } else if(level == 'D') {
	    	instructorLabel = 'instructor_level_special';
	    }
		
		return $filter("translate")(instructorLabel);
	};
	
	/**
	 * 获取讲师级别集合
	 */
	service.getInstructorLevels = function(){
		var instructorLevels = [
		                           {"value":'',label:$filter("translate")('text_all')},
		                           {"value":'J',label:this.getInstructorLevelLabel('J')},
		                           {"value":'M',label:this.getInstructorLevelLabel('M')},
		                           {"value":'S',label:this.getInstructorLevelLabel('S')},
		                           {"value":'D',label:this.getInstructorLevelLabel('D')}
		                       ];
		return instructorLevels;
	};
	
	/**
	 * 根据 【type】 字符串获取对应的讲师类型
	 */
	service.getInstructorTypeLabel = function(type){
		
		var instructorTypeLabel;
		
		if(type == 'P') {
			instructorTypeLabel = 'instructor_type_part_time';
	    } else if(type == 'F') {
	    	instructorTypeLabel = 'instructor_type_duty';
	    }
		
		return $filter("translate")(instructorTypeLabel);
	};
	
	/**
	 * 获取讲师类型集合
	 */
	service.getInstructorTypes = function(){
		
		var instructorTypes = [
		                           {"value":'',label:$filter("translate")('text_all')},
		                           {"value":'P',label:this.getInstructorTypeLabel("P")},
		                           {"value":'F',label:this.getInstructorTypeLabel("F")}
		                      ];
		return instructorTypes;
	};
	
	/**
	 * 获取讲师来源集合
	 */
	service.getInstructroSources = function(){
		var instructroSources = [
		                           {"value":'',label:$filter("translate")('text_all')},
		                           {"value":'IN',label:$filter("translate")('instructor_source_internal')},
		                           {"value":'EXT',label:$filter("translate")('instructor_source_External')}
		                        ];
		return instructroSources;
	};
	
	/**
	 * 获取讲师详情
	 */
	service.getInstructorDetail = function(id,callBack){
		var url = '/app/instr/detailJson/'+id
		Ajax.post(url,{},function(data){
			
			var instr = data.instructorInf;
			
			var gender = instr.iti_gender;
			switch(gender){
			case 'M' :
				instr.iti_gender = $filter("translate")('personal_files_sex_M');
				break;
			case 'F' :
				instr.iti_gender = $filter("translate")('personal_files_sex_F');
				break;
			default:
				instr.iti_gender = $filter("translate")('personal_files_null');
				break;
			}
			
			instr.iti_bday = instr.iti_bday ? $filter("toDate")(instr.iti_bday) : '--';
			instr.iti_mobile = instr.iti_mobile || '--';
			instr.iti_email = instr.iti_email || '--';
			instr.iti_level = this.getInstructorLevelLabel(instr.iti_level);
			//星星  bill.lai 2016.05.10
			if(instr.iti_score == undefined){
				instr.iti_score = 0;
			}
			instr.iti_score_star = [];
			instr.iti_score_star_2 = [];
			for(var i = 0; i < Math.round(instr.iti_score);i++){
				instr.iti_score_star.push({"star":"start-w start-w-full"});
				instr.iti_score_star_2.push({"star":"start-g start-g-full"});
			}
			for(var i = 0; i < 5 - Math.round(instr.iti_score);i++){
				instr.iti_score_star.push({"star":"start-w"});
				instr.iti_score_star_2.push({"star":"start-g"});
			}
			injectStartArr2(instr);
			callBack(data);
		}.bind(this));
	};
	
	/**
	 * 获取讲师开课列表
	 * 注意：調用方法的時候，确保页面已经已经引入了courseAndExamModule.js
	 */
	service.getInstructorCourseListLoader = function(id){
		var url = "/app/instr/pageItemCos/"+id;
		var courseListLoader = new Loader(url,{},function(result){
			for(var i=0;i<result.items.length;i++){
				var item = result.items[i];
				item.encItmId = wbEncryptor.encrypt(item.itm_id);
				item.cla = courseAndExamModule.getItemIcon((item.itm_exam_ind == 1 ? 'exam':'course'),item.itm_type);
			}
		});
		return courseListLoader;
	};
	
	return service;
	
}]);							
