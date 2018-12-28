/**
 * 
 */
package com.cwn.wizbank.utils;

import org.springframework.ui.Model;

/**
 * 请求状态
 */
public class RequestStatus {
	
	public final static String STATUS = "status";
	public final static String ADD = "add";
	public final static String UPDATE = "update";
	public final static String ERROR = "error";
	public final static String SUCCESS = "success";
	
	public final static String EXISTS = "exists";
	
	public static final String MSG = "msg";

	public final static String NO_PERMISSION = "no_permission";

	
	public static final String FLASH_MSG = "flash";
	public static final String TO_FOCUS = "focus";
	
	public static final String MSG_LEVEL = "msg_level";
	// 200: 成功；500：服务器出错；403：无权限；-1：token过期。
	public static final String CODE = "code";
	public static final String CODE_200 = "200";
	public static final String CODE_500 = "500";
	public static final String CODE_403 = "403";
	public static final String CODE_1= "-1";
	
	
	public static final String EXCEPTION  = "exception";
	
	public static Model setError (Model model, String code, String msg){
		model.addAttribute(RequestStatus.STATUS, RequestStatus.ERROR);
		model.addAttribute(RequestStatus.CODE, RequestStatus.CODE_403);
		model.addAttribute(RequestStatus.MSG, msg);
		
		return model;
	}
	
	public static Model setSuss (Model model){
		model.addAttribute(RequestStatus.STATUS, RequestStatus.SUCCESS);
		model.addAttribute(RequestStatus.CODE, RequestStatus.CODE_200);
		return model;
	}
	
	
}
