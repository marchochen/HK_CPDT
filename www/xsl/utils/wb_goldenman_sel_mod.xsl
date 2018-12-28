<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="goldenman_draw_button.xsl"/>
	<!-- =============================================================== -->
	<xsl:template name="wb_goldenman_sel_mod">
		<xsl:param name="frm">document.frmXml</xsl:param>
		<xsl:param name="width">300</xsl:param>
		<xsl:param name="field_name"/>
		<xsl:param name="label_add_btn">
			<xsl:value-of select="$lab_gen_add"/>
		</xsl:param>
		<xsl:param name="label_remove_btn">
			<xsl:value-of select="$lab_gen_remove"/>
		</xsl:param>
		<xsl:param name="add_btn_img"/>
		<xsl:param name="remove_btn_img"/>
		<xsl:param name="name">name</xsl:param>
		<xsl:param name="box_multiple">true</xsl:param>
		<xsl:param name="lab_title"/>
		<xsl:param name="box_size"/>
		<xsl:param name="option_list"/>
		<xsl:param name="single_option_value"/>
		<xsl:param name="single_option_desc"/>
		<xsl:param name="on_change_function"/>
		<xsl:param name="on_set_value_function"/>
		<xsl:param name="on_delete_value_function"/>
		<xsl:param name="sel_type">select_mod</xsl:param>
		<!-- select_mod / import_mod   -->
		<xsl:param name="is_multiple">false</xsl:param>
		<xsl:param name="dis_cos_type">1</xsl:param>
		<!--1: 所有 ；2：不显示Course ; 3: 不显示独立内容Course-->
		<xsl:param name="dis_mod_type">1</xsl:param>
		<!--1: 所有 ；2：只第一层 ; 3: 只测验类 -->
		<xsl:param name="close_pop_win">false</xsl:param>
		<xsl:param name="get_cos_info">true</xsl:param>
		<!--itm id  -->
		<xsl:param name="itm_id_field_name"/>
		<xsl:param name="itm_id_field_value"/>
				<!--true / false -->
		<!-- extra remove function will call after remove button click , it is NOT replacement of remove function-->
		<xsl:param name="cos_title"/>
		<xsl:variable name="size">
			<xsl:choose>
				<xsl:when test="$box_size != '' and $is_multiple= 'true'">
					<xsl:value-of select="$box_size"/>
				</xsl:when>
				<xsl:when test="$box_size = '' and $is_multiple= 'true'">4</xsl:when>
				<xsl:otherwise>1</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<!--   ==================================================================================  -->
		<xsl:choose>
			<xsl:when test="$is_multiple= 'true'">
				<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
		
				function remove_function (field, cos_info) {
				     if(field.options.length > 0) {
		                for(var i = (field.options.length - 1); i >= 0; i--) {
			                  var o = field.options[i];
			                  if(o.selected) {
				                     field.options[i] = null;
		                    	}
		                 }
		                 field.selectedIndex = -1;
		                 	]]><xsl:value-of select="$on_delete_value_function"/><![CDATA[;
	                 }
	                 if(document.getElementById('cos_title_label') != null) {
	                    if(field.options.length < 1) {
	                 		document.getElementById('cos_title_label').innerHTML = '';
	                 	}
	                 }
	                  if(document.getElementById(']]><xsl:value-of select="$itm_id_field_name"/><![CDATA[') != null) {
	                    if(field.options.length < 1) {
	                 		document.getElementById(']]><xsl:value-of select="$itm_id_field_name"/><![CDATA[').value= '';
	                 	}
	                 }

              }		
              function remove_all (field, cos_info) {
				     if(field.options.length > 0) {
		                for(var i = (field.options.length - 1); i >= 0; i--) {
				                     field.options[i] = null;
		                 }
		                 //	]]><xsl:value-of select="$on_delete_value_function"/><![CDATA[;
	                 }
	                  if(document.getElementById('cos_title_label') != null) {
		                    if(field.options.length < 1) {
			                		 document.getElementById('cos_title_label').innerText= cos_info;
			                	}
		               }
	                	if(field.options.length < 1) {
		                	if(document.getElementById(']]><xsl:value-of select="$itm_id_field_name"/><![CDATA[') != null) {
		                 		document.getElementById(']]><xsl:value-of select="$itm_id_field_name"/><![CDATA[').value= '';
		                 }
                    }

              }		
              
              
		      
				function getReValue(field,index, mod_title, mod_id, itm_id) {    
	                field[index] = new Option(mod_title, mod_id);
	                	if(document.getElementById(']]><xsl:value-of select="$itm_id_field_name"/><![CDATA[') != null) {
		                 		document.getElementById(']]><xsl:value-of select="$itm_id_field_name"/><![CDATA[').value= itm_id;
		                 }

	                	]]><xsl:value-of select="$on_set_value_function"/><![CDATA[;
              }	
				]]></SCRIPT>
			</xsl:when>
			<xsl:otherwise>
				<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
					function remove_function (field, field_desc, cos_info){
                    field.value = '';
                   	field_desc.value = '';
                   	if(document.getElementById('cos_title_label') != null) {
	                 	document.getElementById('cos_title_label').innerText= cos_info ;
	                 }
	                 if(document.getElementById(']]><xsl:value-of select="$itm_id_field_name"/><![CDATA[') != null) {
		                 		document.getElementById(']]><xsl:value-of select="$itm_id_field_name"/><![CDATA[').value= '';
		                 }


                   	 ]]><xsl:value-of select="$on_delete_value_function"/><![CDATA[;
                 }
                 
                	function getReValue(field,field_desc, mod_title, mod_id, itm_id) {
                    field.value = mod_id;
                   	field_desc.value = mod_title;
                   		if(document.getElementById(']]><xsl:value-of select="$itm_id_field_name"/><![CDATA[') != null) {
		                 		document.getElementById(']]><xsl:value-of select="$itm_id_field_name"/><![CDATA[').value = itm_id;
		                 }
                   	]]><xsl:value-of select="$on_set_value_function"/><![CDATA[;
                }	

				]]></SCRIPT>
			</xsl:otherwise>
		</xsl:choose>
		<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
		var win     
		function  opensearchwin(is_multiple, dis_cos_type, dis_mod_type, field_name, sel_type, title) {
          
			if(is_multiple == null || is_multiple == '') {is_multiple = 'false';}
			if(dis_cos_type == null || dis_cos_type == '') {dis_cos_type = '1';}
			if (dis_mod_type == null || dis_mod_type == '') {dis_mod_type = '1';}
			if (sel_type  == null || sel_type  == '') {sel_type  = 'select_mod';}
			url = wb_utils_invoke_disp_servlet('module' ,'course.ModuleSelectModule' ,'cmd', 'gen_sel_mod_win','is_multiple',is_multiple,'dis_cos_type',dis_cos_type,'dis_mod_type',dis_mod_type,'field_name',field_name,'sel_type',sel_type,'stylesheet','wb_goldman_sel_mod.xsl','title', title, 'width', 482)
			win = wb_utils_open_win(url, "mod_select_win",500, 500);		
		}
		function closeWin() {    
		 ]]><xsl:if test="$close_pop_win = 'true'"><![CDATA[
		     if(win != null)
		         win.close();
		  ]]></xsl:if><![CDATA[
           }
			   
		]]></SCRIPT>
		<!--  ======================================================================  -->
		<xsl:if test="$itm_id_field_name != ''">
		<input type="hidden" name="{$itm_id_field_name}" value="{$itm_id_field_value}" id="{$itm_id_field_name}"/>
		</xsl:if>
		<xsl:choose>
			<xsl:when test="$is_multiple= 'true'">
				<table border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td>
							<table cellpadding="0" cellspacing="0" border="0">
								<xsl:if test="$get_cos_info = 'true'">
									<tr>
										<td>
										<span class="Text"  id="cos_title_label">
											<xsl:copy-of select="$cos_title"/>
											</span>
										</td>
									</tr>
								</xsl:if>
								<tr>
									<td>
										<span class="Text">
											<select width="{$width}" style="width:{$width}px" class="Select" size="{$size}" name="{$field_name}" id="{$field_name}" onchange="{$on_change_function}">
												<xsl:if test="$box_multiple='true'">
													<xsl:attribute name="multiple">multiple</xsl:attribute>
												</xsl:if>
												<xsl:copy-of select="$option_list"/>
											</select>
										</span>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td style="padding-top:10px;">
							<xsl:variable name="add_function">Javascript:opensearchwin('<xsl:value-of select="$is_multiple"/>','<xsl:value-of select="$dis_cos_type"/>','<xsl:value-of select="$dis_mod_type"/>','<xsl:value-of select="$field_name"/>','<xsl:value-of select="$sel_type"/>','<xsl:value-of select="$lab_title"/>')</xsl:variable>
							<xsl:variable name="remove_function">Javascript:remove_function(<xsl:value-of select="$frm"/>.<xsl:value-of select="$field_name"/>,'')</xsl:variable>
							<xsl:choose>
								<xsl:when test="$add_btn_img=''">
									<input onClick="{$add_function}" class="btn wzb-btn-blue margin-right4" value="{$label_add_btn}" name="genadd_{$name}" type="button"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:call-template name="wb_gen_button">
										<xsl:with-param name="wb_gen_btn_name">
											<xsl:value-of select="$add_btn_img"/>
										</xsl:with-param>
										<xsl:with-param name="wb_gen_btn_href">
											<xsl:value-of select="$add_function"/>
										</xsl:with-param>
										<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
									</xsl:call-template>
								</xsl:otherwise>
							</xsl:choose>
							<img border="0" height="1" width="2" src="{$wb_img_path}tp.gif"/>
							<xsl:choose>
								<xsl:when test="$remove_btn_img=''">
									<input onClick="{$remove_function}" class="btn wzb-btn-blue margin-right4" value="{$label_remove_btn}" name="genremove{$name}" type="button"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:call-template name="wb_gen_button">
										<xsl:with-param name="wb_gen_btn_name">
											<xsl:value-of select="$remove_btn_img"/>
										</xsl:with-param>
										<xsl:with-param name="wb_gen_btn_href">
											<xsl:value-of select="$remove_function"/>
										</xsl:with-param>
										<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
									</xsl:call-template>
								</xsl:otherwise>
							</xsl:choose>
							<!--
							<xsl:call-template name="wb_gen_button">
								<xsl:with-param name="wb_gen_btn_name">
									<xsl:value-of select="$label_add_btn"/>
								</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_href">Javascript:opensearchwin('<xsl:value-of select="$is_multiple"/>','<xsl:value-of select="$dis_cos_type"/>','<xsl:value-of select="$dis_mod_type"/>','<xsl:value-of select="$field_name"/>','<xsl:value-of select="$sel_type"/>','<xsl:value-of select="$lab_title"/>')</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
							</xsl:call-template>
							<img border="0" height="1" width="2" src="{$wb_img_path}tp.gif"/>
							<xsl:call-template name="wb_gen_button">
								<xsl:with-param name="wb_gen_btn_name">
									<xsl:value-of select="$label_remove_btn"/>
								</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_href">Javascript:remove_function(<xsl:value-of select="$frm"/>.<xsl:value-of select="$field_name"/>)</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
							</xsl:call-template>
							-->
						</td>
					</tr>
				</table>
			</xsl:when>
			<xsl:otherwise>
				<table border="0" cellspacing="0" cellpadding="0">
					<xsl:if test="$get_cos_info = 'true'">
						<tr>
							<td>
							<span class="Text"  id="cos_title_label">
								<xsl:copy-of select="$cos_title"/>
								</span>
							</td>
						</tr>
					</xsl:if>
					<tr>
						<td>
							<span>
								<input type="hidden" name="{$field_name}" value="{$single_option_value}" id="{$field_name}"/>
								<input type="text" name="{$field_name}_des" style="width:{$width}px;margin-top: 1px;" readonly="readonly" class="wzb-inputText" value="{$single_option_desc}" id="{$field_name}_desc"/>
							</span>
							<xsl:variable name="add_function">Javascript:opensearchwin('<xsl:value-of select="$is_multiple"/>','<xsl:value-of select="$dis_cos_type"/>','<xsl:value-of select="$dis_mod_type"/>','<xsl:value-of select="$field_name"/>','<xsl:value-of select="$sel_type"/>','<xsl:value-of select="$lab_title"/>')</xsl:variable>
							<xsl:variable name="remove_function">Javascript:remove_function(<xsl:value-of select="$frm"/>.<xsl:value-of select="$field_name"/>,<xsl:value-of select="$frm"/>.<xsl:value-of select="$field_name"/>_des,'')</xsl:variable>
							<xsl:choose>
								<xsl:when test="$add_btn_img=''">
									<input onClick="{$add_function}" class="btn wzb-btn-blue " style="margin-top: -4px;padding-top:5px;" value="{$label_add_btn}" name="genadd{$name}" type="button"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:call-template name="wb_gen_button">
										<xsl:with-param name="wb_gen_btn_name">
											<xsl:value-of select="$add_btn_img"/>
										</xsl:with-param>
										<xsl:with-param name="wb_gen_btn_href">
											<xsl:value-of select="$add_function"/>
										</xsl:with-param>
										<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
									</xsl:call-template>
								</xsl:otherwise>
							</xsl:choose>
							<img border="0" height="1" width="2" src="{$wb_img_path}tp.gif"/>
							<xsl:choose>
								<xsl:when test="$remove_btn_img=''">
									<input onClick="{$remove_function}" class="btn wzb-btn-blue " style="margin-top: -4px;padding-top:5px;" value="{$label_remove_btn}" name="genremove{$name}" type="button"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:call-template name="wb_gen_button">
										<xsl:with-param name="wb_gen_btn_name">
											<xsl:value-of select="$remove_btn_img"/>
										</xsl:with-param>
										<xsl:with-param name="wb_gen_btn_href">
											<xsl:value-of select="$remove_function"/>
										</xsl:with-param>
										<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
									</xsl:call-template>
								</xsl:otherwise>
							</xsl:choose>
							<!--
				
							<xsl:call-template name="wb_gen_button">
								<xsl:with-param name="wb_gen_btn_name">
									<xsl:value-of select="$label_add_btn"/>
								</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_href">Javascript:opensearchwin('<xsl:value-of select="$is_multiple"/>','<xsl:value-of select="$dis_cos_type"/>','<xsl:value-of select="$dis_mod_type"/>','<xsl:value-of select="$field_name"/>','<xsl:value-of select="$sel_type"/>','<xsl:value-of select="$lab_title"/>')</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
							</xsl:call-template>
					
							<xsl:call-template name="wb_gen_button">
								<xsl:with-param name="wb_gen_btn_name">
									<xsl:value-of select="$label_remove_btn"/>
								</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_href">Javascript:remove_function(<xsl:value-of select="$frm"/>.<xsl:value-of select="$field_name"/>,<xsl:value-of select="$frm"/>.<xsl:value-of select="$field_name"/>_des)</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
							</xsl:call-template>
							-->
						</td>
					</tr>
				</table>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>
