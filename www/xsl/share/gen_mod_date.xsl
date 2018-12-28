<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template name="content">
		<xsl:param name="lab_run_info"/>
		<xsl:param name="lab_name"/>
		<xsl:param name="lab_online_content"/>
		<xsl:param name="lab_status"/>
		<xsl:param name="lab_avg_per_learner"/>
		<xsl:param name="lab_hits"/>
		<xsl:param name="lab_avg_per_hit"/>
		<xsl:param name="lab_last_access"/>
		<xsl:param name="lab_avg_score"/>
		<xsl:param name="lab_detail"/>
		<xsl:param name="lab_status_viewed"/>
		<xsl:param name="lab_status_not_viewed"/>
		<xsl:param name="lab_all"/>
		<xsl:param name="lab_module_list"/>
		<xsl:param name="lab_folder_list"/>
		<xsl:param name="lab_no_module_rpt"/>
		<xsl:param name="lab_item_type"/>
		<xsl:param name="lab_user_report"/>
		<xsl:param name="lab_usage_report"/>
		<xsl:param name="lab_tracking_report"/>
		<xsl:param name="lab_tracking_report_desc"/>
		<xsl:param name="lab_g_form_btn_close"/>
		<xsl:param name="lab_folder"/>
		<xsl:param name="lab_module"/>
		<xsl:param name="lab_A"/>
		<xsl:param name="lab_B"/>
		<xsl:param name="lab_C"/>
		<xsl:param name="lab_D"/>
		<xsl:param name="lab_F"/>
		<xsl:param name="nav_link"/>
		<xsl:param name="btn_ok_link"/>
		<xsl:param name="parent_code"/>
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="JavaScript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" src="{$wb_js_path}wb_report.js"/>
			<script language="JavaScript" src="{$wb_js_path}wb_item.js"/>
			<script language="JavaScript" src="{$wb_js_path}urlparam.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script src="{$wb_js_path}wb_assignment.js" language="JavaScript"/>
			<script src="{$wb_js_path}wb_module.js" language="JavaScript"/>
			<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
					wb_utils_set_cookie('report_type' , 'usage'); 
					var itm_lst = new wbItem;
					var module_lst = new wbModule;
					var ass = new wbAssignment;
					var rpt = new wbReport;
					
					
					function go_folder(identifier){
						var url = setUrlParam('location',identifier)
						window.location.href = url;
					}
					
			]]></SCRIPT>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
			<form name="frmSearch" onSubmit="return status()">
		    <xsl:call-template name="itm_action_nav">
				<xsl:with-param  name="cur_node_id">117</xsl:with-param>
			</xsl:call-template>
			<div class="wzb-item-main">
				<xsl:call-template name="wb_ui_hdr">
					<xsl:with-param name="belong_module" select="$belong_module"></xsl:with-param>
					<xsl:with-param name="parent_code" select="$parent_code"/>
				</xsl:call-template>
				<xsl:call-template name="wb_ui_title">
					<xsl:with-param name="text" select="$lab_tracking_report"/>
				</xsl:call-template>
				<xsl:call-template name="wb_ui_nav_link">
					<xsl:with-param name="text">
						<xsl:copy-of select="$nav_link"/>
			    </xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="tracking_rpt_gen_tab">
					<xsl:with-param name="rpt_target_tab">2</xsl:with-param>
					<xsl:with-param name="course_id" select="$cos_id"/>
				</xsl:call-template>
				<xsl:call-template name="wb_ui_nav_link">
					<xsl:with-param name="text">
						<xsl:choose>
							<xsl:when test="$cur_node != '' ">
								<a href="javascript:go_folder('')" class="NavLink">
									<xsl:value-of select="report/course/@title"/>
								</a>
								<xsl:text>&#160;&gt;&#160;</xsl:text>
								<xsl:apply-templates select="report/course/tableofcontents/item[descendant::item[@identifier = $cur_node]]" mode="path"/>
								<xsl:value-of select="report/course/tableofcontents//item[@identifier = $cur_node]/@title"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="report/course/@title"/>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:with-param>
				</xsl:call-template>
				<!-- list of folder -->
				<xsl:choose>
					<xsl:when test="$cur_node != ''">
						<xsl:apply-templates select="report/course/tableofcontents//item[@identifier = $cur_node]" mode="item">
							<xsl:with-param name="lab_folder" select="$lab_folder"/>
							<xsl:with-param name="lab_hits" select="$lab_hits"/>
							<xsl:with-param name="lab_folder_list" select="$lab_folder_list"/>
						</xsl:apply-templates>
					</xsl:when>
					<xsl:otherwise>
						<xsl:apply-templates select="report/course/tableofcontents" mode="item">
							<xsl:with-param name="lab_folder" select="$lab_folder"/>
							<xsl:with-param name="lab_hits" select="$lab_hits"/>
							<xsl:with-param name="lab_folder_list" select="$lab_folder_list"/>
						</xsl:apply-templates>
					</xsl:otherwise>
				</xsl:choose>
				
				<!-- list of modules -->
				<xsl:choose>
					<xsl:when test="$cur_node != ''">
						<xsl:apply-templates select="report/course/tableofcontents//item[@identifier = $cur_node]" mode="module">
							<xsl:with-param name="lab_name" select="$lab_name"/>
							<xsl:with-param name="lab_status" select="$lab_status"/>
							<xsl:with-param name="lab_avg_per_learner" select="$lab_avg_per_learner"/>
							<xsl:with-param name="lab_hits" select="$lab_hits"/>
							<xsl:with-param name="lab_avg_per_hit" select="$lab_avg_per_hit"/>
							<xsl:with-param name="lab_last_access" select="$lab_last_access"/>
							<xsl:with-param name="lab_avg_score" select="$lab_avg_score"/>
							<xsl:with-param name="lab_detail" select="$lab_detail"/>
							<xsl:with-param name="lab_no_module_rpt" select="$lab_no_module_rpt"/>
							<xsl:with-param name="lab_module" select="$lab_module"/>
							<xsl:with-param name="lab_A" select="$lab_A"/>
							<xsl:with-param name="lab_B" select="$lab_B"/>
							<xsl:with-param name="lab_C" select="$lab_C"/>
							<xsl:with-param name="lab_D" select="$lab_D"/>
							<xsl:with-param name="lab_F" select="$lab_F"/>
						</xsl:apply-templates>
					</xsl:when>
					<xsl:otherwise>
						<xsl:choose>
							<xsl:when test="report/course/tableofcontents and count(report/course/tableofcontents/item[@identifierref!='' and itemtype != 'FDR']) &gt; 0">
								<xsl:apply-templates select="report/course/tableofcontents" mode="module">
									<xsl:with-param name="lab_name" select="$lab_name"/>
									<xsl:with-param name="lab_status" select="$lab_status"/>
									<xsl:with-param name="lab_avg_per_learner" select="$lab_avg_per_learner"/>
									<xsl:with-param name="lab_hits" select="$lab_hits"/>
									<xsl:with-param name="lab_avg_per_hit" select="$lab_avg_per_hit"/>
									<xsl:with-param name="lab_last_access" select="$lab_last_access"/>
									<xsl:with-param name="lab_avg_score" select="$lab_avg_score"/>
									<xsl:with-param name="lab_detail" select="$lab_detail"/>
									<xsl:with-param name="lab_no_module_rpt" select="$lab_no_module_rpt"/>
									<xsl:with-param name="lab_module" select="$lab_module"/>
									<xsl:with-param name="lab_A" select="$lab_A"/>
									<xsl:with-param name="lab_B" select="$lab_B"/>
									<xsl:with-param name="lab_C" select="$lab_C"/>
									<xsl:with-param name="lab_D" select="$lab_D"/>
									<xsl:with-param name="lab_F" select="$lab_F"/>							
								</xsl:apply-templates>
							</xsl:when>
							<xsl:otherwise>
								<xsl:if test="count(report/course/tableofcontents/item) = 0">
								<xsl:call-template name="wb_ui_show_no_item">
									<xsl:with-param name="text" select="$lab_no_module_rpt"/>
								</xsl:call-template>
								</xsl:if>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:otherwise>
				</xsl:choose>
				<div class="wzb-bar">
							<xsl:call-template name="wb_gen_form_button">
								<xsl:with-param name="wb_gen_btn_name" select="$lab_gen_ok"/>
								<xsl:with-param name="wb_gen_btn_href"><xsl:value-of select="$btn_ok_link"/>(<xsl:value-of select="$itm_id"/>)</xsl:with-param>
							</xsl:call-template>
				</div>
			</div>
			</form>
		</body>
	</xsl:template>
	<!--=========================================================================-->
	<xsl:template match="item | tableofcontents " mode="module">
		<xsl:param name="lab_name"/>
		<xsl:param name="lab_status"/>
		<xsl:param name="lab_avg_per_learner"/>
		<xsl:param name="lab_hits"/>
		<xsl:param name="lab_avg_per_hit"/>
		<xsl:param name="lab_last_access"/>
		<xsl:param name="lab_avg_score"/>
		<xsl:param name="lab_detail"/>
		<xsl:param name="lab_no_module_rpt"/>
		<xsl:param name="lab_module"/>
		<xsl:param name="lab_A"/>
		<xsl:param name="lab_B"/>
		<xsl:param name="lab_C"/>
		<xsl:param name="lab_D"/>
		<xsl:param name="lab_F"/>	
		<!-- 
		<xsl:call-template name="wb_ui_space"/>
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text" select="$lab_module"/>
		</xsl:call-template>
		 -->	
		<xsl:choose>
			<xsl:when test="count(item[@identifierref!='' and itemtype != 'FDR']) &gt; 0">
				<table class="table wzb-ui-table">
					<tr class="wzb-ui-table-head">
						<td width="30">
						</td>
						<td width="120">
							<xsl:value-of select="$lab_name"/>
						</td>
						<td align="center" width="120">
							<xsl:value-of select="$lab_status"/>
						</td>
						<td align="center">
							<xsl:value-of select="$lab_avg_per_learner"/>
						</td>
						<td align="center">
							<xsl:value-of select="$lab_hits"/>
						</td>
						<td align="center">
							<xsl:value-of select="$lab_avg_per_hit"/>
						</td>
						<td align="center" width="100">
							<xsl:value-of select="$lab_last_access"/>
						</td>
						<td align="center">
							<xsl:value-of select="$lab_avg_score"/>
						</td>
						<!--
				<td width="60">
					<span class="SecBg">
						<xsl:value-of select="$lab_detail"/>
					</span>
				</td>
				-->
					</tr>
					<xsl:for-each select="item[@identifierref!='' and itemtype != 'FDR']">
						<xsl:variable name="idref" select="@identifierref"/>
						<xsl:variable name="row_class">
							<xsl:choose>
								<xsl:when test="position() mod 2">RowsEven</xsl:when>
								<xsl:otherwise>RowsOdd</xsl:otherwise>
							</xsl:choose>
						</xsl:variable>
						<xsl:apply-templates select="/report/course/module_list/module[@id = $idref]">
							<xsl:with-param name="row_class" select="$row_class"/>
							<xsl:with-param name="lab_A" select="$lab_A"/>
							<xsl:with-param name="lab_B" select="$lab_B"/>
							<xsl:with-param name="lab_C" select="$lab_C"/>
							<xsl:with-param name="lab_D" select="$lab_D"/>
							<xsl:with-param name="lab_F" select="$lab_F"/>
						</xsl:apply-templates>
					</xsl:for-each>
				</table>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="wb_ui_show_no_item">
					<xsl:with-param name="text" select="$lab_no_module_rpt"/>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="module">
		<xsl:param name="lab_A"/>
		<xsl:param name="lab_B"/>
		<xsl:param name="lab_C"/>
		<xsl:param name="lab_D"/>
		<xsl:param name="lab_F"/>	
		<xsl:param name="row_class"/>
		<tr class="{$row_class}">
			<td valign="middle">
				<xsl:variable name="item_type_lowercase">
					<xsl:call-template name="change_lowercase">
						<xsl:with-param name="input_value" select="@type"/>
					</xsl:call-template>
				</xsl:variable>
				<img src="{$wb_img_path}icol_{$item_type_lowercase}.gif" border="0" align="absmiddle"/>
			</td>
			<td>
				<xsl:value-of select="@title"/>
			</td>
			<td align="center">
				<xsl:choose>
					<xsl:when test="@hits=0">--</xsl:when>
					<xsl:otherwise>
						<xsl:apply-templates select="status">
							<xsl:with-param name="mod_type" select="@type"/>
							<xsl:with-param name="is_wizpack">
								<xsl:choose>
									<xsl:when test="@src_type != 'WIZPACK'"/>
									<xsl:otherwise>true</xsl:otherwise>
								</xsl:choose>
							</xsl:with-param>
							<xsl:with-param name="score">
								<xsl:call-template name="display_score">
									<xsl:with-param name="score">
										<xsl:value-of select="@score"/>
									</xsl:with-param>
								</xsl:call-template>
							</xsl:with-param>
							<xsl:with-param name="attempted" select="@attempted" />
						</xsl:apply-templates>
					</xsl:otherwise>
				</xsl:choose>
			</td>
			<td align="center">
				<xsl:choose>
					<xsl:when test="@hits=0">--</xsl:when>
					<xsl:otherwise>
						<xsl:choose>
							<xsl:when test="substring-before(@avg_time_per_learner,'.') = ''  or substring-before(@avg_time_per_learner,'.') = 'null'">
								<xsl:value-of select="@avg_time_per_learner"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="substring-before(@avg_time_per_learner,'.')"/>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:otherwise>
				</xsl:choose>
			</td>
			<td align="center">
				<xsl:choose>
					<xsl:when test="@hits=0">--</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="@hits"/>
					</xsl:otherwise>
				</xsl:choose>
			</td>
			<td align="center">
				<xsl:choose>
					<xsl:when test="@hits=0">--</xsl:when>
					<xsl:otherwise>
						<xsl:choose>
							<xsl:when test="substring-before(@avg_time_per_hit,'.') = ''  or substring-before(@avg_time_per_hit,'.') = 'null'">
								<xsl:value-of select="@avg_time_per_hit"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="substring-before(@avg_time_per_hit,'.')"/>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:otherwise>
				</xsl:choose>
			</td>
			<td align="center" nowrap="nowrap">
				<xsl:choose>
					<xsl:when test="@last_access = '' or @last_access = 'null'">--</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="display_time">
							<xsl:with-param name="my_timestamp">
								<xsl:value-of select="@last_access"/>
							</xsl:with-param>
							<xsl:with-param name="dis_time">T</xsl:with-param>
						</xsl:call-template>
					</xsl:otherwise>
				</xsl:choose>
			</td>
			<td align="center">
				<xsl:choose>
					<xsl:when test="@type = 'SVY'">
						<xsl:text>--</xsl:text>
					</xsl:when>
					<!-- handle not submitted case -->
					<xsl:when test="@type = 'TST' and @status='I'">
						<xsl:text>--</xsl:text>
					</xsl:when>
					<xsl:when test="@type = 'DXT' and @status='I'">
						<xsl:text>--</xsl:text>
					</xsl:when>
					<!-- ========== -->
					<xsl:when test="@score != ''">
						<xsl:call-template name="display_score">
							<xsl:with-param name="score">
								<xsl:choose>
									<xsl:when test="string(number(@score)) = 'NaN'">
										<xsl:choose>
											<xsl:when test="@score = 'A+'"><xsl:value-of select="$lab_A"/>+</xsl:when>
											<xsl:when test="@score = 'A'"><xsl:value-of select="$lab_A"/></xsl:when>
											<xsl:when test="@score = 'A-'"><xsl:value-of select="$lab_A"/>-</xsl:when>
											<xsl:when test="@score = 'B+'"><xsl:value-of select="$lab_B"/>+</xsl:when>
											<xsl:when test="@score = 'B'"><xsl:value-of select="$lab_B"/></xsl:when>
											<xsl:when test="@score = 'B-'"><xsl:value-of select="$lab_B"/></xsl:when>
											<xsl:when test="@score = 'C+'"><xsl:value-of select="$lab_C"/>+</xsl:when>
											<xsl:when test="@score = 'C'"><xsl:value-of select="$lab_C"/></xsl:when>
											<xsl:when test="@score = 'C-'"><xsl:value-of select="$lab_C"/>-</xsl:when>
											<xsl:when test="@score = 'D'"><xsl:value-of select="$lab_D"/></xsl:when>
											<xsl:when test="@score = 'F'"><xsl:value-of select="$lab_F"/></xsl:when>
										</xsl:choose>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="@score"/>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:with-param>
						</xsl:call-template>
					</xsl:when>
					<xsl:when test="@mod_vendor = 'SkillSoft'">--</xsl:when>
					<xsl:otherwise>--</xsl:otherwise>
				</xsl:choose>
			</td>
		</tr>
	</xsl:template>
	<!--=====================================================================-->
	<xsl:template match="status">
		<xsl:param name="mod_type"/>
		<xsl:param name="is_wizpack"/>
		<xsl:param name="score"/>
		<xsl:param name="attempted" />
			<xsl:for-each select="attribute::*">
				<xsl:if test=".!=0">
					<!-- <xsl:value-of select="."/>&#160; -->
				<xsl:call-template name="display_progress_tracking">
						<xsl:with-param name="status">
							<xsl:value-of select="name()"/>
						</xsl:with-param>
						<xsl:with-param name="type">module</xsl:with-param>
						<xsl:with-param name="mod_type" select="$mod_type"/>
						<xsl:with-param name="show_text">true</xsl:with-param>
						<xsl:with-param name="score" select="$score"/>
						<xsl:with-param name="is_wizpack" select="$is_wizpack"/>
						<xsl:with-param name="attempted" select="$attempted"/>
					</xsl:call-template>
					<img border="0" height="1" src="{$wb_img_path}tp.gif" width="5"/>
					<br/>
				</xsl:if>
			</xsl:for-each>
	</xsl:template>
	<!--=====================================================================-->
	<xsl:template match="item | tableofcontents" mode="item">
		<xsl:param name="lab_folder"/>

		<xsl:if test="count(item[itemtype = 'FDR']) != 0">
			<xsl:call-template name="wb_ui_head">
				<xsl:with-param name="text" select="$lab_folder"/>
			</xsl:call-template>
			<table>
				<tr>
					<td align="left">
						<table>
							<xsl:for-each select="item[itemtype = 'FDR']">
								<xsl:if test="not(position() mod 2 = 0) ">
									<xsl:text disable-output-escaping="yes">&lt;tr&gt;</xsl:text>
								</xsl:if>
								<td width="50%">
									<table>
										<tr>
											<td width="5%" align="right">
												<img src="{$wb_img_path}ico_big_folder.gif" border="0"/>
											</td>
											<td width="1%">
											</td>
											<td width="93%" align="left">
												<a href="Javascript:go_folder('{@identifier}')" class="Text">
													<xsl:value-of select="@title"/>
												</a>
											</td>
										</tr>
									</table>
								</td>
								<xsl:if test="position() = last() and not(position() mod 2 = 0 )">
									<xsl:text disable-output-escaping="yes">&lt;td&gt;&amp;nbsp;&lt;td&gt;&lt;tr&gt;</xsl:text>
								</xsl:if>
								<xsl:if test="position() mod 2 = 0">
									<xsl:text disable-output-escaping="yes">&lt;tr&gt;</xsl:text>
									<tr>
										<td colspan="2">
										</td>
									</tr>
								</xsl:if>
							</xsl:for-each>
						</table>
					</td>
				</tr>
			</table>
		</xsl:if>

	</xsl:template>
	<!--=====================================================================-->
	<xsl:template match="item" mode="path">
		<a href="javascript:javascript:go_folder('{@identifier}')" class="NavLink">
			<xsl:value-of select="@title"/>
		</a>
		<xsl:text>&#160;&gt;&#160;</xsl:text>
		<xsl:apply-templates select="item[descendant::item[@identifier = $cur_node]]" mode="path"/>
	</xsl:template>
	<!-- ============================================================================================== -->
	<xsl:template match="item" mode="nav">
		<xsl:param name="lab_run_info"/>
		<xsl:param name="lab_session_info"/>
		<xsl:param name="current_role"/>
		<xsl:variable name="_count" select="count(preceding-sibling::item)"/>
		<xsl:choose>
			<xsl:when test="@run_ind = 'true'">
				<xsl:text>&#160;&gt;&#160;</xsl:text>
				<xsl:variable name="value">
					<xsl:for-each select="preceding-sibling::item">
						<xsl:if test="position()=$_count">
							<xsl:value-of select="@id"/>
						</xsl:if>
					</xsl:for-each>
				</xsl:variable>
				<a href="javascript:itm_lst.get_item_run_list({$value})" class="NavLink">
					<xsl:value-of select="$lab_run_info"/>
				</a>
				<span class="NavLink">&#160;&gt;&#160;</span>
				<a href="javascript:itm_lst.get_item_run_detail({@id})" class="NavLink">
				<xsl:value-of select="title"/> 
				</a>
			</xsl:when>
			<xsl:when test="@session_ind = 'true'">
				<span class="NavLink">&#160;&gt;&#160;</span>
				<xsl:variable name="value">
					<xsl:for-each select="preceding-sibling::item">
						<xsl:if test="position()=last()">
							<xsl:value-of select="@id"/>
						</xsl:if>
					</xsl:for-each>
				</xsl:variable>
				<a href="javascript:itm_lst.session.get_session_list({$value})" class="NavLink">
					<xsl:value-of select="$lab_session_info"/>
				</a>
				<span>&#160;&gt;&#160;</span>
				<a href="javascript:itm_lst.get_item_run_detail({@id})" class="NavLink">
					<xsl:value-of select="title"/>
				</a>
			</xsl:when>
			<xsl:otherwise>
				<xsl:choose>
					<xsl:when test="$current_role='INSTR_1'">
						<a href="javascript:itm_lst.get_itm_instr_view({@id})" class="NavLink">
							<xsl:value-of select="title"/>
						</a>
					</xsl:when>
					<xsl:otherwise>
						<a href="javascript:itm_lst.get_item_detail({@id})" class="NavLink">
							<xsl:value-of select="title"/>
						</a>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>
