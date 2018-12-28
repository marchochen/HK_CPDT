// ------------------Multiple Quiz Template General untility functions ------------------- 
// Convention:
//   public functions : use "qt_" prefix 
//   private functions: use "_qt_" prefix
//  Dependency:none
//  Copyright:2002 Cyberwisdom.net
// ------------------------------------------------------------ 

var browser_type = qt_get_browser()
_MM_reloadPage(true)
// Browser Dectector-------------------------------------
//
//Usage : qt_get_browser()
// return string "IE" for Internet Explorer, "NS4" for Netscape 4.X , "NS6" for Netscape 6.X(DOM1.0) 
//
//---------------------------------------------------------------
function qt_get_browser(){
	if(document.layers){
		return 'NS4'
	}else if(document.all){
		return 'IE'
	}else if(document.getElementById){
		return 'NS6'
	} 
	return ''
}

//-Image Swap---------------------------------------------
//
//Usage : qt_img_swap([imgName],[show|hide|down],[LayerName(optional)]])
//
//---------------------------------------------------------------
function qt_img_swap(imgName,state,LayerName) {
	state = state.toLowerCase()
	if(state == 'down'){
		var img = eval(imgName + 'down.src')
	}else if(state == 'show'){
		var img = eval(imgName + 'off.src')
	}else{
		var img = eval(imgName + 'on.src')
	}
	if(LayerName == null){
	document[imgName].src = img;
	}else{
		if(browser_type == 'IE' || browser_type == 'NS6'){
			document[imgName].src = img;
		}else{
			document.layers[LayerName].document[imgName].src = img
		}
	}
}
//-Image Replace---------------------------------------------
//
//Usage : qt_img_replace([imgName],[replace img src],[LayerName(optional)])
//
//---------------------------------------------------------------
function qt_change_img(imgName,ImgSrc,LayerName){
	if(LayerName == null){
		document[imgName].src = ImgSrc
	}else{
		if(browser_type == 'IE' || browser_type == 'NS6'){
			document[imgName].src = ImgSrc
		}else{
			document.layers[LayerName].document[imgName].src = ImgSrc
		}
	}
}

//- Image Resize ------------------------------------------
//
// Usage : qt_img_resize([imgName],[width],[height],[layername(optional)])
//
//---------------------------------------------------------------
function qt_img_resize(imgName,Width,Height,LayerName){
	if(browser_type != 'NS4' && browser_type != '' ){
		if(Width != null && Width != ''){
			document[imgName].width =Width
		}
		if(Height != null && Height != ''){
			document[imgName].height = Height
		}
	}
}

//-Move Layer -----------------------------------------------
//
//Usage : qt_move_layer([object name],[x],[y])
//
//---------------------------------------------------------------
function qt_move_layer(){
args = qt_move_layer.arguments
	if(args[0]){
		if(browser_type == 'IE'){
			objLayer = eval(args[0] + '.style')
		}else if(browser_type == 'NS6'){
			objLayer = document.getElementById(args[0]).style
		}else{
			objLayer = document.layers[args[0]]
		}
		if(args[1] != '' && args[1]){
			objLayer.left = Number(args[1]) + 'px'
		}
		if(args[2] != '' && args[2]){
			objLayer.top =  Number(args[2]) + 'px'
		}
	}
}

//-Resize Layer -----------------------------------------------
//
//Usage : qt_resize_layer([LayerName],[x],[y])
//
//---------------------------------------------------------------
function qt_resize_layer(LayerName,x,y){
	if(LayerName){
		if(browser_type == 'IE'){
			objLayer = eval(LayerName + '.style')
		}else if(browser_type == 'NS6'){
			objLayer = document.getElementById(LayerName).style
		}else{
			objLayer = document.layers[LayerName]
		}
		if(x != '' && x){
			objLayer.width = x
		}
		if(y != '' && y){
			objLayer.height = y
		}
	}
}


//-Show Layer--------------------------------------------
//
//Usage : qt_show_layer([LayerName])
//
//---------------------------------------------------------------
function qt_show_layer(obj){
	_MM_showHideLayers(obj,'','show')
}

//-Hide Layer--------------------------------------------
//
//Usage : qt_hide_layer([LayerName])
//
//---------------------------------------------------------------
function qt_hide_layer(obj){
	_MM_showHideLayers(obj,'','hide')
}

//-Set Text within Layer Value--------------------------------------------
//
//Usage : qt_set_txt_layer([LayerName],[Message])
//
//---------------------------------------------------------------
function qt_set_txt_layer(obj,msg){
	objLayer = _MM_findObj(obj);
	objLayer.innerText = msg;
}

// Private Functions--------------------------------------------------------------------------------------------
function _MM_findObj(n, d) { //v4.0
  var p,i,x;  if(!d) d=document; if((p=n.indexOf('?'))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=_MM_findObj(n,d.layers[i].document);
  if(!x && document.getElementById) x=document.getElementById(n); return x;
}

function _MM_showHideLayers() { //v3.0
  var i,p,v,obj,args=_MM_showHideLayers.arguments;
  for (i=0; i<(args.length-2); i+=3) if ((obj=_MM_findObj(args[i]))!=null) { v=args[i+2];
    if (obj.style) { obj=obj.style; v=(v=='show')?'visible':(v='hide')?'hidden':v; }
    obj.visibility=v; }
}

function _MM_reloadPage(init) {  //reloads the window if Nav4 resized
  if (init==true) with (navigator) {if ((appName=='Netscape')&&(parseInt(appVersion)==4)) {
    document.MM_pgW=innerWidth; document.MM_pgH=innerHeight; onresize=_MM_reloadPage; }}
  else if (innerWidth!=document.MM_pgW || innerHeight!=document.MM_pgH) location.reload();
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
//==

function _mt_lm_drawline(src_obj,tar_obj,imgName,divName){
				var src_pointX = Number(src_obj.style.top.substring(0,src_obj.style.top.length-2)) + (Number(src_obj.style.height.substring(0,src_obj.style.height.length-2)) / 2)
				var tar_pointX = Number(tar_obj.style.top.substring(0,tar_obj.style.top.length-2)) + (Number(tar_obj.style.height.substring(0,tar_obj.style.height.length-2)) / 2)
				if(src_pointX == tar_pointX){
					var _Sx = tar_pointX
					var _Ex = src_pointX
					_img_height = 2
					qt_change_img(imgName,'../images/lm_mid.gif')	
				}else if(src_pointX >  tar_pointX) {
					var _Sx = tar_pointX
					var _Ex = src_pointX
					var _img_height = _Ex - _Sx
					if(_img_height > 150){
						qt_change_img(imgName,'../images/lm_up_4.gif')	
					}else if(_img_height >100){
						qt_change_img(imgName,'../images/lm_up_3.gif')		
					}else if(_img_height >50 ){
						qt_change_img(imgName,'../images/lm_up_2.gif')	
					}else if(_img_height >25 ){ 
						qt_change_img(imgName,'../images/lm_up_1.gif')	
					}else if(_img_height >2 ){ 
						qt_change_img(imgName,'../images/lm_up_5.gif')	
					}else{
						qt_change_img(imgName,'../images/lm_mid.gif')	
					}
				}else{
					var _Sx = src_pointX
					var _Ex = tar_pointX
					var _img_height = _Ex - _Sx
					if(_img_height > 150){
						qt_change_img(imgName,'../images/lm_down_4.gif')	
					}else if(_img_height >100){
						qt_change_img(imgName,'../images/lm_down_3.gif')		
					}else if(_img_height >50){
						qt_change_img(imgName,'../images/lm_down_2.gif')	
					}else if(_img_height >25 ){
						qt_change_img(imgName,'../images/lm_down_1.gif')	
					}else if(_img_height >2 ){ 
						qt_change_img(imgName,'../images/lm_down_5.gif')	
					}else{
						qt_change_img(imgName,'../images/lm_mid.gif')	
					}	
				}
				qt_move_layer(divName,200,_Sx)
				qt_resize_layer(divName,200,_img_height)
				qt_img_resize(imgName,200,_img_height)
}
function MT(que_num,src_num,tar_num,ans){
	this.source = new Array()
	this.src_num = src_num
	this.tar_num = tar_num
	this.que_num = que_num
	this.answer = new LMAnswer(ans)
	this.total_score = 0;
	this.checked = false;
	
	if(src_num != null){
		var i=0
		for(i=0;i<src_num;i++){
			this.source[i] =  new MTSource(i)
			
		}
	}
	
	//Method
	this.highlight = _LMHighLight
	this.checkAnswer = _MTCheckAnswer
	
}

function MTSource(i){
	this.id = i
	this.answer = null
	this.setAnswer = _MTSetAnswer
}

function _MTSetAnswer(ans){
	this.answer = ans
}

function _MTCheckAnswer(){
	
	var i=0;
	var k=0;
	var ln =  this.src_num
	var correct = false
	for(i=0;i<ln;i++){
		//alert(this.source[i].answer);
		if(this.source[i].answer == null){
		//mark wrong
					var imgName = 'MT' + this.que_num +'_img_' + (i+1)
					var temp_src=document[imgName].src.substring(0,document[imgName].src.lastIndexOf('.'))	
					temp_src= imagePath + 'incorrect.gif'
					document[imgName].src = temp_src		
					qt_img_resize(imgName,20,20)		
		}else{
		//checking start
		var target_num = (Number(this.source[i].answer.substring(this.source[i].answer.length-1 , this.source[i].answer.length)) -1)
			for(k=0;k<this.answer.tar_ans[target_num].outcome.length;k++){
				if(this.answer.tar_ans[target_num].outcome[k].condition == (i+1)){
				//correct match
					correct = true
					this.total_score += Number(this.answer.tar_ans[target_num].outcome[k].score)
				}else{				
				}
			
			}
			if(correct){
					var imgName = 'MT' + this.que_num +'_img_' + (i+1)
					var temp_src=document[imgName].src.substring(0,document[imgName].src.lastIndexOf('.'))	
					temp_src= imagePath + 'correct.gif'
					document[imgName].src = temp_src			
					qt_img_resize(imgName,20,20)			
			}else{
					var imgName = 'MT' + this.que_num +'_img_' + (i+1)
					var temp_src=document[imgName].src.substring(0,document[imgName].src.lastIndexOf('.'))	
					temp_src= imagePath + 'incorrect.gif'
					document[imgName].src = temp_src		
					qt_img_resize(imgName,20,20)		
			}
			correct = false
		}
		
	}
	//alert(this.total_score)
	return this.total_score;
}









function LM(que_num,src_num,tar_num,ans){
	this.current_click = new ClickState
	this.source = new Array()
	this.target = new Array()
	this.src_num = src_num
	this.tar_num = tar_num
	this.que_num = que_num
	this.answer = new LMAnswer(ans)
	this.total_score = 0;
	this.checked = false;
	
	if(src_num != null){
		var i=0
		for(i=0;i<src_num;i++){
			this.source[i] =  new LMSource(i)
			
		}
	}
	if(tar_num != null){
		var i=0
		for(i=0;i<tar_num;i++){
			this.target[i] =  new LMTarget(i)
			
		}
	}	
	
	//Method
	
	this.clickSource = _LMClickSource
	this.clickTarget = _LMClickTarget
	this.reset = _LMReset
	this.drawLineMatch = _LMDrawLineMatch
	this.highlight = _LMHighLight
	this.checkAnswer = _LMCheckAnswer
	
}


function LMAnswer(ans){
	this.tar_ans = new Array
	if(ans != null){
		var temp_ans = ans.split('%')
		temp_ans = shiftArray(temp_ans)
		var i=0;
		
		for(i=0;i<temp_ans.length;i++){
			this.tar_ans[i] = new LMAnswerSet(temp_ans[i])
			
		}
	}
}

function _LMCheckAnswer(){
	if(this.checked != true){
	var i=0;
	var j=0;
	var k=0;
	var ln = this.target.length
	
	qt_resize_layer('legend' + this.que_num,200,300)
	//qt_show_layer('legend' + this.que_num)
	qt_move_layer('legend' + this.que_num ,230,120)
	
	for(i=0;i<ln;i++){
		for(j=0;j<this.target[i].result.length;j++){
			var correct = false;
			for(k=0;k<this.answer.tar_ans[i].outcome.length;k++){
				if(this.answer.tar_ans[i].outcome[k].condition == this.target[i].result[j]){
					correct = true
					this.total_score += Number(this.answer.tar_ans[i].outcome[k].score)
				}
				/*
				else{
					this.total_score -= Number(this.answer.tar_ans[i].outcome[k].score)
					if(this.total_score < 0)
						this.total_score = 0;
				}
				*/
			}
			if(correct == true){
					var imgName = 'LM' + this.que_num +'img_' +this.target[i].result[j] + '_' + (i+1)
					var temp_src=document[imgName].src.substring(0,document[imgName].src.lastIndexOf('.'))	
					temp_src+= '_c.gif'
					document[imgName].src = temp_src
			}else{
					var imgName = 'LM' + this.que_num +'img_' + this.target[i].result[j]+ '_' + (i+1)
					var temp_src=document[imgName].src.substring(0,document[imgName].src.lastIndexOf('.'))	
					temp_src+= '_w.gif'
					document[imgName].src = temp_src			

					this.total_score -= 1;			
					if(this.total_score < 0) {
						this.total_score = 0;
					}
			}
		}
	}
	this.checked = true;
	}
	//alert('totalscore:' +this.total_score)
	return this.total_score;
}

function LMAnswerSet(temp_ans){
	this.outcome =  new Array()
	this.id = temp_ans.substring(0,temp_ans.lastIndexOf('#'))
	this.score =  temp_ans.substring(temp_ans.lastIndexOf('#')+1,temp_ans.indexOf('$'))
	
	var temp = temp_ans.substring(temp_ans.indexOf('$')+1,temp_ans.length)
		temp = temp.split('$')
		var i =0;
		for(i=0;i<temp.length;i++){
			this.outcome[i] = new _LMOutCome(temp[i])
		}		
}

function _LMOutCome(temp){
	this.condition = temp.substring(0,temp.indexOf('*'))
	this.score = temp.substring(temp.indexOf('*')+1,temp.length)
}

function shiftArray(a){
	var	ln = a.length
	var i =1;
	var r = new Array()
	for(i=1;i<ln;i++){
		r[i-1] = a[i]
	}
	return r
}



function LMSource(i){
	this.id = i

}
function LMTarget(i){
	this.id = i
	this.result = new Array
	this.addresult = _LMResultAdd
}

function _LMResultAdd(src_id){
	var i =0;
	var can_add = true
	for(i=0;i<this.result.length;i++){
		if(src_id == this.result[i]){
			can_add = false;
			break;
		}
	}
	if(can_add){
		this.result[this.result.length] = src_id
	}
}

function _LMClickSource(src_id){
	if(this.current_click.type != null){
		if(this.current_click.type == 'Source'){
			this.highlight(this.current_click.id,this.current_click.type,'hide')
			this.current_click.reset()
		}else{
			this.highlight(this.current_click.id,this.current_click.type,'hide')		
			this.drawLineMatch(src_id,this.current_click.id)
			this.target[this.current_click.id-1].addresult(src_id)
			this.current_click.reset()
		}
	}else{
		this.highlight(src_id,'Source','show')	
		this.current_click.type = 'Source' 
		this.current_click.id = src_id
	}
}

function _LMClickTarget(tar_id){
	if(this.current_click.type != null){
		if(this.current_click.type == 'Target'){
			this.highlight(this.current_click.id,this.current_click.type,'hide')
			this.current_click.reset()
		}else{
			this.highlight(this.current_click.id,this.current_click.type,'hide')
			this.drawLineMatch(this.current_click.id,tar_id)
			this.target[tar_id-1].addresult(this.current_click.id)
			this.current_click.reset()
		}
	}else{
		this.highlight(tar_id,'Target','show')	
		this.current_click.type = 'Target' 
		this.current_click.id = tar_id
	}
}

function _LMHighLight(id,type,state){
	src_obj = eval('LM' + this.que_num + type + id)
	if(state == 'show'){
		src_obj.style.border = 'solid 1px'
	}else{
		src_obj.style.border = ''
	}
}

function _LMReset(){
	var i=1,j=1,k=0;
	for(i=1;i<=this.src_num;i++){
		for(j=1;j<=this.tar_num;j++){
			qt_hide_layer('LM' + this.que_num + 'img_' + i + '_' + j)
			var imgName = 'LM' + this.que_num +'img_' + i+ '_' + j
			var divName = 'LM' + this.que_num +'line_' + i+ '_' + j
			var src_obj =eval('LM' +this.que_num +'Source' +i)
			var tar_obj = eval('LM' + this.que_num + 'Target' + j)
			_mt_lm_drawline(src_obj,tar_obj,imgName,divName)			
		}
	}
	for(k=0;k<this.target.length;k++){
		this.target[k].result = new Array
	}
	this.checked = false
}

function _LMDrawLineMatch(src_id,tar_id){
	qt_show_layer('LM' + this.que_num + 'img_' + src_id + '_' + tar_id)
}

function ClickState(){
	this.type = null
	this.id = null
	this.reset = _ClickStateReset
}

function _ClickStateReset(){
	this.type = null
	this.id = null
}