如何编译jar文件(2007-11-30)


本目录内容的说明：

1. lib_compile: 编译jar文件所需要的第三方类库，但是运行时不需要放到www\WEB-INF\lib
 1. Bridge2Java.jar: 知识管理模块(KM)中关于搜索MSWord文件所需要的类库
 2. Bridge2Java_Word.jar: 由Bridge2Java.jar生成的类库
 3. jaxb-xjc.jar: 编译xsd文件所需要的类库
 4. jax-qname.jar: 编译xsd文件所需要的类库
 5. netscape.jar: java applet live connect所需要的类库(来源：java40.java from netscape communicator)
 6. servlet.jar: servlet api v2.2b
 7. weblogic.jar: 编译security file servlet所需要的类库(来源：weblogic.jar from weblogic 6.0)
 8. websphere.jar: 编译security file servlet所需要的类库(来源：ibmwebas.jar from websphere 4.5)

2. build_configSchemaJaxb.xml: 编译configSchemaJaxb.jar(xml configuration files)的ant build file

3. build_enterprise.xml: 编译enterprise.jar(imsapi xml schema files)的ant build file

4. build_wizbank.xml: 编译wizbank.jar的ant build file

5. build_dataloader.xml: 编译dataloader.jar的ant build file

6. readme.txt: 本自述文档

7. patching: 做patch的工具


编译步骤：

1. 准备好ant程序

2. 使用ant处理其中一个ant build file，例：
> ant -f build_wizbank.xml

3. 生成出来的jar文件可以在dist目录中找到
