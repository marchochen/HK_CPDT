
// ---------------- Question Set ---------------------

function CwQuestionSet() {
	this.questions = new Array()	

	this.addQuestion = CwQuestionSetAdd
	this.getQuestion = CwQuestionSetGet
	this.getSize = CwQuestionSetGetSize
}

function CwQuestionSetAdd(q) {
	n = this.questions.length
	this.questions[n] = q	
}

function CwQuestionSetGet(i) {
	return this.questions[i-1]
}

function CwQuestionSetGetSize() {
	return this.questions.length
}

// ---------------- Question ---------------------

function CwQuestion(id,media,type,answer,num_ans) {

	this.id = id
	this.media = media
	this.answer = answer
	this.type = type
	this.num = num_ans

	this.getId = CwQuestionGetId
	this.getMedia = CwQuestionGetMedia
	this.getAnswer = CwQuestionGetAnswer
	this.getType = CwQuestionGetType
	this.getNumAns = CwQuestionGetNumAns
}


function CwQuestionGetId() {
	return this.id
}


function CwQuestionGetAnswer() {
	return this.answer
}


function CwQuestionGetMedia() {
	if ( this.media.match('.swf'))
		this.media = ''
	return this.media
}

function CwQuestionGetType() {
	return this.type
}

function CwQuestionGetNumAns() {
	return this.num
}

//------------------------------------

function wizQSetTimeLeft(second) {
	gen_set_cookie(COOKIE_TIME_LEFT, second, gen_get_expiry_date(10));
}

function wizQGetTimeLeft() {
	tmp = gen_get_cookie(COOKIE_TIME_LEFT);
	if (tmp != '')
		return parseInt(tmp);
	else
		return testDuration;
}

function MM_showHideLayers() { //v2.0
  var i, visStr, args, theObj;
  args = MM_showHideLayers.arguments;
  for (i=0; i<(args.length-2); i+=3) { //with arg triples (objNS,objIE,visStr)
    visStr   = args[i+2];
    if (navigator.appName == 'Netscape' && document.layers != null) {
      theObj = eval(args[i]);
      if (theObj) theObj.visibility = visStr;
    } else if (document.all != null) { //IE
      if (visStr == 'show') visStr = 'visible'; //convert vals
      if (visStr == 'hide') visStr = 'hidden';
      theObj = eval(args[i+1]);
      if (theObj) theObj.style.visibility = visStr;
  } }
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

