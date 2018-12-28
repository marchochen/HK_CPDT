Validate = {
	isGtEqZero : function(val) {
		var res = /^(0|[1-9][0-9]*)$/;
		var re = new RegExp(res);
		return re.test(val);
	},
	isDouble : function(val){
		var res = /^\d+\.?\d+$|^\d+$/;
		var re = new RegExp(res);
		return re.test(val);
	}
};
