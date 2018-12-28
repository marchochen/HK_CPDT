var pamc2="mycontent2";
var pamc3="mycontent3";
wbEncrytor = new wbEncrytor;
String.prototype.replaceAll = function(s1,s2) { 
    return this.replace(new RegExp(s1,"gm"),s2); 
}
function GetQueryString(name)
{
     var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
     var r = window.location.search.substr(1).match(reg);
     if(r!=null)return  unescape(r[2]); return null;
}
function nodelist_init(){
    beginInit2_f(contextPath+"/app/learningmap/grade/nodelist?id="+ wbEncrytor.cwnDecrypt(GetQueryString("id")));
    stageCentreSetting();
}

//=======================================================================二级页面
function beginInit2_f(urluserstr){
	var nodeList=new Array();
	show(getId(pamc2));
	fn1();
	readnodelistjsondata(urluserstr);
	function readnodelistjsondata(urluserstr){
		//var myJsonObject='{"data":[{"pfs_id":"1","psi_id":"1","pfs_title":"技术发展序列","ugr_display_bil":"初级java工程师","url":"grade/nodedetail?id=1"},{"pfs_id":"1","psi_id":"2","pfs_title":"技术发展序列","ugr_display_bil":"中级java工程师","url":"grade/nodedetail?id=2"},{"pfs_id":"1","psi_id":"3","pfs_title":"技术发展序列","ugr_display_bil":"高级java工程师","url":"grade/nodedetail?id=3"}]}';
		$.get(urluserstr, function(myJsonObject){
			parseJsonData(myJsonObject);
		});
	}
	function parseJsonData(myJsonObject){
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
		hidden(getIdClass(getId(pamc2),"F1"));
		hidden(getIdClass(getId(pamc2),"F2"));
		hidden(getIdClass(getId(pamc2),"F3"));
		hidden(getIdClass(getId(pamc2),"F4"));
		hidden(getIdClass(getId(pamc2),"F5"));
		if(nodeList.length<=4){
			fn1();
			showHouseMC(getIdClass(getId(pamc2),"F1"),0,nodeList.length-1);
		}else if(nodeList.length<=10){
			fn1();
			fn2();
			showHouseMC(getIdClass(getId(pamc2),"F1"),0,3);
			showHouseMC(getIdClass(getId(pamc2),"F2"),4,nodeList.length-1);
		}else if(nodeList.length<=16){
			fn1();
			fn2();
			fn3();
			showHouseMC(getIdClass(getId(pamc2),"F1"),0,3);
			showHouseMC(getIdClass(getId(pamc2),"F2"),4,9);
			showHouseMC(getIdClass(getId(pamc2),"F3"),10,nodeList.length-1);
		}else if(nodeList.length<=22){
			fn1();
			fn2();
			fn3();
			fn4();
			showHouseMC(getIdClass(getId(pamc2),"F1"),0,3);
			showHouseMC(getIdClass(getId(pamc2),"F2"),4,9);
			showHouseMC(getIdClass(getId(pamc2),"F3"),10,15);
			showHouseMC(getIdClass(getId(pamc2),"F4"),16,nodeList.length-1);
		}else if(nodeList.length<=28){
			fn1();
			fn2();
			fn3();
			fn4();
			fn5();
			showHouseMC(getIdClass(getId(pamc2),"F1"),0,3);
			showHouseMC(getIdClass(getId(pamc2),"F2"),4,9);
			showHouseMC(getIdClass(getId(pamc2),"F3"),10,15);
			showHouseMC(getIdClass(getId(pamc2),"F4"),16,21);
			showHouseMC(getIdClass(getId(pamc2),"F5"),22,nodeList.length-1);
		}else if(nodeList.length>=29){
			fn1();
			fn2();
			fn3();
			fn4();
			fn5();
			showHouseMC(getIdClass(getId(pamc2),"F1"),0,3);
			showHouseMC(getIdClass(getId(pamc2),"F2"),4,9);
			showHouseMC(getIdClass(getId(pamc2),"F3"),10,15);
			showHouseMC(getIdClass(getId(pamc2),"F4"),16,21);
			showHouseMC(getIdClass(getId(pamc2),"F5"),22,27);
		}
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
			//removeListenerObj(obj);
			var urlstr=nodeList[obj.didx].url;
			urlstr=urlstr.replaceAll("&amp;","&");   
			beginInit3_f(urlstr,nodeList[obj.didx].pfs_title+" "+nodeList[obj.didx].ugr_display_bil);
			//window.open(urlstr);
		}
	}

	function fn1(){
		var fnobj=getIdClass(getId(pamc2),"F1");
		show(fnobj);
		shObjs(fnobj,false,false,false);
		showwwbg();
		function showwwbg(){
			shObjs(getIdClass(fnobj,"renitem"),true,true,true);
		}
	}
	function fn2(){
		var fnobj=getIdClass(getId(pamc2),"F2");
		show(fnobj);
		shObjs(fnobj,false,false,false);
		showwwbg();
		function showwwbg(){
			shObjs(getIdClass(fnobj,"renitem"),true,true,true);
			shObjs(getIdClass(fnobj,"renitem1"),true,true,true);
			shObjs(getIdClass(fnobj,"renitem2"),true,true,true);
		}
	}
	function fn3(){
		var fnobj=getIdClass(getId(pamc2),"F3");
		show(fnobj);
		shObjs(fnobj,false,false,false);
		showwwbg();
		function showwwbg(){
			shObjs(getIdClass(fnobj,"renitem"),true,true,true);
			shObjs(getIdClass(fnobj,"renitem1"),true,true,true);
			shObjs(getIdClass(fnobj,"renitem2"),true,true,true);
		}
	}
	function fn4(){
		var fnobj=getIdClass(getId(pamc2),"F4");
		show(fnobj);
		shObjs(fnobj,false,false,false);
		showwwbg();
		function showwwbg(){
			shObjs(getIdClass(fnobj,"renitem"),true,true,true);
			shObjs(getIdClass(fnobj,"renitem1"),true,true,true);
			shObjs(getIdClass(fnobj,"renitem2"),true,true,true);
		}
	}
	function fn5(){
		var fnobj=getIdClass(getId(pamc2),"F5");
		show(fnobj);
		shObjs(fnobj,false,false,false);
		showwwbg();
		function showwwbg(){
			shObjs(getIdClass(fnobj,"renitem"),true,true,true);
			shObjs(getIdClass(fnobj,"renitem1"),true,true,true);
			shObjs(getIdClass(fnobj,"renitem2"),true,true,true);
		}
	}
}


//=======================================================================三级页面
function beginInit3_f(urluserstr,bigtitle){
	var courseList=new Array();
	show(getId(pamc3));
	fn1();
	readnodelistjsondata(urluserstr);
	function readnodelistjsondata(urluserstr){
		$.getJSON(urluserstr, function(data){
			parseJsonData(data);
	});
		//var myJsonObject='{"data":[{"itm_id":"1477","itm_canread":"1","itm_desc":"struct2+spring+hibernate1","itm_status_off":"false","itm_title":"三大框架","itd_compulsory_ind":"0","itm_icon":"1.png","itm_publish_timestamp":"2015-09-30 11:01:45","is_user_like":"0","itm_exam_ind":"0","itm_type":"SELFSTUDY"},{"itm_id":"1557","itm_canread":"0","itm_desc":"面向对象思想","itm_status_off":"false","itm_title":"java OOP30","itd_compulsory_ind":"0","itm_icon":"240490-13021410192422.jpg","itm_publish_timestamp":"2015-09-30 11:01:45","is_user_like":"0","itm_exam_ind":"0","itm_type":"SELFSTUDY"},{"itm_id":1707,"itm_canread":"0","itm_desc":"面向对象思想","itm_status_off":"false","itm_title":"三大框架5555","itd_compulsory_ind":"0","itm_icon":"1.png","itm_publish_timestamp":"2015-09-30 11:01:45","is_user_like":0,"itm_exam_ind":0,"itm_type":"SELFSTUDY"}]}';
	}
	function parseJsonData(myJsonObject){
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
			courseList[i].itm_desc=myJsonObject.data[i].itm_desc;//简介 ok
			courseList[i].itm_status_off=myJsonObject.data[i].itm_status_off;
			courseList[i].itm_title=myJsonObject.data[i].itm_title;//标题 ok
			courseList[i].itd_compulsory_ind=myJsonObject.data[i].itd_compulsory_ind;//1:必修,0:选修 ok
			courseList[i].itm_icon=myJsonObject.data[i].itm_icon;//图片 ok
			courseList[i].itm_publish_timestamp=Wzb.displayTime(myJsonObject.data[i].itm_publish_timestamp, Wzb.time_format_ymd);//发布日期 ok
			courseList[i].is_user_like=myJsonObject.data[i].is_user_like;//点赞数 ok
			courseList[i].itm_exam_ind=myJsonObject.data[i].itm_exam_ind;
			courseList[i].itm_type=myJsonObject.data[i].itm_type;//selfstudy:网上课程,classroom:面授课程,intergrated:项目式培训  ok
			if(myJsonObject.data[i].itm_mobile_ind=="yes"){
			courseList[i].tcss="to3td1 wzb-phone-icon";
			}else{
			courseList[i].tcss="to3td1";
			}
			//alert(courseList[i].itm_canread)pfs_title
		}
		hidden(getIdClass(getId(pamc3),"F1"));
		fn1();
		showHouseMC(getIdClass(getId(pamc3),"F1"),0,courseList.length-1);
		stageCentreSetting();
	}
	function showHouseMC(objf,a,b){
		var typestr=fetchLabel('course_type')+"：";
		var datestr=fetchLabel('course_publish_date')+"：";
		var descstr=fetchLabel('course_introduction')+"：";
		var compulsorytxtobj=new Object();
		compulsorytxtobj.compulsory0="";
		compulsorytxtobj.compulsory1="<font class='wzb-bixiu'>"+fetchLabel('required')+"</font>";
		var typetxtobj=new Object();
		typetxtobj.selfstudy=fetchLabel('selfstudy');
		typetxtobj.classroom=fetchLabel('classroom');
		typetxtobj.integrated=fetchLabel('integrated');
		var hidx=0;
		var beginTopNum=28.472;
		var topAddNum=25.8333;//=  186/720*100
		for(var i=a;i<=b;i++){
			hidx++;
			if(hidx>1){
				if(!getIdClass(objf,"house"+hidx)){
					var houseobj=getIdClass(objf,"house1");
					var html=houseobj.innerHTML;
					var div=document.createElement('div');
					div.className="house"+hidx;
					div.style.left="23.984%";
					div.style.width="52.188%";
					div.style.height="29.722%";
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
			var tdlist=getIdClass(objf,"house"+hidx).getElementsByTagName("td");
			tdlist[0].innerHTML="<font class='"+courseList[i].tcss+"'>"+courseList[i].itm_title+"</font>";
			tdlist[1].innerHTML="<font class='to3td1' style='color:#637D00;'>"+typestr+"</font>"+typetxtobj[courseList[i].itm_type.toLowerCase()];
			tdlist[2].innerHTML="<font class='to3td1' style='color:#637D00;'>"+datestr+"</font>"+courseList[i].itm_publish_timestamp;
			tdlist[3].innerHTML="<font class='to3td1' style='color:#637D00;'>"+descstr+"</font>"+replaceVlaue(courseList[i].itm_desc);
			tdlist[4].innerHTML=compulsorytxtobj["compulsory"+courseList[i].itd_compulsory_ind];
			var imglist=getIdClass(objf,"house"+hidx).getElementsByTagName("img");
			imglist[1].src=courseList[i].itm_icon;
			if(courseList[i].itm_canread=="1"){
				addListenerObj(houseobj,"",housebtnClick);
			}else{
				show(getIdClass(houseobj,"house_notcanread"));
				hidden(getIdClass(houseobj,"house_t4"));
				removeListenerObj(houseobj);
			}
		}
		getIdClass(objf,"cbg_txt").getElementsByTagName("td")[0].innerHTML=bigtitle+"("+courseList.length+")";
		var houseobjlast=getIdClass(objf,"house"+hidx);
		if(houseobjlast){
			//var bg=getIdClass(objf,"bg");
			//bg.style.height=(                getNumValue(houseobjlast.style.top)+getNumValue(houseobjlast.style.height)                )+"%";

			var cbg_d=getIdClass(objf,"cbg_d");
			var tmpcbgtop=getNumValue(houseobjlast.style.top)+getNumValue(houseobjlast.style.height)-getNumValue(cbg_d.style.height);
			if(tmpcbgtop<13){
				tmpcbgtop=13;
			}
			cbg_d.style.top=(              tmpcbgtop                  )+"%";
		
		
			var cbg_c=getIdClass(objf,"cbg_c");
			var tmpcheight=getNumValue(cbg_d.style.top)-getNumValue(cbg_c.style.top);
			if(tmpcheight<0){
				tmpcheight=0;
			}
			cbg_c.style.height=tmpcheight+"%";
		}
		function housebtnClick(e){
			var obj=e.currentTarget;
			//removeListenerObj(obj);
			var urlstr="/app/course/detail/"+ wbEncrytor.cwnEncrypt(courseList[obj.didx].itm_id);
			window.location.href=urlstr;
		}
	}
	function fn1(){
		var fnobj=getIdClass(getId(pamc3),"F1");
		show(fnobj);
		shObjs(fnobj,false,false,false);
		showwwbg();
		function showwwbg(){
			//show(getIdClass(fnobj,"bg"));
			show(getIdClass(fnobj,"cbg_txt"));
			getIdClass(fnobj,"cbg_txt").getElementsByTagName("td")[0].innerHTML=bigtitle;
			
			show(getIdClass(fnobj,"cbg_t"));
			show(getIdClass(fnobj,"cbg_c"));
			show(getIdClass(fnobj,"cbg_d"));
			
			var cbg_closebtn=getIdClass(fnobj,"cbg_closebtn");
			show(cbg_closebtn);
			addListenerObj(cbg_closebtn,"",closebtnClick);
			function closebtnClick(){
				removeListenerObj(cbg_closebtn);
				hidden(getId(pamc3));
			}
		}
	}
}


//////////////////////////////////////////////////////////////////////////////通用function
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