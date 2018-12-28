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

import org.apache.commons.lang.StringUtils;

public class MYSQLDbHelper extends DbHelper {
	
	public String replaceNull(String colName, String value) {
		return " ifnull(" + colName + "," + value + ")";
	}
	
	/**
	 * Get the database time
	 */
	public Timestamp getTime(Connection con) throws SQLException {
		Timestamp ts = null;
		String SQL = "select sysdate()";

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
		SQL.append("  CREATE TEMPORARY TABLE ").append(tableName).append(" ( ");
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
				dbType = "VARCHAR(" + columnLen + ")";
			}
			SQL.append(dbType);
		}
		SQL.append(")");
		con.createStatement().execute(SQL.toString());
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
	
	/**
	 * 针对不同的数据库使用不同的日期函数，以字符串的形式返回
	 * 
	 * @return 以字符串的形式返回不同数据库的日期函数
	 * @throws SQLException
	 */
	public String getDate() {
		return " sysdate() ";
	}
	
	public String getDatabaseSort() {
		// TODO Auto-generated method stub
		return MYSQL;
	}

	@Override
	public String getDbLenFunPattern(Connection con, String colName) {
		return "LENGTH(" + colName + ")";
	}

	@Override
	public String convertInt2String(String field) {
		return " concat(" + field + " ) ";
	}

	@Override
	public String dateadd(String field, String amount) {
		String result = " DATE_ADD(" 
					+((field == null || field.equals("")) ? " sysdate() " : field )
					+" , INTERVAL " + ((amount == null || amount.equals("")) ? "0" : amount) + " DAY )";
		return result;
	}

	@Override
	public String datediff(String field1, String field2) {
		String result = " DATEDIFF(" + ((field1 == null || field1.equals("")) ? "sysdate()" : field1) + ", "
				+ ((field2 == null || field2.equals("")) ? "sysdate()" : field2) + ")";
		return result;
	}

	@Override
	public String subFieldLocation(String main_field, String sub_field,
			boolean int_type) {
		StringBuffer result = new StringBuffer(128);
		String converted_sub_field = sub_field;
		if (int_type) {
			converted_sub_field = convertInt2String(sub_field);
		}
		result.append(" instr(").append(main_field).append(", ").append(converted_sub_field).append(") > 0 ");
		return result.toString();
	}

	@Override
	public String addMonth(String field, String amount) {
		String result = " DATE_ADD(" 
				+((field == null || field.equals("")) ? " sysdate() " : field )
				+" , INTERVAL " + ((amount == null || amount.equals("")) ? "0" : amount) + " MONTH )";
	return result;
	}
	
	/**
	 * 将临时表转换成物理表
	 * @param con
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
	public String tempTable2Physical(Connection con, String tableName) throws SQLException {
		String physicalTableName = genTempTableName(con);
		StringBuffer SQL = new StringBuffer(0);
		SQL.append("  CREATE TABLE ").append(physicalTableName).append(" SELECT * FROM ").append(tableName);
		con.createStatement().execute(SQL.toString());
		return physicalTableName;
	}
	
	/**
	 * 删除table
	 * @param con
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
	public boolean dropTable(Connection con, String tableName) throws SQLException{
		if(StringUtils.isEmpty(tableName)){
			return false;
		}
		StringBuffer SQL = new StringBuffer(0);
		SQL.append(" DROP TABLE ").append(tableName);
		return con.createStatement().execute(SQL.toString());
	}
}
