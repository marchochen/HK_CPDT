package com.cwn.wizbank.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;
  
/**
 * 微信开发者模式，自定义菜单创建器
 * @author Tiger
 */
public class WinxinMenuBuilder {
	private static final Logger logger = Logger.getLogger(WinxinMenuBuilder.class);
	/**
	 * @param args
	 * @throws IOException 
	 * @throws HttpException 
	 */
	public static void main(String[] args) throws HttpException, IOException {
		//使用这行可获取access_token
		//https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx934c3dba3951880b&secret=929d57ba0a721279b8a0c8a90e086082
		
		//String access_token = "nRZvVpDU7LxcSi7GnG2LrSDKEiJFOPUzDt6YYURcBOF0YbOjYsGUYSo-0c4OzaudLLFr0_FesSAt4s83kGZ0lGAL8uMLoQFklZ7whzdvmH3JkoiEl3p_pfgLPa7TxZ0iI3ydguE8dZV0Gzs3hR_COg";
		String access_token = "Ulcy7xXUpEz7p_I69SK3DhmghP-tI3b_NlG7p9Swy0_g4assANNSwYthQe_f6ffQKl7wqZ8Tozgm_P8M6nTfNqPhmrKNWqZI_ktSM5wQ0xYtWLBkWN69yX-cyt4EV6O9Czn3g93GAcmhvqDO053Big";
		
		String url = "http://api.weixin.qq.com/cgi-bin/menu/create?access_token="+access_token;
		//构建http构建[使用HttpClient的jar,怎么获得自己百度]
	    
	    File file = new File("src/com/tiger/app/weixin/weixinMenu/weixinMenuJsonData.txt");
		String weixinMenuJsonData = file2String(file, "GBK");
	    logger.info("weixinMenuJsonData:"+weixinMenuJsonData);
	    
	    HttpClient client = new HttpClient();
	    PostMethod post = new PostMethod(url);
	    post.setRequestBody(weixinMenuJsonData);
	    post.getParams().setContentCharset("utf-8");
	    //发送http请求
	    String respStr = "";
	    try {
	        client.executeMethod(post);
	        respStr = post.getResponseBodyAsString();
	    } catch (HttpException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    post.releaseConnection();
	    
	    logger.info("respStr:"+respStr);

	}

	/** 
     * 文本文件转换为指定编码的字符串 
     * 
     * @param file         文本文件 
     * @param encoding 编码类型 
     * @return 转换后的字符串 
     * @throws IOException 
     */ 
    public static String file2String(File file, String encoding) { 
            InputStreamReader reader = null; 
            StringWriter writer = new StringWriter(); 
            try { 
                    if (encoding == null || "".equals(encoding.trim())) { 
                            reader = new InputStreamReader(new FileInputStream(file), encoding); 
                    } else { 
                            reader = new InputStreamReader(new FileInputStream(file)); 
                    } 
                    //将输入流写入输出流 
                    char[] buffer = new char[1024]; 
                    int n = 0; 
                    while (-1 != (n = reader.read(buffer))) { 
                            writer.write(buffer, 0, n); 
                    } 
            } catch (Exception e) { 
                    e.printStackTrace(); 
                    return null; 
            } finally { 
                    if (reader != null) 
                            try { 
                                    reader.close(); 
                            } catch (IOException e) { 
                                    e.printStackTrace(); 
                            } 
            } 
            //返回转换结果 
            if (writer != null) 
                    return writer.toString(); 
            else return null; 
    }
}  