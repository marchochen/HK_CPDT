<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title></title>
<script type="text/javascript" src="${ctx}/static/js/jquery.js"></script>
<script type="text/javascript" src="${ctx}/static/js/jsrender.js"></script>
<link rel="stylesheet" href="${ctx}/static/js/bootstrap/css/bootstrap.css"/>
<link rel="stylesheet" type="text/css" href="${ctx}/static/js/font-awesome/css/font-awesome.min.css"/>
<link rel="stylesheet" type="text/css" href="${ctx}/static/js/jquery-table/css/jquery-table-default.css"/>
<link rel="stylesheet" type="text/css" href="${ctx}/static/js/jquery-table/css/jquery-table-learner.css"/>
<link rel="stylesheet" type="text/css" href="${ctx}/static/css/base.css"/>
<link rel="stylesheet" type="text/css" href="${ctx}/static/css/learner.css"/>
<link rel="stylesheet" type="text/css" href="${ctx}/static/theme/blue/css/style.css"/>
<link rel="stylesheet" type="text/css" href="${ctx}/static/css/exam.css"/>

<script type="text/javascript">
	var tkhId = '${tkh_id }';
	var modId = '${mod_id }';
	var totalScore = 0;
	var getScore = 0;
	var attempt = 0;
	var attempt_list;
	var pass_percent;
	var pass_score = 0;
	var full_score = 0;
	var total_attempt_nbr = 0;
	var max_score_pgr_attempt = 0;
	
	var params = {
		isMobile : false,  
		tkh_id : tkhId,
		mod_id : modId
	};

	showAttemptDetail(0,true); //默認0,會查詢最高次數.true
	
	function showAttemptDetail(attempt,flag){
		$.ajax({
			url: '/app/module/getTstScore/'+ attempt,
			dataType: 'json',
			type: 'POST',
			data: params,
			success: function(data){
				var attempt_nbr = data.rows[0].pgr_attempt_nbr;//考试次数
				var total_score = Math.round(data.rows[0].pgr_score); //考试得分
				var completion_status = data.rows[0].pgr_completion_status; 
				var incorrect_cnt = 0;//答错数目
				var correct_cnt = 0; //答对数目
				var not_score_cnt = 0; //未评分数目
				var partially_correctt_cnt = 0;//部分正确
				var rows;
				
				if(flag){
					max_score_pgr_attempt = data.rows[0].pgr_attempt_nbr;//考试最高分的次数
					total_attempt_nbr = data.rows[0].pgr_attempt_nbr;//总次数
					full_score = data.rows[0].pgr_max_score; //总分
					pass_score = data.rows[0].pgr_max_score * data.rows[0].mod.mod_pass_score / 100; //合格分数
					pass_percent = data.rows[0].mod.mod_pass_score  + "%";//合格百分率
					
					var arrayObj = new Array();
					for(var i=0;i<attempt_nbr;i++){
						arrayObj.push(i+1);
					}
					attempt_list = arrayObj;
				}
				
				for(var i=0;i<data.rows.length;i++){
					if(data.rows[i].pgr_correct_ind == 0){
						incorrect_cnt = data.rows[i].pgr_total;
					} else {
						correct_cnt = data.rows[i].pgr_total;
					}
				}
				
				//最高分数
				$.ajax({ url: '/app/module/getMaxProgressAttempt', type: 'POST', dataType: 'json', data: params,success: 
					function(data){
						if(data.progressList != undefined && data.progressList.length > 0){
							max_score_pgr_attempt = data.progressList[0].pgr_attempt_nbr;//分数最高的次数
						}
						
						//未评分
						$.ajax({ url: '/app/module/selectNotScore?attempt='+attempt_nbr, type: 'POST', dataType: 'json', data: params, success: 
							function(data){
								not_score_cnt = data.long;
								incorrect_cnt = incorrect_cnt - data.long;
								
								var p = {
										attempt_nbr : attempt_nbr,//考试次数
										max_score_pgr_attempt : max_score_pgr_attempt,//考试最高分的次数
										total_score : total_score,  //考试得分
										completion_status : completion_status, 
										full_score : full_score, //总分
										pass_score : pass_score, //合格分数
										pass_percent : pass_percent,//合格百分率
										incorrect_cnt : incorrect_cnt,//答错数目
										partially_correctt_cnt : partially_correctt_cnt,
										correct_cnt : correct_cnt, //答对数目
										not_score_cnt : not_score_cnt, //未评分数目
										attempt_list : attempt_list, //次数集合
										total_attempt_nbr : total_attempt_nbr
								}
								$("#report-head").html($('#reportHeadTemplate').render(p));
								
								/**
								 * 获取每个题目考试题目对错情况
								 */
								var ansparams = { isMobile : false, tkh_id : tkhId, mod_id : modId, attempt : attempt_nbr }
								$.ajax({ url: '/app/module/getAllAnswerDetail', type: 'POST',dataType: 'json', data: ansparams, success:
									function(data){
										rows = data.rows; //题目
									}
								});
								
								/**
								 * 获取考试答题详情
								 */
								var que_id_list = [0];
								var tstparams = { isMobile : false, tkh_id : tkhId, mod_id : modId, que_id_lst : que_id_list, attempt : attempt_nbr};
								$.ajax({ url: '/app/module/getTstReportDetail', type: 'POST', dataType: 'json', data: tstparams, success:
									function(data){
										if(data.hashMap.student != undefined){
										    
											var question = data.hashMap.student[0].test[0].body[0].question;
											var result = data.hashMap.student[0].test[0].body[0].result;
											
											//计算我的每题得分
											$(result).each(function(index){
												var total_result_score = 0;
												if(this.interaction && this.interaction.length > 0){
													$(this.interaction).each(function(){
														total_result_score += Number(this.usr_score);
													});
												}
												if(total_result_score == -1){
													total_result_score = 0;
												}
												question[index].body[0].total_result_score = total_result_score;
											});
											
											$(question).each(function(index){
												var total_que_score = 0;
												if(this.body[0].interaction && this.body[0].interaction.length > 0){
													$(this.body[0].interaction).each(function(){ //计算总分
														total_que_score += Number(this.score);
													});
													
													if(this.header[0].type == "MC" || this.header[0].type == "TF" ){//将id转换成字母
														if(this.body[0].interaction[0].option && this.body[0].interaction[0].option.length > 0){
															$(this.body[0].interaction[0].option).each(function(){
																this.letter_option = numConvertLetter(this.id - 1);
															});
														}
													}
												}
												
												question[index].body[0].total_que_score = total_que_score;//总分
												
												//问答题 (得到了分数但没得到满分，将那题改为部分正确的)
												if(this.header[0].type=='ES' && question[index].body[0].total_result_score > 0 && question[index].body[0].interaction && question[index].body[0].interaction[0].score > question[index].body[0].total_result_score){
													rows[index].pgr_score = -2;
													correct_cnt = correct_cnt - 1,//答对题目数
													partially_correctt_cnt = partially_correctt_cnt + 1; //部分正确题目数
												}
												//多选题 与  配对题 与 填空题 (有一个正确的答案时，将那题改为部分正确的)
												if((this.header[0].type=='MC') || (this.header[0].type == 'MT' && this.body[0].source != null) || (this.header[0].type == 'FB')){
													var data_source = null;
													if(this.header[0].type=='MC'){
														data_source = this.body[0].interaction[0].option;
													}else if(this.header[0].type == 'MT'){
														data_source = this.outcome;
													}else if(this.header[0].type == 'FB'){
														data_source = this.body[0].interaction;
													}
													var orderid = this.order - 1; //题目顺序
													$(data_source).each(function(i){ //循环所有的选项
														if((question[index].header[0].type=='MC' && (question[index].outcome &&　question[index].outcome[0].logic=='AND' || question[index].outcome[0].logic=='OR') &&　question[index].outcome[0].feedback[i].score != undefined && question[index].outcome[0].feedback[i].score >= 1 && isOk(result,question[index].outcome[0].feedback[i].condition , orderid))
															|| (question[index].header[0].type == 'MT' && result[orderid].interaction[i].response && this.feedback[0].condition == result[orderid].interaction[i].response[0].text) 
															|| (question[index].header[0].type == 'FB' && result[orderid].interaction[i].response && result[orderid].interaction[i].response[0].text.trim() == question[index].explanation[i].rationale[0].condition.trim())){
															//学员存在有答对的答案的时候，题目导航用正确的显示，回答正确的数目加1，回答错误的数目减1
															if(rows[orderid].pgr_score == 0){
																rows[orderid].pgr_score = -2;
																incorrect_cnt = incorrect_cnt - 1,//答错题目数
																partially_correctt_cnt = partially_correctt_cnt + 1; //部分正确题目数
															}
															//学员不存在有答错的答案但并未全部答对的时候，题目导航用部分正确的显示，回答部分正确的数目加1，回答正确的数目减1
															// 配对题部分正确不在此判断 
															 else if(question[index].header[0].type != 'MT' && rows[orderid].pgr_score != -2 &&  question[index].body[0].total_result_score != question[index].header[0].score){
																rows[orderid].pgr_score = -2;
																correct_cnt = correct_cnt - 1,//答对题目数
																partially_correctt_cnt = partially_correctt_cnt + 1; //部分正确题目数
															}  
														}
													
														if(question[index].header[0].type=='MC'){//选择题选项还原被替换掉的字符
															if(this.html && this.html.length > 0){
																replaceHtml(this.html);
															}
														}
														
													});
												}
												
												//还原被替换掉的字符
												if(this.body[0].html && this.body[0].html.length > 0){
													replaceHtml(this.body[0].html);
												}
												
												/* 配对题由于设计原因，配对题是以目标的顺序为准。只保存了目标对应的正确来源，以及某条目标对应了哪条来源。
													找出来源对应所选的目标
												*/
												if(question[index].header[0].type == 'MT' ){
													$(this.body[0].interaction).each(function(i7){ //循环所有的选项
														//学员当前答目标this.body[0].interaction[i7]的所选的来源
														if(result[index].interaction[i7].response){
															//来源id对应该目标
															question[index].body[0].source[0].item[result[index].interaction[i7].response[0].text - 1].source_target = i7;
														}
													});	
												}
												
											});
											
											var param = {
													incorrect_cnt : incorrect_cnt,//答错数目
													correct_cnt : correct_cnt, //答对数目
													not_score_cnt : not_score_cnt, //未评分数目
													partially_correctt_cnt : partially_correctt_cnt,//部分正确
													rows : rows //题目
											}//我的答题分析导航
											$("#report-content-head").html($('#reportContentHeadTemplate').render(param));
											
											if(data.hashMap.mod_showInd == true && data.hashMap.mod_passedInd == true){
												$("#report-detail").show(); 
												$("#no-report-detail").hide();
											 
												var p = {
													question : question,
													result : result,
													isAnwser : isAnwser,
													isOk : isOk,
													getScore : getScore,
													totalScore : totalScore
												}//我的答题分析详情
												$("#report-content").html($('#reportContentTemplate').render(p));
												
											}else{
												$("#report-detail").hide(); 
												if(data.hashMap.mod_passedInd == false){
													$("#no-report-detail").show();//成绩合格才显示的提示语
												}
											}
											
										}else{
											$("#report-detail").hide(); 
											if(data.hashMap.mod_passedInd == false){
												$("#no-report-detail").show();//成绩合格才显示的提示语
												$("#no-report-detail-title").show();
											}else{
												$("#report-head").css("margin-bottom","-8px");
											}
										}
									}
								});
								
							}
						});
						
					}
				});
				
			}
		});
	}
	
	function replaceHtml(html){
		$(html).each(function(bi){
			var txt = this.text;
			if(txt != undefined){
				this.text = txt.replace(/&gt;/g, ">").replace(/&lt;/g, "<").replace(/&amp;/g, "&").replace(/&nbsp;/g, " ");
			}
		});
	}
	
	function isAnwser(result,orderid,ansid){
		var res = result[orderid].interaction[0].response;
		if(res != undefined){
			for (var int = 0; int < res.length; int++) {
				if(res[int].text==ansid){
					return true;
				}
			}
		}
		return false;
	}

	function isOk(result,condition, orderid){
		var res = result[orderid].interaction[0].response;
		if(res != undefined){
			for (var int = 0; int < res.length; int++) {
				if(res[int].text==condition){
					return true;
				}
			}
		}
		return false;
	}
	
	function changeAttemptDetail(attempt){
		showAttemptDetail(attempt,false);
	}
	
	function numConvertLetter(index){
		var array = ['A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'];
		return array[index];
	}
	
	function goToQuestion(num){
	    $("html,body").animate({scrollTop:$("#"+num+"").offset().top-20},1000);
	}
</script>


<script id="reportHeadTemplate" type="text/x-jsrender">
 	<!-- 测验次数start --><!-- 只有一次测验次数不显示这一块 --><!-- 默认显示最高的次数 -->
    <div style="background:#fff;padding:15px 15px 0;">
    	<div class="wzb-title-2"><lb:get key='label_report_total_attempt_nbr_1' /> {{> total_attempt_nbr }} <lb:get key='label_report_total_attempt_nbr_2' /></div>
        <div class="margin-top10">
			{{for attempt_list}}
        		<a id="attempt_nbr_{{: #data}}" onclick="changeAttemptDetail({{: #data}})" class="list-style-fang {{if ~root.attempt_nbr == #data}}active{{/if}}" href="javascript:void(0)">{{: #data}}</a>
			{{/for}}
        </div>
   		
		{{if not_score_cnt > 0}}
			<p style="text-align:center;font-size:18px;padding:15px 0;"><lb:get key='label_report_has_not_score_cnt' /></p><!-- 当存在尚未评分的题目时显示 -->
		{{/if}}
	</div>
    <!-- 测验次数end -->
	
    <!-- 默认显示历史最高分的成绩 -->
    <ul class="wzb-list-34">
    	<li><i class="icon-wdfs"></i><p><lb:get key='label_report_my_score' />：{{> total_score }}</p>{{if max_score_pgr_attempt == attempt_nbr}}<p style="color:#f7a901;line-height:12px;"><lb:get key="label_report_history_max_score" /></p>{{/if}}</li>
        <li><i class="icon-wdzt"></i><p><lb:get key='label_report_my_status' />：{{if not_score_cnt > 0}}<lb:get key='label_report_not_score' />{{else total_score >= pass_score}}<lb:get key='status_passed' />{{else}}<lb:get key='status_flunk' />{{/if}}</p></li>
        <li><i class="icon-sqzf"></i><p><lb:get key='label_report_tst_total_score' />：{{> full_score}}</p></li>
        <li><i class="icon-jgfs"></i><p><lb:get key='label_report_tst_pass_score' />：{{>pass_score}}（{{>full_score}}*{{>pass_percent}}）</p></li>
     </ul>
</script>

<script id="reportContentHeadTemplate" type="text/x-jsrender">
<div class="wzb-title-2" style="padding:15px 15px 0;height:45px;"><lb:get key='label_report_my_answer_analysis' /></div>

<div class="wzb-list-33" id="report-answer-sheet">
    {{include tmpl="#reportAnswerSheet"/}}
</div>

<div style="background:#fff;border-top:1px solid #eee;">
    <div style="padding:15px 15px 10px;">
		{{for rows}}
        	<a id="nav_{{: #getIndex()}}" href="javascript:goToQuestion('dl_{{: #getIndex()+1 }}')" class="xyd-tool-roundnum {{if pgr_score == 0}}bg-color-red{{else pgr_score >= 1}}bg-color-green{{else pgr_score == -1}}bg-color-blue{{else pgr_score == -2}}bg-color-orange{{/if}}">{{: #getIndex()+1}}</a>
		{{/for}}
    </div>
</div>
</script>

<script id="reportAnswerSheet" type="text/x-jsrender">
<ul>
	<li class="xyd-tool-round-1"><lb:get key='label_report_my_answer_questions' />：{{>correct_cnt}}</li>
	<li class="xyd-tool-round-2"><lb:get key='label_report_my_wrong_answer' />：{{>incorrect_cnt}}</li>
	<li class="xyd-tool-round-4"><lb:get key='label_report_partially_correctt' />：{{>partially_correctt_cnt}}</li>
	<li class="xyd-tool-round-3"><lb:get key='label_report_not_score_cnt' />：{{>not_score_cnt}}</li>
</ul>
</script>

<script id="reportContentTemplate" type="text/x-jsrender">
{{for question}}
	<dl class="list list-exam clearfix" id="dl_{{:#getIndex()+1}}">
        <dt style="width:100%;">
            <div class="list_tit">
                <div style="padding-right:5px;">{{:#getIndex()+1}}.<span>[{{:header[0].type == 'MC'? (outcome[0].logic == 'SINGLE'? '<lb:get key="test_mc_single" />' : '<lb:get key="test_mc_and" />') : header[0].type == 'FB' ? '<lb:get key="test_fb" />': header[0].type == 'ES'? '<lb:get key="test_es" />' : header[0].type == 'TF'? '<lb:get key="test_tf" />': header[0].type == 'MT'?'<lb:get key="test_mt" />':'<lb:get key="test_mt" />' }}]</span></div>
            </div>
            <p>
				{{if header[0].type != 'FB'}}
					{{: body[0].html != undefined && body[0].html[0]? body[0].html[0].text : body[0].text}}
				{{/if}}
				{{if header[0].type == 'FB'}}
					{{for body[0].html}}
						<span>
							<span>{{:text}}</span>
							{{if #getIndex() + 1 != #parent.data.length}}
								<span style="padding:0 10px;" data="ans_{{:#parent.parent.parent.data.id}}_{{:#getIndex()}}"> [<lb:get key="label_report_fb" />{{:#getIndex()+1}}] </span>
							{{/if}}
						</span>
					{{/for}}
				{{/if}}
			</p>
		</dt>

		<!--单选或者判断题  -->
		{{if (header[0].type=='MC' && outcome[0].logic=='SINGLE') || header[0].type=='TF'}}
			<dd>
				{{for body[0].interaction[0].option}}
					<div class="list-info">
		                <label class="color-gray666" style="padding:0 0 0 48px;">
							<input disabled="disabled" value="{{:#data.id}}" id="ans_{{:#parent.parent.parent.data.id}}_{{:#getIndex()}}" name="ans_{{:#parent.parent.parent.data.id}}_{{:#parent.parent.parent.data.order}}" {{if ~root.result[#parent.parent.parent.data.order - 1].interaction[0].response && ~root.result[#parent.parent.parent.data.order - 1].interaction[0].response[0].text == #data.id && (#parent.parent.parent.data.outcome[0].logic=='SINGLE' || #parent.parent.parent.data.header[0].type=='TF')}}checked="checked"{{/if}} style="margin:0 0 2px -48px;" class="xsval" type="radio">
							<span style="margin:0 8px 0;">{{: #data.letter_option}}.</span>
							{{if #parent.parent.parent.data.header[0].type=='MC'}}
								<span>{{: html && html.length > 0 && html[0] ? html[0].text : text}}</span>
							{{else #parent.parent.parent.data.header[0].type=='TF'}}
								<span>{{: html && html.length > 0 && html[0] ? html[0].text == 'True'?'<lb:get key="label_report_true" />':'<lb:get key="label_report_false" />' : text == 'True'?'<lb:get key="label_report_true" />':'<lb:get key="label_report_false" />'}}</span>
							{{/if}}
						</label>
						{{if #parent.parent.parent.data.outcome[0].feedback[#getIndex()].score>=1 && ((~root.result[#parent.parent.parent.data.order-1].interaction[0].response && #parent.parent.parent.data.outcome[0].feedback[#getIndex()].condition != ~root.result[#parent.parent.parent.data.order-1].interaction[0].response[0].text) || !~root.result[#parent.parent.parent.data.order-1].interaction[0].response)}}
							<span class="ifzq">(<lb:get key="label_report_true_anwser" />)</span>
						{{/if}}	
						{{if (#parent.parent.parent.data.outcome[0].logic=='SINGLE' || #parent.parent.parent.data.header[0].type=='TF') && ~root.result[#parent.parent.parent.data.order-1].interaction[0].response && ~root.result[#parent.parent.parent.data.order-1].interaction[0].response[0].text== #data.id && #parent.parent.parent.data.outcome[0].feedback[#getIndex()].score==undefined }}		    
							<span class="ifcw">(<lb:get key="label_report_false_anwser" />)</span>
					 	{{/if}}	 
		 				{{if (#parent.parent.parent.data.outcome[0].logic=='SINGLE' || #parent.parent.parent.data.header[0].type=='TF') && ~root.result[#parent.parent.parent.data.order-1].interaction[0].response && ~root.result[#parent.parent.parent.data.order-1].interaction[0].response[0].text== #data.id && #parent.parent.parent.data.outcome[0].feedback[#getIndex()].score>=1 && #parent.parent.parent.data.outcome[0].feedback[#getIndex()].condition == ~root.result[#parent.parent.parent.data.order-1].interaction[0].response[0].text}}
							<span class="ifzq">(<lb:get key="label_report_ok_anwser" />)</span>
						{{/if}}					
						
		            </div>
				{{/for}}
	
	            <div class="margin-top10"><lb:get key="label_report_this_test_score" />： {{: body[0].total_result_score}}/{{: body[0].total_que_score}}</div>
	            {{if header[0].desc[0] && header[0].desc[0].text}} <!--简介  -->
                    <div>
                        <p class="margin-top10 color-gray999">
							<lb:get key="lab_kb_desc" />：{{: header[0].desc[0] && header[0].desc[0].text?header[0].desc[0].text : '--'}}
						</p>
                	</div>
				{{/if}}
	        </dd>
		{{/if}}

		<!-- 多选题 -->
		{{if header[0].type=='MC' && (outcome[0].logic=='AND' || outcome[0].logic=='OR')}}
        	<dd style="width:100%;">
				{{for body[0].interaction[0].option}}
                	<div class="list-info">
                    	<label style="padding:0 0 0 48px;">
							<input disabled="disabled" {{if ~root.isAnwser(~root.result,#parent.parent.parent.data.order-1,id)}}checked="checked"{{/if}} value="{{: #data.id}}" id="ans_{{:#parent.parent.parent.data.id}}_{{:#getIndex()}}" name="ans_{{:#parent.parent.parent.data.id}}_{{:#parent.parent.parent.data.order}}" style="margin:0 0 2px -48px;" class="xsval" type="checkbox">
							<span style="margin:0 8px 0;">{{: #data.letter_option}}.</span>
							<span>{{: html && html.length > 0 && html[0] ? html[0].text : text}}</span>
						</label>
						{{if #parent.parent.parent.data.outcome[0].feedback[#getIndex()].score>=1 && !~root.isOk(~root.result,#parent.parent.parent.data.outcome[0].feedback[#getIndex()].condition , #parent.parent.parent.data.order-1)}}
							<span class="ifzq">(<lb:get key="label_report_true_anwser" />)</span>
						{{/if}}	
						{{if #parent.parent.parent.data.outcome[0].feedback[#getIndex()].score>=1 && ~root.isOk(~root.result,#parent.parent.parent.data.outcome[0].feedback[#getIndex()].condition , #parent.parent.parent.data.order-1)}}
							<span class="ifzq">(<lb:get key="label_report_ok_anwser" />)</span>
						{{/if}}
						{{if #parent.parent.parent.data.outcome[0].feedback[#getIndex()].score==undefined && ~root.isAnwser(~root.result,#parent.parent.parent.data.order-1, #data.id) }}
							<span class="ifcw">(<lb:get key="label_report_false_anwser" />)</span>
					 	{{/if}}	
                     </div>
				{{/for}}

				<div class="margin-top10"><lb:get key="label_report_this_test_score" />： {{: body[0].total_result_score}}/{{: body[0].total_que_score}}</div>
                {{if header[0].desc[0] && header[0].desc[0].text}} <!--简介  -->
	                <div>
		                <p class="margin-top10 color-gray999">
							<lb:get key="lab_kb_desc" />：{{: header[0].desc[0] && header[0].desc[0].text?header[0].desc[0].text : '--'}}
						</p>
	               </div>
				{{/if}}
        	</dd>
		{{/if}}

		<!--填空题  -->
		{{if header[0].type == 'FB'}}
			<dd style="width:100%;">
	             <div class="datatable table-border">
	                 <table class="datatable-table">
	                     <thead class="datatable-table-thead">
	                         <tr class="datatable-table-row">
	                             <th width="10%" class="wzb-form-control" style="border-right:1px solid #fff;background:#eee;"><lb:get key="label_report_fb" /></th>
	                             <th width="40%" class="wzb-form-control" style="border-right:1px solid #fff;background:#eee;"><lb:get key="label_report_your_answer" /></th>
	                             <th width="40%" class="wzb-form-control" style="border-right:1px solid #fff;background:#eee;"><lb:get key="label_report_true_anwser" /></th>
	                             <th width="10%" class="wzb-form-control" style="border-right:1px solid #fff;background:#eee;"><lb:get key="label_report_score" /></th>
	                         </tr>
	                     </thead>
	                     <tbody class="datatable-table-tbody">
							{{for body[0].interaction}}
	                         	<tr>
	                             	<td width="10%" class="wzb-form-control">{{: #getIndex()+1}}</td>
	                             	<td width="40%" class="wzb-form-control">{{if ~root.result[#parent.parent.parent.data.order-1].interaction[#getIndex()].response}}{{: ~root.result[#parent.parent.parent.data.order-1].interaction[#getIndex()].response[0].text}}{{else}}--{{/if}}</td>
	                             	<td width="40%" class="wzb-form-control">{{: #parent.parent.parent.data.explanation[#getIndex()].rationale[0].condition}}</td>
	                             	<td width="10%" class="wzb-form-control">{{: #parent.parent.parent.data.outcome[#getIndex()].score}}</td>
	                         	</tr>
							{{/for}}
	                     </tbody>
	                 </table>
	             </div>
	             <div class="margin-top10"><lb:get key="label_report_this_test_score" />： {{: body[0].total_result_score}}/{{: body[0].total_que_score}}</div>
	             {{if header[0].desc[0] && header[0].desc[0].text}}<!--简介  -->
                 	<div>
                 		<p class="margin-top10 color-gray999">
							<lb:get key="lab_kb_desc" />：{{: header[0].desc[0] && header[0].desc[0].text?header[0].desc[0].text : '--'}}
						</p>
                   	</div>
				 {{/if}}
             </dd>
		{{/if}}

		<!--问答题  -->
		{{if header[0].type == 'ES'}}
			<dd style="width:100%;">
             	<div class="datatable table-border">
             		<table class="datatable-table" style="border-top:1px solid #eee;border-left:1px solid #eee;">
                    	<thead class="datatable-table-thead">
                        	<tr class="datatable-table-row">
                            	<th width="50%" class="wzb-form-control" style="border-right:1px solid #fff;background:#eee;"><lb:get key="label_report_your_answer" /></th>
                                <th width="50%" class="wzb-form-control" style="border-right:1px solid #fff;background:#eee;"><lb:get key="label_report_reference_answer" /></th>
                            </tr>
                        </thead>
                        <tbody class="datatable-table-tbody">
							{{for body[0].interaction}}
                            	<tr>
                                  	<td width="50%" class="wzb-form-control"><!-- 问答题 -->	
                                      	<div>{{if ~root.result[#parent.parent.parent.data.order-1].interaction[0].response}}{{: ~root.result[#parent.parent.parent.data.order-1].interaction[0].response[0].text}}{{else}}--{{/if}}</div>
                                  	</td>
                                  	<td width="50%" class="wzb-form-control"><!-- 问答题答案 -->	
                                      	<div>{{: #parent.parent.parent.data.explanation[0].rationale[0].text?#parent.parent.parent.data.explanation[0].rationale[0].text:'--'}}</div>
                                  	</td>
                              	</tr>
							{{/for}}
                        </tbody>
                    </table>
                </div>
				<div class="margin-top10"><lb:get key="label_report_this_test_score" />：{{: body[0].total_result_score}}/{{: body[0].total_que_score}}</div>
				{{if header[0].desc[0] && header[0].desc[0].text}}
                	<div>
                		<p class="margin-top10 color-gray999">
							<lb:get key="lab_kb_desc" />：{{: header[0].desc[0] && header[0].desc[0].text?header[0].desc[0].text : '--'}}
						</p>
                  	</div>
				{{/if}}
        	</dd>
		{{/if}}

		<!--配对题 -->
		{{if header[0].type == 'MT' && body[0].source != null}}
			<dd style="width:100%;">
				<div class="datatable table-border">
					<table class="datatable-table" style="border-top:1px solid #eee;border-left:1px solid #eee;">
						<thead class="datatable-table-thead">
                        	<tr class="datatable-table-row">
                            	<th width="30%" class="wzb-form-control" style="border-right:1px solid #fff;background:#eee;"><lb:get key="message_From" /></th>
                             	<th width="30%" class="wzb-form-control" style="border-right:1px solid #fff;background:#eee;"><lb:get key="label_report_your_target_answer" /></th>
                                 <th width="30%" class="wzb-form-control" style="border-right:1px solid #fff;background:#eee;"><lb:get key="label_report_true_target_answer" /></th>
                                 <th width="10%" class="wzb-form-control" style="border-right:1px solid #fff;background:#eee;"><lb:get key="label_report_score" /></th>
                            </tr>
                        </thead>
	                    <tbody class="datatable-table-tbody">
							{{for body[0].interaction}}
			                	<tr>
			                    	<td width="30%" class="wzb-form-control">
			                        	<div class="table-content">
											<p>
												<label style="margin-left:10px;">
													{{: #parent.parent.data.body[0].source[0].item[#parent.parent.data.outcome[#getIndex()].feedback[0].condition -1].html 
														&& #parent.parent.data.body[0].source[0].item[#parent.parent.data.outcome[#getIndex()].feedback[0].condition -1].html[0]
														? #parent.parent.data.body[0].source[0].item[#parent.parent.data.outcome[#getIndex()].feedback[0].condition -1].html[0].text:
														#parent.parent.data.body[0].source[0].item[#parent.parent.data.outcome[#getIndex()].feedback[0].condition -1].text[0].text}}
												</label>
				     						</p>
											{{if #parent.data[#parent.parent.data.outcome[#getIndex()].feedback[0].condition -1].object && #parent.data[#parent.parent.data.outcome[#getIndex()].feedback[0].condition -1].object[0] }}
												<img src="${ctx}/resource/{{:#parent.parent.parent.data.id }}/{{:#parent.parent.parent.data.body[0].source[0].item[#parent.parent.parent.data.outcome[#getIndex()].feedback[0].condition -1].object[0].data}}" width="200">
											{{/if}}
										</div>
			                        </td>
			                        <td width="30%" class="wzb-form-control">
			                        	<div class="table-content">
											<div class="table-content">
												<p>
													<label style="margin-left:10px;">
														{{if #parent.parent.parent.data.body[0].source[0].item[#parent.parent.parent.data.outcome[#getIndex()].feedback[0].condition -1].source_target != undefined && #parent.parent.parent.data.body[0].source[0].item[#parent.parent.parent.data.outcome[#getIndex()].feedback[0].condition -1].source_target>=0}}
														{{: #parent.parent.parent.data.body[0].interaction[#parent.parent.parent.data.body[0].source[0].item[#parent.parent.parent.data.outcome[#getIndex()].feedback[0].condition -1].source_target].html 
														&& #parent.parent.parent.data.body[0].interaction[#parent.parent.parent.data.body[0].source[0].item[#parent.parent.parent.data.outcome[#getIndex()].feedback[0].condition -1].source_target].html[0]
														? #parent.parent.parent.data.body[0].interaction[#parent.parent.parent.data.body[0].source[0].item[#parent.parent.parent.data.outcome[#getIndex()].feedback[0].condition -1].source_target].html[0].text:
														#parent.parent.parent.data.body[0].interaction[#parent.parent.parent.data.body[0].source[0].item[#parent.parent.parent.data.outcome[#getIndex()].feedback[0].condition -1].source_target].text[0].text}}
														{{else}}--{{/if}}
													</label>
					     						</p>
												{{if #parent.parent.parent.data.body[0].source[0].item[#parent.parent.parent.data.outcome[#getIndex()].feedback[0].condition -1].source_target != undefined 
													&& #parent.parent.data.body[0].source[0].item[#parent.parent.parent.data.outcome[#getIndex()].feedback[0].condition -1].source_target >= 0 
													&& #parent.data[#parent.parent.parent.data.body[0].source[0].item[#parent.parent.parent.data.outcome[#getIndex()].feedback[0].condition -1].source_target].object 
													&& #parent.data[#parent.parent.parent.data.body[0].source[0].item[#parent.parent.parent.data.outcome[#getIndex()].feedback[0].condition -1].source_target].object[0] }}
													<img src="${ctx}/resource/{{:#parent.parent.parent.data.id }}/{{:#parent.parent.parent.data.body[0].interaction[#parent.parent.parent.data.body[0].source[0].item[#parent.parent.parent.data.outcome[#getIndex()].feedback[0].condition -1].source_target].object[0].data}}" width="200">
												{{/if}}
											</div> 
										</div>
			                        </td>
			                        <td width="30%" class="wzb-form-control">
			                            <div class="table-content">
											<p>
												<label style="margin-left:10px;">{{: html &&　html.length>0 && html[0]? html[0].text : text[0].text}}</label>
				     						</p>
											{{if object && object.length>0}}
												<img src="${ctx}/resource/{{:#parent.parent.parent.data.id }}/{{:object[0].data}}" width="200">
											{{/if}}
										</div>
			                        </td>
			                        <td width="10%" class="wzb-form-control">
			                        	<div class="table-content">
											{{: score}}
										</div>
			                        </td>
			                  	</tr>
							{{/for}}
						</tbody>
			        </table>
			    </div>
	
			    <div class="margin-top10">
					<lb:get key="label_report_this_test_score" />：{{: body[0].total_result_score}}/{{: body[0].total_que_score}}
				</div>
	
				{{if header[0].desc[0] && header[0].desc[0].text}}
			    	<div>
			        	<p class="margin-top10 color-gray999">
							<lb:get key="lab_kb_desc" />：{{: header[0].desc[0] && header[0].desc[0].text?header[0].desc[0].text : '--'}}
						</p>
			    	</div>
				{{/if}}
	  		</dd>
		{{/if}}
   	</dl>
{{/for}}
</script>

</head>
<body>
<%-- <h3 class="wzb-title-1"><lb:get key="label_report_test_report" /></h3> --%>
	<div class="xyd-wrapper">
	    <div class="xyd-main-3 clearfix">
	        
	        <div id="report-head"></div>
	
	        <div style="background:#fff;" >
	            
	            <div id="report-content-head"></div>
				
	            <div style="padding:15px 15px 10px 0;border-top:1px solid #eee;" id="report-detail">
	            
					<div id="report-content"></div>
	
	            </div>
	
	        </div>
	
	        <!-- 每次测验合格后向学员显示试题和答案 -->
	        <div id="no-report-detail" style="background:#fff;display:none;">
	            <div class="wzb-title-2" style="padding:15px 15px 0;height:45px;display:none;" id="no-report-detail-title"><lb:get key="label_report_my_answer_analysis" /></div>
	            <div class="losedata"><i class="fa fa-folder-open-o"></i><p style="font-size:16px;"><lb:get key="label_report_flunk_desc" /></p></div>
	        </div>
	
	    </div> 
	</div>
</body>
</html> 