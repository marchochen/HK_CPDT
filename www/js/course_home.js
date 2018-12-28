var folderTreeSettingRootNode = {
	id : '0',
	pId : '',
	name : getLabel('850')
}

var folderTreeSetting = {
	data : {
		simpleData : {
			enable : true
		}
	},
	callback : {
		onExpand : function(event, treeId, treeNode) {
			filterModuleList(event, treeId, treeNode);
		},
		onClick : function(event, treeId, treeNode) {
			filterModuleList(event, treeId, treeNode);
		}
	}
};

function filterModuleList(event, treeId, treeNode) {
	var folderId = treeNode.id;
	if (folderId !== null && folderId !== '0') {
		$('#mod_lst_table tr.mod_lst').hide();
		$('#mod_lst_table tr.mod_lst_line').hide();

		var hasMod = false;
		var mod_relation = treeModRelation;
		for (var i = 0; i < mod_relation.length; i++) {
			if (mod_relation[i]['folder'] === folderId) {
				$('tr#mod_tr_' + mod_relation[i]['mod_id']).show();
				$('tr#mod_tr_line_' + mod_relation[i]['mod_id']).show();

				hasMod = true;
			}
		}

		if (hasMod === false) {
			$('#mod_lst_empty_text').show();
		} else {
			$('#mod_lst_empty_text').hide();
		}
	} else {
		$('#mod_lst_table tr.mod_lst').show();
		$('#mod_lst_table tr.mod_lst_line').show();
	}
	r();
}

function hasFolder(folder_structure) {
	var has_folder = false;
	var children = folder_structure['children'];
	if (children !== undefined && children.length > 0) {
		has_folder = true;
	}
	return has_folder;
}

function createFolderTree(folder_structure) {
	if (hasFolder(folder_structure)) {

		var treeNodeArray = new Array();
		treeNodeArray.push(folderTreeSettingRootNode);

		var children = folder_structure['children'];
		getFolderTreeArray(treeNodeArray, children, folderTreeSettingRootNode);

		$.fn.zTree.init($("#folderTree"), folderTreeSetting, treeNodeArray);

		$('#folderTreeDiv').show();
	} else {
		$('#folderTreeDiv').hide();
	}
}

function getFolderTreeArray(treeNodeArray, list, parent) {
	if (list !== undefined && list.length > 0) {
		for (var i = 0; i < list.length; i++) {
			var node = list[i];
			var treeNode = {
				id : node.id,
				pId : parent.id,
				name : node.text,
				isParent : true
			}
			treeNodeArray.push(treeNode);

			children = node['children'];
			if (children !== undefined && children.length > 0) {
				getFolderTreeArray(treeNodeArray, children, treeNode);
			}
		}
	}
}