$.include([
], '../../');

$(function(){
	var gradeMapModule = angular.module("gradeMapModule",['globalCwn']);
	
	gradeMapModule.controller("gradeMapListController",['Ajax',function(Ajax){
		var pa="images/";
		var pamc="mycontent";
		var car1list=new Array();
		car1list[0]=["car1_r",{left:"-5.926%",top:"38.177%"},{left:"51",top:"51"},100];
		car1list[1]=["car1_d",{left:"46.759%",top:"53.646%"},{left:"20",top:"20"},100];
		car1list[2]=["car1_u",{left:"37.963%",top:"56.51%"},{left:"19",top:"19"},100];
		car1list[3]=["car1_r",{left:"46.759%",top:"53.646%"},{left:"59",top:"59"},1000];
		car1list[4]=[null,{left:"102.5%",top:"69.844%"},null,null];
		var car2list=new Array();
		car2list[0]=["car2_r",{left:"68.519%",top:"35%"},{left:"36",top:"36"},100];
		car2list[1]=["car2_l",{left:"101.389%",top:"44.323%"},{left:"39",top:"39"},100];
		car2list[2]=["car2_d",{left:"68.519%",top:"35.156%"},{left:"26",top:"26"},100];
		car2list[3]=["car2_r",{left:"48.796%",top:"41.354%"},{left:"19",top:"19"},100];
		car2list[4]=["car2_l",{left:"60.556%",top:"45.417%"},{left:"21",top:"21"},100];
		car2list[5]=["car2_u",{left:"48.889%",top:"41.667%"},{left:"31",top:"31"},100];
		car2list[6]=[null,{left:"68.519%",top:"35.26%"},null,null];
		var car3list=new Array();
		car3list[0]=["car3_r",{left:"38.333%",top:"63.75%"},{left:"24",top:"24"},100];
		car3list[1]=["car3_l",{left:"60.833%",top:"70.573%"},{left:"19",top:"19"},100];
		car3list[2]=["car3_d",{left:"45.833%",top:"66.771%"},{left:"57",top:"57"},1000];
		car3list[3]=["car3_u",{left:"-10.833%",top:"83.49%"},{left:"59",top:"59"},100];
		car3list[4]=["car3_l",{left:"47.037%",top:"66.25%"},{left:"17",top:"17"},100];
		car3list[5]=[null,{left:"38.519%",top:"63.958%"},null,null];
		var car4list=new Array();
		car4list[0]=["car4_l",{left:"91.296%",top:"91.979%"},{left:"38",top:"38"},100];
		car4list[1]=["car4_d",{left:"46.481%",top:"79.063%"},{left:"18",top:"18"},100];
		car4list[2]=["car4_u",{left:"36.574%",top:"82.396%"},{left:"16",top:"16"},100];
		car4list[3]=["car4_l",{left:"47.315%",top:"79.375%"},{left:"47",top:"47"},1000];
		car4list[4]=[null,{left:"-5%",top:"64.01%"},null,null];
		var ren1list=new Array();
		ren1list[0]=["ren1_l",{left:"45.37%",top:"30.417%"},{left:"40",top:"40"},100];
		ren1list[1]=["ren1_l",{left:"40.278%",top:"31.667%"},{left:"48",top:"48"},100];
		ren1list[2]=["ren1_r",{left:"32.315%",top:"29.74%"},{left:"43",top:"43"},100];
		ren1list[3]=["ren1_r",{left:"40.093%",top:"31.719%"},{left:"38",top:"38"},1000];
		ren1list[4]=[null,{left:"45.185%",top:"30.469%"},null,null];
		var ren2list=new Array();
		ren2list[0]=["ren2_l",{left:"18.519%",top:"53.906%"},{left:"40",top:"40"},100];
		ren2list[1]=["ren2_l",{left:"13.426%",top:"55.208%"},{left:"48",top:"48"},100];
		ren2list[2]=["ren2_r",{left:"5.463%",top:"53.229%"},{left:"43",top:"43"},100];
		ren2list[3]=["ren2_r",{left:"13.241%",top:"55.208%"},{left:"38",top:"38"},800];
		ren2list[4]=[null,{left:"18.333%",top:"53.958%"},null,null];
		var ren3list=new Array();
		ren3list[0]=["ren3_l",{left:"100.926%",top:"78.75%"},{left:"40",top:"40"},100];
		ren3list[1]=["ren3_l",{left:"95.741%",top:"80%"},{left:"48",top:"48"},100];
		ren3list[2]=["ren3_r",{left:"87.778%",top:"78.021%"},{left:"43",top:"43"},100];
		ren3list[3]=["ren3_r",{left:"95.556%",top:"80%"},{left:"38",top:"38"},1000];
		ren3list[4]=[null,{left:"100.648%",top:"78.802%"},null,null];
		var ren4list=new Array();
		ren4list[0]=["ren4_l",{left:"51.481%",top:"40.26%"},{left:"40",top:"40"},100];
		ren4list[1]=["ren4_l",{left:"56.667%",top:"41.51%"},{left:"48",top:"48"},100];
		ren4list[2]=["ren4_r",{left:"64.63%",top:"39.531%"},{left:"43",top:"43"},100];
		ren4list[3]=["ren4_r",{left:"56.852%",top:"41.458%"},{left:"38",top:"38"},800];
		ren4list[4]=[null,{left:"51.759%",top:"40.26%"},null,null];
		var ren5list=new Array();
		ren5list[0]=["ren5_l",{left:"46.111%",top:"55.677%"},{left:"40",top:"40"},100];
		ren5list[1]=["ren5_l",{left:"41.019%",top:"56.927%"},{left:"48",top:"48"},100];
		ren5list[2]=["ren5_r",{left:"33.056%",top:"55%"},{left:"43",top:"43"},100];
		ren5list[3]=["ren5_r",{left:"40.833%",top:"56.979%"},{left:"38",top:"38"},1000];
		ren5list[4]=[null,{left:"45.926%",top:"55.729%"},null,null];
		var ren6list=new Array();
		ren6list[0]=["ren6_l",{left:"22.87%",top:"87.188%"},{left:"40",top:"40"},100];
		ren6list[1]=["ren6_l",{left:"17.778%",top:"88.438%"},{left:"48",top:"48"},100];
		ren6list[2]=["ren6_r",{left:"9.722%",top:"86.51%"},{left:"43",top:"43"},100];
		ren6list[3]=["ren6_r",{left:"17.5%",top:"88.438%"},{left:"38",top:"38"},800];
		ren6list[4]=[null,{left:"22.593%",top:"87.24%"},null,null];
		
		var orderList=new Array();
		
		init();
		
		function init(){
			beginInit_f();
			stageCentreSetting();
		}
		
		function beginInit_f(){
			fn1();
			readorderlistjsondata("/app/learningmap/grade/orderlist");
		}
		function escapemystr(estr) {
			//%5C%22
			estr = escape(estr);
			estr = shieldFixStr(estr, "%5C%22", "%22");
			estr = unescape(estr);
			estr = estr.substring(estr.indexOf("{"), estr.lastIndexOf("}")+1);
			return estr;
		}
		function shieldFixStr(contentStr, searchStr, replaceStr) {
			while (contentStr.indexOf(searchStr) != -1) {
				contentStr = shieldStr(contentStr, searchStr, replaceStr);
			}
			return contentStr;
			function shieldStr(contentStr, searchStr, replaceStr) {
				var tmpIdx = contentStr.indexOf(searchStr);
				if (tmpIdx != -1) {
					contentStr = contentStr.substring(0, tmpIdx)+replaceStr+contentStr.substring(tmpIdx+searchStr.length, contentStr.length);
				}
				return contentStr;
			}
		}
		function readorderlistjsondata(urluserstr){
			Ajax.post(urluserstr,{},function(myJsonObject){

				if(myJsonObject && myJsonObject.data){
					
				}else{
					myJsonObject=escapemystr(myJsonObject);
					myJsonObject=eval('('+myJsonObject+')');
				}
				orderList=new Array();
				for(var i=0;i<myJsonObject.data.length;i++){
					orderList[i]=new Object();
					orderList[i].myidx=i;
					orderList[i].pfs_id=myJsonObject.data[i].pfs_id;//pfs_id
					orderList[i].pfs_title=myJsonObject.data[i].pfs_title;//pfs_title
					orderList[i].pfs_template=myJsonObject.data[i].pfs_template;//前端显示使用的模板
					orderList[i].url=myJsonObject.data[i].url;//url
				}

				var contentdiv=getId(pamc);
				var f=0;
				do{
					f++;
					var divf=getIdClass(getId(pamc),"F"+f);
					if(divf){
						hidden(divf);
					}
				}while(divf)
				//6,8,8,8,8,8,8,8
				var f1housetotal=6;
				var f2housetotal=8;
				if(orderList.length<=f1housetotal){
					fn1();
					showHouseMC(getIdClass(getId(pamc),"F1"),0,orderList.length-1);
				}else{
					fn1();
					showHouseMC(getIdClass(getId(pamc),"F1"),0,f1housetotal-1);
					var fcount=Math.ceil((orderList.length-f1housetotal)/f2housetotal);
					for(var f=0;f<fcount;f++){
						fnFromNum(f+2);
						var curbinx=f*f2housetotal+f1housetotal;
						var cureinx=curbinx+f2housetotal-1;
						if(cureinx>orderList.length-1){
							cureinx=orderList.length-1;
						}
						showHouseMC(getIdClass(getId(pamc),"F"+(f+2)),curbinx,cureinx);
					}
				}
				stageCentreSetting();
			});
		}
		function showHouseMC(objf,a,b){
			var hidx=0;
			for(var i=a;i<=b;i++){
				hidx++;
				var houseobj=getIdClass(objf,"house"+hidx);
				houseobj.didx=i;
				shObjs(houseobj,true,true,true);
				getIdClass(objf,"house"+hidx).getElementsByTagName("td")[0].innerHTML=orderList[i].pfs_title;
				yoyoEffectTopImg(   getIdClass(objf,"house"+hidx).getElementsByTagName("img")[1]  );
				yoyoEffectTopImg(    getIdClass(objf,"house"+hidx).getElementsByTagName("div")[0]  );
				addListenerObj(houseobj,"",housebtnClick);
			}
			function housebtnClick(e){
				var obj=e.currentTarget;
				var urlstr=orderList[obj.didx].url || "nodelist.html";//default nodelist.html
				
				var index = urlstr.indexOf("?");
				if(index >= 0){
					urlstr = urlstr.substring(0,index) + ".html" + urlstr.substring(index,urlstr.length);
				}
				
				clicked(urlstr)
			}
		}

		function fn1(){
			var fnobj=getIdClass(getId(pamc),"F1");
			show(fnobj);
			shObjs(fnobj,false,false,false);
			basebgeffect(fnobj);
		}
		function fnFromNum(fnum){
			var fnobj=getIdClass(getId(pamc),"F"+fnum);
			if(!fnobj){
				addNewFdiv(fnum);
			}
			fnobj=getIdClass(getId(pamc),"F"+fnum);
			show(fnobj);
			shObjs(fnobj,false,false,false);
			basebgeffect(fnobj);
		}
		function basebgeffect(objf){
			show(getIdClass(objf,"p1"));
			show(getIdClass(objf,"carren"));
			show(getIdClass(objf,"ww1"));
			show(getIdClass(objf,"ww2"));
			show(getIdClass(objf,"ww3"));
			show(getIdClass(objf,"ww4"));
			show(getIdClass(objf,"ww5"));
			beginMoveObj(car1list);
			beginMoveObj(car2list);
			beginMoveObj(car3list);
			beginMoveObj(car4list);
			beginMoveObj(ren1list);
			beginMoveObj(ren2list,400);
			beginMoveObj(ren3list);
			beginMoveObj(ren4list);
			beginMoveObj(ren5list);
			beginMoveObj(ren6list,500);
			function beginMoveObj(carlist,delay){
				var idx=0;
				if(delay>0){
					var obj=getIdClass(objf,carlist[idx][0]);
					show(obj);
					obj.timerout1=setTimeout(moveObj,delay);
				}else{
					moveObj();
				}
				function moveObj(){
					var obj=getIdClass(objf,carlist[idx][0]);
					var json=carlist[idx][1];
					for(var attr in json){
						obj.style[attr]=json[attr];
					}
					show(obj);
					var endjson=getCopyJson(carlist[idx+1][1]);
					startObjMove(obj,endjson,moveEndObj,carlist[idx][2]);
					function moveEndObj(){
						obj.timerout1=setTimeout(nextObj,carlist[idx][3]);
					}
					function nextObj(){
						hidden(obj);
						if(idx<=carlist.length-3){
							idx++;
							moveObj();
						}else{
							idx=0;
							moveObj();
						}
					}
					function getCopyJson(json){
						var returnobj={}
						for(var attr in json){
							returnobj[attr]=json[attr];
						}
						return returnobj;
					}
				}
			}
		}

		function getId(o){
			return document.getElementById(o);
		}
		function show(objD){
			objD.style.display='block';
		}
		function hidden(objH){
			objH.style.display='none';
		}
		function shObjs(objf,isHaveSelf,isDivSH,isImgSH){
			if(isHaveSelf){
				myfn(objf,isDivSH);
			}
			var divarr=objf.getElementsByTagName("div");
			for(var n=0;n<divarr.length;n++){
				myfn(divarr[n],isDivSH);
			}
			var imgarr=objf.getElementsByTagName("img");
			for(var m=0;m<imgarr.length;m++){
				myfn(imgarr[m],isImgSH);
			}
			function myfn(obj,isSH){
				if(isSH==true){
					show(obj);
				}else if(isSH==false){
					hidden(obj);
				}
			}
		}
		function yoyoEffectImg(obj,json1,json2,speed1,speed2){
			if(!json1){
				json1={left:obj.pleft,top:obj.ptop,width:obj.pwidth,height:obj.pheight,opacity:1};
			}
			if(!json2){
				json2={left:obj.pleft,top:obj.ptop,width:obj.pwidth,height:obj.pheight,opacity:1};
			}
			if(!speed1){
				speed1={left:10,top:10,width:10,height:10,opacity:10};
			}
			if(!speed2){
				speed2={left:10,top:10,width:10,height:10,opacity:10};
			}
			eff1();
			function eff1(){
				startObjMove(obj,json1,eff2,speed1);
			}
			function eff2(obj){
				startObjMove(obj,json2,eff1,speed2);
			}
		}
		function startObjMove(obj,json,fnEnd,effframes,updateFuns,completeFuns){
			clearInterval(obj.timer);
			effframes=effframes || {};
			json=getCopyJson();
			for(var attr in json){
				json[attr]=getNumValue(json[attr]);
				obj["eff"+attr+"suffix"]=getSuffixStyle(obj,attr);
				obj["eff"+attr+"Count"]=fixLoToNumber(effframes[attr],25);
				obj["eff"+attr+"addValue"]=(json[attr]-getnfs(attr))/obj["eff"+attr+"Count"];
				obj["eff"+attr+"CurInt"]=0;
			}
			var tmpJson=getCopyJson();
			obj.timer=setInterval(timeInterval,40);
			/////////////////////
			function timeInterval(){
				for(var attr in tmpJson){
					if(obj["eff"+attr+"CurInt"]<obj["eff"+attr+"Count"]-1){
						obj["eff"+attr+"CurInt"]++;
						obj.style[attr]=getnfs(attr)+obj["eff"+attr+"addValue"]+obj["eff"+attr+"suffix"];
						if(updateFuns){
							updateFuns(obj,attr);
						}
					}else{
						obj["eff"+attr+"CurInt"]=obj["eff"+attr+"Count"];
						obj.style[attr]=json[attr]+obj["eff"+attr+"suffix"];
						delete tmpJson[attr];
						if(completeFuns){
							completeFuns(obj,attr);
						}else{
							if(updateFuns){
								updateFuns(obj,attr);
							}
						}
						if(getAllJsonObjComplete()){
							clearInterval(obj.timer);
							if(fnEnd){
								fnEnd(obj);
							}
						}
					}
				}
			}
			function getCopyJson(){
				var returnobj={}
				for(var attr in json){
					returnobj[attr]=json[attr];
				}
				return returnobj;
			}
			function getAllJsonObjComplete(){
				for(var attr in tmpJson){
					return false;
				}
				return true;
			}
			function getnfs(attr){
				var str=obj.style[attr];
				if(attr=="opacity"){
					return fixLoToNumber(str || 1);
				}else{
					return getNumValue(str);
				}
			}
		}
		function fixLoToNumber(num,defaultNum) {
			num = Number(num);
			if (isNaN(num)) {
				defaultNum=Number(defaultNum);
				if(isNaN(defaultNum)){
					return 0;
				}else{
					return defaultNum;
				}
			} else {
				return num;
			}
		}
		function getNumValue(num){
			return fixLoToNumber(parseFloat(num));//.toFixed(2)
		}
		function getSuffixStyle(obj,attr){
			var str=obj.style[attr];
			if(attr=="opacity"){
				return "";
			}else if((str.substring(str.length-1).toLowerCase()=="%")){
				return "%";
			}else{
				return "px";
			}
		}

		function addListenerObj(obj,className,clickFun){
			obj.style.cursor="pointer";
			obj.isHaveListenerClick=true;
			isTouchDevice(obj,null,null,touchend);
			function touchend(evt,obj){
				if(Math.abs(obj.satrtXYList[0].x-obj.moveXYList[0].x)<=15 && Math.abs(obj.satrtXYList[0].y-obj.moveXYList[0].y)<=15){
					if(clickFun){
						clickFun(evt,obj);
					}
				}
			}
		}
		function removeListenerObj(obj) {
			obj.style.cursor="";
			obj.isHaveListenerClick=false;
			if(obj.killListtener){
				obj.killListtener();
			}
		}

		function isTouchDevice(obj,startfun,movefun,endfun){
			if(obj.killListtener){
				obj.killListtener();
			}
			var isHaveTouch=(IsPC())?false:true;
			if(isHaveTouch){
				obj.addEventListener('touchstart',touchstart,false);
			}else{
				obj.onmousedown=touchstart;
			}
			function touchstart(evt){
				if(evt.touches){
					obj.satrtXYList=setTouchObjXYList(evt.touches);
					obj.moveXYList=copyTouchObjXYList(obj.satrtXYList);
					obj.removeEventListener('touchmove',touchmove);
					obj.removeEventListener('touchend',touchend);
					obj.addEventListener('touchmove',touchmove,false);
					obj.addEventListener('touchend',touchend,false);
				}else{
					obj.satrtXYList=setTouchObjXYList([evt]);
					obj.moveXYList=copyTouchObjXYList(obj.satrtXYList);
					obj.onmousemove=null;
					obj.onmouseup=null;
					obj.onmousemove=touchmove;
					obj.onmouseup=touchend;
				}
				if(startfun){
					startfun(evt,obj);
				}
			}
			function touchmove(evt){
				if(evt.touches){
					obj.moveXYList=setTouchObjXYList(evt.touches);
				}else{
					obj.moveXYList=setTouchObjXYList([evt]);
				}
				if(movefun){
					movefun(evt,obj);
				}
			}
			function touchend(evt){
				if(evt.touches){
					obj.removeEventListener('touchmove',touchmove);
					obj.removeEventListener('touchend',touchend);
				}else{
					obj.onmousemove=null;
					obj.onmouseup=null;
				}
				if(endfun){
					endfun(evt,obj);
				}
			}
			obj.killListtener=function(){
				obj.removeEventListener('touchstart',touchstart);
				obj.removeEventListener('touchmove',touchmove);
				obj.removeEventListener('touchend',touchend);
				obj.onmousedown=null;
				obj.onmousemove=null;
				obj.onmouseup=null;
			}
		}
		function setTouchObjXYList(arr){
			var returnList = new Array();
			for(var i=0;i<arr.length;i++){
				var obj=new Object();
				obj.x=arr[i].pageX;
				obj.y=arr[i].pageY;
				returnList[i]=obj;
			}
			return returnList;
		}
		function copyTouchObjXYList(arr) {
			var returnList = new Array();
			for(var i=0;i<arr.length;i++){
				var obj=new Object();
				obj.x=arr[i].x;
				obj.y=arr[i].y;
				returnList[i]=obj;
			}
			return returnList;
		}
		function IsPC()  {  
		   var userAgentInfo = navigator.userAgent;  
		   var Agents = new Array("Android", "iPhone", "SymbianOS", "Windows Phone", "iPad", "iPod");  
		   var flag = true;  
		   for (var v = 0; v < Agents.length; v++) {  
			   if (userAgentInfo.indexOf(Agents[v]) > 0) { flag = false; break; }  
		   }  
		   return flag;  
		}
		function getIdClass(obj,className){
			if(!obj.myObjClass){obj.myObjClass=new Object();}
			if(obj.myObjClass[className]){return obj.myObjClass[className];}
			var divarr=obj.getElementsByTagName("div");
			for(var i=0;i<divarr.length;i++){
				var iobj=divarr[i];
				if(myfun(iobj.className,className)){
					obj.myObjClass[className]=iobj;
					return iobj;
				}
			}
			var imgarr=obj.getElementsByTagName("img");
			for(var j=0;j<imgarr.length;j++){
				var jobj=imgarr[j];
				if(myfun(jobj.className,className)){
					obj.myObjClass[className]=jobj;
					return jobj;
				}
			}
			return null;
			function myfun(objstr,str1){
				if(objstr.indexOf(" ")>=0){
					return (objstr.indexOf(str1)>=0)?true:false;
				}else{
					return (objstr==str1)?true:false;
				}
			}
		}
		
		function yoyoEffectTopImg(obj){
			yoyoEffectImg(obj,{top:getNumValue(obj.style.top)+2.132},{top:getNumValue(obj.style.top)-2.132});
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
			var topoffestPx="43px";//上面留43px为导航栏
			var downoffestPx="0px"//下面留0px为底航栏
			var topoffest=getNumValue(topoffestPx)/dh*100;
			var downoffest=getNumValue(downoffestPx)/dh*100;
			getId("mycontent").style.top=topoffest+"%";
			getId("mycontent").style.height=(100-topoffest-downoffest)+"%";
			dh-=getNumValue(topoffestPx)+getNumValue(downoffestPx);

			var pw=1080;
			var ph=1920;
			var sw=pw/dw;
			var sh=ph/dh;
			var sn=sh/sw;
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
				tdarr[t].style.fontSize=Number(parseFloat(   dw/pw*1.5   ).toFixed(3))+"em";  
			}
		}
	}]);
	
	gradeMapModule.controller("gradeMapDetailController",['Ajax',function(Ajax){
		
		var id = app.getUrlParam("id");
		
		var pa="images/";
		var pamc="mycontent";
		var nodeList=new Array();
		
		init();
		
		function init(){
			beginInit_f();
			stageCentreSetting();
		}
		function beginInit_f(){
			fn1();
			readnodelistjsondata("/app/learningmap/grade/nodelist?id="+id);
		}
		function escapemystr(estr) {
			//%5C%22
			estr = escape(estr);
			estr = shieldFixStr(estr, "%5C%22", "%22");
			estr = unescape(estr);
			estr = estr.substring(estr.indexOf("{"), estr.lastIndexOf("}")+1);
			return estr;
		}
		function shieldFixStr(contentStr, searchStr, replaceStr) {
			while (contentStr.indexOf(searchStr) != -1) {
				contentStr = shieldStr(contentStr, searchStr, replaceStr);
			}
			return contentStr;
			function shieldStr(contentStr, searchStr, replaceStr) {
				var tmpIdx = contentStr.indexOf(searchStr);
				if (tmpIdx != -1) {
					contentStr = contentStr.substring(0, tmpIdx)+replaceStr+contentStr.substring(tmpIdx+searchStr.length, contentStr.length);
				}
				return contentStr;
			}
		}
		function readnodelistjsondata(urluserstr){
			Ajax.post(urluserstr,{},function(myJsonObject){

				if(myJsonObject && myJsonObject.data){
					
				}else{
					myJsonObject=escapemystr(myJsonObject);
					myJsonObject=eval('('+myJsonObject+')');
				}
				nodeList=new Array();
				for(var i=0;i<myJsonObject.data.length;i++){
					nodeList[i]=new Object();
					nodeList[i].myidx=i;
					nodeList[i].pfs_id=myJsonObject.data[i].pfs_id;
					nodeList[i].psi_id=myJsonObject.data[i].psi_id;
					nodeList[i].pfs_title=myJsonObject.data[i].pfs_title;
					nodeList[i].ugr_display_bil=myJsonObject.data[i].ugr_display_bil;
					nodeList[i].url=myJsonObject.data[i].url;
				}
				hidden(getIdClass(getId(pamc),"F1"));
				hidden(getIdClass(getId(pamc),"F2"));
				hidden(getIdClass(getId(pamc),"F3"));
				if(myJsonObject.data.length<=5){
					fn1();
					showHouseMC(getIdClass(getId(pamc),"F1"),0,myJsonObject.data.length-1);
				}else if(myJsonObject.data.length<=15){
					fn1();
					fn2();
					showHouseMC(getIdClass(getId(pamc),"F1"),0,4);
					showHouseMC(getIdClass(getId(pamc),"F2"),5,myJsonObject.data.length-1);
				}else if(myJsonObject.data.length<=25){
					fn1();
					fn2();
					fn3();
					showHouseMC(getIdClass(getId(pamc),"F1"),0,4);
					showHouseMC(getIdClass(getId(pamc),"F2"),5,14);
					showHouseMC(getIdClass(getId(pamc),"F3"),15,myJsonObject.data.length-1);
				}else if(myJsonObject.data.length>=26){
					fn1();
					fn2();
					fn3();
					showHouseMC(getIdClass(getId(pamc),"F1"),0,4);
					showHouseMC(getIdClass(getId(pamc),"F2"),5,14);
					showHouseMC(getIdClass(getId(pamc),"F3"),15,24);
				}
			
				
			});
		}
		function showHouseMC(objf,a,b){
			var hidx=0;
			for(var i=a;i<=b;i++){
				hidx++;
				var houseobj=getIdClass(objf,"house"+hidx);
				houseobj.didx=i;
				shObjs(houseobj,true,true,true);
				getIdClass(objf,"house"+hidx).getElementsByTagName("td")[0].innerHTML=nodeList[i].pfs_title+"<br>"+nodeList[i].ugr_display_bil;
				yoyoEffectTopImg(   getIdClass(objf,"house"+hidx).getElementsByTagName("img")[1]  );
				yoyoEffectTopImg(    getIdClass(objf,"house"+hidx).getElementsByTagName("div")[0]  );
				addListenerObj(houseobj,"",housebtnClick);
			}
			function housebtnClick(e){
				var obj=e.currentTarget;
				var urlstr=nodeList[obj.didx].url;
				urlstr = urlstr.replace(/&amp;/g,"&"); 
				var index = urlstr.indexOf("?");
				if(index >= 0){
					urlstr = "courseList.html" + urlstr.substring(index,urlstr.length);
				}
				
				clicked(urlstr);
			}
		}

		function fn1(){
			var fnobj=getIdClass(getId(pamc),"F1");
			show(fnobj);
			shObjs(fnobj,false,false,false);
			showwwbg();
			function showwwbg(){
				shObjs(getIdClass(fnobj,"renitem"),true,true,true);
			}
		}
		function fn2(){
			var fnobj=getIdClass(getId(pamc),"F2");
			show(fnobj);
			shObjs(fnobj,false,false,false);
			showwwbg();
			function showwwbg(){
				shObjs(getIdClass(fnobj,"renitem"),true,true,true);
			}
		}
		function fn3(){
			var fnobj=getIdClass(getId(pamc),"F3");
			show(fnobj);
			shObjs(fnobj,false,false,false);
			showwwbg();
			function showwwbg(){
				shObjs(getIdClass(fnobj,"renitem"),true,true,true);
			}
		}

		function getId(o){
			return document.getElementById(o);
		}
		function show(objD){
			objD.style.display='block';
		}
		function hidden(objH){
			objH.style.display='none';
		}
		function shObjs(objf,isHaveSelf,isDivSH,isImgSH){
			if(isHaveSelf){
				myfn(objf,isDivSH);
			}
			var divarr=objf.getElementsByTagName("div");
			for(var n=0;n<divarr.length;n++){
				myfn(divarr[n],isDivSH);
			}
			var imgarr=objf.getElementsByTagName("img");
			for(var m=0;m<imgarr.length;m++){
				myfn(imgarr[m],isImgSH);
			}
			function myfn(obj,isSH){
				if(isSH==true){
					show(obj);
				}else if(isSH==false){
					hidden(obj);
				}
			}
		}
		function yoyoEffectImg(obj,json1,json2,speed1,speed2){
			if(!json1){
				json1={left:obj.pleft,top:obj.ptop,width:obj.pwidth,height:obj.pheight,opacity:1};
			}
			if(!json2){
				json2={left:obj.pleft,top:obj.ptop,width:obj.pwidth,height:obj.pheight,opacity:1};
			}
			if(!speed1){
				speed1={left:10,top:10,width:10,height:10,opacity:10};
			}
			if(!speed2){
				speed2={left:10,top:10,width:10,height:10,opacity:10};
			}
			eff1();
			function eff1(){
				startObjMove(obj,json1,eff2,speed1);
			}
			function eff2(obj){
				startObjMove(obj,json2,eff1,speed2);
			}
		}
		function startObjMove(obj,json,fnEnd,effframes,updateFuns,completeFuns){
			clearInterval(obj.timer);
			effframes=effframes || {};
			json=getCopyJson();
			for(var attr in json){
				json[attr]=getNumValue(json[attr]);
				obj["eff"+attr+"suffix"]=getSuffixStyle(obj,attr);
				obj["eff"+attr+"Count"]=fixLoToNumber(effframes[attr],25);
				obj["eff"+attr+"addValue"]=(json[attr]-getnfs(attr))/obj["eff"+attr+"Count"];
				obj["eff"+attr+"CurInt"]=0;
			}
			var tmpJson=getCopyJson();
			obj.timer=setInterval(timeInterval,40);
			/////////////////////
			function timeInterval(){
				for(var attr in tmpJson){
					if(obj["eff"+attr+"CurInt"]<obj["eff"+attr+"Count"]-1){
						obj["eff"+attr+"CurInt"]++;
						obj.style[attr]=getnfs(attr)+obj["eff"+attr+"addValue"]+obj["eff"+attr+"suffix"];
						if(updateFuns){
							updateFuns(obj,attr);
						}
					}else{
						obj["eff"+attr+"CurInt"]=obj["eff"+attr+"Count"];
						obj.style[attr]=json[attr]+obj["eff"+attr+"suffix"];
						delete tmpJson[attr];
						if(completeFuns){
							completeFuns(obj,attr);
						}else{
							if(updateFuns){
								updateFuns(obj,attr);
							}
						}
						if(getAllJsonObjComplete()){
							clearInterval(obj.timer);
							if(fnEnd){
								fnEnd(obj);
							}
						}
					}
				}
			}
			function getCopyJson(){
				var returnobj={}
				for(var attr in json){
					returnobj[attr]=json[attr];
				}
				return returnobj;
			}
			function getAllJsonObjComplete(){
				for(var attr in tmpJson){
					return false;
				}
				return true;
			}
			function getnfs(attr){
				var str=obj.style[attr];
				if(attr=="opacity"){
					return fixLoToNumber(str || 1);
				}else{
					return getNumValue(str);
				}
			}
		}
		function fixLoToNumber(num,defaultNum) {
			num = Number(num);
			if (isNaN(num)) {
				defaultNum=Number(defaultNum);
				if(isNaN(defaultNum)){
					return 0;
				}else{
					return defaultNum;
				}
			} else {
				return num;
			}
		}
		function getNumValue(num){
			return fixLoToNumber(parseFloat(num));//.toFixed(2)
		}
		function getSuffixStyle(obj,attr){
			var str=obj.style[attr];
			if(attr=="opacity"){
				return "";
			}else if((str.substring(str.length-1).toLowerCase()=="%")){
				return "%";
			}else{
				return "px";
			}
		}

		function addListenerObj(obj,className,clickFun){
			obj.style.cursor="pointer";
			obj.isHaveListenerClick=true;
			isTouchDevice(obj,null,null,touchend);
			function touchend(evt,obj){
				if(Math.abs(obj.satrtXYList[0].x-obj.moveXYList[0].x)<=15 && Math.abs(obj.satrtXYList[0].y-obj.moveXYList[0].y)<=15){
					if(clickFun){
						clickFun(evt,obj);
					}
				}
			}
		}
		function removeListenerObj(obj) {
			obj.style.cursor="";
			obj.isHaveListenerClick=false;
			if(obj.killListtener){
				obj.killListtener();
			}
		}

		function isTouchDevice(obj,startfun,movefun,endfun){
			if(obj.killListtener){
				obj.killListtener();
			}
			var isHaveTouch=(IsPC())?false:true;
			if(isHaveTouch){
				obj.addEventListener('touchstart',touchstart,false);
			}else{
				obj.onmousedown=touchstart;
			}
			function touchstart(evt){
				if(evt.touches){
					obj.satrtXYList=setTouchObjXYList(evt.touches);
					obj.moveXYList=copyTouchObjXYList(obj.satrtXYList);
					obj.removeEventListener('touchmove',touchmove);
					obj.removeEventListener('touchend',touchend);
					obj.addEventListener('touchmove',touchmove,false);
					obj.addEventListener('touchend',touchend,false);
				}else{
					obj.satrtXYList=setTouchObjXYList([evt]);
					obj.moveXYList=copyTouchObjXYList(obj.satrtXYList);
					obj.onmousemove=null;
					obj.onmouseup=null;
					obj.onmousemove=touchmove;
					obj.onmouseup=touchend;
				}
				if(startfun){
					startfun(evt,obj);
				}
			}
			function touchmove(evt){
				if(evt.touches){
					obj.moveXYList=setTouchObjXYList(evt.touches);
				}else{
					obj.moveXYList=setTouchObjXYList([evt]);
				}
				if(movefun){
					movefun(evt,obj);
				}
			}
			function touchend(evt){
				if(evt.touches){
					obj.removeEventListener('touchmove',touchmove);
					obj.removeEventListener('touchend',touchend);
				}else{
					obj.onmousemove=null;
					obj.onmouseup=null;
				}
				if(endfun){
					endfun(evt,obj);
				}
			}
			obj.killListtener=function(){
				obj.removeEventListener('touchstart',touchstart);
				obj.removeEventListener('touchmove',touchmove);
				obj.removeEventListener('touchend',touchend);
				obj.onmousedown=null;
				obj.onmousemove=null;
				obj.onmouseup=null;
			}
		}
		function setTouchObjXYList(arr){
			var returnList = new Array();
			for(var i=0;i<arr.length;i++){
				var obj=new Object();
				obj.x=arr[i].pageX;
				obj.y=arr[i].pageY;
				returnList[i]=obj;
			}
			return returnList;
		}
		function copyTouchObjXYList(arr) {
			var returnList = new Array();
			for(var i=0;i<arr.length;i++){
				var obj=new Object();
				obj.x=arr[i].x;
				obj.y=arr[i].y;
				returnList[i]=obj;
			}
			return returnList;
		}
		function IsPC()  {  
		   var userAgentInfo = navigator.userAgent;  
		   var Agents = new Array("Android", "iPhone", "SymbianOS", "Windows Phone", "iPad", "iPod");  
		   var flag = true;  
		   for (var v = 0; v < Agents.length; v++) {  
			   if (userAgentInfo.indexOf(Agents[v]) > 0) { flag = false; break; }  
		   }  
		   return flag;  
		}
		function getIdClass(obj,className){
			if(!obj.myObjClass){obj.myObjClass=new Object();}
			if(obj.myObjClass[className]){return obj.myObjClass[className];}
			var divarr=obj.getElementsByTagName("div");
			for(var i=0;i<divarr.length;i++){
				var iobj=divarr[i];
				if(myfun(iobj.className,className)){
					obj.myObjClass[className]=iobj;
					return iobj;
				}
			}
			var imgarr=obj.getElementsByTagName("img");
			for(var j=0;j<imgarr.length;j++){
				var jobj=imgarr[j];
				if(myfun(jobj.className,className)){
					obj.myObjClass[className]=jobj;
					return jobj;
				}
			}
			return null;
			function myfun(objstr,str1){
				if(objstr.indexOf(" ")>=0){
					return (objstr.indexOf(str1)>=0)?true:false;
				}else{
					return (objstr==str1)?true:false;
				}
			}
		}
		
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
			var topoffestPx="43px";//上面留43px为导航栏
			var downoffestPx="0px"//下面留0px为底航栏
			var topoffest=getNumValue(topoffestPx)/dh*100;
			var downoffest=getNumValue(downoffestPx)/dh*100;
			getId("mycontent").style.top=topoffest+"%";
			getId("mycontent").style.height=(100-topoffest-downoffest)+"%";
			dh-=getNumValue(topoffestPx)+getNumValue(downoffestPx);

			var pw=1080;
			var ph=1920;
			var sw=pw/dw;
			var sh=ph/dh;
			var sn=sh/sw;
			var tmpp=sn*100;
			for(var f=1;f<=3;f++){
				getIdClass(getId(pamc),"F"+f).style.height=Number(parseFloat(   tmpp   ).toFixed(3))+"%";
				getIdClass(getId(pamc),"F"+f).style.top=Number(parseFloat(   (f-1)*tmpp   ).toFixed(3))+"%";   
			}
			var tdarr=getId(pamc).getElementsByTagName("td");
			for(var t=0;t<tdarr.length;t++){
				tdarr[t].style.fontSize=Number(parseFloat(   dw/pw*2   ).toFixed(3))+"em";  
			}
		}
	}]);
	
	gradeMapModule.controller("gradeMapCourseListController",['Ajax',function(Ajax){
		
		var id = app.getUrlParam("id");
		var pfs_id = app.getUrlParam("pfs_id");
		
		var pa="images/";
		var pamc="mycontent";
		var courseList=new Array();
		
		init();
		
		function init(){
			beginInit_f();
			stageCentreSetting();
		}
		
		function beginInit_f(){
			fn1();
			readnodelistjsondata("/app/learningmap/grade/courselist?pfs_id="+pfs_id+"&id="+id+"");
		}
		function escapemystr(estr) {
			//%5C%22
			estr = escape(estr);
			estr = shieldFixStr(estr, "%5C%22", "%22");
			estr = unescape(estr);
			estr = estr.substring(estr.indexOf("{"), estr.lastIndexOf("}")+1);
			return estr;
		}
		function shieldFixStr(contentStr, searchStr, replaceStr) {
			while (contentStr.indexOf(searchStr) != -1) {
				contentStr = shieldStr(contentStr, searchStr, replaceStr);
			}
			return contentStr;
			function shieldStr(contentStr, searchStr, replaceStr) {
				var tmpIdx = contentStr.indexOf(searchStr);
				if (tmpIdx != -1) {
					contentStr = contentStr.substring(0, tmpIdx)+replaceStr+contentStr.substring(tmpIdx+searchStr.length, contentStr.length);
				}
				return contentStr;
			}
		}
		function readnodelistjsondata(urluserstr){
			Ajax.post(urluserstr,{},function(myJsonObject){

				if(myJsonObject && myJsonObject.data){
					
				}else{
					myJsonObject=escapemystr(myJsonObject);
					myJsonObject=eval('('+myJsonObject+')');
				}
				courseList=new Array();
				for(var i=0;i<myJsonObject.data.length;i++){
					courseList[i]=new Object();
					courseList[i].myidx=i;
					courseList[i].itm_id=myJsonObject.data[i].itm_id;//ok
					courseList[i].itm_canread=myJsonObject.data[i].itm_canread;//1:标题有连接，0：不没连接 ok
					courseList[i].itm_status_off=myJsonObject.data[i].itm_status_off;
					courseList[i].itm_title=myJsonObject.data[i].itm_title;//标题 ok
					courseList[i].itd_compulsory_ind=myJsonObject.data[i].itd_compulsory_ind;//1:必修,0:选修 ok
					courseList[i].itm_icon= serverHost + myJsonObject.data[i].itm_icon;//图片 ok
					courseList[i].itm_publish_timestamp=myJsonObject.data[i].itm_publish_timestamp;//发布日期 ok
					courseList[i].itm_exam_ind=myJsonObject.data[i].itm_exam_ind;
					courseList[i].itm_type=myJsonObject.data[i].itm_type;//selfstudy:网上课程,classroom:面授课程,intergrated:项目式培训  ok
				}
				hidden(getIdClass(getId(pamc),"F1"));
				fn1();
				showHouseMC(getIdClass(getId(pamc),"F1"),0,courseList.length-1);
				stageCentreSetting();
			
				
			});
		}
		function showHouseMC(objf,a,b){
			var hidx=0;
			var beginTopNum=8.073;
			var topAddNum=15.3125;//=  294/1920*100
			for(var i=a;i<=b;i++){
				hidx++;
				if(hidx>1){
					if(!getIdClass(objf,"house"+hidx)){
						var houseobj=getIdClass(objf,"house1");
						var html=houseobj.innerHTML;
						var div=document.createElement('div');
						div.className="house"+hidx;
						div.style.left="1.481%";
						div.style.width="97.037%";
						div.style.height="17.188%";
						div.style.display="none";
						div.innerHTML=html;
						objf.appendChild(div);
					}
				}
				var curTopNum=beginTopNum+(hidx-1)*topAddNum;
				var houseobj=getIdClass(objf,"house"+hidx);
				houseobj.style.top=curTopNum+"%";
				houseobj.didx=i;
				shObjs(houseobj,true,true,false);
				show(getIdClass(houseobj,"house_bg"));
				show(getIdClass(houseobj,"house_tbg"));
				var tmpobj=getIdClass(houseobj,"house_type_"+courseList[i].itm_type.toLowerCase());
				if(tmpobj){
					show(tmpobj);
				}
				var tmpobj=getIdClass(houseobj,"house_compulsory"+courseList[i].itd_compulsory_ind);
				if(tmpobj){
					show(tmpobj);
				}
				var tdlist=getIdClass(objf,"house"+hidx).getElementsByTagName("td");
				tdlist[0].innerHTML=courseList[i].itm_title;
				tdlist[1].innerHTML='';
				tdlist[2].innerHTML=courseList[i].itm_publish_timestamp;
				var imglist=getIdClass(objf,"house"+hidx).getElementsByTagName("img");
				imglist[1].src=courseList[i].itm_icon;
				if(courseList[i].itm_canread=="1"){
					addListenerObj(houseobj,"",housebtnClick);
				}else{
					show(getIdClass(houseobj,"house_notcanread"));
					removeListenerObj(houseobj);
				}
			}

			var houseobjlast=getIdClass(objf,"house"+hidx);
			var bg=getIdClass(objf,"bg");
			if(houseobjlast){
				bg.style.height=getNumValue(houseobjlast.style.top)+getNumValue(houseobjlast.style.height)+"%";
			}
			function housebtnClick(e){
				var obj=e.currentTarget;
				var urlstr="../course/detail.html?itmId="+wbEncryptor.encrypt(courseList[obj.didx].itm_id);
				clicked(urlstr);
			}
		}
		function fn1(){
			var fnobj=getIdClass(getId(pamc),"F1");
			show(fnobj);
			shObjs(fnobj,false,false,false);
			showwwbg();
			function showwwbg(){
				show(getIdClass(fnobj,"bg"));
				show(getIdClass(fnobj,"topbg"));
			}
		}

		function getId(o){
			return document.getElementById(o);
		}
		function show(objD){
			objD.style.display='block';
		}
		function hidden(objH){
			objH.style.display='none';
		}
		function shObjs(objf,isHaveSelf,isDivSH,isImgSH){
			if(isHaveSelf){
				myfn(objf,isDivSH);
			}
			var divarr=objf.getElementsByTagName("div");
			for(var n=0;n<divarr.length;n++){
				myfn(divarr[n],isDivSH);
			}
			var imgarr=objf.getElementsByTagName("img");
			for(var m=0;m<imgarr.length;m++){
				myfn(imgarr[m],isImgSH);
			}
			function myfn(obj,isSH){
				if(isSH==true){
					show(obj);
				}else if(isSH==false){
					hidden(obj);
				}
			}
		}
		function yoyoEffectImg(obj,json1,json2,speed1,speed2){
			if(!json1){
				json1={left:obj.pleft,top:obj.ptop,width:obj.pwidth,height:obj.pheight,opacity:1};
			}
			if(!json2){
				json2={left:obj.pleft,top:obj.ptop,width:obj.pwidth,height:obj.pheight,opacity:1};
			}
			if(!speed1){
				speed1={left:10,top:10,width:10,height:10,opacity:10};
			}
			if(!speed2){
				speed2={left:10,top:10,width:10,height:10,opacity:10};
			}
			eff1();
			function eff1(){
				startObjMove(obj,json1,eff2,speed1);
			}
			function eff2(obj){
				startObjMove(obj,json2,eff1,speed2);
			}
		}
		function startObjMove(obj,json,fnEnd,effframes,updateFuns,completeFuns){
			clearInterval(obj.timer);
			effframes=effframes || {};
			json=getCopyJson();
			for(var attr in json){
				json[attr]=getNumValue(json[attr]);
				obj["eff"+attr+"suffix"]=getSuffixStyle(obj,attr);
				obj["eff"+attr+"Count"]=fixLoToNumber(effframes[attr],25);
				obj["eff"+attr+"addValue"]=(json[attr]-getnfs(attr))/obj["eff"+attr+"Count"];
				obj["eff"+attr+"CurInt"]=0;
			}
			var tmpJson=getCopyJson();
			obj.timer=setInterval(timeInterval,40);
			/////////////////////
			function timeInterval(){
				for(var attr in tmpJson){
					if(obj["eff"+attr+"CurInt"]<obj["eff"+attr+"Count"]-1){
						obj["eff"+attr+"CurInt"]++;
						obj.style[attr]=getnfs(attr)+obj["eff"+attr+"addValue"]+obj["eff"+attr+"suffix"];
						if(updateFuns){
							updateFuns(obj,attr);
						}
					}else{
						obj["eff"+attr+"CurInt"]=obj["eff"+attr+"Count"];
						obj.style[attr]=json[attr]+obj["eff"+attr+"suffix"];
						delete tmpJson[attr];
						if(completeFuns){
							completeFuns(obj,attr);
						}else{
							if(updateFuns){
								updateFuns(obj,attr);
							}
						}
						if(getAllJsonObjComplete()){
							clearInterval(obj.timer);
							if(fnEnd){
								fnEnd(obj);
							}
						}
					}
				}
			}
			function getCopyJson(){
				var returnobj={}
				for(var attr in json){
					returnobj[attr]=json[attr];
				}
				return returnobj;
			}
			function getAllJsonObjComplete(){
				for(var attr in tmpJson){
					return false;
				}
				return true;
			}
			function getnfs(attr){
				var str=obj.style[attr];
				if(attr=="opacity"){
					return fixLoToNumber(str || 1);
				}else{
					return getNumValue(str);
				}
			}
		}
		function fixLoToNumber(num,defaultNum) {
			num = Number(num);
			if (isNaN(num)) {
				defaultNum=Number(defaultNum);
				if(isNaN(defaultNum)){
					return 0;
				}else{
					return defaultNum;
				}
			} else {
				return num;
			}
		}
		function getNumValue(num){
			return fixLoToNumber(parseFloat(num));//.toFixed(2)
		}
		function getSuffixStyle(obj,attr){
			var str=obj.style[attr];
			if(attr=="opacity"){
				return "";
			}else if((str.substring(str.length-1).toLowerCase()=="%")){
				return "%";
			}else{
				return "px";
			}
		}

		function addListenerObj(obj,className,clickFun){
			obj.style.cursor="pointer";
			obj.isHaveListenerClick=true;
			isTouchDevice(obj,null,null,touchend);
			function touchend(evt,obj){
				if(Math.abs(obj.satrtXYList[0].x-obj.moveXYList[0].x)<=15 && Math.abs(obj.satrtXYList[0].y-obj.moveXYList[0].y)<=15){
					if(clickFun){
						clickFun(evt,obj);
					}
				}
			}
		}
		function removeListenerObj(obj) {
			obj.style.cursor="";
			obj.isHaveListenerClick=false;
			if(obj.killListtener){
				obj.killListtener();
			}
		}

		function isTouchDevice(obj,startfun,movefun,endfun){
			if(obj.killListtener){
				obj.killListtener();
			}
			var isHaveTouch=(IsPC())?false:true;
			if(isHaveTouch){
				obj.addEventListener('touchstart',touchstart,false);
			}else{
				obj.onmousedown=touchstart;
			}
			function touchstart(evt){
				if(evt.touches){
					obj.satrtXYList=setTouchObjXYList(evt.touches);
					obj.moveXYList=copyTouchObjXYList(obj.satrtXYList);
					obj.removeEventListener('touchmove',touchmove);
					obj.removeEventListener('touchend',touchend);
					obj.addEventListener('touchmove',touchmove,false);
					obj.addEventListener('touchend',touchend,false);
				}else{
					obj.satrtXYList=setTouchObjXYList([evt]);
					obj.moveXYList=copyTouchObjXYList(obj.satrtXYList);
					obj.onmousemove=null;
					obj.onmouseup=null;
					obj.onmousemove=touchmove;
					obj.onmouseup=touchend;
				}
				if(startfun){
					startfun(evt,obj);
				}
			}
			function touchmove(evt){
				if(evt.touches){
					obj.moveXYList=setTouchObjXYList(evt.touches);
				}else{
					obj.moveXYList=setTouchObjXYList([evt]);
				}
				if(movefun){
					movefun(evt,obj);
				}
			}
			function touchend(evt){
				if(evt.touches){
					obj.removeEventListener('touchmove',touchmove);
					obj.removeEventListener('touchend',touchend);
				}else{
					obj.onmousemove=null;
					obj.onmouseup=null;
				}
				if(endfun){
					endfun(evt,obj);
				}
			}
			obj.killListtener=function(){
				obj.removeEventListener('touchstart',touchstart);
				obj.removeEventListener('touchmove',touchmove);
				obj.removeEventListener('touchend',touchend);
				obj.onmousedown=null;
				obj.onmousemove=null;
				obj.onmouseup=null;
			}
		}
		function setTouchObjXYList(arr){
			var returnList = new Array();
			for(var i=0;i<arr.length;i++){
				var obj=new Object();
				obj.x=arr[i].pageX;
				obj.y=arr[i].pageY;
				returnList[i]=obj;
			}
			return returnList;
		}
		function copyTouchObjXYList(arr) {
			var returnList = new Array();
			for(var i=0;i<arr.length;i++){
				var obj=new Object();
				obj.x=arr[i].x;
				obj.y=arr[i].y;
				returnList[i]=obj;
			}
			return returnList;
		}
		function IsPC()  {  
		   var userAgentInfo = navigator.userAgent;  
		   var Agents = new Array("Android", "iPhone", "SymbianOS", "Windows Phone", "iPad", "iPod");  
		   var flag = true;  
		   for (var v = 0; v < Agents.length; v++) {  
			   if (userAgentInfo.indexOf(Agents[v]) > 0) { flag = false; break; }  
		   }  
		   return flag;  
		}
		function getIdClass(obj,className){
			if(!obj.myObjClass){obj.myObjClass=new Object();}
			if(obj.myObjClass[className]){return obj.myObjClass[className];}
			var divarr=obj.getElementsByTagName("div");
			for(var i=0;i<divarr.length;i++){
				var iobj=divarr[i];
				if(myfun(iobj.className,className)){
					obj.myObjClass[className]=iobj;
					return iobj;
				}
			}
			var imgarr=obj.getElementsByTagName("img");
			for(var j=0;j<imgarr.length;j++){
				var jobj=imgarr[j];
				if(myfun(jobj.className,className)){
					obj.myObjClass[className]=jobj;
					return jobj;
				}
			}
			return null;
			function myfun(objstr,str1){
				if(objstr.indexOf(" ")>=0){
					return (objstr.indexOf(str1)>=0)?true:false;
				}else{
					return (objstr==str1)?true:false;
				}
			}
		}
		
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
			var topoffestPx="43px";//上面留43px为导航栏
			var downoffestPx="0px"//下面留0px为底航栏
			var topoffest=getNumValue(topoffestPx)/dh*100;
			var downoffest=getNumValue(downoffestPx)/dh*100;
			getId("mycontent").style.top=topoffest+"%";
			getId("mycontent").style.height=(100-topoffest-downoffest)+"%";
			dh-=getNumValue(topoffestPx)+getNumValue(downoffestPx);

			var pw=1080;
			var ph=1920;
			var sw=pw/dw;
			var sh=ph/dh;
			var sn=sh/sw;
			var tmpp=sn*100;
			for(var f=1;f<=1;f++){
				getIdClass(getId(pamc),"F"+f).style.height=Number(parseFloat(   tmpp   ).toFixed(3))+"%";
				getIdClass(getId(pamc),"F"+f).style.top=Number(parseFloat(   (f-1)*tmpp   ).toFixed(3))+"%";   
			}
			var tdarr=getId(pamc).getElementsByTagName("td");
			for(var t=0;t<tdarr.length;t++){
				tdarr[t].style.fontSize=Number(parseFloat(   dw/pw*1.7   ).toFixed(3))+"em";  
			}
		}
		
	}]);
});