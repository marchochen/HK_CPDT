<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="../utils/display_time.xsl"/>
	<xsl:import href="../utils/unescape_html_linefeed.xsl"/>
	<xsl:import href="../utils/change_lowercase.xsl"/>
	<xsl:import href="label_role.xsl"/>
	<xsl:import href="label_lrn_soln.xsl"/>
	<xsl:variable name="sys_type"/>
	<xsl:variable name="tc_enabled" select="//meta/tc_enabled"/>
	<!-- ======================================== -->
	
	<xsl:template name="get_desc">
		<xsl:choose>
			<xsl:when test="title">
				<xsl:choose>
					<xsl:when test="title/desc[@lan]">
						<xsl:value-of select="title/desc[@lan = $wb_lang_encoding]/@name"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="title/desc/@name"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:when test="../../title/desc[@lan = $wb_lang_encoding]/@name">
				<xsl:value-of select="../../title/desc[@lan = $wb_lang_encoding]/@name"/>
			</xsl:when>
			<xsl:when test="role">
				<xsl:call-template name="get_rol_title">
					<xsl:with-param name="rol_ext_id" select="role/@id"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="../../role">
				<xsl:call-template name="get_rol_title">
					<xsl:with-param name="rol_ext_id" select="../../role/@id"/>
				</xsl:call-template>
			</xsl:when>
			
			<xsl:otherwise>
				<xsl:value-of select="@name"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ======================================== -->
	<xsl:template name="suffix">
		<xsl:text>&#160;</xsl:text>
		<xsl:value-of select="suffix/desc[@lan = $wb_lang_encoding]/@name"/>
	</xsl:template>
	<!-- ======================================== -->
	<xsl:template name="prefix">
		<xsl:text>&#160;</xsl:text>
		<xsl:value-of select="prefix/desc[@lan = $wb_lang_encoding]/@name"/>
	</xsl:template>
	<!-- ======================================== -->
	<xsl:template name="sub_value">
		<xsl:param name="org_value"/>
		<xsl:choose>
			<xsl:when test="contains($org_value,'[|]') = 'true'">
				<xsl:value-of select="substring-before($org_value,'[|]')"/>
				<xsl:variable name="_org_value">
					<xsl:value-of select="substring-after($org_value,'[|]')"/>
				</xsl:variable>
				<xsl:variable name="tag_name">
					<xsl:value-of select="substring-before($_org_value, '[|]')"/>
				</xsl:variable>
				<xsl:value-of select="/km/node/*[name() = $tag_name]"/>
				<xsl:value-of select="substring-after($_org_value,'[|]')"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$org_value"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- Blocker for ALL======================================================== -->
	<xsl:template match="*" mode="gen_field"/>
	<!-- ======================================== -->
	<xsl:template match="*[not(@type) and subfield_list]" mode="gen_field">
		<xsl:param name="csv"/>
		<xsl:param name="xls"/>
		<xsl:param name="text_class"/>
		<xsl:param name="itm_blend_ind"/>
		<xsl:apply-templates mode="gen_field">
			<xsl:with-param name="text_class">
				<xsl:value-of select="$text_class"/>
			</xsl:with-param>
			<xsl:with-param name="csv" select="$csv"/>
			<xsl:with-param name="xls" select="$xls"/>
			<xsl:with-param name="itm_blend_ind" select="$itm_blend_ind"/>
		</xsl:apply-templates>      
	</xsl:template>
	<!-- ================================================================== -->
	<xsl:template match="subfield_list" mode="gen_field">
		<xsl:param name="csv"/>
		<xsl:param name="xls"/>
		<xsl:param name="text_class"/>
		<xsl:param name="itm_blend_ind"/>
		
		<xsl:for-each select="*[not(../../@blend_ind and ../../@blend_ind !=$itm_blend_ind)]">	
			<xsl:variable name="draw">
				<xsl:call-template name="get_show"/>
			</xsl:variable>
			<xsl:if test="contains($draw,'true')">
				<xsl:apply-templates select="." mode="gen_field">
					<xsl:with-param name="text_class">
						<xsl:value-of select="$text_class"/>
					</xsl:with-param>
					<xsl:with-param name="csv" select="$csv"/>
					<xsl:with-param name="xls" select="$xls"/>
				</xsl:apply-templates>
				<xsl:if test="../../@arrange = 'vertical'">
					<br/>
				</xsl:if>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>
	<!-- ================================================================== -->
	<xsl:template match="*[@type = 'text'] | *[@type = 'email']  | *[@type = 'constant'] |*[@type = 'pos_amount'] | *[@type = 'pos_int'] | *[@type = 'pos_int_unlimited']  | *[@type = 'pos_amount_optional'] | *[@type='pos_amount_bonus']" mode="gen_field">
		<xsl:param name="csv"/>
		<xsl:param name="text_class"/>
		<span class="{$text_class}">
			<xsl:choose>
				<xsl:when test="@external_field='yes'">
					<xsl:choose>
						<xsl:when test="@type = 'pos_amount'">
							<xsl:choose>
								<xsl:when test="*[name() != 'title']/@value != ''">
									<xsl:value-of select="format-number(*[name() != 'title']/@value,'0.00')"/>
								</xsl:when>
							</xsl:choose>
						</xsl:when>
						<xsl:otherwise>
							<xsl:choose>
								<xsl:when test="*[name() != 'title']/@value!=''">
									<xsl:value-of select="*[name() != 'title']/@value"/>
								</xsl:when>
								<xsl:otherwise>
									--
								</xsl:otherwise>
							</xsl:choose>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
				<xsl:otherwise>
					<xsl:choose>
						<xsl:when test="@type = 'pos_amount'">
							<xsl:choose>
								<xsl:when test="@value != ''">
									<xsl:value-of select="format-number(@value,'0.00')"/>
								</xsl:when>
							</xsl:choose>
						</xsl:when>
						<xsl:when test="@type = 'pos_int_unlimited'">
							<xsl:choose>
								<xsl:when test="@value = ''">
									<xsl:value-of select="$lab_const_unlimited"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="@value"/>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
						<xsl:when test="@type = 'pos_amount_optional'">
							<xsl:choose>
								<xsl:when test="@value = '' or not(@value)">
									<xsl:value-of select="$lab_const_free"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="format-number(@value,'0.00')"/>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
						<xsl:otherwise>
							<xsl:choose>
								<xsl:when test="@value">
									<xsl:value-of select="@value"/>
								</xsl:when>
								<xsl:when test="not(@value) and @name='Code' ">
									<xsl:value-of select="../../../../item/@code"/>
								</xsl:when>
								<xsl:when test="not(@value) and @name='Title'">
									<xsl:value-of select="../../../../item/@title"/>
								</xsl:when>
							</xsl:choose>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:otherwise>
			</xsl:choose>
		</span>
		<xsl:apply-templates select="link_list" mode="gen_field">
			<xsl:with-param name="text_class">
				<xsl:value-of select="$text_class"/>
			</xsl:with-param>
			<xsl:with-param name="csv" select="$csv"/>
		</xsl:apply-templates>
	</xsl:template>
	<!-- ======================================== -->
	<xsl:template match="*[@type = 'constant_datetime']" mode="gen_field">
		<xsl:param name="csv"/>
		<xsl:param name="text_class"/>
		<xsl:choose>
			<xsl:when test="@external_field='yes'">
				<xsl:call-template name="display_time">
					<xsl:with-param name="mode">
						<xsl:if test="$csv = 'true'">csv</xsl:if>
					</xsl:with-param>
					<xsl:with-param name="wb_lang">
						<xsl:value-of select="$wb_lang"/>
					</xsl:with-param>
					<xsl:with-param name="dis_time">T</xsl:with-param>
					<xsl:with-param name="my_timestamp">
						<xsl:value-of select="*[name() != 'title']/@value"/>
					</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="display_time">
					<xsl:with-param name="mode">
						<xsl:if test="$csv = 'true'">csv</xsl:if>
					</xsl:with-param>
					<xsl:with-param name="wb_lang">
						<xsl:value-of select="$wb_lang"/>
					</xsl:with-param>
					<xsl:with-param name="dis_time">T</xsl:with-param>
					<xsl:with-param name="my_timestamp">
						<xsl:value-of select="*[name() != 'title']/@value"/>
					</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:apply-templates select="link_list" mode="gen_field">
			<xsl:with-param name="csv" select="$csv"/>
			<xsl:with-param name="text_class">
				<xsl:value-of select="$text_class"/>
			</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- ======================================== -->
	<xsl:template match="*[@type = 'textarea']" mode="gen_field">
		<xsl:param name="csv"/>
		<xsl:param name="text_class"/>
		<xsl:choose>
			<xsl:when test="$csv = 'true'">
				<xsl:call-template name="unescape_html_linefeed">
					<xsl:with-param name="replace_char"/>
					<xsl:with-param name="my_right_value">
						<xsl:value-of select="@value"/>
					</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="unescape_html_linefeed">
					<xsl:with-param name="my_right_value">
						<xsl:value-of select="@value"/>
					</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:apply-templates select="link_list" mode="gen_field">
			<xsl:with-param name="csv" select="$csv"/>
			<xsl:with-param name="text_class">
				<xsl:value-of select="$text_class"/>
			</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- ======================================== -->
	<xsl:template match="*[@type='notify_email_limited']" mode="gen_field">
		<xsl:param name="text_class"/>
		<xsl:variable name="nty_days" select="/applyeasy/item/@notify_days"/>
		<xsl:variable name="lab_optional">
			<xsl:choose>
				<xsl:when test="$wb_lang='ch'">否</xsl:when>
				<xsl:when test="$wb_lang='gb'">否</xsl:when>
				<xsl:otherwise>No</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="lab_specify_to">
			<xsl:choose>
				<xsl:when test="$wb_lang='ch'">是，在課程結束前</xsl:when>
				<xsl:when test="$wb_lang='gb'">是，在课程结束前</xsl:when>
				<xsl:otherwise>Yes, </xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="lab_send_by">
			<xsl:choose>
				<xsl:when test="$wb_lang='ch'">天</xsl:when>
				<xsl:when test="$wb_lang='gb'">天</xsl:when>
				<xsl:otherwise> days before course end date.</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<table>
			<xsl:if test="not($nty_days) or $nty_days = '' or $nty_days = -1">
				<label>
					<xsl:value-of select="$lab_optional"/>
				</label>
			</xsl:if>
			<xsl:if test="$nty_days >= 0">
				<label>
					<xsl:value-of select="$lab_specify_to"/>
					<xsl:value-of select="$nty_days"/>
					<xsl:value-of select="$lab_send_by"/>
				</label>
			</xsl:if>
		</table>
	</xsl:template>
	<xsl:template match="*[@type='notify_support_email']" mode="gen_field">
		<xsl:param name="text_class"/>
		<xsl:variable name="nty_email" select="/applyeasy/item/@notify_email"/>
		<xsl:variable name="sys_email" select="./sys_email/text()"/>
		<xsl:variable name="lab_system">
			<!--  
			<xsl:choose>
				<xsl:when test="$wb_lang='ch'">系統</xsl:when>
				<xsl:when test="$wb_lang='gb'">系统</xsl:when>
				<xsl:otherwise>System</xsl:otherwise>
			</xsl:choose>
			-->
		</xsl:variable>
		<xsl:variable name="lab_others">
		<!--
			<xsl:choose>
				<xsl:when test="$wb_lang='ch'">其他</xsl:when>
				<xsl:when test="$wb_lang='gb'">其他</xsl:when>
				<xsl:otherwise>Others</xsl:otherwise>
			</xsl:choose>
		-->
		</xsl:variable>
		<xsl:variable name="lab_desc">
			<xsl:choose>
				<xsl:when test="$wb_lang='ch'">這個郵件會被用來作為提醒郵件的發件人和回覆地址</xsl:when>
				<xsl:when test="$wb_lang='gb'">这个邮件会被用来作为提醒邮件的发件人和回复地址</xsl:when>
				<xsl:otherwise>It will be used as the “From address” and “Reply-to address” for notification</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<table>
			<xsl:if test="not($nty_email) or $nty_email = ''">
				<label>
					<xsl:value-of select="$lab_system"/> (<xsl:value-of select="$sys_email"/>)
				</label>
			</xsl:if>
			<xsl:if test="$nty_email != ''">
				<label>
					<xsl:value-of select="$lab_others"/>
					<xsl:value-of select="$nty_email"/>
				</label>
			</xsl:if>
			<br/>
			<label class="wzb-ui-module-text">
				(<xsl:value-of select="$lab_desc"/>)
			</label>
		</table>
	</xsl:template>
	<!-- ================================================================== -->
	<xsl:template match="hidden" mode="gen_field">
		<xsl:param name="csv"/>
		<xsl:param name="text_class"/>
		<xsl:apply-templates select="link_list" mode="gen_field">
			<xsl:with-param name="csv" select="$csv"/>
			<xsl:with-param name="text_class">
				<xsl:value-of select="$text_class"/>
			</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- ================================================================== -->
	<xsl:template match="*[@type = 'ul']" mode="gen_field">
		<xsl:param name="csv"/>
		<xsl:param name="text_class"/>
		<ul>
			<xsl:apply-templates mode="li"/>
		</ul>
		<xsl:apply-templates select="link_list" mode="gen_field">
			<xsl:with-param name="csv" select="$csv"/>
			<xsl:with-param name="text_class">
				<xsl:value-of select="$text_class"/>
			</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- ================================================================== -->
	<xsl:template match="*[@type = 'ol']" mode="gen_field">
		<xsl:param name="csv"/>
		<xsl:param name="text_class"/>
		<ol>
			<xsl:apply-templates mode="li">
				<xsl:with-param name="text_class">
					<xsl:value-of select="$text_class"/>
				</xsl:with-param>
			</xsl:apply-templates>
		</ol>
		<xsl:apply-templates select="link_list" mode="gen_field">
			<xsl:with-param name="csv" select="$csv"/>
			<xsl:with-param name="text_class">
				<xsl:value-of select="$text_class"/>
			</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- ======================================== -->
	<xsl:template match="*" mode="li">
		<xsl:param name="csv"/>
		<xsl:param name="text_class"/>
		<xsl:if test="name() != 'title'">
			<li>
				<xsl:apply-templates select="." mode="gen_field">
					<xsl:with-param name="csv" select="$csv"/>
					<xsl:with-param name="text_class">
						<xsl:value-of select="$text_class"/>
					</xsl:with-param>
				</xsl:apply-templates>
			</li>
		</xsl:if>
	</xsl:template>
	<!-- ======================================== -->
	<xsl:template match="*[@type = 'named_url']" mode="gen_field">
		<xsl:param name="csv"/>
		<xsl:param name="text_class"/>
		<xsl:for-each select="*[name() != 'title']">
			<xsl:apply-templates select="." mode="gen_field">
				<xsl:with-param name="text_class">
					<xsl:value-of select="$text_class"/>
				</xsl:with-param>
			</xsl:apply-templates>
			<xsl:if test="position() != last()">
				<br/>
			</xsl:if>
		</xsl:for-each>
		<xsl:apply-templates select="link_list" mode="gen_field">
			<xsl:with-param name="csv" select="$csv"/>
			<xsl:with-param name="text_class">
				<xsl:value-of select="$text_class"/>
			</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- ================================================================== -->
	<xsl:template match="*[@type= 'image']" mode="gen_field">
		<xsl:param name="text_class"/>
		<xsl:param name="csv"/>
		<xsl:variable name="file_ext">
			<xsl:call-template name="change_lowercase">
				<xsl:with-param name="input_value">
					<xsl:value-of select="substring-after(@value,'.')"/>
				</xsl:with-param>
			</xsl:call-template>
		</xsl:variable>
		<xsl:choose>
			<xsl:when test="$file_ext = 'swf'">
				<!-- 不支持 -->
				<OBJECT classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=5,0,0,0">
					<PARAM NAME="movie" VALUE="../item/{/applyeasy/item/@id}/{@value}"/>
					<EMBED src="../item/{/applyeasy/item/@id}/{@value}" TYPE="application/x-shockwave-flash" PLUGINSPAGE="http://www.macromedia.com/shockwave/download/index.cgi?P1_Prod_Version=ShockwaveFlash"/>
				</OBJECT>
			</xsl:when>
			<xsl:when test="$file_ext = 'gif' or $file_ext = 'jpg'  or $file_ext = 'png'">
				<img src="../item/{/applyeasy/item/@id}/{@value}" border="0">
				    <xsl:choose>
					   	<xsl:when  test="name() = 'field113'">
							<xsl:attribute name="width">
								<xsl:text>380</xsl:text>
							</xsl:attribute>
					   	</xsl:when>
					   	<xsl:otherwise>
							<xsl:if test="@width != '' or @height != ''">
								<xsl:attribute name="style"><xsl:if test="@width != ''">width:<xsl:value-of select="@width"/>px;</xsl:if><xsl:if test="@height != ''">height:<xsl:value-of select="@height"/>px;</xsl:if></xsl:attribute>
							</xsl:if>
					 	</xsl:otherwise>
				   </xsl:choose>
				
				</img>
			</xsl:when>
			<xsl:otherwise>
				<a href="../item/{/applyeasy/item/@id}/{@value}" target="_blank">
					<xsl:value-of select="@value"/>
				</a> 
			</xsl:otherwise>
		</xsl:choose>
		<xsl:apply-templates select="link_list" mode="gen_field">
			<xsl:with-param name="csv" select="$csv"/>
			<xsl:with-param name="text_class">
				<xsl:value-of select="$text_class"/>
			</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- ================================================================== -->
	<xsl:template match="*[@type = 'date']" mode="gen_field">
		<xsl:param name="csv"/>
		<xsl:param name="text_class"/>
		<span class="{$text_class}">
			<xsl:if test="title">
				<xsl:value-of select="title/desc[@lan = $wb_lang_encoding]/@name"/>
				<xsl:text>：&#160;</xsl:text>
			</xsl:if>
			<xsl:call-template name="display_time">
				<xsl:with-param name="mode">
					<xsl:if test="$csv = 'true'">csv</xsl:if>
				</xsl:with-param>
				<xsl:with-param name="my_timestamp">
					<xsl:value-of select="@value"/>
				</xsl:with-param>
				<xsl:with-param name="wb_lang">
					<xsl:value-of select="$wb_lang"/>
				</xsl:with-param>
			</xsl:call-template>
		</span>
		<xsl:apply-templates select="link_list" mode="gen_field">
			<xsl:with-param name="csv" select="$csv"/>
			<xsl:with-param name="text_class">
				<xsl:value-of select="$text_class"/>
			</xsl:with-param>
		</xsl:apply-templates>
		<br/>
	</xsl:template>
	<!-- ================================================================== -->
	<xsl:template match="*[@type = 'datetime'] " mode="gen_field">
		<xsl:param name="csv"/>
		<xsl:param name="text_class"/>
		<span class="{$text_class}">
			<xsl:if test="title">
				<xsl:value-of select="title/desc[@lan = $wb_lang_encoding]/@name"/>
				<xsl:text>：&#160;</xsl:text>
			</xsl:if>
			<xsl:call-template name="display_time">
				<xsl:with-param name="mode">
					<xsl:if test="$csv = 'true'">csv</xsl:if>
				</xsl:with-param>
				<xsl:with-param name="my_timestamp">
					<xsl:value-of select="@value"/>
				</xsl:with-param>
				<xsl:with-param name="wb_lang">
					<xsl:value-of select="$wb_lang"/>
				</xsl:with-param>
				<xsl:with-param name="dis_time">T</xsl:with-param>
			</xsl:call-template>
		</span>
		<xsl:apply-templates select="link_list" mode="gen_field">
			<xsl:with-param name="csv" select="$csv"/>
			<xsl:with-param name="text_class">
				<xsl:value-of select="$text_class"/>
			</xsl:with-param>
		</xsl:apply-templates>
		<br/>
	</xsl:template>
	<!-- ================================================================== -->
	<xsl:template match=" *[@type = 'datetime_unlimited']" mode="gen_field">
		<xsl:param name="csv"/>
		<xsl:param name="text_class"/>
		<xsl:variable name="lab_unlimited">
			<xsl:choose>
				<xsl:when test="$wb_lang='ch'">不限</xsl:when>
				<xsl:when test="$wb_lang='gb'">不限</xsl:when>
				<xsl:otherwise>Unlimited</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:choose>
			<xsl:when test="@value != '9999-12-31 23:59:59.000' and @value != '9999-12-31 23:59:59.0' and @value != ''">
				<span class="{$text_class}">
					<xsl:if test="title">
						<xsl:value-of select="title/desc[@lan = $wb_lang_encoding]/@name"/>
						<xsl:text>：&#160;</xsl:text>
					</xsl:if>
					<xsl:call-template name="display_time">
						<xsl:with-param name="mode">
							<xsl:if test="$csv = 'true'">csv</xsl:if>
						</xsl:with-param>
						<xsl:with-param name="my_timestamp">
							<xsl:value-of select="@value"/>
						</xsl:with-param>
						<xsl:with-param name="wb_lang">
							<xsl:value-of select="$wb_lang"/>
						</xsl:with-param>
						<xsl:with-param name="dis_time">T</xsl:with-param>
					</xsl:call-template>
				</span>
				<xsl:apply-templates select="link_list" mode="gen_field">
					<xsl:with-param name="csv" select="$csv"/>
					<xsl:with-param name="text_class">
						<xsl:value-of select="$text_class"/>
					</xsl:with-param>
				</xsl:apply-templates>
				<br/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:if test="title">
						<xsl:value-of select="title/desc[@lan = $wb_lang_encoding]/@name"/>
						<xsl:text>：&#160;</xsl:text>
				</xsl:if>
				<xsl:value-of select="$lab_unlimited"/>
				<xsl:apply-templates select="link_list" mode="gen_field">
					<xsl:with-param name="csv" select="$csv"/>
					<xsl:with-param name="text_class">
						<xsl:value-of select="$text_class"/>
					</xsl:with-param>
				</xsl:apply-templates>
				<br/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ================================================================== -->
	<xsl:template match=" *[@type = 'content_eff_start_end']" mode="gen_field">
		<xsl:param name="csv"/>
		<xsl:param name="text_class"/>
		<xsl:variable name="value" select="*[name() != 'title']/@value"/>
		<xsl:variable name="lab_unlimited">
			<xsl:choose>
				<xsl:when test="$wb_lang='ch'">不限</xsl:when>
				<xsl:when test="$wb_lang='gb'">不限</xsl:when>
				<xsl:otherwise>Unlimited</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="lab_content_eff_start">
			<xsl:choose>
				<xsl:when test="$wb_lang='ch'">報名成功當日</xsl:when>
				<xsl:when test="$wb_lang='gb'">报名成功当日</xsl:when>
				<xsl:otherwise>When successfully enrolled</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="lab_days">
			<xsl:choose>
				<xsl:when test="$wb_lang='ch'">天</xsl:when>
				<xsl:when test="$wb_lang='gb'">天</xsl:when>
				<xsl:otherwise>days</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="lab_duration_suffix">
			<xsl:choose>
				<xsl:when test="$wb_lang='ch'">(從報名被成功錄取開始計算)</xsl:when>
				<xsl:when test="$wb_lang='gb'">(从报名被成功录取开始计算)</xsl:when>
				<xsl:otherwise>(after successful course enrollment)</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<table>
			<tr>
				<td width="2%"  align="left">
					<xsl:value-of select="$lab_const_from"/><xsl:text>：&#160;</xsl:text>
				</td>
				<td width="99%" align="left">
					<xsl:value-of select="$lab_content_eff_start"/>
				</td>
			</tr>
			<tr>
				<td valign="top">
					<xsl:value-of select="$lab_const_cap_to"/>
					<xsl:text>：&#160;</xsl:text>
				</td>
				<td valign="top">
					<xsl:choose>
						<xsl:when test="string-length($value) = 0">
							<xsl:value-of select="$lab_unlimited"/>
						</xsl:when>
						<xsl:when test="string-length(substring-after($value,'-')) &gt; 0">
							<xsl:call-template name="display_time">
								<xsl:with-param name="mode">
									<xsl:if test="$csv = 'true'">csv</xsl:if>
								</xsl:with-param>
								<xsl:with-param name="my_timestamp">
									<xsl:value-of select="$value"/>
								</xsl:with-param>
								<xsl:with-param name="wb_lang">
									<xsl:value-of select="$wb_lang"/>
								</xsl:with-param>
								<xsl:with-param name="dis_time">F</xsl:with-param>
							</xsl:call-template>
						</xsl:when>
						<xsl:when test="string-length(substring-after($value,'-')) = 0">
							<xsl:value-of select="$value"/>
							<xsl:value-of select="$lab_days"/>
							<xsl:text>&#160;</xsl:text>
							<xsl:value-of select="$lab_duration_suffix"/>
						</xsl:when>
					</xsl:choose>
				</td>
			</tr>
		</table>
	</xsl:template>
	<!-- ================================================================== -->
	<xsl:template match="*[@type = 'file']" mode="gen_field">
		<xsl:param name="csv"/>
		<xsl:param name="text_class"/>
		<a href="/item/{/applyeasy/item/@id}/{@value}" target="_blank" class="{$text_class}">
			<xsl:value-of select="@value"/>
		</a>
		<xsl:apply-templates select="link_list" mode="gen_field">
			<xsl:with-param name="csv" select="$csv"/>
			<xsl:with-param name="text_class">
				<xsl:value-of select="$text_class"/>
			</xsl:with-param>
		</xsl:apply-templates>
		<br/>
	</xsl:template>
	<!-- ================================================================== -->
	<xsl:template match="*[@type = 'url' and @value!='']" mode="gen_field">
		<xsl:param name="csv"/>
		<xsl:param name="text_class"/>
		<xsl:variable name="url_value">
			<xsl:choose>
				<xsl:when test="string-length(@value) > 60">
					<xsl:value-of select="substring(@value,0,60)"/>... 
  			</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="@value"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:if test="title and name() = 'subfield'">
			<xsl:value-of select="title/desc[@lan = $wb_lang_encoding]/@name"/>
			<xsl:text>：&#160;</xsl:text>
		</xsl:if>
		<a href="{@value}" target="_blank" class="{$text_class}">
			<xsl:value-of select="$url_value"/>
		</a>
		<xsl:apply-templates select="link_list" mode="gen_field">
			<xsl:with-param name="csv" select="$csv"/>
			<xsl:with-param name="text_class">
				<xsl:value-of select="$text_class"/>
			</xsl:with-param>
		</xsl:apply-templates>
		<br/>
	</xsl:template>
	<!-- ================================================================== -->
	<xsl:template match="*[@type = 'select' or @type='reloading_select']" mode="gen_field">
		<xsl:param name="csv"/>
		<xsl:param name="text_class"/>
		<xsl:variable name="my_val" select="@value"/>
		<xsl:choose>
			<xsl:when test="*[@value = $my_val]/title/desc[@lan]">
				<xsl:value-of select="*[@value = $my_val]/title/desc[@lan = $wb_lang_encoding]/@name"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="*[@value = $my_val]/title/desc/@name"/>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:apply-templates select="link_list" mode="gen_field">
			<xsl:with-param name="csv" select="$csv"/>
			<xsl:with-param name="text_class">
				<xsl:value-of select="$text_class"/>
			</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- ================================================================== -->
	<xsl:template match="*[@type = 'constant_label'] " mode="gen_field">
		<xsl:param name="text_class"/>
		<xsl:choose>
			<xsl:when test="name()='workflow_template'">
				<xsl:variable name="tpl_id" select="id/@value"/>
				<xsl:variable name="reference_tag" select="@ext_value_label_tag"/>
				<xsl:value-of select="/applyeasy/meta/label_reference_data_list/label[@name = $reference_tag]/element[@id = $tpl_id]/title"/>
				<br/>
				<xsl:call-template name="unescape_html_linefeed">
					<xsl:with-param name="my_right_value">
						<xsl:value-of select="/applyeasy/meta/label_reference_data_list/label[@name = $reference_tag]/element[@id = $tpl_id]/description"/>
					</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="name()='km_object_status'">
				<xsl:variable name="my_val" select="status/@value"/>
				<xsl:call-template name="sub_value">
					<xsl:with-param name="org_value">
						<xsl:value-of select="*[@id = $my_val]/desc[@lan = $wb_lang_encoding]/@name"/>
					</xsl:with-param>
				</xsl:call-template>
				<xsl:apply-templates select="link_list" mode="gen_field">
					<xsl:with-param name="text_class">
						<xsl:value-of select="$text_class"/>
					</xsl:with-param>
				</xsl:apply-templates>
			</xsl:when>
			<xsl:otherwise>
				<xsl:variable name="my_val">
					<xsl:choose>
						<xsl:when test="*[name() != 'title']/@value!=''">
							<xsl:value-of select="*[name() != 'title']/@value"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="@value"/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				<xsl:call-template name="sub_value">
					<xsl:with-param name="org_value">
						<xsl:value-of select="*[@id = $my_val]/desc[@lan = $wb_lang_encoding]/@name"/>
					</xsl:with-param>
				</xsl:call-template>
				<xsl:apply-templates select="link_list" mode="gen_field">
					<xsl:with-param name="text_class">
						<xsl:value-of select="$text_class"/>
					</xsl:with-param>
				</xsl:apply-templates>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ================================================================== -->
	<xsl:template match="*[@type = 'radio'] | *[@type = 'checkbox'] | *[@type = 'radio_bonus']" mode="gen_field">
		<xsl:param name="text_class"/>
		<xsl:choose>
			<xsl:when test="name() = 'item_status'">
				<xsl:for-each select="*[name()!='title']">
					<xsl:if test="../../../../@status = @value">
						<table>
							<tr>
								<td>
									<xsl:value-of select="title/desc[@lan = $wb_lang_encoding]/@name"/>
									<xsl:if test="position() != last()">
										<br/>
									</xsl:if>
								</td>
							</tr>
						</table>
					</xsl:if>						
				</xsl:for-each>
			</xsl:when>
			<xsl:when test="@name = 'Certificate_of_completion'">
				<xsl:for-each select="*[name()!='title' and @selected='true']">
					<table>
						<tr>
							<td>
								<xsl:value-of select="title/desc[@lan = $wb_lang_encoding]/@name"/>
								<xsl:if test="@value='true'">
									<xsl:variable name="itm_cfc_id">
										 <xsl:value-of select="/applyeasy/item/@itm_cfc_id"/>
									</xsl:variable>
									&#160;&lt;&lt;&#160;<xsl:value-of select="//cert_lst/cert[@cfc_id=$itm_cfc_id]/@cfc_title"/>&#160;&gt;&gt;
							     </xsl:if>
								<xsl:if test="position() != last()">
									<br/>
								</xsl:if>
								
							</td>
						</tr>
					</table>
				</xsl:for-each>
			</xsl:when>
			<xsl:otherwise>			
			<xsl:for-each select="*[name()!='title' and @selected='true']">
				<table>
					<tr>
						<td>
							<xsl:value-of select="title/desc[@lan = $wb_lang_encoding]/@name"/>
							<xsl:if test="position() != last()">
								<br/>
							</xsl:if>
						</td>
					</tr>
				</table>
			</xsl:for-each>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:apply-templates select="link_list" mode="gen_field">
			<xsl:with-param name="text_class">
				<xsl:value-of select="$text_class"/>
			</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="*[@type = 'resource']" mode="gen_field">
		<xsl:param name="csv">false</xsl:param>
		<xsl:param name="text_class"/>
		<xsl:choose>
			<xsl:when test="count(resource_list/resource) !=0">
				<table>
					<xsl:for-each select="resource_list/resource">
						<tr>
							<td>
								<a href="javascript:itm_lst.get_res_detail({@id})" class="{$text_class}">
									<xsl:value-of select="."/>
								</a>
							</td>
						</tr>
					</xsl:for-each>
				</table>
			</xsl:when>
			<xsl:otherwise>&#160;</xsl:otherwise>
		</xsl:choose>
		<xsl:apply-templates select="link_list" mode="gen_field">
			<xsl:with-param name="text_class">
				<xsl:value-of select="$text_class"/>
			</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="*[@type = 'filesize']" mode="gen_field">
		<xsl:param name="text_class"/>
		111
		<xsl:for-each select="file">
			<xsl:value-of select="@name"/>
			<xsl:text>&#160;(</xsl:text>
			<xsl:value-of select="@size"/>
			<xsl:text>)</xsl:text>
			<xsl:if test="position() != last()">
				<br/>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="*[@type = 'read'] " mode="gen_field">
		<xsl:param name="csv"/>
		<xsl:param name="text_class"/>
		<xsl:param name="lab_approval_status_preapprove"/>
		<xsl:param name="lab_approval_status_pending_approval"/>
		<xsl:param name="lab_approval_status_approved"/>
		<xsl:param name="lab_approval_status_approved_off"/>
		<xsl:param name="lab_approval_status_pending_reapproval"/>
		<xsl:param name="lab_approval_action_req_appr"/>
		<xsl:param name="lab_approval_action_cancel_req_appr"/>
		<xsl:param name="lab_approval_action_appr_pub"/>
		<xsl:param name="lab_approval_action_decline_appr_pub"/>
		<xsl:variable name="pkg_file" select="/applyeasy/item/@offline_pkg"/>
		<xsl:variable name="pkg_file_name" select="/applyeasy/item/@offline_pkg_file"/>
			<xsl:choose>
				<xsl:when test="@external_field='yes'">
					<xsl:choose>
						<xsl:when test="name() = 'approval_status'">
							<xsl:variable name="approval_status">
								<xsl:value-of select="/applyeasy/item/approval_status/text()"/>
							</xsl:variable>
							<xsl:choose>
								<xsl:when test="$approval_status = 'PREAPPROVE'">
									<xsl:value-of select="$lab_approval_status_preapprove"/>
								</xsl:when>
								<xsl:when test="$approval_status = 'PENDING_APPROVAL'">
									<xsl:value-of select="$lab_approval_status_pending_approval"/>
								</xsl:when>
								<xsl:when test="$approval_status = 'APPROVED'">
									<xsl:value-of select="$lab_approval_status_approved"/>
								</xsl:when>
								<xsl:when test="$approval_status = 'APPROVED_OFF'">
									<xsl:value-of select="$lab_approval_status_approved_off"/>
								</xsl:when>
								<xsl:when test="$approval_status = 'PENDING_REAPPROVAL'">
									<xsl:value-of select="$lab_approval_status_pending_reapproval"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="$approval_status"/>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
						<xsl:when test="name() = 'itm_offline_pkg' and $pkg_file!=''">
							<a href="../cp/{/applyeasy/item/@id}/{$pkg_file}" target="_blank">
								<xsl:value-of select="$pkg_file_name"/>
							</a>
						</xsl:when>
						<xsl:when test="name() = 'created_by'">
							<xsl:value-of select="/applyeasy/item/creator/text()"/>
						</xsl:when>
						<xsl:when test="name() = 'updated_by'">
							<xsl:value-of select="/applyeasy/item/last_updated/text()"/>
						</xsl:when>
						<xsl:when test="name() = 'submit_action'">
							<xsl:call-template name="approval_action">
								<xsl:with-param name="action" select="/applyeasy/item/submitted/@action"/>
								<xsl:with-param name="lab_approval_action_req_appr" select="$lab_approval_action_req_appr"/>
								<xsl:with-param name="lab_approval_action_cancel_req_appr" select="$lab_approval_action_cancel_req_appr"/>
								<xsl:with-param name="lab_approval_action_appr_pub" select="$lab_approval_action_appr_pub"/>
								<xsl:with-param name="lab_approval_action_decline_appr_pub" select="$lab_approval_action_decline_appr_pub"/>
							</xsl:call-template>
						</xsl:when>
						<xsl:when test="name() = 'submit_by'">
							<xsl:value-of select="/applyeasy/item/submitted/text()"/>
						</xsl:when>
						<xsl:when test="name() = 'approval_action'">
							<xsl:call-template name="approval_action">
								<xsl:with-param name="action" select="/applyeasy/item/approver/@action"/>
								<xsl:with-param name="lab_approval_action_req_appr" select="$lab_approval_action_req_appr"/>
								<xsl:with-param name="lab_approval_action_cancel_req_appr" select="$lab_approval_action_cancel_req_appr"/>
								<xsl:with-param name="lab_approval_action_appr_pub" select="$lab_approval_action_appr_pub"/>
								<xsl:with-param name="lab_approval_action_decline_appr_pub" select="$lab_approval_action_decline_appr_pub"/>
							</xsl:call-template>
						</xsl:when>
						<xsl:when test="name() = 'approve_by'">
							<xsl:value-of select="/applyeasy/item/approver/text()"/>
						</xsl:when>
						<xsl:otherwise>Unknown field: <xsl:value-of select="name()"/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="@value"/>
				</xsl:otherwise>
			</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="approval_action">
		<xsl:param name="action"/>
		<xsl:param name="lab_approval_action_req_appr"/>
		<xsl:param name="lab_approval_action_cancel_req_appr"/>
		<xsl:param name="lab_approval_action_appr_pub"/>
		<xsl:param name="lab_approval_action_decline_appr_pub"/>
		<xsl:choose>
			<xsl:when test="$action = 'REQ_APPR'">
				<xsl:value-of select="$lab_approval_action_req_appr"/>
			</xsl:when>
			<xsl:when test="$action = 'CANCEL_REQ_APPR'">
				<xsl:value-of select="$lab_approval_action_cancel_req_appr"/>
			</xsl:when>
			<xsl:when test="$action = 'APPR_PUB'">
				<xsl:value-of select="$lab_approval_action_appr_pub"/>
			</xsl:when>
			<xsl:when test="$action = 'DECLINE_APPR_PUB'">
				<xsl:value-of select="$lab_approval_action_decline_appr_pub"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$action"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="*[@type = 'read_datetime']" mode="gen_field">
		<xsl:param name="csv"/>
		<xsl:param name="text_class"/>
			<xsl:choose>
				<xsl:when test="@external_field='yes'">
					<xsl:variable name="date">
						<xsl:choose>
							<xsl:when test="name() = 'create_date'">
								<xsl:value-of select="/applyeasy/item/creator/@timestamp"/>
							</xsl:when>
							<xsl:when test="name() = 'update_date'">
								<xsl:value-of select="/applyeasy/item/last_updated/@timestamp"/>
							</xsl:when>
							<xsl:when test="name() = 'submit_date'">
								<xsl:value-of select="/applyeasy/item/submitted/@timestamp"/>
							</xsl:when>
							<xsl:when test="name() = 'approve_date'">
								<xsl:value-of select="/applyeasy/item/approver/@timestamp"/>
							</xsl:when>
							<xsl:otherwise>Unknown field: <xsl:value-of select="name()"/>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:variable>
					<xsl:call-template name="display_time">
						<xsl:with-param name="mode">
							<xsl:if test="$csv = 'true'">csv</xsl:if>
						</xsl:with-param>
						<xsl:with-param name="wb_lang">
							<xsl:value-of select="$wb_lang"/>
						</xsl:with-param>
						<xsl:with-param name="dis_time">T</xsl:with-param>
						<xsl:with-param name="my_timestamp">
							<xsl:value-of select="$date"/>
						</xsl:with-param>
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="display_time">
						<xsl:with-param name="mode">
							<xsl:if test="$csv = 'true'">csv</xsl:if>
						</xsl:with-param>
						<xsl:with-param name="wb_lang">
							<xsl:value-of select="$wb_lang"/>
						</xsl:with-param>
						<xsl:with-param name="dis_time">T</xsl:with-param>
						<xsl:with-param name="my_timestamp">
							<xsl:value-of select="@value"/>
						</xsl:with-param>
					</xsl:call-template>
				</xsl:otherwise>
			</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="*[@type = 'km_domain_attachment']" mode="gen_field">
		<xsl:param name="text_class"/>
		<xsl:for-each select="ancestor_node_list">
			<xsl:for-each select="node">
				<a href="javascript:kmFolder.folder_lst('{$sys_type}','DOMAIN',{@id})" class="{$text_class}">
					<xsl:value-of select="title"/>
				</a>
				<xsl:if test="position() != last()">
					<span class="{$text_class}">&#160;&gt;&#160;</span>
				</xsl:if>
			</xsl:for-each>
			<xsl:if test="position() != last()">
				<br/>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="*[@type = 'km_domain']" mode="gen_field">
		<xsl:param name="text_class"/>
		<xsl:for-each select="ancestor_node_list">
			<xsl:for-each select="node">111
				<xsl:value-of select="title"/>
				<xsl:if test="position() != last()">
					<span class="{$text_class}">&#160;&gt;&#160;</span>
				</xsl:if>
			</xsl:for-each>
			<xsl:if test="position() != last()">
				<br/>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>
	<!-- =============================================================== -->
	<!-- External Field Type ================================================== -->
	<xsl:template match="*[@type = 'single_checkbox']" mode="gen_field">
		<xsl:param name="text_class"/>
		<xsl:variable name="my_id">
			<xsl:value-of select="@id"/>
		</xsl:variable>
		<xsl:if test="../../mote_level_list/mote_level[@id = $my_id]/@selected = 'true'">
			<xsl:if test="not(number(../../mote_level_list/mote_level[@selected='true']/@id)=number($my_id))">,&#160;</xsl:if>
			<xsl:value-of select="title/desc[@lan = $wb_lang_encoding]/@name"/>
		</xsl:if>
		<xsl:apply-templates select="link_list" mode="gen_field">
			<xsl:with-param name="text_class">
				<xsl:value-of select="$text_class"/>
			</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="*[@type = 'user_group_pickup' or @type = 'targeted_lrn_pickup']" mode="gen_field">
		<xsl:param name="csv"/>
		<xsl:param name="text_class"/>
		<xsl:if test="target_list/target">
			<table>
				<xsl:for-each select="target_list/target">
					<tr>
						<td>
							<xsl:value-of select="entity[@type='USG']/@display_bil"/>&#160;
						</td>
						<td width="20">
						</td>
						<xsl:if test="../../@type = 'targeted_lrn_pickup'">
							<td>
								<xsl:value-of select="entity[@type='UGR']/@display_bil"/>&#160;
							</td>
							<td width="20">
							</td>
							<td>
								<xsl:value-of select="entity[@type='SKILL']/@display_bil"/>&#160;
							</td>
						</xsl:if>
					</tr>
				</xsl:for-each>
			</table>
		</xsl:if>
		<xsl:apply-templates select="link_list" mode="gen_field">
			<xsl:with-param name="text_class">
				<xsl:value-of select="$text_class"/>
			</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="*[@type = 'competency_pickup']" mode="gen_field">
		<xsl:param name="text_class"/>
		<xsl:for-each select="skill_list/skill">
			<xsl:value-of select="@title"/>
			<xsl:if test="position() != last()">
				<br/>
			</xsl:if>
		</xsl:for-each>
		<xsl:apply-templates select="link_list" mode="gen_field">
			<xsl:with-param name="text_class">
				<xsl:value-of select="$text_class"/>
			</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="*[@type = 'item_access_pickup']" mode="gen_field">
		<xsl:param name="text_class"/>
		<xsl:for-each select="assigned_role_list/role/entity">
			<xsl:value-of select="@display_bil"/>
			<xsl:if test="position() != last()">
				<br/>
			</xsl:if>
		</xsl:for-each>
		<xsl:apply-templates select="link_list" mode="gen_field">
			<xsl:with-param name="text_class">
				<xsl:value-of select="$text_class"/>
			</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="*[@type = 'catalog_attachment']" mode="gen_field">
		<xsl:param name="text_class"/>
		<xsl:param name="csv">false</xsl:param>
		<xsl:param name="xls">false</xsl:param>
		<xsl:param name="show_link">true</xsl:param>
		<xsl:param name="this"/>
		<xsl:variable name="hasCataLink">
			<xsl:choose>
				<xsl:when test="$show_link = 'false'">false</xsl:when>
				<xsl:when test="/applyeasy/meta/cur_usr/granted_functions/functions/function[@id = 'FTN_AMD_CAT_MAIN' or @id = 'GLB_CAT_MAIN']">true</xsl:when>
				<xsl:otherwise>false</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="hasCataMain">
			<xsl:choose>
				<xsl:when test="/applyeasy/meta/cur_usr/granted_functions/functions/function[@id = 'FTN_AMD_CAT_MAIN']">true</xsl:when>
				<xsl:otherwise>false</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="hasGlbCataMain">
			<xsl:choose>
				<xsl:when test="/applyeasy/meta/cur_usr/granted_functions/functions/function[@id = 'GLB_CAT_MAIN']">true</xsl:when>
				<xsl:otherwise>false</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="hasCataOffLink">
			<xsl:choose>
				<xsl:when test="$hasCataLink = 'false'">false</xsl:when>
				<xsl:when test="/applyeasy/meta/page_variant/@hasCataOffPrivilege = 'true'">true</xsl:when>
				<xsl:otherwise>false</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:for-each select="node_list/node/nav">
			<xsl:variable name="public_ind" select="../@public_ind"/>
			<xsl:variable name="direct_parent" select="node[(position()+1) = last()]"/>
			<xsl:for-each select="node">
				<xsl:variable name="catalog_path">
					<xsl:if test="position() != last()">
						<xsl:value-of select="title"/>
						<xsl:if test="(position()+1) != last()">
							<xsl:choose>
								<xsl:when test="$csv = 'true'">&gt;</xsl:when>
								<xsl:otherwise>
									<xsl:text>&#160;&gt;&#160;</xsl:text>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:if>
					</xsl:if>
				</xsl:variable>
				<xsl:choose>
					<xsl:when test="$hasCataLink = 'false' or ($direct_parent/status = 'OFF' and $hasCataOffLink = 'false') ">
						<xsl:value-of select="$catalog_path"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:choose>
							<xsl:when test="$hasCataMain = 'true'">
								<a href="javascript:wb_utils_node_lst({$direct_parent/@node_id})" class="text">
									<xsl:value-of select="$catalog_path"/>
								</a>
							</xsl:when>
							<xsl:when test="$hasGlbCataMain = 'true' and $public_ind = 'true'">
								<a href="javascript:wb_utils_glb_node_lst({$direct_parent/@node_id})" class="text">
									<xsl:value-of select="$catalog_path"/>
								</a>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$catalog_path"/>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:for-each>
			<xsl:if test="position() != last()">
				<xsl:choose>
					<xsl:when test="$xls = 'true'">
						<xsl:text>,&#160;</xsl:text>
					</xsl:when>
					<xsl:otherwise>
						<br/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:if>
		</xsl:for-each>
		
		<xsl:apply-templates select="link_list" mode="gen_field">
			<xsl:with-param name="text_class">
				<xsl:value-of select="$text_class"/>
			</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- ======================================== -->
	<xsl:template match="*[@type = 'tcr_pickup']" mode="gen_field">
		<xsl:param name="text_class"/>
		<xsl:value-of select="center/text()"/>
		<xsl:apply-templates select="link_list" mode="gen_field">
			<xsl:with-param name="text_class">
				<xsl:value-of select="$text_class"/>
			</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- ======================================== -->
	<!-- == ITEM TYPE == -->
	<xsl:template match="*[@type = 'itm_type']" mode="gen_field">
		<xsl:param name="text_class"/>
		<xsl:param name="csv">false</xsl:param>
			<!-- HardCode -->
			<xsl:choose>
				<xsl:when test="not(/applyeasy/item/item_type)">
					<xsl:variable name="my_type">
						<xsl:value-of select="../../../../item/@dummy_type"/>
					</xsl:variable>
					<xsl:call-template name="get_ity_title">
						<xsl:with-param name="itm_type" select="$my_type"/>
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="get_ity_title">
						<xsl:with-param name="itm_type" select="/applyeasy/item/item_type/@dummy_type"/>
					</xsl:call-template>
				</xsl:otherwise>
			</xsl:choose>
			<!--HardCode-->
	</xsl:template>
	<!-- ======================================== -->
	<!-- == Approval Request == -->
	<xsl:template match="*[@type = 'approval_ind']" mode="gen_field">
		<xsl:param name="text_class"/>
		<xsl:param name="csv">false</xsl:param>
		<xsl:variable name="lab_yes">
			<xsl:choose>
				<xsl:when test="$wb_lang='ch'">是</xsl:when>
				<xsl:when test="$wb_lang='gb'">是</xsl:when>
				<xsl:otherwise>Yes</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="lab_no">
			<xsl:choose>
				<xsl:when test="$wb_lang='ch'">否</xsl:when>
				<xsl:when test="$wb_lang='gb'">否</xsl:when>
				<xsl:otherwise>No</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:choose>
			<xsl:when test="//workflow_template/@approval_ind = 'true'">
				<xsl:value-of select="$lab_yes"/>
			</xsl:when>
			<xsl:when test="//workflow_template/@approval_ind = 'false'">
				<xsl:value-of select="$lab_no"/>
			</xsl:when>
			<xsl:otherwise>OTHERWISE<xsl:value-of select="$lab_no"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
	<!-- External Field ====================================================== -->
	<xsl:template match="run_item_access" mode="gen_field" priority="10">
		<xsl:param name="text_class"/>
		<xsl:variable name="my_id" select="@id"/>
		<xsl:for-each select="assigned_role_list/role[@id = $my_id]/entity[@selected = 'true']">
			<span class="{$text_class}">
				<xsl:value-of select="@display_bil"/>
			</span>
			<xsl:if test="position() != last()">
				<br/>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>
	<!-- ======================================== -->
	<xsl:template match="mote_due_date" mode="gen_field" priority="10">
		<xsl:param name="csv"/>
		<xsl:param name="text_class"/>
		<xsl:if test="date/@value != ''">
			<span class="{$text_class}">
				<xsl:call-template name="display_time">
					<xsl:with-param name="mode">
						<xsl:if test="$csv = 'true'">csv</xsl:if>
					</xsl:with-param>
					<xsl:with-param name="my_timestamp">
						<xsl:value-of select="date/@value"/>
					</xsl:with-param>
					<xsl:with-param name="wb_lang">
						<xsl:value-of select="$wb_lang"/>
					</xsl:with-param>
				</xsl:call-template>
			</span>
		</xsl:if>
		<xsl:apply-templates select="link_list" mode="gen_field">
			<xsl:with-param name="text_class">
				<xsl:value-of select="$text_class"/>
			</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="mote_cost_target" mode="gen_field" priority="10">
		<xsl:param name="text_class"/>
		<span class="{$text_class}">
			<xsl:value-of select="cost_target/@value"/>
		</span>
		<xsl:apply-templates select="link_list" mode="gen_field">
			<xsl:with-param name="text_class">
				<xsl:value-of select="$text_class"/>
			</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="mote_time_target" mode="gen_field" priority="10">
		<xsl:param name="text_class"/>
		<span class="{$text_class}">
			<xsl:value-of select="time_target/@value"/>
		</span>
		<xsl:apply-templates select="link_list" mode="gen_field">
			<xsl:with-param name="text_class">
				<xsl:value-of select="$text_class"/>
			</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="mote_target_rating" mode="gen_field" priority="10">
		<xsl:param name="text_class"/>
		<span class="{$text_class}">
			<xsl:value-of select="rating_target/@value"/>
		</span>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="targeted_lrn_num" mode="gen_field" priority="10">
		<xsl:param name="text_class"/>
		<xsl:value-of select="num/@value"/>
		<xsl:apply-templates select="link_list" mode="gen_field">
			<xsl:with-param name="text_class">
				<xsl:value-of select="$text_class"/>
			</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="item_access" mode="gen_field" priority="10">
		<xsl:param name="text_class"/>
		<xsl:param name="csv"/>
		<xsl:for-each select="assigned_role_list/role">
			<xsl:for-each select="entity">
				<xsl:choose>
					<xsl:when test="@biography = 'true'">
						<a href="javascript:usr.user.get_usr_biography_popup('{@id}')" class="{$text_class}">
							<xsl:value-of select="@display_bil"/>
						</a>
					</xsl:when>
					<xsl:otherwise>
						<span class="{$text_class}">
							<xsl:value-of select="@display_bil"/>
						</span>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:if test="position() != last()">
					<xsl:choose>
						<xsl:when test="$csv = 'true'">,</xsl:when>
						<xsl:otherwise>&#160;;&#160;</xsl:otherwise>
					</xsl:choose>
				</xsl:if>
			</xsl:for-each>
			<xsl:if test="position() != last()">
				<br/>
			</xsl:if>
		</xsl:for-each>
		<xsl:apply-templates select="link_list" mode="gen_field">
			<xsl:with-param name="text_class">
				<xsl:value-of select="$text_class"/>
			</xsl:with-param>
		</xsl:apply-templates>
		<xsl:if test="suffix">
			<br/>
		</xsl:if>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="catatlog" mode="gen_field" priority="10">
		<xsl:param name="text_class"/>
		<xsl:param name="csv">false</xsl:param>
		<xsl:for-each select="node_list/node/nav">
			<xsl:for-each select="node">
				<xsl:value-of select="title"/>
				<xsl:if test="position() != last()">
					<xsl:choose>
						<xsl:when test="$csv = 'true'">&gt;</xsl:when>
						<xsl:otherwise>
							<xsl:text>&#160;&gt;&#160;</xsl:text>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:if>
			</xsl:for-each>
			<xsl:if test="position() != last()">
				<br/>
			</xsl:if>
		</xsl:for-each>
		<xsl:apply-templates select="link_list" mode="gen_field">
			<xsl:with-param name="text_class">
				<xsl:value-of select="$text_class"/>
			</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="competency" mode="gen_field" priority="10">
		<xsl:param name="text_class"/>
		<xsl:for-each select="skill_list/skill">
			<xsl:value-of select="@title"/>
			<xsl:if test="position() != last()">
				<br/>
			</xsl:if>
		</xsl:for-each>
		<xsl:apply-templates select="link_list" mode="gen_field">
			<xsl:with-param name="text_class">
				<xsl:value-of select="$text_class"/>
			</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="figure_type_list" mode="gen_field" priority="10">
		<xsl:param name="text_class"/>
		<table>
			<xsl:for-each select="figure_type">
				<xsl:if test="num/@value">
					<tr>
						<td valign="top" width="100">
							<xsl:call-template name="get_desc"/>:
						</td>
						<td valign="top">
							<xsl:choose>
								<xsl:when test="num/@value">
									<xsl:value-of select="format-number(num/@value, '0.00')"/>
								</xsl:when>
								<xsl:otherwise>--</xsl:otherwise>
							</xsl:choose>
						</td>
					</tr>
				</xsl:if>
			</xsl:for-each>
		</table>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="mote_plan_type" mode="gen_field" priority="10">
		<xsl:param name="text_class"/>
		<table>
			<xsl:for-each select="resource_list/resource">
				<tr>
					<td>
						<a href="javascript:itm_lst.get_res_detail({@id})" class="{$text_class}">
							<xsl:value-of select="."/>
						</a>
					</td>
				</tr>
			</xsl:for-each>
		</table>
		<xsl:apply-templates select="link_list" mode="gen_field">
			<xsl:with-param name="text_class">
				<xsl:value-of select="$text_class"/>
			</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- == auto enroll target learners == -->
	<xsl:template match="auto_enroll_target_learners" mode="gen_field" priority="10">
		<xsl:param name="text_class"/>
		<xsl:variable name="_value" select="../../../@itm_enroll_type"/>
		<xsl:variable name="lab_no">
			<xsl:choose>
				<xsl:when test="$wb_lang='ch'">否</xsl:when>
				<xsl:when test="$wb_lang='gb'">否</xsl:when>
				<xsl:otherwise>No</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="lab_auto_confirm">
			<xsl:choose>
				<xsl:when test="$wb_lang='ch'">是，目標學員可以立刻開始學習此課程</xsl:when>
				<xsl:when test="$wb_lang='gb'">是，目标学员可以立刻开始学习此课程</xsl:when>
				<xsl:otherwise>Yes and enrolled learner can start learning immediately.</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="lab_auto_enroll">
			<xsl:choose>
				<xsl:when test="$wb_lang='ch'">是，目標學員在通過審批後才能開始學習該課程</xsl:when>
				<xsl:when test="$wb_lang='gb'">是，目标学员在通过审批后才能开始学习该课程</xsl:when>
				<xsl:otherwise>Yes and enrolled learner can start learning only after being approved</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="lab_quota">
			<xsl:choose>
				<xsl:when test="$wb_lang='ch'">此功能不受名額限制。</xsl:when>
				<xsl:when test="$wb_lang='gb'">此功能不受名额限制。</xsl:when>
				<xsl:otherwise>Auto-enrollment is not limited by quota.</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="lab_note">
			<xsl:choose>
				<xsl:when test="../../../auto_enroll_interval > 0">
					<xsl:choose>
						<xsl:when test="$wb_lang='ch'">(自動報名功能每隔<xsl:value-of select="../../../auto_enroll_interval"/>分鐘執行一次。<xsl:value-of select="$lab_quota"/>)</xsl:when>
						<xsl:when test="$wb_lang='gb'">(自动报名功能每隔<xsl:value-of select="../../../auto_enroll_interval"/>分钟执行一次。<xsl:value-of select="$lab_quota"/>)</xsl:when>
						<xsl:otherwise>(Auto-enrollment will be executed every <xsl:value-of select="../../../auto_enroll_interval"/> minutes.&#160;<xsl:value-of select="$lab_quota"/>)</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
				<xsl:otherwise>
					<xsl:choose>
						<xsl:when test="$wb_lang='ch'">(自動報名功能未啟用。<xsl:value-of select="$lab_quota"/>)</xsl:when>
						<xsl:when test="$wb_lang='gb'">(自动报名功能未启用。<xsl:value-of select="$lab_quota"/>)</xsl:when>
						<xsl:otherwise>(Auto-enrollment is not in use.<xsl:value-of select="$lab_quota"/>)</xsl:otherwise>
					</xsl:choose>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<table>
			<tr>
				<td>
					<xsl:choose>
						<xsl:when test="$_value=''">
							<xsl:value-of select="$lab_no"/>
						</xsl:when>
						<xsl:when test="$_value='TARGET_AUTO_CONFIRM'">
							<xsl:value-of select="$lab_auto_confirm"/>
						</xsl:when>
						<xsl:when test="$_value='TARGET_AUTO_ENROLL'">
							<xsl:value-of select="$lab_auto_enroll"/>
						</xsl:when>
					</xsl:choose>
					<br/>
					<xsl:value-of select="$lab_note"/>
				</td>
			</tr>
		</table>
	</xsl:template>	
	<!-- =============================================================== -->
	<xsl:template match="link_list" mode="gen_field" priority="10">
		<!-- a deprecated template (once a pwc-only template) 2007-08-15 kawai -->
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="get_show">
		<xsl:choose>
			<xsl:when test="node_list">
				<xsl:choose>
					<xsl:when test="node_list/node">true</xsl:when>
					<xsl:otherwise>false</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:when test="target_list">
				<xsl:choose>
					<xsl:when test="target_list/target">true</xsl:when>
					<xsl:otherwise>false</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:when test="date">
				<xsl:choose>
					<xsl:when test="date/@value != ''">true</xsl:when>
					<xsl:otherwise>false</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:when test="num">
				<xsl:choose>
					<xsl:when test="num/@value != ''">true</xsl:when>
					<xsl:otherwise>false</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:when test="time_target">
				<xsl:choose>
					<xsl:when test="time_target/@value != ''">true</xsl:when>
					<xsl:otherwise>false</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:when test="cost_target">
				<xsl:choose>
					<xsl:when test="cost_target/@value != ''">true</xsl:when>
					<xsl:otherwise>false</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:when test="assigned_role_list">
				<xsl:choose>
					<xsl:when test="assigned_role_list/role/entity">true</xsl:when>
					<xsl:otherwise>false</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:when test="skill_list">
				<xsl:choose>
					<xsl:when test="skill_list/skill">true</xsl:when>
					<xsl:otherwise>false</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:when test="mote_level_list">
				<xsl:choose>
					<xsl:when test="mote_level_list/*[@selected = 'true']">true</xsl:when>
					<xsl:otherwise>false</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:when test="resource_list">
				<xsl:choose>
					<xsl:when test="resource_list/resource">true</xsl:when>
				</xsl:choose>
			</xsl:when>
			<xsl:when test="subfield_list">
				<xsl:apply-templates select="*/subfield[name() != 'title' and name() != 'hidden']" mode="get_subfield_show"/>
			</xsl:when>
			<xsl:when test="rating_target">
				<xsl:choose>
					<xsl:when test="rating_target/@value != ''">true</xsl:when>
				</xsl:choose>
			</xsl:when>
			<xsl:when test="@type = 'figure_value'">
				<xsl:choose>
					<xsl:when test="figure_type/num">true</xsl:when>
					<xsl:otherwise>false</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:when test="@type = 'select'  or @type='reloading_select'">
				<xsl:choose>
					<xsl:when test="not(@value)">false</xsl:when>
					<xsl:when test="@value != ''">true</xsl:when>
				</xsl:choose>
			</xsl:when>
			<xsl:when test="@type = 'catalog_attachment'">
				<xsl:choose>
					<xsl:when test="node_list/node">true</xsl:when>
					<xsl:otherwise>false</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:when test="@type = 'tcr_pickup'">
				<xsl:choose>
					<xsl:when test="center and $tc_enabled = 'true'">true</xsl:when>
					<xsl:otherwise>false</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:when test="@type = 'textarea'">
				<xsl:choose>
					<xsl:when test="@value != ''">true</xsl:when>
					<xsl:otherwise>false</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:when test="@type = 'filesize'">
				<xsl:choose>
					<xsl:when test="file">true</xsl:when>
				</xsl:choose>
			</xsl:when>
			<xsl:when test="@type = 'file'">
				<xsl:choose>
					<xsl:when test="@value != ''">true</xsl:when>
					<xsl:otherwise>false</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:when test="@type = 'km_domain_attachment'">
				<xsl:choose>
					<xsl:when test="ancestor_node_list/node">true</xsl:when>
				</xsl:choose>
			</xsl:when>
			<xsl:when test="@type = 'km_domain'">
				<xsl:choose>
					<xsl:when test="ancestor_node_list/node">true</xsl:when>
				</xsl:choose>
			</xsl:when>
			<xsl:when test="@type = 'pos_int_unlimited'">true</xsl:when>
			<xsl:when test="@type = 'pos_amount_optional'">true</xsl:when>
			<xsl:when test="@type = 'constant_datetime'">
				<xsl:choose>
					<xsl:when test="*/@value">
						<xsl:choose>
							<xsl:when test="*/@value != ''">true</xsl:when>
							<xsl:otherwise>false</xsl:otherwise>
						</xsl:choose>
					</xsl:when>
					<xsl:when test="@value">
						<xsl:choose>
							<xsl:when test="@value != ''">true</xsl:when>
							<xsl:otherwise>false</xsl:otherwise>
						</xsl:choose>
					</xsl:when>
					<xsl:otherwise>false</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:when test="@type = 'read'">
				<xsl:choose>
					<xsl:when test="name() = 'approval_status'">
						<xsl:choose>
							<xsl:when test="/applyeasy/item/approval_status">true</xsl:when>
							<xsl:otherwise>false</xsl:otherwise>
						</xsl:choose>
					</xsl:when>
					<xsl:when test="name() = 'submit_action' or name() = 'submit_by'">
						<xsl:choose>
							<xsl:when test="/applyeasy/item/submitted">true</xsl:when>
							<xsl:otherwise>false</xsl:otherwise>
						</xsl:choose>
					</xsl:when>
					<xsl:when test="name() = 'approval_action' or name() = 'approve_by'">
						<xsl:choose>
							<xsl:when test="/applyeasy/item/approver">true</xsl:when>
							<xsl:otherwise>false</xsl:otherwise>
						</xsl:choose>
					</xsl:when>
					<xsl:when test="name() = 'itm_offline_pkg' ">
						<xsl:choose>
							<xsl:when test="/applyeasy/item/@offline_pkg!=''">true</xsl:when>
							<xsl:otherwise>false</xsl:otherwise>
						</xsl:choose>
					</xsl:when>
					<xsl:otherwise>true</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:when test="@type = 'read_datetime'">
				<xsl:choose>
					<xsl:when test="name() = 'submit_date'">
						<xsl:choose>
							<xsl:when test="/applyeasy/item/submitted">true</xsl:when>
							<xsl:otherwise>false</xsl:otherwise>
						</xsl:choose>
					</xsl:when>
					<xsl:when test="name() = 'approve_date'">
						<xsl:choose>
							<xsl:when test="/applyeasy/item/approver">true</xsl:when>
							<xsl:otherwise>false</xsl:otherwise>
						</xsl:choose>
					</xsl:when>
					<xsl:otherwise>true</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:when test="@type='auto_enroll_target_learners'">
				true
			</xsl:when>			
			<xsl:when test="@value">
				<xsl:choose>
					<xsl:when test="@value != ''">true</xsl:when>
					<xsl:otherwise>false</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<xsl:choose>
					<xsl:when test="@value != ''">true</xsl:when>
					<xsl:when test="not(@value) and (name(../..) != 'field61')">true</xsl:when>
					<xsl:otherwise>false</xsl:otherwise>
				</xsl:choose>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="subfield" mode="get_subfield_show">
		<xsl:call-template name="get_show"/>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="draw_separate">
		<xsl:if test="(name(following-sibling::*) != 'link_list') and not(position() = last())">
		</xsl:if>
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>
