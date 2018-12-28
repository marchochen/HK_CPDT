package com.cwn.wizbank.ibatis.dialect;


import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * mysql方言
 * @author Administrator
 */
public class MysqlDialect implements Dialect {
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
            //orderStr = orderStr.replaceAll("\\s{1}([a-z|A-Z|_|0-9]+)\\.", " a."); //替换自查询中的临时表名
            logger.debug("[ order by : ]" + orderStr);
        }

        if (StringUtils.isNotEmpty(sortname)) {
            String sn[] = sortname.split("\\$");
            String orderString = "";
            String odStr = sn[0];
                //对于别名中是“count(app_id) as "APP.app_cnt_int"”的，mysql中会失效，所以要取到定别名的字段count(app_id)
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
            orderStr = "order by " + orderString + " " + sortorder;
        }

        StringBuffer sb = new StringBuffer(sql.length() + 80);
        sb.append(sql).append(" ").append(orderStr).append(" limit ").append(offset).append(" , ").append(limit);
        return sb.toString();
    }

    public boolean supportsLimit() {
        return true;
    }
}
