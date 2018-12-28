<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="delete_user_report.xsl"/>
	<!-- =============================================================== -->
	<xsl:variable name="cur_lan" select="/Upload/instr/@cur_lang"/>
	<xsl:variable name="isTcIndependent" select="/Upload/isTcIndependent/text()"/>
	<xsl:variable name="instr_type" select="/Upload/instr/@type"/>
	<xsl:variable name="mod_type" select="/Upload/instr/@mod_type"/>
	<xsl:variable name="psd_length" select="/Upload/field_max_length/field[@name='password']"/>
	<xsl:variable name="usr_id_length" select="/Upload/field_max_length/field[@name='user_id']"/>
	<xsl:variable name="psd_min_length" select="/Upload/field_min_length/field[@name='password']"/>
	<xsl:variable name="usr_id_min_length" select="/Upload/field_min_length/field[@name='user_id']"/>
	<!-- =============================================================== -->
	<xsl:template match="Upload">
		<html>
			<xsl:call-template name="main"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="main">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<xsl:call-template name="new_css"/>
			<style>
				* {
					font-size: 9pt;
				}
				
			
				.instr_table {
					border-collapse: collapse;
				}
				
				.instr_table td {
					border: 1px solid #999999;
					height: 20px;
				}
				
				.title {
					background: #000000;
					color: #FFFFFF;
				}
				
				.sub_title {
					background: #CCCCCC;
				}
			</style>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
			<br/>
			<xsl:choose>
				<xsl:when test="$instr_type = 'MC'">
					<xsl:call-template name="mc">
						<xsl:with-param name="in_lang">
							<xsl:value-of select="$cur_lan"/>
						</xsl:with-param>
					</xsl:call-template>
				</xsl:when>
				<xsl:when test="$instr_type = 'FB'">
					<xsl:call-template name="fb">
						<xsl:with-param name="in_lang">
							<xsl:value-of select="$cur_lan"/>
						</xsl:with-param>
					</xsl:call-template>
				</xsl:when>
				<xsl:when test="$instr_type = 'MT'">
					<xsl:call-template name="mt">
						<xsl:with-param name="in_lang">
							<xsl:value-of select="$cur_lan"/>
						</xsl:with-param>
					</xsl:call-template>
				</xsl:when>
				<xsl:when test="$instr_type = 'TF'">
					<xsl:call-template name="tf">
						<xsl:with-param name="in_lang">
							<xsl:value-of select="$cur_lan"/>
						</xsl:with-param>
					</xsl:call-template>
				</xsl:when>
				<xsl:when test="$instr_type = 'ES'">
					<xsl:call-template name="es">
						<xsl:with-param name="in_lang">
							<xsl:value-of select="$cur_lan"/>
						</xsl:with-param>
					</xsl:call-template>
				</xsl:when>
				<xsl:when test="$instr_type = 'FSC'">
					<xsl:call-template name="fsc">
						<xsl:with-param name="in_lang">
							<xsl:value-of select="$cur_lan"/>
						</xsl:with-param>
					</xsl:call-template>
				</xsl:when>
				<xsl:when test="$instr_type = 'DSC'">
					<xsl:call-template name="dsc">
						<xsl:with-param name="in_lang">
							<xsl:value-of select="$cur_lan"/>
						</xsl:with-param>
					</xsl:call-template>
				</xsl:when>
				<xsl:when test="$instr_type = 'enrol'">
					<xsl:call-template name="enrol">
						<xsl:with-param name="in_lang">
							<xsl:value-of select="$cur_lan"/>
						</xsl:with-param>
					</xsl:call-template>
				</xsl:when>
				<xsl:when test="$instr_type = 'user'">
					<xsl:call-template name="user">
						<xsl:with-param name="in_lang">
							<xsl:value-of select="$cur_lan"/>
						</xsl:with-param>
					</xsl:call-template>
				</xsl:when>
				<xsl:when test="$instr_type = 'credit'">
					<xsl:call-template name="credit">
						<xsl:with-param name="in_lang">
							<xsl:value-of select="$cur_lan"/>
						</xsl:with-param>
					</xsl:call-template>
				</xsl:when>
				<xsl:when test="$instr_type = 'CES'">
					<xsl:call-template name="ces">
						<xsl:with-param name="in_lang">
							<xsl:value-of select="$cur_lan"/>
						</xsl:with-param>
					</xsl:call-template>
				</xsl:when>
				<xsl:when test="$instr_type = 'CMC'">
					<xsl:call-template name="cmc">
						<xsl:with-param name="in_lang">
							<xsl:value-of select="$cur_lan"/>
						</xsl:with-param>
					</xsl:call-template>
				</xsl:when>
				
			</xsl:choose>
			<br/>
		</body>
	</xsl:template>
	<!-- template cmc  =============================================================== -->
	<xsl:template name="cmc">
		<xsl:param name="in_lang"/>
		<table class="instr_table" width="95%" align="center">
			<xsl:choose>
				<xsl:when test="$in_lang = 'zh-cn'">
					<xsl:call-template name="que">
						<xsl:with-param name="in_lang">
							<xsl:value-of select="$cur_lan"/>
						</xsl:with-param>
						<xsl:with-param name="field">type_desc</xsl:with-param>
						<xsl:with-param name="title">导入选择题－说明</xsl:with-param>
						<xsl:with-param name="type">选择题</xsl:with-param>
					</xsl:call-template>
				</xsl:when>
				<xsl:when test="$in_lang = 'zh-hk'">
					<xsl:call-template name="que">
						<xsl:with-param name="in_lang">
							<xsl:value-of select="$cur_lan"/>
						</xsl:with-param>
						<xsl:with-param name="field">type_desc</xsl:with-param>
						<xsl:with-param name="title">匯入選擇題－說明</xsl:with-param>
						<xsl:with-param name="type">選擇題</xsl:with-param>
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="que">
						<xsl:with-param name="in_lang">
							<xsl:value-of select="$cur_lan"/>
						</xsl:with-param>
						<xsl:with-param name="field">type_desc</xsl:with-param>
						<xsl:with-param name="title">Instruction for importing multiple choice questions</xsl:with-param>
						<xsl:with-param name="type">Multiple choice</xsl:with-param>
					</xsl:call-template>
				</xsl:otherwise>
			</xsl:choose>
		</table>
		<br/>
		<table width="95%" align="center" class="instr_table">
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">field_header</xsl:with-param>
			</xsl:call-template>
			
			
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">title</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">question</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">asmulc</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">option_1_10</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">score_1_10</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">description</xsl:with-param>
			</xsl:call-template>
		</table>
	</xsl:template>
	<xsl:template name="mc">
		<xsl:param name="in_lang"/>
		<table class="instr_table" width="95%" align="center">
			<xsl:choose>
				<xsl:when test="$in_lang = 'zh-cn'">
					<xsl:call-template name="que">
						<xsl:with-param name="in_lang">
							<xsl:value-of select="$cur_lan"/>
						</xsl:with-param>
						<xsl:with-param name="field">type_desc</xsl:with-param>
						<xsl:with-param name="title">导入选择题－说明</xsl:with-param>
						<xsl:with-param name="type">选择题</xsl:with-param>
					</xsl:call-template>
				</xsl:when>
				<xsl:when test="$in_lang = 'zh-hk'">
					<xsl:call-template name="que">
						<xsl:with-param name="in_lang">
							<xsl:value-of select="$cur_lan"/>
						</xsl:with-param>
						<xsl:with-param name="field">type_desc</xsl:with-param>
						<xsl:with-param name="title">匯入選擇題－說明</xsl:with-param>
						<xsl:with-param name="type">選擇題</xsl:with-param>
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="que">
						<xsl:with-param name="in_lang">
							<xsl:value-of select="$cur_lan"/>
						</xsl:with-param>
						<xsl:with-param name="field">type_desc</xsl:with-param>
						<xsl:with-param name="title">Instruction for importing multiple choice questions</xsl:with-param>
						<xsl:with-param name="type">Multiple choice</xsl:with-param>
					</xsl:call-template>
				</xsl:otherwise>
			</xsl:choose>
		</table>
		<br/>
		<table width="95%" align="center" class="instr_table">
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">field_header</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">folder_id</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">resource_id</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">title</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">question</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">ashtml</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">shuffle</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">answer</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">option_1_10</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">score</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">difficulty</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">status</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">description</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">explanation</xsl:with-param>
			</xsl:call-template>
		</table>
	</xsl:template>
	<!-- =============================================================== -->
	<!-- template fb  =============================================================== -->
	<xsl:template name="fb">
		<xsl:param name="in_lang"/>
		<table class="instr_table" width="95%" align="center">
			<xsl:choose>
				<xsl:when test="$in_lang = 'zh-cn'">
					<xsl:call-template name="que">
						<xsl:with-param name="in_lang">
							<xsl:value-of select="$cur_lan"/>
						</xsl:with-param>
						<xsl:with-param name="field">type_desc</xsl:with-param>
						<xsl:with-param name="title">导入填空题-说明</xsl:with-param>
						<xsl:with-param name="type">填空题</xsl:with-param>
					</xsl:call-template>
				</xsl:when>
				<xsl:when test="$in_lang = 'zh-hk'">
					<xsl:call-template name="que">
						<xsl:with-param name="in_lang">
							<xsl:value-of select="$cur_lan"/>
						</xsl:with-param>
						<xsl:with-param name="field">type_desc</xsl:with-param>
						<xsl:with-param name="title">匯入填空題－說明</xsl:with-param>
						<xsl:with-param name="type">填空題</xsl:with-param>
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="que">
						<xsl:with-param name="in_lang">
							<xsl:value-of select="$cur_lan"/>
						</xsl:with-param>
						<xsl:with-param name="field">type_desc</xsl:with-param>
						<xsl:with-param name="title">Instruction for importing fill in the blank questions</xsl:with-param>
						<xsl:with-param name="type">Fill in the blank</xsl:with-param>
					</xsl:call-template>
				</xsl:otherwise>
			</xsl:choose>
		</table>
		<br/>
		<table width="95%" align="center" class="instr_table">
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">field_header</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">folder_id</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">resource_id</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">title</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">question_for_fb</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">ashtml</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">blank_answer_1_10</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">blank_score_1_10</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">difficulty</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">status</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">description</xsl:with-param>
			</xsl:call-template>		
		</table>
		<br />
		<table width="95%" align="center">
			<tr>
				<td>
					<xsl:choose>
						<xsl:when test="$cur_lan = 'zh-cn'">* 当“题目”定义了“空格”，对应的此字段为必填</xsl:when>
						<xsl:when test="$cur_lan = 'zh-hk'">* 當“題目”定義了“空格”，對應的此欄位爲必填</xsl:when>
						<xsl:otherwise>* Required when the corresponding blank is defined in question</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
		</table>
	</xsl:template>
	<!-- =============================================================== -->
	<!-- template mt  =============================================================== -->
	<xsl:template name="mt">
		<xsl:param name="in_lang"/>
		<table class="instr_table" width="95%" align="center">
			<xsl:choose>
				<xsl:when test="$in_lang = 'zh-cn'">
					<xsl:call-template name="que">
						<xsl:with-param name="in_lang">
							<xsl:value-of select="$cur_lan"/>
						</xsl:with-param>
						<xsl:with-param name="field">type_desc</xsl:with-param>
						<xsl:with-param name="title">导入配对题-说明</xsl:with-param>
						<xsl:with-param name="type">配对题</xsl:with-param>
					</xsl:call-template>
				</xsl:when>
				<xsl:when test="$in_lang = 'zh-hk'">
					<xsl:call-template name="que">
						<xsl:with-param name="in_lang">
							<xsl:value-of select="$cur_lan"/>
						</xsl:with-param>
						<xsl:with-param name="field">type_desc</xsl:with-param>
						<xsl:with-param name="title">匯入配對題－說明</xsl:with-param>
						<xsl:with-param name="type">配對題</xsl:with-param>
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="que">
						<xsl:with-param name="in_lang">
							<xsl:value-of select="$cur_lan"/>
						</xsl:with-param>
						<xsl:with-param name="field">type_desc</xsl:with-param>
						<xsl:with-param name="title">Instruction for importing matching questions</xsl:with-param>
						<xsl:with-param name="type">Matching</xsl:with-param>
					</xsl:call-template>
				</xsl:otherwise>
			</xsl:choose>
		</table>
		<br/>
		<table width="95%" align="center" class="instr_table">
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">field_header</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">folder_id</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">resource_id</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">title</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">question</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">ashtml</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">source_1_10</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">target_1_10</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">answer_for_mt</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">score_for_mt</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">difficulty</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">status</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">description</xsl:with-param>
			</xsl:call-template>
		</table>
	</xsl:template>
	<!-- =============================================================== -->
	<!-- template tf  =============================================================== -->
	<xsl:template name="tf">
		<xsl:param name="in_lang"/>
		<table class="instr_table" width="95%" align="center">
			<xsl:choose>
				<xsl:when test="$in_lang = 'zh-cn'">
					<xsl:call-template name="que">
						<xsl:with-param name="in_lang">
							<xsl:value-of select="$cur_lan"/>
						</xsl:with-param>
						<xsl:with-param name="field">type_desc</xsl:with-param>
						<xsl:with-param name="title">导入判断题-说明</xsl:with-param>
						<xsl:with-param name="type">判断题</xsl:with-param>
					</xsl:call-template>
				</xsl:when>
				<xsl:when test="$in_lang = 'zh-hk'">
					<xsl:call-template name="que">
						<xsl:with-param name="in_lang">
							<xsl:value-of select="$cur_lan"/>
						</xsl:with-param>
						<xsl:with-param name="field">type_desc</xsl:with-param>
						<xsl:with-param name="title">匯入是非題－說明</xsl:with-param>
						<xsl:with-param name="type">是非題</xsl:with-param>
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="que">
						<xsl:with-param name="in_lang">
							<xsl:value-of select="$cur_lan"/>
						</xsl:with-param>
						<xsl:with-param name="field">type_desc</xsl:with-param>
						<xsl:with-param name="title">Instruction for importing true or false questions</xsl:with-param>
						<xsl:with-param name="type">True or false</xsl:with-param>
					</xsl:call-template>
				</xsl:otherwise>
			</xsl:choose>
		</table>
		<br/>
		<table width="95%" align="center" class="instr_table">
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">field_header</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">folder_id</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">resource_id</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">title</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">question</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">ashtml</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">answer_for_tf</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">score</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">difficulty</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">status</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">description</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">explanation</xsl:with-param>
			</xsl:call-template>
		</table>
	</xsl:template>
	<!-- =============================================================== -->
	<!-- template es  =============================================================== -->
	<xsl:template name="es">
		<xsl:param name="in_lang"/>
		<table class="instr_table" width="95%" align="center">
			<xsl:choose>
				<xsl:when test="$in_lang = 'zh-cn'">
					<xsl:call-template name="que">
						<xsl:with-param name="in_lang">
							<xsl:value-of select="$cur_lan"/>
						</xsl:with-param>
						<xsl:with-param name="field">type_desc</xsl:with-param>
						<xsl:with-param name="title">导入文章-说明</xsl:with-param>
						<xsl:with-param name="type">文章</xsl:with-param>
					</xsl:call-template>
				</xsl:when>
				<xsl:when test="$in_lang = 'zh-hk'">
					<xsl:call-template name="que">
						<xsl:with-param name="in_lang">
							<xsl:value-of select="$cur_lan"/>
						</xsl:with-param>
						<xsl:with-param name="field">type_desc</xsl:with-param>
						<xsl:with-param name="title">匯入文章－說明</xsl:with-param>
						<xsl:with-param name="type">文章</xsl:with-param>
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="que">
						<xsl:with-param name="in_lang">
							<xsl:value-of select="$cur_lan"/>
						</xsl:with-param>
						<xsl:with-param name="field">type_desc</xsl:with-param>
						<xsl:with-param name="title">Instruction for importing essay questions</xsl:with-param>
						<xsl:with-param name="type">Essay</xsl:with-param>
					</xsl:call-template>
				</xsl:otherwise>
			</xsl:choose>
		</table>
		<br/>
		<table width="95%" align="center" class="instr_table">
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">field_header</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">folder_id</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">resource_id</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">title</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">question</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">ashtml</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">model_answer</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">score</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">difficulty</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">status</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">description</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">submit_file</xsl:with-param>
			</xsl:call-template>
		</table>
	</xsl:template>
	<!-- =============================================================== -->
	<!-- template ces  =============================================================== -->
	<xsl:template name="ces">
		<xsl:param name="in_lang"/>
		<table class="instr_table" width="95%" align="center">
			<xsl:choose>
				<xsl:when test="$in_lang = 'zh-cn'">
					<xsl:call-template name="que">
						<xsl:with-param name="in_lang">
							<xsl:value-of select="$cur_lan"/>
						</xsl:with-param>
						<xsl:with-param name="field">type_desc</xsl:with-param>
						<xsl:with-param name="title">导入问答题-说明</xsl:with-param>
						<xsl:with-param name="type">问答题</xsl:with-param>
					</xsl:call-template>
				</xsl:when>
				<xsl:when test="$in_lang = 'zh-hk'">
					<xsl:call-template name="que">
						<xsl:with-param name="in_lang">
							<xsl:value-of select="$cur_lan"/>
						</xsl:with-param>
						<xsl:with-param name="field">type_desc</xsl:with-param>
						<xsl:with-param name="title">匯入問答題－說明</xsl:with-param>
						<xsl:with-param name="type">問答題</xsl:with-param>
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="que">
						<xsl:with-param name="in_lang">
							<xsl:value-of select="$cur_lan"/>
						</xsl:with-param>
						<xsl:with-param name="field">type_desc</xsl:with-param>
						<xsl:with-param name="title">Instruction for importing essay questions</xsl:with-param>
						<xsl:with-param name="type">Essay</xsl:with-param>
					</xsl:call-template>
				</xsl:otherwise>
			</xsl:choose>
		</table>
		<br/>
		<table width="95%" align="center" class="instr_table">
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">field_header</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">title</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">question</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">description</xsl:with-param>
			</xsl:call-template>
		</table>
	</xsl:template>
	
	<!-- =============================================================== -->
	<!-- template fsc  =============================================================== -->
	<xsl:template name="fsc">
		<xsl:param name="in_lang"/>
		<table class="instr_table" width="95%" align="center">
			<xsl:choose>
				<xsl:when test="$in_lang = 'zh-cn'">
					<xsl:call-template name="que">
						<xsl:with-param name="in_lang">
							<xsl:value-of select="$cur_lan"/>
						</xsl:with-param>
						<xsl:with-param name="field">type_desc_for_sc</xsl:with-param>
						<xsl:with-param name="title">导入静态情景题-说明</xsl:with-param>
						<xsl:with-param name="type">静态情景题</xsl:with-param>
					</xsl:call-template>
				</xsl:when>
				<xsl:when test="$in_lang = 'zh-hk'">
					<xsl:call-template name="que">
						<xsl:with-param name="in_lang">
							<xsl:value-of select="$cur_lan"/>
						</xsl:with-param>
						<xsl:with-param name="field">type_desc_for_sc</xsl:with-param>
						<xsl:with-param name="title">匯入靜態情景題－說明</xsl:with-param>
						<xsl:with-param name="type">靜態情景題</xsl:with-param>
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="que">
						<xsl:with-param name="in_lang">
							<xsl:value-of select="$cur_lan"/>
						</xsl:with-param>
						<xsl:with-param name="field">type_desc_for_sc</xsl:with-param>
						<xsl:with-param name="title">Instruction for importing fixed scenario questions</xsl:with-param>
						<xsl:with-param name="type">Fixed scenario</xsl:with-param>
					</xsl:call-template>
				</xsl:otherwise>
			</xsl:choose>
		</table>
		<br/>
		<table width="95%" align="center" class="instr_table">
			<xsl:choose>
				<xsl:when test="$in_lang = 'zh-cn'">
					<xsl:call-template name="que">
						<xsl:with-param name="in_lang">
							<xsl:value-of select="$cur_lan"/>
						</xsl:with-param>
						<xsl:with-param name="field">field_header</xsl:with-param>
						<xsl:with-param name="sub_title">（主题目）</xsl:with-param>
					</xsl:call-template>
				</xsl:when>
				<xsl:when test="$in_lang = 'zh-hk'">
					<xsl:call-template name="que">
						<xsl:with-param name="in_lang">
							<xsl:value-of select="$cur_lan"/>
						</xsl:with-param>
						<xsl:with-param name="field">field_header</xsl:with-param>
						<xsl:with-param name="sub_title">（主題目）</xsl:with-param>
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="que">
						<xsl:with-param name="in_lang">
							<xsl:value-of select="$cur_lan"/>
						</xsl:with-param>
						<xsl:with-param name="field">field_header</xsl:with-param>
						<xsl:with-param name="sub_title">(for scenario question)</xsl:with-param>
					</xsl:call-template>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">folder_id</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">resource_id</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">title</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">question</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">ashtml</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">difficulty</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">status</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">description</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">option_1_10_for_sc_not_fill</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">shuffle_option_for_sc_not_fill</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">answer_for_sc_not_fill</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">score_for_sc_not_fill</xsl:with-param>
			</xsl:call-template>
		</table>
		<br/>
		<table width="95%" align="center" class="instr_table">
			<xsl:choose>
				<xsl:when test="$in_lang = 'zh-cn'">
					<xsl:call-template name="que">
						<xsl:with-param name="in_lang">
							<xsl:value-of select="$cur_lan"/>
						</xsl:with-param>
						<xsl:with-param name="field">field_header</xsl:with-param>
						<xsl:with-param name="sub_title">（主题目下的子题目）</xsl:with-param>
					</xsl:call-template>
				</xsl:when>
				<xsl:when test="$in_lang = 'zh-hk'">
					<xsl:call-template name="que">
						<xsl:with-param name="in_lang">
							<xsl:value-of select="$cur_lan"/>
						</xsl:with-param>
						<xsl:with-param name="field">field_header</xsl:with-param>
						<xsl:with-param name="sub_title">（主題目下的子題目）</xsl:with-param>
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="que">
						<xsl:with-param name="in_lang">
							<xsl:value-of select="$cur_lan"/>
						</xsl:with-param>
						<xsl:with-param name="field">field_header</xsl:with-param>
						<xsl:with-param name="sub_title">(for sub-question of a scenario question)</xsl:with-param>
					</xsl:call-template>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">folder_id_for_sc_not_fill</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">resource_id</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">title</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">question</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">ashtml_with_option</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">shuffle_for_sc_not_fill</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">difficulty_for_sc_not_fill</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">status_for_sc_not_fill</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">description</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">option_1_10</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">shuffle_option_for_sc</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">answer</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">score</xsl:with-param>
			</xsl:call-template>
		</table>
	</xsl:template>
	<!-- =============================================================== -->
	<!-- template dsc  =============================================================== -->
	<xsl:template name="dsc">
		<xsl:param name="in_lang"/>
		<table class="instr_table" width="95%" align="center">
			<xsl:choose>
				<xsl:when test="$in_lang = 'zh-cn'">
					<xsl:call-template name="que">
						<xsl:with-param name="in_lang">
							<xsl:value-of select="$cur_lan"/>
						</xsl:with-param>
						<xsl:with-param name="field">type_desc_for_sc</xsl:with-param>
						<xsl:with-param name="title">导入动态情景题-说明</xsl:with-param>
						<xsl:with-param name="type">动态情景题</xsl:with-param>
					</xsl:call-template>
				</xsl:when>
				<xsl:when test="$in_lang = 'zh-hk'">
					<xsl:call-template name="que">
						<xsl:with-param name="in_lang">
							<xsl:value-of select="$cur_lan"/>
						</xsl:with-param>
						<xsl:with-param name="field">type_desc_for_sc</xsl:with-param>
						<xsl:with-param name="title">匯入動態情景題－說明</xsl:with-param>
						<xsl:with-param name="type">動態情景題</xsl:with-param>
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="que">
						<xsl:with-param name="in_lang">
							<xsl:value-of select="$cur_lan"/>
						</xsl:with-param>
						<xsl:with-param name="field">type_desc_for_sc</xsl:with-param>
						<xsl:with-param name="title">Instruction for importing dynamic scenario questions</xsl:with-param>
						<xsl:with-param name="type">Dynamic scenario</xsl:with-param>
					</xsl:call-template>
				</xsl:otherwise>
			</xsl:choose>
		</table>
		<br/>
		<table width="95%" align="center" class="instr_table">
			<xsl:choose>
				<xsl:when test="$in_lang = 'zh-cn'">
					<xsl:call-template name="que">
						<xsl:with-param name="in_lang">
							<xsl:value-of select="$cur_lan"/>
						</xsl:with-param>
						<xsl:with-param name="field">field_header</xsl:with-param>
						<xsl:with-param name="sub_title">（主题目）</xsl:with-param>
					</xsl:call-template>
				</xsl:when>
				<xsl:when test="$in_lang = 'zh-hk'">
					<xsl:call-template name="que">
						<xsl:with-param name="in_lang">
							<xsl:value-of select="$cur_lan"/>
						</xsl:with-param>
						<xsl:with-param name="field">field_header</xsl:with-param>
						<xsl:with-param name="sub_title">（主題目）</xsl:with-param>
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="que">
						<xsl:with-param name="in_lang">
							<xsl:value-of select="$cur_lan"/>
						</xsl:with-param>
						<xsl:with-param name="field">field_header</xsl:with-param>
						<xsl:with-param name="sub_title">(for scenario question)</xsl:with-param>
					</xsl:call-template>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">folder_id</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">resource_id</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">title</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">question</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">ashtml</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">difficulty</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">status</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">criteria</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">description</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">option_1_10_for_sc_not_fill</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">shuffle_option_for_sc_not_fill</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">answer_for_sc_not_fill</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">score_for_sc_not_fill</xsl:with-param>
			</xsl:call-template>
		</table>
		<br/>
		<table width="95%" align="center" class="instr_table">
			<xsl:choose>
				<xsl:when test="$in_lang = 'zh-cn'">
					<xsl:call-template name="que">
						<xsl:with-param name="in_lang">
							<xsl:value-of select="$cur_lan"/>
						</xsl:with-param>
						<xsl:with-param name="field">field_header</xsl:with-param>
						<xsl:with-param name="sub_title">（主题目下的子题目）</xsl:with-param>
					</xsl:call-template>
				</xsl:when>
				<xsl:when test="$in_lang = 'zh-hk'">
					<xsl:call-template name="que">
						<xsl:with-param name="in_lang">
							<xsl:value-of select="$cur_lan"/>
						</xsl:with-param>
						<xsl:with-param name="field">field_header</xsl:with-param>
						<xsl:with-param name="sub_title">（主題目下的子題目）</xsl:with-param>
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="que">
						<xsl:with-param name="in_lang">
							<xsl:value-of select="$cur_lan"/>
						</xsl:with-param>
						<xsl:with-param name="field">field_header</xsl:with-param>
						<xsl:with-param name="sub_title">(for sub-question of a scenario question)</xsl:with-param>
					</xsl:call-template>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">folder_id_for_sc_not_fill</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">resource_id</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">title</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">question</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">ashtml_with_option</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">difficulty_for_sc_not_fill</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">status_for_sc_not_fill</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">criteria_for_sc_not_fill</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">description</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">option_1_10</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">shuffle_option_for_sc</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">answer_for_sc</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="que">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">score</xsl:with-param>
			</xsl:call-template>
		</table>
	</xsl:template>
	<!-- =============================================================== -->
	<!-- que template =============================================================== -->
	<xsl:template name="que">
		<xsl:param name="in_lang"/>
		<xsl:param name="field"/>
		<xsl:param name="type"/>
		<xsl:param name="title"/>
		<xsl:param name="sub_title"/>
		<xsl:choose>
			<!-- type desc -->
			<xsl:when test="$field = 'type_desc'">
				<tr>
					<td class="title" width="100%" colspan="2" nowrap="nowrap">
						<xsl:value-of select="$title"/>
					</td>
				</tr>
				<xsl:choose>
					<xsl:when test="$in_lang = 'zh-cn'">
						<tr>
							<td align="center" nowrap="nowrap" width="100px">1</td>
							<td>根据文件模板填写信息，每一行代表一道题目。每个字段的说明请看下面的“<b>字段说明</b>”。注：不支持导入包含媒体文档的题目。</td>
						</tr>
						<tr>
							<td align="center" nowrap="nowrap">2</td>
							<td>在文件的工作表中，从菜单栏选择“文件 > 另存为...”。</td>
						</tr>
						<tr>
							<td align="center" nowrap="nowrap">3</td>
							<td>在“<b>保存类型</b>”的下拉表中，选择“<b>Unicode 文本 (*.txt)</b>”，然后点击“保存”。之后会显示两个对话框，在第一个点击“确定”，在第二个点击“是”。<b>注意文件必须是此类型才能被正确导入。</b>
							</td>
						</tr>
						<tr>
							<td align="center" nowrap="nowrap">4</td>
							<td>（可忽略）使用“记事本”等文本工具打开刚才另存出来的文件，检查是否包含需要处理的信息。检查后不要修改任何内容并退出该文本工具。</td>
						</tr>
						<tr>
							<td align="center" nowrap="nowrap">5</td>
							<td>登录学习平台并选择“<b>资源管理</b> &gt; <b>导入题目资源</b>”。只有具有特定管理员权限的用户才有该功能。</td>
						</tr>
						<tr>
							<td align="center" nowrap="nowrap">6</td>
							<td>在“<b>题目类型</b>”选择“<b>
									<xsl:value-of select="$type"/>
								</b>”，然后使用“<b>文件位置</b>”右侧的“<b>浏览</b>...”按钮，指定刚才另存出来的文件。</td>
						</tr>
						<tr>
							<td align="center" nowrap="nowrap">7</td>
							<td>根据学习平台上的提示去完成操作。</td>
						</tr>
					</xsl:when>
					<xsl:when test="$in_lang = 'zh-hk'">
						<tr>
							<td align="center" nowrap="nowrap" width="100px">1</td>
							<td>根據文檔範本填寫資訊，每一行代表一道題目。每個欄位的說明請看下面的“<b>欄位說明</b>”。注：不支援匯入包含媒體檔案的題目。</td>
						</tr>
						<tr>
							<td align="center" nowrap="nowrap">2</td>
							<td>在文檔的工作表中，從功能表欄選擇“文件 > 另存為...”。</td>
						</tr>
						<tr>
							<td align="center" nowrap="nowrap">3</td>
							<td>在“<b>保存類型</b>”的下拉表中，選擇“<b>Unicode 文本 (*.txt)</b>”，然後點擊“保存”。之後會顯示兩個對話方塊，在第一個點擊“確定”，在第二個點擊“是”。注意文件必須是此類型才能被正確匯入。</td>
						</tr>
						<tr>
							<td align="center" nowrap="nowrap">4</td>
							<td>（可忽略）使用“記事本”等文本工具打開剛才另存出來的文件，檢查是否包含需要處理的資訊。檢查後不要修改任何內容並退出該文本工具。</td>
						</tr>
						<tr>
							<td align="center" nowrap="nowrap">5</td>
							<td>登錄學習平台並選擇“<b>教學資源管理</b> &gt; <b>匯入題目資源</b>”。只有具有特定管理員許可權的用戶才有該功能。</td>
						</tr>
						<tr>
							<td align="center" nowrap="nowrap">6</td>
							<td>在“<b>題目類型</b>”選擇“<b><xsl:value-of select="$type"/></b>”，然後使用“檔案位置”右側的“<b>瀏覽</b>...”按鈕，指定剛才另存出來的文件。</td>
						</tr>
						<tr>
							<td align="center" nowrap="nowrap">7</td>
							<td>根據學習平台上的提示去完成操作。</td>
						</tr>
					</xsl:when>
					<xsl:otherwise>
						<tr>
							<td align="center" nowrap="nowrap" width="100px">1</td>
							<td>Fill in the data according to the template. Each row represents the data of a single question. See the <b>Field description</b> below for full description of the fields. (Note: Uploading questions with media file is not supported).</td>
						</tr>
						<tr>
							<td align="center" nowrap="nowrap">2</td>
							<td>While the data worksheet is still active, select File &gt; Save as in the menu.</td>
						</tr>
						<tr>
							<td align="center" nowrap="nowrap">3</td>
							<td>In the <b>Save as type</b> box, select <b>unicode text (*.txt)</b> and save the file. You will be asked to confirm twice. Click Yes in both cases. Note that the input file format must be unicode text (*.txt) and not text (*.txt).</td>
						</tr>
						<tr>
							<td align="center" nowrap="nowrap">4</td>
							<td>(Optional) Open the file using a text editor (e.g. Notepad) to verify the file contains the actual data. Do not change the data and exit the editor after your verification.</td>
						</tr>
						<tr>
							<td align="center" nowrap="nowrap">5</td>
							<td>Login to the learning platform and go to <b>Learning Resource Management</b> &gt; <b>Import question</b>. This function is available only to users with specific administrative user role.</td>
						</tr>
						<tr>
							<td align="center" nowrap="nowrap">6</td>
							<td>Specify <b><xsl:value-of select="$type"/></b> in <b>question type</b> and upload the saved file using the <b>Browse</b>... button to the right of file location.</td>
						</tr>
						<tr>
							<td align="center" nowrap="nowrap">7</td>
							<td>There are instructions in the learning platform to guide you through the process.</td>
						</tr>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:when test="$field = 'type_desc_for_sc'">
				<tr>
					<td class="title" width="100%" colspan="2" nowrap="nowrap">
						<xsl:value-of select="$title"/>
					</td>
				</tr>
				<xsl:choose>
					<xsl:when test="$in_lang = 'zh-cn'">
						<tr>
							<td align="center" nowrap="nowrap" width="10%">1</td>
							<td>根据文件模板填写信息，每一行代表一道情景题的主题目或者其子题目。每个字段的说明请看下面的“<b>字段说明</b>”。注：不支持导入包含媒体文档的题目。</td>
						</tr>
						<tr>
							<td align="center" nowrap="nowrap">2</td>
							<td>填写情景题的子题目时，把子题目的信息直接填写在主题目的下一行，如果有多于一个子题目，请连续的在主题目下一行一个子题目的填写，但需要把子题目的“<b>文件夹编号</b>”留空。</td>
						</tr>
						<tr>
							<td align="center" nowrap="nowrap">3</td>
							<td>登录学习平台并选择“<b>资源管理</b> &gt; <b>导入题目资源</b>”。只有具有特定管理员权限的用户才有该功能。</td>
						</tr>
						<tr>
							<td align="center" nowrap="nowrap">4</td>
							<td>在“<b>题目类型</b>”选择“<b>
									<xsl:value-of select="$type"/>
								</b>”，然后使用“文件位置”右侧的“<b>浏览</b>...”按钮，指定刚才另存出来的文件。</td>
						</tr>
						<tr>
							<td align="center" nowrap="nowrap">5</td>
							<td>根据学习平台上的提示去完成操作。</td>
						</tr>
					</xsl:when>
					<xsl:when test="$in_lang = 'zh-hk'">
						<tr>
							<td align="center" nowrap="nowrap" width="100px">1</td>
							<td>根據文檔範本填寫資訊，每一行代表一道情景題的主題目或者其子題目。每個欄位的說明請看下面的“<b>欄位說明</b>”。注：不支援導入包含媒體文檔的題目。</td>
						</tr>
						<tr>
							<td align="center" nowrap="nowrap">2</td>
							<td>填寫情景題的子題目時，把子題目的資訊直接填寫在主題目的下一行，如果有多於一個子題目，請連續的在主題目下一行一個子題目的填寫，但需要把子題目的“<b>文件夾編號</b>”留空。</td>
						</tr>
						<tr>
							<td align="center" nowrap="nowrap">3</td>
							<td>登錄學習平台並選擇“<b>教學資源管理</b> &gt; <b>匯入題目資源</b>”。只有具有特定管理員許可權的用戶才有該功能。</td>
						</tr>
						<tr>
							<td align="center" nowrap="nowrap">4</td>
							<td>在“<b>題目類型</b>”選擇“<b>
									<xsl:value-of select="$type"/>
								</b>”，然後使用“檔案位置”右側的“<b>瀏覽</b>...”按鈕，指定剛才另存出來的文件。</td>
						</tr>
						<tr>
							<td align="center" nowrap="nowrap">5</td>
							<td>根據學習平台上的提示去完成操作。</td>
						</tr>
					</xsl:when>
					<xsl:otherwise>
						<tr>
							<td align="center" nowrap="nowrap" width="100px">1</td>
							<td>Fill in the data according to the template. Each row represents the data of a single question (for either a scenario question or its sub-question). See the <b>Field description</b> below for full description of the fields. (Note: Uploading questions with media file is not supported)</td>
						</tr>
						<tr>
							<td align="center" nowrap="nowrap">2</td>
							<td>For sub-questions of a scenario question, put them right below the scenario question and leave <b>Folder ID</b> of that sub-question blank.</td>
						</tr>
						<tr>
							<td align="center" nowrap="nowrap">3</td>
							<td>Login to the learning platform and go to <b>Learning Resource Management</b> &gt; <b>Import question</b>. This function is available only to users with specific administrative user role.</td>
						</tr>
						<tr>
							<td align="center" nowrap="nowrap">4</td>
							<td>Specify <b>
									<xsl:value-of select="$type"/>
								</b> in <b>Question type</b> and upload the saved file using the <b>Browse</b>... button to the right of File Location.</td>
						</tr>
						<tr>
							<td align="center" nowrap="nowrap">5</td>
							<td>There are instructions in the learning platform to guide you through the process.</td>
						</tr>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<!-- type desc -->
			<!-- Field Header -->
			<xsl:when test="$field = 'field_header'">
				<xsl:choose>
					<xsl:when test="$in_lang = 'zh-cn'">
						<tr>
							<td class="title" colspan="5" width="100%">字段说明<xsl:value-of select="$sub_title"/>
							</td>
						</tr>
						<tr>
							<td class="sub_title" nowrap="nowrap" width="60">字段名</td>
							<td class="sub_title" nowrap="nowrap" width="60">必填</td>
							<td class="sub_title" nowrap="nowrap" width="60">长度限制</td>
							<td class="sub_title" nowrap="nowrap" width="60">格式</td>
							<td class="sub_title" nowrap="nowrap">备注</td>
						</tr>
					</xsl:when>
					<xsl:when test="$in_lang = 'zh-hk'">
						<tr>
							<td class="title" colspan="5" width="100%">欄位說明<xsl:value-of select="$sub_title"/>
							</td>
						</tr>
						<tr>
							<td class="sub_title" nowrap="nowrap" width="60">欄位名</td>
							<td class="sub_title" nowrap="nowrap" width="60">必填</td>
							<td class="sub_title" nowrap="nowrap" width="60">長度限制 </td>
							<td class="sub_title" nowrap="nowrap" width="60">格式</td>
							<td class="sub_title" nowrap="nowrap">備註</td>
						</tr>
					</xsl:when>
					<xsl:otherwise>
						<tr>
							<td class="title" colspan="5" width="100%">Field description<xsl:value-of select="$sub_title"/>
							</td>
						</tr>
						<tr>
							<td class="sub_title" nowrap="nowrap" width="60">Field</td>
							<td class="sub_title" nowrap="nowrap" width="60">Reqd</td>
							<td class="sub_title" nowrap="nowrap" width="60">Max size</td>
							<td class="sub_title" nowrap="nowrap" width="60">Format</td>
							<td class="sub_title" nowrap="nowrap">Remarks</td>
						</tr>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<!-- end of  Header -->
			<!-- Folder ID -->
			<xsl:when test="$field = 'folder_id'">
				<xsl:choose>
					<xsl:when test="$in_lang = 'zh-cn'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-cn/field[@name='wb_imp_tem_folder_id']"/>
							</td>
							<td nowrap="nowrap">是</td>
							<td align="right" nowrap="nowrap">无</td>
							<td nowrap="nowrap">整数</td>
							<td>请填写对应的编号（而不是名称）。具体编号请到学习平台的“资源管理”中查看。</td>
						</tr>
					</xsl:when>
					<xsl:when test="$in_lang = 'zh-hk'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-hk/field[@name='wb_imp_tem_folder_id']"/>
							</td>
							<td nowrap="nowrap">是</td>
							<td align="right" nowrap="nowrap">無</td>
							<td nowrap="nowrap">整數</td>
							<td>請填寫對應的編號（而不是名稱）。具體編號請到學習平台的“教學資源管理”中查看。</td>
						</tr>
					</xsl:when>
					<xsl:otherwise>
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/en-us/field[@name='wb_imp_tem_folder_id']"/>
							</td>
							<td nowrap="nowrap">Y</td>
							<td align="right" nowrap="nowrap">N/A</td>
							<td nowrap="nowrap">Integer</td>
							<td>Please use the corresponding ID (not name). Please look up the codes in Learning Resource Management in the learning platform.</td>
						</tr>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:when test="$field = 'folder_id_for_sc_not_fill'">
				<xsl:choose>
					<xsl:when test="$in_lang = 'zh-cn'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-cn/field[@name='wb_imp_tem_folder_id']"/>
							</td>
							<td nowrap="nowrap">是</td>
							<td align="right" nowrap="nowrap">无</td>
							<td nowrap="nowrap">整数</td>
							<td>请保留空白。</td>
						</tr>
					</xsl:when>
					<xsl:when test="$in_lang = 'zh-hk'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-hk/field[@name='wb_imp_tem_folder_id']"/>
							</td>
							<td nowrap="nowrap">是</td>
							<td align="right" nowrap="nowrap">無</td>
							<td nowrap="nowrap">整數</td>
							<td>請保留空白。</td>
						</tr>
					</xsl:when>
					<xsl:otherwise>
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/en-us/field[@name='wb_imp_tem_folder_id']"/>
							</td>
							<td nowrap="nowrap">Y</td>
							<td align="right" nowrap="nowrap">N/A</td>
							<td nowrap="nowrap">Integer</td>
							<td>Do not fill in anything.</td>
						</tr>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<!-- end of Folder ID -->
			<!-- Resource ID -->
			<xsl:when test="$field = 'resource_id'">
				<xsl:choose>
					<xsl:when test="$in_lang = 'zh-cn'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-cn/field[@name='wb_imp_tem_resource_id']"/>
							</td>
							<td nowrap="nowrap">否</td>
							<td align="right" nowrap="nowrap">无</td>
							<td nowrap="nowrap">整数</td>
							<td>添加新题目时：把此字段留空，更新题目时：请填写对应的编号（而不是标题）；并且确保题目类型是正确。具体编号请到学习平台的“资源管理”中查看。</td>
						</tr>
					</xsl:when>
					<xsl:when test="$in_lang = 'zh-hk'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-hk/field[@name='wb_imp_tem_resource_id']"/>
							</td>
							<td nowrap="nowrap">否</td>
							<td align="right" nowrap="nowrap">無</td>
							<td nowrap="nowrap">整數</td>
							<td>添加新題目時：把此欄位留空，更新題目時：請填寫對應的編號（而不是標題）；並且確保題目類型是正確。具體編號請到學習平台的“教學資源管理”中查看。</td>
						</tr>
					</xsl:when>
					<xsl:otherwise>
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/en-us/field[@name='wb_imp_tem_resource_id']"/>
							</td>
							<td nowrap="nowrap">N</td>
							<td align="right" nowrap="nowrap">N/A</td>
							<td nowrap="nowrap">Integer</td>
							<td>Insert new question: leave this field blank, update existing question: please use the corresponding ID (not title). And make sure that question type matches.</td>
						</tr>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<!-- end of Resource ID -->
			<!-- Title -->
			<xsl:when test="$field = 'title'">
				<xsl:choose>
					<xsl:when test="$in_lang = 'zh-cn'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-cn/field[@name='wb_imp_tem_title']"/>
							</td>
							<td nowrap="nowrap">是</td>
							<td align="right" nowrap="nowrap">不能超过40个中文字（80个字符）</td>
							<td nowrap="nowrap">文本</td>
							<td/>
						</tr>
					</xsl:when>
					<xsl:when test="$in_lang = 'zh-hk'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-hk/field[@name='wb_imp_tem_title']"/>
							</td>
							<td nowrap="nowrap">是</td>
							<td align="right" nowrap="nowrap">不能超過40個中文字（80個字元）</td>
							<td nowrap="nowrap">文本</td>
							<td/>
						</tr>
					</xsl:when>
					<xsl:otherwise>
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/en-us/field[@name='wb_imp_tem_title']"/>
							</td>
							<td nowrap="nowrap">Y</td>
							<td align="right" nowrap="nowrap">should not exceed 80 characters</td>
							<td nowrap="nowrap">Text</td>
							<td/>
						</tr>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<!-- end of Title -->
			<!-- Question -->
			<xsl:when test="$field = 'question'">
				<xsl:choose>
					<xsl:when test="$in_lang = 'zh-cn'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-cn/field[@name='wb_imp_tem_question']"/>
							</td>
							<td nowrap="nowrap">是</td>
							<td align="right" nowrap="nowrap">无</td>
							<td nowrap="nowrap">文本</td>
							<td/>
						</tr>
					</xsl:when>
					<xsl:when test="$in_lang = 'zh-hk'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-hk/field[@name='wb_imp_tem_question']"/>
							</td>
							<td nowrap="nowrap">是</td>
							<td align="right" nowrap="nowrap">無</td>
							<td nowrap="nowrap">文本</td>
							<td/>
						</tr>
					</xsl:when>
					<xsl:otherwise>
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/en-us/field[@name='wb_imp_tem_question']"/>
							</td>
							<td nowrap="nowrap">Y</td>
							<td align="right" nowrap="nowrap">Not Restricted</td>
							<td nowrap="nowrap">Text</td>
							<td/>
						</tr>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:when test="$field = 'question_for_fb'">
				<xsl:choose>
					<xsl:when test="$in_lang = 'zh-cn'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-cn/field[@name='wb_imp_tem_question']"/>
							</td>
							<td nowrap="nowrap">是</td>
							<td nowrap="nowrap">无</td>
							<td nowrap="nowrap">文本</td>
							<td>请用[blank1]、[blank2]、[blank10]等标志在题目中表示空格。最多10个空格。<br/>
							            如：题目名称为：wizbank6.2是升级版吗？<br/>
							    需要您填写题目：wizbank6.2是升级版吗？[blank1]<br/>
							    如果多个答案需要留多个空白处：wizbank6.2是升级版吗？[blank1]、[blank2]、[blank3]
							</td>
						</tr>
					</xsl:when>
					<xsl:when test="$in_lang = 'zh-hk'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-hk/field[@name='wb_imp_tem_question']"/>
							</td>
							<td nowrap="nowrap">是</td>
							<td align="right" nowrap="nowrap">無</td>
							<td nowrap="nowrap">文本</td>
							<td>请用[blank1]、[blank2]、[blank10]等标志在题目中表示空格。最多10个空格。<br/>
							            如：题目名称为：wizbank6.2是升级版吗？<br/>
							    需要您填写题目：wizbank6.2是升级版吗？[blank1]<br/>
							    如果多个答案需要留多个空白处：wizbank6.2是升级版吗？[blank1]、[blank2]、[blank3]
							</td>
						</tr>
					</xsl:when>
					<xsl:otherwise>
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/en-us/field[@name='wb_imp_tem_question']"/>
							</td>
							<td nowrap="nowrap">Y</td>
							<td align="right" nowrap="nowrap">Not Restricted</td>
							<td nowrap="nowrap">Text</td>
							<td>Please use [blank1], [blank2]...[blank10] to represent the answers that learner has to fill in.
							</td>
						</tr>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<!-- end of Question -->
			<!-- AS Html -->
			<!--
			<xsl:when test="$field = 'ashtml'">
				<xsl:choose>
					<xsl:when test="$in_lang = 'zh-cn'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-cn/field[@name='wb_imp_tem_as_html']"/>
							</td>
							<td nowrap="nowrap">是</td>
							<td align="right" nowrap="nowrap">1</td>
							<td nowrap="nowrap">文本</td>
							<td>此字段同时适用于题目。请填写对应的代码：<br/>Y－是<br/>N－否</td>
						</tr>
					</xsl:when>
					<xsl:when test="$in_lang = 'zh-hk'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-hk/field[@name='wb_imp_tem_as_html']"/>
							</td>
							<td nowrap="nowrap">是</td>
							<td align="right" nowrap="nowrap">1</td>
							<td nowrap="nowrap">文本</td>
							<td>此欄位同時適用於題目。請填寫對應的代碼：<br/>Y－是<br/>N－否</td>
						</tr>
					</xsl:when>
					<xsl:otherwise>
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/en-us/field[@name='wb_imp_tem_as_html']"/>
							</td>
							<td nowrap="nowrap">Y</td>
							<td align="right" nowrap="nowrap">1</td>
							<td nowrap="nowrap">Text</td>
							<td>This applies to the question. Please use the codes below:<br/>Y - Yes<br/>N - No</td>
						</tr>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:when test="$field = 'ashtml_with_option'">
				<xsl:choose>
					<xsl:when test="$in_lang = 'zh-cn'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-cn/field[@name='wb_imp_tem_as_html']"/>
							</td>
							<td nowrap="nowrap">是</td>
							<td align="right" nowrap="nowrap">1</td>
							<td nowrap="nowrap">文本</td>
							<td>此字段同时适用于题目与选项。请填写对应的代码：<br/>Y－是<br/>N－否</td>
						</tr>
					</xsl:when>
					<xsl:when test="$in_lang = 'zh-hk'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-hk/field[@name='wb_imp_tem_as_html']"/>
							</td>
							<td nowrap="nowrap">是</td>
							<td align="right" nowrap="nowrap">1</td>
							<td nowrap="nowrap">文本</td>
							<td>此欄位同時適用於題目與選項。請填寫對應的代碼：<br/>Y－是<br/>N－否</td>
						</tr>
					</xsl:when>
					<xsl:otherwise>
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/en-us/field[@name='wb_imp_tem_as_html']"/>
							</td>
							<td nowrap="nowrap">Y</td>
							<td align="right" nowrap="nowrap">1</td>
							<td nowrap="nowrap">Text</td>
							<td>This applies to the question and the option fields. Please use the codes below:<br/>Y - Yes<br/>N - No</td>
						</tr>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
		     -->
			<!-- end of  AS Html -->
			<!-- As Multiple Choice -->
			<xsl:when test="$field = 'asmulc'">
				<xsl:choose>
					<xsl:when test="$in_lang = 'zh-cn'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-cn/field[@name='wb_imp_tem_as_mulc']"/>
							</td>
							<td nowrap="nowrap">是</td>
							<td align="right" nowrap="nowrap">1</td>
							<td nowrap="nowrap">文本</td>
							<td>此字段同时适用于题目。请填写对应的代码：<br/>Y－是<br/>N－否</td>
						</tr>
					</xsl:when>
					<xsl:when test="$in_lang = 'zh-hk'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-hk/field[@name='wb_imp_tem_as_mulc']"/>
							</td>
							<td nowrap="nowrap">是</td>
							<td align="right" nowrap="nowrap">1</td>
							<td nowrap="nowrap">文本</td>
							<td>此欄位同時適用於題目。請填寫對應的代碼：<br/>Y－是<br/>N－否</td>
						</tr>
					</xsl:when>
					<xsl:otherwise>
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/en-us/field[@name='wb_imp_tem_as_mulc']"/>
							</td>
							<td nowrap="nowrap">Y</td>
							<td align="right" nowrap="nowrap">1</td>
							<td nowrap="nowrap">Text</td>
							<td>This applies to the question. Please use the codes below:<br/>Y - Yes<br/>N - No</td>
						</tr>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:when test="$field = 'ashtml_with_option'">
				<xsl:choose>
					<xsl:when test="$in_lang = 'zh-cn'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-cn/field[@name='wb_imp_tem_as_html']"/>
							</td>
							<td nowrap="nowrap">是</td>
							<td align="right" nowrap="nowrap">1</td>
							<td nowrap="nowrap">文本</td>
							<td>此字段同时适用于题目与选项。请填写对应的代码：<br/>Y－是<br/>N－否</td>
						</tr>
					</xsl:when>
					<xsl:when test="$in_lang = 'zh-hk'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-hk/field[@name='wb_imp_tem_as_html']"/>
							</td>
							<td nowrap="nowrap">是</td>
							<td align="right" nowrap="nowrap">1</td>
							<td nowrap="nowrap">文本</td>
							<td>此欄位同時適用於題目與選項。請填寫對應的代碼：<br/>Y－是<br/>N－否</td>
						</tr>
					</xsl:when>
					<xsl:otherwise>
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/en-us/field[@name='wb_imp_tem_as_html']"/>
							</td>
							<td nowrap="nowrap">Y</td>
							<td align="right" nowrap="nowrap">1</td>
							<td nowrap="nowrap">Text</td>
							<td>This applies to the question and the option fields. Please use the codes below:<br/>Y - Yes<br/>N - No</td>
						</tr>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<!-- end of  As Multiple Choice -->
			<!-- Option 1_10 -->
			<xsl:when test="$field = 'option_1_10'">
				<xsl:choose>
					<xsl:when test="$in_lang = 'zh-cn'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-cn/field[@name='wb_imp_tem_option_1']"/> - 
							<xsl:value-of select="./label/zh-cn/field[@name='wb_imp_tem_option_10']"/>
							</td>
							<td nowrap="nowrap">是</td>
							<td align="right" nowrap="nowrap">无</td>
							<td nowrap="nowrap">文本</td>
							<td>最多10个选项，前2个选项是必填。</td>
						</tr>
					</xsl:when>
					<xsl:when test="$in_lang = 'zh-hk'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-hk/field[@name='wb_imp_tem_option_1']"/> - 
							<xsl:value-of select="./label/zh-hk/field[@name='wb_imp_tem_option_10']"/>
							</td>
							<td nowrap="nowrap">是</td>
							<td align="right" nowrap="nowrap">無</td>
							<td nowrap="nowrap">文本</td>
							<td>最多10個選項，前2個選項是必填。</td>
						</tr>
					</xsl:when>
					<xsl:otherwise>
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/en-us/field[@name='wb_imp_tem_option_1']"/> - 
							<xsl:value-of select="./label/en-us/field[@name='wb_imp_tem_option_10']"/>
							</td>
							<td nowrap="nowrap">Y</td>
							<td align="right" nowrap="nowrap">Not Restricted</td>
							<td nowrap="nowrap">Text</td>
							<td>The choices of the question. It supports up to 10 options and the first 2 options are required.</td>
						</tr>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:when test="$field = 'score_1_10'">
				<xsl:choose>
					<xsl:when test="$in_lang = 'zh-cn'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-cn/field[@name='wb_imp_tem_score_1']"/> - 
							<xsl:value-of select="./label/zh-cn/field[@name='wb_imp_tem_score_10']"/>
							</td>
							<td nowrap="nowrap">是</td>
							<td align="right" nowrap="nowrap">无</td>
							<td nowrap="nowrap">文本</td>
							<td>最多10个分数<xsl:if test="$mod_type != 'SVY'">，前2个分数是必填。</xsl:if></td>
						</tr>
					</xsl:when>
					<xsl:when test="$in_lang = 'zh-hk'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-hk/field[@name='wb_imp_tem_score_1']"/> - 
							<xsl:value-of select="./label/zh-hk/field[@name='wb_imp_tem_score_10']"/>
							</td>
							<td nowrap="nowrap">是</td>
							<td align="right" nowrap="nowrap">無</td>
							<td nowrap="nowrap">文本</td>
							<td>最多10個分數<xsl:if test="$mod_type != 'SVY'">，前2個分數是必填。</xsl:if></td>
						</tr>
					</xsl:when>
					<xsl:otherwise>
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/en-us/field[@name='wb_imp_tem_score_1']"/> - 
							<xsl:value-of select="./label/en-us/field[@name='wb_imp_tem_score_10']"/>
							</td>
							<td nowrap="nowrap">Y</td>
							<td align="right" nowrap="nowrap">Not Restricted</td>
							<td nowrap="nowrap">Text</td>
							<td>The choices of the question.<xsl:if test="$mod_type != 'SVY'"> It supports up to 10 score and the first 2 score are required.</xsl:if></td>
						</tr>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:when test="$field = 'option_1_10_for_sc_not_fill'">
				<xsl:choose>
					<xsl:when test="$in_lang = 'zh-cn'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-cn/field[@name='wb_imp_tem_option_1']"/> -  
							<xsl:value-of select="./label/zh-cn/field[@name='wb_imp_tem_option_10']"/>
							</td>
							<td nowrap="nowrap">是</td>
							<td align="right" nowrap="nowrap">无</td>
							<td nowrap="nowrap">文本</td>
							<td>请保留空白。</td>
						</tr>
					</xsl:when>
					<xsl:when test="$in_lang = 'zh-hk'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-hk/field[@name='wb_imp_tem_option_1']"/> -  
							<xsl:value-of select="./label/zh-hk/field[@name='wb_imp_tem_option_10']"/>
							</td>
							<td nowrap="nowrap">是</td>
							<td align="right" nowrap="nowrap">無</td>
							<td nowrap="nowrap">文本</td>
							<td>請保留空白。</td>
						</tr>
					</xsl:when>
					<xsl:otherwise>
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/en-us/field[@name='wb_imp_tem_option_1']"/> - 
							<xsl:value-of select="./label/en-us/field[@name='wb_imp_tem_option_10']"/>
							</td>
							<td nowrap="nowrap">Y</td>
							<td align="right" nowrap="nowrap">Not Restricted</td>
							<td nowrap="nowrap">Text</td>
							<td>Do not fill in anything.</td>
						</tr>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<!-- end of Option 1_10 -->
			<!-- Shuffle -->
			<xsl:when test="$field = 'shuffle'">
				<xsl:choose>
					<xsl:when test="$in_lang = 'zh-cn'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-cn/field[@name='wb_imp_tem_shuffle']"/>
							</td>
							<td nowrap="nowrap">是</td>
							<td align="right" nowrap="nowrap">1</td>
							<td nowrap="nowrap">文本</td>
							<td>请填写对应的代码：<br/>Y－是<br/>N－否</td>
						</tr>
					</xsl:when>
					<xsl:when test="$in_lang = 'zh-hk'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-hk/field[@name='wb_imp_tem_shuffle']"/>
							</td>
							<td nowrap="nowrap">是</td>
							<td align="right" nowrap="nowrap">1</td>
							<td nowrap="nowrap">文本</td>
							<td>請填寫對應的代碼：<br/>Y－是<br/>N－否</td>
						</tr>
					</xsl:when>
					<xsl:otherwise>
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/en-us/field[@name='wb_imp_tem_shuffle']"/>
							</td>
							<td nowrap="nowrap">Y</td>
							<td align="right" nowrap="nowrap">1</td>
							<td nowrap="nowrap">Text</td>
							<td>Please use the codes below:<br/>Y - Yes<br/>N - No</td>
						</tr>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:when test="$field = 'shuffle_for_sc'">
				<xsl:choose>
					<xsl:when test="$in_lang = 'zh-cn'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-cn/field[@name='wb_imp_tem_shuffle']"/>
							</td>
							<td nowrap="nowrap">是</td>
							<td align="right" nowrap="nowrap">1</td>
							<td nowrap="nowrap">文本</td>
							<td>请填写对应的代码：<br/>Y－是<br/>N－否</td>
						</tr>
					</xsl:when>
					<xsl:when test="$in_lang = 'zh-hk'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-hk/field[@name='wb_imp_tem_shuffle']"/>
							</td>
							<td nowrap="nowrap">是</td>
							<td align="right" nowrap="nowrap">1</td>
							<td nowrap="nowrap">文本</td>
							<td>請填寫對應的代碼：<br/>Y－是<br/>N－否</td>
						</tr>
					</xsl:when>
					<xsl:otherwise>
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/en-us/field[@name='wb_imp_tem_shuffle']"/>
							</td>
							<td nowrap="nowrap">Y</td>
							<td align="right" nowrap="nowrap">1</td>
							<td nowrap="nowrap">Text</td>
							<td>Please use the codes below:<br/>Y - Yes<br/>N - No</td>
						</tr>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:when test="$field = 'shuffle_for_sc_not_fill'">
				<xsl:choose>
					<xsl:when test="$in_lang = 'zh-cn'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-cn/field[@name='wb_imp_tem_shuffle']"/>
							</td>
							<td nowrap="nowrap">是</td>
							<td align="right" nowrap="nowrap">1</td>
							<td nowrap="nowrap">文本</td>
							<td>请保留空白。</td>
						</tr>
					</xsl:when>
					<xsl:when test="$in_lang = 'zh-hk'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-hk/field[@name='wb_imp_tem_shuffle']"/>
							</td>
							<td nowrap="nowrap">是</td>
							<td align="right" nowrap="nowrap">1</td>
							<td nowrap="nowrap">文本</td>
							<td>請保留空白。</td>
						</tr>
					</xsl:when>
					<xsl:otherwise>
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/en-us/field[@name='wb_imp_tem_shuffle']"/>
							</td>
							<td nowrap="nowrap">Y</td>
							<td align="right" nowrap="nowrap">1</td>
							<td nowrap="nowrap">Text</td>
							<td>Do not fill in anything.</td>
						</tr>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<!-- end of Shuffle -->
			<!-- Answer -->
			<xsl:when test="$field = 'answer'">
				<xsl:choose>
					<xsl:when test="$in_lang = 'zh-cn'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-cn/field[@name='wb_imp_tem_answer']"/>
							</td>
							<td nowrap="nowrap">是</td>
							<td align="right" nowrap="nowrap">无</td>
							<td nowrap="nowrap">文本</td>
							<td>请填写选项对应的号码，如多于一个正确答案，请用半角逗号分隔。<br/>如：选项1，选项2，选项3，选项4<br/>
							   &#160; &#160; &#160;&#160; 小a ，&#160;小u ，&#160;小y ，&#160;小s<br/>正确答案：小a，填写答案编号为：1。<br/>
							        如果有多个答案：  小y ，小s，填写答案编号为：3，4。
							</td>
						</tr>
					</xsl:when>
					<xsl:when test="$in_lang = 'zh-hk'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-hk/field[@name='wb_imp_tem_answer']"/>
							</td>
							<td nowrap="nowrap">是</td>
							<td align="right" nowrap="nowrap">無</td>
							<td nowrap="nowrap">文本</td>
							<td>請填寫選項對應的號碼，如多於一個正確答案，請用半角逗號分隔。<br/>如：選項1，選項2，選項3，選項4<br/>
							   &#160; &#160; &#160;&#160; 小a ，小u ，小y ，小s<br/>正確答案：小a，填寫答案編號為：1。<br/>
							        如果有多個答案：  小y ，小s， 填寫答案編號為：3，4。</td>
						</tr>
					</xsl:when>
					<xsl:otherwise>
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/en-us/field[@name='wb_imp_tem_answer']"/>
							</td>
							<td nowrap="nowrap">Y</td>
							<td align="right" nowrap="nowrap">N/A</td>
							<td nowrap="nowrap">Text</td>
							<td>Please fill out the options for the corresponding number, such as more than one right answer, please use half-width commas.<br/>Such as: option 1, option 2, option 3 and option 4<br/>
							   &#160; &#160; &#160;&#160; Small little a, u, y, s<br/>Correct answer: a small a, fill in the answer number is: 1.<br/>
							       If there are multiple answers: little y, s, fill in the answer number is: 3, 4.</td>
						</tr>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:when test="$field = 'answer_for_mt'">
				<xsl:choose>
					<xsl:when test="$in_lang = 'zh-cn'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-cn/field[@name='wb_imp_tem_answer']"/>
							</td>
							<td nowrap="nowrap">是</td>
							<td align="right" nowrap="nowrap">无</td>
							<td nowrap="nowrap">整数</td>
							<td>请填写对应每个来源的目标编号。用半角逗号分隔，每个来源只能有一个匹配的目标。<br/>例一：有两个来源，目标8跟来源1匹配，目标9跟来源2匹配<br/>请在此字段填写：8,9<!-- <br/>例二：有三个来源，没有目标跟来源1和3匹配，目标7跟来源2匹配<br/>请在此字段填写：0,7,0 --></td>
						</tr>
					</xsl:when>
					<xsl:when test="$in_lang = 'zh-hk'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-hk/field[@name='wb_imp_tem_answer']"/>
							</td>
							<td nowrap="nowrap">是</td>
							<td align="right" nowrap="nowrap">無</td>
							<td nowrap="nowrap">整數</td>
							<td>請填寫對應每個來源的目標編號。用半形逗號分隔，每個來源只能有一個匹配的目標。<br/>例一：有兩個來源，目標8跟來源1匹配，目標9跟來源2匹配<br/>請在此欄位填寫：8,9<!-- <br/>例二：有三個來源，沒有目標跟來源1和3匹配，目標7跟來源2匹配<br/>請在此欄位填寫：0,7,0 --></td>
						</tr>
					</xsl:when>
					<xsl:otherwise>
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/en-us/field[@name='wb_imp_tem_answer']"/>
							</td>
							<td nowrap="nowrap">Y</td>
							<td align="right" nowrap="nowrap">N/A</td>
							<td nowrap="nowrap">Integer(s)</td>
							<td>The correct target numbers of the sources, arranged from source 1 to 10.For example, <br/>if there are two sources: <br/>Source 1 matches target 8<br/>Source 2 matches target 9<br/>In this case, you should enter 8,9<!-- <br/>In addition, if there is no target for a source (a distracter), enter 0 -->.</td>
						</tr>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:when test="$field = 'answer_for_tf'">
				<xsl:choose>
					<xsl:when test="$in_lang = 'zh-cn'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-cn/field[@name='wb_imp_tem_answer']"/>
							</td>
							<td nowrap="nowrap">是</td>
							<td align="right" nowrap="nowrap">无</td>
							<td nowrap="nowrap">文本</td>
							<td>请填写对应的代码: <br/>TRUE-正确<br/>FALSE-错误</td>
						</tr>
					</xsl:when>
					<xsl:when test="$in_lang = 'zh-hk'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-hk/field[@name='wb_imp_tem_answer']"/>
							</td>
							<td nowrap="nowrap">是</td>
							<td align="right" nowrap="nowrap">無</td>
							<td nowrap="nowrap">文本</td>
							<td>請填寫對應的代碼: <br/>TRUE-正確<br/>FALSE-錯誤</td>
						</tr>
					</xsl:when>
					<xsl:otherwise>
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/en-us/field[@name='wb_imp_tem_answer']"/>
							</td>
							<td nowrap="nowrap">Y</td>
							<td align="right" nowrap="nowrap">N/A</td>
							<td nowrap="nowrap">Text</td>
							<td>Please use the codes below: <br/>TRUE-true<br/>FALSE-false</td>
						</tr>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:when test="$field = 'answer_for_sc'">
				<xsl:choose>
					<xsl:when test="$in_lang = 'zh-cn'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-cn/field[@name='wb_imp_tem_answer']"/>
							</td>
							<td nowrap="nowrap">是</td>
							<td align="right" nowrap="nowrap">无</td>
							<td nowrap="nowrap">整数</td>
							<td/>
						</tr>
					</xsl:when>
					<xsl:when test="$in_lang = 'zh-hk'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-hk/field[@name='wb_imp_tem_answer']"/>
							</td>
							<td nowrap="nowrap">是</td>
							<td align="right" nowrap="nowrap">無</td>
							<td nowrap="nowrap">整數</td>
							<td/>
						</tr>
					</xsl:when>
					<xsl:otherwise>
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/en-us/field[@name='wb_imp_tem_answer']"/>
							</td>
							<td nowrap="nowrap">Y</td>
							<td align="right" nowrap="nowrap">N/A</td>
							<td nowrap="nowrap">Integer(s)</td>
							<td/>
						</tr>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:when test="$field = 'answer_for_sc_not_fill'">
				<xsl:choose>
					<xsl:when test="$in_lang = 'zh-cn'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-cn/field[@name='wb_imp_tem_answer']"/>
							</td>
							<td nowrap="nowrap">是</td>
							<td align="right" nowrap="nowrap">无</td>
							<td nowrap="nowrap">文本</td>
							<td>请保留空白。</td>
						</tr>
					</xsl:when>
					<xsl:when test="$in_lang = 'zh-hk'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-hk/field[@name='wb_imp_tem_answer']"/>
							</td>
							<td nowrap="nowrap">是</td>
							<td align="right" nowrap="nowrap">無</td>
							<td nowrap="nowrap">文本</td>
							<td>請保留空白。</td>
						</tr>
					</xsl:when>
					<xsl:otherwise>
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/en-us/field[@name='wb_imp_tem_answer']"/>
							</td>
							<td nowrap="nowrap">Y</td>
							<td align="right" nowrap="nowrap">N/A</td>
							<td nowrap="nowrap">Integer(s)</td>
							<td>Do not fill in anything.</td>
						</tr>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<!-- end of Answer -->
			<!-- Score -->
			<xsl:when test="$field = 'score'">
				<xsl:choose>
					<xsl:when test="$in_lang = 'zh-cn'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-cn/field[@name='wb_imp_tem_score']"/>
							</td>
							<td nowrap="nowrap">是</td>
							<td align="right" nowrap="nowrap">无</td>
							<td nowrap="nowrap">1-100</td>
							<td/>
						</tr>
					</xsl:when>
					<xsl:when test="$in_lang = 'zh-hk'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-hk/field[@name='wb_imp_tem_score']"/>
							</td>
							<td nowrap="nowrap">是</td>
							<td align="right" nowrap="nowrap">無</td>
							<td nowrap="nowrap">1-100</td>
							<td/>
						</tr>
					</xsl:when>
					<xsl:otherwise>
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/en-us/field[@name='wb_imp_tem_score']"/>
							</td>
							<td nowrap="nowrap">Y</td>
							<td align="right" nowrap="nowrap">N/A</td>
							<td nowrap="nowrap">1-100</td>
							<td/>
						</tr>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:when test="$field = 'score_for_mt'">
				<xsl:choose>
					<xsl:when test="$in_lang = 'zh-cn'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-cn/field[@name='wb_imp_tem_score']"/>
							</td>
							<td nowrap="nowrap">是</td>
							<td align="right" nowrap="nowrap">无</td>
							<td nowrap="nowrap">1-100</td>
							<td>请为每一对的“来源-目标”填写分数。用半角逗号分隔，每一对“来源-目标”都必须有分数。<br/>例一：有两个来源，目标8跟来源1匹配得3分，目标9跟来源2匹配得4分<br/>请在此字段填写：3,4<!-- <br/>例二：有三个来源，没有目标跟来源1和3匹配，目标7跟来源2匹配得5分<br/>请在此字段填写：0,5,0 --></td>
						</tr>
					</xsl:when>
					<xsl:when test="$in_lang = 'zh-hk'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-hk/field[@name='wb_imp_tem_score']"/></td>
							<td nowrap="nowrap">是</td>
							<td align="right" nowrap="nowrap">無</td>
							<td nowrap="nowrap">1-100</td>
							<td>請爲每一對的“來源-目標”填寫分數。用半形逗號分隔，每一對“來源-目標”都必須有分數。<br/>例一：有兩個來源，目標8跟來源1匹配得3分，目標9跟來源2匹配得4分<br/>請在此欄位填寫：3,4<!-- <br/>例二：有三個來源，沒有目標跟來源1和3匹配，目標7跟來源2匹配得5分<br/>請在此欄位填寫：0,5,0 --></td>
						</tr>
					</xsl:when>
					<xsl:otherwise>
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/en-us/field[@name='wb_imp_tem_score']"/>
							</td>
							<td nowrap="nowrap">Y</td>
							<td align="right" nowrap="nowrap">N/A</td>
							<td nowrap="nowrap">1-100</td>
							<td>The score of each source-target matching pair, arranged from source 1 to 10.
							<br/>For example, if there are two sources: <br/>Source 1 matches target 8, which scores 3 points
							<br/>Source 2 matches target 9, which scores 4 points
							<br/>In this case, you should enter 3,4 in addition<!-- ,
							<br/>if there is no target for a source (a distracter), enter 0 --></td>
						</tr>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:when test="$field = 'score_for_sc_not_fill'">
				<xsl:choose>
					<xsl:when test="$in_lang = 'zh-cn'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-cn/field[@name='wb_imp_tem_score']"/>
							</td>
							<td nowrap="nowrap">是</td>
							<td align="right" nowrap="nowrap">无</td>
							<td nowrap="nowrap">文本</td>
							<td>请保留空白。</td>
						</tr>
					</xsl:when>
					<xsl:when test="$in_lang = 'zh-hk'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-hk/field[@name='wb_imp_tem_score']"/>
							</td>
							<td nowrap="nowrap">是</td>
							<td align="right" nowrap="nowrap">無</td>
							<td nowrap="nowrap">文本</td>
							<td>請保留空白。</td>
						</tr>
					</xsl:when>
					<xsl:otherwise>
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/en-us/field[@name='wb_imp_tem_score']"/>
							</td>
							<td nowrap="nowrap">Y</td>
							<td align="right" nowrap="nowrap">N/A</td>
							<td nowrap="nowrap">Text</td>
							<td>Do not fill in anything.</td>
						</tr>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<!-- end of Score -->
			<!-- Difficulty -->
			<xsl:when test="$field = 'difficulty'">
				<xsl:choose>
					<xsl:when test="$in_lang = 'zh-cn'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-cn/field[@name='wb_imp_tem_difficulty']"/>
							</td>
							<td nowrap="nowrap">是</td>
							<td align="right" nowrap="nowrap">无</td>
							<td nowrap="nowrap">文本</td>
							<td>请填写对应的代码：<br/>1－容易<br/>2－一般<br/>3－困难</td>
						</tr>
					</xsl:when>
					<xsl:when test="$in_lang = 'zh-hk'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-hk/field[@name='wb_imp_tem_difficulty']"/>
							</td>
							<td nowrap="nowrap">是</td>
							<td align="right" nowrap="nowrap">無</td>
							<td nowrap="nowrap">文本</td>
							<td>請填寫對應的代碼：<br/>1－容易<br/>2－一般<br/>3－困</td>
						</tr>
					</xsl:when>
					<xsl:otherwise>
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/en-us/field[@name='wb_imp_tem_difficulty']"/>
							</td>
							<td nowrap="nowrap">Y</td>
							<td align="right" nowrap="nowrap">N/A</td>
							<td nowrap="nowrap">Integer</td>
							<td>Please use the codes below:<br/>1 - Easy<br/>2 - Normal<br/>3 - Hard</td>
						</tr>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:when test="$field = 'difficulty_for_sc_not_fill'">
				<xsl:choose>
					<xsl:when test="$in_lang = 'zh-cn'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-cn/field[@name='wb_imp_tem_difficulty']"/>
							</td>
							<td nowrap="nowrap">是</td>
							<td align="right" nowrap="nowrap">无</td>
							<td nowrap="nowrap">文本</td>
							<td>请保留空白。</td>
						</tr>
					</xsl:when>
					<xsl:when test="$in_lang = 'zh-hk'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-hk/field[@name='wb_imp_tem_difficulty']"/>
							</td>
							<td nowrap="nowrap">是</td>
							<td align="right" nowrap="nowrap">無</td>
							<td nowrap="nowrap">文本</td>
							<td>請保留空白。</td>
						</tr>
					</xsl:when>
					<xsl:otherwise>
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/en-us/field[@name='wb_imp_tem_difficulty']"/>
							</td>
							<td nowrap="nowrap">Y</td>
							<td align="right" nowrap="nowrap">N/A</td>
							<td nowrap="nowrap">Integer</td>
							<td>Do not fill in anything.</td>
						</tr>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<!-- end of Difficulty -->
			<!-- Status -->
			<xsl:when test="$field = 'status'">
				<xsl:choose>
					<xsl:when test="$in_lang = 'zh-cn'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-cn/field[@name='wb_imp_tem_status']"/>
							</td>
							<td nowrap="nowrap">是</td>
							<td align="right" nowrap="nowrap">无</td>
							<td nowrap="nowrap">文本</td>
							<td>请填写对应的代码：<br/>ON－已发布<br/>OFF－未发布</td>
						</tr>
					</xsl:when>
					<xsl:when test="$in_lang = 'zh-hk'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-hk/field[@name='wb_imp_tem_status']"/>
							</td>
							<td nowrap="nowrap">是</td>
							<td align="right" nowrap="nowrap">無</td>
							<td nowrap="nowrap">文本</td>
							<td>請填寫對應的代碼：<br/>ON－已發佈<br/>OFF－未發佈</td>
						</tr>
					</xsl:when>
					<xsl:otherwise>
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/en-us/field[@name='wb_imp_tem_status']"/>
							</td>
							<td nowrap="nowrap">Y</td>
							<td align="right" nowrap="nowrap">N/A</td>
							<td nowrap="nowrap">Text</td>
							<td>Please use the codes below:<br/>ON - Published<br/>OFF - Unpublished</td>
						</tr>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:when test="$field = 'status_for_sc_not_fill'">
				<xsl:choose>
					<xsl:when test="$in_lang = 'zh-cn'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-cn/field[@name='wb_imp_tem_status']"/>
							</td>
							<td nowrap="nowrap">是</td>
							<td align="right" nowrap="nowrap">无</td>
							<td nowrap="nowrap">文本</td>
							<td>请保留空白。</td>
						</tr>
					</xsl:when>
					<xsl:when test="$in_lang = 'zh-hk'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-hk/field[@name='wb_imp_tem_status']"/>
							</td>
							<td nowrap="nowrap">是</td>
							<td align="right" nowrap="nowrap">無</td>
							<td nowrap="nowrap">文本</td>
							<td>請保留空白。</td>
						</tr>
					</xsl:when>
					<xsl:otherwise>
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/en-us/field[@name='wb_imp_tem_status']"/>
							</td>
							<td nowrap="nowrap">Y</td>
							<td align="right" nowrap="nowrap">N/A</td>
							<td nowrap="nowrap">Text</td>
							<td>Do not fill in anything.</td>
						</tr>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<!-- end of Status -->
			<!-- Description -->
			<!-- <xsl:when test="$field = 'description'">
				<xsl:choose>
					<xsl:when test="$in_lang = 'zh-cn'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-cn/field[@name='wb_imp_tem_description']"/>
							</td>
							<td nowrap="nowrap">否</td>
							<td align="right" nowrap="nowrap">1000</td>
							<td nowrap="nowrap">文本</td>
							<td/>
						</tr>
					</xsl:when>
					<xsl:when test="$in_lang = 'zh-hk'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-hk/field[@name='wb_imp_tem_description']"/>
							</td>
							<td nowrap="nowrap">否</td>
							<td align="right" nowrap="nowrap">1000</td>
							<td nowrap="nowrap">文本</td>
							<td/>
						</tr>
					</xsl:when>
					<xsl:otherwise>
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/en-us/field[@name='wb_imp_tem_description']"/>
							</td>
							<td nowrap="nowrap">N</td>
							<td align="right" nowrap="nowrap">1000</td>
							<td nowrap="nowrap">Text</td>
							<td/>
						</tr>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when> -->
			<!-- end of Description -->
			<!-- Explanation -->
			<!-- <xsl:when test="$field = 'explanation'">
				<xsl:choose>
					<xsl:when test="$in_lang = 'zh-cn'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-cn/field[@name='wb_imp_tem_explanation']"/>
							</td>
							<td nowrap="nowrap">否</td>
							<td align="right" nowrap="nowrap">无</td>
							<td nowrap="nowrap">文本</td>
							<td/>
						</tr>
					</xsl:when>
					<xsl:when test="$in_lang = 'zh-hk'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-hk/field[@name='wb_imp_tem_explanation']"/>
							</td>
							<td nowrap="nowrap">否</td>
							<td align="right" nowrap="nowrap">無</td>
							<td nowrap="nowrap">文本</td>
							<td/>
						</tr>
					</xsl:when>
					<xsl:otherwise>
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/en-us/field[@name='wb_imp_tem_explanation']"/>
							</td>
							<td nowrap="nowrap">N</td>
							<td align="right" nowrap="nowrap">Not restricted</td>
							<td nowrap="nowrap">Text</td>
							<td/>
						</tr>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when> -->
			<!-- end of Explanation -->
			<!-- Blank Answer 1_10 -->
			<xsl:when test="$field = 'blank_answer_1_10'">
				<xsl:choose>
					<xsl:when test="$in_lang = 'zh-cn'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-cn/field[@name='wb_imp_tem_blank_answer_1']"/> - 
							<xsl:value-of select="./label/zh-cn/field[@name='wb_imp_tem_blank_answer_10']"/> *
							</td>
							<td nowrap="nowrap">是</td>
							<td nowrap="nowrap" align="right">无</td>
							<td nowrap="nowrap">文本</td>
							<td>空白处的答案，最多支持10个。此字段不能包含换行。</td>
						</tr>
					</xsl:when>
					<xsl:when test="$in_lang = 'zh-hk'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-hk/field[@name='wb_imp_tem_blank_answer_1']"/> - 
							<xsl:value-of select="./label/zh-hk/field[@name='wb_imp_tem_blank_answer_10']"/> *							
							</td>
							<td nowrap="nowrap">是</td>
							<td nowrap="nowrap" align="right">無</td>
							<td nowrap="nowrap">文本</td>
							<td>空白處的答案，最多支持10個。此欄位不能包含換行。</td>
						</tr>
					</xsl:when>
					<xsl:otherwise>
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/en-us/field[@name='wb_imp_tem_blank_answer_1']"/> - 
							<xsl:value-of select="./label/en-us/field[@name='wb_imp_tem_blank_answer_10']"/> *
							</td>
							<td nowrap="nowrap">Y</td>
							<td nowrap="nowrap" align="right">Not Restricted</td>
							<td nowrap="nowrap">Text</td>
							<td>The answer of the blank. It supports up to 10 blanks. This field does not support multiple line.</td>
						</tr>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<!-- end of Blank Answer 1_10 -->
			<!-- Blank Score 1_10 -->
			<xsl:when test="$field = 'blank_score_1_10'">
				<xsl:choose>
					<xsl:when test="$in_lang = 'zh-cn'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-cn/field[@name='wb_imp_tem_blank_score_1']"/> - 
							<xsl:value-of select="./label/zh-cn/field[@name='wb_imp_tem_blank_score_10']"/> *
							</td>
							<td nowrap="nowrap">是</td>
							<td nowrap="nowrap" align="right">无</td>
							<td nowrap="nowrap">文本</td>
							<td>对应空白的分数。</td>
						</tr>
					</xsl:when>
					<xsl:when test="$in_lang = 'zh-hk'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-hk/field[@name='wb_imp_tem_blank_score_1']"/> - 
							<xsl:value-of select="./label/zh-hk/field[@name='wb_imp_tem_blank_score_10']"/> *
							</td>
							<td nowrap="nowrap">是</td>
							<td nowrap="nowrap" align="right">無</td>
							<td nowrap="nowrap">文本</td>
							<td>對應空白的分數。</td>
						</tr>
					</xsl:when>
					<xsl:otherwise>
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/en-us/field[@name='wb_imp_tem_blank_score_1']"/> - 
							<xsl:value-of select="./label/en-us/field[@name='wb_imp_tem_blank_score_10']"/> *
							</td>
							<td nowrap="nowrap">Y</td>
							<td nowrap="nowrap" align="right">1-100</td>
							<td nowrap="nowrap">Integer</td>
							<td>The score of the corresponding blank.</td>
						</tr>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<!-- end of Blank Score 1_10 -->
			<!-- Source 1_10 -->
			<xsl:when test="$field = 'source_1_10'">
				<xsl:choose>
					<xsl:when test="$in_lang = 'zh-cn'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-cn/field[@name='wb_imp_tem_scource_1']"/> - 
							<xsl:value-of select="./label/zh-cn/field[@name='wb_imp_tem_scource_10']"/> *							
							</td>
							<td nowrap="nowrap">是</td>
							<td nowrap="nowrap" align="right">无</td>
							<td nowrap="nowrap">文本</td>
							<td>最多10个来源。<br/>此字段不能包含换行。</td>
						</tr>
					</xsl:when>
					<xsl:when test="$in_lang = 'zh-hk'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-hk/field[@name='wb_imp_tem_scource_1']"/> - 
							<xsl:value-of select="./label/zh-hk/field[@name='wb_imp_tem_scource_10']"/> *							
							</td>
							<td nowrap="nowrap">是</td>
							<td nowrap="nowrap" align="right">無</td>
							<td nowrap="nowrap">文本</td>
							<td>最多10個來源。<br/>此欄位不能包含換行。</td>
						</tr>
					</xsl:when>
					<xsl:otherwise>
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/en-us/field[@name='wb_imp_tem_scource_1']"/> - 
							<xsl:value-of select="./label/en-us/field[@name='wb_imp_tem_scource_10']"/> *
							</td>
							<td nowrap="nowrap">Y</td>
							<td nowrap="nowrap" align="right">Not Restricted</td>
							<td nowrap="nowrap">Text</td>
							<td>The sources of the matching. It supports up to 10 sources.<br/>Remark: This field fo not support any new paragraphs or new lines.</td>
						</tr>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<!-- end of Source 1_10 -->
			<!-- Target 1_10 -->
			<xsl:when test="$field = 'target_1_10'">
				<xsl:choose>
					<xsl:when test="$in_lang = 'zh-cn'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-cn/field[@name='wb_imp_tem_target_1']"/> - 
							<xsl:value-of select="./label/zh-cn/field[@name='wb_imp_tem_target_10']"/> *							
							</td>
							<td nowrap="nowrap">是</td>
							<td nowrap="nowrap" align="right">无</td>
							<td nowrap="nowrap">文本</td>
							<td>最多10个目标。<br/>此字段不能包含换行且不能重复。</td>
						</tr>
					</xsl:when>
					<xsl:when test="$in_lang = 'zh-hk'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-hk/field[@name='wb_imp_tem_target_1']"/> - 
							<xsl:value-of select="./label/zh-hk/field[@name='wb_imp_tem_target_10']"/> *
							</td>
							<td nowrap="nowrap">是</td>
							<td nowrap="nowrap" align="right">無</td>
							<td nowrap="nowrap">文本</td>
							<td>最多10個目標。<br/>此字段不能包含換行且不能重復。</td>
						</tr>
					</xsl:when>
					<xsl:otherwise>
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/en-us/field[@name='wb_imp_tem_target_1']"/> - 
							<xsl:value-of select="./label/en-us/field[@name='wb_imp_tem_target_10']"/> *
							</td>
							<td nowrap="nowrap">Y</td>
							<td nowrap="nowrap" align="right">Not Restricted</td>
							<td nowrap="nowrap">Text</td>
							<td>The targets of the matching.<br/>It supports up to 10 targets.</td>
						</tr>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<!-- end of Target 1_10 -->
			<!-- Submit File -->
			<!-- <xsl:when test="$field = 'submit_file'">
				<xsl:choose>
					<xsl:when test="$in_lang = 'zh-cn'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-cn/field[@name='wb_imp_tem_submit_file']"/>
							</td>
							<td nowrap="nowrap">是</td>
							<td align="right" nowrap="nowrap">1</td>
							<td nowrap="nowrap">文本</td>
							<td>请填写对应的代码：<br/>Y－是<br/>N－否</td>
						</tr>
					</xsl:when>
					<xsl:when test="$in_lang = 'zh-hk'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-hk/field[@name='wb_imp_tem_submit_file']"/>
							</td>
							<td nowrap="nowrap">是</td>
							<td align="right" nowrap="nowrap">1</td>
							<td nowrap="nowrap">文本</td>
							<td>請填寫對應的代碼：<br/>Y－是<br/>N－否</td>
						</tr>
					</xsl:when>
					<xsl:otherwise>
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/en-us/field[@name='wb_imp_tem_submit_file']"/>
							</td>
							<td nowrap="nowrap">Y</td>
							<td align="right" nowrap="nowrap">1</td>
							<td nowrap="nowrap">Text</td>
							<td>Please use the codes below:<br/>Y - Yes<br/>N - No</td>
						</tr>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when> -->
			<!-- end of Submit File -->
			<!-- Model Answer -->
			<xsl:when test="$field = 'model_answer'">
				<xsl:choose>
					<xsl:when test="$in_lang = 'zh-cn'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-cn/field[@name='wb_imp_tem_model_answer']"/>
							</td>
							<td nowrap="nowrap">否</td>
							<td align="right" nowrap="nowrap">无</td>
							<td nowrap="nowrap">文本</td>
							<td/>
						</tr>
					</xsl:when>
					<xsl:when test="$in_lang = 'zh-hk'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-hk/field[@name='wb_imp_tem_model_answer']"/>
							</td>
							<td nowrap="nowrap">否</td>
							<td align="right" nowrap="nowrap">無</td>
							<td nowrap="nowrap">文本</td>
							<td/>
						</tr>
					</xsl:when>
					<xsl:otherwise>
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/en-us/field[@name='wb_imp_tem_model_answer']"/>
							</td>
							<td nowrap="nowrap">N</td>
							<td align="right" nowrap="nowrap">Not Restricted</td>
							<td nowrap="nowrap">Text</td>
							<td>The model answer of the essay.</td>
						</tr>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<!-- end of Model Answer -->
			<!-- Criteria -->
			<xsl:when test="$field = 'criteria'">
				<xsl:choose>
					<xsl:when test="$in_lang = 'zh-cn'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-cn/field[@name='wb_imp_tem_criteria']"/>
							</td>
							<td nowrap="nowrap">否</td>
							<td align="right" nowrap="nowrap">500</td>
							<td nowrap="nowrap">文本</td>
							<td>如果抽题条件如下：<br/>抽题条件1－抽取1道2分的题目<br/>抽题条件2－抽取3道4分的题目<br/>请在此字段填写：2(1),4(3)</td>
						</tr>
					</xsl:when>
					<xsl:when test="$in_lang = 'zh-hk'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-hk/field[@name='wb_imp_tem_criteria']"/>
							</td>
							<td nowrap="nowrap">否</td>
							<td align="right" nowrap="nowrap">500</td>
							<td nowrap="nowrap">文本</td>
							<td>如果抽題條件如下：<br/>抽題條件1－抽取1道2分的題目<br/>抽題條件2－抽取3道4分的題目<br/>請在此欄位填寫：2(1),4(3)</td>
						</tr>
					</xsl:when>
					<xsl:otherwise>
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/en-us/field[@name='wb_imp_tem_criteria']"/>
							</td>
							<td nowrap="nowrap">N</td>
							<td align="right" nowrap="nowrap">500</td>
							<td nowrap="nowrap">Text</td>
							<td>The criteria of selecting sub-questions when presenting in a test. For example, if you want to define the following criteria:<br/>First criterion:<br/>Score of question to be selected = 2<br/>No. of question required = 1<br/>Second criterion:<br/>Score of question to be selected = 4<br/>No. of question required = 3<br/>Then you should enter: 2(1),4(3)</td>
						</tr>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:when test="$field = 'criteria_for_sc_not_fill'">
				<xsl:choose>
					<xsl:when test="$in_lang = 'zh-cn'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-cn/field[@name='wb_imp_tem_criteria']"/>
							</td>
							<td nowrap="nowrap">否</td>
							<td align="right" nowrap="nowrap">500</td>
							<td nowrap="nowrap">文本</td>
							<td>请保留空白。</td>
						</tr>
					</xsl:when>
					<xsl:when test="$in_lang = 'zh-hk'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-hk/field[@name='wb_imp_tem_criteria']"/>
							</td>
							<td nowrap="nowrap">否</td>
							<td align="right" nowrap="nowrap">500</td>
							<td nowrap="nowrap">文本</td>
							<td>請保留空白。</td>
						</tr>
					</xsl:when>
					<xsl:otherwise>
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/en-us/field[@name='wb_imp_tem_criteria']"/>
							</td>
							<td nowrap="nowrap">N</td>
							<td align="right" nowrap="nowrap">500</td>
							<td nowrap="nowrap">Text</td>
							<td>Do not fill in anything.</td>
						</tr>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<!-- end of Criteria -->
			<!-- Shuffle Option -->
			<xsl:when test="$field = 'shuffle_option_for_sc'">
				<xsl:choose>
					<xsl:when test="$in_lang = 'zh-cn'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-cn/field[@name='wb_imp_tem_shuffle_option']"/>
							</td>
							<td nowrap="nowrap">是</td>
							<td align="right" nowrap="nowrap">无</td>
							<td nowrap="nowrap">文本</td>
							<td>请填写对应的代码：<br/>Y－是<br/>N－否</td>
						</tr>
					</xsl:when>
					<xsl:when test="$in_lang = 'zh-hk'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-hk/field[@name='wb_imp_tem_shuffle_option']"/>
							</td>
							<td nowrap="nowrap">是</td>
							<td align="right" nowrap="nowrap">無</td>
							<td nowrap="nowrap">文本</td>
							<td>請填寫對應的代碼：<br/>Y－是<br/>N－否</td>
						</tr>
					</xsl:when>
					<xsl:otherwise>
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/en-us/field[@name='wb_imp_tem_shuffle_option']"/>
							</td>
							<td nowrap="nowrap">Y</td>
							<td align="right" nowrap="nowrap">N/A</td>
							<td nowrap="nowrap">Text</td>
							<td>Please use the codes below:<br/>Y - Yes<br/>N - No</td>
						</tr>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:when test="$field = 'shuffle_option_for_sc_not_fill'">
				<xsl:choose>
					<xsl:when test="$in_lang = 'zh-cn'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-cn/field[@name='wb_imp_tem_shuffle_option']"/>
							</td>
							<td nowrap="nowrap">是</td>
							<td align="right" nowrap="nowrap">无</td>
							<td nowrap="nowrap">文本</td>
							<td>请保留空白。</td>
						</tr>
					</xsl:when>
					<xsl:when test="$in_lang = 'zh-hk'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-hk/field[@name='wb_imp_tem_shuffle_option']"/>
							</td>
							<td nowrap="nowrap">是</td>
							<td align="right" nowrap="nowrap">無</td>
							<td nowrap="nowrap">文本</td>
							<td>請保留空白。</td>
						</tr>
					</xsl:when>
					<xsl:otherwise>
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/en-us/field[@name='wb_imp_tem_shuffle_option']"/>
							</td>
							<td nowrap="nowrap">Y</td>
							<td align="right" nowrap="nowrap">1</td>
							<td nowrap="nowrap">Text</td>
							<td>Do not fill in anything.</td>
						</tr>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<!-- end of Criteria -->
			<xsl:otherwise/>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="enrol">
		<xsl:param name="in_lang"/>
		<xsl:choose>
			<xsl:when test="$in_lang = 'zh-cn'">
				<table width="95%" align="center" class="instr_table">
					<tr>
						<td class="title" width="100%" colspan="2" nowrap="nowrap">导入报名记录－说明</td>
					</tr>
					<tr>
						<td width="10%" nowrap="nowrap" align="center">1</td>
						<td>根据文件模板填写信息，每一行代表一个报名记录。每个字段的说明请看下面的“<b>字段说明</b>”。</td>
					</tr>
					<tr>
						<td nowrap="nowrap" align="center">2</td>
						<td>登录学习平台并选择“<b>课程管理</b> > <i>
								<b>某个课程</b>
							</i> > <b>处理报名</b> > <b>导入</b>”。只有具有特定管理员权限的用户才有该功能。</td>
					</tr>
					<tr>
						<td nowrap="nowrap" align="center">3</td>
						<td>使用“<b>文件位置</b>”右侧的“<b>浏览</b>...”按钮，指定刚才保存的文件。</td>
					</tr>
					<tr>
						<td nowrap="nowrap" align="center">4</td>
						<td>根据学习平台上的提示去完成操作。</td>
					</tr>
				</table>
				<br/>
				<table width="95%" align="center" class="instr_table">
					<xsl:call-template name="que">
						<xsl:with-param name="in_lang">
							<xsl:value-of select="$cur_lan"/>
						</xsl:with-param>
						<xsl:with-param name="field">field_header</xsl:with-param>
					</xsl:call-template>
					<tr>
						<td nowrap="nowrap">
							<xsl:value-of select="./user_label/zh-cn/field[@id='0']"/>
						</td>
						<td nowrap="nowrap">是</td>
						<td nowrap="nowrap" align="right">
							<xsl:value-of select="$usr_id_min_length"/>-<xsl:value-of select="$usr_id_length"/>
						</td>
						<td nowrap="nowrap">文本</td>
						<td>报名学员的登录名（用户的唯一标识）。如此登录名未能对应学习平台上一个现有的用户，导入时会提示错误。</td>
					</tr>
					<tr>
						<td nowrap="nowrap">
							<xsl:value-of select="./label/zh-cn/field[@name='wb_imp_enr_enrollment_workflow']"/>
						</td>
						<td nowrap="nowrap">是</td>
						<td nowrap="nowrap" align="right">1</td>
						<td nowrap="nowrap">文本</td>
						<td>请填写对应的代码：<br/>Y－所增加的用户将等待所需的审批<br/>N－所增加的用户将越过所有需要的审批过程直接录取</td>
					</tr>
					<tr>
						<td nowrap="nowrap">
							<xsl:value-of select="./label/zh-cn/field[@name='wb_imp_enr_completion_status']"/>*
						</td>
						<td nowrap="nowrap">否</td>
						<td nowrap="nowrap" align="right">无</td>
						<td nowrap="nowrap">文本</td>
						<td>请填写对应的代码：<br/>In Progress－已报名<br/>Completed－已完成<br/>Failed－不合格<br/>Withdrawn－已放弃</td>
					</tr>
					<tr>
						<td nowrap="nowrap">
							<xsl:value-of select="./label/zh-cn/field[@name='wb_imp_enr_completion_date']"/>*
						</td>
						<td nowrap="nowrap">否</td>
						<td nowrap="nowrap" align="right">无</td>
						<td nowrap="nowrap">YYYY-MM-DD hh:mm</td>
						<td>学员获取结训状态的时间。当状态为“已报名”时不适用。</td>
					</tr>
					<tr>
						<td nowrap="nowrap">
							<xsl:value-of select="./label/zh-cn/field[@name='wb_imp_enr_completion_remarks']"/>*
						</td>
						<td nowrap="nowrap">否</td>
						<td nowrap="nowrap" align="right">200</td>
						<td nowrap="nowrap">文本</td>
						<td/>
					</tr>
					<tr>
						<td nowrap="nowrap">
							<xsl:value-of select="./label/zh-cn/field[@name='wb_imp_enr_score']"/>*
						</td>
						<td nowrap="nowrap">否</td>
						<td nowrap="nowrap" align="right">无</td>
						<td nowrap="nowrap">0.0-100.0</td>
						<td/>
					</tr>
					<tr>
						<td nowrap="nowrap">
							<xsl:value-of select="./label/zh-cn/field[@name='wb_imp_enr_attendance']"/>*
						</td>
						<td nowrap="nowrap">否</td>
						<td nowrap="nowrap" align="right">无</td>
						<td nowrap="nowrap">0.0-100.0</td>
						<td nowrap="nowrap">出席率只适用“面授课程”，“离线考试”。</td>
					</tr>
					<tr>
						<td nowrap="nowrap">
							<xsl:value-of select="./label/zh-cn/field[@name='wb_imp_enr_send_mail']"/>*
						</td>
						<td nowrap="nowrap">否</td>
						<td nowrap="nowrap" align="right">1</td>
						<td nowrap="nowrap">文本</td>
						<td>请填写对应的代码：<br/>Y－发送报名通知邮件<br/>N－不发送报名通知邮件<br/>默认：当前课程的“发送报名通知邮件”属性</td>
					</tr>
				</table>
				<table width="95%" align="center">
					<tr>
						<td>* 此字段只有当“报名工作流”为“N”时才適用，否则会被忽略。</td>
					</tr>
				</table>
			</xsl:when>
			<xsl:when test="$in_lang = 'zh-hk'">
				<table width="95%" align="center" class="instr_table">
					<tr>
						<td class="title" width="100%" colspan="2" nowrap="nowrap">匯入報名記錄－說明</td>
					</tr>
					<tr>
						<td width="10%" nowrap="nowrap" align="center">1</td>
						<td>根據文檔範本填寫資訊，每一行代表一個報名記錄。每個欄位的說明請看下面的“<b>欄位說明</b>”。</td>
					</tr>
					<tr>
						<td nowrap="nowrap" align="center">2</td>
						<td>登錄學習平台並選擇“<b>課程管理</b> > <i>
								<b>某個課程</b>
							</i> > <b>處理報名</b> > <b>匯入</b>”。只有具有特定管理員許可權的用戶才有該功能。</td>
					</tr>
					<tr>
						<td nowrap="nowrap" align="center">3</td>
						<td>使用“<b>文檔位置</b>”右側的“<b>瀏覽</b>...”按鈕，指定剛才保存的文檔。</td>
					</tr>
					<tr>
						<td nowrap="nowrap" align="center">4</td>
						<td colspan="4">根據學習平台上的提示去完成操作。</td>
					</tr>
				</table>
				<br/>
				<table width="95%" align="center" class="instr_table">
					<xsl:call-template name="que">
						<xsl:with-param name="in_lang">
							<xsl:value-of select="$cur_lan"/>
						</xsl:with-param>
						<xsl:with-param name="field">field_header</xsl:with-param>
					</xsl:call-template>
					<tr>
						<td nowrap="nowrap">
							<xsl:value-of select="./user_label/zh-hk/field[@id='0']"/>
						</td>
						<td nowrap="nowrap">是</td>
						<td nowrap="nowrap" align="right">
							<xsl:value-of select="$usr_id_min_length"/>-<xsl:value-of select="$usr_id_length"/>
						</td>
						<td nowrap="nowrap">文本</td>
						<td>報名學員的登錄名（用戶的唯一標識）。如此登錄名未能對應學習平台上一個現有的用戶，匯入時會提示錯誤。</td>
					</tr>
					<tr>
						<td nowrap="nowrap">
							<xsl:value-of select="./label/zh-hk/field[@name='wb_imp_enr_enrollment_workflow']"/>
						</td>
						<td nowrap="nowrap">是</td>
						<td nowrap="nowrap" align="right">1</td>
						<td nowrap="nowrap">文本</td>
						<td>請填寫對應的代碼：<br/>Y－所增加的用戶將等待所需的審批<br/>N－所增加的用戶將越過所有需要的審批過程直接錄取</td>
					</tr>
					<tr>
						<td nowrap="nowrap">
							<xsl:value-of select="./label/zh-hk/field[@name='wb_imp_enr_completion_status']"/>*
						</td>
						<td nowrap="nowrap">否</td>
						<td nowrap="nowrap" align="right">無</td>
						<td nowrap="nowrap">文本</td>
						<td>請填寫對應的代碼：<br/>In Progress－已報名<br/>Completed－已完成<br/>Failed－不合格<br/>Withdrawn－已放棄</td>
					</tr>
					<tr>
						<td nowrap="nowrap">
							<xsl:value-of select="./label/zh-hk/field[@name='wb_imp_enr_completion_date']"/>*
						</td>
						<td nowrap="nowrap">否</td>
						<td nowrap="nowrap" align="right">無</td>
						<td nowrap="nowrap">YYYY-MM-DD hh:mm</td>
						<td>學員獲取結訓狀態的時間。當狀態為“已報名”時不適用。</td>
					</tr>
					<tr>
						<td nowrap="nowrap">
							<xsl:value-of select="./label/zh-hk/field[@name='wb_imp_enr_completion_remarks']"/>*
						</td>
						<td nowrap="nowrap">否</td>
						<td nowrap="nowrap" align="right">200</td>
						<td nowrap="nowrap">文本</td>
						<td/>
					</tr>
					<tr>
						<td nowrap="nowrap">
							<xsl:value-of select="./label/zh-hk/field[@name='wb_imp_enr_score']"/>*
						</td>
						<td nowrap="nowrap">否</td>
						<td nowrap="nowrap" align="right">無</td>
						<td nowrap="nowrap">0.0-100.0</td>
						<td/>
					</tr>
					<tr>
						<td nowrap="nowrap">
							<xsl:value-of select="./label/zh-hk/field[@name='wb_imp_enr_attendance']"/>*
						</td>
						<td nowrap="nowrap">否</td>
						<td nowrap="nowrap" align="right">無</td>
						<td nowrap="nowrap">0.0-100.0</td>
						<td nowrap="nowrap">出席率只適用“面授課程”，“離線考試”。</td>
						<td/>
					</tr>
					<tr>
						<td nowrap="nowrap">
							<xsl:value-of select="./label/zh-hk/field[@name='wb_imp_enr_send_mail']"/>*
						</td>
						<td nowrap="nowrap">否</td>
						<td nowrap="nowrap" align="right">1</td>
						<td nowrap="nowrap">文本</td>
						<td>請填寫對應的代碼：<br/>Y－發送報名通知郵件<br/>N－不發送報名通知郵件<br/>默認：當前課程的“發送報名通知郵件”屬性</td>
					</tr>
				</table>
				<table width="95%" align="center">
					<tr>
						<td>* 此欄位只有當“報名流程”為“N”時才適用，否則會被忽略</td>
					</tr>
				</table>
			</xsl:when>
			<xsl:when test="$in_lang = 'en-us'">
				<table width="95%" align="center" class="instr_table">
					<tr>
						<td class="title" width="100%" colspan="2" nowrap="nowrap">Instruction for importing enrollment</td>
					</tr>
					<tr>
						<td width="10%" nowrap="nowrap" align="center">1</td>
						<td>Fill in the data according to the template. Each row represents the data of a single record of enrollment. See the <b>Field description</b> below for full description of the fields.</td>
					</tr>
					<tr>
						<td nowrap="nowrap" align="center">2</td>
						<td>Login to the learning platform and go to <b>Learning Solution Management</b> > <i>
								<b>select a course</b>
							</i> > <b>Enrollment</b> > <b>Import</b>. This function is available only to users with specific administrative user role.</td>
					</tr>
					<tr>
						<td nowrap="nowrap" align="center">3</td>
						<td>Upload the saved file using the <b>Browse</b>... button to the right of <b>File location</b>.</td>
					</tr>
					<tr>
						<td nowrap="nowrap" align="center">4</td>
						<td>There are instructions in the learning platform to guide you through the process.</td>
					</tr>
				</table>
				<br/>
				<table width="95%" align="center" class="instr_table">
					<xsl:call-template name="que">
						<xsl:with-param name="in_lang">
							<xsl:value-of select="$cur_lan"/>
						</xsl:with-param>
						<xsl:with-param name="field">field_header</xsl:with-param>
					</xsl:call-template>
					<tr>
						<td nowrap="nowrap">
							<xsl:value-of select="./user_label/en-us/field[@id='0']"/>
						</td>
						<td nowrap="nowrap">Y</td>
						<td nowrap="nowrap" align="right">
							<xsl:value-of select="$usr_id_min_length"/>-<xsl:value-of select="$usr_id_length"/>
						</td>
						<td nowrap="nowrap">Text</td>
						<td>User ID of the learner. It should match with the login ID of existing users; otherwise, the system will consider it an invalid record during the upload process.</td>
					</tr>
					<tr>
						<td nowrap="nowrap">
							<xsl:value-of select="./label/en-us/field[@name='wb_imp_enr_enrollment_workflow']"/>
						</td>
						<td nowrap="nowrap">Y</td>
						<td nowrap="nowrap" align="right">1</td>
						<td nowrap="nowrap">Text</td>
						<td>Please use the codes below:<br/>Y - The enrolled learner can start learning only after being approved.<br/>N - The enrolled learner can start learning immediately.</td>
					</tr>
					<tr>
						<td nowrap="nowrap">
							<xsl:value-of select="./label/en-us/field[@name='wb_imp_enr_completion_status']"/>*
						</td>
						<td nowrap="nowrap">N</td>
						<td nowrap="nowrap" align="right">N/A</td>
						<td nowrap="nowrap">Text</td>
						<td>Please use the codes below:<br/>In Progress<br/>Completed<br/>Failed<br/>Withdrawn</td>
					</tr>
					<tr>
						<td nowrap="nowrap">
							<xsl:value-of select="./label/en-us/field[@name='wb_imp_enr_completion_date']"/>*
						</td>
						<td nowrap="nowrap">N</td>
						<td nowrap="nowrap" align="right">N/A</td>
						<td nowrap="nowrap">YYYY-MM-DD hh:mm</td>
						<td>The date and time when the learner attained the learning status. Note this field does not logically apply to enrolled status.</td>
					</tr>
					<tr>
						<td nowrap="nowrap">
							<xsl:value-of select="./label/en-us/field[@name='wb_imp_enr_completion_remarks']"/>*
						</td>
						<td nowrap="nowrap">N</td>
						<td nowrap="nowrap" align="right">200</td>
						<td nowrap="nowrap">Text</td>
						<td>The remarks of the learner's completion, if any.</td>
					</tr>
					<tr>
						<td nowrap="nowrap">
							<xsl:value-of select="./label/en-us/field[@name='wb_imp_enr_score']"/>*
						</td>
						<td nowrap="nowrap">N</td>
						<td nowrap="nowrap" align="right">N/A</td>
						<td nowrap="nowrap">0.0-100.0</td>
						<td>The score the learner obtained, if any.</td>
					</tr>
					<tr>
						<td nowrap="nowrap">
							<xsl:value-of select="./label/en-us/field[@name='wb_imp_enr_attendance']"/>*
						</td>
						<td nowrap="nowrap">N</td>
						<td nowrap="nowrap" align="right">N/A</td>
						<td nowrap="nowrap">0.0-100.0</td>
						<td nowrap="nowrap">Attendance only for Classroom Course and Classroom Exam.</td>
					</tr>
					<tr>
						<td nowrap="nowrap">
							<xsl:value-of select="./label/en-us/field[@name='wb_imp_enr_send_mail']"/>*
						</td>
						<td nowrap="nowrap">N</td>
						<td nowrap="nowrap" align="right">1</td>
						<td nowrap="nowrap">Text</td>
						<td>Please use the codes below:<br/>Y - enrollment admission email will be sent.<br/>N - enrollment admission email will not be sent.<br/>Default: revert to the value specified in ""Send enrollment notification email"" of the course."</td>
					</tr>
				</table>
				<table width="95%" align="center">
					<tr>
						<td>* This field is only effective when enrollment workflow is set as N. Otherwise this field is ignored.</td>
					</tr>
				</table>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
	<!-- instr for import user =============================================================== -->
	<xsl:template name="user">
		<xsl:param name="in_lang"/>
		<xsl:choose>
			<xsl:when test="$in_lang = 'zh-cn'">
				<table width="95%" align="center" class="instr_table">
					<tr>
						<td class="title" width="100%" colspan="2" nowrap="nowrap">导入用户信息－说明</td>
					</tr>
					<tr>
						<td nowrap="nowrap" align="center" width="10%">1</td>
						<td>根据文件模板填写用户信息，每一行代表一个用户的信息。每个字段的说明请看下面的“字段说明”。</td>
					</tr>
					<tr>
						<td nowrap="nowrap" align="center">2</td>
						<td>登录学习平台并选择“<b>用户管理 > 用户信息 > 批量删除用户</b>”。只有具有特定管理员权限的用户才有该功能。</td>
					</tr>
					<tr>
						<td nowrap="nowrap" align="center">3</td>
						<td>使用“<b>文件位置</b>”右侧的“<b>浏览</b>”按钮，指定刚才填写完成的文件。</td>
					</tr>
					<tr>
						<td nowrap="nowrap" align="center">4</td>
						<td>根据学习平台上的提示去完成操作。</td>
					</tr>
				</table>
				<br/>
				<table width="95%" align="center" class="instr_table">
					<xsl:call-template name="que">
						<xsl:with-param name="in_lang">
							<xsl:value-of select="$cur_lan"/>
						</xsl:with-param>
						<xsl:with-param name="field">field_header</xsl:with-param>
					</xsl:call-template>
					
					<xsl:for-each select="./import_label/zh-cn/label">
						<xsl:call-template name="import_detail">
							<xsl:with-param name="label" select="@name"/>
							<xsl:with-param name="lang" select="$cur_lan"/>
							<xsl:with-param name="id" select="@id"/>
							<xsl:with-param name="minLength" select="@minlength"/>	
							<xsl:with-param name="maxLength" select="@maxlength"/>
							<xsl:with-param name="allowEmpty" select="@allowEmpty"/>
							<xsl:with-param name="isTcIndependent"  select="$isTcIndependent"/>	
						</xsl:call-template>
					</xsl:for-each>
						
				</table>
			</xsl:when>
			<xsl:when test="$in_lang = 'zh-hk'">
				<table width="95%" align="center" class="instr_table">
					<tr>
						<td class="title" width="100%" colspan="2" nowrap="nowrap">匯入用户資訊－說明</td>
					</tr>
					<tr>
						<td nowrap="nowrap" align="center" width="10%">1</td>
						<td>根據文檔範本填寫用戶資訊，每一行代表一個用戶的資訊。每個欄位的說明請看下面的“欄位說明”。</td>
					</tr>
					<tr>
						<td nowrap="nowrap" align="center">2</td>
						<td>登錄學習平台並選擇“<b>用戶管理 > 用戶資訊 > 批量刪除用戶</b>”。只有具有特定管理員許可權的用戶才有該功能。</td>
					</tr>
					<tr>
						<td nowrap="nowrap" align="center">3</td>
						<td>使用“<b>文檔位置</b>”右側的“<b>瀏覽</b>”按鈕，指定剛才填寫完成的文件。</td>
					</tr>
					<tr>
						<td nowrap="nowrap" align="center">4</td>
						<td>根據學習平台上的提示去完成操作。</td>
					</tr>
				</table>
				<br/>
				<table width="95%" align="center" class="instr_table">
					<xsl:call-template name="que">
						<xsl:with-param name="in_lang">
							<xsl:value-of select="$cur_lan"/>
						</xsl:with-param>
						<xsl:with-param name="field">field_header</xsl:with-param>
					</xsl:call-template>
					<xsl:for-each select="./import_label/zh-hk/label">
						<xsl:call-template name="import_detail">
							<xsl:with-param name="label" select="@name"/>
							<xsl:with-param name="lang" select="$cur_lan"/>
							<xsl:with-param name="id" select="@id"/>
							<xsl:with-param name="minLength" select="@minlength"/>	
							<xsl:with-param name="maxLength" select="@maxlength"/>
							<xsl:with-param name="allowEmpty" select="@allowEmpty"/>	
							<xsl:with-param name="isTcIndependent"  select="$isTcIndependent"/>
						</xsl:call-template>
					</xsl:for-each>
				</table>
			</xsl:when>
			<xsl:when test="$in_lang = 'en-us'">
				<table width="95%" align="center" class="instr_table">
					<tr>
						<td class="title" width="100%" colspan="2" nowrap="nowrap">Instruction for batch delete user</td>
					</tr>
					<tr>
						<td nowrap="nowrap" align="center" width="10%">1</td>
						<td>Fill in the data according to the template. Each row represents the data of a single user profile. See the field description below for full description of the fields.</td>
					</tr>
					<tr>
						<td nowrap="nowrap" align="center">2</td>
						<td>Login to the learning platform and go to <b>User Management > User profile > Batch delete user.</b> This function is available only to users with specific administrative user role.</td>
					</tr>
					<tr>

						<td nowrap="nowrap" align="center">3</td>
						<td>Upload the saved file using the <b>Browse</b> button to the right of <b>File location.</b>
						</td>
					</tr>
					<tr>
						<td nowrap="nowrap" align="center">4</td>
						<td>There are instructions in the learning platform to guide you through the process.</td>
					</tr>
				</table>
				<br/>
				<table width="95%" align="center" class="instr_table">
					<xsl:call-template name="que">
						<xsl:with-param name="in_lang">
							<xsl:value-of select="$cur_lan"/>
						</xsl:with-param>
						<xsl:with-param name="field">field_header</xsl:with-param>
					</xsl:call-template>
					<xsl:for-each select="./import_label/en-us/label">
						<xsl:call-template name="import_detail">
							<xsl:with-param name="label" select="@name"/>
							<xsl:with-param name="lang" select="$cur_lan"/>
							<xsl:with-param name="id" select="@id"/>
							<xsl:with-param name="minLength" select="@minlength"/>	
							<xsl:with-param name="maxLength" select="@maxlength"/>
							<xsl:with-param name="allowEmpty" select="@allowEmpty"/>	
							<xsl:with-param name="isTcIndependent"  select="$isTcIndependent"/>
						</xsl:call-template>
					</xsl:for-each>
				</table>
			</xsl:when>
		</xsl:choose>
		<br/>
	</xsl:template>
	<!-- instr for import credit =============================================================== -->
	<xsl:template name="credit">
		<xsl:param name="in_lang"/>
		<xsl:choose>
			<xsl:when test="$in_lang = 'zh-cn'">
				<table width="95%" align="center" class="instr_table">
					<tr>
						<td class="title" width="100%" colspan="2" nowrap="nowrap">导入积分信息－说明</td>
					</tr>
					<tr>
						<td nowrap="nowrap" align="center" width="10%">1</td>
						<td>根据文件模板填写积分信息，每一行代表一个积分的信息。每个字段的说明请看下面的“字段说明”。</td>
					</tr>
					<tr>
						<td nowrap="nowrap" align="center">5</td>
						<td>登录学习平台并选择“<b>积分管理</b> > <b>设置学员积分</b> > <b>导入</b>”。只有具有特定管理员权限的用户才有该功能。</td>
					</tr>
					<tr>
						<td nowrap="nowrap" align="center">6</td>
						<td>使用“<b>文件位置</b>”右侧的“<b>浏览</b>...”按钮，指定刚才另存出来的文件。</td>
					</tr>
					<tr>
						<td nowrap="nowrap" align="center">7</td>
						<td>根据学习平台上的提示去完成操作。</td>
					</tr>
				</table>
				<br/>
				<table width="95%" align="center" class="instr_table">
					<xsl:call-template name="que">
						<xsl:with-param name="in_lang">
							<xsl:value-of select="$cur_lan"/>
						</xsl:with-param>
						<xsl:with-param name="field">field_header</xsl:with-param>
					</xsl:call-template>
					<tr>
						<td nowrap="nowrap">
							<xsl:value-of select="./user_label/zh-cn/field[@id='0']"/>
						</td>
						<td nowrap="nowrap">是</td>
						<td nowrap="nowrap" align="right">
							<xsl:value-of select="$usr_id_min_length"/>-<xsl:value-of select="$usr_id_length"/>
						</td>
						<td nowrap="nowrap">只能包含：纯英文字母、数字、<br/>下划线(_)、横线(-)</td>
						<td>必须是学习平台上现有的用户。</td>
					</tr>
					<tr>
						<td nowrap="nowrap">积分操作类型</td>
						<td nowrap="nowrap">是</td>
						<td nowrap="nowrap" align="right">
						</td>
						<td nowrap="nowrap">只能输入"A"或"D"</td>
						<td>“A”代表加分，“D”代表扣分</td>
					</tr>
					<tr>
						<td nowrap="nowrap">积分(扣分)点名称</td>
						<td>是</td>
						<td align="right"></td>
						<td>文本</td>
						<td>必须跟平台里的手工积分（扣分）点的名称一样</td>
					</tr>
					<tr>
						<td nowrap="nowrap">分数</td>
						<td>是</td>
						<td align="right"></td>
						<td>999.99</td>
						<td>要积分（扣分）的分值</td>
					</tr>
				</table>
			</xsl:when>
			<xsl:when test="$in_lang = 'zh-hk'">
				<table width="95%" align="center" class="instr_table">
					<tr>
						<td class="title" width="100%" colspan="2" nowrap="nowrap">導入積分信息－說明</td>
					</tr>
					<tr>
						<td nowrap="nowrap" align="center" width="10%">1</td>
						<td>根據文件模板填寫積分信息，每一行代表一個積分的信息。每個字段的說明請看下面的「字段說明」。</td>
					</tr>
					<tr>
						<td nowrap="nowrap" align="center">5</td>
						<td>登錄學習平台並選擇「<b>積分管理</b> > <b>設置學員積分</b> > <b>導入</b>」。只有具有特定管理員權限的用戶才有該功能。</td>
					</tr>
					<tr>
						<td nowrap="nowrap" align="center">6</td>
						<td>使用「<b>文件位置</b>」右側的「<b>瀏覽</b>...」按鈕，指定剛才另存出來的文件。</td>
					</tr>
					<tr>
						<td nowrap="nowrap" align="center">7</td>
						<td>根據學習平台上的提示去完成操作。</td>
					</tr>
				</table>
				<br/>
				<table width="95%" align="center" class="instr_table">
					<xsl:call-template name="que">
						<xsl:with-param name="in_lang">
							<xsl:value-of select="$cur_lan"/>
						</xsl:with-param>
						<xsl:with-param name="field">field_header</xsl:with-param>
					</xsl:call-template>
					<tr>
						<td nowrap="nowrap">
							<xsl:value-of select="./user_label/zh-cn/field[@id='0']"/>
						</td>
						<td nowrap="nowrap">是</td>
						<td nowrap="nowrap" align="right">
							<xsl:value-of select="$usr_id_min_length"/>-<xsl:value-of select="$usr_id_length"/>
						</td>
						<td nowrap="nowrap">只能包含：純英文小寫字母、數字、<br/>下劃線(_)、橫線(-)</td>
						<td>>必須是學習平台上現有的用戶。</td>
					</tr>
					<tr>
						<td nowrap="nowrap">積分操作類型</td>
						<td nowrap="nowrap">是</td>
						<td nowrap="nowrap" align="right">
						</td>
						<td nowrap="nowrap">只能輸入"A"或"D"</td>
						<td>「A」代表加分，「D」代表扣分</td>
					</tr>
					<tr>
						<td nowrap="nowrap">積分(扣分)點名稱</td>
						<td>是</td>
						<td align="right"></td>
						<td>文本</td>
						<td>必須跟平台裡的手工積分（扣分）點的名稱一樣</td>
					</tr>
					<tr>
						<td nowrap="nowrap">分數</td>
						<td>是</td>
						<td align="right"></td>
						<td>999.99</td>
						<td>要積分（扣分）的分值</td>
					</tr>
				</table>
			</xsl:when>
			<xsl:when test="$in_lang = 'en-us'">
				<table width="95%" align="center" class="instr_table">
					<tr>
						<td class="title" width="100%" colspan="2" nowrap="nowrap">Instruction for importing credits</td>
					</tr>
					<tr>
						<td nowrap="nowrap" align="center" width="10%">1</td>
						<td>Fill in the data according to the template. Each row represents the data of a credit. See the <b>Field description</b> below for full description of the fields. </td>
					</tr>
					<tr>
						<td nowrap="nowrap" align="center">5</td>
						<td>Login to the learning platform and go to <b>Points Management</b> > <b>Setting manual points</b> > <b>Import</b> This function is available only to users with specific administrative user role. </td>
					</tr>
					<tr>
						<td nowrap="nowrap" align="center">6</td>
						<td>Upload the saved file using the <b>Browse...</b> button to the right of <b>File location. </b></td>
					</tr>
					<tr>
						<td nowrap="nowrap" align="center">7</td>
						<td>There are instructions in the learning platform to guide you through the process. </td>
					</tr>
				</table>
				<br/>
				<table width="95%" align="center" class="instr_table">
					<xsl:call-template name="que">
						<xsl:with-param name="in_lang">
							<xsl:value-of select="$cur_lan"/>
						</xsl:with-param>
						<xsl:with-param name="field">field_header</xsl:with-param>
					</xsl:call-template>
					<tr>
						<td nowrap="nowrap">
							<xsl:value-of select="./user_label/en-us/field[@id='0']"/>
						</td>
						<td nowrap="nowrap">Y</td>
						<td nowrap="nowrap" align="right">
							<xsl:value-of select="$usr_id_min_length"/>-<xsl:value-of select="$usr_id_length"/>
						</td>
						<td nowrap="nowrap">Lower case alphabets, numbers, <br/>underscore (_) and hyphen (-)</td>
						<td>Current platform users required. </td>
					</tr>
					<tr>
						<td nowrap="nowrap">Credit operational type</td>
						<td nowrap="nowrap">Y</td>
						<td nowrap="nowrap" align="right">
						</td>
						<td nowrap="nowrap">Only A or D can be input</td>
						<td>A represents credits accumulation while D means credit deduction. </td>
					</tr>
					<tr>
						<td nowrap="nowrap">Credit accumulation(deduction) name</td>
						<td>Y</td>
						<td align="right"></td>
						<td>Text</td>
						<td>Current platform credits required. </td>
					</tr>
					<tr>
						<td nowrap="nowrap">Score</td>
						<td>Y</td>
						<td align="right"></td>
						<td>999.99</td>
						<td>Amount of credit accumulation/deduction. </td>
					</tr>
				</table>
			</xsl:when>
		</xsl:choose>
		<br/>
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>
