package com.cwn.wizbank.entity;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.util.StringUtils;

import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.utils.CwnUtil;
import com.cwn.wizbank.utils.DateUtil;

public class ObjectActionLog extends SystemLog implements java.io.Serializable {

	private static final long serialVersionUID = 3431381690382149567L;
	
	public static final String OBJECT_TYPE_USR = "usr"; //操作对象的类型: 用户
	public static final String OBJECT_TYPE_GRP = "grp"; //操作对象的类型: 用户组
	public static final String OBJECT_TYPE_UPT = "upt"; //操作对象的类型: 岗位
	public static final String OBJECT_TYPE_UGR = "ugr"; //操作对象的类型: 职级
	public static final String OBJECT_TYPE_COS = "cos"; //操作对象的类型: 课程
	public static final String OBJECT_TYPE_CLASS = "class"; //操作对象的类型: 班级
	public static final String OBJECT_TYPE_ONLINE_EXAM = "online_exam"; //操作对象的类型: 考试
	public static final String OBJECT_TYPE_OFFLINE_EXAM = "offline_exam"; //操作对象的类型: 考试场次
	public static final String OBJECT_TYPE_CC = "cc"; //操作对象的类型: 结训条件
	public static final String OBJECT_TYPE_CREDITS = "credits"; //操作对象的类型: 计分项目
	public static final String OBJECT_TYPE_KB = "kb"; //操作对象的类型: 知识
	public static final String OBJECT_TYPE_EL = "el"; //操作对象的类型: 公共调查问卷
	public static final String OBJECT_TYPE_VT = "vt"; //操作对象的类型: 投票
	public static final String OBJECT_TYPE_AN = "an"; //操作对象的类型: 公告
	public static final String OBJECT_TYPE_INFO = "info"; //操作对象的类型: 资讯
	public static final String OBJECT_TYPE_TC = "tc"; //操作对象的类型: 培训中心
	//CPD功能新增操作对象类型
	public static final String OBJECT_TYPE_CPD_TYPE = "ct"; //操作对象的类型: 大牌
	public static final String OBJECT_TYPE_CPD_GROUP = "cg"; //操作对象的类型: 小牌
	public static final String OBJECT_TYPE_CPD_GROUP_HOURS = "cgh"; //操作对象的类型: 小牌要求时数设置
	public static final String OBJECT_TYPE_CPD_REG = "cr"; //操作对象的类型: cpd用户注册信息
	public static final String OBJECT_TYPE_CPD_COURSE_HOURS = "aci"; //操作对象的类型: 课程CPD时数设置
	
	public static final String OBJECT_ACTION_ADD = "add"; //操作: 添加
	public static final String OBJECT_ACTION_UPD = "upd"; //操作: 修改
	public static final String OBJECT_ACTION_ACTIVE = "active"; //操作: 激活
	public static final String OBJECT_ACTION_DEL = "del"; //操作: 删除
	public static final String OBJECT_ACTION_EXPIRE = "Expire"; //操作: 过期
	public static final String OBJECT_ACTION_UPD_PWD = "upd_pwd"; //操作: 修改密码
	public static final String OBJECT_ACTION_PUB = "pub"; //操作: 发布
	public static final String OBJECT_ACTION_CANCLE_PUB = "cancel_pub"; //操作: 取消发布
	public static final String OBJECT_ACTION_APPR = "appr"; //操作: 审批
	public static final String OBJECT_ACTION_CANCEL_APPR = "cancel_appr"; //操作: 取消审批
	public static final String OBJECT_ACTION_RESTORE = "restore"; //操作: 还原
	public static final String OBJECT_ACTION_TYPE_WEB = "web"; //操作形式: 页面操作
	public static final String OBJECT_ACTION_TYPE_BATCH = "batch"; // 操作形式:批量修改
	public static final String OBJECT_ACTION_TYPE_IMPORT = "import"; //操作形式:导入
	
	private Long objectId ;//操作对象的ID
	private String objectCode;	 //操作对象的编号
	private String objectTitle;	 //操作对象的名称
	private String objectType;		//操作对象的类型
	private String objectAction;		//操作
	private String objectActionType;		//操作形式
	private Date objectActionTime;		//操作时间
	private Long objectOptUserId;	//操作者ID
	
	private String optUserAccount;		//操作者账号
	private String optUserFullName;	//操作者全称
	private Timestamp objectOptUserLoginTime;//操作者登陆时间
	private String objectOptUserLoginIp;//操作者登陆IP
	
	
	public ObjectActionLog(){
		
	}
	
	public ObjectActionLog(Long _objectId ,String _objectCode, String _objectTitle ,String _objectType ,String _objectAction
			,String _objectActionType,Long _optUserId,Timestamp _optUserLoginTime , String _optUserLoginIp){
		this.objectId = _objectId;
		if(StringUtils.isEmpty(_objectCode)){
			this.objectCode = "--"; 
		}else{
			this.objectCode =_objectCode;
		}
		this.objectTitle = _objectTitle;
		this.objectType = _objectType;
		this.objectAction = _objectAction;
		this.objectActionType = _objectActionType;
		this.objectActionTime = DateUtil.getCurrentTime();
		this.objectOptUserId = _optUserId;
		DateUtil dateUtil = new DateUtil();
		if(null!=( _optUserLoginTime)){
			this.objectOptUserLoginTime = _optUserLoginTime;
		}else{
			this.objectOptUserLoginTime = null;
		}
		if(!StringUtils.isEmpty(_optUserLoginIp)){
			this.objectOptUserLoginIp = _optUserLoginIp;
		}else{
			this.objectOptUserLoginIp = "--";
		}
	}
	

	public String getObjectTitle() {
		return objectTitle;
	}

	public void setObjectTitle(String objectTitle) {
		this.objectTitle = objectTitle;
	}

	public String getObjectType() {
		return objectType;
	}
	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}
	public String getObjectAction() {
		return objectAction;
	}
	public void setObjectAction(String objectAction) {
		this.objectAction = objectAction;
	}
	public String getObjectActionType() {
		return objectActionType;
	}
	public void setObjectActionType(String objectActionType) {
		this.objectActionType = objectActionType;
	}
	public Date getObjectActionTime() {
		return objectActionTime;
	}
	public void setObjectActionTime(Date objectActionTime) {
		this.objectActionTime = objectActionTime;
	}
	public Long getObjectId() {
		return objectId;
	}
	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	public String getObjectCode() {
		return objectCode;
	}

	public void setObjectCode(String objectCode) {
		this.objectCode = objectCode;
	}

	public String getOptUserAccount() {
		return optUserAccount;
	}

	public void setOptUserAccount(String optUserAccount) {
		this.optUserAccount = optUserAccount;
	}

	public String getOptUserFullName() {
		return optUserFullName;
	}

	public void setOptUserFullName(String optUserFullName) {
		this.optUserFullName = optUserFullName;
	}

	public Long getObjectOptUserId() {
		return objectOptUserId;
	}

	public void setObjectOptUserId(Long objectOptUserId) {
		this.objectOptUserId = objectOptUserId;
	}

	public Timestamp getObjectOptUserLoginTime() {
		return objectOptUserLoginTime;
	}

	public void setObjectOptUserLoginTime(Timestamp objectOptUserLoginTime) {
		this.objectOptUserLoginTime = objectOptUserLoginTime;
	}

	public String getObjectOptUserLoginIp() {
		return objectOptUserLoginIp;
	}

	public void setObjectOptUserLoginIp(String objectOptUserLoginIp) {
		this.objectOptUserLoginIp = objectOptUserLoginIp;
	}
	
}
