<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="../cust/wb_cust_const.xsl"/>
	<xsl:import href="../utils/wb_css.xsl"/>
	<xsl:import href="que_action_share.xsl"/>
	<xsl:variable name="id_obj" select="/question_form/objective/@id"/>
	<!-- =============================================================== -->
	<xsl:template name="que_mc">
		<xsl:param name="mode"/>
		<xsl:param name="save_function"/>
		<xsl:param name="cancel_function"/>
		<xsl:param name="width"/>
		<xsl:param name="header"/>
		<xsl:param name="upd_type"/>
		<xsl:param name="isOpen"/>
		<xsl:call-template name="mc_html_header">
			<xsl:with-param name="mode"  select="$mode"/>
			<xsl:with-param name="isOpen"  select="$isOpen"/>
		</xsl:call-template>
		<body leftmargin="0" marginheight="0" marginwidth="0" topmargin="0" onunload="wb_utils_close_preloading()" onload="init();">
			<div style="margin:10px;">
				<form name="frmXml" enctype="multipart/form-data">

					<xsl:call-template name="mc_question_body">
						<xsl:with-param name="width" select="$width"/>
						<xsl:with-param name="mode" select="$mode"/>
						<xsl:with-param name="header" select="$header"></xsl:with-param>
					</xsl:call-template>
					<xsl:call-template name="additional_information">
						<xsl:with-param name="width" select="$width"/>
						<xsl:with-param name="save_function" select="$save_function"/>
						<xsl:with-param name="cancel_function" select="$cancel_function"/>
						<xsl:with-param name="mode" select="$mode"/>
						<xsl:with-param name="upd_type" select="$upd_type" />
					</xsl:call-template>
				</form>
			</div>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="que_tf">
		<xsl:param name="mode"/>
		<xsl:param name="save_function"/>
		<xsl:param name="cancel_function"/>
		<xsl:param name="width"/>
		<xsl:param name="header"/>
		<xsl:param name="upd_type"/>
		<xsl:param name="isOpen"/>
		<xsl:param name="isT">true</xsl:param>
		<xsl:call-template name="tf_html_header">
			<xsl:with-param name="mode"  select="$mode"/>
			<xsl:with-param name="isOpen"  select="$isOpen"/>
		</xsl:call-template>
		<body leftmargin="0" marginheight="0" marginwidth="0" topmargin="0" onunload="wb_utils_close_preloading()" onload="init();">
			<form name="frmXml" enctype="multipart/form-data">
				<xsl:if test="$header ='YES'  and isT = 'true'">
					<xsl:call-template name="draw_header"/>
				</xsl:if>
				<xsl:call-template name="tf_question_body">
					<xsl:with-param name="width" select="$width"/>
					<xsl:with-param name="mode" select="$mode"/>
					<xsl:with-param name="isOpen" select="$isOpen"/>
					<xsl:with-param name="header" select="$header"></xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="additional_information">
					<xsl:with-param name="width" select="$width"/>
					<xsl:with-param name="save_function" select="$save_function"/>
					<xsl:with-param name="cancel_function" select="$cancel_function"/>
					<xsl:with-param name="mode" select="$mode"/>
					<xsl:with-param name="upd_type" select="$upd_type" />
				</xsl:call-template>
			</form>
		</body>
	</xsl:template>	
	<!-- =============================================================== -->
	<xsl:template name="que_fb">
		<xsl:param name="mode"/>
		<xsl:param name="save_function"/>
		<xsl:param name="cancel_function"/>
		<xsl:param name="width"/>
		<xsl:param name="header"/>
		<xsl:param name="upd_type"/>
		<xsl:param name="isOpen"/>
		<xsl:call-template name="fb_html_header">
			<xsl:with-param name="mode"  select="$mode"/>
			<xsl:with-param name="isOpen"  select="$isOpen"/>
		</xsl:call-template>
		<body leftmargin="0" marginheight="0" marginwidth="0" topmargin="0" onunload="wb_utils_close_preloading()" onload="init();">
			<div style="margin:10px">
				<form name="frmXml" enctype="multipart/form-data">
		
					<xsl:call-template name="fb_question_body">
						<xsl:with-param name="width" select="$width"/>
						<xsl:with-param name="mode" select="$mode"/>
						<xsl:with-param name="sc_type">FB</xsl:with-param>
						<xsl:with-param name="header" select="$header"></xsl:with-param>
						
					</xsl:call-template>
					<xsl:call-template name="additional_information">
						<xsl:with-param name="width" select="$width"/>
						<xsl:with-param name="save_function" select="$save_function"/>
						<xsl:with-param name="cancel_function" select="$cancel_function"/>
						<xsl:with-param name="mode" select="$mode"/>
						<xsl:with-param name="upd_type" select="$upd_type" />
					</xsl:call-template>
				</form>
			</div>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="que_es">
		<xsl:param name="mode"/>
		<xsl:param name="save_function"/>
		<xsl:param name="cancel_function"/>
		<xsl:param name="width"/>
		<xsl:param name="header"/>
		<xsl:param name="upd_type"/>
		<xsl:param name="isOpen"/>
		<xsl:param name="isT">true</xsl:param>
		<xsl:call-template name="es_html_header">
			<xsl:with-param name="mode"  select="$mode"/>
			<xsl:with-param name="isOpen"  select="$isOpen"/>
		</xsl:call-template>
		<body leftmargin="0" marginheight="0" marginwidth="0" topmargin="0" onunload="wb_utils_close_preloading()" onload="init();">
			<form name="frmXml" enctype="multipart/form-data">
				<xsl:if test="$header ='YES' and isT = 'true' ">
					<xsl:call-template name="draw_header"/>
				</xsl:if>
				<xsl:call-template name="es_question_body">
					<xsl:with-param name="width" select="$width"/>
					<xsl:with-param name="mode" select="$mode"/>
					<xsl:with-param name="header" select="$header"></xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="additional_information">
					<xsl:with-param name="width" select="$width"/>
					<xsl:with-param name="save_function" select="$save_function"/>
					<xsl:with-param name="cancel_function" select="$cancel_function"/>
					<xsl:with-param name="mode" select="$mode"/>
					<xsl:with-param name="upd_type" select="$upd_type" />
				</xsl:call-template>
			</form>
		</body>
	</xsl:template>		
	<!-- =============================================================== -->
	<xsl:template name="que_mt">
		<xsl:param name="mode"/>
		<xsl:param name="save_function"/>
		<xsl:param name="cancel_function"/>
		<xsl:param name="width"/>
		<xsl:param name="header"/>
		<xsl:param name="upd_type"/>
		<xsl:param name="isOpen"/>
		<xsl:param name="isT">true</xsl:param>
		<xsl:call-template name="mt_html_header">
			<xsl:with-param name="mode"  select="$mode"/>
			<xsl:with-param name="isOpen"  select="$isOpen"/>
		</xsl:call-template>
		<xsl:call-template name="kindeditor_css"/>
		<script language="JavaScript" type="text/javascript"><![CDATA[
			var source_num = 2;
			var target_num = 2;
			]]><xsl:if test="$mode ='UPD' "><![CDATA[
				source_num = ]]><xsl:value-of select="count(/question/body/interaction)"/> <![CDATA[;
				target_num = source_num;
			]]></xsl:if><![CDATA[
		
		]]></script>
		<body leftmargin="0" marginheight="0" marginwidth="0" topmargin="0" onunload="wb_utils_close_preloading()" onload="init();">
			<form name="frmXml" enctype="multipart/form-data">
				<xsl:if test="$header = 'YES' and isT = 'true'">
					<xsl:call-template name="draw_header"/>
				</xsl:if>
				<xsl:call-template name="mt_question_body">
					<xsl:with-param name="width" select="$width"/>
					<xsl:with-param name="mode" select="$mode"/>
					<xsl:with-param name="isOpen" select="$isOpen"/>
					<xsl:with-param name="header" select="$header"></xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="additional_information">
					<xsl:with-param name="width" select="$width"/>
					<xsl:with-param name="save_function" select="$save_function"/>
					<xsl:with-param name="cancel_function" select="$cancel_function"/>
					<xsl:with-param name="mode" select="$mode"/>
					<xsl:with-param name="upd_type" select="$upd_type" />
				</xsl:call-template>
			</form>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="que_fixed_sc">
		<xsl:param name="mode"/>
		<xsl:param name="save_function"/>
		<xsl:param name="cancel_function"/>
		<xsl:param name="width"/>
		<xsl:param name="header"/>
		<xsl:param name="upd_type"/>
		<xsl:param name="isOpen"/>
		<xsl:call-template name="sc_html_header">
			<xsl:with-param name="mode"  select="$mode"/>
			<xsl:with-param name="isOpen"  select="$isOpen"/>
		</xsl:call-template>
		<body leftmargin="0" marginheight="0" marginwidth="0" topmargin="0" onunload="wb_utils_close_preloading()" onload="init();">
			<form name="frmXml" enctype="multipart/form-data">
				<xsl:if test="$header ='YES' ">
					<xsl:call-template name="draw_header"/>
				</xsl:if>
				<xsl:call-template name="sc_question_body">
					<xsl:with-param name="width" select="$width"/>
					<xsl:with-param name="mode" select="$mode"/>
					<xsl:with-param name="header" select="$header"></xsl:with-param>
					<xsl:with-param name="sc_type">FSC</xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="additional_information">
					<xsl:with-param name="width" select="$width"/>
					<xsl:with-param name="save_function" select="$save_function"/>
					<xsl:with-param name="cancel_function" select="$cancel_function"/>
					<xsl:with-param name="mode" select="$mode"/>
					<xsl:with-param name="upd_type" select="$upd_type" />
				</xsl:call-template>
			</form>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="que_dna_sc">
		<xsl:param name="mode"/>
		<xsl:param name="save_function"/>
		<xsl:param name="cancel_function"/>
		<xsl:param name="width"/>
		<xsl:param name="header"/>
		<xsl:param name="upd_type"/>
		<xsl:param name="isOpen"/>
		<xsl:call-template name="sc_html_header">
			<xsl:with-param name="mode"  select="$mode"/>
			<xsl:with-param name="isOpen"  select="$isOpen"/>
		</xsl:call-template>
		<body leftmargin="0" marginheight="0" marginwidth="0" topmargin="0" onunload="wb_utils_close_preloading()" onload="init();">
			<form name="frmXml" enctype="multipart/form-data">
				<xsl:if test="$header ='YES' ">
					<xsl:call-template name="draw_header"/>
				</xsl:if>
				<xsl:call-template name="sc_question_body">
					<xsl:with-param name="width" select="$width"/>
					<xsl:with-param name="mode" select="$mode"/>
					<xsl:with-param name="sc_type">DSC</xsl:with-param>
					<xsl:with-param name="header" select="$header"></xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="additional_information">
					<xsl:with-param name="width" select="$width"/>
					<xsl:with-param name="save_function" select="$save_function"/>
					<xsl:with-param name="cancel_function" select="$cancel_function"/>
					<xsl:with-param name="mode" select="$mode"/>
					<xsl:with-param name="upd_type" select="$upd_type" />
				</xsl:call-template>
			</form>
		</body>
	</xsl:template>
</xsl:stylesheet>
