package com.cwn.wizbank.entity.vo;


/**
 *  职级发展序列Vo
 * @author halo.pan
 *
 */
public class GradeNodesVo {
	
	long pfs_id;
	String pfs_title;
	long ugr_ent_id;
	String ugr_display_bil;
	String url;
	public long getPfs_id() {
		return pfs_id;
	}
	public void setPfs_id(long pfs_id) {
		this.pfs_id = pfs_id;
	}
	public String getPfs_title() {
		return pfs_title;
	}
	public void setPfs_title(String pfs_title) {
		this.pfs_title = pfs_title;
	}
	public long getUgr_ent_id() {
		return ugr_ent_id;
	}
	public void setUgr_ent_id(long ugr_ent_id) {
		this.ugr_ent_id = ugr_ent_id;
	}
	public String getUgr_display_bil() {
		return ugr_display_bil;
	}
	public void setUgr_display_bil(String ugr_display_bil) {
		this.ugr_display_bil = ugr_display_bil;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
    

}
