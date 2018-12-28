package com.cw.wizbank.dao;

/*
 * 该工厂用户创建SqlMapClientImpl类的实例
 * @author:wrren
 * 
 */

import com.cw.wizbank.dao.impl.SqlMapClientImpl;
import com.cw.wizbank.dao.pagination.ProcessPagination;

public class SQLMapClientFactory {
	private static final String CUR_PAGE = "cur_page";
	private static final String PAGE_SIZE = "page_size";
	private static final String SORTCOL = "sort_col";
	private static final String SORTORDER = "sort_order";
	private static boolean showSql = false;
	private static SqlMapClientImpl sqlMapClientImpl = null;

	public static final SqlMapClientImpl getSqlMapClient() {
		if (sqlMapClientImpl == null) {
			// 设置分页信息
			ProcessPagination processPagination = new ProcessPagination();
			processPagination.setDefaultOneTimeCount(10);// 设置默认一页显示10条记录
			processPagination.setNextPagePro(CUR_PAGE);// 设置request中的换页参数
			processPagination.setOneTimeCountPro(PAGE_SIZE);// 设置request中指定一页显示记录的数量属性
			processPagination.setOrderByColumnPro(SORTCOL);// 设置request中的排序字段的属性
			processPagination.setPernumationWayPro(SORTORDER);// 设置request中的排序方式的 属性
			sqlMapClientImpl = new SqlMapClientImpl(processPagination);
			sqlMapClientImpl.setShowSql(showSql);
		}
		return sqlMapClientImpl;
	}

}
