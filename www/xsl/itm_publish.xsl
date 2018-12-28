<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_nav_link.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<!-- share -->
	<xsl:import href="share/sys_tab_share.xsl"/>
	<xsl:import href="share/itm_gen_frm_share.xsl"/>
	<xsl:import href="share/usr_detail_label_share.xsl"/>
	<xsl:import href="share/itm_gen_action_nav_share.xsl"/>
	
	<xsl:variable name="itm_id" select="/applyeasy/item/@id"/>
	<xsl:variable name="itm_target_status" select="/applyeasy/item/@status"/>
	<xsl:variable name="itm_mobile_ind" select="/applyeasy/item/@itm_mobile_ind"/>
	<xsl:variable name="itm_updated_timestamp" select="/applyeasy/item/last_updated/@timestamp"/>
	<xsl:variable name="run_ind" select="/applyeasy/item/@run_ind"/>
	<xsl:variable name="session_ind" select="/applyeasy/item/@session_ind"/>
	<xsl:variable name="error_code" select="/applyeasy/meta/error_message/code/text()"/>
	<xsl:variable name="error_msg" select="/applyeasy/meta/error_message/content"/>
	<xsl:variable name="itm_type_lst" select="/applyeasy/item/item_type/@id"/>
	<xsl:variable name="ref_ind" select="/applyeasy/item/nav/item/@ref_ind"/>
	
	<xsl:variable name="lab_select_default_image" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_cur_lang, 'lab_select_default_image')"/>
	<xsl:variable name="lab_default_images" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_cur_lang, 'lab_default_images')"/>
	<xsl:variable name="lab_upload_image" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_cur_lang, 'lab_upload_image')"/>
	<xsl:variable name="lab_button_ok" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_cur_lang, '329')"/>
	<xsl:variable name="lab_g_form_btn_cancel" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '330')"/>
	<xsl:variable name="lab_by_all_user" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_by_all_user')"/>
	<!-- 发布 -->
	<xsl:variable name="label_core_training_management_255" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_core_training_management_255')"/>
	<!-- 更改发布对像 -->
	<xsl:variable name="label_core_training_management_256" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_256')" />

	<!-- 重要提示 -->
	<xsl:variable name="label_core_training_management_317" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_317')" />
	<!-- 课程、考试的总分并不是直接取在学习内容的所得分，而是通过<b>计分项目</b>功能中设置的计分规则计算而得到。所以，如果你现在要发布的这门课程是需要以学员最后获得分数多少来衡量学员的学习结果的，那么你必须先设置好计分项目后才能发布课程、考试。否则，学员在该课程、考试的得分将为0分。对于分数的计算规则，详细请看计分项目功能页面的说明。 -->
	<xsl:variable name="label_core_training_management_318" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_318')" />
	<!-- 在发布框架信息前，请你先认真核对课程的各项设置信息，如果你已确认课程信息是否已设置完整，请在页底点击按钮发布课程。当然，你也可以设置必填信息，先发布课程后，然后再回来修改其它设置。 -->
	<xsl:variable name="label_core_training_management_319" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_319')" />
	
	<xsl:variable name="label_core_training_management_328" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_328')" />
	<xsl:variable name="label_core_training_management_329" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_329')" />
	<xsl:variable name="label_core_training_management_330" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_330')" />
	<xsl:variable name="label_core_training_management_331" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_331')" />
	<xsl:variable name="label_core_training_management_332" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_332')" />
	<xsl:variable name="label_core_training_management_333" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_333')" />
	<xsl:variable name="label_core_training_management_334" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_334')" />
	<xsl:variable name="label_core_training_management_335" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_335')" />
	<xsl:variable name="label_core_training_management_336" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_336')" />
	<xsl:variable name="label_core_training_management_337" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_337')" />
	<xsl:variable name="label_core_training_management_342" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_core_training_management_342')" />
	<xsl:variable name="label_core_training_management_346" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_core_training_management_346')" />
	<xsl:variable name="label_core_training_management_347" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_core_training_management_347')" />
	<xsl:variable name="label_core_training_management_348" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_core_training_management_348')" />
	<xsl:variable name="label_core_training_management_349" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_core_training_management_349')" />
	
	<xsl:variable name="label_core_training_management_387" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_core_training_management_387')" />
	<xsl:variable name="label_core_training_management_388" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_core_training_management_388')" />
	<xsl:variable name="label_core_training_management_389" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_core_training_management_389')" />
	<xsl:variable name="label_core_training_management_390" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_core_training_management_390')" />
	<xsl:variable name="label_core_training_management_391" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_core_training_management_391')" />
	<xsl:variable name="label_core_training_management_392" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_core_training_management_392')" />
	
	
	
	<!-- 判断是否有设置网上内容等，没有设置则在标签上面进行提示 -->
	<xsl:variable name="rmdClassManagement" select="//courseTabsRemind/rmdClassManagement" />
	<xsl:variable name="hadPublishClass" select="//itm_action_nav/@has_run" />
	<xsl:variable name="rmdCompletionCriteriaSettings" select="//courseTabsRemind/rmdCompletionCriteriaSettings" />
	<xsl:variable name="rmdCoursePackage" select="//courseTabsRemind/rmdCoursePackage" />
	<xsl:variable name="rmdCourseScoreSettings" select="//courseTabsRemind/rmdCourseScoreSettings" />
	<xsl:variable name="rmdOnlineContent" select="//courseTabsRemind/rmdOnlineContent" />
	<xsl:variable name="rmdTargetLearner" select="//courseTabsRemind/rmdTargetLearner" />
	<xsl:variable name="rmdTimetable" select="//courseTabsRemind/rmdTimetable" />
	
	<xsl:variable name="itm_exam_ind" select="//itm_action_nav/@itm_exam_ind" />
	<xsl:variable name="itm_type" select="//itm_action_nav/@itm_type" />
	<xsl:variable name="create_run_ind" select="//itm_action_nav/@itm_create_run_ind" />
	<xsl:variable name="itm_status" select="//itm_action_nav/@itm_status" />
	<xsl:variable name="training_type" select="/applyeasy/training_type"/>
	 	
	<xsl:output indent="yes"/>
	<!-- ============================================================= -->
	<xsl:template match="/applyeasy">
		<html>
			<xsl:call-template name="main"/>
		</html>
	</xsl:template>
	<!-- ============================================================= -->
	<xsl:template name="main">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_item.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="Javascript"><![CDATA[	
				itm = new wbItem;
				
				function cancel_publish(){
					document.frmResult.itm_status.value = "OFF";
					itm.upd_item_target_ref(document.frmResult,'item_status', 'false','$run_ind');
				}
				
			]]></script>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
			<form name="frmResult">
				<input type="hidden" name="module"/>
				<input type="hidden" name="cmd"/>
				<input type="hidden" name="stylesheet"/>
				<input type="hidden" name="url_success"/>
				<input type="hidden" name="url_failure"/>
				<input type="hidden" name="itm_upd_timestamp" value="{$itm_updated_timestamp}"/>
				<input type="hidden" name="itm_id" value="{$itm_id}"/>
				<input type="hidden" name="itm_id_lst" value="{$itm_id}"/>
				<input type="hidden" name="itm_status_lst"/>
				<input type="hidden" name="itm_upd_timestamp_lst" value="{$itm_updated_timestamp}"/>
				<input type="hidden" name="show_sys_msg" value="true"/>
	 			<xsl:call-template name="wb_init_lab"/>
			</form>
		</body>
	</xsl:template>
	<!-- ============================================================= -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
		    <xsl:with-param name="lab_run_info">
				<xsl:choose>
					<xsl:when test="$training_type='EXAM'"><xsl:value-of select="$lab_const_exam_run"/></xsl:when>
					<xsl:otherwise><xsl:value-of select="$lab_const_run"/></xsl:otherwise>
				</xsl:choose>訊息
		    </xsl:with-param>
			<xsl:with-param name="lab_target_learner">目標學員</xsl:with-param>
			<xsl:with-param name="lab_tab_publish">在培訓目錄中發佈</xsl:with-param>
			<xsl:with-param name="lab_tab_autoenrol">自動報名</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">確定</xsl:with-param>
			<xsl:with-param name="lab_yes">是</xsl:with-param>
			<xsl:with-param name="lab_no">否</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
		    <xsl:with-param name="lab_run_info">
				<xsl:choose>
					<xsl:when test="$training_type='EXAM'"><xsl:value-of select="$lab_const_exam_run"/></xsl:when>
					<xsl:otherwise><xsl:value-of select="$lab_const_run"/></xsl:otherwise>
				</xsl:choose>信息
		    </xsl:with-param>
			<xsl:with-param name="lab_target_learner">目标学员</xsl:with-param>
			<xsl:with-param name="lab_tab_publish">在培训目录中发布</xsl:with-param>
			<xsl:with-param name="lab_tab_autoenrol">自动报名</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">确定</xsl:with-param>
			<xsl:with-param name="lab_yes">是</xsl:with-param>
			<xsl:with-param name="lab_no">否</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
		    <xsl:with-param name="lab_run_info">
				<xsl:choose>
					<xsl:when test="$training_type='EXAM'"><xsl:value-of select="$lab_const_exam_run"/></xsl:when>
					<xsl:otherwise><xsl:value-of select="$lab_const_run"/></xsl:otherwise>
				</xsl:choose> information
		    </xsl:with-param>
			<xsl:with-param name="lab_target_learner">Target learner</xsl:with-param>
			<xsl:with-param name="lab_tab_publish">Publish to catalog</xsl:with-param>
			<xsl:with-param name="lab_tab_autoenrol">Auto enrollment</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
			<xsl:with-param name="lab_yes">Yes</xsl:with-param>
			<xsl:with-param name="lab_no">No</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
	    <xsl:param name="lab_run_info"/>
		<xsl:param name="lab_target_learner"/>
		<xsl:param name="lab_tab_publish"/>
		<xsl:param name="lab_tab_autoenrol"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:param name="lab_g_form_btn_ok"/>
		<xsl:param name="lab_yes"/>
		<xsl:param name="lab_no"/>
		
		<xsl:variable name="allow_publish" ><xsl:choose>
		 	<xsl:when test="$itm_type = 'SELFSTUDY' and ($rmdOnlineContent = 'true' or $rmdCompletionCriteriaSettings = 'true')">false</xsl:when>
		 	<xsl:when test="$itm_type = 'INTEGRATED' and $rmdCoursePackage = 'true'">false</xsl:when>
		 	<xsl:when test="$itm_type = 'AUDIOVIDEO' and $rmdOnlineContent = 'true'">false</xsl:when>
			<xsl:when test="$itm_type = 'CLASSROOM' and  $create_run_ind ='true' and $hadPublishClass != 'true'">false</xsl:when>
			<xsl:when test="$itm_type = 'CLASSROOM' and  $run_ind ='true' and $rmdCompletionCriteriaSettings = 'true'">false</xsl:when>
			<xsl:otherwise>true</xsl:otherwise>
	    </xsl:choose></xsl:variable>
		
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module" select="$belong_module"></xsl:with-param>
			<xsl:with-param name="parent_code" select="$parent_code"/>
		</xsl:call-template>
	   
        <div class="wzb-item-main">
			<xsl:call-template name="wb_ui_title">
				<xsl:with-param name="text">
					<xsl:value-of select="//itm_action_nav/@itm_title"/>
				</xsl:with-param>
			</xsl:call-template>
		
			<xsl:call-template name="itm_action_nav">
		    	<xsl:with-param name="view_mode">simple</xsl:with-param>
				<xsl:with-param name="is_add">false</xsl:with-param>
				<xsl:with-param  name="cur_node_id">publish</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_ui_nav_link">       
				<xsl:with-param name="text">
					<xsl:choose>
						<xsl:when test="../nav/item/@run_ind = 'false'">
							<xsl:choose>
								<xsl:when test="$current_role='INSTR_1'">
									<a href="javascript:itm_lst.get_itm_instr_view({$itm_id})" class="NavLink">
										<xsl:value-of select="//item/title"/>
									</a>
								</xsl:when>
								<xsl:otherwise>
									<a href="javascript:itm_lst.get_item_detail({$itm_id})" class="NavLink">
										<xsl:value-of select="//item/title"/>
									</a>
								</xsl:otherwise>
							</xsl:choose>
							<span class="NavLink">&#160;&gt;&#160;<xsl:value-of select="$lab_target_learner"/>
							</span>
						</xsl:when>
						<xsl:otherwise>
							<xsl:apply-templates select="//nav/item" mode="nav">
								<xsl:with-param name="lab_run_info" select="$lab_run_info"/>
								<xsl:with-param name="lab_session_info" select="$lab_run_info"/>
							</xsl:apply-templates> 
							<span class="NavLink">
								<xsl:text>&#160;&gt;&#160;</xsl:text>
								<xsl:value-of select="$label_core_training_management_256"/>
							</span>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:with-param>
			</xsl:call-template>
		
			<xsl:call-template name="wb_ui_desc">
				<xsl:with-param name="text">
				<span class="wzb-ui-warn-text"><xsl:value-of select="$label_core_training_management_317"/>：<br/></span>
				<xsl:copy-of select="$label_core_training_management_319"/></xsl:with-param>
			</xsl:call-template>
	
			<table>
				<xsl:choose>
					<xsl:when test="$ref_ind = 'true'">
		 				<input type="hidden" name="itm_mobile_ind" value="yes"/>
		 				<input value="ALL" type="hidden" name="itm_status" />
		 			</xsl:when>
		 			<xsl:otherwise>
			 			<tr>
			                <td class="wzb-form-label wzb-title-2" valign="top"><xsl:value-of select="$label_core_training_management_387"></xsl:value-of><!-- 将课程发布到 -->：</td>
			                <td class="wzb-form-control clearfix">
			                    <div class="wzb-ui-kuangjia" style="width:200px;">
			                        <div class="wzb-ui-bgc"><i class="tool-pc-icon"></i></div>
			                        <label>
			                        	<input id="itm_mobile_ind2" name="itm_mobile_ind" type="radio" value="no">
											<xsl:if test="$itm_mobile_ind = 'no'"><xsl:attribute name="checked">checked</xsl:attribute></xsl:if>
										</input>
			                        	<!-- 方式一 --><xsl:value-of select="$label_core_training_management_388"></xsl:value-of>
			                        </label>
			                    </div>
			                    <div class="wzb-ui-kuangjia clearfix">
			                        <div style="float:left;"><div class="wzb-ui-bgc"><i class="tool-pc-icon"></i></div></div>
			                        <div class="wzb-ui-width"><i class="tool-width-icon"></i></div>
			                        <div style="margin-left:145px;"><div class="wzb-ui-bgc"><i class="tool-phone-icon"></i></div></div>
			                        <label>
			                        	<input id="itm_mobile_ind1" name="itm_mobile_ind" type="radio" value="yes">
											<xsl:if test="$itm_mobile_ind = 'yes' or $itm_mobile_ind = ''"><xsl:attribute name="checked">checked</xsl:attribute></xsl:if>
										</input>
			                        	<!-- 方式二 --><xsl:value-of select="$label_core_training_management_389"></xsl:value-of>
			                        </label>
			                        <p class="color-gray999" style="margin:0 0 0 20px;"><!-- 发布前请确认课程内容可以在移动端正常学习 --><xsl:value-of select="$label_core_training_management_390"></xsl:value-of></p>
			                    </div>
			                </td>
			            </tr>

			            <tr>
			                <td class="wzb-form-label wzb-title-2" valign="top"><xsl:value-of select="$label_core_training_management_342"/></td>
			                <td class="wzb-form-control clearfix">
			                    <div class="wzb-ui-kuangjia" style="width:200px;">
			                        <div class="wzb-ui-bgc" style="border-radius:50%;"><i class="tool-all-icon"></i></div>
			                        <label for="item_status1id">
			                        	<input id="item_status1id" name="item_status_" type="radio" checked="checked" value="ALL">
										<xsl:if test="$itm_target_status = 'ALL'"><xsl:attribute name="checked">checked</xsl:attribute></xsl:if>
										</input>
			                        	<!-- 所有学员 --><xsl:value-of select="$lab_by_all_user"></xsl:value-of>
			                        </label>
			                    </div>
			                    <div class="wzb-ui-kuangjia clearfix">
			                        <div><div class="wzb-ui-bgc" style="border-radius:50%;"><i class="tool-target-icon"></i></div></div>
			                        <xsl:if test="$itm_type != 'AUDIOVIDEO' ">
										<label for="item_status0id">
											<input id="item_status0id" name="item_status_" type="radio" value="TARGET_LEARNER">
												<xsl:if test="$itm_target_status = 'TARGET_LEARNER'"><xsl:attribute name="checked">checked</xsl:attribute></xsl:if> 
											</input>
											<!-- 目标学员 --><xsl:value-of select="$lab_target_learner"></xsl:value-of>
										 </label>
									</xsl:if>
			                    </div>
			                </td>
			                <input id="item_status" name="itm_status" type="hidden"  />
			            </tr>
			             <tr>
							<td colspan="2">
								
								 	<span class = "wzb-ui-warn-text">
										 <xsl:choose>
										 	<xsl:when test="$itm_type = 'SELFSTUDY' ">
										 		<xsl:if test="$rmdOnlineContent = 'true'"><xsl:value-of select="$label_core_training_management_333"/></xsl:if>
										 		<xsl:if test="$rmdCompletionCriteriaSettings = 'true'"><br/><xsl:value-of select="$label_core_training_management_334"/></xsl:if>
										 	</xsl:when>
										 	<xsl:when test="$itm_type = 'INTEGRATED' ">
										 		<xsl:if test="$rmdCoursePackage = 'true'"><xsl:value-of select="$label_core_training_management_335"/></xsl:if>
										 	</xsl:when>
										 	<xsl:when test="$itm_type = 'AUDIOVIDEO' ">
										 		<xsl:if test="$rmdOnlineContent = 'true'"><xsl:value-of select="$label_core_training_management_333"/></xsl:if>
										 	</xsl:when>
											<xsl:when test="$itm_type = 'CLASSROOM' and  $create_run_ind ='true' and ($content_def = 'PARENT' or $content_def = '' ) and $itm_exam_ind='true'">
												<xsl:if test="$hadPublishClass != 'true' "><xsl:value-of select="$label_core_training_management_336"/></xsl:if>
												<xsl:if test="$rmdCompletionCriteriaSettings = 'true' "><br/><xsl:value-of select="$label_core_training_management_334"/></xsl:if>
											</xsl:when>
											
											<xsl:when test="$itm_type = 'CLASSROOM' and  $create_run_ind ='true' and ($content_def = 'PARENT' or $content_def = '') and $itm_exam_ind='false'">
												<xsl:if test="$hadPublishClass != 'true' "><xsl:value-of select="$label_core_training_management_337"/></xsl:if>
												<xsl:if test="$rmdCompletionCriteriaSettings = 'true'"><br/><xsl:value-of select="$label_core_training_management_334"/></xsl:if>
											</xsl:when>
											<xsl:when test="$itm_type = 'CLASSROOM' and  $run_ind ='true'">
												<xsl:if test="$rmdCompletionCriteriaSettings = 'true'"><xsl:value-of select="$label_core_training_management_334"/></xsl:if>
												
											</xsl:when>
										</xsl:choose>
									</span>
							
							</td>
						</tr>
			            
		 			</xsl:otherwise>
	 			</xsl:choose>
				
			</table>
		
			<div class="wzb-bar">
		  		<xsl:call-template name="wb_gen_form_button">
					<xsl:with-param name="wb_gen_btn_name"><!-- 马上发布课程 --><xsl:value-of select="$label_core_training_management_391"></xsl:value-of></xsl:with-param>
					<xsl:with-param name="class">btn wzb-btn-blue margin-right10 wzb-btn-big</xsl:with-param>
					<xsl:with-param name="wb_gen_btn_href">javascript:itm.upd_item_target_ref(document.frmResult,'item_status', 'false','$run_ind');</xsl:with-param>
				</xsl:call-template>
				
				<xsl:call-template name="wb_gen_form_button">
					<xsl:with-param name="wb_gen_btn_name"><!-- 暂不发布课程 --><xsl:value-of select="$label_core_training_management_392"></xsl:value-of></xsl:with-param>
					<xsl:with-param name="class">btn wzb-btn-blue margin-right10 wzb-btn-big</xsl:with-param>
					<xsl:with-param name="wb_gen_btn_href">javascript:cancel_publish()</xsl:with-param>
				</xsl:call-template>
			</div>
		</div>
		<xsl:call-template name="wb_ui_footer"/>
	</xsl:template>
<!-- ============================================================= -->
	
<!-- =============================================================== -->
	<xsl:template match="item" mode="nav">
		<xsl:param name="lab_run_info"/>
		<xsl:param name="lab_session_info"/>
		<xsl:variable name="_count" select="count(preceding-sibling::item)"/>
		<xsl:choose>
			<xsl:when test="@run_ind = 'true'">
				<xsl:text>&#160;&gt;&#160;</xsl:text>
				<xsl:variable name="value">
					<xsl:for-each select="preceding-sibling::item">
						<xsl:if test="position()=$_count">
							<xsl:value-of select="@id"/>
						</xsl:if>
					</xsl:for-each>
				</xsl:variable>
				<a href="javascript:itm_lst.get_item_run_list({$value})" class="NavLink">
					<xsl:choose>
						<xsl:when test="$itm_exam_ind = 'true'"><xsl:value-of select="$lab_const_exam_manage"/></xsl:when>
						<xsl:otherwise><xsl:value-of select="$lab_const_cls_manage"/></xsl:otherwise>
					</xsl:choose>
				</a>
				<xsl:text>&#160;&gt;&#160;</xsl:text>
				<a href="javascript:itm_lst.get_item_run_detail({@id})" class="NavLink">
					<xsl:value-of select="title"/>
				</a>
			</xsl:when>
			<xsl:when test="@session_ind = 'true'">
				<xsl:text>&#160;&gt;&#160;</xsl:text>
				<xsl:variable name="value">
					<xsl:for-each select="preceding-sibling::item">
						<xsl:if test="position()=$_count">
							<xsl:value-of select="@id"/>
						</xsl:if>
					</xsl:for-each>
				</xsl:variable>
				<a href="javascript:itm_lst.session.get_session_list({$value})" class="NavLink">
					<xsl:value-of select="$lab_session_info"/>
				</a>
				<xsl:text>&#160;&gt;&#160;</xsl:text>
				<a href="javascript:itm_lst.get_item_run_detail({@id})" class="NavLink">
					<xsl:value-of select="title"/>
				</a>
			</xsl:when>
			<xsl:otherwise>
				<a href="javascript:itm_lst.get_item_detail({@id})" class="NavLink">
					<xsl:value-of select="title"/>
				</a>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>
