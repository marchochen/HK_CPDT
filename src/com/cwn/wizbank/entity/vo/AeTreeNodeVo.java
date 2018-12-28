package com.cwn.wizbank.entity.vo;



public class AeTreeNodeVo implements java.io.Serializable {
	private static final long serialVersionUID = -93769955642183176L;

    public static final String TND_TYPE_CAT = "CATALOG";
    public static final String TND_TYPE_NORMAL = "NORMAL";
    public static final String TND_TYPE_LINK = "LINK";
    public static final String TND_TYPE_ITEM = "ITEM";

	private long id;
	private long pId;
	private String name;
	private String type;
	private int open;
	private int isParent;
	private boolean hasChild;
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
	public int getOpen() {
		return open;
	}
	public void setOpen(int open) {
		this.open = open;
	}
	public int getIsParent() {
		return isParent;
	}
	public void setIsParent(int isParent) {
		this.isParent = isParent;
	}
	public boolean isHasChild() {
		return hasChild;
	}
	public void setHasChild(boolean hasChild) {
		this.hasChild = hasChild;
	}
	public boolean isNocheck() {
		return nocheck;
	}
	public void setNocheck(boolean nocheck) {
		this.nocheck = nocheck;
	}
	
}