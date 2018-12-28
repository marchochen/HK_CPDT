2009-04-17

使用步骤：
1. 编译wizbank.jar放到www\WEB-INF\lib下
2. 执行bin\build.bat，按提示操作

------------------------
注意事项：
1. 此工具是根据两个版本间的diff从local中拿文件，所以做patch时要注意local的文件没有改动过，否则patch会加上本机的改动。

2. 此工具是从本机拿svn的用户名密码，如果本机还没有保存snv的用户名密码，则会失败，失败的log可以在log\error.log中看到"Could not authenticate to server"的提示信息。这时只要在本机上保存一次svn的用户名密码即可。

3. 删除的文件不会被包括在patch中，所以部署patch时需要手动删除文件。
