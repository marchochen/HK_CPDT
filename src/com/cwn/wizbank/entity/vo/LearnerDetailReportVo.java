package com.cwn.wizbank.entity.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


public class LearnerDetailReportVo implements Serializable {

	private static final long serialVersionUID = -1271022779550145720L;
	public static final Integer COURSE_TYPE_WEB_BASED  =0;  //网上课程
	public static final Integer COURSE_TYPE_CLASSROOM  =1;  //面授课程
	public static final Integer EXAM_TYPE_WEB_BASED  =2;   //网上考试
	public static final Integer EXAM_TYPE_CLASSROOM  =3;   //离线考试
	public static final Integer COURSE_TYPE_INTEGRATED =4; //项目式培训
	
	private Long userEntId;
	
	private Long itmId; //课程/考试id
	
	private String userFullName;
	
	private String userDispalyName;
	
	private Double covTotalTime; //学习时长
	
	private String covStatus;//学习状态
	
	private Double covScore;
	
	private Double covMaxScore; //最大分数

    private Double iesCredit; //学分
    
    private Double finalCredit;//该次课程最终得分
	
	private Double attRate; //出席率
	
	private String itmCode ;//课程编号
	
	private String itmTitle ;//课程名称
	
	private int itmRunInd ;
	
	private int itmApplyInd ;
	
	private int itmIntegratedInd;
	
	private int itmExamInd;
	
	private int itmCreateRunInd;
	
	private String tcrTitle;
	
	private String gruopName;
	
	private String gradeName;
	
	private Integer appId;  //学习记录id
	
	private String appStatus;
	
	private String userEmail ;
	
	private String userTel;
	
	private String staffNo;//员工编号
	
	private List userInfo;
	
	private Date appCreateTime; //报名日期
	
	private Date attTime;//结训时间

	private Date covCommenceDatetime; //首次访问时间
	
	private Integer cov_tkh_id ;
	
	private Integer attemptCount;//尝试次数
	
	private Date covLastAccDatetime;
	
	private String usrStatus;
	
	private String p_itm_code;
	
	private String p_itm_title;
	
	private Long p_itm_id;
	
	private String catalog;

	public Long getUserEntId() {
		return userEntId;
	}

	public void setUserEntId(Long userEntId) {
		this.userEntId = userEntId;
	}

	public String getUserFullName() {
		return userFullName;
	}

	public void setUserFullName(String userFullName) {
		this.userFullName = userFullName;
	}

	public String getUserDispalyName() {
		return userDispalyName;
	}

	public void setUserDispalyName(String userDispalyName) {
		this.userDispalyName = userDispalyName;
	}

	public String getCovStatus() {
		return covStatus;
	}

	public void setCovStatus(String covStatus) {
		this.covStatus = covStatus;
	}

	public String getItmCode() {
		return itmCode;
	}

	public void setItmCode(String itmCode) {
		this.itmCode = itmCode;
	}

	public String getItmTitle() {
		return itmTitle;
	}

	public void setItmTitle(String itmTitle) {
		this.itmTitle = itmTitle;
	}

	public int getItmRunInd() {
		return itmRunInd;
	}

	public void setItmRunInd(int itmRunInd) {
		this.itmRunInd = itmRunInd;
	}

	public int getItmApplyInd() {
		return itmApplyInd;
	}

	public void setItmApplyInd(int itmApplyInd) {
		this.itmApplyInd = itmApplyInd;
	}

	public int getItmIntegratedInd() {
		return itmIntegratedInd;
	}

	public void setItmIntegratedInd(int itmIntegratedInd) {
		this.itmIntegratedInd = itmIntegratedInd;
	}

	public int getItmExamInd() {
		return itmExamInd;
	}

	public void setItmExamInd(int itmExamInd) {
		this.itmExamInd = itmExamInd;
	}

	public int getItmCreateRunInd() {
		return itmCreateRunInd;
	}

	public void setItmCreateRunInd(int itmCreateRunInd) {
		this.itmCreateRunInd = itmCreateRunInd;
	}

	public String getTcrTitle() {
		return tcrTitle;
	}

	public void setTcrTitle(String tcrTitle) {
		this.tcrTitle = tcrTitle;
	}

	public String getGruopName() {
		return gruopName;
	}

	public void setGruopName(String gruopName) {
		this.gruopName = gruopName;
	}

	public String getGradeName() {
		return gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	public Integer getAppId() {
		return appId;
	}

	public void setAppId(Integer appId) {
		this.appId = appId;
	}

	public Double getCovTotalTime() {
		return covTotalTime;
	}

	public void setCovTotalTime(Double covTotalTime) {
		this.covTotalTime = covTotalTime;
	}

	public Double getCovScore() {
		return covScore;
	}

	public void setCovScore(Double covScore) {
		this.covScore = covScore;
	}

	public Double getCovMaxScore() {
		return covMaxScore;
	}

	public void setCovMaxScore(Double covMaxScore) {
		this.covMaxScore = covMaxScore;
	}

	public Double getAttRate() {
		return attRate;
	}

	public void setAttRate(Double attRate) {
		this.attRate = attRate;
	}

	public String getAppStatus() {
		return appStatus;
	}

	public void setAppStatus(String appStatus) {
		this.appStatus = appStatus;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserTel() {
		return userTel;
	}

	public void setUserTel(String userTel) {
		this.userTel = userTel;
	}
	
	public String getStaffNo() {
		return staffNo;
	}

	public void setStaffNo(String staffNo) {
		this.staffNo = staffNo;
	}

	public Date getAttTime() {
		return attTime;
	}

	public void setAttTime(Date attTime) {
		this.attTime = attTime;
	}
	
	public Date getAppCreateTime() {
		return appCreateTime;
	}

	public void setAppCreateTime(Date appCreateTime) {
		this.appCreateTime = appCreateTime;
	}

	public Date getCovCommenceDatetime() {
		return covCommenceDatetime;
	}

	public void setCovCommenceDatetime(Date covCommenceDatetime) {
		this.covCommenceDatetime = covCommenceDatetime;
	}

	public Integer getCov_tkh_id() {
		return cov_tkh_id;
	}

	public void setCov_tkh_id(Integer cov_tkh_id) {
		this.cov_tkh_id = cov_tkh_id;
	}

	public Integer getAttemptCount() {
		return attemptCount;
	}

	public void setAttemptCount(Integer attemptCount) {
		this.attemptCount = attemptCount;
	}

	public Long getItmId() {
		return itmId;
	}

	public Date getCovLastAccDatetime() {
		return covLastAccDatetime;
	}

	public void setCovLastAccDatetime(Date covLastAccDatetime) {
		this.covLastAccDatetime = covLastAccDatetime;
	}

	public void setItmId(Long itmId) {
		this.itmId = itmId;
	}

	public String getUsrStatus() {
		return usrStatus;
	}

	public void setUsrStatus(String usrStatus) {
		this.usrStatus = usrStatus;
	}

	public String getP_itm_code() {
		return p_itm_code;
	}

	public void setP_itm_code(String p_itm_code) {
		this.p_itm_code = p_itm_code;
	}

	public String getP_itm_title() {
		return p_itm_title;
	}

	public void setP_itm_title(String p_itm_title) {
		this.p_itm_title = p_itm_title;
	}

	public String getCatalog() {
		return catalog;
	}

	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}

	public Long getP_itm_id() {
		return p_itm_id;
	}

	public void setP_itm_id(Long p_itm_id) {
		this.p_itm_id = p_itm_id;
	}

    public Double getIesCredit() {
        return iesCredit;
    }

    public void setIesCredit(Double iesCredit) {
        this.iesCredit = iesCredit;
    }
	
	public Double getFinalCredit() {
		return finalCredit;
	}

	public void setFinalCredit(Double finalCredit) {
		this.finalCredit = finalCredit;
	}

	public List getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(List userInfo) {
		this.userInfo = userInfo;
	}

	
}
