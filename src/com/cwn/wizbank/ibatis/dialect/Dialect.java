package com.cwn.wizbank.ibatis.dialect;

public interface Dialect {

	public static enum Type {
		MYSQL,
		SQLSERVER,
		ORACLE,
		DB2
	}
	/**
	 * 是否支持数据库分页，true支持
	 */
	public boolean supportsLimit();
	/**
	 * 返回分页的sql
	 * 
	 * @param sql
	 *            查询的sql
	 * @param offset
	 *            开始记录，从1开始
	 * @param limit
	 *            记录数
	 * @return
	 */
	public String getLimitString(String sql, int offset, int limit, String sortname, String sortorder);
}