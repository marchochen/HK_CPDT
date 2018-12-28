<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_sub_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_goldenman.xsl"/>
	<!-- itm utils -->
	<xsl:import href="share/label_lrn_soln.xsl"/>
	<xsl:import href="utils/display_form_input_time.xsl"/>
	<xsl:strip-space elements="*"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:variable name="ent_id" select="/applyeasy/meta/cur_usr/@ent_id"/>
	<xsl:variable name="itm_id" select="/applyeasy/item/@id"/>
	<xsl:variable name="itm_updated_timestamp" select="/applyeasy/item/last_updated/@timestamp"/>
	<xsl:variable name="itm_type_lst" select="/applyeasy/item/item_type/@id"/>
	<xsl:variable name="run_ind" select="/applyeasy/item/@run_ind"/>
	<xsl:variable name="itm_blend_ind" select="/applyeasy/item/item_type_meta/@blend_ind"/>
	<xsl:variable name="itm_exam_ind" select="/applyeasy/itm_exam_ind"/>
	<xsl:variable name="itm_ref_ind" select="/applyeasy/item/item_type_meta/@ref_ind"/>	
	<xsl:variable name="itm_dummy_type" select="/applyeasy/item/item_type/@dummy_type"/>
	<xsl:variable name="session_ind" select="/applyeasy/item/@session_ind"/>
	<xsl:variable name="error_code" select="/applyeasy/meta/error_message/code/text()"/>
	<xsl:variable name="error_msg" select="/applyeasy/meta/error_message/content"/>
	<xsl:variable name="tc_enabled" select="/applyeasy/meta/tc_enabled"/>
	<xsl:variable name="training_type" select="/applyeasy/training_type"/>
	<xsl:variable name="lab_LN022" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'LN022')"/>
	<xsl:variable name="lab_LN024" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'LN024')"/>
	<xsl:variable name="label_core_training_management_356" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_356')" />
	<xsl:variable name="parent_code">
		<xsl:choose>
			<xsl:when test="$itm_exam_ind='true'">FTN_AMD_EXAM_MGT</xsl:when>
			<xsl:otherwise>FTN_AMD_ITM_COS_MAIN</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<xsl:apply-templates select="applyeasy"/>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="applyeasy">
		<html> 
			<xsl:call-template name="main"/>
		</html>
	</xsl:template>
	<!-- ============================================================= -->
	<xsl:template name="main">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
			<TITLE>
				<xsl:value-of select="$wb_wizbank"/>
			</TITLE>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>  
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_item.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_goldenman.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[		
					itm_lst = new wbItem;
					goldenman = new wbGoldenMan;
					
					function show(frm){
						if(frm.itm_bonus_ind_radio[0].checked){
						   document.frmXml.itm_diff_factor.disabled = false;
						   
					        }
					        if(frm.itm_bonus_ind_radio[1].checked){
					          document.frmXml.itm_diff_factor.disabled = true;
						  document.frmXml.itm_diff_factor.value=1;
					        }
						
					}

					function course_change(frm,obj){
					if(obj.value == 1){
						var pos =true;
						var neg = false;
					}else if (obj.value == 0){
						var pos = false;
						var neg = true;
					}else{
						var pos = true;
						var neg = true;					
					}
					if(frm.tnd_ids.type == 'select-multiple'){
						//frm.tnd_ids.disabled = neg;
						if(frm.genadditm_id_lst){
							//frm.genadditm_id_lst.disabled = pos;
						}
						if(frm.genremoveitm_id_lst){
							//frm.genremoveitm_id_lst.disabled = pos;
						}		
						if(neg == true){
							frm.tnd_ids.options.length = 0
						}
					}
					if(frm.itm_id_lsts.type == 'select-multiple'){
					//genremovetnd_ids
						//frm.itm_id_lsts.disabled = pos;
						if(frm.genaddtnd_ids){
							//frm.genaddtnd_ids.disabled = neg;
						}
						if(frm.genaddtnd_ids){
							//frm.genremovetnd_ids.disabled = neg;
						}		
						if(pos == true){
							frm.itm_id_lsts.options.length = 0
						}
					}
				}
				
				function course_button_change(frm,index){
				  if(index == 1){
				            frm.iscatalog_ind[0].checked=true; 
							frm.tnd_ids.options.length = 0
				  }else if(index == 2){
						    frm.iscatalog_ind[1].checked=true; 
						    frm.itm_id_lsts.options.length = 0
				  }else if(index == 3){
				       frm.iscatalog_change_ind[0].checked=true;
				  }
				  
				}
				
				function change_catalog(frm,obj){
					var pos =true;
					if(obj.value == 1){
						pos =false;
					}
					if(frm.tnd_ids_change.type == 'select-multiple'){
						//frm.tnd_ids_change.disabled = pos;
						if(frm.genaddtnd_ids_change){
							//frm.genaddtnd_ids_change.disabled = pos;
						}
						if(frm.genremovetnd_ids_change){
							//frm.genremovetnd_ids_change.disabled = pos;
						}	
						if(pos == true){
							frm.tnd_ids_change.options.length = 0
						}
						
						
					}
				}
				
				function change_itm_access_type(frm,obj){
					frm.itm_access_type.value = obj.value;
					if(obj.value == 'KEEP'){
						frm.itm_status.value='';
					}else if(obj.value == 'OFF'){
						frm.itm_status.value='OFF';
					}else{
						frm.itm_status.value='ON';
					}
				}
					
		        ]]></SCRIPT>
			<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript">
			     function  initShow(frm ){
				frm.itm_appn_start_datetime_hour.value='00';
				frm.itm_appn_start_datetime_min.value='00';
				frm.itm_appn_end_datetime_temp_hour.value='23';
				frm.itm_appn_end_datetime_temp_min.value='59';		
				//document.frmXml.itm_diff_factor.disabled = true;
		        document.frmXml.itm_diff_factor.value=1;
		        //frm.tnd_ids.disabled = true;
				//frm.genaddtnd_ids.disabled = true;
				//frm.genremovetnd_ids.disabled = true; 
                if(frm.tnd_ids_change.type == 'select-multiple'){
						//frm.tnd_ids_change.disabled = true;
						if(frm.genaddtnd_ids_change){
							//frm.genaddtnd_ids_change.disabled = true;
						}
						if(frm.genremovetnd_ids_change){
							//frm.genremovetnd_ids_change.disabled = true;
						}	
						if(pos == true){
							frm.tnd_ids_change.options.length = 0
						}
					}
			      }
			      function	controlShow(frm){
					frm.itm_content_eff_end_datetime_radio[2].checked=true;
			      }
			</SCRIPT>
		</head>
		<BODY leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onload="initShow(document.frmXml)">
			<FORM name="frmXml" method="post" enctype="multipart/form-data">
				<input type="hidden" name="rename" value="no" />
				<input type="hidden" name="cmd" />
				<input type="hidden" name="itm_xml" />
				<input type="hidden" name="url_failure" />
				<input type="hidden" name="tvw_id" />
				<input type="hidden" name="url_success" />
				<input type="hidden" name="itm_bonus_ind" value="" />
				<input type="hidden" name="itm_retake_ind" value="" />
				<input type="hidden" name="itm_appn_start_datetime" value="" />
				<input type="hidden" name="itm_appn_end_datetime" value="" />
				<input type="hidden" name="itm_id_lst" value="" />
				<input type="hidden" name="alert_msg" value="{$label_core_training_management_356}" />
				<input type="hidden" name="update_success_msg" value="{$lab_LN024}" />
				<input type="hidden" name="tnd_id_lst" value="" />
				<input type="hidden" name="tnd_ids_change_lst" value="" />
				<input type="hidden" name="iscatalog" value="" />
				<input type="hidden" name="itm_status" value="" />
				<input type="hidden" name="itm_access_type" id="itm_access_type" value="" />
				<xsl:call-template name="wb_init_lab" />
			</FORM>
		</BODY>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:apply-templates select="/applyeasy/item">
			 <xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">確定</xsl:with-param>
			<xsl:with-param name="lab_no_limit">不限</xsl:with-param>
			<xsl:with-param name="lab_itm_appn_datetime">報名期限：</xsl:with-param>
			<xsl:with-param name="lab_itm_content_eff_datetime">網上內容期限：</xsl:with-param>
			<xsl:with-param name="lab_itm_content_eff_start_datetime">學員被成功報名</xsl:with-param>
			<xsl:with-param name="lab_itm_content_eff_duration">天 (從學員被成功報名後開始計算)</xsl:with-param>
			<xsl:with-param name="lab_itm_retake_ind">重讀：</xsl:with-param>
			<xsl:with-param name="lab_itm_bonus_ind">自動積分：</xsl:with-param>
			<xsl:with-param name="lab_itm_diff_factor">難度系數：</xsl:with-param>
			<xsl:with-param name="lab_from">由</xsl:with-param>
			<xsl:with-param name="lab_to">至</xsl:with-param>
			<xsl:with-param name="lab_itm_bonus_ind_yes">允許</xsl:with-param>
			<xsl:with-param name="lab_itm_bonus_ind_no">禁止</xsl:with-param>
			<xsl:with-param name="lab_left">(</xsl:with-param>
			<xsl:with-param name="lab_right">)</xsl:with-param>
			<xsl:with-param name="lab_yes">是</xsl:with-param>
			<xsl:with-param name="lab_no">否</xsl:with-param>
          	<xsl:with-param name="lab_title">批量修改</xsl:with-param>
			<xsl:with-param name="lab_title_desc">可批量修改您選擇的網上課程、網上考試信息</xsl:with-param>
			<xsl:with-param name="lab_itm_cos">選擇網上課程、考試：</xsl:with-param>
			<xsl:with-param name="lab_itm_cos_tip">註：如“報名期限”和“網上內容期限”在批量修改中不作修改，則所選課程中的這兩條記錄會保存原有設定</xsl:with-param>
			<xsl:with-param name="lab_no_update">不變</xsl:with-param>
			<xsl:with-param name="lab_itm_cat">選擇指定目錄：</xsl:with-param>
			<xsl:with-param name="lab_itm_cat_change">課程目錄更改到：</xsl:with-param>
			<xsl:with-param name="lab_itm_pub_change">課程發佈狀態：</xsl:with-param>
			<xsl:with-param name="lab_itm_pub_desc">								
							<![CDATA[
								請注意：<br/>
								在這裡批量發佈課程時，系統不會自動幫你檢查課程相關必要信息是否設置好，例如以下信息：<br/>
								1、網上學習內容是否已發佈<br/>
								2、課程結訓條件<br/>
								3、目標學員規則<br/>
								否則，可能會導致以下問題：<br/>
								1、沒有發佈網上學習內容，學員無法開始學習該課程。<br/>
								2、沒有設置結訓條件，學員無法結訓該課程。<br/>
								3、如果只發佈給目標學員，但沒有設置課程的目標學員，將沒有學員可以看到該課程。
							]]>
			</xsl:with-param>
			<xsl:with-param name="lab_itm_pub_cancel">取消發佈</xsl:with-param>
			<xsl:with-param name="lab_itm_pub_target">只發佈給目標學員</xsl:with-param>
			<xsl:with-param name="lab_itm_pub_all">發佈給所有學員</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:apply-templates select="/applyeasy/item">
		    <xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">确定</xsl:with-param>
			<xsl:with-param name="lab_no_limit">不限</xsl:with-param>
			<xsl:with-param name="lab_itm_appn_datetime">报名期限：</xsl:with-param>
			<xsl:with-param name="lab_itm_content_eff_datetime">网上内容期限：</xsl:with-param>
			<xsl:with-param name="lab_itm_content_eff_start_datetime">学员被成功报名</xsl:with-param>
			<xsl:with-param name="lab_itm_content_eff_duration">天 (从学员被成功报名后开始计算)</xsl:with-param>
			<xsl:with-param name="lab_itm_retake_ind">重读：</xsl:with-param>
			<xsl:with-param name="lab_itm_bonus_ind">自动积分：</xsl:with-param>
			<xsl:with-param name="lab_itm_diff_factor">难度系数：</xsl:with-param>
			<xsl:with-param name="lab_from">由</xsl:with-param>
			<xsl:with-param name="lab_to">至</xsl:with-param>
			<xsl:with-param name="lab_itm_bonus_ind_yes">允许</xsl:with-param>
			<xsl:with-param name="lab_itm_bonus_ind_no">禁止</xsl:with-param>
			<xsl:with-param name="lab_left">(</xsl:with-param>
			<xsl:with-param name="lab_right">)</xsl:with-param>
			<xsl:with-param name="lab_yes">是</xsl:with-param>
			<xsl:with-param name="lab_no">否</xsl:with-param>
            <xsl:with-param name="lab_title">批量修改</xsl:with-param>
			<xsl:with-param name="lab_title_desc">可批量修改您选择的网上课程、网上考试信息</xsl:with-param>
			<xsl:with-param name="lab_itm_cos">选择指定网上课程、考试：</xsl:with-param>
			<xsl:with-param name="lab_itm_cos_tip">注：如“报名期限”和“网上内容期限”在批量修改中不做调整，则所选的课程中这两条记录保存原来的设置</xsl:with-param>
			<xsl:with-param name="lab_no_update">不做修改</xsl:with-param>
			<xsl:with-param name="lab_itm_cat">选择指定目录：</xsl:with-param>
			<xsl:with-param name="lab_itm_cat_change">课程目录更改到：</xsl:with-param>
			<xsl:with-param name="lab_itm_pub_change">课程发布状态：</xsl:with-param>
			<xsl:with-param name="lab_itm_pub_desc">								
							<![CDATA[
								请注意：<br/>
								在这里批量发布课程时，系统不会自动帮你检查课程相关必要信息是否设置好，例如以下信息：<br/>
								1、网上学习内容是否已发布<br/>
								2、课程结训条件<br/>
								3、目标学员规则<br/>
								否则，可能会导致以下问题：<br/>
								1、没有发布网上学习内容，学员无法开始学该课程。<br/>
								2、没有设置结训条件，学员无法结训该课程。<br/>
								3、如果只发布给目标学员，但没有设置课程的目标学员，将没有学员可以看到该课程。
							]]>
			</xsl:with-param>
			<xsl:with-param name="lab_itm_pub_cancel">取消发布</xsl:with-param>
			<xsl:with-param name="lab_itm_pub_target">只发布给目标学员</xsl:with-param>
			<xsl:with-param name="lab_itm_pub_all">发布给所有学员</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:apply-templates select="/applyeasy/item">
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
			<xsl:with-param name="lab_no_limit">Unlimited</xsl:with-param>
			<xsl:with-param name="lab_itm_appn_datetime">Enrollment deadline：</xsl:with-param>
			<xsl:with-param name="lab_itm_content_eff_datetime">Online content period：</xsl:with-param>
			<xsl:with-param name="lab_itm_content_eff_start_datetime">Once learners were enrolled successfully</xsl:with-param>
			<xsl:with-param name="lab_itm_content_eff_duration">Day (Start counting after successful learner enrollment)</xsl:with-param>
			<xsl:with-param name="lab_itm_retake_ind">Retake：</xsl:with-param>
			<xsl:with-param name="lab_itm_bonus_ind">Auto point increment：</xsl:with-param>
			<xsl:with-param name="lab_itm_diff_factor">Difficulty coefficient：</xsl:with-param>
			<xsl:with-param name="lab_from">From</xsl:with-param>
			<xsl:with-param name="lab_to">To</xsl:with-param>
			<xsl:with-param name="lab_itm_bonus_ind_yes">Allow</xsl:with-param>
			<xsl:with-param name="lab_itm_bonus_ind_no">prohibit</xsl:with-param>
			<xsl:with-param name="lab_left">(</xsl:with-param>
			<xsl:with-param name="lab_right">)</xsl:with-param>
			<xsl:with-param name="lab_yes">Yes</xsl:with-param>
			<xsl:with-param name="lab_no">No</xsl:with-param>
			<xsl:with-param name="lab_title">Batch editing</xsl:with-param>
			<xsl:with-param name="lab_title_desc">Batch editable online course and exam information</xsl:with-param>
			<xsl:with-param name="lab_itm_cos">Select assigned online course or exam：</xsl:with-param>
			<xsl:with-param name="lab_itm_cos_tip">Remarks: If no batch editing is made for ‘enrollment deadline’ and ‘online content period’, the records of these 2 items will remain unchanged in the selected courses. </xsl:with-param>
			<xsl:with-param name="lab_no_update">Remain unchanged</xsl:with-param>
			<xsl:with-param name="lab_itm_cat">Select assigned catalog：</xsl:with-param>
			<xsl:with-param name="lab_itm_cat_change">Catalog change to：</xsl:with-param>
			<xsl:with-param name="lab_itm_pub_change">Course publish status：</xsl:with-param>
			<xsl:with-param name="lab_itm_pub_desc">								
							<![CDATA[
								Please note:<br/>
								When you release the course, the system will not automatically check the course information setting for you, such as the following:<br/>
								1, whether online learning content is published<br/>
								2, course completion criteria<br/>
								3, rules for target students<br/>
								it may cause the following problems:<br/>
								1, did not publish web-based content, students cannot study the course.<br/>
								2, did not set completion criteria, students cannot complete the course.<br/>
								3, if you release to target students without setting the target course participants, no students can see the course.
							]]>
			</xsl:with-param>
			<xsl:with-param name="lab_itm_pub_cancel">Cancel publish</xsl:with-param>
			<xsl:with-param name="lab_itm_pub_target">Only to target learner</xsl:with-param>
			<xsl:with-param name="lab_itm_pub_all">To all</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="/applyeasy/item">
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:param name="lab_g_form_btn_ok"/>
		<xsl:param name="lab_no_limit"/>
		<xsl:param name="lab_itm_appn_datetime"/>
		<xsl:param name="lab_itm_content_eff_datetime"/>
		<xsl:param name="lab_itm_content_eff_start_datetime"/>
		<xsl:param name="lab_itm_content_eff_duration"/>
		<xsl:param name="lab_itm_retake_ind"/>
		<xsl:param name="lab_itm_bonus_ind"/>
		<xsl:param name="lab_itm_diff_factor"/>
		<xsl:param name="lab_from"/>
		<xsl:param name="lab_to"/>
		<xsl:param name="lab_itm_bonus_ind_yes"/>
		<xsl:param name="lab_itm_bonus_ind_no"/>
		<xsl:param name="lab_left"/>
		<xsl:param name="lab_right"/>
		<xsl:param name="lab_yes"/>
		<xsl:param name="lab_no"/>
		 <xsl:param name="lab_title"/>
		<xsl:param name="lab_title_desc"/>
		<xsl:param name="lab_itm_cos"/>
		<xsl:param name="lab_itm_cos_tip"/>
		<xsl:param name="lab_no_update"/> 
		<xsl:param name="lab_itm_cat_change"/> 
		<xsl:param name="lab_itm_cat"/>
		<xsl:param name="lab_itm_pub_change"/> 
		<xsl:param name="lab_itm_pub_desc"></xsl:param>
		<xsl:param name="lab_itm_pub_cancel"></xsl:param>
		<xsl:param name="lab_itm_pub_target"></xsl:param>
		<xsl:param name="lab_itm_pub_all"></xsl:param>
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_ITM_COS_MAIN</xsl:with-param>
			<xsl:with-param name="parent_code" select="$parent_code"/>
		</xsl:call-template>		
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
			<xsl:value-of select="$lab_title"/>
			</xsl:with-param>
		</xsl:call-template>
		<table>
		       <tr>	
			       <td> <xsl:value-of select="$lab_title_desc"/>
			       </td>
		         </tr>
		</table>
		<table >
			<tr>
				<td  class="wzb-form-label" valign="top" >
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_itm_cos"/>	
				</td>	
				<td class="wzb-form-control">	
					<table>
						<tr>
							<td width="3%" align="center" valign="top"><input  name="iscatalog_ind" style="margin:0px;padding:0px;" type="radio" value="0" checked="true"  onclick="course_change(document.frmXml,this);"/></td>
							<td width="97%" align="left">
								<xsl:call-template name="wb_goldenman">
									<xsl:with-param name="field_name">itm_id_lsts</xsl:with-param>
									<xsl:with-param name="name">itm_id_lst</xsl:with-param>
									<xsl:with-param name="box_size">4</xsl:with-param>
									<xsl:with-param name="select_type">3</xsl:with-param>
									<xsl:with-param name="args_type">row</xsl:with-param>
									<xsl:with-param name="complusory_tree">0</xsl:with-param>
									<xsl:with-param name="custom_js_code_extra">args = goldenman.set_global_catalog_label(args,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
									<xsl:with-param name="pick_root">0</xsl:with-param>
									<xsl:with-param name="show_box">true</xsl:with-param>
									<xsl:with-param name="remove_btn">true</xsl:with-param>
									<xsl:with-param name="tree_type">TC_CATALOG_ITEM_BATCHUPDATE</xsl:with-param>
									<xsl:with-param name="extra_add_function">course_button_change(document.frmXml,1);</xsl:with-param>
								</xsl:call-template>
							</td>
					
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td  class="wzb-form-label" valign="top">
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_itm_cat"/>						
				</td>	
				<td class="wzb-form-control">
					<table>
						<tr>
							<td  width="3%" align="center" valign="top"><input  name="iscatalog_ind" type="radio" style="margin:0px;padding:0px;" value="1" onclick="course_change(document.frmXml,this);"/></td>
							<td width="97%" align="left">
								<xsl:call-template name="wb_goldenman">
									<xsl:with-param name="field_name">tnd_ids</xsl:with-param>
									<xsl:with-param name="name">tnd_ids</xsl:with-param>
									<xsl:with-param name="box_size">4</xsl:with-param>
									<xsl:with-param name="select_type">1</xsl:with-param>
									<xsl:with-param name="args_type">row</xsl:with-param>
									<xsl:with-param name="complusory_tree">0</xsl:with-param>
									<xsl:with-param name="custom_js_code_extra">args = goldenman.set_global_catalog_label(args,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
									<xsl:with-param name="pick_root">0</xsl:with-param>
									<xsl:with-param name="show_box">true</xsl:with-param>
									<xsl:with-param name="remove_btn">true</xsl:with-param>
									<xsl:with-param name="tree_type">TC_CATALOG</xsl:with-param>
									<xsl:with-param name="extra_add_function">course_button_change(document.frmXml,2);</xsl:with-param>
								</xsl:call-template>
							</td>
						</tr>
					</table>
				</td>
				<td >									
				</td>
				<td>									
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_itm_appn_datetime"/>
				</td>
				<td class="wzb-form-control">
					 <xsl:value-of select="$lab_from"/>：&#160;&#160;<xsl:call-template name="display_form_input_time">
						<xsl:with-param name="fld_name">itm_appn_start_datetime</xsl:with-param>
						<xsl:with-param name="hidden_fld_name">itm_appn_start_datetime </xsl:with-param>
						<xsl:with-param name="show_label">Y</xsl:with-param>
						<xsl:with-param name="format">2</xsl:with-param>
						<xsl:with-param name="display_form_input_hhmm">Y</xsl:with-param>
					</xsl:call-template><br/>
					
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label" >
					
				</td>
				<td class="wzb-form-control">
					<table>
						<tr>
							<td valign="top" width="2%">
								<xsl:value-of select="$lab_to"/>
								<xsl:text>：</xsl:text>
							</td>
							<td valign="top" width="98%">
								<input size="30" name="itm_appn_end_datetime_radio" type="radio" value="0"/>&#160; <xsl:value-of select="$lab_no_limit"/>
							</td>
						</tr>
						<tr>
							<td></td>
							<td>
								<input size="30" name="itm_appn_end_datetime_radio" type="radio" value="1" />&#160;
								<xsl:call-template name="display_form_input_time">
									<xsl:with-param name="fld_name">itm_appn_end_datetime_temp</xsl:with-param>
									<xsl:with-param name="hidden_fld_name">itm_appn_end_datetime_temp</xsl:with-param>
									<xsl:with-param name="show_label">Y</xsl:with-param>
									<xsl:with-param name="format">2</xsl:with-param>
									<xsl:with-param name="display_form_input_hhmm">Y</xsl:with-param>
									<xsl:with-param name="focus_rad_btn_name">itm_appn_end_datetime_radio[1]</xsl:with-param>
								</xsl:call-template>
							</td>
						</tr>
						<tr>
							<td></td>
							<td>
								<input size="30" name="itm_appn_end_datetime_radio" type="radio" value="0" checked=''/>&#160; <xsl:value-of select="$lab_no_update"/>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td  class="wzb-form-label"><xsl:value-of select="$lab_itm_content_eff_datetime"/>								
				</td>
				<td class="wzb-form-control" >
					<xsl:value-of select="$lab_from "/>
					<xsl:text>：</xsl:text>
					<xsl:value-of select="$lab_itm_content_eff_start_datetime"/><br/>	
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label">							
				</td>
				<td class="wzb-form-control" >
					<table>
						<tr>
							<td>
								<xsl:value-of select="$lab_to"/>
								<xsl:text>：</xsl:text>
							</td>
							<td valign="top" width="98%">
								<input size="30" name="itm_content_eff_end_datetime_radio" type="radio" value="0" />&#160; <xsl:value-of select="$lab_no_limit"/>
							</td>
						</tr>
						<tr>
							<td></td>
							<td>
								<input size="30" name="itm_content_eff_end_datetime_radio" type="radio" value="1" />&#160;
								<xsl:call-template name="display_form_input_time">
									<xsl:with-param name="fld_name">itm_content_eff_end_datetime</xsl:with-param>
									<xsl:with-param name="hidden_fld_name">itm_content_eff_end_datetime</xsl:with-param>
									<xsl:with-param name="show_label">Y</xsl:with-param>
									<xsl:with-param name="format">2</xsl:with-param>
									<xsl:with-param name="focus_rad_btn_name">itm_content_eff_end_datetime_radio[1]</xsl:with-param>
								</xsl:call-template>
							</td>
						</tr>
						<tr>
							<td></td>
							<td style="padding:5px 0;">
								<input size="30" name="itm_content_eff_end_datetime_radio" type="radio" value="2" />&#160;
								<input size="5" name="itm_content_eff_duration" type="text" class="wzb-inputText"  value="" onClick="controlShow(document.frmXml)" />
								<xsl:value-of select="$lab_itm_content_eff_duration"/>
							</td>
						</tr>
						<tr>
							<td></td>
							<td>
								<input size="30" name="itm_content_eff_end_datetime_radio" type="radio" value="0" checked=''/>&#160; <xsl:value-of select="$lab_no_update"/>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td valign="top" class="wzb-form-label">
					<xsl:value-of select="$lab_itm_retake_ind"/>									
				</td>
				<td class="wzb-form-control">
					<table>
						<tr>
							<td><input  name="itm_retake_ind_radio" type="radio" value="" />&#160;<xsl:value-of select="$lab_itm_bonus_ind_yes"/><br/></td>
						</tr>
						<tr>
							<td><input  name="itm_retake_ind_radio" type="radio" value="" />&#160;<xsl:value-of select="$lab_itm_bonus_ind_no"/></td>
						</tr>
						<tr>
							<td><input  name="itm_retake_ind_radio" type="radio" value="" checked=''/>&#160;<xsl:value-of select="$lab_no_update"/></td>
						</tr>
					</table>
				
				</td>
			</tr>

			<tr>
				<td valign="top" class="wzb-form-label">
					<xsl:value-of select="$lab_itm_bonus_ind"/>									
				</td>
				<td class="wzb-form-control">
					<table>
						<tr>
							<td><input  name="itm_bonus_ind_radio" type="radio" value="" onClick="show(document.frmXml)"/>&#160;<xsl:value-of select="$lab_yes"/><br/></td>
						</tr>
						<tr>
							<td><input  name="itm_bonus_ind_radio" type="radio" value="" checked='' onClick="show(document.frmXml)"/>&#160;<xsl:value-of select="$lab_no"/></td>
						</tr>
						<tr>
							<td><input  name="itm_bonus_ind_radio" type="radio" value="" checked='' onClick="show(document.frmXml)"/>&#160;<xsl:value-of select="$lab_no_update"/></td>
						</tr>
					</table>
				
				</td>
			</tr>
			<tr>
				<td style="padding:10px 0; text-align:right; color:#666;"><xsl:value-of select="$lab_itm_diff_factor"/>
					<input type="hidden" name="lab_itm_diff_factor" value="{$lab_itm_diff_factor}"/>							
				</td>
				<td style="padding:10px 0 10px 10px; color:#333;">
					<input size="5" name="itm_diff_factor" id="itm_diff_factor" type="text" class="wzb-inputText"  value=""/>
				</td>
			</tr>
			
			
			<tr>
				<td valign="top" class="wzb-form-label">
						<xsl:value-of select="$lab_itm_cat_change"/>									
				</td>	
				
				<td class="wzb-form-control">	
					<table>
						<tr>
							<td width="3%" align="left"  valign="top"><input  name="iscatalog_change_ind" type="radio"  value="1" onclick="change_catalog(document.frmXml,this);"/></td>
							<td width="97%">
							<xsl:call-template name="wb_goldenman">
								<xsl:with-param name="field_name">tnd_ids_change</xsl:with-param>
								<xsl:with-param name="name">tnd_ids_change</xsl:with-param>
								<xsl:with-param name="box_size">4</xsl:with-param>
								<xsl:with-param name="select_type">1</xsl:with-param>
								<xsl:with-param name="args_type">row</xsl:with-param>
								<xsl:with-param name="complusory_tree">0</xsl:with-param>
								<xsl:with-param name="custom_js_code_extra">args = goldenman.set_global_catalog_label(args,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
								<xsl:with-param name="pick_root">0</xsl:with-param>
								<xsl:with-param name="show_box">true</xsl:with-param>
								<xsl:with-param name="remove_btn">true</xsl:with-param>
								<xsl:with-param name="tree_type">TC_CATALOG</xsl:with-param>
								<xsl:with-param name="extra_add_function">course_button_change(document.frmXml,3);</xsl:with-param>
							</xsl:call-template>
							</td>
						</tr>
						<tr>
							
							<td colspan = "2"><input  name="iscatalog_change_ind" type="radio" value="" checked='checked' onclick="change_catalog(document.frmXml,this);"/><xsl:value-of select="$lab_no_update"/></td>
						</tr>
					</table>

				</td>
				
			</tr>
			
			<tr>
				<td>
		       </td>
		       <td class="wzb-ui-module-text">
		       	<xsl:value-of select="$lab_itm_cos_tip"/>
		       </td>
			</tr>
			<!-- 发布状态修改 -->
			<tr>
				<td valign="top" class="wzb-form-label">
					<xsl:value-of select="$lab_itm_pub_change"/>							
				</td>	
				
				<td class="wzb-form-control">	
					<table>
						<tr>
							<td width="30%" align="left"  valign="top">
								<input  name="itm_access_type_" type="radio"  value="KEEP" onclick="change_itm_access_type(document.frmXml,this);" checked='checked'/>
								<xsl:value-of select="$lab_no_update"/>	
							</td>
							<td class="wzb-ui-module-text" rowspan='4'>
								<xsl:value-of select="$lab_itm_pub_desc" disable-output-escaping="yes"/>
							</td>
						</tr>
						<tr>
							<td width="30%" align="left"  valign="top">
								<input  name="itm_access_type_" type="radio"  value="OFF" onclick="change_itm_access_type(document.frmXml,this);"/>
								<xsl:value-of select="$lab_itm_pub_cancel"/>	
							</td>
							<td></td>
						</tr>
						<tr>
							<td  align="left"  valign="top">
								<input  name="itm_access_type_" type="radio"  value="TARGET_LEARNER" onclick="change_itm_access_type(document.frmXml,this);"/>
								<xsl:value-of select="$lab_itm_pub_target"/>	
							</td>
							<td></td>
						</tr>
						<tr>
							<td  align="left"  valign="top">
								<input  name="itm_access_type_" type="radio"  value="ALL" onclick="change_itm_access_type(document.frmXml,this);"/>
								<xsl:value-of select="$lab_itm_pub_all"/>	
							</td>
							<td></td>
						</tr>
					</table>

				</td>
				
			</tr>
		</table>
		<div class="wzb-bar">
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_ok"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:itm_lst.upd_item_batch_exec(document.frmXml, '{$wb_lang}','<xsl:value-of select="$itm_exam_ind"/>')</xsl:with-param>
			</xsl:call-template>
			<xsl:variable name="back_ftn">
					ITM_MAIN
			</xsl:variable>
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:wb_utils_nav_go('<xsl:value-of select="$parent_code"/>', '<xsl:value-of select="$ent_id"></xsl:value-of>', '<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
				<!-- <xsl:with-param name="wb_gen_btn_href">javascript:history.back();</xsl:with-param> -->
			</xsl:call-template>
		</div>
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>
