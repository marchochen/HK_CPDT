/**
 *input yyyy-MM-dd hh:mm:dd
 *用于时间格式化的过滤器
 *当时间是今天之前的时候就显示yyyy-MM-dd
 *当时间是****小时/分钟/秒 前
 **/
appModule.filter('dateFormat', ['$filter',
    function($filter) {
        return function(input) {
            if (input) {
                var inputs = input.split(' ');
                var now = new Date();
                var inputDate = new Date();
                inputDate.setFullYear(inputs[0].substring(0, inputs[0].indexOf('-')));
                inputDate.setMonth((inputs[0].substring(inputs[0].indexOf('-') + 1, inputs[0].lastIndexOf('-'))) - 1);
                inputDate.setDate(inputs[0].substring(inputs[0].lastIndexOf('-') + 1));
                inputDate.setHours(inputs[1].substring(0, inputs[1].indexOf(':')));
                inputDate.setMinutes(inputs[1].substring(inputs[1].indexOf(':') + 1, inputs[1].lastIndexOf(':')));
                inputDate.setSeconds(inputs[1].substring(inputs[1].lastIndexOf(':') + 1));
                var time = now.getTime() - inputDate.getTime();
                if (time < 0) {
                    time = -time;
                }
                if (time > (1000 * 60 * 60 * 24)) {
                    return inputs[0];
                } else if (time > 1000 * 60 * 60) {
                    return Math.ceil(time / (1000 * 60 * 60)) + ' ' + $filter('translate')('time_hour_ago');
                } else if (time > 1000 * 60) {
                    return Math.ceil(time / (1000 * 60)) + ' ' + $filter('translate')('time_minute_ago');
                } else {
                    return Math.ceil(time / (1000)) + ' ' + $filter('translate')('time_second_ago');
                }
            }
            return '';
        };
    }])
    .filter('liveUrl', function() {//直播详情
        return function(params) {
            var url = 'detail.html?lvId=' + params.lvId;
            //如果需要输入密码
            if (params.lvIsPwd) {
                url = '../live/livePwdAuth.html?lvId=' + params.lvId;
            }
            //点击进去
            if (params.addCourseInd) {
                url = '../live/' + url;
            }
            return 'javascript:clicked("' + url + '", true, "live' + params.lvId + '");';
        };
    })
    /**
     *input yyyy-MM-dd hh:mm:dd
     *用于时间格式化的过滤器
     *把字符串转换成yy-MM-dd
     **/
    .filter('toDate', function() {
        return function(input, format) {
            if (!input) return '';
            if (!format) {
                format = 'yyyy-MM-dd';
            }
            return app.formatDate(app.parseDate(input, format), format);
        }
    }).filter('personUrl', function() {
        return function(input, indexInd) {
            /*var url = 'personal/personal.html?person=' + input;
            if (!indexInd) {
                url = '../' + url;
            }*/
            return 'javascript:void(0);';
        }
    })
    .filter('articleUrl', function() {
        return function(input, annotherTnd) {
            var url = 'article/articleDetail.html?article=' + input;
            if (annotherTnd) {
                url = '../' + url;
            }
            return 'javascript:clicked("' + url + '",true);';
        };
    })
    .filter('evaUrl', function() {
        return function(input) {
            var url = 'evaluation/detail.html?evaluation=' + input;
            return 'javascript:clicked("' + url + '",true);';
        };
    })
    .filter('knowUrl', function() {
        return function(know) {
            var url = 'detail.html?type=' + know.que_type + '&id=' + wbEncryptor.encrypt(know.que_id);
            //从其他目录点击进去
            if (know.annotherTnd) {
                url = '../know/' + url;
            }
            return 'javascript:clicked("' + url + '",true);';
        };
    })
    .filter('instructorUrl', function() {
        return function(user) {
            var url = 'detail.html?id=' + user.iti_ent_id;
            //从其他目录点击进去
            if (user.annotherTnd) {
                url = '../instructor/' + url;
            }
            return 'javascript:clicked("' + url + '",true);';
        };
    })
    .filter('courseUrl', function() {
        return function(params) {
            var url = 'detail.html?itmId=' + params.itmId;
            //首页点击进去
            if (params.addCourseInd) {
                url = 'course/' + url;
            }
            //从考试等二级目录点击进去
            if (params.examInd) {
                url = '../course/' + url;
            }
            if (params.tkhId) {
                url += '&tkhId=' + params.tkhId;
            }
            return 'javascript:clicked("' + url + '", true, "item' + params.itmId + '");';
        };
    })
    .filter('openCourseUrl', function() {
        return function(params) {
            var url = 'openDetail.html?itmId=' + params.itmId;
            //从父亲目录点击进去
            if (params.fatherCatalogInd) {
                url = 'course/' + url;
            }
            if (params.annotherTnd) {
                url = '../course/' + url;
            }
            return 'javascript:clicked("'+url+'",true,"",true)';
        };
    })
    .filter('moduleUrl', function($filter) {
        return function(params) {
        	var app = params.app;
        	var appOK = app && app.app_id > 0 && app.app_status == 'Admitted';
        	var resources = params.item.resources;
            var res_subtype = resources.res_subtype;
            var rcov = params.item.rcov;
            var mod = resources.mod;
            var mov = resources.mov;
            var curDate = params.curDate;
            var timeValide = params.timeValide;
            
            //是否在网上学习时间内
            var is_time_valide;// boolean类型
            //作业提交截止时间:未指定(默认选项)
            if(mod.ass_due_datetime == undefined && mod.ass_due_date_day == 0){
            	is_time_valide = (app && timeValide && app.app_id > 0 && mod && mod.mod_eff_start_datetime <= curDate && mod.mod_eff_end_datetime >= curDate);
            }
            //作业提交截止时间:指定年月日和时分(选项1)
            if(mod.ass_due_datetime != undefined && mod.ass_due_date_day == 0){
            	if(mod.mod_eff_end_datetime != "9999-12-31 23:59:59" && mod.ass_due_datetime >= mod.mod_eff_end_datetime ){
            		is_time_valide = (app && timeValide && app.app_id > 0 && mod && mod.mod_eff_start_datetime <= curDate && mod.mod_eff_end_datetime >= curDate);
            	}else{
            		is_time_valide = (app && timeValide && app.app_id > 0 && mod && mod.mod_eff_start_datetime <= curDate && mod.ass_due_datetime >= curDate);
            	}
            }
            //作业提交截止时间:?天(从课程开始日期算起)(选项2) //经测试其实是从学员报名时间日期算起的。
            if(mod.ass_due_date_day != undefined && mod.ass_due_datetime != undefined && mod.ass_due_date_day > 0){
            	if(mod.mod_eff_end_datetime != "9999-12-31 23:59:59" && mod.ass_due_datetime >= mod.mod_eff_end_datetime ){
            		is_time_valide = (app && timeValide && app.app_id > 0 && mod && mod.mod_eff_start_datetime <= curDate && mod.mod_eff_end_datetime >= curDate);
            	}else{
            		is_time_valide = (app && timeValide && app.app_id > 0 && mod && mod.mod_eff_start_datetime <= curDate && mod.ass_due_datetime >= curDate);
            	}
            }
            //考试页面的处理方法
            if(mod.ass_due_datetime == undefined && mod.ass_due_date_day == undefined){
            	is_time_valide = (app && timeValide && app.app_id > 0 && mod && mod.mod_eff_start_datetime <= curDate && mod.mod_eff_end_datetime >= curDate);
            }
            
            //学习状态还是进行中状态
            var is_inprogress = (!rcov || rcov.cov_status == 'I');
        	//是否已完成先修模块
        	var completed_pre_mod = (mod && (mod.pre_mod_had_completed || mod.pre_mod_had_completed == undefined || mod.pre_res_title == undefined));
            
        	if (!params.previewInd) {
            	
            	//如果还没报名，提示要去报名
                if (!appOK) {
                    return 'javascript:alertText("' + $filter('translate')('mod_must_app') + '");';
                }
                
                //如果已报名，但是超过了时间的有效期，提示网上内容已过期
                if (appOK && is_inprogress && completed_pre_mod && !is_time_valide) {
                    return 'javascript:alertText("' + $filter('translate')('mod_end_notice') + '");';
                }
                
                //在如果进行中状态，如果有先修模块还没有完成，给出提示
                if (is_inprogress && mod && mod.pre_res_title && !mod.pre_mod_had_completed) {
                    var alertText = "";
                    var obj_mod_status = mod.rrq_status;
                    var obj_mod_type = mod.mod_type;
                    if(obj_mod_type == 'VOD' || obj_mod_type == 'RDG' || obj_mod_type == 'REF') {
        				if(obj_mod_status == 'I'){
        					//您必须先查阅
                            alertText = $filter('translate')('mod_prep_requeid_participate', {
                                value: mod.pre_res_title
                            });
        				} else {
        					//您必须先完成
                            alertText = $filter('translate')('mod_prep_requeid_cp', {
                                value: mod.pre_res_title
                            });
        				}
        			}else if(obj_mod_type == 'SCO' || obj_mod_type == 'AICC_AU' ) {
        				if(obj_mod_status == 'I'){
        					 //您必须先尝试（中文為查阅）
                            alertText = $filter('translate')('mod_prep_requeid_attempt', {
                                value: mod.pre_res_title
                            });
        				} else {
        					//您必须先完成
                            alertText = $filter('translate')('mod_prep_requeid_cp', {
                                value: mod.pre_res_title
                            });
        				}
        			} else if(obj_mod_type == 'ASS' || obj_mod_type == 'DXT' || obj_mod_type == 'TST'){
        				if(obj_mod_status == 'I' ){
        					//您必须先提交
                            alertText = $filter('translate')('mod_prep_requeid_submit', {
                                value: mod.pre_res_title
                            });
        				} else {
        					//你必须先通过 ,才能学习该模块
                            alertText = $filter('translate')('mod_prep_requeid_pass', {
                                value: mod.pre_res_title
                            });
        				}
        			} else if(obj_mod_type == 'SVY'){
        				//您必须先提交
                        alertText = $filter('translate')('mod_prep_requeid_submit', {
                            value: mod.pre_res_title
                        });
        			}else{
        				//默認您必须先学完
                        alertText = $filter('translate')('mod_prep_requeid', {
                            value: mod.pre_res_title
                        });
        			}
                    
                    alertText = alertText.replace(/'/g, "\"");
                    return 'javascript:alertText(\'' + alertText + '\');';
                }
            }
            var html = "";
            
            if(!window.wbModeEnd){
            	/**
                 * 判断在线模块学习时间是否结束
                 * mod_end_time: 模块在线学习结束时间
                 * itm_content_end_date: 课程线学习结束时间
                 */
            	window.wbModeEnd = function(mod_end_time,itm_content_end_date){
                	var result = false;
                	var curDate = new Date;
                	//判断在线模块学习时间是否结束
                	if(mod_end_time !== undefined && mod_end_time !== '' &&  mod_end_time !== 'null' &&  mod_end_time !== 'NULL'){
                		
                		if(mod_end_time.length < 11){
                		//当日期格式只有yyyy-YY-dd时
                			mod_end_time = mod_end_time + ' 23:59:59';
                		}
                		
                		mod_end_time = mod_end_time.replace(/-/g,"/");
                		var end = new Date(mod_end_time );
                		if(end <= curDate){
                			javascript:alertText($filter('translate')('warning_end_notice'));
                			result = true
                		}
                	}
                	
                	//判断课程学习时间是否结束
                	if(itm_content_end_date !== undefined && itm_content_end_date !== '' &&  itm_content_end_date !== 'null' &&  itm_content_end_date !== 'NULL'){
                		if(itm_content_end_date.length < 11){
                		//当日期格式只有yyyy-YY-dd时
                			itm_content_end_date = itm_content_end_date + ' 23:59:59';
                		}
                		itm_content_end_date = itm_content_end_date.replace(/-/g,"/");
                		var end = new Date(itm_content_end_date );
                		if(end <= curDate){
                			javascript:alertText($filter('translate')('warning_end_notice'));
                			result = true
                		}
                	}
                	return result;
                }
            }
            
            if(!window.methodProxy){
            	/**
                 * 执行方法的代理，执行目标方法前校验模块内容的时间有效性
                 */
            	window.methodProxy = function(targetMethod){
                	if(wbModeEnd(mod.mod_eff_end_datetime ,params.item.itm_content_eff_end_time)){
                		return;
                	}
                	eval(targetMethod);
                }
            }
            
            var hardwareAccelerated = false;
            switch (res_subtype) {
                case 'DXT':
                    ;
                case 'TST':
                    if(mod && mod.mod_sub_after_passed_ind == 0 && (mov && mov.mov_status && (mov.mov_status == 'C' || mov.mov_status == 'P'))){
    					//模块中设置了已合格不能提交，且学习已合格
    			    	return 'javascript:alertText("' + $filter('translate')('PGR008') + '");';
    				}else if(mod && mov && (mod.mod_max_usr_attempt != 0 && mov.mov_total_attempt >= mod.mod_max_usr_attempt)){
    					//已超过最大提交次数
    					return 'javascript:alertText("' + $filter('translate')('PGR005') + '");';
    				}else{
    					html = '../module/tst_start.html';
    				}
                    break;
                    
                case 'ASS':
             	 //html = '../module/ass_content.html';
                	//判断是否已经提交了作业
                	if(curDate >= mod.mod_eff_start_datetime && curDate <= mod.mod_eff_end_datetime 
                			&& resources.mov && resources.mov.mov_total_attempt > 0 
                			&& (resources.submit_num && resources.submit_num > 0)){
                		html = '../module/ass_submit1_after.html';
					}else{
						html = '../module/ass_submit1.html';
					}
                    break;
                case 'RDG':
                    html = '../module/rdg_content.html';
                    break;
                case 'REF':
                    html = '../module/ref_content.html';
                    break;
                case 'VOD':
                    html = '../module/vod_content.html';
                    hardwareAccelerated = true;
                    break;
                case 'SVY':
                	if( !mov || (mov.mov_status != 'C' && mov.mov_status != 'P') ){
                		html = '../evaluation/detail.html?from=course';
                	}else{
                		return 'javascript:void(0);';
                	}
                    
                    break;
                case 'AICC_AU':
                    var url = getAiccAuUrl(resources.res_src_link, params.userEntId, params.resId, params.modId, params.tkhId, resources.mod.mod_vendor)
                    var parent_url = window.location.href;
                    var back_btn = $filter('translate')('btn_back');
                    var targetMethod = "javascript:webViewOpenOtherSiteUrl('" + url + "', '" + parent_url +"', '" + back_btn + "', '" + resources.mod.mod_test_style + "');void(0);";
                    return targetMethod;
                    break;
                case 'SCO':
                    var url = getScormUrl(resources.mod.mod_test_style,resources.res_src_link,  params.userEntId, params.resId, params.modId, params.tkhId, resources.res_sco_versio)
                    
                    var targetMethod = 'javascript:clicked("'+url+'",true,"",true)';
                    return "javascript:methodProxy('"+targetMethod+"')";
                    break;
            }
            var query = "";
            for (var name in params) {
                if (res_subtype == 'SVY' && name == 'itmTitle') {
                    query += "&" + name + "=" + encodeURIComponent(params[name]).replace(/%/g, "[|]");
                    continue;
                }
                
                if(name === "item" || name === "app" || name === "curDate" || name === 'itmTitle' || typeof(params[name]) === "object"){
            		continue;
            	}
                
                query += "&" + name + "=" + params[name];
            }
            if (html.indexOf("?") > 0) {
                html += query;
            } else {
                html += "?" + query.substring(1);
            }
            if(hardwareAccelerated){
            	var targetMethod = 'javascript:clicked("'+html+'",true,"",true)';
                return "javascript:methodProxy('"+targetMethod+"')";
            }else{
            	var targetMethod = 'javascript:clicked("' + html + '",true);';
                return "javascript:methodProxy('"+targetMethod+"')";
            }
        };
    })
    .filter('groupUrl', function() {
        return function(input) {
            var url = '../group/detail.html?groupId=' + input;
            return 'javascript:clicked("' + url + '",true);';
        };
    })
    .filter('appUrl', function() {
        return function(params, hasIdInd) {
            url = params.url;
            var query = "";
            for (var name in params) {
                if (name != 'url') {
                    query += "&" + name + "=" + params[name];
                }
            }
            if (query != "") {
                if (url.indexOf("?") > 0) {
                    url += query;
                } else {
                    url += "?" + query.substring(1);
                }
            }
            if (hasIdInd) {
                var id = '';
                switch (params.url) {
                    case 'personal.html':
                        id = 'personal' + params.person;
                        break;
                    case 'group/main.html':
                        id = 'groupMain';
                        break;
                    case 'course/index.html':
                        ;
                    case 'exam/index.html':
                        id = 'courseIndex';
                        break;
                    case 'setUp/setMain.html':
                        id = 'setMain';
                        break;
                }
                return 'javascript:clicked("' + url + '", true, "' + id + '");';
            }
            return 'javascript:clicked("' + url + '",true);';
        }
    })
    .filter('assSubmitUrl',function($filter) {
    	
    	return function(params){
    		var app = params.app;
            var appOK = app && app.app_id > 0 && app.app_status == 'Admitted'
        	if (!appOK) {
                return 'javascript:alertText("' + $filter('translate')('mod_must_app') + '");';
            }
            
            var url = '../module/ass_submit1.html?itmId='+params.itmId+'&tkhId='+params.tkhId+'&modId='+params.modId;
            
            return 'javascript:clicked("' + url + '",true);';
    	};
    	
    })
    .filter('kbDetailUrl', function() {
        return function(input, annotherTnd) {
            var url = 'knowledge/detail.html?kbiId=' + input;
            if (annotherTnd) {
                url = '../' + url;
            }
            return 'javascript:clicked("' + url + '",true);';
        };
    })
    .filter('htmlDecode', ['$sce', function($sce) {
        return function(value) {
            html = !value ? value : String(value).replace(/&amp;/g, "&").replace(/&gt;/g, ">").replace(/&lt;/g, "<").replace(/&quot;/g, '"').replace(/&#34;/g, '"');
            if (serverHost != "" && html && String(html).indexOf("src=\"/") > 0) {
                html = String(html).replace(/src="\//g, "src=\"" + serverHost + "/");
            }
            return $sce.trustAsHtml(html);
        }
    }])
    //英文序号
    .filter('enNo', function() {
        return function(value) {
            return String.fromCharCode(65 + value);
        }
    })
    //文件图片
    .filter('typeImg', function() {
        return function(value) {
            var type = value.substring(value.lastIndexOf(".") + 1, value.length).toLowerCase();
            var allType = 'docx~doc~pptx~ppt~txt~htm~html~swf~gif~jpg~jpge~png~exe~xls~xlsx~pdf';
            if (allType.indexOf(type) < 0) {
                type = 'unknow';
            }
            if(type == "txt"){
            	type = 'notepad';
            }else if(type == "doc" || type == "docx"){
            	type = 'word';
            }else if(type == "ppt" || type == "pptx"){
            	type = 'ppt';
            }else if(type == "xls" || type == "xlsx"){
            	type = 'xls';
            }else if(type == "jpg" || type == "jpge" || type == "png"){
            	type = 'jpg';
            }else if(type == "htm" || type == "html"){
            	type = 'web_browser';
            }
            return type;
        }
    })
    //文字过长的处理
    .filter('textFilter', function() {
        return function(input, length) {
            if (!input || input == '') return;
            var msg = '';

            if (!length || length >= app.getChars(input)) msg = input;
            if (length && app.getChars(input) > length) {
                msg = app.substr(input, 0, length);
                msg += '...';
            }
            return msg;
        }
    })
    //时间格式化
    .filter('formatTimeRemain', function() {
        return function(timeRemain) {
            var secPart = Number(timeRemain) % 60;
            var minPart = parseInt(timeRemain / 60) % 60;
            var hourPart = parseInt(timeRemain / 3600);
            var result = '';
            if (hourPart > 0) {
                if (hourPart < 10) {
                    result = result + '0';
                }
                result = result + hourPart + ':';
            }

            if (minPart < 10) {
                result = result + '0';
            }
            result = result + minPart + ':';

            if (secPart < 10) {
                result = result + '0';
            }
            result = result + secPart;

            return result;
        }
    })
    .filter('votingUrl', function() {
        return function(voting,type) {
        	
        	var enc_vot_id = wbEncryptor.encrypt(voting.vot_id);
        	
        	var url = "";
        	if("ing" === type){
        		if(voting.voteResponse){
        			url = "viewResult.html?vot_id="+enc_vot_id;
        		}else{
        			url = "vote.html?vot_id="+enc_vot_id;
        		}
        	}else {
        		url = "viewResult.html?vot_id="+enc_vot_id;
        	}
        	return 'javascript:clicked("' + url + '",true);';
        };
    })
    .filter('msgDetailUrl', function() {
        return function(msg,type) {
        	msg.notClickable = false;
        	var url = "detail.html?msgId="+msg.wmsg_id+"&type="+type;
        	return 'javascript:clicked("' + url + '",true);';
        };
    });
