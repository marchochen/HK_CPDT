<?xml version="1.0" encoding="UTF-8"?>
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
	<xsl:import href="utils/display_form_input_time.xsl"/>
	<xsl:import href="share/wb_module_type_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:variable name="source_id"><xsl:value-of select="/template_list/dynamic_que_container/@id"/></xsl:variable>
	<xsl:variable name="assessment" select="/template_list/dynamic_que_container"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:variable name="my_privilege">
		<xsl:choose>
			<xsl:when test="//cur_usr/@root_display = $root_cw">CW</xsl:when>
			<xsl:otherwise>AUTHOR</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="wb_gen_table_width" select="$wb_frame_table_width"/>
	<xsl:variable name="mod_subtype" select="//template_list/template/type"/>
	<xsl:variable name="cos_type" select="/template_list/cos_type"/>
	<xsl:variable name="tpl_type" select="/template_list/type"/>
	<xsl:variable name="tpl_subtype" select="/template_list/subtype"/>
	<xsl:variable name="current_time" select="translate(substring-before(//cur_time/., '.'), ':- ', '')"/>
	<xsl:variable name="res_enable">true</xsl:variable>
	<xsl:variable name="isEnrollment_related">
		<xsl:choose>
			<xsl:when test="not (/template_list/enrollment_related)">all</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="/template_list/enrollment_related"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<!-- =============================================================== -->
	<xsl:template match="/template_list">
		<html>
			<xsl:call-template name="wb_init_lab"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_add_module">新增單元</xsl:with-param>
			<xsl:with-param name="lab_type">類型</xsl:with-param>
			<xsl:with-param name="lab_title">標題</xsl:with-param>
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
			<xsl:with-param name="lab_duration">所需時間</xsl:with-param>
			<xsl:with-param name="lab_selection_logic">選擇邏輯</xsl:with-param>
			<xsl:with-param name="lab_template">樣板</xsl:with-param>
			<xsl:with-param name="lab_url">網址(请输入完整的URL，格式为：http://host:port/.../xxx.mp4; 请不要使用相对路径。)</xsl:with-param>
			<xsl:with-param name="lab_online_video">網上影片</xsl:with-param>
			<xsl:with-param name="lab_field_value">標題</xsl:with-param>
			<xsl:with-param name="lab_annotation">註釋</xsl:with-param>
			<xsl:with-param name="lab_from_resource">選取自資源庫</xsl:with-param>
			<xsl:with-param name="lab_upload_file">上傳檔案</xsl:with-param>
			<xsl:with-param name="lab_upload_video">上傳視頻</xsl:with-param>
			<xsl:with-param name="lab_source">來源</xsl:with-param>
			<xsl:with-param name="lab_ashtml">將內容以HTML處理</xsl:with-param>
			<xsl:with-param name="lab_mins">分鐘</xsl:with-param>
			<xsl:with-param name="lab_english">英文</xsl:with-param>
			<xsl:with-param name="lab_gb_chinese">簡體中文</xsl:with-param>
			<xsl:with-param name="lab_ch_chinese">繁體中文</xsl:with-param>
			<xsl:with-param name="lab_random">隨機性</xsl:with-param>
			<xsl:with-param name="lab_adaptive">適應性</xsl:with-param>
			<xsl:with-param name="lab_max_score">最高分數</xsl:with-param>
			<xsl:with-param name="lab_max_upload">最多提交</xsl:with-param>
			<xsl:with-param name="lab_submission">提交作業結後語</xsl:with-param>
			<xsl:with-param name="lab_submission_desc">(請使用不超過2000個位的文字)</xsl:with-param>
			<xsl:with-param name="lab_due_date_format">年-月-日</xsl:with-param>
			<xsl:with-param name="lab_due_date_time_format">小時:分鐘</xsl:with-param>
			<xsl:with-param name="lab_notify_student">提交作業電郵確認</xsl:with-param>
			<xsl:with-param name="lab_file">檔案標題</xsl:with-param>
			<xsl:with-param name="lab_due_date">截止日期</xsl:with-param>
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
			<xsl:with-param name="lab_hard">困難</xsl:with-param>
			<xsl:with-param name="lab_normal">一般</xsl:with-param>
			<xsl:with-param name="lab_easy">容易</xsl:with-param>
			<xsl:with-param name="lab_time_limit">時限</xsl:with-param>
			<xsl:with-param name="lab_suggested_time">建議時間</xsl:with-param>
			<xsl:with-param name="lab_evt_venue">地點</xsl:with-param>
			<xsl:with-param name="lab_evt_datetime">時間</xsl:with-param>
			<xsl:with-param name="lab_status">狀態</xsl:with-param>
			<xsl:with-param name="lab_online">已發佈</xsl:with-param>
			<xsl:with-param name="lab_offline">未發佈</xsl:with-param>
			<xsl:with-param name="lab_default_submission">你的作業將會被評分</xsl:with-param>
			<xsl:with-param name="lab_wizpack">上傳 WizPack 檔案</xsl:with-param>
			<xsl:with-param name="lab_zip">上傳 Zip 檔案</xsl:with-param>
			<xsl:with-param name="lab_default_page">Zip檔案中首頁檔案</xsl:with-param>
			<xsl:with-param name="lab_basic_info">基本資料</xsl:with-param>
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
			<xsl:with-param name="lab_file_cdf">CDF 檔案</xsl:with-param>
			<xsl:with-param name="lab_enter_aicc">上傳 AICC 檔案</xsl:with-param>
			<xsl:with-param name="lab_enter_aicc_zip">上傳 AICC 檔案壓縮包</xsl:with-param>
			<xsl:with-param name="lab_include_rating_que">包括評估問題</xsl:with-param>
			<xsl:with-param name="lab_yes">是</xsl:with-param>
			<xsl:with-param name="lab_no">否</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">儲存</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_A">甲</xsl:with-param>
			<xsl:with-param name="lab_B">乙</xsl:with-param>
			<xsl:with-param name="lab_C">丙</xsl:with-param>
			<xsl:with-param name="lab_D">丁</xsl:with-param>
			<xsl:with-param name="lab_F">不合格</xsl:with-param>
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
			<xsl:with-param name="lab_vod_point">知识要点</xsl:with-param>
			<xsl:with-param name="lab_vdo_desc">請注意: 目前只支持編碼格式為 H264 (H264/AVC) 的<b>MP4</b>視頻文件。</xsl:with-param>
			<xsl:with-param name="lab_file_desc">系統在處理檔案及其內容後有可能會出現內容失真或移位的情況。當你完成上載後，應該先預覽以確保處理完成後的內容能達到所需的效果。如果內容過於複雜(如Powerpoint)，建議於上載前自行轉換為 PDF格式。</xsl:with-param>
			<xsl:with-param name="lab_push_to_mobile">發佈到移動端</xsl:with-param>
			<xsl:with-param name="lab_only_pc_valid">以下設置只對PC端有效：</xsl:with-param>
			<xsl:with-param name="lab_req_time_desc">學員完成該模塊所需要的學習時長。例如，這裡設置10分鐘，那麼學員在該模塊的學習時長累積要大於或等於10分鐘，才能完成該模塊。如果不指定，或指定為0，學員只要一打開該模塊就算完成了。</xsl:with-param>
		    <xsl:with-param name="lab_vod_remind">為保障移動端視頻流暢播放，以下為視頻大小、時長的建議值：<br/>視頻每分鐘大小建議<span style="color:#F00"><b>3-5M</b></span>內<br/>每個視頻播放時長建議<span style="color:#F00"><b>5-10分鐘</b></span>內</xsl:with-param>
			<xsl:with-param name="lab_vod_remind_new">為保障視頻流暢播放，請在保證基本清晰的情況下選擇大小盡可能小的視頻。</xsl:with-param>
			<xsl:with-param name="lab_time_unlimit">不限時</xsl:with-param>
			<xsl:with-param name="lab_time_limit_tip">"-1" 代表不限時</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_add_module">添加模块</xsl:with-param>
			<xsl:with-param name="lab_type">类型</xsl:with-param>
			<xsl:with-param name="lab_title">标题</xsl:with-param>
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
			<xsl:with-param name="lab_duration">所需时间</xsl:with-param>
			<xsl:with-param name="lab_selection_logic">选择逻辑</xsl:with-param>
			<xsl:with-param name="lab_template">模板</xsl:with-param>
			<xsl:with-param name="lab_url">网址(请输入完整的URL，格式为：http://host:port/.../xxx.mp4; 请不要使用相对路径。)</xsl:with-param>
			<xsl:with-param name="lab_online_video">在线视频</xsl:with-param>
			<xsl:with-param name="lab_field_value">标题</xsl:with-param>
			<xsl:with-param name="lab_annotation">注释</xsl:with-param>
			<xsl:with-param name="lab_from_resource">选取自资源库</xsl:with-param>
			<xsl:with-param name="lab_upload_file">上传文档</xsl:with-param>
			<xsl:with-param name="lab_upload_video">上传视频</xsl:with-param>
			<xsl:with-param name="lab_source">来源</xsl:with-param>
			<xsl:with-param name="lab_ashtml">将内容以HTML处理</xsl:with-param>
			<xsl:with-param name="lab_mins">分钟</xsl:with-param>
			<xsl:with-param name="lab_english">英文</xsl:with-param>
			<xsl:with-param name="lab_gb_chinese">简体中文</xsl:with-param>
			<xsl:with-param name="lab_ch_chinese">繁体中文</xsl:with-param>
			<xsl:with-param name="lab_random">随机性</xsl:with-param>
			<xsl:with-param name="lab_adaptive">适应性</xsl:with-param>
			<xsl:with-param name="lab_max_score">满分</xsl:with-param>
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
			<xsl:with-param name="lab_no_of_submission">文档作业提交数量</xsl:with-param>
			<xsl:with-param name="lab_instructor">讲者</xsl:with-param>
			<xsl:with-param name="lab_instructor_select">-- 请选择 --</xsl:with-param>
			<xsl:with-param name="lab_instructor_enter">输入名称</xsl:with-param>
			<xsl:with-param name="lab_moderator">主持人</xsl:with-param>
			<xsl:with-param name="lab_organization">机构</xsl:with-param>
			<xsl:with-param name="lab_hard">困难</xsl:with-param>
			<xsl:with-param name="lab_normal">一般</xsl:with-param>
			<xsl:with-param name="lab_easy">容易</xsl:with-param>
			<xsl:with-param name="lab_time_limit">时限</xsl:with-param>
			<xsl:with-param name="lab_suggested_time">建议时间</xsl:with-param>
			<xsl:with-param name="lab_evt_venue">地点</xsl:with-param>
			<xsl:with-param name="lab_status">状态</xsl:with-param>
			<xsl:with-param name="lab_online">已发布</xsl:with-param>
			<xsl:with-param name="lab_offline">未发布</xsl:with-param>
			<xsl:with-param name="lab_default_submission">即将批阅您的作业</xsl:with-param>
			<xsl:with-param name="lab_evt_datetime">时间</xsl:with-param>
			<xsl:with-param name="lab_wizpack">上传 WizPack 文档</xsl:with-param>
			<xsl:with-param name="lab_zip">上传 Zip 文档</xsl:with-param>
			<xsl:with-param name="lab_default_page">Zip文档中首页文档</xsl:with-param>
			<xsl:with-param name="lab_basic_info">基本资料</xsl:with-param>
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
			<xsl:with-param name="lab_file_cdf">CDF File</xsl:with-param>
			<xsl:with-param name="lab_enter_aicc">上传 AICC 文档</xsl:with-param>
			<xsl:with-param name="lab_enter_aicc_zip">上传 AICC 压缩包</xsl:with-param>
			<xsl:with-param name="lab_include_rating_que">评分</xsl:with-param>
			<xsl:with-param name="lab_yes">是</xsl:with-param>
			<xsl:with-param name="lab_no">否</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">保存</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_A">甲</xsl:with-param>
			<xsl:with-param name="lab_B">乙</xsl:with-param>
			<xsl:with-param name="lab_C">丙</xsl:with-param>
			<xsl:with-param name="lab_D">丁</xsl:with-param>
			<xsl:with-param name="lab_F">不合格</xsl:with-param>
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
			<xsl:with-param name="lab_req_time_desc">学员完成该模块所需要的学习时长。例如，这里设置10分钟，那么学员在该模块的学习时长累积要大于或等于10分钟，才能完成该模块。</xsl:with-param>
		    <xsl:with-param name="lab_vod_remind">为保障移动端视频流畅播放，以下为视频大小、时长的建议值：<br/>视频每分钟大小建议<span style="color:#F00"><b>3-5M</b></span>内 <br/>每个视频播放时长建议<span style="color:#F00"><b>5-10分钟</b></span>内</xsl:with-param>
			<xsl:with-param name="lab_vod_remind_new">为保障视频流畅播放，请在保证基本清晰的情况下选择大小尽可能小的视频。</xsl:with-param>
			<xsl:with-param name="lab_time_unlimit">不限时</xsl:with-param>
			<xsl:with-param name="lab_time_limit_tip">"-1" 代表不限时</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_add_module">Add module</xsl:with-param>
			<xsl:with-param name="lab_type">Type</xsl:with-param>
			<xsl:with-param name="lab_title">Title</xsl:with-param>
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
			<xsl:with-param name="lab_duration">Duration</xsl:with-param>
			<xsl:with-param name="lab_selection_logic">Question selection logic</xsl:with-param>
			<xsl:with-param name="lab_template">Template</xsl:with-param>
			<xsl:with-param name="lab_url">URL (Please enter complete URL, e.g. http://host:port/xxx/xxx.mp4, and do NOT use relative path)</xsl:with-param>
			<xsl:with-param name="lab_online_video">Online video</xsl:with-param>
			<xsl:with-param name="lab_field_value">Title</xsl:with-param>
			<xsl:with-param name="lab_annotation">Annotation</xsl:with-param>
			<xsl:with-param name="lab_from_resource">From learning resource management</xsl:with-param>
			<xsl:with-param name="lab_upload_file">Upload file</xsl:with-param>
			<xsl:with-param name="lab_upload_video">Upload video</xsl:with-param>
			<xsl:with-param name="lab_source">Source</xsl:with-param>
			<xsl:with-param name="lab_ashtml">Treat content as HTML</xsl:with-param>
			<xsl:with-param name="lab_mins"> min(s)</xsl:with-param>
			<xsl:with-param name="lab_english">English</xsl:with-param>
			<xsl:with-param name="lab_gb_chinese">Simplified Chinese</xsl:with-param>
			<xsl:with-param name="lab_ch_chinese">Traditional Chinese</xsl:with-param>
			<xsl:with-param name="lab_random">Random</xsl:with-param>
			<xsl:with-param name="lab_adaptive">Adaptive</xsl:with-param>
			<xsl:with-param name="lab_max_score">Max. score</xsl:with-param>
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
			<xsl:with-param name="lab_not_limited">Unlimited</xsl:with-param>
			<xsl:with-param name="lab_files">files</xsl:with-param>
			<xsl:with-param name="lab_no_of_submission">No. of files to be submitted</xsl:with-param>
			<xsl:with-param name="lab_instructor">Lecturer</xsl:with-param>
			<xsl:with-param name="lab_instructor_select">-- Please select --</xsl:with-param>
			<xsl:with-param name="lab_instructor_enter">Enter name</xsl:with-param>
			<xsl:with-param name="lab_moderator">Moderator</xsl:with-param>
			<xsl:with-param name="lab_organization">Organization</xsl:with-param>
			<xsl:with-param name="lab_hard">Hard</xsl:with-param>
			<xsl:with-param name="lab_normal">Normal</xsl:with-param>
			<xsl:with-param name="lab_easy">Easy</xsl:with-param>
			<xsl:with-param name="lab_time_limit">Time limit</xsl:with-param>
			<xsl:with-param name="lab_suggested_time">Suggested time</xsl:with-param>
			<xsl:with-param name="lab_evt_datetime">Event date</xsl:with-param>
			<xsl:with-param name="lab_evt_venue">Venue</xsl:with-param>
			<xsl:with-param name="lab_status">Status</xsl:with-param>
			<xsl:with-param name="lab_online">Published</xsl:with-param>
			<xsl:with-param name="lab_offline">Unpublished</xsl:with-param>
			<xsl:with-param name="lab_default_submission">Your assignment is going to be graded.</xsl:with-param>
			<xsl:with-param name="lab_wizpack">Upload wizPack</xsl:with-param>
			<xsl:with-param name="lab_zip">Upload zip file</xsl:with-param>
			<xsl:with-param name="lab_default_page">Index file among the zipped files</xsl:with-param>
			<xsl:with-param name="lab_basic_info">Basic information</xsl:with-param>
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
			<xsl:with-param name="lab_file_cdf">CDF file</xsl:with-param>
			<xsl:with-param name="lab_enter_aicc">Upload AICC files</xsl:with-param>
			<xsl:with-param name="lab_enter_aicc_zip">Upload AICC zipped files</xsl:with-param>
			<xsl:with-param name="lab_include_rating_que">Include rating question</xsl:with-param>
			<xsl:with-param name="lab_yes">Yes</xsl:with-param>
			<xsl:with-param name="lab_no">No</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_A">A</xsl:with-param>
			<xsl:with-param name="lab_B">B</xsl:with-param>
			<xsl:with-param name="lab_C">C</xsl:with-param>
			<xsl:with-param name="lab_D">D</xsl:with-param>
			<xsl:with-param name="lab_F">Fail</xsl:with-param>
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
			<xsl:with-param name="lab_text_style_many">Show a list of questions on a screen </xsl:with-param>
			<xsl:with-param name="lab_mod_show_answer_after_passed_ind_text">Display questions and answers to students after test passed</xsl:with-param>
			<xsl:with-param name="lab_on_show_answer_ind_text">Do not display questions and answers to students after test</xsl:with-param>
			<xsl:with-param name="lab_duration_2">Duration</xsl:with-param>
			<xsl:with-param name="lab_vod_img_link">Icon</xsl:with-param>
			<xsl:with-param name="lab_vod_point">Knowledge points</xsl:with-param>
			<xsl:with-param name="lab_vdo_desc">Please note that currently system only supports video file in  <b>MP4</b> format with encoding in H264 (H264/AVC).</xsl:with-param>
			<xsl:with-param name="lab_file_desc">There may be some distortion after various processing of the file and content in the system. You should preview whether the outcome can fulfill the expectation whenever you have uploaded a new file. If the file contains some complicated layout (e.g. Powerpoint), it is recommended to convert to PDF before upload to system.</xsl:with-param>
			<xsl:with-param name="lab_push_to_mobile">Publish to mobile</xsl:with-param>
			<xsl:with-param name="lab_only_pc_valid">The following settings are valid only for the PC:</xsl:with-param>
			<xsl:with-param name="lab_req_time_desc">Learner is supposed to take some time to complete the module. For example, if the pre-set time is 10 minutes, learner is required to spend at least 10 minutes in the module to get it completed. If there is no pre-set time, or it is set to be 0, the module will be marked as completed once the learner clicked in the module.</xsl:with-param>
			<xsl:with-param name="lab_vod_remind">In order to protect the mobile video smooth playback, the following for the video size of the long recommended value：<br/>Video size recommended per minute within <span style="color:#F00"><b>3-5M</b></span><br/>Each video playback time is recommended within <span style="color:#F00"><b>5-10 minutes</b></span></xsl:with-param>
			<xsl:with-param name="lab_vod_remind_new">To ensure smooth video playback, please select a size under the condition of the guarantee basic clear video as small as possible.</xsl:with-param>
			<xsl:with-param name="lab_time_unlimit">Unlimited</xsl:with-param>
			<xsl:with-param name="lab_time_limit_tip">Enter "-1" for unlimited time</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_add_module"/>
		<xsl:param name="lab_type"/>
		<xsl:param name="lab_title"/>
		<xsl:param name="lab_lang"/>
		<xsl:param name="lab_desc"/>
		<xsl:param name="lab_req_time"/>
		<xsl:param name="lab_download"/>
		<xsl:param name="lab_prohibited"/>
		<xsl:param name="lab_allow"/>
		<xsl:param name="download_tip"/>
		<xsl:param name="lab_inst"/>
		<xsl:param name="lab_diff"/>
		<xsl:param name="lab_pass_score"/>
		<xsl:param name="lab_passing_score"/>
		<xsl:param name="lab_duration"/>
		<xsl:param name="lab_selection_logic"/>
		<xsl:param name="lab_template"/>
		<xsl:param name="lab_url"/>
		<xsl:param name="lab_online_video"/>
		<xsl:param name="lab_field_value"/>
		<xsl:param name="lab_annotation"/>
		<xsl:param name="lab_from_resource"/>
		<xsl:param name="lab_upload_file"/>
		<xsl:param name="lab_upload_video"/>
		<xsl:param name="lab_source"/>
		<xsl:param name="lab_ashtml"/>
		<xsl:param name="lab_mins"/>
		<xsl:param name="lab_english"/>
		<xsl:param name="lab_gb_chinese"/>
		<xsl:param name="lab_ch_chinese"/>
		<xsl:param name="lab_random"/>
		<xsl:param name="lab_adaptive"/>
		<xsl:param name="lab_max_score"/>
		<xsl:param name="lab_max_upload"/>
		<xsl:param name="lab_submission"/>
		<xsl:param name="lab_submission_desc"/>
		<xsl:param name="lab_due_date_format"/>
		<xsl:param name="lab_due_date_time_format"/>
		<xsl:param name="lab_notify_student"/>
		<xsl:param name="lab_yes"/>
		<xsl:param name="lab_no"/>
		<xsl:param name="lab_file"/>
		<xsl:param name="lab_due_date"/>
		<xsl:param name="lab_due_date_num"/>
		<xsl:param name="lab_due_date_num_inst_1"/>
		<xsl:param name="lab_due_date_num_inst_2"/>
		<xsl:param name="lab_due_date_non_obligatory"/>
		<xsl:param name="lab_ass_inst"/>
		<xsl:param name="lab_ass_inst_desc"/>
		<xsl:param name="lab_unlimit"/>
		<xsl:param name="lab_immediate"/>
		<xsl:param name="lab_start_date"/>
		<xsl:param name="lab_end_date"/>
		<xsl:param name="lab_grading"/>
		<xsl:param name="lab_grade"/>
		<xsl:param name="lab_by_score"/>
		<xsl:param name="lab_not_limited"/>
		<xsl:param name="lab_files"/>
		<xsl:param name="lab_no_of_submission"/>
		<xsl:param name="lab_instructor"/>
		<xsl:param name="lab_instructor_select"/>
		<xsl:param name="lab_instructor_enter"/>
		<xsl:param name="lab_moderator"/>
		<xsl:param name="lab_organization"/>
		<xsl:param name="lab_hard"/>
		<xsl:param name="lab_normal"/>
		<xsl:param name="lab_easy"/>
		<xsl:param name="lab_time_limit"/>
		<xsl:param name="lab_suggested_time"/>
		<xsl:param name="lab_evt_datetime"/>
		<xsl:param name="lab_evt_venue"/>
		<xsl:param name="lab_status"/>
		<xsl:param name="lab_online"/>
		<xsl:param name="lab_offline"/>
		<xsl:param name="lab_default_submission"/>
		<xsl:param name="lab_wizpack"/>
		<xsl:param name="lab_zip"/>
		<xsl:param name="lab_default_page"/>
		<xsl:param name="lab_basic_info"/>
		<xsl:param name="lab_grading_policy"/>
		<xsl:param name="lab_display_option"/>
		<xsl:param name="lab_submit_detail"/>
		<xsl:param name="lab_detail_info"/>
		<xsl:param name="lab_event_info"/>
		<xsl:param name="lab_file_crs"/>
		<xsl:param name="lab_file_cst"/>
		<xsl:param name="lab_file_des"/>
		<xsl:param name="lab_file_au"/>
		<xsl:param name="lab_file_ort"/>
		<xsl:param name="lab_file_cdf"/>
		<xsl:param name="lab_enter_aicc"/>
		<xsl:param name="lab_enter_aicc_zip"/>
		<xsl:param name="lab_include_rating_que"/>
		<xsl:param name="lab_g_form_btn_ok"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:param name="lab_A"/>
		<xsl:param name="lab_B"/>
		<xsl:param name="lab_C"/>
		<xsl:param name="lab_D"/>
		<xsl:param name="lab_F"/>
		<xsl:param name="lab_max_attempt_num"/>
		<xsl:param name="lab_max_attempt_num_unlimited"/>
		<xsl:param name="lab_max_attempt_num_limited"/>
		<xsl:param name="lab_max_attempt_num_times"/>
		<xsl:param name="lab_show_answer_ind_text"/>
		<xsl:param name="lab_show_save_and_suspend_ind_text"/>
		<xsl:param name="lab_sub_after_passed_ind_text"/>
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
		
		<xsl:param name="lab_req_time_desc" />
        <xsl:param name="lab_vod_remind" />
		<xsl:param name="lab_vod_remind_new" />
		<xsl:param name="lab_time_unlimit"/>
		<xsl:param name="lab_time_limit_tip"/>
		
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<xsl:call-template name="new_css"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}jquery.js"/>
			
			<script language="JavaScript" src="{$wb_js_path}gen_utils.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_course.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_utils.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_resource.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_media.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_module.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_aicc.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}urlparam.js" type="text/javascript"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_upload_util.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script type="text/javascript" src="../../static/js/i18n/{$wb_cur_lang}/global_{$wb_cur_lang}.js" language="JavaScript"/>
			<script type="text/javascript" src="../../static/js/i18n/{$wb_cur_lang}/label_tm_{$wb_cur_lang}.js" language="JavaScript"/>
			<script language="Javascript" type="text/javascript" src="../../static/js/cwn_utils.js"/>
			<script language="Javascript" type="text/javascript" src="../../static/js/layer/layer.js"/>
			<!--alert样式  -->
			<script type="text/javascript" src="../static/js/layer/skin/layer.css"></script>
			 <link rel="stylesheet" href="../static/js/jquery.qtip/jquery.qtip.css" />
			<script type="text/javascript" src="../static/js/jquery.dialogue.js"></script>
			<script type="text/javascript" src="../static/js/jquery.qtip/jquery.qtip.js"></script>
			
			<!-- layer -->
			<script type="text/javascript" src="../static/js/layer/layer.js"></script>
			<link rel="stylesheet" href="../static/js/layer/skin/layer.css"/>
			<script language="JavaScript" type="text/javascript"><![CDATA[
			var course_lst = new wbCourse;
			mod = new wbModule;
			var res = new wbResource;
			wb_utils_set_cookie('url_add_mod',window.location.href)
			var cookie_course_id = getUrlParam('course_id')
			
			//--==Man: For Dynamic Source Type radio button , don't remove
			var src_type_wizpack_id 
			var src_type_file_id 
			var src_type_zipfile_id 
			var src_type_aicc_id
			var src_type_aicc_zip_id
			var src_type_url_id
			var src_type_ass_inst_id 
			var src_type_pick_aicc_res_id 
			var src_type_pick_res_id 
			var src_type_res_id 
			//--===
			
			var doAll = (document.all!=null) // IE
			var doDOM =(document.getElementById!=null) //Netscape 6 DOM 1.0
			var doLayer =(document.layers!=null) // Netscape 4.x

			usr_id = getUrlParam("usr_id");
			mod_type = getUrlParam('tpl_type');
			course_name=wb_utils_get_cookie("title_prev");
			tpl_type = getUrlParam("tpl_type");
			tpl_subtype = getUrlParam("tpl_subtype");
			wb_utils_set_cookie('mod_type',tpl_subtype);												
			
			function togStatus(ratingQue){
				frmXml.mod_has_rate_q.value = ratingQue;
			}
			
			function GetCSSPElement(id) {
				if (doAll) {
					return document.all[id];
				}else{
					if (doDOM){
						return document.getElementById(id);
					}else{
						return document.layers[id];
					}
				}
			}

			function changeTemplate() {
				var frm = document.frmXml;
				var i = frm.tpl_name.selectedIndex;
				frm.templatethumb.src = frm[frm.tpl_name.options[i].text + "thumb"].value;
				
				if (doAll || doDOM){
					var tpl_desc_val = frm[frm.tpl_name.options[i].text + "desc"].value;
					var tpl_notes_val = frm[frm.tpl_name.options[i].text + "note"].value;
					writeElement('tpldesc',tpl_desc_val);
					writeElement('tplnotes',tpl_notes_val);
				}else{
						frm.tpl_desc.value = frm[frm.tpl_name.options[i].text + "desc"].value;
						frm.tpl_notes.value = frm[frm.tpl_name.options[i].text + "note"].value;
				}			
			}

			function writeElement(id,contents) {
				if(doAll){
					var obj= document.all[id]
				}else if(doDOM){
					var obj = document.getElementById(id)
				}
				if (obj!=null){
					obj.innerHTML = contents;
				}
				
			}
      
			function add_cancel(){
				if(window.document["gen_btn_cancel0"]){
					window.document["gen_btn_cancel0"].width = 0;
					window.document["gen_btn_cancel0"].height = 0;
				}
					window.parent.cancelAdd();
					window.location.href = course_lst.view_info_url(cookie_course_id);
			}
			 
			function check_type(){
				]]><xsl:if test="count(template) > 1">changeTemplate();</xsl:if><![CDATA[
			}
			
			function ins_before(){
				var frm = document.frmXml;				
				var is_Save = true;
				if(frm.mod_subtype.value!='ASS'&&frm.mod_subtype.value!='RDG'&&frm.src_type&&frm.src_type.value=='FILE'&&frm.mod_file.value!=null&&frm.mod_file.value!=''&&!/.+\.mp4$/.test(frm.mod_file.value.toLowerCase())){
				   Dialog.alert(']]><xsl:value-of select="$lab_vdo_desc"/><![CDATA[');			 
				   is_Save =false;			
				}	
				if(is_Save){
				       var success = course_lst.ins_mod_exec(document.frmXml,cookie_course_id,']]><xsl:value-of select="$wb_lang"/><![CDATA[');		
				       if(success){
				       		var index = layer.load(1, {
	  							shade: [0.1,'#fff'] //0.1透明度的白色背景
							});		
				       }
			
				}
				
			}
			function ins_default_date(){
				if (tpl_type != 'EAS' ){
					var frm = document.frmXml
					//Get the server current time
					str = "]]><xsl:value-of select="//cur_time/."/><![CDATA["
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
					
					if (frm.start_mm ){
						frm.start_mm.value = cur_month
						frm.start_yy.value = cur_year
						
						frm.start_hour.value = "00"
						frm.start_min.value = "00"
					}					
					
					if (frm.end_mm ){
						frm.end_mm.value = cur_month
						frm.end_yy.value = cur_year
													
						frm.end_hour.value = "23"
						frm.end_min.value = "59"
					}
				}
			}


			function isMaxUpload(){
				if ( document.frmXml.max_upload_file[0].checked == true )
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
					if(source_content.search('http://') != -1){
						document['src_type_img'].src = imgpath + 'icon_web_browser.gif';
					}*/
					
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
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onunload="wb_utils_close_preloading()" onLoad="ins_default_date();">
			<form enctype="multipart/form-data" name="frmXml" onsubmit="return false">
				<!-- == Start Create Hidden Field ============================================-->
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
				<input type="hidden" name="cur_dt" value="{$current_time}"/>
				<input type="hidden" name="max_usr_attempt" value=""/>
				<input type="hidden" name="cmd" value="ins_mod"/>
				<input type="hidden" name="mod_privilege" value="{$my_privilege}"/>
				<input type="hidden" name="url_success" value=""/>
				<input type="hidden" name="url_failure" value=""/>
				<input type="hidden" name="course_id" value=""/>
				<input type="hidden" name="mod_src_type" value=""/>
				<input type="hidden" name="annotation_html" value=""/>
				<input type="hidden" name="copy_media_from" value="{$source_id}"/>
				<input type="hidden" name="course_timestamp" value=""/>
				<input type="hidden" name="course_struct_xml_cnt" value=""/>
				<input type="hidden" name="course_struct_xml_1" value=""/>
				<input type="hidden" name="cos_type" value="{$cos_type}"/>
				<input type="hidden" name="mod_type" value="{$tpl_type}"/>
				<input type="hidden" name="mod_subtype" value="{$tpl_subtype}"/>
				<input type="hidden" name="lab_start_date" value="{$lab_start_date}"/>
				<input type="hidden" name="lab_end_date" value="{$lab_end_date}"/>
				<input type="hidden" name="cur_dt_dd" value=""/>
				<input type="hidden" name="cur_dt_mm" value=""/>
				<input type="hidden" name="cur_dt_yy" value=""/>
				<input type="hidden" name="cur_dt_hour" value=""/>
				<input type="hidden" name="cur_dt_min" value=""/>
				<input type="hidden" name="module" value=""/>
				<input type="hidden" name="mod_logic"/>
				<input type="hidden" name="mod_show_answer_ind"	value=""/>
				<input type="hidden" name="mod_src_online_link" value="" />
				<xsl:choose>
					<xsl:when test="$tpl_type = 'AICC_AU' or $tpl_type = 'NETG_COK'">
						<input value="no" name="rename" type="hidden"/>
					</xsl:when>
				</xsl:choose>
				<!-- == End  Create Hidden Field ============================================-->
				<!-- == Start Basic Info == -->
				<xsl:if test="$tpl_type != 'NETG_COK'">
					<xsl:call-template name="wb_ui_head">
						<xsl:with-param name="text" select="$lab_basic_info"/>
					</xsl:call-template>
					<xsl:call-template name="wb_ui_line"/>
					<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}" class="Bg">
						<xsl:call-template name="table_seperator"/>
						<!-- Module Type -->
						<xsl:call-template name="draw_mod_type">
							<xsl:with-param name="lab_type" select="$lab_type"/>
							<xsl:with-param name="mod_type" select="$tpl_type"/>
						</xsl:call-template>
						<!--source (for test only)-->
						<xsl:if test="$tpl_type = 'TST' or $tpl_type = 'DXT'">
							<tr>
								<td class="wzb-form-label" valign="top">
									<span class="TitleText">
										<xsl:value-of select="$lab_source"/>
										<xsl:text>：</xsl:text>
									</span>
								</td>
								<td class="wzb-form-control">
									<span class="Text">
										<xsl:choose>
											<xsl:when test="$assessment/body/title">
												<xsl:if test="$assessment/header/objective/desc/text()"><xsl:value-of select="$assessment/header/objective/desc/text()"/> > </xsl:if><xsl:value-of select="$assessment/body/title"/>
											</xsl:when>
											<xsl:otherwise>--</xsl:otherwise>
										</xsl:choose>
									</span>
								</td>
							</tr>
						</xsl:if>
						<!-- Module Title -->
						<xsl:call-template name="draw_title">
							<xsl:with-param name="lab_title" select="$lab_title"/>
							<xsl:with-param name="value" select="$assessment/body/title"/>
						</xsl:call-template>
						<!-- Module Desc -->
						<xsl:call-template name="draw_desc">
							<xsl:with-param name="lab_desc" select="$lab_desc"/>
							<xsl:with-param name="value" select="$assessment/body/desc"/>
						</xsl:call-template>
						
<!-- 						<xsl:if test="$cos_type = 'SELFSTUDY' and ($mod_subtype = 'VOD' or $mod_subtype = 'TST' or $mod_subtype = 'DXT' or $mod_subtype = 'SVY' -->
<!-- 						                                          or $tpl_type = 'VOD' or $tpl_type = 'TST' or $tpl_type = 'DXT' or $tpl_type = 'SVY')"> -->
							<xsl:if test="$tpl_type != 'AICC_AU'">
							<xsl:call-template name="draw_push_to_mobile">
								<xsl:with-param name="lab_push_to_mobile" select="$lab_push_to_mobile"/>
								<xsl:with-param name="lab_yes" select="$lab_yes"/>
								<xsl:with-param name="lab_no" select="$lab_no"/>
							</xsl:call-template>
							</xsl:if>
<!-- 						</xsl:if> -->
					<!-- Module duration -->
						<xsl:if test="$tpl_type = 'VOD' or $tpl_type = 'RDG' or $tpl_type = 'REF'">  <!-- 视频 教材，参考    -->
							<xsl:if test="$tpl_type = 'VOD'">
								<xsl:if test="$cos_type = 'AUDIOVIDEO'">
									<xsl:call-template name="draw_img_file">
										<xsl:with-param name="lab_upload_file" select="$lab_vod_img_link"/>
									</xsl:call-template>
								</xsl:if>
							</xsl:if>
							<xsl:call-template name="draw_req_time"><!-- 视频 教材，参考添加学习时长    -->
								<xsl:with-param name="lab_req_time" select="$lab_req_time"/>
								<xsl:with-param name="value" select="$assessment/body/req_time"/>
								<xsl:with-param name="lab_req_time_desc" select="$lab_req_time_desc"/>
							</xsl:call-template>
							<xsl:if test="$tpl_type = 'VOD'">
								<xsl:if test="$cos_type = 'SELFSTUDY' or $cos_type = 'CLASSROOM'">
									<xsl:call-template name="draw_download">
										<xsl:with-param name="lab_download" select="$lab_download"/>
										<xsl:with-param name="lab_prohibited" select="$lab_prohibited"/>
										<xsl:with-param name="lab_allow" select="$lab_allow"/>
										<xsl:with-param name="download_ind" select="0"/>
										<xsl:with-param name="download_tip" select="$download_tip"/>
									</xsl:call-template>
								</xsl:if>
							</xsl:if>
						</xsl:if>
						<xsl:call-template name="table_seperator"/>
					</table>
					<xsl:call-template name="wb_ui_space"/>
				</xsl:if>
				<!-- == Event Information ==-->
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
						</xsl:call-template>
						<!-- Event Venue -->
						<xsl:call-template name="draw_event_venue">
							<xsl:with-param name="lab_evt_venue" select="$lab_evt_venue"/>
						</xsl:call-template>
						<xsl:call-template name="table_seperator"/>
					</table>
					<xsl:call-template name="wb_ui_space"/>
				</xsl:if>
				<!-- == DXT Section Selection Logic==-->
				<!-- <xsl:if test="$tpl_type = 'DXT'">
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
							<xsl:with-param name="value" select="$assessment/header/@selection_logic"/>
						</xsl:call-template>
						<xsl:call-template name="table_seperator"/>
					</table>
					<xsl:call-template name="wb_ui_space"/>
				</xsl:if> -->
				<!-- == End Basic Info == -->
				<!-- ==Start Middle Section== -->
				<xsl:choose>
					<!-- NETG_COK Case-->
					<xsl:when test="$tpl_type = 'NETG_COK'">
						<xsl:call-template name="wb_ui_head">
							<xsl:with-param name="text">
								<xsl:value-of select="$lab_source"/>
							</xsl:with-param>
						</xsl:call-template>
						<xsl:call-template name="wb_ui_line"/>
						<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}" class="Bg">
							<xsl:call-template name="table_seperator"/>
							<!-- Module Type -->
							<xsl:call-template name="draw_mod_type">
								<xsl:with-param name="lab_type" select="$lab_type"/>
								<xsl:with-param name="mod_type" select="$tpl_type"/>
							</xsl:call-template>
							<xsl:if test="$res_enable = 'true'">
								<xsl:call-template name="draw_pick_aicc_res">
									<xsl:with-param name="lab_source" select="$lab_source"/>
									<xsl:with-param name="lab_from_resource" select="$lab_from_resource"/>
								</xsl:call-template>
							</xsl:if>
							<!-- CDF Filename -->
							<xsl:call-template name="draw_cdf_file">
								<xsl:with-param name="lab_file_cdf" select="$lab_file_cdf"/>
							</xsl:call-template>
							<xsl:call-template name="table_seperator"/>
						</table>
						<xsl:call-template name="wb_ui_space"/>
					</xsl:when>
					<!-- End NETG_COK Case-->
					<!-- Start ASS Case -->
					<xsl:when test="$tpl_type = 'ASS'">
						<xsl:call-template name="wb_ui_head">
							<xsl:with-param name="text">
								<xsl:value-of select="$lab_inst"/>
							</xsl:with-param>
						</xsl:call-template>
						<xsl:call-template name="wb_ui_line"/>
						<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}" class="Bg">
							<xsl:call-template name="table_seperator"/>
							<xsl:call-template name="draw_ass_inst">
								<xsl:with-param name="lab_ass_inst" select="$lab_ass_inst"/>
								<xsl:with-param name="lab_ass_inst_desc" select="$lab_ass_inst_desc"/>
								<xsl:with-param name="lab_inst" select="$lab_inst"/>
							</xsl:call-template>
							<xsl:call-template name="draw_src_url">
								<xsl:with-param name="lab_inst" select="$lab_inst"/>
								<xsl:with-param name="lab_source" select="$lab_source"/>
								<xsl:with-param name="lab_url" select="$lab_url"/>
							</xsl:call-template>
                            <xsl:call-template name="draw_src_file">
								<xsl:with-param name="lab_upload_file" select="$lab_upload_file"/>
							</xsl:call-template>
							<xsl:call-template name="draw_src_zipfile">
								<xsl:with-param name="lab_zip" select="$lab_zip"/>
								<xsl:with-param name="lab_default_page" select="$lab_default_page"/>
							</xsl:call-template>
							<xsl:if test="$tpl_type = 'VOD'">
								<xsl:call-template name="draw_src_video">
									<xsl:with-param name="lab_inst" select="$lab_inst"/>
									<xsl:with-param name="lab_source" select="$lab_source"/>
									<xsl:with-param name="lab_online_video" select="$lab_online_video"/>
								</xsl:call-template>
							</xsl:if>
							<input type="hidden" name="mod_src_link" value=""/>
							<xsl:call-template name="table_seperator"/>
						</table>
						<xsl:call-template name="wb_ui_space"/>
						<xsl:call-template name="wb_ui_head">
							<xsl:with-param name="text">
								<xsl:value-of select="$lab_grading_policy"/>
							</xsl:with-param>
						</xsl:call-template>
						<xsl:call-template name="wb_ui_line"/>
						<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}" class="Bg">
							<xsl:call-template name="table_seperator"/>
							<!-- Grading Scheme-->
							<xsl:call-template name="draw_grading">
								<xsl:with-param name="lab_grading" select="$lab_grading"/>
								<xsl:with-param name="lab_grade" select="$lab_grade"/>
							</xsl:call-template>
							<xsl:call-template name="draw_by_score">
								<xsl:with-param name="lab_by_score" select="$lab_by_score"/>
								<xsl:with-param name="lab_max_score" select="$lab_max_score"/>
								<xsl:with-param name="lab_pass_score" select="$lab_passing_score"/>
								<xsl:with-param name="mode">ins</xsl:with-param>
							</xsl:call-template>
							<xsl:call-template name="draw_submission">
								<xsl:with-param name="lab_no_of_submission" select="$lab_no_of_submission"/>
								<xsl:with-param name="lab_not_limited" select="$lab_not_limited"/>
								<xsl:with-param name="lab_max_upload" select="$lab_max_upload"/>
								<xsl:with-param name="lab_files" select="$lab_files"/>
							</xsl:call-template>
							<!-- Message to student after submission: -->
						<!-- 	<xsl:call-template name="draw_submission_msg">
								<xsl:with-param name="lab_submission" select="$lab_submission"/>
								<xsl:with-param name="lab_submission_desc" select="$lab_submission_desc"/>
								<xsl:with-param name="lab_default_submission" select="$lab_default_submission"/>
							</xsl:call-template> -->
							<!-- notify_student -->
							<xsl:call-template name="draw_nofity_student">
								<xsl:with-param name="lab_notify_student" select="$lab_notify_student"/>
								<xsl:with-param name="lab_yes" select="$lab_yes"/>
								<xsl:with-param name="lab_no" select="$lab_no"/>
							</xsl:call-template>
							<!-- Due Date-->
							<xsl:call-template name="draw_ass_due_date">
								<xsl:with-param name="lab_due_date" select="$lab_due_date"/>
								<xsl:with-param name="lab_ass_due_date_unspecified" select="$lab_ass_due_date_unspecified"/>
								<xsl:with-param name="lab_ass_due_date_unspecified" select="$lab_ass_due_date_unspecified"/>
								<xsl:with-param name="lab_due_date_num" select="$lab_due_date_num"/>
								<xsl:with-param name="lab_due_date_num_inst_1" select="$lab_due_date_num_inst_1"/>
								<xsl:with-param name="lab_due_date_num_inst_2" select="$lab_due_date_num_inst_2"/>
								<xsl:with-param name="lab_due_date_non_obligatory" select="$lab_due_date_non_obligatory"/>
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
					<!-- End ASS Case -->
					<!-- Start AICC_AU Case -->
					<xsl:when test="$tpl_type = 'AICC_AU'">
						<xsl:call-template name="wb_ui_head">
							<xsl:with-param name="text">
								<xsl:value-of select="$lab_source"/>
							</xsl:with-param>
						</xsl:call-template>
						<xsl:call-template name="wb_ui_line"/>
						<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}" class="Bg">
							<xsl:call-template name="table_seperator"/>
							<xsl:if test="$res_enable = 'true'">
								<xsl:call-template name="draw_pick_aicc_res">
									<xsl:with-param name="lab_source" select="$lab_source"/>
									<xsl:with-param name="lab_from_resource" select="$lab_from_resource"/>
								</xsl:call-template>
							</xsl:if>
							<xsl:call-template name="draw_src_aicc">
								<xsl:with-param name="lab_enter_aicc" select="$lab_enter_aicc"/>
								<xsl:with-param name="lab_enter_aicc_zip" select="$lab_enter_aicc_zip"/>
								<xsl:with-param name="lab_file_crs" select="$lab_file_crs"/>
								<xsl:with-param name="lab_file_cst" select="$lab_file_cst"/>
								<xsl:with-param name="lab_file_des" select="$lab_file_des"/>
								<xsl:with-param name="lab_file_ort" select="$lab_file_ort"/>
								<xsl:with-param name="lab_file_au" select="$lab_file_au"/>
								<xsl:with-param name="lab_source" select="$lab_source"/>
							</xsl:call-template>
							<xsl:call-template name="table_seperator"/>
						</table>
						<xsl:call-template name="wb_ui_space"/>
					</xsl:when>
					<!-- End AICC_AU Case-->
					<!-- Start VCR Case -->
					<xsl:when test="$tpl_type = 'VCR'">
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
							</xsl:call-template>
							<xsl:call-template name="table_seperator"/>
						</table>
						<xsl:call-template name="wb_ui_space"/>
					</xsl:when>
					<!-- End VCR CAse -->
					<!-- Start EAS Case -->
					<xsl:when test="$tpl_subtype = 'EAS'">
						<xsl:call-template name="wb_ui_head">
							<xsl:with-param name="text">
								<xsl:value-of select="$lab_grading_policy"/>
							</xsl:with-param>
						</xsl:call-template>
						<xsl:call-template name="wb_ui_line"/>
						<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}" class="Bg">
							<xsl:call-template name="table_seperator"/>
							<!-- Grading Scheme-->
							<xsl:call-template name="draw_grading">
								<xsl:with-param name="lab_grading" select="$lab_grading"/>
								<xsl:with-param name="lab_grade" select="$lab_grade"/>
							</xsl:call-template>
							<xsl:call-template name="draw_by_score">
								<xsl:with-param name="lab_by_score" select="$lab_by_score"/>
								<xsl:with-param name="lab_max_score" select="$lab_max_score"/>
								<xsl:with-param name="lab_pass_score" select="$lab_passing_score"/>
								<xsl:with-param name="mode">ins</xsl:with-param>
							</xsl:call-template>
							<xsl:call-template name="table_seperator"/>
						</table>
						<xsl:call-template name="wb_ui_space"/>
					</xsl:when>
					<!-- End EAS Case -->
					<!-- Start Normal Case -->
					<xsl:when test="$tpl_type = 'VOD' or $tpl_type = 'LCT' or $tpl_type = 'TUT' or $tpl_type = 'RDG' or $tpl_type = 'GAG' or $tpl_type = 'MBL'">
						<xsl:call-template name="wb_ui_head">
							<xsl:with-param name="text">
								<xsl:value-of select="$lab_source"/>
							</xsl:with-param>
						</xsl:call-template>
						<xsl:call-template name="wb_ui_line"/>
						<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}" class="Bg wzb-ui-desc-text">
							<xsl:call-template name="table_seperator"/>
							<xsl:if test=" $tpl_type = 'VOD'">
								<tr>
									<td>
										<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
									</td>
									<td>
										
										<xsl:copy-of select="$lab_vdo_desc"/>
									</td>
								</tr>
							</xsl:if>
							<xsl:if test=" $tpl_type = 'RDG'">
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
							<xsl:choose>
								<xsl:when test="$res_enable = 'true' and $tpl_type != 'VOD'">
								
								</xsl:when>
								<xsl:otherwise>
									<input type="hidden" name="source_type" value=""/>
									<input type="hidden" name="source_content" value=""/>
								</xsl:otherwise>
							</xsl:choose>
							<!-- 网址 -->
							<xsl:if test="$tpl_type != 'MBL'">
								<xsl:call-template name="draw_src_url">
									<xsl:with-param name="lab_inst" select="$lab_inst"/>
									<xsl:with-param name="lab_source" select="$lab_source"/>
									<xsl:with-param name="lab_url" select="$lab_url"/>
									<xsl:with-param name="show_title"><xsl:choose>
											<xsl:when test="$res_enable = 'false'">true</xsl:when>
											<xsl:otherwise>false</xsl:otherwise>
									</xsl:choose></xsl:with-param>
									<xsl:with-param name="checked"><xsl:choose>
											<xsl:when test="$res_enable = 'false' or $tpl_type = 'RDG'">true</xsl:when>
											<xsl:otherwise>false</xsl:otherwise>
									</xsl:choose></xsl:with-param>								
								</xsl:call-template>
							</xsl:if>
							
							<!-- 上传文档    /视频-->
							<xsl:choose>
							   <xsl:when test="$tpl_type = 'VOD'">  	 <!-- 已改为上传视频 -->
							      <xsl:call-template name="draw_src_file">  
									<xsl:with-param name="lab_upload_file" select="$lab_upload_video"/>
									<xsl:with-param name="checked">true</xsl:with-param>
									<xsl:with-param name="is_vod">true</xsl:with-param>
									<xsl:with-param name="remind" select="$lab_vod_remind_new"/>
									<xsl:with-param name="lang" select="$wb_lang"/>
								 </xsl:call-template>
							   </xsl:when>
							   <xsl:otherwise>
							       <xsl:call-template name="draw_src_file">  
									<xsl:with-param name="lab_upload_file" select="$lab_upload_file"/>
								 </xsl:call-template> 
							   </xsl:otherwise>
							</xsl:choose>
							
							<!-- Online video -->
							<xsl:if test="$tpl_type = 'VOD'">
								<xsl:call-template name="draw_src_video">
									<xsl:with-param name="lab_inst" select="$lab_inst"/>
									<xsl:with-param name="lab_source" select="$lab_source"/>
									<xsl:with-param name="lab_online_video" select="$lab_online_video"/>
									<xsl:with-param name="show_title">
										<xsl:choose>
											<xsl:when test="$res_enable = 'false'">true</xsl:when>
											<xsl:otherwise>false</xsl:otherwise>
										</xsl:choose></xsl:with-param>
									<xsl:with-param name="checked">
										<xsl:choose>
											<xsl:when test="$res_enable = 'false' or $tpl_type = 'RDG'">true</xsl:when>
											<xsl:otherwise>false</xsl:otherwise>
										</xsl:choose>
									</xsl:with-param>								
								</xsl:call-template>
							</xsl:if>
							
							<!-- 上传ZIP文档 -->
						 	<xsl:if test="$tpl_subtype != 'VOD' and $tpl_subtype != 'MBL' and $tpl_subtype != 'RDG'">
								<xsl:call-template name="draw_src_zipfile">
									<xsl:with-param name="lab_zip" select="$lab_zip"/>
									<xsl:with-param name="lab_default_page" select="$lab_default_page"/>
								</xsl:call-template>
							</xsl:if>  
							<input type="hidden" name="mod_src_link" value=""/>
							<!--End 1-->
							<!--Start 2-->
							<xsl:if test="$tpl_subtype = 'LCT' or $tpl_subtype = 'TUT'">
								<xsl:call-template name="draw_src_wizpack">
									<xsl:with-param name="lab_wizpack" select="$lab_wizpack"/>
								</xsl:call-template>
							</xsl:if>
							<!--End 2-->
							<!-- Start 4-->
							<!--End 4-->
							<xsl:call-template name="table_seperator"/>
						</table>
						<xsl:call-template name="wb_ui_space"/>
					</xsl:when>
					<!-- End Normal Case-->
				</xsl:choose>
				<!-- ==End Middle Section== -->
				<!-- Start Display Options-->
				<xsl:if test="display/option/general/@moderator = 'true' or display/option/general/@instructor = 'true' or display/option/general/@organization = 'true' or display/option/general/@language = 'true' or display/option/progress/@difficulty = 'true'">
					<xsl:call-template name="wb_ui_head">
						<xsl:with-param name="text">
							<xsl:value-of select="$lab_detail_info"/>
						</xsl:with-param>
					</xsl:call-template>
					<xsl:call-template name="wb_ui_line"/>
					<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}" class="Bg">
						<xsl:call-template name="table_seperator"/>
						<xsl:call-template name="draw_instructor">
							<xsl:with-param name="lab_instructor" select="$lab_instructor"/>
							<xsl:with-param name="lab_instructor_select" select="$lab_instructor_select"/>
							<xsl:with-param name="lab_instructor_enter" select="$lab_instructor_enter"/>
							<xsl:with-param name="instructor_list" select="instructor_list"/>
						</xsl:call-template>
						<xsl:call-template name="draw_moderator">
							<xsl:with-param name="lab_instructor" select="$lab_instructor"/>
							<xsl:with-param name="lab_instructor_select" select="$lab_instructor_select"/>
							<xsl:with-param name="instructor_list" select="instructor_list"/>
						</xsl:call-template>
						<xsl:call-template name="draw_organization">
							<xsl:with-param name="lab_organization" select="$lab_organization"/>
						</xsl:call-template>
						<xsl:call-template name="draw_language">
							<xsl:with-param name="lab_lang" select="$lab_lang"/>
							<xsl:with-param name="lab_gb_chinese" select="$lab_gb_chinese"/>
							<xsl:with-param name="lab_ch_chinese" select="$lab_ch_chinese"/>
							<xsl:with-param name="lab_english" select="$lab_english"/>
						</xsl:call-template>
						<xsl:call-template name="draw_difficulty">
							<xsl:with-param name="lab_diff" select="$lab_diff"/>
							<xsl:with-param name="lab_easy" select="$lab_easy"/>
							<xsl:with-param name="lab_normal" select="$lab_normal"/>
							<xsl:with-param name="lab_hard" select="$lab_hard"/>
						</xsl:call-template>
						<!-- time_event -->
						<xsl:if test="display/option/progress/@pass_score != 'true'">
							<xsl:call-template name="draw_time_event">
								<xsl:with-param name="lab_duration" select="$lab_duration"/>
								<xsl:with-param name="lab_mins" select="$lab_mins"/>
								<xsl:with-param name="lab_time_limit" select="$lab_time_limit"/>
								<xsl:with-param name="lab_suggested_time" select="$lab_suggested_time"/>
							</xsl:call-template>
						</xsl:if>
						<xsl:call-template name="table_seperator"/>
					</table>
					<xsl:call-template name="wb_ui_space"/>
				</xsl:if>
				<xsl:if test="(display/option/progress/@pass_score = 'true') and ($tpl_type = 'NETG_COK')">
					<xsl:call-template name="wb_ui_head">
						<xsl:with-param name="text" select="$lab_grading_policy"/>
					</xsl:call-template>
					<xsl:call-template name="wb_ui_line"/>
				</xsl:if>
				<xsl:if test="(display/option/progress/@pass_score = 'true' or display/option/progress/@max_usr_attempt = 'true') and ($tpl_type != 'NETG_COK') and ($tpl_type != 'AICC_AU') and ($tpl_type != 'SCO')">
					<xsl:call-template name="wb_ui_head">
						<xsl:with-param name="text" select="$lab_grading_policy"/>
					</xsl:call-template>
					<xsl:call-template name="wb_ui_line"/>
					<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}" class="Bg">
						<xsl:call-template name="table_seperator"/>
						<xsl:call-template name="draw_pass_score">
							<xsl:with-param name="lab_pass_score"><xsl:choose>
									<xsl:when test="$mod_subtype = 'DXT' or $mod_subtype = 'TST'"><xsl:value-of select="$lab_pass_score"/></xsl:when>
									<xsl:otherwise><xsl:value-of select="$lab_passing_score"/></xsl:otherwise>
								</xsl:choose>
								</xsl:with-param>
						</xsl:call-template>
						<xsl:call-template name="draw_time_event">
							<xsl:with-param name="lab_duration" select="$lab_duration"/>
							<xsl:with-param name="lab_mins" select="$lab_mins"/>
							<xsl:with-param name="lab_time_limit" select="$lab_time_limit"/>
							<xsl:with-param name="lab_suggested_time" select="$lab_suggested_time"/>
							<xsl:with-param name="lab_time_unlimit" select="$lab_time_unlimit"/>
							<xsl:with-param name="lab_time_limit_tip" select="$lab_time_limit_tip"/>
						</xsl:call-template>
					
						<xsl:call-template name="draw_max_usr_attempt">
							<xsl:with-param name="lab_max_attempt_num" select="$lab_max_attempt_num"/>
							<xsl:with-param name="lab_max_attempt_num_unlimited" select="$lab_max_attempt_num_unlimited"/>
							<xsl:with-param name="lab_max_attempt_num_limited" select="$lab_max_attempt_num_limited"/>
							<xsl:with-param name="lab_max_attempt_num_times" select="$lab_max_attempt_num_times"/>
						</xsl:call-template>
						<xsl:call-template name="draw_sub_after_passed_ind">
							<xsl:with-param name="lab_text" select="$lab_sub_after_passed_ind_text"/>
						</xsl:call-template>
						<xsl:call-template name="draw_show_answer_ind_temp">
							<xsl:with-param name="lab_text" select="$lab_show_answer_ind_text"/>
						</xsl:call-template>
						<xsl:call-template name="draw_show_answer_ind_temp">
							<xsl:with-param name="checked">1</xsl:with-param>
							<xsl:with-param name="lab_text" select="$lab_on_show_answer_ind_text"/>
						</xsl:call-template>
						<!--每次参加测验合格后向学员显示试题和答案 -->
						<xsl:call-template name="mod_show_answer_after_passed_ind">
							<xsl:with-param name="checked" select="header/@mod_show_answer_after_passed_ind"/>
							<xsl:with-param name="lab_text" select="$lab_mod_show_answer_after_passed_ind_text"/>
						</xsl:call-template>
						<!-- 由管理员控制测验开始 -->
						<xsl:if test="type/text() ='DXT' or type/text() = 'TST'">
							<xsl:call-template name="draw_show_managed_ind">
								<xsl:with-param name="lab_text" select="$lab_managed_ind_text"/>
							</xsl:call-template>
						</xsl:if>
						<!--=======================================-->
						<xsl:call-template name="draw_show_save_and_suspend_ind">
							<xsl:with-param name="lab_text" select="$lab_show_save_and_suspend_ind_text"/>
							<xsl:with-param name="watch_out" select="$lab_only_pc_valid"></xsl:with-param>
						</xsl:call-template>
						<xsl:if test="type/text() ='DXT' or type/text() = 'TST'">
							<!--测试一屏多题  -->	
							<xsl:call-template name="draw_text_style">
								<xsl:with-param name="lab_title" select="$lab_text_style"/>
								<xsl:with-param name="lab_style1" select="$lab_text_style_only"/>
								<xsl:with-param name="lab_style2" select="$lab_text_style_many"/>
							</xsl:call-template>
						</xsl:if>
						<xsl:call-template name="table_seperator"/>
					</table>
					<xsl:call-template name="wb_ui_space"/>
				</xsl:if>
				<xsl:if test="display/option/datetime/@eff_end = 'true' or display/option/general/@status = 'true' or display/option/datetime/@eff_start = 'true'">
					<xsl:call-template name="wb_ui_head">
						<xsl:with-param name="text">
							<xsl:value-of select="$lab_display_option"/>
						</xsl:with-param>
					</xsl:call-template>
					<xsl:call-template name="wb_ui_line"/>
					<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}" class="Bg">
						<xsl:call-template name="table_seperator"/>
						<!-- Status-->
						<xsl:call-template name="draw_status">
							<xsl:with-param name="lab_status" select="$lab_status"/>
							<xsl:with-param name="lab_online" select="$lab_online"/>
							<xsl:with-param name="lab_offline" select="$lab_offline"/>
							<xsl:with-param name="readonly">
								<xsl:choose>
									<xsl:when test="(type/text()='TST' or type/text()='DXT') and not($assessment)">TRUE</xsl:when>
									<xsl:otherwise>FALSE</xsl:otherwise>
								</xsl:choose>
							</xsl:with-param>
							<xsl:with-param name="isEnrollment_related" select="$isEnrollment_related"></xsl:with-param>
						</xsl:call-template>
						<xsl:choose>
							<xsl:when test="$cos_type != 'AUDIOVIDEO'">
								<xsl:call-template name="draw_effective_start">
									<xsl:with-param name="lab_start_date" select="$lab_start_date"/>
									<xsl:with-param name="lab_immediate" select="$lab_immediate"/>
									<xsl:with-param name="isEnrollment_related" select="$isEnrollment_related"></xsl:with-param>
								</xsl:call-template>
								<xsl:call-template name="draw_effective_end">
									<xsl:with-param name="lab_end_date" select="$lab_end_date"/>
									<xsl:with-param name="lab_unlimit" select="$lab_unlimit"/>
									<xsl:with-param name="isEnrollment_related" select="$isEnrollment_related"></xsl:with-param>
								</xsl:call-template>
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
				<xsl:if test="type/text() !='TST' and type/text() !='DXT' and type/text() !='RDG'">
					<!-- Template -->
					<xsl:if test="count(template) > 1">
						<xsl:call-template name="wb_ui_space"/>
						<xsl:call-template name="wb_ui_head">
							<xsl:with-param name="text">
								<xsl:value-of select="$lab_template"/>
							</xsl:with-param>
						</xsl:call-template>
						<xsl:call-template name="wb_ui_line"/>
						<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}" class="Bg">
							<xsl:call-template name="table_seperator"/>
							<xsl:call-template name="draw_template">
								<xsl:with-param name="lab_template" select="$lab_template"/>
								<xsl:with-param name="template_list" select="."/>
							</xsl:call-template>
				
							<xsl:call-template name="table_seperator"/>
						</table>
					</xsl:if>
				</xsl:if>	
				<xsl:if test="$tpl_type = 'LCT' or $tpl_type = 'TUT'">
					<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}" class="Bg">
						<xsl:call-template name="draw_annotation">
							<xsl:with-param name="lab_annotation" select="$lab_annotation"/>
							<xsl:with-param name="lab_ashtml" select="$lab_ashtml"/>
						</xsl:call-template>
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
				<xsl:if test="count(template) = 1">
					<input type="hidden" name="tpl_name" value="{template/@name}"/>
				</xsl:if>
				<div class="wzb-bar">
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_ok"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:ins_before()</xsl:with-param>
						<xsl:with-param name="id">submitButton</xsl:with-param>
					</xsl:call-template>
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:add_cancel()</xsl:with-param>
						<xsl:with-param name="id">cancelButton</xsl:with-param>
					</xsl:call-template>
			</div>
			</form>
		</body>
	</xsl:template>
	<!--==================================================================-->
	<!--=========================================================-->
	<xsl:template name="debug_show_order"/>
</xsl:stylesheet>
