package com.cwn.wizbank.ibatis.dialect;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * sql server方言
 * @author Administrator
 *
 */
public class SqlServerDialect implements Dialect {
	private static final Logger logger = LoggerFactory.getLogger(SqlServerDialect.class);

	public String getLimitString(String sql, int offset, int limit, String sortname, String sortorder) {
		if (offset < 0 || limit <= 0)
			return sql; // 数据不合法，不进行分页直接返回sql
		sql = sql.replaceAll("\\s+", " ").replaceAll("ORDER BY", "order by").trim(); //过滤掉重复空格以及两端的空格
		//sql = sql.toLowerCase();

		String orderBy = "order by";
		String orderStr = "";
		if (sql.indexOf(orderBy) > -1) {
			orderStr = sql.substring(sql.indexOf(orderBy), sql.length());
			sql = sql.replace(orderStr, ""); //截取掉sql中的order by
			orderStr = orderStr.replaceAll("\\s{1}([a-z|A-Z|_|0-9]+)\\.", " a."); //替换自查询中的临时表名
			logger.debug("[ order by : ]" + orderStr);
		}
		StringBuffer pageSql = new StringBuffer(sql.length() + 100);

		if (StringUtils.isNotEmpty(sortname)) {
			String sn[] = sortname.split("\\$");
			orderStr = "order by " + sn[0] + " " + sortorder;
		} else if (StringUtils.isEmpty(orderStr)) {
			orderStr = "order by CURRENT_TIMESTAMP desc";
		}

		pageSql.append("select * from(select a.*,row_number() over (" + orderStr + ") rownum from( ");
		pageSql.append(sql);
		pageSql.append(") a )b where rownum> " + offset + " and rownum <= " + (offset + limit));
		return pageSql.toString();
	}

	public boolean supportsLimit() {
		return true;
	}

	public static void main(String[] args) {
		Dialect dl = new SqlServerDialect();
		String sql = "select * from tableName order by ddd.account asc ";
		int offset = 30;
		int limit = 10;
		String sql2 = dl.getLimitString(sql, offset, limit, "", "");
		//System.out.println("sql2:" + sql2);
	}
}
