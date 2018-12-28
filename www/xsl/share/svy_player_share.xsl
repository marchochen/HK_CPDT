<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<!-- ========================================================== -->
	<xsl:variable name="quiz" select="quiz"/>
	<xsl:variable name="wb_gen_table_width">100%</xsl:variable>
<!-- ========================================================== -->
<xsl:template match="/">
	<html><xsl:apply-templates/></html>
</xsl:template>
<!-- ========================================================== -->
<xsl:template match="quiz">
	<xsl:call-template name="wb_init_lab"/>
</xsl:template>
<!-- ========================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_g_form_btn_submit">提交</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_no_que">尚未定義問題。</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">關閉</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_g_form_btn_submit">提交</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_no_que">尚未定义问题。</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">关闭</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_g_form_btn_submit">Submit</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_no_que">No questions defined.</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">Close</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
<!--==============================================-->

<!--  test player - fb interaction  -->
<xsl:template match="question[body/interaction[@type='FB']]" mode="TST">
<xsl:variable name="_pos" select="position()" />
<xsl:variable name="style">
<xsl:if test="$_pos > 3">
	display:none
</xsl:if>	
</xsl:variable>
	<li style="{$style}">
		<form name="frm{position()}" onSubmit="return status()">
		<span class="xyd-survey-icon"><xsl:value-of select="$_pos"/></span>
		<dl>
			<dt style="height:auto;">
				<xsl:for-each select="body/text() | body/html ">
					<xsl:value-of disable-output-escaping="yes" select="."/>
			    </xsl:for-each>
			    <xsl:choose>
					<xsl:when test="body/object">
						<br/>
						<xsl:apply-templates select="body/object"/>
					</xsl:when>
					<xsl:otherwise>
						<img border="0" height="1" width="1" src="{$wb_img_path}tp.gif"/>
					</xsl:otherwise>
		 		</xsl:choose> 
			</dt>
        	<dd>
        		<xsl:for-each select="body/interaction">
          			<textarea class="wbpl showhide wzb-textarea-01" name ="txt_q{$_pos}_i{@order}" onChange="qset.getQuestion({$_pos}).getInteraction({@order}).setAnswer(this.value)"></textarea>
        		</xsl:for-each>
        	</dd>
		</dl>
		</form>
	</li>
<script language="JavaScript" type="text/javascript"><![CDATA[
	q = new CwQuestion ('layer1', ']]><xsl:value-of select="@id"/><![CDATA[')

    ]]><xsl:for-each select="body/interaction"><![CDATA[
		control = document.frm]]><xsl:value-of select="$_pos"/><![CDATA[.txt_q]]><xsl:value-of select="$_pos"/><![CDATA[_i]]><xsl:value-of select="@order"/><![CDATA[
	interaction = new CwInteraction (']]><xsl:value-of select="../../@id"/><![CDATA[',']]><xsl:value-of select="@order"/><![CDATA[', 'FB', control)		
	q.addInteraction(interaction)
	]]></xsl:for-each><![CDATA[
	control.value = interaction.getAnswer();
	qset.addQuestion(q)		
]]>
</script>
</xsl:template>
<!--==============================================-->
<xsl:template match="question[body/interaction[@type='MC']]" mode="TST">
<!-- test player - mc interaction -->
<xsl:variable name="_pos" select="position()"/>
<xsl:variable name="_order" select="body/interaction/@order"/>
<xsl:variable name="style">
	<xsl:if test="$_pos > 3">
		display:none
	</xsl:if>	
</xsl:variable>
	<li style="{$style}">
		<form name="frm{$_pos}" onSubmit="return status()">
		<span class="xyd-survey-icon"><xsl:value-of select="position()"/></span>
   		<dl>
       		<dt style="height:auto;">
				<xsl:choose>
					<xsl:when test="body/html">
						<xsl:value-of disable-output-escaping="yes" select="body/html"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="unescape_html_linefeed">
							<xsl:with-param name="my_right_value">
								<xsl:value-of select="body/text()"/>
							</xsl:with-param>
						</xsl:call-template>
					</xsl:otherwise>
				</xsl:choose>
			</dt>
       		<dd>
				<xsl:variable name="que_has_other_option">
					<xsl:choose>
						<xsl:when test="count(body/interaction[@type='MC']/option[@other_option_ind])">true</xsl:when>
						<xsl:otherwise>false</xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				<xsl:variable name="muti_choice_ind">
					<xsl:choose>
						<xsl:when test="body/interaction/@logic = 'OR'">true</xsl:when>
						<xsl:otherwise>false</xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				<xsl:for-each select="body/interaction[@type='MC']/option">
					<xsl:if test="@other_option_ind = 'true'">
						<script><![CDATA[
							function onChangeAns_q]]><xsl:value-of select="$_pos"/><![CDATA[_i]]><xsl:value-of select="$_order"/><![CDATA[(obj, is_disabled, is_other_option) {
								var other_option_extend = document.getElementById('other_q]]><xsl:value-of select="$_pos"/><![CDATA[_i]]><xsl:value-of select="$_order"/><![CDATA[');
								if(other_option_extend != undefined) {
									if(obj.checked == true) {
										other_option_extend.disabled = is_disabled;
										//set the answer extend of interaction
										if(is_disabled == true) {
											qset.getQuestion(]]><xsl:value-of select="$_pos"/>).getInteraction(<xsl:value-of select="$_order"/><![CDATA[).setAnswerExtend('');
										} else {
											qset.getQuestion(]]><xsl:value-of select="$_pos"/>).getInteraction(<xsl:value-of select="$_order"/><![CDATA[).setAnswerExtend(wbUtilsTrimString(other_option_extend.value));
										}
									} else {
										if(is_other_option) {
											other_option_extend.disabled = true;
										}
									}
								}
							}
							function onBlurText_q]]><xsl:value-of select="$_pos"/><![CDATA[_i]]><xsl:value-of select="$_order"/><![CDATA[(obj) {
								var testStr = wbUtilsTrimString(obj.value);
								if(obj.disabled == false && testStr.length > 0) {
									qset.getQuestion(]]><xsl:value-of select="$_pos"/>).getInteraction(<xsl:value-of select="$_order"/><![CDATA[).setAnswerExtend(obj.value);
								}
							}
						]]></script>
					</xsl:if>
					<p class="dcmess">
						<label for="rdo_q{$_pos}_i{$_order}_{position()}" class="radiosite">
							<input name="rdo_q{$_pos}_i{$_order}" value="{@id}" id="rdo_q{$_pos}_i{$_order}_{position()}" style="margin:0 6px 2px 0;">
								<xsl:attribute name="type">
									<xsl:choose>
										<xsl:when test="$muti_choice_ind = 'true'">checkbox</xsl:when>
										<xsl:otherwise>radio</xsl:otherwise>
									</xsl:choose>
								</xsl:attribute>
								<xsl:attribute name="onClick">
									qset.getQuestion(<xsl:value-of select="$_pos"/>).getInteraction(<xsl:value-of select="$_order"/>).setAnswer(this.value);
									<xsl:if test="$que_has_other_option = 'true' and @other_option_ind = 'true'">onChangeAns_q<xsl:value-of select="$_pos"/>_i<xsl:value-of select="$_order"/>(this, false, true);</xsl:if>
									<xsl:if test="$que_has_other_option = 'true' and $muti_choice_ind = 'false' and not(@other_option_ind)">onChangeAns_q<xsl:value-of select="$_pos"/>_i<xsl:value-of select="$_order"/>(this, true);</xsl:if>
								</xsl:attribute>
							</input>
							<xsl:choose>
								<xsl:when test="html">
									<xsl:value-of disable-output-escaping="yes" select="html"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:call-template name="unescape_html_linefeed">
										<xsl:with-param name="my_right_value"><xsl:value-of select="."/></xsl:with-param>
									</xsl:call-template>
								</xsl:otherwise>
							</xsl:choose>										
						</label>
						<!-- 
						<xsl:if test="@other_option_ind = 'true'">
							<xsl:text>&#160;</xsl:text>
							<input class="wzb-inputText"  type="text" id="other_q{$_pos}_i{$_order}" name="other_q{$_pos}_i{$_order}" size="20" maxlength="255" disabled="disabled" onblur="onBlurText_q{$_pos}_i{$_order}(this);"/>
						</xsl:if>
						 -->
						<xsl:apply-templates select="object"/>
					</p>	
				</xsl:for-each>
        	</dd>
        </dl>
        </form>
    </li>
<xsl:variable name="que_mc_type">
	<xsl:choose>
		<xsl:when test="body/interaction[@type='MC']/@logic = 'OR'">MC_M</xsl:when>
		<xsl:otherwise>MC</xsl:otherwise>
	</xsl:choose>
</xsl:variable>
<script language="JavaScript" type="text/javascript">
<![CDATA[
		q = new CwQuestion ('layer1', ']]><xsl:value-of select="@id" /><![CDATA[')
			control = document.frm]]><xsl:value-of select="$_pos" /><![CDATA[.rdo_q]]><xsl:value-of select="$_pos"/><![CDATA[_i]]><xsl:value-of select="$_order"/><![CDATA[
		//----------------------------
		interaction = new CwInteraction (']]><xsl:value-of select="@id" /><![CDATA[',']]><xsl:value-of select="$_order" /><![CDATA[', ']]><xsl:value-of select="$que_mc_type" /><![CDATA[', control)		
		q.addInteraction(interaction)
		qset.addQuestion(q)		
		if(interaction.getAnswer()!=""){
			if(interaction.getAnswer()==0)
				control.checked = true;
			else
				control[interaction.getAnswer()-1].checked = true;
		}
]]>	
</script>
</xsl:template>
	
</xsl:stylesheet>
