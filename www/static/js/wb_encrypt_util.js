
var wbEncrytor = function() {
   var obj = new Object();
   obj.CWN_ENC_KEY_1 = 157862245; 
   obj.CWN_ENC_KEY_2 = 32211; 
   obj.CWN_ENC_SEPARATOR = "_"; 
   
   obj.cwnEncrypt = function( id ) {
	   var enc_id = String(Number(id) + this.CWN_ENC_KEY_1) + this.CWN_ENC_SEPARATOR + String(this.CWN_ENC_KEY_1 - Number(id) - this.CWN_ENC_KEY_2);
	   return enc_id;
   }
   
   obj.cwnDecrypt = function(enc_id){
	  var ids = splitToLong(enc_id,this.CWN_ENC_SEPARATOR);
	  var decr_id = 0;
	  if(null!=ids && ids.length==2){
		  var key_1 = ids[0];
		  var key_2 = ids[1];
		  
		  key_1 = key_1-this.CWN_ENC_KEY_1;
		  var temp_key = key_2 + key_1 + this.CWN_ENC_KEY_2;
		  if(temp_key == this.CWN_ENC_KEY_1){
			  decr_id = key_1;
		  }else{
			  decr_id = Number(enc_id);
		  }
	  }
	  return decr_id;
   }
   
   function splitToLong(encryptStr,delimiter){
	   var res = new Array();
	   if(null!=encryptStr && typeof(encryptStr)!="undefined"){
		   var q = encryptStr.split(delimiter);
		   for(var i = 0 ;i<q.length;i++){
			   res[i]=Number(q[i]);
		   }
	   }else{
		   res[0] = 0;
	   }
	   return res;
   }
   
   return obj;
}
