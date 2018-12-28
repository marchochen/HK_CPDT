package com.cw.wizbank.JsonMod.mobile;

import java.util.Vector;

public class MFolder {
	
	private long id;
	private String name;
	private Vector<MCourse> courses;
	private Vector<MFolder> folders;
	
	public void setId(long id) {
		this.id = id;
	}
	public long getId() {
		return id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setCourses(Vector<MCourse> courses) {
		this.courses = courses;
	}
	public Vector<MCourse> getCourses() {
		return courses;
	}
	public void setFolders(Vector<MFolder> folders) {
		this.folders = folders;
	}
	public Vector<MFolder> getFolders() {
		return folders;
	}
}