<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<%@ include file="../common/template.jsp"%>
<%@ page import="com.cw.wizbank.qdb.*"%>
<%@ page import="com.cw.wizbank.util.cwXSL"%>
<!DOCTYPE html>
<html>
<head>

<jsp:include page="../common/meta.datepicker.jsp"></jsp:include>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-learner.css" />
<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<script type="text/javascript" src="${ctx}/static/js/front/course.js"></script>
<script type="text/javascript" src="${ctx}/js/wb_goldenman.js"></script>
<script type="text/javascript" src="${ctx}/js/wb_mgt_rpt.js"></script>
<script type="text/javascript" src="${ctx}/js/wb_mystaff.js"></script>
<script type="text/javascript" src="${ctx}/js/my_staff_report.js"></script>
<title></title>
<script type="text/javascript">
	$(function(){
		if (document.formRpt) {
			formRpt = document.formRpt;
		}
		init();
		
		$("input[name='enroll_start_date']").datepicker();
		$("input[name='enroll_end_date']").datepicker();
		$(".Btn").toggleClass("Btn wbtj alignbtn");
		$(".Select").parent().css("margin-left", 0);
		$(".Select").toggleClass("Select pertextarea");
		$("img[border='0']").remove();
		
		$("#saveReportSpec").live('click', function(){
			var spec = new Spec();
			if(_wbMgtRptGetFormSpec(formRpt, wb_label_lan, spec, 'yes')){
				$('#msgBox').modal('show');
			}
		});
	})

	function changeTab(thisObj){
		$(".active").removeClass("active");
		$(thisObj).addClass("active");
		$(".wbtabcont").hide();
		if($(thisObj).attr("name") == 'custom_report'){
			$("#custom_report").show();
		} else if($(thisObj).attr("name") == 'saved_report'){
			$("#saved_report").show();
			getReportSpecList();
		}
	}
	
	function getReportSpecList(){
		$(".wbtable").children().html('');
		$(".wbtable").children().table({
			url : '${ctx}/app/subordinate/getReportSpecList/STAFF',
			colModel : colModel,
			rp : 10,
			showpager : 5,
			usepager : true		
		})
	}
	
	var colModel = [ {
		display : fetchLabel('know_title'),
		width : '40%',
		format : function(data) {
			p = {
				className : 'wzb-link02',
				title : data.rsp_title,
				href : 'javascript:;',
				event : 'getRptTpl(' + data.rsp_id + ')'
			};
			return $('#a-template').render(p);
		}
	}, {
		display : fetchLabel('subordinate_update_time'),
		width : '20%',
		align : "center",
		format : function(data) {
			p = {
				text : data.rsp_upd_timestamp.substring(0,10)
			};
			return $('#text-center-template').render(p);
		}
	}, {
		display : '',
		width : '40%',
		align : "right",
		format : function(data) {
			var spec = new Spec();
			var spec_name = [ 'ent_id', 'tnd_id', 'itm_id', 's_usg_ent_id_lst', 'att_create_start_datetime', 'att_create_end_datetime', 'ats_id', 'is_my_staff', 'usr_content_lst',
					'itm_content_lst', 'content_lst' ];

			for ( var i = 0; i < spec_name.length; i++) {
				spec.name += spec_name[i] + d_f;
				var tmp_value = '';
				switch (spec_name[i]) {
				case 'ent_id':
					var id_tmp = data.staffReportBean.gmUsrOption;
					if (id_tmp !== undefined && id_tmp !== '') {
						tmp_value = getSpecId(id_tmp['value']);
					}
					break;
				case 'tnd_id':
					var id_tmp = data.staffReportBean.gmTndOption;
					if (id_tmp !== undefined && id_tmp !== '') {
						tmp_value = getSpecId(id_tmp['value']);
					}
					break;
				case 'itm_id':
					var id_tmp = data.staffReportBean.gmItmOption;
					if (id_tmp !== undefined && id_tmp !== '') {
						tmp_value = getSpecId(id_tmp['value']);
					}
					break;
				case 's_usg_ent_id_lst':
					tmp_value = data.staffReportBean.s_usg_ent_id_lst;
					break;
				case 'att_create_start_datetime':
					tmp_value = data.staffReportBean.att_create_start_datetime;
					if (tmp_value === Wzb.min_timestamp) {
						tmp_value = '';
					}
					break;
				case 'att_create_end_datetime':
					tmp_value = data.staffReportBean.att_create_end_datetime;
					if (tmp_value === Wzb.max_timestamp) {
						tmp_value = '';
					}
					break;
				case 'ats_id':
					tmp_value = data.staffReportBean.ats_id;
					break;
				case 'is_my_staff':
					tmp_value = 'true';
					break;
				case 'usr_content_lst':
					tmp_value = usr_content_lst_value;
					break;
				case 'itm_content_lst':
					tmp_value = itm_content_lst_value;
					break;
				case 'content_lst':
					tmp_value = content_lst_value;
					break;
				}
				spec.value += tmp_value + d_f;
			}
			var handler = 'viewSpecifiedRpt(\'rpt_lrn_res_new.xsl\', ' + 1 + ',\'' + spec.name + '\', \'' + spec.value + '\');';
			
			p1 = {
				className : 'skin-color',
				title : fetchLabel('button_see'),
				href : 'javascript:;',
				event : handler
			};
			p2 = {
				className : 'skin-color',
				title : fetchLabel('button_del'),
				href : 'javascript:;',
				event : "delSavedReport(" + data.rsp_id + ")"
			};
			return $('#a-template').render(p1) + " | " + $('#a-template').render(p2);
		}
	} ]
	
	function delSavedReport(rsp_id){
		Dialog.confirm({text:fetchLabel('subordinate_del_report_confirm'), callback: function (answer) {
				if(answer){
					$.ajax({
						url : '${ctx}/app/subordinate/delReportSpec/STAFF/' + rsp_id,
						type : 'POST',
						dataType : 'json',
						success : function(data){
							if(data.error != undefined && data.error == 'subordinate_del_error'){
								Dialog.alert(fetchLabel('subordinate_del_error'));
							} else {
								getReportSpecList();
							}
						}
					});
				}
			}
		});
	}
	
</script>
</head>
<body>
<div class="xyd-wrapper">
	<div id="main" class="xyd-main clearfix">
		<jsp:include page="subordinateMenu.jsp"></jsp:include>
		
		<div class="xyd-article">            
            <div role="tabpanel" class="wzb-tab-3">
                <!-- Nav tabs -->
                <ul class="nav nav-tabs" role="tablist">
                    <li role="presentation" class="active" name="custom_report" onclick="changeTab(this)"><a href="#home" aria-controls="home" role="tab" data-toggle="tab" style="padding-left:0;"><lb:get key="subordinate_custom_report"/></a></li>
                    <li role="presentation" name="saved_report" onclick="changeTab(this)"><a href="#profile" aria-controls="profile" role="tab" data-toggle="tab"><lb:get key="subordinate_saved_report"/></a></li>
                </ul>
                        
                <div class="tab-content">
                    <div id="custom_report" class="wbtabcont">
                        <form id="formRpt" name="formRpt"> 
                          <table class="margin-top15">
                                <tr>
                                     <td class="wzb-form-label" valign="top"><span class="wzb-form-star">*</span><lb:get key="subordinate_user_or_group"/>：</td>
                                     
                                     <td class="wzb-form-control">
                                         <div><label class="wzb-form-checkbox" for="tabradio-01"><input type="radio" name="s_usg_ent_id_lst" id="tabradio-01" value="my_staff" onclick="javascript: usr_change(this);" checked="checked"><lb:get key="subordinate_my_all"/></label></div>
                                         <div><label class="wzb-form-checkbox" for="tabradio-02"><input type="radio" name="s_usg_ent_id_lst" id="tabradio-02" value="my_direct_staff" onclick="javascript: usr_change(this);"><lb:get key="subordinate_my_direct"/></label></div>
                                         <div><label class="wzb-form-checkbox" for="tabradio-03"><input type="radio" name="s_usg_ent_id_lst" id="tabradio-03" value="" onclick="javascript: usr_change(this);"><lb:get key="subordinate_appoint"/></label></div>
                                         <%
                                             loginProfile prof = (loginProfile) session.getAttribute(qdbAction.AUTH_LOGIN_PROFILE);
                                             String html = cwXSL.getGoldenManHtml(prof.label_lan, "goldenman_mystaff_user");
                                         %>
                                         <%=html%>
                                     </td>
                                </tr>
                                
                                <tr>
                                     <td class="wzb-form-label" valign="top"><span class="wzb-form-star">*</span><lb:get key="subordinate_train"/>：</td>
                                     
                                     <td class="wzb-form-control">
                                         <div><label class="wzb-form-checkbox" for="tabradio-04"><input type="radio" name="course_sel_type" id="tabradio-04" value="2" checked="checked" onclick="javascript: course_change(this);"><lb:get key="subordinate_all_train"/></label></div>
                                         <div><label class="wzb-form-checkbox" for="tabradio-05"><input type="radio" name="course_sel_type" id="tabradio-05" value="0" onclick="javascript: course_change(this);"><lb:get key="subordinate_some_train"/></label></div>
                                         <%
                                             html = cwXSL.getGoldenManHtml(prof.label_lan, "goldenman_mystaff_item",prof.my_top_tc_id);
                                         %>
                                         <%=html%>
                                         
                                         <div><label class="wzb-form-checkbox" for="tabradio-06"><input type="radio" name="course_sel_type" id="tabradio-06" value="1" onclick="javascript: course_change(this);"><lb:get key="subordinate_catalog_train"/></label></div>
                                         <%
                                             html = cwXSL.getGoldenManHtml(prof.label_lan, "goldenman_mystaff_treenode",prof.my_top_tc_id);
                                         %>
                                         <%=html%>
                                     </td>
                                </tr>
                                
                                <tr>
                                     <td class="wzb-form-label"><lb:get key="subordinate_registration_date"/>：</td>
                                     
                                     <td class="wzb-form-control">
                                         <input type="text" id="demo1" name="enroll_start_date" class="laydate-icon wzb-text-02" pattern="yyyy-MM-dd"/>
                                         <lb:get key="time_to"/> 
                                         <input type="text" id="demo" name="enroll_end_date" class="laydate-icon  wzb-text-02" pattern="yyyy-MM-dd"/>
                                     </td>
                                </tr>
                                
                                <tr>
                                     <td class="wzb-form-label"><lb:get key="subordinate_training_status"/>：</td>
                                     
                                     <td class="wzb-form-control">
                                         <label class="wzb-input-label margin-right15" for="psda"><input type="radio" id="psda" value="0" name="ats_id" checked="checked"><lb:get key="status_all_2"/></label> 
                                         <label class="wzb-input-label margin-right15" for="psdb"><input type="radio" id="psdb" value="1" name="ats_id"><lb:get key="status_completed"/></label> 
                                         <label class="wzb-input-label margin-right15" for="psdc"><input type="radio" id="psdc" value="2" name="ats_id"><lb:get key="status_inprogress"/></label> 
                                         <label class="wzb-input-label margin-right15" for="psdd"><input type="radio" id="psdd" value="3" name="ats_id"><lb:get key="status_flunk"/></label> 
                                         <label class="wzb-input-label" for="psde"><input type="radio" id="psde" value="4" name="ats_id"><lb:get key="status_withdrawn"/></label>
                                     </td>
                                </tr>
                                
                                <tr>
                                     <td class="wzb-form-label"></td>
                                     
                                     <td class="wzb-form-control"><span class="wzb-form-star">*</span><lb:get key="subordinate_required"/></td>
                                </tr>
                          </table>
                          
                          <div id="msgBox" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                            <div class="modal-dialog">
                                <div class="modal-content" style="height:260px;">
                                    <div class="modal-header">
                                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true" onclick="refreshMyMember('')">&times;</button>
                                        <h4 class="modal-title" id="myModalLabel"><div class="pfindtit"><lb:get key="subordinate_save_report_template"/></div></h4>
                                    </div>
                                    <div class="modal-body" style="padding-left:0px;">
                                        <div class="mt5 mb15 clearfix">
                                            <div class="fl" style="width:100%;padding-left:20px;">
                                                &nbsp;&nbsp;<lb:get key="subordinate_save_report_desc"/><br/><br/>
                                                &nbsp;&nbsp;<lb:get key="global_title"/><br/>
                                                &nbsp;&nbsp;<input type="text" id="s_rsp_title" class="pertime" maxlength="50" style="width:95%;margin-top:10px;margin-bottom:20px;"><br/>
                                                <input id="s_rsp_id" id="rsp_id" type="hidden" value="${rsp_id}"/>
                                                <div style="text-align: center;">
                                                    <input type="button" class="btn wzb-btn-blue wzb-btn-big" value='<lb:get key="button_ok"/>' onclick="javascript: save_rpt_exec();"/>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        
                        <input type="hidden" name="cmd" value="" />
                        <input type="hidden" name="module" value="" />
                        <input type="hidden" name="rpt_type" value="" />
                        <input type="hidden" name="stylesheet" value="" />
                        <input type="hidden" name="spec_name" value="" />
                        <input type="hidden" name="spec_value" value="" />
                        <input type="hidden" name="window_name" value="" />
                        <input type="hidden" name="url_success" value="" />
                        <input type="hidden" name="url_failure" value="" />
                        <input type="hidden" name="download" value="" />
                        <input type="hidden" name="rte_id" value="" />
                        <input type="hidden" name="att_create_start_datetime" value="" />
                        <input type="hidden" name="att_create_start_datetime_json" value="" />
                        <input type="hidden" name="att_create_end_datetime" value="" />
                        <input type="hidden" name="att_create_end_datetime_json" value="" />
                        <input type="hidden" name="s_usg_ent_id_lst" value="" />
                        <input type="hidden" name="rspTitle" value="" />
                        <input type="hidden" name="ent_id_str" value="" />
                        <input type="hidden" name="usr_name_str" value="" />
                        <input type="hidden" name="itm_id_str" value="" />
                        <input type="hidden" name="itm_title_str" value="" />
                        <input type="hidden" name="tnd_id_str" value="" />
                        <input type="hidden" name="tnd_title_str" value="" />
                        <input type="hidden" name="hidden_spec_name" value="content_lst" />
                        <input type="hidden" name="hidden_spec_value" value="att_create_timestamp~att_status~att_timestamp~cov_commence_datetime~cov_last_acc_datetime~total_attempt~cov_total_time~cov_score" />
                        <input type="hidden" name="hidden_spec_name" value="itm_content_lst" />
                        <input type="hidden" name="hidden_spec_value" value="field01~field02~itm_type~catalog~training_center" />
                        <input type="hidden" name="hidden_spec_name" value="usr_content_lst" />
                        <input type="hidden" name="hidden_spec_value" value="usr_id~usr_display_bil~USR_PARENT_USG~USR_CURRENT_UGR~usr_email~usr_tel_1" />
                        <input type="hidden" name="is_my_staff" value="true" />
                        <input type="hidden" name="rsp_id" value="" />
                        <input type="hidden" name="rsp_tpl_value" value="" />
                          
                        <div class="wzb-bar">
                             <input type="button" name="frmSubmitBtn" value='<lb:get key="button_see"/>' onclick="javascript: run_rpt_new();" class="btn wzb-btn-orange wzb-btn-big margin-right15"/>
                             <input type="button" id="saveReportSpec" name="frmSubmitBtn" value='<lb:get key="button_save"/>' class="btn wzb-btn-orange wzb-btn-big"/>
                        </div>
                      </form>  
                    </div>
                    
                    <div id="saved_report" style="display:none" class="wbtabcont">
                         <div class="wbtable mt15"><div></div></div>
                    </div>
                </div>
            </div> <!-- wbtab end --> 
		</div> <!-- xyd-article End-->
	</div>
</div> <!-- xyd-wrapper End-->
<!-- 日期控件 start-->
<script>
!function(){
laydate.skin('molv');//切换皮肤，请查看skins下面皮肤库
laydate({elem: '#demo'});//绑定元素
laydate({elem: '#demo1'});//绑定元素
}();
//日期范围限制
var start = {
    elem: '#start',
    format: 'YYYY-MM-DD',
    min: laydate.now(), //设定最小日期为当前日期
    max: '2099-06-16', //最大日期
    istime: true,
    istoday: false,
    choose: function(datas){
         end.min = datas; //开始日选好后，重置结束日的最小日期
         end.start = datas //将结束日的初始值设定为开始日
    }
};
var end = {
    elem: '#end',
    format: 'YYYY-MM-DD',
    min: laydate.now(),
    max: '2099-06-16',
    istime: true,
    istoday: false,
    choose: function(datas){
        start.max = datas; //结束日选好后，充值开始日的最大日期
    }
};
laydate(start);
laydate(end);
//自定义日期格式
laydate({
    elem: '#test1',
    format: 'YYYY年MM月DD日',
    festival: true, //显示节日
    choose: function(datas){ //选择日期完毕的回调
        alert('得到：'+datas);
    }
});
//日期范围限定在昨天到明天
laydate({
    elem: '#hello3',
    min: laydate.now(-1), //-1代表昨天，-2代表前天，以此类推
    max: laydate.now(+1) //+1代表明天，+2代表后天，以此类推
});

window.onload=function(){
    var aqing=document.getElementById("qing")
    //console.log(aqing)
    var body2 = document.getElementById("by");
    

}
</script>
<!-- 日期控件 end-->
</body>
</html>

