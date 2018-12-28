<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:import href="wb_const.xsl"/>
    <xsl:import href="utils/wb_utils.xsl"/>
    <xsl:import href="utils/wb_ui_title.xsl"/>
    <xsl:import href="utils/wb_ui_line.xsl"/>
    <xsl:import href="utils/wb_ui_head.xsl"/>
    <xsl:import href="utils/wb_gen_button.xsl"/>
    <xsl:import href="utils/wb_ui_desc.xsl"/>
    <xsl:import href="utils/wb_init_lab.xsl"/>
    <xsl:import href="utils/wb_css.xsl"/>
    <xsl:import href="cust/wb_cust_const.xsl"/>
    <xsl:output indent="yes"/>
    <xsl:variable name="module_id" select="/*/@id"/>
    <xsl:variable name="course_id" select="/*/header/@course_id"/>
    <xsl:variable name="mod_type" select="/*/header/@subtype"/>
    <xsl:variable name="cur_tpl_nm" select="/*/header/template_list/@cur_tpl"/>
    <xsl:variable name="mode" select="/*/@view_mode"/>
	<xsl:variable name="stylesheet">
    	<xsl:choose>
    		<xsl:when test="/*/header/@test_style = 'many'">tst_player_many.xsl</xsl:when>
    		<xsl:otherwise>tst_player1.xsl</xsl:otherwise>
    	</xsl:choose>
    </xsl:variable>      <!--  <xsl:variable name="stylesheet">tst_player1.xsl</xsl:variable> -->

    <!-- =============================================================== -->
    <xsl:template match="/">
        <html>
            <xsl:apply-templates/>
        </html>
    </xsl:template>
    <!-- =============================================================== -->
    <xsl:template match="module | fixed_assessment | dynamic_que_container">
        <head>
            <title>
                <xsl:value-of select="$wb_wizbank"/>
            </title>
            <script language="JavaScript" src="{$wb_js_path}gen_utils.js" type="text/javascript"/>
            <script language="JavaScript" src="{$wb_js_path}wb_utils.js" type="text/javascript"/>
            <script language="JavaScript" src="{$wb_js_path}wb_module.js" type="text/javascript"/>
            <script language="JavaScript" src="{$wb_js_path}wb_assessment.js" type="text/javascript"/>
            <script language="JavaScript" src="{$wb_js_path}urlparam.js" type="text/javascript"/>
            <script language="JavaScript" src="{$wb_js_lang_path}wb_label.js" type="text/javascript"/>
            <SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
				var module_lst = new wbModule 				
				var wb_tst = new wbTst;
				var asm = new wbAssessment;
			function go_summary(){
				parent.content.location.href = wb_tst.info_content_url(]]>'<xsl:value-of select="$module_id"/>','<xsl:value-of select="$course_id"/>', '<xsl:value-of select="$mod_type"/>', '<xsl:value-of select="@view_mode"/>'<![CDATA[);
			}	
				
			function resize_frame(){
				if(document.all){
					if(parent && parent.window && parent.window.resize_fs){
						parent.window.resize_fs(document.all['bottom_img'].offsetTop +5 );	
					}
				}else if(document.getElementById != null){
					if(parent && parent.window && parent.window.resize_fs){
						parent.window.resize_fs(document.getElementById('bottom_img').offsetTop +5 );	
					}			
				}
			}
			]]></SCRIPT>
            <xsl:call-template name="new_css"/>
            <meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
        </head>
        <body marginwidth="0" marginheight="0" topmargin="0" leftmargin="0" onload="resize_frame()">
            <xsl:call-template name="wb_init_lab"/>
        </body>
    </xsl:template>
    <!-- =============================================================== -->
    <xsl:template name="lang_ch">
        <xsl:call-template name="content">
            <xsl:with-param name="lab_tst_inst"><div class="wzb-ui-module-text">你需要從資源庫中選取指定的題目按指定的次序放進試卷。<xsl:if test="not($mode = 'READONLY') ">按<b>題目</b>部分的<b>添加</b>按鈕，根據提示的步驟可以完成試題的添加。<b>靜態測驗中添加的題目是教學資源管理中題目的一個副本，您可以在這裡直接編輯題目的內容及分數，所有改動都不會影響教學資源管理中的原始題目。</b></xsl:if></div></xsl:with-param>
            <xsl:with-param name="lab_dxt_inst"><div class="wzb-ui-module-text">動態測驗按預先設定的條件從資源庫中隨機抽取題目。試卷將於用戶開始測驗時即時生成。題目選拔準則是用來指定題目在教學資源管理中的產生範圍。<xsl:if test="not($mode = 'READONLY') ">按<b>題目選拔準則</b>中的<b>添加準則</b>按鈕可以指定題目的範圍。</xsl:if><b>這裡的題目都是從教學資源管理中直接抽取的，因此在教學資源管理中對題目的任何改動都會直接影響到測驗。</b></div></xsl:with-param>
            <xsl:with-param name="lab_tst_builder">
                <xsl:choose>
                    <xsl:when test="$mode = 'READONLY'">測驗內容</xsl:when>
                    <xsl:otherwise>制作試卷</xsl:otherwise>
                </xsl:choose>
            </xsl:with-param>
            <xsl:with-param name="lab_dxt_builder">
                <xsl:choose>
                    <xsl:when test="$mode = 'READONLY'">測驗內容</xsl:when>
                    <xsl:otherwise>制作試卷</xsl:otherwise>
                </xsl:choose>
            </xsl:with-param>
            <xsl:with-param name="lab_g_txt_btn_finish">
                <xsl:choose>
                    <xsl:when test="$mode = 'READONLY'">關閉</xsl:when>
                    <xsl:otherwise>完成</xsl:otherwise>
                </xsl:choose>
            </xsl:with-param>
            <xsl:with-param name="lab_g_txt_bth_preview_test">預覽試卷</xsl:with-param>
            <xsl:with-param name="lab_g_txt_btn_test_summary">試卷摘要</xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    <xsl:template name="lang_gb">
        <xsl:call-template name="content">
            <xsl:with-param name="lab_tst_inst"><div class="wzb-ui-module-text">静态测验是从资源管理中抽取的一组试题集。<xsl:if test="not($mode = 'READONLY') ">点击<b>题目</b>部分的<b>添加</b>按钮，根据提示的步骤可以完成试题的添加。<b>静态测验中添加的题目是资源管理中题目的一个副本，您可以在这里直接编辑题目的内容及分值，所有改动都不会影响资源管理中的原始题目。</b>  </xsl:if></div></xsl:with-param>
            <xsl:with-param name="lab_dxt_inst"><div class="wzb-ui-module-text">动态测验中的题目是在用户参加测验时实时生成的。抽题条件用于指定题目在资源管理中的生成范围。<xsl:if test="not($mode = 'READONLY') ">点击<b>抽题条件</b>中的<b>添加</b>按钮可以指定题目的范围。</xsl:if><b>这里的题目都是从资源管理直接抽取的，因此在资源管理中对题目的任何改动都会直接影响到测验。</b></div></xsl:with-param>
            <xsl:with-param name="lab_tst_builder">
                <xsl:choose>
                    <xsl:when test="$mode = 'READONLY'">测验内容</xsl:when>
                    <xsl:otherwise>制作试卷</xsl:otherwise>
                </xsl:choose>
            </xsl:with-param>
            <xsl:with-param name="lab_dxt_builder">
                <xsl:choose>
                    <xsl:when test="$mode = 'READONLY'">测验内容</xsl:when>
                    <xsl:otherwise>制作试卷</xsl:otherwise>
                </xsl:choose>
            </xsl:with-param>
            <xsl:with-param name="lab_g_txt_btn_finish">
                <xsl:choose>
                    <xsl:when test="$mode = 'READONLY'">关闭</xsl:when>
                    <xsl:otherwise>完成</xsl:otherwise>
                </xsl:choose>
            </xsl:with-param>
            <xsl:with-param name="lab_g_txt_bth_preview_test">试卷预览</xsl:with-param>
            <xsl:with-param name="lab_g_txt_btn_test_summary">试卷摘要</xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    <xsl:template name="lang_en">
        <xsl:call-template name="content">
            <xsl:with-param name="lab_tst_inst"><div class="wzb-ui-module-text">You need to pick up a set of questions and define their sequence in the test paper. <xsl:if test="not($mode = 'READONLY') "> To specify the questions, click <b>Add</b> in the <b>Question</b> section and follow the steps.  <b>The questions added to a fixed test are separate copies and you can edit their content and scores here. The changes made here will not affect the original copies in Learning Resource Management.</b>  </xsl:if></div></xsl:with-param>
            <xsl:with-param name="lab_dxt_inst"><div class="wzb-ui-module-text">A dynamic test picks up questions from one or more question pools randomly  according to the pre-defined criteria. The test paper will be generated  in real time when user begin the test. It is defined by a set of question selection criteria, that will be used for generating questions from Learning Resource Management. <xsl:if test="not($mode = 'READONLY') ">Click <b>Add criteria</b> in the  <b>Selection criteria</b> section to specify the criteria. </xsl:if> <b>Since questions are retrieved directly from Learning Resource Management, any changes made to the  questions there will have immediate effect on the test. </b></div></xsl:with-param>
            <xsl:with-param name="lab_tst_builder">
                <xsl:choose>
                    <xsl:when test="$mode = 'READONLY'">Test content</xsl:when>
                    <xsl:otherwise>Test builder</xsl:otherwise>
                </xsl:choose>
            </xsl:with-param>
            <xsl:with-param name="lab_dxt_builder">
                <xsl:choose>
                    <xsl:when test="$mode = 'READONLY'">Test content</xsl:when>
                    <xsl:otherwise>Dynamic test builder</xsl:otherwise>
                </xsl:choose>
            </xsl:with-param>
            <xsl:with-param name="lab_g_txt_btn_finish">
                <xsl:choose>
                    <xsl:when test="$mode = 'READONLY'">Close</xsl:when>
                    <xsl:otherwise>Finish</xsl:otherwise>
                </xsl:choose>
            </xsl:with-param>
            <xsl:with-param name="lab_g_txt_bth_preview_test">Preview test</xsl:with-param>
            <xsl:with-param name="lab_g_txt_btn_test_summary">Test summary</xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    <!-- =============================================================== -->
    <xsl:template name="content">
        <xsl:param name="lab_tst_inst"/>
        <xsl:param name="lab_tst_builder"/>
        <xsl:param name="lab_dxt_inst"/>
        <xsl:param name="lab_g_txt_btn_finish"/>
        <xsl:param name="lab_g_txt_bth_preview_test"/>
        <xsl:param name="lab_g_txt_btn_test_summary"/>
        <xsl:param name="lab_dxt_builder"/>
        <xsl:call-template name="wb_ui_title">
            <xsl:with-param name="text">
                <xsl:choose>
                    <xsl:when test="$mod_type = 'DXT' or $mod_type = 'DAS'">
                        <xsl:value-of select="$lab_dxt_builder"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="$lab_tst_builder"/>
                    </xsl:otherwise>
                </xsl:choose>
                <xsl:text>&#160;-&#160;</xsl:text>
                <xsl:choose>
                    <xsl:when test="header/title">
                        <xsl:value-of select="header/title/text()"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="body/title/text()"/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:with-param>
        </xsl:call-template>
        <table width="100%">
            <tr>
                <td>
                    <div class="Desc">
                        <xsl:choose>
                            <xsl:when test="$mod_type = 'DXT' or $mod_type = 'DAS'">
                                <xsl:copy-of select="$lab_dxt_inst"/>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:copy-of select="$lab_tst_inst"/>
                            </xsl:otherwise>
                        </xsl:choose>
                    </div>
                </td>
            </tr>
        </table>
        <table width="100%">
            <tr>
                <td  align="right">
                    <xsl:if test="$mod_type != 'DXT' and $mod_type != 'DAS' and not($mode = 'READONLY') ">
                        <xsl:call-template name="wb_gen_button">
                            <xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
                            <xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_test_summary"/>
                            <xsl:with-param name="wb_gen_btn_href">javascript:go_summary();</xsl:with-param>
                        </xsl:call-template>
                    </xsl:if>
                    <xsl:if test="((//@q_count) > 0) or @subtype = 'EVN'">
                        <img src="{$wb_img_path}tp.gif" width="1" height="3" border="0"/>
                        <xsl:call-template name="wb_gen_button">
                       	    <xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
                            <xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_bth_preview_test"/>
                            <xsl:with-param name="wb_gen_btn_href">
                                <xsl:choose>
                                    <xsl:when test="$mod_type = 'DAS' or $mod_type = 'FAS'">javascript:asm.preview('<xsl:value-of select="$module_id"/>')</xsl:when>
                                    <xsl:otherwise>javascript:module_lst.preview_exec('<xsl:value-of select="$mod_type"/>',<xsl:value-of select="$module_id"/>,'<xsl:value-of select="$stylesheet"/>')</xsl:otherwise>
                                </xsl:choose>
                            </xsl:with-param>
                        </xsl:call-template>
                    </xsl:if>
               
                    <xsl:call-template name="wb_gen_button">
                        <xsl:with-param name="class">btn wzb-btn-orange margin-right2</xsl:with-param>
                        <xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_finish"/>
                        <xsl:with-param name="wb_gen_btn_href">javascript:parent.window.close()</xsl:with-param>
                    </xsl:call-template>
                </td>
            </tr>
        </table>
        <img src="{$wb_img_path}tp.gif" width="1" height="3" border="0" id="bottom_img"/>
        <xsl:call-template name="wb_ui_line"/>
    </xsl:template>
</xsl:stylesheet>
