<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="share/wb_module_type_const.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:output indent="yes"/>
	<!--=======================================================================-->
	<xsl:variable name="wb_gen_table_width">100%</xsl:variable>
	<xsl:variable name="ts_enable" select="/user/ts_enable/text()"/>
	<!--=======================================================================-->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates/>
		</html>
	</xsl:template>
	<!--=======================================================================-->
	<xsl:template match="user">
		<head>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_module.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_course.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_cos_wizard.js"/>
			<script languagE="JavaScript" type="text/javascript"><![CDATA[
	
			var module_lst = new wbModule;
			var course_lst = new wbCourse;		
			var wiz = new wbCosWizard

			var course_id = getUrlParam("course_id");
			var cos_name = wb_utils_get_cookie("title_prev");
			wb_utils_set_cookie('url_prev', parent.location.href)
			function ins_mod_svy_prep(course_id){
				var url = wb_utils_invoke_servlet("cmd","get_public_survey_lst","stylesheet","ins_mod_svy_prep.xsl","course_id",course_id);
				self.location.href = url;
			}
	
		]]></script>
			<xsl:call-template name="new_css"/>
		</head>
		<xsl:call-template name="wb_init_lab"/>
	</xsl:template>
	<!--=======================================================================-->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_add_module">新增模塊</xsl:with-param>
			<xsl:with-param name="lab_course_list">課程目錄</xsl:with-param>
			<xsl:with-param name="lab_select">請選擇模塊種類</xsl:with-param>
			<xsl:with-param name="lab_add_res">新增資源</xsl:with-param>
			<xsl:with-param name="lab_ass_tools">測評</xsl:with-param>
			<xsl:with-param name="lab_events_tools">活動</xsl:with-param>
			<xsl:with-param name="lab_coll_tools">交流</xsl:with-param>
			<xsl:with-param name="lab_class_tools">內容</xsl:with-param>
			<xsl:with-param name="lab_media_tools">媒體</xsl:with-param>
			<xsl:with-param name="lab_otr_tools">其他</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_add_module">添加模块</xsl:with-param>
			<xsl:with-param name="lab_course_list">课程目录</xsl:with-param>
			<xsl:with-param name="lab_select">请选择模块种类</xsl:with-param>
			<xsl:with-param name="lab_add_res">添加资源</xsl:with-param>
			<xsl:with-param name="lab_ass_tools">测评</xsl:with-param>
			<xsl:with-param name="lab_events_tools">活动</xsl:with-param>
			<xsl:with-param name="lab_coll_tools">交流</xsl:with-param>
			<xsl:with-param name="lab_class_tools">內容</xsl:with-param>
			<xsl:with-param name="lab_media_tools">媒体</xsl:with-param>
			<xsl:with-param name="lab_otr_tools">其他</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_add_module">Add module</xsl:with-param>
			<xsl:with-param name="lab_course_list">Course list</xsl:with-param>
			<xsl:with-param name="lab_select">Please select a module type</xsl:with-param>
			<xsl:with-param name="lab_add_res">Add resource</xsl:with-param>
			<xsl:with-param name="lab_ass_tools">Assessment</xsl:with-param>
			<xsl:with-param name="lab_events_tools">Activity</xsl:with-param>
			<xsl:with-param name="lab_coll_tools">Collaboration</xsl:with-param>
			<xsl:with-param name="lab_class_tools">Content</xsl:with-param>
			<xsl:with-param name="lab_media_tools">Media</xsl:with-param>
			<xsl:with-param name="lab_otr_tools">Others</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!--===============================================================-->
	<xsl:template name="content">
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:param name="lab_add_module"/>
		<xsl:param name="lab_course_list"/>
		<xsl:param name="lab_select"/>
		<xsl:param name="lab_add_res"/>
		<xsl:param name="lab_ass_tools"/>
		<xsl:param name="lab_events_tools"/>
		<xsl:param name="lab_coll_tools"/>
		<xsl:param name="lab_class_tools"/>
		<xsl:param name="lab_media_tools"/>
		<xsl:param name="lab_otr_tools"/>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
			<form>
				<xsl:call-template name="wb_ui_head" > 
					<xsl:with-param name="text" select="$lab_select" />
					<xsl:with-param name="title_style">font-weight:900;font-size:16px;</xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="wb_ui_line"/>
				<xsl:choose>
					<!-- tst type -->
					<xsl:when test="/user[itm_exam_ind = 'true']">
						<table>
							<tr>
								<td width="33%" colspan="2">
									<xsl:value-of select="$lab_ass_tools"/>
								</td>
								<td width="33%" colspan="2"></td>
								<td width="33%" colspan="2"></td>
							</tr>						
							<tr>
								<td valign="middle" width="10%" align="right">
									<img src="{$wb_img_path}icol_test.gif" width="24" height="24" border="0" align="absmiddle"/>
								</td>
								<td width="23%">
									<a href="javascript:course_lst.ins_mod_test_prep(course_id,'TEST','TEST')" class="Text">
										<xsl:value-of select="$lab_test"/>
									</a>						
								</td>
								<td valign="middle" align="right"></td>
								<td></td>						
								<td valign="middle" width="10%" align="right">
								</td>
								<td  width="23%">	</td>
							</tr>
						</table>
					</xsl:when>
					
					<xsl:otherwise>
						<!-- all type -->
						<table cellpadding="2" cellspacing="5" border="0" width="{$wb_gen_table_width}" class="Bg" margin="50px">
							<tr >
								<td width="33%" colspan="2" class="wzb-form-control" >
									<span class="TitleText">
										<xsl:value-of select="$lab_ass_tools"/>
									</span>
								</td>
								<td width="33%" colspan="2" class="wzb-form-control" >
									<span class="TitleText">
										<xsl:value-of select="$lab_class_tools"/>
									</span>
								</td>
								<!-- 
								<td width="33%" colspan="2">
									<span class="TitleText">
										<xsl:value-of select="$lab_coll_tools"/>
									</span>
								</td>
								 -->
							</tr>
							<!-- 1 row -->
							<tr >
								<td valign="middle" width="5%" align="left"   class="wzb-form-control" >
									<img src="{$wb_img_path}icol_test.gif" width="24" height="24" border="0" align="absmiddle"/>
								</td>
								<td width="23%" class="wzb-form-control" >
									<a href="javascript:course_lst.ins_mod_test_prep(course_id,'TEST','TEST')" class="Text">
										<xsl:value-of select="$lab_test"/>
									</a>						
								</td>
								<td valign="middle" align="left" class="wzb-form-control" >
									<img src="{$wb_img_path}icol_rdg.gif" width="24" height="24" border="0" align="absmiddle"/>
								</td>
								<td class="wzb-form-control" >
									<a href="javascript:course_lst.ins_mod_prep(course_id,'RDG','RDG')" class="Text">
										<xsl:value-of select="$lab_rdg"/>
									</a>
								</td>	
								<!-- 					
								<td valign="middle" width="10%" align="right">
									<img src="{$wb_img_path}icol_for.gif" width="24" height="24" border="0" align="absmiddle"/>
								</td>
								
								<td  width="23%">
									<a href="javascript:course_lst.ins_mod_prep(course_id,'FOR','FOR')" class="Text">
										<xsl:value-of select="$lab_for"/>
									</a>
								</td>
								 -->
							</tr>
							<!-- 2 row -->
							<tr>
								<td valign="middle" align="left" class="wzb-form-control" >
									<img src="{$wb_img_path}icol_ass.gif" width="24" height="24" border="0" align="absmiddle"/>
								</td>
								<td class="wzb-form-control" >
									<a href="javascript:course_lst.ins_mod_prep(course_id ,'ASS','ASS')" class="Text">
										<xsl:value-of select="$lab_ass"/>
									</a>
								</td>
								<td valign="middle" width="5%" align="left" class="wzb-form-control" >
									<img src="{$wb_img_path}icol_aicc_au.gif" width="24" height="24" border="0" align="absmiddle"/>
								</td>
								<td  width="23%" class="wzb-form-control" >
									<a href="javascript:course_lst.ins_mod_prep(course_id,'AICC_AU','AICC_AU')" class="Text">
										<xsl:value-of select="$lab_aicc_au"/>
									</a>
								</td>	
									<!-- 					
								<td valign="middle" align="right" class="wzb-form-control" >
									<img src="{$wb_img_path}icol_faq.gif" width="24" height="24" border="0" align="absmiddle"/>
								</td>
							
								<td class="wzb-form-control" >
									<a href="javascript:course_lst.ins_mod_prep(course_id,'FAQ','FAQ')" class="Text">
										<xsl:value-of select="$lab_faq"/>
									</a>
								</td>
								 -->
							</tr>
							<!-- 3 row -->
							<tr>
								<td valign="middle"  align="left" class="wzb-form-control" >
									<img src="{$wb_img_path}icol_svy.gif" width="24" height="24" border="0" align="absmiddle"/>
								</td>
								<td class="wzb-form-control" >
									<a href="javascript:ins_mod_svy_prep(course_id)" class="Text">
										<xsl:value-of select="$lab_svy"/>
									</a>
								</td>
					<!--			<td valign="middle" align="right" class="wzb-form-control" >
									<img src="{$wb_img_path}icol_netg_cok.gif" width="24" height="24" border="0" align="absmiddle"/>
								</td>
								<td class="wzb-form-control" >
									<a href="javascript:course_lst.ins_mod_prep(course_id ,'NETG_COK','NETG_COK')" class="Text">
										<xsl:value-of select="$lab_netg_cok"/>
									</a>
								</td>
								<td valign="middle"  align="right" class="wzb-form-control" >
									<xsl:choose>
										<xsl:when test="$ts_enable = 'true'">
											<img src="{$wb_img_path}icol_cht.gif" width="24" height="24" border="0" align="absmiddle"/>
										</xsl:when>
										<xsl:otherwise>
											<img border="0" height="5" width="1" src="{$wb_img_path}tp.gif"/>
										</xsl:otherwise>
									</xsl:choose>
								</td>
								<td class="wzb-form-control" >
									<xsl:choose>
										<xsl:when test="$ts_enable = 'true'">
											<a href="javascript:course_lst.ins_mod_prep(course_id,'CHT','CHT')" class="Text">
												<xsl:value-of select="$lab_cht"/>
											</a>
										</xsl:when>
										<xsl:otherwise>
											<img border="0" height="5" width="1" src="{$wb_img_path}tp.gif"/>
										</xsl:otherwise>
									</xsl:choose>
								</td>
							</tr> -->
							<!-- amanda -->
						<!--	<tr>
								<td valign="middle"  align="right" colspan="2" class="wzb-form-control" >
									<img border="0" height="5" width="1" src="{$wb_img_path}tp.gif"/>
								</td>-->
								<td valign="middle" align="left" class="wzb-form-control" >
									<img src="{$wb_img_path}sico_sco.gif" width="24" height="24" border="0" align="absmiddle"/>
								</td>
								<td class="wzb-form-control" >
									<!--
									<a href="javascript:course_lst.ins_mod_prep(course_id ,'NETG_COK','NETG_COK')" class="Text">
										<xsl:value-of select="$lab_scorm"/>
									</a>
									-->
									<a href="javascript:wiz.add_scorm('true',course_id)" class="Text">
										<xsl:value-of select="$lab_scorm"/>
									</a>
								</td>
								<td valign="middle"  align="left" class="wzb-form-control" >
									<xsl:choose>
										<xsl:when test="$ts_enable = 'true'">
											<img src="{$wb_img_path}icol_cht.gif" width="24" height="24" border="0" align="absmiddle"/>
										</xsl:when>
										<xsl:otherwise>
											<img border="0" height="5" width="1" src="{$wb_img_path}tp.gif"/>
										</xsl:otherwise>
									</xsl:choose>
								</td>
								<td class="wzb-form-control" >
									<xsl:choose>
										<xsl:when test="$ts_enable = 'true'">
											<a href="javascript:course_lst.ins_mod_prep(course_id,'CHT','CHT')" class="Text">
												<xsl:value-of select="$lab_cht"/>
											</a>
										</xsl:when>
										<xsl:otherwise>
											<img border="0" height="5" width="1" src="{$wb_img_path}tp.gif"/>
										</xsl:otherwise>
									</xsl:choose>
								</td>
							</tr>
							<!-- 4 row -->
							<tr>
								<td valign="middle"  align="left" colspan="2"  class="wzb-form-control" >
									<img border="0" height="5" width="1" src="{$wb_img_path}tp.gif"/>
								</td>
								<td valign="middle" align="left" class="wzb-form-control" >
									<img src="{$wb_img_path}icol_ref.gif" width="24" height="24" border="0" align="absmiddle"/>
								</td>
								<td class="wzb-form-control" >
									<a href="javascript:course_lst.ins_mod_prep(course_id ,'REF','REF')" class="Text">
										<xsl:value-of select="$lab_ref"/>
									</a>
								</td>
								<td colspan="2" valign="middle" class="wzb-form-control" >
									<img border="0" height="5" width="1" src="{$wb_img_path}tp.gif"/>
								</td>
							</tr>
							<!-- 5
							<tr>
								<td valign="middle" colspan="2" class="wzb-form-control" >
									<img border="0" height="5" width="1" src="{$wb_img_path}tp.gif"/>
								</td>
								<td valign="middle" align="right" class="wzb-form-control" >
									<img src="{$wb_img_path}icol_glo.gif" width="24" height="24" border="0" align="absmiddle"/>
								</td>
								<td class="wzb-form-control" >
									<a href="javascript:course_lst.ins_mod_prep(course_id ,'GLO','GLO')" class="Text">
										<xsl:value-of select="$lab_glo"/>
									</a>
								</td>
								<td valign="middle" colspan="2" class="wzb-form-control" >
									<img border="0" height="5" width="1" src="{$wb_img_path}tp.gif"/>
								</td>
							</tr>
							 -->
							<!-- 6 -->
							<tr>
								<td valign="middle" colspan="2" class="wzb-form-control" >
									<img border="0" height="5" width="1" src="{$wb_img_path}tp.gif"/>
								</td>
								<td valign="middle" align="left" class="wzb-form-control" >
									<img src="{$wb_img_path}icol_vod.gif" width="24" height="24" border="0" align="absmiddle"/>
								</td>
								<td class="wzb-form-control" >
									<a href="javascript:course_lst.ins_mod_prep(course_id ,'VOD','VOD')" class="Text">
										<xsl:value-of select="$lab_vod"/>
									</a>
								</td>
								<td valign="middle" colspan="2" class="wzb-form-control" >
									<img border="0" height="5" width="1" src="{$wb_img_path}tp.gif"/>
								</td>
							</tr>					
							<tr>
								<td colspan="4" class="wzb-form-control" >
									<img border="0" height="5" width="1" src="{$wb_img_path}tp.gif"/>
								</td>
							</tr>
						</table>
						<!--end all type -->
					</xsl:otherwise>
				</xsl:choose>
			<div class="wzb-bar">
				<xsl:call-template name="wb_gen_form_button">
					<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
					<xsl:with-param name="wb_gen_btn_href">javascript:module_lst.cancel_add(course_id)</xsl:with-param>
				</xsl:call-template>
			</div>
			</form>
		</body>
	</xsl:template>
	<!--=======================================================================-->
</xsl:stylesheet>
