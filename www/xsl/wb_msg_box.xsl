<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<!-- utils -->
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/change_lowercase.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<!-- customized utils -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<xsl:output indent="yes"/>
	
	<!-- variable -->
	<xsl:variable name="lab_operate_forward_message" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'lab_operate_forward_message')"/>
	<xsl:variable name="button_ok" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'button_ok')"/>
	<xsl:variable name="global_operate_error" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'global_operate_error')"/>
	<xsl:variable name="global_operate_success" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'global_operate_success')"/>
	<xsl:variable name="url">
		<xsl:choose>
			<xsl:when test="//body/button/@url = ''">javascript:wb_utils_gen_home(true);</xsl:when>
			<xsl:when test="//body/button/@script and body/button/@script = 'true'">javascript:enter();</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="//body/button/@url"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<!-- =============================================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:call-template name="wb_init_lab"/>
		</html>
	</xsl:template>
	<!-- =============================================================================== -->
	<xsl:template name="lang_ch">
		<xsl:apply-templates select="message">
			<xsl:with-param name="lab_status">狀況</xsl:with-param>
			<xsl:with-param name="lab_error">錯誤</xsl:with-param>
			<xsl:with-param name="lab_info">消息</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">確定</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:apply-templates select="message">
			<xsl:with-param name="lab_status">状态</xsl:with-param>
			<xsl:with-param name="lab_error">错误</xsl:with-param>
			<xsl:with-param name="lab_info">消息</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">确定</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:apply-templates select="message">
			<xsl:with-param name="lab_status">Status</xsl:with-param>
			<xsl:with-param name="lab_error">Error</xsl:with-param>
			<xsl:with-param name="lab_info">Information</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- =============================================================================== -->
	<xsl:template match="message">
	<xsl:param name="lab_status"/>
	<xsl:param name="lab_error"/>
	<xsl:param name="lab_info"/>
	<xsl:param name="lab_g_form_btn_ok"/>
		<head>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
				<xsl:text> - </xsl:text>
				<xsl:choose>
					<xsl:when test="title = 'STATUS'">
						<xsl:value-of select="$lab_status"/>
					</xsl:when>
					<xsl:when test="title =  'ERROR'">
						<xsl:value-of select="$lab_error"/>
					</xsl:when>
					<xsl:when test="title =  'INFORMATION'">
						<xsl:value-of select="$lab_info"/>
					</xsl:when>
					<xsl:otherwise></xsl:otherwise>
				</xsl:choose>
			</title>
			<xsl:call-template name="wb_css">
				<xsl:with-param name="view">wb_ui</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="new_css"/>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script type="text/javascript" src="{$wb_js_path}wb_aicc.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}wb_objective.js" language="JavaScript"/>
			<script language="javascript" type="text/javascript"><![CDATA[
				obj = new wbObjective
				aicc = new wbAicc
				function do_module() {
				]]><xsl:if test="//module">
					<xsl:choose>
						<xsl:when test="//module/@action = 'INSERT'">wb_utils_set_cookie('mod_id',<xsl:value-of select="//module/@id"/>)
							<xsl:choose>
								<xsl:when test="title = 'STATUS'">window.parent.addNode('<xsl:call-template name="escape_js"><xsl:with-param name="input_str"><xsl:value-of select="//module"/></xsl:with-param></xsl:call-template>', '<xsl:value-of select="//module/@subtype"/>', <xsl:value-of select="//module/@id"/>, '<xsl:value-of select="//course/@timestamp"/>');</xsl:when>
								<xsl:when test="title = 'ERROR'">window.parent.cancelAdd();</xsl:when>
							</xsl:choose>
						</xsl:when>
						<xsl:when test="//module/@action = 'UPDATE'">
							<xsl:choose>
								<xsl:when test="title = 'STATUS'">window.parent.editNode('<xsl:call-template name="escape_js"><xsl:with-param name="input_str"><xsl:value-of select="//module"/></xsl:with-param></xsl:call-template>', '<xsl:value-of select="//course/@timestamp"/>');</xsl:when>
								<xsl:when test="title = 'ERROR'">window.parent.cancelEdit();</xsl:when>
							</xsl:choose>
						</xsl:when>
						<xsl:when test="//module/@action = 'DELETE'">
							<xsl:choose>
								<xsl:when test="title = 'STATUS'">window.parent.confirmDelete('<xsl:value-of select="//course/@timestamp"/>');</xsl:when>
								<xsl:when test="title = 'ERROR'">window.parent.cancelDelete();</xsl:when>
							</xsl:choose>
						</xsl:when>
					</xsl:choose>
				</xsl:if><![CDATA[
			}
			
			function exedjs_function(action){
			    //action = 1  关闭当前窗口
			    if(action == 1){
			    	window.close()
			    }
			    
			     if(action == 2){
			    	window.close()
			    }
			}

			function enter(){
				script = "]]><xsl:value-of select="body/button/@script"/><![CDATA[";
				ftn = "]]><xsl:call-template name="escape_js"><xsl:with-param name="input_str"><xsl:value-of select="body/button/@url"/></xsl:with-param></xsl:call-template><![CDATA[";
				if(script=='true'){
					]]>
					<xsl:if test="body/button/@script and body/button/@script = 'true'">
					<xsl:value-of select="body/button/@url"/>
					</xsl:if>
					<![CDATA[
				}else if(ftn==""){
					wb_utils_gen_home(true);
				}else{
					window.location.href = "]]><xsl:call-template name="escape_js"><xsl:with-param name="input_str"><xsl:value-of select="body/button/@url"/></xsl:with-param></xsl:call-template><![CDATA["
				}
				return false;
			}
			
			function go_url(url){
				 while(url.indexOf( "&quot;" ) != -1 ) {
				     url = url.replace("&quot;","\'");
				}
				self.location.href = url;			
			}
			
			function url_error(url){
				alert('Not supported URL format: ' + url )
			}
			
			function change_frame(){
				//parent.document.all.frmset.rows = "0,0,*";
				window.parent.document.getElementById('all_frameid').rows = "0,0,*";
			
			}

			var maxtime = 3; //单位：秒
			var timer;
			function CountDown() {
			    if (maxtime > 0) {
			    	document.getElementById("second").innerHTML = maxtime;
			        --maxtime;
			    }
			    else {
			        clearInterval(timer);
			        var url = "]]><xsl:call-template name="determine_url"><xsl:with-param name="url" select="$url"/></xsl:call-template><![CDATA[";
			        while(url.indexOf( "&quot;" ) != -1 ) {
					     url = url.replace("&quot;","\'"); 
					}
			        self.location.href = url;
			    }
			}
			
			function go(){
				var url = "]]><xsl:call-template name="determine_url"><xsl:with-param name="url" select="$url"/></xsl:call-template><![CDATA[";
		        while(url.indexOf( "&quot;" ) != -1 ) {
				     url = url.replace("&quot;","\'");
				}
		        self.location.href = url;
			}
		]]>
		</script>
		</head>
		<xsl:variable name="onload_script">
			<xsl:choose>
				<xsl:when test="//is_tst_submit_err = 'true'">javascript:change_frame();document.frmXml.elements[0].focus();</xsl:when>
				<xsl:otherwise>javascript:try{do_module();document.frmXml.elements[0].focus();}catch(e){}</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<body leftmargin="0" marginheight="0" marginwidth="0" topmargin="0" onload="{$onload_script}">
			<input id="msg_box" type="hidden" value="wb_msg_box"/>
			<form name="frmXml" onsubmit="enter();return false;">
				<div class="panel-body">
			        <div class="mode">
			             <div>
			             
			             	  <xsl:choose>
				             	  	<xsl:when test="title= 'ERROR'">
				             	  		<xsl:attribute name="class">losepage-2 losepage-3 sessionTimeOut</xsl:attribute>
				             	  		<div class="losepage_tit"><xsl:value-of select="$global_operate_error"></xsl:value-of></div>
				             	  	</xsl:when>
				             	  	<xsl:otherwise>
				             	  		<xsl:attribute name="class">losepage-2 sessionTimeOut</xsl:attribute>
				             	  		<div class="losepage_tit"><xsl:value-of select="$global_operate_success"></xsl:value-of></div>
				             	  	</xsl:otherwise>
			             	  </xsl:choose>
			                  
			                  <div class="losepage_info">
			                  	<xsl:value-of disable-output-escaping="yes" select="body/text"/><br/>
			                  	<xsl:if test="title != 'ERROR'">
			                  		<xsl:value-of disable-output-escaping="yes" select="$lab_operate_forward_message" ></xsl:value-of>
			                  	</xsl:if>
			                  </div>
			                  <div class="losepage_desc">
			                  	<xsl:call-template name="wb_gen_form_button">
									<xsl:with-param name="wb_gen_btn_name" select="$button_ok"/>
									<xsl:with-param name="wb_gen_btn_href"><xsl:call-template name="determine_url"><xsl:with-param name="url" select="$url"/></xsl:call-template></xsl:with-param>
								</xsl:call-template>
			                  </div>
			             </div>
			        </div>
			    </div>
				<script language="javascript" type="text/javascript">
					<![CDATA[
					if(document.all){
					 document.write('<input type="submit" value="" size="0" style="height:0px;width:0px;visibility:hidden;">')
					}
					]]>
					<xsl:choose>
					<xsl:when test="//message//body//button//@tag = 'true'" >
					</xsl:when>
					<xsl:when test="title= 'ERROR'">
					</xsl:when>
					<xsl:otherwise>
						<![CDATA[
						timer = setInterval("CountDown()", 1000);
					]]>
				    </xsl:otherwise>
			</xsl:choose>
				</script>
			</form>
		</body>
	</xsl:template>
	<!-- =============================================================================== -->
	<xsl:template name="determine_url">
		<xsl:param name="url"/>
		<xsl:variable name="js_str">
			<xsl:call-template name="change_lowercase">
				<xsl:with-param name="input_value" select="substring($url,1,11)"/>
			</xsl:call-template>
		</xsl:variable>
		<xsl:choose>
		
		   <xsl:when test="contains($url,'exedjs_function')">javascript:<xsl:value-of select="$url"/></xsl:when>
			<xsl:when test="contains($js_str,'javascript:')">
				<xsl:value-of select="$url"/>
			</xsl:when>
			<xsl:when test="contains(substring($url,1,7),'http://') or contains(substring($url,1,8),'https://')">javascript:go_url('<xsl:call-template name="escape_js"><xsl:with-param name="input_str"><xsl:value-of select="$url"/></xsl:with-param></xsl:call-template>')</xsl:when>
			<xsl:when test="contains(substring($url,1,11),'../servlet/') or contains(substring($url,1,7),'../htm/')">javascript:go_url('<xsl:call-template name="escape_js"><xsl:with-param name="input_str"><xsl:value-of select="$url"/></xsl:with-param></xsl:call-template>')</xsl:when>		
			<xsl:when test="contains(substring($url,1,1),'/')">javascript:go_url('<xsl:call-template name="escape_js"><xsl:with-param name="input_str"><xsl:value-of select="$url"/></xsl:with-param></xsl:call-template>')</xsl:when>
			<xsl:otherwise>javascript:window.location.href='<xsl:call-template name="escape_js"><xsl:with-param name="input_str"><xsl:value-of select="$url"/></xsl:with-param></xsl:call-template>'</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template name="replaceFunc">
		<xsl:param name="text" />
		<xsl:param name="replace" />
		<xsl:param name="by" />
		<xsl:choose>
			<xsl:when test="contains($text,$replace)">
				<xsl:value-of select="substring-before($text,$replace)" />
				<xsl:value-of select="$by" />
				<xsl:call-template name="replaceFunc">
					<xsl:with-param name="text"
						select="substring-after($text,$replace)" />
					<xsl:with-param name="replace" select="$replace" />
					<xsl:with-param name="by" select="$by" />
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$text" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================================== -->
</xsl:stylesheet>
