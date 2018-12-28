<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
	<head>
		<!-- 日程表选择插件 -->
		<script type="text/javascript" src="${ctx}/static/js/richengbiao.js"></script>
		<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-learner.css">
	</head>
	<body>
	<div class="xyd-wrapper">
	    <div class="xyd-main clearfix" style="width:1000px;">
	    	<div class="wzb-model-13 clearfix">
				<div class="wzb-title-14" style="padding:0 0 0 24px;"><!-- 学习日程表 --><lb:get key="global.lab_menu_study_map" /></div>
				<div class="aboluo-w">
					<div class="aboluo-tools">
						<a class="aboluo-month-a-perv" href="javascript:;"></a>
						<div class="aboluo-calendar-select-year"></div>
						<div class="aboluo-calendar-month"></div>
						<a class="aboluo-month-a-next" href="javascript:;"></a>
	
						<input type="button" class="aboluo-toToday" value="<lb:get key="calendar_today"/>" />
					</div>
					<div class="aboluo-leftdiv">
						<div class="aboluo-rilidiv">
							<table class="aboluo-rilitable" cellspacing="0" cellpadding="0" >
								<thead class="aboluo-rilithead">
									<tr>
										<th><lb:get key="calendar_sun"/></th>
										<th><lb:get key="calendar_mon"/></th>
										<th><lb:get key="calendar_tue"/></th>
										<th><lb:get key="calendar_wed"/></th>
										<th><lb:get key="calendar_thu"/></th>
										<th><lb:get key="calendar_fri"/></th>
										<th><lb:get key="calendar_sat"/></th>
									</tr>
								</thead>
							</table>
						</div>
					</div>
					<div class="aboluo-rightdiv">
						<div class="aboluo-rightdiv-top"></div>
						<div class="aboluo-rightdiv-tap wzb-tab-8">
							<ul class="nav-tabs">
								<li class="tap-all" id="change_content_1"><span style="display:none;"><!-- 所有课程 --><lb:get key="label_core_training_management_350"/></span></li>
								<li class="tap-mianshou active" data='mianshou' id="change_content_2"><span ><!-- 面授课程 --><lb:get key="classroom"/></span></li>
								<li class="tap-wangshang" data='wangshang' id="change_content_3"><span style="display:none;"><!-- 网上课程 --><lb:get key="selfstudy"/></span></li>
								<!-- 项目式课程 
								<li class="tap-xiangmushi" data='xiangmu'><span style="display:none;"><lb:get key="integrated"/></span></li>
								-->
							</ul>
						</div>
						<div class="aboluo-rightdiv-content">
							<div id="schedule-mianshou-content" data='class'>
								没有数据需要显示
								
							</div>
							<div id="schedule-wangshang-content" data='class' style='display:none'>
							</div>
							<div id="schedule-xiangmu-content" data='class' style='display:none'>
							</div>
							<input type="hidden" value="2" id="check_content"/>
							
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
<script id="schedule-class-content-show" type="text/x-jsrender"> 
  <div class="wzb-tuwen-list-1"> 
    <a href="${ctx}/app/course/detail/{{:encItmId}}"> 
      <div class="wzb-tuwen-list-1-pic">
        <img src="${ctx}{{:itm_icon}}" alt="" width="87px"/>
        <div class="wzb-tuwen-list-1-pic-shade">{{:appStatusStr}}</div>
      </div>  
      <div class="margin-left97"> 
        <div class="wzb-tuwen-list-1-title">{{:itm_title}}</div>  
        <div class="wzb-tuwen-list-1-subtitle"> 
          <div>{{:itm_desc}}</div>  
          <div>{{:itm_eff_start_datetime.split(' ')[0]}} <lb:get key="time_to"/> {{if itm_eff_end_datetime}} {{:itm_eff_end_datetime.split(' ')[0]}} {{else}} <lb:get key="course_date_nolimit"/> {{/if}}</div> 
        </div> 
      </div> 
    </a> 
  </div> 
</script>
		<!-- 日程表业务代码 -->
		<script type="text/javascript" src="${ctx}/static/js/front/schedule/schedule.js"></script>
	</body>
	
</html>
