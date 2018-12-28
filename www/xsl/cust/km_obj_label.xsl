<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<!-- Knowledge Object Type Label -->
	<xsl:template name="obj_label">
		<xsl:param name="site_id"/>
		<xsl:param name="label"/>
		<xsl:param name="format">xml</xsl:param>
		<xsl:param name="lang"/>
		<xsl:choose>
			<xsl:when test="$site_id='1'">
				<xsl:choose>
					<!-- DOCUMENT -->
					<xsl:when test="$label = 'DOCUMENT'">
						<xsl:choose>
							<xsl:when test="$format = 'xml'">
								<xsl:element name="desc">
									<xsl:attribute name="lan">ISO-8859-1</xsl:attribute>
									<xsl:attribute name="name">Document</xsl:attribute>
								</xsl:element>
								<xsl:element name="desc">
									<xsl:attribute name="lan">Big5</xsl:attribute>
									<xsl:attribute name="name">文件</xsl:attribute>
								</xsl:element>
								<xsl:element name="desc">
									<xsl:attribute name="lan">GB2312</xsl:attribute>
									<xsl:attribute name="name">文档</xsl:attribute>
								</xsl:element>
							</xsl:when>
							<xsl:when test="$format = 'xsl' or $format = 'html'">
								<xsl:choose>
									<xsl:when test="$lang='en'">Document</xsl:when>
									<xsl:when test="$lang='ch'">文件</xsl:when>
									<xsl:when test="$lang='gb'">文档</xsl:when>
								</xsl:choose>
							</xsl:when>
						</xsl:choose>
					</xsl:when>
					<!-- PROPOSAL -->
					<xsl:when test="$label = 'PROPOSAL'">
						<xsl:choose>
							<xsl:when test="$format = 'xml'">
								<xsl:element name="desc">
									<xsl:attribute name="lan">ISO-8859-1</xsl:attribute>
									<xsl:attribute name="name">Proposal</xsl:attribute>
								</xsl:element>
								<xsl:element name="desc">
									<xsl:attribute name="lan">Big5</xsl:attribute>
									<xsl:attribute name="name">建議書</xsl:attribute>
								</xsl:element>
								<xsl:element name="desc">
									<xsl:attribute name="lan">GB2312</xsl:attribute>
									<xsl:attribute name="name">建议书</xsl:attribute>
								</xsl:element>
							</xsl:when>
							<xsl:when test="$format = 'xsl' or $format = 'html'">
								<xsl:choose>
									<xsl:when test="$lang='en'">Proposal</xsl:when>
									<xsl:when test="$lang='ch'">建議書</xsl:when>
									<xsl:when test="$lang='gb'">建议书</xsl:when>
								</xsl:choose>
							</xsl:when>
						</xsl:choose>
					</xsl:when>
					<!-- PRESENTATION -->
					<xsl:when test="$label = 'PRESENTATION'">
						<xsl:choose>
							<xsl:when test="$format = 'xml'">
								<xsl:element name="desc">
									<xsl:attribute name="lan">ISO-8859-1</xsl:attribute>
									<xsl:attribute name="name">Presentation</xsl:attribute>
								</xsl:element>
								<xsl:element name="desc">
									<xsl:attribute name="lan">Big5</xsl:attribute>
									<xsl:attribute name="name">發表 </xsl:attribute>
								</xsl:element>
								<xsl:element name="desc">
									<xsl:attribute name="lan">GB2312</xsl:attribute>
									<xsl:attribute name="name">发表</xsl:attribute>
								</xsl:element>
							</xsl:when>
							<xsl:when test="$format = 'xsl' or $format = 'html'">
								<xsl:choose>
									<xsl:when test="$lang='en'">Presentation</xsl:when>
									<xsl:when test="$lang='ch'">發表 </xsl:when>
									<xsl:when test="$lang='gb'">发表</xsl:when>
								</xsl:choose>
							</xsl:when>
						</xsl:choose>
					</xsl:when>
					<!-- NEWS -->
					<xsl:when test="$label = 'NEWS'">
						<xsl:choose>
							<xsl:when test="$format  = 'xml'">
								<xsl:element name="desc">
									<xsl:attribute name="lan">ISO-8859-1</xsl:attribute>
									<xsl:attribute name="name">News</xsl:attribute>
								</xsl:element>
								<xsl:element name="desc">
									<xsl:attribute name="lan">Big5</xsl:attribute>
									<xsl:attribute name="name">消息</xsl:attribute>
								</xsl:element>
								<xsl:element name="desc">
									<xsl:attribute name="lan">GB2312</xsl:attribute>
									<xsl:attribute name="name">讯息</xsl:attribute>
								</xsl:element>
							</xsl:when>
							<xsl:when test="$format = 'xsl' or $format = 'html'">
								<xsl:choose>
									<xsl:when test="$lang='en'">News</xsl:when>
									<xsl:when test="$lang='ch'">消息</xsl:when>
									<xsl:when test="$lang='gb'">讯息</xsl:when>
								</xsl:choose>
							</xsl:when>
						</xsl:choose>
					</xsl:when>
					<!-- BUSINESS_CASE -->
					<xsl:when test="$label = 'BUSINESS_CASE'">
						<xsl:choose>
							<xsl:when test="$format  = 'xml'">
								<xsl:element name="desc">
									<xsl:attribute name="lan">ISO-8859-1</xsl:attribute>
									<xsl:attribute name="name">Business case</xsl:attribute>
								</xsl:element>
								<xsl:element name="desc">
									<xsl:attribute name="lan">Big5</xsl:attribute>
									<xsl:attribute name="name">公司事例</xsl:attribute>
								</xsl:element>
								<xsl:element name="desc">
									<xsl:attribute name="lan">GB2312</xsl:attribute>
									<xsl:attribute name="name">Business case</xsl:attribute>
								</xsl:element>
							</xsl:when>
							<xsl:when test="$format = 'xsl' or $format = 'html'">
								<xsl:choose>
									<xsl:when test="$lang='en'">Business case</xsl:when>
									<xsl:when test="$lang='ch'">公司事例</xsl:when>
									<xsl:when test="$lang='gb'">Business case</xsl:when>
								</xsl:choose>
							</xsl:when>
						</xsl:choose>
					</xsl:when>
					<!-- BOOK -->
					<xsl:when test="$label = 'KM_BOOK'">
						<xsl:choose>
							<xsl:when test="$format = 'xml'">
								<xsl:element name="desc">
									<xsl:attribute name="lan">ISO-8859-1</xsl:attribute>
									<xsl:attribute name="name">Book</xsl:attribute>
								</xsl:element>
								<xsl:element name="desc">
									<xsl:attribute name="lan">Big5</xsl:attribute>
									<xsl:attribute name="name">書本</xsl:attribute>
								</xsl:element>
								<xsl:element name="desc">
									<xsl:attribute name="lan">GB2312</xsl:attribute>
									<xsl:attribute name="name">Book</xsl:attribute>
								</xsl:element>
							</xsl:when>
							<xsl:when test="$format = 'xsl' or $format = 'html'">
								<xsl:choose>
									<xsl:when test="$lang='en'">Book</xsl:when>
									<xsl:when test="$lang='ch'">書本</xsl:when>
									<xsl:when test="$lang='gb'">Book</xsl:when>
								</xsl:choose>
							</xsl:when>
						</xsl:choose>
					</xsl:when>
					<!-- VIDEO -->
					<xsl:when test="$label = 'KM_VIDEO'">
						<xsl:choose>
							<xsl:when test="$format = 'xml'">
								<xsl:element name="desc">
									<xsl:attribute name="lan">ISO-8859-1</xsl:attribute>
									<xsl:attribute name="name">Video</xsl:attribute>
								</xsl:element>
								<xsl:element name="desc">
									<xsl:attribute name="lan">Big5</xsl:attribute>
									<xsl:attribute name="name">視像</xsl:attribute>
								</xsl:element>
								<xsl:element name="desc">
									<xsl:attribute name="lan">GB2312</xsl:attribute>
									<xsl:attribute name="name">Video</xsl:attribute>
								</xsl:element>
							</xsl:when>
							<xsl:when test="$format = 'xsl' or $format = 'html'">
								<xsl:choose>
									<xsl:when test="$lang='en'">Video</xsl:when>
									<xsl:when test="$lang='ch'">視像</xsl:when>
									<xsl:when test="$lang='gb'">Video</xsl:when>
								</xsl:choose>
							</xsl:when>
						</xsl:choose>
					</xsl:when>
					<!-- eBOOK -->
					<xsl:when test="$label = 'KM_EBOOK'">
						<xsl:choose>
							<xsl:when test="$format = 'xml'">
								<xsl:element name="desc">
									<xsl:attribute name="lan">ISO-8859-1</xsl:attribute>
									<xsl:attribute name="name">e-Book</xsl:attribute>
								</xsl:element>
								<xsl:element name="desc">
									<xsl:attribute name="lan">Big5</xsl:attribute>
									<xsl:attribute name="name">電子圖書</xsl:attribute>
								</xsl:element>
								<xsl:element name="desc">
									<xsl:attribute name="lan">GB2312</xsl:attribute>
									<xsl:attribute name="name">电子图书</xsl:attribute>
								</xsl:element>
							</xsl:when>
							<xsl:when test="$format = 'xsl' or $format = 'html'">
								<xsl:choose>
									<xsl:when test="$lang='en'">e-Book</xsl:when>
									<xsl:when test="$lang='ch'">電子圖書</xsl:when>
									<xsl:when test="$lang='gb'">电子图书</xsl:when>
								</xsl:choose>
							</xsl:when>
						</xsl:choose>
					</xsl:when>
					<!-- magazine -->
					<xsl:when test="$label = 'KM_MAGAZINE'">
						<xsl:choose>
							<xsl:when test="$format = 'xml'">
								<xsl:element name="desc">
									<xsl:attribute name="lan">ISO-8859-1</xsl:attribute>
									<xsl:attribute name="name">Magazine</xsl:attribute>
								</xsl:element>
								<xsl:element name="desc">
									<xsl:attribute name="lan">Big5</xsl:attribute>
									<xsl:attribute name="name">雜誌</xsl:attribute>
								</xsl:element>
								<xsl:element name="desc">
									<xsl:attribute name="lan">GB2312</xsl:attribute>
									<xsl:attribute name="name">杂志</xsl:attribute>
								</xsl:element>
							</xsl:when>
							<xsl:when test="$format = 'xsl' or $format = 'html'">
								<xsl:choose>
									<xsl:when test="$lang='en'">Magazine</xsl:when>
									<xsl:when test="$lang='ch'">雜誌</xsl:when>
									<xsl:when test="$lang='gb'">杂志</xsl:when>
								</xsl:choose>
							</xsl:when>
						</xsl:choose>
					</xsl:when>
					<!-- magazine ：Publication Frequency -->
					<xsl:when test="$label = 'Frequency'">
						<xsl:choose>
							<xsl:when test="$format = 'xml'">
								<xsl:element name="desc">
									<xsl:attribute name="lan">ISO-8859-1</xsl:attribute>
									<xsl:attribute name="name">Select a frequency</xsl:attribute>
								</xsl:element>
								<xsl:element name="desc">
									<xsl:attribute name="lan">Big5</xsl:attribute>
									<xsl:attribute name="name">請選擇出版週期</xsl:attribute>
								</xsl:element>
								<xsl:element name="desc">
									<xsl:attribute name="lan">GB2312</xsl:attribute>
									<xsl:attribute name="name">请选择出版周期</xsl:attribute>
								</xsl:element>
							</xsl:when>
							<xsl:when test="$format = 'xsl' or $format = 'html'">
								<xsl:choose>
									<xsl:when test="$lang='en'">Select a frequency</xsl:when>
									<xsl:when test="$lang='ch'">請選擇出版週期</xsl:when>
									<xsl:when test="$lang='gb'">请选择出版周期</xsl:when>
								</xsl:choose>
							</xsl:when>
						</xsl:choose>
					</xsl:when>
					<xsl:when test="$label = 'Annual'">
						<xsl:choose>
							<xsl:when test="$format = 'xml'">
								<xsl:element name="desc">
									<xsl:attribute name="lan">ISO-8859-1</xsl:attribute>
									<xsl:attribute name="name">Annual</xsl:attribute>
								</xsl:element>
								<xsl:element name="desc">
									<xsl:attribute name="lan">Big5</xsl:attribute>
									<xsl:attribute name="name">每年</xsl:attribute>
								</xsl:element>
								<xsl:element name="desc">
									<xsl:attribute name="lan">GB2312</xsl:attribute>
									<xsl:attribute name="name">每年</xsl:attribute>
								</xsl:element>
							</xsl:when>
							<xsl:when test="$format = 'xsl' or $format = 'html'">
								<xsl:choose>
									<xsl:when test="$lang='en'">Annual</xsl:when>
									<xsl:when test="$lang='ch'">每年</xsl:when>
									<xsl:when test="$lang='gb'">每年</xsl:when>
								</xsl:choose>
							</xsl:when>
						</xsl:choose>
					</xsl:when>
					<xsl:when test="$label = 'Bi-Annual'">
						<xsl:choose>
							<xsl:when test="$format = 'xml'">
								<xsl:element name="desc">
									<xsl:attribute name="lan">ISO-8859-1</xsl:attribute>
									<xsl:attribute name="name">Bi-annual</xsl:attribute>
								</xsl:element>
								<xsl:element name="desc">
									<xsl:attribute name="lan">Big5</xsl:attribute>
									<xsl:attribute name="name">每年兩次</xsl:attribute>
								</xsl:element>
								<xsl:element name="desc">
									<xsl:attribute name="lan">GB2312</xsl:attribute>
									<xsl:attribute name="name">每年两次</xsl:attribute>
								</xsl:element>
							</xsl:when>
							<xsl:when test="$format = 'xsl' or $format = 'html'">
								<xsl:choose>
									<xsl:when test="$lang='en'">Bi-annual</xsl:when>
									<xsl:when test="$lang='ch'">每年兩次</xsl:when>
									<xsl:when test="$lang='gb'">每年两次</xsl:when>
								</xsl:choose>
							</xsl:when>
						</xsl:choose>
					</xsl:when>
					<xsl:when test="$label = 'Bi-Monthly'">
						<xsl:choose>
							<xsl:when test="$format = 'xml'">
								<xsl:element name="desc">
									<xsl:attribute name="lan">ISO-8859-1</xsl:attribute>
									<xsl:attribute name="name">Bi-monthly</xsl:attribute>
								</xsl:element>
								<xsl:element name="desc">
									<xsl:attribute name="lan">Big5</xsl:attribute>
									<xsl:attribute name="name">每月兩次</xsl:attribute>
								</xsl:element>
								<xsl:element name="desc">
									<xsl:attribute name="lan">GB2312</xsl:attribute>
									<xsl:attribute name="name">每月两次</xsl:attribute>
								</xsl:element>
							</xsl:when>
							<xsl:when test="$format = 'xsl' or $format = 'html'">
								<xsl:choose>
									<xsl:when test="$lang='en'">Bi-monthly</xsl:when>
									<xsl:when test="$lang='ch'">每月兩次</xsl:when>
									<xsl:when test="$lang='gb'">每月两次</xsl:when>
								</xsl:choose>
							</xsl:when>
						</xsl:choose>
					</xsl:when>
					<xsl:when test="$label = 'Bi-Weekly'">
						<xsl:choose>
							<xsl:when test="$format = 'xml'">
								<xsl:element name="desc">
									<xsl:attribute name="lan">ISO-8859-1</xsl:attribute>
									<xsl:attribute name="name">Bi-weekly</xsl:attribute>
								</xsl:element>
								<xsl:element name="desc">
									<xsl:attribute name="lan">Big5</xsl:attribute>
									<xsl:attribute name="name">每星期兩次</xsl:attribute>
								</xsl:element>
								<xsl:element name="desc">
									<xsl:attribute name="lan">GB2312</xsl:attribute>
									<xsl:attribute name="name">每星期两次</xsl:attribute>
								</xsl:element>
							</xsl:when>
							<xsl:when test="$format = 'xsl' or $format = 'html'">
								<xsl:choose>
									<xsl:when test="$lang='en'">Bi-weekly</xsl:when>
									<xsl:when test="$lang='ch'">每星期兩次</xsl:when>
									<xsl:when test="$lang='gb'">每星期两次</xsl:when>
								</xsl:choose>
							</xsl:when>
						</xsl:choose>
					</xsl:when>
					<xsl:when test="$label = 'Weekly'">
						<xsl:choose>
							<xsl:when test="$format = 'xml'">
								<xsl:element name="desc">
									<xsl:attribute name="lan">ISO-8859-1</xsl:attribute>
									<xsl:attribute name="name">Weekly</xsl:attribute>
								</xsl:element>
								<xsl:element name="desc">
									<xsl:attribute name="lan">Big5</xsl:attribute>
									<xsl:attribute name="name">每星期一次</xsl:attribute>
								</xsl:element>
								<xsl:element name="desc">
									<xsl:attribute name="lan">GB2312</xsl:attribute>
									<xsl:attribute name="name">每星期一次</xsl:attribute>
								</xsl:element>
							</xsl:when>
							<xsl:when test="$format = 'xsl' or $format = 'html'">
								<xsl:choose>
									<xsl:when test="$lang='en'">Weekly</xsl:when>
									<xsl:when test="$lang='ch'">每星期一次</xsl:when>
									<xsl:when test="$lang='gb'">每星期一次</xsl:when>
								</xsl:choose>
							</xsl:when>
						</xsl:choose>
					</xsl:when>
					<xsl:when test="$label = 'Daily'">
						<xsl:choose>
							<xsl:when test="$format = 'xml'">
								<xsl:element name="desc">
									<xsl:attribute name="lan">ISO-8859-1</xsl:attribute>
									<xsl:attribute name="name">Daily</xsl:attribute>
								</xsl:element>
								<xsl:element name="desc">
									<xsl:attribute name="lan">Big5</xsl:attribute>
									<xsl:attribute name="name">每日</xsl:attribute>
								</xsl:element>
								<xsl:element name="desc">
									<xsl:attribute name="lan">GB2312</xsl:attribute>
									<xsl:attribute name="name">每日</xsl:attribute>
								</xsl:element>
							</xsl:when>
							<xsl:when test="$format = 'xsl' or $format = 'html'">
								<xsl:choose>
									<xsl:when test="$lang='en'">Daily</xsl:when>
									<xsl:when test="$lang='ch'">每日</xsl:when>
									<xsl:when test="$lang='gb'">每日</xsl:when>
								</xsl:choose>
							</xsl:when>
						</xsl:choose>
					</xsl:when>
					<xsl:when test="$label = 'Irregular'">
						<xsl:choose>
							<xsl:when test="$format = 'xml'">
								<xsl:element name="desc">
									<xsl:attribute name="lan">ISO-8859-1</xsl:attribute>
									<xsl:attribute name="name">Irregular</xsl:attribute>
								</xsl:element>
								<xsl:element name="desc">
									<xsl:attribute name="lan">Big5</xsl:attribute>
									<xsl:attribute name="name">不規則</xsl:attribute>
								</xsl:element>
								<xsl:element name="desc">
									<xsl:attribute name="lan">GB2312</xsl:attribute>
									<xsl:attribute name="name">不规则</xsl:attribute>
								</xsl:element>
							</xsl:when>
							<xsl:when test="$format = 'xsl' or $format = 'html'">
								<xsl:choose>
									<xsl:when test="$lang='en'">Irregular</xsl:when>
									<xsl:when test="$lang='ch'">不規則</xsl:when>
									<xsl:when test="$lang='gb'">不规则</xsl:when>
								</xsl:choose>
							</xsl:when>
						</xsl:choose>
					</xsl:when>
					<xsl:when test="$label = 'Monthly'">
						<xsl:choose>
							<xsl:when test="$format = 'xml'">
								<xsl:element name="desc">
									<xsl:attribute name="lan">ISO-8859-1</xsl:attribute>
									<xsl:attribute name="name">Monthly</xsl:attribute>
								</xsl:element>
								<xsl:element name="desc">
									<xsl:attribute name="lan">Big5</xsl:attribute>
									<xsl:attribute name="name">每月一次</xsl:attribute>
								</xsl:element>
								<xsl:element name="desc">
									<xsl:attribute name="lan">GB2312</xsl:attribute>
									<xsl:attribute name="name">每月一次</xsl:attribute>
								</xsl:element>
							</xsl:when>
							<xsl:when test="$format = 'xsl' or $format = 'html'">
								<xsl:choose>
									<xsl:when test="$lang='en'">Monthly</xsl:when>
									<xsl:when test="$lang='ch'">每月一次</xsl:when>
									<xsl:when test="$lang='gb'">每月一次</xsl:when>
								</xsl:choose>
							</xsl:when>
						</xsl:choose>
					</xsl:when>
					<xsl:when test="$label = 'Quarterly'">
						<xsl:choose>
							<xsl:when test="$format = 'xml'">
								<xsl:element name="desc">
									<xsl:attribute name="lan">ISO-8859-1</xsl:attribute>
									<xsl:attribute name="name">Quarterly</xsl:attribute>
								</xsl:element>
								<xsl:element name="desc">
									<xsl:attribute name="lan">Big5</xsl:attribute>
									<xsl:attribute name="name">每季</xsl:attribute>
								</xsl:element>
								<xsl:element name="desc">
									<xsl:attribute name="lan">GB2312</xsl:attribute>
									<xsl:attribute name="name">每季</xsl:attribute>
								</xsl:element>
							</xsl:when>
							<xsl:when test="$format = 'xsl' or $format = 'html'">
								<xsl:choose>
									<xsl:when test="$lang='en'">Quarterly</xsl:when>
									<xsl:when test="$lang='ch'">每季</xsl:when>
									<xsl:when test="$lang='gb'">每季</xsl:when>
								</xsl:choose>
							</xsl:when>
						</xsl:choose>
					</xsl:when>
					<xsl:when test="$label = 'Others'">
						<xsl:choose>
							<xsl:when test="$format = 'xml'">
								<xsl:element name="desc">
									<xsl:attribute name="lan">ISO-8859-1</xsl:attribute>
									<xsl:attribute name="name">Others</xsl:attribute>
								</xsl:element>
								<xsl:element name="desc">
									<xsl:attribute name="lan">Big5</xsl:attribute>
									<xsl:attribute name="name">其他</xsl:attribute>
								</xsl:element>
								<xsl:element name="desc">
									<xsl:attribute name="lan">GB2312</xsl:attribute>
									<xsl:attribute name="name">其他</xsl:attribute>
								</xsl:element>
							</xsl:when>
							<xsl:when test="$format = 'xsl' or $format = 'html'">
								<xsl:choose>
									<xsl:when test="$lang='en'">Others</xsl:when>
									<xsl:when test="$lang='ch'">其他</xsl:when>
									<xsl:when test="$lang='gb'">其他</xsl:when>
								</xsl:choose>
							</xsl:when>
						</xsl:choose>
					</xsl:when>
				</xsl:choose>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>
