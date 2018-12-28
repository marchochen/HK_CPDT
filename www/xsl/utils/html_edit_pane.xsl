<?xml version='1.0' encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="escape_all.xsl"/>

<!-- =============================================================== -->
	<xsl:template name="rte_css">
		<!-- Start RTE CSS -->
		<style>
/*body {margin:0pt;border:none;padding:0pt}*/
		#tbDBSelect {display:none;text-align:left;width: 100;margin-right: 1pt;margin-bottom: 0pt;margin-top: 0pt;padding: 0pt}
		#DBSelect, #idMode, .userButton {font:8pt arial}
		#DBSelect {width:100}
		#idMode {margin-top:0pt}
		.tbButton {text-align:left;margin:0pt 1pt 0pt 0pt;padding:0pt}
		#EditBox {position: relative}</style>
		<style id="skin" disabled="diabled">#EditBox {margin: 0px 0px 0px 0px}
		#tbUpRight, #tbUpLeft {width:0px}	
		#idMode {margin-left:11px;padding:0pt}
		#idMode LABEL {color: navy;text-decoration: underline}
		#tbTopBar {height:0px}
		/* #tbButtons, #tbContents {background: #DEE7F7;vertical-align: top} */
		#tbContents {padding:0px 5px}
		#tbBottomBar {height:px}</style>
		<style id="defPopupSkin">#popup BODY {margin:0px;border-top:none}
		#popup .colorTable {height:91px}
		#popup #header {width:100%}
		#popup #close {cursor:default;font:bold 8pt system;width:16px;text-align: center}
		#popup #content {padding:10pt}
		#popup TABLE {vertical-align:top}
		#popup .tabBody {border:1px black solid;border-top: none}
		#popup .tabItem, #popup .tabSpace {border-bottom:1px black solid;border-left:1px black solid}
		#popup .tabItem {border-top:1px black solid;font:10pt arial,geneva,sans-serif;}
		#popup .currentColor {width:20px;height:20px; margin: 0pt;margin-right:15pt;border:1px black solid}
		#popup .tabItem DIV {margin:3px;padding:0px;cursor: hand}
		#popup .tabItem DIV.disabled {color: gray;cursor: default}
		#popup .selected {font-weight:bold}
		#popup .emoticon {cursor:hand}</style>
		<style id="popupSkin">#popup BODY {border: 3px #006699 solid; background: #F1F1F1}
		#popup #header {background: #006699; color: white}
		#popup #caption {text-align: left;font: bold 12pt arial , geneva, sans-serif}
		#popup .ColorTable, #popup #idList TD#current {border: 1px black solid}
		#popup #idList TD{cursor: hand;border: 1px #F1F1F1 solid}
		#popup #close {border: 1px #99CCFF solid;cursor:hand;color: #99CCFF;font-weight: bold;margin-right: 6px;padding:0px 4px 2px}
		#popup #tableProps .tablePropsTitle {color:#006699;text-align:left;margin:0pt;border-bottom: 1px black solid;margin-bottom:5pt}
		#tableButtons, #tableProps {padding:5px}
		#popup #tableContents {height:175px}
		#popup #tableProps .tablePropsTitle, #popup #tableProps, #popup #tableProps TABLE {font:bold 9pt Arial, Geneva, Sans-serif}
		#popup #tableOptions  {font:9pt Arial, Geneva, Sans-serif;padding:15pt 5pt}
		#popup #puDivider {background:black;width:1px}
		#popup #content {margin: 0pt;padding:5pt 5pt 10pt 5pt}
		#popup #ColorPopup {width: 250px}
		#popup .ColorTable TR {height:6px}
		#popup .ColorTable TD {width:6px;cursor:hand}
		#popup .block P,#popup .block H1,#popup .block H2,#popup .block H3,
		#popup .block H4, #popup .block H5,#popup .block H6,#popup .block PRE {margin:0pt;padding:0pt}
		#popup #customFont {font:12pt Arial;text-decoration:italic}</style>
		<!-- End RTE CSS -->
	</xsl:template>
<!-- richtext editor -->
<xsl:template name="html_edit_pane">
<xsl:param name="supportHTML">true</xsl:param>
<xsl:param name="body"/>
<xsl:param name="frm"/>
<xsl:param name="fld_name"/>
<script language="Javascript"><![CDATA[
browser = navigator.appName;
if(browser.indexOf("Microsoft")>-1 && ]]><xsl:value-of select="$supportHTML"/><![CDATA[){
	str = '<input type="hidden" value="" name="]]><xsl:value-of select="$fld_name"/><![CDATA["/>';
	str += '<table width="500" border="0" cellspacing="0" cellpadding="0">';
	str += '<tr class="wbParInfTitleBg">';
	str += '<td>';
	str += '<div id="idEditor" style="VISIBILITY:hidden">';
	str += '<table id="idToolbar" width="500" cellspacing="0" cellpadding="0" onClick="_CPopup_Hide()" border="0">';
	str += '<tr id="tbTopBar">';
	str += '<td id="tbUpLeft"></td>';
	str += '<td colspan="2" id="tbUpMiddle"></td>';
	str += '<td id="tbUpRight"></td>';
	str += '</tr>';
	str += '<tr>';
	str += '<td id="tbMidLeft"></td>';
	str += '<td id="tbContents" class="wbAnnounceRTEcontent">';
	document.write(str);
	L_TOOLBARGIF_TEXT = "]]><xsl:value-of select="$wb_img_path"/><![CDATA[rte_tbEN.gif";
	L_EMOTICONPATH_TEXT = "]]><xsl:value-of select="$wb_img_path"/><![CDATA[";
	_drawToolbar();
	str = '<td id="tbButtons" align="right" class="wbAnnounceRTEcontent"></td>';
	str += '<td id="tbMidRight"></td>';
	str += '</tr>';
	str += '<tr id="tbbottomBar">';
	str += '<td id="tbLowLeft"></td>';
	str += '<td colspan="2" id="tbLowMiddle"></td>';
	str += '<td id="tbLowRight"></td>';
	str += '</tr>';
	str += '</table>';
	str += '<iframe name="idPopup" style="HEIGHT: 100px; LEFT: 25px; MARGIN-TOP: 8px; POSITION: absolute; VISIBILITY: hidden; Z-INDEX: -1"></iframe>';
	str += '<iframe id="EditBox" style="LEFT: 0px;border-top:solid 1px #000000; MARGIN-TOP: 0px;" name="idEditbox" width="500" height="100" onFocus="_CPopup_Hide()"></iframe>';
	str += '<div id="tbmode" >';
	document.write(str);
	_drawModeSelect(']]><xsl:value-of select="$wb_lang"/><![CDATA[');
	str = '</div>';
	str += '</div>';
	str += '</td>';
	str += '</tr>';
	str += '</table>';
	document.write(str);
	_initEditor();
	idEditbox.document.body.innerHTML = ']]><xsl:value-of select="$body"/><![CDATA[';
}else{
	str = '<textarea rows="6" cols="40" name="msg_body">';
	str += '</textarea>';
	document.write(str);
	]]><xsl:value-of select="$frm"/><![CDATA[.]]><xsl:value-of select="$fld_name"/><![CDATA[.value = ']]><xsl:value-of select="$body"/><![CDATA[';
}
]]></script>
</xsl:template>
<!-- =============================================================== -->
</xsl:stylesheet> 
