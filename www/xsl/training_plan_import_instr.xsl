<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<!-- =============================================================== -->
	<xsl:variable name="cur_lan" select="/Upload/instr/@cur_lang"/>
	<xsl:variable name="instr_type" select="/Upload/instr/@type"/>
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
				<xsl:when test="$instr_type = 'tpplan'">
					<xsl:call-template name="tpplan">
						<xsl:with-param name="in_lang">
							<xsl:value-of select="$cur_lan"/>
						</xsl:with-param>
					</xsl:call-template>
				</xsl:when>
			</xsl:choose>
			<br/>
		</body>
	</xsl:template>
	<!-- template mc  =============================================================== -->
	<xsl:template name="tpplan">
		<xsl:param name="in_lang"/>
		<table class="instr_table" width="95%" align="center">
			<xsl:choose>
				<xsl:when test="$in_lang = 'zh-cn'">
					<xsl:call-template name="gen_row">
						<xsl:with-param name="in_lang">
							<xsl:value-of select="$cur_lan"/>
						</xsl:with-param>
						<xsl:with-param name="field">type_desc</xsl:with-param>
						<xsl:with-param name="title">导入年度计划－说明</xsl:with-param>
					</xsl:call-template>
				</xsl:when>
				<xsl:when test="$in_lang = 'zh-hk'">
					<xsl:call-template name="gen_row">
						<xsl:with-param name="in_lang">
							<xsl:value-of select="$cur_lan"/>
						</xsl:with-param>
						<xsl:with-param name="field">type_desc</xsl:with-param>
						<xsl:with-param name="title">導入年度計劃－說明</xsl:with-param>
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="gen_row">
						<xsl:with-param name="in_lang">
							<xsl:value-of select="$cur_lan"/>
						</xsl:with-param>
						<xsl:with-param name="field">type_desc</xsl:with-param>
						<xsl:with-param name="title">Instruction for importing training plan</xsl:with-param>
					</xsl:call-template>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:call-template name="gen_row">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">type_intr</xsl:with-param>
			</xsl:call-template>
		</table>
		<br/>
		<table class="instr_table" width="95%" align="center">
			<xsl:call-template name="gen_row">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">field_header</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="gen_row">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">wb_imp_tp_plan_date</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="gen_row">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">wb_imp_tp_plan_name</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="gen_row">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">wb_imp_tp_plan_type</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="gen_row">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">wb_imp_tp_plan_tnd</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="gen_row">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">wb_imp_tp_plan_intr</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="gen_row">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">wb_imp_tp_plan_aim</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="gen_row">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">wb_imp_tp_plan_target</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="gen_row">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">wb_imp_tp_plan_responser</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="gen_row">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">wb_imp_tp_plan_duration</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="gen_row">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">wb_imp_tp_plan_ftf_start_time</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="gen_row">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">wb_imp_tp_plan_ftf_end_time</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="gen_row">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">wb_imp_tp_plan_wb_start_time</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="gen_row">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">wb_imp_tp_plan_wb_end_time</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="gen_row">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">wb_imp_tp_plan_lrn_count</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="gen_row">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">wb_imp_tp_plan_fee</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="gen_row">
				<xsl:with-param name="in_lang">
					<xsl:value-of select="$cur_lan"/>
				</xsl:with-param>
				<xsl:with-param name="field">wb_imp_tp_plan_remark</xsl:with-param>
			</xsl:call-template>
		</table>
		<br/>
		<table width="95%" align="center" class="instr_table">
		</table>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="gen_row">
		<xsl:param name="in_lang"/>
		<xsl:param name="field"/>
		<xsl:param name="type"/>
		<xsl:param name="title"/>
		<xsl:param name="sub_title"/>
		<xsl:choose>
			<!--  desc -->
			<xsl:when test="$field = 'type_desc'">
				<tr>
					<td class="title" width="100%" colspan="2" nowrap="nowrap">
						<xsl:value-of select="$title"/>
					</td>
				</tr>
			</xsl:when>
			<xsl:when test="$field = 'type_intr'">
				<xsl:choose>
					<xsl:when test="$in_lang = 'zh-cn'">
						<tr>
							<td align="center" nowrap="nowrap" width="10%">1</td>
							<td>根据文件模板填写信息，每一行代表一个年度计划。每个字段的说明请看下面的“<b>字段说明</b>”。</td>
						</tr>
						<tr>
							<td align="center" nowrap="nowrap">2</td>
							<td>登录学习平台并选择“<b>年度计划</b> &gt; <b>添加</b>”。只有具有特定管理员权限的用户才有该功能。</td>
						</tr>
						<tr>
							<td align="center" nowrap="nowrap">3</td>
							<td>根据学习平台上的提示去完成操作。</td>
						</tr>
					</xsl:when>
					<xsl:when test="$in_lang = 'zh-hk'">
						<tr>
							<td align="center" nowrap="nowrap" width="10%">1</td>
							<td>根據文件模板填寫信息，每一行代表一個年度計劃。每個字段的說明請看下面的"<b>字段說明</b>"。</td>
						</tr>
						<tr>
							<td align="center" nowrap="nowrap">2</td>
							<td>登入學習平台並選擇"<b>年度計劃</b> &gt; <b>添加</b>"。只有具有特定管理員權限的用戶才有該功能。</td>
						</tr>
						<tr>
							<td align="center" nowrap="nowrap">3</td>
							<td>根據學習平台上的提示去完成操作。</td>
						</tr>
					</xsl:when>
					<xsl:otherwise>
						<tr>
							<td align="center" nowrap="nowrap" width="10%">1</td>
							<td>Fill in the data according to the template. Each row represents the data of a single training plan. See the <b>Field description</b> below for full description of the fields.</td>
						</tr>
						<tr>
							<td align="center" nowrap="nowrap">2</td>
							<td>Login to the learning platform and select <b>Annual training plan</b> &gt; <b>Add</b>. This function is available only to users with specific administrative user role.</td>
						</tr>
						<tr>
							<td align="center" nowrap="nowrap">3</td>
							<td>There are instructions in the learning platform to guide you through the process.</td>
						</tr>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<!--  Header -->
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
							<td class="sub_title" nowrap="nowrap" width="160">欄位名</td>
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
			<!-- wb_imp_tp_plan_date -->
			<xsl:when test="$field = 'wb_imp_tp_plan_date'">
				<xsl:choose>
					<xsl:when test="$in_lang = 'zh-cn'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-cn/field[@name='wb_imp_tp_plan_date']"/>
							</td>
							<td nowrap="nowrap">是</td>
							<td align="right" nowrap="nowrap">2</td>
							<td nowrap="nowrap">MM</td>
							<td>培训的推出月份</td>
						</tr>
					</xsl:when>
					<xsl:when test="$in_lang = 'zh-hk'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-hk/field[@name='wb_imp_tp_plan_date']"/>
							</td>
							<td nowrap="nowrap">是</td>
							<td align="right" nowrap="nowrap">2</td>
							<td nowrap="nowrap">MM</td>
							<td>培訓的推出月份</td>
						</tr>
					</xsl:when>
					<xsl:otherwise>
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/en-us/field[@name='wb_imp_tp_plan_date']"/>
							</td>
							<td nowrap="nowrap">Y</td>
							<td align="right" nowrap="nowrap">2</td>
							<td nowrap="nowrap">MM</td>
							<td>The month in which this training will be implemented</td>
						</tr>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<!-- wb_imp_tp_plan_name -->
			<xsl:when test="$field = 'wb_imp_tp_plan_name'">
				<xsl:choose>
					<xsl:when test="$in_lang = 'zh-cn'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-cn/field[@name='wb_imp_tp_plan_name']"/>
							</td>
							<td nowrap="nowrap">是</td>
							<td align="right" nowrap="nowrap">200</td>
							<td nowrap="nowrap">文字</td>
							<td>培训活动名称</td>
						</tr>
					</xsl:when>
					<xsl:when test="$in_lang = 'zh-hk'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-hk/field[@name='wb_imp_tp_plan_name']"/>
							</td>
							<td nowrap="nowrap">是</td>
							<td align="right" nowrap="nowrap">200</td>
							<td nowrap="nowrap">文字</td>
							<td>培訓活動名稱</td>
						</tr>
					</xsl:when>
					<xsl:otherwise>
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/en-us/field[@name='wb_imp_tp_plan_name']"/>
							</td>
							<td nowrap="nowrap">Y</td>
							<td align="right" nowrap="nowrap">200</td>
							<td nowrap="nowrap">Text</td>
							<td>Training Title</td>
						</tr>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<!-- wb_imp_tp_plan_type -->
			<xsl:when test="$field = 'wb_imp_tp_plan_type'">
				<xsl:choose>
					<xsl:when test="$in_lang = 'zh-cn'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-cn/field[@name='wb_imp_tp_plan_type']"/>
							</td>
							<td nowrap="nowrap">是</td>
							<td align="right" nowrap="nowrap">255</td>
							<td nowrap="nowrap">文字</td>
							<td>
								请填写以下类型：<br/>							
								网上课程<br/>
								面授课程<br/>
								网上考试<br/>
								离线考试<br/>
								项目式培训
							</td>
						</tr>
					</xsl:when>
					<xsl:when test="$in_lang = 'zh-hk'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-hk/field[@name='wb_imp_tp_plan_type']"/>
							</td>
							<td nowrap="nowrap">是</td>
							<td align="right" nowrap="nowrap">255</td>
							<td nowrap="nowrap">文字</td>
							<td>
								請填寫以下類型：<br/>							
								網上課程<br/>
								面授課程<br/>
								網上考試<br/>
								離線考試<br/>
								項目式培訓
							</td>
						</tr>
					</xsl:when>
					<xsl:otherwise>
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/en-us/field[@name='wb_imp_tp_plan_type']"/>
							</td>
							<td nowrap="nowrap">Y</td>
							<td align="right" nowrap="nowrap">255</td>
							<td nowrap="nowrap">Text</td>
							<td>
								Please use one of the following:<br/>							
								Web-based Course<br/>
								Blended Course<br/>
								Web-based Exam<br/>
								Blended Exam<br/>
								Integrated Learning
							</td>
						</tr>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<!-- wb_imp_tp_plan_tnd -->
			<xsl:when test="$field = 'wb_imp_tp_plan_tnd'">
				<xsl:choose>
					<xsl:when test="$in_lang = 'zh-cn'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-cn/field[@name='wb_imp_tp_plan_tnd']"/>
							</td>
							<td nowrap="nowrap">否</td>
							<td align="right" nowrap="nowrap">100</td>
							<td nowrap="nowrap">文字</td>
							<td>课程目录里面选择</td>
						</tr>
					</xsl:when>
					<xsl:when test="$in_lang = 'zh-hk'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-hk/field[@name='wb_imp_tp_plan_tnd']"/>
							</td>
							<td nowrap="nowrap">否</td>
							<td align="right" nowrap="nowrap">100</td>
							<td nowrap="nowrap">文字</td>
							<td>課程目錄裡面選擇</td>
						</tr>
					</xsl:when>
					<xsl:otherwise>
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/en-us/field[@name='wb_imp_tp_plan_tnd']"/>
							</td>
							<td nowrap="nowrap">N</td>
							<td align="right" nowrap="nowrap">100</td>
							<td nowrap="nowrap">Text</td>
							<td>Please select from the training categories</td>
						</tr>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<!-- wb_imp_tp_plan_intr -->
			<xsl:when test="$field = 'wb_imp_tp_plan_intr'">
				<xsl:choose>
					<xsl:when test="$in_lang = 'zh-cn'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-cn/field[@name='wb_imp_tp_plan_intr']"/>
							</td>
							<td nowrap="nowrap">否</td>
							<td align="right" nowrap="nowrap">4000</td>
							<td nowrap="nowrap">文字</td>
							<td>简介</td>
						</tr>
					</xsl:when>
					<xsl:when test="$in_lang = 'zh-hk'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-hk/field[@name='wb_imp_tp_plan_intr']"/>
							</td>
							<td nowrap="nowrap">否</td>
							<td align="right" nowrap="nowrap">4000</td>
							<td nowrap="nowrap">文字</td>
							<td>簡介</td>
						</tr>
					</xsl:when>
					<xsl:otherwise>
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/en-us/field[@name='wb_imp_tp_plan_intr']"/>
							</td>
							<td nowrap="nowrap">N</td>
							<td align="right" nowrap="nowrap">4000</td>
							<td nowrap="nowrap">Text</td>
							<td>Summary</td>
						</tr>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<!-- wb_imp_tp_plan_aim -->
			<xsl:when test="$field = 'wb_imp_tp_plan_aim'">
				<xsl:choose>
					<xsl:when test="$in_lang = 'zh-cn'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-cn/field[@name='wb_imp_tp_plan_aim']"/>
							</td>
							<td nowrap="nowrap">否</td>
							<td align="right" nowrap="nowrap">4000</td>
							<td nowrap="nowrap">文字</td>
							<td>目标</td>
						</tr>
					</xsl:when>
					<xsl:when test="$in_lang = 'zh-hk'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-hk/field[@name='wb_imp_tp_plan_aim']"/>
							</td>
							<td nowrap="nowrap">否</td>
							<td align="right" nowrap="nowrap">4000</td>
							<td nowrap="nowrap">文字</td>
							<td>目標</td>
						</tr>
					</xsl:when>
					<xsl:otherwise>
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/en-us/field[@name='wb_imp_tp_plan_aim']"/>
							</td>
							<td nowrap="nowrap">N</td>
							<td align="right" nowrap="nowrap">4000</td>
							<td nowrap="nowrap">Text</td>
							<td>Objective</td>
						</tr>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<!-- wb_imp_tp_plan_target -->
			<xsl:when test="$field = 'wb_imp_tp_plan_target'">
				<xsl:choose>
					<xsl:when test="$in_lang = 'zh-cn'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-cn/field[@name='wb_imp_tp_plan_target']"/>
							</td>
							<td nowrap="nowrap">否</td>
							<td align="right" nowrap="nowrap">4000</td>
							<td nowrap="nowrap">文字</td>
							<td>对象</td>
						</tr>
					</xsl:when>
					<xsl:when test="$in_lang = 'zh-hk'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-hk/field[@name='wb_imp_tp_plan_target']"/>
							</td>
							<td nowrap="nowrap">否</td>
							<td align="right" nowrap="nowrap">4000</td>
							<td nowrap="nowrap">文字</td>
							<td>對像</td>
						</tr>
					</xsl:when>
					<xsl:otherwise>
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/en-us/field[@name='wb_imp_tp_plan_target']"/>
							</td>
							<td nowrap="nowrap">N</td>
							<td align="right" nowrap="nowrap">4000</td>
							<td nowrap="nowrap">Text</td>
							<td>Target audience</td>
						</tr>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<!-- wb_imp_tp_plan_responser -->
			<xsl:when test="$field = 'wb_imp_tp_plan_responser'">
				<xsl:choose>
					<xsl:when test="$in_lang = 'zh-cn'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-cn/field[@name='wb_imp_tp_plan_responser']"/>
							</td>
							<td nowrap="nowrap">是</td>
							<td align="right" nowrap="nowrap">255</td>
							<td nowrap="nowrap">文字</td>
							<td>负责人</td>
						</tr>
					</xsl:when>
					<xsl:when test="$in_lang = 'zh-hk'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-hk/field[@name='wb_imp_tp_plan_responser']"/>
							</td>
							<td nowrap="nowrap">是</td>
							<td align="right" nowrap="nowrap">255</td>
							<td nowrap="nowrap">文字</td>
							<td>负责人</td>
						</tr>
					</xsl:when>
					<xsl:otherwise>
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/en-us/field[@name='wb_imp_tp_plan_responser']"/>
							</td>
							<td nowrap="nowrap">Y</td>
							<td align="right" nowrap="nowrap">255</td>
							<td nowrap="nowrap">Text</td>
							<td>Responsible person</td>
						</tr>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<!-- wb_imp_tp_plan_duration -->
			<xsl:when test="$field = 'wb_imp_tp_plan_duration'">
				<xsl:choose>
					<xsl:when test="$in_lang = 'zh-cn'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-cn/field[@name='wb_imp_tp_plan_duration']"/>
							</td>
							<td nowrap="nowrap">否</td>
							<td align="right" nowrap="nowrap">4000</td>
							<td nowrap="nowrap">文字</td>
							<td>时长</td>
						</tr>
					</xsl:when>
					<xsl:when test="$in_lang = 'zh-hk'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-hk/field[@name='wb_imp_tp_plan_duration']"/>
							</td>
							<td nowrap="nowrap">否</td>
							<td align="right" nowrap="nowrap">4000</td>
							<td nowrap="nowrap">文字</td>
							<td>時長</td> 
						</tr>
					</xsl:when>
					<xsl:otherwise>
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/en-us/field[@name='wb_imp_tp_plan_duration']"/>
							</td>
							<td nowrap="nowrap">N</td>
							<td align="right" nowrap="nowrap">4000</td>
							<td nowrap="nowrap">Text</td>
							<td>Duration</td>
						</tr>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<!-- wb_imp_tp_plan_ftf_start_time -->
			<xsl:when test="$field = 'wb_imp_tp_plan_ftf_start_time'">
				<xsl:choose>
					<xsl:when test="$in_lang = 'zh-cn'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-cn/field[@name='wb_imp_tp_plan_ftf_start_time']"/>
							</td>
							<td nowrap="nowrap">否</td>
							<td align="right" nowrap="nowrap">10</td>
							<td nowrap="nowrap">YYYY-MM-DD</td>
							<td>面授开始日</td>
						</tr>
					</xsl:when>
					<xsl:when test="$in_lang = 'zh-hk'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-hk/field[@name='wb_imp_tp_plan_ftf_start_time']"/>
							</td>
							<td nowrap="nowrap">否</td>
							<td align="right" nowrap="nowrap">10</td>
							<td nowrap="nowrap">YYYY-MM-DD</td>
							<td>面授开始日</td>
						</tr>
					</xsl:when>
					<xsl:otherwise>
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/en-us/field[@name='wb_imp_tp_plan_ftf_start_time']"/>
							</td>
							<td nowrap="nowrap">N</td>
							<td align="right" nowrap="nowrap">10</td>
							<td nowrap="nowrap">YYYY-MM-DD</td>
							<td>Class start date</td>
						</tr>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<!-- wb_imp_tp_plan_ftf_end_time -->
			<xsl:when test="$field = 'wb_imp_tp_plan_ftf_end_time'">
				<xsl:choose>
					<xsl:when test="$in_lang = 'zh-cn'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-cn/field[@name='wb_imp_tp_plan_ftf_end_time']"/>
							</td>
							<td nowrap="nowrap">否</td>
							<td align="right" nowrap="nowrap">10</td>
							<td nowrap="nowrap">YYYY-MM-DD</td>
							<td>面授结束日</td>
						</tr>
					</xsl:when>
					<xsl:when test="$in_lang = 'zh-hk'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-hk/field[@name='wb_imp_tp_plan_ftf_end_time']"/>
							</td>
							<td nowrap="nowrap">否</td>
							<td align="right" nowrap="nowrap">10</td>
							<td nowrap="nowrap">YYYY-MM-DD</td>
							<td>面授結束日</td>
						</tr>
					</xsl:when>
					<xsl:otherwise>
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/en-us/field[@name='wb_imp_tp_plan_ftf_end_time']"/>
							</td>
							<td nowrap="nowrap">N</td>
							<td align="right" nowrap="nowrap">10</td>
							<td nowrap="nowrap">YYYY-MM-DD</td>
							<td>Class end date</td>
						</tr>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<!-- wb_imp_tp_plan_wb_start_time -->
			<xsl:when test="$field = 'wb_imp_tp_plan_wb_start_time'">
				<xsl:choose>
					<xsl:when test="$in_lang = 'zh-cn'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-cn/field[@name='wb_imp_tp_plan_wb_start_time']"/>
							</td>
							<td nowrap="nowrap">否</td>
							<td align="right" nowrap="nowrap">10</td>
							<td nowrap="nowrap">YYYY-MM-DD</td>
							<td>网上内容开始日</td>
						</tr>
					</xsl:when>
					<xsl:when test="$in_lang = 'zh-hk'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-hk/field[@name='wb_imp_tp_plan_wb_start_time']"/>
							</td>
							<td nowrap="nowrap">否</td>
							<td align="right" nowrap="nowrap">10</td>
							<td nowrap="nowrap">YYYY-MM-DD</td>
							<td>網上內容開始日</td>
						</tr>
					</xsl:when>
					<xsl:otherwise>
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/en-us/field[@name='wb_imp_tp_plan_wb_start_time']"/>
							</td>
							<td nowrap="nowrap">N</td>
							<td align="right" nowrap="nowrap">10</td>
							<td nowrap="nowrap">YYYY-MM-DD</td>
							<td>Online content start date</td>
						</tr>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<!-- wb_imp_tp_plan_wb_end_time -->
			<xsl:when test="$field = 'wb_imp_tp_plan_wb_end_time'">
				<xsl:choose>
					<xsl:when test="$in_lang = 'zh-cn'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-cn/field[@name='wb_imp_tp_plan_wb_end_time']"/>
							</td>
							<td nowrap="nowrap">否</td>
							<td align="right" nowrap="nowrap">10</td>
							<td nowrap="nowrap">YYYY-MM-DD</td>
							<td>网上内容结束日</td>
						</tr>
					</xsl:when>
					<xsl:when test="$in_lang = 'zh-hk'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-hk/field[@name='wb_imp_tp_plan_wb_end_time']"/>
							</td>
							<td nowrap="nowrap">否</td>
							<td align="right" nowrap="nowrap">10</td>
							<td nowrap="nowrap">YYYY-MM-DD</td>
							<td>網上內容結束日</td>
						</tr>
					</xsl:when>
					<xsl:otherwise>
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/en-us/field[@name='wb_imp_tp_plan_wb_end_time']"/>
							</td>
							<td nowrap="nowrap">N</td>
							<td align="right" nowrap="nowrap">10</td>
							<td nowrap="nowrap">YYYY-MM-DD</td>
							<td>Online content end date</td>
						</tr>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<!-- wb_imp_tp_plan_lrn_count -->
			<xsl:when test="$field = 'wb_imp_tp_plan_lrn_count'">
				<xsl:choose>
					<xsl:when test="$in_lang = 'zh-cn'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-cn/field[@name='wb_imp_tp_plan_lrn_count']"/>
							</td>
							<td nowrap="nowrap">否</td>
							<td align="right" nowrap="nowrap">10</td>
							<td nowrap="nowrap">数字</td>
							<td>预计参训人数</td>
						</tr>
					</xsl:when>
					<xsl:when test="$in_lang = 'zh-hk'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-hk/field[@name='wb_imp_tp_plan_lrn_count']"/>
							</td>
							<td nowrap="nowrap">否</td>
							<td align="right" nowrap="nowrap">10</td>
							<td nowrap="nowrap">数字</td>
							<td>預計參訓人數</td>
						</tr>
					</xsl:when>
					<xsl:otherwise>
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/en-us/field[@name='wb_imp_tp_plan_lrn_count']"/>
							</td>
							<td nowrap="nowrap">N</td>
							<td align="right" nowrap="nowrap">10</td>
							<td nowrap="nowrap">Numbers</td>
							<td>Estimated participants</td>
						</tr>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<!-- wb_imp_tp_plan_fee -->
			<xsl:when test="$field = 'wb_imp_tp_plan_fee'">
				<xsl:choose>
					<xsl:when test="$in_lang = 'zh-cn'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-cn/field[@name='wb_imp_tp_plan_fee']"/>
							</td>
							<td nowrap="nowrap">否</td>
							<td align="right" nowrap="nowrap">10</td>
							<td nowrap="nowrap">数字</td>
							<td>预计费用</td>
						</tr>
					</xsl:when>
					<xsl:when test="$in_lang = 'zh-hk'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-hk/field[@name='wb_imp_tp_plan_fee']"/>
							</td>
							<td nowrap="nowrap">否</td>
							<td align="right" nowrap="nowrap">10</td>
							<td nowrap="nowrap">数字</td>
							<td>预计费用</td>
						</tr>
					</xsl:when>
					<xsl:otherwise>
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/en-us/field[@name='wb_imp_tp_plan_fee']"/>
							</td>
							<td nowrap="nowrap">N</td>
							<td align="right" nowrap="nowrap">10</td>
							<td nowrap="nowrap">Numbers</td>
							<td>Estimated training cost</td>
						</tr>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<!-- wb_imp_tp_plan_remark -->
			<xsl:when test="$field = 'wb_imp_tp_plan_remark'">
				<xsl:choose>
					<xsl:when test="$in_lang = 'zh-cn'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-cn/field[@name='wb_imp_tp_plan_remark']"/>
							</td>
							<td nowrap="nowrap">否</td>
							<td align="right" nowrap="nowrap">4000</td>
							<td nowrap="nowrap">文字</td>
							<td>备注</td>
						</tr>
					</xsl:when>
					<xsl:when test="$in_lang = 'zh-hk'">
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/zh-hk/field[@name='wb_imp_tp_plan_remark']"/>
							</td>
							<td nowrap="nowrap">否</td>
							<td align="right" nowrap="nowrap">4000</td>
							<td nowrap="nowrap">文字</td>
							<td>備註</td>
						</tr>
					</xsl:when>
					<xsl:otherwise>
						<tr>
							<td nowrap="nowrap">
								<xsl:value-of select="./label/en-us/field[@name='wb_imp_tp_plan_remark']"/>
							</td>
							<td nowrap="nowrap">N</td>
							<td align="right" nowrap="nowrap">4000</td>
							<td nowrap="nowrap">Text</td>
							<td>Remarks</td>
						</tr>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>
