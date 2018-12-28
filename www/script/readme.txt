单点登录ASP文件设置说明 (zh-cn)

请根据以下步骤设置单点登录所需要的ASP文件：

1. 将wizbank\www\script目录整个复制到IIS可以访问的目录
2. 启动IIS
3. 创建新站点
4. 设置一般参数
5. “路径”参数指定为前面复制的script目录
6. 剔除“匿名访问”选项
7. 确定“脚本资源访问”选项被选上
8. 修改script\utils\utils.asp：
a) 修改参数lmsURL为wizBank的网址
	例如wizBank的网址是http://www.wizbank.com/，则应该修改为：
	lmsURL = "http://www.wizbank.com/"
	例如wizBank的网址是http://www.wizbank.com:8080/，则应该修改为：
	lmsURL = "http://www.wizbank.com:8080/"
b) 修改参数urlFailure为单点登录失败后转向的网页地址
	例如网页地址是http://www.abc.com/login_failed.htm，则应该修改为：
	urlFailure = "http://www.abc.com/login_failed.htm"
9. 启动该站点
10. 在数据库设置机构的acSite记录
a) 更新acSite.ste_domain为IIS的域名
b) 如果单点登录不需要密码检查，更新acSite.ste_trusted为1


SSO ASP Configuration (en-us)

Please follow the steps below to configure the necessary ASP for SSO:

1. Copy the whole directory wizbank\www\script to a directory where IIS can access
2. Start IIS
3. Create a new Web Site
4. Set general parameters
5. Set "Path" to the "script" directory copied previously
6. Uncheck the option "Allow anonymous access to this Web site"
7. Make sure the option "Run script (such as ASP)" is checked
8. Modify script\utils\utils.asp:
a) Modify parameter "lmsURL" to the address of wizBank
	If the address of wizBank is "http://www.wizbank.com/", the value should be set as:
	lmsURL = "http://www.wizbank.com/"
	If the address of wizBank is "http://www.wizbank.com:8080/", the value should be set as:
	lmsURL = "http://www.wizbank.com:8080/"
b) Modify parameter "urlFailure" to the address of the web page for SSO login failure
	If the address of the web page is "http://www.abc.com/login_failed.htm", the value should be set as:
	urlFailure = "http://www.abc.com/login_failed.htm"
9. Start the Web Site
10. Set the acSite record of the organization in database
a) update acSite.ste_domain to the domain name of IIS
b) update acSite.ste_trusted to 1 if password is not required during SSO
