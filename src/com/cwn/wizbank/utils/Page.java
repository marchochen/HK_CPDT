/* 
 * 
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of Finalist IT Group, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with Teamsun.
 * 
 */
package com.cwn.wizbank.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.NativeWebRequest;

import com.cw.wizbank.util.cwPagination;



/** 
 * 对分页的基本数据进行一个简单的封装 
 */  
public class Page<T> {  
   
	public static Logger logger = LoggerFactory.getLogger(Page.class);
	
    private int pageNo = 1;//页码，默认是第一页  
    private int pageSize = 10;//每页显示的记录数，默认是15  
    private int totalRecord;//总记录数  
    private int totalPage;//总页数  
    private List<T> results;//对应的当前页记录  
    private Map<String, Object> params = new HashMap<String, Object>();//其他的参数我们把它分装成一个Map对象  
   
	private String sortname;
	private String sortorder;
    
    public Page(){
    	
    }
    
    public Page(NativeWebRequest webRequest){
		String param = null;
		String value = null;

		param = "pageNo";
		value = webRequest.getParameter(param);
		if (value != null && value.length() > 0) {
			this.pageNo = Integer.parseInt(value);
		} else {
			this.pageNo = 1;
		}

		param = "pageSize";
		value = webRequest.getParameter(param);
		if (value != null && value.length() > 0) {
			this.pageSize = Integer.parseInt(value);
		} else {
			//pageSize = ;
		}

		param = "sortname";
		value = webRequest.getParameter(param);
		if (value != null && value.length() > 0) {
			this.sortname = cwPagination.esc4SortSql(value);
		}

		param = "sortorder";
		value = webRequest.getParameter(param);
		if (value != null && value.length() > 0) {
			this.sortorder = cwPagination.esc4SortSql(value);
		} else {
			this.sortorder = "asc";
		}
		
		// 输出查询参数
		Iterator<String> it = webRequest.getParameterNames();
		while (it.hasNext()) {
			param = it.next();
			value = webRequest.getParameter(param);

			this.params.put(param, value);

			logger.debug("param [{}] values [{}]", new Object[] { param, value });
		}
    }
    
    public int getPageNo() {  
       return pageNo;  
    }  
   
    public void setPageNo(int pageNo) {  
       this.pageNo = pageNo;  
    }  
   
    public int getPageSize() {  
       return pageSize;  
    }  
   
    public void setPageSize(int pageSize) {  
       this.pageSize = pageSize;  
    }  
   
    public int getTotalRecord() {  
       return totalRecord;  
    }  
   
    public void setTotalRecord(int totalRecord) {  
       this.totalRecord = totalRecord;  
       //在设置总页数的时候计算出对应的总页数，在下面的三目运算中加法拥有更高的优先级，所以最后可以不加括号。  
       int totalPage = totalRecord % pageSize == 0 ? totalRecord/pageSize : totalRecord/pageSize + 1;  
       this.setTotalPage(totalPage);  
    }  
   
    public int getTotalPage() {  
       return totalPage;  
    }  
   
    public void setTotalPage(int totalPage) {  
       this.totalPage = totalPage;  
    }  
   
    public List<T> getResults() {  
       return results;  
    }  
   
    public void setResults(List<T> results) {  
       this.results = results;  
    }  
     
    public Map<String, Object> getParams() {  
       return params;  
    }  
     
    public void setParams(Map<String, Object> params) {  
       this.params = params;  
    }  
   
    public String getSortname() {
		return cwPagination.esc4SortSql(sortname);
	}

	public void setSortname(String sortname) {
		this.sortname = cwPagination.esc4SortSql(sortname);
	}

	public String getSortorder() {
		return cwPagination.esc4SortSql(sortorder);
	}

	public void setSortorder(String sortorder) {
		this.sortorder = cwPagination.esc4SortSql(sortorder);
	}

	@Override  
    public String toString() {  
       StringBuilder builder = new StringBuilder();  
       builder.append("Page [pageNo=").append(pageNo).append(", pageSize=")  
              .append(pageSize).append(", results=").append(results).append(  
                     ", totalPage=").append(totalPage).append(  
                     ", totalRecord=").append(totalRecord).append("]");  
       return builder.toString();  
    }  
   
}  