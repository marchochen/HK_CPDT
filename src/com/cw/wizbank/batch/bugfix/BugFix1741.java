package com.cw.wizbank.batch.bugfix;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.NamingException;

import com.cw.wizbank.batch.batchUtil.BatchUtils;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cwn.wizbank.utils.CommonLog;

/**
 * This is to fix the data caused by the bug (as in class name)
 * Please refer to bugzilla for details.
 */
public class BugFix1741 {
    private static final String REC_TO_FIX_SQL = "select itm_id, itm_send_enroll_email_ind, itm_xml "
                                               + "from aeItemType, aeItem "
                                               + "where ity_apply_ind = 1 "
                                               + "and ity_id = itm_type "
                                               + "and ity_run_ind = itm_run_ind "
                                               + "and itm_send_enroll_email_ind is not null ";
    private static final String ELE_BEG = "<field21>";
    private static final String ELE_END = "</field21>";
    private static final String ELE_TRUE  = "<field21 id=\"1\">true</field21>";
    private static final String ELE_FALSE = "<field21 id=\"2\">false</field21>";
    private static final String FIX_TABLE = "aeItem";
    private static final String FIX_COL = "itm_xml";
    private static final String FIX_COND = "itm_id = ";
    
    Connection con;
    
    public static void main(String[] argv) {
        try {
            Connection con = BatchUtils.openDB(argv[0]);
            BugFix1741 curObj = new BugFix1741(con);
            try {
                //System.out.println(curObj.getClass().getName() + " begins.");
                curObj.fix();
                con.commit();
                //System.out.println(curObj.getClass().getName() + " finishes.");
            } catch (SQLException e) {
                CommonLog.error(e.getMessage(),e);
                con.rollback();
            }
        }
        catch (cwException e) {
            CommonLog.error(e.getMessage(),e);
        }
        catch (SQLException e) {
            CommonLog.error(e.getMessage(),e);
        }
        catch (IOException e) {
            CommonLog.error(e.getMessage(),e);
        } catch (NamingException e) {
			CommonLog.error(e.getMessage(),e);
		} catch (InstantiationException e) {
			CommonLog.error(e.getMessage(),e);
		} catch (IllegalAccessException e) {
			CommonLog.error(e.getMessage(),e);
		} catch (ClassNotFoundException e) {
			CommonLog.error(e.getMessage(),e);
		}
    }
    
    public BugFix1741(Connection inCon) {
        this.con = inCon;
    }
    
    void fix() throws SQLException {
        long itm_id;
        boolean itm_send_enroll_email_ind;
        String itm_xml;
        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int idx = 1;
        
        try {
            stmt = con.prepareStatement(REC_TO_FIX_SQL);
            rs = stmt.executeQuery();
            while (rs.next()) {
                idx = 1;
                itm_id = rs.getLong(idx++);
                itm_send_enroll_email_ind = rs.getBoolean(idx++);
                itm_xml = cwSQL.getClobValue(rs, FIX_COL);
            
                if (itm_xml != null) {
                    int begIdx = itm_xml.indexOf(ELE_BEG);
                    int endIdx = itm_xml.lastIndexOf(ELE_END);
                    if (begIdx >= 0 && endIdx >= 0 && begIdx < endIdx) {
                        StringBuffer result = new StringBuffer();
                        result.append(itm_xml.substring(0, begIdx)).append(ELE_BEG);
                        if (itm_send_enroll_email_ind) {
                            result.append(ELE_TRUE);
                        } else {
                            result.append(ELE_FALSE);
                        }
                        result.append(itm_xml.substring(endIdx));
                        itm_xml = result.toString();
                
                        cwSQL.updateClobFields(con, FIX_TABLE, new String[] {FIX_COL}, new String[] {itm_xml}, FIX_COND + String.valueOf(itm_id));
                        CommonLog.info("itm_id=" + itm_id + " has been updated.");
                    } else {
                    	CommonLog.error("itm_id=" + itm_id + " has not been updated as its itm_xml is incorrect.");
                    	CommonLog.error(String.valueOf(itm_send_enroll_email_ind));
                    	CommonLog.error(itm_xml);
                    }
                }
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }
}
