package com.cw.wizbank.know.db;

import java.sql.Timestamp;

/**
 * @author dean.chen
 * 
 */
public class KnowVoteDetailDB {

	private int kvd_que_id;

	private int kvd_ans_id;

	private int kvd_ent_id;

	private String kvd_create_usr_id;

	private Timestamp kvd_create_timestamp;

	public int getKvd_ans_id() {
		return kvd_ans_id;
	}

	public void setKvd_ans_id(int kvd_ans_id) {
		this.kvd_ans_id = kvd_ans_id;
	}

	public Timestamp getKvd_create_timestamp() {
		return kvd_create_timestamp;
	}

	public void setKvd_create_timestamp(Timestamp kvd_create_timestamp) {
		this.kvd_create_timestamp = kvd_create_timestamp;
	}

	public String getKvd_create_usr_id() {
		return kvd_create_usr_id;
	}

	public void setKvd_create_usr_id(String kvd_create_usr_id) {
		this.kvd_create_usr_id = kvd_create_usr_id;
	}

	public int getKvd_ent_id() {
		return kvd_ent_id;
	}

	public void setKvd_ent_id(int kvd_ent_id) {
		this.kvd_ent_id = kvd_ent_id;
	}

	public int getKvd_que_id() {
		return kvd_que_id;
	}

	public void setKvd_que_id(int kvd_que_id) {
		this.kvd_que_id = kvd_que_id;
	}

}
