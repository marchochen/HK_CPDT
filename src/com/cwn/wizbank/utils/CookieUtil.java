package com.cwn.wizbank.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {
	
	/**
	 * 获取Cookie
	 * @param cookieName
	 * @param request
	 * @return
	 */
	public static Cookie getCookie(HttpServletRequest request, String cookieName) {
		/** 获取当前用户浏览器中的所有Cookie */
		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(cookieName)) {
					return cookie;
				}
			}
		}
		return null;
	}
	
	/**
	 * 添加Cookie
	 * @param cookieName
	 * @param cookieValue
	 * @param maxAge
	 */
	public static void addCookie(HttpServletResponse response, String cookieName, String cookieValue, int maxAge) {
		Cookie cookie = new Cookie(cookieName, cookieValue);
		cookie.setMaxAge(maxAge);
		cookie.setPath("/");
		response.addCookie(cookie);
	}
	
	/**
	 * 删除Cookie
	 * @param cookieName
	 */
	public static void removeCookie(HttpServletRequest request, HttpServletResponse response, String cookieName) {
		Cookie cookie = getCookie(request, cookieName);
		if(cookie != null) {
			cookie.setMaxAge(0);
			cookie.setPath("/");
			response.addCookie(cookie);
		}
		
	}

}
