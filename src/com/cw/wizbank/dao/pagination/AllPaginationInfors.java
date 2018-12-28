/*
 * 该javaben用于封装分页信息
 */
package com.cw.wizbank.dao.pagination;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AllPaginationInfors implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long startRecord = 0;//起始记录
	private long endRecord = 0;//结束记录
	private long recordCount = 0;//记录总数量
	private long onePageDisRecordCount = 0;//一页显示的记录总数量
	private List pageList = null;//页面导航的的序列

	private long nowPageNumber;//当前页面号码
	private long allPathCount;//页面总数量
	private List queryResultList = new ArrayList();//查询结果列表

	public AllPaginationInfors() {

	}

	public AllPaginationInfors(long startRecord, long endRecord,
			List pathList, long nowPageNumber, long allPathCount) {
		this.startRecord = startRecord;
		this.endRecord = endRecord;
		this.pageList = pathList;
		this.nowPageNumber = nowPageNumber;
		this.allPathCount = allPathCount;
	}

	public long getstartRecord() {
		return startRecord;
	}

	public long getEndRecord() {
		return endRecord;
	}

	public List getPageList() {
		return pageList;
	}

	public long getNowPageNumber() {
		return nowPageNumber;
	}

	public long getAllPathCount() {
		return allPathCount;
	}

	public void setQueryResultList(List queryResultList) {
		this.queryResultList = queryResultList;
	}

	public List getQueryResultList() {
		return queryResultList;
	}

	public void setRecordCount(long recordCount) {
		this.recordCount = recordCount;
	}

	public long getRecordCount() {
		return recordCount;
	}

	public void setOnePageDisRecordCount(long onePageDisRecordCount) {
		this.onePageDisRecordCount = onePageDisRecordCount;
	}

	public long getOnePageDisRecordCount() {
		return onePageDisRecordCount;
	}

}
