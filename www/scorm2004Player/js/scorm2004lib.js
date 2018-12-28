var dot1="_XvYvX_";
var dot2="_XyXyX_";
var dot3="_XyOyX_";
var _view_count="";
var _lastest_date="";
var _course_data1="";
var _error_code="0";
var notSubmit=true;
//
var _cmi_mode="review";
var _cmi_learner_id="dummy";
var _cmi_course_id="x001";
var _cmi_version="2004";
var _cmi_learner_name="DUMMY";
var _cmi_completion_status="not attempted";
var _cmi_success_status="unknown";
var _cmi_credit="no-credit";
var _cmi_launch_data="";
var _cmi_total_time="";
var _cmi_session_time="";
var _cmi_time_limit_action="continue,no message";
var _cmi_score_children="scaled,raw,min,max";
var _cmi_score_raw=0;
var _cmi_score_max=100;
var _cmi_score_min=0;
var _cmi_scaled_passing_score=60; 
var _cmi_location="";
var _cmi_suspend_data="";
var _cmi_objectives_children="id,score,success_status,completion_status,progress_measure,description";
var _cmi_objectives_count=0;
var _cmi_objectives_id_Arr=new Array();
var _cmi_objectives_score_children_Arr=new Array();
var _cmi_objectives_score_scaled_Arr=new Array();
var _cmi_objectives_score_raw_Arr=new Array();
var _cmi_objectives_score_max_Arr=new Array();
var _cmi_objectives_score_min_Arr=new Array();
var _cmi_objectives_success_status_Arr=new Array();
var _cmi_objectives_completion_status_Arr=new Array();
var _cmi_objectives_progress_measure_Arr=new Array();
var _cmi_objectives_description_Arr=new Array();
var _cmi_interactions_id_Arr=new Array();
var _cmi_interactions_type_Arr=new Array();
var _cmi_interactions_objectives_count_Arr=new Array();
var _cmi_interactions_timestamp_Arr=new Array();
var _cmi_interactions_result_Arr=new Array();
var _cmi_interactions_description_Arr=new Array();
//
var API_1484_11= new SCORMapi();
function SCORMapi(){
     //
}
//-----ERROR----------------------------------------------------------------
SCORMapi.prototype.GetLastError = function (){
	return _error_code;
}
SCORMapi.prototype.GetErrorString = function (wizErrorString){
   var error_string="No error";
   switch (wizErrorString){
        case "0" :
	   error_string="No error";
         break;
        case "101" :
	   error_string="General exception";
         break;
	case "201" :
	   error_string="Invalid argument error";
         break;
	case "202" :
	   error_string="Element cannot have chlidren";
         break;
	case "203" :
	   error_string="Element not an array - cannot have count";
         break;
	case "301" :
	   error_string="Not initialized";
         break;
	case "401" :
	   error_string="Not implemented error";
         break;
	case "402" :
	   error_string="Invalid set value, element is a keyword";
         break;
	case "403" :
	   error_string="Element is read only";
         break;
	case "404" :
	   error_string="Element is wirte only";
         break;
	case "405" :
	   error_string="Incorrect Data Type";
         break; 
        default :
         break;
   }
   return error_string;
}
SCORMapi.prototype.GetDiagnostic = function (wizErrorString){
   var error_string="No error";
   switch (wizErrorString){
        case "0" :
	   error_string="No error";
         break;
        case "101" :
	   error_string="General exception";
         break;
	case "201" :
	   error_string="Invalid argument error";
         break;
	case "202" :
	   error_string="Element cannot have chlidren";
         break;
	case "203" :
	   error_string="Element not an array - cannot have count";
         break;
	case "301" :
	   error_string="Not initialized";
         break;
	case "401" :
	   error_string="Not implemented error";
         break;
	case "402" :
	   error_string="Invalid set value, element is a keyword";
         break;
	case "403" :
	   error_string="Element is read only";
         break;
	case "404" :
	   error_string="Element is wirte only";
         break;
	case "405" :
	   error_string="Incorrect Data Type";
         break; 
        default :
         break;
   }
   return error_string;
}
//-----GET------------------------------------------------------------------
SCORMapi.prototype.GetValue = function (GetV){
	var returnV="";
	_error_code="0";
	var getV_arr=GetV.split(".");
	if(getV_arr[0].toLowerCase()=="cmi"){
	switch (getV_arr[1].toLowerCase()){
          case "_version" :
	 returnV=_cmi_version;
          break;
          case "completion_status" :
	 returnV=_cmi_completion_status;
          break;    
	 case "completion_threshold" :
	 returnV="1";
          break;
	 case "credit" :
	 returnV=_cmi_credit;
          break;  
	 case "entry" :
	 returnV="";
          break;
	 case "launch_data" :
	 returnV=_cmi_launch_data;
          break;
	 case "learner_id" :
	 returnV=_cmi_learner_id;
          break;
	 case "learner_name" :
	 returnV=_cmi_learner_name;
          break;
	 case "location" :
	 returnV=_cmi_location;
          break;
	 case "mode" :
	 returnV=_cmi_mode;
          break;
	 case "success_status" :
	 returnV=_cmi_success_status;
          break;
	 case "suspend_data" :
	 returnV=_cmi_suspend_data;
          break;
	 case "time_limit_action" :
	 returnV=_cmi_time_limit_action;
          break;
	 case "total_time" :
	 returnV=_cmi_total_time;
          break;
	 case "scaled_passing_score" :
	 returnV=_cmi_scaled_passing_score;
          break;                              
	 case "score" :
	 if(getV_arr[2]!=""&&getV_arr[2]!=null&&typeof(getV_arr[2])!="undefined"){
	    returnV=get_SCORM_score(getV_arr[2].toLowerCase());
	 }else{
	 _error_code="201";
	 }
          break;
	 case "objectives" :
	 if(getV_arr[2]!=""&&getV_arr[2]!=null&&typeof(getV_arr[2])!="undefined"){
	 if(_cmi_objectives_id_Arr[parseInt(getV_arr[2])]==null||typeof(_cmi_objectives_id_Arr[parseInt(getV_arr[2])])=="undefined"||_cmi_objectives_id_Arr[parseInt(getV_arr[2])]==""){
	_cmi_objectives_id_Arr.push(getV_arr[2]);
	_cmi_objectives_score_children_Arr.push("scaled,raw,min,max");
	_cmi_objectives_score_scaled_Arr.push("0");
	_cmi_objectives_score_raw_Arr.push("0");
	_cmi_objectives_score_max_Arr.push("100");
	_cmi_objectives_score_min_Arr.push("0");
	_cmi_objectives_success_status_Arr.push("unknown");
	_cmi_objectives_completion_status_Arr.push("not attempted");
	_cmi_objectives_progress_measure_Arr.push("0");
	_cmi_objectives_description_Arr.push("");
	  }
	 if(getV_arr.length==3){
	 if(getV_arr[2].toLowerCase()=="_children"){
	 returnV=_cmi_objectives_children;
	 }else if(getV_arr[2].toLowerCase()=="_count"){
	 returnV=_cmi_objectives_count;
	 }else{
	 //
	 }
	 }else if(getV_arr.length==4){
	 returnV=get_SCORM_objectives_status(getV_arr[2].toLowerCase(),getV_arr[3].toLowerCase());
	 }else if(getV_arr.length==5){
	 returnV=get_SCORM_objectives_score(getV_arr[2].toLowerCase(),getV_arr[3].toLowerCase()+"."+getV_arr[4].toLowerCase());
	 }else{
	 //
	 }
	 }else{
	 _error_code="201";
	 }
          break;
	 case "interactions" :
	 if(getV_arr[2]!=""&&getV_arr[2]!=null&&typeof(getV_arr[2])!="undefined"){
	     if(getV_arr.length==4){
	 returnV=get_SCORM_interactions_status(getV_arr[2].toLowerCase(),getV_arr[3].toLowerCase());
	 }else if(getV_arr.length==5){
	 if(getV_arr[3].toLowerCase()+"."+getV_arr[4].toLowerCase()=="objectives._count"){
	     returnV=_cmi_interactions_objectives_count_Arr[Number(getV_arr[2])];
	 }
	 }else{
	 //
	 }
	 }else{
	 _error_code="201";
	 }
          break;  
          default :
              returnV="";
          break;
	}
	}else if(getV_arr[0].toLowerCase()=="adl"){
	//
	}else{
	//
	}
	//
	window.parent.setDebugInfo("Read "+GetV+" , Return "+returnV);
	//
	return returnV;
}
//-----SET------------------------------------------------------------------
SCORMapi.prototype.SetValue = function (SetV,SetArgs){
	var returnV="false";
	_error_code="0";
	//alert("Set- "+SetV+" -as-"+SetArgs);
	var setV_arr=SetV.split(".");
	if(setV_arr[0].toLowerCase()=="cmi"){
	switch (setV_arr[1].toLowerCase()){
          case "completion_status" :
	 _cmi_completion_status=SetArgs;
	 returnV="true";
          break;
	 case "exit" :
	 returnV="true";
          break;
	 case "location" :
	 _cmi_location=SetArgs;
	 returnV="true";
          break;
	 case "session_time" :
	 _cmi_session_time=SetArgs;
	 returnV="true";
          break;
	 case "success_status" :
	 _cmi_success_status=SetArgs;
	 returnV="true";
          break;
	 case "suspend_data" :
	 _cmi_suspend_data=SetArgs;
	 returnV="true";
          break;
	 case "score" :
	 if(setV_arr[2]!=""&&setV_arr[2]!=null&&typeof(setV_arr[2])!="undefined"){
	    returnV=set_SCORM_score(setV_arr[2].toLowerCase(),SetArgs);
	 }else{
	 _error_code="201";
	 }
          break;
	 case "objectives" :
	 if(setV_arr[2]!=""&&setV_arr[2]!=null&&typeof(setV_arr[2])!="undefined"){
	 if(setV_arr.length==4){
	 returnV=set_SCORM_objectives_status(setV_arr[2].toLowerCase(),setV_arr[3].toLowerCase(),SetArgs);
	 }else if(setV_arr.length==5){
	 returnV=set_SCORM_objectives_score(setV_arr[2].toLowerCase(),setV_arr[3].toLowerCase()+"."+setV_arr[4].toLowerCase(),SetArgs);
	 }else{
	 //
	 }
	 }else{
	 _error_code="201";
	 }
          break;
	 case "interactions" :
	 if(setV_arr[2]!=""&&setV_arr[2]!=null&&typeof(setV_arr[2])!="undefined"){
	    if(setV_arr.length==4){
	 returnV=set_SCORM_interactions_status(setV_arr[2].toLowerCase(),setV_arr[3].toLowerCase(),SetArgs);
	 }else{
	 //
	 }
	 }else{
	 _error_code="201";
	 }
          break;                        
	 default :
              returnV="";
          break;
	}
	joinStr();
	}else if(setV_arr[0].toLowerCase()=="adl"){
	//
	}else{
	//
	}
	window.parent.setDebugInfo("Write "+SetV+" as "+SetArgs+" , Return "+returnV);
	return returnV;
}
//-------Initialize---------------------------------------------------------
SCORMapi.prototype.Initialize = function (InitStr){
	//alert("Initialize");
   beTerminate=false;
   _error_code="0";
   window.parent.setDebugInfo("SCO Initialize.");
   return "true";
}
//-------Commit---------------------------------------------------------
SCORMapi.prototype.Commit = function (InitStr){
   _error_code="0";
   //alert("Commit");
 //  beTerminate=true;
   joinStr();
   window.parent.setDebugInfo("SCO Commit.");
   //submitSCOInfo();
   return "true";
}
//-------Terminate-----------------------------------------------------------
SCORMapi.prototype.Terminate = function (InitStr){
   _error_code="0";
  // beTerminate=true;
   joinStr();
   window.parent.setDebugInfo("SCO Terminate.");
   //submitSCOInfo();
   return "true";
}
//
function string2CMI(_str){
    var _tmpArr=_str.split(dot1);
	_cmi_learner_name=_tmpArr[0];
	_cmi_success_status=_tmpArr[1];
	window.parent.setDebugInfo("SCO success_status: "+_cmi_success_status);
	_cmi_completion_status=_tmpArr[2];
	window.parent.setDebugInfo("SCO completion_status: "+_cmi_completion_status);
	_cmi_credit=_tmpArr[3];
	_cmi_launch_data=_tmpArr[4];
	_view_count=_tmpArr[5];
	_lastest_date=_tmpArr[6];
	window.parent.setDebugInfo("SCO lastest_date: "+_lastest_date);
	_cmi_total_time=s2time(_tmpArr[7]);
	_cmi_time_limit_action=_tmpArr[8];
	_cmi_score_raw=Number(_tmpArr[9]);
	window.parent.setDebugInfo("SCO score raw: "+_cmi_score_raw);
	_cmi_score_max=Number(_tmpArr[10]);
	_cmi_score_min=Number(_tmpArr[11]);
	_cmi_scaled_passing_score=Number(_tmpArr[12]);
	_cmi_location=_tmpArr[13];
	_cmi_suspend_data=_tmpArr[14];
	//
	var _tmpArr1=_tmpArr[15].split(dot2);
	if(_tmpArr[15].indexOf(dot2)>=0){
	_cmi_objectives_count=_tmpArr1.length;
	for(var n=0;n<_cmi_objectives_count;n++){
	var _tmpArrArr1=_tmpArr1[n].split(dot3);
	_cmi_objectives_id_Arr.push(_tmpArrArr1[0]);
	_cmi_objectives_score_children_Arr.push("scaled,raw,min,max");
	_cmi_objectives_score_scaled_Arr.push("0");
	_cmi_objectives_score_raw_Arr.push(_tmpArrArr1[3]);
	_cmi_objectives_score_max_Arr.push(_tmpArrArr1[4]);
	_cmi_objectives_score_min_Arr.push(_tmpArrArr1[5]);
	_cmi_objectives_success_status_Arr.push(_tmpArrArr1[6]);
	_cmi_objectives_completion_status_Arr.push(_tmpArrArr1[7]);
	_cmi_objectives_progress_measure_Arr.push(_tmpArrArr1[8]);
	_cmi_objectives_description_Arr.push(_tmpArrArr1[9]);
	}
	}
	//
	var _tmpArr2=_tmpArr[16].split(dot2);
	if(_tmpArr[16].indexOf(dot2)>=0){
	for(var t=0;t<_tmpArr2.length;t++){
	var _tmpArrArr2=_tmpArr2[t].split(dot3);
	_cmi_interactions_id_Arr.push(_tmpArrArr2[0]);
	_cmi_interactions_type_Arr.push("choice");
	_cmi_interactions_objectives_count_Arr.push(_cmi_objectives_count);
	_cmi_interactions_timestamp_Arr.push(_tmpArrArr2[3]);
	_cmi_interactions_result_Arr.push(_tmpArrArr2[4]);
	}
	}
	//
	_course_data1=_tmpArr[17];
}
//
function get_SCORM_score(_str){
   var returnV="";
   switch (_str){
        case "_children" :
	   returnV=_cmi_score_children;
         break;
	case "raw" :
	   returnV=_cmi_score_raw;
         break;
	case "max" :
	   returnV=_cmi_score_max;
         break;
	case "min" :
	   returnV=_cmi_score_min;
         break; 
        default :
         break;
   }
   return returnV;
}
//
function set_SCORM_score(_str,_setv){
   var returnV="false";
   switch (_str){
	case "raw" :
	   _cmi_score_raw=_setv;
	returnV="true";
         break;
	case "max" :
	   _cmi_score_max=_setv;
	returnV="true";
         break;
	case "min" :
	   _cmi_score_min=_setv;
	returnV="true";
         break; 
        default :
         break;
   }
   return returnV;
}
//
function get_SCORM_objectives_status(num,_str){
   var returnV="";
   switch (_str){
        case "id" :
	   returnV=get_id2n(_cmi_objectives_id_Arr,num,_cmi_objectives_id_Arr);
         break;
	case "success_status" :
	   returnV=get_id2n(_cmi_objectives_id_Arr,num,_cmi_objectives_success_status_Arr);
         break;
	case "completion_status" :
	   returnV=get_id2n(_cmi_objectives_id_Arr,num,_cmi_objectives_completion_status_Arr);
         break;
	case "progress_measure" :
	   returnV=get_id2n(_cmi_objectives_id_Arr,num,_cmi_objectives_progress_measure_Arr);
         break;
	case "description" :
	   returnV=get_id2n(_cmi_objectives_id_Arr,num,_cmi_objectives_description_Arr);
         break; 
        default :
         break;
   }
   return returnV;
}
//
function set_SCORM_objectives_status(num,_str,_setv){
   var returnV="false";
   switch (_str){
	  case "id" :
	set_id2n(_cmi_objectives_id_Arr,num,_cmi_objectives_id_Arr,_setv);
	returnV="true";
	break;
	case "success_status" :
	set_id2n(_cmi_objectives_id_Arr,num,_cmi_objectives_success_status_Arr,_setv);
	returnV="true";
	break;
	case "completion_status" :
	set_id2n(_cmi_objectives_id_Arr,num,_cmi_objectives_completion_status_Arr,_setv);
	returnV="true";
	break;
	case "progress_measure" :
	set_id2n(_cmi_objectives_id_Arr,num,_cmi_objectives_progress_measure_Arr,_setv);
	returnV="true";
	break;
	case "description" :
	set_id2n(_cmi_objectives_id_Arr,num,_cmi_objectives_description_Arr,_setv);
	returnV="true";
	break; 
	default :
	break;
   }
   return returnV;
}
//
function get_SCORM_objectives_score(num,_str){
   var returnV="";
   switch (_str){
        case "score._children" :
	   returnV=get_id2n(_cmi_objectives_id_Arr,num,_cmi_objectives_score_children_Arr);
         break;
	case "score.scaled" :
	   returnV=get_id2n(_cmi_objectives_id_Arr,num,_cmi_objectives_score_scaled_Arr);
         break;
	case "score.raw" :
	   returnV=get_id2n(_cmi_objectives_id_Arr,num,_cmi_objectives_score_raw_Arr);
         break;
	case "score.max" :
	   returnV=get_id2n(_cmi_objectives_id_Arr,num,_cmi_objectives_score_max_Arr);
         break; 
	case "score.min" :
	   returnV=get_id2n(_cmi_objectives_id_Arr,num,_cmi_objectives_score_min_Arr);
         break; 
        default :
         break;
   }
   return returnV;
}
//
function set_SCORM_objectives_score(num,_str,_setv){
   var returnV="false";
   switch (_str){
	case "score.scaled" :
	set_id2n(_cmi_objectives_id_Arr,num,_cmi_objectives_score_scaled_Arr,_setv);
	returnV="true";
         break;
	case "score.raw" :
	set_id2n(_cmi_objectives_id_Arr,num,_cmi_objectives_score_raw_Arr,_setv);
	returnV="true";
         break;
	case "score.max" :
	set_id2n(_cmi_objectives_id_Arr,num,_cmi_objectives_score_max_Arr,_setv);
	returnV="true";
         break; 
	case "score.min" :
	set_id2n(_cmi_objectives_id_Arr,num,_cmi_objectives_score_min_Arr,_setv);
	returnV="true";
         break; 
        default :
         break;
   }
   return returnV;
}
//
function get_SCORM_interactions_status(num,_str){
   var returnV="";
   switch (_str){
        case "id" :
	   returnV=get_id2n(_cmi_interactions_id_Arr,num,_cmi_interactions_id_Arr);
         break;
	case "type" :
	   returnV=get_id2n(_cmi_interactions_id_Arr,num,_cmi_interactions_type_Arr);
         break;
	case "timestamp" :
	   returnV=get_id2n(_cmi_interactions_id_Arr,num,_cmi_interactions_timestamp_Arr);
         break;
	case "result" :
	   returnV=get_id2n(_cmi_interactions_id_Arr,num,_cmi_interactions_result_Arr);
         break; 
        default :
         break;
   }
   return returnV;
}
//
function set_SCORM_interactions_status(num,_str,_setv){
   var returnV="false";
   switch (_str){
        case "id" :
	   set_id2n(_cmi_interactions_id_Arr,num,_cmi_interactions_id_Arr,_setv);
	returnV="true";
         break;
	case "type" :
	   set_id2n(_cmi_interactions_id_Arr,num,_cmi_interactions_type_Arr,_setv);
	returnV="true";
         break;
	case "timestamp" :
	   set_id2n(_cmi_interactions_id_Arr,num,_cmi_interactions_timestamp_Arr,_setv);
	returnV="true";
         break;
	case "result" :
	set_id2n(_cmi_interactions_id_Arr,num,_cmi_interactions_result_Arr,_setv);
	returnV="true";
         break; 
        default :
         break;
   }
   return returnV;
}
//
function get_id2n(fromArr,n,toArr){
	var returnV="";
	for(var o=0;o<fromArr.length;o++){
	if(fromArr[o]==n){
	if(toArr[o]!=null&&typeof(toArr[o])!="undefined"){
	  returnV=toArr[o];
	}
	break;
	}
	}
	return returnV;
}
//
function set_id2n(fromArr,n,toArr,setV){
	toArr[parseInt(n)]=setV;
	/*
	for(var o=0;o<fromArr.length;o++){
	if(fromArr[o]==n){
	if(toArr[o]!=null&&typeof(toArr[o])!="undefined"){
	  toArr[o]=setV;
	}
	break;
	}
	}
	*/
}
//
function time2s(time){
	if(time!=""){
	  var timeCount=0;
	  if(time.substring(0,2)=="PT"){
		 var _time=time.substring(2);
		 if(_time.indexOf("H")>=0){
			 var _timeArr1=_time.split("H");  
			 timeCount+=parseInt(_timeArr1[0])*3600;
		     var _timeArr2=_timeArr1[1].split("M");
			 timeCount+=parseInt(_timeArr2[0])*60;
		 	 timeCount+=parseInt(_timeArr2[1].split("S")[0]);
		 }else{
			 if(_time.indexOf("M")>=0){
				 var _timeArr3=_time.split("M");
				 timeCount+=parseInt(_timeArr3[0])*60;
				 timeCount+=parseInt(_timeArr3[1].split("S")[0]);
			 }else{
				 timeCount=parseInt(_time.split("S")[0]);
			 }
	 	}   
	  }else{
	  var timeArr=time.split(":");
	  
	  if(timeArr[0]!="00"){
	  timeCount=Number(timeArr[0])*3600;
	  }
	  if(timeArr[1]!="00"){
	  timeCount+=Number(timeArr[1])*60;
	  }
	  if(timeArr[2]!="00"){
	  timeCount+=parseInt(timeArr[2])+1;
	  }
	  }
	  return timeCount;
	}else{
	  return 0;
	}
}
//
function s2time(ts){
   var sec = (ts % 60);
   ts -= sec;
   var tmp = (ts % 3600);
   ts -= tmp; 
   sec = Math.round(sec*100)/100;   
   var strSec = new String(sec);
   var strWholeSec = strSec;
   var strFractionSec = "";
   if (strSec.indexOf(".") != -1){
      strWholeSec =  strSec.substring(0, strSec.indexOf("."));
      strFractionSec = strSec.substring(strSec.indexOf(".")+1, strSec.length);
   }
   if (strWholeSec.length < 2){
      strWholeSec = "0" + strWholeSec;
   }
   strSec = strWholeSec;   
   if (strFractionSec.length){
      strSec = strSec+ "." + strFractionSec;
   }
   if ((ts % 3600) != 0 )
      var hour = 0;
   else var hour = (ts / 3600);
   if ( (tmp % 60) != 0 )
      var min = 0;
   else var min = (tmp / 60);

   if ((new String(hour)).length < 2)
      hour = "0"+hour;
   if ((new String(min)).length < 2)
      min = "0"+min;

   var rtnVal = hour+":"+min+":"+strSec;
   return rtnVal;
}
//
function computeTime(){
   var elapsedSeconds =0; 
   if ( startDate != 0 ){
      var currentDate = new Date().getTime();
      elapsedSeconds = ( (currentDate - startDate) / 1000 );
   }
   return elapsedSeconds;
}
//
function joinStr(){
	var _tmpStr=_cmi_completion_status;
	if(_cmi_completion_status.toLowerCase().substring(0,1)=="c"){
	window.parent.setNodeTree(_isco);
	}
	_tmpStr+=dot1+_cmi_location;
	_tmpStr+=dot1+_cmi_score_raw;
	_tmpStr+=dot1+_cmi_score_max;
	_tmpStr+=dot1+_cmi_score_min;
	if(_cmi_session_time!=""){
		var time = Number(_cmi_session_time);
		if(time){
			
		}else{
			time = _cmi_session_time;
		}

	  _tmpStr+=dot1+time2s(time);
	}else{
	_tmpStr+=dot1+computeTime();
	}
	_tmpStr+=dot1+_cmi_success_status;
	_tmpStr+=dot1+encodeURIComponent(_cmi_suspend_data);
	var _tmpStr1="";
	for(var p=0;p<_cmi_objectives_id_Arr.length;p++){
	var _tmpStr2=_cmi_objectives_id_Arr[p];
	_tmpStr2+=dot3+_cmi_objectives_score_children_Arr[p];
	_tmpStr2+=dot3+_cmi_objectives_score_scaled_Arr[p];
	_tmpStr2+=dot3+_cmi_objectives_score_raw_Arr[p];
	_tmpStr2+=dot3+_cmi_objectives_score_max_Arr[p];
	_tmpStr2+=dot3+_cmi_objectives_score_min_Arr[p];
	_tmpStr2+=dot3+_cmi_objectives_success_status_Arr[p];
	_tmpStr2+=dot3+_cmi_objectives_completion_status_Arr[p];
	_tmpStr2+=dot3+_cmi_objectives_progress_measure_Arr[p];
	_tmpStr2+=dot3+_cmi_objectives_description_Arr[p];
	_tmpStr1+=dot2+_tmpStr2;
	}
	if(_cmi_objectives_id_Arr.length>0){
	   _tmpStr1=_tmpStr1.substring(dot2.length);
	}
	_tmpStr+=dot1+_tmpStr1;
	_tmpStr1="";
	for(var q=0;q<_cmi_interactions_id_Arr.length;q++){
	var _tmpStr3=_cmi_interactions_id_Arr[q];
	_tmpStr3+=dot3+_cmi_interactions_type_Arr[q];
	_tmpStr3+=dot3+_cmi_interactions_objectives_count_Arr[q];
	_tmpStr3+=dot3+_cmi_interactions_timestamp_Arr[q];
	_tmpStr3+=dot3+_cmi_interactions_result_Arr[q];
	_tmpStr1+=dot2+_tmpStr3;
	}
	if(_cmi_interactions_id_Arr.length>0){
	   _tmpStr1=_tmpStr1.substring(dot2.length);
    }
	_tmpStr+=dot1+_tmpStr1;
	//
	_tmpStr+=dot1+_course_data1;
	if(window.parent.adapterDOM!=null&&typeof(window.parent.adapterDOM)!="undefined"){
		window.parent.adapterDOM.postStr="cid="+_cmi_course_id+"&uid="+_cmi_learner_id+"&iid="+_scoid+"&op=w&args="+_tmpStr;
		window.parent.setDebugInfo("SCO write to Opener: "+"cid="+_cmi_course_id+"&uid="+_cmi_learner_id+"&iid="+_scoid+"&op=w&args="+_tmpStr);
	}
}
//
function submitSCOInfo(){
	if(_cmi_mode=="normal"){
	if(window.parent.adapterDOM!=null&&typeof(window.parent.adapterDOM)!="undefined"&&notSubmit){
	  window.parent.setDebugInfo("SCO submit record to LMS. ");
	     window.parent.adapterDOM.writeCourseInfo();
	 notSubmit=false;
	}
	}
}
//
function fromOpener(){
	_cmi_session_time="00:00:00.000";
	notSubmit=true;
	beTerminate=false;
}
//
function unLoadSCO(){
	window.parent.setDebugInfo("unLoadSCO, and  beTerminate is "+beTerminate);
	if(beTerminate==false){
		joinStr();
		submitSCOInfo();
	}
}
//