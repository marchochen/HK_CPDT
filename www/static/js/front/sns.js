function Sns() {
	this.share = new SnsShare();
	this.collect = new SnsCollect();
//	this.attent = new SnsAttent();
	this.comment = new SnsComment();
	this.doing = new SnsDoing();
	this.praise = new SnsValuation();
}

function SnsDoing(){
	this.add = DoingAdd;
	this.del = DoingDel;
}
function DoingAdd(note, action, module, targetId, callback){
	$.ajax({
		url :  contextPath + '/app/doing/add',
		type : 'post',
		dataType : 'json',
		data : {
			note : note,
			action : action,
			module : module,
			targetId : targetId
		},
		success : function(result) {
			if(callback && result.status == 'success'){
				callback(result);
			}
		}
	});
}
function DoingDel(id, callback){
	var text = fetchLabel('warning_delete_notice'); //"确认删除吗？";
	Dialog.confirm({text:text, callback: function (answer) {
			if(answer){
				$.ajax({
					url: contextPath + '/app/doing/del/' + id,
					type : 'post',
					async : false,
					dataType : 'json',
					success : function(result){
						if(callback && result.status == 'success'){
							callback(result);
						}
					}
				});
			}
		}
	});
	
}


function SnsShare(){
	this.add = ShareAdd;
	this.del = ShareDel;
}

function ShareAdd(targetId, module, note, tkhId, callback){
	$.ajax({
		url :  contextPath + '/app/share/add/' + module + "/" + targetId,
		type : 'post',
		dataType : 'json',
		data : {
			note : note,
			tkhId : (tkhId && tkhId > 0) ? tkhId : ''
		},
		success : function(result) {
			if(callback && result.status == 'success'){
				callback(result);
			}
		}
	});
}

function ShareDel(id, module, callback){
	$.ajax({
		url: contextPath + '/app/share/del/' + module + "/" + id,
		type : 'post',
		async : false,
		dataType : 'json',
		success : function(result){
			if(callback && result.status == 'success'){
				callback(result);
			}
		}
	});
}

function SnsValuation(){
	this.add = PraiseAdd;
	this.cancel = PraiseCancel;
}

function PraiseAdd(targetId, module, tkhId, isComment, callback) {
	$.ajax({
		url :  contextPath + '/app/valuation/praise/' + module + "/" + targetId,
		type : 'post',
		dataType : 'json',
		data : {
			tkhId : (tkhId && tkhId > 0) ? tkhId : '',
			isComment : isComment
		},
		success : function(result) {
			if(callback && result.status == 'success'){
				callback(result);
			}
		}
	});
}

function PraiseCancel(id, module, isComment, callback){
	$.ajax({
		url: contextPath + '/app/valuation/cancel/' + module + "/" + id,
		type : 'post',
		async : false,
		dataType : 'json',
		data:{
			isComment : isComment
		},
		success : function(result){
			if(callback && result.status == 'success'){
				callback(result);
			}
		}
	});
}


function SnsComment(){
	this.add = CommentAdd;
	this.del = CommentDel;
}

function CommentAdd(targetId, replyToId, toUserId, tkhId, module, note, callback){
	if(module== 'Course' && tkhId == undefined){
		return;
	}
	$.ajax({
		url: contextPath + '/app/comment/' + module + '/' + targetId,
		type : 'post',
		data : {
			itmId : targetId,
			tkhId : tkhId,
			repalyTo : replyToId,
			note : note,
			toUserId : toUserId
		},
		async : false,
		dataType : 'json',
		success : function(result){
			if(callback && result.status == 'success'){
				callback(result);
			}
		}
	});
}

function CommentDel(id,topParentID, callback){
	var text = fetchLabel('warning_delete_notice'); //"确认删除吗？";
	Dialog.confirm({text:text, callback: function (answer) {
			if(answer){
				$.ajax({
					url: contextPath + '/app/comment/del/' + id+'/'+topParentID,
					type : 'post',
					async : false,
					dataType : 'json',
					success : function(result){
						if(callback && result.status == 'success'){
							callback(result);
						}
					}
				});
			}
		}
	});
}

//收藏
function SnsCollect(){
	this.add = CollectAdd;
	this.cancel = CollectDel;
}

function CollectAdd(targetId, module, tkhId, callback){
	$.ajax({
		url :  contextPath + '/app/collect/add/' + module + "/" + targetId,
		type : 'post',
		dataType : 'json',
		data : {
			tkhId : (tkhId && tkhId > 0) ? tkhId : ''
		},
		success : function(result) {
			if(callback && result.status == 'success'){
				callback(result);
			}
		}
	});
}

function CollectDel(id, module, callback){
	$.ajax({
		url: contextPath + '/app/collect/cancel/' + module + "/" + id,
		type : 'post',
		async : false,
		dataType : 'json',
		success : function(result){
			if(callback && result.status == 'success'){
				callback(result);
			}
		}
	});
}
