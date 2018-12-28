package com.cw.wizbank.util;

import java.io.StringReader;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

public abstract class DbHelper {
	public static final String DB2 = "DB2";
	public static final String ORACLE = "ORACLE";
	public static final String MSSQLDB = "MSSQLDB";
	public static final String MYSQL = "MYSQL";
	
	/**
	 * Replace NULL value
	 */
	public abstract String replaceNull(String colName, String value);

	/**
	 * Get the database time
	 * @throws SQLException 
	 */
	public abstract Timestamp getTime(Connection con) throws SQLException;

	/**
	 * Get database null string
	 */
	public abstract String get_null_sql(String type);

	/**
	 * Get SQL right outer join string
	 */
	public String get_right_join() {
		return " =* ";
	}

	/**
	 * Get the value of a database column which length is larger than 4000
	 */
	public String getClobValue(ResultSet rs, String colName) throws SQLException {
		String val = null;
		Clob lob_loc = rs.getClob(colName);
		if (lob_loc != null) {
			val = lob_loc.getSubString(1, (int) lob_loc.length());
		}
		return val;
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
	public int updateClobFields(Connection con, String tableName, String columnName[], String columnValue[], String condition) throws SQLException {
		int rc = 0;

		StringBuffer SQLBuf = new StringBuffer();
		PreparedStatement stmt = null;

		SQLBuf.append("UPDATE ").append(tableName).append(" SET ");
		for (int i = 0; i < columnName.length; i++) {
			if (columnName[i] == null || columnName[i].length() == 0) {
				throw new SQLException("Invalid Clob column name");
			}
			SQLBuf.append(columnName[i]).append(" = ? ");
			if (i < (columnName.length - 1)) {
				SQLBuf.append(" , ");
			}
		}
		if (condition != null) {
			SQLBuf.append(" WHERE ").append(condition);
		}
		stmt = con.prepareStatement(SQLBuf.toString());
		for (int i = 0; i < columnName.length; i++) {
			if (columnValue[i] != null) {
				StringReader srVal = new StringReader(columnValue[i]);
				stmt.setCharacterStream((i + 1), srVal, columnValue[i].length());
			} else {
				stmt.setNull((i + 1), java.sql.Types.VARCHAR);
			}
		}
		rc = stmt.executeUpdate();
		stmt.close();

		return rc;
	}

	/**
	 * Get the string which can insert a null to a database column
	 */
	public String getClobNull() {
		return "'EMPTY_STRING'";
	}

	abstract String genTempTableName(Connection con) throws SQLException;
	/**
	 * Create a single column temporary table with a system generated table name
	 * 
	 * @return name of the temprorary table
	 * @throws SQLException 
	 */
	public String createSimpleTemptable(Connection con, String columnName, String columnType, int columnLength) throws SQLException {
		return null;
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
     * Create a single column temporary table with a given name
     * 
     * @return name of the temprorary table
     */
    public String createSimpleTemptable(Connection con, String columnName, String columnType, int columnLength, String selSql)
            throws SQLException {
        String tableName = genTempTableName(con);
        createSimpleTemptable(con, tableName, columnName, columnType, columnLength);
        String tmpsql = "insert into " + tableName + " " + selSql;
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            stmt.executeUpdate(tmpsql);
        } finally {
            if (stmt != null) stmt.close();
        }
        return tableName;
    }
    
	/**
	 * Create single column temporary table with a given name
	 * 
	 * @return true if success <BR>
	 *         false otherwise
	 * @throws SQLException 
	 */
	public boolean createTempTable(Connection con, String tableName, Hashtable nameType, Hashtable nameLength) throws SQLException {
		return false;
	}

	/**
	 * insert data to a single column temporary table
	 */
	public boolean insertSimpleTempTable(Connection con, String tableName, List data, String columnType) throws SQLException {
		StringBuffer SQL = new StringBuffer();

		SQL.append("INSERT INTO ").append(tableName).append(" VALUES (?) ");
		PreparedStatement stmt = con.prepareStatement(SQL.toString());

		int typeCase = 0;
		if (columnType.equalsIgnoreCase(cwSQL.COL_TYPE_LONG)) {
			typeCase = 1;
		} else if (columnType.equalsIgnoreCase(cwSQL.COL_TYPE_STRING)) {
			typeCase = 2;
		} else if (columnType.equalsIgnoreCase(cwSQL.COL_TYPE_TIMESTAMP)) {
			typeCase = 3;
		} else if (columnType.equalsIgnoreCase(cwSQL.COL_TYPE_INTEGER)) {
			typeCase = 4;
		} else if (columnType.equalsIgnoreCase(cwSQL.COL_TYPE_FLOAT)) {
			typeCase = 5;
		} else if (columnType.equalsIgnoreCase(cwSQL.COL_TYPE_BOOLEAN)) {
			typeCase = 6;
		}

		for (int i = 0; i < data.size(); i++) {
			switch (typeCase) {
			case (1):
				stmt.setLong(1, ((Long) data.get(i)).longValue());
				break;
			case (2):
				stmt.setString(1, (String) data.get(i));
				break;
			case (3):
				stmt.setTimestamp(1, (Timestamp) data.get(i));
				break;
			case (4):
				stmt.setInt(1, ((Integer) data.get(i)).intValue());
				break;
			case (5):
				stmt.setFloat(1, ((Float) data.get(i)).floatValue());
				break;
			case (6):
				stmt.setBoolean(1, ((Boolean) data.get(i)).booleanValue());
				break;
			default:
				break;
			}

			stmt.executeUpdate();
		}
		stmt.close();
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
	 * Drop a temporary table
	 */
	public boolean dropTempTable(Connection con, String tableName) throws SQLException {
		StringBuffer SQL = new StringBuffer();
		StringBuffer SQL1 = new StringBuffer();
		SQL1.append("truncate table ").append(tableName);
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
	 * Del a temporary table
	 */
	public boolean delTempTable(Connection con, String tableName) throws SQLException {
		StringBuffer SQL = new StringBuffer();
		SQL.append("Delete From ").append(tableName);
		PreparedStatement stmt = null;
		stmt = con.prepareStatement(SQL.toString());
		stmt.executeUpdate();
		stmt.close();

		return true;
	}

	/**
	 * Check if the record time is the same as that in the database
	 * 
	 * @param SQL
	 *            query to get the database time of the record
	 * @param user_time
	 *            time recieved from the user
	 * @return boolean true if both time are equal , false otherwise
	 */
	public boolean equalsTimestamp(Connection con, String query, Timestamp user_time) throws SQLException {
		boolean isEqual = false;
		PreparedStatement stmt = con.prepareStatement(query);
		ResultSet rs = stmt.executeQuery();
		if (rs.next()) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			Timestamp db_time = rs.getTimestamp(1);
			if (sdf.format(db_time).equals(sdf.format(user_time))) {
				isEqual = true;
			}
		}
		stmt.close();

		return isEqual;
	}

	/**
	 * Insert Null value to long type of column if the value is 0
	 * 
	 * @param SQL
	 *            query to get the database time of the record
	 * @param user_time
	 *            time recieved from the user
	 * @return boolean true if both time are equal , false otherwise
	 */
	public void setLong(PreparedStatement stmt, int index, long value) throws SQLException {
		if (value > 0) {
			stmt.setLong(index, value);
		} else {
			stmt.setNull(index, java.sql.Types.INTEGER);
		}
		return;
	}

	/**
	 * Insert Null value to long type of column if the value is 0
	 * 
	 * @param SQL
	 *            query to get the database time of the record
	 * @param user_time
	 *            time recieved from the user
	 * @return boolean true if both time are equal , false otherwise
	 */
	public void setInt(PreparedStatement stmt, int index, int value) throws SQLException {
		if (value > 0) {
			stmt.setInt(index, value);
		} else {
			stmt.setNull(index, java.sql.Types.INTEGER);
		}
		return;
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
	public abstract String getDbLenFunPattern(Connection con, String colName);

	public void closeResultSet(ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public void closePreparedStatement(PreparedStatement pst) {
		try {
			if (pst != null) {
				pst.close();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * Get the corresponding concat operator for the sql
	 */
	public String getConcatOperator() {
		return " || ";
	}

	/**
	 * Convert the int type to String for the sql
	 * 
	 * @param field
	 *            is the table field
	 */
	public abstract String convertInt2String(String field);

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

	public abstract String dateadd(String field, String amount);

	public abstract String datediff(String field1, String field2);

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
	public String minDate(String field1, String field2, String alias, int dbMajorVersion) {
		String result = " case " + " when " + field2 + " is null or " + field1 + " < " + field2 + " then " + field1 + " else " + field2 + " end "
				+ alias + " ";
		return result;
	}

	public abstract String subFieldLocation(String main_field, String sub_field, boolean int_type) ;

	/**
	 * 针对不同的数据库使用不同的日期函数，以字符串的形式返回
	 * 
	 * @return 以字符串的形式返回不同数据库的日期函数
	 * @throws SQLException
	 */
	public abstract String getDate();

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
	public abstract String addMonth(String field, String amount);

	/**
	 * 针对不同的数据库使用日期函数，以字符串的形式返回整条SQL语句
	 * 
	 * @param dateFuncStr
	 *            日期函数的字符串形式(例如："getDate()")
	 * @return 以字符串的形式返回不同数据库的日期函数的整条SQL语句
	 */
	public String getDateSql(String dateFuncStr) {
		return "select " + dateFuncStr;
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
	public String getPartOfDate(String field, int part) {
		if (field == null) {
			field = getDate();
		}
		String result = null;
		if (part == cwSQL.YEAR) {
			result = " year(" + field + ") ";
		}
		if (part == cwSQL.MONTH) {
			result = " month(" + field + ") ";
		}
		if (part == cwSQL.DAY) {
			result = " day(" + field + ") ";
		}
		return result;
	}
	
	public abstract String getDatabaseSort();
}
