package com.cwn.wizbank.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.cwn.wizbank.security.annotation.Anonymous;
import com.cwn.wizbank.utils.CookieUtil;

@Controller
@RequestMapping("session")
public class SessionTimeOutController {
	@Anonymous
	@RequestMapping("out")
	public ModelAndView toPage(ModelAndView mav, Model model, HttpServletRequest request){
		String lang = null;
		Cookie cookie = CookieUtil.getCookie(request, "user_lan");
		if(cookie != null) {
			lang = cookie.getValue();
		} else {
			lang = "en-us";
		}
		model.addAttribute("lang", lang);
		mav = new ModelAndView("error/sessionTimeOut");
		return mav;
	}
}
