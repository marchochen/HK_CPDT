package com.cwn.wizbank.ibatis.dialect;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * oracle方言
 */
public class OracleDialect implements Dialect {
	private static final Logger logger = LoggerFactory.getLogger(SqlServerDialect.class);

	public String getLimitString(String sql, int offset, int limit, String sortname, String sortorder) {
		sql = sql.replaceAll("\\s+", " ").replaceAll("ORDER BY", "order by").trim(); //过滤掉重复空格以及两端的空格
		boolean isForUpdate = false;
		if (sql.toLowerCase().endsWith(" for update")) {
			sql = sql.substring(0, sql.length() - 11);
			isForUpdate = true;
		}
		//sql = sql.toLowerCase();

		String orderBy = "order by";
		String orderStr = "";
		if (sql.indexOf(orderBy) > -1) {
			orderStr = sql.substring(sql.indexOf(orderBy), sql.length());
			sql = sql.replace(orderStr, ""); //截取掉sql中的order by
			//orderStr = orderStr.replaceAll("\\s{1}([a-z|A-Z|_|0-9]+)\\."," ");	//替换自查询中的临时表名
			logger.debug("[ order by : ]" + orderStr);
		}
		if (StringUtils.isNotEmpty(sortname)) {
			String sn[] = sortname.split("\\$");
			String orderString = "";
            int index = 0;
            for (String odStr : sn) {
                //对于别名中是“count(app_id) as "APP.app_cnt_int"”的，oraclesql中会失效，所以要取到定别名的字段count(app_id)
                if (odStr.indexOf(".") > 0) {
                    int len = sql.indexOf(odStr);
                    if (len > 0) {
                        String formName = sql.substring(0, len);
                        formName = formName.substring(formName.lastIndexOf(",") + 1, formName.length());
                        formName = formName.replaceAll("\\s+", " ").replaceAll("AS|as", "").trim();
                        //过滤掉重复空格以及两端的空格
                        orderString += formName;
                    }
                } else {
                    orderString += odStr;
                }
                if (index != sn.length - 1) {
                    orderString += ",";
                }
                index++;
            }
			orderStr = " order by " + orderString+ " " + sortorder;
		} else if (StringUtils.isEmpty(orderStr)) {
			//orderStr = "order by CURRENT_TIMESTAMP desc";
		}

		StringBuffer pageSql = new StringBuffer(sql.length() + 100);
		pageSql.append("select * from ( select row_.*, rownum rownum_ from ( ");
		pageSql.append(sql).append(" ").append(orderStr);
		pageSql.append(" ) row_ ) where rownum_ > " + offset + " and rownum_ <= " + (offset + limit));
		if (isForUpdate) {
			pageSql.append(" for update");
		}
		return pageSql.toString();
	}

	public boolean supportsLimit() {
		return true;
	}
}
