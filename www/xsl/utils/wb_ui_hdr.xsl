<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<xsl:import href="../utils/wb_gen_nav_home.xsl"/>
	<xsl:import href="../utils/wb_ui_space.xsl"/>
	<xsl:import href="../share/label_function.xsl"/>
	<xsl:import href="../share/label_role.xsl"/>
	<xsl:import href="../share/label_rpt.xsl"/>
	<xsl:variable name="function" select="//granted_functions/functions"/>
	<xsl:variable name="role_id" select="//cur_usr/role/@id"/>
	<xsl:variable name="report" select="//granted_functions/rte_list"/>
	
	<!-- ======================================== top menu type =============================================== -->
	<!--=========== 培训管理员 ===========-->
	<!-- 需求管理 -->
	<xsl:variable name="menu_type_demand_mgt">FTN_AMD_DEMAND_MGT</xsl:variable>
	<!-- 计划管理 -->
	<xsl:variable name="menu_type_plan_mgt">FTN_AMD_PLAN_MGT</xsl:variable>
	<!-- 培训管理 -->
	<xsl:variable name="menu_type_training_mgt">FTN_AMD_TRAINING_MGT</xsl:variable>
	<!-- 评价培训结果 -->
	<xsl:variable name="menu_type_training_result">training_result</xsl:variable>
	<!-- 基础设定 -->
	<xsl:variable name="menu_type_basis_set">basis_set</xsl:variable>
	<!-- 社区管理 -->
	<xsl:variable name="menu_type_community_mgt">community_management</xsl:variable>
	<!-- 个人设定 -->
	<xsl:variable name="menu_type_self_set">self_set</xsl:variable>
	
	<!--=========== 系统管理员 ===========-->
	<!-- 培训管理 -->
	<xsl:variable name="menu_type_adm_training_mgt">FTN_AMD_TRAINING_MGT</xsl:variable>
	<!-- 系统管理 -->
	<xsl:variable name="menu_type_sys_mgt">system_management</xsl:variable>
	<!-- 社区管理 -->
	<xsl:variable name="menu_type_adm_community_mgt">adm_community_management</xsl:variable>
	
	<!--=========== 讲师 ===========-->
	<!-- 教学管理 -->
	<xsl:variable name="menu_type_teach_mgt">teach_management</xsl:variable>
	<!-- 社区 -->
	<xsl:variable name="menu_type_community">community</xsl:variable>

	<xsl:variable name="lab_300" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '300')"/>
	<xsl:variable name="lab_530" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '530')"/>
	<xsl:variable name="lab_634" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '634')"/>
	<xsl:variable name="lab_635" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '635')"/>
	<xsl:variable name="lab_15" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '15')"/>
	<xsl:variable name="lab_636" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '636')"/>
	<xsl:variable name="lab_637" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '637')"/>
	<xsl:variable name="lab_638" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '638')"/>
	<xsl:variable name="lab_640" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '640')"/>
	<xsl:variable name="lab_643" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '643')"/>
	<xsl:variable name="lab_644" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '644')"/>
	<xsl:variable name="lab_641" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '641')"/>
	<xsl:variable name="lab_639" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '639')"/>
	<xsl:variable name="lab_642" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '642')"/>
	<xsl:variable name="lab_311" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '311')"/>
	<xsl:variable name="lab_9" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '9')"/>
	<xsl:variable name="lab_sys_role_main" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'hws28')"/>
	<xsl:variable name="lab_646" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_ftn_ITM_MAIN2')"/>
	<xsl:variable name="lab_647" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '638_')"/>
	<xsl:variable name="lab_648" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_ftn_EVN_MAIN')"/>
	<xsl:variable name="lab_en" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '891')"/>
	<xsl:variable name="lab_gb" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '892')"/>
	<xsl:variable name="lab_ch" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '893')"/>
	<xsl:template name = "wb_ui_hdr">
		<xsl:param name="belong_module"></xsl:param>
		<xsl:param name="parent_code"></xsl:param>
		<xsl:param name="page_title"></xsl:param>
		<xsl:choose>
		    <!-- 培训管理 -->
			<xsl:when test="$belong_module = 'FTN_AMD_ITM_COS_MAIN'        or $belong_module = 'ITM_OFFINE_MAIN'
							or $belong_module = 'FTN_AMD_CERTIFICATE_MAIN' or $belong_module = 'FTN_AMD_OPEN_COS_MAIN'
							or $belong_module = 'FTN_AMD_RES_MAIN'     or $belong_module = 'FTN_AMD_COS_EVN_MAIN'
							or $belong_module = 'FTN_AMD_CAT_MAIN'         or $belong_module = 'FTN_AMD_COURSE_ASSIGN'
							or $belong_module = 'FTN_AMD_TEACHING_COURSE_LIST'">
				<input type="hidden" name="belong_module" value="FTN_AMD_TRAINING_MGT"/>
			</xsl:when>
			
		    <!-- 考试管理 -->
			<xsl:when test="$belong_module = 'FTN_AMD_EXAM_MGT'">
				<input type="hidden" name="belong_module" value="FTN_AMD_EXAM_MGT"/>
			</xsl:when>
			
			<!-- 资讯管理 -->
			<xsl:when test="$belong_module = 'FTN_AMD_MSG_MAIN' or $belong_module = 'FTN_AMD_ARTICLE_MAIN'">
				<input type="hidden" name="belong_module" value="FTN_AMD_ARTICLE_MGT"/>
			</xsl:when>
			
			<!-- 需求管理-->
			<xsl:when test="$belong_module = 'FTN_AMD_EVN_MAIN'">
				<input type="hidden" name="belong_module" value="FTN_AMD_DEMAND_MGT"/>
			</xsl:when>
			
			<!-- 计划管理-->
			<xsl:when test="$belong_module = 'FTN_AMD_PLAN_CARRY_OUT'      or $belong_module = 'FTN_AMD_YEAR_PALN'
							or $belong_module = 'FTN_AMD_MAKEUP_PLAN'      or $belong_module = 'FTN_AMD_YEAR_PLAN_APPR'
							or $belong_module = 'FTN_AMD_MAKEUP_PLAN_APPR' or $belong_module = 'FTN_AMD_YEAR_SETTING'">
				<input type="hidden" name="belong_module" value="FTN_AMD_PLAN_MGT"/>
			</xsl:when>
			
			<!-- 社区管理-->
			<xsl:when test="$belong_module = 'FTN_AMD_Q_AND_A_MAIN' or $belong_module = 'FTN_AMD_CREDIT_SETTING_MAIN'">
				<input type="hidden" name="belong_module" value="FTN_AMD_SNS_MGT"/>
			</xsl:when>
			
			<!-- 用户管理-->
			<xsl:when test="$belong_module = 'FTN_AMD_POSITION_MAIN' or $belong_module = 'FTN_AMD_USR_INFO' or $belong_module='FTN_AMD_USR_REGIETER_APP'
							or $belong_module='FTN_AMD_USR_ACTIVATE' or $belong_module='FTN_AMD_GRADE_MAIN'">
				<input type="hidden" name="belong_module" value="FTN_AMD_USR_INFO_MGT"/>
			</xsl:when>
			
			<!-- 系统设置-->
			<xsl:when test=" $belong_module = 'FTN_AMD_SYS_ROLE_MAIN'  or $belong_module = 'FTN_AMD_POSTER_MAIN' or $belong_module = 'FTN_AMD_SYS_SETTING_LOG'
							or $belong_module = 'FTN_AMD_SYS_SETTING_MAIN' or $belong_module = 'FTN_AMD_EIP_MAIN'  or $belong_module = 'FTN_AMD_MESSAGE_TEMPLATE_MAIN'" >
				<input type="hidden" name="belong_module" value="FTN_AMD_SYSTEM_SETTING_MGT"/>
			</xsl:when>
			
			<!-- 基础数据管理-->
			<xsl:when test="$belong_module = 'FTN_AMD_INT_INSTRUCTOR_MAIN' 
							or $belong_module = 'FTN_AMD_EXT_INSTRUCTOR_MAIN'   or $belong_module = 'FTN_AMD_TC_MAIN'
							or $belong_module = 'FTN_AMD_SUPPLIER_MAIN'  ">
				<input type="hidden" name="belong_module" value="FTN_AMD_BASE_DATA_MGT"/>
			</xsl:when>
			
			<!-- 学习地图-->
			<xsl:when test="$belong_module = 'FTN_AMD_PROFESSION_MAIN'">
				<input type="hidden" name="belong_module" value="FTN_AMD_STUDY_MAP_MGT"/>
			</xsl:when>
			
			<!-- 设施管理-->
			<xsl:when test="$belong_module = 'FTN_AMD_FACILITY_BOOK_CREATE'">
				<input type="hidden" name="belong_module" value="FTN_AMD_FACILITY_MGT"/>
			</xsl:when>
			
			<!-- 培训报表-->
			<xsl:when test="$belong_module = 'FTN_AMD_TRAINING_REPORT_MGT'">
				<input type="hidden" name="belong_module" value="FTN_AMD_TRAINING_REPORT_MGT"/>
			</xsl:when>
			<xsl:when test="$belong_module = 'TASK_MANAGEMENT'">
				<input type="hidden" name="belong_module" value="TASK_MANAGEMENT"/>
			</xsl:when>
		</xsl:choose>
		
		<xsl:if test="$parent_code != ''">
			<input type="hidden" name="parent_code" value="{$parent_code}"/>
		</xsl:if>
		
		<xsl:if test="$page_title != ''">
			<input type="hidden" name="page_title" value="{$page_title}"/>
		</xsl:if>
	</xsl:template>
	<xsl:template name="wb_ui_hdr__">
		<xsl:param name="width" select="$wb_gen_table_width"/>
		<xsl:param name="role_box"/>
		<xsl:param name="mode"/>
		<xsl:param name="nav_function"/>
		<xsl:param name="show_home">true</xsl:param>
		<xsl:param name="show_back">true</xsl:param>
		<xsl:param name="show_functions">true</xsl:param>
		<table width="{$width}" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td valign="top" align="right" style="background-image:url({$wb_img_skin_path}banner.jpg)" height="127px">
					<!--<img src="{$wb_img_skin_path}banner.jpg" border="0"/>-->
					<span>
					<a class="banner_user_info_link" href="javascript:changeLang('en-us');"><xsl:value-of select="$lab_en"/></a>
					<span class="BannerText">&#160;|&#160;</span>
					<a class="banner_user_info_link" href="javascript:changeLang('zh-cn')"><xsl:value-of select="$lab_gb"/></a>
					<span class="BannerText">&#160;|&#160;</span>
					<a class="banner_user_info_link" href="javascript:changeLang('zh-hk')"><xsl:value-of select="$lab_ch"/></a>
					<span class="BannerText">&#160;&#160;&#160;&#160;&#160;&#160;</span>
					
						<span class="BannerText"><xsl:value-of select="//cur_usr/@display_bil"/>&#160;|&#160;</span>
						<xsl:if test="$show_back='true'">
							<span class="BannerText">
								<a href="javascript:history.back()" class="banner_user_info_link">
									<xsl:value-of select="$lab_300"/>
								</a>&#160;|&#160;
							</span>
						</xsl:if>	
						<xsl:if test="//cur_usr/login/cur_login_status/@method='normal_login'">
							<a class="banner_user_info_link" href="javascript:wb_utils_logout('{$wb_lang}')"><xsl:value-of select="$lab_530"/></a>
						</xsl:if>&#160;
					</span>	
				</td>
				
			</tr>
		</table>
		<!--
		<table width="{$width}" border="0" cellspacing="0" cellpadding="0" class="TopBg">
			<tr>
				<td height="15" align="right" valign="middle">
					<xsl:if test="$show_back='true'">
						<a href="javascript:history.back()" class="TopText">
							<xsl:value-of select="$lab_back"/>
						</a>
						<xsl:if test="//cur_usr/login/cur_login_status/@method='normal_login'">
							<img src="{$wb_img_skin_path}wb_nav_sep.gif" border="0" align="absmiddle" hspace="1"/>
						</xsl:if>
					</xsl:if>
					<xsl:if test="//cur_usr/login/cur_login_status/@method='normal_login'">
						<a href="javascript:wb_utils_logout('{$wb_lang}')" class="TopText">
							<xsl:value-of select="$lab_logout"/>
						</a>
					</xsl:if>
					<img border="0" height="1" width="3" src="{$wb_img_path}tp.gif"/>
				</td>
			</tr>
		</table>
		-->
		<table width="{$width}" height="40" style="background:#f0f0f0" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td class="top_menu_2">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td class="top_menu_left_2">
								<div class="horizontalcssmenu_2">
									<ul id="cssmenu2">
										<xsl:if test="$show_home='true'">
											<li class="horizontalcssmenu_first_2"><xsl:call-template name="wb_gen_nav_home"/></li>
										</xsl:if>
										<xsl:if test="$show_functions='true'">
											<!--=============================== 培训管理员 ==============================-->
											<xsl:if test="starts-with($role_id, 'TADM_')">
												<li class="horizontalcssmenu_top_2">
													<!--======= 需求管理 =======-->
													<a href="#" style="width:90px"><xsl:value-of select="$lab_634"/></a>
													<ul id="cssmenu_g1">
														<xsl:call-template name="nav_function">
															<xsl:with-param name="menu_type" select="$menu_type_demand_mgt"/>
														</xsl:call-template>
													</ul>
												</li>
												<xsl:if test="$has_plan_mgt = 'true'">
													<li class="horizontalcssmenu_top_2">
														<!--======= 计划管理 =======-->
														<a href="#" style="width:90px"><xsl:value-of select="$lab_640"/></a>
														<ul id="cssmenu_g2">
															<xsl:call-template name="nav_function">
																<xsl:with-param name="menu_type" select="$menu_type_plan_mgt"/>
															</xsl:call-template>
														</ul>
													</li>
												</xsl:if>
												<li class="horizontalcssmenu_top_2">
													<!--======= 培训管理 =======-->
													<a href="#" style="width:90px"><xsl:value-of select="$lab_637"/></a>
													<ul id="cssmenu_g3">
														<xsl:call-template name="nav_function">
															<xsl:with-param name="menu_type" select="$menu_type_training_mgt"/>
														</xsl:call-template>
													</ul>
												</li>
												<li class="horizontalcssmenu_top_2">
													<!--======= 评价培训结果 =======-->
													<a href="#" style="width:90px"><xsl:value-of select="$lab_638"/></a>
													<ul id="cssmenu_g4">
														<xsl:call-template name="nav_function">
															<xsl:with-param name="menu_type" select="$menu_type_training_result"/>
														</xsl:call-template>
													</ul>
												</li>
												<li class="horizontalcssmenu_top_2">
													<!--======= 基础设定 =======-->
													<a href="#" style="width:90px"><xsl:value-of select="$lab_643"/></a>
													<ul id="cssmenu_g5">
														<xsl:call-template name="nav_function">
															<xsl:with-param name="menu_type" select="$menu_type_basis_set"/>
														</xsl:call-template>
													</ul>
												</li>
												<li class="horizontalcssmenu_top_2">
													<!--======= 社区管理 =======-->
													<a href="#" style="width:90px"><xsl:value-of select="$lab_644"/></a>
													<ul id="cssmenu_g6">
														<xsl:call-template name="nav_function">
															<xsl:with-param name="menu_type" select="$menu_type_community_mgt"/>
														</xsl:call-template>
													</ul>
												</li>
												<li class="horizontalcssmenu_top_2">
													<!--======= 个人设定 =======-->
													<a href="#" style="width:90px"><xsl:value-of select="$lab_641"/></a>
													<ul id="cssmenu_g7">
														<xsl:call-template name="nav_function">
															<xsl:with-param name="menu_type" select="$menu_type_self_set"/>
														</xsl:call-template>
													</ul>
												</li>
											</xsl:if>
											<!--=============================== 系统管理员 ==============================-->
											<xsl:if test="starts-with($role_id, 'ADM_')">
												<li class="horizontalcssmenu_top_2">
													<!--======= 培训管理 =======-->
													<a href="#" style="width:90px"><xsl:value-of select="$lab_637"/></a>
													<ul id="cssmenu_g1">
														<xsl:call-template name="nav_function">
															<xsl:with-param name="menu_type" select="$menu_type_adm_training_mgt"/>
														</xsl:call-template>
													</ul>
												</li>
												<li class="horizontalcssmenu_top_2">
													<!--======= 系统管理 =======-->
													<a href="#" style="width:90px"><xsl:value-of select="$lab_639"/></a>
													<ul id="cssmenu_g2">
														<xsl:call-template name="nav_function">
															<xsl:with-param name="menu_type" select="$menu_type_sys_mgt"/>
														</xsl:call-template>
													</ul>
												</li>
												<li class="horizontalcssmenu_top_2">
													<!--======= 社区管理 =======-->
													<a href="#" style="width:90px"><xsl:value-of select="$lab_644"/></a>
													<ul id="cssmenu_g3">
														<xsl:call-template name="nav_function">
															<xsl:with-param name="menu_type" select="$menu_type_adm_community_mgt"/>
														</xsl:call-template>
													</ul>
												</li>
												<li class="horizontalcssmenu_top_2">
													<!--======= 个人设定 =======-->
													<a href="#" style="width:90px"><xsl:value-of select="$lab_641"/></a>
													<ul id="cssmenu_g4">
														<xsl:call-template name="nav_function">
															<xsl:with-param name="menu_type" select="$menu_type_self_set"/>
														</xsl:call-template>
													</ul>
												</li>
											</xsl:if>
											<!--=============================== 讲师 ==============================-->
											<xsl:if test="starts-with($role_id, 'INSTR_')">
												<li class="horizontalcssmenu_top_2">
													<!--======= 教学管理 =======-->
													<a href="#" style="width:90px"><xsl:value-of select="$lab_642"/></a>
													<ul id="cssmenu_g1">
														<xsl:call-template name="nav_function">
															<xsl:with-param name="menu_type" select="$menu_type_teach_mgt"/>
														</xsl:call-template>
													</ul>
												</li>
												<li class="horizontalcssmenu_top_2">
													<!--======= 社区 =======-->
													<a href="#" style="width:90px"><xsl:value-of select="$lab_311"/></a>
													<ul id="cssmenu_g2">
														<xsl:call-template name="nav_function">
															<xsl:with-param name="menu_type" select="$menu_type_community"/>
														</xsl:call-template>
													</ul>
												</li>
												<li class="horizontalcssmenu_top_2">
													<!--======= 个人设定 =======-->
													<a href="#" style="width:90px"><xsl:value-of select="$lab_641"/></a>
													<ul id="cssmenu_g3">
														<xsl:call-template name="nav_function">
															<xsl:with-param name="menu_type" select="$menu_type_self_set"/>
														</xsl:call-template>
													</ul>
												</li>
											</xsl:if>
											<!--=============================== 考试监考员 ==============================-->
											<xsl:if test="starts-with($role_id, 'EXA_')">
												<li class="horizontalcssmenu_top_2">
													<!--======= 培训管理 =======-->
													<a href="#" style="width:90px"><xsl:value-of select="$lab_637"/></a>
													<ul id="cssmenu_g1">
														<xsl:call-template name="nav_function">
															<xsl:with-param name="menu_type" select="$menu_type_training_mgt"/>
														</xsl:call-template>
													</ul>
												</li>
												<li class="horizontalcssmenu_top_2">
													<!--======= 个人设定 =======-->
													<a href="#" style="width:90px"><xsl:value-of select="$lab_641"/></a>
													<ul id="cssmenu_g2">
														<xsl:call-template name="nav_function">
															<xsl:with-param name="menu_type" select="$menu_type_self_set"/>
														</xsl:call-template>
													</ul>
												</li>
											</xsl:if>
											<xsl:if test="starts-with($role_id, 'ROL')">
											    <xsl:if test="java:com.cw.wizbank.util.RoleManager.jurgetRoleHadFunction($role_id, 'NEED_MANAGER') = 'true'">
												    <li class="horizontalcssmenu_top_2">
														<!--======= 需求管理 =======-->
														<a href="#" style="width:90px"><xsl:value-of select="$lab_634"/></a>
														<ul id="cssmenu_g1">
															<xsl:call-template name="nav_function">
																<xsl:with-param name="menu_type" select="$menu_type_demand_mgt"/>
															</xsl:call-template>
														</ul>
													</li>
												</xsl:if>
												<xsl:if test="$has_plan_mgt = 'true'">
													<li class="horizontalcssmenu_top_2">
														<!--======= 计划管理 =======-->
														<a href="#" style="width:90px"><xsl:value-of select="$lab_640"/></a>
														<ul id="cssmenu_g2">
															<xsl:call-template name="nav_function">
																<xsl:with-param name="menu_type" select="$menu_type_plan_mgt"/>
															</xsl:call-template>
														</ul>
													</li>
												</xsl:if>
												<!--<li class="horizontalcssmenu_top_2">
													<a style="width:90px"><xsl:value-of select="$lab_634"/></a>
													<ul id="cssmenu_g1">
														<xsl:call-template name="nav_function">
															<xsl:with-param name="menu_type" select="$menu_type_demand_mgt"/>
														</xsl:call-template>
													</ul>
												</li>-->
												<xsl:if test="java:com.cw.wizbank.util.RoleManager.jurgetRoleHadFunction($role_id, 'ITM_MAIN') = 'true'">
													<li class="horizontalcssmenu_top_2">
														<!--======= 培训管理 =======-->
														<a href="#" style="width:90px"><xsl:value-of select="$lab_637"/></a>
														<ul id="cssmenu_g3">
															<xsl:call-template name="nav_function">
																<xsl:with-param name="menu_type" select="$menu_type_training_mgt"/>
															</xsl:call-template>
														</ul>
													</li>
												</xsl:if>
												<xsl:if test="java:com.cw.wizbank.util.RoleManager.jurgetRoleHadFunction($role_id, 'RPT_LINK') = 'true'">
													<li class="horizontalcssmenu_top_2">
														<!--======= 评价培训结果 =======-->
														<a href="#" style="width:90px"><xsl:value-of select="$lab_638"/></a>
														<ul id="cssmenu_g4">
															<xsl:call-template name="nav_function">
																<xsl:with-param name="menu_type" select="$menu_type_training_result"/>
															</xsl:call-template>
														</ul>
													</li>
												</xsl:if>
												<xsl:if test="java:com.cw.wizbank.util.RoleManager.jurgetRoleHadFunctions($role_id, 'FTN_AMD_TC_MAIN,FTN_AMD_CAT_MAIN,FTN_AMD_USR_INFO,CREDIT_OTHER_MAIN') = 'true'">
													<li class="horizontalcssmenu_top_2">
														<!--======= 基础设定 =======-->
														<a href="#" style="width:90px"><xsl:value-of select="$lab_643"/></a>
														<ul id="cssmenu_g5">
															<xsl:call-template name="nav_function">
																<xsl:with-param name="menu_type" select="$menu_type_basis_set"/>
															</xsl:call-template>
														</ul>
													</li>
												</xsl:if>
												<xsl:if test="java:com.cw.wizbank.util.RoleManager.jurgetRoleHadFunctions($role_id, 'KNOW_MAIN,STUDY_GROUP_MAIN,FOR_MAIN,FTN_AMD_MSG_MAIN,MSG_MGT_SYS') = 'true'">
													<li class="horizontalcssmenu_top_2">
														<!--======= 社区管理 =======-->
														<a href="#" style="width:90px"><xsl:value-of select="$lab_644"/></a>
														<ul id="cssmenu_g6">
															<xsl:call-template name="nav_function">
																<xsl:with-param name="menu_type" select="$menu_type_community_mgt"/>
															</xsl:call-template>
														</ul>
													</li>
												</xsl:if>
												<xsl:if test="java:com.cw.wizbank.util.RoleManager.jurgetRoleHadFunctions($role_id, 'USR_OWN_PREFER,USR_OWN_MAIN,USR_PWD_UPD') = 'true'">
													<li class="horizontalcssmenu_top_2">
														<!--======= 个人设定 =======-->
														<a href="#" style="width:90px"><xsl:value-of select="$lab_641"/></a>
														<ul id="cssmenu_g7">
															<xsl:call-template name="nav_function">
																<xsl:with-param name="menu_type" select="$menu_type_self_set"/>
															</xsl:call-template>
														</ul>
													</li>
												</xsl:if>
											</xsl:if>											
										</xsl:if>
									</ul>
								</div>
							</td>
							<xsl:if test="//granted_roles">
							<td class="top_menu_right_2">
								<table border="0" cellspacing="0" cellpadding="0" style=""  height="25">
									<tr>
										<td align="left">
												<a class="wzb-dropdown" id="role-select">
												<xsl:for-each select="//granted_roles/role">
														<xsl:if test="@id = $role_id">
															<xsl:variable name="lab_rol_id">
																<xsl:choose>
																	<xsl:when test="not(contains(@id,'_'))"></xsl:when>
																	<xsl:otherwise><xsl:value-of select="concat('lab_rol_',substring-before(@id,'_'))"/></xsl:otherwise>
																</xsl:choose>
															</xsl:variable>
															<xsl:choose>
																<xsl:when test="$lab_rol_id =''"><xsl:value-of select="@title"/></xsl:when>
																<xsl:otherwise>
																	<xsl:value-of select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, $lab_rol_id)"/>																	
																</xsl:otherwise>
															</xsl:choose>
														</xsl:if>
													</xsl:for-each>
												<img class="wzb-dropdown-icon wzb-dropdown-icon-down" src="{$wb_img_path}/tp.gif" />
											</a>
											<div class="wzb-dropdown-list" id="role-select-list">
												<xsl:for-each select="//granted_roles/role">
													<div class="wzb-dropdown-list-item" data-value="{@id}">
														<xsl:call-template name="get_rol_title">
															<xsl:with-param name="is_menu">true</xsl:with-param>
														</xsl:call-template>
													</div>
												</xsl:for-each>
											</div>
										</td>
									</tr>
								</table>
							</td>
							</xsl:if>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<xsl:call-template name="wb_ui_space"/>
	</xsl:template>
	
	<!--============================= role_box ==========================================-->
	<xsl:template match="role" mode="wb_gen_role">
		<option value="{@id}">
			<xsl:if test="@id = $role_id">
				<xsl:attribute name="selected">selected</xsl:attribute>
			</xsl:if>
			<xsl:call-template name="get_rol_title"/>
		</option>
	</xsl:template>
	<!--============================= menu ==========================================-->
	<xsl:template name="nav_function">
		<xsl:param name="menu_type"/>
		<xsl:choose>
		<!--==================================== 培训管理员 =======================================-->
			<!--=================================== 需求管理 ===================================-->
			<xsl:when test="$menu_type = $menu_type_demand_mgt">
				
				<xsl:if test="$function/function[@id='EVN_MAIN']">
				<!-- <span class="HomeLinkGroup"><xsl:value-of select="$lab_648"/></span> -->
					<li>
						<a href="javascript:wb_utils_nav_go('EVN_MAIN',{//cur_usr/@ent_id},'{$wb_lang}')">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">EVN_MAIN</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
				</xsl:if>
				
				<xsl:if test="$function/function[@id='CM_MAIN'] or $function/function[@id='CM_ASS_MAIN'] 
						or $function/function[@id='CM_SKL_ANALYSIS']">
					<!-- <span class="HomeLinkGroup"><xsl:value-of select="$lab_635"/></span> -->
				</xsl:if>
				
				<xsl:if test="$function/function[@id='CM_MAIN']">
					<li>
						<a href="javascript:wb_utils_nav_go('CM_MAIN',{//cur_usr/@ent_id},'{$wb_lang}')">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">CM_MAIN</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
				</xsl:if>
				<xsl:if test="$function/function[@id='CM_ASS_MAIN']">
					<li>
						<a href="javascript:wb_utils_nav_go('CM_ASS_MAIN',{//cur_usr/@ent_id},'{$wb_lang}')">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">CM_ASS_MAIN</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
				</xsl:if>
				<xsl:if test="$function/function[@id='CM_SKL_ANALYSIS']">
					<li>
						<a href="javascript:wb_utils_nav_go('CM_SKL_ANALYSIS',{//cur_usr/@ent_id},'{$wb_lang}')">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">CM_SKL_ANALYSIS</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
				</xsl:if>
			</xsl:when>
			<!--=================================== 计划管理 ===================================-->
			<xsl:when test="$menu_type = $menu_type_plan_mgt">
				<xsl:if test="$function/function[@id='FTN_AMD_PLAN_CARRY_OUT'] or $function/function[@id='FTN_AMD_YEAR_PALN'] 
						or $function/function[@id='FTN_AMD_MAKEUP_PLAN']">
					<!-- <span class="HomeLinkGroup"><xsl:value-of select="$lab_15"/></span> -->
				</xsl:if>
				
				<xsl:if test="$function/function[@id='FTN_AMD_PLAN_CARRY_OUT']">
					<xsl:variable name="js">
						<xsl:choose>
							<xsl:when test="//meta/tc_enabled='true'">javascript:wb_utils_nav_go('FTN_AMD_PLAN_CARRY_OUT')</xsl:when>
							<xsl:otherwise>javascript:wb_utils_nav_go('FTN_AMD_PLAN_CARRY_OUT')</xsl:otherwise>
						</xsl:choose>
					</xsl:variable>
					<li>
						<a href="{$js}">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">FTN_AMD_PLAN_CARRY_OUT</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
				</xsl:if>
				<xsl:if test="$function/function[@id='FTN_AMD_YEAR_PALN']">
					<xsl:variable name="js">
						<xsl:choose>
							<xsl:when test="//meta/tc_enabled='true'">javascript:wb_utils_nav_go('FTN_AMD_YEAR_PALN')</xsl:when>
							<xsl:otherwise>javascript:wb_utils_nav_go('FTN_AMD_YEAR_PALN')</xsl:otherwise>
						</xsl:choose>
					</xsl:variable>
					<li>
						<a href="{$js}">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">FTN_AMD_YEAR_PALN</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
				</xsl:if>
				<xsl:if test="$function/function[@id='FTN_AMD_MAKEUP_PLAN']">
					<xsl:variable name="js">
						<xsl:choose>
							<xsl:when test="//meta/tc_enabled='true'">javascript:wb_utils_nav_go('FTN_AMD_MAKEUP_PLAN')</xsl:when>
							<xsl:otherwise>javascript:wb_utils_nav_go('FTN_AMD_MAKEUP_PLAN')</xsl:otherwise>
						</xsl:choose>
					</xsl:variable>
					<li>
						<a href="{$js}">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">FTN_AMD_MAKEUP_PLAN</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
				</xsl:if>
				
				<xsl:if test="$function/function[@id='FTN_AMD_YEAR_PLAN_APPR'] or $function/function[@id='FTN_AMD_MAKEUP_PLAN_APPR'] 
						or $function/function[@id='FTN_AMD_YEAR_SETTING']">
					<!-- <span class="HomeLinkGroup"><xsl:value-of select="$lab_636"/></span> -->
				</xsl:if>
				
				<xsl:if test="$function/function[@id='FTN_AMD_YEAR_PLAN_APPR']">
					<xsl:variable name="js">
						<xsl:choose>
							<xsl:when test="//meta/tc_enabled='true'">javascript:wb_utils_nav_go('FTN_AMD_YEAR_PLAN_APPR')</xsl:when>
							<xsl:otherwise>javascript:wb_utils_nav_go('FTN_AMD_YEAR_PLAN_APPR')</xsl:otherwise>
						</xsl:choose>
					</xsl:variable>
					<li>
						<a href="{$js}">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">FTN_AMD_YEAR_PLAN_APPR</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
				</xsl:if>
				<xsl:if test="$function/function[@id='FTN_AMD_MAKEUP_PLAN_APPR']">
					<xsl:variable name="js">
						<xsl:choose>
							<xsl:when test="//meta/tc_enabled='true'">javascript:wb_utils_nav_go('FTN_AMD_MAKEUP_PLAN_APPR')</xsl:when>
							<xsl:otherwise>javascript:wb_utils_nav_go('FTN_AMD_MAKEUP_PLAN_APPR')</xsl:otherwise>
						</xsl:choose>
					</xsl:variable>
					<li>
						<a href="{$js}">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">FTN_AMD_MAKEUP_PLAN_APPR</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
				</xsl:if>
				<xsl:if test="$function/function[@id='FTN_AMD_YEAR_SETTING']">
					<xsl:variable name="js">
						<xsl:choose>
							<xsl:when test="//meta/tc_enabled='true'">javascript:wb_utils_nav_go('FTN_AMD_YEAR_SETTING')</xsl:when>
							<xsl:otherwise>javascript:wb_utils_nav_go('FTN_AMD_YEAR_SETTING')</xsl:otherwise>
						</xsl:choose>
					</xsl:variable>
					<li>
						<a href="{$js}">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">FTN_AMD_YEAR_SETTING</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
				</xsl:if>
			</xsl:when>
			<!--=================================== 培训管理 ===================================-->
			<xsl:when test="$menu_type = $menu_type_training_mgt">
				<xsl:if test="$function/function[@id='ITM_INTEGRATED_MAIN']">
					<li>
						<a href="javascript:wb_utils_nav_go('ITM_INTEGRATED_MAIN',{//cur_usr/@ent_id})">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">ITM_INTEGRATED_MAIN</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
				</xsl:if>
				<xsl:if test="$function/function[@id='FTN_AMD_ITM_COS_MAIN']">
					<li>
						<a href="javascript:wb_utils_nav_go('FTN_AMD_ITM_COS_MAIN',{//cur_usr/@ent_id})">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">FTN_AMD_ITM_COS_MAIN</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
				</xsl:if>
				<xsl:if test="$function/function[@id='ITM_EXAM_MAIN']">
					<li>
						<a href="javascript:wb_utils_nav_go('ITM_EXAM_MAIN',{//cur_usr/@ent_id})">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">ITM_EXAM_MAIN</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
				</xsl:if>
				<xsl:if test="$function/function[@id='ITM_REF_MAIN']">
					<li>
						<a href="javascript:wb_utils_nav_go('ITM_REF_MAIN',{//cur_usr/@ent_id})">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">ITM_REF_MAIN</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
				</xsl:if>
				<xsl:if test="$function/function[@id='FTN_AMD_RES_MAIN']">
					<li>
						<a href="javascript:wb_utils_nav_go('FTN_AMD_RES_MAIN',{//cur_usr/@ent_id})">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">FTN_AMD_RES_MAIN</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
				</xsl:if>
				<xsl:if test="$function/function[@id='FTN_AMD_COS_EVN_MAIN']">
					<li>
						<a href="javascript:wb_utils_nav_go('FTN_AMD_COS_EVN_MAIN',{//cur_usr/@ent_id},'{$wb_lang}')">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">FTN_AMD_COS_EVN_MAIN</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
				</xsl:if>
				<xsl:if test="$function/function[@id='SSO_LINK_QUERY']">
					<li>
						<a href="javascript:wb_utils_nav_go('SSO_LINK_QUERY',{//cur_usr/@ent_id},'{$wb_lang}')">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">SSO_LINK_QUERY</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
				</xsl:if>
				<xsl:if test="$function/function[@id='APPR_APP_LIST']">
					<li>
						<a href="javascript:wb_utils_nav_go('APPR_APP_LIST',{//cur_usr/@ent_id},'{$wb_lang}')">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">APPR_APP_LIST</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
				</xsl:if>
				<xsl:if test="$function/function[@id='QQ_CONSULTATION']">
					<li>
						<a href="javascript:wb_utils_nav_go('QQ_CONSULTATION',{//cur_usr/@ent_id},'{$wb_lang}')">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">QQ_CONSULTATION</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
				</xsl:if>
				<xsl:if test="$function/function[@id='FTN_AMD_EXAM_LIST']">
					<li>
						<a href="javascript:wb_utils_nav_go('FTN_AMD_EXAM_LIST',{//cur_usr/@ent_id},'{$wb_lang}')">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">FTN_AMD_EXAM_LIST</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
				</xsl:if>
				<xsl:if test="$function/function[@id='BOOK_MAIN']">
					<li>
						<a href="javascript:wb_utils_nav_go('BOOK_MAIN',{//cur_usr/@ent_id},'{$wb_lang}')">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">BOOK_MAIN</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
				</xsl:if>
				<xsl:if test="$function/function[@id='BOOK_QUERY']">
					<li>
						<a href="javascript:wb_utils_nav_go('BOOK_QUERY',{//cur_usr/@ent_id},'{$wb_lang}')">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">BOOK_QUERY</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
				</xsl:if>
				<!-- supplier -->
				<xsl:if test="$function/function[@id='FTN_AMD_SUPPLIER_MAIN']">
					<li>
						<a href="javascript:wb_utils_nav_go('FTN_AMD_SUPPLIER_MAIN',{//cur_usr/@ent_id},'{$wb_lang}')">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">FTN_AMD_SUPPLIER_MAIN</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
				</xsl:if>
				<!-- E-certificate Management -->
				<xsl:if test="$function/function[@id='FTN_AMD_CERTIFICATE_MAIN']">
					<li>
						<a href="javascript:wb_utils_nav_go('FTN_AMD_CERTIFICATE_MAIN',{//cur_usr/@ent_id})">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">FTN_AMD_CERTIFICATE_MAIN</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
				</xsl:if>
				<!-- Instructor Management -->
				<xsl:if test="$function/function[@id='FTN_AMD_INT_INSTRUCTOR_MAIN']">
					<li>
						<a href="javascript:wb_utils_nav_go('FTN_AMD_INT_INSTRUCTOR_MAIN')">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">FTN_AMD_INT_INSTRUCTOR_MAIN</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
				</xsl:if>
				<xsl:if test="$function/function[@id='FTN_AMD_EXT_INSTRUCTOR_MAIN']">
					<li>
						<a href="javascript:wb_utils_nav_go('FTN_AMD_EXT_INSTRUCTOR_MAIN')">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">FTN_AMD_EXT_INSTRUCTOR_MAIN</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
				</xsl:if>
			</xsl:when>
			<!--=================================== 评价培训结果 ===================================-->
			<xsl:when test="$menu_type = $menu_type_training_result">
				<xsl:if test="$function/function[@id='RPT_LINK']">
					<li>
						<a href="javascript:wb_utils_nav_go('RPT_LINK',{//cur_usr/@ent_id})">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">RPT_LINK</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
					<xsl:for-each select="$report/rte">
						<li>
							<a href="javascript:wb_utils_rpt_go('{@id}', '{@type}', '{@xsl}')">
								<xsl:call-template name="get_rte_title">
									<xsl:with-param name="rte_type" select="@type"/>
									<xsl:with-param name="home">yes</xsl:with-param>
								</xsl:call-template>
							</a>
						</li>
					</xsl:for-each>
				</xsl:if>
			</xsl:when>
			<!--=================================== 基础设定 ===================================-->
			<xsl:when test="$menu_type = $menu_type_basis_set">
				<xsl:if test="//tc_enabled = 'true' and $function/function[@id='FTN_AMD_TC_MAIN']">
					<li>
						<a href="javascript:wb_utils_nav_go('FTN_AMD_TC_MAIN',{//cur_usr/@ent_id},'{$wb_lang}')">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">FTN_AMD_TC_MAIN</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
				</xsl:if>
				<xsl:if test="$function/function[@id='FTN_AMD_CAT_MAIN']">
					<li>
						<a href="javascript:wb_utils_nav_go('FTN_AMD_CAT_MAIN',{//cur_usr/@ent_id})">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">FTN_AMD_CAT_MAIN</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
				</xsl:if>
				<xsl:if test="$function/function[@id='FTN_AMD_USR_INFO']">
					<li>
						<a href="javascript:wb_utils_nav_go('FTN_AMD_USR_INFO',{//cur_usr/@ent_id},'{$wb_lang}')">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">FTN_AMD_USR_INFO</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
				</xsl:if>
				<xsl:if test="$function/function[@id='CREDIT_OTHER_MAIN']">
					<li>
						<a href="javascript:wb_utils_nav_go('CREDIT_OTHER_MAIN',{//cur_usr/@ent_id},'{$wb_lang}')">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">CREDIT_OTHER_MAIN</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
				</xsl:if>
			    <xsl:if test="$function/function[@id='DEFINED_PROJECT']">
					<li>
						<a href="javascript:wb_utils_nav_go('DEFINED_PROJECT',{//cur_usr/@ent_id},'{$wb_lang}')">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">DEFINED_PROJECT</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
				</xsl:if>
				<xsl:if test ="$function/function[@id='FTN_AMD_POSTER_MAIN'] and //meta/tc_independent = 'true'">
					<xsl:variable name="js">
						<xsl:choose>
							<xsl:when test="//meta/tc_enabled='true'">javascript:wb_utils_nav_go('FTN_AMD_POSTER_MAIN')</xsl:when>
							<xsl:otherwise>javascript:wb_utils_nav_go('FTN_AMD_POSTER_MAIN')</xsl:otherwise>
						</xsl:choose>
					</xsl:variable>
					<li>
						<a href="{$js}">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">FTN_AMD_POSTER_MAIN</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
					<xsl:variable name="js_">
						<xsl:choose>
							<xsl:when test="//meta/tc_enabled='true'">javascript:wb_utils_nav_go('FTN_AMD_MOBILE_POSTER_MAIN')</xsl:when>
							<xsl:otherwise>javascript:wb_utils_nav_go('FTN_AMD_MOBILE_POSTER_MAIN')</xsl:otherwise>
						</xsl:choose>
					</xsl:variable>
					<li>
						<a href="{$js_}">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">FTN_AMD_MOBILE_POSTER_MAIN</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
				</xsl:if>
			</xsl:when>
			<!--=================================== 社区管理 ===================================-->
			<xsl:when test="$menu_type = $menu_type_community_mgt">
				<xsl:if test="$function/function[@id='KNOW_MAIN']">
					<li>
						<a href="javascript:wb_utils_nav_go('KNOW_MAIN',{//cur_usr/@ent_id},'{$wb_lang}')">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">KNOW_MAIN</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
				</xsl:if>
<!-- 				<xsl:if test="$function/function[@id='STUDY_GROUP_MAIN']">
					<li>
						<a href="javascript:wb_utils_nav_go('STUDY_GROUP_MAIN',{//cur_usr/@ent_id},'{$wb_lang}')">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">STUDY_GROUP_MAIN</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
				</xsl:if> -->
				<xsl:if test="$function/function[@id='FOR_MAIN']">
					<li>
						<a href="javascript:wb_utils_nav_go('FOR_MAIN',{//cur_usr/@ent_id},'{$wb_lang}')">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">FOR_MAIN</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
				</xsl:if>
				<xsl:if test="$function/function[@id='MSG_MGT_SYS']">
					<li>
						<a href="javascript:wb_utils_nav_go('MSG_MGT_SYS',{//cur_usr/@ent_id},'{$wb_lang}')">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">MSG_MGT_SYS</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
					<li>
						<a href="javascript:wb_utils_nav_go('MOBILE_MSG_MGT_SYS',{//cur_usr/@ent_id},'{$wb_lang}')">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">MOBILE_MSG_MGT_SYS</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
				</xsl:if>
				<xsl:if test="$function/function[@id='FTN_AMD_MSG_MAIN']">
					<xsl:variable name="js">
						<xsl:choose>
							<xsl:when test="//meta/tc_enabled='true'">javascript:wb_utils_nav_go('FTN_AMD_MSG_MAIN')</xsl:when>
							<xsl:otherwise>javascript:wb_utils_nav_go('FTN_AMD_MSG_MAIN')</xsl:otherwise>
						</xsl:choose>
					</xsl:variable>
					<li>
						<a href="{$js}">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">FTN_AMD_MSG_MAIN</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>

				</xsl:if>
				<xsl:if test="$function/function[@id='FTN_AMD_ARTICLE_MAIN']">
					<li>
						<a href="javascript:wb_utils_nav_go('FTN_AMD_ARTICLE_MAIN',{//cur_usr/@ent_id},'{$wb_lang}')">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">FTN_AMD_ARTICLE_MAIN</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
				</xsl:if>
				<xsl:if test="$function/function[@id='KB_MGT']">
					<li>
						<a href="javascript:wb_utils_nav_go('KB_MGT',{//cur_usr/@ent_id},'{$wb_lang}')">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">KB_MGT</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
				</xsl:if>
				<xsl:if test="$function/function[@id='FTN_AMD_MESSAGE_TEMPLATE_MAIN']">
					<li>
						<a href="javascript:wb_utils_nav_go('FTN_AMD_MESSAGE_TEMPLATE_MAIN',{//cur_usr/@ent_id},'{$wb_lang}')">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">FTN_AMD_MESSAGE_TEMPLATE_MAIN</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
				</xsl:if>
				
					<xsl:if test="$function/function[@id='SMS_GROUP_MANAGE']">
					<li>
						<a href="javascript:wb_utils_nav_go('SMS_GROUP_MANAGE',{//cur_usr/@ent_id},'{$wb_lang}')">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">SMS_GROUP_MANAGE</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
				</xsl:if>
			</xsl:when>
			<!--=================================== 个人设定 ===================================-->
			<xsl:when test="$menu_type = $menu_type_self_set">
				<xsl:if test="$function/function[@id='USR_OWN_PREFER']">
					<li>
						<a href="javascript:wb_utils_nav_go('USR_OWN_PREFER',{//cur_usr/@ent_id},'{$wb_lang}')">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">USR_OWN_PREFER</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
				</xsl:if>
				<xsl:if test="$function/function[@id='USR_OWN_MAIN']">
					<li>
						<a href="javascript:wb_utils_nav_go('USR_OWN_MAIN',{//cur_usr/@ent_id})">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">USR_OWN_MAIN</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
				</xsl:if>
				<xsl:if test="$function/function[@id='USR_PWD_UPD']">
					<li>
						<a href="javascript:wb_utils_nav_go('USR_PWD_UPD',{//cur_usr/@ent_id})">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">USR_PWD_UPD</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
				</xsl:if>
			</xsl:when>
			
		<!--==================================== 系统管理员 =======================================-->
			<!--=================================== 培训管理 ===================================-->
			<xsl:when test="$menu_type = $menu_type_adm_training_mgt">
			    <xsl:if test="$function/function[@id='FTN_AMD_EIP_MAIN']">
					<li>
						<a href="javascript:wb_utils_nav_go('FTN_AMD_EIP_MAIN',{//cur_usr/@ent_id},'{$wb_lang}')">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">FTN_AMD_EIP_MAIN</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
				</xsl:if>
				<xsl:if test="$function/function[@id='LRN_RES_ADMIN']">
					<li>
						<a href="javascript:wb_utils_nav_go('LRN_RES_ADMIN',{//cur_usr/@ent_id},'{$wb_lang}')">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">LRN_RES_ADMIN</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
				</xsl:if>
				<xsl:if test="$function/function[@id='FTN_AMD_USR_INFO']">
					<li>
						<a href="javascript:wb_utils_nav_go('FTN_AMD_USR_INFO',{//cur_usr/@ent_id},'{$wb_lang}')">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">FTN_AMD_USR_INFO</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
				</xsl:if>
				<xsl:if test="//tc_enabled = 'true' and $function/function[@id='FTN_AMD_TC_MAIN']">
					<li>
						<a href="javascript:wb_utils_nav_go('FTN_AMD_TC_MAIN',{//cur_usr/@ent_id},'{$wb_lang}')">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">FTN_AMD_TC_MAIN</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
				</xsl:if>
				<xsl:if test="//tc_enabled = 'true' and $function/function[@id='BOOK_MAIN']">
					<li>
						<a href="javascript:wb_utils_nav_go('BOOK_MAIN',{//cur_usr/@ent_id},'{$wb_lang}')">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">BOOK_MAIN</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
				</xsl:if>
				<xsl:if test="$function/function[@id='APPR_APP_LIST']">
					<li>
						<a href="javascript:wb_utils_nav_go('APPR_APP_LIST',{//cur_usr/@ent_id},'{$wb_lang}')">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">APPR_APP_LIST</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
				</xsl:if>
				<xsl:if test="$function/function[@id='FTN_AMD_COURSE_ASSIGN']">
					<li>
						<a href="javascript:wb_utils_nav_go('FTN_AMD_COURSE_ASSIGN',{//cur_usr/@ent_id},'{$wb_lang}')">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">FTN_AMD_COURSE_ASSIGN</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
				</xsl:if>
				<xsl:if test="$function/function[@id='FTN_AMD_PROFESSION_MAIN']">
					<li>
						<a href="javascript:wb_utils_nav_go('FTN_AMD_PROFESSION_MAIN',{//cur_usr/@ent_id},'{$wb_lang}')">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">FTN_AMD_PROFESSION_MAIN</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
				</xsl:if>
				<xsl:if test="$function/function[@id='QQ_CONSULTATION']">
					<li>
						<a href="javascript:wb_utils_nav_go('QQ_CONSULTATION',{//cur_usr/@ent_id},'{$wb_lang}')">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">QQ_CONSULTATION</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
				</xsl:if>
			</xsl:when>
			<!--=================================== 系统管理 ===================================-->
			<xsl:when test="$menu_type = $menu_type_sys_mgt">
				<xsl:if test="$function/function[@id='ACL_MAIN']">
					<li>
						<a href="javascript:wb_utils_nav_go('ACL_MAIN',{//cur_usr/@ent_id},'{$wb_lang}')">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">ACL_MAIN</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
				</xsl:if>
				<xsl:if test="$function/function[@id='FTN_AMD_SYS_SETTING_MAIN']">
					<li>
						<a href="javascript:wb_utils_nav_go('FTN_AMD_SYS_SETTING_MAIN',{//cur_usr/@ent_id},'{$wb_lang}')">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">FTN_AMD_SYS_SETTING_MAIN</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
				</xsl:if>
				<xsl:if test="$function/function[@id='FTN_AMD_CREDIT_SETTING_MAIN']">
					<li>
						<a href="javascript:wb_utils_nav_go('FTN_AMD_CREDIT_SETTING_MAIN',{//cur_usr/@ent_id},'{$wb_lang}')">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">FTN_AMD_CREDIT_SETTING_MAIN</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
				</xsl:if>
				<xsl:if test="$function/function[@id='FTN_AMD_SYS_ROLE_MAIN']">
					<li>
						<a href="javascript:wb_utils_nav_go('FTN_AMD_SYS_ROLE_MAIN',{//cur_usr/@ent_id},'{$wb_lang}')">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">FTN_AMD_SYS_ROLE_MAIN</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
				</xsl:if>	
			</xsl:when>
			<!--=================================== 社区管理 ===================================-->
			<!--=================================== 公告管理 ===================================-->
			<xsl:when test="$menu_type = $menu_type_adm_community_mgt">
				<xsl:if test="$function/function[@id='MSG_MGT_SYS']">
					<xsl:variable name="js">
						<xsl:choose>
							<xsl:when test="//meta/tc_enabled='true'">javascript:wb_utils_nav_go('MSG_MGT_SYS')</xsl:when>
							<xsl:otherwise>javascript:wb_utils_nav_go('MSG_MGT_SYS')</xsl:otherwise>
						</xsl:choose>
					</xsl:variable>
					<li>
						<a href="{$js}">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">MSG_MGT_SYS</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
					
				</xsl:if>
				<!--=================================== 友情链接 ===================================-->
				<xsl:if test="$function/function[@id='FS_LINK_MAIN']">
					<xsl:variable name="js">
						<xsl:choose>
							<xsl:when test="//meta/tc_enabled='true'">javascript:wb_utils_nav_go('FS_LINK_MAIN')</xsl:when>
							<xsl:otherwise>javascript:wb_utils_nav_go('FS_LINK_MAIN')</xsl:otherwise>
						</xsl:choose>
					</xsl:variable>
					<li>
						<a href="{$js}">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">FS_LINK_MAIN</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
				</xsl:if>
				<!--=================================== 宣传栏 ===================================-->
				<xsl:if test="$function/function[@id='FTN_AMD_POSTER_MAIN']">
					<xsl:variable name="js">
						<xsl:choose>
							<xsl:when test="//meta/tc_enabled='true'">javascript:wb_utils_nav_go('FTN_AMD_POSTER_MAIN')</xsl:when>
							<xsl:otherwise>javascript:wb_utils_nav_go('FTN_AMD_POSTER_MAIN')</xsl:otherwise>
						</xsl:choose>
					</xsl:variable>
					<li>
						<a href="{$js}">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">FTN_AMD_POSTER_MAIN</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
				</xsl:if>
				<!--================================= 移动宣传栏 ==================================-->
				<xsl:if test="$function/function[@id='FTN_AMD_POSTER_MAIN']">
					<xsl:variable name="js">
						<xsl:choose>
							<xsl:when test="//meta/tc_enabled='true'">javascript:wb_utils_nav_go('FTN_AMD_MOBILE_POSTER_MAIN')</xsl:when>
							<xsl:otherwise>javascript:wb_utils_nav_go('FTN_AMD_MOBILE_POSTER_MAIN')</xsl:otherwise>
						</xsl:choose>
					</xsl:variable>
					<li>
						<a href="{$js}">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">FTN_AMD_MOBILE_POSTER_MAIN</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
				</xsl:if>
				<!--================================= 在线问答管理 ==================================-->
				<xsl:if test="$function/function[@id='KNOW_MAIN'] and //meta/tc_independent = 'true'">
					<li>
						<a href="javascript:wb_utils_nav_go('KNOW_MAIN',{//cur_usr/@ent_id},'{$wb_lang}')">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">KNOW_MAIN</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
				</xsl:if>
				<!--================================= 文章管理 ==================================-->
				<xsl:if test="$function/function[@id='FTN_AMD_ARTICLE_MAIN']">
					<li>
						<a href="javascript:wb_utils_nav_go('FTN_AMD_ARTICLE_MAIN',{//cur_usr/@ent_id},'{$wb_lang}')">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">FTN_AMD_ARTICLE_MAIN</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
				</xsl:if>
				
				<!--================================= 知识管理 ==================================-->
				<xsl:if test="$function/function[@id='KB_MGT']">
					<li>
						<a href="javascript:wb_utils_nav_go('KB_MGT',{//cur_usr/@ent_id},'{$wb_lang}')">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">KB_MGT</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
				</xsl:if>
				
					<!--=================================群组管理 ==================================-->
				<xsl:if test="$function/function[@id='SMS_GROUP_MANAGE']">
					<li>
						<a href="javascript:wb_utils_nav_go('SMS_GROUP_MANAGE',{//cur_usr/@ent_id},'{$wb_lang}')">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">SMS_GROUP_MANAGE</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
				</xsl:if>
				<!--================================= 邮件模板管理 ==================================-->
				<xsl:if test="$function/function[@id='FTN_AMD_MESSAGE_TEMPLATE_MAIN']">
					<li>
						<a href="javascript:wb_utils_nav_go('FTN_AMD_MESSAGE_TEMPLATE_MAIN',{//cur_usr/@ent_id},'{$wb_lang}')">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">FTN_AMD_MESSAGE_TEMPLATE_MAIN</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
				</xsl:if>
			</xsl:when>
			
		<!--==================================== 讲师 =======================================-->
			<!--=================================== 教学管理 ===================================-->
			<xsl:when test="$menu_type = $menu_type_teach_mgt">
				<xsl:if test="$function/function[@id='FTN_AMD_RES_MAIN']">
					<li>
						<a href="javascript:wb_utils_nav_go('FTN_AMD_RES_MAIN',{//cur_usr/@ent_id})">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">FTN_AMD_RES_MAIN</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
				</xsl:if>
				<xsl:if test="$function/function[@id='FTN_AMD_TEACHING_COURSE_LIST']">
					<li>
						<a href="javascript:wb_utils_nav_go('FTN_AMD_TEACHING_COURSE_LIST',{//cur_usr/@ent_id})">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">FTN_AMD_TEACHING_COURSE_LIST</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
				</xsl:if>
				<xsl:if test="$function/function[@id='APPR_APP_LIST']">
					<li>
						<a href="javascript:wb_utils_nav_go('APPR_APP_LIST',{//cur_usr/@ent_id},'{$wb_lang}')">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">APPR_APP_LIST</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
				</xsl:if>
				<xsl:if test="$function/function[@id='QQ_CONSULTATION']">
					<li>
						<a href="javascript:wb_utils_nav_go('QQ_CONSULTATION',{//cur_usr/@ent_id},'{$wb_lang}')">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">QQ_CONSULTATION</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
				</xsl:if>
			</xsl:when>
			<!--=================================== 社区 ===================================-->
			<xsl:when test="$menu_type = $menu_type_community">
				<xsl:if test="$function/function[@id='FOR_LINK']">
					<li>
						<a href="javascript:wb_utils_nav_go('FOR_LINK',{//cur_usr/@ent_id},'{$wb_lang}')">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">FOR_LINK</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
				</xsl:if>
				<xsl:if test="$function/function[@id='FTN_AMD_SYS_MSG_LIST']">
					<li>
						<a href="javascript:wb_utils_nav_go('FTN_AMD_SYS_MSG_LIST',{//cur_usr/@ent_id})">
							<xsl:call-template name="get_ftn_title">
								<xsl:with-param name="ftn_ext_id">FTN_AMD_SYS_MSG_LIST</xsl:with-param>
							</xsl:call-template>
						</a>
					</li>
				</xsl:if>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>
