<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_nav_link.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="share/itm_gen_action_nav_share.xsl"/>
	
	<xsl:output indent="yes"/>
	<xsl:variable name="mod_cnt_all" select="count(CourseCriteria/marking_scheme_list/item)+count(CourseCriteria/marking_scheme_list/item_del)"/>
	<xsl:variable name="mod_cnt" select="count(CourseCriteria/marking_scheme_list/item)"/>
	<xsl:variable name="online_cnt" select="count(CourseCriteria/online_module/module)"/>
	<xsl:variable name="itm_id" select="/CourseCriteria/item/@id"/>
	<xsl:variable name="run_ind" select="/CourseCriteria/item/@run_ind"/>
	<xsl:variable name="ccr_id" select="/CourseCriteria/marking_scheme_list/@ccr_id"/>
	<xsl:variable name="mod_res_id" select="/CourseCriteria/marking_scheme_list/mod_res_id"/>
	<xsl:variable name="cmt_max_score" select="/CourseCriteria/marking_scheme_list/item[@cmt_id = '0']/cmt_max_score"/>
	<xsl:variable name="cmt_pass_score" select="/CourseCriteria/marking_scheme_list/item[@cmt_id = '0']/cmt_pass_score"/>
	<xsl:variable name="cmt_title" select="/CourseCriteria/marking_scheme_list/item[@cmt_id = '0']/cmt_title"/>
	<xsl:variable name="flag" select="count(CourseCriteria/marking_scheme_list/item[@cmt_id = '0'])"/>
	<xsl:variable name="per_sum" select="sum(CourseCriteria/marking_scheme_list/item/cmt_contri_rate)"/>
	<xsl:variable name="cmt_id_del" select="/CourseCriteria/marking_scheme_list/item_del/@cmt_id_del"/>
	<xsl:variable name="current_role" select="/CourseCriteria/current_role"/>
	<!-- ================================================================ -->
	<xsl:template match="/">
		<xsl:apply-templates select="CourseCriteria"/>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template match="CourseCriteria">
		<html>
			<xsl:call-template name="draw_header"/>
			<xsl:call-template name="draw_body"/>
		</html>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template name="draw_header">
		<head>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<meta http-equiv="Content-Type" content="text/html; charset={$wb_lang_encoding}"/>
			<xsl:call-template name="wb_css">
				<xsl:with-param name="view">wb_ui</xsl:with-param>
			</xsl:call-template>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_item.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_attendance.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_scorescheme.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="Javascript"><![CDATA[
			var cmt=new wbScoreScheme;
			attd = new wbAttendance;
			itm_lst = new wbItem;
			
			function changeScore(cmt_id){
				var trobj = document.all.item("cmt_contri_rate");			
				var total= 0;
				if(trobj.length > 0){
			   		for(var i = 0; i < trobj.length; i++){
			   			if(trobj[i].value == ""){
			   				trobj[i].value = 0;
			   			}
						total += parseFloat(trobj[i].value);
					}
				}else{
				   total= parseFloat(document.getElementById(cmt_id).value);
				}
				document.frmXml.totalscore.value= total;
			}
			]]></script>
		</head>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template name="draw_body">
		<body topmargin="0" leftmargin="0" marginwidth="0" marginheight="0" onLoad="wb_utils_gen_form_focus(document.frmXml)">
			<form name="frmXml">
				<xsl:call-template name="wb_init_lab"/>
				<input type="hidden" name="url_success" value=""/>
				<input type="hidden" name="url_failure" value=""/>
				<input type="hidden" name="cmd" value="all_cmt_oper_final"/>
				<input type="hidden" name="ccr_id" value=""/>
				<input type="hidden" name="itm_id" value=""/>
				<input type="hidden" name="module" value="course.CourseCriteriaModule"/>
				<input type="hidden" name="mod_res_id" value="{$mod_res_id}"/>
				<input type="hidden" name="cmt_id_list" value=""/>
				<input type="hidden" name="cmt_id_percent_list" value=""/>
				<input type="hidden" name="cmt_max_score" value="{$cmt_max_score}"/>
				<input type="hidden" name="cmt_pass_score" value="{$cmt_pass_score}"/>
				<input type="hidden" name="cmt_title" value="{$cmt_title}"/>
				<input type="hidden" name="cmt_id_del" value="{$cmt_id_del}"/>
				<input type="hidden" name="re_evaluate_ind" value=""/>
				<input type="hidden" name="upd_comp_date" value=""/>
			</form>
		</body>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_run_info">
				<xsl:value-of select="$lab_const_run"/>訊息</xsl:with-param>
			<xsl:with-param name="lab_content">内容</xsl:with-param>
			<xsl:with-param name="lab_title">標題</xsl:with-param>
			<xsl:with-param name="lab_main_eval">添加計分項目</xsl:with-param>
			<xsl:with-param name="lab_main_mod_eval">修改比重</xsl:with-param>
			<xsl:with-param name="lab_score_item">計分規則</xsl:with-param>
			<xsl:with-param name="lab_percent">比重(%)</xsl:with-param>
			<xsl:with-param name="lab_no_eval">沒有计分项目</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_ok">確定</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_message">*比率合計必須為100%。</xsl:with-param>
			<xsl:with-param name="lab_scope">應用範圍</xsl:with-param>
			<xsl:with-param name="lab_sum">合計</xsl:with-param>
			<xsl:with-param name="lab_not_re_evaluate">此修改只應用於日後的分數計算，但並不應用於以往的分數及其結訓條件。</xsl:with-param>
			<xsl:with-param name="lab_re_evaluate">此修改除應用於日後的分數計算外，亦即時應用於以往的分數及其結訓記錄，並有以下選項：</xsl:with-param>
			<xsl:with-param name="lab_mod_date">原有的結訓日期改為更新設定當日</xsl:with-param>
			<xsl:with-param name="lab_message_mod">*此修改只會影響日後所開班別的預設值，並不會影響已有班別的設定。</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_run_info">
				<xsl:value-of select="$lab_const_run"/>信息</xsl:with-param>
			<xsl:with-param name="lab_content">内容</xsl:with-param>
			<xsl:with-param name="lab_title">标题</xsl:with-param>
			<xsl:with-param name="lab_main_eval">添加计分项目</xsl:with-param>
			<xsl:with-param name="lab_main_mod_eval">修改比重</xsl:with-param>
			<xsl:with-param name="lab_sum">合计</xsl:with-param>
			<xsl:with-param name="lab_score_item">计分规则</xsl:with-param>
			<xsl:with-param name="lab_percent">比重(%)</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_ok">确定</xsl:with-param>
			<xsl:with-param name="lab_no_eval">沒有计分项目</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_scope">应用范围</xsl:with-param>
			<xsl:with-param name="lab_not_re_evaluate">此修改只应用于日后的分数计算，但并不应用于以往的分数及其结训条件。</xsl:with-param>
			<xsl:with-param name="lab_re_evaluate">此修改除应用于日后的分数计算外，亦及时应用于以往的分数及其结训记录，并有以下选项：</xsl:with-param>
			<xsl:with-param name="lab_mod_date">原有的结训日期改为更新设定当日</xsl:with-param>
			<xsl:with-param name="lab_message">*比率合计必须为100%。</xsl:with-param>
			<xsl:with-param name="lab_message_mod">*此修改只会影响日后所开班別的预设值，并不会影响已有班別的设定。</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_run_info">
				<xsl:value-of select="$lab_const_run"/> information</xsl:with-param>
			<xsl:with-param name="lab_content">Content</xsl:with-param>
			<xsl:with-param name="lab_title">Title</xsl:with-param>
			<xsl:with-param name="lab_main_eval">Add scoring item</xsl:with-param>
			<xsl:with-param name="lab_main_mod_eval">Edit weight</xsl:with-param>
			<xsl:with-param name="lab_sum">Total</xsl:with-param>
			<xsl:with-param name="lab_score_item">Scoring rule</xsl:with-param>
			<xsl:with-param name="lab_percent">Weight(%)</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_ok">OK</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_message">* Total weight must be equal to 100%.</xsl:with-param>
			<xsl:with-param name="lab_scope">Records affected</xsl:with-param>
			<xsl:with-param name="lab_not_re_evaluate">Change in setting only affects the scores obtained thereafter. The previous scores are not affected.</xsl:with-param>
			<xsl:with-param name="lab_re_evaluate">Change in setting affects the scores obtained thereafter, as well as the scores obtained and the result evaluated previously.</xsl:with-param>
			<xsl:with-param name="lab_mod_date"> Change the completion date to today</xsl:with-param>
			<xsl:with-param name="lab_message_mod">*Changes in scoring item will only affect the classes of this course created thereafter. The corresponding setting of class created in the past remains unchanged.</xsl:with-param>
			<xsl:with-param name="lab_no_eval">No scoring item</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template name="content">
		<xsl:param name="lab_content"/>
		<xsl:param name="lab_main_eval"/>
		<xsl:param name="lab_main_mod_eval"/>
		<xsl:param name="lab_title"/>
		<xsl:param name="lab_score_item"/>
		<xsl:param name="lab_no_eval"/>
		<xsl:param name="lab_g_txt_btn_ok"/>
		<xsl:param name="lab_g_txt_btn_cancel"/>
		<xsl:param name="lab_message"/>
		<xsl:param name="lab_run_info"/>
		<xsl:param name="lab_percent"/>
		<xsl:param name="lab_message_mod"/>
		<xsl:param name="lab_scope"/>
		<xsl:param name="lab_not_re_evaluate"/>
		<xsl:param name="lab_re_evaluate"/>
		<xsl:param name="lab_mod_date"/>
		<xsl:param name="lab_sum"/>

	    <xsl:call-template name="itm_action_nav">
			<xsl:with-param  name="cur_node_id">103</xsl:with-param>
		</xsl:call-template>
	<div class="wzb-item-main">
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module" select="$belong_module"></xsl:with-param>
			<xsl:with-param name="parent_code" select="$parent_code"/>
		</xsl:call-template>
		<xsl:choose>
			<xsl:when test="$flag = '1'">
				<xsl:call-template name="wb_ui_title">
					<xsl:with-param name="text">
						<xsl:value-of select="//itm_action_nav/@itm_title"/>
					</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="wb_ui_title">
					<xsl:with-param name="text">
						<xsl:value-of select="//itm_action_nav/@itm_title"/>
					</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:call-template name="wb_ui_nav_link">
			<xsl:with-param name="text">
				<xsl:choose>
					<xsl:when test="//itm_action_nav/hasTeachingCourse/text()='true'">
						<a href="javascript:itm_lst.get_itm_instr_view({//itm_action_nav/@itm_id})" class="NavLink">
							<xsl:value-of select="//itm_action_nav/@itm_title"/>
						</a>
					</xsl:when>
					<xsl:when test="/CourseCriteria/item/@run_ind = 'false'">
						<a href="javascript:itm_lst.get_item_detail({$itm_id})" class="NavLink">
							<xsl:value-of select="/CourseCriteria/item/title"/>
						</a>
					</xsl:when>
					<xsl:otherwise>
						<xsl:apply-templates select="/CourseCriteria/item/nav/item" mode="nav">
							<xsl:with-param name="lab_run_info" select="$lab_run_info"/>
							<xsl:with-param name="lab_session_info" select="$lab_run_info"/>
						</xsl:apply-templates>
					
					</xsl:otherwise>
				</xsl:choose>
					<span class="NavLink">
						<span class="TitleText">&#160;&gt;&#160;</span>
						<a href="javascript:cmt.get_score_scheme_list({$itm_id})" class="NavLink">
							<xsl:value-of select="$lab_score_item"/>
						</a>
						<xsl:text>&#160;&gt;&#160;</xsl:text>
						<xsl:choose>
							<xsl:when test="$flag = '1'">
								<xsl:value-of select="$lab_main_eval"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$lab_main_mod_eval"/>
							</xsl:otherwise>
						</xsl:choose>
					</span>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:choose>
			<xsl:when test="$mod_cnt = '0' and  $mod_cnt_all='1'">
				<xsl:call-template name="wb_ui_line"/>
				<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
			</xsl:when>
			<xsl:otherwise>
				<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}">
					<tr class="SecBg">
						<td width="50%" align="left">
							<span class="TitleText" style="color:#999999;">
								<xsl:value-of select="$lab_title"/>
							</span>
						</td>
						<td align="right" width="22%">
						</td>
						<td align="left" width="28%">
							<span class="TitleText" style="color:#999999;">
								<xsl:value-of select="$lab_percent"/>
							</span>
						</td>
					</tr>
					<xsl:apply-templates select="marking_scheme_list/item">
						<xsl:with-param name="lab_per" select="$lab_percent"/>
					</xsl:apply-templates>
				</table>
				<xsl:call-template name="wb_ui_line"/>
				<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}">
					<tr>
						<td width="50%">
							<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
						</td>
						<td align="right" width="22%">
							<span class="titletext">
								<xsl:value-of select="$lab_sum"/>：
							</span>
						</td>
						<td align="left" width="28%">
							<span class="text">
								<xsl:choose>
									<xsl:when test="$mod_cnt = '1' and  $mod_cnt_all='1'">
										<input type="text" name="totalscore" disabled="disabled" size="35" style="width:50px;" class="wzb-inputText" value="100"/>
									</xsl:when>
									<xsl:otherwise>
										<input type="text" name="totalscore" disabled="disabled" size="35" style="width:50px;" class="wzb-inputText" value="{number($per_sum)}"/>
									</xsl:otherwise>
								</xsl:choose>
							</span>
						</td>
					</tr>
				</table>
			</xsl:otherwise>
		</xsl:choose>
		<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}">
			<tr>
				<td width="20%" align="right" valign="top">
					<span class="TitleText">
						<xsl:value-of select="$lab_scope"/>：
							</span>
				</td>
				<td>
					<input name="re_evaluate" value="false" type="radio" checked="checked" id="re_evaluate_1" onclick="javascript:upd_date.disabled=true;"/>
				</td>
				<td>
					<span class="titletext">
						<label for="re_evaluate_1">
							<xsl:value-of select="$lab_not_re_evaluate"/>
						</label>
					</span>
				</td>
			</tr>
			<tr>
				<td width="20%" align="right" valign="top">
					<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
				</td>
				<td>
					<input name="re_evaluate" value="true" type="radio" id="re_evaluate" onclick="javascript:upd_date.disabled=false;"/>
				</td>
				<td>
					<span class="titletext">
						<label for="re_evaluate">
							<xsl:value-of select="$lab_re_evaluate"/>
						</label>
					</span>
				</td>
			</tr>
			<tr>
				<td width="20%" align="right" valign="top">
					<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
				</td>
				<td width="3%" align="center" valign="top">
					<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
				</td>
				<td>
					<input name="upd_date" type="checkbox" id="upd_date" disabled="disabled"/>
					<span class="titletext">
						<label for="upd_date">
							<xsl:value-of select="$lab_mod_date"/>
						</label>
					</span>
				</td>
			</tr>
		</table>
		<div class="margin-top10"></div>
		<xsl:call-template name="wb_ui_line"/>
		<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}">
			<tr>
				<td height="19" align="center">
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_ok"/>
						<xsl:with-param name="wb_gen_btn_href">Javascript:cmt.add_new_cmt_final(frmXml,'<xsl:value-of select="$ccr_id"/>','<xsl:value-of select="$itm_id"/>','<xsl:value-of select="$wb_lang"/>','<xsl:value-of select="$mod_cnt"/>');</xsl:with-param>
					</xsl:call-template>
					<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_cancel"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:history.back();</xsl:with-param>
					</xsl:call-template>
				</td>
			</tr>
		</table>
		<xsl:call-template name="wb_ui_footer"/>
	</div>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template match="item">
		<xsl:param name="lab_per"/>
		<xsl:variable name="percent" select="cmt_contri_rate"/>
		<xsl:variable name="upd_timestamp" select="cmt_update_timestamp"/>
		<xsl:variable name="row_class">
			<xsl:choose>
				<xsl:when test="position() mod 2">RowsOdd</xsl:when>
				<xsl:otherwise>RowsEven</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<tr class="{$row_class}">
			<!--<xsl:attribute name="row_class"><xsl:choose><xsl:when test="position() mod 2">RowsEven</xsl:when><xsl:otherwise>RowsOdd</xsl:otherwise></xsl:choose></xsl:attribute>-->
			<td align="left" width="50%">
				<span class="Text">
					<xsl:value-of select="cmt_title"/>
				</span>
			</td>
			<td width="22%">
			</td>
			<td align="left" width="50%">
				<xsl:choose>
					<xsl:when test="$mod_cnt_all = '1'">
						<input type="text" name="cmt_contri_rate" lang="{$lab_per}" id="{@cmt_id}" size="35" style="width:50px;" class="wzb-inputText" disabled="disabled" value="100"/>
						<input type="hidden" name="upd_timestamp_{@cmt_id}" value="{$upd_timestamp}"/>
					</xsl:when>
					<xsl:otherwise>
						<input type="text" name="cmt_contri_rate" lang="{$lab_per}" id="{@cmt_id}" size="35" style="width:50px;" class="wzb-inputText" value="{number($percent)}" onblur="javascript:changeScore({@cmt_id})" onchange="javascript:changeScore({@cmt_id})"/>
						<input type="hidden" name="upd_timestamp_{@cmt_id}" value="{$upd_timestamp}"/>
					</xsl:otherwise>
				</xsl:choose>
			</td>
		</tr>
	</xsl:template>
	<!-- ========================================================================================= -->
	<!--this template can be reused in aeItem's Moudle to draw nav-link of items which can run-->
	<xsl:template match="item" mode="nav">
		<xsl:param name="lab_run_info"/>
		<xsl:param name="lab_session_info"/>
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
					<xsl:choose>
						<xsl:when test="$itm_exam_ind = 'true'"><xsl:value-of select="$lab_const_exam_manage"/></xsl:when>
						<xsl:otherwise><xsl:value-of select="$lab_const_cls_manage"/></xsl:otherwise>
					</xsl:choose>
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
				<a href="javascript:itm_lst.get_item_detail({@id})" class="NavLink">
					<xsl:value-of select="title"/>
				</a>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!--===============================================-->
</xsl:stylesheet>
