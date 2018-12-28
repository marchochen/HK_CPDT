<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
	<xsl:import href="../utils/wb_ui_nav_link.xsl"/>
	<xsl:template name="div_tree">
		<xsl:param name="title">Training Center</xsl:param>
		<xsl:param name="title_url">javascript:location.reload()</xsl:param>
		<xsl:param name="lab_cur_tcr"/>
		<xsl:param name="lab_root_training_center"/>
		<xsl:param name="nav_text"/>
		<xsl:param name="width"/>
		<xsl:param name="parent_id"/>
		<script LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
			//for hide or display div with tree
			var timer=null;
			function hide_tree_div() {
				div_menu.style.visibility="hidden";
				if(document.getElementById('folder_id') != null && document.getElementById('folder_id') !=''){
			 		document.getElementById('folder_id').style.visibility="visible";
				}
			}

			function show_tree_div(left_offset){
					div_menu.style.visibility="visible";
					var offset_l;
					if (left_offset == 0 || left_offset) {
						offset_l = document.getElementsByTagName('table')[0].offsetLeft + document.getElementById('table_tc_td').offsetLeft;
					} else {
						offset_l = 10;
					}
					div_menu.style.left=offset_l+'px';
					if(document.getElementById('folder_id') !=null && document.getElementById('folder_id') !=''){
						document.getElementById('folder_id').style.visibility="hidden";
					}
			}
		]]></script>
		<table border="0" id="table_tc" cellpadding="0" cellspacing="0">
			<xsl:attribute name="width">
				<xsl:choose>
					<xsl:when test="$width = ''"><xsl:value-of select="$wb_gen_table_width"/></xsl:when>
					<xsl:when test="$width = 'auto'"></xsl:when>
					<xsl:otherwise><xsl:value-of select="$width"/></xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			<tr>
				<td align="left">
					<xsl:variable name="table_id">
						<xsl:choose>
							<xsl:when test="$parent_id = ''">table_tc</xsl:when>
							<xsl:otherwise><xsl:value-of select="$parent_id"/></xsl:otherwise>
						</xsl:choose>
					</xsl:variable>
					<table align="left" onmouseover="show_tree_div(document.getElementById('{$table_id}').offsetLeft);clearTimeout(timer)" onmouseout="timer=setTimeout('hide_tree_div()',500)">
						<tr>
							<xsl:choose>
								<xsl:when test="$nav_text != ''">
									<td id="table_tc_td" nowrap="nowrap">
										<span class="wzb-listbox3">
													<xsl:copy-of select="$nav_text"/>
										</span>&#160;
										<img  onclick="hide_tree_div()" style="margin-left:-9px;margin-top:-3px;" height="30" border="0"  src="/static/admin/images/wzb-select.png"/>
									</td>
								</xsl:when>
								<xsl:otherwise>
									<td align="left" width="5%" valign="bottom" nowrap="nowrap" class="padding-right10">
										<xsl:value-of select="$lab_cur_tcr"/>：
									</td>
									<td id="table_tc_td" width="95%" class="wzb-listbox1">
										
										<a class="font vbtm div-select" href="{$title_url}" style="margin-left:10px;" >
											<xsl:value-of select="$title"/>
											
										</a>
										<img onclick="hide_tree_div()" src="/static/admin/images/wzb-select.png" border="0" class="vbtm" height="28" align="right"/>
									</td>
								</xsl:otherwise>
							</xsl:choose>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<div id="div_menu" onmouseover="clearTimeout(timer);" onmouseout="timer=setTimeout('hide_tree_div()',500)" style="position:absolute;visibility:hidden;width:300px;border:0px black solid;background-color:menu;padding:0px">
			<table>
				<tr>
					<td>
						<xsl:call-template name="tree_js">
							<xsl:with-param name="lab_root_training_center" select="$lab_root_training_center"/>
							<xsl:with-param name="show_button">false</xsl:with-param>
						</xsl:call-template>
					</td>
				</tr>
			</table>
		</div>
	</xsl:template>
</xsl:stylesheet>
