<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template name="sys_gen_tab">
		<xsl:param name="tab_1"/>
		<xsl:param name="tab_1_href"/>
		<xsl:param name="tab_2"/>
		<xsl:param name="tab_2_href"/>
		<xsl:param name="tab_3"/>
		<xsl:param name="tab_3_href"/>
		<xsl:param name="tab_4"/>
		<xsl:param name="tab_4_href"/>
		<xsl:param name="tab_5"/>
		<xsl:param name="tab_5_href"/>
		<xsl:param name="current_tab"/>
		<table cellpadding="0" cellspacing="0" border="0" width="{$wb_gen_table_width}">
			<tr>
				<td>
					<ul id="foldertab" class="nav nav-tabs" role="tablist">
						<xsl:choose>
							<xsl:when test="$tab_1=$current_tab">
								<li class="active" role="presentation">
									<a class="current" href="#">
										<xsl:value-of select="$tab_1"/>
									</a>
								</li>
							</xsl:when>
							<xsl:when test="$tab_1 != ''">
								<li role="presentation">
									<a href="{$tab_1_href}">
										<xsl:value-of select="$tab_1"/>
									</a>
								</li>
							</xsl:when>
						</xsl:choose>
						<xsl:choose>
							<xsl:when test="$tab_2 = $current_tab">
								<li class="active" role="presentation">
									<a class="current" href="#">
										<xsl:value-of select="$tab_2"/>
									</a>
								</li>
							</xsl:when>
							<xsl:when test="$tab_2 != ''">
								<li role="presentation">
									<a href="{$tab_2_href}">
										<xsl:value-of select="$tab_2"/>
									</a>
								</li>
							</xsl:when>
						</xsl:choose>
						<xsl:choose>
							<xsl:when test="$tab_3 = $current_tab">
								<li class="active" role="presentation">
									<a class="current" href="#">
										<xsl:value-of select="$tab_3"/>
									</a>
								</li>
							</xsl:when>
							<xsl:when test="$tab_3 != ''">
								<li role="presentation">
									<a href="{$tab_3_href}">
										<xsl:value-of select="$tab_3"/>
									</a>
								</li>
							</xsl:when>
						</xsl:choose>
						<xsl:choose>
							<xsl:when test="$tab_4 = $current_tab">
								<li class="active" role="presentation">
									<a class="current" href="#">
										<xsl:value-of select="$tab_4"/>
									</a>
								</li>
							</xsl:when>
							<xsl:when test="$tab_4 != ''">
								<li role="presentation">
									<a href="{$tab_4_href}">
										<xsl:value-of select="$tab_4"/>
									</a>
								</li>
							</xsl:when>
						</xsl:choose>
						<xsl:choose>
							<xsl:when test="$tab_5 = $current_tab">
								<li class="active"  role="presentation">
									<a class="current" href="#">
										<xsl:value-of select="$tab_5"/>
									</a>
								</li>
							</xsl:when>
							<xsl:when test="$tab_5 != ''">
								<li  role="presentation">
									<a href="{$tab_5_href}">
										<xsl:value-of select="$tab_5"/>
									</a>
								</li>
							</xsl:when>
						</xsl:choose>
					</ul>
				</td>
			</tr>
		</table>
	</xsl:template>
</xsl:stylesheet>
