<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:variable name="img_path">
		<xsl:value-of select="$wb_img_path"/>
	</xsl:variable>
	<xsl:template match="homepage_function">
		<xsl:variable name="hom_ftn_id">
			<xsl:value-of select="@id"/>
		</xsl:variable>
		<xsl:apply-templates select="." mode="click_this"/>
		<xsl:if test="(position()-1) mod 3 = 0">
			<xsl:text disable-output-escaping="yes">&lt;tr &gt;</xsl:text>
		</xsl:if>
		<td width="33%" valign="top">
			<table>
				<tr>
					<td>
						<label for="{@id}">
							<span>
								<xsl:attribute name="class">
									<xsl:choose>
										<xsl:when test="@tc_related = 'true'">TitleTextBold</xsl:when>
										<xsl:otherwise>TitleText</xsl:otherwise>
									</xsl:choose>
								</xsl:attribute>
								<input type="checkbox" value="{@id}" name="{@id}" id="{@id}" onclick="check_this_{@id}(frmAction)">
									<xsl:for-each select="/role_function/role/function_list/function">
										<xsl:if test="@id = $hom_ftn_id">
											<xsl:attribute name="checked">checked</xsl:attribute>
										</xsl:if>
									</xsl:for-each>
								</input>
								<input type="hidden" value="{@tc_related}" id="{@id}_tc_related"/>
								<xsl:for-each select="desc">
									<xsl:if test="@lan=$wb_lang_encoding">
										<xsl:value-of select="@name"/>
									</xsl:if>
								</xsl:for-each>
							</span>
						</label>
					</td>
				</tr>
				<xsl:apply-templates select="child_functions/function" mode="html">
					<xsl:with-param name="count_child" select="0"/>
				</xsl:apply-templates>
			</table>
		</td>
		<xsl:if test="position() mod 3 = 0">
			<xsl:text disable-output-escaping="yes">&lt;/tr &gt;
				&lt;tr &gt;
					&lt;td height="20" colspan="3"&gt;   &lt;/td &gt;
				&lt;/tr &gt;
		</xsl:text>
		</xsl:if>
	</xsl:template>
	<!-- ============================================================================  -->
	<xsl:template match="other_functions">
		<xsl:for-each select="function">
			<xsl:variable name="hom_ftn_id">
				<xsl:value-of select="@id"/>
			</xsl:variable>
			<xsl:if test="(position()-1) mod 3 = 0">
				<xsl:text disable-output-escaping="yes">&lt;tr &gt;</xsl:text>
			</xsl:if>
			<td width="33%" valign="top">
				<table border="0" width="100%" cellspacing="0" cellpadding="3">
					<tr>
						<td>
							<label for="{@id}">
								<span>
									<xsl:attribute name="class">
										<xsl:choose>
											<xsl:when test="@tc_related = 'true'">TitleTextBold</xsl:when>
											<xsl:otherwise>TitleText</xsl:otherwise>
										</xsl:choose>
									</xsl:attribute>
									<input type="checkbox" value="{@id}" name="{@id}" id="{@id}">
										<xsl:for-each select="/role_function/role/function_list/function">
											<xsl:if test="@id = $hom_ftn_id">
												<xsl:attribute name="checked">checked</xsl:attribute>
											</xsl:if>
										</xsl:for-each>
									</input>
									<input type="hidden" value="{@tc_related}" id="{@id}_tc_related"/>
									<xsl:for-each select="desc">
										<xsl:if test="@lan=$wb_lang_encoding">
											<xsl:value-of select="@name"/>
										</xsl:if>
									</xsl:for-each>
									<img border="0" height="1" width="8" src="{$img_path}tp.gif"/>
								</span>
							</label>
						</td>
					</tr>
					<xsl:apply-templates select="child_functions/function" mode="html">
						<xsl:with-param name="count_child" select="1"/>
					</xsl:apply-templates>
				</table>
			</td>
			<xsl:if test="position() mod 3 = 0">
				<xsl:text disable-output-escaping="yes">
				&lt;/tr &gt;
				&lt;tr &gt;
					&lt;td height="20" colspan="3"&gt; &lt;img border="0" height="1" width="8" src="{$img_path}tp.gif"/&gt; &lt;/td &gt;
				&lt;/tr &gt;
		</xsl:text>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>
	<!-- ============================================================================  -->
	<xsl:template match="function" mode="html">
		<xsl:param name="count_child"/>
		<xsl:variable name="ftn_id">
			<xsl:value-of select="@id"/>
		</xsl:variable>
		<xsl:apply-templates select="." mode="click_this"/>
		<tr>
			<td>
				<xsl:call-template name="DrawImg">
					<xsl:with-param name="count_child" select="$count_child"/>
				</xsl:call-template>
				<xsl:choose>
					<xsl:when test="position() = last()">
						<img border="0" src="{$img_path}tree/images/default/L.png"/>
					</xsl:when>
					<xsl:otherwise>
						<img border="0" src="{$img_path}tree/images/default/T.png"/>
					</xsl:otherwise>
				</xsl:choose>
				<label for="{@id}">
					<span>
						<xsl:attribute name="class">
							<xsl:choose>
								<xsl:when test="@tc_related = 'true'">TitleTextBold</xsl:when>
								<xsl:otherwise>TitleText</xsl:otherwise>
							</xsl:choose>
						</xsl:attribute>
						<input type="checkbox" value="{@id}" name="{@id}" id="{@id}" onclick="check_this_{@id}(frmAction)">
							<xsl:for-each select="//role_function/role/function_list/function">
								<xsl:if test="@id = $ftn_id">
									<xsl:attribute name="checked">checked</xsl:attribute>
								</xsl:if>
							</xsl:for-each>
						</input>
						<input type="hidden" value="{@tc_related}" id="{@id}_tc_related"/>
						<xsl:for-each select="desc">
							<xsl:if test="@lan=$wb_lang_encoding">
								<xsl:value-of select="@name"/>
							</xsl:if>
						</xsl:for-each>
					</span>
				</label>
			</td>
		</tr>
		<xsl:apply-templates select="child_functions/function" mode="html">
			<xsl:with-param name="count_child" select="$count_child + 2"/>
		</xsl:apply-templates>
	</xsl:template>
	<!-- ============================================================================  -->
	<xsl:template name="DrawImg">
		<xsl:param name="count_child"/>
		<xsl:if test="$count_child &gt; 0">
			<xsl:choose>
				<xsl:when test="$count_child mod 2 = 0">
					<img border="0" src="{$img_path}tree/images/default/I.png"/>
				</xsl:when>
				<xsl:otherwise>
					<img border="0" height="1" width="2" src="{$img_path}tp.gif"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:call-template name="DrawImg">
				<xsl:with-param name="count_child" select="$count_child - 1"/>
			</xsl:call-template>
		</xsl:if>
	</xsl:template>
	<!-- ============================================================================  -->
	<xsl:template match="*" mode="click_this">
		<script type="text/javascript" language="javascript"><![CDATA[ 
			function check_this_]]><xsl:value-of select="@id"/><![CDATA[(frm) {
				var clear = true;	     
				]]><xsl:apply-templates select="child_functions/function" mode="sel_child_js"/><![CDATA[
				]]><xsl:apply-templates select="related_functions/function" mode="related_js"/>
				<xsl:if test="not(@isHomepageFunction)"><![CDATA[
					if(frm.]]><xsl:value-of select="@id"/><![CDATA[.checked == true){
						]]><xsl:apply-templates select="." mode="sel_parent_js"/><![CDATA[ 
					}else{
						]]><xsl:apply-templates select="." mode="clear_parent_js"/><![CDATA[
					}]]>
				</xsl:if><![CDATA[				
			}]]>
        </script>
	</xsl:template>
	<!-- ============================================================================  -->
	<xsl:template match="*" mode="sel_child_js"><![CDATA[
		frm.]]><xsl:value-of select="@id"/><![CDATA[.checked = 
				  (frm.]]><xsl:value-of select="../../@id"/><![CDATA[.checked == true)?true:false                 
		]]><xsl:apply-templates select="related_functions/function" mode="related_js"/><![CDATA[          
		]]><xsl:apply-templates select="child_functions/function" mode="sel_child_js"/>
	</xsl:template>
	<!-- ============================================================================  -->
	<xsl:template match="*" mode="sel_parent_js"><![CDATA[
		frm.]]><xsl:value-of select="../../@id"/><![CDATA[.checked = true
		]]><xsl:apply-templates select="../../related_functions/function" mode="related_js"/>
		<xsl:if test="not(../../@isHomepageFunction)">
			<xsl:apply-templates select="../../." mode="sel_parent_js"/>
		</xsl:if>
	</xsl:template>
	<!-- ============================================================================  -->
	<xsl:template match="*" mode="clear_parent_js">
		<xsl:for-each select="../function"><![CDATA[
			if( frm.]]><xsl:value-of select="@id"/><![CDATA[.checked == true){
				clear = false;            
			}]]>
		</xsl:for-each><![CDATA[
		if(clear){
			frm.]]><xsl:value-of select="../../@id"/><![CDATA[.checked = false; 
		}]]>
		<xsl:if test="not(../../@isHomepageFunction)"><![CDATA[
			clear = true;
			]]><xsl:apply-templates select="../../." mode="clear_parent_js"/>
		</xsl:if>
	</xsl:template>
	<!-- ============================================================================  -->
	<xsl:template match="*" mode="related_js"><![CDATA[
		if(frm.]]><xsl:value-of select="../../@id"/><![CDATA[.checked == true){
			frm.]]><xsl:value-of select="@id"/><![CDATA[.checked = true
		}else{
			frm.]]><xsl:value-of select="@id"/><![CDATA[.checked = false
		}]]>   
    </xsl:template>	
</xsl:stylesheet>
