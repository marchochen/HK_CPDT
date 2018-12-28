package com.cw.wizbank.studyGroup;

import java.sql.Timestamp;

public class StudyGroupDB {
	public long sgp_id;
	public long sgp_tcr_id;
	public String sgp_title;
	public String sgp_desc;
	public int sgp_public_type;
	public int sgp_send_email_ind;
	public String sgp_create_usr_id; 
	public Timestamp sgp_create_timestamp;
	public Timestamp sgp_upd_timestamp; 
}
