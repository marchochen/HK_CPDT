<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	version="1.0">
	<xsl:template name="import_detail">
		<xsl:param name="label" />
		<xsl:param name="lang" />
		<xsl:param name="id" />
		<xsl:param name="minLength" />
		<xsl:param name="maxLength" />
		<xsl:param name="allowEmpty" />
		<xsl:param name="isTcIndependent" />
		<xsl:choose>
			<!-- 登录名 -->
			<xsl:when test="$id='user_id'">
				<tr>
					<td nowrap="nowrap">
						<xsl:value-of select="$label" />
					</td>
					<xsl:call-template name="allowEmpty">
						<xsl:with-param name="empty" select="$allowEmpty" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:call-template name="length">
						<xsl:with-param name="minlength" select="$minLength" />
						<xsl:with-param name="maxlength" select="$maxLength" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:if test="$lang='en-us'">
						<td nowrap="nowrap">
							Character a - z, 0 - 9, '_' or '-' only
						</td>
						<td>
							If user ID exists in the system, information will be updated to
							that user;
							<br />
							otherwise, a new record is created.
						</td>
					</xsl:if>
					<xsl:if test="$lang='zh-cn'">
						<td nowrap="nowrap">
							只能包含：纯英文字母、数字、
							<br />
							下划线(_)、横线(-)
						</td>
						<td>如果系统存在该用户名，则执行更新操作，否则执行新增操作。</td>
					</xsl:if>
					<xsl:if test="$lang='zh-hk'">
						<td nowrap="nowrap">
							只能包含：純英文小寫字母、數字、
							<br />
							底線(_)、橫線(-)
						</td>
						<td>如果系統存在該用戶名，則執行更新操作，否則執行新增操作。</td>
					</xsl:if>
				</tr>
			</xsl:when>
			<!-- 密码 -->
			<xsl:when test="$id='password'">
				<tr>
					<td nowrap="nowrap">
						<xsl:value-of select="$label" />
					</td>
					<xsl:call-template name="allowEmpty">
						<xsl:with-param name="empty" select="$allowEmpty" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:call-template name="length">
						<xsl:with-param name="minlength" select="$minLength" />
						<xsl:with-param name="maxlength" select="$maxLength" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:if test="$lang='en-us'">
						<td nowrap="nowrap">
							Character A - Z, a - z, 0 - 9, '_' or '-' only
							<br/>
							Must contain letters and numbers
						</td>
						<td>
							For new users: If you do not specify a password, the system default password .<br/>
						    For existing users: If you do not specify a password, the original password will not be updated. If a password is specified and you selected "Update password for the existing user", your password will be updated with the specified password.   
						</td>
					</xsl:if>
					<xsl:if test="$lang='zh-cn'">
						<td nowrap="nowrap">
							只能包含：纯英文字母、数字、
							<br />
							下划线(_)、横线(-),
							<br />
							必须同时包含英文字母和数字。
						</td>
						<td>对于导入的新用户：如果不指定密码，将会使用系统默认密码。<br/>
						            对于旧用户：如果不指定密码，则不会更新该用户原来的密码；如果有指定密码，同时选中“同时更新旧用户的密码”，
						            则会使用指定的密码更新该用户的密码。     
					    </td>
					</xsl:if>
					<xsl:if test="$lang='zh-hk'">
						<td nowrap="nowrap">
							只能包含：純英文字母、數字、
							<br />
							底線(_)、橫線(-),
							<br />
							必須同時包含英文字母和數字。
						</td>
						<td>對於導入的新用戶：如果不指定密碼，將會使用系統默認密碼。<br/>
							對於舊用戶：如果不指定密碼，則不會更新該用戶原來的密碼；如果有指定密碼，同時選中“同時更新舊用戶的密碼”，
							則會使用指定的密碼更新該用戶的密碼。
						</td>
					</xsl:if>
				</tr>
			</xsl:when>
			<!-- 全名 -->
			<xsl:when test="$id='name'">
				<tr>
					<td nowrap="nowrap">
						<xsl:value-of select="$label" />
					</td>
					<xsl:call-template name="allowEmpty">
						<xsl:with-param name="empty" select="$allowEmpty" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:call-template name="length">
						<xsl:with-param name="minlength" select="$minLength" />
						<xsl:with-param name="maxlength" select="$maxLength" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:if test="$lang='en-us'">
						<td nowrap="nowrap">Text</td>
						<td></td>
					</xsl:if>
					<xsl:if test="$lang='zh-cn'">
						<td nowrap="nowrap">文本</td>
						<td></td>
					</xsl:if>
					<xsl:if test="$lang='zh-hk'">
						<td nowrap="nowrap">文本</td>
						<td></td>
					</xsl:if>
				</tr>

			</xsl:when>
			<!-- 昵称 -->
			<xsl:when test="$id='nickname'">
				<tr>
					<td nowrap="nowrap">
						<xsl:value-of select="$label" />
					</td>
					<xsl:call-template name="allowEmpty">
						<xsl:with-param name="empty" select="$allowEmpty" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:call-template name="length">
						<xsl:with-param name="minlength" select="$minLength" />
						<xsl:with-param name="maxlength" select="$maxLength" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:if test="$lang='en-us'">
						<td nowrap="nowrap">Text</td>
						<td></td>
					</xsl:if>
					<xsl:if test="$lang='zh-cn'">
						<td nowrap="nowrap">文本</td>
						<td></td>
					</xsl:if>
					<xsl:if test="$lang='zh-hk'">
						<td nowrap="nowrap">文本</td>
						<td></td>
					</xsl:if>
				</tr>


			</xsl:when>
			<!-- 性别 -->
			<xsl:when test="$id='gender'">
				<tr>
					<td nowrap="nowrap">
						<xsl:value-of select="$label" />
					</td>
					<xsl:call-template name="allowEmpty">
						<xsl:with-param name="empty" select="$allowEmpty" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:call-template name="length">
						<xsl:with-param name="minlength" select="$minLength" />
						<xsl:with-param name="maxlength" select="$maxLength" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:if test="$lang='en-us'">
						<td nowrap="nowrap">Text</td>
						<td>
							''M' for Male
							<br />
							'F' for Female
						</td>
					</xsl:if>
					<xsl:if test="$lang='zh-cn'">
						<td nowrap="nowrap">文本</td>
						<td>
							请填写对应的代码：
							<br />
							M－男
							<br />
							F－女
						</td>
					</xsl:if>
					<xsl:if test="$lang='zh-hk'">
						<td nowrap="nowrap">文本</td>
						<td>
							請填寫對應的代碼：
							<br />
							M－男
							<br />
							F－女
						</td>
					</xsl:if>
				</tr>

			</xsl:when>
			<!-- 出生日期 -->
			<xsl:when test="$id='date_of_birth'">
				<tr>
					<td nowrap="nowrap">
						<xsl:value-of select="$label" />
					</td>
					<xsl:call-template name="allowEmpty">
						<xsl:with-param name="empty" select="$allowEmpty" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:call-template name="length">
						<xsl:with-param name="minlength" select="$minLength" />
						<xsl:with-param name="maxlength" select="$maxLength" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:if test="$lang='en-us'">
						<td nowrap="nowrap">YYYY/MM/DD</td>
						<td>Set to time format "YYYY/MM/DD"</td>
					</xsl:if>
					<xsl:if test="$lang='zh-cn'">
						<td nowrap="nowrap">YYYY/MM/DD</td>
						<td>设置单元格格式为"YYYY/MM/DD"</td>
					</xsl:if>
					<xsl:if test="$lang='zh-hk'">
						<td nowrap="nowrap">YYYY/MM/DD</td>
						<td>設置單元格格式爲"YYYY/MM/DD"</td>
					</xsl:if>
				</tr>

			</xsl:when>
			<!-- 电子邮件 -->
			<xsl:when test="$id='email'">
				<tr>
					<td nowrap="nowrap">
						<xsl:value-of select="$label" />
					</td>
					<xsl:call-template name="allowEmpty">
						<xsl:with-param name="empty" select="$allowEmpty" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:call-template name="length">
						<xsl:with-param name="minlength" select="$minLength" />
						<xsl:with-param name="maxlength" select="$maxLength" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:if test="$lang='en-us'">
						<td nowrap="nowrap">Text</td>
						<td>A valid email address.</td>
					</xsl:if>
					<xsl:if test="$lang='zh-cn'">
						<td nowrap="nowrap">文本</td>
						<td>需要输入一个正确的邮箱地址。</td>
					</xsl:if>
					<xsl:if test="$lang='zh-hk'">
						<td nowrap="nowrap">文本</td>
						<td>需要輸入一個正確的郵箱地址。</td>
					</xsl:if>
				</tr>

			</xsl:when>
			<!-- 电话 -->
			<xsl:when test="$id='phone'">
				<tr>
					<td nowrap="nowrap">
						<xsl:value-of select="$label" />
					</td>
					<xsl:call-template name="allowEmpty">
						<xsl:with-param name="empty" select="$allowEmpty" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:call-template name="length">
						<xsl:with-param name="minlength" select="$minLength" />
						<xsl:with-param name="maxlength" select="$maxLength" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:if test="$lang='en-us'">
						<td nowrap="nowrap">Text</td>
						<td></td>
					</xsl:if>
					<xsl:if test="$lang='zh-cn'">
						<td nowrap="nowrap">文本</td>
						<td></td>
					</xsl:if>
					<xsl:if test="$lang='zh-hk'">
						<td nowrap="nowrap">文本</td>
						<td></td>
					</xsl:if>
				</tr>

			</xsl:when>
			<!-- 传真 -->
			<xsl:when test="$id='fax'">
				<tr>
					<td nowrap="nowrap">
						<xsl:value-of select="$label" />
					</td>
					<xsl:call-template name="allowEmpty">
						<xsl:with-param name="empty" select="$allowEmpty" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:call-template name="length">
						<xsl:with-param name="minlength" select="$minLength" />
						<xsl:with-param name="maxlength" select="$maxLength" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:if test="$lang='en-us'">
						<td nowrap="nowrap">Text</td>
						<td></td>
					</xsl:if>
					<xsl:if test="$lang='zh-cn'">
						<td nowrap="nowrap">文本</td>
						<td></td>
					</xsl:if>
					<xsl:if test="$lang='zh-hk'">
						<td nowrap="nowrap">文本</td>
						<td></td>
					</xsl:if>
				</tr>
			</xsl:when>
			<!-- QQ -->
			<xsl:when test="$id='extension_41'">
				<tr>
					<td nowrap="nowrap">
						<xsl:value-of select="$label" />
					</td>
					<xsl:call-template name="allowEmpty">
						<xsl:with-param name="empty" select="$allowEmpty" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:call-template name="length">
						<xsl:with-param name="minlength" select="$minLength" />
						<xsl:with-param name="maxlength" select="$maxLength" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:if test="$lang='en-us'">
						<td nowrap="nowrap">Text</td>
						<td></td>
					</xsl:if>
					<xsl:if test="$lang='zh-cn'">
						<td nowrap="nowrap">文本</td>
						<td></td>
					</xsl:if>
					<xsl:if test="$lang='zh-hk'">
						<td nowrap="nowrap">文本</td>
						<td></td>
					</xsl:if>
				</tr>
			</xsl:when>
			<!-- 微信 -->
			<xsl:when test="$id='extension_42'">
				<tr>
					<td nowrap="nowrap">
						<xsl:value-of select="$label" />
					</td>
					<xsl:call-template name="allowEmpty">
						<xsl:with-param name="empty" select="$allowEmpty" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:call-template name="length">
						<xsl:with-param name="minlength" select="$minLength" />
						<xsl:with-param name="maxlength" select="$maxLength" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:if test="$lang='en-us'">
						<td nowrap="nowrap">Text</td>
						<td></td>
					</xsl:if>
					<xsl:if test="$lang='zh-cn'">
						<td nowrap="nowrap">文本</td>
						<td></td>
					</xsl:if>
					<xsl:if test="$lang='zh-hk'">
						<td nowrap="nowrap">文本</td>
						<td></td>
					</xsl:if>
				</tr>
			</xsl:when>
			<!-- 职称 -->
			<xsl:when test="$id='job_title'">
				<tr>
					<td nowrap="nowrap">
						<xsl:value-of select="$label" />
					</td>
					<xsl:call-template name="allowEmpty">
						<xsl:with-param name="empty" select="$allowEmpty" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:call-template name="length">
						<xsl:with-param name="minlength" select="$minLength" />
						<xsl:with-param name="maxlength" select="$maxLength" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:if test="$lang='en-us'">
						<td nowrap="nowrap">Text</td>
						<td></td>
					</xsl:if>
					<xsl:if test="$lang='zh-cn'">
						<td nowrap="nowrap">文本</td>
						<td></td>
					</xsl:if>
					<xsl:if test="$lang='zh-hk'">
						<td nowrap="nowrap">文本</td>
						<td></td>
					</xsl:if>
				</tr>
			</xsl:when>
			<!-- 加入公司日期 -->
			<xsl:when test="$id='join_date'">
				<tr>
					<td nowrap="nowrap">
						<xsl:value-of select="$label" />
					</td>
					<xsl:call-template name="allowEmpty">
						<xsl:with-param name="empty" select="$allowEmpty" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:call-template name="length">
						<xsl:with-param name="minlength" select="$minLength" />
						<xsl:with-param name="maxlength" select="$maxLength" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:if test="$lang='en-us'">
						<td nowrap="nowrap">YYYY/MM/DD</td>
						<td>Set to time format "YYYY/MM/DD"</td>
					</xsl:if>
					<xsl:if test="$lang='zh-cn'">
						<td nowrap="nowrap">YYYY/MM/DD</td>
						<td>设置单元格格式为"YYYY/MM/DD"</td>
					</xsl:if>
					<xsl:if test="$lang='zh-hk'">
						<td nowrap="nowrap">YYYY/MM/DD</td>
						<td>設置單元格格式爲"YYYY/MM/DD"</td>
					</xsl:if>
				</tr>
			</xsl:when>
			<!-- 来源 -->
			<xsl:when test="$id='source'">
				<tr>
					<td nowrap="nowrap">
						<xsl:value-of select="$label" />
					</td>
					<xsl:call-template name="allowEmpty">
						<xsl:with-param name="empty" select="$allowEmpty" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:call-template name="length">
						<xsl:with-param name="minlength" select="$minLength" />
						<xsl:with-param name="maxlength" select="$maxLength" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:if test="$lang='en-us'">
						<td nowrap="nowrap">Text</td>
						<td></td>
					</xsl:if>
					<xsl:if test="$lang='zh-cn'">
						<td nowrap="nowrap">文本</td>
						<td></td>
					</xsl:if>
					<xsl:if test="$lang='zh-hk'">
						<td nowrap="nowrap">文本</td>
						<td></td>
					</xsl:if>
				</tr>
			</xsl:when>
			<!-- 账号有效期 -->
			<xsl:when test="$id='extension_11'">
				<tr>
					<td nowrap="nowrap">
						<xsl:value-of select="$label" />
					</td>
					<xsl:call-template name="allowEmpty">
						<xsl:with-param name="empty" select="$allowEmpty" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:call-template name="length">
						<xsl:with-param name="minlength" select="$minLength" />
						<xsl:with-param name="maxlength" select="$maxLength" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:if test="$lang='en-us'">
						<td nowrap="nowrap">YYYY/MM/DD</td>
						<td>Set to time format "YYYY/MM/DD"<xsl:if test="$isTcIndependent='true' or $isTcIndependent=true">,this field is not valid in learnnow mode</xsl:if></td>
					</xsl:if>
					<xsl:if test="$lang='zh-cn'">
						<td nowrap="nowrap">YYYY/MM/DD</td>
						<td>设置单元格格式为"YYYY/MM/DD"<xsl:if test="$isTcIndependent='true' or $isTcIndependent=true">，该字段learnnow模式下无效</xsl:if></td>
					</xsl:if>
					<xsl:if test="$lang='zh-hk'">
						<td nowrap="nowrap">YYYY/MM/DD</td>
						<td>設置單元格格式爲"YYYY/MM/DD"<xsl:if test="$isTcIndependent='true' or $isTcIndependent=true">，該字段learnnow模式下無效</xsl:if></td>
					</xsl:if>
				</tr>

			</xsl:when>
			<!-- 个人头像 -->
			<xsl:when test="$id='extension_43'">
				<tr>
					<td nowrap="nowrap">
						<xsl:value-of select="$label" />
					</td>
					<xsl:call-template name="allowEmpty">
						<xsl:with-param name="empty" select="$allowEmpty" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:call-template name="length">
						<xsl:with-param name="minlength" select="$minLength" />
						<xsl:with-param name="maxlength" select="$maxLength" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:if test="$lang='en-us'">
						<td nowrap="nowrap">Text</td>
						<td></td>
					</xsl:if>
					<xsl:if test="$lang='zh-cn'">
						<td nowrap="nowrap">文本</td>
						<td></td>
					</xsl:if>
					<xsl:if test="$lang='zh-hk'">
						<td nowrap="nowrap">文本</td>
						<td></td>
					</xsl:if>
				</tr>
			</xsl:when>

			<!-- 个人描述 -->
			<xsl:when test="$id='extension_44'">
				<tr>
					<td nowrap="nowrap">
						<xsl:value-of select="$label" />
					</td>
					<xsl:call-template name="allowEmpty">
						<xsl:with-param name="empty" select="$allowEmpty" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:call-template name="length">
						<xsl:with-param name="minlength" select="$minLength" />
						<xsl:with-param name="maxlength" select="$maxLength" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:if test="$lang='en-us'">
						<td nowrap="nowrap">Text</td>
						<td></td>
					</xsl:if>
					<xsl:if test="$lang='zh-cn'">
						<td nowrap="nowrap">文本</td>
						<td></td>
					</xsl:if>
					<xsl:if test="$lang='zh-hk'">
						<td nowrap="nowrap">文本</td>
						<td></td>
					</xsl:if>
				</tr>
			</xsl:when>

			<!-- 兴趣 -->
			<xsl:when test="$id='extension_45'">
				<tr>
					<td nowrap="nowrap">
						<xsl:value-of select="$label" />
					</td>
					<xsl:call-template name="allowEmpty">
						<xsl:with-param name="empty" select="$allowEmpty" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:call-template name="length">
						<xsl:with-param name="minlength" select="$minLength" />
						<xsl:with-param name="maxlength" select="$maxLength" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:if test="$lang='en-us'">
						<td nowrap="nowrap">Text</td>
						<td></td>
					</xsl:if>
					<xsl:if test="$lang='zh-cn'">
						<td nowrap="nowrap">文本</td>
						<td></td>
					</xsl:if>
					<xsl:if test="$lang='zh-hk'">
						<td nowrap="nowrap">文本</td>
						<td></td>
					</xsl:if>
				</tr>
			</xsl:when>

			<!-- 一级用户组编号 -->
			<xsl:when test="$id='group_code_level1'">
				<tr>
					<td nowrap="nowrap">
						<xsl:value-of select="$label" />
					</td>
					<xsl:call-template name="allowEmpty">
						<xsl:with-param name="empty" select="$allowEmpty" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:call-template name="length">
						<xsl:with-param name="minlength" select="$minLength" />
						<xsl:with-param name="maxlength" select="$maxLength" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:if test="$lang='en-us'">
						<td nowrap="nowrap">Text</td>
						<td>
							Please use the corresponding code (not title). Please look up the codes in <b>User Management</b> &gt; <b>User profile</b> in the learning platform.
						</td>
					</xsl:if>
					<xsl:if test="$lang='zh-cn'">
						<td nowrap="nowrap">文本</td>
						<td>
							请填写该用户所在用户组的编号（而不是标题）。具体编号请到学习平台的“<b>用户管理</b> > <b>用户信息</b>”中查看。
						</td>
					</xsl:if>
					<xsl:if test="$lang='zh-hk'">
						<td nowrap="nowrap">文本</td>
						<td>
							請填寫該用戶所在用戶組的編號（而不是標題）。具體編號請到學習平臺的“<b>用户管理</b> > <b>用戶資訊</b>”中查看。
						</td>
					</xsl:if>
				</tr>
			</xsl:when>
			<!-- 一级用户组标题 -->
			<xsl:when test="$id='group_title_level1'">
				<tr>
					<td nowrap="nowrap">
						<xsl:value-of select="$label" />
					</td>
					<xsl:call-template name="allowEmpty">
						<xsl:with-param name="empty" select="$allowEmpty" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:call-template name="length">
						<xsl:with-param name="minlength" select="$minLength" />
						<xsl:with-param name="maxlength" select="$maxLength" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:if test="$lang='en-us'">
						<td nowrap="nowrap">Text</td>
						<td></td>
					</xsl:if>
					<xsl:if test="$lang='zh-cn'">
						<td nowrap="nowrap">文本</td>
						<td></td>
					</xsl:if>
					<xsl:if test="$lang='zh-hk'">
						<td nowrap="nowrap">文本</td>
						<td></td>
					</xsl:if>
				</tr>
			</xsl:when>
			<!-- 二级用户组编号 -->
			<xsl:when test="$id='group_code_level2'">
				<tr>
					<td nowrap="nowrap">
						<xsl:value-of select="$label" />
					</td>
					<xsl:call-template name="allowEmpty">
						<xsl:with-param name="empty" select="$allowEmpty" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:call-template name="length">
						<xsl:with-param name="minlength" select="$minLength" />
						<xsl:with-param name="maxlength" select="$maxLength" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:if test="$lang='en-us'">
						<td nowrap="nowrap">Text</td>
						<td></td>
					</xsl:if>
					<xsl:if test="$lang='zh-cn'">
						<td nowrap="nowrap">文本</td>
						<td></td>
					</xsl:if>
					<xsl:if test="$lang='zh-hk'">
						<td nowrap="nowrap">文本</td>
						<td></td>
					</xsl:if>
				</tr>
			</xsl:when>
			<!-- 二级用户组标题 -->
			<xsl:when test="$id='group_title_level2'">
				<tr>
					<td nowrap="nowrap">
						<xsl:value-of select="$label" />
					</td>
					<xsl:call-template name="allowEmpty">
						<xsl:with-param name="empty" select="$allowEmpty" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:call-template name="length">
						<xsl:with-param name="minlength" select="$minLength" />
						<xsl:with-param name="maxlength" select="$maxLength" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:if test="$lang='en-us'">
						<td nowrap="nowrap">Text</td>
						<td></td>
					</xsl:if>
					<xsl:if test="$lang='zh-cn'">
						<td nowrap="nowrap">文本</td>
						<td></td>
					</xsl:if>
					<xsl:if test="$lang='zh-hk'">
						<td nowrap="nowrap">文本</td>
						<td></td>
					</xsl:if>
				</tr>
			</xsl:when>
			<!-- 三级用户组编号 -->
			<xsl:when test="$id='group_code_level3'">
				<tr>
					<td nowrap="nowrap">
						<xsl:value-of select="$label" />
					</td>
					<xsl:call-template name="allowEmpty">
						<xsl:with-param name="empty" select="$allowEmpty" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:call-template name="length">
						<xsl:with-param name="minlength" select="$minLength" />
						<xsl:with-param name="maxlength" select="$maxLength" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:if test="$lang='en-us'">
						<td nowrap="nowrap">Text</td>
						<td></td>
					</xsl:if>
					<xsl:if test="$lang='zh-cn'">
						<td nowrap="nowrap">文本</td>
						<td></td>
					</xsl:if>
					<xsl:if test="$lang='zh-hk'">
						<td nowrap="nowrap">文本</td>
						<td></td>
					</xsl:if>
				</tr>
			</xsl:when>
			<!-- 三级用户组标题 -->
			<xsl:when test="$id='group_code_level3'">
				<tr>
					<td nowrap="nowrap">
						<xsl:value-of select="$label" />
					</td>
					<xsl:call-template name="allowEmpty">
						<xsl:with-param name="empty" select="$allowEmpty" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:call-template name="length">
						<xsl:with-param name="minlength" select="$minLength" />
						<xsl:with-param name="maxlength" select="$maxLength" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:if test="$lang='en-us'">
						<td nowrap="nowrap">Text</td>
						<td></td>
					</xsl:if>
					<xsl:if test="$lang='zh-cn'">
						<td nowrap="nowrap">文本</td>
						<td></td>
					</xsl:if>
					<xsl:if test="$lang='zh-hk'">
						<td nowrap="nowrap">文本</td>
						<td></td>
					</xsl:if>
				</tr>
			</xsl:when>
			<!-- 四级用户组编号 -->
			<xsl:when test="$id='group_code_level4'">
				<tr>
					<td nowrap="nowrap">
						<xsl:value-of select="$label" />
					</td>
					<xsl:call-template name="allowEmpty">
						<xsl:with-param name="empty" select="$allowEmpty" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:call-template name="length">
						<xsl:with-param name="minlength" select="$minLength" />
						<xsl:with-param name="maxlength" select="$maxLength" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:if test="$lang='en-us'">
						<td nowrap="nowrap">Text</td>
						<td></td>
					</xsl:if>
					<xsl:if test="$lang='zh-cn'">
						<td nowrap="nowrap">文本</td>
						<td></td>
					</xsl:if>
					<xsl:if test="$lang='zh-hk'">
						<td nowrap="nowrap">文本</td>
						<td></td>
					</xsl:if>
				</tr>
			</xsl:when>
			<!-- 四级用户组标题 -->
			<xsl:when test="$id='group_title_level4'">
				<tr>
					<td nowrap="nowrap">
						<xsl:value-of select="$label" />
					</td>
					<xsl:call-template name="allowEmpty">
						<xsl:with-param name="empty" select="$allowEmpty" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:call-template name="length">
						<xsl:with-param name="minlength" select="$minLength" />
						<xsl:with-param name="maxlength" select="$maxLength" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:if test="$lang='en-us'">
						<td nowrap="nowrap">Text</td>
						<td></td>
					</xsl:if>
					<xsl:if test="$lang='zh-cn'">
						<td nowrap="nowrap">文本</td>
						<td></td>
					</xsl:if>
					<xsl:if test="$lang='zh-hk'">
						<td nowrap="nowrap">文本</td>
						<td></td>
					</xsl:if>
				</tr>
			</xsl:when>
			<!-- 五级用户组编号 -->
			<xsl:when test="$id='group_code_level5'">
				<tr>
					<td nowrap="nowrap">
						<xsl:value-of select="$label" />
					</td>
					<xsl:call-template name="allowEmpty">
						<xsl:with-param name="empty" select="$allowEmpty" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:call-template name="length">
						<xsl:with-param name="minlength" select="$minLength" />
						<xsl:with-param name="maxlength" select="$maxLength" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:if test="$lang='en-us'">
						<td nowrap="nowrap">Text</td>
						<td></td>
					</xsl:if>
					<xsl:if test="$lang='zh-cn'">
						<td nowrap="nowrap">文本</td>
						<td></td>
					</xsl:if>
					<xsl:if test="$lang='zh-hk'">
						<td nowrap="nowrap">文本</td>
						<td></td>
					</xsl:if>
				</tr>
			</xsl:when>
			<!-- 五级用户组标题 -->
			<xsl:when test="$id='group_title_level5'">
				<tr>
					<td nowrap="nowrap">
						<xsl:value-of select="$label" />
					</td>
					<xsl:call-template name="allowEmpty">
						<xsl:with-param name="empty" select="$allowEmpty" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:call-template name="length">
						<xsl:with-param name="minlength" select="$minLength" />
						<xsl:with-param name="maxlength" select="$maxLength" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:if test="$lang='en-us'">
						<td nowrap="nowrap">Text</td>
						<td></td>
					</xsl:if>
					<xsl:if test="$lang='zh-cn'">
						<td nowrap="nowrap">文本</td>
						<td></td>
					</xsl:if>
					<xsl:if test="$lang='zh-hk'">
						<td nowrap="nowrap">文本</td>
						<td></td>
					</xsl:if>
				</tr>
			</xsl:when>
			<!-- 六级用户组编号 -->
			<xsl:when test="$id='group_code_level6'">
				<tr>
					<td nowrap="nowrap">
						<xsl:value-of select="$label" />
					</td>
					<xsl:call-template name="allowEmpty">
						<xsl:with-param name="empty" select="$allowEmpty" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:call-template name="length">
						<xsl:with-param name="minlength" select="$minLength" />
						<xsl:with-param name="maxlength" select="$maxLength" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:if test="$lang='en-us'">
						<td nowrap="nowrap">Text</td>
						<td></td>
					</xsl:if>
					<xsl:if test="$lang='zh-cn'">
						<td nowrap="nowrap">文本</td>
						<td></td>
					</xsl:if>
					<xsl:if test="$lang='zh-hk'">
						<td nowrap="nowrap">文本</td>
						<td></td>
					</xsl:if>
				</tr>
			</xsl:when>
			<!-- 六级用户组标题 -->
			<xsl:when test="$id='group_title_level6'">
				<tr>
					<td nowrap="nowrap">
						<xsl:value-of select="$label" />
					</td>
					<xsl:call-template name="allowEmpty">
						<xsl:with-param name="empty" select="$allowEmpty" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:call-template name="length">
						<xsl:with-param name="minlength" select="$minLength" />
						<xsl:with-param name="maxlength" select="$maxLength" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:if test="$lang='en-us'">
						<td nowrap="nowrap">Text</td>
						<td></td>
					</xsl:if>
					<xsl:if test="$lang='zh-cn'">
						<td nowrap="nowrap">文本</td>
						<td></td>
					</xsl:if>
					<xsl:if test="$lang='zh-hk'">
						<td nowrap="nowrap">文本</td>
						<td></td>
					</xsl:if>
				</tr>
			</xsl:when>
			<!-- 七级用户组编号 -->
			<xsl:when test="$id='group_code_level7'">
				<tr>
					<td nowrap="nowrap">
						<xsl:value-of select="$label" />
					</td>
					<xsl:call-template name="allowEmpty">
						<xsl:with-param name="empty" select="$allowEmpty" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:call-template name="length">
						<xsl:with-param name="minlength" select="$minLength" />
						<xsl:with-param name="maxlength" select="$maxLength" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:if test="$lang='en-us'">
						<td nowrap="nowrap">Text</td>
						<td></td>
					</xsl:if>
					<xsl:if test="$lang='zh-cn'">
						<td nowrap="nowrap">文本</td>
						<td></td>
					</xsl:if>
					<xsl:if test="$lang='zh-hk'">
						<td nowrap="nowrap">文本</td>
						<td></td>
					</xsl:if>
				</tr>
			</xsl:when>
			<!-- 七级用户组标题 -->
			<xsl:when test="$id='group_title_level7'">
				<tr>
					<td nowrap="nowrap">
						<xsl:value-of select="$label" />
					</td>
					<xsl:call-template name="allowEmpty">
						<xsl:with-param name="empty" select="$allowEmpty" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:call-template name="length">
						<xsl:with-param name="minlength" select="$minLength" />
						<xsl:with-param name="maxlength" select="$maxLength" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:if test="$lang='en-us'">
						<td nowrap="nowrap">Text</td>
						<td></td>
					</xsl:if>
					<xsl:if test="$lang='zh-cn'">
						<td nowrap="nowrap">文本</td>
						<td></td>
					</xsl:if>
					<xsl:if test="$lang='zh-hk'">
						<td nowrap="nowrap">文本</td>
						<td></td>
					</xsl:if>
				</tr>
			</xsl:when>
			<!-- 一级职级编号 -->
			<xsl:when test="$id='grade_code_level1'">
				<tr>
					<td nowrap="nowrap">
						<xsl:value-of select="$label" />
					</td>
					<xsl:call-template name="allowEmpty">
						<xsl:with-param name="empty" select="$allowEmpty" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:call-template name="length">
						<xsl:with-param name="minlength" select="$minLength" />
						<xsl:with-param name="maxlength" select="$maxLength" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:if test="$lang='en-us'">
						<td nowrap="nowrap">Text</td>
						<td>
							Please use the corresponding code (not title). Please look up the codes in <b>User Management</b> &gt; <b>Grade maintenance</b> in the learning platform.
						</td>
					</xsl:if>
					<xsl:if test="$lang='zh-cn'">
						<td nowrap="nowrap">文本</td>
						<td>
							请填写该用户的职级编号（而不是标题）。具体编号请到学习平台的“<b>用户管理</b> > <b>职级管理</b>”中查看。
						</td>
					</xsl:if>
					<xsl:if test="$lang='zh-hk'">
						<td nowrap="nowrap">文本</td>
						<td>
							請填寫該用戶的職級編號（而不是標題）。具體編號請到學習平台的“<b>用戶管理</b> &gt; <b>職級管理</b>”中查看。
						</td>
					</xsl:if>
				</tr>
			</xsl:when>
			<!-- 一级职级标题 -->
			<xsl:when test="$id='grade_title_level1'">
				<tr>
					<td nowrap="nowrap">
						<xsl:value-of select="$label" />
					</td>
					<xsl:call-template name="allowEmpty">
						<xsl:with-param name="empty" select="$allowEmpty" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:call-template name="length">
						<xsl:with-param name="minlength" select="$minLength" />
						<xsl:with-param name="maxlength" select="$maxLength" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:if test="$lang='en-us'">
						<td nowrap="nowrap">Text</td>
						<td></td>
					</xsl:if>
					<xsl:if test="$lang='zh-cn'">
						<td nowrap="nowrap">文本</td>
						<td></td>
					</xsl:if>
					<xsl:if test="$lang='zh-hk'">
						<td nowrap="nowrap">文本</td>
						<td></td>
					</xsl:if>
				</tr>
			</xsl:when>
			<!-- 二级职级编号 -->
			<xsl:when test="$id='grade_code_level2'">
				<tr>
					<td nowrap="nowrap">
						<xsl:value-of select="$label" />
					</td>
					<xsl:call-template name="allowEmpty">
						<xsl:with-param name="empty" select="$allowEmpty" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:call-template name="length">
						<xsl:with-param name="minlength" select="$minLength" />
						<xsl:with-param name="maxlength" select="$maxLength" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:if test="$lang='en-us'">
						<td nowrap="nowrap">Text</td>
						<td></td>
					</xsl:if>
					<xsl:if test="$lang='zh-cn'">
						<td nowrap="nowrap">文本</td>
						<td></td>
					</xsl:if>
					<xsl:if test="$lang='zh-hk'">
						<td nowrap="nowrap">文本</td>
						<td></td>
					</xsl:if>
				</tr>
			</xsl:when>
			<!-- 二级职级标题 -->
			<xsl:when test="$id='grade_title_level2'">
				<tr>
					<td nowrap="nowrap">
						<xsl:value-of select="$label" />
					</td>
					<xsl:call-template name="allowEmpty">
						<xsl:with-param name="empty" select="$allowEmpty" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:call-template name="length">
						<xsl:with-param name="minlength" select="$minLength" />
						<xsl:with-param name="maxlength" select="$maxLength" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:if test="$lang='en-us'">
						<td nowrap="nowrap">Text</td>
						<td></td>
					</xsl:if>
					<xsl:if test="$lang='zh-cn'">
						<td nowrap="nowrap">文本</td>
						<td></td>
					</xsl:if>
					<xsl:if test="$lang='zh-hk'">
						<td nowrap="nowrap">文本</td>
						<td></td>
					</xsl:if>
				</tr>
			</xsl:when>
			<!-- 三级职级编号 -->
			<xsl:when test="$id='grade_code_level3'">
				<tr>
					<td nowrap="nowrap">
						<xsl:value-of select="$label" />
					</td>
					<xsl:call-template name="allowEmpty">
						<xsl:with-param name="empty" select="$allowEmpty" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:call-template name="length">
						<xsl:with-param name="minlength" select="$minLength" />
						<xsl:with-param name="maxlength" select="$maxLength" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:if test="$lang='en-us'">
						<td nowrap="nowrap">Text</td>
						<td></td>
					</xsl:if>
					<xsl:if test="$lang='zh-cn'">
						<td nowrap="nowrap">文本</td>
						<td></td>
					</xsl:if>
					<xsl:if test="$lang='zh-hk'">
						<td nowrap="nowrap">文本</td>
						<td></td>
					</xsl:if>
				</tr>
			</xsl:when>
			<!-- 三级职级标题 -->
			<xsl:when test="$id='grade_title_level3'">
				<tr>
					<td nowrap="nowrap">
						<xsl:value-of select="$label" />
					</td>
					<xsl:call-template name="allowEmpty">
						<xsl:with-param name="empty" select="$allowEmpty" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:call-template name="length">
						<xsl:with-param name="minlength" select="$minLength" />
						<xsl:with-param name="maxlength" select="$maxLength" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:if test="$lang='en-us'">
						<td nowrap="nowrap">Text</td>
						<td></td>
					</xsl:if>
					<xsl:if test="$lang='zh-cn'">
						<td nowrap="nowrap">文本</td>
						<td></td>
					</xsl:if>
					<xsl:if test="$lang='zh-hk'">
						<td nowrap="nowrap">文本</td>
						<td></td>
					</xsl:if>
				</tr>
			</xsl:when>
			<!-- 岗位 -->
			<xsl:when test="$id='competency'">
				<tr>
				   <xsl:if test="$lang='en-us'">
						<td>Position</td>
					</xsl:if>
					<xsl:if test="$lang='zh-cn'">
						<td>岗位编号</td>
					</xsl:if>
					<xsl:if test="$lang='zh-hk'">
						<td>崗位編號</td>
					</xsl:if>
					<xsl:call-template name="allowEmpty">
						<xsl:with-param name="empty" select="$allowEmpty" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:call-template name="length">
						<xsl:with-param name="minlength" select="$minLength" />
						<xsl:with-param name="maxlength" select="$maxLength" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<xsl:if test="$lang='en-us'">
						<td nowrap="nowrap">Text</td>
						<td>Please fill in the Position code. You can check the specific Position code in "<b>User management</b> > <b>Position management</b>" . 
						    <br /> Please enter the specify Position code if the Position already exist. A new position record will be created if the position does not exist in the system.
						</td>
					</xsl:if>
					<xsl:if test="$lang='zh-cn'">
						<td nowrap="nowrap">文本</td>
						<td>请指定用户所在岗位的编号。如果系统存在该岗位，不会进行任何操作，否则会根据您填写的岗位进行新增操作。</td>
					</xsl:if>
					<xsl:if test="$lang='zh-hk'">
						<td nowrap="nowrap">文本</td>
						<td>請指定用戶所在崗位的編號。如果系統存在該崗位，不會進行任何操作，否則會根據您填寫的崗位進行新增操作。</td>
					</xsl:if>
				</tr>
			</xsl:when>
			<!-- 角色 -->
			<xsl:when test="$id='role'">
				<tr>
					 <xsl:if test="$lang='en-us'">
						<td>Role</td>
					</xsl:if>
					<xsl:if test="$lang='zh-cn'">
						<td>角色代码</td>
					</xsl:if>
					<xsl:if test="$lang='zh-hk'">
						<td>角色代碼</td>
					</xsl:if>
					<xsl:call-template name="allowEmpty">
						<xsl:with-param name="empty" select="$allowEmpty" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<!-- 
					<xsl:call-template name="length">
						<xsl:with-param name="minlength" select="$minLength" />
						<xsl:with-param name="maxlength" select="$maxLength" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					 -->
				 	<td nowrap="nowrap" align="center">--</td>
					<xsl:if test="$lang='en-us'">
						<td nowrap="nowrap">Text</td>
						<td>
							Please specify the role(s) of the user (use "," for multiple roles, e.g. LRNR,TADM):
							<br />
							'LRNR' for Learners
							<br />
							'TADM' for Training Administrator
							<br />
							'SADM' for System Administrator
							<br />
							'EXA' for Examiner
							<br />
							For new user accounts:<br />
                            If role is not specified, assigned to "LRNR" by default.
							<br />
							For existing user accounts:<br />
                            If role is not specified, it is assumed to keep orginal role.
					
						</td>
					</xsl:if>
					<xsl:if test="$lang='zh-cn'">
						<td nowrap="nowrap">文本</td>
						<td>
							请填写对应的代码（如果用户具有多于一个角色，请用英文逗号分隔）：
							<br />
							学员－LRNR
							<br />
							培训管理员－TADM
							<br />
							系统管理员－SADM
							<br />
							考试监考员－EXA
							<br />
							如果角色没有填写时，系统默认角色为学员：LRNR
							<br />
							如果系统存在该用户，角色将不会进行更新。
						</td>
					</xsl:if>
					<xsl:if test="$lang='zh-hk'">
						<td nowrap="nowrap">文本</td>
						<td>
							請填寫對應的代碼（如果用戶具有多於一個角色，請用英文逗號分隔）：
							<br />
							學員－LRNR
							<br />
							培訓管理員－TADM
							<br />
							系統管理員－SADM
							<br />
							考試監考員－EXA
							<br />
							如果角色沒有填寫時，系統默認角色為學員：LRNR
							<br />
							如果系統存在該用戶，角色將不會進行更新。
						</td>
					</xsl:if>
				</tr>
			</xsl:when>
			<!-- 直属领导 -->
			<xsl:when test="$id='direct_supervisors'">
				<tr>
					<td nowrap="nowrap">
						<xsl:value-of select="$label" />
					</td>
					<xsl:call-template name="allowEmpty">
						<xsl:with-param name="empty" select="$allowEmpty" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<!-- 
					<xsl:call-template name="length">
						<xsl:with-param name="minlength" select="$minLength" />
						<xsl:with-param name="maxlength" select="$maxLength" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					 -->
					<td nowrap="nowrap" align="center">--</td>
					<xsl:if test="$lang='en-us'">
						<td nowrap="nowrap">Text</td>
						<td>
							Identified by User ID, use comma to separate them if more than 1
							User ID is required.
							<br />
							The line of text will be ignored if the User ID here is not
							exist.
						</td>
					</xsl:if>
					<xsl:if test="$lang='zh-cn'">
						<td nowrap="nowrap">文本</td>
						<td>请填写用户名，如果需要填写多个请用英文逗号分隔。如果该用户不存在的话,该行记录将不会被插入。</td>
					</xsl:if>
					<xsl:if test="$lang='zh-hk'">
						<td nowrap="nowrap">文本</td>
						<td>請填寫用戶名，如果需要填寫多個請用英文逗號分隔。如果該用戶不存在的話，該行記錄將不會被插入</td>
					</xsl:if>
				</tr>
			</xsl:when>
			<!-- 最高报名审批用户组 -->
			<xsl:when test="$id='app_approval_usg_ent_id'">
				<tr>
					 <xsl:if test="$lang='en-us'">
						<td>Highest approval group</td>
					</xsl:if>
					<xsl:if test="$lang='zh-cn'">
						<td>最高报名审批用户组编号</td>
					</xsl:if>
					<xsl:if test="$lang='zh-hk'">
						<td>最高報名<br/>審批用戶組編號</td>
					</xsl:if>
					<xsl:call-template name="allowEmpty">
						<xsl:with-param name="empty" select="$allowEmpty" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<!-- 
					<xsl:call-template name="length">
						<xsl:with-param name="minlength" select="$minLength" />
						<xsl:with-param name="maxlength" select="$maxLength" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					 -->
					<td nowrap="nowrap" align="center">--</td>
					<xsl:if test="$lang='en-us'">
						<td nowrap="nowrap">Text</td>
						<td>
						    Please fill in the Group code, each User group will auto assign a unique Group code in system. You can check the specific Group code in " <b>User management</b> > <b>User profile</b>"  .
						</td>
					</xsl:if>
					<xsl:if test="$lang='zh-cn'">
						<td nowrap="nowrap">文本</td>
						<td>
							请填写用户组编号，具体编号请到学习平台的“<b>用户管理</b> > <b>用户信息</b>”中查看。
						</td>
					</xsl:if>
					<xsl:if test="$lang='zh-hk'">
						<td nowrap="nowrap">文本</td>
						<td>
							請填寫用戶組編號，具體編號請到學習平台的“<b>用戶管理</b> > <b>用戶信息</b>”中查看。
						</td>
					</xsl:if>
				</tr>
			</xsl:when>
			<!-- 下属部门 -->
			<xsl:when test="$id='supervised_groups'">
				<tr>
					 <xsl:if test="$lang='en-us'">
						<td>Supervised groups</td>
					</xsl:if>
					<xsl:if test="$lang='zh-cn'">
						<td>下属部门编号</td>
					</xsl:if>
					<xsl:if test="$lang='zh-hk'">
						<td>管理部門編號</td>
					</xsl:if>
					<xsl:call-template name="allowEmpty">
						<xsl:with-param name="empty" select="$allowEmpty" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					<!-- 
					<xsl:call-template name="length">
						<xsl:with-param name="minlength" select="$minLength" />
						<xsl:with-param name="maxlength" select="$maxLength" />
						<xsl:with-param name="lang" select="$lang" />
					</xsl:call-template>
					 -->
					 <td nowrap="nowrap" align="center">--</td>
					<xsl:if test="$lang='en-us'">
						<td nowrap="nowrap">Text</td>
						<td>
							Please fill in the Group code, each User group will auto assign a unique Group code in system. You can check the specific Group code in " <b>User management</b> > <b>User profile</b>"  .
							<br />
							Use comma to separate them if more than 1 User Group is required to supervise. The line of text will be ignored if the User Group here is not exist. 
						</td>
					</xsl:if>
					<xsl:if test="$lang='zh-cn'">
						<td nowrap="nowrap">文本</td>
						<td>
							请填写用户组编号，如果需要填写多个请用英文逗号分隔。如果用户组不存在,该行记录将不会被插入。
						</td>
					</xsl:if>
					<xsl:if test="$lang='zh-hk'">
						<td nowrap="nowrap">文本</td>
						<td>
							請填寫用戶組編號，如果需要填寫多個請用英文逗號分隔。如果用戶組不存在,該行記錄將不會被插入。
						</td>
					</xsl:if>
				</tr>
			</xsl:when>

		</xsl:choose>
	</xsl:template>

	<xsl:template name="allowEmpty">
		<xsl:param name="empty" />
		<xsl:param name="lang" />
		<xsl:choose>
			<xsl:when test="$empty='1'">
				<xsl:if test="$lang='zh-hk'">
					<td nowrap="nowrap">是</td>
				</xsl:if>
				<xsl:if test="$lang='zh-cn'">
					<td nowrap="nowrap">是</td>
				</xsl:if>
				<xsl:if test="$lang='en-us'">
					<td nowrap="nowrap">Yes</td>
				</xsl:if>
			</xsl:when>
			<xsl:when test="$empty='0'">
				<xsl:if test="$lang='zh-hk'">
					<td nowrap="nowrap">否</td>
				</xsl:if>
				<xsl:if test="$lang='zh-cn'">
					<td nowrap="nowrap">否</td>
				</xsl:if>
				<xsl:if test="$lang='en-us'">
					<td nowrap="nowrap">No</td>
				</xsl:if>
			</xsl:when>
		</xsl:choose>
	</xsl:template>


	<xsl:template name="length">
		<xsl:param name="minlength" />
		<xsl:param name="maxlength" />
		<xsl:param name="lang" />
		<td nowrap="nowrap" align="right">
			<xsl:if test="@minlength!='0'">
				<xsl:value-of select="@minlength" />
				-
			</xsl:if>
			<xsl:value-of select="@maxlength" />
			(<xsl:if test="$lang='zh-hk'">
				字元
			</xsl:if>
			<xsl:if test="$lang='zh-cn'">
				字符
			</xsl:if>
			<xsl:if test="$lang='en-us'">
				Char
			</xsl:if>)
		</td>
	</xsl:template>

</xsl:stylesheet>