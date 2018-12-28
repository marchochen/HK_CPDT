package com.cwn.wizbank.entity.vo;



/**
 * <p>Title:TreeNodeVo</p>
 * <p>Description:ztree树 </p>
 * @author halo.pan
 *
 * @date 2016年4月5日 下午3:34:11
 *
 */
public class TreeNodeVo implements java.io.Serializable {
	private static final long serialVersionUID = -601729520222081996L;
	public static final String ROOT_ROLE = "ROOT";
	private long id;
	private long pId;
	private String name;
	private String type;
	private boolean open;
	private boolean isParent;
	private String role;
	private boolean nocheck;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getpId() {
		return pId;
	}
	public void setpId(long pId) {
		this.pId = pId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public boolean isOpen() {
		return open;
	}
	public void setOpen(boolean open) {
		this.open = open;
	}
	public boolean isIsParent() {
		return isParent;
	}
	public void setIsParent(boolean isParent) {
		this.isParent = isParent;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public boolean isNocheck() {
		return nocheck;
	}
	public void setNocheck(boolean nocheck) {
		this.nocheck = nocheck;
	}
	
	
}