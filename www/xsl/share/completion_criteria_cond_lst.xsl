<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<!-- =============================================================== -->
	<xsl:template name="gen_comp_crt_cond_lst">
		<xsl:param name="name"/>
		<xsl:param name="lang"/>
		<xsl:param name="default"/>
		<xsl:param name="display"/>
		<xsl:choose>
			<xsl:when test="$lang='gb'">
				<xsl:call-template name="gen_cond_drop_down_lst">
					<xsl:with-param name="lab_cond_0">需阅读一段培训资料/影片</xsl:with-param>
					<xsl:with-param name="lab_cond_1">需浏览一个课件</xsl:with-param>
					<xsl:with-param name="lab_cond_2">需完成一个课件/已取得一个课件合格分数</xsl:with-param>
					<xsl:with-param name="lab_cond_3">需递交一项功课</xsl:with-param>
					<xsl:with-param name="lab_cond_4">需递交一份课程评估问卷</xsl:with-param>
					<xsl:with-param name="lab_cond_5">需参与一个讨论区</xsl:with-param>
					<xsl:with-param name="lab_cond_6">需参与一个聊天室</xsl:with-param>
					<xsl:with-param name="lab_cond_7">需参与一个解答栏</xsl:with-param>
					<xsl:with-param name="name" select="$name"/>
					<xsl:with-param name="default" select="$default"/>
					<xsl:with-param name="display" select="$display"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$lang='ch'">
				<xsl:call-template name="gen_cond_drop_down_lst">
					<xsl:with-param name="lab_cond_0">需閱讀一段培訓資料/影片</xsl:with-param>
					<xsl:with-param name="lab_cond_1">需瀏覽一個課件</xsl:with-param>
					<xsl:with-param name="lab_cond_2">需完成一個課件/已取得一個課件合格分數</xsl:with-param>
					<xsl:with-param name="lab_cond_3">需遞交一項作業</xsl:with-param>
					<xsl:with-param name="lab_cond_4">需遞交一份課程評估問卷</xsl:with-param>
					<xsl:with-param name="lab_cond_5">需參與一個討論區</xsl:with-param>
					<xsl:with-param name="lab_cond_6">需參與一個聊天室</xsl:with-param>
					<xsl:with-param name="lab_cond_7">需參與一個解答欄</xsl:with-param>
					<xsl:with-param name="name" select="$name"/>
					<xsl:with-param name="default" select="$default"/>
					<xsl:with-param name="display" select="$display"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="gen_cond_drop_down_lst">
					<xsl:with-param name="lab_cond_0">Viewed a training document/video clip</xsl:with-param>
					<xsl:with-param name="lab_cond_1">Attempted an eLearning courseware</xsl:with-param>
					<xsl:with-param name="lab_cond_2">Completed/passed an eLearning courseware</xsl:with-param>
					<xsl:with-param name="lab_cond_3">Submitted an assignment</xsl:with-param>
					<xsl:with-param name="lab_cond_4">Submitted a course evaluation form</xsl:with-param>
					<xsl:with-param name="lab_cond_5">Participated in a forum</xsl:with-param>
					<xsl:with-param name="lab_cond_6">Participated in a chatroom</xsl:with-param>
					<xsl:with-param name="lab_cond_7">Particpated in a Q&#38;A discussion</xsl:with-param>
					<xsl:with-param name="name" select="$name"/>
					<xsl:with-param name="default" select="$default"/>
					<xsl:with-param name="display" select="$display"/>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template name="gen_cond_drop_down_lst">
		<xsl:param name="lab_cond_0"/>
		<xsl:param name="lab_cond_1"/>
		<xsl:param name="lab_cond_2"/>
		<xsl:param name="lab_cond_3"/>
		<xsl:param name="lab_cond_4"/>
		<xsl:param name="lab_cond_5"/>
		<xsl:param name="lab_cond_6"/>
		<xsl:param name="lab_cond_7"/>
		<xsl:param name="name"/>
		<xsl:param name="default"/>
		<xsl:param name="display"/>
		<xsl:choose>
			<xsl:when test="$display = 'true'">
				<xsl:choose>
					<xsl:when test="$default=0">
						<xsl:value-of select="$lab_cond_0"/>
					</xsl:when>
					<xsl:when test="$default=1">
						<xsl:value-of select="$lab_cond_1"/>
					</xsl:when>
					<xsl:when test="$default=2">
						<xsl:value-of select="$lab_cond_2"/>
					</xsl:when>
					<xsl:when test="$default=3">
						<xsl:value-of select="$lab_cond_4"/>
					</xsl:when>
					<xsl:when test="$default=4">
						<xsl:value-of select="$lab_cond_4"/>
					</xsl:when>
					<xsl:when test="$default=5">
						<xsl:value-of select="$lab_cond_5"/>
					</xsl:when>
					<xsl:when test="$default=6">
						<xsl:value-of select="$lab_cond_6"/>
					</xsl:when>
					<xsl:when test="$default=7">
						<xsl:value-of select="$lab_cond_7"/>
					</xsl:when>
					<xsl:otherwise>- -</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<select class="Select" name="{$name}">
					<option value="">
						<xsl:if test="string-length($default)=0">
							<xsl:attribute name="selected">true</xsl:attribute>
						</xsl:if>
				- -
			</option>
					<option value="0">
						<xsl:if test="$default=0">
							<xsl:attribute name="selected">true</xsl:attribute>
						</xsl:if>
						<xsl:value-of select="$lab_cond_0"/>
					</option>
					<option value="1">
						<xsl:if test="$default=1">
							<xsl:attribute name="selected">true</xsl:attribute>
						</xsl:if>
						<xsl:value-of select="$lab_cond_1"/>
					</option>
					<option value="2">
						<xsl:if test="$default=2">
							<xsl:attribute name="selected">true</xsl:attribute>
						</xsl:if>
						<xsl:value-of select="$lab_cond_2"/>
					</option>
					<option value="3">
						<xsl:if test="$default=3">
							<xsl:attribute name="selected">true</xsl:attribute>
						</xsl:if>
						<xsl:value-of select="$lab_cond_3"/>
					</option>
					<option value="4">
						<xsl:if test="$default=4">
							<xsl:attribute name="selected">true</xsl:attribute>
						</xsl:if>
						<xsl:value-of select="$lab_cond_4"/>
					</option>
					<option value="5">
						<xsl:if test="$default=5">
							<xsl:attribute name="selected">true</xsl:attribute>
						</xsl:if>
						<xsl:value-of select="$lab_cond_5"/>
					</option>
					<option value="6">
						<xsl:if test="$default=6">
							<xsl:attribute name="selected">true</xsl:attribute>
						</xsl:if>
						<xsl:value-of select="$lab_cond_6"/>
					</option>
					<option value="7">
						<xsl:if test="$default=7">
							<xsl:attribute name="selected">true</xsl:attribute>
						</xsl:if>
						<xsl:value-of select="$lab_cond_7"/>
					</option>
				</select>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="gen_comp_crt_cond_lst2">
		<xsl:param name="name"/>
		<xsl:param name="lang"/>
		<xsl:param name="default"/>
		<xsl:param name="display"/>
		<xsl:choose>
			<xsl:when test="$lang='gb'">
				<xsl:call-template name="gen_cond_drop_down_lst2">
					<xsl:with-param name="lab_cond_0">需递交</xsl:with-param>
					<xsl:with-param name="lab_cond_1">需合格</xsl:with-param>
					<xsl:with-param name="name" select="$name"/>
					<xsl:with-param name="default" select="$default"/>
					<xsl:with-param name="display" select="$display"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$lang='ch'">
				<xsl:call-template name="gen_cond_drop_down_lst2">
					<xsl:with-param name="lab_cond_0">需遞交</xsl:with-param>
					<xsl:with-param name="lab_cond_1">需合格</xsl:with-param>
					<xsl:with-param name="name" select="$name"/>
					<xsl:with-param name="default" select="$default"/>
					<xsl:with-param name="display" select="$display"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="gen_cond_drop_down_lst2">
					<xsl:with-param name="lab_cond_0">Attempted the item</xsl:with-param>
					<xsl:with-param name="lab_cond_1">Passed the item</xsl:with-param>
					<xsl:with-param name="name" select="$name"/>
					<xsl:with-param name="default" select="$default"/>
					<xsl:with-param name="display" select="$display"/>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template name="gen_cond_drop_down_lst2">
		<xsl:param name="lab_cond_0"/>
		<xsl:param name="lab_cond_1"/>
		<xsl:param name="name"/>
		<xsl:param name="default"/>
		<xsl:param name="display"/>
		<xsl:choose>
			<xsl:when test="$display= 'true'">
				<xsl:choose>
					<xsl:when test="$default=0">
						<xsl:value-of select="$lab_cond_0"/>
					</xsl:when>
					<xsl:when test="$default=1">
						<xsl:value-of select="$lab_cond_1"/>
					</xsl:when>
					<xsl:otherwise>- -</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<select class="Select" name="{$name}" id="{$name}">
					<option value="">
						<xsl:if test="string-length($default)=0">
							<xsl:attribute name="selected">true</xsl:attribute>
						</xsl:if>
				- -
			</option>
					<option value="0">
						<xsl:if test="$default=0">
							<xsl:attribute name="selected">true</xsl:attribute>
						</xsl:if>
						<xsl:value-of select="$lab_cond_0"/>
					</option>
					<option value="1">
						<xsl:if test="$default=1">
							<xsl:attribute name="selected">true</xsl:attribute>
						</xsl:if>
						<xsl:value-of select="$lab_cond_1"/>
					</option>
				</select>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="gen_module_lst">
		<xsl:param name="name"/>
		<xsl:param name="default"/>
		<xsl:param name="options"/>
		<xsl:param name="display"/>
		<xsl:param name="func"/>
		<xsl:param name="p"/>
		<xsl:param name="style"/>
		<xsl:choose>
			<xsl:when test="$display = 'true'">
				<xsl:choose>
					<xsl:when test="$options/module[@id = $default]">
						<xsl:value-of select="$options/module[@id = $default]/title"/>
					</xsl:when>
					<xsl:otherwise>
					- -
			</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<select class="wzb-form-select" style="{$style}" name="{$name}" id="{$name}" onchange="{$func};changeStatus(this,'mod_status_{$p}','mod_pre_{$p}_status',this)">
					<option value="" id="">
						<xsl:if test="string-length($default)=0">
							<xsl:attribute name="selected">true</xsl:attribute>
						</xsl:if>
				- -
			</option>
					<xsl:for-each select="$options/module">
						<option value="{@id}" id="{type}">
							<xsl:if test="@id = $default">
								<xsl:attribute name="selected">true</xsl:attribute>
							</xsl:if>
							<xsl:value-of select="title"/>
						</option>
					</xsl:for-each>
				</select>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>
