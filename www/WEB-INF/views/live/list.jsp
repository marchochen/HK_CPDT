<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<%@ include file="../common/template.jsp"%>
<!DOCTYPE html>
<html>
	<head>
	<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-learner.css" />
	
	<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
	<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_${lang}.js"></script>
	<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_lvm_${lang}.js"></script>
	<script type="text/javascript" src='${ctx}/static/js/countdown.js'></script>
	<title></title>
	<script type="text/javascript">
		var dt;
		var len = 22;
		var statu = "1,2,3";
		var sortname = "lv_start_datetime";
		var sortorder = "desc";
		$(function() {
			dt = $("#content").table({
				url : '${ctx}/app/live/liveList',
				gridTemplate : colModel,
				rp : 10,
				userView : true,
		        view : 'grid',
		        rowSize : 2,
				params : {
					isMobile: false,
					status : statu,
					sortname : sortname,
					sortorder : sortorder
				},
				showpager : 5,
				usepager : true,
				hideHeader : true
			});
				
			//简介展开与收起
			$("#click_upanddown").live('click',function() {
				var sub = $(this).children("i");
				if($(sub).hasClass("fa-angle-down")) {
					$(sub).removeClass("fa-angle-down").addClass("fa-angle-up").prev("span").html(fetchLabel('click_up'));
					var pv = $(this).prev();
					var data = $(pv).attr("data");
					if(data != undefined && data != '') {
						$(pv).empty().append(data);
					}
				} else {
					$(sub).removeClass("fa-angle-up").addClass("fa-angle-down").prev("span").html(fetchLabel('click_down'));
					var pv = $(this).prev();
					var data = $(pv).attr("data");
					if(data != undefined && data != '') {
						$(pv).empty().append(substr(data, 0, len) + "...");
					}
				}
			});
			
			$("#sortul li").click(function(){
				sortname = $(this).attr("id");
				sortorder = $(this).attr("data");
				if(sortorder == 'asc') {
					$(this).attr("data","desc");
				} else if(sortorder == 'desc') {
					$(this).attr("data","asc");
				}
				//css 切换
				$(this).addClass("active").siblings().removeClass("active");
				if($(this).find("i").hasClass("fa-caret-up")){
					$(this).find("i").addClass("fa-caret-down").removeClass("fa-caret-up");
				} else {
					$(this).find("i").addClass("fa-caret-up").removeClass("fa-caret-down");
				}
				$(dt).reloadTable({
					url : '${ctx}/app/live/liveList',
					params : {
						sortname : sortname,
						sortorder : sortorder,
						status : statu
					}
				});
			});
			
		})
		
		var colModel = function(data) {
				var is_desc_sub;
				var lv_desc_sub = data.lv_desc ? substr(data.lv_desc, 0, len) : "";
				if(data.lv_desc == undefined || getChars(data.lv_desc) <= len) {
					 is_desc_sub = 'display:none';
				}
				if(getChars(data.lv_desc) > len){
					lv_desc_sub += "...";
				}
				
				var has_record_for_gensee = false;
				if(data.lv_gensee_record_url !=  null && data.lv_gensee_record_url != undefined && data.lv_gensee_record_url != 'false' && data.lv_gensee_record_url.length > 0){
					has_record_for_gensee = true;
				}
				var p = {
					lv_desc_sub : lv_desc_sub,
					is_desc_sub : is_desc_sub,
					title : data.lv_title,
					lv_desc : data.lv_desc,
					lv_id : data.lv_id,
					lv_url : data.lv_url,
					lv_create_datetime : Wzb.displayTime(data.lv_create_datetime, Wzb.time_format_ymdhm),
					lv_start_datetime : Wzb.displayTime(data.lv_start_datetime, Wzb.time_format_ymdhm),
					lv_end_datetime : Wzb.displayTime(data.lv_end_datetime, Wzb.time_format_ymdhm),
					lv_image : data.lv_image_path,
					is_compulsory : data.itd? data.itd.itd_compulsory_ind == '1' ? "<div class=wzb-bixiu><lb:get key='required'/></div>" : "" :"",
					timeid : "time_"+data.lv_id,
					lv_pwd : data.lv_pwd,
					lv_type : data.lv_type,
					lv_enc_id : data.lv_enc_id,
					lv_had_live : data.lv_had_live,
					lv_onlineNum : data.lv_onlineNum,
					lv_need_pwd : data.lv_need_pwd,
					lv_mode_type : data.lv_mode_type,
					lv_upstream_address : data.lv_upstream_address,
					lv_hls_downstream_address : data.lv_hls_downstream_address,
					lv_rtmp_downstream_address : data.lv_rtmp_downstream_address,
					lv_flv_downstream_address : data.lv_flv_downstream_address,
					lv_channel_id : data.lv_channel_id,
					has_record_for_gensee : has_record_for_gensee
				};
				var date_time = data.lv_start_datetime;
				
				var orderby = 0; //desc
				if(data.lv_type == 1){
					date_time = data.lv_real_start_datetime;
					orderby = 1;//asc
				}
				if(data.lv_type == 2){
					new TimeShow({
						element:{
							tian : "#timeday_" + data.lv_id + ' .tian',
							shi1 : "#time_" + data.lv_id + ' .shi1',
							shi2 : "#time_" + data.lv_id + ' .shi2',
							fen1 : "#time_" + data.lv_id + ' .fen1',
							fen2 : "#time_" + data.lv_id + ' .fen2'
						},
						mode: orderby
					}, date_time.replace(new RegExp('-','gm'),'/'));
				}
				return $('#liveTemplate').render(p);
			}
		
		function changeTab(id,status){
			$("#"+id).addClass("cur").parent().siblings().find("a").removeClass("cur");
			$("#content").html('');
			statu = status;
			dt = $("#content").table({
				url : '${ctx}/app/live/liveList',
				gridTemplate : colModel,
				rp : 10,
				userView : true,
		        view : 'grid',
		        rowSize : 2,
		        params : {
					status : statu
				},
				showpager : 5,
				usepager : true,
				hideHeader : true
			});
		}
		
		function notLive(){
			Dialog.alert(fetchLabel("label_core_live_management_46"));
		}
	</script>

<!-- template start -->
<script id="liveTemplate" type="text/x-jsrender">
<li>
	<div>
		{{if lv_type == 3 && (lv_mode_type == 'VHALL' || (lv_mode_type == 'GENSEE' && has_record_for_gensee))}}
			<a href="{{if (lv_had_live == false || lv_had_live == 'false')  && lv_type == 3}}javascript:Dialog.alert(fetchLabel('label_core_live_management_46'));{{else}}${ctx}/app/live/view/{{>lv_enc_id}}{{/if}}">
		{{else}}
			<span>
		{{/if}}
			<img src="{{>lv_image}}" />
			 {{if lv_type == 2}}
				<img src="${ctx}/static/images/wzb-live-time.png" alt="">
				<span class="wzb-live-shi" id="time_{{>lv_id}}">
			    	<span><em class="shi1">--</em><em class="shi2">--</em></span>
			    	<span><em class="fen1">--</em><em class="fen2">--</em></span>
			    </span>
			    <span class="wzb-live-shi-zhuang">
			    	<em><lb:get key="label_live_countdown" /></em>
			    	<em id="timeday_{{>lv_id}}"><span class="tian">--</span><lb:get key="time_day" /></em>
			    </span>
			{{else lv_type == 1}}
				<img src="${ctx}/static/images/wzb-live-living.png" alt="" />
			    <span class="wzb-live-shi-jinxing">
			        <lb:get key="label_live_looking" />
			    </span>
			    <span class="wzb-live-shi-ren">
			        <em>{{:lv_onlineNum}} </em><lb:get key="label_live_people" />
			    </span>
			{{else lv_type == 3}}
				<img src="${ctx}/static/images/wzb-live-lived.png" alt="" />
			    <span class="wzb-live-shi-lived">
					{{if (lv_mode_type == 'VHALL' || (lv_mode_type == 'GENSEE' && has_record_for_gensee))}}
			        	<lb:get key="label_live_look_lived" />
					{{else}}
						<lb:get key="label_live_over" />
					{{/if}}
			    </span>
			{{/if}}
			{{>is_compulsory}}
		{{if lv_type == 3 &&  (lv_mode_type == 'VHALL' || (lv_mode_type == 'GENSEE' && has_record_for_gensee))}}
			</a>
		{{else}}
			</span>
		{{/if}}
	 </div>
	 <p style="margin:5px 0;">
		{{if lv_type == 3 &&  (lv_mode_type == 'VHALL' || (lv_mode_type == 'GENSEE' && has_record_for_gensee))}}
			<a class="wzb-link01" href="{{if (lv_had_live == false || lv_had_live == 'false')  && lv_type == 3}}javascript:Dialog.alert(fetchLabel('label_core_live_management_46'));{{else}}${ctx}/app/live/view/{{>lv_enc_id}}{{/if}}" title="{{>title}}">{{>title}}</a>
		{{else}}
			<span>{{>title}}</span>
		{{/if}}
	</p> <!--直播标题 -->
	<div class="offheight clearfix"><span class="color-gray999" style="float:left;"><lb:get key="label_core_live_management_48"/>：</span><div style="float:left;"><p style="margin:0;"><lb:get key="label_core_community_management_214"/> {{>lv_start_datetime}} </p><p style="margin:0;"><lb:get key="time_to"/> {{>lv_end_datetime}}</p></div></div><!--开始直播时间 -->
	{{if lv_type == 3}}
		<div class="offheight clearfix">
			<span class="color-gray999"><lb:get key="label_core_live_management_49"/>：</span><span>{{:lv_onlineNum}}</span>
		</div><!--直播观看总人数 -->
	{{/if}}	
	<div class="offheight clearfix"><span class="color-gray999"><lb:get key="label_live_status"/>：</span>{{if lv_type == 2}}<lb:get key="label_live_lv_booking" />{{else lv_type == 3}}<lb:get key="label_live_over" />{{else}}<lb:get key="label_live_living" />{{/if}}</div><!--直播状态 -->
	 <p>
	 	<span class="color-gray999"><lb:get key="label_core_live_management_10"/>：</span><span data="{{>lv_desc}}">{{>lv_desc_sub}}</span><!--直播简介 -->
		<a class="wzb-show skin-color open_desc" id="click_upanddown" style="{{>is_desc_sub}}" >
			<span><lb:get key="click_down"/></span>
			<i class="fa fa-angle-down"></i>
		</a>
	</p>
</li>
</script>
<!-- template end -->
</head>
<body>
	
	<div class="xyd-wrapper">
		<div class="xyd-main clearfix">
            <div class="xyd-sidebar">
                <h3 class="wzb-title-4 skin-bg"><lb:get key="label_live" /><i class="fa fa-sanzuo fa-caret-up"></i></h3>
                <ul class="wzb-list-15 list-15" id="list">
                    <li><a onclick="changeTab('all','1,2,3');" id="all" href="javascript:void(0);"  class="wzb-live-all cur"><lb:get key="label_live_all_live" /></a></li>
                    <li><a onclick="changeTab('living','1');" id="living"  href="javascript:void(0);" class="wzb-live-living"><lb:get key="label_live_living" /></a></li>
                    <li><a onclick="changeTab('live_booking','2');" id="live_booking" href="javascript:void(0);" class="wzb-live-booking"><lb:get key="label_live_booking" /></a></li>
                    <li><a onclick="changeTab('lived','3');" id="lived" href="javascript:void(0);" class="wzb-live-lived"><lb:get key="label_live_lived" /></a></li>
                </ul>

            </div> <!-- xyd-sidebar End-->

            <div class="xyd-article">
                <div role="tabpanel" class="wzb-tab-2">
                    <!-- Nav tabs -->
                    <ul class="nav nav-tabs margin-bottom20" role="tablist" id="sortul">
                        <span><lb:get key="label_live_list" /></span>
                        
                        <%-- <li id="lv_upd_datetime" data="asc" role="presentation"  class="active">
	 						<a href="#profile" aria-controls="profile" role="tab" data-toggle="tab">
	 							<!-- 最后修改时间 --> <lb:get key="label_core_live_management_47"/>
	 							<i class="fa fa-caret-down"></i>
	 						</a>
	 					</li> --%>
                        
                        <li id="lv_start_datetime" data="asc" role="presentation" class="active">
	 						<a href="#profile" aria-controls="profile" role="tab" data-toggle="tab">
	 							<!-- 开始时间 --> <lb:get key="start_time"/>
	 							<i class="fa fa-caret-down"></i>
	 						</a>
	 					</li>
                        
                        <li id="lv_title" data="asc" role="presentation">
	 						<a href="#home" aria-controls="home" role="tab" data-toggle="tab">
	 							<!-- 直播标题  --><lb:get key="label_core_live_management_13"/>
	 							<i class="fa fa-caret-down"></i>
	 						</a>
	 					</li>
	 					
                    </ul>
                    <div class='wzb-list-35'>
                        <ul id="content" class="datatable" style="width: 100%;">
                        </ul>
                    </div>
            	</div> 
	        </div>
		</div><!-- xyd-main End -->
	</div> <!-- xyd-wrap End-->
</body>
</html>