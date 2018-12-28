package com.cw.wizbank.JsonMod.links;
import java.sql.Timestamp;

import com.cw.wizbank.JsonMod.BaseParam;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwUtils;
public class LinksModuleParam extends BaseParam {
	private long fsl_id;
	private String fsl_title;
	private String fsl_url;
	private String fsl_status;
	private Timestamp fsl_update_timestamp;
	private String fsl_id_lst;
	private String fsl_update_timestamp_lst;
	
	public long getFsl_id() {
		return fsl_id;
	}
	public void setFsl_id(long fsl_id) {
		this.fsl_id = fsl_id;
	}
	public String getFsl_title() {
		return fsl_title;
	}
	public void setFsl_title(String fsl_title) throws cwException {
		fsl_title=cwUtils.unicodeFrom(fsl_title, clientEnc, encoding);
		this.fsl_title = fsl_title;
	}
	public String getFsl_url() {
		return fsl_url;
	}
	public void setFsl_url(String fsl_url) {
		this.fsl_url = fsl_url;
	}
	public String getFsl_status() {
		return fsl_status;
	}
	public void setFsl_status(String fsl_status) {
		this.fsl_status = fsl_status;
	}
	public Timestamp getFsl_update_timestamp() {
		return fsl_update_timestamp;
	}
	public void setFsl_update_timestamp(Timestamp fsl_update_timestamp) {
		this.fsl_update_timestamp = fsl_update_timestamp;
	}
	public String getFsl_id_lst() {
		return fsl_id_lst;
	}
	public void setFsl_id_lst(String fsl_id_lst) {
		this.fsl_id_lst = fsl_id_lst;
	}
	public String getFsl_update_timestamp_lst() {
		return fsl_update_timestamp_lst;
	}
	public void setFsl_update_timestamp_lst(String fsl_update_timestamp_lst) {
		this.fsl_update_timestamp_lst = fsl_update_timestamp_lst;
	}

}

