// Copyright 1998,1999 Macromedia, Inc. All rights reserved.

// Localization strings
var TRACK_system_not_found = 'Tracking system not found.';

//Constructs an Interaction
function MM_interaction(theSelf, theAllowMultiSel,theAllThatApply,theUnknownIsCorrect){			   
  // properties

  this.allowMultiSel = theAllowMultiSel;
  this.allThatApply = theAllThatApply;
  this.unknownIsCorrect = theUnknownIsCorrect;
  this.totalElems = 0;
  this.possCorrect = 0;
  this.possIncorrect = 0;
  this.knownResponse = false;
  this.totalCorrect = 0;
  this.totalIncorrect = 0;
  this.correct = false;
  this.totalTaget = 0;
//  this.score = 0;

 

  this._self = theSelf;

  this.browserIsNS = (navigator.appName.indexOf('Netscape') != -1);
  this.browserIsIE = (navigator.appName.indexOf('Microsoft') != -1);
  this.osIsWindows = (navigator.appVersion.indexOf('Win') != -1);
  this.osIsMac = (navigator.appVersion.indexOf('Mac') != -1); // ????
  this.browserVersion = parseFloat(navigator.appVersion);
  
  this.e = new Array();
  this.b = new Array();
  this.sname = new Array();
  
  // methods
  this.init = MM_intInit;
  this.reset = MM_intReset;
  this.resetElems = MM_intResetElems;
  this.update = MM_intUpdate;
  this.add = MM_intAdd;

}

  
  
  
//Calls the element init funtions if they exist, and then does a reset
function MM_intInit() {
  var i,j,localPC;

  with (this) {
    // init elems, and set totalElems, possCorrect, possIncorrect;
    totalElems = 0;
    possCorrect = 0;
    possIncorrect = 0;
	totalTaget = 0;
  	var counter = 1;
   	for (i in e) if (i != 'length') {
	  if (i.match('Source')){
		 sname[counter]= i.substring(6,i.length);
		 counter++;
	  }else if (i.match('Taget'))
	  		totalTaget++;
      if (e[i].init != null) e[i].init();
      totalElems++;
   
    }
	
    reset();
  }
  window['\''+this._self+'\''] = this._self; //redeclare global on window in case inserted in layer
}

//Called to reset the interaction
function MM_intReset() {
  with (this) {
  
    resetElems();
	
    update(true);
  }
}

//Calls the reset for the individual elements
function MM_intResetElems() {
  with (this) {
    for (var i in e) if (i != 'length')
	{  	if (e[i].reset != null)  e[i].reset();		
				
	 }
    update(true);
  }
}

//Update the interaction state
// Note: tries will be updated by the judge method, and time will
//       be updated by both this method and the judge method.
function MM_intUpdate(noJudge) {
 // if (!this.disabled) 
 	with (this) {
    knownResponse = false;
    totalCorrect = 0;
    totalIncorrect = 0;
    correct = false;
   // score = 0;
	
	//var counter;
	for (var i in e) if (i != 'length'){
		for (var j in e[i].c) if (j != 'length') {
			if (e[i].c[j].selected == true) 
			{		for ( var k in sname){
						 if (i.substring(6,i.length) == sname[k]){
							qset.getQuestion(currentQue).getInteraction(k).setAnswer(j);
							if(document.frmResult && document.frmResult.mod_type && document.frmResult.mod_type.value == 'EXC' && qset.getQuestion(currentQue).type != 'MT_LM'){
						 		eval('_MT' +currentQue +'.source[k-1].setAnswer(j)')
						 	}
						 }
					}				
			}
		}
	}

    if (!knownResponse) correct = unknownIsCorrect;
    else if (totalIncorrect != 0) correct = false;
    else if (totalCorrect == 0) correct = null; // not judged
    else correct = (!allThatApply || totalCorrect >= possCorrect);


 }
 
}

function MM_intAdd(theType, A, B, C, D, E, F, G, H, I, J, K, L, M) {
  var theObj = eval('new MM_' + theType + '(' + this._self + ', A, B, C, D, E, F, G, H, I, J, K, L, M)');
  if (theObj._isChoice != null) 
    this.e[A].c[B] = theObj;
  else 
    this.e[A] = theObj;
}


//Finds any object in either browser using recursion.
//Only pass the first argument, the name of the object to find.
//Returns a pointer the object if found, else an empty string.
//  MM_intFindObject('bar') returns the object
//  document.layers['foo'].document.layers['bar']

function MM_intFindObject(objName,  parentObj) {
  var i,tempObj='',found=false,curObj = '';
  var NS = (navigator.appName.indexOf('Netscape') != -1);
  if (!NS && document.all) curObj = document.all[objName]; //IE4
  if (!curObj) {
    parentObj = (parentObj != null)? parentObj.document : document;
    if (parentObj[objName] != null) curObj = parentObj[objName]; //at top level
    else { //if in form
      if (parentObj.forms) for (i=0; i<parentObj.forms.length; i++) {  //search level for form object
        if (parentObj.forms[i][objName]) {
          curObj = parentObj.forms[i][objName];
          found = true; break;
      } }
      if (!found && NS && parentObj.layers && parentObj.layers.length > 0) {
        parentObj = parentObj.layers;
        for (i=0; i<parentObj.length; i++) { //else search for child layers
          tempObj = MM_intFindObject(objName,parentObj[i]); //recurse
          if (tempObj) { curObj = tempObj; break;} //if found, done
  } } } }
  if(!curObj){
  	if(document.getElementById != null){
  		curObj = document.getElementById(objName);
  		//alert('getElementById');
  	}
}
  return curObj;
}

//Called from within conditions to check document properties
function MM_getDocProp(theName, theProp, theType) {
  var theObj = MM_intFindObject(theName);
  if (theObj) return eval('theObj.' + theProp);
  else return null;
}

function MM_resetInt(intId,method,item) { //v1.0
  if (item!=null && item)
    if (method=='resetElems') {method='e[\''+item+'\'].reset'; item=''}
    else item = '\''+item+'\'';
  else item='';
  eval(intId+'.'+method+'('+item+')');

 var counter = 1;
   for (var i in eval(intId).e) if (i != 'length')
	{  
		if ( i.match('Source') ){
	  	
			qset.getQuestion(currentQue).getInteraction(counter).setAnswer('');
			counter++;
			
			}
	 }
}

