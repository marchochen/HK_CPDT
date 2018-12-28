<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:variable name="profile_attributes" select="//profile_attributes"/>
<xsl:variable name="cur_lang">
	<xsl:choose>
		<xsl:when test="$wb_lang= 'ch'">zh-hk</xsl:when>
		<xsl:when test="$wb_lang= 'gb'">zh-cn</xsl:when>
		<xsl:when test="$wb_lang= 'en'">en-us</xsl:when>
		<xsl:otherwise>en</xsl:otherwise>
	</xsl:choose>
</xsl:variable>
<!-- ======================================================================== -->
<!--
<xsl:variable name="lab_resigned">
	<xsl:choose>
		<xsl:when test="$wb_lang = 'ch'">已註銷</xsl:when>
		<xsl:when test="$wb_lang = 'gb'">已注销</xsl:when>
		<xsl:otherwise>Resigned</xsl:otherwise>
	</xsl:choose>
</xsl:variable>
-->
<xsl:variable name="lab_not_specified">
	<xsl:choose>
		<xsl:when test="$wb_lang = 'ch'">沒有指定</xsl:when>
		<xsl:when test="$wb_lang = 'gb'">未指定</xsl:when>
		<xsl:otherwise>Not specified</xsl:otherwise>
	</xsl:choose>
</xsl:variable>

<!-- usr id -->
<xsl:variable name="lab_login_id">
	<xsl:choose>
		<xsl:when test="$profile_attributes/user_id">
			<xsl:value-of select="$profile_attributes/user_id/label[@xml:lang = $cur_lang]"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:choose><xsl:when test="$wb_lang = 'ch'">(sys)用戶名稱</xsl:when><xsl:when test="$wb_lang = 'gb'">(sys)用户名称</xsl:when><xsl:otherwise>(sys)User name</xsl:otherwise></xsl:choose>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>

<xsl:variable name="lab_id_requirement">
	<xsl:choose><xsl:when test="$wb_lang = 'ch'">請使用英文字母/數字/底線/橫線的組合<br/>長度不短過: <xsl:value-of select="$profile_attributes/user_id/@min_length"/><br/>長度不超過: <xsl:value-of select="$profile_attributes/user_id/@max_length"/></xsl:when><xsl:when test="$wb_lang = 'gb'">请使用纯英文字母/数字/下划线/横线的组合。<br/>最小长度：<xsl:value-of select="$profile_attributes/user_id/@min_length"/><br/>最大长度：<xsl:value-of select="$profile_attributes/user_id/@max_length"/></xsl:when><xsl:otherwise>Please use alphabets, numbers, underscore or hyphen only.<br/>Minimum characters required: <xsl:value-of select="$profile_attributes/user_id/@min_length"/><br/>Maximum characters allowed: <xsl:value-of select="$profile_attributes/user_id/@max_length"/></xsl:otherwise></xsl:choose>
</xsl:variable>

<!-- usr_display_bil -->
<xsl:variable name="lab_dis_name">
	<xsl:choose>
		<xsl:when test="$profile_attributes/name">
			<xsl:value-of select="$profile_attributes/name/label[@xml:lang = $cur_lang]"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:choose><xsl:when test="$wb_lang = 'ch'">(sys)顯示名稱</xsl:when><xsl:when test="$wb_lang = 'gb'">(sys)昵称</xsl:when><xsl:otherwise>(sys)English name</xsl:otherwise></xsl:choose>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>

<!-- usr_ID -->
<xsl:variable name="lab_usr_id">
	<xsl:choose>
		<xsl:when test="$profile_attributes/user_id">
			<xsl:value-of select="$profile_attributes/user_id/label[@xml:lang = $cur_lang]"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:choose><xsl:when test="$wb_lang = 'ch'">(sys)用戶帳號</xsl:when><xsl:when test="$wb_lang = 'gb'">(sys)用户帐号</xsl:when><xsl:otherwise>(sys)User Id</xsl:otherwise></xsl:choose>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>


<!-- usr_pwd -->
<xsl:variable name="lab_passwd">
	<xsl:choose>
		<xsl:when test="$profile_attributes/password">
			<xsl:value-of select="$profile_attributes/password/label[@xml:lang = $cur_lang]"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:choose><xsl:when test="$wb_lang = 'ch'">(sys)密碼</xsl:when><xsl:when test="$wb_lang = 'gb'">(sys)密码</xsl:when><xsl:otherwise>(sys)Password</xsl:otherwise></xsl:choose>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>

<xsl:variable name="lab_passwd_length">
	<xsl:choose><xsl:when test="$wb_lang = 'ch'">請使用英文字母/數字/底線/橫線的組合<br/>(必須同時包含英文字母和數字)<br/>長度不短過: <xsl:value-of select="$profile_attributes/password/@min_length"/><br/>長度不超過: <xsl:value-of select="$profile_attributes/password/@max_length"/></xsl:when><xsl:when test="$wb_lang = 'gb'">请使用纯英文字母/数字/下划线/横线的组合<br/>(必须同时包含英文字母和数字)。<br/>最小长度：<xsl:value-of select="$profile_attributes/password/@min_length"/><br/>最大长度： <xsl:value-of select="$profile_attributes/password/@max_length"/></xsl:when><xsl:otherwise>Please use a combination of lower letters or uppercase letters / numbers / underline / hyphen<br/> (must contain letters and numbers)<br/>Minimum characters required: <xsl:value-of select="$profile_attributes/password/@min_length"/><br/>Maximum characters allowed: <xsl:value-of select="$profile_attributes/password/@max_length"/></xsl:otherwise></xsl:choose>
</xsl:variable>

<!-- usr_old_pwd -->
<xsl:variable name="lab_old_passwd">
 <xsl:choose><xsl:when test="$wb_lang = 'ch'">舊<xsl:value-of select="$lab_passwd"/></xsl:when><xsl:when test="$wb_lang = 'gb'">旧<xsl:value-of select="$lab_passwd"/></xsl:when><xsl:otherwise>Old password</xsl:otherwise></xsl:choose>
</xsl:variable>

<!-- usr_new_pwd -->
<xsl:variable name="lab_new_passwd">
 <xsl:choose><xsl:when test="$wb_lang = 'ch'">新<xsl:value-of select="$lab_passwd"/></xsl:when><xsl:when test="$wb_lang = 'gb'">新<xsl:value-of select="$lab_passwd"/></xsl:when><xsl:otherwise>New password</xsl:otherwise></xsl:choose>
</xsl:variable>

<!-- confirm_usr_pwd -->
<xsl:variable name="lab_con_passwd">
	<xsl:choose><xsl:when test="$wb_lang = 'ch'">確認<xsl:value-of select="$lab_passwd"/></xsl:when><xsl:when test="$wb_lang = 'gb'">确认<xsl:value-of select="$lab_passwd"/></xsl:when><xsl:otherwise>Confirm password</xsl:otherwise></xsl:choose>
</xsl:variable>

<!-- usr_pwd_need_change_ind -->
<xsl:variable name="lab_usr_pwd_need_change_desc">
	<xsl:choose><xsl:when test="$wb_lang = 'ch'">用戶必須於下一次登入時更改<xsl:value-of select="$lab_passwd"/></xsl:when><xsl:when test="$wb_lang = 'gb'">用户必须在下一次登录时修改<xsl:value-of select="$lab_passwd"/>。</xsl:when><xsl:otherwise>User must change <xsl:value-of select="$lab_passwd"/> at next login.</xsl:otherwise></xsl:choose>
</xsl:variable>

<!-- ent_syn_ind -->
<xsl:variable name="lab_delete_immune">
	<xsl:choose><xsl:when test="$wb_lang = 'ch'">防止因整合系統而被刪除</xsl:when><xsl:when test="$wb_lang = 'gb'">记录不被同步删除</xsl:when><xsl:otherwise>Deletion immuned</xsl:otherwise></xsl:choose>
</xsl:variable>

<!-- ent_syn_rol_ind -->
<xsl:variable name="lab_role_update_immuned">
	<xsl:choose><xsl:when test="$wb_lang = 'ch'">防止因整合系統而更新身份</xsl:when><xsl:when test="$wb_lang = 'gb'">身份不被同步更新</xsl:when><xsl:otherwise>Role update immuned</xsl:otherwise></xsl:choose>
</xsl:variable>

<xsl:variable name="lab_not_syn">
	<xsl:choose><xsl:when test="$wb_lang = 'ch'">防止因整合系統而更新</xsl:when><xsl:when test="$wb_lang = 'gb'">Don't update on system synchronization</xsl:when><xsl:otherwise>Don't update on system synchronization</xsl:otherwise></xsl:choose>
</xsl:variable>


<!-- usr_gender -->
<xsl:variable name="lab_gender">
	<xsl:choose>
		<xsl:when test="$profile_attributes/gender">
			<xsl:value-of select="$profile_attributes/gender/label[@xml:lang = $cur_lang]"/>
		</xsl:when>
		<xsl:otherwise>
	<xsl:choose><xsl:when test="$wb_lang = 'ch'">(sys)性別</xsl:when><xsl:when test="$wb_lang = 'gb'">(sys)性别</xsl:when><xsl:otherwise>(sys)Gender</xsl:otherwise></xsl:choose>
	</xsl:otherwise>
	</xsl:choose>
</xsl:variable>
<xsl:variable name="lab_gender_m">
	<xsl:choose><xsl:when test="$wb_lang = 'ch'">男</xsl:when><xsl:when test="$wb_lang = 'gb'">男</xsl:when><xsl:otherwise>Male</xsl:otherwise></xsl:choose>
</xsl:variable>
<xsl:variable name="lab_gender_f">
	<xsl:choose><xsl:when test="$wb_lang = 'ch'">女</xsl:when><xsl:when test="$wb_lang = 'gb'">女</xsl:when><xsl:otherwise>Female</xsl:otherwise></xsl:choose>
</xsl:variable>
<xsl:variable name="lab_gender_unspecified">
	<xsl:choose><xsl:when test="$wb_lang = 'ch'">未指定</xsl:when><xsl:when test="$wb_lang = 'gb'">未指定</xsl:when><xsl:otherwise>Unspecified</xsl:otherwise></xsl:choose>
</xsl:variable>

<!-- usr_job_title -->
<xsl:variable name="lab_job_title">
	<xsl:choose>
		<xsl:when test="$profile_attributes/job_title">
			<xsl:value-of select="$profile_attributes/job_title/label[@xml:lang = $cur_lang]"/>
		</xsl:when>
		<xsl:otherwise>
	<xsl:choose><xsl:when test="$wb_lang = 'ch'">(sys)工作日期</xsl:when><xsl:when test="$wb_lang = 'gb'">(sys)工作日期</xsl:when><xsl:otherwise>(sys)Job title</xsl:otherwise></xsl:choose>
	</xsl:otherwise>
	</xsl:choose>
</xsl:variable>
<!--usr_supervised_groups-->
<xsl:variable name="lab_supervised_groups">
	<xsl:choose>
		<xsl:when test="$profile_attributes/supervised_groups">
			<xsl:value-of select="$profile_attributes/supervised_groups/label[@xml:lang = $cur_lang]"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:choose><xsl:when test="$wb_lang = 'ch'">(sys)直屬用戶組</xsl:when><xsl:when test="$wb_lang = 'gb'">(sys)直属用户组</xsl:when><xsl:otherwise>(sys)Supervised groups</xsl:otherwise></xsl:choose>
	</xsl:otherwise>
	</xsl:choose>
</xsl:variable>

<xsl:variable name="lab_skillset">
	<xsl:choose>
		<xsl:when test="$profile_attributes/skillset">
			<xsl:value-of select="$profile_attributes/skillset/label[@xml:lang = $cur_lang]"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:choose><xsl:when test="$wb_lang = 'ch'">岗位</xsl:when><xsl:when test="$wb_lang = 'gb'">岗位</xsl:when><xsl:otherwise>skillset</xsl:otherwise></xsl:choose>
	</xsl:otherwise>
	</xsl:choose>
</xsl:variable>


<!-- usr_bday -->
<xsl:variable name="lab_bday">
	<xsl:choose>
		<xsl:when test="$profile_attributes/date_of_birth">
			<xsl:value-of select="$profile_attributes/date_of_birth/label[@xml:lang = $cur_lang]"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:choose><xsl:when test="$wb_lang = 'ch'">(sys)出生日期</xsl:when><xsl:when test="$wb_lang = 'gb'">(sys)出生日期</xsl:when><xsl:otherwise>(sys)Date of birth</xsl:otherwise></xsl:choose>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>

<!-- usr_email -->
<xsl:variable name="lab_e_mail">
	<xsl:choose>
		<xsl:when test="$profile_attributes/email">
			<xsl:value-of select="$profile_attributes/email/label[@xml:lang = $cur_lang]"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:choose><xsl:when test="$wb_lang = 'ch'">(sys)電子郵件</xsl:when><xsl:when test="$wb_lang = 'gb'">(sys)电子邮件</xsl:when><xsl:otherwise>(sys)E-mail address</xsl:otherwise></xsl:choose>
		</xsl:otherwise>
		</xsl:choose>
</xsl:variable>

<!-- usr_tel_1 -->
<xsl:variable name="lab_tel_1">
	<xsl:choose>
		<xsl:when test="$profile_attributes/phone">
			<xsl:value-of select="$profile_attributes/phone/label[@xml:lang = $cur_lang]"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:choose><xsl:when test="$wb_lang = 'ch'">(sys)電話</xsl:when><xsl:when test="$wb_lang = 'gb'">(sys)电话</xsl:when><xsl:otherwise>(sys)Phone</xsl:otherwise></xsl:choose>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>
<xsl:variable name="lab_tel_1_desc">
	<xsl:choose><xsl:when test="$wb_lang = 'ch'">國家號碼 - 區域號碼 - 電話號碼 </xsl:when><xsl:when test="$wb_lang = 'gb'">国家区号 - 城市区号 - 号码</xsl:when><xsl:otherwise>Country code - Area code - Number</xsl:otherwise></xsl:choose>
</xsl:variable>

<!-- usr_fax_1 -->

<!-- 员工编号 -->
<xsl:variable name="lab_staff_no">
	<xsl:choose>
		<xsl:when test="$profile_attributes/staffNo">
			<xsl:value-of select="$profile_attributes/staffNo/label[@xml:lang = $cur_lang]"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:choose><xsl:when test="$wb_lang = 'ch'">員工號碼</xsl:when><xsl:when test="$wb_lang = 'gb'">员工号码</xsl:when><xsl:otherwise>Staff No.</xsl:otherwise></xsl:choose>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>

<xsl:variable name="lab_fax_1">
	<xsl:choose>
		<xsl:when test="$profile_attributes/fax">
			<xsl:value-of select="$profile_attributes/fax/label[@xml:lang = $cur_lang]"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:choose><xsl:when test="$wb_lang = 'ch'">(sys)傳真</xsl:when><xsl:when test="$wb_lang = 'gb'">(sys)传真</xsl:when><xsl:otherwise>(sys)Fax</xsl:otherwise></xsl:choose>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>
<xsl:variable name="lab_fax_1_desc">
	<xsl:choose><xsl:when test="$wb_lang = 'ch'">國家號碼 - 區域號碼 - 電話號碼 </xsl:when><xsl:when test="$wb_lang = 'gb'">国家区号 - 城市区号 - 号码</xsl:when><xsl:otherwise>Country code - area Code - Number</xsl:otherwise></xsl:choose>
</xsl:variable>

<!-- usr_role_lst -->
<xsl:variable name="lab_role">
	<xsl:choose>
		<xsl:when test="$profile_attributes/role">
			<xsl:value-of select="$profile_attributes/role/label[@xml:lang = $cur_lang]"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:choose><xsl:when test="$wb_lang = 'ch'">(sys)身份</xsl:when><xsl:when test="$wb_lang = 'gb'">(sys)身份</xsl:when><xsl:otherwise>(sys)Role</xsl:otherwise></xsl:choose>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>

<xsl:variable name="lab_to">
	<xsl:choose><xsl:when test="$wb_lang = 'ch'">至</xsl:when><xsl:when test="$wb_lang = 'gb'">至</xsl:when><xsl:otherwise>to</xsl:otherwise></xsl:choose>
</xsl:variable>
<xsl:variable name="lab_all_time">
	<xsl:choose><xsl:when test="$wb_lang = 'ch'">全部時間</xsl:when><xsl:when test="$wb_lang = 'gb'">全部时间</xsl:when><xsl:otherwise>All time</xsl:otherwise></xsl:choose>
</xsl:variable>

<!-- usr_grade_lst -->
<xsl:variable name="lab_grade">
	<xsl:choose>
		<xsl:when test="$profile_attributes/grade">
			<xsl:value-of select="$profile_attributes/grade/label[@xml:lang = $cur_lang]"/>
		</xsl:when>
		<xsl:otherwise>
	<xsl:choose><xsl:when test="$wb_lang = 'ch'">所有職级</xsl:when><xsl:when test="$wb_lang = 'gb'">所有职级</xsl:when><xsl:otherwise>Grade</xsl:otherwise></xsl:choose>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>

<xsl:variable name="lab_grades">
	<xsl:value-of select="$lab_grade"/>
</xsl:variable>


<!--  usr_group_lst -->
<xsl:variable name="lab_group">
	<xsl:choose>
		<xsl:when test="$profile_attributes/group">
			<xsl:value-of select="$profile_attributes/group/label[@xml:lang = $cur_lang]"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:choose><xsl:when test="$wb_lang = 'ch'">(sys)組</xsl:when><xsl:when test="$wb_lang = 'gb'">(sys)用户组</xsl:when><xsl:otherwise>(sys)User group</xsl:otherwise></xsl:choose>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>

<!-- usr_direct_supervisors -->
<xsl:variable name="lab_direct_supervisors">
	<xsl:choose>
		<xsl:when test="$profile_attributes/direct_supervisors">
			<xsl:value-of select="$profile_attributes/direct_supervisors/label[@xml:lang = $cur_lang]"/>
		</xsl:when>
		<xsl:otherwise>
	<xsl:choose><xsl:when test="$wb_lang = 'ch'">(sys)直屬上司</xsl:when><xsl:when test="$wb_lang = 'gb'">(sys)直接领导</xsl:when><xsl:otherwise>(sys)Direct supervisors</xsl:otherwise></xsl:choose>
	</xsl:otherwise>
	</xsl:choose>
</xsl:variable>

<!-- usr_app_approval_usg_ent_id -->
<xsl:variable name="lab_usr_app_approval_usg">
	<xsl:choose>
		<xsl:when test="$profile_attributes/app_approval_usg_ent_id">
			<xsl:value-of select="$profile_attributes/app_approval_usg_ent_id/label[@xml:lang = $cur_lang]"/>
		</xsl:when>
		<xsl:otherwise>
	<xsl:choose><xsl:when test="$wb_lang = 'ch'">(sys)需要審批報名請求的用戶組上司</xsl:when><xsl:when test="$wb_lang = 'gb'">(sys)需要审批报名请求的用户组领导</xsl:when><xsl:otherwise>(sys)Group supervisors for approval</xsl:otherwise></xsl:choose>
	</xsl:otherwise>
	</xsl:choose>
</xsl:variable>
<xsl:variable name="lab_usr_app_approval_usg_desc">
	<xsl:choose>
		<xsl:when test="$wb_lang = 'ch'">當報名流程需要用戶組上司審批時，該報名個案將需要此用戶組以下的用戶組上司審批。</xsl:when>
		<xsl:when test="$wb_lang = 'gb'">当报名处理流程需要多级审批时，将通过以上属性决定执行用户组领导审批的最高层用户组。</xsl:when>
		<xsl:otherwise>Used for determining the highest level of group approval required for this user in an enrollment process that requires group supervisor approval.</xsl:otherwise>
	</xsl:choose>
</xsl:variable>


<!-- usr_join_date -->
<xsl:variable name="lab_join_date">
	<xsl:choose>
		<xsl:when test="$profile_attributes/join_date">
			<xsl:value-of select="$profile_attributes/join_date/label[@xml:lang = $cur_lang]"/>
		</xsl:when>
		<xsl:otherwise>
	<xsl:choose><xsl:when test="$wb_lang = 'ch'">(sys)加入日期</xsl:when><xsl:when test="$wb_lang = 'gb'">(sys)加入日期</xsl:when><xsl:otherwise>(sys)Join date</xsl:otherwise></xsl:choose>
	</xsl:otherwise>
	</xsl:choose>
</xsl:variable>

<!-- usr_source -->
<xsl:variable name="lab_usr_source">
	<xsl:choose>
		<xsl:when test="$profile_attributes/source">
			<xsl:value-of select="$profile_attributes/source/label[@xml:lang = $cur_lang]"/>
		</xsl:when>
		<xsl:otherwise>
	<xsl:choose><xsl:when test="$wb_lang = 'ch'">(sys)來源</xsl:when><xsl:when test="$wb_lang = 'gb'">(sys)来源</xsl:when><xsl:otherwise>(sys)Source</xsl:otherwise></xsl:choose>
	</xsl:otherwise>
	</xsl:choose>
</xsl:variable>

<!-- other_id -->
<xsl:variable name="lab_other_id">
	<xsl:choose>
		<xsl:when test="$profile_attributes/other_id">
			<xsl:value-of select="$profile_attributes/other_id/label[@xml:lang = $cur_lang]"/>
		</xsl:when>
		<xsl:otherwise>
	<xsl:choose><xsl:when test="$wb_lang = 'ch'">(sys)其他編號</xsl:when><xsl:when test="$wb_lang = 'gb'">(sys)其他编号</xsl:when><xsl:otherwise>(sys)Other ID</xsl:otherwise></xsl:choose>
	</xsl:otherwise>
	</xsl:choose>
</xsl:variable>

<!-- other_id_type -->
<xsl:variable name="lab_other_id_type">
	<xsl:choose>
		<xsl:when test="$profile_attributes/other_id_type">
			<xsl:value-of select="$profile_attributes/other_id_type/label[@xml:lang = $cur_lang]"/>
		</xsl:when>
		<xsl:otherwise>
	<xsl:choose><xsl:when test="$wb_lang = 'ch'">(sys)其他編號類別</xsl:when><xsl:when test="$wb_lang = 'gb'">(sys)其他编号类别</xsl:when><xsl:otherwise>(sys)Other ID type</xsl:otherwise></xsl:choose>
	</xsl:otherwise>
	</xsl:choose>
</xsl:variable>

<!-- usr_extra_1 -->
<xsl:variable name="lab_extra_1">
	<xsl:choose>
		<xsl:when test="$profile_attributes/extension_1">
			<xsl:value-of select="$profile_attributes/extension_1/label[@xml:lang = $cur_lang]"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:choose><xsl:when test="$wb_lang = 'ch'">(sys)其他資料 1</xsl:when><xsl:when test="$wb_lang = 'gb'">(sys)其他资料1</xsl:when><xsl:otherwise>(sys)usr_extra_1</xsl:otherwise></xsl:choose>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>

<!-- usr_extra_2 -->
<xsl:variable name="lab_extra_2">
	<xsl:choose>
		<xsl:when test="$profile_attributes/extension_2">
			<xsl:value-of select="$profile_attributes/extension_2/label[@xml:lang = $cur_lang]"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:choose><xsl:when test="$wb_lang = 'ch'">(sys)其他資料 2</xsl:when><xsl:when test="$wb_lang = 'gb'">(sys)其他资料_2</xsl:when><xsl:otherwise>(sys)usr_extra_2</xsl:otherwise></xsl:choose>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>

<!-- usr_extra_3 -->
<xsl:variable name="lab_extra_3">
	<xsl:choose>
		<xsl:when test="$profile_attributes/extension_3">
			<xsl:value-of select="$profile_attributes/extension_3/label[@xml:lang = $cur_lang]"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:choose><xsl:when test="$wb_lang = 'ch'">(sys)其他資料 3</xsl:when><xsl:when test="$wb_lang = 'gb'">(sys)其他资料_3</xsl:when><xsl:otherwise>(sys)usr_extra_3</xsl:otherwise></xsl:choose>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>

<!-- usr_extra_4 -->
<xsl:variable name="lab_extra_4">
	<xsl:choose>
		<xsl:when test="$profile_attributes/extension_4">
			<xsl:value-of select="$profile_attributes/extension_4/label[@xml:lang = $cur_lang]"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:choose><xsl:when test="$wb_lang = 'ch'">(sys)其他資料 4</xsl:when><xsl:when test="$wb_lang = 'gb'">(sys)其他资料_4</xsl:when><xsl:otherwise>(sys)usr_extra_4</xsl:otherwise></xsl:choose>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>


<!-- usr_extra_5 -->
<xsl:variable name="lab_extra_5">
	<xsl:choose>
		<xsl:when test="$profile_attributes/extension_5">
			<xsl:value-of select="$profile_attributes/extension_5/label[@xml:lang = $cur_lang]"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:choose><xsl:when test="$wb_lang = 'ch'">(sys)其他資料 5</xsl:when><xsl:when test="$wb_lang = 'gb'">(sys)其他资料_5</xsl:when><xsl:otherwise>(sys)usr_extra_5</xsl:otherwise></xsl:choose>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>

<!-- usr_extra_6 -->
<xsl:variable name="lab_extra_6">
	<xsl:choose>
		<xsl:when test="$profile_attributes/extension_6">
			<xsl:value-of select="$profile_attributes/extension_6/label[@xml:lang = $cur_lang]"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:choose><xsl:when test="$wb_lang = 'ch'">(sys)其他資料 6</xsl:when><xsl:when test="$wb_lang = 'gb'">(sys)其他资料_6</xsl:when><xsl:otherwise>(sys)usr_extra_6</xsl:otherwise></xsl:choose>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>

<!-- usr_extra_7 -->
<xsl:variable name="lab_extra_7">
	<xsl:choose>
		<xsl:when test="$profile_attributes/extension_7">
			<xsl:value-of select="$profile_attributes/extension_7/label[@xml:lang = $cur_lang]"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:choose><xsl:when test="$wb_lang = 'ch'">(sys)其他資料 7</xsl:when><xsl:when test="$wb_lang = 'gb'">(sys)其他资料_7</xsl:when><xsl:otherwise>(sys)usr_extra_7</xsl:otherwise></xsl:choose>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>

<!-- usr_extra_8 -->
<xsl:variable name="lab_extra_8">
	<xsl:choose>
		<xsl:when test="$profile_attributes/extension_8">
			<xsl:value-of select="$profile_attributes/extension_8/label[@xml:lang = $cur_lang]"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:choose><xsl:when test="$wb_lang = 'ch'">(sys)其他資料 8</xsl:when><xsl:when test="$wb_lang = 'gb'">(sys)其他资料_8</xsl:when><xsl:otherwise>(sys)usr_extra_8</xsl:otherwise></xsl:choose>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>

<!-- usr_extra_9 -->
<xsl:variable name="lab_extra_9">
	<xsl:choose>
		<xsl:when test="$profile_attributes/extension_9">
			<xsl:value-of select="$profile_attributes/extension_9/label[@xml:lang = $cur_lang]"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:choose><xsl:when test="$wb_lang = 'ch'">(sys)其他資料 9</xsl:when><xsl:when test="$wb_lang = 'gb'">(sys)其他资料_9</xsl:when><xsl:otherwise>(sys)usr_extra_9</xsl:otherwise></xsl:choose>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>

<!-- usr_extra_10 -->
<xsl:variable name="lab_extra_10">
	<xsl:choose>
		<xsl:when test="$profile_attributes/extension_10">
			<xsl:value-of select="$profile_attributes/extension_10/label[@xml:lang = $cur_lang]"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:choose><xsl:when test="$wb_lang = 'ch'">(sys)其他資料 10</xsl:when><xsl:when test="$wb_lang = 'gb'">(sys)其他资料_10</xsl:when><xsl:otherwise>(sys)usr_extra_10</xsl:otherwise></xsl:choose>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>
<!-- usr_extra_datetime_11 -->
<xsl:variable name="lab_extra_datetime_11">
	<xsl:choose>
		<xsl:when test="$profile_attributes/extension_11">
			<xsl:value-of select="$profile_attributes/extension_11/label[@xml:lang = $cur_lang]"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:choose><xsl:when test="$wb_lang = 'ch'">(sys)其他資料 11</xsl:when><xsl:when test="$wb_lang = 'gb'">(sys)其他资料_10</xsl:when><xsl:otherwise>(sys)usr_extra_datetime_11</xsl:otherwise></xsl:choose>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>
<!-- usr_extra_datetime_12 -->
<xsl:variable name="lab_extra_datetime_12">
	<xsl:choose>
		<xsl:when test="$profile_attributes/extension_12">
			<xsl:value-of select="$profile_attributes/extension_12/label[@xml:lang = $cur_lang]"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:choose><xsl:when test="$wb_lang = 'ch'">(sys)其他資料 12</xsl:when><xsl:when test="$wb_lang = 'gb'">(sys)其他资料_10</xsl:when><xsl:otherwise>(sys)usr_extra_datetime_12</xsl:otherwise></xsl:choose>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>
<!-- usr_extra_datetime_13 -->
<xsl:variable name="lab_extra_datetime_13">
	<xsl:choose>
		<xsl:when test="$profile_attributes/extension_13">
			<xsl:value-of select="$profile_attributes/extension_13/label[@xml:lang = $cur_lang]"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:choose><xsl:when test="$wb_lang = 'ch'">(sys)其他資料 13</xsl:when><xsl:when test="$wb_lang = 'gb'">(sys)其他资料_10</xsl:when><xsl:otherwise>(sys)usr_extra_datetime_13</xsl:otherwise></xsl:choose>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>
<!-- usr_extra_datetime_14 -->
<xsl:variable name="lab_extra_datetime_14">
	<xsl:choose>
		<xsl:when test="$profile_attributes/extension_14">
			<xsl:value-of select="$profile_attributes/extension_14/label[@xml:lang = $cur_lang]"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:choose><xsl:when test="$wb_lang = 'ch'">(sys)其他資料 14</xsl:when><xsl:when test="$wb_lang = 'gb'">(sys)其他资料_10</xsl:when><xsl:otherwise>(sys)usr_extra_datetime_14</xsl:otherwise></xsl:choose>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>
<!-- usr_extra_datetime_15 -->
<xsl:variable name="lab_extra_datetime_15">
	<xsl:choose>
		<xsl:when test="$profile_attributes/extension_15">
			<xsl:value-of select="$profile_attributes/extension_15/label[@xml:lang = $cur_lang]"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:choose><xsl:when test="$wb_lang = 'ch'">(sys)其他資料 15</xsl:when><xsl:when test="$wb_lang = 'gb'">(sys)其他资料_10</xsl:when><xsl:otherwise>(sys)usr_extra_datetime_15</xsl:otherwise></xsl:choose>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>
<!-- usr_extra_datetime_16 -->
<xsl:variable name="lab_extra_datetime_16">
	<xsl:choose>
		<xsl:when test="$profile_attributes/extension_16">
			<xsl:value-of select="$profile_attributes/extension_16/label[@xml:lang = $cur_lang]"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:choose><xsl:when test="$wb_lang = 'ch'">(sys)其他資料 16</xsl:when><xsl:when test="$wb_lang = 'gb'">(sys)其他资料_10</xsl:when><xsl:otherwise>(sys)usr_extra_datetime_16</xsl:otherwise></xsl:choose>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>
<!-- usr_extra_datetime_17 -->
<xsl:variable name="lab_extra_datetime_17">
	<xsl:choose>
		<xsl:when test="$profile_attributes/extension_17">
			<xsl:value-of select="$profile_attributes/extension_17/label[@xml:lang = $cur_lang]"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:choose><xsl:when test="$wb_lang = 'ch'">(sys)其他資料 17</xsl:when><xsl:when test="$wb_lang = 'gb'">(sys)其他资料_10</xsl:when><xsl:otherwise>(sys)usr_extra_datetime_17</xsl:otherwise></xsl:choose>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>
<!-- usr_extra_datetime_18 -->
<xsl:variable name="lab_extra_datetime_18">
	<xsl:choose>
		<xsl:when test="$profile_attributes/extension_18">
			<xsl:value-of select="$profile_attributes/extension_18/label[@xml:lang = $cur_lang]"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:choose><xsl:when test="$wb_lang = 'ch'">(sys)其他資料 18</xsl:when><xsl:when test="$wb_lang = 'gb'">(sys)其他资料_10</xsl:when><xsl:otherwise>(sys)usr_extra_datetime_18</xsl:otherwise></xsl:choose>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>
<!-- usr_extra_datetime_19 -->
<xsl:variable name="lab_extra_datetime_19">
	<xsl:choose>
		<xsl:when test="$profile_attributes/extension_19">
			<xsl:value-of select="$profile_attributes/extension_19/label[@xml:lang = $cur_lang]"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:choose><xsl:when test="$wb_lang = 'ch'">(sys)其他資料 19</xsl:when><xsl:when test="$wb_lang = 'gb'">(sys)其他资料_10</xsl:when><xsl:otherwise>(sys)usr_extra_datetime_19</xsl:otherwise></xsl:choose>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>
<!-- usr_extra_datetime_20 -->
<xsl:variable name="lab_extra_datetime_20">
	<xsl:choose>
		<xsl:when test="$profile_attributes/extension_20">
			<xsl:value-of select="$profile_attributes/extension_20/label[@xml:lang = $cur_lang]"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:choose><xsl:when test="$wb_lang = 'ch'">(sys)其他資料 20</xsl:when><xsl:when test="$wb_lang = 'gb'">(sys)其他资料_10</xsl:when><xsl:otherwise>(sys)usr_extra_datetime_20</xsl:otherwise></xsl:choose>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>

<!-- ======================================================================== -->
<xsl:variable name="lab_form_title_1">
	<xsl:choose><xsl:when test="$wb_lang = 'ch'">用戶資料</xsl:when><xsl:when test="$wb_lang = 'gb'">用户资料</xsl:when><xsl:otherwise>Account information</xsl:otherwise></xsl:choose>
</xsl:variable>

<xsl:variable name="lab_form_title_2">
	<xsl:choose><xsl:when test="$wb_lang = 'ch'">聯絡資料</xsl:when><xsl:when test="$wb_lang = 'gb'">联络资料</xsl:when><xsl:otherwise>Contact information</xsl:otherwise></xsl:choose>
</xsl:variable>

<xsl:variable name="lab_form_title_3">
	<xsl:choose><xsl:when test="$wb_lang = 'ch'">系統資料</xsl:when><xsl:when test="$wb_lang = 'gb'">系统资料</xsl:when><xsl:otherwise>System information</xsl:otherwise></xsl:choose>
</xsl:variable>

<xsl:variable name="lab_form_title_4">
	<xsl:choose><xsl:when test="$wb_lang = 'ch'">系統資料</xsl:when><xsl:when test="$wb_lang = 'gb'">系统资料</xsl:when><xsl:otherwise>Supervisor reference information</xsl:otherwise></xsl:choose>
</xsl:variable>

<xsl:variable name="lab_form_title_others">
	<xsl:choose><xsl:when test="$wb_lang = 'ch'">其他資料</xsl:when><xsl:when test="$wb_lang = 'gb'">其他资料</xsl:when><xsl:otherwise>Other information</xsl:otherwise></xsl:choose>
</xsl:variable>

<!-- ======================================================================== -->
<xsl:variable name="lab_usr_status">
	<xsl:choose><xsl:when test="$wb_lang = 'ch'">用戶狀態</xsl:when><xsl:when test="$wb_lang = 'gb'">用户状态</xsl:when><xsl:otherwise>User status</xsl:otherwise></xsl:choose>
</xsl:variable>

<xsl:variable name="lab_usr_sort">
	<xsl:choose><xsl:when test="$wb_lang = 'ch'">篩選次序</xsl:when><xsl:when test="$wb_lang = 'gb'">排序</xsl:when><xsl:otherwise>Sort order</xsl:otherwise></xsl:choose>
</xsl:variable>

<xsl:variable name="lab_usr_status_active">
	<xsl:choose><xsl:when test="$wb_lang = 'ch'">正常</xsl:when><xsl:when test="$wb_lang = 'gb'">正常</xsl:when><xsl:otherwise>Active</xsl:otherwise></xsl:choose>
</xsl:variable>

<xsl:variable name="lab_usr_status_deleted">
	<xsl:choose><xsl:when test="$wb_lang = 'ch'">已刪除</xsl:when><xsl:when test="$wb_lang = 'gb'">已删除</xsl:when><xsl:otherwise>Deleted</xsl:otherwise></xsl:choose>
</xsl:variable>

<xsl:variable name="lab_sort_value_1">
	<xsl:choose><xsl:when test="$wb_lang = 'ch'">順序</xsl:when><xsl:when test="$wb_lang = 'gb'">顺序</xsl:when><xsl:otherwise>Ascending</xsl:otherwise></xsl:choose>
</xsl:variable>

<xsl:variable name="lab_sort_value_2">
	<xsl:choose><xsl:when test="$wb_lang = 'ch'">倒序</xsl:when><xsl:when test="$wb_lang = 'gb'">倒序</xsl:when><xsl:otherwise>Descending</xsl:otherwise></xsl:choose>
</xsl:variable>
<!-- ======================================================================== -->
<xsl:variable name="lab_supervised_groups_desc">
	<xsl:choose><xsl:when test="$wb_lang = 'ch'">指定該用戶可以管理的用戶組，所有被指定的用戶組和此用戶的直接下屬，此用戶都擁有直接管理的許可權，包括學員的個人資訊。</xsl:when><xsl:when test="$wb_lang = 'gb'">指定该用户可以管理的用户组，所有被指定的用户组和此用户的直接下属，此用户都拥有直接管理的权限，包括学员的个人信息。 </xsl:when><xsl:otherwise>User groups to which this user is the “group supervisor”. A group supervisor can review and approve enrollments of those users that are under the supervised group’s hierarchy.</xsl:otherwise></xsl:choose>
</xsl:variable>

<xsl:variable name="lab_extra_others">
			<xsl:choose>
				<xsl:when test="$wb_lang = 'ch'">其他...</xsl:when><xsl:when test="$wb_lang = 'gb'">其他...</xsl:when>
				<xsl:otherwise>others...</xsl:otherwise>
			</xsl:choose>
</xsl:variable>
<!--usr_extra_singleoption-->
<xsl:variable name="lab_extra_singleoption">
			<xsl:choose>
				<xsl:when test="$wb_lang = 'ch'">(sys)其他資料 21~30</xsl:when><xsl:when test="$wb_lang = 'gb'">(sys)其他资料_21~30</xsl:when>
				<xsl:otherwise>(sys)usr_extra_singleoption_21~30</xsl:otherwise>
			</xsl:choose>
</xsl:variable>


<!-- usr_extra_singleoption_21-->
<xsl:variable name="lab_extra_singleoption_21">
	<xsl:choose>
		<xsl:when test="$profile_attributes/extension_21">
			<xsl:value-of select="$profile_attributes/extension_21/label[@xml:lang = $cur_lang]"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:choose><xsl:when test="$wb_lang = 'ch'">(sys)其他資料 21</xsl:when><xsl:when test="$wb_lang = 'gb'">(sys)其他资料_21</xsl:when><xsl:otherwise>(sys)usr_extra_singleoption_21</xsl:otherwise></xsl:choose>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>
<!-- usr_extra_singleoption_22-->
<xsl:variable name="lab_extra_singleoption_22">
	<xsl:choose>
		<xsl:when test="$profile_attributes/extension_22">
			<xsl:value-of select="$profile_attributes/extension_22/label[@xml:lang = $cur_lang]"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:choose><xsl:when test="$wb_lang = 'ch'">(sys)其他資料 22</xsl:when><xsl:when test="$wb_lang = 'gb'">(sys)其他资料_22</xsl:when><xsl:otherwise>(sys)usr_extra_singleoption_22</xsl:otherwise></xsl:choose>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>
<!-- usr_extra_singleoption_23-->
<xsl:variable name="lab_extra_singleoption_23">
	<xsl:choose>
		<xsl:when test="$profile_attributes/extension_23">
			<xsl:value-of select="$profile_attributes/extension_23/label[@xml:lang = $cur_lang]"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:choose><xsl:when test="$wb_lang = 'ch'">(sys)其他資料 23</xsl:when><xsl:when test="$wb_lang = 'gb'">(sys)其他资料_23</xsl:when><xsl:otherwise>(sys)usr_extra_singleoption_23</xsl:otherwise></xsl:choose>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>
<!-- usr_extra_singleoption_24-->
<xsl:variable name="lab_extra_singleoption_24">
	<xsl:choose>
		<xsl:when test="$profile_attributes/extension_24">
			<xsl:value-of select="$profile_attributes/extension_24/label[@xml:lang = $cur_lang]"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:choose><xsl:when test="$wb_lang = 'ch'">(sys)其他資料 24</xsl:when><xsl:when test="$wb_lang = 'gb'">(sys)其他资料_24</xsl:when><xsl:otherwise>(sys)usr_extra_singleoption_24</xsl:otherwise></xsl:choose>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>
<!-- usr_extra_singleoption_25-->
<xsl:variable name="lab_extra_singleoption_25">
	<xsl:choose>
		<xsl:when test="$profile_attributes/extension_25">
			<xsl:value-of select="$profile_attributes/extension_25/label[@xml:lang = $cur_lang]"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:choose><xsl:when test="$wb_lang = 'ch'">(sys)其他資料 25</xsl:when><xsl:when test="$wb_lang = 'gb'">(sys)其他资料_25</xsl:when><xsl:otherwise>(sys)usr_extra_singleoption_25</xsl:otherwise></xsl:choose>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>
<!-- usr_extra_singleoption_26-->
<xsl:variable name="lab_extra_singleoption_26">
	<xsl:choose>
		<xsl:when test="$profile_attributes/extension_26">
			<xsl:value-of select="$profile_attributes/extension_26/label[@xml:lang = $cur_lang]"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:choose><xsl:when test="$wb_lang = 'ch'">(sys)其他資料 26</xsl:when><xsl:when test="$wb_lang = 'gb'">(sys)其他资料_26</xsl:when><xsl:otherwise>(sys)usr_extra_singleoption_26</xsl:otherwise></xsl:choose>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>
<!-- usr_extra_singleoption_27-->
<xsl:variable name="lab_extra_singleoption_27">
	<xsl:choose>
		<xsl:when test="$profile_attributes/extension_27">
			<xsl:value-of select="$profile_attributes/extension_27/label[@xml:lang = $cur_lang]"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:choose><xsl:when test="$wb_lang = 'ch'">(sys)其他資料 27</xsl:when><xsl:when test="$wb_lang = 'gb'">(sys)其他资料_27</xsl:when><xsl:otherwise>(sys)usr_extra_singleoption_27</xsl:otherwise></xsl:choose>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>
<!-- usr_extra_singleoption_28-->
<xsl:variable name="lab_extra_singleoption_28">
	<xsl:choose>
		<xsl:when test="$profile_attributes/extension_28">
			<xsl:value-of select="$profile_attributes/extension_28/label[@xml:lang = $cur_lang]"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:choose><xsl:when test="$wb_lang = 'ch'">(sys)其他資料 28</xsl:when><xsl:when test="$wb_lang = 'gb'">(sys)其他资料_28</xsl:when><xsl:otherwise>(sys)usr_extra_singleoption_28</xsl:otherwise></xsl:choose>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>
<!-- usr_extra_singleoption_29-->
<xsl:variable name="lab_extra_singleoption_29">
	<xsl:choose>
		<xsl:when test="$profile_attributes/extension_29">
			<xsl:value-of select="$profile_attributes/extension_29/label[@xml:lang = $cur_lang]"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:choose>
				<xsl:when test="$wb_lang = 'ch'">(sys)其他資料 29</xsl:when><xsl:when test="$wb_lang = 'gb'">(sys)其他资料_29</xsl:when>
				<xsl:otherwise>(sys)usr_extra_singleoption_29</xsl:otherwise>
			</xsl:choose>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>
<!-- usr_extra_singleoption_30-->
<xsl:variable name="lab_extra_singleoption_30">
	<xsl:choose>
		<xsl:when test="$profile_attributes/extension_30">
			<xsl:value-of select="$profile_attributes/extension_30/label[@xml:lang = $cur_lang]"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:choose><xsl:when test="$wb_lang = 'ch'">(sys)其他資料 30</xsl:when><xsl:when test="$wb_lang = 'gb'">(sys)其他资料_30</xsl:when><xsl:otherwise>(sys)usr_extra_singleoption_30</xsl:otherwise></xsl:choose>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>

<!--usr_extra_multipleoption-->
<xsl:variable name="lab_extra_multipleoption">
			<xsl:choose>
				<xsl:when test="$wb_lang = 'ch'">(sys)其他資料 31~40</xsl:when><xsl:when test="$wb_lang = 'gb'">(sys)其他资料_31~40</xsl:when>
				<xsl:otherwise>(sys)usr_extra_singleoption_31~40</xsl:otherwise>
			</xsl:choose>
</xsl:variable>

<!-- usr_extra_multipleoption_31-->
<xsl:variable name="lab_extra_multipleoption_31">
	<xsl:choose>
		<xsl:when test="$profile_attributes/extension_31">
			<xsl:value-of select="$profile_attributes/extension_31/label[@xml:lang = $cur_lang]"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:choose><xsl:when test="$wb_lang = 'ch'">(sys)其他資料 31</xsl:when><xsl:when test="$wb_lang = 'gb'">(sys)其他资料_31</xsl:when><xsl:otherwise>(sys)usr_extra_singleoption_31</xsl:otherwise></xsl:choose>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>
<!-- usr_extra_singleoption_32-->
<xsl:variable name="lab_extra_multipleoption_32">
	<xsl:choose>
		<xsl:when test="$profile_attributes/extension_32">
			<xsl:value-of select="$profile_attributes/extension_32/label[@xml:lang = $cur_lang]"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:choose><xsl:when test="$wb_lang = 'ch'">(sys)其他資料 32</xsl:when><xsl:when test="$wb_lang = 'gb'">(sys)其他资料_32</xsl:when><xsl:otherwise>(sys)usr_extra_singleoption_32</xsl:otherwise></xsl:choose>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>
<!-- usr_extra_singleoption_33-->
<xsl:variable name="lab_extra_multipleoption_33">
	<xsl:choose>
		<xsl:when test="$profile_attributes/extension_33">
			<xsl:value-of select="$profile_attributes/extension_33/label[@xml:lang = $cur_lang]"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:choose><xsl:when test="$wb_lang = 'ch'">(sys)其他資料 33</xsl:when><xsl:when test="$wb_lang = 'gb'">(sys)其他资料_33</xsl:when><xsl:otherwise>(sys)usr_extra_singleoption_33</xsl:otherwise></xsl:choose>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>
<!-- usr_extra_singleoption_34-->
<xsl:variable name="lab_extra_multipleoption_34">
	<xsl:choose>
		<xsl:when test="$profile_attributes/extension_34">
			<xsl:value-of select="$profile_attributes/extension_34/label[@xml:lang = $cur_lang]"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:choose><xsl:when test="$wb_lang = 'ch'">(sys)其他資料 34</xsl:when><xsl:when test="$wb_lang = 'gb'">(sys)其他资料_34</xsl:when><xsl:otherwise>(sys)usr_extra_singleoption_34</xsl:otherwise></xsl:choose>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>
<!-- usr_extra_singleoption_35-->
<xsl:variable name="lab_extra_multipleoption_35">
	<xsl:choose>
		<xsl:when test="$profile_attributes/extension_35">
			<xsl:value-of select="$profile_attributes/extension_35/label[@xml:lang = $cur_lang]"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:choose><xsl:when test="$wb_lang = 'ch'">(sys)其他資料 35</xsl:when><xsl:when test="$wb_lang = 'gb'">(sys)其他资料_35</xsl:when><xsl:otherwise>(sys)usr_extra_singleoption_35</xsl:otherwise></xsl:choose>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>
<!-- usr_extra_singleoption_36-->
<xsl:variable name="lab_extra_multipleoption_36">
	<xsl:choose>
		<xsl:when test="$profile_attributes/extension_36">
			<xsl:value-of select="$profile_attributes/extension_36/label[@xml:lang = $cur_lang]"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:choose><xsl:when test="$wb_lang = 'ch'">(sys)其他資料 36</xsl:when><xsl:when test="$wb_lang = 'gb'">(sys)其他资料_36</xsl:when><xsl:otherwise>(sys)usr_extra_singleoption_36</xsl:otherwise></xsl:choose>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>
<!-- usr_extra_singleoption_37-->
<xsl:variable name="lab_extra_multipleoption_37">
	<xsl:choose>
		<xsl:when test="$profile_attributes/extension_37">
			<xsl:value-of select="$profile_attributes/extension_37/label[@xml:lang = $cur_lang]"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:choose><xsl:when test="$wb_lang = 'ch'">(sys)其他資料 37</xsl:when><xsl:when test="$wb_lang = 'gb'">(sys)其他资料_37</xsl:when><xsl:otherwise>(sys)usr_extra_singleoption_37</xsl:otherwise></xsl:choose>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>
<!-- usr_extra_singleoption_38-->
<xsl:variable name="lab_extra_multipleoption_38">
	<xsl:choose>
		<xsl:when test="$profile_attributes/extension_38">
			<xsl:value-of select="$profile_attributes/extension_38/label[@xml:lang = $cur_lang]"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:choose><xsl:when test="$wb_lang = 'ch'">(sys)其他資料 38</xsl:when><xsl:when test="$wb_lang = 'gb'">(sys)其他资料_38</xsl:when><xsl:otherwise>(sys)usr_extra_singleoption_38</xsl:otherwise></xsl:choose>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>
<!-- usr_extra_singleoption_39-->
<xsl:variable name="lab_extra_multipleoption_39">
	<xsl:choose>
		<xsl:when test="$profile_attributes/extension_39">
			<xsl:value-of select="$profile_attributes/extension_39/label[@xml:lang = $cur_lang]"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:choose><xsl:when test="$wb_lang = 'ch'">(sys)其他資料 39</xsl:when><xsl:when test="$wb_lang = 'gb'">(sys)其他资料_39</xsl:when><xsl:otherwise>(sys)usr_extra_singleoption_39</xsl:otherwise></xsl:choose>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>
<!-- usr_extra_singleoption_40-->
<xsl:variable name="lab_extra_multipleoption_40">
	<xsl:choose>
		<xsl:when test="$profile_attributes/extension_40">
			<xsl:value-of select="$profile_attributes/extension_40/label[@xml:lang = $cur_lang]"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:choose><xsl:when test="$wb_lang = 'ch'">(sys)其他資料 40</xsl:when><xsl:when test="$wb_lang = 'gb'">(sys)其他资料_40</xsl:when><xsl:otherwise>(sys)usr_extra_singleoption_40</xsl:otherwise></xsl:choose>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>

<xsl:variable name="lab_nickname">
	<xsl:choose>
		<xsl:when test="$profile_attributes/nickname">
			<xsl:value-of select="$profile_attributes/nickname/label[@xml:lang = $cur_lang]"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:choose><xsl:when test="$wb_lang = 'ch'">(sys)名稱</xsl:when><xsl:when test="$wb_lang = 'gb'">(sys)昵称</xsl:when><xsl:otherwise>(sys)Name</xsl:otherwise></xsl:choose>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>


<xsl:variable name="lab_name">
	<xsl:choose>
		<xsl:when test="$profile_attributes/nickname">
			<xsl:value-of select="$profile_attributes/name/label[@xml:lang = $cur_lang]"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:choose><xsl:when test="$wb_lang = 'ch'">(sys)全名</xsl:when><xsl:when test="$wb_lang = 'gb'">(sys)全名</xsl:when><xsl:otherwise>(sys)Name</xsl:otherwise></xsl:choose>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>

<xsl:variable name="lab_competency">
	<xsl:choose>
		<xsl:when test="$profile_attributes/competency">
			<xsl:value-of select="$profile_attributes/competency/label[@xml:lang = $cur_lang]"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:choose><xsl:when test="$wb_lang = 'ch'">崗位</xsl:when><xsl:when test="$wb_lang = 'gb'">岗位</xsl:when><xsl:otherwise>Competency</xsl:otherwise></xsl:choose>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>

<xsl:variable name="lab_extension_41">
	<xsl:choose>
		<xsl:when test="$profile_attributes/extension_41">
			<xsl:value-of select="$profile_attributes/extension_41/label[@xml:lang = $cur_lang]"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:choose><xsl:when test="$wb_lang = 'ch'">(sys)Msn Messenger</xsl:when><xsl:when test="$wb_lang = 'gb'">(sys)Msn messenger</xsl:when><xsl:otherwise>(sys)Msn messenger</xsl:otherwise></xsl:choose>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>

<xsl:variable name="lab_extension_42">
	<xsl:choose>
		<xsl:when test="$profile_attributes/extension_42">
			<xsl:value-of select="$profile_attributes/extension_42/label[@xml:lang = $cur_lang]"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:choose><xsl:when test="$wb_lang = 'ch'">(sys)博客网址</xsl:when><xsl:when test="$wb_lang = 'gb'">(sys)博客网址</xsl:when><xsl:otherwise>(sys)blog</xsl:otherwise></xsl:choose>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>

<xsl:variable name="lab_extension_43">
	<xsl:choose>
		<xsl:when test="$profile_attributes/extension_43">
			<xsl:value-of select="$profile_attributes/extension_43/label[@xml:lang = $cur_lang]"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:choose><xsl:when test="$wb_lang = 'ch'">(sys)个性头像</xsl:when><xsl:when test="$wb_lang = 'gb'">(sys)个性头像</xsl:when><xsl:otherwise>(sys)个性头像</xsl:otherwise></xsl:choose>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>

<xsl:variable name="lab_extension_44">
	<xsl:choose>
		<xsl:when test="$profile_attributes/extension_44">
			<xsl:value-of select="$profile_attributes/extension_44/label[@xml:lang = $cur_lang]"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:choose><xsl:when test="$wb_lang = 'ch'">(sys)个人描述</xsl:when><xsl:when test="$wb_lang = 'gb'">(sys)个人描述</xsl:when><xsl:otherwise>(sys)个人描述</xsl:otherwise></xsl:choose>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>

<xsl:variable name="lab_extension_45">
	<xsl:choose>
		<xsl:when test="$profile_attributes/extension_45">
			<xsl:value-of select="$profile_attributes/extension_45/label[@xml:lang = $cur_lang]"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:choose><xsl:when test="$wb_lang = 'ch'">(sys)兴趣</xsl:when><xsl:when test="$wb_lang = 'gb'">(sys)兴趣</xsl:when><xsl:otherwise>(sys)兴趣</xsl:otherwise></xsl:choose>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>

<!-- 样式修改 -->
<xsl:variable name="lab_attended_before_enrrol">
    <xsl:choose>
        <xsl:when test="$wb_lang = 'ch'">重修學員</xsl:when>
        <xsl:when test="$wb_lang = 'gb'">重修学员</xsl:when>
        <xsl:otherwise>Retake learner</xsl:otherwise>
    </xsl:choose>
</xsl:variable>
<xsl:variable name="lab_no_show_before_enrrol">
    <xsl:choose>
        <xsl:when test="$wb_lang = 'ch'">缺席次數</xsl:when>
        <xsl:when test="$wb_lang = 'gb'">缺席次数</xsl:when>
        <xsl:otherwise>Number of no-shows</xsl:otherwise>
    </xsl:choose>
</xsl:variable><xsl:variable name="lab_non_target_lrn_enrrol">
    <xsl:choose>
        <xsl:when test="$wb_lang = 'ch'">非目標學員</xsl:when>
        <xsl:when test="$wb_lang = 'gb'">非目标学员</xsl:when>
        <xsl:otherwise>Non-target learner</xsl:otherwise>
    </xsl:choose>
</xsl:variable><xsl:variable name="lab_non_target_enr_enrrol">
    <xsl:choose>
        <xsl:when test="$wb_lang = 'ch'">不可報名學員</xsl:when>
        <xsl:when test="$wb_lang = 'gb'">不可报名学员</xsl:when>
        <xsl:otherwise>Non-target enrollment</xsl:otherwise>
    </xsl:choose>
</xsl:variable>


</xsl:stylesheet>
