package com.cwn.wizbank.interceptor;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.cw.wizbank.Application;
import com.cwn.wizbank.exception.UploadException;

public class UploadInterceptor extends HandlerInterceptorAdapter{
	

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		HttpServletRequest req=(HttpServletRequest)request;
		MultipartResolver res=new org.springframework.web.multipart.commons.CommonsMultipartResolver();
		if(res.isMultipart(req)){
			MultipartHttpServletRequest  multipartRequest=(MultipartHttpServletRequest) req;
			Map<String, MultipartFile> files= multipartRequest.getFileMap();
			Iterator<String> iterator = files.keySet().iterator();
			while (iterator.hasNext()) {
				String formKey = (String) iterator.next();
				MultipartFile multipartFile = multipartRequest.getFile(formKey);
				//获取后缀名
				String suffix = multipartFile.getOriginalFilename().substring  
						(multipartFile.getOriginalFilename().lastIndexOf(".")+1);  
				
				if(!checkFile(suffix)){
					throw new UploadException("error_upload_forbidden");
				}
				
			}
			return true;
		}else{
			return true;
		}
	}
	
	private  boolean checkFile(String suffix){
		String forbidden = Application.UPLOAD_FORBIDDEN;
		String[] forbiddens = StringUtils.isEmpty(forbidden)?null:forbidden.split(",");
		if(null!=forbiddens && forbiddens.length>0){
			for(String fb : forbiddens){
				if(fb.equalsIgnoreCase(suffix)){
					return false;
				}
			}
		}
		return true;
	}

}
