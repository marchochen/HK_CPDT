var pa="images/";
var pamc="mycontent";
var orderList=new Array();
wbEncrytor = new wbEncrytor;
function orderlist_init(){
	beginInit_f();
	stageCentreSetting();
}
function beginInit_f(){
	fn1();
	readorderlistjsondata(contextPath+"/app/learningmap/grade/orderlist"  );
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
	//var myJsonObject='{"data":[{"pfs_id":"1","pfs_title":"技术发展序列008","pfs_template":"1","url":"grade/nodelist?id=1"},{"pfs_id":"2","pfs_title":"人事发展序列2","pfs_template":"2","url":"grade/nodelist?id=2"},{"pfs_id":"3","pfs_title":"人事发展序列3","pfs_template":"3","url":"grade/nodelist?id=3"},{"pfs_id":"4","pfs_title":"人事发展序列4","pfs_template":"4","url":"grade/nodelist?id=4"},{"pfs_id":"5","pfs_title":"人事发展序列5","pfs_template":"5","url":"grade/nodelist?id=5"},{"pfs_id":"6","pfs_title":"人事发展序列6","pfs_template":"6","url":"grade/nodelist?id=6"},{"pfs_id":"7","pfs_title":"人事发展序列7","pfs_template":"7","url":"grade/nodelist?id=7"},{"pfs_id":"8","pfs_title":"人事发展序列8","pfs_template":"8","url":"grade/nodelist?id=8"},{"pfs_id":"9","pfs_title":"人事发展序列9","pfs_template":"9","url":"grade/nodelist?id=9"},{"pfs_id":"10","pfs_title":"人事发展序列10","pfs_template":"10","url":"grade/nodelist?id=10"},{"pfs_id":"11","pfs_title":"人事发展序列11","pfs_template":"11","url":"grade/nodelist?id=11"},{"pfs_id":"12","pfs_title":"人事发展序列12","pfs_template":"12","url":"grade/nodelist?id=12"},{"pfs_id":"13","pfs_title":"人事发展序列13","pfs_template":"13","url":"grade/nodelist?id=13"},{"pfs_id":"14","pfs_title":"人事发展序列14","pfs_template":"14","url":"grade/nodelist?id=14"},{"pfs_id":"15","pfs_title":"人事发展序列15","pfs_template":"15","url":"grade/nodelist?id=15"},{"pfs_id":"16","pfs_title":"人事发展序列16","pfs_template":"16","url":"grade/nodelist?id=16"},{"pfs_id":"17","pfs_title":"人事发展序列17","pfs_template":"17","url":"grade/nodelist?id=17"},{"pfs_id":"18","pfs_title":"人事发展序列18","pfs_template":"18","url":"grade/nodelist?id=18"},{"pfs_id":"19","pfs_title":"人事发展序列19","pfs_template":"19","url":"grade/nodelist?id=19"},{"pfs_id":"20","pfs_title":"人事发展序列20","pfs_template":"20","url":"grade/nodelist?id=20"},{"pfs_id":"21","pfs_title":"人事发展序列21","pfs_template":"21","url":"grade/nodelist?id=21"},{"pfs_id":"22","pfs_title":"人事发展序列228","pfs_template":"22","url":"grade/nodelist?id=22"}]}';
	//var myJsonObject='{"data":[{"pfs_id":"1","pfs_title":"技术发展序列008","pfs_template":"1","url":"grade/nodelist?id=1"},{"pfs_id":"2","pfs_title":"人事发展序列2","pfs_template":"2","url":"grade/nodelist?id=2"},{"pfs_id":"3","pfs_title":"人事发展序列3","pfs_template":"3","url":"grade/nodelist?id=3"},{"pfs_id":"4","pfs_title":"人事发展序列4","pfs_template":"4","url":"grade/nodelist?id=4"},{"pfs_id":"5","pfs_title":"人事发展序列5","pfs_template":"5","url":"grade/nodelist?id=5"},{"pfs_id":"6","pfs_title":"人事发展序列6","pfs_template":"6","url":"grade/nodelist?id=6"},{"pfs_id":"7","pfs_title":"人事发展序列7","pfs_template":"7","url":"grade/nodelist?id=7"},{"pfs_id":"8","pfs_title":"人事发展序列8","pfs_template":"8","url":"grade/nodelist?id=8"},{"pfs_id":"9","pfs_title":"人事发展序列9","pfs_template":"9","url":"grade/nodelist?id=9"},{"pfs_id":"10","pfs_title":"人事发展序列10","pfs_template":"10","url":"grade/nodelist?id=10"},{"pfs_id":"11","pfs_title":"人事发展序列11","pfs_template":"11","url":"grade/nodelist?id=11"},{"pfs_id":"12","pfs_title":"人事发展序列12","pfs_template":"12","url":"grade/nodelist?id=12"},{"pfs_id":"13","pfs_title":"人事发展序列13","pfs_template":"13","url":"grade/nodelist?id=13"},{"pfs_id":"14","pfs_title":"人事发展序列14","pfs_template":"14","url":"grade/nodelist?id=14"},{"pfs_id":"15","pfs_title":"人事发展序列15","pfs_template":"15","url":"grade/nodelist?id=15"},{"pfs_id":"16","pfs_title":"人事发展序列16","pfs_template":"16","url":"grade/nodelist?id=16"},{"pfs_id":"17","pfs_title":"人事发展序列17","pfs_template":"17","url":"grade/nodelist?id=17"},{"pfs_id":"18","pfs_title":"人事发展序列18","pfs_template":"18","url":"grade/nodelist?id=18"},{"pfs_id":"19","pfs_title":"人事发展序列19","pfs_template":"19","url":"grade/nodelist?id=19"},{"pfs_id":"20","pfs_title":"人事发展序列20","pfs_template":"20","url":"grade/nodelist?id=20"},{"pfs_id":"21","pfs_title":"人事发展序列21","pfs_template":"21","url":"grade/nodelist?id=21"},{"pfs_id":"22","pfs_title":"人事发展序列228","pfs_template":"22","url":"grade/nodelist?id=22"},{"pfs_id":"23","pfs_title":"人事发展序列23","pfs_template":"23","url":"grade/nodelist?id=23"}]}';
	$.get(urluserstr, function(myJsonObject){
		if(myJsonObject && myJsonObject.data){
			
		}else{
			myJsonObject=escapemystr(myJsonObject);
			myJsonObject=eval('('+myJsonObject+')');
		}
		orderList=new Array();
		for(var i=0;i<myJsonObject.data.length;i++){
			orderList[i]=new Object();
			orderList[i].myidx=i;
			orderList[i].pfs_id=  wbEncrytor.cwnEncrypt(myJsonObject.data[i].pfs_id);//pfs_id
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
		//10,12
		var f1housetotal=7;
		var f2housetotal=12;
		if(orderList.length<=f1housetotal){
			fn1();
			showHouseMC(getIdClass(getId(pamc),"F1"),0,orderList.length-1);
		}else{
			fn1();
			showHouseMC(getIdClass(getId(pamc),"F1"),0,f1housetotal-1);
			var fcount=Math.ceil((orderList.length-f1housetotal)/f2housetotal);
			//alert(fcount)
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
		//removeListenerObj(obj);
		var urlstr=orderList[obj.didx].url;
		window.location.href=urlstr;
//		window.open(urlstr);
	}
}

function fn1(){
	var fnobj=getIdClass(getId(pamc),"F1");
	show(fnobj);
	shObjs(fnobj,false,false,false);
	basebgeffect(fnobj,"f1");
}
function fnFromNum(fnum){
	var fnobj=getIdClass(getId(pamc),"F"+fnum);
	if(!fnobj){
		addNewFdiv(fnum);
	}
	fnobj=getIdClass(getId(pamc),"F"+fnum);
	show(fnobj);
	shObjs(fnobj,false,false,false);
	basebgeffect(fnobj,"f"+fnum);
}
function basebgeffect(objf,fid){
	show(getIdClass(objf,"p1"));
	for(var i=1;i<=12;i++){
		var wwobj=getIdClass(objf,"ww"+i);
		if(wwobj){
			show(wwobj);
		}
	}
	if(fid=="f1"){
		beginMoveObj(car1list);
		beginMoveObj(car2list);
		beginMoveObj(car3list);
		beginMoveObj(ren1list);
		beginMoveObj(ren2list,400);
		beginMoveObj(ren3list);
		beginMoveObj(ren4list);
		beginMoveObj(ren5list);
		beginMoveObj(ren6list,500);
		beginMoveObj(ren7list);
	}else{
		beginMoveObj(f2_car1list);
		beginMoveObj(f2_car2list);
		beginMoveObj(f2_car3list);
		beginMoveObj(f2_car4list);
		beginMoveObj(f2_ren1list);
		beginMoveObj(f2_ren2list,400);
		beginMoveObj(f2_ren3list);
		beginMoveObj(f2_ren4list);
		beginMoveObj(f2_ren5list);
		beginMoveObj(f2_ren6list,500);
		beginMoveObj(f2_ren7list);
	}
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
	//obj.className=className;
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
	//obj.className=null;
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
	//var satrtXYList=copyTouchObjXYList(obj.satrtXYList);//obj.satrtXYList=copyTouchObjXYList(obj.moveXYList);function myTriangleLen(a,b){return Math.pow(Math.pow(a,2)+Math.pow(b,2),0.5);}
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