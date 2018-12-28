package com.cw.wizbank.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.Hashtable;

class DB2DbHelper extends DbHelper {

	/**
	 * Replace NULL value
	 */
	public String replaceNull(String colName, String value)  {
		return " coalesce(" + colName + "," + value + ")";
	}

	/**
	 * Get the database time
	 */
	public Timestamp getTime(Connection con) throws SQLException {
		Timestamp ts = null;
		String SQL = "values current timestamp";

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
			nullSQL = " nullif(0,0) ";
		} else if (type.equals(cwSQL.COL_TYPE_TIMESTAMP)) {
			nullSQL = " CAST(NULL AS timestamp) ";
		} else {
			nullSQL = " CAST(NULL AS VARCHAR(10)) ";
		}
		return nullSQL;
	}

	String genTempTableName(Connection con) throws SQLException {
	    String tableName = new String();
        Timestamp ts = getTime(con);
        int ns = ts.getNanos();
        tableName = "session.tmp";
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
		String tempSpace = "temp_space";

		StringBuffer SQL = new StringBuffer(0);
		SQL.append(" DECLARE GLOBAL TEMPORARY TABLE ").append(tableName).append(" ( ");
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
				dbType = "TIMESTAMP";
			} else if (columnType.equalsIgnoreCase(cwSQL.COL_TYPE_LONG)) {
				 dbType = "INT";
			} else if (columnType.equalsIgnoreCase(cwSQL.COL_TYPE_INTEGER)) {
				dbType = "SMALLINT";
			} else if (columnType.equalsIgnoreCase(cwSQL.COL_TYPE_FLOAT)) {
				dbType = "FLOAT(53)";
			} else if (columnType.equalsIgnoreCase(cwSQL.COL_TYPE_BOOLEAN)) {
				dbType = "CHARACTER(1)";
			} else if (columnType.equalsIgnoreCase(cwSQL.COL_TYPE_STRING)) {
				dbType = "VARGRAPHIC(" + columnLen + ")";
			}
			SQL.append(dbType);
		}
		SQL.append(") IN " + tempSpace + " ");

		con.createStatement().execute(SQL.toString());

		return true;
	}

	/**
	 * Drop a temporary table
	 */
	public boolean dropTempTable(Connection con, String tableName) throws SQLException {
		StringBuffer SQL = new StringBuffer();
		StringBuffer SQL1 = new StringBuffer();
		SQL1.append("delete from ").append(tableName);
		SQL.append("DROP TABLE ").append(tableName);

		PreparedStatement stmt = null;
		stmt = con.prepareStatement(SQL1.toString());
		stmt.executeUpdate();
		stmt = con.prepareStatement(SQL.toString());
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
	public String getDbLenFunPattern(Connection con, String colName)  {
		return "LENGTH(" + colName + ")";
	}

	/**
	 * Convert the int type to String for the sql
	 * 
	 * @param field
	 *            is the table field
	 */
	public String convertInt2String(String field)  {
		return " cast(" + field + " as char) ";
	}

	public String getDayUnit() {
		return "days";
	}

	public long getAutoId(Connection con, PreparedStatement stmt, String tableName, String columnName) throws SQLException {
		long id = 0;
		ResultSet rs = stmt.getGeneratedKeys();
		if (rs.next()) {
			id = rs.getLong(1);
		}
		rs.close();
		if (id <= 0) {
			throw new SQLException("Cannot get the auto id of " + columnName + " from " + tableName);
		}
		return id;
	}

	public String dateadd(String field, String amount)  {
		String result = ((field == null || field.equals("")) ? "current timestamp" : field) + " + "
				+ ((amount == null || amount.equals("")) ? "0" : amount) + " days";
		return result;
	}

	public String datediff(String field1, String field2)  {
		String result = "days(" + ((field1 == null || field1.equals("")) ? "current timestamp" : field1) + ") - " + "days("
				+ ((field2 == null || field2.equals("")) ? "current timestamp" : field2) + ")";
		return result;
	}

	public String subFieldLocation(String main_field, String sub_field, boolean int_type) {
		StringBuffer result = new StringBuffer(128);
		String converted_sub_field = sub_field;
		if (int_type) {
			converted_sub_field = convertInt2String(sub_field);
		}
		result.append(" locate(").append(converted_sub_field).append(", ").append(main_field).append(") <> 0 ");
		return result.toString();
	}

	/**
	 * 针对不同的数据库使用不同的日期函数，以字符串的形式返回
	 * 
	 * @return 以字符串的形式返回不同数据库的日期函数
	 * @throws SQLException
	 */
	public String getDate() {
		return " current timestamp ";
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
		String result = ((field == null || field.equals("")) ? "current timestamp" : field) + " + "
				+ ((amount == null || amount.equals("")) ? "0" : amount) + " month";
		return result;
	}

	public String getDatabaseSort() {
		// TODO Auto-generated method stub
		return DB2;
	}
}
