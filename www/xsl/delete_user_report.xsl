<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	version="1.0">
	<xsl:template name="import_detail">
		<xsl:param name="label" />
		<xsl:param name="lang" />
		<xsl:param name="id" />
		<xsl:param name="minLength" />
		<xsl:param name="maxLength" />
		<xsl:param name="allowEmpty" />
		<xsl:param name="isTcIndependent" />
		<xsl:choose>
			<!-- 登录名 -->
			<xsl:when test="$id='user_id'">
				<tr>
					<td nowrap="nowrap">
						<xsl:value-of select="$label" />
					</td>
					<xsl:call-template name="allowEmpty">
						<xsl:with-param name="empty" select="$allowEmpty" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:call-template name="length">
						<xsl:with-param name="minlength" select="$minLength" />
						<xsl:with-param name="maxlength" select="$maxLength" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:if test="$lang='en-us'">
						<td nowrap="nowrap">
							Character a - z, 0 - 9, '_' or '-' only
						</td>
						<td>

						</td>
					</xsl:if>
					<xsl:if test="$lang='zh-cn'">
						<td nowrap="nowrap">
							只能包含：纯英文字母、数字、
							<br />
							下划线(_)、横线(-)
						</td>
						<td></td>
					</xsl:if>
					<xsl:if test="$lang='zh-hk'">
						<td nowrap="nowrap">
							只能包含：純英文小寫字母、數字、
							<br />
							底線(_)、橫線(-)
						</td>
						<td></td>
					</xsl:if>
				</tr>
			</xsl:when>


		</xsl:choose>
	</xsl:template>

	<xsl:template name="allowEmpty">
		<xsl:param name="empty" />
		<xsl:param name="lang" />
		<xsl:choose>
			<xsl:when test="$empty='1'">
				<xsl:if test="$lang='zh-hk'">
					<td nowrap="nowrap">是</td>
				</xsl:if>
				<xsl:if test="$lang='zh-cn'">
					<td nowrap="nowrap">是</td>
				</xsl:if>
				<xsl:if test="$lang='en-us'">
					<td nowrap="nowrap">Yes</td>
				</xsl:if>
			</xsl:when>
			<xsl:when test="$empty='0'">
				<xsl:if test="$lang='zh-hk'">
					<td nowrap="nowrap">否</td>
				</xsl:if>
				<xsl:if test="$lang='zh-cn'">
					<td nowrap="nowrap">否</td>
				</xsl:if>
				<xsl:if test="$lang='en-us'">
					<td nowrap="nowrap">No</td>
				</xsl:if>
			</xsl:when>
		</xsl:choose>
	</xsl:template>


	<xsl:template name="length">
		<xsl:param name="minlength" />
		<xsl:param name="maxlength" />
		<xsl:param name="lang" />
		<td nowrap="nowrap" align="left">
			<xsl:if test="@minlength!='0'">
				<xsl:value-of select="@minlength" />
				-
			</xsl:if>
			<xsl:value-of select="@maxlength" />
			(<xsl:if test="$lang='zh-hk'">
				字元
			</xsl:if>
			<xsl:if test="$lang='zh-cn'">
				字符
			</xsl:if>
			<xsl:if test="$lang='en-us'">
				Char
			</xsl:if>)
		</td>
	</xsl:template>

</xsl:stylesheet>