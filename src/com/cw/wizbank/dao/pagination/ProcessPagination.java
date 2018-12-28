package com.cw.wizbank.dao.pagination;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/*
 * 分页处理的参数信息
 */
public class ProcessPagination{
	private long defaultOneTimeCount = 5;//一页显示的记录数量delaut value （null and 参数转换异常）
	private long defaultOnePageCount = 10;//页面导航的数量 delaut value （null and 参数转换异常）
	private String oneTimeCountPro = "onePageDispalyCount";//一页显示的记录数量 的 request and param  的属性
	private String disPageCountPro ="disPageCount";//显示的页面导航的数量 的 request and prams  的属性
	private String nextPagePro = "nextPage";// 换页参数 requet and paramr  的属性
	private String orderByColumnPro = "orderByColumn"; //request 中的排序字段参数的 属性
	private String pernumationWayPro = "pernumationWay";//request 中的排序方式参数的 属性
	private String nowPageInSessionPro = "nowPageNumberInHttpSession"; //session 中的当前页面 的属性

	public void setDefaultOneTimeCount(long defaultOneTimeCount) {
		this.defaultOneTimeCount = defaultOneTimeCount;
	}
	
	public long getDefaultOneTimeCount() {
		return defaultOneTimeCount;
	}
	
	public void setDefaultOnePageCount(long defaultOnePageCount) {
		this.defaultOnePageCount = defaultOnePageCount;
	}
	
	public long getDefaultOnePageCount() {
		return defaultOnePageCount;
	}

	public String getOneTimeCountPro() {
		return oneTimeCountPro;
	}

	public void setOneTimeCountPro(String oneTimeCountPro) {
		this.oneTimeCountPro = oneTimeCountPro;
	}

	public String getDisPageCountPro() {
		return disPageCountPro;
	}

	public void setDisPageCountPro(String disPageCountPro) {
		this.disPageCountPro = disPageCountPro;
	}

	public String getNextPagePro() {
		return nextPagePro;
	}

	public void setNextPagePro(String nextPagePro) {
		this.nextPagePro = nextPagePro;
	}

	public String getOrderByColumnPro() {
		return orderByColumnPro;
	}

	public void setOrderByColumnPro(String orderByColumnPro) {
		this.orderByColumnPro = orderByColumnPro;
	}

	public String getPernumationWayPro() {
		return pernumationWayPro;
	}

	public void setPernumationWayPro(String pernumationWayPro) {
		this.pernumationWayPro = pernumationWayPro;
	}

	public String getNowPageInSessionPro() {
		return nowPageInSessionPro;
	}

	public void setNowPageInSessionPro(String nowPageInSessionPro) {
		this.nowPageInSessionPro = nowPageInSessionPro;
	}

	//得到请求的url地址
	public String getURl(HttpServletRequest request){
		String url = request.getRequestURL().toString();
		int doWay = url.indexOf("?");
		
		String serverUrl = null;
		if(doWay != -1){
			serverUrl = url.substring(0,doWay);
		}else{
			serverUrl = url;
		}
		
		StringBuffer stringBuffer = new StringBuffer("?");
		List paramServerUrl = new ArrayList();
		Enumeration enumRequestNames = request.getParameterNames();
		while(enumRequestNames.hasMoreElements()){
			String paramName = (String)enumRequestNames.nextElement();
			if(!paramName.equals(nextPagePro)){
				paramServerUrl.add(paramName);
			}
		}
		Collections.sort(paramServerUrl);
		
		for(int i=0;i<paramServerUrl.size();i++){
			stringBuffer.append(paramServerUrl.get(i)).append("=").append(request.getParameter((String)paramServerUrl.get(i)));
			if((i+1) < paramServerUrl.size()){
				stringBuffer.append("&");
			}
		}
		
		if(!paramServerUrl.isEmpty()){
			serverUrl += stringBuffer.toString();
		}
		
		return serverUrl;
	}
	
	//获取session中的当前页面 default为第一页 （null） 
	public long getNowPage(HttpServletRequest request){
		HttpSession session = request.getSession();
		NowPage nowPage = (NowPage)session.getAttribute(nowPageInSessionPro);
		if(nowPage == null || !nowPage.getUrl().equals(getURl(request))){
			session.setAttribute(nowPageInSessionPro, new NowPage(getURl(request),1));
			return 1;
		}else {
            return nowPage.getNowPath();
		}
		
	}

}
