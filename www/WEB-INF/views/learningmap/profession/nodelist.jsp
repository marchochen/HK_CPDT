<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
	<title></title>
	<script type="text/javascript" src="${ctx}/static/js/front/profession/nodelist.js"></script>
	<script type="text/javascript" src="${ctx}/static/js/cwn_utils.js"></script>
<style>
		body{font-size:20px;color:#644A06;font-family:"Microsoft YaHei";margin:0 auto;background:#FFFFFF;}
		[full]{width:100%;height:100%;}
		#mycontent2,#mycontent2 div,#mycontent3 div{position:absolute;}
		#mycontent2 img,#mycontent3 img{position:absolute;}
        .to2td{
			font-family:"Microsoft YaHei";
			font-size:0.5em;
			color:#FFFFFF;
			line-height:100%;
		}
		.to3td{
			font-family:"Microsoft YaHei";
			font-size:1.0em;
			color:#000000;
			line-height:100%;
		}
		.to3td1{
			font-family:"Microsoft YaHei";
			font-size:1.0em;
			color:#666666;
			line-height:100%;
		}
		.to3td2{
			font-family:"Microsoft YaHei";
			font-size:1.0em;
			color:#FFFFFF;
			line-height:100%;
		}
		.to3td3{
			font-family:"Microsoft YaHei";
			font-size:1.0em;
			color:#FFFFFF;
			line-height:95%;
		}
		.xyd-header{ position:relative;z-index:11;background:#ffffff;}
		.xyd-fixbox{ position:relative;z-index:10;}
		.xyd-bottom{ position:fixed;bottom:0;}
	</style>
	<script type="text/javascript">
		function yoyoEffectTopImg(obj){
			yoyoEffectImg(obj,{top:getNumValue(obj.style.top)+1.946},{top:getNumValue(obj.style.top)-1.946});
		}
		window.onresize=stageCentreSetting;
		window.onorientationchange=stageCentreSetting;
		function stageCentreSetting(){
			var dw = document.documentElement.clientWidth;
			var dh = document.documentElement.clientHeight;
			if(dw!=window.top.document.body.clientWidth){
				if(window.top.document.getElementById("content")){
					dw=window.top.document.body.clientWidth;
					dh=window.top.document.getElementById("content").height;
				}
			}
			var topoffestPx="0";//上面留108px为导航栏
			var downoffestPx="0"//下面留0px为底航栏
			var topoffest=getNumValue(topoffestPx)/dh*100;
			var downoffest=getNumValue(downoffestPx)/dh*100;
			getId("mycontent2").style.top=topoffest+"%";
			getId("mycontent2").style.height=(100-topoffest-downoffest)+"%";
			getId("mycontent3").style.top=topoffest+"%";
			//debugger;
			getId("mycontent3").style.height=(100-topoffest-downoffest)+"%";
			dh-=getNumValue(topoffestPx)+getNumValue(downoffestPx);
			var pw=1280;
			var ph=720;
			var sw=pw/dw;
			var sh=ph/dh;
			var sn=sh/sw;
			var tmpp=sn*100;
			myfn2();
			myfn3();
			function myfn2(){
				//debugger;
				for(var f=1;f<=5;f++){
					getIdClass(getId(pamc2),"F"+f).style.height=100+"%"/* Number(parseFloat(   tmpp   ).toFixed(3))+"%"; */
					//getIdClass(getId(pamc2),"F"+f).style.top=Number(parseFloat(   (f-1)*tmpp   ).toFixed(3))+"%";
				}
				var tdarr=getId(pamc2).getElementsByTagName("td");
				for(var t=0;t<tdarr.length;t++){
					tdarr[t].style.fontSize=Number(parseFloat(   dw/pw*0.8   ).toFixed(3))+"em";  
				}
			}
			
			function myfn3(){
				
				for(var f=1;f<=1;f++){
					getIdClass(getId(pamc3),"F"+f).style.height=Number(parseFloat(   tmpp   ).toFixed(3))+"%";
					getIdClass(getId(pamc3),"F"+f).style.top=Number(parseFloat(   (f-1)*tmpp   ).toFixed(3))+"%";   
				}
				var tdarr=getId(pamc3).getElementsByTagName("td");
				for(var t=0;t<tdarr.length;t++){
					tdarr[t].style.fontSize=Number(parseFloat(   dw/pw*1.0   ).toFixed(3))+"em";  
				}
			}
		}
		$(function(){
			nodelist_init();
		});

	</script>
	<script>
	window.onload = function(){
		$('#prentcontent').height($(window).height()-108-43+"px")
		$(window).resize(function(){
			$('#prentcontent').height($(window).height()-108-43+"px")
		})
		
	}
	
	</script>
</head>
<body full ondragstart="return false;">
<div id="prentcontent" style="padding:0;margin:0;position:relative">
	<div id="mycontent2" style="left:0%;top:0%;width:100%;height:100%;position:absolute;overflow-x:hidden;display:none;">
		<div class="F1" style="left:0%;top:0%;width:100%;height:100%;display:none;">
			<div class="renitem" style="left:0%;top:0%;width:100%;height:100%;overflow:hidden;">
				<img class="p1" style="left:0%;top:0%;width:100%;height:100%;" src="${ctx}/static/images/profession/item/p1.jpg" alt="" />
				<img style="left:31.977%;top:8.75%;width:28.672%;height:36.111%;" src="${ctx}/static/images/profession/item/item3.png" alt="" />
				<img style="left:41.398%;top:22.306%;width:7.109%;height:22.083%;" src="${ctx}/static/images/profession/item/ren2_r.gif" alt="" />
				<img style="left:47.625%;top:1.875%;width:27.422%;height:47.222%;" src="${ctx}/static/images/profession/item/item2.png" alt="" />

				<img style="left:-5.289%;top:43.889%;width:28.984%;height:26.111%;" src="${ctx}/static/images/profession/item/item4.png" alt="" />
				<img style="left:1.508%;top:46.125%;width:7.031%;height:23.611%;" src="${ctx}/static/images/profession/item/ren6.gif" alt="" />
				<img style="left:2.391%;top:61.236%;width:12.812%;height:17.917%;" src="${ctx}/static/images/profession/item/item5.png" alt="" />
				<img style="left:10.844%;top:45.333%;width:6.25%;height:25%;" src="${ctx}/static/images/profession/item/ren5.gif" alt="" />
				<img style="left:19.828%;top:53.556%;width:6.25%;height:16.111%;" src="${ctx}/static/images/profession/item/item1.png" alt="" />

				<img style="left:70.313%;top:44.319%;width:6.25%;height:22.778%;" src="${ctx}/static/images/profession/item/ren1_r.gif" alt="" />
				<img style="left:60.617%;top:51.528%;width:28.672%;height:36.111%;" src="${ctx}/static/images/profession/item/item6.png" alt="" />
				<img style="left:72.758%;top:64.917%;width:7.109%;height:22.083%;" src="${ctx}/static/images/profession/item/ren2_l.gif" alt="" />

				<img style="left:77.641%;top:63.694%;width:28.672%;height:40.694%;" src="${ctx}/static/images/profession/item/item7.png" alt="" />
				<img style="left:98.18%;top:73.944%;width:7.109%;height:22.083%;" src="${ctx}/static/images/profession/item/ren2_l.gif" alt="" />
			</div>
			<div class="house1" style="left:20.055%;top:17.917%;width:11.875%;height:21.25%;">
				<img class="house_bg" style="left:13.816%;top:58.17%;width:71.053%;height:41.83%;" src="${ctx}/static/images/profession/item/housebg.png" alt="" />
				<img class="house_tbg" style="left:0%;top:0%;width:100%;height:73.856%;" src="${ctx}/static/images/profession/item/housetitlebg.png" alt="" />
				<div class="house_t" style="left:1.316%;top:1.961%;width:97.368%;height:50.98%;overflow:hidden;">
					<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0"><tr align="center"><td align="center" valign="middle" class="to2td"></td></tr></table>
				</div>
			</div>
			<div class="house2" style="left:39.328%;top:37.264%;width:11.875%;height:21.25%;">
				<img class="house_bg" style="left:13.816%;top:58.17%;width:71.053%;height:41.83%;" src="${ctx}/static/images/profession/item/housebg.png" alt="" />
				<img class="house_tbg" style="left:0%;top:0%;width:100%;height:73.856%;" src="${ctx}/static/images/profession/item/housetitlebg.png" alt="" />
				<div class="house_t" style="left:1.316%;top:1.961%;width:97.368%;height:50.98%;overflow:hidden;">
					<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0"><tr align="center"><td align="center" valign="middle" class="to2td"></td></tr></table>
				</div>
			</div>
			<div class="house3" style="left:24.406%;top:51.028%;width:11.875%;height:21.25%;">
				<img class="house_bg" style="left:13.816%;top:58.17%;width:71.053%;height:41.83%;" src="${ctx}/static/images/profession/item/housebg.png" alt="" />
				<img class="house_tbg" style="left:0%;top:0%;width:100%;height:73.856%;" src="${ctx}/static/images/profession/item/housetitlebg.png" alt="" />
				<div class="house_t" style="left:1.316%;top:1.961%;width:97.368%;height:50.98%;overflow:hidden;">
					<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0"><tr align="center"><td align="center" valign="middle" class="to2td"></td></tr></table>
				</div>
			</div>
			<div class="house4" style="left:51.203%;top:59.014%;width:11.875%;height:21.25%;">
				<img class="house_bg" style="left:13.816%;top:58.17%;width:71.053%;height:41.83%;" src="${ctx}/static/images/profession/item/housebg.png" alt="" />
				<img class="house_tbg" style="left:0%;top:0%;width:100%;height:73.856%;" src="${ctx}/static/images/profession/item/housetitlebg.png" alt="" />
				<div class="house_t" style="left:1.316%;top:1.961%;width:97.368%;height:50.98%;overflow:hidden;">
					<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0"><tr align="center"><td align="center" valign="middle" class="to2td"></td></tr></table>
				</div>
			</div>
			
		</div>
		<div class="F2" style="left:0%;top:100%;width:100%;height:100%;display:none;">
			<div class="renitem1" style="left:0%;top:0%;width:100%;height:100%;overflow:hidden;">
				<img class="p1" style="left:0%;top:0%;width:100%;height:100%;" src="${ctx}/static/images/profession/item/p2.jpg" alt="" />
				<img style="left:77.641%;top:-36.306%;width:28.672%;height:40.694%;" src="${ctx}/static/images/profession/item/item7.png" alt="" />
			</div>
			<div class="renitem2" style="left:0%;top:0%;width:100%;height:100%;">
				<img style="left:70.625%;top:-24.264%;width:29.375%;height:49.861%;" src="${ctx}/static/images/profession/item/item8.png" alt="" />
				<img style="left:70.359%;top:-9.583%;width:6.25%;height:16.111%;" src="${ctx}/static/images/profession/item/item1.png" alt="" />
				<img style="left:9.5%;top:-19.444%;width:8.438%;height:27.361%;" src="${ctx}/static/images/profession/item/ren3.gif" alt="" />
				<img style="left:19.305%;top:-14.458%;width:7.344%;height:23.611%;" src="${ctx}/static/images/profession/item/ren4.gif" alt="" />
			</div>
			<div class="renitem" style="left:0%;top:0%;width:100%;height:100%;overflow:hidden;">
				<img style="left:67.031%;top:1.333%;width:28.984%;height:26.111%;" src="${ctx}/static/images/profession/item/item4.png" alt="" />
				<img style="left:73.82%;top:3.583%;width:7.031%;height:23.611%;" src="${ctx}/static/images/profession/item/ren6.gif" alt="" />
				<img style="left:74.711%;top:18.681%;width:12.812%;height:17.917%;" src="${ctx}/static/images/profession/item/item5.png" alt="" />
				<img style="left:83.148%;top:2.778%;width:6.25%;height:25%;" src="${ctx}/static/images/profession/item/ren5.gif" alt="" />
				<img style="left:8.297%;top:2.403%;width:6.406%;height:17.778%;" src="${ctx}/static/images/profession/item/item9.png" alt="" />

				<img style="left:-5.289%;top:47.167%;width:28.984%;height:26.111%;" src="${ctx}/static/images/profession/item/item4.png" alt="" />
				<img style="left:1.508%;top:49.389%;width:7.031%;height:23.611%;" src="${ctx}/static/images/profession/item/ren6.gif" alt="" />
				<img style="left:2.391%;top:64.5%;width:12.812%;height:17.917%;" src="${ctx}/static/images/profession/item/item5.png" alt="" />
				<img style="left:10.844%;top:48.597%;width:6.25%;height:25%;" src="${ctx}/static/images/profession/item/ren5.gif" alt="" />
				<img style="left:19.828%;top:56.819%;width:6.25%;height:16.111%;" src="${ctx}/static/images/profession/item/item1.png" alt="" />

				<img style="left:70.313%;top:47.569%;width:6.25%;height:22.778%;" src="${ctx}/static/images/profession/item/ren1_r.gif" alt="" />
				<img style="left:60.617%;top:54.792%;width:28.672%;height:36.111%;" src="${ctx}/static/images/profession/item/item6.png" alt="" />
				<img style="left:72.758%;top:68.181%;width:7.109%;height:22.083%;" src="${ctx}/static/images/profession/item/ren2_l.gif" alt="" />
				<img style="left:77.641%;top:66.958%;width:28.672%;height:40.694%;" src="${ctx}/static/images/profession/item/item7.png" alt="" />
				<img style="left:98.18%;top:77.208%;width:7.109%;height:22.083%;" src="${ctx}/static/images/profession/item/ren2_l.gif" alt="" />
			</div>
			
			<div class="house1" style="left:29.344%;top:-19.847%;width:11.875%;height:21.25%;">
				<img class="house_bg" style="left:13.816%;top:58.17%;width:71.053%;height:41.83%;" src="${ctx}/static/images/profession/item/housebg.png" alt="" />
				<img class="house_tbg" style="left:0%;top:0%;width:100%;height:73.856%;" src="${ctx}/static/images/profession/item/housetitlebg.png" alt="" />
				<div class="house_t" style="left:1.316%;top:1.961%;width:97.368%;height:50.98%;overflow:hidden;">
					<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0"><tr align="center"><td align="center" valign="middle" class="to2td"></td></tr></table>
				</div>
			</div>
			<div class="house2" style="left:57.492%;top:-9.778%;width:11.875%;height:21.25%;">
				<img class="house_bg" style="left:13.816%;top:58.17%;width:71.053%;height:41.83%;" src="${ctx}/static/images/profession/item/housebg.png" alt="" />
				<img class="house_tbg" style="left:0%;top:0%;width:100%;height:73.856%;" src="${ctx}/static/images/profession/item/housetitlebg.png" alt="" />
				<div class="house_t" style="left:1.316%;top:1.961%;width:97.368%;height:50.98%;overflow:hidden;">
					<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0"><tr align="center"><td align="center" valign="middle" class="to2td"></td></tr></table>
				</div>
			</div>
			<div class="house3" style="left:21.484%;top:22.819%;width:11.875%;height:21.25%;">
				<img class="house_bg" style="left:13.816%;top:58.17%;width:71.053%;height:41.83%;" src="${ctx}/static/images/profession/item/housebg.png" alt="" />
				<img class="house_tbg" style="left:0%;top:0%;width:100%;height:73.856%;" src="${ctx}/static/images/profession/item/housetitlebg.png" alt="" />
				<div class="house_t" style="left:1.316%;top:1.961%;width:97.368%;height:50.98%;overflow:hidden;">
					<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0"><tr align="center"><td align="center" valign="middle" class="to2td"></td></tr></table>
				</div>
			</div>
			<div class="house4" style="left:39.328%;top:40.639%;width:11.875%;height:21.25%;">
				<img class="house_bg" style="left:13.816%;top:58.17%;width:71.053%;height:41.83%;" src="${ctx}/static/images/profession/item/housebg.png" alt="" />
				<img class="house_tbg" style="left:0%;top:0%;width:100%;height:73.856%;" src="${ctx}/static/images/profession/item/housetitlebg.png" alt="" />
				<div class="house_t" style="left:1.316%;top:1.961%;width:97.368%;height:50.98%;overflow:hidden;">
					<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0"><tr align="center"><td align="center" valign="middle" class="to2td"></td></tr></table>
				</div>
			</div>
			<div class="house5" style="left:24.406%;top:54.417%;width:11.875%;height:21.25%;">
				<img class="house_bg" style="left:13.816%;top:58.17%;width:71.053%;height:41.83%;" src="${ctx}/static/images/profession/item/housebg.png" alt="" />
				<img class="house_tbg" style="left:0%;top:0%;width:100%;height:73.856%;" src="${ctx}/static/images/profession/item/housetitlebg.png" alt="" />
				<div class="house_t" style="left:1.316%;top:1.961%;width:97.368%;height:50.98%;overflow:hidden;">
					<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0"><tr align="center"><td align="center" valign="middle" class="to2td"></td></tr></table>
				</div>
			</div>
			<div class="house6" style="left:51.203%;top:62.403%;width:11.875%;height:21.25%;">
				<img class="house_bg" style="left:13.816%;top:58.17%;width:71.053%;height:41.83%;" src="${ctx}/static/images/profession/item/housebg.png" alt="" />
				<img class="house_tbg" style="left:0%;top:0%;width:100%;height:73.856%;" src="${ctx}/static/images/profession/item/housetitlebg.png" alt="" />
				<div class="house_t" style="left:1.316%;top:1.961%;width:97.368%;height:50.98%;overflow:hidden;">
					<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0"><tr align="center"><td align="center" valign="middle" class="to2td"></td></tr></table>
				</div>
			</div>
			
		</div>
		<div class="F3" style="left:0%;top:200%;width:100%;height:100%;display:none;">
			<div class="renitem1" style="left:0%;top:0%;width:100%;height:100%;overflow:hidden;">
				<img class="p1" style="left:0%;top:0%;width:100%;height:100%;" src="${ctx}/static/images/profession/item/p3.jpg" alt="" />
				<img style="left:77.641%;top:-33.042%;width:28.672%;height:40.694%;" src="${ctx}/static/images/profession/item/item7.png" alt="" />
			</div>
			<div class="renitem2" style="left:0%;top:0%;width:100%;height:100%;">
				<img style="left:70.625%;top:-21%;width:29.375%;height:49.861%;" src="${ctx}/static/images/profession/item/item8.png" alt="" />
				<img style="left:70.359%;top:-6.319%;width:6.25%;height:16.111%;" src="${ctx}/static/images/profession/item/item1.png" alt="" />
				<img style="left:9.5%;top:-16.181%;width:8.438%;height:27.361%;" src="${ctx}/static/images/profession/item/ren3.gif" alt="" />
				<img style="left:19.305%;top:-11.194%;width:7.344%;height:23.611%;" src="${ctx}/static/images/profession/item/ren4.gif" alt="" />
			</div>
			<div class="renitem" style="left:0%;top:0%;width:100%;height:100%;overflow:hidden;">
				<img style="left:67.031%;top:4.597%;width:28.984%;height:26.111%;" src="${ctx}/static/images/profession/item/item4.png" alt="" />
				<img style="left:73.82%;top:6.847%;width:7.031%;height:23.611%;" src="${ctx}/static/images/profession/item/ren6.gif" alt="" />
				<img style="left:74.711%;top:21.944%;width:12.812%;height:17.917%;" src="${ctx}/static/images/profession/item/item5.png" alt="" />
				<img style="left:83.148%;top:6.042%;width:6.25%;height:25%;" src="${ctx}/static/images/profession/item/ren5.gif" alt="" />
				<img style="left:8.297%;top:5.667%;width:6.406%;height:17.778%;" src="${ctx}/static/images/profession/item/item9.png" alt="" />

				<img style="left:-5.289%;top:50.431%;width:28.984%;height:26.111%;" src="${ctx}/static/images/profession/item/item4.png" alt="" />
				<img style="left:1.508%;top:52.653%;width:7.031%;height:23.611%;" src="${ctx}/static/images/profession/item/ren6.gif" alt="" />
				<img style="left:2.391%;top:67.764%;width:12.812%;height:17.917%;" src="${ctx}/static/images/profession/item/item5.png" alt="" />
				<img style="left:10.844%;top:51.861%;width:6.25%;height:25%;" src="${ctx}/static/images/profession/item/ren5.gif" alt="" />
				<img style="left:19.828%;top:60.083%;width:6.25%;height:16.111%;" src="${ctx}/static/images/profession/item/item1.png" alt="" />

				<img style="left:70.313%;top:50.833%;width:6.25%;height:22.778%;" src="${ctx}/static/images/profession/item/ren1_r.gif" alt="" />
				<img style="left:60.617%;top:58.056%;width:28.672%;height:36.111%;" src="${ctx}/static/images/profession/item/item6.png" alt="" />
				<img style="left:72.758%;top:71.444%;width:7.109%;height:22.083%;" src="${ctx}/static/images/profession/item/ren2_l.gif" alt="" />
				<img style="left:77.641%;top:70.222%;width:28.672%;height:40.694%;" src="${ctx}/static/images/profession/item/item7.png" alt="" />
				<img style="left:98.18%;top:80.472%;width:7.109%;height:22.083%;" src="${ctx}/static/images/profession/item/ren2_l.gif" alt="" />
			</div>

			
			<div class="house1" style="left:29.344%;top:-16.472%;width:11.875%;height:21.25%;">
				<img class="house_bg" style="left:13.816%;top:58.17%;width:71.053%;height:41.83%;" src="${ctx}/static/images/profession/item/housebg.png" alt="" />
				<img class="house_tbg" style="left:0%;top:0%;width:100%;height:73.856%;" src="${ctx}/static/images/profession/item/housetitlebg.png" alt="" />
				<div class="house_t" style="left:1.316%;top:1.961%;width:97.368%;height:50.98%;overflow:hidden;">
					<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0"><tr align="center"><td align="center" valign="middle" class="to2td"></td></tr></table>
				</div>
			</div>
			<div class="house2" style="left:57.492%;top:-6.403%;width:11.875%;height:21.25%;">
				<img class="house_bg" style="left:13.816%;top:58.17%;width:71.053%;height:41.83%;" src="${ctx}/static/images/profession/item/housebg.png" alt="" />
				<img class="house_tbg" style="left:0%;top:0%;width:100%;height:73.856%;" src="${ctx}/static/images/profession/item/housetitlebg.png" alt="" />
				<div class="house_t" style="left:1.316%;top:1.961%;width:97.368%;height:50.98%;overflow:hidden;">
					<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0"><tr align="center"><td align="center" valign="middle" class="to2td"></td></tr></table>
				</div>
			</div>
			<div class="house3" style="left:21.484%;top:26.097%;width:11.875%;height:21.25%;">
				<img class="house_bg" style="left:13.816%;top:58.17%;width:71.053%;height:41.83%;" src="${ctx}/static/images/profession/item/housebg.png" alt="" />
				<img class="house_tbg" style="left:0%;top:0%;width:100%;height:73.856%;" src="${ctx}/static/images/profession/item/housetitlebg.png" alt="" />
				<div class="house_t" style="left:1.316%;top:1.961%;width:97.368%;height:50.98%;overflow:hidden;">
					<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0"><tr align="center"><td align="center" valign="middle" class="to2td"></td></tr></table>
				</div>
			</div>
			<div class="house4" style="left:39.328%;top:43.931%;width:11.875%;height:21.25%;">
				<img class="house_bg" style="left:13.816%;top:58.17%;width:71.053%;height:41.83%;" src="${ctx}/static/images/profession/item/housebg.png" alt="" />
				<img class="house_tbg" style="left:0%;top:0%;width:100%;height:73.856%;" src="${ctx}/static/images/profession/item/housetitlebg.png" alt="" />
				<div class="house_t" style="left:1.316%;top:1.961%;width:97.368%;height:50.98%;overflow:hidden;">
					<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0"><tr align="center"><td align="center" valign="middle" class="to2td"></td></tr></table>
				</div>
			</div>
			<div class="house5" style="left:24.406%;top:57.694%;width:11.875%;height:21.25%;">
				<img class="house_bg" style="left:13.816%;top:58.17%;width:71.053%;height:41.83%;" src="${ctx}/static/images/profession/item/housebg.png" alt="" />
				<img class="house_tbg" style="left:0%;top:0%;width:100%;height:73.856%;" src="${ctx}/static/images/profession/item/housetitlebg.png" alt="" />
				<div class="house_t" style="left:1.316%;top:1.961%;width:97.368%;height:50.98%;overflow:hidden;">
					<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0"><tr align="center"><td align="center" valign="middle" class="to2td"></td></tr></table>
				</div>
			</div>
			<div class="house6" style="left:51.203%;top:65.681%;width:11.875%;height:21.25%;">
				<img class="house_bg" style="left:13.816%;top:58.17%;width:71.053%;height:41.83%;" src="${ctx}/static/images/profession/item/housebg.png" alt="" />
				<img class="house_tbg" style="left:0%;top:0%;width:100%;height:73.856%;" src="${ctx}/static/images/profession/item/housetitlebg.png" alt="" />
				<div class="house_t" style="left:1.316%;top:1.961%;width:97.368%;height:50.98%;overflow:hidden;">
					<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0"><tr align="center"><td align="center" valign="middle" class="to2td"></td></tr></table>
				</div>
			</div>
		</div>
		<div class="F4" style="left:0%;top:300%;width:100%;height:100%;display:none;">
			<div class="renitem1" style="left:0%;top:0%;width:100%;height:100%;overflow:hidden;">
				<img class="p1" style="left:0%;top:0%;width:100%;height:100%;" src="${ctx}/static/images/profession/item/p4.jpg" alt="" />
				<img style="left:77.641%;top:-29.778%;width:28.672%;height:40.694%;" src="${ctx}/static/images/profession/item/item7.png" alt="" />
			</div>
			<div class="renitem2" style="left:0%;top:0%;width:100%;height:100%;">
				<img style="left:70.625%;top:-17.736%;width:29.375%;height:49.861%;" src="${ctx}/static/images/profession/item/item8.png" alt="" />
				<img style="left:70.359%;top:-3.056%;width:6.25%;height:16.111%;" src="${ctx}/static/images/profession/item/item1.png" alt="" />
				<img style="left:9.5%;top:-12.917%;width:8.438%;height:27.361%;" src="${ctx}/static/images/profession/item/ren3.gif" alt="" />
				<img style="left:19.305%;top:-7.931%;width:7.344%;height:23.611%;" src="${ctx}/static/images/profession/item/ren4.gif" alt="" />
			</div>
			<div class="renitem" style="left:0%;top:0%;width:100%;height:100%;overflow:hidden;">
				<img style="left:67.031%;top:7.861%;width:28.984%;height:26.111%;" src="${ctx}/static/images/profession/item/item4.png" alt="" />
				<img style="left:73.82%;top:10.111%;width:7.031%;height:23.611%;" src="${ctx}/static/images/profession/item/ren6.gif" alt="" />
				<img style="left:74.711%;top:25.208%;width:12.812%;height:17.917%;" src="${ctx}/static/images/profession/item/item5.png" alt="" />
				<img style="left:83.148%;top:9.306%;width:6.25%;height:25%;" src="${ctx}/static/images/profession/item/ren5.gif" alt="" />
				<img style="left:8.297%;top:8.931%;width:6.406%;height:17.778%;" src="${ctx}/static/images/profession/item/item9.png" alt="" />

				<img style="left:-5.289%;top:53.694%;width:28.984%;height:26.111%;" src="${ctx}/static/images/profession/item/item4.png" alt="" />
				<img style="left:1.508%;top:55.917%;width:7.031%;height:23.611%;" src="${ctx}/static/images/profession/item/ren6.gif" alt="" />
				<img style="left:2.391%;top:71.028%;width:12.812%;height:17.917%;" src="${ctx}/static/images/profession/item/item5.png" alt="" />
				<img style="left:10.844%;top:55.125%;width:6.25%;height:25%;" src="${ctx}/static/images/profession/item/ren5.gif" alt="" />
				<img style="left:19.828%;top:63.347%;width:6.25%;height:16.111%;" src="${ctx}/static/images/profession/item/item1.png" alt="" />

				<img style="left:70.313%;top:54.097%;width:6.25%;height:22.778%;" src="${ctx}/static/images/profession/item/ren1_r.gif" alt="" />
				<img style="left:60.617%;top:61.319%;width:28.672%;height:36.111%;" src="${ctx}/static/images/profession/item/item6.png" alt="" />
				<img style="left:72.758%;top:74.708%;width:7.109%;height:22.083%;" src="${ctx}/static/images/profession/item/ren2_l.gif" alt="" />
				<img style="left:77.641%;top:73.486%;width:28.672%;height:40.694%;" src="${ctx}/static/images/profession/item/item7.png" alt="" />
				<img style="left:98.18%;top:83.736%;width:7.109%;height:22.083%;" src="${ctx}/static/images/profession/item/ren2_l.gif" alt="" />
			</div>

			<div class="house1" style="left:29.344%;top:-13.208%;width:11.875%;height:21.25%;">
				<img class="house_bg" style="left:13.816%;top:58.17%;width:71.053%;height:41.83%;" src="${ctx}/static/images/profession/item/housebg.png" alt="" />
				<img class="house_tbg" style="left:0%;top:0%;width:100%;height:73.856%;" src="${ctx}/static/images/profession/item/housetitlebg.png" alt="" />
				<div class="house_t" style="left:1.316%;top:1.961%;width:97.368%;height:50.98%;overflow:hidden;">
					<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0"><tr align="center"><td align="center" valign="middle" class="to2td"></td></tr></table>
				</div>
			</div>
			<div class="house2" style="left:57.492%;top:-3.139%;width:11.875%;height:21.25%;">
				<img class="house_bg" style="left:13.816%;top:58.17%;width:71.053%;height:41.83%;" src="${ctx}/static/images/profession/item/housebg.png" alt="" />
				<img class="house_tbg" style="left:0%;top:0%;width:100%;height:73.856%;" src="${ctx}/static/images/profession/item/housetitlebg.png" alt="" />
				<div class="house_t" style="left:1.316%;top:1.961%;width:97.368%;height:50.98%;overflow:hidden;">
					<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0"><tr align="center"><td align="center" valign="middle" class="to2td"></td></tr></table>
				</div>
			</div>
			<div class="house3" style="left:21.484%;top:29.361%;width:11.875%;height:21.25%;">
				<img class="house_bg" style="left:13.816%;top:58.17%;width:71.053%;height:41.83%;" src="${ctx}/static/images/profession/item/housebg.png" alt="" />
				<img class="house_tbg" style="left:0%;top:0%;width:100%;height:73.856%;" src="${ctx}/static/images/profession/item/housetitlebg.png" alt="" />
				<div class="house_t" style="left:1.316%;top:1.961%;width:97.368%;height:50.98%;overflow:hidden;">
					<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0"><tr align="center"><td align="center" valign="middle" class="to2td"></td></tr></table>
				</div>
			</div>
			<div class="house4" style="left:39.328%;top:47.194%;width:11.875%;height:21.25%;">
				<img class="house_bg" style="left:13.816%;top:58.17%;width:71.053%;height:41.83%;" src="${ctx}/static/images/profession/item/housebg.png" alt="" />
				<img class="house_tbg" style="left:0%;top:0%;width:100%;height:73.856%;" src="${ctx}/static/images/profession/item/housetitlebg.png" alt="" />
				<div class="house_t" style="left:1.316%;top:1.961%;width:97.368%;height:50.98%;overflow:hidden;">
					<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0"><tr align="center"><td align="center" valign="middle" class="to2td"></td></tr></table>
				</div>
			</div>
			<div class="house5" style="left:24.406%;top:60.958%;width:11.875%;height:21.25%;">
				<img class="house_bg" style="left:13.816%;top:58.17%;width:71.053%;height:41.83%;" src="${ctx}/static/images/profession/item/housebg.png" alt="" />
				<img class="house_tbg" style="left:0%;top:0%;width:100%;height:73.856%;" src="${ctx}/static/images/profession/item/housetitlebg.png" alt="" />
				<div class="house_t" style="left:1.316%;top:1.961%;width:97.368%;height:50.98%;overflow:hidden;">
					<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0"><tr align="center"><td align="center" valign="middle" class="to2td"></td></tr></table>
				</div>
			</div>
			<div class="house6" style="left:51.203%;top:68.944%;width:11.875%;height:21.25%;">
				<img class="house_bg" style="left:13.816%;top:58.17%;width:71.053%;height:41.83%;" src="${ctx}/static/images/profession/item/housebg.png" alt="" />
				<img class="house_tbg" style="left:0%;top:0%;width:100%;height:73.856%;" src="${ctx}/static/images/profession/item/housetitlebg.png" alt="" />
				<div class="house_t" style="left:1.316%;top:1.961%;width:97.368%;height:50.98%;overflow:hidden;">
					<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0"><tr align="center"><td align="center" valign="middle" class="to2td"></td></tr></table>
				</div>
			</div>
		</div>
		<div class="F5" style="left:0%;top:400%;width:100%;height:100%;display:none;">
			<div class="renitem1" style="left:0%;top:0%;width:100%;height:100%;overflow:hidden;">
				<img class="p1" style="left:0%;top:0%;width:100%;height:100%;" src="${ctx}/static/images/profession/item/p5.jpg" alt="" />
				<img style="left:77.641%;top:-26.514%;width:28.672%;height:40.694%;" src="${ctx}/static/images/profession/item/item7.png" alt="" />
			</div>
			<div class="renitem2" style="left:0%;top:0%;width:100%;height:100%;">
				<img style="left:70.625%;top:-14.472%;width:29.375%;height:49.861%;" src="${ctx}/static/images/profession/item/item8.png" alt="" />
				<img style="left:70.359%;top:0.208%;width:6.25%;height:16.111%;" src="${ctx}/static/images/profession/item/item1.png" alt="" />
				<img style="left:9.5%;top:-9.653%;width:8.438%;height:27.361%;" src="${ctx}/static/images/profession/item/ren3.gif" alt="" />
				<img style="left:19.305%;top:-4.667%;width:7.344%;height:23.611%;" src="${ctx}/static/images/profession/item/ren4.gif" alt="" />
			</div>
			<div class="renitem" style="left:0%;top:0%;width:100%;height:100%;overflow:hidden;">
				<img style="left:67.031%;top:11.125%;width:28.984%;height:26.111%;" src="${ctx}/static/images/profession/item/item4.png" alt="" />
				<img style="left:73.82%;top:13.375%;width:7.031%;height:23.611%;" src="${ctx}/static/images/profession/item/ren6.gif" alt="" />
				<img style="left:74.711%;top:28.472%;width:12.812%;height:17.917%;" src="${ctx}/static/images/profession/item/item5.png" alt="" />
				<img style="left:83.148%;top:12.569%;width:6.25%;height:25%;" src="${ctx}/static/images/profession/item/ren5.gif" alt="" />
				<img style="left:8.297%;top:12.194%;width:6.406%;height:17.778%;" src="${ctx}/static/images/profession/item/item9.png" alt="" />

				<img style="left:-5.289%;top:56.958%;width:28.984%;height:26.111%;" src="${ctx}/static/images/profession/item/item4.png" alt="" />
				<img style="left:1.508%;top:59.181%;width:7.031%;height:23.611%;" src="${ctx}/static/images/profession/item/ren6.gif" alt="" />
				<img style="left:2.391%;top:74.292%;width:12.812%;height:17.917%;" src="${ctx}/static/images/profession/item/item5.png" alt="" />
				<img style="left:10.844%;top:58.389%;width:6.25%;height:25%;" src="${ctx}/static/images/profession/item/ren5.gif" alt="" />
				<img style="left:19.828%;top:66.611%;width:6.25%;height:16.111%;" src="${ctx}/static/images/profession/item/item1.png" alt="" />

				<img style="left:70.313%;top:57.361%;width:6.25%;height:22.778%;" src="${ctx}/static/images/profession/item/ren1_r.gif" alt="" />
				<img style="left:60.617%;top:64.583%;width:28.672%;height:36.111%;" src="${ctx}/static/images/profession/item/item6.png" alt="" />
				<img style="left:72.758%;top:77.972%;width:7.109%;height:22.083%;" src="${ctx}/static/images/profession/item/ren2_l.gif" alt="" />
				<img style="left:77.641%;top:76.75%;width:28.672%;height:40.694%;" src="${ctx}/static/images/profession/item/item7.png" alt="" />
				<img style="left:98.18%;top:87%;width:7.109%;height:22.083%;" src="${ctx}/static/images/profession/item/ren2_l.gif" alt="" />
			</div>

			<div class="house1" style="left:29.344%;top:-9.944%;width:11.875%;height:21.25%;">
				<img class="house_bg" style="left:13.816%;top:58.17%;width:71.053%;height:41.83%;" src="${ctx}/static/images/profession/item/housebg.png" alt="" />
				<img class="house_tbg" style="left:0%;top:0%;width:100%;height:73.856%;" src="${ctx}/static/images/profession/item/housetitlebg.png" alt="" />
				<div class="house_t" style="left:1.316%;top:1.961%;width:97.368%;height:50.98%;overflow:hidden;">
					<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0"><tr align="center"><td align="center" valign="middle" class="to2td"></td></tr></table>
				</div>
			</div>
			<div class="house2" style="left:57.492%;top:0.125%;width:11.875%;height:21.25%;">
				<img class="house_bg" style="left:13.816%;top:58.17%;width:71.053%;height:41.83%;" src="${ctx}/static/images/profession/item/housebg.png" alt="" />
				<img class="house_tbg" style="left:0%;top:0%;width:100%;height:73.856%;" src="${ctx}/static/images/profession/item/housetitlebg.png" alt="" />
				<div class="house_t" style="left:1.316%;top:1.961%;width:97.368%;height:50.98%;overflow:hidden;">
					<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0"><tr align="center"><td align="center" valign="middle" class="to2td"></td></tr></table>
				</div>
			</div>
			<div class="house3" style="left:21.484%;top:32.625%;width:11.875%;height:21.25%;">
				<img class="house_bg" style="left:13.816%;top:58.17%;width:71.053%;height:41.83%;" src="${ctx}/static/images/profession/item/housebg.png" alt="" />
				<img class="house_tbg" style="left:0%;top:0%;width:100%;height:73.856%;" src="${ctx}/static/images/profession/item/housetitlebg.png" alt="" />
				<div class="house_t" style="left:1.316%;top:1.961%;width:97.368%;height:50.98%;overflow:hidden;">
					<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0"><tr align="center"><td align="center" valign="middle" class="to2td"></td></tr></table>
				</div>
			</div>
			<div class="house4" style="left:39.328%;top:50.458%;width:11.875%;height:21.25%;">
				<img class="house_bg" style="left:13.816%;top:58.17%;width:71.053%;height:41.83%;" src="${ctx}/static/images/profession/item/housebg.png" alt="" />
				<img class="house_tbg" style="left:0%;top:0%;width:100%;height:73.856%;" src="${ctx}/static/images/profession/item/housetitlebg.png" alt="" />
				<div class="house_t" style="left:1.316%;top:1.961%;width:97.368%;height:50.98%;overflow:hidden;">
					<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0"><tr align="center"><td align="center" valign="middle" class="to2td"></td></tr></table>
				</div>
			</div>
			<div class="house5" style="left:24.406%;top:64.222%;width:11.875%;height:21.25%;">
				<img class="house_bg" style="left:13.816%;top:58.17%;width:71.053%;height:41.83%;" src="${ctx}/static/images/profession/item/housebg.png" alt="" />
				<img class="house_tbg" style="left:0%;top:0%;width:100%;height:73.856%;" src="${ctx}/static/images/profession/item/housetitlebg.png" alt="" />
				<div class="house_t" style="left:1.316%;top:1.961%;width:97.368%;height:50.98%;overflow:hidden;">
					<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0"><tr align="center"><td align="center" valign="middle" class="to2td"></td></tr></table>
				</div>
			</div>
			<div class="house6" style="left:51.203%;top:72.208%;width:11.875%;height:21.25%;">
				<img class="house_bg" style="left:13.816%;top:58.17%;width:71.053%;height:41.83%;" src="${ctx}/static/images/profession/item/housebg.png" alt="" />
				<img class="house_tbg" style="left:0%;top:0%;width:100%;height:73.856%;" src="${ctx}/static/images/profession/item/housetitlebg.png" alt="" />
				<div class="house_t" style="left:1.316%;top:1.961%;width:97.368%;height:50.98%;overflow:hidden;">
					<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0"><tr align="center"><td align="center" valign="middle" class="to2td"></td></tr></table>
				</div>
			</div>
		</div>
	</div>


	<div id="mycontent3" style="left:0%;top:0%;width:100%;height:95%;position:absolute;overflow-x:hidden;background:url(${ctx}/static/images/profession/course/p1_l3.png);display:none;">
		<div class="F1" style="left:0%;top:0%;width:100%;height:100%;display:none;">
			<img class="cbg_c" style="left:22.031%;top:32.361%;width:55.937%;height:4.722%;" src="${ctx}/static/images/profession/course/cbg_c.png" alt="" />
			<img class="cbg_d" style="left:22.031%;top:37.083%;width:55.937%;height:50.694%;" src="${ctx}/static/images/profession/course/cbg_d.png" alt="" />
			<img class="cbg_t" style="left:22.031%;top:12.222%;width:55.937%;height:20.139%;" src="${ctx}/static/images/profession/course/cbg_t.png" alt="" />
			<div class="cbg_txt" style="left:22.422%;top:12.917%;width:55.313%;height:4.861%;overflow:hidden;">
				<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0"><tr align="center"><td align="center" valign="middle" class="to3td2"></td></tr></table>
			</div>
			<img class="cbg_closebtn" style="left:74.219%;top:11.806%;width:3.906%;height:6.944%;" src="${ctx}/static/images/profession/course/cbg_closebtn.png" alt="" />
			<div class="house1" style="left:23.984%;top:28.472%;width:52.188%;height:29.722%;display:none;">
				<img class="house_bg" style="left:0%;top:0%;width:100%;height:100%;" src="${ctx}/static/images/profession/course/housebg_l3.png" alt="" />
				<img class="house_tbg" style="left:2.246%;top:19.626%;width:33.383%;height:64.019%;" src="${ctx}/static/images/profession/course/icon01.png" alt="" />

				<img class="house_compulsory0" style="left:2.246%;top:73.832%;width:5.988%;height:9.813%;" src="${ctx}/static/images/profession/course/compulsory0.png" alt="" />
				<img class="house_compulsory1" style="left:2.246%;top:73.832%;width:5.988%;height:9.813%;" src="${ctx}/static/images/profession/course/compulsory1.png" alt="" />
				<img class="house_notcanread" style="left:0%;top:15.421%;width:6.138%;height:19.159%;" src="${ctx}/static/images/profession/course/notcanread.png" alt="" />

				<div class="house_t" style="left:41.617%;top:17.29%;width:57.784%;height:28.972%;">
					<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0"><tr><td align="left" valign="top" class="to3td" style="width:486px;margin:3px 0 0 0;line-height:22px;height:22px;display:-webkit-box;display:-moz-box;display:-ms-flexbox;display:-o-box;overflow: hidden;text-overflow: ellipsis;"></td></tr></table>
				</div>
				<div class="house_t1" style="left:41.617%;top:31.215%;width:57.784%;height:11.682%;">
					<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0"><tr align="center"><td align="left" valign="middle" class="to3td1"></td></tr></table>
				</div>
				<div class="house_t2" style="left:41.617%;top:41.065%;width:57.784%;height:11.682%;">
					<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0"><tr align="center"><td align="left" valign="middle" class="to3td1"></td></tr></table>
				</div>
				<div class="house_t3" style="left:41.617%;top:52.215%;width:57.784%;height:11.682%;">
					<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0"><tr align="center"><td align="left" valign="middle" class="to3td1" style="height:48px; line-height:22px; overflow:hidden; display:block;text-overflow:ellipsis;-webkit-line-clamp:2;display: -webkit-box;-webkit-box-orient: vertical;"></td></tr></table>
				</div>
				<div class="house_t4" style="left:2.246%;top:73.832%;width:33.383%;height:9.813%;">
					<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0"><tr align="center"><td align="left" valign="middle" class="to3td3"></td></tr></table>
				</div>
			</div>
		</div>
	</div>
</div>
</body>
</html>