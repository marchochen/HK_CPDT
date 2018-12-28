package com.cwn.wizbank.wechat.common;


/**
 * 公共变量类
 */
public class Static {

	/** 符号，句号 . */
	public final static String CHAR_DOT = ".";
	/** 符号，逗号 , */
	public final static String CHAR_COMMA = ",";
	/** 符号，斜杠 , / */
	public final static String CHAR_SLASH = "/";
	/** 符号，叹号 , ! */
	public final static String CHAR_EXCLAMATION = "!";
	/** 符号，空格 , "" */
	public static final String CHAR_Blank = "";
	/** 符号，true  */
	public static final String CHAR_TRUE = "true";
	/** 符号，false  */
	public static final String CHAR_FALSE = "false";
	/** 错误 */
	public final static String ERROR = "error";
	/** todo，查看 */
	public final static String TODO_SHOW = "show";
	
	public final static String SESSION_USER_ARG = "session_admin_user";
	public final static String SESSION_CUSTOMER_ARG ="session_shop_user";
	public final static String SESSION_USER = "loginUser";
	public final static String SESSION_CUSTOMER = "loginCustomer";

	/**角色  admin:系统管理员 */
	public final static String ROLE_AMINN = "admin";
	
	/** select 关键字 */
	public final static String KEY_SELECT = "select";
	/** order 关键字 */
	public final static String KEY_ORDER = "order by";

	/** 数据状态 全部 */
	public final static String DATA_ALL = "all";
	/** 数据状态 草稿 */
	public final static String DATA_DRAFT = "c";
	/** 数据状态 审核 */
	public final static String DATA_AUDIT = "s";
	/** 数据状态 删除 */
	public final static String DATA_DELETE = "d";	
	
	/** 微信接口默认Token "weixin" 删除 */
	public static final String WEIXIN_TOKEN = "weixin";
	/** 微信消息类型,文本, "text" */
	public static final String MSG_TYPE_TEXT = "text";
	/** 微信消息类型,图文, "news" */
	public static final String MSG_TYPE_NEWS = "news";
	/** 微信消息类型,音乐, "music" */
	public static final String MSG_TYPE_MUSIC = "music";
	
	/** 微信消息类型,事件, "event" */
	public static final String MSG_TYPE_EVENT = "event";
	/** 微信消息类型,CLICK事件, "CLICK" */
	public static final String MSG_TYPE_EVENT_CLICK = "CLICK";
	/** 微信消息类型,订阅事件, "subscribe" */
	public static final String MSG_TYPE_EVENT_SUBSCRIBE = "subscribe";
	/** 微信消息类型,取消订阅事件, "unsubscribe" */
	public static final String MSG_TYPE_EVENT_UNSUBSCRIBE = "unsubscribe";
	
	/** 微信消息类型,开始标签, "<MsgType><![CDATA[" */
	public static final String MSG_TYPE_TAG_S = "<MsgType><![CDATA[";
	/** 微信消息类型,结束标签, "]]></MsgType>" */
	public static final String MSG_TYPE_TAG_E = "]]></MsgType>";
	
	/** wiz登录api结果, "未知错误" */
	public static final String WIZAPI_RESULT_ERROR = "LGF00";
	/** wiz登录api结果, "用户名不存在" */
	public static final String WIZAPI_RESULT_USER_UNEXIST = "LGF01";
	/** wiz登录api结果, "密码无效" */
	public static final String WIZAPI_RESULT_PWD_INVALID = "LGF04";
	/** wiz登录api结果, "成功登录" */
	public static final String WIZAPI_RESULT_LOGIN_SUCCESS = "LGS01";
	
	/** wiz api查询状态, "成功" */
	public static final String WIZAPI_STATUS_SUCCESS = "0";
	/** wiz api查询状态, "出错" */
	public static final String WIZAPI_STATUS_ERROR = "1";
	
	/** wizbank账户绑定页面相对路径*/
	public final static String PATH_BIND_WIZBANK = "/wechat/toBind";
	
	/** 图文消息，公告列表图片url*/
	public final static String ANNOUNCEMENT_LIST_ICON_URL = "/static/images/wechat/announce.jpg";
	/** 图文消息，公告图片url*/
	public final static String ANNOUNCEMENT_ITEM_ICON_URL = "/static/images/wechat/ANNOUNCEMENT_ITEM.jpg";

	/** 图文消息，文章图片url*/
	public final static String ARTICLE_LIST_ICON_URL = "/static/images/wechat/article.jpg";
	
	/** 图文消息，推荐课程列表图片url*/
	public final static String RECOMMEND_LIST_ICON_URL = "/static/images/wechat/recommend.jpg";
	
	/** 图文消息，课程列表图片url*/
	public final static String COURSE_LIST_ICON_URL = "/static/images/wechat/signupcourse.jpg";
	/** 图文消息，课程图片url*/
	public final static String COURSE_ITEM_ICON_URL = "/static/images/wechat/COURSE_ITEM.jpg";
	/** 图文消息，考试列表图片url*/
	public final static String EXAM_LIST_ICON_URL = "/static/images/wechat/signupexam.jpg";
	/** 图文消息，考试图片url*/
	public final static String EXAM_ITEM_ICON_URL = "/static/images/wechat/EXAM_ITEM.jpg";
	/** 绑定图文消息的默认图片url*/
	public final static String BIND_NEWS_PIC_URL = "/static/images/wechat/tobind.jpg";
	
	public static final String OPEN_LIST_ICON_URL = "/static/images/wechat/open.jpg";
	
	public static final String HELP_ICON_URL = "/static/images/wechat/help.jpg";


	/** wizMobile页面名称：课程列表*/
	public static final String WIZMOBILE_PAGE_COURSE_LIST = "course";
	/** wizMobile页面名称：课程详情*/
	public static final String WIZMOBILE_PAGE_COURSE_DETAIL = "course_detail";
	/** wizMobile页面名称：推荐*/
	public static final String WIZMOBILE_PAGE_RECOMMEND_LIST = "recommend";
	/** wizMobile页面名称：公告列表*/
	public static final String WIZMOBILE_PAGE_ANNOUNCEMENT_LIST = "announcement_list";
	/** wizMobile页面名称：公告详情*/
	public static final String WIZMOBILE_PAGE_ANNOUNCEMENT_DETAIL = "announcement_detail";
	/** wizMobile页面名称：测验列表*/
	public static final String WIZMOBILE_PAGE_TEST_LIST = "test";
	/** wizMobile页面名称：测验详情*/
	public static final String WIZMOBILE_PAGE_TEST_DETAIL = "test_detail";
	
	public static final String WIZMOBILE_PAGE_OPEN_LIST = "open";
	
	public static final String WIZMOBILE_PAGE_OPEN_DETAIL = "open_detail";
	
	public static final String WIZMOBILE_PAGE_ARTICLE_LIST = "article_list";

	public static final String WIZMOBILE_PAGE_ARTICLE_DETAIL = "article_detail";
	
	public static final String WIZMOBILE_PAGE_MESSAGE_LIST = "message_list";

}
