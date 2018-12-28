<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
    <!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>

	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_nav_link.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:import href="utils/select_all_checkbox.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_goldenman.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="share/usr_gen_tab_share.xsl"/>
	<xsl:import href="share/usr_detail_label_share.xsl"/>
	<xsl:import href="share/label_role.xsl"/>
	
	<xsl:variable select="tc_module/training_center" name="tc"/>
	<xsl:variable name="admin_role" select="tc_module/training_center/role_list"/>
	<xsl:variable select="tc_module/meta/cur_usr/@root_ent_id" name="root_ent_id"/>
	<xsl:variable name="cur_rol" select="//cur_usr/role/@id"/>
	<!--for update value-->
	<xsl:variable select="$tc/@id" name="tc_id"/>
	<xsl:variable select="$tc/@code" name="tc_code"/>
	<xsl:variable select="$tc/name" name="tc_name"/>
	<xsl:variable select="$tc/last_updated/@timestamp" name="last_upd_time"/>
	<xsl:variable select="$tc/target_entity_list/entity" name="target_group"/>
	
	<!-- ================================================================== -->
	<xsl:template match="/">
		<xsl:call-template name="main"/>
	</xsl:template>
	<!--=================================================================== -->
	<xsl:template name="main">
		<html>
			<head>
				<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
				<title>
					<xsl:value-of select="$wb_wizbank"/>
				</title>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_usergroup.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_goldenman.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_tc_mgt.js"/>
				<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
				<script language="JavaScript" src="{$wb_js_path}urlparam.js"/>
				<script language="JavaScript" type="text/javascript"><![CDATA[
				    ct_mgt = new wbTcMgt;
					goldenman = new wbGoldenMan;
					usr = new wbUserGroup;
					var role_tadm = "TADM";
					var role_instr = "INSTR";
					function getPopupUsrLst(fld_name,id_lst,nm_lst, usr_argv){
						]]><xsl:for-each select="$tc/role_list/role"><![CDATA[
							if(fld_name == ']]><xsl:value-of select="@id"/><![CDATA[_usr_ent_id_lst'){]]>
								<xsl:value-of select="@id"/><![CDATA[_usr_ent_id_lst(usr_argv);
							}
						]]></xsl:for-each><![CDATA[
			        }
					
			    ]]></script>
			</head>
			<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
				<form name="frmXml">
					<input type="hidden" name="module"/>
					<input type="hidden" name="cmd"/>
					<input type="hidden" name="url_success"/>
					<input type="hidden" name="url_failure"/>
					<input type="hidden" name="url_failure1"/>
					<input type="hidden" name="stylesheet"/>
					<input type="hidden" name="usg_lst"/>
					<input type="hidden" name="parent_tcr_id"/>
					<input name="role_num" type="hidden">
						<xsl:attribute name="value">
							<xsl:value-of select="count($admin_role/role)"/>
						</xsl:attribute> 
					</input>
					<input type="hidden" name="tc_id" value="{$tc_id}"/>
					<input type="hidden" name="upd_time" value="{$last_upd_time}"/>
					<xsl:call-template name="content"/>
				</form>
				<script type="text/javascript">
				<![CDATA[
					function sleep(n) { //n表示的毫秒数
			            var start = new Date().getTime();
			            while(true){
			            	if (new Date().getTime() - start > n) 
			            	break;
			            };
			        }
			        
			       
					// 重新规定按钮的click事件 
					$("input[name='genaddusg_ent_id_lst']").each(function(index,element){
						var clickFunc = $(this).attr("onclick");
						$(this).removeAttr("onclick");
						$(this).click(function(){
							var selectObj = $(this).parent().parent().parent().find("select");
							//var obj = $(this).parents().find("select option");
							// 获取已经选中的用户
							var entIdArr = new Array();
							$(this).parent().parent().parent().find("select option").each(function(index,element){
								var val = $(element).val();
								var text = $(element).text();
								entIdArr.push(val);
							});
							
							// 获取弹出层
							var winOpen = eval(clickFunc);
							
							$(winOpen).load(function(){
							
								 // 禁止F5刷新
								winOpen.document.onkeydown = function (e) {
								    var ev = window.event || e;
								    var code = ev.keyCode || ev.which;
								    if (code == 116) {
								        if(ev.preventDefault) {
								            ev.preventDefault();
								        } else {
								            ev.keyCode = 0;
								            ev.returnValue = false;
								        }
								    }
								}
								
								// 重置确定按钮的事件
								winOpen.$('input[name="frmSubmitBtn"]').each(function(index,element){
	       	 						if(index == 0) {
	       	 							var callFunc = $(this).attr("onclick");
										$(this).removeAttr("onclick");
										$(this).click(function(){
											$(selectObj).empty();
											winOpen.returnSelectValues();
										});
	       	 						}
	       	 					});
							
								// 回显逻辑函数
								function tempFun(winOpen,items,time) {
									
									winOpen.setTimeout(function(){
										var flag = false;
										// 遍历当前树
			       	 					items.each(function(index,element){
			       	 						var down = $(element).parent().find("img:first").attr("src");
			       	 						var prev =  $(element).prev().attr("src");
			       	 						if(down != null && down.indexOf("Tplus.png") != -1 || down.indexOf("Lplus.png") != -1) {
			       	 							$(element).parent().dblclick();
					       	 					flag = true;
			       	 						} else if(prev!= null && prev != 'undefined'  && prev.indexOf("loading.gif") != -1){
			       	 							flag = true;
			       	 						}
				       	 				});
				       	 				
				       	 				// 是否整棵树都已经打开
				       	 				if(flag) {
				       	 					tempFun(winOpen,winOpen.$('a'),time);
				       	 				} else {
				       	 					console.info("加载完成");
				       	 					winOpen.$('input').each(function(index,element){
				       	 						var val = $(element).val();
				       	 						var id = val.split(",")[0];
				       	 						if($.inArray(id,entIdArr) >= 0) {
				       	 							$(element).prop("checked",true);
				       	 						}
				       	 					});
				       	 				}
				       	 				
						       	 	},time);
								};
								
								
								var interval = winOpen.setInterval(function(){
						       	 	var items = winOpen.$('a');
						       	 	if(items.length >= 2) {
						       	 		var imgSrc = $(items[2]).prev().attr("src");
						       	 		if(imgSrc != null && imgSrc.indexOf("loading.gif") != -1) {
						       	 			tempFun(winOpen,items,10);
						       	 			winOpen.clearInterval(interval);
						       	 		}
						       	 	}
						       		
						        },100);
						        
							});
						});
					});
					]]>
				</script>
			</body>
		</html>
	</xsl:template>
	<!-- ========================================================================== -->
	
	
	<xsl:variable name="lab_add_tc_title" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_108')"/>
	<xsl:variable name="lab_upd_tc_title" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_109')"/>
	<xsl:variable name="lab_tc_code" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_110')"/>
	<xsl:variable name="lab_tc_name" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_111')"/>
	<xsl:variable name="lab_target_group" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_112')"/>
	<xsl:variable name="lab_g_form_btn_ok" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_113')"/>
	<xsl:variable name="lab_g_form_btn_cancel" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_114')"/>
	<xsl:variable name="lab_officer_desc" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_115')"/>
	<xsl:variable name="lab_target_group_desc" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_116')"/>
	<xsl:variable name="lab_all_usergroups" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_117')"/>
	<xsl:variable name="lab_mgt_user" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_118')"/>
	<xsl:variable name="lab_mgt_user_yes" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_119')"/>
	<xsl:variable name="lab_mgt_user_no" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_120')"/>
	<xsl:variable name="lab_ta" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_121')"/>



	<!-- ============================================================================  -->
	<xsl:template name="content">
	 
		<xsl:call-template name="wb_ui_hdr">
				<xsl:with-param name="belong_module">FTN_AMD_POSTER_MAIN</xsl:with-param>
			<xsl:with-param name="parent_code">FTN_AMD_TC_MAIN</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:choose>
					<xsl:when test="$tc_code">
						<xsl:value-of select="$lab_upd_tc_title"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$lab_add_tc_title"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:with-param>
		 </xsl:call-template>
		 <!--detail table-->
		 <xsl:call-template name="draw_detail_lst">
			<xsl:with-param name="lab_tc_code" select="$lab_tc_code"/>
			<xsl:with-param name="lab_tc_name" select="$lab_tc_name"/>
			<xsl:with-param name="lab_target_group" select="$lab_target_group"/>
			<xsl:with-param name="lab_officer_desc" select="$lab_officer_desc"/>
			<xsl:with-param name="lab_target_group_desc" select="$lab_target_group_desc"/>
			<xsl:with-param name="lab_g_form_btn_cancel" select="$lab_g_form_btn_cancel"/>
			<xsl:with-param name="lab_all_usergroups" select="$lab_all_usergroups"/>
			<xsl:with-param name="lab_mgt_user" select="$lab_mgt_user"/>
			<xsl:with-param name="lab_mgt_user_yes" select="$lab_mgt_user_yes"/>
			<xsl:with-param name="lab_mgt_user_no" select="$lab_mgt_user_no"/>	
			<xsl:with-param name="lab_ta" select="$lab_ta"/>
		 </xsl:call-template>
		 <!--end detail table-->
		 <table>
			<tr>
				<td class="wzb-form-label">
				</td>
				<td class="wzb-ui-module-text">
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_info_required"/>
				</td>
			</tr>
		 </table>
		<div class="wzb-bar">
			<xsl:variable name="is_ins_ind">
				<xsl:choose>
					<xsl:when test="$tc_code">false</xsl:when>
					<xsl:otherwise>true</xsl:otherwise>
				</xsl:choose>
			</xsl:variable>
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_g_form_btn_ok"/></xsl:with-param>
				<xsl:with-param name="wb_gen_btn_href">javascript:ct_mgt.add_upd_tc_exe(frmXml,'<xsl:value-of select="$tc_id"/>','<xsl:value-of select="$is_ins_ind"/>','<xsl:value-of select="$tc/parent_tcr/@id"/>')</xsl:with-param>
				<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
			</xsl:call-template>
			<xsl:variable name="cancel_js">
				<xsl:choose>
					<xsl:when test="$tc/@code">javascript:ct_mgt.tc_detail('<xsl:value-of select="$tc_id"/>');</xsl:when>
					<xsl:otherwise>javascript:ct_mgt.tc_detail('<xsl:value-of select="$tc/parent_tcr/@id"/>');</xsl:otherwise>
				</xsl:choose>
			</xsl:variable>
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_g_form_btn_cancel"/></xsl:with-param>
				<xsl:with-param name="wb_gen_btn_href"><xsl:value-of select="$cancel_js"/></xsl:with-param>
				<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
			</xsl:call-template>
		</div>
	</xsl:template>
	<!-- ============================================================================  -->
	<xsl:template name="draw_detail_lst">
		<xsl:param name="lab_tc_code"/>
		<xsl:param name="lab_tc_name"/>
		<xsl:param name="lab_target_group"/>
		<xsl:param name="lab_officer_desc"/>
		<xsl:param name="lab_target_group_desc"/>
		<xsl:param name="lab_all_usergroups"/>
		 <xsl:param name="lab_mgt_user"/>
		 <xsl:param name="lab_mgt_user_yes"/>
		 <xsl:param name="lab_mgt_user_no"/>
		 <xsl:param name="lab_ta"/>	
		<table>
			<tr>
				<td class="wzb-form-label">
					<span class="wzb-form-star">*</span>
					<xsl:value-of select="$lab_tc_code"/>
					<xsl:text>：</xsl:text>
				</td>
				<td class="wzb-form-control">
					<input class="wzb-inputText" type="text" name="tc_code" size="25" style="width:300px;"  value="{$tc_code}"/>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label">
					<span class="wzb-form-star">*</span>
					<xsl:value-of select="$lab_tc_name"/>
					<xsl:text>：</xsl:text>
				</td>
				<td class="wzb-form-control">
					<input class="wzb-inputText" type="text" name="tc_name" size="25" style="width:300px;"  value="{$tc_name}"/>
				</td>
			</tr>
			</table>
			<xsl:call-template name="draw_officers">
				<xsl:with-param name="lab_officer_desc" select="$lab_officer_desc"/>
				<xsl:with-param name="lab_ta" select="$lab_ta"/>
			</xsl:call-template>
			<xsl:call-template name="draw_target_group">
				<xsl:with-param name="lab_target_group_desc" select="$lab_target_group_desc"/>
				<xsl:with-param name="lab_target_group" select="$lab_target_group"/>
				<xsl:with-param name="lab_all_usergroups" select="$lab_all_usergroups"/>
			</xsl:call-template>
			<table>
				<tr>
					<td class="wzb-form-label" valign="top">
						<span class="wzb-form-star">*</span>
						<xsl:value-of select="$lab_mgt_user"/>
						<xsl:text>：</xsl:text>
					</td>
					<td class="wzb-form-control">		
						<input type="radio" name="mgt_user_rdo" id="mgt_user_yes" value="Y">
							<xsl:if test="$tc/@user_mgt_ind='true'">
								<xsl:attribute name="checked">checked</xsl:attribute>
							</xsl:if>
						</input>
						<label for="mgt_user_yes">
							<xsl:value-of select="$lab_mgt_user_yes"/>
						</label>
						<br/>
						<input type="radio" name="mgt_user_rdo" id="mgt_user_no" value="N">
							<xsl:if test="$tc/@user_mgt_ind='false'">
								<xsl:attribute name="checked">checked</xsl:attribute>
							</xsl:if>
						</input>				
						<label for="mgt_user_no">
							<xsl:value-of select="$lab_mgt_user_no"/>
						</label>
					</td>
				</tr>
			</table>
	</xsl:template>
	<!-- ============================================================================  -->
	<xsl:template name="draw_officers">
		<xsl:param name="lab_officer_desc"/>
		<xsl:param name="lab_ta"/>
		<xsl:if test="count($admin_role/role)>0">
			<xsl:apply-templates select="$admin_role/role">
				<xsl:with-param name="lab_officer_desc" select="$lab_officer_desc"/>
				<xsl:with-param name="lab_ta" select="$lab_ta"/>
			</xsl:apply-templates>
		</xsl:if>
	</xsl:template>
	<!-- ============================================================================  -->
	<xsl:template name="draw_target_group">
		<xsl:param name="lab_target_group"/>
		<xsl:param name="lab_target_group_desc"/>
		<xsl:param name="lab_all_usergroups"/>
		<table>
		   <tr>
				<td class="wzb-form-label" valign="top">
						<xsl:value-of select="$lab_target_group"/>
						<xsl:text>：</xsl:text>
				</td>
				<td class="wzb-form-control">
				<xsl:choose>
					<xsl:when test="$tc/@is_super='true'">
						<xsl:value-of select="$lab_all_usergroups"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="wb_goldenman">
									<xsl:with-param name="field_name">usg_ent_id_lst</xsl:with-param>
									<xsl:with-param name="name">usg_ent_id_lst</xsl:with-param>
									<xsl:with-param name="box_size">4</xsl:with-param>
									<xsl:with-param name="tree_type">user_group</xsl:with-param>
									<xsl:with-param name="select_type">1</xsl:with-param>
									<xsl:with-param name="pick_leave">0</xsl:with-param>
									<xsl:with-param name="pick_root">0</xsl:with-param>
									<xsl:with-param name="label_add_btn">
										<xsl:value-of select="$lab_gen_select"/>
									</xsl:with-param>
									<xsl:with-param name="option_list">
										<xsl:apply-templates select="$target_group"/>
									</xsl:with-param>
									<xsl:with-param name="parent_tcr_id">
										<xsl:value-of select="$tc/parent_tcr/@id"/>
									</xsl:with-param>
									<xsl:with-param name="tc_id">
										<xsl:value-of select="$tc_id"/>
									</xsl:with-param>
						</xsl:call-template>
					</xsl:otherwise>
				</xsl:choose>
				</td>
			</tr>
			<!--
			<tr>
				<td width="20%" align="right" valign="top" height="14">
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
				<td>
					<xsl:value-of select="$lab_target_group_desc"/>
				</td>
			</tr>
			-->
		</table>
	</xsl:template>
	<!-- ============================================================================  -->
	<xsl:template match="role">
		<xsl:param name="lab_officer_desc"/>
		<xsl:param name="lab_ta"/>
		<xsl:variable name="id" select="@id"/>
		<xsl:variable name="field_name"><xsl:value-of select="@id"/>_usr_ent_id_lst</xsl:variable>
		<input type="hidden" name="role_{position()}" value="{@id}"/>
		<input type="hidden">
			<xsl:attribute name="name"><xsl:value-of select="@id"/>_usr_lst</xsl:attribute>
		</input>
		<table>
		  <tr>
			<td class="wzb-form-label" valign="top">
				<xsl:choose>
					   	<xsl:when test="starts-with(@ id, 'TADM_')"><xsl:value-of select="$lab_ta"/></xsl:when>
						<xsl:otherwise><xsl:value-of select="@title"/></xsl:otherwise>
				    </xsl:choose>
					
					<xsl:text>：</xsl:text>
			</td>
			<td class="wzb-form-control">
				<xsl:call-template name="wb_goldenman">
					    <xsl:with-param name="field_name"><xsl:value-of select="$field_name"/></xsl:with-param>
						<xsl:with-param name="name">usr_ent_id_lst</xsl:with-param>
						<xsl:with-param name="box_size">4</xsl:with-param>
						<xsl:with-param name="tree_type">user_group_and_user</xsl:with-param>
						<xsl:with-param name="select_type">4</xsl:with-param>
						<xsl:with-param name="pick_leave">0</xsl:with-param>
						<xsl:with-param name="pick_root">0</xsl:with-param>
						<!--<xsl:with-param name="ftn_ext_id">TC_MAIN_IN_TCR</xsl:with-param>-->
						<xsl:with-param name="rol_ext_id"><xsl:value-of select="$id"/></xsl:with-param>
						<!--<xsl:with-param name="add_function">
							goldenman.openitemaccwin(<xsl:value-of select="/tc_module/meta/cur_usr/@root_ent_id"/>,'<xsl:value-of select="@id"/>','<xsl:value-of select="$field_name"/>','')
						</xsl:with-param> -->
						<xsl:with-param name="label_add_btn">
								<xsl:value-of select="$lab_gen_select"/>
						</xsl:with-param>
						<xsl:with-param name="option_list">
							<xsl:apply-templates select="$tc/officer_list/role[@id = $id]/entity"/>
						</xsl:with-param>
						<xsl:with-param name="search">true</xsl:with-param>
						<xsl:with-param name="filter_user_group">1</xsl:with-param>
					    <!--<xsl:with-param name="search_function">
						    goldenman.openitemaccsearchwin(<xsl:value-of select="/tc_module/meta/cur_usr/@root_ent_id"/>,'<xsl:value-of select="@id"/>','<xsl:value-of select="$field_name"/>','<xsl:value-of select="$wb_lang"/>','')
					    </xsl:with-param>-->
					     <xsl:with-param name="search_function">
					     	<xsl:choose>
								<xsl:when test="$cur_rol != 'ADM'">
									javascript:usr.search.popup_search_prep('<xsl:value-of select="$field_name"/>','','<xsl:value-of select="$root_ent_id"/>', '0', '', '', '<xsl:value-of select="$id"/>', '0','', '','', '1','<xsl:value-of select="$tc/parent_tcr/@id"/>')
								</xsl:when>
								<xsl:otherwise>
									javascript:usr.search.popup_search_prep('<xsl:value-of select="$field_name"/>','','<xsl:value-of select="$root_ent_id"/>', '0', '', '', '<xsl:value-of select="$id"/>', '0','', '','', '0')
								</xsl:otherwise>
							</xsl:choose>
						</xsl:with-param>
					     <!--<xsl:with-param name="search_function">
							javascript:usr.search.popup_search_prep('<xsl:value-of select="$field_name"/>','','<xsl:value-of select="$root_ent_id"/>', '0', '', '', '0', '0','', '','TC_MAIN_IN_TCR', '0')
						</xsl:with-param>-->
						
						<xsl:with-param name="parent_tcr_id">
							<xsl:if test="starts-with($cur_rol, 'TADM_')">
								<xsl:value-of select="$tc/parent_tcr/@id"/>
							</xsl:if>
						</xsl:with-param>
						
				</xsl:call-template>
			</td>
		</tr>
		<!--
		<tr>
			<td width="20%" align="right" valign="top" height="14">
				<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
			</td>
			<td>
				<xsl:value-of select="$lab_officer_desc"/>
			</td>
		</tr>
		-->
		</table>
	</xsl:template>
	<!-- ============================================================================  -->
	<xsl:template match="entity">
		<option value="{@id}">
			<xsl:value-of select="text()"/>
		</option>
	</xsl:template>
</xsl:stylesheet>