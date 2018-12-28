<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	version="1.0" xmlns:java="http://xml.apache.org/xalan/java"
	exclude-result-prefixes="java">
	<xsl:import href="wb_const.xsl" />
	<xsl:import href="cust/wb_cust_const.xsl" />
	<xsl:import href="utils/wb_css.xsl" />
	<xsl:import href="utils/wb_gen_form_button.xsl" />
	<xsl:import href="utils/wb_ui_footer.xsl" />
	<xsl:import href="utils/wb_ui_head.xsl" />
	<xsl:import href="utils/wb_ui_line.xsl" />
	<xsl:import href="utils/wb_ui_hdr.xsl" />
	<xsl:import href="utils/wb_ui_title.xsl" />
	<xsl:import href="utils/wb_utils.xsl" />
	<xsl:import href="utils/wb_gen_button.xsl" />
	<xsl:import href="utils/wb_ui_show_no_item.xsl" />
	<xsl:import href="utils/wb_sortorder_cursor.xsl"/>
	<xsl:import href="utils/wb_ui_pagination.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:import href="suppliercomment_list.xsl"/>
		<xsl:import href="utils/wb_css.xsl"/>
	<!-- =========================================================================== -->
	
	<xsl:variable name="cur_page" select="//pagination/@cur_page"/>
	<xsl:variable name="total" select="//pagination/@total_rec"/>
	<xsl:variable name="page_size" select="//pagination/@page_size"/>
	<xsl:variable name="order_by" select="//pagination/@sort_col"/>
	<xsl:variable name="cur_order" select="//pagination/@sort_order"/>
	
	<xsl:variable name="is_poup" select="//searchCom/is_poup"/>
	<xsl:variable name="iti_type_mark" select="//searchCom/iti_type_mark"/>
	
	<xsl:variable name="table_width">
		<xsl:choose>
			<xsl:when test="$is_poup='true'">803</xsl:when>
			<xsl:otherwise><xsl:value-of select="$wb_gen_table_width"/></xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_page_title">
		<xsl:choose>
			<xsl:when test="$iti_type_mark='EXT'"><xsl:value-of select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1126')"/></xsl:when>
			<xsl:otherwise><xsl:value-of select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1127')"/> </xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_btn_add" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '473')" />
	<xsl:variable name="lab_btn_srh" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '400')" />
	<xsl:variable name="lab_949" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '949')" />
	<xsl:variable name="lab_951" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '951')" />
	<xsl:variable name="lab_955" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '955')" />
	<xsl:variable name="lab_967" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '967')" />
	<xsl:variable name="lab_968" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '968')" />
	<xsl:variable name="lab_969" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '969')" />
	<xsl:variable name="lab_970" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '970')" />
	<xsl:variable name="lab_383" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '383')" />
	<xsl:variable name="lab_940" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '940')" />
	<xsl:variable name="lab_942" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '942')" />
	<xsl:variable name="lab_960" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '960')" />
	<xsl:variable name="lab_1043" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1043')" />
	<xsl:variable name="lab_1044" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1044')" />
	<xsl:variable name="lab_1045" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1045')" />
	<xsl:variable name="lab_1018" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1018')" />
	<xsl:variable name="lab_1067" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1067')" />
	<xsl:variable name="lab_1068" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1068')" />
		<xsl:variable name="lab_10681" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '10681')" />
	<xsl:variable name="lab_1069" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1069')" />
	<xsl:variable name="lab_not_specified" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_not_specified')" />
	<xsl:variable name="lab_all" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'ALL')" />
	<xsl:variable name="lab_g_form_btn_ok" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '329')" />
	<xsl:variable name="lab_g_form_btn_cancel" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '330')" />
	
	<xsl:variable name="iti_property_options" select="/instructor/meta/iti_property_options" />
	<xsl:variable name="iti_type_options" select="/instructor/meta/iti_type_options" />
	<xsl:variable name="iti_level_options" select="/instructor/meta/iti_level_options" />
	<xsl:variable name="iti_gender_options" select="/instructor/meta/iti_gender_options" />
	<!-- =========================================================================== -->
	<xsl:output indent="yes" />
	<xsl:template match="/">
		<html>
			<head>
				<title>
					<xsl:value-of select="$wb_wizbank" />
				</title>
				<meta http-equiv="X-UA-Compatible" content="IE=edge" />
				<meta http-equiv="Content-Type" content="text/html; charset={$encoding}" />
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js" />
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js" />
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_instructor.js" />
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_usergroup.js" />
				<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js" />
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js" />
				<script language="Javascript" type="text/javascript" src="{$wb_js_path}urlparam.js" />
				<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_goldenman.js" />
				
				
				
		 <script language="Javascript" type="text/javascript" src="{$wb_js_path}jquery.js"/> 
            
            <link rel="stylesheet" href="../static/js/jquery.qtip/jquery.qtip.css" />
            <script type="text/javascript" src="../static/js/jquery.dialogue.js"></script>
            <script type="text/javascript" src="../static/js/jquery.qtip/jquery.qtip.js"></script>
            
            <script language="Javascript" type="text/javascript" src="../../static/js/cwn_utils.js"/>
            <script type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/global_{$wb_cur_lang}.js"></script>
				
				
				<!-- 
				<link rel="stylesheet" href="../static/css/base.css"/>
				 -->
								
				<script type="text/javascript"><![CDATA[
					usr = new wbUserGroup;
					instr = new wbInstructor;
					goldenman =  new wbGoldenMan
					
					function getPopupUsrLst(fld_name, id_lst, nm_lst, blank, auto_enroll_ind, usr_id_lst) {
						instr.int_ins_upd_prep(0, id_lst);
					}
					function searchInstr(type){
						var is_poup = getUrlParam('is_poup');
						var search_text =document.getElementById('search_text').value;
						var ils_id =document.getElementById('ils_id').value;
						var cmd = 'inst_list';
						var js_name=getUrlParam('js_name');
						var stylesheet = "instructor_list.xsl";
						url = wb_utils_invoke_disp_servlet("module", "instructor.InstructorModule","js_name",js_name, "cmd", cmd, 'stylesheet', stylesheet,'search_text',search_text,'iti_type_mark', type,'ils_id', ils_id,'is_poup',is_poup);
						window.location.href = url;
					}
					
					var isExcludes = false;
					var is_poup = getUrlParam("is_poup")?getUrlParam("is_poup"):'false';
					if( is_poup == 'true'){
						isExcludes = true;
					}
					]]>
				</script>
				 <xsl:call-template name="new_css"/>
			 
			</head>
			<body leftmargin="0" marginheight="0" marginwidth="0" topmargin="0">
				<form name="frmXml" onsubmit="searchInstr('{//searchCom/iti_type_mark}');return false;">
				
					<input type="hidden" name="module" />
					<input type="hidden" name="cmd" />
					<input type="hidden" name="cmd" />
					<input type="hidden" name="js_name" value="{//searchCom/js_name}" />
					<input type="hidden" name="max_select" value="{//searchCom/max_select}" />
					<input type="hidden" name="is_poup" value="{//searchCom/is_poup}" />
					<input type="hidden" name="for_time_table" value="{//searchCom/for_time_table}" />
					<input type="hidden" id="ils_id" name="ils_id" value="{//searchCom/ils_id}" />
					<input type="hidden" name="stylesheet" />
					<xsl:call-template name="content" />
				</form>
			</body>
		</html>
	</xsl:template>
	<!--
		===========================================================================
	-->
	<xsl:template name="content">
		<xsl:choose>
			<xsl:when test="$is_poup='true'"></xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="wb_ui_hdr">
					<xsl:with-param name="belong_module">FTN_AMD_INT_INSTRUCTOR_MAIN</xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="wb_ui_title">
					<xsl:with-param name="text" select="$lab_page_title" />
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	<!-- 	<xsl:if test="$is_poup='true'">
			<xsl:call-template name="wb_ui_space">
				<xsl:with-param name="height">15</xsl:with-param>
			</xsl:call-template>
		</xsl:if>   -->  <!-- 去掉了头部空白部分   （线下考试-日程表-指定讲师） -->
		<!-- ===================== Search Form ===================== -->
		<xsl:choose>
		    <xsl:when test="//searchCom/for_time_table='DEL'">
		    	<xsl:call-template name="wb_ui_title">
		    	    <xsl:with-param name="text_class" >pop-up-title</xsl:with-param>
		    		<xsl:with-param name="width" select="$table_width"/>
					<xsl:with-param name="text" select="$lab_1067"/>
				</xsl:call-template>
		    </xsl:when>
		    
		    <xsl:when test="//searchCom/for_time_table='ADD'">
		    	<xsl:call-template name="wb_ui_title">
		    	    <xsl:with-param name="text_class" >pop-up-title</xsl:with-param>
		    	    <xsl:with-param name="width" select="$table_width"/>
					<xsl:with-param name="text" select="$lab_1068"/>
				</xsl:call-template>
		    </xsl:when>
		    
		    <xsl:otherwise>
				<table cellpadding="0" cellspacing="0" border="0" width="{$table_width}" class="Bg wizGrid">
					<!--  
					<tr>
						<td width="10%">
						</td>
						<td width="10%" align="right">
							<xsl:value-of select="$lab_967" />:
						</td>
						<td>
							<input type="Text" style="width:180px;" maxlength="255" name="iti_name"
								value="{//searchCom/iti_name}" class="wzb-inputText" />
						</td>
						<td width="10%" align="right">
							<xsl:value-of select="$lab_951" />:
						</td>
						<td>
							<select name="iti_level" class="wzb-form-select">
								<option value="">
									<xsl:value-of select="$lab_all" />								
								</option>
								<xsl:for-each select="$iti_level_options/option">
									<option value="{@value}">
										<xsl:if test="@value=//searchCom/iti_level"><xsl:attribute name="selected">true</xsl:attribute></xsl:if>
										<xsl:value-of select="text()" />
									</option>
								</xsl:for-each>
							</select>
						</td>
						
						<xsl:choose>
							<xsl:when test="$iti_type_mark='EXT'">
								<td width="10%" align="right">
									<xsl:value-of select="$lab_955" />:
								</td>
								<td>
									<input type="Text" style="width:180px;" maxlength="255"
										name="iti_main_course" value="{//searchCom/iti_main_course}" class="wzb-inputText" />
								</td>
							</xsl:when>
							<xsl:otherwise>
								<td width="10%" align="right">
									<xsl:value-of select="$lab_949" />:
								</td>
								<td>
									<input type="Text" style="width:180px;" maxlength="255"
										name="iti_gw_str" value="{//searchCom/iti_gw_str}" class="wzb-inputText" />
								</td>
							</xsl:otherwise>
						</xsl:choose>
						
						<td width="10%">
						</td>
					</tr>
					<tr>
						<td>
						</td>
							
						<xsl:choose>
							<xsl:when test="$iti_type_mark='EXT'">
								<td align="right">
									<xsl:value-of select="$lab_1018" />:
								</td>
								<td>
									<input type="Text" style="width:180px;" maxlength="255"
										name="iti_expertise_areas" value="{//searchCom/iti_expertise_areas}" class="wzb-inputText" />
								</td>
								<td align="right">
									<xsl:value-of select="$lab_968" />:
								</td>
								<td >
									<table>
										<tr>
											<td><input type="hidden" name="lab_iti_score" value="{$lab_968}"/>
												<input type="Text" style="width:30px;" maxlength="3"
													name="iti_score_from" value="{//searchCom/iti_score_from}" class="wzb-inputText" />
											</td>
											<td>
												<xsl:value-of select="$lab_383" />
											</td>
											<td>
												<input type="Text" style="width:30px;" maxlength="3"
													name="iti_score_to" value="{//searchCom/iti_score_to}" class="wzb-inputText" />
											</td>
										</tr>
									</table>
				
								</td>
								<td width="10%" align="right">
									<xsl:value-of select="$lab_1045" />:
								</td>
								<td>
									<input type="Text" style="width:180px;" maxlength="255"
										name="iti_training_company" value="{//searchCom/iti_training_company}" class="wzb-inputText" />
								</td>
							</xsl:when>
							<xsl:otherwise>
								<td align="right">
									<xsl:value-of select="$lab_955" />:
								</td>
								<td>
									<input type="Text" style="width:180px;" maxlength="255"
										name="iti_main_course" value="{//searchCom/iti_main_course}" class="wzb-inputText" />
								</td>
								<td align="right">
									<xsl:value-of select="$lab_968" />:
								</td>
								<td colspan="3">
									<table>
										<tr>
											<td width="10%"><input type="hidden" name="lab_iti_score" value="{$lab_968}"/>
												<input type="Text" style="width:30px;" maxlength="3"
													name="iti_score_from" value="{//searchCom/iti_score_from}" class="wzb-inputText" />
											</td>
											<td width="5%" align="left">
												<xsl:value-of select="$lab_383" />
											</td>
											<td width="85%" align="left">
												<input type="Text" style="width:30px;" maxlength="3"
													name="iti_score_to" value="{//searchCom/iti_score_to}" class="wzb-inputText" />
											</td>
										</tr>
									</table>
				
								</td>
							
							</xsl:otherwise>
						</xsl:choose>
						
						<td>
						</td>
					</tr>
					<tr>
						<td>
						</td>
						<td colspan="6" align="center">
							<xsl:call-template name="wb_gen_button">
								<xsl:with-param name="class">btn wzb-btn-orange</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_name" select="$lab_btn_srh" />
								<xsl:with-param name="wb_gen_btn_href">javascript: instr.int_search(document.frmXml, '<xsl:value-of select="$iti_type_mark" />');</xsl:with-param>
							</xsl:call-template>
						</td>
						<td>
						</td>
					</tr>
					-->
					<xsl:if test="$is_poup='true'"> 
						<tr>
						  <td>
						    <xsl:call-template name="wb_ui_title">
								<xsl:with-param name="text_class" >pop-up-title</xsl:with-param>
								<xsl:with-param name="text" select="$lab_1068"/>
							</xsl:call-template>
						  </td>
						</tr>
					</xsl:if>
					<tr>
						<td colspan="3" align="right" style="padding:9px 8px 10px 8px;">
							<from class="form-search">
								<xsl:choose>
									<xsl:when test="$iti_type_mark='IN'">
								<input class="form-control" id="search_text" type="text" style=""  value="{//searchCom/search_text}"/>
								</xsl:when>
								<xsl:otherwise>
								<input class="form-control" id="search_text" type="text"  value="{//searchCom/search_text}"/>
								</xsl:otherwise>
								</xsl:choose>
								<input id="searchBtn" class="form-submit" type="button" onClick="searchInstr('{$iti_type_mark}');"/>
							</from>
							<xsl:choose>
							<xsl:when test="$is_poup='true'"></xsl:when>
							<xsl:otherwise>
								<xsl:call-template name="wb_gen_button">
											<!-- 
												<xsl:with-param name="style">line-height:normal</xsl:with-param>
											 -->
											<xsl:with-param name="class">btn wzb-btn-yellow margin-right4</xsl:with-param>
											<xsl:with-param name="wb_gen_btn_name" select="$lab_btn_add" />
											<xsl:with-param name="wb_gen_btn_href">
												<xsl:choose>
													<xsl:when test="$iti_type_mark = 'EXT'">
														<xsl:text>javascript: instr.ext_ins_upd_prep();</xsl:text>
													</xsl:when>
													<xsl:otherwise>
														<xsl:text>javascript: instr.int_pickup();</xsl:text>
													</xsl:otherwise>
												</xsl:choose>
											</xsl:with-param>
								</xsl:call-template>
							</xsl:otherwise>
							</xsl:choose>
						
						</td>
					</tr>
				</table>
				<!-- ===================== Search Form ===================== -->
	

		    </xsl:otherwise>
		</xsl:choose>	   

		<xsl:choose>
			<xsl:when test="count(/instructor/instructor_lst/instructor) = 0">
				<xsl:call-template name="wb_ui_show_no_item">
					<xsl:with-param name="text"><xsl:choose>
							<xsl:when test="$iti_type_mark='IN'">
								<xsl:value-of select="$lab_969"/>
							</xsl:when>
							<xsl:when test="$iti_type_mark='EXT'">
								<xsl:value-of select="$lab_970"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$lab_1069"/>
							</xsl:otherwise>
						</xsl:choose></xsl:with-param>
						
					<xsl:with-param name="width">
						<xsl:value-of select="$table_width" />
					</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<table class="table wzb-ui-table" style="margin-top:0px;">
					<tr class="wzb-ui-table-head">
						<td width="13%">
							<xsl:choose>
								<xsl:when test="$is_poup='true'">
								</xsl:when>
								<xsl:otherwise></xsl:otherwise>
							</xsl:choose>
							<span>
								<xsl:value-of select="$lab_967" />
							</span>
						</td>
						<!-- <td width="8%">
							<xsl:value-of select="$lab_940" />
						</td> -->
						<td width="12%">
							<xsl:value-of select="$lab_951" />
						</td>
						<xsl:choose>
							<xsl:when test="$iti_type_mark='EXT'">
								<td width="16%">
									<xsl:value-of select="$lab_955" />
								</td>
								<td width="16%">
									<xsl:value-of select="$lab_1045" />
								</td>
								<td width="14%">
									<xsl:value-of select="$lab_1018" />
								</td>
							</xsl:when>
							<xsl:when test="$iti_type_mark='IN'">
								<!-- <td width="13%">
									<xsl:value-of select="$lab_949" />
								</td> -->
								<td width="22%">
									<xsl:value-of select="$lab_955" />
								</td>
							</xsl:when>
						</xsl:choose>
						<td width="11%">
							<xsl:value-of select="$lab_960" />
						</td>
						<!-- <td width="10%" align="right">
							<xsl:value-of select="$lab_942" />
						</td> -->
					</tr>
					<xsl:for-each select="/instructor/instructor_lst/instructor">
						<tr>
						<td>
							<xsl:choose>
								<xsl:when test="$is_poup='true'">
									<input style="margin-top:8px" type="checkbox" name="usr" value="{@id}"/>
									<script language="JavaScript"><![CDATA[
										usr_]]><xsl:value-of select="@id"/><![CDATA[_id = ]]><xsl:value-of select="@id"/><![CDATA[
										usr_]]><xsl:value-of select="@id"/><![CDATA[_type = ]]><xsl:value-of select="@id"/><![CDATA[
										usr_]]><xsl:value-of select="@id"/><![CDATA[_name = ']]><xsl:call-template name="escape_js"><xsl:with-param name="input_str"><xsl:value-of select="iti_name"/></xsl:with-param></xsl:call-template><![CDATA['
									]]></script>
									<xsl:value-of select="iti_name" />
								</xsl:when>
								<xsl:otherwise>
								    <a class="Text" href="javascript:instr.get({@id}, '{$iti_type_mark}',isExcludes);" ><xsl:value-of select="iti_name" /></a>
								</xsl:otherwise>
							</xsl:choose>
							
						</td>
						<td>
							<xsl:variable name="iti_level" select="iti_level"></xsl:variable>
							<xsl:choose>
								<xsl:when test="$iti_level_options and count($iti_level_options/option[@value=$iti_level]) &gt;0">
									<xsl:for-each select="$iti_level_options/option">
										<xsl:if test="@value = $iti_level">
											<xsl:value-of select="text()" />
										</xsl:if>
									</xsl:for-each>
								</xsl:when>
								<xsl:otherwise>--</xsl:otherwise>
							</xsl:choose>
						</td>
						<xsl:choose>
							<xsl:when test="$iti_type_mark='EXT'">
								<td>

										<xsl:choose>
											<xsl:when test="iti_main_course=''">--</xsl:when>
									       <xsl:otherwise>
												<xsl:choose>
													<xsl:when test="string-length(iti_main_course)>18">
														<span class="Text"  title="{iti_main_course}">
															<xsl:value-of select="substring(iti_main_course,0,18)"/>...
														</span>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="iti_main_course"/>
													</xsl:otherwise>
												</xsl:choose>
										       
									       </xsl:otherwise>
										</xsl:choose>
						
								</td>
								<td>
									<xsl:choose>
										<xsl:when test="iti_training_company=''">--</xsl:when>
								       <xsl:otherwise>
								      		 <xsl:choose>
												<xsl:when test="string-length(iti_training_company)>12">
													<span class="Text"  title="{iti_training_company}">
														<xsl:value-of select="substring(iti_training_company,0,12)"/>...
													</span>
												</xsl:when>
												<xsl:otherwise>
													<span class="Text" >
														<xsl:value-of select="iti_training_company"/>
													</span>
												</xsl:otherwise>
											</xsl:choose>
											
								       </xsl:otherwise>
									</xsl:choose>
								</td>
								<td>
									<xsl:choose>
										<xsl:when test="iti_expertise_areas=''">--</xsl:when>
								       <xsl:otherwise>
								     		  <xsl:choose>
												<xsl:when test="string-length(iti_expertise_areas)>18">
													<span class="Text"  title="{iti_expertise_areas}">
														<xsl:value-of select="substring(iti_expertise_areas,0,18)"/>...
													</span>
												</xsl:when>
												<xsl:otherwise>
													<span class="Text" >
														<xsl:value-of select="iti_expertise_areas"/>
													</span>
												</xsl:otherwise>
											</xsl:choose>
								       </xsl:otherwise>
									</xsl:choose>
								</td>
							</xsl:when>
							<xsl:when test="$iti_type_mark='IN'">
								<td>
									<xsl:choose>
										<xsl:when test="iti_main_course=''">--</xsl:when>
								       <xsl:otherwise>
								     		  <xsl:choose>
												<xsl:when test="string-length(iti_main_course)>35">
													<span class="Text"  title="{iti_main_course}">
														<xsl:value-of select="substring(iti_main_course,0,35)"/>...
													</span>
												</xsl:when>
												<xsl:otherwise>
													<span class="Text" >
														<xsl:value-of select="iti_main_course"/>
													</span>
												</xsl:otherwise>
											</xsl:choose>
								       </xsl:otherwise>
									</xsl:choose>
								</td>
							</xsl:when>
						</xsl:choose>
						<td id="itc_score" >
							<xsl:call-template name="supplier_star">
						       	<xsl:with-param name="score" select="score"/>
					       	</xsl:call-template>
						</td>

					</tr>
					</xsl:for-each>
				</table>
				
				<xsl:call-template name="wb_ui_pagination">
					<xsl:with-param name="cur_page" select="$cur_page"/>
					<xsl:with-param name="page_size" select="$page_size"/>
					<xsl:with-param name="total" select="$total"/>
					<xsl:with-param name="width" select="$table_width"/>
					<xsl:with-param name="cur_page_name">cur_page</xsl:with-param>
					<xsl:with-param name="wzb-page-style" >padding-top: 17px;</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:choose>
			<xsl:when test="$is_poup='true'">
				<div class="wzb-bar" style="margin-top:0px">
					<xsl:call-template name="wb_gen_form_button">
						<!-- 
								<xsl:with-param name="style">line-height:normal</xsl:with-param>
						 -->
		
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_ok" />
							<xsl:with-param name="wb_gen_btn_href">
								<xsl:choose>
									<xsl:when test="//searchCom/for_time_table='ADD'">javascript:instr.edit_lesson_ins(document.frmXml,'<xsl:value-of select="$lab_10681" />','setting_instr')</xsl:when>
									<xsl:when test="//searchCom/for_time_table='DEL'">javascript:instr.edit_lesson_ins(document.frmXml,'<xsl:value-of select="$lab_1067" />','REMOVE_INSTR')</xsl:when>
									<xsl:otherwise>javascript:goldenman.pickitemacc(document.frmXml,max_select)</xsl:otherwise>
								</xsl:choose>
							</xsl:with-param>
					</xsl:call-template>
					<xsl:call-template name="wb_gen_form_button">
						<!-- 
							<xsl:with-param name="style">line-height:normal</xsl:with-param>
						 -->
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel" />
						<xsl:with-param name="wb_gen_btn_href">javascript:window.close()</xsl:with-param>
					</xsl:call-template>
			</div>
			</xsl:when>
			<xsl:otherwise>
			</xsl:otherwise>
		</xsl:choose>
		</xsl:template>
	</xsl:stylesheet>