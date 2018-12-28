<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<xsl:import href="../utils/wb_ui_nav_link.xsl"/>

	<xsl:variable name="lab_cos_info" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1154')"/>
	<xsl:variable name="lab_requisite" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1155')"/>
	<xsl:variable name="lab_g_txt_btn_tarlrn" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1156')"/>
	<xsl:variable name="lab_g_txt_btn_process_enrol" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1157')"/>
	<xsl:variable name="lab_ils_btn_content" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1158')"/>
	<xsl:variable name="lab_g_txt_btn_item_cost" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1159')"/>
	<xsl:variable name="lab_g_txt_btn_ann" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1160')"/>
	<xsl:variable name="lab_g_txt_btn_content" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1161')"/>
	<xsl:variable name="lab_earlier_model" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1162')"/>
	<xsl:variable name="lab_course_result" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1163')"/>
	<xsl:variable name="lab_grad_term" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1164')"/>
	<xsl:variable name="lab_score_record" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1165')"/>
	<xsl:variable name="lab_grad_record" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1166')"/>
	<xsl:variable name="lab_tracking_rpt" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1167')"/>
	<xsl:variable name="lab_g_txt_btn_comments" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1168')"/>
	<xsl:variable name="lab_g_txt_btn_edit_workflow" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1169')"/>
	<xsl:variable name="lab_run_info_exam" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1170')"/>
	<xsl:variable name="lab_run_info_class" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1171')"/>
	<xsl:variable name="lab_run_exam_info" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1172')"/>
	<xsl:variable name="lab_run_class_info" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1173')"/>
	<xsl:variable name="lab_run_tarlrn" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1174')"/>
	<xsl:variable name="lab_g_txt_btn_joining_inst" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1175')"/>
	<xsl:variable name="lab_g_txt_btn_book_sys" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1176')"/>
	<xsl:variable name="lab_completion_cri" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1179')"/>
	<xsl:variable name="lab_att_rate" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1181')"/>
	<xsl:variable name="lab_project_info" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1184')"/>
	<xsl:variable name="lab_g_txt_btn_courses" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1185')"/>
	<xsl:variable name="lab_itm_action_nav" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1201')"/>

	<xsl:variable name="hasItmCosMain" select="//itm_action_nav/hasItmCosMain/text()"/>
	<xsl:variable name="itm_id" select="//itm_action_nav/@itm_id"/>
	<xsl:variable name="itm_exam_ind" select="//itm_action_nav/@itm_exam_ind"/>
	<xsl:variable name="itm_type" select="//itm_action_nav/@itm_type"/>
	<xsl:variable name="cos_res_id" select="//itm_action_nav/@cos_res_id"/>
	<xsl:variable name="run_ind" select="//itm_action_nav/@itm_run_ind"/>
	<xsl:variable name="create_run_ind" select="//itm_action_nav/@itm_create_run_ind"/>
	<xsl:variable name="content_def" select="//itm_action_nav/@itm_content_def"/>
	<xsl:variable name="rsv_id" select="//itm_action_nav/@itm_rsv_id"/>
	<xsl:variable name="has_mod" select="//itm_action_nav/@has_mod"/>
	
	<xsl:template name="itm_action_nav">
		<xsl:param name="cur_node_id">100</xsl:param>

		<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_itm_req.js"/>
		<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
		<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
		<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_item.js"/>
		<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_course.js"/>
		<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_announcement.js"/>
		<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_report.js"/>
		<script language="javascript" type="text/javascript" src="{$wb_js_path}wb_criteria.js"/>
		<script language="javascript" type="text/javascript" src="{$wb_js_path}wb_mote.js"/>
		<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_application.js"/>
		<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_attendance.js"/>
		<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_usergroup.js"/>
		<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_module.js"/>
		<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_cos_wizard.js"/>
		<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_fm.js"/>
		<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_scorescheme.js"/>
		<script language="JavaScript" type="text/javascript"><![CDATA[
		fm = new wbFm
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
		
		nod_id = ]]>'<xsl:value-of select="$cur_node_id"/>'<![CDATA[

		 $(document).ready(function(){
		    $("#navNode>li[id=]]>'<xsl:value-of select="$cur_node_id"/>'<![CDATA[ ]").addClass("active").siblings().removeClass("active");
		});

	]]></script>		 
		<xsl:choose>
			<xsl:when test="$hasItmCosMain = 'true'">
				<ul id="navNode" class="nav nav-tabs page-tabs" role="tablist">
					<xsl:choose>
						<xsl:when test="$itm_type = 'SELFSTUDY'">
							<xsl:call-template name="selfstudy_tab_li"></xsl:call-template>
						</xsl:when>
						<xsl:when test="$itm_type = 'CLASSROOM' and $run_ind !='true'">
							<xsl:call-template name="classroom_tab_li"></xsl:call-template>
						</xsl:when>
						<xsl:when test="$itm_type = 'CLASSROOM' and $run_ind ='true'">
							<xsl:call-template name="run_tab_li"></xsl:call-template>
						</xsl:when>
						<xsl:when test="$itm_type = 'INTEGRATED'">
							<xsl:call-template name="integrated_tab_li"></xsl:call-template>
						</xsl:when>
						<xsl:when test="$itm_type = 'AUDIOVIDEO'">
							<xsl:call-template name="audiovideo_tab_li"></xsl:call-template>
						</xsl:when>
					</xsl:choose>
				</ul>
			</xsl:when>
			<xsl:otherwise>无权限</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
<!--================网上课程/考试=================-->
	<xsl:template name="selfstudy_tab_li">
		<!--课程信息-->
		<li id="100" role="presentation" class="active">
			<xsl:variable name="link">javascript:itm_lst.get_item_detail(<xsl:value-of select="$itm_id"/>)</xsl:variable>
			<a aria-controls="userGroupLish" role="tab" href="{$link}">
				<xsl:value-of select="$lab_cos_info"/>
			</a>     
		</li>
		<!--先修课程-->
		<li id="101" role="presentation">
			<xsl:variable name="link">javascript:itmReq.itm_req_lst(<xsl:value-of select="$itm_id"/>)</xsl:variable>
			<a aria-controls="userGroupLish" role="tab" href="{$link}">
				<xsl:value-of select="$lab_requisite"/>
			</a>     
		</li>
		
		<!--目标学员-->
		<li id="102" role="presentation">
			<xsl:variable name="link">javascript:itm_lst.get_target_rule(<xsl:value-of select="$itm_id"/>, 'TARGET_LEARNER');</xsl:variable>
			<a aria-controls="userGroupLish" role="tab" href="{$link}">
				<xsl:value-of select="$lab_g_txt_btn_tarlrn"/>
			</a>     
		</li>
		
		<!--处理报名<-->
		<li id="103" role="presentation">
			<xsl:variable name="link">javascript:app.get_application_list('',<xsl:value-of select="$itm_id"/>,'','','','')</xsl:variable>
			<a aria-controls="userGroupLish" role="tab" href="{$link}">
				<xsl:value-of select="$lab_g_txt_btn_process_enrol"/>
			</a>     
		</li>
		
		<!--课程费用-->
		<li id="104" role="presentation">
			<xsl:variable name="link">javascript:itm_lst.get_item_cost(<xsl:value-of select="$itm_id"/>)</xsl:variable>
			<a aria-controls="userGroupLish" role="tab" href="{$link}">
				<xsl:value-of select="$lab_g_txt_btn_item_cost"/>
			</a>     
		</li>
		
		<!--课程公告-->
		<li id="105" role="presentation">
			<xsl:variable name="link">javascript:ann.sys_lst('all','RES','<xsl:value-of select="$cos_res_id"/>','','','','','',true)</xsl:variable>
			<a aria-controls="userGroupLish" role="tab" href="{$link}">
				<xsl:value-of select="$lab_g_txt_btn_ann"/>
			</a>     
		</li>
			
		<!--网上内容-->	
		<li id="106" role="presentation">
			<xsl:variable name="link">javascript:course_lst.edit_cos('<xsl:value-of select="$cos_res_id"/>','<xsl:value-of select="$itm_type"/>','<xsl:value-of select="$create_run_ind"/>','<xsl:value-of select="$content_def"/>','<xsl:value-of select="$has_mod"/>')</xsl:variable>
			<a aria-controls="userGroupLish" role="tab" href="{$link}">
				<xsl:value-of select="$lab_g_txt_btn_content"/>
			</a>     
		</li>
					
		<!--先修模块设置-->
		<li id="107" role="presentation">
			<xsl:variable name="link">javascript:itm_lst.get_mod_pre(<xsl:value-of select="$itm_id"/>)</xsl:variable>
			<a aria-controls="userGroupLish" role="tab" href="{$link}">
				<xsl:value-of select="$lab_earlier_model"/>
			</a>     
		</li>
		
		<!--课程分数设置-->
		<li id="108" role="presentation">
			<xsl:variable name="link">javascript:cmt.get_score_scheme_list(<xsl:value-of select="$itm_id"/>)</xsl:variable>
			<a aria-controls="userGroupLish" role="tab" href="{$link}">
				<xsl:value-of select="$lab_course_result"/>
			</a>     
		</li>
		
		<!--结训条件设置-->
		<li id="109" role="presentation">
			<xsl:variable name="link">javascript:cmt.get_criteria(<xsl:value-of select="$itm_id"/>)</xsl:variable>
			<a aria-controls="userGroupLish" role="tab" href="{$link}">
				<xsl:value-of select="$lab_completion_cri"/>
			</a>     
		</li>

		<!--计分记录-->
		<li id="110" role="presentation">
			<xsl:variable name="link">javascript:cmt.get_scoring_itm_lst(<xsl:value-of select="$itm_id"/>)</xsl:variable>
			<a aria-controls="userGroupLish" role="tab" href="{$link}">
				<xsl:value-of select="$lab_score_record"/>
			</a>     
		</li>
		
		<!--结训记录-->
		<li id="111" role="presentation">
			<xsl:variable name="link">javascript:attn.get_grad_record(<xsl:value-of select="$itm_id"/>)</xsl:variable>
			<a aria-controls="userGroupLish" role="tab" href="{$link}">
				<xsl:value-of select="$lab_grad_record"/>
			</a>     
		</li>
			
		<!--跟踪报告-->
		<li id="112" role="presentation">
			<xsl:variable name="link">javascript:rpt.open_cos_lrn_lst(<xsl:value-of select="$itm_id"/>)</xsl:variable>
			<a aria-controls="userGroupLish" role="tab" href="{$link}">
				<xsl:value-of select="$lab_tracking_rpt"/>
			</a>     
		</li>
		
		<!--评论管理-->
		<li id="113" role="presentation">
			<xsl:variable name="link">javascript: itm_lst.get_itm_comment_lst(<xsl:value-of select="$itm_id"/>)</xsl:variable>
			<a aria-controls="userGroupLish" role="tab" href="{$link}">
				<xsl:value-of select="$lab_g_txt_btn_comments"/>
			</a>     
		</li>
		
		<!--报名工作流-->
		<li id="114" role="presentation">
			<xsl:variable name="link">javascript:itm_lst.upd_item_workflow_prep(<xsl:value-of select="$itm_id"/>)</xsl:variable>
			<a aria-controls="userGroupLish" role="tab" href="{$link}">
				<xsl:value-of select="$lab_g_txt_btn_edit_workflow"/>
			</a>     
		</li>
	</xsl:template>
	
<!--================离线课程/考试=================-->
	<xsl:template name="classroom_tab_li">
		<!--课程信息-->
		<li id="200" role="presentation" class="active">
			<xsl:variable name="link">javascript:itm_lst.get_item_detail(<xsl:value-of select="$itm_id"/>)</xsl:variable>
			<a aria-controls="userGroupLish" role="tab" href="{$link}">
				<xsl:value-of select="$lab_cos_info"/>
			</a>     
		</li>
		<!--班级管理/考试场次-->
		<li id="201" role="presentation">
			<xsl:variable name="link">javascript:itm_lst.get_item_run_list(<xsl:value-of select="$itm_id"/>)</xsl:variable>
			<a aria-controls="userGroupLish" role="tab" href="{$link}">
					<xsl:choose>
						<xsl:when test="$itm_exam_ind = 'true'"><xsl:value-of select="$lab_const_exam_manage"/></xsl:when>
						<xsl:otherwise><xsl:value-of select="$lab_const_cls_manage"/></xsl:otherwise>
					</xsl:choose>
					
			</a>     
		</li>
		
		<!--先修课程-->
		<li id="202" role="presentation">
			<xsl:variable name="link">javascript:itmReq.itm_req_lst(<xsl:value-of select="$itm_id"/>)</xsl:variable>
			<a aria-controls="userGroupLish" role="tab" href="{$link}">
				<xsl:value-of select="$lab_requisite"/>
			</a>     
		</li>
		
		<!--目标学员-->
		<li id="203" role="presentation">
			<xsl:variable name="link">javascript:itm_lst.get_target_rule(<xsl:value-of select="$itm_id"/>, 'TARGET_LEARNER');</xsl:variable>
			<a aria-controls="userGroupLish" role="tab" href="{$link}">
				<xsl:value-of select="$lab_g_txt_btn_tarlrn"/>
			</a>     
		</li>
		
		<!--日程表-->
		<li id="204" role="presentation">
			<xsl:variable name="link">javascript:itm_lst.ae_get_course_lesson(<xsl:value-of select="$itm_id"/>)</xsl:variable>
			<a aria-controls="userGroupLish" role="tab" href="{$link}">
				<xsl:value-of select="$lab_ils_btn_content"/>
			</a>     
		</li>
		
		<!--设置内容模式-->
		<xsl:if test="$content_def = ''">
			<li id="206" role="presentation">
				<xsl:variable name="link">javascript:itm_lst.ae_get_online_content_info(<xsl:value-of select="$itm_id"/>)</xsl:variable>
				<a aria-controls="userGroupLish" role="tab" href="{$link}">
					<xsl:value-of select="$lab_g_txt_btn_content"/>
				</a>     
			</li>
		</xsl:if>
		<!--内容统一模式-->
		<xsl:if test="$content_def = 'PARENT'">
			<!--课程公告-->
			<li id="205" role="presentation">
				<xsl:variable name="link">javascript:ann.sys_lst('all','RES','<xsl:value-of select="$cos_res_id"/>','','','','','',true)</xsl:variable>
				<a aria-controls="userGroupLish" role="tab" href="{$link}">
					<xsl:value-of select="$lab_g_txt_btn_ann"/>
				</a>     
			</li>
			
		   <!--网上内容-->
			<li id="206" role="presentation">
				<xsl:variable name="link">javascript:course_lst.edit_cos('<xsl:value-of select="$cos_res_id"/>','<xsl:value-of select="$itm_type"/>','<xsl:value-of select="$create_run_ind"/>','<xsl:value-of select="$content_def"/>','<xsl:value-of select="$has_mod"/>')</xsl:variable>
				<a aria-controls="userGroupLish" role="tab" href="{$link}">
					<xsl:value-of select="$lab_g_txt_btn_content"/>
				</a>     
			</li>
			
			<!--先修模块设置-->
			<li id="207" role="presentation">
				<xsl:variable name="link">javascript:itm_lst.get_mod_pre(<xsl:value-of select="$itm_id"/>)</xsl:variable>
				<a aria-controls="userGroupLish" role="tab" href="{$link}">
					<xsl:value-of select="$lab_earlier_model"/>
				</a>     
			</li>
					
			<!--课程分数设置-->
			<li id="208" role="presentation">
				<xsl:variable name="link">javascript:cmt.get_score_scheme_list(<xsl:value-of select="$itm_id"/>)</xsl:variable>
				<a aria-controls="userGroupLish" role="tab" href="{$link}">
					<xsl:value-of select="$lab_course_result"/>
				</a>     
			</li>
		
			<!--结训条件设置-->
			<li id="209" role="presentation">
				<xsl:variable name="link">javascript:cmt.get_criteria(<xsl:value-of select="$itm_id"/>)</xsl:variable>
				<a aria-controls="userGroupLish" role="tab" href="{$link}">
					<xsl:value-of select="$lab_completion_cri"/>
				</a>     
			</li>
		</xsl:if>
			
		<!--评论管理-->
		<li id="210" role="presentation">
			<xsl:variable name="link">javascript: itm_lst.get_itm_comment_lst(<xsl:value-of select="$itm_id"/>)</xsl:variable>
			<a aria-controls="userGroupLish" role="tab" href="{$link}">
				<xsl:value-of select="$lab_g_txt_btn_comments"/>
			</a>     
		</li>
		
		<!--报名工作流-->
		<li id="211" role="presentation">
			<xsl:variable name="link">javascript:itm_lst.upd_item_workflow_prep(<xsl:value-of select="$itm_id"/>)</xsl:variable>
			<a aria-controls="userGroupLish" role="tab" href="{$link}">
				<xsl:value-of select="$lab_g_txt_btn_edit_workflow"/>
			</a>     
		</li>	
	</xsl:template>

<!--================班级=================-->
	<xsl:template name="run_tab_li">
		<!--课程信息-->
		<li id="300" role="presentation" class="active">
			<xsl:variable name="link">javascript:itm_lst.get_item_run_detail(<xsl:value-of select="$itm_id"/>)</xsl:variable>
			<a aria-controls="userGroupLish" role="tab" href="{$link}">
				<xsl:choose><xsl:when test="$itm_exam_ind='true'"><xsl:value-of select="$lab_run_exam_info"/></xsl:when>
				<xsl:otherwise><xsl:value-of select="$lab_run_class_info"/></xsl:otherwise></xsl:choose>
			</a>     
		</li>

		<!--目标学员-->
		<li id="301" role="presentation">
			<xsl:variable name="link">javascript:itm_lst.get_target_rule(<xsl:value-of select="$itm_id"/>, 'TARGET_ENROLLMENT');</xsl:variable>
			<a aria-controls="userGroupLish" role="tab" href="{$link}">
				<xsl:value-of select="$lab_run_tarlrn"/>
			</a>     
		</li>
		
		<!--先修课程-->
		<li id="302" role="presentation">
			<xsl:variable name="link">javascript:itmReq.itm_req_lst(<xsl:value-of select="$itm_id"/>)</xsl:variable>
			<a aria-controls="userGroupLish" role="tab" href="{$link}">
				<xsl:value-of select="$lab_requisite"/>
			</a>     
		</li>
		
		<!--处理报名-->
		<li id="303" role="presentation">
			<xsl:variable name="link">javascript:app.get_application_list('',<xsl:value-of select="$itm_id"/>,'','','','')</xsl:variable>
			<a aria-controls="userGroupLish" role="tab" href="{$link}">
				<xsl:value-of select="$lab_g_txt_btn_process_enrol"/>
			</a>     
		</li>
		
		<!--日程表-->
		<li id="304" role="presentation">
			<xsl:variable name="link">javascript:itm_lst.ae_get_run_lesson(<xsl:value-of select="$itm_id"/>)</xsl:variable>
			<a aria-controls="userGroupLish" role="tab" href="{$link}">
				<xsl:value-of select="$lab_ils_btn_content"/>
			</a>     
		</li>
		
		<!--开课通知-->
		<li id="305" role="presentation">
			<xsl:variable name="link">javascript:itm_lst.itm_ji_msg('<xsl:value-of select="$itm_id"/>')</xsl:variable>
			<a aria-controls="userGroupLish" role="tab" href="{$link}">
				<xsl:value-of select="$lab_g_txt_btn_joining_inst"/>
			</a>     
		</li>
		
		<!--预订设施-->
		<li id="306" role="presentation">
			<xsl:variable name="link">javascript:fm.get_itm_fm(<xsl:value-of select="$itm_id"/>,'<xsl:value-of select="$rsv_id"/>',rsv_itm_title)</xsl:variable>
			<a aria-controls="userGroupLish" role="tab" href="{$link}">
				<xsl:value-of select="$lab_g_txt_btn_book_sys"/>
			</a>     
		</li>
		
		<!--课程费用-->
		<li id="307" role="presentation">
			<xsl:variable name="link">javascript:itm_lst.get_item_cost(<xsl:value-of select="$itm_id"/>)</xsl:variable>
			<a aria-controls="userGroupLish" role="tab" href="{$link}">
				<xsl:value-of select="$lab_g_txt_btn_item_cost"/>
			</a>     
		</li>
		
		<!--课程公告-->
		<li id="308" role="presentation">
			<xsl:variable name="link">javascript:ann.sys_lst('all','RES','<xsl:value-of select="$cos_res_id"/>','','','','','',true)</xsl:variable>
			<a aria-controls="userGroupLish" role="tab" href="{$link}">
				<xsl:value-of select="$lab_g_txt_btn_ann"/>
			</a>     
		</li>
			
		<!--网上内容-->
		<li id="309" role="presentation">
			<xsl:variable name="link">javascript:course_lst.edit_cos('<xsl:value-of select="$cos_res_id"/>','<xsl:value-of select="$itm_type"/>','<xsl:value-of select="$create_run_ind"/>','<xsl:value-of select="$content_def"/>','<xsl:value-of select="$has_mod"/>')</xsl:variable>
			<a aria-controls="userGroupLish" role="tab" href="{$link}">
				<xsl:value-of select="$lab_g_txt_btn_content"/>
			</a>     
		</li>
			
		<!--先修模块设置-->
		<li id="310" role="presentation">
			<xsl:variable name="link">javascript:itm_lst.get_mod_pre(<xsl:value-of select="$itm_id"/>)</xsl:variable>
			<a aria-controls="userGroupLish" role="tab" href="{$link}">
				<xsl:value-of select="$lab_earlier_model"/>
			</a>     
		</li>
		
		<!--课程分数设置-->
		<li id="311" role="presentation">
			<xsl:variable name="link">javascript:cmt.get_score_scheme_list(<xsl:value-of select="$itm_id"/>)</xsl:variable>
			<a aria-controls="userGroupLish" role="tab" href="{$link}">
				<xsl:value-of select="$lab_course_result"/>
			</a>     
		</li>
		
		<!--结训条件设置-->
		<li id="312" role="presentation">
			<xsl:variable name="link">javascript:cmt.get_criteria(<xsl:value-of select="$itm_id"/>)</xsl:variable>
			<a aria-controls="userGroupLish" role="tab" href="{$link}">
				<xsl:value-of select="$lab_completion_cri"/>
			</a>     
		</li>

		<!--计分记录-->
		<li id="313" role="presentation">
			<xsl:variable name="link">javascript:cmt.get_scoring_itm_lst(<xsl:value-of select="$itm_id"/>)</xsl:variable>
			<a aria-controls="userGroupLish" role="tab" href="{$link}">
				<xsl:value-of select="$lab_score_record"/>
			</a>     
		</li>
		
		<!--出席率-->
		<li id="314" role="presentation">
			<xsl:variable name="link">javascript:attn.get_attd_rate_lst(<xsl:value-of select="$itm_id"/>)</xsl:variable>
			<a aria-controls="userGroupLish" role="tab" href="{$link}">
				<xsl:value-of select="$lab_att_rate"/>
			</a>     
		</li>
		
		<!--结训记录-->
		<li id="315" role="presentation">
			<xsl:variable name="link">javascript:attn.get_grad_record(<xsl:value-of select="$itm_id"/>)</xsl:variable>
			<a aria-controls="userGroupLish" role="tab" href="{$link}">
				<xsl:value-of select="$lab_grad_record"/>
			</a>     
		</li>
			
		<!--跟踪报告-->
		<li id="316" role="presentation">
			<xsl:variable name="link">javascript:rpt.open_cos_lrn_lst(<xsl:value-of select="$itm_id"/>)</xsl:variable>
			<a aria-controls="userGroupLish" role="tab" href="{$link}">
				<xsl:value-of select="$lab_tracking_rpt"/>
			</a>     
		</li>
			
		<!--评论管理-->
		<xsl:if test="$itm_exam_ind != 'true'">
			<li id="317" role="presentation">
				<xsl:variable name="link">javascript: itm_lst.get_itm_comment_lst(<xsl:value-of select="$itm_id"/>)</xsl:variable>
				<a aria-controls="userGroupLish" role="tab" href="{$link}">
					<xsl:value-of select="$lab_g_txt_btn_comments"/>
				</a>     
			</li>
		</xsl:if>
	</xsl:template>
	
<!--================项目式培训=================-->
	<xsl:template name="integrated_tab_li">
		<!--课程信息-->
		<li id="400" role="presentation" class="active">
			<xsl:variable name="link">javascript:itm_lst.get_item_detail(<xsl:value-of select="$itm_id"/>)</xsl:variable>
			<a aria-controls="userGroupLish" role="tab" href="{$link}">
				<xsl:value-of select="$lab_project_info"/>
			</a>     
		</li>
		<!--先修课程-->
		<li id="401" role="presentation">
			<xsl:variable name="link">javascript:itmReq.itm_req_lst(<xsl:value-of select="$itm_id"/>)</xsl:variable>
			<a aria-controls="userGroupLish" role="tab" href="{$link}">
				<xsl:value-of select="$lab_requisite"/>
			</a>     
		</li>
		
		<!--目标学员-->
		<li id="402" role="presentation">
			<xsl:variable name="link">javascript:itm_lst.get_target_rule(<xsl:value-of select="$itm_id"/>, 'TARGET_LEARNER');</xsl:variable>
			<a aria-controls="userGroupLish" role="tab" href="{$link}">
				<xsl:value-of select="$lab_g_txt_btn_tarlrn"/>
			</a>     
		</li>
		
		<!--处理报名<-->
		<li id="403" role="presentation">
			<xsl:variable name="link">javascript:app.get_application_list('',<xsl:value-of select="$itm_id"/>,'','','','')</xsl:variable>
			<a aria-controls="userGroupLish" role="tab" href="{$link}">
				<xsl:value-of select="$lab_g_txt_btn_process_enrol"/>
			</a>     
		</li>
		
		<!--课程包-->
		<li id="404" role="presentation">
			<xsl:variable name="link">javascript:itm_lst.get_course_list(<xsl:value-of select="$itm_id"/>)</xsl:variable>
			<a aria-controls="userGroupLish" role="tab" href="{$link}">
				<xsl:value-of select="$lab_g_txt_btn_courses"/>
			</a>     
		</li>

		<!--课程公告-->
		<li id="405" role="presentation">
			<xsl:variable name="link">javascript:ann.sys_lst('all','RES','<xsl:value-of select="$cos_res_id"/>','','','','','',true)</xsl:variable>
			<a aria-controls="userGroupLish" role="tab" href="{$link}">
				<xsl:value-of select="$lab_g_txt_btn_ann"/>
			</a>     
		</li>
			
		<!--结训记录-->
		<li id="406" role="presentation">
			<xsl:variable name="link">javascript:attn.get_grad_record(<xsl:value-of select="$itm_id"/>)</xsl:variable>
			<a aria-controls="userGroupLish" role="tab" href="{$link}">
				<xsl:value-of select="$lab_grad_record"/>
			</a>     
		</li>
			
		<!--评论管理-->
		<!--<li>
			<span class="wzb-list-3-num"/>
			<xsl:variable name="link">javascript: itm_lst.get_itm_comment_lst(<xsl:value-of select="$itm_id"/>)</xsl:variable>
			<a href="{$link}"><xsl:value-of select="$lab_g_txt_btn_comments"/></a> 
		</li>-->
		
		<!--报名工作流-->
		<li id="407" role="presentation">
			<xsl:variable name="link">javascript:itm_lst.upd_item_workflow_prep(<xsl:value-of select="$itm_id"/>)</xsl:variable>
			<a aria-controls="userGroupLish" role="tab" href="{$link}">
				<xsl:value-of select="$lab_g_txt_btn_edit_workflow"/>
			</a>     
		</li>
	</xsl:template>
	
	<!--================公开课=================-->
	<xsl:template name="audiovideo_tab_li">
		<!--课程信息-->
		<li id="500" role="presentation" class="active">
			<xsl:variable name="link">javascript:itm_lst.get_item_detail(<xsl:value-of select="$itm_id"/>)</xsl:variable>
			<a aria-controls="userGroupLish" role="tab" href="{$link}">
				<xsl:value-of select="$lab_cos_info"/>
			</a>     
		</li>
		
		<!--网上内容-->
		<li id="501" role="presentation">
			<xsl:variable name="link">javascript:course_lst.edit_cos(<xsl:value-of select="$cos_res_id"/>,'<xsl:value-of select="$itm_type"/>','false','','false')</xsl:variable>
			<a aria-controls="userGroupLish" role="tab" href="{$link}">
				<xsl:value-of select="$lab_g_txt_btn_content"/>
			</a>     
		</li>
		
		<!--评论管理-->
		<li id="502" role="presentation">
			<xsl:variable name="link">javascript: itm_lst.get_itm_comment_lst(<xsl:value-of select="$itm_id"/>)</xsl:variable>
			<a aria-controls="userGroupLish" role="tab" href="{$link}">
				<xsl:value-of select="$lab_g_txt_btn_comments"/>
			</a>     
		</li>
	</xsl:template>
</xsl:stylesheet>
