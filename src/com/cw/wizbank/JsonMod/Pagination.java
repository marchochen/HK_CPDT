package com.cw.wizbank.JsonMod;

public class Pagination {
	private int total_rec;

	private String sort;

	private String dir;

	private int limit=10;
	private int start=0;

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public int getTotal_rec() {
		return total_rec;
	}

	public void setTotal_rec(int total_rec) {
		this.total_rec = total_rec;
	}

	public Pagination(int total, int start, int limit, String sort, String dir) {
		this.total_rec=total;
		this.start=start;
		this.sort=sort;
		this.dir=dir;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}
}
