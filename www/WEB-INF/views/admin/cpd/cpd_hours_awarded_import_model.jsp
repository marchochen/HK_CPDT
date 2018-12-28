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
    <table width="95%" align="center" class="instr_table">
                    <tr>
                        <td class="title" width="100%" colspan="2" nowrap="nowrap">
						  <c:choose>
						  	<c:when test="${lang == 'zh-cn'}">导入用户授予的CPT / D小时指令</c:when>
						  	<c:when test="${lang == 'zh-hk'}">導入用戶授予的CPT / D小時指令</c:when>
						  	<c:otherwise>Instruction for importing user awarded CPT/D hours</c:otherwise>
						  </c:choose>
						</td>
                    </tr>
                    <tr>
                        <td nowrap="nowrap" align="center" width="10%">1</td>
                        <td>
						  <c:choose>
						  	<c:when test="${lang == 'zh-cn'}">根据模板填充数据。每一行代表一个CPT / D组用户在课程中使用的CPT / D小时。请参见下面的字段描述，以便对字段进行完整的描述。</c:when>
						  	<c:when test="${lang == 'zh-hk'}">根據範本填充數據。每一行代表一個CPT / D組用戶在課程中使用的CPT / D小時。請參見下麵的欄位描述，以便對欄位進行完整的描述。</c:when>
						  	<c:otherwise>Fill in the data according to the template. Each row represents CPT/D hours awarded to a user for a CPT/D group in a course. See the field description below for full description of the fields.</c:otherwise>
						  </c:choose>
						</td>
                    </tr>
                    <tr>
                        <td nowrap="nowrap" align="center">2</td>
                        <td>
						  <c:choose>
						  	<c:when test="${lang == 'zh-cn'}">登录到学习平台，进入到<b>CPT/D管理</b> > <b>导入用户获得CPT/D时数。</b>此功能仅适用于具有特定管理用户角色的用户。</c:when>
						  	<c:when test="${lang == 'zh-hk'}">登入到學習平臺，進入到<b>CPT/D管理</b> > 導入用戶獲得CPT/D時數。</b>此功能僅適用於具有特定管理用戶角色的用戶。</c:when>
						  	<c:otherwise>Login to the learning platform and go to <b>CPT/D management </b> > <b> Import user awarded CPT/D hours. </b>This function is available only to users with specific administrative user role.</c:otherwise>
						  </c:choose>
						</td>
                    </tr>
                    <tr>
                        <td nowrap="nowrap" align="center">3</td>
                        <td>
						  <c:choose>
						  	<c:when test="${lang == 'zh-cn'}">使用<b>浏览</b>方式上传保存的文件…按钮到<b>文件位置</b>的右边。</c:when>
						  	<c:when test="${lang == 'zh-hk'}">使用<b>流覽</b>方式上傳保存的檔案…按鈕到<b>檔案位置</b>的右邊。</c:when>
						  	<c:otherwise>Upload the saved file using the<b> Browse...</b> button to the right of <b>File location.</b></c:otherwise>
						  </c:choose>
						</td>
                    </tr>
                    <tr>
                        <td nowrap="nowrap" align="center">4</td>
                        <td>
						  <c:choose>
						  	<c:when test="${lang == 'zh-cn'}">在学习平台上有一些指令来指导你完成这个过程。</c:when>
						  	<c:when test="${lang == 'zh-hk'}">在學習平臺上有一些指令來指導你完成這個過程。</c:when>
						  	<c:otherwise>There are instructions in the learning platform to guide you through the process.</c:otherwise>
						  </c:choose>
						</td>
                    </tr>
                </table>
                <br/>
                <table width="95%" align="center" class="instr_table">
                    <tr>
                        <td class="title" width="100%" colspan="5" nowrap="nowrap">
						  <c:choose>
						  	<c:when test="${lang == 'zh-cn'}">字段说明</c:when>
						  	<c:when test="${lang == 'zh-hk'}">欄位說明</c:when>
						  	<c:otherwise>Field description</c:otherwise>
						  </c:choose>
						</td>
                    </tr>
                    <tr class="sub_title">
                        <td nowrap="nowrap" width="10%">
						  <c:choose>
						  	<c:when test="${lang == 'zh-cn'}">字段名</c:when>
						  	<c:when test="${lang == 'zh-hk'}">欄位名</c:when>
						  	<c:otherwise>Field</c:otherwise>
						  </c:choose>
						</td>
                        <td nowrap="nowrap" width="5%">
						  <c:choose>
						  	<c:when test="${lang == 'zh-cn'}">必填</c:when>
						  	<c:when test="${lang == 'zh-hk'}">必填</c:when>
						  	<c:otherwise>Reqd</c:otherwise>
						  </c:choose>
                        </td>
                        <td nowrap="nowrap" width="10%">
						  <c:choose>
						  	<c:when test="${lang == 'zh-cn'}">长度限制</c:when>
						  	<c:when test="${lang == 'zh-hk'}">長度限制</c:when>
						  	<c:otherwise>Max size</c:otherwise>
						  </c:choose>                        
                        </td>
                        <td nowrap="nowrap" width="20%">
						  <c:choose>
						  	<c:when test="${lang == 'zh-cn'}">格式</c:when>
						  	<c:when test="${lang == 'zh-hk'}">格式</c:when>
						  	<c:otherwise>Format</c:otherwise>
						  </c:choose>                        
                        </td>
                        <td nowrap="nowrap" width="55%">
						  <c:choose>
						  	<c:when test="${lang == 'zh-cn'}">备注</c:when>
						  	<c:when test="${lang == 'zh-hk'}">備註</c:when>
						  	<c:otherwise>Remarks</c:otherwise>
						  </c:choose>                        
                        </td>
                    </tr>
                    <tr >
                        <td nowrap="nowrap" width="10%">
						  <c:choose>
						  	<c:when test="${lang == 'zh-cn'}">用户名</c:when>
						  	<c:when test="${lang == 'zh-hk'}">用戶名</c:when>
						  	<c:otherwise>User ID</c:otherwise>
						  </c:choose>                        
                        </td>
                        <td nowrap="nowrap" width="5%">
						  <c:choose>
						  	<c:when test="${lang == 'zh-cn'}">是</c:when>
						  	<c:when test="${lang == 'zh-hk'}">是</c:when>
						  	<c:otherwise>Y</c:otherwise>
						  </c:choose>                        
                        </td>
                        <td nowrap="nowrap" width="10%">
						  <c:choose>
						  	<c:when test="${lang == 'zh-cn'}">3 - 20 ( 字符 )</c:when>
						  	<c:when test="${lang == 'zh-hk'}">3 - 20 ( 字元 )</c:when>
						  	<c:otherwise>3 – 20 (Char)</c:otherwise>
						  </c:choose>                        
                        </td>
                        <td nowrap="nowrap" width="20%">
						  <c:choose>
						  	<c:when test="${lang == 'zh-cn'}">只能包含：纯英文字母、数字、下划线(_)、横线(-)</c:when>
						  	<c:when test="${lang == 'zh-hk'}">只能包含：純英文字母、數位、底線(_)、橫線(-)</c:when>
						  	<c:otherwise>Character a - z, 0 - 9, '_' or '-' only</c:otherwise>
						  </c:choose>                        
                        </td>
                        <td nowrap="nowrap" width="55%">
						  <c:choose>
						  	<c:when test="${lang == 'zh-cn'}">它必须是现有的活动用户。</c:when>
						  	<c:when test="${lang == 'zh-hk'}">它必須是現有的活動用戶。</c:when>
						  	<c:otherwise>It must be an existing active user.</c:otherwise>
						  </c:choose>                        
                        </td>
                    </tr>
                    <tr >
                        <td nowrap="nowrap" >
						  <c:choose>
						  	<c:when test="${lang == 'zh-cn'}">课程编号/班级编号</c:when>
						  	<c:when test="${lang == 'zh-hk'}">課程編號/班級編號</c:when>
						  	<c:otherwise>Course Code/ Class Code</c:otherwise>
						  </c:choose>  
                        </td>
                        <td nowrap="nowrap" >
						  <c:choose>
						  	<c:when test="${lang == 'zh-cn'}">是</c:when>
						  	<c:when test="${lang == 'zh-hk'}">是</c:when>
						  	<c:otherwise>Y</c:otherwise>
						  </c:choose>                         
                        </td>
                        <td nowrap="nowrap" >
						  <c:choose>
						  	<c:when test="${lang == 'zh-cn'}">50 ( 字符 )</c:when>
						  	<c:when test="${lang == 'zh-hk'}">50 ( 字元 )</c:when>
						  	<c:otherwise>50 (Char)</c:otherwise>
						  </c:choose>                        
                        </td>
                        <td nowrap="nowrap" >
						  <c:choose>
						  	<c:when test="${lang == 'zh-cn'}">文本</c:when>
						  	<c:when test="${lang == 'zh-hk'}">文字</c:when>
						  	<c:otherwise>Text</c:otherwise>
						  </c:choose>
						</td>
                        <td nowrap="nowrap" >
						  <c:choose>
						  	<c:when test="${lang == 'zh-cn'}">
这是用户授予的课程/课程。<br/>

只有以下的学习解决方案可以奖励CPT / D小时：<br/>
•基于网络的课程<br/>
•基于Web的考试<br/>
•课堂课程<br/>
•课堂考试<br/>

如果是基于网络的课程/考试，请指定课程代码。<br/>

如果是课堂课程/考试，请指定课堂代码。<br/>

课程/课程一定已经设置好了。<br/>

如果用户已经注册在课程/类中，记录将被跳过。<br/>
							</c:when>
						  	<c:when test="${lang == 'zh-hk'}">
這是用戶授予的課程/課程。<br/>

只有以下的學習解決方案可以獎勵CPT / D小時：<br/>
•基於網絡的課程<br/>
•基於Web的考試<br/>
•課堂課程<br/>
•課堂考試<br/>

如果是基於網絡的課程/考試，請指定課程代碼。<br/>

如果是課堂課程/考試，請指定課堂程式碼。<br/>

課程/課程一定已經設定好了。<br/>

如果用戶已經注册在課程/類中，記錄將被跳過。<br/>
							</c:when>
						  	<c:otherwise>
This is the course/class that the user is awarded CPT/D hours.<br/>

Only the following learning solutions can award CPT/D hours:<br/>
•   Web-based Course<br/>
•   Web-based Exam<br/>
•   Classroom Course<br/>
•   Classroom Exam<br/>

If it is a web-based course/exam, please specify the course code.<br/>

If it is a classroom course/exam, please specify the class code.<br/>

The course/class must have been set up.<br/>

If the user is already enrolled in the course/class, the record will be skipped.<br/>
							</c:otherwise>
						  </c:choose>                       

                        </td>
                    </tr>
                    <tr >
                        <td nowrap="nowrap" >
						  <c:choose>
						  	<c:when test="${lang == 'zh-cn'}">许可证类型</c:when>
						  	<c:when test="${lang == 'zh-hk'}">許可證類型</c:when>
						  	<c:otherwise>License type</c:otherwise>
						  </c:choose>
                        </td>
                        <td nowrap="nowrap" >
						  <c:choose>
						  	<c:when test="${lang == 'zh-cn'}">是</c:when>
						  	<c:when test="${lang == 'zh-hk'}">是</c:when>
						  	<c:otherwise>Y</c:otherwise>
						  </c:choose>                             
                        </td>
                        <td nowrap="nowrap" >
						  <c:choose>
						  	<c:when test="${lang == 'zh-cn'}">20 ( 字符 )</c:when>
						  	<c:when test="${lang == 'zh-hk'}">20 ( 字元 )</c:when>
						  	<c:otherwise>20 (Char)</c:otherwise>
						  </c:choose>                        
                        </td>
                        <td nowrap="nowrap" >
						  <c:choose>
						  	<c:when test="${lang == 'zh-cn'}">文本</c:when>
						  	<c:when test="${lang == 'zh-hk'}">文字</c:when>
						  	<c:otherwise>Text</c:otherwise>
						  </c:choose>
						</td>
                        <td nowrap="nowrap" >
						  <c:choose>
						  	<c:when test="${lang == 'zh-cn'}">
这是用户注册的许可类型。许可证类型必须在“D／D许可证管理”中设置。<br/>	

如果用户未在许可证中注册，则将跳过记录。<br/>								  	
						  	</c:when>
						  	<c:when test="${lang == 'zh-hk'}">
這是用戶註冊的許可類型。許可證類型必須在“D／D許可證管理”中設定。<br/>

如果用戶未在許可證中注册，則將跳過記錄。<br/>			
						  	</c:when>
						  	<c:otherwise>
This is the license type that the user registers in. The license type must have been set up in “CPT/D license management”<br/>

If the user does not register in the license, the record will be skipped.<br/>						  	
						  	</c:otherwise>
						  </c:choose>
                        </td>
                    </tr>
                    <tr >
                        <td nowrap="nowrap" >
						  <c:choose>
						  	<c:when test="${lang == 'zh-cn'}">CPT/D 组编号</c:when>
						  	<c:when test="${lang == 'zh-hk'}">CPT/D 組編號</c:when>
						  	<c:otherwise>CPT/D group code</c:otherwise>
						  </c:choose>                        
                        </td>
                        <td nowrap="nowrap" >
                          <c:choose>
					  	    <c:when test="${lang == 'zh-cn'}">是</c:when>
					  	    <c:when test="${lang == 'zh-hk'}">是</c:when>
					  		<c:otherwise>Y</c:otherwise>
					  	  </c:choose>
					  	</td>
                        <td nowrap="nowrap" >
						  <c:choose>
						  	<c:when test="${lang == 'zh-cn'}">20 ( 字符 )</c:when>
						  	<c:when test="${lang == 'zh-hk'}">20 ( 字元 )</c:when>
						  	<c:otherwise>20 (Char)</c:otherwise>
						  </c:choose>                            
                        </td>
                        <td nowrap="nowrap" >
						  <c:choose>
						  	<c:when test="${lang == 'zh-cn'}">文本</c:when>
						  	<c:when test="${lang == 'zh-hk'}">文字</c:when>
						  	<c:otherwise>Text</c:otherwise>
						  </c:choose>                        
                        </td>
                        <td nowrap="nowrap" >
						  <c:choose>
						  	<c:when test="${lang == 'zh-cn'}">
这是用户注册的CPT / D组（在指定的许可类型下）。“D／D许可证管理”中必须设置CPT／D组代码。<br/>	

如果用户不在CPT组中注册，记录将被跳过。<br/>							  	
						  	</c:when>
						  	<c:when test="${lang == 'zh-hk'}">
這是用戶註冊的CPT / D組（在指定的許可類型下）。“D／D許可證管理”中必須設定CPT／D組程式碼。<br/>	

如果用戶不在CPT組中注册，記錄將被跳過。<br/>			
						  	</c:when>
						  	<c:otherwise>
This is the CPT/D group (under the specified license type) that the user registers in. The CPT/D group code must have been set up in “CPT/D license management”.<br/>

If the user does not register in the CPT/D group, the record will be skipped.<br/>				  	
						  	</c:otherwise>
						  </c:choose>                        
                        </td>
                    </tr>
                    <tr >
                        <td nowrap="nowrap" >
						  <c:choose>
						  	<c:when test="${lang == 'zh-cn'}">CPT/D时数获取日期</c:when>
						  	<c:when test="${lang == 'zh-hk'}">CPT/D時數獲取日期</c:when>
						  	<c:otherwise>CPT/D hours award date</c:otherwise>
						  </c:choose>                           
                        </td>
                        <td nowrap="nowrap" >
                          <c:choose>
					  	    <c:when test="${lang == 'zh-cn'}">是</c:when>
					  	    <c:when test="${lang == 'zh-hk'}">是</c:when>
					  		<c:otherwise>Y</c:otherwise>
					  	  </c:choose>                        
                        </td>
                        <td nowrap="nowrap" >
						  <c:choose>
						  	<c:when test="${lang == 'zh-cn'}">10 ( 字符 )</c:when>
						  	<c:when test="${lang == 'zh-hk'}">10 ( 字元 )</c:when>
						  	<c:otherwise>10 (Char)</c:otherwise>
						  </c:choose>                                  
                        </td>
                        <td nowrap="nowrap" >
						  <c:choose>
						  	<c:when test="${lang == 'zh-cn'}">YYYY/MM/DD</c:when>
						  	<c:when test="${lang == 'zh-hk'}">YYYY/MM/DD</c:when>
						  	<c:otherwise>YYYY/MM/DD</c:otherwise>
						  </c:choose>                        
                        </td>
                        <td nowrap="nowrap" >
						  <c:choose>
						  	<c:when test="${lang == 'zh-cn'}">
这是指定的CPT / D组的CPT / D小时的日期。<br/>

它将被设置为注册日期和完成日期。<br/>

基于Web：<br/>
如果CPT / D小时奖励日期晚于CPT / D小时结束日期，则记录将被跳过。<br/>

教室：<br/>
如果D／D小时奖励日期晚于课程结束日期，记录将被跳过。	<br/>				  	
						  	</c:when>
						  	<c:when test="${lang == 'zh-hk'}">
這是指定的CPT / D組的CPT / D小時的日期。<br/>

它將被設定為注册日期和完成日期。<br/>

基於Web:<br/>
如果CPT / D小時獎勵日期晚於CPT / D小時結束日期，則記錄將被跳過。<br/>

教室：<br/>
如果D／D小時獎勵日期晚於課程結束日期，記錄將被跳過。<br/>	
						  	</c:when>
						  	<c:otherwise>
This is the date on which CPT/D hours is awarded for the specified CPT/D group. <br/>

It will be set as the Enrollment Date and Completion Date as well.<br/>

Web-based:<br/>
If CPT/D hours award date is later than CPT/D hours end date, the record will be skipped.<br/>

Classroom:<br/>
If CPT/D hours award date is later than class end date, the record will be skipped.<br/>			  	
						  	</c:otherwise>
						  </c:choose>                            
                        </td>
                    </tr>
                    <tr >
                        <td nowrap="nowrap" >
						  <c:choose>
						  	<c:when test="${lang == 'zh-cn'}">获取核心小时</c:when>
						  	<c:when test="${lang == 'zh-hk'}">獲取核心小時</c:when>
						  	<c:otherwise>Core hours awarded</c:otherwise>
						  </c:choose>                        
                        </td>
                        <td nowrap="nowrap" >
                          <c:choose>
					  	    <c:when test="${lang == 'zh-cn'}">是</c:when>
					  	    <c:when test="${lang == 'zh-hk'}">是</c:when>
					  		<c:otherwise>Y</c:otherwise>
					  	  </c:choose>                          
                        </td>
                        <td nowrap="nowrap" >6</td>
                        <td nowrap="nowrap" >999.99</td>
                        <td nowrap="nowrap" >
						  <c:choose>
						  	<c:when test="${lang == 'zh-cn'}">
这是为指定的D / D组颁发的核心工时。<br/>

如果课程/类本身不授予任何核心时间，则记录将被跳过。<br/>

如果核心课时超过了课程/班级授予的最高金额，则记录将被跳过。<br/>					  	
						  	</c:when>
						  	<c:when test="${lang == 'zh-hk'}">
這是為指定的D / D組頒發的核心工時。<br/>

如果課程/類本身不授予任何核心時間，則記錄將被跳過。<br/>

如果核心課時超過了課程/班級授予的最高金額，則記錄將被跳過。<br/>		
						  	</c:when>
						  	<c:otherwise>
This is the core hours awarded for the specified CPT/D group.<br/>

If the course/class itself does not award any core hours, the record will be skipped.<br/>

If core hours awarded exceeds the maximum amount awarded by the course/class, the record will be skipped.<br/>		  	
						  	</c:otherwise>
						  </c:choose>                           
                        </td>
                    </tr>
                    <tr >
                        <td nowrap="nowrap" >
						  <c:choose>
						  	<c:when test="${lang == 'zh-cn'}">获取非核心小时</c:when>
						  	<c:when test="${lang == 'zh-hk'}">獲取非核心小時</c:when>
						  	<c:otherwise>Non-core hours awarded</c:otherwise>
						  </c:choose>                            
                        </td>
                        <td nowrap="nowrap" >
                          <c:choose>
					  	    <c:when test="${lang == 'zh-cn'}">否</c:when>
					  	    <c:when test="${lang == 'zh-hk'}">否</c:when>
					  		<c:otherwise>N</c:otherwise>
					  	  </c:choose>                         
                        </td>
                        <td nowrap="nowrap" >6</td>
                        <td nowrap="nowrap" >999.99</td>
                        <td nowrap="nowrap" >
						  <c:choose>
						  	<c:when test="${lang == 'zh-cn'}">
这是为指定的D / D组提供的非核心时间（如果许可有非核心时间）。<br/>

如果许可证没有指定非核心小时数和非核心小时数，则将跳过记录。<br/>

如果课程/课程本身不授予任何非核心时间和非核心时间，则记录将被跳过。<br/>

如果核心课时超过了课程/班级授予的最高金额，则记录将被跳过。<br/>				  	
						  	</c:when>
						  	<c:when test="${lang == 'zh-hk'}">
這是為指定的D / D組提供的非核心時間（如果許可有非核心時間）。<br/>

如果許可證沒有指定非核心小時數和非核心小時數，則將跳過記錄。<br/>

如果課程/課程本身不授予任何非核心時間和非核心時間，則記錄將被跳過。<br/>

如果核心課時超過了課程/班級授予的最高金額，則記錄將被跳過。	<br/>
						  	</c:when>
						  	<c:otherwise>
This is the non-core hours awarded for the specified CPT/D group (provided that the license has non-core hours). <br/>

If the license does not have non-core hours and non-core hours awarded is specified, the record will be skipped.<br/>

If the course/class itself does not award any non-core hours and non-core hours awarded is specified, the record will be skipped.<br/>

If core hours awarded exceeds the maximum amount awarded by the course/class, the record will be skipped.<br/>		  	
						  	</c:otherwise>
						  </c:choose>                          
                        </td>
                    </tr>
                        
                </table>
    <br />
    <br />


</body>

</html>