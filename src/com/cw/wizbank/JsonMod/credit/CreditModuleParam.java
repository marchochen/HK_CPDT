package com.cw.wizbank.JsonMod.credit;

import java.sql.Timestamp;

import com.cw.wizbank.JsonMod.BaseParam;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwUtils;

public class CreditModuleParam extends BaseParam {
	private float score_zd_init;
	private float score_sys_normal_login;
	private float score_sys_upd_my_profile;
	private float score_sys_submit_svy;
	private float score_sys_ins_topic;
	private float score_sys_ins_msg;
	private float score_sys_msg_upload_res;
	private float score_zd_new_que;
	private float score_zd_commit_ans;
	private float score_zd_right_ans;
	private float score_zd_cancel_que;
	private float score_itm_enrolled;
	private float score_itm_score_past_60;
	private float score_itm_score_past_70;
	private float score_itm_score_past_80;
	private float score_itm_score_past_90;
	private float score_itm_past_test;
	private float score_itm_submit_ass;
	private float score_itm_submit_svy;
	private float score_itm_view_rdg;
	private float score_itm_view_courseware;
	private float score_itm_past_courseware;
	private float score_itm_view_ref;
	private float score_itm_view_vod;
	private float score_itm_view_faq;
	private float score_itm_ins_topic;
	private float score_itm_ins_msg;
	private float score_itm_msg_upload_res;
	private float score_itm_import_credit;
	
	private float score_sys_create_group;
	
	private float score_sys_jion_group;
	private float score_sys_get_like;
	private float score_sys_cos_comment;
	private float score_sys_click_like;
	private float score_kb_share_knowledge;

	private long hit_sys_normal_login;
	private long hit_sys_upd_my_profile;
	private long hit_sys_ins_topic;
	private long hit_sys_ins_msg;
	private long hit_sys_msg_upload_res;
	private long hit_zd_new_que;
	private long hit_zd_commit_ans;
	private long hit_zd_right_ans;
	private long hit_zd_cancel_que;
	private long hit_itm_ins_topic;
	private long hit_itm_ins_msg;
	private long hit_itm_msg_upload_res;
	private long hit_sys_cos_comment;
	private long hit_kb_share_knowledge;
	
	//新增五项（参加公共调查问卷、创建群组、参与群组、被点赞、点赞）限制积分次数
	private long hit_sys_submit_svy;
	private long hit_sys_create_group;
	private long hit_sys_jion_group;
	private long hit_sys_get_like;
	private long hit_sys_click_like;
	
	
	private int cty_id;
	private String cty_code;
	private boolean cty_deduction_ind;
	private Timestamp cty_update_timestmap;
	
	private String cty_set_type;
	// 积分点所属培训中心
	private long cty_tcr_id;
	
	public long getCty_tcr_id() {
		return cty_tcr_id;
	}
	public void setCty_tcr_id(long ctyTcrId) {
		cty_tcr_id = ctyTcrId;
	}
	//设置课程积分，查找学员条件: YES已积分， NO未积分，空为查找所有已完成学员；
	private String ucd_itm_status;
	
	//设置课程积分，按用户名或全名搜索；
	private String usr_steid_or_diplaybil;
	
	//设置课程积分，课程/班级ID
	private String sel_app_id_list;
	private String sel_usr_ent_id_list;
	private long itm_id;
	
	private String usr_n_usg_id_lst;
	private float input_point;
	
	
	public float getInput_point() {
		return input_point;
	}
	public void setInput_point(float input_point) {
		this.input_point = input_point;
	}
	public String getCty_code() {
		return cty_code;
	}
	public void setCty_code(String cty_code) {
		this.cty_code = cty_code;
	}
	public boolean isCty_deduction_ind() {
		return cty_deduction_ind;
	}
	public void setCty_deduction_ind(boolean cty_deduction_ind) {
		this.cty_deduction_ind = cty_deduction_ind;
	}
	public boolean getCty_deduction_ind() {
		return cty_deduction_ind;
	}
	public int getCty_id() {
		return cty_id;
	}
	public void setCty_id(int cty_id) {
		this.cty_id = cty_id;
	}
	public String getCty_set_type() {
		return cty_set_type;
	}
	public void setCty_set_type(String cty_set_type) {
		this.cty_set_type = cty_set_type;
	}
	public Timestamp getCty_update_timestmap() {
		return cty_update_timestmap;
	}
	public void setCty_update_timestmap(Timestamp cty_update_timestmap) {
		this.cty_update_timestmap = cty_update_timestmap;
	}
	public long getHit_itm_ins_msg() {
		return hit_itm_ins_msg;
	}
	public void setHit_itm_ins_msg(long hit_itm_ins_msg) {
		this.hit_itm_ins_msg = hit_itm_ins_msg;
	}
	public long getHit_itm_ins_topic() {
		return hit_itm_ins_topic;
	}
	public void setHit_itm_ins_topic(long hit_itm_ins_topic) {
		this.hit_itm_ins_topic = hit_itm_ins_topic;
	}
	public long getHit_itm_msg_upload_res() {
		return hit_itm_msg_upload_res;
	}
	public void setHit_itm_msg_upload_res(long hit_itm_msg_upload_res) {
		this.hit_itm_msg_upload_res = hit_itm_msg_upload_res;
	}
	public long getHit_sys_ins_msg() {
		return hit_sys_ins_msg;
	}
	public void setHit_sys_ins_msg(long hit_sys_ins_msg) {
		this.hit_sys_ins_msg = hit_sys_ins_msg;
	}
	public long getHit_sys_ins_topic() {
		return hit_sys_ins_topic;
	}
	public void setHit_sys_ins_topic(long hit_sys_ins_topic) {
		this.hit_sys_ins_topic = hit_sys_ins_topic;
	}
	public long getHit_sys_msg_upload_res() {
		return hit_sys_msg_upload_res;
	}
	public void setHit_sys_msg_upload_res(long hit_sys_msg_upload_res) {
		this.hit_sys_msg_upload_res = hit_sys_msg_upload_res;
	}
	public long getHit_sys_normal_login() {
		return hit_sys_normal_login;
	}
	public void setHit_sys_normal_login(long hit_sys_normal_login) {
		this.hit_sys_normal_login = hit_sys_normal_login;
	}
	public long getHit_sys_upd_my_profile() {
		return hit_sys_upd_my_profile;
	}
	public void setHit_sys_upd_my_profile(long hit_sys_upd_my_profile) {
		this.hit_sys_upd_my_profile = hit_sys_upd_my_profile;
	}
	public long getHit_zd_cancel_que() {
		return hit_zd_cancel_que;
	}
	public void setHit_zd_cancel_que(long hit_zd_cancel_que) {
		this.hit_zd_cancel_que = hit_zd_cancel_que;
	}
	public long getHit_zd_commit_ans() {
		return hit_zd_commit_ans;
	}
	public void setHit_zd_commit_ans(long hit_zd_commit_ans) {
		this.hit_zd_commit_ans = hit_zd_commit_ans;
	}
	public long getHit_zd_new_que() {
		return hit_zd_new_que;
	}
	public void setHit_zd_new_que(long hit_zd_new_que) {
		this.hit_zd_new_que = hit_zd_new_que;
	}
	public long getHit_zd_right_ans() {
		return hit_zd_right_ans;
	}
	public void setHit_zd_right_ans(long hit_zd_right_ans) {
		this.hit_zd_right_ans = hit_zd_right_ans;
	}
	public long getItm_id() {
		return itm_id;
	}
	public void setItm_id(long itm_id) {
		this.itm_id = itm_id;
	}
	public float getScore_itm_enrolled() {
		return score_itm_enrolled;
	}
	public void setScore_itm_enrolled(float score_itm_enrolled) {
		this.score_itm_enrolled = score_itm_enrolled;
	}
	public float getScore_itm_import_credit() {
		return score_itm_import_credit;
	}
	public void setScore_itm_import_credit(float score_itm_import_credit) {
		this.score_itm_import_credit = score_itm_import_credit;
	}
	public float getScore_itm_ins_msg() {
		return score_itm_ins_msg;
	}
	public void setScore_itm_ins_msg(float score_itm_ins_msg) {
		this.score_itm_ins_msg = score_itm_ins_msg;
	}
	public float getScore_itm_ins_topic() {
		return score_itm_ins_topic;
	}
	public void setScore_itm_ins_topic(float score_itm_ins_topic) {
		this.score_itm_ins_topic = score_itm_ins_topic;
	}
	public float getScore_itm_msg_upload_res() {
		return score_itm_msg_upload_res;
	}
	public void setScore_itm_msg_upload_res(float score_itm_msg_upload_res) {
		this.score_itm_msg_upload_res = score_itm_msg_upload_res;
	}
	public float getScore_itm_past_courseware() {
		return score_itm_past_courseware;
	}
	public void setScore_itm_past_courseware(float score_itm_past_courseware) {
		this.score_itm_past_courseware = score_itm_past_courseware;
	}
	public float getScore_itm_past_test() {
		return score_itm_past_test;
	}
	public void setScore_itm_past_test(float score_itm_past_test) {
		this.score_itm_past_test = score_itm_past_test;
	}
	public float getScore_itm_score_past_60() {
		return score_itm_score_past_60;
	}
	public void setScore_itm_score_past_60(float score_itm_score_past_60) {
		this.score_itm_score_past_60 = score_itm_score_past_60;
	}
	public float getScore_itm_score_past_70() {
		return score_itm_score_past_70;
	}
	public void setScore_itm_score_past_70(float score_itm_score_past_70) {
		this.score_itm_score_past_70 = score_itm_score_past_70;
	}
	public float getScore_itm_score_past_80() {
		return score_itm_score_past_80;
	}
	public void setScore_itm_score_past_80(float score_itm_score_past_80) {
		this.score_itm_score_past_80 = score_itm_score_past_80;
	}
	public float getScore_itm_score_past_90() {
		return score_itm_score_past_90;
	}
	public void setScore_itm_score_past_90(float score_itm_score_past_90) {
		this.score_itm_score_past_90 = score_itm_score_past_90;
	}
	public float getScore_itm_submit_ass() {
		return score_itm_submit_ass;
	}
	public void setScore_itm_submit_ass(float score_itm_submit_ass) {
		this.score_itm_submit_ass = score_itm_submit_ass;
	}
	public float getScore_itm_submit_svy() {
		return score_itm_submit_svy;
	}
	public void setScore_itm_submit_svy(float score_itm_submit_svy) {
		this.score_itm_submit_svy = score_itm_submit_svy;
	}
	public float getScore_itm_view_courseware() {
		return score_itm_view_courseware;
	}
	public void setScore_itm_view_courseware(float score_itm_view_courseware) {
		this.score_itm_view_courseware = score_itm_view_courseware;
	}
	public float getScore_itm_view_faq() {
		return score_itm_view_faq;
	}
	public void setScore_itm_view_faq(float score_itm_view_faq) {
		this.score_itm_view_faq = score_itm_view_faq;
	}
	public float getScore_itm_view_rdg() {
		return score_itm_view_rdg;
	}
	public void setScore_itm_view_rdg(float score_itm_view_rdg) {
		this.score_itm_view_rdg = score_itm_view_rdg;
	}
	public float getScore_itm_view_ref() {
		return score_itm_view_ref;
	}
	public void setScore_itm_view_ref(float score_itm_view_ref) {
		this.score_itm_view_ref = score_itm_view_ref;
	}
	public float getScore_itm_view_vod() {
		return score_itm_view_vod;
	}
	public void setScore_itm_view_vod(float score_itm_view_vod) {
		this.score_itm_view_vod = score_itm_view_vod;
	}
	public float getScore_sys_ins_msg() {
		return score_sys_ins_msg;
	}
	public void setScore_sys_ins_msg(float score_sys_ins_msg) {
		this.score_sys_ins_msg = score_sys_ins_msg;
	}
	public float getScore_sys_ins_topic() {
		return score_sys_ins_topic;
	}
	public void setScore_sys_ins_topic(float score_sys_ins_topic) {
		this.score_sys_ins_topic = score_sys_ins_topic;
	}
	public float getScore_sys_msg_upload_res() {
		return score_sys_msg_upload_res;
	}
	public void setScore_sys_msg_upload_res(float score_sys_msg_upload_res) {
		this.score_sys_msg_upload_res = score_sys_msg_upload_res;
	}
	public float getScore_sys_normal_login() {
		return score_sys_normal_login;
	}
	public void setScore_sys_normal_login(float score_sys_normal_login) {
		this.score_sys_normal_login = score_sys_normal_login;
	}
	public float getScore_sys_submit_svy() {
		return score_sys_submit_svy;
	}
	public void setScore_sys_submit_svy(float score_sys_submit_svy) {
		this.score_sys_submit_svy = score_sys_submit_svy;
	}
	public float getScore_sys_upd_my_profile() {
		return score_sys_upd_my_profile;
	}
	public void setScore_sys_upd_my_profile(float score_sys_upd_my_profile) {
		this.score_sys_upd_my_profile = score_sys_upd_my_profile;
	}
	public float getScore_zd_cancel_que() {
		return score_zd_cancel_que;
	}
	public void setScore_zd_cancel_que(float score_zd_cancel_que) {
		this.score_zd_cancel_que = score_zd_cancel_que;
	}
	public float getScore_zd_commit_ans() {
		return score_zd_commit_ans;
	}
	public void setScore_zd_commit_ans(float score_zd_commit_ans) {
		this.score_zd_commit_ans = score_zd_commit_ans;
	}
	public float getScore_zd_init() {
		return score_zd_init;
	}
	public void setScore_zd_init(float score_zd_init) {
		this.score_zd_init = score_zd_init;
	}
	public float getScore_zd_new_que() {
		return score_zd_new_que;
	}
	public void setScore_zd_new_que(float score_zd_new_que) {
		this.score_zd_new_que = score_zd_new_que;
	}
	public float getScore_zd_right_ans() {
		return score_zd_right_ans;
	}
	public void setScore_zd_right_ans(float score_zd_right_ans) {
		this.score_zd_right_ans = score_zd_right_ans;
	}
	public String getSel_app_id_list() {
		return sel_app_id_list;
	}
	public void setSel_app_id_list(String sel_app_id_list) {
		this.sel_app_id_list = sel_app_id_list;
	}
	public String getSel_usr_ent_id_list() {
		return sel_usr_ent_id_list;
	}
	public void setSel_usr_ent_id_list(String sel_usr_ent_id_list) {
		this.sel_usr_ent_id_list = sel_usr_ent_id_list;
	}
	public String getUcd_itm_status() {
		return ucd_itm_status;
	}
	public void setUcd_itm_status(String ucd_itm_status) {
		this.ucd_itm_status = ucd_itm_status;
	}
	public String getUsr_n_usg_id_lst() {
		return usr_n_usg_id_lst;
	}
	public void setUsr_n_usg_id_lst(String usr_n_usg_id_lst) {
		this.usr_n_usg_id_lst = usr_n_usg_id_lst;
	}
	public String getUsr_steid_or_diplaybil() {
		return usr_steid_or_diplaybil;
	}
	public void setUsr_steid_or_diplaybil(String usr_steid_or_diplaybil) {
		this.usr_steid_or_diplaybil = usr_steid_or_diplaybil;
	}
	public float getScore_sys_create_group() {
		return score_sys_create_group;
	}
	public void setScore_sys_create_group(float scoreSysCreateGroup) {
		score_sys_create_group = scoreSysCreateGroup;
	}
	public float getScore_sys_jion_group() {
		return score_sys_jion_group;
	}
	public void setScore_sys_jion_group(float scoreSysJionGroup) {
		score_sys_jion_group = scoreSysJionGroup;
	}
	public float getScore_sys_get_like() {
		return score_sys_get_like;
	}
	public void setScore_sys_get_like(float scoreSysGetLike) {
		score_sys_get_like = scoreSysGetLike;
	}
	public float getScore_sys_cos_comment() {
		return score_sys_cos_comment;
	}
	public void setScore_sys_cos_comment(float scoreSysCosComment) {
		score_sys_cos_comment = scoreSysCosComment;
	}

	public float getScore_sys_click_like() {
		return score_sys_click_like;
	}
	public void setScore_sys_click_like(float scoreSysClickLike) {
		score_sys_click_like = scoreSysClickLike;
	}
	
	public long getHit_sys_cos_comment() {
		return hit_sys_cos_comment;
	}
	public void setHit_sys_cos_comment(long hitSysCosComment) {
		hit_sys_cos_comment = hitSysCosComment;
	}
	
	public float getScore_kb_share_knowledge() {
		return score_kb_share_knowledge;
	}
	public void setScore_kb_share_knowledge(float score_kb_share_knowledge) {
		this.score_kb_share_knowledge = score_kb_share_knowledge;
	}
	
	public long getHit_kb_share_knowledge() {
		return hit_kb_share_knowledge;
	}
	public void setHit_kb_share_knowledge(long hit_kb_share_knowledge) {
		this.hit_kb_share_knowledge = hit_kb_share_knowledge;
	}
	public long getHit_sys_submit_svy() {
		return hit_sys_submit_svy;
	}
	public void setHit_sys_submit_svy(long hit_sys_submit_svy) {
		this.hit_sys_submit_svy = hit_sys_submit_svy;
	}
	public long getHit_sys_create_group() {
		return hit_sys_create_group;
	}
	public void setHit_sys_create_group(long hit_sys_create_group) {
		this.hit_sys_create_group = hit_sys_create_group;
	}
	public long getHit_sys_jion_group() {
		return hit_sys_jion_group;
	}
	public void setHit_sys_jion_group(long hit_sys_jion_group) {
		this.hit_sys_jion_group = hit_sys_jion_group;
	}
	public long getHit_sys_get_like() {
		return hit_sys_get_like;
	}
	public void setHit_sys_get_like(long hit_sys_get_like) {
		this.hit_sys_get_like = hit_sys_get_like;
	}
	public long getHit_sys_click_like() {
		return hit_sys_click_like;
	}
	public void setHit_sys_click_like(long hit_sys_click_like) {
		this.hit_sys_click_like = hit_sys_click_like;
	}

	
	
}