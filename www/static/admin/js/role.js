var dt;
$(function(){
    dt = $("#rolelist").table({
        url : contextPath + '/app/admin/role/indexJson',
		colModel : listModel,
        rp : 10,
        hideHeader : false,
		sortname : 'rol_title',
		sortorder : 'desc',
        usepager : true,
        rowCallback : function(row){
        	if(row.rol_type!= 'SYSTEM' && row.isRefByUser){//如果是非系统角色，而且已经 被用户引用，则提示不可以删除
        		var label = fetchLabel("label_core_system_setting_127");
        		$("#role_title_"+row.rol_id).qtip({position:{my:"center left",at:"center right"},content:label});
        	}
        }
    });
})
//list
var listModel = [/*{
	display : '<div style="margin-bottom:6px"></div>',
	align : 'left',
	format : function(data){
		p = {
				id : data.rol_id,
				type : data.rol_type,
				ext_id : data.rol_ext_id,
				isRefByUser : data.isRefByUser
		}
		return $("#input-checkbox-template").render(p);
	}
},*/{
	display : cwn.getLabel('label_core_system_setting_6'),
	tdWidth : '20%',
	sortable : true,
	format : function(data){
		
		var title =data.rol_title;
		if(data.rol_type != null && data.rol_type == 'SYSTEM'){
			title = fetchLabel("lab_rol_" + data.rol_ste_uid) 
		}
		p = {
				text : title,
				id : data.rol_id,
				type : data.rol_type,
				ext_id : data.rol_ext_id,
				isRefByUser : data.isRefByUser
		}
		return $("#input-checkbox-template").render(p);
	}
}, {
	display : cwn.getLabel('label_core_system_setting_7'),
	align : "center",
	tdWidth : '20%',
	sortable : false,
	format : function(data){
		
		var text ;
		
		if( data.rol_tc_ind == "1"){
			text = "label_core_system_setting_13"
		}
		else{
			text = "label_core_system_setting_14"
		}
		
		p = {
				text : cwn.getLabel(text)
		}
		return $("#text-operate-template").render(p);
	}
}, {
	display : cwn.getLabel('label_core_system_setting_8'),
	align : "center",
	tdWidth : '20%',
	sortable : false,
	format : function(data){
		p = {
				text : data.user.usr_display_bil
		}
		return $("#text-operate-template").render(p);
	}
}, {
	display : cwn.getLabel('label_core_system_setting_9'),
	align : "center",
	tdWidth : '20%',
	sortable : true,
	format : function(data){
		p = {
				text : data.rol_update_timestamp
		}
		return $("#text-operate-template").render(p);
	}
},{
	display : '<div style="margin-bottom:6px"></div>',
	align : "center",
	tdWidth : '20%',
	format : function(data){
		p = {
				id : data.rol_id,
				type : data.rol_type,
				title : data.rol_title,
				name : data.user.usr_display_bil,
				ext_id : data.rol_ext_id
		}
		return $("#input-button-template").render(p);
	}
}
]

/**根据id删除角色*/
function delById(){
	var ids = '';
	$("input[name='rol_checkbox']:checked").each(function(){
			ids += $(this).val() + ',';
	})
	if(ids == ''){
		Dialog.alert(fetchLabel('label_core_system_setting_10'));
	}else{
		if(confirm(fetchLabel('label_core_system_setting_90'))){
			$.post( contextPath + "/app/admin/role/delById",{
				ids : ids
			},function(){
				reloadTable();
			});
		}
	}
}

function reloadTable(){
	$(dt).reloadTable({
		url : contextPath + '/app/admin/role/indexJson'
	})
}
