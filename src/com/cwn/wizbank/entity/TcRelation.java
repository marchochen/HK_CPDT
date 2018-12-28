package com.cwn.wizbank.entity;

import java.util.Date;

public class TcRelation {
	Long tcn_child_tcr_id;
	Long tcn_ancestor;
	Long tcn_order;
	String tcn_create_usr_id;
	Date tcn_create_timestamp;
	public Long getTcn_child_tcr_id() {
		return tcn_child_tcr_id;
	}
	public void setTcn_child_tcr_id(Long tcnChildTcrId) {
		tcn_child_tcr_id = tcnChildTcrId;
	}
	public Long getTcn_ancestor() {
		return tcn_ancestor;
	}
	public void setTcn_ancestor(Long tcnAncestor) {
		tcn_ancestor = tcnAncestor;
	}
	public Long getTcn_order() {
		return tcn_order;
	}
	public void setTcn_order(Long tcnOrder) {
		tcn_order = tcnOrder;
	}
	public String getTcn_create_usr_id() {
		return tcn_create_usr_id;
	}
	public void setTcn_create_usr_id(String tcnCreateUsrId) {
		tcn_create_usr_id = tcnCreateUsrId;
	}
	public Date getTcn_create_timestamp() {
		return tcn_create_timestamp;
	}
	public void setTcn_create_timestamp(Date tcnCreateTimestamp) {
		tcn_create_timestamp = tcnCreateTimestamp;
	}

}
