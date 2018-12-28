package com.cw.wizbank.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.cwn.wizbank.utils.CommonLog;

class MSSQLDbHelper extends DbHelper {
	
	public String replaceNull(String colName, String value) {
		return " isnull(" + colName + "," + value + ")";
	}

	/**
	 * Get the database time
	 */
	public Timestamp getTime(Connection con) throws SQLException {
		Timestamp ts = null;
		String SQL = "select t = getdate()";

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
		return " null ";
	}

    String genTempTableName(Connection con) throws SQLException {
        String tableName = new String();
        Timestamp ts = getTime(con);
        int ns = ts.getNanos();
        tableName = "#tmp";
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
		// System.out.println("tableName = "+ tableName);
		createSimpleTemptable(con, tableName, columnName, columnType, columnLength);
		return (tableName);
	}

	/**
	 * Create a single column temporary table with a given name
	 * 
	 * @return name of the temprorary table
	 */
	public boolean createSimpleTemptable(Connection con, String tableName, String columnName, String columnType, int columnLength)
			throws SQLException {
		Hashtable nameType = new Hashtable();
		Hashtable nameLength = new Hashtable();

		nameType.put(columnName, columnType);
		nameLength.put(columnName, Integer.toString(columnLength));

		return createTempTable(con, tableName, nameType, nameLength);

	}

	/**
	 * Create multi column temporary table with a given name
	 * 
	 * @return true if success <BR>
	 *         false otherwise
	 */
	public boolean createTempTable(Connection con, String tableName, Hashtable nameType, Hashtable nameLength) throws SQLException {
		StringBuffer SQL = new StringBuffer(0);
		SQL.append(" CREATE TABLE ").append(tableName).append(" ( ");
		Enumeration keys = nameType.keys();
		String index_nm = "";
		int cnt = 0;
		while (keys.hasMoreElements()) {
			cnt++;
			String columnName = (String) keys.nextElement();
			if(cnt==1){
				index_nm = columnName;
			}
			String columnType = (String) nameType.get(columnName);
			String columnLen = (String) nameLength.get(columnName);
			if (columnLen == null || columnLen.length() == 0)
				columnLen = cwSQL.DEFAULT_COLUMN_LENGTH; // Deafault lenght;

			String dbType = new String();
			if (cnt > 1)
				SQL.append(" , ");
			SQL.append(columnName).append(" ");
			if (columnType.equalsIgnoreCase(cwSQL.COL_TYPE_TIMESTAMP)) {
				dbType = "DATETIME";
			} else if (columnType.equalsIgnoreCase(cwSQL.COL_TYPE_LONG)) {
				dbType = "INT";
			} else if (columnType.equalsIgnoreCase(cwSQL.COL_TYPE_INTEGER)) {
				dbType = "SMALLINT";
			} else if (columnType.equalsIgnoreCase(cwSQL.COL_TYPE_FLOAT)) {
				dbType = "FLOAT(53)";
			} else if (columnType.equalsIgnoreCase(cwSQL.COL_TYPE_BOOLEAN)) {
				dbType = "BIT";
			} else if (columnType.equalsIgnoreCase(cwSQL.COL_TYPE_STRING)) {
				dbType = "NVARCHAR(" + columnLen + ") collate Chinese_Taiwan_Stroke_CI_AS";
			}
			SQL.append(dbType);
		}
		SQL.append(")");
		con.createStatement().execute(SQL.toString());
		String create_index = "CREATE INDEX IX_"+tableName+" ON "+tableName+"("+index_nm+")";
		CommonLog.debug("temptable index sql: "+create_index);
		con.createStatement().execute(create_index);
		return true;
	}

	public String[] getSQLClause(Connection con, String colName, String columnType, List data, int columnLength) throws SQLException {
		String[] s = new String[2];
		StringBuffer sb = new StringBuffer(" ( select ").append(colName).append(" from ");
		String tableName = createSimpleTemptable(con, colName, cwSQL.COL_TYPE_LONG, 0);
		insertSimpleTempTable(con, tableName, (Vector) data, cwSQL.COL_TYPE_LONG);
		sb.append(tableName).append(" ) ");
		s[0] = sb.toString();
		s[1] = tableName;
		return s;
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
		return "LEN(" + colName + ")";
	}

	/**
	 * Get the corresponding concat operator for the sql
	 */
	public String getConcatOperator() {
		return " + ";
	}

	/**
	 * Convert the int type to String for the sql
	 * 
	 * @param field
	 *            is the table field
	 */
	public String convertInt2String(String field) {
		return " cast(" + field + " as varchar) ";
	}

	public String getDayUnit() {
		return "";
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

	public String dateadd(String field, String amount) {
		String result = " dateadd(day, " + ((amount == null || amount.equals("")) ? "0" : amount) + ", "
				+ ((field == null || field.equals("")) ? " getDate() " : field) + ")";
		return result;
	}

	public String datediff(String field1, String field2) {
		String result = " datediff(day, " + ((field1 == null || field1.equals("")) ? "getDate()" : field1) + ", "
				+ ((field2 == null || field2.equals("")) ? "getDate()" : field2) + ")";
		return result;
	}

	public String subFieldLocation(String main_field, String sub_field, boolean int_type) {
		StringBuffer result = new StringBuffer(128);

		String converted_sub_field = sub_field;
		if (int_type) {
			converted_sub_field = convertInt2String(sub_field);
		}
		result.append(" charindex(").append(converted_sub_field).append(", ").append(main_field).append(") > 0 ");
		return result.toString();
	}

	/**
	 * 针对不同的数据库使用不同的日期函数，以字符串的形式返回
	 * 
	 * @return 以字符串的形式返回不同数据库的日期函数
	 * @throws SQLException
	 */
	public String getDate() {
		return " getdate() ";
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
		String result = " dateadd(month, " + ((amount == null || amount.equals("")) ? "0" : amount) + ", "
					+ ((field == null || field.equals("")) ? " getDate() " : field) + ") ";
		return result;
	}

	public String getDatabaseSort() {
		// TODO Auto-generated method stub
		return MSSQLDB;
	}
}
