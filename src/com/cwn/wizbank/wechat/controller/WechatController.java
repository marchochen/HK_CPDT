package com.cwn.wizbank.wechat.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSysMessage;
import com.cwn.wizbank.entity.APIToken;
import com.cwn.wizbank.security.annotation.Anonymous;
import com.cwn.wizbank.services.LoginService;
import com.cwn.wizbank.utils.JsonFormat;
import com.cwn.wizbank.wechat.model.Authentication;
import com.cwn.wizbank.wechat.service.WechatService;

@RequestMapping("wechat")
@Controller
public class WechatController {

	Logger logger = LoggerFactory.getLogger(WechatController.class);
	
	@Autowired
	LoginService loginService;

	@Autowired
	WechatService wechatService;

	@RequestMapping("toBind")
	@Anonymous
	public String toBind(HttpServletRequest request,
			Model model,
			@RequestParam(value = "userOpenId", required = true) String userOpenId,
			@RequestParam(value = "fwhCode", required = false) String fwhCode) {
		request.getSession().invalidate();

		String browserType = request.getHeader("User-Agent");// 浏览器类型
		logger.info("browserType:" + browserType);
		if (StringUtils.contains(browserType, "MicroMessenger")) {
			logger.info("from weixin browser!");
		} else {
			logger.warn("not from weixin browser!");
		}
		model.addAttribute("fwhCode", fwhCode);
		model.addAttribute("userOpenId", userOpenId);

		return "wechat/edit";
	}
	
	@RequestMapping("toHelp")
	@Anonymous
	public String toHelp(HttpServletRequest request) {

		return "wechat/help";
	}


	@RequestMapping("bind")
	@ResponseBody
	@Anonymous
	public String bind(Model model, loginProfile prof, WizbiniLoader wizbini,
			@RequestParam(value="account") String account,
			@RequestParam(value="password") String password,
			@RequestParam(value="userOpenId") String userOpenId,
			@RequestParam(value="fwhCode") String fwhCode, HttpServletRequest request) throws qdbException, cwException, SQLException, cwSysMessage {
		prof = new loginProfile();
		request.getSession().invalidate();
		loginService.login(model,request, prof, wizbini, account, password, userOpenId, "", 1, APIToken.API_DEVELOPER_WEIXIN, true, null);
		
		return JsonFormat.format(model);
		//return "wechat/success";
	}

	/**
	 * @param fwhCode
	 *            //服务号
	 * @param echostr
	 *            //随机字符串
	 * @param token
	 *            //令牌
	 * @param signature
	 *            //开发者微信号
	 * @param timestamp
	 *            //时间戳
	 * @param nonce
	 *            //随机数
	 * @return
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value="", headers="Accept=text/plain")
	@Anonymous
	public String entrance(
			HttpServletRequest request,
			HttpServletResponse response,
			WizbiniLoader wizbini,
			loginProfile prof,
			@RequestParam(value = "fwhCode", required = false, defaultValue = "offline") String fwhCode,
			@RequestParam(value = "echostr", required = false, defaultValue = "") String echostr,
			@RequestParam(value = "token", required = false, defaultValue = "") String token,
			@RequestParam(value = "signature", required = false, defaultValue = "") String signature,
			@RequestParam(value = "timestamp", required = false, defaultValue = "") String timestamp,
			@RequestParam(value = "nonce", required = false, defaultValue = "") String nonce)
			throws IOException {
		
		Authentication authentication = wechatService.setAuthentication(
				echostr, signature, timestamp, nonce);
		boolean isCheckAuthentication = (null != authentication);// 微信是否正在检查接口合法性
		if (isCheckAuthentication) {
			boolean isAuthentication = wechatService.checkAuthentication(authentication);
			if (isAuthentication) {
				logger.info("微信平台签名消息验证成功");
				return authentication.getEchostr();
			} else {
				logger.warn("微信平台签名消息验证失败");
			}
		} else {
			return wechatService.work(request, wizbini, prof, fwhCode);
		}
		return null;
	}

}
