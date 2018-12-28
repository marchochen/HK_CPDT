WebFXCookie.prototype.setCookie = function setCookie(sName, sValue, nDays) {
};


webFXTreeHandler.handleEvent = function (e) {
		var el = e.target || e.srcElement;
		if((el.type == 'checkbox' || el.type=='radio') && e.type == 'mousedown'){
				el.checked = !el.checked;
			}
		while (el != null && !this.all[el.id]) {
			el = el.parentNode;
		}
		if (el == null) {
			return false;
		}	
		var node = this.all[el.id];
		if (typeof node["_on" + e.type] == "function") {
			return node["_on" + e.type](e);
		}
		return false;
	};

webFXTreeHandler.textToHtml = function (s) {
		//alert("wizbTree.js");
		return String(s).replace(/&|<|>|\n|\"/g, this._textToHtml);
	};

webFXTreeHandler._textToHtml= function (s) {
		switch (s) {
			case "&":
				return "&amp;";
			case "<":
				return "&lt;";
			case ">":
				return "&gt;";
			case "\n":
				return "<BR>";
			case "\"":
				return "&quot;";	// so we can use this in attributes
			default:
				//return "&nbsp;";
		}
	};


webFXTreeConfig.setSkinImage = function(imagePath) {
		 if(imagePath == '' || imagePath.length == 0){
	 		imagePath = 'images/';
	 	}
	 	if(imagePath.charAt(imagePath.length-1)!="/"){
	 		imagePath = imagePath + "/";
	 	}
	 	webFXTreeConfig.imagePath       = imagePath;
		webFXTreeConfig.loadingText 	= "Loading...";
		webFXTreeConfig.loadingIcon 	= imagePath+"loading.gif";	 	
		webFXTreeConfig.rootIcon        = imagePath+"folder.png";
		webFXTreeConfig.openRootIcon    = imagePath+"openfolder.png";
		webFXTreeConfig.folderIcon      = imagePath+"folder.png";
		webFXTreeConfig.openFolderIcon  = imagePath+"openfolder.png";
		webFXTreeConfig.fileIcon        = imagePath+"file.png";
		webFXTreeConfig.iIcon           = imagePath+"I.png";
		webFXTreeConfig.lIcon           = imagePath+"L.png";
		webFXTreeConfig.lMinusIcon      = imagePath+"Lminus.png";
		webFXTreeConfig.lPlusIcon       = imagePath+"Lplus.png";
		webFXTreeConfig.tIcon           = imagePath+"T.png";
		webFXTreeConfig.tMinusIcon      = imagePath+"Tminus.png";
		webFXTreeConfig.tPlusIcon       = imagePath+"Tplus.png";
		webFXTreeConfig.plusIcon        = imagePath+"plus.png";
		webFXTreeConfig.minusIcon       = imagePath+"minus.png";
		webFXTreeConfig.tcFolderIcon      = imagePath+"folder_tc.gif";
		webFXTreeConfig.tcOpenFolderIcon  = imagePath+"openfolder_tc.gif";
		webFXTreeConfig.blankIcon       = imagePath+"blank.png";
	};




function wizbTree(sText, sXmlSrc, oAction, sBehavior, sIcon, sOpenIcon) {
	WebFXLoadTree.call(this, sText, sXmlSrc, oAction, sBehavior, sIcon, sOpenIcon);
};

wizbTree._maxitemindex = 0;

wizbTree.setMaxItemIndex = function setMaxItemIndex(item_index){
	//item_index format is "ITEM??"
	if(item_index){
		var index = parseInt(item_index.substring(4,item_index.length));
		
		//alert(this._maxitemindex + " : " + index + "==>" + (this._maxitemindex < index));
		if(this._maxitemindex < index){
			this._maxitemindex = index;
		}
	}
};

wizbTree.getMaxItemIndex = function getMaxItemIndex(){
	return this._maxitemindex;
};

_p = wizbTree.prototype = new WebFXLoadTree();

_p._enableCheckBox = false;
_p._enableRadio = false;
_p._enableNodeBox = true;
_p._enableRootBox = true;


_p.getAllSelected = function getAllSelected(){
	var t = this.getTree();
	var str = "";	
	if(t._enableCheckBox || t._enableRadio){
		//alert(t.getText());
		var element_lst = document.getElementsByName("input_"+t.getId());
		//alert(element_lst.length);
		for(i=0;i<element_lst.length;i++){
			var ele = element_lst[i];
			if((ele.type == 'checkbox' || ele.type == 'radio') && ele.checked){
				if(str.length == 0){
			  	str = ele.value ;
				}else {
					str = str + ":_:_:" + ele.value;
				}
			}	
		}
	} else {
		var node = this.getSelected();
		if(node){
			var att = node.attributes;
			var pickable = true;
			if(att){
			   pickable = att["pickable"]=="NO"?false:true;
			} 
			if(this.getSelected() && (this._enableRootBox || this.getSelected()!=t) && pickable){
				str = this.getSelected().getSrcId() + ","+this.getSelected().getText();	
			}
		}
	}
	return str;	
	
}

_p.setEnableRootBox = function setEnableRootBox(isEnable) {
	this._enableRootBox = isEnable;
};

_p.getEnableRootBox = function getEnableRootBox() {
	return this._enableRootBox;
};

_p.setEnableNodeBox = function setEnableNodeBox(isEnable) {
	this._enableNodeBox = isEnable;
};

_p.getEnableNodeBox = function getEnableNodeBox() {
	return this._enableNodeBox;
};

_p.setEnableCheckBox = function setEnableCheckBox(isEnable) {
	this._enableCheckBox = isEnable;
	if(isEnable){
		this._enableRadio = false;
	}
};

_p.getEnableCheckBox = function getEnableCheckBox(isEnable) {
	return this._enableCheckBox;
};

_p.setEnableRadio = function setEnableRadio(isEnable) {
	this._enableRadio = isEnable;
	if(isEnable){
		this._enableCheckBox = false;
	}	
};

_p.getEnableRadio = function getEnableRadio(isEnable) {
	return this._enableRadio;
};

function wizbTreeItem(sText, sXmlSrc, oAction, eParent, sIcon, sOpenIcon,sSrcId) {
	WebFXLoadTreeItem.call(this, sText, sXmlSrc, oAction, eParent, sIcon, sOpenIcon);
	this.srcid = sSrcId;
}
_p = wizbTreeItem.prototype = new WebFXLoadTreeItem();

_p.setSrcId = wizbTree.prototype.setSrcId = function setSrcId(sId){
	var el = this.getElement();
	webFXTreeHandler.removeNode(this);
	this.srcid = sId;
	if (el) {
		el.srcid = sId;
	}
	webFXTreeHandler.addNode(this);	
};

_p.getSrcId = wizbTree.prototype.getSrcId = function getSrcId(){
	return this.srcid;
};
// HTML generation

_p.toHtml = wizbTree.prototype.toHtml = function toHtml() {
	var sb = [];
	var cs = this.childNodes;
	var l = cs.length;
	var t = this.getTree();
	for (var y = 0; y < l; y++) {
		sb[y] = cs[y].toHtml();
	}

	var t = this.getTree();
	var hideLines = !t.getShowLines() || t == this.parentNode && !t.getShowRootLines();

	return "<div   class=\"webfx-tree-item\" id=\"" +
		this.id + "\"" + this.getEventHandlersHtml() + ">" +
		this.getRowHtml() + 
		"<div     class=\"webfx-tree-children" +
		(hideLines ? "-nolines" : "") + "\" style=\"" +
		this.getLineStyle() +
		(this.getExpanded() && this.hasChildren() ? "" : "display:none;") +
		"\">" +
		sb.join("") +
		"</div></div>";
};

_p.getRowHtml = wizbTree.prototype.getRowHtml = function getRowHtml() {
	var t = this.getTree();
	var box = this.getInputHtml();
//alert(this.getText());
	return "<div class=\"" + this.getRowClassName() + "\" style=\"padding-left:" +
		Math.max(0, (this.getDepth() - 1) * this.indentWidth) + "px\">" +
		this.getExpandIconHtml() +
		//"<span class=\"webfx-tree-icon-and-label\">" +
		this.getIconHtml() + box+ this.getLabelHtml() +
		//"</span>" +
		"</div>";
};

_p.getInputHtml = wizbTree.prototype.getInputHtml = function getInputHtml() {
	 var t = this.getTree();
	 var box = "";
	 //alert("InputHtml");
	 var att = webFXTreeHandler.getNodeById(this.getId()).attributes;
	 var pickable = true;
	 var isFolder = true;
	 var show = false;
	 if(att){
	 	isFolder = att["is_folder"] == "YES"?true:false;
		pickable = att["pickable"]=="NO"?false:true;
	 }  
	if(this.getParent()) {
		if((isFolder && t._enableNodeBox) || !isFolder) {
	 		show = true;
		}
	} else if(t._enableRootBox) {
		show = true;
	}
	
	if(show) {
		if(t._enableCheckBox && pickable){
			box = this.getInputElementHtml("checkbox");
		}
		if(t._enableRadio && pickable){
			box = this.getInputElementHtml("radio");	
		}
	}
	 return box;				
};

_p.getInputElementHtml = wizbTree.prototype.getInputElementHtml = function getInputElementHtml(input_type){
	var node_id= this.getSrcId();
	var obj = document.getElementById("input_"+this.getId());
	var checked = false;
	if(obj){
		checked = obj.checked;
		//alert(this.getText()+":"+obj.checked);
	}
	
	var str = "<input id=\"input_"+this.getId()+"\" type=\""+input_type+"\"" + 
	" class=\"tree-check-box\" name=\"input_"+this.getTree().getId()+"\"" +
	(checked ? " checked=\"checked\"" : "") +
	" onclick=\"webFXTreeHandler.handleEvent(event)\" value=\""+node_id+","+this.text+"\"" +
	" />";
	//alert(this.getId());
	return str;
};


// XML generation
_p.toXML = wizbTree.prototype.toXML = function toXML(exclusion_id){
	var node = [];
	var child = this.childNodes;
	var l = child.length;
	var t = this.getTree();
    t._maxitemindex = 0;
	//alert(l);
	var xml = "<tableofcontents identifier=\"TOC1\" title=\""+this.text+"\">";
	for (var x = 0; x < l; x++) {
		//alert(x+":"+(child[x].constructor)+"\n"+child[x].getText());
		xml += child[x].getNodeXML(exclusion_id);
	}
	
	xml += "</tableofcontents>";
	return xml;
};

_p.getNodeXML = wizbTree.prototype.getNodeXML = function getNodeXML(exclusion_id) {
	var sb = [];
	var cs = this.childNodes;
	var l = cs.length;
	//var t = this.getTree();
	//t.setMaxItemIndex(this.attributes.identifier);
	var node_xml = "";
	if(this.id != exclusion_id){
		node_xml = "<item identifier=\""+this.attributes.identifier+"\"";
		//alert(this.getText()+":"+this.srcid);
		if(this.srcid){
			node_xml +=" identifierref=\""+this.srcid+"\"";
		}
		node_xml+=" title=\""+webFXTreeHandler.textToHtml(this.attributes.title)+"\">"+
				"<itemtype>"+this.attributes.itemtype+"</itemtype>";
		if(this.attributes.restype){
			node_xml += "<restype>"+this.attributes.restype+"</restype>";
		}	
		for (var y = 0; y < l; y++) {
			node_xml += cs[y].getNodeXML(exclusion_id);
		}
	
		node_xml +="</item>";
	}
	//alert(node_xml);
	return node_xml;
};

_p.moveNodeUp= wizbTree.prototype.moveNodeUp = function moveNodeUp() {
	var t = this.getTree();
	var src_node = this;
	var target_node_parent = this.getParent();
	if(target_node_parent){
		if(src_node.getPreviousSibling()){
			//alert(target_node_parent.getFirstChild().getText() + "\n pre:" +src_node.getPreviousSibling().getText());
			target_node_parent.add(src_node,src_node.getPreviousSibling());	
		} 
		t.setSelected(src_node);		
	}	
};

_p.moveNodeDown= wizbTree.prototype.moveNodeDown = function moveNodeDown() {
	var t = this.getTree();
	var src_node = this;
	var target_node_parent = this.getParent();
	if(target_node_parent){		
		if(src_node.getNextSibling() 
		  && src_node.getNextSibling().getNextSibling()
			&& src_node.getNextSibling() != src_node.getNextSibling().getNextSibling()
			&& target_node_parent.getLastChild()!= src_node
			){
			target_node_parent.add(src_node,src_node.getNextSibling().getNextSibling());	
		} 
		else{ 
			if(target_node_parent.getLastChild()!= src_node){
				//alert(target_node_parent.getLastChild().getText()+"\n pre:"+src_node.getPreviousSibling().getText()  + "\n next:" 
				//		+src_node.getNextSibling().getText()+"\n ");
				target_node_parent.add(src_node,null); 
				src_node.nextSibling = null;
			}
		}
		t.setSelected(src_node);
	}			
};

_p.dynamicExpandAll = wizbTree.prototype.dynamicExpandAll = function dynamicExpandAll(str_caller_name){
	if(!this.getExpanded()){
		this.setExpanded(true);
	}
	var t = this.getTree();
	if (t.getCreated()){
		this.expandAll(); 
		return;
		//window.clearInterval(timer);
	}else{
		var fun_str = str_caller_name+".dynamicExpandAll('"+str_caller_name+"');";
        //timer = window.setInterval(fun_str,10);
        window.setTimeout(fun_str,10);
	}	
};

// Event handlers

_p._onmousedown = function _onmousedown(e) {
	var el = e.target || e.srcElement;
	// expand icon
	if (/webfx-tree-expand-icon/.test(el.className) && this.hasChildren()) {
		this.toggle();
		if (webFXTreeHandler.ie) {
			//window.setTimeout("WebFXTreeAbstractNode._onTimeoutFocus(\"" + this.id + "\")", 10);
		}
		return false;
	}

	this.select();
	if (/*!/webfx-tree-item-label/.test(el.className) && */!webFXTreeHandler.opera)	{ // opera cancels the click if focus is called
		
		// in case we are not clicking on the label
		if (webFXTreeHandler.ie) {
			window.setTimeout("WebFXTreeAbstractNode._onTimeoutFocus(\"" + this.id + "\")", 10);
		} else {
			this.focus();
		}
	}
	var rowEl = this.getRowElement();
	if (rowEl) {
		rowEl.className = this.getRowClassName();
	}

	return false;
};

_p._onkeydown = wizbTree.prototype._onkeydown = function _onkeydown(e) {
	var n;
	var rv = true;
	switch (e.keyCode) {
		case 39:	// RIGHT
			if (e.altKey) {
				rv = true;
				break;
			}
			if (this.hasChildren()) {
				if (!this.getExpanded()) {
					this.setExpanded(true);
				} else {
					n = this.getFirstChild();
					n.focus();
				}
			}
			rv = false;
			break;
		case 37:	// LEFT
			if (e.altKey) {
				rv = true;
				break;
			}
			if (this.hasChildren() && this.getExpanded()) {
				this.setExpanded(false);
			} else {
				var p = this.getParent();
				var t = this.getTree();
				// don't go to root if hidden
				if (p && (t.showRootNode || p != t)) {
					n = p;
					n.focus();
				}
			}
			rv = false;
			break;

		case 40:	// DOWN
			n = this.getNextShownNode();
			if (n) {
				n.focus();
			}
			rv = false;
			break;
		case 38:	// UP
			n = this.getPreviousShownNode()
			if (n) {
				n.focus();
			}
			rv = false;
			break;
	}
	if(n){
		//alert(typeof n.getAction());
		eval(n.getAction())
	}
	if (!rv && e.preventDefault) {
		e.preventDefault();
	}
	e.returnValue = rv;
	return rv;
};


WebFXLoadTree.createItemFromElement = function (oNode) {
	var jsAttrs = {};
	var domAttrs = oNode.attributes;
	var i, l;

	l = domAttrs.length;
	for (i = 0; i < l; i++) {
		if (domAttrs[i] == null) {
			continue;
		}
		jsAttrs[domAttrs[i].nodeName] = domAttrs[i].nodeValue;
	}

	var name, val;
	for (i = 0; i < WebFXLoadTree._attrs.length; i++) {
		name = WebFXLoadTree._attrs[i];
		value = oNode.getAttribute(name);
		if (value) {
			jsAttrs[name] = value;
		}
	}

	var action;
	if (jsAttrs.onaction) {
		action = new Function(jsAttrs.onaction);
	} else if (jsAttrs.action) {
		action = jsAttrs.action;
	}
	var jsNode = new wizbTreeItem(jsAttrs.html || "", jsAttrs.src, action,
									   null, jsAttrs.icon, jsAttrs.openIcon, jsAttrs.srcid);
	if (jsAttrs.text) {
		jsNode.setText(jsAttrs.text);
	}

	if (jsAttrs.target) {
		jsNode.target = jsAttrs.target;
	}
	if (jsAttrs.id) {
		jsNode.setId(jsAttrs.id);
	}
	if (jsAttrs.srcid) {
		jsNode.setSrcId(jsAttrs.srcid);
	}	
	if (jsAttrs.toolTip) {
		jsNode.toolTip = jsAttrs.toolTip;
	}
	if (jsAttrs.expanded) {
		jsNode.setExpanded(jsAttrs.expanded != "false");
	}
	if (jsAttrs.onload) {
		jsNode.onload = new Function(jsAttrs.onload);
	}
	if (jsAttrs.onerror) {
		jsNode.onerror = new Function(jsAttrs.onerror);
	}
	//alert("1."+eval(jsAttrs.onsetsrc));
	if (jsAttrs.onsetsrc) {
		jsNode.src = eval(jsAttrs.onsetsrc);
		//alert("2."+jsNode.src);
		//alert("3."+jsNode.onsetsrc);
	}
	if (jsAttrs.is_folder && jsAttrs.is_folder == "YES") {
		if(jsAttrs.node_type && jsAttrs.node_type == "TC") {
			jsNode.icon = webFXTreeConfig.tcFolderIcon;	
			jsNode.openIcon = webFXTreeConfig.tcOpenFolderIcon;
		} else {
			jsNode.icon = webFXTreeConfig.folderIcon;
			if(jsAttrs.src || (jsAttrs.has_child && jsAttrs.has_child=="YES")){
				jsNode.openIcon = webFXTreeConfig.openFolderIcon;
			}else{
				jsNode.openIcon = webFXTreeConfig.folderIcon;
			}			
		}
	}	
	
	jsNode.attributes = jsAttrs;
	
	wizbTree.setMaxItemIndex(jsNode.attributes.identifier);

	// go through childNodes
	var cs = oNode.childNodes;
	l = cs.length;
	for (i = 0; i < l; i++) {
		if (cs[i].tagName == "tree") {
			jsNode.add(WebFXLoadTree.createItemFromElement(cs[i]));
		}
	}

	return jsNode;
};









	

