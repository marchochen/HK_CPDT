package com.cwn.wizbank.wechat.admin;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cwn.wizbank.utils.CommonLog;


@RequestMapping("admin/wechat")
@Controller("adminWechatController")
public class WechatController {

	Logger logger = LoggerFactory.getLogger(WechatController.class);

	/**
	 * 更新微信菜单
	 * 
	 * @param tokenUrl
	 * @param menuStr
	 * @return
	 */
	@RequestMapping("updateMenu")
	@ResponseBody
	public String updateMenu(String tokenUrl, String menuStr) {
		
		String token = getToken(tokenUrl);
		String result = sendUpdateMenuReq(token, menuStr);
		
		return result;
	}

	private String sendUpdateMenuReq(String token, String menuStr) {

		String url = "http://api.weixin.qq.com/cgi-bin/menu/create?access_token=" + 
				token;
		HttpClient client = new HttpClient();
	    PostMethod post = new PostMethod(url);
	    post.setRequestBody(menuStr);
	    post.getParams().setContentCharset("utf-8");
	    
	    String respStr = "";
	    try {
			client.executeMethod(post);
			respStr = post.getResponseBodyAsString();
		} catch (Exception e) {
			CommonLog.error(e.getMessage(),e);
			logger.error("sendUpdateMenuReqError", e);
			throw new RuntimeException(e);
		}finally{
			logger.info("weixinMenuResp", respStr);
			if(post!=null){
				post.releaseConnection();
				post = null;
			}
		}
	    
	    return respStr;
	}

	private String getToken(String tokenUrl) {
		String token = null;
		String respStr = null;
		HttpClient client = new HttpClient();
		GetMethod get = new GetMethod(tokenUrl);
		try {
			client.executeMethod(get);
			respStr = get.getResponseBodyAsString();
			JSONObject json = JSONObject.fromObject(respStr);
			token = json.get("access_token").toString();
		} catch (Exception e) {
			CommonLog.error(e.getMessage(),e);
			logger.error("getTokenError", e);
			throw new RuntimeException(e);
		}finally{
			logger.info("getTokenResp", respStr);
			if(get!=null){
				get.releaseConnection();
				get = null;
			}
		}
		return token;
	}

/*	@RequestMapping("weixin")
	public String weixin(HttpServletRequest request) {
		return "weinxing";
	}*/

}
