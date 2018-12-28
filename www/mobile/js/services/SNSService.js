appModule.service('SNS',['Ajax',function(Ajax){
	return {
		addLike : function(targetId, module, tkhId, isComment, callback){
			var url = '/app/valuation/praise/' + module + '/' + targetId;
			var params = {
				tkhId : (tkhId && tkhId > 0) ? tkhId : '',
				isComment : isComment
			};
			Ajax.post(url, params, function(data){
				if(appInd){
					switch(module){
						case 'Article' :
							plus.webview.currentWebview().opener().evalJS("changeTotal('nal_" + targetId + "','add')");
							plus.webview.currentWebview().opener().evalJS("changeTotal('pal_" + targetId + "','add')");
							break;
						case 'Course' :
							plus.webview.currentWebview().opener().evalJS("changeTotal('fcl_" + targetId + "','add')");
							plus.webview.currentWebview().opener().evalJS("changeTotal('scl_" + targetId + "','add')");
							plus.webview.currentWebview().opener().evalJS("changeTotal('tcl_" + targetId + "','add')");
							plus.webview.all()[1].evalJS("changeTotal('ocl_" + targetId + "','add')");
							break;
					}
				}
				if(callback && data.status == 'success'){
				    callback(data);
			    }
            });
		},
		cancelLike : function(id, module, isComment, callback){
			var url = '/app/valuation/cancel/' + module + '/' + id;
			var params = {
				isComment : isComment
			};
			Ajax.post(url, params, function(data){
				if(appInd){
					switch(module){
						case 'Article' :
							plus.webview.currentWebview().opener().evalJS("changeTotal('nal_" + id + "','cancel')");
							plus.webview.currentWebview().opener().evalJS("changeTotal('pal_" + id + "','cancel')");
							break;
						case 'Course' :
							plus.webview.currentWebview().opener().evalJS("changeTotal('fcl_" + id + "','cancel')");
							plus.webview.currentWebview().opener().evalJS("changeTotal('scl_" + id + "','cancel')");
							plus.webview.currentWebview().opener().evalJS("changeTotal('tcl_" + id + "','cancel')");
							plus.webview.all()[1].evalJS("changeTotal('ocl_" + id + "','cancel')");
							break;
					}
				}
				if(callback && data.status == 'success'){
				    callback(data);
			    }
            });
		},		
		addCollect : function(targetId, module, tkhId, callback){
			var url = '/app/collect/add/' + module + '/' + targetId;
			var params = {
				tkhId : (tkhId && tkhId > 0) ? tkhId : ''
			};
			Ajax.post(url, params, function(data){
				if(callback && data.status == 'success'){
				    callback(data);
			    }
            });
		},
		cancelCollect : function(id, module, callback){
			var url = '/app/collect/cancel/' + module + '/' + id;
			Ajax.post(url, {}, function(data){
				if(callback && data.status == 'success'){
				    callback(data);
			    }
            });
		},			
		addShare : function(targetId, module, note, tkhId, callback){
			var url = '/app/share/add/' + module + "/" + targetId;
			var params = {
				note : note,
			    tkhId : (tkhId && tkhId > 0) ? tkhId : ''
			};
			Ajax.post(url, params, function(data){
				if(callback && data.status == 'success'){
				    callback(data);
			    }
            });
		},		
		cancelShare : function(id, module, callback){
			var url = '/app/share/del/' + module + "/" + id;
			Ajax.post(url, {}, function(data){
				if(callback && data.status == 'success'){
				    callback(data);
			    }
            });
		},
		addDoing : function(note, action, module, targetId, callback){
			var url = '/app/doing/add';
			var params = {
				note : encodeURIComponent(note),
			    action : action,
			    module : module,
			    targetId : targetId
			};
			Ajax.post(url, params, function(data){
				if(callback && data.status == 'success'){
					if(appInd){
						switch(module){
							case 'Group' : 
								plus.webview.currentWebview().opener().evalJS("changeTotal('gm_" + targetId + "','add')");
								break;
						}
					}
				    callback(data);
			    }
            });
		},
		cancelDoing : function(doingId, targetId, module, callback){
			var url = '/app/doing/del/' + doingId;
			Ajax.post(url, {}, function(data){
				if(callback && data.status == 'success'){
					if(appInd){
						switch(module){
							case 'Group' : 
								plus.webview.currentWebview().opener().evalJS("changeTotal('gm_" + targetId + "','cancel')");
								break;
						}
					}
				    callback(data);
			    }
            });
		},
		addComment : function(targetId, replyToId, toUserId, tkhId, module, note, callback){
			if(module== 'Course' && tkhId == undefined){
				return;
			}
			var url = '/app/comment/' + module + '/' + targetId;
			var params = {
				itmId : targetId,
				tkhId : tkhId,
				repalyTo : replyToId,
				note : note,
				toUserId : toUserId
			};
			Ajax.post(url, params, function(data){
				if(appInd){
					switch(module){
						case 'Article' :
							plus.webview.currentWebview().opener().evalJS("changeTotal('nac_" + targetId + "','add')");
							plus.webview.currentWebview().opener().evalJS("changeTotal('pac_" + targetId + "','add')");
							break;
						case 'Course' :
							plus.webview.currentWebview().opener().evalJS("changeTotal('fcc_" + targetId + "','add')");
							plus.webview.currentWebview().opener().evalJS("changeTotal('scc_" + targetId + "','add')");
							plus.webview.currentWebview().opener().evalJS("changeTotal('tcc_" + targetId + "','add')");
							plus.webview.all()[1].evalJS("changeTotal('occ_" + targetId + "','add')");
							break;
						case 'Group' : 
							plus.webview.currentWebview().opener().evalJS("changeTotal('gm_" + targetId + "','add')");
							break;
					}
				}
				if(callback && data.status == 'success'){
				    callback(data);
			    }
            });
		},
		cancelComment : function(cmtId, targetId, module, callback){
			var url = '/app/comment/del/' + cmtId+'/'+targetId;
			Ajax.post(url, {}, function(data){
				if(appInd){
					switch(module){
						case 'Article' :
							plus.webview.currentWebview().opener().evalJS("changeTotal('nac_" + targetId + "','cancel')");
							plus.webview.currentWebview().opener().evalJS("changeTotal('pac_" + targetId + "','cancel')");
							break;
						case 'Course' :
							plus.webview.currentWebview().opener().evalJS("changeTotal('fcc_" + targetId + "','cancel')");
							plus.webview.currentWebview().opener().evalJS("changeTotal('scc_" + targetId + "','cancel')");
							plus.webview.currentWebview().opener().evalJS("changeTotal('tcc_" + targetId + "','cancel')");
							plus.webview.all()[1].evalJS("changeTotal('occ_" + targetId + "','cancel')");
							break;
					}
				}
				if(callback && data.status == 'success'){
				    callback(data);
			    }
            });
		} 
	};
}]);							
