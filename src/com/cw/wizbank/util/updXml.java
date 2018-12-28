/**
 * 
 */
package com.cw.wizbank.util;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;

import com.cwn.wizbank.utils.CommonLog;

/**
 * @author DeanChen
 *
 */
public class updXml {
	private static final int TARGET_ARR_LENGHT = 4;
	private static final String DELIMITER_BLANK = " ";
	
	public static void upd(Connection con, String appPath, String filePath) throws IOException {
		Statement statement = null;
        ResultSet resultset = null;
        ResultSetMetaData resultsetmetadata = null;
		try
        {
			Vector sqlMetaVec = getSqlMetaVec(filePath, cwUtils.ENC_UTF);
			statement = con.createStatement();
            for(int sqlMetaIndex = 0; sqlMetaIndex < sqlMetaVec.size(); sqlMetaIndex++) {
            	Vector vector = (Vector)sqlMetaVec.elementAt(sqlMetaIndex);
            	String xmlPath = appPath + cwUtils.SLASH + (String)vector.elementAt(0);
            	if (xmlPath != null) 
            	    xmlPath = xmlPath.replaceAll("\\\\", "\\" + cwUtils.SLASH);
                String s = getColValue(xmlPath, cwUtils.ENC_UTF);
                resultset = statement.executeQuery(getReadSQL(vector));
                resultsetmetadata = resultset.getMetaData();
                int i = resultsetmetadata.getColumnType(1);
                if(s == null || s.length() == 0)
                {
                    BufferedWriter bufferedwriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(xmlPath), cwUtils.ENC_UTF));
                    int k;
                    for(k = 0; resultset.next(); k++)
                    {
                        switch(i)
                        {
                        case -10: 
                        case 2005: 
                            String s2 = cwSQL.getClobValue(resultset, (String)vector.elementAt(2));
                            if(s2 != null)
                                bufferedwriter.write(s2);
                            else
                                bufferedwriter.write("<NULL>");
                            break;

                        case 2: // '\002'
                        case 3: // '\003'
                        case 4: // '\004'
                            int l = resultset.getInt((String)vector.elementAt(2));
                            if(!resultset.wasNull())
                               bufferedwriter.write(Integer.toString(l)); 
                            else
                                bufferedwriter.write("<NULL>");
                            break;

                        default:
                            String s3 = resultset.getString((String)vector.elementAt(2));
                            if(s3 != null)
                                bufferedwriter.write(s3);
                            else
                                bufferedwriter.write("<NULL>");
                            break;
                        }
                        bufferedwriter.newLine();
                    }

                    bufferedwriter.close();
                } else
                {
                    int j = 0;
                    switch(i)
                    {
                    case -10: 
                    case 2005: 
                        String s1 = (String)vector.elementAt(1);
                        String args1[] = {
                            (String)vector.elementAt(2)
                        };
                        String args2[] = {
                            s
                        };
                        String s4 = (String)vector.elementAt(3);
                        j = cwSQL.updateClobFields(con, s1, args1, args2, s4);
                        break;
                    case 2011: 
                        String s11 = (String)vector.elementAt(1);
                        String args11[] = {
                            (String)vector.elementAt(2)
                        };
                        String args21[] = {
                            s
                        };
                        String s41 = (String)vector.elementAt(3);
                        j = cwSQL.updateClobFields(con, s11, args11, args21, s41);
                        break;

                    default:
                        PreparedStatement preparedstatement = con.prepareStatement(getUpdateSQL(vector));
                        preparedstatement.setString(1, s);
                        j = preparedstatement.executeUpdate();
                        preparedstatement.close();
                        break;
                    }
                    con.commit();
                }
            }
        }
		catch(SQLException sqlexception)
        {
            sqlexception.printStackTrace();
            CommonLog.error("SQL error: " + sqlexception.getMessage());
            try
            {
                con.rollback();
                con.close();
            }
            catch(SQLException sqlexception1)
            {
            	CommonLog.error("SQL rollback error: " + sqlexception1.getMessage());
            }
        }
        finally {
        	try
            {
        		if(statement != null) {
             		statement.close();
             	}
             	if(con != null) {
             		con.close();
             	}
            }
            catch(SQLException sqlexception1)
            {
            	CommonLog.error("SQL rollback error: " + sqlexception1.getMessage());
            }
        }
	}
	
	private static Vector getSqlMetaVec(String filePath, String enc)
	throws IOException
	{
		Vector sqlMetaVec = null;
	    File file = new File(filePath);
	    if(!file.exists()) {
	    	return null;
	    }
	    int i = (int)file.length();
	    if(i <= 0) {
	    	return null;
	    }
	    BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(cwUtils.openUTF8FileStream(file), enc), i);
	    sqlMetaVec = new Vector();
	    int line_num = 0;
	    for(String s2 = null; (s2 = bufferedreader.readLine()) != null; ){
	    	try {
	    		String[] sqlArgs = splitToString(s2, ++line_num);
	    		if(sqlArgs != null) {
	    			Vector args_vec = cwUtils.String2vector(sqlArgs);
	    			sqlMetaVec.add(args_vec);
	    		}
	    	} catch (cwException e) {
	    		CommonLog.error(e.getMessage(),e);
	    	}
		}
	    bufferedreader.close();
	    return sqlMetaVec;
	}
	
	private static String getColValue(String s, String enc)
    throws IOException
	{
	    String s1 = null;
	    File file = new File(s);
	    if(!file.exists())
	        return s1;
	    int i = (int)file.length();
	    if(i <= 0)
	        return s1;
	    StringBuffer stringbuffer = new StringBuffer(i);
	    BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(new FileInputStream(file), enc), i);
	    for(String s2 = null; (s2 = bufferedreader.readLine()) != null;)
	        stringbuffer.append(s2);
	
	    bufferedreader.close();
	    s1 = stringbuffer.toString();
	    return s1;
	}
	
	private static String getReadSQL(Vector vector)
    {
        StringBuffer stringbuffer = new StringBuffer();
        stringbuffer.append(" SELECT ").append(vector.elementAt(2)).append(" FROM ").append(vector.elementAt(1)).append(" WHERE ").append(vector.elementAt(3));
        return stringbuffer.toString();
    }
	
	private static String getUpdateSQL(Vector vector)
    {
        StringBuffer stringbuffer = new StringBuffer();
        stringbuffer.append(" UPDATE ").append(vector.elementAt(1)).append(" SET ").append(vector.elementAt(2)).append(" = ? ").append(" WHERE ").append(vector.elementAt(3));
        return stringbuffer.toString();
    }
	
	private static String[] splitToString(String in, int line_num) throws cwException {
		boolean isOk = true;
		in = in.replaceAll("\\s", DELIMITER_BLANK).trim();
    	if (in != null && in.length() == 0) {
    		return null;
    	}

    	String[] result_arr = new String[TARGET_ARR_LENGHT];
		int delimiterIndex = 0;
		for(int i = 0; i < TARGET_ARR_LENGHT; i++) {
			if(i < TARGET_ARR_LENGHT - 1) {
				delimiterIndex = in.indexOf(DELIMITER_BLANK);
				if(delimiterIndex > 0) {
					result_arr[i] = in.substring(0, delimiterIndex);
					in = in.substring(delimiterIndex).trim();
				} else {
					isOk = false;
					break;
				}
			} else {
				result_arr[i] = in;
			}
		}
		if(!isOk) {
			throw new cwException("Updxml.ini is invalid at line " + line_num);
		}
		return result_arr;
	}
}
