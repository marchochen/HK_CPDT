package com.cw.wizbank.JsonMod;


import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;

import com.cw.wizbank.util.cwPagination;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.qdb.qdbEnv;

public class BaseParam {
	public static int LIMIT_NUM_TEN=10;
	private int total_rec;
	private String sort;
	private String dir;
	private int start=0;
	private int limit=LIMIT_NUM_TEN;
	
	private String cmd;
	private String developer_id;//API接口类别ID
	private String token;//API接口标示
	private String method;//API接口返回数据格式:JSON&XML
	private Pagination page;
	private cwPagination cwPage;
	private String url_success;
	private String url_failure;
	
	  /**
    encoding of the client : request.getCharacterEncoding()
    */
    protected String clientEnc;
    /**
    encoding of the web site
    */
    protected String encoding;
    
    private qdbEnv static_env;
    
    //  for xsl 分页
	private String sortCol;
	private String sortOrder;
	private int cur_page;
	private int page_size;
	private String sort_order;
	private String sort_col;
	
	private String stylesheet;
	
	private String goldenman_param;

	
	protected boolean bMultiPart;
    
	private Timestamp cur_time;
	
	public boolean isBMultiPart() {
		return bMultiPart;
	}
	public void setBMultiPart(boolean multiPart) {
		bMultiPart = multiPart;
	}
	public String getStylesheet() {
		return stylesheet;
	}
	public void setStylesheet(String stylesheet) {
		this.stylesheet = stylesheet;
	}
	public int getCur_page() {
		return cur_page;
	}
	public void setCur_page(int cur_page) {
		this.cur_page = cur_page;
	}
	public int getPage_size() {
		return page_size;
	}
	public void setPage_size(int page_size) {
		this.page_size = page_size;
	}
	public String getSortCol() {
		return sortCol;
	}
	public void setSortCol(String sortCol) {
		this.sortCol = cwPagination.esc4SortSql(sortCol);
	}
	public String getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(String sortOrder) {
		this.sortOrder = cwPagination.esc4SortSql(sortOrder);
	}
	public String getClientEnc() {
		return clientEnc;
	}
	public void setClientEnc(String clientEnc) {
		this.clientEnc = clientEnc;
	}
	public String getEncoding() {
		return encoding;
	}
	public void setEncoding(String encoding) {
		this.encoding = encoding;
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
	
	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}
	
	public void common() {
		page = new Pagination(getTotal_rec(),getStart(), getLimit(), getSort(), getDir());
	}
	public String getCmd() {
		return cmd;
	}
	public void setCmd(String cmd) {
		this.cmd = cmd;
	}
	public Pagination getPage() {
		return page;
	}
	public void setPage(Pagination page) {
		this.page = page;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public String getUrl_failure() {
		return url_failure;
	}

	public void setUrl_failure(String url_failure) {
		this.url_failure = cwUtils.esc4JS(cwUtils.getUrlByisPhishing(url_failure),true);
	}

	public String getUrl_success() {
		return cwUtils.esc4JS(cwUtils.getUrlByisPhishing(url_success),true);
	}

	public void setUrl_success(String url_success) {
		this.url_success = url_success;
	}
	
	public cwPagination getCwPage() {
		if (cwPage == null) {
			cwPage = new cwPagination();
			cwPage.curPage = (this.cur_page == 0) ? 1 : this.cur_page;
			cwPage.pageSize = (this.page_size == 0) ? cwPagination.defaultPageSize : this.page_size;
			cwPage.sortCol = cwPagination.esc4SortSql(this.sort_col);
			cwPage.sortOrder = cwPagination.esc4SortSql(this.sort_order);
		}
		return cwPage;
	}
	
	public void setCwPage(cwPagination cwPage) {
		this.cwPage = cwPage;
	}

	public String getSort_order() {
		return sort_order;
	}

	public void setSort_order(String sort_order) {
		this.sort_order = sort_order;
	}

	public String getSort_col() {
		return sort_col;
	}

	public void setSort_col(String sort_col) {
		this.sort_col = sort_col;
	}
	
    public qdbEnv getStatic_env() {
        return static_env;
    }
    
    public void setStatic_env(qdbEnv static_env) {
        this.static_env = static_env;
    }
	public String getGoldenman_param() {
		return goldenman_param;
	}
	public void setGoldenman_param(String goldenman_param) {
		this.goldenman_param = goldenman_param;
	}
	
	public Timestamp getCur_time() {
		return cur_time;
	}
	public void setCur_time(Timestamp cur_time) {
		this.cur_time = cur_time;
	}
	public void setDeveloper_id(String developer_id) {
		this.developer_id = developer_id;
	}
	public String getDeveloper_id() {
		return developer_id;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getToken() {
		return token;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getMethod() {
		return method;
	}
	
}
