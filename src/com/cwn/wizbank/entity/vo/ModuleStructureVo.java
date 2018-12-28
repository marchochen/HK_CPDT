package com.cwn.wizbank.entity.vo;

import java.util.ArrayList;
import java.util.List;

import com.cwn.wizbank.entity.Resources;

public class ModuleStructureVo {

	long id;
	String identifier;
	String title;
	String itemtype;
	String restype;
	
	Resources resources;
		
	List<ModuleStructureVo> children = new ArrayList<ModuleStructureVo>();

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getItemtype() {
		return itemtype;
	}

	public void setItemtype(String itemtype) {
		this.itemtype = itemtype;
	}

	public String getRestype() {
		return restype;
	}

	public void setRestype(String restype) {
		this.restype = restype;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Resources getResources() {
		return resources;
	}

	public void setResources(Resources resources) {
		this.resources = resources;
	}

	public List<ModuleStructureVo> getChildren() {
		return children;
	}

	public void setChildren(List<ModuleStructureVo> children) {
		this.children = children;
	}
	
	
	
	
}
