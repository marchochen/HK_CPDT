﻿//注：不要再住该文件中添加label, 后面会去掉该文件，所有Label请加到对应的功能模块中,详细请看以下设计文档
//http://svn.cyberwisdom.net.cn:8231/svn/wizbank/产品文档/产品开发、测试、设计、需求文档/技术设计文档/LMS_6_国际化设计文档.doc

var text_label_old = {
	page_title : "Cyberwisdom wizBank 6.2  移動學習整合管理平臺 ",
	wizbank_version : "wizBank6.2 @cyberwisdom. 版權所有",
	session_timeout_message : '您的登錄已經失效!',
	 
	session_timeout_message_2 : '請重新登錄，本頁將會在<span id="second" style="color:#00aeef;margin:0 2px;">5</span>秒後自動跳回登錄頁',
	session_timeout_message_3 : '<input type="button"  class="btn   wzb-btn-yellow"  onclick="go()" value="登錄頁面"></input>',
	
	lab_error : '錯誤',
	lab_error_msg : '請點擊<a href="javascript: go()">這裡</a>轉到<a href="javascript: go()">首頁</a>',
	error_data_not_found : '記錄不存在或已經刪除',
	error_no_authority : '沒有權限',
	error_no_authority_desc : '您沒有權限进進行此操作',
	error_msg_need_app : '請先報名再進行此操作',
	error_group_dissolved : '該群組已解散',
	error_repeat_score : '不能重復評分',
	error_landed_somewhere_else : '您的帳號已在其它地方登錄，您已被擠下線；若非您本人操作，請與管理員聯系。',
	error_limit_reached : '你已經完成了此測驗所允許的參加次數。',
	error_server_error : '出錯啦！服務器內部錯誤，請聯繫管理員。',
	warning_notice : '提示',
	warning_delete_notice : '確認刪除嗎？',
	warning_end_notice : '該網上內容已經結束或未開始',
	// table国际化
	lab_table_data_null : '暫無相關數據顯示',
	lab_table_data_no_more : '沒有更多數據顯示',
	lab_table_btn_load_more : '加載更多',
	lab_table_stat : '顯示 {from} - {to}，共 {total} 條',
	lab_table_before_page : '第',
	lab_table_after_page : '頁，共 <span> 1 </span> 頁',
	lab_table_first : '第一頁',
	lab_table_prev : '前一頁',
	lab_table_next : '下一頁',
	lab_table_last : '最後頁',
	lab_table_refresh : '刷新',
	lab_table_loading : '加載中...',
	lab_table_error : '加載錯誤',
	lab_table_empty : '沒有記錄',
    lab_table_empty_cpd : '还没有註冊組别',
	// 语言
	language_en_us : 'English',
	language_zh_cn : '简体中文 ',
	language_zh_hk : '繁體中文 ',
	// role
	lab_rol_ADM : '系統管理員',
	lab_rol_APPR : '經理',
	lab_rol_CDN : '課程開發',
	lab_rol_GADM : '總培訓管理員',
	lab_rol_INSTR : '講師',
	lab_rol_NLRN : '學員',
	lab_rol_TADM : '培訓管理員',
	lab_rol_TUT : '授課老師',
	lab_rol_VISTR : '訪客',
	lab_rol_EXA : '考試監考員',
	lab_rol_LADM : 'e-Library Administrator',
	// validate
	validate_must_be : '值必須',
	validate_gt_eq_0 : '大於等於0',
	validate_max_5 : '不能超過5',
	validate_not_null : '不能為空',
	validate_max_200 : '最多輸入200個字的評論',
	validate_max_400 : '最多輸入400個字元的評論（200個中文字）',
	alert_clear_text : '確認清空嗎？',
	// Units 单位
	units_points : "分",
	// menu
	menu_started : '首頁',
	menu_information : '資訊',
	menu_news : '消息',
	menu_article : '資訊',
	menu_announce : '通告',
	menu_study : '學習',
	menu_singup_course : '已報名課程',
	menu_recommend : '推薦課程',
	menu_course_catalog : '課程目錄',
	menu_open_course : '公開課',
	menu_study_map : '學習日程表',
	menu_profession : '崗位發展路徑圖',
	menu_test : '考試',
	menu_signup_course : '已報名考試',
	menu_test_catalog : '考試目錄',
	menu_recommend_exam : '推薦考試',
	menu_community : '社區',
	menu_answer : '在線問答',
	menu_group : '群組',
	menu_wiki : 'wiki',
	menu_course_top : '課程排行榜',
	menu_dynamic : '動態消息',
	menu_home : '我的主頁',
	menu_subordinate : '我的下屬',
	menu_certificate : '證書',
	menu_role : '角色',
	menu_language : '語言',
	menu_exit : '退出',
	menu_credit_rank : '積分排行榜',
	menu_learning_rank : '學習排行榜',
	menu_help : '幫助中心',
	// 培训中心
	traning_center : '培訓中心',
	// 状态
	status_yes : '是',
	status_no : '否',
	// 通用字段
	global_title : '標題',
	global_name : '名稱',
	global_status : '狀態',
	global_current_status : '當前狀態',
	global_require_status : '是否滿足',
	global_kinds : '類型',
	global_select_file : '選擇文件',
	global_more : '更多',
	global_show_more : '展開更多',
	global_que_total : '共$data題',
	global_content : '內容',
	global_click_rate : '點擊',
	global_publish : '發佈',
	global_unpublish : '取消發布',
	global_operate_success : '操作成功！',
	global_operate_error : '操作失敗！',
	global_course_top : '課程排行榜',
	global_credit_rank : '積分排行榜',
	global_learning_rank : '學習排行榜',
	global_rank_notes : '(以下數據每日更新一次)',
	know_ta : '了解Ta',
	know_qunzu : '了解群組',
	update_ok : '修改成功',
	power_error : '沒有足夠的權限修改',
	global_wel : '歡迎來到e-Learning學習平臺',
	no_select_file : '沒有選擇文件',
	global_upload_file : '上傳文件',
	global_upload_image : '上傳圖片',
	global_upload_document : '上傳文檔',
	global_upload_vedio : '上傳視頻',
	global_upload_vedio_online : '在綫視頻',
	global_unpublished : '未發布',
	// 课程类型
	selfstudy : '網上課程',
	classroom : '面授課程',
	integrated : '項目式培訓',
	// 考试类型
	exam_myself : '自選考試',
	exam_selfstudy : '網上考試',
	exam_classroom : '離線考試',
	// 课程状态
	status_all : '全部',
	status_inprogress : '進行中',
	status_completed : '已完成',
	status_viewed : '已查閱',
	status_admitted : '報名成功',
	status_pending : '審批中',
	status_others : '其他',
	status_notapp : '未報名',
	status_pass : '已通過',
	status_withdrawn : '已放棄',
	status_fail : '未完成',
	status_not_started : '未開始',
	status_all_2 : '所有',
	status_flunk : '不合格',
	status_waiting : '等候名單',
	status_passed : '合格',
	// time
	second_ago : '秒前',
	minute_ago : '分鐘前',
	hour_ago : '小時前',
	day_ago : '天前',
	month_ago : '月前',
	year_ago : '年前',
	time_unlimited : '無限期 ',
	time_to : '至',
	time_by : '至',
	time_from : '由',
	time_before : '或之前',
	time_minute : '分鐘',
	time_day : '天',
	time_hour : '小时',
	time_after_day : '天，由報名成功當日開始計算',
	// 公告
	announce_title : '通告',
	announce_readed : '已閲讀',
	article_title : '資訊',
	article_catalog : '分類',
	article_the_hottest : '最熱',
	article_the_new : '最新',
	article_popularity : '人氣',
	article_desc_range : '簡介不能超過400個字元（200個中文）',
	// 动态
	doing_title : '動態消息',
	doing_your_doing : '你的動態',
	doing_action : '更新了狀態',
	doing_action_share : '分享了',
	doing_action_like : '讚了',
	doing_action_comment : '評價了',
	doing_action_reply : '回復了你',
	doing_action_create_group : '創建了群組',
	doing_action_dissmiss_group : '解散了群組',
	doing_action_app_group : '通過審批加入了群組',
	doing_action_group_doing : '在群組發表言論',
	doing_action_open_group : '把群組修改為公開',
	doing_action_add_question : '提出問題',
	doing_action_add_answer : '回答了問題',
	doing_action_enroll_cos : '報名了',
	doing_action_completed_cos : '完成了',
	doing_list : '動態列表',
	doing_publish : '發表了',
	doing_please_comment : '請您評論',
	doing_input_comment_desc : '最多輸入200個字的評論',
	doing_image : '圖片',
	doing_doc : '文檔',
	doing_video : '視頻',
	doing_publish_comment : '發表評論',
	doing_all : '所有動態',
	doing_me : '我的動態',
	doing_course : '學習動態',
	doing_answer : '問答動態',
	doing_group : '群組動態',
	doing_lowercase_course : '課程',
	doing_lowercase_exam : '考試',
	doing_lowercase_article : '資訊',
	doing_lowercase_knowledge : '知識',
	doing_lowercase_title : '動態消息',
	doing_lowercase_group : '群組',

	click_down : '展開',
	click_up : '收起',
	condition : '篩選條件',
	required : '必修',
	elective : '選修',
	condition_required : '必修條件',
	condition_elective : '選修條件',
	// 考试
	exam_type : '考試類型',
	exam_introduction : '考試簡介',
	detail_exam_detail : '考試詳情',
	detail_exam_notice : '考試通告',
	exam_content_date : '考試日期',
	exam_class : '考試場次',
	exam_duration : '考試',
	exam_welcome : '歡迎來到',
	exam_name : '考試',
	exam_title : '考試名稱',
	exam_code : '考試編號',
	exam_address : '考試地點',
	exam_interval : '考試期限',

	exam_doing_quickly : '快行動吧！',
	// 课程
	course_type : '課程類型',
	course_or_exam : '課程/考試',
	course_or_exam_type : '課程類型/考試類型',
	course_publish_date : '發佈日期',
	course_publish_time : '發佈時間',
	course_introduction : '課程簡介',
	course_progress : '學習進度',
	exam_progress : '考試進度',
	course_enrollment_date : '報名日期',
	exam_enrollment_date : '報名日期',
	course_progress_desc : '（進度每天更新一次）',
	course_status : '狀態',
	course_number : '學習人數',
	course_title : '課程名稱',
	course_time_long : '學習時長',
	course_audience : '學習對象',
	course_credit : '學分',
	enrollment_duration : '報名期限',
	course_code : '課程編號',
	course_is_approval : '需要審批',
	course_train_address : '地址',
	course_train_interval : '培訓期限',
	class_title : '班級名稱',
	class_code : '班級編號',
	class_address : '班級地址',
	class_introduction : '班級簡介',
	content_duration : '培訓期限',
	course_score : '成績',
	course_end_day : '天',
	course_fee : '報名費',
	course_objective : '目標',
	course_exemptions : '免修條件',
	course_prerequisites : '必備知識',
	course_contents : '內容',
	course_remarks : '備註',
	course_fulfill : '已滿足',
	course_unfulfill : '未滿足',
	course_grading : '評分中',
	course_fail_resubmit : '不合格，請重新提交',
	// 已报名课程
	course_signup : '已報名課程',
	course_completed_date : '結訓日期',
	course_class_period : '培訓日期',
	course_content_date : '培訓日期',
	course_signup_date : '報名成功當日',
	course_date_nolimit : '無限期',
	// 已报名考试
	exam_signup : '已報名考試',
	// 课程目录
	course_catalog : '課程目錄',
	course_list : '課程列表',
	catalog_kinds : '目錄分類',
	start_time : '開始時間',
	cos_start_time : '課程開始時間',
	not_limit : '不限制',
	next_week : '未來一週',
	next_month : '未來一個月',
	next_quarter : '未來三個月',
	// 考试目录
	exam_catalog : '考試目錄',
	exam_list : '考試列表',

	// 推荐课程
	recommend_course : '推薦課程',
	recommend_type : '建議來自',
	recommend_group : '部分推薦',
	recommend_grade : '職級推薦',
	recommend_tadm : '培訓部推薦',
	recommend_sup : '上司推薦',
	recommend_myself : '自選課程',
	recommend_position : '崗位推薦',
	recommend_study_status : '學習狀態',
	recommend_all_course : '所有課程',
	// 推荐考试
	recommend_exam : '推薦考試',
	recommend_all_exam : '所有考試',
	// 排行
	rank_ranking : '排名',
	rank_praise : '讚',
	rank_be_praise : '被讚',
	rank_day : '天',
	last_update_time : '上次更新時間',
	// 用户
	usr_password_type :'請使用純英文字母/數位/下劃線/橫線的組合',
	usr_password_erre :'請使用純英文字母/數位/下劃線/橫線的組合(必須同時包含英文字母和數字)且長度為',
	usr_name : '用戶名',
	usr_nickname : '暱稱',
	usr_group : '總用戶組',
	usr_credit : '總積分',
	usr_credit_rank : '總積分',//排行榜用
	usr_display_name : '名稱',
	usr_full_name : '全名',
	usr_gender : '性別',
	usr_birthday : '出生日期',
	usr_detail : '詳細個人資料',
	usr_contact_information : '聯絡資料',
	usr_mail : '電子郵件 ',
	usr_mail_error : '電子郵件格式錯誤',
	usr_tel : '電話',
	usr_tel_error : '請輸入正確的電話號碼',
	usr_fax : '傳真',
	usr_qq : 'QQ',
	usr_staff_no : '員工編號',
	usr_weixin : '微信',
	usr_job_title : '職位',
	usr_join_datetime : '入職日期',
	usr_grade : '職級',
	usr_grade_information : '就業資料',
	usr_other_information : '其他資料',
	usr_msn : 'MSN',
	usr_blog : '博客網址',
	usr_personal_description : '個人描述',
	usr_interest : '興趣',
	usr_profile_picture : '個人頭像',
	usr_keep_head : '保留現有頭像',
	usr_default_head : '使用預設頭像',
	usr_upload_head : '上傳一個本地圖片作為顯示的圖像',
	usr_browse : '瀏覽',
	usr_update_ok : '修改成功',
	usr_img_type_limit : '圖片類型必須為\'jpg/jpeg/gif/png\'',
	usr_check_grp_name : '群組名稱已存在，請重新輸入',
	usr_select_upload_head : '請選擇所要上傳的頭像',
	usr_change_psd : '修改密碼',
	usr_old_password : '舊密碼',
	usr_new_password : '新密碼',
	usr_password : '密碼',
	usr_confirm_password : '確認密碼',
	usr_required : '為必填',
	usr_check_password : '新密碼和確認密碼不一致',
	usr_is_not_null : '不能為空',
	usr_password_size : '請輸入密碼長度 min - max',
	usr_password_check : '密碼必須同時包含英文字母和數字',
	usr_old_password_error : '舊密碼錯誤',
	usr_set_all_see : '所有人可見',
	usr_set_my_attention_see : '只有我關注中的人可見 ',
	usr_set_all_not_see : '只有我自己可見',
	usr_privacy_set : '私隱設定',
	usr_set_who_browse : '<p class="fontfamily">空間訊息</p>',
	usr_set_tip : '誰可查閱我的 …',
	usr_gender_M : '男',
	usr_gender_F : '女',
	usr_gender_null : '未指定',
	usr_id_error : '格式為英文/數字/下劃線/橫線的組合且長度',
	usr_supervise : '直屬上司',
	usr_password_check_error : '密碼需要純英文字母、數字、 下劃線(_)、橫線(-), 必須同時包含英文字母和數字 ',
	// 课程详情
	detail_nav_timetable : '日程表',
	detail_online_content : '網上內容',
	detail_class_detail : '班級詳情',
	detail_class_notice : '班級通告',
	detail_my_status : '我的狀態',
	detail_course_detail : '課程詳情',
	detail_course_notice : '課程通告',
    detail_course_cpdHours : 'CPT/D時數',
    detail_cpdHours_information: 'CPT/D信息',
    detail_cpdHours_end_time: '獲取CPT/D時數截止日期',
    detail_cpdHours_unlimied: '不限',
    detail_award_hours: '可獲得時數',
    detail_core_hours: '核心時數',
    detail_non_core_hours: '非核心時數',
	detail_class_list : '班級列表',
	detail_study_status : '學習狀態',
	detail_exam_status : '考試狀態',
	detail_app_status : '報名狀態',
	detail_comment_title : '滿分為5分,請為以下項打分',
	detail_last_attent : '上次參與',
	detail_best_score : '最佳分數',
	detail_need_completed_course : '必須完成以下 $data 門的課程/測試',
	detail_no_message : '暫無',
	detail_comment_not_empty : '內容不能為空！',
	detail_comment_count : '評論（共$data條）',
	detail_comment_to : '回覆',
	detail_count_project : '計分項目',
	detail_count_project_desc : '(可以計算分數的在線/離線學習内容)',
	detail_sum_score : '總分',
	detail_pass_score : '合格分数',
	detail_my_score : '我的分數',
	detail_need_to : '要求',
	detail_complate_condition : '完成條件',
	detail_access_condition : '參與要求',
	detail_complate_desc : '為已完成此項要求',
	detail_score_condition : '分數要求',
	detail_attendance_condition : '出席率要求',
	detail_my_attendance : '我的出席率',
	detail_valuation : ' 評 論',
	detail_teacher_valuation : '講師評分',
	detail_contri_rate : '比重',
	detail_note : '筆記',
	// 课程模块
	module_review : '回顧',
	module_study_report : '測驗報告',
	module_exam_report : '考試報告',
	module_timelimit : '限時',
	module_max_score : '滿分',
	module_percent_of_pass : '合格率',
	module_submit_job : '提交作業',
	module_detail : '詳細訊息',
	module_prep_requeid : '先修條件：您必須先學習完 \' $data \'，才能學習该單元。',
	module_prep_requeid_attempted : '先修條件：您必須先查閱 \' $data \'，才能學習该單元。',
	module_prep_requeid_completed : '先修條件：您必須先完成 \' $data \'，才能學習该單元。',
	module_prep_requeid_viewed : '先修條件：您必須先查閱 \' $data \'，才能學習该單元。',
	module_prep_requeid_submitted : '先修條件：您必須先提交 \' $data \'，才能學習该單元。',
	module_prep_requeid_passed : '先修條件：您必須先完成 \' $data \'，才能學習该單元。',
	module_need_preview : '需查閱',
	module_need_passed : '需合格',
	module_need_pass : '需完成',
	module_need_attent : '需參與',
	module_need_submit : '需提交',
	module_content_duration : '內容時長',
	module_full_score : '滿分',
	module_passing_score : '合格分數',
	module_max_test_count : '測試次數限額',
	module_save_success : '保存成功',
	module_attainment : '成績',
	module_unlimited : '無限制',
	module_need_attempt : '已嘗試',
	// 日程表
	lesson_title : '環節',
	lesson_instructor : '講師',
	lesson_duration : '時間',
	lesson_address : '地點',
	// 评分
	comment_please : '請您評論',
	comment_max_score : '滿分5分',
	commnet_avg_score : '平均分',
	comment_style : '授課風格',
	comment_quality : '教學質量',
	comment_structure : '內容結構',
	comment_interaction : '交流互動',
	comment_prompt : '至少评分一颗星',
	comment_had_submit_score : '你已提交評分了',
	// 按钮
	btn_enrollment : '報名',
	btn_recommend_under : '推薦給下屬',
	btn_download : '下載',
	btn_submit : '提交',
	btn_detail : '班級資料',
	button_ok : '確定',
	btn_post : '發表',
	btn_cancel : '取消',
	button_cancel : '取消',
	button_delete : '刪除',
	button_approval : '批准',
	button_refuse : '拒絕',
	button_add : '添加',
	button_select : '選擇',
	button_search : '搜索',
	button_see : '查看',
	button_save : '儲存',
	button_add_all : '添加全部',
	button_close : '關閉',
	button_back : '返回',
	button_exit : '離開',
	global_attachment : '附件',
	// 讲师
	instructor_name : '講師名稱',
	// sns
	sns_like : '讚',
	sns_share : '分享',
	sns_comment : '評論',
	sns_liked : '已贊',
	sns_shared : '已分享',
	sns_collected : '已收藏',
	sns_lowercase_comment : '評論',
	sns_label_comment : '評論',
	sns_collect : '收藏',
	sns_del_like : '取消讚',
	sns_del_collect : '取消收藏',
	sns_click_like : '點讚',
	sns_click_collect : '點收藏',
	sns_click_share : '點分享',
	sns_notice_already_like : '你已經讚過啦',
	sns_notice_already_collect : '你已經收藏過啦',
	sns_notice_already_share : '你已經分享過啦',
	sns_notice_share_success : '已成功分享！',
	sns_say_something : '說點什麼吧',
	// 问答
	know_catalog : '分類',
	know_catalog_desc : '*如果您的問題無法歸入任何子分類，您可以只選擇一級分類',
	know_catalog_desc_true : '請選擇正確的分類，以使您的問題盡快得到解答',
	know_child_catalog : '子分類',
	know_title : '標題',
	know_ask_num : '回答數',
	know_time : '提問時間',
	know_status : '狀態',
	know_SOLVED : '已解決',
	know_UNSOLVED : '待解決',
	know_POPULAR : '精選',
	know_FAQ : 'FAQ',
	know_question : '問題',
	know_question_content : '提問',
	know_put_question : '提問',
	know_question_categories : '提問分類',
	know_put_question_head : '問題',
	know_put_question_desc : '搜索里找不到嗎？請點此發問吧！',
	know_my_put_question_desc : '問題說明越詳細，回答也會越準確，限200個中文字（400個字符）',
	know_answer : '回答',
	know_answer_desc : '分享你所知道的，幫助你的同事！顯示你對事物的了解，成為領域專家',
	know_explore : '發掘',
	know_explore_desc : '所搜知識！創建在線企業知識庫！',
	know_no : '沒有',
	know_list : '列表',
	know_my : '我的',
	know_no_select : '不選',
	know_my_want : '我要提問',
	know_question_supplement : '問題補充說明',
	know_question_supplement_desc : '限1000個中文字（2000個字符）',
	know_help : '幫助',
	know_help_01_title : '如何使用在線問答',
	know_help_01_desc : '您可以在此分享您所知道的，也可以了解您所不知道的。您可以回答問題幫助他人，也可以突出問題求助他人回答，或者搜索自己關心的問題。當問題解決后，您還可以通過投票來發表對問題的評價。趕快行動吧！',
	know_help_02_desc_1 : '所謂精選問題，顧名思義，即通過用戶投票方式挑選出來的一部分經典問題。',
	know_help_02_desc_2 : '當問題狀態變為',
	know_help_02_desc_3 : '后，在問題的最佳答案下方會出現',
	know_help_02_desc_4 : '，在30天內，學員可根據自己感受選擇',
	know_help_02_desc_5 : '對問題進行評價。',
	know_help_02_desc_6 : '系統根據每個評選週期（7天一次）的臨時投票結果中，投"好"與"不好"的票數差{即投（好）減去投（不好）來選取精選問題。',
	know_help_wdicon : '<div class="wzb-help-icon"><span class="skin-bg">已解決問題</span><em>出现</em><span class="skin-bg">投票区</span><em>选择</em><span class="skin-bg">好 / 不好</span></div>',
	know_good : '好',
	know_or : '或',
	know_no_good : '不好',
	know_vote_detail : '投票區',
	know_del_answer : '刪除該回答',
	know_ask_user : '回答者',
	know_ask_time : '回答時間',
	know_que_user : '提問者',
	know_que_time : '提問時間',
	know_relevant : '相關回答',
	know_relevant_question : '相關問題',
	know_publish_information : '發表訊息',
	know_publish_information_desc : '請針對問題做詳細的回答。限10000 個字元以內',
	know_reference_material : '參考資料',
	know_reference_material_desc : '如果您的回答是從其他地方引用，請表明出處。限400 個中文字（800個字元）以內。',
	know_open : '展開',
	know_collect : '收起',
	know_praise : '點讚',
	know_not_available : '暫無回答',
	know_not_relavant_request : '暫無相關問題',
	know_submit : '提交',
	know_myask_not_null : '我的問題不能為空且限200個中文字（400個字元）以內',
	know_ask_desc_not_null : '問題補充說明不能超過1000個中文字（2000個字元）',
	know_answer_content_not_null : '發佈訊息不能為空',
	know_answer_content_max_tips : '發佈訊息不能超過1000個中文字（2000個字元）',
	know_reference_material_error : '參考資料不能超過400個中文字（800個字元）',
	know_del_question : '你確定取消該提問？',
	know_supplement_desc_1 : '請輸入補充的問題。限400個中文字（800個字元）以內',
	know_supplement_desc_2 : '問題說明越詳細，回答也會越準確。',
	know_supplement : '補充',
	know_requestion_supplement : '問題補充',
	know_best_answer : '最佳回答',
	know_answer_good_ind : '您覺得最佳答案好不好？',
	know_set_best_answer : '設為最佳答案',
	know_set_best_answer_confirm : '確定把該回答設為最佳答案？',
	know_del_answer_confirm : '確定刪除該條回答嗎?',
	know_currently : '目前有',
	know_personal_evaluation : '個人評價',
	know_faq_answer : '答案',
	know_not_repeat_evaluation : '對不起！您不能重複評價',
	know_all_catalog : '全部分類',
	know_change_catalog : '改變分類',
	know_confirm_delete : '確認刪除該問題？',
	know_class_catalog : '分類目錄',
	know_select_catalog_error : '請選擇問題分類',
	know_bounty : '懸賞積分',
	know_inputBouty_tip1 : '當選擇最佳答案後，會從您的積分中扣除懸賞積分',
	know_inputBouty_tip2 : '懸賞積分不可大於你的積分',
	know_inputBouty_tip3 : '請輸入正整數',
	know_inputBouty_tip4 : '您當前的積分',
	know_teacher_score : '评分',
	know_inputBouty_tip5 : '不能為負數或非數字',
/*	know_teacher_J : '初级讲师',
	know_teacher_M : '中级讲师',
	know_teacher_S : '高级讲师',
	know_teacher_D : '特聘讲师',*/
	// 个人
	personal_my_group : '群組',
	personal_my_attention : '關注',
	personal_my_fans : '愛好者',
	personal_my_praise : '讚',
	personal_my_dynamic : '動態',
	personal_my_files : '檔案',
	personal_my_collection : '收藏',
	personal_my_credit : '積分',
	personal_my_learning_record : '學習記錄',
	personal_my_learning_situation : '學習概況',
	personal_ta_group : '群組',
	personal_ta_attention : '關注',
	personal_ta_fans : '粉絲',
	personal_ta_praise : '被讚',
	personal_ta_sendmesg : '給TA發站內信',
	personal_ta_dynamic : '動態',
	personal_ta_files : '檔案',
	personal_ta_collection : '收藏',
	personal_ta_credit : '積分',
	personal_ta_learning_record : '學習記錄',
	personal_ta_learning_situation : '學習概況',
	personal_fans : '愛好者',
	personal_attention : '關注',
	personal_find : '可能認識的人',
	personal_situation : '概況',
	personal_pandect : '總覽',
	personal_total_learn_duration : '學習總時長',
	personal_total_duration : '總時長',
	personal_total_credit : '總學分',
	personal_deadline : '截止日期',
	personal_deadline_notes : '(以下數據每日更新一次)',
	personal_course : '課程',
	personal_course_study : '課程學習',
	personal_total_courses : '課程總數',
	personal_total_exams : '考試總數',
	personal_be_praised : '被讚',
	personal_praise_others : '讚他人',
	personal_group_speech : '群組發言',
	personal_know : '問答',
	personal_question_number : '提問次數',
	personal_feedback_number : '回答次數',
	personal_evaluation : '調查問卷',
	personal_evaluation_list : '調查問卷列表',
	personal_evaluation_wait_anwser : '待回答',
	personal_evaluation_anwsered : '已回答',
	personal_submit_time : '提交時間',
	personal_submit_detail : '提交情況',
	personal_no_submit : '未提交',
	personal_submit_ok : '已提交',
	// 关注
	attention_mutual : '互相關注',
	attention_now : '已關注',
	attention_find : '找人',
	attention_find_desc : '用戶名/部門/職級',
	// 收藏
	collect_catalog : '目錄',
	collect_course : '收藏了',
	collect_article : '收藏了資訊',
	collect_courses : '收藏課程',
	collect_articles : '收藏資訊',
	collect_knowledge : '收藏知識',
	collect_confirm_delete : '是否確定刪除該收藏',
	collect_catalog_null : '未指定',
	collect_article_type : '資訊類型',
	collect_ok : '收藏成功',
	//赞
	valuation_list : '贊列表',
	// 积分
	credits_ZD_INIT : '用戶首次登錄',
	credits_SYS_NORMAL_LOGIN : '用戶非首次登錄',
	credits_SYS_UPD_MY_PROFILE : '更改個人資料',
	credits_SYS_SUBMIT_SVY : '參加公共調查問卷',
	credits_SYS_INS_TOPIC : '論壇發帖得分',
	credits_SYS_INS_MSG : '論壇回帖得分',
	credits_SYS_MSG_UPLOAD_RES : '論壇共享資料得分',
	credits_ZD_NEW_QUE : '在線提問得分',
	credits_ZD_COMMIT_ANS : '在線回答得分',
	credits_ZD_RIGHT_ANS : '回答被採納為最佳答案得分',
	credits_ZD_CANCEL_QUE : '取消提問',
	credits_ITM_ENROLLED : '成功報讀培訓',
	credits_ITM_SCORE_PAST_60 : '成績達到60分或以上',
	credits_ITM_SCORE_PAST_70 : '成績達到70分或以上',
	credits_ITM_SCORE_PAST_80 : '成績達到80分或以上',
	credits_ITM_SCORE_PAST_90 : '成績達到90分或以上',
	credits_ITM_PAST_TEST : '測驗已通過',
	credits_ITM_SUBMIT_ASS : '作業已提交',
	credits_ITM_SUBMIT_SVY : '調查問卷已提交',
	credits_ITM_VIEW_RDG : '教材已瀏覽',
	credits_ITM_VIEW_COURSEWARE : '\"課件\"已瀏覽',
	credits_ITM_PAST_COURSEWARE : '\"課件\"已完成',
	credits_ITM_VIEW_REF : '參考已瀏覽',
	credits_ITM_VIEW_VOD : '視頻點播已瀏覽',
	credits_ITM_VIEW_FAQ : '答疑欄已參與',
	credits_ITM_INS_TOPIC : '培訓論壇發帖得分',
	credits_ITM_INS_MSG : '論壇回帖得分',
	credits_ITM_MSG_UPLOAD_RES : '論壇共享資料得分',
	credits_ITM_IMPORT_CREDIT : '手工設置課程積分',
	credits_SYS_CREATE_GROUP : '創建群組',
	credits_SYS_JION_GROUP : '參與群組',
	credits_SYS_GET_LIKE : '被點讚',
	credits_SYS_COS_COMMENT : '參與課程評論',
	credits_SYS_CLICK_LIKE : '點讚',
	credits_SYS_ANWSER_BOUNTY : '回答問題(悬赏)',
	credits_SYS_QUESTION_BOUNTY : '提問(悬赏)',
	credits_KB_SHARE_KNOWLEDGE : '知識分享',
	credits_INTEGRAL_EMPTY : '積分清空',
	credits_EMPTY : '清空',
	credits_name : '積分名稱',
	credits_source : '來源',
	credits_point : '積分',
	credits_operate_type : '操作類型',
	credits_zd_ind : '手動/自動',
	credits_type : '積分類型',
	credits_time : '時間',
	credits_no_source : '未有',
	credits_add : '加分',
	credits_deduction : '扣分',
	credits_automatic : '自動',
	credits_manual : '手動',
	credits_activity : '活動積分',
	credits_train : '培訓積分',
	credits_information : '積分訊息',
	credits_records : '積分記錄',
	credits_API_UPDATE_CREDITS : '積分消費',
	// 群组
	group_member : '成員',
	group_message : '訊息',
	group_find : '私人群組',
	group_list : '群組列表',
	group_my : '我的群組',
	group_openMenu : '公開群組',
	group_create : '新建群組',
	group_name : '群組名稱',
	group_scale : '群組規模',
	group_manager : '管理員',
	group_create_time : '創建日期',
	group_operate : '操作',
	group_join : '申請加入',
	group_name_not_null : '群組名稱不能為空',
	group_desc : '群組簡介',
	group_privacy : '群組隱私',
	group_card : '群組名片',
	group_public : '所有人可申請',
	group_not_public : '不開放申請',
	group_open : '無需申請既可以加入',
	group_apply_ok : '申請加入，請等待審批',
	group_apply_error : '不能重複申請',
	group_add_error : '群組名稱已存在，請重新輸入',
	group_detail : '群組詳情',
	group_members : '群組成員',
	group_member_approve : '成員審批',
	group_information_set : '訊息設置',
	group_apply_date : '申請日期',
	group_student_name : '學員名稱',
	group_approve_status : '審批狀態',
	group_approve_date : '審核日期',
	group_approve_status_0 : '待審批',
	group_approve_status_1 : '已通過',
	group_approve_status_3 : '已拒絕',
	group_approve_pending : '等待中的審批',
	group_approve_admitted : '已通過的審批',
	group_approve_rejected : '已拒絕的審批',
	group_add_member : '添加成員',
	group_add_member_error : '添加失敗',
	group_add_ok : '已添加',
	group_assignment : '轉讓管理員',
	group_assignment_error : '轉讓失敗',
	group_approve_ing : '審批中',
	group_cancel_app : '取消申請',
	group_dissolve : '解散群組',
	group_confirm_dissolve : '確認解散該群組',
	group_dissolve_error : '你沒有足夠的權限解散該群組',
	group_sign_out : '退出群組',
	group_confirm_sign_out : '確認退出該群組',
	group_confirm_cancel_app : '確認取消該群組的申請',
	group_say_words : '發表訊息',
	group_speech : '群組言論',
	group_lowercase_speech :'群組言論',
	group_dissolved : '(已解散)',
	group_wait_app : "待審批人數",
	group_all_users : '顯示所有用戶',
	group_show_instr_only : '僅顯示講師',
	// 公开课
	open_back_list : '返回列表',
	open_ad_title : '職場精英成長之路',
	open_course : '影片',
	open_catalog : '目錄',
	open_sub_catalog : '子目錄',
	open_the_hottest : '最熱',
	open_the_new : '最新',
	open_popularity : '人氣',
	open_own_kinds : '所屬分類',
	// 下属
	subordinate_list : '下屬列表',
	subordinate_report : '查看報表',
	subordinate_approval : '審批報名',
	subordinate_all : '所有下屬',
	subordinate_direct : '直屬下屬',
	subordinate_group : '下屬部門',
	subordinate_filter_result : '篩選結果',
	subordinate_custom_report : '自定義報表',
	subordinate_saved_report : '已保存報表',
	subordinate_user_or_group : '用戶名/用戶組',
	subordinate_my_all : '我的所有下屬',
	subordinate_my_direct : '我的直屬下屬',
	subordinate_appoint : '指定學員',
	subordinate_train : '培訓',
	subordinate_all_train : '所有培訓',
	subordinate_some_train : '僅這些培訓',
	subordinate_catalog_train : '僅這些目錄下的培訓',
	subordinate_registration_date : '報名日期',
	subordinate_training_status : '結訓狀態',
	subordinate_desc : '說明',
	subordinate_required : '為必填項',
	subordinate_update_time : '修改時間',
	subordinate_del_error : '刪除錯誤',
	subordinate_pending : '等待審批',
	subordinate_approved : '已審批',
	subordinate_train_name : '課程',
	subordinate_apply_time : '申請時間',
	subordinate_approval_process : '審批歷程',
	subordinate_complete_approval : '完成審批後',
	subordinate_APPROVED : '批准',
	subordinate_APPROVED2 : '批准',
	subordinate_ENROLLED : '批准',
	subordinate_CANCELLED : '不批准',
	subordinate_DISAPPROVED : '不批准',
	subordinate_DISAPPROVED2 : '不批准',
	subordinate_approval_reason : '請輸入審批理由，最多輸入200個字訊息',
	subordinate_next_approver : '下一審批者',
	subordinate_please_select : '請選擇審批者',
	subordinate_select_user : '選人',
	subordinate_send_message : '發送消息',
	subordinate_send_remind_message : '發送提醒訊息',
	subordinate_message_content_desc : '最多輸入200個字訊息',
	subordinate_clear_all : '清空所有',
	subordinate_select_user_name : '所選人名',
	subordinate_please_select_user : '請選擇所以發送提醒訊息的用戶',
	subordinate_send_ok : '發送成功',
	subordinate_del_report_confirm : '你確定刪除該報表模板？',
	subordinate_user_group_leader : '用戶組領導',
	subordinate_save_report_template : '保存報告模板',
	subordinate_save_report_desc : '你可以將報表選項儲存起來，方便日後快速以同一組選項來查看報表。',
	subordinate_input_title : '請輸入消息標題',
	subordinate_input_content : '請輸入消息內容',
	subordinate_approval_operation : '審批',
	subordinate_not_pass : '審批未通過',
	subordinate_send : '發送',
	//审批下属报名课程
    subordinate_app_waiting : '等待審批',
    subordinate_app_not_approved : '審批未通過',
	// 訊息
	message_SYS : '系統訊息',
	message_PERSON : '站內信',
	message_From : '來源',
	message_Pri_Frm : '',
	message_suffix : '的站內信',
	// 错误
	error_login_fail : '用戶名或密碼錯誤，請重新輸入。',
	error_lab_login_fail_08 : '系統目前十分繁忙，請稍後再試。',
	error_trial_limit1 : '一個帳號如果連續',
	error_trial_limit2 : '次密碼錯誤，會被馬上凍結。',
	error_trial_login_limit : '用戶名或密碼錯誤，請重新輸入。一個帳號如果連續6次密碼錯誤，會被馬上凍結。',
	error_account_suspended : '用戶帳號已被凍結，請聯繫系統管理員。',
	error_over_validity_period : '該帳號已經過期',
	error_please_input : '請輸入',
	error_please_select : '請選擇',
	error_over_length : '長度為',
	error_user_name_exist : '用戶名已存在',
	error_pwd_inconsistent : '兩次密碼不一致',
	error_delete_fail : '刪除失敗',
	error_exceed_max_usr : '註冊用戶數超過了授權合約的規定，請聯繫系統管理員。',
	error_user_system_issue : '您的帳戶因系統問題已被鎖定，請與系統管理員聯繫。',
	// 登录
	login : '登入',
	login_forget_password : '忘記密碼？',
	login_new_user_register : '新用戶註冊',
	login_contact_us : '聯繫我們',
	login_attention_us : '關注我們',
	login_app_download : 'APP下載',
	login_remember : '記住用戶名',
	login_about_cyberwisdom : '關於匯思',
	login_register_attention_matters : '請填寫用戶資訊 ，管理員會盡快審批並聯繫你。"<span style="margin-right:0px;" class="wzb-form-star">*</span>"為必填內容。',
	login_haved_account : '已有平台帳號，馬上',
	login_register_submited : '您的註冊已成功提交！',
	login_waiting_approval : '注：您需要在管理員審核通過后方可登陸。',
	login_find_password : '找回密碼',
	login_email_find : '郵箱找回',
	login_find_pwd_desc : '系統會以郵件形式將密碼發送到您的註冊郵箱裏',
	login_register_email : '註冊郵箱',
	login_register_email_error : '請輸入註冊郵箱',
	login_find_error : '帳號不存在或與電子郵件不匹配',
	login_pwd_email_send : '密碼已經經由電子郵件發送，請檢查。',
	login_reset_pwd : '重置密碼',
	login_connect_fail : '找回密碼連接已失效！',
	login_input_error : '你的輸入有誤，請重新輸入。',
	login_input_error_three : '你輸入錯誤已經滿3次，本次修改密碼失敗。',
	login_update_pwd_ok : '密碼修改成功。',
	login_update_pwd_error : '密码修改失败，旧密码输入有误。',
	login_welcome : '歡迎您，請登錄',
	contact_us_city_hk : '香港',
	contact_us_city_bj : '北京',
	contact_us_city_gz : '廣州',
	contact_us_city_sz : '深圳',
	contact_us_city_sh : '上海',
	contact_us_adress_hk : '<p>地址：香港九龍青山道660號百生利中心4樓B室</p><p>電郵：sales_hk@cyberwisdom.net</p><p>電話：(852) 2581 0300</p>',
	contact_us_adress_bj : '<p>地址：北京市朝陽區東大橋路甲8號尚都國際中心A座2211室</p><p>電郵：sales_bj@cyberwisdom.net</p><p>電話：(86 10) 58702040、58703299</p>',
	contact_us_adress_gz : '<p>地址：廣州市天河區天河路490號壬豐大廈17樓02房 郵編：510620</p><p>電郵：sales_gz@cyberwisdom.net</p><p>電話：(86 20)3888 6860;3888 9596</p>',
	contact_us_adress_sz : '<p>地址：深圳市羅湖區人民南路2008號嘉里中心大廈1120室 郵編：518001</p><p>電郵：sales_sz@cyberwisdom.net</p><p>電話：(86 755)8214 5110</p>',
	contact_us_adress_sh : '<p>地址：上海市長寧區定西路788號凱陽大厦7樓D座 郵編：200050</p><p>電郵：sales_sh@cyberwisdom.net</p><p>電話：(86 21)6167 9301</p>',
	login_web_hk : '香港官網',
	login_web_ch : '大陸官網',
	contact_us_weixin_cn : '匯思官網（大陸）',
	contact_us_weixin_hk : '匯思官網（香港）',
	contact_us_weixin_cp : 'LT企業學習智囊團</br>(微信公眾號)',
	about_cyberwisdom_1 : '匯思于1999年成立於香港，在北京、上海、廣州和深圳均設有分支機構。作為亞太地區領先與大中華地區規模最大的人才發展整體解決方案的提供商，匯思擁有一支超過300人的專業團隊，憑借累積的豐富的e-Learning行業經驗，一直致力於為大中華及整個亞太地區的企業、政府和教育機構提供全方位及最領先的學習方案。',
	about_cyberwisdom_2 : '作為真正的e-Learning解決方案供貨商，匯思不僅提供平台技術，並且擁有亞太地區最龐大的定制課程設計與開發團隊、超過800門自主版權的現成通用課件、企業大學2.0咨詢、遊戲化學習(Serious Game)、學習支持與推廣服務等。',
	app_title : 'APP客户端下載',
	app_download : '馬上下載',
	app_android : 'Android版下載',
	app_android_version : '版本V3.6 大小	17.6M',
	app_android_desc : '支持Android 4.3及以上版本',
	app_ios : 'iPhone/ipad版下載',
	app_ios_version : '版本V3.6 大小	34.5M',
	app_ios_desc : '支持IOS 7.0及以上版本',

	// 证书
	certificate_list : "證書列表",
	certificate_title : "證書標題",
	certificate_period_of_validity : "有效期至",
	certificate : '證書',
	certificate_view_need_text : '只有完成狀態下，才能查看證書。',
	certificate_download_error : '已過期',
	// 搜索页面
	search_text : '搜索',
	search_result : '搜索結果',
	search_type_all:'全部',
	search_type_course : '課程',
	search_type_exam : '考試',
	search_type_open : '影片',
	search_type_answer : '問答',
	search_type_article : '資訊',
	search_type_message : '通告',
	search_type_group : '群組',
	search_type_contacts : '人脈',
	search_result_notice : '共找到 $data 項記錄',
	search_type_nav : '搜索課程、資訊......',
	search_type_question : '搜索問題',
	search_type_qunzu : '搜索群組',
	search_type_knowledge : '知識',
	// 类型
	type_TST : '靜態測驗',
	type_DXT : '動態測驗',
	type_ASS : '作業',
	type_RDG : '教材',
	type_AICC_AU : 'AICC課件',
	type_REF : '參考',
	type_VOD : '視頻點播',
	type_TEST : '測驗',
	type_SCO : 'SCORM課件',
	type_SVY : '課程評估問卷',
	type_FOR : '論壇',
	type_FAQ : '答疑欄',
	type_GLO : '詞彙表',
	type_CSVY : '課程評估問卷',
	// 首页
	index_last_login_date : '上次登錄',
	public_evaluation : '公共調查問卷',
	index_on_reader : '學習中課程',
	index_required_course : '必修課程',
	index_last_access : '最近訪問',
	index_click_to_study : '點擊學習',
	index_click_to_exam : '進入考試',
	index_hot_course : '最熱課程',
	// 评论
	upload_img : '上傳圖片',
	online_img : '在線圖片',
	upload_doc : '上傳文檔',
	upload_video : '上傳視頻',
	online_video : '在線視頻',
	publish_doing : '發表動態',
	upload_tip_image : '圖片大小須在50MB以內, 支持文件格式: jpg, gif, png',
	upload_tip_doc : '文件大小須在50MB以內',
	upload_tip_vedio : '視頻大小須在50MB以內, 支持文件格式: mp4',
	// 我的学习地图
	map_my_map : '學習日程表',
	map_month_task : '本月任務',
	map_year_task : '本年任務',
	map_task_name : '任務名稱',
	map_week_en : '',
	map_week_cn : '週',
	map_month_1 : '1月',
	map_month_2 : '2月',
	map_month_3 : '3月',
	map_month_4 : '4月',
	map_month_5 : '5月',
	map_month_6 : '6月',
	map_month_7 : '7月',
	map_month_8 : '8月',
	map_month_9 : '9月',
	map_month_10 : '10月',
	map_month_11 : '11月',
	map_month_12 : '12月',

	// 试题
	test_mc_and : '多項選擇題',
	test_mc_single : '單項選擇題',
	test_fb : '填充題',
	test_mt : '配對題',
	test_tf : '判斷題',
	test_es : '問答題',
	test_dsc : '動態情景題',
	test_fsc : '靜態情景題',
	test_topic_desc : '題，共',

	//标签
	tag_empty : '當前無任何可管理的標籤',
	tag_title : '標籤名稱',
	button_update : '修改',

	//知识库
	lab_kb_title : '標題',
	lab_kb_image : '圖示',
	lab_kb_desc : '簡介',
	lab_kb_content : '內容',
	lab_kb_status : '狀態',
	lab_kb_opera : '操作',
	lab_kb_update : '修改',
	lab_kb_delete : '刪除',
	lab_kb_confirm : '確認刪除該記錄嗎？',
	lab_kb_type : '類型',
	lab_STATUS_ALL : '全部',
	lab_STATUS_ON : '已發佈',
	lab_STATUS_OFF : '未發佈',
	lab_kb_app_status : '審批狀態',
	lab_APP_STATUS_PENDING : '待審批',
	lab_APP_STATUS_APPROVED : '已審批',
	lab_APP_STATUS_REAPPROVAL : '已拒絕',
	lab_create_datetime : '創建日期',
	lab_TYPE_ALL : '全部',
	lab_TYPE_ARTICLE : '文章',
	lab_TYPE_DOCUMENT : '文檔',
	lab_TYPE_VEDIO : '視頻',
	lab_TYPE_IMAGE : '圖片',
	lab_kb_my_share : '我的分享',
	lab_kb_new_browse : '最新瀏覽',
	lab_kb_know_catalog : '知識分類',
	lab_kb_know_tag : '知識標籤',
	lab_kb_know_list : '知識列表',
	lab_kb_praise : '贊',
	lab_kb_create_time : '發佈日期',
	lab_kb_share_man : '分享者',
	lab_kb_want_share : '我要分享',
	lab_kb_search : '搜索',
	lab_kb_prompt : '請輸入標題搜索',
	lab_kb_catalog : '知識目錄',
	lab_kb_menu_knowledge : '知識庫',
	lab_kb_menu_catalog : '目錄管理',
	lab_kb_menu_tag : '標籤管理',
	lab_kb_menu_approval : '知識審批',
	lab_kb_menu_knowledge_manager : '知識管理',
	lab_kb_menu_knowledge_add_ARTICLE : '添加文章',
	lab_kb_menu_knowledge_add_DOCUMENT : '添加文檔',
	lab_kb_menu_knowledge_add_VEDIO : '添加視頻',
	lab_kb_menu_knowledge_add_IMAGE : '添加圖片',
	lab_kb_menu_knowledge_update_ARTICLE : '修改文章',
	lab_kb_menu_knowledge_update_DOCUMENT : '修改文檔',
	lab_kb_menu_knowledge_update_VEDIO : '修改視頻',
	lab_kb_menu_knowledge_update_IMAGE : '修改圖片',
	lab_kb_menu_catalog_add : '添加目錄',
	lab_kb_menu_catalog_update : '修改目錄',
	lab_kb_menu_tag_add : '添加標籤',
	lab_kb_menu_tag_update : '修改標籤',
	lab_kb_menu_tag_detail : '標籤內容',
	lab_kb_count_view : '瀏覽量',
	lab_kb_click_praise : '點贊',
	lab_kb_share_person : '分享人',
	lab_kb_share_time : '分享日期',
	lab_kb_app_person : '審批人',
	lab_kb_app_time : '審批日期',
	lab_kb_thr_approval : '通過',
	lab_kb_re_approval : '拒絕',
	lab_kb_knowledge_number : '知識數',
	lab_kb_item : '知識',
	lab_kb_center : '知識中心',
	lab_kb_DOWNLOAD_ALLOW : '允許',
	lab_kb_DOWNLOAD_INTERDICT : '禁止',
	lab_kb_DOWNLOAD : '下載',
	lab_tag_selection : '標籤選擇',
	lab_do_not_del_catalog : '無法刪除該知識目錄，已有知識關聯到該目錄',
	lab_tcr_select : '培訓中心選擇',
	lab_kb_from : '來自',
	lab_kb_view_lattest : '最近瀏覽',
	lab_kb_tag : '標籤',
	lab_share_ARTICLE : '分享文章',
	lab_share_DOCUMENT : '分享文檔',
	lab_share_VEDIO : '分享視頻',
	lab_share_IMAGE : '分享圖片',
	lab_default_images : '默認圖庫',
	lab_kb_submit : '確定',
	lab_kb_cancel : '取消',
	lab_kb_no_catalog : '沒有分類資料',
	lab_kb_no_tag : '沒有標籤資料',
	lab_kb_index_share : '我分享的',
	lab_kb_index_access : '我瀏覽的',
	lab_kb_credit_ranking : '當前積分排名',
	lab_kb_comfirm_del_tag : '此知識標籤已被使用，刪除會導致使用該標籤的知識內容消除此標籤標記，請確認是否刪除？',
	lab_kb_del_knowledge_by_ids : '批量刪除',
	lab_kb_del_knowledge_no_ids : '請選擇要刪除的知識',
	lab_kb_knowledge_not_publish : '知識未發佈或已刪除',
	lab_kb_prompt_title_desc_content : '請輸入標題、描述或內容',
	lab_kb_error : '請輸入必填項',
	lab_kb_title_error : '請輸入標題',
	lab_kb_desc_error : '請輸入簡介',
	lab_kb_length_error : '輸入的內容長度請不要超過255',
	lab_kb_img_error : '請選擇圖示',
	lab_kb_content_error : '請輸入內容',
	lab_kb_video_url_error : '請輸入合法的連結位址(只支援MP4格式的視頻檔。)',
	lab_kb_default_images : '從默認圖庫中選一張',
	lab_kb_publish : '發佈',
	lab_kb_online_video_url : '鏈接地址',
	lab_kb_image_error : '請上傳圖示',
	lab_eist_catalog : '該目錄名稱已經存在',
	lab_eist_tag : '該標籤名稱已經存在',
	lab_kb_unclassified : '未分類',
	lab_kb_unspecified : '未分類',
	lab_kb_Notice: '系統在處理檔案及其內容後有可能會出現內容失真或移位的情況。當你完成上載後，應該先預覽以確保處理完成後的內容能達到所需的效果。如果內容過於複雜(如Powerpoint)，建議於上載前自行轉換為 PDF格式。',

//职业发展计划
	lab_pfs_new : '入職',

	traning_center_all : '所有培訓中心',
	course_full_capacity : '報名人數已滿',
	course_app_time_tip : '報名期限已過',
	lab_ftn_USR_OWN_MAIN : '我的檔案',
	lab_ftn_USR_OWN_PREFER : '我的喜好',
	lab_ftn_APPR_APP_LIST : '報名審批',
	lab_ftn_ITM_MAIN : '培訓管理',
	lab_ftn_ITM_EXAM_MAIN : '考試管理',
	lab_ftn_USR_INFO_MAIN : '用戶管理',
	lab_ftn_EXT_INSTRUCTOR : '外部講師',
	lab_ftn_INT_INSTRUCTOR : '內部講師',
	lab_ftn_SYS_SETTING : '系統設置',
	lab_ftn_POSTER_MAIN : 'PC風格設定',
	lab_ftn_MOBILE_POSTER_MAIN : '移動風格設定',
	lab_ftn_TC_MAIN : '培訓中心管理',
	lab_mod : '課程評估問卷',
	lab_supplier : '供應商',
 
	lab_article_main: '資訊管理',
	lab_assignment_correction : '作業批改',
	lab_exam_correction : '試卷批改',
	lab_knowledge_approve : '知識審批',
 

	
	
	//計劃管理
	lab_ftn_TRA_PLA_MGT : '計劃管理',
	lab_ftn_PLAN_CARRY_OUT : '計劃實施',
	lab_ftn_YEAR_PALN : '年度計劃',
	lab_ftn_MAKEUP_PLAN : '編外計劃',
	lab_ftn_YEAR_PLAN_APPR : '年度計劃審批',
	lab_ftn_MAKEUP_PLAN_APPR : '編外計劃審批',
	lab_ftn_YEAR_SETTING : '年度設置',

	//学习地图
 
	
	instr_main : '講師風采',
	create : '新增',
	COS:'課程',
	INTEGRATED:'項目式培訓',
	EXAM:'考試',
	REF:'公開課',
	
	group_instr_only:"僅顯示講師",
	
	lab_import_credit_info : '導入積分訊息',
	
	voting_not_result : '暫無投票結果',
	
	//bill 2016-04-19
	lab_group_online : '在線',
	lab_group_upload : '上傳',
	lab_group_online_addr : '在線文件路徑地址',
	
	//bill 2016-04-22
	lab_knowmenu_help: '向我求助',
	
	know_record_invalid : '記錄已失效',
	
	upload_tips_online : '請輸入在線文件路徑地址',
	
	sys_warning : '系統消息',
	sys_warning_user_msg : '系統正處於高用戶量狀態，操作速度會比較緩慢。',
	sys_warning_know : '我知道了',
	
	lab_passwd_length : '請使用英文字母/數字/底線/橫線的組合<br/>(必須同時包含英文字母和數字)<br/>長度不短過： $minLength <br/>長度不超過： $maxLength',
	
	lab_message_to_user : '發送給',
	
	lab_mt_file_type : '媒體檔案只支持上傳JPG, GIF, PNG格式的圖片',
	lab_mt_des : '圖片規格建議：寬80px，高80px',
	
	lab_file_not_download : '該文件不允許下載！',
	
	lab_course_spend_learning_desc : '你要完成本教材，你必須閱讀最少',
	lab_course_spend_vod_desc : '你要完成本視頻，你必須閱讀最少',
	lab_course_spend_reference_desc : '你要完成本參考，你必須閱讀最少',
	lab_course_spend_desc : '分鐘。',
	
	label_title_length_warn_80 : "不能超過40個中文字（80個字符）",
	label_title_length_warn_400 : "不能超過200個中文字（400個字符）",
	label_title_length_warn_20 : "不能超過10個中文字（20個字符）",

	label_lose_data : "暫無學習資源",
	//考试报告
	label_report_total_attempt_nbr_1 : '共有',
	label_report_total_attempt_nbr_2 : '次測驗報告',
	label_report_has_not_score_cnt : '試卷已提交，請等待評分',
	label_report_my_score : '我的分數',
	label_report_my_status : '我的狀態',
	label_report_tst_total_score : '試卷總分',
	label_report_tst_pass_score : '合格分數',
	label_report_history_max_score : '歷史最高分',
	label_report_my_answer_analysis : '我的答題分析',
	label_report_my_answer_questions : '答對題',
	label_report_my_wrong_answer : '答錯題',
	label_report_not_score_cnt : '尚未評分題',
	label_report_fb : '填空',
	label_report_true : '對',
	label_report_false : '錯',
	label_report_true_anwser : '正確答案',
	label_report_false_anwser: '你的答案錯誤X',
	label_report_ok_anwser : '恭喜你答對了',
	label_report_this_test_score : '本題得分',
	label_report_answer_prompt : '你沒有作答',
	label_report_your_answer : '你的答案',
	label_report_score : '分數',
	label_report_reference_answer : '參考答案',
	label_report_your_target_answer : '你配對的目標',
	label_report_true_target_answer : '正確的目標',
	label_report_test_report : '測驗報告',
	label_report_flunk_desc : '需要考試合格后，才能查看題目和答案',
	label_report_partially_correctt : '部分正確',
	label_report_not_score : '尚未評分',
	
	//直播
	label_live_list : '直播列表',
	label_live : '直播',
	label_live_all_live : '全部直播',
	label_live_living : '直播中',
	label_live_booking : '直播預告',
	label_live_lived : '往期直播',
	label_live_status : '直播狀態',
	label_live_looking : '馬上觀看',
	label_live_lv_booking : '未開始',
	label_live_over : '已結束',
	label_live_look_lived : '回顧直播',
	label_live_countdown : '倒計時',
	label_live_people : '人參與',
	
	//推荐给下属
	lab_app_conflict : '推薦成功',
	lab_app_conflict_info_l : "由於以下原因，",
	lab_app_conflict_info_r : "的報名存在衝突",
	lab_app_conflict_1 : "學員在同一門課程的以下班級中有學習記錄，課程不允許重讀。",
	lab_app_conflict_2 : "不是課程指定的目標學員。",
	lab_app_conflict_3 : "沒有通過課程的先修條件。",
	lab_app_conflict_5 : "日程表跟以下已報名課程的日程表有衝突。",
	lab_app_conflict_not_nominate : '沒有可推薦的下屬',
	
	label_cpd_outstanding_hours_report : 'CPT/D未完成時數報表',
    label_cpd_individual_hours_report : '個人 CPT/D 時數報表',
    
    label_pls_spc_a_valid : '請輸入一個正確的',
    
    label_voting_validate_tips : '請選擇投票選項',
    label_know_validate_select_only_one_tips : '只能選擇一個分類',
    
    lab_appn_ps_status_enrolled : '已報名',
    lab_appn_ps_status_pending_approval : '待審批',
    lab_appn_ps_status_waitlisted : '等待隊列',
    lab_appn_ps_status_not_approved : '審批未通過',
    lab_appn_ps_status_cancelled : '拒絕',
    
 };
text_label = $.extend(text_label, text_label_old);

//注：不要再住该文件中添加label, 后面会去掉该文件，所有Label请加到对应的功能模块中,详细请看以下设计文档
//http://svn.cyberwisdom.net.cn:8231/svn/wizbank/产品文档/产品开发、测试、设计、需求文档/技术设计文档/LMS_6_国际化设计文档.doc
