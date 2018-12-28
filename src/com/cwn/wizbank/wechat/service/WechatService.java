package com.cwn.wizbank.wechat.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cw.wizbank.Application;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.dao.Log4jFactory;
import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.entity.APIToken;
import com.cwn.wizbank.entity.AeApplication;
import com.cwn.wizbank.entity.AeItem;
import com.cwn.wizbank.entity.Article;
import com.cwn.wizbank.entity.ArticleType;
import com.cwn.wizbank.entity.ItemTargetLrnDetail;
import com.cwn.wizbank.entity.LearningSituation;
import com.cwn.wizbank.entity.Message;
import com.cwn.wizbank.entity.RegUser;
import com.cwn.wizbank.entity.TcTrainingCenter;
import com.cwn.wizbank.entity.UserCredits;
import com.cwn.wizbank.push.AppPushEngineImpl;
import com.cwn.wizbank.services.APITokenService;
import com.cwn.wizbank.services.AeApplicationService;
import com.cwn.wizbank.services.AeItemService;
import com.cwn.wizbank.services.ArticleService;
import com.cwn.wizbank.services.BaseService;
import com.cwn.wizbank.services.ItemTargetLrnDetailService;
import com.cwn.wizbank.services.LearningSituationService;
import com.cwn.wizbank.services.LoginService;
import com.cwn.wizbank.services.MessageService;
import com.cwn.wizbank.services.RegUserService;
import com.cwn.wizbank.services.TcTrainingCenterService;
import com.cwn.wizbank.services.UserCreditsService;
import com.cwn.wizbank.utils.CharUtils;
import com.cwn.wizbank.utils.CommonLog;
import com.cwn.wizbank.utils.DateUtil;
import com.cwn.wizbank.utils.Page;
import com.cwn.wizbank.web.WzbApplicationContext;
import com.cwn.wizbank.wechat.common.Static;
import com.cwn.wizbank.wechat.model.Authentication;
import com.cwn.wizbank.wechat.model.Base;
import com.cwn.wizbank.wechat.model.event.InputEvent;
import com.cwn.wizbank.wechat.model.music.OutputMusicItem;
import com.cwn.wizbank.wechat.model.news.OutputNewsItem;
import com.cwn.wizbank.wechat.model.text.InputText;
import com.cwn.wizbank.wechat.util.FileStringHelper;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class WechatService extends BaseService<APIToken>{
	
	private static final Logger logger = Log4jFactory.createLogger(WechatService.class);
	
	@Autowired
	WechatResponceService wechatResponseService;
	
	@Autowired
	APITokenService apiTokenService;
	
	@Autowired
	LoginService loginService;
	
	@Autowired
	MessageService messageService;
	
	@Autowired
	TcTrainingCenterService tcTrainingCenterService;
	
	@Autowired
	AeApplicationService aeApplicationService;
	
	@Autowired
	UserCreditsService userCreditsService;
	
	@Autowired
	AeItemService aeItemService;
	
	@Autowired
	LearningSituationService learningSituationService;
	
	@Autowired
	ItemTargetLrnDetailService itemTargetLrnDetailService;
	
	@Autowired
	ArticleService articleService;
	
	private String weChatTemplateId;
	
	public String getWeChatTemplateId() {
		return weChatTemplateId;
	}

	public void setWeChatTemplateId(String weChatTemplateId) {
		this.weChatTemplateId = weChatTemplateId;
	}


	private LinkedHashMap<String,String> menuMap;//菜单
	
	private final String MENU_ANNOUNCE_CODE = "1";	//公告
	private final String MENU_ARTICLE_CODE = "2";	//文章，行业资讯
	private final String MENU_SINGUP_COURSE_CODE = "3";	//已报名课程
	private final String MENU_SINGUP_EXAM_CODE = "4";	//已报名课程
	private final String MENU_RECOMMEND_COURSE_CODE = "5";	//推荐课程
	private final String MENU_OPEN_CODE = "6";	//公开课
	private final String MENU_TO_MOBILE_CODE = "7";	//进入到学习平台
	private final String MENU_UNBIND_CODE = "8";	//解除绑定
	private final String MENU_CREDIT_CODE = "9";	//我的积分
	private final String MENU_SUMMARY_CODE = "10";	//学习概况
	
	private final String MENU_HELP = "h";
	
	private final String MENU_BIND_CODE = "11";
	
	public static final int titleLimitLength = 30;//20 个字40个字符
	public static final int contentLimitLength = 150;//100 个字200个字符


	/**
	 * 封装微信提供的网址接入参数
	 * @param echostr
	 * @param signature
	 * @param timestamp
	 * @param nonce
	 * @return
	 */
	public Authentication setAuthentication(String echostr, String signature, 
			String timestamp, String nonce){
		Authentication authentication = null;
		if(StringUtils.isNotBlank(echostr)) {
			authentication = new Authentication();
			authentication.setEchostr(echostr);
			authentication.setSignature(signature);
			authentication.setTimestamp(timestamp);
			authentication.setNonce(nonce);
		}
		return authentication;
	}
	
	/**
	 * 微信参数
	 * 
	 * @param signature
	 *            微信加密签名
	 * @param timestamp
	 *            时间
	 * @param nonce
	 *            随机
	 * @return
	 */
	public boolean checkAuthentication(Authentication authentication) {
		String signature = authentication.getSignature();// 微信加密签名
		String timestamp = authentication.getTimestamp();// 时间
		String nonce = authentication.getNonce();// 随机
		String echostr = authentication.getEchostr();// 随机字符

		boolean isAuthentication = false;// 参数合法
		if (StringUtils.isBlank(signature) || StringUtils.isBlank(timestamp) || StringUtils.isBlank(nonce)) {
			return isAuthentication;
		}

		// 将获取到的参数放入数
		String[] ArrTmp = { Static.WEIXIN_TOKEN, timestamp, nonce };
		// 按微信提供的方法，对数据内容进行排序
		Arrays.sort(ArrTmp);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < ArrTmp.length; i++) {
			sb.append(ArrTmp[i]);
		}
		// 对排序后的字符串进行SHA-1加密
		String pwd = Encrypt(sb.toString());
		if (pwd.equals(signature)) {
			isAuthentication = true;
		}

		logger.info("微信服务器提供的参数，signature=" + signature + "; timestamp=" + timestamp + ";nonce:" + nonce + "; echostr=" + echostr + ";是否合法：" + isAuthentication);
		return isAuthentication;
	}

	
	/**
	 * 用SHA-1算法加密字符串并返回16进制
	 * 
	 * @param strSrc
	 * @return
	 */
	public String Encrypt(String strSrc) {
		MessageDigest md = null;
		String strDes = null;
		byte[] bt = strSrc.getBytes();
		try {
			md = MessageDigest.getInstance("SHA-1");
			md.update(bt);
			strDes = bytes2Hex(md.digest()); // to HexString
		} catch (NoSuchAlgorithmException e) {
			logger.error("Invalid algorithm.", e);
			return null;
		}
		return strDes;
	}
	
	/**
	 * 字节6进制
	 * 
	 * @param bts
	 * @return
	 */
	public static String bytes2Hex(byte[] bts) {
		String des = "";
		String tmp = null;
		for (int i = 0; i < bts.length; i++) {
			tmp = (Integer.toHexString(bts[i] & 0xFF));
			if (tmp.length() == 1) {
				des += "0";
			}
			des += tmp;
		}
		return des;
	}

	/**
	 * 从输入流读取post参数
	 * 
	 * @param in
	 * @return
	 */
	public String readStreamParameter(ServletInputStream in) {
		StringBuilder buffer = new StringBuilder();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			String line = null;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
		} catch (Exception e) {
			logger.error("readStreamParameter error", e);
		} finally {
			if (null != reader) {
				try {
					reader.close();
				} catch (IOException e) {
					logger.error("reader.close() error", e);
				}
			}
		}
		return buffer.toString();
	}
	
	/**
	 * 根据微信提交的xml，判断用户的留言消息类型
	 * 
	 * @param weixinServerPostXml
	 * @return
	 */
	public String getMsgType(String weixinServerPostXml) {
		String msgType = null;
		logger.info("weixinServerPostXml:" + weixinServerPostXml);
		if (StringUtils.isNotBlank(weixinServerPostXml)) {
			int indexOfS = weixinServerPostXml.indexOf(Static.MSG_TYPE_TAG_S);
			int indexOfE = weixinServerPostXml.indexOf(Static.MSG_TYPE_TAG_E);
			msgType = weixinServerPostXml.substring(indexOfS + Static.MSG_TYPE_TAG_S.length(), indexOfE);
		}
		logger.info("msgType:" + msgType);
		return msgType;
	}
	
	
	public String getBindNewsXml(WizbiniLoader wizbini, Base infoInput, String wizbankUserId, String fwhCode) {
		OutputNewsItem newsItem = new OutputNewsItem();
		newsItem.setTitle("点击立即绑定e-Learning账户，学习更轻松!");
		newsItem.setDescription("为了帮助您在微信中随时随地学习，建议您将e-Learning账户与本微信服务号进行一次快速绑定。绑定成功后，您就能在微信中查看公告、观看课程、参加考试。");
		newsItem.setPicUrl(Application.WECHAT_DOMAIN + Static.BIND_NEWS_PIC_URL);
		newsItem.setUrl(Application.WECHAT_DOMAIN +"/app" + Static.PATH_BIND_WIZBANK + "?fwhCode=" + fwhCode + "&userOpenId=" + wizbankUserId);
		String newsItemXml = wechatResponseService.buildNewsInfoXml(infoInput, newsItem);

		return newsItemXml;
	}
	
	public String getMobileStartXml(WizbiniLoader wizbini, Base infoInput, String wizbankUserId, String fwhCode, String token, loginProfile prof) {
		OutputNewsItem newsItem = new OutputNewsItem();
		newsItem.setTitle("点击进入到e-Learning平台，立即开始学习!");
		//newsItem.setDescription("为了帮助您在微信中随时随地学习，建议您将e-Learning账户与本微信服务号进行一次快速绑定。绑定成功后，您就能在微信中查看公告、观看课程、参加考试。");
		newsItem.setPicUrl(Application.WECHAT_DOMAIN + Static.BIND_NEWS_PIC_URL);
		String forwardUrl = Application.WECHAT_MOBILE_DOMAIN  + "views/forward.html?token=" + token + "&uid=" + prof.usr_ent_id ;
		newsItem.setUrl(forwardUrl);
		String newsItemXml = wechatResponseService.buildNewsInfoXml(infoInput, newsItem);

		return newsItemXml;
	}
	
	
	public String getHelpStartXml(WizbiniLoader wizbini, Base infoInput, String wizbankUserId, String fwhCode, String token, loginProfile prof) {
		List<OutputNewsItem> newsItems = new ArrayList<OutputNewsItem>();
		OutputNewsItem viewAllItem = new OutputNewsItem();
		String viewAllTitle = "点击大图查看汇思公司介绍";
		viewAllItem.setTitle(viewAllTitle);
		viewAllItem.setDescription(viewAllTitle);
		viewAllItem.setPicUrl(Application.WECHAT_DOMAIN + Static.HELP_ICON_URL);
		viewAllItem.setUrl(Application.WECHAT_DOMAIN + "/app/wechat/toHelp");
		newsItems.add(viewAllItem);// 微信图文列表第一项为”列表链接
		
		OutputNewsItem outputNewsItem = new OutputNewsItem();
		outputNewsItem.setTitle("进入学习平台，体验更多功能");
		outputNewsItem.setDescription("");
		outputNewsItem.setPicUrl("");
		String forwardUrl = Application.WECHAT_MOBILE_DOMAIN  + "views/forward.html?token=" + token + "&uid=" + prof.usr_ent_id ;
		outputNewsItem.setUrl(forwardUrl);
		newsItems.add(outputNewsItem);
		
		return wechatResponseService.buildNewsInfoXml(infoInput, newsItems);
	}
	
	
	/**
	 * 构文本消息xml
	 * 
	 * @param infoInput
	 * @param content
	 * @return
	 */
	public String getTextInfoXml(Base infoInput, String content) {
		return wechatResponseService.buildTextInfoXml(infoInput, content);
	}
	
	
	public String work(HttpServletRequest request, WizbiniLoader wizbini, loginProfile prof, String fwhCode) throws IOException {
		
		String responseXml = null;			//回复xml
		String userOpenId = null;		//微信用户的openid
		
		String serverPostXml = readStreamParameter(request.getInputStream());//微信服务器转发给wiz微信的的用户留言
		
		/************开启此功能虚拟微信留言为true，无需与微信服务器联调，即可本地联调最终发送给微信服务器的xml。 开始*****/
		boolean isUseVirtualweixinServerPostXml = false;
		if(isUseVirtualweixinServerPostXml){
			String filePath = request.getSession().getServletContext().getRealPath("/WEB-INF/classes/com/tiger/app/weixin/connector/action/testWeixinServerPostXml.txt");
			File file = new File(filePath);
			logger.info("filepath:"+file.getAbsolutePath());
			serverPostXml = FileStringHelper.file2String(file, "UTF-8");
		}
		/************开启此功能虚拟微信留言为true，无需与微信服务器联调，即可本地联调最终发送给微信服务器的xml。 结束*****/
		
		logger.info("serverPostXml:" + serverPostXml);
		String inputMsgType = getMsgType(serverPostXml);//用户的留言消息类型
		
		//处理用户消息
		if(StringUtils.isNotBlank(inputMsgType)){
			String cmdFromUser = null;//用户的请求
			Base infoInput = new Base();
			
			XStream inputXstream = new XStream(new DomDriver());
			if(Static.MSG_TYPE_TEXT.equals(inputMsgType)){
				inputXstream.processAnnotations(InputText.class);
				InputText infoInputText = (InputText)inputXstream.fromXML(serverPostXml);
				userOpenId = infoInputText.getFromUserName();//微信用户openId
				cmdFromUser = infoInputText.getContent();
				
				infoInput = infoInputText;
			}
			if(Static.MSG_TYPE_EVENT.equals(inputMsgType)){
				inputXstream.processAnnotations(InputEvent.class);
				InputEvent infoInputEvent = (InputEvent)inputXstream.fromXML(serverPostXml);
				userOpenId = infoInputEvent.getFromUserName();//微信用户openId
				
				String eventType = infoInputEvent.getEvent();//事件类型
				if(Static.MSG_TYPE_EVENT_CLICK.equals(eventType)){//自定义菜单事件
					cmdFromUser = infoInputEvent.getEventKey();
				}else if(Static.MSG_TYPE_EVENT_UNSUBSCRIBE.equals(eventType)){//取消订阅事件
					//记录某个绑定学员取消订阅
				}
				
				infoInput = infoInputEvent;
			}
			
			logger.info("userOpenId:" + userOpenId);
			
			if(null == menuMap){
				menuMap = new LinkedHashMap<String,String>();//主要菜单
				menuMap.put(MENU_ANNOUNCE_CODE, "【1】 公告\r\n");
				menuMap.put(MENU_ARTICLE_CODE, "【2】 行业资讯\r\n");
				menuMap.put(MENU_SINGUP_COURSE_CODE, "【3】 已报名课程\r\n");
				menuMap.put(MENU_SINGUP_EXAM_CODE, "【4】 已报名考试\r\n");
				menuMap.put(MENU_RECOMMEND_COURSE_CODE, "【5】 推荐课程\r\n");
				menuMap.put(MENU_OPEN_CODE, "【6】公开课\r\n");
				menuMap.put(MENU_TO_MOBILE_CODE, "【7】进入到学习平台\r\n");
				menuMap.put(MENU_UNBIND_CODE, "【8】 解除绑定\r\n");
				menuMap.put(MENU_CREDIT_CODE, "【9】 我的学习积分\r\n");
				menuMap.put(MENU_SUMMARY_CODE, "【10】学习概况\r\n");
				menuMap.put(MENU_BIND_CODE, "【11】 绑定账号\r\n");
				menuMap.put(MENU_HELP, "【h】帮助");
			}
				
			//用户是否正在使用功能
			boolean isUsingFunction = menuMap.containsKey(cmdFromUser);
			
			APIToken weixinUser = apiTokenService.getByWechatOpenId(userOpenId);//根据微信openId获取wiz微信用户信息
			String originalWeixinUserOpenId = userOpenId;		//原始微信ID，如不开启使用体验模式控制，则与weixinUserOpenId一致
			boolean isUseTrialAccount = false;		//是否正在体验模式
			if(null == weixinUser){		//对于未绑定的用户,尝试使用体验模式控制
				//是否使用体验账号中
				if("offline".equals(fwhCode) && Static.CHAR_TRUE.equals(isUseTrialAccount)){
					userOpenId = "oIH12jqtQS2YiEWKUuGlIi-p4vJM";
					weixinUser = apiTokenService.getByWechatOpenId(userOpenId);
					if(null != weixinUser){
						isUseTrialAccount = true;
						logger.info("账号体验模式已开启，借用账号:" + weixinUser.getAtk_usr_id() +" 作为体验账号");
					} else {
						logger.warn("体验账号不存在！");
					}
				}
			}
			
			String welcome = getWelcome(menuMap);
			
			if(null == weixinUser){
				responseXml = getBindNewsXml(wizbini, infoInput, originalWeixinUserOpenId,fwhCode);//如果微信用户始终为空，则引导用户去做微信绑定
			} else {//对于未绑定的用户
				String wizbankUserName = weixinUser.getAtk_usr_ent_id() + "";//wizbank用户名称
				String token = weixinUser.getAtk_id();// wizbank token
				if(weixinUser.getUser() != null){
					wizbankUserName = weixinUser.getUser().getUsr_display_bil();
				}
				if(!isUsingFunction){
					StringBuffer contentBf = new StringBuffer();
					contentBf.append("您好").append(wizbankUserName).append(" ").append(welcome);
					responseXml = getTextInfoXml(infoInput, contentBf.toString());
				} else {
					try{
						if(prof == null)  prof = new loginProfile();
						loginService.initProfFromToken(prof, token);
					} catch (Exception e) {
						//TODOtoken过期，请重新绑定
						CommonLog.error(e.getMessage(),e);
						logger.debug(e.getMessage());
						if(MENU_BIND_CODE.equals(cmdFromUser)){
							responseXml = getBindNewsXml(wizbini, infoInput, originalWeixinUserOpenId, fwhCode);//如果微信用户始终为空，则引导用户去做微信绑定
						} else{
							responseXml = getTextInfoXml(infoInput, "身份信息已过期，请重新绑定！");
						}
						return responseXml;							
					}
					
					//处理命令
					return  responseCommond(cmdFromUser, wizbini, prof, infoInput, userOpenId, token, fwhCode, isUseTrialAccount, request);
				}
			
			}
			
			logger.info("向微信openId 为 "+userOpenId+ " 的用户，发送 xml：\r\n"+responseXml);
			return responseXml;
			//=============回复 E================
		
		}
		return null;
	}

	/**
	 * 获取欢迎语
	 * @return
	 */
	private String getWelcome(Map<String, String> menuMap) {
		StringBuilder welcome = new StringBuilder("\r\n亲，微信学习精灵欢迎您!/::)\r");//欢迎语
		welcome.append("您可以回复以下菜单序号，快速获取相应服务：\r\n\r\n");
		welcome.append("【" + MENU_ANNOUNCE_CODE + "】 公告\r\n");
		welcome.append("【" + MENU_ARTICLE_CODE + "】 行业资讯\r\n");
		welcome.append("【" + MENU_SINGUP_COURSE_CODE + "】 已报名课程\r\n");
		welcome.append("【" + MENU_SINGUP_EXAM_CODE + "】 已报名考试\r\n");
		welcome.append("【" + MENU_RECOMMEND_COURSE_CODE + "】 推荐课程\r\n");
		welcome.append("【" + MENU_OPEN_CODE + "】公开课\r\n");
		welcome.append("【" + MENU_TO_MOBILE_CODE + "】进入到学习平台\r\n");
		welcome.append("【" + MENU_UNBIND_CODE + "】 解除绑定\r\n");
		welcome.append("【" + MENU_CREDIT_CODE + "】 我的学习积分\r\n");
		welcome.append("【" + MENU_SUMMARY_CODE + "】学习概况\r\n");
		welcome.append("【" + MENU_HELP + "】帮助\r\n\r\n");

/*		for (Map.Entry<String, String> entry : menuMap.entrySet()) {
			welcome.append("【" + entry.getKey() + "】" + entry.getValue() + "\r\n");
		}*/
		welcome.append("快行动吧！GO");
		return welcome.toString();
	}

	/**
	 * 处理用户请求
	 * @param cmdFromUser
	 * @param prof
	 * @param infoInput
	 * @param userOpenId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public String responseCommond(String cmdFromUser, WizbiniLoader wizbini, loginProfile prof,
			Base infoInput, String userOpenId, String token, String fwhCode, boolean isUseTrialAccount, HttpServletRequest request) {
		String responseXml = null;
		Page<?> page = new Page();
		if(MENU_ANNOUNCE_CODE.equals(cmdFromUser)) {
			List<OutputNewsItem> newsItemList = this.getMessageNews(page, prof, wizbini, token);
			responseXml = wechatResponseService.buildNewsInfoXml(infoInput, newsItemList);
		} else if (MENU_ARTICLE_CODE.equals(cmdFromUser)) {
			//我的微课程
			List<OutputNewsItem> newsItemList = this.getArticleNews(page, prof, wizbini, token, true);
			responseXml = wechatResponseService.buildNewsInfoXml(infoInput, newsItemList);
		} else if (MENU_SINGUP_COURSE_CODE.equals(cmdFromUser)) {
			//我的微课程
			List<OutputNewsItem> newsItemList = this.getSignupCourseNews(page, prof, wizbini, token, true);
			responseXml = wechatResponseService.buildNewsInfoXml(infoInput, newsItemList);
		} else if (MENU_SINGUP_EXAM_CODE.equals(cmdFromUser)) {
			//我的微考试
			List<OutputNewsItem> newsItemList = this.getSignupCourseNews(page, prof, wizbini, token, false);
			responseXml = wechatResponseService.buildNewsInfoXml(infoInput, newsItemList);
		} else if (MENU_RECOMMEND_COURSE_CODE.equals(cmdFromUser)){
			List<OutputNewsItem> newsItemList = this.getRecommendCourseNews(page, prof, wizbini, token, true);
			responseXml = wechatResponseService.buildNewsInfoXml(infoInput, newsItemList);
		} else if (MENU_OPEN_CODE.equals(cmdFromUser)){
			List<OutputNewsItem> newsItemList = this.getOpenCourseNews(page, prof, wizbini, token, true);
			responseXml = wechatResponseService.buildNewsInfoXml(infoInput, newsItemList);
		} else if (MENU_CREDIT_CODE.equals(cmdFromUser)) {
			//我的学习积分
			String wizbankUserCreditInfo = getWizbankUserCreditInfo(prof, wizbini, userOpenId);
			responseXml = wechatResponseService.buildTextInfoXml(infoInput, wizbankUserCreditInfo);
		} else if (MENU_CREDIT_CODE.equals(cmdFromUser)) {
			//我的个人信息
			String weixinUserInfo = getWinxinUserInfo(prof);
			responseXml = wechatResponseService.buildTextInfoXml(infoInput, weixinUserInfo);
		} else if (MENU_SUMMARY_CODE.equals(cmdFromUser)){
			//我的学习概况
			String summary = getUserSummaryNewsXml(prof);
			responseXml = wechatResponseService.buildTextInfoXml(infoInput, summary);
		} else if (MENU_BIND_CODE.equals(cmdFromUser)) {
			//绑定账号
			//为预防处于体验模式时，用户使用体验账号的微信id去做微信绑定，所以必须用originalWeixinUserOpenId
			responseXml = getBindNewsXml(wizbini, infoInput, userOpenId, fwhCode);
		} else if(MENU_TO_MOBILE_CODE.equals(cmdFromUser)){
			responseXml = getMobileStartXml(wizbini, infoInput, userOpenId, fwhCode, token, prof);
		} else if (MENU_UNBIND_CODE.equals(cmdFromUser)) {
			//解除微信绑定
			//是否使用体验账号
			if(isUseTrialAccount){
				responseXml = wechatResponseService.buildTextInfoXml(infoInput, "您当前处于账号体验模式，不允许使用此功能!");
			}else{
				request.getSession().invalidate();
				boolean isUnbindOk = unbindWizbank(userOpenId);//是否解绑成功
				if(isUnbindOk){
					responseXml = wechatResponseService.buildTextInfoXml(infoInput, "成功解除微信绑定!");
				}else{
					responseXml = wechatResponseService.buildTextInfoXml(infoInput, "无法解除微信绑定!");
				}
			}
		} else if(MENU_HELP.equalsIgnoreCase(cmdFromUser)){
			responseXml = getHelpStartXml(wizbini, infoInput, userOpenId, fwhCode, token, prof);
		}
		return responseXml;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<OutputNewsItem> getArticleNews(Page page,
			loginProfile prof, WizbiniLoader wizbini, String token, boolean b) {
		List<OutputNewsItem> newItemList = null;
		page.setPageSize(Application.WECHAT_MAX_MESSAGE);
		List<ArticleType> list = articleService.getarticleType(prof.my_top_tc_id);
		List<Long> aty_id_list = new ArrayList<Long>();
		aty_id_list.add(0l);
		for(int i=0;i<list.size();i++){
			aty_id_list.add(list.get(i).getAty_id());
		}
		page.setPageSize(Application.WECHAT_MAX_MESSAGE);
		articleService.pageArticle(prof.usr_ent_id, 1, page, prof.my_top_tc_id, aty_id_list, wizbini.cfgSysSetupadv.getNewestDuration());
		List<Article> msgList = page.getResults(); 
		if(msgList !=null && msgList.size()>0){
			newItemList = new ArrayList<OutputNewsItem>();
			OutputNewsItem viewAllItem = new OutputNewsItem();
			String viewAllTitle = "点击大图查看更多文章";
			String viewAllDescription = viewAllTitle;
			viewAllItem.setTitle(viewAllTitle);
			viewAllItem.setDescription(viewAllDescription);
			//FIXME  更改图片
			viewAllItem.setPicUrl(Application.WECHAT_DOMAIN  + Static.ARTICLE_LIST_ICON_URL);
			// 跳转的url
			String forwardUrl = Application.WECHAT_MOBILE_DOMAIN  + "views/forward.html";
			String viewAllUrl = forwardUrl + "?token=" + token + "&url=" + Static.WIZMOBILE_PAGE_ARTICLE_LIST + "&uid=" + prof.usr_ent_id ;// 点击图文消息跳转链接
			viewAllItem.setUrl(viewAllUrl);
			newItemList.add(viewAllItem);// 微信图文列表第一项为”列表链接
			for (Article obj : msgList) {
				String title = obj.getArt_title();// 图文消息标题
				//FIXME  更改图片
				String thumbnail = Application.WECHAT_DOMAIN + Static.ANNOUNCEMENT_ITEM_ICON_URL;// 公告图片
				int id = obj.getArt_id(); // ID
				String description = title;// 图文消息描述
				String picUrl = thumbnail;// 图片链接，支持JPG、PNG格式，较好的效果为大40*320，小0*80
				String url = forwardUrl + "?token=" + token + "&id=" + id + "&url=" + Static.WIZMOBILE_PAGE_ARTICLE_DETAIL + "&uid=" + prof.usr_ent_id ;// 点击图文消息跳转链接
				OutputNewsItem outputNewsItem = new OutputNewsItem();
				outputNewsItem.setTitle(title);
				outputNewsItem.setDescription(description);
				outputNewsItem.setPicUrl(picUrl);
				outputNewsItem.setUrl(url);
				newItemList.add(outputNewsItem);
			}
			
		}
		return newItemList;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<OutputNewsItem> getOpenCourseNews(Page page,
			loginProfile prof, WizbiniLoader wizbini, String token, boolean b) {
		List<OutputNewsItem> newsItems = null;
		page.setPageSize(Application.WECHAT_MAX_MESSAGE);
		aeItemService.getOpens(page, prof.my_top_tc_id, prof.usr_ent_id);
		List<AeItem> aeItems = page.getResults();
		if(aeItems != null && aeItems.size() > 0){
			newsItems = new ArrayList<OutputNewsItem>();
			String listPageFileName =  Static.WIZMOBILE_PAGE_OPEN_LIST;// 微信图文的列表url
			String detailPageFileName = Static.WIZMOBILE_PAGE_OPEN_DETAIL ;// 微信图文的子项url
			// 跳转的url
			String forwardUrl = Application.WECHAT_MOBILE_DOMAIN  + "views/forward.html";
			String viewAllPicUrl = Application.WECHAT_DOMAIN + Static.OPEN_LIST_ICON_URL;
			OutputNewsItem viewAllItem = new OutputNewsItem();
			String viewAllTitle ="点击大图查看更多公开课";
			viewAllItem.setTitle(viewAllTitle);
			viewAllItem.setDescription(viewAllTitle);
			viewAllItem.setPicUrl(viewAllPicUrl);
			String viewAllUrl = forwardUrl + "?token=" + token + "&url=" + listPageFileName + "&uid=" + prof.usr_ent_id ;// 点击图文消息跳转链接
			viewAllItem.setUrl(viewAllUrl);
			newsItems.add(viewAllItem);// 微信图文列表第一项为”列表链接
			for (AeItem item : aeItems) {
				String title = item.getItm_title();// 图文消息标题
				//Long cos_id = item.getCov().getCov_cos_id();// 课程cos_res_id
				String thumbnail = item.getItm_icon();// 课程标题缩略图?
				if (StringUtils.isNotBlank(thumbnail)) {
					if (-1 != thumbnail.indexOf("..")) {
						thumbnail = StringUtils.removeStart(thumbnail, "..");
						thumbnail = thumbnail.replaceAll("\\\\", "/");
					}
					thumbnail = Application.WECHAT_DOMAIN + thumbnail;
				} else {
					thumbnail = Application.WECHAT_DOMAIN + Static.EXAM_ITEM_ICON_URL;
				}
				String description = title;// 图文消息描述
				String picUrl = thumbnail;// 图片链接，支持JPG、PNG格式，较好的效果为大40*320，小0*80
				String url = forwardUrl + "?token=" + token + "&url=" + detailPageFileName + "&id=" + item.getItm_id() + "&uid=" + prof.usr_ent_id ;// 点击图文消息跳转链接
				if(item.getApp() != null && item.getApp().getApp_tkh_id() != null && item.getApp().getApp_tkh_id() > 0) {
					url += "&tkhId="+ item.getApp().getApp_tkh_id();
				}
				OutputNewsItem outputNewsItem = new OutputNewsItem();
				outputNewsItem.setTitle(title);
				outputNewsItem.setDescription(description);
				outputNewsItem.setPicUrl(picUrl);
				outputNewsItem.setUrl(url);
				newsItems.add(outputNewsItem);
			}
			
		}
		return newsItems;
	}

	/**
	 * 获取课程概况信息
	 * @param prof
	 * @param wizbini
	 * @param infoInput
	 * @return
	 */
	private String getUserSummaryNewsXml(loginProfile prof) {
		LearningSituation ls = learningSituationService.getLearningSituation(prof.usr_ent_id);
		StringBuilder sb = new StringBuilder();
		sb.append("学习总时长：");
		if(ls.getLs_learn_duration() == null) {
			ls.setLs_learn_duration(0l);
		}
		sb.append(DateUtil.getInstance().secToTime(ls.getLs_learn_duration())).append("\r\n");
		
		sb.append("总学分：");
		sb.append(ls.getLs_learn_credit()).append("\r\n");
		
		sb.append("总积分：");
		sb.append(ls.getLs_total_integral()).append("\r\n");
		
		sb.append("课程总数：");
		sb.append(ls.getLs_total_courses()).append("\r\n");
		
		sb.append("已完成课程总数：");
		sb.append(ls.getLs_course_completed_num()).append("\r\n");
		
		sb.append("未完成课程总数：");
		sb.append(ls.getLs_course_fail_num()).append("\r\n");
		
		sb.append("进行中课程总数：");
		sb.append(ls.getLs_course_inprogress_num()).append("\r\n");
		
		sb.append("审批中课程总数：");
		sb.append(ls.getLs_course_pending_num()).append("\r\n");
		
		sb.append("考试总数：");
		sb.append(ls.getLs_total_exams()).append("\r\n");
		
		sb.append("已完成考试总数：");
		sb.append(ls.getLs_exam_completed_num()).append("\r\n");
		
		sb.append("未完成考试总数：");
		sb.append(ls.getLs_exam_fail_num()).append("\r\n");
		
		sb.append("进行中考试总数：");
		sb.append(ls.getLs_exam_inprogress_num()).append("\r\n");
		
		sb.append("审批中考试总数：");
		sb.append(ls.getLs_exam_pending_num()).append("\r\n");
		
		return sb.toString();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<OutputNewsItem> getMessageNews(Page page, loginProfile prof, WizbiniLoader wizbini, String token){	
		List<Long> tcr_id_list = new ArrayList<Long>();
		List<OutputNewsItem> newItemList = null;

		List<TcTrainingCenter> list = tcTrainingCenterService.getTcrIdList(prof.usr_ent_id, prof.my_top_tc_id);
		for(int i=0;i<list.size();i++){
			tcr_id_list.add(list.get(i).getTcr_id());
		}
		
		page.setPageSize(Application.WECHAT_MAX_MESSAGE);
		messageService.pageMessage(prof.getUsr_ent_id(),tcr_id_list, 1l, false, page, wizbini.cfgSysSetupadv.getNewestDuration());
		List<Message> msgList = page.getResults(); 
		if(msgList !=null && msgList.size()>0){
			newItemList = new ArrayList<OutputNewsItem>();
			OutputNewsItem viewAllItem = new OutputNewsItem();
			String viewAllTitle = "点击大图查看更多培训公告";
			String viewAllDescription = viewAllTitle;
			viewAllItem.setTitle(viewAllTitle);
			viewAllItem.setDescription(viewAllDescription);
			viewAllItem.setPicUrl(Application.WECHAT_DOMAIN  + Static.ANNOUNCEMENT_LIST_ICON_URL);
			// 跳转的url
			String forwardUrl = Application.WECHAT_MOBILE_DOMAIN + "views/forward.html";
			String viewAllUrl = forwardUrl + "?token=" + token + "&url=" + Static.WIZMOBILE_PAGE_ANNOUNCEMENT_LIST+ "&uid=" + prof.usr_ent_id ;// 点击图文消息跳转链接
			viewAllItem.setUrl(viewAllUrl);
			newItemList.add(viewAllItem);// 微信图文列表第一项为”列表链接
			for (Message msg : msgList) {
				String title = msg.getMsg_title();// 图文消息标题
				//String thumbnail = wizbini.cfgSysSetupadv.getWechatConf().getDomainName() + Static.ANNOUNCEMENT_ITEM_ICON_URL;// 公告图片
				Long msg_id = msg.getMsg_id();// 公告ID
				String description = title;// 图文消息描述
				//String picUrl = thumbnail;// 图片链接，支持JPG、PNG格式，较好的效果为大40*320，小0*80
				String url = forwardUrl + "?token=" + token + "&id=" + msg_id + "&url=" + Static.WIZMOBILE_PAGE_ANNOUNCEMENT_DETAIL+ "&uid=" + prof.usr_ent_id ;// 点击图文消息跳转链接
				OutputNewsItem outputNewsItem = new OutputNewsItem();
				outputNewsItem.setTitle(title);
				outputNewsItem.setDescription(description);
				//outputNewsItem.setPicUrl(picUrl);
				outputNewsItem.setUrl(url);
				newItemList.add(outputNewsItem);
			}
			
		}
		return newItemList;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<OutputNewsItem> getSignupCourseNews(Page page, loginProfile prof, WizbiniLoader wizbini, String token, boolean isGetCourse){
		List<OutputNewsItem> newsItems = null;
		page.getParams().put("isExam", isGetCourse ? 0 : 1);
		page.getParams().put("appStatus", "inprogeress");
		page.setPageSize(Application.WECHAT_MAX_MESSAGE);
		aeApplicationService.signup(prof.usr_ent_id, prof.cur_lan, page);
		List<AeApplication> aeItems = page.getResults();
		if(aeItems != null && aeItems.size() > 0){
			newsItems = new ArrayList<OutputNewsItem>();
			String listPageFileName = isGetCourse ? Static.WIZMOBILE_PAGE_COURSE_LIST : Static.WIZMOBILE_PAGE_TEST_LIST;// 微信图文的列表url
			String detailPageFileName = isGetCourse ? Static.WIZMOBILE_PAGE_COURSE_DETAIL : Static.WIZMOBILE_PAGE_TEST_DETAIL;// 微信图文的子项url
			// 跳转的url
			String forwardUrl = Application.WECHAT_MOBILE_DOMAIN  + "views/forward.html";
			String viewAllPicUrl = Application.WECHAT_DOMAIN + (isGetCourse ? Static.COURSE_LIST_ICON_URL : Static.EXAM_LIST_ICON_URL);
			OutputNewsItem viewAllItem = new OutputNewsItem();
			String viewAllTitle = isGetCourse ? "点击大图查看更多微信课程" : "点击大图查看更多微信考试";
			viewAllItem.setTitle(viewAllTitle);
			viewAllItem.setDescription(viewAllTitle);
			viewAllItem.setPicUrl(viewAllPicUrl);
			String viewAllUrl = forwardUrl + "?token=" + token + "&url=" + listPageFileName+ "&uid=" + prof.usr_ent_id ;// 点击图文消息跳转链接
			viewAllItem.setUrl(viewAllUrl);
			newsItems.add(viewAllItem);// 微信图文列表第一项为”列表链接
			for (AeApplication item : aeItems) {
				String title = item.getItem().getItm_title();// 图文消息标题
				Long tkh_id = item.getApp_tkh_id();// 课程学习跟踪ID
				//Long cos_id = item.getCov().getCov_cos_id();// 课程cos_res_id
				String thumbnail = item.getItem().getItm_icon();// 课程标题缩略图?
				if (StringUtils.isNotBlank(thumbnail)) {
					if (-1 != thumbnail.indexOf("..")) {
						thumbnail = StringUtils.removeStart(thumbnail, "..");
						thumbnail = thumbnail.replaceAll("\\\\", "/");
					}
					thumbnail = Application.WECHAT_DOMAIN + thumbnail;
				} else {
					thumbnail = Application.WECHAT_DOMAIN + (isGetCourse ? Static.COURSE_ITEM_ICON_URL : Static.EXAM_ITEM_ICON_URL);
				}
				String description = title;// 图文消息描述
				String picUrl = thumbnail;// 图片链接，支持JPG、PNG格式，较好的效果为大40*320，小0*80
				String url = forwardUrl + "?token=" + token + "&id=" + item.getItem().getItm_id() + "&tkhId=" + tkh_id + "&url=" + detailPageFileName+ "&uid=" + prof.usr_ent_id ;// 点击图文消息跳转链接
				OutputNewsItem outputNewsItem = new OutputNewsItem();
				outputNewsItem.setTitle(title);
				outputNewsItem.setDescription(description);
				outputNewsItem.setPicUrl(picUrl);
				outputNewsItem.setUrl(url);
				newsItems.add(outputNewsItem);
			}
			
		}
		return newsItems;
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<OutputNewsItem> getRecommendCourseNews(Page page, loginProfile prof, WizbiniLoader wizbini, String token, boolean isGetCourse){
		List<OutputNewsItem> newsItems = null;
		page.getParams().put("isExam", isGetCourse ? 0 : 1);
		long tcrId = 0;
		if(page.getParams().get("tcrId") == null || Integer.parseInt((String)page.getParams().get("tcrId")) < 1){
			if (wizbini.cfgSysSetupadv.isTcIndependent()) {
				tcrId = prof.my_top_tc_id;
			}
			page.getParams().put("tcrId", tcrId);
		}
		page.setPageSize(Application.WECHAT_MAX_MESSAGE);
		itemTargetLrnDetailService.recommend(prof.usr_ent_id, prof.cur_lan, page);
		List<ItemTargetLrnDetail> aeItems = page.getResults();
		if(aeItems != null && aeItems.size() > 0){
			newsItems = new ArrayList<OutputNewsItem>();
			String listPageFileName = Static.WIZMOBILE_PAGE_RECOMMEND_LIST;//isGetCourse ? Static.WIZMOBILE_PAGE_RECOMMEND_LIST : Static.WIZMOBILE_PAGE_RECOMMEND_LIST;// 微信图文的列表url
			String detailPageFileName = isGetCourse ? Static.WIZMOBILE_PAGE_COURSE_DETAIL : Static.WIZMOBILE_PAGE_TEST_DETAIL;// 微信图文的子项url
			// 跳转的url
			String forwardUrl = Application.WECHAT_MOBILE_DOMAIN  + "views/forward.html";
			String viewAllPicUrl = Application.WECHAT_DOMAIN + (isGetCourse ? Static.RECOMMEND_LIST_ICON_URL : Static.EXAM_LIST_ICON_URL);
			OutputNewsItem viewAllItem = new OutputNewsItem();
			String viewAllTitle = isGetCourse ? "点击大图查看更多推荐课程" : "点击大图查看更多推荐考试";
			viewAllItem.setTitle(viewAllTitle);
			viewAllItem.setDescription(viewAllTitle);
			viewAllItem.setPicUrl(viewAllPicUrl);
			String viewAllUrl = forwardUrl + "?token=" + token + "&url=" + listPageFileName+ "&uid=" + prof.usr_ent_id  ;// 点击图文消息跳转链接
			viewAllItem.setUrl(viewAllUrl);
			newsItems.add(viewAllItem);// 微信图文列表第一项为”列表链接
			for (ItemTargetLrnDetail item : aeItems) {
				String title = item.getItem().getItm_title();	// 图文消息标题
				Long tkh_id = 0l;		// 课程学习跟踪ID
				if(item.getApp() != null && item.getApp().getApp_id() != null) {
					tkh_id = item.getApp().getApp_id();
				}
				String thumbnail = item.getItem().getItm_icon();// 课程标题缩略图?
				if (StringUtils.isNotBlank(thumbnail)) {
					if (-1 != thumbnail.indexOf("..")) {
						thumbnail = StringUtils.removeStart(thumbnail, "..");
						thumbnail = thumbnail.replaceAll("\\\\", "/");
					}
					thumbnail = Application.WECHAT_DOMAIN + thumbnail;
				} else {
					thumbnail = Application.WECHAT_DOMAIN + (isGetCourse ? Static.COURSE_ITEM_ICON_URL : Static.EXAM_ITEM_ICON_URL);
				}
				String description = title;// 图文消息描述
				String picUrl = thumbnail;// 图片链接，支持JPG、PNG格式，较好的效果为大40*320，小0*80
				String url = forwardUrl + "?token=" + token + "&id="+ item.getItem().getItm_id() +"&tkhIid=" + tkh_id + "&url=" + detailPageFileName+ "&uid=" + prof.usr_ent_id;// 点击图文消息跳转链接
				OutputNewsItem outputNewsItem = new OutputNewsItem();
				outputNewsItem.setTitle(title);
				outputNewsItem.setDescription(description);
				outputNewsItem.setPicUrl(picUrl);
				outputNewsItem.setUrl(url);
				newsItems.add(outputNewsItem);
			}
			
		}
		return newsItems;
	}
	
	public String getWizbankUserCreditInfo(loginProfile prof, WizbiniLoader wizbini, String userOpenId){
		StringBuffer wizbankUserCreditInfo = new StringBuffer();
		if(StringUtils.isNotBlank(userOpenId)){
			long tcr_id =  prof.root_ent_id;
			if(wizbini.cfgSysSetupadv.isTcIndependent()){
				tcr_id = prof.my_top_tc_id;
			}
			UserCredits wizbankUserCredit = userCreditsService.getUserCreditAndRank(prof.usr_ent_id, tcr_id);
			wizbankUserCreditInfo.append("您的学习积分:").append(wizbankUserCredit!=null?wizbankUserCredit.getUct_total():0).append("分");
		}
		return wizbankUserCreditInfo.toString();
	}
	
	public String getWinxinUserInfo(loginProfile prof){
		StringBuffer wechatUserInfo = new StringBuffer();		
		if (null != prof) {
			wechatUserInfo.append("wizbank账户：" + prof.getUsr_id() + "\r\n");
			wechatUserInfo.append("wizbank姓名：" + prof.getUsr_display_bil() + "\r\n");
		}
		return wechatUserInfo.toString();
	}
	
	/**
	 * 获取音乐图文消息
	 * 
	 * @param infoInput
	 * @return
	 */
	public String getMusicNewsXml(WizbiniLoader wizbini, Base infoInput){
		//String musicUrl = "http://qd.cache.baidupcs.com/file/59f76564d1759b1eb4b6fd367955f5f1?xcode=215d0e3ba47a25820b56efbee62a45f7&fid=3342165865-250528-3861767624&time=1371450120&sign=FDTAXER-DCb740ccc5511e5e8fedcff06b081203-NYm6vUrhEtBgafJL8qJq0%2BzGLIQ%3D&to=hc,qc&fm=N,Q,U&expires=8h&rt=sh&r=743666543&logid=1459958038&sh=1";
		//String hqMusicUrl = "http://qd.cache.baidupcs.com/file/59f76564d1759b1eb4b6fd367955f5f1?xcode=215d0e3ba47a25820b56efbee62a45f7&fid=3342165865-250528-3861767624&time=1371450120&sign=FDTAXER-DCb740ccc5511e5e8fedcff06b081203-NYm6vUrhEtBgafJL8qJq0%2BzGLIQ%3D&to=hc,qc&fm=N,Q,U&expires=8h&rt=sh&r=743666543&logid=1459958038&sh=1";
		
		String musicUrl = Application.WECHAT_DOMAIN+"/content/PriceTag.mp3";
		String hqMusicUrl = musicUrl;		
		OutputMusicItem musicItem = new OutputMusicItem();
		musicItem.setTitle("音频点播：Price Tag");
		musicItem.setDescription("Price Tag是Jessie J录音室专辑《Who You Are》中第二波单曲，与美国当红新人B.O.B合作的， 发行首周即空降英国单曲榜冠军宝座，这是非常令人惊喜的成绩!");
		musicItem.setMusicUrl(musicUrl);//音乐链接
		musicItem.setHqMusicUrl(hqMusicUrl);//高质量音乐链接，WIFI环境优先使用该链接播放音乐		
		String newsItemXml = wechatResponseService.buildMusicInfoXml(infoInput, musicItem);		
		return newsItemXml;
	}
	
	/**
	 * 获取课程图文消息，videoJs视频点播
	 * 
	 * @param infoInput
	 * @return
	 */
	public String getVideoJsNewsXml(WizbiniLoader wizbini, Base infoInput){
		OutputNewsItem newsItem = new OutputNewsItem();
		newsItem.setTitle("课程名称：未来全息技术的发展");
		newsItem.setDescription("全息（Holography）（来自于拉丁词汇，whole+ drawing的复合），特指一种技术，这种技术可以让从物体发射的衍射光被重现，其位置和大小同之前一模一样。从不同的位置观测此物体，其显示的像也会变化。因此全息图像是三维的。");
		newsItem.setPicUrl("http://img3.douban.com/view/photo/photo/public/p2002999097.jpg");
		String coursePath = Application.WECHAT_MOBILE_DOMAIN + "/app/wizWeixin/toCoursePage";
		newsItem.setUrl(coursePath);		
		String newsItemXml = wechatResponseService.buildNewsInfoXml(infoInput, newsItem);		
		return newsItemXml;
	}

	/**
	 * 获取课程图文消息，新用户注册
	 * 
	 * @param infoInput
	 * @return
	 */
	public String getWizbankRegisterNewsXml(WizbiniLoader wizbini, Base infoInput) {
		OutputNewsItem newsItem = new OutputNewsItem();
		newsItem.setTitle("如需访问PC版，请点击这里！");
		newsItem.setDescription("如需访问PC版，请点击这里！");
		newsItem.setPicUrl(Application.WECHAT_MOBILE_DOMAIN + Static.COURSE_ITEM_ICON_URL);
		String url = "http://offline.cyberwisdom.net.cn";
		newsItem.setUrl(url);
		String newsItemXml = wechatResponseService.buildNewsInfoXml(infoInput, newsItem);
		return newsItemXml;
	}

	/**
	 * 微信解绑
	 * @param weixinOpenId
	 * @return
	 */
	public boolean unbindWizbank(String userOpenId){
		boolean isOk = false;
		
		if(StringUtils.isNotBlank(userOpenId)){
			 APIToken apiToken = apiTokenService.getByWechatOpenId(userOpenId);
			if(null != apiToken){
				String id = apiToken.getAtk_id();
				try {
					apiTokenService.delete(id);
					isOk = true;
				} catch (Exception e) {
					logger.error("unbindWizbank error! userOpenId:"+userOpenId, e);
				}
			}
		}
		
		return isOk;
	}
	
	
	/**
	 * 微信解绑通过用户登录ID
	 * @param weixinOpenId
	 * @return
	 */
	public boolean unbindWizbankByUserSteEntId(String userSteEntId){
		boolean isOk = false;
		if(StringUtils.isNotBlank(userSteEntId)){
			List<APIToken> userBindList = apiTokenService.getList(userSteEntId, APIToken.API_DEVELOPER_WEIXIN, null);
			for(APIToken a : userBindList){
				try {
				//删除之前的绑定
				 apiTokenService.delete(a.getAtk_id());
				}catch (Exception e) {
					CommonLog.error(e.getMessage(),e);
					logger.error("unbindWizbankByUserSteEntId error! userSteEntId:"+userSteEntId, e);
				}
			}
		}
		
		return isOk;
	}
	
	public String sendTemplate(JSONObject jobj){
		if(jobj == null){
			logger.info("推送到微信失败，传递模板参数为空");
			return null;
		}
		
		if(Application.WECHAT_TOKEN_URL == null || Application.WECHAT_TOKEN_URL.equals("")){
			logger.info("你还没有配置获取微信token的URL，请登录系统管理员设置");
			return null;
		}
		
//		String tokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx562a47405070bf12&secret=ba8a300310bc429e1059ed9294841a28";
		String tokenUrl = Application.WECHAT_TOKEN_URL.trim();
		String token = getToken(tokenUrl);
		
		String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + token;
	    
	    return callInterface(url, jobj);
	    
	}
	
	/**
	 * 调用接口方法
	 * @param url
	 * @param jobj
	 * @return
	 */
	public String callInterface(String url, JSONObject jobj) {
		
		HttpClient client = new HttpClient();
	    PostMethod post = new PostMethod(url);
	    
	    RequestEntity requestEntity;
	    String respStr = "";
		try {
			
			requestEntity = new StringRequestEntity(jobj.toString(),"text/json","UTF-8");
			post.setRequestEntity(requestEntity);
		
			post.getParams().setContentCharset("utf-8");
	    	
			client.executeMethod(post);
			respStr = post.getResponseBodyAsString();
			
		} catch (Exception e) {
			CommonLog.error(e.getMessage(),e);
			logger.error("sendTemplateReqError", e);
			throw new RuntimeException(e);
		}finally{
			logger.info("weixinTemplateResp：" + respStr);
			if(post!=null){
				post.releaseConnection();
				post = null;
			}
		}
	    
	    return respStr;
	}
	
	/**
	 * 获得token
	 * @param tokenUrl
	 * @return
	 */
	public String getToken(String tokenUrl) {
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
			logger.info("getTokenResp：" + respStr);
			if(get!=null){
				get.releaseConnection();
				get = null;
			}
		}
		return token;
	}
	
	/**
	 * 构造传送参数
	 * @param open_id
	 * @param title
	 * @param content
	 * @return
	 */
	public JSONObject jsonFormat(String open_id,String title,String content,long usr_ent_id,String usr_display_bil,long wmsg_id){
		if(Application.WECHAT_TOKEN_URL == null || Application.WECHAT_TOKEN_URL.equals("")){
			logger.info("你还没有配置获取微信token的URL，请登录系统管理员设置");
			return null;
		}
		if(open_id == null || open_id.equals("")){
			logger.info("用户还没有绑定微信平台");
			return null;
		}
		String tokenUrl = Application.WECHAT_TOKEN_URL.trim();
		String token = getToken(tokenUrl);
		String forwardUrl = Application.WECHAT_MOBILE_DOMAIN + "views/forward.html";
		String viewAllUrl = forwardUrl + "?token=" + token + "&url=" + Static.WIZMOBILE_PAGE_MESSAGE_LIST+ "&uid=" + usr_ent_id + "&wmsg_id=" + wmsg_id;// 点击图文消息跳转链接
		JSONObject jobj = new JSONObject();
		jobj.put("touser", open_id);
		//微信消息模板ID，请在微信消息库里面查找对应的ID。如果修改了模板ID。请记得看看下面的json参数是否一样。如不一样，请自行修改
		jobj.put("template_id", this.getWeChatTemplateId());
		jobj.put("url", viewAllUrl);
		JSONObject dataobj = new JSONObject();
		JSONObject dataCObj = new JSONObject();
		
		dataCObj.put("value", CharUtils.subStringWithDecoretor(subStringHTML(content).trim(), contentLimitLength, "..."));
		dataCObj.put("color", "#173177"); 
		dataobj.put("first", dataCObj);
		dataCObj.put("value", CharUtils.subStringWithDecoretor(title, titleLimitLength, "..."));
		dataCObj.put("color", "#173177");
		dataobj.put("subject", dataCObj);
		dataCObj.put("value", usr_display_bil);
		dataCObj.put("color", "#173177");
		dataobj.put("sender", dataCObj);
//		dataCObj.put("value", "赶紧开始学习吧！");
//		dataCObj.put("color", "#173177");
//		dataobj.put("remark", dataCObj);
		
		jobj.put("data", dataobj);
		return jobj;
	}
	
	public String subStringHTML(String con) {
		String content = ""; 
		content=con.replaceAll("</?[^>]+>","");//剔除了<html>的标签 
		content=content.replace("&nbsp;",""); 
		content=content.replace("\n",""); 
		content=content.replace("\r","");
		content=content.replace("\"","‘");
		content=content.replace("'","‘");

		return content;
    }
	
	
	public void pushMsgToWechat(long sender_ent_id,long rec_ent_id,long wmsg_id,String subject,String content){
		
		try {
			//获取用户open_id
			APIToken apiToken = apiTokenService.getByWechatEntId(Long.toString(rec_ent_id));
			
			if(apiToken != null){ //跟微信绑定过
				String open_id = apiToken.getAtk_wechat_open_id();
				RegUserService regUserService = (RegUserService) WzbApplicationContext.getBean("regUserService");
				RegUser user = regUserService.getUserDetail(sender_ent_id);
				if(user != null){
					//组装参数 推送到微信
					this.sendTemplate(this.jsonFormat(open_id,subject,content,rec_ent_id,user.getUsr_display_bil(),wmsg_id));
				}else{
					logger.info("没有找到发件人信息");
				}
			}
		} catch (Exception e) {
			CommonLog.error(e.getMessage(),e);
		}
	}
	
	
}
