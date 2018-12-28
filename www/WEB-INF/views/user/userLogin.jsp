<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<script type="text/javascript" src="${ctx}/js/wb_login.js"></script>
<script type="text/javascript" src="${ctx}/js/urlparam.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery.prompt.js"></script>
<script type="text/javascript" src="${ctx}/static/js/browser.js"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_${lang}.js"></script>
<script type="text/javascript" src="${ctx}/static/js/login_label.js"></script>
<link rel="stylesheet" href="${ctx}/static/css/learner.css"/>

<style>
.mybtn{ display:inline-block; padding:5px 10px; font-family:"Microsoft YaHei","微软雅黑"; /*color:#fff; background:#ffb900;*/}
</style>


<script type="text/javascript">
    var userSaveInfo = '';
	MyLogin = new wbLogin;
	MyLogin.changePage();

	function checkRemember(){
		var courseID = getUrlParam('course');
        var userID = getUrlParam('userID');
        var type = getUrlParam('type');
        var appID = getUrlParam('appID');
        var action_ent_id = getUrlParam('action_ent_id');
        var acc_ent_id= getUrlParam('acc_ent_id');
        if(courseID.length > 0){
             $("input[name='courseID']").val(courseID);
        }
        if(userID.length > 0){
            $("input[name='userID']").val(userID);
        }
        if(action_ent_id.length > 0){
            $("input[name='action_ent_id']").val(action_ent_id);
        }
        if(appID.length > 0){
            $("input[name='appID']").val(appID);
        }
        if(type.length != ''){
            $("input[name='type']").val(type);
        }
        if(acc_ent_id.length != ''){
            $("input[name='acc_ent_id']").val(acc_ent_id);
        }
		
		var checkbox_status = $("#user_checked").attr("checked");
		if(checkbox_status == 'checked'){
			var usrID = $("#usr_id").val();
			if(usrID != 'User ID')
				saveUserInfo(usrID);
		}else{
			delCookie();
		}
	}
	
	function getUserInfo(){
        var arr = document.cookie.match(new RegExp("(^| )"+"usrID"+"=([^;]*)(;|$)"));  
        if(arr != null){  
         return unescape(arr[2]);  
        }else{  
         return null;  
        }  
    }
	function delCookie()
	{
	    var exp = new Date();
	    exp.setTime(exp.getTime() - 1);
	    var cval=getUserInfo();
	    if(cval!=null){
	    	document.cookie= "usrID" + "="+cval+";expires="+exp.toGMTString();	
	    }
	} 
	function saveUserInfo(userID){
        var d= new Date();
        d.setHours(d.getHours() + (24 * 30)); //保存一个月
        //var day = formatTime(value);
        document.cookie ="usrID"+'='+escape(userID)+'; expires='+d.toGMTString();  
    }
	$(function(){
		$("#usr_id").focus();
		$("#login_welcome").html(labels['${lang}']['login_welcome']);
		$("#login_id").html(labels['${lang}']['usr_name']);
		$("input[name='lab_login_id']").val(labels['${lang}']['usr_name']);
		$("#login_pwd").html(labels['${lang}']['usr_password']);
		$("input[name='lab_passwd']").val(labels['${lang}']['usr_password']);
		$("input[name='frmSubmitBtn']").val(labels['${lang}']['login']);
		$("#login_remember").html(labels['${lang}']['login_remember']);
		$("#login_forget_pwd").html(labels['${lang}']['login_forget_password']);
		$("input[name='login_lan']").val('${lang}');
		$("input[name='lab_login_fail']").val(labels['${lang}']['error_login_fail']);
		$("input[name='lab_login_fail_08']").val(labels['${lang}']['error_lab_login_fail_08']);
		$("input[name='lab_trial_limit1']").val(labels['${lang}']['error_trial_limit1']);
		$("input[name='lab_trial_limit2']").val(labels['${lang}']['error_trial_limit2']);
		$("input[name='lab_account_suspended']").val(labels['${lang}']['error_account_suspended']);
		$("input[name='lab_over_validity_period']").val(labels['${lang}']['error_over_validity_period']);
	})
	
	$(function(){
		$("#user_checked").click(function(){
			var checkbox_status = $("#user_checked").attr("checked");
			if(checkbox_status == 'checked'){
				var usrID = $("#usr_id").val();
				if(usrID != 'User ID')
					saveUserInfo(usrID);
			}else{
				delCookie();
			}
		});
		userSaveInfo = getUserInfo();
		if(userSaveInfo != null){
			$("#usr_id").attr("value",userSaveInfo);
			$("#user_checked").attr("checked",'true');
		}
			
		
		MyLogin.init(document.frmLogin,1,'${encoding}','${code}');
	})
	
	$(function(){
		$("#usr_id").siblings("em").hide();
		var numpic = $('#xyd-move-pic li').size()-1;
		var nownow = 0;
		var inout = 0;
		var TT = 0;
		var SPEED = 5000;
	
		$('#xyd-move-pic li').eq(0).siblings('li').css({'display':'none'});
	
		var ulstart = '<ul id="xyd-move-pagination">',
			ulcontent = '',
			ulend = '</ul>';
		ADDLI();
		var pagination = $('#xyd-move-pagination li');
		var paginationwidth = $('#xyd-move-pagination').width();
		
		pagination.eq(0).addClass('current')
			
		function ADDLI(){
			//var lilicount = numpic + 1;
			for(var i = 0; i <= numpic; i++){
				ulcontent += '<li></li>';
			}
			if(numpic > 0){
				$('#xyd-move-pic').after(ulstart + ulcontent + ulend);	
			}
		}
	
		pagination.on('click',DOTCHANGE)
		
		function DOTCHANGE(){
			var changenow = $(this).index();
			
			$('#xyd-move-pic li').eq(nownow).css('z-index','66');
			$('#xyd-move-pic li').eq(changenow).css({'z-index':'55'}).show();
			pagination.eq(changenow).addClass('current').siblings('li').removeClass('current');
			$('#xyd-move-pic li').eq(nownow).fadeOut(400,function(){$('#xyd-move-pic li').eq(changenow).fadeIn(500);});
			nownow = changenow;
		}
		
		pagination.mouseenter(function(){
			inout = 1;
		})
		
		pagination.mouseleave(function(){
			inout = 0;
		})
		
		function GOGO(){
			var NN = nownow+1;		
			if( inout == 1 ){
				} else {
				if(nownow < numpic){
				$('#xyd-move-pic li').eq(nownow).css('z-index','66');
				$('#xyd-move-pic li').eq(NN).css({'z-index':'55'}).show();
				pagination.eq(NN).addClass('current').siblings('li').removeClass('current');
				$('#xyd-move-pic li').eq(nownow).fadeOut(400,function(){$('#xyd-move-pic li').eq(NN).fadeIn(500);});
				nownow += 1;
			}else{
				NN = 0;
				$('#xyd-move-pic li').eq(nownow).css('z-index','66');
				$('#xyd-move-pic li').eq(NN).stop(true,true).css({'z-index':'55'}).show();
				$('#xyd-move-pic li').eq(nownow).fadeOut(400,function(){$('#xyd-move-pic li').eq(0).fadeIn(500);});
				pagination.eq(NN).addClass('current').siblings('li').removeClass('current');
	
				nownow=0;
				}
			}
			if(numpic > 0){
				TT = setTimeout(GOGO, SPEED);
			}
		}
		if(numpic > 0){
			TT = setTimeout(GOGO, SPEED); 
		}
		
		if(window.innerHeight){
			winHeight = window.innerHeight;
		}
		else if ((document.body) && (document.documentElement.clientHeight)){
			winHeight = document.documentElement.clientHeight; 
		}
		
		/* if(${show_login_header_ind} == true){
		
		}else{	
		  //$("#xyd-move").height(winHeight-1);   自适应浏览器的高度 
		  //$(".xyd-form-box").css("top", '180px');
		} */
		
		process();// 浏览器类型处理
	});
	
	function changeLan() {
		var lang = $("#lan_sel option:selected").val();
		$("#usr_id").focus();
		/* $("#login_welcome").html(labels[lang]['login_welcome']);
		$("#login_id").html(labels[lang]['usr_name']);
		$("input[name='lab_login_id']").val(labels[lang]['usr_name']);
		$("#login_pwd").html(labels[lang]['usr_password']);
		$("input[name='lab_passwd']").val(labels[lang]['usr_password']);
		$("input[name='frmSubmitBtn']").val(labels[lang]['login']);
		$("#login_remember").html(labels[lang]['login_remember']);
		$("#login_forget_pwd").html(labels[lang]['login_forget_password']);
		$("input[name='login_lan']").val(lang);
		 */
		var err_code = '${code}';
		var err_msg_txt = '';
		if(err_code != '') {
			switch (err_code) {
				case "" :
					err_msg_txt = "";
					break;
				case "LGF05" :
					err_msg_txt = labels[lang]['error_account_suspended'];
					break;
				case "LGF08" :
					err_msg_txt = labels[lang]['error_lab_login_fail_08'];
					break;
				case "LGF09" :
					err_msg_txt = labels[lang]['error_over_validity_period'];
					break;
				case "LGF13" :
					err_msg_txt = labels[lang]['error_user_system_issue'];
					break;
				default :
					err_msg_txt = labels[lang]['error_login_fail'];
					var maxTrialObj = '${loginMaxTrial}';
					var login_is_active = '${isActive}';
					if (maxTrialObj !== undefined && maxTrialObj.length > 0 && login_is_active == "true") {
						err_msg_txt += (labels[lang]['error_trial_limit1'] + maxTrialObj + labels[lang]['error_trial_limit2']);
					}
			}
			if (err_msg_txt !== '') {
				$(".xyd-form-error span").html(err_msg_txt);
			}
		}
		//var url = '/app/user/userLogin/$?lang=' + lang;
		//Bug 17696 - 登录页面，输错密码出现提示后，转换语言，提示会不见 
		var url = window.location.href;
		if(url.indexOf("?") != -1) {
			url = url.split("?")[0];
		}
		url += '?lang=' + lang;
		window.location.href = url;
	}
	
	function gotoPage() {
		var lang = $("#lan_sel option:selected").val();
		window.location.href = '${ctx}/app/user/forgetPassword/$?lang=' + lang;
	}
	
	$(function() {
		 $("#lan_sel option[value='${lang}']").attr("selected", true);		 
		 //$("#usr_id").prompt("<lb:get key='usr_name'/>" );
		 $("#usr_id").click();
		 $("#usr_id").focus(function(){
			 if($(this).val() == '<lb:get key="usr_name"/>'){
				 $(this).val('');
				 $(this).css('color', '#000'); 
			 }			 
		 }).blur(function(){
			 if($(this).val() == '<lb:get key="usr_name"/>'){
				 $(this).css('color', '#999');
			 }else{
				 $(this).css('color', '#000');	 
			 }
		 });
		 $(".xyd-form-user").focus(function(){
			 $(this).siblings("em").hide();
			 $(this).css('color','#000');
		 }).blur(function(){
			 if($(".xyd-form-user").val().length == 0){
				 $(this).siblings("em").show(); 
				 $(this).css('color','#999');
			 }else if($(".xyd-form-user").val().length > 0){
			     $(this).siblings("em").hide();
			     $(this).css('color','#000');
			 }
		 });
		 $(".xyd-form-pass").focus(function(){
			 $(this).siblings("em").hide();
			 $(this).css('color','#000');
		 }).blur(function(){
			 if($(".xyd-form-pass").val().length == 0){
				 $(this).siblings("em").show(); 
				 $(this).css('color','#999');
			 }else if($(".xyd-form-pass").val().length > 0){
			     $(this).siblings("em").hide();
			     $(this).css('color','#000');
			 }
		 });
		 var user_id = '${user_id}';
		 var code = '${code}';
		 if(code == "") {
			 if(user_id != null && user_id != '') {
				 $("input[name='usr_id']").val(user_id);
				 $("input[name='usr_pwd']").focus();
			 }
		 }
		 
		 $(".xyd-form-pass").siblings("em").click(function(){$(".xyd-form-pass").focus()});
	})
	
	function emHide(){
		$(".xyd-form-pass").siblings("em").hide();
	}
	
	var drag_ = false; 
	var D = new Function('obj', 'return document.getElementById(obj);'); 
	var oevent = new Function('e', 'if (!e) e = window.event;return e'); 
	function Move_obj(obj) {
		
		var x, y; 
		D("wizform").onmousedown = function(e) { 
			drag_ = true;	 
			with (this) {	 
				D("wizbox").style.position = "absolute";		 
				var temp1 = D("wizbox").offsetLeft; //距离左边的初始值		 
				var temp2 = D("wizbox").offsetTop; //距离顶边的初始值		 
				x = oevent(e).clientX;		 
				y = oevent(e).clientY;		 	 
				document.onmousemove = function(e) {
					if($("#usr_pwd").hasClass("input-focus") || $("#usr_id").hasClass("input-focus")){
						return;
					}
					if (!drag_) {			 
						return false;			 
					}			 
					with (this) {			 
						D("wizbox").style.left = temp1 + oevent(e).clientX - x + "px"; //层离左边距的像素			 
						D("wizbox").style.top = temp2 + oevent(e).clientY - y + "px"; //层离顶部距的像素			 
					}		 
				}	 
			}	 
			document.onmouseup = new Function("drag_=false"); 
		} 
	}
	
	$(document).ready(function(){
		var video = document.getElementById("wiz-video");
		if(video){
			video.addEventListener('canplaythrough',function(){
				$(".full-slider-photo").css("display","none");
			});
		}
	})
	
	document.onkeydown = function (event) {
                var e = event || window.event || arguments.callee.caller.arguments[0];
                if (e && e.keyCode == 13) {
                	if(document.querySelector('#qtip-noticeTip') != null){
                		return false;
                	}
                }
            };
	
</script>
</head>
<body>
	<div class="xyd-selected-box">
		<select style="width:100px;" id="lan_sel" onchange="changeLan()">
			<option value="en-us"><lb:get key="language_en_us"/></option>
			<option value="zh-cn"><lb:get key="language_zh_cn"/></option>
			<option value="zh-hk"><lb:get key="language_zh_hk"/></option>
		</select>
	</div>
	<div class="xyd-wrapper">
		<div class="xyd-form">
			<div class="xyd-form-box" id="wizbox">
				<div class="xyd-form-main"  id="wizform" >
					<div class="xyd-form-bg"></div>
					<div class="xyd-form-content">
						<form name="frmLogin" method="post" onsubmit="checkRemember();MyLogin.submitFrm(document.frmLogin,'${label_lan}'); return false;">
							<div class="xyd-form-title" id="login_welcome"></div>
							<div class="xyd-form-info xyd-form-usr">
								<em id="login_id"></em>
								<input onblur="$(this).removeClass('input-focus')" onfocus="$(this).addClass('input-focus')" type="text" value="" name="usr_id" id="usr_id" class="xyd-form-text xyd-form-user">
							</div>
							<div class="xyd-form-info xyd-form-two">	
								<em id="login_pwd"></em>
								<input onblur="$(this).removeClass('input-focus')" autocomplete="off" onfocus="$(this).addClass('input-focus')" type="password" value="" name="usr_pwd" id="usr_pwd" class="xyd-form-text xyd-form-pass" oninput="emHide()">
							</div>
							<div class="xyd-form-bar">
								<input type="submit" name="frmSubmitBtn" class="xyd-form-button" value=''>
							</div>
							<div class="xyd-form-area clearfix">
								<span class="pull-left">
									<input type="checkbox" id="user-checked" name="user-checked" class="xyd-form-checked" value="1" style="cursor:pointer;" />
									<span id="login_remember"></span>
								</span> 
								<a class="wzb-link06 pull-right" href="javascript:gotoPage();">
									<span id="login_forget_pwd"></span>
								</a>
							</div>
							<input type="hidden" name="courseID" value="" />
                            <input type="hidden" name="userID" value="" />
                            <input type="hidden" name="type" value="" />
                            <input type="hidden" name="appID" value="" />
                            <input type="hidden" name="action_ent_id" value="" />
                            <input type="hidden" name="acc_ent_id" value="" />
                            
							<input type="hidden" name="site_id" value="${ste_ent_id}">
							<input type="hidden" name="url_success" value="" /> 
							<input type="hidden" name="url_change_pwd" value="" /> 
							<input type="hidden" name="stylesheet" value="" /> 
							<input type="hidden" name="url_failure" value="${ctx}/app/user/userLogin/$" /> 
							<input type="hidden" name="style" value="" /> 
							<input type="hidden" name="label_lan" value="" />
							<input type="hidden" name="login_lan" value="${lang }"> 
							<input type="hidden" name="cmd" value="" /> 
							<input type="hidden" name="module" value="" /> 
							<input type="hidden" name="lab_login_id" value='<lb:get key="usr_name"/>'> 
							<input type="hidden" name="lab_passwd" value='<lb:get key="usr_password"/>'> 
							<input type="hidden" name="site_style1" value=""> 
							<input type="hidden" name="site_login_max_trial1" value="${loginMaxTrial}">
							<input type="hidden" name="login_is_active1" value="${isActive}"> 
							<input type="hidden" name="lab_login_fail" value=''>
							<input type="hidden" name="lab_login_fail_08" value=''> 
							<input type="hidden" name="lab_trial_limit1" value=''> 
							<input type="hidden" name="lab_trial_limit2" value=''> 
							<input type="hidden" name="lab_account_suspended" value=''> 
							<input type="hidden" name="lab_over_validity_period" value=''>
							<input type="hidden" name="lab_error_user_system_issue" value='<lb:get key="error_user_system_issue"/>'>
						</form>
					</div>
				</div>
				<div class="xyd-form-error" style="display: none;">
					<i class="fa fa-exclamation-triangle"></i><span>
					<lb:get
							key="error_trial_login_limit" />
					</span>
				</div>
			</div>
		</div>

		<!-- 添加视频 开始 -->
		<c:if test="${!empty sitePoster.login_bg_type and sitePoster.login_bg_type == 'VOD'}">
			
			<div class="xyd-full-slider-2">
		        <div class="xyd-header-overlay-pc"></div>
		        <video class="xyd-f-video" loop="" muted="" autoplay="" id="xyd-wiz-video">
		           <c:if test="${!empty sitePoster.login_bg_video}">
					   <source src="${ctx}/poster/loginPage/${site}/${sitePoster.login_bg_video}">  
					</c:if>
					<c:if test="${empty sitePoster.login_bg_video}">
					   <source src="${ctx}/static/images/wiz-video.mp4">  
					</c:if>  
		        </video>
		        <div class="xyd-full-slider-photo" style="display: none;">
		             <c:if test="${!empty sitePoster.login_bg_file5}">
					   <img src="${ctx}/poster/loginPage/${site}/${sitePoster.login_bg_file5}" alt="" height="100%" width="100%">
					</c:if>
					  <c:if test="${empty sitePoster.login_bg_file5}">
					   <img src="${ctx}/static/images/adv67.jpg" alt="" width="100%" style="margin-top:92px;"> 
					</c:if>
		        </div>
		    </div>
			
		</c:if>
		<!-- 添加视频结束-->
		
		<!-- 图片轮播 -->
      	<c:if test="${!empty sitePoster.login_bg_type and sitePoster.login_bg_type == 'PIC'}">
	      	<div id="xyd-move">
				<ul id="xyd-move-pic">
					<c:if test="${empty sitePoster.login_bg_file1 and empty sitePoster.login_bg_file2  and empty sitePoster.login_bg_file3  and empty sitePoster.login_bg_file4  }">
						<li style="background:url('${ctx}/poster/loginPage/cw/banner01.jpg') no-repeat center top"></li>
					</c:if>
					<c:if test="${!empty sitePoster.login_bg_file1}">
						<li
							style="background:url('${ctx}/poster/loginPage/${site}/${sitePoster.login_bg_file1}') no-repeat center center"></li>
					</c:if>
					<c:if test="${!empty sitePoster.login_bg_file2}">
						<li
							style="background:url('${ctx}/poster/loginPage/${site}/${sitePoster.login_bg_file2}') no-repeat center center"></li>
					</c:if>
					<c:if test="${!empty sitePoster.login_bg_file3}">
						<li
							style="background:url('${ctx}/poster/loginPage/${site}/${sitePoster.login_bg_file3}') no-repeat center center"></li>
					</c:if>
					<c:if test="${!empty sitePoster.login_bg_file4}">
						<li
							style="background:url('${ctx}/poster/loginPage/${site}/${sitePoster.login_bg_file4}') no-repeat center center"></li>
					</c:if>
				</ul>
			</div>
		</c:if>
	
	</div>
	<!-- xyd-wrapper End-->

    <c:if test="${!empty sitePoster.login_bg_type and sitePoster.login_bg_type == 'VOD'}">
		<div class="xyd-f-photo">
	    	<c:if test="${!empty sitePoster.login_bg_file5}">
			   <img src="${ctx}/poster/loginPage/${site}/${sitePoster.login_bg_file5}" alt="" width="100%" style="margin-top:93px;">
			</c:if>
			 <c:if test="${empty sitePoster.login_bg_file5}">
			   <img src="${ctx}/static/images/adv67.jpg" alt="" width="100%" style="margin-top:92px;"> 
			</c:if>
	    </div>
	</c:if>

	<div class="modal fade" id="favoriteFunsModal" tabindex="-1" role="dialog"  >
		<div class="modal-dialog"><!--  style="margin:180px auto 0;" -->
			<div class="modal-content" style="height:400px;">
				<div class="modal-body">
					<div class="mode">
						<div class="losepage sessionTimeOut">
							<div class="losepage_tit" style="font-size:20px;">
								<lb:get key="global.lab_browser_error_title" />
							</div>
							<div class="losepage_info">
								<lb:get key="global.lab_browser_error_content" />
							</div>
							 <div class="losepage_desc">
								<lb:get key="global.lab_browser_version_ie" /><br/>
								<lb:get key="global.lab_browser_version_safari" /><br/>
								<lb:get key="global.lab_browser_version_chrome" /><br/>
								<lb:get key="global.lab_browser_version_firefox" />
							</div>
							 <c:if test="${lang == 'en-us'}">  
								<div class="losepage_info">
									<lb:get key="global.lab_browser_error_content_end" />
								</div>
						     </c:if> 
						</div>
						<div style="margin:40px auto 0; display:block; width:308px;">
							<button type="button" class="btn  mybtn wzb-btn-yellow" id="favoriteFunsSubmit" style="margin-right:20px" >
								<lb:get key="global.lab_browser_button_update" />
							</button>  
							<button type="button" class="btn  mybtn wzb-btn-yellow"  data-dismiss="modal">
								<lb:get key="global.lab_browser_button_reject" />
							</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>