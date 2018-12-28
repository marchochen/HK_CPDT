package com.cw.wizbank.trunkinterface;

import java.sql.Timestamp;

import com.cw.wizbank.JsonMod.BaseParam;

public class InterfaceModuleParam extends BaseParam
{
	public String usr_id;//登录名
	public String usr_pwd;//密码

	public long msg_id;//公告ID
	
	public long cos_id;//课程cos_res_id
	public long tkh_id;//学习跟踪ID
	public long itm_id;//课程ID
	public String comment;//课程评论
	public long score;//课程评分
	public String weixin_id;//微信ID
	
	public long mod_id;//模块ID
    public Timestamp start_datetime;//考试开始时间
	public String que_id_lst;//题目ID列表
	public String que_anwser_option_lst;//学员在每道题的答案列表
	public String que_anwser_option_id_lst;//学员在每道题的答案id列表
	public long duration;
	
	public int exam_ind;//0: 课程； 1：考试
	//分页信息
	private int cur_page;
	private int cur_size;
	//调查问卷信息
	private long used_time;
	//移动课程搜索
	private String title;
	private long tnd_id;
	private String order;
	private Timestamp last_time;
	private String cos_id_lst;
	private String tkh_id_lst;
	private String en_user;
	private boolean rem_me;
	//批量上传学习记录
	private String mod_id_lst;
	private String duration_lst;
	private String last_time_lst;
	
	public int getExam_ind() {
        return exam_ind;
    }
    public void setExam_ind(int examInd) {
        exam_ind = examInd;
    }
    public long getDuration() {
        return duration;
    }
    public void setDuration(long duration) {
        this.duration = duration;
    }
    public String getUsr_id() {
		return usr_id;
	}
	public void setUsr_id(String usrId) {
		usr_id = usrId;
	}
	public String getUsr_pwd() {
		return usr_pwd;
	}
	public void setUsr_pwd(String usrPwd) {
		usr_pwd = usrPwd;
	}
	public long getMsg_id() {
		return msg_id;
	}
	public void setMsg_id(long msgId) {
		msg_id = msgId;
	}
	public long getCos_id() {
		return cos_id;
	}
	public void setCos_id(long cosId) {
		cos_id = cosId;
	}
	public long getTkh_id() {
		return tkh_id;
	}
	public void setTkh_id(long tkhId) {
		tkh_id = tkhId;
	}
	public long getItm_id() {
		return itm_id;
	}
	public String getComment() {
		return comment;
	}
	public long getScore() {
		return score;
	}
	public void setItm_id(long itm_id) {
		this.itm_id = itm_id;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public void setScore(long score) {
		this.score = score;
	}
	public String getWeixin_id() {
		return weixin_id;
	}
	public void setWeixin_id(String weixinId) {
		weixin_id = weixinId;
	}
	
	public long getMod_id() {
        return mod_id;
    }
    public void setMod_id(long modId) {
        mod_id = modId;
    }
    public Timestamp getStart_datetime() {
        return start_datetime;
    }
    public void setStart_datetime(Timestamp startDatetime) {
        start_datetime = startDatetime;
    }
    public String getQue_id_lst() {
        return que_id_lst;
    }
    public void setQue_id_lst(String queIdLst) {
        que_id_lst = queIdLst;
    }
    public String getQue_anwser_option_lst() {
        return que_anwser_option_lst;
    }
    public void setQue_anwser_option_lst(String queAnwserOptionLst) {
        que_anwser_option_lst = queAnwserOptionLst;
    }
	public String getQue_anwser_option_id_lst() {
		return que_anwser_option_id_lst;
	}
	public void setQue_anwser_option_id_lst(String que_anwser_option_id_lst) {
		this.que_anwser_option_id_lst = que_anwser_option_id_lst;
	}
	public int getCur_page() {
		return cur_page;
	}
	public void setCur_page(int cur_page) {
		this.cur_page = cur_page;
	}
	public int getCur_size() {
		return cur_size;
	}
	public void setCur_size(int cur_size) {
		this.cur_size = cur_size;
	}
	public long getUsed_time() {
		return used_time;
	}
	public void setUsed_time(long used_time) {
		this.used_time = used_time;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public long getTnd_id() {
		return tnd_id;
	}
	public void setTnd_id(long tnd_id) {
		this.tnd_id = tnd_id;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public Timestamp getLast_time() {
		return last_time;
	}
	public void setLast_time(Timestamp last_time) {
		this.last_time = last_time;
	}
	public String getCos_id_lst() {
		return cos_id_lst;
	}
	public void setCos_id_lst(String cos_id_lst) {
		this.cos_id_lst = cos_id_lst;
	}
	public String getTkh_id_lst() {
		return tkh_id_lst;
	}
	public void setTkh_id_lst(String tkh_id_lst) {
		this.tkh_id_lst = tkh_id_lst;
	}
	public String getEn_user() {
		return en_user;
	}
	public void setEn_user(String en_user) {
		this.en_user = en_user;
	}
	public boolean isRem_me() {
		return rem_me;
	}
	public void setRem_me(boolean rem_me) {
		this.rem_me = rem_me;
	}
	public String getMod_id_lst() {
		return mod_id_lst;
	}
	public void setMod_id_lst(String mod_id_lst) {
		this.mod_id_lst = mod_id_lst;
	}
	public String getDuration_lst() {
		return duration_lst;
	}
	public void setDuration_lst(String duration_lst) {
		this.duration_lst = duration_lst;
	}
	public String getLast_time_lst() {
		return last_time_lst;
	}
	public void setLast_time_lst(String last_time_lst) {
		this.last_time_lst = last_time_lst;
	}
}
