<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/tst_ins_upd_utils.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="utils/display_form_input_time.xsl"/>
	<xsl:import href="share/wb_module_type_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	
	<xsl:output indent="yes"/>
	<!-- ======================================================================= -->
	<xsl:variable name="res_enable">
	<xsl:choose>
		<xsl:when test="count(//cur_usr/granted_functions/functions/function[@id='FTN_AMD_RES_MAIN']) = 0">false</xsl:when>
		<xsl:otherwise>true</xsl:otherwise>
	</xsl:choose>	
	</xsl:variable>	
	<xsl:variable name="mod_privilege">
		<xsl:choose>
			<xsl:when test="//cur_usr/@root_display = $root_cw">CW</xsl:when>
			<xsl:otherwise>AUTHOR</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="mod_id" select="/module/@id"/>
	<xsl:variable name="usr_id" select="/module/cur_usr/@id"/>
	<xsl:variable name="status" select="/module/header/@status"/>
	<xsl:variable name="attp" select="/module/header/@attempted"/>
	<xsl:variable name="tpl_type" select="/module/header/@type"/>	
	<xsl:variable name="mod_type" select="/module/header/@type"/>
	<xsl:variable name="mod_subtype" select="/module/header/@subtype"/>
	<xsl:variable name="title" select="/module/header/title"/>
	<xsl:variable name="source_link" select="/module/header/source"/>
	<xsl:variable name="source_type" select="/module/header/source/@type"/>
	<xsl:variable name="source_img_link" select="/module/header/img_link"/>
	<xsl:variable name="vod_duration" select="/module/header/vod_duration"/>
	<xsl:variable name="desc" select="/module/header/desc"/>
	<xsl:variable name="annotation" select="/module/header/annotation"/>
	<xsl:variable name="inst" select="/module/header/instruction"/>
	<xsl:variable name="diff" select="/module/header/@difficulty"/>
	<xsl:variable name="dur" select="/module/header/@duration"/>
	<xsl:variable name="template" select="/module/header/template_list/@cur_tpl"/>
	<xsl:variable name="timestamp" select="/module/@timestamp"/>
	<xsl:variable name="language" select="/module/@language"/>
	<xsl:variable name="res_instr_name" select="/module/header/instructor"/>
	<xsl:variable name="res_moderator" select="/module/header/moderator "/>
	<xsl:variable name="res_organization" select="/module/header/organization "/>
	<xsl:variable name="wb_gen_table_width" select="$wb_frame_table_width"/>
	<xsl:variable name="max_usr_attempt_num" select="/module/header/@max_usr_attempt"/>
	<xsl:variable name="is_relation_com" select="/module/is_relation_com"/>
	<xsl:variable name="show_answer_ind" select="/module/header/@show_answer_ind"/> 
	<xsl:variable name="mod_show_answer_after_passed_ind" select="/module/header/@mod_show_answer_after_passed_ind"/> 
	<xsl:variable name="res_status">
		<xsl:choose>
			<xsl:when test="$mod_subtype = 'ASS'"><xsl:value-of select="/module/@is_public"/></xsl:when>
			<xsl:otherwise><xsl:value-of select="/module/@res_status"/></xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	
	<xsl:variable name="isEnrollment_related">
	<xsl:choose>
		<xsl:when test="not (/module/enrollment_related)">0</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="/module/enrollment_related"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="cos_type" select="//itm_type"/>
	<xsl:variable name="itmContentDef" select="/module/header/@itmContentDef"/>
	<!-- ======================================================================= -->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates select="module"/>
		</html>
	</xsl:template>
	<!-- ======================================================================= -->
	<xsl:template match="module">
		<xsl:call-template name="wb_init_lab"/>
	</xsl:template>
	<!-- ======================================================================= -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_basic_info">基本資料</xsl:with-param>
			<xsl:with-param name="lab_mod_title">標題</xsl:with-param>
			<xsl:with-param name="lab_mod_duration">所需時間</xsl:with-param>
			<xsl:with-param name="lab_type">類型</xsl:with-param>
			<xsl:with-param name="lab_lang">語言</xsl:with-param>
			<xsl:with-param name="lab_desc">簡介</xsl:with-param>
			<xsl:with-param name="lab_req_time">時長(分鐘)</xsl:with-param>
			<xsl:with-param name="lab_download">是否允許在APP中下載</xsl:with-param>
			<xsl:with-param name="lab_prohibited">禁止</xsl:with-param>
			<xsl:with-param name="lab_allow">允許</xsl:with-param>
			<xsl:with-param name="download_tip">網上影片類型為不可下載。</xsl:with-param>
			<xsl:with-param name="lab_inst">指示</xsl:with-param>
			<xsl:with-param name="lab_diff">難度</xsl:with-param>
			<xsl:with-param name="lab_pass_score">合格率</xsl:with-param>
			<xsl:with-param name="lab_passing_score">合格分數</xsl:with-param>
			<xsl:with-param name="lab_selection_logic">選取邏輯</xsl:with-param>
			<xsl:with-param name="lab_template">樣板</xsl:with-param>
			<xsl:with-param name="lab_url">網址(请输入完整的URL，格式为：http://host:port/.../xxx.mp4; 请不要使用相对路径。)</xsl:with-param>
			<xsl:with-param name="lab_online_video">網上影片</xsl:with-param>
			<xsl:with-param name="lab_status">狀態</xsl:with-param>
			<xsl:with-param name="lab_max_score">滿分</xsl:with-param>
			<xsl:with-param name="lab_ch">繁體中文</xsl:with-param>
			<xsl:with-param name="lab_gb">簡體中文</xsl:with-param>
			<xsl:with-param name="lab_eng">英文</xsl:with-param>
			<xsl:with-param name="lab_online">已發佈</xsl:with-param>
			<xsl:with-param name="lab_offline">未發佈</xsl:with-param>
			<xsl:with-param name="lab_hard">困難</xsl:with-param>
			<xsl:with-param name="lab_normal">一般</xsl:with-param>
			<xsl:with-param name="lab_easy">容易</xsl:with-param>
			<xsl:with-param name="lab_upload_file">上傳檔案</xsl:with-param>
			<xsl:with-param name="lab_upload_video">上傳視頻</xsl:with-param>
			<xsl:with-param name="lab_uploaded_video">已上傳視頻</xsl:with-param>
			<xsl:with-param name="lab_uploaded_file">已上傳檔案</xsl:with-param>
			<xsl:with-param name="lab_source">來源</xsl:with-param>
			<xsl:with-param name="lab_ann">註釋</xsl:with-param>
			<xsl:with-param name="lab_keep_file">保留檔案</xsl:with-param>
			<xsl:with-param name="lab_change_to">更改為</xsl:with-param>
			<xsl:with-param name="lab_from_resource">選取自資源庫</xsl:with-param>
			<xsl:with-param name="lab_ashtml">成為HTML?</xsl:with-param>
			<xsl:with-param name="lab_min">分鐘</xsl:with-param>
			<xsl:with-param name="lab_random">隨機</xsl:with-param>
			<xsl:with-param name="lab_adaptive">適性</xsl:with-param>
			<xsl:with-param name="lab_max_upload">最多提交</xsl:with-param>
			<xsl:with-param name="lab_submission">提交作業結後語</xsl:with-param>
			<xsl:with-param name="lab_submission_desc">(請使用不超過2000個位的文字)</xsl:with-param>
			<xsl:with-param name="lab_due_date_format">年-月-日</xsl:with-param>
			<xsl:with-param name="lab_due_date_time_format">小時:分鐘</xsl:with-param>
			<xsl:with-param name="lab_notify_student">提交作業電郵確認</xsl:with-param>
			<xsl:with-param name="lab_file">檔案標題</xsl:with-param>
			<xsl:with-param name="lab_due_date">作業提交截止時間</xsl:with-param>
			<xsl:with-param name="lab_due_date_num">天(從課程開始日期算起)</xsl:with-param>
			<xsl:with-param name="lab_due_date_num_inst_1">對於面授培訓，課程的開始日期是班別的開始日期。</xsl:with-param>
			<xsl:with-param name="lab_due_date_num_inst_2">對於網上課程，課程的開始日期是學員成功報讀的日期。</xsl:with-param>
			<xsl:with-param name="lab_due_date_non_obligatory">(非強制)</xsl:with-param>
			<xsl:with-param name="lab_ass_inst">文字</xsl:with-param>
			<xsl:with-param name="lab_ass_inst_desc">(請使用不超過1000個位的文字)</xsl:with-param>
			<xsl:with-param name="lab_unlimit">不限</xsl:with-param>
			<xsl:with-param name="lab_immediate">即時</xsl:with-param>
			<xsl:with-param name="lab_start_date">開始時間</xsl:with-param>
			<xsl:with-param name="lab_end_date">結束時間</xsl:with-param>
			<xsl:with-param name="lab_grading">評分制度</xsl:with-param>
			<xsl:with-param name="lab_grade">等級制</xsl:with-param>
			<xsl:with-param name="lab_by_score">分數制</xsl:with-param>
			<xsl:with-param name="lab_not_limited">不限</xsl:with-param>
			<xsl:with-param name="lab_files">個檔案</xsl:with-param>
			<xsl:with-param name="lab_no_of_submission">提交作業檔案數目</xsl:with-param>
			<xsl:with-param name="lab_instructor">講者</xsl:with-param>
			<xsl:with-param name="lab_instructor_select">-- 請選擇 --</xsl:with-param>
			<xsl:with-param name="lab_instructor_enter">輸入名稱</xsl:with-param>
			<xsl:with-param name="lab_moderator">主持人</xsl:with-param>
			<xsl:with-param name="lab_organization">機構</xsl:with-param>
			<xsl:with-param name="lab_time_limit">時限</xsl:with-param>
			<xsl:with-param name="lab_suggested_time">建議時間</xsl:with-param>
			<xsl:with-param name="lab_evt_datetime">時間</xsl:with-param>
			<xsl:with-param name="lab_evt_venue">地點</xsl:with-param>
			<xsl:with-param name="lab_wizpack">上傳 WizPack 檔案</xsl:with-param>
			<xsl:with-param name="lab_zip">上傳 Zip 檔案</xsl:with-param>
			<xsl:with-param name="lab_default_page">Zip檔案中首頁檔案</xsl:with-param>
			<xsl:with-param name="lab_grading_policy">評分制度</xsl:with-param>
			<xsl:with-param name="lab_display_option">顯示設置</xsl:with-param>
			<xsl:with-param name="lab_submit_detail">提交詳情</xsl:with-param>
			<xsl:with-param name="lab_detail_info">詳細資料</xsl:with-param>
			<xsl:with-param name="lab_event_info">活動資料</xsl:with-param>
			<xsl:with-param name="lab_file_crs">AICC 檔案(CRS)</xsl:with-param>
			<xsl:with-param name="lab_file_cst">AICC 檔案(CST)</xsl:with-param>
			<xsl:with-param name="lab_file_des">AICC 檔案(DES)</xsl:with-param>
			<xsl:with-param name="lab_file_au">AICC 檔案(AU)</xsl:with-param>
			<xsl:with-param name="lab_file_ort">AICC 檔案(ORT)</xsl:with-param>
			<xsl:with-param name="lab_enter_aicc">上傳 AICC 檔案</xsl:with-param>
			<xsl:with-param name="lab_previous">保留現有來源</xsl:with-param>
			<xsl:with-param name="lab_include_rating_que">評分</xsl:with-param>
			<xsl:with-param name="lab_yes">是</xsl:with-param>
			<xsl:with-param name="lab_no">否</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">儲存</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_max_attempt_num">允許參加的最大次數</xsl:with-param>
			<xsl:with-param name="lab_max_attempt_num_unlimited">無限制</xsl:with-param>
			<xsl:with-param name="lab_max_attempt_num_limited">限制到</xsl:with-param>
			<xsl:with-param name="lab_max_attempt_num_times">次</xsl:with-param>
			<xsl:with-param name="lab_show_answer_ind_text">每次參加測驗後對學員顯示題目及答案</xsl:with-param>
			<xsl:with-param name="lab_show_save_and_suspend_ind_text">允許學員保存答案並暫停測驗</xsl:with-param>
			<xsl:with-param name="lab_sub_after_passed_ind_text">不允許學員合格後再度參加</xsl:with-param>
			<xsl:with-param name="lab_clew">提示：你只能在各個班別裡設定顯示設置</xsl:with-param>
			<xsl:with-param name="lab_clewASS">提示：你只能在各個班別裡設定截止日期</xsl:with-param>
			<xsl:with-param name="lab_ass_due_date_unspecified">未指定</xsl:with-param>
			<xsl:with-param name="lab_managed_ind_text">由管理員控制測驗開始</xsl:with-param>
			<xsl:with-param name="lab_text_style">試卷樣式</xsl:with-param>
			<xsl:with-param name="lab_text_style_only">一屏單題 </xsl:with-param>
			<xsl:with-param name="lab_text_style_many">一屏多題 </xsl:with-param>
			<xsl:with-param name="lab_mod_show_answer_after_passed_ind_text">每次參加測驗合格後向學員顯示試題和答案</xsl:with-param>
			<xsl:with-param name="lab_on_show_answer_ind_text">每次參加測驗後不對學員顯示題目及答案</xsl:with-param>
			<xsl:with-param name="lab_duration_2">時長</xsl:with-param>
			<xsl:with-param name="lab_vod_img_link">視頻截圖</xsl:with-param>
			<xsl:with-param name="lab_vod_point">知識要點</xsl:with-param>
			<xsl:with-param name="lab_vdo_desc">請注意: 目前只支持編碼格式為 H264 (H264/AVC) 的<b>MP4</b>視頻文件。</xsl:with-param>
			<xsl:with-param name="lab_file_desc">>系統在處理檔案及其內容後有可能會出現內容失真或移位的情況。當你完成上載後，應該先預覽以確保處理完成後的內容能達到所需的效果。如果內容過於複雜(如Powerpoint)，建議於上載前自行轉換為 PDF格式。</xsl:with-param>
			<xsl:with-param name="lab_push_to_mobile">發佈到移動端</xsl:with-param>
			<xsl:with-param name="lab_only_pc_valid">以下設置只對PC端有效：</xsl:with-param>
			<xsl:with-param name="lab_text_style_sco">移動端顯示方式</xsl:with-param>
			<xsl:with-param name="lab_text_style_only_sco">豎屏</xsl:with-param>
			<xsl:with-param name="lab_text_style_many_sco">橫屏</xsl:with-param>
			<xsl:with-param name="lab_req_time_desc">學員完成該模塊所需要的學習時長。例如，這裡設置10分鐘，那麼學員在該模塊的學習時長累積要大於或等於10分鐘，才能完成該模塊。如果不指定，或指定為0，學員只要一打開該模塊就算完成了。</xsl:with-param>
			<xsl:with-param name="lab_aicc_sco_desc">如果你不打算將課件的分數納入課程總分數，或課件並不計算分數，只需將設置留空即可。<br/>註: 此設置只適用於系統內課程的「計分項目」。課件自身的計分及狀態由課件自行計算，不會被系統覆蓋。如錯誤輸入或會引致總分計算錯誤，甚至使課程無法完結。如果你沒有相關資料，請向內容提供者查詢。</xsl:with-param>
            <xsl:with-param name="lab_vod_remind">為保障移動端視頻流暢播放，以下為視頻大小、時長的建議值：<br/>視頻每分鐘大小建議<span style="color:#F00"><b>3-5M</b></span>內<br/>每個視頻播放時長建議<span style="color:#F00"><b>5-10分鐘</b></span>內</xsl:with-param>
			<xsl:with-param name="lab_vod_remind_new">為保障視頻流暢播放，請在保證基本清晰的情況下選擇大小盡可能小的視頻。</xsl:with-param>
			<xsl:with-param name="lab_time_unlimit">不限時</xsl:with-param>
			<xsl:with-param name="lab_time_limit_tip">"-1" 代表不限時</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_basic_info">基本资料</xsl:with-param>
			<xsl:with-param name="lab_mod_title">标题</xsl:with-param>
			<xsl:with-param name="lab_mod_duration">所需时间</xsl:with-param>
			<xsl:with-param name="lab_type">类型</xsl:with-param>
			<xsl:with-param name="lab_lang">语言</xsl:with-param>
			<xsl:with-param name="lab_desc">简介</xsl:with-param>
			<xsl:with-param name="lab_req_time">时长(分钟)</xsl:with-param>
			<xsl:with-param name="lab_download">是否允许在APP中下载</xsl:with-param>
			<xsl:with-param name="lab_prohibited">禁止</xsl:with-param>
			<xsl:with-param name="lab_allow">允许</xsl:with-param>
			<xsl:with-param name="download_tip">在线视频类型为不可下载。</xsl:with-param>
			<xsl:with-param name="lab_inst">布置作业</xsl:with-param>
			<xsl:with-param name="lab_diff">难度</xsl:with-param>
			<xsl:with-param name="lab_pass_score">合格率</xsl:with-param>
			<xsl:with-param name="lab_passing_score">合格分数</xsl:with-param>
			<xsl:with-param name="lab_selection_logic">选取逻辑</xsl:with-param>
			<xsl:with-param name="lab_template">模板</xsl:with-param>
			<xsl:with-param name="lab_url">网址(请输入完整的URL，格式为：http://host:port/.../xxx.mp4; 请不要使用相对路径。)</xsl:with-param>
			<xsl:with-param name="lab_online_video">在线视频</xsl:with-param>
			<xsl:with-param name="lab_status">状态</xsl:with-param>
			<xsl:with-param name="lab_max_score">满分</xsl:with-param>
			<xsl:with-param name="lab_ch">繁体中文</xsl:with-param>
			<xsl:with-param name="lab_gb">简体中文</xsl:with-param>
			<xsl:with-param name="lab_eng">英文</xsl:with-param>
			<xsl:with-param name="lab_online">已发布</xsl:with-param>
			<xsl:with-param name="lab_offline">未发布</xsl:with-param>
			<xsl:with-param name="lab_hard">困难</xsl:with-param>
			<xsl:with-param name="lab_normal">一般</xsl:with-param>
			<xsl:with-param name="lab_easy">容易</xsl:with-param>
			<xsl:with-param name="lab_upload_file">上传文档</xsl:with-param>
			<xsl:with-param name="lab_upload_video">上传视频</xsl:with-param>
			<xsl:with-param name="lab_uploaded_file">已上传文档</xsl:with-param>
			<xsl:with-param name="lab_uploaded_video">已上传视频</xsl:with-param>
			<xsl:with-param name="lab_source">来源</xsl:with-param>
			<xsl:with-param name="lab_ann">注释</xsl:with-param>
			<xsl:with-param name="lab_keep_file">保留文档</xsl:with-param>
			<xsl:with-param name="lab_change_to">更改为</xsl:with-param>
			<xsl:with-param name="lab_from_resource">选取自资源库</xsl:with-param>
			<xsl:with-param name="lab_ashtml">成为HTML?</xsl:with-param>
			<xsl:with-param name="lab_min">分钟</xsl:with-param>
			<xsl:with-param name="lab_random">随机性</xsl:with-param>
			<xsl:with-param name="lab_adaptive">适应性</xsl:with-param>
			<xsl:with-param name="lab_max_upload">最多提交</xsl:with-param>
			<xsl:with-param name="lab_submission">提交完成结束语</xsl:with-param>
			<xsl:with-param name="lab_submission_desc">(请使用不超过2000个的文字)</xsl:with-param>
			<xsl:with-param name="lab_due_date_format">年-月-日</xsl:with-param>
			<xsl:with-param name="lab_due_date_time_format">时:分</xsl:with-param>
			<xsl:with-param name="lab_notify_student">发送电子邮件确认作业提交</xsl:with-param>
			<xsl:with-param name="lab_file">文档标题</xsl:with-param>
			<xsl:with-param name="lab_due_date">作业提交截止时间</xsl:with-param>
			<xsl:with-param name="lab_due_date_num">天(从课程开始日期算起)</xsl:with-param>
			<xsl:with-param name="lab_due_date_num_inst_1">对于面授课程，课程的开始日期是班级的开始日期。</xsl:with-param>
			<xsl:with-param name="lab_due_date_num_inst_2">对于网上课程，课程的开始日期是注册确认日期。</xsl:with-param>
			<xsl:with-param name="lab_due_date_non_obligatory">(非强制)</xsl:with-param>
			<xsl:with-param name="lab_ass_inst">文字</xsl:with-param>
			<xsl:with-param name="lab_ass_inst_desc">(请使用不超过1000个的文字)</xsl:with-param>
			<xsl:with-param name="lab_unlimit">不限</xsl:with-param>
			<xsl:with-param name="lab_immediate">即时</xsl:with-param>
			<xsl:with-param name="lab_start_date">开始时间</xsl:with-param>
			<xsl:with-param name="lab_end_date">结束时间</xsl:with-param>
			<xsl:with-param name="lab_grading">评分方式</xsl:with-param>
			<xsl:with-param name="lab_grade">分级制</xsl:with-param>
			<xsl:with-param name="lab_by_score">打分</xsl:with-param>
			<xsl:with-param name="lab_not_limited">不限</xsl:with-param>
			<xsl:with-param name="lab_files">个文档</xsl:with-param>
			<xsl:with-param name="lab_no_of_submission">提交文档的数目</xsl:with-param>
			<xsl:with-param name="lab_instructor">讲者</xsl:with-param>
			<xsl:with-param name="lab_instructor_select">-- 请选择 --</xsl:with-param>
			<xsl:with-param name="lab_instructor_enter">输入名称</xsl:with-param>
			<xsl:with-param name="lab_moderator">主持人</xsl:with-param>
			<xsl:with-param name="lab_organization">机构</xsl:with-param>
			<xsl:with-param name="lab_time_limit">时限</xsl:with-param>
			<xsl:with-param name="lab_suggested_time">建议时间</xsl:with-param>
			<xsl:with-param name="lab_evt_datetime">时间</xsl:with-param>
			<xsl:with-param name="lab_evt_venue">地点</xsl:with-param>
			<xsl:with-param name="lab_wizpack">上传 WizPack 文档</xsl:with-param>
			<xsl:with-param name="lab_zip">上传 Zip 文档</xsl:with-param>
			<xsl:with-param name="lab_default_page">Zip文档中首页文档</xsl:with-param>
			<xsl:with-param name="lab_grading_policy">评分制度</xsl:with-param>
			<xsl:with-param name="lab_display_option">显示设置</xsl:with-param>
			<xsl:with-param name="lab_submit_detail">提交详情</xsl:with-param>
			<xsl:with-param name="lab_detail_info">详细资料</xsl:with-param>
			<xsl:with-param name="lab_event_info">活动资料</xsl:with-param>
			<xsl:with-param name="lab_file_crs">The Course File(CRS)</xsl:with-param>
			<xsl:with-param name="lab_file_cst">Course Structure File(CST)</xsl:with-param>
			<xsl:with-param name="lab_file_des">Description File(DES)</xsl:with-param>
			<xsl:with-param name="lab_file_au">Assignable Unit File(AU)</xsl:with-param>
			<xsl:with-param name="lab_file_ort">Objective Relationship File(ORT)</xsl:with-param>
			<xsl:with-param name="lab_enter_aicc">上传 AICC 文档</xsl:with-param>
			<xsl:with-param name="lab_previous">保留现有来源</xsl:with-param>
			<xsl:with-param name="lab_include_rating_que">评分</xsl:with-param>
			<xsl:with-param name="lab_yes">是</xsl:with-param>
			<xsl:with-param name="lab_no">否</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">保存</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_max_attempt_num">允许参加的最大次数</xsl:with-param>
			<xsl:with-param name="lab_max_attempt_num_unlimited">无限制</xsl:with-param>
			<xsl:with-param name="lab_max_attempt_num_limited">限制到</xsl:with-param>
			<xsl:with-param name="lab_max_attempt_num_times">次</xsl:with-param>
			<xsl:with-param name="lab_show_answer_ind_text">每次参加测验后向学员显示试题和答案</xsl:with-param>
			<xsl:with-param name="lab_show_save_and_suspend_ind_text">允许学员保存答案并暂停测验</xsl:with-param>
			<xsl:with-param name="lab_sub_after_passed_ind_text">禁止学员在成绩合格后继续参加测验</xsl:with-param>
			<xsl:with-param name="lab_clew">提示：您只能在各个班级里设定显示设置</xsl:with-param>
			<xsl:with-param name="lab_clewASS">提示：您只能在各个班级里设定作业提交截止时间</xsl:with-param>
			<xsl:with-param name="lab_ass_due_date_unspecified">未指定</xsl:with-param>
			<xsl:with-param name="lab_managed_ind_text">由管理员控制测验开始</xsl:with-param>
			<xsl:with-param name="lab_text_style">试卷样式</xsl:with-param>
			<xsl:with-param name="lab_text_style_only">一屏单题样式</xsl:with-param>
			<xsl:with-param name="lab_text_style_many">一屏多题样式</xsl:with-param>
			<xsl:with-param name="lab_mod_show_answer_after_passed_ind_text">每次参加测验合格后向学员显示试题和答案</xsl:with-param>
			<xsl:with-param name="lab_on_show_answer_ind_text">每次参加测验后不向学员显示试题和答案</xsl:with-param>
			<xsl:with-param name="lab_duration_2">时长</xsl:with-param>
			<xsl:with-param name="lab_vod_img_link">视频截图</xsl:with-param>
			<xsl:with-param name="lab_vod_point">知识要点</xsl:with-param>
			<xsl:with-param name="lab_vdo_desc">请注意：现在只支持<b>MP4(编码格式为H264)</b>格式的视频文件。</xsl:with-param>
			<xsl:with-param name="lab_file_desc">系统在处理文档及其内容后有可能会出现内容失真或移位的情况。当你完成上传后，应该先预览已确保处理完成后的内容能达到所需的效果。如果内容过于复杂（如Powerpoint），建议于上传前自行转换为PDF格式。</xsl:with-param>
			<xsl:with-param name="lab_push_to_mobile">发布到移动端</xsl:with-param>
			<xsl:with-param name="lab_only_pc_valid">以下设置只对PC端有效：</xsl:with-param>
			<xsl:with-param name="lab_text_style_sco">移动端显示方式</xsl:with-param>
			<xsl:with-param name="lab_text_style_only_sco">竖屏</xsl:with-param>
			<xsl:with-param name="lab_text_style_many_sco">横屏</xsl:with-param>
			<xsl:with-param name="lab_req_time_desc">学员完成该模块所需要的学习时长。例如，这里设置10分钟，那么学员在该模块的学习时长累积要大于或等于10分钟，才能完成该模块。</xsl:with-param>
			<xsl:with-param name="lab_aicc_sco_desc">如果你不打算将课件的分数纳入课程总分数，或课件并不计算分数，只需将设置留空即可。<br/>注: 此设置只适用于系统内课程的「计分项目」。课件自身的计分及状态由课件自行计算，不会被系统覆盖。如错误输入或会引致总分计算错误，甚至使课程无法完结。如果你没有相关数据，请向内容提供商查询。</xsl:with-param>
            <xsl:with-param name="lab_vod_remind">为保障移动端视频流畅播放，以下为视频大小、时长的建议值：<br/>视频每分钟大小建议<span style="color:#F00"><b>3-5M</b></span>内 <br/>每个视频播放时长建议<span style="color:#F00"><b>5-10分钟</b></span>内</xsl:with-param>
			<xsl:with-param name="lab_vod_remind_new">为保障视频流畅播放，请在保证基本清晰的情况下选择大小尽可能小的视频。</xsl:with-param>
			<xsl:with-param name="lab_time_unlimit">不限时</xsl:with-param>
			<xsl:with-param name="lab_time_limit_tip">"-1" 代表不限时</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_basic_info">Basic information</xsl:with-param>
			<xsl:with-param name="lab_mod_title">Title</xsl:with-param>
			<xsl:with-param name="lab_mod_duration">Duration</xsl:with-param>
			<xsl:with-param name="lab_type">Type</xsl:with-param>
			<xsl:with-param name="lab_lang">Language</xsl:with-param>
			<xsl:with-param name="lab_desc">Description</xsl:with-param>
			<xsl:with-param name="lab_req_time">Duration(minutes)</xsl:with-param>
			<xsl:with-param name="lab_download">Allow download in App</xsl:with-param>
			<xsl:with-param name="lab_prohibited">Prohibited</xsl:with-param>
			<xsl:with-param name="lab_allow">Allow</xsl:with-param>
			<xsl:with-param name="download_tip">Online video type (e.g. youtube) are always prohibited from download.</xsl:with-param>
			<xsl:with-param name="lab_inst">Instruction</xsl:with-param>
			<xsl:with-param name="lab_diff">Difficulty</xsl:with-param>
			<xsl:with-param name="lab_pass_score">Passing percentage</xsl:with-param>
			<xsl:with-param name="lab_passing_score">Passing score</xsl:with-param>
			<xsl:with-param name="lab_selection_logic">Question selection logics</xsl:with-param>
			<xsl:with-param name="lab_template">Template</xsl:with-param>
			<xsl:with-param name="lab_url">URL (Please enter complete URL, e.g. http://host:port/xxx/xxx.mp4, and do NOT use relative path)</xsl:with-param>
			<xsl:with-param name="lab_online_video">Online video</xsl:with-param>
			<xsl:with-param name="lab_status">Status</xsl:with-param>
			<xsl:with-param name="lab_max_score">Maximum score</xsl:with-param>
			<xsl:with-param name="lab_ch">Traditional Chinese</xsl:with-param>
			<xsl:with-param name="lab_gb">Simplified Chinese</xsl:with-param>
			<xsl:with-param name="lab_eng">English</xsl:with-param>
			<xsl:with-param name="lab_online">Published</xsl:with-param>
			<xsl:with-param name="lab_offline">Unpublished</xsl:with-param>
			<xsl:with-param name="lab_hard">Hard</xsl:with-param>
			<xsl:with-param name="lab_normal">Normal</xsl:with-param>
			<xsl:with-param name="lab_easy">Easy</xsl:with-param>
			<xsl:with-param name="lab_upload_file">Upload file</xsl:with-param>
			<xsl:with-param name="lab_upload_video">Upload video</xsl:with-param>
			<xsl:with-param name="lab_uploaded_file">Uploaded file</xsl:with-param>
			
			<xsl:with-param name="lab_uploaded_video">Uploaded video</xsl:with-param>
			
			<xsl:with-param name="lab_source">Source</xsl:with-param>
			<xsl:with-param name="lab_ann">Annotation</xsl:with-param>
			<xsl:with-param name="lab_keep_file">Keep the file</xsl:with-param>
			<xsl:with-param name="lab_change_to">Change to</xsl:with-param>
			<xsl:with-param name="lab_from_resource">From Learning Resource Management</xsl:with-param>
			<xsl:with-param name="lab_ashtml">As HTML?</xsl:with-param>
			<xsl:with-param name="lab_min">min(s)</xsl:with-param>
			<xsl:with-param name="lab_random">Random</xsl:with-param>
			<xsl:with-param name="lab_adaptive">Adaptive</xsl:with-param>
			<xsl:with-param name="lab_max_upload">Limited to</xsl:with-param>
			<xsl:with-param name="lab_submission">Message to learner after submission</xsl:with-param>
			<xsl:with-param name="lab_submission_desc">(Not more than 2000 characters)</xsl:with-param>
			<xsl:with-param name="lab_due_date_format">YYYY-MM-DD</xsl:with-param>
			<xsl:with-param name="lab_due_date_time_format">HH:MM</xsl:with-param>
			<xsl:with-param name="lab_notify_student">E-mail acknowledgment to submission</xsl:with-param>
			<xsl:with-param name="lab_file">File title</xsl:with-param>
			<xsl:with-param name="lab_due_date">Due date</xsl:with-param>
			<xsl:with-param name="lab_due_date_num">days since the course started.</xsl:with-param>
			<xsl:with-param name="lab_due_date_num_inst_1">For Classroom Course, the course start date is the class start date.</xsl:with-param>
			<xsl:with-param name="lab_due_date_num_inst_2">For Web-based Course, the course start date is the enrollment confirmation date.</xsl:with-param>
			<xsl:with-param name="lab_due_date_non_obligatory">(non-obligatory)</xsl:with-param>
			<xsl:with-param name="lab_ass_inst">Text</xsl:with-param>
			<xsl:with-param name="lab_ass_inst_desc">(Not more than 1000 characters)</xsl:with-param>
			<xsl:with-param name="lab_unlimit">Unlimited</xsl:with-param>
			<xsl:with-param name="lab_immediate">Immediate</xsl:with-param>
			<xsl:with-param name="lab_start_date">Available after</xsl:with-param>
			<xsl:with-param name="lab_end_date">Available until</xsl:with-param>
			<xsl:with-param name="lab_grading">Grading scheme</xsl:with-param>
			<xsl:with-param name="lab_grade">By letter grade</xsl:with-param>
			<xsl:with-param name="lab_by_score">By score</xsl:with-param>
			<xsl:with-param name="lab_not_limited">Unimited</xsl:with-param>
			<xsl:with-param name="lab_files">files</xsl:with-param>
			<xsl:with-param name="lab_no_of_submission">No. of files to be submitted</xsl:with-param>
			<xsl:with-param name="lab_instructor">Lecturer</xsl:with-param>
			<xsl:with-param name="lab_instructor_select">-- Please select --</xsl:with-param>
			<xsl:with-param name="lab_instructor_enter">Enter name</xsl:with-param>
			<xsl:with-param name="lab_moderator">Moderator</xsl:with-param>
			<xsl:with-param name="lab_organization">Organization</xsl:with-param>
			<xsl:with-param name="lab_time_limit">Time limit</xsl:with-param>
			<xsl:with-param name="lab_suggested_time">Suggested time</xsl:with-param>
			<xsl:with-param name="lab_evt_datetime">Event date</xsl:with-param>
			<xsl:with-param name="lab_evt_venue">Venue</xsl:with-param>
			<xsl:with-param name="lab_wizpack">Upload wizPack</xsl:with-param>
			<xsl:with-param name="lab_zip">Upload zip file</xsl:with-param>
			<xsl:with-param name="lab_default_page">Index file among the zipped files</xsl:with-param>
			<xsl:with-param name="lab_grading_policy">Grading policy</xsl:with-param>
			<xsl:with-param name="lab_display_option">Release schedule</xsl:with-param>
			<xsl:with-param name="lab_submit_detail">Submission details</xsl:with-param>
			<xsl:with-param name="lab_detail_info">Detail information</xsl:with-param>
			<xsl:with-param name="lab_event_info">Event information</xsl:with-param>
			<xsl:with-param name="lab_file_crs">The course file(CRS)</xsl:with-param>
			<xsl:with-param name="lab_file_cst">Course structure file(CST)</xsl:with-param>
			<xsl:with-param name="lab_file_des">Description file(DES)</xsl:with-param>
			<xsl:with-param name="lab_file_au">Assignable unit file(AU)</xsl:with-param>
			<xsl:with-param name="lab_file_ort">Objective relationship file(ORT)</xsl:with-param>
			<xsl:with-param name="lab_enter_aicc">Upload AICC files</xsl:with-param>
			<xsl:with-param name="lab_previous">Keep existing source</xsl:with-param>
			<xsl:with-param name="lab_include_rating_que">Include rating question</xsl:with-param>
			<xsl:with-param name="lab_yes">Yes</xsl:with-param>
			<xsl:with-param name="lab_no">No</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_max_attempt_num">Attempt limit</xsl:with-param>
			<xsl:with-param name="lab_max_attempt_num_unlimited">Unlimited</xsl:with-param>
			<xsl:with-param name="lab_max_attempt_num_limited">Limited to</xsl:with-param>
			<xsl:with-param name="lab_max_attempt_num_times">attempt(s)</xsl:with-param>
			<xsl:with-param name="lab_show_answer_ind_text">Show questions and answers to learners after each attempt</xsl:with-param>
			<xsl:with-param name="lab_show_save_and_suspend_ind_text">Allow learner to save and suspend the test</xsl:with-param>
			<xsl:with-param name="lab_sub_after_passed_ind_text">Do not allow learners to take further attempts after passing the test</xsl:with-param>
			<xsl:with-param name="lab_clew">Note: You can set the release schedule at individual class</xsl:with-param>
			<xsl:with-param name="lab_clewASS">Note: You can set the due date at individual class</xsl:with-param>
			<xsl:with-param name="lab_ass_due_date_unspecified">Unspecified</xsl:with-param>
			<xsl:with-param name="lab_managed_ind_text">The test need to be started by administrator</xsl:with-param>
			<xsl:with-param name="lab_text_style">Test</xsl:with-param>
			<xsl:with-param name="lab_text_style_only">Show only one question on a screen </xsl:with-param>
			<xsl:with-param name="lab_text_style_many">Show a list of questions on a screen </xsl:with-param>			<xsl:with-param name="lab_mod_show_answer_after_passed_ind_text">Display the questions and answers only when the test is passed</xsl:with-param>
			<xsl:with-param name="lab_on_show_answer_ind_text">Do not display questions and answers to students after test</xsl:with-param>
			<xsl:with-param name="lab_duration_2">Duration</xsl:with-param>
			<xsl:with-param name="lab_vod_img_link">Icon</xsl:with-param>
			<xsl:with-param name="lab_vod_point">Knowledge points</xsl:with-param>
			<xsl:with-param name="lab_vdo_desc">Please note that currently system only supports video file in <b>MP4</b> format with encoding in H264 (H264/AVC).</xsl:with-param>
			<xsl:with-param name="lab_file_desc">There may be some distortion after various processing of the file and content in the system. You should preview whether the outcome can fulfill the expectation whenever you have uploaded a new file. If the file contains some complicated layout (e.g. Powerpoint), it is recommended to convert to PDF before upload to system.</xsl:with-param>
			<xsl:with-param name="lab_push_to_mobile">Publish to mobile</xsl:with-param>
			<xsl:with-param name="lab_only_pc_valid">The following settings are valid only for the PC:</xsl:with-param>
			<xsl:with-param name="lab_text_style_sco">Mobile Display Method</xsl:with-param>
			<xsl:with-param name="lab_text_style_only_sco">Portrait</xsl:with-param>
			<xsl:with-param name="lab_text_style_many_sco">Landscape</xsl:with-param>
			<xsl:with-param name="lab_req_time_desc">Learner is supposed to take some time to complete the module. For example, if the pre-set time is 10 minutes, learner is required to spend at least 10 minutes in the module to get it completed. If there is no pre-set time, or it is set to be 0, the module will be marked as completed once the learner clicked in the module.</xsl:with-param>
			<xsl:with-param name="lab_aicc_sco_desc">If you decided not to include the score in the course’s final score or the courseware does not calculate score, simply leave them blank.<br/>Remark: These settings will only apply to course’s “scoring item” of the system. The original scoring of the learning content and its status will not be overridden. If these scores were wrongly input, it may lead to the incorrect final score calculation, and even affecting the completion status of course. If you do not know the scores, please seek advice from your content provider.</xsl:with-param>
            <xsl:with-param name="lab_vod_remind">In order to protect the mobile video smooth playback, the following for the video size of the long recommended value：<br/>Video size recommended per minute within <span style="color:#F00"><b>3-5M</b></span><br/>Each video playback time is recommended within <span style="color:#F00"><b>5-10 minutes</b></span></xsl:with-param>
			<xsl:with-param name="lab_vod_remind_new">To ensure smooth video playback, please select a size under the condition of the guarantee basic clear video as small as possible.</xsl:with-param>
			<xsl:with-param name="lab_time_unlimit">Unlimited</xsl:with-param>
			<xsl:with-param name="lab_time_limit_tip">Enter "-1" for unlimited time</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- ======================================================================= -->
	<xsl:template name="content">
		<xsl:param name="lab_adaptive"/>
		<xsl:param name="lab_ann"/>
		<xsl:param name="lab_ashtml"/>
		<xsl:param name="lab_ass_inst"/>
		<xsl:param name="lab_ass_inst_desc"/>
		<xsl:param name="lab_basic_info"/>
		<xsl:param name="lab_by_score"/>
		<xsl:param name="lab_ch"/>
		<xsl:param name="lab_change_to"/>
		<xsl:param name="lab_default_page"/>
		<xsl:param name="lab_desc"/>
		<xsl:param name="lab_req_time"/>
		<xsl:param name="lab_download"/>
		<xsl:param name="lab_prohibited"/>
		<xsl:param name="lab_allow"/>
		<xsl:param name="download_tip"/>
		<xsl:param name="lab_detail_info"/>
		<xsl:param name="lab_diff"/>
		<xsl:param name="lab_display_option"/>
		<xsl:param name="lab_due_date"/>
		<xsl:param name="lab_due_date_format"/>
		<xsl:param name="lab_due_date_num"/>
		<xsl:param name="lab_due_date_num_inst_1"/>
		<xsl:param name="lab_due_date_num_inst_2"/>
		<xsl:param name="lab_due_date_non_obligatory"/>
		<xsl:param name="lab_due_date_time_format"/>
		<xsl:param name="lab_easy"/>
		<xsl:param name="lab_end_date"/>
		<xsl:param name="lab_eng"/>
		<xsl:param name="lab_enter_aicc"/>
		<xsl:param name="lab_event_info"/>
		<xsl:param name="lab_evt_datetime"/>
		<xsl:param name="lab_evt_venue"/>
		<xsl:param name="lab_file"/>
		<xsl:param name="lab_file_au"/>
		<xsl:param name="lab_file_crs"/>
		<xsl:param name="lab_file_cst"/>
		<xsl:param name="lab_file_des"/>
		<xsl:param name="lab_file_ort"/>
		<xsl:param name="lab_files"/>
		<xsl:param name="lab_from_resource"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:param name="lab_g_form_btn_ok"/>
		<xsl:param name="lab_gb"/>
		<xsl:param name="lab_grade"/>
		<xsl:param name="lab_grading"/>
		<xsl:param name="lab_grading_policy"/>
		<xsl:param name="lab_hard"/>
		<xsl:param name="lab_immediate"/>
		<xsl:param name="lab_include_rating_que"/>
		<xsl:param name="lab_inst"/>
		<xsl:param name="lab_instructor"/>
		<xsl:param name="lab_instructor_enter"/>
		<xsl:param name="lab_instructor_select"/>
		<xsl:param name="lab_keep_file"/>
		<xsl:param name="lab_lang"/>
		<xsl:param name="lab_max_attempt_num"/>
		<xsl:param name="lab_max_attempt_num_limited"/>
		<xsl:param name="lab_max_attempt_num_times"/>
		<xsl:param name="lab_max_attempt_num_unlimited"/>
		<xsl:param name="lab_max_score"/>
		<xsl:param name="lab_max_upload"/>
		<xsl:param name="lab_min"/>
		<xsl:param name="lab_mod_duration"/>
		<xsl:param name="lab_mod_title"/>
		<xsl:param name="lab_moderator"/>
		<xsl:param name="lab_no"/>
		<xsl:param name="lab_no_of_submission"/>
		<xsl:param name="lab_normal"/>
		<xsl:param name="lab_not_limited"/>
		<xsl:param name="lab_notify_student"/>
		<xsl:param name="lab_offline"/>
		<xsl:param name="lab_online"/>
		<xsl:param name="lab_organization"/>
		<xsl:param name="lab_pass_score"/>
		<xsl:param name="lab_passing_score"/>
		
		<xsl:param name="lab_previous"/>
		<xsl:param name="lab_random"/>
		<xsl:param name="lab_selection_logic"/>
		<xsl:param name="lab_show_answer_ind_text"/>
		<xsl:param name="lab_show_save_and_suspend_ind_text"/>
		<xsl:param name="lab_source"/>
		<xsl:param name="lab_start_date"/>
		<xsl:param name="lab_status"/>
		<xsl:param name="lab_sub_after_passed_ind_text"/>
		<xsl:param name="lab_submission"/>
		<xsl:param name="lab_submission_desc"/>
		<xsl:param name="lab_submit_detail"/>
		<xsl:param name="lab_suggested_time"/>
		<xsl:param name="lab_template"/>
		<xsl:param name="lab_time_limit"/>
		<xsl:param name="lab_type"/>
		<xsl:param name="lab_unlimit"/>
		<xsl:param name="lab_upload_file"/>
		<xsl:param name="lab_upload_video"/>
		<xsl:param name="lab_uploaded_file"/>
		<xsl:param name="lab_uploaded_video"/>
		<xsl:param name="lab_time_unlimit"/>
		<xsl:param name="lab_time_limit_tip"/>
		
		<xsl:param name="lab_url"/>
		<xsl:param name="lab_online_video"/>
		<xsl:param name="lab_wizpack"/>
		<xsl:param name="lab_yes"/>
		<xsl:param name="lab_zip"/>
		<xsl:param name="lab_clew"/>
		<xsl:param name="lab_clewASS"/>
		<xsl:param name="lab_ass_due_date_unspecified"/>
		<xsl:param name="lab_managed_ind_text"/>
		<xsl:param name="lab_text_style"/>
		<xsl:param name="lab_text_style_only"/>
		<xsl:param name="lab_text_style_many"/>
		<xsl:param name="lab_mod_show_answer_after_passed_ind_text"/>
		<xsl:param name="lab_on_show_answer_ind_text"/>
		<xsl:param name="lab_duration_2"/>
		<xsl:param name="lab_vod_img_link"/>
		<xsl:param name="lab_vod_point"/>
		<xsl:param name="lab_vdo_desc"/>
		<xsl:param name="lab_file_desc"/>
		<xsl:param name="lab_push_to_mobile"/>
		<xsl:param name="lab_only_pc_valid"/>
		<xsl:param name="lab_text_style_sco"/>
		<xsl:param name="lab_text_style_only_sco"/>
		<xsl:param name="lab_text_style_many_sco"/>
		
		<xsl:param name="lab_req_time_desc" />
		<xsl:param name="lab_aicc_sco_desc" />
		<xsl:param name="lab_vod_remind" />
		<xsl:param name="lab_vod_remind_new"/>
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<xsl:call-template name="new_css"/>
			<script language="JavaScript" src="{$wb_js_path}gen_utils.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_module.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_course.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_utils.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_media.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_resource.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_aicc.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}urlparam.js" type="text/javascript"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}jquery.js"/>
			
			<script type="text/javascript" src="../../static/js/i18n/{$wb_cur_lang}/global_{$wb_cur_lang}.js" language="JavaScript"/>
			<script type="text/javascript" src="../../static/js/i18n/{$wb_cur_lang}/label_tm_{$wb_cur_lang}.js" language="JavaScript"/>
			<script language="Javascript" type="text/javascript" src="../../static/js/cwn_utils.js"/>
			
			<!--alert样式  -->
			 <link rel="stylesheet" href="../static/js/jquery.qtip/jquery.qtip.css" />
			<script type="text/javascript" src="../static/js/jquery.dialogue.js"></script>
			<script type="text/javascript" src="../static/js/jquery.qtip/jquery.qtip.js"></script>
			
			<script LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
				
			var doAll = (document.all!=null) // IE
			var doDOM =(document.getElementById!=null) //Netscape 6 DOM 1.0
			var doLayer =(document.layers!=null) // Netscape 4.x
			
			mod = new wbModule;
			cos = new wbCourse; 		
			res = new wbResource
			cos_id = getUrlParam("course_id")   	
			cookie_course_id = getUrlParam("course_id");
			cos_name = wb_utils_get_cookie("title_prev")
			wb_utils_set_cookie('mod_type',']]><xsl:value-of select="$mod_subtype"/><![CDATA[')
			tpl_type = ']]><xsl:value-of select="$mod_subtype"/><![CDATA['							
			var hide_content = ''	
			
			//--==Man: For Dynamic Source Type radio button , don't remove
			var src_type_wizpack_id 
			var src_type_file_id 
			var src_type_zipfile_id 
			var src_type_aicc_id 
			var src_type_url_id
			var src_type_ass_inst_id 
			var src_type_pick_aicc_res_id 
			var src_type_pick_res_id 
			var src_type_res_id 
			//--===		
			
			function togStatus(ratingQue){
				frmXml.mod_has_rate_q.value = ratingQue;
			}
					
			function changeTemplate() {
				var frm = document.frmXml;
				var i = frm.tpl_name.selectedIndex;
				
				frm.templatethumb.src = frm[frm.tpl_name.options[i].text + "thumb"].value;
				if (doAll || doDOM){
				var tpl_desc_val = frm[frm.tpl_name.options[i].text + "desc"].value;
				var tpl_notes_val = frm[frm.tpl_name.options[i].text + "note"].value;
				writeElement('tpldesc',tpl_desc_val);
				writeElement('tplnotes',tpl_notes_val);}
				else
				{
						frm.tpl_desc.value = frm[frm.tpl_name.options[i].text + "desc"].value;
						frm.tpl_notes.value = frm[frm.tpl_name.options[i].text + "note"].value;
				}				
			}
							
			function writeElement(id, contents) {
				if(doAll){
	        		var obj= document.all[id]
				}else if(doDOM){
					var obj = document.getElementById(id)
				}
	        	if (obj!=null){
	            obj.innerHTML = contents;
				}
			}
			 			
			function GetCSSPElement(id) {
				if (doAll) {
					return document.all[id];
				}else{
					if (doDOM)
						return document.getElementById(id)
					else
			          return document.layers[id];
				}
			}

			function AssWriteElement(id, contents) {
			        var pEl = GetCSSPElement(id)
			        if (pEl!=null)
			          if (doAll || doDOM)
			            pEl.innerHTML = contents
			          else {
			            pEl.document.open()
			            pEl.document.write(contents) 
			            pEl.document.close()
			          }
			}
	
			function check_type(){
				]]><xsl:if test="count(header/template_list/template) > 1 ">changeTemplate();</xsl:if><![CDATA[					
			}
				
			function ins_default_date(){
				var frm = document.frmXml
				//Get the server current time
				str = "]]><xsl:value-of select="header/@cur_time"/><![CDATA["
				cur_day = str.substring((str.lastIndexOf('-') + 1), str.indexOf(' '))
				cur_month = str.substring((str.indexOf('-') + 1), str.lastIndexOf('-'))
				cur_year = str.substring(0, str.indexOf('-'))
				cur_hour = str.substring((str.indexOf(' ') + 1), str.indexOf(':'))
				cur_min = str.substring((str.indexOf(':') + 1), str.lastIndexOf(':'))
					
				frm.cur_dt_dd.value = cur_day
				frm.cur_dt_mm.value = cur_month
				frm.cur_dt_yy.value = cur_year
				frm.cur_dt_hour.value = cur_hour
				frm.cur_dt_min.value = cur_min
				
				]]><xsl:if test="( header/@subtype != 'CHT' and header/@subtype  != 'VCR') or translate(substring-before(header/@cur_time, '.'), ':- ', '') &lt; translate(substring-before(header/@eff_start_datetime, '.'), ':- ', '')"><![CDATA[
				if (frm.start_date){

					if (frm.start_date[0].checked == true){
						frm.start_mm.value = cur_month
						frm.start_yy.value = cur_year
						
						frm.start_hour.value = "00"
						frm.start_min.value = "00"
					}
				}
				]]></xsl:if><![CDATA[
				
				if (frm.end_date){					
					if (frm.end_date[0].checked == true){
						frm.end_mm.value = cur_month
						frm.end_yy.value = cur_year
						
						frm.end_hour.value = "23"
						frm.end_min.value = "59"
					}
				}
			}
				
			function isMaxUpload(){
				if ( document.frmXml.max_uplaod_file[0].checked == true )
					document.frmXml.ass_max_upload.value = ''
			}
				
			function putHTMLvalue(){
				if(document.forms[0].mod_instr.type!="textarea"){
				var myText = getText()
					if(myText.length != 0){
						document.forms[0].mod_instr.value = getHTML();
					}
				}
			}
			
			function chg_max_attp_num(frm){
				frm.max_usr_attempt_unlimited_num.value = '';	
			}
			
			function chg_due_date_ts(frm){
				frm.due_dd.value = ''
				frm.due_mm.value = ''
				frm.due_yy.value = ''
				frm.due_hour.value = ''
				frm.due_min.value = ''
			}
			
			function chg_due_date_day(frm){
				frm.ass_due_date_day.value = ''
			}
			
			function invoke_after_pick(){
				var imgpath = ]]>'<xsl:value-of select="$wb_img_path"/>'<![CDATA[
				var frm = document.frmXml;
				
				if(frm.source_type && frm.source_content && document['src_type_img']){
				
					var source_type = frm.source_type.value;
					var source_content = frm.source_content.value;
					var file_ext = get_file_ext(source_content);
					if(frm.src_type_display){
						frm.src_type_display.value = frm.source_content.value;
					}
					/*if(frm.source_content.value.length > 60){
						var display_str = frm.source_content.value.substring(0,30) + "..." + frm.source_content.value.substring( frm.source_content.value.length - 27, frm.source_content.value.length)
					}else{
						var display_str = frm.source_content.value;
					}
					var contents = "<a class=\"Text\" href=\"" +frm.source_content.value + "\" target=\"_blank\" >" +display_str+ "</a>";
					writeElement('src_display',contents)
					*/
					if(source_content.search('http://') != -1){
						document['src_type_img'].src = imgpath + 'icon_web_browser.gif';
					}					
					switch (file_ext){
						
							case 'doc' :
								document['src_type_img'].src = imgpath + 'icon_word.gif';
							break;
							case 'ppt' :
								document['src_type_img'].src = imgpath + 'icon_ppt.gif';
							break;			
							case 'txt' :
								document['src_type_img'].src = imgpath + 'icon_notepad.gif';
							break;														
							case 'html' :
								document['src_type_img'].src = imgpath + 'icon_web_browser.gif';
							break;									
							case 'htm' :
								document['src_type_img'].src = imgpath + 'icon_web_browser.gif';
							break;	
							case 'swf' :
								document['src_type_img'].src = imgpath + 'icon_swf.gif';
							break;		
							case 'gif' :
								document['src_type_img'].src = imgpath + 'icon_gif.gif';
							break;		
							case 'jpg' :
								document['src_type_img'].src = imgpath + 'icon_jpg.gif';
							break;		
							case 'exe' :
								document['src_type_img'].src = imgpath + 'icon_exe.gif';
							break;	
							case 'xls' :
								document['src_type_img'].src = imgpath + 'icon_xls.gif';
							break;	
							case 'pdf' :
								document['src_type_img'].src = imgpath + 'icon_pdf.gif';
							break;																																												
												
					}
				}
				
			}
			
			function get_file_ext(href){
				return href.substring(href.lastIndexOf('.') + 1,href.length)
			}	
			
			 function get_check(divnode)
			    {
			     var radio=divnode.previousSibling.previousSibling.previousSibling;
			     radio.checked="checked";
			    }
			
			function checkModAndSave(){
				var is_public = ]]>'<xsl:value-of select="$res_status"/>'<![CDATA[;
				var is_relation_com = ]]>'<xsl:value-of select="$is_relation_com"/>'<![CDATA[;
				var frm = document.frmXml;
				var itmContentDef = ]]>'<xsl:value-of select="$itmContentDef"/>'<![CDATA[;
				var status=frm.mod_status.value;
				var is_Save = true;
				if(frm.mod_subtype.value!='ASS'&&frm.mod_subtype.value!='RDG'&&frm.src_type&&frm.src_type.value=='FILE'&&frm.mod_file.value!=null&&frm.mod_file.value!=''&&!/.+\.mp4$/.test(frm.mod_file.value.toLowerCase())){
				   Dialog.alert(']]><xsl:value-of select="$lab_vdo_desc"/><![CDATA[');			 
				   is_Save =false;			
				}else if(is_public =='ON' && status=='OFF' && is_relation_com === 'true' && itmContentDef != 'PARENT'){
					if(!confirm(eval('wb_msg_mod_relation_com_not_public'))){
						is_Save =false;
					}else{
						is_Save=true;
					}
				}
				if(is_Save){ 
				is_Save = cos.check(document.frmXml, ']]><xsl:value-of select="$wb_lang"/><![CDATA[');
                }
                if(is_Save){                                                                                                             
					mod.upd_exec(document.frmXml,]]>'<xsl:value-of select="$attp"/>'<![CDATA[,]]>'<xsl:value-of select="$wb_lang"/>'<![CDATA[,]]>'<xsl:value-of select="$source_link"/>'<![CDATA[)
				}
			}
			function setInfo(frm){
				var show_answer_ind = ]]>'<xsl:value-of select="$show_answer_ind"/>'<![CDATA[;
				var	mod_show_answer_after_passed_ind= ]]>'<xsl:value-of select="$mod_show_answer_after_passed_ind"/>'<![CDATA[;
				if(show_answer_ind=='0'&&mod_show_answer_after_passed_ind!=1){
				frm.mod_show_answer_ind_chk[1].checked=true; 
				}
				if(show_answer_ind=='1'){
				frm.mod_show_answer_ind_chk[0].checked=true; 
				}
			}
			
			function changeSourceType() {
				var types = document.getElementsByName('src_type');
				var download_rdo = document.getElementsByName("mod_download_ind");
				for(var i=0; i<types.length; i++) {
					if(types[i].checked) {
						if(i == 0 || i == 2) {
							download_rdo[0].checked = true;
							download_rdo[1].disabled = true;
						} else {
							download_rdo[1].disabled = false;
						}
					}
				}
			}
			
		]]></script>
		</head>
		<xsl:call-template name="kindeditor_css"/>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onunload="wb_utils_close_preloading()" onLoad="ins_default_date();setInfo(document.frmXml)">
			<form name="frmXml" enctype="multipart/form-data" onsubmit="return false">
				<input type="hidden" name="lab_start_date" value="{$lab_start_date}"/>
				<input type="hidden" name="lab_end_date" value="{$lab_end_date}"/>
				<input type="hidden" name="cur_dt_dd" value=""/>
				<input type="hidden" name="cur_dt_mm" value=""/>
				<input type="hidden" name="cur_dt_yy" value=""/>
				<input type="hidden" name="cur_dt_hour" value=""/>
				<input type="hidden" name="cur_dt_min" value=""/>
				<input type="hidden" name="mod_logic"/>
				<input type="hidden" name="mod_show_answer_ind"	value=""/>
				<input type="hidden" name="cos_type" value="{$cos_type}"/>
				<!-- hidden field -->
				<xsl:if test="$mod_subtype='SVY'">
					<input type="hidden" name="mod_max_usr_attempt" value="1"/>
					<input type="hidden" name="mod_has_rate_q">
						<xsl:choose>
							<xsl:when test="header/@has_rate_q='true'">
								<xsl:attribute name="value">true</xsl:attribute>
							</xsl:when>
							<xsl:otherwise>
								<xsl:attribute name="value">false</xsl:attribute>
							</xsl:otherwise>
						</xsl:choose>
					</input>
				</xsl:if>
				<xsl:if test="$mod_subtype = 'AICC_AU'">
					<input value="no" name="rename" type="hidden"/>
				</xsl:if>
				<xsl:if test="$mod_subtype = 'NETG_COK'">
					<input value="no" name="rename" type="hidden"/>
					<input name="mod_duration" value="{$dur}" type="hidden"/>
				</xsl:if>
				<input type="hidden" name="annonation_html">
					<xsl:attribute name="value"><xsl:if test="/module/header/annotation/html">y</xsl:if></xsl:attribute>
				</input>
				<xsl:call-template name="wb_ui_head">
					<xsl:with-param name="text" select="$lab_basic_info"/>
				</xsl:call-template>
				<xsl:call-template name="wb_ui_line"/>
				<table>
					<xsl:call-template name="table_seperator"/>
					<!-- Module Type -->
					<xsl:call-template name="draw_mod_type">
						<xsl:with-param name="lab_type" select="$lab_type"/>
						<xsl:with-param name="mod_type" select="$mod_subtype"/>
					</xsl:call-template>
					<!-- Module Title -->
					<xsl:call-template name="draw_title">
						<xsl:with-param name="lab_title" select="$lab_mod_title"/>
						<xsl:with-param name="value" select="$title"/>
					</xsl:call-template>
					<!-- Module Desc -->
					<xsl:call-template name="draw_desc">
						<xsl:with-param name="lab_desc" select="$lab_desc"/>
						<xsl:with-param name="value" select="$desc"/>
					</xsl:call-template>
					
<!-- 					<xsl:if test="$cos_type = 'SELFSTUDY' and ($mod_subtype = 'VOD' or $mod_subtype = 'TST' or $mod_subtype = 'DXT' or $mod_subtype = 'SVY')"> -->
					<xsl:if test="$mod_subtype != 'AICC_AU'">
						<xsl:call-template name="draw_push_to_mobile">
							<xsl:with-param name="lab_push_to_mobile" select="$lab_push_to_mobile"/>
							<xsl:with-param name="lab_yes" select="$lab_yes"/>
							<xsl:with-param name="lab_no" select="$lab_no"/>
							<xsl:with-param name="mod_mobile_ind" select="/module/header/@mod_mobile_ind"/>
						</xsl:call-template>
					</xsl:if>
<!-- 					</xsl:if> -->
					<xsl:if test="$mod_subtype = 'SCO' or $mod_subtype = 'AICC_AU'">
					<!--测试一屏多题  -->	
							<xsl:call-template name="draw_text_style">
								<xsl:with-param name="select" select="header/@test_style"/>
								<xsl:with-param name="lab_title" select="$lab_text_style_sco"/>
								<xsl:with-param name="lab_style1" select="$lab_text_style_only_sco"/>
								<xsl:with-param name="lab_style2" select="$lab_text_style_many_sco"/>
							</xsl:call-template>
					</xsl:if>
					<!-- Module Desc -->
					<xsl:if test="$mod_subtype = 'VOD' or $mod_subtype = 'RDG' or $mod_subtype = 'REF'">
						<xsl:if test="$mod_subtype = 'VOD'">
							<xsl:if test="$cos_type = 'AUDIOVIDEO'">
								<xsl:call-template name="draw_img_file">
									<xsl:with-param name="lab_upload_file" select="$lab_vod_img_link"/>
									<xsl:with-param name="lab_change_to" select="$lab_change_to"/>
									<xsl:with-param name="source_link" select="$source_img_link"/>
								</xsl:call-template>
							</xsl:if>
						</xsl:if>
						<xsl:call-template name="draw_req_time">
							<xsl:with-param name="lab_req_time" select="$lab_req_time"/>
							<xsl:with-param name="value">
								<xsl:if test="/module/header/@mod_required_time != '' and /module/header/@mod_required_time != 0">
									<xsl:value-of select="/module/header/@mod_required_time" />
								</xsl:if>
							</xsl:with-param>
							<xsl:with-param name="lab_req_time_desc" select="$lab_req_time_desc"/>
						</xsl:call-template>
						<xsl:if test="$mod_subtype = 'VOD'">
							<xsl:if test="$cos_type = 'SELFSTUDY' or $cos_type = 'CLASSROOM'">
								<xsl:call-template name="draw_download">
									<xsl:with-param name="lab_download" select="$lab_download"/>
									<xsl:with-param name="lab_prohibited" select="$lab_prohibited"/>
									<xsl:with-param name="lab_allow" select="$lab_allow"/>
									<xsl:with-param name="download_ind" select="/module/header/@mod_download_ind"/>
									<xsl:with-param name="download_tip" select="$download_tip"/>
									<xsl:with-param name="src_type" select="/module/header/@res_src_type"></xsl:with-param>
								</xsl:call-template>
							</xsl:if>
						</xsl:if>
					</xsl:if>
					<xsl:call-template name="table_seperator"/>
				</table>
				<xsl:call-template name="wb_ui_space">
					<xsl:with-param name="width" select="$wb_gen_table_width"/>
				</xsl:call-template>
				<xsl:if test="display/option/event/@datetime = 'true' or display/option/event/@venue = 'true'">
					<xsl:call-template name="wb_ui_head">
						<xsl:with-param name="text" select="$lab_event_info"/>
					</xsl:call-template>
					<xsl:call-template name="wb_ui_line"/>
					<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}" class="Bg">
						<xsl:call-template name="table_seperator"/>
						<!-- Event Datetime -->
						<xsl:call-template name="draw_event_datetime">
							<xsl:with-param name="lab_evt_datetime" select="$lab_evt_datetime"/>
							<xsl:with-param name="value" select="header/event/datetime"/>
						</xsl:call-template>
						<!-- Event Venue -->
						<xsl:call-template name="draw_event_venue">
							<xsl:with-param name="lab_evt_venue" select="$lab_evt_venue"/>
							<xsl:with-param name="value" select="header/event/venue"/>
						</xsl:call-template>
						<xsl:call-template name="table_seperator"/>
					</table>
					<xsl:call-template name="wb_ui_space"/>
				</xsl:if>
				<!-- 
				<xsl:if test="$mod_subtype = 'DXT'">
					<xsl:call-template name="wb_ui_head">
						<xsl:with-param name="text" select="$lab_selection_logic"/>
					</xsl:call-template>
					<xsl:call-template name="wb_ui_line"/>
					<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}" class="Bg">
						<xsl:call-template name="table_seperator"/> -->
						<!-- Selection Logic -->
						<!-- <xsl:call-template name="draw_selection_logic">
							<xsl:with-param name="lab_selection_logic" select="$lab_selection_logic"/>
							<xsl:with-param name="lab_adaptive" select="$lab_adaptive"/>
							<xsl:with-param name="lab_random" select="$lab_random"/>
							<xsl:with-param name="value" select="header/@logic"/>
						</xsl:call-template>
						<xsl:call-template name="table_seperator"/>
					</table>
					<xsl:call-template name="wb_ui_space"/>
				</xsl:if>
				-->
				<xsl:choose>
					<xsl:when test="$mod_subtype = 'ASS'">
						<xsl:call-template name="wb_ui_head">
							<xsl:with-param name="text">
								<xsl:value-of select="$lab_inst"/>
							</xsl:with-param>
						</xsl:call-template>
						<xsl:call-template name="wb_ui_line"/>
						<table>
							<xsl:call-template name="table_seperator"/>
							<xsl:call-template name="draw_ass_inst">
								<xsl:with-param name="lab_ass_inst" select="$lab_ass_inst"/>
								<xsl:with-param name="lab_ass_inst_desc" select="$lab_ass_inst_desc"/>
								<xsl:with-param name="lab_inst" select="$lab_inst"/>
								<xsl:with-param name="mode">upd</xsl:with-param>
								<xsl:with-param name="source_type" select="$source_type"/>
								<xsl:with-param name="inst" select="$inst"/>
							</xsl:call-template>
							<xsl:call-template name="draw_src_url">
								<xsl:with-param name="lab_inst" select="$lab_inst"/>
								<xsl:with-param name="lab_source" select="$lab_source"/>
								<xsl:with-param name="lab_url" select="$lab_url"/>
								<xsl:with-param name="source_type" select="$source_type"/>
								<xsl:with-param name="source_link" select="$source_link"/>
							</xsl:call-template>
							<xsl:call-template name="draw_src_file">
								<xsl:with-param name="lab_upload_file" select="$lab_upload_file"/>
								<xsl:with-param name="lab_uploaded_file" select="$lab_uploaded_file"/>
								<xsl:with-param name="lab_change_to" select="$lab_change_to"/>
								<xsl:with-param name="source_type" select="$source_type"/>
								<xsl:with-param name="source_link" select="$source_link"/>
							</xsl:call-template>
							<xsl:if test="$mod_subtype = 'VOD'">
								<xsl:call-template name="draw_src_video">
									<xsl:with-param name="lab_inst" select="$lab_inst"/>
									<xsl:with-param name="lab_source" select="$lab_source"/>
									<xsl:with-param name="lab_online_video" select="$lab_online_video"/>
									<xsl:with-param name="source_type" select="$source_type"/>
									<xsl:with-param name="source_link" select="$source_link"/>
								</xsl:call-template>
							</xsl:if>
							<xsl:call-template name="draw_src_zipfile">
								<xsl:with-param name="lab_zip" select="$lab_zip"/>
								<xsl:with-param name="lab_default_page" select="$lab_default_page"/>
								<xsl:with-param name="source_type" select="$source_type"/>
								<xsl:with-param name="source_link" select="$source_link"/>
							</xsl:call-template>
							<xsl:call-template name="table_seperator"/>
							<input type="hidden" name="mod_src_link" value=""/>
						</table>
						<xsl:call-template name="wb_ui_space"/>
						<xsl:call-template name="wb_ui_head">
							<xsl:with-param name="text">
								<xsl:value-of select="$lab_grading_policy"/>
							</xsl:with-param>
						</xsl:call-template>
						<xsl:call-template name="wb_ui_line"/>
						<table>
							<xsl:call-template name="table_seperator"/>
							<!-- Grading Scheme-->
							<xsl:call-template name="draw_grading">
								<xsl:with-param name="lab_grading" select="$lab_grading"/>
								<xsl:with-param name="lab_grade" select="$lab_grade"/>
								<xsl:with-param name="max_score" select="header/@max_score"/>
								<xsl:with-param name="pass_score" select="header/@pass_score"/>								
								<xsl:with-param name="lab_max_score" select="$lab_max_score"/>
								<xsl:with-param name="lab_pass_score" select="$lab_passing_score"/>
								
								<xsl:with-param name="mode">upd</xsl:with-param>
							</xsl:call-template>
							<xsl:call-template name="draw_by_score">
								<xsl:with-param name="lab_by_score" select="$lab_by_score"/>
								<xsl:with-param name="lab_max_score" select="$lab_max_score"/>
								<xsl:with-param name="lab_pass_score" select="$lab_passing_score"/>
								<xsl:with-param name="mode">upd</xsl:with-param>
							</xsl:call-template>
							<xsl:call-template name="draw_submission">
								<xsl:with-param name="lab_no_of_submission" select="$lab_no_of_submission"/>
								<xsl:with-param name="lab_not_limited" select="$lab_not_limited"/>
								<xsl:with-param name="lab_max_upload" select="$lab_max_upload"/>
								<xsl:with-param name="lab_files" select="$lab_files"/>
								<xsl:with-param name="mode">upd</xsl:with-param>
							</xsl:call-template>
							<!-- Message to student after submission: -->
							<!-- <xsl:call-template name="draw_submission_msg">
								<xsl:with-param name="lab_submission" select="$lab_submission"/>
								<xsl:with-param name="lab_submission_desc" select="$lab_submission_desc"/>
								<xsl:with-param name="lab_default_submission" select="header/submission"/>
							</xsl:call-template> -->
							<!-- notify_student -->
							<xsl:call-template name="draw_nofity_student">
								<xsl:with-param name="lab_notify_student" select="$lab_notify_student"/>
								<xsl:with-param name="lab_yes" select="$lab_yes"/>
								<xsl:with-param name="lab_no" select="$lab_no"/>
								<xsl:with-param name="value" select="header/@notify"/>
							</xsl:call-template>
							<!-- Due Date-->
							<xsl:call-template name="draw_ass_due_date">
								<xsl:with-param name="lab_due_date" select="$lab_due_date"/>
								<xsl:with-param name="lab_ass_due_date_unspecified" select="$lab_ass_due_date_unspecified"/>
								<xsl:with-param name="lab_due_date_num" select="$lab_due_date_num"/>
								<xsl:with-param name="lab_due_date_num_inst_1" select="$lab_due_date_num_inst_1"/>
								<xsl:with-param name="lab_due_date_num_inst_2" select="$lab_due_date_num_inst_2"/>
								<xsl:with-param name="lab_due_date_non_obligatory" select="$lab_due_date_non_obligatory"/>
								<xsl:with-param name="due_date_day" select="header/@due_date_day"/>
								<xsl:with-param name="due_datetime" select="header/@due_datetime"/>
								<xsl:with-param name="isEnrollment_related" select="$isEnrollment_related"></xsl:with-param>
							</xsl:call-template>
							<xsl:if test="$isEnrollment_related= 'false'">
								<tr>
									<td><img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/></td>
									<td><xsl:value-of select="$lab_clewASS"/></td>
								</tr>
							</xsl:if>
							<xsl:call-template name="table_seperator"/>
						</table>
						<xsl:call-template name="wb_ui_space"/>
					</xsl:when>
					<xsl:when test="$mod_subtype = 'AICC_AU'">
					<input type="hidden" name="src_type" value="AICC_FILES"/>
					<!--
						<xsl:call-template name="wb_ui_head">
							<xsl:with-param name="text">
								<xsl:value-of select="$lab_source"/>
							</xsl:with-param>
						</xsl:call-template>
						<xsl:call-template name="wb_ui_line"/>
						<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}" class="Bg">
							<xsl:call-template name="table_seperator"/>
							<xsl:call-template name="draw_keep_res">
								<xsl:with-param name="lab_previous" select="$lab_previous"/>
								<xsl:with-param name="res_src_link" select="header/@res_src_link"/>
								<xsl:with-param name="lab_source" select="$lab_source"/>
							</xsl:call-template>
							<xsl:if test="$res_enable = 'true'">
							<xsl:call-template name="draw_pick_aicc_res">
								<xsl:with-param name="lab_source" select="$lab_source"/>
								<xsl:with-param name="lab_from_resource" select="$lab_from_resource"/>
								<xsl:with-param name="show_title">false</xsl:with-param>
								<xsl:with-param name="checked">false</xsl:with-param>
							</xsl:call-template>
							</xsl:if>							

							<xsl:call-template name="draw_src_aicc">
								<xsl:with-param name="lab_enter_aicc" select="$lab_enter_aicc"/>
								<xsl:with-param name="lab_file_crs" select="$lab_file_crs"/>
								<xsl:with-param name="lab_file_cst" select="$lab_file_cst"/>
								<xsl:with-param name="lab_file_des" select="$lab_file_des"/>
								<xsl:with-param name="lab_file_ort" select="$lab_file_ort"/>
								<xsl:with-param name="lab_file_au" select="$lab_file_au"/>
								<xsl:with-param name="lab_source" select="$lab_source"/>
								<xsl:with-param name="checked">false</xsl:with-param>
							</xsl:call-template>
							<xsl:call-template name="table_seperator"/>
						</table>
						<xsl:call-template name="wb_ui_space"/>
					-->
					</xsl:when>
					<xsl:when test="$mod_subtype = 'VCR'">
						<xsl:call-template name="wb_ui_head">
							<xsl:with-param name="text">
								<xsl:value-of select="$lab_source"/>
							</xsl:with-param>
						</xsl:call-template>
						<xsl:call-template name="wb_ui_line"/>
						<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}" class="Bg">
							<xsl:call-template name="table_seperator"/>
							<xsl:call-template name="draw_src_vcr_url">
								<xsl:with-param name="lab_url" select="$lab_url"/>
								<xsl:with-param name="show_rdo">false</xsl:with-param>
								<xsl:with-param name="source_link" select="$source_link"/>
							</xsl:call-template>
							<xsl:call-template name="table_seperator"/>
						</table>
						<xsl:call-template name="wb_ui_space"/>
					</xsl:when>
					<xsl:when test="$mod_subtype = 'EAS'">
						<xsl:call-template name="wb_ui_head">
							<xsl:with-param name="text" select="$lab_grading_policy"/>
						</xsl:call-template>
						<xsl:call-template name="wb_ui_line"/>
						<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}" class="Bg wzb-ui-desc-text">
							<xsl:call-template name="table_seperator"/>
							<xsl:call-template name="draw_grading">
								<xsl:with-param name="lab_grading" select="$lab_grading"/>
								<xsl:with-param name="lab_grade" select="$lab_grade"/>
								<xsl:with-param name="max_score" select="header/@max_score"/>
								<xsl:with-param name="pass_score" select="header/@pass_score"/>
								<xsl:with-param name="lab_max_score" select="$lab_max_score"/>
								<xsl:with-param name="lab_pass_score" select="$lab_passing_score"/>
								<xsl:with-param name="mode">eas_upd</xsl:with-param>
							</xsl:call-template>
							<xsl:call-template name="table_seperator"/>
							<xsl:call-template name="wb_ui_space"/>
						</table>
					</xsl:when>
					<xsl:when test="$mod_subtype = 'VOD' or $mod_subtype = 'LCT' or $mod_subtype = 'TUT' or $mod_subtype = 'RDG' or $mod_subtype = 'GAG' or $mod_subtype = 'MBL'">
						<xsl:call-template name="wb_ui_head">
							<xsl:with-param name="text">
								<xsl:value-of select="$lab_source"/>
							</xsl:with-param>
						</xsl:call-template>
						<xsl:call-template name="wb_ui_line"/>
						<table>
							<xsl:call-template name="table_seperator"/>
							<xsl:if test="$mod_subtype = 'VOD'">
								<tr>
									<td>
									</td>
									<td>
										<xsl:copy-of select="$lab_vdo_desc"/>
									</td>
								</tr>
							</xsl:if>
							<xsl:if test=" $mod_subtype = 'RDG'">
								<tr>
									<td>
										<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
									</td>
									<td>
										<div class="wzb-ui-module-text">
											<xsl:copy-of select="$lab_file_desc"/>
										</div>
									</td>
								</tr>
							</xsl:if>
							<!--<xsl:call-template name="draw_keep_res">
								<xsl:with-param name="lab_previous" select="$lab_previous"/>
								<xsl:with-param name="res_src_link" select="header/@res_src_link"/>
								<xsl:with-param name="lab_source" select="$lab_source"/>	
							</xsl:call-template>-->
							<xsl:choose>
							<xsl:when test="$res_enable = 'true' and $mod_subtype != 'VOD'">
							
							</xsl:when>
							<xsl:otherwise>
								<input type="hidden" name="source_type" value=""/>
								<input type="hidden" name="source_content" value=""/>
							</xsl:otherwise>
							</xsl:choose>
							<!-- 网址 -->
							<xsl:if test="$mod_subtype != 'MBL'">					
								<xsl:call-template name="draw_src_url">
									<xsl:with-param name="lab_inst" select="$lab_inst"/>
									<xsl:with-param name="lab_source" select="$lab_source"/>
									<xsl:with-param name="lab_url" select="$lab_url"/>
									<xsl:with-param name="source_type" select="$source_type"/>
									<xsl:with-param name="source_link" select="$source_link"/>
									<xsl:with-param name="lab_change_to" select="$lab_change_to"/>
								</xsl:call-template>
							</xsl:if>
							
							<xsl:call-template name="draw_src_file">  <!-- 已改为上传视频 -->
								<xsl:with-param name="lab_upload_file"><xsl:choose><xsl:when test=" $mod_subtype = 'VOD'"><xsl:value-of select="$lab_upload_video"/></xsl:when><xsl:otherwise><xsl:value-of select="$lab_upload_file"/></xsl:otherwise></xsl:choose>
								</xsl:with-param>
								<xsl:with-param name="lab_uploaded_file"><xsl:choose><xsl:when test=" $mod_subtype = 'VOD'"><xsl:value-of select="$lab_uploaded_video"/></xsl:when><xsl:otherwise><xsl:value-of select="$lab_uploaded_file"/></xsl:otherwise></xsl:choose>
								</xsl:with-param>
								<xsl:with-param name="source_type" select="$source_type"/>
								<xsl:with-param name="source_link" select="$source_link"/>
								<xsl:with-param name="lab_change_to" select="$lab_change_to"/>
								<xsl:with-param name="is_vod">
									<xsl:if test="$mod_subtype = 'VOD'">true</xsl:if>
								</xsl:with-param>
								<xsl:with-param name="remind" select="$lab_vod_remind_new"/>
								<xsl:with-param name="lang" select="$wb_lang"></xsl:with-param>
								<xsl:with-param name="modType" select="$mod_subtype"/>
							</xsl:call-template>
							
							<!-- Online video -->
							<xsl:if test="$mod_subtype = 'VOD'">
								<xsl:call-template name="draw_src_video">
									<xsl:with-param name="lab_inst" select="$lab_inst"/>
									<xsl:with-param name="lab_source" select="$lab_source"/>
									<xsl:with-param name="lab_online_video" select="$lab_online_video"/>
									<xsl:with-param name="source_type" select="$source_type"/>
									<xsl:with-param name="source_link" select="$source_link"/>
									<xsl:with-param name="lab_change_to" select="$lab_change_to"/>				
								</xsl:call-template>
							</xsl:if>
							
							<xsl:if test="$mod_subtype != 'VOD' and $mod_subtype != 'MBL' and $mod_subtype != 'RDG'">
								<xsl:call-template name="draw_src_zipfile">
									<xsl:with-param name="lab_zip" select="$lab_zip"/>
									<xsl:with-param name="lab_default_page" select="$lab_default_page"/>
									<xsl:with-param name="source_type" select="$source_type"/>
									<xsl:with-param name="source_link" select="$source_link"/>
									<xsl:with-param name="lab_change_to" select="$lab_change_to"/>
								</xsl:call-template>
							</xsl:if>
							<input type="hidden" name="mod_src_link" value=""/>
							<xsl:if test="$mod_subtype = 'LCT' or $mod_subtype = 'TUT'">
								<xsl:call-template name="draw_src_wizpack">
									<xsl:with-param name="lab_wizpack" select="$lab_wizpack"/>
									<xsl:with-param name="source_type" select="$source_type"/>
									<xsl:with-param name="source_link" select="$source_link"/>
								</xsl:call-template>
							</xsl:if>
							<xsl:call-template name="table_seperator"/>
						</table>
						<xsl:call-template name="wb_ui_space"/>
					</xsl:when>
				</xsl:choose>
				<!-- -->
				<xsl:if test="display/option/general/@moderator = 'true' or display/option/general/@instructor = 'true' or display/option/general/@organization = 'true' or display/option/general/@language = 'true' or display/option/progress/@difficulty = 'true'">
					<xsl:call-template name="wb_ui_head">
						<xsl:with-param name="text" select="$lab_detail_info"/>
					</xsl:call-template>
					<xsl:call-template name="wb_ui_line"/>
					<table>
						<xsl:call-template name="table_seperator"/>
						<xsl:call-template name="draw_instructor">
							<xsl:with-param name="lab_instructor" select="$lab_instructor"/>
							<xsl:with-param name="lab_instructor_select" select="$lab_instructor_select"/>
							<xsl:with-param name="lab_instructor_enter" select="$lab_instructor_enter"/>
							<xsl:with-param name="instructor_list" select="header/instructor_list"/>
						</xsl:call-template>	
						<xsl:call-template name="draw_moderator">
							<xsl:with-param name="lab_instructor" select="$lab_instructor"/>
							<xsl:with-param name="lab_instructor_select" select="$lab_instructor_select"/>
							<xsl:with-param name="instructor_list" select="header/instructor_list"/>
						</xsl:call-template>	
						<xsl:call-template name="draw_organization">
							<xsl:with-param name="lab_organization" select="$lab_organization"/>
							<xsl:with-param name="value" select="$res_organization"/>
						</xsl:call-template>	
						<xsl:call-template name="draw_language">
							<xsl:with-param name="lab_lang" select="$lab_lang"/>
							<xsl:with-param name="lab_gb_chinese" select="$lab_gb"/>
							<xsl:with-param name="lab_ch_chinese" select="$lab_ch"/>
							<xsl:with-param name="lab_english" select="$lab_eng"/>
							<xsl:with-param name="language" select="$language"/>
						</xsl:call-template>																					
						<xsl:call-template name="draw_difficulty">
							<xsl:with-param name="lab_diff" select="$lab_diff"/>
							<xsl:with-param name="lab_easy" select="$lab_easy"/>
							<xsl:with-param name="lab_normal" select="$lab_normal"/>
							<xsl:with-param name="lab_hard" select="$lab_hard"/>
							<xsl:with-param name="diff" select="$diff"/>
						</xsl:call-template>
						<xsl:if test="display/option/progress/@pass_score != 'true'">
								<xsl:call-template name="draw_time_event">
									<xsl:with-param name="lab_duration"  select="$lab_mod_duration"/>
									<xsl:with-param name="lab_mins" select="$lab_min"/>
									<xsl:with-param name="lab_time_limit" select="$lab_time_limit"/>
									<xsl:with-param name="lab_suggested_time"  select="$lab_suggested_time"/>
									<xsl:with-param name="dur" select="$dur"/>
								</xsl:call-template>
						</xsl:if>
						<xsl:call-template name="table_seperator"/>
					</table>
					<xsl:call-template name="wb_ui_space"/>
				</xsl:if>
				<!-- -->
				<xsl:if test="(display/option/progress/@pass_score = 'true') and ($mod_subtype = 'NETG_COK')">
					<xsl:call-template name="wb_ui_head">
						<xsl:with-param name="text" select="$lab_grading_policy"/>
					</xsl:call-template>
					<xsl:call-template name="wb_ui_line"/>  
					<table>
					<xsl:call-template name="draw_pass_score">   
							<xsl:with-param name="lab_pass_score" select="$lab_passing_score"/>
							<xsl:with-param name="value" select="header/@pass_score"/>
					</xsl:call-template>
					<xsl:call-template name="draw_time_event">
							    <xsl:with-param name="lab_duration" select="$lab_mod_duration"/>
								<xsl:with-param name="lab_mins" select="$lab_min"/>
								<xsl:with-param name="lab_time_limit" select="$lab_time_limit"/>
								<xsl:with-param name="lab_suggested_time" select="$lab_suggested_time"/>
								<xsl:with-param name="dur" select="$dur"/>
					</xsl:call-template>
					</table>
				</xsl:if>
					
				<xsl:if test="(display/option/progress/@pass_score = 'true' or display/option/progress/@max_score= 'true'  or display/option/progress/@max_usr_attempt = 'true') and ($mod_subtype != 'NETG_COK' )">
					<xsl:call-template name="wb_ui_head">
						<xsl:with-param name="text" select="$lab_grading_policy"/>
					</xsl:call-template>
					<xsl:call-template name="wb_ui_line"/>
					<table>
						<xsl:call-template name="table_seperator"/>
							<xsl:call-template name="draw_max_score">
								<xsl:with-param name="value" select="header/@max_score"/>
								<xsl:with-param name="lab_max_score" select="$lab_max_score"/>
								<xsl:with-param name="mod_subtype" select="$mod_subtype"/>
							</xsl:call-template>
						<xsl:if test="display/option/progress/@pass_score= 'true'">
							<xsl:call-template name="draw_pass_score">
								<xsl:with-param name="lab_pass_score"><xsl:choose>
									<xsl:when test="$mod_subtype = 'DXT' or $mod_subtype = 'TST'"><xsl:value-of select="$lab_pass_score"/></xsl:when>
									<xsl:otherwise><xsl:value-of select="$lab_passing_score"/></xsl:otherwise>
								</xsl:choose>
								</xsl:with-param>
								<xsl:with-param name="value" select="header/@pass_score"/>
								<xsl:with-param name="mod_subtype" select="$mod_subtype"/>
								<xsl:with-param name="lab_aicc_sco_desc" select="$lab_aicc_sco_desc"/>
								
								<xsl:with-param name="readonly"><xsl:choose>
									<xsl:when test="$mod_subtype = 'DXT' or $mod_subtype = 'TST'">TRUE</xsl:when>
									<xsl:otherwise>FALSE</xsl:otherwise>
								</xsl:choose></xsl:with-param>
								<xsl:with-param name="required">
									<xsl:choose>
										<xsl:when test="$mod_subtype = 'DXT' or $mod_subtype = 'TST'">true</xsl:when>
										<xsl:otherwise>false</xsl:otherwise>
									</xsl:choose>
								</xsl:with-param>
							</xsl:call-template>
							
						</xsl:if>
						<xsl:if test="$mod_subtype = 'DXT' or $mod_subtype = 'TST'">
							<xsl:call-template name="draw_time_event">
								<xsl:with-param name="lab_duration" select="$lab_mod_duration"/>
								<xsl:with-param name="lab_mins" select="$lab_min"/>
								<xsl:with-param name="lab_time_limit" select="$lab_time_limit"/>
								<xsl:with-param name="lab_time_unlimit" select="$lab_time_unlimit"/>
								<xsl:with-param name="lab_time_limit_tip" select="$lab_time_limit_tip"/>
								<xsl:with-param name="lab_suggested_time" select="$lab_suggested_time"/>
								<xsl:with-param name="dur" select="$dur"/>
							</xsl:call-template>
							<xsl:call-template name="draw_max_usr_attempt">
								<xsl:with-param name="lab_max_attempt_num" select="$lab_max_attempt_num"/>
								<xsl:with-param name="lab_max_attempt_num_unlimited" select="$lab_max_attempt_num_unlimited"/>
								<xsl:with-param name="lab_max_attempt_num_limited" select="$lab_max_attempt_num_limited"/>
								<xsl:with-param name="lab_max_attempt_num_times" select="$lab_max_attempt_num_times"/>
								<xsl:with-param name="max_usr_attempt_num" select="$max_usr_attempt_num"/>
							</xsl:call-template>						
							<!--禁止学员在成绩合格后继续参加测验 -->
							<xsl:call-template name="draw_sub_after_passed_ind">
								<xsl:with-param name="checked" select="header/@sub_after_passed_ind"/>
								<xsl:with-param name="lab_text" select="$lab_sub_after_passed_ind_text"/>
							</xsl:call-template>
							<!--每次参加测验后向学员显示试题和答案 -->
							<xsl:call-template name="draw_show_answer_ind_temp">
								<xsl:with-param name="checked" select="header/@show_answer_ind"/>
								<xsl:with-param name="lab_text" select="$lab_show_answer_ind_text"/>
							</xsl:call-template>
							<xsl:call-template name="draw_show_answer_ind_temp">
								<xsl:with-param name="checked" select="header/@show_answer_ind"/>
								<xsl:with-param name="lab_text" select="$lab_on_show_answer_ind_text"/>
							</xsl:call-template>
							<!--每次参加测验合格后向学员显示试题和答案 -->
							<xsl:call-template name="mod_show_answer_after_passed_ind">
								<xsl:with-param name="checked" select="header/@mod_show_answer_after_passed_ind"/>
								<xsl:with-param name="lab_text" select="$lab_mod_show_answer_after_passed_ind_text"/>
							</xsl:call-template>
							<!--由管理员控制检测开始-->
							<xsl:if test="$mod_subtype ='DXT' or $mod_subtype = 'TST'">
								<xsl:call-template name="draw_show_managed_ind">
									<xsl:with-param name="checked" select="header/@managed_ind"/>
									<xsl:with-param name="lab_text" select="$lab_managed_ind_text"/>
								</xsl:call-template>
							</xsl:if>
							<!--允许学员保存答案并暂停测验 -->
							<xsl:call-template name="draw_show_save_and_suspend_ind">
								<xsl:with-param name="checked" select="header/@show_save_and_suspend_ind"/>
								<xsl:with-param name="lab_text" select="$lab_show_save_and_suspend_ind_text"/>
								<xsl:with-param name="watch_out" select="$lab_only_pc_valid"></xsl:with-param>
							</xsl:call-template>
							<xsl:if test="$mod_subtype ='DXT' or $mod_subtype = 'TST'">
								<!--测试一屏多题  -->	
								<xsl:call-template name="draw_text_style">
									<xsl:with-param name="select" select="header/@test_style"/>
									<xsl:with-param name="lab_title" select="$lab_text_style"/>
									<xsl:with-param name="lab_style1" select="$lab_text_style_only"/>
									<xsl:with-param name="lab_style2" select="$lab_text_style_many"/>
								</xsl:call-template>
							</xsl:if>
						</xsl:if>
					
						<xsl:call-template name="table_seperator"/>
					</table>
					<xsl:call-template name="wb_ui_space"/>
				</xsl:if>
				<!--  -->
				<xsl:if test="display/option/datetime/@eff_end = 'true' or display/option/general/@status = 'true' or display/option/datetime/@eff_start = 'true'">
					<xsl:call-template name="wb_ui_head">
						<xsl:with-param name="text" select="$lab_display_option"/>
					</xsl:call-template>
					<xsl:call-template name="wb_ui_line"/>
					<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}" class="Bg">
						<xsl:call-template name="table_seperator"/>
						<xsl:call-template name="draw_status">
							<xsl:with-param name="lab_status" select="$lab_status"/>
							<xsl:with-param name="lab_online" select="$lab_online"/>
							<xsl:with-param name="lab_offline" select="$lab_offline"/>
							<xsl:with-param name="status" select="$status"/>
							<xsl:with-param name="isEnrollment_related" select="$isEnrollment_related"></xsl:with-param>
						</xsl:call-template>
						
						<xsl:choose>
							<xsl:when test="$cos_type != 'AUDIOVIDEO'">
								<xsl:choose>
									<xsl:when test="display/option/datetime/@eff_start = 'true'">
										<xsl:variable name="current_time" select="translate(substring-before(header/@cur_time, '.'), ':- ', '')"/>
										<xsl:variable name="start_time" select="translate(substring-before(header/@eff_start_datetime, '.'), ':- ', '')"/>
										<input type="hidden" name="cur_dt" value="{$current_time}"/>
										<input type="hidden" name="eff_dt" value="{$start_time}"/>
										<xsl:choose>
											<xsl:when test="( header/@subtype = 'CHT' or header/@subtype = 'VCR' ) and ( $current_time > $start_time)">
												<xsl:call-template name="draw_effective_start">
													<xsl:with-param name="lab_start_date" select="$lab_start_date"/>
													<xsl:with-param name="lab_immediate" select="$lab_immediate"/>
													<xsl:with-param name="timestamp" select="header/@eff_start_datetime"/>
													<xsl:with-param name="readonly">true</xsl:with-param>
													<xsl:with-param name="isEnrollment_related" select="$isEnrollment_related"></xsl:with-param>
												</xsl:call-template>
											</xsl:when>
											<xsl:otherwise>
												<xsl:call-template name="draw_effective_start">
													<xsl:with-param name="lab_start_date" select="$lab_start_date"/>
													<xsl:with-param name="lab_immediate" select="$lab_immediate"/>
													<xsl:with-param name="timestamp" select="header/@eff_start_datetime"/>
													<xsl:with-param name="readonly">false</xsl:with-param>
													<xsl:with-param name="isEnrollment_related" select="$isEnrollment_related"></xsl:with-param>
												</xsl:call-template>									
											</xsl:otherwise>
										</xsl:choose>
									</xsl:when>
									<xsl:otherwise>
										<input type="hidden" name="mod_eff_start_datetime" value="{header/@eff_start_datetime}"/>
									</xsl:otherwise>
								</xsl:choose>
								<xsl:choose>
									<xsl:when test="display/option/datetime/@eff_end = 'true'">
										<xsl:call-template name="draw_effective_end">
											<xsl:with-param name="lab_end_date" select="$lab_end_date"/>
											<xsl:with-param name="lab_unlimit" select="$lab_unlimit"/>
											<xsl:with-param name="timestamp" select="header/@eff_end_datetime"/>
											<xsl:with-param name="isEnrollment_related" select="$isEnrollment_related"></xsl:with-param>
										</xsl:call-template>
									</xsl:when>
									<xsl:otherwise>
										<input type="hidden" name="mod_eff_end_datetime" value="{header/@eff_end_datetime}"/>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:when>
							<xsl:otherwise>
								<input type="hidden" name="mod_eff_start_datetime" value="IMMEDIATE"/>
								<input type="hidden" name="mod_eff_end_datetime" value="UNLIMITED"/>
							</xsl:otherwise>
						</xsl:choose>	
						<xsl:if test="$isEnrollment_related= 'false'">
						<tr>
						    <td><img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/></td>
							<td><xsl:value-of select="$lab_clew"/></td>
						</tr>
						</xsl:if>
						<xsl:call-template name="table_seperator"/>
					</table>
				</xsl:if>
				<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}" class="Bg">
					<tr>
						<td width="20%" align="right" height="10">
							<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
						</td>
						<td width="80%" height="10">
							<span class="wzb-ui-desc-text">
								<span class="wzb-form-star">*</span>
								<xsl:value-of select="$lab_info_required"/>
							</span>
						</td>
					</tr>
				</table>
				<xsl:if test="header/template_list/template/type/text() !='TST' and header/template_list/template/type/text() !='DXT' and header/template_list/template/type/text() !='RDG'">
					<!--  -->
					<xsl:if test="count(header/template_list/template) > 1">
						<xsl:call-template name="wb_ui_space"/>
						<xsl:call-template name="wb_ui_head">
							<xsl:with-param name="text" select="$lab_template"/>
						</xsl:call-template>
						<xsl:call-template name="wb_ui_line"/>
						<table>
							<xsl:call-template name="table_seperator"/>
							<xsl:call-template name="draw_template">
								<xsl:with-param name="lab_template" select="$lab_template"/>
								<xsl:with-param name="template_list" select="header/template_list"/>
							</xsl:call-template>		
							<xsl:call-template name="table_seperator"/>
						</table>
					</xsl:if>
				</xsl:if>	
							<xsl:if test="$mod_subtype = 'LCT' or $mod_subtype = 'TUT'">
							<table>
								<xsl:call-template name="draw_annotation">
									<xsl:with-param name="lab_annotation" select="$lab_ann"/>
									<xsl:with-param name="lab_ashtml" select="$lab_ashtml"/>
									<xsl:with-param name="value">
										<xsl:choose>
											<xsl:when test="/module/header/annotation/html"><xsl:copy-of select="/module/header/annotation/html/text()"/></xsl:when>
											<xsl:otherwise><xsl:copy-of select="/module/header/annotation/text()"/></xsl:otherwise>
										</xsl:choose>									
									</xsl:with-param>
									<xsl:with-param name="ashtml">
										<xsl:choose>
											<xsl:when test="/module/header/annotation/html">true</xsl:when>
											<xsl:otherwise>false</xsl:otherwise>
										</xsl:choose>
									</xsl:with-param>
								</xsl:call-template>
								</table>
							</xsl:if>				
				<xsl:if test="count(header/template_list/template) = 1">
					<input type="hidden" name="tpl_name" value="{header/template_list/@cur_tpl}"/>
				</xsl:if>
			<div class="wzb-bar">
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_ok"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:checkModAndSave();</xsl:with-param>
						<xsl:with-param name="id">submitButton</xsl:with-param>
					</xsl:call-template>
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:window.parent.cancelEdit();window.location.href=mod.view_info_url(<xsl:value-of select="$mod_id"/>)</xsl:with-param>
					</xsl:call-template>
				</div>
				<script language="JavaScript">
			<![CDATA[
			with (document) {	
					write("<input type=\"hidden\" name=\"url_success\" value=\"" + gen_get_cookie('url_success') +"\">")	
					write('<input type="hidden" name="url_failure" value="' +gen_get_cookie('url_failure')+'"/>')				
			}
		//-->]]></script>
				<input type="hidden" name="max_usr_attempt"/>
				<input type="hidden" name="cmd" value="upd_mod"/>
				<input type="hidden" name="mod_id" value="{$mod_id}"/>
				<input type="hidden" name="mod_type" value="{$mod_type}"/>
				<input type="hidden" name="mod_subtype" value="{$mod_subtype}"/>
				<input type="hidden" name="mod_privilege" value="{mod_privilege}"/>
				<input type="hidden" name="mod_timestamp" value="{$timestamp}"/>
				<input type="hidden" name="annotation_html" value=""/>
				<input type="hidden" name="copy_media_from" value=""/>
				<xsl:if test="($mod_subtype != 'VCR' )and ($mod_subtype!='AICC_AU')">
					<input type="hidden" name="mod_src_type" value="{header/@res_src_type}"/>
				</xsl:if>
				<input type="hidden" name="course_timestamp" value=""/>
				<input type="hidden" name="course_struct_xml_cnt" value=""/>
				<input type="hidden" name="course_struct_xml_1" value=""/>
				<xsl:if test="($mod_subtype = 'NETG_COK') or ($mod_subtype = 'SCO')">
					<input name="mod_src_link" type="hidden" value="{header/@res_src_link}"/>
				</xsl:if>
				<input type="hidden" name="mod_src_online_link" value="" />
			</form>
		</body>
	</xsl:template>
	
	<xsl:template match="annotation">
		<xsl:copy-of select="text()"/>
	</xsl:template>
	<!-- ======================================================================= -->
</xsl:stylesheet>
