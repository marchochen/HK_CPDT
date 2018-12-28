package com.cw.wizbank.util;

import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;

public class SendHttpRequest {
    
    private static class TrustAnyTrustManager implements X509TrustManager {
        
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }
    
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }
    
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
    }
    
    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }
    
    public static String sendUrl(String url, String postData, String enc, HttpServletRequest request, String Content_type) throws Exception {

        if (enc == null || enc.length() < 1) {

        }
        if (postData == null) {
            postData = "";
        }
        if (!postData.trim().startsWith("&")) {
            postData = "&" + postData.trim();
        }
        StringBuffer str = new StringBuffer();
        InputStreamReader in = null;
        HttpsURLConnection conn = null;
        HttpURLConnection uc = null;
        URL console = new URL(url);
        try {
            if (url != null && url.trim().toLowerCase().startsWith("https")) {
                SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, new TrustManager[] { new TrustAnyTrustManager() }, new java.security.SecureRandom());

                conn = (HttpsURLConnection) console.openConnection();
                conn.setSSLSocketFactory(sc.getSocketFactory());
                conn.setHostnameVerifier(new TrustAnyHostnameVerifier());

                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setUseCaches(false);

                if (Content_type != null && Content_type.length() > 0) {
                    conn.setRequestProperty("Content-type", Content_type);
                } else {
                    conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
                }

                if (request != null && request.getHeader("Cookie") != null) {
                    conn.setRequestProperty("Cookie", request.getHeader("Cookie"));
                }

                // BufferedOutputStream hurlBufOus=new
                // BufferedOutputStream(conn.getOutputStream());
                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                // dos.write(postData.getBytes());//这里面已经将RequestMethod设置为POST.前面设置无效
                // dos.flush();
                // dos.close();

                dos.writeBytes(postData.toString());
                dos.flush();
                dos.close();

                conn.connect();
                if (enc == null || enc.length() < 1) {
                    in = new InputStreamReader(conn.getInputStream());
                } else {
                    in = new InputStreamReader(conn.getInputStream(), enc);
                }

                int chr = in.read();
                while (chr != -1) {
                    str.append(String.valueOf((char) chr));
                    chr = in.read();
                }

            } else {
                System.getProperties().put("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");
                java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
                uc = (HttpURLConnection) console.openConnection();
                uc.setDoOutput(true);
                uc.setDoInput(true);
                uc.setUseCaches(false);
                uc.setRequestMethod("POST");

                if (Content_type != null && Content_type.length() > 0) {
                    uc.setRequestProperty("Content-type", Content_type);
                } else {
                    uc.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
                }
                if (request != null && request.getHeader("Cookie") != null) {
                    uc.setRequestProperty("Cookie", request.getHeader("Cookie"));
                }
                DataOutputStream dos = new DataOutputStream(uc.getOutputStream());
                dos.writeBytes(postData);
                dos.flush();
                dos.close();

                
                if (enc == null || enc.length() < 1) {
                    in = new InputStreamReader(uc.getInputStream());
                } else {
                    in = new InputStreamReader(uc.getInputStream(), enc);
                }
                int chr = in.read();
                while (chr != -1) {
                    str.append(String.valueOf((char) chr));
                    chr = in.read();
                }

            }

            return str.toString();
        } finally {
            if (in != null) {
                in.close();
            }
            if (uc != null) {
                uc.disconnect();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }

    }
    
    public static String sendUrl_new(String url,String postData) throws Exception{
		HttpClient client = new HttpClient();
	    PostMethod post = new PostMethod(url);
	    
	    if (postData == null) {
            postData = "";
        }
	    
	    RequestEntity entity = new StringRequestEntity(postData,"application/x-www-form-urlencoded","utf-8");
	    
	    post.setRequestEntity(entity);
	    
	    
	    String respStr = "";
	    try {
			client.executeMethod(post);
			respStr = post.getResponseBodyAsString();
		} finally{
			if(post!=null){
				post.releaseConnection();
				post = null;
			}
		}
	    
	    return respStr;
    }
    

}
