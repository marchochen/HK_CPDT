<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
	<title></title>
	<style>
		body { font-size:20px; color:#644A06; font-family:"Microsoft YaHei"; overflow-x:hidden; margin:0 auto; background:#FFFFFF;}
		[full] { width:100%; height:100%;}
		#mycontent div{position:absolute;}
		#mycontent img{position:absolute;}
		.totd { font-family:"Microsoft YaHei"; font-size:0.5em; color:#644A06; line-height:100%;}
		.xyd-header{ position:relative;z-index:11;background:#ffffff;}
		.xyd-fixbox{ position:relative;z-index:10;}
		.xyd-bottom{ position:fixed;bottom:0;}
	</style>
	<script type="text/javascript" src="${ctx}/static/js/front/profession/orderlist.js"></script>
	<script type="text/javascript">
	function yoyoEffectTopImg(obj){
		yoyoEffectImg(obj,{top:getNumValue(obj.style.top)+1.066},{top:getNumValue(obj.style.top)-1.066});
	}
	function addNewFdiv(fnum){
		var fnobj=getIdClass(getId(pamc),"F2");
		var html=fnobj.innerHTML;
		var contentdiv=getId(pamc);
		var divf=document.createElement('div');
		divf.className="F"+fnum;
		divf.style.left="0%";
		divf.style.top=(fnum-1)*100+"%";
		divf.style.width="100%";
		divf.style.height="100%";
		divf.style.display="none";
		divf.innerHTML=html;
		contentdiv.appendChild(divf);
	}
	///////////////////////////////////////////////////////
	var car1list=new Array();
	car1list[0]=["car1_d",{left:"80.813%",top:"33.153%"},{left:"20",top:"20"},100];
	car1list[1]=["car1_l",{left:"65.477%",top:"45.819%"},{left:"19",top:"19"},100];
	car1list[2]=["car1_d",{left:"56.375%",top:"38.542%"},{left:"22",top:"22"},100];
	car1list[3]=["car1_u",{left:"43.898%",top:"50.653%"},{left:"23",top:"23"},100];
	car1list[4]=["car1_r",{left:"56.82%",top:"37.708%"},{left:"19",top:"19"},100];
	car1list[5]=["car1_u",{left:"66.367%",top:"46.431%"},{left:"18",top:"18"},100];
	car1list[6]=[null,{left:"80.539%",top:"33.694%"},null,null];
	var car2list=new Array();
	car2list[0]=["car2_r",{left:"57.805%",top:"73.431%"},{left:"21",top:"21"},100];
	car2list[1]=["car2_l",{left:"68.336%",top:"83.625%"},{left:"20",top:"20"},100];
	car2list[2]=["car2_u",{left:"57.047%",top:"72.611%"},{left:"32",top:"32"},100];
	car2list[3]=["car2_l",{left:"74.984%",top:"55.361%"},{left:"16",top:"16"},100];
	car2list[4]=["car2_r",{left:"71.719%",top:"51.431%"},{left:"16",top:"16"},100];
	car2list[5]=["car2_d",{left:"74.633%",top:"55.708%"},{left:"28",top:"28"},100];
	car2list[6]=[null,{left:"57.047%",top:"72.611%"},null,null];
	var car3list=new Array();
	car3list[0]=["car3_l",{left:"46.219%",top:"80.528%"},{left:"28",top:"28"},100];
	car3list[1]=["car3_d",{left:"18.203%",top:"55.375%"},{left:"12",top:"12"},100];
	car3list[2]=["car3_u",{left:"9.203%",top:"63.389%"},{left:"14",top:"14"},100];
	car3list[3]=["car3_r",{left:"19.078%",top:"55.306%"},{left:"28",top:"28"},100];
	car3list[4]=[null,{left:"45.648%",top:"79.056%"},null,null];

	var ren1list=new Array();
	ren1list[0]=["ren1_l",{left:"61.672%",top:"18.653%"},{left:"40",top:"40"},100];
	ren1list[1]=["ren1_l",{left:"57.305%",top:"22.014%"},{left:"48",top:"48"},100];
	ren1list[2]=["ren1_r",{left:"50.852%",top:"16.806%"},{left:"43",top:"43"},100];
	ren1list[3]=["ren1_r",{left:"57.148%",top:"22.111%"},{left:"38",top:"38"},1000];
	ren1list[4]=[null,{left:"61.438%",top:"18.806%"},null,null];
	var ren2list=new Array();
	ren2list[0]=["ren2_r",{left:"60.063%",top:"45.889%"},{left:"40",top:"40"},100];
	ren2list[1]=["ren2_r",{left:"64.414%",top:"49.208%"},{left:"48",top:"48"},100];
	ren2list[2]=["ren2_l",{left:"71.156%",top:"44.014%"},{left:"43",top:"43"},100];
	ren2list[3]=["ren2_l",{left:"64.57%",top:"49.181%"},{left:"38",top:"38"},800];
	ren2list[4]=[null,{left:"60.281%",top:"45.958%"},null,null];
	var ren3list=new Array();
	ren3list[0]=["ren3_l",{left:"41.555%",top:"52.986%"},{left:"40",top:"40"},100];
	ren3list[1]=["ren3_l",{left:"37.18%",top:"56.278%"},{left:"48",top:"48"},100];
	ren3list[2]=["ren3_r",{left:"30.414%",top:"51.083%"},{left:"43",top:"43"},100];
	ren3list[3]=["ren3_r",{left:"37%",top:"56.264%"},{left:"38",top:"38"},1000];
	ren3list[4]=[null,{left:"41.266%",top:"53.042%"},null,null];
	var ren4list=new Array();
	ren4list[0]=["ren4_l",{left:"33.664%",top:"78.597%"},{left:"40",top:"40"},100];
	ren4list[1]=["ren4_l",{left:"29.305%",top:"81.917%"},{left:"48",top:"48"},100];
	ren4list[2]=["ren4_r",{left:"22.531%",top:"76.681%"},{left:"43",top:"43"},100];
	ren4list[3]=["ren4_r",{left:"29.094%",top:"81.847%"},{left:"38",top:"38"},800];
	ren4list[4]=[null,{left:"33.391%",top:"78.694%"},null,null];
	var ren5list=new Array();
	ren5list[0]=["ren5_l",{left:"94.258%",top:"26.111%"},{left:"40",top:"40"},100];
	ren5list[1]=["ren5_l",{left:"89.953%",top:"29.417%"},{left:"48",top:"48"},100];
	ren5list[2]=["ren5_r",{left:"83.18%",top:"24.292%"},{left:"43",top:"43"},100];
	ren5list[3]=["ren5_r",{left:"89.781%",top:"29.472%"},{left:"38",top:"38"},1000];
	ren5list[4]=[null,{left:"94.063%",top:"26.181%"},null,null];
	var ren6list=new Array();
	ren6list[0]=["ren6_r",{left:"3.422%",top:"61.986%"},{left:"40",top:"40"},100];
	ren6list[1]=["ren6_r",{left:"10.008%",top:"67.167%"},{left:"48",top:"48"},100];
	ren6list[2]=["ren6_l",{left:"14.273%",top:"64.778%"},{left:"43",top:"43"},100];
	ren6list[3]=["ren6_l",{left:"10.188%",top:"67.167%"},{left:"38",top:"38"},800];
	ren6list[4]=[null,{left:"3.641%",top:"62.125%"},null,null];
	var ren7list=new Array();
	ren7list[0]=["ren7_l",{left:"92.742%",top:"76.194%"},{left:"40",top:"40"},100];
	ren7list[1]=["ren7_l",{left:"88.352%",top:"79.417%"},{left:"48",top:"48"},100];
	ren7list[2]=["ren7_r",{left:"81.648%",top:"74.278%"},{left:"43",top:"43"},100];
	ren7list[3]=["ren7_r",{left:"88.219%",top:"79.5%"},{left:"38",top:"38"},800];
	ren7list[4]=[null,{left:"92.5%",top:"76.222%"},null,null];

	///////////////////////////////////////////////////////
	var f2_car1list=new Array();
	f2_car1list[0]=["car1_d",{left:"80.813%",top:"32.667%"},{left:"20",top:"20"},100];
	f2_car1list[1]=["car1_l",{left:"65.477%",top:"45.333%"},{left:"19",top:"19"},100];
	f2_car1list[2]=["car1_d",{left:"56.375%",top:"38.056%"},{left:"22",top:"22"},100];
	f2_car1list[3]=["car1_u",{left:"43.898%",top:"50.167%"},{left:"23",top:"23"},100];
	f2_car1list[4]=["car1_r",{left:"56.82%",top:"37.222%"},{left:"19",top:"19"},100];
	f2_car1list[5]=["car1_u",{left:"66.367%",top:"45.944%"},{left:"18",top:"18"},100];
	f2_car1list[6]=[null,{left:"80.539%",top:"33.208%"},null,null];
	var f2_car2list=new Array();
	f2_car2list[0]=["car2_r",{left:"57.805%",top:"72.944%"},{left:"21",top:"21"},100];
	f2_car2list[1]=["car2_l",{left:"68.336%",top:"83.139%"},{left:"20",top:"20"},100];
	f2_car2list[2]=["car2_u",{left:"57.047%",top:"72.125%"},{left:"32",top:"32"},100];
	f2_car2list[3]=["car2_l",{left:"74.984%",top:"54.875%"},{left:"16",top:"16"},100];
	f2_car2list[4]=["car2_r",{left:"71.719%",top:"50.944%"},{left:"16",top:"16"},100];
	f2_car2list[5]=["car2_d",{left:"74.633%",top:"55.222%"},{left:"28",top:"28"},100];
	f2_car2list[6]=[null,{left:"57.047%",top:"72.125%"},null,null];
	var f2_car3list=new Array();
	f2_car3list[0]=["car3_l",{left:"46.219%",top:"80.042%"},{left:"28",top:"28"},100];
	f2_car3list[1]=["car3_d",{left:"18.203%",top:"54.889%"},{left:"12",top:"12"},100];
	f2_car3list[2]=["car3_u",{left:"9.203%",top:"62.903%"},{left:"14",top:"14"},100];
	f2_car3list[3]=["car3_r",{left:"19.078%",top:"54.819%"},{left:"28",top:"28"},100];
	f2_car3list[4]=[null,{left:"45.648%",top:"78.569%"},null,null];
	var f2_car4list=new Array();
	f2_car4list[0]=["car4_u",{left:"38.273%",top:"6.139%"},{left:"21",top:"21"},100];
	f2_car4list[1]=["car4_l",{left:"57.227%",top:"-9.861%"},{left:"11",top:"11"},100];
	f2_car4list[2]=["car4_r",{left:"52.844%",top:"-13.861%"},{left:"24",top:"24"},100];
	f2_car4list[3]=["car4_d",{left:"74.523%",top:"6.722%"},{left:"14",top:"14"},100];
	f2_car4list[4]=["car4_l",{left:"65.57%",top:"15.069%"},{left:"20",top:"20"},100];
	f2_car4list[5]=["car4_d",{left:"47.578%",top:"-0.139%"},{left:"12",top:"12"},100];
	f2_car4list[6]=[null,{left:"38.469%",top:"5.111%"},null,null];
	var f2_ren1list=new Array();
	f2_ren1list[0]=["ren1_l",{left:"61.672%",top:"22.167%"},{left:"40",top:"40"},100];
	f2_ren1list[1]=["ren1_l",{left:"57.305%",top:"25.528%"},{left:"48",top:"48"},100];
	f2_ren1list[2]=["ren1_r",{left:"50.852%",top:"20.319%"},{left:"43",top:"43"},100];
	f2_ren1list[3]=["ren1_r",{left:"57.148%",top:"25.625%"},{left:"38",top:"38"},1000];
	f2_ren1list[4]=[null,{left:"61.438%",top:"22.319%"},null,null];
	var f2_ren2list=new Array();
	f2_ren2list[0]=["ren2_r",{left:"60.063%",top:"49.403%"},{left:"40",top:"40"},100];
	f2_ren2list[1]=["ren2_r",{left:"64.414%",top:"52.722%"},{left:"48",top:"48"},100];
	f2_ren2list[2]=["ren2_l",{left:"71.156%",top:"47.528%"},{left:"43",top:"43"},100];
	f2_ren2list[3]=["ren2_l",{left:"64.57%",top:"52.694%"},{left:"38",top:"38"},800];
	f2_ren2list[4]=[null,{left:"60.281%",top:"49.472%"},null,null];
	var f2_ren3list=new Array();
	f2_ren3list[0]=["ren3_l",{left:"41.555%",top:"56.5%"},{left:"40",top:"40"},100];
	f2_ren3list[1]=["ren3_l",{left:"37.18%",top:"59.792%"},{left:"48",top:"48"},100];
	f2_ren3list[2]=["ren3_r",{left:"30.414%",top:"54.597%"},{left:"43",top:"43"},100];
	f2_ren3list[3]=["ren3_r",{left:"37%",top:"59.778%"},{left:"38",top:"38"},1000];
	f2_ren3list[4]=[null,{left:"41.266%",top:"56.556%"},null,null];
	var f2_ren4list=new Array();
	f2_ren4list[0]=["ren4_l",{left:"33.664%",top:"82.111%"},{left:"40",top:"40"},100];
	f2_ren4list[1]=["ren4_l",{left:"29.305%",top:"85.431%"},{left:"48",top:"48"},100];
	f2_ren4list[2]=["ren4_r",{left:"22.531%",top:"80.194%"},{left:"43",top:"43"},100];
	f2_ren4list[3]=["ren4_r",{left:"29.094%",top:"85.361%"},{left:"38",top:"38"},800];
	f2_ren4list[4]=[null,{left:"33.391%",top:"82.208%"},null,null];
	var f2_ren5list=new Array();
	f2_ren5list[0]=["ren5_l",{left:"94.258%",top:"29.625%"},{left:"40",top:"40"},100];
	f2_ren5list[1]=["ren5_l",{left:"89.953%",top:"32.931%"},{left:"48",top:"48"},100];
	f2_ren5list[2]=["ren5_r",{left:"83.18%",top:"27.806%"},{left:"43",top:"43"},100];
	f2_ren5list[3]=["ren5_r",{left:"89.781%",top:"32.986%"},{left:"38",top:"38"},1000];
	f2_ren5list[4]=[null,{left:"94.063%",top:"29.694%"},null,null];
	var f2_ren6list=new Array();
	f2_ren6list[0]=["ren6_r",{left:"3.422%",top:"65.5%"},{left:"40",top:"40"},100];
	f2_ren6list[1]=["ren6_r",{left:"10.008%",top:"70.681%"},{left:"48",top:"48"},100];
	f2_ren6list[2]=["ren6_l",{left:"14.273%",top:"68.292%"},{left:"43",top:"43"},100];
	f2_ren6list[3]=["ren6_l",{left:"10.188%",top:"70.681%"},{left:"38",top:"38"},800];
	f2_ren6list[4]=[null,{left:"3.641%",top:"65.639%"},null,null];
	var f2_ren7list=new Array();
	f2_ren7list[0]=["ren7_l",{left:"92.742%",top:"79.708%"},{left:"40",top:"40"},100];
	f2_ren7list[1]=["ren7_l",{left:"88.352%",top:"82.931%"},{left:"48",top:"48"},100];
	f2_ren7list[2]=["ren7_r",{left:"81.648%",top:"77.792%"},{left:"43",top:"43"},100];
	f2_ren7list[3]=["ren7_r",{left:"88.219%",top:"83.014%"},{left:"38",top:"38"},800];
	f2_ren7list[4]=[null,{left:"92.5%",top:"79.736%"},null,null];

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
		var pw=1280;
		var ph=720;
		var sw=pw/dw;
		var sh=ph/dh;
		var sn=sh/sw;
		//document.title=sn;//alert(sw+":"+sh);
		var tmpp=sn*100;
		
		var f=0;
		do{
			f++;
			var divf=getIdClass(getId(pamc),"F"+f);
			if(divf){
				divf.style.height=Number(parseFloat(   tmpp   ).toFixed(3))+"%";
				divf.style.top=Number(parseFloat(   (f-1)*tmpp   ).toFixed(3))+"%";
			}
		}while(divf)

		var tdarr=getId(pamc).getElementsByTagName("td");
		for(var t=0;t<tdarr.length;t++){
			tdarr[t].style.fontSize=Number(parseFloat(   dw/pw*1   ).toFixed(3))+"em";  
		}
	}
	$(function(){
		orderlist_init();
	});
</script>
</head>
<body>
	<div id="mycontent" style="left:0%;top:0%;width:100%;height:100%;position:absolute;overflow-x:hidden;">
		<div class="F1" style="left:0%;top:0%;width:100%;height:100%;overflow:hidden;display:none;">
			<img class="p1" style="left:0%;top:0%;width:100%;height:100%;" src="${ctx}/static/images/profession/p1.jpg" alt="" />
			<img class="ww1" style="left:81.664%;top:2.597%;width:3.281%;height:2.917%;" src="${ctx}/static/images/profession/waterwaves.gif" alt="" />
			<img class="ww2" style="left:0.406%;top:13.514%;width:3.281%;height:2.917%;" src="${ctx}/static/images/profession/waterwaves.gif" alt="" />
			<img class="ww3" style="left:37.898%;top:65.292%;width:3.281%;height:2.917%;" src="${ctx}/static/images/profession/waterwaves.gif" alt="" />
			<img class="ww4" style="left:66.07%;top:56.653%;width:3.281%;height:2.917%;" src="${ctx}/static/images/profession/waterwaves.gif" alt="" />
			<img class="ww5" style="left:34.578%;top:34.889%;width:3.281%;height:2.917%;" src="${ctx}/static/images/profession/waterwaves.gif" alt="" />
			<img class="ww6" style="left:37.82%;top:31.722%;width:3.281%;height:2.917%;" src="${ctx}/static/images/profession/waterwaves.gif" alt="" />
			<img class="ww7" style="left:39.398%;top:35.917%;width:3.281%;height:2.917%;" src="${ctx}/static/images/profession/waterwaves.gif" alt="" />
			<img class="ww8" style="left:71.992%;top:17.708%;width:3.281%;height:2.917%;" src="${ctx}/static/images/profession/waterwaves.gif" alt="" />
			<img class="ww9" style="left:75.227%;top:14.556%;width:3.281%;height:2.917%;" src="${ctx}/static/images/profession/waterwaves.gif" alt="" />
			<img class="ww10" style="left:76.805%;top:18.736%;width:3.281%;height:2.917%;" src="${ctx}/static/images/profession/waterwaves.gif" alt="" />
			
			<img class="car1_d" style="left:80.813%;top:29.153%;width:4.063%;height:4.861%;" src="${ctx}/static/images/profession/car1_d.png" alt="" />
			<img class="car1_l" style="left:80.813%;top:29.153%;width:4.063%;height:4.861%;" src="${ctx}/static/images/profession/car1_l.png" alt="" />
			<img class="car1_u" style="left:80.813%;top:29.153%;width:4.063%;height:4.861%;" src="${ctx}/static/images/profession/car1_u.png" alt="" />
			<img class="car1_r" style="left:80.813%;top:29.153%;width:4.063%;height:4.861%;" src="${ctx}/static/images/profession/car1_r.png" alt="" />
			<img class="car3_l" style="left:46.219%;top:76.528%;width:4.063%;height:4.861%;" src="${ctx}/static/images/profession/car3_l.png" alt="" />
			<img class="car3_d" style="left:46.219%;top:76.528%;width:4.063%;height:4.861%;" src="${ctx}/static/images/profession/car3_d.png" alt="" />
			<img class="car3_u" style="left:46.219%;top:76.528%;width:4.063%;height:4.861%;" src="${ctx}/static/images/profession/car3_u.png" alt="" />
			<img class="car3_r" style="left:46.219%;top:76.528%;width:4.063%;height:4.861%;" src="${ctx}/static/images/profession/car3_r.png" alt="" />
			<img class="ren1_l" style="left:61.672%;top:18.653%;width:2.5%;height:5.556%;" src="${ctx}/static/images/profession/ren1_l.gif" alt="" />
			<img class="ren1_r" style="left:61.672%;top:18.653%;width:2.5%;height:5.556%;" src="${ctx}/static/images/profession/ren1_r.gif" alt="" />
			<img class="ren4_l" style="left:33.664%;top:78.597%;width:2.5%;height:5.556%;" src="${ctx}/static/images/profession/ren1_l.gif" alt="" />
			<img class="ren4_r" style="left:33.664%;top:78.597%;width:2.5%;height:5.556%;" src="${ctx}/static/images/profession/ren1_r.gif" alt="" />
			<img class="ren5_l" style="left:94.258%;top:26.111%;width:2.5%;height:5.556%;" src="${ctx}/static/images/profession/ren3_l.gif" alt="" />
			<img class="ren5_r" style="left:94.258%;top:26.111%;width:2.5%;height:5.556%;" src="${ctx}/static/images/profession/ren3_r.gif" alt="" />
			<img class="ren7_l" style="left:92.742%;top:76.194%;width:2.5%;height:5.556%;" src="${ctx}/static/images/profession/ren2_l.gif" alt="" />
			<img class="ren7_r" style="left:92.742%;top:76.194%;width:2.5%;height:5.556%;" src="${ctx}/static/images/profession/ren2_r.gif" alt="" />
			
			<div class="house1" style="left:1.758%;top:37.417%;width:19.063%;height:33.889%;">
				<img class="house_bg" style="left:0%;top:0%;width:100%;height:100%;" src="${ctx}/static/images/profession/house5.png" alt="" />
				<img class="house_tbg" style="left:1.23%;top:6.557%;width:95.492%;height:38.115%;" src="${ctx}/static/images/profession/housetitlebg.png" alt="" />
				<div class="house_t" style="left:2.459%;top:7.787%;width:93.033%;height:28.689%;overflow:hidden;">
					<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0"><tr align="center"><td align="center" valign="middle" class="totd"></td></tr></table>
				</div>
			</div>
			<div class="house2" style="left:29.93%;top:28.861%;width:19.063%;height:33.889%;">
				<img class="house_bg" style="left:0%;top:0%;width:100%;height:100%;" src="${ctx}/static/images/profession/house2.png" alt="" />
				<img class="house_tbg" style="left:1.23%;top:6.557%;width:95.492%;height:38.115%;" src="${ctx}/static/images/profession/housetitlebg.png" alt="" />
				<div class="house_t" style="left:2.459%;top:7.787%;width:93.033%;height:28.689%;overflow:hidden;">
					<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0"><tr align="center"><td align="center" valign="middle" class="totd"></td></tr></table>
				</div>
			</div>
			<div class="house3" style="left:58.117%;top:20.028%;width:19.063%;height:33.889%;">
				<img class="house_bg" style="left:0%;top:0%;width:100%;height:100%;" src="${ctx}/static/images/profession/house5.png" alt="" />
				<img class="house_tbg" style="left:1.23%;top:6.557%;width:95.492%;height:38.115%;" src="${ctx}/static/images/profession/housetitlebg.png" alt="" />
				<div class="house_t" style="left:2.459%;top:7.787%;width:93.033%;height:28.689%;overflow:hidden;">
					<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0"><tr align="center"><td align="center" valign="middle" class="totd"></td></tr></table>
				</div>
			</div>
			<div class="house4" style="left:76.844%;top:37.472%;width:19.063%;height:33.889%;">
				<img class="house_bg" style="left:0%;top:0%;width:100%;height:100%;" src="${ctx}/static/images/profession/house4.png" alt="" />
				<img class="house_tbg" style="left:1.23%;top:6.557%;width:95.492%;height:38.115%;" src="${ctx}/static/images/profession/housetitlebg.png" alt="" />
				<div class="house_t" style="left:2.459%;top:7.787%;width:93.033%;height:28.689%;overflow:hidden;">
					<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0"><tr align="center"><td align="center" valign="middle" class="totd"></td></tr></table>
				</div>
			</div>
			<img class="car2_r" style="left:57.805%;top:69.431%;width:4.063%;height:4.861%;" src="${ctx}/static/images/profession/car2_r.png" alt="" />
			<img class="car2_l" style="left:57.805%;top:69.431%;width:4.063%;height:4.861%;" src="${ctx}/static/images/profession/car2_l.png" alt="" />
			<img class="car2_u" style="left:57.805%;top:69.431%;width:4.063%;height:4.861%;" src="${ctx}/static/images/profession/car2_u.png" alt="" />
			<img class="car2_d" style="left:57.805%;top:69.431%;width:4.063%;height:4.861%;" src="${ctx}/static/images/profession/car2_d.png" alt="" />
			<img class="ren2_r" style="left:60.063%;top:45.889%;width:2.5%;height:5.556%;" src="${ctx}/static/images/profession/ren2_r.gif" alt="" />
			<img class="ren2_l" style="left:60.063%;top:45.889%;width:2.5%;height:5.556%;" src="${ctx}/static/images/profession/ren2_l.gif" alt="" />
			<img class="ren3_l" style="left:41.555%;top:52.986%;width:2.5%;height:5.556%;" src="${ctx}/static/images/profession/ren3_l.gif" alt="" />
			<img class="ren3_r" style="left:41.555%;top:52.986%;width:2.5%;height:5.556%;" src="${ctx}/static/images/profession/ren3_r.gif" alt="" />
			<img class="ren6_r" style="left:3.422%;top:61.986%;width:2.5%;height:5.556%;" src="${ctx}/static/images/profession/ren3_r.gif" alt="" />
			<img class="ren6_l" style="left:3.422%;top:61.986%;width:2.5%;height:5.556%;" src="${ctx}/static/images/profession/ren3_l.gif" alt="" />
			<div class="house5" style="left:11.219%;top:63.319%;width:19.063%;height:33.889%;">
				<img class="house_bg" style="left:0%;top:0%;width:100%;height:100%;" src="${ctx}/static/images/profession/house4.png" alt="" />
				<img class="house_tbg" style="left:1.23%;top:6.557%;width:95.492%;height:38.115%;" src="${ctx}/static/images/profession/housetitlebg.png" alt="" />
				<div class="house_t" style="left:2.459%;top:7.787%;width:93.033%;height:28.689%;overflow:hidden;">
					<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0"><tr align="center"><td align="center" valign="middle" class="totd"></td></tr></table>
				</div>
			</div>
			<div class="house6" style="left:39.211%;top:54.639%;width:19.063%;height:33.889%;">
				<img class="house_bg" style="left:0%;top:0%;width:100%;height:100%;" src="${ctx}/static/images/profession/house3.png" alt="" />
				<img class="house_tbg" style="left:1.23%;top:6.557%;width:95.492%;height:38.115%;" src="${ctx}/static/images/profession/housetitlebg.png" alt="" />
				<div class="house_t" style="left:2.459%;top:7.787%;width:93.033%;height:28.689%;overflow:hidden;">
					<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0"><tr align="center"><td align="center" valign="middle" class="totd"></td></tr></table>
				</div>
			</div>
			<div class="house7" style="left:67.5%;top:63.194%;width:19.063%;height:33.889%;">
				<img class="house_bg" style="left:0%;top:0%;width:100%;height:100%;" src="${ctx}/static/images/profession/house1.png" alt="" />
				<img class="house_tbg" style="left:1.23%;top:6.557%;width:95.492%;height:38.115%;" src="${ctx}/static/images/profession/housetitlebg.png" alt="" />
				<div class="house_t" style="left:2.459%;top:7.787%;width:93.033%;height:28.689%;overflow:hidden;">
					<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0"><tr align="center"><td align="center" valign="middle" class="totd"></td></tr></table>
				</div>
			</div>
		</div>

		<div class="F2" style="left:0%;top:100%;width:100%;height:100%;display:none;">
			<img class="p1" style="left:0%;top:0%;width:100%;height:100%;" src="${ctx}/static/images/profession/p2.jpg" alt="" />
			<img class="ww1" style="left:15.875%;top:3.736%;width:3.281%;height:2.917%;" src="${ctx}/static/images/profession/waterwaves.gif" alt="" />
			<img class="ww2" style="left:19.117%;top:0.569%;width:3.281%;height:2.917%;" src="${ctx}/static/images/profession/waterwaves.gif" alt="" />
			<img class="ww3" style="left:20.695%;top:4.764%;width:3.281%;height:2.917%;" src="${ctx}/static/images/profession/waterwaves.gif" alt="" />
			<img class="ww4" style="left:34.789%;top:38.319%;width:3.281%;height:2.917%;" src="${ctx}/static/images/profession/waterwaves.gif" alt="" />
			<img class="ww5" style="left:38.031%;top:35.167%;width:3.281%;height:2.917%;" src="${ctx}/static/images/profession/waterwaves.gif" alt="" />
			<img class="ww6" style="left:39.609%;top:39.361%;width:3.281%;height:2.917%;" src="${ctx}/static/images/profession/waterwaves.gif" alt="" />
			<img class="ww7" style="left:72.109%;top:21.236%;width:3.281%;height:2.917%;" src="${ctx}/static/images/profession/waterwaves.gif" alt="" />
			<img class="ww8" style="left:75.352%;top:18.069%;width:3.281%;height:2.917%;" src="${ctx}/static/images/profession/waterwaves.gif" alt="" />
			<img class="ww9" style="left:76.937%;top:22.264%;width:3.281%;height:2.917%;" src="${ctx}/static/images/profession/waterwaves.gif" alt="" />
			<img class="ww10" style="left:38.133%;top:-0.333%;width:3.281%;height:2.917%;" src="${ctx}/static/images/profession/waterwaves.gif" alt="" />
			<img class="ww11" style="left:38.055%;top:68.75%;width:3.281%;height:2.917%;" src="${ctx}/static/images/profession/waterwaves.gif" alt="" />
			<img class="ww12" style="left:66.305%;top:60.181%;width:3.281%;height:2.917%;" src="${ctx}/static/images/profession/waterwaves.gif" alt="" />
			<div class="house1" style="left:29.813%;top:-20.75%;width:19.063%;height:33.889%;">
				<img class="house_bg" style="left:0%;top:0%;width:100%;height:100%;" src="${ctx}/static/images/profession/house2.png" alt="" />
				<img class="house_tbg" style="left:1.23%;top:6.557%;width:95.492%;height:38.115%;" src="${ctx}/static/images/profession/housetitlebg.png" alt="" />
				<div class="house_t" style="left:2.459%;top:7.787%;width:93.033%;height:28.689%;overflow:hidden;">
					<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0"><tr align="center"><td align="center" valign="middle" class="totd"></td></tr></table>
				</div>
			</div>
			<img class="car4_u" style="left:38.273%;top:2.139%;width:4.063%;height:4.861%;" src="${ctx}/static/images/profession/car2_u.png" alt="" />
			<img class="car4_l" style="left:38.273%;top:2.139%;width:4.063%;height:4.861%;" src="${ctx}/static/images/profession/car2_l.png" alt="" />
			<img class="car4_r" style="left:38.273%;top:2.139%;width:4.063%;height:4.861%;" src="${ctx}/static/images/profession/car2_r.png" alt="" />
			<img class="car4_d" style="left:38.273%;top:2.139%;width:4.063%;height:4.861%;" src="${ctx}/static/images/profession/car2_d.png" alt="" />
			<div class="house2" style="left:58.422%;top:-12.569%;width:19.063%;height:33.889%;">
				<img class="house_bg" style="left:0%;top:0%;width:100%;height:100%;" src="${ctx}/static/images/profession/house5.png" alt="" />
				<img class="house_tbg" style="left:1.23%;top:6.557%;width:95.492%;height:38.115%;" src="${ctx}/static/images/profession/housetitlebg.png" alt="" />
				<div class="house_t" style="left:2.459%;top:7.787%;width:93.033%;height:28.689%;overflow:hidden;">
					<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0"><tr align="center"><td align="center" valign="middle" class="totd"></td></tr></table>
				</div>
			</div>
			<div class="house5" style="left:79.68%;top:6.222%;width:19.063%;height:33.889%;">
				<img class="house_bg" style="left:0%;top:0%;width:100%;height:100%;" src="${ctx}/static/images/profession/house3.png" alt="" />
				<img class="house_tbg" style="left:1.23%;top:6.557%;width:95.492%;height:38.115%;" src="${ctx}/static/images/profession/housetitlebg.png" alt="" />
				<div class="house_t" style="left:2.459%;top:7.787%;width:93.033%;height:28.689%;overflow:hidden;">
					<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0"><tr align="center"><td align="center" valign="middle" class="totd"></td></tr></table>
				</div>
			</div>
			<img class="car1_d" style="left:80.813%;top:32.667%;width:4.063%;height:4.861%;" src="${ctx}/static/images/profession/car1_d.png" alt="" />
			<img class="car1_l" style="left:80.813%;top:32.667%;width:4.063%;height:4.861%;" src="${ctx}/static/images/profession/car1_l.png" alt="" />
			<img class="car1_u" style="left:80.813%;top:32.667%;width:4.063%;height:4.861%;" src="${ctx}/static/images/profession/car1_u.png" alt="" />
			<img class="car1_r" style="left:80.813%;top:32.667%;width:4.063%;height:4.861%;" src="${ctx}/static/images/profession/car1_r.png" alt="" />
			<img class="car3_l" style="left:46.219%;top:80.042%;width:4.063%;height:4.861%;" src="${ctx}/static/images/profession/car3_l.png" alt="" />
			<img class="car3_d" style="left:46.219%;top:80.042%;width:4.063%;height:4.861%;" src="${ctx}/static/images/profession/car3_d.png" alt="" />
			<img class="car3_u" style="left:46.219%;top:80.042%;width:4.063%;height:4.861%;" src="${ctx}/static/images/profession/car3_u.png" alt="" />
			<img class="car3_r" style="left:46.219%;top:80.042%;width:4.063%;height:4.861%;" src="${ctx}/static/images/profession/car3_r.png" alt="" />
			<img class="ren1_l" style="left:61.672%;top:22.167%;width:2.5%;height:5.556%;" src="${ctx}/static/images/profession/ren1_l.gif" alt="" />
			<img class="ren1_r" style="left:61.672%;top:22.167%;width:2.5%;height:5.556%;" src="${ctx}/static/images/profession/ren1_r.gif" alt="" />
			<img class="ren4_l" style="left:33.664%;top:82.111%;width:2.5%;height:5.556%;" src="${ctx}/static/images/profession/ren1_l.gif" alt="" />
			<img class="ren4_r" style="left:33.664%;top:82.111%;width:2.5%;height:5.556%;" src="${ctx}/static/images/profession/ren1_r.gif" alt="" />
			<img class="ren5_l" style="left:94.258%;top:29.625%;width:2.5%;height:5.556%;" src="${ctx}/static/images/profession/ren3_l.gif" alt="" />
			<img class="ren5_r" style="left:94.258%;top:29.625%;width:2.5%;height:5.556%;" src="${ctx}/static/images/profession/ren3_r.gif" alt="" />
			<img class="ren7_l" style="left:92.742%;top:79.708%;width:2.5%;height:5.556%;" src="${ctx}/static/images/profession/ren2_l.gif" alt="" />
			<img class="ren7_r" style="left:92.742%;top:79.708%;width:2.5%;height:5.556%;" src="${ctx}/static/images/profession/ren2_r.gif" alt="" />
			<div class="house3" style="left:1.789%;top:6.347%;width:19.063%;height:33.889%;">
				<img class="house_bg" style="left:0%;top:0%;width:100%;height:100%;" src="${ctx}/static/images/profession/house3.png" alt="" />
				<img class="house_tbg" style="left:1.23%;top:6.557%;width:95.492%;height:38.115%;" src="${ctx}/static/images/profession/housetitlebg.png" alt="" />
				<div class="house_t" style="left:2.459%;top:7.787%;width:93.033%;height:28.689%;overflow:hidden;">
					<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0"><tr align="center"><td align="center" valign="middle" class="totd"></td></tr></table>
				</div>
			</div>
			<div class="house4" style="left:39.328%;top:6.347%;width:19.063%;height:33.889%;">
				<img class="house_bg" style="left:0%;top:0%;width:100%;height:100%;" src="${ctx}/static/images/profession/house1.png" alt="" />
				<img class="house_tbg" style="left:1.23%;top:6.557%;width:95.492%;height:38.115%;" src="${ctx}/static/images/profession/housetitlebg.png" alt="" />
				<div class="house_t" style="left:2.459%;top:7.787%;width:93.033%;height:28.689%;overflow:hidden;">
					<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0"><tr align="center"><td align="center" valign="middle" class="totd"></td></tr></table>
				</div>
			</div>
			<div class="house6" style="left:1.758%;top:40.931%;width:19.063%;height:33.889%;">
				<img class="house_bg" style="left:0%;top:0%;width:100%;height:100%;" src="${ctx}/static/images/profession/house5.png" alt="" />
				<img class="house_tbg" style="left:1.23%;top:6.557%;width:95.492%;height:38.115%;" src="${ctx}/static/images/profession/housetitlebg.png" alt="" />
				<div class="house_t" style="left:2.459%;top:7.787%;width:93.033%;height:28.689%;overflow:hidden;">
					<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0"><tr align="center"><td align="center" valign="middle" class="totd"></td></tr></table>
				</div>
			</div>
			<div class="house7" style="left:29.93%;top:32.375%;width:19.063%;height:33.889%;">
				<img class="house_bg" style="left:0%;top:0%;width:100%;height:100%;" src="${ctx}/static/images/profession/house2.png" alt="" />
				<img class="house_tbg" style="left:1.23%;top:6.557%;width:95.492%;height:38.115%;" src="${ctx}/static/images/profession/housetitlebg.png" alt="" />
				<div class="house_t" style="left:2.459%;top:7.787%;width:93.033%;height:28.689%;overflow:hidden;">
					<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0"><tr align="center"><td align="center" valign="middle" class="totd"></td></tr></table>
				</div>
			</div>
			<div class="house8" style="left:58.117%;top:23.542%;width:19.063%;height:33.889%;">
				<img class="house_bg" style="left:0%;top:0%;width:100%;height:100%;" src="${ctx}/static/images/profession/house5.png" alt="" />
				<img class="house_tbg" style="left:1.23%;top:6.557%;width:95.492%;height:38.115%;" src="${ctx}/static/images/profession/housetitlebg.png" alt="" />
				<div class="house_t" style="left:2.459%;top:7.787%;width:93.033%;height:28.689%;overflow:hidden;">
					<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0"><tr align="center"><td align="center" valign="middle" class="totd"></td></tr></table>
				</div>
			</div>
			<div class="house9" style="left:76.844%;top:40.986%;width:19.063%;height:33.889%;">
				<img class="house_bg" style="left:0%;top:0%;width:100%;height:100%;" src="${ctx}/static/images/profession/house4.png" alt="" />
				<img class="house_tbg" style="left:1.23%;top:6.557%;width:95.492%;height:38.115%;" src="${ctx}/static/images/profession/housetitlebg.png" alt="" />
				<div class="house_t" style="left:2.459%;top:7.787%;width:93.033%;height:28.689%;overflow:hidden;">
					<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0"><tr align="center"><td align="center" valign="middle" class="totd"></td></tr></table>
				</div>
			</div>
			<img class="car2_r" style="left:57.805%;top:72.944%;width:4.063%;height:4.861%;" src="${ctx}/static/images/profession/car2_r.png" alt="" />
			<img class="car2_l" style="left:57.805%;top:72.944%;width:4.063%;height:4.861%;" src="${ctx}/static/images/profession/car2_l.png" alt="" />
			<img class="car2_u" style="left:57.805%;top:72.944%;width:4.063%;height:4.861%;" src="${ctx}/static/images/profession/car2_u.png" alt="" />
			<img class="car2_d" style="left:57.805%;top:72.944%;width:4.063%;height:4.861%;" src="${ctx}/static/images/profession/car2_d.png" alt="" />
			<img class="ren2_r" style="left:60.063%;top:49.403%;width:2.5%;height:5.556%;" src="${ctx}/static/images/profession/ren2_r.gif" alt="" />
			<img class="ren2_l" style="left:60.063%;top:49.403%;width:2.5%;height:5.556%;" src="${ctx}/static/images/profession/ren2_l.gif" alt="" />
			<img class="ren3_l" style="left:41.555%;top:56.5%;width:2.5%;height:5.556%;" src="${ctx}/static/images/profession/ren3_l.gif" alt="" />
			<img class="ren3_r" style="left:41.555%;top:56.5%;width:2.5%;height:5.556%;" src="${ctx}/static/images/profession/ren3_r.gif" alt="" />
			<img class="ren6_r" style="left:3.422%;top:65.5%;width:2.5%;height:5.556%;" src="${ctx}/static/images/profession/ren3_r.gif" alt="" />
			<img class="ren6_l" style="left:3.422%;top:65.5%;width:2.5%;height:5.556%;" src="${ctx}/static/images/profession/ren3_l.gif" alt="" />

			<div class="house10" style="left:11.219%;top:66.167%;width:19.063%;height:33.889%;">
				<img class="house_bg" style="left:0%;top:0%;width:100%;height:100%;" src="${ctx}/static/images/profession/house4.png" alt="" />
				<img class="house_tbg" style="left:1.23%;top:6.557%;width:95.492%;height:38.115%;" src="${ctx}/static/images/profession/housetitlebg.png" alt="" />
				<div class="house_t" style="left:2.459%;top:7.787%;width:93.033%;height:28.689%;overflow:hidden;">
					<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0"><tr align="center"><td align="center" valign="middle" class="totd"></td></tr></table>
				</div>
			</div>
			<div class="house11" style="left:39.211%;top:58.153%;width:19.063%;height:33.889%;">
				<img class="house_bg" style="left:0%;top:0%;width:100%;height:100%;" src="${ctx}/static/images/profession/house3.png" alt="" />
				<img class="house_tbg" style="left:1.23%;top:6.557%;width:95.492%;height:38.115%;" src="${ctx}/static/images/profession/housetitlebg.png" alt="" />
				<div class="house_t" style="left:2.459%;top:7.787%;width:93.033%;height:28.689%;overflow:hidden;">
					<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0"><tr align="center"><td align="center" valign="middle" class="totd"></td></tr></table>
				</div>
			</div>
			<div class="house12" style="left:67.5%;top:66.042%;width:19.063%;height:33.889%;">
				<img class="house_bg" style="left:0%;top:0%;width:100%;height:100%;" src="${ctx}/static/images/profession/house1.png" alt="" />
				<img class="house_tbg" style="left:1.23%;top:6.557%;width:95.492%;height:38.115%;" src="${ctx}/static/images/profession/housetitlebg.png" alt="" />
				<div class="house_t" style="left:2.459%;top:7.787%;width:93.033%;height:28.689%;overflow:hidden;">
					<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0"><tr align="center"><td align="center" valign="middle" class="totd"></td></tr></table>
				</div>
			</div>
		</div>
	</div>
</body>
</html>