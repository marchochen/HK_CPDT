<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<!-- ============================================================================================== -->
<!-- PLEASE KEEP THE SAME STYLE FORMAT OF THIS XSL FILE -->
<!-- = Default Const ==================================================================================== -->
<!-- Default encoding for //cur_usr  absent pages -->
<xsl:variable name="default_encoding">ISO-8859-1</xsl:variable>
<xsl:variable name="default_label">ISO-8859-1</xsl:variable>
<!-- = System Const ==================================================================================== -->
<!-- servlet package -->
<xsl:variable name="wb_servlet_package_qdbAction">qdbAction</xsl:variable>
<xsl:variable name="wb_servlet_package_aeAction">aeAction</xsl:variable>
<xsl:variable name="wb_servlet_package_Dispatcher">Dispatcher</xsl:variable>
<xsl:variable name="wb_servlet_package_DRMRegistration">DRMRegistration</xsl:variable>
<xsl:variable name="wb_servlet_package_CMI">CMI</xsl:variable>
<!-- servlet path -->
<xsl:variable name="wb_servlet_qdbaction_url">../servlet/<xsl:value-of select="$wb_servlet_package_qdbAction"/></xsl:variable>
<xsl:variable name="wb_servlet_aeaction_url">../servlet/<xsl:value-of select="$wb_servlet_package_aeAction"/></xsl:variable>
<xsl:variable name="wb_servlet_dispatcher_url">../servlet/<xsl:value-of select="$wb_servlet_package_Dispatcher"/></xsl:variable>
<xsl:variable name="wb_servlet_drmregistration_url">../servlet/<xsl:value-of select="$wb_servlet_package_DRMRegistration"/></xsl:variable>
<xsl:variable name="wb_servlet_cmi_url">../servlet/<xsl:value-of select="$wb_servlet_package_CMI"/></xsl:variable>
<!-- jsp root -->
<xsl:variable name="wb_jsp_root_url">../jsp/</xsl:variable>
<!-- -->
<xsl:variable name="url_qdb_action"><xsl:value-of select="$wb_servlet_qdbaction_url"/>?</xsl:variable>
<xsl:variable name="root_cw">CW</xsl:variable>
<xsl:variable name="language"><xsl:value-of select="//@language"/></xsl:variable>
<!-- = Generic Const ==================================================================================== -->
<xsl:variable name="encoding">
	<xsl:choose>
		<xsl:when test="not(//cur_usr/@encoding)"><xsl:value-of select="$default_encoding"/></xsl:when>
		<xsl:otherwise><xsl:value-of select="//cur_usr/@encoding"/></xsl:otherwise>
	</xsl:choose>
</xsl:variable>	
<!-- = init language & image & CSS path ========================================================================= -->
<xsl:variable name="wb_lang_encoding">
	<xsl:choose>
		<xsl:when test="not(//cur_usr/@label)"><xsl:value-of select="$default_label"/></xsl:when>
		<xsl:otherwise><xsl:value-of select="//cur_usr/@label"/></xsl:otherwise>
	</xsl:choose>
</xsl:variable>
<!-- -->
<xsl:variable name="wb_lang">
	<xsl:choose>
		<xsl:when test="$wb_lang_encoding= 'Big5'">ch</xsl:when>
		<xsl:when test="$wb_lang_encoding= 'GB2312'">gb</xsl:when>
		<xsl:when test="$wb_lang_encoding= 'ISO-8859-1'">en</xsl:when>
		<xsl:otherwise>en</xsl:otherwise>
	</xsl:choose>
</xsl:variable>
<!-- -->
<xsl:variable name="wb_cur_lang">
	<xsl:choose>
		<xsl:when test="$wb_lang= 'ch'">zh-hk</xsl:when>
		<xsl:when test="$wb_lang= 'gb'">zh-cn</xsl:when>
		<xsl:when test="$wb_lang= 'en'">en-us</xsl:when>
		<xsl:otherwise>en-us</xsl:otherwise>
	</xsl:choose>
</xsl:variable>

<xsl:variable name="uppercase">ABCDEFGHIJKLMNOPQRSTUVWXYZ</xsl:variable>
<xsl:variable name="lowercase">abcdefghijklmnopqrstuvwxyz</xsl:variable>

<!-- -->
<xsl:variable name="wb_style">
	<xsl:choose>
		<xsl:when test="//cur_usr/@style != ''"><xsl:value-of select="//cur_usr/@style"/></xsl:when>
		<xsl:when test="//universe/@style != ''"><xsl:value-of select="//universe/@style"/></xsl:when>
		<xsl:otherwise>cw/skin1/</xsl:otherwise>
	</xsl:choose>
</xsl:variable>
<!-- -->
<xsl:variable name="wb_skin">
	<xsl:choose>
		<xsl:when test="//cur_usr/@skin != ''"><xsl:value-of select="//cur_usr/@skin"/></xsl:when>
		<xsl:when test="//universe/@skin != ''"><xsl:value-of select="//universe/@skin"/></xsl:when>
		<xsl:otherwise>skin1</xsl:otherwise>
	</xsl:choose>
</xsl:variable>
<!-- -->
<xsl:variable name="wb_media_path">../<xsl:value-of select="$wb_style"/>images/<xsl:value-of select="$wb_lang"/>/</xsl:variable>
<xsl:variable name="wb_img_path">../wb_image/</xsl:variable>
<xsl:variable name="wb_img_lang_path"><xsl:value-of select="$wb_img_path"/>lang/<xsl:value-of select="$wb_cur_lang"/>/</xsl:variable>
<xsl:variable name="wb_img_skin_path"><xsl:value-of select="$wb_img_path"/>skin/<xsl:value-of select="$wb_skin"/>/</xsl:variable>
<xsl:variable name="wb_common_img_path">../wb_image/</xsl:variable>
<xsl:variable name="wb_css_path"><xsl:value-of select="$wb_media_path"/>css/</xsl:variable>
<xsl:variable name="wb_glb_css_path"><xsl:value-of select="$wb_img_path"/>css/</xsl:variable>
<xsl:variable name="wb_js_path">../js/</xsl:variable>
<xsl:variable name="wb_jsp_path">../<xsl:value-of select="$wb_style"/>jsp/</xsl:variable>
<xsl:variable name="wb_js_lang_path"><xsl:value-of select="$wb_js_path"/><xsl:value-of select="$wb_lang"/>/</xsl:variable>
<xsl:variable name="wb_applet_skin_path"><xsl:value-of select="$wb_media_path"/>app/</xsl:variable>
<xsl:variable name="wb_planFile_path">../plan/</xsl:variable>
<xsl:variable name="wb_style_path">../theme/skin1/</xsl:variable>
<xsl:variable name="wb_new_css">../static/js/bootstrap/css/</xsl:variable>
<xsl:variable name="wb_new_admin_css">../static/admin/css/</xsl:variable>
<xsl:variable name="wb_new_font_css">../static/admin/css/font-awesome/css/</xsl:variable>
<!-- = wizBank Page Header Desc ============================================================================ -->
<xsl:variable name="skin_type"/>
<xsl:variable name="applet_img_path"><xsl:value-of select="$wb_media_path"/>app/</xsl:variable>

<!-- = Generic label ====================================================================================== -->
<xsl:variable name="lab_gen_loading"><xsl:choose><xsl:when test="$wb_lang = 'ch'">下載中，請稍候...</xsl:when><xsl:when test="$wb_lang = 'gb'">装载中，请稍候......</xsl:when><xsl:otherwise>Loading, please wait...</xsl:otherwise></xsl:choose></xsl:variable>
<xsl:variable name="lab_const_year"><xsl:choose><xsl:when test="$wb_lang = 'ch'">年</xsl:when><xsl:when test="$wb_lang = 'gb'">年</xsl:when><xsl:otherwise>-</xsl:otherwise></xsl:choose></xsl:variable>
<xsl:variable name="lab_const_month"><xsl:choose><xsl:when test="$wb_lang = 'ch'">月</xsl:when><xsl:when test="$wb_lang = 'gb'">月</xsl:when><xsl:otherwise>-</xsl:otherwise></xsl:choose></xsl:variable>
<xsl:variable name="lab_const_day"><xsl:choose><xsl:when test="$wb_lang = 'ch'">日</xsl:when><xsl:when test="$wb_lang = 'gb'">日</xsl:when><xsl:otherwise/></xsl:choose></xsl:variable>
<xsl:variable name="lab_const_hrs"><xsl:choose><xsl:when test="$wb_lang = 'ch'">時</xsl:when><xsl:when test="$wb_lang = 'gb'">时</xsl:when><xsl:otherwise>:</xsl:otherwise></xsl:choose></xsl:variable>
<xsl:variable name="lab_const_mins"><xsl:choose><xsl:when test="$wb_lang = 'ch'">分</xsl:when><xsl:when test="$wb_lang = 'gb'">分</xsl:when><xsl:otherwise/></xsl:choose></xsl:variable>
<xsl:variable name="lab_const_cap_to"><xsl:choose><xsl:when test="$wb_lang = 'ch'">至</xsl:when><xsl:when test="$wb_lang = 'gb'">至</xsl:when><xsl:otherwise>To</xsl:otherwise></xsl:choose></xsl:variable>
<xsl:variable name="lab_const_to"><xsl:choose><xsl:when test="$wb_lang = 'ch'">至</xsl:when><xsl:when test="$wb_lang = 'gb'">至</xsl:when><xsl:otherwise>to</xsl:otherwise></xsl:choose></xsl:variable>
<xsl:variable name="lab_const_from"><xsl:choose><xsl:when test="$wb_lang = 'ch'">由</xsl:when><xsl:when test="$wb_lang = 'gb'">由</xsl:when><xsl:otherwise>From</xsl:otherwise></xsl:choose></xsl:variable>
<xsl:variable name="lab_const_up"><xsl:choose><xsl:when test="$wb_lang = 'ch'">起</xsl:when><xsl:when test="$wb_lang = 'gb'">起</xsl:when><xsl:otherwise/></xsl:choose></xsl:variable>
<xsl:variable name="lab_gen_select"><xsl:choose><xsl:when test="$wb_lang = 'ch'">選擇</xsl:when><xsl:when test="$wb_lang = 'gb'">选择</xsl:when><xsl:otherwise>Select</xsl:otherwise></xsl:choose></xsl:variable>
<!-- Flag indicating the status of the question -->
<xsl:variable name="quest_ok">ok</xsl:variable>
<!-- Question stamp when this is error when parsing the question -->
<xsl:variable name="quest_htmlerr">Question contains HTML tag, cannot be displayed.</xsl:variable>
<xsl:variable name="quest_objerr">Question contains OBJECT tag, cannot be displayed.</xsl:variable>
<xsl:variable name="quest_optmaxerr">Too many options, cannot be displayed.</xsl:variable>
<xsl:variable name="quest_opthtmlerr">Option contains HTML tag, cannnot be displayed.</xsl:variable>
<!-- Option stamp when this is error when parsing the question -->
<xsl:variable name="option_err">N/A</xsl:variable>
<!-- Aanswer stamp when this is error when parsing the question -->
<xsl:variable name="answer_err"/>
<!-- Question form url -->
<xsl:variable name="url_que_ins_frm"><xsl:value-of select="concat($url_qdb_action, '&amp;', 'cmd=get_que_frm')"/></xsl:variable>
<xsl:variable name="lab_home"><xsl:choose><xsl:when test="$wb_lang = 'ch'">首頁</xsl:when><xsl:when test="$wb_lang = 'gb'">首页</xsl:when><xsl:otherwise>Home</xsl:otherwise></xsl:choose></xsl:variable>
<xsl:variable name="lab_adm_home"><xsl:choose><xsl:when test="$wb_lang = 'ch'">首頁</xsl:when><xsl:when test="$wb_lang = 'gb'">首页</xsl:when><xsl:otherwise>Home</xsl:otherwise></xsl:choose></xsl:variable>
<xsl:variable name="lab_ist_home"><xsl:choose><xsl:when test="$wb_lang = 'ch'">首頁</xsl:when><xsl:when test="$wb_lang = 'gb'">首页</xsl:when><xsl:otherwise>Home</xsl:otherwise></xsl:choose></xsl:variable>
<xsl:variable name="lab_lrn_home"><xsl:choose><xsl:when test="$wb_lang = 'ch'">首頁</xsl:when><xsl:when test="$wb_lang = 'gb'">首页</xsl:when><xsl:otherwise>Home</xsl:otherwise></xsl:choose></xsl:variable>
<xsl:variable name="lab_not_applicable"><xsl:choose><xsl:when test="$wb_lang = 'ch'">不適用</xsl:when><xsl:when test="$wb_lang = 'gb'">不适用</xsl:when><xsl:otherwise>N/A</xsl:otherwise></xsl:choose></xsl:variable>
<xsl:variable name="lab_empty">--</xsl:variable>
<!-- Pagination Label -->
<xsl:variable name="lab_showing"><xsl:choose><xsl:when test="$wb_lang = 'ch'">本頁顯示</xsl:when><xsl:when test="$wb_lang = 'gb'">本页显示第</xsl:when><xsl:otherwise>Showing</xsl:otherwise></xsl:choose></xsl:variable>
<xsl:variable name="lab_total"><xsl:choose><xsl:when test="$wb_lang = 'ch'">總數</xsl:when><xsl:when test="$wb_lang = 'gb'">总计</xsl:when><xsl:otherwise>Total</xsl:otherwise></xsl:choose></xsl:variable>
<xsl:variable name="lab_prev"><xsl:choose><xsl:when test="$wb_lang = 'ch'">上5頁</xsl:when><xsl:when test="$wb_lang = 'gb'">上5页</xsl:when><xsl:otherwise>Prev 5</xsl:otherwise></xsl:choose></xsl:variable>
<xsl:variable name="lab_next"><xsl:choose><xsl:when test="$wb_lang = 'ch'">下5頁</xsl:when><xsl:when test="$wb_lang = 'gb'">下5页</xsl:when><xsl:otherwise>Next 5</xsl:otherwise></xsl:choose></xsl:variable>
<xsl:variable name="lab_page_of"><xsl:choose><xsl:when test="$wb_lang = 'ch'">個，共找到</xsl:when><xsl:when test="$wb_lang = 'gb'">个，共找到</xsl:when><xsl:otherwise>of</xsl:otherwise></xsl:choose></xsl:variable>
<xsl:variable name="lab_page_piece"><xsl:choose><xsl:when test="$wb_lang = 'ch'">個</xsl:when><xsl:when test="$wb_lang = 'gb'">个</xsl:when><xsl:otherwise/></xsl:choose></xsl:variable>
<xsl:variable name="lab_all"><xsl:choose><xsl:when test="$wb_lang = 'ch'">全部</xsl:when><xsl:when test="$wb_lang = 'gb'">全部</xsl:when><xsl:otherwise>All</xsl:otherwise></xsl:choose></xsl:variable>
<xsl:variable name="lab_gen_ok"><xsl:choose><xsl:when test="$wb_lang = 'ch'">確定</xsl:when><xsl:when test="$wb_lang = 'gb'">确定</xsl:when><xsl:otherwise>OK</xsl:otherwise></xsl:choose></xsl:variable>
<xsl:variable name="lab_gen_cancel"><xsl:choose><xsl:when test="$wb_lang = 'ch'">取消</xsl:when><xsl:when test="$wb_lang = 'gb'">取消</xsl:when><xsl:otherwise>Cancel</xsl:otherwise></xsl:choose></xsl:variable>
<xsl:variable name="lab_gen_add"><xsl:choose><xsl:when test="$wb_lang = 'ch'">新增</xsl:when><xsl:when test="$wb_lang = 'gb'">添加</xsl:when><xsl:otherwise>Add</xsl:otherwise></xsl:choose></xsl:variable>
<xsl:variable name="lab_gen_edit"><xsl:choose><xsl:when test="$wb_lang = 'ch'">修改</xsl:when><xsl:when test="$wb_lang = 'gb'">修改</xsl:when><xsl:otherwise>Edit</xsl:otherwise></xsl:choose></xsl:variable>
<xsl:variable name="lab_gen_remove"><xsl:choose><xsl:when test="$wb_lang = 'ch'">刪除</xsl:when><xsl:when test="$wb_lang = 'gb'">删除</xsl:when><xsl:otherwise>Remove</xsl:otherwise></xsl:choose></xsl:variable>
<xsl:variable name="lab_gen_search"><xsl:choose><xsl:when test="$wb_lang = 'ch'">搜尋</xsl:when><xsl:when test="$wb_lang = 'gb'">搜索</xsl:when><xsl:otherwise>Search</xsl:otherwise></xsl:choose></xsl:variable>
<xsl:variable name="lab_adv_search"><xsl:choose><xsl:when test="$wb_lang = 'ch'">進階搜尋</xsl:when><xsl:when test="$wb_lang = 'gb'">高级搜索</xsl:when><xsl:otherwise>Advanced search</xsl:otherwise></xsl:choose></xsl:variable>
<xsl:variable name="lab_yy_mm_dd"><xsl:choose><xsl:when test="$wb_lang = 'ch'">年-月-日</xsl:when><xsl:when test="$wb_lang = 'gb'">年-月-日</xsl:when><xsl:otherwise>YYYY-MM-DD</xsl:otherwise></xsl:choose></xsl:variable>
<xsl:variable name="lab_dd_mm_yy"><xsl:choose><xsl:when test="$wb_lang = 'ch'">日-月-年</xsl:when><xsl:when test="$wb_lang = 'gb'">日-月-年</xsl:when><xsl:otherwise>DD-MM-YYYY</xsl:otherwise></xsl:choose></xsl:variable>
<xsl:variable name="lab_yy_mm"><xsl:choose><xsl:when test="$wb_lang = 'ch'">年-月</xsl:when><xsl:when test="$wb_lang = 'gb'">年-月</xsl:when><xsl:otherwise>YYYY-MM</xsl:otherwise></xsl:choose></xsl:variable>
<xsl:variable name="lab_hh_mm"><xsl:choose><xsl:when test="$wb_lang = 'ch'">小時:分鐘</xsl:when><xsl:when test="$wb_lang = 'gb'">时：分</xsl:when><xsl:otherwise>HH:MM</xsl:otherwise></xsl:choose></xsl:variable>
<!-- Date Format Label for english Format -->
<xsl:variable name="lab_mm_jan"><xsl:choose><xsl:when test="$wb_lang = 'ch'">一月</xsl:when><xsl:when test="$wb_lang = 'gb'">一月</xsl:when><xsl:otherwise>Jan</xsl:otherwise></xsl:choose></xsl:variable>
<xsl:variable name="lab_mm_feb"><xsl:choose><xsl:when test="$wb_lang = 'ch'">二月</xsl:when><xsl:when test="$wb_lang = 'gb'">二月</xsl:when><xsl:otherwise>Feb</xsl:otherwise></xsl:choose></xsl:variable>
<xsl:variable name="lab_mm_mar"><xsl:choose><xsl:when test="$wb_lang = 'ch'">三月</xsl:when><xsl:when test="$wb_lang = 'gb'">三月</xsl:when><xsl:otherwise>Mar</xsl:otherwise></xsl:choose></xsl:variable>
<xsl:variable name="lab_mm_apr"><xsl:choose><xsl:when test="$wb_lang = 'ch'">四月</xsl:when><xsl:when test="$wb_lang = 'gb'">四月</xsl:when><xsl:otherwise>Apr</xsl:otherwise></xsl:choose></xsl:variable>
<xsl:variable name="lab_mm_may"><xsl:choose><xsl:when test="$wb_lang = 'ch'">五月</xsl:when><xsl:when test="$wb_lang = 'gb'">五月</xsl:when><xsl:otherwise>May</xsl:otherwise></xsl:choose></xsl:variable>
<xsl:variable name="lab_mm_jun"><xsl:choose><xsl:when test="$wb_lang = 'ch'">六月</xsl:when><xsl:when test="$wb_lang = 'gb'">六月</xsl:when><xsl:otherwise>Jun</xsl:otherwise></xsl:choose></xsl:variable>
<xsl:variable name="lab_mm_jul"><xsl:choose><xsl:when test="$wb_lang = 'ch'">七月</xsl:when><xsl:when test="$wb_lang = 'gb'">七月</xsl:when><xsl:otherwise>Jul</xsl:otherwise></xsl:choose></xsl:variable>
<xsl:variable name="lab_mm_aug"><xsl:choose><xsl:when test="$wb_lang = 'ch'">八月</xsl:when><xsl:when test="$wb_lang = 'gb'">八月</xsl:when><xsl:otherwise>Aug</xsl:otherwise></xsl:choose></xsl:variable>
<xsl:variable name="lab_mm_sep"><xsl:choose><xsl:when test="$wb_lang = 'ch'">九月</xsl:when><xsl:when test="$wb_lang = 'gb'">九月</xsl:when><xsl:otherwise>Sep</xsl:otherwise></xsl:choose></xsl:variable>
<xsl:variable name="lab_mm_oct"><xsl:choose><xsl:when test="$wb_lang = 'ch'">十月</xsl:when><xsl:when test="$wb_lang = 'gb'">十月</xsl:when><xsl:otherwise>Oct</xsl:otherwise></xsl:choose></xsl:variable>
<xsl:variable name="lab_mm_nov"><xsl:choose><xsl:when test="$wb_lang = 'ch'">十一月</xsl:when><xsl:when test="$wb_lang = 'gb'">十一月</xsl:when><xsl:otherwise>Nov</xsl:otherwise></xsl:choose></xsl:variable>
<xsl:variable name="lab_mm_dec"><xsl:choose><xsl:when test="$wb_lang = 'ch'">十二月</xsl:when><xsl:when test="$wb_lang = 'gb'">十二月</xsl:when><xsl:otherwise>Dec</xsl:otherwise></xsl:choose></xsl:variable>
<!-- User Information Label -->
<xsl:variable name="lab_usr_display_name"><xsl:choose><xsl:when test="$wb_lang = 'ch'">名稱</xsl:when><xsl:when test="$wb_lang = 'gb'">名称</xsl:when><xsl:otherwise>Name</xsl:otherwise></xsl:choose></xsl:variable>
<!-- Required Information Instruction Label -->
<xsl:variable name="lab_all_info_required"><xsl:choose><xsl:when test="$wb_lang = 'ch'">提示: 所有資料都必須填寫</xsl:when><xsl:when test="$wb_lang = 'gb'">提示：所有信息都必须填写</xsl:when><xsl:otherwise>Note: All information is required</xsl:otherwise></xsl:choose></xsl:variable>
<xsl:variable name="lab_info_required"><xsl:choose><xsl:when test="$wb_lang = 'ch'">為必填</xsl:when><xsl:when test="$wb_lang = 'gb'">为必填</xsl:when><xsl:otherwise>Required</xsl:otherwise></xsl:choose></xsl:variable>
<!-- Run Item Label -->
<xsl:variable name="lab_const_manage"><xsl:choose><xsl:when test="$wb_lang = 'ch'">管理</xsl:when><xsl:when test="$wb_lang = 'gb'">管理</xsl:when><xsl:otherwise> management</xsl:otherwise></xsl:choose></xsl:variable>
<xsl:variable name="lab_const_run"><xsl:choose><xsl:when test="$wb_lang = 'ch'">班別</xsl:when><xsl:when test="$wb_lang = 'gb'">班级</xsl:when><xsl:otherwise>class</xsl:otherwise></xsl:choose></xsl:variable>
<xsl:variable name="lab_const_exam_run"><xsl:choose><xsl:when test="$wb_lang = 'ch'">考試場次</xsl:when><xsl:when test="$wb_lang = 'gb'">考试场次</xsl:when><xsl:otherwise>class</xsl:otherwise></xsl:choose></xsl:variable>
<xsl:variable name="lab_const_run_plural"><xsl:choose><xsl:when test="$wb_lang = 'ch'">班別</xsl:when><xsl:when test="$wb_lang = 'gb'">班级</xsl:when><xsl:otherwise>Classes</xsl:otherwise></xsl:choose></xsl:variable>

<xsl:variable name="lab_const_run_up"><xsl:choose><xsl:when test="$wb_lang = 'ch'">班別</xsl:when><xsl:when test="$wb_lang = 'gb'">班级</xsl:when><xsl:otherwise>Class</xsl:otherwise></xsl:choose></xsl:variable>
<xsl:variable name="lab_const_exam_run_up"><xsl:choose><xsl:when test="$wb_lang = 'ch'">考試場次</xsl:when><xsl:when test="$wb_lang = 'gb'">考试场次</xsl:when><xsl:otherwise>Class</xsl:otherwise></xsl:choose></xsl:variable>

<xsl:variable name="itm_exam_ind" select="//itm_action_nav/@itm_exam_ind" />
<xsl:variable name="lab_const_cls_manage"><xsl:value-of select="$lab_const_run_up"/><xsl:value-of select="$lab_const_manage"/></xsl:variable>
<xsl:variable name="lab_const_exam_manage"><xsl:value-of select="$lab_const_exam_run_up"/><xsl:value-of select="$lab_const_manage"/></xsl:variable>

<!-- Session Item Label -->
<xsl:variable name="lab_const_session"><xsl:choose><xsl:when test="$wb_lang = 'ch'">課堂</xsl:when><xsl:when test="$wb_lang = 'gb'">课堂</xsl:when><xsl:otherwise>Session</xsl:otherwise></xsl:choose></xsl:variable>

<!-- Enrollment Label -->
<xsl:variable name="lab_const_enroll"><xsl:choose><xsl:when test="$wb_lang = 'ch'">報名</xsl:when><xsl:when test="$wb_lang = 'gb'">报名</xsl:when><xsl:otherwise>Enroll</xsl:otherwise></xsl:choose></xsl:variable>
<xsl:variable name="lab_const_enrolled"><xsl:choose><xsl:when test="$wb_lang = 'ch'">已報名</xsl:when><xsl:when test="$wb_lang = 'gb'">已报名</xsl:when><xsl:otherwise>Enrolled</xsl:otherwise></xsl:choose></xsl:variable>
<xsl:variable name="lab_const_enrollment"><xsl:choose><xsl:when test="$wb_lang = 'ch'">報名</xsl:when><xsl:when test="$wb_lang = 'gb'">报名</xsl:when><xsl:otherwise>Enrollment</xsl:otherwise></xsl:choose></xsl:variable>
<xsl:variable name="lab_const_enrollments"><xsl:choose><xsl:when test="$wb_lang = 'ch'">報名</xsl:when><xsl:when test="$wb_lang = 'gb'">报名</xsl:when><xsl:otherwise>Enrollments</xsl:otherwise></xsl:choose></xsl:variable>
<xsl:variable name="lab_const_enroll_sm"><xsl:choose><xsl:when test="$wb_lang = 'ch'">報名</xsl:when><xsl:when test="$wb_lang = 'gb'">报名</xsl:when><xsl:otherwise>enroll</xsl:otherwise></xsl:choose></xsl:variable>
<xsl:variable name="lab_const_enrolled_sm"><xsl:choose><xsl:when test="$wb_lang = 'ch'">報名</xsl:when><xsl:when test="$wb_lang = 'gb'">报名</xsl:when><xsl:otherwise>enrolled</xsl:otherwise></xsl:choose></xsl:variable>
<xsl:variable name="lab_const_enrollment_sm"><xsl:choose><xsl:when test="$wb_lang = 'ch'">報名</xsl:when><xsl:when test="$wb_lang = 'gb'">报名</xsl:when><xsl:otherwise>enrollment</xsl:otherwise></xsl:choose></xsl:variable>
<xsl:variable name="lab_const_enrollments_sm"><xsl:choose><xsl:when test="$wb_lang = 'ch'">報名</xsl:when><xsl:when test="$wb_lang = 'gb'">报名</xsl:when><xsl:otherwise>enrollments</xsl:otherwise></xsl:choose></xsl:variable>
<!-- pagination -->
<xsl:variable name="lab_page"><xsl:choose><xsl:when test="$wb_lang = 'ch'">頁</xsl:when><xsl:when test="$wb_lang = 'gb'">页</xsl:when><xsl:otherwise>Page</xsl:otherwise></xsl:choose></xsl:variable>
<!-- Knowledge Management -->
<xsl:variable name="lab_const_know_domain"><xsl:choose><xsl:when test="$wb_lang = 'ch'">目錄</xsl:when><xsl:when test="$wb_lang = 'gb'">知识库</xsl:when><xsl:otherwise>Knowledge base</xsl:otherwise></xsl:choose></xsl:variable>
<xsl:variable name="lab_const_know_domain_sm"><xsl:choose><xsl:when test="$wb_lang = 'ch'">目錄</xsl:when><xsl:when test="$wb_lang = 'gb'">知识库</xsl:when><xsl:otherwise>Knowledge base</xsl:otherwise></xsl:choose></xsl:variable>
<xsl:variable name="lab_const_know_domains"><xsl:choose><xsl:when test="$wb_lang = 'ch'">目錄</xsl:when><xsl:when test="$wb_lang = 'gb'">目录</xsl:when><xsl:otherwise>Knowledge base</xsl:otherwise></xsl:choose></xsl:variable>
<xsl:variable name="lab_const_know_domains_sm"><xsl:choose><xsl:when test="$wb_lang = 'ch'">目錄</xsl:when><xsl:when test="$wb_lang = 'gb'">目录</xsl:when><xsl:otherwise>Knowledge base</xsl:otherwise></xsl:choose></xsl:variable>
<xsl:variable name="lab_const_domain"><xsl:choose><xsl:when test="$wb_lang = 'ch'">目錄</xsl:when><xsl:when test="$wb_lang = 'gb'">目录</xsl:when><xsl:otherwise>Category</xsl:otherwise></xsl:choose></xsl:variable>
<xsl:variable name="lab_const_domain_sm"><xsl:choose><xsl:when test="$wb_lang = 'ch'">目錄</xsl:when><xsl:when test="$wb_lang = 'gb'">目录</xsl:when><xsl:otherwise>Category</xsl:otherwise></xsl:choose></xsl:variable>
<xsl:variable name="lab_const_domain_s"><xsl:choose><xsl:when test="$wb_lang = 'ch'">目錄</xsl:when><xsl:when test="$wb_lang = 'gb'">目录</xsl:when><xsl:otherwise>Categories</xsl:otherwise></xsl:choose></xsl:variable>
<xsl:variable name="lab_const_domain_s_sm"><xsl:choose><xsl:when test="$wb_lang = 'ch'">目錄</xsl:when><xsl:when test="$wb_lang = 'gb'">目录</xsl:when><xsl:otherwise>Categories</xsl:otherwise></xsl:choose></xsl:variable>
<xsl:variable name="lab_const_domains"><xsl:choose><xsl:when test="$wb_lang = 'ch'">目錄</xsl:when><xsl:when test="$wb_lang = 'gb'">目录</xsl:when><xsl:otherwise>Categories</xsl:otherwise></xsl:choose></xsl:variable>
<xsl:variable name="lab_const_domains_sm"><xsl:choose><xsl:when test="$wb_lang = 'ch'">目錄</xsl:when><xsl:when test="$wb_lang = 'gb'">目录</xsl:when><xsl:otherwise>catalogs</xsl:otherwise></xsl:choose></xsl:variable>
<xsl:variable name="lab_const_sub_domain"><xsl:choose><xsl:when test="$wb_lang = 'ch'">子目錄</xsl:when><xsl:when test="$wb_lang = 'gb'">子目录</xsl:when><xsl:otherwise>Sub-category</xsl:otherwise></xsl:choose></xsl:variable>
<xsl:variable name="lab_const_sub_domains"><xsl:choose><xsl:when test="$wb_lang = 'ch'">子目錄</xsl:when><xsl:when test="$wb_lang = 'gb'">子目录</xsl:when><xsl:otherwise>Sub-categories</xsl:otherwise></xsl:choose></xsl:variable>
<xsl:variable name="lab_const_sub_domains_sm"><xsl:choose><xsl:when test="$wb_lang = 'ch'">子目錄</xsl:when><xsl:when test="$wb_lang = 'gb'">子目录</xsl:when><xsl:otherwise>Sub-categories</xsl:otherwise></xsl:choose></xsl:variable>
<xsl:variable name="lab_const_itm_per_page"><xsl:choose><xsl:when test="$wb_lang = 'ch'">個每頁</xsl:when><xsl:when test="$wb_lang = 'gb'">个每页</xsl:when><xsl:otherwise>items per page</xsl:otherwise></xsl:choose></xsl:variable>
<xsl:variable name="lab_const_unlimited"><xsl:choose><xsl:when test="$wb_lang = 'ch'">無限</xsl:when><xsl:when test="$wb_lang = 'gb'">无限</xsl:when><xsl:otherwise>Unlimited</xsl:otherwise></xsl:choose></xsl:variable>
<xsl:variable name="lab_const_assigned_to"><xsl:choose><xsl:when test="$wb_lang = 'ch'">指定</xsl:when><xsl:when test="$wb_lang = 'gb'">指定</xsl:when><xsl:otherwise>Assigned to</xsl:otherwise></xsl:choose></xsl:variable>
<xsl:variable name="lab_const_free"><xsl:choose><xsl:when test="$wb_lang = 'ch'">不收費</xsl:when><xsl:when test="$wb_lang = 'gb'">不收费</xsl:when><xsl:otherwise>Free</xsl:otherwise></xsl:choose></xsl:variable>
<!-- role -->
<xsl:variable name="lab_const_role"><xsl:choose><xsl:when test="$wb_lang = 'ch'">培訓管理員</xsl:when><xsl:when test="$wb_lang = 'gb'">培训管理员</xsl:when><xsl:otherwise>Training administrator</xsl:otherwise></xsl:choose></xsl:variable>

	<!-- logo title -->
	<xsl:variable name="lab_const_logo_faq">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">解答欄</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">答疑栏</xsl:when>
			<xsl:otherwise>Q&amp;A</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_const_logo_forum">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">論壇</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">论坛</xsl:when>
			<xsl:otherwise>Forum</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_const_logo_fm">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">設施管理</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">设施管理</xsl:when>
			<xsl:otherwise></xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	
	<xsl:variable name="lab_type_cos">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">課堂類</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">课堂类</xsl:when>
			<xsl:otherwise>课堂类</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_type_exam">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">測試類</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">测试类</xsl:when>
			<xsl:otherwise>测试类</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_type_ref">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">參考類</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">参考类</xsl:when>
			<xsl:otherwise>参考类</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>

	<!--<xsl:variable name="all_user_groups" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '668')"/>-->
	
	<xsl:variable name="all_user_groups">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">所有用戶組</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">所有用户组</xsl:when>
			<xsl:otherwise>All user groups</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	
	<xsl:variable name="lab_sel">
		<xsl:choose>
			<xsl:when test="$wb_lang='ch'">--請選擇--</xsl:when>
			<xsl:when test="$wb_lang='gb'">--请选择--</xsl:when>
			<xsl:otherwise>--Please select--</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	
	<xsl:variable name="tst_que_instr">
		<xsl:choose>
			<xsl:when test="$wb_lang='ch'">請注意，發佈到移動端的測驗，只支持<b>選擇題（單選題/多選題）</b>。如果你測驗需要發佈到移動端，請不要添加以下類型的題目：  <b>填充題，配對題、判斷題、問答題 、靜態情景題 、動態情景題</b>。</xsl:when>
			<xsl:when test="$wb_lang='gb'">请注意，发布到移动端的测验，只支持<b>选择题（单选题/多选题）</b>。如果你测验需要发布到移动端的测验，请不要添加以下类型的题目：  <b>填空题，配对题、判断题、问答题 、静态情景题 、动态情景题</b>。</xsl:when>
			<xsl:otherwise>Please note that a test unit in mobile version only support  <b>multiple choice questions (single / multi answers)</b>.  If you are planning to conduct the test by using mobile device, please do not include any questions other than multiple choice.</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	

	<xsl:variable name="function_lst" select="//granted_functions/functions/function"/>
	<xsl:variable name="has_plan_mgt">
		<xsl:choose>
			<xsl:when test="$function_lst and ($function_lst[@id='FTN_AMD_PLAN_CARRY_OUT'] or $function_lst[@id='FTN_AMD_YEAR_PALN'] or $function_lst[@id='FTN_AMD_MAKEUP_PLAN'] or $function_lst[@id='FTN_AMD_YEAR_PLAN_APPR'] or $function_lst[@id='FTN_AMD_MAKEUP_PLAN_APPR'] or $function_lst[@id='FTN_AMD_YEAR_SETTING'])">true</xsl:when>
			<xsl:otherwise>false</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="has_competency_mgt">
		<xsl:choose>
			<xsl:when test="$function_lst and ($function_lst[@id='CM_MAIN'] or $function_lst[@id='CM_ASS_MAIN'] or $function_lst[@id='CM_SKL_ANALYSIS'])">true</xsl:when>
			<xsl:otherwise>false</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	
	<xsl:template name="usg_display_bil">
		<xsl:param name="role"/>
		<xsl:param name="display_bil"/>
		<xsl:choose>
			<xsl:when test="$role = 'ROOT'">
				<xsl:value-of select="$all_user_groups"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$display_bil"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>
