<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_pagination.xsl"/>	
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:import href="utils/select_all_checkbox.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="utils/wb_sortorder_cursor.xsl"/>
	
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	
	<xsl:import href="share/itm_gen_action_nav_share.xsl"/>
	
<xsl:output indent="yes"/>	
<!-- =============================================================== -->	
<xsl:variable name="page_variant" select="/announcement/meta/page_variant"/>
<xsl:variable name="ann_cnt" select="count(/announcement/message/item)"/>
<!-- paginatoin variables -->
<xsl:variable name="page_size"><xsl:value-of select="/announcement/message/pagination/@page_size"/></xsl:variable>
<xsl:variable name="cur_page"><xsl:value-of select="/announcement/message/pagination/@cur_page"/></xsl:variable>
<xsl:variable name="total"><xsl:value-of select="/announcement/message/pagination/@total_rec"/></xsl:variable>
<xsl:variable name="timestamp"><xsl:value-of select="/announcement/message/pagination/@timestamp"/></xsl:variable>
<!-- sorting variable -->
<xsl:variable name="cur_sort_col"><xsl:value-of select="/announcement/message/pagination/@sort_col"/></xsl:variable>
<xsl:variable name="cur_sort_order"><xsl:value-of select="/announcement/message/pagination/@sort_order"/></xsl:variable>
<xsl:variable name="sort_order_by">
	<xsl:choose>
		<xsl:when test="$cur_sort_order = 'ASC' or $cur_sort_order = 'asc' ">DESC</xsl:when>
		<xsl:otherwise>ASC</xsl:otherwise>
	</xsl:choose>	
</xsl:variable>
<xsl:variable name="msg_type"><xsl:value-of select="/announcement/message/@type"/></xsl:variable>
<xsl:variable name="res_id"><xsl:value-of select="/announcement/message/@res_id"/></xsl:variable>

<!-- 课程公告 -->
<xsl:variable name="label_core_training_management_236" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_236')"/>

<!-- 考试公告 -->
<xsl:variable name="label_core_training_management_236" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_236')"/>
	
<!-- =============================================================== -->	
<xsl:template match="/">
	<html><xsl:apply-templates select="announcement"/></html>
</xsl:template>
<!-- =============================================================== -->	
<xsl:template match="announcement">
		<xsl:apply-templates select="message"/>
</xsl:template>
<!-- =============================================================== -->
<xsl:template match="message">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
		<title><xsl:value-of select="$wb_wizbank"/></title>
		<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
		<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
		<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_announcement.js"/>
		<script language="Javascript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
		<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
		<script language="JavaScript" type="text/javascript"><![CDATA[
			ann = new wbAnnouncement;			
			/*function changeView(view){
				if (view=="curr") {url = wb_utils_invoke_servlet("cmd","get_msg","stylesheet","announ_lst.xsl","msg_type","SYS","msg_start_date","]]><xsl:value-of select="cur_time"/><![CDATA[");}
				else if (view=="arch") {url = wb_utils_invoke_servlet("cmd","get_msg","stylesheet","announ_lst.xsl","msg_type","SYS","msg_end_date","]]><xsl:value-of select="cur_time"/><![CDATA[");}
				parent.location.href = url;
			}*/
		]]></script>
		<xsl:call-template name="wb_css"><xsl:with-param name="view">wb_ui</xsl:with-param></xsl:call-template>
		<xsl:call-template name="new_css"/>
	</head>
	<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
		<form name="frmXml" method="" action="">
			<xsl:call-template name="wb_init_lab"/>
			<input type="hidden" name="cmd"/>
			<input type="hidden" name="url_success" value=""/>
			<input type="hidden" name="url_failure" value=""/>
			<input type="hidden" name="msg_type" value=""/>
			<input type="hidden" name="msg_lst" value=""/>
			<xsl:if test="$msg_type = 'RES'"><input type="hidden" name="res_id" value="{$res_id}"/></xsl:if>
		</form>
	</body>		
</xsl:template>
<!-- =============================================================== -->	
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_g_form_btn_close">關閉</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_remove">刪除</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_add">添加</xsl:with-param>
	      <xsl:with-param name="lab_ann">通告</xsl:with-param>
			<xsl:with-param name="lab_title">標題</xsl:with-param>
			<xsl:with-param name="lab_create_by">建立者</xsl:with-param>
			<xsl:with-param name="lab_run_info"><xsl:value-of select="$lab_const_run"/>訊息</xsl:with-param>
			<xsl:with-param name="lab_date">建立日期</xsl:with-param>
			<xsl:with-param name="lab_edit">修改</xsl:with-param>
			<xsl:with-param name="lab_view">查看</xsl:with-param>
			<xsl:with-param name="lab_no_item">列表中無通告</xsl:with-param>
			<xsl:with-param name="lab_status">狀態</xsl:with-param>
			<xsl:with-param name="lab_list">列表</xsl:with-param>
			<xsl:with-param name="lab_on">已發佈</xsl:with-param>
			<xsl:with-param name="lab_off">未發佈</xsl:with-param>
			<xsl:with-param name="lab_ann_desc">創建並管理課程公告。出現在這裡的公告只能被學習此課程的學員查看。</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_g_form_btn_close">关闭</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_remove">删除</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_add">添加</xsl:with-param>
	      <xsl:with-param name="lab_ann">公告</xsl:with-param>
			<xsl:with-param name="lab_title">标题</xsl:with-param>
			<xsl:with-param name="lab_create_by">建立者</xsl:with-param>
			<xsl:with-param name="lab_run_info"><xsl:value-of select="$lab_const_run"/>信息</xsl:with-param>
			<xsl:with-param name="lab_date">建立日期</xsl:with-param>
			<xsl:with-param name="lab_edit">修改</xsl:with-param>
			<xsl:with-param name="lab_view">查看</xsl:with-param>
			<xsl:with-param name="lab_no_item">列表中无公告</xsl:with-param>
			<xsl:with-param name="lab_status">状态</xsl:with-param>
			<xsl:with-param name="lab_list">列表</xsl:with-param>
			<xsl:with-param name="lab_on">已发布</xsl:with-param>
			<xsl:with-param name="lab_off">未发布</xsl:with-param>
			<xsl:with-param name="lab_ann_desc">
				<xsl:choose>
					<xsl:when test="$itm_exam_ind = 'true'">
						创建并管理考试公告。出现在这里的公告只能被学习此考试程的学员查看。
					</xsl:when>
					<xsl:otherwise>
						创建并管理课程公告。出现在这里的公告只能被学习此课程的学员查看。
					</xsl:otherwise>
				</xsl:choose>
			</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_g_form_btn_close">Close</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_remove">Remove</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_add">Add</xsl:with-param>
			<xsl:with-param name="lab_ann">Notice </xsl:with-param>
			<xsl:with-param name="lab_title">Title</xsl:with-param>
			<xsl:with-param name="lab_create_by">Author</xsl:with-param>
			<xsl:with-param name="lab_run_info"><xsl:value-of select="$lab_const_run"/> information</xsl:with-param>
			<xsl:with-param name="lab_date">Time</xsl:with-param>
			<xsl:with-param name="lab_edit">Edit</xsl:with-param>
			<xsl:with-param name="lab_view">View</xsl:with-param>
			<xsl:with-param name="lab_no_item">No notices found</xsl:with-param>
			<xsl:with-param name="lab_status">Status</xsl:with-param>
			<xsl:with-param name="lab_list">list</xsl:with-param>
			<xsl:with-param name="lab_on">Published</xsl:with-param>
			<xsl:with-param name="lab_off">Unpublished</xsl:with-param>
			<xsl:with-param name="lab_ann_desc">Create and manage the course notices. Notices here will be published to learners of this course only.</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
<!-- =============================================================== -->	
<xsl:template name="content">
	<xsl:param name="lab_ann"/>
	<xsl:param name="lab_title"/>
	<xsl:param name="lab_create_by"/>
	<xsl:param name="lab_run_info"/>
	<xsl:param name="lab_date"/>
	<xsl:param name="lab_edit"/>	
	<xsl:param name="lab_view"/>
	<xsl:param name="lab_no_item"/>
	<xsl:param name="lab_status"/>
	<xsl:param name="lab_list"/>
	<xsl:param name="lab_on"/>
	<xsl:param name="lab_off"/>
	<xsl:param name="lab_ann_desc"/>
	<xsl:param name="lab_g_form_btn_close"/>
	<xsl:param name="lab_g_txt_btn_remove"/>
	<xsl:param name="lab_g_txt_btn_add"/>
	
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module" select="$belong_module"></xsl:with-param>
			<xsl:with-param name="parent_code" select="$parent_code"/>
		</xsl:call-template>
		
		<xsl:call-template name="itm_action_nav">
			<xsl:with-param  name="cur_node_id">110</xsl:with-param>
		</xsl:call-template>
	<xsl:call-template name="wb_ui_title">
		<xsl:with-param name="text">
			<xsl:value-of select="//itm_action_nav/@itm_title"/>
		</xsl:with-param>
	</xsl:call-template>
	<xsl:call-template name="wb_ui_nav_link">
			<xsl:with-param name="text">
				<!-- <a href="javascript:itm_lst.get_item_detail({$itm_id})" class="NavLink">
					abc<xsl:value-of select="$itm_title"/>
				</a> -->
				<xsl:choose>
					<xsl:when test="$run_ind = 'false'">
						 <a href="javascript:itm_lst.get_item_detail({$itm_id})" class="NavLink">
						   <xsl:value-of select="$itm_title"/>
						</a>
						<span class="NavLink">&#160;&gt;&#160;<xsl:value-of select="$label_core_training_management_236"/>
						</span>
					</xsl:when>
					<xsl:otherwise>
					   <xsl:apply-templates select="//item/nav/item" mode="nav">
					       <xsl:with-param name="lab_run_info" select="$lab_run_info"/>
			               <xsl:with-param name="lab_session_info" select="$lab_run_info"/>
				       </xsl:apply-templates><span class="NavLink"><xsl:text>&#160;&gt;&#160;</xsl:text>
				       <xsl:value-of select="$label_core_training_management_236"/></span>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:with-param>
		</xsl:call-template>
	<div class="margin-top28"></div>
	
	<xsl:call-template name="wb_ui_desc">
		<xsl:with-param name="text"><xsl:value-of select="$lab_ann_desc"/></xsl:with-param>
	</xsl:call-template>
	
	<xsl:call-template name="wb_ui_head">
		<xsl:with-param name="extra_td">
			<td align="right" height="19">
				<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				<!-- Access Control -->
				<xsl:choose>
					<xsl:when test="$page_variant/@hasAddSysAnnBtn= 'true'">
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_g_txt_btn_add"/></xsl:with-param>
							<xsl:with-param name="wb_gen_btn_href">javascript:ann.add_sys_ann_lst('<xsl:value-of select="$msg_type"/>','<xsl:value-of select="$res_id"/>',true,'','','<xsl:value-of select="$itm_exam_ind"/>')</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
						</xsl:call-template>
					</xsl:when>
				</xsl:choose>
				<xsl:choose>
					<xsl:when test="$page_variant/@hasDelSysAnnBtn = 'true'">
						<xsl:if test="$ann_cnt &gt;= 1">
							<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
							<xsl:call-template name="wb_gen_button">
								<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_g_txt_btn_remove"/></xsl:with-param>
								<xsl:with-param name="wb_gen_btn_href">javascript:ann.multi_del_sys_ann_lst(document.frmXml,'<xsl:value-of select="$msg_type"/>','','<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
							</xsl:call-template>
						</xsl:if>
					</xsl:when>			
				</xsl:choose>
				<!-- Access Control --> 
			</td>
		</xsl:with-param>
	</xsl:call-template>
	<!-- list view -->
		<xsl:choose>
			<xsl:when test="$ann_cnt &gt;= 1">	
	<table  class="table wzb-ui-table">
			<tr class="wzb-ui-table-head">
				<td align="left" width="40%">
					<!-- Access Control -->
					<xsl:choose>
						<xsl:when test="$page_variant/@hasDelSysAnnBtn = 'true'">
							<xsl:call-template name="select_all_checkbox">
								<xsl:with-param name="chkbox_lst_cnt"><xsl:value-of select="$ann_cnt"/></xsl:with-param>
								<xsl:with-param name="display_icon">false</xsl:with-param>
							</xsl:call-template>
						</xsl:when>
						<xsl:otherwise><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/></xsl:otherwise>
					</xsl:choose>
					<!-- Access Control -->
					<xsl:choose>
						<xsl:when test="$cur_sort_col = 'msg_title' ">
							<a href="javascript:wb_utils_nav_get_urlparam('sortCol','msg_title','sortOrder','{$sort_order_by}','cur_page','1')" class="Text">
								<xsl:value-of select="$lab_title"/>
								<xsl:call-template name="wb_sortorder_cursor">
									<xsl:with-param name="img_path"><xsl:value-of select="$wb_img_path"/></xsl:with-param>
									<xsl:with-param name="sort_order"><xsl:value-of select="$cur_sort_order"/></xsl:with-param>
								</xsl:call-template>
							</a>
						</xsl:when>
						<xsl:otherwise>
							<a href="javascript:wb_utils_nav_get_urlparam('sortCol','msg_title','sortOrder','asc','cur_page','1')" class="Text">
								<xsl:value-of select="$lab_title"/>
							</a>
						</xsl:otherwise>
					</xsl:choose>
				</td>
				
				
				<td width="20%" align="left">
					<xsl:choose>
						<xsl:when test="$cur_sort_col = 'usr_display_bil'">
							<a href="javascript:wb_utils_nav_get_urlparam('sortCol','usr_display_bil','sortOrder','{$sort_order_by}','cur_page','1')" class="Text">
								<xsl:value-of select="$lab_create_by"/>
								<xsl:call-template name="wb_sortorder_cursor">
									<xsl:with-param name="img_path"><xsl:value-of select="$wb_img_path"/></xsl:with-param>
									<xsl:with-param name="sort_order"><xsl:value-of select="$cur_sort_order"/></xsl:with-param>
								</xsl:call-template>
							</a>
						</xsl:when>
						<xsl:otherwise>
							<a href="javascript:wb_utils_nav_get_urlparam('sortCol','usr_display_bil','sortOrder','asc','cur_page','1')" class="Text">
								<xsl:value-of select="$lab_create_by"/>
							</a>
						</xsl:otherwise>
					</xsl:choose>
				</td>
				
				<!-- Access Control -->
				<xsl:if test="$page_variant/@ShowSysAnnStatus = 'true'">
					<td width="10%" align="left">
						<xsl:choose>
							<xsl:when test="$cur_sort_col = 'msg_status'">
								<a href="javascript:wb_utils_nav_get_urlparam('sortCol','msg_status','sortOrder','{$sort_order_by}','cur_page','1')" class="Text">
									<xsl:value-of select="$lab_status"/>
									<xsl:call-template name="wb_sortorder_cursor">
										<xsl:with-param name="img_path"><xsl:value-of select="$wb_img_path"/></xsl:with-param>
										<xsl:with-param name="sort_order"><xsl:value-of select="$cur_sort_order"/></xsl:with-param>
									</xsl:call-template>
								</a>
							</xsl:when>
							<xsl:otherwise>
								<a href="javascript:wb_utils_nav_get_urlparam('sortCol','msg_status','sortOrder','asc','cur_page','1')" class="Text">
									<xsl:value-of select="$lab_status"/>
								</a>
							</xsl:otherwise>
						</xsl:choose>
					</td>
				</xsl:if>
				<!-- Access Control -->
				<td width="20%" align="left">
					<xsl:choose>
						<xsl:when test="$cur_sort_col = 'msg_begin_date' ">
							<a href="javascript:wb_utils_nav_get_urlparam('sortCol','msg_begin_date','sortOrder','{$sort_order_by}','cur_page','1')" class="Text">
								<xsl:value-of select="$lab_date"/>
								<xsl:call-template name="wb_sortorder_cursor">
									<xsl:with-param name="img_path"><xsl:value-of select="$wb_img_path"/></xsl:with-param>
									<xsl:with-param name="sort_order"><xsl:value-of select="$cur_sort_order"/></xsl:with-param>
								</xsl:call-template>
							</a>
						</xsl:when>
						<xsl:otherwise>
							<a href="javascript:wb_utils_nav_get_urlparam('sortCol','msg_begin_date','sortOrder','asc','cur_page','1')" class="Text">
								<xsl:value-of select="$lab_date"/>
							</a>
						</xsl:otherwise>
					</xsl:choose>
				</td>
				<td width="10%"><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/></td>
			</tr>
				<xsl:apply-templates select="item">
					<xsl:with-param name="lab_edit"><xsl:value-of select="$lab_edit"/></xsl:with-param>
					<xsl:with-param name="lab_on"><xsl:value-of select="$lab_on"/></xsl:with-param>
					<xsl:with-param name="lab_off"><xsl:value-of select="$lab_off"/></xsl:with-param>
					<xsl:with-param name="itm_id"><xsl:value-of select="$itm_id"/></xsl:with-param>
				</xsl:apply-templates>
					</table>
			</xsl:when>
			<xsl:otherwise>
						<xsl:call-template name="wb_ui_show_no_item">
							<xsl:with-param name="text">
								<xsl:value-of select="$lab_no_item"/>
							</xsl:with-param>
						</xsl:call-template>	
			</xsl:otherwise>
		</xsl:choose>		

	<!-- Pagination -->
	<xsl:if test="$ann_cnt &gt;= 1">
		<xsl:call-template name="wb_ui_pagination">
			<xsl:with-param name="cur_page"><xsl:value-of select="$cur_page"/></xsl:with-param>
			<xsl:with-param name="page_size"><xsl:value-of select="$page_size"/></xsl:with-param>
			<xsl:with-param name="total"><xsl:value-of select="$total"/></xsl:with-param>
			<xsl:with-param name="timestamp"><xsl:value-of select="$timestamp"/></xsl:with-param>
		</xsl:call-template>
	</xsl:if>
</xsl:template>
<!-- =============================================================== -->
	<xsl:template match="item" mode="nav">
		<xsl:param name="lab_run_info"/>
		<xsl:param name="lab_session_info"/>
		<xsl:choose>
			<xsl:when test="@run_ind = 'true'">
				<xsl:text>&#160;&gt;&#160;</xsl:text>
				<xsl:variable name="value">
					<xsl:for-each select="preceding-sibling::item">
						<xsl:if test="position()=last()">
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
						<xsl:if test="position()=last()">
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
<xsl:template match="item">
	<xsl:param name="lab_edit"/>
	<xsl:param name="lab_on"/>
	<xsl:param name="lab_off"/>
	<xsl:param name="itm_id" />
	<xsl:variable name="row_class">
		<xsl:choose>
			<xsl:when test="position() mod 2">RowsEven</xsl:when>
			<xsl:otherwise>RowsOdd</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>		
	<tr class="{$row_class}">
		<td align="left">
			<!-- Access Control -->
			<xsl:choose>
				<xsl:when test="$page_variant/@hasDelSysAnnBtn = 'true'">
					<input type="checkbox" name="" value="{@id}"/>
				</xsl:when>
				<xsl:otherwise><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/></xsl:otherwise>
			</xsl:choose>
			<!-- Access Control --> 
			<a class="Text" href="javascript:ann.get_ann_dtl({@id},'{$msg_type}','','',{$itm_id},{$itm_exam_ind})"><xsl:value-of select="title"/></a>
		</td>
		<td align="left"><span class="Text"><xsl:value-of select="@usr_display_bil"/></span></td>
		<!-- Access Control -->
		<xsl:if test="$page_variant/@ShowSysAnnStatus = 'true'">
			<td align="left">
				<span class="Text">
					<xsl:choose>
						<xsl:when test="@status = 'ON'"><xsl:value-of select="$lab_on"/></xsl:when>
						<xsl:otherwise><xsl:value-of select="$lab_off"/></xsl:otherwise>
					</xsl:choose>
				</span>
			</td>
		</xsl:if>
		<!-- Access Control -->
		<td align="left">
			<span class="Text">
				<xsl:call-template name="display_time">
					<xsl:with-param name="my_timestamp"><xsl:value-of select="@begin_date"/></xsl:with-param>
					<xsl:with-param name="dis_time">T</xsl:with-param>
				</xsl:call-template>
			</span>
		</td>
		<td align="right">
			<span class="Text">
				<!-- Access Control -->
				<xsl:choose>
					<xsl:when test="$page_variant/@hasEditSysAnnBtn = 'true'">
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_edit"/></xsl:with-param>
							<xsl:with-param name="wb_gen_btn_href">javascript:ann.upd_sys_ann_lst('<xsl:value-of select="$msg_type"/>','<xsl:value-of select="@id"/>','<xsl:value-of select="$res_id"/>',true,'','','',<xsl:value-of select="$itm_exam_ind"/>)</xsl:with-param>
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/></xsl:otherwise>
				</xsl:choose>
				<!-- Access Control -->
			</span>
		</td>
	</tr>	
</xsl:template>
<!-- =============================================================== -->
</xsl:stylesheet>
