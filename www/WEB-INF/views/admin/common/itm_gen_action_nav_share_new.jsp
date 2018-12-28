
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<% 
	String cur_node_id=request.getParameter("cur_node_id");
    String is_add=request.getParameter("is_add");
%>
<script language="JavaScript" type="text/javascript" src="${ctx}/js/wb_itm_req.js"></script>
<script language="JavaScript" type="text/javascript" src="${ctx}/js/gen_utils.js"></script>
<script language="JavaScript" type="text/javascript" src="${ctx}/js/wb_utils.js"></script>
<script language="JavaScript" type="text/javascript" src="${ctx}/js/wb_item.js"></script>
<script language="JavaScript" type="text/javascript" src="${ctx}/js/wb_course.js"></script>
<script language="JavaScript" type="text/javascript" src="${ctx}/js/wb_announcement.js"></script>
<script language="JavaScript" type="text/javascript" src="${ctx}/js/wb_report.js"></script>
<script language="javascript" type="text/javascript" src="${ctx}/js/wb_criteria.js"></script>
<script language="javascript" type="text/javascript" src="${ctx}/js/wb_mote.js"></script>
<script language="JavaScript" type="text/javascript" src="${ctx}/js/wb_application.js"></script>
<script language="JavaScript" type="text/javascript" src="${ctx}/js/wb_attendance.js"></script>
<script language="JavaScript" type="text/javascript" src="${ctx}/js/wb_usergroup.js"></script>
<script language="JavaScript" type="text/javascript" src="${ctx}/js/wb_module.js"></script>
<script language="JavaScript" type="text/javascript" src="${ctx}/js/wb_cos_wizard.js"></script>
<script language="JavaScript" type="text/javascript" src="${ctx}/js/wb_fm.js"></script>
<script language="JavaScript" type="text/javascript" src="${ctx}/js/wb_scorescheme.js"></script>
<script language="JavaScript" type="text/javascript" src="${ctx}/static/js/wb_encrypt_util.js"></script>

<script language="JavaScript" type="text/javascript" src="${ctx}/static/js/itm_gen_action_nav_shares.js"></script>

<script type="text/javascript">
	fm = new wbFm(true)
	crit = new wbCriteria
	itm_lst = new wbItem
	ann = new wbAnnouncement
	course_lst = new wbCourse
	var module_lst = new wbModule;	
	rpt = new wbReport
	mote = new wbMote
	app = new wbApplication
	attn = new wbAttendance
	itmReq = new wbItemReq
	usr = new wbUserGroup
	wiz = new wbCosWizard
	cmt = new wbScoreScheme;
	wbencrytor = new wbEncrytor;
	
	var rsv_itm_title = "${item.parent.itm_title}" + " - " + "${item.itm_title}";
	
	//是否是新增  
	var is_add = <%=is_add%>;
	if(is_add =='' || is_add == null){
		is_add = false;
	}
	var cur_node_id = <%=cur_node_id%>;
	var itm_id = '${item.itm_id}';
	var itm_exam_ind = numTransformationTF('${item.itm_exam_ind}');
	var itm_type = '${item.itm_type}';
	var cos_res_id = '${item.course.cos_res_id}';
	var run_ind = numTransformationTF('${item.itm_run_ind}');
	var ref_ind = numTransformationTF('${item.itm_ref_ind}');
	var create_run_ind = numTransformationTF('${item.itm_create_run_ind}');
	var content_def = '${item.itm_content_def}';
	var rsv_id = '${item.itm_rsv_id}';
	var itm_status = '${item.itm_status}';
	var itm_title = '${item.itm_title}';
	var parent_title = '${item.parent.itm_title}';
	var isStatusOff = '${item.itm_status_off}';
	//var hadPublishClass = '${item.childrens}';

	var hadPublishClass = ${hasRole.has_run};
	var has_mod = ${hasRole.has_mod};	
	var has_lesson = ${item.itm_has_lesson}; 
	var hasItmCosMain = ${hasRole.hasItmCosMain};
	var hasContentMain = ${hasRole.hasContentMain};
	var hasEnrollMain = ${hasRole.hasEnrollMain};
	var hasResultMain = ${hasRole.hasResultMain};
	//是否开放CPT/D功能
	var hasCPT = ${hasRole.hasCPT};
	
	<!-- 判断是否有设置网上内容等，没有设置则在标签上面进行提示 -->
	var rmdClassManagement = ${courseTabsRemind.rmdClassManagement};
	var rmdCompletionCriteriaSettings = ${courseTabsRemind.rmdCompletionCriteriaSettings};
	var rmdCoursePackage = ${courseTabsRemind.rmdCoursePackage};
	var rmdCourseScoreSettings = ${courseTabsRemind.rmdCourseScoreSettings};
	var rmdOnlineContent = ${courseTabsRemind.rmdOnlineContent};
	var rmdTargetLearner = ${courseTabsRemind.rmdTargetLearner};
	var rmdTimetable = ${courseTabsRemind.rmdTimetable};

	//图片 
	var space_img = '../../../../static/images/wzb-dian-2.png';
	<!-- 我的教学课程 -->
	var hasTeachingCourse = ${hasRole.hasTeachingCourse};
	
	//<!-- 公共链接定义start -->
	var link103 = link101 = link113 = link107 = link104 = link111 = link118 = link110 = link112 = link119 = link121 = '';
	//<!--计分规则-->
	if(hasContentMain == true){
		link103 = 'javascript:cmt.get_score_scheme_list('+itm_id+')';
	}
	//<!--报名工作流 -->
	if(hasItmCosMain == true){
		link101 = 'javascript:itm_lst.upd_item_workflow_prep('+itm_id+')';
	}
	//<!--结训记录 -->
	if(hasResultMain == true){
		link113 = 'javascript:attn.get_grad_record('+itm_id+')';
	}
	//<!--课程费用 -->
	if(hasItmCosMain == true){
		link107 = 'javascript:itm_lst.get_item_cost('+itm_id+')';
	}
	//<!-- 先修模块设置 -->
	if(hasContentMain == true){
		link104 = 'javascript:itm_lst.get_mod_pre('+itm_id+')';
	}
	//<!--处理报名 -->
	if(hasEnrollMain == true){
		link111 = 'javascript:app.get_application_list("",'+itm_id+',"","","","")';
	}
	//<!--评论管理 -->
	if(hasItmCosMain == true){
		link118 = 'javascript: itm_lst.get_itm_comment_lst(wbencrytor.cwnEncrypt('+itm_id+'))';
	}
	//<!--课程公告 -->
	if(hasContentMain == true){
		link110 = 'javascript:ann.sys_lst("all","RES",'+cos_res_id+',"","","","","",true)';
	}
	//<!--先修课程 -->
	if(hasContentMain == true){
		link112 = 'javascript:tmReq.itm_req_lst('+itm_id+')';
	}
	// 设置CPD所需时数
	link119 = 'javascript: itm_lst.set_itm_cpd_gourp_hour(wbencrytor.cwnEncrypt('+itm_id+'))';
	
	//CPD获得时数
	link121 = 'javascript: itm_lst.itm_cpd_hours_award_record(wbencrytor.cwnEncrypt('+itm_id+'))';
	
	//<!-- 公共链接定义end -->
	
	function numTransformationTF(num){
    	if(num == '0'){
    		return false;
    	}else if(num == '1'){
    		return true;
    	}else{
    		return num;
    	}
    }
</script>

<!-- 导航按钮 -->
    <div style="width:646px;position:relative;height:98px;margin:0 auto;" id="buzhou">
      <div class="buzhou" id="first_buzhou">
            
      </div>
     </div>

     <div class="wzb-tab-7-show" style="display: inline-block;" id="geli_div">
      </div> 

    <div class="wzb-tab-7 " style="display: inline-block;" id="second_buzhou">
            
   </div>


<div class="clear"></div>

<input type="hidden" name="parent_code" value="${parent_code}"/>


<!-- 第一级导航栏每个步骤的模板start -->
<script id="progress_step" type="text/x-jsrender">
         <a href="{{if step_url != ''}} {{>step_url}} {{else}} javascript:void(0) {{/if}}" >
			<div class="{{>div_class}}" id="{{>id}}" cur="{{>cur_node_id}}">
				{{>step_num}}
				<span>{{>step_title}}</span>
				<div class="wzb-buzhou-jiao sjcolor-5"></div>
			</div>
		</a>
 
     {{if is_last != true}}
        <div class="wzb-float wzb-fudian">
			{{if space_img}}
				<img src="{{>space_img}}"/>
				{{else}}
				<img src="${ctx}/static/images/wzb-dian-0.png"/>
			{{/if}}
		</div>
     {{/if}}

</script>

<!-- 二级导航每一项的模板 -->
<script id="second_nav_item" type="text/x-jsrender">
     <a href="{{>url}}" class="{{>a_class}}" id="{{>id}}">
         <span class="{{>icon}}"><em>{{>title}}</em></span>
     </a>
</script>
