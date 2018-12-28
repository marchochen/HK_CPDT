<?xml version='1.0' encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="../utils/display_filetype_icon.xsl"/>
	<xsl:import href="../utils/unescape_html_linefeed.xsl"/>


<xsl:template name="resource_body">
	<xsl:param name = "width"/>
	<xsl:choose>
		<xsl:when test="$wb_lang='ch'">
			<xsl:apply-templates select="body" mode="res">
				<xsl:with-param name="width"><xsl:value-of select="$width"/></xsl:with-param>
				<xsl:with-param name="res_annotation">註釋</xsl:with-param>
				<xsl:with-param name="res_no_annotation">沒有註釋</xsl:with-param>
				<xsl:with-param name="res_desc">簡介</xsl:with-param>
				<xsl:with-param name="res_no_desc">沒有簡介</xsl:with-param>
			</xsl:apply-templates>		
		</xsl:when>			
		<xsl:when test="$wb_lang='gb'">
			<xsl:apply-templates select="body" mode="res">
				<xsl:with-param name="width"><xsl:value-of select="$width"/></xsl:with-param>
				<xsl:with-param name="res_annotation">注释</xsl:with-param>
				<xsl:with-param name="res_no_annotation">没有注释</xsl:with-param>
				<xsl:with-param name="res_desc">简介</xsl:with-param>
				<xsl:with-param name="res_no_desc">没有简介</xsl:with-param>
			</xsl:apply-templates>		
		</xsl:when>					
		<xsl:otherwise>
			<xsl:apply-templates select="body" mode="res">
				<xsl:with-param name="width"><xsl:value-of select="$width"/></xsl:with-param>
				<xsl:with-param name="res_annotation">Annotation</xsl:with-param>
				<xsl:with-param name="res_no_annotation">No annotation</xsl:with-param>
				<xsl:with-param name="res_desc">Description</xsl:with-param>
				<xsl:with-param name="res_no_desc">No description</xsl:with-param>				
			</xsl:apply-templates>
		</xsl:otherwise>			
	</xsl:choose>

</xsl:template>	
<!-- =============================================================== -->
<xsl:template match="body" mode="res">
	<xsl:param name="width"/>
	<xsl:param name="res_annotation"/>
	<xsl:param name="res_no_annotation"/>
	<xsl:param name="res_desc"/>
	<xsl:param name="res_no_desc"/>
	<!-- ==========Resourece Icon Desc================= -->	
	<table border="0" cellpadding="5" cellspacing="0" width="{$width}">	
		<tr>
			<td>
				<img src="{$wb_img_path}tp.gif" width="1" height="10" border="0"/>
			</td>
		</tr>		
		<tr>
		  <td width="98%" align="centre">	  	  
		  <xsl:variable name="fileName"><xsl:value-of select="source"/></xsl:variable>
		  <xsl:variable name="URL"><xsl:value-of select="source"/></xsl:variable>
		  <xsl:variable name="fullPath">../<xsl:value-of select="concat('resource/', /resource/@id, '/')" /></xsl:variable>
		  <xsl:choose>	   
		    <xsl:when test="source/@type = 'FILE' or source/@type = 'ZIPFILE'">
			   <xsl:text disable-output-escaping="yes">&lt;a href="</xsl:text><xsl:value-of select="$fullPath"/><xsl:value-of select="$fileName"/><xsl:text disable-output-escaping="yes">" target="_blank"&gt;</xsl:text>
					<xsl:call-template name="display_filetype_icon">	
						<xsl:with-param name="fileName"><xsl:value-of select="$fileName"/></xsl:with-param>
					</xsl:call-template>
				<xsl:text disable-output-escaping="yes">&lt;/a&gt;</xsl:text>	
		    </xsl:when>	
			<xsl:when test="source/@type = 'WIZPACK'">
				<a href="javascript:res.read_wizpack({/resource/@id})">
		   		<img src="{$wb_img_path}icon_web_browser.gif" border="0" hspace="4" vspace="4"/></a>
			</xsl:when>
			<xsl:when test="source/@type = 'AICC_FILES'">
				<a href="{$URL}" target="_blank">
		   			<img src="{$wb_img_path}icol_ssc.gif" border="0" hspace="4" vspace="4" />
				</a>
			</xsl:when>
			<xsl:when test="source/@type = 'SCORM_FILES'">
				<xsl:call-template name="show_scorm_files"/>
			</xsl:when>			
			<xsl:when test="source/@type = 'NETGCOK_FILES'">
	  			<a href="javascript:module_lst.preview_netg('', '{/resource/@id}','{$URL}')">
		   		<img src="{$wb_img_path}icon_web_browser.gif" border="0" hspace="4" vspace="4" /></a>
			</xsl:when>			
		    <xsl:otherwise>
	  			<a href="{$URL}" target="_blank">
		   		<img src="{$wb_img_path}icon_web_browser.gif" border="0" hspace="4" vspace="4" /></a>
		    </xsl:otherwise>		  
		  </xsl:choose>
		  </td>
		</tr >
		<xsl:if test="source/@type != 'FILE'">
		 	<tr>
		  	<td width="98%">
				<span class="Text"><xsl:value-of select="source"/></span>  
		  	</td>
		</tr>
		</xsl:if>
		<tr>
			<td>
				<img src="{$wb_img_path}tp.gif" width="1" height="10" border="0"/>
			</td>
		</tr>	
	</table>
	<xsl:if test="annotation != ''">
		<xsl:if test="source/@type != 'AICC_FILES' and source/@type != 'SCORM_FILES' ">
			<!-- ==========Resourece Annotation================= -->
			<!-- ParInf Header Bar-->
			<xsl:call-template name="wb_ui_head">
				<xsl:with-param name="text" select="$res_annotation"/>
				<xsl:with-param name="width" select="$width"/>
			</xsl:call-template>
			<xsl:call-template name="wb_ui_line">
				<xsl:with-param name="width" select="$width"/>
			</xsl:call-template>
			<!-- ParInf Header Bar-->
			<!-- Parinf Content-->
			<table width="{$width}" cellspacing="0" cellpadding="3" border="0" class="Bg">
				<tr>
					<td>
						<img src="{$wb_img_path}tp.gif" width="1" height="10" border="0"/>
					</td>
				</tr>
				<tr>
					<td>
						<span class="Text">				
						<xsl:choose>
			       		<xsl:when test="annotation/html">
								<xsl:value-of disable-output-escaping="yes" select="annotation/html"/>
			       		</xsl:when>
							<xsl:when test="annotation = ''">
								<xsl:value-of select="$res_no_annotation"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:call-template name="unescape_html_linefeed">
									<xsl:with-param name="my_right_value">
										<xsl:value-of select="annotation/."/>
									</xsl:with-param>
								</xsl:call-template>
							</xsl:otherwise>
					  </xsl:choose>
						</span>
					</td>
				</tr>   
				<tr>
					<td>
						<img src="{$wb_img_path}tp.gif" width="1" height="10" border="0"/>
					</td>
				</tr>				
			</table>
			<xsl:call-template name="wb_ui_line">
				<xsl:with-param name="width" select="$width"/>
			</xsl:call-template>
					<xsl:call-template name="wb_ui_space">
						<xsl:with-param name="width" select="$width"/>
					</xsl:call-template>
		</xsl:if>
	</xsl:if>	
	<!-- Parinf Content-->
	<!-- ==========Resourece Desc================= -->
	<xsl:if test="desc != ''">
	<!-- ParInf Header Bar-->
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text"><xsl:value-of select="$res_desc"/></xsl:with-param>
			<xsl:with-param name="width" select="$width"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_line">
			<xsl:with-param name="width" select="$width"/>
		</xsl:call-template>
	
	<!-- Parinf Content-->
		<table width="{$width}" cellspacing="0" cellpadding="3" border="0" class="Bg">
			<tr>
				<td>
					<img src="{$wb_img_path}tp.gif" width="1" height="10" border="0"/>
				</td>
			</tr>
			<tr>
				<td>
					<span class="Text">	
					<xsl:if test="desc = ''"><xsl:value-of select="$res_no_desc"/></xsl:if>
					<xsl:call-template name="unescape_html_linefeed">
							<xsl:with-param name="my_right_value"><xsl:value-of select="desc"/></xsl:with-param>
					</xsl:call-template>
					</span>
				</td>		
			</tr>
			<tr>
				<td>
					<img src="{$wb_img_path}tp.gif" width="1" height="10" border="0"/>
				</td>
			</tr>
		</table>
			<xsl:call-template name="wb_ui_line">
				<xsl:with-param name="width" select="$width"/>
			</xsl:call-template>
	<!-- Parinf Content-->
	</xsl:if>
	
	<!-- Footer 1px Line-->
	<table width="{$width}" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td>
				<IMG border="0" height="1" width="1" src="{$wb_img_path}tp.gif"/>
			</td>
		</tr>
	</table>
	<!-- Footer 1px Line-->	
</xsl:template>
<!-- =============================================================== -->
<xsl:template name="show_scorm_files">
	<xsl:variable name="version" select="scorm_info/@version"/>
	<xsl:for-each select="scorm_info/tableofcontents/item">
		<xsl:call-template name="show_item">
			<xsl:with-param name="version" select="$version"/>
		</xsl:call-template>
	</xsl:for-each >
</xsl:template>
<!-- =============================================================== -->
<xsl:template name="show_item" match="item">
	<xsl:param name="version"/>
	<xsl:param name="space"/>
	<xsl:choose>
		<xsl:when test="itemtype = 'FDR'"><xsl:value-of select="$space"/><xsl:value-of select="@title"/><br/>
		</xsl:when>
		<xsl:otherwise>&#160;<xsl:value-of select="$space"/><a href="javascript:module_lst.preview_scorm('{src_link}','0',0,'0','','','','{$version}')" class="Text"><xsl:value-of select="@title"/></a><br/>
		</xsl:otherwise>
	</xsl:choose>
	<xsl:apply-templates select="item">
		<xsl:with-param name="space">&#160;<xsl:value-of select="$space"/></xsl:with-param>
		<xsl:with-param name="version" select="$version"/>
	</xsl:apply-templates>
</xsl:template>
</xsl:stylesheet>
