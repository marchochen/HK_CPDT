package com.cwn.wizbank.common;
/**
 * 社区化的静态变量
 * @author leon.li
 * 2014-7-28 下午12:01:32
 */
public class SNS {
	public final static String MODULE_COURSE = "Course";
	public final static String MODULE_DOING = "Doing";
	public final static String MODULE_GROUP = "Group";
	public static final String MODULE_ARTICLE = "Article";
	public static final String MODULE_COMMENT = "Comment";
	public static final String MODULE_ANSWER = "Answer";
	public static final String MODULE_QUESTION = "Question";
	public static final String MODULE_NOTICE = "Notice";
	public static final String MODULE_KNOWLEDGE = "Knowledge";

	
	public final static String VALUATION_TYPE_STAR = "Star"; // 星星评分
	public final static String VALUATION_TYPE_LIKE = "Like"; // 赞

	public final static String ATTENTION_MY_FOLLOW = "myfollow"; // 我的关注
	public final static String ATTENTION_MY_FANS = "myfans"; // 我的粉丝

	public final static String MESSAGE = "message";
	public final static String STATUS = "status";
	public final static String STATUS_SUCCESS = "success";
	public final static String STATUS_ERROR = "error";

	public final static int UNIT_HOUR = 1;
	public final static int UNIT_MINUTE = 2;
	public final static int UNIT_SECOND = 3;

	public final static String DOING_ACTION_DOING = "doing"; // 个人中心发表动态
	public final static String DOING_ACTION_SHARE = "share"; // 分享课程
	public final static String DOING_ACTION_LIKE = "like"; //  赞了课程
	public final static String DOING_ACTION_ENROLL_COS = "enroll_cos"; //  报名课程
	public final static String DOING_ACTION_COMPLETED_COS = "completed_cos"; //  报名课程
	public final static String DOING_ACTION_COMMENT = "comment"; //  评论课程
	public final static String DOING_ACTION_GROUP_CREATE = "group_create"; // 创建群组动态
	public final static String DOING_ACTION_GROUP_APP = "group_app"; // 群组申请通过动态
	public final static String DOING_ACTION_GROUP_DISSMISS = "group_dissmiss"; // 解散群组动态
	public final static String DOING_ACTION_GROUP_OPEN = "group_open"; // 开放
	public final static String DOING_ACTION_GROUP_IMAGE = "group_image"; // 群组分享图片
	public final static String DOING_ACTION_GROUP_VEDIO = "group_vedio"; // 群组分享视频
	public final static String DOING_ACTION_GROUP_DOING = "group_doing"; // 群组发表动态
	public final static String DOING_ACTION_GROUP_DOCUMENT = "group_document"; // 群组分享文档
	public final static String DOING_ACTION_GROUP = "group"; //在群组发表动态
	public final static String DOING_ACTION_GROUP_MGT = "group_mgt"; //在群组发表动态
	public final static String DOING_ACTION_QUESTION_ADD = "question_add"; //提问动态
	public final static String DOING_ACTION_ANSWER_ADD = "answer_add"; //回答问题动态
	// 群组成员状态
	public final static int GROUP_MEMBER_STATUS_PENDING = 0; // 申请状态
	public final static int GROUP_MEMBER_STATUS_OK = 1; // 正式成员
	public final static int GROUP_MEMBER_STATUS_DISABLED = 2; // 禁用
	public final static int GROUP_MEMBER_STATUS_DECLINE = 3; //拒绝

	// 群组成员类型
	public final static int GROUP_MEMBER_TYPE_OWNER = 1; // 创建人
	public final static int GROUP_MEMBER_TYPE_MEMBER = 2; // 成员
}
