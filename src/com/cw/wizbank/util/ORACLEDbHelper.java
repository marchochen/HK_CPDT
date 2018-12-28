package com.cw.wizbank.util;

import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.Hashtable;

class ORACLEDbHelper extends DbHelper {
	/**
	 * Replace NULL value
	 */
	public String replaceNull(String colName, String value) {
		return " nvl(" + colName + "," + value + ")";
	}

	/**
	 * Get the database time
	 */
	public Timestamp getTime(Connection con) throws SQLException {
		String SQL = null;
		Timestamp ts = null;
		SQL = "select systimestamp from dual";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(SQL);

		if (rs.next()) {
			ts = rs.getTimestamp(1);
		}
		rs.close();
		stmt.close();
		if (ts == null) {
			throw new SQLException("Failed to get current time.");
		}
		return ts;
	}

	/**
	 * Get database null string
	 */
	public String get_null_sql(String type) {
		String nullSQL = new String();
		if (type.equals(cwSQL.COL_TYPE_INTEGER) || type.equals(cwSQL.COL_TYPE_LONG) || type.equals(cwSQL.COL_TYPE_FLOAT) 
				|| type.equals(cwSQL.COL_TYPE_DECIMAL)) {
			nullSQL = " null+0 ";
		} else if (type.equals(cwSQL.COL_TYPE_TIMESTAMP)) {
			nullSQL = " null+to_date(0) ";
		} else {
			nullSQL = " null ";
		}
		return nullSQL;
	}

	/**
	 * Get SQL right outer join string
	 */
	public String get_right_join() {
		return " (+) = ";
	}

	/**
	 * Updates clob columns of a table using database vendor specific methods
	 * 
	 * @author kawai
	 * @version 2002.04.25
	 * @param con
	 *            connection of the database to be updated
	 * @param tableName
	 *            name of the table to be updated
	 * @param columnName
	 *            name of the column(s) to be updated
	 * @param columnValue
	 *            value of the column(s) to be updated
	 * @param condition
	 *            the WHERE clause string of the records to be updated, null if
	 *            no WHERE clause
	 * @return either the number of rows updated; or 0 when no rows are updated
	 * @exception SQLException
	 *                if any error occurs during any sql operations
	 */
	public int updateClobFields(Connection con, String tableName, String columnName[], String columnValue[], String condition)
			throws SQLException {
		int rc = 0;

		StringBuffer updsql = new StringBuffer();
		PreparedStatement clobStmt = null;

		// empty the column to be updated
		updsql.setLength(0);
		updsql.append("UPDATE ").append(tableName).append(" SET ");
		for (int i = 0; i < columnName.length; i++) {
			if (columnName[i] == null || columnName[i].length() == 0) {
				throw new SQLException("Invalid Clob column name");
			}
			updsql.append(columnName[i]).append(" = ");
			if (columnValue[i] == null) {
				updsql.append(get_null_sql(cwSQL.COL_TYPE_STRING));
			} else {
				updsql.append(getClobNull());
			}
			if (i < (columnName.length - 1)) {
				updsql.append(" , ");
			}
		}
		if (condition != null) {
			updsql.append(" WHERE ").append(condition);
		}
		clobStmt = con.prepareStatement(updsql.toString());
		rc = clobStmt.executeUpdate();
		clobStmt.close();

		// get the locator for updating the value
		updsql.setLength(0);
		updsql.append("SELECT ");
		for (int i = 0; i < columnName.length; i++) {
			updsql.append(columnName[i]);
			if (i < (columnName.length - 1)) {
				updsql.append(" , ");
			}
		}
		updsql.append(" FROM ").append(tableName);
		if (condition != null) {
			updsql.append(" WHERE ").append(condition);
		}
		updsql.append(" for update ");
		clobStmt = con.prepareStatement(updsql.toString());
		ResultSet rs = clobStmt.executeQuery();
		// for each record retrieved, update each column with the supplied
		// value
		while (rs.next()) {
			for (int i = 0; i < columnName.length; i++) {
				if (columnValue[i] != null) {
					Clob clobLocator = rs.getClob(i + 1);
					clobLocator.setString(1, columnValue[i]);
				}
			}
		}
		rs.close();
		clobStmt.close();

		return rc;
	}

	/**
	 * Get the string which can insert a null to a database column
	 */
	public String getClobNull() {
		return "EMPTY_CLOB()";
	}

	String genTempTableName(Connection con) throws SQLException {
        String tableName = new String();
        Timestamp ts = getTime(con);
        int ns = ts.getNanos();
        tableName = "tmp";
        tableName += (Math.round(Math.random() * 1000000) + ns);
        return tableName;
    }
	   
	/**
	 * Create a single column temporary table with a system generated table name
	 * 
	 * @return name of the temprorary table
	 */
	public String createSimpleTemptable(Connection con, String columnName, String columnType, int columnLength) throws SQLException {
		String tableName = genTempTableName(con);
		createSimpleTemptable(con, tableName, columnName, columnType, columnLength);
		return tableName;

	}

	/**
	 * Create multi column temporary table with a given name
	 * 
	 * @return true if success <BR>
	 *         false otherwise
	 */
	public boolean createTempTable(Connection con, String tableName, Hashtable nameType, Hashtable nameLength) throws SQLException {

		StringBuffer SQL = new StringBuffer(0);
		SQL.append(" CREATE GLOBAL TEMPORARY TABLE ").append(tableName).append(" ( ");
		Enumeration keys = nameType.keys();
		int cnt = 0;
		while (keys.hasMoreElements()) {
			cnt++;
			String columnName = (String) keys.nextElement();
			String columnType = (String) nameType.get(columnName);
			String columnLen = (String) nameLength.get(columnName);
			if (columnLen == null || columnLen.length() == 0)
				columnLen = cwSQL.DEFAULT_COLUMN_LENGTH; // Deafault lenght;

			String dbType = new String();
			if (cnt > 1)
				SQL.append(" , ");
			SQL.append(columnName).append(" ");
			if (columnType.equalsIgnoreCase(cwSQL.COL_TYPE_TIMESTAMP)) {
				dbType = "DATE";
			} else if (columnType.equalsIgnoreCase(cwSQL.COL_TYPE_LONG)) {
				dbType = "NUMBER(10)";
			} else if (columnType.equalsIgnoreCase(cwSQL.COL_TYPE_INTEGER)) {
				dbType = "NUMBER(5)";
			} else if (columnType.equalsIgnoreCase(cwSQL.COL_TYPE_FLOAT)) {
				dbType = "FLOAT";
			} else if (columnType.equalsIgnoreCase(cwSQL.COL_TYPE_BOOLEAN)) {
				dbType = "NUMBER(1)";
			} else if (columnType.equalsIgnoreCase(cwSQL.COL_TYPE_STRING)) {
				dbType = "VARCHAR2(" + columnLen + ")";
			}
			SQL.append(dbType);
		}
		SQL.append(") ON COMMIT PRESERVE ROWS ");

		PreparedStatement stmt = con.prepareStatement(SQL.toString());
		stmt.executeUpdate();
		stmt.close();
		return true;
	}

	/**
	 * Get the len() function pattern of different database
	 * 
	 * @param con
	 *            is the dbConnection
	 * @param colName
	 *            is the param to the len() function to ocunt the length of the
	 *            column
	 * @return the meta len() function pattern
	 * @throws SQLException
	 */
	public String getDbLenFunPattern(Connection con, String colName) {
		return "LENGTH(" + colName + ")";
	}

	/**
	 * Convert the int type to String for the sql
	 * 
	 * @param field
	 *            is the table field
	 */
	public String convertInt2String(String field) {
		return field;
	}

	public String getDayUnit() {
		return "";
	}

	public long getAutoId(Connection con, PreparedStatement stmt, String tableName, String columnName) throws SQLException {
		long id = 0;
		ResultSet rs = stmt.getGeneratedKeys();
		String rowid = null;
		if (rs.next()) {
			rowid = rs.getString(1);
		}
		PreparedStatement stmt_ora = con.prepareStatement("SELECT " + columnName + " FROM " + tableName + " WHERE rowid = ? ");
		stmt_ora.setString(1, rowid);
		ResultSet rs_ora = stmt_ora.executeQuery();
		if (rs_ora.next()) {
			id = rs_ora.getLong(1);
		}
		rs_ora.close();
		stmt_ora.close();
		if (id <= 0) {
			throw new SQLException("Cannot get the auto id of " + columnName + " from " + tableName);
		}
		return id;
	}

	public String dateadd(String field, String amount) {
		String result = ((field == null || field.equals("")) ? " sysdate" : " cast(" + field + " as date)") + " + "
				+ ((amount == null || amount.equals("")) ? "0" : amount);
		return result;
	}

	public String datediff(String field1, String field2) {
		String result = " trunc(" + ((field2 == null || field2.equals("")) ? "sysdate)" : "cast( " + field2 + " as date))");
			result += " - " +  ((field1 == null || field1.equals("")) ? "sysdate" : "cast( " + field1 + " as date)");
		return result;
	}

	/**
	 * 取二个时间中的最小值，此值将会出现在定义的别名列中
	 * 
	 * @param dbMajorVersion
	 * @param field1:
	 *            第一个时间
	 * @param field2:
	 *            第二个时间
	 * @param alias:
	 *            最后生成字段的别名
	 * @return
	 */
	public String minDate(String field1, String field2, String alias, int dbMajorVersion)  {
		String result = " case " + " when " + field2 + " is null or " + field1 + " < " + field2 + " then " + field1 + " else " + field2 + " end "
				+ alias + " ";
		// case...when...end语法只是在oracle(8i)中不能使用,在oracle(9i)中就已经可以正常调用了
		if (dbMajorVersion < 9) {
			result = " decode(sign(cast(" + field2 + " as date) - cast(" + field1 + " as date)),1," + field2 + ",-1," + field1 + "," + field1 + ") "
					+ alias + " ";
		}
		return result;
	}

	public String subFieldLocation(String main_field, String sub_field, boolean int_type)  {
		StringBuffer result = new StringBuffer(128);
		String converted_sub_field = sub_field;
		if (int_type) {
			converted_sub_field = convertInt2String(sub_field);
		}
		result.append(" instr(").append(main_field).append(", ").append(converted_sub_field).append(") > 0 ");
		return result.toString();
	}

	/**
	 * 针对不同的数据库使用不同的日期函数，以字符串的形式返回
	 * 
	 * @return 以字符串的形式返回不同数据库的日期函数
	 * @throws SQLException
	 */
	public String getDate() {
		return " sysdate ";
	}

	/**
	 * 针对不同的数据库使用不同的月份日期函数，以字符串的形式返回
	 * 
	 * @param field
	 *            日期字段(字符串形式)
	 * @param amount
	 *            月数(字符串形式)
	 * @return 以字符串的形式返回不同数据库的月份日期函数
	 * @throws SQLException
	 */
	public String addMonth(String field, String amount) {
		String result = " add_months(" + (field == null || field.equals("") ? " sysdate " : field) + ", "
				+ (amount == null || amount.equals("") ? "0" : amount) + ") ";
		return result;
	}

	/**
	 * 针对不同的数据库使用日期函数，以字符串的形式返回整条SQL语句
	 * 
	 * @param dateFuncStr
	 *            日期函数的字符串形式(例如："getDate()")
	 * @return 以字符串的形式返回不同数据库的日期函数的整条SQL语句
	 */
	public String getDateSql(String dateFuncStr) {
		return "select " + dateFuncStr + " from dual ";
	}

	/**
	 * 特定数据库中某个日期的年份（或月份、或天数）函数
	 * 
	 * @param field
	 *            字段名
	 * @param part
	 *            指定数据库中具体的日期函数（可选值cwSQL.YEAR、cwSQL.MONTH、cwSQL.DAY）
	 * @return 特定数据库的日期函数
	 * @throws SQLException
	 */
	public String getPartOfDate(String field, int part)  {
		if (field == null) {
			field = getDate();
		}
		String result = null;
		if (part == cwSQL.YEAR) {
			result = " to_char(" + field + ", 'YYYY') ";
		}

		if (part == cwSQL.MONTH) {
			result = " to_char(" + field + ", 'MM') ";
		}

		if (part == cwSQL.DAY) {
			result = " to_char(" + field + ", 'DD') ";
		}
		return result;
	}

	public String getDatabaseSort() {
		// TODO Auto-generated method stub
		return ORACLE;
	}
}
