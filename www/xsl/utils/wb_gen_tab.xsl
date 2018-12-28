<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<!-- utils -->
<xsl:import href="escape_js.xsl"/>
<!-- ======================================================================================== -->
<xsl:template name="wb_gen_tab">	
	<xsl:param name="tab_name_lst"/>
	<xsl:param name="tab_link_lst"/>
	<xsl:param name="target_tab"/>
	<xsl:param name="tab_name">tab</xsl:param>
	<xsl:param name="delimiter">:_:_:</xsl:param>

	<table cellpadding="0" cellspacing="0" border="0"  width="{$wb_gen_table_width}">
		<tr>
			<td height="3"><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/></td>
		</tr>
		<tr><td class="SecLine" height="1"><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/></td></tr>
		
		<tr>
			<td>
				<table cellpadding="0" cellspacing="0" border="0">				
					<tr class="wbTabBarBg">
						<script language="javascript" type="text/javascript"><![CDATA[
							var wbTabNameLst = ']]><xsl:call-template name="escape_js"><xsl:with-param name="input_str"><xsl:value-of select="$tab_name_lst"/></xsl:with-param></xsl:call-template><![CDATA['
							var wbTabLinkLst = ']]><xsl:call-template name="escape_js"><xsl:with-param name="input_str"><xsl:value-of select="$tab_link_lst"/></xsl:with-param></xsl:call-template><![CDATA['
							var wbTabDelimiter = ']]><xsl:value-of select="$delimiter"/><![CDATA['
																						
							function wbTabObj(){
								this.nmLst;
								this.lnkLst;
								this.name;
								this.link;
							}
							
							
							function wbTabShow(){
								var obj = new Array()
								var cnt = 0
								var i = 0								
								var str = ''
								
								do{								
									obj[cnt] = new wbTabObj
									
									if (cnt >= 1) {
										obj[cnt].nmLst = (obj[cnt-1].nmLst).substring((obj[cnt-1].nmLst).indexOf(wbTabDelimiter)+5,obj[cnt-1].nmLst.length);
										obj[cnt].lnkLst = (obj[cnt-1].lnkLst).substring((obj[cnt-1].lnkLst).indexOf(wbTabDelimiter)+5,obj[cnt-1].lnkLst.length);
									}
									else {
										obj[cnt].nmLst = wbTabNameLst;
										obj[cnt].lnkLst = wbTabLinkLst;
									}							
									
									if (obj[cnt].nmLst.indexOf(wbTabDelimiter) != -1) {
										obj[cnt].name = (obj[cnt].nmLst).substring(0,obj[cnt].nmLst.indexOf(wbTabDelimiter));
										obj[cnt].link = (obj[cnt].lnkLst).substring(0,obj[cnt].lnkLst.indexOf(wbTabDelimiter));
										
									}
									else {
										obj[cnt].name = obj[cnt].nmLst;
										obj[cnt].link = obj[cnt].lnkLst;
									}
									
									cnt++;			
								}while(obj[cnt-1].nmLst.indexOf(wbTabDelimiter) != -1);
								str += '<td>';			
								str += '<ul role=\"tablist\" class=\"nav nav-tabs\" id=\"foldertab\" style="margin-top:-60px">';					
								for(i=0; i<cnt; i++){
									if (i == (]]><xsl:value-of select="number($target_tab)"/><![CDATA[-1))  {
									str +='<li role=\"presentation\" class=\"active\">'
										str += '<a href=\"' + obj[i].link + '\"'
										str += 'class=\"wbTabBarTargetLink\" id="]]><xsl:value-of select="$tab_name"/>_link_<![CDATA['
										str += i+'">';
									}
									else {
										str +='<li role=\"presentation\" >'
										str += '<a href=\"' + obj[i].link + '\"'
										str += 'class=\"wbTabBarLink\" id="]]><xsl:value-of select="$tab_name"/>_link_<![CDATA['
										str+= i+'">';	
									}
									str += obj[i].name + '</a>'
									str += '</li>'
								
								}
									str += '</ul>'
									str += '</td>'									
									str += '<td class="SecLine" width="1">'
									str += '<img border="0" height="1" src="]]><xsl:value-of select="$wb_img_path"/><![CDATA[tp.gif" width="1"/>'
									str += '</td>'
								if (str != '') {document.write(str);}
							}
							
							function  wbTabInit(){
								var str;
								if (wbTabNameLst != null && wbTabNameLst != '') {wbTabShow();}
								else{ 
									str = '<td><img src=\"]]><xsl:value-of select="$wb_img_path"/><![CDATA[tp.gif\" width=\"1\" height=\"1\" border=\"0\"/></td>'
									document.write(str)
								}						
							}
							
							wbTabInit();
						]]></script>			
					</tr>
				</table>
			</td>			
		</tr>
	</table>	


</xsl:template>
<!-- ======================================================================================== -->
</xsl:stylesheet>
