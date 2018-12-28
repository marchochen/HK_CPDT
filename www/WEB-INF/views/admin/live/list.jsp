<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<%@ include file="../../common/template.jsp"%>
<!DOCTYPE html>
<html>
	<head>
		<link rel="stylesheet" href="${ctx}/static/theme/blue/css/style.css">
		<link rel="stylesheet" href="${ctx}/static/js/layer/skin/layer.css">
		<script type="text/javascript" src="${ctx}/static/js/layer/layer.js"></script>
		<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
		<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_${lang}.js"></script>
		<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_lvm_${lang}.js"></script>
		<script type="text/javascript" src='${ctx}/static/js/countdown.js'></script>
		<title></title>
		<script type="text/javascript">
			var dt;
			var len = 40;
			var statu = "1,2,3";
			var sortorder = "desc"; 
			var sortname = "lv_upd_datetime";
			var liveListUrl = '${ctx}/app/admin/live/liveList';
			
			var mode_type = cwn.getUrlParam("mode_type");
			var live_mode_type = '${liveAuth.liveMode}';			
			
			$(function() {
				
				if(mode_type != undefined && mode_type != ""){
					$("#live_"+mode_type.toLowerCase()).addClass('active').siblings('li').removeClass('active');
					live_mode_type = mode_type;
				}
				
				if(live_mode_type == "QCLOUD"){
					$("#live-tutorial").show();
					$("#sortul").css("margin","0 40px 0 0");
				}
				
				dt = $(".wbtable").table({
					url : liveListUrl,
					colModel : colModel,
					rp : 10,
					params : {
						status : statu,
						sortname : sortname,
						sortorder : sortorder,
						live_mode_type : live_mode_type
					},
					showpager : 5,
					usepager : true,
					hideHeader : true,
				});
				
				$(".deletelive").live('click',function(){
					var lvId = $(this).attr("data");
					Dialog.confirm({text:fetchLabel("label_core_live_management_21"), callback: 
						function (flag) {
							if(flag){
								$.ajax({url: "${ctx}/app/admin/live/delete/"+lvId, 
										type:'POST',
										dataType:'json',
										success: function(result_data){
											if(result_data.status == 'success'){
												$(dt).reloadTable({
													url : liveListUrl,
													params : {
														status : statu,
														sortname : sortname,
														sortorder : sortorder,
														live_mode_type : live_mode_type
													}
												});
											}
										}
								});
							}
						}
					});	
				});
				
				$(document).keydown(function(event){
		        	  if(event.keyCode ==13){
		        		  searchLive();
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
						url : liveListUrl,
						params : {
							sortname : sortname,
							sortorder : sortorder,
							status : statu,
							live_mode_type : live_mode_type
						}
					});
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
				
			});
			
			function changeLiveType(val){
				statu = val;
				$(dt).reloadTable({
					url : liveListUrl,
					params : {
						sortname : sortname,
						sortorder : sortorder,
						status : statu,
						live_mode_type : live_mode_type
					}
				});
			}
			
			function searchLive(){
				var searchContent = $("input[name='searchContent']").val();
				if(searchContent == fetchLabel('label_core_live_management_4')){
					searchContent = '';
				}
				$(dt).reloadTable({
					url : liveListUrl,
					params : {
						searchContent : searchContent,
						status : statu,
						sortname : sortname,
						sortorder : sortorder,
						live_mode_type : live_mode_type
					}
				});
			}
			
			function changeTab(live_mode){
				var url;
				if(mode_type != ""){
					url = changeURLArg(window.location.href,"mode_type",live_mode);
				}else{
					url = window.location.href + "?mode_type=" + live_mode;
				}
				window.location.href = url;
			}
			
			function changeURLArg(url,arg,arg_val){ 
			    var pattern=arg+'=([^&]*)'; 
			    var replaceText=arg+'='+arg_val; 
			    if(url.match(pattern)){ 
			        var tmp='/('+ arg+'=)([^&]*)/gi'; 
			        tmp=url.replace(eval(tmp),replaceText); 
			        return tmp; 
			    }else{ 
			        if(url.match('[\?]')){ 
			            return url+'&'+replaceText; 
			        }else{ 
			            return url+'?'+replaceText; 
			        } 
			    } 
			    return url+'\n'+arg+'\n'+arg_val; 
			}
			
			function setQcloudLVBId(lv_id){
				$.ajax({url: "${ctx}/app/admin/live/startLive/"+lv_id, 
					dataType:'json',
					success: function(result_data){
						$(dt).reloadTable();
					}
				});
			}
			
			function setLiveModalTemplate(lv_id){
				var p = {
					lv_id : lv_id
				}
				$('#model_div').html($('#liveModalTemplate').render(p));
			}
			
			var colModel = [{
				format : function(data) {
					var is_desc_sub;
					var lv_desc_sub = data.lv_desc ? substr(data.lv_desc, 0, len) : "";
					if(data.lv_desc == undefined || getChars(data.lv_desc) <= len) {
						 is_desc_sub = 'display:none';
					}
					if(getChars(data.lv_desc) > len){
						lv_desc_sub += "...";
					}
					var lv_upstream_address_1 = '';
					var lv_upstream_address_2 = '';
					if(data.lv_upstream_address){
						var sindex = data.lv_upstream_address.lastIndexOf("/");
						if(sindex != -1){
							lv_upstream_address_1 = data.lv_upstream_address.substring(0,sindex+1);
							lv_upstream_address_2 = data.lv_upstream_address.substring(sindex+1);
						}
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
						lv_end_datetime : Wzb.displayTime(data.lv_end_datetime, Wzb.time_format_ymdhm),
						lv_start_datetime : Wzb.displayTime(data.lv_start_datetime, Wzb.time_format_ymdhm),
						lv_image : data.lv_image_path,
						is_compulsory : data.itd? data.itd.itd_compulsory_ind == '1' ? "<div class=wzb-bixiu><lb:get key='required'/></div>" : "" :"",
						timeid : "time_"+data.lv_id,
						lv_pwd : data.lv_pwd,
						lv_type : data.lv_type,
						lv_enc_id : data.lv_enc_id,
						lv_status : data.lv_status,
						lv_had_live : data.lv_had_live,
						lv_onlineNum : data.lv_onlineNum,
						lv_need_pwd : data.lv_need_pwd,
						lv_mode_type : data.lv_mode_type,
						lv_upstream_address_1 : lv_upstream_address_1,
						lv_upstream_address_2 : lv_upstream_address_2,
						lv_hls_downstream_address : data.lv_hls_downstream_address,
						lv_rtmp_downstream_address : data.lv_rtmp_downstream_address,
						lv_flv_downstream_address : data.lv_flv_downstream_address,
						lv_channel_id : data.lv_channel_id,
						lv_people_num : data.lv_people_num,
						has_record_for_gensee : has_record_for_gensee
					};
					if(data.lv_type == 2){
						new TimeShow({
							element:{
								tian : "#timeday_" + data.lv_id + ' .tian',
								shi1 : "#time_" + data.lv_id + ' .shi1',
								shi2 : "#time_" + data.lv_id + ' .shi2',
								fen1 : "#time_" + data.lv_id + ' .fen1',
								fen2 : "#time_" + data.lv_id + ' .fen2'
							},
							mode: 2
						}, data.lv_start_datetime.replace(new RegExp('-','gm'),'/'));
					}
					
					if(data.lv_mode_type == "VHALL"){
						$(".v-start-live").mouseenter(function(){
							layer.tips(fetchLabel("label_core_live_management_84"),this, {
				        		tips: [2,'rgba(128,128,128,0.9)'],
				            	time:50000
							});
						});
			          	$(".v-start-live").mouseleave(function(){
			              	layer.tips()
			          	});
					}
					
					return $('#liveTemplate').render(p);
				}
			}];
		</script>

		<!-- template start type="text/x-jsrender"-->
		<script id="liveTemplate"  type="text/x-jsrender">
			<dl class="wzb-list-6 wzb-live-list" style="border:0;">
				<dd>
					<div>
						{{if lv_type == 3 && (lv_mode_type == 'VHALL' || (lv_mode_type == 'GENSEE' && has_record_for_gensee))}}
							<a href="{{if (lv_had_live == false || lv_had_live == 'false')}}javascript:Dialog.alert('<lb:get key="label_core_live_management_46" />');{{else}}${ctx}/app/admin/live/view/{{>lv_enc_id}}{{else}}javascript:void(0);{{/if}}">
						{{else}}
							<span>
						{{/if}}
							<img src="{{>lv_image}}" />
							{{if lv_type == 2}}
								<img src="${ctx}/static/images/wzb-live-time.png" alt="">
								<span class="wzb-live-shi"  id="time_{{>lv_id}}">
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
				 </dd>
				 <dt class="dt-1"  style="display: inline-block;">
					  	<p><span style="font-size:16px" title="{{>title}}">{{>title}}</span></p> <!--直播标题 -->
						<div class="offheight clearfix"><span class="color-gray999"><lb:get key="label_core_live_management_48"/>：</span><lb:get key="label_core_community_management_214"/> {{>lv_start_datetime}} <lb:get key="time_to"/> {{>lv_end_datetime}}</div><!--开始直播时间 -->
						{{if lv_status == 'ON' && lv_need_pwd == true}}
							<div class="offheight clearfix"><span class="color-gray999"><lb:get key="label_core_live_management_7" />：</span>{{>lv_pwd}}</a></div>
						{{/if}}<!--直播密码 -->
						{{if lv_people_num > 0}}
							<div class="offheight clearfix"><span class="color-gray999"><lb:get key="label_core_live_management_83" />：</span>{{:lv_people_num}}</a></div>
						{{/if}}<!--限制人数 -->
						<div class="offheight clearfix">
							<span class="color-gray999"><lb:get key="label_live_status"/>：</span>{{if lv_type == 2}}<lb:get key="label_live_lv_booking" />{{else lv_type == 3}}<lb:get key="label_live_over" />{{else}}<lb:get key="label_live_living" />{{/if}}
						</div><!--直播状态 -->
						{{if lv_type != 3 &&  lv_mode_type == 'QCLOUD'}}
							<div class="offheight clearfix" style="word-break:break-all;"><span class="color-gray999"><lb:get key="label_core_live_management_94"/>：</span>{{:lv_upstream_address_1}}</div>
							<div class="offheight clearfix" style="word-break:break-all;"><span class="color-gray999"><lb:get key="label_core_live_management_81"/>：</span>{{:lv_upstream_address_2}}</div>
						{{/if}}
						{{if lv_type == 3}}
							<div class="offheight clearfix">
							<span class="color-gray999"><lb:get key="label_core_live_management_49"/>：</span><span>{{:lv_onlineNum}}</span>
							</div><!--直播观看总人数 -->
						{{/if}}	
					  <p>
					  	<span class="color-gray999"><lb:get key="label_core_live_management_10"/>：</span><span data="{{>lv_desc}}">{{>lv_desc_sub}}</span><!--直播简介 -->
						<a class="wzb-show skin-color open_desc" id="click_upanddown" style="{{>is_desc_sub}}" >
							<span><lb:get key="click_down"/></span>
							<i class="fa fa-angle-down"></i>
						</a>
					 </p>
				 </dt>
				<dt class="dt-2" style="display:inline-block;float:right;">
					{{if lv_status == 'ON'}}
						{{if lv_type == 2}}<!--开始直播 -->
						<p style="margin:0;text-align: right;">
							<span class="grayC999">
								{{if lv_mode_type == 'QCLOUD'}}
									<a onclick="setLiveModalTemplate('{{>lv_enc_id}}')"  href="javascript:void(0);"  data-toggle="modal" data-target="#model_div" class="btn wzb-btn-blue thickbox"><lb:get key="label_core_live_management_22" /></a>
								{{else}}
									<a href="${ctx}/app/admin/live/detail/host/{{>lv_enc_id}}" target="_blank" class="btn wzb-btn-blue v-start-live"><lb:get key="label_core_live_management_22" /></a>
								{{/if}}
							</span>
						</p>
						{{/if}}
						{{if lv_type == 1 && lv_mode_type == 'QCLOUD'}}<!--结束直播 -->
							<p  style="margin:0;text-align: right;"><a href="${ctx}/app/admin/live/endlive/{{>lv_enc_id}}" class="btn wzb-btn-blue"><lb:get key="label_core_live_management_55" /></a></p>
						{{/if}}
						<p style="margin:5px 0 0 0;text-align: right;"><!--取消发布-->
							<span class="grayC999">	
								<a href="${ctx}/app/admin/live/unpublish/{{>lv_enc_id}}" class="btn wzb-btn-blue"><lb:get key="label_core_live_management_36" /></a>
							</span>
						</p>
					{{else}}<!--马上发布-->
						<span class="grayC999">	
							<a href="${ctx}/app/admin/live/publish/{{>lv_enc_id}}" class="btn wzb-btn-blue"><lb:get key="label_core_live_management_35" /></a>
						</span>
					{{/if}}
					
					{{if lv_type == 3 &&  (lv_mode_type == 'VHALL' || (lv_mode_type == 'GENSEE' && has_record_for_gensee))}}<!--查看回放 -->
					<p style="margin:5px 0 0 0;text-align: right;">
						<span class="grayC999">
							<a href="{{if (lv_had_live == false || lv_had_live == 'false')  && lv_type == 3}}javascript:Dialog.alert('<lb:get key="label_core_live_management_46" />');{{else}}${ctx}/app/admin/live/view/{{>lv_enc_id}}{{/if}}" class="btn wzb-btn-blue"><lb:get key="label_core_live_management_27" /></a>
						</span>
					</p>
					{{/if}}
			
					<p style="margin:5px 0 0 0;text-align: right;"><!--修改直播 -->
						<span class="grayC999">
							<a href="/app/admin/live/update/{{>lv_enc_id}}" class="btn wzb-btn-blue"><lb:get key="button_update" /></a>	
						</span>
					</p>
					<p style="margin:5px 0 0 0;text-align: right;"><!--删除直播 -->
						<span class="grayC999">
							<a href="javascript:void(0);" data="{{>lv_enc_id}}" class="deletelive btn wzb-btn-blue"><lb:get key="button_delete" /></a>
						</span>
					</p>
				 </dt>
			</dl>
		</script>
		<script id="liveModalTemplate"  type="text/x-jsrender">
				<div class="modal-dialog modal-dialog-2" style="width:770px;margin: -310px 0 0 -385px;">
					<div class="modal-content" >
					
				   		<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal" aria-label="Close">
				             	<span aria-hidden="true" class="font-size28">&times;</span>
				            </button>
				            <span align="left" id="myModalLabel" class="font-size18"><lb:get key="label_core_live_management_56" /></span>
				        </div>
				        
						<div class="wzb-pop-1-main"  style="height:520px;">
							<div><img src="${ctx}/static/images/live0.png" width="700px"/></div>
							<div>
								<div class="font-size18"><lb:get key="label_core_live_management_57" /></div>
								<lb:get key="label_core_live_management_58" /><a href="https://obsproject.com/download" target="_blank" style="text-decoration: underline;"><lb:get key="label_core_live_management_59" /></a><lb:get key="label_core_live_management_60" />
								<div><img src="${ctx}/static/images/live1-1.png" width="700px"/></div>
							</div>
							<div>
								<div class="font-size18 margin-top20"><lb:get key="label_core_live_management_61" /></div>
								<p class="font-size16 margin-top15 margin-bottom15"><lb:get key="label_core_live_management_62" /></p>
								<div><img src="${ctx}/static/images/live2-1.png" width="700px"/></div>
								<p class="margin-top15 margin-bottom15"><lb:get key="label_core_live_management_63" /></p>
								<div><img src="${ctx}/static/images/live2-2.png" width="700px"/></div>
								<p class="font-size16 margin-top15 margin-bottom15"><lb:get key="label_core_live_management_64" /></p>
								<div><img src="${ctx}/static/images/live2-3.png" width="700px"/></div>
								<p class="margin-top10"><lb:get key="label_core_live_management_65" />：</p>
								<p><lb:get key="label_core_live_management_66" /></p>
								<p><lb:get key="label_core_live_management_67" /></p>
								<p class="font-size16 margin-top10  margin-bottom10"><lb:get key="label_core_live_management_68" /></p>
								<p><lb:get key="label_core_live_management_69" /></p>
								<p><lb:get key="label_core_live_management_70" /></p>
								<div><img src="${ctx}/static/images/live2-4.png" width="700px"/></div>
								<p class="font-size16 margin-top15  margin-bottom15"><lb:get key="label_core_live_management_71" /></p>
								<div><img src="${ctx}/static/images/live2-5.png" width="700px"/></div>
								<p class="font-size16 margin-top15  margin-bottom15"><lb:get key="label_core_live_management_72" /></p>
								<div><img src="${ctx}/static/images/live2-6.png" width="700px"/></div>
								<p class="margin-top10 margin-bottom10"><lb:get key="label_core_live_management_75" /></p>
								<div><img src="${ctx}/static/images/live2-7.png" width="700px"/></div>
								<p><lb:get key="label_core_live_management_76" /></p>
								<div><img src="${ctx}/static/images/live2-8.png" width="300px"/></div>
								<p class="margin-top10 margin-bottom10"><lb:get key="label_core_live_management_77" /></p>
								<div><img src="${ctx}/static/images/live2-10.png" width="700px"/></div>
							</div>
							<div>
								<div class="font-size18 margin-top20"><lb:get key="label_core_live_management_78" /></div>
								<p class="margin-top10 margin-bottom10"><lb:get key="label_core_live_management_79" /></p>
								<div><img src="${ctx}/static/images/live3-1.png" width="700px"/></div>
								<p class="margin-top10 margin-bottom10" style="color:red;"><lb:get key="label_core_live_management_80" /></p>
							</div>	
							{{if lv_id != undefined }}
								<a onclick="setQcloudLVBId('{{>lv_id}}')" id="startQcloudLVB"  data="0" aria-hidden="true" aria-label="Close" data-dismiss="modal" href="javascript:void(0);" style="background:#ff8c00;color:#fff;text-align:center;position:absolute;bottom:-30px;left:0;width:100%;height:50px;line-height:46px;margin:0;"><lb:get key="label_core_live_management_82" /></a>
							{{/if}}
						</div>
					</div>
			</div>
		</script>
		<!-- template end -->
	</head>
	<body>
		<input type="hidden" name="belong_module" value="FTN_AMD_LIVE_MAIN" />	
		<title:get function="global.FTN_AMD_LIVE_MAIN" />
	
		<ol class="breadcrumb wzb-breadcrumb">
			<li><a href="javascript:wb_utils_gen_home(true);"><i
					class="fa wzb-breadcrumb-home fa-home"></i>
				<lb:get key="global.lab_menu_started" /></a></li>
			<li class="active">
					<lb:get key="global.FTN_AMD_LIVE_MAIN" />
			</li>
		</ol>
		
		<div class="panel wzb-panel">
			<div class="panel-heading">
				<lb:get key="label_cm.label_core_community_management_60" />
			</div>
	
			<div class="panel-body">
				<div class="col-xs-12 padlr0">
					<div class="cont">
						<span>
							<!-- Nav tabs -->
							<c:if test="${(liveAuth.qcloudLiveAuth == true && liveAuth.genseeLiveAuth == true) || (liveAuth.vhallLiveAuth == true && liveAuth.genseeLiveAuth == true) || (liveAuth.vhallLiveAuth == true && liveAuth.qcloudLiveAuth == true) || (liveAuth.vhallLiveAuth == true && liveAuth.qcloudLiveAuth == true && liveAuth.genseeLiveAuth == true) }">
				                 <ul class="nav nav-tabs" role="tablist">
				                 	<c:if test="${liveAuth.qcloudLiveAuth == true }">
										<li role="presentation" id="live_qcloud" class="active" onclick="changeTab('QCLOUD')">
											<a href="#demand" aria-controls="settings" role="tab" data-toggle="tab">
												<lb:get key="label_core_live_management_52" />
											</a>
										</li>
									</c:if>
									<c:if test="${liveAuth.vhallLiveAuth == true }">
										<li role="presentation" id="live_vhall"  class="${liveAuth.qcloudLiveAuth == true?'':'active' }"  onclick="changeTab('VHALL')">
											<a href="#home" aria-controls="home" role="tab" data-toggle="tab" >
												<lb:get key="label_core_live_management_53" />
											</a>
										</li>
									</c:if>
									<c:if test="${liveAuth.genseeLiveAuth == true }">
										<li role="presentation" id="live_gensee"  class="${liveAuth.genseeLiveAuth == true?'':'active' }"  onclick="changeTab('GENSEE')">
											<a href="#gensee" aria-controls="gensee" role="tab" data-toggle="tab" >
												<lb:get key="label_core_live_management_99" />
											</a>
										</li>
									</c:if>
								</ul>
							</c:if>
						</span>
						<div style="width:100%;display:inline-block;">
						<form name="searchFrm" onsubmit="return false;" class="form-search" style="float:right;${liveAuth.vhallLiveAuth == true && liveAuth.qcloudLiveAuth == true?'margin-top:-35px;':''}">
								<c:set var="a_url">
									<c:choose>
										<c:when test="${(empty liveAuth.vhallLiveAuth || liveAuth.vhallLiveAuth == false || (liveAuth.vhallLiveAuth == true && liveAuth.hasVhallLiveCount==false)) && (empty  liveAuth.qcloudLiveAuth || liveAuth.qcloudLiveAuth == false) }">javascript:Dialog.alert('<lb:get key="label_core_live_management_45"/>');</c:when>
										<c:otherwise>javascript:window.location.href='${ctx}/app/admin/live/insert?mode_type='+live_mode_type</c:otherwise>
									</c:choose>
								</c:set>
								<a href="${a_url }" class="wbtj mr20 fontfamily swop_bg btn wzb-btn-orange" style="float:right;margin-left:4px;">
									<lb:get key='label_core_live_management_3' />
								</a>
								<input onclick="searchLive()" value="<lb:get key='button_search' />" class="btn swop_bg btn wzb-btn-blue" type="button" style="float: right; border-radius: 0;"> 
								<input placeholder="<lb:get key='label_core_live_management_4' />" prompt="<lb:get key='label_core_live_management_4' />" name="searchContent" size="25" type="text" class="form-control" style="float: right; outline: none; margin: 2px 0;"> 
								<div style="float:right;margin:2px 4px 0;">
										<lb:get key="global_status"/>：
										<select onchange="changeLiveType(this.value)"  class="wzb-form-select" size="1" style="width: 220px;">
											<option value="" selected="selected">-- <lb:get key="status_all"/> --</option>
											<option value="2"><lb:get key="label_live_lv_booking"/></option>
											<option value="1"><lb:get key="label_live_living"/></option>
											<option value="3"><lb:get key="label_live_over"/></option>
										</select>
								</div>
							</form>
						</div>
						
						<div role="tabpanel" class="wzb-tab-2">
							<ul class="nav nav-tabs" role="tablist" id="sortul" >
		                        <!-- 最后修改时间 --> 
		                        <li id="lv_upd_datetime" data="asc" role="presentation"  class="active">
			 						<a href="#profile" aria-controls="profile" role="tab" data-toggle="tab">
			 							<lb:get key="label_core_live_management_47"/>
			 							<i class="fa fa-caret-down"></i>
			 						</a>
			 					</li>
		                        <!-- 开始时间 --> 
		                        <li id="lv_start_datetime" data="asc" role="presentation" class="">
			 						<a href="#profile" aria-controls="profile" role="tab" data-toggle="tab">
			 							<lb:get key="start_time"/>
			 							<i class="fa fa-caret-down"></i>
			 						</a>
			 					</li>
		                        <!-- 直播标题  -->
		                        <li id="lv_title" data="asc" role="presentation">
			 						<a href="#home" aria-controls="home" role="tab" data-toggle="tab">
			 							<lb:get key="label_core_live_management_13"/>
			 							<i class="fa fa-caret-down"></i>
			 						</a>
			 					</li>	
		                    </ul>
		 					  <!-- 直播指引  -->
		 					<a href="javascript:void(0);" onclick="setLiveModalTemplate()"  id="live-tutorial" data-toggle="modal" data-target="#model_div"  style="float:right;margin:-22px 0 0 0;display:none;">
                            	<i class="ask-in margin4 allhint-style-1"></i>
		 					</a>
	                   	</div>
	                   	
						<div class="pfind">
						     <div class="wbtable"></div>
						</div>
						
					</div>
				</div>
			</div>
		</div>
		<div class="modal fade"  id="model_div" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		</div>
	</body>
</html>