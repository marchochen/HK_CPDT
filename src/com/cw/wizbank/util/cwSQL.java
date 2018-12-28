package com.cw.wizbank.util;

import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Hashtable;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.config.system.db.DriverType;
import com.cw.wizbank.db.DbTrainingCenter;
import com.cw.wizbank.qdb.dbUserGroup;
import com.cwn.wizbank.utils.CommonLog;

/**
A class to handle database related method <BR>
*/
public class cwSQL
{
    /**
    default connection pool size
    */
    public static final int DEFAULT_POOL_SIZE = 20;

    /**
    JDBC column type : Timestamp
    */
    public static final String COL_TYPE_TIMESTAMP = "Timestamp";
    /**
    JDBC column type : Long
    */
    public static final String COL_TYPE_LONG = "Long";
    /**
    JDBC column type : Integer
    */
    public static final String COL_TYPE_INTEGER = "Integer";
    /**
    JDBC column type : Float
    */
    public static final String COL_TYPE_FLOAT = "Float";
    /**
    JDBC column type : Boolean
    */
    public static final String COL_TYPE_BOOLEAN = "Boolean";
    /**
    JDBC column type : String
    */
    public static final String COL_TYPE_STRING = "String";
    /**
    JDBC column type : Decimal
    */
    public static final String COL_TYPE_DECIMAL = "Decimal";
    /**
    Default column length
    */
    public static final String DEFAULT_COLUMN_LENGTH = "30";

    /**
    Database Vendor : Microsft SQL Server
    */
    public static final String DBVENDOR_MSSQL    = "MSSQL";
    /**
    Database Vendor : Oracle
    */
    public static final String DBVENDOR_ORACLE   = "ORACLE";
    /**
    Database Vendor : DB2
    */
    public static final String DBVENDOR_DB2      = "DB2";
    
    /**
    Database Vendor : MYSQL
    */
    public static final String DBVENDOR_MYSQL      = "MYSQL";

    /**
    JDBC product name : oracle
    */
    public static final String ProductName_ORACLE = "oracle";
    /**
    JDBC product name : microsoft sql server
    */
    public static final String ProductName_MSSQL = "microsoft sql server";
    /**
    JDBC product name : db2
    */
    public static final String ProductName_DB2 = "db2";
    /**
    JDBC product name : Postgres SQL
    */
    public static final String ProductName_POSTGRESQL = "postgresql";
    
    /**
    JDBC product name : MYSQL
    */
    public static final String ProductName_MYSQL = "mysql";

    /**
     * For access to date clause
     */
    public static final int YEAR = 0;
    public static final int MONTH = 1;
    public static final int DAY = 2;

    /**
    Database connection parameter : vendor name
    */
    private String dbVendor;
    /**
    Database connection parameter : hostname / IP address
    */
    private String dbHost;
    /**
    Database connection parameter : port number
    */
    private String dbPort;
    /**
    Database connection parameter : database name
    */
    private String dbName;
    /**
    Database connection parameter : database user
    */
    private String usrNm;
    /**
    Database connection parameter : user's password
    */
    private String usrPw;

    /**
    static variables for database pooling
    */
	private boolean dsEnabled;
    private String dsName;
    private DataSource ds = null ;
    private InitialContext ctx = null ;
    private String classname;
    private String protocol;
    private String separator;

    /**
    Set the parameters of the database connection
    */
    public void setParam(WizbiniLoader wizbini) {
    	dbVendor = wizbini.cfgSysDb.getDbConnect().getDbvendor();
        dbHost   = wizbini.cfgSysDb.getDbConnect().getHost();
        int port = wizbini.cfgSysDb.getDbConnect().getPort();
        if(port != 0) {
        	dbPort = String.valueOf(port);
        }
        dbName   = wizbini.cfgSysDb.getDbConnect().getName();
        usrNm    = wizbini.cfgSysDb.getDbConnect().getUser();
        usrPw    = wizbini.cfgSysDb.getDbConnect().getPass();
        dsEnabled = wizbini.cfgSysDb.getDatasource().isEnabled();
        dsName   = wizbini.cfgSysDb.getDatasource().getJndiname();
        List driver_lst = wizbini.cfgSysDb.getJdbcDriver().getDriver();
        for(int i=0; i<driver_lst.size(); i++) {
        	DriverType dt = (DriverType)driver_lst.get(i);
        	if(dbVendor.equalsIgnoreCase(dt.getVendor())) {
        		classname = dt.getClassname();
        		protocol = dt.getProtocol();
        		separator = dt.getSeparator();
        		break;
        	}
        }
    }

    /**
     * Open a new database connection
     * @param isBatch : true: the API is used for batch, batch not use datasource.
     * @return
     * @throws cwException
     * @throws NamingException
     * @throws SQLException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     */
    public Connection openDB(boolean isBatch) throws cwException, NamingException, SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException{
    	Connection con = null;
    	if(dsEnabled && !isBatch){
    		if(ds==null){
				ctx = new InitialContext();
				ds =(DataSource)ctx.lookup(dsName);
    		}
    		con = ds.getConnection();
    	} else {
    		String url = "";
			String port = "";
			if(dbPort != null && !dbPort.equals("")) {
				port = ":" + dbPort;
			}
			Class.forName(classname);
			url = protocol + dbHost + port + separator + dbName;
    		con = DriverManager.getConnection(url, usrNm, usrPw);
    	}
    	con.setAutoCommit(false);
    	newInstance(getDbProductName(con));
    	return con;
    }

    /**
     * get the product name of database
     */
	public static String getDbProductName(Connection con) throws SQLException, cwException {
        return con.getMetaData().getDatabaseProductName().toLowerCase();
	}

	private static String db_product_name = null;
	private static String db_type = null;
    private static DbHelper db_helper = null;

    /**
     * initialize a static instance of database helper
     * @param dbProductName
     * @throws cwException
     */
    public static void newInstance(String dbProductName) throws cwException {
        if (db_helper == null) {
        	db_product_name = dbProductName;
        	String dbType = getDbType(db_product_name);
        	try {
    			db_helper = (DbHelper) Class.forName("com.cw.wizbank.util." + dbType + "DbHelper").newInstance();
    		} catch (Exception e) {
    			CommonLog.error(e.getMessage(),e);
    			throw new cwException("Can't load com.cw.wizbank.util." + dbType + "DbHelper");
    		}
        }
    }

    private static String getDbType(String dbproduct) throws cwException {
        db_type = null;
        if(dbproduct.indexOf(ProductName_MSSQL) >= 0) {
            db_type = DBVENDOR_MSSQL;
        } else if(dbproduct.indexOf(ProductName_ORACLE) >= 0) {
            db_type = DBVENDOR_ORACLE;
        } else if(dbproduct.indexOf(ProductName_DB2) >= 0) {
            db_type = DBVENDOR_DB2;
        }else if(dbproduct.indexOf(ProductName_MYSQL) >= 0){
        	db_type = DBVENDOR_MYSQL;
        }else {
        	throw new cwException("The type of database is invalid.");
        }
		return db_type;
	}

    public static String getDbProductName() {
    	return db_product_name;
    }

    public static String getDbType() {
        return db_type;
    }

    public static String noLockTable() {
        return (cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType()) ? " with(nolock) ":" ");
    }

    public static String rowLockTable() {
        return (cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType()) ? " with(rowlock) ":" ");
    }

    /**
	 * Replace NULL value
	 */
	public static String replaceNull(String colName, String value) {
	    return db_helper.replaceNull(colName, value);
	}

	/**
	Get the database time
	*/
	public static Timestamp getTime(Connection con) throws SQLException {
		return db_helper.getTime(con);
	}

	/**
	Get database null string
	*/
	public static String get_null_sql(String type) {
		return db_helper.get_null_sql(type);
	}

	/**
	Get SQL right outer join string
	*/
	public static String get_right_join() {
		return db_helper.get_right_join();
	}

	/**
	Get the value of a database column which length is larger than 4000
	*/
	public static String getClobValue(ResultSet rs, String colName) throws SQLException {
		return db_helper.getClobValue(rs, colName);
	}
	
	public static String getClobValue(Connection con, ResultSet rs, String colName) throws SQLException {
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
	 * @author    kawai
	 * @version   2002.04.25
	 * @param     con           connection of the database to be updated
	 * @param     tableName     name of the table to be updated
	 * @param     columnName    name of the column(s) to be updated
	 * @param     columnValue   value of the column(s) to be updated
	 * @param     condition     the WHERE clause string of the records to be updated, null if no WHERE clause
	 * @return    either the number of rows updated; or 0 when no rows are updated
	 * @exception SQLException  if any error occurs during any sql operations
	 */
	public static int updateClobFields(Connection con, String tableName, String columnName[], String columnValue[], String condition) throws SQLException {
		return db_helper.updateClobFields(con, tableName, columnName, columnValue, condition);
	}

	/**
	Get the string which can insert a null to a database column
	*/
	public static String getClobNull() {
		return db_helper.getClobNull();
	}

	/**
	Create a single column temporary table with a system generated table name
	@return name of the temprorary table
	*/
	public static String createSimpleTemptable(Connection con, String columnName, String columnType, int columnLength) throws SQLException {
		return db_helper.createSimpleTemptable(con, columnName, columnType, columnLength);
	}

	/**
	Create a single column temporary table with a given name
	@return name of the temprorary table
	*/
	public static boolean createSimpleTemptable(Connection con, String tableName, String columnName, String columnType, int columnLength) throws SQLException {
		return db_helper.createSimpleTemptable(con, tableName, columnName, columnType, columnLength);
	}

	/**
	Create multi column temporary table with a given name
	@return true if success <BR>
	false otherwise
	*/
	public static boolean createTempTable(Connection con, String tableName, Hashtable nameType, Hashtable nameLength) throws SQLException {
		return db_helper.createTempTable(con, tableName, nameType, nameLength);
	}

	/**
    Create single column temporary table with a given select statement.
    @param  selSql  a select statement e.g: select usr_ent_id from reguser
    @return name of the temprorary table <BR>
    false otherwise
    */
    public static String createSimpleTemptable(Connection con, String columnName, String columnType, int columnLength, String selSql) throws SQLException {
        return db_helper.createSimpleTemptable(con, columnName , columnType, columnLength, selSql);
    }

	/**
	insert data to a single column temporary table
	*/
	public static boolean insertSimpleTempTable(Connection con, String tableName, List data, String columnType) throws SQLException {
		return db_helper.insertSimpleTempTable(con, tableName, data, columnType);
	}

	public static String[] getSQLClause(Connection con, String colName, String columnType, List data, int columnLength) throws SQLException {
		return db_helper.getSQLClause(con, colName, columnType, data, columnLength);
	}

	/**
	Drop a temporary table
	*/
	public static boolean dropTempTable(Connection con, String tableName) throws SQLException {
		return db_helper.dropTempTable(con, tableName);
	}
	
	/**
	Delete a temporary table
	*/
	public static boolean delTempTable(Connection con, String tableName) throws SQLException {
		return db_helper.delTempTable(con, tableName);
	}

	/**
	Check if the record time is the same as that in the database
	@param SQL query to get the database time of the record
	@param user_time time recieved from the user
	@return boolean true if both time are equal , false otherwise
	*/
	public static boolean equalsTimestamp(Connection con, String query, Timestamp user_time) throws SQLException {
		return db_helper.equalsTimestamp(con, query, user_time);
	}

	/**
	Insert Null value to long type of column if the value is 0
	@param SQL query to get the database time of the record
	@param user_time time recieved from the user
	@return boolean true if both time are equal , false otherwise
	*/
	public static void setLong(PreparedStatement stmt, int index, long value) throws SQLException {
		db_helper.setLong(stmt, index, value);
	}

	/**
	Insert Null value to long type of column if the value is 0
	@param SQL query to get the database time of the record
	@param user_time time recieved from the user
	@return boolean true if both time are equal , false otherwise
	*/
	public static void setInt(PreparedStatement stmt, int index, int value) throws SQLException {
		db_helper.setInt(stmt, index, value);
	}

	public static String getAncesterPattern(Connection con, String colName) throws SQLException {
		DatabaseMetaData conMD = con.getMetaData();
		String dbproduct = conMD.getDatabaseProductName().toLowerCase();
		StringBuffer SQLBuf = new StringBuffer(128);
		if (dbproduct.indexOf(ProductName_ORACLE) >= 0) {
			SQLBuf.append("'% ' || TO_CHAR(").append(colName).append(") || ' %'");
		} else {
			SQLBuf.append("'% ' + cast(").append(colName).append(" as varchar) + ' %'");
		}
		return SQLBuf.toString();
	}

	/**
	 * Get the len() function pattern of different database
	 * @param con is the dbConnection
	 * @param colName is the param to the len() function to ocunt the length of the column
	 * @return the meta len() function pattern
	 * @throws SQLException
	 */
	public static String getDbLenFunPattern(Connection con, String colName) throws SQLException {
		return db_helper.getDbLenFunPattern(con, colName);
	}

	public static void closeResultSet(ResultSet rs) {
		db_helper.closeResultSet(rs);
	}

	public static void closePreparedStatement(PreparedStatement pst) {
		db_helper.closePreparedStatement(pst);
	}

	public static void cleanUp(ResultSet rs, PreparedStatement pst) {
		db_helper.closeResultSet(rs);
		db_helper.closePreparedStatement(pst);
	}

	/**
	 * Get the corresponding concat operator for the sql
	 */
	public static String getConcatOperator() {
		return db_helper.getConcatOperator();
	}

	/**
	 * Convert the int type to String for the sql
	 * @param field is the table field
	 */
	public static String convertInt2String(String field) {
		return db_helper.convertInt2String(field);
	}

	public static String getDayUnit() {
		return  db_helper.getDayUnit();
	}

	public static long getAutoId(Connection con, PreparedStatement stmt, String tableName, String columnName) throws SQLException {
		return  db_helper.getAutoId(con, stmt, tableName, columnName);
	}
	

	/*
	 * 不建议使用该方法法，只有在 PreparedStatement.RETURN_GENERATED_KEYS 无法使用，且又要得到当家新插入记录ID时才用。
	 * 其它情况，请用getAutoId（）方法
	 */
	public static long getMaxId(Connection con, String tableName, String columnName) throws SQLException {
		long id = 0;
		PreparedStatement stmt_ora = con.prepareStatement("SELECT max(" + columnName + ") as id FROM " + tableName + "");
		ResultSet rs_ora = stmt_ora.executeQuery();
		if (rs_ora.next()) {
			id = rs_ora.getLong(1);
		}
		rs_ora.close();
		stmt_ora.close();
		if (id <= 0) {
			throw new SQLException("Cannot get the max id of " + columnName + " from " + tableName);
		}
		return id;
	}


	public static String dateadd(String field, String amount) {
		return db_helper.dateadd(field, amount);
	}

	public static String datediff(String field1, String field2) {
		return db_helper.datediff(field1, field2);
	}

	/**
	 * 取二个时间中的最小值，此值将会出现在定义的别名列中
	 * @param field1: 第一个时间
	 * @param field2: 第二个时间
	 * @param alias: 最后生成字段的别名
	 * @param dbMajorVersion
	 * @return
	 */
	public static String minDate(String field1, String field2, String alias, int dbMajorVersion) {
		return db_helper.minDate(field1, field2, alias, dbMajorVersion);
	}

	public static String subFieldLocation(String main_field, String sub_field, boolean int_type) {
		return db_helper.subFieldLocation(main_field, sub_field, int_type);
	}

	/**
	 * 针对不同的数据库使用不同的日期函数，以字符串的形式返回
	 * @return 以字符串的形式返回不同数据库的日期函数
	 */
	public static String getDate() {
		return db_helper.getDate();
	}

	/**
	 * 针对不同的数据库使用不同的月份日期函数，以字符串的形式返回
	 * @param field 日期字段(字符串形式)
	 * @param amount 月数(字符串形式)
	 * @return  以字符串的形式返回不同数据库的月份日期函数
	 */
	public static String addMonth(String field, String amount) {
		return db_helper.addMonth(field, amount);
	}

	/**
	 * 针对不同的数据库使用日期函数，以字符串的形式返回整条SQL语句
	 * @param dateFuncStr 日期函数的字符串形式(例如："getDate()")
	 * @return  以字符串的形式返回不同数据库的日期函数的整条SQL语句
	 */
	public static String getDateSql(String dateFuncStr) {
		return db_helper.getDateSql(dateFuncStr);
	}

	/**
	    * 特定数据库中某个日期的年份（或月份、或天数）函数
	 * @param field 字段名
	 * @param part 指定数据库中具体的日期函数（可选值cwSQL.YEAR、cwSQL.MONTH、cwSQL.DAY）
	    * @return 特定数据库的日期函数
	    */
	public static String getPartOfDate(String field, int part) {
		return db_helper.getPartOfDate(field, part);
	}
	
	  public static String dateadd(Connection con, String field, String amount)
            throws SQLException {
        String result = "";
        DatabaseMetaData conMD = con.getMetaData();
        String dbproduct = conMD.getDatabaseProductName().toLowerCase();
        if (dbproduct.indexOf(ProductName_MSSQL) >= 0) {
            result = " dateadd(day, "
                    + ((amount == null || amount.equals("")) ? "0" : amount)
                    + ", "
                    + ((field == null || field.equals("")) ? " getDate() "
                            : field) + ")";
        } else if (dbproduct.indexOf(ProductName_ORACLE) >= 0) {
            result = ((field == null || field.equals("")) ? " sysdate"
                    : " cast(" + field + " as date)")
                    + " + "
                    + ((amount == null || amount.equals("")) ? "0" : amount);
        } else if (dbproduct.indexOf(ProductName_DB2) >= 0) {
            result = ((field == null || field.equals("")) ? "current timestamp"
                    : field)
                    + " + "
                    + ((amount == null || amount.equals("")) ? "0" : amount)
                    + " days";
        }
        return result;
    }

    /**
     * Replace NULL value
     */
    public static String replaceNull(Connection con, String colName,
            String value) throws SQLException {
        String SQL = null;

        DatabaseMetaData conMD = con.getMetaData();
        String dbproduct = conMD.getDatabaseProductName().toLowerCase();

        if (dbproduct.indexOf(ProductName_MSSQL) >= 0)
            SQL = " isnull(" + colName + "," + value + ")";
        else if (dbproduct.indexOf(ProductName_ORACLE) >= 0)
            SQL = " nvl(" + colName + "," + value + ")";
        else if (dbproduct.indexOf(ProductName_DB2) >= 0)
            SQL = " coalesce(" + colName + "," + value + ")";
        else if (dbproduct.indexOf(ProductName_POSTGRESQL) >= 0)
            SQL = " coalesce(" + colName + "," + value + ")";

        return SQL;
    }

    public static String datediff(Connection con, String field1, String field2) throws SQLException{
        String result = "";
        DatabaseMetaData conMD = con.getMetaData();
        String dbproduct = conMD.getDatabaseProductName().toLowerCase();
        if (dbproduct.indexOf(ProductName_MSSQL) >= 0) {
            result = " datediff(day, " + ((field1 == null || field1.equals("")) ? "getDate()" : field1) + ", " +
                ((field2 == null || field2.equals("")) ? "getDate()" : field2) + ")";
        }
        else if (dbproduct.indexOf(ProductName_ORACLE) >= 0) {
             result = " trunc("  + ((field2 == null || field2.equals("")) ? "sysdate" : "cast( " + field2 + " as date)");
             result += " - " + ((field1 == null || field1.equals("")) ? "sysdate)" : "cast( " + field1 + " as date))");
	}
        else if (dbproduct.indexOf(ProductName_DB2) >= 0) {
            result = "days(" + ((field1 == null || field1.equals("")) ? "current timestamp" : field1) + ") - " +
                "days(" + ((field2 == null || field2.equals("")) ? "current timestamp" : field2) + ")";
        }
        else if (dbproduct.indexOf(ProductName_MYSQL) >= 0) {
        	MYSQLDbHelper helper = new MYSQLDbHelper();
        	result = helper.datediff(field1, field2);
        }
        return result;
    }
    
    /**
     *  自动生成需要手工添加的编号
     * @param con
     * @param ins_type
     * @return
     */
    public static String getAutoCode(Connection con, String ins_type) throws SQLException{
    	String prefix = "";
    	long maxId = 0;
    	if(ins_type.equalsIgnoreCase("COS")){ // 课程
    		prefix = "CM";
    		maxId = aeItem.getMaxItemId(con);
    	} else if(ins_type.equalsIgnoreCase("INTEGRATED")){	// 集成培训
    		prefix = "IT";
    		maxId = aeItem.getMaxItemId(con);
    	} else if(ins_type.equalsIgnoreCase("EXAM")){ // 考试
    		prefix = "EM";
    		maxId = aeItem.getMaxItemId(con);
    	} else if(ins_type.equalsIgnoreCase("BOOK")){ // 参考资料
    		prefix = "RM";
    		maxId = aeItem.getMaxItemId(con);
    	} else if(ins_type.equalsIgnoreCase("TC")){ // 培训中心
    		prefix = "TC";
    		maxId = DbTrainingCenter.getMaxTcrId(con);
    	} else if(ins_type.equalsIgnoreCase("USG")){ // 用户组
    		prefix = "UG";
    		maxId = dbUserGroup.getMaxUsgEntId(con);
    	}else if(ins_type.equalsIgnoreCase("CLASS")){	// 班级
    		prefix = "CL";
    		maxId = aeItem.getMaxItemId(con);
    	}else if(ins_type.equalsIgnoreCase("AUDIOVIDEO")){	// 影音
    		prefix = "VD";
    		maxId = aeItem.getMaxItemId(con);
    	}else if(ins_type.equalsIgnoreCase("WEBSITE")){	// 网站
    		prefix = "WS";
    		maxId = aeItem.getMaxItemId(con);
    	}else if(ins_type.equalsIgnoreCase("MOBILE")){	// 网站
    		prefix = "MB";
    		maxId = aeItem.getMaxItemId(con);
    	}
    	maxId++;
    	NumberFormat nf = new DecimalFormat("0000");
    	return prefix + nf.format(maxId);
    }
}