package com.cwn.wizbank.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

public class HttpClientUtils {
	
	public static String doPost(String posturl, Map<String, String> params, String charset) {
		return doPost(posturl, params, charset, null);
	}

	public static String doPost(String posturl, Map<String, String> params, String charset, String contentType) {
		if ((charset == null) || ("".equals(charset))) {
			charset = "UTF-8";
		}
		String parmsStr = "";

		Iterator<String> it = params.keySet().iterator();

		while (it.hasNext()) {
			String key = (String) it.next();
			String value = (String) params.get(key);
			parmsStr = parmsStr + "&" + key + "=" + value;
		}
		return doPost(posturl, parmsStr, charset, null);
	}

	public static String doPost(String posturl, String postStr, String charset) {
		return doPost(posturl, postStr, charset, null);
	}

	public static String doPost(String posturl, String postStr, String charset, String contentType) {
		System.out.println("发送：" + posturl + " " + postStr);
		if (isNull(charset)) {
			charset = "UTF-8";
		}
		if (isNull(contentType)) {
			contentType = "application/x-www-form-urlencoded";
		}
		StringBuffer sb = new StringBuffer();
		try {
			URL url = new URL(posturl);
			HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();

			httpConn.setConnectTimeout(10000);

			httpConn.setDoOutput(true);
			httpConn.setDoInput(true);
			httpConn.setUseCaches(false);
			httpConn.setRequestMethod("POST");
			httpConn.setRequestProperty("Content-Type", contentType);
			httpConn.connect();

			OutputStreamWriter outputStream = new OutputStreamWriter(httpConn.getOutputStream(), charset);
			outputStream.write(postStr);
			outputStream.flush();
			outputStream.close();

			int responseCode = httpConn.getResponseCode();
			if (200 == responseCode) {
				BufferedReader responseReader = new BufferedReader(
						new InputStreamReader(httpConn.getInputStream(), charset));
				String readLine;
				while ((readLine = responseReader.readLine()) != null) {
					sb.append(readLine);
				}
				responseReader.close();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		System.out.println("POST接收:" + sb);
		return sb.toString();
	}

	public static String doGet(String geturl, String charset) {
		if ((charset == null) || ("".equals(charset))) {
			charset = "UTF-8";
		}
		StringBuffer sb = new StringBuffer();
 		HttpURLConnection httpConn = null;
		try {
			URL url = new URL(geturl);
			httpConn = (HttpURLConnection) url.openConnection();

			httpConn.setConnectTimeout(10000);
			httpConn.setUseCaches(false);
			httpConn.setRequestMethod("GET");
			httpConn.connect();

			int responseCode = httpConn.getResponseCode();
			if (200 == responseCode) {

				BufferedReader responseReader = new BufferedReader(
						new InputStreamReader(httpConn.getInputStream(), charset));
				String readLine;
				while ((readLine = responseReader.readLine()) != null) {
					sb.append(readLine);
				}
				responseReader.close();
			}
			httpConn.disconnect();
		} catch (Exception ex) {
			ex.printStackTrace();
			if (httpConn != null) {
				httpConn.disconnect();
			}
		}
		return sb.toString();
	}

	public static boolean isNull(Object obj) {
		if (obj == null) {
			return true;
		}
		if ("".equals(obj.toString().trim())) {
			return true;
		}
		return false;
	}

}
