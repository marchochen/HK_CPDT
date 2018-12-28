<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_pagination.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/trun_date.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="utils/wb_sortorder_cursor.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:import href="utils/select_all_checkbox.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="share/label_lrn_soln.xsl"/>
	<xsl:import href="share/div_tree_share.xsl"/>
	<xsl:import href="share/gen_tree_js.xsl"/>
	<xsl:strip-space elements="*"/>
	<xsl:output indent="no"/>
	<!-- =============================================================== -->
	<xsl:variable name="type" select="/applyeasy/input/types/type/@value"/>
	<xsl:variable name="_create_run_ind" select="/applyeasy/item_type_meta_list/item_type_meta[@id=$type]/@create_run_ind"/>
	<xsl:variable name="cur_page" select="/applyeasy/items/@cur_page"/>
	<xsl:variable name="total" select="/applyeasy/items/@total_search"/>
	<xsl:variable name="page_size" select="/applyeasy/items/@page_size"/>
	<xsl:variable name="search_code" select="/applyeasy/items/@search_code"/>
	<xsl:variable name="srh_timestamp" select="/applyeasy/items/@timestamp"/>
	<xsl:variable name="order_by" select="/applyeasy/input/@orderby"/>
	<xsl:variable name="cur_order" select="/applyeasy/input/@sortorder"/>
	<xsl:variable name="columns" select="/applyeasy/columns"/>
	<xsl:variable name="columns_label" select="/applyeasy/columns_label"/>
	<xsl:variable name="only_open_enrol_now" select="/applyeasy/input/@only_open_enrol_now"/>
	<xsl:variable name="sort_order">
		<xsl:choose>
			<xsl:when test="$cur_order = 'asc' ">desc</xsl:when>
			<xsl:otherwise>asc</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="page_variant" select="/applyeasy/meta/page_variant"/>
	<xsl:variable name="tcr_id_lst">
		<xsl:choose>
			<xsl:when test="/applyeasy/select_type = 1">-1</xsl:when>
			<xsl:otherwise><xsl:value-of select="/applyeasy/input_tcr_lst/tcr/@id"/></xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="status" select="/applyeasy/input/@status"></xsl:variable>
	<xsl:variable name="adv_srh_ind" select="/applyeasy/adv_srh_ind"/>
	<xsl:variable name="training_type" select="/applyeasy/training_type"/>
	<xsl:variable name="is_integrated">
		<xsl:choose>
			<xsl:when test="$training_type = 'INTEGRATED'">true</xsl:when>
			<xsl:otherwise>false</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:call-template name="main"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="main">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_item.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="Javascript" type="text/javascript"><![CDATA[
			itm_lst = new wbItem;
			var training_type=getUrlParam('training_type');
			var status = getUrlParam('status');
			var tcr_id_lst = getUrlParam('tcr_id_lst');
			
			var orderby = getUrlParam('orderby');
			var cur_order = training_type+'~'+orderby;	
			
			function getOrderCookie(type){				
				var itm_order=gen_get_cookie('itm_sort_order');
				var old_order='';
				if(itm_order!=null &&itm_order.length>0){
					var order =itm_order.split(',');
					for(var i=0; i<order.length;i++){
						if(order[i].indexOf(type)!=-1){
							old_order=order[i];
						}				
					}
					
				}
				return old_order;
			}
			
			function getOrderCol(type){
				var old_order=getOrderCookie(type);
				var order = new Array();
				if(old_order!=null && old_order.length>0){
					order=old_order.split('||');
					return order[1];
				}
				return '';
			}
			
			function getOrderSort(type){
				var old_order=getOrderCookie(type);
				var order = new Array();
				if(old_order!=null && old_order.length>0){
					order=old_order.split('||');
					return order[2];
				}
				return '';
			}			
			
			function setOrderCookie(type,col,sort){
				var Days = 365; 
				var exp  = new Date();  
				exp.setTime(exp.getTime() + Days*24*60*60*1000);
				var itm_order=gen_get_cookie('itm_sort_order');
				var order=new Array;
				order =itm_order.split(',');
				var all_itm_order='';
				var old_order='';
				var sortorder='';
				if(itm_order==null ||itm_order.length==0 ){
					all_itm_order=type+'||'+col+'||'+sort; 
				}else{
					if(itm_order.indexOf(type)==-1){
						order[order.length]=type+'||'+col+'||'+sort;
					}
						for(var i=0; i<order.length;i++){
						if(order[i].indexOf(type)!=-1){
							order[i]=type+'||'+col+'||'+sort;	
						}
						if(all_itm_order==''){
							all_itm_order=order[i]
						}else{
							all_itm_order=all_itm_order+','+order[i]
						}					
					}	
				}
				document.cookie = 'itm_sort_order' + '='+ escape (all_itm_order) + ';expires=' + exp.toGMTString();
			}

			function checkstatus(){
				if(event.keyCode ==13){
					itm_lst.simple_search_item_exec(document.frmSearch,]]>'<xsl:value-of select="$wb_lang"/>'<![CDATA[)
				}
				return false;
			}
				
			function changeTrainingType(type){
				orderby=getOrderCol(type);
				sort=getOrderSort(type);	url=wb_utils_invoke_ae_servlet('cmd','ae_lookup_itm','stylesheet','itm_search_result.xsl','all_ind','true','training_type',type,'status',status,'tcr_id_lst',tcr_id_lst,'orderby',orderby,'sortorder',sort);
				window.location.href = url;
			}
			function changeStatus(status){
				url=wb_utils_invoke_ae_servlet('cmd','ae_lookup_itm','stylesheet','itm_search_result.xsl','all_ind','true','status',status ,'training_type',training_type,'tcr_id_lst',tcr_id_lst);
				window.location.href = url;
			}
			function changeTC(tc_id){			
			
				url=wb_utils_invoke_ae_servlet('cmd','ae_lookup_itm','stylesheet','itm_search_result.xsl','all_ind','true','tcr_id_lst',tc_id ,'training_type',training_type,'status',status);
				window.location.href = url;
			}	
			
			function show_content(tcr_id) {
				if (tcr_id && tcr_id == 0) {
					tcr_id = -1;
				}
				changeTC(tcr_id);
			}	
			
			function load_tree() {
				if (frmSearch.tc_enabled_ind) {
					page_onload(250);
				}
			}	
			
		]]></script>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onload="load_tree();" >
			<form name="frmSearch" onsubmit="return false">
				<input type="hidden" name="cmd" value=""/>
				<input type="hidden" name="stylesheet" value=""/>
				<input type="hidden" name="url_failure" value=""/>
				<input type="hidden" name="tnd_id" value="0"/>
				<input type="hidden" name="exact" value="false"/>
				<input type="hidden" name="page_size" value="10"/>
				<input type="hidden" name="page" value="1"/>
				<input type="hidden" name="all_ind" value="true"/>
				<input type="hidden" name="sortorder" value="asc"/>
				<input type="hidden" name="orderby" value="itm_code"/>
				<input type="hidden" name="cat_public_ind" value=""/>
				<input type="hidden" name="tvw_id" value=""/>
				<input type="hidden" name="show_respon" value=""/>
				<input type="hidden" name="training_type" value="{$training_type}"/>
				<input type="hidden" name="tcr_id_lst" value="{$tcr_id_lst}"/>
				<input type="hidden" name="status" value="{$status}"/>
				<xsl:call-template name="wb_init_lab"/>
			</form>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:apply-templates>
			<xsl:with-param name="lab_search_result">搜索結果</xsl:with-param>
			<xsl:with-param name="lab_item_found">找到的課程</xsl:with-param>
			<xsl:with-param name="lab_adv_search">搜尋</xsl:with-param>
			<xsl:with-param name="lab_advanced_search">進階搜尋</xsl:with-param>
			<xsl:with-param name="lab_title">名稱</xsl:with-param>
			<xsl:with-param name="lab_type">類型</xsl:with-param>
			<xsl:with-param name="lab_not_match">很抱歉，找不到符合您檢索條件的結果</xsl:with-param>
			<xsl:with-param name="lab_enrolment">
				<xsl:value-of select="$lab_const_enrollment"/>
			</xsl:with-param>
			<xsl:with-param name="lab_course_nature">課程屬性</xsl:with-param>
			<xsl:with-param name="lab_duration">學分</xsl:with-param>
			<xsl:with-param name="lab_start_date">開始日期</xsl:with-param>
			<xsl:with-param name="lab_end_date">結束日期</xsl:with-param>
			<xsl:with-param name="lab_enrol_start_date">
				<xsl:value-of select="$lab_const_enrollment"/>開始日期</xsl:with-param>
			<xsl:with-param name="lab_enrol_end_date">
				<xsl:value-of select="$lab_const_enrollment"/>結束日期</xsl:with-param>
			<xsl:with-param name="lab_run_title">
				<xsl:value-of select="$lab_const_run"/>
			</xsl:with-param>
			<xsl:with-param name="lab_min_max">最小值/最大值</xsl:with-param>
			<xsl:with-param name="lab_learning_solutions">課程</xsl:with-param>
			<xsl:with-param name="lab_code">編號</xsl:with-param>
			<xsl:with-param name="lab_status">狀態</xsl:with-param>
			<xsl:with-param name="lab_itemstatus">狀態</xsl:with-param>
			<xsl:with-param name="lab_itemlifestatus">批准狀態</xsl:with-param>
			<xsl:with-param name="lab_status_on">已發佈</xsl:with-param>
			<xsl:with-param name="lab_status_off">未發佈</xsl:with-param>
			<xsl:with-param name="lab_learning_solution_search_result">搜尋結果</xsl:with-param>
			<xsl:with-param name="lab_not_sent">* 開課通知已發送</xsl:with-param>
			<xsl:with-param name="lab_star">*</xsl:with-param>
			<xsl:with-param name="lab_tbc">未定</xsl:with-param>
			<xsl:with-param name="lab_req_appr">申請批准</xsl:with-param>
			<xsl:with-param name="lab_in_dev">製作中</xsl:with-param>
			<xsl:with-param name="lab_appr">已批准</xsl:with-param>
			<xsl:with-param name="lab_compulsory">必修</xsl:with-param>
			<xsl:with-param name="lab_elective">選修</xsl:with-param>
			<xsl:with-param name="lab_nomination">需要提名</xsl:with-param>
			<xsl:with-param name="lab_invitation">需要邀請</xsl:with-param>
			<xsl:with-param name="lab_credit">學分</xsl:with-param>
			<xsl:with-param name="lab_created_by">創建者</xsl:with-param>
			<xsl:with-param name="lab_last_modified">修改日期</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_view">顯示</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_edit">修改</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_performance">成績</xsl:with-param>
			<xsl:with-param name="lab_training_center">培訓中心</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_add_lrn_sol">新增</xsl:with-param>
			<xsl:with-param name="lab_manage">管理</xsl:with-param>
			<xsl:with-param name="lab_bar_type">類型</xsl:with-param>
			<xsl:with-param name="lab_bar_status">狀態</xsl:with-param>
			<xsl:with-param name="lab_batch_update">批量修改</xsl:with-param>
			<xsl:with-param name="lab_select_all">--<xsl:value-of select="$lab_all"/>--</xsl:with-param>
			<xsl:with-param name="lab_select_my_tc">我負責的培訓中心</xsl:with-param>
			<xsl:with-param name="lab_search">搜索结果</xsl:with-param>
			<xsl:with-param name="lab_name_no">課程名稱/編號</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:apply-templates>
			<xsl:with-param name="lab_search_result">检索结果</xsl:with-param>
			<xsl:with-param name="lab_item_found">找到的课程</xsl:with-param>
			<xsl:with-param name="lab_adv_search">搜索</xsl:with-param>
			<xsl:with-param name="lab_advanced_search">高级搜索</xsl:with-param>
			<xsl:with-param name="lab_title">名称</xsl:with-param>
			<xsl:with-param name="lab_type">类型</xsl:with-param>
			<xsl:with-param name="lab_not_match">很抱歉，找不到符合您检索条件的结果</xsl:with-param>
			<xsl:with-param name="lab_enrolment">
				<xsl:value-of select="$lab_const_enrollment"/>
			</xsl:with-param>
			<xsl:with-param name="lab_course_nature">课程属性</xsl:with-param>
			<xsl:with-param name="lab_duration">学分</xsl:with-param>
			<xsl:with-param name="lab_start_date">开始日期</xsl:with-param>
			<xsl:with-param name="lab_end_date">结束日期</xsl:with-param>
			<xsl:with-param name="lab_enrol_start_date">
				<xsl:value-of select="$lab_const_enrollment"/>开始日期</xsl:with-param>
			<xsl:with-param name="lab_enrol_end_date">
				<xsl:value-of select="$lab_const_enrollment"/>结束日期</xsl:with-param>
			<xsl:with-param name="lab_run_title">
				<xsl:value-of select="$lab_const_run"/>
			</xsl:with-param>
			<xsl:with-param name="lab_min_max">最小值/最大值</xsl:with-param>
			<xsl:with-param name="lab_learning_solutions">课程</xsl:with-param>
			<xsl:with-param name="lab_code">编号</xsl:with-param>
			<xsl:with-param name="lab_status">
				<xsl:value-of select="$lab_const_run"/>状态</xsl:with-param>
			<xsl:with-param name="lab_itemstatus">状态</xsl:with-param>
			<xsl:with-param name="lab_itemlifestatus">批准状态</xsl:with-param>
			<xsl:with-param name="lab_status_on">已发布</xsl:with-param>
			<xsl:with-param name="lab_status_off">未发布</xsl:with-param>
			<xsl:with-param name="lab_learning_solution_search_result">搜索结果</xsl:with-param>
			<xsl:with-param name="lab_not_sent">* 开课通知已发送</xsl:with-param>
			<xsl:with-param name="lab_star">*</xsl:with-param>
			<xsl:with-param name="lab_tbc">未定</xsl:with-param>
			<xsl:with-param name="lab_req_appr">申请批准</xsl:with-param>
			<xsl:with-param name="lab_in_dev">制作中</xsl:with-param>
			<xsl:with-param name="lab_appr">已批准</xsl:with-param>
			<xsl:with-param name="lab_compulsory">必修</xsl:with-param>
			<xsl:with-param name="lab_elective">选修</xsl:with-param>
			<xsl:with-param name="lab_nomination">需要提名</xsl:with-param>
			<xsl:with-param name="lab_invitation">需要邀请</xsl:with-param>
			<xsl:with-param name="lab_credit">学分</xsl:with-param>
			<xsl:with-param name="lab_created_by">创建者</xsl:with-param>
			<xsl:with-param name="lab_last_modified">修改日期</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_view">显示</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_edit">修改</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_performance">成绩</xsl:with-param>
			<xsl:with-param name="lab_training_center">培训中心</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_add_lrn_sol">新增</xsl:with-param>
			<xsl:with-param name="lab_manage">管理</xsl:with-param>
			<xsl:with-param name="lab_bar_type">类型</xsl:with-param>
			<xsl:with-param name="lab_bar_status">状态</xsl:with-param>
			<xsl:with-param name="lab_select_all">--<xsl:value-of select="$lab_all"/>--</xsl:with-param>
			<xsl:with-param name="lab_batch_update">批量修改</xsl:with-param>
			<xsl:with-param name="lab_select_my_tc">我负责的培训中心</xsl:with-param>
			<xsl:with-param name="lab_search">搜索结果</xsl:with-param>
			<xsl:with-param name="lab_name_no">课程名称/编号</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:apply-templates>
			<xsl:with-param name="lab_search_result">Search result</xsl:with-param>
			<xsl:with-param name="lab_item_found">items found</xsl:with-param>
			<xsl:with-param name="lab_adv_search"> search</xsl:with-param>
			<xsl:with-param name="lab_advanced_search">Advanced search</xsl:with-param>
			<xsl:with-param name="lab_title">Title</xsl:with-param>
			<xsl:with-param name="lab_type">Type</xsl:with-param>
			<xsl:with-param name="lab_not_match">No results found</xsl:with-param>
			<xsl:with-param name="lab_enrolment">
				<xsl:value-of select="$lab_const_enrollment"/>
			</xsl:with-param>
			<xsl:with-param name="lab_course_nature">Course nature</xsl:with-param>
			<xsl:with-param name="lab_duration">Duration</xsl:with-param>
			<xsl:with-param name="lab_start_date">Start date</xsl:with-param>
			<xsl:with-param name="lab_end_date">End date</xsl:with-param>
			<xsl:with-param name="lab_enrol_start_date">
				<xsl:value-of select="$lab_const_enrollment"/> start date</xsl:with-param>
			<xsl:with-param name="lab_enrol_end_date">
				<xsl:value-of select="$lab_const_enrollment"/> end date</xsl:with-param>
			<xsl:with-param name="lab_run_title">
				<xsl:value-of select="$lab_const_run"/>
			</xsl:with-param>
			<xsl:with-param name="lab_min_max">Min/max</xsl:with-param>
			<xsl:with-param name="lab_learning_solutions">Learning solution</xsl:with-param>
			<xsl:with-param name="lab_code">Code</xsl:with-param>
			<xsl:with-param name="lab_status">
				<xsl:value-of select="$lab_const_run"/> status</xsl:with-param>
			<xsl:with-param name="lab_itemstatus">Status</xsl:with-param>
			<xsl:with-param name="lab_itemlifestatus">Approval status</xsl:with-param>
			<xsl:with-param name="lab_status_on">Published</xsl:with-param>
			<xsl:with-param name="lab_status_off">Unpublished</xsl:with-param>
			<xsl:with-param name="lab_learning_solution_search_result">Learning solution search results</xsl:with-param>
			<xsl:with-param name="lab_not_sent">* JI has been sent</xsl:with-param>
			<xsl:with-param name="lab_star">*</xsl:with-param>
			<xsl:with-param name="lab_tbc">TBC</xsl:with-param>
			<xsl:with-param name="lab_req_appr">Request approval</xsl:with-param>
			<xsl:with-param name="lab_in_dev">In development</xsl:with-param>
			<xsl:with-param name="lab_appr">Approved</xsl:with-param>
			<xsl:with-param name="lab_compulsory">Compulsory</xsl:with-param>
			<xsl:with-param name="lab_elective">Elective</xsl:with-param>
			<xsl:with-param name="lab_nomination">By nomination</xsl:with-param>
			<xsl:with-param name="lab_invitation">By invitation</xsl:with-param>
			<xsl:with-param name="lab_credit">Credit</xsl:with-param>
			<xsl:with-param name="lab_created_by">Created by</xsl:with-param>
			<xsl:with-param name="lab_last_modified">Modified</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_view">View</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_edit">Edit</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_performance">Performance</xsl:with-param>
			<xsl:with-param name="lab_training_center">Training center</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_add_lrn_sol">Add </xsl:with-param>
			<xsl:with-param name="lab_manage"> management</xsl:with-param>
			<xsl:with-param name="lab_bar_type">Type</xsl:with-param>
			<xsl:with-param name="lab_bar_status">Status</xsl:with-param>
			<xsl:with-param name="lab_batch_update">Batch editing</xsl:with-param>
			<xsl:with-param name="lab_select_all">-- <xsl:value-of select="$lab_all"/> --</xsl:with-param>
			<xsl:with-param name="lab_select_my_tc">My training centers</xsl:with-param>
			<xsl:with-param name="lab_search">Search result</xsl:with-param>
			<xsl:with-param name="lab_name_no">Course/Number</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="applyeasy">
		<xsl:param name="lab_adv_search"/>
		<xsl:param name="lab_advanced_search"/>
		<xsl:param name="lab_title"/>
		<xsl:param name="lab_type"/>
		<xsl:param name="lab_not_match"/>
		<xsl:param name="lab_start_date"/>
		<xsl:param name="lab_end_date"/>
		<xsl:param name="lab_enrol_start_date"/>
		<xsl:param name="lab_enrol_end_date"/>
		<xsl:param name="lab_run_title"/>
		<xsl:param name="lab_min_max"/>
		<xsl:param name="lab_learning_solutions"/>
		<xsl:param name="lab_search_result"/>
		<xsl:param name="lab_code"/>
		<xsl:param name="lab_status"/>
		<xsl:param name="lab_itemstatus"/>
		<xsl:param name="lab_itemlifestatus"/>
		<xsl:param name="lab_status_on"/>
		<xsl:param name="lab_status_off"/>
		<xsl:param name="lab_learning_solution_search_result"/>
		<xsl:param name="lab_not_sent"/>
		<xsl:param name="lab_star"/>
		<xsl:param name="lab_tbc"/>
		<xsl:param name="lab_in_dev"/>
		<xsl:param name="lab_req_appr"/>
		<xsl:param name="lab_appr"/>
		<xsl:param name="lab_compulsory"/>
		<xsl:param name="lab_elective"/>
		<xsl:param name="lab_nomination"/>
		<xsl:param name="lab_invitation"/>
		<xsl:param name="lab_duration"/>
		<xsl:param name="lab_credit"/>
		<xsl:param name="lab_item_found"/>
		<xsl:param name="lab_created_by"/>
		<xsl:param name="lab_last_modified"/>
		<xsl:param name="lab_g_txt_btn_view"/>
		<xsl:param name="lab_g_txt_btn_edit"/>
		<xsl:param name="lab_g_txt_btn_performance"/>
		<xsl:param name="lab_enrolment"/>
		<xsl:param name="lab_course_nature"/>
		<xsl:param name="lab_training_center"/>
		<xsl:param name="lab_g_txt_btn_add_lrn_sol"/>
		<xsl:param name="lab_manage"/>	
		<xsl:param name="lab_bar_type"/>
		<xsl:param name="lab_bar_status"/>
		<xsl:param name="lab_batch_update"/>
		<xsl:param name="lab_select_all"/>
		<xsl:param name="lab_select_my_tc"/>
		<xsl:param name="lab_search"/>
		<xsl:param name="lab_name_no"/>
		
		<xsl:param name="test" select='$training_type'></xsl:param>
		
		<xsl:variable name="lab_training_type">
			<xsl:call-template name="get_lab">
				<xsl:with-param name="lab_title" select="$test">
				</xsl:with-param>
			</xsl:call-template>
		</xsl:variable>
		<xsl:variable name="lab_manage_type">
			<xsl:value-of select="$lab_search"/>		
		</xsl:variable>
		<xsl:variable name="lab_ins_training">
			<xsl:value-of select="$lab_g_txt_btn_add_lrn_sol"/>
			<xsl:choose>
				<xsl:when test="$training_type = 'ALL'">
					<xsl:value-of select="$lab_search"></xsl:value-of>
				</xsl:when>
				<xsl:otherwise><xsl:value-of select="translate($lab_training_type,$uppercase,$lowercase)"/></xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_ITM_COS_MAIN</xsl:with-param>
			<xsl:with-param name="parent_code">FTN_AMD_CAT_MAIN</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text" select="$lab_manage_type"/>
		</xsl:call-template>
		<xsl:call-template name="search_bar">
			<xsl:with-param name="lab_adv_search" select="$lab_adv_search"/>
			<xsl:with-param name="lab_advanced_search" select="$lab_advanced_search"/>
			<xsl:with-param name="lab_ins_training" select="$lab_ins_training"/>
			<xsl:with-param name="lab_bar_type" select="$lab_bar_type"/>
			<xsl:with-param name="lab_bar_status" select="$lab_bar_status"/>
			<xsl:with-param name="lab_training_center" select="$lab_training_center"/>
			<xsl:with-param name="lab_status_on" select="$lab_status_on"/>
			<xsl:with-param name="lab_status_off" select="$lab_status_off"/>
			<xsl:with-param name="lab_select_all" select="$lab_select_all"/>
			<xsl:with-param name="lab_select_my_tc" select="$lab_select_my_tc"/>
			<xsl:with-param name="lab_batch_update" select="$lab_batch_update"/>
			<xsl:with-param name="lab_name_no" select="$lab_name_no"/>
		</xsl:call-template>
		<xsl:apply-templates select="items">
			<xsl:with-param name="lab_title" select="$lab_title"/>
			<xsl:with-param name="lab_type" select="$lab_type"/>
			<xsl:with-param name="lab_not_match" select="$lab_not_match"/>
			<xsl:with-param name="lab_enrolment" select="$lab_enrolment"/>
			<xsl:with-param name="lab_course_nature" select="$lab_course_nature"/>
			<xsl:with-param name="lab_duration" select="$lab_duration"/>
			<xsl:with-param name="lab_credit" select="$lab_credit"/>
			<xsl:with-param name="lab_start_date" select="$lab_start_date"/>
			<xsl:with-param name="lab_end_date" select="$lab_end_date"/>
			<xsl:with-param name="lab_enrol_start_date" select="$lab_enrol_start_date"/>
			<xsl:with-param name="lab_enrol_end_date" select="$lab_enrol_end_date"/>
			<xsl:with-param name="lab_run_title" select="$lab_run_title"/>
			<xsl:with-param name="lab_min_max" select="$lab_min_max"/>
			<xsl:with-param name="lab_learning_solutions" select="$lab_learning_solutions"/>
			<xsl:with-param name="lab_status" select="$lab_status"/>
			<xsl:with-param name="lab_itemstatus" select="$lab_itemstatus"/>
			<xsl:with-param name="lab_itemlifestatus" select="$lab_itemlifestatus"/>
			<xsl:with-param name="lab_code" select="$lab_code"/>
			<xsl:with-param name="lab_status_on" select="$lab_status_on"/>
			<xsl:with-param name="lab_status_off" select="$lab_status_off"/>
			<xsl:with-param name="lab_not_sent" select="$lab_not_sent"/>
			<xsl:with-param name="lab_star" select="$lab_star"/>
			<xsl:with-param name="lab_tbc" select="$lab_tbc"/>
			<xsl:with-param name="lab_in_dev" select="$lab_in_dev"/>
			<xsl:with-param name="lab_req_appr" select="$lab_req_appr"/>
			<xsl:with-param name="lab_appr" select="$lab_appr"/>
			<xsl:with-param name="lab_compulsory" select="$lab_compulsory"/>
			<xsl:with-param name="lab_elective" select="$lab_elective"/>
			<xsl:with-param name="lab_nomination" select="$lab_nomination"/>
			<xsl:with-param name="lab_invitation" select="$lab_invitation"/>
			<xsl:with-param name="lab_created_by" select="$lab_created_by"/>
			<xsl:with-param name="lab_last_modified" select="$lab_last_modified"/>
			<xsl:with-param name="lab_g_txt_btn_view" select="$lab_g_txt_btn_view"/>
			<xsl:with-param name="lab_g_txt_btn_edit" select="$lab_g_txt_btn_edit"/>
			<xsl:with-param name="lab_g_txt_btn_performance" select="$lab_g_txt_btn_performance"/>
			<xsl:with-param name="lab_training_center" select="$lab_training_center"/>
		</xsl:apply-templates>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="search_bar">
		<xsl:param name="lab_adv_search"/>
		<xsl:param name="lab_advanced_search"/>
		<xsl:param name="lab_ins_training"/>
		<xsl:param name="lab_bar_type"/>
		<xsl:param name="lab_bar_status"/>
		<xsl:param name="lab_training_center"/>
		<xsl:param name="lab_status_on"/>
		<xsl:param name="lab_status_off"/>
		<xsl:param name="lab_select_all"/>
		<xsl:param name="lab_select_my_tc"/>
		<xsl:param name="lab_batch_update"/>
		<xsl:param name="lab_name_no"/>
		
		<xsl:variable name="search_table_id">search_bar</xsl:variable>
		
		<table id="{$search_table_id}">
			<tr>
				<td align="right">
					<table cellpadding="1">
						<tr>
							<td nowrap="nowrap" align="right">
								<div class="wzb-form-search" style="display: inline-block;">
									<input type="text" size="11" name="title_code" class="form-control" style="width:130px;" placeholder="{$lab_name_no}" onkeypress="checkstatus()" value="{$search_code}"/>
									<input type="button" class="form-submit" value="" onclick="itm_lst.simple_search_item_exec(document.frmSearch, '{$wb_lang}')"/>
								</div>								
								<a href="javascript:itm_lst.search_item_prep()" class="btn wzb-btn-blue margin-right10 vbtm" style="height:27px;">
									<xsl:value-of select="$lab_advanced_search"/>
								</a>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</xsl:template>
	<xsl:template name="gen_header">
		<xsl:param name="extra"/>
		<xsl:param name="s_db_col"/>
		<xsl:param name="id_"/>
		<xsl:variable name="td_width">
			<xsl:choose>
				<xsl:when test="$columns_label/col[@id=$id_]/@id = 'title'">40%</xsl:when>
				<xsl:otherwise>12%</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<td width="{$td_width}">
			<xsl:if test="$columns_label/col[@id=$id_]/@id != 'enrolled'"> <!-- 排除已报名 -->
			<xsl:choose>
				<xsl:when test="@extra='no'">
						<xsl:choose>
							<xsl:when test="$order_by =$s_db_col">
								<a href="javascript:setOrderCookie('{$training_type}','{$s_db_col}','{$sort_order}');itm_lst.sort_item_list('{$s_db_col}','{$sort_order}')" class="TitleText">
									<xsl:value-of select="$columns_label/col[@id=$id_]/@label"/>
									<xsl:call-template name="wb_sortorder_cursor">
										<xsl:with-param name="img_path">
											<xsl:value-of select="$wb_img_path"/>
										</xsl:with-param>
										<xsl:with-param name="sort_order">
											<xsl:value-of select="$cur_order"/>
										</xsl:with-param>
									</xsl:call-template>
								</a>
							</xsl:when>
							<xsl:otherwise>
								<a href="javascript:setOrderCookie('{$training_type}','{$s_db_col}','asc');itm_lst.sort_item_list('{$s_db_col}','asc')" class="TitleText">
									<xsl:value-of select="$columns_label/col[@id=$id_]/@label"/>
								</a>
							</xsl:otherwise>
						</xsl:choose>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$columns_label/col[@id=$id_]/@label"/>
				</xsl:otherwise>
			</xsl:choose>
			</xsl:if>
		</td>	
	</xsl:template>
	<xsl:template name="gen_desc">
		<xsl:param name="id_"/>
		<xsl:param name="item"/>
		<xsl:param name="type_"/>
		<xsl:param name="lab_status_on"/>
		<xsl:param name="lab_status_off"/>
		<xsl:param name="href" />
		<xsl:choose>
			<xsl:when test="$type_='timestamp'">
				<td width="12%">
					<xsl:variable name="lab_unlimited">
						<xsl:choose>
							<xsl:when test="$wb_lang='ch'">不限</xsl:when>
							<xsl:when test="$wb_lang='gb'">不限</xsl:when>
							<xsl:otherwise>Unlimited</xsl:otherwise>
						</xsl:choose>
					</xsl:variable>
					<xsl:variable name="value_" select="$item/*[name()=$id_]"/>
					<xsl:choose>
						<xsl:when test="$id_='appn_end_datetime' and contains($value_,'9999-12-31 23:59:59')='true'">
							<xsl:value-of select="$lab_unlimited"/>
						</xsl:when>
						<xsl:when test="$value_ !=''">
							<xsl:call-template name="display_time">
								<xsl:with-param name="my_timestamp">
									<xsl:value-of select="$item/*[name()=$id_]"/>
								</xsl:with-param>
							</xsl:call-template>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text>--</xsl:text>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</xsl:when>
			<xsl:when test="$id_='dummy_type'">
				<td width="12%">
					<xsl:variable name="value_" select="$item/*[name()=$id_]"/>
					<xsl:call-template name="get_ity_title">
						<xsl:with-param name="dummy_type" select="$value_"/>
					</xsl:call-template>
				</td>
			</xsl:when>
			<xsl:when test="$id_='status'">
				<td width="12%">
					<xsl:variable name="value_" select="$item/*[name()=$id_]"/>
						<xsl:choose>
							<xsl:when test="$value_ = 'ON'">
								<xsl:value-of select="$lab_status_on"/>
							</xsl:when>
							<xsl:when test="$value_ = 'OFF'">
								<xsl:value-of select="$lab_status_off"/>
							</xsl:when>
						</xsl:choose>
					<xsl:text>&#160;</xsl:text>
				</td>
			</xsl:when>
			<xsl:when test="$id_='title'">
				<td width="12%">
					<a>
						<xsl:attribute name="href"><xsl:value-of select="$href" /></xsl:attribute>
						<xsl:if test="$columns_label/col[@id=$id_]/@id != 'enrolled'"> <!-- 排除已报名 -->
							<xsl:choose>
								<xsl:when test="$item/*[name()=$id_] != '' and $item/*[name()=$id_] !='0'">
									<xsl:value-of select="$item/*[name()=$id_]"/>
								</xsl:when>
								<xsl:when test="$item/class/*[name()=$id_] !='' and $item/class/*[name()=$id_] !='0'">
									<xsl:value-of select="$item/class/*[name()=$id_]"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:text></xsl:text>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:if>
					</a>
				</td>
			</xsl:when>
			<xsl:otherwise>
				<td width="12%">
					<xsl:if test="$columns_label/col[@id=$id_]/@id != 'enrolled'"> <!-- 排除已报名 -->
						<xsl:choose>
							<xsl:when test="$item/*[name()=$id_] != '' and $item/*[name()=$id_] !='0'">
								<xsl:value-of select="$item/*[name()=$id_]"/>
							</xsl:when>
							<xsl:when test="$item/class/*[name()=$id_] !='' and $item/class/*[name()=$id_] !='0'">
								<xsl:value-of select="$item/class/*[name()=$id_]"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:text></xsl:text>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:if>
				</td>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="items">
		<xsl:param name="lab_title"/>
		<xsl:param name="lab_type"/>
		<xsl:param name="lab_not_match"/>
		<xsl:param name="lab_start_date"/>
		<xsl:param name="lab_end_date"/>
		<xsl:param name="lab_enrol_start_date"/>
		<xsl:param name="lab_enrol_end_date"/>
		<xsl:param name="lab_run_title"/>
		<xsl:param name="lab_min_max"/>
		<xsl:param name="lab_code"/>
		<xsl:param name="lab_status"/>
		<xsl:param name="lab_itemstatus"/>
		<xsl:param name="lab_itemlifestatus"/>
		<xsl:param name="lab_status_on"/>
		<xsl:param name="lab_status_off"/>
		<xsl:param name="lab_not_sent"/>
		<xsl:param name="lab_star"/>
		<xsl:param name="lab_tbc"/>
		<xsl:param name="lab_in_dev"/>
		<xsl:param name="lab_req_appr"/>
		<xsl:param name="lab_appr"/>
		<xsl:param name="lab_compulsory"/>
		<xsl:param name="lab_elective"/>
		<xsl:param name="lab_nomination"/>
		<xsl:param name="lab_invitation"/>
		<xsl:param name="lab_duration"/>
		<xsl:param name="lab_credit"/>
		<xsl:param name="lab_created_by"/>
		<xsl:param name="lab_last_modified"/>
		<xsl:param name="lab_g_txt_btn_view"/>
		<xsl:param name="lab_g_txt_btn_edit"/>
		<xsl:param name="lab_g_txt_btn_performance"/>
		<xsl:param name="lab_training_center"/>
		<xsl:choose>
			<xsl:when test="count(item) = 0">
				<xsl:call-template name="wb_ui_show_no_item">
					<xsl:with-param name="text" select="$lab_not_match"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<table class="table wzb-ui-table">
					<tr class="wzb-ui-table-head">
						<xsl:for-each select="$columns/col">
							<xsl:variable name="s_db_col" select="db_col/@name"/>
							<xsl:variable name="id_" select="@id"/>
							<xsl:if test="$s_db_col = 'r_itm_title'">
								<xsl:call-template name="gen_header">
									<xsl:with-param name="s_db_col" select="$s_db_col"/>
									<xsl:with-param name="id_" select="$id_"/>
									<xsl:with-param name="extra" select="@extra"/>
								</xsl:call-template>
							</xsl:if>
						</xsl:for-each>
						<xsl:for-each select="$columns/col">
							<xsl:if test="@active='true'">
							<xsl:variable name="s_db_col" select="db_col/@name"/>
							<xsl:variable name="id_" select="@id"/>
							<xsl:if test="$s_db_col != 'r_itm_title' and $s_db_col != 'r_itm_eff_start_datetime' and $s_db_col != 'r_itm_eff_end_datetime'">
							<xsl:choose>
								<xsl:when test="$s_db_col='r_itm_status'">
									<xsl:if test="$page_variant/@ShowItemStatus = 'true'">
										<xsl:call-template name="gen_header">
											<xsl:with-param name="s_db_col" select="$s_db_col"/>
											<xsl:with-param name="id_" select="$id_"/>
											<xsl:with-param name="extra" select="@extra"/>
										</xsl:call-template>
									</xsl:if>
								</xsl:when>
								<xsl:when test="$s_db_col='r_itm_capacity'">
									<xsl:if test="$page_variant/@ShowItemQuota = 'true'">
										<xsl:call-template name="gen_header">
											<xsl:with-param name="s_db_col" select="$s_db_col"/>
											<xsl:with-param name="id_" select="$id_"/>
											<xsl:with-param name="extra" select="@extra"/>
										</xsl:call-template>
									</xsl:if>
								</xsl:when>
								<xsl:when test="$s_db_col='r_enrolled'">
									<xsl:if test="$page_variant/@ShowItemEnrolled = 'true' and $training_type !='REF'">
										<xsl:call-template name="gen_header">
											<xsl:with-param name="s_db_col" select="$s_db_col"/>
											<xsl:with-param name="id_" select="$id_"/>
											<xsl:with-param name="extra" select="@extra"/>
										</xsl:call-template>
									</xsl:if>
								</xsl:when>
								<xsl:when test="($s_db_col='c_itm_code' or $s_db_col='c_itm_title')">
									<xsl:if test="$only_open_enrol_now = 'true'">
										<xsl:call-template name="gen_header">
											<xsl:with-param name="s_db_col" select="$s_db_col"/>
											<xsl:with-param name="id_" select="$id_"/>
											<xsl:with-param name="extra" select="@extra"/>
										</xsl:call-template>
									</xsl:if>
								</xsl:when>
								<xsl:when test="$s_db_col='create_usr_id' or $s_db_col='upd_timestamp'">
									<xsl:if test="$page_variant/@ShowItemUpdateTime = 'true'">
										<xsl:call-template name="gen_header">
											<xsl:with-param name="s_db_col" select="$s_db_col"/>
											<xsl:with-param name="id_" select="$id_"/>
											<xsl:with-param name="extra" select="@extra"/>
										</xsl:call-template>
									</xsl:if>
								</xsl:when>
								<xsl:when test="$s_db_col='tcr_title'">
									<xsl:if test="$page_variant/@ShowItemTrainingCenter = 'true'">
										<xsl:call-template name="gen_header">
											<xsl:with-param name="s_db_col" select="$s_db_col"/>
											<xsl:with-param name="id_" select="$id_"/>
											<xsl:with-param name="extra" select="@extra"/>
										</xsl:call-template>
									</xsl:if>
								</xsl:when>
								<xsl:when test="$s_db_col='r_itm_appn_start_datetime' or $s_db_col='r_itm_appn_end_datetime' or $s_db_col='r_itm_eff_start_datetime' or $s_db_col='r_itm_eff_end_datetime' ">
									<xsl:if test="$training_type !='REF'">
										<xsl:call-template name="gen_header">
											<xsl:with-param name="s_db_col" select="$s_db_col"/>
											<xsl:with-param name="id_" select="$id_"/>
											<xsl:with-param name="extra" select="@extra"/>
										</xsl:call-template>
									</xsl:if>
								</xsl:when>
								<xsl:otherwise>
									<xsl:if test="$s_db_col!='r_itm_upd_timestamp'">
										<xsl:call-template name="gen_header">
											<xsl:with-param name="s_db_col" select="$s_db_col"/>
											<xsl:with-param name="id_" select="$id_"/>
											<xsl:with-param name="extra" select="@extra"/>
										</xsl:call-template>
									</xsl:if>
								</xsl:otherwise>
							</xsl:choose>
							</xsl:if>
						</xsl:if>
						</xsl:for-each>
					</tr>
					<xsl:for-each select="item">
						<xsl:variable name="row_class">
							<xsl:choose>
								<xsl:when test="position() mod 2">RowsOdd</xsl:when>
								<xsl:otherwise>RowsEven</xsl:otherwise>
							</xsl:choose>
						</xsl:variable>
						<tr>
						<xsl:variable name="item" select="."/>
						<xsl:variable name="title_">
							<xsl:choose>
								<xsl:when test="class/title"><xsl:value-of select="class/title"/></xsl:when>
								<xsl:otherwise><xsl:value-of select="title"/></xsl:otherwise>
							</xsl:choose>
						</xsl:variable>
						<xsl:variable name="title_escaped">
							<xsl:call-template name="escape_js">
								<xsl:with-param name="input_str">
									<xsl:value-of select="$title_"/>
								</xsl:with-param>
							</xsl:call-template>
						</xsl:variable>
						<xsl:variable name="id_escaped">
							<xsl:choose>
								<xsl:when test="class/@id"><xsl:value-of select="class/@id"/></xsl:when>
								<xsl:otherwise><xsl:value-of select="@id"/></xsl:otherwise>
							</xsl:choose>
						</xsl:variable>
						<xsl:for-each select="$columns/col">
							<xsl:variable name="s_db_col" select="db_col/@name"/>
							<xsl:variable name="type_" select="db_col/@type"/>								
							<xsl:variable name="id_" select="@id"/>
							<xsl:if  test="$s_db_col='r_itm_title' and @active='true'">
								<xsl:call-template name="gen_desc">
										<xsl:with-param name="id_" select="$id_"/>
										<xsl:with-param name="item" select="$item"/>
										<xsl:with-param name="type_" select="$type_"/>
										<xsl:with-param name="lab_status_on" select="$lab_status_on"/>
										<xsl:with-param name="lab_status_off" select="$lab_status_off"/>
										<xsl:with-param name="href">
											<xsl:variable name="page_variant" select="/applyeasy/meta/page_variant"/>
											<xsl:choose>
												<xsl:when test="$page_variant/@hasItemDtlLink = 'true'">
													<xsl:choose>
														<xsl:when test="class/@id">javascript:itm_lst.get_item_run_detail(<xsl:value-of select="$id_escaped"/>)</xsl:when>
														<xsl:otherwise>javascript:itm_lst.get_item_detail(<xsl:value-of select="$id_escaped"/>,'<xsl:value-of select="$title_escaped"/>')</xsl:otherwise>
													</xsl:choose>
												</xsl:when>
												<xsl:otherwise>javascript:itm_lst.get_item_detail(<xsl:value-of select="$id_escaped"/>)</xsl:otherwise>
											</xsl:choose>
										</xsl:with-param>
									</xsl:call-template>
							</xsl:if>
						</xsl:for-each>
						<xsl:for-each select="$columns/col">
							<xsl:if test="@active='true' and db_col/@name !='r_itm_title' and db_col/@name != 'r_itm_eff_start_datetime' and db_col/@name != 'r_itm_eff_end_datetime'">
								<xsl:variable name="s_db_col" select="db_col/@name"/>
								<xsl:variable name="type_" select="db_col/@type"/>								
								<xsl:variable name="id_" select="@id"/>
								<xsl:choose>
									<xsl:when test="$s_db_col='r_itm_status'">
										<xsl:if test="$page_variant/@ShowItemStatus = 'true'">
											<xsl:call-template name="gen_desc">
												<xsl:with-param name="id_" select="$id_"/>
												<xsl:with-param name="item" select="$item"/>
												<xsl:with-param name="type_" select="$type_"/>
												<xsl:with-param name="lab_status_on" select="$lab_status_on"/>
												<xsl:with-param name="lab_status_off" select="$lab_status_off"/>
											</xsl:call-template>
										</xsl:if>
									</xsl:when>
									<xsl:when test="$s_db_col='r_itm_capacity'">
										<xsl:if test="$page_variant/@ShowItemQuota = 'true'">
											<xsl:call-template name="gen_desc">
												<xsl:with-param name="id_" select="$id_"/>
												<xsl:with-param name="item" select="$item"/>
												<xsl:with-param name="type_" select="$type_"/>
												<xsl:with-param name="lab_status_on" select="$lab_status_on"/>
												<xsl:with-param name="lab_status_off" select="$lab_status_off"/>
											</xsl:call-template>
										</xsl:if>
									</xsl:when>
									<xsl:when test="$s_db_col='r_enrolled'">
										<xsl:if test="$page_variant/@ShowItemEnrolled = 'true' and $training_type!='REF'">
											<xsl:call-template name="gen_desc">
												<xsl:with-param name="id_" select="$id_"/>
												<xsl:with-param name="item" select="$item"/>
												<xsl:with-param name="type_" select="$type_"/>
												<xsl:with-param name="lab_status_on" select="$lab_status_on"/>
												<xsl:with-param name="lab_status_off" select="$lab_status_off"/>
											</xsl:call-template>
										</xsl:if>
									</xsl:when>
									<xsl:when test="($s_db_col='c_itm_code' or $s_db_col='c_itm_title')">
										<xsl:if test="$only_open_enrol_now = 'true'">
											<xsl:call-template name="gen_desc">
												<xsl:with-param name="id_" select="$id_"/>
												<xsl:with-param name="item" select="$item"/>
												<xsl:with-param name="type_" select="$type_"/>
												<xsl:with-param name="lab_status_on" select="$lab_status_on"/>
												<xsl:with-param name="lab_status_off" select="$lab_status_off"/>
											</xsl:call-template>
										</xsl:if>
									</xsl:when>

									<xsl:when test="$s_db_col='create_usr_id' or $s_db_col='upd_timestamp'">
										<xsl:if test="$page_variant/@ShowItemUpdateTime = 'true'">
											<xsl:call-template name="gen_desc">
												<xsl:with-param name="id_" select="$id_"/>
												<xsl:with-param name="item" select="$item"/>
												<xsl:with-param name="type_" select="$type_"/>
												<xsl:with-param name="lab_status_on" select="$lab_status_on"/>
												<xsl:with-param name="lab_status_off" select="$lab_status_off"/>
											</xsl:call-template>
										</xsl:if>
									</xsl:when>
									<xsl:when test="$s_db_col='tcr_title'">
										<xsl:if test="$page_variant/@ShowItemTrainingCenter = 'true'">
											<xsl:call-template name="gen_desc">
												<xsl:with-param name="id_" select="$id_"/>
												<xsl:with-param name="item" select="$item"/>
												<xsl:with-param name="type_" select="$type_"/>
												<xsl:with-param name="lab_status_on" select="$lab_status_on"/>
												<xsl:with-param name="lab_status_off" select="$lab_status_off"/>
											</xsl:call-template>
										</xsl:if>
									</xsl:when>
									<xsl:when test="$s_db_col='r_itm_appn_start_datetime' or $s_db_col='r_itm_appn_end_datetime' or $s_db_col='r_itm_eff_start_datetime' or $s_db_col='r_itm_eff_end_datetime' ">
										<xsl:if test="$training_type!='REF'">
											<xsl:call-template name="gen_desc">
											<xsl:with-param name="id_" select="$id_"/>
											<xsl:with-param name="item" select="$item"/>
											<xsl:with-param name="type_" select="$type_"/>
											<xsl:with-param name="lab_status_on" select="$lab_status_on"/>
											<xsl:with-param name="lab_status_off" select="$lab_status_off"/>
										</xsl:call-template>
										</xsl:if>
									</xsl:when>
									<xsl:otherwise>
										<xsl:if test="$s_db_col!='r_itm_upd_timestamp'">
											<xsl:call-template name="gen_desc">
												<xsl:with-param name="id_" select="$id_"/>
												<xsl:with-param name="item" select="$item"/>
												<xsl:with-param name="type_" select="$type_"/>
												<xsl:with-param name="lab_status_on" select="$lab_status_on"/>
												<xsl:with-param name="lab_status_off" select="$lab_status_off"/>
											</xsl:call-template>
										</xsl:if>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:if>
						</xsl:for-each>		
													
					</tr>
					</xsl:for-each>
				</table>
				<xsl:call-template name="wb_ui_pagination">
					<xsl:with-param name="cur_page" select="$cur_page"/>
					<xsl:with-param name="page_size" select="$page_size"/>
					<xsl:with-param name="total" select="$total"/>
					<xsl:with-param name="width" select="$wb_gen_table_width"/>
					<xsl:with-param name="cur_page_name">page</xsl:with-param>
					<xsl:with-param name="timestamp" select="$srh_timestamp"/>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="template_view" mode="header">
		<xsl:apply-templates select="*" mode="title"/>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="*" mode="title">
		<xsl:variable name="father_name">
			<xsl:value-of select="name(..)"/>
		</xsl:variable>
		<xsl:variable name="grandfather_name">
			<xsl:value-of select="name(../..)"/>
		</xsl:variable>
		<xsl:if test="(name() = 'title') and ($father_name != $grandfather_name)">
			<td>
				<xsl:value-of select="desc[@lan = $wb_lang_encoding]/@name"/>
			</td>
		</xsl:if>
		<xsl:apply-templates select="*" mode="title"/>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="valued_template" mode="row">
		<xsl:param name="id"/>
		<xsl:for-each select="section/*">
			<xsl:apply-templates select="." mode="row">
				<xsl:with-param name="pos" select="position()"/>
				<xsl:with-param name="id" select="$id"/>
			</xsl:apply-templates>
		</xsl:for-each>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="*" mode="row">
		<xsl:param name="if"/>
		<xsl:param name="pos"/>
		<xsl:param name="id"/>
		<xsl:variable name="father_name">
			<xsl:value-of select="name(..)"/>
		</xsl:variable>
		<xsl:variable name="grandfather_name">
			<xsl:value-of select="name(../..)"/>
		</xsl:variable>
		<xsl:if test="(name() = 'title') and ($father_name != $grandfather_name)">
			<td>
				<xsl:choose>
					<xsl:when test="../@value">
						<xsl:choose>
							<xsl:when test="../@type = 'url'">
								<a href="{../@value}" target="_blank">
									<xsl:value-of select="../@value"/>
								</a>
							</xsl:when>
							<xsl:when test="../@type = 'date'">
								<xsl:call-template name="display_time">
									<xsl:with-param name="my_timestamp">
										<xsl:value-of select="../@value"/>
									</xsl:with-param>
								</xsl:call-template>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="../@value"/>&#160;</xsl:otherwise>
						</xsl:choose>
					</xsl:when>
					<xsl:when test="../@type = 'catalog_attachment'">
						<!--xsl:value-of select="../node_list/node/nav/node[position()=last()-1]/title"/-->
						<xsl:for-each select="../node_list/node">
							<xsl:for-each select="nav/node">
								<xsl:if test="position() != last()">
									<xsl:value-of select="title"/>
									<xsl:if test="position() != last()-1"> &gt; </xsl:if>
								</xsl:if>
							</xsl:for-each>
							<xsl:if test="position() != last()"> ; </xsl:if>
						</xsl:for-each>
					</xsl:when>
					<xsl:otherwise>
						<xsl:for-each select="../*[@selected = 'true']">
							<xsl:value-of select="title/desc[@lan = $wb_lang_encoding]/@name"/>
							<xsl:if test="position() != last()"> ;</xsl:if>
						</xsl:for-each>
					</xsl:otherwise>
				</xsl:choose>
			</td>
		</xsl:if>
		<xsl:apply-templates mode="row">
			<xsl:with-param name="pos" select="$pos"/>
			<xsl:with-param name="id" select="$id"/>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template match="node" mode="row"/>
	<!-- =============================================================== -->
	<!-- =============================================================== -->
	<xsl:template name="get_desc">
		<xsl:value-of select="desc[@lan =$wb_lang_encoding]/@name"/>
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>
