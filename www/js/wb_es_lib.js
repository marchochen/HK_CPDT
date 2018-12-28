function es_add_file_field(id,pos,lang){
	//Find Question span object
	var spanObj = document.getElementById("es_file_" + id)
	if(spanObj.childNodes.length){
		if(spanObj.childNodes[spanObj.childNodes.length-1].childNodes[0].value == ""){
			alert(wb_msg_use_exist_attach_box);
			return;
		}
	}		
	//Define HTML tag
	var fileHTML 	= '<input class="InputFrm file_file" type="file" size="87" onchange="qset.getQuestion(' + pos +').getInteraction(1).setFileAttach(this.value);showValue(this);" onkeydown="this.blur()" name="file_'+ id + '_' + (spanObj.childNodes.length+1) + '" style="display:inline;">' + '<input value="'+fetchLabel("no_select_file")+'" class="file_txt"><div class="file_button-blue">'+fetchLabel("usr_browse")+'</div>'
	if(lang!=undefined&&lang=='en')
	{
	   var rmbtnHTML 	= '<input type="button" value="' + wb_msg_es_box_remove + '" onclick="es_remove_file_field(this,' + id + ',' + pos +')" name="' + id + '_' + (spanObj.childNodes.length+1) + '" class="file_button-blue" style="margin:0 -75px 0 0;" />'
	}else{
		var rmbtnHTML 	= '<input type="button" value="' + wb_msg_es_box_remove + '" onclick="es_remove_file_field(this,' + id + ',' + pos +')" name="' + id + '_' + (spanObj.childNodes.length+1) + '" class="file_button-blue" style="margin:0 -50px 0 0;" />'
	}
			
	
	var imgHTML 	= '<img src="' + wb_image_path + 'tp.gif" border="0" width="2" height="2">'		
	var brHTML 		= '<br>'		
	//Create a span tag, and put those HTML tag inside
	var spanElement = document.createElement("div");
		spanElement.setAttribute("id","span_" + id +"_"+  (spanObj.childNodes.length+1))	
		spanElement.setAttribute("style","margin: 10px 0 0 0;")
		//alert(fileHTML)
		spanElement.innerHTML = fileHTML + imgHTML + rmbtnHTML + brHTML;
		spanElement.className = "psdmess file";
	//Write element into document	
		spanObj.appendChild(spanElement);
	
}

function es_remove_file_field(obj,id,pos){
	//Remove File Field
	var spanElement = document.getElementById("span_" + obj.name);
	var spanObj = spanElement.parentNode
	try {
		spanElement.removeNode(true);
	} catch (e) {
		spanObj.removeChild(spanElement);
	}
	//Sort and Rename File Field Name again
	for(var i =0 ;i<spanObj.childNodes.length;i++){
		spanObj.childNodes[i].childNodes[0].name = 'file_'+ id + '_' + (i+1)
	}
	if(spanObj.childNodes.length == 0){
		qset.getQuestion(pos).getInteraction(1).setFileAttach('')
	}
}
function showValue(obj){
	var ns=obj.nextElementSibling;
	ns.value = obj.value;
}