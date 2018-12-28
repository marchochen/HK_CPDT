package com.cw.wizbank.JsonMod.Course;
import com.cw.wizbank.JsonMod.*;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwUtils;

public class CourseModuleParam extends BaseParam {
	private String activetab ;

	private long tcr_id = 0;	// 培训中心ID(初始值为0，默认为总培训中心)
	private long res_id;
	private long tkh_id;
	private long sgp_id;
	private int floor_num = 0;	// 要显示的目录层数(当floor_num的值为0时，则遍历所有层次的子目录)
	private long tnd_id = 0;	// 目录ID(当tnd_id的值为0时，默认为输出某个培训中心下所有顶层节点目录树；当为其他值时，输出该节点目录树)
	private long id = 0;		// 目录ID，用于前台目录树中传递到后台
	private String tnd_title;	// 目录名
	private String note;
	private String search_id;	// 高级搜索的类型(判别用户从课程页面、还是从知道、还是从讨论区搜索)，此search_id对应一个是搜索条件(放入Session中)
	
	// 以下是搜索参数
	private String itm_code;				// 课程编号
	private String[] srh_key; 				// 搜索关键词
	private String srh_key_type = "TITLE";	// 是否全文搜索(可选值："TITLE"、"FULLTEXT")
	private long[] tnd_id_lst;				// 目录ID 
	private String srh_itm_type_lst;		// 课程类型选项
	private String ske_id_lst;	// 对应岗位能力实体表(cmSkillEntitiy表)的ske_id(形如""、"0"、"1"或"1,2,3"，其中""则认为不对能力进行筛选，"0"则认为目标课程的所有能力)
	
	private String srh_appn_start_datetime;	// 报名日期的开始搜索时间
	private String srh_appn_end_datetime;	// 报名日期的结束搜索时间
	
	private String from;		// 搜索类型的名称("lrn_center":选课中心，"adv_itm_srh":高级搜索)
	private boolean not_planed;	// 是否过滤学习计划(true:过滤 false:不过滤)
	
	// 以下是时间期限搜索参数的可选值：
	// 即时开始 IMMEDIATE、最近一周 LAST_1_WEEK、最近两周 LAST_2_WEEK
	// 最近一个月 LAST_1_MONTH、最近两个月 LAST_2_MONTH、不限 UNLIMITED
	private String srh_start_period;// 开课日期时间期限
	
	private long score;
	private String comment;

    // for item template
    private String tvw_id;

    private String stylesheet;

    private boolean show_run_ind;

    private boolean show_session_ind;
	
	private long itm_id;
	
	//for to get training center and catalog tree
	private int node_id;
	private String node_type;	// 可选值："TC"、"CATALOG"
	
	private int rec_num = 5;	// "课程信息"页面显示相关培训的数目
	
	private boolean page_readonly;
	
	//check whether the record need be paged and default is true.
	private boolean paging_ind = true;
	
	private String poc_level;//岗位层级
	//移动信息
	private boolean isMobile = false;
	
	public int getRec_num() {
		return rec_num;
	}

	public void setRec_num(int rec_num) {
		this.rec_num = rec_num;
	}

	public String getActivetab() {
		return activetab;
	}

	public void setActivetab(String activetab) {
		this.activetab = activetab;
	}

	
	public long getTcr_id() {
		return tcr_id;
	}

	public void setTcr_id(long tcr_id) {
		this.tcr_id = tcr_id;
	}

	public long getRes_id() {
		return res_id;
	}

	public void setRes_id(long res_id) {
		this.res_id = res_id;
	}

	public long getTkh_id() {
		return tkh_id;
	}

	public void setTkh_id(long tkh_id) {
		this.tkh_id = tkh_id;
	}

	public long getSgp_id() {
		return sgp_id;
	}

	public void setSgp_id(long sgp_id) {
		this.sgp_id = sgp_id;
	}

	public int getFloor_num() {
		return floor_num;
	}

	public void setFloor_num(int floor_num) {
		this.floor_num = floor_num;
	}

	public long getTnd_id() {
		return tnd_id;
	}

	public void setTnd_id(long tnd_id) {
		this.tnd_id = tnd_id;
	}

	public String getTnd_title() {
		return tnd_title;
	}

	public void setTnd_title(String tnd_title) {
		this.tnd_title = tnd_title;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) throws cwException {
		this.note = cwUtils.unicodeFrom(note, clientEnc, encoding);
	}



	public long getScore() {
		return score;
	}

	public void setScore(long score) {
		this.score = score;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) throws cwException {
		this.comment = cwUtils.unicodeFrom(comment, clientEnc, encoding);
	}

	public long getItm_id() {
		return itm_id;
	}

	public void setItm_id(long itm_id) {
		this.itm_id = itm_id;
	}

	public String[] getSrh_key() {
		return srh_key;
	}

	public void setSrh_key(String[] srh_key) throws cwException {
		this.srh_key = srh_key;
	}

	public String getSrh_key_type() {
		return srh_key_type;
	}

	public void setSrh_key_type(String srh_key_type) {
		this.srh_key_type = srh_key_type;
	}

	public long[] getTnd_id_lst() {
		return tnd_id_lst;
	}

	public void setTnd_id_lst(long[] tnd_id_lst) {
		this.tnd_id_lst = tnd_id_lst;
	}

	public String getSrh_itm_type_lst() {
		return srh_itm_type_lst;
	}

	public void setSrh_itm_type_lst(String srh_itm_type_lst) {
		this.srh_itm_type_lst = srh_itm_type_lst;
	}

	public String getSrh_start_period() {
		return srh_start_period;
	}

	public void setSrh_start_period(String srh_start_period) {
		this.srh_start_period = srh_start_period;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

    public String getTvw_id() {
        return tvw_id;
    }

    public void setTvw_id(String tvw_id) {
        this.tvw_id = tvw_id;
    }

    public String getStylesheet() {
        return stylesheet;
    }

    public void setStylesheet(String stylesheet) {
        this.stylesheet = stylesheet;
    }

    public boolean isShow_run_ind() {
        return show_run_ind;
    }

    public void setShow_run_ind(boolean show_run_ind) {
        this.show_run_ind = show_run_ind;
    }

    public boolean isShow_session_ind() {
        return show_session_ind;
    }

    public void setShow_session_ind(boolean show_session_ind) {
        this.show_session_ind = show_session_ind;
    }

	public String getSearch_id() {
		return search_id;
	}

	public void setSearch_id(String search_id) {
		this.search_id = search_id;
	}

	public String getItm_code() {
		return itm_code;
	}

	public void setItm_code(String itm_code) {
		this.itm_code = itm_code;
	}

	public String getSrh_appn_start_datetime() {
		return srh_appn_start_datetime;
	}

	public void setSrh_appn_start_datetime(String srh_appn_start_datetime) {
		this.srh_appn_start_datetime = srh_appn_start_datetime;
	}

	public String getSrh_appn_end_datetime() {
		return srh_appn_end_datetime;
	}

	public void setSrh_appn_end_datetime(String srh_appn_end_datetime) {
		this.srh_appn_end_datetime = srh_appn_end_datetime;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getNode_id() {
		return node_id;
	}

	public void setNode_id(int node_id) {
		this.node_id = node_id;
	}

	public String getNode_type() {
		return node_type;
	}

	public void setNode_type(String node_type) {
		this.node_type = node_type;
	}

	public String getSke_id_lst() {
		return ske_id_lst;
	}

	public void setSke_id_lst(String ske_id_lst) {
		this.ske_id_lst = ske_id_lst;
	}

	public boolean isNot_planed() {
		return not_planed;
	}

	public void setNot_planed(boolean not_planed) {
		this.not_planed = not_planed;
	}

	public boolean isPage_readonly() {
		return page_readonly;
	}

	public void setPage_readonly(boolean page_readonly) {
		this.page_readonly = page_readonly;
	}

	public boolean isPaging_ind() {
		return paging_ind;
	}

	public void setPaging_ind(boolean paging_ind) {
		this.paging_ind = paging_ind;
	}

	public String getPoc_level() {
		return poc_level;
	}

	public void setPoc_level(String poc_level) {
		this.poc_level = poc_level;
	}

	public boolean isMobile() {
		return isMobile;
	}

	public void setMobile(boolean isMobile) {
		this.isMobile = isMobile;
	}
	
}
