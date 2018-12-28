<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title></title>
    <script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_lvm_${lang}.js"></script>
    <c:if test="${lv.lv_mode_type != 'VHALL' }">
	    <script src="${ctx}/static/js/qcloudPlayer.js" charset="utf-8"></script>
    </c:if>
    <script type="text/javascript">
    
    if('${lv_excess}' != 'true'){ //退出时调用。双重保障
    	window.onbeforeunload = function(event) { 
    		$.ajax({ 
				url: "${ctx }/app/live/updateRecordsStatus/${lv.lv_enc_id}", 
				data:{  
					status:0
				}, 
				dataType:'json',
				success: function(data){
      			}
			});
   		}
	}
    
    $(function(){
    	if(${lv_excess}){ //直播观众已满，请稍后再尝试
    		$('#bg-rgba').show();
    		Dialog.alert(fetchLabel("label_core_live_management_43"),function(){
    			window.history.back();
    		});
    		return;
    	}
    	if(${lv.lv_need_pwd }){ //需要输入密码时
    		$('#bg-rgba').show();
	    	$('#msgBox').modal('show');
    	}
    	
    	if('${lv_excess}' != 'true'){ //退出时
			$(window).bind('beforeunload',function(){
				$.ajax({ 
					url: "${ctx }/app/live/updateRecordsStatus/${lv.lv_enc_id}", 
					data:{  
						status:0
					}, 
					dataType:'json',
					success: function(data){
	      			}
				});
			});
		}
    	
    	$("#submitPwd").click(function(){
			if($("input[name='password']").val() == ""){
				Dialog.alert(fetchLabel("label_core_live_management_20"));
				return;
			}
			
			$.ajax({ 
				url: "/app/live/checkPwd", 
				data:{  
					password : $("input[name='password']").val(),
					lv_enc_id : '${lv.lv_enc_id}'
				}, 
				dataType:'json',
				type:'post',
				success: function(data){
					var result = data.status;
					if(result){
						$('#msgBox').modal('hide');
						$('#bg-rgba').hide();
					}else{
						Dialog.alert(fetchLabel("label_core_live_management_23"));
					}							
      			}
			});
		});
    	
    });
    
    </script>
</head>
<body>
	
	<div id="bg-rgba" style="width:100%;height:100%;background:rgba(0,0,0,0.7);position:fixed;top:0;left:0;z-index:999;display:none;"></div>
	<div class="xyd-wrapper" style="height:80%;">
		<div class="xyd-main clearfix" style="height:100%;">
	    	
	    	<div class="" style="width:100%;height:100%;">
	    		<c:choose>
	    			<c:when test="${lv.lv_mode_type == 'GENSEE' }">
	    				<iframe id="liveIframe" name="liveIframe" frameborder="no" border="0" src=""  width="100%" style="height: 100%;border: 0;"></iframe>
	    				<script>
	    					var iframe_url = '${lv.lv_student_join_url }';
	    					if('${lv.lv_mode_type }' == "GENSEE"){
	    						iframe_url = '${lv.lv_gensee_record_url }';
	    					}
	    					$('#liveIframe').attr('src', iframe_url+ '?nickname=' + encodeURIComponent('${prof.usr_display_bil }'));
	    				</script>
	    			</c:when>
	    			<c:when test="${lv.lv_mode_type == 'VHALL' }">
		   				<iframe name="liveIframe" frameborder="no" border="0" src="${lv.lv_url }" width="100%" style="height: 100%;border: 0;"></iframe>
	    			</c:when>
	    			<c:otherwise>
	    				<div id="id_test_video" style="width:100%; height:100%;"></div>
	    				<script type="text/javascript">
		    				var player = new TcPlayer('id_test_video', {
								"m3u8": "${lv.lv_hls_downstream_address}",
								"flv": "${lv.lv_flv_downstream_address}", //增加了一个flv的播放地址，用于PC平台的播放
								"autoplay" : true,      //iOS下safari浏览器是不开放这个能力的
								"coverpic" : "",
								"wording": {
									1: fetchLabel("label_core_live_management_89"),
									2: fetchLabel("label_core_live_management_90"),
									3: fetchLabel("label_core_live_management_89"),
									4: fetchLabel("label_core_live_management_91"),
									1001: fetchLabel("label_core_live_management_89"),
									1002: fetchLabel("label_core_live_management_91"),
								    2032: fetchLabel("label_core_live_management_92"),
								    2048: fetchLabel("label_core_live_management_93")
								},
								'live' : true,
								"width" :  '100%',//视频的显示宽度，请尽量使用视频分辨率宽度
								"height" : '100%'//视频的显示高度，请尽量使用视频分辨率高度
							});
	    				</script>
	    			</c:otherwise>
	    		</c:choose>
		    </div>
		    
		    <div id="msgBox" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" data-backdrop="false" data-keyboard="false" aria-hidden="true">
				<div class="modal-dialog" style="width:480px;margin:-155px 0 0 -240px;">
					<div class="modal-content cont">
		           		<div class="modal-header">
		               		<h4 class="modal-title pfindtit" id="myModalLabel">
		               			<lb:get key="label_core_live_management_41"/>
		               		</h4>
		               	</div>
		                                
		                <div class="wzb-model-3" style="height:100px;overflow-y:auto;">
		                    <div class="modal-body"  style="margin-left:50px;">
		                    	<lb:get key="usr_password"/>：<input type="password" class="form-control" name="password" style="width:250px;" placeholder="<lb:get key='label_core_live_management_42'/>" />
		                    </div>
		                </div>
		                <div class="modal-footer">
							<button type="button" id="submitPwd" class="btn wzb-btn-blue wzb-btn-big"><lb:get key="button_ok"/></button>
							<button type="button" class="btn wzb-btn-blue wzb-btn-big" data-dismiss="modal" style="margin-bottom:2px" onclick="javascript:history.back();">
								<lb:get key="button_cancel"/>
							</button>
						</div>
					</div>
				</div>
			</div>
		    
		</div>
	</div>
</body>
</html>