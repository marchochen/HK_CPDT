function MM_dragLayer(objName,x,hL,hT,hW,hH,toFront,dropBack,cU,cD,cL,cR,targL,targT,tol,dropJS,et,dragJS) { //v3.0
  //Copyright 1998 Macromedia, Inc. All rights reserved.
  var i,j,aLayer,retVal,curDrag=null,NS=(navigator.appName=='Netscape'), curLeft, curTop;
 // if (!document.all && !document.layers) return false;
  retVal = true; if(!NS && event) event.returnValue = true;
  if (MM_dragLayer.arguments.length > 1) {
    curDrag = MM_intFindObject(objName); if (!curDrag) return false;
  //  if (!document.allLayers) { document.allLayers = new Array();
    //  with (document) if (NS) { for (i=0; i<layers.length; i++) allLayers[i]=layers[i];
     //   for (i=0; i<allLayers.length; i++) if (allLayers[i].document && allLayers[i].document.layers)
     //     with (allLayers[i].document) for (j=0; j<layers.length; j++) allLayers[allLayers.length]=layers[j];
    //  } else for (i=0;i<all.length;i++) if (all[i].style&&all[i].style.position) allLayers[allLayers.length]=all[i];}
	if (!document.allLayers) {
      		document.allLayers = new Array();
      		for (i=0;i<document.all.length;i++) 
      			if (document.all[i].style&&document.all[i].style.position)
      				document.allLayers[document.allLayers.length]=document.all[i];
      	}
    curDrag.MM_dragOk=true; curDrag.MM_targL=targL; curDrag.MM_targT=targT;
    curDrag.MM_tol=Math.pow(tol,2); curDrag.MM_hLeft=hL; curDrag.MM_hTop=hT;
    curDrag.MM_hWidth=hW; curDrag.MM_hHeight=hH; curDrag.MM_toFront=toFront;
    curDrag.MM_dropBack=dropBack; curDrag.MM_dropJS=dropJS;
    curDrag.MM_everyTime=et; curDrag.MM_dragJS=dragJS;
   // curDrag.MM_oldZ = (NS)?curDrag.zIndex:curDrag.style.zIndex;
    curDrag.MM_oldZ = curDrag.style.zIndex;
    curLeft= (NS)?curDrag.left:curDrag.style.pixelLeft; curDrag.MM_startL = curLeft;
    curTop = (NS)?curDrag.top:curDrag.style.pixelTop; curDrag.MM_startT = curTop;
    curDrag.MM_bL=(cL<0)?null:curLeft-cL; curDrag.MM_bT=(cU<0)?null:curTop -cU;
    curDrag.MM_bR=(cR<0)?null:curLeft+cR; curDrag.MM_bB=(cD<0)?null:curTop +cD;
    curDrag.MM_LEFTRIGHT=0; curDrag.MM_UPDOWN=0; curDrag.MM_SNAPPED=false; //use in your JS!
    document.onmousedown = MM_dragLayer; document.onmouseup = MM_dragLayer;
    if (NS) document.captureEvents(Event.MOUSEDOWN|Event.MOUSEUP);
  } else {
    var theEvent = ((NS)?objName.type:event.type);
    if (theEvent == 'mousedown') {
	if(NS){
		_nsobj = objName.target;
		while (_nsobj.tagName != 'DIV' && _nsobj.tagName != 'BODY'){
			_nsobj = _nsobj.offsetParent;
		}
		if(_nsobj.tagName == 'DIV'){
			_obj = _nsobj;
			//alert('####'+_obj.id)
			layer_name = 'layer' + currentQue
			mouseX = parseInt(_obj.style.left)+parseInt(document.all[layer_name].style.left)+parseInt(objName.layerX);
			mouseY = parseInt(_obj.style.top) +parseInt(document.all[layer_name].style.top) +parseInt(objName.layerY); 
		}	 
	}
	if(document.all){
	idx = window.event.srcElement.sourceIndex
	
	
	while (document.all(idx).tagName != 'DIV' && document.all(idx).tagName != 'BODY' && idx > 0 )
		idx--;	

	if (document.all(idx).tagName == 'DIV' && document.all(idx).id != '' ){
		_obj = document.all(idx).id
		_obj = eval(_obj)
		layer_name = 'layer' + currentQue

		mouseX = parseInt((NS)?_obj.pageX:_obj.style.left) +parseInt((NS)?document.layers[layer_name].pageX:document.all[layer_name].style.left) + parseInt((NS)? event.layerX:event.offsetX)
		mouseY = parseInt((NS)?_obj.pageY:_obj.style.top) + parseInt((NS)?document.layers[layer_name].pageY:document.all[layer_name].style.top)+ parseInt((NS)? event.layerY:event.offsetY); 
	}
	
	}else{
  		var mouseX = (NS)?objName.pageX : event.clientX + document.body.scrollLeft;
     	var mouseY = (NS)?objName.pageY : event.clientY + document.body.scrollTop;
	}

	
      var maxDragZ=null; document.MM_maxZ = 0;
      for (i=0; i<document.allLayers.length; i++) { aLayer = document.allLayers[i];
        //var aLayerZ = (NS)?aLayer.zIndex:aLayer.style.zIndex;
        var aLayerZ = aLayer.style.zIndex;
        if (aLayerZ > document.MM_maxZ) document.MM_maxZ = aLayerZ;
      //  var isVisible = (((NS)?aLayer.visibility:aLayer.style.visibility).indexOf('hid') == -1);
	var isVisible = (aLayer.style.visibility).indexOf('hid') == -1;	
		
        if (aLayer.MM_dragOk != null && isVisible) with (aLayer) {
      
		  var parentL=0; var parentT=0;
          if (NS)  parentLayer = aLayer.offsetParent; 
          else	parentLayer = aLayer.parentElement;
          while (parentLayer != null && parentLayer.style.position) {
              parentL += parentLayer.offsetLeft; parentT += parentLayer.offsetTop;
              if(NS) parentLayer = parentLayer.offsetParent; 
              else parentLayer = parentLayer.parentElement; 
           } 
          var tmpX=mouseX-(((NS)?offsetLeft:style.pixelLeft)+parentL+MM_hLeft);
          var tmpY=mouseY-(((NS)?offsetTop:style.pixelTop) +parentT+MM_hTop);
          var tmpW = MM_hWidth;  if (tmpW <= 0) tmpW += offsetWidth;
          var tmpH = MM_hHeight; if (tmpH <= 0) tmpH += offsetHeight;
          if ((0 <= tmpX && tmpX < tmpW && 0 <= tmpY && tmpY < tmpH) && (maxDragZ == null 
              || maxDragZ <= aLayerZ)) { curDrag = aLayer; maxDragZ = aLayerZ; } } }
      if (curDrag) {
        document.onmousemove = MM_dragLayer; if (NS) document.captureEvents(Event.MOUSEMOVE);
        curLeft = (NS)?curDrag.offsetLeft:curDrag.style.pixelLeft;
        curTop = (NS)?curDrag.offsetTop:curDrag.style.pixelTop;
        MM_oldX = mouseX - curLeft; MM_oldY = mouseY - curTop;
        document.MM_curDrag = curDrag;  curDrag.MM_SNAPPED=false;
        if(curDrag.MM_toFront) {
          //eval('curDrag.'+((NS)?'':'style.')+'zIndex=document.MM_maxZ+1');
          curDrag.style.zIndex=Number(document.MM_maxZ)+1;
          if (!curDrag.MM_dropBack) document.MM_maxZ++; }
        retVal = false; if(!NS) event.returnValue = false;
    } } else if (theEvent == 'mousemove') {
      if (document.MM_curDrag) with (document.MM_curDrag) {
      	if(NS){
		_nsobj = objName.target;
		while (_nsobj.tagName != 'DIV' && _nsobj.tagName != 'BODY'){
			_nsobj = _nsobj.offsetParent;
		}
		if(_nsobj.tagName == 'DIV'){
			_obj = _nsobj;
			//alert('####'+_obj.id)
			layer_name = 'layer' + currentQue
			mouseX = parseInt(_obj.style.left)+parseInt(document.all[layer_name].style.left)+parseInt(objName.layerX);
			mouseY = parseInt(_obj.style.top) +parseInt(document.all[layer_name].style.top) +parseInt(objName.layerY); 
		}	 
	}
	  if(document.all){
	  	idx = window.event.srcElement.sourceIndex
		found = false
	
		while (document.all(idx).tagName != 'DIV' && document.all(idx).tagName != 'BODY' && idx > 0 )
			idx--;	

		if (document.all(idx).tagName == 'DIV' && document.all(idx).id != ''){
			_obj = document.all(idx).id
			_obj = eval(_obj)
			layer_name = 'layer' + currentQue

			mouseX = parseInt((NS)?_obj.pageX:_obj.style.left) +parseInt((NS)?document.layers[layer_name].pageX:document.all[layer_name].style.left) + parseInt((NS)? event.layerX:event.offsetX)
			mouseY = parseInt((NS)?_obj.pageY:_obj.style.top) + parseInt((NS)?document.layers[layer_name].pageY:document.all[layer_name].style.top)+ parseInt((NS)? event.layerY:event.offsetY); 
		}
		
		}else{
  			var mouseX = (NS)?objName.pageX : event.clientX + document.body.scrollLeft;
  		   	var mouseY = (NS)?objName.pageY : event.clientY + document.body.scrollTop;
		}

      

	    newLeft = mouseX-MM_oldX; newTop  = mouseY-MM_oldY;
        if (MM_bL!=null) newLeft = Math.max(newLeft,MM_bL);
        if (MM_bR!=null) newLeft = Math.min(newLeft,MM_bR);
        if (MM_bT!=null) newTop  = Math.max(newTop ,MM_bT);
        if (MM_bB!=null) newTop  = Math.min(newTop ,MM_bB);
        MM_LEFTRIGHT = newLeft-MM_startL; MM_UPDOWN = newTop-MM_startT;
        if (NS) {style.left = newLeft; style.top = newTop;}
        else {style.pixelLeft = newLeft; style.pixelTop = newTop;}
        if (MM_dragJS) eval(MM_dragJS);
        retVal = false; if(!NS) event.returnValue = false;
    } } else if (theEvent == 'mouseup') {
      document.onmousemove = null;
      if (NS) document.releaseEvents(Event.MOUSEMOVE);
      if (NS) document.captureEvents(Event.MOUSEDOWN); //for mac NS
      if (document.MM_curDrag) with (document.MM_curDrag) {
        if (typeof MM_targL =='number' && typeof MM_targT == 'number' &&
            (Math.pow(MM_targL-((NS)?style.left:style.pixelLeft),2)+
             Math.pow(MM_targT-((NS)?style.top:style.pixelTop),2))<=MM_tol) {
          if (NS) {style.left = MM_targL;style.top = MM_targT;}
          else {style.pixelLeft = MM_targL; style.pixelTop = MM_targT;}
          MM_SNAPPED = true; MM_LEFTRIGHT = MM_startL-MM_targL; MM_UPDOWN = MM_startT-MM_targT; }
        if (MM_everyTime || MM_SNAPPED) eval(MM_dropJS);
     //   if(MM_dropBack) {if (NS) zIndex = MM_oldZ; else style.zIndex = MM_oldZ;}
      if(MM_dropBack) { style.zIndex = MM_oldZ;}
        retVal = false; if(!NS) event.returnValue = false; } 
      document.MM_curDrag = null;
    }
    if (NS) document.routeEvent(objName);
  } return retVal;
}
