$(function() {

	$("#list_ing").table({
		url : contextPath + '/app/voting/pageJsonForIng',
		colModel : ingModel,
		userView : true,
		rp : 10,
		rowSize : 1,
		hideHeader : false,
		usepager : true,
		params : {}
	});

	$("#list_ed").table({
		url : contextPath + '/app/voting/pageJsonForEd',
		colModel : edModel,
		userView : true,
		rp : 10,
		rowSize : 1,
		hideHeader : false,
		usepager : true,
		params : {}
	});

});

var ingModel = [
		{
			display : cwn.getLabel('global_title'),// "标题",
			align : 'left',
			width : '30%',
			format : function(data) {
				return data.vot_title;
			}
		},
		{
			display : cwn.getLabel('label_core_requirements_management_45'),// "截止日期",
			align : 'center',
			width : '20%',
			format : function(data) {
				return (data.vot_eff_date_to+"").substring(0,10);
			}
		},
		{
			display : cwn.getLabel('label_core_requirements_management_46'),// "提交时间",
			align : 'center',
			width : '20%',
			format : function(data) {
				if (data.voteResponse && data.voteResponse.vrp_respone_time) {
					return (data.voteResponse.vrp_respone_time+"").substring(0,10);
				}
				return "--";
			}
		},
		{
			display : cwn.getLabel('label_core_requirements_management_47'),// "提交情况",
			align : 'left',
			width : '20%',
			format : function(data) {
				if (data.voteResponse) {
					return cwn.getLabel('label_core_requirements_management_48');/* 已提交 */
				} else {
					return cwn.getLabel('label_core_requirements_management_49');/* 未提交 */
				}
			}
		},
		{
			display : "",
			align : 'right',
			width : '10%',
			format : function(data) {
				
				var enc_vot_id = wbEncrytor().cwnEncrypt(data.vot_id);
				
				if (data.voteResponse) {
					p = {
						text : cwn.getLabel('label_core_requirements_management_7'),/* 查看结果 */
						url : contextPath + "/app/voting/viewResult?enc_vot_id="
								+ enc_vot_id
					};
				} else {
					p = {
						text : cwn.getLabel('label_core_requirements_management_50'),/* 参与投票 */
						url : contextPath + "/app/voting/toVotingPage?enc_vot_id="
								+ enc_vot_id
					};
				}
				return $('#operateBtnTemplate').render(p);
			}
		}

];

var edModel = [
		{
			display : cwn.getLabel('global_title'),// "标题",
			align : 'left',
			width : '30%',
			format : function(data) {
				return data.vot_title;
			}
		},
		{
			display : cwn.getLabel('label_core_requirements_management_45'),// "截止日期",
			align : 'center',
			width : '20%',
			format : function(data) {
				return (data.vot_eff_date_to+"").substring(0,10);
			}
		},
		{
			display : cwn.getLabel('label_core_requirements_management_46'),// "提交时间",
			align : 'center',
			width : '20%',
			format : function(data) {
				if (data.voteResponse && data.voteResponse.vrp_respone_time) {
					return (data.voteResponse.vrp_respone_time +"").substring(0,10);
				}
				return "--";
			}
		},
		{
			display : cwn.getLabel('label_core_requirements_management_47'),// "提交情况",
			align : 'left',
			width : '20%',
			format : function(data) {
				if (data.voteResponse) {
					return cwn.getLabel('label_core_requirements_management_48');/* 已提交 */
				} else {
					return cwn.getLabel('label_core_requirements_management_49');/* 未提交 */
				}
			}
		},
		{
			display : "",
			align : 'right',
			width : '10%',
			format : function(data) {
				var enc_vot_id = wbEncrytor().cwnEncrypt(data.vot_id);
				p = {
					text : cwn.getLabel('label_core_requirements_management_7'),
					url : contextPath + "/app/voting/viewResult?enc_vot_id="
							+ enc_vot_id
				};
				return $('#operateBtnTemplate').render(p);
			}
		}
];


/*
 * function getModel(type) { var model = []; var coloumNames = [ "标题", "截止日期",
 * "提交时间", "提交情况", "" ]; var aligns = ["left","center","center","left","right"];
 * var widths = ["30%","20%","20%","20%","10%"]; for (var i = 0; i < 5; i++) {
 * var coloumItem = { display : coloumNames[i], align : aligns[i], width :
 * widths[i], format : function(data) { var result = ""; switch(i){ case 0:
 * result = data.vot_title; break; case 1: result = data.vot_eff_date_to; break;
 * case 2: if(data.voteResponse){ result = data.voteResponse.vrp_respone_time; }
 * break; case 3: if(data.voteResponse){ result = "已提交"; }else{ result = "未提交"; }
 * break; case 4: if("ed" === type){ p = { text:"查看结果" }; }else if("ing" ===
 * type){
 * 
 * if(data.voteResponse){ p = { text:"查看结果" }; }else{ p = { text:"参与投票" }; } }
 * result = $('#operateBtnTemplate').render(p); break; case 5: result = 5+""; }
 * return result; } }; model.push(coloumItem); } return model; }
 */
