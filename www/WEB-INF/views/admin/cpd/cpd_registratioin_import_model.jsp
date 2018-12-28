<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<%@ include file="../../common/template.jsp"%>
<!DOCTYPE html>
<html>
<head>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_cpd_${lang}.js"></script>
<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-admin.css" />
		
	    <link rel="stylesheet" href="${ctx}/static/css/base.css"/>
		<link rel="stylesheet" href="${ctx}/static/admin/css/admin.css"/> 
<style>
				* {
					font-size: 9pt;
				}
				
			
				.instr_table {
					border-collapse: collapse;
				}
				
				.instr_table td {
					border: 1px solid #999999;
					height: 20px;
				}
				
				.title {
					background: #000000;
					color: #FFFFFF;
				}
				
				.sub_title {
					background: #CCCCCC;
				}
				b,
strong {
  font-weight: bold;
}
			</style>

</head>

<body topmargin="0" leftmargin="0" marginwidth="0" marginheight="0">
	<br />
	<table class="instr_table" width="95%" align="center">
		<tbody>
			<tr>
				<td colspan="2" class="title" width="100%" nowrap="">
				  <c:choose>
				  	<c:when test="${lang == 'zh-cn'}">导入注册记录－说明</c:when>
				  	<c:when test="${lang == 'zh-hk'}">導入注册記錄－說明</c:when>
				  	<c:otherwise>Instruction for importing registration records</c:otherwise>
				  </c:choose>
				</td>
			</tr>
			<tr>
				<td width="10%" nowrap="" align="center">1</td>
				<td>
				  <c:choose>
				  	<c:when test="${lang == 'zh-cn'}">根据模板填充数据。每一行代表用户的CPT / D组注册数据。请参见下面的字段描述，以便对字段进行完整的描述。</c:when>
				  	<c:when test="${lang == 'zh-hk'}">根據範本填充數據。每一行代表用戶的CPT / D組注册數據。請參見下麵的欄位描述，以便對欄位進行完整的描述。</c:when>
				  	<c:otherwise>Fill in the data according to the template. Each row represents CPT/D group registration data of a user. See the field description below for full description of the fields.</c:otherwise>
				  </c:choose>
				</td>
			</tr>
			<tr>
				<td nowrap="" align="center">2</td>
				<td>
				  <c:choose>
				  	<c:when test="${lang == 'zh-cn'}">登录到学习平台，然后转到<b>许可证登记管理>进口登记记录。</b>此功能仅适用于具有特定管理用户角色的用户。</c:when>
				  	<c:when test="${lang == 'zh-hk'}">登入到學習平臺，然後轉到<b>許可證登記管理>進口登記記錄。</b>此功能僅適用於具有特定管理用戶角色的用戶。</c:when>
				  	<c:otherwise>Login to the learning platform and go to <b>License registration management>Import registration records. </b>This function is available only to users with specific administrative user role</c:otherwise>
				  </c:choose>
				</td>
			</tr>
			<tr>
				<td nowrap="" align="center">3</td>
				<td>
				  <c:choose>
				  	<c:when test="${lang == 'zh-cn'}">将保存的文件使用<b>浏览</b>按钮上传到<b>文件位置</b>的右边。</c:when>
				  	<c:when test="${lang == 'zh-hk'}">將保存的檔案使用<b>瀏覽</b>按鈕上傳到<b>檔案位置</b>的右邊。</c:when>
				  	<c:otherwise>Upload the saved file using the <b>Browse...</b>button to the right of <b> File location.</b></c:otherwise>
				  </c:choose>
				</td>
			</tr>
			<tr>
				<td nowrap="" align="center">4</td>
				<td>
				  <c:choose>
				  	<c:when test="${lang == 'zh-cn'}">在学习平台上有一些指令来指导你完成这个过程。</c:when>
				  	<c:when test="${lang == 'zh-hk'}">在學習平臺上有一些指令來指導你完成這個過程。</c:when>
				  	<c:otherwise>There are instructions in the learning platform to guide you through the process.</c:otherwise>
				  </c:choose>
				</td>
			</tr>
		</tbody>
	</table>
	<br />
	<table class="instr_table" width="95%" align="center">
		<tbody>
			<tr>
				<td colspan="5" class="title" width="100%">
				  <c:choose>
				  	<c:when test="${lang == 'zh-cn'}">字段说明</c:when>
				  	<c:when test="${lang == 'zh-hk'}">欄位說明</c:when>
				  	<c:otherwise>Filed description</c:otherwise>
				  </c:choose>
				</td>
			</tr>
			<tr>
				<td class="sub_title" width="60" nowrap="">
				  <c:choose>
				  	<c:when test="${lang == 'zh-cn'}">字段名</c:when>
				  	<c:when test="${lang == 'zh-hk'}">欄位名</c:when>
				  	<c:otherwise>Field</c:otherwise>
				  </c:choose>
				</td>
				<td class="sub_title" width="60" nowrap="">
				  <c:choose>
				  	<c:when test="${lang == 'zh-cn'}">必填</c:when>
				  	<c:when test="${lang == 'zh-hk'}">必填</c:when>
				  	<c:otherwise>Reqd</c:otherwise>
				  </c:choose>
				</td>
				<td class="sub_title" width="60" nowrap="">
				  <c:choose>
				  	<c:when test="${lang == 'zh-cn'}">长度限制</c:when>
				  	<c:when test="${lang == 'zh-hk'}">長度限制</c:when>
				  	<c:otherwise>Max size</c:otherwise>
				  </c:choose>
				</td>
				<td class="sub_title" width="60" nowrap="">
				  <c:choose>
				  	<c:when test="${lang == 'zh-cn'}">格式</c:when>
				  	<c:when test="${lang == 'zh-hk'}">格式</c:when>
				  	<c:otherwise>Format</c:otherwise>
				  </c:choose>
				</td>
				<td class="sub_title" nowrap="">
				  <c:choose>
				  	<c:when test="${lang == 'zh-cn'}">备注</c:when>
				  	<c:when test="${lang == 'zh-hk'}">備註</c:when>
				  	<c:otherwise>Remarks</c:otherwise>
				  </c:choose>
				</td>
			</tr>
			<tr>
				<td nowrap="">
				  <c:choose>
				  	<c:when test="${lang == 'zh-cn'}">用户名</c:when>
				  	<c:when test="${lang == 'zh-hk'}">用戶名</c:when>
				  	<c:otherwise>User ID</c:otherwise>
				  </c:choose>
				</td>
				<td nowrap="">
				  <c:choose>
				  	<c:when test="${lang == 'zh-cn'}">是</c:when>
				  	<c:when test="${lang == 'zh-hk'}">是</c:when>
				  	<c:otherwise>Y</c:otherwise>
				  </c:choose>
				</td>
				<td nowrap="" align="right">
				  <c:choose>
				  	<c:when test="${lang == 'zh-cn'}">3 - 20 ( 字符 )</c:when>
				  	<c:when test="${lang == 'zh-hk'}">3 - 20 ( 字元 )</c:when>
				  	<c:otherwise>3 – 20 (Char)</c:otherwise>
				  </c:choose>
				</td>
				<td nowrap="">
				  <c:choose>
				  	<c:when test="${lang == 'zh-cn'}">只能包含：纯英文字母、数字、下划线(_)、横线(-)</c:when>
				  	<c:when test="${lang == 'zh-hk'}">只能包含：純英文字母、數位、底線(_)、橫線(-)</c:when>
				  	<c:otherwise>Character a - z, 0 - 9, '_' or '-' only</c:otherwise>
				  </c:choose>
				</td>
				<td>
				  <c:choose>
				  	<c:when test="${lang == 'zh-cn'}">它必须是现有的活动用户。</c:when>
				  	<c:when test="${lang == 'zh-hk'}">它必須是現有的活動用戶。</c:when>
				  	<c:otherwise>It must be an existing active user.</c:otherwise>
				  </c:choose>
				</td>
			</tr>
			<tr>
				<td nowrap="">
				  <c:choose>
				  	<c:when test="${lang == 'zh-cn'}">许可证类型</c:when>
				  	<c:when test="${lang == 'zh-hk'}">許可證類型</c:when>
				  	<c:otherwise>License type</c:otherwise>
				  </c:choose>
				</td>
				<td nowrap="">
				  <c:choose>
				  	<c:when test="${lang == 'zh-cn'}">是</c:when>
				  	<c:when test="${lang == 'zh-hk'}">是</c:when>
				  	<c:otherwise>Y</c:otherwise>
				  </c:choose>
				</td>
				<td nowrap="" align="right">
				  <c:choose>
				  	<c:when test="${lang == 'zh-cn'}">20 ( 字符 )</c:when>
				  	<c:when test="${lang == 'zh-hk'}">20 ( 字元 )</c:when>
				  	<c:otherwise>20 (Char)</c:otherwise>
				  </c:choose>
				</td>
				<td nowrap="">
				  <c:choose>
				  	<c:when test="${lang == 'zh-cn'}">文本</c:when>
				  	<c:when test="${lang == 'zh-hk'}">文字</c:when>
				  	<c:otherwise>Text</c:otherwise>
				  </c:choose>
				</td>
				<td>
				  <c:choose>
				  	<c:when test="${lang == 'zh-cn'}">这是用户注册的许可类型。许可证类型必须在“D／D许可证管理”中设置。</c:when>
				  	<c:when test="${lang == 'zh-hk'}">這是用戶註冊的許可類型。許可證類型必須在“D／D許可證管理”中設定。</c:when>
				  	<c:otherwise>This is the license type that the user registers in. The license type must have been set up in “CPT/D license management”</c:otherwise>
				  </c:choose>
				</td>
			</tr>
			<tr>
				<td nowrap="">
				  <c:choose>
				  	<c:when test="${lang == 'zh-cn'}">注册号</c:when>
				  	<c:when test="${lang == 'zh-hk'}">註冊號</c:when>
				  	<c:otherwise>Reg no</c:otherwise>
				  </c:choose>
				</td>
				<td nowrap="">
				  <c:choose>
				  	<c:when test="${lang == 'zh-cn'}">是</c:when>
				  	<c:when test="${lang == 'zh-hk'}">是</c:when>
				  	<c:otherwise>Y</c:otherwise>
				  </c:choose>
				</td>
				<td nowrap="" align="right">
				  <c:choose>
				  	<c:when test="${lang == 'zh-cn'}">80 ( 字符 )</c:when>
				  	<c:when test="${lang == 'zh-hk'}">80 ( 字元 )</c:when>
				  	<c:otherwise>80 (Char)</c:otherwise>
				  </c:choose>
				</td>
				<td nowrap="">
				  <c:choose>
				  	<c:when test="${lang == 'zh-cn'}">文本</c:when>
				  	<c:when test="${lang == 'zh-hk'}">文字</c:when>
				  	<c:otherwise>Text</c:otherwise>
				  </c:choose>
				</td>
				<td>
				  <c:choose>
				  	<c:when test="${lang == 'zh-cn'}">这是具有指定许可类型的用户注册号。</c:when>
				  	<c:when test="${lang == 'zh-hk'}">這是具有指定許可類型的用戶註冊號。</c:when>
				  	<c:otherwise>This is the user’s registration number with the specified license type.</c:otherwise>
				  </c:choose>
				</td>
			</tr>
			<tr>
				<td nowrap="">
				  <c:choose>
				  	<c:when test="${lang == 'zh-cn'}">注册日期</c:when>
				  	<c:when test="${lang == 'zh-hk'}">註冊日期</c:when>
				  	<c:otherwise>Reg date</c:otherwise>
				  </c:choose>
				</td>
				<td nowrap="">
				  <c:choose>
				  	<c:when test="${lang == 'zh-cn'}">是</c:when>
				  	<c:when test="${lang == 'zh-hk'}">是</c:when>
				  	<c:otherwise>Y</c:otherwise>
				  </c:choose>
				</td>
				<td nowrap="" align="right">
				  <c:choose>
				  	<c:when test="${lang == 'zh-cn'}">10 ( 字符 )</c:when>
				  	<c:when test="${lang == 'zh-hk'}">10 ( 字元 )</c:when>
				  	<c:otherwise>10 (Char)</c:otherwise>
				  </c:choose>
				</td>
				<td nowrap="">
				  <c:choose>
				  	<c:when test="${lang == 'zh-cn'}">YYYY/MM/DD</c:when>
				  	<c:when test="${lang == 'zh-hk'}">YYYY/MM/DD</c:when>
				  	<c:otherwise>YYYY/MM/DD</c:otherwise>
				  </c:choose>
				</td>
				<td>
				  <c:choose>
				  	<c:when test="${lang == 'zh-cn'}">这是具有指定许可类型的用户注册日期。</c:when>
				  	<c:when test="${lang == 'zh-hk'}">這是具有指定許可類型的用戶注册日期。</c:when>
				  	<c:otherwise>This is the user’s registration date with the specified license type.</c:otherwise>
				  </c:choose>
				</td>
			</tr>
			<tr>
				<td nowrap="">
				  <c:choose>
				  	<c:when test="${lang == 'zh-cn'}">注销日期</c:when>
				  	<c:when test="${lang == 'zh-hk'}">註銷日期</c:when>
				  	<c:otherwise>De-reg date</c:otherwise>
				  </c:choose>
				</td>
				<td nowrap="">
				  <c:choose>
				  	<c:when test="${lang == 'zh-cn'}">否</c:when>
				  	<c:when test="${lang == 'zh-hk'}">否</c:when>
				  	<c:otherwise>N</c:otherwise>
				  </c:choose>
				</td>
				<td nowrap="" align="right">
				  <c:choose>
				  	<c:when test="${lang == 'zh-cn'}">10 ( 字符 )</c:when>
				  	<c:when test="${lang == 'zh-hk'}">10 ( 字元 )</c:when>
				  	<c:otherwise>10 (Char)</c:otherwise>
				  </c:choose>
				</td>
				<td nowrap="">
				  <c:choose>
				  	<c:when test="${lang == 'zh-cn'}">YYYY/MM/DD</c:when>
				  	<c:when test="${lang == 'zh-hk'}">YYYY/MM/DD</c:when>
				  	<c:otherwise>YYYY/MM/DD</c:otherwise>
				  </c:choose>
				</td>
				<td>
				  <c:choose>
				  	<c:when test="${lang == 'zh-cn'}">这是用户指定的许可证类型的注销日期。</c:when>
				  	<c:when test="${lang == 'zh-hk'}">這是用戶指定的許可證類型的註銷日期。</c:when>
				  	<c:otherwise>This is the user’s de-registration date with the specified license type.</c:otherwise>
				  </c:choose>
				</td>
			</tr>
			<tr>
				<td nowrap="">
				  <c:choose>
				  	<c:when test="${lang == 'zh-cn'}">CPT/D 组编号</c:when>
				  	<c:when test="${lang == 'zh-hk'}">CPT/D 組編號</c:when>
				  	<c:otherwise>CPT/D group code</c:otherwise>
				  </c:choose>
				</td>
				<td nowrap="">
				  <c:choose>
				  	<c:when test="${lang == 'zh-cn'}">是</c:when>
				  	<c:when test="${lang == 'zh-hk'}">是</c:when>
				  	<c:otherwise>Y</c:otherwise>
				  </c:choose>
				</td>
				<td nowrap="" align="right">
				  <c:choose>
				  	<c:when test="${lang == 'zh-cn'}">20 ( 字符 )</c:when>
				  	<c:when test="${lang == 'zh-hk'}">20 ( 字元 )</c:when>
				  	<c:otherwise>20 (Char)</c:otherwise>
				  </c:choose>
				</td>
				<td nowrap="">
				  <c:choose>
				  	<c:when test="${lang == 'zh-cn'}">文本</c:when>
				  	<c:when test="${lang == 'zh-hk'}">文字</c:when>
				  	<c:otherwise>Text</c:otherwise>
				  </c:choose>
				</td>
				<td>
				  <c:choose>
				  	<c:when test="${lang == 'zh-cn'}">这是用户注册的CPT / D组（在指定的许可类型下）。CPT / D组代码必须在“D／D许可证管理”中设置。
用户可以在同一许可证类型的多个CPT / D组注册。请在Excel文件中以单独的行指定其他组。
                    </c:when>
				  	<c:when test="${lang == 'zh-hk'}">這是用戶註冊的CPT / D組（在指定的許可類型下）。CPT / D組程式碼必須在“D／D許可證管理”中設定。
用戶可以在同一許可證類型的多個CPT / D組注册。請在Excel檔案中以單獨的行指定其他組。
                    </c:when>
				  	<c:otherwise>This is the CPT/D group (under the specified license type) that the user registers in. The CPT/D group code must have been set up in “CPT/D license management”.
The user may register in multiple CPT/D groups of the same license type. Please specify additional groups in separate rows in the Excel file.
				    </c:otherwise>
				  </c:choose>
				</td>
			</tr>
			<tr>
				<td nowrap="">
				  <c:choose>
				  	<c:when test="${lang == 'zh-cn'}">初始日期</c:when>
				  	<c:when test="${lang == 'zh-hk'}">初始日期</c:when>
				  	<c:otherwise>Initial date</c:otherwise>
				  </c:choose>
				</td>
				<td nowrap="">
				  <c:choose>
				  	<c:when test="${lang == 'zh-cn'}">是</c:when>
				  	<c:when test="${lang == 'zh-hk'}">是</c:when>
				  	<c:otherwise>Y</c:otherwise>
				  </c:choose>
				</td>
				<td nowrap="" align="right">
				  <c:choose>
				  	<c:when test="${lang == 'zh-cn'}">10 ( 字符 )</c:when>
				  	<c:when test="${lang == 'zh-hk'}">10 ( 字元 )</c:when>
				  	<c:otherwise>10 (Char)</c:otherwise>
				  </c:choose>
				</td>
				<td nowrap="">
				  <c:choose>
				  	<c:when test="${lang == 'zh-cn'}">YYYY/MM/DD</c:when>
				  	<c:when test="${lang == 'zh-hk'}">YYYY/MM/DD</c:when>
				  	<c:otherwise>YYYY/MM/DD</c:otherwise>
				  </c:choose>
				</td>
				<td>
				  <c:choose>
				  	<c:when test="${lang == 'zh-cn'}">这是用户指定的CPT组的初始日期。初始日期用于计算评估年度内CPT / D组的所需时间。</c:when>
				  	<c:when test="${lang == 'zh-hk'}">這是用戶指定的CPT組的初始日期。初始日期用於計算評估年度內CPT / D組的所需時間。</c:when>
				  	<c:otherwise>This is the user’s initial date with the specified CPT/D group. Initial date is used to calculate the required hours for the CPT/D group in an assessment year.</c:otherwise>
				  </c:choose>
				</td>
			</tr>
			<tr>
				<td nowrap="">
				  <c:choose>
				  	<c:when test="${lang == 'zh-cn'}">到期期限</c:when>
				  	<c:when test="${lang == 'zh-hk'}">到期期限</c:when>
				  	<c:otherwise>Expiry date</c:otherwise>
				  </c:choose>
				</td>
				<td nowrap="">
				  <c:choose>
				  	<c:when test="${lang == 'zh-cn'}">否</c:when>
				  	<c:when test="${lang == 'zh-hk'}">否</c:when>
				  	<c:otherwise>N</c:otherwise>
				  </c:choose>
				</td>
				<td nowrap="" align="right">
				  <c:choose>
				  	<c:when test="${lang == 'zh-cn'}">10 ( 字符 )</c:when>
				  	<c:when test="${lang == 'zh-hk'}">10 ( 字元 )</c:when>
				  	<c:otherwise>10 (Char)</c:otherwise>
				  </c:choose>
				</td>
				<td nowrap="">
				  <c:choose>
				  	<c:when test="${lang == 'zh-cn'}">YYYY/MM/DD</c:when>
				  	<c:when test="${lang == 'zh-hk'}">YYYY/MM/DD</c:when>
				  	<c:otherwise>YYYY/MM/DD</c:otherwise>
				  </c:choose>
				</td>
				<td>
				  <c:choose>
				  	<c:when test="${lang == 'zh-cn'}">这是用户使用指定的D／D组的到期日期。如果用户的CPT / D组在评估年度到期，该组的所需时间将为该评估期的0。</c:when>
				  	<c:when test="${lang == 'zh-hk'}">這是用戶使用指定的D／D組的到期日期。如果用戶的CPT / D組在評估年度到期，該組的所需時間將為該評估期的0。</c:when>
				  	<c:otherwise>This is the user’s expiry date with the specified CPT/D group. If the user’s CPT/D group is expired in an assessment year, required hours for that group will be 0 in that assessment period.</c:otherwise>
				  </c:choose>
				</td>
			</tr>
		</tbody>
	</table>
	<br />
	<br />


</body>

</html>