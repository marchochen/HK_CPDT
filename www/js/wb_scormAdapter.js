var wiz_cmi_core_children="";
var wiz_cmi_core_student_id="";
var wiz_cmi_core_student_name="Dummy,Dummy";
var wiz_cmi_core_lesson_location="";
var wiz_cmi_core_credit="";
var wiz_cmi_core_lesson_status="not attempted";
var wiz_cmi_core_entry="";
var wiz_cmi_core_score_children="";
var wiz_cmi_core_score_raw="0";
var wiz_cmi_core_score_max="0";
var wiz_cmi_core_score_min="0"
var wiz_cmi_core_date=new Date();
var wiz_cmi_core_total_time="";
var wiz_cmi_core_lesson_mode="normal";
var wiz_cmi_core_exit="";
var wiz_cmi_core_session_time="";
//
var wiz_cmi_suspend_data="";
var wiz_cmi_launch_data="";
var wiz_cmi_comments="";
var wiz_cmi_comments_from_lms="";
//
var wiz_cmi_objectives_children="";
var wiz_cmi_objectives_count="";
var wiz_cmi_objectives_n_id="";
var wiz_cmi_objectives_n_score_children="";
var wiz_cmi_objectives_n_score_raw="";
var wiz_cmi_objectives_n_score_max="";
var wiz_cmi_objectives_n_score_min="";
var wiz_cmi_objectives_n_status="";
//
var wiz_cmi_student_data_children="";
var wiz_cmi_student_data_mastery_score="";
var wiz_cmi_student_data_max_time_allowed="";
var wiz_cmi_student_data_time_limit_action="";
var wiz_cmi_student_preference_children="";
var wiz_cmi_student_preference_audio="";
var wiz_cmi_student_preference_language="";
var wiz_cmi_student_preference_speed="";
var wiz_cmi_student_preference_text="";
//
var wiz_cmi_interactions_children="";
var wiz_cmi_interactions_count="";
var wiz_cmi_interactions_time=0;
var wiz_cmi_interactions_n_id="";
var wiz_cmi_interactions_n_objectives_count="";
var wiz_cmi_interactions_n_objectives_n_id="";
var wiz_cmi_interactions_n_time="";
var wiz_cmi_interactions_n_type="";
var wiz_cmi_interactions_n_correct_responses_count="";
var wiz_cmi_interactions_n_correct_responses_n_pattern="";
var wiz_cmi_interactions_n_weighting="";
var wiz_cmi_interactions_n_student_response="";
var wiz_cmi_interactions_n_result="";
var wiz_cmi_interactions_n_latency="";
//
var _wiz_error_code="0";
var _wiz_error_decrp="";
var _wiz_scormAdapter_ver="";
var _wiz_aicc_data="";
var _wiz_student_data="";
var _wiz_attempt_number="";
var _wiz_familiar_name="";
var _wiz_student_demographics="";
//
var API= new SCORMapi();
function SCORMapi(){
     //
}
//-----ERROR----------------------------------------------------------------
SCORMapi.prototype.LMSGetLastError = function (){
	return _wiz_error_code;
}
SCORMapi.prototype.LMSGetErrorString = function (wizErrorString){
   var wiz_error_string="No error";
   switch (wizErrorString){
        case "0" :
		    wiz_error_string="No error";
        	break;
        case "101" :
		    wiz_error_string="General exception";
        	break;
		case "201" :
		    wiz_error_string="Invalid argument error";
        	break;
		case "202" :
		    wiz_error_string="Element cannot have chlidren";
        	break;
		case "203" :
		    wiz_error_string="Element not an array - cannot have count";
        	break;
		case "301" :
		    wiz_error_string="Not initialized";
        	break;
		case "401" :
		    wiz_error_string="Not implemented error";
        	break;
		case "402" :
		    wiz_error_string="Invalid set value, element is a keyword";
        	break;
		case "403" :
		    wiz_error_string="Element is read only";
        	break;
		case "404" :
		    wiz_error_string="Element is wirte only";
        	break;
		case "405" :
		    wiz_error_string="Incorrect Data Type";
        	break;	
        default :
        	break;
   }
   return wiz_error_string;
}
SCORMapi.prototype.LMSGetDiagnostic = function (wizErrorString){
   var wiz_error_string="No error";
   switch (wizErrorString){
        case "0" :
		    wiz_error_string="No error";
        	break;
        case "101" :
		    wiz_error_string="General exception";
        	break;
		case "201" :
		    wiz_error_string="Invalid argument error";
        	break;
		case "202" :
		    wiz_error_string="Element cannot have chlidren";
        	break;
		case "203" :
		    wiz_error_string="Element not an array - cannot have count";
        	break;
		case "301" :
		    wiz_error_string="Not initialized";
        	break;
		case "401" :
		    wiz_error_string="Not implemented error";
        	break;
		case "402" :
		    wiz_error_string="Invalid set value, element is a keyword";
        	break;
		case "403" :
		    wiz_error_string="Element is read only";
        	break;
		case "404" :
		    wiz_error_string="Element is wirte only";
        	break;
		case "405" :
		    wiz_error_string="Incorrect Data Type";
        	break;	
        default :
        	break;
   }
   return wiz_error_string;
}
//-------GET-------------------------------------------------------------------
function get_SCORM_core(coreStr){
   var core_string="";
   switch (coreStr){
        case "_children" :
		    core_string=wiz_cmi_core_children;
        	break;
		case "student_id" :
		    core_string=wiz_cmi_core_student_id;
        	break;	
		case "student_name" :
		    core_string=wiz_cmi_core_student_name;
        	break;
		case "lesson_location" :
		    core_string=wiz_cmi_core_lesson_location;
        	break;
		case "credit" :
		    core_string=wiz_cmi_core_credit;
        	break;	
		case "lesson_status" :
		    core_string=wiz_cmi_core_lesson_status;
        	break;
		case "entry" :
		    if(wiz_cmi_core_lesson_status=="not attempted"){
				wiz_cmi_core_entry="ab-initio";
		        core_string=wiz_cmi_core_entry;
            }else{
				wiz_cmi_core_entry="resume";
		        core_string=wiz_cmi_core_entry;
			}
        	break;
		case "score._children" :
		    core_string=wiz_cmi_core_score_children;
        	break;	
		case "score.raw" :
		    core_string=wiz_cmi_core_score_raw;
        	break;	
		case "score.max" :
		    core_string=wiz_cmi_core_score_max;
        	break;	
		case "score.min" :
		    core_string=wiz_cmi_core_score_min;
        	break;		
		case "total_time" :
		    core_string=wiz_cmi_core_total_time;
        	break;	
		case "lesson_mode" :
		    core_string=wiz_cmi_core_lesson_mode;
        	break;
		case "exit" :
		    core_string=wiz_cmi_core_exit;
        	break;	
		case "session_time" :
		    core_string=wiz_cmi_core_session_time;
        	break;		
        default :
        	break;
   }
   return core_string;
}
function get_SCORM_suspend_data(){
   return wiz_cmi_suspend_data;
}
function get_SCORM_launch_data(){
	return wiz_cmi_launch_data;
}
function get_SCORM_comments(){
   return wiz_cmi_comments;
}
function get_SCORM_comments_from_lms(){
	return wiz_cmi_comments_from_lms;
}
function get_SCORM_objectives(objStr){
   var obj_string="";
   return obj_string;
}
function get_SCORM_student_data(sdStr){
   var sd_string="";
   switch (sdStr){
        case "_children" :
		    sd_string=wiz_cmi_student_data_children;
        	break;
		case "mastery_score" :
		    sd_string=wiz_cmi_student_data_mastery_score;
        	break;
		case "max_time_allowed" :
		    sd_string=wiz_cmi_student_data_max_time_allowed;
        	break;
		case "time_limit_action" :
		    sd_string=wiz_cmi_student_data_time_limit_action;
        	break;	
        default :
        	break;
   }
   return sd_string;
}
function get_SCORM_student_preference(spStr){
   var sp_string="";
   switch (spStr){
        case "_children" :
		    sp_string=wiz_cmi_student_preference_children;
        	break;
		case "audio" :
		    sp_string=wiz_cmi_student_preference_audio;
        	break;
		case "language" :
		    sp_string=wiz_cmi_student_preference_language;
        	break;
		case "speed" :
		    sp_string=wiz_cmi_student_preference_speed;
        	break;
		case "text" :
		    sp_string=wiz_cmi_student_preference_text;
        	break;	
        default :
        	break;
   }
   return sp_string;
}
function get_SCORM_interactions(inteStr){
   var inte_string="";
   return inte_string;
}
//-------SET-------------------------------------------------------------------
function set_SCORM_core(coreStr,setStr){
   var return_string="false";
   switch (coreStr){
        case "_children" :
		    wiz_cmi_core_children=setStr;
			return_string="true";
        	break;
		case "student_id" :
		    wiz_cmi_core_student_id=setStr;
			return_string="true";
        	break;	
		case "student_name" :
		    wiz_cmi_core_student_name=setStr;
			return_string="true";
        	break;
		case "lesson_location" :
		    wiz_cmi_core_lesson_location=setStr;
			return_string="true";
        	break;
		case "credit" :
		    wiz_cmi_core_credit=setStr;
			return_string="true";
        	break;	
		case "lesson_status" :
		    wiz_cmi_core_lesson_status=setStr;
			return_string="true";
        	break;
		case "entry" :
		    wiz_cmi_core_entry=setStr;
			return_string="true";
        	break;
		case "score._children" :
		    wiz_cmi_core_score_children=setStr;
			return_string="true";
        	break;	
		case "score.raw" :
		    wiz_cmi_core_score_raw=setStr;
			return_string="true";
        	break;	
		case "score.max" :
		    wiz_cmi_core_score_max=setStr;
			return_string="true";
        	break;	
		case "score.min" :
		    wiz_cmi_core_score_min=setStr;
			return_string="true";
        	break;		
		case "total_time" :
		    wiz_cmi_core_total_time=setStr;
			return_string="true";
        	break;	
		case "lesson_mode" :
		    wiz_cmi_core_lesson_mode=setStr;
			return_string="true";
        	break;
		case "exit" :
		    wiz_cmi_core_exit=setStr;
			return_string="true";
        	break;	
		case "session_time" :
		    wiz_cmi_core_session_time=setStr;
			return_string="true";
        	break;		
        default :
		    return_string="false";
        	break;
   }
   return return_string;
}
function set_SCORM_suspend_data(setStr){
	wiz_cmi_suspend_data=setStr;
    return "true";
}
function set_SCORM_launch_data(setStr){
	wiz_cmi_launch_data=setStr;
    return "true";
}
function set_SCORM_comments(setStr){
    wiz_cmi_comments=setStr;
    return "true";
}
function set_SCORM_comments_from_lms(setStr){
	wiz_cmi_comments_from_lms=setStr;
    return "true";
}
function set_SCORM_objectives(objStr,setStr){
	var return_string="false";
   return "true";
}
function set_SCORM_student_data(sdStr,setStr){
   var return_string="false";
   switch (sdStr){
        case "_children" :
		    wiz_cmi_student_data_children=setStr;
			return_string="true";
        	break;
		case "mastery_score" :
		    wiz_cmi_student_data_mastery_score=setStr;
			return_string="true";
        	break;
		case "max_time_allowed" :
		    wiz_cmi_student_data_max_time_allowed=setStr;
			return_string="true";
        	break;
		case "time_limit_action" :
		    wiz_cmi_student_data_time_limit_action=setStr;
			return_string="true";
        	break;	
        default :
		    return_string="false";
        	break;
   }
   return return_string;
}
function set_SCORM_student_preference(spStr,setStr){
   var return_string="false";
   switch (spStr){
        case "_children" :
		    wiz_cmi_student_preference_children=setStr;
			return_string="true";
        	break;
		case "audio" :
		    wiz_cmi_student_preference_audio=setStr;
			return_string="true";
        	break;
		case "language" :
		    wiz_cmi_student_preference_language=setStr;
			return_string="true";
        	break;
		case "speed" :
		    wiz_cmi_student_preference_speed=setStr;
			return_string="true";
        	break;
		case "text" :
		    wiz_cmi_student_preference_text=setStr;
			return_string="true";
        	break;	
        default :
		    return_string="false";
        	break;
   }
   return return_string;
}
function set_SCORM_interactions(inteStr){
   var return_string="false";
   return "true";
}
//-------Init-------------------------------------------------------------------
SCORMapi.prototype.LMSInitialize = function (InitStr){
	//;
	lmsInitCalled = true;
   _wiz_error_code="0";
   return "true";
}
SCORMapi.prototype.LMSFinish = function (InitStr){
	//;
	if(lmsInitCalled){
		//scoPlayerSubmit();
	}
   _wiz_error_code="0";
   lmsFinishCalled = true;
   return "true";
}
SCORMapi.prototype.LMSCommit = function (InitStr){
	if(lmsInitCalled){
		//scoPlayerSubmit();
	}
	//;
   _wiz_error_code="0";
   return "true";
}
//---------GET-SET----------------------------------------------------------------
SCORMapi.prototype.LMSGetValue = function (GetV){
	var returnV="";
	_wiz_error_code="0";
	var splitGetV_arr;
	var is_skip = false;
	splitGetV_arr=GetV.split(".");
	wiz_cmi_interactions_time=parseInt(wiz_cmi_core_date.getMonth());
	if(splitGetV_arr[0].toLowerCase()=="cmi"){
	  switch (splitGetV_arr[1].toLowerCase()){
          case "core" :
			  if(splitGetV_arr[2]!=""&&splitGetV_arr[2]!=null&&typeof(splitGetV_arr[2])!="undefined"){
				  if(splitGetV_arr[2]=="score"){
					  if(splitGetV_arr[3]!=""&&splitGetV_arr[3]!=null&&typeof(splitGetV_arr[3])!="undefined"){
			              returnV=get_SCORM_core(splitGetV_arr[2].toLowerCase()+"."+splitGetV_arr[3].toLowerCase());
					  }else{
						  _wiz_error_code="201";
					  }
				  }else{
			          returnV=get_SCORM_core(splitGetV_arr[2].toLowerCase());
			          if(splitGetV_arr[2].toLowerCase()=='lesson_location' || splitGetV_arr[2].toLowerCase()=='_children'){
			          	is_skip = true;
			          }
				  }
			  }else{
				  _wiz_error_code="201";
			  }
        	  break;
          case "suspend_data" :
              is_skip = true;
			  returnV=get_SCORM_suspend_data();
			  return returnV;
        	  break;
		  case "launch_data" :
		   	  is_skip = true;
			  returnV=get_SCORM_launch_data();
        	  break;
		  case "comments" :
			  returnV=get_SCORM_comments();
        	  break;
		  case "comments_from_lms" :
			  returnV=get_SCORM_comments_from_lms();
        	  break;	  
		  case "objectives" :
			  if(splitGetV_arr[2]!=""&&splitGetV_arr[2]!=null&&typeof(splitGetV_arr[2])!="undefined"){
			     returnV=get_SCORM_objectives(splitGetV_arr[2].toLowerCase());
			  }else{
				  _wiz_error_code="201";
			  }
        	  break;
		  case "student_data" :
			  if(splitGetV_arr[2]!=""&&splitGetV_arr[2]!=null&&typeof(splitGetV_arr[2])!="undefined"){
			     returnV=get_SCORM_student_data(splitGetV_arr[2].toLowerCase());
			      if(splitGetV_arr[2].toLowerCase()=='lesson_location' || splitGetV_arr[2].toLowerCase()=='_children'){
			          	is_skip = true;
			          }
			  }else{
				  _wiz_error_code="201";
			  }
        	  break;	  
          case "student_preference" :
			  if(splitGetV_arr[2]!=""&&splitGetV_arr[2]!=null&&typeof(splitGetV_arr[2])!="undefined"){
			     returnV=get_SCORM_student_preference(splitGetV_arr[2].toLowerCase());
			  }else{
				  _wiz_error_code="201";
			  }
        	  break;
		  case "interactions" :
			  if(splitGetV_arr[2]!=""&&splitGetV_arr[2]!=null&&typeof(splitGetV_arr[2])!="undefined"){
			     returnV=get_SCORM_interactions(splitGetV_arr[2].toLowerCase());
			  }else{
				  _wiz_error_code="201";
			  }
        	  break;	  
          default :
              returnV="";
        	  break;
      }
	}else{
		_wiz_error_code="201";
	}
	//
   if(returnV=="" && !is_skip){
	  _wiz_error_code="101";
   }
   return returnV;
}
SCORMapi.prototype.LMSSetValue = function (SetV,SetArgs){
	var returnS="false";
	_wiz_error_code="0";
	var splitSetV_arr;
	if(wiz_cmi_interactions_time>=11){wiz_cmi_interactions_time=0}
	splitSetV_arr=SetV.split(".");
	if(splitSetV_arr[0].toLowerCase()=="cmi"&&wiz_cmi_interactions_time<=11){
	  switch (splitSetV_arr[1].toLowerCase()){
          case "core" :
			  if(splitSetV_arr[2]!=""&&splitSetV_arr[2]!=null&&typeof(splitSetV_arr[2])!="undefined"){
				  if(splitSetV_arr[2]=="score"){
					  if(splitSetV_arr[3]!=""&&splitSetV_arr[3]!=null&&typeof(splitSetV_arr[3])!="undefined"){
			             returnS=set_SCORM_core(splitSetV_arr[2].toLowerCase()+"."+splitSetV_arr[3].toLowerCase(),SetArgs);
					  }else{
						 _wiz_error_code="201";
					  }
				  }else{
			          returnS=set_SCORM_core(splitSetV_arr[2].toLowerCase(),SetArgs);
				  }
			  }else{
				  _wiz_error_code="201";
			  }
        	  break;
          case "suspend_data" :
			  returnS=set_SCORM_suspend_data(SetArgs);
        	  break;
		  case "launch_data" :
			  returnS=set_SCORM_launch_data(SetArgs);
        	  break;
		  case "comments" :
			  returnS=set_SCORM_comments(SetArgs);
        	  break;
		  case "comments_from_lms" :
			  returnS=set_SCORM_comments_from_lms(SetArgs);
        	  break;	  
		  case "objectives" :
			  if(splitSetV_arr[2]!=""&&splitSetV_arr[2]!=null&&typeof(splitSetV_arr[2])!="undefined"){
			     returnS=set_SCORM_objectives(splitSetV_arr[2].toLowerCase(),SetArgs);
			  }else{
				  _wiz_error_code="201";
			  }
        	  break;
		  case "student_data" :
			  if(splitSetV_arr[2]!=""&&splitSetV_arr[2]!=null&&typeof(splitSetV_arr[2])!="undefined"){
			     returnS=set_SCORM_student_data(splitSetV_arr[2].toLowerCase(),SetArgs);
			  }else{
				  _wiz_error_code="201";
			  }
        	  break;	  
          case "student_preference" :
			  if(splitSetV_arr[2]!=""&&splitSetV_arr[2]!=null&&typeof(splitSetV_arr[2])!="undefined"){
			     returnS=set_SCORM_student_preference(splitSetV_arr[2].toLowerCase(),SetArgs);
			  }else{
				  _wiz_error_code="201";
			  }
        	  break;
		  case "interactions" :
			  if(splitSetV_arr[2]!=""&&splitSetV_arr[2]!=null&&typeof(splitSetV_arr[2])!="undefined"){
			     returnS=set_SCORM_interactions(splitSetV_arr[2].toLowerCase(),SetArgs);
			  }else{
				  _wiz_error_code="201";
			  }
        	  break;	  
          default :
              returnS="false";
        	  break;
      }
	}else{
		_wiz_error_code="201";
	}
	putToSendAdapter();
    return returnS;
}
//
function splitString(totalStr,bofStr,eofStr){
	var splitStr="";
	var bofInt=0;
	var eofInt=0;
	bofInt=totalStr.indexOf(bofStr);
	bofInt=bofInt+bofStr.length;
	eofInt=totalStr.lastIndexOf(eofStr);
	if(bofInt>=0&&eofInt>bofInt){
	    splitStr=totalStr.substring(bofInt,eofInt);
	}
	return splitStr;
}
function splitEndString(totalStr,bofStr){
	var splitStr="";
	var bofInt=0;
	var totalInt=0;
	bofInt=totalStr.lastIndexOf(bofStr);
	bofInt=bofInt+bofStr.length;
	totalInt=totalStr.length;
	totalInt=totalInt-2;
	if(bofInt>=0&&totalInt>bofInt){
	    splitStr=totalStr.substring(bofInt,totalInt);
	}
	return splitStr;
}
//
function loopArrForVar(loopArr,varName){
	var idx1=-1;
	var tempVar="";
	varName=varName+"=";
	var loopArrLength=loopArr.length;
	for(var i=0;i<loopArrLength;i++){
		if(loopArr[i]!=""&&loopArr[i]!=null){
	        var idx1=loopArr[i].indexOf(varName);
		    if(idx1>-1){
	           idx1=idx1+varName.length;
			   if(idx1<loopArr[i].length){
	              tempVar=loopArr[i].substring(idx1);
			   }else{
				   tempVar="";
			   }
			   break;
		    }
		}
	}
	return tempVar;
}
//
function initSCORM_Var(initVar){
	var splitArr=[];
	splitArr=initVar.split("\r\n");
	//�鿴ѧϰ��¼ת��������
	//alert("1splitArrLength:"+splitArr.length+"--1splitArr:"+splitArr);
	_wiz_error_code=loopArrForVar(splitArr,"error");
	_wiz_error_decrp=loopArrForVar(splitArr,"error_text");
	_wiz_scormAdapter_ver=loopArrForVar(splitArr,"version");
	_wiz_aicc_data=loopArrForVar(splitArr,"aicc_data");
	wiz_cmi_core_student_id=loopArrForVar(splitArr,"student_ID");
	var _temp_cmi_core_student_name=loopArrForVar(splitArr,"student_name");
	//wiz_cmi_core_student_name=loopArrForVar(splitArr,"student_name");
	wiz_cmi_core_lesson_location=loopArrForVar(splitArr,"lesson_location");
	var _wiz_temp_credit=""
	_wiz_temp_credit=loopArrForVar(splitArr,"credit");
	if (_wiz_temp_credit != "") {
          if (_wiz_temp_credit.toUpperCase().substring(0,1)=="C") {
               wiz_cmi_core_credit="credit";
          }else {
               wiz_cmi_core_credit="no credit";
          }
    }
	var _wiz_temp_lesson_status="";
	_wiz_temp_lesson_status=loopArrForVar(splitArr,"lesson_status");
	//debug info:alert("module status:lesson_status="+_wiz_temp_lesson_status)
	if (_wiz_temp_lesson_status != "") {
          if (_wiz_temp_lesson_status.toUpperCase().substring(0,1)=="P") {
			   wiz_cmi_core_lesson_status="passed";
			   wiz_cmi_core_lesson_mode="review";
          }else if (_wiz_temp_lesson_status.toUpperCase().substring(0,1)=="C") {
               wiz_cmi_core_lesson_status="completed";
			   wiz_cmi_core_lesson_mode="review";
          }else if (_wiz_temp_lesson_status.toUpperCase().substring(0,1)=="F") {
               wiz_cmi_core_lesson_status="failed";
			   wiz_cmi_core_lesson_mode="review";
          }else if (_wiz_temp_lesson_status.toUpperCase().substring(0,1)=="I") {
               wiz_cmi_core_lesson_status="incomplete";
			   wiz_cmi_core_lesson_mode="normal";
          }else if (_wiz_temp_lesson_status.toUpperCase().substring(0,1)=="B") {
               wiz_cmi_core_lesson_status="browsed";
			   wiz_cmi_core_lesson_mode="normal";
          }else if (_wiz_temp_lesson_status.toUpperCase().substring(0,1)=="N") {
               wiz_cmi_core_lesson_status="not attempted";
			   wiz_cmi_core_lesson_mode="normal";
          }else {
               wiz_cmi_core_lesson_status="incomplete";
			   wiz_cmi_core_lesson_mode="normal";
          }
    }
	wiz_cmi_core_total_time=loopArrForVar(splitArr,"time");
	var _wiz_temp_score="";
	_wiz_temp_score=loopArrForVar(splitArr,"score");
	var _wiz_temp_score_arr=_wiz_temp_score.split(",");
	if(_wiz_temp_score_arr.length==3){
	  wiz_cmi_core_score_raw=_wiz_temp_score_arr[0];
      wiz_cmi_core_score_max=_wiz_temp_score_arr[1];
      wiz_cmi_core_score_min=_wiz_temp_score_arr[2];
	}
	_wiz_temp_score_arr=null;
	//
	_wiz_attempt_number=loopArrForVar(splitArr,"attempt_number");
	wiz_cmi_student_data_mastery_score=loopArrForVar(splitArr,"mastery_score");
	wiz_cmi_student_data_max_time_allowed=loopArrForVar(splitArr,"max_time_allowed");
	_wiz_familiar_name=loopArrForVar(splitArr,"familiar_name");
	if(_temp_cmi_core_student_name==""){
		if(_wiz_familiar_name!=""){
		   wiz_cmi_core_student_name=_wiz_familiar_name;
		}
	}else{
		wiz_cmi_core_student_name=_temp_cmi_core_student_name;
	}
	wiz_cmi_suspend_data=getSuspendData(splitArr);
	
	//debug info:alert("module process:suspend_data="+wiz_cmi_suspend_data);
  if ((wiz_cmi_suspend_data=="[core_lesson]")||(wiz_cmi_suspend_data=="[core_vendor]"))
  wiz_cmi_suspend_data = "";
  //alert(wiz_cmi_suspend_data);

}


function getSuspendData(loopArr){
	suspend_data = '';
	var loopArrLength=loopArr.length;
	for(var i=0;i<loopArrLength;i++){
		if(loopArr[i]=="[core_lesson]"){
	        if(loopArrLength > (i +1)){
	        	suspend_data = loopArr[i +1]
	        }
		}
	}
	suspend_data = clearBr(suspend_data,"\r\n")
	return suspend_data;
}

function clearBr(key,replaceChar) 
{ 

    key = key.replace(/\n/g,'');
    return key; 
}
//�˷���ûʹ��
function initSCORMVar(initVar){
	_wiz_error_code=splitString(initVar,"error=","&error_text=");
	_wiz_error_decrp=splitString(initVar,"&error_text=","&version=");
	_wiz_scormAdapter_ver=splitString(initVar,"&version=","&aicc_data=");
	_wiz_aicc_data=splitString(initVar,"&aicc_data=","&student_ID=");
	wiz_cmi_core_student_id=splitString(initVar,"&student_ID=","&student_name=");
	//wiz_cmi_core_student_name=splitString(initVar,"&student_name=","&lesson_location=");
	var _temp_cmi_core_student_name=splitString(initVar,"&student_name=","&lesson_location=");
	wiz_cmi_core_lesson_location=splitString(initVar,"&lesson_location=","&credit=");
	//
	var _wiz_temp_credit="";
	wiz_cmi_core_credit=splitString(initVar,"&credit=","&lesson_status=");
	if (_wiz_temp_credit != "") {
          if (_wiz_temp_lesson_status.toUpperCase().substring(0,1)=="C") {
               wiz_cmi_core_credit="credit";
          }else {
               wiz_cmi_core_credit="no credit";
          }
    }
	//
	var _wiz_temp_lesson_status="";
	_wiz_temp_lesson_status=splitString(initVar,"&lesson_status=","&time=");
	if (_wiz_temp_lesson_status != "") {
          if (_wiz_temp_lesson_status.toUpperCase().substring(0,1)=="P") {
			   wiz_cmi_core_lesson_status="passed";
			   wiz_cmi_core_lesson_mode="review";
          }else if (_wiz_temp_lesson_status.toUpperCase().substring(0,1)=="C") {
               wiz_cmi_core_lesson_status="completed";
			   wiz_cmi_core_lesson_mode="review";
          }else if (_wiz_temp_lesson_status.toUpperCase().substring(0,1)=="F") {
               wiz_cmi_core_lesson_status="failed";
			   wiz_cmi_core_lesson_mode="review";
          }else if (_wiz_temp_lesson_status.toUpperCase().substring(0,1)=="I") {
               wiz_cmi_core_lesson_status="incomplete";
			   wiz_cmi_core_lesson_mode="normal";
          }else if (_wiz_temp_lesson_status.toUpperCase().substring(0,1)=="B") {
               wiz_cmi_core_lesson_status="browsed";
			   wiz_cmi_core_lesson_mode="normal";
          }else if (_wiz_temp_lesson_status.toUpperCase().substring(0,1)=="N") {
               wiz_cmi_core_lesson_status="not attempted";
			   wiz_cmi_core_lesson_mode="normal";
          }else {
               wiz_cmi_core_lesson_status="incomplete";
			   wiz_cmi_core_lesson_mode="normal";
          }
    }
	wiz_cmi_core_total_time=splitString(initVar,"&time=","&score=");
	//
	var _wiz_temp_score="";
	_wiz_temp_score=splitString(initVar,"&score=","&[student_data]");
	var _wiz_temp_score_arr=_wiz_temp_score.split(",");
	if(_wiz_temp_score_arr.length==3){
	  wiz_cmi_core_score_raw=_wiz_temp_score_arr[0];
      wiz_cmi_core_score_max=_wiz_temp_score_arr[1];
      wiz_cmi_core_score_min=_wiz_temp_score_arr[2];
	}
	_wiz_temp_score_arr=null;
	//
	_wiz_student_data=splitString(initVar,"&[student_data]","&attempt_number=");
	_wiz_attempt_number=splitString(initVar,"&attempt_number=","&mastery_score=");
	wiz_cmi_student_data_mastery_score=splitString(initVar,"&mastery_score=","&max_time_allowed=");
	wiz_cmi_student_data_max_time_allowed=splitString(initVar,"&max_time_allowed=","&[student_demographics]");
	_wiz_student_demographics=splitString(initVar,"&[student_demographics]","&familiar_name=");
	_wiz_familiar_name=splitString(initVar,"&familiar_name=","&[core_lesson]");

	wiz_cmi_suspend_data=splitEndString(initVar,"&[core_lesson]&");
	//
	if(_temp_cmi_core_student_name==""){
		if(_wiz_familiar_name!=""){
		   wiz_cmi_core_student_name=_wiz_familiar_name;
		}
	}else{
		wiz_cmi_core_student_name=_temp_cmi_core_student_name;
	}
	//
	//
}