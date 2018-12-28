//根据当前课程/考试类型  加载导航栏
function loadNavigationTemplate() { 
	//判断权限     没有权限则不显示 
	if(hasItmCosMain == true || hasContentMain == true || hasEnrollMain == true || hasResultMain == true || hasTeachingCourse ==true){
		//如果是讲师 
		if(hasTeachingCourse == true){
		  if(create_run_ind != true){
			  $("#buzhou").hide();
		      $("#geli_div").hide();
			  //讲师导航 
			  var secondHtml = role_for_teacher();
			  $("#second_buzhou").html(secondHtml); 
		  }
		}else{
			    var innerHTML =  '';
			    if(itm_type == 'SELFSTUDY'){
			    	//<!-- 网上课程-->
			    	$("#buzhou").width("646");
			    	$("#second_buzhou").addClass(" erjiyi");
			    	
			        innerHTML = itm_type_selfstudy();
				}else if(itm_type == 'INTEGRATED'){
					//<!-- 项目式培训-->
					$("#buzhou").width("546");
					$("#second_buzhou").addClass(" erjisi");
					
				    innerHTML = itm_type_integrated();
				}else if(itm_type == 'CLASSROOM' && create_run_ind == true){
					//<!-- 离线课程 -->
					$("#buzhou").width("765");
					$("#second_buzhou").addClass(" erjier");
					
					innerHTML = itm_type_classroom_course();
				}else if(itm_type == 'CLASSROOM' && run_ind == true){
					//<!-- 班级-->
					$("#buzhou").width("765");
					$("#second_buzhou").addClass(" erjisan");
					
					innerHTML = itm_type_classroom_class();
				}else{
					$("#buzhou").width("360");
					
					innerHTML = itm_type_classroom_other();
				}
			    
			    //如果没有内容  则隐藏
			    if(innerHTML == ''){
			    	$("#buzhou").hide();
			    	$("#geli_div").hide();
			    }else{
			    	$("#first_buzhou").html(innerHTML);
			    }
			    
			    var secondHtml = getSecondNavigationInfo();
			    if(secondHtml == ''){
			    	$("#second_buzhou").hide();
			    }else{
			    	$("#second_buzhou").html(secondHtml); 
			    }
			  
		}
		
	}
	
};

//根据不同类型  获取一级导航栏
//网上课程
 function  itm_type_selfstudy(){
    	 var html = '';
    	 //框架信息节点     
    	 var is_full_01 = true;
    	 if(is_add == true){
    		 is_full_01 = false;
    	 }
    	 html += getHtmlByParameter(is_full_01,link01(),'01','01',fetchLabel('label_core_training_management_315'),false,null,space_img);
    	 //网上学习内容 
    	 var is_full_05 = true;
    	 if(rmdOnlineContent == true || is_add == true){
    		 is_full_05 = false;
    	 }
    	 html += getHtmlByParameter(is_full_05,link05(),'05','02',fetchLabel('label_core_training_management_237'),false,hasContentMain,space_img);
     	 //分数与结训条件
    	  var is_full_03 = true;
    	  if(rmdCompletionCriteriaSettings == true || is_add == true){
    		 is_full_03 = false;
    	  }
     	  html += getHtmlByParameter(is_full_03,link03(),'03','03',fetchLabel('label_core_training_management_386'),false,hasContentMain,space_img);
     	  //发布    
     	  var  publish_title = fetchLabel('global_publish');
     	  var  publish_is_full = false;
     	  if(itm_status == 'ON' || itm_status == 'ALL'  || itm_status == 'TARGET_LEARNER'){
     		publish_title = fetchLabel('label_core_training_management_221');
     		publish_is_full = true;
     	  }
     	  html += getHtmlByParameter(publish_is_full,link13(),'publish','04',publish_title,false,hasContentMain);
     	 //报名管理
     	  html += getHtmlByParameter(false,enroll_management_link(),'enroll_manager','',fetchLabel('label_core_training_management_384'),false,hasContentMain);
     	 //学习管理
     	  html += getHtmlByParameter(false,learning_result_management_link(),'learning_result_manager','',fetchLabel('label_core_training_management_385'),false,hasContentMain);
     	 //更多
     	  html += getHtmlByParameter(false,more_function_link(),'more_function','',fetchLabel('label_core_training_management_257'),true,hasContentMain);
    	return html;
    }                    

//项目式培训
 function itm_type_integrated(){
 	var html = '';
	   	 //框架信息节点
 	 var is_full_01 = true;
	   	 if(is_add == true){
	   		 is_full_01 = false;
	   	 }
	     html += getHtmlByParameter(is_full_01,link01(),'01','01',fetchLabel('label_core_training_management_315'),false,null,space_img);
	     //课程包
	     var is_full_06 = true;
	   	 if(is_add == true || rmdCoursePackage == true){
	   		is_full_06 = false;
	   	 }
	   	 html += getHtmlByParameter(is_full_06,link06(),'06','02',fetchLabel('label_core_training_management_248'),false,hasItmCosMain,space_img);
	     //发布    
 	  var  publish_title = fetchLabel('global_publish');
 	  var  publish_is_full = false;
 	  if(itm_status == 'ON' || itm_status == 'ALL'  || itm_status == 'TARGET_LEARNER'){
 		publish_title = fetchLabel('label_core_training_management_221');
 		publish_is_full = true;
 	  }
 	  html += getHtmlByParameter(publish_is_full,link13(),'publish','03',publish_title,false,hasContentMain);
 	  //报名管理
 	  html += getHtmlByParameter(false,enroll_management_link(),'enroll_manager','',fetchLabel('label_core_training_management_384'),false,hasContentMain);
 	  //结训记录
 	  html += getHtmlByParameter(false,link113,'113','',fetchLabel('label_core_training_management_242'),false,hasItmCosMain);
 	  //更多
  	  html += getHtmlByParameter(false,more_function_link(),'more_function','',fetchLabel('label_core_training_management_257'),true,hasContentMain);
       return html;
 }

 
 //离线课程
 function itm_type_classroom_course(){
 	var html = '';
	   	 //框架信息节点
 	 var is_full_01 = true;
 	 if(is_add == true){
 		 is_full_01 = false;
 	 }
	   	 html += getHtmlByParameter(is_full_01,link01(),'01','01',fetchLabel('label_core_training_management_315'),false,null,space_img);
	   	 //如果是添加课程或者还没有设置内容模式
	   	 if(is_add == true || content_def == '' ){
	   		 //内容模式  
	   		 var is_full_3 = true;
	    	 if(is_add == true || content_def == ''){
	    		 is_full_3 = false;
	    	 }
	   		 html += getHtmlByParameter(is_full_3,link3(),'05','02',fetchLabel('label_core_training_management_314'),false,hasItmCosMain,space_img);
	   		 //日程表
	   		 var is_full_108 = true;
	    	 if(has_lesson == false || is_add == true){
	    		 is_full_108 = false;
	    	 }
	   		 html += getHtmlByParameter(is_full_108,link108(),'108','03',fetchLabel('label_core_training_management_247'),false,hasContentMain,space_img);
	   	 }else{
	     	 //网上学习内容
	   		 var is_full_05 = true;
	   		 if(rmdOnlineContent == true || is_add == true){
	    		 is_full_05 = false;
	    	 }
	   		 html += getHtmlByParameter(is_full_05,link05(),'05','02',fetchLabel('label_core_training_management_237'),false,hasContentMain,space_img);
	   		 //日程表
	   		/* var is_full_108 = true;
	    	 if(has_lesson == false || is_add == true){
	    		 is_full_108 = false;
	    	 }
	   		 html += getHtmlByParameter(is_full_108,link108(),'108','03',fetchLabel('label_core_training_management_247'),false,hasContentMain,space_img);
	   	   */
	   	 }
	   	 
	   	 if(content_def == 'PARENT'){
	   		 //分数与结训条件
	   		 var is_full_03 = true;
	    	  if(rmdCompletionCriteriaSettings == true || is_add == true){
	    		 is_full_03 = false;
	    	  }
	   		 html += getHtmlByParameter(is_full_03,link03(),'03','03',fetchLabel('label_core_training_management_386'),false,hasContentMain,space_img);
  	     //考试场次管理
	   		  var is_full_6 = true;
	   		  var lab_value = lab_value = fetchLabel('label_core_training_management_468_ex');
	    	  if(hadPublishClass == false || is_add == true){
	    		  is_full_6 = false;
	    	  }
	    	  if(itm_exam_ind == '0'){
	    		  lab_value = fetchLabel('label_core_training_management_246');
	    	  }
	    	//label_core_training_management_246
	   		  html += getHtmlByParameter(is_full_6,link6(),'04','04',lab_value,false,hasContentMain,space_img);
	   		 //发布
	   		  var  publish_title = fetchLabel('global_publish');
	    	  var  publish_is_full = false;
	    	  if(itm_status == 'ON' || itm_status == 'ALL'  || itm_status == 'TARGET_LEARNER'){
	    		publish_title = fetchLabel('label_core_training_management_221');
	    		publish_is_full = true;
	    	  }
	    	  html += getHtmlByParameter(publish_is_full,link13(),'publish','06',publish_title,false,hasContentMain);
	      }else{
	    	  //班级管理
	    	  var is_full_6 = true;
	    	  var lab_value = fetchLabel('label_core_training_management_468_ex');
	    	  if(hadPublishClass == false || is_add == true){
	    		  is_full_6 = false;
	    	  }
	    	  if(itm_exam_ind == '0'){
	    		  lab_value = fetchLabel('label_core_training_management_246');
	    	  }
	   		  html += getHtmlByParameter(is_full_6,link6(),'04','03',lab_value,false,hasContentMain,space_img);
	   		 //发布
	   		  var  publish_title = fetchLabel('global_publish');
	    	  var  publish_is_full = false;
	    	  if(itm_status == 'ON' || itm_status == 'ALL'  || itm_status == 'TARGET_LEARNER'){
	    		publish_title = fetchLabel('label_core_training_management_221');
	    		publish_is_full = true;
	    	  }
	    	  html += getHtmlByParameter(publish_is_full,link13(),'publish','04',publish_title,false,hasContentMain); 
	      }
 	  //报名管理
 	  html += getHtmlByParameter(false,enroll_management_link(),'enroll_manager','',fetchLabel('label_core_training_management_384'),false,hasContentMain);
 	  //更多
  	  html += getHtmlByParameter(false,more_function_link(),'more_function','',fetchLabel('label_core_training_management_257'),true,hasContentMain);
       return html;
 }
 
 //班级
 
 function itm_type_classroom_class(){
 	var html = '';
	   	 //框架信息节点
	     var is_full_01 = true;
	   	 if(is_add == true){
	   		 is_full_01 = false;
	   	 }
	   	 html += getHtmlByParameter(is_full_01,link01(),'01','01',fetchLabel('label_core_training_management_315'),false,null,space_img);
	     //网上学习内容
	     var is_full_05 = true;
	   	 if(rmdOnlineContent == true || is_add == true){
	   		 is_full_05 = false;
	   	 }
		 html += getHtmlByParameter(is_full_05,link05(),'05','02',fetchLabel('label_core_training_management_237'),false,hasContentMain,space_img);
		 //日程表
		 var is_full_108 = true;
	   	 if(has_lesson == false || is_add == true){
	   		is_full_108 = false;
	   	 }
		 html += getHtmlByParameter(is_full_108,link108(),'108','03',fetchLabel('label_core_training_management_247'),false,hasContentMain,space_img);
		 if(content_def == 'PARENT'){
	   		 //发布
	   		  var  publish_title = fetchLabel('global_publish');
	    	  var  publish_is_full = false;
	    	  if(itm_status == 'ON' || itm_status == 'ALL'  || itm_status == 'TARGET_LEARNER'){
	    		publish_title = fetchLabel('label_core_training_management_221');
	    		publish_is_full = true;
	    	  }
	    	  html += getHtmlByParameter(publish_is_full,link13(),'publish','04',publish_title,false,hasContentMain);
	      }else{
	    	  //分数与结训条件
	    	  var is_full_03 = true;
	    	  if(rmdCompletionCriteriaSettings == true || is_add == true){
	    		 is_full_03 = false;
	    	  }
	   		  html += getHtmlByParameter(is_full_03,link03(),'03','04',fetchLabel('label_core_training_management_386'),false,null,space_img);
  	      //发布
	   		  var  publish_title = fetchLabel('global_publish');
	    	  var  publish_is_full = false;
	    	  if(itm_status == 'ON' || itm_status == 'ALL'  || itm_status == 'TARGET_LEARNER'){
	    		publish_title = fetchLabel('label_core_training_management_221');
	    		publish_is_full = true;
	    	  }
	    	  html += getHtmlByParameter(publish_is_full,link13(),'publish','05',publish_title,false,hasContentMain); 
	      }
		 //处理报名
		  html += getHtmlByParameter(false,link111,'111','',fetchLabel('label_core_training_management_234'),false,hasEnrollMain); 
		 //学习结果管理
		  html += getHtmlByParameter(false,learning_result_management_link(),'learning_result_manager','',fetchLabel('label_core_training_management_385'),false,hasContentMain);
		 //更多
		 html += getHtmlByParameter(false,more_function_link(),'more_function','',fetchLabel('label_core_training_management_257'),true,hasContentMain);
 	return html;
 }
 
 //other
 function itm_type_classroom_other(){
		var html = '';
		  //框架信息节点
		  var is_full_01 = true;
	   	  if(is_add == true){
	   		 is_full_01 = false;
	   	  }
	   	  html += getHtmlByParameter(is_full_01,link01(),'01','01',fetchLabel('label_core_training_management_315'),false,null,space_img);
	      //网上学习内容
	 	  var is_full_05 = true;
	 	  if(rmdOnlineContent == true || is_add == true){
	 		 is_full_05 = false;
	 	  }
	 	  html += getHtmlByParameter(is_full_05,link05(),'05','02',fetchLabel('label_core_training_management_237'),false,hasContentMain,space_img);
	  	  //发布
	      var  publish_title = fetchLabel('global_publish');
	   	  var  publish_is_full = false;
	   	  if(itm_status == 'ON' || itm_status == 'ALL'  || itm_status == 'TARGET_LEARNER'){
	   		publish_title = fetchLabel('label_core_training_management_221');
	   		publish_is_full = true;
	   	  }
	   	 /* html += getHtmlByParameter(publish_is_full,link13(),'publish','03',publish_title,false,hasContentMain,space_img); 
	   	  //评论管理
	   	  var link_118 ='javascript:void(0)';
	   	  if(is_add != true && hasContentMain == true){
	   		link_118 = link118;
	   	  }
	   	  html += getHtmlByParameter(false,link_118,'118','04',fetchLabel('label_core_training_management_244'),true,hasItmCosMain); 
	    */ 
	   	  return html;
	}
 
//讲师导航模板
 function role_for_teacher(){
		var html = '';
		//网上内容
		if(itm_type == 'CLASSROOM' ||  itm_type == 'SELFSTUDY'){
			var link = ''
			if(hasTeachingCourse == true){
				link = 'javascript:course_lst.edit_cos('+cos_res_id+',"'+itm_type+'",'+create_run_ind+',"'+content_def+'",'+has_mod+')';
			}
		   html += getSecondHtmlByParameter(link,102,'wsneirongs',fetchLabel('label_core_training_management_237'),hasTeachingCourse);
		}
		if(itm_type == 'INTEGRATED'){
			var link = ''
			if(hasTeachingCourse == true){
				link = 'javascript:itm_lst.get_course_list('+itm_id+')';
			}
		   html += getSecondHtmlByParameter(link,'102','wsneirongs',fetchLabel('label_core_training_management_237'),hasTeachingCourse);
		}
		//日程表
		if(itm_type == 'CLASSROOM'){
			var link = '';
			if(hasItmCosMain == false){
				if(run_ind == false){
					link = 'javascript:itm_lst.ae_get_run_lesson('+itm_id+')';
				}else{
					link = 'javascript:itm_lst.ae_get_course_lesson('+itm_id+')';
				}
			}
		  html += getSecondHtmlByParameter(link,'108','rcbiaos',fetchLabel('label_core_training_management_247'),true);
		}
		//计分项目
		if(itm_type == 'CLASSROOM' ||  itm_type == 'SELFSTUDY'){
			var link = '';
			if(hasTeachingCourse == true){
					link = 'javascript:itm_lst.ae_get_run_lesson('+itm_id+')';
			}
		  html += getSecondHtmlByParameter(link,'103','jfguizes',fetchLabel('label_core_training_management_467'),hasTeachingCourse);
		}
		if(itm_type == 'CLASSROOM' ||  itm_type == 'SELFSTUDY'){
			var link = '';
			if(hasTeachingCourse == true){
					link = 'javascript:itm_lst.ae_get_run_lesson('+itm_id+')';
			}
		  html += getSecondHtmlByParameter(link,'103','jfguizes',fetchLabel('label_core_training_management_240'),hasTeachingCourse);
		}
		//先修模块设置
		if(itm_type == 'CLASSROOM' ||  itm_type == 'SELFSTUDY'){
			var link = '';
			if(hasTeachingCourse == true){
					link = 'javascript:itm_lst.get_mod_pre('+itm_id+')';
			}
		  html += getSecondHtmlByParameter(link,'104','xxmkshezhi',fetchLabel('label_core_training_management_238'),hasTeachingCourse);
		}
		//课程公告
		if(itm_type == 'CLASSROOM' || itm_type == 'SELFSTUDY'  || itm_type == 'INTEGRATED'){
			var link = '';
			if(hasTeachingCourse == true){
					link = 'javascript:ann.sys_lst("all","RES","'+cos_res_id+'","","","","","",true)';
			}
			var title = '';
			if(itm_exam_ind == true){
				title = fetchLabel('label_core_training_management_236');
			}else{
				title = fetchLabel('label_core_training_management_236');
			}
		  html += getSecondHtmlByParameter(link,'110','kcgonggao',title,hasTeachingCourse);
		}
		if(!(itm_type == 'CLASSROOM' && create_run_ind ==true)){
			//结训记录
			if((itm_type == 'CLASSROOM' &&  run_ind == true )  || itm_type == 'INTEGRATED' || itm_type == 'SELFSTUDY'){
				var link = '';
				if(hasTeachingCourse == true){
						link = 'javascript:attn.get_grad_record('+itm_id+')';
				}
			  html += getSecondHtmlByParameter(link,'113','jxjilu',fetchLabel('label_core_training_management_242'),hasTeachingCourse);
			}
			//计分记录
			if((itm_type == 'CLASSROOM' &&  run_ind == true ) || itm_type == 'SELFSTUDY'){
				var link = '';
				if(hasTeachingCourse == true){
						link = 'javascript:attn.get_grad_record('+itm_id+')';
				}
			  html += getSecondHtmlByParameter(link,'114','jxjilu',fetchLabel('label_core_training_management_241'),hasTeachingCourse);
			}
			//签到记录
			if(itm_type == 'CLASSROOM' && run_ind == true){
				var link = '';
				if(hasTeachingCourse == true){
						link = 'javascript:attn.get_qiandao_Lst('+itm_id+',0)';
				}
			  html += getSecondHtmlByParameter(link,'115','rcbiaos',fetchLabel('label_core_training_management_270'),hasTeachingCourse);
			}
			//出席率
			if(itm_type == 'CLASSROOM' && run_ind == true){
				var link = '';
				if(hasTeachingCourse == true){
						link = 'javascript:javascript:attn.get_attd_rate_lst('+itm_id+')';
				}
			  html += getSecondHtmlByParameter(link,'116','cxlv',fetchLabel('label_core_training_management_252'),hasTeachingCourse);
			}
			//跟踪报告
			if((itm_type == 'CLASSROOM' &&  run_ind == true ) || itm_type == 'SELFSTUDY'){
				var link = '';
				if(hasTeachingCourse == true){
						link = 'javascript:rpt.open_cos_lrn_lst('+itm_id+')';
				}
			  html += getSecondHtmlByParameter(link,'117','gzbaogao',fetchLabel('label_core_training_management_243'),hasTeachingCourse);
			}
			//测评报告
			if((itm_type == 'CLASSROOM' &&  run_ind == true ) || itm_type == 'INTEGRATED' || itm_type == 'SELFSTUDY'){
				var link = '';
				if(hasTeachingCourse == true){
						link = 'javascript:itm_lst.itm_evaluation_report('+cos_res_id+',"TST")';
				}
			  html += getSecondHtmlByParameter(link,'120','jxjilu',fetchLabel('label_core_training_management_393'),hasTeachingCourse);
			}
		}
		
		return html;
	}
 
 //获取一级导航下二级导航信息节点（若没有2级节点则不显示）
 function getSecondNavigationInfo(){
		//var cur_node_id = <%=cur_node_id%>;
		 var html = '';
		 var url = '';
		 if(cur_node_id == '103' || cur_node_id == '03'){
			//<!--结训条件设置 -->
			var link03 = '';
			if(is_add != true && hasContentMain == true){
				link03 = 'javascript:cmt.get_criteria('+itm_id+')';
			}
			html += getSecondHtmlByParameter(link03,'03','jxtiaojians',fetchLabel('label_core_training_management_240'));
			//<!--计分规则-->
			html += getSecondHtmlByParameter(link103,'103','jfguizes',fetchLabel('label_core_training_management_467'));
		 }else if(cur_node_id == '101' ||cur_node_id == '106' || cur_node_id == '111'){
			 //<!--处理报名 -->
			 if((itm_type == 'CLASSROOM' &&  run_ind == true )  || itm_type == 'INTEGRATED' || itm_type == 'SELFSTUDY'){
		     html += getSecondHtmlByParameter(link111,'111','clbaoming',fetchLabel('label_core_training_management_234'));
			 }
			 //<!--报名工作流 -->
			 html += getSecondHtmlByParameter(link101,'101','bmgzuoliu',fetchLabel('label_core_training_management_245'));
			 //<!--目标学员 -->
			 var link106 = '';
			 if(hasItmCosMain == true){
				 link106 = 'javascript:itm_lst.get_target_rule('+itm_id+',"TARGET_LEARNER")';
			 }
			 html += getSecondHtmlByParameter(link106,'106','mbxueyuans',fetchLabel('label_core_training_management_233'));
		 }else if(cur_node_id == '113' || cur_node_id == '114' || cur_node_id == '117' || cur_node_id == '115' || cur_node_id == '116' || cur_node_id == '120' || cur_node_id == '121'){
			 //<!--结训记录 -->
			 html += getSecondHtmlByParameter(link113,'113','jxjilu',fetchLabel('label_core_training_management_138'));
			 //<!--计分记录 -->
			 var link114 = '';
			 if(hasResultMain == true){
				 link114 = 'javascript:cmt.get_scoring_itm_lst('+itm_id+')';
			 }
			 html += getSecondHtmlByParameter(link114,'114','jfjilu',fetchLabel('label_core_training_management_241'));
			 if(itm_type == 'CLASSROOM' && run_ind == true){
				 //<!--签到记录  -->
				 var link115 = '';
				 if(hasResultMain == true){
					 link115 = 'javascript:attn.get_qiandao_Lst('+itm_id+',0)';
				 }
			   html += getSecondHtmlByParameter(link115,'115','rcbiaos',fetchLabel('label_core_training_management_270'));
			   //<!--出席率 -->
			   var link116 = '';
				 if(hasResultMain == true){
					 link116 = 'javascript:attn.get_attd_rate_lst('+itm_id+')';
				 }
			   html += getSecondHtmlByParameter(link116,'116','cxlv',fetchLabel('label_core_training_management_252'));
			 }
			 //<!--跟踪报告 -->
			 var link117 = '';
			 if(hasResultMain == true){
				 link117 = 'javascript:rpt.open_cos_lrn_lst('+itm_id+')';
			 }
			 html += getSecondHtmlByParameter(link117,'117','gzbaogao',fetchLabel('label_core_training_management_243'));
			 //<!--测评结果 -->
			 var link120 = '';
			 if(hasResultMain == true){
				 link120 = 'javascript:itm_lst.itm_evaluation_report('+cos_res_id+',"TST")';
			 }
			 html += getSecondHtmlByParameter(link120,'120','cpjieguo',fetchLabel('label_core_training_management_393'));
            
			 //<!--CPD获得时数-->
		     if(itm_type != 'INTEGRATED' && hasCPT == true){
		    	 html += getSecondHtmlByParameter(link121,'121','cptkhdshishu',fetchLabel('label_core_cpt_d_management_119'));
		     }
		 
		 }else if(cur_node_id == '107' || cur_node_id == '110' || cur_node_id == '105' || cur_node_id == '104' || cur_node_id == '118' || cur_node_id == '112' || cur_node_id == '119' ){
	         if((itm_type == 'CLASSROOM' && create_run_ind == true && content_def == 'PARENT') || (itm_type == 'CLASSROOM' &&  run_ind == true ) || itm_type == 'SELFSTUDY'){
				 //<!--  先修模块设置 -->
				 html += getSecondHtmlByParameter(link104,'104','xxmkshezhi',fetchLabel('label_core_training_management_238'));
	         }
	         if((itm_type == 'CLASSROOM' && create_run_ind == true) || itm_type == 'INTEGRATED' || itm_type == 'SELFSTUDY'){
				 //<!--先修课程 -->
				 var link117 = '';
				 if(hasItmCosMain == true){
					 link117 = 'javascript:itmReq.itm_req_lst('+itm_id+')';
				 }
				 html += getSecondHtmlByParameter(link117,'105','xxkecheng',fetchLabel('label_core_training_management_232'));
	         }
	         if((itm_type == 'CLASSROOM' && run_ind == true )  || itm_type == 'SELFSTUDY'){
	            //<!--课程费用 -->
	            var title107 = fetchLabel('label_core_training_management_235');
	            if(itm_exam_ind == true){
	            	title107 = fetchLabel('label_core_training_management_286');
	            }
			    html += getSecondHtmlByParameter(link107,'107','kcfeiyong',title107);
				 
	         }
			 //<!--课程公告 -->
	         if(itm_type != 'AUDIOVIDEO'){
				 var title110 = fetchLabel('label_core_training_management_236');
		                      if(itm_exam_ind == true){
                 title110 = fetchLabel('label_core_training_management_236');
                }
             }
             if (itm_type != "AUDIOVIDEO"){
                 html += getSecondHtmlByParameter(link110,'110','kcgonggao',title110);
             }
			 
			 if(itm_type == 'CLASSROOM' &&  run_ind == true){
				 var link12 = '';
				 if(hasEnrollMain == true){
					 link12 = 'javascript:itm_lst.itm_ji_msg('+itm_id+')';
				 }
				 //<!--开课通知 -->
				 html += getSecondHtmlByParameter(link12,'112','kktongzhi',fetchLabel('label_core_training_management_254'));
			 }
		     //<!--评论管理 -->
		    /* if(!(itm_type == 'CLASSROOM' &&  create_run_ind == true)){
		    	 html += getSecondHtmlByParameter(link118,'118','plguanli',fetchLabel('label_core_training_management_244'));
		     }*/
		     
		     //<!--CPD 设置所需时数-->
		     if(itm_type != 'INTEGRATED' && hasCPT == true){
		    	 html += getSecondHtmlByParameter(link119,'119','cptssshezhi',fetchLabel('label_core_cpt_d_management_195'));
		     }
		 }
		
		 return html;
	}
    
 //第一级导航栏   
 function getHtmlByParameter(is_full,step_url,id,step_num,step_title,is_last,hasRight,spaceImg){
		
	 //var cur_node_id = <%=cur_node_id%>;
	 var div_class = get_css_class(itm_type,id,step_url,is_full,cur_node_id);
	 //判断是否发布  如果已发布 则显示“√”符号 
	 if(id == 'publish' && (itm_status == 'ON' || itm_status == 'ALL'  || itm_status == 'TARGET_LEARNER')){
			step_num = '';
	 }
	 
	  p = {
       		step_url : step_url,
            div_class  : div_class,
            id : id,
            cur_node_id : cur_node_id,
            step_num : step_num,
            step_title : step_title,
            is_last : is_last,
            space_img : spaceImg
        }
    return  $('#progress_step').render(p);
}
 
 //第二级导航栏 
 function getSecondHtmlByParameter(url,id,icon,title,hasRight){
	 //var cur_node_id = <%=cur_node_id%>;
	 var a_class = '';
	 if(id == cur_node_id){
	  a_class = ' active'
	 }
	  p = {
		   url : url,
		   a_class  : a_class,
           id : id,
           icon : icon,
           title : title
       }
	  if(hasRight == false){
		  return '';
	  }
   return  $('#second_nav_item').render(p);
	
}
 
 //判断是否为当前节点
 function check_is_cur(id,cur_node_id){
		var is_cur = false;
		if(id == '02'){
		   if(cur_node_id == '102'){
			   is_cur = true;
		   }
		}else if(id == '03'){
		   if(cur_node_id == '03' || cur_node_id == '103' ){
	 		   is_cur = true;
	 	   }
		}else if(id == 'enroll_manager'){
			if(cur_node_id == '101' || cur_node_id == '106' || cur_node_id == '111' ){
	 		   is_cur = true;
	 	   }
		}else if(id == 'learning_result_manager'){
			if(cur_node_id == '113' || cur_node_id == '114' || cur_node_id == '117' || cur_node_id == '115' || cur_node_id == '116' || cur_node_id == '120' || cur_node_id == '121'){
	 		   is_cur = true;
	 	   }
		}else if(id == 'more_function'){
			if(cur_node_id == '105' || cur_node_id == '104' || cur_node_id == '107' || cur_node_id == '110' || cur_node_id == '118' || cur_node_id == '112' || cur_node_id == '119' ){
	 		   is_cur = true;
	 	   }
		}else if(id == '118'){
			if(cur_node_id == '118'){
		 		   is_cur = true;
		 	   }
		}
		return is_cur;
	}
 
 //获取样式的类名 
 function get_css_class(itm_type,id,step_url,is_full,cur_node_id){
	 //默认样式
	 var div_class = 'wzb-float wzb-buzhou1';
	//在上面默认类名，不同类型的课程还需要添加额外的类名 
     if(itm_type == 'SELFSTUDY'){
     	 //<!-- 网上课程-->
     }else if(itm_type == 'INTEGRATED'){
			 //<!-- 项目式培训-->	
			 div_class += ' wzb-buzhou4';
	 }else if(itm_type == 'CLASSROOM' && create_run_ind == true){
			 //<!-- 离线课程 -->
			 div_class += ' wzb-buzhou2';	
	 }else if(itm_type == 'CLASSROOM' && run_ind == true ){
			 //<!-- 班级-->
			 div_class += ' wzb-buzhou3';	
	 }else{
			//<!-- 公开课 -->
			div_class += ' wzb-buzhou5';
			}
	
	//<!-- 每个步骤的不同情况下有不同的类名 -->
	
	//<!-- 该节点是否当前的节点，包括其子节点 -->
	var is_cur = check_is_cur(id,cur_node_id);
	if(is_cur == true){ 
		 div_class += ' cur';
		 }
	
	//<!-- 没有URL，即不可点击 -->
	if(step_url == ''){
		div_class += ' wzb-buzhou-no';
	}
	
	//<!-- 有URL，可点击 -->
	if(step_url != '' && step_url !='javascript:void(0)'){
		div_class += ' wzb-hover';
	}
	
	//<!-- 是否实心 -->
	if(is_full == true){ 
		 div_class += ' over';
		 }
	if(id == 'publish' && (itm_status == 'ON' || itm_status == 'ALL'  || itm_status == 'TARGET_LEARNER')){
		div_class += ' wzb-buzhou1_unpublish';
	}else if(id == 'enroll_manager' || id == 'learning_result_manager' || id == 'more_function' || id == '113' || id == '118' || id == '111'){
		div_class += ' wzb-quan-wu';
		if(id == 'more_function'){
			div_class += ' wzb-buzhou-more';
		}else if(id == 'enroll_manager'){
			div_class += ' wzb-buzhou-baominggl';
		}else if(id == 'learning_result_manager'){
			div_class += ' wzb-buzhou-xuexijggl';
		}else if(id == '118'){
			div_class += ' wzb-buzhou-pinglungl';
		}else if(id == '113'){
			div_class += ' wzb-buzhou-jiexunjl';
		}else if(id == '111'){
			div_class += ' wzb-buzhou-chulibm';
		}
	}
	return div_class;
}
 
 //获取第一级导航栏 节点的link
//框架信息 
 function link01(){
 	var link01 = '';
 	if(is_add == true){
 		link01 = 'javascript:void(0)';
 	}else if(run_ind == true){
 		link01 = 'javascript:itm_lst.get_item_run_detail('+itm_id+')';
 	}else{
 		link01 = 'javascript:itm_lst.get_item_detail('+itm_id+')';
 	}
 	return link01;
 }
 //网上学习内容 
 function link05(){
 	var link = '';
 	if(is_add != true && hasContentMain == true){
 		if(itm_type == 'CLASSROOM' && create_run_ind == true && (content_def == 'CHILD' || content_def == '')){
 			link = 'javascript:itm_lst.ae_get_online_content_info('+itm_id+')';
 		}else if(ref_ind == true){
 			link = 'javascript:course_lst.edit_cos("'+cos_res_id+'","'+itm_type+'","'+create_run_ind+'","'+content_def+'","'+has_mod+'")';
 		}else{
 			link = 'javascript:course_lst.edit_cos("'+cos_res_id+'","'+itm_type+'","'+create_run_ind+'","'+content_def+'","'+has_mod+'")';
 		}
 	}	
 	return link;
 }
 //分数与结训条件 
 function link03(){
 	var link = '';
 	if(is_add != true && hasContentMain == true){
      link = 'javascript:cmt.get_criteria('+itm_id+')';
 	}
 	return link;
 }
 //发布 
 function link13(){
 	var link = '';
 	if(hasItmCosMain == true ){
       if(is_add != true && (itm_type != 'CLASSROOM'  || (itm_type == 'CLASSROOM' && content_def != ''))){ 
 		link = 'javascript:itm_lst.get_item_publish('+itm_id+')';
       }
 	}
 	return link;
 }
 //报名管理
 function enroll_management_link(){
 	var link = '';
 	if(is_add != true && itm_type == 'CLASSROOM' &&  create_run_ind == true && content_def == ''){
 		link = 'javascript:void(0)';
 	}else if(is_add != true && itm_type == 'CLASSROOM' && create_run_ind == true){
 	    link = link101;
 	}else if(is_add != true && is_add != true && hasContentMain == true){
 		link = link111;
 	}else{
 		link = 'javascript:void(0)';
 	}
 	return link;
 }
 //学习结果管理
 function learning_result_management_link(){
 	var link = '';
     if(is_add != true && hasContentMain == true){ 
 		link = 'javascript:attn.get_grad_record('+itm_id+')';
 	}else{
 		link = 'javascript:void(0)';
 	}
 	return link;
 }
 //更多
 function more_function_link(){
 	var link = '';
     if(is_add != true && (itm_type != 'CLASSROOM'  || (itm_type == 'CLASSROOM' && content_def != ''))){ 
     	if(itm_type == 'SELFSTUDY' || run_ind == true || (itm_type == 'CLASSROOM' && content_def == 'PARENT' && create_run_ind == true)){
     		if(hasContentMain == true){
     		 link = 'javascript:itm_lst.get_mod_pre('+itm_id+')';
     		}
     	}else{
     		if(hasContentMain == true){
 	   		 link = 'javascript:itmReq.itm_req_lst('+itm_id+')';
 			}
     	}
 	}else{
 		link = 'javascript:void(0)';
 	}
 	return link;
 }
 //课程包
 function link06(){
 	var link = '';
 	if(is_add != true && hasItmCosMain == true){
 		link = 'javascript:itm_lst.get_course_list('+itm_id+')';
 	}
 	return link;
 }
 //内容模式
 function link3(){
 	var link = '';
 	if(is_add != true && hasItmCosMain == true){
 		link = 'javascript:itm_lst.ae_get_online_content_info('+itm_id+')';
 	}
 	return link;
 }
 //日程表
 function link108(){
 	var link = '';
 	if(hasItmCosMain == true){
 		 if(run_ind == true){
 			 link = 'javascript:itm_lst.ae_get_run_lesson('+itm_id+')';
 		 }else{
 			 link = 'javascript:itm_lst.ae_get_course_lesson('+itm_id+')';
 		 }
 	}
 	return link;
 }
 //班级管理
 function link6(){
 	var link = '';
 	if(is_add != true && (content_def == 'CHILD' || content_def == 'PARENT')){
 		link = 'javascript:itm_lst.get_item_run_list('+itm_id+')';
 	}
 	return link;
 }
 