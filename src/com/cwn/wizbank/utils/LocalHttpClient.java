package com.cwn.wizbank.utils;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

/**
 * Http工具 调用第三方接口
 * @author bill.lai
 *
 */
public class LocalHttpClient {

	/**
	 * 返回json类型结果
	 * @return 
	 */
	public static JSONObject HttpGetResponseUrl(String url,List<NameValuePair> params){
		
		HttpEntity entity = null;
		try{
			entity = new UrlEncodedFormEntity(params, "UTF-8");
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
			return null;
		}
		
		HttpUriRequest hur = RequestBuilder.post()
				.setUri(url)				
				.setEntity(entity)
				.build();
		
		String info = execute(hur);
		
		JSONObject jsonObject = JSONObject.fromObject(info);
			
		return jsonObject;
	}
	
	public static String execute(HttpUriRequest httpUriRequest) {
		CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().build();
		String responseBody = null;
		CloseableHttpResponse response = null;
		try {
			response = closeableHttpClient.execute(httpUriRequest);
			responseBody = EntityUtils.toString(response.getEntity(),"utf-8");
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (response != null) {
					response.close();
				}
				if(closeableHttpClient != null){
					closeableHttpClient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return responseBody;
	}

	public CloseableHttpResponse exec(HttpUriRequest httpUriRequest) {
		CloseableHttpClient closeableHttpClient = HttpClientBuilder.create()
				.build();
		CloseableHttpResponse response = null;
		try {
			response = closeableHttpClient.execute(httpUriRequest);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if (closeableHttpClient != null){
					closeableHttpClient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return response;
	}

}
