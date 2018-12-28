appModule.service('knowledgeCenterService',['Loader',function(Loader){
	
	var getKnowledgeInfo = function(knowledgeType,kbi_filetype){
		
		var result = {};
		
		if(knowledgeType === 'VEDIO'){
			result.title = "VOD";
			result.cla = "tuwen-list-1-vod";
		}else if(knowledgeType === 'IMAGE'){
			result.title = "PIC";
			result.cla = "tuwen-list-2-pho";
		}else if(knowledgeType === 'DOCUMENT'){
			
			if(kbi_filetype){
				var fileType = kbi_filetype;
				
				if(fileType != null && fileType == "XLS"){
					result.title = "XLS";
					result.cla = "tuwen-list-1-xls";
				}else if(fileType != null && fileType == "PPT"){
					result.title = "PPT";
					result.cla = "tuwen-list-1-ppt";
				}else if(fileType != null && fileType == "PDF"){
					result.title = "PDF";
					result.cla = "tuwen-list-1-pdf";
				}else{
					result.title = "DOC";
					result.cla = "tuwen-list-1-doc";
				}
			}
		}else{ // article or other
			result.title = "ART";
			result.cla = "tuwen-list-1-art";
		}
		
		return result;
	};
	
	return {
		getKnCenterListLoader : function(options){
			
			var url = '/app/kb/center/index/indexJson';
			var params = {
			    pageSize : 10,
			    sortname : 'kbi_publish_datetime',
			    sortorder : 'desc'
			};
			options = options || {};
			params = angular.extend(params,options);
			
			var loader = new Loader(url, params,function(result){
				for(var i in result.items){
					var item = result.items[i];
					item.enc_kbi_id = wbEncryptor.encrypt(item.kbi_id);
					item.info = getKnowledgeInfo(item.kbi_type,item.docType);
				}
			});
			return loader;
		},
		getKnowledgeInfo : getKnowledgeInfo
	};
	
}]);							
