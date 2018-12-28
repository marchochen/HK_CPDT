package com.cwn.wizbank.cpd.vo;

public class CpdGroupSumVo implements java.io.Serializable{

	private static final long serialVersionUID = -1141760636376559076L;
	
	private Long cg_id;
	
	private Float sum_award_core_hours;
	
	private Float sum_award_non_core_hours;
	
	private Float req_core;
	
	private Float req_non_core;
	
	private boolean contain_non_core_ind;
	
	private Float outStanding_core;
	
	private Float outStanding_non_core;
	
	private boolean reg_ind;

	public Float getSum_award_core_hours() {
		return sum_award_core_hours;
	}

	public void setSum_award_core_hours(Float sum_award_core_hours) {
		this.sum_award_core_hours = sum_award_core_hours;
	}

	public Float getSum_award_non_core_hours() {
		return sum_award_non_core_hours;
	}

	public void setSum_award_non_core_hours(Float sum_award_non_core_hours) {
		this.sum_award_non_core_hours = sum_award_non_core_hours;
	}

	public Long getCg_id() {
		return cg_id;
	}

	public void setCg_id(Long cg_id) {
		this.cg_id = cg_id;
	}

	public Float getReq_core() {
		return req_core;
	}

	public void setReq_core(Float req_core) {
		this.req_core = req_core;
	}

	public Float getReq_non_core() {
		return req_non_core;
	}

	public void setReq_non_core(Float req_non_core) {
		this.req_non_core = req_non_core;
	}

	public boolean isContain_non_core_ind() {
		return contain_non_core_ind;
	}

	public void setContain_non_core_ind(boolean contain_non_core_ind) {
		this.contain_non_core_ind = contain_non_core_ind;
	}

	public Float getOutStanding_core() {
		return outStanding_core;
	}

	public void setOutStanding_core(Float outStanding_core) {
		this.outStanding_core = outStanding_core;
	}

	public Float getOutStanding_non_core() {
		return outStanding_non_core;
	}

	public void setOutStanding_non_core(Float outStanding_non_core) {
		this.outStanding_non_core = outStanding_non_core;
	}

	public boolean isReg_ind() {
		return reg_ind;
	}

	public void setReg_ind(boolean reg_ind) {
		this.reg_ind = reg_ind;
	}
	
}
