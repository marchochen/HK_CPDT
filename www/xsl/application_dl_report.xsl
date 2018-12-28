<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<!-- utils -->
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<!-- customize utils -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- others -->
	<xsl:import href="share/usr_detail_label_share.xsl"/>
	<xsl:import href="share/label_role.xsl"/>
	<xsl:output />
	<xsl:variable name="label" select="/applyeasy/meta/label_reference_data_list"/>
	<xsl:variable name="total_process_status_cnt" select="/applyeasy/process_status_cnt_list/total_process_status_cnt/@cnt"/>
	<xsl:variable name="profile_attributes" select="/applyeasy/meta/profile_attributes"/>
	<xsl:variable name="columns" select="/applyeasy/columns"/>
	<xsl:variable name="integrated_ind" select="/applyeasy/item/@integrated_ind"/>
	<!-- ======================================================================== -->
	<xsl:template match="/applyeasy">
		<html>
			<xsl:apply-templates select="application_list"/>
		</html>
	</xsl:template>
	<!-- ======================================================================== -->
	<xsl:template match="application_list">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<style type="text/css">
			td{
				border: #CCCCCC;
			}
			</style>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
			<xsl:call-template name="wb_init_lab"/>
		</body>
	</xsl:template>
	<!-- ======================================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_enroll_status">狀態</xsl:with-param>
			<xsl:with-param name="lab_date">日期</xsl:with-param>
			<xsl:with-param name="lab_lost_and_found">回收站</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_enroll_status">状态</xsl:with-param>
			<xsl:with-param name="lab_date">日期</xsl:with-param>
			<xsl:with-param name="lab_lost_and_found">回收站</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_enroll_status">Status</xsl:with-param>
			<xsl:with-param name="lab_date">Date</xsl:with-param>
			<xsl:with-param name="lab_lost_and_found">Recycle bin</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- ======================================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_enroll_status"/>
		<xsl:param name="lab_date"/>
		<xsl:param name="lab_lost_and_found"/>
		<table border="1" cellpadding="0" cellspacing="0">
			<xsl:call-template name="draw_header">
				<xsl:with-param name="lab_enroll_status" select="$lab_enroll_status"/>
				<xsl:with-param name="lab_date" select="$lab_date"/>
			</xsl:call-template>
			<xsl:apply-templates select="application">
				<xsl:with-param name="lab_lost_and_found" select="$lab_lost_and_found"/>
			</xsl:apply-templates>
		</table>
	</xsl:template>
	<!-- ===========================get label================================= -->
	<xsl:template name="get_label">
		<xsl:param name="label_name"/>
		<xsl:param name="label_type"/>
		<xsl:variable name="label_usr" select="$profile_attributes/*[name() = $label_name]/label[@xml:lang = $cur_lang]"/>
		<xsl:choose>
			<xsl:when test="$label_usr !=''"><xsl:value-of select="$label_usr"/></xsl:when>
			<xsl:when test="$label_type != '' and $label_type = 'jslabel'"><xsl:value-of select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, $label_name)"/></xsl:when>
			<xsl:otherwise><xsl:value-of select="$label/label[@name = $label_name]"/></xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ======================================================================== -->
	<xsl:template name="draw_header">
		<xsl:param name="lab_enroll_status"/>
		<xsl:param name="lab_date"/>
		<tr>
		
			<xsl:for-each select="$columns/col">
				<xsl:if test="@active='true' and ($integrated_ind != 'true' or @id != 'integrated_lrn')">
					<td>
						<xsl:call-template name="get_label">
							<xsl:with-param name="label_name" select="@label"></xsl:with-param>
							<xsl:with-param name="label_type" select="@label_type"></xsl:with-param>
						</xsl:call-template>
					</td>
				</xsl:if>
			</xsl:for-each>
			<!--
				<td>
					<xsl:value-of select="$lab_login_id"/>
				</td>
				<td>
					<xsl:value-of select="$lab_dis_name"/>
				</td>
				<td>
					<xsl:value-of select="$lab_tel_1"/>
				</td>
				<td>
					<xsl:value-of select="$lab_group"/>
				</td>
				<td>
					<xsl:value-of select="$lab_grade"/>
				</td>
				<td>
					<xsl:value-of select="$lab_enroll_status"/>
				</td>
				<td>
					<xsl:value-of select="$lab_date"/>
				</td>
			-->
		</tr>
	</xsl:template>
	<!-- ======================================================================== -->
	<xsl:template match="application">
		<xsl:param name="lab_lost_and_found"/>
		<tr>
			<xsl:variable name="app_id" select="@id"/>
			<xsl:variable name="app_export_col" select="app_export_col"/>
			<xsl:variable name="app" select="."/>
			<xsl:for-each select="$columns/col">
				<xsl:if test="@active='true' and ($integrated_ind != 'true' or @id != 'integrated_lrn')">
					<xsl:variable name="id_" select="@id"/>
					<xsl:variable name="type_" select="db_col/@type"/>
					<xsl:choose>
					<xsl:when test="@id='direct_supervisors'">
						<td>
						<span class="Text">
								<xsl:choose>
									<xsl:when test="$app_export_col/*[name()='direct_supervisors']/direct_supervisor/entity">
										<xsl:for-each select="$app_export_col/*[name()='direct_supervisors']/direct_supervisor/entity[@type = 'USR']">
											<xsl:value-of select="@display_bil"/>
											<xsl:if test="position() != last()">,</xsl:if>
										</xsl:for-each>
									</xsl:when>
									<xsl:otherwise>
										<xsl:text>--</xsl:text>
									</xsl:otherwise>
								</xsl:choose>
							</span>
						</td>									
					</xsl:when>
					<xsl:when test="@id='gender'">
						<td>
							<span class="Text">
								<xsl:choose>
									<xsl:when test="$app_export_col/*[name()=$id_]='F'">
										<xsl:value-of select="$lab_gender_f"/>
									</xsl:when>
									<xsl:when test="$app_export_col/*[name()=$id_]='M'">
										<xsl:value-of select="$lab_gender_m"/>
									</xsl:when>														
									<xsl:otherwise>
										<xsl:value-of select="$lab_gender_unspecified"/>
									</xsl:otherwise>
								</xsl:choose>
							</span>
						</td>			
					</xsl:when>
					<xsl:when test="$type_='timestamp'">
						<td nowrap="nowrap">
							<span class="Text">
								<xsl:variable name="value_" select="$app_export_col/*[name()=$id_]"/>
								<xsl:choose>
									<xsl:when test="$value_ !=''">
										<xsl:call-template name="display_time">
										<xsl:with-param name="my_timestamp">
											<xsl:value-of select="$app_export_col/*[name()=$id_]"/>
										</xsl:with-param>
										</xsl:call-template>
									</xsl:when>
									<xsl:otherwise>
										<xsl:text>--</xsl:text>
									</xsl:otherwise>
								</xsl:choose>
							</span>
						</td>
					</xsl:when>
					<xsl:when test="@id='pending_approval_role'">
						<td align="center">
							<span class="Text">
								<xsl:choose>
									<xsl:when test="$app_export_col/pending_approval_role = ''">
										<xsl:text>--</xsl:text>
									</xsl:when>
									<xsl:otherwise>
										<xsl:variable name="pending_approval_role" select="$app_export_col/pending_approval_role/text()"/>
										<xsl:value-of select="$label/label[@name = $pending_approval_role]"/>
									</xsl:otherwise>
								</xsl:choose>
							</span>
						</td>									
					</xsl:when>
					<xsl:when test="contains(@id,'extension')='true'">
						<td align="center">
							<span class="Text">
								<xsl:variable name="value_" select="$app_export_col/*[name()=$id_]"/>
								<xsl:choose>
									<xsl:when test="$value_ !='' and $profile_attributes/*[name() =$id_]/option_list">
										<xsl:value-of select="$profile_attributes/*[name() =$id_]/option_list/option[@id=$value_]/label[@xml:lang=$cur_lang]"/>
									</xsl:when>
									<xsl:when test="$value_ !=''"><xsl:value-of select="$value_"/></xsl:when>
									<xsl:otherwise>
										<xsl:text>--</xsl:text>
									</xsl:otherwise>
								</xsl:choose>
							</span>
						</td>				
					</xsl:when>
					<xsl:when test="@id='role'">
						<td align="center">
							<span class="Text">
								<xsl:choose>
									<xsl:when test="$app_export_col/*[name()='role']/granted_roles">
										<xsl:for-each select="$app_export_col/*[name()='role']/granted_roles/role">
											<xsl:call-template name="get_rol_title"/>
											<xsl:if test="position() != last()">,&#160;</xsl:if>
										</xsl:for-each>
									</xsl:when>
									<xsl:otherwise>
										<xsl:text>--</xsl:text>
									</xsl:otherwise>
								</xsl:choose>
							</span>
						</td>	
					</xsl:when>
					<xsl:when test="@id='integrated_lrn'">
						<td>
							<span class="Text">
								<xsl:choose>
									<xsl:when test="count($app/be_integrated_lrn_lst/intg_lrn) > 0">
										<xsl:for-each select="$app/be_integrated_lrn_lst/intg_lrn">
											<xsl:value-of select="text()"/>
											<xsl:if test="position() != last()"><br/></xsl:if>
										</xsl:for-each>
									</xsl:when>
									<xsl:otherwise>
										<xsl:text>--</xsl:text>
									</xsl:otherwise>
								</xsl:choose>
							</span>
						</td>									
					</xsl:when>
					<xsl:otherwise>
						<td>
							<span class="Text">
								<xsl:choose>
									<xsl:when test="$app_export_col/*[name()=$id_] != ''">
										<xsl:value-of select="$app_export_col/*[name()=$id_]"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:text>--</xsl:text>
									</xsl:otherwise>
								</xsl:choose>
							</span>
						</td>
					</xsl:otherwise>
				</xsl:choose>
				</xsl:if>
			</xsl:for-each>
			<!--
			<td>
				<xsl:value-of select="user/@id"/>
			</td>
			<td>
				<xsl:value-of select="user/@name"/>
			</td>
			<td>
				<xsl:choose>
					<xsl:when test="user/@tel != ''">
						<xsl:value-of select="user/@tel"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:text></xsl:text>
					</xsl:otherwise>
				</xsl:choose>
			</td>
			<td>
				<xsl:choose>
					<xsl:when test="user/@usr_group_name = ''">
						<xsl:value-of select="$lab_lost_and_found"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="user/@usr_group_name"/>
					</xsl:otherwise>
				</xsl:choose>
			</td>
			<td>
				<span class="Text">
					<xsl:value-of select="user/@usr_grade_name"/>
				</span>
			</td>
			<td>
				<xsl:value-of select="@process_status"/>
			</td>
			<td>
				<xsl:variable name="last_action" select="history/action_history/action[last()]"/>
				<xsl:call-template name="display_time">
					<xsl:with-param name="dis_time">T</xsl:with-param>
					<xsl:with-param name="my_timestamp" select="$last_action/@upd_timestamp"/>
				</xsl:call-template>
			</td>
			-->
		</tr>
	</xsl:template>
	<!-- ======================================================================== -->
	<xsl:template match="entity">
		<xsl:if test="position()!=1">
			<xsl:text>, </xsl:text>
		</xsl:if>
		<xsl:value-of select="@display_bil"/>
	</xsl:template>
	<!-- ======================================================================== -->
	<xsl:template name="emails">
		<xsl:if test="user/email/@email_2!='null'">
			<xsl:value-of select="user/email/@email_2"/>
		</xsl:if>
	</xsl:template>
	<!-- ======================================================================== -->
	<xsl:template match="action">
		<xsl:if test="position()=last()">
			<xsl:choose>
				<xsl:when test="@verb!='' and display_name!='' and @upd_timestamp!=''">
					<xsl:value-of select="@verb"/>
					<xsl:text> by </xsl:text>
					<xsl:value-of select="display_name"/>
					<xsl:text> on </xsl:text>
					<xsl:value-of select="substring-before(@upd_timestamp,' ')"/>
				</xsl:when>
				<xsl:otherwise>--</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
	</xsl:template>
	<!-- ======================================================================== -->
</xsl:stylesheet>
