<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
	
	<jsp:include page="../../common/meta.tree.jsp" />
	<jsp:include page="../../common/meta.datepicker.jsp" />
	<script type="text/javascript" src="${ctx}/js/jquery.selector.js"></script>
	
	<script type="text/javascript" src="${ctx}/js/date-picker.js"></script>
		
	<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_lvm_${lang}.js"></script>
	<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_cm_${lang}.js"></script>
	<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_rm_${lang}.js"></script>
	<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_rp_${lang}.js"></script>
	<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_lm_${lang}.js"></script>
    <meta charset="UTF-8">
    <title></title>
    <script>
	    $(function(){
		    
	    	$('#tc-selector-single').wzbSelector({
		        type : 'single',
		        ignoreRootNode : true,
		        tree : {
		            enable : true,
		            type : 'single',
		            setting : {
		                async : {
		                    enable : true,
		                    autoParam : [ "id" ],
		                    url : contextPath+'/app/tree/tcListJson/withHead'
		                },data : {
		                    simpleData : {
		                        enable : true
		                    }
		                }
		            }
		        },
		        message : {
		            title : fetchLabel('label_core_requirements_management_43')
		        }
		    });
	    	
			//提交
			$("#btn_submit").live("click",function(){
				
				if($("input[name='lv_title']").val() == ""){
					Dialog.alert(fetchLabel("label_core_live_management_15"));
					return;
				}
				if(getChars($("input[name='lv_title']").val()) > 80){
					Dialog.alert(fetchLabel("label_core_live_management_25"));
					return;
				}
				
				if(!$.trim($("#tc-selector-single").val())){
	    			Dialog.alert(cwn.getLabel('label_core_requirements_management_28'));
	    			return;
	    		}
				
				$("#live_start_time").val(getTime("start"));
				if($("input[name='lv_start_datetime']").val() == ""){
					Dialog.alert(fetchLabel("label_core_live_management_16"));
					return;
				}
				if(getDatetime('start','start_time')){
					return false;
				}
				
				$("#live_end_time").val(getTime("end"));
				if($("input[name='lv_end_datetime']").val() == ""){
					Dialog.alert(fetchLabel("label_core_live_management_17"));
					return;
				}
				if(getDatetime('end','label_core_live_management_5')){
					return false;
				}
				
				if(checkTime($("input[name='lv_start_datetime']").val(),$("input[name='lv_end_datetime']").val())){
					Dialog.alert(fetchLabel("label_core_live_management_32"));
					return;
				}
				
				if($("input[name='lv_people_num']").val() == ""){
					$("input[name='lv_people_num']").val(0);
				}else{
					 if(!(/^\d+$/.test($("input[name='lv_people_num']").val()))){  
						Dialog.alert(fetchLabel("label_core_live_management_88"));
			        	return;  
				    }
				}
				
				if($("input[name='image_radio']:checked").val() == 2){
					if($("input[name='image']").val() != ''){
						var file_ext = $("input[name='image']").val().substring($("input[name='image']").val().lastIndexOf(".") + 1);
						if(file_ext != 'jpg' && file_ext != 'gif' && file_ext != 'png'){
							Dialog.alert(cwn.getLabel('label_core_live_management_33'));
							return;
						}
					} else {
						Dialog.alert(cwn.getLabel('label_core_live_management_34'));
						return;
					}
				}else{
					$("input[name='image']").attr("disabled", true);
					clearFileInput(document.getElementById("file_photo_url"));
				}
				
				if($("textarea[name='lv_desc']").val() == ""){
					Dialog.alert(fetchLabel("label_core_live_management_19"));
					return;
				}
				if(getChars($("textarea[name='lv_desc']").val()) > 400){
					Dialog.alert(fetchLabel("label_core_live_management_10") + fetchLabel("label_core_live_management_26"));
					return;
				}
				
				$("#liveForm").submit();
				
			});
			
			if('${type}' == "update"){
				var start_time = '<fmt:formatDate value="${lv.lv_start_datetime}" pattern="yyyy/MM/dd HH:mm:ss"/>';
				var ent_time = '<fmt:formatDate value="${lv.lv_end_datetime}" pattern="yyyy/MM/dd HH:mm:ss"/>';
				setTime(start_time ? new Date(start_time) : "","start");
			    setTime(ent_time ? new Date(ent_time) : "","end");
			}
		    
	    })
	    function changeLivePhoto(thisObj, img_name){
			if($(thisObj).val() == 2){
				
			} else {
				$("input[name='lv_image']").val(img_name);
			}
		}
	    
	    // 获取日期控制的日期
	    function getTime(field){
	    	var year, month, date, hour, min ;
	    	eval("year = document.liveForm."+field+"_yy.value");
	    	eval("month = document.liveForm."+field+"_mm.value");
	    	eval("date = document.liveForm."+field+"_dd.value");
	    	eval("hour = document.liveForm."+field+"_hour.value");
	    	eval("min = document.liveForm."+field+"_min.value");
	    	
	    	if(year && month && date &&　hour　&& min){
	    		if(month < 10 && month.length < 2){
	    			month = "0" + month;
	    		}
	    		if(date < 10 && date.length < 2){
	    			date = "0" + date;
	    		}
	    		if(hour < 10 && hour.length < 2){
	    			hour = "0" + hour;
	    		}
	    		if(min < 10 && min.length < 2){
	    			min = "0" + min;
	    		}
	    		return year+"-"+month+"-"+date+ " " + hour + ":" + min + ":00";
	    	}
	    	return "";
	    };
	    
	    // 设置开始和结束时间的值
	    function setTime(source,field){
	    	if(source){
	        	$("input[name='"+field+"_yy']").val(source.getFullYear());
	        	$("input[name='"+field+"_mm']").val(source.getMonth() + 1);
	        	$("input[name='"+field+"_dd']").val(source.getDate());
	        	$("input[name='"+field+"_hour']").val(source.getHours());
	        	$("input[name='"+field+"_min']").val(source.getMinutes());
	        }
	    };
	    
	    function checkTime(startTime,endTime){
	        var start = new Date(Date.parse(startTime.replace(/-/g,"/")));
	        var end = new Date(Date.parse(endTime.replace(/-/g,"/")));
	        if(start.getTime()  >=  end.getTime()){
	            return true;
	        }
            return false;
	    }
	    
	    function getDatetime(spec,label){
	    	var DATE_FORMAT = /^[0-9]{4}-[0-1]?[0-9]{1}-[0-3]?[0-9]{1}$/;
	    	var date;
	    	var live_datetime_yy = $("input[name='"+spec+"_yy']").val();
	    	var live_datetime_mm = $("input[name='"+spec+"_mm']").val();
	    	var live_datetime_dd = $("input[name='"+spec+"_dd']").val();
	    	if(live_datetime_yy != '' || live_datetime_mm != '' || live_datetime_dd != ''){
	    		date = live_datetime_yy +'-'+ live_datetime_mm +'-'+ live_datetime_dd
	    	}else if(live_datetime_yy == '' && live_datetime_mm == '' && live_datetime_dd == ''){
	    		date = "";
	    	}
	    	
	    	if(!(DATE_FORMAT.test(date) && date != undefined) && date != ""){
	    		Dialog.alert('"'+fetchLabel(label) +'"'+ fetchLabel("label_core_report_152"));
	    		return true;
	    	}
	    	
	    	var TIME_FORMAT = /^([0-1]?[0-9]{1}|[2]+[0-3]{1}):[0-5]?[0-9]{1}$/;
	    	var time;
	    	var live_datetime_hh = $("input[name='"+spec+"_hour']").val();
	    	var live_datetime_min = $("input[name='"+spec+"_min']").val();
			if(live_datetime_hh  != '' || live_datetime_min != ''){
				time = live_datetime_hh +':'+ live_datetime_min;
	    	}else if(live_datetime_hh == '' && live_datetime_min == '' ){
	    		time = "";
	    	}
			if(!(TIME_FORMAT.test(time) && time != undefined) && time != ""){
	    		Dialog.alert('"'+fetchLabel(label) +'"'+ fetchLabel("label_core_report_152"));
	    		return true;
	    	}
	    	return false;
	    }
    	
    </script>

</head>
<body>

	<input type="hidden" name="belong_module" value="FTN_AMD_LIVE_MAIN" />

	<title:get function="global.FTN_AMD_LIVE_MAIN" />

	<ol class="breadcrumb wzb-breadcrumb">
		<li><a href="javascript:wb_utils_gen_home(true);"><i
				class="fa wzb-breadcrumb-home fa-home"></i>
			<lb:get key="global.lab_menu_started" /></a></li>
		<li><a href="${ctx }/app/admin/live/list?mode_type=${type eq 'update' ? lv.lv_mode_type : live_mode_type}"><lb:get key="global.FTN_AMD_LIVE_MAIN"/> </a></li>
		<li class="active">
			<c:choose>
				<c:when test="${type eq 'update'}">
					<lb:get key="label_core_live_management_9" />
				</c:when>
				<c:otherwise>
					<lb:get key="label_core_live_management_3" />
				</c:otherwise>
			</c:choose>
		</li>
	</ol>
	<!-- wzb-breadcrumb End-->
	
	<div class="panel wzb-panel">

		<div class="panel-body">
			<form action="${ctx}/app/admin/live/${type eq 'update' ? 'updatelive/' : 'addlive' }${lv.lv_id}" id="liveForm" name="liveForm" method="post" enctype="multipart/form-data">
				<input type="hidden" name="lv_mode_type"  value="${type eq 'update' ? lv.lv_mode_type : live_mode_type}"  />
				<table>
					<tbody>
						<tr>
							<td class="wzb-form-label"> <!-- 标题 -->
								<span class="wzb-form-star">*</span>
								<span class="wzb-live-title"><lb:get key="label_core_live_management_13"/>：</span>
							</td>
							<td class="wzb-form-control">
								<span class="wzb-live-content"><input type="text" name="lv_title" value="${lv.lv_title}" placeholder="<lb:get key='label_core_live_management_15' />" class="form-control" style="width:400px;"/></span>
							</td>
						</tr>
						<tr id="tr_trc"> <!-- 培训中心 -->
							<td class="wzb-form-label" style="padding-bottom:10px;">
								<span class="wzb-form-star">*</span>
								<lb:get key="label_rm.label_core_requirements_management_27" />：
							</td>
							<td class="wzb-form-control" style="padding-bottom:0px;">
								<div class="wzb-selector">
									<select name="lv_tcr_id" id="tc-selector-single" style="width: 100px;" class="form-control">
										<c:if test="${not empty tcTrainingCenter.tcr_id}">
	                                   		<option value="${tcTrainingCenter.tcr_id}">${tcTrainingCenter.tcr_title}</option>
	                               		</c:if>
									</select>
								</div>
							</td>
						</tr>
						<tr>
							<td class="wzb-form-label"> <!-- 开始时间 -->
								<span class="wzb-form-star">*</span>
								<span class="wzb-live-title"><lb:get key="start_time"/>：</span>
							</td>
							<td class="wzb-form-control">
								<span class="wzb-live-content">
									<input name="start_yy" ${!empty lv.lv_type && lv.lv_type ne 2 && lv.lv_type ne 0 ? 'disabled="disabled"' : '' } maxlength="4" size="4" class="wzb-inputText">-
									<input type="text" ${!empty lv.lv_type && lv.lv_type ne 2 && lv.lv_type ne 0 ? 'disabled="disabled"' : '' } name="start_mm" maxlength="2" size="2" class="wzb-inputText">-
									<input type="text" ${!empty lv.lv_type && lv.lv_type ne 2 && lv.lv_type ne 0 ? 'disabled="disabled"' : '' } name="start_dd" maxlength="2" size="2" class="wzb-inputText">
									<lb:get key="label_core_training_management_193"/>&nbsp;
									<c:set var="starttime_url" value="show_calendar('document.liveForm.start', '','','','${label_lan}','${ctx}/cw/skin4/images/gb/css/wb_ui.css');"></c:set>
									<a href="javascript:${!empty lv.lv_type && lv.lv_type ne 2 && lv.lv_type ne 0 ? 'void(0);' : starttime_url }"><img border="0" src="${ctx}/wb_image/btn_calendar.gif"></a> &nbsp;&nbsp;
									<input type="text" ${!empty lv.lv_type && lv.lv_type ne 2 && lv.lv_type ne 0 ? 'disabled="disabled"' : '' } name="start_hour" maxlength="2" size="2" class="wzb-inputText" value="">：
									<input type="text" ${!empty lv.lv_type && lv.lv_type ne 2 && lv.lv_type ne 0 ? 'disabled="disabled"' : '' } name="start_min" maxlength="2" size="2" class="wzb-inputText" value="">
									<lb:get key="label_core_live_management_39"/>：<lb:get key="label_core_live_management_40"/>
									<input value="" type="hidden"  id="live_start_time"  name="lv_start_datetime" />
								</span>
							</td>
						</tr>
						<tr>
							<td class="wzb-form-label"> <!-- 结束时间 -->
								<span class="wzb-form-star">*</span>
								<span class="wzb-live-title"><lb:get key="label_core_live_management_5"/>：</span>
							</td>
							<td class="wzb-form-control">
								<span class="wzb-live-content">
									<input name="end_yy" ${!empty lv.lv_type && lv.lv_type ne 2 && lv.lv_type ne 0 ? 'disabled="disabled"' : '' } maxlength="4" size="4" class="wzb-inputText">-
									<input type="text" ${!empty lv.lv_type && lv.lv_type ne 2 && lv.lv_type ne 0 ? 'disabled="disabled"' : '' } name="end_mm" maxlength="2" size="2" class="wzb-inputText">-
									<input type="text" ${!empty lv.lv_type && lv.lv_type ne 2 && lv.lv_type ne 0 ? 'disabled="disabled"' : '' } name="end_dd" maxlength="2" size="2" class="wzb-inputText">
									<lb:get key="label_core_training_management_193"/>&nbsp;
									<c:set var="endtime_url" value="show_calendar('document.liveForm.end', '','','','${label_lan}','${ctx}/cw/skin4/images/gb/css/wb_ui.css');"></c:set>
									<a href="javascript:${!empty lv.lv_type && lv.lv_type ne 2 && lv.lv_type ne 0 ? 'void(0);' : endtime_url }">
										<img border="0" src="${ctx}/wb_image/btn_calendar.gif">
									</a> 
									&nbsp;&nbsp;
									<input type="text" ${!empty lv.lv_type && lv.lv_type ne 2 && lv.lv_type ne 0 ? 'disabled="disabled"' : '' } name="end_hour" maxlength="2" size="2" class="wzb-inputText" value="">：
									<input type="text" ${!empty lv.lv_type && lv.lv_type ne 2 && lv.lv_type ne 0 ? 'disabled="disabled"' : '' } name="end_min" maxlength="2" size="2" class="wzb-inputText" value="">
									<lb:get key="label_core_live_management_39"/>：<lb:get key="label_core_live_management_40"/>
									<input value="" type="hidden" id="live_end_time"  name="lv_end_datetime" />
								</span>
							</td>
						</tr>
						<tr>
							<td class="wzb-form-label"> <!-- 限制直播的人数 -->
								<lb:get key="label_core_live_management_83"/>：
							</td>
							<td class="wzb-form-control">
								<span class="wzb-live-content">
									<input type="number" min="0" name="lv_people_num" class="form-control" value="${lv.lv_people_num }" style="width:400px;" />
								</span>
							</td>
						</tr>
						<tr>
							<td class="wzb-form-label"><!-- 需要输入密码才参加  -->
							</td>
							<td class="wzb-form-control">
								<span class="wzb-live-content">
									<input type="checkbox" name="lv_need_pwd" ${lv.lv_need_pwd ? 'checked' : ''} />
									<lb:get key="label_core_live_management_38"/>
									<span class="color-gray999">（<lb:get key="label_core_live_management_50"/>）</span>
								</span>
							</td>
						</tr>
						<tr>
							<td valign="top" class="wzb-form-label"> <!-- 图片 -->
								<span class="wzb-form-star">*</span>
								<span class="wzb-live-title"><lb:get key="label_core_live_management_12"/>：</span>
							</td>
							<td class="wzb-form-control">
								<script type="text/javascript">
									var defaultImageTemplateName = 'live';
									var input_name = "live_image";
									var input_hidden_name = "lv_image";
									var handle_type = '${type}';//增加(add)或修改(update)
									p = {
										type : handle_type, 
										image_url : '${ctx}${lv.lv_image_path }', //图片的路径
										input_name :　input_name,
										input_hidden_name : input_hidden_name
									}
								</script>
								<div id="select_image"></div>
								<a class="thickbox" href="#TB_inline?height=380&amp;width=580&amp;inlineId=myOnPageContent" id="default_btn"></a>
								<jsp:include page="../../common/imglib.jsp" />
							</td>
						</tr>
						<tr>
							<td class="wzb-form-label" valign="top"> <!-- 直播简介 -->
								<span class="wzb-form-star">*</span>
								<span class="wzb-live-title"><lb:get key="label_core_live_management_10" />：</span>
							</td>
							<td class="wzb-form-control">
								<span class="wzb-live-content"><textarea style="width:400px" class="wzb-inputTextArea" rows="4" cols="50" name="lv_desc" placeholder="<lb:get key='label_core_live_management_19' />">${lv.lv_desc}</textarea></span>
							</td>
						</tr>
						<tr>
							<td class="wzb-form-label" align="right" width="20%"></td>
							<td class="wzb-form-control wzb-ui-module-text" width="80%"><span class="wzb-ui-module-text"><span class="wzb-form-star">*</span><lb:get key="usr_required"/></span></td>
						</tr>
					</tbody>
				</table>
			</form>
			<div class="wzb-bar">
				<input class="btn wzb-btn-blue wzb-btn-big margin-right15" id="btn_submit" type="button" value="<lb:get key='button_ok' />" />
				<input class="btn wzb-btn-blue wzb-btn-big" type="button" onclick="javascript:history.back()" value="<lb:get key='btn_cancel' />" />    				
			</div>
		</div>	    
    </div>
</body>

</html>