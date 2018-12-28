package com.cw.wizbank.JsonMod.Course.bean;

import java.sql.Timestamp;
import java.util.List;

public class ModBean {
	  private String mod_folder;    //新加属性: 当前学习模块所属模块夹的identifier
      //以下信息均为原XML中会包含的信息
        //在dbResource.getCosContentListAsJson()生成XML的代码中,
       //可找到获取方式,这里所用的名称大多数都是和XML中的属性名或者节点名相同.
      private long id;               
      private String title;
      private String desc;
      private String organization;
      private String instruct;
      private String type;
      private String subtype;
      private int order;
      private long sub_num;
      private long score_multiplier;
      private String privilege;
      private String status;
      private String language;
      private long max_user_attempt;
      private int difficulty;
      private float duration;
      private float time_limit;
      private float suggested_time;
      private Timestamp eff_start_datetime;
      private Timestamp eff_end_datetime;
      private Timestamp cur_time;
      private float max_score;
      private int pass_score;
      private long attempt_nbr;
      private Timestamp pgr_start;
      private Timestamp pgr_complete;
      private Timestamp pgr_last_acc;
      private String mod_vendor;
      private String mod_web_launch;
      private String res_src_type;
      private String res_src_link;
      private Timestamp timestamp;
      private String due_datetime;
      private PreModuleBean pre_mod_inf;     
      private AiccDataBean aicc_data;      
      private String cmr_status;    
      private String stylesheet;
      private List filterLst;
      
      private String sco_version;
      private String title_;
      private boolean mod_download_ind;
      private long mod_required_time;
      
	public String getTitle_() {
		return title_;
	}
	public void setTitle_(String title_) {
		this.title_ = title_;
	}
	public String getStylesheet() {
		return stylesheet;
	}
	public void setStylesheet(String stylesheet) {
		this.stylesheet = stylesheet;
	}
	public String getCmr_status() {
		return cmr_status;
	}
	public void setCmr_status(String cmr_status) {
		this.cmr_status = cmr_status;
	}
	public AiccDataBean getAicc_data() {
		return aicc_data;
	}
	public void setAicc_data(AiccDataBean aicc_data) {
		this.aicc_data = aicc_data;
	}
	public PreModuleBean getPre_mod_inf() {
		return pre_mod_inf;
	}
	public void setPre_mod_inf(PreModuleBean pre_mod_inf) {
		this.pre_mod_inf = pre_mod_inf;
	}
	public long getAttempt_nbr() {
		return attempt_nbr;
	}
	public void setAttempt_nbr(long attempt_nbr) {
		this.attempt_nbr = attempt_nbr;
	}
	public Timestamp getCur_time() {
		return cur_time;
	}
	public void setCur_time(Timestamp cur_time) {
		this.cur_time = cur_time;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public int getDifficulty() {
		return difficulty;
	}
	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}
	public float getDuration() {
		return duration;
	}
	public void setDuration(float duration) {
		this.duration = duration;
	}
	public Timestamp getEff_end_datetime() {
		return eff_end_datetime;
	}
	public void setEff_end_datetime(Timestamp eff_end_datetime) {
		this.eff_end_datetime = eff_end_datetime;
	}
	public Timestamp getEff_start_datetime() {
		return eff_start_datetime;
	}
	public void setEff_start_datetime(Timestamp eff_start_datetime) {
		this.eff_start_datetime = eff_start_datetime;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getInstruct() {
		return instruct;
	}
	public void setInstruct(String instruct) {
		this.instruct = instruct;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public float getMax_score() {
		return max_score;
	}
	public void setMax_score(float max_score) {
		this.max_score = max_score;
	}
	public long getMax_user_attempt() {
		return max_user_attempt;
	}
	public void setMax_user_attempt(long max_user_attempt) {
		this.max_user_attempt = max_user_attempt;
	}
	public String getMod_folder() {
		return mod_folder;
	}
	public void setMod_folder(String mod_folder) {
		this.mod_folder = mod_folder;
	}
	public String getMod_vendor() {
		return mod_vendor;
	}
	public void setMod_vendor(String mod_vendor) {
		this.mod_vendor = mod_vendor;
	}
	public String getMod_web_launch() {
		return mod_web_launch;
	}
	public void setMod_web_launch(String mod_web_launch) {
		this.mod_web_launch = mod_web_launch;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public String getOrganization() {
		return organization;
	}
	public void setOrganization(String organization) {
		this.organization = organization;
	}
	public int getPass_score() {
		return pass_score;
	}
	public void setPass_score(int pass_score) {
		this.pass_score = pass_score;
	}
	public Timestamp getPgr_complete() {
		return pgr_complete;
	}
	public void setPgr_complete(Timestamp pgr_complete) {
		this.pgr_complete = pgr_complete;
	}
	public Timestamp getPgr_last_acc() {
		return pgr_last_acc;
	}
	public void setPgr_last_acc(Timestamp pgr_last_acc) {
		this.pgr_last_acc = pgr_last_acc;
	}
	public Timestamp getPgr_start() {
		return pgr_start;
	}
	public void setPgr_start(Timestamp pgr_start) {
		this.pgr_start = pgr_start;
	}
	public String getPrivilege() {
		return privilege;
	}
	public void setPrivilege(String privilege) {
		this.privilege = privilege;
	}
	public String getRes_src_link() {
		return res_src_link;
	}
	public void setRes_src_link(String res_src_link) {
		this.res_src_link = res_src_link;
	}
	public String getRes_src_type() {
		return res_src_type;
	}
	public void setRes_src_type(String res_src_type) {
		this.res_src_type = res_src_type;
	}
	public long getScore_multiplier() {
		return score_multiplier;
	}
	public void setScore_multiplier(long score_multiplier) {
		this.score_multiplier = score_multiplier;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public long getSub_num() {
		return sub_num;
	}
	public void setSub_num(long sub_num) {
		this.sub_num = sub_num;
	}
	public String getSubtype() {
		return subtype;
	}
	public void setSubtype(String subtype) {
		this.subtype = subtype;
	}
	public float getSuggested_time() {
		return suggested_time;
	}
	public void setSuggested_time(float suggested_time) {
		this.suggested_time = suggested_time;
	}
	public float getTime_limit() {
		return time_limit;
	}
	public void setTime_limit(float time_limit) {
		this.time_limit = time_limit;
	}
	public Timestamp getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDue_datetime() {
		return due_datetime;
	}
	public void setDue_datetime(String due_datetime) {
		this.due_datetime = due_datetime;
	}
	
	public List getFilterLst() {
		return filterLst;
	}
	public void setFilterLst(List filter_lst) {
		this.filterLst = filter_lst;
	}
	public String getSco_version() {
		return sco_version;
	}
	public void setSco_version(String scoVersion) {
		sco_version = scoVersion;
	}


	public boolean isMod_download_ind() {
		return mod_download_ind;
	}
	public void setMod_download_ind(boolean mod_download_ind) {
		this.mod_download_ind = mod_download_ind;
	}
	public long getMod_required_time() {
		return mod_required_time;
	}
	public void setMod_required_time(long mod_required_time) {
		this.mod_required_time = mod_required_time;
	}

}
