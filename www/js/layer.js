STS_EMPTY = 0
STS_INCOMPLETE = 1
STS_COMPLETE = 2

COOKIE_QUE_FLG = 'quiz_que_flg';
//COOKIE_EXPIRY = gen_get_expiry_date(10);


// ---------------- Question Set ---------------------

function CwQuestionSet() {
	this.questions = new Array()	

	this.addQuestion = CwQuestionSetAdd
	this.getQuestion = CwQuestionSetGet
	this.getQuestionStatus = CwQuestionSetGetStatus
	this.loadQuestion = CwQuestionSetLoad
	this.unloadQuestion = CwQuestionSetUnload
	this.getSize = CwQuestionSetGetSize
	this.setFlag = CwQuestionSetSetFlag
	this.getFlag = CwQuestionSetGetFlag
	this.submit = CwQuestionSetSubmit
	this.nextQuestion = CwQuestionSetNext
}

function CwQuestionSetAdd(q) {
	n = this.questions.length
	this.questions[n] = q	
}

function CwQuestionSetGet(i) {
	return this.questions[i-1]
}

function CwQuestionSetGetStatus(i) {
	return this.questions[i-1].getStatus()	
}
function CwQuestionSetLoad(i) {
	return this.questions[i-1].load()
}
function CwQuestionSetUnload(i) {
	return this.questions[i-1].unload()	
}
function CwQuestionSetGetSize() {
	//alert ('n=' + this.questions.length);
	return this.questions.length
}
function CwQuestionSetSetFlag(i, bool) {
	this.questions[i-1].setFlag(bool)	
}
function CwQuestionSetGetFlag(i) {
	return this.questions[i-1].getFlag()	
}
function CwQuestionSetSubmit() {
	qt_show_layer('completion');
}
function CwQuestionSetNext(cur_que){
	//if(this.getQuestionStatus(cur_que)==STS_COMPLETE||this.getQuestion(cur_que).getType()=="MT"||this.getQuestion(cur_que).getType()=="MT_DD"||this.getQuestion(cur_que).getType()=="MT_LM"){
		if(cur_que<this.getSize()){
			this.unloadQuestion(cur_que)
			this.loadQuestion(cur_que+1)
			cur_que++
		}else if(cur_que==this.getSize()){
			this.unloadQuestion(cur_que)
			this.submit();
		}
	//}
	return cur_que
}

// ---------------- Question ---------------------

function CwQuestion(layer, id, type, answer, num_ans) {

	this.layer = layer
	this.id = id
	this.answer = answer
	this.answer_array = new Array()//for MT answers
	this.type = type
	this.num = num_ans
	this.ans_cnt
	this.status = STS_EMPTY
	this.interactions = new Array()
	this.cookie_token_nm = '_q' + id;
	
	this.addInteraction = CwQuestionAddInteraction
	this.getInteraction = CwQuestionGetInteraction
	this.getStatus = CwQuestionGetStatus
	this.getId = CwQuestionGetId
	this.setFlag = CwQuestionSetFlag
	this.getFlag = CwQuestionGetFlag
	this.load = CwQuestionLoad
	this.unload = CwQuestionUnload
	this.getSize = CwQuestionGetSize	
	this.getAnswer = CwQuestionGetAnswer
	this.getType = CwQuestionGetType
	this.getNumAns = CwQuestionGetNumAns
	this.queLayerTop;
}

function CwQuestionGetAnswer() {
	
	return this.answer
}

function CwQuestionGetType() {

	return this.type
}

function CwQuestionGetNumAns() {
	return this.num
}

function CwQuestionAddInteraction(interaction) {
	n = this.interactions.length
	this.interactions[n] = interaction	
}

function CwQuestionGetInteraction(i) {
	return this.interactions[i-1]
}

function CwQuestionLoad() {
	n = this.interactions.length;
	for (i = 0; i < n; i++) {
		this.interactions[i].showAnswer();
	}
	if(document.frmResult.mod_type.value == 'EXC'){
		eval(this.layer).style.display = 'block'
		qt_show_layer(this.layer);
	}else{
		MM_showHideLayers(this.layer,'show',this.queLayerTop);

	}
 	if (this.interactions[0] && (this.interactions[0].getType() == 'FB' || this.interactions[0].getType() == 'ES')){
		if (navigator.appName != 'Netscape' ){
		    var	objname = 'frm' + this.layer.substring(5,this.layer.length) + '.txt_q' + this.layer.substring(5,this.layer.length)+'_i1'
			if(eval(objname).type != 'textarea'){
			eval(objname + '.focus()');
			}		
		}
	}
	
}

function CwQuestionUnload() {
	MM_showHideLayers(this.layer,'hide')
}

function CwQuestionGetStatus() {
	
		var i, j, n;
		j = 0
		n = this.interactions.length
		for (i = 0; i < n; i++) {
			if (this.interactions[i].getAnswer() != '' || this.interactions[i].getFileAttach() != '')			
					j++					
		}		
		
		if (j == n)
			return STS_COMPLETE
		else if (j > 0)
			return STS_INCOMPLETE
		else
			return STS_EMPTY
}

function CwQuestionGetId() {
	return this.id
}
function CwQuestionGetSize() {
	return this.interactions.length
}
function CwQuestionSetFlag(bool) {
	if (bool == true) {
		cookie_token_val = '1'
	} else {
		cookie_token_val = '0'
	}
	
	gen_set_cookie_token(COOKIE_QUE_FLG, this.cookie_token_nm, cookie_token_val, gen_get_expiry_date(10))
}
function CwQuestionGetFlag() {
	cookie_token_val = gen_get_cookie_token(COOKIE_QUE_FLG, this.cookie_token_nm)
	if (cookie_token_val == '1')
		return true;
	else
		return false;
}

function CwQuestionGetNumAns() {
	return this.num
}

// ---------------- Interaction ---------------------
function CwInteraction(que_id, id, type, ctrl) {
	this.que_id = que_id
	this.id = id
	this.type = type
	this.ctrl = ctrl
	this.c_answer = new Array()
	this.ans_case = new Array()
	this.ans_space = new Array()
	this.ans_type = new Array()
	this.score = new Array()
	this.cookie_token_nm = '_qa' + que_id + '_i' + id;
	this.setAnswer = CwInteractionSetAnswer
	this.setAnswerExtend = CwInteractionSetAnswerExtend
	this.setAnswerShuffle = CwInteractionSetAnswerShuffle
	this.getAnswer = CwInteractionGetAnswer
	this.getAnswerExtend = CwInteractionGetAnswerExtend
	this.setFileAttach = CwInteractionSetFileAttach
	this.getFileAttach = CwInteractionGetFileAttach		
	this.showAnswer = CwInteractionShowAnswer
	this.getId = CwInteractionGetId
	this.getType = CwInteractionGetType
	
	this.setCorrectAns = CwInteractionSetCorrectAnswer
	this.getCorrectAns = CwInteractionGetCorrectAnswer
	this.setAnsCondition = CwInteractionSetAnswerCondition
	this.getAnsCase = CwInteractionGetAnswerCase
	this.getAnsPos = CwQuestionGetAnsPos
	this.getAnsSpace = CwInteractionGetAnswerSpace
	this.getAnsType = CwInteractionGetAnswerType
	this.setScore = CwInteractionSetScore
	this.getScore = CwInteractionGetScore
	this.unsave_value
	this.answer_value
	this.answer_extend_value
	this.answer_shuffled_position
	this.attachment_value
}

function CwInteractionShowAnswer() {
	a = this.getAnsPos();

	if ( this.type == 'MC_M' && a != ''){
		for ( i=0; i< a.length; i++){
			b = a.substring(i, i+1)	
			if ( b != '~' )
				this.ctrl[b].checked = true;
		}
	} else if (this.type == 'MC' && a !='') {
		this.ctrl[parseInt(a)-1].checked = true;
	} else if (this.type == 'FB' && a !='') {
		this.ctrl.value = a;		
	} else if (this.type == 'ES' && a !='') {
		this.ctrl.value = a;		
	} else if (this.type == 'MT' && a != '' )
	{	for ( var i = 1; i <= totalQ; i++)
		{	if ( this.que_id == qset.getQuestion(i).getId() )
				counter = i;
		}

		var objname = 'G0' + (counter)		
		var tagname = objname + a
		var souname = objname + 'Source' + eval(objname).sname[this.id]
		
	   if (navigator.appName == 'Netscape' ) {
     		 eval(souname).pageX = Math.round(0.5 *(eval(tagname).pageX + eval(tagname).pageWidth)) + Math.round(0.5 *eval(souname).pageWidth)
			 eval(souname).pageY = Math.round(0.5 *(eval(tagname).pageY + eval(tagname).pageHeight))+ Math.round(0.5 *eval(souname).pageHeight)
			 eval(souname).zIndex = eval(tagname).zIndex+1
		 } else {
		 	 eval(souname).style.pixelLeft = eval(tagname).style.pixelLeft + eval(tagname).style.pixelWidth/2 - eval(souname).style.pixelWidth/2
			 eval(souname).style.pixelTop = eval(tagname).style.pixelTop + eval(tagname).style.pixelHeight/2 - eval(souname).style.pixelHeight/2
   			 eval(souname).style.zIndex = eval(tagname).style.zIndex+1
    	}
	
	}
}
function CwInteractionSetFileAttach(a) {
	this.attachment_value = a
}

function CwInteractionGetFileAttach() {
	if( this.attachment_value == null ) {
		return ''
	} else {
		return this.attachment_value
	}	
}
function CwInteractionSetAnswer(a) {
	if (this.type == 'MC_M'){
		a = '';
		for ( i=0; i< this.ctrl.length; i++){
			if ( this.ctrl[i].checked == true ) {
				a +=  this.ctrl[i].value + '~'
			}
		}
	}
	this.answer_value = a
	return
}

function CwInteractionSetAnswerExtend(a) {
	this.answer_extend_value = a
	return
}

function CwInteractionSetAnswerShuffle(a) {
	if (this.type == 'MC_M'){
		for ( i=0; i< this.ctrl.length; i++){
			if ( this.ctrl[i].checked == true ) {
				var j=i+1;
				a +=  i + '~'
			}
		}
	}
	this.answer_shuffled_position = a
	return
}

function CwInteractionGetAnswer() {
	if( this.answer_value == null ) {
		return ''
	} else {
		return this.answer_value
	}
}

function CwInteractionGetAnswerExtend() {
	if( this.answer_extend_value == null ) {
		return ''
	} else {
		return this.answer_extend_value
	}
}


function CwQuestionGetAnsPos() {
	if (this.answer_shuffled_position == null) {
		return ''
	}else {
		return this.answer_shuffled_position;
	}
}

function CwInteractionGetId() {
	return this.id
}

function CwInteractionGetType() {
	return this.type
}
function CwInteractionSetCorrectAnswer(ans){
	this.c_answer[this.c_answer.length] = ans;
}

function CwInteractionGetCorrectAnswer(){
	return this.c_answer;
}

function CwInteractionSetAnswerCondition(_case,_space,_type){
	this.ans_case[this.ans_case.length] = _case;
	this.ans_space[this.ans_space.length] = _space;
	this.ans_type[this.ans_type.length] = _type;
}

function CwInteractionGetAnswerCase(){
	return this.ans_case;
}

function CwInteractionGetAnswerSpace(){
	return this.ans_space;
}

function CwInteractionGetAnswerType(){
	return this.ans_type;
}
function CwInteractionSetScore(score){
	this.score[this.score.length] = score;
}

function CwInteractionGetScore(){
	return this.score;
}
// ---------------- Dreamweaver generated ---------------------

function MM_showHideLayers(obj_name,visStr,ypos) {
 var theObj = null;
 if(document.all){
 	theObj = eval('document.all[\'' + obj_name + '\']')
	
     if (visStr == 'show'){
	  	visStr = 'visible'; //convert vals
		if(ypos == null){
	  		var pos = 150
		}else{
			var pos = ypos
		}
		$("#" + obj_name).show();
	 }
     if (visStr == 'hide') {
	  	visStr = 'hidden';
		var pos = -2000;
		$("#" + obj_name).hide();
	 }
     if (theObj) {
	 	theObj.style.top = pos;
		theObj.style.visibility = visStr;
	}
 }else if(document.getElementById != null){
 	theObj = document.getElementById(obj_name)
	if (visStr == 'show'){
		visStr = 'visible'; //convert vals
		if(ypos == null){
	  		pos = 150
		}else{
			pos = ypos
		}
		$("#" + obj_name).show();
	}
	if (visStr == 'hide') {
		visStr = 'hidden';
		pos = -2000;
		$("#" + obj_name).hide();
	}
    
	if (theObj) {
		theObj.style.visibility = visStr;
		theObj.style.top = pos;
	}	
 }else{
	 theObj = eval('document.layers[\'' + obj_name + '\']')
	if (theObj) {
		theObj.visibility = visStr;
		if (visStr == 'hide' ){
			theObj.pageY = -2000;
			$("#" + obj_name).hide();
		} else {
			if(ypos == null){
		  		pos = 80
			} else {
				pos = ypos
			} 
			$("#" + obj_name).show();
		}
	}
 }
}

function MM_findObj(r, d) { //v3.0  special CourseBuilder version of findObj
  var p,n,i,x;  if (!d) d=document; n=r.substring(r.lastIndexOf('.')+1);
  if (n.indexOf('layers[')==0) n=n.substring(8,n.length-2);
  if (parent.frames.length) { if (r.indexOf('.frames[')==6) d=eval(r.substring(0,r.indexOf(']')+10));
    if ((p=r.indexOf('?'))!=-1) {n=r.substring(0,p); d=parent.frames[r.substring(p+1)].document;} }
  if (!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for (i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);return x;
}

function MM_setIntProps(jsStr) { //v1.0
  eval(jsStr);
}

function MM_initInteractions() { //v1.0
  if (window.MM_initIntFns) {eval(window.MM_initIntFns); window.MM_initIntFns = '';}
}

function MM_callJS(jsStr) { //v2.0
  return eval(jsStr)
}

function MM_swapImage() { //v2.0
  var i,j=0,objStr,obj,swapArray=new Array,oldArray=document.MM_swapImgData;
  for (i=0; i < (MM_swapImage.arguments.length-2); i+=3) {
    objStr = MM_swapImage.arguments[(navigator.appName == 'Netscape')?i:i+1];
    if ((objStr.indexOf('document.layers[')==0 && document.layers==null) ||
        (objStr.indexOf('document.all[')   ==0 && document.all   ==null))
      objStr = 'document'+objStr.substring(objStr.lastIndexOf('.'),objStr.length);
    obj = eval(objStr);
    if (obj != null) {
      swapArray[j++] = obj;
      swapArray[j++] = (oldArray==null || oldArray[j-1]!=obj)?obj.src:oldArray[j];
      obj.src = MM_swapImage.arguments[i+2];
  } }
  document.MM_swapImgData = swapArray; //used for restore
}

function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf('#')!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}
