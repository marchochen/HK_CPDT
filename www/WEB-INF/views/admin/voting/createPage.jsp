<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>

<jsp:include page="../../common/meta.datepicker.jsp"></jsp:include>
<%@ include file="../../common/meta.tree.jsp"%>
<%@ include file="../../common/meta.kindeditor.jsp"%>

</head>
<body>
<input type="hidden" name="belong_module" value="FTN_AMD_DEMAND_MGT"/>

 	<title:get function="global.FTN_AMD_DEMAND_MGT"/>
	<ol class="breadcrumb wzb-breadcrumb">
		<li><a href="javascript:wb_utils_gen_home(true);"><i
				class="fa wzb-breadcrumb-home fa-home"></i> 
				<lb:get key="label_rm.label_core_requirements_management_1" /></a></li>
		<li><a href="${ctx}/app/admin/voting"><!-- 投票 -->
		<lb:get key="label_rm.label_core_requirements_management_2" /></a></li>
		<li class="active">
				<c:choose>
				<c:when test="${type =='update'}">
					<lb:get key="label_rm.label_core_requirements_management_54"/> <!-- 修改投票活动 -->
				</c:when>
				<c:otherwise>
					<lb:get key="label_rm.label_core_requirements_management_16"/><!-- 创建投票活动 -->
				</c:otherwise>
			</c:choose>
		</li>
	</ol>
	<!-- wzb-breadcrumb End-->
	<div class="panel wzb-panel">
		<form id="votingForm" method="post" action="${ctx}/app/admin/voting/${type }Voting" name="votingForm">
		<input type="hidden" value="${v.vot_id }" name="vot_id"/>
		<div class="panel-heading"><!-- 创建投票活动 -->
			<c:choose>
				<c:when test="${type =='update'}">
					<lb:get key="label_rm.label_core_requirements_management_54"/> <!-- 修改投票活动 -->
				</c:when>
				<c:otherwise>
					<lb:get key="label_rm.label_core_requirements_management_16"/> <!-- 创建投票活动 -->
				</c:otherwise>
			</c:choose>
		</div>

		<div class="panel-body">
			<div class="">
				<div class="wzb-title-2 wzb-before"><!-- 基本信息 --><lb:get key="label_rm.label_core_requirements_management_17"/></div>

				<table>
					<tbody>
						<tr>
							<td class="wzb-form-label"><span class="wzb-form-star">*</span><!-- 标题 -->
							<lb:get key="global.global_title"/>：</td>

							<td class="wzb-form-control">
								<div class="wzb-selector" style="width:600px;">
									<input type="text" class="form-control" id="vot_title" name="vot_title" style="width:400px;" value="${v.vot_title}"/>
								</div>
							</td>
						</tr>
						<tr id="tr_content">
							<td class="wzb-form-label" valign="top"><span
								class="wzb-form-star">*</span><!-- 投票内容 --><lb:get key="label_rm.label_core_requirements_management_18"/>：</td>

							<td class="wzb-form-control">
								<textarea id="vot_content"
									style="width: 300px;" class="form-control" name="vot_content">${v.vot_content }</textarea>
									<script type="text/javascript">
										var editor;
										KindEditor.ready(function(K) {
											var temp = {
												allowFileManager : true,
												afterBlur : function(){
								                	//编辑器失去焦点时直接同步，可以取到值
								                    this.sync();
								                }
											}
											minKindeditorOptions.langType = "${label_lan}";
											editor = K.create('#vot_content',$.extend(temp,minKindeditorOptions)
													);
										});
										
									</script>
							</td>
						</tr>

						<tr id="tr_options">
							<td class="wzb-form-label" valign="top"><span
								class="wzb-form-star">*</span><!-- 投票选项 -->
								<lb:get key="label_rm.label_core_requirements_management_20"/>：</td>

							<td class="wzb-form-control">
								<label class="wzb-form-checkbox" for="tabradio-05">
									<input type="radio" name="optionType" id="tabradio-05" value="MC_S"><!-- 单选 --><lb:get key="label_rm.label_core_requirements_management_21" />
								</label> 
								<label class="wzb-form-checkbox" for="tabradio-06">
									<input type="radio" name="optionType" id="tabradio-06" value="MC_M" checked="checked"><!-- 多选 --><lb:get key="label_rm.label_core_requirements_management_22"/>
								</label>
 								<script>
 									var questionType = "${v.voteQuestion.vtq_type}";
 									if(questionType&&questionType!="MC_C"){
 										$("input:radio[name='optionType'][value='"+questionType+"']").attr("checked",true);
 									}
 								</script>
 								
 								<c:forEach items="${v.voteQuestion.voteOptions }" var="vo" >
 									<p>
										<lb:get key="label_rm.label_core_requirements_management_40"/>：
									 	<input value="${vo.vto_desc }" type="text" class="form-control margin-right4"   style="width: 280px;" name="options">
									 	<input type="button" class="btn wzb-btn-blue margin-right4 margin-bottom1" onclick="delOption(this);" value="<lb:get key='global.button_del'/>" style="margin-bottom:3px;">
									</p>
 								</c:forEach>
 								
								<span id="optionContainer"></span>
								<p>
									<input id="creatOptionBtn" type="button"
										class="btn wzb-btn-blue" value="<lb:get key="label_rm.label_core_requirements_management_24"/>">
										（<!-- 最多添加十项选项 -->
										<lb:get key="label_rm.label_core_requirements_management_25"/>）
								</p>
								</td>
						</tr>
					</tbody>
				</table>
			</div>

			<div class="margin-top28">
				<div class="wzb-title-2 wzb-before"><!-- 显示设置 -->
				<lb:get key="label_rm.label_core_requirements_management_26" />
				</div>

				<table>
					<tbody>
						<tr id="tr_trc">
							<td class="wzb-form-label" style="padding-bottom:10px;">
							<span class="wzb-form-star">*</span><!-- 培训中心 -->
							<lb:get key="label_rm.label_core_requirements_management_27" />：
							</td>

							<td class="wzb-form-control" style="padding-bottom:0px;">
								<div class="wzb-selector">
										<select name="vot_tcr_id" id="tc-selector-single" style="width: 100px;"
											class="form-control">
											<c:if test="${not empty tcTrainingCenter.tcr_id}">
	                                    		<option value="${tcTrainingCenter.tcr_id}">${tcTrainingCenter.tcr_title}</option>
	                                		</c:if>
										</select>
								</div>
							</td>
							</tr>
						<tr>
							<td></td>
							<td style="padding-left:10px;">
							</td>
						</tr>
						<tr>
							<td class="wzb-form-label" valign="top"><span
								class="wzb-form-star">*</span><!-- 发表状态 -->
								<lb:get key="label_rm.label_core_requirements_management_29"/>：</td>

							<td class="wzb-form-control">
								<p>
									<label class="wzb-form-checkbox" for="tabradio-07">
										<input type="radio" name="vot_status" id="tabradio-07" value="ON" checked="checked"><!-- 已发布 --><lb:get key="label_rm.label_core_requirements_management_30" />
									</label>
								</p>
								<p>
									<label class="wzb-form-checkbox" for="tabradio-08">
										<input type="radio" name="vot_status" id="tabradio-08" value="OFF"><!-- 未发布 --><lb:get key="label_rm.label_core_requirements_management_31" />
									</label>
								</p>
								
								<script>
 									var vot_status = "${v.vot_status}";
 									if(vot_status&&vot_status!="DEL"){
 										$("input:radio[name='vot_status'][value='"+vot_status+"']").attr("checked",true);
 									}
 								</script>
							</td>
						</tr>

						<tr>
							<td class="wzb-form-label" valign="top"><span
								class="wzb-form-star">*</span><!-- 发布时间 --><lb:get key="label_rm.label_core_requirements_management_32" />：</td>
							
							<td class="wzb-form-control">
								<lb:get key="label_rm.label_core_requirements_management_33" /><!-- 由 -->
								&nbsp;<input name="start_yy" maxlength="4" size="4" class="wzb-inputText">-<input type="text" name="start_mm" maxlength="2" size="2" class="wzb-inputText">-<input type="text" name="start_dd" maxlength="2" size="2" class="wzb-inputText"><lb:get key="label_core_training_management_193"/> <!-- 年-月-日 -->&nbsp;<a href="javascript:show_calendar('document.votingForm.start', '','','','${label_lan}','${ctx}/cw/skin4/images/gb/css/wb_ui.css');"><img border="0" src="${ctx}/wb_image/btn_calendar.gif"></a>
								<input value="" type="hidden" id="vot_eff_date_from" name="vot_eff_date_from">&nbsp;
								<lb:get key="label_rm.label_core_requirements_management_34" /> <!-- 至 -->
								&nbsp;<input name="end_yy" maxlength="4" size="4" class="wzb-inputText">-<input type="text" name="end_mm" maxlength="2" size="2" class="wzb-inputText">-<input type="text" name="end_dd" maxlength="2" size="2" class="wzb-inputText"> <lb:get key="label_core_training_management_193"/><!-- 年-月-日 -->&nbsp;<a href="javascript:show_calendar('document.votingForm.end', '','','','${label_lan}','${ctx}/cw/skin4/images/gb/css/wb_ui.css');"><img border="0" src="${ctx}/wb_image/btn_calendar.gif"></a>
								<input value="" type="hidden" id="vot_eff_date_to" name="vot_eff_date_to">
							</td>
						</tr>
					</tbody>
				</table>
			</div>

			<table>
				<tbody>
					<tr>
						<td class="wzb-form-label"></td>

						<td class="wzb-form-control"><span class="wzb-form-star">*</span>
							<!-- 为必填 --><lb:get key="label_rm.label_core_requirements_management_35" /></td>
					</tr>
				</tbody>
			</table>

			<div class="wzb-bar">
				<input id="createBtn" type="submit" name="frmSubmitBtn" value="<lb:get key='global.button_ok'/>"
					class="btn wzb-btn-blue wzb-btn-big margin-right15"
					onclick="javascript:void(0);"><!-- 确定 --> <input type="button"
					name="frmSubmitBtn" value="<lb:get key='global.button_cancel'/>" class="btn wzb-btn-blue wzb-btn-big"
					onclick="javascript:window.history.go(-1)"><!-- 取消 -->
			</div>

		</div>
	
		</form>		
	</div>
	<!-- wzb-panel End-->
	<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_rm_${lang}.js"></script>
 
 	<script type="text/javascript">
 		var vot_eff_date_from = "${v.vot_eff_date_from}" ? new Date("${v.vot_eff_date_from}") : "";
 		var vot_eff_date_to = "${v.vot_eff_date_to}" ? new Date("${v.vot_eff_date_to}") : "";
 	</script>
 
	<script type="text/javascript" src="${ctx}/js/jquery.selector.js"></script>
	<script type="text/javascript" src="${ctx}/js/wb_item.js"></script>
	<script type="text/javascript" src="${ctx}/js/wb_application.js"></script>
	<script type="text/javascript" src="${ctx}/js/jquery.validate.js"></script>
	<script type="text/javascript" src="${ctx}/static/admin/js/voting/createPage.js"></script>
	<script type="text/javascript" src="${ctx}/static/js/cwn_utils.js"></script>
	<script type="text/javascript" src="${ctx}/js/date-picker.js"></script>
	<script id="option-template" type="text/x-jsrender">
		<p>
			<lb:get key="label_rm.label_core_requirements_management_40"/>：
			<input type="text" class="form-control" style="width: 280px;" name="options">
			<input type="button" class="btn wzb-btn-blue margin-right4 margin-bottom1" onclick="delOption(this);" value="<lb:get key='global.button_del'/>" style="margin-bottom:3px;">
		</p>
	</script>
</body>
</html>