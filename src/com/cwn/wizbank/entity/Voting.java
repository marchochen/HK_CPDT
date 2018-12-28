package com.cwn.wizbank.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.cwn.wizbank.utils.DateUtil;

/**
 * 投票 实体
 * 
 * @author Andrew 2015-6-6 下午2:00
 */
public class Voting implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5414868554973585555L;
	
	
	public static final String PUBLISHED_STATUS_ON = "ON";
	public static final String PUBLISHED_STATUS_DEL = "DEL";
	public static final String PUBLISHED_STATUS_OFF = "OFF";

	/**
	 * 自增长id
	 */
	private Long vot_id;

	/**
	 * 标题
	 */
	private String vot_title;

	/**
	 * 内容
	 */
	private String vot_content;

	/**
	 * 状态
	 */
	private String vot_status;

	/**
	 * 相关联培训中心的id
	 */
	private Long vot_tcr_id;

	/**
	 * 有效的起始时间
	 */
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date vot_eff_date_from;

	/**
	 * 结束时间
	 */
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date vot_eff_date_to;

	/**
	 * 创建时间
	 */
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date vot_create_timestamp;

	/**
	 * 创建人
	 */
	private String vot_create_usr_id;

	/**
	 * 修改时间
	 */
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date vot_update_timestamp;

	/**
	 * 修改人
	 */
	private String vot_update_usr_id;

	/**
	 * 回应数目
	 */
	private Long responseCount;
	
	private VoteQuestion voteQuestion;
	
	/**
	 * 当前用户的答题情况
	 */
	private VoteResponse voteResponse;
	
	/**
	 * 培训中心
	 */
	private TcTrainingCenter tcTrainingCenter;
	
	private String tcr_title;
	
	private String encrypt_vot_id;
	
	public Long getVot_id() {
		return vot_id;
	}

	public void setVot_id(Long vot_id) {
		this.vot_id = vot_id;
	}

	public String getVot_title() {
		return vot_title;
	}

	public void setVot_title(String vot_title) {
		this.vot_title = vot_title;
	}

	public String getVot_content() {
		return vot_content;
	}

	public void setVot_content(String vot_content) {
		this.vot_content = vot_content;
	}

	public String getVot_status() {
		return vot_status;
	}

	public void setVot_status(String vot_status) {
		this.vot_status = vot_status;
	}

	public Long getVot_tcr_id() {
		return vot_tcr_id;
	}

	public void setVot_tcr_id(Long vot_tcr_id) {
		this.vot_tcr_id = vot_tcr_id;
	}

	public Date getVot_eff_date_from() {
		return vot_eff_date_from;
	}

	public void setVot_eff_date_from(Date vot_eff_date_from) {
		this.vot_eff_date_from = vot_eff_date_from;
	}

	public Date getVot_eff_date_to() {
		return vot_eff_date_to;
	}

	public void setVot_eff_date_to(Date vot_eff_date_to) {
		try{
			this.vot_eff_date_to = DateUtil.getInstance().getEndDateDay(vot_eff_date_to);
		}catch(Exception e){
			this.vot_eff_date_to = vot_eff_date_to;
		}
	}

	public Date getVot_create_timestamp() {
		return vot_create_timestamp;
	}

	public void setVot_create_timestamp(Date vot_create_timestamp) {
		this.vot_create_timestamp = vot_create_timestamp;
	}

	public String getVot_create_usr_id() {
		return vot_create_usr_id;
	}

	public void setVot_create_usr_id(String vot_create_usr_id) {
		this.vot_create_usr_id = vot_create_usr_id;
	}

	public Date getVot_update_timestamp() {
		return vot_update_timestamp;
	}

	public void setVot_update_timestamp(Date vot_update_timestamp) {
		this.vot_update_timestamp = vot_update_timestamp;
	}

	public String getVot_update_usr_id() {
		return vot_update_usr_id;
	}

	public void setVot_update_usr_id(String vot_update_usr_id) {
		this.vot_update_usr_id = vot_update_usr_id;
	}

	public Long getResponseCount() {
		return responseCount;
	}

	public void setResponseCount(Long responseCount) {
		this.responseCount = responseCount;
	}

	public VoteQuestion getVoteQuestion() {
		return voteQuestion;
	}

	public void setVoteQuestion(VoteQuestion voteQuestion) {
		this.voteQuestion = voteQuestion;
	}

	public VoteResponse getVoteResponse() {
		return voteResponse;
	}

	public void setVoteResponse(VoteResponse voteResponse) {
		this.voteResponse = voteResponse;
	}

	public TcTrainingCenter getTcTrainingCenter() {
		return tcTrainingCenter;
	}

	public void setTcTrainingCenter(TcTrainingCenter tcTrainingCenter) {
		this.tcTrainingCenter = tcTrainingCenter;
	}

	public String getTcr_title() {
		return tcr_title;
	}

	public void setTcr_title(String tcr_title) {
		this.tcr_title = tcr_title;
	}

	public String getEncrypt_vot_id() {
		return encrypt_vot_id;
	}

	public void setEncrypt_vot_id(String encrypt_vot_id) {
		this.encrypt_vot_id = encrypt_vot_id;
	}

}
