<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/><xsl:import href="utils/wb_utils.xsl"/>
	<xsl:output indent="yes" />
	<!--================================================= -->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates/>
		</html>
	</xsl:template>
	<!--================================================= -->
	<xsl:template match="user">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={@encoding}"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript" type="text/javascript"><![CDATA[
				
				URL_FRM_ACTION = wb_utils_servlet_url
				FLD_CMD = "save_cos_struct";
				FLD_ENV = "wizb";
				FLD_URL_SUCCESS = wb_utils_invoke_servlet('cmd','get_prof','stylesheet','saveCosStructSuccess.xsl');
				FLD_URL_FAILURE = wb_utils_invoke_servlet('cmd','get_prof','stylesheet','saveCosStructFailure.xsl');

//function saveCourseStructXML(in_course_id, in_course_timestamp, in_course_struct_xml_cnt, in_course_struct_xml_1) {
//	frm = document.frmSaveToc;
//
//	frm.course_id.value=in_course_id;
//	frm.course_timestamp.value=in_course_timestamp;
//	frm.course_struct_xml_cnt.value=in_course_struct_xml_cnt;
//	frm.course_struct_xml_1.value=in_course_struct_xml_1;
//	submitFrm();
//}

function saveCourseStructXML() {
	in_course_id = parent.cos_id;
	in_course_timestamp = parent.course_timestamp;
	in_course_struct_xml_cnt = parent.course_struct_xml_cnt;
	in_course_struct_xml_1 = parent.course_struct_xml_1;

	frm = document.frmSaveToc;

	frm.course_id.value=in_course_id;
	frm.course_timestamp.value=in_course_timestamp;
	frm.course_struct_xml_cnt.value=in_course_struct_xml_cnt;
	frm.course_struct_xml_1.value=in_course_struct_xml_1;
	submitFrm();
}

function submitFrm() {
	frm = document.frmSaveToc;
	
	// populate hidden form data
	frm.env.value=FLD_ENV;
	frm.cmd.value=FLD_CMD;
	frm.url_failure.value=FLD_URL_FAILURE;
	frm.url_success.value=FLD_URL_SUCCESS;
	
	// submit
	frm.action = URL_FRM_ACTION;
	frm.submit();
}]]></script>
		</head>
		<body marginwidth="0" marginheight="0" topmargin="0" leftmargin="0" onload="saveCourseStructXML()">
			<form name="frmSaveToc" method="post" onSubmit="submitFrm()">
				<input type="hidden" name="env"/>
				<input type="hidden" name="cmd"/>
				<input type="hidden" name="course_id"/>
				<input type="hidden" name="course_timestamp"/>
				<input type="hidden" name="course_struct_xml_cnt"/>
				<input type="hidden" name="course_struct_xml_1"/>
				<input type="hidden" name="url_success"/>
				<input type="hidden" name="url_failure"/>
			</form>
		</body>
	</xsl:template>
	<!--================================================= -->
</xsl:stylesheet>
