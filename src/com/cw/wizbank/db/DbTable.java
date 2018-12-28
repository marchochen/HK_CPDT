package com.cw.wizbank.db;

import java.sql.*;
import java.util.*;
import com.cw.wizbank.util.*;

public class DbTable {

    public static final String COL_TYPE_STRING = "STRING";
    public static final String COL_TYPE_CLOB = "CLOB";
    public static final String COL_TYPE_LONG = "LONG";
    public static final String COL_TYPE_INT = "INT";
    public static final String COL_TYPE_BOOLEAN = "BOOLEAN";
    public static final String COL_TYPE_TIMESTAMP = "TIMESTAMP";
    public static final String COL_TYPE_FLOAT = "FLOAT";

    private String tableName;
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    public String getTableName() {
        return this.tableName;
    }

    private String keyColName;
    public void setKeyColName(String keyColName) {
        this.keyColName = keyColName;
    }
    public String getKeyColName() {
        return this.keyColName;
    }

    private String clobColName;
    public void setClobColName(String clobColName) {
        this.clobColName = clobColName;
    }
    public String getClobColName() {
        return this.clobColName;
    }

    private String clobColValue;
    public void setClobColValue(String clobColValue) {
        this.clobColValue = clobColValue;
    }
    public String getClobColValue() {
        return this.clobColValue;
    }

    private String colName;
    public void setColName(String colName) {
        this.colName = colName;
    }
    public String getColName() {
        return this.colName;
    }

    private Vector vColName;
    public void setVColName(Vector vColName) {
        this.vColName = vColName;
    }
    public Vector getVColName() {
        return this.vColName;
    }

    private Vector vColType;
    public void setVColType(Vector vColType) {
        this.vColType = vColType;
    }
    public Vector getVColType() {
        return this.vColType;
    }

    private Vector vColValue;
    public void setVColValue(Vector vColValue) {
        this.vColValue = vColValue;
    }
    public Vector getVColValue() {
        return this.vColValue;
    }

    private Connection con;
    public void setCon(Connection con) {
        this.con = con;
    }
    public Connection getCon() {
        return this.con;
    }

    private PreparedStatement stmt;

    public DbTable(Connection con) {
        super();
        setCon(con);
    }

    /**
    Update tableName<BR>
    Will Ignore those Clob data type
    */
    public void upd(String tableName, Vector vColName, Vector vColType, Vector vColValue,
                    long key, String keyColName)
        throws SQLException {

        //store the input param into class variable
        setTableName(tableName);
        setVColName(vColName);
        setVColType(vColType);
        setVColValue(vColValue);
        setKeyColName(keyColName);

        //construct SQL
        StringBuffer SQLBuf = new StringBuffer(512);
        SQLBuf.append(" Update ").append(this.tableName)
              .append(" Set ").append(makeUpdColNameList())
              .append(" Where ").append(keyColName).append(" = ? ");

        PreparedStatement stmt = this.con.prepareStatement(SQLBuf.toString());
        int index = setStmt(stmt);
        stmt.setLong(index++, key);
        stmt.executeUpdate();
        stmt.close();
        return;
    }
    public String setInsParam(String tableName, Vector vColName, Vector vColType, Vector vColValue) throws SQLException {
    	//store the input param into class variable
    	setTableName(tableName);
    	setVColName(vColName);
    	setVColType(vColType);
    	setVColValue(vColValue);
    	
    	//construct SQL
    	StringBuffer SQLBuf = new StringBuffer(512);
    	SQLBuf.append(" Insert Into ").append(this.tableName)
    		  .append(" (").append(makeColNameList()).append(")")
    		  .append(" Values (").append(makeQMarkList()).append(")");
    	return SQLBuf.toString();
    }
    /**
    Insert into tableName<BR>
    Will Ignore those Clob data type
    */
    public PreparedStatement ins4AutoId(String tableName, Vector vColName, Vector vColType, Vector vColValue)
        throws SQLException {
    	
    	String sql = setInsParam(tableName, vColName, vColType, vColValue);
    	PreparedStatement stmt = this.con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        setStmt(stmt);
        stmt.executeUpdate();
        return stmt;
    }
    /**
    Insert into tableName<BR>
    Will Ignore those Clob data type
    */
    public void ins(String tableName, Vector vColName, Vector vColType, Vector vColValue) throws SQLException {
    	PreparedStatement stmt = null;
    	try{
    		String sql = setInsParam(tableName, vColName, vColType, vColValue);
    		stmt = this.con.prepareStatement(sql);
    		setStmt(stmt);
    		stmt.executeUpdate();
    	} finally {
    		if(stmt != null) stmt.close();
    	}
    }

    /**
    Update one Clob data type column
    */
    public void UpdClob(String tableName, String clobColName, String clobColValue,
                        long key, String keyColName)
        throws SQLException {
        // << BEGIN oracle migration
/*        //store the input param into class variable
        setTableName(tableName);
        setClobColName(clobColName);
        setClobColValue(clobColValue);
        setKeyColName(keyColName);

        //construct SQL
        String SQL = makeUpdClobSQL();
        PreparedStatement stmt = this.con.prepareStatement(SQL,
                                 ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        stmt.setLong(1, key);
        ResultSet rs = stmt.executeQuery();
        if (rs.next())
        {
            cwSQL.setClobValue(this.con, rs, this.clobColName, this.clobColValue);
            rs.updateRow();
        }
        stmt.close();*/
        Hashtable columns = new Hashtable();
        // construct the condition
        String condition = keyColName + " = " + key;
        // construct the column & value
        String[] columnName = new String[1];
        String[] columnValue = new String[1];
        columnName[0] = clobColName;
        columnValue[0] = clobColValue;
        cwSQL.updateClobFields(this.con, tableName, columnName, columnValue, condition);
        // >> END
        return;
    }

    public void UpdClob(String tableName, String colName, String colValue,
                        String key, String keyColName)
        throws SQLException {
        // << BEGIN oracle migration
/*        //store the input param into class variable
        setTableName(tableName);
        setClobColName(clobColName);
        setClobColValue(clobColValue);
        setKeyColName(keyColName);

        //construct SQL
        String SQL = makeUpdClobSQL();
        PreparedStatement stmt = this.con.prepareStatement(SQL,
                                 ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        stmt.setString(1, key);
        ResultSet rs = stmt.executeQuery();
        if (rs.next())
        {
            cwSQL.setClobValue(this.con, rs, this.clobColName, this.clobColValue);
            rs.updateRow();
        }
        stmt.close();*/
        // construct the condition
        String condition = keyColName + " = '" + key + "'";
        // construct the column & value
        String[] columnName = new String[1];
        String[] columnValue = new String[1];
        columnName[0] = clobColName;
        columnValue[0] = clobColValue;
        cwSQL.updateClobFields(this.con, tableName, columnName, columnValue, condition);
        // >> END
        return;
    }

    /**
    Construct a SQL to update Clob data type
    */
    // retired!
    private String makeUpdClobSQL() {

        StringBuffer SQLBuf = new StringBuffer();
        SQLBuf.append(" Select ").append(this.clobColName)
              .append(" From ").append(this.tableName)
              .append(" Where ").append(this.keyColName).append(" = ? For Update ");
        return SQLBuf.toString();
    }

    /**
    Set data for input PreparedStatement<Br>
    base on vColType, vColValue.<br>
    Will not consider Clob data type
    */
    private int setStmt(PreparedStatement stmt) throws SQLException {

        int index = 1;
        for(int i=0; i<this.vColName.size(); i++) {
            String colType = (String)this.vColType.elementAt(i);
            if(colType.equals(COL_TYPE_LONG)) {
                //long lvalue = Long.parseLong((String)this.vColValue.elementAt(i));
                if(this.vColValue.elementAt(i)==null) {
                    stmt.setNull(index++, java.sql.Types.INTEGER);
                } else {
                    long lvalue = ((Long)this.vColValue.elementAt(i)).longValue();
                    stmt.setLong(index++, lvalue);
                }
            }
            else if(colType.equals(COL_TYPE_STRING)) {
                String svalue;
                if (this.vColValue.elementAt(i)==null)
                    svalue = null;                    
                else
                    svalue = (String)this.vColValue.elementAt(i);
                
                stmt.setString(index++, svalue);
            }
            else if(colType.equals(COL_TYPE_TIMESTAMP)) {
                //Timestamp tsvalue = Timestamp.valueOf((String)this.vColValue.elementAt(i));
                Timestamp tsvalue;
                if (this.vColValue.elementAt(i)==null){
                    tsvalue = null;                    
                }else{
                    tsvalue = (Timestamp)this.vColValue.elementAt(i);
                }
                stmt.setTimestamp(index++, tsvalue);
            }
            else if(colType.equals(COL_TYPE_BOOLEAN)) {
                boolean bvalue = ((Boolean)this.vColValue.elementAt(i)).booleanValue();
                stmt.setBoolean(index++, bvalue);
            }
            else if(colType.equals(COL_TYPE_FLOAT)) {
                if(this.vColValue.elementAt(i)==null) {
                    stmt.setNull(index++, java.sql.Types.FLOAT);
                } else {
                    float fvalue = ((Float)this.vColValue.elementAt(i)).floatValue();
                    stmt.setFloat(index++, fvalue);
                }
            }
            else if(colType.equals(COL_TYPE_INT)) {
                if(this.vColValue.elementAt(i)==null) {
                    stmt.setNull(index++, java.sql.Types.INTEGER);
                } else {
                    int ivalue = ((Integer)this.vColValue.elementAt(i)).intValue();
                    stmt.setInt(index++, ivalue);
                }
            }
        }
        return index;
    }

    /**
    Make a list of question marks : "?,?,?,?"<Br>
    base on this.vColName and this.vColType.<Br>
    Will insert cwSQL.getClobNull(con) for clob data type
    */
    private String makeQMarkList() throws SQLException {

        StringBuffer Buf = new StringBuffer(256);
        boolean first = true;
        for(int i=0; i<this.vColName.size(); i++) {
            //if(!((String)this.vColType.elementAt(i)).equals(COL_TYPE_CLOB)) {
                if(!first) {
                    Buf.append(",");
                }
                if(((String)this.vColType.elementAt(i)).equals(COL_TYPE_CLOB)) {
                    Buf.append(cwSQL.getClobNull());
                } else {
                    Buf.append("?");
                }
                first = false;
            //}
        }
        return Buf.toString();
    }

    /**
    Make a list of colume names : "itm_id, itm_code, itm_version_code"<Br>
    base on this.vColName and this.vColType<Br>
    */
    private String makeColNameList() {

        StringBuffer Buf = new StringBuffer(256);
        boolean first = true;
        for(int i=0; i<this.vColName.size(); i++) {
            //if(!((String)this.vColType.elementAt(i)).equals(COL_TYPE_CLOB)) {
                if(!first) {
                    Buf.append(",");
                }

                Buf.append((String)vColName.elementAt(i));
                first = false;
            //}
        }
        return Buf.toString();
    }

    /**
    Make a list of colume names : "itm_id = ? , itm_code = ? , itm_version_code = ?"<Br>
    base on this.vColName and this.vColType<Br>
    Will not consider Clob data type
    */
    private String makeUpdColNameList() {

        StringBuffer Buf = new StringBuffer(256);
        boolean first = true;
        for(int i=0; i<this.vColName.size(); i++) {
            if(!((String)this.vColType.elementAt(i)).equals(COL_TYPE_CLOB)) {
                if(!first) {
                    Buf.append(",");
                }
                Buf.append((String)vColName.elementAt(i)).append(" = ? ");
                first = false;
            }
        }
        return Buf.toString();
    }
    
    
    public void upd_batch(String tableName, Vector vColName, Vector vColType, Vector vColValue,
            long key[], String keyColName)throws SQLException {

	//store the input param into class variable
	setTableName(tableName);
	setVColName(vColName);
	setVColType(vColType);
	setVColValue(vColValue);
	setKeyColName(keyColName);
	
	//construct SQL
	StringBuffer SQLBuf = new StringBuffer(512);
	SQLBuf.append(" UPDATE ").append(this.tableName).append(" SET ").append(makeUpdColNameList());
	if(key!=null&&key.length!=0){  
	       SQLBuf.append(" WHERE ").append(keyColName).append(" in").append(cwUtils.array2list(key));
	       PreparedStatement stmt = this.con.prepareStatement(SQLBuf.toString());
	       int index = setStmt(stmt);
	       stmt.executeUpdate();
	       stmt.close();
			}
	return;
	}
}