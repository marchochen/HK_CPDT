<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<!-- Pagination -->
<xsl:template name="wb_pagination">
<xsl:param name="cur_page"/>
<xsl:param name="page_size"/>
<xsl:param name="timestamp"/>
<xsl:param name="total"/>
<xsl:param name="width"/>
<xsl:param name="tr_class">wbGenPaginationBarBg</xsl:param>
<xsl:param name="td_class">wbGenPaginationBarBg</xsl:param>
<xsl:param name="txt_class">wbGenPaginationBarText</xsl:param>
<xsl:param name="link_txt_class">wbGenPaginationBarLink</xsl:param>
<xsl:param name="cur_page_name">cur_page</xsl:param>
<xsl:param name="nav_nm"/><!-- navigation name -->
<xsl:variable name="page_timestamp"><xsl:value-of select="$timestamp"/></xsl:variable>
<TABLE cellpadding="3" cellspacing="0" border="0" width="{$width}" height="19">
<TR class="{$tr_class}">
	<TD width="8"><IMG src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/></TD>
	<!-- SHOWING -->
	<TD width="295">
		<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>			
		<xsl:choose>
			<xsl:when test="$total = 0 "><IMG src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/></xsl:when>
			<xsl:otherwise>
				<span class="{$txt_class}">
					<xsl:value-of select="$lab_showing"/><xsl:text>&#160;</xsl:text>
					<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
						cur_page = 	]]><xsl:value-of select="$cur_page"/><![CDATA[
						if ( cur_page == 1 )
							str = '1';
						else
							str = (cur_page - 1 ) * ]]><xsl:value-of select="$page_size"/><![CDATA[+1;
						str += ' - '; 
						if ( cur_page * ]]><xsl:value-of select="$page_size"/><![CDATA[ > ]]><xsl:value-of select="$total"/><![CDATA[ )
							str += ']]><xsl:value-of select="$total"/><![CDATA[';
						else
							str += cur_page * ]]><xsl:value-of select="$page_size"/><![CDATA[;
						document.write(str);
					]]></SCRIPT><xsl:text>&#160;</xsl:text><xsl:value-of select="$lab_page_of"/><xsl:text>&#160;</xsl:text>				
					<xsl:value-of select="$total"/><xsl:text>&#160;</xsl:text><xsl:value-of select="$lab_page_piece"/>
				</span>
			</xsl:otherwise>
		</xsl:choose>
	</TD>
	<!-- PAGES -->
	<TD align="right">
		<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
		<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[				
			cur_page = ]]><xsl:value-of  select="$cur_page"/><![CDATA[
			no_of_page = Math.ceil(]]><xsl:value-of select="$total"/><![CDATA[/]]><xsl:value-of select="$page_size"/><![CDATA[);
			index = Math.floor((cur_page-1)/5);
			str = '';
			// PREV 5
			if ( no_of_page >= 6 && cur_page > 5 )
			]]><xsl:choose>
				<xsl:when test="$nav_nm != ''">
					<xsl:choose>
						<xsl:when test="$page_timestamp != ''"><![CDATA[str += '<a href="Javascript:wb_utils_nav_get_urlparam(\'timestamp\',escape(escape(\']]><xsl:value-of select="$page_timestamp"/><![CDATA[\')),\']]><xsl:value-of select="$cur_page_name"/><![CDATA[\','+(index*5)+',\'nav_nm\',\']]><xsl:value-of select="$nav_nm"/><![CDATA[\')" class="]]><xsl:value-of select="$link_txt_class"/><![CDATA["> ]]><xsl:value-of select="$lab_prev"/><![CDATA[</a>';]]></xsl:when>
						<xsl:otherwise><![CDATA[str += '<a href="Javascript:wb_utils_nav_get_urlparam(\']]><xsl:value-of select="$cur_page_name"/><![CDATA[\','+(index*5)+',\'nav_nm\',\']]><xsl:value-of select="$nav_nm"/><![CDATA[\')" class="]]><xslvalue-of select="$link_txt_class"/><![CDATA["> ]]><xsl:value-of select="$lab_prev"/><![CDATA[</a>';]]></xsl:otherwise>
					</xsl:choose>						
				</xsl:when>
				<xsl:otherwise>
					<xsl:choose>
						<xsl:when test="$page_timestamp != ''"><![CDATA[str += '<a href="Javascript:wb_utils_nav_get_urlparam(\'timestamp\',escape(\']]><xsl:value-of select="$page_timestamp"/><![CDATA[\'),\']]><xsl:value-of select="$cur_page_name"/><![CDATA[\','+(index*5)+')" class="]]><xsl:value-of select="$link_txt_class"/><![CDATA["> ]]><xsl:value-of select="$lab_prev"/><![CDATA[</a>';]]></xsl:when>
						<xsl:otherwise><![CDATA[str += '<a href="Javascript:wb_utils_nav_get_urlparam(\']]><xsl:value-of select="$cur_page_name"/><![CDATA[\','+(index*5)+')" class="]]><xsl:value-of select="$link_txt_class"/><![CDATA["> ]]><xsl:value-of select="$lab_prev"/><![CDATA[</a>';]]></xsl:otherwise>
					</xsl:choose>
				</xsl:otherwise>
			</xsl:choose><![CDATA[	
			//			
			if (no_of_page != 1){
				if (no_of_page != 0) {str += '<span class="]]><xsl:value-of select="$txt_class"/><![CDATA["> | </span>';}
			for ( i=1; i <= 5; i++){
				if ( index*5 + i <= no_of_page){
					if ( cur_page == index*5 + i )
						str += '<span class="]]><xsl:value-of select="$txt_class"/><![CDATA[">' +( index*5 + i )+ ' | </span>';
					else{
					]]><xsl:choose>
						<xsl:when test="$nav_nm != ''">
							<xsl:choose>
								<xsl:when test="$page_timestamp != ''"><![CDATA[str += '<a href="Javascript:wb_utils_nav_get_urlparam(\'timestamp\',escape(\']]><xsl:value-of select="$page_timestamp"/><![CDATA[\'),\']]><xsl:value-of select="$cur_page_name"/><![CDATA[\','+(index*5+i)+',\'nav_nm\',\']]><xsl:value-of select="$nav_nm"/><![CDATA[\')" class="]]><xsl:value-of select="$link_txt_class"/><![CDATA[">' + (index*5+i) + '</a>';]]></xsl:when>
								<xsl:otherwise><![CDATA[str += '<a href="Javascript:wb_utils_nav_get_urlparam(\']]><xsl:value-of select="$cur_page_name"/><![CDATA[\','+(index*5+i)+',\'nav_nm\',\']]><xsl:value-of select="$nav_nm"/><![CDATA[\')" class="]]><xsl:value-of select="$link_txt_class"/><![CDATA[">' + (index*5+i) + '</a>';]]></xsl:otherwise>
							</xsl:choose>
						</xsl:when>
						<xsl:otherwise>
							<xsl:choose>
								<xsl:when test="$page_timestamp != ''"><![CDATA[str += '<a href="Javascript:wb_utils_nav_get_urlparam(\'timestamp\',escape(\']]><xsl:value-of select="$page_timestamp"/><![CDATA[\'),\']]><xsl:value-of select="$cur_page_name"/><![CDATA[\','+(index*5+i)+')" class="]]><xsl:value-of select="$link_txt_class"/><![CDATA[">' + (index*5+i) + '</a>';]]></xsl:when>
								<xsl:otherwise><![CDATA[str += '<a href="Javascript:wb_utils_nav_get_urlparam(\']]><xsl:value-of select="$cur_page_name"/><![CDATA[\','+(index*5+i)+')" class="]]><xsl:value-of select="$link_txt_class"/><![CDATA[">' + (index*5+i) + '</a>';]]></xsl:otherwise>
							</xsl:choose>
						</xsl:otherwise>
					</xsl:choose>	<![CDATA[								
						str +=  '<span class="]]><xsl:value-of select="$txt_class"/><![CDATA["> | </span>';
					}						
				}
			}
			}
			// NEXT5
			if ( no_of_page > (index + 1)*5 )
			]]><xsl:choose>
				<xsl:when test="$nav_nm != ''">
					<xsl:choose>
						<xsl:when test="$page_timestamp != ''"><![CDATA[str += '<a href="Javascript:wb_utils_nav_get_urlparam(\'timestamp\',escape(\']]><xsl:value-of select="$page_timestamp"/><![CDATA[\'),\']]><xsl:value-of select="$cur_page_name"/><![CDATA[\','+((index+1)*5+1)+',\'nav_nm\',\']]><xsl:value-of select="$nav_nm"/><![CDATA[\')" class="]]><xsl:value-of select="$link_txt_class"/><![CDATA["> ]]><xsl:value-of select="$lab_next"/><![CDATA[</a>';]]></xsl:when>
						<xsl:otherwise><![CDATA[str += '<a href="Javascript:wb_utils_nav_get_urlparam(\']]><xsl:value-of select="$cur_page_name"/><![CDATA[\','+((index+1)*5+1)+',\'nav_nm\',\']]><xsl:value-of select="$nav_nm"/><![CDATA[\')" class="]]><xsl:value-of select="$link_txt_class"/><![CDATA["> ]]><xsl:value-of select="$lab_next"/><![CDATA[</a>';]]></xsl:otherwise>
					</xsl:choose>			
				</xsl:when>
				<xsl:otherwise>
					<xsl:choose>
						<xsl:when test="$page_timestamp != ''"><![CDATA[str += '<a href="Javascript:wb_utils_nav_get_urlparam(\'timestamp\',escape(\']]><xsl:value-of select="$page_timestamp"/><![CDATA[\'),\']]><xsl:value-of select="$cur_page_name"/><![CDATA[\','+((index+1)*5+1)+')" class="]]><xsl:value-of select="$link_txt_class"/><![CDATA["> ]]><xsl:value-of select="$lab_next"/><![CDATA[</a>';]]></xsl:when>
						<xsl:otherwise><![CDATA[str += '<a href="Javascript:wb_utils_nav_get_urlparam(\']]><xsl:value-of select="$cur_page_name"/><![CDATA[\','+((index+1)*5+1)+')" class="]]><xsl:value-of select="$link_txt_class"/><![CDATA["> ]]><xsl:value-of select="$lab_next"/><![CDATA[</a>';]]></xsl:otherwise>
					</xsl:choose>			
				</xsl:otherwise>
			</xsl:choose>	<![CDATA[					
			else
				str += '';
			document.write(str);
		]]></SCRIPT>
	</TD>
	<TD width="8"><IMG src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/></TD>
</TR>
</TABLE>
</xsl:template>
</xsl:stylesheet>
