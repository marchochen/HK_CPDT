<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	version="1.0" xmlns:java="http://xml.apache.org/xalan/java"
	exclude-result-prefixes="java">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_sub_title.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="share/question_body_share.xsl"/>
	<xsl:import href="share/res_label_share.xsl"/>
	<xsl:import href="share/itm_nav_share.xsl"/>
	<xsl:import href="share/itm_gen_action_nav_share.xsl"/>	
	
	<xsl:output indent="yes"/>
	<xsl:variable name="mod_id" select="/student_report/student/test/@id"/>
	<xsl:variable name="course_id" select="/student_report/student/test/header/@course_id"/>
	<xsl:variable name="mod_title" select="/student_report/student/test/header/title/text()"/>
	<xsl:variable name="attempt_cur" select="/student_report/attempt_list/@current"/>
	<xsl:variable name="pgr_usr_id" select="/student_report/student/@ent_id"/>
	<xsl:variable name="tkh_id" select="/student_report/student/test/@tkh_id"/>
	<xsl:variable name="is_item" select="/student_report/is_item/text()"/>
<xsl:variable name="qid_list">
<xsl:for-each select="/student_report/student/test/body/question/header[@type='ES']">
	<xsl:value-of select="../@id"/>
	<xsl:if test="position() != last()">~</xsl:if>
</xsl:for-each>
</xsl:variable>
<xsl:variable name="num_es">
	<xsl:value-of select="count(/student_report/student/test/body/question/header[@type='ES'])"/>
</xsl:variable>
<xsl:variable name="is_graded">
	<xsl:choose>
		<xsl:when test="$num_es &gt; 0 and /student_report/cur_usr/role[@id='CDN_1']">false</xsl:when>
		<xsl:otherwise>true</xsl:otherwise>
	</xsl:choose>
</xsl:variable>	
<xsl:variable name="label_core_training_management_250" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_250')"/>
<xsl:variable name="label_core_training_management_393" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_393')"/>
<xsl:variable name="label_core_training_management_30" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_30')"/>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates select="/student_report/student"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="/student_report/student">
		<xsl:call-template name="wb_init_lab"/>
	</xsl:template>
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_grade_submission">評分</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_save">儲存</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_no_attp">嘗試次數：</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_grade_submission">评分</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_save">保存</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_no_attp">答题次数：</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_grade_submission">Grade test</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_save">Save</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_no_attp">Attempt number : </xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<!--内容模块-->
	<xsl:template name="content">
		<xsl:param name="lab_g_form_btn_save"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:param name="lab_no_attp"/>
		<xsl:param name="lab_grade_submission"/>
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_module.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<!--alert样式  -->
		     <script language="Javascript" type="text/javascript" src="{$wb_js_path}jquery.js"/> 
			
			<link rel="stylesheet" href="../static/js/jquery.qtip/jquery.qtip.css" />
			<script type="text/javascript" src="../static/js/jquery.dialogue.js"></script>
			<script type="text/javascript" src="../static/js/jquery.qtip/jquery.qtip.js"></script>
			
			<script language="Javascript" type="text/javascript" src="../../static/js/cwn_utils.js"/>
			<script type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/global_{$wb_cur_lang}.js"></script>
			<!--alert样式  end -->
			
			<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
					var module_lst = new wbModule
					var isExcludes = getUrlParam('isExcludes');
			]]></SCRIPT>
			<xsl:call-template name="new_css"/>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
			<form name="frmXml" method="post">
				<input type="hidden" name="pgr_usr_id" value="{$pgr_usr_id}"/>
				<input type="hidden" name="cmd" value="grade_all_que"/>
				<input type="hidden" name="mod_id" value="{$mod_id}"/>
				<input type="hidden" name="cos_id" value="{$course_id}"/>
				<input type="hidden" name="attempt_nbr" value="{$attempt_cur}"/>
				<input type="hidden" name="tkh_id" value="{$tkh_id}"/>
				<input type="hidden" name="que_id_lst" value="{$qid_list}"/>
				<input type="hidden" name="que_score_lst" value=""/>
				<input type="hidden" name="url_success" value=""/>
				<input type="hidden" name="url_failure" value=""/>
				<!-- header -->
				<xsl:choose>
					<xsl:when test="$is_item = true or $is_item = 'true'">	
						<xsl:call-template name="itm_action_nav">
							<xsl:with-param  name="cur_node_id">120</xsl:with-param>
						</xsl:call-template>

						<!--测评详细报告页面判断显示(课程或者是考试) 菜单样式以及模块显示-->
						<xsl:choose>
							<xsl:when test="//item/@exam_ind='false'">
								<xsl:call-template name="wb_ui_hdr">
									<xsl:with-param name="belong_module">FTN_AMD_ITM_COS_MAIN</xsl:with-param>
									<xsl:with-param name="parent_code">FTN_AMD_ITM_COS_MAIN</xsl:with-param>
								</xsl:call-template>
							</xsl:when>
							<xsl:otherwise>
								<xsl:call-template name="wb_ui_hdr">
									<xsl:with-param name="belong_module">FTN_AMD_EXAM_MGT</xsl:with-param>
									<xsl:with-param name="parent_code">FTN_AMD_EXAM_MGT</xsl:with-param>
								</xsl:call-template>
							</xsl:otherwise>
						</xsl:choose>
						
						<xsl:call-template name="wb_ui_nav_link">
							<xsl:with-param name="text">
								<xsl:apply-templates select="/student_report/item/nav/item" mode="nav">
									<xsl:with-param name="lab_run_info"><xsl:value-of select="$label_core_training_management_250" /></xsl:with-param>
								</xsl:apply-templates>
								<span class="NavLink">
									<xsl:text>&#160;&gt;&#160;</xsl:text>
									<a href="javascript:itm_lst.itm_evaluation_report({$course_id },'TST')"><xsl:value-of select="$label_core_training_management_393"/></a>
									<xsl:text>&#160;&gt;&#160;</xsl:text>
									<a href="Javascript:module_lst.view_submission_lst({$course_id },{$mod_id },'all')"><xsl:value-of select="$label_core_training_management_30"/></a>
									<xsl:text>&#160;&gt;&#160;</xsl:text>
									<xsl:value-of select="$lab_grade_submission"/> - <xsl:value-of select="$mod_title"/>
								</span>
							</xsl:with-param>
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<!--测评详细报告页面判断显示(课程或者是考试) 菜单样式以及模块显示-->
						<xsl:choose>
							<xsl:when test="//item/@exam_ind='false'">
								<xsl:call-template name="wb_ui_hdr">
									<xsl:with-param name="belong_module">FTN_AMD_ITM_COS_MAIN</xsl:with-param>
									<xsl:with-param name="parent_code">FTN_AMD_ITM_COS_MAIN</xsl:with-param>
								</xsl:call-template>
							</xsl:when>
							<xsl:otherwise>
								<xsl:call-template name="wb_ui_hdr">
									<xsl:with-param name="belong_module">FTN_AMD_EXAM_MGT</xsl:with-param>
									<xsl:with-param name="parent_code">FTN_AMD_EXAM_MGT</xsl:with-param>
								</xsl:call-template>
							</xsl:otherwise>
						</xsl:choose>
						<xsl:call-template name="wb_ui_title">
							<xsl:with-param name="text" >
							<xsl:value-of select="$lab_grade_submission"/> - <xsl:value-of select="$mod_title"/>
							</xsl:with-param>
						</xsl:call-template>
					</xsl:otherwise>
				</xsl:choose>
				<!-- header end -->
				
				<xsl:call-template name="wb_ui_sub_title">
					<xsl:with-param name="text">
						<xsl:value-of select="@display_bil"/> (<xsl:value-of select="@id"/>)</xsl:with-param>
				</xsl:call-template>
				<xsl:for-each select="test/body/question[body/interaction/@type='ES']">
					<!-- question info start -->
					<xsl:comment>= Question ID : <xsl:value-of select="@id"/> =============================</xsl:comment>
					<!-- heading -->
					<xsl:call-template name="wb_ui_head">
						<xsl:with-param name="text" select="concat(@order, '. ', header/title)"/>
					</xsl:call-template>
					<xsl:call-template name="wb_ui_line"/>
					<!-- question info end -->
					<xsl:call-template name="question_body">
						<xsl:with-param name="width" select="$wb_gen_table_width"/>
						<xsl:with-param name="que_id" select="@id"/>
						<xsl:with-param name="view">RPT</xsl:with-param>
						<xsl:with-param name="grade">true</xsl:with-param>
					</xsl:call-template>
					<xsl:if test="position() != last()">
					<xsl:call-template name="wb_ui_space"/>
					</xsl:if>
				</xsl:for-each>
				<xsl:call-template name="wb_ui_line"/>
				<table border="0" cellpadding="3" cellspacing="0" width="{$wb_gen_table_width}">
					<tr align="center">
						<td>
							<xsl:call-template name="wb_gen_form_button">
								<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_save"/>
								<xsl:with-param name="wb_gen_btn_href">javascript:module_lst.grade_submission_exec(document.frmXml, '<xsl:value-of select="$is_item" />','<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
							</xsl:call-template>					
							<img src="{$wb_img_path}tp.gif" border="0" width="1" height="1"/>			
							<xsl:call-template name="wb_gen_form_button">
								<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
								<xsl:with-param name="wb_gen_btn_href">javascript:history.go(-1)</xsl:with-param>
							</xsl:call-template>
						</td>
					</tr>
				</table>
			</form>
		</body>
	</xsl:template>
</xsl:stylesheet>
