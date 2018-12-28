package com.cw.wizbank.util;

import java.sql.*;

import com.cw.wizbank.util.cwSQL;

public class cwTransactionLog
{
    static public final int INSERT = 1;
    static public final int UPDATE = 2;
    static public final int DELETE = 3;
    static public final int TYPE_NVARCHAR = -9;
    static public final int TYPE_NTEXT = -10;

    public cwTransactionLog() {
    }
       
    static public void logTransaction(Connection con, String type, String usr_id, Timestamp timestamp, String tableName, String[] keys, String[] ref_id, int action) throws SQLException, cwException{
        if (timestamp == null) {
            timestamp = cwSQL.getTime(con);
        }
        
        StringBuffer query = null;
        
        // get the column name and type
        PreparedStatement stmt = null;
        ResultSet rs = null;
        query = new StringBuffer();
        query.append("SELECT * FROM " + tableName + " WHERE 1=2");
        stmt = con.prepareStatement(query.toString());
        rs = stmt.executeQuery();
        ResultSetMetaData rsmd = rs.getMetaData();
        int numberOfColumns = rsmd.getColumnCount();
        
        String[] columnName = new String[numberOfColumns];
        Integer[] columnType = new Integer[numberOfColumns];
        String[] columnValue = new String[numberOfColumns];
        
        for (int i=0; i<numberOfColumns; i++) {
            columnName[i] = rsmd.getColumnName(i+1);
            columnType[i] = new Integer(rsmd.getColumnType(i+1));
        }
        
        rs.close();
        stmt.close();
        
        boolean boolKeyExist = false;
        for (int i=0; i<keys.length; i++) {
            boolKeyExist = false;
            for (int j=0; j<numberOfColumns; j++) {
                if (keys[i].equalsIgnoreCase(columnName[j])) {
                    boolKeyExist = true;
                }
            }
            if (boolKeyExist == false) {
                break;
            }
        }
        
        if (boolKeyExist == false) {
            throw new cwException("Keys not found in the target table's column");
        }
        
        stmt = null;
        query = new StringBuffer();
        query.append("SELECT * FROM " + tableName + " where");
        for (int i=0; i<keys.length; i++) {
            if (i>0) {
                query.append(" and");
            }
            query.append(" " + keys[i] + "=" + "?");
        }
        stmt = con.prepareStatement(query.toString());
        
        for (int i=0; i<keys.length; i++) {
            for (int j=0; j<numberOfColumns; j++) {
                if (keys[i].equalsIgnoreCase(columnName[j])) {
                    if ((columnType[j]).intValue() == java.sql.Types.INTEGER) {
                        stmt.setInt(i+1, Integer.parseInt(ref_id[i]));
                    }
                    else if ((columnType[j]).intValue() == java.sql.Types.FLOAT) {
                        stmt.setFloat(i+1, Float.parseFloat(ref_id[i]));
                    }
                    else if ((columnType[j]).intValue() == java.sql.Types.DOUBLE) {
                        stmt.setDouble(i+1, Double.parseDouble(ref_id[i]));
                    }
                    else if ((columnType[j]).intValue() == java.sql.Types.DECIMAL) {
                        stmt.setFloat(i+1, Float.parseFloat(ref_id[i]));
                    }
                    else if ((columnType[j]).intValue() == java.sql.Types.VARCHAR) {
                        stmt.setString(i+1, ref_id[i]);
                    }
                    else if ((columnType[j]).intValue() == java.sql.Types.BIT) {
                        stmt.setBoolean(i+1, Boolean.getBoolean(ref_id[i]));
                    }
                    else if ((columnType[j]).intValue() == java.sql.Types.DATE || (columnType[i]).intValue() == java.sql.Types.TIMESTAMP || (columnType[i]).intValue() == java.sql.Types.TIME) {
                        stmt.setTimestamp(i+1, Timestamp.valueOf(ref_id[i]));
                    }
                    // ntext should not be used as key.  To make the API complete, just consider as normal string will be OK as this case should not happen
                    else if ((columnType[j]).intValue() == java.sql.Types.LONGVARCHAR) {
                        stmt.setString(i+1, ref_id[i]);
                    }                    
                    break;
                }
            }
        }
        
        rs = stmt.executeQuery();       
        
        if (rs.next()) {
            if (rs.isLast() == false) {
                throw new cwException("More than 1 record are being modified");
            }
        }

        // get the column value
        for (int i=0; i<numberOfColumns; i++) {
            if ((columnType[i]).intValue() == java.sql.Types.INTEGER || (columnType[i]).intValue() == java.sql.Types.SMALLINT) {
                columnValue[i] = Integer.toString(rs.getInt(columnName[i]));
            }
            else if ((columnType[i]).intValue() == java.sql.Types.FLOAT) {
                columnValue[i] = Float.toString(rs.getFloat(columnName[i]));
            }
            else if ((columnType[i]).intValue() == java.sql.Types.DOUBLE) {
                columnValue[i] = Double.toString(rs.getDouble(columnName[i]));
            }
            else if ((columnType[i]).intValue() == java.sql.Types.DECIMAL) {
                columnValue[i] = Float.toString(rs.getFloat(columnName[i]));
            }
            else if ((columnType[i]).intValue() == java.sql.Types.VARCHAR || (columnType[i]).intValue() == java.sql.Types.CHAR || (columnType[i]).intValue() == TYPE_NVARCHAR) {
                if (rs.getString(columnName[i]) != null) {
                    columnValue[i] = rs.getString(columnName[i]);
                }
                else {
                    columnValue[i] = null;
                }
            }
            else if ((columnType[i]).intValue() == java.sql.Types.BIT) {
                columnValue[i] = (new Boolean(rs.getBoolean(columnName[i])).toString());
            }
            else if ((columnType[i]).intValue() == java.sql.Types.DATE || (columnType[i]).intValue() == java.sql.Types.TIMESTAMP || (columnType[i]).intValue() == java.sql.Types.TIME) {
                if (rs.getTimestamp(columnName[i]) != null) {
                    columnValue[i] = (rs.getTimestamp(columnName[i])).toString();
                }
                else {
                    columnValue[i] = null;
                }
            }
            else if ((columnType[i]).intValue() == java.sql.Types.LONGVARCHAR || (columnType[i]).intValue() == java.sql.Types.CLOB || (columnType[i]).intValue() == TYPE_NTEXT) {
                columnValue[i] = cwSQL.getClobValue(rs, columnName[i]);
            }
        }
        
        rs.close();
        stmt.close();
        
        // build the transaction log XML
        StringBuffer xml = new StringBuffer();
        xml.append("<transaction type=\"").append(type).append("\">").append(cwUtils.NEWL);
        xml.append("<timestamp>").append(timestamp.toString()).append("</timestamp>").append(cwUtils.NEWL);
        xml.append("<usr_id>").append(usr_id).append("</usr_id>").append(cwUtils.NEWL);
        for (int i=0; i<ref_id.length; i++) {
            if (keys[i] != null && keys[i].length()>0 && ref_id[i] != null && ref_id[i].length() > 0) {
                xml.append("<ref_id type=\"").append(Integer.toString(i+1)).append("\">");
                xml.append("<column>");
                xml.append("<name>").append(keys[i]).append("</name>");
                xml.append("<value>").append(ref_id[i]).append("</value>");
                xml.append("</column>");
                xml.append("</ref_id>").append(cwUtils.NEWL);
            }
            else {
                break;
            }
        }
        xml.append("<table>").append(cwUtils.NEWL);
        xml.append("<name>").append(tableName).append("</name>").append(cwUtils.NEWL);
        if (action == INSERT) {
            xml.append("<action>").append("INSERT").append("</action>").append(cwUtils.NEWL);
        }
        else if (action == UPDATE) {
            xml.append("<action>").append("UPDATE").append("</action>").append(cwUtils.NEWL);
        }
        else if (action == DELETE) {
            xml.append("<action>").append("DELETE").append("</action>").append(cwUtils.NEWL);
        }
        if (action == INSERT ||  action == UPDATE) {
            for (int i=0; i<numberOfColumns; i++) {
                xml.append("<column>").append(cwUtils.NEWL);
                xml.append("<name>").append(columnName[i]).append("</name>").append(cwUtils.NEWL);
                xml.append("<value>").append(columnValue[i]).append("</value>").append(cwUtils.NEWL);
                xml.append("</column>").append(cwUtils.NEWL);
            }
        }
        xml.append("</table>").append(cwUtils.NEWL);
        xml.append("</transaction>").append(cwUtils.NEWL);
        
        query = new StringBuffer();
        query.append("INSERT INTO wbTransactionLog (");
        for (int i=0; i<ref_id.length; i++) {
            if (i>0) {
                query.append(",");
            }            
            
            if (ref_id[i] != null && ref_id[i].length() > 0) {
                query.append("tlg_ref_id_").append(Integer.toString(i+1));
            }
            else {
                break;
            }
        }
        query.append(",").append("tlg_type");
        query.append(",").append("tlg_detail_xml");
        query.append(",").append("tlg_create_usr_id");
        query.append(",").append("tlg_create_timestamp");
        query.append(") VALUES (");
        for (int i=0; i<ref_id.length; i++) {
            if (i>0) {
                query.append(",");
            }            
            
            if (ref_id[i] != null && ref_id[i].length() > 0) {
                query.append("?");
            }
            else {
                break;
            }
        }
        query.append(",?,").append(cwSQL.getClobNull()).append(",?,?)");
        stmt = con.prepareStatement(query.toString(), PreparedStatement.RETURN_GENERATED_KEYS);
        int index = 1;
        for (int i=0; i<ref_id.length; i++) {
            if (ref_id[i] != null && ref_id[i].length() > 0) {
                stmt.setString(index++, ref_id[i]);
            }
            else {
                break;
            }
        }
        stmt.setString(index++, type);
        stmt.setString(index++, usr_id);
        stmt.setTimestamp(index++, timestamp);
        if (stmt.executeUpdate() != 1) {
            throw new cwException("Insertion failed.");
        }

        long tlg_id = cwSQL.getAutoId(con, stmt, "wbTransactionLog", "tlg_id");
        String condition = "tlg_id = " + tlg_id;
        String[] tempColumnName = new String[1];
        String[] tempColumnValue = new String[1];
        tempColumnName[0] = "tlg_detail_xml";
        tempColumnValue[0] = xml.toString();
        cwSQL.updateClobFields(con, "wbTransactionLog", tempColumnName, tempColumnValue, condition);
        
        rs.close();
        stmt.close();
    }
}
